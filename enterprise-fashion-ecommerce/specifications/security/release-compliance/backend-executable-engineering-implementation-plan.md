---
title: Backend Executable Engineering Implementation Plan
version: 0.1.0
status: Draft
owner: Engineering — accountable disposition pending
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Backend Executable Engineering Implementation Plan

## 1. Purpose and Governance

This Draft defines an incremental Engineering implementation plan for the verified
backend executable boundary and the twelve items in the
[Backend Executable Engineering Disposition Request](backend-executable-engineering-disposition.md).
The founder has authorized planning and later bounded implementation in the stated
technical direction. That authorization is not an independent accountable
Engineering, Security, Platform/DevOps, Architecture, qualified Legal/licensing or
rights-holder disposition.

The direction used for planning is:

| Decision | Founder-authorized planning direction | Accountable response |
| --- | --- | --- |
| ENG-AB-01 | `CONFIRM` direction | **PENDING** |
| ENG-AB-02 | `CONFIRM` direction | **PENDING** |
| ENG-AB-03 | `AMEND` direction | **PENDING** |
| ENG-AB-04 | `CONFIRM` direction | **PENDING** |
| ENG-AB-05 | `CONFIRM` direction | **PENDING** |
| ENG-AB-06 | `CONFIRM` direction | **PENDING** |
| ENG-AB-07 | `CONFIRM` direction | **PENDING** |
| ENG-AB-08 | `CONFIRM` direction | **PENDING** |
| ENG-AB-09 | `CONFIRM` direction | **PENDING** |
| ENG-AB-10 | `AMEND` direction | **PENDING** |
| ENG-AB-11 | `CONFIRM` direction | **PENDING** |
| ENG-AB-12 | `CONFIRM` direction | **PENDING** |

DEC-0047 remains **Proposed** and non-authoritative. Rights-holder authority remains
**NOT VERIFIED**; the outbound license remains **UNRESOLVED**; all accountable owner
dispositions remain **PENDING**; Gates 5 and 7 remain **INCOMPLETE**; production
release authorization remains **NOT GRANTED**.

## 2. Evidence Baseline

| Evidence | Verified observation |
| --- | --- |
| Candidate executable | `backend/build/libs/backend.jar` |
| Executable SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |
| Executable size | `42,557,193` bytes |
| Resolved runtime coordinates | 73 |
| Locked JAR components | 71 |
| BOM-only coordinates | 2 |
| Packaged locked JARs | 61 |
| Omitted starter JARs | 10 |
| Build-added JARs | 1 |
| Total packaged JARs | 62 |
| Reproducibility | Two byte-identical builds on one machine and dependency cache |
| Retained governed artifact | Not available |
| Backend release notice | Not generated |
| Backend SBOM | Not generated |
| Executable reconciliation CI | Not implemented |

The evidence is an observed technical baseline, not a release baseline. The current
backend workflow verifies strict dependencies, lock consistency and reproducibility
of the ordinary JAR, not the executable `bootJar`.

## 3. Decision-to-Requirement Mapping

