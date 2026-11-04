package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.time.Instant;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceObservation;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceService;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Synthetic storage tests only: no operational acceptance producer or trusted Staff fixture. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductEvidenceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private JdbcProductEvidenceStore store;
    @Autowired private ProductEvidenceService evidence;
    @Autowired private ProductPersistenceService products;
    @Autowired private PlatformTransactionManager manager;
    @Autowired private JdbcTemplate jdbc;

    private Product product() {
        Product value = new Product(new ProductId(UUID.randomUUID().toString()), ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL, false, false);
        products.create(value);
        return value;
    }

    private EvidenceEventRow record(Product product) {
        return record(product, ProductEvidenceEvent.Purpose.CONTENT);
    }

    private EvidenceEventRow record(Product product, ProductEvidenceEvent.Purpose purpose) {
        return new EvidenceEventRow(UUID.randomUUID(), product.id().value(), product.revision().value(), purpose.name(), "RECORD",
                "synthetic-source", "source-asserted-actor", new String[]{"synthetic-support-1", "synthetic-support-2"},
                "audit-correlation-1", Instant.EPOCH, "caller-cannot-set-recorder", null, null);
    }

    private EvidenceEventRow change(EvidenceEventRow target, String type, UUID replacement) {
        return new EvidenceEventRow(UUID.randomUUID(), target.productId(), target.representationRevision(), target.purpose(), type,
                "synthetic-withdrawal-source", "source-asserted-system", new String[]{"synthetic-reason-reference"},
                "audit-correlation-2", null, null, target.eventId(), replacement);
    }

    private TransactionTemplate transaction() {
        var transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return transaction;
    }

    private void append(EvidenceEventRow event) {
        // Test fixture owns this transaction; production application has no writing command.
        transaction().executeWithoutResult(status -> store.append(event));
    }

    private ProductEvidenceObservation observe(Product product) {
        return evidence.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
    }

    @Test
    void persistsExactBindingProvenanceAndDatabaseRecordingWithoutAcceptance() {
        Product product = product();
        EvidenceEventRow input = record(product);
        Instant before = Instant.now();
        append(input);
        var observation = observe(product);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        var event = observation.history().getFirst();
        assertThat(event.id()).isEqualTo(input.eventId());
        assertThat(event.productId()).isEqualTo(product.id());
        assertThat(event.revision()).isEqualTo(product.revision());
        assertThat(event.sourceReference()).isEqualTo("synthetic-source");
        assertThat(event.sourceActorClaim()).isEqualTo("source-asserted-actor");
        assertThat(event.supportingReferences()).containsExactly("synthetic-support-1", "synthetic-support-2");
        assertThat(event.auditCorrelation()).isEqualTo("audit-correlation-1");
        assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
        assertThat(event.recordedAt()).isBetween(before.minusSeconds(1), Instant.now().plusSeconds(1));
        assertThat(new AssessProductVisibilityReadiness(products).assess(product.id())).isEqualTo(ProductVisibilityReadiness.NOT_VISIBLE);
        jdbc.update("UPDATE product.product SET lifecycle = 'PUBLISHED' WHERE product_id = ?", product.id().value());
        assertThat(new AssessProductVisibilityReadiness(products).assess(product.id())).isEqualTo(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
    }

    @Test
    void missingProductAndMissingPurposeRemainDistinct() {
        Product product = product();
        assertThat(observe(product).state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        append(record(product));
        assertThat(evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA).state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        assertThat(evidence.observe(new ProductId(UUID.randomUUID().toString()), ProductEvidenceEvent.Purpose.CONTENT).state())
                .isEqualTo(ProductEvidenceObservation.State.PRODUCT_NOT_FOUND);
    }

    private Product changedRepresentation(Product old) {
        Product changed = old.withRepresentation(new ProductContent("Accessory", "Changed synthetic description"), java.util.List.of());
        jdbc.update("UPDATE product.product SET content_name = ?, content_description = ?, representation_revision = ? WHERE product_id = ?",
                changed.content().name(), changed.content().description(), changed.revision().value(), changed.id().value());
        return changed;
    }

    @Test
    void detectsStaleRevisionAndRejectsNewRecordForWrongRevision() {
        Product product = product();
        append(record(product));
        changedRepresentation(product);
        assertThat(observe(product).state()).isEqualTo(ProductEvidenceObservation.State.STALE);
        assertThatThrownBy(() -> append(record(product))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void conflictingCurrentRecordsNeverBecomeAcceptance() {
        Product product = product();
        append(record(product));
        append(record(product));
        assertThat(observe(product).state()).isEqualTo(ProductEvidenceObservation.State.CONFLICTING);
    }

    @Test
    void invalidationAndSupersessionRetainImmutableHistory() {
        Product product = product();
        var old = record(product);
        append(old);
        append(change(old, "INVALIDATE", null));
        assertThat(observe(product).state()).isEqualTo(ProductEvidenceObservation.State.INVALIDATED);
        assertThat(observe(product).history()).hasSize(2);
        Product second = product();
        var previous = record(second);
        append(previous);
        var replacement = record(changedRepresentation(second));
        append(replacement);
        append(change(previous, "SUPERSEDE", replacement.eventId()));
        var result = observe(second);
        assertThat(result.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(result.history()).hasSize(3);
        assertThatThrownBy(() -> jdbc.execute("TRUNCATE product.evidence_event")).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("DELETE FROM product.evidence_event WHERE event_id = ?", previous.eventId()))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.evidence_event SET source_reference = 'replacement' WHERE event_id = ?", previous.eventId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsCrossProductRevisionAndHistorySubstitution() {
        Product first = product();
        Product second = product();
        var original = record(first);
        append(original);
        var wrongRevision = new EvidenceEventRow(UUID.randomUUID(), second.id().value(), first.revision().value(), "CONTENT", "RECORD",
                "source", "claimed-actor", new String[]{"support"}, "correlation", null, null, null, null);
        assertThatThrownBy(() -> append(wrongRevision)).isInstanceOf(IllegalArgumentException.class);
        var foreignChange = new EvidenceEventRow(UUID.randomUUID(), second.id().value(), first.revision().value(), "CONTENT", "INVALIDATE",
                "source", "claimed-actor", new String[]{"support"}, "correlation", null, null, original.eventId(), null);
        assertThatThrownBy(() -> append(foreignChange)).isInstanceOf(DbActionExecutionException.class);
        assertThat(observe(first).state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(observe(second).history()).isEmpty();
    }

    @Test
    void failedAppendRollsBackEarlierHistoryInSameTransaction() {
        Product product = product();
        var first = record(product);
        var absentTarget = record(product);
        assertThatThrownBy(() -> transaction().executeWithoutResult(status -> {
            store.append(first);
            store.append(change(absentTarget, "INVALIDATE", null));
        })).isInstanceOf(DbActionExecutionException.class);
        assertThat(observe(product).history()).isEmpty();
    }

    @Test
    void databaseFailurePropagatesAndTransactionGuardsRemainEffective() {
        Product product = product();
        assertThatThrownBy(() -> store.append(record(product))).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> store.history(product.id(), ProductEvidenceEvent.Purpose.CONTENT)).isInstanceOf(IllegalStateException.class);
        new TransactionTemplate(manager).executeWithoutResult(status ->
                assertThatThrownBy(() -> store.history(product.id(), ProductEvidenceEvent.Purpose.CONTENT)).isInstanceOf(IllegalStateException.class));
        transaction().executeWithoutResult(status ->
                assertThatThrownBy(() -> observe(product)).isInstanceOf(IllegalStateException.class));
        jdbc.execute("ALTER TABLE product.evidence_event RENAME TO unavailable_evidence");
        try {
            assertThatThrownBy(() -> observe(product)).isInstanceOf(DataAccessException.class);
        } finally {
            jdbc.execute("ALTER TABLE product.unavailable_evidence RENAME TO evidence_event");
        }
    }

    @ParameterizedTest
    @EnumSource(value = ProductEvidenceObservation.State.class,
            names = {"MISSING", "STALE", "CONFLICTING", "INVALIDATED", "CURRENT_UNCONFIRMED"})
    void publishedReadinessRemainsUnconfirmedIndependentlyOfMediaDiagnostics(ProductEvidenceObservation.State expected) {
        Product product = product();
        // Synthetic historical lifecycle fixture, not a publication operation or authorization.
        jdbc.update("UPDATE product.product SET lifecycle = 'PUBLISHED' WHERE product_id = ?", product.id().value());
        if (expected != ProductEvidenceObservation.State.MISSING) {
            var media = record(product, ProductEvidenceEvent.Purpose.MEDIA);
            append(media);
            switch (expected) {
                case STALE -> changedRepresentation(product);
                case CONFLICTING -> append(record(product, ProductEvidenceEvent.Purpose.MEDIA));
                case INVALIDATED -> append(change(media, "INVALIDATE", null));
                case CURRENT_UNCONFIRMED -> { }
                default -> throw new AssertionError("Unsupported fixture state");
            }
        }
        var before = jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value());
        var mediaObservation = evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(mediaObservation.state()).isEqualTo(expected);
        assertThat(mediaObservation.history()).allSatisfy(event -> {
            assertThat(event.purpose()).isEqualTo(ProductEvidenceEvent.Purpose.MEDIA);
            assertThat(event.sourceActorClaim()).startsWith("source-asserted-");
            // Recording attribution is not a Staff identity, checking authority or acceptance.
            assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
        });
        assertThat(observe(product).state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        assertThat(observe(product).history()).isEmpty();

        // The current readiness evaluator does NOT consume MEDIA observations. This verifies
        // its independent fail-closed boundary, not media-specific eligibility evaluation.
        assertThat(new AssessProductVisibilityReadiness(products).assess(product.id()))
                .isEqualTo(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
        Product loaded = products.load(product.id()).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle.PUBLISHED);
        assertThat(loaded.isCustomerVisible()).isFalse();
        assertThat(loaded.variants()).isEmpty();
        assertThat(jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value())).isEqualTo(before);
        assertThat(evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA).history())
                .containsExactlyElementsOf(mediaObservation.history());
    }

    @Test
    void unavailableMediaHistoryFailsWithoutFabricatingAnObservationOrUpgradingReadiness() {
        Product product = product();
        append(record(product, ProductEvidenceEvent.Purpose.MEDIA));
        jdbc.update("UPDATE product.product SET lifecycle = 'PUBLISHED' WHERE product_id = ?", product.id().value());
        var before = jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value());
        var historyBefore = evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA).history();
        jdbc.execute("ALTER TABLE product.evidence_event RENAME TO unavailable_media_evidence");
        try {
            // The evidence service propagates this database failure. It has no unavailable
            // enum result and must not fabricate MISSING, acceptance or a successful observation.
            assertThatThrownBy(() -> evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA))
                    .isInstanceOf(DataAccessException.class);
            // Readiness does not query this store: it still withholds positive visibility.
            assertThat(new AssessProductVisibilityReadiness(products).assess(product.id()))
                    .isEqualTo(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
            assertThat(products.load(product.id()).orElseThrow().isCustomerVisible()).isFalse();
        } finally {
            jdbc.execute("ALTER TABLE product.unavailable_media_evidence RENAME TO evidence_event");
        }
        assertThat(jdbc.queryForMap("SELECT * FROM product.product WHERE product_id = ?", product.id().value())).isEqualTo(before);
        assertThat(evidence.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA).history())
                .containsExactlyElementsOf(historyBefore);
    }

    @Test
    void legacyV4AndV5RowsAcquireNoEvidenceDuringUpgrade() {
        for (String version : new String[]{"4", "5"}) {
            String database = "evidence_upgrade_v" + version;
            jdbc.execute("CREATE DATABASE " + database);
            var source = new DriverManagerDataSource("jdbc:postgresql://" + POSTGRESQL.getHost() + ":"
                    + POSTGRESQL.getMappedPort(5432) + "/" + database, POSTGRESQL.getUsername(), POSTGRESQL.getPassword());
            Flyway.configure().dataSource(source).target(version).load().migrate();
            var legacy = new JdbcTemplate(source);
            legacy.update("INSERT INTO product.product(product_id,lifecycle,family,attribute_standard_version,size_applicable,colour_applicable) "
                    + "VALUES ('legacy','PUBLISHED','Accessories','1.0.0',false,false)");
            var upgraded = Flyway.configure().dataSource(source).load();
            upgraded.migrate();
            upgraded.validate();
            assertThat(legacy.queryForObject("SELECT count(*) FROM product.evidence_event", Integer.class)).isZero();
            assertThat(legacy.queryForObject("SELECT lifecycle FROM product.product WHERE product_id = 'legacy'", String.class)).isEqualTo("PUBLISHED");
        }
    }
}
