package cv.nosi.igrp.runtime.core.engine.process;

import cv.nosi.igrp.runtime.core.engine.process.model.ProcessDefinition;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessFilter;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessInstance;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessVariableInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface ProcessManagerAdapter {


    String startProcess(String processDefinitionKey, String businessKey,
                        Map<String, Object> variables);


    void suspendProcess(String processInstanceId);


    void resumeProcess(String processInstanceId);


    void terminateProcess(String processInstanceId, String reason);


    Optional<ProcessInstance> getProcessInstance(String processInstanceId);


    List<ProcessInstance> listProcessInstances(ProcessFilter filter);


    void setProcessVariables(String processInstanceId, Map<String, Object> variables);


    List<ProcessVariableInstance> getProcessVariables(String processInstanceId);

    List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter);


}