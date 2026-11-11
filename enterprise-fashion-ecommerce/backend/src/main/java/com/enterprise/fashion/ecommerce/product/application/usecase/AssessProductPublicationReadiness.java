package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.ContentAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaAssociationAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSecurityCheck;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSubject;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaUsageRightsAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Result;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Validity;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidencePolicy;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidencePolicy.Kind;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Outcome;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Reason;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/** Read-only evaluator. It neither authorizes an actor nor mutates Product lifecycle or visibility. */
public final class AssessProductPublicationReadiness {
    private final ProductPublicationEvidencePort evidencePort;
    private final ProductPublicationEvidencePolicy evidencePolicy;

    public AssessProductPublicationReadiness(ProductPublicationEvidencePort evidencePort,
            ProductPublicationEvidencePolicy evidencePolicy) {
        this.evidencePort = java.util.Objects.requireNonNull(evidencePort, "evidencePort");
        this.evidencePolicy = java.util.Objects.requireNonNull(evidencePolicy, "evidencePolicy");
    }

    public ProductPublicationReadiness assess(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        ProductRevision revision = product.revision();
        Snapshot evidence;
        try {
            evidence = evidencePort.currentEvidence(product.id(), revision);
        } catch (RuntimeException unavailable) {
            return unavailable();
        }
        if (evidence == null || evidence.availability() == Availability.UNAVAILABLE) {
            return unavailable();
        }
        if (evidence.availability() == Availability.CONFLICTING) {
            return conflicting();
        }

        EnumSet<Reason> reasons = EnumSet.noneOf(Reason.class);
        if (product.lifecycle() != ProductLifecycle.DRAFT) {
            reasons.add(Reason.PRODUCT_LIFECYCLE_NOT_ELIGIBLE);
        }
        if (product.content() == null || product.variants().isEmpty()) {
            reasons.add(Reason.PRODUCT_REPRESENTATION_INCOMPLETE);
        }
        if (product.media().stream().noneMatch(media -> media.variantId() == null)) {
            reasons.add(Reason.PRODUCT_LEVEL_IMAGE_REQUIRED);
        }

        Set<UUID> invalidated = invalidatedEvidence(product, revision, evidence, reasons);
        evaluateContent(product, revision, evidence.contentAcceptances(), invalidated, reasons);
        for (ProductMedia media : product.media()) {
            evaluateMedia(product, revision, media, evidence, invalidated, reasons);
        }
        if (reasons.contains(Reason.EVIDENCE_CONFLICT)) {
            return conflicting();
        }
        return reasons.isEmpty() ? ProductPublicationReadiness.ready()
                : ProductPublicationReadiness.denied(reasons);
    }

    private Set<UUID> invalidatedEvidence(Product product, ProductRevision revision, Snapshot evidence,
            Set<Reason> reasons) {
        Set<UUID> targets = new HashSet<>();
        evidence.invalidations().forEach(invalidation -> {
            if (invalidation.productId().equals(product.id())
                    && invalidation.productRevision().equals(revision)) {
                if (!evidencePolicy.supports(Kind.INVALIDATION, invalidation.source())) {
                    reasons.add(Reason.EVIDENCE_SOURCE_UNSUPPORTED);
                } else if (!targets.add(invalidation.targetEvidenceId())) {
                    reasons.add(Reason.EVIDENCE_CONFLICT);
                }
            }
        });
        return Set.copyOf(targets);
    }

    private void evaluateContent(Product product, ProductRevision revision, List<ContentAcceptance> candidates,
            Set<UUID> invalidated, Set<Reason> reasons) {
        List<ContentAcceptance> matching = candidates.stream()
                .filter(item -> item.productId().equals(product.id()) && item.productRevision().equals(revision))
                .toList();
        if (matching.size() > 1) {
            reasons.add(Reason.EVIDENCE_CONFLICT);
            return;
        }
        if (matching.isEmpty()) {
            reasons.add(Reason.CONTENT_ACCEPTANCE_MISSING_OR_STALE);
            return;
        }
        ContentAcceptance acceptance = matching.getFirst();
        if (!evidencePolicy.supports(Kind.CONTENT_ACCEPTANCE, acceptance.source())) {
            reasons.add(Reason.EVIDENCE_SOURCE_UNSUPPORTED);
        }
        if (!accepted(acceptance.result(), acceptance.validity()) || invalidated.contains(acceptance.evidenceId())) {
            reasons.add(Reason.EVIDENCE_INVALIDATED_OR_WITHDRAWN);
        }
    }

