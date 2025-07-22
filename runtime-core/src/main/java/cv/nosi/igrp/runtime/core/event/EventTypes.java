package cv.nosi.igrp.runtime.core.event;

/**
 * Enumeração de tipos de eventos de processos.
 * <p>
 * Define os tipos de eventos que podem ser publicados e consumidos
 * pelo sistema de eventos de processos.
 */
public enum EventTypes {
    
    // Eventos de ciclo de vida de processo
    PROCESS_STARTED,
    PROCESS_COMPLETED,
    PROCESS_CANCELLED,
    PROCESS_SUSPENDED,
    PROCESS_RESUMED,
    PROCESS_TERMINATED,
    
    // Eventos de atividade
    ACTIVITY_STARTED,
    ACTIVITY_COMPLETED,
    ACTIVITY_CANCELLED,
    ACTIVITY_SUSPENDED,
    ACTIVITY_RESUMED,
    
    // Eventos de tarefa
    TASK_CREATED,
    TASK_ASSIGNED,
    TASK_COMPLETED,
    TASK_CANCELLED,
    TASK_SUSPENDED,
    TASK_RESUMED,
    TASK_DELEGATED,
    TASK_CLAIMED,
    TASK_UNCLAIMED,
    TASK_DUE_DATE_CHANGED,
    
    // Eventos de variável
    VARIABLE_CREATED,
    VARIABLE_UPDATED,
    VARIABLE_DELETED,
    
    // Eventos de erro
    ERROR_OCCURRED,
    JOB_EXECUTION_FAILED,
    
    // Eventos de transição
    SEQUENCE_FLOW_TAKEN,
    BOUNDARY_EVENT_TRIGGERED,
    
    // Eventos de timer
    TIMER_SCHEDULED,
    TIMER_FIRED,
    TIMER_CANCELLED,
    
    // Eventos de sinal
    SIGNAL_RECEIVED,
    SIGNAL_SENT,
    
    // Eventos de mensagem
    MESSAGE_RECEIVED,
    MESSAGE_SENT,
    
    // Eventos de compensação
    COMPENSATION_TRIGGERED,
    
    // Eventos de SLA
    SLA_VIOLATED,
    SLA_MET,
    
    // Eventos de deployment
    DEPLOYMENT_CREATED,
    DEPLOYMENT_DELETED,
    PROCESS_DEFINITION_DEPLOYED,
    
    // Eventos de histórico
    HISTORIC_ACTIVITY_INSTANCE_CREATED,
    HISTORIC_ACTIVITY_INSTANCE_ENDED,
    HISTORIC_PROCESS_INSTANCE_CREATED,
    HISTORIC_PROCESS_INSTANCE_ENDED,
    HISTORIC_TASK_INSTANCE_CREATED,
    HISTORIC_TASK_INSTANCE_ENDED,
    
    // Eventos de sistema
    ENGINE_CREATED,
    ENGINE_CLOSED,
    
    // Evento genérico para eventos personalizados
    CUSTOM_EVENT;
    
    /**
     * Verifica se o tipo de evento está relacionado a processos.
     * 
     * @return true se for um evento de processo
     */
    public boolean isProcessEvent() {
        return name().startsWith("PROCESS_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a atividades.
     * 
     * @return true se for um evento de atividade
     */
    public boolean isActivityEvent() {
        return name().startsWith("ACTIVITY_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a tarefas.
     * 
     * @return true se for um evento de tarefa
     */
    public boolean isTaskEvent() {
        return name().startsWith("TASK_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a variáveis.
     * 
     * @return true se for um evento de variável
     */
    public boolean isVariableEvent() {
        return name().startsWith("VARIABLE_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a erros.
     * 
     * @return true se for um evento de erro
     */
    public boolean isErrorEvent() {
        return name().startsWith("ERROR_") || name().equals("JOB_EXECUTION_FAILED");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a timers.
     * 
     * @return true se for um evento de timer
     */
    public boolean isTimerEvent() {
        return name().startsWith("TIMER_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a SLAs.
     * 
     * @return true se for um evento de SLA
     */
    public boolean isSlaEvent() {
        return name().startsWith("SLA_");
    }
    
    /**
     * Verifica se o tipo de evento está relacionado a deployments.
     * 
     * @return true se for um evento de deployment
     */
    public boolean isDeploymentEvent() {
        return name().startsWith("DEPLOYMENT_") || name().equals("PROCESS_DEFINITION_DEPLOYED");
    }
    
    /**
     * Verifica se o tipo de evento é um evento personalizado.
     * 
     * @return true se for um evento personalizado
     */
    public boolean isCustomEvent() {
        return this == CUSTOM_EVENT;
    }
}