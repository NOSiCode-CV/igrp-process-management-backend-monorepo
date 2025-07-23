package cv.nosi.igrp.runtime.core.engine.model;

public record ProcessVariableInstance(
        String name,

        String type,

        String processInstanceId,

        Object value
) {

}
