package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.enterprise.fashion.ecommerce.identity.domain.model.PreparedProspectiveCustomerPassword;
import org.junit.jupiter.api.Test;

import static com.enterprise.fashion.ecommerce.identity.application.credential.ClosedPasswordScreening.Outcome.*;
import static org.assertj.core.api.Assertions.assertThat;

class ClosedPasswordScreeningTest {
    private static final Instant NOW = Instant.parse("2026-11-27T00:00:00Z");
    private static final String FIXTURE = "synthetic-value-é";
    private final TestClock clock = new TestClock();
    private final ClosedPasswordScreening screening = new ClosedPasswordScreening(clock);

    @Test
    void screensOnlyTheCompleteNfcPreparedValue() throws Exception {
        byte[] payload = payload(FIXTURE);
        assertThat(screening.replace(manifest(1, 1, payload), payload)).isTrue();
        assertThat(screening.screen(prepare("synthetic-value-e\u0301"))).isEqualTo(BLOCKED);
        for (String distinct : new String[]{"Synthetic-value-é", " " + FIXTURE, FIXTURE + " ",
                FIXTURE + "-suffix", "prefix-" + FIXTURE, "synthetic-value-"}) {
            assertThat(screening.screen(prepare(distinct))).isEqualTo(NOT_PRESENT);
        }
    }

    @Test
    void rejectsMalformedIncompleteAndCorruptSnapshotsWithoutReplacingValidState() throws Exception {
        byte[] valid = payload(FIXTURE);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isTrue();
        assertThat(screening.replace(null, valid)).isFalse();
        assertThat(screening.replace(new ClosedPasswordScreening.Manifest("unknown", 2, 1, null, NOW, null), valid))
                .isFalse();
        assertThat(screening.replace(manifest(2, 2, valid), valid)).isFalse();
        assertThat(screening.replace(manifest(2, 1, valid), payload("another-synthetic-value"))).isFalse();
        byte[] malformed = ByteBuffer.allocate(5).putInt(1).put((byte) 0xff).array();
        assertThat(screening.replace(manifest(2, 1, malformed), malformed)).isFalse();
        byte[] duplicate = payload(FIXTURE, FIXTURE);
        assertThat(screening.replace(manifest(2, 2, duplicate), duplicate)).isFalse();
        byte[] nonCanonical = payload("synthetic-value-e\u0301");
        assertThat(screening.replace(manifest(2, 1, nonCanonical), nonCanonical)).isFalse();
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(BLOCKED);
        assertThat(screening.screen(prepare("another-synthetic-value"))).isEqualTo(NOT_PRESENT);
    }

    @Test
    void failedReplacementDoesNotExtendFreshnessAndExpiredCandidateCannotPublish() throws Exception {
        byte[] valid = payload(FIXTURE);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isTrue();
        clock.now.set(NOW.plusSeconds(60));
        assertThat(screening.replace(manifest(2, 1, valid), valid)).isFalse();
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(UNAVAILABLE);
    }

    @Test
    void freshnessWindowIsStartInclusiveAndEndExclusive() throws Exception {
        byte[] valid = payload(FIXTURE);
        var password = prepare(FIXTURE);
        clock.now.set(NOW.minusNanos(1));
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isFalse();
        clock.now.set(NOW);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isTrue();
        assertThat(screening.screen(password)).isEqualTo(BLOCKED);
        clock.now.set(NOW.minusNanos(1));
        assertThat(screening.screen(password)).isEqualTo(UNAVAILABLE);
        clock.now.set(NOW.plusSeconds(60).minusNanos(1));
        assertThat(screening.screen(password)).isEqualTo(BLOCKED);
        clock.now.set(NOW.plusSeconds(60));
        assertThat(screening.screen(password)).isEqualTo(UNAVAILABLE);
        clock.now.set(NOW);
        assertThat(screening.screen(password)).isEqualTo(UNAVAILABLE);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isFalse();
        assertThat(screening.replace(manifest(2, 1, valid), valid)).isTrue();
        assertThat(screening.screen(password)).isEqualTo(BLOCKED);
    }

    @Test
    void unavailableClockDeniesObservationAndPublication() throws Exception {
        byte[] valid = payload(FIXTURE);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isTrue();
        clock.now.set(null);
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(UNAVAILABLE);
        assertThat(screening.replace(manifest(2, 1, valid), valid)).isFalse();
        clock.nextRead.set(() -> { throw new IllegalStateException("Test clock unavailable"); });
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(UNAVAILABLE);
    }

