package cv.nosi.igrp.task;

/**
 * Enum representing the possible states of a task.
 */
public enum TaskState {
    
    /**
     * Task has been created but not yet assigned or claimed
     */
    CREATED,
    
    /**
     * Task has been assigned to a user
     */
    ASSIGNED,
    
    /**
     * Task has been claimed by a user
     */
    CLAIMED,
    
    /**
     * Task has been delegated to another user
     */
    DELEGATED,
    
    /**
     * Task is in progress
     */
    IN_PROGRESS,
    
    /**
     * Task has been suspended
     */
    SUSPENDED,
    
    /**
     * Task has been completed
     */
    COMPLETED,
    
    /**
     * Task has been cancelled
     */
    CANCELLED,
    
    /**
     * Task has failed
     */
    FAILED;
    
    /**
     * Checks if the state is an active state
     * 
     * @return true if the state is active, false otherwise
     */
    public boolean isActive() {
        return this == ASSIGNED || this == CLAIMED || this == DELEGATED || this == IN_PROGRESS;
    }
    
    /**
     * Checks if the state is a terminal state
     * 
     * @return true if the state is terminal, false otherwise
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED || this == FAILED;
    }
    
    /**
     * Checks if the state allows assignment
     * 
     * @return true if the state allows assignment, false otherwise
     */
    public boolean canBeAssigned() {
        return this == CREATED || this == SUSPENDED;
    }
    
    /**
     * Checks if the state allows claiming
     * 
     * @return true if the state allows claiming, false otherwise
     */
    public boolean canBeClaimed() {
        return this == CREATED || this == SUSPENDED;
    }
    
    /**
     * Checks if the state allows completion
     * 
     * @return true if the state allows completion, false otherwise
     */
    public boolean canBeCompleted() {
        return this == ASSIGNED || this == CLAIMED || this == IN_PROGRESS;
    }
}