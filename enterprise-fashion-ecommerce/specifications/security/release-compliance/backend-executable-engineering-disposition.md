---
title: Backend Executable Engineering Disposition Request
version: 0.1.0
status: Draft
owner: Engineering — accountable disposition pending
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Backend Executable Engineering Disposition Request

## 1. Purpose and Scope

This Draft requests accountable Engineering decisions for the verified backend
executable artifact boundary and DEC-0047 Gate 5. It consolidates existing technical
evidence into twelve bounded decision items. It is a decision request, not an
accepted Engineering disposition, implementation authorization, release approval or
substitute for another accountable owner's decision.

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

Technical evidence, accountable disposition, Legal sufficiency and production
release authorization are separate states. No approval follows from creating,
merging or reviewing this Draft.

## 3. Evidence Baseline

| Evidence | Verified observation |
| --- | --- |
| Executable | `backend/build/libs/backend.jar` |
| Executable SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |
| Executable size | `42,557,193` bytes |
| Resolved runtime coordinates | 73 |
| Locked JAR components | 71 |
| BOM-only coordinates | 2 |
| Packaged locked JARs | 61 |
| Omitted starter JARs | 10 |
| Build-added JARs | 1 |
| Total packaged JARs | 62 |

The observed executable was reproduced byte-for-byte twice on the same machine. It
is ignored local build output, not a retained governed release artifact. Current CI
verifies the ordinary JAR, dependency locks and strict dependency verification; it
does not enforce executable reconciliation, notice coverage or SBOM coverage.

## 4. Decision Scope and Exclusions

Engineering is asked to decide the technical correctness and implementability of the
candidate application-artifact boundary, its inventory, reconciliation,
reproducibility and evidence bindings.

This request does not ask Engineering to:

- select outbound licensing terms or establish rights-holder authority;
- determine license compatibility, legally required notice content or notice
  sufficiency;
- approve supply-chain, vulnerability or production security policy;
- select a hosting provider, artifact repository, release channel, retention service,
  notice tool, SBOM tool or SBOM format;
- decide whether a future container or wrapper is the production release unit without
  Architecture and Platform/DevOps participation; or
- authorize deployment, distribution or production release.

Security, Architecture, Platform/DevOps, qualified Legal/licensing and the repository
rights holder retain their respective authority.

## 5. Engineering Decision Items

### ENG-AB-01 — Candidate Executable Boundary

**Proposed position:** Confirm the exact Spring Boot `backend.jar`, identified by
filename, byte size and SHA-256, as the candidate backend executable application
artifact. A future container or wrapper must preserve its identity and requires a
separate release-unit determination.

**Supporting evidence:** The observed archive has the expected executable manifest,
Spring Boot loader, application content, packaged libraries and migrations. Canonical
Architecture defines one Spring Boot backend deployable application.

**Expected technical consequence:** Gate 5 can use one deterministic backend
application-artifact subject without preselecting a deployment wrapper.

**Outstanding implementation requirements:** Retain the governed artifact and its
digest; generate and verify its complete inventory.

**Cross-owner dependencies:** Architecture and Platform/DevOps must confirm how any
future image or wrapper relates to this application artifact.

**Response:** **PENDING**

### ENG-AB-02 — Application-Content Boundary

**Proposed position:** Confirm that the complete executable archive is the technical
artifact boundary. `BOOT-INF/classes` owns application classes and resources;
`BOOT-INF/lib` owns packaged component content; the manifest, Spring Boot loader,
indexes, migrations and other executable metadata also remain inventoried content.

**Supporting evidence:** Direct archive inspection found these resources within the
exact executable. Restricting the boundary to the two `BOOT-INF` directories would
omit executable loader and metadata bytes.

**Expected technical consequence:** Inventory, notice and SBOM evidence can describe
the actual artifact rather than a partial projection.

**Outstanding implementation requirements:** Define deterministic application-resource
and executable-metadata inventory output.

