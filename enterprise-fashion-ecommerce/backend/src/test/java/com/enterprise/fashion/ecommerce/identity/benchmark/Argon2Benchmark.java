package com.enterprise.fashion.ecommerce.identity.benchmark;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/** Isolated local benchmark evidence for Proposed DEC-0006. */
public final class Argon2Benchmark {

    private static final String EVIDENCE_LABEL =
            "LOCAL DEVELOPMENT BENCHMARK EVIDENCE — NOT PRODUCTION CAPACITY OR SLO";
    private static final String SYNTHETIC_PASSWORD =
            "synthetic-dec-0006-benchmark-password-only";
    private static final int WARM_UP_PAIRS = 10;
    private static final int SEQUENTIAL_ENCODE_SAMPLES = 30;
    private static final int SEQUENTIAL_MATCH_SAMPLES = 30;
    private static final int CONCURRENT_OPERATIONS = 30;
    private static final int[] CONCURRENCY_LEVELS = {1, 2, 4};
    private static final int MEBIBYTE = 1024 * 1024;
    private static final AtomicLong BLACKHOLE = new AtomicLong();

    private static final Candidate CANDIDATE_A = new Candidate(
            "Candidate A — OWASP baseline", 19_456, 2, 1, 16, 32);
    private static final Candidate CANDIDATE_B = new Candidate(
            "Candidate B — RFC 9106 constrained baseline", 65_536, 3, 4, 16, 32);

    private Argon2Benchmark() {
    }

    public static void main(String[] args) throws Exception {
        requireJava21();
        printEnvironment();
        printMethod();
        printMemoryEvidence(CANDIDATE_A);
        printMemoryEvidence(CANDIDATE_B);

        runSuite("A then B", List.of(CANDIDATE_A, CANDIDATE_B));
        runSuite("B then A", List.of(CANDIDATE_B, CANDIDATE_A));

        System.out.printf(Locale.ROOT, "%nfinal volatile checksum/blackhole=%d%n", BLACKHOLE.get());
        System.out.println("All generated argon2id verifiers used v=19 and passed PHC and matches assertions.");
    }

    private static void requireJava21() {
        int feature = Runtime.version().feature();
        if (feature != 21) {
            throw new IllegalStateException("Benchmark requires Java 21 but ran on Java " + feature);
        }
    }

    private static void printEnvironment() {
        Runtime runtime = Runtime.getRuntime();
        MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

        System.out.println(EVIDENCE_LABEL);
        System.out.println("timestamp=" + ZonedDateTime.now());
        System.out.println("timezone=" + ZonedDateTime.now().getZone());
        System.out.println("os.name=" + System.getProperty("os.name"));
        System.out.println("os.version=" + System.getProperty("os.version"));
        System.out.println("os.arch=" + System.getProperty("os.arch"));
        System.out.println("Java vendor=" + System.getProperty("java.vendor"));
        System.out.println("Java runtime version=" + System.getProperty("java.runtime.version"));
        System.out.println("Java VM name=" + System.getProperty("java.vm.name"));
        System.out.println("java.home=" + System.getProperty("java.home"));
        System.out.println("JVM input arguments="
                + ManagementFactory.getRuntimeMXBean().getInputArguments());
        System.out.println("availableProcessors=" + runtime.availableProcessors());
        System.out.println("initialHeapBytes=" + heap.getInit());
        System.out.println("maximumHeapBytes=" + heap.getMax());
        System.out.println("committedHeapBytes=" + heap.getCommitted());
        System.out.println("usedHeapBytes=" + heap.getUsed());
        System.out.println("availableHeapBytes=" + availableHeap(heap));
        printPhysicalMemory();
        System.out.println("Spring Security implementation version="
                + packageVersion(Argon2PasswordEncoder.class));
        System.out.println("Bouncy Castle implementation version="
                + packageVersion(Argon2BytesGenerator.class));
        System.out.println("benchmark password input=synthetic benchmark-only value; plaintext not printed");
    }

