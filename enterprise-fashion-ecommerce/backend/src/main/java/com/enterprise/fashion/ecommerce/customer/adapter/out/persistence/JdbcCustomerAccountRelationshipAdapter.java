package com.enterprise.fashion.ecommerce.customer.adapter.out.persistence;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.customer.application.port.out.CustomerAccountRelationshipPort;
import com.enterprise.fashion.ecommerce.customer.domain.model.AccountId;
import com.enterprise.fashion.ecommerce.customer.domain.model.CustomerAccountRelationship;
import com.enterprise.fashion.ecommerce.customer.domain.model.CustomerId;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

/** Explicitly composed for controlled use. A single statement is the atomic write boundary. */
public final class JdbcCustomerAccountRelationshipAdapter implements CustomerAccountRelationshipPort {

    private final JdbcClient jdbc;

    public JdbcCustomerAccountRelationshipAdapter(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public WriteOutcome record(CustomerAccountRelationship relationship) {
        try {
            int inserted = bind("""
                    INSERT INTO customer.account_relationship (customer_id, account_id)
                    VALUES (:customerId, :accountId)
                    ON CONFLICT (customer_id, account_id) DO NOTHING
                    """, relationship).update();
            return switch (inserted) {
                case 1 -> WriteOutcome.INSERTED;
                case 0 -> WriteOutcome.ALREADY_RECORDED;
                default -> WriteOutcome.UNAVAILABLE;
            };
        } catch (DataAccessException unavailable) {
            // Translating the failure does not repair an enclosing PostgreSQL transaction.
            return WriteOutcome.UNAVAILABLE;
        }
    }

    @Override
    public Observation observe(CustomerAccountRelationship relationship) {
        try {
            return bind("""
                    SELECT customer_id, account_id FROM customer.account_relationship
                    WHERE customer_id = :customerId AND account_id = :accountId
                    """, relationship)
                    .query((row, ignored) -> new CustomerAccountRelationship(
                            new CustomerId(row.getObject("customer_id", UUID.class)),
                            new AccountId(row.getObject("account_id", UUID.class))))
                    .optional()
                    .<Observation>map(Observation.Present::new)
                    .orElse(Observation.Status.ABSENT);
        } catch (DataAccessException unavailable) {
            // Translating the failure does not repair an enclosing PostgreSQL transaction.
            return Observation.Status.UNAVAILABLE;
        }
    }

    private JdbcClient.StatementSpec bind(String sql, CustomerAccountRelationship relationship) {
        return jdbc.sql(sql)
                .param("customerId", relationship.customerId().value())
                .param("accountId", relationship.accountId().value());
    }
}