**Cross-owner dependencies:** Security reviews configuration exposure; qualified
Legal/licensing determines any resulting obligations.

**Response:** **PENDING**

### ENG-AB-03 — Development-Local Configuration

**Proposed position:** Exclude `application-local.properties` from production
packaging and add an automated artifact check preventing its inclusion, subject to
accountable Engineering and Security confirmation.

**Supporting evidence:** The resource is currently packaged and explicitly contains
development-local loopback endpoints, local database identities, local-profile
settings and environment-variable references. No checked-in secret value was
observed, but the resource is development-local rather than production configuration.

**Expected technical consequence:** The production candidate avoids ambiguous local
runtime configuration and reduces accidental activation risk.

**Outstanding implementation requirements:** Decide and implement resource filtering,
then prove exclusion through executable inspection without changing the local
development contract unintentionally.

**Cross-owner dependencies:** Security must confirm the exposure and configuration
control requirements.

**Response:** **PENDING**

### ENG-AB-04 — Packaged Inventory Completeness

**Proposed position:** Require every JAR physically present beneath `BOOT-INF/lib` to
appear exactly once in the artifact-specific inventory; all 62 observed packaged JARs
must be represented.

**Supporting evidence:** Direct executable inspection found 62 packaged JARs: 61
hash-matched locked application components and one build-added component.

**Expected technical consequence:** No shipped component can escape inventory,
notice-evidence or SBOM consideration.

**Outstanding implementation requirements:** Implement deterministic archive
enumeration, digesting, duplicate detection and retained comparison evidence.

**Cross-owner dependencies:** Platform/DevOps governs retained execution and evidence;
Security reviews integrity enforcement.

**Response:** **PENDING**

### ENG-AB-05 — Build-Added Components

**Proposed position:** Require an explicit `BUILD_ADDED` classification for every
packaged component absent from the application runtime graph, and reject completeness
when such a component lacks an evidenced build origin.

**Supporting evidence:** `spring-boot-jarmode-tools-3.5.16.jar` is supplied by Spring
Boot Loader Tools through `BootJar.includeTools`; it is not independently resolved by
the application's runtime dependency graph.

**Expected technical consequence:** Packaging-time additions remain visible in the
physical inventory, notice inputs and artifact-specific SBOM.

**Outstanding implementation requirements:** Detect, classify and bind build-added
components to verified build inputs.

**Cross-owner dependencies:** Security reviews provenance sufficiency; Platform/DevOps
governs operational evidence; qualified Legal/licensing reviews notice treatment.

**Response:** **PENDING**

### ENG-AB-06 — Resolved-but-Not-Packaged Relationships

**Proposed position:** Preserve the ten omitted Spring Boot starters as
`RESOLVED_NOT_PACKAGED` and the two BOMs as `DEPENDENCY_MANAGEMENT_METADATA`. Neither
classification may be represented as physically embedded in the executable.

**Supporting evidence:** Lockfile-to-archive reconciliation established 71 locked JAR
components, of which 61 were packaged, plus two BOM-only coordinates.

**Expected technical consequence:** Dependency intent and physical containment remain
distinct and reproducible.

**Outstanding implementation requirements:** Encode these relationships without
inventing containment and verify them against both the locked graph and archive.

**Cross-owner dependencies:** Platform/DevOps and Security must confirm that any later
SBOM representation preserves these distinctions.

**Response:** **PENDING**

### ENG-AB-07 — Loader Tools Integrity Chain

**Proposed position:** Confirm as technically sufficient for byte identity the chain
from the exactly checksum-verified Loader Tools 3.5.16 source container, through its
nested jarmode resource, to the byte-identical packaged jarmode JAR and its recorded
digest. Do not treat that confirmation as publisher authentication, separate
dependency admission, Legal compatibility or release approval.