| Decision | Planned requirement | Primary increment | Approval boundary |
| --- | --- | --- | --- |
| ENG-AB-01 | Treat the exact digest-identified `backend.jar` as the candidate application-artifact subject. | BEI-01 | Engineering, Architecture and Platform/DevOps remain pending. |
| ENG-AB-02 | Inventory the entire archive, including application resources, loader content and executable metadata. | BEI-01 / BEI-03 | Engineering and Architecture remain pending. |
| ENG-AB-03 | Plan exclusion of development-local configuration from a production candidate without breaking local development. | BEI-03 | Engineering and Security confirmation required before the packaging change. |
| ENG-AB-04 | Require exact equality between the physical packaged-JAR set and its inventory. | BEI-01 / BEI-02 | Engineering confirmation required before enforcement is represented as accepted. |
| ENG-AB-05 | Classify and evidence every build-added component. | BEI-02 | Security provenance requirements remain pending. |
| ENG-AB-06 | Preserve omitted starters and BOMs as non-contained relationships. | BEI-02 | SBOM representation remains subject to later approval. |
| ENG-AB-07 | Preserve the Loader Tools outer-to-nested-to-packaged integrity chain. | BEI-02 / BEI-04 | Publisher authentication and Legal sufficiency remain outside Engineering authority. |
| ENG-AB-08 | Bind every evidence object to the exact executable digest. | BEI-04 | Retention and security controls remain pending. |
| ENG-AB-09 | Reject unknown, ambiguous, missing, unclassified or digest-mismatched content. | BEI-02 / BEI-04 | Security and Platform/DevOps blocking policy remains pending. |
| ENG-AB-10 | Plan clean-runner, two-build `bootJar` reproducibility and retain local evidence as bounded only. | BEI-05 | Accountable assurance level and CI adoption remain pending. |
| ENG-AB-11 | Produce complete technical notice-input coverage without making Legal sufficiency claims. | BEI-06 | Qualified Legal and Platform/DevOps decisions required. |
| ENG-AB-12 | Design an artifact-specific SBOM inventory reconciling physical and non-contained relationships. | BEI-07 | Tool, format and operational policy remain unselected. |

No mapping changes the **PENDING** responses in the disposition request.

## 4. Ordered Implementation Increments

| Order | Identifier | Increment | Dependency |
| ---: | --- | --- | --- |
| 1 | BEI-01 | Deterministic executable archive inventory | Existing executable evidence only |
| 2 | BEI-02 | Locked-to-packaged reconciliation and build-added classification | BEI-01 |
| 3 | BEI-03 | Application-resource policy and local-configuration exclusion | BEI-01; Security confirmation before exclusion |
| 4 | BEI-04 | Digest-bound evidence manifest and fail-closed verification | BEI-01–BEI-03 |
| 5 | BEI-05 | Clean-runner executable reproducibility | BEI-01, BEI-02 and approved CI boundary |
| 6 | BEI-06 | Technical notice-input coverage | BEI-02 and BEI-04 |
| 7 | BEI-07 | Artifact-specific SBOM inventory | BEI-02, BEI-04 and admitted format/tool decision |

Each increment must be independently reviewable and must leave production release
**NOT GRANTED**.

## 5. BEI-01 — Deterministic Executable Archive Inventory

**Objective:** Produce a deterministic, read-only inventory of one exact executable
archive before selecting notice or SBOM tooling.

**Exact intended repository scope:**

- `enterprise-fashion-ecommerce/backend/build.gradle.kts` only if a project-owned
  verification task is approved;
- a new, narrowly scoped implementation beneath
  `enterprise-fashion-ecommerce/backend/src/test/` or a repository-approved backend
  verification-script location, selected during implementation review; and
- focused verification tests colocated with that implementation.

No application production package, dependency declaration, lockfile, verification
metadata or migration belongs in this increment.

**Dependencies:** Existing `bootJar`, archive reproducibility configuration and the
verified artifact layout.

**Proposed implementation approach:** Read ZIP/JAR metadata without executing the
application. Record artifact filename, size and SHA-256; manifest attributes; every
archive path; every `BOOT-INF/lib` entry and digest; application-resource paths;
loader paths; classpath/layers indexes; and deterministic ordering. Reject duplicate
paths, malformed archives and unreadable entries.

**Acceptance criteria:**

- identical bytes yield byte-identical canonical inventory output;
- the observed baseline reports 62 packaged JARs;
- the inventory includes `BOOT-INF/classes`, loader content, manifest and indexes;
- no archive entry is silently omitted; and
- inventory output names its exact executable digest.

**Verification commands or test categories:** Focused unit tests with synthetic ZIP
fixtures; focused archive-integration test against a locally built `bootJar`; strict
backend checks after separately authorized implementation; `git diff --check`.

**Evidence outputs:** Canonical archive inventory, executable digest, inventory
digest, deterministic test results and explicit failure results. Outputs remain test
evidence until retention is approved.

**Explicit approval prerequisites:** Accountable Engineering must confirm the archive
boundary before the output is treated as governed Gate 5 evidence.

