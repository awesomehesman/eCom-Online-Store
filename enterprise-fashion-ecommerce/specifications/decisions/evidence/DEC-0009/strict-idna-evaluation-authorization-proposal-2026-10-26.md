# DEC-0009 — Fresh bounded strict-IDNA2008 evaluation authorization record

- Version: 0.4.0.
- Status: Technical plan approved; bounded environment verification authorized; candidate evaluation withheld.
- Decision date: 2026-10-26.
- Accountable decision-maker: repository owner, acting separately in Identity, Security, Architecture and Engineering capacities.
- Date: 2026-10-26 (Africa/Johannesburg).
- Owner: Identity.
- Authoritative: false.
- Repository: awesomehesman/eCom-Online-Store, original workspace only.
- Preparation branch: `docs/dec-0009-strict-idna-evaluation-authorization`.
- Preparation baseline: `90358f8ab3bdd058a6e89d4ed91081abee304e3c`.
- Evaluation execution: NOT STARTED; no candidate artifacts acquired or probes run by this proposal.

## 1. Purpose and investigation question

Can this exact composition demonstrate the complete DEC-0005 strict IDNA2008 profile with a supportable public codec, coherent Unicode data, safe resource behavior and explicit maintenance ownership?

This record documents the repository owner’s explicit approval on 2026-10-26 of one new, separately bounded evidence-gathering evaluation in four distinct accountable capacities, as recorded in §9. The approval provenance is the repository owner’s explicit instruction in this task; no signature or independent reviewer is invented. This is evaluation authorization only, not Decision Record acceptance, dependency admission or implementation selection. The remaining §6 technical pre-execution gates are incomplete; Decisions A and B in §10 record platform direction and accepted maintenance ownership: candidate evaluation must not begin yet. The separately authorized environment-verification exception in §10C permits only its specified activities subject to integrity, licensing and containment gates. Reviewing or merging this record does not complete those gates. DEC-0009 remains version 0.8.0 Proposed and non-authoritative; this record changes neither it nor the Decision Index.

## 2. Governing sources and evidence traceability

| Reference | Source and controlling concern |
| --- | --- |
| G1 | [DEC-0009 v0.8.0](../../DEC-0009-customer-login-idna2008-implementation-strategy.md): strict evidence obligations, exhausted authorizations, ownership, separate acceptance/admission/implementation gates. |
| G2 | [Accepted DEC-0005](../../DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md): Input Boundary; Case and Domain Semantics; Logical Uniqueness; Failure and Uncertainty; compatibility and ownership. |
| G3 | [Accepted DEC-0001](../../DEC-0001-backend-build-tool-dependency-management.md): fixed versions, dependency admission, locks, reviewed verification, reproducibility and build compatibility. |
| G4 | [Security Standards](../../../../.ai/core/SECURITY-STANDARDS.md), especially §§23–26 and 30: runtime containment, verified provenance, dependency maintenance and threat modelling. |
| G5 | [Architecture](../../../../.ai/core/ARCHITECTURE.md), especially Architecture governance and Identity boundaries: Java 21, owner isolation, material change assessment and Accepted ADR/canonical synchronization. |
| G6 | [AGENTS](../../../../.ai/core/AGENTS.md), [Documentation Standards](../../../../.ai/core/DOCUMENTATION-STANDARDS.md) and [Decision governance](../../../../.ai/core/DECISIONS.md): scope, truthful evidence, review, lifecycle and authority. |
| E1 | [Completed composition evidence](bounded-evaluation-2026-10-02.md): Outcome C; complete composition and material resource behavior unproven. |
| E2 | [Completed ICU internal-codec evidence](icu-punycode-supportability-evaluation-2026-10-03.md): Outcome B; external support/upgrade basis not demonstrated. |
| E3 | [Completed native evidence](native-idna2008-feasibility-evaluation-2026-10-06.md): Outcome B; inspected public lookup/registration arrangement lacked demonstrated complete validation; custom protocol completion was outside scope. |

External evidence anchors identify the proposed inventory, not measured conformance or current security clearance:

