package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
class ProductVisibilityReadinessIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private ProductPersistenceService persistence;
    @Autowired private ProductPersistencePort adapter;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;

    private AssessProductVisibilityReadiness evaluator() {
        return new AssessProductVisibilityReadiness(persistence);
    }

    private Product product(boolean withVariant) {
        Product product = new Product(new ProductId(UUID.randomUUID().toString()), ProductFamily.APPAREL,
                ProductAttributeStandard.INITIAL, true, true);
        if (withVariant) {
            var config = new VariantConfiguration(product.family(), product.standard(), true, true,
                    ProductSize.apparel("M"), new ProductColour("BLACK"));
            product = product.withVariant(new ProductVariant(new ProductVariantId(UUID.randomUUID().toString()),
                    product.id(), config, "Regular", "Cotton"));
        }
        return product;
    }

    @Test
    void unknownProductRemainsAbsentWithoutCreatingRows() {
        ProductId unknown = new ProductId(UUID.randomUUID().toString());
        var before = jdbc.queryForList("SELECT * FROM product.product");
        assertThat(evaluator().assess(unknown)).isEqualTo(ProductVisibilityReadiness.NOT_FOUND);
        assertThat(persistence.load(unknown)).isEmpty();
        assertThat(jdbc.queryForList("SELECT * FROM product.product")).isEqualTo(before);
    }

    @ParameterizedTest
    @CsvSource({"DRAFT,false,NOT_VISIBLE", "DRAFT,true,NOT_VISIBLE",
            "ARCHIVED,false,NOT_VISIBLE", "ARCHIVED,true,NOT_VISIBLE",
            "PUBLISHED,false,EVIDENCE_UNCONFIRMED", "PUBLISHED,true,EVIDENCE_UNCONFIRMED"})
    void evaluatesReconstructedPostgresStateWithoutWritesOrVisibility(ProductLifecycle lifecycle,
            boolean withVariant, ProductVisibilityReadiness expected) {
        Product original = product(withVariant);
        persistence.create(original);
        // Synthetic prior state fixture; no lifecycle command is introduced.
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle.name(), original.id().value());
        var productBefore = jdbc.queryForList("SELECT * FROM product.product WHERE product_id = ?", original.id().value());
        var variantsBefore = jdbc.queryForList("SELECT * FROM product.product_variant WHERE product_id = ?", original.id().value());
        assertThat(evaluator().assess(original.id())).isEqualTo(expected);
        Product restored = persistence.load(original.id()).orElseThrow();
        assertThat(restored.id()).isEqualTo(original.id());
        assertThat(restored.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.variants()).containsExactlyElementsOf(original.variants());
        assertThat(restored.isCustomerVisible()).isFalse();
        assertThat(jdbc.queryForList("SELECT * FROM product.product WHERE product_id = ?", original.id().value()))
                .isEqualTo(productBefore);
        assertThat(jdbc.queryForList("SELECT * FROM product.product_variant WHERE product_id = ?", original.id().value()))
                .isEqualTo(variantsBefore);
    }

    @Test
    void invalidStoredRepresentationIsAnErrorAndIsNotRepaired() {
        Product original = product(false);
        persistence.create(original);
        // Isolated container corruption fixture, restored in finally. The evaluator owns its
        // normal read transaction so this exercises raw-value validation through reconstruction.
        jdbc.execute("ALTER TABLE product.product DROP CONSTRAINT product_lifecycle_check");
        try {
            jdbc.update("UPDATE product.product SET lifecycle = 'published' WHERE product_id = ?", original.id().value());
            assertThat(evaluator().assess(original.id())).isEqualTo(ProductVisibilityReadiness.ERROR);
            assertThat(jdbc.queryForObject("SELECT lifecycle FROM product.product WHERE product_id = ?",
                    String.class, original.id().value())).isEqualTo("published");
        } finally {
            jdbc.update("UPDATE product.product SET lifecycle = 'DRAFT' WHERE product_id = ?", original.id().value());
            jdbc.execute("ALTER TABLE product.product ADD CONSTRAINT product_lifecycle_check "
                    + "CHECK (lifecycle IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))");
        }
    }

    @Test
    void assessmentCannotBypassExistingTransactionSafeguards() {
        Product original = product(true);
        persistence.create(original);
        assertThatThrownBy(() -> adapter.load(original.id())).isInstanceOf(IllegalStateException.class);
        new TransactionTemplate(manager).executeWithoutResult(status -> {
            assertThat(evaluator().assess(original.id())).isEqualTo(ProductVisibilityReadiness.ERROR);
            assertThatThrownBy(() -> adapter.load(original.id())).isInstanceOf(IllegalStateException.class);
        });
        assertThat(evaluator().assess(original.id())).isEqualTo(ProductVisibilityReadiness.NOT_VISIBLE);
    }
}
