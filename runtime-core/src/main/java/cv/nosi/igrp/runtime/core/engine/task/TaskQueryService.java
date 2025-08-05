package cv.nosi.igrp.runtime.core.engine.task;

import cv.nosi.igrp.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskVariableInstance;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {

    Optional<TaskInfo> getTask(String taskId);

    List<TaskInfo> getTasks(TaskFilter filter);

    List<TaskVariableInstance> getTaskVariables(String taskId);

    List<TaskInfo> getActiveTaskInstances(String processInstanceId);

    List<ProcessTaskInfo> getAllTasks(String processInstanceId);
}