package cv.igrp.framework.runtime.core.engine.process.model;


import java.util.ArrayList;
import java.util.List;

public class TaskFilter {

	private String id;
	private String name;
	private String description;
	private String processInstanceId;
	private String taskDefinitionKey;
	private List<VariablesExpression> variablesExpressions = new ArrayList<>();

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public List<VariablesExpression> getVariablesExpressions() {
		return variablesExpressions;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public void setVariablesExpressions(List<VariablesExpression> variablesExpressions) {
		this.variablesExpressions = variablesExpressions;
	}

	@Override
	public String toString() {
		return "TaskFilter{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", processInstanceId='" + processInstanceId + '\'' +
				", taskDefinitionKey='" + taskDefinitionKey + '\'' +
				", variablesExpressions=" + variablesExpressions +
				'}';
	}

}
