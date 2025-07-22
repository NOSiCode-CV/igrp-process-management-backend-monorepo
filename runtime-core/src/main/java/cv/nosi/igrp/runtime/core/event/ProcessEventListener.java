package cv.nosi.igrp.runtime.core.event;

import java.util.Map;

/**
 * Interface para listeners de eventos de processos.
 * <p>
 * Define o contrato para componentes que desejam receber e processar
 * eventos relacionados a processos.
 */
public interface ProcessEventListener {
    
    /**
     * Método chamado quando um evento é publicado.
     * 
     * @param event evento publicado
     */
    void onEvent(ProcessEvent event);
    
    /**
     * Verifica se este listener está interessado em um determinado tipo de evento.
     * 
     * @param eventType tipo do evento
     * @return true se o listener estiver interessado no evento
     */
    default boolean isInterestedIn(EventTypes eventType) {
        return true;
    }
    
    /**
     * Verifica se este listener deve processar um evento com base em seu conteúdo.
     * 
     * @param event evento a ser verificado
     * @return true se o evento deve ser processado por este listener
     */
    default boolean shouldProcessEvent(ProcessEvent event) {
        return isInterestedIn(event.getEventType());
    }
    
    /**
     * Classe interna para representar um evento de processo.
     */
    class ProcessEvent {
        private String id;
        private EventTypes eventType;
        private String processInstanceId;
        private String executionId;
        private String taskId;
        private String activityId;
        private String activityName;
        private String activityType;
        private String variableName;
        private Object variableValue;
        private String errorMessage;
        private Throwable exception;
        private Map<String, Object> payload;
        private long timestamp;
        private String userId;
        private String processDefinitionId;
        private String processDefinitionKey;
        private String businessKey;
        
        public ProcessEvent(String id, EventTypes eventType, String processInstanceId, long timestamp) {
            this.id = id;
            this.eventType = eventType;
            this.processInstanceId = processInstanceId;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getId() { return id; }
        public EventTypes getEventType() { return eventType; }
        public String getProcessInstanceId() { return processInstanceId; }
        public String getExecutionId() { return executionId; }
        public String getTaskId() { return taskId; }
        public String getActivityId() { return activityId; }
        public String getActivityName() { return activityName; }
        public String getActivityType() { return activityType; }
        public String getVariableName() { return variableName; }
        public Object getVariableValue() { return variableValue; }
        public String getErrorMessage() { return errorMessage; }
        public Throwable getException() { return exception; }
        public Map<String, Object> getPayload() { return payload; }
        public long getTimestamp() { return timestamp; }
        public String getUserId() { return userId; }
        public String getProcessDefinitionId() { return processDefinitionId; }
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public String getBusinessKey() { return businessKey; }
        
        // Setters
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public void setActivityId(String activityId) { this.activityId = activityId; }
        public void setActivityName(String activityName) { this.activityName = activityName; }
        public void setActivityType(String activityType) { this.activityType = activityType; }
        public void setVariableName(String variableName) { this.variableName = variableName; }
        public void setVariableValue(Object variableValue) { this.variableValue = variableValue; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public void setException(Throwable exception) { this.exception = exception; }
        public void setPayload(Map<String, Object> payload) { this.payload = payload; }
        public void setUserId(String userId) { this.userId = userId; }
        public void setProcessDefinitionId(String processDefinitionId) { this.processDefinitionId = processDefinitionId; }
        public void setProcessDefinitionKey(String processDefinitionKey) { this.processDefinitionKey = processDefinitionKey; }
        public void setBusinessKey(String businessKey) { this.businessKey = businessKey; }
        
        /**
         * Obtém um valor do payload do evento.
         * 
         * @param <T> tipo do valor
         * @param key chave do valor no payload
         * @return valor do payload, ou null se não existir
         */
        @SuppressWarnings("unchecked")
        public <T> T getPayloadValue(String key) {
            if (payload == null) {
                return null;
            }
            return (T) payload.get(key);
        }
        
        /**
         * Verifica se este evento é de um determinado tipo.
         * 
         * @param type tipo de evento a verificar
         * @return true se o evento for do tipo especificado
         */
        public boolean isOfType(EventTypes type) {
            return this.eventType == type;
        }
        
        /**
         * Verifica se este evento está relacionado a uma determinada instância de processo.
         * 
         * @param processInstanceId ID da instância de processo
         * @return true se o evento estiver relacionado à instância especificada
         */
        public boolean isForProcessInstance(String processInstanceId) {
            return this.processInstanceId != null && this.processInstanceId.equals(processInstanceId);
        }
        
        /**
         * Verifica se este evento está relacionado a uma determinada tarefa.
         * 
         * @param taskId ID da tarefa
         * @return true se o evento estiver relacionado à tarefa especificada
         */
        public boolean isForTask(String taskId) {
            return this.taskId != null && this.taskId.equals(taskId);
        }
        
        /**
         * Verifica se este evento está relacionado a uma determinada atividade.
         * 
         * @param activityId ID da atividade
         * @return true se o evento estiver relacionado à atividade especificada
         */
        public boolean isForActivity(String activityId) {
            return this.activityId != null && this.activityId.equals(activityId);
        }
        
        /**
         * Verifica se este evento está relacionado a uma determinada variável.
         * 
         * @param variableName nome da variável
         * @return true se o evento estiver relacionado à variável especificada
         */
        public boolean isForVariable(String variableName) {
            return this.variableName != null && this.variableName.equals(variableName);
        }
        
        /**
         * Verifica se este evento é um evento de erro.
         * 
         * @return true se for um evento de erro
         */
        public boolean isErrorEvent() {
            return eventType != null && eventType.isErrorEvent();
        }
        
        /**
         * Verifica se este evento é um evento de tarefa.
         * 
         * @return true se for um evento de tarefa
         */
        public boolean isTaskEvent() {
            return eventType != null && eventType.isTaskEvent();
        }
        
        /**
         * Verifica se este evento é um evento de processo.
         * 
         * @return true se for um evento de processo
         */
        public boolean isProcessEvent() {
            return eventType != null && eventType.isProcessEvent();
        }
        
        /**
         * Verifica se este evento é um evento de atividade.
         * 
         * @return true se for um evento de atividade
         */
        public boolean isActivityEvent() {
            return eventType != null && eventType.isActivityEvent();
        }
        
        /**
         * Verifica se este evento é um evento de variável.
         * 
         * @return true se for um evento de variável
         */
        public boolean isVariableEvent() {
            return eventType != null && eventType.isVariableEvent();
        }
    }
    
    /**
     * Implementação base para listeners de eventos de processos.
     */
    abstract class BaseProcessEventListener implements ProcessEventListener {
        private EventTypes[] interestedEventTypes;
        
        /**
         * Construtor.
         * 
         * @param interestedEventTypes tipos de eventos que este listener está interessado
         */
        public BaseProcessEventListener(EventTypes... interestedEventTypes) {
            this.interestedEventTypes = interestedEventTypes;
        }
        
        @Override
        public boolean isInterestedIn(EventTypes eventType) {
            if (interestedEventTypes == null || interestedEventTypes.length == 0) {
                return true;
            }
            
            for (EventTypes type : interestedEventTypes) {
                if (type == eventType) {
                    return true;
                }
            }
            
            return false;
        }
    }
}