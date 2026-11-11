package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage.Failure;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage.Reason;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Outcome;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductImageIngestionIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private IngestProductImage ingestion;
    @Autowired private ProductPersistenceService products;
    @Autowired private AssessProductPublicationReadiness readiness;
    @Autowired private JdbcTemplate jdbc;

    @Test
    void originalBytesAndMetadataCommitInPrivateImmutableQuarantineOnly() throws Exception {
        Product product = product();
        products.create(product);
        byte[] bytes = png(2, 3);

        var asset = ingestion.ingest(product.id(), bytes);

        var stored = jdbc.queryForMap("""
                SELECT product_id, sha256, detected_format, byte_length, pixel_width, pixel_height,
                       quarantine_status, original_bytes
                FROM product.product_image_quarantine WHERE asset_id = ?
                """, asset.assetId());
        assertThat(stored.get("product_id")).isEqualTo(product.id().value());
        assertThat(stored.get("sha256")).isEqualTo(asset.sha256());
        assertThat(stored.get("detected_format")).isEqualTo("PNG");
        assertThat(((Number) stored.get("byte_length")).longValue()).isEqualTo(bytes.length);
        assertThat(stored.get("pixel_width")).isEqualTo(2);
        assertThat(stored.get("pixel_height")).isEqualTo(3);
        assertThat(stored.get("quarantine_status")).isEqualTo("QUARANTINED");
        assertThat((byte[]) stored.get("original_bytes")).containsExactly(bytes);

        assertThatThrownBy(() -> jdbc.update("""
                UPDATE product.product_image_quarantine SET quarantine_status = 'QUARANTINED'
                WHERE asset_id = ?
                """, asset.assetId())).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update(
                "DELETE FROM product.product_image_quarantine WHERE asset_id = ?", asset.assetId()))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.evidence_event", Integer.class)).isZero();
        assertThat(readiness.assess(product).outcome()).isEqualTo(Outcome.EVIDENCE_UNAVAILABLE);
    }

    @Test
    void unknownProductForeignKeyBoundaryCannotLeaveAnOrphanAsset() throws Exception {
        ProductId unknown = new ProductId("unknown-" + UUID.randomUUID());

        assertThatThrownBy(() -> ingestion.ingest(unknown, png(1, 1)))
                .isInstanceOfSatisfying(Failure.class,
                        failure -> assertThat(failure.reason()).isEqualTo(Reason.PRODUCT_NOT_FOUND));
        assertThat(jdbc.queryForObject("""
                SELECT count(*) FROM product.product_image_quarantine WHERE product_id = ?
                """, Integer.class, unknown.value())).isZero();
    }

    private static Product product() {
        return new Product(new ProductId("product-" + UUID.randomUUID()), ProductFamily.APPAREL,
                ProductAttributeStandard.INITIAL, true, false);
    }

    private static byte[] png(int width, int height) throws Exception {
        var output = new ByteArrayOutputStream();
        assertThat(ImageIO.write(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), "png", output))
                .isTrue();
        return output.toByteArray();
    }
}
