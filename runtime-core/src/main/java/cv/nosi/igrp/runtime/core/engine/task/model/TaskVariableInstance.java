package cv.nosi.igrp.runtime.core.engine.task.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskVariableInstance {
    private String name;

    private String type;

    private String processInstanceId;

    private String taskId;

    private boolean isTaskVariable;

    private Object value;
}