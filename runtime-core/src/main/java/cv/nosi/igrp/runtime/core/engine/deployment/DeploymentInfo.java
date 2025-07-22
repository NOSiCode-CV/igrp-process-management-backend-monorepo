package cv.nosi.igrp.runtime.core.engine.deployment;

/**
 * Classe para representar informações de um deployment.
 */
public class DeploymentInfo {
    private String id;
    private String name;
    private long deploymentTime;
    
    /**
     * Construtor.
     * 
     * @param id ID do deployment
     * @param name nome do deployment
     * @param deploymentTime tempo de deployment
     */
    public DeploymentInfo(String id, String name, long deploymentTime) {
        this.id = id;
        this.name = name;
        this.deploymentTime = deploymentTime;
    }
    
    /**
     * Obtém o ID do deployment.
     * 
     * @return ID do deployment
     */
    public String getId() {
        return id;
    }
    
    /**
     * Obtém o nome do deployment.
     * 
     * @return nome do deployment
     */
    public String getName() {
        return name;
    }
    
    /**
     * Obtém o tempo de deployment.
     * 
     * @return tempo de deployment
     */
    public long getDeploymentTime() {
        return deploymentTime;
    }
}