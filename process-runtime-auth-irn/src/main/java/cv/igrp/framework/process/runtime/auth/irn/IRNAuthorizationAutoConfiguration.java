package cv.igrp.framework.process.runtime.auth.irn;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"cv.igrp.framework.process.runtime.auth.irn.adapter",
})
@ConfigurationPropertiesScan
public class IRNAuthorizationAutoConfiguration {
}
