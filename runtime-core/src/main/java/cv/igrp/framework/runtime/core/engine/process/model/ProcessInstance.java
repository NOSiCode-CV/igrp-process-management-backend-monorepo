package cv.igrp.framework.runtime.core.engine.process.model;

import java.util.Date;

/**
 * Classe para representar informações de uma instância de processo.
 */
public record ProcessInstance(
        String id,
        String name,
        Date startDate,
        Date completedDate,
        String initiator,
        String processDefinitionId,
        String processDefinitionKey,
        String businessKey,
        String parentId,
        Integer processDefinitionVersion,
        String processDefinitionName,
        IGRPProcessStatus status
) {
}