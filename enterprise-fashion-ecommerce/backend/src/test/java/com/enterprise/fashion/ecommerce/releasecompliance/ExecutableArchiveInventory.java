package com.enterprise.fashion.ecommerce.releasecompliance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/** Test-scoped, read-only inventory of one Spring Boot executable archive. */
final class ExecutableArchiveInventory {

    static final String SCHEMA_VERSION = "bei01-executable-inventory-v1";
    private static final int BUFFER_SIZE = 8192;
    private static final Pattern SHA256 = Pattern.compile("[0-9a-f]{64}");
    private static final Pattern ATTRIBUTE_NAME = Pattern.compile("[A-Za-z0-9_-]{1,70}");
    private static final Comparator<String> UTF8_ORDER =
            (left, right) -> Arrays.compareUnsigned(left.getBytes(StandardCharsets.UTF_8),
                    right.getBytes(StandardCharsets.UTF_8));
    private static final Comparator<String> ATTRIBUTE_ORDER =
            String.CASE_INSENSITIVE_ORDER.thenComparing(Comparator.naturalOrder());

    private ExecutableArchiveInventory() {
    }

    static Inventory inspect(Path archive, Optional<String> expectedSha256, Limits limits) {
        return inspect(archive, expectedSha256, limits, ignored -> { });
    }

    static Inventory inspect(Path archive, Optional<String> expectedSha256, Limits limits,
            SourceSnapshotObserver observer) {
        Objects.requireNonNull(archive, "archive");
        Objects.requireNonNull(expectedSha256, "expectedSha256");
        Objects.requireNonNull(limits, "limits");
        Objects.requireNonNull(observer, "observer");
        expectedSha256.ifPresent(ExecutableArchiveInventory::requireCanonicalSha256);

        if (!Files.exists(archive, LinkOption.NOFOLLOW_LINKS)) {
            throw failure(FailureCategory.MissingArchive, "archive does not exist");
        }
        if (!Files.isRegularFile(archive, LinkOption.NOFOLLOW_LINKS)) {
            throw failure(FailureCategory.UnsupportedStructure, "archive is not a regular file");
        }

        String fileName = archive.getFileName() == null ? "" : archive.getFileName().toString();
        if (fileName.isEmpty()) {
            throw failure(FailureCategory.AmbiguousPath, "archive filename is unavailable");
        }

        Snapshot snapshot = createSnapshot(archive, limits, observer);
        try {
            String archiveSha256 = digest(snapshot.path());
            if (expectedSha256.isPresent() && !archiveSha256.equals(expectedSha256.orElseThrow())) {
                throw failure(FailureCategory.DigestMismatch, "archive SHA-256 does not match");
            }
            return inspectSnapshot(snapshot.path(), fileName, snapshot.size(), archiveSha256, limits);
        } finally {
            deleteSnapshot(snapshot.path());
        }
    }