    private static void printMethod() {
        System.out.println("warm-up encode/matches pairs per Candidate per suite=" + WARM_UP_PAIRS);
        System.out.println("sequential encode samples per Candidate per suite=" + SEQUENTIAL_ENCODE_SAMPLES);
        System.out.println("sequential matches samples per Candidate per suite=" + SEQUENTIAL_MATCH_SAMPLES);
        System.out.println("controlled concurrency levels=" + Arrays.toString(CONCURRENCY_LEVELS));
        System.out.println("concurrent total operations per phase/Candidate/concurrency="
                + CONCURRENT_OPERATIONS);
        System.out.println("p95 method=nearest-rank ceil(0.95*n), converted to zero-based index");
        System.out.println("Immediately free physical memory is diagnostic only and is not treated"
                + " as total available/reclaimable memory.");
    }

    private static void printPhysicalMemory() {
        PhysicalMemory memory = physicalMemory();
        if (memory.totalAvailable()) {
            System.out.println("physicalMemoryTotalBytes=" + memory.totalBytes());
        }
        else {
            System.out.println("physicalMemoryTotalBytes=unavailable through supported management APIs");
        }
        if (memory.freeAvailable()) {
            System.out.println("physicalMemoryFreeBytes=" + memory.freeBytes());
        }
        else {
            System.out.println("physicalMemoryFreeBytes=unavailable through supported management APIs");
        }
    }

    private static void printMemoryEvidence(Candidate candidate) {
        System.out.println();
        System.out.println(candidate.name() + " parameter-derived working memory (excludes JVM/library/runtime overhead)");
        for (int concurrency : CONCURRENCY_LEVELS) {
            long totalMiB = (long) candidate.memoryKiB() * concurrency / 1024;
            System.out.printf(Locale.ROOT, "Candidate=%s concurrency=%d workingMemoryMiB=%d%n",
                    candidate.name(), concurrency, totalMiB);
        }
    }

    private static void runSuite(String order, List<Candidate> candidates) throws Exception {
        System.out.printf(Locale.ROOT, "%n================ Suite order: %s ================%n", order);
        for (Candidate candidate : candidates) {
            runCandidate(order, candidate);
        }
    }

    private static void runCandidate(String order, Candidate candidate) throws Exception {
        Argon2PasswordEncoder encoder = candidate.encoder();
        System.out.printf(Locale.ROOT,
                "%nCandidate=%s suite=%s argon2id v=19 memoryKiB=%d iterations=%d parallelism=%d saltBytes=%d hashBytes=%d%n",
                candidate.name(), order, candidate.memoryKiB(), candidate.iterations(),
                candidate.parallelism(), candidate.saltLength(), candidate.hashLength());

        warmUp(candidate, encoder);
        List<String> sequentialHashes = sequentialEncode(candidate, encoder);
        sequentialMatches(candidate, encoder, sequentialHashes);

        for (int concurrency : CONCURRENCY_LEVELS) {
            ensureMemoryHeadroom(candidate, concurrency);
            List<String> concurrentHashes = concurrentEncode(candidate, encoder, concurrency);
            concurrentMatches(candidate, encoder, concurrency, concurrentHashes);
        }
    }

    private static void warmUp(Candidate candidate, Argon2PasswordEncoder encoder) {
        for (int i = 0; i < WARM_UP_PAIRS; i++) {
            String encoded = encoder.encode(SYNTHETIC_PASSWORD);
            validateRepresentation(candidate, encoded);
            requireMatch(encoder, encoded);
            consume(encoded);
        }
        System.out.printf(Locale.ROOT, "Candidate=%s warm-up pairs=%d complete; samples excluded%n",
                candidate.name(), WARM_UP_PAIRS);
    }

    private static List<String> sequentialEncode(Candidate candidate, Argon2PasswordEncoder encoder) {
        long[] samples = new long[SEQUENTIAL_ENCODE_SAMPLES];
        List<String> hashes = new ArrayList<>(SEQUENTIAL_ENCODE_SAMPLES);
        long wallStart = System.nanoTime();
        for (int i = 0; i < SEQUENTIAL_ENCODE_SAMPLES; i++) {
            long start = System.nanoTime();
            String encoded = encoder.encode(SYNTHETIC_PASSWORD);
            samples[i] = System.nanoTime() - start;
            validateRepresentation(candidate, encoded);
            requireMatch(encoder, encoded);
            hashes.add(encoded);
            consume(encoded);
        }
        long wallNanos = System.nanoTime() - wallStart;
        report(candidate, "sequential encode", 1, samples, wallNanos);
        return hashes;
    }

