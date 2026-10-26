# DEC-0009 — Fresh bounded strict-IDNA2008 evaluation authorization record

- Version: 0.2.0.
- Status: Approved bounded evaluation authorization; pre-execution gates incomplete; execution withheld.
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

This record documents the repository owner’s explicit approval on 2026-10-26 of one new, separately bounded evidence-gathering evaluation in four distinct accountable capacities, as recorded in §9. The approval provenance is the repository owner’s explicit instruction in this task; no signature or independent reviewer is invented. This is evaluation authorization only, not Decision Record acceptance, dependency admission or implementation selection. All §6 pre-execution gates remain incomplete: candidate artifact acquisition, compilation and executable probes must not begin yet. Reviewing or merging this record does not complete those gates. DEC-0009 remains version 0.8.0 Proposed and non-authoritative; this record changes neither it nor the Decision Index.

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

The previous authorizations remain consumed/exhausted. This new authorization neither reopens them nor reclassifies their outcomes. Earlier negative findings remain specific to the evaluated arrangements. The owner approved investigation of this new responsibility allocation; pre-execution inventory and assignment review remains incomplete. Approval is not evidence that the previous blockers have been solved.

## 5. Approved finite evaluation boundary

The owner authorizes, subject to completion of all §6 gates, one execution, one candidate composition, one development platform and one deployment-representative platform, with a maximum of five engineering days. For an enforceable ceiling, one engineering day means eight aggregate person-hours: at most 40 aggregate person-hours across contributors, including preparation, acquisition, coding, probes, analysis and reporting. This is an investigation budget, not production timing policy. Record cumulative time; parallel contributors do not multiply the budget. No automatic extension, restart, replacement candidate or second run follows from unused capacity or an inconclusive result.

Conditional permitted work consists only of reviewed static/source inspection, verified acquisition into temporary scratch, a minimal experimental JNI bridge and strict-validator harness, synthetic conformance/resource tests, and a reproducible evidence report. No such work is executed by recording these approvals. The report destination must be recorded before execution; this record creates no evaluation report.

Use the original workspace for read-only source context and temporary scratch for the harness. No additional Git worktree, clone, checkout or project directory is permitted. Scratch must not become a production adapter or repository dependency. Candidate artifact acquisition, compilation and executable probes remain prohibited until all §6 preconditions and the report destination are reviewed and recorded. The fresh bounded authorization is recorded here; it does not satisfy those prerequisites.

There must be no production integration, application dependency/build/lock/verification changes, database/schema/data changes, authoritative lookup, Authentication, login, Principal or Session implementation, API/Contract changes, deployment changes, external service adoption or real Customer input. Synthetic canonical keys in the harness establish no ownership or authentication authority. Preserve existing local changes, including `verification-keyring.keys`.

## 6. Platform and supply-chain preconditions

The following pre-execution gates remain incomplete and are not assumed facts. Before candidate artifact acquisition, compilation or executable probes, Identity must record the inventory and obtain attributable Security, Architecture and Engineering review of it, including assigned protocol and native maintenance ownership. Static documentation review may prepare the inventory, but this task authorizes recording only. Where local-byte integrity evidence cannot exist before acquisition, record the exact source, expected hashes/signatures and verification procedure for approval before acquisition; verify acquired bytes before any build or execution. This sequence does not waive provenance or integrity requirements.

| Precondition | Required evidence before probes | Current disposition |
| --- | --- | --- |
| Development platform | Exact OS/release, CPU, native ABI, libc where applicable and execution containment. | Pending |
| Deployment-representative platform | Exact OS/CPU/ABI/libc and why it represents the intended deployment; no deployment topology selected by default. | Pending |
| Toolchains | Exact Java 21 vendor/patch/build, JNI headers, C compiler/linker versions, flags, build utilities and any platform SDK. | Pending |
| Native dependency closure | Exact Libidn2 source revision, configuration, system/transitive library versions and hashes; allocation, loading and ABI contracts. Auxiliary components cannot introduce a second candidate or change the nominated versions. | Pending |
| Provenance and integrity | Source/binary URLs, immutable revisions, locally calculated cryptographic hashes and comparison basis, signatures where available, build-input/output manifest and reproducible acquisition/build commands. Distinguish publisher claims from verified bytes. | Pending |
| Unicode/API inventory | Exact Unicode 17.0.0 files, ICU source/API inventory, runtime/data version checks and raw-codec exported symbols. | Pending |
| Licensing and security | Exact licenses/notices, linking/redistribution implications, current vulnerability/support review, trust boundaries, resource hypotheses and safe native execution plan. | Pending |
| Scope and ownership | Reviewed corpus, run limits, failure injection plan and accountable maintenance responsibilities from §9. | Pending |

