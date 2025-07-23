package cv.nosi.igrp.runtime.core.engine.process.model;

public record ProcessVariableInstance(
        String name,

        String type,

        String processInstanceId,

        Object value
) {

}