    private static void sequentialMatches(
            Candidate candidate, Argon2PasswordEncoder encoder, List<String> hashes) {
        if (hashes.size() != SEQUENTIAL_MATCH_SAMPLES) {
            throw new IllegalStateException("Expected exactly " + SEQUENTIAL_MATCH_SAMPLES + " retained hashes");
        }
        long[] samples = new long[SEQUENTIAL_MATCH_SAMPLES];
        long wallStart = System.nanoTime();
        for (int i = 0; i < SEQUENTIAL_MATCH_SAMPLES; i++) {
            long start = System.nanoTime();
            boolean matches = encoder.matches(SYNTHETIC_PASSWORD, hashes.get(i));
            samples[i] = System.nanoTime() - start;
            if (!matches) {
                throw new IllegalStateException("Sequential matches returned false");
            }
            consume(matches);
        }
        long wallNanos = System.nanoTime() - wallStart;
        report(candidate, "sequential matches", 1, samples, wallNanos);
    }

    private static List<String> concurrentEncode(
            Candidate candidate, Argon2PasswordEncoder encoder, int concurrency) throws Exception {
        String[] hashes = new String[CONCURRENT_OPERATIONS];
        long[] samples = new long[CONCURRENT_OPERATIONS];
        long wallNanos = executeConcurrent(concurrency, index -> {
            long start = System.nanoTime();
            String encoded = encoder.encode(SYNTHETIC_PASSWORD);
            samples[index] = System.nanoTime() - start;
            validateRepresentation(candidate, encoded);
            requireMatch(encoder, encoded);
            hashes[index] = encoded;
            consume(encoded);
        });
        report(candidate, "concurrent encode", concurrency, samples, wallNanos);
        return List.of(hashes);
    }

    private static void concurrentMatches(
            Candidate candidate,
            Argon2PasswordEncoder encoder,
            int concurrency,
            List<String> hashes) throws Exception {
        if (hashes.size() != CONCURRENT_OPERATIONS) {
            throw new IllegalStateException("Expected exactly " + CONCURRENT_OPERATIONS + " concurrent hashes");
        }
        long[] samples = new long[CONCURRENT_OPERATIONS];
        long wallNanos = executeConcurrent(concurrency, index -> {
            long start = System.nanoTime();
            boolean matches = encoder.matches(SYNTHETIC_PASSWORD, hashes.get(index));
            samples[index] = System.nanoTime() - start;
            if (!matches) {
                throw new IllegalStateException("Concurrent matches returned false");
            }
            consume(matches);
        });
        report(candidate, "concurrent matches", concurrency, samples, wallNanos);
    }

