package cv.nosi.igrp.runtime.core.engine.task;

import java.util.Map;


public interface TaskCreator {

    String createTask(String processInstanceId, String taskDefinitionKey, String taskName, String assignee, Map<String, Object> variables);

}