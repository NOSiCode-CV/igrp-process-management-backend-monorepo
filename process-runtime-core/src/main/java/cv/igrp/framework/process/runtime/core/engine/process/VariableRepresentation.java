package cv.igrp.framework.process.runtime.core.engine.process;

public interface VariableRepresentation {

    String getName();

    String getType();

    Object getValue();

    boolean isRequired();

}
