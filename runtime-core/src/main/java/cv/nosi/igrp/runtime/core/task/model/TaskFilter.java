package cv.nosi.igrp.runtime.core.task.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFilter {

    private Integer startIndex;

    private Integer maxResults;

    private String assignee;

    private String processInstanceId;

    private String taskName;

    private String taskDefinitionKey;

    private boolean unassigned;

    private Long createdAfter;

    private Long createdBefore;

    private Long dueDateAfter;

    private Long dueDateBefore;

    private IGRPTaskStatus status;
}