package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.enterprise.fashion.ecommerce.product.adapter.out.persistence.PostgresProductPersistenceAdapter;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceObservation;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceService;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@Tag("integration")
@SpringBootTest
@Testcontainers
class UpdateDraftProductIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired private UpdateDraftProduct commands;
    @Autowired private ProductPersistenceService products;
    @Autowired private ProductEvidenceService evidence;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;
    @MockitoSpyBean private PostgresProductPersistenceAdapter adapter;

    private String id() { return UUID.randomUUID().toString(); }

    private ProductVariant variant(Product product, String id, String size, String colour) {
        return new ProductVariant(new ProductVariantId(id), product.id(), new VariantConfiguration(product.family(),
                product.standard(), true, true, new ProductSize(product.family(), size), new ProductColour(colour)), "Regular", "Cotton");
    }

    private Product draft() {
        Product base = new Product(new ProductId(id()), ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, true);
        Product product = base.withVariant(variant(base, id(), "S", "black"))
                .withVariant(variant(base, id(), "M", "BLACK"))
                .withRepresentation(new ProductContent("Shirt", "Original shirt"), List.of());
        products.create(product);
        return product;
    }

    private UpdateDraftProduct.Command command(Product current, String description, List<ProductVariant> variants, List<ProductMedia> media) {
        return new UpdateDraftProduct.Command(current.id(), current.revision(), new ProductContent("Shirt", description), variants, media);
    }

    private void rejected(UpdateDraftProduct.Reason reason, Runnable call) {
        assertThatThrownBy(call::run).isInstanceOfSatisfying(UpdateDraftProduct.Rejected.class,
                failure -> assertThat(failure.reason()).isEqualTo(reason));
    }

    @Test
    void replacesContentCanonicalConfigurationsAndOrderedMediaWhilePreservingVariantRows() {
        Product original = draft();
        // Synthetic historical reference proves that replacement never deletes Variant identities.
        jdbc.execute("CREATE TABLE IF NOT EXISTS product.test_variant_reference (variant_id TEXT REFERENCES product.product_variant(variant_id))");
        jdbc.update("INSERT INTO product.test_variant_reference VALUES (?)", original.variants().getFirst().id().value());
        ProductMedia first = new ProductMedia(id(), original.id(), original.variants().getFirst().id(), "asset-1", "https://example.test/one", "Front");
        ProductMedia second = new ProductMedia(id(), original.id(), null, "asset-2", "https://example.test/two", "Back");
        var seeded = commands.update(command(original, "With media", original.variants(), List.of(first, second)));
        Product before = products.load(original.id()).orElseThrow();
        assertThat(before.revision()).isEqualTo(seeded.revision());
        var variants = List.of(variant(before, before.variants().get(0).id().value(), "m", "black"),
                variant(before, before.variants().get(1).id().value(), "s", "Black"));
        var result = commands.update(command(before, "Updated shirt", variants, List.of(second, first)));
        Product loaded = products.load(original.id()).orElseThrow();
        assertThat(result.id()).isEqualTo(original.id());
        assertThat(loaded.content().description()).isEqualTo("Updated shirt");
        assertThat(loaded.variants()).extracting(ProductVariant::id).containsExactlyInAnyOrderElementsOf(
                original.variants().stream().map(ProductVariant::id).toList());
        for (ProductVariant expected : variants) {
            assertThat(loaded.variants().stream().filter(v -> v.id().equals(expected.id())).findFirst().orElseThrow().configuration())
                    .isEqualTo(expected.configuration());
        }
        assertThat(loaded.media()).containsExactly(second, first);
        assertThat(loaded.revision()).isEqualTo(result.revision()).isNotEqualTo(before.revision());
        assertThat(products.load(original.id()).orElseThrow().revision()).isEqualTo(result.revision());
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(loaded.isCustomerVisible()).isFalse();
        assertThat(new AssessProductVisibilityReadiness(products).assess(original.id())).isEqualTo(ProductVisibilityReadiness.NOT_VISIBLE);
    }

    @Test
    void addsVariantWithoutReplacingExistingMembershipAndSupportsOptionalContentRemoval() {
        Product original = draft();
        var variants = new java.util.ArrayList<>(original.variants());
        variants.add(variant(original, id(), "L", "black"));
        var result = commands.update(new UpdateDraftProduct.Command(original.id(), original.revision(), null, variants, List.of()));
        Product loaded = products.load(original.id()).orElseThrow();
        assertThat(loaded.variants()).hasSize(3);
        assertThat(loaded.content()).isNull();
        assertThat(loaded.revision()).isEqualTo(result.revision());
    }

    @Test
    void staleRevisionAndMissingProductAreDistinct() {
        Product original = draft();
        commands.update(command(original, "First edit", original.variants(), List.of()));
        rejected(UpdateDraftProduct.Reason.REVISION_CONFLICT,
                () -> commands.update(command(original, "Stale edit", original.variants(), List.of())));
        var missing = new UpdateDraftProduct.Command(new ProductId(id()), original.revision(), null, List.of(), List.of());
        rejected(UpdateDraftProduct.Reason.NOT_FOUND, () -> commands.update(missing));
        assertThat(products.load(original.id()).orElseThrow().content().description()).isEqualTo("First edit");
    }

    @ParameterizedTest
    @EnumSource(value = ProductLifecycle.class, names = {"PUBLISHED", "ARCHIVED"})
    void nonDraftCannotBeModified(ProductLifecycle lifecycle) {
        Product original = draft();
        jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle.name(), original.id().value());
        rejected(UpdateDraftProduct.Reason.NOT_DRAFT,
                () -> commands.update(command(original, "Forbidden edit", original.variants(), List.of())));
        Product loaded = products.load(original.id()).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(lifecycle);
        assertThat(loaded.revision()).isEqualTo(original.revision());
    }

    @Test
    void rejectsInvalidMembershipDuplicatesAndMediaWithoutWriting() {
        Product original = draft();
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> commands.update(command(original, "Removed identity", List.of(), List.of())));
        var duplicate = List.of(original.variants().getFirst(), variant(original, original.variants().get(1).id().value(), "s", "BLACK"));
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> commands.update(command(original, "Duplicate", duplicate, List.of())));
        ProductMedia foreign = new ProductMedia(id(), new ProductId(id()), null, "asset", "https://example.test/image", "Front");
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> commands.update(command(original, "Foreign media", original.variants(), List.of(foreign))));
        assertThat(products.load(original.id()).orElseThrow().revision()).isEqualTo(original.revision());
        assertThatIllegalArgumentException().isThrownBy(() -> variant(original, id(), "M", "blue"));
        assertThatIllegalArgumentException().isThrownBy(() -> variant(original, id(), "4", "black"));
    }

    @Test
    void failedLateMediaInsertRollsBackParentVariantsAndDeletedMedia() {
        Product original = draft();
        ProductMedia retained = new ProductMedia(id(), original.id(), null, "asset", "https://example.test/old", "Original");
        commands.update(command(original, "With original media", original.variants(), List.of(retained)));
        original = products.load(original.id()).orElseThrow();
        Product foreign = draft();
        ProductMedia collision = new ProductMedia(id(), foreign.id(), null, "asset", "https://example.test/foreign", "Foreign");
        commands.update(command(foreign, "Foreign media", foreign.variants(), List.of(collision)));
        ProductMedia attempted = new ProductMedia(collision.id(), original.id(), null, "asset", "https://example.test/new", "New");
        Product before = original;
        var variants = List.of(variant(before, before.variants().get(0).id().value(), "L", "black"), before.variants().get(1));
        assertThatThrownBy(() -> commands.update(command(before, "Must roll back", variants, List.of(attempted))))
                .isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        Product after = products.load(before.id()).orElseThrow();
        assertThat(after.revision()).isEqualTo(before.revision());
        assertThat(after.media()).containsExactly(retained);
        assertThat(after.variants().stream().map(ProductVariant::configuration).toList())
                .containsExactlyInAnyOrderElementsOf(before.variants().stream().map(ProductVariant::configuration).toList());
    }

    @Test
    void concurrentWritersHaveExactlyOneWinnerWithoutRetry() throws Exception {
        Product original = draft();
        CyclicBarrier readBoth = new CyclicBarrier(2);
        doAnswer(invocation -> {
            readBoth.await(10, TimeUnit.SECONDS);
            return invocation.callRealMethod();
        }).when(adapter).replaceDraft(any(), any());
        try (var workers = Executors.newFixedThreadPool(2)) {
            var one = workers.submit(() -> attempt(command(original, "Writer one", original.variants(), List.of())));
            var two = workers.submit(() -> attempt(command(original, "Writer two", original.variants(), List.of())));
            assertThat(List.of(one.get(20, TimeUnit.SECONDS), two.get(20, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder("UPDATED", "REVISION_CONFLICT");
        }
        Product loaded = products.load(original.id()).orElseThrow();
        assertThat(loaded.content().description()).isIn("Writer one", "Writer two");
        assertThat(loaded.revision()).isNotEqualTo(original.revision());
    }

    private String attempt(UpdateDraftProduct.Command command) {
        try { commands.update(command); return "UPDATED"; }
        catch (UpdateDraftProduct.Rejected rejected) { return rejected.reason().name(); }
    }

    @Test
    void changedRevisionLeavesHistoricalEvidenceStaleAndUnmodified() {
        Product original = draft();
        UUID event = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO product.evidence_event (event_id, product_id, representation_revision,
                    purpose, event_type, source_reference, source_actor_claim, supporting_references,
                    audit_correlation, recorded_at, recorded_by)
                VALUES (?, ?, ?, 'MEDIA', 'RECORD', 'synthetic-source', 'untrusted-source-claim',
                    ARRAY['synthetic-support'], 'test-correlation', now(), 'PRODUCT_DATABASE')
                """, event, original.id().value(), original.revision().value());
        assertThat(evidence.observe(original.id(), ProductEvidenceEvent.Purpose.MEDIA).state())
                .isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        commands.update(command(original, "New representation", original.variants(), List.of()));
        var observation = evidence.observe(original.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.STALE);
        assertThat(observation.history()).hasSize(1);
        assertThat(observation.history().getFirst().id()).isEqualTo(event);
        assertThat(observation.history().getFirst().revision()).isEqualTo(original.revision());
    }

    @Test
    void legacyNullRevisionIsReconstructedAndConditionallyUpdated() {
        ProductId id = new ProductId(id());
        jdbc.update("""
                INSERT INTO product.product (product_id, lifecycle, family, attribute_standard_version, size_applicable, colour_applicable)
                VALUES (?, 'DRAFT', 'Accessories', '1.0.0', false, false)
                """, id.value());
        Product legacy = products.load(id).orElseThrow();
        var result = commands.update(new UpdateDraftProduct.Command(id, legacy.revision(), new ProductContent("Bag", "Legacy edit"), List.of(), List.of()));
        assertThat(products.load(id).orElseThrow().revision()).isEqualTo(result.revision());
    }

    @Test
    void noOpKeepsRevisionAndApplicationRejectsAmbientTransactions() {
        Product original = draft();
        var unchanged = new UpdateDraftProduct.Command(original.id(), original.revision(), original.content(), original.variants(), original.media());
        assertThat(commands.update(unchanged).revision()).isEqualTo(original.revision());
        new TransactionTemplate(manager).executeWithoutResult(status ->
                assertThatThrownBy(() -> commands.update(unchanged)).isInstanceOf(IllegalStateException.class));
        assertThatThrownBy(() -> adapter.replaceDraft(original, original.revision())).isInstanceOf(IllegalStateException.class);
        var weak = new TransactionTemplate(manager);
        weak.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        weak.executeWithoutResult(status -> assertThatThrownBy(() -> adapter.replaceDraft(original, original.revision()))
                .isInstanceOf(IllegalStateException.class));
    }

    @Test
    void persistenceUnavailabilityIsNotRevisionConflictOrEmptySuccess() {
        Product original = draft();
        doThrow(new DataAccessResourceFailureException("private SQL")).when(adapter).load(original.id());
        assertThatThrownBy(() -> commands.update(command(original, "Unavailable", original.variants(), List.of())))
                .isInstanceOf(DataAccessResourceFailureException.class);
    }
}
