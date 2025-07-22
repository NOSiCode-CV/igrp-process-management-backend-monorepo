package cv.nosi.igrp.runtime.core.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Classe que representa uma instância de processo em execução.
 * <p>
 * Contém informações sobre uma instância específica de um processo,
 * incluindo seu estado atual, variáveis e histórico de execução.
 */
public class ProcessInstance {
    
    /**
     * Enumeração de possíveis estados de uma instância de processo.
     */
    public enum State {
        ACTIVE,
        SUSPENDED,
        COMPLETED,
        TERMINATED,
        CANCELLED,
        FAILED
    }
    
    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private int processDefinitionVersion;
    private String businessKey;
    private String parentProcessInstanceId;
    private String rootProcessInstanceId;
    private String caseInstanceId;
    private State state;
    private Date startTime;
    private Date endTime;
    private String startUserId;
    private String superExecutionId;
    private String tenantId;
    private String description;
    private Map<String, Object> variables;
    private Set<String> activeActivityIds;
    private String currentActivityId;
    private String currentActivityName;
    private Map<String, Object> attributes;
    private Set<String> tags;
    private String callbackId;
    private String callbackType;
    
    /**
     * Construtor padrão.
     */
    public ProcessInstance() {
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param id ID da instância de processo
     * @param processDefinitionId ID da definição do processo
     * @param businessKey chave de negócio (opcional)
     * @param state estado da instância
     */
    public ProcessInstance(String id, String processDefinitionId, String businessKey, State state) {
        this.id = id;
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.state = state;
        this.startTime = new Date();
    }
    
    /**
     * Obtém o ID da instância de processo.
     * 
     * @return ID da instância de processo
     */
    public String getId() {
        return id;
    }
    
    /**
     * Define o ID da instância de processo.
     * 
     * @param id ID da instância de processo
     */
    public void setId(String id) {
        this.id = id;
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
     * Obtém a chave da definição do processo.
     * 
     * @return chave da definição do processo
     */
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
    
    /**
     * Define a chave da definição do processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     */
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    /**
     * Obtém o nome da definição do processo.
     * 
     * @return nome da definição do processo
     */
    public String getProcessDefinitionName() {
        return processDefinitionName;
    }
    
    /**
     * Define o nome da definição do processo.
     * 
     * @param processDefinitionName nome da definição do processo
     */
    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
    
    /**
     * Obtém a versão da definição do processo.
     * 
     * @return versão da definição do processo
     */
    public int getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }
    
    /**
     * Define a versão da definição do processo.
     * 
     * @param processDefinitionVersion versão da definição do processo
     */
    public void setProcessDefinitionVersion(int processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }
    
    /**
     * Obtém a chave de negócio da instância.
     * 
     * @return chave de negócio
     */
    public String getBusinessKey() {
        return businessKey;
    }
    
    /**
     * Define a chave de negócio da instância.
     * 
     * @param businessKey chave de negócio
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    
    /**
     * Obtém o ID da instância de processo pai.
     * 
     * @return ID da instância de processo pai
     */
    public String getParentProcessInstanceId() {
        return parentProcessInstanceId;
    }
    
    /**
     * Define o ID da instância de processo pai.
     * 
     * @param parentProcessInstanceId ID da instância de processo pai
     */
    public void setParentProcessInstanceId(String parentProcessInstanceId) {
        this.parentProcessInstanceId = parentProcessInstanceId;
    }
    
    /**
     * Obtém o ID da instância de processo raiz.
     * 
     * @return ID da instância de processo raiz
     */
    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }
    
    /**
     * Define o ID da instância de processo raiz.
     * 
     * @param rootProcessInstanceId ID da instância de processo raiz
     */
    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    /**
     * Obtém o ID da instância de caso.
     * 
     * @return ID da instância de caso
     */
    public String getCaseInstanceId() {
        return caseInstanceId;
    }
    
    /**
     * Define o ID da instância de caso.
     * 
     * @param caseInstanceId ID da instância de caso
     */
    public void setCaseInstanceId(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }
    
    /**
     * Obtém o estado da instância.
     * 
     * @return estado da instância
     */
    public State getState() {
        return state;
    }
    
    /**
     * Define o estado da instância.
     * 
     * @param state estado da instância
     */
    public void setState(State state) {
        this.state = state;
    }
    
    /**
     * Obtém a data de início da instância.
     * 
     * @return data de início
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * Define a data de início da instância.
     * 
     * @param startTime data de início
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * Obtém a data de término da instância.
     * 
     * @return data de término
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * Define a data de término da instância.
     * 
     * @param endTime data de término
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * Obtém o ID do usuário que iniciou a instância.
     * 
     * @return ID do usuário que iniciou a instância
     */
    public String getStartUserId() {
        return startUserId;
    }
    
    /**
     * Define o ID do usuário que iniciou a instância.
     * 
     * @param startUserId ID do usuário que iniciou a instância
     */
    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
    
    /**
     * Obtém o ID da super execução.
     * 
     * @return ID da super execução
     */
    public String getSuperExecutionId() {
        return superExecutionId;
    }
    
