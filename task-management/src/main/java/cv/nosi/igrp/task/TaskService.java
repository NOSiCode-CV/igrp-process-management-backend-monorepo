package cv.nosi.igrp.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing human tasks.
 * Provides methods for creating, assigning, completing, and querying tasks.
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param taskName The name of the task
     * @param description The description of the task
     * @param processInstanceId The ID of the process instance (optional)
     * @param variables Task variables
     * @return The ID of the newly created task
     */
    String createTask(String taskName, String description, String processInstanceId, Map<String, Object> variables);

    /**
     * Assigns a task to a user.
     *
     * @param taskId The ID of the task
     * @param userId The ID of the user
     * @return true if the task was successfully assigned, false otherwise
     */
    boolean assignTask(String taskId, String userId);

    /**
     * Claims a task for the current user.
     *
     * @param taskId The ID of the task
     * @param userId The ID of the user claiming the task
     * @return true if the task was successfully claimed, false otherwise
     */
    boolean claimTask(String taskId, String userId);

    /**
     * Releases a task back to the pool.
     *
     * @param taskId The ID of the task
     * @return true if the task was successfully released, false otherwise
     */
    boolean releaseTask(String taskId);

    /**
     * Completes a task.
     *
     * @param taskId The ID of the task
     * @param variables Output variables for the task
     * @return true if the task was successfully completed, false otherwise
     */
    boolean completeTask(String taskId, Map<String, Object> variables);

    /**
     * Delegates a task to another user.
     *
     * @param taskId The ID of the task
     * @param userId The ID of the user to delegate to
     * @return true if the task was successfully delegated, false otherwise
     */
    boolean delegateTask(String taskId, String userId);

    /**
     * Resolves a delegated task.
     *
     * @param taskId The ID of the task
     * @return true if the task was successfully resolved, false otherwise
     */
    boolean resolveTask(String taskId);

    /**
     * Gets a task by its ID.
     *
     * @param taskId The ID of the task
     * @return An Optional containing the task if found, or empty if not found
     */
    Optional<Task> getTask(String taskId);

    /**
     * Gets all tasks assigned to a user.
     *
     * @param userId The ID of the user
     * @return A list of tasks assigned to the user
     */
    List<Task> getTasksAssignedToUser(String userId);

    /**
     * Gets all tasks that a user can claim.
     *
     * @param userId The ID of the user
     * @param groupIds The IDs of the groups the user belongs to
     * @return A list of tasks that the user can claim
     */
    List<Task> getTasksClaimableByUser(String userId, List<String> groupIds);

    /**
     * Gets all tasks for a process instance.
     *
     * @param processInstanceId The ID of the process instance
     * @return A list of tasks for the process instance
     */
    List<Task> getTasksByProcessInstance(String processInstanceId);

    /**
     * Gets all tasks with a specific name.
     *
     * @param taskName The name of the task
     * @return A list of tasks with the specified name
     */
    List<Task> getTasksByName(String taskName);

    /**
     * Gets all tasks with a specific priority.
     *
     * @param priority The priority of the task
     * @return A list of tasks with the specified priority
     */
    List<Task> getTasksByPriority(int priority);

    /**
     * Gets all tasks that are due before a specific date.
     *
     * @param dueDateMillis The due date in milliseconds since epoch
     * @return A list of tasks that are due before the specified date
     */
    List<Task> getTasksDueBefore(long dueDateMillis);

    /**
     * Sets the priority of a task.
     *
     * @param taskId The ID of the task
     * @param priority The new priority
     * @return true if the priority was successfully set, false otherwise
     */
    boolean setTaskPriority(String taskId, int priority);

    /**
     * Sets the due date of a task.
     *
     * @param taskId The ID of the task
     * @param dueDateMillis The due date in milliseconds since epoch
     * @return true if the due date was successfully set, false otherwise
     */
    boolean setTaskDueDate(String taskId, long dueDateMillis);

    /**
     * Adds a comment to a task.
     *
     * @param taskId The ID of the task
     * @param userId The ID of the user adding the comment
     * @param comment The comment text
     * @return The ID of the newly created comment
     */
    String addTaskComment(String taskId, String userId, String comment);

    /**
     * Gets all comments for a task.
     *
     * @param taskId The ID of the task
     * @return A list of comments for the task
     */
    List<TaskComment> getTaskComments(String taskId);
}