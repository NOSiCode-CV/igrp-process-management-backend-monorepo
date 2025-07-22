package cv.nosi.igrp.runtime.core.exception;

/**
 * Exceção para erros em operações de tarefas.
 * <p>
 * Esta exceção é lançada quando ocorre um erro durante operações
 * relacionadas a tarefas, como atribuição, conclusão ou delegação.
 */
public class TaskOperationException extends ProcessRuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String taskId;
    private String taskDefinitionKey;
    private String operation;
    private String userId;
    
    /**
     * Construtor padrão.
     */
    public TaskOperationException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message mensagem de erro
     */
    public TaskOperationException(String message) {
        super(message);
    }
    
    /**
     * Construtor com mensagem de erro e causa.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     */
    public TaskOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor com mensagem de erro e ID da tarefa.
     * 
     * @param message mensagem de erro
     * @param taskId ID da tarefa
     */
    public TaskOperationException(String message, String taskId) {
        super(message);
        this.taskId = taskId;
    }
    
    /**
     * Construtor com mensagem de erro, causa e ID da tarefa.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param taskId ID da tarefa
     */
    public TaskOperationException(String message, Throwable cause, String taskId) {
        super(message, cause);
        this.taskId = taskId;
    }
    
    /**
     * Construtor com mensagem de erro, ID da tarefa e operação.
     * 
     * @param message mensagem de erro
     * @param taskId ID da tarefa
     * @param operation operação que falhou
     */
    public TaskOperationException(String message, String taskId, String operation) {
        super(message);
        this.taskId = taskId;
        this.operation = operation;
    }
    
    /**
     * Construtor completo.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param processInstanceId ID da instância de processo
     * @param taskId ID da tarefa
     * @param taskDefinitionKey chave de definição da tarefa
     * @param operation operação que falhou
     * @param userId ID do usuário que tentou a operação
     */
    public TaskOperationException(String message, Throwable cause, String processInstanceId,
                                 String taskId, String taskDefinitionKey, String operation, String userId) {
        super(message, cause, processInstanceId);
        this.taskId = taskId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.operation = operation;
        this.userId = userId;
    }
    
    /**
     * Obtém o ID da tarefa.
     * 
     * @return ID da tarefa
     */
    public String getTaskId() {
        return taskId;
    }
    
    /**
     * Define o ID da tarefa.
     * 
     * @param taskId ID da tarefa
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    /**
     * Obtém a chave de definição da tarefa.
     * 
     * @return chave de definição da tarefa
     */
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }
    
    /**
     * Define a chave de definição da tarefa.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     */
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
    
    /**
     * Obtém a operação que falhou.
     * 
     * @return operação que falhou
     */
    public String getOperation() {
        return operation;
    }
    
    /**
     * Define a operação que falhou.
     * 
     * @param operation operação que falhou
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    /**
     * Obtém o ID do usuário que tentou a operação.
     * 
     * @return ID do usuário
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Define o ID do usuário que tentou a operação.
     * 
     * @param userId ID do usuário
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        
        if (taskId != null) {
            sb.append(", taskId=").append(taskId);
        }
        
        if (taskDefinitionKey != null) {
            sb.append(", taskDefinitionKey=").append(taskDefinitionKey);
        }
        
        if (operation != null) {
            sb.append(", operation=").append(operation);
        }
        
        if (userId != null) {
            sb.append(", userId=").append(userId);
        }
        
        return sb.toString();
    }
    
    /**
     * Cria uma exceção para operação de atribuição de tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param cause causa da exceção
     * @return exceção de operação de tarefa
     */
    public static TaskOperationException createAssignmentException(String taskId, String userId, Throwable cause) {
        return new TaskOperationException("Failed to assign task " + taskId + " to user " + userId, 
                                         cause, null, taskId, null, "ASSIGN", userId);
    }
    
    /**
     * Cria uma exceção para operação de conclusão de tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param cause causa da exceção
     * @return exceção de operação de tarefa
     */
    public static TaskOperationException createCompletionException(String taskId, String userId, Throwable cause) {
        return new TaskOperationException("Failed to complete task " + taskId + " by user " + userId, 
                                         cause, null, taskId, null, "COMPLETE", userId);
    }
    
    /**
     * Cria uma exceção para operação de delegação de tarefa.
     * 
     * @param taskId ID da tarefa
     * @param fromUserId ID do usuário atual
     * @param toUserId ID do novo usuário
     * @param cause causa da exceção
     * @return exceção de operação de tarefa
     */
    public static TaskOperationException createDelegationException(String taskId, String fromUserId, 
                                                                 String toUserId, Throwable cause) {
        return new TaskOperationException("Failed to delegate task " + taskId + " from user " + 
                                         fromUserId + " to user " + toUserId, 
                                         cause, null, taskId, null, "DELEGATE", fromUserId);
    }
    
    /**
     * Cria uma exceção para operação de reivindicação de tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param cause causa da exceção
     * @return exceção de operação de tarefa
     */
    public static TaskOperationException createClaimException(String taskId, String userId, Throwable cause) {
        return new TaskOperationException("Failed to claim task " + taskId + " by user " + userId, 
                                         cause, null, taskId, null, "CLAIM", userId);
    }
}