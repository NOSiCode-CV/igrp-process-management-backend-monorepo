package cv.nosi.igrp.runtime.core.task.filter;

import lombok.Getter;

/**
 * Classe para representar critérios de filtragem de tarefas.
 */
@Getter
public class TaskFilter {
    /**
     * -- GETTER --
     *  Obtém o responsável pela tarefa.
     *
     * @return responsável pela tarefa
     */
    private String assignee;
    /**
     * -- GETTER --
     *  Obtém o ID da instância de processo.
     *
     * @return ID da instância de processo
     */
    private String processInstanceId;
    /**
     * -- GETTER --
     *  Obtém o nome da tarefa.
     *
     * @return nome da tarefa
     */
    private String taskName;
    /**
     * -- GETTER --
     *  Obtém a chave de definição da tarefa.
     *
     * @return chave de definição da tarefa
     */
    private String taskDefinitionKey;
    /**
     * -- GETTER --
     *  Verifica se devem ser incluídas apenas tarefas não atribuídas.
     *
     * @return true para incluir apenas tarefas não atribuídas
     */
    private Boolean unassigned;
    /**
     * -- GETTER --
     *  Obtém o tempo mínimo de criação.
     *
     * @return tempo mínimo de criação (timestamp)
     */
    private Long createdAfter;
    /**
     * -- GETTER --
     *  Obtém o tempo máximo de criação.
     *
     * @return tempo máximo de criação (timestamp)
     */
    private Long createdBefore;
    /**
     * -- GETTER --
     *  Obtém a data mínima de vencimento.
     *
     * @return data mínima de vencimento (timestamp)
     */
    private Long dueDateAfter;
    /**
     * -- GETTER --
     *  Obtém a data máxima de vencimento.
     *
     * @return data máxima de vencimento (timestamp)
     */
    private Long dueDateBefore;
    
    /**
     * Construtor padrão.
     */
    public TaskFilter() {
    }
    
    /**
     * Define o responsável pela tarefa como critério de filtragem.
     * 
     * @param assignee responsável pela tarefa
     * @return this para encadeamento de métodos
     */
    public TaskFilter assignee(String assignee) {
        this.assignee = assignee;
        return this;
    }
    
    /**
     * Define o ID da instância de processo como critério de filtragem.
     * 
     * @param processInstanceId ID da instância de processo
     * @return this para encadeamento de métodos
     */
    public TaskFilter processInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    /**
     * Define o nome da tarefa como critério de filtragem.
     * 
     * @param taskName nome da tarefa
     * @return this para encadeamento de métodos
     */
    public TaskFilter taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }
    
    /**
     * Define a chave de definição da tarefa como critério de filtragem.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     * @return this para encadeamento de métodos
     */
    public TaskFilter taskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
        return this;
    }
    
    /**
     * Define se devem ser incluídas apenas tarefas não atribuídas.
     * 
     * @param unassigned true para incluir apenas tarefas não atribuídas
     * @return this para encadeamento de métodos
     */
    public TaskFilter unassigned(Boolean unassigned) {
        this.unassigned = unassigned;
        return this;
    }
    
    /**
     * Define o tempo mínimo de criação como critério de filtragem.
     * 
     * @param createdAfter tempo mínimo de criação (timestamp)
     * @return this para encadeamento de métodos
     */
    public TaskFilter createdAfter(Long createdAfter) {
        this.createdAfter = createdAfter;
        return this;
    }
    
    /**
     * Define o tempo máximo de criação como critério de filtragem.
     * 
     * @param createdBefore tempo máximo de criação (timestamp)
     * @return this para encadeamento de métodos
     */
    public TaskFilter createdBefore(Long createdBefore) {
        this.createdBefore = createdBefore;
        return this;
    }
    
    /**
     * Define a data mínima de vencimento como critério de filtragem.
     * 
     * @param dueDateAfter data mínima de vencimento (timestamp)
     * @return this para encadeamento de métodos
     */
    public TaskFilter dueDateAfter(Long dueDateAfter) {
        this.dueDateAfter = dueDateAfter;
        return this;
    }
    
    /**
     * Define a data máxima de vencimento como critério de filtragem.
     * 
     * @param dueDateBefore data máxima de vencimento (timestamp)
     * @return this para encadeamento de métodos
     */
    public TaskFilter dueDateBefore(Long dueDateBefore) {
        this.dueDateBefore = dueDateBefore;
        return this;
    }

}