    @Test
    void expiryObserverCannotRetireAConcurrentlyPublishedReplacement() throws Exception {
        byte[] original = payload(FIXTURE);
        byte[] newer = payload("newer-synthetic-value");
        assertThat(screening.replace(manifest(1, 1, original), original)).isTrue();
        var readingClock = new CountDownLatch(1);
        var releaseClock = new CountDownLatch(1);
        clock.nextRead.set(() -> {
            readingClock.countDown();
            try {
                if (!releaseClock.await(5, TimeUnit.SECONDS)) {
                    throw new AssertionError("Expiry reader did not resume");
                }
                return NOW.plusSeconds(60);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new AssertionError(interrupted);
            }
        });
        try (var worker = Executors.newSingleThreadExecutor()) {
            var observer = worker.submit(() -> screening.screen(prepare(FIXTURE)));
            try {
                assertThat(readingClock.await(5, TimeUnit.SECONDS)).isTrue();
                assertThat(screening.replace(manifest(2, 1, newer), newer)).isTrue();
            } finally {
                releaseClock.countDown();
            }
            assertThat(observer.get(5, TimeUnit.SECONDS)).isEqualTo(UNAVAILABLE);
        }
        assertThat(screening.screen(prepare("newer-synthetic-value"))).isEqualTo(BLOCKED);
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(NOT_PRESENT);
    }

    @Test
    void rejectsMalformedEntryFramingWithoutReplacingTheCurrentSnapshot() throws Exception {
        byte[] valid = payload(FIXTURE);
        assertThat(screening.replace(manifest(1, 1, valid), valid)).isTrue();
        byte[][] malformed = {
            ByteBuffer.allocate(4).putInt(-1).array(),
            ByteBuffer.allocate(4).putInt(0).array(),
            ByteBuffer.allocate(261).putInt(257).array(),
            new byte[3],
            java.util.Arrays.copyOf(valid, valid.length - 1),
            java.util.Arrays.copyOf(valid, valid.length + 1)
        };
        for (byte[] bytes : malformed) {
            assertThat(screening.replace(manifest(2, 1, bytes), bytes)).isFalse();
            assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(BLOCKED);
        }
    }

    @Test
    void rejectsInvalidManifestBoundsAndNonIncreasingVersionsWithoutReplacingCurrentSnapshot() throws Exception {
        byte[] valid = payload(FIXTURE);
        byte[] candidate = payload("newer-synthetic-value");
        assertThat(screening.replace(manifest(2, 1, valid), valid)).isTrue();
        var candidateManifest = manifest(3, 1, candidate);
        var invalid = java.util.List.of(
                manifest(3, 100_001, candidate),
                new ClosedPasswordScreening.Manifest(ClosedPasswordScreening.FORMAT, 3, 1,
                        "g".repeat(64), NOW, NOW.plusSeconds(60)),
                new ClosedPasswordScreening.Manifest(ClosedPasswordScreening.FORMAT, 3, 1,
                        candidateManifest.payloadSha256(), NOW, NOW),
                manifest(1, 1, candidate),
                manifest(2, 1, candidate));
        for (var manifest : invalid) {
            assertThat(screening.replace(manifest, candidate)).isFalse();
            assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(BLOCKED);
            assertThat(screening.screen(prepare("newer-synthetic-value"))).isEqualTo(NOT_PRESENT);
        }
        byte[] oversized = new byte[16 * 1024 * 1024 + 1];
        assertThat(screening.replace(manifest(3, 1, oversized), oversized)).isFalse();
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(BLOCKED);
    }

    @Test
    void interruptedReplacementPreservesInterruptAndCurrentSnapshot() throws Exception {
        byte[] existing = payload(FIXTURE);
        byte[] candidate = payload("another-synthetic-value");
        assertThat(screening.replace(manifest(1, 1, existing), existing)).isTrue();
        var replacement = manifest(2, 1, candidate);
        try (var worker = Executors.newSingleThreadExecutor()) {
            assertThat(worker.submit(() -> {
                Thread.currentThread().interrupt();
                return !screening.replace(replacement, candidate) && Thread.currentThread().isInterrupted();
            }).get(5, TimeUnit.SECONDS)).isTrue();
        }
        assertThat(screening.screen(prepare(FIXTURE))).isEqualTo(BLOCKED);
    }

