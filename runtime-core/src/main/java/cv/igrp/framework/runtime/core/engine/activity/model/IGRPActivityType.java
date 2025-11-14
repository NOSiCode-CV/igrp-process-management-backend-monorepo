package cv.igrp.framework.runtime.core.engine.activity.model;

public enum IGRPActivityType {
    USER_TASK,
    SERVICE_TASK,
    SCRIPT_TASK,
    MANUAL_TASK,
    RECEIVE_TASK,
    SEND_TASK,
    BUSINESS_RULE_TASK,
    EXCLUSIVE_GATEWAY,
    PARALLEL_GATEWAY,
    INCLUSIVE_GATEWAY,
    MESSAGE_INTERMEDIATE_EVENT_CATCH,
    CALL_ACTIVITY,
    SUB_PROCESS,
    OTHER
}