**Supporting evidence:** Loader Tools SHA-256
`8096fc208bea29f8986c3411b65b36edd264292a8b0b3982157b945d79e9113a`
is recorded in repository verification metadata. The nested and packaged jarmode JAR
SHA-256 is
`d05beb46a7eac0f1a06733c75828b190a1cb250076ce03be2213aca4a5c0f03f`,
and the bytes matched exactly.

**Expected technical consequence:** The observed build-added bytes have a reproducible
source-container relationship.

**Outstanding implementation requirements:** Retain the outer, nested and packaged
digests and equality result in artifact-specific evidence.

**Cross-owner dependencies:** Security decides whether publisher authentication must
supplement this chain; qualified Legal/licensing decides compatibility and notice
sufficiency.

**Response:** **PENDING**

### ENG-AB-08 — Digest-Bound Evidence

**Proposed position:** Bind inventory, reconciliation, notices, SBOMs, verification
results and the release-evidence manifest to the exact executable SHA-256. A changed
digest creates a new artifact revision and invalidates artifact-specific evidence
unless an approved equivalence rule is satisfied.

**Supporting evidence:** Existing designs define source-to-artifact and
artifact-to-evidence relationships; the observed executable already has an exact
size and digest.

**Expected technical consequence:** Evidence cannot be reused silently across
different executable bytes.

**Outstanding implementation requirements:** Implement manifest generation, digest
verification, freshness checks and immutable retention.

**Cross-owner dependencies:** Platform/DevOps owns operational retention and
promotion; Security owns evidence-integrity requirements.

**Response:** **PENDING**

### ENG-AB-09 — Unknown or Ambiguous Differences

**Proposed position:** Fail closed on an unknown packaged component, ambiguous
coordinate mapping, missing expected packaged component, unclassified resolved
component or component digest mismatch.

**Supporting evidence:** The verified baseline reconciles cleanly only by digest and
explicit classification; filename equality alone is insufficient evidence of
identity.

**Expected technical consequence:** A successful build cannot be represented as
complete release evidence when its physical and resolved inventories disagree.

**Outstanding implementation requirements:** Implement deterministic error outcomes,
negative tests and retained failure evidence.

**Cross-owner dependencies:** Security confirms blocking integrity policy;
Platform/DevOps confirms pipeline failure and recovery behavior.

**Response:** **PENDING**

### ENG-AB-10 — Reproducibility Level

**Proposed position:** Require at minimum two byte-identical `bootJar` builds on a
clean CI runner using the committed Gradle wrapper, locks, verification metadata,
Java baseline and source revision. Treat same-machine local reproducibility as
supporting evidence rather than sufficient retained Gate 5 implementation evidence.

**Supporting evidence:** Two local builds matched byte-for-byte, and Gradle disables
archive timestamps and enables reproducible file ordering. Current CI verifies only
the ordinary JAR.

**Expected technical consequence:** The executable and its inventory become
repeatable under a governed build baseline.

**Outstanding implementation requirements:** Add separately authorized executable
reproducibility and inventory verification; decide whether independent-builder or
cross-platform reproduction is also required.

**Cross-owner dependencies:** Security and Platform/DevOps determine higher-assurance
builder, provenance and retention requirements.

**Response:** **PENDING**

### ENG-AB-11 — Technical Notice Completeness

**Proposed position:** Define Engineering notice-input completeness as an evidence
status for every packaged and build-added component, with applicable nested
attribution preserved. Do not present technical completeness as a Legal notice
sufficiency decision.

**Supporting evidence:** Existing runtime and executable scans identify POM,
embedded-license, notice, attribution, compound-license and nested-material evidence,
including components without conventionally named resources.

**Expected technical consequence:** Technical omissions become detectable while
qualified Legal/licensing retains authority over required delivered content.

**Outstanding implementation requirements:** Implement deterministic collection,
source mapping, completeness verification and artifact-bound notice evidence.

**Cross-owner dependencies:** Qualified Legal/licensing decides required content and
sufficiency; Platform/DevOps governs generation, retention and delivery; Security
reviews integrity.

