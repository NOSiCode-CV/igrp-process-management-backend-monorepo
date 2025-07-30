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
        LOGGER.info("Deploying process definition representation: {}", processDefinitionRepresentation);
        try {

            var resourceName = Objects.requireNonNull(processDefinitionRepresentation.getResourceName(), "The resource name is required for deployment. Ex: dynamicProcess.bpmn20.xml");

            var deployment = repositoryService.createDeployment()
                    .addString(resourceName, processDefinitionRepresentation.getBpmnXml())
                    .name(processDefinitionRepresentation.getName() != null && !processDefinitionRepresentation.getName().isBlank()
                            ? processDefinitionRepresentation.getName()
                            : processDefinitionRepresentation.getDescription())
                    .key(Objects.requireNonNull(processDefinitionRepresentation.getKey(), "The key is required for deployment."))
                    .tenantId(processDefinitionRepresentation.getApplicationBase())
                    .deploy();

            LOGGER.debug("Deployment created: {}", deployment);

            final var bpmnXml = getBpmnXml(deployment.getId(), processDefinitionRepresentation.getResourceName());

            LOGGER.debug("BPMN XML retrieved: {}", bpmnXml);

            return IgrpProcessDefinitionRepresentation.builder()
                    .id(deployment.getId())
                    .key(deployment.getKey())
                    .name(deployment.getName())
                    .description(processDefinitionRepresentation.getDescription())
                    .version(String.valueOf(deployment.getVersion()))
                    .bpmnXml(bpmnXml)
                    .resourceName(processDefinitionRepresentation.getResourceName())
                    .applicationBase(deployment.getCategory())
                    .bpmnSourceType(BpmnSourceType.INLINE_XML)
                    .deployed(true)
                    .deploymentId(deployment.getId())
                    .deployedAt(deployment.getDeploymentTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

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

}
