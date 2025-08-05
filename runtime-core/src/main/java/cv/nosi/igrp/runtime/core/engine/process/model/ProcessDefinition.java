package cv.nosi.igrp.runtime.core.engine.process.model;

public record ProcessDefinition(
        String id,
        String name,
        String resourceName,
        String key,
        int version,
        String deploymentId,
        String description,
        String applicationBase,
        boolean suspended
) {
}