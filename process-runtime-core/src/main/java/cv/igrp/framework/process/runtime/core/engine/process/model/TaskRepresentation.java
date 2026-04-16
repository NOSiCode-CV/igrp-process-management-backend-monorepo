package cv.igrp.framework.process.runtime.core.engine.process.model;

import cv.igrp.framework.process.runtime.core.engine.process.VariableRepresentation;

import java.util.List;

public interface TaskRepresentation {

    String getId();

    String getKey();

    String getName();

    List<VariableRepresentation> getVariables();

}
