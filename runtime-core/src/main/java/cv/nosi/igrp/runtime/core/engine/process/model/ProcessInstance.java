package cv.nosi.igrp.runtime.core.engine.process.model;

import java.util.Date;

/**
 * Classe para representar informações de uma instância de processo.
 */
public class ProcessInstance {

    private String id;
    private String name;
    private Date startDate;
    private Date completedDate;
    private String initiator;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String businessKey;
    private String parentId;
    private Integer processDefinitionVersion;
    private String processDefinitionName;
    private IGRPProcessStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
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
               ", name='" + name + '\'' +
               ", startDate=" + startDate +
               ", completedDate=" + completedDate +
               ", initiator='" + initiator + '\'' +
               ", processDefinitionId='" + processDefinitionId + '\'' +
               ", processDefinitionKey='" + processDefinitionKey + '\'' +
               ", businessKey='" + businessKey + '\'' +
               ", parentId='" + parentId + '\'' +
               ", processDefinitionVersion='" + processDefinitionVersion + '\'' +
               ", processDefinitionName='" + processDefinitionName + '\'' +
               ", status=" + status +
               '}';
    }
}