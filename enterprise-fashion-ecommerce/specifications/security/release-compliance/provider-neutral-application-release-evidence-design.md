---
title: Provider-Neutral Application Release-Evidence Design
version: 0.1.0
status: Draft
owner: Platform/DevOps — accountable disposition pending
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Provider-Neutral Application Release-Evidence Design

## 1. Purpose and Scope

This Draft proposes a provider-neutral design for binding an application release
artifact to its dependency inventory, third-party notices, software bill of materials
(SBOM), provenance, verification results and accountable release dispositions. It is
a technical proposal for review, not an implemented release control or release record.

The design explicitly excludes:

- production deployment or release authorization;
- selection of an outbound repository license or distribution model;
- verification of rights-holder or licensing authority;
- qualified Legal/licensing approval;
- selection of a cloud, hosting, registry, deployment or CI/CD provider;
- implementation or modification of CI/CD, release or retention controls; and
- admission of an SBOM, notice, signing, scanning or other dependency tool.

## 2. Governance and Authority

| Governance area | Current status |
| --- | --- |
| [DEC-0047](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md) | **PROPOSED; non-authoritative** |
| Single-merchant hosted model | **RECOMMENDED; NOT APPROVED** |
| Rights-holder authority | **NOT VERIFIED** |
| Repository outbound license | **UNRESOLVED** |
| Distribution model | **UNRESOLVED** |
| Qualified Legal/licensing disposition | **PENDING** |
| Engineering disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Production release authorization | **NOT GRANTED** |

Four states remain distinct:

1. a **technical proposal** describes a possible control;
2. **evidence verification** establishes bounded facts about a specific input or
   artifact;
3. **accountable approval** records an authorized, attributable disposition within
   its scope; and
4. **release authorization** permits a defined release unit to enter an approved
   distribution context.

No earlier state implies a later one. A reviewed or merged Draft is not accountable
approval, and accountable approval of one concern is not production release
authorization.

## 3. Evidence Baseline

The following SHA-256 values were recalculated from local repository files on
2026-11-20 at commit `8d2a43acc88f2c32ab90d3d398e0441095813e7f` before this Draft was
created. They match the historical ARCH-LIC-M2D.5A observations.

| Evidence input | SHA-256 |
| --- | --- |
| Frontend `package-lock.json` | `5d3b5308c9c8b7e0570fa31b99328bd95b27c97dce1834180df433739b4caea7` |
| Frontend `package.json` | `4a0f1f35fbb17e91fa13f4ce41a8d9b1b4574fa15f49f2b3af30698218116fdb` |
| Frontend `angular.json` | `5ecb13d4558872743c0f7ac0e7d06380bafd290a4f6879cd3428e2c7ae7786cc` |
| Backend `gradle.lockfile` | `34a91e1cb6e77c2cadff10b02d874ca18febb7b9fecfaa8629bfc270a8c8b5c8` |
| Backend `build.gradle.kts` | `20095f7d6b15f31b04ab06f13ae6de1705d5eefd15ac423a52500e141694e1ad` |
| Backend `settings.gradle.kts` | `a2dd0b63db48fde3ddef17fd018c40b5e7d80f46e9d0d0c5b87d1d0968854774` |
| Backend verification metadata | `451a2e91b79952754ccba5793eb5463c008fc167b95d58540d231161c289b6f7` |

These are historical assessment values, not immutable release identifiers. They do
not establish that a release was built, reviewed, approved, retained or distributed.
Any later evidence generation must recalculate the inputs and bind them to the exact
release-unit revision.

## 4. Release-Unit Candidates

### 4.1 Angular Browser Application

| Concern | Observed or proposed boundary |
| --- | --- |
| Build boundary | Angular production browser output produced from the frontend workspace. |
| Source inputs | Frontend source, `package.json`, `package-lock.json`, `angular.json`, TypeScript configuration and build tooling. |
| Dependency scope | Locked production npm graph plus build-resolved code actually included in browser output. |
| Bundled resources | Generated JavaScript, Cascading Style Sheets (CSS), HyperText Markup Language (HTML) and generated third-party-license output. |
| Current evidence | Exact manifest/lock/configuration hashes; locked production dependency evidence; observed production build and `3rdpartylicenses.txt`. |
| Missing evidence | Retained artifact digest, build-aware inventory reconciliation, retained notice, SBOM, provenance, accountable dispositions and release authorization. |
| Intended context | Browser assets in the recommended single-merchant hosted model; this context is not approved. |
| Unresolved decisions | Outbound terms, delivery obligations, artifact boundary, SBOM profile, notice delivery, retention and release authority. |

