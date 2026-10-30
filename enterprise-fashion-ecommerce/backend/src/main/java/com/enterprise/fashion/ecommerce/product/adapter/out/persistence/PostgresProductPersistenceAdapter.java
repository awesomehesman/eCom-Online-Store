package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import org.springframework.data.domain.Sort;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Create/load and conditional Draft replacement mechanics within application-owned transactions. Guards check the
 * effective PostgreSQL transaction settings, not merely requested Spring isolation metadata.
 * Stored lifecycle is restored state, never publication evidence.
 */
@Repository
public class PostgresProductPersistenceAdapter implements ProductPersistencePort {
    private final JdbcAggregateTemplate aggregates;
    private final JdbcClient jdbc;

    public PostgresProductPersistenceAdapter(JdbcAggregateTemplate aggregates, JdbcClient jdbc) {
        this.aggregates = Objects.requireNonNull(aggregates);
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public void create(Product product) {
        Objects.requireNonNull(product);
        requireTransaction(false);
        aggregates.insert(new ProductRow(product.id().value(), product.lifecycle().name(), product.family().value(),
                product.standard().version(), product.sizeApplicable(), product.colourApplicable(),
                product.content() == null ? null : product.content().name(),
                product.content() == null ? null : product.content().description(), product.revision().value()));
        for (ProductVariant variant : product.variants()) {
            VariantConfiguration config = variant.configuration();
            aggregates.insert(new ProductVariantRow(variant.id().value(), variant.productId().value(),
                    config.family().value(), config.standard().version(), config.sizeApplicable(),
                    config.colourApplicable(), config.size() == null ? null : config.size().code(),
                    config.colour() == null ? null : config.colour().code(), variant.fit(), variant.material()));
        }
        int order = 0;
        for (ProductMedia media : product.media()) {
            aggregates.insert(new ProductMediaRow(media.id(), media.productId().value(),
                    media.variantId() == null ? null : media.variantId().value(), media.assetRevision(),
                    media.url(), media.alternativeText(), order++));
        }
    }

    @Override
    public boolean replaceDraft(Product replacement, ProductRevision expectedRevision) {
        Objects.requireNonNull(replacement);
        Objects.requireNonNull(expectedRevision);
        requireTransaction(false);
        requireTransaction(true);
        if (replacement.lifecycle() != ProductLifecycle.DRAFT) {
            throw new IllegalArgumentException("Only Draft representations may be replaced");
        }
        // Validate independently at the port boundary, including pre-V5 NULL stored revisions.
        Product current = load(replacement.id()).orElse(null);
        if (current == null || current.lifecycle() != ProductLifecycle.DRAFT
                || !current.revision().equals(expectedRevision)) { return false; }
        var existingIds = current.variants().stream().map(ProductVariant::id).collect(java.util.stream.Collectors.toSet());
        var replacementIds = replacement.variants().stream().map(ProductVariant::id).collect(java.util.stream.Collectors.toSet());
        if (!replacementIds.containsAll(existingIds) || !current.family().equals(replacement.family())
                || !current.standard().equals(replacement.standard())
                || current.sizeApplicable() != replacement.sizeApplicable()
                || current.colourApplicable() != replacement.colourApplicable()) {
            throw new IllegalArgumentException("Draft replacement must preserve identity and Attribute context");
        }
        int changed = jdbc.sql("""
                UPDATE product.product SET content_name = :name, content_description = :description,
                    representation_revision = :revision
                WHERE product_id = :id AND lifecycle = 'DRAFT'
                    AND (representation_revision = :expected OR representation_revision IS NULL)
                """)
                .param("name", replacement.content() == null ? null : replacement.content().name())
                .param("description", replacement.content() == null ? null : replacement.content().description())
                .param("revision", replacement.revision().value()).param("id", replacement.id().value())
                .param("expected", expectedRevision.value()).update();
        if (changed != 1) { return false; }
        // The parent CAS locks this aggregate through commit. Child changes cannot race another
        // supported writer. NULL revision is supported only after validated legacy reconstruction.
        jdbc.sql("SET CONSTRAINTS product.product_variant_configuration_unique DEFERRED").update();
        for (ProductVariant variant : replacement.variants()) {
            var config = variant.configuration();
            var row = new ProductVariantRow(variant.id().value(), variant.productId().value(),
                    config.family().value(), config.standard().version(), config.sizeApplicable(), config.colourApplicable(),
                    config.size() == null ? null : config.size().code(), config.colour() == null ? null : config.colour().code(),
                    variant.fit(), variant.material());
            if (existingIds.contains(variant.id())) { aggregates.update(row); }
            else { aggregates.insert(row); }
        }
        // Association metadata is replaced in supplied order; Variant rows are never deleted.
        jdbc.sql("DELETE FROM product.product_media WHERE product_id = :id").param("id", replacement.id().value()).update();
        int order = 0;
        for (ProductMedia media : replacement.media()) {
            aggregates.insert(new ProductMediaRow(media.id(), media.productId().value(),
                    media.variantId() == null ? null : media.variantId().value(), media.assetRevision(),
                    media.url(), media.alternativeText(), order++));
        }
        jdbc.sql("SET CONSTRAINTS product.product_variant_configuration_unique IMMEDIATE").update();
        return true;
    }

    @Override
    public Optional<Product> load(ProductId id) {
        Objects.requireNonNull(id);
        requireTransaction(true);
        ProductRow row = aggregates.findById(id.value(), ProductRow.class);
        if (row == null) {
            return Optional.empty();
        }
        try {
            var variants = new ArrayList<ProductVariant>();
            for (ProductVariantRow variant : aggregates.findAll(
                    Query.query(Criteria.where("product_id").is(id.value())), ProductVariantRow.class)) {
                variants.add(restoreVariant(variant));
            }
            Product restored = Product.reconstruct(new ProductId(row.productId()), new ProductFamily(row.family()),
                    new ProductAttributeStandard(row.attributeStandardVersion()), required(row.sizeApplicable()),
                    required(row.colourApplicable()), ProductLifecycle.valueOf(row.lifecycle()), variants);
            var media = new ArrayList<ProductMedia>();
            for (ProductMediaRow item : aggregates.findAll(Query.query(Criteria.where("product_id").is(id.value()))
                    .sort(Sort.by("presentationOrder")), ProductMediaRow.class)) {
                if (item.presentationOrder() == null || item.presentationOrder() != media.size()) {
                    throw new IllegalArgumentException("Invalid stored media order");
                }
                media.add(new ProductMedia(item.mediaId(), new ProductId(item.productId()),
                        item.variantId() == null ? null : new ProductVariantId(item.variantId()),
                        item.assetRevision(), item.url(), item.alternativeText()));
            }
            ProductContent content = null;
            if (row.contentName() != null || row.contentDescription() != null) {
                content = new ProductContent(row.contentName(), row.contentDescription());
            }
            if (row.representationRevision() == null) {
                if (content != null || !media.isEmpty()) {
                    throw new IllegalArgumentException("Missing stored representation revision");
                }
                // Unbackfilled pre-V5 structural rows remain loadable, never accepted or public.
            } else {
                restored = Product.reconstruct(restored.id(), restored.family(), restored.standard(),
                        restored.sizeApplicable(), restored.colourApplicable(), restored.lifecycle(), restored.variants(),
                        content, media, new ProductRevision(row.representationRevision()));
            }
            if (!id.equals(restored.id())) {
                throw new IllegalArgumentException("Stored Product identity mismatch");
            }
            return Optional.of(restored);
        } catch (IllegalArgumentException | NullPointerException invalid) {
            // No invalid raw values or descriptive content in the outward diagnostic.
            throw new DataIntegrityViolationException("Invalid stored Product aggregate");
        }
    }

    @Override
    public List<ProductId> findProductIdsAfter(ProductId afterExclusive, int limit) {
        return findIdsAfter(afterExclusive, limit, "");
    }

    @Override
    public List<ProductId> findCatalogueCandidateIdsAfter(ProductId afterExclusive, int limit) {
        // Negative lifecycle filtering only. Unknown/corrupt states remain candidates so
        // reconstruction fails closed rather than presenting fabricated empty success.
        return findIdsAfter(afterExclusive, limit, "(lifecycle IS NULL OR lifecycle NOT IN ('DRAFT', 'ARCHIVED'))");
    }

    private List<ProductId> findIdsAfter(ProductId afterExclusive, int limit, String candidateRestriction) {
        if (limit < 1 || limit > 101) {
            throw new IllegalArgumentException("Product ID query limit must be between 1 and 101");
        }
        requireTransaction(true);
        var conditions = new ArrayList<String>();
        if (!candidateRestriction.isEmpty()) {
            conditions.add(candidateRestriction);
        }
        if (afterExclusive != null) {
            conditions.add("convert_to(product_id, 'UTF8') > convert_to(:after, 'UTF8')");
        }
        // Shared byte ordering; no offset or database-default linguistic collation.
        String query = "SELECT product_id FROM product.product "
                + (conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions) + " ")
                + "ORDER BY convert_to(product_id, 'UTF8') ASC LIMIT :limit";
        var statement = jdbc.sql(query).param("limit", limit);
        if (afterExclusive != null) {
            statement = statement.param("after", afterExclusive.value());
        }
        return statement.query((row, number) -> new ProductId(row.getString("product_id"))).list();
    }

