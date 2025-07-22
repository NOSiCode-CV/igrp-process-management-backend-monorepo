package cv.nosi.igrp.runtime.core.task.model;

/**
 * Classe para representar um comentário de tarefa.
 */
public class TaskComment {
    private String id;
    private String taskId;
    private String userId;
    private String content;
    private long createdTime;
    
    /**
     * Construtor.
     * 
     * @param id ID do comentário
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param content conteúdo do comentário
     * @param createdTime tempo de criação
     */
    public TaskComment(String id, String taskId, String userId, String content, long createdTime) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.content = content;
        this.createdTime = createdTime;
    }
    
    /**
     * Obtém o ID do comentário.
     * 
     * @return ID do comentário
     */
    public String getId() { 
        return id; 
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
     * Obtém o ID do usuário.
     * 
     * @return ID do usuário
     */
    public String getUserId() { 
        return userId; 
    }
    
    /**
     * Obtém o conteúdo do comentário.
     * 
     * @return conteúdo do comentário
     */
    public String getContent() { 
        return content; 
    }
    
    /**
     * Obtém o tempo de criação do comentário.
     * 
     * @return tempo de criação
     */
    public long getCreatedTime() { 
        return createdTime; 
    }
}