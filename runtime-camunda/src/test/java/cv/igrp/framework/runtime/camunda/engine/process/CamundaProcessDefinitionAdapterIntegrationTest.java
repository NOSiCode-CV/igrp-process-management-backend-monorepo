package cv.igrp.framework.runtime.camunda.engine.process;

import cv.igrp.framework.runtime.camunda.engine.RuntimeCamundaEngineApplication;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RuntimeCamundaEngineApplication.class)
class CamundaProcessDefinitionAdapterIntegrationTest {

	@Autowired
	private CamundaProcessDefinitionAdapter camundaProcessDefinitionAdapter;

	@Test
	void deploy() throws Exception {

		String bpmnXml = Files.readString(Paths.get(getClass().getClassLoader()
				.getResource("process-payments.bpmn").toURI()));

		ProcessDefinitionRepresentation processDefinitionRepresentation = IgrpProcessDefinitionRepresentation
				.builder()
				.name("process-payments")
				.description("Deployed BPMN process: process-payments.bpmn")
				.bpmnXml(bpmnXml)
				.resourceName("process-payments.bpmn")
				.applicationBase("igrp-app")
				.build();

		ProcessDefinitionRepresentation deployed = camundaProcessDefinitionAdapter.deploy(processDefinitionRepresentation);

		assertTrue(deployed.deployed());
		assertNotNull(deployed.key());
		assertNotNull(deployed.deploymentId());

		System.out.println(deployed);
	}

	@Test
	void undeploy() {
		assertThrows(UnsupportedOperationException.class, () -> {
			camundaProcessDefinitionAdapter.undeploy("123");
		});
	}

	@Test
	void getDeployedProcesses() {
		ProcessFilter filter = new ProcessFilter();
		filter.setPageSize(50);
		List<ProcessDefinition> processDefinitions = camundaProcessDefinitionAdapter.getDeployedProcesses(filter);
		assertFalse(processDefinitions.isEmpty());
	}

	@Test
	void getProcessArtifacts() {

	}

}