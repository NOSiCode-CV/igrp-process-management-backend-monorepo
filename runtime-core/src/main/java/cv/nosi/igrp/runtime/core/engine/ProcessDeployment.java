package cv.nosi.igrp.runtime.core.engine;

import java.io.InputStream;
import java.util.List;
import cv.nosi.igrp.runtime.core.engine.deployment.DeploymentResource;
import cv.nosi.igrp.runtime.core.engine.deployment.DeploymentInfo;

/**
 * Interface para gerenciamento de deployments de processos.
 * <p>
 * Define operações para deploy, undeploy e gerenciamento de definições de processos
 * no motor de execução.
 */
public interface ProcessDeployment {
    
    /**
     * Realiza o deployment de um processo a partir de um arquivo BPMN.
     * 
     * @param processDefinitionStream stream contendo a definição do processo em formato BPMN
     * @param deploymentName nome do deployment
     * @return identificador único do deployment realizado
     * @throws Exception se ocorrer algum erro durante o deployment
     */
    String deploy(InputStream processDefinitionStream, String deploymentName) throws Exception;
    
    /**
     * Realiza o deployment de múltiplos processos em uma única operação.
     * 
     * @param resources mapa de recursos a serem deployados (nome -> stream)
     * @param deploymentName nome do deployment
     * @return identificador único do deployment realizado
     * @throws Exception se ocorrer algum erro durante o deployment
     */
    String deployMultiple(List<DeploymentResource> resources, String deploymentName) throws Exception;
    
    /**
     * Remove um deployment existente.
     * 
     * @param deploymentId identificador do deployment a ser removido
     * @param cascade se true, remove também todas as instâncias em execução
     * @throws Exception se ocorrer algum erro durante a remoção
     */
    void undeploy(String deploymentId, boolean cascade) throws Exception;
    
    /**
     * Verifica se um processo está deployado.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @return true se o processo estiver deployado, false caso contrário
     */
    boolean isDeployed(String processDefinitionKey);
    
    /**
     * Obtém a lista de todos os deployments ativos.
     * 
     * @return lista de deployments
     */
    List<DeploymentInfo> getDeployments();
    
    
}