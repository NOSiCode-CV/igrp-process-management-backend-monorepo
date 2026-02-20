package cv.igrp.framework.process.runtime.core.engine.activity;


import cv.igrp.framework.process.runtime.core.engine.activity.model.ActivityInfo;
import cv.igrp.framework.process.runtime.core.engine.activity.model.ActivityVariableInstance;
import cv.igrp.framework.process.runtime.core.engine.activity.model.ProcessTimelineEvent;

import java.util.List;
import java.util.Optional;

public interface ActivityQueryService {

    Optional<ActivityInfo> getActivity(String activityId);

    List<ActivityVariableInstance> getActivityVariables(String activityId);

	List<ActivityVariableInstance> getRuntimeActivityVariables(String activityId);

	List<ActivityVariableInstance> getHistoricActivityVariables(String activityId);

    List<ActivityInfo> getActiveActivityInstances(String processInstanceId);

    List<ProcessTimelineEvent> getActivityTimelineEvents(String processInstanceId);

}