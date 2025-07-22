package cv.nosi.igrp.runtime.core.event;

import java.util.Map;

/**
 * Interface para publicação de eventos de processos.
 * <p>
 * Define operações para publicar eventos relacionados a processos,
 * permitindo que outros componentes sejam notificados sobre mudanças
 * no estado dos processos.
 */
public interface ProcessEventPublisher {
    
    /**
     * Publica um evento de processo.
     * 
     * @param eventType tipo do evento
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishEvent(EventTypes eventType, String processInstanceId, Map<String, Object> payload);
    
    /**
     * Publica um evento de processo com contexto de execução.
     * 
     * @param eventType tipo do evento
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param executionId ID da execução relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishEvent(EventTypes eventType, String processInstanceId, String executionId, Map<String, Object> payload);
    
    /**
     * Publica um evento de tarefa.
     * 
     * @param eventType tipo do evento
     * @param taskId ID da tarefa relacionada ao evento
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishTaskEvent(EventTypes eventType, String taskId, String processInstanceId, Map<String, Object> payload);
    
    /**
     * Publica um evento de variável.
     * 
     * @param eventType tipo do evento
     * @param variableName nome da variável
     * @param variableValue valor da variável
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishVariableEvent(EventTypes eventType, String variableName, Object variableValue, 
                               String processInstanceId, Map<String, Object> payload);
    
    /**
     * Publica um evento de atividade.
     * 
     * @param eventType tipo do evento
     * @param activityId ID da atividade
     * @param activityName nome da atividade
     * @param activityType tipo da atividade
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishActivityEvent(EventTypes eventType, String activityId, String activityName, 
                               String activityType, String processInstanceId, Map<String, Object> payload);
    
    /**
     * Publica um evento de erro.
     * 
     * @param errorMessage mensagem de erro
     * @param exception exceção que causou o erro
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param activityId ID da atividade onde ocorreu o erro (opcional)
     * @return ID do evento publicado
     */
    String publishErrorEvent(String errorMessage, Throwable exception, 
                            String processInstanceId, String activityId);
    
    /**
     * Publica um evento personalizado.
     * 
     * @param eventName nome do evento personalizado
     * @param processInstanceId ID da instância de processo relacionada ao evento
     * @param payload dados adicionais do evento
     * @return ID do evento publicado
     */
    String publishCustomEvent(String eventName, String processInstanceId, Map<String, Object> payload);
    
    /**
     * Registra um listener para eventos.
     * 
     * @param listener listener a ser registrado
     * @param eventTypes tipos de eventos que o listener deseja receber
     * @return ID do registro do listener
     */
    String registerEventListener(ProcessEventListener listener, EventTypes... eventTypes);
    
    /**
     * Registra um listener para eventos com filtro.
     * 
     * @param listener listener a ser registrado
     * @param filter filtro para eventos
     * @param eventTypes tipos de eventos que o listener deseja receber
     * @return ID do registro do listener
     */
    String registerEventListener(ProcessEventListener listener, EventFilter filter, EventTypes... eventTypes);
    
    /**
     * Remove um listener registrado.
     * 
     * @param listenerId ID do registro do listener
     * @return true se o listener foi removido com sucesso
     */
    boolean unregisterEventListener(String listenerId);
    
    /**
     * Classe interna para representar um filtro de eventos.
     */
    class EventFilter {
        private String processDefinitionKey;
        private String processInstanceId;
        private String taskDefinitionKey;
        private String activityId;
        private String variableName;
        
        public EventFilter() {
        }
        
        // Builder methods
        public EventFilter processDefinitionKey(String processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
            return this;
        }
        
        public EventFilter processInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }
        
        public EventFilter taskDefinitionKey(String taskDefinitionKey) {
            this.taskDefinitionKey = taskDefinitionKey;
            return this;
        }
        
        public EventFilter activityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        
        public EventFilter variableName(String variableName) {
            this.variableName = variableName;
            return this;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public String getProcessInstanceId() { return processInstanceId; }
        public String getTaskDefinitionKey() { return taskDefinitionKey; }
        public String getActivityId() { return activityId; }
        public String getVariableName() { return variableName; }
    }
}