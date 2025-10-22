package cv.igrp.framework.runtime.activiti.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.exception.ProcessDefinitionException;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CamundaProcessDefinitionAdapter implements ProcessDefinitionAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaProcessDefinitionAdapter.class);


	@Override
	public ProcessDefinitionRepresentation deploy(ProcessDefinitionRepresentation processDefinitionRepresentation) throws ProcessDefinitionException {
		return null;
	}

	@Override
	public void undeploy(String deploymentId) throws ProcessDefinitionException {

	}

	@Override
	public List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {
		return List.of();
	}

	@Override
	public List<ProcessArtifact> getProcessArtifacts(String processDefinitionKey) {
		return List.of();
	}

	@Override
	public String getLatesProcessDefinitionIdByKey(String processDefinitionKey) {
		return null;
	}

}
