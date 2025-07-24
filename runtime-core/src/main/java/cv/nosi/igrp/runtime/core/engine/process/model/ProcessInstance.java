package cv.nosi.igrp.runtime.core.engine.process.model;

import cv.nosi.igrp.runtime.core.engine.process.IGRPProcessStatus;

/**
 * Classe para representar informações de uma instância de processo.
 */
public record ProcessInstance(
        String id,
        String processDefinitionId,
        String processDefinitionKey,
        String businessKey,
        String startUserId,
        long startTime,
        IGRPProcessStatus status) {

}