package cv.nosi.igrp.runtime.activiti;

import cv.nosi.igrp.runtime.core.ProcessEngine;
import cv.nosi.igrp.runtime.core.ProcessInstance;
import cv.nosi.igrp.runtime.core.ProcessInstanceState;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ProcessEngine interface using Activiti.
 * This class wraps the Activiti engine and provides the functionality defined in the ProcessEngine interface.
 */
@Service
public class ActivitiProcessEngine implements ProcessEngine {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ActivitiEventListener eventListener;

    /**
     * Constructor with required dependencies.
     *
     * @param runtimeService Activiti runtime service
     * @param taskService Activiti task service
     * @param eventListener Event listener for Activiti events
     */
    @Autowired
    public ActivitiProcessEngine(RuntimeService runtimeService, 
                                TaskService taskService,
                                ActivitiEventListener eventListener) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.eventListener = eventListener;
    }

    @Override
    public String startProcess(String processDefinitionKey, Map<String, Object> variables) {
        // Start a new process instance in Activiti
        org.activiti.engine.runtime.ProcessInstance activitiInstance = 
            runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        
        // Return the process instance ID
        return activitiInstance.getProcessInstanceId();
    }

    @Override
    public boolean suspendProcess(String processInstanceId) {
        try {
            // Suspend the process instance in Activiti
            runtimeService.suspendProcessInstanceById(processInstanceId);
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error suspending process instance: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean resumeProcess(String processInstanceId) {
        try {
            // Activate the process instance in Activiti
            runtimeService.activateProcessInstanceById(processInstanceId);
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error resuming process instance: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean terminateProcess(String processInstanceId, String reason) {
        try {
            // Delete the process instance in Activiti with a reason
            runtimeService.deleteProcessInstance(processInstanceId, reason);
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error terminating process instance: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<ProcessInstanceState> getProcessState(String processInstanceId) {
        try {
            // Get the process instance from Activiti
            org.activiti.engine.runtime.ProcessInstance activitiInstance = 
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            
            if (activitiInstance == null) {
                // Check if the process instance is completed
                boolean isCompleted = runtimeService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .finished()
                    .count() > 0;
                
                if (isCompleted) {
                    return Optional.of(ProcessInstanceState.COMPLETED);
                } else {
                    return Optional.empty();
                }
            }
            
            // Convert Activiti state to our state
            if (activitiInstance.isSuspended()) {
                return Optional.of(ProcessInstanceState.SUSPENDED);
            } else {
                return Optional.of(ProcessInstanceState.ACTIVE);
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting process state: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean signalProcess(String processInstanceId, String signalName, Map<String, Object> variables) {
        try {
            // Send a signal to the process instance in Activiti
            runtimeService.signalEventReceived(signalName, processInstanceId, variables);
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error signaling process: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean correlateMessage(String messageName, Map<String, Object> correlationKeys, Map<String, Object> variables) {
        try {
            // Correlate a message in Activiti
            runtimeService.createMessageCorrelation(messageName)
                .processInstanceBusinessKey((String) correlationKeys.get("businessKey"))
                .setVariables(variables)
                .correlate();
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error correlating message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ProcessInstance> getActiveProcessInstances() {
        try {
            // Get all active process instances from Activiti
            List<org.activiti.engine.runtime.ProcessInstance> activitiInstances = 
                runtimeService.createProcessInstanceQuery()
                    .active()
                    .list();
            
            // Convert Activiti process instances to our process instances
            return convertActivitiInstances(activitiInstances);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting active process instances: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProcessInstance> getProcessInstancesByDefinition(String processDefinitionKey) {
        try {
            // Get all process instances for a specific definition from Activiti
            List<org.activiti.engine.runtime.ProcessInstance> activitiInstances = 
                runtimeService.createProcessInstanceQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .list();
            
            // Convert Activiti process instances to our process instances
            return convertActivitiInstances(activitiInstances);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting process instances by definition: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProcessInstance> getProcessInstancesByUser(String userId) {
        try {
            // Get all process instances started by a specific user from Activiti
            List<org.activiti.engine.runtime.ProcessInstance> activitiInstances = 
                runtimeService.createProcessInstanceQuery()
                    .startedBy(userId)
                    .list();
            
            // Convert Activiti process instances to our process instances
            return convertActivitiInstances(activitiInstances);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting process instances by user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Converts a list of Activiti process instances to our process instances.
     *
     * @param activitiInstances The list of Activiti process instances
     * @return A list of our process instances
     */
    private List<ProcessInstance> convertActivitiInstances(List<org.activiti.engine.runtime.ProcessInstance> activitiInstances) {
        return activitiInstances.stream()
            .map(this::convertActivitiInstance)
            .collect(Collectors.toList());
    }

    /**
     * Converts an Activiti process instance to our process instance.
     *
     * @param activitiInstance The Activiti process instance
     * @return Our process instance
     */
    private ProcessInstance convertActivitiInstance(org.activiti.engine.runtime.ProcessInstance activitiInstance) {
        ProcessInstanceState state = activitiInstance.isSuspended() ? 
            ProcessInstanceState.SUSPENDED : ProcessInstanceState.ACTIVE;
        
        ProcessInstance instance = new ProcessInstance(
            activitiInstance.getProcessInstanceId(),
            activitiInstance.getProcessDefinitionId(),
            activitiInstance.getProcessDefinitionKey(),
            state
        );
        
        instance.setBusinessKey(activitiInstance.getBusinessKey());
        
        // Additional mapping can be done here
        
        return instance;
    }
}