package cv.nosi.igrp.runtime.activiti.engine.process;

import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import cv.nosi.igrp.runtime.core.engine.process.model.*;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Optional.*;

@Component
public class ActivitiProcessManagerAdapter implements ProcessManagerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessManagerAdapter.class);

    private final ProcessRuntime processRuntime;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final RepositoryService repositoryService;

    public ActivitiProcessManagerAdapter(ProcessRuntime processRuntime, RuntimeService runtimeService, HistoryService historyService, RepositoryService repositoryService) {
        this.processRuntime = processRuntime;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }

    @Override
    public ProcessInstance startProcess(String processDefinitionId, String businessKey, Map<String, Object> variables) {

        Objects.requireNonNull(processDefinitionId, "processDefinitionId cannot be null");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AccessDeniedException("No authenticated user found");

        LOGGER.debug("Process started by {}", authentication.getName());

        LOGGER.info("Starting process with definition id: {}, business key: {}", processDefinitionId, businessKey);

        LOGGER.debug("Process variables prepared, count: {}", variables.size());

        var payload = ProcessPayloadBuilder
                .start()
                .withProcessDefinitionId(processDefinitionId)
                .withBusinessKey(businessKey)
                .withVariables(variables)
                .withVariable("startedBy", authentication.getName())
                .build();

        LOGGER.debug("Process start payload built successfully: {}", payload);

        var activitiProcessInstance = processRuntime.start(payload);

        var igrpProcessInstance = new ProcessInstance(
                activitiProcessInstance.getId(),
                activitiProcessInstance.getName(),
                activitiProcessInstance.getStartDate(),
                activitiProcessInstance.getCompletedDate(),
                activitiProcessInstance.getInitiator(),
                activitiProcessInstance.getProcessDefinitionId(),
                activitiProcessInstance.getProcessDefinitionKey(),
                activitiProcessInstance.getBusinessKey(),
                activitiProcessInstance.getParentId(),
                activitiProcessInstance.getProcessDefinitionVersion(),
                activitiProcessInstance.getProcessDefinitionName(),
                IGRPProcessStatus.valueOf(activitiProcessInstance.getStatus().name())
        );

        LOGGER.debug("Process instance details: {}", activitiProcessInstance);

        return igrpProcessInstance;
    }

    @Override
    public void suspendProcess(String processInstanceId) {

        LOGGER.info("Suspending process instance with id: {}", processInstanceId);

        var payload = ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(processInstanceId)
                .build();

        processRuntime.suspend(payload);

        LOGGER.info("Process instance with id: {} suspended successfully", processInstanceId);
    }

    @Override
    public void resumeProcess(String processInstanceId) {

        LOGGER.info("Resuming process instance with id: {}", processInstanceId);

        var payload = ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(processInstanceId)
                .build();

        processRuntime.resume(payload);

        LOGGER.info("Process instance with id: {} resumed successfully", processInstanceId);
    }

    @Override
    public void terminateProcess(String processInstanceId, String deleteReason) {

        LOGGER.info("Terminating process instance with id: {}, reason: {}", processInstanceId, deleteReason);

        var payload = ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId(processInstanceId)
                .withReason(deleteReason)
                .build();

        processRuntime.delete(payload);

        LOGGER.info("Process instance with id: {} terminated successfully", processInstanceId);
    }


    @Override
    public Optional<ProcessInstance> getProcessInstance(String processInstanceId) {

        LOGGER.info("Retrieving process instance with id: {}", processInstanceId);

        try {

            var instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (instance == null) {
                LOGGER.info("Process instance with id: {} not found", processInstanceId);
                return empty();
            }

            var endDate = ofNullable(historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult())
                    .map(HistoricProcessInstance::getEndTime)
                    .orElse(null);

            var status = IGRPProcessStatus.RUNNING;

            if (instance.isSuspended())
                status = IGRPProcessStatus.SUSPENDED;
            else if (instance.isEnded())
                status = IGRPProcessStatus.COMPLETED;

            var processInstance = new ProcessInstance(
                    instance.getId(),
                    instance.getName(),
                    instance.getStartTime(),
                    endDate,
                    instance.getStartUserId(),
                    instance.getProcessDefinitionId(),
                    instance.getProcessDefinitionKey(),
                    instance.getBusinessKey(),
                    instance.getParentId(),
                    instance.getProcessDefinitionVersion(),
                    instance.getProcessDefinitionName(),
                    status
            );

            LOGGER.debug("Process instance retrieved successfully: {}", processInstance);
            return of(processInstance);

        } catch (Exception e) {
            LOGGER.error("Error getting process instance with id: {}", processInstanceId, e);
            return empty();
        }
    }

    @Override
    public List<ProcessInstance> listProcessInstances(ProcessFilter filter) {
        LOGGER.info("Listing process instances with filter: status={}, definitionKey={}, businessKey={}",
                filter.getStatus(), filter.getProcessDefinitionKey(), filter.getBusinessKey());

        LOGGER.debug("Processing filter parameters: {}", filter);
        final var status = filter.getStatus();

        if (status == IGRPProcessStatus.RUNNING || status == IGRPProcessStatus.SUSPENDED) {
            LOGGER.debug("Processing {} process instances query", status == IGRPProcessStatus.RUNNING ? "RUNNING" : "SUSPENDED");

            LOGGER.debug("Creating runtime process instance query");
            var query = runtimeService.createProcessInstanceQuery();

            ofNullable(filter.getProcessDefinitionKey())
                    .ifPresent(key -> {
                        LOGGER.debug("Filtering by process definition key: {}", key);
                        query.processDefinitionKey(key);
                    });

            ofNullable(filter.getBusinessKey())
                    .ifPresent(key -> {
                        LOGGER.debug("Filtering by business key: {}", key);
                        query.processInstanceBusinessKey(key);
                    });

            ofNullable(filter.getStartUserId())
                    .ifPresent(userId -> {
                        LOGGER.debug("Filtering by start user id: {}", userId);
                        query.startedBy(userId);
                    });

            ofNullable(filter.getStartedAfter())
                    .ifPresent(date -> {
                        LOGGER.debug("Filtering by started after: {}", new Date(date));
                        query.startedAfter(new Date(date));
                    });

            ofNullable(filter.getStartedBefore())
                    .ifPresent(date -> {
                        LOGGER.debug("Filtering by started before: {}", new Date(date));
                        query.startedBefore(new Date(date));
                    });

            if (status == IGRPProcessStatus.RUNNING) {
                LOGGER.debug("Filtering for active process instances");
                query.active();
            } else {
                LOGGER.debug("Filtering for suspended process instances");
                query.suspended();
            }

            var results = query.list(); // TODO 29/07/2025 16:01 add pagination support

            LOGGER.info("Found {} process instances matching the filter criteria", results.size());

            return results
                    .stream()
                    .map(instance -> new ProcessInstance(
                            instance.getId(),
                            instance.getName(),
                            instance.getStartTime(),
                            null,
                            instance.getStartUserId(),
                            instance.getProcessDefinitionId(),
                            instance.getProcessDefinitionKey(),
                            instance.getBusinessKey(),
                            instance.getParentId(),
                            instance.getProcessDefinitionVersion(),
                            instance.getProcessDefinitionName(),
                            instance.isSuspended() ? IGRPProcessStatus.SUSPENDED : IGRPProcessStatus.RUNNING
                    ))
                    .toList();
        }

        LOGGER.debug("Creating historic process instance query");

        var query = historyService.createHistoricProcessInstanceQuery();

        ofNullable(filter.getProcessDefinitionKey())
                .ifPresent(key -> {
                    LOGGER.debug("Filtering by process definition key: {}", key);
                    query.processDefinitionKey(key);
                });

        ofNullable(filter.getBusinessKey())
                .ifPresent(key -> {
                    LOGGER.debug("Filtering by business key: {}", key);
                    query.processInstanceBusinessKey(key);
                });

        ofNullable(filter.getStartUserId())
                .ifPresent(userId -> {
                    LOGGER.debug("Filtering by start user id: {}", userId);
                    query.startedBy(userId);
                });

        ofNullable(filter.getStartedAfter())
                .ifPresent(date -> {
                    LOGGER.debug("Filtering by started after: {}", new Date(date));
                    query.startedAfter(new Date(date));
                });

        ofNullable(filter.getStartedBefore())
                .ifPresent(date -> {
                    LOGGER.debug("Filtering by started before: {}", new Date(date));
                    query.startedBefore(new Date(date));
                });

        ofNullable(status).ifPresent(s -> {
            LOGGER.debug("Applying status filter: {}", s);
            switch (s) {
                case COMPLETED -> {
                    LOGGER.debug("Filtering for completed process instances");
                    query.finished();
                }
                case CANCELLED -> {
                    LOGGER.debug("Filtering for cancelled process instances");
                    query.deleted();
                }
                case CREATED -> {
                    LOGGER.debug("Filtering for created/unfinished process instances");
                    query.unfinished();
                }
            }
        });

        LOGGER.debug("Executing historic query and mapping results");
        var results = query.list();
        LOGGER.info("Found {} historic process instances matching the filter criteria", results.size());

        return results
                .stream()
                .map(instance -> {
                    var resolvedStatus = resolveStatus(instance);
                    return new ProcessInstance(
                            instance.getId(),
                            instance.getName(),
                            instance.getStartTime(),
                            instance.getEndTime(),
                            instance.getStartUserId(),
                            instance.getProcessDefinitionId(),
                            instance.getProcessDefinitionKey(),
                            instance.getBusinessKey(),
                            null,
                            instance.getProcessDefinitionVersion(),
                            instance.getProcessDefinitionName(),
                            resolvedStatus
                    );
                })
                .toList();
    }


    private IGRPProcessStatus resolveStatus(HistoricProcessInstance instance) {

        LOGGER.debug("Resolving status for historic process instance: id={}", instance.getId());

        if (instance.getEndTime() != null) {
            LOGGER.debug("Process instance has end time, status: COMPLETED");
            return IGRPProcessStatus.COMPLETED;
        }

        if (instance.getDeleteReason() != null) {
            LOGGER.debug("Process instance has delete reason: {}, status: CANCELLED", instance.getDeleteReason());
            return IGRPProcessStatus.CANCELLED;
        }

        LOGGER.debug("Process instance is still running, status: RUNNING");
        return IGRPProcessStatus.RUNNING;
    }

    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {

        LOGGER.info("Setting variables for process instance with id: {}", processInstanceId);

        LOGGER.debug("Variables to set: count={}, keys={}",
                variables != null ? variables.size() : 0,
                variables != null ? variables.keySet() : "null");

        LOGGER.debug("Building set variables payload for process instance id: {}", processInstanceId);
        var payload = ProcessPayloadBuilder
                .setVariables()
                .withProcessInstanceId(processInstanceId)
                .withVariables(variables)
                .build();

        processRuntime.setVariables(payload);

        LOGGER.info("Variables set successfully for process instance with id: {}", processInstanceId);
    }

    @Override
    public List<ProcessVariableInstance> getProcessVariables(String processInstanceId) {

        LOGGER.info("Getting variables for process instance with id: {}", processInstanceId);

        var payload = ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(processInstanceId)
                .build();

        LOGGER.debug("GET VARIABLES PAYLOAD: {}", payload);

        var variables = processRuntime.variables(payload);

        LOGGER.debug("Retrieved {} variables for process instance id: {}", variables.size(), processInstanceId);

        var result = variables
                .stream()
                .map(obj -> new ProcessVariableInstance(
                        obj.getName(),
                        obj.getType(),
                        obj.getProcessInstanceId(),
                        obj.getValue()
                ))
                .toList();

        LOGGER.debug("Successfully retrieved {} variables for process instance with id: {}", result.size(), processInstanceId);

        return result;
    }

    @Override
    public List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {

        LOGGER.info("Getting deployed processes with filter: {}", filter);

        Predicate<String> isValidString = obj -> obj != null && !obj.isBlank();

        var query = repositoryService.createProcessDefinitionQuery();

        if (isValidString.test(filter.getId())) {
            LOGGER.debug("Filtering by process definition id: {}", filter.getId());
            query.processDefinitionId(filter.getId());
        }

        if (isValidString.test(filter.getKey())) {
            LOGGER.debug("Filtering by process definition key: {}", filter.getKey());
            query.processDefinitionKey(filter.getKey());
        }

        if (isValidString.test(filter.getName())) {
            var pattern = "%" + filter.getName().trim() + "%";
            LOGGER.debug("Filtering by process definition name like: {}", pattern);
            query.processDefinitionNameLike(pattern);
        }

        if (isValidString.test(filter.getDeploymentId())) {
            LOGGER.debug("Filtering by deployment id: {}", filter.getDeploymentId());
            query.deploymentId(filter.getDeploymentId());
        }

        if (isValidString.test(filter.getTenantId())) {
            LOGGER.debug("Filtering by tenant id: {}", filter.getTenantId());
            query.processDefinitionTenantId(filter.getTenantId());
        }

        if (Boolean.TRUE.equals(filter.getSuspended())) {
            LOGGER.debug("Filtering for suspended process definitions");
            query.suspended();
        }
        if (Boolean.FALSE.equals(filter.getSuspended())) {
            LOGGER.debug("Filtering for active process definitions");
            query.active();
        }

        if (filter.isLatestVersion()) {
            LOGGER.debug("Filtering for latest process definitions");
            query.latestVersion();
        }

        if (isValidString.test(filter.getApplicationBase())) {
            LOGGER.debug("Filtering by ApplicationBase: {}", filter.getApplicationBase());
            query.processDefinitionTenantId(filter.getApplicationBase());
        }

        var startIndex = ofNullable(filter.getPageNumber()).orElse(0);
        var maxResults = ofNullable(filter.getPageSize()).orElse(50);
        LOGGER.debug("Final Pagination: startIndex={}, maxResults={}", startIndex, maxResults);

        var definitions = query.listPage(startIndex, maxResults);

        LOGGER.info("Found {} deployed process definitions matching the filter criteria", definitions.size());

        return definitions
                .stream()
                .map(def -> new ProcessDefinition(
                        def.getId(),
                        def.getName(),
                        def.getResourceName(),
                        def.getKey(),
                        def.getVersion(),
                        def.getDeploymentId(),
                        def.getDescription(),
                        def.getTenantId(), // TODO 05/08/2025 16:37 validate tenant id containing application base
                        def.isSuspended()))
                .toList();
    }
}
