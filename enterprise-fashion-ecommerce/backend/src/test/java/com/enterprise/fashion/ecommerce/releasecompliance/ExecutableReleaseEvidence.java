package com.enterprise.fashion.ecommerce.releasecompliance;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

/** Local engineering evidence. Contains identities only, never resource contents. */
final class ExecutableReleaseEvidence {
    private ExecutableReleaseEvidence() {
    }

    enum ResourceStatus { EXPECTED, OBSERVED, UNEXPECTED, PROHIBITED, DISPOSITION_REQUIRED }

    record Resource(ExecutableArchiveInventory.Entry entry, ResourceStatus status) {
    }

    record Resources(String subject, String inventoryDigest, List<Resource> entries, List<String> missing) {
        Resources {
            entries = List.copyOf(entries);
            missing = List.copyOf(missing);
        }

        boolean ready() {
            return missing.isEmpty() && entries.stream().noneMatch(resource -> switch (resource.status()) {
                case PROHIBITED, UNEXPECTED, DISPOSITION_REQUIRED -> true;
                default -> false;
            });
        }
    }

    static Resources inspect(ExecutableArchiveInventory.Inventory inventory) {
        var required = new TreeSet<>(List.of("BOOT-INF/classes/application.properties",
                "BOOT-INF/classes/db/migration/README.md", "BOOT-INF/classpath.idx", "BOOT-INF/layers.idx"));
        for (int version = 1; version <= 17; version++) {
            required.add("migration:" + version);
        }
        var entries = inventory.entries().stream()
                .filter(entry -> entry.category() != ExecutableArchiveInventory.Category.PACKAGED_LIBRARY)
                .map(entry -> new Resource(entry, classify(entry, required)))
                .toList();
        return new Resources(inventory.archiveSha256(), sha256(inventory.canonicalOutput()), entries,
                List.copyOf(required));
    }

    private static ResourceStatus classify(ExecutableArchiveInventory.Entry entry, TreeSet<String> required) {
        if (entry.kind() == ExecutableArchiveInventory.Kind.DIRECTORY) {
            return ResourceStatus.OBSERVED;
        }
        String path = entry.path();
        String lower = path.toLowerCase(java.util.Locale.ROOT);
        if (lower.matches(".*(?:/\\.env(?:\\.[^/]+)?|\\.(?:pem|key|p12|pfx|jks))$")) {
            return ResourceStatus.PROHIBITED;
        }
        if (required.remove(path)) {
            return ResourceStatus.EXPECTED;
        }
        if (path.matches("BOOT-INF/classes/application[^/]*\\.(properties|ya?ml)")) {
            return ResourceStatus.DISPOSITION_REQUIRED;
        }
        if (entry.category() == ExecutableArchiveInventory.Category.FLYWAY_MIGRATION) {
            String version = path.substring(path.lastIndexOf('/') + 2, path.indexOf("__"));
            return required.remove("migration:" + version) ? ResourceStatus.EXPECTED : ResourceStatus.UNEXPECTED;
        }
        return switch (entry.category()) {
            case MANIFEST, SPRING_BOOT_LOADER, ARCHIVE_METADATA -> ResourceStatus.EXPECTED;
            case APPLICATION_CONTENT -> path.endsWith(".class") ? ResourceStatus.EXPECTED : ResourceStatus.OBSERVED;
            default -> ResourceStatus.UNEXPECTED;
        };
    }

    record BuildInputs(String revision, boolean dirty, String configuration, String locks,
            String verification, String toolchain) {
        BuildInputs {
            if (revision == null || !revision.matches("[0-9a-f]{40}|[0-9a-f]{64}")) {
                throw new IllegalArgumentException("Invalid source revision");
            }
            for (String value : List.of(configuration, locks, verification, toolchain)) {
                requireDigest(value);
            }
        }
    }

    record Manifest(String payload, String sha256, boolean complete) {
    }

    static Manifest manifest(BuildInputs inputs, ExecutableArchiveInventory.Inventory inventory,
            Optional<ExecutablePackagedJarReconciliation.Reconciliation> reconciliation) {
        var resources = inspect(inventory);
        reconciliation.ifPresent(result -> {
            if (!resources.subject().equals(result.executableSha256())
                    || !resources.inventoryDigest().equals(result.inventorySha256())
                    || !inputs.locks().equals(result.lockfileSha256())
                    || !inputs.verification().equals(result.verificationMetadataSha256())
                    || !sha256(result.canonicalOutput()).equals(result.reportSha256())) {
                throw new IllegalArgumentException("Reconciliation evidence identity differs");
            }
        });
        boolean complete = !inputs.dirty() && resources.ready() && reconciliation.isPresent();
        String payload = String.join("\n", "bei04-manifest-v1", inputs.revision(), Boolean.toString(inputs.dirty()),
                inputs.configuration(), inputs.locks(), inputs.verification(), inputs.toolchain(),
                Base64.getUrlEncoder().withoutPadding().encodeToString(
                        inventory.archiveFileName().getBytes(StandardCharsets.UTF_8)),
                inventory.archiveSha256(), Long.toString(inventory.archiveByteSize()), resources.inventoryDigest(),
                Boolean.toString(resources.ready()), reconciliation.map(
                        ExecutablePackagedJarReconciliation.Reconciliation::reportSha256).orElse("ABSENT"),
                complete ? "COMPLETE" : "INCOMPLETE") + "\n";
        return new Manifest(payload, sha256(payload.getBytes(StandardCharsets.UTF_8)), complete);
    }

