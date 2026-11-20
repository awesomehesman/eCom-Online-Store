---
title: Backend Executable Notice and SBOM Coverage Design
version: 0.1.0
status: Draft
owner: Engineering — accountable disposition pending
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Backend Executable Notice and SBOM Coverage Design

## 1. Purpose and Scope

This Draft defines a provider-neutral and tool-neutral technical design for complete,
reproducible, artifact-bound third-party notice and software bill of materials (SBOM)
coverage for the observed Spring Boot executable. It distinguishes application
dependency resolution from physical executable contents and defines the evidence
relationships needed to reconcile them.

This document does not select or admit tooling, approve licensing compatibility or
notice sufficiency, establish accountable approval, authorize a release, modify the
build, generate evidence or establish production readiness.

## 2. Governance and Authority

| Governance area | Current status |
| --- | --- |
| [DEC-0047](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md) | **PROPOSED; non-authoritative** |
| Rights-holder authority | **NOT VERIFIED** |
| Repository outbound license | **UNRESOLVED** |
| Engineering disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Architecture disposition | **PENDING** |
| Qualified Legal/licensing disposition | **PENDING** |
| DEC-0047 Gate 5 | **INCOMPLETE** |
| DEC-0047 Gate 7 | **INCOMPLETE** |
| Production release authorization | **NOT GRANTED** |

Technical completeness, Legal sufficiency, accountable approval and release
authorization are separate states. No state is implied by the existence or review of
this Draft.

## 3. Existing Evidence Baseline

| Evidence | Observed value |
| --- | --- |
| Source revision | `3044074b83a0c5efadfb6dcc75b0bab445a82713` |
| Executable | `backend/build/libs/backend.jar` |
| Executable SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |
| Executable size | `42,557,193` bytes |
| Packaged JARs | 62 |
| Locked-and-packaged JARs | 61 |
| Build-added jarmode JARs | 1 |
| Resolved but omitted starter JARs | 10 |
| BOM-only coordinates | 2 |

These values describe the observed local technical-evidence baseline. The executable
is ignored build output, not a retained governed artifact or authorized release.

## 4. Component Classification Model

| Classification | Meaning |
| --- | --- |
| `APPLICATION_CONTENT` | First-party or origin-unverified application classes, configuration, migrations, documentation and build-generated application metadata contained in the executable. |
| `PACKAGED` | A component physically present beneath `BOOT-INF/lib` and reconciled to a locked application-runtime component. |
| `BUILD_ADDED` | A component physically present in the executable because the executable-packaging process added it, rather than because the application runtime graph resolved it. |
| `RESOLVED_NOT_PACKAGED` | A component present in the resolved application graph but absent from the executable archive. |
| `DEPENDENCY_MANAGEMENT_METADATA` | A Bill of Materials (BOM) or other metadata that governs resolution but is not executable content. |

Every relevant item must receive exactly one physical-content classification for a
given executable revision. A logical dependency relationship may coexist with that
classification. `RESOLVED_NOT_PACKAGED` starters and
`DEPENDENCY_MANAGEMENT_METADATA` BOMs must not be represented as physically embedded.

## 5. Executable Artifact Boundary

The proposed SBOM and notice subject is the exact Spring Boot executable JAR identified
by filename, byte size and SHA-256. Its observed boundary includes:

- the executable ZIP/JAR container and `META-INF/MANIFEST.MF`;
- application classes and resources beneath `BOOT-INF/classes`;
- 62 component JARs beneath `BOOT-INF/lib`;
- `application.properties` and `application-local.properties`;
- Flyway migrations V1–V17;
- the migration README;
- Spring Boot loader classes;
- the build-generated classpath and layers indexes; and
- other executable metadata physically contained by the exact artifact.

This is a candidate technical boundary. Architecture confirmation remains
**PENDING**, particularly if a future container image or another wrapper becomes the
production release unit.

## 6. Resolved-to-Packaged Reconciliation

The deterministic baseline is:

| Reconciliation category | Count |
| --- | ---: |
| Resolved `productionRuntimeClasspath` coordinates | 73 |
| Locked JAR components | 71 |
| BOM-only coordinates | 2 |
| Packaged JARs hash-matched to locked artifacts | 61 |
| Resolved starter JARs omitted from the executable | 10 |
| Build-added JARs | 1 |
| Total packaged JARs | 62 |

Reconciliation must:

1. enumerate the exact resolved coordinate set;
2. enumerate the exact physical `BOOT-INF/lib` entries;
3. identify packaged components by cryptographic digest, not filename alone;
4. map each of the 61 packaged application JARs to exactly one locked coordinate;
5. record the ten omitted starters as `RESOLVED_NOT_PACKAGED`;
6. record the two BOMs as `DEPENDENCY_MANAGEMENT_METADATA`;
7. record jarmode tools as `BUILD_ADDED`;
8. reject unknown, ambiguous or unclassified differences; and
9. bind the reconciliation result to the executable digest.