**Stop conditions:** Stop on an ambiguous output schema, nondeterministic ordering,
archive execution requirement, dependency addition, or pressure to treat a local
artifact as an authorized release.

## 6. BEI-02 — Exact Packaged-JAR Reconciliation

**Objective:** Reconcile the exact archive inventory with the committed production
runtime graph and classify every difference.

**Exact intended repository scope:** The BEI-01 verifier and tests; read-only inputs
from `backend/gradle.lockfile` and `backend/gradle/verification-metadata.xml`;
`backend/build.gradle.kts` only for an approved verification-task entry point.

**Dependencies:** BEI-01 canonical inventory and the committed locked graph.

**Local verification:** From `backend` using Java 21, explicitly invoke
`./gradlew --offline --no-daemon --dependency-verification=strict inspectBei02GradleEvidence`.
The task uses the selected Gradle runtime variants and artifacts, checks the executable
through BEI-01, and verifies the Loader Tools provenance chain. The current baseline
reaches the exact-checksum gate with the expected physical classifications and returns
`MissingChecksumEvidence` for 35 selected runtime JARs. See the reconciliation evidence
record for the observed counts and evidence boundary. Engineering and Security
dispositions remain pending.

**Proposed implementation approach:** Resolve the governed production runtime graph
without rewriting locks; distinguish JAR components and BOM metadata; hash packaged
JAR bytes; match by verified identity and digest rather than filename; classify
packaged, resolved-not-packaged, dependency-management metadata and build-added
items; require one deterministic classification per relevant item.

**Acceptance criteria:**

- 73 resolved coordinates reconcile to 71 JAR components and two BOMs;
- 61 locked JARs match packaged bytes exactly;
- ten starter JARs remain non-contained relationships;
- one jarmode JAR is classified build-added;
- all 62 physical JARs are accounted for; and
- ambiguous, unknown, missing or duplicate matches fail closed.

**Verification commands or test categories:** Unit fixtures for every classification;
negative tests for unknown, ambiguous, missing and duplicate components; focused
lock-to-archive integration verification; strict Gradle checks when implementation is
authorized.

**Evidence outputs:** Classified reconciliation report, component digests, mapping
results, omissions, build additions, input identities and report digest.

**Explicit approval prerequisites:** Engineering confirmation of the classification
model; Security confirmation before results become a release integrity control.

**Stop conditions:** Stop if resolution would mutate locks, require unapproved network
access, rely on filename identity alone, or cannot account for every archive entry.

## 7. Build-Added Component Classification

This requirement is delivered within BEI-02, not as a separate deployment milestone.
The jarmode component must record:

- packaged filename and digest;
- `BUILD_ADDED` classification;
- Loader Tools source coordinate and outer-artifact digest;
- nested resource path and digest;
- nested-to-packaged byte-equality result; and
- absence from the application runtime lockfile.

Unknown build-added content must fail reconciliation. The verified checksum chain
does not establish publisher-signature authentication, dependency admission, Legal
compatibility or notice sufficiency.

## 8. BEI-03 — Application Resource and Loader Inventory

**Objective:** Classify non-library executable content and establish explicit policy
inputs for application resources, loader content and generated metadata.

**Exact intended repository scope:** BEI-01 inventory implementation and tests;
`backend/src/main/resources/application-local.properties` and
`backend/build.gradle.kts` only after the required Engineering and Security decision
authorizes an exclusion mechanism. The local file must not be deleted merely to
exclude it from a production artifact.

**Dependencies:** BEI-01 and review of the existing local-development contract.

**Proposed implementation approach:** Inventory manifest, application resources,
Flyway migrations, migration README, loader classes and indexes. Represent
first-party origin as unverified unless rights evidence exists. Design a production
resource-filtering rule that preserves explicit local development while excluding
`application-local.properties` from the production candidate.

**Acceptance criteria:**

- every non-library archive path belongs to an explicit category;
- all V1–V17 migrations are accounted for;
- loader and generated indexes are retained in the artifact boundary;
- migration README treatment is explicit; and
- if approved, the production executable excludes `application-local.properties`
  while local development remains testable.

