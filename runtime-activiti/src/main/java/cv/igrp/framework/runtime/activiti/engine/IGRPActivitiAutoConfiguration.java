package cv.igrp.framework.runtime.activiti.engine;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "cv.nosi.igrp.runtime.activiti.engine.process",
        "cv.nosi.igrp.runtime.activiti.engine.task"
})
public class IGRPActivitiAutoConfiguration {
}