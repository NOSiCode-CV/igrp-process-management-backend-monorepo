package cv.nosi.igrp.runtime.activiti.config;

import cv.nosi.igrp.runtime.activiti.ActivitiEventListener;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for Activiti.
 * Sets up the Activiti process engine with optimized settings.
 */
@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    @Autowired
    private ActivitiEventListener activitiEventListener;

    @Value("${activiti.database-schema-update:true}")
    private String databaseSchemaUpdate;

    @Value("${activiti.async-executor-activate:true}")
    private boolean asyncExecutorActivate;

    @Value("${activiti.history-level:full}")
    private String historyLevel;

    @Value("${activiti.process-definition-location-prefix:classpath:/processes/}")
    private String processDefinitionLocationPrefix;

    @Value("${activiti.deployment-name:igrp-process-deployment}")
    private String deploymentName;

    /**
     * Creates and configures the Activiti process engine.
     *
     * @param transactionManager The transaction manager to use
     * @return The configured process engine
     * @throws IOException If there's an error loading process definitions
     */
    @Bean
    public ProcessEngine processEngine(PlatformTransactionManager transactionManager) throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        
        // Set the data source and transaction manager
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager);
        
        // Configure database settings
        config.setDatabaseSchemaUpdate(databaseSchemaUpdate);
        
        // Configure history level
        config.setHistory(historyLevel);
        
        // Configure async executor
        config.setAsyncExecutorActivate(asyncExecutorActivate);
        
        // Configure job executor
        config.setAsyncExecutorDefaultTimerJobAcquireWaitTime(5000);
        config.setAsyncExecutorDefaultAsyncJobAcquireWaitTime(5000);
        config.setAsyncExecutorDefaultQueueSizeFullWaitTime(5000);
        config.setAsyncExecutorCorePoolSize(8);
        config.setAsyncExecutorMaxPoolSize(16);
        config.setAsyncExecutorThreadKeepAliveTime(60000);
        
        // Configure process definition deployment
        Resource[] resources = resourceLoader.getResources(processDefinitionLocationPrefix + "**/*.bpmn20.xml");
        config.setDeploymentResources(resources);
        config.setDeploymentName(deploymentName);
        
        // Register event listeners
        config.setEventListeners(Arrays.asList(activitiEventListener));
        
        // Configure caching
        config.setProcessDefinitionCacheLimit(100);
        config.setEnableSafeBpmnXml(true);
        
        // Build and return the process engine
        return config.buildProcessEngine();
    }

    /**
     * Creates a transaction manager for Activiti.
     *
     * @return The transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}