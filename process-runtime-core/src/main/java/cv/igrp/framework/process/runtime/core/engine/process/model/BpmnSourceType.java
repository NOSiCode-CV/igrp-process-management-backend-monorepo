package cv.igrp.framework.process.runtime.core.engine.process.model;

public enum BpmnSourceType {

    INLINE_XML,    // Raw XML string
    EXTERNAL_URL   // URL to fetch BPMN (e.g. from Git, web, file server, etc.)

}
