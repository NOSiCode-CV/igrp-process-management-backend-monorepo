package cv.igrp.framework.runtime.core.engine.activity.model;

/**
 * Classe para representar informações de uma atividade.
 */
public record ActivityInfo(
        String id,
        String name,
        String description,
        String processInstanceId,
        String parentId,
		String parentProcessInstanceId,
        IGRPActivityStatus status,
        IGRPActivityType type
) {
}