package cv.nosi.igrp.runtime.core.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface para gerenciamento de tarefas de processos.
 * <p>
 * Define operações para criar, atualizar, consultar e completar tarefas
 * associadas a instâncias de processos.
 */
public interface TaskManager {
    
    /**
     * Cria uma nova tarefa.
     * 
     * @param processInstanceId ID da instância de processo associada (opcional)
     * @param taskDefinitionKey chave de definição da tarefa
     * @param taskName nome da tarefa
     * @param assignee responsável pela tarefa (opcional)
     * @param variables variáveis da tarefa
     * @return ID da tarefa criada
     */
    String createTask(String processInstanceId, String taskDefinitionKey, 
                     String taskName, String assignee, Map<String, Object> variables);
    
    /**
     * Obtém uma tarefa pelo seu ID.
     * 
     * @param taskId ID da tarefa
     * @return a tarefa encontrada ou Optional vazio se não existir
     */
    Optional<TaskInfo> getTask(String taskId);
    
    /**
     * Lista tarefas com base em critérios de filtragem.
     * 
     * @param filter critérios de filtragem
     * @return lista de tarefas que correspondem aos critérios
     */
    List<TaskInfo> listTasks(TaskFilter filter);
    
    /**
     * Atribui uma tarefa a um usuário.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     */
    void assignTask(String taskId, String userId);
    
    /**
     * Completa uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param variables variáveis a serem atualizadas ao completar a tarefa
     * @param userId ID do usuário que está completando a tarefa
     */
    void completeTask(String taskId, Map<String, Object> variables, String userId);
    
    /**
     * Atualiza variáveis de uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param variables variáveis a serem atualizadas
     */
    void setTaskVariables(String taskId, Map<String, Object> variables);
    
    /**
     * Obtém variáveis de uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return mapa com as variáveis da tarefa
     */
    Map<String, Object> getTaskVariables(String taskId);
    
    /**
     * Adiciona um comentário a uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário que está adicionando o comentário
     * @param comment texto do comentário
     * @return ID do comentário criado
     */
    String addTaskComment(String taskId, String userId, String comment);
    
    /**
     * Obtém comentários de uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de comentários da tarefa
     */
    List<TaskComment> getTaskComments(String taskId);
    
    /**
     * Classe interna para representar informações de uma tarefa.
     */
    class TaskInfo {
        private String id;
        private String name;
        private String description;
        private String processInstanceId;
        private String taskDefinitionKey;
        private String assignee;
        private String owner;
        private long createdTime;
        private Long dueDate;
        private String priority;
        private String formKey;
        
        // Construtor, getters e setters
        public TaskInfo(String id, String name, String processInstanceId, String taskDefinitionKey,
                       String assignee, long createdTime) {
            this.id = id;
            this.name = name;
            this.processInstanceId = processInstanceId;
            this.taskDefinitionKey = taskDefinitionKey;
            this.assignee = assignee;
            this.createdTime = createdTime;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getProcessInstanceId() { return processInstanceId; }
        public String getTaskDefinitionKey() { return taskDefinitionKey; }
        public String getAssignee() { return assignee; }
        public String getOwner() { return owner; }
        public long getCreatedTime() { return createdTime; }
        public Long getDueDate() { return dueDate; }
        public String getPriority() { return priority; }
        public String getFormKey() { return formKey; }
        
        // Setters para campos opcionais
        public void setDescription(String description) { this.description = description; }
        public void setOwner(String owner) { this.owner = owner; }
        public void setDueDate(Long dueDate) { this.dueDate = dueDate; }
        public void setPriority(String priority) { this.priority = priority; }
        public void setFormKey(String formKey) { this.formKey = formKey; }
    }
    
    /**
     * Classe interna para representar um comentário de tarefa.
     */
    class TaskComment {
        private String id;
        private String taskId;
        private String userId;
        private String content;
        private long createdTime;
        
        public TaskComment(String id, String taskId, String userId, String content, long createdTime) {
            this.id = id;
            this.taskId = taskId;
            this.userId = userId;
            this.content = content;
            this.createdTime = createdTime;
        }
        
        public String getId() { return id; }
        public String getTaskId() { return taskId; }
        public String getUserId() { return userId; }
        public String getContent() { return content; }
        public long getCreatedTime() { return createdTime; }
    }
    
    /**
     * Classe interna para representar critérios de filtragem de tarefas.
     */
    class TaskFilter {
        private String assignee;
        private String processInstanceId;
        private String taskName;
        private String taskDefinitionKey;
        private Boolean unassigned;
        private Long createdAfter;
        private Long createdBefore;
        private Long dueDateAfter;
        private Long dueDateBefore;
        
        public TaskFilter() {
        }
        
        // Builder methods
        public TaskFilter assignee(String assignee) {
            this.assignee = assignee;
            return this;
        }
        
        public TaskFilter processInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }
        
        public TaskFilter taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }
        
        public TaskFilter taskDefinitionKey(String taskDefinitionKey) {
            this.taskDefinitionKey = taskDefinitionKey;
            return this;
        }
        
        public TaskFilter unassigned(Boolean unassigned) {
            this.unassigned = unassigned;
            return this;
        }
        
        public TaskFilter createdAfter(Long createdAfter) {
            this.createdAfter = createdAfter;
            return this;
        }
        
        public TaskFilter createdBefore(Long createdBefore) {
            this.createdBefore = createdBefore;
            return this;
        }
        
        public TaskFilter dueDateAfter(Long dueDateAfter) {
            this.dueDateAfter = dueDateAfter;
            return this;
        }
        
        public TaskFilter dueDateBefore(Long dueDateBefore) {
            this.dueDateBefore = dueDateBefore;
            return this;
        }
        
        // Getters
        public String getAssignee() { return assignee; }
        public String getProcessInstanceId() { return processInstanceId; }
        public String getTaskName() { return taskName; }
        public String getTaskDefinitionKey() { return taskDefinitionKey; }
        public Boolean getUnassigned() { return unassigned; }
        public Long getCreatedAfter() { return createdAfter; }
        public Long getCreatedBefore() { return createdBefore; }
        public Long getDueDateAfter() { return dueDateAfter; }
        public Long getDueDateBefore() { return dueDateBefore; }
    }
}