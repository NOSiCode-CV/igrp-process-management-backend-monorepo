package cv.nosi.igrp.runtime.core.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Classe que representa uma tarefa de processo.
 * <p>
 * Contém informações sobre uma tarefa específica, incluindo
 * seu estado, atribuição, prazos e variáveis.
 */
public class Task {
    
    /**
     * Enumeração de possíveis estados de uma tarefa.
     */
    public enum State {
        CREATED,
        ASSIGNED,
        CLAIMED,
        COMPLETED,
        CANCELLED,
        SUSPENDED,
        DELEGATED
    }
    
    /**
     * Enumeração de possíveis prioridades de uma tarefa.
     */
    public enum Priority {
        LOW(0),
        NORMAL(50),
        HIGH(100),
        CRITICAL(150);
        
        private final int value;
        
        Priority(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public static Priority fromValue(int value) {
            if (value <= 25) {
                return LOW;
            } else if (value <= 75) {
                return NORMAL;
            } else if (value <= 125) {
                return HIGH;
            } else {
                return CRITICAL;
            }
        }
    }
    
    private String id;
    private String name;
    private String description;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String executionId;
    private String assignee;
    private String owner;
    private String delegationState;
    private Date createTime;
    private Date dueDate;
    private Date followUpDate;
    private Date claimTime;
    private Date completeTime;
    private Priority priority;
    private State state;
    private String formKey;
    private String tenantId;
    private String parentTaskId;
    private Map<String, Object> variables;
    private Set<String> candidateUsers;
    private Set<String> candidateGroups;
    private Map<String, Object> attributes;
    private Set<String> tags;
    private boolean suspended;
    
