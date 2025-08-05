package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskQueryService;
import cv.nosi.igrp.runtime.core.engine.task.model.IGRPTaskStatus;
import cv.nosi.igrp.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class ActivitiTaskQueryService implements TaskQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskQueryService.class);

    private final TaskRuntime taskRuntime;
    private final TaskService taskService;
    private final HistoryService historyService;

    public ActivitiTaskQueryService(TaskRuntime taskRuntime, TaskService taskService, HistoryService historyService) {
        this.taskRuntime = taskRuntime;
        this.taskService = taskService;
        this.historyService = historyService;
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

        LOGGER.info("Successfully retrieved {} variables for task with id: {}", result.size(), taskId);

        return result;
    }

    @Override
    public List<ProcessTaskInfo> getAllTasks(String processInstanceId) {

        LOGGER.info("Getting all tasks for process instance with id: {}", processInstanceId);

        var result = new ArrayList<ProcessTaskInfo>();

        var activeTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        for (var task : activeTasks) {

            var status = IGRPTaskStatus.ASSIGNED;

            if (task.isSuspended())
                status = IGRPTaskStatus.SUSPENDED;
            else if (task.getAssignee() == null)
                status = IGRPTaskStatus.CREATED;

            result.add(new ProcessTaskInfo(
                    task.getTaskDefinitionKey(),
                    task.getName(),
                    status,
                    task.getProcessInstanceId(),
                    task.getFormKey()
            ));
        }

        var historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();

        for (var task : historicTasks) {

            var status = IGRPTaskStatus.COMPLETED;

            // TODO 05/08/2025 10:31 set this default messages when deleting or cancelling a task

            if ("deleted".equalsIgnoreCase(task.getDeleteReason()))
                status = IGRPTaskStatus.DELETED;
            else if ("cancelled".equalsIgnoreCase(task.getDeleteReason()))
                status = IGRPTaskStatus.CANCELLED;

            result.add(new ProcessTaskInfo(
                    task.getTaskDefinitionKey(),
                    task.getName(),
                    status,
                    task.getProcessInstanceId(),
                    task.getFormKey()
            ));
        }

        return result;
    }
}
