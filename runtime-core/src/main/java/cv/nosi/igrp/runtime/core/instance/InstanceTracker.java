package cv.nosi.igrp.runtime.core.instance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface para rastreamento de instâncias de processos.
 * <p>
 * Define operações para localizar, consultar e acompanhar instâncias de processos
 * em execução ou concluídas.
 */
public interface InstanceTracker {
    
    /**
     * Obtém uma instância de processo pelo seu ID.
     * 
     * @param instanceId ID da instância
     * @return a instância encontrada ou Optional vazio se não existir
     */
    Optional<ProcessInstanceInfo> getProcessInstance(String instanceId);
    
    /**
     * Lista instâncias de processos com base em critérios de filtragem.
     * 
     * @param filter critérios de filtragem
     * @return lista de instâncias que correspondem aos critérios
     */
    List<ProcessInstanceInfo> listProcessInstances(ProcessInstanceFilter filter);
    
    /**
     * Obtém o histórico de atividades de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return lista de atividades executadas na instância
     */
    List<ActivityInfo> getProcessInstanceHistory(String instanceId);
    
    /**
     * Obtém variáveis de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return mapa com as variáveis da instância
     */
    Map<String, Object> getProcessInstanceVariables(String instanceId);
    
    /**
     * Obtém tarefas ativas de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return lista de tarefas ativas
     */
    List<TaskInfo> getActiveTasks(String instanceId);
    
    /**
     * Obtém o caminho de execução de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return lista de IDs de atividades executadas, na ordem de execução
     */
    List<String> getExecutionPath(String instanceId);
    
    /**
     * Verifica se uma instância de processo está ativa.
     * 
     * @param instanceId ID da instância
     * @return true se a instância estiver ativa
     */
    boolean isProcessInstanceActive(String instanceId);
    
    /**
     * Obtém a definição do processo associada a uma instância.
     * 
     * @param instanceId ID da instância
     * @return informações da definição do processo
     */
    ProcessDefinitionInfo getProcessDefinition(String instanceId);
    
    /**
     * Obtém instâncias de processo relacionadas a uma entidade de negócio.
     * 
     * @param businessKey chave de negócio
     * @return lista de instâncias relacionadas à chave de negócio
     */
    List<ProcessInstanceInfo> getProcessInstancesByBusinessKey(String businessKey);
    
    /**
     * Obtém instâncias de processo iniciadas por um usuário específico.
     * 
     * @param userId ID do usuário
     * @return lista de instâncias iniciadas pelo usuário
     */
    List<ProcessInstanceInfo> getProcessInstancesByInitiator(String userId);
    
    /**
     * Obtém instâncias de processo com tarefas atribuídas a um usuário específico.
     * 
     * @param userId ID do usuário
     * @return lista de instâncias com tarefas atribuídas ao usuário
     */
    List<ProcessInstanceInfo> getProcessInstancesWithTasksAssignedTo(String userId);
    
    /**
     * Classe interna para representar informações de uma instância de processo.
     */
    class ProcessInstanceInfo {
        private String id;
        private String processDefinitionId;
        private String processDefinitionKey;
        private String processDefinitionName;
        private String businessKey;
        private String startUserId;
        private long startTime;
        private Long endTime;
        private String status;
        private String currentActivityId;
        
        public ProcessInstanceInfo(String id, String processDefinitionId, String processDefinitionKey,
                                  String businessKey, String startUserId, long startTime, String status) {
            this.id = id;
            this.processDefinitionId = processDefinitionId;
            this.processDefinitionKey = processDefinitionKey;
            this.businessKey = businessKey;
            this.startUserId = startUserId;
            this.startTime = startTime;
            this.status = status;
        }
        
        // Getters
        public String getId() { return id; }
        public String getProcessDefinitionId() { return processDefinitionId; }
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public String getProcessDefinitionName() { return processDefinitionName; }
        public String getBusinessKey() { return businessKey; }
        public String getStartUserId() { return startUserId; }
        public long getStartTime() { return startTime; }
        public Long getEndTime() { return endTime; }
        public String getStatus() { return status; }
        public String getCurrentActivityId() { return currentActivityId; }
        
        // Setters
        public void setProcessDefinitionName(String processDefinitionName) { this.processDefinitionName = processDefinitionName; }
        public void setEndTime(Long endTime) { this.endTime = endTime; }
        public void setStatus(String status) { this.status = status; }
        public void setCurrentActivityId(String currentActivityId) { this.currentActivityId = currentActivityId; }
    }
    
    /**
     * Classe interna para representar informações de uma atividade executada.
     */
    class ActivityInfo {
        private String id;
        private String activityId;
        private String activityName;
        private String activityType;
        private String processInstanceId;
        private String executionId;
        private String assignee;
        private long startTime;
        private Long endTime;
        private long duration;
        
        public ActivityInfo(String id, String activityId, String activityName, String activityType,
                           String processInstanceId, long startTime) {
            this.id = id;
            this.activityId = activityId;
            this.activityName = activityName;
            this.activityType = activityType;
            this.processInstanceId = processInstanceId;
            this.startTime = startTime;
        }
        
