package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskActionService;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ActivitiTaskActionService implements TaskActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskActionService.class);

    private final TaskRuntime taskRuntime;
    private final TaskService taskService;
    private final RuntimeService runtimeService;

    public ActivitiTaskActionService(TaskRuntime taskRuntime, TaskService taskService, RuntimeService runtimeService) {
        this.taskRuntime = taskRuntime;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables, String userId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.debug("Completing task with id: {}, user: {}", taskId, userId);

        LOGGER.debug("Variables count: {}, keys: {}",
                variables != null ? variables.size() : 0,
                variables != null ? variables.keySet() : "null");

        var variablesPayload = variables != null ? new HashMap<>(variables) : new HashMap<String, Object>();

        var payload = TaskPayloadBuilder.complete()
                .withTaskId(taskId)
                .withVariables(variablesPayload)
                .build();

        taskRuntime.complete(payload);

        LOGGER.info("Task with id: {} successfully completed by user: {}", taskId, userId);
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.info("Setting variables for task with id: {}", taskId);

        LOGGER.debug("Variables count: {}, keys: {}",
                variables != null ? variables.size() : 0,
                variables != null ? variables.keySet() : "null");

        if (variables != null && !variables.isEmpty()) {
            runtimeService.setVariables(taskId, variables);
            LOGGER.info("Variables successfully set for task with id: {}", taskId);
            return;
        }

        LOGGER.debug("No variables to set for task id: {}, skipping operation", taskId);
    }


    @Override
    public boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason) {

        LOGGER.info("Delegating task id: {} from owner: {} to user: {}", taskId, ownerUserId, delegateUserId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null || !Objects.equals(task.getOwner(), ownerUserId)) {
                LOGGER.warn("Cannot delegate task: Task {} does not exist or owner mismatch", taskId);
                return false;
            }

            taskService.setOwner(taskId, ownerUserId);
            taskService.delegateTask(taskId, delegateUserId);

            if (reason != null)
                taskService.addComment(taskId, task.getProcessInstanceId(), "Delegated: " + reason);

            LOGGER.info("Task {} successfully delegated from {} to {}", taskId, ownerUserId, delegateUserId);

            return true;

        } catch (Exception e) {
            LOGGER.error("Error delegating task {} from {} to {}", taskId, ownerUserId, delegateUserId, e);
            return false;
        }
    }

    @Override
    public boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment) {

        LOGGER.info("Resolving delegated task id: {} by delegate user: {}", taskId, delegateUserId);

        try {
            var task = taskService.createTaskQuery().taskId(taskId).taskAssignee(delegateUserId).singleResult();
            if (task == null) {
                LOGGER.warn("Cannot resolve task: Task {} does not exist or delegate user mismatch", taskId);
                return false;
            }

            if (comment != null) {
                taskService.addComment(taskId, task.getProcessInstanceId(), "Resolved: " + comment);
            }

            taskService.resolveTask(taskId);

            LOGGER.info("Task {} successfully resolved by delegate user {}", taskId, delegateUserId);

            return true;

        } catch (Exception e) {
            LOGGER.error("Error resolving delegated task {} by user {}", taskId, delegateUserId, e);
            return false;
        }
    }

    @Override
    public boolean setTaskDueDate(String taskId, long dueDate) {

        LOGGER.info("Setting due date {} for task id: {}", new Date(dueDate), taskId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                LOGGER.warn("Cannot set due date: Task {} does not exist", taskId);
                return false;
            }

            task.setDueDate(new Date(dueDate));
            taskService.saveTask(task);

            LOGGER.info("Successfully set due date for task {}", taskId);

            return true;

        } catch (Exception e) {
            LOGGER.error("Error setting due date for task {}", taskId, e);
            return false;
        }
    }

    @Override
    public void assignTask(String taskId, String userId, String reason) {

        LOGGER.info("Assigning task id: {} to user: {}", taskId, userId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                LOGGER.warn("Cannot assign task: Task {} does not exist", taskId);
                return;
            }

            taskService.setAssignee(taskId, userId);
            taskService.addComment(taskId, task.getProcessInstanceId(), "Assigned to " + userId + ": " + reason);

            LOGGER.info("Task {} successfully assigned to user {}", taskId, userId);

        } catch (Exception e) {
            LOGGER.error("Error assigning task {} to user {}", taskId, userId, e);
        }
    }

    @Override
    public void claimTask(String taskId, String userId) {

        LOGGER.info("Claiming task with id: {}", taskId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                LOGGER.warn("Cannot claim task: Task {} does not exist", taskId);
                return;
            }

            taskService.claim(taskId, userId);

            LOGGER.info("Task {} successfully claimed by user {}", taskId, userId);

        } catch (Exception e) {
            LOGGER.error("Error claiming task {}", taskId, e);
        }
    }

    @Override
    public void unclaimTask(String taskId) {

        LOGGER.info("Unclaiming task with id: {}", taskId);

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                LOGGER.warn("Cannot unclaim task: Task {} does not exist", taskId);
                return;
            }

            taskService.setAssignee(taskId, null);

            LOGGER.info("Task {} successfully unclaimed", taskId);

        } catch (Exception e) {
            LOGGER.error("Error unclaiming task {}", taskId, e);
        }
    }
}