The resolved graph and physical artifact inventory are related but non-identical
sets. Neither may be substituted for the other.

## 7. Build-Added Component Coverage

| Property | Verified value |
| --- | --- |
| Packaged component | `spring-boot-jarmode-tools-3.5.16.jar` |
| Source coordinate | `org.springframework.boot:spring-boot-loader-tools:3.5.16` |
| Source artifact SHA-256 | `8096fc208bea29f8986c3411b65b36edd264292a8b0b3982157b945d79e9113a` |
| Nested source resource | `META-INF/jarmode/spring-boot-jarmode-tools.jar` |
| Packaged SHA-256 | `d05beb46a7eac0f1a06733c75828b190a1cb250076ce03be2213aca4a5c0f03f` |
| Nested-to-packaged comparison | **BYTE-IDENTICAL** |
| Direct standalone verification entry | Not present |
| Source-container verification | Exact Loader Tools checksum covered by repository verification metadata |

The nested component is indirectly covered by the verified outer Loader Tools
artifact and was copied byte-for-byte into the executable by default
`BootJar.includeTools` behavior. Independent publisher-signature authentication was
not performed. This evidence does not grant separate dependency admission or Legal
approval.

## 8. Notice Evidence Inputs

A technically complete collection phase must preserve:

- exact Maven Project Object Model (POM) declarations for resolved coordinates;
- parent-POM identity for inherited declarations;
- embedded license resources from physically packaged JARs;
- embedded `NOTICE`, `DEPENDENCIES` and equivalent attribution resources;
- compound and alternative license declarations without flattening them;
- nested third-party attribution and copied-component evidence;
- publisher-specific or nonstandard license wording without unsupported relabeling;
- the jarmode-tools Apache-2.0 license and Spring Boot notice;
- documented POM evidence for packaged components with no conventional embedded legal
  resources; and
- an explicit unresolved state when evidence remains unavailable or ambiguous.

Relevant existing findings include compound Logback and Jakarta declarations,
additional Jackson TOML terms, Tomcat and Spring Framework nested material,
PostgreSQL/OnGres attributions, Log4j dependency notices and publisher-specific
Bouncy Castle wording. Collection does not determine which material qualified Legal
review will require in the delivered notice.

## 9. Observed License and Notice Counts

Direct inspection of the 62 packaged JARs found:

| Conventional resource category | Count |
| --- | ---: |
| JARs with a conventional license resource | 42 |
| JARs with a conventional notice or attribution resource | 30 |
| JARs with neither conventional category | 20 |

The categories overlap. A JAR may contain both resource types, and “neither” means
only that the inspected archive lacked a conventionally named resource. It does not
establish that the component is unlicensed, incompatible or exempt from notice. These
are technical observations, not Legal sufficiency conclusions.

## 10. Release Notice Coverage Model

The proposed lifecycle distinguishes:

1. **Collected source evidence** — exact POM, parent-POM, embedded legal resources,
   nested attribution and build-added-component evidence.
2. **Reviewed required content** — material that qualified Legal/licensing and other
   accountable reviewers determine must be preserved or delivered for the approved
   release context.
3. **Generated notice** — deterministic output assembled from reviewed required
   content and bound to the executable digest.
4. **Verified notice** — generated output whose input coverage, content identity,
   digest and artifact association pass approved checks.
5. **Delivered notice** — the verified notice placed or supplied through the approved
   release channel, with delivery evidence retained.

The release notice must identify its source-evidence revision, executable SHA-256 and
own SHA-256. No generation, review, retention, placement or delivery mechanism is
approved by this Draft.

## 11. SBOM Coverage Model

The future SBOM must represent:

| Subject or component | Classification and relationship requirement |
| --- | --- |
| Exact `backend.jar` | Primary SBOM subject, identified by size and SHA-256. |
| 61 locked-and-packaged JARs | `PACKAGED`; physically contained and mapped by digest to resolved coordinates. |
| jarmode tools JAR | `BUILD_ADDED`; physically contained and derived from the verified Loader Tools nested resource. |
| Application classes and resources | `APPLICATION_CONTENT`; physically contained by the executable without inventing third-party package identity. |
| Ten starter modules | `RESOLVED_NOT_PACKAGED`; application graph relationship only, never physical containment. |
| Two BOM coordinates | `DEPENDENCY_MANAGEMENT_METADATA`; version-management relationship only. |
| Loader Tools source container | Build input and source-container relationship for jarmode tools; not an application runtime or packaged component. |

Required semantics include physical containment, application dependency, resolved
coordinate mapping, build input, derived-from/source-container association and
dependency-management relationships. Exact relationship names depend on the later
approved format. This Draft does not select SPDX, CycloneDX or another format.

