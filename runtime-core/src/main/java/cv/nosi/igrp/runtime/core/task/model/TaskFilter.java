package cv.nosi.igrp.runtime.core.task.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskFilter {

    private String assignee;

    private String processInstanceId;

    private String taskName;

    private String taskDefinitionKey;

    private Boolean unassigned;

    private Long createdAfter;

    private Long createdBefore;

    private Long dueDateAfter;

    private Long dueDateBefore;

    private String status;
}