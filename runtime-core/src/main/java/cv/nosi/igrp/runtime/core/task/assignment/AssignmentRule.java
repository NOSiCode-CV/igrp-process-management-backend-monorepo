package cv.nosi.igrp.runtime.core.task.assignment;

/**
 * Classe para representar uma regra de atribuição de tarefas.
 */
public class AssignmentRule {
    private String id;
    private String taskDefinitionKey;
    private String condition;
    private String assigneeId;
    private String assigneeExpression;
    private int priority;
    private boolean active;
    
    /**
     * Construtor.
     * 
     * @param id ID da regra
     * @param taskDefinitionKey chave de definição da tarefa
     * @param condition condição para aplicação da regra
     * @param assigneeId ID do responsável pela tarefa
     * @param priority prioridade da regra
     */
    public AssignmentRule(String id, String taskDefinitionKey, String condition, 
                         String assigneeId, int priority) {
        this.id = id;
        this.taskDefinitionKey = taskDefinitionKey;
        this.condition = condition;
        this.assigneeId = assigneeId;
        this.priority = priority;
        this.active = true;
    }
    
    /**
     * Obtém o ID da regra.
     * 
     * @return ID da regra
     */
    public String getId() { return id; }
    
    /**
     * Obtém a chave de definição da tarefa.
     * 
     * @return chave de definição da tarefa
     */
    public String getTaskDefinitionKey() { return taskDefinitionKey; }
    
    /**
     * Obtém a condição para aplicação da regra.
     * 
     * @return condição para aplicação da regra
     */
    public String getCondition() { return condition; }
    
    /**
     * Obtém o ID do responsável pela tarefa.
     * 
     * @return ID do responsável pela tarefa
     */
    public String getAssigneeId() { return assigneeId; }
    
    /**
     * Obtém a expressão para determinar o responsável pela tarefa.
     * 
     * @return expressão para determinar o responsável pela tarefa
     */
    public String getAssigneeExpression() { return assigneeExpression; }
    
    /**
     * Obtém a prioridade da regra.
     * 
     * @return prioridade da regra
     */
    public int getPriority() { return priority; }
    
    /**
     * Verifica se a regra está ativa.
     * 
     * @return true se a regra estiver ativa
     */
    public boolean isActive() { return active; }
    
    /**
     * Define a condição para aplicação da regra.
     * 
     * @param condition condição para aplicação da regra
     */
    public void setCondition(String condition) { this.condition = condition; }
    
    /**
     * Define o ID do responsável pela tarefa.
     * 
     * @param assigneeId ID do responsável pela tarefa
     */
    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
    
    /**
     * Define a expressão para determinar o responsável pela tarefa.
     * 
     * @param assigneeExpression expressão para determinar o responsável pela tarefa
     */
    public void setAssigneeExpression(String assigneeExpression) { this.assigneeExpression = assigneeExpression; }
    
    /**
     * Define a prioridade da regra.
     * 
     * @param priority prioridade da regra
     */
    public void setPriority(int priority) { this.priority = priority; }
    
    /**
     * Define se a regra está ativa.
     * 
     * @param active true se a regra estiver ativa
     */
    public void setActive(boolean active) { this.active = active; }
}