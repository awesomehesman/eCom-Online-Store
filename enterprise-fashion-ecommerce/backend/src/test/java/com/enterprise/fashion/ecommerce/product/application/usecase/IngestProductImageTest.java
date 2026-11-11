package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset.Format;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset.Status;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageQuarantinePort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage.Failure;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage.Limits;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage.Reason;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngestProductImageTest {
    private static final ProductId PRODUCT_ID = new ProductId("product-one");
    private static final UUID ASSET_ID = UUID.fromString("77fd9691-eaf7-41c0-8910-175943348963");
    private static final Instant NOW = Instant.parse("2026-11-11T12:00:00Z");
    private final ProductPersistencePort products = mock(ProductPersistencePort.class);
    private final ProductImageQuarantinePort quarantine = mock(ProductImageQuarantinePort.class);
    private final TransactionOperations writes = immediateTransaction();

    @Test
    void validPngIsContentInspectedHasInternalIdentityAndIsStoredOnlyAsQuarantined() throws Exception {
        byte[] supplied = image("png", 3, 2);
        when(products.load(PRODUCT_ID)).thenReturn(Optional.of(product()));
        var useCase = useCase(new Limits(1024 * 1024, 100, 100, 10_000));

        var result = useCase.ingest(PRODUCT_ID, supplied);

        assertThat(result.assetId()).isEqualTo(ASSET_ID);
        assertThat(result.productId()).isEqualTo(PRODUCT_ID);
        assertThat(result.format()).isEqualTo(Format.PNG);
        assertThat(result.status()).isEqualTo(Status.QUARANTINED);
        assertThat(result.pixelWidth()).isEqualTo(3);
        assertThat(result.pixelHeight()).isEqualTo(2);
        assertThat(result.byteLength()).isEqualTo(supplied.length);
        assertThat(result.sha256()).matches("[0-9a-f]{64}");
        assertThat(result.assetRevision()).isEqualTo("sha256_" + result.sha256());
        assertThat(result.createdAt()).isEqualTo(NOW);

        var payload = ArgumentCaptor.forClass(ProductImageQuarantinePort.Payload.class);
        verify(quarantine).store(payload.capture());
        assertThat(payload.getValue().originalBytes()).containsExactly(supplied);
        supplied[0] = 0;
        assertThat(payload.getValue().originalBytes()[0]).isNotZero();
    }

    @Test
    void validJpegIsDetectedFromBytesWithoutFilenameOrMimeClaims() throws Exception {
        byte[] jpeg = image("jpeg", 2, 2);
        when(products.load(PRODUCT_ID)).thenReturn(Optional.of(product()));

        var result = useCase(new Limits(1024 * 1024, 100, 100, 10_000)).ingest(PRODUCT_ID, jpeg);

        assertThat(result.format()).isEqualTo(Format.JPEG);
        assertThat(IngestProductImage.class.getDeclaredMethods())
                .filteredOn(method -> method.getName().equals("ingest"))
                .singleElement().satisfies(method -> assertThat(method.getParameterTypes())
                        .containsExactly(ProductId.class, byte[].class));
    }

    @Test
    void malformedAndUnsupportedContentAreRejectedBeforeStorage() {
        byte[] malformedPng = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a, 1, 2, 3};
        byte[] gif = "GIF89a-not-an-approved-image".getBytes(java.nio.charset.StandardCharsets.US_ASCII);

        assertFailure(malformedPng, Reason.MALFORMED_CONTENT);
        assertFailure(gif, Reason.UNSUPPORTED_FORMAT);
        assertFailure(new byte[] {1, 2, 3}, Reason.MALFORMED_CONTENT);
        verify(quarantine, never()).store(any());
        verify(products, never()).load(any());
    }

    @Test
    void byteDimensionAndPixelLimitsRejectBeforePersistence() throws Exception {
        byte[] image = image("png", 4, 3);
        assertThatThrownBy(() -> useCase(new Limits(image.length - 1, 100, 100, 10_000))
                .ingest(PRODUCT_ID, image)).isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(Reason.UPLOAD_TOO_LARGE));
        assertThatThrownBy(() -> useCase(new Limits(1024 * 1024, 3, 100, 300))
                .ingest(PRODUCT_ID, image)).isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(Reason.IMAGE_RESOURCE_LIMIT_EXCEEDED));
        assertThatThrownBy(() -> useCase(new Limits(1024 * 1024, 100, 100, 11))
                .ingest(PRODUCT_ID, image)).isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(Reason.IMAGE_RESOURCE_LIMIT_EXCEEDED));
        verify(quarantine, never()).store(any());
    }

    @Test
    void unknownProductAndStorageFailureCannotReturnAnAsset() throws Exception {
        byte[] bytes = image("png", 1, 1);
        when(products.load(PRODUCT_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase(Limits.conservativeLocalDefaults()).ingest(PRODUCT_ID, bytes))
                .isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(Reason.PRODUCT_NOT_FOUND));
        verify(quarantine, never()).store(any());

        when(products.load(PRODUCT_ID)).thenReturn(Optional.of(product()));
        IllegalStateException storageFailure = new IllegalStateException("quarantine unavailable");
        doThrow(storageFailure).when(quarantine).store(any());
        assertThatThrownBy(() -> useCase(Limits.conservativeLocalDefaults()).ingest(PRODUCT_ID, bytes))
                .isSameAs(storageFailure);
    }

    @Test
    void payloadIsDefensiveAndTheStoragePortExposesNoReadOrServingOperation() throws Exception {
        byte[] bytes = image("png", 1, 1);
        when(products.load(PRODUCT_ID)).thenReturn(Optional.of(product()));
        useCase(Limits.conservativeLocalDefaults()).ingest(PRODUCT_ID, bytes);
        var payload = ArgumentCaptor.forClass(ProductImageQuarantinePort.Payload.class);
        verify(quarantine).store(payload.capture());
        byte[] observed = payload.getValue().originalBytes();
        observed[0] = 0;
        assertThat(payload.getValue().originalBytes()[0]).isNotZero();
        assertThat(ProductImageQuarantinePort.class.getDeclaredMethods())
                .extracting(java.lang.reflect.Method::getName).containsExactly("store");
    }

    private IngestProductImage useCase(Limits limits) {
        return new IngestProductImage(products, quarantine, writes, limits,
                Clock.fixed(NOW, ZoneOffset.UTC), () -> ASSET_ID);
    }

    private void assertFailure(byte[] bytes, Reason reason) {
        assertThatThrownBy(() -> useCase(Limits.conservativeLocalDefaults()).ingest(PRODUCT_ID, bytes))
                .isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(reason));
    }

    private static Product product() {
        return new Product(PRODUCT_ID, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false);
    }

    private static byte[] image(String format, int width, int height) throws Exception {
        BufferedImage image = new BufferedImage(width, height,
                "jpeg".equals(format) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
        var output = new ByteArrayOutputStream();
        assertThat(ImageIO.write(image, format, output)).isTrue();
        return output.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private static TransactionOperations immediateTransaction() {
        TransactionOperations transactions = mock(TransactionOperations.class);
        when(transactions.execute(any())).thenAnswer(invocation ->
                ((TransactionCallback<Object>) invocation.getArgument(0))
                        .doInTransaction(mock(TransactionStatus.class)));
        return transactions;
    }
}
