package com.enterprise.fashion.ecommerce.releasecompliance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** Test-scoped deterministic reconciliation of BEI-01 libraries with explicit dependency evidence. */
final class ExecutablePackagedJarReconciliation {

    static final String SCHEMA_VERSION = "bei02-packaged-jar-reconciliation-v1";
    private static final String RUNTIME_CONFIGURATION = "productionRuntimeClasspath";
    private static final Pattern SHA256 = Pattern.compile("[0-9a-f]{64}");
    private static final Pattern COORDINATE_PART = Pattern.compile("[^:\s=,]+", Pattern.UNICODE_CHARACTER_CLASS);
    private static final Comparator<String> UTF8_ORDER = (left, right) ->
            java.util.Arrays.compareUnsigned(left.getBytes(StandardCharsets.UTF_8),
                    right.getBytes(StandardCharsets.UTF_8));

    private ExecutablePackagedJarReconciliation() {
    }

    static Reconciliation reconcile(ExecutableArchiveInventory.Inventory inventory,
            byte[] lockfileBytes, byte[] verificationMetadataBytes,
            ResolvedGraph resolvedGraph, ClassificationPolicy policy,
            byte[] loaderToolsSourceBytes, Limits limits) {
        requireInput(inventory, "inventory");
        requireInput(lockfileBytes, "lockfileBytes");
        requireInput(verificationMetadataBytes, "verificationMetadataBytes");
        requireInput(resolvedGraph, "resolvedGraph");
        requireInput(policy, "policy");
        requireInput(loaderToolsSourceBytes, "loaderToolsSourceBytes");
        requireInput(limits, "limits");
        if (lockfileBytes.length > limits.maximumLockfileBytes()
                || verificationMetadataBytes.length > limits.maximumMetadataBytes()
                || loaderToolsSourceBytes.length > limits.maximumSourceArchiveBytes()) {
            throw failure(FailureCategory.LimitExceeded, "an evidence input exceeds its configured byte limit");
        }

        byte[] lockSnapshot = lockfileBytes.clone();
        byte[] metadataSnapshot = verificationMetadataBytes.clone();
        byte[] sourceSnapshot = loaderToolsSourceBytes.clone();
        String lockfileSha256 = sha256(lockSnapshot);
        String metadataSha256 = sha256(metadataSnapshot);
        Set<Coordinate> lockedCoordinates = parseLockfile(lockSnapshot, limits);
        VerificationEvidence verification = parseVerificationMetadata(metadataSnapshot, limits);

        Map<Coordinate, ResolvedComponent> graph = resolvedGraph.byCoordinate();
        if (!lockedCoordinates.equals(graph.keySet())) {
            throw failure(FailureCategory.InconsistentClassification,
                    "resolved graph coordinates do not equal locked runtime coordinates");
        }
        validatePolicy(policy, graph);
        validateResolvedChecksums(graph, verification);

        List<ExecutableArchiveInventory.Entry> packaged = inventory.entries().stream()
                .filter(entry -> entry.category() == ExecutableArchiveInventory.Category.PACKAGED_LIBRARY)
                .sorted(Comparator.comparing(ExecutableArchiveInventory.Entry::path, UTF8_ORDER))
                .toList();
        var componentResults = new ArrayList<ComponentResult>();
        var buildAddedResults = new ArrayList<BuildAddedResult>();
        var matchedCoordinates = new HashSet<Coordinate>();
        var usedBuildRules = new HashSet<String>();
        var physicalPaths = new HashSet<String>();

        for (ExecutableArchiveInventory.Entry entry : packaged) {
            if (!physicalPaths.add(entry.path())) {
                throw failure(FailureCategory.DuplicatePackagedComponent,
                        "a packaged path occurs more than once");
            }
            List<ResolvedComponent> candidates = policy.expectedPackagedCoordinates().stream()
                    .map(graph::get)
                    .filter(Objects::nonNull)
                    .filter(component -> entry.sha256().equals(component.artifactSha256()))
                    .filter(component -> verification.containsChecksum(
                            component.coordinate(), component.artifactName(), component.artifactSha256()))
                    .toList();
            if (candidates.size() > 1) {
                throw failure(FailureCategory.AmbiguousComponentMatch,
                        "a packaged digest identifies multiple resolved coordinates");
            }
            if (candidates.size() == 1) {
                ResolvedComponent component = candidates.getFirst();
                if (!entry.path().equals("BOOT-INF/lib/" + component.artifactName())) {
                    throw failure(FailureCategory.DigestMismatch,
                            "packaged filename is inconsistent with the digest-identified artifact");
                }
                if (!matchedCoordinates.add(component.coordinate())) {
                    throw failure(FailureCategory.DuplicatePackagedComponent,
                            "multiple physical entries map to one resolved coordinate");
                }
                componentResults.add(ComponentResult.packaged(component, entry.path()));
                continue;
            }

            BuildAddedRule rule = policy.buildAddedRulesByPath().get(entry.path());
            if (rule == null) {
                boolean filenameOnly = policy.expectedPackagedCoordinates().stream()
                        .map(graph::get).filter(Objects::nonNull)
                        .anyMatch(component -> entry.path().equals("BOOT-INF/lib/" + component.artifactName()));
                throw failure(filenameOnly ? FailureCategory.DigestMismatch
                                : FailureCategory.UnmatchedPackagedComponent,
                        "a physical packaged component has no exact verified identity");
            }
            if (!usedBuildRules.add(rule.packagedPath())) {
                throw failure(FailureCategory.DuplicatePackagedComponent,
                        "a build-added rule is used more than once");
            }
            buildAddedResults.add(verifyBuildAdded(entry, rule, sourceSnapshot,
                    lockedCoordinates, verification, limits));
        }

        for (Coordinate coordinate : policy.expectedPackagedCoordinates()) {
            if (!matchedCoordinates.contains(coordinate)) {
                throw failure(FailureCategory.MissingExpectedPackagedComponent,
                        "an expected packaged coordinate has no physical match");
            }
        }
        for (Coordinate coordinate : sorted(policy.permittedOmittedCoordinates())) {
            componentResults.add(ComponentResult.nonPackaged(graph.get(coordinate),
                    Classification.RESOLVED_NOT_PACKAGED));
        }
        for (Coordinate coordinate : sorted(policy.permittedPlatformCoordinates())) {
            componentResults.add(ComponentResult.nonPackaged(graph.get(coordinate),
                    Classification.DEPENDENCY_MANAGEMENT_METADATA));
        }
        if (usedBuildRules.size() != policy.buildAddedRules().size()) {
            throw failure(FailureCategory.UnknownBuildAddedComponent,
                    "an expected build-added component is absent");
        }

        componentResults.sort(Comparator.comparing(result -> result.coordinate().value(), UTF8_ORDER));
        buildAddedResults.sort(Comparator.comparing(BuildAddedResult::packagedPath, UTF8_ORDER));
        EnumMap<Classification, Integer> counts = classificationCounts(componentResults, buildAddedResults);
        validateCompleteness(policy.cardinalities(), graph.size(), packaged.size(), counts);

        String inventorySha256 = sha256(inventory.canonicalOutput());
        String graphSha256 = resolvedGraph.sha256();
        String policySha256 = policy.sha256();
        Reconciliation preliminary = new Reconciliation(SCHEMA_VERSION, inventory.archiveSha256(),
                inventorySha256, lockfileSha256, metadataSha256, graphSha256, policySha256,
                graph.size(), packaged.size(), counts, componentResults, buildAddedResults, null);
        byte[] canonical = preliminary.canonicalPayload();
        return preliminary.withReportSha256(sha256(canonical));
    }

