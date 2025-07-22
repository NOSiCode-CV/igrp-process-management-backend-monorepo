package cv.nosi.igrp.runtime.core.engine;

/**
 * Interface principal para o motor de processos.
 * <p>
 * Define as operações fundamentais que um motor de processos deve implementar
 * para gerenciar o ciclo de vida completo dos processos de negócio.
 */
public interface ProcessEngine {
    
    /**
     * Inicializa o motor de processos com a configuração especificada.
     * 
     * @return true se a inicialização for bem-sucedida, false caso contrário
     */
    boolean initialize();
    
    /**
     * Obtém o deployment manager associado a este motor.
     * 
     * @return o gerenciador de deployment
     */
    ProcessDeployment getDeploymentManager();
    
    /**
     * Obtém o execution manager associado a este motor.
     * 
     * @return o gerenciador de execução
     */
    ProcessExecution getExecutionManager();
    
    /**
     * Verifica se o motor está em execução.
     * 
     * @return true se o motor estiver em execução, false caso contrário
     */
    boolean isRunning();
    
    /**
     * Encerra o motor de processos, liberando todos os recursos.
     */
    void shutdown();
}