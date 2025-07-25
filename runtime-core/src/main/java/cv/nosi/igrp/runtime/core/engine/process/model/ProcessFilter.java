package cv.nosi.igrp.runtime.core.engine.process.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProcessFilter {
    private Integer startIndex;
    private Integer maxResults;
    private String id;
    private String key;
    private String name;
    private String category;
    private String deploymentId;
    private String tenantId;
    private Boolean suspended;
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private IGRPProcessStatus status;
    private Long startedAfter;
    private Long startedBefore;
}
