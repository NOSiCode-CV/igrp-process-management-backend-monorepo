package cv.igrp.framework.process.runtime.auth.irn.adapter.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Configuration for the IRN REST client with appropriate timeouts and error handling.
 */
@Configuration
public class RestClientConfig {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

    @Bean
    public RestClient irnRestClient(IrnApiProperties props) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Authorization", "Bearer " + props.accessToken())
                .requestFactory(requestFactory)
                .build();
    }
}