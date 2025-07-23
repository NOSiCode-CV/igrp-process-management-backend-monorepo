package cv.nosi.igrp.runtime.core.engine.model;

import cv.nosi.igrp.runtime.core.engine.IGRPProcessStatus;

/**
 * Classe para representar informações de uma instância de processo.
 */
public record ProcessInstanceInfo(
        String id,
        String processDefinitionId,
        String processDefinitionKey,
        String businessKey,
        String startUserId,
        long startTime,
        IGRPProcessStatus status) {
    /**
     * @param id                   ID da instância de processo
     * @param processDefinitionId  ID da definição do processo
     * @param processDefinitionKey chave da definição do processo
     * @param businessKey          chave de negócio
     * @param startUserId          ID do usuário que iniciou o processo
     * @param startTime            tempo de início
     * @param status               status da instância
     */
    public ProcessInstanceInfo {
    }
}