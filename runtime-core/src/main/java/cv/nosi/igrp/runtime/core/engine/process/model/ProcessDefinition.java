package cv.nosi.igrp.runtime.core.engine.process.model;

public class ProcessDefinition {

    private String id;
    private String name;
    private String key;
    private int version;
    private String deploymentId;
    private String description;
    private String category;
    private String tenantId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String toString() {
        return "ProcessDefinition{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", key='" + key + '\'' +
               ", version=" + version +
               ", deploymentId='" + deploymentId + '\'' +
               ", description='" + description + '\'' +
               ", category='" + category + '\'' +
               ", tenantId='" + tenantId + '\'' +
               ", suspended=" + suspended +
               '}';
    }
}
