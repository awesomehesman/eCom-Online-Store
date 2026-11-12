package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.MalwareStatus;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Outcome;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.TechnicalStatus;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.BindProductImageAssociation;
import com.enterprise.fashion.ecommerce.product.application.usecase.CorrelateProductMediaSecurityEvidence;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductMediaSecurityEvidenceCorrelationIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired ProductPersistenceService products;
    @Autowired IngestProductImage ingestion;
    @Autowired UpdateDraftProduct updates;
    @Autowired BindProductImageAssociation bindings;
    @Autowired CorrelateProductMediaSecurityEvidence correlations;
    @Autowired AssessProductPublicationReadiness readiness;
    @Autowired JdbcTemplate jdbc;

    @Test
    void exactBindingCorrelatesTechnicalAndCleanMalwareHistoryWithoutPublicationEligibility() throws Exception {
        Fixture fixture = fixture();
        technical(fixture, "TECHNICAL_CHECKS_PASSED", null);
        malware(fixture, "CLEAN", null, null);

        var result = correlations.correlate(subject(fixture));

        assertThat(result.outcome()).isEqualTo(Outcome.MATCHING_NON_AUTHORITATIVE);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.MATCHING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CLEAN);
        assertThat(result.technicalObservations()).hasSize(1);
        assertThat(result.malwareObservations()).hasSize(1);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
        assertThat(readiness.assess(fixture.product()).outcome())
                .isEqualTo(ProductPublicationReadiness.Outcome.EVIDENCE_UNAVAILABLE);
    }

    @Test
    void contradictoryHistoryIsConflictingAndInfectionIsNeverSuppressed() throws Exception {
        Fixture fixture = fixture();
        technical(fixture, "TECHNICAL_CHECKS_PASSED", null);
        technical(fixture, "REJECTED", "MALFORMED_CONTENT");
        malware(fixture, "CLEAN", null, null);
        malware(fixture, "INDETERMINATE", "STALE_SIGNATURE_DATABASE", null);

        var conflicting = correlations.correlate(subject(fixture));
        assertThat(conflicting.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(conflicting.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(conflicting.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);

        malware(fixture, "INFECTED", null, "Eicar-Signature");
        var infected = correlations.correlate(subject(fixture));
        assertThat(infected.outcome()).isEqualTo(Outcome.INFECTED);
        assertThat(infected.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
        assertThat(infected.authoritativeAcceptanceEligible()).isFalse();
    }

    @Test
    void representationChangeMakesPreviouslyExactCorrelationStale() throws Exception {
        Fixture fixture = fixture();
        technical(fixture, "TECHNICAL_CHECKS_PASSED", null);
        malware(fixture, "CLEAN", null, null);

        updates.update(new UpdateDraftProduct.Command(fixture.product().id(), fixture.product().revision(),
                new ProductContent("Coat", "Changed description"), fixture.product().variants(),
                fixture.product().media()));

        var result = correlations.correlate(subject(fixture));
        assertThat(result.outcome()).isEqualTo(Outcome.STALE_OR_INVALIDATED);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
    }

    private Fixture fixture() throws Exception {
        ProductId id = new ProductId("product-" + UUID.randomUUID());
        Product draft = new Product(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false);
        products.create(draft);
        var asset = ingestion.ingest(id, image());
        String mediaId = "hero-" + UUID.randomUUID();
        ProductMedia media = new ProductMedia(mediaId, id, null, asset.assetRevision(),
                "https://media.example/hero.png", "Front view");
        updates.update(new UpdateDraftProduct.Command(id, draft.revision(),
                new ProductContent("Coat", "Winter coat"), List.of(), List.of(media)));
        Product product = products.load(id).orElseThrow();
        bindings.bind(new BindProductImageAssociation.Command(id, product.revision(), mediaId,
                product.revision(), asset.assetId(), asset.assetRevision(), asset.sha256(), null));
        return new Fixture(product, mediaId, asset.assetId(), asset.sha256(), asset.assetRevision());
    }

    private static Subject subject(Fixture fixture) {
        return new Subject(fixture.product().id(), fixture.product().revision(), fixture.mediaId(),
                fixture.product().revision(), fixture.assetId(), fixture.sha256(), fixture.assetRevision(), null);
    }

    private void technical(Fixture fixture, String outcome, String failure) {
        jdbc.update("""
                INSERT INTO product.product_image_security_check
                    (check_id, asset_id, product_id, original_sha256, check_definition,
                     implementation_version, executed_at, outcome, failure_reason,
                     malware_scan_status, authoritative_acceptance_eligible)
                VALUES (?, ?, ?, ?, 'product-image-technical-validation', '1.0.0', ?, ?, ?,
                        'NOT_PERFORMED', false)
                """, UUID.randomUUID(), fixture.assetId(), fixture.product().id().value(), fixture.sha256(),
                java.sql.Timestamp.from(Instant.now()), outcome, failure);
    }

    private void malware(Fixture fixture, String outcome, String failure, String detection) {
        boolean conclusive = "CLEAN".equals(outcome) || "INFECTED".equals(outcome);
        jdbc.update("""
                INSERT INTO product.product_image_malware_observation
                    (observation_id, asset_id, product_id, original_sha256, scanner_identity,
                     check_definition, implementation_version, engine_version,
                     signature_database_version, signature_database_timestamp, executed_at,
                     outcome, detection_signature, failure_category, authoritative_acceptance_eligible)
                VALUES (?, ?, ?, ?, 'clamav', 'clamav-instream-malware-scan', '1.1.0', ?, ?, ?, ?, ?, ?, ?, false)
                """, UUID.randomUUID(), fixture.assetId(), fixture.product().id().value(), fixture.sha256(),
                conclusive ? "1.4.6" : null, conclusive ? "daily-1" : null,
                conclusive ? java.sql.Timestamp.from(Instant.now().minusSeconds(60)) : null,
                java.sql.Timestamp.from(Instant.now()), outcome, detection, failure);
    }

    private static byte[] image() throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), "png", output);
        return output.toByteArray();
    }

    private record Fixture(Product product, String mediaId, UUID assetId, String sha256, String assetRevision) { }
}
