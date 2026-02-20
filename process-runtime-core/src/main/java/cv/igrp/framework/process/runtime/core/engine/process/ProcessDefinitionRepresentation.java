package cv.igrp.framework.process.runtime.core.engine.process;

import cv.igrp.framework.process.runtime.core.engine.process.model.BpmnSourceType;
import cv.igrp.framework.process.runtime.core.engine.process.model.ProcessVersionRepresentation;
import cv.igrp.framework.process.runtime.core.engine.process.model.TaskRepresentation;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcessDefinitionRepresentation {

    String key();

    String name();

    String description();

    String version();

    BpmnSourceType bpmnSourceType();

    String bpmnXml();

    String bpmnUrl();

    String resourceName();

    boolean deployed();

    String deploymentId();

    LocalDateTime deployedAt();

    List<VariableRepresentation> variables();

    List<TaskRepresentation> tasks();

    List<ProcessVersionRepresentation> versionHistory();

    String applicationBase();
}

