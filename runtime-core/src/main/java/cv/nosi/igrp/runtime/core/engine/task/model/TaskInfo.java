package cv.nosi.igrp.runtime.core.engine.task.model;

import java.util.Date;

/**
 * Classe para representar informações de uma tarefa.
 */

public class TaskInfo {
    private String id;
    private String name;
    private String description;
    private String processInstanceId;
    private String taskDefinitionKey;
    private String assignee;
    private String owner;
    private Date createdTime;
    private Date dueDate;
    private int priority;
    private String formKey;

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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", processInstanceId='" + processInstanceId + '\'' +
               ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
               ", assignee='" + assignee + '\'' +
               ", owner='" + owner + '\'' +
               ", createdTime=" + createdTime +
               ", dueDate=" + dueDate +
               ", priority=" + priority +
               ", formKey='" + formKey + '\'' +
               '}';
    }
}