package cv.igrp.framework.process.runtime.core.engine.task;


import cv.igrp.framework.process.runtime.core.engine.process.model.TaskFilter;
import cv.igrp.framework.process.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.igrp.framework.process.runtime.core.engine.task.model.TaskInfo;
import cv.igrp.framework.process.runtime.core.engine.task.model.TaskVariableInstance;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {

    Optional<TaskInfo> getTask(String taskId);

    List<TaskVariableInstance> getTaskVariables(String taskId);

	List<TaskVariableInstance> getRuntimeTaskVariables(String taskId);

	List<TaskVariableInstance> getHistoricTaskVariables(String taskId);

    List<TaskInfo> getActiveTaskInstances(String processInstanceId);

    List<ProcessTaskInfo> getUserTaskProgress(String processInstanceId);

	List<TaskInfo> listTaskInstances(TaskFilter filter);

}