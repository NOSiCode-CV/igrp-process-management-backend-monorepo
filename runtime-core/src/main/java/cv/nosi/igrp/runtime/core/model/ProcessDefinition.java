package cv.nosi.igrp.runtime.core.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Classe que representa a definição de um processo.
 * <p>
 * Contém informações sobre a definição de um processo, incluindo
 * sua chave, nome, versão e outros metadados.
 */
public class ProcessDefinition {
    
    private String id;
    private String key;
    private String name;
    private String description;
    private int version;
    private String deploymentId;
    private String resourceName;
    private String diagramResourceName;
    private String category;
    private boolean suspended;
    private Date deploymentTime;
    private String tenantId;
    private Map<String, Object> attributes;
    private Set<String> tags;
    private String owner;
    
    /**
     * Construtor padrão.
     */
    public ProcessDefinition() {
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param id ID da definição do processo
     * @param key chave da definição do processo
     * @param name nome do processo
     * @param version versão do processo
     */
    public ProcessDefinition(String id, String key, String name, int version) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.version = version;
    }
    
    /**
     * Obtém o ID da definição do processo.
     * 
     * @return ID da definição do processo
     */
    public String getId() {
        return id;
    }
    
    /**
     * Define o ID da definição do processo.
     * 
     * @param id ID da definição do processo
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtém a chave da definição do processo.
     * 
     * @return chave da definição do processo
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Define a chave da definição do processo.
     * 
     * @param key chave da definição do processo
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * Obtém o nome do processo.
     * 
     * @return nome do processo
     */
    public String getName() {
        return name;
    }
    
    /**
     * Define o nome do processo.
     * 
     * @param name nome do processo
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Obtém a descrição do processo.
     * 
     * @return descrição do processo
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Define a descrição do processo.
     * 
     * @param description descrição do processo
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Obtém a versão do processo.
     * 
     * @return versão do processo
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * Define a versão do processo.
     * 
     * @param version versão do processo
     */
    public void setVersion(int version) {
        this.version = version;
    }
    
    /**
     * Obtém o ID do deployment.
     * 
     * @return ID do deployment
     */
    public String getDeploymentId() {
        return deploymentId;
    }
    
    /**
     * Define o ID do deployment.
     * 
     * @param deploymentId ID do deployment
     */
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    /**
     * Obtém o nome do recurso.
     * 
     * @return nome do recurso
     */
    public String getResourceName() {
        return resourceName;
    }
    
    /**
     * Define o nome do recurso.
     * 
     * @param resourceName nome do recurso
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    /**
     * Obtém o nome do recurso do diagrama.
     * 
     * @return nome do recurso do diagrama
     */
    public String getDiagramResourceName() {
        return diagramResourceName;
    }
    
    /**
     * Define o nome do recurso do diagrama.
     * 
     * @param diagramResourceName nome do recurso do diagrama
     */
    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    /**
     * Obtém a categoria do processo.
     * 
     * @return categoria do processo
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Define a categoria do processo.
     * 
     * @param category categoria do processo
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * Verifica se o processo está suspenso.
     * 
     * @return true se o processo estiver suspenso
     */
    public boolean isSuspended() {
        return suspended;
    }
    
    /**
     * Define se o processo está suspenso.
     * 
     * @param suspended true se o processo estiver suspenso
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
    
    /**
     * Obtém a data de deployment.
     * 
     * @return data de deployment
     */
    public Date getDeploymentTime() {
        return deploymentTime;
    }
    
    /**
     * Define a data de deployment.
     * 
     * @param deploymentTime data de deployment
     */
    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
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
     * Obtém os atributos do processo.
     * 
     * @return atributos do processo
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Define os atributos do processo.
     * 
     * @param attributes atributos do processo
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Obtém as tags do processo.
     * 
     * @return tags do processo
     */
    public Set<String> getTags() {
        return tags;
    }
    
    /**
     * Define as tags do processo.
     * 
     * @param tags tags do processo
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Obtém o proprietário do processo.
     * 
     * @return proprietário do processo
     */
    public String getOwner() {
        return owner;
    }
    
    /**
     * Define o proprietário do processo.
     * 
     * @param owner proprietário do processo
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Obtém um atributo específico do processo.
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
     * Define um atributo específico do processo.
     * 
     * @param key chave do atributo
     * @param value valor do atributo
     */
    public void setAttribute(String key, Object value) {
        if (attributes != null) {
            attributes.put(key, value);
        }
    }
    
    /**
     * Adiciona uma tag ao processo.
     * 
     * @param tag tag a ser adicionada
     */
    public void addTag(String tag) {
        if (tags != null) {
            tags.add(tag);
        }
    }
    
    /**
     * Remove uma tag do processo.
     * 
     * @param tag tag a ser removida
     */
    public void removeTag(String tag) {
        if (tags != null) {
            tags.remove(tag);
        }
    }
    
    /**
     * Verifica se o processo possui uma determinada tag.
     * 
     * @param tag tag a ser verificada
     * @return true se o processo possuir a tag
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }
    
    @Override
    public String toString() {
        return "ProcessDefinition{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}