    private static Inventory inspectSnapshot(Path snapshot, String fileName, long archiveSize,
            String archiveSha256, Limits limits) {

        var entries = new ArrayList<Entry>();
        var exactPaths = new HashSet<String>();
        var normalizedPaths = new HashMap<String, String>();
        var identities = new HashMap<String, Kind>();
        byte[][] rawManifest = new byte[1][];
        long[] totalUncompressed = {0L};

        try {
            if (hasConfirmedUnsupportedCompression(snapshot)) {
                throw failure(FailureCategory.UnsupportedStructure,
                        "archive uses an unsupported compression method");
            }
        } catch (IOException malformed) {
            throw failure(FailureCategory.InvalidArchive, "archive central directory cannot be read safely");
        }

        try (var zip = new ZipFile(snapshot.toFile(), StandardCharsets.UTF_8)) {
            Enumeration<? extends ZipEntry> enumeration = zip.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (entries.size() == limits.maximumEntries()) {
                    throw failure(FailureCategory.LimitExceeded, "ZIP entry-count limit exceeded");
                }
                String path = validatePath(zipEntry.getName(), limits, exactPaths, normalizedPaths, identities,
                        zipEntry.isDirectory() ? Kind.DIRECTORY : Kind.FILE);
                validateMetadata(zipEntry);

                if (zipEntry.isDirectory()) {
                    if (zipEntry.getSize() != 0L || zipEntry.getCrc() != 0L) {
                        throw failure(FailureCategory.InvalidArchive, "directory metadata is inconsistent");
                    }
                    entries.add(new Entry(path, Kind.DIRECTORY, categorize(path), 0L,
                            zipEntry.getCompressedSize(), 0L, zipEntry.getMethod(), null));
                    continue;
                }

                if (zipEntry.getSize() > limits.maximumEntryUncompressedBytes()) {
                    throw failure(FailureCategory.LimitExceeded, "entry byte limit exceeded");
                }
                boolean manifest = "META-INF/MANIFEST.MF".equals(path);
                if (manifest && zipEntry.getSize() > limits.maximumManifestBytes()) {
                    throw failure(FailureCategory.LimitExceeded, "manifest byte limit exceeded");
                }

                ReadResult read = readEntry(zip, zipEntry, limits, totalUncompressed, manifest);
                if (read.size() != zipEntry.getSize() || read.crc32() != zipEntry.getCrc()) {
                    throw failure(FailureCategory.InvalidArchive, "entry size or CRC does not match metadata");
                }
                if (manifest) {
                    rawManifest[0] = read.retainedBytes();
                }
                entries.add(new Entry(path, Kind.FILE, categorize(path), read.size(),
                        zipEntry.getCompressedSize(), read.crc32(), zipEntry.getMethod(), read.sha256()));
            }
        } catch (Failure known) {
            throw known;
        } catch (ZipException malformed) {
            throw failure(FailureCategory.InvalidArchive, "archive is not a valid ZIP file");
        } catch (IOException unreadable) {
            throw failure(FailureCategory.InvalidArchive, "archive content cannot be read safely");
        } catch (IllegalArgumentException invalidNameEncoding) {
            throw failure(FailureCategory.AmbiguousPath, "archive contains an unsupported entry name");
        }

        if (entries.isEmpty()) {
            throw failure(FailureCategory.InvalidArchive, "archive contains no entries");
        }
        Map<String, String> manifestAttributes = parseManifest(rawManifest[0], limits);
        entries.sort(Comparator.comparing(Entry::path, UTF8_ORDER));
        validateSpringBootStructure(entries, manifestAttributes);

