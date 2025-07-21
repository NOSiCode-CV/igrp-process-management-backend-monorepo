package cv.nosi.igrp.runtime.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for managing process instances.
 * Provides methods for creating, retrieving, and manipulating process instances.
 */
public interface InstanceManager {

    /**
     * Creates a new process instance.
     *
     * @param processDefinitionKey The key of the process definition
     * @param businessKey Optional business key for the process instance
     * @param variables Initial variables for the process instance
     * @param startUserId ID of the user starting the process
     * @return The newly created process instance
     */
    ProcessInstance createInstance(String processDefinitionKey, String businessKey, 
                                  Map<String, Object> variables, String startUserId);

    /**
     * Retrieves a process instance by its ID.
     *
     * @param processInstanceId The ID of the process instance
     * @return An Optional containing the process instance if found, or empty if not found
     */
    Optional<ProcessInstance> getInstance(String processInstanceId);

    /**
     * Updates the state of a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @param newState The new state for the process instance
     * @return true if the state was successfully updated, false otherwise
     */
    boolean updateInstanceState(String processInstanceId, ProcessInstanceState newState);

    /**
     * Updates variables for a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @param variables The variables to update
     * @return true if the variables were successfully updated, false otherwise
     */
    boolean updateInstanceVariables(String processInstanceId, Map<String, Object> variables);

    /**
     * Deletes a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @param deleteReason The reason for deletion
     * @return true if the process instance was successfully deleted, false otherwise
     */
    boolean deleteInstance(String processInstanceId, String deleteReason);

    /**
     * Retrieves all process instances in a specific state.
     *
     * @param state The state to filter by
     * @return A list of process instances in the specified state
     */
    List<ProcessInstance> getInstancesByState(ProcessInstanceState state);

    /**
     * Retrieves all process instances for a specific process definition.
     *
     * @param processDefinitionKey The key of the process definition
     * @return A list of process instances for the specified process definition
     */
    List<ProcessInstance> getInstancesByDefinition(String processDefinitionKey);

    /**
     * Retrieves all process instances started by a specific user.
     *
     * @param userId The ID of the user
     * @return A list of process instances started by the specified user
     */
    List<ProcessInstance> getInstancesByUser(String userId);

    /**
     * Retrieves all process instances with a specific business key.
     *
     * @param businessKey The business key to filter by
     * @return A list of process instances with the specified business key
     */
    List<ProcessInstance> getInstancesByBusinessKey(String businessKey);

    /**
     * Counts the number of process instances in a specific state.
     *
     * @param state The state to count
     * @return The number of process instances in the specified state
     */
    long countInstancesByState(ProcessInstanceState state);
}