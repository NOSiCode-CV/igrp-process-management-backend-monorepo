package cv.nosi.igrp.runtime.core.engine.process.model;

public class ProcessVariableInstance {

    private String name;

    private String type;

    private String processInstanceId;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ProcessVariableInstance{" +
               "name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", processInstanceId='" + processInstanceId + '\'' +
               ", value=" + value +
               '}';
    }
}