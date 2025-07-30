package cv.nosi.igrp.runtime.core.engine.process.model;

public class ProcessFilter {
    private Integer startIndex;
    private Integer maxResults;
    private String id;
    private String key;
    private String name;
    private String applicationBase;
    private String deploymentId;
    private String tenantId;
    private Boolean suspended;
    private boolean isLatestVersion = true;
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private IGRPProcessStatus status;
    private Long startedAfter;
    private Long startedBefore;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationBase() {
        return applicationBase;
    }

    public void setApplicationBase(String applicationBase) {
        this.applicationBase = applicationBase;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public IGRPProcessStatus getStatus() {
        return status;
    }

    public void setStatus(IGRPProcessStatus status) {
        this.status = status;
    }

    public Long getStartedAfter() {
        return startedAfter;
    }

    public void setStartedAfter(Long startedAfter) {
        this.startedAfter = startedAfter;
    }

    public Long getStartedBefore() {
        return startedBefore;
    }

    public void setStartedBefore(Long startedBefore) {
        this.startedBefore = startedBefore;
    }

    @Override
    public String toString() {
        return "ProcessFilter{" +
               "startIndex=" + startIndex +
               ", maxResults=" + maxResults +
               ", id='" + id + '\'' +
               ", key='" + key + '\'' +
               ", name='" + name + '\'' +
               ", applicationBase='" + applicationBase + '\'' +
               ", deploymentId='" + deploymentId + '\'' +
               ", tenantId='" + tenantId + '\'' +
               ", suspended=" + suspended +
               ", processDefinitionKey='" + processDefinitionKey + '\'' +
               ", businessKey='" + businessKey + '\'' +
               ", startUserId='" + startUserId + '\'' +
               ", status=" + status +
               ", startedAfter=" + startedAfter +
               ", startedBefore=" + startedBefore +
               '}';
    }

    public boolean isLatestVersion() {
        return isLatestVersion;
    }

    public void setLatestVersion(boolean latestVersion) {
        isLatestVersion = latestVersion;
    }
}
