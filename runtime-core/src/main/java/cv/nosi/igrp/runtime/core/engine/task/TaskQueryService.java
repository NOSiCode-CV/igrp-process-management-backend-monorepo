package cv.nosi.igrp.runtime.core.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {

    Optional<TaskInfo> getTask(String taskId);

    List<TaskVariableInstance> getTaskVariables(String taskId);

    List<TaskInfo> getActiveTaskInstances(String processInstanceId);

    List<ProcessTaskInfo> getUserTaskProgress(String processInstanceId);
}