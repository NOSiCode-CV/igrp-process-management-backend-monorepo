package cv.nosi.igrp.runtime.core.config;

/**
 * Constantes para propriedades de configuração do runtime de processos.
 * <p>
 * Define as chaves de propriedades utilizadas para configurar
 * o comportamento do runtime de processos.
 */
public final class RuntimeProperties {
    
    private RuntimeProperties() {
        // Classe utilitária, não deve ser instanciada
    }
    
    /** Prefixo para todas as propriedades do runtime de processos */
    public static final String PREFIX = "process.runtime.";
    
    /** Modo de execução do runtime (development, production, test) */
    public static final String RUNTIME_MODE = PREFIX + "mode";
    
    /** ID do tenant padrão */
    public static final String DEFAULT_TENANT_ID = PREFIX + "tenant.default";
    
    /** Diretório base para arquivos de configuração */
    public static final String CONFIG_DIRECTORY = PREFIX + "directory.config";
    
    /** Diretório de trabalho do runtime */
    public static final String WORK_DIRECTORY = PREFIX + "directory.work";
    
    /** Diretório para arquivos temporários */
    public static final String TEMP_DIRECTORY = PREFIX + "directory.temp";
    
    /** Diretório para arquivos de log */
    public static final String LOG_DIRECTORY = PREFIX + "directory.log";
    
    /** Nível de log (TRACE, DEBUG, INFO, WARN, ERROR) */
    public static final String LOG_LEVEL = PREFIX + "log.level";
    
    /** Tamanho do pool de threads para execução de processos */
    public static final String PROCESS_EXECUTION_THREAD_POOL_SIZE = PREFIX + "execution.threadPoolSize";
    
    /** Timeout para operações de processo em milissegundos */
    public static final String PROCESS_OPERATION_TIMEOUT = PREFIX + "execution.timeout";
    
    /** Intervalo de verificação de jobs em milissegundos */
    public static final String JOB_EXECUTION_INTERVAL = PREFIX + "job.interval";
    
    /** Número máximo de tentativas para jobs */
    public static final String MAX_JOB_RETRIES = PREFIX + "job.maxRetries";
    
    /** Tamanho máximo do histórico de processos */
    public static final String MAX_PROCESS_HISTORY_SIZE = PREFIX + "history.maxSize";
    
    /** Período de retenção de histórico em dias */
    public static final String HISTORY_RETENTION_DAYS = PREFIX + "history.retentionDays";
    
    /** Flag para habilitar/desabilitar o histórico de processos */
    public static final String PROCESS_HISTORY_ENABLED = PREFIX + "history.enabled";
    
    /** Flag para habilitar/desabilitar a validação de processos */
    public static final String PROCESS_VALIDATION_ENABLED = PREFIX + "validation.enabled";
    
    /** Flag para habilitar/desabilitar a execução assíncrona */
    public static final String ASYNC_EXECUTION_ENABLED = PREFIX + "async.enabled";
    
    /** Flag para habilitar/desabilitar o monitoramento de métricas */
    public static final String METRICS_ENABLED = PREFIX + "metrics.enabled";
    
    /** Intervalo de coleta de métricas em milissegundos */
    public static final String METRICS_COLLECTION_INTERVAL = PREFIX + "metrics.interval";
    
    /** Prefixo para propriedades de datasource */
    public static final String DATASOURCE_PREFIX = PREFIX + "datasource.";
    
    /** URL de conexão com o banco de dados */
    public static final String DATASOURCE_URL = DATASOURCE_PREFIX + "url";
    
    /** Usuário do banco de dados */
    public static final String DATASOURCE_USERNAME = DATASOURCE_PREFIX + "username";
    
    /** Senha do banco de dados */
    public static final String DATASOURCE_PASSWORD = DATASOURCE_PREFIX + "password";
    
    /** Driver JDBC */
    public static final String DATASOURCE_DRIVER = DATASOURCE_PREFIX + "driver";
    
    /** Tamanho mínimo do pool de conexões */
    public static final String DATASOURCE_MIN_POOL_SIZE = DATASOURCE_PREFIX + "minPoolSize";
    
    /** Tamanho máximo do pool de conexões */
    public static final String DATASOURCE_MAX_POOL_SIZE = DATASOURCE_PREFIX + "maxPoolSize";
    
    /** Timeout de conexão em milissegundos */
    public static final String DATASOURCE_CONNECTION_TIMEOUT = DATASOURCE_PREFIX + "connectionTimeout";
    
    /** Prefixo para propriedades de cache */
    public static final String CACHE_PREFIX = PREFIX + "cache.";
    
    /** Flag para habilitar/desabilitar o cache */
    public static final String CACHE_ENABLED = CACHE_PREFIX + "enabled";
    
