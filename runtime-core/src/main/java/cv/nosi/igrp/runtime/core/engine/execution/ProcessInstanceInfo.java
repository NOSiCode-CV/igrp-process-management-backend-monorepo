package cv.nosi.igrp.runtime.core.engine.execution;

import lombok.Getter;

/**
 * Classe para representar informações de uma instância de processo.
 */
@Getter
public class ProcessInstanceInfo {
    private final String id;
    private final String processDefinitionId;
    private final String processDefinitionKey;
    private final String businessKey;
    private final String startUserId;
    private final long startTime;
    private final String status;

    /**
     * Construtor.
     * 
     * @param id ID da instância de processo
     * @param processDefinitionId ID da definição do processo
     * @param processDefinitionKey chave da definição do processo
     * @param businessKey chave de negócio
     * @param startUserId ID do usuário que iniciou o processo
     * @param startTime tempo de início
     * @param status status da instância
     */
    public ProcessInstanceInfo(String id, String processDefinitionId, String processDefinitionKey, 
                              String businessKey, String startUserId, long startTime, String status) {
        this.id = id;
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionKey = processDefinitionKey;
        this.businessKey = businessKey;
        this.startUserId = startUserId;
        this.startTime = startTime;
        this.status = status;
    }
}