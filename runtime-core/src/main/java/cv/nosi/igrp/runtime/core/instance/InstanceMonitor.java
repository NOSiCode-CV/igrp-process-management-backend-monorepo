package cv.nosi.igrp.runtime.core.instance;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interface para monitoramento de instâncias de processos.
 * <p>
 * Define operações para monitorar e receber notificações sobre eventos
 * relacionados a instâncias de processos em execução.
 */
public interface InstanceMonitor {
    
    /**
     * Registra um listener para eventos de instâncias de processos.
     * 
     * @param eventType tipo de evento a ser monitorado
     * @param listener função a ser chamada quando o evento ocorrer
     * @return ID do registro do listener
     */
    String registerInstanceEventListener(InstanceEventType eventType, Consumer<InstanceEvent> listener);
    
    /**
     * Remove um listener registrado.
     * 
     * @param listenerId ID do registro do listener
     * @return true se o listener foi removido com sucesso
     */
    boolean unregisterInstanceEventListener(String listenerId);
    
    /**
     * Configura um alerta para uma condição específica em uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @param alertCondition condição para o alerta
     * @param alertAction ação a ser executada quando a condição for satisfeita
     * @return ID do alerta configurado
     */
    String setInstanceAlert(String instanceId, AlertCondition alertCondition, AlertAction alertAction);
    
    /**
     * Remove um alerta configurado.
     * 
     * @param alertId ID do alerta
     * @return true se o alerta foi removido com sucesso
     */
    boolean removeInstanceAlert(String alertId);
    
    /**
     * Obtém alertas ativos para uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return lista de alertas ativos
     */
    List<AlertInfo> getActiveAlerts(String instanceId);
    
    /**
     * Configura um monitor de SLA (Service Level Agreement) para uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @param slaDefinition definição do SLA
     * @return ID do monitor de SLA configurado
     */
    String setSlaMonitor(String instanceId, SlaDefinition slaDefinition);
    
    /**
     * Verifica o status de SLA de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return status do SLA
     */
    SlaStatus checkSlaStatus(String instanceId);
    
    /**
     * Obtém instâncias de processo que violaram SLAs.
     * 
     * @return lista de IDs de instâncias com SLAs violados
     */
    List<String> getSlaViolations();
    
    /**
     * Configura um monitor de progresso para uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @param milestones marcos de progresso a serem monitorados
     * @return ID do monitor de progresso configurado
     */
    String setProgressMonitor(String instanceId, List<Milestone> milestones);
    
    /**
     * Obtém o progresso atual de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @return informações de progresso
     */
    ProgressInfo getInstanceProgress(String instanceId);
    
    /**
     * Registra um evento personalizado para uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @param eventType tipo do evento
     * @param eventData dados do evento
     * @return ID do evento registrado
     */
    String recordCustomEvent(String instanceId, String eventType, Map<String, Object> eventData);
    
    /**
     * Obtém eventos personalizados de uma instância de processo.
     * 
     * @param instanceId ID da instância
     * @param eventType tipo do evento (opcional)
     * @return lista de eventos personalizados
     */
    List<CustomEvent> getCustomEvents(String instanceId, String eventType);
    
    /**
     * Enumeration para tipos de eventos de instâncias.
     */
    enum InstanceEventType {
        STARTED,
        COMPLETED,
        SUSPENDED,
        RESUMED,
        TERMINATED,
        ACTIVITY_STARTED,
        ACTIVITY_COMPLETED,
        TASK_CREATED,
        TASK_COMPLETED,
        VARIABLE_CHANGED,
        ERROR_OCCURRED,
        SLA_VIOLATED,
        MILESTONE_REACHED
    }
    
    /**
     * Classe interna para representar um evento de instância.
     */
    class InstanceEvent {
        private String id;
        private String instanceId;
        private InstanceEventType eventType;
        private long timestamp;
        private Map<String, Object> eventData;
        
        public InstanceEvent(String id, String instanceId, InstanceEventType eventType, 
                            long timestamp, Map<String, Object> eventData) {
            this.id = id;
            this.instanceId = instanceId;
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.eventData = eventData;
        }
        
        // Getters
        public String getId() { return id; }
        public String getInstanceId() { return instanceId; }
        public InstanceEventType getEventType() { return eventType; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Object> getEventData() { return eventData; }
    }
    
    /**
     * Classe interna para representar uma condição de alerta.
     */
    class AlertCondition {
        private String id;
        private String type; // DURATION, ACTIVITY, VARIABLE, ERROR
        private String expression;
        private long threshold;
        private String variableName;
        private String variableValue;
        
        public AlertCondition(String id, String type) {
            this.id = id;
            this.type = type;
        }
        
        // Getters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getExpression() { return expression; }
        public long getThreshold() { return threshold; }
        public String getVariableName() { return variableName; }
        public String getVariableValue() { return variableValue; }
        
        // Setters
        public void setExpression(String expression) { this.expression = expression; }
        public void setThreshold(long threshold) { this.threshold = threshold; }
        public void setVariableName(String variableName) { this.variableName = variableName; }
        public void setVariableValue(String variableValue) { this.variableValue = variableValue; }
    }
    
    /**
     * Classe interna para representar uma ação de alerta.
     */
    class AlertAction {
        private String id;
        private String type; // NOTIFY, ESCALATE, LOG, CALLBACK
        private String recipient;
        private String message;
        private String callbackUrl;
        
        public AlertAction(String id, String type) {
            this.id = id;
            this.type = type;
        }
        