**Response:** **PENDING**

### ENG-AB-12 — SBOM Physical Completeness

**Proposed position:** Require the artifact-specific SBOM to identify the exact
executable as its subject, reconcile to all 62 packaged JARs and preserve accurate
non-contained relationships for the ten omitted starters and two BOMs.

**Supporting evidence:** The executable inventory and locked graph are verified,
related but non-identical sets with one explicit build-added component.

**Expected technical consequence:** The SBOM represents actual shipped bytes and
relevant dependency-management relationships without conflating them.

**Outstanding implementation requirements:** Obtain approval of a suitable format and
tool, implement relationship mapping and validate SBOM-to-archive equality.

**Cross-owner dependencies:** Security and Platform/DevOps govern tool admission,
format, generation and release enforcement; qualified Legal/licensing reviews use of
the resulting evidence.

**Response:** **PENDING**

## 6. Engineering Response Instructions

The accountable Engineering owner must provide one response for every decision item:
**CONFIRM**, **AMEND** or **REJECT**.

Each response must include:

- the attributable accountable owner;
- the response date;
- decision-specific rationale;
- conditions or limitations;
- exact replacement text or position for an amendment; and
- supporting evidence references.

A collective response must preserve item-level traceability. Silence, elapsed time,
document creation, merge status or implementation evidence is not approval.

## 7. Gate 5 Completion Prerequisites

Gate 5 remains **INCOMPLETE**. Completion requires:

1. an attributable Engineering disposition for all twelve decision items;
2. a final candidate application-artifact boundary;
3. decided and enforced treatment of `application-local.properties`;
4. a retained exact executable artifact and digest;
5. a deterministic physical inventory of the executable;
6. one-to-one reconciliation of packaged, omitted, BOM and build-added components;
7. automated rejection of unknown, ambiguous, missing or digest-mismatched components;
8. clean-runner `bootJar` reproducibility evidence at the approved assurance level;
9. a complete application-resource inventory;
10. artifact-bound technical license and notice evidence status for every packaged
    component;
11. an artifact-specific SBOM inventory, or an approved boundary explaining which
    part remains governed by incomplete Gate 7;
12. a retained manifest binding source revision, build inputs, locks, verification
    metadata, artifact, inventories and results; and
13. no unresolved Engineering-owned completeness question.

The existing executable, reconciliation, resource and licensing observations are
verified technical evidence. The completeness and failure rules in this request are
proposals. Governed retained artifacts, automated enforcement, notices, SBOMs and
manifest evidence are missing implementation evidence. The attributable Engineering
disposition is also missing. None of these states substitutes for another.

## 8. Cross-Owner Dependencies

| Owner | Retained authority and required contribution | Status |
| --- | --- | --- |
| Engineering | Decide technical artifact boundary, inventory, reconciliation, reproducibility and implementability. | **PENDING** |
| Security | Decide supply-chain assurance, configuration exposure, provenance, integrity, vulnerability and fail-closed security requirements. | **PENDING** |
| Platform/DevOps | Decide protected build execution, artifact/evidence retention, notice/SBOM operations, delivery, promotion and failure recovery. | **PENDING** |
| Architecture | Confirm compatibility with the canonical deployment model and whether a wrapper or container creates another governed release unit. | **PENDING** |
| Qualified Legal/licensing | Decide compatibility, required notice content, attribution, delivery obligations and sufficiency. | **PENDING** |
| Repository rights holder | Establish rights-holder authority, licensing authority and authorized distribution boundaries. | **NOT VERIFIED; PENDING** |

Engineering confirmation cannot transfer, satisfy or infer another owner's
authority.

## 9. Open Technical Questions

1. Is `application-local.properties` excluded, or is a distinct production
   resource-processing rule required?
