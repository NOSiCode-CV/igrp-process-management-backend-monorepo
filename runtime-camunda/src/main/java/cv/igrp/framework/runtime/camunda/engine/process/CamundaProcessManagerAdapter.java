package cv.igrp.framework.runtime.camunda.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessManagerAdapter;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessInstance;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CamundaProcessManagerAdapter implements ProcessManagerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaProcessManagerAdapter.class);

	@Override
	public ProcessInstance createProcess(String processDefinitionId, String businessKey) {
		return null;
	}

	@Override
	public ProcessInstance startCreatedProcess(String processInstanceId, String processDefinitionId, String businessKey, Map<String, Object> variables) {
		return null;
	}

	@Override
	public ProcessInstance startProcess(String processDefinitionId, String businessKey, Map<String, Object> variables) {
		return null;
	}

	@Override
	public void suspendProcess(String processInstanceId) {

	}

	@Override
	public void resumeProcess(String processInstanceId) {

	}

	@Override
	public void terminateProcess(String processInstanceId, String reason) {

	}

	@Override
	public Optional<ProcessInstance> getProcessInstance(String processInstanceId) {
		return Optional.empty();
	}

	@Override
	public Optional<ProcessInstance> getProcessInstanceByBusinessKey(String businessKey) {
		return Optional.empty();
	}

	@Override
	public List<ProcessInstance> listProcessInstances(ProcessFilter filter) {
		return List.of();
	}

	@Override
	public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {

	}

	@Override
	public List<ProcessVariableInstance> getProcessVariables(String processInstanceId) {
		return List.of();
	}

	@Override
	public List<ProcessVariableInstance> getRuntimeProcessVariables(String processInstanceId) {
		return List.of();
	}

	@Override
	public List<ProcessVariableInstance> getHistoricProcessVariables(String processInstanceId) {
		return List.of();
	}

	@Override
	public void correlateMessage(String businessKey, String messageName, Map<String, Object> variables) {

	}

	@Override
	public void signal(String processInstanceId, String taskId, Map<String, Object> processVariables) {

	}

	@Override
	public void rescheduleTimer(String processInstanceId, String timerEventId, long seconds) {

	}

	@Override
	public void rescheduleTimer(String processInstanceId, long seconds) {

	}

}
