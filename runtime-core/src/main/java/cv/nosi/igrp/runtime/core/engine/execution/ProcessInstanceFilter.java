package cv.nosi.igrp.runtime.core.engine.execution;

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
    private String status;
    private Long startedAfter;
    private Long startedBefore;
}