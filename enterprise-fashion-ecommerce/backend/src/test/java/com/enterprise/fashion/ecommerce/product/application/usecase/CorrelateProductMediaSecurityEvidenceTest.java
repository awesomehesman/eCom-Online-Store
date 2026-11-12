package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.MalwareStatus;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Outcome;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.TechnicalStatus;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaSecurityEvidenceCorrelationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelateProductMediaSecurityEvidenceTest {
    private static final Instant NOW = Instant.parse("2026-11-12T12:00:00Z");
    private static final ProductId PRODUCT_ID = new ProductId("product-1");
    private static final UUID ASSET_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final String DIGEST = "a".repeat(64);
    private static final String ASSET_REVISION = "sha256_" + DIGEST;
    private Product product;
    private FakeEvidence evidence;
    private CorrelateProductMediaSecurityEvidence useCase;

    @BeforeEach
    void setUp() {
        product = new Product(PRODUCT_ID, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false)
                .withRepresentation(new ProductContent("Coat", "Winter coat"), List.of(
                        new ProductMedia("hero", PRODUCT_ID, null, ASSET_REVISION,
                                "https://media.example/hero.png", "Front view")));
        evidence = new FakeEvidence(binding());
        useCase = new CorrelateProductMediaSecurityEvidence(new ProductSource(product), evidence, direct());
    }

    @Test
    void exactTechnicalAndFreshCleanEvidenceCorrelatesWithoutAcceptance() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.MATCHING_NON_AUTHORITATIVE);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.MATCHING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CLEAN);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
        assertThat(result.technicalObservations()).hasSize(1);
        assertThat(result.malwareObservations()).hasSize(1);
    }

    @Test
    void missingBindingOrObservationsRemainMissing() {
        evidence.binding = null;
        assertThat(useCase.correlate(subject()).outcome()).isEqualTo(Outcome.MISSING);

        evidence.binding = binding();
        var result = useCase.correlate(subject());
        assertThat(result.outcome()).isEqualTo(Outcome.MISSING);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.MISSING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.MISSING);
    }

    @Test
    void contradictoryTechnicalOrMalwareHistoryIsConflicting() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.TIMEOUT));

        var result = useCase.correlate(subject());
        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
    }

    @Test
    void returnedBindingMustIndependentlyMatchEveryRequestedSubjectField() {
        ProductRevision otherRevision = new ProductRevision("v1_" + "b".repeat(64));
        ProductId otherProduct = new ProductId("product-2");
        UUID otherAsset = UUID.fromString("20000000-0000-0000-0000-000000000002");
        String otherDigest = "c".repeat(64);
        String otherAssetRevision = "sha256_" + otherDigest;
        ProductVariantId otherVariant = new ProductVariantId("variant-2");
        List<ProductImageAssociationBinding> mismatches = List.of(
                binding(otherProduct, product.revision(), "hero", ASSET_ID, ASSET_REVISION, DIGEST, null),
                binding(PRODUCT_ID, otherRevision, "hero", ASSET_ID, ASSET_REVISION, DIGEST, null),
                binding(PRODUCT_ID, product.revision(), "other", ASSET_ID, ASSET_REVISION, DIGEST, null),
                binding(PRODUCT_ID, product.revision(), "hero", otherAsset, ASSET_REVISION, DIGEST, null),
                binding(PRODUCT_ID, product.revision(), "hero", ASSET_ID,
                        otherAssetRevision, otherDigest, null),
                binding(PRODUCT_ID, product.revision(), "hero", ASSET_ID,
                        ASSET_REVISION, DIGEST, otherVariant));

        for (ProductImageAssociationBinding mismatch : mismatches) {
            evidence.binding = mismatch;
            var result = useCase.correlate(subject());
            assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
            assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
            assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
        }
    }

    @Test
    void mismatchedTechnicalAndMalwareObservationIdentitiesAreConflicting() {
        UUID otherAsset = UUID.fromString("20000000-0000-0000-0000-000000000002");
        ProductId otherProduct = new ProductId("product-2");
        String otherDigest = "c".repeat(64);
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED,
                null, otherAsset, PRODUCT_ID, DIGEST));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null,
                ASSET_ID, otherProduct, DIGEST));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null,
                ASSET_ID, PRODUCT_ID, otherDigest));

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
        assertThat(result.technicalObservations()).hasSize(1);
        assertThat(result.malwareObservations()).hasSize(2);
    }

    @Test
    void completeTechnicalHistoryMustBeUniformlyPassingToMatch() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        assertThat(useCase.correlate(subject()).technicalStatus()).isEqualTo(TechnicalStatus.MATCHING);

        evidence.technical.clear();
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT));
        var rejected = useCase.correlate(subject());
        assertThat(rejected.technicalStatus()).isEqualTo(TechnicalStatus.UNUSABLE);
        assertThat(rejected.outcome()).isEqualTo(Outcome.INDETERMINATE);
    }

    @Test
    void singlePassedAmongMultipleRejectedIsConflicting() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.UNSUPPORTED_FORMAT));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));

        var result = useCase.correlate(subject());
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
    }

    @Test
    void allRejectedHistoryIsUnusableNotConflicting() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.RESOURCE_LIMIT_EXCEEDED));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));

        var result = useCase.correlate(subject());
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.UNUSABLE);
        assertThat(result.outcome()).isEqualTo(Outcome.INDETERMINATE);
    }

    @Test
    void cleanCannotOverrideStaleOrIndeterminateMalwareHistory() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.STALE_SIGNATURE_DATABASE));

        var stale = useCase.correlate(subject());
        assertThat(stale.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
        assertThat(stale.outcome()).isEqualTo(Outcome.CONFLICTING);

        evidence.malware.clear();
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.SIGNATURE_TIMESTAMP_INVALID));
        assertThat(useCase.correlate(subject()).malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
    }

    @Test
    void uniformCleanHistoryWithoutStaleIsClean() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CLEAN);
        assertThat(result.outcome()).isEqualTo(Outcome.MATCHING_NON_AUTHORITATIVE);
    }

    @Test
    void singleCleanAmongMultipleIndeterminateIsConflicting() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.CONNECTION_FAILURE));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.TIMEOUT));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
    }

    @Test
    void allIndeterminateWithStaleIsStaleOrUnusable() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.STALE_SIGNATURE_DATABASE));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.TIMEOUT));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.STALE_OR_UNUSABLE);
        assertThat(result.outcome()).isEqualTo(Outcome.INDETERMINATE);
    }

    @Test
    void allIndeterminateWithoutStaleIsIndeterminate() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.CONNECTION_FAILURE));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.TIMEOUT));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INDETERMINATE);
        assertThat(result.outcome()).isEqualTo(Outcome.INDETERMINATE);
    }

    @Test
    void infectedAlwaysTakesPrecedenceOverCleanOrStale() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INFECTED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.STALE_SIGNATURE_DATABASE));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
        assertThat(result.outcome()).isEqualTo(Outcome.INFECTED);
    }

    @Test
    void versionUnavailableTreatedAsStale() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.VERSION_UNAVAILABLE));

        var result = useCase.correlate(subject());
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.STALE_OR_UNUSABLE);
    }

    @Test
    void staleRevisionReassociationAssetOrVariantMismatchFailsClosed() {
        ProductRevision stale = new ProductRevision("v1_" + "b".repeat(64));
        assertThat(useCase.correlate(subject(stale, "hero", ASSET_REVISION, null)).outcome())
                .isEqualTo(Outcome.STALE_OR_INVALIDATED);
        assertThat(useCase.correlate(subject(product.revision(), "other", ASSET_REVISION, null)).outcome())
                .isEqualTo(Outcome.STALE_OR_INVALIDATED);
        assertThat(useCase.correlate(subject(product.revision(), "hero", "sha256_" + "c".repeat(64), null)).outcome())
                .isEqualTo(Outcome.STALE_OR_INVALIDATED);
        assertThat(useCase.correlate(subject(product.revision(), "hero", ASSET_REVISION,
                new ProductVariantId("variant-1"))).outcome()).isEqualTo(Outcome.STALE_OR_INVALIDATED);
    }

    @Test
    void infectedIsNeverSuppressedAndIndeterminateRemainsNonEligible() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INFECTED, null));
        assertThat(useCase.correlate(subject()).outcome()).isEqualTo(Outcome.INFECTED);

        evidence.malware.clear();
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.CONNECTION_FAILURE));
        var result = useCase.correlate(subject());
        assertThat(result.outcome()).isEqualTo(Outcome.INDETERMINATE);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INDETERMINATE);
    }

    @Test
    void staleOrMissingSignatureFreshnessIsExplicitlyUnusable() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.STALE_SIGNATURE_DATABASE));
        assertThat(useCase.correlate(subject()).malwareStatus()).isEqualTo(MalwareStatus.STALE_OR_UNUSABLE);

        evidence.malware.clear();
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.SIGNATURE_TIMESTAMP_UNAVAILABLE));
        assertThat(useCase.correlate(subject()).malwareStatus()).isEqualTo(MalwareStatus.STALE_OR_UNUSABLE);
    }

    @Test
    void unavailableEvidenceSourceReturnsExplicitUnavailableOutcome() {
        evidence.failure = new DataAccessResourceFailureException("private database failure");

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.UNAVAILABLE);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.UNAVAILABLE);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.UNAVAILABLE);
    }

    private Subject subject() {
        return subject(product.revision(), "hero", ASSET_REVISION, null);
    }

    private Subject subject(ProductRevision revision, String mediaId, String assetRevision,
            ProductVariantId variantId) {
        String digest = assetRevision.substring("sha256_".length());
        return new Subject(PRODUCT_ID, revision, mediaId, revision, ASSET_ID, digest, assetRevision, variantId);
    }

    private ProductImageAssociationBinding binding() {
        return binding(PRODUCT_ID, product.revision(), "hero", ASSET_ID, ASSET_REVISION, DIGEST, null);
    }

    private ProductImageAssociationBinding binding(ProductId productId, ProductRevision revision,
            String mediaId, UUID assetId, String assetRevision, String digest, ProductVariantId variantId) {
        return new ProductImageAssociationBinding(UUID.randomUUID(), productId, revision, mediaId,
                revision, assetId, assetRevision, digest, variantId, NOW, false);
    }

    private ProductMediaSecurityCheckResult technical(ProductMediaSecurityCheckResult.Outcome outcome,
            ProductMediaSecurityCheckResult.FailureReason failure) {
        return technical(outcome, failure, ASSET_ID, PRODUCT_ID, DIGEST);
    }

    private ProductMediaSecurityCheckResult technical(ProductMediaSecurityCheckResult.Outcome outcome,
            ProductMediaSecurityCheckResult.FailureReason failure, UUID assetId,
            ProductId productId, String digest) {
        return new ProductMediaSecurityCheckResult(UUID.randomUUID(), assetId, productId, digest,
                CheckQuarantinedProductImage.CHECK_DEFINITION, CheckQuarantinedProductImage.IMPLEMENTATION_VERSION,
                NOW, outcome, failure, ProductMediaSecurityCheckResult.MalwareScan.NOT_PERFORMED, false);
    }

    private ProductImageMalwareObservation malware(ProductImageMalwareScanResult.Outcome outcome,
            ProductImageMalwareScanResult.FailureCategory failure) {
        return malware(outcome, failure, ASSET_ID, PRODUCT_ID, DIGEST);
    }

    private ProductImageMalwareObservation malware(ProductImageMalwareScanResult.Outcome outcome,
            ProductImageMalwareScanResult.FailureCategory failure, UUID assetId,
            ProductId productId, String digest) {
        String detection = outcome == ProductImageMalwareScanResult.Outcome.INFECTED ? "Test.Signature" : null;
        boolean scannerMetadata = outcome != ProductImageMalwareScanResult.Outcome.INDETERMINATE;
        var scan = new ProductImageMalwareScanResult(outcome, "clamav", "clamav-instream", "1.1.0",
                scannerMetadata ? "1.4.6" : null, scannerMetadata ? "daily-1" : null,
                scannerMetadata ? NOW.minusSeconds(60) : null, detection, failure);
        return new ProductImageMalwareObservation(UUID.randomUUID(), assetId, productId, digest,
                NOW, scan, false);
    }

    private static TransactionOperations direct() {
        return new TransactionOperations() {
            @Override public <T> T execute(TransactionCallback<T> action) { return action.doInTransaction(null); }
        };
    }

    private static final class ProductSource implements ProductPersistencePort {
        private final Product product;
        private ProductSource(Product product) { this.product = product; }
        @Override public void create(Product ignored) { throw new UnsupportedOperationException(); }
        @Override public boolean replaceDraft(Product replacement, ProductRevision expected) { return false; }
        @Override public Optional<Product> load(ProductId id) {
            return product.id().equals(id) ? Optional.of(product) : Optional.empty();
        }
        @Override public List<Product> loadAll(List<ProductId> ids) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findProductIdsAfter(ProductId after, int limit) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findCatalogueCandidateIdsAfter(ProductId after, int limit) { throw new UnsupportedOperationException(); }
    }

    private static final class FakeEvidence implements ProductMediaSecurityEvidenceCorrelationPort {
        private ProductImageAssociationBinding binding;
        private final List<ProductMediaSecurityCheckResult> technical = new ArrayList<>();
        private final List<ProductImageMalwareObservation> malware = new ArrayList<>();
        private RuntimeException failure;
        private FakeEvidence(ProductImageAssociationBinding binding) { this.binding = binding; }
        @Override public Optional<ProductImageAssociationBinding> exactBinding(Subject subject) {
            if (failure != null) throw failure;
            return Optional.ofNullable(binding);
        }
        @Override public List<ProductMediaSecurityCheckResult> technicalObservations(Subject subject) {
            if (failure != null) throw failure;
            return List.copyOf(technical);
        }
        @Override public List<ProductImageMalwareObservation> malwareObservations(Subject subject) {
            if (failure != null) throw failure;
            return List.copyOf(malware);
        }
    }
}