**Verification commands or test categories:** Archive-resource inventory tests;
positive checks for mandatory resources; negative check for local-only configuration
in a production candidate; local-profile regression tests where applicable.

**Evidence outputs:** Resource inventory, classification results, exclusion result,
artifact digest and focused test evidence.

**Explicit approval prerequisites:** Engineering and Security confirmation is required
before modifying production resource packaging. Architecture review is required if
the packaging boundary changes materially.

**Stop conditions:** Stop if exclusion would delete or expose secrets, break local
development without an approved replacement, modify runtime semantics beyond
resource packaging, or proceed without Security confirmation.

## 9. BEI-04 — Digest-Bound Evidence Manifest

**Objective:** Bind source, build inputs, executable, inventory and reconciliation
evidence without selecting signing, storage or release infrastructure.

**Exact intended repository scope:** A project-owned logical manifest model and tests
within the approved backend verification boundary; no production application API,
database migration, credential store or deployment configuration.

**Dependencies:** Canonical outputs from BEI-01 through BEI-03.

**Proposed implementation approach:** Define a versioned deterministic model that
records source revision and working-tree state; build configuration and toolchain;
lock and verification-metadata digests; executable identity; archive and
reconciliation evidence identities; verification outcomes; optional notice/SBOM
references with explicit absent/not-applicable distinctions; and accountable review
references. Serialization, signing and storage remain unselected.

**Acceptance criteria:**

- every artifact-specific object names the executable digest;
- stale source, locks, configuration or artifact identity is rejected;
- evidence for a different digest cannot be reused;
- missing mandatory fields fail closed;
- credentials and secrets cannot appear in the model; and
- output is deterministic for identical inputs.

**Verification commands or test categories:** Unit tests for canonical ordering,
missing fields, stale inputs, digest mismatch, malformed evidence, secret-field
rejection and deterministic repeat output.

**Evidence outputs:** Logical schema, canonical fixture, fixture digest, verification
results and traceability matrix.

**Explicit approval prerequisites:** Engineering confirms the technical model;
Security confirms integrity and sensitive-data requirements; Platform/DevOps later
confirms retention and operational identity.

**Stop conditions:** Stop if implementation requires credentials, selects an
unapproved serialization/signing tool, persists evidence operationally, or implies
release authorization.

## 10. Negative and Fail-Closed Test Matrix

The following cases must be represented across BEI-01 through BEI-04:

| Failure | Required technical result |
| --- | --- |
| Missing packaged JAR | Reject inventory equality and reconciliation. |
| Unexpected packaged JAR | Reject until explicitly classified and evidenced. |
| Duplicate archive path or component mapping | Reject as ambiguous. |
| Filename match with digest mismatch | Reject identity. |
| Multiple coordinate matches | Reject as ambiguous. |
| Missing build-added origin | Reject completeness. |
| Omitted starter represented as contained | Reject relationship model. |
| BOM represented as executable JAR | Reject relationship model. |
| Missing application resource required by policy | Reject completeness. |
| Local configuration present after approved exclusion | Reject production candidate. |
| Executable digest differs from manifest subject | Reject all artifact-specific evidence. |
| Stale lock, build configuration or verification metadata | Reject evidence reuse. |
| Missing mandatory manifest field | Reject manifest. |
| Malformed archive or evidence | Reject deterministically without execution. |
| Notice or SBOM references another artifact | Reject association. |
| Missing accountable disposition | Keep release authorization blocked. |

These are proposed technical results, not active CI controls.

## 11. Development-Local Configuration Exclusion

The ENG-AB-03 planning direction is **AMEND**, meaning the implementation plan adopts
a narrower conditional position: prepare an exclusion design now, but do not change
production packaging until Engineering and Security confirm the exact rule.

The design must:

1. preserve `application-local.properties` for explicit local use;
2. prevent it from entering the governed production executable candidate;
3. avoid copying local database identities or configuration into another packaged
   file;
