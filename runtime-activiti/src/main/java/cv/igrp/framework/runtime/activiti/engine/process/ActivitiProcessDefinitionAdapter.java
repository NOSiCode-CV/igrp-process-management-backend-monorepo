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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

		if(!filter.getGroupsIds().isEmpty()){
			LOGGER.debug("Filtering by groups ids: {}", filter.getGroupsIds());
			query.startableByGroups(filter.getGroupsIds());
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

	@Override
	public String getLastProcessDefinitionIdByKey(String processDefinitionKey) {
		LOGGER.info("Resolving latest process definition ID for key: {}", processDefinitionKey);

		if (processDefinitionKey == null || processDefinitionKey.isBlank()) {
			LOGGER.warn("Provided processDefinitionKey is null or blank");
			throw new IllegalArgumentException("processDefinitionKey cannot be null or blank");
		}

		var processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey)
				.latestVersion()
				.singleResult();

		if (processDefinition == null) {
			LOGGER.error("No process definition found for key: {}", processDefinitionKey);
			throw new RuntimeException("No process definition found for key: " + processDefinitionKey);
		}

		LOGGER.info("Found latest process definition. ID: {}, Name: {}, Version: {}",
				processDefinition.getId(),
				processDefinition.getName(),
				processDefinition.getVersion());

		return processDefinition.getId();
	}

	@Override
	public Optional<ProcessDefinitionRepresentation> getProcessDefinition(String processDefinitionId) {
		LOGGER.info("Getting process definition for Id: {}", processDefinitionId);

		if (processDefinitionId == null || processDefinitionId.isBlank()) {
			LOGGER.warn("Provided processDefinitionId is null or blank");
			throw new IllegalArgumentException("processDefinitionId cannot be null or blank");
		}

		var processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId)
				.singleResult();

		if (processDefinition == null) {
			LOGGER.error("No process definition found for ID: {}", processDefinitionId);
			throw new RuntimeException("No process definition found for id: " + processDefinitionId);
		}

		LOGGER.info("Found process definition. ID: {}, Name: {}, Version: {}",
				processDefinition.getId(),
				processDefinition.getName(),
				processDefinition.getVersion());

		String bpmnXml = null;
		try (InputStream is = repositoryService.getProcessModel(processDefinition.getId())) {
			if (is == null) {
				LOGGER.warn("No BPMN model found for processDefinitionId: {}", processDefinitionId);
			} else {
				bpmnXml = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			LOGGER.error("Error reading BPMN XML for processDefinitionId: {}", processDefinitionId, e);
			throw new ProcessDefinitionException("Failed to read BPMN XML", e);
		}

        return Optional.of(
				IgrpProcessDefinitionRepresentation.builder()
						.key(processDefinition.getKey())
						.name(processDefinition.getName())
						.description(processDefinition.getDescription())
						.version(String.valueOf(processDefinition.getVersion()))
						.bpmnXml(bpmnXml)
						.resourceName(processDefinition.getResourceName())
						.applicationBase(processDefinition.getTenantId())
						.build()
		);
	}

	@Override
	public void addCandidateStarterGroup(String processDefinitionId, String groupId) {
		LOGGER.info("Adding candidate starter group '{}' for process definition '{}'", groupId, processDefinitionId);
		repositoryService.addCandidateStarterGroup(processDefinitionId, groupId);
		LOGGER.info("Added candidate starter group '{}' for process definition '{}'", groupId, processDefinitionId);
	}

}
