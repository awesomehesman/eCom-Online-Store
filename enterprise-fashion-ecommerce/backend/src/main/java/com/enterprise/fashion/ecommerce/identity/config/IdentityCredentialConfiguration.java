package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.out.credential.Argon2CustomerPasswordVerificationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class IdentityCredentialConfiguration {

    @Bean
    CustomerPasswordVerificationPort customerPasswordVerificationPort() {
        return new Argon2CustomerPasswordVerificationAdapter();
    }
}
