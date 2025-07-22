package cv.nosi.igrp.runtime.core.task;

import cv.nosi.igrp.runtime.core.task.model.TaskFilter;
import cv.nosi.igrp.runtime.core.task.model.TaskInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface para gerenciamento de tarefas de processos.
 * <p>
 * Define operações para criar, atualizar, consultar e completar tarefas
 * associadas a instâncias de processos.
 */
public interface TaskManager {

    /**
     * Cria uma nova tarefa.
     *
     * @param processInstanceId ID da instância de processo associada (opcional)
     * @param taskDefinitionKey chave de definição da tarefa
     * @param taskName          nome da tarefa
     * @param assignee          responsável pela tarefa (opcional)
     * @param variables         variáveis da tarefa
     * @return ID da tarefa criada
     */
    String createTask(String processInstanceId, String taskDefinitionKey,String taskName, String assignee, Map<String, Object> variables);

    /**
     * Obtém uma tarefa pelo seu ID.
     *
     * @param taskId ID da tarefa
     * @return a tarefa encontrada ou Optional vazio se não existir
     */
    Optional<TaskInfo> getTask(String taskId);

    /**
     * Lista tarefas com base em critérios de filtragem.
     *
     * @param filter critérios de filtragem
     * @return lista de tarefas que correspondem aos critérios
     */
    List<TaskInfo> listTasks(TaskFilter filter);

    /**
     * Atribui uma tarefa a um usuário.
     *
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     */
    void assignTask(String taskId, String userId);

    /**
     * Completa uma tarefa.
     *
     * @param taskId    ID da tarefa
     * @param variables variáveis a serem atualizadas ao completar a tarefa
     * @param userId    ID do usuário que está completando a tarefa
     */
    void completeTask(String taskId, Map<String, Object> variables, String userId);

    /**
     * Atualiza variáveis de uma tarefa.
     *
     * @param taskId    ID da tarefa
     * @param variables variáveis a serem atualizadas
     */
    void setTaskVariables(String taskId, Map<String, Object> variables);

    /**
     * Obtém variáveis de uma tarefa.
     *
     * @param taskId ID da tarefa
     * @return mapa com as variáveis da tarefa
     */
    Map<String, Object> getTaskVariables(String taskId);

}