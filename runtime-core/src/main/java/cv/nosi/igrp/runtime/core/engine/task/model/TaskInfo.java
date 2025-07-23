package cv.nosi.igrp.runtime.core.engine.task.model;

/**
 * Classe para representar informações de uma tarefa.
 */
public record TaskInfo(
        String id,
        String name,
        String description,
        String processInstanceId,
        String taskDefinitionKey,
        String assignee,
        String owner,
        long createdTime,
        Long dueDate,
        int priority,
        String formKey
) {
}
