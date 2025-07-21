package cv.nosi.igrp.runtime.activiti;

import cv.nosi.igrp.runtime.core.EventPublisher;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Event listener for Activiti events.
 * Listens to Activiti engine events and converts them to our domain events.
 */
@Component
public class ActivitiEventListener implements ActivitiEventListener {

    private final EventPublisher eventPublisher;

    /**
     * Constructor with required dependencies.
     *
     * @param eventPublisher The event publisher to publish domain events
     */
    @Autowired
    public ActivitiEventListener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        // Convert Activiti event to our domain event and publish it
        String eventType = mapEventType(event.getType());
        String processInstanceId = event.getProcessInstanceId();
        Map<String, Object> payload = createEventPayload(event);

        // Publish the event using our event publisher
        eventPublisher.publishProcessInstanceEvent(eventType, processInstanceId, payload);
    }

    @Override
    public boolean isFailOnException() {
        // Return false to prevent Activiti from failing if event handling fails
        return false;
    }

    /**
     * Maps an Activiti event type to our domain event type.
     *
     * @param activitiEventType The Activiti event type
     * @return Our domain event type
     */
    private String mapEventType(ActivitiEventType activitiEventType) {
        switch (activitiEventType) {
            case PROCESS_STARTED:
                return EventPublisher.EventTypes.PROCESS_STARTED;
            case PROCESS_COMPLETED:
                return EventPublisher.EventTypes.PROCESS_COMPLETED;
            case PROCESS_CANCELLED:
                return EventPublisher.EventTypes.PROCESS_TERMINATED;
            case ACTIVITY_STARTED:
                return EventPublisher.EventTypes.ACTIVITY_STARTED;
            case ACTIVITY_COMPLETED:
                return EventPublisher.EventTypes.ACTIVITY_COMPLETED;
            case TASK_CREATED:
                return EventPublisher.EventTypes.TASK_CREATED;
            case TASK_ASSIGNED:
                return EventPublisher.EventTypes.TASK_ASSIGNED;
            case TASK_COMPLETED:
                return EventPublisher.EventTypes.TASK_COMPLETED;
            case JOB_EXECUTION_FAILURE:
                return EventPublisher.EventTypes.ERROR_OCCURRED;
            default:
                return "activiti." + activitiEventType.name().toLowerCase();
        }
    }

    /**
     * Creates a payload map from an Activiti event.
     *
     * @param event The Activiti event
     * @return A map containing event data
     */
    private Map<String, Object> createEventPayload(ActivitiEvent event) {
        Map<String, Object> payload = new HashMap<>();
        
        // Add common event properties
        payload.put("eventType", event.getType().name());
        payload.put("processDefinitionId", event.getProcessDefinitionId());
        payload.put("processInstanceId", event.getProcessInstanceId());
        payload.put("executionId", event.getExecutionId());
        
        // Add specific properties based on event type
        switch (event.getType()) {
            case TASK_CREATED:
            case TASK_ASSIGNED:
            case TASK_COMPLETED:
                // For task events, we could add task-specific data
                // This would require casting to the specific event type
                // org.activiti.engine.delegate.event.ActivitiTaskEvent taskEvent = (org.activiti.engine.delegate.event.ActivitiTaskEvent) event;
                // payload.put("taskId", taskEvent.getTaskId());
                // payload.put("taskName", taskEvent.getTaskName());
                break;
            case ACTIVITY_STARTED:
            case ACTIVITY_COMPLETED:
                // For activity events, we could add activity-specific data
                // This would require casting to the specific event type
                // org.activiti.engine.delegate.event.ActivitiActivityEvent activityEvent = (org.activiti.engine.delegate.event.ActivitiActivityEvent) event;
                // payload.put("activityId", activityEvent.getActivityId());
                // payload.put("activityName", activityEvent.getActivityName());
                // payload.put("activityType", activityEvent.getActivityType());
                break;
            case JOB_EXECUTION_FAILURE:
                // For error events, we could add error-specific data
                // This would require casting to the specific event type
                // org.activiti.engine.delegate.event.ActivitiErrorEvent errorEvent = (org.activiti.engine.delegate.event.ActivitiErrorEvent) event;
                // payload.put("errorCode", errorEvent.getErrorCode());
                // payload.put("errorMessage", errorEvent.getCause().getMessage());
                break;
        }
        
        return payload;
    }
}