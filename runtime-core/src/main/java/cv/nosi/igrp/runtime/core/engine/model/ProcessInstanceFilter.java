package cv.nosi.igrp.runtime.core.engine.model;

import cv.nosi.igrp.runtime.core.engine.IGRPProcessStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe para representar critérios de filtragem de instâncias de processo.
 */
@Getter
@Setter
public class ProcessInstanceFilter {
    private String processDefinitionKey;
    private String businessKey;
    private String startUserId;
    private IGRPProcessStatus status;
    private Long startedAfter;
    private Long startedBefore;
}