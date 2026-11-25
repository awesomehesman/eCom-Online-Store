package com.enterprise.fashion.ecommerce.releasecompliance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExecutableReleaseEvidenceTest {
    private static final String HASH = "a".repeat(64);

    @Test
    void accountsForExpectedResourcesAndBlocksSensitiveOrPendingConfiguration() {
        assertThat(ExecutableReleaseEvidence.inspect(inventory(null)).ready()).isTrue();
        var local = ExecutableReleaseEvidence.inspect(inventory("BOOT-INF/classes/application-local.properties"));
        assertThat(local.ready()).isFalse();
        assertThat(local.entries()).anyMatch(row ->
                row.status() == ExecutableReleaseEvidence.ResourceStatus.DISPOSITION_REQUIRED);
        var sensitive = ExecutableReleaseEvidence.inspect(inventory("BOOT-INF/classes/private.p12"));
        assertThat(sensitive.ready()).isFalse();
        assertThat(sensitive.entries()).anyMatch(row ->
                row.status() == ExecutableReleaseEvidence.ResourceStatus.PROHIBITED);
        assertThatThrownBy(() -> local.entries().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void bindsNoticeObservationsToPhysicalDigestsAndKeepsMissingNoticeExplicit() {
        String path = "BOOT-INF/lib/jarmode.jar";
        var base = inventory(null);
        var entries = new ArrayList<>(base.entries());
        entries.add(entry(path, ExecutableArchiveInventory.Category.PACKAGED_LIBRARY));
        var inventory = new ExecutableArchiveInventory.Inventory(base.schemaVersion(), base.archiveFileName(),
                base.archiveByteSize(), base.archiveSha256(), entries.size(), base.manifestAttributes(), entries);
        var license = new ExecutableReleaseEvidence.NoticeInput(path, HASH,
                ExecutableReleaseEvidence.NoticeKind.LICENSE, "META-INF/LICENSE", HASH);
        var notice = new ExecutableReleaseEvidence.NoticeInput(path, HASH,
                ExecutableReleaseEvidence.NoticeKind.NOTICE, "META-INF/NOTICE", HASH);
        var required = java.util.Set.of(path);
        assertThat(ExecutableReleaseEvidence.noticeCoverage(inventory, List.of(), required).missing()).hasSize(2);
        assertThat(ExecutableReleaseEvidence.noticeCoverage(inventory, List.of(license), required).missing())
                .containsExactly(path + " missing required NOTICE evidence");
        var complete = ExecutableReleaseEvidence.noticeCoverage(inventory, List.of(license, notice), required);
        assertThat(complete.missing()).isEmpty();
        assertThat(complete).isEqualTo(ExecutableReleaseEvidence.noticeCoverage(
                inventory, List.of(notice, license), required));
        assertThatThrownBy(() -> ExecutableReleaseEvidence.noticeCoverage(
                inventory, List.of(license, license), required)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void completeManifestRequiresMatchingReconciliationAndBuildInputs() {
        var base = inventory(null);
        String path = "BOOT-INF/lib/a.jar";
        var entries = new ArrayList<>(base.entries());
        entries.add(entry(path, ExecutableArchiveInventory.Category.PACKAGED_LIBRARY));
        var inventory = new ExecutableArchiveInventory.Inventory(base.schemaVersion(), base.archiveFileName(),
                base.archiveByteSize(), base.archiveSha256(), entries.size(), base.manifestAttributes(), entries);
        var coordinate = new ExecutablePackagedJarReconciliation.Coordinate("test", "a", "1");
        byte[] lock = "test:a:1=productionRuntimeClasspath\n".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] metadata = ("""
                <verification-metadata xmlns="https://schema.gradle.org/dependency-verification">
                  <components><component group="test" name="a" version="1">
                    <artifact name="a.jar"><sha256 value="%s"/></artifact>
                  </component></components>
                </verification-metadata>
                """.formatted(HASH)).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        var reconciliation = ExecutablePackagedJarReconciliation.reconcile(inventory, lock, metadata,
                new ExecutablePackagedJarReconciliation.ResolvedGraph("fixture", List.of(
                        new ExecutablePackagedJarReconciliation.ResolvedComponent(coordinate,
                                ExecutablePackagedJarReconciliation.ComponentKind.JAR, "a.jar", HASH))),
                new ExecutablePackagedJarReconciliation.ClassificationPolicy("fixture", java.util.Set.of(coordinate),
                        java.util.Set.of(), java.util.Set.of(), List.of(),
                        new ExecutablePackagedJarReconciliation.Cardinalities(1, 1, 0, 0, 0, 1)),
                new byte[0], new ExecutablePackagedJarReconciliation.Limits(2000, 10, 2000, 10, 1, 1, 1, 1));
        var inputs = new ExecutableReleaseEvidence.BuildInputs("b".repeat(40), false, HASH,
                reconciliation.lockfileSha256(), reconciliation.verificationMetadataSha256(), HASH);
        var complete = ExecutableReleaseEvidence.manifest(inputs, inventory, Optional.of(reconciliation));
        assertThat(complete.complete()).isTrue();
        assertThat(ExecutableReleaseEvidence.equivalent(complete, complete)).isTrue();
        assertThatThrownBy(() -> ExecutableReleaseEvidence.manifest(inputs, base, Optional.of(reconciliation)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void missingReconciliationStaysIncompleteAndEvidenceChangesManifestIdentity() {
        var inputs = new ExecutableReleaseEvidence.BuildInputs("b".repeat(40), false, HASH, HASH, HASH, HASH);
        var inventory = inventory(null);
        var manifest = ExecutableReleaseEvidence.manifest(inputs, inventory, Optional.empty());
        assertThat(manifest).isEqualTo(ExecutableReleaseEvidence.manifest(inputs, inventory, Optional.empty()));
        assertThat(manifest.payload()).contains("ABSENT", "INCOMPLETE");
        assertThat(ExecutableReleaseEvidence.equivalent(manifest, manifest)).isFalse();
        var changed = new ExecutableReleaseEvidence.BuildInputs("c".repeat(40), false, HASH, HASH, HASH, HASH);
        assertThat(ExecutableReleaseEvidence.manifest(changed, inventory, Optional.empty()).sha256())
                .isNotEqualTo(manifest.sha256());
    }

    private static ExecutableArchiveInventory.Inventory inventory(String extra) {
        var entries = new ArrayList<ExecutableArchiveInventory.Entry>();
        for (String path : List.of("BOOT-INF/classes/application.properties",
                "BOOT-INF/classes/db/migration/README.md", "BOOT-INF/classpath.idx", "BOOT-INF/layers.idx")) {
            entries.add(entry(path, ExecutableArchiveInventory.Category.APPLICATION_CONTENT));
        }
        for (int version = 1; version <= 17; version++) {
            entries.add(entry("BOOT-INF/classes/db/migration/V" + version + "__test.sql",
                    ExecutableArchiveInventory.Category.FLYWAY_MIGRATION));
        }
        if (extra != null) {
            entries.add(entry(extra, ExecutableArchiveInventory.Category.APPLICATION_CONTENT));
        }
        return new ExecutableArchiveInventory.Inventory(ExecutableArchiveInventory.SCHEMA_VERSION,
                "backend.jar", 1, HASH, entries.size(), Map.of(), entries);
    }

    private static ExecutableArchiveInventory.Entry entry(String path, ExecutableArchiveInventory.Category category) {
        return new ExecutableArchiveInventory.Entry(path, ExecutableArchiveInventory.Kind.FILE, category,
                1, 1, 0, 0, HASH);
    }
}
