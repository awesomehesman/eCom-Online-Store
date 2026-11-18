package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueQueryService;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest
@Testcontainers
class PublishProductIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private CreateDraftProduct createDraft;
    @Autowired private ProductPersistenceService persistence;
    @Autowired private ProductPersistencePort persistencePort;
    @Autowired private AssessProductPublicationReadiness readiness;
    @Autowired private ProductCatalogueQueryService catalogueQuery;
    @Autowired private JdbcClient jdbc;
    @Autowired private PlatformTransactionManager transactionManager;

    private TransactionOperations writes;
    private PublishProduct publishProduct;

    @BeforeEach
    void setUp() {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        writes = template;
        publishProduct = new PublishProduct(persistencePort, readiness, writes);
    }

    private ProductId id() { return new ProductId(UUID.randomUUID().toString()); }

    private ProductVariant variant(ProductId owner, String variantId) {
        return new ProductVariant(new ProductVariantId(variantId), owner,
                new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                        false, true, null, new ProductColour("BLACK")), null, null);
    }

    private ProductMedia media(ProductId owner, String mediaId, ProductVariantId variantId) {
        return new ProductMedia(mediaId, owner, variantId, "asset-v1", "https://example.test/image", "Front view");
    }

    private CreateDraftProduct.Command createCommand(ProductId id, List<ProductVariant> variants, List<ProductMedia> media) {
        return new CreateDraftProduct.Command(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                false, true, variants, new ProductContent("Test Product", "A test description"), media);
    }

    private PublishProduct.Command publishCommand(ProductId id) {
        return new PublishProduct.Command(id, UUID.randomUUID());
    }

    // === Default Evidence Blocks Publication ===

    @Test
    void defaultUnavailableEvidencePreventsPublication() {
        ProductId id = id();
        var variant = variant(id, UUID.randomUUID().toString());
        var media = media(id, UUID.randomUUID().toString(), null);
        createDraft.create(createCommand(id, List.of(variant), List.of(media)));

        var result = publishProduct.publish(publishCommand(id));

        // Default configuration has unavailable evidence adapter
        assertThat(result).isInstanceOf(PublishProduct.EvidenceUnavailable.class);
        // Verify no lifecycle change occurred
        Product loaded = persistence.load(id).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
    }

    // === Lifecycle Persistence Behavior ===

    @Test
    void lifecycleUpdateModifiesOnlyLifecycleColumn() {
        ProductId id = id();
        var variant = variant(id, UUID.randomUUID().toString());
        var media = media(id, UUID.randomUUID().toString(), null);
        createDraft.create(createCommand(id, List.of(variant), List.of(media)));
        Product before = persistence.load(id).orElseThrow();

        // Directly update lifecycle using port (bypassing evidence for this specific test)
        boolean updated = writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));

        assertThat(updated).isTrue();
        Product after = persistence.load(id).orElseThrow();
        assertThat(after.lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
        // Verify other fields unchanged
        assertThat(after.id()).isEqualTo(before.id());
        assertThat(after.family()).isEqualTo(before.family());
        assertThat(after.standard()).isEqualTo(before.standard());
        assertThat(after.content()).isEqualTo(before.content());
        assertThat(after.media()).isEqualTo(before.media());
        assertThat(after.variants()).isEqualTo(before.variants());
        assertThat(after.revision()).isEqualTo(before.revision());
    }

    @Test
    void lifecycleUpdateFailsOnLifecycleMismatch() {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));

        // Try to update from PUBLISHED when it's actually DRAFT
        boolean updated = writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.PUBLISHED, ProductLifecycle.ARCHIVED));

        assertThat(updated).isFalse();
        // Verify unchanged
        Product loaded = persistence.load(id).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
    }

    @Test
    void lifecycleUpdateFailsForNonexistentProduct() {
        ProductId nonexistent = new ProductId("nonexistent-" + UUID.randomUUID());

        boolean updated = writes.execute(status -> persistencePort.updateLifecycle(nonexistent, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));

        assertThat(updated).isFalse();
    }

    // === V4 Lifecycle Constraint ===

    @Test
    void v4ConstraintAllowsAllValidLifecycleValues() {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));

        // DRAFT → PUBLISHED
        Boolean updated1 = writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));
        assertThat(updated1).isTrue();
        assertThat(persistence.load(id).orElseThrow().lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);

        // PUBLISHED → ARCHIVED
        Boolean updated2 = writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.PUBLISHED, ProductLifecycle.ARCHIVED));
        assertThat(updated2).isTrue();
        assertThat(persistence.load(id).orElseThrow().lifecycle()).isEqualTo(ProductLifecycle.ARCHIVED);

        // All values permitted by V4 CHECK constraint
        String lifecycle = jdbc.sql("SELECT lifecycle FROM product.product WHERE product_id = ?")
                .param(id.value())
                .query(String.class)
                .single();
        assertThat(lifecycle).isEqualTo("ARCHIVED");
    }

    // === No Accidental Catalogue Visibility ===

    @Test
    void publishedProductNotVisibleInCatalogue() {
        ProductId id = id();
        var variant = variant(id, UUID.randomUUID().toString());
        var media = media(id, UUID.randomUUID().toString(), null);
        createDraft.create(createCommand(id, List.of(variant), List.of(media)));

        // Directly transition to PUBLISHED
        Boolean updated = writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));
        assertThat(updated).isTrue();

        // Verify PUBLISHED in database
        Product loaded = persistence.load(id).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
        assertThat(loaded.isCustomerVisible()).isFalse();

        // Verify visibility readiness still blocks (not ERROR, but also not positive)
        ProductVisibilityReadiness visibility = new AssessProductVisibilityReadiness(persistence).assess(id);
        // Evidence is unconfirmed — no positive VISIBLE outcome exists
        assertThat(visibility).isIn(ProductVisibilityReadiness.NOT_VISIBLE, ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);

        // Verify catalogue query service still blocks
        var catalogueResult = catalogueQuery.list(20);
        assertThat(catalogueResult).isInstanceOf(ProductCatalogueQueryService.AuthorityUnavailable.class);
    }

    // === Concurrency ===

    @Test
    void concurrentLifecycleUpdatesPreventLostUpdates() throws InterruptedException {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));

        int threads = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    ready.countDown();
                    start.await();
                    boolean updated = writes.execute(status ->
                            persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));
                    if (updated) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception ignored) {
                    // CAS failure is expected
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown(); // Release all threads
        done.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // Exactly one should succeed
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(persistence.load(id).orElseThrow().lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
    }

    // === Use Case Returns NotFound ===

    @Test
    void useCaseReturnsNotFoundForMissingProduct() {
        ProductId nonexistent = new ProductId("nonexistent-" + UUID.randomUUID());

        var result = publishProduct.publish(publishCommand(nonexistent));

        assertThat(result).isInstanceOf(PublishProduct.NotFound.class);
    }

    // === Use Case Returns NotDraft ===

    @Test
    void useCaseReturnsNotDraftForPublishedProduct() {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));
        writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));

        var result = publishProduct.publish(publishCommand(id));

        assertThat(result).isInstanceOf(PublishProduct.NotDraft.class);
        assertThat(((PublishProduct.NotDraft) result).actual()).isEqualTo(ProductLifecycle.PUBLISHED);
    }

    @Test
    void useCaseReturnsNotDraftForArchivedProduct() {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));
        writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED));
        writes.execute(status -> persistencePort.updateLifecycle(id, ProductLifecycle.PUBLISHED, ProductLifecycle.ARCHIVED));

        var result = publishProduct.publish(publishCommand(id));

        assertThat(result).isInstanceOf(PublishProduct.NotDraft.class);
        assertThat(((PublishProduct.NotDraft) result).actual()).isEqualTo(ProductLifecycle.ARCHIVED);
    }

    // === Rollback on Failure ===

    @Test
    void failedPublicationLeavesProductUnchanged() {
        ProductId id = id();
        createDraft.create(createCommand(id, List.of(), List.of()));
        Product before = persistence.load(id).orElseThrow();

        // Publication fails due to unavailable evidence
        var result = publishProduct.publish(publishCommand(id));

        assertThat(result).isInstanceOf(PublishProduct.EvidenceUnavailable.class);
        Product after = persistence.load(id).orElseThrow();
        assertThat(after.lifecycle()).isEqualTo(before.lifecycle());
        assertThat(after.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
    }
}
