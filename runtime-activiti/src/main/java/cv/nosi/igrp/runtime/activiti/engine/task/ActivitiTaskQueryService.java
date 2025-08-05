package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskQueryService;
import cv.nosi.igrp.runtime.core.engine.task.model.*;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.*;

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

        LOGGER.info("Retrieving task with id: {}", taskId);

        Objects.requireNonNull(taskId, "taskId cannot be null");

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
            LOGGER.info("Task with id: {} not found or error occurred", taskId);
            LOGGER.debug("Error getting task with id: {}", taskId, e);
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
    public List<TaskInfo> getTasks(TaskFilter filter) {

        LOGGER.info("Listing tasks with filter: {}", filter);

        final var status = filter.getStatus();

        // If COMPLETED or CANCELLED, use historic task query
        if (status == IGRPTaskStatus.COMPLETED || status == IGRPTaskStatus.CANCELLED) {

            LOGGER.debug("Processing {} tasks query using history service", status);

            var query = historyService.createHistoricTaskInstanceQuery();

            LOGGER.debug("Applying filter parameters to historic query");

            if (filter.getProcessInstanceId() != null && !filter.getProcessInstanceId().isBlank()) {
                LOGGER.debug("Filtering by process instance id: {}", filter.getProcessInstanceId());
                query.processInstanceId(filter.getProcessInstanceId());
            }

            if (filter.getTaskDefinitionKey() != null && !filter.getTaskDefinitionKey().isBlank()) {
                LOGGER.debug("Filtering by task definition key: {}", filter.getTaskDefinitionKey());
                query.taskDefinitionKey(filter.getTaskDefinitionKey());
            }

            if (filter.getAssignee() != null && !filter.getAssignee().isBlank()) {
                LOGGER.debug("Filtering by assignee: {}", filter.getAssignee());
                query.taskAssignee(filter.getAssignee());
            }

            if (filter.getCreatedAfter() != null) {
                LOGGER.debug("Filtering by created after: {}", new Date(filter.getCreatedAfter()));
                query.taskCreatedAfter(new Date(filter.getCreatedAfter()));
            }

            if (filter.getCreatedBefore() != null) {
                LOGGER.debug("Filtering by created before: {}", new Date(filter.getCreatedBefore()));
                query.taskCreatedBefore(new Date(filter.getCreatedBefore()));
            }

            if (status == IGRPTaskStatus.COMPLETED) {
                LOGGER.debug("Filtering for completed tasks");
                query.finished();
            } else {
                LOGGER.debug("Filtering for cancelled tasks with delete reason: deleted");
                query.taskDeleteReason("deleted"); // ⚠️ Confirm if this delete reason matches your engine config
            }

            var tasks = query.list();

            LOGGER.info("Found {} historic tasks matching the filter criteria", tasks.size());

            return tasks
                    .stream()
                    .map(task -> {
                        return new TaskInfo(
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
                    })
                    .toList();
        }

        LOGGER.debug("Processing active tasks query using task runtime");

        var builder = TaskPayloadBuilder.tasks();

        if (filter.getProcessInstanceId() != null && !filter.getProcessInstanceId().isBlank()) {
            LOGGER.debug("Filtering by process instance id: {}", filter.getProcessInstanceId());
            builder.withProcessInstanceId(filter.getProcessInstanceId());
        }

        if (filter.getTaskDefinitionKey() != null && !filter.getTaskDefinitionKey().isBlank()) {
            LOGGER.debug("Filtering by task definition key (as parentTaskId): {}", filter.getTaskDefinitionKey());
            // TODO 04/08/2025 15:33 fix this
            builder.withParentTaskId(filter.getTaskDefinitionKey()); // ⚠️ Note: parentTaskId vs taskDefinitionKey
        }

        if (filter.isUnassigned()) {
            LOGGER.debug("Filtering for unassigned tasks");
            builder.withAssignee(null);
        } else if (filter.getAssignee() != null && !filter.getAssignee().isBlank()) {
            LOGGER.debug("Filtering by assignee: {}", filter.getAssignee());
            builder.withAssignee(filter.getAssignee());
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
                    return new TaskInfo(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getProcessInstanceId(),
                            task.getTaskDefinitionKey(),
                            task.getAssignee(),
                            null, // No owner in runtime task
                            task.getCreatedDate(),
                            task.getDueDate(),
                            task.getPriority(),
                            task.getFormKey()
                    );
                })
                .toList();

        LOGGER.debug("Found {} active tasks matching all filter criteria", result.size());

        return result;
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
                    task.getProcessInstanceId()
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
                    task.getProcessInstanceId()
            ));
        }

        return result;
    }
}
