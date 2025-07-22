package cv.nosi.igrp.runtime.core.instance;

import java.util.List;
import java.util.Map;

/**
 * Interface para coleta e análise de métricas de instâncias de processos.
 * <p>
 * Define operações para coletar, agregar e analisar métricas de desempenho
 * e estatísticas relacionadas a instâncias de processos.
 */
public interface InstanceMetrics {
    
    /**
     * Obtém métricas de tempo de execução para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de tempo de execução
     */
    ExecutionTimeMetrics getExecutionTimeMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de volume de instâncias para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de volume de instâncias
     */
    VolumeMetrics getVolumeMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de atividade para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de atividade
     */
    ActivityMetrics getActivityMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de tarefas para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de tarefas
     */
    TaskMetrics getTaskMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de SLA para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de SLA
     */
    SlaMetrics getSlaMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de erro para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de erro
     */
    ErrorMetrics getErrorMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de usuário para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de usuário
     */
    UserMetrics getUserMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas de caminho de execução para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param timeRange período de análise
     * @return métricas de caminho de execução
     */
    PathMetrics getPathMetrics(String processDefinitionKey, TimeRange timeRange);
    
    /**
     * Obtém métricas personalizadas para um tipo de processo.
     * 
     * @param processDefinitionKey chave da definição do processo
     * @param metricDefinitions definições das métricas personalizadas
     * @param timeRange período de análise
     * @return métricas personalizadas
     */
    Map<String, Object> getCustomMetrics(String processDefinitionKey, 
                                        List<CustomMetricDefinition> metricDefinitions,
                                        TimeRange timeRange);
    
    /**
     * Exporta métricas para um formato específico.
     * 
     * @param metrics métricas a serem exportadas
     * @param format formato de exportação (CSV, JSON, XML)
     * @return dados exportados no formato especificado
     */
    byte[] exportMetrics(Map<String, Object> metrics, String format);
    
    /**
     * Agenda a geração periódica de relatórios de métricas.
     * 
     * @param reportDefinition definição do relatório
     * @param schedule programação de execução
     * @return ID do agendamento criado
     */
    String scheduleMetricsReport(ReportDefinition reportDefinition, ReportSchedule schedule);
    
    /**
     * Classe interna para representar um intervalo de tempo.
     */
    class TimeRange {
        private long startTime;
        private long endTime;
        private String timeUnit; // DAY, HOUR, MINUTE
        
        public TimeRange(long startTime, long endTime, String timeUnit) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.timeUnit = timeUnit;
        }
        
