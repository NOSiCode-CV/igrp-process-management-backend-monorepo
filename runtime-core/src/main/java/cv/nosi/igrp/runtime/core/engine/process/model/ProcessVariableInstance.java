package cv.nosi.igrp.runtime.core.engine.process.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProcessVariableInstance {
    private String name;

    private String type;

    private String processInstanceId;

    private Object value;
}