package cv.nosi.igrp.runtime.core.task;

import java.util.List;
import java.util.Map;

/**
 * Interface para coordenação de tarefas em processos.
 * <p>
 * Define operações para gerenciar o fluxo e a coordenação de tarefas,
 * incluindo delegação, escalação e sincronização de tarefas relacionadas.
 */
public interface TaskCoordination {
    
    /**
     * Delega uma tarefa para outro usuário, mantendo o proprietário original.
     * 
     * @param taskId ID da tarefa
     * @param ownerUserId ID do usuário proprietário
     * @param delegateUserId ID do usuário delegado
     * @param reason motivo da delegação
     * @return true se a delegação for bem-sucedida
     */
    boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason);
    
    /**
     * Resolve uma tarefa delegada, retornando-a ao proprietário original.
     * 
     * @param taskId ID da tarefa
     * @param delegateUserId ID do usuário delegado
     * @param comment comentário opcional sobre a resolução
     * @return true se a resolução for bem-sucedida
     */
    boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment);
    
    /**
     * Escala uma tarefa para um nível superior de gestão.
     * 
     * @param taskId ID da tarefa
     * @param currentUserId ID do usuário atual
     * @param escalationLevel nível de escalação
     * @param reason motivo da escalação
     * @return ID da nova tarefa de escalação criada
     */
    String escalateTask(String taskId, String currentUserId, int escalationLevel, String reason);
    
    /**
     * Cria uma subtarefa associada a uma tarefa principal.
     * 
     * @param parentTaskId ID da tarefa principal
     * @param taskName nome da subtarefa
     * @param assigneeId ID do responsável pela subtarefa
     * @param variables variáveis da subtarefa
     * @return ID da subtarefa criada
     */
    String createSubtask(String parentTaskId, String taskName, String assigneeId, Map<String, Object> variables);
    
    /**
     * Obtém subtarefas de uma tarefa principal.
     * 
     * @param parentTaskId ID da tarefa principal
     * @return lista de IDs das subtarefas
     */
    List<String> getSubtasks(String parentTaskId);
    
    /**
     * Verifica se todas as subtarefas de uma tarefa principal estão completas.
     * 
     * @param parentTaskId ID da tarefa principal
     * @return true se todas as subtarefas estiverem completas
     */
    boolean areAllSubtasksComplete(String parentTaskId);
    
    /**
     * Sincroniza tarefas relacionadas, garantindo que estejam em estados consistentes.
     * 
     * @param taskIds lista de IDs de tarefas a serem sincronizadas
     * @param syncAction ação de sincronização a ser aplicada
     * @return número de tarefas sincronizadas com sucesso
     */
    int synchronizeTasks(List<String> taskIds, SyncAction syncAction);
    
    /**
     * Configura dependências entre tarefas.
     * 
     * @param taskId ID da tarefa
     * @param dependsOnTaskIds lista de IDs de tarefas das quais esta tarefa depende
     * @return true se a configuração for bem-sucedida
     */
    boolean setTaskDependencies(String taskId, List<String> dependsOnTaskIds);
    
    /**
     * Verifica se todas as dependências de uma tarefa estão satisfeitas.
     * 
     * @param taskId ID da tarefa
     * @return true se todas as dependências estiverem satisfeitas
     */
    boolean areTaskDependenciesSatisfied(String taskId);
    
    /**
     * Obtém tarefas que dependem de uma tarefa específica.
     * 
     * @param taskId ID da tarefa
     * @return lista de IDs de tarefas dependentes
     */
    List<String> getDependentTasks(String taskId);
    
    /**
     * Configura um prazo para uma tarefa e ações a serem tomadas quando o prazo expirar.
     * 
     * @param taskId ID da tarefa
     * @param dueDate data de vencimento (timestamp)
     * @param escalationActions ações de escalação a serem tomadas quando o prazo expirar
     * @return true se a configuração for bem-sucedida
     */
    boolean setTaskDueDate(String taskId, long dueDate, List<EscalationAction> escalationActions);
    
    /**
     * Obtém tarefas com prazos expirados.
     * 
     * @param processInstanceId ID da instância de processo (opcional)
     * @return lista de IDs de tarefas com prazos expirados
     */
    List<String> getOverdueTasks(String processInstanceId);
    
    /**
     * Executa ações de escalação para tarefas com prazos expirados.
     * 
     * @param taskIds lista de IDs de tarefas
     * @return número de tarefas escaladas com sucesso
     */
    int executeEscalationActions(List<String> taskIds);
    
    /**
     * Enumeration para ações de sincronização de tarefas.
     */
    enum SyncAction {
        SUSPEND,
        RESUME,
        COMPLETE,
        CANCEL,
        UPDATE_VARIABLES
    }
    
    /**
     * Classe interna para representar uma ação de escalação.
     */
    class EscalationAction {
        private String id;
        private String type; // NOTIFY, REASSIGN, ESCALATE, PRIORITIZE
        private String targetUserId;
        private String targetGroupId;
        private String message;
        private long delayAfterDueDate; // em milissegundos
        private boolean executed;
        
        public EscalationAction(String id, String type) {
            this.id = id;
            this.type = type;
            this.executed = false;
        }
        
        // Getters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getTargetUserId() { return targetUserId; }
        public String getTargetGroupId() { return targetGroupId; }
        public String getMessage() { return message; }
        public long getDelayAfterDueDate() { return delayAfterDueDate; }
        public boolean isExecuted() { return executed; }
        
        // Setters
        public void setTargetUserId(String targetUserId) { this.targetUserId = targetUserId; }
        public void setTargetGroupId(String targetGroupId) { this.targetGroupId = targetGroupId; }
        public void setMessage(String message) { this.message = message; }
        public void setDelayAfterDueDate(long delayAfterDueDate) { this.delayAfterDueDate = delayAfterDueDate; }
        public void setExecuted(boolean executed) { this.executed = executed; }
    }
}