    private void evaluateMedia(Product product, ProductRevision revision, ProductMedia media, Snapshot evidence,
            Set<UUID> invalidated, Set<Reason> reasons) {
        Predicate<MediaSubject> exactSubject = subject -> subject.mediaAssociationId().equals(media.id())
                && subject.assetRevision().equals(media.assetRevision())
                && subject.associationRevision().equals(revision)
                && java.util.Objects.equals(subject.variantId(), media.variantId());

        MediaAssociationAcceptance association = oneMatching(
                evidence.associationAcceptances(), item -> item.productId().equals(product.id())
                        && item.productRevision().equals(revision) && exactSubject.test(item.subject()),
                Reason.MEDIA_ASSOCIATION_EVIDENCE_MISSING_OR_STALE, reasons);
        MediaUsageRightsAcceptance rights = oneMatching(
                evidence.usageRightsAcceptances(), item -> item.productId().equals(product.id())
                        && item.productRevision().equals(revision) && exactSubject.test(item.subject()),
                Reason.MEDIA_USAGE_RIGHTS_MISSING_OR_STALE, reasons);
        MediaSecurityCheck security = oneMatching(
                evidence.securityChecks(), item -> item.productId().equals(product.id())
                        && item.productRevision().equals(revision) && exactSubject.test(item.subject()),
                Reason.MEDIA_SECURITY_EVIDENCE_MISSING_OR_STALE, reasons);

        check(association, Kind.MEDIA_ASSOCIATION, invalidated, reasons,
                MediaAssociationAcceptance::source, MediaAssociationAcceptance::result,
                MediaAssociationAcceptance::validity, MediaAssociationAcceptance::evidenceId);
        check(rights, Kind.MEDIA_USAGE_RIGHTS, invalidated, reasons,
                MediaUsageRightsAcceptance::source, MediaUsageRightsAcceptance::result,
                MediaUsageRightsAcceptance::validity, MediaUsageRightsAcceptance::evidenceId);
        check(security, Kind.MEDIA_SECURITY, invalidated, reasons,
                MediaSecurityCheck::source, MediaSecurityCheck::result,
                MediaSecurityCheck::validity, MediaSecurityCheck::evidenceId);

        if (association != null && rights != null && security != null
                && (!association.subject().assetId().equals(rights.subject().assetId())
                || !association.subject().assetId().equals(security.subject().assetId()))) {
            reasons.add(Reason.EVIDENCE_CONFLICT);
        }
    }

    private static <T> T oneMatching(List<T> candidates, Predicate<T> predicate, Reason missing,
            Set<Reason> reasons) {
        List<T> matching = candidates.stream().filter(predicate).toList();
        if (matching.size() > 1) {
            reasons.add(Reason.EVIDENCE_CONFLICT);
            return null;
        }
        if (matching.isEmpty()) {
            reasons.add(missing);
            return null;
        }
        return matching.getFirst();
    }

    private <T> void check(T item, Kind kind, Set<UUID> invalidated, Set<Reason> reasons,
            Function<T, ProductPublicationEvidence.Source> source,
            Function<T, Result> result, Function<T, Validity> validity, Function<T, UUID> id) {
        if (item == null) {
            return;
        }
        if (!evidencePolicy.supports(kind, source.apply(item))) {
            reasons.add(Reason.EVIDENCE_SOURCE_UNSUPPORTED);
        }
        if (!accepted(result.apply(item), validity.apply(item)) || invalidated.contains(id.apply(item))) {
            reasons.add(Reason.EVIDENCE_INVALIDATED_OR_WITHDRAWN);
        }
    }

    private static boolean accepted(Result result, Validity validity) {
        return result == Result.ACCEPTED && validity == Validity.CURRENT;
    }

    private static ProductPublicationReadiness unavailable() {
        return new ProductPublicationReadiness(Outcome.EVIDENCE_UNAVAILABLE,
                Set.of(Reason.EVIDENCE_PROVIDER_UNAVAILABLE));
    }

    private static ProductPublicationReadiness conflicting() {
        return new ProductPublicationReadiness(Outcome.CONFLICTING_EVIDENCE, Set.of(Reason.EVIDENCE_CONFLICT));
    }
}
