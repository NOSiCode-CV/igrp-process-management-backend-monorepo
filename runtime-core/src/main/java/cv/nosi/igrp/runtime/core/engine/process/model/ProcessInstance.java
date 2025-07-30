package cv.nosi.igrp.runtime.core.engine.process.model;

/**
 * Classe para representar informações de uma instância de processo.
 */
public class ProcessInstance {
    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private long startTime;
    private IGRPProcessStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public IGRPProcessStatus getStatus() {
        return status;
    }

    public void setStatus(IGRPProcessStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
               "id='" + id + '\'' +
               ", processDefinitionId='" + processDefinitionId + '\'' +
               ", processDefinitionKey='" + processDefinitionKey + '\'' +
               ", businessKey='" + businessKey + '\'' +
               ", startUserId='" + startUserId + '\'' +
               ", startTime=" + startTime +
               ", status=" + status +
               '}';
    }
}