    /** Tipo de cache (local, distributed) */
    public static final String CACHE_TYPE = CACHE_PREFIX + "type";
    
    /** Tamanho máximo do cache */
    public static final String CACHE_MAX_SIZE = CACHE_PREFIX + "maxSize";
    
    /** Tempo de expiração do cache em segundos */
    public static final String CACHE_EXPIRATION = CACHE_PREFIX + "expiration";
    
    /** Prefixo para propriedades de segurança */
    public static final String SECURITY_PREFIX = PREFIX + "security.";
    
    /** Flag para habilitar/desabilitar a autenticação */
    public static final String SECURITY_AUTHENTICATION_ENABLED = SECURITY_PREFIX + "authentication.enabled";
    
    /** Flag para habilitar/desabilitar a autorização */
    public static final String SECURITY_AUTHORIZATION_ENABLED = SECURITY_PREFIX + "authorization.enabled";
    
    /** Tipo de autenticação (basic, oauth, ldap) */
    public static final String SECURITY_AUTHENTICATION_TYPE = SECURITY_PREFIX + "authentication.type";
    
    /** Prefixo para propriedades de notificação */
    public static final String NOTIFICATION_PREFIX = PREFIX + "notification.";
    
    /** Flag para habilitar/desabilitar notificações */
    public static final String NOTIFICATION_ENABLED = NOTIFICATION_PREFIX + "enabled";
    
    /** Tipo de notificação (email, sms, webhook) */
    public static final String NOTIFICATION_TYPE = NOTIFICATION_PREFIX + "type";
    
    /** Prefixo para propriedades de email */
    public static final String EMAIL_PREFIX = NOTIFICATION_PREFIX + "email.";
    
    /** Host do servidor SMTP */
    public static final String EMAIL_HOST = EMAIL_PREFIX + "host";
    
    /** Porta do servidor SMTP */
    public static final String EMAIL_PORT = EMAIL_PREFIX + "port";
    
    /** Usuário do servidor SMTP */
    public static final String EMAIL_USERNAME = EMAIL_PREFIX + "username";
    
    /** Senha do servidor SMTP */
    public static final String EMAIL_PASSWORD = EMAIL_PREFIX + "password";
    
    /** Endereço de email do remetente */
    public static final String EMAIL_FROM = EMAIL_PREFIX + "from";
    
    /** Flag para habilitar/desabilitar SSL/TLS */
    public static final String EMAIL_SSL_ENABLED = EMAIL_PREFIX + "ssl.enabled";
    
    /** Prefixo para propriedades de webhook */
    public static final String WEBHOOK_PREFIX = NOTIFICATION_PREFIX + "webhook.";
    
    /** URL do webhook */
    public static final String WEBHOOK_URL = WEBHOOK_PREFIX + "url";
    
    /** Método HTTP do webhook (GET, POST, PUT) */
    public static final String WEBHOOK_METHOD = WEBHOOK_PREFIX + "method";
    
    /** Timeout do webhook em milissegundos */
    public static final String WEBHOOK_TIMEOUT = WEBHOOK_PREFIX + "timeout";
    
    /** Prefixo para propriedades de cluster */
    public static final String CLUSTER_PREFIX = PREFIX + "cluster.";
    
    /** Flag para habilitar/desabilitar o modo cluster */
    public static final String CLUSTER_ENABLED = CLUSTER_PREFIX + "enabled";
    
    /** Nome do nó no cluster */
    public static final String CLUSTER_NODE_NAME = CLUSTER_PREFIX + "nodeName";
    
    /** Endereço do nó no cluster */
    public static final String CLUSTER_NODE_ADDRESS = CLUSTER_PREFIX + "nodeAddress";
    
    /** Lista de endereços de outros nós no cluster */
    public static final String CLUSTER_NODES = CLUSTER_PREFIX + "nodes";
    
    /** Prefixo para propriedades de plugins */
    public static final String PLUGIN_PREFIX = PREFIX + "plugin.";
    
    /** Diretório de plugins */
    public static final String PLUGIN_DIRECTORY = PLUGIN_PREFIX + "directory";
    
    /** Lista de plugins habilitados */
    public static final String PLUGIN_ENABLED = PLUGIN_PREFIX + "enabled";
    
    /** Prefixo para propriedades de monitoramento */
    public static final String MONITORING_PREFIX = PREFIX + "monitoring.";
    
    /** Flag para habilitar/desabilitar o monitoramento de saúde */
    public static final String HEALTH_MONITORING_ENABLED = MONITORING_PREFIX + "health.enabled";
    
    /** Intervalo de verificação de saúde em milissegundos */
    public static final String HEALTH_CHECK_INTERVAL = MONITORING_PREFIX + "health.interval";
    
