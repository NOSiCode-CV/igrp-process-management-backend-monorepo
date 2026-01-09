package cv.igrp.framework.runtime.core.engine.activity.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class ProcessTimelineEvent {

	private String activityInstanceId;
	private String activityId;
	private String activityName;
	private IGRPActivityType type;
	private String executionId;
	private String taskId;
	private String processInstanceId;
	private IGRPActivityStatus status;
	private Instant startTime;
	private Instant endTime;
	private Long duration;
	private Map<String, Object> variables;
	private String assignee;
	private String treeNumber;

	public ProcessTimelineEvent() {}

	public ProcessTimelineEvent(String activityInstanceId,
								String activityId,
								String activityName,
								IGRPActivityType type,
								String executionId,
								String taskId,
								String processInstanceId,
								IGRPActivityStatus status,
								Instant startTime,
								Instant endTime,
								Long duration,
								String assignee,
								Map<String, Object> variables,
								String treeNumber
	) {
		this.activityInstanceId = activityInstanceId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.type = type;
		this.executionId = executionId;
		this.taskId = taskId;
		this.processInstanceId = processInstanceId;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.variables = variables;
		this.assignee = assignee;
		this.treeNumber = treeNumber;
	}

	public String getActivityInstanceId() {
		return activityInstanceId;
	}

	public void setActivityInstanceId(String activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public IGRPActivityType getType() {
		return type;
	}

	public void setType(IGRPActivityType type) {
		this.type = type;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public IGRPActivityStatus getStatus() {
		return status;
	}

	public void setStatus(IGRPActivityStatus status) {
		this.status = status;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTreeNumber() {
		return treeNumber;
	}

	public void setTreeNumber(String treeNumber) {
		this.treeNumber = treeNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ProcessTimelineEvent that = (ProcessTimelineEvent) o;
		return Objects.equals(activityInstanceId, that.activityInstanceId) && Objects.equals(activityId, that.activityId) && Objects.equals(activityName, that.activityName) && type == that.type && Objects.equals(executionId, that.executionId) && Objects.equals(taskId, that.taskId) && Objects.equals(processInstanceId, that.processInstanceId) && status == that.status && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(duration, that.duration) && Objects.equals(variables, that.variables) && Objects.equals(assignee, that.assignee) && Objects.equals(treeNumber, that.treeNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(activityInstanceId, activityId, activityName, type, executionId, taskId, processInstanceId, status, startTime, endTime, duration, variables, assignee, treeNumber);
	}

	@Override
	public String toString() {
		return "ProcessTimelineEvent{" +
				"activityInstanceId='" + activityInstanceId + '\'' +
				", activityId='" + activityId + '\'' +
				", activityName='" + activityName + '\'' +
				", type=" + type +
				", executionId='" + executionId + '\'' +
				", taskId='" + taskId + '\'' +
				", processInstanceId='" + processInstanceId + '\'' +
				", status=" + status +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", duration=" + duration +
				", variables=" + variables +
				", assignee='" + assignee + '\'' +
				", treeNumber='" + treeNumber + '\'' +
				'}';
	}
}
