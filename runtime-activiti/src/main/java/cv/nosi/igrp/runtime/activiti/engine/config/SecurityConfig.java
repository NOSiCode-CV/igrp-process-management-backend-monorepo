package cv.nosi.igrp.runtime.activiti.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        var userDetails = User.withUsername("admin")
                .password("{noop}admin")
                .roles("USER")
                .build();

        var manager = new InMemoryUserDetailsManager();
        manager.createUser(userDetails);

        return manager;
    }
}