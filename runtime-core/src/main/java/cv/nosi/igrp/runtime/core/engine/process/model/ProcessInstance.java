package cv.nosi.igrp.runtime.core.engine.process.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe para representar informações de uma instância de processo.
 */
@Setter
@Getter
@ToString
public class ProcessInstance {
    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private long startTime;
    private IGRPProcessStatus status;
}