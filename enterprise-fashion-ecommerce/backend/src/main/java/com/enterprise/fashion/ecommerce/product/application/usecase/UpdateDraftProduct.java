package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Internal Draft-only replacement, not an authorized Staff entry point. REQ-PRD-035–036,
 * BPRD-REQ-011, 038–039 and DEC-0019: preserve identity, membership and canonical meaning.
 * Owns one writable repeatable-read transaction; no publication, evidence writing or retries.
 */
public final class UpdateDraftProduct {
    private final ProductPersistencePort products;
    private final TransactionOperations writes;

    public UpdateDraftProduct(ProductPersistencePort products, TransactionOperations writes) {
        this.products = Objects.requireNonNull(products);
        this.writes = Objects.requireNonNull(writes);
    }

    public Updated update(Command command) {
        Objects.requireNonNull(command);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Draft Product update must own its transaction");
        }
        try {
            return Objects.requireNonNull(writes.execute(status -> {
                Product current = products.load(command.id()).orElseThrow(() -> new Rejected(Reason.NOT_FOUND));
                if (current.lifecycle() != ProductLifecycle.DRAFT) { throw new Rejected(Reason.NOT_DRAFT); }
                if (!current.revision().equals(command.expectedRevision())) { throw new Rejected(Reason.REVISION_CONFLICT); }
                final Product replacement;
                try {
                    // Retain every established Variant identity. No deletion, identity replacement
                    // or reassignment is authorized; additions and canonical corrections are allowed.
                    var retained = command.variants().stream().map(ProductVariant::id).collect(Collectors.toSet());
                    if (!current.variants().stream().allMatch(v -> retained.contains(v.id()))) {
                        throw new IllegalArgumentException("Existing Variant membership must be retained");
                    }
                    replacement = Product.reconstruct(current.id(), current.family(), current.standard(),
                            current.sizeApplicable(), current.colourApplicable(), current.lifecycle(), command.variants())
                            .withRepresentation(command.content(), command.media());
                } catch (IllegalArgumentException invalid) {
                    throw new Rejected(Reason.INVALID_REPRESENTATION);
                }
                if (!products.replaceDraft(replacement, command.expectedRevision())) {
                    throw new Rejected(Reason.REVISION_CONFLICT);
                }
                return new Updated(replacement.id(), replacement.revision());
            }));
        } catch (PessimisticLockingFailureException concurrent) {
            // PostgreSQL serialization/lock failures reject competing writes; no retry or partial success.
            throw new Rejected(Reason.REVISION_CONFLICT);
        }
    }

    public record Command(ProductId id, ProductRevision expectedRevision, ProductContent content,
            List<ProductVariant> variants, List<ProductMedia> media) {
        public Command {
            if (id == null || expectedRevision == null || variants == null || media == null
                    || variants.stream().anyMatch(Objects::isNull) || media.stream().anyMatch(Objects::isNull)) {
                throw new Rejected(Reason.INVALID_REPRESENTATION);
            }
            variants = List.copyOf(variants);
            media = List.copyOf(media);
        }
        @Override
        public String toString() { return "UpdateDraftProduct.Command[internal]"; }
    }

    public record Updated(ProductId id, ProductRevision revision) { }
    public enum Reason { NOT_FOUND, NOT_DRAFT, REVISION_CONFLICT, INVALID_REPRESENTATION }

    /** Expected command rejection. Persistence failures retain their existing exception semantics. */
    public static final class Rejected extends RuntimeException {
        private final Reason reason;
        public Rejected(Reason reason) {
            super("Draft Product update rejected: " + reason);
            this.reason = reason;
        }
        public Reason reason() { return reason; }
    }
}
