package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.FailureReason;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.MalwareScan;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.Outcome;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Performs bounded technical image inspection and optional ClamAV malware scanning.
 * Its immutable observations never establish authoritative media or publication acceptance.
 */
public final class CheckQuarantinedProductImage {
    public static final String CHECK_DEFINITION = "product-image-technical-validation";
    public static final String IMPLEMENTATION_VERSION = "1.0.0";
    private final ProductImageSecurityCheckPort storage;
    private final ProductImageMalwareObservationPort observations;
    private final ProductImageMalwareScanner scanner;
    private final ProductImageMalwareScanner.Descriptor scannerDescriptor;
    private final TransactionOperations reads;
    private final TransactionOperations writes;
    private final ProductImageTechnicalInspector.Limits limits;
    private final Clock clock;
    private final Supplier<UUID> identifiers;

    public CheckQuarantinedProductImage(ProductImageSecurityCheckPort storage,
            ProductImageMalwareObservationPort observations, ProductImageMalwareScanner scanner,
            TransactionOperations reads, TransactionOperations writes,
            ProductImageTechnicalInspector.Limits limits, Clock clock, Supplier<UUID> identifiers) {
        this.storage = Objects.requireNonNull(storage); this.observations = Objects.requireNonNull(observations);
        this.scanner = Objects.requireNonNull(scanner);
        this.scannerDescriptor = Objects.requireNonNull(scanner.descriptor(), "scanner descriptor");
        this.reads = Objects.requireNonNull(reads);
        this.writes = Objects.requireNonNull(writes);
        this.limits = Objects.requireNonNull(limits); this.clock = Objects.requireNonNull(clock); this.identifiers = Objects.requireNonNull(identifiers);
    }

    public ProductMediaSecurityCheckResult check(ProductId productId, UUID assetId) {
        Objects.requireNonNull(productId); Objects.requireNonNull(assetId);
        if (TransactionSynchronizationManager.isActualTransactionActive()) throw new IllegalStateException("Media checking must own its transaction");
        var retrieved = Objects.requireNonNull(reads.execute(status -> storage.retrieve(assetId, productId)));
        FailureReason failure = null;
        try { ProductImageTechnicalInspector.inspect(retrieved.originalBytes(), limits); }
        catch (ProductImageTechnicalInspector.Failure rejected) {
            failure = switch (rejected.reason()) {
                case MALFORMED_CONTENT -> FailureReason.MALFORMED_CONTENT;
                case UNSUPPORTED_FORMAT -> FailureReason.UNSUPPORTED_FORMAT;
                case RESOURCE_LIMIT_EXCEEDED -> FailureReason.RESOURCE_LIMIT_EXCEEDED;
            };
        }
        ProductImageMalwareScanResult malware = failure == null ? scan(retrieved.originalBytes())
                : technicalRejection();
        FailureReason finalFailure = failure;
        return Objects.requireNonNull(writes.execute(status -> {
            var revalidated = storage.retrieve(assetId, productId);
            if (!revalidated.asset().sha256().equals(retrieved.asset().sha256())) {
                throw new ProductImageSecurityCheckPort.Failure(ProductImageSecurityCheckPort.FailureReason.DIGEST_MISMATCH);
            }
            var result = new ProductMediaSecurityCheckResult(Objects.requireNonNull(identifiers.get()), assetId, productId,
                    retrieved.asset().sha256(), CHECK_DEFINITION, IMPLEMENTATION_VERSION, Instant.now(clock),
                    finalFailure == null ? Outcome.TECHNICAL_CHECKS_PASSED : Outcome.REJECTED, finalFailure,
                    MalwareScan.NOT_PERFORMED, false);
            storage.append(result);
            observations.append(new ProductImageMalwareObservation(Objects.requireNonNull(identifiers.get()),
                    assetId, productId, retrieved.asset().sha256(), Instant.now(clock), malware, false));
            return result;
        }));
    }

    private ProductImageMalwareScanResult scan(byte[] originalBytes) {
        try {
            return Objects.requireNonNull(scanner.scan(originalBytes), "scanner result");
        } catch (RuntimeException unexpectedScannerFailure) {
            return indeterminate(ProductImageMalwareScanResult.FailureCategory.SCANNER_ERROR);
        }
    }

    private ProductImageMalwareScanResult technicalRejection() {
        return indeterminate(ProductImageMalwareScanResult.FailureCategory.TECHNICAL_CHECK_REJECTED);
    }

    private ProductImageMalwareScanResult indeterminate(ProductImageMalwareScanResult.FailureCategory failure) {
        return new ProductImageMalwareScanResult(ProductImageMalwareScanResult.Outcome.INDETERMINATE,
                scannerDescriptor.scannerIdentity(), scannerDescriptor.checkDefinition(),
                scannerDescriptor.implementationVersion(), null, null, null, null, failure);
    }
}