    private static void validateResolvedChecksums(Map<Coordinate, ResolvedComponent> graph,
            VerificationEvidence verification) {
        for (ResolvedComponent component : graph.values()) {
            if (component.kind() == ComponentKind.JAR
                    && !verification.containsChecksum(component.coordinate(), component.artifactName(),
                            component.artifactSha256())) {
                if (!verification.hasArtifact(component.coordinate(), component.artifactName())) {
                    throw failure(FailureCategory.MissingChecksumEvidence,
                            "resolved artifact has no matching verification-metadata artifact");
                }
                throw failure(FailureCategory.DigestMismatch,
                        "resolved artifact digest is not admitted by verification metadata");
            }
        }
    }

    private static BuildAddedResult verifyBuildAdded(ExecutableArchiveInventory.Entry packaged,
            BuildAddedRule rule, byte[] sourceBytes, Set<Coordinate> locked,
            VerificationEvidence verification, Limits limits) {
        if (locked.contains(rule.sourceCoordinate())) {
            throw failure(FailureCategory.InvalidBuildAddedOrigin,
                    "build-added source coordinate is part of the application runtime lock");
        }
        String sourceSha256 = sha256(sourceBytes);
        if (!sourceSha256.equals(rule.sourceArtifactSha256())) {
            throw failure(FailureCategory.DigestMismatch,
                    "build-added source-container digest does not match policy");
        }
        if (!verification.containsChecksum(rule.sourceCoordinate(), rule.sourceArtifactName(), sourceSha256)) {
            throw failure(verification.hasArtifact(rule.sourceCoordinate(), rule.sourceArtifactName())
                            ? FailureCategory.DigestMismatch : FailureCategory.MissingChecksumEvidence,
                    "build-added source artifact lacks exact checksum evidence");
        }
        byte[] nested = readUniqueNestedResource(sourceBytes, rule.nestedResourcePath(), limits);
        String nestedSha256 = sha256(nested);
        if (!nestedSha256.equals(rule.nestedResourceSha256())
                || !nestedSha256.equals(packaged.sha256())) {
            throw failure(FailureCategory.DigestMismatch,
                    "build-added nested and packaged digests are inconsistent");
        }
        return new BuildAddedResult(packaged.path(), packaged.sha256(), rule.sourceCoordinate(),
                rule.sourceArtifactName(), sourceSha256, rule.nestedResourcePath(), nestedSha256, true);
    }

