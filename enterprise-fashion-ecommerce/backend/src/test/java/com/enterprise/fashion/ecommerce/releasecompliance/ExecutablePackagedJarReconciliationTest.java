package com.enterprise.fashion.ecommerce.releasecompliance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.Classification.BUILD_ADDED;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.Classification.DEPENDENCY_MANAGEMENT_METADATA;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.Classification.PACKAGED;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.Classification.RESOLVED_NOT_PACKAGED;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.ComponentKind.JAR;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.ComponentKind.PLATFORM;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.AmbiguousComponentMatch;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.DigestMismatch;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.DuplicateCoordinate;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.DuplicatePackagedComponent;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.InconsistentClassification;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.InvalidBuildAddedOrigin;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.LimitExceeded;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.MalformedLockfile;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.MalformedVerificationMetadata;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.MissingChecksumEvidence;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.MissingExpectedPackagedComponent;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.UnmatchedPackagedComponent;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutablePackagedJarReconciliation.FailureCategory.UnknownBuildAddedComponent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutablePackagedJarReconciliationTest {

    private static final ExecutablePackagedJarReconciliation.Coordinate PACKAGED_COORD = coordinate("test:alpha:1");
    private static final ExecutablePackagedJarReconciliation.Coordinate OMITTED = coordinate("test:starter:1");
    private static final ExecutablePackagedJarReconciliation.Coordinate PLATFORM_COORD = coordinate("test:bom:1");
    private static final ExecutablePackagedJarReconciliation.Coordinate LOADER =
            coordinate("org.springframework.boot:spring-boot-loader-tools:3.5.16");
    private static final byte[] PACKAGED_BYTES = bytes("locked-alpha-jar");
    private static final byte[] OMITTED_BYTES = bytes("omitted-starter-jar");
    private static final byte[] JARMODE_BYTES = bytes("nested-jarmode-jar");
    private static final String PACKAGED_PATH = "BOOT-INF/lib/alpha-1.jar";
    private static final String JARMODE_PATH = "BOOT-INF/lib/spring-boot-jarmode-tools-3.5.16.jar";
    private static final String NESTED_PATH = "META-INF/jarmode/spring-boot-jarmode-tools.jar";
    private static final ExecutablePackagedJarReconciliation.Limits LIMITS =
            new ExecutablePackagedJarReconciliation.Limits(100_000, 100, 500_000, 100,
                    1_000_000, 100, 100_000, 500_000);
    private static final ExecutableArchiveInventory.Limits INVENTORY_LIMITS =
            new ExecutableArchiveInventory.Limits(1_000_000, 100, 100_000, 500_000, 512, 65_536);

    @TempDir
    Path temporaryDirectory;

    @Test
    void reconcilesCompleteEvidenceAndPreservesEveryClassification() throws Exception {
        Evidence evidence = baseline();

        var result = reconcile(evidence);

        assertThat(result.schemaVersion()).isEqualTo("bei02-packaged-jar-reconciliation-v1");
        assertThat(result.resolvedCoordinateCount()).isEqualTo(3);
        assertThat(result.physicalPackagedLibraryCount()).isEqualTo(2);
        assertThat(result.classificationCounts()).containsEntry(PACKAGED, 1)
                .containsEntry(RESOLVED_NOT_PACKAGED, 1)
                .containsEntry(DEPENDENCY_MANAGEMENT_METADATA, 1)
                .containsEntry(BUILD_ADDED, 1);
        assertThat(result.componentResults()).extracting(ExecutablePackagedJarReconciliation.ComponentResult::coordinate)
                .containsExactly(PACKAGED_COORD, PLATFORM_COORD, OMITTED);
        assertThat(result.buildAddedResults()).singleElement().satisfies(added -> {
            assertThat(added.packagedPath()).isEqualTo(JARMODE_PATH);
            assertThat(added.sourceCoordinate()).isEqualTo(LOADER);
            assertThat(added.nestedResourcePath()).isEqualTo(NESTED_PATH);
            assertThat(added.nestedToPackagedDigestEqual()).isTrue();
        });
        assertThat(result.reportSha256()).isEqualTo(sha256(result.canonicalOutput()));
    }

    @Test
    void usesDigestIdentityAndRejectsFilenameOnlyMatches() throws Exception {
        Evidence evidence = baseline();
        var changed = evidence.withInventory(inventory(Map.of(
                PACKAGED_PATH, bytes("changed-alpha"), JARMODE_PATH, JARMODE_BYTES)));

        assertFailure(DigestMismatch, () -> reconcile(changed));
    }

    @Test
    void rejectsDuplicatePhysicalMappingAndAmbiguousCoordinateDigest() throws Exception {
        Evidence evidence = baseline();
        var entries = new ArrayList<>(evidence.inventory().entries());
        entries.add(entries.stream().filter(entry -> entry.path().equals(PACKAGED_PATH)).findFirst().orElseThrow());
        var duplicateInventory = new ExecutableArchiveInventory.Inventory(
                evidence.inventory().schemaVersion(), evidence.inventory().archiveFileName(),
                evidence.inventory().archiveByteSize(), evidence.inventory().archiveSha256(),
                entries.size(), evidence.inventory().manifestAttributes(), entries);
        assertFailure(DuplicatePackagedComponent,
                () -> reconcile(evidence.withInventory(duplicateInventory)));

        var second = jar("test:beta:1", "beta-1.jar", PACKAGED_BYTES);
        var graph = graph(evidence.graph().components().get(0), evidence.graph().components().get(1),
                evidence.graph().components().get(2), second);
        var policy = policy(Set.of(PACKAGED_COORD, second.coordinate()), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                evidence.policy().buildAddedRules(), new ExecutablePackagedJarReconciliation.Cardinalities(4, 2, 1, 1, 1, 3));
        var ambiguousInventory = inventory(Map.of(
                PACKAGED_PATH, PACKAGED_BYTES, "BOOT-INF/lib/beta-1.jar", PACKAGED_BYTES,
                JARMODE_PATH, JARMODE_BYTES));
        var ambiguous = evidence.withGraph(graph).withPolicy(policy).withInventory(ambiguousInventory)
                .withLockfile(lockfile(graph.components()))
                .withMetadata(metadata(graph.components(), evidence.sourceBytes(), null));
        assertFailure(AmbiguousComponentMatch, () -> reconcile(ambiguous));
    }

    @Test
    void rejectsMissingAndUnexpectedPackagedComponents() throws Exception {
        Evidence evidence = baseline();
        assertFailure(MissingExpectedPackagedComponent, () -> reconcile(evidence.withInventory(
                inventory(Map.of(JARMODE_PATH, JARMODE_BYTES)))));
        assertFailure(UnmatchedPackagedComponent, () -> reconcile(evidence.withInventory(
                inventory(Map.of(PACKAGED_PATH, PACKAGED_BYTES, JARMODE_PATH, JARMODE_BYTES,
                        "BOOT-INF/lib/unknown.jar", bytes("unknown"))))));
    }

    @Test
    void distinguishesPermittedOmissionAndPlatformFromUnexplainedOrInferredStatus() throws Exception {
        Evidence evidence = baseline();
        var result = reconcile(evidence);
        assertThat(result.componentResults()).filteredOn(r -> r.coordinate().equals(OMITTED))
                .singleElement().extracting(ExecutablePackagedJarReconciliation.ComponentResult::classification)
                .isEqualTo(RESOLVED_NOT_PACKAGED);
        assertThat(result.componentResults()).filteredOn(r -> r.coordinate().equals(PLATFORM_COORD))
                .singleElement().extracting(ExecutablePackagedJarReconciliation.ComponentResult::classification)
                .isEqualTo(DEPENDENCY_MANAGEMENT_METADATA);

        var noOmissionPolicy = policy(Set.of(PACKAGED_COORD), Set.of(), Set.of(PLATFORM_COORD),
                evidence.policy().buildAddedRules(), new ExecutablePackagedJarReconciliation.Cardinalities(2, 1, 0, 1, 1, 2));
        assertFailure(InconsistentClassification, () -> reconcile(evidence.withPolicy(noOmissionPolicy)));

        var falsePolicy = policy(Set.of(PACKAGED_COORD), Set.of(), Set.of(OMITTED, PLATFORM_COORD),
                evidence.policy().buildAddedRules(), new ExecutablePackagedJarReconciliation.Cardinalities(3, 1, 0, 2, 1, 2));
        assertFailure(InconsistentClassification,
                () -> reconcile(evidence.withPolicy(falsePolicy)));
    }

    @Test
    void verifiesBuildAddedSourceContainerNestedResourceAndRuntimeSeparation() throws Exception {
        Evidence evidence = baseline();
        var added = reconcile(evidence).buildAddedResults().getFirst();
        assertThat(added.sourceArtifactSha256()).isEqualTo(sha256(evidence.sourceBytes()));
        assertThat(added.nestedResourceSha256()).isEqualTo(sha256(JARMODE_BYTES));

        byte[] changedSource = sourceArchive(NESTED_PATH, JARMODE_BYTES, "changed-marker");
        assertFailure(DigestMismatch, () -> reconcile(evidence.withSourceBytes(changedSource)));

        byte[] changedNested = sourceArchive(NESTED_PATH, bytes("changed-nested"), null);
        var changedRule = buildRule(changedNested, bytes("changed-nested"));
        assertFailure(DigestMismatch, () -> reconcile(evidence.withSourceBytes(changedNested)
                .withPolicy(policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                        List.of(changedRule), evidence.policy().cardinalities()))
                .withMetadata(metadata(evidence.graph().components(), changedNested, null))));
    }

    @Test
    void rejectsMissingDuplicateInvalidAndUnknownBuildAddedOrigins() throws Exception {
        Evidence evidence = baseline();
        byte[] missing = sourceArchive("different/resource.jar", JARMODE_BYTES, null);
        var missingRule = buildRule(missing, JARMODE_BYTES);
        assertFailure(InvalidBuildAddedOrigin, () -> reconcile(evidence.withSourceBytes(missing)
                .withPolicy(policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                        List.of(missingRule), evidence.policy().cardinalities()))
                .withMetadata(metadata(evidence.graph().components(), missing, null))));

        byte[] duplicate = duplicateNestedSourceArchive();
        var duplicateRule = buildRule(duplicate, JARMODE_BYTES);
        assertFailure(InvalidBuildAddedOrigin, () -> reconcile(evidence.withSourceBytes(duplicate)
                .withPolicy(policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                        List.of(duplicateRule), evidence.policy().cardinalities()))
                .withMetadata(metadata(evidence.graph().components(), duplicate, null))));

        byte[] invalid = bytes("not-a-zip");
        var invalidRule = buildRule(invalid, JARMODE_BYTES);
        assertFailure(InvalidBuildAddedOrigin, () -> reconcile(evidence.withSourceBytes(invalid)
                .withPolicy(policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                        List.of(invalidRule), evidence.policy().cardinalities()))
                .withMetadata(metadata(evidence.graph().components(), invalid, null))));
        assertFailure(UnknownBuildAddedComponent, () -> reconcile(evidence.withInventory(
                inventory(Map.of(PACKAGED_PATH, PACKAGED_BYTES)))));
    }

    @Test
    void requiresExactAndUnambiguousVerificationChecksumEvidence() throws Exception {
        Evidence evidence = baseline();
        String withoutAlpha = metadata(evidence.graph().components(), evidence.sourceBytes(), PACKAGED_COORD);
        assertFailure(MissingChecksumEvidence, () -> reconcile(evidence.withMetadata(withoutAlpha)));

        var wrongDigest = graph(jar("test:alpha:1", "alpha-1.jar", bytes("wrong-resolved")),
                jar("test:starter:1", "starter-1.jar", OMITTED_BYTES), platform("test:bom:1"));
        assertFailure(DigestMismatch, () -> reconcile(evidence.withGraph(wrongDigest)));

        String multiple = metadataWithAdditionalChecksum(evidence.metadata(), PACKAGED_COORD,
                "alpha-1.jar", "f".repeat(64));
        assertThat(reconcile(evidence.withMetadata(multiple)).componentResults()).hasSize(3);
    }

    @Test
    void validatesLockfileSyntaxDuplicatesConflictsAndResolvedGraphEquality() throws Exception {
        Evidence evidence = baseline();
        assertFailure(MalformedLockfile, () -> reconcile(evidence.withLockfile(
                "test:alpha:1 productionRuntimeClasspath\n")));
        assertFailure(DuplicateCoordinate, () -> reconcile(evidence.withLockfile(
                evidence.lockfile() + "test:alpha:1=productionRuntimeClasspath\n")));
        assertFailure(DuplicateCoordinate, () -> reconcile(evidence.withLockfile(
                evidence.lockfile() + "test:alpha:2=productionRuntimeClasspath\n")));
        assertFailure(InconsistentClassification, () -> reconcile(evidence.withLockfile(
                evidence.lockfile().replace("test:starter:1=productionRuntimeClasspath\n", ""))));

        String legitimate = "# comment\nempty=annotationProcessor,developmentOnly\n" + evidence.lockfile();
        assertThat(reconcile(evidence.withLockfile(legitimate)).resolvedCoordinateCount()).isEqualTo(3);
    }

    @Test
    void securelyRejectsMalformedDuplicateConflictingAndExternalXml() throws Exception {
        Evidence evidence = baseline();
        assertFailure(MalformedVerificationMetadata, () -> reconcile(evidence.withMetadata("<broken")));
        String duplicate = evidence.metadata().replace("</components>",
                componentXml(PACKAGED_COORD, "alpha-1.jar", sha256(PACKAGED_BYTES)) + "</components>");
        assertFailure(MalformedVerificationMetadata, () -> reconcile(evidence.withMetadata(duplicate)));
        String external = "<?xml version=\"1.0\"?><!DOCTYPE x [<!ENTITY ext SYSTEM \"file:///etc/passwd\">]>"
                + "<verification-metadata><components>&ext;</components></verification-metadata>";
        assertFailure(MalformedVerificationMetadata, () -> reconcile(evidence.withMetadata(external)));
        String badSha = evidence.metadata().replace(sha256(PACKAGED_BYTES), "not-a-sha");
        assertFailure(MalformedVerificationMetadata, () -> reconcile(evidence.withMetadata(badSha)));
    }

    @Test
    void enforcesCardinalityAndBoundedSourceArchiveReading() throws Exception {
        Evidence evidence = baseline();
        assertThatThrownBy(() -> new ExecutablePackagedJarReconciliation.Cardinalities(3, 2, 1, 1, 1, 2))
                .isInstanceOf(ExecutablePackagedJarReconciliation.Failure.class);
        var wrongCounts = new ExecutablePackagedJarReconciliation.Cardinalities(3, 0, 2, 1, 2, 2);
        var wrongPolicy = policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                evidence.policy().buildAddedRules(), wrongCounts);
        assertFailure(InconsistentClassification, () -> reconcile(evidence.withPolicy(wrongPolicy)));

        var tiny = new ExecutablePackagedJarReconciliation.Limits(100_000, 100, 500_000, 100,
                1_000_000, 1, 4, 4);
        assertFailure(LimitExceeded, () -> reconcile(evidence.withLimits(tiny)));
    }

    @Test
    void canonicalOutputIsDeterministicImmutableAndSensitiveToExactEvidenceBytes() throws Exception {
        Evidence evidence = baseline();
        var reorderedGraph = graph(evidence.graph().components().get(2), evidence.graph().components().get(0),
                evidence.graph().components().get(1));
        var reorderedPolicy = policy(new LinkedHashSet<>(List.of(PACKAGED_COORD)),
                new LinkedHashSet<>(List.of(OMITTED)), new LinkedHashSet<>(List.of(PLATFORM_COORD)),
                evidence.policy().buildAddedRules(), evidence.policy().cardinalities());
        var first = reconcile(evidence);
        var second = reconcile(evidence.withGraph(reorderedGraph).withPolicy(reorderedPolicy));
        assertThat(first).isEqualTo(second);
        assertThat(first.canonicalOutput()).containsExactly(second.canonicalOutput());
        assertThat(first.reportSha256()).isEqualTo(second.reportSha256());
        assertThatThrownBy(() -> first.componentResults().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> first.classificationCounts().clear()).isInstanceOf(UnsupportedOperationException.class);

        var changedLockBytes = reconcile(evidence.withLockfile("# distinct exact bytes\n" + evidence.lockfile()));
        assertThat(changedLockBytes.lockfileSha256()).isNotEqualTo(first.lockfileSha256());
        assertThat(changedLockBytes.reportSha256()).isNotEqualTo(first.reportSha256());
        assertThat(new String(first.canonicalOutput(), StandardCharsets.UTF_8))
                .doesNotContain(temporaryDirectory.toAbsolutePath().toString());
    }

    private Evidence baseline() throws Exception {
        byte[] source = sourceArchive(NESTED_PATH, JARMODE_BYTES, null);
        var graph = graph(jar("test:alpha:1", "alpha-1.jar", PACKAGED_BYTES),
                jar("test:starter:1", "starter-1.jar", OMITTED_BYTES), platform("test:bom:1"));
        var policy = policy(Set.of(PACKAGED_COORD), Set.of(OMITTED), Set.of(PLATFORM_COORD),
                List.of(buildRule(source, JARMODE_BYTES)),
                new ExecutablePackagedJarReconciliation.Cardinalities(3, 1, 1, 1, 1, 2));
        return new Evidence(inventory(Map.of(PACKAGED_PATH, PACKAGED_BYTES, JARMODE_PATH, JARMODE_BYTES)),
                lockfile(graph.components()), metadata(graph.components(), source, null), graph, policy, source, LIMITS);
    }

    private ExecutablePackagedJarReconciliation.Reconciliation reconcile(Evidence evidence) {
        return ExecutablePackagedJarReconciliation.reconcile(evidence.inventory(),
                bytes(evidence.lockfile()), bytes(evidence.metadata()), evidence.graph(), evidence.policy(),
                evidence.sourceBytes(), evidence.limits());
    }

    private ExecutableArchiveInventory.Inventory inventory(Map<String, byte[]> libraries) throws Exception {
        Path archive = temporaryDirectory.resolve("synthetic-" + Math.abs(libraries.hashCode()) + ".jar");
        try (var output = new ZipOutputStream(Files.newOutputStream(archive))) {
            write(output, "META-INF/MANIFEST.MF", bytes("Manifest-Version: 1.0\r\n"
                    + "Main-Class: org.springframework.boot.loader.launch.JarLauncher\r\n"
                    + "Start-Class: test.Application\r\n"
                    + "Spring-Boot-Version: 3.5.16\r\n"
                    + "Spring-Boot-Classes: BOOT-INF/classes/\r\n"
                    + "Spring-Boot-Lib: BOOT-INF/lib/\r\n\r\n"));
            write(output, "BOOT-INF/classes/test/Application.class", bytes("application"));
            write(output, "org/springframework/boot/loader/launch/JarLauncher.class", bytes("loader"));
            libraries.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> writeUnchecked(output, entry.getKey(), entry.getValue()));
        }
        return ExecutableArchiveInventory.inspect(archive, java.util.Optional.empty(), INVENTORY_LIMITS);
    }

    private static byte[] sourceArchive(String nestedPath, byte[] nestedBytes, String marker) throws IOException {
        var bytes = new ByteArrayOutputStream();
        try (var output = new ZipOutputStream(bytes)) {
            write(output, nestedPath, nestedBytes);
            if (marker != null) {
                write(output, "marker.txt", bytes(marker));
            }
        }
        return bytes.toByteArray();
    }

    private static byte[] duplicateNestedSourceArchive() throws IOException {
        return sourceArchiveWithPatchedDuplicateName();
    }

    private static byte[] sourceArchiveWithPatchedDuplicateName() throws IOException {
        // Construct duplicate names through the ordinary writer, then patch equal-length entry names.
        var outputBytes = new ByteArrayOutputStream();
        try (var output = new ZipOutputStream(outputBytes)) {
            write(output, NESTED_PATH, JARMODE_BYTES);
            write(output, "META-INF/jarmode/duplicate---jarmode-tools.jar", JARMODE_BYTES);
        }
        byte[] result = outputBytes.toByteArray();
        byte[] target = bytes("duplicate---jarmode-tools.jar");
        byte[] replacement = bytes("spring-boot-jarmode-tools.jar");
        int replacements = 0;
        for (int offset = indexOf(result, target); offset >= 0; offset = indexOf(result, target)) {
            System.arraycopy(replacement, 0, result, offset, replacement.length);
            replacements++;
        }
        assertThat(replacements).isEqualTo(2);
        return result;
    }

    private static void writeUnchecked(ZipOutputStream output, String path, byte[] value) {
        try {
            write(output, path, value);
        } catch (IOException failure) {
            throw new AssertionError(failure);
        }
    }

    private static void write(ZipOutputStream output, String path, byte[] value) throws IOException {
        var entry = new ZipEntry(path);
        output.putNextEntry(entry);
        output.write(value);
        output.closeEntry();
    }

    private static ExecutablePackagedJarReconciliation.ResolvedComponent jar(
            String coordinate, String name, byte[] value) {
        return new ExecutablePackagedJarReconciliation.ResolvedComponent(
                coordinate(coordinate), JAR, name, sha256(value));
    }

    private static ExecutablePackagedJarReconciliation.ResolvedComponent platform(String coordinate) {
        return new ExecutablePackagedJarReconciliation.ResolvedComponent(
                coordinate(coordinate), PLATFORM, null, null);
    }

    private static ExecutablePackagedJarReconciliation.ResolvedGraph graph(
            ExecutablePackagedJarReconciliation.ResolvedComponent... components) {
        return new ExecutablePackagedJarReconciliation.ResolvedGraph("resolved-graph-v1", List.of(components));
    }

    private static ExecutablePackagedJarReconciliation.ClassificationPolicy policy(
            Set<ExecutablePackagedJarReconciliation.Coordinate> packaged,
            Set<ExecutablePackagedJarReconciliation.Coordinate> omitted,
            Set<ExecutablePackagedJarReconciliation.Coordinate> platforms,
            List<ExecutablePackagedJarReconciliation.BuildAddedRule> additions,
            ExecutablePackagedJarReconciliation.Cardinalities cardinalities) {
        return new ExecutablePackagedJarReconciliation.ClassificationPolicy(
                "classification-policy-v1", packaged, omitted, platforms, additions, cardinalities);
    }

    private static ExecutablePackagedJarReconciliation.BuildAddedRule buildRule(
            byte[] source, byte[] nested) {
        return new ExecutablePackagedJarReconciliation.BuildAddedRule(JARMODE_PATH, LOADER,
                "spring-boot-loader-tools-3.5.16.jar", sha256(source), NESTED_PATH, sha256(nested));
    }

    private static String lockfile(List<ExecutablePackagedJarReconciliation.ResolvedComponent> components) {
        var output = new StringBuilder("# synthetic Gradle lockfile\nempty=annotationProcessor\n");
        components.stream().map(component -> component.coordinate().value()).sorted()
                .forEach(value -> output.append(value).append("=productionRuntimeClasspath\n"));
        return output.toString();
    }

    private static String metadata(List<ExecutablePackagedJarReconciliation.ResolvedComponent> components,
            byte[] source, ExecutablePackagedJarReconciliation.Coordinate omit) {
        var output = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<verification-metadata xmlns=\"https://schema.gradle.org/dependency-verification\">"
                + "<components>");
        components.stream().filter(component -> component.kind() == JAR)
                .filter(component -> !component.coordinate().equals(omit))
                .sorted(java.util.Comparator.comparing(c -> c.coordinate().value()))
                .forEach(component -> output.append(componentXml(component.coordinate(),
                        component.artifactName(), component.artifactSha256())));
        output.append(componentXml(LOADER, "spring-boot-loader-tools-3.5.16.jar", sha256(source)));
        return output.append("</components></verification-metadata>").toString();
    }

    private static String componentXml(ExecutablePackagedJarReconciliation.Coordinate coordinate,
            String artifact, String checksum) {
        return "<component group=\"" + coordinate.group() + "\" name=\"" + coordinate.artifact()
                + "\" version=\"" + coordinate.version() + "\"><artifact name=\"" + artifact
                + "\"><sha256 value=\"" + checksum + "\"/></artifact></component>";
    }

    private static String metadataWithAdditionalChecksum(String xml,
            ExecutablePackagedJarReconciliation.Coordinate coordinate, String artifact, String checksum) {
        String marker = "<artifact name=\"" + artifact + "\">";
        int start = xml.indexOf(marker, xml.indexOf("name=\"" + coordinate.artifact() + "\""));
        int insertion = start + marker.length();
        return xml.substring(0, insertion) + "<sha256 value=\"" + checksum + "\"/>" + xml.substring(insertion);
    }

    private static ExecutablePackagedJarReconciliation.Coordinate coordinate(String value) {
        return ExecutablePackagedJarReconciliation.Coordinate.parse(value, InconsistentClassification);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private static String sha256(byte[] value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value));
        } catch (Exception impossible) {
            throw new AssertionError(impossible);
        }
    }

    private static void assertFailure(ExecutablePackagedJarReconciliation.FailureCategory category,
            org.junit.jupiter.api.function.Executable executable) {
        var failure = assertThrows(ExecutablePackagedJarReconciliation.Failure.class, executable);
        assertThat(failure.category()).isEqualTo(category);
        assertThat(failure.getMessage()).startsWith(category + ": ")
                .doesNotContain("locked-alpha-jar", "nested-jarmode-jar", temporaryPathFragment());
    }

    private static String temporaryPathFragment() {
        return "/Users/";
    }

    private static int indexOf(byte[] source, byte[] target) {
        outer:
        for (int offset = 0; offset <= source.length - target.length; offset++) {
            for (int index = 0; index < target.length; index++) {
                if (source[offset + index] != target[index]) {
                    continue outer;
                }
            }
            return offset;
        }
        return -1;
    }

    private record Evidence(ExecutableArchiveInventory.Inventory inventory, String lockfile,
            String metadata, ExecutablePackagedJarReconciliation.ResolvedGraph graph,
            ExecutablePackagedJarReconciliation.ClassificationPolicy policy,
            byte[] sourceBytes, ExecutablePackagedJarReconciliation.Limits limits) {
        private Evidence {
            sourceBytes = sourceBytes.clone();
        }

        @Override
        public byte[] sourceBytes() {
            return sourceBytes.clone();
        }

        Evidence withInventory(ExecutableArchiveInventory.Inventory value) {
            return new Evidence(value, lockfile, metadata, graph, policy, sourceBytes, limits);
        }

        Evidence withLockfile(String value) {
            return new Evidence(inventory, value, metadata, graph, policy, sourceBytes, limits);
        }

        Evidence withMetadata(String value) {
            return new Evidence(inventory, lockfile, value, graph, policy, sourceBytes, limits);
        }

        Evidence withGraph(ExecutablePackagedJarReconciliation.ResolvedGraph value) {
            return new Evidence(inventory, lockfile, metadata, value, policy, sourceBytes, limits);
        }

        Evidence withPolicy(ExecutablePackagedJarReconciliation.ClassificationPolicy value) {
            return new Evidence(inventory, lockfile, metadata, graph, value, sourceBytes, limits);
        }

        Evidence withSourceBytes(byte[] value) {
            return new Evidence(inventory, lockfile, metadata, graph, policy, value, limits);
        }

        Evidence withLimits(ExecutablePackagedJarReconciliation.Limits value) {
            return new Evidence(inventory, lockfile, metadata, graph, policy, sourceBytes, value);
        }
    }
}
