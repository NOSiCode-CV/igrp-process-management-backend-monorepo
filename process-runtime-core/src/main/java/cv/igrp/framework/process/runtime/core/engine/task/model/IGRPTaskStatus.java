package cv.igrp.framework.process.runtime.core.engine.task.model;

public enum IGRPTaskStatus {
    CREATED,
    ASSIGNED,
    SUSPENDED,
    COMPLETED,
    CANCELLED,
    DELETED,
    PENDING, // todo this to another enum
    CURRENT // todo this to another enum
}
