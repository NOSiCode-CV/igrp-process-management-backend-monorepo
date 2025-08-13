package cv.igrp.framework.runtime.core.engine.task;

import java.util.Map;


public interface TaskActionService {

    void completeTask(String taskId, Map<String, Object> variables, String userId);

    void assignTask(String taskId, String userId, String reason);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason);

    boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment);

    void setTaskVariables(String taskId, Map<String, Object> variables);

    boolean setTaskDueDate(String taskId, long dueDate);

}