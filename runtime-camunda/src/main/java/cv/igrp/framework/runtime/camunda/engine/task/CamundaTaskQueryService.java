package cv.igrp.framework.runtime.camunda.engine.task;

import cv.igrp.framework.runtime.core.engine.process.model.TaskFilter;
import cv.igrp.framework.runtime.core.engine.task.TaskQueryService;

import cv.igrp.framework.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.igrp.framework.runtime.core.engine.task.model.TaskInfo;
import cv.igrp.framework.runtime.core.engine.task.model.TaskVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CamundaTaskQueryService implements TaskQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaTaskQueryService.class);


	@Override
	public Optional<TaskInfo> getTask(String taskId) {
		return Optional.empty();
	}

	@Override
	public List<TaskVariableInstance> getTaskVariables(String taskId) {
		return List.of();
	}

	@Override
	public List<TaskVariableInstance> getRuntimeTaskVariables(String taskId) {
		return List.of();
	}

	@Override
	public List<TaskVariableInstance> getHistoricTaskVariables(String taskId) {
		return List.of();
	}

	@Override
	public List<TaskInfo> getActiveTaskInstances(String processInstanceId) {
		return List.of();
	}

	@Override
	public List<ProcessTaskInfo> getUserTaskProgress(String processInstanceId) {
		return List.of();
	}

	@Override
	public List<TaskInfo> listTaskInstances(TaskFilter filter) {
		return List.of();
	}

}
