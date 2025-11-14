package cv.igrp.framework.runtime.camunda.engine.activity;

import cv.igrp.framework.runtime.core.engine.activity.ActivityQueryService;
import cv.igrp.framework.runtime.core.engine.activity.model.ActivityInfo;
import cv.igrp.framework.runtime.core.engine.activity.model.ActivityVariableInstance;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;

import java.util.List;
import java.util.Optional;

public class CamundaActivityQueryService implements ActivityQueryService {

    @Override
    public Optional<ActivityInfo> getActivity(String activityId) {
        return Optional.empty();
    }

    @Override
    public List<ActivityVariableInstance> getActivityVariables(String activityId) {
        return List.of();
    }

    @Override
    public List<ActivityVariableInstance> getRuntimeActivityVariables(String activityId) {
        return List.of();
    }

    @Override
    public List<ActivityVariableInstance> getHistoricActivityVariables(String activityId) {
        return List.of();
    }

    @Override
    public List<ActivityInfo> getActiveActivityInstances(String processInstanceId) {
        return List.of();
    }

    @Override
    public List<ProcessActivityInfo> getActivityProgress(String processInstanceId) {
        return List.of();
    }
}
