package cv.nosi.igrp.runtime.activiti.engine.process;

import cv.nosi.igrp.runtime.core.engine.ProcessExecution;
import cv.nosi.igrp.runtime.core.engine.model.ProcessInstanceFilter;
import cv.nosi.igrp.runtime.core.engine.model.ProcessInstanceInfo;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.*;

@Component
public class ActivitiExecutionManager implements ProcessExecution {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiExecutionManager.class);

    private static final String IGRP_START_USER_ID = "igrpStartUserId";

    private final ProcessRuntime processRuntime;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public ActivitiExecutionManager(ProcessRuntime processRuntime, RuntimeService runtimeService, HistoryService historyService) {
        this.processRuntime = processRuntime;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    @Override
    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables, String startUserId) throws Exception {

        Objects.requireNonNull(startUserId, "startUserId cannot be null");
        Objects.requireNonNull(processDefinitionKey, "processDefinitionKey cannot be null");

        var payload = ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
                .withBusinessKey(businessKey)
                .withVariable(IGRP_START_USER_ID, startUserId) // TODO 22/07/2025 19:34 validate this name
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

            return of(new ProcessInstanceInfo(
                    instance.getId(),
                    instance.getProcessDefinitionId(),
                    instance.getProcessDefinitionKey(),
                    instance.getBusinessKey(),
                    instance.getInitiator(),
                    ofNullable(instance.getStartDate()).map(Date::getTime).orElse(0L),
                    instance.getStatus().name()
            ));

        } catch (Exception e) {

            LOGGER.debug("Error getting process instance with id {}", processInstanceId, e);

            return empty();
        }
    }

    @Override
    public List<ProcessInstanceInfo> listProcessInstances(ProcessInstanceFilter filter) {

        final var status = ofNullable(filter.getStatus()).map(String::toLowerCase).orElse("");

        if (status.equals("active") || status.equals("suspended") || status.isEmpty()) {

            var query = runtimeService.createProcessInstanceQuery();

            ofNullable(filter.getProcessDefinitionKey())
                    .ifPresent(query::processDefinitionKey);

            ofNullable(filter.getBusinessKey())
                    .ifPresent(query::processInstanceBusinessKey);

            ofNullable(filter.getStartUserId())
                    .ifPresent(query::startedBy);

            ofNullable(filter.getStartedAfter())
                    .ifPresent(date -> query.startedAfter(new Date(date)));

            ofNullable(filter.getStartedBefore())
                    .ifPresent(date -> query.startedBefore(new Date(date)));

            // TODO 23/07/2025 09:30 validate this string status names, see if enum is needed

            if (status.equals("active"))
                query.active();
            else if (status.equals("suspended"))
                query.suspended();

            return query.list()
                    .stream()
                    .map(instance -> new ProcessInstanceInfo(
                            instance.getId(),
                            instance.getProcessDefinitionId(),
                            instance.getProcessDefinitionKey(),
                            instance.getBusinessKey(),
                            instance.getStartUserId(),
                            ofNullable(instance.getStartTime()).map(Date::getTime).orElse(0L),
                            instance.isSuspended() ? "suspended" : "active"
                    ))
                    .toList();

        }
        var query = historyService.createHistoricProcessInstanceQuery();

        ofNullable(filter.getProcessDefinitionKey())
                .ifPresent(query::processDefinitionKey);

        ofNullable(filter.getBusinessKey())
                .ifPresent(query::processInstanceBusinessKey);

        ofNullable(filter.getStartUserId())
                .ifPresent(query::startedBy);

        ofNullable(filter.getStartedAfter())
                .ifPresent(date -> query.startedAfter(new Date(date)));

        ofNullable(filter.getStartedBefore())
                .ifPresent(date -> query.startedBefore(new Date(date)));

        switch (status) {
            case "completed" -> query.finished();
            case "cancelled" -> query.deleted();
            case "running" -> query.unfinished();
        }

        return query.list()
                .stream()
                .map(instance -> new ProcessInstanceInfo(
                        instance.getId(),
                        instance.getProcessDefinitionId(),
                        instance.getProcessDefinitionKey(),
                        instance.getBusinessKey(),
                        instance.getStartUserId(),
                        ofNullable(instance.getStartTime()).map(Date::getTime).orElse(0L),
                        resolveStatus(instance)
                ))
                .toList();
    }

    private String resolveStatus(HistoricProcessInstance instance) {
        if (instance.getEndTime() != null) return "completed";
        if (instance.getDeleteReason() != null) return "cancelled";
        return "running";
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

        return processRuntime.variables(payload)
                .stream()
                .collect(
                        Collectors.toMap(
                                VariableInstance::getName,
                                VariableInstance::getValue
                        )
                );
    }
}
