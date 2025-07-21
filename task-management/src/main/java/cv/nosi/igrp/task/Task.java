package cv.nosi.igrp.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a human task in the system.
 * Contains information about a task that needs to be performed by a user.
 */
public class Task {
    
    private String id;
    private String name;
    private String description;
    private String assignee;
    private String owner;
    private String processInstanceId;
    private String processDefinitionId;
    private String executionId;
    private TaskState state;
    private int priority;
    private LocalDateTime createdTime;
    private LocalDateTime dueDate;
    private LocalDateTime claimedTime;
    private LocalDateTime completedTime;
    private Map<String, Object> variables;
    
    /**
     * Default constructor
     */
    public Task() {
        this.variables = new HashMap<>();
        this.createdTime = LocalDateTime.now();
        this.state = TaskState.CREATED;
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id Task ID
     * @param name Task name
     * @param description Task description
     */
    public Task(String id, String name, String description) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    /**
     * Constructor with process information
     * 
     * @param id Task ID
     * @param name Task name
     * @param description Task description
     * @param processInstanceId Process instance ID
     * @param processDefinitionId Process definition ID
     */
    public Task(String id, String name, String description, String processInstanceId, String processDefinitionId) {
        this(id, name, description);
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
    }

    // Getters and Setters
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getClaimedTime() {
        return claimedTime;
    }

    public void setClaimedTime(LocalDateTime claimedTime) {
        this.claimedTime = claimedTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    /**
     * Adds a variable to the task
     * 
     * @param key Variable key
     * @param value Variable value
     */
    public void addVariable(String key, Object value) {
        this.variables.put(key, value);
    }
    
    /**
     * Gets a variable from the task
     * 
     * @param key Variable key
     * @return Variable value, or null if not found
     */
    public Object getVariable(String key) {
        return this.variables.get(key);
    }
    
    /**
     * Checks if the task is assigned
     * 
     * @return true if the task is assigned, false otherwise
     */
    public boolean isAssigned() {
        return this.assignee != null && !this.assignee.isEmpty();
    }
    
    /**
     * Checks if the task is completed
     * 
     * @return true if the task is completed, false otherwise
     */
    public boolean isCompleted() {
        return this.state == TaskState.COMPLETED;
    }
    
    /**
     * Checks if the task is overdue
     * 
     * @return true if the task is overdue, false otherwise
     */
    public boolean isOverdue() {
        return this.dueDate != null && LocalDateTime.now().isAfter(this.dueDate);
    }
    
    /**
     * Gets the time spent on the task in milliseconds
     * 
     * @return Time spent in milliseconds, or -1 if the task is not completed
     */
    public long getTimeSpentMillis() {
        if (this.completedTime == null) {
            return -1;
        }
        
        LocalDateTime startTime = this.claimedTime != null ? this.claimedTime : this.createdTime;
        return java.time.Duration.between(startTime, this.completedTime).toMillis();
    }
}