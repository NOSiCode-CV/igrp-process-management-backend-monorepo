package cv.igrp.framework.process.runtime.activiti.engine;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "cv.igrp.framework.process.runtime.activiti.engine.process",
        "cv.igrp.framework.process.runtime.activiti.engine.task",
        "cv.igrp.framework.process.runtime.activiti.engine.activity"
})
public class IGRPActivitiAutoConfiguration {
}