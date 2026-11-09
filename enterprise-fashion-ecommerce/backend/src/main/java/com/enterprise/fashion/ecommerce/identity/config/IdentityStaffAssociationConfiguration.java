package com.enterprise.fashion.ecommerce.identity.config;

import java.time.Clock;
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.JdbcStaffAssociationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffAssociations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class IdentityStaffAssociationConfiguration {
    @Bean StaffAssociationPort staffAssociationPort(JdbcClient jdbc) { return new JdbcStaffAssociationAdapter(jdbc); }
    @Bean StaffAssociations staffAssociations(StaffAssociationPort port, PlatformTransactionManager manager) {
        var transactions = new TransactionTemplate(manager);
        transactions.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return new StaffAssociations(port, transactions, Clock.systemUTC());
    }
}