    private void requireTransaction(boolean snapshot) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product persistence requires an application-owned transaction");
        }
        if (snapshot) {
            String isolation = jdbc.sql("SELECT current_setting('transaction_isolation')").query(String.class).single();
            if (!"repeatable read".equals(isolation) && !"serializable".equals(isolation)) {
                throw new IllegalStateException("Product load requires a consistent snapshot transaction");
            }
        } else if (!"off".equals(jdbc.sql("SELECT current_setting('transaction_read_only')").query(String.class).single())) {
            throw new IllegalStateException("Product creation requires a writable transaction");
        }
    }

    private ProductVariant restoreVariant(ProductVariantRow row) {
        ProductFamily family = new ProductFamily(row.family());
        ProductAttributeStandard standard = new ProductAttributeStandard(row.attributeStandardVersion());
        // Validate exact storage spellings BEFORE the normalizing domain constructors run.
        if (row.colourCode() != null && !"BLACK".equals(row.colourCode())) {
            throw new IllegalArgumentException("Noncanonical stored Colour");
        }
        String size = row.sizeCode();
        if (size != null) {
            boolean canonical = family.equals(ProductFamily.APPAREL)
                    ? size.matches("XS|S|M|L|XL|XXL|XXXL")
                    : family.equals(ProductFamily.FOOTWEAR)
                        ? size.matches("(?:[3-9]|1[0-2])(?:\\.5)?|13") : "ONE_SIZE".equals(size);
            if (!canonical) {
                throw new IllegalArgumentException("Noncanonical stored Size");
            }
        }
        var config = new VariantConfiguration(family, standard, required(row.sizeApplicable()),
                required(row.colourApplicable()), size == null ? null : new ProductSize(family, size),
                row.colourCode() == null ? null : new ProductColour(row.colourCode()));
        return new ProductVariant(new ProductVariantId(row.variantId()), new ProductId(row.productId()),
                config, row.fit(), row.material());
    }

    private boolean required(Boolean value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing stored applicability");
        }
        return value;
    }
}
