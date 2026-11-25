package com.enterprise.fashion.ecommerce.releasecompliance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.*;

/** Entry point for the explicitly invoked, offline Gradle verification task. */
final class Bei02Verification {
    private Bei02Verification() {
    }

    public static void main(String[] args) throws Exception {
        var graph = new java.util.ArrayList<ResolvedComponent>();
        for (int index = 4; index < args.length; index += 4) {
            var coordinate = coordinate(args[index]);
            var kind = switch (args[index + 1]) {
                case "library" -> ComponentKind.JAR;
                case "platform", "enforced-platform" -> ComponentKind.PLATFORM;
                default -> throw new IllegalArgumentException("Unsupported selected variant for " + coordinate.value());
            };
            if (kind == ComponentKind.PLATFORM && !"-".equals(args[index + 2])) {
                throw new IllegalArgumentException("Platform supplied a physical artifact");
            }
            graph.add(new ResolvedComponent(coordinate, kind,
                    kind == ComponentKind.JAR ? args[index + 2] : null,
                    kind == ComponentKind.JAR ? args[index + 3] : null));
        }
        byte[] lock = bounded(args[2], 1_000_000);
        if (!digest(lock).equals("34a91e1cb6e77c2cadff10b02d874ca18febb7b9fecfaa8629bfc270a8c8b5c8")) {
            throw new IllegalStateException("Lockfile differs from the versioned BEI-02 expected baseline");
        }
        var omitted = Arrays.stream(("data-jdbc jdbc json logging oauth2-client security tomcat validation web base")
                .split(" "))
                .map(suffix -> coordinate("org.springframework.boot:spring-boot-starter"
                        + (suffix.equals("base") ? "" : "-" + suffix) + ":3.5.16"))
                .collect(Collectors.toSet());
        var platforms = Set.of(coordinate("org.springframework.boot:spring-boot-dependencies:3.5.16"),
                coordinate("com.fasterxml.jackson:jackson-bom:2.21.4"));
        var packaged = Arrays.stream(new String(lock, java.nio.charset.StandardCharsets.UTF_8).split("\\R"))
                .filter(line -> !line.startsWith("#"))
                .filter(line -> line.matches(".*=(?:[^,]+,)*productionRuntimeClasspath(?:,.*)?"))
                .map(line -> coordinate(line.substring(0, line.indexOf('='))))
                .filter(value -> !omitted.contains(value) && !platforms.contains(value))
                .collect(Collectors.toSet());
        var policy = new ClassificationPolicy("backend-executable-baseline-v1", packaged, omitted, platforms,
                List.of(new BuildAddedRule("BOOT-INF/lib/spring-boot-jarmode-tools-3.5.16.jar",
                        coordinate("org.springframework.boot:spring-boot-loader-tools:3.5.16"),
                        "spring-boot-loader-tools-3.5.16.jar",
                        "8096fc208bea29f8986c3411b65b36edd264292a8b0b3982157b945d79e9113a",
                        "META-INF/jarmode/spring-boot-jarmode-tools.jar",
                        "d05beb46a7eac0f1a06733c75828b190a1cb250076ce03be2213aca4a5c0f03f")),
                new Cardinalities(73, 61, 10, 2, 1, 62));
        var inventory = ExecutableArchiveInventory.inspect(Path.of(args[0]), Optional.empty(),
                new ExecutableArchiveInventory.Limits(50_000_000, 1_000, 20_000_000,
                        150_000_000, 1_024, 1_000_000));
        System.out.println("Executable SHA-256 " + inventory.archiveSha256());
        System.out.println("BEI-01 inventory SHA-256 " + digest(inventory.canonicalOutput()));
        var result = reconcile(inventory, lock, bounded(args[3], 2_000_000),
                new ResolvedGraph("gradle-production-runtime-v1", graph), policy, bounded(args[1], 10_000_000),
                new Limits(1_000_000, 10_000, 2_000_000, 5_000, 10_000_000, 5_000, 10_000_000, 50_000_000));
        System.out.println("BEI-02 report SHA-256 " + result.reportSha256());
    }

    private static Coordinate coordinate(String value) {
        return Coordinate.parse(value, FailureCategory.InconsistentClassification);
    }

    private static String digest(byte[] bytes) throws java.security.NoSuchAlgorithmException {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }

    private static byte[] bounded(String path, int maximum) throws java.io.IOException {
        try (var input = Files.newInputStream(Path.of(path))) {
            byte[] bytes = input.readNBytes(maximum + 1);
            if (bytes.length > maximum) {
                throw new IllegalArgumentException("Evidence exceeds its byte limit");
            }
            return bytes;
        }
    }
}
