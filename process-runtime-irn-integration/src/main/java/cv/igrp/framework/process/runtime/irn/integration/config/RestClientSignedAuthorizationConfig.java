package cv.igrp.framework.process.runtime.irn.integration.config;

import cv.igrp.framework.process.runtime.irn.integration.config.security.JwtSigner;
import cv.igrp.framework.process.runtime.irn.integration.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;

import java.io.IOException;

/**
 * RestClient configuration that automatically signs a JWT token
 * and injects it in the Authorization Bearer header.
 */
@Configuration
@ConditionalOnProperty(name = "igrp.restclient.provider", havingValue = "irn")
public class RestClientSignedAuthorizationConfig {

    @Bean
    public JwtSigner jwtSigner(@Value("${igrp.authorization.jwt.private-key:default}") Resource privateKeyResource) {
        return new JwtSigner(privateKeyResource);
    }

    @Bean
    public JwtTokenService jwtTokenService(JwtSigner jwtSigner,
                                           @Value("${igrp.authorization.jwt.key:default}") String jwtKey) {
        return new JwtTokenService(jwtSigner, jwtKey);
    }

    @Bean
    public RestClient restClient(JwtTokenService jwtTokenService) {
        return RestClient.builder()
                .requestInterceptor(new JwtAuthorizationInterceptor(jwtTokenService))
                .build();
    }

    /**
     * Interceptor that injects cached JWT tokens into request headers.
     * Token generation and caching are handled by {@link JwtTokenService}.
     */
    private record JwtAuthorizationInterceptor(
            JwtTokenService jwtTokenService) implements ClientHttpRequestInterceptor {

        @NonNull
        @Override
        public ClientHttpResponse intercept(HttpRequest request, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String token = jwtTokenService.generateToken();
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        }
    }
}
