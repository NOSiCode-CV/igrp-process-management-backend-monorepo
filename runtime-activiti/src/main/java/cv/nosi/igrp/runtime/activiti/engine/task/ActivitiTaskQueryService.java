package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskQueryService;
import cv.nosi.igrp.runtime.core.engine.task.model.IGRPTaskStatus;
import cv.nosi.igrp.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public List<TaskVariableInstance> getTaskVariables(String taskId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.debug("Getting variables for task with id: {}", taskId);

        var payload = TaskPayloadBuilder.variables()
                .withTaskId(taskId)
                .build();

        var variables = taskRuntime.variables(payload);

        LOGGER.debug("Retrieved {} variables for task id: {}", variables.size(), taskId);

        var result = variables
                .stream()
                .map(obj -> new TaskVariableInstance(
                        obj.getName(),
                        obj.getType(),
                        obj.getProcessInstanceId(),
                        obj.getTaskId(),
                        obj.isTaskVariable(),
                        obj.getValue()
                ))
                .toList();

        LOGGER.debug("Successfully retrieved {} variables for task with id: {}", result.size(), taskId);

        return result;
    }

    @Override
    public List<ProcessTaskInfo> getUserTaskProgress(String processInstanceId) {

        LOGGER.debug("Getting all tasks for process instance with id: {}", processInstanceId);

        var completedTaskKeys = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list()
                .stream()
                .map(HistoricTaskInstance::getTaskDefinitionKey)
                .collect(Collectors.toSet());

        var currentTaskKeys = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list()
                .stream()
                .map(Task::getTaskDefinitionKey)
                .collect(Collectors.toSet());

        var instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (instance == null) {
            LOGGER.warn("Process instance with ID {} not found", processInstanceId);
            return List.of();
        }

        var flowElements = repositoryService.getBpmnModel(instance.getProcessDefinitionId())
                .getMainProcess()
                .getFlowElements();

        var result = new ArrayList<ProcessTaskInfo>();

        flowElements.forEach(element -> {

            if (element instanceof UserTask userTask) {

                var taskKey = userTask.getId();

                var status = IGRPTaskStatus.PENDING;

                if (completedTaskKeys.contains(taskKey))
                    status = IGRPTaskStatus.COMPLETED;
                else if (currentTaskKeys.contains(taskKey))
                    status = IGRPTaskStatus.CURRENT;

                result.add(new ProcessTaskInfo(
                        taskKey,
                        userTask.getName(),
                        status,
                        processInstanceId,
                        userTask.getFormKey()
                ));
            }
        });

        return result;
    }
}