        return new Inventory(SCHEMA_VERSION, fileName, archiveSize, archiveSha256,
                entries.size(), manifestAttributes, entries);
    }

    private static boolean hasConfirmedUnsupportedCompression(Path snapshot) throws IOException {
        try (var archive = new RandomAccessFile(snapshot.toFile(), "r")) {
            long length = archive.length();
            long searchStart = Math.max(0L, length - 65_557L);
            long endOfCentralDirectory = -1L;
            for (long offset = length - 22L; offset >= searchStart; offset--) {
                archive.seek(offset);
                if (readUnsignedIntLittleEndian(archive) == 0x06054b50L) {
                    endOfCentralDirectory = offset;
                    break;
                }
            }
            if (endOfCentralDirectory < 0L) {
                return false;
            }
            archive.seek(endOfCentralDirectory + 10L);
            int entryCount = readUnsignedShortLittleEndian(archive);
            long centralDirectorySize = readUnsignedIntLittleEndian(archive);
            long centralDirectoryOffset = readUnsignedIntLittleEndian(archive);
            if (entryCount == 0xffff || centralDirectorySize == 0xffffffffL
                    || centralDirectoryOffset == 0xffffffffL
                    || centralDirectoryOffset + centralDirectorySize > endOfCentralDirectory) {
                return false;
            }
            long entryOffset = centralDirectoryOffset;
            for (int index = 0; index < entryCount; index++) {
                if (entryOffset + 46L > endOfCentralDirectory) {
                    return false;
                }
                archive.seek(entryOffset);
                if (readUnsignedIntLittleEndian(archive) != 0x02014b50L) {
                    return false;
                }
                archive.seek(entryOffset + 10L);
                int method = readUnsignedShortLittleEndian(archive);
                if (method != ZipEntry.STORED && method != ZipEntry.DEFLATED) {
                    return true;
                }
                archive.seek(entryOffset + 28L);
                int nameLength = readUnsignedShortLittleEndian(archive);
                int extraLength = readUnsignedShortLittleEndian(archive);
                int commentLength = readUnsignedShortLittleEndian(archive);
                entryOffset += 46L + nameLength + extraLength + commentLength;
            }
            return false;
        }
    }

    private static int readUnsignedShortLittleEndian(RandomAccessFile input) throws IOException {
        return input.readUnsignedByte() | input.readUnsignedByte() << 8;
    }

    private static long readUnsignedIntLittleEndian(RandomAccessFile input) throws IOException {
        return Integer.toUnsignedLong(readUnsignedShortLittleEndian(input)
                | readUnsignedShortLittleEndian(input) << 16);
    }

    private static Snapshot createSnapshot(Path archive, Limits limits, SourceSnapshotObserver observer) {
        Path snapshot = null;
        try {
            BasicFileAttributes before = Files.readAttributes(
                    archive, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            if (!before.isRegularFile() || before.isSymbolicLink()) {
                throw failure(FailureCategory.UnsupportedStructure, "archive is not a regular file");
            }
            snapshot = Files.createTempFile("bei01-executable-inventory-", ".jar");
            long copied = 0L;
            long openedSize;
            long finalChannelSize;
            try (var source = Files.newByteChannel(archive, Set.of(StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS));
                    var output = Files.newOutputStream(snapshot, StandardOpenOption.TRUNCATE_EXISTING)) {
                openedSize = source.size();
                observer.afterSourceOpened(archive);
                byte[] buffer = new byte[BUFFER_SIZE];
                var input = java.nio.channels.Channels.newInputStream(source);
                int count;
                while ((count = input.read(buffer)) != -1) {
                    copied = addBounded(copied, count, limits.maximumArchiveBytes(),
                            "archive byte limit exceeded");
                    output.write(buffer, 0, count);
                }
                finalChannelSize = source.size();
            }
            BasicFileAttributes after = Files.readAttributes(
                    archive, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            if (!sameSource(before, after) || openedSize != finalChannelSize || copied != openedSize) {
                throw failure(FailureCategory.InvalidArchive, "archive changed while the snapshot was created");
            }
            return new Snapshot(snapshot, copied);
        } catch (Failure known) {
            if (snapshot != null) {
                deleteSnapshot(snapshot);
            }
            throw known;
        } catch (IOException unreadable) {
            if (snapshot != null) {
                deleteSnapshot(snapshot);
            }
            throw failure(FailureCategory.InvalidArchive, "archive snapshot cannot be created safely");
        }
    }

    private static boolean sameSource(BasicFileAttributes before, BasicFileAttributes after) {
        return after.isRegularFile()
                && !after.isSymbolicLink()
                && Objects.equals(before.fileKey(), after.fileKey())
                && before.size() == after.size()
                && before.lastModifiedTime().equals(after.lastModifiedTime());
    }

    private static void deleteSnapshot(Path snapshot) {
        try {
            Files.deleteIfExists(snapshot);
        } catch (IOException deletionFailure) {
            throw failure(FailureCategory.InvalidArchive, "private archive snapshot could not be deleted");
        }
    }

    private static String validatePath(String path, Limits limits, Set<String> exactPaths,
            Map<String, String> normalizedPaths, Map<String, Kind> identities, Kind kind) {
        if (path == null || path.isEmpty()) {
            throw failure(FailureCategory.AmbiguousPath, "entry path is empty");
        }
        if (path.getBytes(StandardCharsets.UTF_8).length > limits.maximumUtf8PathBytes()) {
            throw failure(FailureCategory.LimitExceeded, "entry path byte limit exceeded");
        }
        if (path.startsWith("/") || path.indexOf('\\') >= 0 || isDriveAbsolute(path)) {
            throw failure(FailureCategory.AmbiguousPath, "entry path is absolute or platform-specific");
        }
        for (int index = 0; index < path.length(); index++) {
            if (Character.isISOControl(path.charAt(index))) {
                throw failure(FailureCategory.AmbiguousPath, "entry path contains a control character");
            }
        }
        String withoutDirectoryMarker = kind == Kind.DIRECTORY ? path.substring(0, path.length() - 1) : path;
        if (withoutDirectoryMarker.isEmpty()) {
            throw failure(FailureCategory.AmbiguousPath, "root directory entry is ambiguous");
        }
        String[] segments = withoutDirectoryMarker.split("/", -1);
        for (String segment : segments) {
            if (segment.isEmpty() || ".".equals(segment) || "..".equals(segment)) {
                throw failure(FailureCategory.AmbiguousPath, "entry path contains an ambiguous segment");
            }
        }
        if (!exactPaths.add(path)) {
            throw failure(FailureCategory.DuplicateEntry, "archive contains a duplicate entry name");
        }
        String normalized = Normalizer.normalize(path, Normalizer.Form.NFC);
        String previous = normalizedPaths.putIfAbsent(normalized, path);
        if (previous != null && !previous.equals(path)) {
            throw failure(FailureCategory.AmbiguousPath, "entry paths collide under Unicode normalization");
        }
        Kind previousKind = identities.putIfAbsent(withoutDirectoryMarker, kind);
        if (previousKind != null && previousKind != kind) {
            throw failure(FailureCategory.AmbiguousPath, "file and directory entry identities collide");
        }
        rejectHierarchicalCollision(withoutDirectoryMarker, kind, identities);
        return path;
    }

    private static void rejectHierarchicalCollision(String identity, Kind kind, Map<String, Kind> identities) {
        for (int separator = identity.indexOf('/'); separator >= 0; separator = identity.indexOf('/', separator + 1)) {
            if (identities.get(identity.substring(0, separator)) == Kind.FILE) {
                throw failure(FailureCategory.AmbiguousPath, "a file entry is used as a directory");
            }
        }
        if (kind == Kind.FILE) {
            String descendantPrefix = identity + "/";
            if (identities.keySet().stream().anyMatch(candidate -> candidate.startsWith(descendantPrefix))) {
                throw failure(FailureCategory.AmbiguousPath, "a file entry has existing descendants");
            }
        }
    }

    private static boolean isDriveAbsolute(String path) {
        return path.length() >= 3 && Character.isLetter(path.charAt(0)) && path.charAt(1) == ':'
                && path.charAt(2) == '/';
    }

    private static void validateMetadata(ZipEntry entry) {
        if (entry.getMethod() != ZipEntry.STORED && entry.getMethod() != ZipEntry.DEFLATED) {
            throw failure(FailureCategory.UnsupportedStructure, "entry uses an unsupported compression method");
        }
        if (entry.getSize() < 0L || entry.getCompressedSize() < 0L || entry.getCrc() < 0L) {
            throw failure(FailureCategory.InvalidArchive, "entry metadata is incomplete");
        }
    }

    private static ReadResult readEntry(ZipFile zip, ZipEntry entry, Limits limits,
            long[] totalUncompressed, boolean retainBytes) throws IOException {
        MessageDigest digest = sha256();
        CRC32 crc = new CRC32();
        ByteArrayOutputStream retained = retainBytes ? new ByteArrayOutputStream() : null;
        long size = 0L;
        byte[] buffer = new byte[BUFFER_SIZE];
        try (InputStream input = zip.getInputStream(entry)) {
            int count;
            while ((count = input.read(buffer)) != -1) {
                size = addBounded(size, count, limits.maximumEntryUncompressedBytes(),
                        "entry byte limit exceeded");
                totalUncompressed[0] = addBounded(totalUncompressed[0], count,
                        limits.maximumTotalUncompressedBytes(), "total uncompressed byte limit exceeded");
                if (retainBytes && size > limits.maximumManifestBytes()) {
                    throw failure(FailureCategory.LimitExceeded, "manifest byte limit exceeded");
                }
                digest.update(buffer, 0, count);
                crc.update(buffer, 0, count);
                if (retained != null) {
                    retained.write(buffer, 0, count);
                }
            }
        }
        return new ReadResult(size, crc.getValue(), HexFormat.of().formatHex(digest.digest()),
                retained == null ? null : retained.toByteArray());
    }

    private static long addBounded(long current, int addition, long maximum, String failureMessage) {
        if (current > maximum - addition) {
            throw failure(FailureCategory.LimitExceeded, failureMessage);
        }
        return current + addition;
    }

    private static Map<String, String> parseManifest(byte[] bytes, Limits limits) {
        if (bytes == null) {
            throw failure(FailureCategory.UnsupportedStructure, "executable manifest is missing");
        }
        if (bytes.length > limits.maximumManifestBytes()) {
            throw failure(FailureCategory.LimitExceeded, "manifest byte limit exceeded");
        }
        String text;
        try {
            text = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(java.nio.ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException unsupported) {
            throw failure(FailureCategory.MalformedMetadata, "manifest is not valid UTF-8");
        }
        for (int index = 0; index < text.length(); index++) {
            if (text.charAt(index) == '\r' && (index + 1 == text.length() || text.charAt(index + 1) != '\n')) {
                throw failure(FailureCategory.MalformedMetadata, "manifest contains an unsupported line ending");
            }
        }
        String[] physicalLines = text.replace("\r\n", "\n").split("\n", -1);
        var logicalLines = new ArrayList<String>();
        String current = null;
        for (String line : physicalLines) {
            if (line.isEmpty()) {
                if (current != null) {
                    logicalLines.add(current);
                }
                current = null;
                break;
            }
            if (line.charAt(0) == ' ') {
                if (current == null) {
                    throw failure(FailureCategory.MalformedMetadata, "manifest continuation has no attribute");
                }
                current += line.substring(1);
            } else {
                if (current != null) {
                    logicalLines.add(current);
                }
                current = line;
            }
        }
        if (current != null) {
            logicalLines.add(current);
        }

        var attributes = new HashMap<String, Attribute>();
        for (String line : logicalLines) {
            int separator = line.indexOf(": ");
            if (separator <= 0 || separator + 2 > line.length()) {
                throw failure(FailureCategory.MalformedMetadata, "manifest attribute syntax is invalid");
            }
            String name = line.substring(0, separator);
            String value = line.substring(separator + 2);
            if (!ATTRIBUTE_NAME.matcher(name).matches()) {
                throw failure(FailureCategory.MalformedMetadata, "manifest attribute name is invalid");
            }
            String identity = name.toLowerCase(Locale.ROOT);
            if (attributes.putIfAbsent(identity, new Attribute(name, value)) != null) {
                throw failure(FailureCategory.MalformedMetadata, "manifest contains a duplicate attribute");
            }
        }
        var sorted = new ArrayList<>(attributes.values());
        sorted.sort(Comparator.comparing(Attribute::name, ATTRIBUTE_ORDER));
        var ordered = new LinkedHashMap<String, String>();
        for (Attribute attribute : sorted) {
            ordered.put(attribute.name(), attribute.value());
        }
        return Collections.unmodifiableMap(ordered);
    }

    private static void validateSpringBootStructure(List<Entry> entries, Map<String, String> manifest) {
        requireManifest(manifest, "Main-Class", "org.springframework.boot.loader.launch.JarLauncher");
        requireNonBlankManifest(manifest, "Start-Class");
        requireManifest(manifest, "Spring-Boot-Classes", "BOOT-INF/classes/");
        requireManifest(manifest, "Spring-Boot-Lib", "BOOT-INF/lib/");
        boolean applicationContent = entries.stream()
                .anyMatch(entry -> entry.kind() == Kind.FILE && entry.path().startsWith("BOOT-INF/classes/"));
        boolean packagedLibrary = entries.stream()
                .anyMatch(entry -> entry.kind() == Kind.FILE && entry.category() == Category.PACKAGED_LIBRARY);
        boolean loaderContent = entries.stream()
                .anyMatch(entry -> entry.kind() == Kind.FILE && entry.category() == Category.SPRING_BOOT_LOADER);
        if (!applicationContent || !packagedLibrary || !loaderContent) {
            throw failure(FailureCategory.UnsupportedStructure,
                    "archive does not contain the required Spring Boot executable structure");
        }
    }

    private static void requireManifest(Map<String, String> manifest, String name, String expected) {
        String actual = manifestValue(manifest, name);
        if (!expected.equals(actual)) {
            throw failure(FailureCategory.UnsupportedStructure, "manifest does not describe the supported executable structure");
        }
    }

    private static void requireNonBlankManifest(Map<String, String> manifest, String name) {
        String actual = manifestValue(manifest, name);
        if (actual == null || actual.isBlank()) {
            throw failure(FailureCategory.UnsupportedStructure, "manifest does not identify the application entry point");
        }
    }

    private static String manifestValue(Map<String, String> manifest, String name) {
        return manifest.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private static Category categorize(String path) {
        if ("META-INF/MANIFEST.MF".equals(path)) {
            return Category.MANIFEST;
        }
        if ("BOOT-INF/classpath.idx".equals(path)) {
            return Category.CLASSPATH_INDEX;
        }
        if ("BOOT-INF/layers.idx".equals(path)) {
            return Category.LAYERS_INDEX;
        }
        if (path.startsWith("BOOT-INF/lib/") && path.endsWith(".jar")) {
            return Category.PACKAGED_LIBRARY;
        }
        if (path.startsWith("org/springframework/boot/loader/")) {
            return Category.SPRING_BOOT_LOADER;
        }
        if (path.startsWith("BOOT-INF/classes/db/migration/") && path.endsWith(".sql")) {
            return Category.FLYWAY_MIGRATION;
        }
        if ("BOOT-INF/classes/db/migration/README.md".equals(path)) {
            return Category.MIGRATION_DOCUMENTATION;
        }
        if (path.startsWith("BOOT-INF/classes/")) {
            return Category.APPLICATION_CONTENT;
        }
        if (path.startsWith("META-INF/")) {
            return Category.ARCHIVE_METADATA;
        }
        return Category.OTHER;
    }

    private static String digest(Path archive) {
        MessageDigest digest = sha256();
        byte[] buffer = new byte[BUFFER_SIZE];
        try (InputStream input = Files.newInputStream(archive)) {
            int count;
            while ((count = input.read(buffer)) != -1) {
                digest.update(buffer, 0, count);
            }
        } catch (IOException unreadable) {
            throw failure(FailureCategory.InvalidArchive, "archive bytes cannot be read");
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException("Required SHA-256 implementation is unavailable", impossibleOnJava21);
        }
    }

    private static void requireCanonicalSha256(String value) {
        if (value == null || !SHA256.matcher(value).matches()) {
            throw new IllegalArgumentException("expectedSha256 must be exactly 64 lowercase hexadecimal characters");
        }
    }

    private static Failure failure(FailureCategory category, String message) {
        return new Failure(category, message);
    }

    record Limits(long maximumArchiveBytes, int maximumEntries,
            long maximumEntryUncompressedBytes, long maximumTotalUncompressedBytes,
            int maximumUtf8PathBytes, long maximumManifestBytes) {

        Limits {
            if (maximumArchiveBytes <= 0L || maximumEntries <= 0
                    || maximumEntryUncompressedBytes <= 0L || maximumTotalUncompressedBytes <= 0L
                    || maximumUtf8PathBytes <= 0 || maximumManifestBytes <= 0L) {
                throw new IllegalArgumentException("all inventory limits must be positive");
            }
            if (maximumManifestBytes > maximumEntryUncompressedBytes) {
                throw new IllegalArgumentException("manifest limit cannot exceed the per-entry limit");
            }
        }
    }

    record Inventory(String schemaVersion, String archiveFileName, long archiveByteSize,
            String archiveSha256, int entryCount, Map<String, String> manifestAttributes,
            List<Entry> entries) {

        Inventory {
            Objects.requireNonNull(schemaVersion, "schemaVersion");
            Objects.requireNonNull(archiveFileName, "archiveFileName");
            requireCanonicalSha256(archiveSha256);
            manifestAttributes = Collections.unmodifiableMap(new LinkedHashMap<>(manifestAttributes));
            entries = List.copyOf(entries);
            if (!SCHEMA_VERSION.equals(schemaVersion) || archiveFileName.isEmpty()
                    || archiveByteSize < 0L || entryCount != entries.size()) {
                throw new IllegalArgumentException("inventory model is inconsistent");
            }
        }

        byte[] canonicalOutput() {
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            var output = new StringBuilder();
            output.append(schemaVersion).append('\n');
            output.append("archive\t").append(encoded(encoder, archiveFileName)).append('\t')
                    .append(archiveByteSize).append('\t').append(archiveSha256).append('\t')
                    .append(entryCount).append('\n');
            manifestAttributes.forEach((name, value) -> output.append("manifest\t")
                    .append(encoded(encoder, name)).append('\t')
                    .append(encoded(encoder, value)).append('\n'));
            for (Entry entry : entries) {
                output.append("entry\t").append(encoded(encoder, entry.path())).append('\t')
                        .append(entry.kind()).append('\t').append(entry.category()).append('\t')
                        .append(entry.uncompressedSize()).append('\t').append(entry.compressedSize()).append('\t')
                        .append(String.format(Locale.ROOT, "%08x", entry.crc32())).append('\t')
                        .append(entry.compressionMethod()).append('\t')
                        .append(entry.sha256() == null ? "-" : entry.sha256()).append('\n');
            }
            return output.toString().getBytes(StandardCharsets.US_ASCII);
        }

        private static String encoded(Base64.Encoder encoder, String value) {
            return encoder.encodeToString(value.getBytes(StandardCharsets.UTF_8));
        }
    }

    record Entry(String path, Kind kind, Category category, long uncompressedSize,
            long compressedSize, long crc32, int compressionMethod, String sha256) {

        Entry {
            Objects.requireNonNull(path, "path");
            Objects.requireNonNull(kind, "kind");
            Objects.requireNonNull(category, "category");
            if (uncompressedSize < 0L || compressedSize < 0L || crc32 < 0L) {
                throw new IllegalArgumentException("entry metadata cannot be negative");
            }
            if (kind == Kind.FILE) {
                requireCanonicalSha256(sha256);
            } else if (sha256 != null) {
                throw new IllegalArgumentException("directory entries cannot have file digests");
            }
        }
    }

    enum Kind {
        FILE,
        DIRECTORY
    }

    enum Category {
        MANIFEST,
        APPLICATION_CONTENT,
        PACKAGED_LIBRARY,
        SPRING_BOOT_LOADER,
        CLASSPATH_INDEX,
        LAYERS_INDEX,
        FLYWAY_MIGRATION,
        MIGRATION_DOCUMENTATION,
        ARCHIVE_METADATA,
        OTHER
    }

    enum FailureCategory {
        MissingArchive,
        InvalidArchive,
        DuplicateEntry,
        AmbiguousPath,
        DigestMismatch,
        MalformedMetadata,
        UnsupportedStructure,
        LimitExceeded
    }

    static final class Failure extends RuntimeException {
        private final FailureCategory category;

        private Failure(FailureCategory category, String message) {
            super(category + ": " + message);
            this.category = Objects.requireNonNull(category, "category");
        }

        FailureCategory category() {
            return category;
        }
    }

    @FunctionalInterface
    interface SourceSnapshotObserver {
        void afterSourceOpened(Path source) throws IOException;
    }

    private record Snapshot(Path path, long size) {
    }

    private record ReadResult(long size, long crc32, String sha256, byte[] retainedBytes) {
    }

    private record Attribute(String name, String value) {
    }
}