### 4.2 Spring Boot Executable `bootJar`

| Concern | Observed or proposed boundary |
| --- | --- |
| Build boundary | A Spring Boot executable archive that the applied Spring Boot Gradle plugin is capable of producing. |
| Source inputs | Backend source and resources, `build.gradle.kts`, `settings.gradle.kts`, `gradle.lockfile`, verification metadata and Java 21 toolchain. |
| Dependency scope | Locked production runtime coordinates actually included beneath `BOOT-INF/lib`. |
| Bundled resources | Application classes, properties, Flyway migrations, migration README and runtime dependency JARs. |
| Current evidence | 73-coordinate runtime licensing inventory; strict dependency verification configuration; reproducible ordinary-JAR CI check. |
| Missing evidence | Observed executable artifact, `bootJar` reproducibility, `BOOT-INF/lib` reconciliation, manifest inspection, artifact digest, backend notice and backend SBOM. |
| Intended context | Privately operated backend service under the recommended model; this context is not approved. |
| Unresolved decisions | Executable-JAR versus image release unit, resource exclusions, distribution channel, notices, SBOM, retention and release authority. |

The ordinary JAR currently checked by backend CI is not the executable Spring Boot
`bootJar`. Evidence for one must not be reused as evidence for the other. No container
image is assumed or proposed as an implemented release unit.

## 5. Non-Package Third-Party Material

The classifications below describe the observed repository/build baseline. Because
rights-holder evidence is unresolved, repository-authored material is not classified
as first-party verified.

| Area | Material | Classification | Release-evidence implication |
| --- | --- | --- | --- |
| Frontend | Application source | **ORIGIN NOT VERIFIED** | Rights and contribution provenance remain unresolved. |
| Frontend | Generated JavaScript, CSS and HTML | **ORIGIN NOT VERIFIED / THIRD-PARTY IDENTIFIED** | Mixed output requires build-aware dependency and source association. |
| Frontend | Bundled image files | **NOT FOUND** | Re-evaluate on asset changes. |
| Frontend | SVG or icon assets | **NOT FOUND** | Re-evaluate on asset changes. |
| Frontend | Bundled font files | **NOT FOUND** | Re-evaluate on asset changes. |
| Frontend | Runtime-loaded Product media | **NOT DISTRIBUTED** with the observed browser build | Governed Product content remains a separate runtime concern. |
| Frontend | Test-only accessibility fixtures | **NOT DISTRIBUTED** | Exclude from production release inventory while retaining test provenance. |
| Frontend | Vendored JavaScript | **NOT FOUND** | Re-evaluate on source or build changes. |
| Backend | Application classes | **ORIGIN NOT VERIFIED** | Rights and contribution provenance remain unresolved. |
| Backend | `application.properties` | **ORIGIN NOT VERIFIED** | Record exact inclusion and inspect for release suitability. |
| Backend | `application-local.properties` | **ORIGIN NOT VERIFIED** | Requires Engineering and Security review before release packaging. |
| Backend | Flyway V1–V17 migrations | **ORIGIN NOT VERIFIED** | Bind exact migration resources to the executable artifact. |
| Backend | Migration README | **ORIGIN NOT VERIFIED** | Confirm whether inclusion is intended. |
| Backend | Runtime dependency JARs | **THIRD-PARTY IDENTIFIED** | Reconcile the exact executable inventory with licensing evidence. |
| Backend | Development Compose and Keycloak assets | **NOT DISTRIBUTED** with the candidate application artifact | No production container or infrastructure unit is inferred. |
| Backend | Static or template assets | **NOT FOUND** | Re-evaluate on resource changes. |

No observed item is classified **FIRST-PARTY VERIFIED**. That classification requires
rights evidence unavailable to this Draft.

## 6. Frontend Notice Design