- [Libidn2 v2.3.8 public header](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/lib/idn2.h.in) and [public API reference](https://www.gnu.org/software/libidn/libidn2/reference/libidn2-idn2.h.html): raw codec declarations and documented error/buffer contracts; later review must verify the exact release bytes.
- [Libidn2 v2.3.8 README](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/README.md): library licensing and maintenance references.
- [ICU 78 documentation](https://unicode-org.github.io/icu/download/78.html) and [ICU release-78.3 license](https://raw.githubusercontent.com/unicode-org/icu/release-78.3/LICENSE): release/data and licensing anchors; verify exact APIs against the pinned source inventory.
- [Unicode 17.0.0 IDNA classifications](https://www.unicode.org/Public/17.0.0/idna/Idna2008.txt) and [Unicode 17.0.0 UCD](https://www.unicode.org/Public/17.0.0/ucd/): versioned classifications and property data.
- [Java 21 JNI specification](https://docs.oracle.com/en/java/javase/21/docs/specs/jni/intro.html): integration mechanism only, not proof of a working bridge.
- [RFC 5890](https://www.rfc-editor.org/rfc/rfc5890), [RFC 5891](https://www.rfc-editor.org/rfc/rfc5891), [RFC 5892](https://www.rfc-editor.org/rfc/rfc5892), [RFC 5893](https://www.rfc-editor.org/rfc/rfc5893) and [RFC 3492](https://www.rfc-editor.org/rfc/rfc3492): clause-level evaluation basis, including applicable updates/errata to be recorded before execution.

## 3. Exact candidate and responsibilities

Exactly one arrangement is proposed. No candidate substitution or component/data version changes are permitted within the future evaluation.

| Component | Exact scope and responsibility |
| --- | --- |
| ICU4J `com.ibm.icu:icu4j:78.3` | Public `Normalizer2.getNFCInstance().isNormalized`, `UCharacter` property/version access and `UProperty` constants for Unicode properties. No internal codec, ICU IDNA conversion or UTS #46 processing. NFC is tested, not repaired. |
| Unicode 17.0.0 | Pinned `Idna2008.txt` and the corresponding UCD property/normalization inputs needed to verify classifications, scripts, joining properties, combining classes and bidi classes. Exact file inventory, hashes and applicable protocol exceptions must be reviewed before execution. |
| GNU Libidn2 2.3.8 | Only public `idn2_punycode_encode` and `idn2_punycode_decode` for raw scalar-array/ASCII conversion, with public version/error helpers where needed. No lookup/registration pipeline, TR46 flags or internal validation helpers. |
| Minimal Java 21 JNI bridge | Proposed repository-owned experimental source, not an existing released component. Transfer checked scalar arrays, lengths and bounded buffers; translate failures safely. No custom or copied codec. Retain bridge source/hash and exact compiler configuration. |
| Repository-owned strict validator | Explicit experimental implementation of the entire strict validation boundary, including contextual rules, domain-wide bidi applicability, syntax/length checks, A-label verification, canonical composition and safe failure. This is substantial protocol ownership, not a thin wrapper. |

Libidn2's bundled IDNA Unicode tables are not the validator's classification authority: only its raw codec is invoked. Inventory all linked native dependencies nevertheless. Verify isolation from their normalization/classification behavior; do not assume that naming a raw API proves isolation. Do not mix JDK Unicode properties with Unicode 17.0.0 decisions. Any material inconsistency is a failed gate, not permission to substitute data.

ICU/Unicode licensing includes Unicode-3.0 and applicable bundled notices; Libidn2's C library offers LGPLv3-or-later/GPLv2-or-later alternatives. Exact file, linked-library, tool and redistribution obligations remain subject to review. No legal clearance, maintainer commitment, security clearance, current-release claim or production support guarantee is asserted.

## 4. Material difference and preserved historical outcomes

| Exhausted evaluation | Material difference proposed here | Still unresolved |
| --- | --- | --- |
| 0.3.0 composition; E1 Outcome C | Names a public scalar-array codec instead of the unsupported internal-codec path and explicitly scopes complete strict-validator and composed resource experiments. | Complete protocol correctness, resource proof, compatibility and maintenance remain unproven. |
| 0.5.0 ICU internal codec; E2 Outcome B | Uses no `com.ibm.icu.impl.Punycode`; ICU supplies public Unicode facilities, Libidn2 supplies public raw codec calls. | Public accessibility alone does not establish defensible support/remediation. Verify stability, security handling and upgrade options. |
| 0.7.0 native arrangement; E3 Outcome B | Does not ask native lookup/registration to provide the complete profile. A new, explicit scope would cover repository-owned contextual, ASCII and domain-aware bidi validation, previously prohibited. | Native packaging/process-failure risks persist, and custom validator maintenance must be accepted for investigation. |

The previous authorizations remain consumed/exhausted. This new authorization neither reopens them nor reclassifies their outcomes. Earlier negative findings remain specific to the evaluated arrangements. The owner approved investigation of this new responsibility allocation; pre-execution technical inventory review remains incomplete. Approval is not evidence that the previous blockers have been solved.

## 5. Approved finite evaluation boundary

The owner authorizes, subject to completion of all §6 gates, one execution, one candidate composition, one development platform and one deployment-representative platform, with a maximum of five engineering days. For an enforceable ceiling, one engineering day means eight aggregate person-hours: at most 40 aggregate person-hours across contributors, including preparation, acquisition, coding, probes, analysis and reporting. This is an investigation budget, not production timing policy. Record cumulative time; parallel contributors do not multiply the budget. No automatic extension, restart, replacement candidate or second run follows from unused capacity or an inconclusive result.

Conditional permitted work consists only of reviewed static/source inspection, verified acquisition into temporary scratch, a minimal experimental JNI bridge and strict-validator harness, synthetic conformance/resource tests, and a reproducible evidence report. No such work is executed by recording these approvals. The report destination must be recorded before execution; this record creates no evaluation report.

Use the original workspace for read-only source context and temporary scratch for the harness. No additional Git worktree, clone, checkout or project directory is permitted. Scratch must not become a production adapter or repository dependency. Candidate evaluation remains prohibited until all §6 preconditions and the report destination are reviewed and recorded. Section 10C separately permits bounded environment verification and conditional acquisition/dependency builds, not candidate implementation or evaluation. The fresh bounded authorization is recorded here; it does not satisfy those prerequisites.

There must be no production integration, application dependency/build/lock/verification changes, database/schema/data changes, authoritative lookup, Authentication, login, Principal or Session implementation, API/Contract changes, deployment changes, external service adoption or real Customer input. Synthetic canonical keys in the harness establish no ownership or authentication authority. Preserve existing local changes, including `verification-keyring.keys`.

## 6. Platform and supply-chain preconditions

The following pre-execution gates remain incomplete and are not assumed facts. Before candidate artifact acquisition, compilation or executable probes, Identity must record the inventory and obtain attributable Security, Architecture and Engineering review of it, including assigned protocol and native maintenance ownership. The later owner decision in §10C authorizes bounded environment verification to establish this evidence, subject to pre-acquisition provenance/licensing review and pre-execution integrity/containment review. Where local-byte integrity evidence cannot exist before acquisition, record the exact source, expected hashes/signatures and verification procedure for approval before acquisition; verify acquired bytes before any build or execution. This sequence does not waive provenance or integrity requirements.

| Precondition | Required evidence before probes | Current disposition |
| --- | --- | --- |
| Development platform | Exact OS/release, CPU, native ABI, libc where applicable and execution containment. | Partially established by §11; technical approval pending |
| Deployment-representative platform | Exact OS/CPU/ABI/libc and why it represents the intended deployment. | Linux AMD64/Java 21/container direction approved in Decision A; exact image/runtime/ABI pending |
| Toolchains | Exact Java 21 vendor/patch/build, JNI headers, C compiler/linker versions, flags, build utilities and any platform SDK. | Pending |
| Native dependency closure | Exact Libidn2 source revision, configuration, system/transitive library versions and hashes; allocation, loading and ABI contracts. Auxiliary components cannot introduce a second candidate or change the nominated versions. | Pending |
| Provenance and integrity | Source/binary URLs, immutable revisions, locally calculated cryptographic hashes and comparison basis, signatures where available, build-input/output manifest and reproducible acquisition/build commands. Distinguish publisher claims from verified bytes. | Pending |
| Unicode/API inventory | Exact Unicode 17.0.0 files, ICU source/API inventory, runtime/data version checks and raw-codec exported symbols. | Pending |
| Licensing and security | Exact licenses/notices, linking/redistribution implications, current vulnerability/support review, trust boundaries, resource hypotheses and safe native execution plan. | Pending |
| Scope and ownership | Reviewed corpus, run limits, failure injection plan and accountable maintenance responsibilities from §9. | Ownership accepted in Decision B; technical plan review pending |

Missing preconditions withhold candidate evaluation; environment verification may proceed only under the narrower §10C authorization and its artifact-specific gates. If they cannot be established within the finite scope, report outcome C; an evidenced incompatible requirement may instead support B. No speculative platform, JDK patch, compiler, ABI or artifact hash is fabricated here.

## 7. Required evidence and falsifiable checks

The later report must provide a clause-to-responsibility-to-test-to-result matrix. Mark each obligation demonstrated, failed or unexecuted, with source clauses, exact inputs, expected disposition/key, actual result and limitations. A sampled passing corpus must not be described as exhaustive conformance or security clearance.

| Evidence obligation | Required checks | Trace |
| --- | --- | --- |
| Strict profile and canonical equality | RFC 5890–5893 clause traceability, applicable updates/errata, ASCII local-part handling with dots/plus preserved, ASCII-dot segmentation and no provider rewrite. Invalid/uncertain inputs produce no canonical key. | G1 strict-profile criteria; G2 Input Boundary and Case and Domain Semantics |
| No mapping or repair | Distinguish sharp-s/`ss` and sigma forms; reject decomposition requiring NFC repair, width/ignored-character mapping and alternative separators. No transitional/non-transitional TR46 or IDNA2003 fallback. | G1 no-mapping criteria; G2 Case and Domain Semantics |
| Complete contextual behavior | Positive, negative and boundary cases for every CONTEXTJ/CONTEXTO rule, including virama/joining context, middle dot, script context and numeral combinations; trace exact property data. | G1 contextual coverage; RFC 5892 |
| Domain-aware bidi | RFC 5893 applicability and all relevant rules over the decoded domain, including ASCII labels; test mixed domains such as `אב.123`, valid RTL domains, invalid mixed directions and digit combinations. | G1 domain-aware bidi; RFC 5893 §§1.4 and 2; E3 decisive gap |
| A-label/U-label correctness | Scalar-safe decode, complete U-label validation, re-encode and ASCII-canonical comparison. Fake/malformed `xn--`, empty payload, supplementary scalars, noncanonical round trips, leading combining marks, hyphens and encoded label/domain limits. Raw conversion alone never means validity. | G1 round-trip criteria; G2 A-label boundary; RFC 3492 |
| Unicode consistency | Pin Unicode 17.0.0 classifications and UCD; verify coverage/derivation against RFC 5892 and applicable exceptions across all scalars; reconcile ICU property/version results with that data. Reject unassigned values; record JDK/data divergence without using it as authority. | G1 Unicode Data and Determinism; E1 mixed-version gap |
| Hostile-input/resource safety | Malformed UTF-16/scalars, pathological context runs, label counts, repeated invalid A-labels, length/overflow edges, allocation and CPU growth, concurrency and repeatability. Set finite per-process memory/time/input/iteration budgets before execution; stop on unexplained unsafe growth. These are harness limits, not new Product limits. | G1 resource requirements; G4 §§23–26 |
| JNI safety | Explicit scalar-array representation, buffer ownership/size arithmetic, bounds and cleanup on every path; allocation failure, public error codes, link/load failure and concurrent calls. Contain intentional fault tests in disposable child processes; distinguish native crash from recoverable Java errors. No Java exception handler is claimed to isolate native process failure. | G1 Java/native evidence; E3 safety gaps |
| Reproducibility and support | Complete native/JVM/data artifact manifest, hashes, commands, flags, platform results, source hashes for experimental bridge/validator, bounded seeds/iterations, raw synthetic observations and unexecuted cases. Review API stability, patch/remediation routes, licenses, dependency closure and maintenance cost. | G3; G4; §6 |
| Identifier compatibility/rollback | Synthetic retained-identifier replay, stable expected canonical outputs, locale/repeated-run/platform comparisons, deterministic rebuilds and simulated changed-classification/output fixtures. Identify collision/rollback risks without merging/reassigning identities. Do not acquire or execute other candidate/Unicode versions; actual future-version compatibility remains unproven. | G1 compatibility; G2 uniqueness, concurrency and failure boundaries |

Protect raw identifiers and native details from ordinary diagnostics. Synthetic test evidence may retain only what reproduction requires. Record privacy-safe failure categories; no protected existence, live credentials or production records may enter the harness. Prototype tests create neither production provenance nor complete Authentication evidence.

## 8. Stop conditions and terminal outcomes

Stop immediately, preserve already-collected evidence safely and report the precise boundary if:

- DEC-0005 semantics would be weakened, mapped, repaired or approximated.
- Internal or unsupported codec APIs are introduced.
- A custom, copied, forked or vendored Punycode codec is substituted.
- Unreviewed dependencies, another candidate or component/data version changes are required.
- Production state is accessed or modified, or application integration becomes necessary.
- Material safety, provenance, licensing, resource, platform or maintenance gaps prevent meaningful safe continuation.
- Any reviewed execution limit is reached, required preconditions cannot be met, or five engineering days/40 aggregate person-hours are exhausted.

Return exactly one terminal outcome:

- **A — Evidence supports a separate acceptance review.** All required evidence is addressed sufficiently to justify that review, with residual risks and limits explicit. This is not selection or production readiness.
- **B — A requirement or supportability gate fails.** Identify the evidenced failure and its scope; do not generalize to universal infeasibility.
- **C — Evaluation remains inconclusive or is stopped.** Identify missing evidence, unexecuted work and triggered stop conditions. Resource/access limits are not proof of technical infeasibility.

Any terminal outcome consumes this new single-use authorization. No outcome automatically selects the implementation, accepts DEC-0009, admits dependencies, approves Architecture or authorizes further execution. Safe reporting is included in the budget; reserve time for it and report partial evidence if limits prevent completeness.

## 9. Accountable review and accepted ongoing ownership

The repository owner explicitly confirmed the following four separate approval decisions on 2026-10-26. The same maintainer performs all four capacities; these are separate accountable dispositions, not four independent reviewers. This record transcribes the owner’s decisions and does not grant approval on the owner’s behalf. No independent human review, external organizational approval or signature is claimed.

| Capacity | Approved scope | Attributable disposition |
| --- | --- | --- |
| Identity | Investigation question; strict DEC-0005 identifier semantics; fixed candidate composition; evaluation scope/boundaries; identifier compatibility obligations. | APPROVED — repository owner acting in Identity capacity; 2026-10-26. |
| Security | Bounded investigation of strict IDNA2008 correctness, contextual/domain-aware bidi validation, hostile-input/resource safety, JNI/native risks, supply-chain integrity and licensing review. | APPROVED — repository owner acting in Security capacity; 2026-10-26. |
| Architecture | Investigation of Java 21 JNI boundary, native dependencies, deployment compatibility, failure isolation and operational consequences; no production Architecture adoption. | APPROVED — repository owner acting in Architecture capacity; 2026-10-26. |
| Engineering | Bounded feasibility investigation, reproducibility, platform/toolchain inventory, experimental implementation constraints and maintenance/supportability assessment. | APPROVED — repository owner acting in Engineering capacity; 2026-10-26. |

| Ongoing responsibility | Proposed accountable owner and participation | Acceptance |
| --- | --- | --- |
| Protocol validation | Identity accountable; Engineering implements/tests; Security reviews correctness. | ACCEPTED — Decision B, repository owner, 2026-10-26 |
| Unicode data upgrades | Identity accountable; Engineering verifies data/build reproducibility; Security reviews semantic/security effects. | ACCEPTED — Decision B, repository owner, 2026-10-26 |
| Native bridge maintenance | Engineering accountable; Identity preserves semantic boundaries; Security reviews native safety; Architecture reviews boundary changes. | ACCEPTED — Decision B, repository owner, 2026-10-26 |
| Vulnerability monitoring and remediation | Security accountable; Engineering delivers verified fixes; Identity reviews identifier effects; Architecture reviews material runtime changes. | ACCEPTED — Decision B, repository owner, 2026-10-26 |
| Deployment/ABI compatibility | Engineering accountable; Architecture reviews deployment/failure boundaries; Security reviews loading/supply chain. | ACCEPTED — Decision B, repository owner, 2026-10-26 |
| Identifier compatibility and rollback | Identity accountable; Engineering supplies comparison/rollback evidence; Security reviews collision/withdrawn-access safety; Architecture reviews material changes. | ACCEPTED — Decision B, repository owner, 2026-10-26 |

Decision B accepts these capacity-level assignments. No independent human review or additional staffing is inferred. Remaining technical inventories, resource/failure plans and integrity/licensing reviews must still be recorded and approved before acquisition, compilation or probes.

## 10. Separate governance and next review step

The next step is to review and record the incomplete §6 inventories, integrity plan, report destination and accepted maintenance responsibilities. This task records authorization decisions only and executes no evaluation. No amendment of DEC-0009, DECISIONS.md, Approved Specifications or ADRs occurs in this step.

DEC-0009 remains v0.8.0 Proposed. Previous authorizations remain exhausted; this is a new, separately bounded evaluation authorization. No implementation strategy is selected. No dependency admission, production implementation, login capability, production Architecture change, release readiness or independent human review is granted or claimed. DEC-0001 admission, including native/system artifacts, remains separate. Material native Architecture adoption requires applicable Architecture approval, an Accepted ADR and canonical Architecture synchronization before affected production implementation. A scratch evaluation does not establish that adoption.

Even outcome A would require separate DEC-0009 acceptance with Identity ownership confirmation, Security and Architecture approval and Engineering review, followed by applicable dependency admission and separately authorized implementation. Canonicalization alone supplies no lookup ownership, verified email, credential validity, Authentication, Principal, Session or Authorization truth.

## 10A. Repository-owner platform and maintenance decisions

The repository owner explicitly approved Decisions A and B on 2026-10-26 through the instruction to record them. The same maintainer acts in separate accountable capacities; no independent human review, signature or production approval is claimed. These decisions supplement, rather than replace, the four §9 investigation approvals.

| Decision | Disposition and attribution | Approved boundary |
| --- | --- | --- |
| A — Evaluation platform | APPROVED — repository owner in Architecture capacity, with Engineering platform and Security boundary responsibilities; 2026-10-26. | Linux AMD64, Java 21, containerized deployment-representative evaluation, with Azure Container Apps as the working hosting assumption. Evaluation only: not production hosting selection or an Accepted production Architecture ADR. Exact Linux distribution, image, JVM patch, libc/ABI and native loading configuration remain subject to technical verification and review. |
| B — Maintenance ownership | APPROVED — repository owner separately in Identity, Engineering and Security accountable capacities, with Architecture supporting review; 2026-10-26. | All six accepted assignments and supporting responsibilities in §9. Acceptance of ownership does not approve an unverified toolchain, dependency configuration or executable plan. |

## 10B. Consolidated technical assessment — 2026-10-26

The following observations use read-only inspection in the original workspace. Recommendations are explicitly unapproved. No candidate acquisition, installation, compilation, conformance probe or experimental code creation occurred.

| Prerequisite | Evidence, proposed configuration and remaining gap | Gate |
| --- | --- | --- |
| Development OS/JDK | macOS 26.5.2 build 25F84, arm64; Temurin 21.0.6+7-LTS at `/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home`, with `javac 21.0.6` and Darwin JNI headers. Default Java is 23; later experiments must explicitly select Java 21. Installed patch is an observation, not current security/support approval. | PARTIALLY SATISFIED |
| Compiler/linker/build | Apple Clang 17.0.0 (`clang-1700.4.4.1`), arm64 Darwin target; Apple linker `ld-1230.1`; GNU Make 3.81; application Wrapper pins Gradle 8.14.5 but is not needed to admit scratch components. No build was run. | PARTIALLY SATISFIED |
| SDK configuration | `xcode-select` points to `/Applications/Xcode.app/Contents/Developer`; its SDKSettings.plist reports macOS SDK 26.1. CommandLineTools `MacOSX.sdk` resolves to `MacOSX26.5.sdk`, version 26.5. Earlier automatic SDK lookup failed despite these files existing. Proposed: explicitly pair Xcode Clang with the Xcode SDK 26.1; exact sysroot, linker/header paths and suitability require review, not an assumed successful build. | PARTIALLY SATISFIED |
| Deployment-representative container | Decision A establishes Linux AMD64/Java 21 and the ACA working assumption. Proposed unapproved base: official `eclipse-temurin:21-jdk-noble`, Ubuntu 24.04 family, requested as `linux/amd64`. This mutable tag is a discovery reference only: resolve immutable image/index and AMD64 manifest digests, exact JDK vendor/patch, OS packages and libc before approval. No image was pulled or inspected. | PARTIALLY SATISFIED |
| Container execution platform | An AMD64 host or explicitly reviewed emulation is needed on the arm64 development machine. Exact engine/host versions, CPU mode, availability, containment and fidelity are not established. No cloud deployment or container runtime changes are authorized. | BLOCKED |
| ABI/JNI loading | Proposed Linux ELF x86-64/glibc shared library, compiled against that image's reviewed headers/toolchain and loaded by the same Java 21 ABI. Darwin dylibs are not Linux artifacts. Pin compiler, linker, glibc/libstdc++ if used, and all transitive symbols; use a controlled absolute load path, no ambient search-path dependency, non-root/read-only runtime and bounded writable scratch. Verify missing/incorrect-architecture library failures in the later harness. Exact libc, SONAMEs, loader paths and flags remain pending. | BLOCKED |
| Candidate dependency configuration | Fixed ICU4J 78.3 public APIs, Unicode 17.0.0, Libidn2 2.3.8 raw codec, owned minimal JNI and strict validator remain unchanged. Proposed native configuration uses the release tarball's bundled libunistring subset (`--with-included-libunistring`), with optional locale/message features disabled where supported. Exact configure flags and whether iconv/gettext/system libraries remain linked must be verified from tagged build sources before approval. No guessed dependency closure or implicit system package is admitted. | PARTIALLY SATISFIED |
| Artifact inventory/integrity | Require ICU binary/source JARs and POM from Maven Central; Unicode IDNA/UCD files from versioned unicode.org sources; Libidn2 release archive/signature from GNU and tagged source provenance; container digests and a package/toolchain manifest. Before acquisition, review immutable identities, expected SHA-256/signatures and trusted signer basis; after separately permitted acquisition, compute local hashes/verify signatures before building. Historical checksums do not verify newly acquired bytes. Exact manifest/hashes still pending. | PARTIALLY SATISFIED |
| Licensing/maintenance | Review ICU/Unicode notices, Libidn2 library LGPLv3-or-later/GPLv2-or-later alternatives, distinct tool/test licenses, bundled libunistring and container/JDK/system package terms. Proposed LGPL use and dynamic linking are recommendations only; record redistribution, notices, source/relinking implications as applicable. Security owns current vulnerability/support assessment; no clearance or guarantee is claimed. | PARTIALLY SATISFIED |
| Maintenance ownership | Six capacity-level accountable assignments accepted by Decision B; named additional reviewers or independent review are not claimed. | SATISFIED |
| Resource plan | Proposed, not approved: 1 CPU and 2 GiB total container memory; child JVM heap 512 MiB; maximum 60 seconds per probe process and 30 minutes per suite; 64 KiB hostile input cap, 100,000 generated cases with fixed seeds and at most four concurrent callers. Include scalar-wide data verification separately, in bounded batches within suite limits. Native allocations share the container limit. These are harness limits, not identifier acceptance rules or production policy; report cases beyond the envelope unexecuted. Overall five engineering days/40 aggregate person-hours remains unchanged, including reporting. | PARTIALLY SATISFIED |
| Synthetic corpus/failures | Proposed: source-attributed vectors for all §7 clauses, contextual rules, RTL/ASCII domains, fake A-labels, supplementary/unassigned scalars, no-mapping distinctions, encoded boundaries and malformed UTF-16. Fixed-seed mutations and repeated/concurrent runs; explicit expected outcomes independent of candidate output. Disposable child processes for native faults; test load/ABI, allocation/error and cleanup failures without production data. Final vector manifest, iteration allocation and safe fault injection need review. | PARTIALLY SATISFIED |
| Report destination | Proposed future durable destination: `specifications/decisions/evidence/DEC-0009/strict-idna-composition-evaluation-2026-10-26.md`, with actual execution dates inside the report; this is a reserved proposed name, not an assertion of execution date. No file created. Destination acceptance and report-writing scope remain pending. | PARTIALLY SATISFIED |

Documentary anchors for recommendations: [official Temurin image manifest](https://github.com/docker-library/official-images/blob/master/library/eclipse-temurin), [Adoptium containers](https://adoptium.net/installation/containers/), [ACA container requirements](https://learn.microsoft.com/en-us/azure/container-apps/containers) and [Libidn2 2.3.8 dependency inventory](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/DEPENDENCIES.md). These identify candidate platform/build directions, not verified local image contents, current support clearance or approved configurations.

### Remaining explicit owner dispositions

1. Engineering/Architecture: approve the exact development SDK/sysroot/toolchain pairing and AMD64 execution host or emulation arrangement after technical verification.
2. Architecture/Engineering/Security: approve the immutable Linux AMD64 image, exact JVM patch, libc/ABI, native load configuration and containment. Decision A already approves the broad platform direction and must not be requested again.
3. Engineering/Security: approve the complete build dependency/configuration manifest, expected artifact integrity/signature plan, current support/security and licensing/redistribution assessment. Exact unresolved versions and hashes must be established before this approval; no blanket approval is inferred.
4. Identity/Security/Engineering, with Architecture for containment: approve the finite resource limits, reviewed corpus, failure injection and compatibility/rollback test plan.
5. Identity/Engineering: approve the report destination and durable evidence-writing scope.

Decision B already accepts maintenance ownership; remaining review concerns technical plans rather than reapproval of those assignments. All remaining dispositions are pending. NO-GO for candidate evaluation until they and the §6 technical gates are completed and recorded. The subsequent §10C authorization permits only bounded environment verification under its distinct gates. No automatic execution authorization, implementation selection, production dependency admission, production Architecture adoption or Authentication change follows from Decisions A or B. Previous authorizations remain exhausted, DEC-0009 remains v0.8.0 Proposed and terminal outcomes A/B/C and all stop conditions remain intact.

## 10C. Technical-plan approval and bounded environment verification

- Decision date: 2026-10-26.
- Decision-maker: repository owner, acting separately in Identity, Security, Architecture and Engineering capacities; same maintainer, no independent human review claimed.
- Technical plan: APPROVED — the consolidated technical configuration and finite safety plan supplied in the preceding readiness assessment, including the limits below.
- Bounded environment verification: AUTHORIZED — development toolchain verification, Docker/AMD64 verification, approved artifact acquisition and integrity verification, native dependency/licensing inspection, disposable environment construction and environment/native-loading smoke checks only.
- Recording/execution branch: `chore/dec-0009-environment-verification`; baseline `88b9168bce003a4040921a20fe9846789a5f63b2` in the original workspace.

This decision supersedes earlier blanket withholding of acquisition/environment compilation only to the extent expressly described here. It does not complete the technical gates by assertion. Before each acquisition, record official exact identity, provenance/trust basis, expected integrity evidence, licensing implications and procedure. Before executing acquired binaries or building dependencies, verify required integrity and licensing. Before pulling a container, establish its official identity, immutable index/AMD64 manifest digests and source revision. Do not bypass unavailable Docker access or environment restrictions.

Disposable dependency builds may use only the reviewed Linux AMD64 environment after these gates pass. A minimal disposable native loader may test availability without calling Punycode or IDNA APIs. No JNI bridge implementation, repository-owned validator, RFC conformance probes, hostile-input evaluation, synthetic corpus execution, production dependency admission, Authentication/database changes, strategy selection or DEC-0009 acceptance is authorized. No additional repository files, worktree or checkout is authorized. Concise environment evidence belongs in this record; propose detailed evidence paths before creating any additional repository evidence.

The approved environment target is Ubuntu 24.04 Noble, Temurin Java 21, Linux AMD64/glibc, using the proposed `eclipse-temurin:21.0.12.1_1-jdk-noble` only after immutable identity review. It remains evaluation-only, not production hosting or Architecture adoption. The fixed candidate versions, Decisions A/B and all six maintenance assignments remain unchanged.

Approved experimental ceilings: 65,536 UTF-16 code units per hostile input; 128 generated labels; 4,096 scalars per hostile label; 100,000 corpus cases plus bounded classification pass; 60 seconds per process; 30 minutes per suite; four hours total executable workload; 1 CPU, 2 GiB container RAM, 512 MiB JVM heap, 64 PIDs, four callers and 512 MiB writable scratch. These limits are not exercised as candidate tests here and do not change identifier semantics. Use read-only root, controlled scratch, no smoke-execution network, dropped capabilities and no-new-privileges; no credentials, production/repository content or Docker-socket mounts.

Stop immediately on integrity failure, untrusted provenance/signer, unreviewed substitution or material dependency changes, licensing incompatibility, unexpected credential/network access, unsafe loading, inability to enforce containment, scope expansion or effort exhaustion. The existing five-day/40-aggregate-person-hour ceiling includes this work and is not reset. Previous authorizations remain exhausted. Candidate evaluation remains NO-GO until the remaining technical gates are evidenced and recorded; this authorization does not select a strategy or grant production readiness.

### Environment-verification result — BLOCKED, 2026-10-26

Checkpoint 1 passed: only this record changed; scope/attribution and continuing candidate-evaluation prohibition verified; Git whitespace passed. No commit or push occurred.

Checkpoint 2 observations: explicit Temurin home `/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home` returned Java `21.0.6+7-LTS`; compiler returned `javac 21.0.6`. Java invocation explicitly supplied the approved JAVA_HOME, DEVELOPER_DIR and SDKROOT as per-command environment only. Apple Clang returned `17.0.0 (clang-1700.4.4.1)`, target `arm64-apple-darwin25.5.0`. These are verified version observations, not compiler/SDK or JNI linkage smoke results. Previously recorded SDK/header inventory is retained; this run did not reverify it by compilation. No global shell configuration changed.

Checkpoint 3 BLOCKED: Docker client `28.0.4`, API `1.48`, Darwin arm64, context `desktop-linux` was observed using `docker version --format '{{json .Client}} {{json .Server}}'`. Server information was null; connection to `unix:///Users/staytru/.docker/run/docker.sock` failed with `connect: operation not permitted`. This establishes lack of access from this task, not that the daemon is stopped or that AMD64 execution is impossible. No permission escalation, alternate socket or bypass was attempted.

Verification stopped at this access/containment boundary. Linux runtime, AMD64 capability, exact image/index/manifest digests, image source revision, container permissions/resource enforcement and native loading: NOT VERIFIED / BLOCKED. No image metadata resolution or pull followed the stop. Checkpoints 4–6: NOT EXECUTED; no candidate artifact acquisition, calculated artifact hashes, signature verification, dependency build/closure verification, licensing clearance or safety-limit enforcement is claimed. Existing documentary licensing/dependency findings remain provisional. No validator, JNI bridge, conformance test, hostile-input test or evaluation report was created.

Final recommendation: NO-GO for beginning candidate evaluation. First establish permitted Docker/AMD64 environment access and resume only within the recorded verification scope; do not infer a new candidate-evaluation permission. This blocked environment check is not a terminal A/B/C candidate-evaluation result and does not claim consumption of the candidate evaluation through execution. All effort remains chargeable to the existing ceiling. This verification segment began at 2026-10-26 21:12:32 UTC and consumed approximately one minute, as reported in the task response. Earlier cumulative engineering effort is not established here, so the remaining 40-person-hour balance must be reconciled before further work.

## 11. Revision history

| Version | Date | Status | Change |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-26 | Draft authorization proposal | Proposed one fresh bounded composition evaluation with pending review, explicit preconditions, finite evidence scope and no execution authority. |
| 0.2.0 | 2026-10-26 | Approved bounded evaluation authorization; execution withheld | Recorded the repository owner’s four separate capacity approvals; retained incomplete pre-execution gates, pending ongoing assignments and every candidate, time, stop and adoption boundary. |
| 0.3.0 | 2026-10-26 | Approved bounded evaluation authorization; technical gates incomplete | Recorded owner Decisions A and B, six accepted maintenance assignments and a consolidated technical assessment; recommendations remain unapproved and execution withheld. |
| 0.4.0 | 2026-10-26 | Technical plan approved; bounded environment verification authorized | Recorded owner approval in four capacities; permits narrowly gated environment verification only, preserves candidate-evaluation prohibition and existing effort ceiling. |
