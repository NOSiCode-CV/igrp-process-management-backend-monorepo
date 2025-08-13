package cv.igrp.framework.runtime.activiti.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.BpmnSourceType;
import cv.igrp.framework.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
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

}
