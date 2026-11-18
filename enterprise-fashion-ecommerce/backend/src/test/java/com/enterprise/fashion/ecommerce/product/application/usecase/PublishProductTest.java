package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.ContentAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaAssociationAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSecurityCheck;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaSubject;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.MediaUsageRightsAcceptance;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Result;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Source;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Validity;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidencePolicy;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PublishProductTest {
    private static final ProductId PRODUCT_ID = new ProductId("test-product");
    private static final UUID STAFF_ID = UUID.randomUUID();
    private static final Source CONTENT_SOURCE = new Source("product-owner", "content-acceptance", "1.0");
    private static final Source RIGHTS_SOURCE = new Source("product-owner", "media-rights", "1.0");
    private static final Source SECURITY_SOURCE = new Source("security-checker", "image-safety", "1.0");
    private static final Source ASSOCIATION_SOURCE = new Source("product-service", "media-association", "1.0");

    private TestPersistence persistence;
    private TestEvidencePort evidencePort;
    private TestEvidencePolicy evidencePolicy;
    private AssessProductPublicationReadiness readiness;
    private TestTransactions transactions;
    private PublishProduct useCase;

    @BeforeEach
    void setUp() {
        persistence = new TestPersistence();
        evidencePort = new TestEvidencePort();
        evidencePolicy = new TestEvidencePolicy();
        readiness = new AssessProductPublicationReadiness(evidencePort, evidencePolicy);
        transactions = new TestTransactions();
        useCase = new PublishProduct(persistence, readiness, transactions);
    }

    // === Success Path ===

    @Test
    void publishesReadyDraftProduct() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(validEvidence(draft));
        evidencePolicy.setSupportsAll(true);
        persistence.setUpdateSuccess(true);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.Published.class);
        var published = (PublishProduct.Published) result;
        assertThat(published.id()).isEqualTo(PRODUCT_ID);
        assertThat(published.lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
    }

    @Test
    void readyEvidencePermitsTransitionInIsolatedTest() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(validEvidence(draft));
        evidencePolicy.setSupportsAll(true);
        persistence.setUpdateSuccess(true);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.Published.class);
        assertThat(persistence.wasUpdateLifecycleCalled()).isTrue();
        assertThat(persistence.getLastUpdateExpected()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(persistence.getLastUpdateTarget()).isEqualTo(ProductLifecycle.PUBLISHED);
    }

    // === Not Found ===

    @Test
    void returnsNotFoundForMissingProduct() {
        persistence.setProduct(null);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.NotFound.class);
        assertThat(evidencePort.wasQueried()).isFalse();
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    // === Invalid Lifecycle ===

    @Test
    void returnsNotDraftForPublishedProduct() {
        Product published = publishedProduct();
        persistence.setProduct(published);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.NotDraft.class);
        var notDraft = (PublishProduct.NotDraft) result;
        assertThat(notDraft.actual()).isEqualTo(ProductLifecycle.PUBLISHED);
        assertThat(evidencePort.wasQueried()).isFalse();
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    @Test
    void returnsNotDraftForArchivedProduct() {
        Product archived = archivedProduct();
        persistence.setProduct(archived);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.NotDraft.class);
        var notDraft = (PublishProduct.NotDraft) result;
        assertThat(notDraft.actual()).isEqualTo(ProductLifecycle.ARCHIVED);
        assertThat(evidencePort.wasQueried()).isFalse();
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    // === Evidence Failures ===

    @Test
    void returnsEvidenceUnavailableWhenProviderIsUnavailable() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(Snapshot.unavailable());

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.EvidenceUnavailable.class);
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    @Test
    void returnsConflictingEvidenceOnEvidenceConflict() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(new Snapshot(Availability.CONFLICTING, List.of(), List.of(), List.of(), List.of(), List.of()));

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.ConflictingEvidence.class);
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    @Test
    void returnsNotReadyWhenReadinessDenied() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        // Evidence available but content acceptance missing
        evidencePort.setSnapshot(new Snapshot(Availability.AVAILABLE, List.of(), List.of(), List.of(), List.of(), List.of()));
        evidencePolicy.setSupportsAll(true);

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.NotReady.class);
        var notReady = (PublishProduct.NotReady) result;
        assertThat(notReady.reasons()).contains(Reason.CONTENT_ACCEPTANCE_MISSING_OR_STALE);
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    // === No Write on Non-READY ===

    @Test
    void noWriteOccursWhenEvidenceNotReady() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(new Snapshot(Availability.AVAILABLE, List.of(), List.of(), List.of(), List.of(), List.of()));
        evidencePolicy.setSupportsAll(true);

        useCase.publish(command());

        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    @Test
    void noWriteOccursWhenEvidenceUnavailable() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(Snapshot.unavailable());

        useCase.publish(command());

        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    // === No Mutation Before Readiness Succeeds ===

    @Test
    void readinessEvaluatedBeforeLifecycleChange() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(new Snapshot(Availability.AVAILABLE, List.of(), List.of(), List.of(), List.of(), List.of()));
        evidencePolicy.setSupportsAll(true);

        useCase.publish(command());

        // Evidence was queried (readiness was evaluated)
        assertThat(evidencePort.wasQueried()).isTrue();
        // But no persistence update occurred (readiness not READY)
        assertThat(persistence.wasUpdateLifecycleCalled()).isFalse();
    }

    // === Concurrency Conflict ===

    @Test
    void returnsConcurrencyConflictOnCasFailure() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(validEvidence(draft));
        evidencePolicy.setSupportsAll(true);
        persistence.setUpdateSuccess(false); // CAS fails

        var result = useCase.publish(command());

        assertThat(result).isInstanceOf(PublishProduct.ConcurrencyConflict.class);
    }

    // === Input Validation ===

    @Test
    void rejectsNullCommand() {
        assertThatNullPointerException()
                .isThrownBy(() -> useCase.publish(null))
                .withMessageContaining("command");
    }

    @Test
    void rejectsNullProductId() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PublishProduct.Command(null, STAFF_ID))
                .withMessageContaining("Product identity");
    }

    @Test
    void rejectsNullStaffPrincipalId() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PublishProduct.Command(PRODUCT_ID, null))
                .withMessageContaining("Staff principal");
    }

    // === Result Validation ===

    @Test
    void publishedResultRejectsNonPublishedLifecycle() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new PublishProduct.Published(PRODUCT_ID, ProductLifecycle.DRAFT))
                .withMessageContaining("PUBLISHED");
    }

    @Test
    void notDraftResultRejectsDraftLifecycle() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new PublishProduct.NotDraft(ProductLifecycle.DRAFT))
                .withMessageContaining("DRAFT");
    }

    @Test
    void notReadyResultRejectsEmptyReasons() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new PublishProduct.NotReady(Set.of()))
                .withMessageContaining("at least one reason");
    }

    @Test
    void notReadyReasonsAreImmutable() {
        var result = new PublishProduct.NotReady(Set.of(Reason.EVIDENCE_CONFLICT));
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.reasons().add(Reason.PRODUCT_LEVEL_IMAGE_REQUIRED));
    }

    // === Immutability ===

    @Test
    void publishDoesNotMutateOriginalProduct() {
        Product draft = draftProduct();
        ProductLifecycle beforeLifecycle = draft.lifecycle();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(validEvidence(draft));
        evidencePolicy.setSupportsAll(true);
        persistence.setUpdateSuccess(true);

        useCase.publish(command());

        // Original product unchanged
        assertThat(draft.lifecycle()).isEqualTo(beforeLifecycle);
        assertThat(draft.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
    }

    // === Command toString Safety ===

    @Test
    void commandToStringConcealsInternals() {
        var command = command();
        assertThat(command.toString()).doesNotContain(PRODUCT_ID.value());
        assertThat(command.toString()).doesNotContain(STAFF_ID.toString());
        assertThat(command.toString()).contains("internal");
    }

    // === Visibility ===

    @Test
    void publishedProductStillNotCustomerVisible() {
        Product draft = draftProduct();
        persistence.setProduct(draft);
        evidencePort.setSnapshot(validEvidence(draft));
        evidencePolicy.setSupportsAll(true);
        persistence.setUpdateSuccess(true);

        useCase.publish(command());

        // The domain publish() does not grant visibility
        Product afterDomainTransition = draft.publish();
        assertThat(afterDomainTransition.isCustomerVisible()).isFalse();
    }

    // === Helpers ===

    private PublishProduct.Command command() {
        return new PublishProduct.Command(PRODUCT_ID, STAFF_ID);
    }

    private Product draftProduct() {
        var variant = new ProductVariant(new ProductVariantId("variant-1"), PRODUCT_ID,
                new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                        true, false, ProductSize.apparel("M"), null), null, null);
        var media = new ProductMedia("front", PRODUCT_ID, null, "asset-v1",
                "https://example.test/image", "Front view");
        return new Product(PRODUCT_ID, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false)
                .withVariant(variant)
                .withRepresentation(new ProductContent("Test Shirt", "A test shirt"), List.of(media));
    }

    private Product publishedProduct() {
        Product draft = draftProduct();
        return Product.reconstruct(draft.id(), draft.family(), draft.standard(),
                draft.sizeApplicable(), draft.colourApplicable(), ProductLifecycle.PUBLISHED,
                draft.variants(), draft.content(), draft.media(), draft.revision());
    }

    private Product archivedProduct() {
        Product draft = draftProduct();
        return Product.reconstruct(draft.id(), draft.family(), draft.standard(),
                draft.sizeApplicable(), draft.colourApplicable(), ProductLifecycle.ARCHIVED,
                draft.variants(), draft.content(), draft.media(), draft.revision());
    }

    // === Test Doubles ===

    private static class TestPersistence implements ProductPersistencePort {
        private Product product;
        private boolean updateSuccess;
        private boolean updateLifecycleCalled;
        private ProductLifecycle lastUpdateExpected;
        private ProductLifecycle lastUpdateTarget;

        void setProduct(Product product) { this.product = product; }
        void setUpdateSuccess(boolean success) { this.updateSuccess = success; }
        boolean wasUpdateLifecycleCalled() { return updateLifecycleCalled; }
        ProductLifecycle getLastUpdateExpected() { return lastUpdateExpected; }
        ProductLifecycle getLastUpdateTarget() { return lastUpdateTarget; }

        @Override
        public Optional<Product> load(ProductId id) {
            return Optional.ofNullable(product);
        }

        @Override
        public boolean updateLifecycle(ProductId id, ProductLifecycle expected, ProductLifecycle target) {
            updateLifecycleCalled = true;
            lastUpdateExpected = expected;
            lastUpdateTarget = target;
            return updateSuccess;
        }

        @Override public void create(Product product) { throw new UnsupportedOperationException(); }
        @Override public boolean replaceDraft(Product replacement, ProductRevision expectedRevision) { throw new UnsupportedOperationException(); }
        @Override public List<Product> loadAll(List<ProductId> ids) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findProductIdsAfter(ProductId afterExclusive, int limit) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findCatalogueCandidateIdsAfter(ProductId afterExclusive, int limit) { throw new UnsupportedOperationException(); }
    }

    private static class TestEvidencePort implements ProductPublicationEvidencePort {
        private Snapshot snapshot;
        private boolean queried;

        void setSnapshot(Snapshot snapshot) { this.snapshot = snapshot; }
        boolean wasQueried() { return queried; }

        @Override
        public Snapshot currentEvidence(ProductId productId, ProductRevision productRevision) {
            queried = true;
            return snapshot;
        }
    }

    private static class TestEvidencePolicy implements ProductPublicationEvidencePolicy {
        private boolean supportsAll;

        void setSupportsAll(boolean supportsAll) { this.supportsAll = supportsAll; }

        @Override
        public boolean supports(Kind kind, Source source) {
            return supportsAll;
        }
    }

    private static class TestTransactions implements TransactionOperations {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T execute(TransactionCallback<T> action) {
            return action.doInTransaction(null);
        }
    }

    private Snapshot validEvidence(Product product) {
        var contentAcceptance = new ContentAcceptance(
                UUID.randomUUID(),
                PRODUCT_ID,
                product.revision(),
                CONTENT_SOURCE,
                Result.ACCEPTED,
                Validity.CURRENT
        );
        // ProductMedia in draftProduct(): id="front", assetRevision="asset-v1", variantId=null
        // MediaSubject must match: mediaAssociationId=media.id, assetRevision=media.assetRevision, 
        //                          associationRevision=product.revision, variantId=media.variantId
        ProductMedia media = product.media().getFirst();
        var mediaSubject = new MediaSubject(
                media.id(),
                "asset-id-1",  // assetId - not checked in predicate but required by record
                media.assetRevision(),
                product.revision(),
                media.variantId()
        );
        var usageRights = new MediaUsageRightsAcceptance(
                UUID.randomUUID(),
                PRODUCT_ID,
                product.revision(),
                mediaSubject,
                RIGHTS_SOURCE,
                Result.ACCEPTED,
                Validity.CURRENT
        );
        var securityCheck = new MediaSecurityCheck(
                UUID.randomUUID(),
                PRODUCT_ID,
                product.revision(),
                mediaSubject,
                SECURITY_SOURCE,
                Result.ACCEPTED,
                Validity.CURRENT
        );
        var association = new MediaAssociationAcceptance(
                UUID.randomUUID(),
                PRODUCT_ID,
                product.revision(),
                mediaSubject,
                ASSOCIATION_SOURCE,
                Result.ACCEPTED,
                Validity.CURRENT
        );
        return new Snapshot(
                Availability.AVAILABLE,
                List.of(contentAcceptance),
                List.of(usageRights),
                List.of(securityCheck),
                List.of(association),
                List.of()
        );
    }
}
