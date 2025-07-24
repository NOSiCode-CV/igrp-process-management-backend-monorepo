package cv.nosi.igrp.runtime.activiti.engine.process;

import cv.nosi.igrp.runtime.core.engine.process.model.IGRPProcessStatus;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManager;
import cv.nosi.igrp.runtime.core.engine.process.model.*;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Optional.*;

@Component
public class ActivitiProcessManager implements ProcessManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessManager.class);

    private static final String IGRP_START_USER_ID = "igrpStartUserId";

    private final ProcessRuntime processRuntime;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final RepositoryService repositoryService;

    public ActivitiProcessManager(ProcessRuntime processRuntime, RuntimeService runtimeService, HistoryService historyService, RepositoryService repositoryService) {
        this.processRuntime = processRuntime;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }

    @Override
    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables, String startUserId) throws Exception {

        Objects.requireNonNull(startUserId, "startUserId cannot be null");
        Objects.requireNonNull(processDefinitionKey, "processDefinitionKey cannot be null");

        var payloadVariables = variables != null ? new HashMap<>(variables) : new HashMap<String, Object>();
        payloadVariables.put(IGRP_START_USER_ID, startUserId); // TODO 22/07/2025 19:34 validate this name

        var payload = ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
                .withBusinessKey(businessKey)
                .withVariables(payloadVariables)
                .build();

        var processInstance = processRuntime.start(payload);

        LOGGER.debug("Process instance started {}", processInstance);

        return processInstance.getId();
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
    public Optional<ProcessInstance> getProcessInstance(String processInstanceId) {
        try {

            LOGGER.debug("Getting process instance with id {}", processInstanceId);

            var instance = processRuntime.processInstance(processInstanceId);

            return of(new ProcessInstance(
                    instance.getId(),
                    instance.getProcessDefinitionId(),
                    instance.getProcessDefinitionKey(),
                    instance.getBusinessKey(),
                    instance.getInitiator(),
                    ofNullable(instance.getStartDate()).map(Date::getTime).orElse(0L),
                    IGRPProcessStatus.valueOf(instance.getStatus().name())
            ));

        } catch (Exception e) {

            LOGGER.debug("Error getting process instance with id {}", processInstanceId, e);

            return empty();
        }
    }

    @Override
    public List<ProcessInstance> listProcessInstances(ProcessFilter filter) {

        final var status = filter.getStatus();

        if (status == IGRPProcessStatus.RUNNING || status == IGRPProcessStatus.SUSPENDED) {

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

            if (status == IGRPProcessStatus.RUNNING)
                query.active();
            else
                query.suspended();

            return query.list()
                    .stream()
                    .map(instance -> new ProcessInstance(
                            instance.getId(),
                            instance.getProcessDefinitionId(),
                            instance.getProcessDefinitionKey(),
                            instance.getBusinessKey(),
                            instance.getStartUserId(),
                            ofNullable(instance.getStartTime()).map(Date::getTime).orElse(0L),
                            instance.isSuspended() ? IGRPProcessStatus.SUSPENDED : IGRPProcessStatus.RUNNING
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

        ofNullable(status).ifPresent(s -> {
            switch (s) {
                case COMPLETED -> query.finished();
                case CANCELLED -> query.deleted();
                case CREATED -> query.unfinished();
            }
        });

        return query.list()
                .stream()
                .map(instance -> new ProcessInstance(
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


    private IGRPProcessStatus resolveStatus(HistoricProcessInstance instance) {
        if (instance.getEndTime() != null) return IGRPProcessStatus.COMPLETED;
        if (instance.getDeleteReason() != null) return IGRPProcessStatus.CANCELLED;
        return IGRPProcessStatus.RUNNING;
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
    public List<ProcessVariableInstance> getProcessVariables(String processInstanceId) throws Exception {

        var payload = ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(processInstanceId)
                .build();

        return processRuntime.variables(payload)
                .stream()
                .map(obj -> new ProcessVariableInstance(
                        obj.getName(),
                        obj.getType(),
                        obj.getProcessInstanceId(),
                        obj.getValue()
                ))
                .toList();
    }

    @Override
    public List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {

        var query = repositoryService.createProcessDefinitionQuery();

        if (filter.getId() != null)
            query.processDefinitionId(filter.getId());

        if (filter.getKey() != null)
            query.processDefinitionKey(filter.getKey());

        if (filter.getName() != null)
            query.processDefinitionName(filter.getName());

        if (filter.getCategory() != null)
            query.processDefinitionCategory(filter.getCategory());

        if (filter.getDeploymentId() != null)
            query.deploymentId(filter.getDeploymentId());

        if (filter.getTenantId() != null)
            query.processDefinitionTenantId(filter.getTenantId());

        if (Boolean.TRUE.equals(filter.getSuspended()))
            query.suspended();
        if (Boolean.FALSE.equals(filter.getSuspended()))
            query.active();

        return query.orderByProcessDefinitionKey().asc()
                .list()
                .stream()
                .map(def -> new ProcessDefinition(
                        def.getId(),
                        def.getName(),
                        def.getKey(),
                        def.getVersion(),
                        def.getDeploymentId(),
                        def.getDescription(),
                        def.getCategory(),
                        def.getTenantId(),
                        def.isSuspended()
                ))
                .toList();
    }
}