    static boolean equivalent(Manifest left, Manifest right) {
        return left.complete() && right.complete() && left.equals(right);
    }

    enum NoticeKind { DECLARATION, LICENSE, NOTICE, ATTRIBUTION, NESTED_ATTRIBUTION }

    record NoticeInput(String packagedPath, String componentDigest, NoticeKind kind,
            String evidenceIdentity, String evidenceDigest) {
        NoticeInput {
            requireDigest(componentDigest);
            requireDigest(evidenceDigest);
            java.util.Objects.requireNonNull(kind);
            if (evidenceIdentity == null || evidenceIdentity.isBlank()
                    || evidenceIdentity.startsWith("/") || evidenceIdentity.contains("\\")
                    || evidenceIdentity.matches(".*[\\p{Cntrl}].*")) {
                throw new IllegalArgumentException("Invalid notice evidence identity");
            }
        }
    }

    record NoticeCoverage(String executableSha256, List<NoticeInput> inputs, List<String> missing) {
        NoticeCoverage {
            inputs = List.copyOf(inputs);
            missing = List.copyOf(missing);
        }
    }

    static NoticeCoverage noticeCoverage(ExecutableArchiveInventory.Inventory inventory, List<NoticeInput> inputs,
            java.util.Set<String> noticeRequired) {
        var physical = new java.util.TreeMap<String, String>();
        inventory.entries().stream()
                .filter(entry -> entry.category() == ExecutableArchiveInventory.Category.PACKAGED_LIBRARY)
                .forEach(entry -> {
                    if (physical.putIfAbsent(entry.path(), entry.sha256()) != null) {
                        throw new IllegalArgumentException("Duplicate physical path");
                    }
                });
        if (!physical.keySet().containsAll(noticeRequired)) {
            throw new IllegalArgumentException("Unknown required notice component");
        }
        var seen = new java.util.HashSet<List<Object>>();
        var ordered = inputs.stream().sorted(java.util.Comparator.comparing(NoticeInput::packagedPath)
                .thenComparing(NoticeInput::kind).thenComparing(NoticeInput::evidenceIdentity)).toList();
        for (var input : ordered) {
            if (!input.componentDigest().equals(physical.get(input.packagedPath()))
                    || !seen.add(List.of(input.packagedPath(), input.kind(), input.evidenceIdentity()))) {
                throw new IllegalArgumentException("Unknown, stale, duplicate or conflicting notice input");
            }
        }
        var missing = new java.util.ArrayList<String>();
        for (String path : physical.keySet()) {
            if (ordered.stream().noneMatch(input -> path.equals(input.packagedPath())
                    && (input.kind() == NoticeKind.LICENSE || input.kind() == NoticeKind.DECLARATION))) {
                missing.add(path + " missing license evidence");
            }
            if (noticeRequired.contains(path) && ordered.stream().noneMatch(input ->
                    path.equals(input.packagedPath()) && input.kind() == NoticeKind.NOTICE)) {
                missing.add(path + " missing required NOTICE evidence");
            }
        }
        return new NoticeCoverage(inventory.archiveSha256(), ordered, missing);
    }

    private static void requireDigest(String value) {
        if (!value.matches("[0-9a-f]{64}")) {
            throw new IllegalArgumentException("Invalid evidence digest");
        }
    }

    static String sha256(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException(impossible);
        }
    }

    public static void main(String[] args) {
        var inventory = ExecutableArchiveInventory.inspect(Path.of(args[0]), Optional.empty(),
                new ExecutableArchiveInventory.Limits(50_000_000, 1_000, 20_000_000,
                        150_000_000, 1_024, 1_000_000));
        var resources = inspect(inventory);
        System.out.println("Executable SHA-256 " + resources.subject());
        System.out.println("BEI-01 inventory SHA-256 " + resources.inventoryDigest());
        for (var status : ResourceStatus.values()) {
            System.out.println(status + " " + resources.entries().stream().filter(row -> row.status() == status).count());
        }
        System.out.println("Missing expected resources " + resources.missing());
        resources.entries().stream().filter(row -> switch (row.status()) {
            case DISPOSITION_REQUIRED, UNEXPECTED, PROHIBITED -> true;
            default -> false;
        }).forEach(row -> System.out.println(row.status() + " " + row.entry().path()));
        if (!resources.ready()) {
            throw new IllegalStateException("Executable resource evidence is incomplete");
        }
    }
}