4. add a deterministic archive assertion;
5. document the effect on local commands and tests; and
6. preserve externalized production configuration and secret handling.

No checked-in secret value is asserted by this plan.

## 12. BEI-05 — Clean-Runner bootJar Reproducibility

**Objective:** Establish retained executable reproducibility evidence on a clean CI
runner without claiming cross-platform reproducibility.

**Exact intended repository scope:** `.github/workflows/backend-gradle-baseline.yml`
and the approved BEI-01/BEI-02 verification entry points only. No deployment job,
secret, environment approval or publishing step.

**Dependencies:** Accountable approval of the CI boundary, BEI-01 deterministic
inventory and BEI-02 reconciliation.

**Proposed implementation approach:** On the supported Java 21 runner, use the
committed wrapper, strict verification and committed locks; build `bootJar` twice in
equivalent clean workspaces; compare bytes; inventory and reconcile the resulting
artifact; retain bounded check results according to later approved retention policy.
Do not disable verification or refresh dependencies.

**Acceptance criteria:**

- both executable builds succeed under strict verification;
- output filenames, sizes and digests match;
- byte comparison succeeds;
- both inventory outputs and reconciliations match;
- local-only configuration behavior follows the approved decision; and
- failure blocks the verification job but does not imply a production release gate.

**Verification commands or test categories:** Existing wrapper validation, strict
`bootJar` builds, byte comparison, inventory verification, reconciliation verification
and negative workflow review. Exact commands must follow the approved workflow.

**Evidence outputs:** Runner/toolchain identity, source revision, input digests, both
artifact digests, byte-comparison result, inventory/reconciliation digests and job
result.

**Explicit approval prerequisites:** Engineering must approve the amended
reproducibility requirement; Security and Platform/DevOps must approve the runner,
network, provenance and retention boundaries before representing the result as
governed evidence.

**Stop conditions:** Stop on dependency refresh, lock mutation, verification bypass,
unapproved artifact upload, secret requirement, nondeterminism or claims of
independent/cross-platform reproducibility not established by the run.

## 13. BEI-06 — Technical Notice-Input Coverage

**Objective:** Produce complete, traceable technical inputs for later qualified Legal
review and notice generation, without deciding required delivered content.

**Exact intended repository scope:** The approved executable verifier and a bounded
evidence model/tests; read-only use of existing POM, parent-POM, embedded license,
notice, attribution and nested-material evidence. No `LICENSE` change and no release
notice publication.

**Dependencies:** BEI-02 classification and BEI-04 artifact binding.

**Proposed implementation approach:** Associate every packaged and build-added
component with exact evidence status; preserve compound alternatives, inherited
declarations, publisher wording and nested attribution; record missing or ambiguous
evidence explicitly; bind the resulting input index to the executable digest.

**Acceptance criteria:**

- all 62 packaged components have an evidence status;
- build-added jarmode license and notice evidence is included;
- nested attribution is not flattened or discarded;
- missing conventional resources do not become unsupported license conclusions;
- the index is deterministic and artifact-bound; and
- no Legal compatibility or sufficiency result is generated.

**Verification commands or test categories:** Coverage equality tests, compound and
nested evidence fixtures, missing/ambiguous evidence tests, deterministic output and
artifact-digest mismatch tests.

**Evidence outputs:** Technical notice-input index, source mappings, unresolved
evidence list, index digest and verification result.

**Explicit approval prerequisites:** Engineering confirms technical completeness;
qualified Legal/licensing defines required content; Security reviews evidence
integrity; Platform/DevOps later governs generation, retention and delivery.

**Stop conditions:** Stop on license inference, compatibility claims, notice
sufficiency claims, alteration of publisher text, or selection of unapproved tooling.

## 14. BEI-07 — Artifact-Specific SBOM Inventory Design

**Objective:** Define and later implement an artifact-specific inventory capable of
representing physical content and non-contained dependency relationships accurately.

**Exact intended repository scope:** Initially this plan and a separately reviewed
tool/format evaluation. Later implementation scope remains unset until dependency,
provenance, license, Security and Platform/DevOps admission is complete.