        // Getters
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public String getTimeUnit() { return timeUnit; }
    }
    
    /**
     * Classe interna para representar métricas de tempo de execução.
     */
    class ExecutionTimeMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private double averageExecutionTime;
        private double minExecutionTime;
        private double maxExecutionTime;
        private double medianExecutionTime;
        private double percentile90ExecutionTime;
        private double percentile95ExecutionTime;
        private Map<String, Double> executionTimeDistribution;
        private Map<String, Double> executionTimeByActivity;
        
        public ExecutionTimeMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public double getAverageExecutionTime() { return averageExecutionTime; }
        public double getMinExecutionTime() { return minExecutionTime; }
        public double getMaxExecutionTime() { return maxExecutionTime; }
        public double getMedianExecutionTime() { return medianExecutionTime; }
        public double getPercentile90ExecutionTime() { return percentile90ExecutionTime; }
        public double getPercentile95ExecutionTime() { return percentile95ExecutionTime; }
        public Map<String, Double> getExecutionTimeDistribution() { return executionTimeDistribution; }
        public Map<String, Double> getExecutionTimeByActivity() { return executionTimeByActivity; }
        
        // Setters
        public void setAverageExecutionTime(double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
        public void setMinExecutionTime(double minExecutionTime) { this.minExecutionTime = minExecutionTime; }
        public void setMaxExecutionTime(double maxExecutionTime) { this.maxExecutionTime = maxExecutionTime; }
        public void setMedianExecutionTime(double medianExecutionTime) { this.medianExecutionTime = medianExecutionTime; }
        public void setPercentile90ExecutionTime(double percentile90ExecutionTime) { this.percentile90ExecutionTime = percentile90ExecutionTime; }
        public void setPercentile95ExecutionTime(double percentile95ExecutionTime) { this.percentile95ExecutionTime = percentile95ExecutionTime; }
        public void setExecutionTimeDistribution(Map<String, Double> executionTimeDistribution) { this.executionTimeDistribution = executionTimeDistribution; }
        public void setExecutionTimeByActivity(Map<String, Double> executionTimeByActivity) { this.executionTimeByActivity = executionTimeByActivity; }
    }
    
    /**
     * Classe interna para representar métricas de volume de instâncias.
     */
    class VolumeMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private int totalInstances;
        private int activeInstances;
        private int completedInstances;
        private int terminatedInstances;
        private int suspendedInstances;
        private Map<String, Integer> instanceCountByTime;
        private Map<String, Integer> instanceCountByStatus;
        private Map<String, Integer> instanceCountByBusinessKey;
        
        public VolumeMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public int getTotalInstances() { return totalInstances; }
        public int getActiveInstances() { return activeInstances; }
        public int getCompletedInstances() { return completedInstances; }
        public int getTerminatedInstances() { return terminatedInstances; }
        public int getSuspendedInstances() { return suspendedInstances; }
        public Map<String, Integer> getInstanceCountByTime() { return instanceCountByTime; }
        public Map<String, Integer> getInstanceCountByStatus() { return instanceCountByStatus; }
        public Map<String, Integer> getInstanceCountByBusinessKey() { return instanceCountByBusinessKey; }
        
        // Setters
        public void setTotalInstances(int totalInstances) { this.totalInstances = totalInstances; }
        public void setActiveInstances(int activeInstances) { this.activeInstances = activeInstances; }
        public void setCompletedInstances(int completedInstances) { this.completedInstances = completedInstances; }
        public void setTerminatedInstances(int terminatedInstances) { this.terminatedInstances = terminatedInstances; }
        public void setSuspendedInstances(int suspendedInstances) { this.suspendedInstances = suspendedInstances; }
        public void setInstanceCountByTime(Map<String, Integer> instanceCountByTime) { this.instanceCountByTime = instanceCountByTime; }
        public void setInstanceCountByStatus(Map<String, Integer> instanceCountByStatus) { this.instanceCountByStatus = instanceCountByStatus; }
        public void setInstanceCountByBusinessKey(Map<String, Integer> instanceCountByBusinessKey) { this.instanceCountByBusinessKey = instanceCountByBusinessKey; }
    }
    
    /**
     * Classe interna para representar métricas de atividade.
     */
    class ActivityMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private Map<String, Integer> activityExecutionCount;
        private Map<String, Double> activityAverageExecutionTime;
        private Map<String, Double> activityMaxExecutionTime;
        private Map<String, List<String>> activityExecutionPath;
        private Map<String, Map<String, Integer>> activityTransitionCount;
        
        public ActivityMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public Map<String, Integer> getActivityExecutionCount() { return activityExecutionCount; }
        public Map<String, Double> getActivityAverageExecutionTime() { return activityAverageExecutionTime; }
        public Map<String, Double> getActivityMaxExecutionTime() { return activityMaxExecutionTime; }
        public Map<String, List<String>> getActivityExecutionPath() { return activityExecutionPath; }
        public Map<String, Map<String, Integer>> getActivityTransitionCount() { return activityTransitionCount; }
        
        // Setters
        public void setActivityExecutionCount(Map<String, Integer> activityExecutionCount) { this.activityExecutionCount = activityExecutionCount; }
        public void setActivityAverageExecutionTime(Map<String, Double> activityAverageExecutionTime) { this.activityAverageExecutionTime = activityAverageExecutionTime; }
        public void setActivityMaxExecutionTime(Map<String, Double> activityMaxExecutionTime) { this.activityMaxExecutionTime = activityMaxExecutionTime; }
        public void setActivityExecutionPath(Map<String, List<String>> activityExecutionPath) { this.activityExecutionPath = activityExecutionPath; }
        public void setActivityTransitionCount(Map<String, Map<String, Integer>> activityTransitionCount) { this.activityTransitionCount = activityTransitionCount; }
    }
    
    /**
     * Classe interna para representar métricas de tarefas.
     */
    class TaskMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private int totalTasks;
        private int completedTasks;
        private int activeTasks;
        private double averageTaskCompletionTime;
        private Map<String, Integer> taskCountByType;
        private Map<String, Integer> taskCountByAssignee;
        private Map<String, Double> taskAverageCompletionTimeByType;
        private Map<String, Integer> taskOverdueCount;
        
        public TaskMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public int getTotalTasks() { return totalTasks; }
        public int getCompletedTasks() { return completedTasks; }
        public int getActiveTasks() { return activeTasks; }
        public double getAverageTaskCompletionTime() { return averageTaskCompletionTime; }
        public Map<String, Integer> getTaskCountByType() { return taskCountByType; }
        public Map<String, Integer> getTaskCountByAssignee() { return taskCountByAssignee; }
        public Map<String, Double> getTaskAverageCompletionTimeByType() { return taskAverageCompletionTimeByType; }
        public Map<String, Integer> getTaskOverdueCount() { return taskOverdueCount; }
        
        // Setters
        public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }
        public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }
        public void setActiveTasks(int activeTasks) { this.activeTasks = activeTasks; }
        public void setAverageTaskCompletionTime(double averageTaskCompletionTime) { this.averageTaskCompletionTime = averageTaskCompletionTime; }
        public void setTaskCountByType(Map<String, Integer> taskCountByType) { this.taskCountByType = taskCountByType; }
        public void setTaskCountByAssignee(Map<String, Integer> taskCountByAssignee) { this.taskCountByAssignee = taskCountByAssignee; }
        public void setTaskAverageCompletionTimeByType(Map<String, Double> taskAverageCompletionTimeByType) { this.taskAverageCompletionTimeByType = taskAverageCompletionTimeByType; }
        public void setTaskOverdueCount(Map<String, Integer> taskOverdueCount) { this.taskOverdueCount = taskOverdueCount; }
    }
    
    /**
     * Classe interna para representar métricas de SLA.
     */
    class SlaMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private int totalSlas;
        private int metSlas;
        private int violatedSlas;
        private double slaComplianceRate;
        private Map<String, Integer> slaViolationsByType;
        private Map<String, Double> averageViolationTimeByType;
        private Map<String, Integer> slaViolationsByTime;
        
        public SlaMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public int getTotalSlas() { return totalSlas; }
        public int getMetSlas() { return metSlas; }
        public int getViolatedSlas() { return violatedSlas; }
        public double getSlaComplianceRate() { return slaComplianceRate; }
        public Map<String, Integer> getSlaViolationsByType() { return slaViolationsByType; }
        public Map<String, Double> getAverageViolationTimeByType() { return averageViolationTimeByType; }
        public Map<String, Integer> getSlaViolationsByTime() { return slaViolationsByTime; }
        
        // Setters
        public void setTotalSlas(int totalSlas) { this.totalSlas = totalSlas; }
        public void setMetSlas(int metSlas) { this.metSlas = metSlas; }
        public void setViolatedSlas(int violatedSlas) { this.violatedSlas = violatedSlas; }
        public void setSlaComplianceRate(double slaComplianceRate) { this.slaComplianceRate = slaComplianceRate; }
        public void setSlaViolationsByType(Map<String, Integer> slaViolationsByType) { this.slaViolationsByType = slaViolationsByType; }
        public void setAverageViolationTimeByType(Map<String, Double> averageViolationTimeByType) { this.averageViolationTimeByType = averageViolationTimeByType; }
        public void setSlaViolationsByTime(Map<String, Integer> slaViolationsByTime) { this.slaViolationsByTime = slaViolationsByTime; }
    }
    
    /**
     * Classe interna para representar métricas de erro.
     */
    class ErrorMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private int totalErrors;
        private Map<String, Integer> errorCountByType;
        private Map<String, Integer> errorCountByActivity;
        private Map<String, Integer> errorCountByTime;
        private List<ErrorInfo> mostFrequentErrors;
        
        public ErrorMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public int getTotalErrors() { return totalErrors; }
        public Map<String, Integer> getErrorCountByType() { return errorCountByType; }
        public Map<String, Integer> getErrorCountByActivity() { return errorCountByActivity; }
        public Map<String, Integer> getErrorCountByTime() { return errorCountByTime; }
        public List<ErrorInfo> getMostFrequentErrors() { return mostFrequentErrors; }
        
        // Setters
        public void setTotalErrors(int totalErrors) { this.totalErrors = totalErrors; }
        public void setErrorCountByType(Map<String, Integer> errorCountByType) { this.errorCountByType = errorCountByType; }
        public void setErrorCountByActivity(Map<String, Integer> errorCountByActivity) { this.errorCountByActivity = errorCountByActivity; }
        public void setErrorCountByTime(Map<String, Integer> errorCountByTime) { this.errorCountByTime = errorCountByTime; }
        public void setMostFrequentErrors(List<ErrorInfo> mostFrequentErrors) { this.mostFrequentErrors = mostFrequentErrors; }
    }
    
    /**
     * Classe interna para representar informações de erro.
     */
    class ErrorInfo {
        private String errorType;
        private String errorMessage;
        private String activityId;
        private int count;
        
        public ErrorInfo(String errorType, String errorMessage, String activityId, int count) {
            this.errorType = errorType;
            this.errorMessage = errorMessage;
            this.activityId = activityId;
            this.count = count;
        }
        
        // Getters
        public String getErrorType() { return errorType; }
        public String getErrorMessage() { return errorMessage; }
        public String getActivityId() { return activityId; }
        public int getCount() { return count; }
    }
    
    /**
     * Classe interna para representar métricas de usuário.
     */
    class UserMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private Map<String, Integer> processStartsByUser;
        private Map<String, Integer> taskCompletionsByUser;
        private Map<String, Double> averageTaskCompletionTimeByUser;
        private Map<String, Integer> taskOverdueCountByUser;
        private Map<String, Integer> activeTasksByUser;
        
        public UserMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public Map<String, Integer> getProcessStartsByUser() { return processStartsByUser; }
        public Map<String, Integer> getTaskCompletionsByUser() { return taskCompletionsByUser; }
        public Map<String, Double> getAverageTaskCompletionTimeByUser() { return averageTaskCompletionTimeByUser; }
        public Map<String, Integer> getTaskOverdueCountByUser() { return taskOverdueCountByUser; }
        public Map<String, Integer> getActiveTasksByUser() { return activeTasksByUser; }
        
        // Setters
        public void setProcessStartsByUser(Map<String, Integer> processStartsByUser) { this.processStartsByUser = processStartsByUser; }
        public void setTaskCompletionsByUser(Map<String, Integer> taskCompletionsByUser) { this.taskCompletionsByUser = taskCompletionsByUser; }
        public void setAverageTaskCompletionTimeByUser(Map<String, Double> averageTaskCompletionTimeByUser) { this.averageTaskCompletionTimeByUser = averageTaskCompletionTimeByUser; }
        public void setTaskOverdueCountByUser(Map<String, Integer> taskOverdueCountByUser) { this.taskOverdueCountByUser = taskOverdueCountByUser; }
        public void setActiveTasksByUser(Map<String, Integer> activeTasksByUser) { this.activeTasksByUser = activeTasksByUser; }
    }
    
    /**
     * Classe interna para representar métricas de caminho de execução.
     */
    class PathMetrics {
        private String processDefinitionKey;
        private TimeRange timeRange;
        private List<ExecutionPath> commonPaths;
        private Map<String, Integer> pathFrequency;
        private Map<String, Double> pathAverageExecutionTime;
        private Map<String, List<String>> pathVariations;
        
        public PathMetrics(String processDefinitionKey, TimeRange timeRange) {
            this.processDefinitionKey = processDefinitionKey;
            this.timeRange = timeRange;
        }
        
        // Getters
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public TimeRange getTimeRange() { return timeRange; }
        public List<ExecutionPath> getCommonPaths() { return commonPaths; }
        public Map<String, Integer> getPathFrequency() { return pathFrequency; }
        public Map<String, Double> getPathAverageExecutionTime() { return pathAverageExecutionTime; }
        public Map<String, List<String>> getPathVariations() { return pathVariations; }
        
        // Setters
        public void setCommonPaths(List<ExecutionPath> commonPaths) { this.commonPaths = commonPaths; }
        public void setPathFrequency(Map<String, Integer> pathFrequency) { this.pathFrequency = pathFrequency; }
        public void setPathAverageExecutionTime(Map<String, Double> pathAverageExecutionTime) { this.pathAverageExecutionTime = pathAverageExecutionTime; }
        public void setPathVariations(Map<String, List<String>> pathVariations) { this.pathVariations = pathVariations; }
    }
    
    /**
     * Classe interna para representar um caminho de execução.
     */
    class ExecutionPath {
        private String id;
        private List<String> activities;
        private int frequency;
        private double averageExecutionTime;
        
        public ExecutionPath(String id, List<String> activities, int frequency, double averageExecutionTime) {
            this.id = id;
            this.activities = activities;
            this.frequency = frequency;
            this.averageExecutionTime = averageExecutionTime;
        }
        
        // Getters
        public String getId() { return id; }
        public List<String> getActivities() { return activities; }
        public int getFrequency() { return frequency; }
        public double getAverageExecutionTime() { return averageExecutionTime; }
    }
    
    /**
     * Classe interna para representar uma definição de métrica personalizada.
     */
    class CustomMetricDefinition {
        private String id;
        private String name;
        private String expression;
        private String description;
        private String resultType; // NUMBER, STRING, BOOLEAN, OBJECT
        
        public CustomMetricDefinition(String id, String name, String expression, String resultType) {
            this.id = id;
            this.name = name;
            this.expression = expression;
            this.resultType = resultType;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getExpression() { return expression; }
        public String getDescription() { return description; }
        public String getResultType() { return resultType; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * Classe interna para representar uma definição de relatório.
     */
    class ReportDefinition {
        private String id;
        private String name;
        private String description;
        private List<String> metricTypes;
        private String processDefinitionKey;
        private String format; // CSV, JSON, XML, PDF, HTML
        private List<String> recipients;
        
        public ReportDefinition(String id, String name, List<String> metricTypes, String processDefinitionKey) {
            this.id = id;
            this.name = name;
            this.metricTypes = metricTypes;
            this.processDefinitionKey = processDefinitionKey;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<String> getMetricTypes() { return metricTypes; }
        public String getProcessDefinitionKey() { return processDefinitionKey; }
        public String getFormat() { return format; }
        public List<String> getRecipients() { return recipients; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setFormat(String format) { this.format = format; }
        public void setRecipients(List<String> recipients) { this.recipients = recipients; }
    }
    
    /**
     * Classe interna para representar uma programação de relatório.
     */
    class ReportSchedule {
        private String id;
        private String cronExpression;
        private long startTime;
        private Long endTime;
        private boolean active;
        private String timeZone;
        
        public ReportSchedule(String id, String cronExpression, long startTime) {
            this.id = id;
            this.cronExpression = cronExpression;
            this.startTime = startTime;
            this.active = true;
        }
        
        // Getters
        public String getId() { return id; }
        public String getCronExpression() { return cronExpression; }
        public long getStartTime() { return startTime; }
        public Long getEndTime() { return endTime; }
        public boolean isActive() { return active; }
        public String getTimeZone() { return timeZone; }
        
        // Setters
        public void setEndTime(Long endTime) { this.endTime = endTime; }
        public void setActive(boolean active) { this.active = active; }
        public void setTimeZone(String timeZone) { this.timeZone = timeZone; }
    }
}