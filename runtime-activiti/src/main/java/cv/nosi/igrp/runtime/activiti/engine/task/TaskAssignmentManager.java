package cv.nosi.igrp.runtime.activiti.engine.task;

import cv.nosi.igrp.runtime.core.task.TaskAssignment;
import cv.nosi.igrp.runtime.core.task.assignment.AssignmentEvent;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentManager implements TaskAssignment {

    private final TaskService taskService;
    private final HistoryService historyService;

    public TaskAssignmentManager(TaskService taskService, HistoryService historyService) {
        this.taskService = taskService;
        this.historyService = historyService;
    }

    @Override
    public boolean assignTask(String taskId, String userId, String reason) {
        var task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            taskService.setAssignee(taskId, userId);
            taskService.addComment(taskId, task.getProcessInstanceId(), "Assigned to " + userId + ": " + reason);
            return true;
        }
        return false;
    }

    @Override
    public boolean reassignTask(String taskId, String fromUserId, String toUserId, String reason) {
        var task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null && fromUserId.equals(task.getAssignee())) {
            taskService.setAssignee(taskId, toUserId);
            taskService.addComment(taskId, task.getProcessInstanceId(), "Reassigned from " + fromUserId + " to " + toUserId + ": " + reason);
            return true;
        }
        return false;
    }

    @Override
    public List<AssignmentEvent> getAssignmentHistory(String taskId) {

        var history = historyService.getHistoricIdentityLinksForTask(taskId);

        history.forEach(link -> {

        });

        return List.of();
    }
}
