package cv.nosi.igrp.runtime.core.engine.task.model;

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

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public boolean isUnassigned() {
        return unassigned;
    }

    public void setUnassigned(boolean unassigned) {
        this.unassigned = unassigned;
    }

    public Long getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Long createdAfter) {
        this.createdAfter = createdAfter;
    }

    public Long getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Long createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Long getDueDateAfter() {
        return dueDateAfter;
    }

    public void setDueDateAfter(Long dueDateAfter) {
        this.dueDateAfter = dueDateAfter;
    }

    public Long getDueDateBefore() {
        return dueDateBefore;
    }

    public void setDueDateBefore(Long dueDateBefore) {
        this.dueDateBefore = dueDateBefore;
    }

    public IGRPTaskStatus getStatus() {
        return status;
    }

    public void setStatus(IGRPTaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskFilter{" +
               "startIndex=" + startIndex +
               ", maxResults=" + maxResults +
               ", assignee='" + assignee + '\'' +
               ", processInstanceId='" + processInstanceId + '\'' +
               ", taskName='" + taskName + '\'' +
               ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
               ", unassigned=" + unassigned +
               ", createdAfter=" + createdAfter +
               ", createdBefore=" + createdBefore +
               ", dueDateAfter=" + dueDateAfter +
               ", dueDateBefore=" + dueDateBefore +
               ", status=" + status +
               '}';
    }
}