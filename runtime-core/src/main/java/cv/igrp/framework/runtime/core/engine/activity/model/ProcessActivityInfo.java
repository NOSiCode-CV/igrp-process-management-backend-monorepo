package cv.igrp.framework.runtime.core.engine.activity.model;

import java.time.Instant;
import java.util.Set;

public record ProcessActivityInfo(
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
		Long durationMillis
) {
}