The Angular production builder generated
`frontend/dist/storefront/browser/3rdpartylicenses.txt` in the observed build. The
file contained eight package sections, while the locked production npm graph contained
nine package entries. `@angular/compiler` was absent from the generated notice. The
NgRx section preserved nested `rxjs-etc` attribution.

The generated output is ignored by Git, is not retained by current frontend CI and
has no approved delivery mechanism. Package-count equality is not an adequate
completeness rule because a locked production dependency can be build-time-only or
absent from the emitted bundle, while one bundled package can contain nested copied
material requiring additional attribution.

A future control should reconcile:

1. the locked production graph;
2. the exact code and assets emitted into the browser release unit;
3. generated license sections and nested attribution;
4. explained exclusions, such as a dependency not present in emitted output; and
5. the notice digest and exact browser artifact digest.

This build-aware reconciliation is a design proposal, not implementation and not a
claim that the generated notice is legally sufficient.

## 7. Frontend SBOM Design

The installed npm baseline exposes commands capable of producing SPDX and CycloneDX
SBOM output and supports production dependency scoping. No format is approved and no
governed frontend SBOM is currently generated.

A future evaluation must distinguish:

- a lockfile-derived production dependency SBOM from a bundled-content inventory;
- dependencies installed for production from code actually emitted into the browser;
- package components from non-package code, generated material and runtime Product
  media;
- reproducibility of component identity, relationships and hashes; and
- association of the SBOM subject with the exact browser artifact digest, source
  commit and lockfile digest.

Candidate evaluation may compare SPDX and CycloneDX without selecting either. It
must also determine whether package-lock-only generation is sufficient, how peer and
optional relationships are represented, and how generated output is retained and
verified.

## 8. Backend Notice Design

The [backend production runtime license evidence](../dependency-admission/backend-production-runtime-license-evidence.md)
records:

- 73 locked production runtime coordinates;
- 71 runtime JARs and two Bill of Materials (BOM) components;
- 12 effective declarations inherited from parent POMs;
- 51 JARs with conventional embedded license resources;
- 39 JARs with `NOTICE`, `DEPENDENCIES` or attribution resources; and
- 20 JARs with neither conventional resource category.

The categories overlap and must not be added as mutually exclusive counts. A future
backend notice mechanism must preserve compound alternatives, parent-POM provenance,
nested third-party attributions and publisher-specific wording. It must not reduce
the Bouncy Castle wording or other publisher text to an unsupported SPDX or Legal
conclusion. It must also preserve observed differences between POM declarations and
distributed JAR evidence.

The existing inventory is detailed technical evidence. It is not a generated release
notice, compatibility approval, retention mechanism or delivery control.

## 9. Backend SBOM Design

Future review should compare the following provider-neutral candidates without
selecting or admitting one:

| Candidate | Potential evidence | Principal limitation or decision |
| --- | --- | --- |
| Existing Gradle dependency evidence | Locked coordinates and resolution metadata already governed by the build. | Does not by itself describe the built executable or provide a standardized release SBOM. |
| Project-owned conversion logic | Could transform locked data into a chosen schema. | Creates custom security-sensitive maintenance and completeness obligations. |
| Admitted Gradle SBOM plugin | Could integrate standardized generation with the build graph. | Requires dependency, provenance, maintenance and Security admission. |
| Admitted external artifact scanner | Could inspect the built executable independently. | Requires tool admission, artifact access, format policy and completeness validation. |
| Lockfile-to-`bootJar` reconciliation | Could compare governed intent with `BOOT-INF/lib` reality. | Requires an actual executable artifact and deterministic component matching. |

No candidate is authorized. An eventual design may require complementary generation
and artifact inspection rather than relying on a single source.

## 10. Executable Backend Artifact

The backend applies the Spring Boot Gradle plugin, uses a Java 21 toolchain, sets
archive timestamps off and reproducible file order on, and has a Spring Boot main
application entry point. A `bootJar` would normally contain application classes,
runtime dependencies under `BOOT-INF/lib`, `application.properties`,
`application-local.properties`, Flyway V1–V17 migrations and the migration README.

The current evidence does not verify:

