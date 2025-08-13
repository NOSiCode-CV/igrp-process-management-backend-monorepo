package cv.igrp.framework.runtime.core.engine.task.model;

public record TaskVariableInstance(
        String name,
        String type,
        String processInstanceId,
        String taskId,
        boolean isTaskVariable,
        Object value
) {
}