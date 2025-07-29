package cv.nosi.igrp.runtime.core.engine.process.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessDefinition {
    private String id;
    private String name;
    private String key;
    private int version;
    private String deploymentId;
    private String description;
    private String category;
    private String tenantId;
    private boolean suspended;
}