- an executable backend artifact;
- reproducible `bootJar` output;
- the exact `BOOT-INF/lib` inventory;
- executable manifest contents;
- a retained executable artifact digest;
- a backend release notice; or
- a backend SBOM.

Backend CI verifies reproducibility of an ordinary JAR only. That check cannot be
represented as executable Spring Boot release evidence.

## 11. Release-Evidence Manifest

### 11.1 Proposed Logical Schema

| Field group | Proposed content | Candidate requirement |
| --- | --- | --- |
| Schema | Schema name and version | Mandatory |
| Evidence identity | Unique evidence identifier | Mandatory; identifier semantics require review |
| Source | Repository identity, exact commit and working-tree state | Mandatory |
| Build unit | Build-unit identifier and release-unit type | Mandatory |
| Artifact | Filename, byte size, cryptographic digest and digest algorithm | Mandatory |
| Build configuration | Relevant configuration-file identities and digests | Mandatory |
| Toolchain | Build tool, runtime and relevant tool versions | Mandatory |
| Locks | Exact lockfile identities and digests | Mandatory |
| Inventory | Dependency inventory identity, scope, format and digest | Mandatory |
| Notice | Notice identity, scope and digest | Conditionally mandatory when the approved release context requires it; policy unresolved |
| SBOM | SBOM identity, format, version, subject and digest | Conditionally mandatory; format and policy unresolved |
| Provenance | Provenance identity, subject, verification result and verifier | Conditionally mandatory; mechanism unresolved |
| Verification | Named checks, input identity, outcome and timestamp | Mandatory |
| Reviews | References to accountable review and disposition evidence | Mandatory for authorization; owners and form unresolved |
| Generation | Generation timestamp and automation identity | Mandatory; no signing identity selected |
| Retention | Reference to retained artifact/evidence location | Mandatory for release; technology and period unresolved |
| Distribution context | Approved context classification and decision reference | Mandatory for release |

The manifest must distinguish an absent conditional field from a field determined not
applicable by an approved policy. No retention period, signing identity, identifier
authority, SBOM format or distribution context is selected by this Draft.

### 11.2 Manifest Status

The schema is logical, not serialized. JSON, another representation, a signing
envelope and storage technology remain unresolved. A manifest instance must not
contain credentials, secrets, tokens or private signing material.

## 12. Evidence-Binding Invariants

The proposed design requires:

1. one manifest per exact release-unit revision;
2. exact association to the artifact filename, size and cryptographic digest;
3. no evidence reuse when artifact digests differ;
4. rejection of evidence generated from stale source, locks or build configuration;
5. rejection when mandatory evidence is missing, malformed or unverifiable;
6. explicit separation of technical verification from accountable approval;
7. explicit separation of accountable approval from release authorization;
8. exclusion of credentials and secrets from evidence and logs;
9. explicit representation of unresolved, unavailable and not-applicable states;
10. exact release-unit type and distribution-context binding; and
11. preservation of immutable evidence relationships across retention and review.

## 13. Fail-Closed Control Matrix

| Condition | Existing enforcement | Proposed future behavior | Accountable owner | Current status |
| --- | --- | --- | --- | --- |
| Missing artifact | Frontend build and backend ordinary-JAR CI steps fail when their expected output is absent. | Reject release evidence and authorization for the missing release unit. | Engineering / Platform/DevOps | **PARTIAL; not a release gate** |
| Missing notice | None for retained release evidence. | Reject when an approved policy requires a notice. | Legal/licensing / Platform/DevOps | **NOT IMPLEMENTED** |
| Missing SBOM | None. | Reject when the approved release policy requires an SBOM. | Security / Platform/DevOps | **NOT IMPLEMENTED** |
| Missing inventory | Lock and dependency checks exist but are not artifact-bound release inventories. | Reject release evidence lacking the required exact inventory. | Engineering | **PARTIAL; not artifact-bound** |
| Stale evidence | Lock consistency checks exist; no complete manifest freshness check exists. | Reject on source, lock, configuration or artifact mismatch. | Engineering / Platform/DevOps | **NOT IMPLEMENTED as release control** |
| Digest mismatch | No cross-evidence release-manifest comparison exists. | Reject every mismatched artifact or evidence digest. | Platform/DevOps / Security | **NOT IMPLEMENTED** |
| Wrong release-unit association | Ordinary JAR and browser build checks are separate but no manifest binds their types. | Reject evidence naming another unit, revision or artifact. | Architecture / Platform/DevOps | **NOT IMPLEMENTED** |
| Unverified provenance | Dependency verification exists; release-artifact provenance is absent. | Reject release authorization when required provenance is unavailable or invalid. | Security | **PARTIAL dependency control only** |
| Missing owner review | No technical build step proves accountable disposition. | Reject authorization until every required scoped disposition is present. | Repository Owner / relevant owner | **NOT IMPLEMENTED** |
| Missing release authorization | No production authorization mechanism was found. | Reject distribution without exact, attributable release authorization. | Repository Owner / Platform/DevOps | **NOT IMPLEMENTED** |
| Open blocking vulnerability | Production npm audit and bounded dependency review exist; policy coverage is incomplete. | Reject according to an approved severity, applicability and exception policy. | Security | **PARTIAL; policy unresolved** |
| Unaccounted third-party material | Package evidence exists; non-package reconciliation is absent. | Reject when shipped material lacks origin, license or approved disposition. | Engineering / Legal/licensing | **NOT IMPLEMENTED** |