    private static long executeConcurrent(int concurrency, IndexedOperation operation) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>(CONCURRENT_OPERATIONS);
        try {
            for (int index = 0; index < CONCURRENT_OPERATIONS; index++) {
                int operationIndex = index;
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    operation.run(operationIndex);
                    return null;
                }));
            }
            if (!ready.await(30, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Concurrent workers did not become ready");
            }
            long wallStart = System.nanoTime();
            start.countDown();
            for (Future<?> future : futures) {
                future.get();
            }
            return System.nanoTime() - wallStart;
        }
        finally {
            start.countDown();
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Benchmark executor did not terminate");
                }
            }
        }
    }

    private static void report(
            Candidate candidate,
            String phase,
            int concurrency,
            long[] samples,
            long wallNanos) {
        long[] sorted = samples.clone();
        Arrays.sort(sorted);
        long min = sorted[0];
        double median = median(sorted);
        int p95Index = Math.max(0, (int) Math.ceil(0.95d * sorted.length) - 1);
        long p95 = sorted[p95Index];
        long max = sorted[sorted.length - 1];
        double mean = Arrays.stream(samples).average().orElseThrow();
        double throughput = samples.length / nanosToSeconds(wallNanos);

        System.out.printf(Locale.ROOT,
                "Candidate=%s phase=%s concurrency=%d samples=%d min=%.3fms median=%.3fms p95=%.3fms max=%.3fms mean=%.3fms wall=%.3fms throughput=%.3f ops/s%n",
                candidate.name(), phase, concurrency, samples.length, nanosToMillis(min),
                nanosToMillis(median), nanosToMillis(p95), nanosToMillis(max),
                nanosToMillis(mean), nanosToMillis(wallNanos), throughput);
    }

    private static double median(long[] sorted) {
        int middle = sorted.length / 2;
        if ((sorted.length & 1) == 1) {
            return sorted[middle];
        }
        return (sorted[middle - 1] / 2.0d) + (sorted[middle] / 2.0d);
    }

    private static void validateRepresentation(Candidate candidate, String encoded) {
        String[] fields = encoded.split("\\$", -1);
        if (fields.length != 6 || !fields[0].isEmpty() || !"argon2id".equals(fields[1])) {
            throw new IllegalStateException("Malformed or unexpected Argon2 PHC representation");
        }
        if (!"v=19".equals(fields[2])) {
            throw new IllegalStateException("Expected Argon2 v=19 but found " + fields[2]);
        }

        Map<String, Integer> parameters = parseParameters(fields[3]);
        requireParameter(parameters, "m", candidate.memoryKiB());
        requireParameter(parameters, "t", candidate.iterations());
        requireParameter(parameters, "p", candidate.parallelism());

        byte[] salt = decodeUnpaddedBase64(fields[4]);
        byte[] hash = decodeUnpaddedBase64(fields[5]);
        if (salt.length != candidate.saltLength()) {
            throw new IllegalStateException("Expected salt length " + candidate.saltLength()
                    + " but found " + salt.length);
        }
        if (hash.length != candidate.hashLength()) {
            throw new IllegalStateException("Expected hash length " + candidate.hashLength()
                    + " but found " + hash.length);
        }
    }

    private static Map<String, Integer> parseParameters(String field) {
        Map<String, Integer> parameters = new LinkedHashMap<>();
        for (String entry : field.split(",")) {
            String[] pair = entry.split("=", -1);
            if (pair.length != 2 || parameters.put(pair[0], Integer.parseInt(pair[1])) != null) {
                throw new IllegalStateException("Malformed or duplicate Argon2 parameter: " + entry);
            }
        }
        if (!parameters.keySet().equals(java.util.Set.of("m", "t", "p"))) {
            throw new IllegalStateException("Unexpected Argon2 parameters: " + parameters.keySet());
        }
        return parameters;
    }

    private static void requireParameter(Map<String, Integer> parameters, String name, int expected) {
        if (!Integer.valueOf(expected).equals(parameters.get(name))) {
            throw new IllegalStateException("Expected " + name + "=" + expected
                    + " but found " + parameters.get(name));
        }
    }

    private static byte[] decodeUnpaddedBase64(String value) {
        int paddingLength = (4 - value.length() % 4) % 4;
        return Base64.getDecoder().decode(value + "=".repeat(paddingLength));
    }

    private static void requireMatch(Argon2PasswordEncoder encoder, String encoded) {
        boolean matches = encoder.matches(SYNTHETIC_PASSWORD, encoded);
        if (!matches) {
            throw new IllegalStateException("Generated verifier did not match synthetic password");
        }
        consume(matches);
    }

    private static void ensureMemoryHeadroom(Candidate candidate, int concurrency) {
        PhysicalMemory physicalMemory = physicalMemory();
        long parameterDerivedAggregateBytes = Math.multiplyExact(
                Math.multiplyExact((long) candidate.memoryKiB(), 1024L), concurrency);
        long conservativeRequired = Math.addExact(
                Math.multiplyExact(parameterDerivedAggregateBytes, 2L), 512L * MEBIBYTE);
        long maximumHeapBytes = Runtime.getRuntime().maxMemory();

        boolean totalPhysicalMemoryAvailable = physicalMemory.totalAvailable();
        long physicalMemoryLimit = totalPhysicalMemoryAvailable
                ? physicalMemory.totalBytes() / 4L
                : -1L;
        boolean totalPhysicalMemoryPass = !totalPhysicalMemoryAvailable
                || conservativeRequired <= physicalMemoryLimit;
        boolean maximumHeapPass = conservativeRequired <= maximumHeapBytes;
        boolean preflightPass = totalPhysicalMemoryPass && maximumHeapPass;

        System.out.printf(Locale.ROOT,
                "Candidate=%s concurrency=%d parameterDerivedAggregateBytes=%d "
                        + "conservativeRequiredBytes=%d physicalMemoryTotalBytes=%s "
                        + "physicalMemoryFreeBytes=%s physicalMemory25PercentLimitBytes=%s "
                        + "maximumHeapBytes=%d totalPhysicalMemoryCheck=%s maximumHeapCheck=%s "
                        + "immediatelyFreeMemoryDiagnosticBytes=%s preflight=%s%n",
                candidate.name(), concurrency, parameterDerivedAggregateBytes, conservativeRequired,
                availableValue(physicalMemory.totalBytes(), physicalMemory.totalAvailable()),
                availableValue(physicalMemory.freeBytes(), physicalMemory.freeAvailable()),
                availableValue(physicalMemoryLimit, totalPhysicalMemoryAvailable), maximumHeapBytes,
                totalPhysicalMemoryAvailable
                        ? (totalPhysicalMemoryPass ? "PASS" : "FAIL")
                        : "UNAVAILABLE",
                maximumHeapPass ? "PASS" : "FAIL",
                availableValue(physicalMemory.freeBytes(), physicalMemory.freeAvailable()),
                preflightPass ? "PASS" : "FAIL");
        if (!preflightPass) {
            throw new IllegalStateException(
                    "Conservative benchmark memory preflight failed for " + candidate.name()
                            + " at concurrency " + concurrency);
        }
    }

    private static String availableValue(long value, boolean available) {
        return available ? Long.toString(value) : "UNAVAILABLE";
    }

    private static PhysicalMemory physicalMemory() {
        java.lang.management.OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (bean instanceof com.sun.management.OperatingSystemMXBean operatingSystem) {
            long total = operatingSystem.getTotalMemorySize();
            long free = operatingSystem.getFreeMemorySize();
            if (total > 0 || free >= 0) {
                return new PhysicalMemory(total, free);
            }
        }
        return PhysicalMemory.UNAVAILABLE;
    }

    private static String packageVersion(Class<?> type) {
        String version = type.getPackage().getImplementationVersion();
        return version == null ? "unavailable" : version;
    }

    private static long availableHeap(MemoryUsage heap) {
        return heap.getMax() < 0 ? -1 : Math.max(0L, heap.getMax() - heap.getUsed());
    }

    private static void consume(String value) {
        BLACKHOLE.accumulateAndGet(value.hashCode(), (current, next) -> Long.rotateLeft(current, 7) ^ next);
    }

    private static void consume(boolean value) {
        BLACKHOLE.accumulateAndGet(value ? 1L : 0L, (current, next) -> Long.rotateLeft(current, 3) ^ next);
    }

    private static double nanosToMillis(double nanos) {
        return nanos / 1_000_000.0d;
    }

    private static double nanosToSeconds(long nanos) {
        return nanos / 1_000_000_000.0d;
    }

    @FunctionalInterface
    private interface IndexedOperation {
        void run(int index) throws Exception;
    }

    private record Candidate(
            String name,
            int memoryKiB,
            int iterations,
            int parallelism,
            int saltLength,
            int hashLength) {

        private Argon2PasswordEncoder encoder() {
            return new Argon2PasswordEncoder(
                    saltLength, hashLength, parallelism, memoryKiB, iterations);
        }
    }

    private record PhysicalMemory(long totalBytes, long freeBytes) {
        private static final PhysicalMemory UNAVAILABLE = new PhysicalMemory(-1L, -1L);

        private boolean totalAvailable() {
            return totalBytes > 0;
        }

        private boolean freeAvailable() {
            return freeBytes >= 0;
        }
    }
}