2. Is the migration README intended production artifact content?
3. Must Gate 5 require a generated SBOM, or only a complete implementation-ready
   inventory while Gate 7 governs SBOM operations?
4. What minimum independent-builder or cross-platform reproducibility evidence is
   proportionate?
5. How will application content and non-JAR third-party material be identified in the
   executable inventory?
6. Which SBOM relationship types will preserve packaged, omitted, BOM and build-added
   distinctions?
7. Where will immutable executable, inventory and verification evidence be retained?
8. What identity evidence is required when digest-to-coordinate matching is
   unavailable?
9. Must independent publisher authentication supplement the Loader Tools checksum
   chain?
10. How will inventory completeness be tested when Spring Boot packaging behavior
    changes?

## 10. Proposed Review Sequence

1. Confirm the candidate executable and physical boundary.
2. Decide production treatment of local-only and documentation resources.
3. Confirm reconciliation classifications and fail-closed rules.
4. Confirm the Loader Tools integrity boundary.
5. Confirm digest binding and stale-evidence behavior.
6. Set the required reproducibility assurance level.
7. Confirm technical notice and SBOM completeness definitions.
8. Record each response as **CONFIRM**, **AMEND** or **REJECT**.
9. Translate confirmed positions into independently testable implementation
   requirements.
10. Implement and retain evidence before reconsidering Gate 5 completion.

## 11. Current Findings and Limitations

- No accountable Engineering disposition exists.
- No retained governed executable artifact exists.
- No generated backend notice exists.
- No generated backend SBOM exists.
- No executable reconciliation CI enforcement exists.
- `application-local.properties` remains packaged.
- No production release authorization exists.
- Same-machine reproducibility does not establish independent-builder or
  cross-platform reproducibility.
- Independent publisher-signature authentication was not performed for Loader Tools
  or the nested jarmode component.
- Technical evidence does not establish licensing compatibility or notice
  sufficiency.

## 12. DEC-0047 Gate Mapping

| Gate | Contribution of this request | Remaining work | Status |
| --- | --- | --- | --- |
| Gate 5 — Engineering artifact inventory | Presents exact technical decisions, evidence and completion prerequisites for accountable Engineering review. | Attributable disposition, implementation, retained artifact inventory and verification evidence. | **INCOMPLETE** |
| Gate 7 — Platform/DevOps notice and SBOM plan | Preserves the tool-neutral operational dependencies and evidence relationships. | Accountable plan, selected and admitted mechanisms, implementation, retention, delivery and verification. | **INCOMPLETE** |
| All other DEC-0047 gates | Creates no rights-holder, business, Legal, Architecture, Security, licensing or final-decision approval. | All requirements stated by DEC-0047 remain outstanding. | **INCOMPLETE; unchanged** |

No DEC-0047 gate becomes **MET** through this request.

## 13. References

- [Backend Executable Notice and SBOM Coverage Design](backend-executable-notice-sbom-coverage-design.md)
- [Backend Executable Dependency-Reconciliation Evidence](backend-executable-dependency-reconciliation-evidence.md)
- [Backend Production Runtime Dependency License Evidence](../dependency-admission/backend-production-runtime-license-evidence.md)
- [Provider-Neutral Application Release-Evidence Design](provider-neutral-application-release-evidence-design.md)
- [DEC-0047 — Repository Outbound Licensing and Distribution Model](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md)
- [Architecture](../../../.ai/core/ARCHITECTURE.md)
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md)
- [Backend build configuration](../../../backend/build.gradle.kts)
- [Backend dependency lock](../../../backend/gradle.lockfile)
- [Backend dependency verification metadata](../../../backend/gradle/verification-metadata.xml)
- [Backend CI workflow](../../../../.github/workflows/backend-gradle-baseline.yml)

## 14. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Created a non-authoritative request for accountable Engineering disposition on the backend executable artifact boundary, reconciliation, completeness and DEC-0047 Gate 5; preserved all decisions as pending and all release authorization as not granted. |
