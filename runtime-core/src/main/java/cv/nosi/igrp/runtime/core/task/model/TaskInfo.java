package cv.nosi.igrp.runtime.core.task.model;

/**
 * Classe para representar informações de uma tarefa.
 */
public class TaskInfo {
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
    
    /**
     * Construtor.
     * 
     * @param id ID da tarefa
     * @param name nome da tarefa
     * @param processInstanceId ID da instância de processo
     * @param taskDefinitionKey chave de definição da tarefa
     * @param assignee responsável pela tarefa
     * @param createdTime tempo de criação
     */
    public TaskInfo(String id, String name, String processInstanceId, String taskDefinitionKey,
                   String assignee, long createdTime) {
        this.id = id;
        this.name = name;
        this.processInstanceId = processInstanceId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.assignee = assignee;
        this.createdTime = createdTime;
    }
    
    /**
     * Obtém o ID da tarefa.
     * 
     * @return ID da tarefa
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Obtém o nome da tarefa.
     * 
     * @return nome da tarefa
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Obtém a descrição da tarefa.
     * 
     * @return descrição da tarefa
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * Obtém o ID da instância de processo.
     * 
     * @return ID da instância de processo
     */
    public String getProcessInstanceId() { 
        return processInstanceId; 
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
     * Obtém o responsável pela tarefa.
     * 
     * @return responsável pela tarefa
     */
    public String getAssignee() { 
        return assignee; 
    }
    
    /**
     * Obtém o proprietário da tarefa.
     * 
     * @return proprietário da tarefa
     */
    public String getOwner() { 
        return owner; 
    }
    
    /**
     * Obtém o tempo de criação da tarefa.
     * 
     * @return tempo de criação
     */
    public long getCreatedTime() { 
        return createdTime; 
    }
    
    /**
     * Obtém a data de vencimento da tarefa.
     * 
     * @return data de vencimento
     */
    public Long getDueDate() { 
        return dueDate; 
    }
    
    /**
     * Obtém a prioridade da tarefa.
     * 
     * @return prioridade da tarefa
     */
    public String getPriority() { 
        return priority; 
    }
    
    /**
     * Obtém a chave do formulário da tarefa.
     * 
     * @return chave do formulário
     */
    public String getFormKey() { 
        return formKey; 
    }
    
    /**
     * Define a descrição da tarefa.
     * 
     * @param description descrição da tarefa
     */
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    /**
     * Define o proprietário da tarefa.
     * 
     * @param owner proprietário da tarefa
     */
    public void setOwner(String owner) { 
        this.owner = owner; 
    }
    
    /**
     * Define a data de vencimento da tarefa.
     * 
     * @param dueDate data de vencimento
     */
    public void setDueDate(Long dueDate) { 
        this.dueDate = dueDate; 
    }
    
    /**
     * Define a prioridade da tarefa.
     * 
     * @param priority prioridade da tarefa
     */
    public void setPriority(String priority) { 
        this.priority = priority; 
    }
    
    /**
     * Define a chave do formulário da tarefa.
     * 
     * @param formKey chave do formulário
     */
    public void setFormKey(String formKey) { 
        this.formKey = formKey; 
    }
}