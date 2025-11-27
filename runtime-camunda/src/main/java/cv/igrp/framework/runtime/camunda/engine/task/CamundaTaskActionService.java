package cv.igrp.framework.runtime.camunda.engine.task;

import cv.igrp.framework.runtime.core.engine.task.TaskActionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CamundaTaskActionService implements TaskActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaTaskActionService.class);

	@Override
	public void saveTask(String taskId, Map<String, Object> variables) {

	}

	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {

	}

	@Override
	public void assignTask(String taskId, String userId, String reason) {

	}

	@Override
	public void claimTask(String taskId, String userId) {

	}

	@Override
	public void unclaimTask(String taskId) {

	}

	@Override
	public boolean delegateTask(String taskId, String ownerUserId, String delegateUserId, String reason) {
		return false;
	}

	@Override
	public boolean resolveDelegatedTask(String taskId, String delegateUserId, String comment) {
		return false;
	}

	@Override
	public void setTaskVariables(String taskId, Map<String, Object> variables) {

	}

	@Override
	public boolean setTaskDueDate(String taskId, long dueDate) {
		return false;
	}

	@Override
	public void setTaskPriority(String taskInstanceId, int priority) {

	}

	@Override
	public void addCandidateGroup(String taskId, String groupId) {

	}

	@Override
	public void deleteCandidateGroup(String taskId, String groupId) {

	}

}
