package com.enterprise.fashion.ecommerce.pricing.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.enterprise.fashion.ecommerce.pricing.application.persistence.BasePricePersistenceService;
import com.enterprise.fashion.ecommerce.pricing.application.port.out.BasePricePersistencePort.RevisionConflict;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Currency;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Synthetic internal storage fixtures, not evidence of Product existence or authorized commercial writes. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class BasePricePersistenceIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired BasePricePersistenceService service;
    @Autowired com.enterprise.fashion.ecommerce.pricing.application.query.ObserveVariantBasePrice query;
    @Autowired PostgresBasePricePersistenceAdapter adapter;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager manager;
    @Autowired Flyway flyway;

    private Subject subject() { return new Subject("p-" + UUID.randomUUID(), "v-" + UUID.randomUUID()); }
    private Money money(String amount) { return new Money(new BigDecimal(amount), Currency.ZAR); }
    private TransactionTemplate transaction(boolean snapshot) {
        var result = new TransactionTemplate(manager);
        result.setIsolationLevel(snapshot ? TransactionDefinition.ISOLATION_REPEATABLE_READ : TransactionDefinition.ISOLATION_READ_COMMITTED);
        return result;
    }

    @Test
    void allMigrationsValidateAndExactMoneyRoundTripsWithoutProductIntegration() {
        flyway.validate();
        assertThat(flyway.info().applied()).extracting(migration -> migration.getVersion().getVersion())
                .containsExactly("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        assertThat(flyway.info().pending()).isEmpty();
        var s = subject();
        var amount = money("123456789012345678901234567890.123456789012345678901234567890");
        service.initialize(s, "first", amount);
        var rows = service.load(s);
        assertThat(rows).hasSize(1);
        assertThat(rows.getFirst().subject()).isEqualTo(s);
        assertThat(rows.getFirst().revision()).isEqualTo("first");
        assertThat(rows.getFirst().money().amount()).isEqualTo(amount.amount());
        assertThat(rows.getFirst().state()).isEqualTo(State.CURRENT);
        assertThatThrownBy(rows::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void replacementAndWithdrawalRetainImmutableRowsAndDistinctHistory() {
        var s = subject();
        service.initialize(s, "first", money("10.1234"));
        service.replace(s, "first", "second", money("20.5678"));
        assertThat(service.load(s)).filteredOn(a -> a.state() == State.CURRENT).singleElement()
                .satisfies(a -> assertThat(a.revision()).isEqualTo("second"));
        service.withdraw(s, "second", "withdrawal");
        var history = service.load(s);
        assertThat(history).hasSize(3).noneMatch(a -> a.state() == State.CURRENT);
        assertThat(history).filteredOn(a -> a.revision().equals("first")).singleElement()
                .satisfies(a -> { assertThat(a.money()).isEqualTo(money("10.1234")); assertThat(a.state()).isEqualTo(State.OBSOLETE); });
        assertThat(new SelectVariantBasePrice().select(s, history))
                .isEqualTo(new SelectVariantBasePrice.Historical(java.util.Set.of(State.OBSOLETE, State.WITHDRAWN)));
        assertThatThrownBy(() -> jdbc.update("UPDATE pricing.base_price_revision SET amount = 99 WHERE variant_reference = ?", s.variantReference()))
                .isInstanceOf(DataAccessException.class);
        assertThatThrownBy(() -> jdbc.update("DELETE FROM pricing.base_price_revision WHERE variant_reference = ?", s.variantReference()))
                .isInstanceOf(DataAccessException.class);
        assertThat(service.load(s)).isEqualTo(history);
    }

    @Test
    void missingIsDistinctFromWithdrawnAndDatabaseFailure() {
        var s = subject();
        assertThat(service.load(s)).isEmpty();
        service.initialize(s, "one", money("1"));
        service.withdraw(s, "one", "gone");
        assertThat(service.load(s)).extracting("state").contains(State.WITHDRAWN);
        assertThatThrownBy(() -> service.withdraw(s, "gone", "again")).isInstanceOf(RevisionConflict.class);

    }

    @Test
    void unavailableDatabaseDoesNotBecomeMissingSuccess() {
        var source = new org.springframework.jdbc.datasource.DriverManagerDataSource(
                "jdbc:postgresql://127.0.0.1:1/unavailable?connectTimeout=1", "unused", "unused");
        var unavailable = new PostgresBasePricePersistenceAdapter(org.springframework.jdbc.core.simple.JdbcClient.create(source));
        var boundary = new BasePricePersistenceService(unavailable, transaction(false), transaction(true));
        assertThatThrownBy(() -> boundary.load(subject())).isInstanceOf(DataAccessException.class);
    }

    @Test
    void repeatableReadRetainsOneObservationAcrossConcurrentCommit() throws Exception {
        var s = subject();
        service.initialize(s, "one", money("1"));
        try (var pool = Executors.newSingleThreadExecutor()) {
            transaction(true).executeWithoutResult(status -> {
                var before = adapter.load(s);
                assertThat(jdbc.queryForObject("SHOW transaction_isolation", String.class)).isEqualTo("repeatable read");
                try { pool.submit(() -> service.replace(s, "one", "two", money("2"))).get(15, TimeUnit.SECONDS); }
                catch (Exception failure) { throw new IllegalStateException(failure); }
                assertThat(adapter.load(s)).isEqualTo(before);
            });
        }
        assertThat(service.load(s)).hasSize(2);
    }

    @Test
    void duplicateInitializationAndRevisionFailWithoutPartialState() {
        var s = subject();
        service.initialize(s, "one", money("1"));
        assertThatThrownBy(() -> service.initialize(s, "other", money("2"))).isInstanceOf(DataAccessException.class);
        service.replace(s, "one", "two", money("2"));
        assertThatThrownBy(() -> service.replace(s, "two", "one", money("3"))).isInstanceOf(DataAccessException.class);
        assertThat(service.load(s)).hasSize(2).filteredOn(a -> a.state() == State.CURRENT).singleElement()
                .satisfies(a -> assertThat(a.revision()).isEqualTo("two"));
    }

    @Test
    void staleRevisionAndOwnershipMismatchFailClosedAndSeparateVariantsAreIndependent() {
        var s = subject();
        service.initialize(s, "one", money("1"));
        assertThatThrownBy(() -> service.replace(s, "stale", "two", money("2"))).isInstanceOf(RevisionConflict.class);
        var wrong = new Subject("different-owner", s.variantReference());
        assertThatThrownBy(() -> service.load(wrong)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> service.replace(wrong, "one", "two", money("2"))).isInstanceOf(RevisionConflict.class);
        var separate = new Subject(s.productReference(), "different-" + UUID.randomUUID());
        service.initialize(separate, "one", money("1"));
        assertThat(service.load(separate)).hasSize(1);
        assertThat(service.load(s)).hasSize(1);
    }

    @Test
    void adapterRequiresTransactionAndEffectiveSnapshotAndServiceOwnsBoundary() {
        var s = subject();
        assertThatThrownBy(() -> adapter.initialize(s, "one", money("1"))).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> adapter.load(s)).isInstanceOf(IllegalStateException.class);
        transaction(false).executeWithoutResult(status -> {
            assertThatThrownBy(() -> adapter.load(s)).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> service.load(s)).isInstanceOf(IllegalStateException.class);
        });
    }

    @Test
    void explicitFailureRollsBackBothInitialAndReplacementState() {
        var s = subject();
        assertThatThrownBy(() -> transaction(false).executeWithoutResult(status -> {
            adapter.initialize(s, "one", money("1"));
            throw new IllegalStateException("synthetic failure");
        })).isInstanceOf(IllegalStateException.class);
        assertThat(service.load(s)).isEmpty();
        service.initialize(s, "one", money("1"));
        assertThatThrownBy(() -> transaction(false).executeWithoutResult(status -> {
            adapter.replace(s, "one", "two", money("2"));
            throw new IllegalStateException("synthetic failure");
        })).isInstanceOf(IllegalStateException.class);
        assertThat(service.load(s)).hasSize(1).allMatch(a -> a.state() == State.CURRENT);
    }

    @Test
    void concurrentReplacementsAllowExactlyOneWinner() throws Exception {
        var s = subject();
        service.initialize(s, "one", money("1"));
        var start = new CountDownLatch(1);
        try (var pool = Executors.newFixedThreadPool(2)) {
            Callable<Boolean> first = () -> { start.await(); return replace(s, "two"); };
            Callable<Boolean> second = () -> { start.await(); return replace(s, "three"); };
            var a = pool.submit(first); var b = pool.submit(second); start.countDown();
            assertThat(List.of(a.get(15, TimeUnit.SECONDS), b.get(15, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(true, false);
        }
        assertThat(service.load(s)).hasSize(2).filteredOn(a -> a.state() == State.CURRENT).hasSize(1);
    }
    private boolean replace(Subject s, String revision) {
        try { service.replace(s, "one", revision, money("2")); return true; }
        catch (RevisionConflict expected) { return false; }
    }

    @Test
    void databaseRejectsForksNoncanonicalMoneyAndReassignment() {
        var s = subject();
        service.initialize(s, "one", money("1"));
        assertThatThrownBy(() -> jdbc.update("UPDATE pricing.base_price_subject SET product_reference = 'other' WHERE variant_reference = ?", s.variantReference()))
                .isInstanceOf(DataAccessException.class);
        assertThatThrownBy(() -> jdbc.update("INSERT INTO pricing.base_price_revision VALUES (?, ?, 'bad', 'one', 'ASSIGN', 'NaN', 'ZAR')", s.variantReference(), s.productReference()))
                .isInstanceOf(DataAccessException.class);
        service.replace(s, "one", "two", money("2"));
        assertThatThrownBy(() -> jdbc.update("INSERT INTO pricing.base_price_revision VALUES (?, ?, 'fork', 'one', 'ASSIGN', 3, 'ZAR')", s.variantReference(), s.productReference()))
                .isInstanceOf(DataAccessException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE pricing.base_price_subject SET head_revision = 'one' WHERE variant_reference = ?", s.variantReference()))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void orphanedSuccessorIsRejectedAtCommitAndRolledBack() {
        var s = subject();
        service.initialize(s, "r1", money("1"));
        var inserted = new java.util.concurrent.atomic.AtomicBoolean();
        assertThatThrownBy(() -> transaction(false).executeWithoutResult(status -> {
            jdbc.update("INSERT INTO pricing.base_price_revision VALUES (?, ?, 'r2', 'r1', 'ASSIGN', 2, 'ZAR')",
                    s.variantReference(), s.productReference());
            inserted.set(true); // Statement succeeded; rejection must occur at commit.
        })).isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class)
                .hasStackTraceContaining("Pricing head must be terminal");
        assertThat(inserted).isTrue();
        assertThat(service.load(s)).hasSize(1).singleElement().satisfies(a -> {
            assertThat(a.revision()).isEqualTo("r1");
            assertThat(a.state()).isEqualTo(State.CURRENT);
        });
        assertThat(jdbc.queryForObject("SELECT head_revision FROM pricing.base_price_subject WHERE variant_reference = ?",
                String.class, s.variantReference())).isEqualTo("r1");
        assertThat(jdbc.queryForObject("SELECT count(*) FROM pricing.base_price_revision WHERE variant_reference = ? AND revision = 'r2'",
                Integer.class, s.variantReference())).isZero();
    }

    @Test
    void directSqlAtomicReplacementCommitsWithTerminalHead() {
        var s = subject();
        service.initialize(s, "r1", money("1"));
        transaction(false).executeWithoutResult(status -> {
            jdbc.update("INSERT INTO pricing.base_price_revision VALUES (?, ?, 'r2', 'r1', 'ASSIGN', 2, 'ZAR')",
                    s.variantReference(), s.productReference());
            jdbc.update("UPDATE pricing.base_price_subject SET head_revision = 'r2' WHERE variant_reference = ?", s.variantReference());
        });
        assertThat(service.load(s)).hasSize(2).filteredOn(a -> a.state() == State.CURRENT).singleElement()
                .satisfies(a -> assertThat(a.revision()).isEqualTo("r2"));
        assertTerminalHead(s, "r2");
    }

    @Test
    void directSqlCannotMoveHeadToANonSuccessor() {
        var s = subject();
        service.initialize(s, "r1", money("1"));
        service.replace(s, "r1", "r2", money("2"));
        assertThatThrownBy(() -> transaction(false).executeWithoutResult(status ->
                jdbc.update("UPDATE pricing.base_price_subject SET head_revision = 'r1' WHERE variant_reference = ?", s.variantReference())))
                .isInstanceOf(DataAccessException.class).hasStackTraceContaining("Invalid Pricing head replacement");
        assertThatThrownBy(() -> transaction(false).executeWithoutResult(status ->
                jdbc.update("UPDATE pricing.base_price_subject SET head_revision = 'unrelated' WHERE variant_reference = ?", s.variantReference())))
                .isInstanceOf(DataAccessException.class).hasStackTraceContaining("Invalid Pricing head replacement");
        assertTerminalHead(s, "r2");
    }

    @Test
    void directSqlWithdrawalIsTerminalWithoutRestoringHistoricalAssignment() {
        var s = subject();
        service.initialize(s, "r1", money("1.123"));
        transaction(false).executeWithoutResult(status -> {
            jdbc.update("INSERT INTO pricing.base_price_revision VALUES (?, ?, 'withdrawn', 'r1', 'WITHDRAW', 1.123, 'ZAR')",
                    s.variantReference(), s.productReference());
            jdbc.update("UPDATE pricing.base_price_subject SET head_revision = 'withdrawn' WHERE variant_reference = ?", s.variantReference());
        });
        assertTerminalHead(s, "withdrawn");
        var rows = service.load(s);
        assertThat(rows).hasSize(2).noneMatch(a -> a.state() == State.CURRENT);
        assertThat(rows).filteredOn(a -> a.revision().equals("r1")).singleElement().satisfies(a -> {
            assertThat(a.state()).isEqualTo(State.OBSOLETE);
            assertThat(a.money()).isEqualTo(money("1.123"));
        });
        assertThat(rows).filteredOn(a -> a.revision().equals("withdrawn")).singleElement()
                .satisfies(a -> assertThat(a.state()).isEqualTo(State.WITHDRAWN));
    }

    @Test
    void multipleValidAdvancesInOneTransactionCheckFinalHeadRatherThanQueuedHeads() {
        var s = subject();
        transaction(false).executeWithoutResult(status -> {
            adapter.initialize(s, "r1", money("1"));
            adapter.replace(s, "r1", "r2", money("2"));
            adapter.withdraw(s, "r2", "r3");
        });
        assertTerminalHead(s, "r3");
        assertThat(service.load(s)).hasSize(3).noneMatch(a -> a.state() == State.CURRENT);
    }

    @Test
    void conditionalQueryObservesMissingSelectedReplacedAndWithdrawnHistory() {
        var s = subject();
        assertThat(query.observe(s)).isEqualTo(new SelectVariantBasePrice.Missing());
        service.initialize(s, "z-old", money("1.123456789"));
        var initial = (SelectVariantBasePrice.Selected) query.observe(s);
        assertThat(initial.assignment().money().amount()).isEqualTo(new BigDecimal("1.123456789"));
        service.replace(s, "z-old", "a-current", money("2.987654321"));
        assertThat(((SelectVariantBasePrice.Selected) query.observe(s)).assignment().revision()).isEqualTo("a-current");
        service.withdraw(s, "a-current", "withdrawn");
        assertThat(query.observe(s)).isEqualTo(new SelectVariantBasePrice.Historical(java.util.Set.of(State.OBSOLETE, State.WITHDRAWN)));
        assertThat(service.load(s)).hasSize(3).noneMatch(a -> a.state() == State.CURRENT);
    }

    @Test
    void conditionalQueryPropagatesOwnerMismatchAndTransactionBoundaryFailure() {
        var s = subject();
        service.initialize(s, "r", money("1"));
        assertThatThrownBy(() -> query.observe(new Subject("other-owner", s.variantReference())))
                .isInstanceOf(IllegalStateException.class);
        transaction(true).executeWithoutResult(status ->
                assertThatThrownBy(() -> query.observe(s)).isInstanceOf(IllegalStateException.class));
    }

    private void assertTerminalHead(Subject subject, String expected) {
        assertThat(jdbc.queryForObject("SELECT head_revision FROM pricing.base_price_subject WHERE variant_reference = ?",
                String.class, subject.variantReference())).isEqualTo(expected);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM pricing.base_price_revision WHERE variant_reference = ? AND previous_revision = ?",
                Integer.class, subject.variantReference(), expected)).isZero();
    }
}
