package cv.nosi.igrp.runtime.core.engine;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface para execução de processos.
 * <p>
 * Define operações para iniciar, suspender, retomar e finalizar instâncias de processos,
 * bem como para consultar o estado atual das instâncias em execução.
 */
public interface ProcessExecution {

    /**
     * Inicia uma nova instância de processo.
     *
     * @param processDefinitionKey chave da definição do processo
     * @param businessKey          chave de negócio opcional para a instância
     * @param variables            variáveis iniciais para o processo
     * @param startUserId          ID do usuário que está iniciando o processo
     * @return identificador da instância criada
     * @throws Exception se ocorrer algum erro durante a inicialização
     */
    String startProcess(String processDefinitionKey, String businessKey,
                        Map<String, Object> variables, String startUserId) throws Exception;

    /**
     * Suspende uma instância de processo em execução.
     *
     * @param processInstanceId identificador da instância
     * @throws Exception se ocorrer algum erro durante a suspensão
     */
    void suspendProcess(String processInstanceId) throws Exception;

    /**
     * Retoma a execução de uma instância de processo suspensa.
     *
     * @param processInstanceId identificador da instância
     * @throws Exception se ocorrer algum erro durante a retomada
     */
    void resumeProcess(String processInstanceId) throws Exception;

    /**
     * Termina uma instância de processo em execução.
     *
     * @param processInstanceId identificador da instância
     * @param deleteReason      motivo da terminação
     * @throws Exception se ocorrer algum erro durante a terminação
     */
    void terminateProcess(String processInstanceId, String deleteReason) throws Exception;

    /**
     * Obtém informações sobre uma instância de processo específica.
     *
     * @param processInstanceId identificador da instância
     * @return informações da instância, ou Optional vazio se não encontrada
     */
    Optional<ProcessInstanceInfo> getProcessInstance(String processInstanceId);

    /**
     * Lista instâncias de processo com base em critérios de filtragem.
     *
     * @param filter critérios de filtragem
     * @return lista de instâncias que correspondem aos critérios
     */
    List<ProcessInstanceInfo> listProcessInstances(ProcessInstanceFilter filter);

    /**
     * Atualiza variáveis de uma instância de processo.
     *
     * @param processInstanceId identificador da instância
     * @param variables         mapa de variáveis a serem atualizadas
     * @throws Exception se ocorrer algum erro durante a atualização
     */
    void setProcessVariables(String processInstanceId, Map<String, Object> variables) throws Exception;

    /**
     * Obtém variáveis de uma instância de processo.
     *
     * @param processInstanceId identificador da instância
     * @return mapa com as variáveis da instância
     * @throws Exception se ocorrer algum erro durante a consulta
     */
    Map<String, Object> getProcessVariables(String processInstanceId) throws Exception;

    /**
     * Classe interna para representar informações de uma instância de processo.
     */
    @Getter
    class ProcessInstanceInfo {
        private final String id;
        private final String processDefinitionId;
        private final String processDefinitionKey;
        private final String businessKey;
        private final String startUserId;
        private final long startTime;
        private final String status;

        public ProcessInstanceInfo(String id, String processDefinitionId, String processDefinitionKey, String businessKey, String startUserId, long startTime, String status) {
            this.id = id;
            this.processDefinitionId = processDefinitionId;
            this.processDefinitionKey = processDefinitionKey;
            this.businessKey = businessKey;
            this.startUserId = startUserId;
            this.startTime = startTime;
            this.status = status;
        }

    }

    /**
     * Classe interna para representar critérios de filtragem de instâncias de processo.
     */
    @Getter
    class ProcessInstanceFilter {
        private String processDefinitionKey;
        private String businessKey;
        private String startUserId;
        private String status;
        private Long startedAfter;
        private Long startedBefore;

        public ProcessInstanceFilter() {
        }

        public ProcessInstanceFilter processDefinitionKey(String processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
            return this;
        }

        public ProcessInstanceFilter businessKey(String businessKey) {
            this.businessKey = businessKey;
            return this;
        }

        public ProcessInstanceFilter startUserId(String startUserId) {
            this.startUserId = startUserId;
            return this;
        }

        public ProcessInstanceFilter status(String status) {
            this.status = status;
            return this;
        }

        public ProcessInstanceFilter startedAfter(Long startedAfter) {
            this.startedAfter = startedAfter;
            return this;
        }

        public ProcessInstanceFilter startedBefore(Long startedBefore) {
            this.startedBefore = startedBefore;
            return this;
        }

    }
}