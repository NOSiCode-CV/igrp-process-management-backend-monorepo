package cv.igrp.framework.runtime.activiti.engine.process;

import cv.igrp.framework.runtime.core.engine.process.ProcessManagerAdapter;
import cv.igrp.framework.runtime.core.engine.process.model.*;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public ProcessInstance createProcess(String processDefinitionId, String businessKey) {
        Objects.requireNonNull(processDefinitionId, "processDefinitionId cannot be null");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AccessDeniedException("No authenticated user found");

        LOGGER.debug("Process created by {}", authentication.getName());

        LOGGER.info("Creating process with definition id: {}, business key: {}", processDefinitionId, businessKey);

        var payload = ProcessPayloadBuilder
                .create()
                .withProcessDefinitionId(processDefinitionId)
                .withBusinessKey(businessKey)
                .build();

        LOGGER.debug("Process create payload built successfully: {}", payload);

        LOGGER.info("Authenticated User create: {}", Authentication.getAuthenticatedUserId());

        var activitiProcessInstance = processRuntime.create(payload);

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

        LOGGER.debug("Process created by user: {}", activitiProcessInstance.getInitiator());
        LOGGER.debug("Process instance created. Details: {}", activitiProcessInstance);

        return igrpProcessInstance;
    }

    @Override
    public ProcessInstance startCreatedProcess(String processInstanceId, String processDefinitionId, String businessKey, Map<String, Object> variables) {

        Objects.requireNonNull(processInstanceId, "processInstanceId cannot be null");
        Objects.requireNonNull(processDefinitionId, "processDefinitionId cannot be null");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AccessDeniedException("No authenticated user found");

        LOGGER.debug("Process started by {}", authentication.getName());

        LOGGER.info("Starting created process with definition id: {}, business key: {}", processDefinitionId, businessKey);

        LOGGER.debug("Process variables prepared, count: {}", variables.size());

        var payload = ProcessPayloadBuilder
                .start()
                .withProcessDefinitionId(processDefinitionId)
                .withBusinessKey(businessKey)
                .withVariables(variables)
                .withVariable("startedBy", authentication.getName())
                .build();

        LOGGER.debug("Process start created payload built successfully: {}", payload);

        var activitiProcessInstance = processRuntime.startCreatedProcess(processInstanceId, payload);

        LOGGER.info("Authenticated User start: {}", Authentication.getAuthenticatedUserId());

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

        LOGGER.debug("Process instance started. Details: {}", activitiProcessInstance);

        return igrpProcessInstance;
    }

    @Override
    @Deprecated
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

			var runtimeInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult();

			if (runtimeInstance != null) {
				IGRPProcessStatus status = IGRPProcessStatus.RUNNING;
				if (runtimeInstance.isSuspended()) {
					status = IGRPProcessStatus.SUSPENDED;
				}

				var processInstance = new ProcessInstance(
						runtimeInstance.getId(),
						runtimeInstance.getName(),
						runtimeInstance.getStartTime(),
						null, // still running, so no endDate
						runtimeInstance.getStartUserId(),
						runtimeInstance.getProcessDefinitionId(),
						runtimeInstance.getProcessDefinitionKey(),
						runtimeInstance.getBusinessKey(),
						runtimeInstance.getParentId(),
						runtimeInstance.getProcessDefinitionVersion(),
						runtimeInstance.getProcessDefinitionName(),
						status
				);

				LOGGER.debug("Active process instance retrieved: {}", processInstance);
				return Optional.of(processInstance);
			}

			var historicInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult();

			if (historicInstance != null) {
				IGRPProcessStatus status = historicInstance.getEndTime() != null
						? IGRPProcessStatus.COMPLETED
						: IGRPProcessStatus.SUSPENDED;

				var processInstance = new ProcessInstance(
						historicInstance.getId(),
						historicInstance.getName(),
						historicInstance.getStartTime(),
						historicInstance.getEndTime(),
						historicInstance.getStartUserId(),
						historicInstance.getProcessDefinitionId(),
						historicInstance.getProcessDefinitionKey(),
						historicInstance.getBusinessKey(),
						historicInstance.getSuperProcessInstanceId(),
						historicInstance.getProcessDefinitionVersion(),
						historicInstance.getProcessDefinitionName(),
						status
				);

				LOGGER.debug("Historic process instance retrieved: {}", processInstance);
				return Optional.of(processInstance);
			}

			LOGGER.info("Process instance with id: {} not found", processInstanceId);
			return Optional.empty();

		} catch (Exception e) {
			LOGGER.error("Error getting process instance with id: {}", processInstanceId, e);
			return Optional.empty();
		}
	}

    @Override
    public Optional<ProcessInstance> getProcessInstanceByBusinessKey(String businessKey) {
        LOGGER.info("Retrieving process instance with business key: {}", businessKey);

        try {

            var runtimeInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();

            if (runtimeInstance != null) {
                IGRPProcessStatus status = IGRPProcessStatus.RUNNING;
                if (runtimeInstance.isSuspended()) {
                    status = IGRPProcessStatus.SUSPENDED;
                }

                var processInstance = new ProcessInstance(
                        runtimeInstance.getId(),
                        runtimeInstance.getName(),
                        runtimeInstance.getStartTime(),
                        null, // still running, so no endDate
                        runtimeInstance.getStartUserId(),
                        runtimeInstance.getProcessDefinitionId(),
                        runtimeInstance.getProcessDefinitionKey(),
                        runtimeInstance.getBusinessKey(),
                        runtimeInstance.getParentId(),
                        runtimeInstance.getProcessDefinitionVersion(),
                        runtimeInstance.getProcessDefinitionName(),
                        status
                );

                LOGGER.debug("Active process instance retrieved: {}", processInstance);
                return Optional.of(processInstance);
            }

            var historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();

            if (historicInstance != null) {
                IGRPProcessStatus status = historicInstance.getEndTime() != null
                        ? IGRPProcessStatus.COMPLETED
                        : IGRPProcessStatus.SUSPENDED;

                var processInstance = new ProcessInstance(
                        historicInstance.getId(),
                        historicInstance.getName(),
                        historicInstance.getStartTime(),
                        historicInstance.getEndTime(),
                        historicInstance.getStartUserId(),
                        historicInstance.getProcessDefinitionId(),
                        historicInstance.getProcessDefinitionKey(),
                        historicInstance.getBusinessKey(),
                        historicInstance.getSuperProcessInstanceId(),
                        historicInstance.getProcessDefinitionVersion(),
                        historicInstance.getProcessDefinitionName(),
                        status
                );

                LOGGER.debug("Historic process instance retrieved: {}", processInstance);
                return Optional.of(processInstance);
            }

            LOGGER.info("Process instance with business key: {} not found", businessKey);
            return Optional.empty();

        } catch (Exception e) {
            LOGGER.error("Error getting process instance with business key: {}", businessKey, e);
            return Optional.empty();
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

		runtimeService.setVariables(processInstanceId, variables);

        LOGGER.info("Variables set successfully for process instance with id: {}", processInstanceId);
    }

    @Override
	public List<ProcessVariableInstance> getProcessVariables(String processInstanceId) {
		LOGGER.info("Getting process variables for processInstanceId={}", processInstanceId);
		var runtimeInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		if (runtimeInstance != null) {
			return getRuntimeProcessVariables(processInstanceId);
		}
		return getHistoricProcessVariables(processInstanceId);
	}

	@Override
	public List<ProcessVariableInstance> getRuntimeProcessVariables(String processInstanceId) {
		LOGGER.debug("Fetching runtime variables for processInstanceId={}", processInstanceId);
		Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
		return vars.entrySet().stream()
				.map(e -> new ProcessVariableInstance(
						e.getKey(),
						e.getValue() != null ? e.getValue().getClass().getSimpleName() : "null",
						processInstanceId,
						e.getValue()
				))
				.toList();
	}

	@Override
	public List<ProcessVariableInstance> getHistoricProcessVariables(String processInstanceId) {
		LOGGER.debug("Fetching historic variables for processInstanceId={}", processInstanceId);

		List<HistoricVariableInstance> vars = historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();

		return vars.stream()
				.map(v -> new ProcessVariableInstance(
						v.getVariableName(),
						v.getVariableTypeName(),
						v.getProcessInstanceId(),
						v.getValue()
				))
				.toList();
	}

	@Override
	public void correlateMessage(String businessKey, String messageName, Map<String, Object> variables) {
		LOGGER.info("Correlating message with name: {} for businessKey: {}", messageName, businessKey);

        var runtimeInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(runtimeInstance.getProcessInstanceId())
				.messageEventSubscriptionName(messageName)
				.singleResult();

		if (execution != null) {
			runtimeService.messageEventReceived(messageName, execution.getId(), variables);
		} else {
			LOGGER.warn("No execution waiting for message {} and businessKey {}", messageName, businessKey);
		}
		LOGGER.info("Message with name: {} correlated for businessKey: {}", messageName, businessKey);
	}

	public void signal(String processInstanceId, Map<String, Object> processVariables) {
		LOGGER.info("Signaling process instance with id: {} with variables: {}", processInstanceId, processVariables);
		List<Execution> executions = this.runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId)
				.list();
		if (executions.isEmpty()) {
			LOGGER.warn("No executions found for process instance {}", processInstanceId);
			return;
		}
		boolean signaled = false;
		for (Execution execution : executions) {
			if (execution.getActivityId() != null) {
				((RuntimeServiceImpl) this.runtimeService).signal(execution.getId());
				LOGGER.info("Execution with id: {} at activity {} signaled successfully",
						execution.getId(), execution.getActivityId());
				signaled = true;
			}
		}
		if (!signaled) {
			LOGGER.warn("No signalable executions found for process instance {}", processInstanceId);
		}
	}


}