Every future behavior in this table is proposed. The table does not claim that a
production release gate currently exists.

## 14. Provider-Neutral Controls

The following proposed controls do not depend on Azure, Docker, a particular
container registry, CI/CD vendor or deployment infrastructure:

- cryptographic hashing of artifacts and evidence;
- exact source, lockfile and build-configuration association;
- release-unit type and revision identity;
- dependency inventory and emitted-content reconciliation;
- notice and SBOM identity, subject and digest binding;
- provenance recording and independent verification result capture;
- explicit evidence relationships and stale-evidence rejection;
- accountable disposition and release-authorization references;
- evidence retention requirements expressed independently of storage technology; and
- deterministic fail-closed rules for absent, mismatched or unresolved evidence.

## 15. Deferred Deployment Decisions

This Draft does not select:

- executable backend JAR versus container image as the production unit;
- hosting or cloud provider;
- artifact repository or container registry;
- signing, attestation or key-custody mechanism;
- evidence-retention technology or period;
- browser notice presentation or delivery method;
- environment-promotion workflow;
- deployment identity or credentials;
- rollback mechanism; or
- runtime image, artifact or workload scanning.

Each decision requires evidence from the eventual operating environment and the
appropriate accountable review.

## 16. Review-Readiness Matrix

| Owner | Evidence available | Open questions | Required disposition | Status |
| --- | --- | --- | --- | --- |
| Engineering | Frontend locks/build evidence; backend 73-coordinate inventory; candidate release boundaries. | Exact artifact inventory, `bootJar` contents, completeness algorithms and implementation feasibility. | Confirm technical correctness and implementability. | **PENDING** |
| Security | Strict dependency controls, provenance evidence, advisory triage and proposed fail-closed matrix. | Provenance policy, vulnerability gate, evidence integrity, signing, access and incident behavior. | Confirm supply-chain and release-control requirements. | **PENDING** |
| Platform/DevOps | Current CI behavior, candidate manifest and provider-neutral retention relationships. | Artifact store, retention, promotion, automation identity, operational failure and recovery. | Confirm operable evidence lifecycle and release gating. | **PENDING** |
| Architecture | Approved deployment baseline and two candidate application release units. | Final unit boundaries and whether deployment choices trigger an ADR. | Confirm compatibility and required architecture governance. | **PENDING** |
| Qualified Legal/licensing | Frontend and backend license inventories and notice observations. | Rights, compatibility, required content, delivery channels and sufficiency of notices. | Provide qualified licensing and notice disposition. | **PENDING** |

## 17. DEC-0047 Gate Mapping

