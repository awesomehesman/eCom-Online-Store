package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.Objects;
import java.util.Collections;
import java.util.IdentityHashMap;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.TransactionException;

/** Product producer only; no Pricing consumer, HTTP or positive commercial eligibility. */
public final class ObserveProductVariantEvidence implements ProductVariantEvidence {
    private final ProductPersistenceService persistence;
    public ObserveProductVariantEvidence(ProductPersistenceService persistence) {
        this.persistence = Objects.requireNonNull(persistence);
    }

    @Override
    public Result observe(String productReference, String variantReference) {
        final ProductId productId;
        final ProductVariantId variantId;
        try {
            productId = new ProductId(productReference);
            variantId = new ProductVariantId(variantReference);
        } catch (IllegalArgumentException invalidInput) {
            return new Invalid(InvalidReason.INVALID_INPUT);
        }
        try {
            // The service completes one read-only REPEATABLE READ reconstruction. Its
            // immutable snapshot keeps membership, lifecycle and revision coherent here.
            var observed = persistence.load(productId);
            if (observed.isEmpty()) { return new ProductAbsent(); }
            var product = observed.get();
            if (!productId.equals(product.id())) { return new Invalid(InvalidReason.INCONSISTENT_EVIDENCE); }
            var member = product.variants().stream().filter(v -> variantId.equals(v.id())).findFirst();
            if (member.isEmpty()) { return new VariantNotAssociated(); }
            if (!productId.equals(member.get().productId())) { return new Invalid(InvalidReason.INCONSISTENT_EVIDENCE); }
            var lifecycle = switch (product.lifecycle()) {
                case DRAFT -> Lifecycle.DRAFT;
                case PUBLISHED -> Lifecycle.PUBLISHED;
                case ARCHIVED -> Lifecycle.ARCHIVED;
            };
            return new ConfirmedAssociation(product.id().value(), member.get().id().value(), lifecycle, product.revision().value());
        } catch (DataAccessResourceFailureException | TransientDataAccessResourceException
                | QueryTimeoutException | ConcurrencyFailureException | CannotCreateTransactionException
                | TransactionTimedOutException | RecoverableDataAccessException unavailable) {
            return new Unavailable();
        } catch (TransactionException transactionFailure) {
            return containsAvailabilityFailure(transactionFailure)
                    ? new Unavailable() : new Invalid(InvalidReason.INTERNAL_FAILURE);
        } catch (IllegalArgumentException | DataIntegrityViolationException inconsistent) {
            return new Invalid(InvalidReason.INCONSISTENT_EVIDENCE);
        } catch (RuntimeException unexpected) {
            // No exception messages, private data, actor claims or implementation details escape.
            return new Invalid(InvalidReason.INTERNAL_FAILURE);
        }
    }

    /** Inspect transaction wrappers only; arbitrary internal/data exceptions are not availability evidence. */
    private static boolean containsAvailabilityFailure(TransactionException failure) {
        var seen = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        Throwable cause = failure;
        // Explicit work bound as well as identity-based cycle protection; no messages or
        // Spring root-cause helpers (which may traverse a cyclic chain) are inspected.
        for (int depth = 0; depth < 32 && cause != null && seen.add(cause); depth++, cause = cause.getCause()) {
            if (cause instanceof DataAccessResourceFailureException
                    || cause instanceof TransientDataAccessResourceException
                    || cause instanceof QueryTimeoutException
                    || cause instanceof ConcurrencyFailureException
                    || cause instanceof CannotCreateTransactionException
                    || cause instanceof TransactionTimedOutException
                    || cause instanceof RecoverableDataAccessException) {
                return true;
            }
        }
        return false;
    }

}
