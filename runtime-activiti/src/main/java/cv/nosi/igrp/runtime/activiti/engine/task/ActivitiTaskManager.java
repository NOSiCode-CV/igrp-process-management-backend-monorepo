package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskManager;
import cv.nosi.igrp.runtime.core.engine.task.model.IGRPTaskStatus;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.*;

@Service
public class ActivitiTaskManager implements TaskManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskManager.class);

    private final TaskRuntime taskRuntime;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public ActivitiTaskManager(TaskRuntime taskRuntime, TaskService taskService, RuntimeService runtimeService, HistoryService historyService) {
        this.taskRuntime = taskRuntime;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    @Override
    public String createTask(String processInstanceId, String taskDefinitionKey, String taskName, String assignee, Map<String, Object> variables) {

        LOGGER.info("Creating task for process instance: {}, definition key: {}, name: {}, assignee: {}", processInstanceId, taskDefinitionKey, taskName, assignee);

        var payload = TaskPayloadBuilder.create()
                .withName(taskName)
                .withAssignee(assignee)
                .withParentTaskId(taskDefinitionKey)
                .build();

        var task = taskRuntime.create(payload);

        LOGGER.debug("Task created successfully: {}", task);

        return task.getId();
    }

    @Override
    public Optional<TaskInfo> getTask(String taskId) {

        LOGGER.info("Retrieving task with id: {}", taskId);

        Objects.requireNonNull(taskId, "taskId cannot be null");

        try {

            var task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null)
                return empty();

            LOGGER.debug("Task found: {}", task);

            var taskInfo = new TaskInfo();
            taskInfo.setId(task.getId());
            taskInfo.setName(task.getName());
            taskInfo.setDescription(task.getDescription());
            taskInfo.setProcessInstanceId(task.getProcessInstanceId());
            taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            taskInfo.setAssignee(task.getAssignee());
            taskInfo.setOwner(task.getOwner());
            taskInfo.setCreatedTime(task.getCreateTime().getTime());
            ofNullable(task.getDueDate()).map(Date::getTime).ifPresent(taskInfo::setDueDate);
            taskInfo.setPriority(task.getPriority());
            taskInfo.setFormKey(task.getFormKey());

            return of(taskInfo);

        } catch (Exception e) {
            LOGGER.info("Task with id: {} not found or error occurred", taskId);
            LOGGER.debug("Error getting task with id: {}", taskId, e);
            return empty();
        }
    }

    @Override
    public List<TaskInfo> listTasks(TaskFilter filter) {

        LOGGER.info("Listing tasks with filter: {}", filter);

        final var status = filter.getStatus();

        // If COMPLETED or CANCELLED, use historic task query
        if (status == IGRPTaskStatus.COMPLETED || status == IGRPTaskStatus.CANCELLED) {

            LOGGER.debug("Processing {} tasks query using history service", status);

            var query = historyService.createHistoricTaskInstanceQuery();

            LOGGER.debug("Applying filter parameters to historic query");

            ofNullable(filter.getProcessInstanceId()).ifPresent(id -> {
                LOGGER.debug("Filtering by process instance id: {}", id);
                query.processInstanceId(id);
            });

            ofNullable(filter.getTaskDefinitionKey()).ifPresent(key -> {
                LOGGER.debug("Filtering by task definition key: {}", key);
                query.taskDefinitionKey(key);
            });

            ofNullable(filter.getAssignee()).ifPresent(assignee -> {
                LOGGER.debug("Filtering by assignee: {}", assignee);
                query.taskAssignee(assignee);
            });

            ofNullable(filter.getCreatedAfter()).ifPresent(time -> {
                LOGGER.debug("Filtering by created after: {}", new Date(time));
                query.taskCreatedAfter(new Date(time));
            });

            ofNullable(filter.getCreatedBefore()).ifPresent(time -> {
                LOGGER.debug("Filtering by created before: {}", new Date(time));
                query.taskCreatedBefore(new Date(time));
            });

            if (status == IGRPTaskStatus.COMPLETED) {
                LOGGER.debug("Filtering for completed tasks");
                query.finished();
            } else {
                LOGGER.debug("Filtering for cancelled tasks with delete reason: deleted");
                query.taskDeleteReason("deleted"); // ⚠️ Confirm if this delete reason matches your engine config
            }

            LOGGER.debug("Executing historic query and mapping results");
            var tasks = query.list();
            LOGGER.info("Found {} historic tasks matching the filter criteria", tasks.size());

            return tasks
                    .stream()
                    .map(task -> {
                        var taskInfo = new TaskInfo();
                        taskInfo.setId(task.getId());
                        taskInfo.setName(task.getName());
                        taskInfo.setDescription(task.getDescription());
                        taskInfo.setProcessInstanceId(task.getProcessInstanceId());
                        taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
                        taskInfo.setAssignee(task.getAssignee());
                        taskInfo.setOwner(task.getOwner());
                        taskInfo.setCreatedTime(task.getCreateTime().getTime());
                        taskInfo.setDueDate(ofNullable(task.getDueDate()).map(Date::getTime).orElse(null));
                        taskInfo.setPriority(task.getPriority());
                        taskInfo.setFormKey(task.getFormKey());
                        return taskInfo;
                    })
                    .toList();
        }

        LOGGER.debug("Processing active tasks query using task runtime");

        var builder = TaskPayloadBuilder.tasks();

        ofNullable(filter.getProcessInstanceId()).ifPresent(id -> {
            LOGGER.debug("Filtering by process instance id: {}", id);
            builder.withProcessInstanceId(id);
        });

        ofNullable(filter.getTaskDefinitionKey()).ifPresent(key -> {
            LOGGER.debug("Filtering by task definition key (as parentTaskId): {}", key);
            builder.withParentTaskId(key); // ⚠️ Note: parentTaskId vs taskDefinitionKey
        });

        if (filter.isUnassigned()) {
            LOGGER.debug("Filtering for unassigned tasks");
            builder.withAssignee(null);
        } else {
            ofNullable(filter.getAssignee()).ifPresent(assignee -> {
                LOGGER.debug("Filtering by assignee: {}", assignee);
                builder.withAssignee(assignee);
            });
        }

        var payload = builder.build();

        var startIndex = ofNullable(filter.getStartIndex()).orElse(0);
        var maxResults = ofNullable(filter.getMaxResults()).orElse(50);
        LOGGER.debug("Pagination: startIndex={}, maxResults={}", startIndex, maxResults);

        var page = taskRuntime.tasks(Pageable.of(startIndex, maxResults), payload);

        LOGGER.debug("Retrieved {} tasks from task runtime", page.getTotalItems());

        var createdAfter = filter.getCreatedAfter() != null;
        var createdBefore = filter.getCreatedBefore() != null;
        LOGGER.debug("Additional filtering: createdAfter={}, createdBefore={}, status={}",
                createdAfter ? new Date(filter.getCreatedAfter()) : "null",
                createdBefore ? new Date(filter.getCreatedBefore()) : "null",
                status);

        var result = page.getContent()
                .stream()
                .filter(task -> {
                    var createdTime = ofNullable(task.getCreatedDate()).map(Date::getTime).orElse(null);

                    if (createdAfter && createdTime != null && createdTime < filter.getCreatedAfter()) {
                        LOGGER.debug("Task {} filtered out: created before filter date", task.getId());
                        return false;
                    }

                    if (createdBefore && createdTime != null && createdTime > filter.getCreatedBefore()) {
                        LOGGER.debug("Task {} filtered out: created after filter date", task.getId());
                        return false;
                    }

                    if (status == null)
                        return true;

                    try {
                        var runtimeStatus = IGRPTaskStatus.valueOf(task.getStatus().name());
                        boolean matches = runtimeStatus == status;
                        if (!matches) {
                            LOGGER.debug("Task {} filtered out: status {} doesn't match filter {}",
                                    task.getId(), runtimeStatus, status);
                        }
                        return matches;
                    } catch (IllegalArgumentException e) {
                        LOGGER.debug("Task {} filtered out: invalid status mapping {}", task.getId(), task.getStatus().name());
                        return false;
                    }
                })
                .map(task -> {
                    LOGGER.debug("Mapping task: id={}, name={}, processInstanceId={}, assignee={}",
                            task.getId(), task.getName(), task.getProcessInstanceId(), task.getAssignee());
                    var taskInfo = new TaskInfo();
                    taskInfo.setId(task.getId());
                    taskInfo.setName(task.getName());
                    taskInfo.setDescription(task.getDescription());
                    taskInfo.setProcessInstanceId(task.getProcessInstanceId());
                    taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
                    taskInfo.setAssignee(task.getAssignee());
                    taskInfo.setCreatedTime(ofNullable(task.getCreatedDate()).map(Date::getTime).orElse(0L));
                    taskInfo.setDueDate(ofNullable(task.getDueDate()).map(Date::getTime).orElse(null));
                    taskInfo.setPriority(task.getPriority());
                    taskInfo.setFormKey(task.getFormKey());
                    return taskInfo;
                })
                .toList();

        LOGGER.debug("Found {} active tasks matching all filter criteria", result.size());

        return result;
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables, String userId) {

        LOGGER.info("Completing task with id: {}, user: {}", taskId, userId);

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

        LOGGER.info("Setting variables for task with id: {}", taskId);

        LOGGER.debug("Variables count: {}, keys: {}",
                variables != null ? variables.size() : 0,
                variables != null ? variables.keySet() : "null");

        Objects.requireNonNull(taskId, "taskId cannot be null");

        if (variables != null && !variables.isEmpty()) {
            runtimeService.setVariables(taskId, variables);
            LOGGER.info("Variables successfully set for task with id: {}", taskId);
            return;
        }

        LOGGER.debug("No variables to set for task id: {}, skipping operation", taskId);
    }

    @Override
    public List<TaskVariableInstance> getTaskVariables(String taskId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.info("Getting variables for task with id: {}", taskId);

        var payload = TaskPayloadBuilder.variables()
                .withTaskId(taskId)
                .build();

        var variables = taskRuntime.variables(payload);

        LOGGER.debug("Retrieved {} variables for task id: {}", variables.size(), taskId);

        var result = variables
                .stream()
                .map(obj -> {
                    LOGGER.debug("Mapping variable: name={}, type={}, taskVariable={}, value={}",
                            obj.getName(), obj.getType(), obj.isTaskVariable(), obj.getValue());
                    var taskVariable = new TaskVariableInstance();
                    taskVariable.setName(obj.getName());
                    taskVariable.setType(obj.getType());
                    taskVariable.setProcessInstanceId(obj.getProcessInstanceId());
                    taskVariable.setTaskId(obj.getTaskId());
                    taskVariable.setTaskVariable(obj.isTaskVariable());
                    taskVariable.setValue(obj.getValue());
                    return taskVariable;
                })
                .toList();

        LOGGER.info("Successfully retrieved {} variables for task with id: {}", result.size(), taskId);

        return result;
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
