package cv.nosi.igrp.runtime.core.engine.task.model;

public class TaskVariableInstance {
    private String name;
    private String type;
    private String processInstanceId;
    private String taskId;
    private boolean isTaskVariable;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isTaskVariable() {
        return isTaskVariable;
    }

    public void setTaskVariable(boolean taskVariable) {
        isTaskVariable = taskVariable;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TaskVariableInstance{" +
               "name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", processInstanceId='" + processInstanceId + '\'' +
               ", taskId='" + taskId + '\'' +
               ", isTaskVariable=" + isTaskVariable +
               ", value=" + value +
               '}';
    }
}