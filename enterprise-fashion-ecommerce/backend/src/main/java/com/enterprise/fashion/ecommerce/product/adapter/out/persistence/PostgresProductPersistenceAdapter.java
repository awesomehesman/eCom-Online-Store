package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

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
 * Insert-only persistence mechanics within application-owned transactions. Guards check the
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
                product.standard().version(), product.sizeApplicable(), product.colourApplicable()));
        for (ProductVariant variant : product.variants()) {
            VariantConfiguration config = variant.configuration();
            aggregates.insert(new ProductVariantRow(variant.id().value(), variant.productId().value(),
                    config.family().value(), config.standard().version(), config.sizeApplicable(),
                    config.colourApplicable(), config.size() == null ? null : config.size().code(),
                    config.colour() == null ? null : config.colour().code(), variant.fit(), variant.material()));
        }
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
        if (limit < 1 || limit > 101) {
            throw new IllegalArgumentException("Product ID query limit must be between 1 and 101");
        }
        requireTransaction(true);
        // PostgreSQL bytea ordering implements the Contract's unsigned UTF-8 comparison;
        // neither the database default nor a linguistic collation defines continuation.
        String query = "SELECT product_id FROM product.product "
                + (afterExclusive == null ? "" : "WHERE convert_to(product_id, 'UTF8') > convert_to(:after, 'UTF8') ")
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
