package cv.nosi.igrp.runtime.activiti.engine.process;


import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.nosi.igrp.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.nosi.igrp.runtime.core.engine.process.model.BpmnSourceType;
import cv.nosi.igrp.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.Objects;


@Service
public class ActivitiProcessDefinitionAdapter implements ProcessDefinitionAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessDefinitionAdapter.class);

    private final RepositoryService repositoryService;

    public ActivitiProcessDefinitionAdapter(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public ProcessDefinitionRepresentation deploy(ProcessDefinitionRepresentation processDefinitionRepresentation) throws ProcessDefinitionException {
        LOGGER.info("Deploying process definition with key: {}", processDefinitionRepresentation.getKey());
        try {

            LOGGER.debug("Validating process definition deployment parameters");

            final var resourceName = Objects.requireNonNull(processDefinitionRepresentation.getResourceName(), "The resource name is required for deployment. Ex: dynamicProcess.bpmn20.xml");

            LOGGER.debug("Creating deployment for process with key: {}, resource name: {}", processDefinitionRepresentation.getKey(), resourceName);

            var deployment = repositoryService.createDeployment()
                    .addString(resourceName, processDefinitionRepresentation.getBpmnXml())
                    .name(processDefinitionRepresentation.getName() != null && !processDefinitionRepresentation.getName().isBlank()
                            ? processDefinitionRepresentation.getName()
                            : processDefinitionRepresentation.getDescription())
                    .key(Objects.requireNonNull(processDefinitionRepresentation.getKey(), "The key is required for deployment."))
                    .category(processDefinitionRepresentation.getApplicationBase())
                    .deploy();

            LOGGER.debug("Deployment created with id: {}", deployment.getId());

            LOGGER.debug("Retrieving process definition for deployment id: {}", deployment.getId());

            var processDefinition = getProcessDefinition(deployment.getId());

            LOGGER.debug("Retrieving BPMN XML for deployment id: {} and resource name: {}", deployment.getId(), processDefinition.getResourceName());

            final var bpmnXml = getBpmnXml(deployment.getId(), processDefinition.getResourceName());

            LOGGER.debug("Building process definition representation for id: {}, key: {}", processDefinition.getId(), processDefinition.getKey());

            var result = IgrpProcessDefinitionRepresentation.builder()
                    .id(processDefinition.getId())
                    .key(processDefinition.getKey())
                    .name(processDefinition.getName())
                    .description(processDefinition.getDescription())
                    .version(String.valueOf(processDefinition.getVersion()))
                    .bpmnXml(bpmnXml)
                    .resourceName(processDefinition.getResourceName())
                    .applicationBase(processDefinition.getCategory())
                    .bpmnSourceType(BpmnSourceType.INLINE_XML)
                    .deployed(true)
                    .deploymentId(deployment.getId())
                    .deployedAt(deployment.getDeploymentTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

            LOGGER.info("Successfully deployed process definition with id: {}, key: {}, version: {}",
                    processDefinition.getId(), processDefinition.getKey(), processDefinition.getVersion());
            return result;

        } catch (Exception ex) {
            LOGGER.error("Failed to deploy BPMN XML", ex);
            throw new ProcessDefinitionException("An error occurred when deploy a new process definition bpmn xml", ex);
        }
    }

    @Override
    public void undeploy(String deploymentId) throws ProcessDefinitionException {
        LOGGER.info("Undeploying process definition with deployment id: {}", deploymentId);
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
        LOGGER.debug("Found process definition: id={}, key={}, version={}",
                result != null ? result.getId() : "null",
                result != null ? result.getKey() : "null",
                result != null ? result.getVersion() : "null");
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

}
