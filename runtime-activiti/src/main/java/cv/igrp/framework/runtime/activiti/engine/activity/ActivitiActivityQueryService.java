package cv.igrp.framework.runtime.activiti.engine.activity;

import cv.igrp.framework.runtime.core.engine.activity.ActivityQueryService;
import cv.igrp.framework.runtime.core.engine.activity.model.*;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class ActivitiActivityQueryService implements ActivityQueryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiActivityQueryService.class);

	private final HistoryService historyService;
	private final RuntimeService runtimeService;
	private final RepositoryService repositoryService;
	private final TaskService taskService;

	public ActivitiActivityQueryService(HistoryService historyService,
										RuntimeService runtimeService,
										RepositoryService repositoryService,
										TaskService taskService) {
		this.historyService = historyService;
		this.runtimeService = runtimeService;
		this.repositoryService = repositoryService;
		this.taskService = taskService;
	}

	@Override
	public Optional<ActivityInfo> getActivity(String activityId) {
		Objects.requireNonNull(activityId, "activityId cannot be null");
		LOGGER.info("Retrieving activity with id: {}", activityId);

		try {
			Execution execution = runtimeService.createExecutionQuery().activityId(activityId).singleResult();
			if (execution == null) return empty();

			ActivityInfo activityInfo = new ActivityInfo(
					execution.getId(),
					execution.getActivityId(),
					null,
					execution.getProcessInstanceId(),
					execution.getParentId(),
					execution.getParentProcessInstanceId(),
					execution.isEnded() ? IGRPActivityStatus.COMPLETED :
							execution.isSuspended() ? IGRPActivityStatus.SUSPENDED : IGRPActivityStatus.CURRENT,
					determineActivityTypeById(execution.getActivityId())
			);
			return of(activityInfo);

		} catch (Exception e) {
			LOGGER.error("Error getting activity with id: {}", activityId, e);
			return empty();
		}
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
						activity.isEnded() ? IGRPActivityStatus.COMPLETED :
								activity.isSuspended() ? IGRPActivityStatus.SUSPENDED : IGRPActivityStatus.CURRENT,
						determineActivityTypeById(activity.getActivityId())
				))
				.toList();
	}

	@Override
	public List<ProcessActivityInfo> getActivityProgress(String processInstanceId) {

		LOGGER.debug("Getting activities for BPMN progress drawing, processInstanceId: {}", processInstanceId);

		// 1. Get all historic tasks
		List<HistoricTaskInstance> historicTasks = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();

		Map<String, List<HistoricTaskInstance>> tasksByKey = historicTasks.stream()
				.collect(Collectors.groupingBy(HistoricTaskInstance::getTaskDefinitionKey));

		Set<String> completedTaskKeys = historicTasks.stream()
				.filter(t -> t.getEndTime() != null)
				.map(HistoricTaskInstance::getTaskDefinitionKey)
				.collect(Collectors.toSet());

		Set<String> currentTaskKeys = historicTasks.stream()
				.filter(t -> t.getEndTime() == null)
				.map(HistoricTaskInstance::getTaskDefinitionKey)
				.collect(Collectors.toSet());

		// 2. Get historic activity executions (ALL BPMN activities)
		Map<String, List<HistoricActivityInstance>> activitiesByKey =
				historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(processInstanceId)
						.list()
						.stream()
						.collect(Collectors.groupingBy(HistoricActivityInstance::getActivityId));

		// 3. Resolve process definition id
		String processDefinitionId;
		ProcessInstance runtimeInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();

		if (runtimeInstance != null) {
			processDefinitionId = runtimeInstance.getProcessDefinitionId();
		} else {
			HistoricProcessInstance historicInstance = historyService
					.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult();

			if (historicInstance == null) {
				LOGGER.warn("No process instance found with ID {}", processInstanceId);
				return List.of();
			}
			processDefinitionId = historicInstance.getProcessDefinitionId();
		}

		// 4. Walk BPMN model
		Collection<FlowElement> flowElements = repositoryService
				.getBpmnModel(processDefinitionId)
				.getMainProcess()
				.getFlowElements();

		List<ProcessActivityInfo> result = new ArrayList<>();

		collectAllActivities(
				flowElements,
				result,
				processInstanceId,
				completedTaskKeys,
				currentTaskKeys,
				tasksByKey,
				activitiesByKey
		);

		return result;
	}

	private void collectAllActivities(
			Collection<FlowElement> flowElements,
			List<ProcessActivityInfo> result,
			String processInstanceId,
			Set<String> completedTaskKeys,
			Set<String> currentTaskKeys,
			Map<String, List<HistoricTaskInstance>> tasksByKey,
			Map<String, List<HistoricActivityInstance>> activitiesByKey
	) {

		for (FlowElement element : flowElements) {

			String activityKey = element.getId();
			String activityName = element.getName();

			IGRPActivityStatus status = computeStatus(activityKey, completedTaskKeys, currentTaskKeys);
			IGRPActivityType type = determineActivityType(element);

			// -------------------------------
			// Execution timing (ALL activities)
			// -------------------------------
			List<HistoricActivityInstance> activityHistory =
					activitiesByKey.getOrDefault(activityKey, List.of());

			// If multiple executions (loops, multi-instance), take last execution
			HistoricActivityInstance lastExecution =
					activityHistory.isEmpty() ? null : activityHistory.get(activityHistory.size() - 1);

			Instant startTime = lastExecution != null && lastExecution.getStartTime() != null
					? lastExecution.getStartTime().toInstant()
					: null;

			Instant endTime = lastExecution != null && lastExecution.getEndTime() != null
					? lastExecution.getEndTime().toInstant()
					: null;

			Long durationMillis = lastExecution != null
					? lastExecution.getDurationInMillis()
					: null;

			// -------------------------------
			// USER TASK
			// -------------------------------
			if (element instanceof UserTask) {

				List<HistoricTaskInstance> tasks =
						tasksByKey.getOrDefault(activityKey, List.of());

				String assignee = null;
				Set<String> candidateUsers = new HashSet<>();
				Set<String> candidateGroups = new HashSet<>();

				for (HistoricTaskInstance task : tasks) {

					if (assignee == null) {
						assignee = task.getAssignee();
					}

					List<HistoricIdentityLink> identityLinks =
							historyService.getHistoricIdentityLinksForTask(task.getId());

					for (HistoricIdentityLink il : identityLinks) {
						if ("candidate".equals(il.getType())) {
							if (il.getUserId() != null) {
								candidateUsers.add(il.getUserId());
							}
							if (il.getGroupId() != null) {
								candidateGroups.add(il.getGroupId());
							}
						}
					}
				}

				result.add(new ProcessActivityInfo(
						activityKey,
						activityName,
						status,
						type,
						processInstanceId,
						assignee,
						candidateUsers.isEmpty() ? null : candidateUsers,
						candidateGroups.isEmpty() ? null : candidateGroups,
						startTime,
						endTime,
						durationMillis
				));

			}
			// -------------------------------
			// NON-USER TASK
			// -------------------------------
			else {

				result.add(new ProcessActivityInfo(
						activityKey,
						activityName,
						status,
						type,
						processInstanceId,
						null,
						null,
						null,
						startTime,
						endTime,
						durationMillis
				));
			}

			// -------------------------------
			// SUBPROCESS (recursive)
			// -------------------------------
			if (element instanceof SubProcess subProcess) {
				collectAllActivities(
						subProcess.getFlowElements(),
						result,
						processInstanceId,
						completedTaskKeys,
						currentTaskKeys,
						tasksByKey,
						activitiesByKey
				);
			}

			// -------------------------------
			// CALL ACTIVITY (recursive)
			// -------------------------------
			if (element instanceof CallActivity callActivity) {

				String calledElement = callActivity.getCalledElement();

				if (calledElement != null) {

					BpmnModel subModel = repositoryService.getBpmnModel(calledElement);

					if (subModel != null && subModel.getMainProcess() != null) {

						collectAllActivities(
								subModel.getMainProcess().getFlowElements(),
								result,
								processInstanceId,
								completedTaskKeys,
								currentTaskKeys,
								tasksByKey,
								activitiesByKey
						);
					}
				}
			}
		}
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

	private IGRPActivityStatus computeStatus(String activityKey,
											 Set<String> completed,
											 Set<String> current) {
		if (completed.contains(activityKey)) return IGRPActivityStatus.COMPLETED;
		if (current.contains(activityKey)) return IGRPActivityStatus.CURRENT;
		return IGRPActivityStatus.PENDING;
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
