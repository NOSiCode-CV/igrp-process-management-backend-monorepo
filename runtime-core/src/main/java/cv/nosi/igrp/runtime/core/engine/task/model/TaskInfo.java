package cv.nosi.igrp.runtime.core.engine.task.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe para representar informações de uma tarefa.
 */
@Getter
@Setter
@ToString
public class TaskInfo {
    private String id;
    private String name;
    private String description;
    private String processInstanceId;
    private String taskDefinitionKey;
    private String assignee;
    private String owner;
    private long createdTime;
    private Long dueDate;
    private int priority;
    private String formKey;
}