    /** Flag para habilitar/desabilitar o monitoramento de performance */
    public static final String PERFORMANCE_MONITORING_ENABLED = MONITORING_PREFIX + "performance.enabled";
    
    /** Intervalo de coleta de métricas de performance em milissegundos */
    public static final String PERFORMANCE_METRICS_INTERVAL = MONITORING_PREFIX + "performance.interval";
    
    /** Valores padrão para propriedades */
    public static final class Defaults {
        private Defaults() {
            // Classe utilitária, não deve ser instanciada
        }
        
        /** Modo de execução padrão */
        public static final String RUNTIME_MODE = "development";
        
        /** ID do tenant padrão */
        public static final String DEFAULT_TENANT_ID = "default";
        
        /** Tamanho do pool de threads padrão */
        public static final int PROCESS_EXECUTION_THREAD_POOL_SIZE = 10;
        
        /** Timeout padrão para operações de processo (30 segundos) */
        public static final long PROCESS_OPERATION_TIMEOUT = 30000L;
        
        /** Intervalo padrão de verificação de jobs (10 segundos) */
        public static final long JOB_EXECUTION_INTERVAL = 10000L;
        
        /** Número máximo de tentativas para jobs padrão */
        public static final int MAX_JOB_RETRIES = 3;
        
        /** Tamanho máximo do histórico de processos padrão */
        public static final int MAX_PROCESS_HISTORY_SIZE = 1000;
        
        /** Período de retenção de histórico em dias padrão (30 dias) */
        public static final int HISTORY_RETENTION_DAYS = 30;
        
        /** Flag padrão para histórico de processos */
        public static final boolean PROCESS_HISTORY_ENABLED = true;
        
        /** Flag padrão para validação de processos */
        public static final boolean PROCESS_VALIDATION_ENABLED = true;
        
        /** Flag padrão para execução assíncrona */
        public static final boolean ASYNC_EXECUTION_ENABLED = true;
        
        /** Flag padrão para monitoramento de métricas */
        public static final boolean METRICS_ENABLED = true;
        
        /** Intervalo padrão de coleta de métricas (60 segundos) */
        public static final long METRICS_COLLECTION_INTERVAL = 60000L;
        
        /** Tamanho mínimo do pool de conexões padrão */
        public static final int DATASOURCE_MIN_POOL_SIZE = 5;
        
        /** Tamanho máximo do pool de conexões padrão */
        public static final int DATASOURCE_MAX_POOL_SIZE = 20;
        
        /** Timeout de conexão padrão (30 segundos) */
        public static final long DATASOURCE_CONNECTION_TIMEOUT = 30000L;
        
        /** Flag padrão para cache */
        public static final boolean CACHE_ENABLED = true;
        
        /** Tipo de cache padrão */
        public static final String CACHE_TYPE = "local";
        
        /** Tamanho máximo do cache padrão */
        public static final int CACHE_MAX_SIZE = 1000;
        
        /** Tempo de expiração do cache padrão (1 hora) */
        public static final int CACHE_EXPIRATION = 3600;
        
        /** Flag padrão para autenticação */
        public static final boolean SECURITY_AUTHENTICATION_ENABLED = true;
        
        /** Flag padrão para autorização */
        public static final boolean SECURITY_AUTHORIZATION_ENABLED = true;
        
        /** Tipo de autenticação padrão */
        public static final String SECURITY_AUTHENTICATION_TYPE = "basic";
        
        /** Flag padrão para notificações */
        public static final boolean NOTIFICATION_ENABLED = true;
        
        /** Tipo de notificação padrão */
        public static final String NOTIFICATION_TYPE = "email";
        
        /** Porta SMTP padrão */
        public static final int EMAIL_PORT = 25;
        
        /** Flag padrão para SSL/TLS em email */
        public static final boolean EMAIL_SSL_ENABLED = true;
        
        /** Método HTTP padrão para webhook */
        public static final String WEBHOOK_METHOD = "POST";
        
        /** Timeout padrão para webhook (10 segundos) */
        public static final long WEBHOOK_TIMEOUT = 10000L;
        
        /** Flag padrão para modo cluster */
        public static final boolean CLUSTER_ENABLED = false;
        
        /** Flag padrão para monitoramento de saúde */
        public static final boolean HEALTH_MONITORING_ENABLED = true;
        
        /** Intervalo padrão de verificação de saúde (60 segundos) */
        public static final long HEALTH_CHECK_INTERVAL = 60000L;
        
        /** Flag padrão para monitoramento de performance */
        public static final boolean PERFORMANCE_MONITORING_ENABLED = true;
        
        /** Intervalo padrão de coleta de métricas de performance (60 segundos) */
        public static final long PERFORMANCE_METRICS_INTERVAL = 60000L;
    }
}