Missing preconditions withhold candidate artifact acquisition, compilation and probes. If they cannot be established within the finite scope, report outcome C; an evidenced incompatible requirement may instead support B. No speculative platform, JDK patch, compiler, ABI or artifact hash is fabricated here.

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

## 9. Accountable review and proposed ongoing ownership

The repository owner explicitly confirmed the following four separate approval decisions on 2026-10-26. The same maintainer performs all four capacities; these are separate accountable dispositions, not four independent reviewers. This record transcribes the owner’s decisions and does not grant approval on the owner’s behalf. No independent human review, external organizational approval or signature is claimed.

| Capacity | Approved scope | Attributable disposition |
| --- | --- | --- |
| Identity | Investigation question; strict DEC-0005 identifier semantics; fixed candidate composition; evaluation scope/boundaries; identifier compatibility obligations. | APPROVED — repository owner acting in Identity capacity; 2026-10-26. |
| Security | Bounded investigation of strict IDNA2008 correctness, contextual/domain-aware bidi validation, hostile-input/resource safety, JNI/native risks, supply-chain integrity and licensing review. | APPROVED — repository owner acting in Security capacity; 2026-10-26. |
| Architecture | Investigation of Java 21 JNI boundary, native dependencies, deployment compatibility, failure isolation and operational consequences; no production Architecture adoption. | APPROVED — repository owner acting in Architecture capacity; 2026-10-26. |
| Engineering | Bounded feasibility investigation, reproducibility, platform/toolchain inventory, experimental implementation constraints and maintenance/supportability assessment. | APPROVED — repository owner acting in Engineering capacity; 2026-10-26. |

| Ongoing responsibility | Proposed accountable owner and participation | Acceptance |
| --- | --- | --- |
| Protocol validation | Identity, with Security correctness review and Engineering implementation/testing responsibility. | Pending |
| Unicode data upgrades | Identity, with Engineering reproducible data/version verification and Security compatibility review. | Pending |
| Native bridge maintenance | Engineering, with Identity boundary ownership and Security native-safety review. | Pending |
| Vulnerability remediation | Engineering for component triage/patch delivery, with Security risk/remediation review and Identity decisions on identifier effects. | Pending |
| Deployment compatibility | Engineering for native build/platform compatibility, with Architecture review and Identity identifier-compatibility confirmation. | Pending |

These ongoing assignments remain proposed and pending confirmation, distinct from the four approved evaluation dispositions. Before acquisition, compilation or probes, record attributable acceptance of protocol and native maintenance ownership and the other §6 gate reviews. The owner’s scope approvals do not establish available staff, completed inventory review or accepted ongoing assignments. Lack of an accountable protocol/native maintenance basis continues to withhold execution.

## 10. Separate governance and next review step

The next step is to review and record the incomplete §6 inventories, integrity plan, report destination and maintenance assignments. This task records authorization decisions only and executes no evaluation. No amendment of DEC-0009, DECISIONS.md, Approved Specifications or ADRs occurs in this step.

DEC-0009 remains v0.8.0 Proposed. Previous authorizations remain exhausted; this is a new, separately bounded evaluation authorization. No implementation strategy is selected. No dependency admission, production implementation, login capability, production Architecture change, release readiness or independent human review is granted or claimed. DEC-0001 admission, including native/system artifacts, remains separate. Material native Architecture adoption requires applicable Architecture approval, an Accepted ADR and canonical Architecture synchronization before affected production implementation. A scratch evaluation does not establish that adoption.

Even outcome A would require separate DEC-0009 acceptance with Identity ownership confirmation, Security and Architecture approval and Engineering review, followed by applicable dependency admission and separately authorized implementation. Canonicalization alone supplies no lookup ownership, verified email, credential validity, Authentication, Principal, Session or Authorization truth.

## 11. Revision history

| Version | Date | Status | Change |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-26 | Draft authorization proposal | Proposed one fresh bounded composition evaluation with pending review, explicit preconditions, finite evidence scope and no execution authority. |
| 0.2.0 | 2026-10-26 | Approved bounded evaluation authorization; execution withheld | Recorded the repository owner’s four separate capacity approvals; retained incomplete pre-execution gates, pending ongoing assignments and every candidate, time, stop and adoption boundary. |