| DEC-0047 gate | How this Draft informs the gate | Status after this Draft |
| --- | --- | --- |
| 1. Verified rights holder | Explicitly preserves the unresolved origin and authority boundary. | **INCOMPLETE** |
| 2. Confirmed commercial operating model | Uses the single-merchant hosted model only as a recommendation. | **INCOMPLETE** |
| 3. Qualified Legal disposition | Presents notice, inventory and delivery questions for review. | **INCOMPLETE** |
| 4. Architecture compatibility | Defines candidate units and deferred architecture decisions. | **INCOMPLETE** |
| 5. Engineering artifact inventory | Consolidates current unit and non-package observations and identifies missing executable evidence. | **INCOMPLETE; materially informed** |
| 6. Security review | Proposes integrity and fail-closed controls without recording a disposition. | **INCOMPLETE** |
| 7. Platform/DevOps notice and SBOM plan | Supplies a reviewable provider-neutral design baseline. | **INCOMPLETE; materially informed** |
| 8. Reviewed `LICENSE` migration | Makes no license selection or migration proposal. | **INCOMPLETE** |
| 9. Explicit final decision | Makes no selection and does not revise DEC-0047. | **INCOMPLETE** |
| 10. Recorded accountable approvals | Records every disposition as pending. | **INCOMPLETE** |

Writing this Draft does not make any DEC-0047 acceptance gate **MET**.

## 18. Implementation and Release NO-GO

This Draft must not be treated as authorization to:

- modify `LICENSE` or select outbound terms;
- accept or promote DEC-0047;
- admit `@ngrx/signals@20.1.0`;
- accept ADR-0024 or promote `ANGULAR.md`;
- close, waive or accept any vulnerability finding;
- install or admit SBOM, notice, signing or scanning tooling;
- change release CI, retention or deployment configuration; or
- deploy or distribute production software.

Implementation and production release remain **NO-GO**.

## 19. Open Questions and Next Evidence

The smallest prioritized technical investigations are:

1. produce a read-only, reproducible inventory specification for the Angular browser
   output that explains locked-but-unbundled packages and nested attribution;
2. inspect a locally built executable `bootJar` only under separately authorized
   validation, recording its manifest, resources, `BOOT-INF/lib` coordinates and
   reproducibility without treating it as a release;
3. evaluate existing npm SBOM output offline against the exact lockfile and browser
   artifact, without selecting a format or changing dependencies;
4. prototype, outside production CI and only when authorized, a manifest fixture that
   binds synthetic artifact and evidence digests without credentials or signing; and
5. obtain owner review of the mandatory-versus-conditional manifest fields and
   fail-closed matrix before implementation design proceeds.

Open policy questions include notice and SBOM applicability by release context,
retention period, artifact identity, provenance mechanism, vulnerability threshold,
review expiry, exception handling and final release authority.

## 20. References

- [DEC-0047 — Repository Outbound Licensing and Distribution Model](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md)
- [Backend Production Runtime Dependency License Evidence](../dependency-admission/backend-production-runtime-license-evidence.md)
- [NgRx Signal Store 20.1.0 License Evidence](../dependency-admission/ngrx-signals-20-1-0-license-evidence.md)
- [NgRx Signal Store 20.1.0 Provenance Evidence](../dependency-admission/ngrx-signals-20-1-0-provenance-evidence.md)
- [Frontend Development-Tooling Vulnerability Triage](../vulnerability-triage/frontend-development-tooling-advisories.md)
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md)
- [Architecture](../../../.ai/core/ARCHITECTURE.md)
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [Frontend package manifest](../../../frontend/package.json)
- [Frontend package lock](../../../frontend/package-lock.json)
- [Angular build configuration](../../../frontend/angular.json)
- [Frontend CI workflow](../../../../.github/workflows/frontend-angular-baseline.yml)
- [Backend build configuration](../../../backend/build.gradle.kts)
- [Backend dependency lock](../../../backend/gradle.lockfile)
- [Backend dependency verification metadata](../../../backend/gradle/verification-metadata.xml)
- [Backend CI workflow](../../../../.github/workflows/backend-gradle-baseline.yml)
- [ADR-0024 — Frontend Feature-State Management Strategy](../../adr/ADR-0024-frontend-feature-state-management-strategy.md)
- [Angular Standards](../../../.ai/frontend/ANGULAR.md)

## 21. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Created a provider-neutral, non-authoritative application release-evidence design; recorded verified technical baselines, candidate frontend and backend release units, notice and SBOM design considerations, manifest invariants, fail-closed proposals, pending owner reviews and explicit implementation and release NO-GO boundaries. |
