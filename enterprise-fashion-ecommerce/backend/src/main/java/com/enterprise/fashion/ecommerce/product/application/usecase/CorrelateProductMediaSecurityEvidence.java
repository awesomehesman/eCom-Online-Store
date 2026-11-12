package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult.FailureCategory;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.MalwareStatus;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Outcome;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.TechnicalStatus;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaSecurityEvidenceCorrelationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Correlates existing technical observations without producing acceptance or publication evidence. */
public final class CorrelateProductMediaSecurityEvidence {
    private final ProductPersistencePort products;
    private final ProductMediaSecurityEvidenceCorrelationPort evidence;
    private final TransactionOperations reads;

    public CorrelateProductMediaSecurityEvidence(ProductPersistencePort products,
            ProductMediaSecurityEvidenceCorrelationPort evidence, TransactionOperations reads) {
        this.products = Objects.requireNonNull(products, "products");
        this.evidence = Objects.requireNonNull(evidence, "evidence");
        this.reads = Objects.requireNonNull(reads, "reads");
    }

    public ProductMediaSecurityEvidenceCorrelation correlate(Subject subject) {
        Objects.requireNonNull(subject, "subject");
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Media evidence correlation must own its transaction");
        }
        try {
            return Objects.requireNonNull(reads.execute(status -> correlateInTransaction(subject)));
        } catch (DataAccessException | TransactionException unavailable) {
            return result(subject, Outcome.UNAVAILABLE, TechnicalStatus.UNAVAILABLE,
                    MalwareStatus.UNAVAILABLE, List.of(), List.of());
        } catch (RuntimeException invalidOrUnavailable) {
            return result(subject, Outcome.UNAVAILABLE, TechnicalStatus.UNAVAILABLE,
                    MalwareStatus.UNAVAILABLE, List.of(), List.of());
        }
    }

    private ProductMediaSecurityEvidenceCorrelation correlateInTransaction(Subject subject) {
        var product = products.load(subject.productId()).orElse(null);
        if (product == null || !product.revision().equals(subject.productRevision())
                || !subject.associationRevision().equals(subject.productRevision())
                || !QuarantinedProductImageAsset.revisionOf(subject.originalSha256()).equals(subject.assetRevision())) {
            return stale(subject);
        }
        ProductMedia media = product.media().stream()
                .filter(item -> item.id().equals(subject.mediaAssociationId())).findFirst().orElse(null);
        if (media == null || !media.assetRevision().equals(subject.assetRevision())
                || !Objects.equals(media.variantId(), subject.variantId())) {
            return stale(subject);
        }
        var binding = evidence.exactBinding(subject).orElse(null);
        if (binding == null) {
            return result(subject, Outcome.MISSING, TechnicalStatus.MISSING,
                    MalwareStatus.MISSING, List.of(), List.of());
        }
        if (!exactBindingMatches(subject, binding)) {
            return result(subject, Outcome.CONFLICTING, TechnicalStatus.CONFLICTING,
                    MalwareStatus.CONFLICTING, List.of(), List.of());
        }

        List<ProductMediaSecurityCheckResult> technical = List.copyOf(evidence.technicalObservations(subject));
        List<ProductImageMalwareObservation> malware = List.copyOf(evidence.malwareObservations(subject));
        TechnicalStatus technicalStatus = technical.stream().allMatch(item -> observationMatches(subject,
                item.assetId(), item.productId(), item.originalSha256()))
                ? technicalStatus(technical) : TechnicalStatus.CONFLICTING;
        MalwareStatus malwareStatus = malware.stream().allMatch(item -> observationMatches(subject,
                item.assetId(), item.productId(), item.originalSha256()))
                ? malwareStatus(malware) : MalwareStatus.CONFLICTING;
        Outcome outcome = outcome(technicalStatus, malwareStatus);
        return result(subject, outcome, technicalStatus, malwareStatus, technical, malware);
    }

    private static boolean exactBindingMatches(Subject subject, ProductImageAssociationBinding binding) {
        return subject.productId().equals(binding.productId())
                && subject.productRevision().equals(binding.productRevision())
                && subject.mediaAssociationId().equals(binding.mediaAssociationId())
                && subject.associationRevision().equals(binding.associationRevision())
                && subject.assetId().equals(binding.assetId())
                && subject.originalSha256().equals(binding.originalSha256())
                && subject.assetRevision().equals(binding.assetRevision())
                && Objects.equals(subject.variantId(), binding.variantId());
    }

    private static boolean observationMatches(Subject subject, UUID assetId,
            ProductId productId, String originalSha256) {
        return subject.assetId().equals(assetId)
                && subject.productId().equals(productId)
                && subject.originalSha256().equals(originalSha256);
    }

    private static TechnicalStatus technicalStatus(List<ProductMediaSecurityCheckResult> observations) {
        if (observations.isEmpty()) return TechnicalStatus.MISSING;
        boolean allPassed = observations.stream().allMatch(item ->
                item.outcome() == ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED);
        boolean anyRejected = observations.stream().anyMatch(item ->
                item.outcome() == ProductMediaSecurityCheckResult.Outcome.REJECTED);
        boolean anyPassed = observations.stream().anyMatch(item ->
                item.outcome() == ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED);
        if (anyPassed && anyRejected) return TechnicalStatus.CONFLICTING;
        if (allPassed) return TechnicalStatus.MATCHING;
        // Mixed or unknown observations: conservative unusable classification
        return TechnicalStatus.UNUSABLE;
    }

    private static MalwareStatus malwareStatus(List<ProductImageMalwareObservation> observations) {
        if (observations.isEmpty()) return MalwareStatus.MISSING;
        boolean infected = observations.stream().anyMatch(item ->
                item.scanResult().outcome() == com.enterprise.fashion.ecommerce.product.application.media
                        .ProductImageMalwareScanResult.Outcome.INFECTED);
        boolean anyClean = observations.stream().anyMatch(item ->
                item.scanResult().outcome() == com.enterprise.fashion.ecommerce.product.application.media
                        .ProductImageMalwareScanResult.Outcome.CLEAN);
        boolean allClean = observations.stream().allMatch(item ->
                item.scanResult().outcome() == com.enterprise.fashion.ecommerce.product.application.media
                        .ProductImageMalwareScanResult.Outcome.CLEAN);
        boolean anyIndeterminate = observations.stream().anyMatch(item ->
                item.scanResult().outcome() == com.enterprise.fashion.ecommerce.product.application.media
                        .ProductImageMalwareScanResult.Outcome.INDETERMINATE);
        // Infected always takes precedence
        if (infected) return MalwareStatus.INFECTED;
        // Clean mixed with any other outcome is conflicting
        if (anyClean && anyIndeterminate) return MalwareStatus.CONFLICTING;
        // Stale signature metadata makes even clean results unreliable
        boolean anyStale = observations.stream().map(item -> item.scanResult().failureCategory())
                .anyMatch(CorrelateProductMediaSecurityEvidence::freshnessFailure);
        // Only uniformly clean history without stale indicators can be trusted
        if (allClean && !anyStale) return MalwareStatus.CLEAN;
        // Any stale or unusable evidence in history
        if (anyStale) return MalwareStatus.STALE_OR_UNUSABLE;
        // Mixed or indeterminate observations: conservative classification
        return MalwareStatus.INDETERMINATE;
    }

    private static boolean freshnessFailure(FailureCategory failure) {
        return failure == FailureCategory.STALE_SIGNATURE_DATABASE
                || failure == FailureCategory.SIGNATURE_TIMESTAMP_UNAVAILABLE
                || failure == FailureCategory.SIGNATURE_TIMESTAMP_INVALID
                || failure == FailureCategory.VERSION_UNAVAILABLE;
    }

    private static Outcome outcome(TechnicalStatus technical, MalwareStatus malware) {
        if (malware == MalwareStatus.INFECTED) return Outcome.INFECTED;
        if (technical == TechnicalStatus.CONFLICTING || malware == MalwareStatus.CONFLICTING) {
            return Outcome.CONFLICTING;
        }
        if (technical == TechnicalStatus.MISSING || malware == MalwareStatus.MISSING) return Outcome.MISSING;
        if (technical != TechnicalStatus.MATCHING || malware != MalwareStatus.CLEAN) return Outcome.INDETERMINATE;
        return Outcome.MATCHING_NON_AUTHORITATIVE;
    }

    private static ProductMediaSecurityEvidenceCorrelation stale(Subject subject) {
        return result(subject, Outcome.STALE_OR_INVALIDATED, TechnicalStatus.UNUSABLE,
                MalwareStatus.STALE_OR_UNUSABLE, List.of(), List.of());
    }

    private static ProductMediaSecurityEvidenceCorrelation result(Subject subject, Outcome outcome,
            TechnicalStatus technical, MalwareStatus malware,
            List<ProductMediaSecurityCheckResult> technicalObservations,
            List<ProductImageMalwareObservation> malwareObservations) {
        return new ProductMediaSecurityEvidenceCorrelation(subject, outcome, technical, malware,
                technicalObservations, malwareObservations, false);
    }
}
