package cv.nosi.igrp.runtime.core.config;

import java.util.Map;
import java.util.Properties;

/**
 * Interface para configuração do runtime de processos.
 * <p>
 * Define operações para acessar e gerenciar configurações
 * do runtime de processos.
 */
public interface RuntimeConfiguration {
    
    /**
     * Obtém uma propriedade de configuração como String.
     * 
     * @param key chave da propriedade
     * @return valor da propriedade, ou null se não existir
     */
    String getProperty(String key);
    
    /**
     * Obtém uma propriedade de configuração como String, com valor padrão.
     * 
     * @param key chave da propriedade
     * @param defaultValue valor padrão
     * @return valor da propriedade, ou o valor padrão se não existir
     */
    String getProperty(String key, String defaultValue);
    
    /**
     * Obtém uma propriedade de configuração como boolean.
     * 
     * @param key chave da propriedade
     * @param defaultValue valor padrão
     * @return valor da propriedade como boolean, ou o valor padrão se não existir
     */
    boolean getBooleanProperty(String key, boolean defaultValue);
    
    /**
     * Obtém uma propriedade de configuração como int.
     * 
     * @param key chave da propriedade
     * @param defaultValue valor padrão
     * @return valor da propriedade como int, ou o valor padrão se não existir
     */
    int getIntProperty(String key, int defaultValue);
    
    /**
     * Obtém uma propriedade de configuração como long.
     * 
     * @param key chave da propriedade
     * @param defaultValue valor padrão
     * @return valor da propriedade como long, ou o valor padrão se não existir
     */
    long getLongProperty(String key, long defaultValue);
    
    /**
     * Obtém uma propriedade de configuração como double.
     * 
     * @param key chave da propriedade
     * @param defaultValue valor padrão
     * @return valor da propriedade como double, ou o valor padrão se não existir
     */
    double getDoubleProperty(String key, double defaultValue);
    
    /**
     * Define uma propriedade de configuração.
     * 
     * @param key chave da propriedade
     * @param value valor da propriedade
     */
    void setProperty(String key, String value);
    
    /**
     * Remove uma propriedade de configuração.
     * 
     * @param key chave da propriedade
     * @return valor anterior da propriedade, ou null se não existia
     */
    String removeProperty(String key);
    
    /**
     * Verifica se uma propriedade de configuração existe.
     * 
     * @param key chave da propriedade
     * @return true se a propriedade existir
     */
    boolean hasProperty(String key);
    
    /**
     * Obtém todas as propriedades de configuração.
     * 
     * @return mapa com todas as propriedades
     */
    Map<String, String> getAllProperties();
    
    /**
     * Obtém todas as propriedades de configuração com um prefixo específico.
     * 
     * @param prefix prefixo das propriedades
     * @return mapa com as propriedades que possuem o prefixo
     */
    Map<String, String> getPropertiesWithPrefix(String prefix);
    
    /**
     * Carrega propriedades de configuração a partir de um arquivo.
     * 
     * @param filePath caminho do arquivo
     * @return true se as propriedades foram carregadas com sucesso
     */
    boolean loadFromFile(String filePath);
    
    /**
     * Carrega propriedades de configuração a partir de um objeto Properties.
     * 
     * @param properties objeto Properties
     */
    void loadFromProperties(Properties properties);
    
    /**
     * Salva as propriedades de configuração em um arquivo.
     * 
     * @param filePath caminho do arquivo
     * @return true se as propriedades foram salvas com sucesso
     */
    boolean saveToFile(String filePath);
    
    /**
     * Limpa todas as propriedades de configuração.
     */
    void clear();
    
    /**
     * Recarrega as propriedades de configuração a partir da fonte original.
     * 
     * @return true se as propriedades foram recarregadas com sucesso
     */
    boolean reload();
    
    /**
     * Obtém o diretório base para arquivos de configuração.
     * 
     * @return caminho do diretório de configuração
     */
    String getConfigDirectory();
    
    /**
     * Obtém o diretório de trabalho do runtime.
     * 
     * @return caminho do diretório de trabalho
     */
    String getWorkDirectory();
    
    /**
     * Obtém o diretório para arquivos temporários.
     * 
     * @return caminho do diretório temporário
     */
    String getTempDirectory();
    
    /**
     * Obtém o diretório para arquivos de log.
     * 
     * @return caminho do diretório de log
     */
    String getLogDirectory();
    
    /**
     * Obtém o nível de log configurado.
     * 
     * @return nível de log
     */
    String getLogLevel();
    
    /**
     * Obtém o modo de execução do runtime.
     * 
     * @return modo de execução (ex: "development", "production", "test")
     */
    String getRuntimeMode();
    
    /**
     * Verifica se o modo de execução é de desenvolvimento.
     * 
     * @return true se o modo for de desenvolvimento
     */
    boolean isDevelopmentMode();
    
    /**
     * Verifica se o modo de execução é de produção.
     * 
     * @return true se o modo for de produção
     */
    boolean isProductionMode();
    
    /**
     * Verifica se o modo de execução é de teste.
     * 
     * @return true se o modo for de teste
     */
    boolean isTestMode();
    
    /**
     * Obtém o ID do tenant padrão.
     * 
     * @return ID do tenant padrão
     */
    String getDefaultTenantId();
    
    /**
     * Obtém o tamanho do pool de threads para execução de processos.
     * 
     * @return tamanho do pool de threads
     */
    int getProcessExecutionThreadPoolSize();
    
    /**
     * Obtém o timeout para operações de processo em milissegundos.
     * 
     * @return timeout em milissegundos
     */
    long getProcessOperationTimeout();
    
    /**
     * Obtém o intervalo de verificação de jobs em milissegundos.
     * 
     * @return intervalo em milissegundos
     */
    long getJobExecutionInterval();
    
    /**
     * Obtém o número máximo de tentativas para jobs.
     * 
     * @return número máximo de tentativas
     */
    int getMaxJobRetries();
    
    /**
     * Obtém o tamanho máximo do histórico de processos.
     * 
     * @return tamanho máximo do histórico
     */
    int getMaxProcessHistorySize();
    
    /**
     * Obtém o período de retenção de histórico em dias.
     * 
     * @return período de retenção em dias
     */
    int getHistoryRetentionDays();
    
    /**
     * Verifica se o histórico de processos está habilitado.
     * 
     * @return true se o histórico estiver habilitado
     */
    boolean isProcessHistoryEnabled();
    
    /**
     * Verifica se a validação de processos está habilitada.
     * 
     * @return true se a validação estiver habilitada
     */
    boolean isProcessValidationEnabled();
    
    /**
     * Verifica se a execução assíncrona está habilitada.
     * 
     * @return true se a execução assíncrona estiver habilitada
     */
    boolean isAsyncExecutionEnabled();
    
    /**
     * Verifica se o monitoramento de métricas está habilitado.
     * 
     * @return true se o monitoramento estiver habilitado
     */
    boolean isMetricsEnabled();
    
    /**
     * Obtém o intervalo de coleta de métricas em milissegundos.
     * 
     * @return intervalo em milissegundos
     */
    long getMetricsCollectionInterval();
}