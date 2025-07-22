package cv.nosi.igrp.runtime.core.engine.execution;

import lombok.Getter;

/**
 * Classe para representar critérios de filtragem de instâncias de processo.
 */
@Getter
public class ProcessInstanceFilter {
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private String status;
    private Long startedAfter;
    private Long startedBefore;

    /**
     * Construtor padrão.
     */
    public ProcessInstanceFilter() {
    }

    /**
     * Define a chave da definição do processo como critério de filtragem.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter processDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }

    /**
     * Define a chave de negócio como critério de filtragem.
     * 
     * @param businessKey chave de negócio
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter businessKey(String businessKey) {
        this.businessKey = businessKey;
        return this;
    }

    /**
     * Define o ID do usuário que iniciou o processo como critério de filtragem.
     * 
     * @param startUserId ID do usuário
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter startUserId(String startUserId) {
        this.startUserId = startUserId;
        return this;
    }

    /**
     * Define o status como critério de filtragem.
     * 
     * @param status status da instância
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Define o tempo mínimo de início como critério de filtragem.
     * 
     * @param startedAfter tempo mínimo de início (timestamp)
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter startedAfter(Long startedAfter) {
        this.startedAfter = startedAfter;
        return this;
    }

    /**
     * Define o tempo máximo de início como critério de filtragem.
     * 
     * @param startedBefore tempo máximo de início (timestamp)
     * @return this para encadeamento de métodos
     */
    public ProcessInstanceFilter startedBefore(Long startedBefore) {
        this.startedBefore = startedBefore;
        return this;
    }
}