package cv.nosi.igrp.runtime.core.engine.process.model;

import java.time.LocalDateTime;

public interface ProcessVersionRepresentation {

    String getVersion();

    String getDeploymentId();

    String getBpmnXml();

    LocalDateTime getDeployedAt();

    boolean isActive();

}