## 12. Artifact and Evidence Binding

Every artifact-specific evidence record must identify the exact executable SHA-256 it
describes. The complete binding set includes:

- source repository and commit;
- working-tree state;
- build configuration and toolchain identities;
- dependency lock and verification-metadata digests;
- executable filename, size and digest;
- packaged component filenames and digests;
- resolved coordinate and classification mappings;
- build-input and source-container identities and digests;
- collected license and notice evidence identities and digests;
- generated and verified notice identity and digest;
- SBOM identity, format, subject and digest;
- verification results and verification identity;
- generation timestamp and automation identity;
- retention reference; and
- accountable review and release-authorization references.

A changed executable digest creates a new artifact revision and invalidates reuse of
artifact-specific notice, SBOM and verification evidence unless exact equivalence is
independently re-established under an approved rule.

## 13. Deterministic Completeness Rules

Technical completeness requires all of the following to pass:

1. the physical `BOOT-INF/lib` set equals the recorded 62-component inventory;
2. every packaged component digest equals its recorded digest;
3. all resolved coordinates are classified as packaged, resolved-not-packaged or
   dependency-management metadata as applicable;
4. every physical component absent from the application graph is classified and
   evidenced as build-added;
5. every packaged component has linked license-evidence status;
6. every applicable nested attribution is preserved and linked;
7. the notice input set matches the reviewed required-content set;
8. the SBOM subject digest equals the executable digest;
9. SBOM physical containment equals the archive inventory;
10. omitted starters and BOMs are not represented as physically embedded;
11. notice, SBOM and manifest digests are verified; and
12. source, build, artifact and evidence revisions are current and mutually
    consistent.

Technical completeness does not establish license compatibility, notice sufficiency,
accountable approval or release authorization.

## 14. Proposed Failure Conditions

| Condition | Proposed fail-closed result | Current CI enforcement |
| --- | --- | --- |
| Missing packaged component | Reject executable coverage claim. | Not enforced |
| Unknown build-added component | Reject executable coverage claim. | Not enforced |
| Unresolved or ambiguous component identity | Reject inventory and SBOM verification. | Not enforced |
| Unaccounted nested attribution | Reject notice completeness. | Not enforced |
| Incorrect executable or component digest | Reject all affected artifact-specific evidence. | Not enforced as release evidence |
| Missing required notice material | Reject notice verification and release authorization. | Not enforced |
| SBOM inventory mismatch | Reject SBOM verification. | Not enforced |
| Omitted jarmode component | Reject physical inventory, notice and SBOM coverage. | Not enforced |
| Stale source, lock, build or evidence revision | Reject evidence reuse. | Lock checks exist; complete release freshness is not enforced |
| Missing required provenance | Keep coverage unverified and reject authorization when policy requires it. | Partial dependency verification only |
| Missing required accountable disposition | Reject release authorization. | No production authorization mechanism exists |

These are proposed controls. Current backend CI performs strict dependency checks,
lock consistency, tests and an ordinary-JAR reproducibility check; it does not enforce
executable notice or SBOM coverage.

## 15. Tool-Neutral Generation Interfaces

| Logical stage | Required inputs | Required outputs |
| --- | --- | --- |
| Dependency graph inventory | Locked configurations, resolved coordinates, BOMs, verification evidence | Exact classified graph inventory with digests and provenance references |
| Executable archive inspection | Exact executable bytes | Physical component and application-resource inventory with digests |
| Component reconciliation | Graph inventory and archive inventory | One-to-one mappings, omissions, build additions and explicit failures |
| License evidence collection | Coordinates, POMs, parent POMs, packaged JARs, source containers | Immutable evidence index preserving compound and nested material |
| Notice assembly | Reviewed required content and evidence index | Candidate notice plus source mapping and digest |
| SBOM assembly | Executable subject, reconciled graph, application content and build inputs | Candidate artifact-specific SBOM and digest |
| Artifact-binding manifest | Source/build identities, artifact, inventories, notice, SBOM, checks | One manifest binding every evidence object to the executable digest |
| Verification | Manifest and all referenced objects | Deterministic results, failures, verifier identity and timestamps |
| Retention | Verified immutable objects and accountable dispositions | Protected retention references and retrieval evidence |

No concrete tool, plugin, provider, serialization or storage mechanism is selected or
admitted.

## 16. Engineering Review Questions

1. Is the executable JAR the correct technical artifact boundary?
2. Should `application-local.properties` and the migration README be included?
3. Are the proposed resolved-to-packaged classifications and reconciliation rules
   complete and deterministic?
4. What same-machine, clean-builder or cross-platform reproducibility evidence is
   required?
