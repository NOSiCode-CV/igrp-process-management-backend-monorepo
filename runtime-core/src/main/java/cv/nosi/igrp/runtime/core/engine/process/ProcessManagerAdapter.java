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
                        Map<String, Object> variables, String startUserId) throws Exception;


    void suspendProcess(String processInstanceId) throws Exception;


    void resumeProcess(String processInstanceId) throws Exception;


    void terminateProcess(String processInstanceId, String reason) throws Exception;


    Optional<ProcessInstance> getProcessInstance(String processInstanceId);


    List<ProcessInstance> listProcessInstances(ProcessFilter filter);


    void setProcessVariables(String processInstanceId, Map<String, Object> variables) throws Exception;


    List<ProcessVariableInstance> getProcessVariables(String processInstanceId) throws Exception;

    List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter);


}