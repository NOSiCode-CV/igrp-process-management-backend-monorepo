package cv.nosi.igrp.runtime.core;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for publishing domain events related to process execution.
 * Provides methods for publishing various types of events that occur during process execution.
 */
public interface EventPublisher {

    /**
     * Publishes a process instance event.
     *
     * @param eventType The type of event
     * @param processInstanceId The ID of the process instance
     * @param payload Additional data for the event
     * @return true if the event was successfully published, false otherwise
     */
    boolean publishProcessInstanceEvent(String eventType, String processInstanceId, Map<String, Object> payload);

    /**
     * Publishes a task event.
     *
     * @param eventType The type of event
     * @param taskId The ID of the task
     * @param processInstanceId The ID of the process instance
     * @param payload Additional data for the event
     * @return true if the event was successfully published, false otherwise
     */
    boolean publishTaskEvent(String eventType, String taskId, String processInstanceId, Map<String, Object> payload);

    /**
     * Publishes an activity event.
     *
     * @param eventType The type of event
     * @param activityId The ID of the activity
     * @param processInstanceId The ID of the process instance
     * @param payload Additional data for the event
     * @return true if the event was successfully published, false otherwise
     */
    boolean publishActivityEvent(String eventType, String activityId, String processInstanceId, Map<String, Object> payload);

    /**
     * Publishes an error event.
     *
     * @param errorCode The error code
     * @param errorMessage The error message
     * @param processInstanceId The ID of the process instance
     * @param payload Additional data for the event
     * @return true if the event was successfully published, false otherwise
     */
    boolean publishErrorEvent(String errorCode, String errorMessage, String processInstanceId, Map<String, Object> payload);

    /**
     * Publishes a custom event.
     *
     * @param eventType The type of event
     * @param source The source of the event
     * @param payload The payload of the event
     * @return true if the event was successfully published, false otherwise
     */
    boolean publishCustomEvent(String eventType, String source, Map<String, Object> payload);

    /**
     * Publishes an event asynchronously.
     *
     * @param event The event to publish
     * @return A CompletableFuture that completes when the event is published
     */
    CompletableFuture<Boolean> publishAsync(ProcessEvent event);

    /**
     * Represents a process event.
     */
    class ProcessEvent {
        private String id;
        private String type;
        private String source;
        private LocalDateTime timestamp;
        private Map<String, Object> payload;
        
        /**
         * Default constructor
         */
        public ProcessEvent() {
            this.id = UUID.randomUUID().toString();
            this.timestamp = LocalDateTime.now();
        }
        
        /**
         * Constructor with essential fields
         * 
         * @param type The type of event
         * @param source The source of the event
         * @param payload The payload of the event
         */
        public ProcessEvent(String type, String source, Map<String, Object> payload) {
            this();
            this.type = type;
            this.source = source;
            this.payload = payload;
        }
        
        // Getters and setters
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getSource() {
            return source;
        }
        
        public void setSource(String source) {
            this.source = source;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        
        public Map<String, Object> getPayload() {
            return payload;
        }
        
        public void setPayload(Map<String, Object> payload) {
            this.payload = payload;
        }
    }
    
    /**
     * Common event types
     */
    interface EventTypes {
        String PROCESS_STARTED = "process.started";
        String PROCESS_COMPLETED = "process.completed";
        String PROCESS_TERMINATED = "process.terminated";
        String PROCESS_SUSPENDED = "process.suspended";
        String PROCESS_RESUMED = "process.resumed";
        String TASK_CREATED = "task.created";
        String TASK_ASSIGNED = "task.assigned";
        String TASK_COMPLETED = "task.completed";
        String ACTIVITY_STARTED = "activity.started";
        String ACTIVITY_COMPLETED = "activity.completed";
        String ERROR_OCCURRED = "error.occurred";
    }
}