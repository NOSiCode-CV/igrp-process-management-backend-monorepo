package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.task.TaskManager;
import cv.nosi.igrp.runtime.core.task.model.IGRPTaskStatus;
import cv.nosi.igrp.runtime.core.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.task.model.TaskVariableInstance;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.*;

@Service
public class ActivitiTaskManager implements TaskManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskManager.class);

    private final TaskRuntime taskRuntime;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public ActivitiTaskManager(TaskRuntime taskRuntime, RuntimeService runtimeService, HistoryService historyService) {
        this.taskRuntime = taskRuntime;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    @Override
    public String createTask(String processInstanceId, String taskDefinitionKey, String taskName, String assignee, Map<String, Object> variables) {

        var payload = TaskPayloadBuilder.create()
                .withName(taskName)
                .withAssignee(assignee)
                .withParentTaskId(taskDefinitionKey)
                .build();

        return taskRuntime.create(payload).getId();
    }

    @Override
    public Optional<TaskInfo> getTask(String taskId) {
        try {

            var task = taskRuntime.task(taskId);

            return of(
                    new TaskInfo(
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
                    )
            );

        } catch (Exception e) {
            LOGGER.debug("Error getting task with id {}", taskId, e);
            return empty();
        }
    }

    @Override
    public List<TaskInfo> listTasks(TaskFilter filter) {

        final var status = filter.getStatus();

        // If COMPLETED or CANCELLED, use historic task query
        if (status == IGRPTaskStatus.COMPLETED || status == IGRPTaskStatus.CANCELLED) {

            var query = historyService.createHistoricTaskInstanceQuery();

            // Apply filters
            ofNullable(filter.getProcessInstanceId()).ifPresent(query::processInstanceId);
            ofNullable(filter.getTaskDefinitionKey()).ifPresent(query::taskDefinitionKey);
            ofNullable(filter.getAssignee()).ifPresent(query::taskAssignee);
            ofNullable(filter.getCreatedAfter()).ifPresent(time -> query.taskCreatedAfter(new Date(time)));
            ofNullable(filter.getCreatedBefore()).ifPresent(time -> query.taskCreatedBefore(new Date(time)));

            // Apply status
            if (status == IGRPTaskStatus.COMPLETED) {
                query.finished();
            } else {
                query.taskDeleteReason("deleted"); // ⚠️ Confirm if this delete reason matches your engine config
            }

            return query.list()
                    .stream()
                    .map(task -> new TaskInfo(
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
                    ))
                    .toList();
        }

        var builder = TaskPayloadBuilder.tasks();

        ofNullable(filter.getProcessInstanceId()).ifPresent(builder::withProcessInstanceId);
        ofNullable(filter.getTaskDefinitionKey()).ifPresent(builder::withParentTaskId); // ⚠️ Note: parentTaskId vs taskDefinitionKey

        if (filter.isUnassigned())
            builder.withAssignee(null);
        else
            ofNullable(filter.getAssignee()).ifPresent(builder::withAssignee);

        var payload = builder.build();

        var startIndex = filter.getStartIndex() != null ? filter.getStartIndex() : 0;
        var maxResults = filter.getMaxResults() != null ? filter.getMaxResults() : 50;

        var page = taskRuntime.tasks(Pageable.of(startIndex, maxResults), payload);

        var createdAfter = filter.getCreatedAfter() != null;
        var createdBefore = filter.getCreatedBefore() != null;

        return page.getContent()
                .stream()
                .filter(task -> {

                    var createdTime = ofNullable(task.getCreatedDate()).map(Date::getTime).orElse(null);

                    if (createdAfter && createdTime != null && createdTime < filter.getCreatedAfter())
                        return false;

                    if (createdBefore && createdTime != null && createdTime > filter.getCreatedBefore())
                        return false;

                    if (status == null)
                        return true;

                    try {
                        var runtimeStatus = IGRPTaskStatus.valueOf(task.getStatus().name());
                        return runtimeStatus == status;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .map(task -> new TaskInfo(
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
                ))
                .toList();
    }

    @Override
    public void assignTask(String taskId, String userId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        var payload = TaskPayloadBuilder.claim()
                .withTaskId(taskId)
                .withAssignee(userId)
                .build();

        taskRuntime.claim(payload);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables, String userId) {

        Map<String, Object> variablesPayload = variables != null ? new HashMap<>(variables) : new HashMap<>();
        //variablesPayload.put(, userId);

        var payload = TaskPayloadBuilder.complete()
                .withTaskId(taskId)
                .withVariables(variablesPayload)
                .build();

        taskRuntime.complete(payload);
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        if (variables != null && !variables.isEmpty())
            runtimeService.setVariables(taskId, variables);
    }

    @Override
    public List<TaskVariableInstance> getTaskVariables(String taskId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        var payload = TaskPayloadBuilder.variables()
                .withTaskId(taskId)
                .build();

        return taskRuntime.variables(payload)
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
    }
}
