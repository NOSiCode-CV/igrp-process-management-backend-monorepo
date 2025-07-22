package cv.nosi.igrp.runtime.core.task.assignment;

/**
 * Classe para representar um evento de atribuição de tarefa.
 */
public class AssignmentEvent {
    private String taskId;
    private String userId;
    private String previousAssignee;
    private String type; // ASSIGN, REASSIGN, CLAIM, RELEASE
    private String reason;
    private long timestamp;
    
    /**
     * Construtor.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param previousAssignee responsável anterior pela tarefa
     * @param type tipo de evento (ASSIGN, REASSIGN, CLAIM, RELEASE)
     * @param reason motivo da atribuição
     * @param timestamp tempo do evento
     */
    public AssignmentEvent(String taskId, String userId, String previousAssignee, 
                          String type, String reason, long timestamp) {
        this.taskId = taskId;
        this.userId = userId;
        this.previousAssignee = previousAssignee;
        this.type = type;
        this.reason = reason;
        this.timestamp = timestamp;
    }
    
    /**
     * Obtém o ID da tarefa.
     * 
     * @return ID da tarefa
     */
    public String getTaskId() { return taskId; }
    
    /**
     * Obtém o ID do usuário.
     * 
     * @return ID do usuário
     */
    public String getUserId() { return userId; }
    
    /**
     * Obtém o responsável anterior pela tarefa.
     * 
     * @return responsável anterior pela tarefa
     */
    public String getPreviousAssignee() { return previousAssignee; }
    
    /**
     * Obtém o tipo de evento.
     * 
     * @return tipo de evento
     */
    public String getType() { return type; }
    
    /**
     * Obtém o motivo da atribuição.
     * 
     * @return motivo da atribuição
     */
    public String getReason() { return reason; }
    
    /**
     * Obtém o tempo do evento.
     * 
     * @return tempo do evento
     */
    public long getTimestamp() { return timestamp; }
}