package com.enterprise.fashion.ecommerce.customer.adapter.out.persistence;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.enterprise.fashion.ecommerce.customer.application.port.out.CustomerAccountRelationshipPort.Observation;
import com.enterprise.fashion.ecommerce.customer.application.port.out.CustomerAccountRelationshipPort.WriteOutcome;
import com.enterprise.fashion.ecommerce.customer.domain.model.AccountId;
import com.enterprise.fashion.ecommerce.customer.domain.model.CustomerAccountRelationship;
import com.enterprise.fashion.ecommerce.customer.domain.model.CustomerId;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest
@Testcontainers
class CustomerAccountRelationshipIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired DataSource dataSource;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired Flyway flyway;

    @Test
    void recordsAndObservesExactPairsWithoutSelectingGlobalCardinality() {
        var adapter = adapter();
        var pair = pair();
        var otherAccount = new CustomerAccountRelationship(pair.customerId(), new AccountId(UUID.randomUUID()));
        var otherCustomer = new CustomerAccountRelationship(new CustomerId(UUID.randomUUID()), pair.accountId());

        assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.ABSENT);
        assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.INSERTED);
        assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.ALREADY_RECORDED);
        assertThat(adapter.record(otherAccount)).isEqualTo(WriteOutcome.INSERTED);
        assertThat(adapter.record(otherCustomer)).isEqualTo(WriteOutcome.INSERTED);
        assertThat(adapter.observe(pair)).isEqualTo(new Observation.Present(pair));
        assertThat(adapter.observe(otherAccount)).isEqualTo(new Observation.Present(otherAccount));
        assertThat(adapter.observe(otherCustomer)).isEqualTo(new Observation.Present(otherCustomer));
    }

    @Test
    void statementReceiptAndTransactionLocalPresenceDoNotSurviveRollback() {
        var adapter = adapter();
        var pair = pair();
        new TransactionTemplate(transactionManager).executeWithoutResult(transaction -> {
            assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.INSERTED);
            assertThat(adapter.observe(pair)).isEqualTo(new Observation.Present(pair));
            transaction.setRollbackOnly();
        });
        assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.ABSENT);
    }

    @Test
    void translatedStatementFailureLeavesPostgresqlTransactionAborted() {
        var adapter = adapter();
        var pair = pair();
        new TransactionTemplate(transactionManager).executeWithoutResult(transaction -> {
            JdbcClient.create(dataSource).sql("SET TRANSACTION READ ONLY").update();
            assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.UNAVAILABLE);
            assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.UNAVAILABLE);
            transaction.setRollbackOnly();
        });
        assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.ABSENT);
    }

    @Test
    void concurrentDuplicateWritesProduceOneInsert() throws Exception {
        var adapter = adapter();
        var pair = pair();
        var start = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(2)) {
            var first = workers.submit(() -> {
                start.await();
                return adapter.record(pair);
            });
            var second = workers.submit(() -> {
                start.await();
                return adapter.record(pair);
            });
            start.countDown();
            assertThat(java.util.List.of(first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(WriteOutcome.INSERTED, WriteOutcome.ALREADY_RECORDED);
        }
        assertThat(adapter.observe(pair)).isEqualTo(new Observation.Present(pair));
    }

    @Test
    void unavailableStorageCannotProduceReliableAbsenceOrWriteReceipt() {
        var inaccessible = new DriverManagerDataSource(
                POSTGRESQL.getJdbcUrl(), "nonexistent_customer_test_role", "");
        var adapter = new JdbcCustomerAccountRelationshipAdapter(JdbcClient.create(inaccessible));
        var pair = pair();
        assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.UNAVAILABLE);
        assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.UNAVAILABLE);
    }

    @Test
    void cleanMigrationInstallsCustomerSchema() {
        flyway.validate();
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("18");
        assertThat(adapter().observe(pair())).isEqualTo(Observation.Status.ABSENT);
    }

    @Test
    void upgradesExistingV17DatabaseWithoutCreatingRelationshipData() {
        JdbcClient.create(dataSource).sql("CREATE DATABASE customer_v17_upgrade_test").update();
        var source = new DriverManagerDataSource(
                POSTGRESQL.getJdbcUrl().replace("/" + POSTGRESQL.getDatabaseName(), "/customer_v17_upgrade_test"),
                POSTGRESQL.getUsername(), POSTGRESQL.getPassword());
        Flyway.configure().dataSource(source).target("17").load().migrate();
        var upgrade = Flyway.configure().dataSource(source).load();
        assertThat(upgrade.migrate().migrationsExecuted).isEqualTo(1);
        upgrade.validate();
        var adapter = new JdbcCustomerAccountRelationshipAdapter(JdbcClient.create(source));
        var pair = pair();
        assertThat(adapter.observe(pair)).isEqualTo(Observation.Status.ABSENT);
        assertThat(adapter.record(pair)).isEqualTo(WriteOutcome.INSERTED);
    }

    private JdbcCustomerAccountRelationshipAdapter adapter() {
        return new JdbcCustomerAccountRelationshipAdapter(JdbcClient.create(dataSource));
    }

    private static CustomerAccountRelationship pair() {
        return new CustomerAccountRelationship(new CustomerId(UUID.randomUUID()), new AccountId(UUID.randomUUID()));
    }
}
