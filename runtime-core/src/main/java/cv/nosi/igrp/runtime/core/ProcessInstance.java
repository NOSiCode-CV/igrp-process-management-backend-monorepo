package cv.nosi.igrp.runtime.core;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a process instance in the system.
 * Contains information about a specific execution of a process definition.
 */
public class ProcessInstance {
    
    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private String businessKey;
    private ProcessInstanceState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startUserId;
    private Map<String, Object> variables;
    
    /**
     * Default constructor
     */
    public ProcessInstance() {
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id Process instance ID
     * @param processDefinitionId Process definition ID
     * @param processDefinitionKey Process definition key
     * @param state Current state of the process instance
     */
    public ProcessInstance(String id, String processDefinitionId, String processDefinitionKey, ProcessInstanceState state) {
        this.id = id;
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionKey = processDefinitionKey;
        this.state = state;
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    
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

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public ProcessInstanceState getState() {
        return state;
    }

    public void setState(ProcessInstanceState state) {
        this.state = state;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    /**
     * Checks if the process instance is active
     * 
     * @return true if the process is active, false otherwise
     */
    public boolean isActive() {
        return state == ProcessInstanceState.ACTIVE || 
               state == ProcessInstanceState.SUSPENDED;
    }
    
    /**
     * Checks if the process instance is completed
     * 
     * @return true if the process is completed, false otherwise
     */
    public boolean isCompleted() {
        return state == ProcessInstanceState.COMPLETED;
    }
    
    /**
     * Checks if the process instance is terminated
     * 
     * @return true if the process is terminated, false otherwise
     */
    public boolean isTerminated() {
        return state == ProcessInstanceState.TERMINATED;
    }
    
    /**
     * Checks if the process instance is suspended
     * 
     * @return true if the process is suspended, false otherwise
     */
    public boolean isSuspended() {
        return state == ProcessInstanceState.SUSPENDED;
    }
    
    /**
     * Calculates the duration of the process instance
     * 
     * @return Duration in milliseconds, or -1 if the process is still active
     */
    public long getDurationInMillis() {
        if (endTime == null) {
            return -1;
        }
        return java.time.Duration.between(startTime, endTime).toMillis();
    }
}