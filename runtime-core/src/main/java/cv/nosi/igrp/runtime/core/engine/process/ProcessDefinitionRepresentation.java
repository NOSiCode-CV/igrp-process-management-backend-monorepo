package cv.nosi.igrp.runtime.core.engine.process;

import cv.nosi.igrp.runtime.core.engine.process.model.BpmnSourceType;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessVersionRepresentation;
import cv.nosi.igrp.runtime.core.engine.process.model.TaskRepresentation;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcessDefinitionRepresentation {

    String getId();

    String getKey();

    String getName();

    String getDescription();

    String getVersion();

    BpmnSourceType getBpmnSourceType();

    String getBpmnXml();

    String getBpmnUrl();

    String getResourceName();

    boolean isDeployed();

    String getDeploymentId();

    LocalDateTime getDeployedAt();

    List<VariableRepresentation> getVariables();

    List<TaskRepresentation> getTasks();

    List<ProcessVersionRepresentation> getVersionHistory();

    String getApplicationBase();
}

