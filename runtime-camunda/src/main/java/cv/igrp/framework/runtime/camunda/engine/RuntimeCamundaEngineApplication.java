package cv.igrp.framework.runtime.camunda.engine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RuntimeCamundaEngineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RuntimeCamundaEngineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
