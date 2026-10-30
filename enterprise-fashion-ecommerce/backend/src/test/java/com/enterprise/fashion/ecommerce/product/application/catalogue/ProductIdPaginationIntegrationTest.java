package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductIdPaginationIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private ProductPersistenceService service;
    @Autowired private ProductPersistencePort adapter;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;

    @BeforeEach
    void clearSyntheticFixture() {
        jdbc.update("DELETE FROM product.product_variant");
        jdbc.update("DELETE FROM product.product");
    }

    private void insert(String... ids) {
        for (String id : ids) {
            service.create(new Product(new ProductId(id), ProductFamily.ACCESSORIES,
                    ProductAttributeStandard.INITIAL, false, false));
        }
    }

    @Test
    void emptyDatasetReturnsEmptyPageWithoutContinuation() {
        var page = service.pageProductIds(null, null);
        assertThat(page.ids()).isEmpty();
        assertThat(page.pageSize()).isEqualTo(20);
        assertThat(page.nextCursor()).isNull();
    }

    @Test
    void singleProductHasNoContinuationAndRemainsNonVisible() {
        insert("only");
        var page = service.pageProductIds(null, null);
        assertThat(page.ids()).containsExactly(new ProductId("only"));
        assertThat(page.nextCursor()).isNull();
        assertThat(service.load(page.ids().getFirst()).orElseThrow().isCustomerVisible()).isFalse();
    }

    @Test
    void unsignedUtf8OrderingIsExactAcrossCaseDigitsAndPunctuation() {
        insert("a", "_", "Z", "0", "-", "A", "z", "AA", "A_", "A0");
        assertThat(service.pageProductIds(100, null).ids()).extracting(ProductId::value)
                .containsExactly("-", "0", "A", "A0", "AA", "A_", "Z", "_", "a", "z");
    }

    @Test
    void defaultAndMaximumSizesUseLookaheadAndTraverseWithoutDuplicates() {
        insert(IntStream.range(0, 105).mapToObj(i -> "p%03d".formatted(i)).toArray(String[]::new));
        var defaultPage = service.pageProductIds(null, null);
        assertThat(defaultPage.ids()).hasSize(20);
        assertThat(defaultPage.nextCursor().afterExclusive().value()).isEqualTo("p019");
        var first = service.pageProductIds(100, null);
        assertThat(first.ids()).hasSize(100);
        var second = service.pageProductIds(100, first.nextCursor());
        assertThat(second.ids()).hasSize(5);
        assertThat(second.nextCursor()).isNull();
        var all = new ArrayList<>(first.ids());
        all.addAll(second.ids());
        assertThat(all).hasSize(105).doesNotHaveDuplicates();
        assertThat(all).extracting(ProductId::value).isSorted();
    }

    @Test
    void exactFullLastPageDoesNotEmitCursor() {
        insert("A", "B");
        assertThat(service.pageProductIds(2, null).nextCursor()).isNull();
    }

    @Test
    void committedInsertsBetweenPagesRespectLiveExclusiveBoundary() {
        insert("A", "C", "E");
        var first = service.pageProductIds(2, null);
        assertThat(first.ids()).extracting(ProductId::value).containsExactly("A", "C");
        // Independent committed writes between page snapshots: earlier B may be missed.
        insert("B", "D");
        var next = service.pageProductIds(2, first.nextCursor());
        assertThat(next.ids()).extracting(ProductId::value).containsExactly("D", "E");
        assertThat(next.ids()).doesNotContainAnyElementsOf(first.ids());
        assertThat(next.nextCursor()).isNull();
    }

    @Test
    void invalidRequestsAndTransactionContextsFailWithoutRestart() {
        insert("A");
        assertThatThrownBy(() -> service.pageProductIds(101, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.pageProductIds(2, new ProductIdPage.Cursor(new ProductId("A"), 1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> adapter.findProductIdsAfter(null, 21)).isInstanceOf(IllegalStateException.class);
        new TransactionTemplate(manager).executeWithoutResult(status -> {
            assertThatThrownBy(() -> adapter.findProductIdsAfter(null, 21)).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> service.pageProductIds(null, null)).isInstanceOf(IllegalStateException.class);
        });
    }

    @Test
    void databaseFailurePropagatesRatherThanReturningEmptyPage() {
        jdbc.execute("ALTER TABLE product.product RENAME TO unavailable_product");
        try {
            assertThatThrownBy(() -> service.pageProductIds(null, null)).isInstanceOf(DataAccessException.class);
        } finally {
            jdbc.execute("ALTER TABLE product.unavailable_product RENAME TO product");
        }
    }

    @Test
    void traversalDoesNotMutateProductsVariantsOrLifecycle() {
        insert("A", "B");
        jdbc.update("UPDATE product.product SET lifecycle = 'PUBLISHED' WHERE product_id = 'B'");
        jdbc.update("""
                INSERT INTO product.product_variant
                    (variant_id, product_id, family, attribute_standard_version, size_applicable, colour_applicable)
                VALUES ('v1', 'B', 'Accessories', '1.0.0', false, false)
                """);
        List<?> products = jdbc.queryForList("SELECT * FROM product.product ORDER BY product_id");
        List<?> variants = jdbc.queryForList("SELECT * FROM product.product_variant ORDER BY variant_id");
        var first = service.pageProductIds(1, null);
        service.pageProductIds(1, first.nextCursor());
        assertThat(jdbc.queryForList("SELECT * FROM product.product ORDER BY product_id")).isEqualTo(products);
        assertThat(jdbc.queryForList("SELECT * FROM product.product_variant ORDER BY variant_id")).isEqualTo(variants);
    }
}
