package cv.igrp.framework.runtime.core.engine.process.model;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.VariableRepresentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public record IgrpProcessDefinitionRepresentation(
		String key,
		String name,
		String description,
		String version,
		String releaseId,
		String bpmnXml,
		String bpmnUrl,
		String resourceName,
		BpmnSourceType bpmnSourceType,
		boolean deployed,
		String deploymentId,
		String applicationBase,
		LocalDateTime deployedAt,
		List<VariableRepresentation> variables,
		List<TaskRepresentation> tasks,
		List<ProcessVersionRepresentation> versionHistory
) implements ProcessDefinitionRepresentation {

	public IgrpProcessDefinitionRepresentation(
			String key,
			String name,
			String description,
			String version,
			String releaseId,
			String bpmnXml,
			String bpmnUrl,
			String resourceName,
			BpmnSourceType bpmnSourceType,
			boolean deployed,
			String deploymentId,
			String applicationBase,
			LocalDateTime deployedAt,
			List<VariableRepresentation> variables,
			List<TaskRepresentation> tasks,
			List<ProcessVersionRepresentation> versionHistory
	) {
		this.key = key;
		this.name = name;
		this.description = description;
		this.version = version;
		this.releaseId = releaseId;
		this.bpmnXml = bpmnXml;
		this.bpmnUrl = bpmnUrl;
		this.resourceName = resourceName;
		this.bpmnSourceType = bpmnSourceType;
		this.deployed = deployed;
		this.deploymentId = deploymentId;
		this.applicationBase = applicationBase;
		this.deployedAt = deployedAt;
		this.variables = variables == null ? new ArrayList<>() : variables;
		this.tasks = tasks == null ? new ArrayList<>() : tasks;
		this.versionHistory = versionHistory == null ? new ArrayList<>() : versionHistory;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String key;
		private String name;
		private String description;
		private String version;
		private String releaseId;
		private String bpmnXml;
		private String bpmnUrl;
		private String resourceName;
		private BpmnSourceType bpmnSourceType;
		private boolean deployed;
		private String deploymentId;
		private String applicationBase;
		private LocalDateTime deployedAt;
		private List<VariableRepresentation> variables;
		private List<TaskRepresentation> tasks;
		private List<ProcessVersionRepresentation> versionHistory;

		public Builder key(String key) {
			this.key = key;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder version(String version) {
			this.version = version;
			return this;
		}

		public Builder releaseId(String releaseId) {
			this.releaseId = releaseId;
			return this;
		}

		public Builder bpmnXml(String bpmnXml) {
			this.bpmnXml = bpmnXml;
			return this;
		}

		public Builder bpmnUrl(String bpmnUrl) {
			this.bpmnUrl = bpmnUrl;
			return this;
		}

		public Builder resourceName(String resourceName) {
			this.resourceName = resourceName;
			return this;
		}

		public Builder bpmnSourceType(BpmnSourceType bpmnSourceType) {
			this.bpmnSourceType = bpmnSourceType;
			return this;
		}

		public Builder deployed(boolean deployed) {
			this.deployed = deployed;
			return this;
		}

		public Builder deploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
			return this;
		}

		public Builder applicationBase(String applicationBase) {
			this.applicationBase = applicationBase;
			return this;
		}

		public Builder deployedAt(LocalDateTime deployedAt) {
			this.deployedAt = deployedAt;
			return this;
		}

		public Builder variables(List<VariableRepresentation> variables) {
			this.variables = variables;
			return this;
		}

		public Builder tasks(List<TaskRepresentation> tasks) {
			this.tasks = tasks;
			return this;
		}

		public Builder versionHistory(List<ProcessVersionRepresentation> versionHistory) {
			this.versionHistory = versionHistory;
			return this;
		}

		public IgrpProcessDefinitionRepresentation build() {
			return new IgrpProcessDefinitionRepresentation(
					key,
					name,
					description,
					version,
					releaseId,
					bpmnXml,
					bpmnUrl,
					resourceName,
					bpmnSourceType,
					deployed,
					deploymentId,
					applicationBase,
					deployedAt,
					variables,
					tasks,
					versionHistory
			);
		}
	}
}
