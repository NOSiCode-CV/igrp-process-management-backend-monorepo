package cv.igrp.framework.runtime.core.engine.process;

public interface VariableRepresentation {

    String getName();

    String getType();

    Object getValue();

    boolean isRequired();

}
