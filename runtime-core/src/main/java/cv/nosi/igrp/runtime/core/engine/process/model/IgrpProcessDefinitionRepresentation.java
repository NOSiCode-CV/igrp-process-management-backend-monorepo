package cv.nosi.igrp.runtime.core.engine.process.model;

import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.nosi.igrp.runtime.core.engine.process.VariableRepresentation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class IgrpProcessDefinitionRepresentation implements ProcessDefinitionRepresentation {

    private String id;
    private String key;
    private String name;
    private String description;
    private String version;

    private String bpmnXml;
    private String bpmnUrl;
    private BpmnSourceType bpmnSourceType;
    private String resourceName;

    private boolean deployed;
    private String deploymentId;
    private LocalDateTime deployedAt;

    private List<VariableRepresentation> variables;
    private List<TaskRepresentation> tasks;
    private List<ProcessVersionRepresentation> versionHistory;

    @Builder
    public IgrpProcessDefinitionRepresentation(String id,
                                               String key,
                                               String name,
                                               String description,
                                               String version,
                                               String bpmnXml,
                                               String bpmnUrl,
                                               String resourceName,
                                               BpmnSourceType bpmnSourceType,
                                               boolean deployed,
                                               String deploymentId,
                                               LocalDateTime deployedAt,
                                               List<VariableRepresentation> variables,
                                               List<TaskRepresentation> tasks,
                                               List<ProcessVersionRepresentation> versionHistory) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.version = version;
        this.bpmnXml = bpmnXml;
        this.bpmnUrl = bpmnUrl;
        this.resourceName = resourceName;
        this.bpmnSourceType = bpmnSourceType;
        this.deployed = deployed;
        this.deploymentId = deploymentId;
        this.deployedAt = deployedAt;
        this.variables = variables == null ? new ArrayList<>() : variables;
        this.tasks = tasks == null ? new ArrayList<>() : tasks;
        this.versionHistory = versionHistory == null ? new ArrayList<>() : versionHistory;
    }

}