    /**
     * Define o ID da super execução.
     * 
     * @param superExecutionId ID da super execução
     */
    public void setSuperExecutionId(String superExecutionId) {
        this.superExecutionId = superExecutionId;
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
     * Obtém a descrição da instância.
     * 
     * @return descrição da instância
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Define a descrição da instância.
     * 
     * @param description descrição da instância
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Obtém as variáveis da instância.
     * 
     * @return variáveis da instância
     */
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    /**
     * Define as variáveis da instância.
     * 
     * @param variables variáveis da instância
     */
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    /**
     * Obtém os IDs das atividades ativas.
     * 
     * @return IDs das atividades ativas
     */
    public Set<String> getActiveActivityIds() {
        return activeActivityIds;
    }
    
    /**
     * Define os IDs das atividades ativas.
     * 
     * @param activeActivityIds IDs das atividades ativas
     */
    public void setActiveActivityIds(Set<String> activeActivityIds) {
        this.activeActivityIds = activeActivityIds;
    }
    
    /**
     * Obtém o ID da atividade atual.
     * 
     * @return ID da atividade atual
     */
    public String getCurrentActivityId() {
        return currentActivityId;
    }
    
    /**
     * Define o ID da atividade atual.
     * 
     * @param currentActivityId ID da atividade atual
     */
    public void setCurrentActivityId(String currentActivityId) {
        this.currentActivityId = currentActivityId;
    }
    
    /**
     * Obtém o nome da atividade atual.
     * 
     * @return nome da atividade atual
     */
    public String getCurrentActivityName() {
        return currentActivityName;
    }
    
    /**
     * Define o nome da atividade atual.
     * 
     * @param currentActivityName nome da atividade atual
     */
    public void setCurrentActivityName(String currentActivityName) {
        this.currentActivityName = currentActivityName;
    }
    
    /**
     * Obtém os atributos da instância.
     * 
     * @return atributos da instância
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Define os atributos da instância.
     * 
     * @param attributes atributos da instância
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Obtém as tags da instância.
     * 
     * @return tags da instância
     */
    public Set<String> getTags() {
        return tags;
    }
    
    /**
     * Define as tags da instância.
     * 
     * @param tags tags da instância
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Obtém o ID de callback.
     * 
     * @return ID de callback
     */
    public String getCallbackId() {
        return callbackId;
    }
    
    /**
     * Define o ID de callback.
     * 
     * @param callbackId ID de callback
     */
    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }
    
    /**
     * Obtém o tipo de callback.
     * 
     * @return tipo de callback
     */
    public String getCallbackType() {
        return callbackType;
    }
    
    /**
     * Define o tipo de callback.
     * 
     * @param callbackType tipo de callback
     */
    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }
    
    /**
     * Obtém uma variável específica da instância.
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
     * Define uma variável específica da instância.
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
     * Remove uma variável específica da instância.
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
     * Verifica se a instância está ativa.
     * 
     * @return true se a instância estiver ativa
     */
    public boolean isActive() {
        return state == State.ACTIVE;
    }
    
    /**
     * Verifica se a instância está suspensa.
     * 
     * @return true se a instância estiver suspensa
     */
    public boolean isSuspended() {
        return state == State.SUSPENDED;
    }
    
    /**
     * Verifica se a instância está concluída.
     * 
     * @return true se a instância estiver concluída
     */
    public boolean isCompleted() {
        return state == State.COMPLETED;
    }
    
    /**
     * Verifica se a instância está terminada.
     * 
     * @return true se a instância estiver terminada
     */
    public boolean isTerminated() {
        return state == State.TERMINATED;
    }
    
    /**
     * Verifica se a instância está cancelada.
     * 
     * @return true se a instância estiver cancelada
     */
    public boolean isCancelled() {
        return state == State.CANCELLED;
    }
    
    /**
     * Verifica se a instância falhou.
     * 
     * @return true se a instância tiver falhado
     */
    public boolean isFailed() {
        return state == State.FAILED;
    }
    
    /**
     * Verifica se a instância está encerrada (concluída, terminada, cancelada ou falha).
     * 
     * @return true se a instância estiver encerrada
     */
    public boolean isEnded() {
        return isCompleted() || isTerminated() || isCancelled() || isFailed();
    }
    
    /**
     * Obtém a duração da instância em milissegundos.
     * 
     * @return duração da instância em milissegundos, ou -1 se a instância ainda estiver em execução
     */
    public long getDuration() {
        if (startTime == null) {
            return -1;
        }
        
        if (endTime == null) {
            return System.currentTimeMillis() - startTime.getTime();
        }
        
        return endTime.getTime() - startTime.getTime();
    }
    
    /**
     * Adiciona uma tag à instância.
     * 
     * @param tag tag a ser adicionada
     */
    public void addTag(String tag) {
        if (tags != null) {
            tags.add(tag);
        }
    }
    
    /**
     * Remove uma tag da instância.
     * 
     * @param tag tag a ser removida
     */
    public void removeTag(String tag) {
        if (tags != null) {
            tags.remove(tag);
        }
    }
    
    /**
     * Verifica se a instância possui uma determinada tag.
     * 
     * @param tag tag a ser verificada
     * @return true se a instância possuir a tag
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }
    
    /**
     * Obtém um atributo específico da instância.
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
     * Define um atributo específico da instância.
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
        return "ProcessInstance{" +
                "id='" + id + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", state=" + state +
                '}';
    }
}