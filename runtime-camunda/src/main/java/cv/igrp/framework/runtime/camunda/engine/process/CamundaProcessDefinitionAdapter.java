package cv.igrp.framework.runtime.camunda.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.BpmnSourceType;
import cv.igrp.framework.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact;
import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.search.FlowNodeInstanceFilter;
import io.camunda.operate.search.SearchQuery;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CamundaProcessDefinitionAdapter implements ProcessDefinitionAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaProcessDefinitionAdapter.class);

	private final ZeebeClient client;
	private final CamundaOperateClient camundaOperateClient;

	public CamundaProcessDefinitionAdapter(ZeebeClient client,
										   CamundaOperateClient camundaOperateClient) {
		this.client = client;
		this.camundaOperateClient = camundaOperateClient;
	}

	@Override
	public ProcessDefinitionRepresentation deploy(ProcessDefinitionRepresentation processDefinitionRepresentation) throws ProcessDefinitionException {
		try {

			LOGGER.info("Deploying process definition representation: {}", processDefinitionRepresentation);
			DeploymentEvent deploymentEvent = client.newDeployResourceCommand()
					.addResourceStringUtf8(
							processDefinitionRepresentation.bpmnXml(),
							processDefinitionRepresentation.resourceName()
					)
					.send()
					.join();

			var deployedProcess = deploymentEvent.getProcesses().getFirst();

			LOGGER.info("Successfully deployed process definition representation: {}", deployedProcess);

			return IgrpProcessDefinitionRepresentation.builder()
					.key(String.valueOf(deployedProcess.getProcessDefinitionKey()))
					.deploymentId(String.valueOf(deploymentEvent.getKey()))
					.name(deployedProcess.getBpmnProcessId())
					.description("Deployed BPMN process: " + deployedProcess.getBpmnProcessId())
					.version(String.valueOf(deployedProcess.getVersion()))
					.bpmnXml(processDefinitionRepresentation.bpmnXml())
					.resourceName(deployedProcess.getResourceName())
					.applicationBase(null)
					.bpmnSourceType(BpmnSourceType.INLINE_XML)
					.deployed(true)
					.deployedAt(java.time.LocalDateTime.now())
					.build();

		} catch (Exception e) {
			throw new ProcessDefinitionException("Error deploying process", e);
		}

	}

	@Override
	public void undeploy(String deploymentId) throws ProcessDefinitionException {
		LOGGER.warn("Undeploy operation is not supported in Zeebe. DeploymentId: {}", deploymentId);
		throw new ProcessDefinitionException("Undeploy not supported in Camunda 8 / Zeebe.");
	}

	@Override
	public List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {
		try {
			SearchQuery searchQuery = SearchQuery.builder()
					.size(filter.getPageSize())
					.build();
			return camundaOperateClient
					.searchProcessDefinitions(searchQuery)
					.stream()
					.map(processDefinition -> new ProcessDefinition(
							processDefinition.getKey().toString(),
							processDefinition.getName(),
							null,
							processDefinition.getKey().toString(),
							processDefinition.getVersion().intValue(),
							processDefinition.getBpmnProcessId(),
							processDefinition.getName(),
							processDefinition.getTenantId(),
							false))
					.toList();
		} catch (OperateException e) {
			LOGGER.error("Error while fetching deployed processes from Operate", e);
			return List.of();
		}
	}

	@Override
	public List<ProcessArtifact> getProcessArtifacts(String processDefinitionKey) {
		try {
			FlowNodeInstanceFilter filter = FlowNodeInstanceFilter.builder()
					.processDefinitionKey(Long.parseLong(processDefinitionKey))
					.build();
			SearchQuery searchQuery = SearchQuery.builder()
					.filter(filter)
					.size(1000)
					.build();
			return camundaOperateClient.searchFlowNodeInstances(searchQuery)
					.stream()
					.filter(flowNode -> "USER_TASK".equals(flowNode.getType()))
					.map(userTask -> new ProcessArtifact(
							String.valueOf(userTask.getFlowNodeId()),
							userTask.getFlowNodeName(),
							null))
					.distinct()
					.toList();
		} catch (OperateException e) {
			LOGGER.error("Error while fetching process artifacts from Operate for process definition: {}", processDefinitionKey, e);
			return List.of();
		}
	}

	@Override
	public String getLatesProcessDefinitionIdByKey(String processDefinitionKey) {
		return null;
	}




}
