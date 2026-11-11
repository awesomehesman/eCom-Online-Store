package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.ContentAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Invalidation;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.InvalidationKind;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaAssociationAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSecurityCheck;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSubject;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaUsageRightsAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Result;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Source;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Validity;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Outcome;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Reason;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AssessProductPublicationReadinessTest {
    private static final ProductId PRODUCT_ID = new ProductId("product-one");
    private static final Source CONTENT_SOURCE = new Source("product-owner", "content-acceptance", "1.0");
    private static final Source RIGHTS_SOURCE = new Source("product-owner", "media-rights", "1.0");
    private static final Source SECURITY_SOURCE = new Source("security-checker", "image-safety", "1.0");
    private static final Source ASSOCIATION_SOURCE = new Source("product-service", "media-association", "1.0");
    private static final Source INVALIDATION_SOURCE = new Source("evidence-authority", "invalidation", "1.0");

    @Test
    void completeCurrentTrustedEvidenceIsReadyWithoutPublishingTheProduct() {
        Product product = product(true, true);
        ProductRevision before = product.revision();

        var result = evaluator(validEvidence(product)).assess(product);

        assertThat(result.outcome()).isEqualTo(Outcome.READY);
        assertThat(result.reasons()).isEmpty();
        assertThat(product.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(product.revision()).isEqualTo(before);
        assertThat(product.isCustomerVisible()).isFalse();
    }

    @Test
    void nonDraftLifecycleIsNotEligibleEvenWhenEvidenceMatches() {
        Product draft = product(true, true);
        Product published = Product.reconstruct(draft.id(), draft.family(), draft.standard(),
                draft.sizeApplicable(), draft.colourApplicable(), ProductLifecycle.PUBLISHED,
                draft.variants(), draft.content(), draft.media(), draft.revision());

        var result = evaluator(validEvidence(published)).assess(published);

        assertDenied(result.outcome(), result.reasons(), Reason.PRODUCT_LIFECYCLE_NOT_ELIGIBLE);
        assertThat(published.lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
    }

    @Test
    void missingContentAcceptanceIsDenied() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);

        var result = evaluator(copy(valid, List.of(), valid.usageRightsAcceptances(), valid.securityChecks(),
                valid.associationAcceptances(), valid.invalidations())).assess(product);

        assertDenied(result.outcome(), result.reasons(), Reason.CONTENT_ACCEPTANCE_MISSING_OR_STALE);
    }

    @Test
    void staleProductRevisionAndWrongProductIdentityAreDenied() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);
        ProductRevision stale = product.withRepresentation(
                new ProductContent("Changed", "Changed description"), product.media()).revision();
        ContentAcceptance staleAcceptance = new ContentAcceptance(id("stale"), product.id(), stale,
                CONTENT_SOURCE, Result.ACCEPTED, Validity.CURRENT);
        ContentAcceptance wrongProduct = new ContentAcceptance(id("wrong-product"), new ProductId("other"),
                product.revision(), CONTENT_SOURCE, Result.ACCEPTED, Validity.CURRENT);

        for (ContentAcceptance acceptance : List.of(staleAcceptance, wrongProduct)) {
            var result = evaluator(copy(valid, List.of(acceptance), valid.usageRightsAcceptances(),
                    valid.securityChecks(), valid.associationAcceptances(), valid.invalidations())).assess(product);
            assertDenied(result.outcome(), result.reasons(), Reason.CONTENT_ACCEPTANCE_MISSING_OR_STALE);
        }
    }

    @Test
    void wrongVariantAssociationAndChangedAssetRevisionAreDenied() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);
        ProductMedia variantMedia = product.media().get(1);
        MediaAssociationAcceptance original = valid.associationAcceptances().get(1);
        MediaSubject wrongVariant = new MediaSubject(variantMedia.id(), original.subject().assetId(),
                variantMedia.assetRevision(), product.revision(), null);
        MediaSubject changedAsset = new MediaSubject(variantMedia.id(), original.subject().assetId(),
                "changed-revision", product.revision(), variantMedia.variantId());

        for (MediaSubject subject : List.of(wrongVariant, changedAsset)) {
            var associations = new ArrayList<>(valid.associationAcceptances());
            associations.set(1, new MediaAssociationAcceptance(original.evidenceId(), product.id(),
                    product.revision(), subject, ASSOCIATION_SOURCE, Result.ACCEPTED, Validity.CURRENT));
            var result = evaluator(copy(valid, valid.contentAcceptances(), valid.usageRightsAcceptances(),
                    valid.securityChecks(), associations, valid.invalidations())).assess(product);
            assertDenied(result.outcome(), result.reasons(), Reason.MEDIA_ASSOCIATION_EVIDENCE_MISSING_OR_STALE);
        }
    }

    @Test
    void unsupportedProducerOrCheckVersionIsDenied() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);
        MediaSecurityCheck check = valid.securityChecks().getFirst();
        MediaSecurityCheck unsupported = new MediaSecurityCheck(check.evidenceId(), product.id(),
                product.revision(), check.subject(), new Source("unknown-scanner", "image-safety", "9.9"),
                Result.ACCEPTED, Validity.CURRENT);
        var checks = new ArrayList<>(valid.securityChecks());
        checks.set(0, unsupported);

        var result = evaluator(copy(valid, valid.contentAcceptances(), valid.usageRightsAcceptances(),
                checks, valid.associationAcceptances(), valid.invalidations())).assess(product);

        assertDenied(result.outcome(), result.reasons(), Reason.EVIDENCE_SOURCE_UNSUPPORTED);
    }

    @Test
    void revokedEvidenceAndTrustedInvalidationBothDenyReadiness() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);
        ContentAcceptance content = valid.contentAcceptances().getFirst();
        ContentAcceptance revoked = new ContentAcceptance(content.evidenceId(), product.id(), product.revision(),
                CONTENT_SOURCE, Result.ACCEPTED, Validity.REVOKED);
        var revokedResult = evaluator(copy(valid, List.of(revoked), valid.usageRightsAcceptances(),
                valid.securityChecks(), valid.associationAcceptances(), List.of())).assess(product);
        assertDenied(revokedResult.outcome(), revokedResult.reasons(), Reason.EVIDENCE_INVALIDATED_OR_WITHDRAWN);

        Invalidation withdrawal = new Invalidation(id("withdrawal"), content.evidenceId(), product.id(),
                product.revision(), INVALIDATION_SOURCE, InvalidationKind.WITHDRAWN);
        var invalidatedResult = evaluator(copy(valid, valid.contentAcceptances(), valid.usageRightsAcceptances(),
                valid.securityChecks(), valid.associationAcceptances(), List.of(withdrawal))).assess(product);
        assertDenied(invalidatedResult.outcome(), invalidatedResult.reasons(),
                Reason.EVIDENCE_INVALIDATED_OR_WITHDRAWN);
    }

    @Test
    void missingProductLevelImageAndVariantOnlyImagesAreDenied() {
        Product missing = product(false, false);
        var missingResult = evaluator(validEvidence(missing)).assess(missing);
        assertDenied(missingResult.outcome(), missingResult.reasons(), Reason.PRODUCT_LEVEL_IMAGE_REQUIRED);

        Product variantOnly = product(false, true);
        var variantResult = evaluator(validEvidence(variantOnly)).assess(variantOnly);
        assertDenied(variantResult.outcome(), variantResult.reasons(), Reason.PRODUCT_LEVEL_IMAGE_REQUIRED);
    }

    @Test
    void duplicateEvidenceAndDisagreeingAssetIdentityAreConflicting() {
        Product product = product(true, true);
        Snapshot valid = validEvidence(product);
        var duplicateContent = new ArrayList<>(valid.contentAcceptances());
        ContentAcceptance content = duplicateContent.getFirst();
        duplicateContent.add(new ContentAcceptance(id("duplicate"), product.id(), product.revision(),
                CONTENT_SOURCE, Result.ACCEPTED, Validity.CURRENT));
        assertThat(evaluator(copy(valid, duplicateContent, valid.usageRightsAcceptances(), valid.securityChecks(),
                valid.associationAcceptances(), valid.invalidations())).assess(product).outcome())
                .isEqualTo(Outcome.CONFLICTING_EVIDENCE);

        MediaUsageRightsAcceptance rights = valid.usageRightsAcceptances().getFirst();
        MediaSubject disagreement = new MediaSubject(rights.subject().mediaAssociationId(), "different-asset",
                rights.subject().assetRevision(), rights.subject().associationRevision(), rights.subject().variantId());
        var changedRights = new ArrayList<>(valid.usageRightsAcceptances());
        changedRights.set(0, new MediaUsageRightsAcceptance(rights.evidenceId(), product.id(), product.revision(),
                disagreement, RIGHTS_SOURCE, Result.ACCEPTED, Validity.CURRENT));
        assertThat(evaluator(copy(valid, valid.contentAcceptances(), changedRights, valid.securityChecks(),
                valid.associationAcceptances(), valid.invalidations())).assess(product).outcome())
                .isEqualTo(Outcome.CONFLICTING_EVIDENCE);
    }

    @Test
    void unavailableOrFailingEvidenceProviderDefaultsToEvidenceUnavailable() {
        Product product = product(true, true);
        assertThat(evaluator(Snapshot.unavailable()).assess(product).outcome())
                .isEqualTo(Outcome.EVIDENCE_UNAVAILABLE);
        var failing = new AssessProductPublicationReadiness((id, revision) -> {
            throw new IllegalStateException("evidence service offline");
        }, AssessProductPublicationReadinessTest::supports);
        assertThat(failing.assess(product).outcome()).isEqualTo(Outcome.EVIDENCE_UNAVAILABLE);
    }

    @Test
    void snapshotAndAssessmentCollectionsAreImmutable() {
        Product product = product(true, true);
        Snapshot evidence = validEvidence(product);
        var result = evaluator(evidence).assess(product);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> evidence.contentAcceptances().clear());
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.reasons().add(Reason.EVIDENCE_CONFLICT));
    }

    @Test
    void trustedPortIsQueriedForTheExactProductAndRevision() {
        Product product = product(true, true);
        AtomicReference<ProductId> requestedId = new AtomicReference<>();
        AtomicReference<ProductRevision> requestedRevision = new AtomicReference<>();
        var evaluator = new AssessProductPublicationReadiness((id, revision) -> {
            requestedId.set(id);
            requestedRevision.set(revision);
            return validEvidence(product);
        }, AssessProductPublicationReadinessTest::supports);

        evaluator.assess(product);

        assertThat(requestedId).hasValue(product.id());
        assertThat(requestedRevision).hasValue(product.revision());
    }

    private static AssessProductPublicationReadiness evaluator(Snapshot snapshot) {
        return new AssessProductPublicationReadiness((id, revision) -> snapshot,
                AssessProductPublicationReadinessTest::supports);
    }

    private static boolean supports(Object kind, Source source) {
        return switch (source.definition()) {
            case "content-acceptance", "media-rights", "image-safety", "media-association", "invalidation" ->
                    source.version().equals("1.0");
            default -> false;
        };
    }

    private static Snapshot validEvidence(Product product) {
        ProductRevision revision = product.revision();
        var content = List.of(new ContentAcceptance(id("content"), product.id(), revision,
                CONTENT_SOURCE, Result.ACCEPTED, Validity.CURRENT));
        var rights = new ArrayList<MediaUsageRightsAcceptance>();
        var checks = new ArrayList<MediaSecurityCheck>();
        var associations = new ArrayList<MediaAssociationAcceptance>();
        for (ProductMedia media : product.media()) {
            MediaSubject subject = new MediaSubject(media.id(), "asset-" + media.id(), media.assetRevision(),
                    revision, media.variantId());
            rights.add(new MediaUsageRightsAcceptance(id("rights-" + media.id()), product.id(), revision,
                    subject, RIGHTS_SOURCE, Result.ACCEPTED, Validity.CURRENT));
            checks.add(new MediaSecurityCheck(id("security-" + media.id()), product.id(), revision,
                    subject, SECURITY_SOURCE, Result.ACCEPTED, Validity.CURRENT));
            associations.add(new MediaAssociationAcceptance(id("association-" + media.id()), product.id(), revision,
                    subject, ASSOCIATION_SOURCE, Result.ACCEPTED, Validity.CURRENT));
        }
        return new Snapshot(Availability.AVAILABLE, content, rights, checks, associations, List.of());
    }

    private static Snapshot copy(Snapshot original, List<ContentAcceptance> content,
            List<MediaUsageRightsAcceptance> rights, List<MediaSecurityCheck> checks,
            List<MediaAssociationAcceptance> associations, List<Invalidation> invalidations) {
        return new Snapshot(original.availability(), content, rights, checks, associations, invalidations);
    }

    private static Product product(boolean productImage, boolean variantImage) {
        var variantId = new ProductVariantId("variant-m");
        var configuration = new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, false, ProductSize.apparel("M"), null);
        var variant = new ProductVariant(variantId, PRODUCT_ID, configuration, null, null);
        Product product = new Product(PRODUCT_ID, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, false).withVariant(variant);
        var media = new ArrayList<ProductMedia>();
        if (productImage) {
            media.add(new ProductMedia("front", PRODUCT_ID, null, "asset-revision-1",
                    "https://media.example.invalid/front", "Front view"));
        }
        if (variantImage) {
            media.add(new ProductMedia("variant", PRODUCT_ID, variantId, "asset-revision-2",
                    "https://media.example.invalid/variant", "Size M view"));
        }
        return product.withRepresentation(new ProductContent("Black shirt", "A plain black shirt."), media);
    }

    private static UUID id(String value) {
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private static void assertDenied(Outcome outcome, java.util.Set<Reason> reasons, Reason reason) {
        assertThat(outcome).isEqualTo(Outcome.DENIED);
        assertThat(reasons).contains(reason);
    }
}
