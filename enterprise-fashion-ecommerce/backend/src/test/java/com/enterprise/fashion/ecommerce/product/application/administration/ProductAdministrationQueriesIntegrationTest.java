package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.enterprise.fashion.ecommerce.product.adapter.out.persistence.PostgresProductPersistenceAdapter;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductAdministrationQueriesIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired private GetProductForAdministration get;
    @Autowired private ListProductsForAdministration list;
    @Autowired private ProductPersistenceService products;
    @Autowired private UpdateDraftProduct updates;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;
    @MockitoSpyBean private PostgresProductPersistenceAdapter adapter;
    @MockitoSpyBean private JdbcAggregateTemplate aggregates;

    @BeforeEach
    void clearSyntheticProducts() {
        jdbc.update("DELETE FROM product.product_media");
        jdbc.update("DELETE FROM product.product_variant");
        jdbc.update("DELETE FROM product.product");
    }

    private Product fixture(String id, ProductLifecycle lifecycle) {
        Product product = new Product(new ProductId(id), ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, true);
        var variant = new ProductVariant(new ProductVariantId(UUID.randomUUID().toString()), product.id(),
                new VariantConfiguration(product.family(), product.standard(), true, true,
                        new ProductSize(product.family(), "m"), new ProductColour("black")), "Regular", "Cotton");
        var first = new ProductMedia(UUID.randomUUID().toString(), product.id(), variant.id(), "asset-front",
                "https://example.test/front", "PRIVATE front description");
        var second = new ProductMedia(UUID.randomUUID().toString(), product.id(), null, "asset-back",
                "https://example.test/back", "PRIVATE back description");
        product = product.withVariant(variant).withRepresentation(new ProductContent("PRIVATE shirt", "Cotton shirt"), List.of(first, second));
        products.create(product);
        // Synthetic persisted lifecycle fixture, not an authorized transition command.
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle.name(), id);
        return product;
    }

    private ListProductsForAdministration.PageRequest request(Integer size,
            com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage.Cursor cursor) {
        return new ListProductsForAdministration.PageRequest(size, cursor);
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void retrievesCompleteImmutableEditingSnapshotWithoutVisibilityAuthority(ProductLifecycle lifecycle) {
        Product product = fixture("product", lifecycle);
        var view = get.get(product.id());
        assertThat(view.id()).isEqualTo(product.id());
        assertThat(view.lifecycle()).isEqualTo(lifecycle);
        assertThat(view.revision()).isEqualTo(product.revision());
        assertThat(view.content()).isEqualTo(product.content());
        assertThat(view.family()).isEqualTo(product.family());
        assertThat(view.standard()).isEqualTo(product.standard());
        assertThat(view.sizeApplicable()).isTrue();
        assertThat(view.colourApplicable()).isTrue();
        assertThat(view.variants()).extracting(ProductVariant::id).containsExactly(product.variants().getFirst().id());
        assertThat(view.variants().getFirst().configuration()).isEqualTo(product.variants().getFirst().configuration());
        assertThat(view.variants().getFirst().configuration().colour().code()).isEqualTo("BLACK");
        assertThat(view.variants().getFirst().configuration().size().code()).isEqualTo("M");
        assertThat(view.variants().getFirst().fit()).isEqualTo("Regular");
        assertThat(view.variants().getFirst().material()).isEqualTo("Cotton");
        assertThat(view.media()).containsExactlyElementsOf(product.media());
        assertThatThrownBy(() -> view.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> view.media().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(view.toString()).doesNotContain("PRIVATE", "asset-front", product.id().value());
        assertThat(new AssessProductVisibilityReadiness(products).assess(product.id())).isEqualTo(
                lifecycle == ProductLifecycle.PUBLISHED ? ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED : ProductVisibilityReadiness.NOT_VISIBLE);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.evidence_event", Long.class)).isZero();
    }

    @Test
    void missingProductAndEmptyCatalogueAreConfirmedOutcomes() {
        assertThatThrownBy(() -> get.get(new ProductId("missing"))).isInstanceOf(GetProductForAdministration.NotFound.class);
        var page = list.list(request(null, null));
        assertThat(page.items()).isEmpty();
        assertThat(page.pageSize()).isEqualTo(20);
        assertThat(page.nextCursor()).isNull();
    }

    @Test
    void listingIncludesEveryLifecycleInStableByteOrderWithExclusiveContinuation() {
        fixture("a", ProductLifecycle.ARCHIVED);
        fixture("Z", ProductLifecycle.PUBLISHED);
        fixture("A", ProductLifecycle.DRAFT);
        var first = list.list(request(2, null));
        assertThat(first.items()).extracting(view -> view.id().value()).containsExactly("A", "Z");
        assertThat(first.items()).extracting(ProductAdministrationView::lifecycle)
                .containsExactly(ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED);
        var second = list.list(request(2, first.nextCursor()));
        assertThat(second.items()).extracting(view -> view.id().value()).containsExactly("a");
        assertThat(second.items().getFirst().lifecycle()).isEqualTo(ProductLifecycle.ARCHIVED);
        assertThat(second.nextCursor()).isNull();
        assertThatThrownBy(() -> first.items().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void defaultAndMaximumSizesAreBoundedAndBatchLoadingAvoidsNPlusOne() {
        for (int index = 0; index < 25; index++) { fixture("p" + String.format(java.util.Locale.ROOT, "%03d", index), ProductLifecycle.DRAFT); }
        var first = list.list(request(null, null));
        assertThat(first.items()).hasSize(20);
        verify(adapter).loadAll(any());
        verify(adapter, never()).load(any());
        // Exactly three aggregate queries, independent of the 20 Products in this page.
        verify(aggregates, org.mockito.Mockito.times(3)).findAll(any(org.springframework.data.relational.core.query.Query.class), org.mockito.ArgumentMatchers.<Class<Object>>any());
        var second = list.list(request(null, first.nextCursor()));
        assertThat(second.items()).hasSize(5);
        assertThat(second.nextCursor()).isNull();
        assertThat(first.items().stream().map(ProductAdministrationView::id).toList())
                .doesNotContainAnyElementsOf(second.items().stream().map(ProductAdministrationView::id).toList());
        assertThat(list.list(request(100, null)).items()).hasSize(25);
    }

    @Test
    void pageRemainsOneReadOnlySnapshotDuringConcurrentRepresentationChange() {
        Product original = fixture("A", ProductLifecycle.DRAFT);
        AtomicBoolean changed = new AtomicBoolean();
        doAnswer(invocation -> {
            assertThat(jdbc.queryForObject("SHOW transaction_isolation", String.class)).isEqualTo("repeatable read");
            assertThat(jdbc.queryForObject("SHOW transaction_read_only", String.class)).isEqualTo("on");
            if (changed.compareAndSet(false, true)) {
                try (var worker = Executors.newSingleThreadExecutor()) {
                    worker.submit(() -> {
                        updates.update(new UpdateDraftProduct.Command(original.id(), original.revision(),
                                new ProductContent("Changed shirt", "Changed description"), original.variants(), List.of()));
                        fixture("B", ProductLifecycle.DRAFT);
                    }).get(10, TimeUnit.SECONDS);
                }
            }
            return invocation.callRealMethod();
        }).when(adapter).loadAll(any());
        var snapshot = list.list(request(20, null));
        assertThat(snapshot.items()).hasSize(1);
        assertThat(snapshot.items().getFirst().revision()).isEqualTo(original.revision());
        assertThat(snapshot.items().getFirst().content()).isEqualTo(original.content());
        assertThat(snapshot.items().getFirst().media()).containsExactlyElementsOf(original.media());
        assertThat(get.get(original.id()).content().name()).isEqualTo("Changed shirt");
        assertThat(list.list(request(20, null)).items()).hasSize(2);
    }

    @Test
    void corruptRepresentationFailsWholePageWithoutPartialResultsOrNormalization() {
        fixture("A", ProductLifecycle.DRAFT);
        Product broken = fixture("B", ProductLifecycle.DRAFT);
        jdbc.update("UPDATE product.product SET representation_revision = ? WHERE product_id = ?", "v1_" + "0".repeat(64), broken.id().value());
        assertThatThrownBy(() -> get.get(broken.id())).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Invalid stored Product aggregate");
        assertThatThrownBy(() -> list.list(request(20, null))).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Invalid stored Product aggregate");
    }

    @Test
    void databaseFailureCannotBecomeMissingOrEmptySuccess() {
        Product product = fixture("A", ProductLifecycle.DRAFT);
        jdbc.execute("ALTER TABLE product.product RENAME TO unavailable_product");
        try {
            assertThatThrownBy(() -> get.get(product.id())).isInstanceOf(DataAccessException.class);
            assertThatThrownBy(() -> list.list(request(20, null))).isInstanceOf(DataAccessException.class);
        } finally {
            jdbc.execute("ALTER TABLE product.unavailable_product RENAME TO product");
        }
    }

    @Test
    void refusesAmbientTransactionsAndDirectBatchWithoutSnapshot() {
        Product original = fixture("A", ProductLifecycle.DRAFT);
        new TransactionTemplate(manager).executeWithoutResult(status -> {
            assertThatThrownBy(() -> get.get(original.id())).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> list.list(request(null, null))).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> adapter.loadAll(List.of(original.id()))).isInstanceOf(IllegalStateException.class);
        });
        assertThatThrownBy(() -> adapter.loadAll(List.of(original.id()))).isInstanceOf(IllegalStateException.class);
    }
}
