package cv.igrp.framework.runtime.activiti.engine.activity;

import cv.igrp.framework.runtime.core.engine.activity.ActivityQueryService;
import cv.igrp.framework.runtime.core.engine.activity.model.*;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityStatus.COMPLETED;
import static cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityStatus.CURRENT;


@Service
public class ActivitiActivityQueryService implements ActivityQueryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiActivityQueryService.class);

	private final HistoryService historyService;
	private final RuntimeService runtimeService;
	private final RepositoryService repositoryService;

	public ActivitiActivityQueryService(HistoryService historyService,
										RuntimeService runtimeService,
										RepositoryService repositoryService
	) {
		this.historyService = historyService;
		this.runtimeService = runtimeService;
		this.repositoryService = repositoryService;
	}

	@Override
	public Optional<ActivityInfo> getActivity(String activityId) {
		Objects.requireNonNull(activityId, "activityId cannot be null");
		LOGGER.info("Retrieving activity with id: {}", activityId);

		// Try RUNTIME (activityId = BPMN activity id)
		Execution execution = runtimeService
				.createExecutionQuery()
				.activityId(activityId)
				.singleResult();

		if (execution != null) {
			return Optional.of(new ActivityInfo(
					execution.getId(),
					execution.getActivityId(),
					null,
					execution.getProcessInstanceId(),
					execution.getParentId(),
					execution.getParentProcessInstanceId(),
					execution.isSuspended()
							? IGRPActivityStatus.SUSPENDED
							: CURRENT,
					determineActivityTypeById(execution.getActivityId())
			));
		}

		// Try HISTORY (activityId = historic activity instance id)
		HistoricActivityInstance hai = historyService
				.createHistoricActivityInstanceQuery()
				.activityInstanceId(activityId)
				.singleResult();

		if (hai != null) {
			return Optional.of(new ActivityInfo(
					hai.getId(),
					hai.getActivityId(),
					hai.getTaskId(),
					hai.getProcessInstanceId(),
					hai.getExecutionId(),
					null,
					hai.getEndTime() != null
							? COMPLETED
							: CURRENT,
					determineActivityTypeById(hai.getActivityId())
			));
		}

		return Optional.empty();
	}

	@Override
	public List<ActivityInfo> getActiveActivityInstances(String processInstanceId) {
		Objects.requireNonNull(processInstanceId, "processInstanceId cannot be null");

		return runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId)
				.list()
				.stream()
				.filter(e -> e.getActivityId() != null && !e.getActivityId().isEmpty())
				.map(activity -> new ActivityInfo(
						activity.getId(),
						activity.getActivityId(),
						null,
						activity.getProcessInstanceId(),
						activity.getParentId(),
						activity.getParentProcessInstanceId(),
						activity.isEnded() ? COMPLETED :
								activity.isSuspended() ? IGRPActivityStatus.SUSPENDED : CURRENT,
						determineActivityTypeById(activity.getActivityId())
				))
				.toList();
	}

	@Override
	public List<ProcessTimelineEvent> getActivityTimelineEvents(String processInstanceId) {

		LOGGER.info("Retrieving timeline events for processInstanceId={}", processInstanceId);

		List<ProcessTimelineEvent> timelineEvents = new ArrayList<>();

		// ------------------------------------------------------
		// Historic activities (BACKBONE of the timeline)
		// ------------------------------------------------------
		List<HistoricActivityInstance> activities =
				historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(processInstanceId)
						.orderByHistoricActivityInstanceStartTime()
						.asc()
						.list();

		if (activities.isEmpty()) {
			return timelineEvents;
		}

		// ------------------------------------------------------
		// Resolve process definition and BPMN model (ONCE)
		// ------------------------------------------------------
		String processDefinitionId = activities.getFirst().getProcessDefinitionId();
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);

		// ------------------------------------------------------
		// Historic variables
		// ------------------------------------------------------
		Map<String, List<HistoricVariableInstance>> varsByTaskId =
				historyService.createHistoricVariableInstanceQuery()
						.processInstanceId(processInstanceId)
						.list()
						.stream()
						.filter(v -> v.getTaskId() != null)
						.collect(Collectors.groupingBy(HistoricVariableInstance::getTaskId));

		List<HistoricVariableInstance> processVars =
				historyService.createHistoricVariableInstanceQuery()
						.processInstanceId(processInstanceId)
						.list()
						.stream()
						.filter(v -> v.getTaskId() == null)
						.toList();

		// ------------------------------------------------------
		// Historic tasks
		// ------------------------------------------------------
		Map<String, HistoricTaskInstance> tasksById =
				historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(processInstanceId)
						.list()
						.stream()
						.collect(Collectors.toMap(HistoricTaskInstance::getId, t -> t));

		// ------------------------------------------------------
		// Build tree numbers (recursive)
		// ------------------------------------------------------
		Map<String, String> treeNumbers = new HashMap<>();
		Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
		int startIndex = 1;

		for (FlowElement fe : flowElements) {
			if (fe instanceof StartEvent se) {
				assignTreeNumber(se, String.valueOf(startIndex++), treeNumbers);
			}
		}

		// ------------------------------------------------------
		// Build timeline
		// ------------------------------------------------------
		Set<String> executedActivityIds =
				activities.stream()
						.map(HistoricActivityInstance::getActivityId)
						.collect(Collectors.toSet());

		for (FlowElement element : flowElements) {

			if (!(element instanceof FlowNode)) continue;

			String activityId = element.getId();

			// Find historic instance if executed
			HistoricActivityInstance hai = activities.stream()
					.filter(h -> h.getActivityId().equals(activityId))
					.findFirst()
					.orElse(null);

			Instant start = hai != null && hai.getStartTime() != null ? hai.getStartTime().toInstant() : null;
			Instant end = hai != null && hai.getEndTime() != null ? hai.getEndTime().toInstant() : null;
			Instant cutoff = end != null ? end : Instant.now();

			// -------------------------------
			// Variable SNAPSHOT
			// -------------------------------
			Map<String, Object> snapshotVars = new HashMap<>();
			processVars.stream()
					.filter(v -> v.getCreateTime() != null)
					.filter(v -> v.getCreateTime().toInstant().isBefore(cutoff))
					.forEach(v -> snapshotVars.put(v.getVariableName(), v.getValue()));

			if (hai != null && hai.getTaskId() != null) {
				varsByTaskId.getOrDefault(hai.getTaskId(), List.of())
						.stream()
						.filter(v -> v.getCreateTime() != null)
						.filter(v -> v.getCreateTime().toInstant().isBefore(cutoff))
						.forEach(v -> snapshotVars.put(v.getVariableName(), v.getValue()));
			}

			HistoricTaskInstance task = hai != null ? tasksById.get(hai.getTaskId()) : null;

			// Inside your timeline building loop
			timelineEvents.add(new ProcessTimelineEvent(
					hai != null ? hai.getId() : null,
					activityId,
					resolveActivityName(model, activityId),
					resolveActivityType(model, activityId),
					hai != null ? hai.getExecutionId() : null,
					hai != null ? hai.getTaskId() : null,
					processInstanceId,
					hai != null && end != null ? IGRPActivityStatus.COMPLETED :
							hai != null ? IGRPActivityStatus.CURRENT : IGRPActivityStatus.PENDING,
					start,
					end,
					hai != null ? hai.getDurationInMillis() : null,
					task != null ? task.getAssignee() : null,
					snapshotVars,
					treeNumbers.get(activityId) // set treeNumber here
			));


		}

		return timelineEvents;
	}

	/**
	 * Recursive method to assign tree numbers to each FlowNode
	 */
	private void assignTreeNumber(FlowNode node, String currentNumber, Map<String, String> treeNumbers) {
		if (node == null || treeNumbers.containsKey(node.getId())) return;

		treeNumbers.put(node.getId(), currentNumber);

		List<SequenceFlow> outgoing = node.getOutgoingFlows();
		for (int i = 0; i < outgoing.size(); i++) {
			FlowElement target = outgoing.get(i).getTargetFlowElement();
			if (target instanceof FlowNode fn) {
				assignTreeNumber(fn, currentNumber + "." + (i + 1), treeNumbers);
			}
		}
	}

	private String resolveActivityName(BpmnModel model, String activityId) {
		if (model == null || activityId == null || model.getMainProcess() == null) {
			return activityId;
		}
		FlowElement element = model.getMainProcess().getFlowElement(activityId);
		return element != null && element.getName() != null
				? element.getName()
				: activityId;
	}

	private IGRPActivityType resolveActivityType(BpmnModel model, String activityId) {
		if (model == null || activityId == null || model.getMainProcess() == null) {
			return IGRPActivityType.OTHER;
		}
		FlowElement element = model.getMainProcess().getFlowElement(activityId);
		return element != null
				? determineActivityType(element)
				: IGRPActivityType.OTHER;
	}


	private IGRPActivityType determineActivityType(FlowElement element) {
		if (element instanceof UserTask) return IGRPActivityType.USER_TASK;
		if (element instanceof ServiceTask) return IGRPActivityType.SERVICE_TASK;
		if (element instanceof ScriptTask) return IGRPActivityType.SCRIPT_TASK;
		if (element instanceof ManualTask) return IGRPActivityType.MANUAL_TASK;
		if (element instanceof ReceiveTask) return IGRPActivityType.RECEIVE_TASK;
		if (element instanceof SendTask) return IGRPActivityType.SEND_TASK;
		if (element instanceof BusinessRuleTask) return IGRPActivityType.BUSINESS_RULE_TASK;
		if (element instanceof ExclusiveGateway) return IGRPActivityType.EXCLUSIVE_GATEWAY;
		if (element instanceof ParallelGateway) return IGRPActivityType.PARALLEL_GATEWAY;
		if (element instanceof InclusiveGateway) return IGRPActivityType.INCLUSIVE_GATEWAY;
		if (element instanceof IntermediateCatchEvent) return IGRPActivityType.MESSAGE_INTERMEDIATE_EVENT_CATCH;
		if (element instanceof CallActivity) return IGRPActivityType.CALL_ACTIVITY;
		if (element instanceof SubProcess) return IGRPActivityType.SUB_PROCESS;
		return IGRPActivityType.OTHER;
	}

	private IGRPActivityType determineActivityTypeById(String activityId) {
		if (activityId == null) return IGRPActivityType.OTHER;

		// Try to find the flow element in all deployed process definitions
		List<BpmnModel> models = repositoryService.createProcessDefinitionQuery()
				.list()
				.stream()
				.map(pd -> repositoryService.getBpmnModel(pd.getId()))
				.toList();

		for (BpmnModel model : models) {
			if (model.getMainProcess() != null) {
				FlowElement element = model.getMainProcess().getFlowElement(activityId);
				if (element != null) {
					return determineActivityType(element);
				}
			}
		}

		return IGRPActivityType.OTHER;
	}


	@Override
	public List<ActivityVariableInstance> getActivityVariables(String activityId) {
		List<ActivityVariableInstance> runtimeVariables = getRuntimeActivityVariables(activityId);
		List<ActivityVariableInstance> historicVariables = getHistoricActivityVariables(activityId);

		Map<String, ActivityVariableInstance> variablesMap = new LinkedHashMap<>();
		runtimeVariables.forEach(v -> variablesMap.put(v.name(), v));
		historicVariables.forEach(v -> variablesMap.putIfAbsent(v.name(), v));

		return new ArrayList<>(variablesMap.values());
	}

	@Override
	public List<ActivityVariableInstance> getRuntimeActivityVariables(String activityId) {
		Objects.requireNonNull(activityId, "activityId cannot be null");
		LOGGER.debug("Getting runtime variables for activityId={}", activityId);

		Execution execution = runtimeService.createExecutionQuery()
				.activityId(activityId)
				.singleResult();

		if (execution == null) {
			LOGGER.debug("No running execution found for activity {}, returning empty list", activityId);
			return List.of();
		}

		Map<String, Object> vars = runtimeService.getVariables(execution.getId());
		return vars.entrySet().stream()
				.map(e -> new ActivityVariableInstance(
						e.getKey(),
						e.getValue() != null ? e.getValue().getClass().getSimpleName() : null,
						execution.getProcessInstanceId(),
						activityId,
						true,
						e.getValue()
				))
				.toList();
	}

	@Override
	public List<ActivityVariableInstance> getHistoricActivityVariables(String activityId) {
		Objects.requireNonNull(activityId);

		List<HistoricActivityInstance> historicActivities = historyService
				.createHistoricActivityInstanceQuery()
				.activityId(activityId)
				.list();

		List<ActivityVariableInstance> result = new ArrayList<>();

		for (HistoricActivityInstance hist : historicActivities) {
			// Task variables
			if (hist.getTaskId() != null) {
				List<HistoricVariableInstance> taskVars = historyService.createHistoricVariableInstanceQuery()
						.taskId(hist.getTaskId())
						.list();

				for (HistoricVariableInstance v : taskVars) {
					result.add(new ActivityVariableInstance(
							v.getVariableTypeName(),
							v.getVariableName(),
							hist.getProcessInstanceId(),
							activityId,
							true,
							v.getValue()
					));
				}
			}

			// Execution variables (automatic / non-user tasks)
			List<HistoricVariableInstance> execVars = historyService.createHistoricVariableInstanceQuery()
					.executionId(hist.getExecutionId())
					.list();

			for (HistoricVariableInstance v : execVars) {
				result.add(new ActivityVariableInstance(
						v.getVariableTypeName(),
						v.getVariableName(),
						hist.getProcessInstanceId(),
						activityId,
						true,
						v.getValue()
				));
			}
		}

		return result;
	}
}
