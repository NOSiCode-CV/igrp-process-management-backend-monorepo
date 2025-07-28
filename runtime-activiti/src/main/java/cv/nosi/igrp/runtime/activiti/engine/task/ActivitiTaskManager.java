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
        LOGGER.info("Creating task for process instance: {}, definition key: {}, name: {}, assignee: {}", 
                processInstanceId, taskDefinitionKey, taskName, assignee);
        
        LOGGER.debug("Building task creation payload");
        var payload = TaskPayloadBuilder.create()
                .withName(taskName)
                .withAssignee(assignee)
                .withParentTaskId(taskDefinitionKey)
                .build();
        
        LOGGER.debug("Executing task creation");
        var task = taskRuntime.create(payload);
        
        LOGGER.info("Task created successfully with id: {}", task.getId());
        LOGGER.debug("Created task details: id={}, name={}, assignee={}", 
                task.getId(), task.getName(), task.getAssignee());
                
        return task.getId();
    }

    @Override
    public Optional<TaskInfo> getTask(String taskId) {
        LOGGER.info("Retrieving task with id: {}", taskId);
        try {
            LOGGER.debug("Querying task runtime for task id: {}", taskId);
            var task = taskRuntime.task(taskId);
            
            LOGGER.debug("Task found: id={}, name={}, processInstanceId={}, assignee={}", 
                    task.getId(), task.getName(), task.getProcessInstanceId(), task.getAssignee());
            
            var taskInfo = new TaskInfo(
                    task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getProcessInstanceId(),
                    task.getTaskDefinitionKey(),
                    task.getAssignee(),
                    task.getOwner(),
                    task.getCreatedDate().getTime(),
                    ofNullable(task.getDueDate()).map(Date::getTime).orElse(null),
                    task.getPriority(),
                    task.getFormKey()
            );
            
            LOGGER.info("Successfully retrieved task with id: {}, name: {}", taskId, task.getName());
            return of(taskInfo);

        } catch (Exception e) {
            LOGGER.info("Task with id: {} not found or error occurred", taskId);
            LOGGER.debug("Error getting task with id: {}", taskId, e);
            return empty();
        }
    }

    @Override
    public List<TaskInfo> listTasks(TaskFilter filter) {
        LOGGER.info("Listing tasks with filter: status={}, processInstanceId={}, assignee={}", 
                filter.getStatus(), filter.getProcessInstanceId(), filter.getAssignee());
        
        LOGGER.debug("Processing filter parameters: {}", filter);
        final var status = filter.getStatus();

        // If COMPLETED or CANCELLED, use historic task query
        if (status == IGRPTaskStatus.COMPLETED || status == IGRPTaskStatus.CANCELLED) {
            LOGGER.debug("Processing {} tasks query using history service", status);
            
            LOGGER.debug("Creating historic task instance query");
            var query = historyService.createHistoricTaskInstanceQuery();

            LOGGER.debug("Applying filter parameters to historic query");
            // Apply filters
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

            // Apply status
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
                        LOGGER.debug("Mapping historic task: id={}, name={}, processInstanceId={}", 
                                task.getId(), task.getName(), task.getProcessInstanceId());
                        return new TaskInfo(
                                task.getId(),
                                task.getName(),
                                task.getDescription(),
                                task.getProcessInstanceId(),
                                task.getTaskDefinitionKey(),
                                task.getAssignee(),
                                task.getOwner(),
                                task.getCreateTime().getTime(),
                                ofNullable(task.getDueDate()).map(Date::getTime).orElse(0L),
                                task.getPriority(),
                                task.getFormKey()
                        );
                    })
                    .toList();
        }

        LOGGER.debug("Processing active tasks query using task runtime");
        
        LOGGER.debug("Building task payload builder");
        var builder = TaskPayloadBuilder.tasks();

        LOGGER.debug("Applying filter parameters to task payload");
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

        LOGGER.debug("Building task payload");
        var payload = builder.build();

        var startIndex = ofNullable(filter.getStartIndex()).orElse(0);
        var maxResults = ofNullable(filter.getMaxResults()).orElse(50);
        LOGGER.debug("Pagination: startIndex={}, maxResults={}", startIndex, maxResults);

        LOGGER.debug("Executing task runtime query");
        var page = taskRuntime.tasks(Pageable.of(startIndex, maxResults), payload);
        LOGGER.debug("Retrieved {} tasks from task runtime", page.getTotalItems());

        var createdAfter = filter.getCreatedAfter() != null;
        var createdBefore = filter.getCreatedBefore() != null;
        LOGGER.debug("Additional filtering: createdAfter={}, createdBefore={}, status={}", 
                createdAfter ? new Date(filter.getCreatedAfter()) : "null", 
                createdBefore ? new Date(filter.getCreatedBefore()) : "null", 
                status);

        LOGGER.debug("Filtering and mapping task results");
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
                    return new TaskInfo(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getProcessInstanceId(),
                            task.getProcessInstanceId(), // TODO: Execution ID unavailable
                            task.getTaskDefinitionKey(),
                            task.getAssignee(),
                            ofNullable(task.getCreatedDate()).map(Date::getTime).orElse(0L),
                            ofNullable(task.getDueDate()).map(Date::getTime).orElse(0L),
                            task.getPriority(),
                            task.getFormKey()
                    );
                })
                .toList();
                
        LOGGER.info("Found {} active tasks matching all filter criteria", result.size());
        return result;
    }

    @Override
    public void assignTask(String taskId, String userId) {
        LOGGER.info("Assigning task with id: {} to user: {}", taskId, userId);
        
        LOGGER.debug("Validating parameters");
        Objects.requireNonNull(taskId, "taskId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        LOGGER.debug("Building claim payload for task id: {}, assignee: {}", taskId, userId);
        var payload = TaskPayloadBuilder.claim()
                .withTaskId(taskId)
                .withAssignee(userId)
                .build();

        LOGGER.debug("Executing claim operation");
        taskRuntime.claim(payload);
        
        LOGGER.info("Task with id: {} successfully assigned to user: {}", taskId, userId);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables, String userId) {
        LOGGER.info("Completing task with id: {}, user: {}", taskId, userId);
        LOGGER.debug("Variables count: {}, keys: {}", 
                variables != null ? variables.size() : 0, 
                variables != null ? variables.keySet() : "null");
        
        LOGGER.debug("Preparing variables payload");
        Map<String, Object> variablesPayload = variables != null ? new HashMap<>(variables) : new HashMap<>();
        //variablesPayload.put(, userId);
        
        LOGGER.debug("Building complete payload for task id: {}", taskId);
        var payload = TaskPayloadBuilder.complete()
                .withTaskId(taskId)
                .withVariables(variablesPayload)
                .build();

        LOGGER.debug("Executing complete operation for task id: {}", taskId);
        taskRuntime.complete(payload);
        
        LOGGER.info("Task with id: {} successfully completed by user: {}", taskId, userId);
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        LOGGER.info("Setting variables for task with id: {}", taskId);
        LOGGER.debug("Variables count: {}, keys: {}", 
                variables != null ? variables.size() : 0, 
                variables != null ? variables.keySet() : "null");
        
        LOGGER.debug("Validating parameters");
        Objects.requireNonNull(taskId, "taskId cannot be null");

        if (variables != null && !variables.isEmpty()) {
            LOGGER.debug("Setting variables for task id: {}", taskId);
            runtimeService.setVariables(taskId, variables);
            LOGGER.info("Variables successfully set for task with id: {}", taskId);
        } else {
            LOGGER.debug("No variables to set for task id: {}, skipping operation", taskId);
        }
    }

    @Override
    public List<TaskVariableInstance> getTaskVariables(String taskId) {
        LOGGER.info("Getting variables for task with id: {}", taskId);
        
        LOGGER.debug("Validating parameters");
        Objects.requireNonNull(taskId, "taskId cannot be null");

        LOGGER.debug("Building variables payload for task id: {}", taskId);
        var payload = TaskPayloadBuilder.variables()
                .withTaskId(taskId)
                .build();

        LOGGER.debug("Executing get variables operation for task id: {}", taskId);
        var variables = taskRuntime.variables(payload);
        LOGGER.debug("Retrieved {} variables for task id: {}", variables.size(), taskId);
        
        LOGGER.debug("Mapping variable objects to TaskVariableInstance");
        var result = variables
                .stream()
                .map(obj -> {
                    LOGGER.debug("Mapping variable: name={}, type={}, taskVariable={}, value={}", 
                            obj.getName(), obj.getType(), obj.isTaskVariable(), obj.getValue());
                    return new TaskVariableInstance(
                            obj.getName(),
                            obj.getType(),
                            obj.getProcessInstanceId(),
                            obj.getTaskId(),
                            obj.isTaskVariable(),
                            obj.getValue()
                    );
                })
                .toList();
        
        LOGGER.info("Successfully retrieved {} variables for task with id: {}", 
                result.size(), taskId);
        return result;
    }

    @Override
    public boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason) {

        var task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null || !Objects.equals(task.getOwner(), ownerUserId))
            return false;

        taskService.setOwner(taskId, ownerUserId);
        taskService.delegateTask(taskId, delegateUserId);

        if (reason != null)
            taskService.addComment(taskId, task.getProcessInstanceId(), "Delegated: " + reason);

        return true;
    }

    @Override
    public boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment) {

        var task = taskService.createTaskQuery().taskId(taskId).taskAssignee(delegateUserId).singleResult();
        if (task == null)
            return false;

        if (comment != null)
            taskService.addComment(taskId, task.getProcessInstanceId(), "Resolved: " + comment);

        taskService.resolveTask(taskId);

        return true;
    }

    @Override
    public boolean setTaskDueDate(String taskId, long dueDate) {

        var task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            return false;

        task.setDueDate(new Date(dueDate));
        taskService.saveTask(task);

        return true;
    }

    @Override
    public boolean assignTask(String taskId, String userId, String reason) {
        var task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            taskService.setAssignee(taskId, userId);
            taskService.addComment(taskId, task.getProcessInstanceId(), "Assigned to " + userId + ": " + reason);
            return true;
        }
        return false;
    }
}
