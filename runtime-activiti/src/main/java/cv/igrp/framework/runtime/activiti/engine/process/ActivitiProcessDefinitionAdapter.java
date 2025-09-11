package cv.igrp.framework.runtime.activiti.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.BpmnSourceType;
import cv.igrp.framework.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;


@Service
public class ActivitiProcessDefinitionAdapter implements ProcessDefinitionAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessDefinitionAdapter.class);

    private final RepositoryService repositoryService;

    public ActivitiProcessDefinitionAdapter(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public ProcessDefinitionRepresentation deploy(ProcessDefinitionRepresentation processDefinitionRepresentation) throws ProcessDefinitionException {

        LOGGER.info("Deploying process definition representation: {}", processDefinitionRepresentation);

        try {

            var resourceName = Objects.requireNonNull(
                    processDefinitionRepresentation.resourceName(),
                    "The resource name is required for deployment. Ex: dynamicProcess.bpmn20.xml"
            );

            var bpmnXml = processDefinitionRepresentation.bpmnXml();

            var processKey = Objects.requireNonNull(
                    processDefinitionRepresentation.key(),
                    "The key is required for deployment."
            );

            var deployment = repositoryService.createDeployment()
                    .addString(resourceName, bpmnXml)
                    .name(processDefinitionRepresentation.name())
                    .key(processKey)
                    .tenantId(processDefinitionRepresentation.applicationBase())
                    .deploy();

            var processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();

            var deployedBpmnXml = this.getBpmnXml(deployment.getId(), resourceName);

            var result = IgrpProcessDefinitionRepresentation.builder()
                    .key(processDefinition.getKey())
                    .deploymentId(deployment.getId())
                    .name(processDefinition.getName())
                    .description(processDefinition.getDescription())
                    .version(String.valueOf(processDefinition.getVersion()))
                    .bpmnXml(deployedBpmnXml)
                    .resourceName(resourceName)
                    .applicationBase(processDefinition.getTenantId())
                    .bpmnSourceType(BpmnSourceType.INLINE_XML)
                    .deployed(true)
                    .deployedAt(deployment.getDeploymentTime().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

            LOGGER.debug("Process definition representation created: {}", result);

            return result;

        } catch (Exception ex) {
            LOGGER.error("Failed to deploy BPMN XML", ex);
            throw new ProcessDefinitionException("An error occurred when deploying a new process definition BPMN XML", ex);
        }
    }


    @Override
    public void undeploy(String deploymentId) throws ProcessDefinitionException {

        try {
            LOGGER.debug("Deleting deployment with id: {}, cascade: true", deploymentId);

            repositoryService.deleteDeployment(deploymentId, true);

            LOGGER.info("Successfully undeployed process definition with deployment id: {}", deploymentId);

        } catch (Exception ex) {
            LOGGER.error("Failed to undeploy the process definition with deployment id: {}", deploymentId, ex);
            throw new ProcessDefinitionException("An error occurred when undeploy the process definition", ex);
        }
    }

    private ProcessDefinition getProcessDefinition(final String deploymentId) {

        LOGGER.debug("Querying process definition for deployment id: {}", deploymentId);

        var result = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        LOGGER.debug("Found process definition: {}", result);

        return result;
    }

    private String getBpmnXml(final String deploymentId, final String resourceName) throws Exception {

        LOGGER.debug("Retrieving BPMN XML content for deployment id: {}, resource name: {}", deploymentId, resourceName);

        try (var bpmnStream = repositoryService.getResourceAsStream(deploymentId, resourceName)) {

            var xml = new String(bpmnStream.readAllBytes(), StandardCharsets.UTF_8);

            LOGGER.debug("Successfully retrieved BPMN XML content, size: {} bytes", xml.length());

            return xml;
        }
    }

	@Override
	public List<cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {

		LOGGER.info("Getting deployed processes with filter: {}", filter);

		Predicate<String> isValidString = obj -> obj != null && !obj.isBlank();

		var query = repositoryService.createProcessDefinitionQuery();

		if (isValidString.test(filter.getId())) {
			LOGGER.debug("Filtering by process definition id: {}", filter.getId());
			query.processDefinitionId(filter.getId());
		}

		if (isValidString.test(filter.getKey())) {
			LOGGER.debug("Filtering by process definition key: {}", filter.getKey());
			query.processDefinitionKey(filter.getKey());
		}

		if (isValidString.test(filter.getName())) {
			var pattern = "%" + filter.getName().trim() + "%";
			LOGGER.debug("Filtering by process definition name like: {}", pattern);
			query.processDefinitionNameLike(pattern);
		}

		if (isValidString.test(filter.getDeploymentId())) {
			LOGGER.debug("Filtering by deployment id: {}", filter.getDeploymentId());
			query.deploymentId(filter.getDeploymentId());
		}

		if (isValidString.test(filter.getTenantId())) {
			LOGGER.debug("Filtering by tenant id: {}", filter.getTenantId());
			query.processDefinitionTenantId(filter.getTenantId());
		}

		if (Boolean.TRUE.equals(filter.getSuspended())) {
			LOGGER.debug("Filtering for suspended process definitions");
			query.suspended();
		}
		if (Boolean.FALSE.equals(filter.getSuspended())) {
			LOGGER.debug("Filtering for active process definitions");
			query.active();
		}

		if (filter.isLatestVersion()) {
			LOGGER.debug("Filtering for latest process definitions");
			query.latestVersion();
		}

		if (isValidString.test(filter.getApplicationBase())) {
			LOGGER.debug("Filtering by ApplicationBase: {}", filter.getApplicationBase());
			query.processDefinitionTenantId(filter.getApplicationBase());
		}

		var startIndex = ofNullable(filter.getPageNumber()).orElse(0);
		var maxResults = ofNullable(filter.getPageSize()).orElse(50);
		LOGGER.debug("Final Pagination: startIndex={}, maxResults={}", startIndex, maxResults);

		var definitions = query.listPage(startIndex, maxResults);

		LOGGER.info("Found {} deployed process definitions matching the filter criteria", definitions.size());

		return definitions
				.stream()
				.map(def -> new cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition(
						def.getId(),
						def.getName(),
						def.getResourceName(),
						def.getKey(),
						def.getVersion(),
						def.getDeploymentId(),
						def.getDescription(),
						def.getTenantId(), // TODO 05/08/2025 16:37 validate tenant id containing application base
						def.isSuspended()))
				.toList();
	}

	@Override
	public List<ProcessArtifact> getProcessArtifacts(String processDefinitionKey) {

		LOGGER.debug("Getting tasks for BPMN progress drawing, processDefinitionKey: {}", processDefinitionKey);

		return repositoryService.getBpmnModel(processDefinitionKey)
				.getMainProcess()
				.getFlowElements()
				.stream()
				.filter(element -> element instanceof UserTask)
				.map(ut -> {

					var userTask = (UserTask) ut;

					return new ProcessArtifact(
							userTask.getId(),
							userTask.getName(),
							userTask.getFormKey()
					);

				})
				.toList();
	}

}
