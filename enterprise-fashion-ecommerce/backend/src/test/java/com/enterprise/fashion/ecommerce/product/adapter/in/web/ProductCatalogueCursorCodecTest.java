package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueQueryService;
import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCatalogueCursorCodecTest {
    // Synthetic key only; production configuration has no default.
    private static final String SECRET = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";
    private final ProductCatalogueCursorCodec codec = new ProductCatalogueCursorCodec(SECRET);

    @Test
    void roundTripsExactBoundaryAndSizeWithRandomizedConfidentialTokens() {
        var position = new ProductIdPage.Cursor(new ProductId("A_z-09"), 20);
        String token = codec.encode(position);
        assertThat(token).matches("[A-Za-z0-9_-]{1,2048}");
        assertThat(codec.decode(token, 20)).isEqualTo(position);
        assertThat(new ProductCatalogueCursorCodec(SECRET).decode(token, 20)).isEqualTo(position);
        assertThat(codec.encode(position)).isNotEqualTo(token);
        assertThat(new String(Base64.getUrlDecoder().decode(token), StandardCharsets.ISO_8859_1)).doesNotContain("A_z-09");
    }

    @Test
    void acceptsLongestCanonicalBoundaryAndMaximumSize() {
        var position = new ProductIdPage.Cursor(new ProductId("z".repeat(128)), 100);
        assertThat(codec.decode(codec.encode(position), 100)).isEqualTo(position);
    }

    @Test
    void rejectsEverySingleByteManipulationIncludingVersionNonceCiphertextAndMac() {
        byte[] original = Base64.getUrlDecoder().decode(codec.encode(new ProductIdPage.Cursor(new ProductId("boundary"), 20)));
        for (int index = 0; index < original.length; index++) {
            byte[] changed = original.clone();
            changed[index] ^= 1;
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(changed);
            assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 20));
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"raw-product-id", "bad+token", "AA==", "AA", " A", "a.b"})
    void rejectsMalformedTokens(String token) {
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 20));
    }

    @Test
    void rejectsOversizedTokensAndPageSizeMismatch() {
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode("A".repeat(2049), 20));
        String token = codec.encode(new ProductIdPage.Cursor(new ProductId("boundary"), 1));
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 20));
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 0));
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 101));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"not-base64!", "AAAA", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="})
    void invalidConfigurationFailsClosedWithoutCallingQueries(String secret) throws Exception {
        var unavailable = new ProductCatalogueCursorCodec(secret);
        assertThatThrownBy(unavailable::requireAvailable).isInstanceOf(ProductCatalogueCursorCodec.Unavailable.class);
        String token = codec.encode(new ProductIdPage.Cursor(new ProductId("boundary"), 20));
        assertThatThrownBy(() -> unavailable.decode(token, 20)).isInstanceOf(ProductCatalogueCursorCodec.Unavailable.class);
        var queries = mock(ProductCatalogueQueryService.class);
        var http = MockMvcBuilders.standaloneSetup(new ProductCatalogueController(queries, unavailable)).build();
        var response = http.perform(get("/api/v1/products")).andExpect(status().isServiceUnavailable())
                .andExpect(header().string("Cache-Control", "no-store")).andReturn();
        assertThat(response.getResponse().getContentAsString()).doesNotContain("secret", "Exception");
        assertThat(response.getRequest().getSession(false)).isNull();
        verifyNoInteractions(queries);
    }

    @Test
    void keyReplacementRejectsOutstandingContinuation() {
        String otherSecret = Base64.getEncoder().encodeToString("11111111111111111111111111111111".getBytes(StandardCharsets.US_ASCII));
        String token = codec.encode(new ProductIdPage.Cursor(new ProductId("boundary"), 20));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductCatalogueCursorCodec(otherSecret).decode(token, 20));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bad.id", "", "é", "boundary with spaces"})
    void authenticatedButInvalidBoundariesAreRejectedBeforeNormalization(String boundary) throws Exception {
        String token = syntheticToken(20, boundary);
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(token, 20));
    }

    @Test
    void authenticatedInvalidSizeAndUnsupportedVersionAreRejected() throws Exception {
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(syntheticToken(101, "boundary"), 20));
        byte[] bytes = Base64.getUrlDecoder().decode(syntheticToken(20, "boundary"));
        bytes[0] = 2;
        assertThatIllegalArgumentException().isThrownBy(() -> codec.decode(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes), 20));
    }

    /** Constructs hostile authenticated inputs solely in this isolated protocol test. */
    private String syntheticToken(int size, String boundary) throws Exception {
        byte[] secret = Base64.getDecoder().decode(SECRET);
        Mac derivation = Mac.getInstance("HmacSHA256");
        derivation.init(new SecretKeySpec(secret, "HmacSHA256"));
        byte[] encryption = derivation.doFinal("catalogue-encryption-v1".getBytes(StandardCharsets.US_ASCII));
        byte[] signing = derivation.doFinal("catalogue-signing-v1".getBytes(StandardCharsets.US_ASCII));
        byte[] nonce = new byte[12];
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryption, "AES"), new GCMParameterSpec(128, nonce));
        cipher.updateAAD("product-catalogue:utf8-id:exclusive:v1".getBytes(StandardCharsets.US_ASCII));
        byte[] id = boundary.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = cipher.doFinal(ByteBuffer.allocate(4 + id.length).putInt(size).put(id).array());
        byte[] frame = ByteBuffer.allocate(13 + encrypted.length).put((byte) 1).put(nonce).put(encrypted).array();
        Mac signature = Mac.getInstance("HmacSHA256");
        signature.init(new SecretKeySpec(signing, "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                ByteBuffer.allocate(frame.length + 32).put(frame).put(signature.doFinal(frame)).array());
    }
}
