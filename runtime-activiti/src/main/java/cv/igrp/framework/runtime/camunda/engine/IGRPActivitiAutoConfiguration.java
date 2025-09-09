package cv.igrp.framework.runtime.camunda.engine;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "cv.igrp.framework.runtime.activiti.engine.process",
        "cv.igrp.framework.runtime.activiti.engine.task"
})
public class IGRPActivitiAutoConfiguration {
}