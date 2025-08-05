package cv.nosi.igrp.runtime.core.engine.task.model;

public class ProcessTaskInfo {

    private String taskKey;
    private String taskName;
    private IGRPTaskStatus status;
    private String processInstanceId;

    public ProcessTaskInfo(String taskKey, String taskName, IGRPTaskStatus status, String processInstanceId) {
        this.taskKey = taskKey;
        this.taskName = taskName;
        this.status = status;
        this.processInstanceId = processInstanceId;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public IGRPTaskStatus getStatus() {
        return status;
    }

    public void setStatus(IGRPTaskStatus status) {
        this.status = status;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String toString() {
        return "ProcessTaskInfo{" +
               "taskKey='" + taskKey + '\'' +
               ", taskName='" + taskName + '\'' +
               ", status=" + status +
               ", processInstanceId='" + processInstanceId + '\'' +
               '}';
    }
}