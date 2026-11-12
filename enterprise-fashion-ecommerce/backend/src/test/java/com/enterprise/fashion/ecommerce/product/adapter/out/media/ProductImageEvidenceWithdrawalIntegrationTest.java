package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaSecurityEvidenceCorrelationPort;
import com.enterprise.fashion.ecommerce.product.application.usecase.BindProductImageAssociation;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * V15 schema integration tests for product image evidence withdrawal.
 * Tests persistence mechanics only — no operational withdrawal capability is exposed.
 */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductImageEvidenceWithdrawalIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired ProductPersistenceService products;
    @Autowired IngestProductImage ingestion;
    @Autowired UpdateDraftProduct updates;
    @Autowired BindProductImageAssociation bindings;
    @Autowired ProductMediaSecurityEvidenceCorrelationPort correlationPort;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    // === Schema Constraint Tests ===

    @Test
    void exactlyOneTargetConstraintRejectsMissingTarget() {
        UUID withdrawalId = UUID.randomUUID();
        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, NULL, NULL, NULL, now(), 'test', 'SECURITY_FINDING', false)
                """, withdrawalId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void exactlyOneTargetConstraintRejectsMultipleTargets() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, ?, now(), 'test', 'SECURITY_FINDING', false)
                """, withdrawalId, checkId, fixture.bindingId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void technicalCheckForeignKeyEnforced() {
        UUID withdrawalId = UUID.randomUUID();
        UUID nonExistentCheckId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), 'test', 'SECURITY_FINDING', false)
                """, withdrawalId, nonExistentCheckId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void malwareObservationForeignKeyEnforced() {
        UUID withdrawalId = UUID.randomUUID();
        UUID nonExistentObservationId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, NULL, ?, NULL, now(), 'test', 'SECURITY_FINDING', false)
                """, withdrawalId, nonExistentObservationId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void bindingForeignKeyEnforced() {
        UUID withdrawalId = UUID.randomUUID();
        UUID nonExistentBindingId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, NULL, NULL, ?, now(), 'test', 'SECURITY_FINDING', false)
                """, withdrawalId, nonExistentBindingId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void duplicateTechnicalWithdrawalRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        insertTechnicalWithdrawal(checkId);

        assertThatThrownBy(() -> insertTechnicalWithdrawal(checkId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void duplicateMalwareWithdrawalRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID observationId = insertMalwareObservation(fixture, "CLEAN");
        insertMalwareWithdrawal(observationId);

        assertThatThrownBy(() -> insertMalwareWithdrawal(observationId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void duplicateBindingWithdrawalRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        insertBindingWithdrawal(fixture.bindingId());

        assertThatThrownBy(() -> insertBindingWithdrawal(fixture.bindingId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // === Immutability Tests ===

    @Test
    void withdrawalUpdateRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = insertTechnicalWithdrawal(checkId);

        assertThatThrownBy(() -> jdbc.update("""
                UPDATE product.product_image_evidence_withdrawal
                   SET withdrawal_reason = 'updated reason'
                 WHERE withdrawal_id = ?
                """, withdrawalId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void withdrawalDeleteRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = insertTechnicalWithdrawal(checkId);

        assertThatThrownBy(() -> jdbc.update("""
                DELETE FROM product.product_image_evidence_withdrawal WHERE withdrawal_id = ?
                """, withdrawalId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void withdrawalTruncateRejected() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        insertTechnicalWithdrawal(checkId);

        assertThatThrownBy(() -> jdbc.execute("TRUNCATE product.product_image_evidence_withdrawal"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // === Read-Side Correlation Port Tests ===

    @Test
    void isBindingWithdrawnReturnsFalseWhenNoWithdrawal() throws Exception {
        Fixture fixture = createFixtureWithBinding();

        boolean withdrawn = inReadTransaction(() ->
                correlationPort.isBindingWithdrawn(fixture.bindingId()));

        assertThat(withdrawn).isFalse();
    }

    @Test
    void isBindingWithdrawnReturnsTrueWhenWithdrawn() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        insertBindingWithdrawal(fixture.bindingId());

        boolean withdrawn = inReadTransaction(() ->
                correlationPort.isBindingWithdrawn(fixture.bindingId()));

        assertThat(withdrawn).isTrue();
    }

    @Test
    void withdrawnTechnicalCheckIdsReturnsCorrectSet() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId1 = insertTechnicalCheck(fixture);
        UUID checkId2 = insertTechnicalCheck(fixture);
        insertTechnicalWithdrawal(checkId1);

        var withdrawnIds = inReadTransaction(() ->
                correlationPort.withdrawnTechnicalCheckIds(fixture.subject()));

        assertThat(withdrawnIds).containsExactly(checkId1);
        assertThat(withdrawnIds).doesNotContain(checkId2);
    }

    @Test
    void withdrawnMalwareObservationIdsReturnsCorrectSet() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID obsId1 = insertMalwareObservation(fixture, "CLEAN");
        UUID obsId2 = insertMalwareObservation(fixture, "CLEAN");
        insertMalwareWithdrawal(obsId1);

        var withdrawnIds = inReadTransaction(() ->
                correlationPort.withdrawnMalwareObservationIds(fixture.subject()));

        assertThat(withdrawnIds).containsExactly(obsId1);
        assertThat(withdrawnIds).doesNotContain(obsId2);
    }

    @Test
    void noWithdrawalReturnsEmptySet() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        insertTechnicalCheck(fixture);
        insertMalwareObservation(fixture, "CLEAN");

        var technicalWithdrawn = inReadTransaction(() ->
                correlationPort.withdrawnTechnicalCheckIds(fixture.subject()));
        var malwareWithdrawn = inReadTransaction(() ->
                correlationPort.withdrawnMalwareObservationIds(fixture.subject()));

        assertThat(technicalWithdrawn).isEmpty();
        assertThat(malwareWithdrawn).isEmpty();
    }

    // === Non-Authoritative Constraint Tests ===

    @Test
    void authoritativeAcceptanceEligibleMustBeFalse() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), 'test', 'SECURITY_FINDING', true)
                """, withdrawalId, checkId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void withdrawalSourceMustBeValidEnum() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), 'test', 'INVALID_SOURCE', false)
                """, withdrawalId, checkId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void withdrawalReasonMustNotBeEmpty() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = UUID.randomUUID();

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), '', 'SECURITY_FINDING', false)
                """, withdrawalId, checkId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    /**
     * V15-03 regression: Whitespace-only withdrawal reason must be rejected.
     * The btrim() check ensures spaces, tabs, and newlines are stripped before validation.
     */
    @Test
    void withdrawalReasonMustNotBeWhitespaceOnly() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);

        // Test various whitespace patterns
        String[] whitespacePatterns = {
            "   ",           // spaces only
            "\t\t",          // tabs only
            "\n\n",          // newlines only
            "  \t\n  ",      // mixed whitespace
            " ",             // single space
        };

        for (String whitespace : whitespacePatterns) {
            UUID withdrawalId = UUID.randomUUID();
            assertThatThrownBy(() -> jdbc.update("""
                    INSERT INTO product.product_image_evidence_withdrawal
                        (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                         withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                    VALUES (?, ?, NULL, NULL, now(), ?, 'SECURITY_FINDING', false)
                    """, withdrawalId, checkId, whitespace))
                    .as("Whitespace pattern '%s' should be rejected", whitespace.replace("\t", "\\t").replace("\n", "\\n"))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    /**
     * V15-03 regression: Leading/trailing whitespace should be allowed if content exists.
     * btrim() only checks that the trimmed result is non-empty.
     */
    @Test
    void withdrawalReasonWithLeadingTrailingWhitespaceIsAccepted() throws Exception {
        Fixture fixture = createFixtureWithBinding();
        UUID checkId = insertTechnicalCheck(fixture);
        UUID withdrawalId = UUID.randomUUID();

        // Should succeed — has actual content after trimming
        int result = jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), '  valid reason with whitespace  ', 'SECURITY_FINDING', false)
                """, withdrawalId, checkId);

        assertThat(result).isEqualTo(1);
    }

    // === Helper Methods ===

    private Fixture createFixtureWithBinding() throws Exception {
        ProductId id = new ProductId("product-" + UUID.randomUUID());
        Product draft = new Product(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false);
        products.create(draft);
        var asset = ingestion.ingest(id, image());
        String mediaId = "hero-" + UUID.randomUUID();
        ProductMedia media = new ProductMedia(mediaId, id, null, asset.assetRevision(),
                "https://media.example/hero.png", "Front view of coat");
        updates.update(new UpdateDraftProduct.Command(id, draft.revision(),
                new ProductContent("Coat", "A winter coat"), List.of(), List.of(media)));
        Product current = products.load(id).orElseThrow();
        var binding = bindings.bind(new BindProductImageAssociation.Command(
                id, current.revision(), mediaId, current.revision(),
                asset.assetId(), asset.assetRevision(), asset.sha256(), null));
        return new Fixture(current, mediaId, asset.assetId(), asset.sha256(),
                asset.assetRevision(), binding.bindingId());
    }

    private UUID insertTechnicalCheck(Fixture fixture) {
        UUID checkId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO product.product_image_security_check
                    (check_id, asset_id, product_id, original_sha256, check_definition,
                     implementation_version, executed_at, outcome, failure_reason,
                     malware_scan_status, authoritative_acceptance_eligible)
                VALUES (?, ?, ?, ?, 'test-check', '1.0.0', now(), 'TECHNICAL_CHECKS_PASSED',
                        NULL, 'NOT_PERFORMED', false)
                """, checkId, fixture.assetId(), fixture.product().id().value(), fixture.sha256());
        return checkId;
    }

    private UUID insertMalwareObservation(Fixture fixture, String outcome) {
        UUID observationId = UUID.randomUUID();
        String detectionSignature = "INFECTED".equals(outcome) ? "Test.Signature" : null;
        String failureCategory = "INDETERMINATE".equals(outcome) ? "TIMEOUT" : null;
        String engineVersion = "INDETERMINATE".equals(outcome) ? null : "1.0.0";
        String sigDbVersion = "INDETERMINATE".equals(outcome) ? null : "daily-1";
        jdbc.update("""
                INSERT INTO product.product_image_malware_observation
                    (observation_id, asset_id, product_id, original_sha256, scanner_identity,
                     check_definition, implementation_version, engine_version,
                     signature_database_version, signature_database_timestamp, executed_at,
                     outcome, detection_signature, failure_category, authoritative_acceptance_eligible)
                VALUES (?, ?, ?, ?, 'clamav', 'clamav-instream', '1.1.0', ?, ?, 
                        CASE WHEN ? IS NOT NULL THEN now() ELSE NULL END,
                        now(), ?, ?, ?, false)
                """, observationId, fixture.assetId(), fixture.product().id().value(), fixture.sha256(),
                engineVersion, sigDbVersion, sigDbVersion, outcome, detectionSignature, failureCategory);
        return observationId;
    }

    private UUID insertTechnicalWithdrawal(UUID checkId) {
        UUID withdrawalId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, ?, NULL, NULL, now(), 'test withdrawal', 'SECURITY_FINDING', false)
                """, withdrawalId, checkId);
        return withdrawalId;
    }

    private UUID insertMalwareWithdrawal(UUID observationId) {
        UUID withdrawalId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, NULL, ?, NULL, now(), 'test withdrawal', 'ADVERSE_RECHECK', false)
                """, withdrawalId, observationId);
        return withdrawalId;
    }

    private UUID insertBindingWithdrawal(UUID bindingId) {
        UUID withdrawalId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO product.product_image_evidence_withdrawal
                    (withdrawal_id, technical_check_id, malware_observation_id, binding_id,
                     withdrawn_at, withdrawal_reason, withdrawal_source, authoritative_acceptance_eligible)
                VALUES (?, NULL, NULL, ?, now(), 'test withdrawal', 'RIGHTS_REVOCATION', false)
                """, withdrawalId, bindingId);
        return withdrawalId;
    }

    private <T> T inReadTransaction(java.util.function.Supplier<T> action) {
        var txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        txTemplate.setReadOnly(true);
        return txTemplate.execute(status -> action.get());
    }

    private static byte[] image() throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), "png", output);
        return output.toByteArray();
    }

    private record Fixture(Product product, String mediaId, UUID assetId, String sha256,
            String assetRevision, UUID bindingId) {
        Subject subject() {
            return new Subject(product.id(), product.revision(), mediaId, product.revision(),
                    assetId, sha256, assetRevision, null);
        }
    }
}
