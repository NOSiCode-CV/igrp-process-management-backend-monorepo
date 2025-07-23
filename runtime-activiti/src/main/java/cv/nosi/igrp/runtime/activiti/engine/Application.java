package cv.nosi.igrp.runtime.activiti.engine;

import org.activiti.engine.RepositoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Test application for runtime-activiti module.
 * This class is used to bootstrap the Spring Boot application for testing.
 */
@SpringBootApplication
public class Application {

    private final RepositoryService repositoryService;

    public Application(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {

            repositoryService.createDeployment()
                    .addInputStream("simple-process.bpmn20.xml", getClass().getResourceAsStream("/processes/simple-process.bpmn20.xml"))
                    .deploy();

            repositoryService.createProcessDefinitionQuery()
                    .list()
                    .forEach(pd -> System.out.println("Deployed process: " + pd.getName() + " id = " + pd.getId()));
        };
    }

}