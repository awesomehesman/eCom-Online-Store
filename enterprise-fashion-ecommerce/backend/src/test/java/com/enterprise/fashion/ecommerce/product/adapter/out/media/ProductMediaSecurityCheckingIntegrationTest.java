package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.CheckQuarantinedProductImage;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductMediaSecurityCheckingIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired ProductPersistenceService products;
    @Autowired IngestProductImage ingestion;
    @Autowired CheckQuarantinedProductImage checking;
    @Autowired AssessProductPublicationReadiness readiness;
    @Autowired JdbcTemplate jdbc;

    @Test
    void validQuarantinedBytesProduceImmutableNonAuthoritativeHistoryOnly() throws Exception {
        Product product = product(); products.create(product);
        var asset = ingestion.ingest(product.id(), image("png"));
        var result = checking.check(product.id(), asset.assetId());

        var row = jdbc.queryForMap("SELECT * FROM product.product_image_security_check WHERE check_id = ?", result.checkId());
        assertThat(row.get("outcome")).isEqualTo("TECHNICAL_CHECKS_PASSED");
        assertThat(row.get("malware_scan_status")).isEqualTo("NOT_PERFORMED");
        assertThat(row.get("authoritative_acceptance_eligible")).isEqualTo(false);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.evidence_event", Integer.class)).isZero();
        assertThat(readiness.assess(product).outcome()).isEqualTo(ProductPublicationReadiness.Outcome.EVIDENCE_UNAVAILABLE);
        assertThatThrownBy(() -> jdbc.update("DELETE FROM product.product_image_security_check WHERE check_id = ?", result.checkId()))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }

    @Test
    void storedDigestMismatchAndWrongOwnershipFailClosedWithoutHistory() throws Exception {
        Product owner = product(); Product other = product(); products.create(owner); products.create(other);
        byte[] bytes = image("png"); UUID asset = UUID.randomUUID();
        insert(asset, owner.id(), "0".repeat(64), bytes, "PNG");
        assertThatThrownBy(() -> checking.check(owner.id(), asset))
                .isInstanceOfSatisfying(ProductImageSecurityCheckPort.Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(ProductImageSecurityCheckPort.FailureReason.DIGEST_MISMATCH));
        assertThatThrownBy(() -> checking.check(other.id(), asset))
                .isInstanceOfSatisfying(ProductImageSecurityCheckPort.Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(ProductImageSecurityCheckPort.FailureReason.WRONG_PRODUCT_OWNER));
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_image_security_check WHERE asset_id = ?", Integer.class, asset)).isZero();
    }

    @Test
    void malformedStoredContentIsRejectedAndNoPublicMediaControllerExists() {
        Product product = product(); products.create(product);
        byte[] malformed = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a, 1}; UUID asset = UUID.randomUUID();
        insert(asset, product.id(), digest(malformed), malformed, "PNG");
        assertThat(checking.check(product.id(), asset).outcome())
                .isEqualTo(com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult.Outcome.REJECTED);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_image_security_check WHERE asset_id = ?", Integer.class, asset)).isOne();
        assertThat(org.springframework.web.bind.annotation.RestController.class.isAssignableFrom(JdbcProductImageSecurityCheckAdapter.class)).isFalse();
    }

    private void insert(UUID asset, ProductId owner, String digest, byte[] bytes, String format) {
        jdbc.update("""
                INSERT INTO product.product_image_quarantine
                (asset_id, product_id, sha256, detected_format, byte_length, pixel_width, pixel_height,
                 quarantine_status, created_at, original_bytes) VALUES (?, ?, ?, ?, ?, 1, 1, 'QUARANTINED', now(), ?)
                """, asset, owner.value(), digest, format, bytes.length, bytes);
    }
    private static Product product() {
        return new Product(new ProductId("product-" + UUID.randomUUID()), ProductFamily.APPAREL,
                ProductAttributeStandard.INITIAL, true, false);
    }
    private static byte[] image(String format) throws Exception {
        var out = new ByteArrayOutputStream(); ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), format, out); return out.toByteArray();
    }
    private static String digest(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (Exception impossible) { throw new IllegalStateException(impossible); }
    }
}
