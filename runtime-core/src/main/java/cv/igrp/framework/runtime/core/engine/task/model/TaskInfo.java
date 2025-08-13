package cv.igrp.framework.runtime.core.engine.task.model;

import java.util.Date;

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
        Date createdTime,
        Date dueDate,
        int priority,
        String formKey
) {
}