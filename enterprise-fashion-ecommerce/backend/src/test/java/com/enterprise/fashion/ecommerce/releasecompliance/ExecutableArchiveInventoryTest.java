package com.enterprise.fashion.ecommerce.releasecompliance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.APPLICATION_CONTENT;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.ARCHIVE_METADATA;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.CLASSPATH_INDEX;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.FLYWAY_MIGRATION;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.LAYERS_INDEX;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.MANIFEST;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.MIGRATION_DOCUMENTATION;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.OTHER;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.PACKAGED_LIBRARY;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.Category.SPRING_BOOT_LOADER;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.AmbiguousPath;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.DigestMismatch;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.DuplicateEntry;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.InvalidArchive;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.LimitExceeded;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.MalformedMetadata;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.MissingArchive;
import static com.enterprise.fashion.ecommerce.releasecompliance.ExecutableArchiveInventory.FailureCategory.UnsupportedStructure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutableArchiveInventoryTest {

    private static final byte[] EXECUTABLE_MANIFEST = ("Manifest-Version: 1.0\r\n"
            + "Main-Class: org.springframework.boot.loader.launch.JarLauncher\r\n"
            + "Start-Class: com.example.Application\r\n"
            + "Spring-Boot-Version: 3.5.16\r\n"
            + "Spring-Boot-Classes: BOOT-INF/classes/\r\n"
            + "Spring-Boot-Lib: BOOT-INF/lib/\r\n"
            + "Z-Attribute: last\r\n"
            + "a-Attribute: first\r\n"
            + "Long-Attribute: first\r\n second\r\n"
            + "\r\n").getBytes(StandardCharsets.UTF_8);

    private static final ExecutableArchiveInventory.Limits LIMITS =
            new ExecutableArchiveInventory.Limits(10_000_000L, 100, 1_000_000L,
                    5_000_000L, 512, 65_536L);

    @TempDir
    Path temporaryDirectory;

    @Test
    void inventoriesEveryEntryAndCategoryWithExactIdentity() throws Exception {
        Path archive = writeArchive("complete.jar", completeEntries(true), false);

        var inventory = ExecutableArchiveInventory.inspect(archive, Optional.of(sha256(Files.readAllBytes(archive))), LIMITS);

        assertThat(inventory.schemaVersion()).isEqualTo("bei01-executable-inventory-v1");
        assertThat(inventory.archiveFileName()).isEqualTo("complete.jar");
        assertThat(inventory.archiveByteSize()).isEqualTo(Files.size(archive));
        assertThat(inventory.archiveSha256()).isEqualTo(sha256(Files.readAllBytes(archive)));
        assertThat(inventory.entryCount()).isEqualTo(completeEntries(true).size());
        assertThat(inventory.entries()).extracting(ExecutableArchiveInventory.Entry::category)
                .contains(MANIFEST, APPLICATION_CONTENT, PACKAGED_LIBRARY, SPRING_BOOT_LOADER,
                        CLASSPATH_INDEX, LAYERS_INDEX, FLYWAY_MIGRATION,
                        MIGRATION_DOCUMENTATION, ARCHIVE_METADATA, OTHER);
        assertThat(inventory.entries()).extracting(ExecutableArchiveInventory.Entry::path)
                .containsExactly(
                        "BOOT-INF/classes/",
                        "BOOT-INF/classes/com/example/Application.class",
                        "BOOT-INF/classes/db/migration/README.md",
                        "BOOT-INF/classes/db/migration/V1__foundation.sql",
                        "BOOT-INF/classpath.idx",
                        "BOOT-INF/layers.idx",
                        "BOOT-INF/lib/dependency.jar",
                        "META-INF/MANIFEST.MF",
                        "META-INF/NOTICE.txt",
                        "docs/other.txt",
                        "org/springframework/boot/loader/launch/JarLauncher.class");
        var directory = inventory.entries().getFirst();
        assertThat(directory.kind()).isEqualTo(ExecutableArchiveInventory.Kind.DIRECTORY);
        assertThat(directory.sha256()).isNull();
        var library = inventory.entries().stream().filter(entry -> entry.category() == PACKAGED_LIBRARY).findFirst().orElseThrow();
        assertThat(library.sha256()).isEqualTo(sha256(bytes("synthetic-jar")));
        assertThat(inventory.manifestAttributes().keySet()).containsExactly(
                "a-Attribute", "Long-Attribute", "Main-Class", "Manifest-Version",
                "Spring-Boot-Classes", "Spring-Boot-Lib", "Spring-Boot-Version",
                "Start-Class", "Z-Attribute");
        assertThat(inventory.manifestAttributes().get("Long-Attribute")).isEqualTo("firstsecond");
    }

    @Test
    void returnsImmutableEqualModelsAndByteIdenticalCanonicalOutput() throws Exception {
        Path archive = writeArchive("repeatable.jar", completeEntries(true), false);

        var first = ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS);
        var second = ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS);

        assertThat(first).isEqualTo(second);
        assertThat(first.canonicalOutput()).containsExactly(second.canonicalOutput());
        assertThat(new String(first.canonicalOutput(), StandardCharsets.US_ASCII))
                .startsWith("bei01-executable-inventory-v1\narchive\t")
                .endsWith("\n")
                .doesNotContain(temporaryDirectory.toAbsolutePath().toString());
        assertThatThrownBy(() -> first.entries().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> first.manifestAttributes().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void canonicalEntryOrderingDoesNotDependOnZipOrder() throws Exception {
        List<EntryFixture> entries = completeEntries(true);
        Path forward = writeArchive("forward.jar", entries, false);
        var reversedEntries = new ArrayList<>(entries);
        java.util.Collections.reverse(reversedEntries);
        Path reversed = writeArchive("reversed.jar", reversedEntries, false);

        var first = ExecutableArchiveInventory.inspect(forward, Optional.empty(), LIMITS);
        var second = ExecutableArchiveInventory.inspect(reversed, Optional.empty(), LIMITS);

        assertThat(first.entries()).isEqualTo(second.entries());
        assertThat(first.manifestAttributes()).isEqualTo(second.manifestAttributes());
        assertThat(first.archiveSha256()).isNotEqualTo(second.archiveSha256());
    }

    @Test
    void classpathAndLayersIndexesAreOptional() throws Exception {
        Path archive = writeArchive("without-indexes.jar", completeEntries(false), false);

        var inventory = ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS);

        assertThat(inventory.entries()).extracting(ExecutableArchiveInventory.Entry::category)
                .doesNotContain(CLASSPATH_INDEX, LAYERS_INDEX);
    }

    @Test
    void rejectsMissingAndNonRegularArchives() throws Exception {
        assertFailure(MissingArchive, () -> ExecutableArchiveInventory.inspect(
                temporaryDirectory.resolve("missing.jar"), Optional.empty(), LIMITS));
        assertFailure(UnsupportedStructure, () -> ExecutableArchiveInventory.inspect(
                temporaryDirectory, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsSymbolicLinkInputs() throws Exception {
        Path target = writeArchive("link-target.jar", completeEntries(true), false);
        Path link = temporaryDirectory.resolve("linked.jar");
        Files.createSymbolicLink(link, target.getFileName());

        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(link, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsSourceReplacementDetectedDuringSnapshotCreation() throws Exception {
        Path source = writeArchive("replace-source.jar", completeEntries(true), false);
        Path replacement = writeArchive("replacement.jar", withExtra(file("replacement.txt", "replacement")), false);

        assertFailure(InvalidArchive, () -> ExecutableArchiveInventory.inspect(
                source, Optional.empty(), LIMITS,
                ignored -> Files.move(replacement, source, StandardCopyOption.REPLACE_EXISTING)));
    }

    @Test
    void rejectsInvalidAndTruncatedZipFiles() throws Exception {
        Path invalid = temporaryDirectory.resolve("invalid.jar");
        Files.writeString(invalid, "not a ZIP", StandardCharsets.UTF_8);
        assertFailure(InvalidArchive, () -> ExecutableArchiveInventory.inspect(invalid, Optional.empty(), LIMITS));

        Path valid = writeArchive("source.jar", completeEntries(true), false);
        byte[] bytes = Files.readAllBytes(valid);
        Path truncated = temporaryDirectory.resolve("truncated.jar");
        Files.write(truncated, Arrays.copyOf(bytes, bytes.length / 2));
        assertFailure(InvalidArchive, () -> ExecutableArchiveInventory.inspect(truncated, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsDuplicateExactEntryNames() throws Exception {
        var entries = completeEntries(true);
        entries.add(file("duplicate-A.txt", "first"));
        entries.add(file("duplicate-B.txt", "second"));
        Path archive = writeArchive("duplicates.jar", entries, false);
        replaceAll(archive, bytes("duplicate-B.txt"), bytes("duplicate-A.txt"));

        assertFailure(DuplicateEntry, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsAbsolutePlatformSpecificEmptySegmentDotAndControlPaths() throws Exception {
        for (String path : List.of("", "/absolute", "C:/absolute", "back\\slash", "dot/./path",
                "dot/../path", "double//segment", "control/\u0001name")) {
            Path archive = writeArchive("ambiguous-" + Math.abs(path.hashCode()) + ".jar",
                    withExtra(file(path, "hostile")), false);
            assertFailure(AmbiguousPath,
                    () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
        }
    }

    @Test
    void rejectsUnicodeNormalizationAndFileDirectoryIdentityCollisions() throws Exception {
        Path unicode = writeArchive("unicode.jar", withExtras(
                file("docs/caf\u00e9.txt", "one"), file("docs/cafe\u0301.txt", "two")), false);
        assertFailure(AmbiguousPath, () -> ExecutableArchiveInventory.inspect(unicode, Optional.empty(), LIMITS));

        Path identity = writeArchive("identity.jar", withExtras(
                file("collision", "file"), directory("collision/")), false);
        assertFailure(AmbiguousPath, () -> ExecutableArchiveInventory.inspect(identity, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsFileAncestorAndDescendantCollisionsInEitherOrder() throws Exception {
        Path ancestorFirst = writeArchive("ancestor-first.jar", withExtras(
                file("ambiguous", "file"), file("ambiguous/child", "child")), false);
        assertFailure(AmbiguousPath,
                () -> ExecutableArchiveInventory.inspect(ancestorFirst, Optional.empty(), LIMITS));

        Path descendantFirst = writeArchive("descendant-first.jar", withExtras(
                file("ambiguous/child", "child"), file("ambiguous", "file")), false);
        assertFailure(AmbiguousPath,
                () -> ExecutableArchiveInventory.inspect(descendantFirst, Optional.empty(), LIMITS));

        Path directoryDescendant = writeArchive("directory-descendant.jar", withExtras(
                file("ambiguous", "file"), directory("ambiguous/child/")), false);
        assertFailure(AmbiguousPath,
                () -> ExecutableArchiveInventory.inspect(directoryDescendant, Optional.empty(), LIMITS));

        Path directoryDescendantFirst = writeArchive("directory-descendant-first.jar", withExtras(
                directory("ambiguous/child/"), file("ambiguous", "file")), false);
        assertFailure(AmbiguousPath,
                () -> ExecutableArchiveInventory.inspect(directoryDescendantFirst, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsDigestMismatchAndNonCanonicalExpectedDigest() throws Exception {
        Path archive = writeArchive("digest.jar", completeEntries(true), false);
        assertFailure(DigestMismatch, () -> ExecutableArchiveInventory.inspect(
                archive, Optional.of("0".repeat(64)), LIMITS));
        assertThatIllegalArgumentException().isThrownBy(() -> ExecutableArchiveInventory.inspect(
                archive, Optional.of("A".repeat(64)), LIMITS));
    }

    @Test
    void rejectsDuplicateManifestAttributesInvalidContinuationAndUnsupportedEncoding() throws Exception {
        byte[] duplicate = manifestWith("main-class: duplicate\r\n");
        Path duplicateArchive = writeArchive("duplicate-manifest.jar", entriesWithManifest(duplicate, true), false);
        assertFailure(MalformedMetadata,
                () -> ExecutableArchiveInventory.inspect(duplicateArchive, Optional.empty(), LIMITS));

        byte[] continuation = bytes(" continuation-without-attribute\r\n\r\n");
        Path continuationArchive = writeArchive("continuation.jar", entriesWithManifest(continuation, true), false);
        assertFailure(MalformedMetadata,
                () -> ExecutableArchiveInventory.inspect(continuationArchive, Optional.empty(), LIMITS));

        byte[] invalidUtf8 = {(byte) 0xc3, (byte) 0x28, '\r', '\n', '\r', '\n'};
        Path encodingArchive = writeArchive("encoding.jar", entriesWithManifest(invalidUtf8, true), false);
        assertFailure(MalformedMetadata,
                () -> ExecutableArchiveInventory.inspect(encodingArchive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsMissingManifestAndIncompleteSpringBootStructures() throws Exception {
        var withoutManifest = completeEntries(true);
        withoutManifest.removeIf(entry -> entry.path().equals("META-INF/MANIFEST.MF"));
        Path missingManifest = writeArchive("missing-manifest.jar", withoutManifest, false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(missingManifest, Optional.empty(), LIMITS));

        var withoutLoader = completeEntries(true);
        withoutLoader.removeIf(entry -> entry.path().startsWith("org/springframework/boot/loader/"));
        Path missingLoader = writeArchive("missing-loader.jar", withoutLoader, false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(missingLoader, Optional.empty(), LIMITS));

        var withoutApplication = completeEntries(true);
        withoutApplication.removeIf(entry -> !entry.directory() && entry.path().startsWith("BOOT-INF/classes/"));
        Path missingApplication = writeArchive("missing-application.jar", withoutApplication, false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(missingApplication, Optional.empty(), LIMITS));

        var withoutLibrary = completeEntries(true);
        withoutLibrary.removeIf(entry -> entry.path().startsWith("BOOT-INF/lib/"));
        Path missingLibrary = writeArchive("missing-library.jar", withoutLibrary, false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(missingLibrary, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsIncorrectRequiredManifestValuesAndBlankStartClass() throws Exception {
        byte[] wrongMainClass = replaceManifestValue(
                "Main-Class: org.springframework.boot.loader.launch.JarLauncher",
                "Main-Class: com.example.NotTheBootLoader");
        Path wrongMainClassArchive = writeArchive(
                "wrong-main-class.jar", entriesWithCompleteStructure(wrongMainClass), false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(wrongMainClassArchive, Optional.empty(), LIMITS));

        byte[] blankStartClass = replaceManifestValue(
                "Start-Class: com.example.Application", "Start-Class: ");
        Path blankStartClassArchive = writeArchive(
                "blank-start-class.jar", entriesWithCompleteStructure(blankStartClass), false);
        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(blankStartClassArchive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsUnsupportedCompressionMethods() throws Exception {
        Path archive = writeArchive("compression.jar", completeEntries(true), false);
        patchCompressionMethods(archive, 99);

        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void ignoresEocdSignaturesEmbeddedInValidZipComments() throws Exception {
        Path archive = writeArchive(
                "comment-signature.jar", completeEntries(true), false, deceptiveEocdComment());

        var inventory = ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS);

        assertThat(inventory.archiveFileName()).isEqualTo("comment-signature.jar");
    }

    @Test
    void findsUnsupportedCompressionBeforeAnEocdSignatureInTheZipComment() throws Exception {
        Path archive = writeArchive(
                "comment-compression.jar", completeEntries(true), false, deceptiveEocdComment());
        patchCompressionMethods(archive, 99);

        assertFailure(UnsupportedStructure,
                () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsMalformedEocdCommentLength() throws Exception {
        Path archive = writeArchive("comment-length.jar", completeEntries(true), false, "comment");
        patchEocdUnsignedShort(archive, 20, 1);

        assertFailure(InvalidArchive,
                () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsMultiDiskEocdMetadata() throws Exception {
        Path archive = writeArchive("multi-disk.jar", completeEntries(true), false);
        patchEocdUnsignedShort(archive, 4, 1);

        assertFailure(InvalidArchive,
                () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void rejectsCentralDirectoryExtentMismatch() throws Exception {
        Path archive = writeArchive("directory-extent.jar", completeEntries(true), false);
        patchEocdUnsignedInt(archive, 12, eocdUnsignedInt(archive, 12) + 1L);

        assertFailure(InvalidArchive,
                () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void enforcesArchiveEntryPathAndManifestLimits() throws Exception {
        Path archive = writeArchive("limits.jar", completeEntries(true), false);
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(Files.size(archive) - 1, 100, 1_000_000, 5_000_000, 512, 65_536)));
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, completeEntries(true).size() - 1, 1_000_000, 5_000_000, 512, 65_536)));
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, 100, 5, 5_000_000, 512, 5)));
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, 100, 1_000_000, 20, 512, 65_536)));
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, 100, 1_000_000, 5_000_000, 10, 65_536)));
        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, 100, 1_000_000, 5_000_000, 512, EXECUTABLE_MANIFEST.length - 1)));
    }

    @Test
    void enforcesStreamingEntryLimitWhenDeclaredSizeIsTooSmall() throws Exception {
        String content = "x".repeat(2_000);
        Path archive = writeArchive("dishonest-size.jar", withExtra(file("large-content.bin", content)), false);
        patchUncompressedSize(archive, "large-content.bin", 1);

        assertFailure(LimitExceeded, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(),
                limits(10_000_000, 100, 1_000, 5_000_000, 512, 1_000)));
    }

    @Test
    void rejectsCrcMismatch() throws Exception {
        var entries = completeEntries(true);
        entries.removeIf(entry -> entry.path().equals("BOOT-INF/classes/com/example/Application.class"));
        entries.add(file("BOOT-INF/classes/com/example/Application.class", "CRC-MISMATCH-CONTENT"));
        Path archive = writeArchive("crc.jar", entries, true);
        byte[] archiveBytes = Files.readAllBytes(archive);
        int contentOffset = indexOf(archiveBytes, bytes("CRC-MISMATCH-CONTENT"));
        assertThat(contentOffset).isGreaterThanOrEqualTo(0);
        archiveBytes[contentOffset] ^= 1;
        Files.write(archive, archiveBytes);

        assertFailure(InvalidArchive, () -> ExecutableArchiveInventory.inspect(archive, Optional.empty(), LIMITS));
    }

    @Test
    void validatesAllLimitsBeforeInspection() {
        assertThatIllegalArgumentException().isThrownBy(() -> limits(0, 1, 1, 1, 1, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 0, 1, 1, 1, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 1, 0, 1, 1, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 1, 1, 0, 1, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 1, 1, 1, 0, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 1, 1, 1, 1, 0));
        assertThatIllegalArgumentException().isThrownBy(() -> limits(1, 1, 1, 1, 1, 2));
    }

    private List<EntryFixture> completeEntries(boolean indexes) {
        var entries = entriesWithManifest(EXECUTABLE_MANIFEST, indexes);
        entries.add(directory("BOOT-INF/classes/"));
        entries.add(file("BOOT-INF/classes/com/example/Application.class", "application"));
        entries.add(file("BOOT-INF/lib/dependency.jar", "synthetic-jar"));
        entries.add(file("org/springframework/boot/loader/launch/JarLauncher.class", "loader"));
        entries.add(file("BOOT-INF/classes/db/migration/V1__foundation.sql", "select 1;"));
        entries.add(file("BOOT-INF/classes/db/migration/README.md", "migration guidance"));
        entries.add(file("META-INF/NOTICE.txt", "notice"));
        entries.add(file("docs/other.txt", "other"));
        return entries;
    }

    private List<EntryFixture> entriesWithManifest(byte[] manifest, boolean indexes) {
        var entries = new ArrayList<EntryFixture>();
        entries.add(new EntryFixture("META-INF/MANIFEST.MF", manifest, false));
        if (indexes) {
            entries.add(file("BOOT-INF/classpath.idx", "classpath"));
            entries.add(file("BOOT-INF/layers.idx", "layers"));
        }
        return entries;
    }

    private List<EntryFixture> entriesWithCompleteStructure(byte[] manifest) {
        var entries = entriesWithManifest(manifest, true);
        entries.add(directory("BOOT-INF/classes/"));
        entries.add(file("BOOT-INF/classes/com/example/Application.class", "application"));
        entries.add(file("BOOT-INF/lib/dependency.jar", "synthetic-jar"));
        entries.add(file("org/springframework/boot/loader/launch/JarLauncher.class", "loader"));
        return entries;
    }

    private List<EntryFixture> withExtra(EntryFixture extra) {
        return withExtras(extra);
    }

    private List<EntryFixture> withExtras(EntryFixture... extras) {
        var entries = completeEntries(true);
        entries.addAll(List.of(extras));
        return entries;
    }

    private Path writeArchive(String fileName, List<EntryFixture> entries, boolean stored) throws IOException {
        return writeArchive(fileName, entries, stored, null);
    }

    private Path writeArchive(String fileName, List<EntryFixture> entries, boolean stored, String comment)
            throws IOException {
        Path archive = temporaryDirectory.resolve(fileName);
        try (var output = new ZipOutputStream(Files.newOutputStream(archive))) {
            if (comment != null) {
                output.setComment(comment);
            }
            for (EntryFixture fixture : entries) {
                var entry = new ZipEntry(fixture.path());
                if (stored) {
                    CRC32 crc = new CRC32();
                    crc.update(fixture.content());
                    entry.setMethod(ZipEntry.STORED);
                    entry.setSize(fixture.content().length);
                    entry.setCompressedSize(fixture.content().length);
                    entry.setCrc(crc.getValue());
                }
                output.putNextEntry(entry);
                if (!fixture.directory()) {
                    output.write(fixture.content());
                }
                output.closeEntry();
            }
        }
        return archive;
    }

    private static String deceptiveEocdComment() {
        return "comment-prefix-PK\u0005\u0006" + "\u0000".repeat(20);
    }

    private static EntryFixture file(String path, String content) {
        return new EntryFixture(path, bytes(content), false);
    }

    private static EntryFixture directory(String path) {
        return new EntryFixture(path, new byte[0], true);
    }

    private static byte[] manifestWith(String extraLine) {
        String manifest = new String(EXECUTABLE_MANIFEST, StandardCharsets.UTF_8);
        return manifest.replace("\r\n\r\n", "\r\n" + extraLine + "\r\n")
                .getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] replaceManifestValue(String existing, String replacement) {
        return new String(EXECUTABLE_MANIFEST, StandardCharsets.UTF_8)
                .replace(existing, replacement)
                .getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private static String sha256(byte[] value) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value));
    }

    private static ExecutableArchiveInventory.Limits limits(long archive, int entries,
            long entry, long total, int path, long manifest) {
        return new ExecutableArchiveInventory.Limits(archive, entries, entry, total, path, manifest);
    }

    private static void assertFailure(ExecutableArchiveInventory.FailureCategory category,
            org.junit.jupiter.api.function.Executable executable) {
        var failure = assertThrows(ExecutableArchiveInventory.Failure.class, executable);
        assertThat(failure.category()).isEqualTo(category);
        assertThat(failure.getMessage()).startsWith(category + ": ").doesNotContain("synthetic-jar", "select 1;");
    }

    private static void replaceAll(Path file, byte[] target, byte[] replacement) throws IOException {
        assertThat(replacement).hasSameSizeAs(target);
        byte[] bytes = Files.readAllBytes(file);
        int replacements = 0;
        for (int offset = indexOf(bytes, target); offset >= 0; offset = indexOf(bytes, target)) {
            System.arraycopy(replacement, 0, bytes, offset, replacement.length);
            replacements++;
        }
        assertThat(replacements).isEqualTo(2);
        Files.write(file, bytes);
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

    private static void patchCompressionMethods(Path archive, int method) throws IOException {
        byte[] bytes = Files.readAllBytes(archive);
        int patched = 0;
        for (int offset = 0; offset <= bytes.length - 12; offset++) {
            if (matches(bytes, offset, 0x50, 0x4b, 0x03, 0x04)) {
                bytes[offset + 8] = (byte) method;
                bytes[offset + 9] = (byte) (method >>> 8);
                patched++;
            } else if (matches(bytes, offset, 0x50, 0x4b, 0x01, 0x02)) {
                bytes[offset + 10] = (byte) method;
                bytes[offset + 11] = (byte) (method >>> 8);
                patched++;
            }
        }
        assertThat(patched).isGreaterThan(0);
        Files.write(archive, bytes);
    }

    private static void patchUncompressedSize(Path archive, String entryName, int size) throws IOException {
        byte[] bytes = Files.readAllBytes(archive);
        byte[] name = bytes(entryName);
        int patched = 0;
        for (int offset = 0; offset <= bytes.length - 30; offset++) {
            if (matches(bytes, offset, 0x50, 0x4b, 0x03, 0x04)) {
                int nameLength = unsignedShort(bytes, offset + 26);
                if (nameLength == name.length && matches(bytes, offset + 30, name)) {
                    writeLittleEndianInt(bytes, offset + 22, size);
                    patched++;
                }
            } else if (matches(bytes, offset, 0x50, 0x4b, 0x01, 0x02)) {
                int nameLength = unsignedShort(bytes, offset + 28);
                if (nameLength == name.length && matches(bytes, offset + 46, name)) {
                    writeLittleEndianInt(bytes, offset + 24, size);
                    patched++;
                }
            }
        }
        assertThat(patched).isEqualTo(2);
        Files.write(archive, bytes);
    }

    private static void patchEocdUnsignedShort(Path archive, int relativeOffset, int value) throws IOException {
        byte[] bytes = Files.readAllBytes(archive);
        int eocd = eocdOffset(bytes);
        bytes[eocd + relativeOffset] = (byte) value;
        bytes[eocd + relativeOffset + 1] = (byte) (value >>> 8);
        Files.write(archive, bytes);
    }

    private static void patchEocdUnsignedInt(Path archive, int relativeOffset, long value) throws IOException {
        byte[] bytes = Files.readAllBytes(archive);
        writeLittleEndianInt(bytes, eocdOffset(bytes) + relativeOffset, (int) value);
        Files.write(archive, bytes);
    }

    private static long eocdUnsignedInt(Path archive, int relativeOffset) throws IOException {
        byte[] bytes = Files.readAllBytes(archive);
        int offset = eocdOffset(bytes) + relativeOffset;
        return Integer.toUnsignedLong(Byte.toUnsignedInt(bytes[offset])
                | Byte.toUnsignedInt(bytes[offset + 1]) << 8
                | Byte.toUnsignedInt(bytes[offset + 2]) << 16
                | Byte.toUnsignedInt(bytes[offset + 3]) << 24);
    }

    private static int eocdOffset(byte[] bytes) {
        for (int offset = bytes.length - 22; offset >= Math.max(0, bytes.length - 65_557); offset--) {
            if (matches(bytes, offset, 0x50, 0x4b, 0x05, 0x06)) {
                int commentLength = unsignedShort(bytes, offset + 20);
                if (offset + 22 + commentLength == bytes.length) {
                    return offset;
                }
            }
        }
        throw new AssertionError("synthetic ZIP has no valid EOCD");
    }

    private static int unsignedShort(byte[] bytes, int offset) {
        return Byte.toUnsignedInt(bytes[offset]) | Byte.toUnsignedInt(bytes[offset + 1]) << 8;
    }

    private static void writeLittleEndianInt(byte[] bytes, int offset, int value) {
        for (int index = 0; index < Integer.BYTES; index++) {
            bytes[offset + index] = (byte) (value >>> (index * 8));
        }
    }

    private static boolean matches(byte[] bytes, int offset, int... expected) {
        for (int index = 0; index < expected.length; index++) {
            if (Byte.toUnsignedInt(bytes[offset + index]) != expected[index]) {
                return false;
            }
        }
        return true;
    }

    private static boolean matches(byte[] bytes, int offset, byte[] expected) {
        return offset >= 0 && offset + expected.length <= bytes.length
                && Arrays.equals(bytes, offset, offset + expected.length, expected, 0, expected.length);
    }

    private record EntryFixture(String path, byte[] content, boolean directory) {
        private EntryFixture {
            content = content.clone();
        }

        @Override
        public byte[] content() {
            return content.clone();
        }
    }
}
