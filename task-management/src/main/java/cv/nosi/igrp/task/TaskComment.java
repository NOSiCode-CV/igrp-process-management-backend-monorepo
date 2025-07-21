package cv.nosi.igrp.task;

import java.time.LocalDateTime;

/**
 * Represents a comment on a task.
 * Contains information about a comment made by a user on a task.
 */
public class TaskComment {
    
    private String id;
    private String taskId;
    private String userId;
    private String text;
    private LocalDateTime createdTime;
    
    /**
     * Default constructor
     */
    public TaskComment() {
        this.createdTime = LocalDateTime.now();
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id Comment ID
     * @param taskId Task ID
     * @param userId User ID
     * @param text Comment text
     */
    public TaskComment(String id, String taskId, String userId, String text) {
        this();
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.text = text;
    }

    // Getters and Setters
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    /**
     * Gets the age of the comment in milliseconds
     * 
     * @return Age in milliseconds
     */
    public long getAgeMillis() {
        return java.time.Duration.between(this.createdTime, LocalDateTime.now()).toMillis();
    }
    
    /**
     * Checks if the comment was created by a specific user
     * 
     * @param userId User ID to check
     * @return true if the comment was created by the specified user, false otherwise
     */
    public boolean isCreatedBy(String userId) {
        return this.userId != null && this.userId.equals(userId);
    }
    
    /**
     * Creates a formatted string representation of the comment
     * 
     * @return Formatted comment string
     */
    public String toFormattedString() {
        return String.format("[%s] %s: %s", 
                this.createdTime.toString(), 
                this.userId, 
                this.text);
    }
}