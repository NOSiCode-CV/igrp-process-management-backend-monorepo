package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.task.TaskManager;
import cv.nosi.igrp.runtime.core.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.task.model.TaskInfo;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        var status = ofNullable(filter.getStatus())
                .map(String::toLowerCase)
                .orElse("");

        if ("completed".equals(status) || "cancelled".equals(status)) {

            var query = historyService.createHistoricTaskInstanceQuery();

            ofNullable(filter.getProcessInstanceId()).ifPresent(query::processInstanceId);
            ofNullable(filter.getTaskDefinitionKey()).ifPresent(query::taskDefinitionKey);
            ofNullable(filter.getAssignee()).ifPresent(query::taskAssignee);
            ofNullable(filter.getCreatedAfter()).ifPresent(time -> query.taskCreatedAfter(new Date(time)));
            ofNullable(filter.getCreatedBefore()).ifPresent(time -> query.taskCreatedBefore(new Date(time)));

            if ("completed".equals(status))
                query.finished();
            else if ("cancelled".equals(status))
                query.taskDeleteReason("deleted"); // TODO 22/07/2025 21:01 validate this

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
        ofNullable(filter.getTaskDefinitionKey()).ifPresent(builder::withParentTaskId);
        ofNullable(filter.getAssignee()).ifPresent(builder::withAssignee);

        // TODO 22/07/2025 21:08 add missing filters

        var payload = builder.build();
        var page = taskRuntime.tasks(Pageable.of(0, 100), payload);

        return page.getContent()
                .stream()
                .filter(task -> {
                    // TODO 22/07/2025 21:04 change this
                    if (status.isEmpty()) return true;
                    return task.getStatus().name().equalsIgnoreCase(status);
                })
                .map(task -> new TaskInfo(
                        task.getId(),
                        task.getName(),
                        task.getDescription(),
                        task.getProcessInstanceId(),
                        task.getProcessInstanceId(), // TODO execution ID not available in TaskRuntime
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

        Map<String, Object> variablesPayload = variables != null ? variables : Map.of();

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
    public Map<String, Object> getTaskVariables(String taskId) {

        Objects.requireNonNull(taskId, "taskId cannot be null");

        var payload = TaskPayloadBuilder.variables()
                .withTaskId(taskId)
                .build();

        // TODO 22/07/2025 21:08 fix this get variables

        return taskRuntime.variables(payload)
                .stream()
                .collect(Collectors.toMap(v -> v.getName(), v -> v.getValue()));
    }
}
