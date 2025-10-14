package cv.igrp.framework.runtime.core.engine.process;

import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessInstance;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessVariableInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface ProcessManagerAdapter {

    ProcessInstance startProcess(String processDefinitionId, String businessKey, Map<String, Object> variables);

    void suspendProcess(String processInstanceId);

    void resumeProcess(String processInstanceId);

    void terminateProcess(String processInstanceId, String reason);

    Optional<ProcessInstance> getProcessInstance(String processInstanceId);

    List<ProcessInstance> listProcessInstances(ProcessFilter filter);

    void setProcessVariables(String processInstanceId, Map<String, Object> variables);

    List<ProcessVariableInstance> getProcessVariables(String processInstanceId);

	List<ProcessVariableInstance> getRuntimeProcessVariables(String processInstanceId);

	List<ProcessVariableInstance> getHistoricProcessVariables(String processInstanceId);

	void correlateMessage(String businessKey, String messageName, Map<String, Object> variables);

	void signal(String processInstanceId, Map<String, Object> processVariables);

}