    /**
     * Construtor padrão.
     */
    public Task() {
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param id ID da tarefa
     * @param name nome da tarefa
     * @param processInstanceId ID da instância de processo
     * @param taskDefinitionKey chave de definição da tarefa
     */
    public Task(String id, String name, String processInstanceId, String taskDefinitionKey) {
        this.id = id;
        this.name = name;
        this.processInstanceId = processInstanceId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.createTime = new Date();
        this.priority = Priority.NORMAL;
        this.state = State.CREATED;
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
     * Define o ID da tarefa.
     * 
     * @param id ID da tarefa
     */
    public void setId(String id) {
        this.id = id;
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
     * Define o nome da tarefa.
     * 
     * @param name nome da tarefa
     */
    public void setName(String name) {
        this.name = name;
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
     * Define a descrição da tarefa.
     * 
     * @param description descrição da tarefa
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Define o ID da instância de processo.
     * 
     * @param processInstanceId ID da instância de processo
     */
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    /**
     * Obtém o ID da definição do processo.
     * 
     * @return ID da definição do processo
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }
    
    /**
     * Define o ID da definição do processo.
     * 
     * @param processDefinitionId ID da definição do processo
     */
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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
     * Obtém o ID da execução.
     * 
     * @return ID da execução
     */
    public String getExecutionId() {
        return executionId;
    }
    
    /**
     * Define o ID da execução.
     * 
     * @param executionId ID da execução
     */
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
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
     * Define o responsável pela tarefa.
     * 
     * @param assignee responsável pela tarefa
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
        if (assignee != null && !assignee.isEmpty()) {
            this.state = State.ASSIGNED;
        }
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
     * Define o proprietário da tarefa.
     * 
     * @param owner proprietário da tarefa
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Obtém o estado de delegação da tarefa.
     * 
     * @return estado de delegação
     */
    public String getDelegationState() {
        return delegationState;
    }
    
    /**
     * Define o estado de delegação da tarefa.
     * 
     * @param delegationState estado de delegação
     */
    public void setDelegationState(String delegationState) {
        this.delegationState = delegationState;
        if ("PENDING".equals(delegationState)) {
            this.state = State.DELEGATED;
        }
    }
    
    /**
     * Obtém a data de criação da tarefa.
     * 
     * @return data de criação
     */
    public Date getCreateTime() {
        return createTime;
    }
    
    /**
     * Define a data de criação da tarefa.
     * 
     * @param createTime data de criação
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * Obtém a data de vencimento da tarefa.
     * 
     * @return data de vencimento
     */
    public Date getDueDate() {
        return dueDate;
    }
    
    /**
     * Define a data de vencimento da tarefa.
     * 
     * @param dueDate data de vencimento
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    /**
     * Obtém a data de acompanhamento da tarefa.
     * 
     * @return data de acompanhamento
     */
    public Date getFollowUpDate() {
        return followUpDate;
    }
    
    /**
     * Define a data de acompanhamento da tarefa.
     * 
     * @param followUpDate data de acompanhamento
     */
    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    /**
     * Obtém a data de reivindicação da tarefa.
     * 
     * @return data de reivindicação
     */
    public Date getClaimTime() {
        return claimTime;
    }
    
    /**
     * Define a data de reivindicação da tarefa.
     * 
     * @param claimTime data de reivindicação
     */
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }
    
    /**
     * Obtém a data de conclusão da tarefa.
     * 
     * @return data de conclusão
     */
    public Date getCompleteTime() {
        return completeTime;
    }
    
    /**
     * Define a data de conclusão da tarefa.
     * 
     * @param completeTime data de conclusão
     */
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
        if (completeTime != null) {
            this.state = State.COMPLETED;
        }
    }
    
    /**
     * Obtém a prioridade da tarefa.
     * 
     * @return prioridade da tarefa
     */
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * Define a prioridade da tarefa.
     * 
     * @param priority prioridade da tarefa
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    /**
     * Define a prioridade da tarefa a partir de um valor numérico.
     * 
     * @param priorityValue valor numérico da prioridade
     */
    public void setPriorityValue(int priorityValue) {
        this.priority = Priority.fromValue(priorityValue);
    }
    
    /**
     * Obtém o estado da tarefa.
     * 
     * @return estado da tarefa
     */
    public State getState() {
        return state;
    }
    
    /**
     * Define o estado da tarefa.
     * 
     * @param state estado da tarefa
     */
    public void setState(State state) {
        this.state = state;
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
     * Define a chave do formulário da tarefa.
     * 
     * @param formKey chave do formulário
     */
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }
    
    /**
     * Obtém o ID do tenant.
     * 
     * @return ID do tenant
     */
    public String getTenantId() {
        return tenantId;
    }
    
    /**
     * Define o ID do tenant.
     * 
     * @param tenantId ID do tenant
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    /**
     * Obtém o ID da tarefa pai.
     * 
     * @return ID da tarefa pai
     */
    public String getParentTaskId() {
        return parentTaskId;
    }
    
    /**
     * Define o ID da tarefa pai.
     * 
     * @param parentTaskId ID da tarefa pai
     */
    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
    
    /**
     * Obtém as variáveis da tarefa.
     * 
     * @return variáveis da tarefa
     */
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    /**
     * Define as variáveis da tarefa.
     * 
     * @param variables variáveis da tarefa
     */
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    /**
     * Obtém os usuários candidatos para a tarefa.
     * 
     * @return usuários candidatos
     */
    public Set<String> getCandidateUsers() {
        return candidateUsers;
    }
    
    /**
     * Define os usuários candidatos para a tarefa.
     * 
     * @param candidateUsers usuários candidatos
     */
    public void setCandidateUsers(Set<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }
    
    /**
     * Obtém os grupos candidatos para a tarefa.
     * 
     * @return grupos candidatos
     */
    public Set<String> getCandidateGroups() {
        return candidateGroups;
    }
    
    /**
     * Define os grupos candidatos para a tarefa.
     * 
     * @param candidateGroups grupos candidatos
     */
    public void setCandidateGroups(Set<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }
    
    /**
     * Obtém os atributos da tarefa.
     * 
     * @return atributos da tarefa
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Define os atributos da tarefa.
     * 
     * @param attributes atributos da tarefa
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Obtém as tags da tarefa.
     * 
     * @return tags da tarefa
     */
    public Set<String> getTags() {
        return tags;
    }
    
    /**
     * Define as tags da tarefa.
     * 
     * @param tags tags da tarefa
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Verifica se a tarefa está suspensa.
     * 
     * @return true se a tarefa estiver suspensa
     */
    public boolean isSuspended() {
        return suspended;
    }
    
    /**
     * Define se a tarefa está suspensa.
     * 
     * @param suspended true se a tarefa estiver suspensa
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
        if (suspended) {
            this.state = State.SUSPENDED;
        } else if (this.state == State.SUSPENDED) {
            // Restaura o estado anterior
            if (this.assignee != null && !this.assignee.isEmpty()) {
                this.state = State.ASSIGNED;
            } else {
                this.state = State.CREATED;
            }
        }
    }
    
    /**
     * Obtém uma variável específica da tarefa.
     * 
     * @param <T> tipo da variável
     * @param name nome da variável
     * @return valor da variável
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariable(String name) {
        if (variables == null) {
            return null;
        }
        return (T) variables.get(name);
    }
    
    /**
     * Define uma variável específica da tarefa.
     * 
     * @param name nome da variável
     * @param value valor da variável
     */
    public void setVariable(String name, Object value) {
        if (variables != null) {
            variables.put(name, value);
        }
    }
    
    /**
     * Remove uma variável específica da tarefa.
     * 
     * @param name nome da variável
     * @return valor da variável removida
     */
    public Object removeVariable(String name) {
        if (variables != null) {
            return variables.remove(name);
        }
        return null;
    }
    
    /**
     * Adiciona um usuário candidato à tarefa.
     * 
     * @param userId ID do usuário
     */
    public void addCandidateUser(String userId) {
        if (candidateUsers != null) {
            candidateUsers.add(userId);
        }
    }
    
    /**
     * Remove um usuário candidato da tarefa.
     * 
     * @param userId ID do usuário
     */
    public void removeCandidateUser(String userId) {
        if (candidateUsers != null) {
            candidateUsers.remove(userId);
        }
    }
    
    /**
     * Adiciona um grupo candidato à tarefa.
     * 
     * @param groupId ID do grupo
     */
    public void addCandidateGroup(String groupId) {
        if (candidateGroups != null) {
            candidateGroups.add(groupId);
        }
    }
    
    /**
     * Remove um grupo candidato da tarefa.
     * 
     * @param groupId ID do grupo
     */
    public void removeCandidateGroup(String groupId) {
        if (candidateGroups != null) {
            candidateGroups.remove(groupId);
        }
    }
    
    /**
     * Reivindica a tarefa para um usuário.
     * 
     * @param userId ID do usuário
     */
    public void claim(String userId) {
        this.assignee = userId;
        this.claimTime = new Date();
        this.state = State.CLAIMED;
    }
    
    /**
     * Libera a tarefa, removendo o responsável.
     */
    public void unclaim() {
        this.assignee = null;
        this.state = State.CREATED;
    }
    
    /**
     * Completa a tarefa.
     */
    public void complete() {
        this.completeTime = new Date();
        this.state = State.COMPLETED;
    }
    
    /**
     * Cancela a tarefa.
     */
    public void cancel() {
        this.state = State.CANCELLED;
    }
    
    /**
     * Delega a tarefa para outro usuário.
     * 
     * @param userId ID do usuário
     */
    public void delegate(String userId) {
        this.owner = this.assignee;
        this.assignee = userId;
        this.delegationState = "PENDING";
        this.state = State.DELEGATED;
    }
    
    /**
     * Resolve uma tarefa delegada, retornando-a ao proprietário original.
     */
    public void resolve() {
        if ("PENDING".equals(this.delegationState)) {
            this.assignee = this.owner;
            this.delegationState = "RESOLVED";
            this.state = State.ASSIGNED;
        }
    }
    
    /**
     * Verifica se a tarefa está vencida.
     * 
     * @return true se a tarefa estiver vencida
     */
    public boolean isOverdue() {
        if (dueDate == null || state == State.COMPLETED || state == State.CANCELLED) {
            return false;
        }
        return new Date().after(dueDate);
    }
    
    /**
     * Obtém a duração da tarefa em milissegundos.
     * 
     * @return duração da tarefa em milissegundos, ou -1 se a tarefa ainda estiver em execução
     */
    public long getDuration() {
        if (createTime == null) {
            return -1;
        }
        
        if (completeTime == null) {
            return System.currentTimeMillis() - createTime.getTime();
        }
        
        return completeTime.getTime() - createTime.getTime();
    }
    
    /**
     * Adiciona uma tag à tarefa.
     * 
     * @param tag tag a ser adicionada
     */
    public void addTag(String tag) {
        if (tags != null) {
            tags.add(tag);
        }
    }
    
    /**
     * Remove uma tag da tarefa.
     * 
     * @param tag tag a ser removida
     */
    public void removeTag(String tag) {
        if (tags != null) {
            tags.remove(tag);
        }
    }
    
    /**
     * Verifica se a tarefa possui uma determinada tag.
     * 
     * @param tag tag a ser verificada
     * @return true se a tarefa possuir a tag
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }
    
    /**
     * Obtém um atributo específico da tarefa.
     * 
     * @param <T> tipo do atributo
     * @param key chave do atributo
     * @return valor do atributo
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return (T) attributes.get(key);
    }
    
    /**
     * Define um atributo específico da tarefa.
     * 
     * @param key chave do atributo
     * @param value valor do atributo
     */
    public void setAttribute(String key, Object value) {
        if (attributes != null) {
            attributes.put(key, value);
        }
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", assignee='" + assignee + '\'' +
                ", state=" + state +
                '}';
    }
}