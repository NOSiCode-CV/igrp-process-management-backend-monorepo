package cv.nosi.igrp.runtime.core.engine.deployment;

import java.io.InputStream;

/**
 * Classe para representar um recurso a ser deployado.
 */
public class DeploymentResource {
    private String name;
    private InputStream content;
    
    /**
     * Construtor.
     * 
     * @param name nome do recurso
     * @param content conteúdo do recurso
     */
    public DeploymentResource(String name, InputStream content) {
        this.name = name;
        this.content = content;
    }
    
    /**
     * Obtém o nome do recurso.
     * 
     * @return nome do recurso
     */
    public String getName() {
        return name;
    }
    
    /**
     * Obtém o conteúdo do recurso.
     * 
     * @return conteúdo do recurso
     */
    public InputStream getContent() {
        return content;
    }
}