package cv.nosi.igrp.runtime.core.exception;

/**
 * Exceção para erros em operações de deployment de processos.
 * <p>
 * Esta exceção é lançada quando ocorre um erro durante operações
 * de deployment, undeployment ou atualização de definições de processos.
 */
public class DeploymentException extends ProcessRuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String deploymentId;
    private String resourceName;
    private String deploymentOperation;
    private String validationErrors;
    
    /**
     * Construtor padrão.
     */
    public DeploymentException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message mensagem de erro
     */
    public DeploymentException(String message) {
        super(message);
    }
    
    /**
     * Construtor com mensagem de erro e causa.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     */
    public DeploymentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor com mensagem de erro e ID do deployment.
     * 
     * @param message mensagem de erro
     * @param deploymentId ID do deployment
     */
    public DeploymentException(String message, String deploymentId) {
        super(message);
        this.deploymentId = deploymentId;
    }
    
    /**
     * Construtor com mensagem de erro, causa e ID do deployment.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param deploymentId ID do deployment
     */
    public DeploymentException(String message, Throwable cause, String deploymentId) {
        super(message, cause);
        this.deploymentId = deploymentId;
    }
    
    /**
     * Construtor completo.
     * 
     * @param message mensagem de erro
     * @param cause causa da exceção
     * @param deploymentId ID do deployment
     * @param resourceName nome do recurso
     * @param deploymentOperation operação de deployment
     * @param validationErrors erros de validação
     */
    public DeploymentException(String message, Throwable cause, String deploymentId,
                              String resourceName, String deploymentOperation, String validationErrors) {
        super(message, cause);
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.deploymentOperation = deploymentOperation;
        this.validationErrors = validationErrors;
    }
    
    /**
     * Obtém o ID do deployment.
     * 
     * @return ID do deployment
     */
    public String getDeploymentId() {
        return deploymentId;
    }
    
    /**
     * Define o ID do deployment.
     * 
     * @param deploymentId ID do deployment
     */
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    /**
     * Obtém o nome do recurso.
     * 
     * @return nome do recurso
     */
    public String getResourceName() {
        return resourceName;
    }
    
    /**
     * Define o nome do recurso.
     * 
     * @param resourceName nome do recurso
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    /**
     * Obtém a operação de deployment.
     * 
     * @return operação de deployment
     */
    public String getDeploymentOperation() {
        return deploymentOperation;
    }
    
    /**
     * Define a operação de deployment.
     * 
     * @param deploymentOperation operação de deployment
     */
    public void setDeploymentOperation(String deploymentOperation) {
        this.deploymentOperation = deploymentOperation;
    }
    
    /**
     * Obtém os erros de validação.
     * 
     * @return erros de validação
     */
    public String getValidationErrors() {
        return validationErrors;
    }
    
    /**
     * Define os erros de validação.
     * 
     * @param validationErrors erros de validação
     */
    public void setValidationErrors(String validationErrors) {
        this.validationErrors = validationErrors;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        
        if (deploymentId != null) {
            sb.append(", deploymentId=").append(deploymentId);
        }
        
        if (resourceName != null) {
            sb.append(", resourceName=").append(resourceName);
        }
        
        if (deploymentOperation != null) {
            sb.append(", deploymentOperation=").append(deploymentOperation);
        }
        
        if (validationErrors != null) {
            sb.append(", validationErrors=").append(validationErrors);
        }
        
        return sb.toString();
    }
    
    /**
     * Cria uma exceção para falha de deployment.
     * 
     * @param resourceName nome do recurso
     * @param cause causa da exceção
     * @return exceção de deployment
     */
    public static DeploymentException createDeployFailureException(String resourceName, Throwable cause) {
        return new DeploymentException("Failed to deploy process definition from resource " + resourceName, 
                                      cause, null, resourceName, "DEPLOY", null);
    }
    
    /**
     * Cria uma exceção para falha de validação.
     * 
     * @param resourceName nome do recurso
     * @param validationErrors erros de validação
     * @return exceção de deployment
     */
    public static DeploymentException createValidationException(String resourceName, String validationErrors) {
        DeploymentException exception = new DeploymentException("Process definition validation failed for resource " + resourceName);
        exception.setResourceName(resourceName);
        exception.setDeploymentOperation("VALIDATE");
        exception.setValidationErrors(validationErrors);
        return exception;
    }
    
    /**
     * Cria uma exceção para falha de undeployment.
     * 
     * @param deploymentId ID do deployment
     * @param cause causa da exceção
     * @return exceção de deployment
     */
    public static DeploymentException createUndeployFailureException(String deploymentId, Throwable cause) {
        return new DeploymentException("Failed to undeploy process definition with deployment ID " + deploymentId, 
                                      cause, deploymentId, null, "UNDEPLOY", null);
    }
    
    /**
     * Cria uma exceção para falha de atualização.
     * 
     * @param deploymentId ID do deployment
     * @param resourceName nome do recurso
     * @param cause causa da exceção
     * @return exceção de deployment
     */
    public static DeploymentException createUpdateFailureException(String deploymentId, String resourceName, Throwable cause) {
        return new DeploymentException("Failed to update process definition with deployment ID " + deploymentId, 
                                      cause, deploymentId, resourceName, "UPDATE", null);
    }
    
    /**
     * Cria uma exceção para recurso não encontrado.
     * 
     * @param resourceName nome do recurso
     * @return exceção de deployment
     */
    public static DeploymentException createResourceNotFoundException(String resourceName) {
        DeploymentException exception = new DeploymentException("Process definition resource not found: " + resourceName);
        exception.setResourceName(resourceName);
        exception.setDeploymentOperation("FIND");
        return exception;
    }
    
    /**
     * Cria uma exceção para deployment não encontrado.
     * 
     * @param deploymentId ID do deployment
     * @return exceção de deployment
     */
    public static DeploymentException createDeploymentNotFoundException(String deploymentId) {
        DeploymentException exception = new DeploymentException("Deployment not found with ID: " + deploymentId);
        exception.setDeploymentId(deploymentId);
        exception.setDeploymentOperation("FIND");
        return exception;
    }
}