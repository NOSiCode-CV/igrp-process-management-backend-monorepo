package cv.igrp.framework.process.runtime.auth.irn.adapter.integration.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for the IRN API integration.
 */
@ConfigurationProperties(prefix = "irn.api")
public record IrnApiProperties(
        String baseUrl,
        String accessToken,
        String superAdminEmail,
        @DefaultValue("session_id") String sessionCookieName
) {}
