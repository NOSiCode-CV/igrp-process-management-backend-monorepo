package cv.nosi.igrp.runtime.core.engine.process.model;

public class ProcessDefinition {

    private String id;
    private String name;
    private String resourceName;
    private String key;
    private int version;
    private String deploymentId;
    private String description;
    private String applicationBase;
    private boolean suspended;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicationBase() {
        return applicationBase;
    }

    public void setApplicationBase(String applicationBase) {
        this.applicationBase = applicationBase;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return "ProcessDefinition{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", resourceName='" + resourceName + '\'' +
               ", key='" + key + '\'' +
               ", version=" + version +
               ", deploymentId='" + deploymentId + '\'' +
               ", description='" + description + '\'' +
               ", category='" + applicationBase + '\'' +
               ", suspended=" + suspended +
               '}';
    }
}
