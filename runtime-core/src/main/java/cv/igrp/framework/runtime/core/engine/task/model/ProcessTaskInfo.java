package cv.igrp.framework.runtime.core.engine.task.model;

public record ProcessTaskInfo(
        String taskKey,
        String taskName,
        IGRPTaskStatus status,
        String processInstanceId,
        String formKey
) {
}