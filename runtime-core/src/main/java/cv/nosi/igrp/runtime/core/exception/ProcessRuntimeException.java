package cv.nosi.igrp.runtime.core.exception;

/**
 * Exceção base para erros de runtime no sistema de processos.
 * <p>
 * Esta exceção é a classe base para todas as exceções de runtime
 * específicas do sistema de processos.
 */
public class ProcessRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String processInstanceId;
    private String processDefinitionId;
    private String activityId;
    private String errorCode;
    
    /**
     * Construtor padrão.
     */
    public ProcessRuntimeException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message mensagem de erro
     */
    public ProcessRuntimeException(String message) {
        super(message);
    }
    
    /**
     * Construtor com mensagem de erro e causa.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     */
    public ProcessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor com causa.
     * 
     * @param cause causa da exceção
     */
    public ProcessRuntimeException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Construtor com mensagem de erro e ID da instância de processo.
     * 
     * @param message mensagem de erro
     * @param processInstanceId ID da instância de processo
     */
    public ProcessRuntimeException(String message, String processInstanceId) {
        super(message);
        this.processInstanceId = processInstanceId;
    }
    
    /**
     * Construtor com mensagem de erro, causa e ID da instância de processo.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param processInstanceId ID da instância de processo
     */
    public ProcessRuntimeException(String message, Throwable cause, String processInstanceId) {
        super(message, cause);
        this.processInstanceId = processInstanceId;
    }
    
    /**
     * Construtor completo.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param processInstanceId ID da instância de processo
     * @param processDefinitionId ID da definição do processo
     * @param activityId ID da atividade
     * @param errorCode código de erro
     */
    public ProcessRuntimeException(String message, Throwable cause, String processInstanceId,
                                  String processDefinitionId, String activityId, String errorCode) {
        super(message, cause);
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.activityId = activityId;
        this.errorCode = errorCode;
    }
    
    /**
     * Obtém o ID da instância de processo.
     * 
     * @return ID da instância de processo
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    /**
     * Define o ID da instância de processo.
     * 
     * @param processInstanceId ID da instância de processo
     */
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    /**
     * Obtém o ID da definição do processo.
     * 
     * @return ID da definição do processo
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }
    
    /**
     * Define o ID da definição do processo.
     * 
     * @param processDefinitionId ID da definição do processo
     */
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    /**
     * Obtém o ID da atividade.
     * 
     * @return ID da atividade
     */
    public String getActivityId() {
        return activityId;
    }
    
    /**
     * Define o ID da atividade.
     * 
     * @param activityId ID da atividade
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    
    /**
     * Obtém o código de erro.
     * 
     * @return código de erro
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Define o código de erro.
     * 
     * @param errorCode código de erro
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        
        if (processInstanceId != null) {
            sb.append(", processInstanceId=").append(processInstanceId);
        }
        
        if (processDefinitionId != null) {
            sb.append(", processDefinitionId=").append(processDefinitionId);
        }
        
        if (activityId != null) {
            sb.append(", activityId=").append(activityId);
        }
        
        if (errorCode != null) {
            sb.append(", errorCode=").append(errorCode);
        }
        
        return sb.toString();
    }
}