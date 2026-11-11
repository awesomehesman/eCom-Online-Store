package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector.Limits;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.adapter.out.media.UnavailableProductImageMalwareScanner;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort.RetrievedAsset;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckQuarantinedProductImageTest {
    private static final ProductId PRODUCT = new ProductId("product-one");
    private static final UUID ASSET = UUID.fromString("96ad1b65-e992-431d-be48-35dfee451f23");
    private static final UUID CHECK = UUID.fromString("f005361e-b0d5-4772-a118-e1f703755c94");
    private static final Instant NOW = Instant.parse("2026-11-11T14:00:00Z");
    private final ProductImageSecurityCheckPort storage = mock(ProductImageSecurityCheckPort.class);
    private final ProductImageMalwareObservationPort observations = mock(ProductImageMalwareObservationPort.class);
    private final ProductImageMalwareScanner scanner = new UnavailableProductImageMalwareScanner();

    @Test
    void validPngAndJpegProduceAccurateNonAuthoritativeResultsWithoutMalwareClaim() throws Exception {
        for (String format : new String[] {"png", "jpeg"}) {
            byte[] bytes = image(format, 2, 3);
            when(storage.retrieve(ASSET, PRODUCT)).thenReturn(retrieved(bytes, format));
            var result = checker(Limits.conservativeDefaults()).check(PRODUCT, ASSET);
            assertThat(result.outcome()).isEqualTo(ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED);
            assertThat(result.malwareScan()).isEqualTo(ProductMediaSecurityCheckResult.MalwareScan.NOT_PERFORMED);
            assertThat(result.authoritativeAcceptanceEligible()).isFalse();
            assertThat(result.checkDefinition()).isEqualTo(CheckQuarantinedProductImage.CHECK_DEFINITION);
            assertThat(result.implementationVersion()).isEqualTo(CheckQuarantinedProductImage.IMPLEMENTATION_VERSION);
            assertThat(result.executedAt()).isEqualTo(NOW);
            verify(storage).append(result);
        }
    }

    @Test
    void malformedUnsupportedAndResourceExcessAreRejectedAndRecorded() throws Exception {
        assertRejected(new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a, 1},
                ProductMediaSecurityCheckResult.FailureReason.MALFORMED_CONTENT, Limits.conservativeDefaults());
        assertRejected("GIF89a-invalid".getBytes(java.nio.charset.StandardCharsets.US_ASCII),
                ProductMediaSecurityCheckResult.FailureReason.UNSUPPORTED_FORMAT, Limits.conservativeDefaults());
        byte[] large = image("png", 4, 4);
        assertRejected(large, ProductMediaSecurityCheckResult.FailureReason.RESOURCE_LIMIT_EXCEEDED,
                new Limits(large.length, 3, 3, 9));
    }

    @Test
    void missingWrongOwnerAndDigestMismatchFailClosedWithoutResult() {
        for (var reason : ProductImageSecurityCheckPort.FailureReason.values()) {
            reset(storage);
            when(storage.retrieve(ASSET, PRODUCT)).thenThrow(new ProductImageSecurityCheckPort.Failure(reason));
            assertThatThrownBy(() -> checker(Limits.conservativeDefaults()).check(PRODUCT, ASSET))
                    .isInstanceOfSatisfying(ProductImageSecurityCheckPort.Failure.class,
                            failure -> assertThat(failure.reason()).isEqualTo(reason));
        }
        verify(storage, never()).append(any());
    }

    @Test
    void resultTypeCannotRepresentMalwareSuccessOrAuthoritativeAcceptance() throws Exception {
        byte[] bytes = image("png", 1, 1);
        assertThatThrownBy(() -> new ProductMediaSecurityCheckResult(CHECK, ASSET, PRODUCT, digest(bytes),
                "definition", "1", NOW, ProductMediaSecurityCheckResult.Outcome.TECHNICAL_CHECKS_PASSED,
                null, ProductMediaSecurityCheckResult.MalwareScan.NOT_PERFORMED, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void scannerRunsOutsideTransactionsAndPersistenceRevalidatesExactAsset() throws Exception {
        byte[] bytes = image("png", 1, 1);
        when(storage.retrieve(ASSET, PRODUCT)).thenReturn(retrieved(bytes, "png"));
        var outsideTransaction = new java.util.concurrent.atomic.AtomicBoolean();
        ProductImageMalwareScanner verifyingScanner = new ProductImageMalwareScanner() {
            @Override public Descriptor descriptor() { return scanner.descriptor(); }
            @Override public com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult scan(byte[] content) {
                outsideTransaction.set(!org.springframework.transaction.support.TransactionSynchronizationManager
                        .isActualTransactionActive());
                return scanner.scan(content);
            }
        };
        var useCase = new CheckQuarantinedProductImage(storage, observations, verifyingScanner,
                trackedTransaction(), trackedTransaction(), Limits.conservativeDefaults(),
                Clock.fixed(NOW, ZoneOffset.UTC), () -> CHECK);

        useCase.check(PRODUCT, ASSET);

        assertThat(outsideTransaction).isTrue();
        verify(storage, org.mockito.Mockito.times(2)).retrieve(ASSET, PRODUCT);
        verify(observations).append(any());
    }

    @Test
    void unexpectedScannerRuntimeFailureBecomesImmutableIndeterminateObservation() throws Exception {
        byte[] bytes = image("png", 1, 1);
        when(storage.retrieve(ASSET, PRODUCT)).thenReturn(retrieved(bytes, "png"));
        ProductImageMalwareScanner failingScanner = new ProductImageMalwareScanner() {
            @Override public Descriptor descriptor() {
                return new Descriptor("configured-scanner", "configured-check", "7.3");
            }
            @Override public ProductImageMalwareScanResult scan(byte[] content) {
                throw new IllegalStateException("unexpected scanner failure");
            }
        };
        var useCase = new CheckQuarantinedProductImage(storage, observations, failingScanner,
                immediate(), immediate(), Limits.conservativeDefaults(),
                Clock.fixed(NOW, ZoneOffset.UTC), () -> CHECK);

        useCase.check(PRODUCT, ASSET);

        var observation = org.mockito.ArgumentCaptor.forClass(ProductImageMalwareObservation.class);
        verify(observations).append(observation.capture());
        assertThat(observation.getValue().scanResult().outcome())
                .isEqualTo(ProductImageMalwareScanResult.Outcome.INDETERMINATE);
        assertThat(observation.getValue().scanResult().failureCategory())
                .isEqualTo(ProductImageMalwareScanResult.FailureCategory.SCANNER_ERROR);
        assertThat(observation.getValue().scanResult().scannerIdentity()).isEqualTo("configured-scanner");
        assertThat(observation.getValue().scanResult().checkDefinition()).isEqualTo("configured-check");
        assertThat(observation.getValue().scanResult().implementationVersion()).isEqualTo("7.3");
        assertThat(observation.getValue().authoritativeAcceptanceEligible()).isFalse();
    }

    private void assertRejected(byte[] bytes, ProductMediaSecurityCheckResult.FailureReason reason, Limits limits) {
        when(storage.retrieve(ASSET, PRODUCT)).thenReturn(retrieved(bytes, "png"));
        var result = checker(limits).check(PRODUCT, ASSET);
        assertThat(result.outcome()).isEqualTo(ProductMediaSecurityCheckResult.Outcome.REJECTED);
        assertThat(result.failureReason()).isEqualTo(reason);
        assertThat(result.authoritativeAcceptanceEligible()).isFalse();
        verify(storage).append(result);
    }

    private CheckQuarantinedProductImage checker(Limits limits) {
        return new CheckQuarantinedProductImage(storage, observations, scanner, immediate(), immediate(), limits,
                Clock.fixed(NOW, ZoneOffset.UTC), () -> CHECK);
    }
    private static RetrievedAsset retrieved(byte[] bytes, String format) {
        return new RetrievedAsset(new QuarantinedProductImageAsset(ASSET, PRODUCT, digest(bytes),
                "jpeg".equals(format) ? QuarantinedProductImageAsset.Format.JPEG : QuarantinedProductImageAsset.Format.PNG,
                bytes.length, 1, 1, QuarantinedProductImageAsset.Status.QUARANTINED, NOW), bytes);
    }
    private static String digest(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (Exception impossible) { throw new IllegalStateException(impossible); }
    }
    private static byte[] image(String format, int width, int height) throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(width, height, "jpeg".equals(format) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB), format, output);
        return output.toByteArray();
    }
    @SuppressWarnings("unchecked")
    private static TransactionOperations immediate() {
        var operations = mock(TransactionOperations.class);
        when(operations.execute(any())).thenAnswer(invocation -> ((TransactionCallback<Object>) invocation.getArgument(0))
                .doInTransaction(mock(TransactionStatus.class)));
        return operations;
    }

    @SuppressWarnings("unchecked")
    private static TransactionOperations trackedTransaction() {
        TransactionOperations transactions = mock(TransactionOperations.class);
        when(transactions.execute(any())).thenAnswer(invocation -> {
            org.springframework.transaction.support.TransactionSynchronizationManager
                    .setActualTransactionActive(true);
            try {
                return ((TransactionCallback<Object>) invocation.getArgument(0))
                        .doInTransaction(mock(TransactionStatus.class));
            } finally {
                org.springframework.transaction.support.TransactionSynchronizationManager
                        .setActualTransactionActive(false);
            }
        });
        return transactions;
    }
}
