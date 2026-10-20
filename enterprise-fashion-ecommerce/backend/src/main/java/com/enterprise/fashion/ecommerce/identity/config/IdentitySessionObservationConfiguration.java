package com.enterprise.fashion.ecommerce.identity.config;

import java.time.Clock;

import com.enterprise.fashion.ecommerce.identity.adapter.out.session.SpringSessionJdbcObservationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import com.enterprise.fashion.ecommerce.identity.application.usecase.ObserveIdentitySession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration(proxyBeanMethods = false)
public class IdentitySessionObservationConfiguration {
    @Bean
    IdentitySessionObservationPort identitySessionObservationPort(JdbcClient jdbc,
            @Value("${spring.session.jdbc.table-name:SPRING_SESSION}") String tableName) {
        return new SpringSessionJdbcObservationAdapter(jdbc, tableName, Clock.systemUTC());
    }

    @Bean
    ObserveIdentitySession observeIdentitySession(IdentitySessionObservationPort observations) {
        return new ObserveIdentitySession(observations);
    }
}
