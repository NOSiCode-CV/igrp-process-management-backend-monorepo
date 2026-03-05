package cv.igrp.framework.process.runtime.auth.irn.adapter.integration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Default RestClient configuration for IRN authentication module.
 * This bean is only created if no other RestClient bean exists (e.g., from process-runtime-irn-integration).
 */
@Configuration
public class IrnRestClientConfig {

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
