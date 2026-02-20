package cv.igrp.framework.process.runtime.core.engine.task.model;

public record ProcessTaskInfo(
        String taskKey,
        String taskName,
        IGRPTaskStatus status,
        String processInstanceId,
        String formKey
) {
}