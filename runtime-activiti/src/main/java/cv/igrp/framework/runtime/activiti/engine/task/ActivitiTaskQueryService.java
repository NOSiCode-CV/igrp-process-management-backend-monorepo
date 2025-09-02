package cv.igrp.framework.runtime.activiti.engine.task;

import cv.igrp.framework.runtime.core.engine.task.TaskQueryService;
import cv.igrp.framework.runtime.core.engine.task.model.*;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class ActivitiTaskQueryService implements TaskQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskQueryService.class);

    private final TaskRuntime taskRuntime;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

	public ActivitiTaskQueryService(TaskRuntime taskRuntime, TaskService taskService, HistoryService historyService, RuntimeService runtimeService, RepositoryService repositoryService) {
        this.taskRuntime = taskRuntime;
        this.taskService = taskService;
        this.historyService = historyService;
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
    }

    @Override
    public Optional<TaskInfo> getTask(String taskId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.info("Retrieving task with id: {}", taskId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null)
                return empty();

            LOGGER.debug("Task found: {}", task);

            var taskInfo = new TaskInfo(
                    task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getProcessInstanceId(),
                    task.getTaskDefinitionKey(),
                    task.getAssignee(),
                    task.getOwner(),
                    task.getCreateTime(),
                    task.getDueDate(),
                    task.getPriority(),
                    task.getFormKey()
            );

            return of(taskInfo);

        } catch (Exception e) {
            LOGGER.error("Error getting task with id: {}", taskId, e);
            return empty();
        }
    }

    @Override
    public List<TaskInfo> getActiveTaskInstances(String processInstanceId) {

        Objects.requireNonNull(processInstanceId, "processInstanceId cannot be null");

        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .orderByTaskCreateTime().asc()
                .list()
                .stream()
                .map(task -> new TaskInfo(
                        task.getId(),
                        task.getName(),
                        task.getDescription(),
                        task.getProcessInstanceId(),
                        task.getTaskDefinitionKey(),
                        task.getAssignee(),
                        task.getOwner(),
                        task.getCreateTime(),
                        task.getDueDate(),
                        task.getPriority(),
                        task.getFormKey()
                ))
                .toList();
    }

    @Override
	public List<ProcessTaskInfo> getUserTaskProgress(String processInstanceId) {
		LOGGER.debug("Getting tasks for BPMN progress drawing, processInstanceId: {}", processInstanceId);

		// 1. Get all historic tasks (both completed and running)
		List<HistoricTaskInstance> historicTasks = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();

		Set<String> completedTaskKeys = historicTasks.stream()
				.filter(t -> t.getEndTime() != null)
				.map(HistoricTaskInstance::getTaskDefinitionKey)
				.collect(Collectors.toSet());

		Set<String> currentTaskKeys = historicTasks.stream()
				.filter(t -> t.getEndTime() == null)
				.map(HistoricTaskInstance::getTaskDefinitionKey)
				.collect(Collectors.toSet());

		// 2. Resolve process definition id (works for active or historic instance)
		String processDefinitionId;
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();

		if (instance != null) {
			processDefinitionId = instance.getProcessDefinitionId();
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

		// 3. Walk all tasks in a BPMN model (including subprocesses)
		Collection<FlowElement> flowElements = repositoryService
				.getBpmnModel(processDefinitionId)
				.getMainProcess()
				.getFlowElements();

		List<ProcessTaskInfo> result = new ArrayList<>();
		collectUserTasks(flowElements, result, processInstanceId, completedTaskKeys, currentTaskKeys);

		return result;
	}

	/**
	 * Recursively collect user tasks from process, including subprocesses and call activities.
	 */
	private void collectUserTasks(Collection<FlowElement> flowElements,
								  List<ProcessTaskInfo> result,
								  String processInstanceId,
								  Set<String> completedTaskKeys,
								  Set<String> currentTaskKeys) {

		for (FlowElement element : flowElements) {
			if (element instanceof UserTask userTask) {
				String taskKey = userTask.getId();
				IGRPTaskStatus status = IGRPTaskStatus.PENDING;

				if (completedTaskKeys.contains(taskKey)) {
					status = IGRPTaskStatus.COMPLETED;
				} else if (currentTaskKeys.contains(taskKey)) {
					status = IGRPTaskStatus.CURRENT;
				}

				result.add(new ProcessTaskInfo(
						taskKey,
						userTask.getName(),
						status,
						processInstanceId,
						userTask.getFormKey()
				));
			}
			else if (element instanceof SubProcess subProcess) {
				// Embedded subprocess → recurse
				collectUserTasks(subProcess.getFlowElements(),
						result, processInstanceId, completedTaskKeys, currentTaskKeys);
			}
			else if (element instanceof CallActivity callActivity) {
				// Call Activity → follow called process definition
				String calledElement = callActivity.getCalledElement();
				if (calledElement != null) {
					BpmnModel subModel = repositoryService.getBpmnModel(calledElement);
					if (subModel != null && subModel.getMainProcess() != null) {
						collectUserTasks(subModel.getMainProcess().getFlowElements(),
								result, processInstanceId, completedTaskKeys, currentTaskKeys);
					}
				}
			}
		}
	}

	@Override
    public List<ProcessArtifact> getProcessArtifacts(String processDefinitionKey) {

        LOGGER.debug("Getting tasks for BPMN progress drawing, processDefinitionKey: {}", processDefinitionKey);

        return repositoryService.getBpmnModel(processDefinitionKey)
                .getMainProcess()
                .getFlowElements()
                .stream()
                .filter(element -> element instanceof UserTask)
                .map(ut -> {

                    var userTask = (UserTask) ut;

                    return new ProcessArtifact(
                            userTask.getId(),
                            userTask.getName(),
                            userTask.getFormKey()
                    );

                })
                .toList();
    }

	@Override
	public List<TaskVariableInstance> getTaskVariables(String taskId) {
		List<TaskVariableInstance> runtimeVariables = new ArrayList<>(getRuntimeTaskVariables(taskId));
		List<TaskVariableInstance> historicVariables = new ArrayList<>(getHistoricTaskVariables(taskId));
		runtimeVariables.addAll(historicVariables);
		return runtimeVariables;
	}

	@Override
	public List<TaskVariableInstance> getRuntimeTaskVariables(String taskId) {
		Objects.requireNonNull(taskId, "taskId cannot be null");

		LOGGER.debug("Getting variables for task with id: {}", taskId);

		try {
			var payload = TaskPayloadBuilder.variables()
					.withTaskId(taskId)
					.build();

			var variables = taskRuntime.variables(payload);

			LOGGER.debug("Retrieved {} runtime variables for task id: {}", variables.size(), taskId);

			return variables.stream()
					.map(obj -> new TaskVariableInstance(
							obj.getName(),
							obj.getType(),
							obj.getProcessInstanceId(),
							obj.getTaskId(),
							obj.isTaskVariable(),
							obj.getValue()
					))
					.toList();

		} catch (Exception e) {
			LOGGER.debug("No runtime variables found for task id {} (probably completed). Returning empty list.", taskId);
			return new ArrayList<>();
		}
	}

	public List<TaskVariableInstance> getHistoricTaskVariables(String taskId) {
		Objects.requireNonNull(taskId, "taskId cannot be null");

		LOGGER.debug("Getting historic variables for task with id: {}", taskId);

		List<HistoricVariableInstance> vars =
				historyService.createHistoricVariableInstanceQuery()
						.taskId(taskId)
						.list();

		LOGGER.debug("Retrieved {} historic variables for task id: {}", vars.size(), taskId);

		return vars.stream()
				.map(obj -> new TaskVariableInstance(
						obj.getVariableName(),
						obj.getVariableTypeName(),
						obj.getProcessInstanceId(),
						obj.getTaskId(),
						true,
						obj.getValue()
				))
				.toList();
	}

}
