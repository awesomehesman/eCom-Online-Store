package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Outcome;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Controlled publication transition for Staff-administered Products.
 * Requires confirmed publication readiness evidence BEFORE lifecycle transition.
 * Publication alone does NOT grant customer visibility or structural sellability;
 * visibility remains blocked until a complete visibility evidence path exists.
 * <p>
 * BPRD-REQ-020, 021, 022, 037: contextual authorization, evidence, audit.
 * <p>
 * This use case is NOT exposed through any Controller, Filter, or public API.
 * No Staff roles, permissions, or authentication assumptions are made.
 * The staffPrincipalId is a reference for audit/correlation only, not authorization proof.
 */
public final class PublishProduct {
    private final ProductPersistencePort products;
    private final AssessProductPublicationReadiness readiness;
    private final TransactionOperations writes;

    public PublishProduct(ProductPersistencePort products, AssessProductPublicationReadiness readiness,
            TransactionOperations writes) {
        this.products = Objects.requireNonNull(products, "products");
        this.readiness = Objects.requireNonNull(readiness, "readiness");
        this.writes = Objects.requireNonNull(writes, "writes");
    }

    /**
     * Attempts to publish a Draft Product after confirming publication readiness.
     * No lifecycle mutation or persistence write occurs unless readiness is READY.
     * @param command contains Product identity and Staff principal reference
     * @return typed result indicating success or specific failure reason
     */
    public Result publish(Command command) {
        Objects.requireNonNull(command, "command");
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("PublishProduct must own its transaction boundary");
        }
        return Objects.requireNonNull(writes.execute(status -> {
            // 1. Load current Product
            Product product = products.load(command.id()).orElse(null);
            if (product == null) {
                return new NotFound();
            }

            // 2. Verify lifecycle is DRAFT
            if (product.lifecycle() != ProductLifecycle.DRAFT) {
                return new NotDraft(product.lifecycle());
            }

            // 3. Assess publication readiness (mandatory evidence evaluation)
            ProductPublicationReadiness assessment = readiness.assess(product);

            // 4. Handle non-READY outcomes — no lifecycle mutation permitted
            return switch (assessment.outcome()) {
                case READY -> attemptLifecycleTransition(product, command.staffPrincipalId());
                case DENIED -> new NotReady(assessment.reasons());
                case EVIDENCE_UNAVAILABLE -> new EvidenceUnavailable();
                case CONFLICTING_EVIDENCE -> new ConflictingEvidence();
            };
        }), "PublishProduct transaction returned null");
    }

    private Result attemptLifecycleTransition(Product product, UUID staffPrincipalId) {
        // 5. Apply domain transition (returns new immutable aggregate)
        Product published = product.publish();

        // 6. Persist lifecycle change with CAS predicate
        boolean updated = products.updateLifecycle(product.id(), ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED);
        if (!updated) {
            // Concurrent modification or state changed between load and update
            return new ConcurrencyConflict();
        }

        // Return success — note: visibility is NOT granted by this transition
        return new Published(published.id(), published.lifecycle());
    }

    /**
     * Command input for publication. Both fields are required.
     * staffPrincipalId is a reference for audit/correlation, not authorization proof.
     */
    public record Command(ProductId id, UUID staffPrincipalId) {
        public Command {
            Objects.requireNonNull(id, "Product identity required");
            Objects.requireNonNull(staffPrincipalId, "Staff principal reference required");
        }

        @Override
        public String toString() {
            return "PublishProduct.Command[internal]";
        }
    }

    /** Sealed result hierarchy for typed outcome handling. */
    public sealed interface Result permits Published, NotFound, NotDraft, NotReady,
            EvidenceUnavailable, ConflictingEvidence, ConcurrencyConflict {}

    /** Product lifecycle successfully transitioned to PUBLISHED. Visibility is NOT granted. */
    public record Published(ProductId id, ProductLifecycle lifecycle) implements Result {
        public Published {
            Objects.requireNonNull(id);
            if (lifecycle != ProductLifecycle.PUBLISHED) {
                throw new IllegalArgumentException("Published result requires PUBLISHED lifecycle");
            }
        }
    }

    /** Product with the given identity does not exist. */
    public record NotFound() implements Result {}

    /** Product exists but is not in DRAFT state. */
    public record NotDraft(ProductLifecycle actual) implements Result {
        public NotDraft {
            Objects.requireNonNull(actual);
            if (actual == ProductLifecycle.DRAFT) {
                throw new IllegalArgumentException("NotDraft cannot indicate DRAFT");
            }
        }
    }

    /** Publication readiness evaluation returned DENIED with specific reasons. */
    public record NotReady(Set<ProductPublicationReadiness.Reason> reasons) implements Result {
        public NotReady {
            Objects.requireNonNull(reasons);
            if (reasons.isEmpty()) {
                throw new IllegalArgumentException("NotReady requires at least one reason");
            }
            reasons = Set.copyOf(reasons);
        }
    }

    /** Publication evidence provider is unavailable. No lifecycle mutation occurred. */
    public record EvidenceUnavailable() implements Result {}

    /** Publication evidence is conflicting. No lifecycle mutation occurred. */
    public record ConflictingEvidence() implements Result {}

    /** Concurrent modification detected. Lifecycle may have changed after load. */
    public record ConcurrencyConflict() implements Result {}
}
