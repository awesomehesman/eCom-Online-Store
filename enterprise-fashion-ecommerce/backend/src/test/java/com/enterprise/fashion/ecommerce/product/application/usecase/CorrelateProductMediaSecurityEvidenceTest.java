package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    // === Withdrawal-aware correlation tests ===

    @Test
    void withdrawnBindingReturnsWithdrawnOutcome() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
    }

    @Test
    void withdrawnPassedTechnicalObservationReturnsWithdrawnStatus() {
        var passed = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        evidence.technical.add(passed);
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.withdrawnTechnicalIds.add(passed.checkId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.technicalObservations()).hasSize(1);
    }

    @Test
    void withdrawnCleanMalwareObservationReturnsWithdrawnStatus() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        var clean = malware(ProductImageMalwareScanResult.Outcome.CLEAN, null);
        evidence.malware.add(clean);
        evidence.withdrawnMalwareIds.add(clean.observationId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
        assertThat(result.malwareObservations()).hasSize(1);
    }

    @Test
    void withdrawnRejectedTechnicalObservationReturnsWithdrawnStatus() {
        var rejected = technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT);
        evidence.technical.add(rejected);
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.withdrawnTechnicalIds.add(rejected.checkId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
    }

    @Test
    void withdrawnIndeterminateMalwareObservationReturnsWithdrawnStatus() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        var indeterminate = malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.CONNECTION_FAILURE);
        evidence.malware.add(indeterminate);
        evidence.withdrawnMalwareIds.add(indeterminate.observationId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
    }

    @Test
    void withdrawnInfectedMalwareObservationStillReturnsInfected() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        var infected = malware(ProductImageMalwareScanResult.Outcome.INFECTED, null);
        evidence.malware.add(infected);
        evidence.withdrawnMalwareIds.add(infected.observationId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.INFECTED);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
    }

    @Test
    void infectedTakesPrecedenceOverWithdrawnCleanInSameHistory() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        var clean = malware(ProductImageMalwareScanResult.Outcome.CLEAN, null);
        var infected = malware(ProductImageMalwareScanResult.Outcome.INFECTED, null);
        evidence.malware.add(clean);
        evidence.malware.add(infected);
        evidence.withdrawnMalwareIds.add(clean.observationId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.INFECTED);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
    }

    @Test
    void nonWithdrawnEvidenceStillReturnsMatchingNonAuthoritative() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.MATCHING_NON_AUTHORITATIVE);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.MATCHING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CLEAN);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
    }

    @Test
    void withdrawnAndMissingAreDistinguishable() {
        // Missing case
        evidence.binding = null;
        assertThat(useCase.correlate(subject()).outcome()).isEqualTo(Outcome.MISSING);

        // Withdrawn case
        evidence.binding = binding();
        evidence.bindingWithdrawn = true;
        assertThat(useCase.correlate(subject()).outcome()).isEqualTo(Outcome.WITHDRAWN);
    }

    @Test
    void partialWithdrawalOfTechnicalHistoryReturnsWithdrawn() {
        var passed1 = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        var passed2 = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        evidence.technical.add(passed1);
        evidence.technical.add(passed2);
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.withdrawnTechnicalIds.add(passed1.checkId()); // Only withdraw one

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.technicalObservations()).hasSize(2); // Both still visible
    }

    @Test
    void staleProductIdentityStillTakesPrecedenceOverWithdrawal() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        ProductRevision stale = new ProductRevision("v1_" + "b".repeat(64));
        var result = useCase.correlate(subject(stale, "hero", ASSET_REVISION, null));

        assertThat(result.outcome()).isEqualTo(Outcome.STALE_OR_INVALIDATED);
    }

    @Test
    void unavailableWithdrawalHistoryReturnsUnavailable() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.withdrawalFailure = new DataAccessResourceFailureException("withdrawal lookup failed");

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.UNAVAILABLE);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.UNAVAILABLE);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.UNAVAILABLE);
    }

    @Test
    void withdrawnEvidenceRemainsVisibleInObservationHistory() {
        var passed = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        evidence.technical.add(passed);
        var clean = malware(ProductImageMalwareScanResult.Outcome.CLEAN, null);
        evidence.malware.add(clean);
        evidence.withdrawnTechnicalIds.add(passed.checkId());
        evidence.withdrawnMalwareIds.add(clean.observationId());

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalObservations()).hasSize(1);
        assertThat(result.malwareObservations()).hasSize(1);
    }

    @Test
    void publicationRemainsDefaultDenyWithWithdrawnEvidence() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
    }

    // === V15 regression tests: precedence and withdrawal validation ===

    /**
     * V15-01 regression: Withdrawn binding with INFECTED malware must return INFECTED.
     * INFECTED always takes precedence over WITHDRAWN to prevent malware propagation.
     */
    @Test
    void withdrawnBindingWithInfectedMalwareReturnsInfected() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INFECTED, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.INFECTED);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
        // Technical still shows WITHDRAWN since binding is withdrawn
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
    }

    /**
     * V15-01 regression: Withdrawn binding with CLEAN malware returns WITHDRAWN (not UNAVAILABLE).
     * Observations must be loaded before checking withdrawal status.
     */
    @Test
    void withdrawnBindingWithCleanMalwareReturnsWithdrawn() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
        // Observations must still be loaded and returned
        assertThat(result.technicalObservations()).hasSize(1);
        assertThat(result.malwareObservations()).hasSize(1);
    }

    /**
     * V15-01 regression: Withdrawn binding with missing malware history returns WITHDRAWN
     * because binding withdrawal takes precedence over missing observations when binding is withdrawn.
     */
    @Test
    void withdrawnBindingWithMissingMalwareHistoryReturnsWithdrawn() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        // No malware observations added
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        // WITHDRAWN (binding) takes precedence over MISSING (malware)
        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.MISSING);
    }

    /**
     * V15-01 regression: Withdrawn binding still exposes historical observations.
     * Withdrawal must not prevent loading observations for audit trail.
     */
    @Test
    void withdrawnBindingRetainsHistoricalObservationsForAudit() {
        var tech1 = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        var tech2 = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        var mal1 = malware(ProductImageMalwareScanResult.Outcome.CLEAN, null);
        var mal2 = malware(ProductImageMalwareScanResult.Outcome.CLEAN, null);
        evidence.technical.add(tech1);
        evidence.technical.add(tech2);
        evidence.malware.add(mal1);
        evidence.malware.add(mal2);
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalObservations()).hasSize(2);
        assertThat(result.malwareObservations()).hasSize(2);
    }

    /**
     * V15-02 regression: CONFLICTING technical status takes precedence over WITHDRAWN.
     * Contradictory evidence is more severe than withdrawn evidence.
     */
    @Test
    void conflictingTechnicalTakesPrecedenceOverWithdrawn() {
        // Contradictory technical evidence
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        // Malware is WITHDRAWN since binding is withdrawn and no conflicting malware
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
    }

    /**
     * V15-02 regression: CONFLICTING malware status takes precedence over WITHDRAWN.
     * Contradictory evidence is more severe than withdrawn evidence.
     */
    @Test
    void conflictingMalwareTakesPrecedenceOverWithdrawn() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        // Contradictory malware evidence
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.TIMEOUT));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
        // Technical is WITHDRAWN since binding is withdrawn
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
    }

    /**
     * V15-02 regression: Both CONFLICTING takes precedence over both WITHDRAWN.
     * Overall outcome must be CONFLICTING when both have contradictions.
     */
    @Test
    void bothConflictingTakesPrecedenceOverBothWithdrawn() {
        // Contradictory technical
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.RESOURCE_LIMIT_EXCEEDED));
        // Contradictory malware
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                ProductImageMalwareScanResult.FailureCategory.CONNECTION_FAILURE));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.CONFLICTING);
    }

    /**
     * V15-02 regression: Withdrawn individual observations with contradictions.
     * CONFLICTING precedence applies even when specific observations are withdrawn.
     */
    @Test
    void conflictingWithWithdrawnObservationsTakesPrecedenceOverWithdrawn() {
        var passed = technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null);
        var rejected = technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT);
        evidence.technical.add(passed);
        evidence.technical.add(rejected);
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        // Withdraw the passed observation — but we still have contradicting history
        evidence.withdrawnTechnicalIds.add(passed.checkId());

        var result = useCase.correlate(subject());

        // CONFLICTING detected because passed+rejected contradiction exists
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
        assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING);
    }

    /**
     * V15-02 regression: INFECTED takes precedence over CONFLICTING which takes precedence over WITHDRAWN.
     * Full precedence chain: INFECTED > CONFLICTING > WITHDRAWN > MISSING > MATCHING
     */
    @Test
    void infectedTakesPrecedenceOverConflictingOverWithdrawn() {
        // Contradictory technical
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.REJECTED,
                ProductMediaSecurityCheckResult.FailureReason.UNSUPPORTED_FORMAT));
        // INFECTED malware
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.INFECTED, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        // INFECTED wins over everything
        assertThat(result.outcome()).isEqualTo(Outcome.INFECTED);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.INFECTED);
        // Technical still shows CONFLICTING
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.CONFLICTING);
    }

    /**
     * V15-01/02 combined: CLEAN withdrawal without conflicts returns WITHDRAWN correctly.
     * Ensures normal withdrawal case still works after precedence fixes.
     */
    @Test
    void cleanWithdrawalWithoutConflictsReturnsWithdrawn() {
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.technical.add(technical(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.malware.add(malware(ProductImageMalwareScanResult.Outcome.CLEAN, null));
        evidence.bindingWithdrawn = true;

        var result = useCase.correlate(subject());

        assertThat(result.outcome()).isEqualTo(Outcome.WITHDRAWN);
        assertThat(result.technicalStatus()).isEqualTo(TechnicalStatus.WITHDRAWN);
        assertThat(result.malwareStatus()).isEqualTo(MalwareStatus.WITHDRAWN);
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
        private boolean bindingWithdrawn = false;
        private final Set<UUID> withdrawnTechnicalIds = new HashSet<>();
        private final Set<UUID> withdrawnMalwareIds = new HashSet<>();
        private RuntimeException withdrawalFailure;
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
        @Override public boolean isBindingWithdrawn(UUID bindingId) {
            if (withdrawalFailure != null) throw withdrawalFailure;
            return bindingWithdrawn;
        }
        @Override public Set<UUID> withdrawnTechnicalCheckIds(Subject subject) {
            if (withdrawalFailure != null) throw withdrawalFailure;
            return Set.copyOf(withdrawnTechnicalIds);
        }
        @Override public Set<UUID> withdrawnMalwareObservationIds(Subject subject) {
            if (withdrawalFailure != null) throw withdrawalFailure;
            return Set.copyOf(withdrawnMalwareIds);
        }
    }
}
