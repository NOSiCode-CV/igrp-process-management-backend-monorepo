package cv.nosi.igrp.runtime.core;

/**
 * Enum representing the possible states of a process instance.
 */
public enum ProcessInstanceState {
    
    /**
     * Process instance is currently running
     */
    ACTIVE,
    
    /**
     * Process instance is temporarily paused
     */
    SUSPENDED,
    
    /**
     * Process instance has completed successfully
     */
    COMPLETED,
    
    /**
     * Process instance was terminated before completion
     */
    TERMINATED,
    
    /**
     * Process instance failed due to an error
     */
    FAILED,
    
    /**
     * Process instance is in an unknown state
     */
    UNKNOWN;
    
    /**
     * Checks if the state represents an active process
     * 
     * @return true if the process is considered active, false otherwise
     */
    public boolean isActive() {
        return this == ACTIVE || this == SUSPENDED;
    }
    
    /**
     * Checks if the state represents a completed process
     * 
     * @return true if the process is considered completed, false otherwise
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }
    
    /**
     * Checks if the state represents a terminated process
     * 
     * @return true if the process is considered terminated, false otherwise
     */
    public boolean isTerminated() {
        return this == TERMINATED;
    }
    
    /**
     * Checks if the state represents a failed process
     * 
     * @return true if the process is considered failed, false otherwise
     */
    public boolean isFailed() {
        return this == FAILED;
    }
}