package cv.nosi.igrp.runtime.activiti.engine;

import cv.nosi.igrp.runtime.core.engine.ProcessExecution;
import cv.nosi.igrp.runtime.core.engine.execution.ProcessInstanceFilter;
import cv.nosi.igrp.runtime.core.engine.execution.ProcessInstanceInfo;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.GetProcessInstancesPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ActivitiExecutionManager implements ProcessExecution {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiExecutionManager.class);

    private final ProcessRuntime processRuntime;

    public ActivitiExecutionManager(ProcessRuntime processRuntime) {
        this.processRuntime = processRuntime;
    }

    @Override
    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables, String startUserId) throws Exception {

        Objects.requireNonNull(startUserId, "startUserId cannot be null");
        Objects.requireNonNull(processDefinitionKey, "processDefinitionKey cannot be null");

        var payload = ProcessPayloadBuilder
                .start()
                .withVariable("startUserId", startUserId) // TODO 22/07/2025 15:52 validate the name of this variable
                .withProcessDefinitionKey(processDefinitionKey)
                .withBusinessKey(businessKey)
                .withVariables(variables)
                .build();

        return processRuntime.start(payload).getId();
    }

    @Override
    public void suspendProcess(String processInstanceId) throws Exception {

        var payload = ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(processInstanceId)
                .build();

        processRuntime.suspend(payload);
    }

    @Override
    public void resumeProcess(String processInstanceId) throws Exception {

        var payload = ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(processInstanceId)
                .build();

        processRuntime.resume(payload);
    }

    @Override
    public void terminateProcess(String processInstanceId, String deleteReason) throws Exception {

        var payload = ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId(processInstanceId)
                .withReason(deleteReason)
                .build();

        processRuntime.delete(payload);
    }

    @Override
    public Optional<ProcessInstanceInfo> getProcessInstance(String processInstanceId) {
        try {

            LOGGER.debug("Getting process instance with id {}", processInstanceId);

            var instance = processRuntime.processInstance(processInstanceId);

            return Optional.ofNullable(toProcessInstanceInfo(instance));

        } catch (Exception e) {

            LOGGER.debug("Error getting process instance with id {}", processInstanceId, e);

            return Optional.empty();
        }
    }

    @Override
    public List<ProcessInstanceInfo> listProcessInstances(ProcessInstanceFilter filter) {

        List<ProcessInstanceInfo> results = new ArrayList<>();

        var payload = new GetProcessInstancesPayload();
        payload.setBusinessKey(filter.getBusinessKey());
        payload.setProcessDefinitionKeys(Set.of(filter.getProcessDefinitionKey()));
        //payload.setSuspendedOnly(filter.getStatus());
        //payload.setActiveOnly();
        //payload.setParentProcessInstanceId();

        // Activiti 7 does not yet support dynamic queries with filters like startUserId, So we fetch all and filter in-memory

        var page = processRuntime.processInstances(Pageable.of(0, 100), payload);
        for (ProcessInstance instance : page.getContent()) {
            if (matchesFilter(instance, filter)) {
                results.add(toProcessInstanceInfo(instance));
            }
        }

        return results;
    }

    private boolean matchesFilter(ProcessInstance instance, ProcessInstanceFilter filter) {

        if (filter == null) return true;

        if (filter.getStatus() != null &&
            !filter.getStatus().equalsIgnoreCase(instance.getStatus().name())) {
            return false;
        }

        if (filter.getStartedAfter() != null &&
            (instance.getStartDate() == null || instance.getStartDate().getTime() < filter.getStartedAfter())) {
            return false;
        }

        if (filter.getStartedBefore() != null &&
            (instance.getStartDate() == null || instance.getStartDate().getTime() > filter.getStartedBefore())) {
            return false;
        }

        // Activiti 7 does not expose startUserId directly via ProcessInstance
        // You could map it via identity/variables/custom headers if needed
        return true;
    }

    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) throws Exception {

        var payload = ProcessPayloadBuilder
                .setVariables()
                .withProcessInstanceId(processInstanceId)
                .withVariables(variables)
                .build();

        processRuntime.setVariables(payload);
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) throws Exception {

        var payload = ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(processInstanceId)
                .build();

        return processRuntime.variables(payload).stream()
                .collect(
                        Collectors.toMap(
                                VariableInstance::getName,
                                VariableInstance::getValue
                        )
                );
    }

    private ProcessInstanceInfo toProcessInstanceInfo(ProcessInstance instance) {

        if (instance == null) return null;

        return new ProcessInstanceInfo(
                instance.getId(),
                instance.getProcessDefinitionId(),
                instance.getProcessDefinitionKey(),
                instance.getBusinessKey(),
                null, // startUserId is not exposed directly
                instance.getStartDate() != null ? instance.getStartDate().getTime() : 0,
                instance.getStatus().name()
        );
    }
}