5. What component identity evidence is mandatory when hash-to-coordinate matching is
   unavailable or ambiguous?
6. What technical evidence constitutes complete notice and SBOM input coverage?

Engineering disposition remains **PENDING**.

## 17. Security Review Questions

1. Is the outer Loader Tools verification plus nested-to-packaged equality sufficient
   for the build-added integrity boundary?
2. What additional provenance or signature verification is required for build-added
   components?
3. What admission requirements apply to future generation and inspection tooling?
4. Which failures must block evidence verification or release authorization?
5. How must vulnerability-gating results bind to the exact executable and SBOM?
6. How must evidence integrity, access, retention, revocation and incident response be
   protected?

Security disposition remains **PENDING**.

## 18. Platform/DevOps Review Questions

1. Where and under which protected automation identity will generation execute?
2. How will immutable artifacts and their digests be retained?
3. How will notice, SBOM, manifest and verification evidence be retained together?
4. How will evidence remain associated through promotion and delivery?
5. What fail-closed operational and recovery behavior applies when generation,
   verification or retention is unavailable or uncertain?
6. What identifier defines one release-unit revision across environments?

Platform/DevOps disposition remains **PENDING**.

## 19. Architecture Review Questions

1. Is `bootJar` the appropriate candidate application release unit?
2. If a future image contains the executable, does the image become an additional or
   superseding release-evidence subject?
3. How must backend and frontend release evidence remain independently identifiable
   while supporting one application release?
4. Is this provider-neutral design compatible with the canonical Architecture, and
   would any later packaging choice require an Architecture Decision Record?

Architecture disposition remains **PENDING**.

## 20. Qualified Legal Review Questions

1. Which exact license declarations apply to the physically packaged components?
2. How must compound, alternative and inherited declarations be treated?
3. Which nested attribution and publisher-specific text must be preserved?
4. When is POM-only evidence sufficient for a component without conventional embedded
   legal resources?
5. What exact content is required in the delivered backend notice?
6. Where and how must notice material accompany each approved distribution context?
7. Is the resulting notice legally sufficient for the eventual outbound terms and
   distribution model?

This Draft does not answer these questions. Qualified Legal/licensing disposition
remains **PENDING**.

## 21. DEC-0047 Gate Mapping

| Gate | Contribution from this Draft | Remaining work | Status |
| --- | --- | --- | --- |
| Gate 5 — Engineering artifact inventory | Defines exact executable scope, component classifications, reconciliation rules, binding requirements and completeness checks. | Accountable Engineering confirmation, retained inventory and verified implementation evidence. | **INCOMPLETE** |
| Gate 7 — Platform/DevOps notice and SBOM plan | Defines tool-neutral notice/SBOM lifecycles, interfaces, failure conditions and retention relationships. | Approved tooling and formats, implementation, operational verification, delivery and accountable dispositions. | **INCOMPLETE** |
| All other gates | No approval, licensing, rights-holder, Architecture, Security or final-decision evidence is created. | Requirements in DEC-0047 remain outstanding. | **INCOMPLETE; unchanged** |

No DEC-0047 gate becomes **MET** through this Draft.

## 22. Evidence Limitations

- No backend release notice has been generated.
- No backend SBOM has been generated.
- No generation, scanning or verification tool has been selected or admitted.
- No release-evidence manifest has been generated or retained.
- No independent builder or application-artifact attestation exists.
- No qualified Legal compatibility or notice-sufficiency determination exists.
- No accountable Engineering, Security, Platform/DevOps, Architecture or Legal
  approval exists.
- No production release authorization exists.
- The observed executable remains local ignored build output rather than a governed
  retained release artifact.

## 23. References

- [Backend Executable Dependency-Reconciliation Evidence](backend-executable-dependency-reconciliation-evidence.md)
- [Backend Production Runtime Dependency License Evidence](../dependency-admission/backend-production-runtime-license-evidence.md)
- [Provider-Neutral Application Release-Evidence Design](provider-neutral-application-release-evidence-design.md)
- [DEC-0047 — Repository Outbound Licensing and Distribution Model](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md)
- [Architecture](../../../.ai/core/ARCHITECTURE.md)
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md)
- [Backend build configuration](../../../backend/build.gradle.kts)
- [Backend repository configuration](../../../backend/settings.gradle.kts)
- [Backend dependency lock](../../../backend/gradle.lockfile)
- [Backend dependency verification metadata](../../../backend/gradle/verification-metadata.xml)
- [Backend CI workflow](../../../../.github/workflows/backend-gradle-baseline.yml)

## 24. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Created a provider-neutral and tool-neutral backend executable notice and SBOM coverage design; distinguished resolved, packaged, build-added, omitted and dependency-management components; defined artifact binding, deterministic completeness, proposed failure conditions, pending owner reviews and explicit non-approval boundaries. |
