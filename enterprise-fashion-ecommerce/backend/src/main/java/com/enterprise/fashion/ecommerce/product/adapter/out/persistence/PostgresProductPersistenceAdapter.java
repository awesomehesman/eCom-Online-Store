package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Insert-only aggregate persistence. A repeatable-read snapshot keeps parent and membership
 * consistent across load statements. Ambient reads must provide that same isolation; transactions
 * are never silently suspended. Stored lifecycle is restored state, not publication evidence.
 */
@Repository
public class PostgresProductPersistenceAdapter implements ProductPersistencePort {
    private final JdbcAggregateTemplate aggregates;
    private final TransactionTemplate writes;
    private final TransactionTemplate reads;

    public PostgresProductPersistenceAdapter(JdbcAggregateTemplate aggregates, PlatformTransactionManager manager) {
        this.aggregates = Objects.requireNonNull(aggregates);
        writes = new TransactionTemplate(manager);
        reads = new TransactionTemplate(manager);
        reads.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        reads.setReadOnly(true);
    }

    @Override
    public void create(Product product) {
        Objects.requireNonNull(product);
        writes.executeWithoutResult(status -> {
            aggregates.insert(new ProductRow(product.id().value(), product.lifecycle().name(), product.family().value(),
                    product.standard().version(), product.sizeApplicable(), product.colourApplicable()));
            for (ProductVariant variant : product.variants()) {
                VariantConfiguration config = variant.configuration();
                aggregates.insert(new ProductVariantRow(variant.id().value(), variant.productId().value(),
                        config.family().value(), config.standard().version(), config.sizeApplicable(),
                        config.colourApplicable(), config.size() == null ? null : config.size().code(),
                        config.colour() == null ? null : config.colour().code(), variant.fit(), variant.material()));
            }
        });
    }

    @Override
    public Optional<Product> load(ProductId id) {
        Objects.requireNonNull(id);
        if (TransactionSynchronizationManager.isActualTransactionActive()
                && !Integer.valueOf(TransactionDefinition.ISOLATION_REPEATABLE_READ)
                        .equals(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())
                && !Integer.valueOf(TransactionDefinition.ISOLATION_SERIALIZABLE)
                        .equals(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())) {
            throw new IllegalStateException("Product load requires a consistent snapshot transaction");
        }
        return reads.execute(status -> {
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
        });
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
