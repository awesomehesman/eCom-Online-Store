package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
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
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductPersistenceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private ProductPersistenceService persistence;
    @Autowired private ProductPersistencePort adapter;
    @MockitoSpyBean private JdbcAggregateTemplate aggregates;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;

    private String id() { return UUID.randomUUID().toString(); }

    private Product product(ProductFamily family, boolean sized, boolean coloured) {
        return new Product(new ProductId(id()), family, ProductAttributeStandard.INITIAL, sized, coloured);
    }

    private ProductVariant variant(Product product, String variantId, String size) {
        var config = new VariantConfiguration(product.family(), product.standard(), product.sizeApplicable(),
                product.colourApplicable(), size == null ? null : new ProductSize(product.family(), size),
                product.colourApplicable() ? new ProductColour("black") : null);
        return new ProductVariant(new ProductVariantId(variantId), product.id(), config, "Regular", "Cotton");
    }

    @Test
    void createsAndLoadsProductWithoutVariantsAndReturnsAbsenceForUnknownIdentity() {
        Product original = product(ProductFamily.APPAREL, true, true);
        assertThat(persistence.load(original.id())).isEmpty();
        persistence.create(original);
        Product loaded = persistence.load(original.id()).orElseThrow();
        assertThat(loaded.id()).isEqualTo(original.id());
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(loaded.variants()).isEmpty();
        assertThat(loaded.isCustomerVisible()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"Apparel,true,true,M", "Footwear,true,true,3.5", "Footwear,true,false,13",
            "Accessories,true,true,ONE_SIZE", "Accessories,true,false,ONE_SIZE",
            "Accessories,false,true,", "Accessories,false,false,"})
    void roundTripsCanonicalConfigurationContextAndDescriptions(String family, boolean sized, boolean coloured, String size) {
        Product original = product(new ProductFamily(family), sized, coloured);
        ProductVariant accepted = variant(original, id(), size);
        original = original.withVariant(accepted);
        persistence.create(original);
        Product loaded = persistence.load(original.id()).orElseThrow();
        assertThat(loaded.id()).isEqualTo(original.id());
        assertThat(loaded.family()).isEqualTo(original.family());
        assertThat(loaded.standard()).isEqualTo(original.standard());
        assertThat(loaded.sizeApplicable()).isEqualTo(sized);
        assertThat(loaded.colourApplicable()).isEqualTo(coloured);
        assertThat(loaded.variants()).hasSize(1);
        ProductVariant actual = loaded.variants().getFirst();
        assertThat(actual.id()).isEqualTo(accepted.id());
        assertThat(actual.productId()).isEqualTo(original.id());
        assertThat(actual.configuration()).isEqualTo(accepted.configuration());
        assertThat(actual.fit()).isEqualTo("Regular");
        assertThat(actual.material()).isEqualTo("Cotton");
        assertThatThrownBy(() -> loaded.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void multipleVariantsSurviveRoundTrip() {
        Product original = product(ProductFamily.APPAREL, true, true);
        original = original.withVariant(variant(original, id(), "M")).withVariant(variant(original, id(), "L"));
        persistence.create(original);
        Product loaded = persistence.load(original.id()).orElseThrow();
        assertThat(loaded.variants()).containsExactlyInAnyOrderElementsOf(original.variants());
        assertThat(loaded.variants()).extracting(v -> v.configuration().size().code()).containsExactlyInAnyOrder("M", "L");
    }

    @Test
    void duplicateProductNeverOverwritesExistingState() {
        Product original = product(ProductFamily.APPAREL, true, true);
        persistence.create(original);
        Product duplicate = original.withVariant(variant(original, id(), "M"));
        assertThatThrownBy(() -> persistence.create(duplicate)).isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        assertThat(persistence.load(original.id()).orElseThrow().variants()).isEmpty();
    }

    @Test
    void duplicateVariantAcrossProductsRollsBackParentAndEarlierVariantInsertions() {
        Product first = product(ProductFamily.APPAREL, true, true);
        String sharedId = id();
        first = first.withVariant(variant(first, sharedId, "M"));
        persistence.create(first);
        Product second = product(ProductFamily.APPAREL, true, true);
        String earlierId = id();
        second = second.withVariant(variant(second, earlierId, "L")).withVariant(variant(second, sharedId, "M"));
        Product attempted = second;
        assertThatThrownBy(() -> persistence.create(attempted)).isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        assertThat(persistence.load(second.id())).isEmpty();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_variant WHERE variant_id = ?",
                Integer.class, earlierId)).isZero();
        assertThat(persistence.load(first.id()).orElseThrow().variants().getFirst().productId()).isEqualTo(first.id());
    }

    @Test
    void independentProductsAllowEquivalentConfigurationsButAggregateRejectsCanonicalDuplicates() {
        Product first = product(ProductFamily.APPAREL, true, true);
        first = first.withVariant(variant(first, id(), "M"));
        Product second = product(ProductFamily.APPAREL, true, true);
        second = second.withVariant(variant(second, id(), "m"));
        persistence.create(first);
        persistence.create(second);
        Product loaded = persistence.load(first.id()).orElseThrow();
        ProductVariant duplicate = variant(loaded, id(), "m");
        assertThatThrownBy(() -> loaded.withVariant(duplicate)).isInstanceOf(IllegalArgumentException.class);
        assertThat(persistence.load(second.id()).orElseThrow().variants()).hasSize(1);
        // Also exercise the database constraint against an out-of-band duplicate insertion.
        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO product.product_variant SELECT ?, product_id, family, attribute_standard_version,
                    size_applicable, colour_applicable, size_code, colour_code, fit, material
                FROM product.product_variant WHERE product_id = ?
                """, id(), loaded.id().value())).isInstanceOf(DataIntegrityViolationException.class);
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void storedLifecycleIsPreservedWithoutVisibilityAuthority(ProductLifecycle lifecycle) {
        Product original = product(ProductFamily.APPAREL, true, true);
        original = original.withVariant(variant(original, id(), "M"));
        persistence.create(original);
        // Synthetic historical state fixture; the persistence port exposes no update operation.
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle.name(), original.id().value());
        Product loaded = persistence.load(original.id()).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(lifecycle);
        assertThat(loaded.variants()).containsExactlyElementsOf(original.variants());
        assertThat(loaded.isCustomerVisible()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"size_code,m,product_variant_size_check", "size_code,3.0,product_variant_size_check",
            "colour_code,Black,product_variant_colour_check", "colour_code,BLUE,product_variant_colour_check"})
    void invalidRawVariantRepresentationsFailClosedBeforeNormalization(String column, String value, String constraint) {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.executeWithoutResult(status -> {
            status.setRollbackOnly();
            Product original = product(ProductFamily.APPAREL, true, true);
            original = original.withVariant(variant(original, id(), "M"));
            adapter.create(original);
            // Privileged corruption fixture only; DDL and data are rolled back together.
            jdbc.execute("ALTER TABLE product.product_variant DROP CONSTRAINT " + constraint);
            jdbc.update("UPDATE product.product_variant SET " + column + " = ? WHERE product_id = ?", value, original.id().value());
            ProductId owner = original.id();
            assertThatThrownBy(() -> adapter.load(owner)).isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessage("Invalid stored Product aggregate");
        });
    }

    @ParameterizedTest
    @CsvSource({"lifecycle,published,product_lifecycle_check", "family,apparel,product_family_check",
            "attribute_standard_version,2.0.0,product_attribute_standard_version_check"})
    void invalidRawProductContextFailsClosed(String column, String value, String constraint) {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.executeWithoutResult(status -> {
            status.setRollbackOnly();
            Product original = product(ProductFamily.APPAREL, true, true);
            adapter.create(original);
            jdbc.execute("ALTER TABLE product.product DROP CONSTRAINT " + constraint);
            jdbc.update("UPDATE product.product SET " + column + " = ? WHERE product_id = ?", value, original.id().value());
            assertThatThrownBy(() -> adapter.load(original.id())).isInstanceOf(DataIntegrityViolationException.class);
        });
    }

    @Test
    void directAdapterCallsRequireTransactionsAndDoNotCreatePartialState() {
        Product original = product(ProductFamily.APPAREL, true, true);
        assertThatThrownBy(() -> adapter.create(original)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> adapter.load(original.id())).isInstanceOf(IllegalStateException.class);
        assertThat(persistence.load(original.id())).isEmpty();
    }

    @Test
    void adapterRejectsReadCommittedAndChecksEffectiveDatabaseIsolation() {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.executeWithoutResult(status -> assertThatThrownBy(() -> adapter.load(new ProductId(id())))
                .isInstanceOf(IllegalStateException.class));
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.executeWithoutResult(status -> {
            // Spring metadata still says repeatable read; the database setting is authoritative.
            jdbc.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
            assertThatThrownBy(() -> adapter.load(new ProductId(id()))).isInstanceOf(IllegalStateException.class);
        });
    }

    @Test
    void directCreateRejectsReadOnlyTransaction() {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.setReadOnly(true);
        Product original = product(ProductFamily.APPAREL, true, true);
        transaction.executeWithoutResult(status -> assertThatThrownBy(() -> adapter.create(original))
                .isInstanceOf(IllegalStateException.class));
        assertThat(persistence.load(original.id())).isEmpty();
    }

    @Test
    void serviceRejectsAmbientCreateWithoutPartialCommit() {
        Product original = product(ProductFamily.APPAREL, true, true);
        new TransactionTemplate(manager).executeWithoutResult(status ->
                assertThatThrownBy(() -> persistence.create(original)).isInstanceOf(IllegalStateException.class));
        assertThat(persistence.load(original.id())).isEmpty();
    }

    @Test
    void consistentSnapshotSurvivesCommittedChangesBetweenParentAndVariantQueries() {
        Product original = product(ProductFamily.APPAREL, true, true);
        Product accepted = original.withVariant(variant(original, id(), "M"));
        persistence.create(accepted);
        AtomicBoolean changed = new AtomicBoolean();
        doAnswer(invocation -> {
            Object parent = invocation.callRealMethod();
            if (changed.compareAndSet(false, true)) {
                assertThat(jdbc.queryForObject("SHOW transaction_isolation", String.class)).isEqualTo("repeatable read");
                try (var executor = Executors.newSingleThreadExecutor()) {
                    executor.submit(() -> new TransactionTemplate(manager).executeWithoutResult(status -> {
                        // Synthetic concurrent maintenance fixture, not an application transition API.
                        jdbc.update("UPDATE product.product SET lifecycle = 'ARCHIVED' WHERE product_id = ?", accepted.id().value());
                        jdbc.update("UPDATE product.product_variant SET size_code = 'L' WHERE product_id = ?", accepted.id().value());
                        Product changedRepresentation = new Product(accepted.id(), accepted.family(), accepted.standard(), true, true)
                                .withVariant(variant(accepted, accepted.variants().getFirst().id().value(), "L"));
                        jdbc.update("UPDATE product.product SET representation_revision = ? WHERE product_id = ?",
                                changedRepresentation.revision().value(), accepted.id().value());
                    })).get(10, TimeUnit.SECONDS);
                }
            }
            return parent;
        }).when(aggregates).findById(eq(accepted.id().value()), eq(ProductRow.class));
        Product snapshot = persistence.load(accepted.id()).orElseThrow();
        assertThat(snapshot.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(snapshot.variants().getFirst().configuration().size().code()).isEqualTo("M");
        Product current = persistence.load(accepted.id()).orElseThrow();
        assertThat(current.lifecycle()).isEqualTo(ProductLifecycle.ARCHIVED);
        assertThat(current.variants().getFirst().configuration().size().code()).isEqualTo("L");
    }

    @Test
    void databaseFailureIsNotReportedAsAbsence() {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.executeWithoutResult(status -> {
            status.setRollbackOnly();
            jdbc.execute("ALTER TABLE product.product RENAME TO unavailable_product");
            assertThatThrownBy(() -> adapter.load(new ProductId(id()))).isInstanceOf(DataAccessException.class);
        });
    }

    @Test
    void rejectsAmbientLoadWithoutConsistentSnapshot() {
        TransactionTemplate transaction = new TransactionTemplate(manager);
        transaction.executeWithoutResult(status -> assertThatThrownBy(() -> persistence.load(new ProductId(id())))
                .isInstanceOf(IllegalStateException.class));
    }
}