    @Test
    void readersObserveCoherentSnapshotsAndConcurrentPublicationCannotRegress() throws Exception {
        byte[] initial = payload(FIXTURE, "synthetic-initial-only");
        byte[] second = payload(FIXTURE, "synthetic-second-only");
        byte[] third = payload(FIXTURE, "synthetic-third-only");
        assertThat(screening.replace(manifest(1, 2, initial), initial)).isTrue();
        var password = prepare(FIXTURE);
        var start = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(3)) {
            var reader = workers.submit(() -> {
                start.await();
                for (int i = 0; i < 10_000; i++) {
                    assertThat(screening.screen(password)).isEqualTo(BLOCKED);
                }
                return true;
            });
            var older = workers.submit(() -> {
                start.await();
                return screening.replace(manifest(2, 2, second), second);
            });
            var newer = workers.submit(() -> {
                start.await();
                return screening.replace(manifest(3, 2, third), third);
            });
            start.countDown();
            assertThat(newer.get(5, TimeUnit.SECONDS)).isTrue();
            older.get(5, TimeUnit.SECONDS);
            assertThat(reader.get(5, TimeUnit.SECONDS)).isTrue();
        }
        assertThat(screening.screen(prepare("synthetic-third-only"))).isEqualTo(BLOCKED);
        assertThat(screening.screen(prepare("synthetic-second-only"))).isEqualTo(NOT_PRESENT);
    }

    @Test
    void isolatedCredentialOperationRequiresEligibleNotPresentEvidence() throws Exception {
        var attempts = new AtomicInteger();
        var password = prepare(FIXTURE);
        byte[] bytes = payload(FIXTURE);
        screenThenInvoke(password, attempts::incrementAndGet);
        assertThat(screening.replace(manifest(1, 2, bytes), bytes)).isFalse();
        screenThenInvoke(password, attempts::incrementAndGet);
        assertThat(screening.replace(manifest(1, 1, bytes), bytes)).isTrue();
        screenThenInvoke(password, attempts::incrementAndGet);
        clock.now.set(null);
        screenThenInvoke(prepare("another-synthetic-value"), attempts::incrementAndGet);
        clock.now.set(NOW.plusSeconds(60));
        screenThenInvoke(prepare("another-synthetic-value"), attempts::incrementAndGet);
        assertThat(attempts.get()).isZero();
        clock.now.set(NOW);
        screenThenInvoke(prepare("another-synthetic-value"), attempts::incrementAndGet);
        assertThat(attempts.get()).isZero();
        assertThat(screening.replace(manifest(2, 1, bytes), bytes)).isTrue();
        screenThenInvoke(prepare("another-synthetic-value"), attempts::incrementAndGet);
        assertThat(attempts.get()).isOne();
    }

    @Test
    void manifestDiagnosticsDoNotExposeSuppliedValues() throws Exception {
        var manifest = manifest(1, 1, payload(FIXTURE));
        assertThat(manifest.toString()).isEqualTo("ScreeningManifest[redacted]");
        assertThat(prepare(FIXTURE).toString()).doesNotContain(FIXTURE);
    }

    private void screenThenInvoke(PreparedProspectiveCustomerPassword password, Runnable operation) {
        if (screening.screen(password) == NOT_PRESENT) {
            operation.run();
        }
    }

    private static PreparedProspectiveCustomerPassword prepare(String value) {
        return PreparedProspectiveCustomerPassword.prepareForRemainingPolicyChecks(value);
    }

    private static byte[] payload(String... values) {
        var output = new java.io.ByteArrayOutputStream();
        for (String value : values) {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            output.writeBytes(ByteBuffer.allocate(Integer.BYTES).putInt(bytes.length).array());
            output.writeBytes(bytes);
        }
        return output.toByteArray();
    }

    private static ClosedPasswordScreening.Manifest manifest(long version, int count, byte[] bytes) throws Exception {
        String digest = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        return new ClosedPasswordScreening.Manifest(ClosedPasswordScreening.FORMAT, version, count, digest,
                NOW, NOW.plusSeconds(60));
    }

    private static final class TestClock extends Clock {
        private final AtomicReference<Instant> now = new AtomicReference<>(NOW);
        private final AtomicReference<java.util.function.Supplier<Instant>> nextRead = new AtomicReference<>();

        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { throw new UnsupportedOperationException(); }
        @Override public Instant instant() {
            var override = nextRead.getAndSet(null);
            return override == null ? now.get() : override.get();
        }
    }
}
