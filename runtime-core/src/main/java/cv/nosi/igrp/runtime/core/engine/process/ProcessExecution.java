package cv.nosi.igrp.runtime.core.engine.process;

import cv.nosi.igrp.runtime.core.engine.process.model.ProcessInstanceFilter;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessInstanceInfo;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessVariableInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface ProcessExecution {


    String startProcess(String processDefinitionKey, String businessKey,
                        Map<String, Object> variables, String startUserId) throws Exception;


    void suspendProcess(String processInstanceId) throws Exception;


    void resumeProcess(String processInstanceId) throws Exception;


    void terminateProcess(String processInstanceId, String reason) throws Exception;


    Optional<ProcessInstanceInfo> getProcessInstance(String processInstanceId);


    List<ProcessInstanceInfo> listProcessInstances(ProcessInstanceFilter filter);


    void setProcessVariables(String processInstanceId, Map<String, Object> variables) throws Exception;


    List<ProcessVariableInstance> getProcessVariables(String processInstanceId) throws Exception;


}