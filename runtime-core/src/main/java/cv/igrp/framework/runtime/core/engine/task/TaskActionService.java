package cv.igrp.framework.runtime.core.engine.task;

import java.util.Map;


public interface TaskActionService {

    void saveTask(String taskId, Map<String, Object> variables);

    void completeTask(String taskId, Map<String, Object> variables);

    void assignTask(String taskId, String userId, String reason);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason);

    boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment);

    boolean setTaskDueDate(String taskId, long dueDate);

	void setTaskPriority(String taskInstanceId, int priority);

	void addCandidateGroup(String taskId, String groupId);

	void deleteCandidateGroup(String taskId, String groupId);
}