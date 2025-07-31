package cv.nosi.igrp.runtime.core.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface TaskManager {

    String createTask(String processInstanceId, String taskDefinitionKey, String taskName, String assignee, Map<String, Object> variables);

    Optional<TaskInfo> getTask(String taskId);

    List<TaskInfo> listTasks(TaskFilter filter);

    void assignTask(String taskId, String userId, String reason);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    void completeTask(String taskId, Map<String, Object> variables, String userId);

    void setTaskVariables(String taskId, Map<String, Object> variables);

    List<TaskVariableInstance> getTaskVariables(String taskId);

    boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason);

    boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment);

    boolean setTaskDueDate(String taskId, long dueDate);

}