package cv.nosi.igrp.runtime.core.engine.process.model;

import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.nosi.igrp.runtime.core.engine.process.VariableRepresentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IgrpProcessDefinitionRepresentation implements ProcessDefinitionRepresentation {

    private final String key;
    private final String name;
    private final String description;
    private final String version;

    private final String bpmnXml;
    private final String bpmnUrl;
    private final BpmnSourceType bpmnSourceType;
    private final String resourceName;

    private final boolean deployed;
    private final String deploymentId;
    private final String applicationBase;
    private final LocalDateTime deployedAt;

    private final List<VariableRepresentation> variables;
    private final List<TaskRepresentation> tasks;
    private final List<ProcessVersionRepresentation> versionHistory;

    private IgrpProcessDefinitionRepresentation(
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
            String applicationBase,
            LocalDateTime deployedAt,
            List<VariableRepresentation> variables,
            List<TaskRepresentation> tasks,
            List<ProcessVersionRepresentation> versionHistory) {
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
        this.applicationBase = applicationBase;
        this.deployedAt = deployedAt;
        this.variables = variables == null ? new ArrayList<>() : variables;
        this.tasks = tasks == null ? new ArrayList<>() : tasks;
        this.versionHistory = versionHistory == null ? new ArrayList<>() : versionHistory;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getBpmnXml() {
        return bpmnXml;
    }

    @Override
    public String getBpmnUrl() {
        return bpmnUrl;
    }

    @Override
    public BpmnSourceType getBpmnSourceType() {
        return bpmnSourceType;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public boolean isDeployed() {
        return deployed;
    }

    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    @Override
    public String getApplicationBase() {
        return applicationBase;
    }

    @Override
    public LocalDateTime getDeployedAt() {
        return deployedAt;
    }

    @Override
    public List<VariableRepresentation> getVariables() {
        return variables;
    }

    @Override
    public List<TaskRepresentation> getTasks() {
        return tasks;
    }

    @Override
    public List<ProcessVersionRepresentation> getVersionHistory() {
        return versionHistory;
    }

    public static class Builder {
        private String key;
        private String name;
        private String description;
        private String version;
        private String bpmnXml;
        private String bpmnUrl;
        private String resourceName;
        private BpmnSourceType bpmnSourceType;
        private boolean deployed;
        private String deploymentId;
        private String applicationBase;
        private LocalDateTime deployedAt;
        private List<VariableRepresentation> variables;
        private List<TaskRepresentation> tasks;
        private List<ProcessVersionRepresentation> versionHistory;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder bpmnXml(String bpmnXml) {
            this.bpmnXml = bpmnXml;
            return this;
        }

        public Builder bpmnUrl(String bpmnUrl) {
            this.bpmnUrl = bpmnUrl;
            return this;
        }

        public Builder resourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public Builder bpmnSourceType(BpmnSourceType bpmnSourceType) {
            this.bpmnSourceType = bpmnSourceType;
            return this;
        }

        public Builder deployed(boolean deployed) {
            this.deployed = deployed;
            return this;
        }

        public Builder deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }

        public Builder applicationBase(String applicationBase) {
            this.applicationBase = applicationBase;
            return this;
        }

        public Builder deployedAt(LocalDateTime deployedAt) {
            this.deployedAt = deployedAt;
            return this;
        }

        public Builder variables(List<VariableRepresentation> variables) {
            this.variables = variables;
            return this;
        }

        public Builder tasks(List<TaskRepresentation> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Builder versionHistory(List<ProcessVersionRepresentation> versionHistory) {
            this.versionHistory = versionHistory;
            return this;
        }

        public IgrpProcessDefinitionRepresentation build() {
            return new IgrpProcessDefinitionRepresentation(
                    key, name, description, version, bpmnXml, bpmnUrl,
                    resourceName, bpmnSourceType, deployed, deploymentId,
                    applicationBase, deployedAt, variables, tasks, versionHistory
            );
        }
    }

    @Override
    public String toString() {
        return "IgrpProcessDefinitionRepresentation{" +
               " key='" + key + '\'' +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", version='" + version + '\'' +
               ", bpmnXml='" + bpmnXml + '\'' +
               ", bpmnUrl='" + bpmnUrl + '\'' +
               ", bpmnSourceType=" + bpmnSourceType +
               ", resourceName='" + resourceName + '\'' +
               ", deployed=" + deployed +
               ", deploymentId='" + deploymentId + '\'' +
               ", applicationBase='" + applicationBase + '\'' +
               ", deployedAt=" + deployedAt +
               ", variables=" + variables +
               ", tasks=" + tasks +
               ", versionHistory=" + versionHistory +
               '}';
    }
}
