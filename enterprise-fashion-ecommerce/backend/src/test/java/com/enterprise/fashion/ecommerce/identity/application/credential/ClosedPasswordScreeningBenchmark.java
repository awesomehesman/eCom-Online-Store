package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.enterprise.fashion.ecommerce.identity.domain.model.PreparedProspectiveCustomerPassword;

/** Standalone synthetic BL-03 measurement. Kept outside the timed CI test suite. */
public final class ClosedPasswordScreeningBenchmark {
    private static final int ENTRIES = 100_000;
    private static final int WORKERS = 16;
    private static final int SAMPLES_PER_WORKER = 10_000;

    private ClosedPasswordScreeningBenchmark() {}

    public static void main(String[] args) throws Exception {
        int width = generated(0).length();
        var fixture = ByteBuffer.allocate(ENTRIES * (Integer.BYTES + width));
        var hits = new PreparedProspectiveCustomerPassword[ENTRIES];
        for (int i = 0; i < ENTRIES; i++) {
            String value = generated(i);
            fixture.putInt(width).put(value.getBytes(StandardCharsets.UTF_8));
            hits[i] = PreparedProspectiveCustomerPassword.prepareForRemainingPolicyChecks(value);
        }
        var miss = PreparedProspectiveCustomerPassword.prepareForRemainingPolicyChecks(generated(ENTRIES));
        var screening = new ClosedPasswordScreening(Clock.systemUTC());
        Instant now = Instant.now();
        String checksum = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(fixture.array()));
        var manifest = new ClosedPasswordScreening.Manifest(
                ClosedPasswordScreening.FORMAT, 1, ENTRIES, checksum, now, now.plusSeconds(300));
        long start = System.nanoTime();
        if (!screening.replace(manifest, fixture.array())) {
            throw new IllegalStateException("Synthetic snapshot rejected");
        }
        System.out.printf(Locale.ROOT, "workers=%d entries=%d bytes=%d maxHeap=%d loadMs=%.3f%n",
                WORKERS, ENTRIES, fixture.capacity(), Runtime.getRuntime().maxMemory(),
                (System.nanoTime() - start) / 1_000_000.0);
        try (var pool = Executors.newFixedThreadPool(WORKERS)) {
            for (String phase : new String[]{"cold first batch", "warm-up", "warm"}) {
                var barrier = new CountDownLatch(WORKERS);
                var futures = new java.util.ArrayList<java.util.concurrent.Future<long[]>>();
                for (int worker = 0; worker < WORKERS; worker++) {
                    int offset = worker * 1543;
                    futures.add(pool.submit(() -> {
                        barrier.countDown();
                        barrier.await();
                        long[] timings = new long[SAMPLES_PER_WORKER];
                        for (int i = 0; i < timings.length; i++) {
                            boolean hit = i % 2 == 0;
                            var password = hit ? hits[(offset + i * 13) % ENTRIES] : miss;
                            long before = System.nanoTime();
                            var outcome = screening.screen(password);
                            timings[i] = System.nanoTime() - before;
                            if (outcome != (hit ? ClosedPasswordScreening.Outcome.BLOCKED
                                    : ClosedPasswordScreening.Outcome.NOT_PRESENT)) {
                                throw new IllegalStateException("Unexpected synthetic outcome");
                            }
                        }
                        return timings;
                    }));
                }
                long[] timings = new long[WORKERS * SAMPLES_PER_WORKER];
                for (int worker = 0; worker < WORKERS; worker++) {
                    System.arraycopy(futures.get(worker).get(), 0, timings,
                            worker * SAMPLES_PER_WORKER, SAMPLES_PER_WORKER);
                }
                Arrays.sort(timings);
                System.out.printf(Locale.ROOT, "%s samples=%d hitRatio=0.5 p50Ms=%.6f p95Ms=%.6f p99Ms=%.6f%n",
                        phase, timings.length, percentile(timings, 0.50), percentile(timings, 0.95),
                        percentile(timings, 0.99));
            }
        }
    }

    private static String generated(int i) {
        return "synthetic-password-" + (1_000_000 + i);
    }

    private static double percentile(long[] sorted, double p) {
        return sorted[(int) Math.ceil(sorted.length * p) - 1] / 1_000_000.0;
    }
}