**Dependencies:** BEI-02 reconciliation, BEI-04 manifest model and an approved SBOM
format/tool decision.

**Proposed implementation approach:** Make the exact executable the subject; represent
61 packaged locked JARs and one build-added JAR as physical contents; represent ten
starters as resolved-not-packaged and two BOMs as dependency-management metadata;
retain Loader Tools as a build input/source-container relationship; bind the SBOM
digest and subject to the executable.

**Acceptance criteria:**

- physical inventory equals all 62 packaged JARs;
- no omitted starter or BOM is described as contained;
- the build-added component and source-container relationship are explicit;
- application content is represented without invented package identity;
- subject and component digests reconcile to the executable; and
- output validation fails on missing, unknown, stale or mismatched content.

**Verification commands or test categories:** Schema validation for the later approved
format; archive-to-SBOM equality; relationship tests; digest verification; malformed,
missing and unexpected component tests.

**Evidence outputs:** Candidate SBOM, SBOM digest, subject binding, reconciliation
report and validation results. No output is production evidence until retained and
approved.

**Explicit approval prerequisites:** Tool and format admission; Engineering,
Security and Platform/DevOps dispositions; qualified Legal review where applicable.

**Stop conditions:** Stop if no format can preserve the required distinctions, if a
new dependency lacks admission, if output cannot bind to the executable, or if the
work is represented as completing Gate 7 prematurely.

## 15. CI Integration Boundaries

CI changes are not authorized by this plan. A later reviewed change may add bounded
verification only after the relevant increment passes locally and its owner
prerequisites are satisfied. The future CI boundary must:

- retain least-privilege read permissions;
- use the committed wrapper, locks and strict dependency verification;
- avoid dependency refreshes and automatic fixes;
- execute no application or deployment;
- expose no credentials or production secrets;
- distinguish technical verification failure from release authorization;
- fail on inventory, reconciliation, digest or reproducibility mismatch as approved;
- avoid publishing artifacts until retention and access controls are approved; and
- preserve the existing ordinary-JAR and dependency checks unless separately changed
  through review.

## 16. Retention and Evidence Requirements

The future retained evidence set should include:

- source revision and working-tree state;
- build configuration, wrapper, Java and runner identity;
- lockfile and verification-metadata digests;
- exact executable filename, size and digest;
- archive inventory and digest;
- reconciliation report and digest;
- resource inventory and policy result;
- Loader Tools outer, nested and packaged identity chain;
- notice-input index and digest when implemented;
- SBOM identity, subject and digest when implemented;
- verification outcomes and timestamps;
- accountable review references; and
- retention location and access-control evidence.

Storage, retention period, signing, attestation and promotion mechanisms remain
Platform/DevOps and Security decisions. Local ignored build output is not retained
governed evidence.

## 17. Engineering Acceptance Criteria

Engineering implementation acceptance requires:

1. exact executable identity and complete deterministic archive inventory;
2. exact physical-to-resolved reconciliation with no unexplained difference;
3. explicit build-added, omitted and dependency-management classifications;
4. complete application-resource and loader inventory;
5. decided and enforced local-configuration treatment;
6. digest-bound, stale-resistant evidence relationships;
7. deterministic fail-closed negative tests;
8. approved reproducibility evidence at the agreed assurance level;
9. complete technical notice-input status for every packaged component;
10. artifact-specific SBOM design capable of preserving all relationships;
11. no dependency, tool or CI boundary introduced without its required review; and
12. retained evidence sufficient for accountable review.

Meeting technical criteria does not itself record accountable Engineering approval
or complete a DEC-0047 gate.

## 18. Security Dependencies

Security must decide or confirm:

- exclusion and verification of development-local configuration;
- sufficiency of the Loader Tools integrity chain and any publisher authentication;
- provenance and evidence-integrity requirements;
- fail-closed conditions and vulnerability-gate interaction;
- admission of any future notice, SBOM, signing or scanning tool;
- handling of secrets and sensitive build data;
- evidence access, tamper protection, incident handling and revocation; and
- whether independent-builder or stronger reproducibility evidence is required.

