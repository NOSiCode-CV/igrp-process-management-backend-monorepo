package cv.nosi.igrp.runtime.core.engine.process.model;

public record ProcessDefinition(
        String id,
        String name,
        String key,
        int version,
        String deploymentId,
        String description,
        String category,
        String tenantId,
        boolean suspended
) {
}
