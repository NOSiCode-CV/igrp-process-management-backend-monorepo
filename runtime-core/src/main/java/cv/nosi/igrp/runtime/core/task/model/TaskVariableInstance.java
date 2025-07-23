package cv.nosi.igrp.runtime.core.task.model;

public record TaskVariableInstance(
        String name,

        String type,

        String processInstanceId,

        String taskId,

        boolean isTaskVariable,

        Object value
) {

}
