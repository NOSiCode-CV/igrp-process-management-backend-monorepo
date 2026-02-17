package cv.igrp.framework.runtime.core.engine.process;

import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact;

import java.util.List;
import java.util.Optional;

/**
 * Adapter interface for abstracting access to process definitions
 * from different workflow engines (e.g., Activiti, Camunda).
 * <p>
 * This interface allows the application to remain decoupled from
 * any specific process engine implementation.
 */
public interface ProcessDefinitionAdapter {

    /**
     * Deploys the given process definition to the underlying workflow engine using
     * the provided BPMN 2.0 XML content.
     *
     * @param processDefinitionRepresentation the process definition representation containing metadata and BPMN XML
     * @return the deployed process definition representation including updated deployment metadata
     * @throws ProcessDefinitionException if deployment fails
     */
    ProcessDefinitionRepresentation deploy(ProcessDefinitionRepresentation processDefinitionRepresentation) throws ProcessDefinitionException;

    /**
     * Undeploys a previously deployed process definition from the workflow engine.
     *
     * @param deploymentId the ID of the deployment to be removed
     * @throws ProcessDefinitionException if undeployment fails
     */
    void undeploy(String deploymentId) throws ProcessDefinitionException;

	/**
	 * Retrieves a list of deployed process definitions from the workflow engine
	 * that match the given filter criteria.
	 * <p>
	 * The {@link ProcessFilter} can be used to restrict results based on attributes such as:
	 * <ul>
	 *   <li>Pagination (page number, page size)</li>
	 *   <li>Identifiers (id, key, name, deploymentId, tenantId)</li>
	 *   <li>Status (suspended, latest version, process status)</li>
	 *   <li>Business metadata (processDefinitionKey, businessKey, startUserId)</li>
	 *   <li>Start time boundaries (startedAfter, startedBefore)</li>
	 * </ul>
	 *
	 * @param filter the filter criteria encapsulated in a {@link ProcessFilter} instance
	 * @return a list of deployed process definitions matching the filter
	 * @throws ProcessDefinitionException if retrieval fails
	 */
	List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter);

	/**
	 * Retrieves the user task artifacts associated with a specific process definition.
	 * <p>
	 * Each {@link ProcessArtifact} represents a user task within the process, including:
	 * <ul>
	 *   <li>The BPMN task key (technical identifier)</li>
	 *   <li>The task name (human-readable label)</li>
	 *   <li>The form key associated with the task (if any)</li>
	 * </ul>
	 *
	 * @param processDefinitionKey the unique key of the process definition
	 * @return a list of user task artifacts defined in the process
	 * @throws ProcessDefinitionException if retrieval fails
	 */
	List<ProcessArtifact> getProcessArtifacts(String processDefinitionKey);

	/**
	 * Retrieves the identifier of the latest deployed version of a process
	 * definition for the given process definition key.
	 *
	 * <p>
	 * This method is typically used when process definitions are versioned
	 * and only the most recent version should be used for operations such as
	 * starting a process instance or managing candidate starters.
	 * </p>
	 *
	 * @param processDefinitionKey
	 *        the unique key of the process definition
	 * @return
	 *        the identifier of the latest process definition associated
	 *        with the given key, or {@code null} if no definition is found
	 */
	Optional<String> getLastProcessDefinitionIdByKey(String processDefinitionKey);

	/**
	 * Retrieves detailed information about a specific process definition.
	 *
	 * <p>
	 * This method returns a representation of the process definition metadata,
	 * such as its identifier, key, name, version, deployment information,
	 * suspension state, and other descriptive attributes.
	 * </p>
	 *
	 * @param processDefinitionId
	 *        the unique identifier of the process definition
	 * @return
	 *        a {@link ProcessDefinitionRepresentation} containing the process
	 *        definition details, or {@code null} if the process definition
	 *        does not exist
	 */
	Optional<ProcessDefinitionRepresentation> getProcessDefinition(String processDefinitionId);

	/**
	 * Adds a candidate starter group to the specified process definition.
	 *
	 * <p>
	 * Users belonging to the given group will be allowed to start new
	 * instances of the process definition. This operation does not modify
	 * the BPMN model and applies to the process definition metadata only.
	 * </p>
	 *
	 * <p>
	 * The restriction applies to all future process instances started
	 * from the specified process definition. Existing process instances
	 * are not affected.
	 * </p>
	 *
	 * @param processDefinitionId
	 *        the unique identifier of the process definition
	 * @param groupId
	 *        the identifier of the group to be granted permission to start
	 *        the process
	 */
	void addCandidateStarterGroup(String processDefinitionId, String groupId);

	/**
	 * Removes the specified candidate starter group from the given process definition.
	 *
	 * This operation revokes the permission of the group to start new instances of the
	 * process definition. It does not affect the BPMN model or any existing process
	 * instances initiated by members of the group.
	 *
	 * @param processDefinitionId the unique identifier of the process definition
	 *                            from which the group will be removed
	 * @param groupId the identifier of the group to be removed as a candidate starter
	 */
	void removeCandidateStarterGroup(String processDefinitionId, String groupId);

	/**
	 * Suspends the specified process definition by its unique identifier.
	 *
	 * This method updates the state of the process definition to suspended,
	 * preventing new process instances from being started based on it,
	 * although existing instances will remain active.
	 *
	 * @param processDefinitionId the unique identifier of the process definition to be suspended
	 */
	void suspendProcessDefinitionById(String processDefinitionId);

	/**
	 * Activates the specified process definition by its unique identifier.
	 *
	 * Activating a process definition allows new process instances to be started
	 * based on it. This operation restores the ability to use the process for
	 * initiating workflows if it was previously suspended.
	 *
	 * @param processDefinitionId the unique identifier of the process definition
	 *                            to be activated
	 */
	void activateProcessDefinitionById(String processDefinitionId);

	/**
	 * Retrieves the list of candidate starter groups for a specific process definition.
	 *
	 * This method provides the identifiers of groups that are authorized to start new
	 * instances of the specified process definition.
	 *
	 * @param processDefinitionId the unique identifier of the process definition
	 * @return a list of group identifiers that are allowed to start the process
	 */
	List<String> getCandidateStarterGroups(String processDefinitionId);
}
