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
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Optional.*;

@Component
public class ActivitiProcessManagerAdapter implements ProcessManagerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessManagerAdapter.class);

    private static final String IGRP_START_USER_ID = "igrpStartUserId";

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
    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) throws Exception {
        LOGGER.info("Starting process with definition key: {}, business key: {}",
                processDefinitionKey, businessKey);

        LOGGER.debug("Validating process start parameters");
        Objects.requireNonNull(processDefinitionKey, "processDefinitionKey cannot be null");

        LOGGER.debug("Process variables prepared, count: {}", variables.size());

        LOGGER.debug("Building process start payload for definition key: {}", processDefinitionKey);
        var payload = ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
                .withBusinessKey(businessKey)
                .withVariables(variables)
                .build();

        LOGGER.debug("Starting process instance with definition key: {}", processDefinitionKey);
        var processInstance = processRuntime.start(payload);

        LOGGER.info("Process instance started successfully with id: {}, definition id: {}",
                processInstance.getId(), processInstance.getProcessDefinitionId());
        LOGGER.debug("Process instance details: {}", processInstance);

        return processInstance.getId();
    }

    @Override
    public void suspendProcess(String processInstanceId) throws Exception {
        LOGGER.info("Suspending process instance with id: {}", processInstanceId);

        LOGGER.debug("Building suspend payload for process instance id: {}", processInstanceId);
        var payload = ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(processInstanceId)
                .build();

        LOGGER.debug("Executing suspend operation for process instance id: {}", processInstanceId);
        processRuntime.suspend(payload);

        LOGGER.info("Process instance with id: {} suspended successfully", processInstanceId);
    }

    @Override
    public void resumeProcess(String processInstanceId) throws Exception {
        LOGGER.info("Resuming process instance with id: {}", processInstanceId);

        LOGGER.debug("Building resume payload for process instance id: {}", processInstanceId);
        var payload = ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(processInstanceId)
                .build();

        LOGGER.debug("Executing resume operation for process instance id: {}", processInstanceId);
        processRuntime.resume(payload);

        LOGGER.info("Process instance with id: {} resumed successfully", processInstanceId);
    }

    @Override
    public void terminateProcess(String processInstanceId, String deleteReason) throws Exception {
        LOGGER.info("Terminating process instance with id: {}, reason: {}", processInstanceId, deleteReason);

        LOGGER.debug("Building delete payload for process instance id: {}", processInstanceId);
        var payload = ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId(processInstanceId)
                .withReason(deleteReason)
                .build();

        LOGGER.debug("Executing delete operation for process instance id: {}", processInstanceId);
        processRuntime.delete(payload);

        LOGGER.info("Process instance with id: {} terminated successfully", processInstanceId);
    }

    @Override
    public Optional<ProcessInstance> getProcessInstance(String processInstanceId) {
        LOGGER.info("Retrieving process instance with id: {}", processInstanceId);
        try {
            LOGGER.debug("Querying runtime for process instance with id: {}", processInstanceId);
            var instance = processRuntime.processInstance(processInstanceId);

            LOGGER.debug("Process instance found: id={}, definitionId={}, key={}, status={}",
                    instance.getId(), instance.getProcessDefinitionId(),
                    instance.getProcessDefinitionKey(), instance.getStatus().name());

            var status = IGRPProcessStatus.valueOf(instance.getStatus().name());
            LOGGER.info("Successfully retrieved process instance with id: {}, status: {}",
                    processInstanceId, status);

            var processInstance = new ProcessInstance();
            processInstance.setId(instance.getId());
            processInstance.setProcessDefinitionId(instance.getProcessDefinitionId());
            processInstance.setProcessDefinitionKey(instance.getProcessDefinitionKey());
            processInstance.setBusinessKey(instance.getBusinessKey());
            processInstance.setStartUserId(instance.getInitiator());
            processInstance.setStartTime(ofNullable(instance.getStartDate()).map(Date::getTime).orElse(0L));
            processInstance.setStatus(status);
            return of(processInstance);

        } catch (Exception e) {
            LOGGER.info("Process instance with id: {} not found or error occurred", processInstanceId);
            LOGGER.debug("Error getting process instance with id: {}", processInstanceId, e);
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

            LOGGER.debug("Applying filter parameters to query");
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

            LOGGER.debug("Executing query and mapping results");
            var results = query.list(); // TODO 29/07/2025 16:01 add pagination support
            LOGGER.info("Found {} process instances matching the filter criteria", results.size());

            return results
                    .stream()
                    .map(instance -> {
                        LOGGER.debug("Mapping process instance: id={}, definitionId={}, key={}",
                                instance.getId(), instance.getProcessDefinitionId(), instance.getProcessDefinitionKey());
                        var processInstance = new ProcessInstance();
                        processInstance.setId(instance.getId());
                        processInstance.setProcessDefinitionId(instance.getProcessDefinitionId());
                        processInstance.setProcessDefinitionKey(instance.getProcessDefinitionKey());
                        processInstance.setBusinessKey(instance.getBusinessKey());
                        processInstance.setStartUserId(instance.getStartUserId());
                        processInstance.setStartTime(ofNullable(instance.getStartTime()).map(Date::getTime).orElse(0L));
                        processInstance.setStatus(instance.isSuspended() ? IGRPProcessStatus.SUSPENDED : IGRPProcessStatus.RUNNING);
                        return processInstance;
                    })
                    .toList();
        }

        LOGGER.debug("Processing historic process instances query");

        LOGGER.debug("Creating historic process instance query");
        var query = historyService.createHistoricProcessInstanceQuery();

        LOGGER.debug("Applying filter parameters to historic query");
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
                    LOGGER.debug("Mapping historic process instance: id={}, definitionId={}, key={}, status={}",
                            instance.getId(), instance.getProcessDefinitionId(),
                            instance.getProcessDefinitionKey(), resolvedStatus);
                    var processInstance = new ProcessInstance();
                    processInstance.setId(instance.getId());
                    processInstance.setProcessDefinitionId(instance.getProcessDefinitionId());
                    processInstance.setProcessDefinitionKey(instance.getProcessDefinitionKey());
                    processInstance.setBusinessKey(instance.getBusinessKey());
                    processInstance.setStartUserId(instance.getStartUserId());
                    processInstance.setStartTime(ofNullable(instance.getStartTime()).map(Date::getTime).orElse(0L));
                    processInstance.setStatus(resolvedStatus);
                    return processInstance;
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
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) throws Exception {
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

        LOGGER.debug("Executing set variables operation for process instance id: {}", processInstanceId);
        processRuntime.setVariables(payload);

        LOGGER.info("Variables set successfully for process instance with id: {}", processInstanceId);
    }

    @Override
    public List<ProcessVariableInstance> getProcessVariables(String processInstanceId) throws Exception {
        LOGGER.info("Getting variables for process instance with id: {}", processInstanceId);

        LOGGER.debug("Building variables payload for process instance id: {}", processInstanceId);
        var payload = ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(processInstanceId)
                .build();

        LOGGER.debug("Executing get variables operation for process instance id: {}", processInstanceId);
        var variables = processRuntime.variables(payload);
        LOGGER.debug("Retrieved {} variables for process instance id: {}", variables.size(), processInstanceId);

        LOGGER.debug("Mapping variable objects to ProcessVariableInstance");
        var result = variables
                .stream()
                .map(obj -> {
                    LOGGER.debug("Mapping variable: name={}, type={}, value={}",
                            obj.getName(), obj.getType(), obj.getValue());
                    var processVariableInstance = new ProcessVariableInstance();
                    processVariableInstance.setName(obj.getName());
                    processVariableInstance.setType(obj.getType());
                    processVariableInstance.setProcessInstanceId(obj.getProcessInstanceId());
                    processVariableInstance.setValue(obj.getValue());
                    return processVariableInstance;
                })
                .toList();

        LOGGER.info("Successfully retrieved {} variables for process instance with id: {}",
                result.size(), processInstanceId);
        return result;
    }

    @Override
    public List<ProcessDefinition> getDeployedProcesses(ProcessFilter filter) {

        LOGGER.info("Getting deployed processes with filter: {}}", filter);

        var query = repositoryService.createProcessDefinitionQuery();

        if (filter.getId() != null) {
            LOGGER.debug("Filtering by process definition id: {}", filter.getId());
            query.processDefinitionId(filter.getId());
        }

        if (filter.getKey() != null) {
            LOGGER.debug("Filtering by process definition key: {}", filter.getKey());
            query.processDefinitionKey(filter.getKey());
        }

        if (filter.getName() != null) {
            LOGGER.debug("Filtering by process definition name: {}", filter.getName());
            query.processDefinitionName(filter.getName());
        }

        if (filter.getApplicationBase() != null) {
            LOGGER.debug("Filtering by category: {}", filter.getApplicationBase());
            query.processDefinitionCategory(filter.getApplicationBase());
        }

        if (filter.getDeploymentId() != null) {
            LOGGER.debug("Filtering by deployment id: {}", filter.getDeploymentId());
            query.deploymentId(filter.getDeploymentId());
        }

        if (filter.getTenantId() != null) {
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

        if (filter.getApplicationBase() != null) {
            LOGGER.debug("Filtering for category: {}", filter.getApplicationBase());
            query.processDefinitionCategory(filter.getApplicationBase());
        }

        var startIndex = ofNullable(filter.getStartIndex()).orElse(0);
        var maxResults = ofNullable(filter.getMaxResults()).orElse(50);
        LOGGER.debug("Pagination: startIndex={}, maxResults={}", startIndex, maxResults);

        var definitions = query.orderByProcessDefinitionKey()
                .asc()
                .listPage(startIndex, maxResults);

        LOGGER.info("Found {} deployed process definitions matching the filter criteria", definitions.size());

        return definitions
                .stream()
                .map(def -> {
                    var processDefinition = new ProcessDefinition();
                    processDefinition.setId(def.getId());
                    processDefinition.setName(def.getName());
                    processDefinition.setKey(def.getKey());
                    processDefinition.setVersion(def.getVersion());
                    processDefinition.setDeploymentId(def.getDeploymentId());
                    processDefinition.setDescription(def.getDescription());
                    processDefinition.setCategory(def.getCategory());
                    processDefinition.setTenantId(def.getTenantId());
                    processDefinition.setSuspended(def.isSuspended());
                    return processDefinition;
                })
                .toList();
    }
}
