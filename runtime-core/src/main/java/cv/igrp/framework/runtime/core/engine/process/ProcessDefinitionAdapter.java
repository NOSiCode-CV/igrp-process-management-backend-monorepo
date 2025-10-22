package cv.igrp.framework.runtime.core.engine.process;

import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact;

import java.util.List;

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


	public String getLatesProcessDefinitionIdByKey(String processDefinitionKey);

}
