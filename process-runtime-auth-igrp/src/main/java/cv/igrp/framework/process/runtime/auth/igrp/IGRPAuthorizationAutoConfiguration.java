package cv.igrp.framework.process.runtime.auth.igrp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"cv.igrp.framework.process.runtime.auth.igrp.adapter",
})
public class IGRPAuthorizationAutoConfiguration {
}
