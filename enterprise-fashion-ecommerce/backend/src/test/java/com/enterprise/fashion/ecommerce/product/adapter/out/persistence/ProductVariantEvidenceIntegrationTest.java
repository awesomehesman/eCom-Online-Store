package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.eq;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductVariantEvidenceIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired ProductVariantEvidence provider;
    @Autowired ProductPersistenceService persistence;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager manager;
    @MockitoSpyBean JdbcAggregateTemplate aggregates;

    private Product product(ProductLifecycle lifecycle, String fit) {
        var id = new ProductId("p-" + UUID.randomUUID());
        var config = new VariantConfiguration(ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL, false, false, null, null);
        var variant = new ProductVariant(new ProductVariantId("v-" + UUID.randomUUID()), id, config, fit, null);
        return Product.reconstruct(id, ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL, false, false, lifecycle, List.of(variant));
    }
    private String variant(Product product) { return product.variants().getFirst().id().value(); }

    @ParameterizedTest @EnumSource(ProductLifecycle.class)
    void persistedMembershipLifecycleAndRevisionAreObservedTogether(ProductLifecycle lifecycle) {
        var product = product(lifecycle, "before");
        persistence.create(product);
        assertThat(provider.observe(product.id().value(), variant(product))).isEqualTo(
                new ProductVariantEvidence.ConfirmedAssociation(product.id().value(), variant(product),
                        ProductVariantEvidence.Lifecycle.valueOf(lifecycle.name()), product.revision().value()));
    }

    @Test
    void absenceAndOtherProductsVariantHaveDistinctSafeOutcomes() {
        var first = product(ProductLifecycle.DRAFT, null);
        var second = product(ProductLifecycle.DRAFT, null);
        persistence.create(first); persistence.create(second);
        assertThat(provider.observe("absent-" + UUID.randomUUID(), variant(first))).isEqualTo(new ProductVariantEvidence.ProductAbsent());
        assertThat(provider.observe(first.id().value(), variant(second))).isEqualTo(new ProductVariantEvidence.VariantNotAssociated());
        assertThat(provider.observe(first.id().value(), "absent-variant")).isEqualTo(new ProductVariantEvidence.VariantNotAssociated());
        assertThat(provider.observe(null, variant(first)))
                .isEqualTo(new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INVALID_INPUT));
    }

    @Test
    void persistedRepresentationMismatchDoesNotBecomeConfirmedOrAbsent() {
        var product = product(ProductLifecycle.DRAFT, null);
        persistence.create(product);
        jdbc.update("UPDATE product.product SET representation_revision = ? WHERE product_id = ?", "v1_" + "0".repeat(64), product.id().value());
        assertThat(provider.observe(product.id().value(), variant(product)))
                .isEqualTo(new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INCONSISTENT_EVIDENCE));
    }

    @Test
    void ambientTransactionFailureCannotBeMistakenForAbsence() {
        new TransactionTemplate(manager).executeWithoutResult(status ->
                assertThat(provider.observe("absent", "variant"))
                        .isEqualTo(new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INTERNAL_FAILURE)));
    }

    @Test
    void concurrentCommitBetweenRootAndVariantReadsCannotProduceMixedEvidence() throws Exception {
        var original = product(ProductLifecycle.DRAFT, "before");
        persistence.create(original);
        var oldMember = original.variants().getFirst();
        var updated = Product.reconstruct(original.id(), original.family(), original.standard(), false, false,
                original.lifecycle(), List.of(new ProductVariant(oldMember.id(), original.id(), oldMember.configuration(), "after", null)));
        var once = new AtomicBoolean();
        try (var pool = Executors.newSingleThreadExecutor()) {
            doAnswer(call -> {
                var root = call.callRealMethod();
                if (once.compareAndSet(false, true)) {
                    assertThat(jdbc.queryForObject("SHOW transaction_isolation", String.class)).isEqualTo("repeatable read");
                    assertThat(jdbc.queryForObject("SHOW transaction_read_only", String.class)).isEqualTo("on");
                    pool.submit(() -> new TransactionTemplate(manager).executeWithoutResult(status -> {
                        jdbc.update("UPDATE product.product_variant SET fit = 'after' WHERE variant_id = ?", oldMember.id().value());
                        jdbc.update("UPDATE product.product SET representation_revision = ? WHERE product_id = ?",
                                updated.revision().value(), original.id().value());
                    })).get(15, TimeUnit.SECONDS);
                }
                return root;
            }).when(aggregates).findById(eq(original.id().value()), eq(ProductRow.class));
            assertThat(provider.observe(original.id().value(), variant(original)))
                    .isEqualTo(new ProductVariantEvidence.ConfirmedAssociation(original.id().value(), variant(original),
                            ProductVariantEvidence.Lifecycle.DRAFT, original.revision().value()));
            assertThat(provider.observe(original.id().value(), variant(original)))
                    .isEqualTo(new ProductVariantEvidence.ConfirmedAssociation(original.id().value(), variant(original),
                            ProductVariantEvidence.Lifecycle.DRAFT, updated.revision().value()));
        }
        assertThat(once).isTrue();
    }
}
