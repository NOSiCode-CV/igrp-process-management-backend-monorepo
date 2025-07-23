package cv.nosi.igrp.runtime.core.task;

import cv.nosi.igrp.runtime.core.task.assignment.AssignmentEvent;

import java.util.List;
import java.util.Map;

public interface TaskAssignment {

    boolean assignTask(String taskId, String userId, String reason);

    boolean reassignTask(String taskId, String fromUserId, String toUserId, String reason);

    List<AssignmentEvent> getAssignmentHistory(String taskId);
}