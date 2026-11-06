package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/**
 * Stateless v1 continuation for the UTF-8 Product-ID ordering. Not visibility evidence.
 * Configure product.catalogue.cursor-secret (PRODUCT_CATALOGUE_CURSOR_SECRET) from an external
 * secret store: standard Base64 of exactly 32 cryptographically random bytes, shared by replicas.
 * No fallback key. Replacing the secret invalidates outstanding cursors; never log keys or tokens.
 * Separate derived encryption/MAC keys prevent disclosure of even internal boundary identifiers.
 */
public final class ProductCatalogueCursorCodec {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final byte[] CONTEXT = "product-catalogue:utf8-id:exclusive:v1".getBytes(StandardCharsets.US_ASCII);
    private final byte[] encryptionKey;
    private final byte[] signingKey;
    private final SecureRandom random = new SecureRandom();

    public ProductCatalogueCursorCodec(String configuredSecret) {
        byte[] encryption = null;
        byte[] signing = null;
        try {
            byte[] secret = Base64.getDecoder().decode(configuredSecret == null ? "" : configuredSecret);
            try {
                if (secret.length == 32) {
                    encryption = mac(secret, "catalogue-encryption-v1".getBytes(StandardCharsets.US_ASCII));
                    signing = mac(secret, "catalogue-signing-v1".getBytes(StandardCharsets.US_ASCII));
                }
            } finally {
                Arrays.fill(secret, (byte) 0);
            }
        } catch (IllegalArgumentException | GeneralSecurityException invalid) {
            // Keep detail retrieval available; listing fails closed with a safe 503.
        }
        encryptionKey = encryption;
        signingKey = signing;
    }

    public void requireAvailable() {
        if (encryptionKey == null || signingKey == null) {
            throw new Unavailable();
        }
    }

    /** Only a future confirmed eligible page may call this; candidate pages must never be issued. */
    String encode(ProductIdPage.Cursor cursor) {
        requireAvailable();
        try {
            byte[] nonce = new byte[12];
            random.nextBytes(nonce);
            byte[] id = cursor.afterExclusive().value().getBytes(StandardCharsets.US_ASCII);
            byte[] payload = ByteBuffer.allocate(4 + id.length).putInt(cursor.pageSize()).put(id).array();
            Cipher cipher = cipher(Cipher.ENCRYPT_MODE, nonce);
            byte[] encrypted = cipher.doFinal(payload);
            byte[] frame = ByteBuffer.allocate(1 + nonce.length + encrypted.length)
                    .put((byte) 1).put(nonce).put(encrypted).array();
            return ENCODER.encodeToString(ByteBuffer.allocate(frame.length + 32).put(frame).put(mac(signingKey, frame)).array());
        } catch (GeneralSecurityException failure) {
            throw new Unavailable();
        }
    }

    public ProductIdPage.Cursor decode(String token, int effectiveSize) {
        ProductIdPage.validateSize(effectiveSize);
        if (token == null || token.length() > 2048 || !token.matches("[A-Za-z0-9_-]+")) {
            throw malformed();
        }
        final byte[] bytes;
        try {
            bytes = Base64.getUrlDecoder().decode(token);
        } catch (IllegalArgumentException invalid) {
            throw malformed();
        }
        if (bytes.length < 66 || bytes.length > 193 || !ENCODER.encodeToString(bytes).equals(token) || bytes[0] != 1) {
            throw malformed();
        }
        requireAvailable();
        try {
            byte[] frame = Arrays.copyOf(bytes, bytes.length - 32);
            byte[] signature = Arrays.copyOfRange(bytes, frame.length, bytes.length);
            if (!MessageDigest.isEqual(mac(signingKey, frame), signature)) {
                throw malformed();
            }
            byte[] nonce = Arrays.copyOfRange(frame, 1, 13);
            byte[] plaintext = cipher(Cipher.DECRYPT_MODE, nonce).doFinal(Arrays.copyOfRange(frame, 13, frame.length));
            ByteBuffer payload = ByteBuffer.wrap(plaintext);
            int size = payload.getInt();
            byte[] id = new byte[payload.remaining()];
            payload.get(id);
            // Reject non-ASCII bytes before decoding, rather than replacing malformed input.
            for (byte value : id) {
                if (value < 0) { throw malformed(); }
            }
            ProductIdPage.Cursor cursor = new ProductIdPage.Cursor(new ProductId(new String(id, StandardCharsets.US_ASCII)), size);
            if (size != effectiveSize) { throw malformed(); }
            return cursor;
        } catch (GeneralSecurityException invalid) {
            throw malformed();
        }
    }

    private Cipher cipher(int mode, byte[] nonce) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(mode, new SecretKeySpec(encryptionKey, "AES"), new GCMParameterSpec(128, nonce));
        cipher.updateAAD(CONTEXT);
        return cipher;
    }

    private static byte[] mac(byte[] key, byte[] value) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(value);
    }

    private static IllegalArgumentException malformed() {
        return new IllegalArgumentException("Invalid catalogue continuation");
    }

    public static final class Unavailable extends RuntimeException {
        private Unavailable() { super("Catalogue continuation unavailable"); }
    }
}