    private static byte[] readUniqueNestedResource(byte[] sourceBytes, String expectedPath, Limits limits) {
        byte[] found = null;
        int entries = 0;
        long total = 0L;
        try (var input = new ZipInputStream(new ByteArrayInputStream(sourceBytes), StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                if (++entries > limits.maximumSourceEntries()) {
                    throw failure(FailureCategory.LimitExceeded, "source-container entry limit exceeded");
                }
                long entryBytes = 0L;
                var retained = entry.getName().equals(expectedPath) ? new ByteArrayOutputStream() : null;
                byte[] buffer = new byte[8192];
                int count;
                while ((count = input.read(buffer)) != -1) {
                    entryBytes += count;
                    total += count;
                    if (entryBytes > limits.maximumSourceEntryBytes()
                            || total > limits.maximumSourceExpandedBytes()) {
                        throw failure(FailureCategory.LimitExceeded,
                                "source-container expanded byte limit exceeded");
                    }
                    if (retained != null) {
                        retained.write(buffer, 0, count);
                    }
                }
                if (retained != null) {
                    if (found != null) {
                        throw failure(FailureCategory.InvalidBuildAddedOrigin,
                                "nested build-added resource occurs more than once");
                    }
                    found = retained.toByteArray();
                }
                input.closeEntry();
            }
        } catch (Failure known) {
            throw known;
        } catch (ZipException malformed) {
            throw failure(FailureCategory.InvalidBuildAddedOrigin,
                    "build-added source container is not a valid ZIP archive");
        } catch (IOException | IllegalArgumentException malformed) {
            throw failure(FailureCategory.InvalidBuildAddedOrigin,
                    "build-added source container cannot be inspected safely");
        }
        if (found == null) {
            throw failure(FailureCategory.InvalidBuildAddedOrigin,
                    "nested build-added resource is missing");
        }
        return found;
    }

    private static Set<Coordinate> parseLockfile(byte[] bytes, Limits limits) {
        String text = decodeUtf8(bytes, FailureCategory.MalformedLockfile);
        var coordinates = new LinkedHashSet<Coordinate>();
        var versions = new HashMap<String, String>();
        int rows = 0;
        for (String line : text.split("\\R", -1)) {
            if (++rows > limits.maximumLockfileRows()) {
                throw failure(FailureCategory.LimitExceeded, "lockfile row limit exceeded");
            }
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            int equals = trimmed.indexOf('=');
            if (equals < 0) {
                if (trimmed.contains(RUNTIME_CONFIGURATION)) {
                    throw failure(FailureCategory.MalformedLockfile, "malformed relevant lockfile row");
                }
                continue;
            }
            String configurations = trimmed.substring(equals + 1);
            boolean relevant = java.util.Arrays.stream(configurations.split(",", -1))
                    .anyMatch(RUNTIME_CONFIGURATION::equals);
            if (!relevant) {
                continue;
            }
            Coordinate coordinate = Coordinate.parse(trimmed.substring(0, equals),
                    FailureCategory.MalformedLockfile);
            if (!coordinates.add(coordinate)) {
                throw failure(FailureCategory.DuplicateCoordinate,
                        "locked runtime coordinate occurs more than once");
            }
            String module = coordinate.group() + ":" + coordinate.artifact();
            String prior = versions.putIfAbsent(module, coordinate.version());
            if (prior != null && !prior.equals(coordinate.version())) {
                throw failure(FailureCategory.DuplicateCoordinate,
                        "locked runtime module has conflicting versions");
            }
        }
        if (coordinates.isEmpty()) {
            throw failure(FailureCategory.MalformedLockfile,
                    "lockfile contains no production runtime coordinates");
        }
        return Collections.unmodifiableSet(coordinates);
    }

