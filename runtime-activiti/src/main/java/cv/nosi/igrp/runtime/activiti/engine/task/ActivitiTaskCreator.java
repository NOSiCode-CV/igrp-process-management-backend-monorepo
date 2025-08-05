package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.TaskCreator;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ActivitiTaskCreator implements TaskCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTaskCreator.class);

    private final TaskRuntime taskRuntime;

    public ActivitiTaskCreator(TaskRuntime taskRuntime) {
        this.taskRuntime = taskRuntime;
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
}
