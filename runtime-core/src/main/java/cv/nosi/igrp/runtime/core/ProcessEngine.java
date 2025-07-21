package cv.nosi.igrp.runtime.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main interface for the process engine.
 * Provides methods for starting, managing and querying process instances.
 */
public interface ProcessEngine {

    /**
     * Starts a new process instance with the given process definition key.
     *
     * @param processDefinitionKey The key of the process definition to start
     * @param variables Initial variables for the process instance
     * @return The ID of the newly created process instance
     */
    String startProcess(String processDefinitionKey, Map<String, Object> variables);

    /**
     * Suspends a running process instance.
     *
     * @param processInstanceId The ID of the process instance to suspend
     * @return true if the process was successfully suspended, false otherwise
     */
    boolean suspendProcess(String processInstanceId);

    /**
     * Resumes a suspended process instance.
     *
     * @param processInstanceId The ID of the process instance to resume
     * @return true if the process was successfully resumed, false otherwise
     */
    boolean resumeProcess(String processInstanceId);

    /**
     * Terminates a process instance.
     *
     * @param processInstanceId The ID of the process instance to terminate
     * @param reason The reason for termination
     * @return true if the process was successfully terminated, false otherwise
     */
    boolean terminateProcess(String processInstanceId, String reason);

    /**
     * Gets the current state of a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @return An Optional containing the process state if found, or empty if not found
     */
    Optional<ProcessInstanceState> getProcessState(String processInstanceId);

    /**
     * Sends a signal to a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @param signalName The name of the signal
     * @param variables Variables to pass with the signal
     * @return true if the signal was successfully sent, false otherwise
     */
    boolean signalProcess(String processInstanceId, String signalName, Map<String, Object> variables);

    /**
     * Correlates a message to a process instance.
     *
     * @param messageName The name of the message
     * @param correlationKeys Keys to correlate the message to a specific process instance
     * @param variables Variables to pass with the message
     * @return true if the message was successfully correlated, false otherwise
     */
    boolean correlateMessage(String messageName, Map<String, Object> correlationKeys, Map<String, Object> variables);

    /**
     * Gets all active process instances.
     *
     * @return A list of active process instances
     */
    List<ProcessInstance> getActiveProcessInstances();

    /**
     * Gets all process instances for a specific process definition.
     *
     * @param processDefinitionKey The key of the process definition
     * @return A list of process instances for the given process definition
     */
    List<ProcessInstance> getProcessInstancesByDefinition(String processDefinitionKey);

    /**
     * Gets all process instances started by a specific user.
     *
     * @param userId The ID of the user
     * @return A list of process instances started by the given user
     */
    List<ProcessInstance> getProcessInstancesByUser(String userId);
}