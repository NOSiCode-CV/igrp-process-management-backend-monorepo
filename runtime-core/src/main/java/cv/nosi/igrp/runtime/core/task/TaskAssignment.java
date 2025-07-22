package cv.nosi.igrp.runtime.core.task;

import java.util.List;
import java.util.Map;
import cv.nosi.igrp.runtime.core.task.assignment.AssignmentRule;
import cv.nosi.igrp.runtime.core.task.assignment.AssignmentEvent;

/**
 * Interface para gerenciamento de atribuições de tarefas.
 * <p>
 * Define operações para atribuir, reatribuir e gerenciar candidatos para tarefas,
 * incluindo suporte para atribuição individual, em grupo e baseada em regras.
 */
public interface TaskAssignment {
    
    /**
     * Atribui uma tarefa a um usuário específico.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @param reason motivo da atribuição (opcional)
     * @return true se a atribuição for bem-sucedida
     */
    boolean assignTask(String taskId, String userId, String reason);
    
    /**
     * Reatribui uma tarefa de um usuário para outro.
     * 
     * @param taskId ID da tarefa
     * @param fromUserId ID do usuário atual
     * @param toUserId ID do novo usuário
     * @param reason motivo da reatribuição
     * @return true se a reatribuição for bem-sucedida
     */
    boolean reassignTask(String taskId, String fromUserId, String toUserId, String reason);
    
    /**
     * Adiciona um usuário como candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se a adição for bem-sucedida
     */
    boolean addCandidateUser(String taskId, String userId);
    
    /**
     * Remove um usuário da lista de candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se a remoção for bem-sucedida
     */
    boolean removeCandidateUser(String taskId, String userId);
    
    /**
     * Adiciona um grupo como candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param groupId ID do grupo
     * @return true se a adição for bem-sucedida
     */
    boolean addCandidateGroup(String taskId, String groupId);
    
    /**
     * Remove um grupo da lista de candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param groupId ID do grupo
     * @return true se a remoção for bem-sucedida
     */
    boolean removeCandidateGroup(String taskId, String groupId);
    
    /**
     * Obtém a lista de usuários candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de IDs de usuários candidatos
     */
    List<String> getCandidateUsers(String taskId);
    
    /**
     * Obtém a lista de grupos candidatos para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de IDs de grupos candidatos
     */
    List<String> getCandidateGroups(String taskId);
    
    /**
     * Verifica se um usuário é candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se o usuário for candidato
     */
    boolean isUserCandidate(String taskId, String userId);
    
    /**
     * Verifica se um usuário pertence a algum grupo candidato para uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @param userId ID do usuário
     * @return true se o usuário pertencer a algum grupo candidato
     */
    boolean isUserInCandidateGroup(String taskId, String userId);
    
    /**
     * Configura regras de atribuição automática para tarefas.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     * @param rules regras de atribuição
     * @return true se a configuração for bem-sucedida
     */
    boolean setAssignmentRules(String taskDefinitionKey, List<AssignmentRule> rules);
    
    /**
     * Obtém regras de atribuição automática para uma tarefa.
     * 
     * @param taskDefinitionKey chave de definição da tarefa
     * @return lista de regras de atribuição
     */
    List<AssignmentRule> getAssignmentRules(String taskDefinitionKey);
    
    /**
     * Executa atribuição automática para uma tarefa com base nas regras configuradas.
     * 
     * @param taskId ID da tarefa
     * @param variables variáveis de contexto para avaliação das regras
     * @return ID do usuário atribuído ou null se nenhuma regra for aplicável
     */
    String autoAssign(String taskId, Map<String, Object> variables);
    
    /**
     * Obtém o histórico de atribuições de uma tarefa.
     * 
     * @param taskId ID da tarefa
     * @return lista de eventos de atribuição
     */
    List<AssignmentEvent> getAssignmentHistory(String taskId);
}