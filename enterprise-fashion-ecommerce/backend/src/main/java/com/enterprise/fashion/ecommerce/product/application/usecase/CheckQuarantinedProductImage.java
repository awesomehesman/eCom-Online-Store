package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.FailureReason;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.MalwareScan;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.Outcome;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Runs bounded technical validation; malware checking is explicitly unavailable in this increment. */
public final class CheckQuarantinedProductImage {
    public static final String CHECK_DEFINITION = "product-image-technical-validation";
    public static final String IMPLEMENTATION_VERSION = "1.0.0";
    private final ProductImageSecurityCheckPort storage;
    private final TransactionOperations transactions;
    private final ProductImageTechnicalInspector.Limits limits;
    private final Clock clock;
    private final Supplier<UUID> identifiers;

    public CheckQuarantinedProductImage(ProductImageSecurityCheckPort storage, TransactionOperations transactions,
            ProductImageTechnicalInspector.Limits limits, Clock clock, Supplier<UUID> identifiers) {
        this.storage = Objects.requireNonNull(storage); this.transactions = Objects.requireNonNull(transactions);
        this.limits = Objects.requireNonNull(limits); this.clock = Objects.requireNonNull(clock); this.identifiers = Objects.requireNonNull(identifiers);
    }

    public ProductMediaSecurityCheckResult check(ProductId productId, UUID assetId) {
        Objects.requireNonNull(productId); Objects.requireNonNull(assetId);
        if (TransactionSynchronizationManager.isActualTransactionActive()) throw new IllegalStateException("Media checking must own its transaction");
        return Objects.requireNonNull(transactions.execute(status -> {
            var retrieved = storage.retrieve(assetId, productId);
            FailureReason failure = null;
            try { ProductImageTechnicalInspector.inspect(retrieved.originalBytes(), limits); }
            catch (ProductImageTechnicalInspector.Failure rejected) {
                failure = switch (rejected.reason()) {
                    case MALFORMED_CONTENT -> FailureReason.MALFORMED_CONTENT;
                    case UNSUPPORTED_FORMAT -> FailureReason.UNSUPPORTED_FORMAT;
                    case RESOURCE_LIMIT_EXCEEDED -> FailureReason.RESOURCE_LIMIT_EXCEEDED;
                };
            }
            var result = new ProductMediaSecurityCheckResult(Objects.requireNonNull(identifiers.get()), assetId, productId,
                    retrieved.asset().sha256(), CHECK_DEFINITION, IMPLEMENTATION_VERSION, Instant.now(clock),
                    failure == null ? Outcome.TECHNICAL_CHECKS_PASSED : Outcome.REJECTED, failure,
                    MalwareScan.NOT_PERFORMED, false);
            storage.append(result);
            return result;
        }));
    }
}
