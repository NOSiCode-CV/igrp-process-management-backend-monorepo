package cv.igrp.framework.process.runtime.auth.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"cv.igrp.framework.process.runtime.auth.core.adapter",
})
public class CoreAuthorizationAutoConfiguration {
}
