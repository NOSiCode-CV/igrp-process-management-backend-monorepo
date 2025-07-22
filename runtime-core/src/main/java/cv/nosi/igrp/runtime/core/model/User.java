package cv.nosi.igrp.runtime.core.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe que representa um usuário do sistema de processos.
 * <p>
 * Contém informações sobre um usuário, incluindo seus dados pessoais,
 * grupos, permissões e preferências.
 */
public class User {
    
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String displayName;
    private boolean active;
    private Set<String> groups;
    private Set<String> roles;
    private Map<String, Object> attributes;
    private String tenantId;
    private String type;
    
    /**
     * Construtor padrão.
     */
    public User() {
        this.groups = new HashSet<>();
        this.roles = new HashSet<>();
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param id ID do usuário
     * @param username nome de usuário
     * @param firstName primeiro nome
     * @param lastName sobrenome
     * @param email email
     */
    public User(String id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = true;
        this.groups = new HashSet<>();
        this.roles = new HashSet<>();
        
        // Define o nome de exibição como a combinação de primeiro nome e sobrenome
        this.displayName = firstName + " " + lastName;
    }
    
    /**
     * Obtém o ID do usuário.
     * 
     * @return ID do usuário
     */
    public String getId() {
        return id;
    }
    
    /**
     * Define o ID do usuário.
     * 
     * @param id ID do usuário
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome de usuário.
     * 
     * @return nome de usuário
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Define o nome de usuário.
     * 
     * @param username nome de usuário
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Obtém o primeiro nome do usuário.
     * 
     * @return primeiro nome
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Define o primeiro nome do usuário.
     * 
     * @param firstName primeiro nome
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateDisplayName();
    }
    
    /**
     * Obtém o sobrenome do usuário.
     * 
     * @return sobrenome
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Define o sobrenome do usuário.
     * 
     * @param lastName sobrenome
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateDisplayName();
    }
    
    /**
     * Obtém o email do usuário.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Define o email do usuário.
     * 
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtém o nome de exibição do usuário.
     * 
     * @return nome de exibição
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Define o nome de exibição do usuário.
     * 
     * @param displayName nome de exibição
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Verifica se o usuário está ativo.
     * 
     * @return true se o usuário estiver ativo
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Define se o usuário está ativo.
     * 
     * @param active true se o usuário estiver ativo
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Obtém os grupos do usuário.
     * 
     * @return grupos do usuário
     */
    public Set<String> getGroups() {
        return groups;
    }
    
    /**
     * Define os grupos do usuário.
     * 
     * @param groups grupos do usuário
     */
    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }
    
    /**
     * Obtém os papéis do usuário.
     * 
     * @return papéis do usuário
     */
    public Set<String> getRoles() {
        return roles;
    }
    
    /**
     * Define os papéis do usuário.
     * 
     * @param roles papéis do usuário
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    /**
     * Obtém os atributos do usuário.
     * 
     * @return atributos do usuário
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Define os atributos do usuário.
     * 
     * @param attributes atributos do usuário
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
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
     * Obtém o tipo do usuário.
     * 
     * @return tipo do usuário
     */
    public String getType() {
        return type;
    }
    
    /**
     * Define o tipo do usuário.
     * 
     * @param type tipo do usuário
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Adiciona um grupo ao usuário.
     * 
     * @param group grupo a ser adicionado
     */
    public void addGroup(String group) {
        if (groups != null) {
            groups.add(group);
        }
    }
    
    /**
     * Remove um grupo do usuário.
     * 
     * @param group grupo a ser removido
     */
    public void removeGroup(String group) {
        if (groups != null) {
            groups.remove(group);
        }
    }
    
    /**
     * Verifica se o usuário pertence a um determinado grupo.
     * 
     * @param group grupo a ser verificado
     * @return true se o usuário pertencer ao grupo
     */
    public boolean isMemberOf(String group) {
        return groups != null && groups.contains(group);
    }
    
    /**
     * Adiciona um papel ao usuário.
     * 
     * @param role papel a ser adicionado
     */
    public void addRole(String role) {
        if (roles != null) {
            roles.add(role);
        }
    }
    
    /**
     * Remove um papel do usuário.
     * 
     * @param role papel a ser removido
     */
    public void removeRole(String role) {
        if (roles != null) {
            roles.remove(role);
        }
    }
    
    /**
     * Verifica se o usuário possui um determinado papel.
     * 
     * @param role papel a ser verificado
     * @return true se o usuário possuir o papel
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Obtém um atributo específico do usuário.
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
     * Define um atributo específico do usuário.
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
     * Atualiza o nome de exibição com base no primeiro nome e sobrenome.
     */
    private void updateDisplayName() {
        if (firstName != null && lastName != null) {
            this.displayName = firstName + " " + lastName;
        } else if (firstName != null) {
            this.displayName = firstName;
        } else if (lastName != null) {
            this.displayName = lastName;
        }
    }
    
    /**
     * Obtém o nome completo do usuário.
     * 
     * @return nome completo
     */
    public String getFullName() {
        return getDisplayName();
    }
    
    /**
     * Verifica se o usuário tem permissão para executar uma determinada ação.
     * 
     * @param permission permissão a ser verificada
     * @return true se o usuário tiver permissão
     */
    public boolean hasPermission(String permission) {
        // Implementação básica - pode ser estendida para verificar permissões mais complexas
        return isActive() && hasRole("ADMIN") || hasRole(permission);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        User user = (User) o;
        
        return id != null ? id.equals(user.id) : user.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}