    private static VerificationEvidence parseVerificationMetadata(byte[] bytes, Limits limits) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            var builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
                throw new SAXException("external entities are prohibited");
            });
            var document = builder.parse(new InputSource(new ByteArrayInputStream(bytes)));
            Element root = document.getDocumentElement();
            if (root == null || !"verification-metadata".equals(root.getLocalName())) {
                throw failure(FailureCategory.MalformedVerificationMetadata,
                        "unsupported verification metadata root");
            }
            var evidence = new LinkedHashMap<Coordinate, Map<String, Set<String>>>();
            var nodes = document.getElementsByTagNameNS("*", "component");
            if (nodes.getLength() > limits.maximumMetadataComponents()) {
                throw failure(FailureCategory.LimitExceeded, "verification component limit exceeded");
            }
            for (int index = 0; index < nodes.getLength(); index++) {
                Element component = (Element) nodes.item(index);
                Coordinate coordinate = new Coordinate(requiredAttribute(component, "group"),
                        requiredAttribute(component, "name"), requiredAttribute(component, "version"));
                if (evidence.containsKey(coordinate)) {
                    throw failure(FailureCategory.MalformedVerificationMetadata,
                            "verification metadata repeats a component definition");
                }
                var artifacts = new LinkedHashMap<String, Set<String>>();
                for (Element artifact : childElements(component, "artifact")) {
                    String name = requiredAttribute(artifact, "name");
                    var checksums = new LinkedHashSet<String>();
                    for (Element checksum : childElements(artifact, "sha256")) {
                        String value = requiredAttribute(checksum, "value");
                        requireSha256(value, FailureCategory.MalformedVerificationMetadata);
                        checksums.add(value);
                    }
                    if (checksums.isEmpty()) {
                        throw failure(FailureCategory.MalformedVerificationMetadata,
                                "verification artifact has no SHA-256 evidence");
                    }
                    if (artifacts.putIfAbsent(name, Set.copyOf(checksums)) != null) {
                        throw failure(FailureCategory.MalformedVerificationMetadata,
                                "verification metadata repeats an artifact definition");
                    }
                }
                evidence.put(coordinate, Map.copyOf(artifacts));
            }
            return new VerificationEvidence(evidence);
        } catch (Failure known) {
            throw known;
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException malformed) {
            throw failure(FailureCategory.MalformedVerificationMetadata,
                    "verification metadata cannot be parsed safely");
        }
    }

    private static List<Element> childElements(Element parent, String localName) {
        var result = new ArrayList<Element>();
        var children = parent.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            if (children.item(index) instanceof Element child && localName.equals(child.getLocalName())) {
                result.add(child);
            }
        }
        return result;
    }

    private static String requiredAttribute(Element element, String name) {
        String value = element.getAttribute(name);
        if (value == null || value.isBlank()) {
            throw failure(FailureCategory.MalformedVerificationMetadata,
                    "verification metadata has a missing attribute");
        }
        return value;
    }

    private static void validatePolicy(ClassificationPolicy policy,
            Map<Coordinate, ResolvedComponent> graph) {
        var all = new HashSet<Coordinate>();
        addDisjoint(all, policy.expectedPackagedCoordinates());
        addDisjoint(all, policy.permittedOmittedCoordinates());
        addDisjoint(all, policy.permittedPlatformCoordinates());
        if (!all.equals(graph.keySet())) {
            throw failure(FailureCategory.InconsistentClassification,
                    "classification policy does not partition the resolved graph");
        }
        for (Coordinate coordinate : policy.expectedPackagedCoordinates()) {
            requireKind(graph.get(coordinate), ComponentKind.JAR);
        }
        for (Coordinate coordinate : policy.permittedOmittedCoordinates()) {
            requireKind(graph.get(coordinate), ComponentKind.JAR);
        }
        for (Coordinate coordinate : policy.permittedPlatformCoordinates()) {
            requireKind(graph.get(coordinate), ComponentKind.PLATFORM);
        }
    }

    private static void addDisjoint(Set<Coordinate> destination, Set<Coordinate> values) {
        for (Coordinate value : values) {
            if (!destination.add(value)) {
                throw failure(FailureCategory.InconsistentClassification,
                        "classification policy sets overlap");
            }
        }
    }

    private static void requireKind(ResolvedComponent component, ComponentKind expected) {
        if (component == null || component.kind() != expected) {
            throw failure(FailureCategory.InconsistentClassification,
                    "resolved component kind conflicts with classification policy");
        }
    }

    private static EnumMap<Classification, Integer> classificationCounts(
            List<ComponentResult> components, List<BuildAddedResult> additions) {
        var counts = new EnumMap<Classification, Integer>(Classification.class);
        for (Classification classification : Classification.values()) {
            counts.put(classification, 0);
        }
        for (ComponentResult component : components) {
            counts.compute(component.classification(), (ignored, count) -> count + 1);
        }
        counts.put(Classification.BUILD_ADDED, additions.size());
        return counts;
    }

    private static void validateCompleteness(Cardinalities expected, int resolved, int physical,
            Map<Classification, Integer> counts) {
        int packaged = counts.get(Classification.PACKAGED);
        int omitted = counts.get(Classification.RESOLVED_NOT_PACKAGED);
        int platforms = counts.get(Classification.DEPENDENCY_MANAGEMENT_METADATA);
        int added = counts.get(Classification.BUILD_ADDED);
        if (resolved != packaged + omitted + platforms || physical != packaged + added) {
            throw failure(FailureCategory.IncompleteReconciliation,
                    "classification arithmetic is incomplete");
        }
        if (resolved != expected.resolvedCoordinates() || physical != expected.physicalPackagedLibraries()
                || packaged != expected.packagedLocked() || omitted != expected.resolvedNotPackaged()
                || platforms != expected.platformMetadata() || added != expected.buildAdded()) {
            throw failure(FailureCategory.InconsistentClassification,
                    "classification cardinalities do not match policy");
        }
    }

    private static String decodeUtf8(byte[] bytes, FailureCategory category) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException invalid) {
            throw failure(category, "evidence is not valid UTF-8");
        }
    }

    private static String sha256(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException("SHA-256 is unavailable", impossibleOnJava21);
        }
    }

    private static void requireSha256(String value, FailureCategory category) {
        if (value == null || !SHA256.matcher(value).matches()) {
            throw failure(category, "SHA-256 evidence is malformed");
        }
    }

    private static <T> T requireInput(T value, String name) {
        if (value == null) {
            throw failure(FailureCategory.MissingInput, name + " is required");
        }
        return value;
    }

    private static Failure failure(FailureCategory category, String message) {
        return new Failure(category, message);
    }

    private static List<Coordinate> sorted(Set<Coordinate> values) {
        return values.stream().sorted(Comparator.comparing(Coordinate::value, UTF8_ORDER)).toList();
    }

    record Coordinate(String group, String artifact, String version) {
        Coordinate {
            if (!validPart(group) || !validPart(artifact) || !validPart(version)) {
                throw failure(FailureCategory.InconsistentClassification, "coordinate identity is malformed");
            }
        }

        static Coordinate parse(String value, FailureCategory category) {
            String[] parts = value.split(":", -1);
            if (parts.length != 3 || !validPart(parts[0]) || !validPart(parts[1]) || !validPart(parts[2])) {
                throw failure(category, "coordinate identity is malformed");
            }
            return new Coordinate(parts[0], parts[1], parts[2]);
        }

        String value() {
            return group + ":" + artifact + ":" + version;
        }

        private static boolean validPart(String value) {
            return value != null && COORDINATE_PART.matcher(value).matches();
        }
    }

    enum ComponentKind { JAR, PLATFORM }

    enum Classification {
        PACKAGED,
        RESOLVED_NOT_PACKAGED,
        DEPENDENCY_MANAGEMENT_METADATA,
        BUILD_ADDED
    }

    record ResolvedComponent(Coordinate coordinate, ComponentKind kind,
            String artifactName, String artifactSha256) {
        ResolvedComponent {
            requireInput(coordinate, "coordinate");
            requireInput(kind, "kind");
            if (kind == ComponentKind.JAR) {
                if (artifactName == null || artifactName.isBlank()) {
                    throw failure(FailureCategory.InconsistentClassification, "JAR artifact name is required");
                }
                requireSha256(artifactSha256, FailureCategory.InconsistentClassification);
            } else if (artifactName != null || artifactSha256 != null) {
                throw failure(FailureCategory.InconsistentClassification,
                        "platform components cannot expose JAR artifact identity");
            }
        }
    }

    record ResolvedGraph(String schemaVersion, List<ResolvedComponent> components) {
        ResolvedGraph {
            requireInput(schemaVersion, "resolvedGraph.schemaVersion");
            components = List.copyOf(requireInput(components, "resolvedGraph.components"));
            var seen = new HashSet<Coordinate>();
            for (ResolvedComponent component : components) {
                if (!seen.add(component.coordinate())) {
                    throw failure(FailureCategory.DuplicateCoordinate,
                            "resolved graph repeats a coordinate");
                }
            }
        }

        Map<Coordinate, ResolvedComponent> byCoordinate() {
            var result = new LinkedHashMap<Coordinate, ResolvedComponent>();
            components.stream().sorted(Comparator.comparing(c -> c.coordinate().value(), UTF8_ORDER))
                    .forEach(component -> result.put(component.coordinate(), component));
            return Collections.unmodifiableMap(result);
        }

        String sha256() {
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            var output = new StringBuilder();
            appendCanonical(output, encoder, "schema", schemaVersion);
            byCoordinate().values().forEach(component -> appendCanonical(output, encoder, "component",
                    component.coordinate().value(), component.kind().name(),
                    component.artifactName() == null ? "-" : component.artifactName(),
                    component.artifactSha256() == null ? "-" : component.artifactSha256()));
            return ExecutablePackagedJarReconciliation.sha256(output.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    record BuildAddedRule(String packagedPath, Coordinate sourceCoordinate,
            String sourceArtifactName, String sourceArtifactSha256,
            String nestedResourcePath, String nestedResourceSha256) {
        BuildAddedRule {
            if (packagedPath == null || !packagedPath.startsWith("BOOT-INF/lib/") || packagedPath.endsWith("/")) {
                throw failure(FailureCategory.InconsistentClassification, "build-added packaged path is invalid");
            }
            requireInput(sourceCoordinate, "buildAdded.sourceCoordinate");
            if (sourceArtifactName == null || sourceArtifactName.isBlank()
                    || nestedResourcePath == null || nestedResourcePath.isBlank()) {
                throw failure(FailureCategory.InconsistentClassification,
                        "build-added artifact and nested-resource identities are required");
            }
            requireSha256(sourceArtifactSha256, FailureCategory.InconsistentClassification);
            requireSha256(nestedResourceSha256, FailureCategory.InconsistentClassification);
        }
    }

    record Cardinalities(int resolvedCoordinates, int packagedLocked,
            int resolvedNotPackaged, int platformMetadata, int buildAdded,
            int physicalPackagedLibraries) {
        Cardinalities {
            if (resolvedCoordinates < 0 || packagedLocked < 0 || resolvedNotPackaged < 0
                    || platformMetadata < 0 || buildAdded < 0 || physicalPackagedLibraries < 0
                    || resolvedCoordinates != packagedLocked + resolvedNotPackaged + platformMetadata
                    || physicalPackagedLibraries != packagedLocked + buildAdded) {
                throw failure(FailureCategory.InconsistentClassification,
                        "expected cardinalities are internally inconsistent");
            }
        }
    }

    record ClassificationPolicy(String schemaVersion, Set<Coordinate> expectedPackagedCoordinates,
            Set<Coordinate> permittedOmittedCoordinates, Set<Coordinate> permittedPlatformCoordinates,
            List<BuildAddedRule> buildAddedRules, Cardinalities cardinalities) {
        ClassificationPolicy {
            requireInput(schemaVersion, "policy.schemaVersion");
            expectedPackagedCoordinates = Set.copyOf(requireInput(expectedPackagedCoordinates,
                    "policy.expectedPackagedCoordinates"));
            permittedOmittedCoordinates = Set.copyOf(requireInput(permittedOmittedCoordinates,
                    "policy.permittedOmittedCoordinates"));
            permittedPlatformCoordinates = Set.copyOf(requireInput(permittedPlatformCoordinates,
                    "policy.permittedPlatformCoordinates"));
            buildAddedRules = List.copyOf(requireInput(buildAddedRules, "policy.buildAddedRules"));
            requireInput(cardinalities, "policy.cardinalities");
            var paths = new HashSet<String>();
            for (BuildAddedRule rule : buildAddedRules) {
                if (!paths.add(rule.packagedPath())) {
                    throw failure(FailureCategory.InconsistentClassification,
                            "build-added rules repeat a packaged path");
                }
            }
        }

        Map<String, BuildAddedRule> buildAddedRulesByPath() {
            var result = new LinkedHashMap<String, BuildAddedRule>();
            buildAddedRules.stream().sorted(Comparator.comparing(BuildAddedRule::packagedPath, UTF8_ORDER))
                    .forEach(rule -> result.put(rule.packagedPath(), rule));
            return Collections.unmodifiableMap(result);
        }

        String sha256() {
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            var output = new StringBuilder();
            appendCanonical(output, encoder, "schema", schemaVersion);
            appendCoordinates(output, encoder, "packaged", expectedPackagedCoordinates);
            appendCoordinates(output, encoder, "omitted", permittedOmittedCoordinates);
            appendCoordinates(output, encoder, "platform", permittedPlatformCoordinates);
            buildAddedRules.stream().sorted(Comparator.comparing(BuildAddedRule::packagedPath, UTF8_ORDER))
                    .forEach(rule -> appendCanonical(output, encoder, "added", rule.packagedPath(),
                            rule.sourceCoordinate().value(), rule.sourceArtifactName(),
                            rule.sourceArtifactSha256(), rule.nestedResourcePath(), rule.nestedResourceSha256()));
            appendCanonical(output, encoder, "counts",
                    Integer.toString(cardinalities.resolvedCoordinates()),
                    Integer.toString(cardinalities.packagedLocked()),
                    Integer.toString(cardinalities.resolvedNotPackaged()),
                    Integer.toString(cardinalities.platformMetadata()),
                    Integer.toString(cardinalities.buildAdded()),
                    Integer.toString(cardinalities.physicalPackagedLibraries()));
            return ExecutablePackagedJarReconciliation.sha256(output.toString().getBytes(StandardCharsets.UTF_8));
        }

        private static void appendCoordinates(StringBuilder output, Base64.Encoder encoder,
                String label, Set<Coordinate> values) {
            sorted(values).forEach(value -> appendCanonical(output, encoder, label, value.value()));
        }
    }

    private static void appendCanonical(StringBuilder output, Base64.Encoder encoder,
            String label, String... values) {
        output.append(label);
        for (String value : values) {
            output.append('\t').append(encoder.encodeToString(value.getBytes(StandardCharsets.UTF_8)));
        }
        output.append('\n');
    }

    record Limits(int maximumLockfileBytes, int maximumLockfileRows,
            int maximumMetadataBytes, int maximumMetadataComponents,
            int maximumSourceArchiveBytes, int maximumSourceEntries,
            long maximumSourceEntryBytes, long maximumSourceExpandedBytes) {
        Limits {
            if (maximumLockfileBytes <= 0 || maximumLockfileRows <= 0 || maximumMetadataBytes <= 0
                    || maximumMetadataComponents <= 0 || maximumSourceArchiveBytes <= 0
                    || maximumSourceEntries <= 0 || maximumSourceEntryBytes <= 0
                    || maximumSourceExpandedBytes <= 0) {
                throw failure(FailureCategory.InconsistentClassification,
                        "all reconciliation limits must be positive");
            }
        }
    }

    record ComponentResult(Coordinate coordinate, Classification classification,
            ComponentKind componentKind, String artifactName, String artifactSha256,
            String packagedPath, String verificationDisposition) {
        ComponentResult {
            requireInput(coordinate, "componentResult.coordinate");
            requireInput(classification, "componentResult.classification");
            requireInput(componentKind, "componentResult.componentKind");
            requireInput(verificationDisposition, "componentResult.verificationDisposition");
        }

        static ComponentResult packaged(ResolvedComponent component, String path) {
            return new ComponentResult(component.coordinate(), Classification.PACKAGED, component.kind(),
                    component.artifactName(), component.artifactSha256(), path, "EXACT_SHA256_MATCH");
        }

        static ComponentResult nonPackaged(ResolvedComponent component, Classification classification) {
            return new ComponentResult(component.coordinate(), classification, component.kind(),
                    component.artifactName(), component.artifactSha256(), null,
                    component.kind() == ComponentKind.JAR ? "EXACT_SHA256_EVIDENCE" : "NOT_APPLICABLE");
        }
    }

    record BuildAddedResult(String packagedPath, String packagedSha256,
            Coordinate sourceCoordinate, String sourceArtifactName, String sourceArtifactSha256,
            String nestedResourcePath, String nestedResourceSha256, boolean nestedToPackagedDigestEqual) {
    }

    record Reconciliation(String schemaVersion, String executableSha256,
            String inventorySha256, String lockfileSha256, String verificationMetadataSha256,
            String resolvedGraphSha256, String classificationPolicySha256,
            int resolvedCoordinateCount, int physicalPackagedLibraryCount,
            Map<Classification, Integer> classificationCounts,
            List<ComponentResult> componentResults, List<BuildAddedResult> buildAddedResults,
            String reportSha256) {
        Reconciliation {
            classificationCounts = Collections.unmodifiableMap(new EnumMap<>(classificationCounts));
            componentResults = List.copyOf(componentResults);
            buildAddedResults = List.copyOf(buildAddedResults);
            for (String digest : List.of(executableSha256, inventorySha256, lockfileSha256,
                    verificationMetadataSha256, resolvedGraphSha256, classificationPolicySha256)) {
                requireSha256(digest, FailureCategory.IncompleteReconciliation);
            }
            if (reportSha256 != null) {
                requireSha256(reportSha256, FailureCategory.IncompleteReconciliation);
            }
        }

        byte[] canonicalOutput() {
            if (reportSha256 == null) {
                throw new IllegalStateException("successful reconciliation report digest is unavailable");
            }
            return canonicalPayload().clone();
        }

        private Reconciliation withReportSha256(String value) {
            return new Reconciliation(schemaVersion, executableSha256, inventorySha256, lockfileSha256,
                    verificationMetadataSha256, resolvedGraphSha256, classificationPolicySha256,
                    resolvedCoordinateCount, physicalPackagedLibraryCount, classificationCounts,
                    componentResults, buildAddedResults, value);
        }

        private byte[] canonicalPayload() {
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            var output = new StringBuilder();
            append(output, encoder, "schema", schemaVersion);
            append(output, encoder, "executable", executableSha256);
            append(output, encoder, "inventory", inventorySha256);
            append(output, encoder, "lockfile", lockfileSha256);
            append(output, encoder, "verification", verificationMetadataSha256);
            append(output, encoder, "graph", resolvedGraphSha256);
            append(output, encoder, "policy", classificationPolicySha256);
            output.append("counts\t").append(resolvedCoordinateCount).append('\t')
                    .append(physicalPackagedLibraryCount);
            for (Classification classification : Classification.values()) {
                output.append('\t').append(classification).append('=')
                        .append(classificationCounts.get(classification));
            }
            output.append('\n');
            for (ComponentResult result : componentResults) {
                output.append("component");
                appendValue(output, encoder, result.coordinate().value());
                appendValue(output, encoder, result.classification().name());
                appendValue(output, encoder, result.componentKind().name());
                appendValue(output, encoder, nullable(result.artifactName()));
                appendValue(output, encoder, nullable(result.artifactSha256()));
                appendValue(output, encoder, nullable(result.packagedPath()));
                appendValue(output, encoder, result.verificationDisposition());
                output.append('\n');
            }
            for (BuildAddedResult result : buildAddedResults) {
                output.append("build-added");
                appendValue(output, encoder, result.packagedPath());
                appendValue(output, encoder, result.packagedSha256());
                appendValue(output, encoder, result.sourceCoordinate().value());
                appendValue(output, encoder, result.sourceArtifactName());
                appendValue(output, encoder, result.sourceArtifactSha256());
                appendValue(output, encoder, result.nestedResourcePath());
                appendValue(output, encoder, result.nestedResourceSha256());
                appendValue(output, encoder, Boolean.toString(result.nestedToPackagedDigestEqual()));
                output.append('\n');
            }
            return output.toString().getBytes(StandardCharsets.UTF_8);
        }

        private static void append(StringBuilder output, Base64.Encoder encoder,
                String label, String value) {
            output.append(label);
            appendValue(output, encoder, value);
            output.append('\n');
        }

        private static void appendValue(StringBuilder output, Base64.Encoder encoder, String value) {
            output.append('\t').append(encoder.encodeToString(value.getBytes(StandardCharsets.UTF_8)));
        }

        private static String nullable(String value) {
            return value == null ? "-" : value;
        }
    }

    private record VerificationEvidence(Map<Coordinate, Map<String, Set<String>>> values) {
        private VerificationEvidence {
            var immutable = new LinkedHashMap<Coordinate, Map<String, Set<String>>>();
            values.forEach((coordinate, artifacts) -> immutable.put(coordinate, Map.copyOf(artifacts)));
            values = Map.copyOf(immutable);
        }

        boolean hasArtifact(Coordinate coordinate, String artifact) {
            return values.getOrDefault(coordinate, Map.of()).containsKey(artifact);
        }

        boolean containsChecksum(Coordinate coordinate, String artifact, String checksum) {
            return values.getOrDefault(coordinate, Map.of())
                    .getOrDefault(artifact, Set.of()).contains(checksum);
        }
    }

    enum FailureCategory {
        MissingInput,
        MalformedLockfile,
        MalformedVerificationMetadata,
        DuplicateCoordinate,
        DuplicatePackagedComponent,
        MissingChecksumEvidence,
        DigestMismatch,
        AmbiguousComponentMatch,
        UnmatchedPackagedComponent,
        MissingExpectedPackagedComponent,
        UnknownBuildAddedComponent,
        InvalidBuildAddedOrigin,
        InconsistentClassification,
        IncompleteReconciliation,
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
}