        // Getters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getRecipient() { return recipient; }
        public String getMessage() { return message; }
        public String getCallbackUrl() { return callbackUrl; }
        
        // Setters
        public void setRecipient(String recipient) { this.recipient = recipient; }
        public void setMessage(String message) { this.message = message; }
        public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    }
    
    /**
     * Classe interna para representar informações de um alerta.
     */
    class AlertInfo {
        private String id;
        private String instanceId;
        private AlertCondition condition;
        private AlertAction action;
        private boolean triggered;
        private long createdTime;
        private Long triggeredTime;
        
        public AlertInfo(String id, String instanceId, AlertCondition condition, 
                        AlertAction action, long createdTime) {
            this.id = id;
            this.instanceId = instanceId;
            this.condition = condition;
            this.action = action;
            this.createdTime = createdTime;
            this.triggered = false;
        }
        
        // Getters
        public String getId() { return id; }
        public String getInstanceId() { return instanceId; }
        public AlertCondition getCondition() { return condition; }
        public AlertAction getAction() { return action; }
        public boolean isTriggered() { return triggered; }
        public long getCreatedTime() { return createdTime; }
        public Long getTriggeredTime() { return triggeredTime; }
        
        // Setters
        public void setTriggered(boolean triggered) { this.triggered = triggered; }
        public void setTriggeredTime(Long triggeredTime) { this.triggeredTime = triggeredTime; }
    }
    
    /**
     * Classe interna para representar uma definição de SLA.
     */
    class SlaDefinition {
        private String id;
        private String name;
        private long duration; // em milissegundos
        private String description;
        private List<AlertAction> violationActions;
        
        public SlaDefinition(String id, String name, long duration) {
            this.id = id;
            this.name = name;
            this.duration = duration;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public long getDuration() { return duration; }
        public String getDescription() { return description; }
        public List<AlertAction> getViolationActions() { return violationActions; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setViolationActions(List<AlertAction> violationActions) { this.violationActions = violationActions; }
    }
    
    /**
     * Classe interna para representar o status de um SLA.
     */
    class SlaStatus {
        private String instanceId;
        private String slaDefinitionId;
        private long startTime;
        private long dueTime;
        private Long completionTime;
        private boolean violated;
        private long remainingTime; // em milissegundos, negativo se violado
        
        public SlaStatus(String instanceId, String slaDefinitionId, long startTime, long dueTime) {
            this.instanceId = instanceId;
            this.slaDefinitionId = slaDefinitionId;
            this.startTime = startTime;
            this.dueTime = dueTime;
            this.violated = false;
            this.remainingTime = dueTime - System.currentTimeMillis();
        }
        
        // Getters
        public String getInstanceId() { return instanceId; }
        public String getSlaDefinitionId() { return slaDefinitionId; }
        public long getStartTime() { return startTime; }
        public long getDueTime() { return dueTime; }
        public Long getCompletionTime() { return completionTime; }
        public boolean isViolated() { return violated; }
        public long getRemainingTime() { return remainingTime; }
        
        // Setters
        public void setCompletionTime(Long completionTime) { this.completionTime = completionTime; }
        public void setViolated(boolean violated) { this.violated = violated; }
        public void setRemainingTime(long remainingTime) { this.remainingTime = remainingTime; }
    }
    
    /**
     * Classe interna para representar um marco de progresso.
     */
    class Milestone {
        private String id;
        private String name;
        private String activityId;
        private int order;
        private String description;
        
        public Milestone(String id, String name, String activityId, int order) {
            this.id = id;
            this.name = name;
            this.activityId = activityId;
            this.order = order;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getActivityId() { return activityId; }
        public int getOrder() { return order; }
        public String getDescription() { return description; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * Classe interna para representar informações de progresso.
     */
    class ProgressInfo {
        private String instanceId;
        private List<Milestone> milestones;
        private List<String> completedMilestoneIds;
        private String currentMilestoneId;
        private int completedPercentage;
        
        public ProgressInfo(String instanceId, List<Milestone> milestones) {
            this.instanceId = instanceId;
            this.milestones = milestones;
        }
        
        // Getters
        public String getInstanceId() { return instanceId; }
        public List<Milestone> getMilestones() { return milestones; }
        public List<String> getCompletedMilestoneIds() { return completedMilestoneIds; }
        public String getCurrentMilestoneId() { return currentMilestoneId; }
        public int getCompletedPercentage() { return completedPercentage; }
        
        // Setters
        public void setCompletedMilestoneIds(List<String> completedMilestoneIds) { this.completedMilestoneIds = completedMilestoneIds; }
        public void setCurrentMilestoneId(String currentMilestoneId) { this.currentMilestoneId = currentMilestoneId; }
        public void setCompletedPercentage(int completedPercentage) { this.completedPercentage = completedPercentage; }
    }
    
    /**
     * Classe interna para representar um evento personalizado.
     */
    class CustomEvent {
        private String id;
        private String instanceId;
        private String eventType;
        private long timestamp;
        private Map<String, Object> eventData;
        private String userId;
        
        public CustomEvent(String id, String instanceId, String eventType, 
                          long timestamp, Map<String, Object> eventData) {
            this.id = id;
            this.instanceId = instanceId;
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.eventData = eventData;
        }
        
        // Getters
        public String getId() { return id; }
        public String getInstanceId() { return instanceId; }
        public String getEventType() { return eventType; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Object> getEventData() { return eventData; }
        public String getUserId() { return userId; }
        
        // Setters
        public void setUserId(String userId) { this.userId = userId; }
    }
}