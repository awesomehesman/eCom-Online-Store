package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductRepresentationIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private ProductPersistenceService service;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private Flyway flyway;

    private String id() { return UUID.randomUUID().toString(); }

    private Product product() {
        return new Product(new ProductId(id()), ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL, false, false);
    }

    private ProductMedia media(String mediaId, Product owner, ProductVariantId variant) {
        return new ProductMedia(mediaId, owner.id(), variant, "asset-version-1",
                "https://media.example.invalid/example", "Front view of the accessory");
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void roundTripsRepresentationAndLifecycleWithoutAcceptanceOrVisibility(ProductLifecycle lifecycle) {
        Product original = product();
        var variant = new ProductVariant(new ProductVariantId(id()), original.id(),
                new VariantConfiguration(original.family(), original.standard(), false, false, null, null), null, null);
        original = original.withVariant(variant);
        original = original.withRepresentation(new ProductContent("Accessory", "Synthetic accessory description"),
                List.of(media(id(), original, null), media(id(), original, variant.id())));
        service.create(original);
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle.name(), original.id().value());
        Product restored = service.load(original.id()).orElseThrow();
        assertThat(restored.id()).isEqualTo(original.id());
        assertThat(restored.content()).isEqualTo(original.content());
        assertThat(restored.media()).containsExactlyElementsOf(original.media());
        assertThat(restored.variants()).containsExactly(variant);
        assertThat(restored.revision()).isEqualTo(original.revision());
        assertThat(restored.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.isCustomerVisible()).isFalse();
        assertThat(new AssessProductVisibilityReadiness(service).assess(original.id())).isEqualTo(
                lifecycle == ProductLifecycle.PUBLISHED ? ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED
                        : ProductVisibilityReadiness.NOT_VISIBLE);
    }

    @Test
    void unsupportedRawRepresentationOrReusedRevisionFailsClosed() {
        Product original = product().withRepresentation(new ProductContent("Accessory", "Original description"), List.of());
        service.create(original);
        jdbc.update("UPDATE product.product SET content_description = 'Changed description' WHERE product_id = ?", original.id().value());
        assertThatThrownBy(() -> service.load(original.id())).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(new AssessProductVisibilityReadiness(service).assess(original.id())).isEqualTo(ProductVisibilityReadiness.ERROR);
    }

    @Test
    void changedMediaOrderOrAssetReferenceCannotSilentlyReuseStoredRevision() {
        Product original = product();
        original = original.withRepresentation(null, List.of(media(id(), original, null)));
        service.create(original);
        jdbc.update("UPDATE product.product_media SET asset_revision = 'asset-version-2' WHERE product_id = ?", original.id().value());
        ProductId owner = original.id();
        assertThatThrownBy(() -> service.load(owner)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void databaseRejectsForeignVariantAssociationAndRestrictsIdentityChanges() {
        Product first = product();
        Product second = product();
        var variant = new ProductVariant(new ProductVariantId(id()), first.id(),
                new VariantConfiguration(first.family(), first.standard(), false, false, null, null), null, null);
        service.create(first.withVariant(variant));
        service.create(second);
        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_media VALUES (?, ?, ?, 'asset-1', 'https://example.invalid/a', 'Front view', 0)
                """, id(), second.id().value(), variant.id().value())).isInstanceOf(DataIntegrityViolationException.class);
        Product third = product();
        String mediaId = id();
        service.create(third.withRepresentation(null, List.of(media(mediaId, third, null))));
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product_media SET product_id = ? WHERE media_id = ?",
                second.id().value(), mediaId)).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("DELETE FROM product.product WHERE product_id = ?", third.id().value()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void mediaInsertFailureRollsBackEntireAggregate() {
        Product first = product();
        String shared = id();
        service.create(first.withRepresentation(null, List.of(media(shared, first, null))));
        Product second = product();
        Product attempted = second.withRepresentation(new ProductContent("Accessory", "Description"),
                List.of(media(shared, second, null)));
        assertThatThrownBy(() -> service.create(attempted)).isInstanceOf(DbActionExecutionException.class);
        assertThat(service.load(second.id())).isEmpty();
    }

    @Test
    void databaseRequiresPairedNonblankContentAndRevision() {
        Product original = product();
        service.create(original);
        for (String sql : new String[]{"content_name = 'Name'", "content_name = '', content_description = 'Description'",
                "content_name = 'Name', content_description = 'Description', representation_revision = NULL",
                "representation_revision = 'invalid'"}) {
            assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET " + sql + " WHERE product_id = ?", original.id().value()))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
        flyway.validate();
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("6");
    }

    @Test
    void additiveMigrationPreservesRealV4RowsWithoutFabricatedContent() {
        jdbc.execute("CREATE DATABASE representation_upgrade");
        String url = "jdbc:postgresql://" + POSTGRESQL.getHost() + ":" + POSTGRESQL.getMappedPort(5432) + "/representation_upgrade";
        var source = new DriverManagerDataSource(url, POSTGRESQL.getUsername(), POSTGRESQL.getPassword());
        Flyway.configure().dataSource(source).target("4").load().migrate();
        JdbcTemplate legacy = new JdbcTemplate(source);
        legacy.update("INSERT INTO product.product VALUES ('legacy', 'PUBLISHED', 'Accessories', '1.0.0', false, false)");
        Flyway.configure().dataSource(source).load().migrate();
        assertThat(legacy.queryForMap("SELECT * FROM product.product WHERE product_id = 'legacy'"))
                .containsEntry("lifecycle", "PUBLISHED").containsEntry("content_name", null)
                .containsEntry("content_description", null).containsEntry("representation_revision", null);
        // Also exercise adapter reconstruction of the unbackfilled shape on its normal connection.
        jdbc.update("INSERT INTO product.product VALUES ('legacy', 'PUBLISHED', 'Accessories', '1.0.0', false, false)");
        Product restored = service.load(new ProductId("legacy")).orElseThrow();
        assertThat(restored.content()).isNull();
        assertThat(restored.media()).isEmpty();
        assertThat(restored.revision()).isNotNull();
        assertThat(restored.lifecycle()).isEqualTo(ProductLifecycle.PUBLISHED);
        assertThat(restored.isCustomerVisible()).isFalse();
    }
}
