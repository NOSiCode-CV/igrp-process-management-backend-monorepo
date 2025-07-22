package cv.nosi.igrp.runtime.core.task;

import java.util.List;
import java.util.Map;

/**
 * Interface para gerenciamento de atribuições de tarefas.
 * <p>
 * Define operações para atribuir, reatribuir e gerenciar candidatos para tarefas,
 * incluindo suporte para atribuição individual, em grupo e baseada em regras.
 */
public interface TaskAssignment {
    
    /**
     * Atribui uma tarefa a um usuário específico.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param reason motivo da atribuição (opcional)
     * @return true se a atribuição for bem-sucedida
     */
    boolean assignTask(String taskId, String userId, String reason);
    
    /**
     * Reatribui uma tarefa de um usuário para outro.
     * 
     * @param taskId ID da tarefa
     * @param fromUserId ID do usuário atual
     * @param toUserId ID do novo usuário
     * @param reason motivo da reatribuição
     * @return true se a reatribuição for bem-sucedida
     */
    boolean reassignTask(String taskId, String fromUserId, String toUserId, String reason);
    
    /**
     * Adiciona um usuário como candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se a adição for bem-sucedida
     */
    boolean addCandidateUser(String taskId, String userId);
    
    /**
     * Remove um usuário da lista de candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se a remoção for bem-sucedida
     */
    boolean removeCandidateUser(String taskId, String userId);
    
    /**
     * Adiciona um grupo como candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param groupId ID do grupo
     * @return true se a adição for bem-sucedida
     */
    boolean addCandidateGroup(String taskId, String groupId);
    
    /**
     * Remove um grupo da lista de candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param groupId ID do grupo
     * @return true se a remoção for bem-sucedida
     */
    boolean removeCandidateGroup(String taskId, String groupId);
    
    /**
     * Obtém a lista de usuários candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de IDs de usuários candidatos
     */
    List<String> getCandidateUsers(String taskId);
    
    /**
     * Obtém a lista de grupos candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de IDs de grupos candidatos
     */
    List<String> getCandidateGroups(String taskId);
    
    /**
     * Verifica se um usuário é candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se o usuário for candidato
     */
    boolean isUserCandidate(String taskId, String userId);
    
    /**
     * Verifica se um usuário pertence a algum grupo candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se o usuário pertencer a algum grupo candidato
     */
    boolean isUserInCandidateGroup(String taskId, String userId);
    
    /**
     * Configura regras de atribuição automática para tarefas.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     * @param rules regras de atribuição
     * @return true se a configuração for bem-sucedida
     */
    boolean setAssignmentRules(String taskDefinitionKey, List<AssignmentRule> rules);
    
    /**
     * Obtém regras de atribuição automática para uma tarefa.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     * @return lista de regras de atribuição
     */
    List<AssignmentRule> getAssignmentRules(String taskDefinitionKey);
    
    /**
     * Executa atribuição automática para uma tarefa com base nas regras configuradas.
     * 
     * @param taskId ID da tarefa
     * @param variables variáveis de contexto para avaliação das regras
     * @return ID do usuário atribuído ou null se nenhuma regra for aplicável
     */
    String autoAssign(String taskId, Map<String, Object> variables);
    
    /**
     * Obtém o histórico de atribuições de uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de eventos de atribuição
     */
    List<AssignmentEvent> getAssignmentHistory(String taskId);
    
    /**
     * Classe interna para representar uma regra de atribuição.
     */
    class AssignmentRule {
        private String id;
        private String taskDefinitionKey;
        private String condition;
        private String assigneeId;
        private String assigneeExpression;
        private int priority;
        private boolean active;
        
        public AssignmentRule(String id, String taskDefinitionKey, String condition, 
                             String assigneeId, int priority) {
            this.id = id;
            this.taskDefinitionKey = taskDefinitionKey;
            this.condition = condition;
            this.assigneeId = assigneeId;
            this.priority = priority;
            this.active = true;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTaskDefinitionKey() { return taskDefinitionKey; }
        public String getCondition() { return condition; }
        public String getAssigneeId() { return assigneeId; }
        public String getAssigneeExpression() { return assigneeExpression; }
        public int getPriority() { return priority; }
        public boolean isActive() { return active; }
        
        // Setters
        public void setCondition(String condition) { this.condition = condition; }
        public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
        public void setAssigneeExpression(String assigneeExpression) { this.assigneeExpression = assigneeExpression; }
        public void setPriority(int priority) { this.priority = priority; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    /**
     * Classe interna para representar um evento de atribuição.
     */
    class AssignmentEvent {
        private String taskId;
        private String userId;
        private String previousAssignee;
        private String type; // ASSIGN, REASSIGN, CLAIM, RELEASE
        private String reason;
        private long timestamp;
        
        public AssignmentEvent(String taskId, String userId, String previousAssignee, 
                              String type, String reason, long timestamp) {
            this.taskId = taskId;
            this.userId = userId;
            this.previousAssignee = previousAssignee;
            this.type = type;
            this.reason = reason;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getTaskId() { return taskId; }
        public String getUserId() { return userId; }
        public String getPreviousAssignee() { return previousAssignee; }
        public String getType() { return type; }
        public String getReason() { return reason; }
        public long getTimestamp() { return timestamp; }
    }
}