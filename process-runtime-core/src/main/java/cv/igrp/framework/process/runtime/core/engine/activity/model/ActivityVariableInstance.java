package cv.igrp.framework.process.runtime.core.engine.activity.model;

public record ActivityVariableInstance(
        String name,
        String type,
        String processInstanceId,
        String activityId,
        boolean isActivityVariable,
        Object value
) {
}