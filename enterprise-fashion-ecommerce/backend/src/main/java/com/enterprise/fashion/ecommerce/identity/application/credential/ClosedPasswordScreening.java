package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.enterprise.fashion.ecommerce.identity.domain.model.PreparedProspectiveCustomerPassword;

/** Unwired BL-03 screening boundary. Inputs are synthetic qualification fixtures only. */
public final class ClosedPasswordScreening {
    public static final String FORMAT = "nfc-utf8-length-prefixed-v1";
    private static final int MAX_BYTES = 16 * 1024 * 1024;
    private static final int MAX_ENTRIES = 100_000;
    private static final int MAX_ENTRY_BYTES = 64 * 4;

    private final Clock clock;
    private final AtomicReference<Snapshot> active = new AtomicReference<>();

    public ClosedPasswordScreening(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public enum Outcome { BLOCKED, NOT_PRESENT, UNAVAILABLE }

    /**
     * Each fixture entry is a big-endian 32-bit byte length followed by exact NFC UTF-8.
     * The manifest is supplied independently by the closed fixture builder.
     */
    public record Manifest(String format, long version, int entryCount, String payloadSha256,
                           Instant validFrom, Instant expiresAt) {
        @Override
        public String toString() {
            return "ScreeningManifest[redacted]";
        }

        private boolean eligibleAt(Instant now) {
            return now != null && !now.isBefore(validFrom) && now.isBefore(expiresAt);
        }
    }

    private record Snapshot(Manifest manifest, Set<String> entries) {
        @Override
        public String toString() {
            return "ScreeningSnapshot[redacted]";
        }
    }

    public Outcome screen(PreparedProspectiveCustomerPassword password) {
        Snapshot current = active.get();
        if (password == null || current == null || current.entries().isEmpty()) {
            return Outcome.UNAVAILABLE;
        }
        Instant now = time();
        if (now != null && !now.isBefore(current.manifest().expiresAt())) {
            // Retire only the observed version and retain its version as the publication floor.
            active.compareAndSet(current, new Snapshot(current.manifest(), Set.of()));
            return Outcome.UNAVAILABLE;
        }
        if (now == null || now.isBefore(current.manifest().validFrom())) {
            return Outcome.UNAVAILABLE;
        }
        return current.entries().contains(password.normalizedValue()) ? Outcome.BLOCKED : Outcome.NOT_PRESENT;
    }

    /** Validate defensively before publication. Earlier versions cannot replace a later version. */
    public boolean replace(Manifest manifest, byte[] payload) {
        if (!validManifest(manifest) || payload == null || payload.length > MAX_BYTES
                || Thread.currentThread().isInterrupted()) {
            return false;
        }

        byte[] candidate = payload.clone();
        try {
            if (!MessageDigest.isEqual(sha256(candidate), HexFormat.of().parseHex(manifest.payloadSha256()))) {
                return false;
            }
            Set<String> entries = parse(ByteBuffer.wrap(candidate), manifest.entryCount());
            return entries != null && publish(new Snapshot(manifest, Set.copyOf(entries)));
        } finally {
            java.util.Arrays.fill(candidate, (byte) 0);
        }
    }

    private boolean publish(Snapshot replacement) {
        Manifest manifest = replacement.manifest();
        Snapshot previous;
        do {
            previous = active.get();
            if (Thread.currentThread().isInterrupted() || !manifest.eligibleAt(time())
                    || (previous != null && manifest.version() <= previous.manifest().version())) {
                return false;
            }
        } while (!active.compareAndSet(previous, replacement));
        return true;
    }

    private static Set<String> parse(ByteBuffer data, int count) {
        Set<String> entries = new HashSet<>();
        try {
            for (int index = 0; index < count; index++) {
                if (Thread.currentThread().isInterrupted() || data.remaining() < Integer.BYTES) {
                    return null;
                }
                int length = data.getInt();
                if (length < 1 || length > MAX_ENTRY_BYTES || length > data.remaining()) {
                    return null;
                }
                String value = StandardCharsets.UTF_8.newDecoder()
                        .decode(data.slice(data.position(), length)).toString();
                data.position(data.position() + length);
                if (!PreparedProspectiveCustomerPassword.prepareForRemainingPolicyChecks(value)
                        .normalizedValue().equals(value) || !entries.add(value)) {
                    return null;
                }
            }
        } catch (CharacterCodingException | IllegalArgumentException invalid) {
            return null;
        }
        return data.hasRemaining() ? null : entries;
    }

    private static boolean validManifest(Manifest manifest) {
        return manifest != null && FORMAT.equals(manifest.format()) && manifest.version() > 0
                && manifest.entryCount() > 0 && manifest.entryCount() <= MAX_ENTRIES
                && manifest.payloadSha256() != null && manifest.payloadSha256().matches("[0-9a-f]{64}")
                && manifest.validFrom() != null && manifest.expiresAt() != null
                && manifest.validFrom().isBefore(manifest.expiresAt());
    }

    private Instant time() {
        try {
            return clock.instant();
        } catch (RuntimeException unavailable) {
            return null;
        }
    }

    private static byte[] sha256(byte[] bytes) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(bytes);
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("Required SHA-256 algorithm unavailable", impossible);
        }
    }
}
