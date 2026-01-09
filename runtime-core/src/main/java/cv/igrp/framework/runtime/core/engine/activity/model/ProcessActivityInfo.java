package cv.igrp.framework.runtime.core.engine.activity.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessActivityInfo {

	private String activityId;
	private String activityKey;
	private String activityName;
	private IGRPActivityStatus status;
	private IGRPActivityType type;
	private String processInstanceId;
	private String assignee;
	private Set<String> candidateUsers;
	private Set<String> candidateGroups;
	private Instant startTime;
	private Instant endTime;
	private Long durationMillis;
	private Map<String, Object> variables;

	public ProcessActivityInfo(String activityId,
							   String activityKey,
							   String activityName,
							   IGRPActivityStatus status,
							   IGRPActivityType type,
							   String processInstanceId,
							   String assignee,
							   Set<String> candidateUsers,
							   Set<String> candidateGroups,
							   Instant startTime,
							   Instant endTime,
							   Long durationMillis) {
		this.activityId = activityId;
		this.activityKey = activityKey;
		this.activityName = activityName;
		this.status = status;
		this.type = type;
		this.processInstanceId = processInstanceId;
		this.assignee = assignee;
		this.candidateUsers = candidateUsers;
		this.candidateGroups = candidateGroups;
		this.startTime = startTime;
		this.endTime = endTime;
		this.durationMillis = durationMillis;
		this.variables = new HashMap<>();
	}

	public String getActivityId() {
		return activityId;
	}

	public String getActivityKey() {
		return activityKey;
	}

	public String getActivityName() {
		return activityName;
	}

	public IGRPActivityStatus getStatus() {
		return status;
	}

	public IGRPActivityType getType() {
		return type;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getAssignee() {
		return assignee;
	}

	public Set<String> getCandidateUsers() {
		return candidateUsers;
	}

	public Set<String> getCandidateGroups() {
		return candidateGroups;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public Long getDurationMillis() {
		return durationMillis;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void addVariable(String key, Object value) {
		this.variables.put(key, value);
	}

	public void addVariable(Map<String, Object> variables) {
		this.variables.putAll(variables);
	}

}
