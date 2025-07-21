package cv.nosi.igrp.runtime.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service for coordinating process execution.
 * Provides methods for executing process steps, handling signals, and managing execution flow.
 */
public interface ExecutionService {

    /**
     * Executes the next step in a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @return true if the step was successfully executed, false otherwise
     */
    boolean executeNextStep(String processInstanceId);

    /**
     * Executes a specific activity in a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @param activityId The ID of the activity to execute
     * @param variables Variables to pass to the activity
     * @return true if the activity was successfully executed, false otherwise
     */
    boolean executeActivity(String processInstanceId, String activityId, Map<String, Object> variables);

    /**
     * Executes a service task asynchronously.
     *
     * @param processInstanceId The ID of the process instance
     * @param serviceTaskId The ID of the service task
     * @param variables Variables to pass to the service task
     * @return A CompletableFuture that completes when the service task is done
     */
    CompletableFuture<Boolean> executeServiceTaskAsync(String processInstanceId, String serviceTaskId, 
                                                     Map<String, Object> variables);

    /**
     * Handles a signal event.
     *
     * @param processInstanceId The ID of the process instance
     * @param signalName The name of the signal
     * @param variables Variables to pass with the signal
     * @return true if the signal was successfully handled, false otherwise
     */
    boolean handleSignal(String processInstanceId, String signalName, Map<String, Object> variables);

    /**
     * Handles a message event.
     *
     * @param processInstanceId The ID of the process instance
     * @param messageName The name of the message
     * @param variables Variables to pass with the message
     * @return true if the message was successfully handled, false otherwise
     */
    boolean handleMessage(String processInstanceId, String messageName, Map<String, Object> variables);

    /**
     * Handles a timer event.
     *
     * @param processInstanceId The ID of the process instance
     * @param timerEventId The ID of the timer event
     * @return true if the timer event was successfully handled, false otherwise
     */
    boolean handleTimer(String processInstanceId, String timerEventId);

    /**
     * Handles an error event.
     *
     * @param processInstanceId The ID of the process instance
     * @param errorCode The error code
     * @param errorMessage The error message
     * @return true if the error event was successfully handled, false otherwise
     */
    boolean handleError(String processInstanceId, String errorCode, String errorMessage);

    /**
     * Gets the current execution path for a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @return A list of activity IDs representing the current execution path
     */
    List<String> getCurrentExecutionPath(String processInstanceId);

    /**
     * Gets the execution history for a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @return A list of executed activities with timestamps
     */
    List<ExecutionHistoryEntry> getExecutionHistory(String processInstanceId);

    /**
     * Checks if a process instance is waiting at a specific activity.
     *
     * @param processInstanceId The ID of the process instance
     * @param activityId The ID of the activity
     * @return true if the process instance is waiting at the specified activity, false otherwise
     */
    boolean isWaitingAtActivity(String processInstanceId, String activityId);
    
    /**
     * Represents an entry in the execution history.
     */
    class ExecutionHistoryEntry {
        private String activityId;
        private String activityName;
        private String activityType;
        private long startTimeMillis;
        private long endTimeMillis;
        private String executedBy;
        
        // Getters and setters
        
        public String getActivityId() {
            return activityId;
        }
        
        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }
        
        public String getActivityName() {
            return activityName;
        }
        
        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }
        
        public String getActivityType() {
            return activityType;
        }
        
        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }
        
        public long getStartTimeMillis() {
            return startTimeMillis;
        }
        
        public void setStartTimeMillis(long startTimeMillis) {
            this.startTimeMillis = startTimeMillis;
        }
        
        public long getEndTimeMillis() {
            return endTimeMillis;
        }
        
        public void setEndTimeMillis(long endTimeMillis) {
            this.endTimeMillis = endTimeMillis;
        }
        
        public String getExecutedBy() {
            return executedBy;
        }
        
        public void setExecutedBy(String executedBy) {
            this.executedBy = executedBy;
        }
        
        /**
         * Gets the duration of the activity execution in milliseconds.
         * 
         * @return The duration in milliseconds
         */
        public long getDurationMillis() {
            return endTimeMillis - startTimeMillis;
        }
    }
}