        // Getters
        public String getId() { return id; }
        public String getActivityId() { return activityId; }
        public String getActivityName() { return activityName; }
        public String getActivityType() { return activityType; }
        public String getProcessInstanceId() { return processInstanceId; }
        public String getExecutionId() { return executionId; }
        public String getAssignee() { return assignee; }
        public long getStartTime() { return startTime; }
        public Long getEndTime() { return endTime; }
        public long getDuration() { return duration; }
        
        // Setters
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public void setAssignee(String assignee) { this.assignee = assignee; }
        public void setEndTime(Long endTime) { 
            this.endTime = endTime;
            if (endTime != null) {
                this.duration = endTime - startTime;
            }
        }
    }
    
    /**
     * Classe interna para representar informações de uma tarefa.
     */
    class TaskInfo {
        private String id;
        private String taskDefinitionKey;
        private String name;
        private String assignee;
        private String processInstanceId;
        private long createTime;
        private Long dueDate;
        
        public TaskInfo(String id, String taskDefinitionKey, String name, String assignee, 
                       String processInstanceId, long createTime) {
            this.id = id;
            this.taskDefinitionKey = taskDefinitionKey;
            this.name = name;
            this.assignee = assignee;
            this.processInstanceId = processInstanceId;
            this.createTime = createTime;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTaskDefinitionKey() { return taskDefinitionKey; }
        public String getName() { return name; }
        public String getAssignee() { return assignee; }
        public String getProcessInstanceId() { return processInstanceId; }
        public long getCreateTime() { return createTime; }
        public Long getDueDate() { return dueDate; }
        
        // Setters
        public void setDueDate(Long dueDate) { this.dueDate = dueDate; }
    }
    
    /**
     * Classe interna para representar informações de uma definição de processo.
     */
    class ProcessDefinitionInfo {
        private String id;
        private String key;
        private String name;
        private String description;
        private int version;
        private String deploymentId;
        private String resourceName;
        private boolean suspended;
        
        public ProcessDefinitionInfo(String id, String key, String name, int version, String deploymentId) {
            this.id = id;
            this.key = key;
            this.name = name;
            this.version = version;
            this.deploymentId = deploymentId;
        }
        
        // Getters
        public String getId() { return id; }
        public String getKey() { return key; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getVersion() { return version; }
        public String getDeploymentId() { return deploymentId; }
        public String getResourceName() { return resourceName; }
        public boolean isSuspended() { return suspended; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setResourceName(String resourceName) { this.resourceName = resourceName; }
        public void setSuspended(boolean suspended) { this.suspended = suspended; }
    }
    
    /**
     * Classe interna para representar critérios de filtragem de instâncias de processo.
     */
    class ProcessInstanceFilter {
        private String processDefinitionKey;
        private String processDefinitionId;
        private String businessKey;
        private String startUserId;
        private String status;
        private Long startedAfter;
        private Long startedBefore;
        private Long finishedAfter;
        private Long finishedBefore;
        private boolean includeFinished;
        private boolean includeActive;
        private boolean includeSuspended;
        
        public ProcessInstanceFilter() {
            this.includeActive = true;
        }
        
        // Builder methods
        public ProcessInstanceFilter processDefinitionKey(String processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
            return this;
        }
        
        public ProcessInstanceFilter processDefinitionId(String processDefinitionId) {
            this.processDefinitionId = processDefinitionId;
            return this;
        }
        
        public ProcessInstanceFilter businessKey(String businessKey) {
            this.businessKey = businessKey;
            return this;
        }
        
        public ProcessInstanceFilter startUserId(String startUserId) {
            this.startUserId = startUserId;
            return this;
        }
        
        public ProcessInstanceFilter status(String status) {
            this.status = status;
            return this;
        }
        
        public ProcessInstanceFilter startedAfter(Long startedAfter) {
            this.startedAfter = startedAfter;
            return this;
        }
        
        public ProcessInstanceFilter startedBefore(Long startedBefore) {
            this.startedBefore = startedBefore;
            return this;
        }
        
        public ProcessInstanceFilter finishedAfter(Long finishedAfter) {
            this.finishedAfter = finishedAfter;
            return this;
        }
        
        public ProcessInstanceFilter finishedBefore(Long finishedBefore) {
            this.finishedBefore = finishedBefore;
            return this;
        }
        
        public ProcessInstanceFilter includeFinished(boolean includeFinished) {
            this.includeFinished = includeFinished;
            return this;
        }
        
        public ProcessInstanceFilter includeActive(boolean includeActive) {
            this.includeActive = includeActive;
            return this;
        }
        
        public ProcessInstanceFilter includeSuspended(boolean includeSuspended) {
            this.includeSuspended = includeSuspended;
            return this;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public String getProcessDefinitionId() { return processDefinitionId; }
        public String getBusinessKey() { return businessKey; }
        public String getStartUserId() { return startUserId; }
        public String getStatus() { return status; }
        public Long getStartedAfter() { return startedAfter; }
        public Long getStartedBefore() { return startedBefore; }
        public Long getFinishedAfter() { return finishedAfter; }
        public Long getFinishedBefore() { return finishedBefore; }
        public boolean isIncludeFinished() { return includeFinished; }
        public boolean isIncludeActive() { return includeActive; }
        public boolean isIncludeSuspended() { return includeSuspended; }
    }
}