Security disposition remains **PENDING**.

## 19. Platform/DevOps Dependencies

Platform/DevOps must decide or confirm:

- protected runner and automation identity;
- network and cache policy for trusted builds;
- immutable artifact and evidence storage;
- retention period and retrieval evidence;
- notice and SBOM generation operations;
- evidence association through promotion and delivery;
- failure, retry and recovery behavior; and
- the operational relationship between an application JAR and any later container.

Platform/DevOps disposition and Gate 7 remain **PENDING** and **INCOMPLETE**.

## 20. Architecture Dependencies

Architecture must confirm the candidate executable application boundary is compatible
with the canonical one-deployable Spring Boot baseline and decide whether a future
container or wrapper becomes an additional or superseding release-evidence subject.
Any material deployment or packaging strategy change must follow applicable ADR
governance. Architecture disposition remains **PENDING**.

## 21. Qualified Legal Dependencies

Qualified Legal/licensing must determine:

- compatibility with eventual outbound terms and distribution context;
- treatment of compound, alternative and inherited declarations;
- required publisher and nested attribution text;
- required backend notice content;
- notice placement and delivery obligations; and
- whether generated evidence is legally sufficient.

This plan makes no Legal determination. Qualified Legal/licensing disposition remains
**PENDING**.

## 22. DEC-0047 Gate Mapping

| Gate | Contribution of this plan | Remaining requirement | Status |
| --- | --- | --- | --- |
| Gate 5 — Engineering artifact inventory | Defines ordered, testable increments for executable inventory, reconciliation, resources, manifest and technical completeness. | Accountable Engineering disposition, implementation and retained verified evidence. | **INCOMPLETE** |
| Gate 7 — Platform/DevOps notice and SBOM plan | Identifies CI, retention, notice and SBOM operational dependencies without selecting mechanisms. | Accountable Platform/DevOps plan, admitted tools/formats, implementation, retention, delivery and verification. | **INCOMPLETE** |
| All other gates | Creates no rights-holder, licensing, Legal, Architecture, Security or final-decision approval. | All requirements in DEC-0047 remain outstanding. | **INCOMPLETE; unchanged** |

No gate becomes **MET** through founder authorization, this plan or later technical
implementation alone.

## 23. Risks and Unresolved Decisions

| Risk or uncertainty | Current treatment |
| --- | --- |
| Founder direction mistaken for accountable approval | Explicitly prohibited; every accountable response remains pending. |
| Local configuration shipped unintentionally | Conditional exclusion design and Security gate in BEI-03. |
| Physical artifact differs from locked graph | Deterministic reconciliation and fail-closed classifications in BEI-02. |
| Build-added content escapes inventory | Mandatory build-added classification and origin chain. |
| Same-machine reproducibility overclaimed | Clean-runner proposal remains unimplemented; independent/cross-platform level unresolved. |
| Evidence reused for different bytes | Digest binding and stale-evidence rejection in BEI-04. |
| Custom verification logic becomes security-sensitive debt | Bounded implementation, negative tests and accountable review required. |
| Notice evidence mistaken for Legal sufficiency | Technical inputs and Legal decisions remain separate. |
| SBOM tool or format selected prematurely | BEI-07 stops until admission and owner decisions exist. |
| CI result mistaken for release authorization | CI boundary expressly excludes release authorization and deployment. |
| Retention or promotion mechanism absent | Platform/DevOps decision remains pending. |
| Rights-holder or outbound terms inferred | Rights-holder authority remains not verified and outbound license unresolved. |

## 24. References

- [Backend Executable Engineering Disposition Request](backend-executable-engineering-disposition.md)
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

## 25. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Created an incremental, non-authoritative Engineering implementation plan for the backend executable boundary; mapped all twelve pending decisions, ordered seven bounded increments, preserved cross-owner approvals and kept DEC-0047 Gates 5 and 7 incomplete and production release not granted. |
