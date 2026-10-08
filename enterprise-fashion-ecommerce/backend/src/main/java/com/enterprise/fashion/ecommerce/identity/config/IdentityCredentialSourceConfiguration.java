package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialSource;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class IdentityCredentialSourceConfiguration {
    @Bean
    CredentialSource credentialSource(CredentialSourcePort source, PlatformTransactionManager manager) {
        TransactionTemplate transactions = new TransactionTemplate(manager);
        transactions.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactions.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return new CredentialSource(source, transactions);
    }
}
