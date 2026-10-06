# DEC-0009 — Customer Login IDNA2008 Implementation Strategy

- **Identifier:** DEC-0009
- **Title:** Customer Login IDNA2008 Implementation Strategy
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.7.0
- **Date:** 2026-10-04
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted DEC-0004 selects email as the initial Customer Login identifier. Accepted DEC-0005 defines its input, strict per-label IDNA2008 validation, canonical comparison, retained ownership, uniqueness, collision, and lifecycle semantics, but selects no implementation library. The Initial Customer Login Contracts require these semantics before authoritative identifier resolution and successful Authentication.

PR #110 implemented bounded Identity-owned Argon2id credential verification. PR #111 implemented `CustomerLoginEmailInput`: preparation of untrusted input, governed local-part handling, and preservation of domain-label candidates for later validation. That type does not establish IDNA validity or a canonical comparison key. Its acceptance of a candidate is not permission to perform authoritative lookup.

The required dependency remains:

```text
prepared login-email candidate
→ strict IDNA2008 validation
→ canonical comparison key
→ authoritative Identity lookup
```

Canonicalization and lookup do not establish mailbox control, current-email verification, credential validity, Authentication, Principal, Session, association, Customer/Account ownership, or Authorization. DEC-0008 independently requires accepted verification evidence for the current authoritative login email. A credential `MATCH` alone is not Authentication.

## Decision Question

Which implementation, exact release, configuration, and Unicode-data behavior can demonstrably satisfy DEC-0005's strict IDNA2008 profile on Java 21 without prohibited mapping, repair, equality drift, or unsafe failure behavior?

## Decision — Proposed Evaluation Boundary

This proposal defines the evaluation and acceptance boundary only. **No library, implementation, candidate release, configuration, or Unicode version is selected.** The single evaluation authorized by version 0.3.0 has completed with a stopped-path disposition, recorded below. No complete conformant implementation, acceptance approval, or dependency admission is established. The separate 0.5.0 codec-supportability evaluation is complete with **B. SUPPORTABILITY BASIS NOT DEMONSTRATED**; its authorization is exhausted and cannot be reused. Direct use of `com.ibm.icu.impl.Punycode` from ICU4J 78.3 is removed from the current candidate path. Version 0.6.0 recorded that result only. Version 0.7.0 authorizes exactly one bounded native strict-IDNA2008 feasibility and admission-readiness evaluation under the scope below. DEC-0009 remains unresolved/Proposed; no evaluation is executed or result claimed by this revision.

The eventual strategy must satisfy DEC-0005 exactly. It must not weaken that decision to fit a convenient API. The proposed evaluation must distinguish IDNA2008, IDNA2003, and UTS #46 compatibility processing, including transitional and non-transitional behavior. A product label such as “IDNA support” is not conformance evidence.

Selection remains unresolved until reproducible technical evidence and the required authority reviews support an explicit choice. If no candidate demonstrates the governed semantics safely, the outcome must remain no selection; authoritative Login lookup must remain blocked rather than use an approximation.

## Evaluation Criteria and Required Evidence

### Strict Profile and Canonical Composition

For every candidate, record the exact API calls, configuration flags, defaults, Unicode data sources, and validation/conversion sequence. Trace each criterion to DEC-0005 and the applicable external standard. Evaluation must cover:

1. Strict IDNA2008 U-label validity, including required normalized form, without silently normalizing or repairing input that the governed profile rejects.
2. A-label decoding, validation of the corresponding U-label, canonical re-encoding, and comparison after governed locale-independent ASCII lowercase canonicalization. Malformed payloads, fake `xn--` labels, and failed or non-canonical round trips must fail safely.
3. CONTEXTJ and CONTEXTO positive and negative cases; RFC 5893 bidirectional requirements, including applicable context across labels.
4. Applicable ASCII-label syntax, hyphen restrictions, and encoded label-length behavior. Evidence must distinguish protocol-derived constraints from transport bounds and must not invent new Product length policy.
5. Per-label processing with ASCII full stop as the only domain separator. Alternative separators and other disallowed characters must be rejected rather than mapped.
6. No UTS #46 mapping, compatibility mapping, provider rewriting, runtime-specific normalization, or other prohibited preprocessing that alters governed equality.
7. Deterministic composition of the validated canonical ASCII domain with the already governed ASCII-lowercase local part. Dots and plus tags in the local part remain preserved. No new local-part transformation is authorized.
8. Rejected, ambiguous, failed, or uncertain candidates never proceed to authoritative lookup. A prepared candidate must not be promoted into a canonical comparison key merely because a conversion method returns a string.

The evaluation corpus must contain source-attributed inputs, expected acceptance/rejection and canonical output where applicable, actual results, and explanations of differences. Include valid U-label/A-label equivalents; ordinary ASCII and mixed-case A-labels; malformed/fake A-labels; leading combining marks; contextual characters; valid and invalid bidirectional cases; syntax/hyphen and encoded-length boundaries; empty labels; malformed Unicode; and prohibited separators.

Mapping-difference evidence must include representative sharp-s, final sigma, width variants, ignored/mapped characters, and alternative separator forms. Expected results must derive from DEC-0005's profile, not be copied uncritically from a compatibility-processing test suite. Include deliberately invalid prepared candidates accepted by `CustomerLoginEmailInput` to prove the downstream validation boundary rejects them appropriately.

### Unicode Data and Determinism

Record the exact Unicode version and data/table provenance used by each candidate, including normalization and derived-property data and any reliance on JDK tables. Demonstrate deterministic behavior across the supported Java 21 execution environments, locale changes, and repeated runs. Document unassigned-code-point handling and code points whose acceptance or properties differ between evaluated versions.

Before acceptance, define how Unicode-data and implementation upgrades are reviewed, how behavior differences are detected, and how compatibility of retained authoritative identifiers is assessed. Runtime defaults or environment upgrades must not silently change equality. The proposal does not select a Unicode version or authorize a new acceptance policy.

### Reproducibility and Candidate Inventory

Documentation establishes claims to test, not proof that a candidate passes. A separately authorized technical evaluation must provide reproducible executable evidence before selection. Preserve the candidate identity, exact release/version, source provenance, license, maintenance status, security history, transitive dependency inventory, Java 21 compatibility, configuration, runtime/data versions, commands, corpus provenance, and actual results. Record failures and untested cases explicitly; do not infer conformance from dependency availability or popularity.

Evidence must permit an independent reviewer to reproduce the results. Compare implementations only against the governed requirements, and identify any residual gap or required supplementary validation. This proposal does not authorize production implementation or addition of evaluation dependencies to the backend build.

## Alternatives to Evaluate

### A. Maintained Implementation Claiming Strict IDNA2008 Support

Evaluate whether its exact release and APIs implement the entire required profile, including contextual and bidirectional checks, A-label round trips, version behavior, and rejection without mapping. Assess maintenance, license, supply chain, dependency footprint, and integration cost. No candidate is named or preferred by this proposal.

### B. Existing JDK or Admitted Repository Capabilities

Inventory actual capabilities and demonstrate conformance or document gaps using the same corpus. Availability in the JDK or a transitive artifact does not establish suitability, admission for this purpose, or approval. `java.net.IDN` must not be selected merely because it is available; IDNA2003 and strict IDNA2008 behavior must be distinguished by evidence.

### C. Implementations Primarily Providing UTS #46 Compatibility Processing

Evaluate whether the exact APIs and configuration can satisfy the no-mapping strict profile in full. Disabling transitional processing alone is not proof. Any unavoidable prohibited mapping, missing validation, or equality change disqualifies the proposed use under DEC-0005. Do not silently substitute UTS #46 for IDNA2008.

### D. Deferral / No Selection

If no candidate demonstrates safe conformance, document the gaps and defer selection. Login canonicalization and authoritative lookup remain incomplete. Deferral does not authorize weakened semantics or a fallback implementation.

### Prohibited Fallbacks

Ad-hoc hand-written Punycode or IDNA used merely to bypass the absence of a demonstrated conformant implementation remains prohibited. Partial wrappers presented as complete IDNA2008, approximate IDNA behavior, raw/prepared-email lookup, `lower(email)`, database-collation equality, ASCII-only fallback, `java.net.IDN` substitution, JDK availability as proof, silent UTS #46 substitution, and semantic weakening of DEC-0005 are not acceptable fallbacks. Exploratory code MUST NOT become production authority without the required governance.

### E. Future Separately Authorized Repository-Owned Validation Evaluation

DEC-0009 MAY evaluate a precisely bounded repository-owned strict validation composition when no maintained complete implementation has been established, but only after explicit, separately reviewed spike authorization. The 0.2.0 Proposed-scope clarification did not authorize a spike. The 0.3.0 bounded authorization below permitted one evaluation only and is now exhausted; it does not select this direction or establish that repository-owned validation is safe or preferred. Substantial repository-owned protocol validation creates security and continuing maintenance responsibilities; it must not be presented as an easy wrapper.

Before experimental execution, the evaluation scope and candidate inventory MUST identify the following for review under the bounded authorization below:

- the exact investigation scope, Identity ownership, and Security, Architecture, and Engineering review;
- exact third-party components and data, if any, their Unicode/property-data versions, the Punycode/codec strategy, and each validation responsibility that would be repository-owned;
- a strict conformance evidence plan and source-attributed counterexample/regression corpus, including CONTEXTJ/CONTEXTO, RFC 5893 and applicable cross-label bidi behavior, and A-label decode/validate/re-encode evidence;
- malformed-input and resource-exhaustion hypotheses, and privacy-preserving, enumeration-safe failure requirements;
- implementation/data upgrade and rollback considerations, retained-identifier compatibility, and collision/reconciliation considerations;
- isolation from production lookup, persistence, Authentication, Principal, Session, and HTTP behavior; and
- objective stop conditions, including required semantic weakening, missing protocol behavior outside reviewed scope, unauthorized codec work, production integration, unapproved dependency adoption, or exhaustion of the authorized investigation boundary.

Permission to evaluate repository-owned validation does NOT authorize repository-owned Punycode implementation. Any codec strategy requires separate explicit evaluation. Repository-owned logic MUST NOT silently implement missing protocol behavior without reviewed scope. The spike must return reproducible evidence and unresolved gaps rather than create implementation authority; failed or incomplete evidence must not be represented as conformance or permission to proceed to production.

### F. Native Implementation Evaluation

A maintained native implementation exposed through a governed JVM integration remains a possible evaluation direction, subject to separately reviewed investigation scope and authorization. Exact native and JVM components, profile/data configuration, provenance, Java compatibility, packaging, failure behavior, and maintenance responsibilities require evidence. Native/JNI/JNA/runtime/deployment/platform/ABI changes require Architecture assessment; an ADR is required if the selected strategy materially changes Architecture. This alternatives clarification itself authorizes no native strategy or spike and makes no Architecture change. The separate 0.7.0 authorization below now bounds one native evidence evaluation; it does not select this direction.

Repository-owned composition, native implementation, a future maintained implementation, and deferral remain unselected directions. DEC-0009 remains Proposed until an actual implementation strategy satisfies its acceptance gates and receives the required authority.

## Bounded Technical Evaluation Authorization — 0.3.0 Proposed (Completed)

The following scope records the completed 0.3.0 authorization. It does not grant continuing authority; any additional technical evaluation requires separate explicit authorization.

### Purpose, Ownership, and Review

Version 0.3.0 authorized one isolated technical evaluation to determine whether a repository-owned composition can satisfy DEC-0005's strict IDNA2008 profile. The purpose is evidence gathering on feasibility, completeness, security burden, maintenance burden, and unresolved gaps only. Successful experimentation creates no production authority. The authorization revision itself created no spike/source/test code and claimed no test results; the later executed observations are retained in the evidence record below.

Identity owns the evaluation. Security reviews protocol correctness, hostile-input behavior, and security consequences. Architecture reviews architectural, runtime, and dependency implications. Engineering reviews feasibility, maintainability, and testability. Required reviews and their outcomes must be durable and discoverable; this record asserts no individual reviewer identity or completed review. Acceptance of DEC-0009 remains separately governed by its existing gates.

### Isolation and Component Inventory

The evaluation MUST remain in isolated external/scratch work, separate from production Login behavior. It MUST NOT wire into authoritative Identity lookup, modify persistence or Authentication orchestration, establish Principal or Session behavior, modify HTTP Contracts or OpenAPI, change `CustomerLoginEmailInput` production semantics, or process real Customer data. Experimental code and results MUST NOT become production authority.

The evaluation MAY investigate standards-derived property data, maintained Unicode components, normalization primitives, lower-level library capabilities, and other bounded components. No component or Unicode version is selected by this authorization. Before executing experiments, identify exact candidate source/component releases, data provenance and Unicode/property-data versions, proposed repository-owned versus external responsibilities, the codec strategy, evidence plan, and resource/security hypotheses for Identity-owned scope review with Security, Architecture, and Engineering. Recording an experimental candidate is not final strategy or dependency selection. Missing behavior outside that reviewed scope requires stopping for further authorization, not silently implementing it to make tests pass.

Repository-owned Punycode/ACE implementation is NOT authorized. Punycode/ACE codec capability must be independently identified and evaluated, including its provenance, supported API, compatibility, and failure behavior. Permission to investigate validation does not imply permission to write a codec.

### Required Coverage and Counterexample Corpus

The evaluation MUST cover at least:

- RFC 5890 terminology and A-label/U-label handling; RFC 5891 expectations relevant to strict registration/lookup validation; and RFC 5892 derived-property classifications, exceptions, and contextual behavior;
- CONTEXTJ, CONTEXTO, RFC 5893 bidi rules and applicable cross-label requirements, canonical A-label decode → validate → re-encode checks, fake/malformed `xn--` input, and prohibited hyphen forms;
- label/domain length constraints, distinguished from transport bounds without inventing Product policy; malformed Unicode and UTF-16 boundaries; exact Unicode/property-data versioning; and unassigned-code-point handling;
- rejection without implicit mapping, repair, width folding, case mapping beyond governed comparison semantics, ignored-character removal, or UTS #46 substitution, and deterministic canonical comparison-key consequences;
- resource-exhaustion and hostile-input behavior, privacy and enumeration consequences, and safe failure without disclosure of protected input or existence; and
- upgrade/rollback responsibilities, retained-identifier compatibility, and collision/reconciliation consequences without assuming a production population or authorizing migration.

The source-attributed strict conformance and regression corpus MUST include sharp-s versus `ss`, final versus ordinary sigma, fullwidth forms, soft hyphen/ignored-character behavior, decomposed Unicode, valid and invalid join-control and middle-dot contexts, malformed/fake A-labels, prohibited hyphens, alternative separators, bidi failures, and disallowed emoji in both U-label and A-label form. Include positive cases and boundary cases as well as rejection cases. This minimum corpus is not a complete conformance suite; expected outcomes must follow DEC-0005 and the applicable standards, not UTS #46 mapping expectations. Record actual outcomes, commands, versions, gaps, and reproduction instructions; fabricate no results.

### Dependency and Architecture Boundaries

Isolated scratch/prototype evidence gathering does not itself admit a production dependency. Any artifact added to the repository build, test, or runtime dependency graph requires applicable DEC-0001 dependency-admission governance. This authorization admits neither ICU4J, GNU libidn2, idnkit, nor any other dependency. Existing evidence that `java.net.IDN` and ICU4J 78.3's tested public UTS #46 APIs do not satisfy the complete strict profile remains evidence only and does not authorize their substitution.

Repository-owned validation composition does not automatically require an ADR when it remains behind existing Identity boundaries. Material native/JNI/JNA/runtime/deployment/platform/ABI/container/loading/failure-model changes require Architecture assessment and an ADR where current governance requires one. This authorization changes no Architecture and does not authorize a native-integration spike. Native evaluation, future maintained implementations, and deferral remain legitimate unresolved alternatives subject to their own required authorization.

### Required Output and Stop Conditions

The evaluation MUST return a durable, reproducible evidence report identifying technical feasibility; exact repository-owned and external/component responsibilities; unresolved conformance gaps; security and operational risks; Unicode upgrade responsibilities; compatibility/collision consequences; and dependency and Architecture implications. It must explain whether evidence supports proceeding toward acceptance, another evaluation, consideration of another strategy, or continued deferral. The report MUST NOT itself change DEC-0009 status or create implementation authority. The completed report is now retained in the durable evidence artifact below.

Stop the evaluation and return evidence and gaps without promoting experimental work if:

- strict DEC-0005 semantics would need weakening, required RFC behavior cannot be demonstrated, or protocol behavior would need silent approximation;
- an unapproved custom Punycode/ACE implementation or unauthorized repository dependency admission becomes necessary;
- production lookup, persistence, Authentication, Principal, Session, or HTTP wiring would be required merely to prove feasibility;
- security/resource behavior remains materially unresolved or Unicode upgrade/retained-identifier compatibility cannot be bounded; or
- the investigation exceeds its authorized scope.

This authorization does NOT accept DEC-0009, select repository-owned validation as the final strategy, select a library or Unicode version, admit a dependency, authorize repository-owned Punycode, authorize production implementation or Login lookup integration, authorize persistence or Authentication/Principal/Session changes, authorize Contract/OpenAPI changes, or modify DEC-0005 or Architecture. DEC-0009 remains Proposed and non-authoritative for implementation until its acceptance gates and required authority are satisfied.

## Completed Evaluation and Stopped-Path Disposition — 0.4.0 Proposed

The single 0.3.0-authorized evaluation was executed against `ccddce97c68d25686c3d20224019e98b47759d1c` and is complete. Its authorization is exhausted. [Durable bounded-evaluation evidence](evidence/DEC-0009/bounded-evaluation-2026-10-02.md) retains the environment, exact components and hashes, procedures, original harness listings, raw observations, responsibility and counterexample matrices, limitations, and original scratch inventory without depending on temporary files remaining available.

Evidence classification: **CURRENT REPOSITORY-OWNED COMPOSITION PATH STOPPED**. The two triggered stop conditions for that evaluated path were:

1. Required RFC behavior was not demonstrated for the evaluated composition.
2. Material composed security/resource behavior remains unresolved.

Component probes were executed; the complete CONTEXTJ/CONTEXTO, RFC 5893, and strict A-label validation composition was **NOT EXECUTED**. Lack of demonstration is not proof of universal infeasibility: this outcome does not establish that every future repository-owned composition is infeasible.

GNU Libidn 1.43's evaluated Java codec is unsuitable for the evaluated Unicode repertoire: decoding `097c` returned U+0300 instead of U+10300, and decoding `e28h` returned U+F600 instead of U+1F600. ICU4J 78.3 internal Punycode demonstrated useful raw codec behavior, but at the 0.4.0 evidence-recording stage its independent production supportability remained **UNRESOLVED**, not selected or approved. The later 0.5.0 outcome is recorded separately below. Primitive conversion and round-trip success do not establish strict IDNA2008 validity.

Mandatory blockers remain: a suitable scalar-correct codec with an explicit support/maintenance basis; complete strict contextual, domain-aware bidi, syntax, length and canonical A-label evidence; bounded hostile-input/resource and safe failure behavior; consistent versioned Unicode/property data and reproducible classification; upgrade/rollback, retained-identifier compatibility and collision/reconciliation evidence; applicable provenance, licensing, security and dependency review; and the required acceptance authority. No completed human acceptance approvals are asserted.

The post-evaluation review recommended recording this evidence before separately considering authorization of one narrower evidence question:

> Can direct use of `com.ibm.icu.impl.Punycode` from ICU4J 78.3 be supported as an independently maintained codec dependency, with an explicit compatibility, security-maintenance and upgrade basis, without copying/forking the codec or relying on UTS #46 processing?

In version 0.4.0 this was a **recommended future evidence question only**, with no authorization to investigate it or continue the stopped path. Version 0.5.0 subsequently granted the separate bounded authorization below, which is now complete and exhausted. The completed evaluation and its exhausted authority remain unchanged; no continuation of the stopped composition path or production implementation is authorized. Native evaluation, future maintained implementations, and deferral remain unresolved alternatives; none is selected.

DEC-0009 remains Proposed, Identity-owned and non-authoritative for implementation. DEC-0005 semantics remain unchanged. DEC-0001 admission and applicable Architecture/ADR gates remain separate. No dependency, library, Unicode version, final strategy, Architecture, Product, Contract/OpenAPI, persistence, Authentication, Principal, Session, or production implementation change is authorized by recording these findings.

## Bounded Codec-Supportability Evaluation Authorization — 0.5.0 Proposed (Completed)

The following scope and exit criteria are retained as the historical terms of the executed 0.5.0 evaluation, not continuing permission or pending work. Its single authorization is exhausted and cannot be reused. The completed Outcome B and current candidate consequence are recorded in the 0.6.0 disposition below.

### Exact Question and Authority Boundary

Version 0.5.0 authorized **one** evidence evaluation answering only:

> Can direct use of `com.ibm.icu.impl.Punycode` from ICU4J 78.3 be supported as an independently maintained codec dependency, with an explicit compatibility, security-maintenance and upgrade basis, without copying/forking the codec or relying on UTS #46 processing?

ICU4J 78.3 and this internal/non-public API were the exact raw Punycode codec **candidate for that evaluation**, not a selected technology, admitted dependency, supported public API, or approved production API. This authorization establishes no Architecture acceptance, implementation authority, or strict IDNA2008 conformance. It neither repeats nor reinterprets the [completed evaluation evidence](evidence/DEC-0009/bounded-evaluation-2026-10-02.md). That evaluated composition remains stopped; the finding does not prove every future composition impossible.

Identity owns this bounded evaluation. Its report must identify Security review needs for protocol and maintenance risks, Architecture review needs for internal-API and dependency implications, and Engineering review needs for compatibility and operational burden. No completed review or acceptance approval is asserted. The existing acceptance gates remain mandatory.

### Permitted Investigation and Evidence

The completed authorization permitted inspection of authoritative upstream documentation, tagged source, release history, issue/security-handling records, and published artifact provenance. It permitted minimal direct encode/decode and compatibility probes on Java 21 in an isolated external/scratch harness against unmodified published artifacts. Only synthetic inputs may be used; no production data, repository build/test/runtime dependency changes, or Login integration is permitted. Source inspection does not authorize copying implementation code into a codec, fork, vendored artifact, or repository-owned replacement.

The evaluation MUST establish or explicitly identify missing evidence for:

1. **Support and API stability:** distinguish public support commitments from internal implementation details; assess relocation, removal, visibility, signature, exception and behavioral change risks. Availability and past stability alone do not establish a support commitment.
2. **Upgrade compatibility:** record a finite, justified comparison inventory before probes: ICU4J 78.3 as the candidate, relevant earlier releases, and a later published release if available. Record exact versions, hashes, Java environment, calls, expected/actual results, and differences. Other releases are compatibility evidence only, not alternative selections. If no later release is available, record that limit without claiming future compatibility.
3. **Upstream security maintenance:** assess whether codec defects and security reports have an identifiable upstream handling path, what maintenance evidence covers this internal API, and what remains unsupported. Identify owner responsibilities for monitoring, triage, patch availability, upgrade/rollback review and responding if the internal API disappears. Do not infer independent codec maintenance from general ICU project activity alone.
4. **Isolation from UTS #46:** inspect the direct call path and use bounded probes to determine whether raw codec use can avoid mapping, repair, transitional processing, normalization or other UTS #46 semantics. No UTS #46 API may become the strict pipeline or a fallback. Successful conversion or round trips are not IDNA2008 validation.
5. **Dependency and supply chain:** inventory exact coordinates, artifact provenance/integrity, license and notices, transitive footprint, Java 21 compatibility, packaging and security-maintenance implications sufficiently to assess whether a later DEC-0001 admission proposal could be justified. No dependency is admitted by this investigation or its result.
6. **No copied implementation:** determine whether the proposed direct-use path can remain viable without copying, forking, vendoring, modifying or reimplementing ICU's Punycode codec. If any such work is necessary, record the incompatibility with this authorization rather than performing it.
7. **Repository-owned verification needs:** identify the compatibility/security evidence that would be required before any production proposal: API/linkage change detection; scalar and supplementary-code-point correctness; malformed-input and exception behavior; encode/decode regression vectors; mapping-isolation checks; resource-bound and pathological-input evidence; version-to-version behavior comparison; and retained-identifier compatibility/upgrade gates. Minimal scratch probes may inform these requirements; no production test suite or validator is authorized here.

The investigation must assess whether maintaining these checks and responding to upstream changes is defensible, including operational burden and residual risk. Repository tests can detect covered changes but do not create an upstream support guarantee. Unresolved evidence must remain explicit, not be replaced by an assumed compatibility or security promise.

### Preserved Semantics and Prohibited Expansion

This codec-supportability inquiry does not waive or resolve RFC 5890/5891/5892/5893 requirements, CONTEXTJ/CONTEXTO, domain-aware Bidi, A-label/U-label validation, no-mapping semantics, Unicode data/version strategy, unassigned-code-point handling, resource bounds, upgrade/retained-compatibility requirements, or canonical comparison-key requirements. All existing DEC-0005 and DEC-0009 obligations remain. No complete strict validation composition is implemented or re-evaluated under this authorization.

Do not select ICU4J or another IDNA library; admit dependencies to Gradle; implement IDNA2008 or Login; change backend implementation, API Contracts or OpenAPI; choose UTS #46; or copy/fork/vendor/modify/reimplement ICU codec source. Do not reopen `java.net.IDN` or GNU Libidn evaluation except source review directly necessary to preserve an already-recorded comparison; do not rerun their probes or treat them as renewed candidates. Native integration and other candidate searches are outside scope. No unrelated Product, Architecture, Security, persistence, Session, Authentication, provider, infrastructure or operational decision may be resolved.

### Required Output, Stop Conditions and Exit Criteria

Return one durable, reproducible evidence report addressing each permitted investigation item, the finite comparison inventory, dated authoritative sources, commands and actual probe results where executed, limitations, residual risks, proposed compatibility/security verification obligations, and owner/review needs. Preserve the prior stopped evidence by reference rather than rewriting it. Do not fabricate measurements, maintenance commitments, reviews or approvals.

The single investigation ends when the bounded evidence is assessed. It must stop rather than expand if answering the question requires prohibited implementation work, mapping semantics, a different candidate, production integration, repository dependency admission, or further investigation beyond the recorded finite inventory. Missing access, missing support evidence or inability to investigate safely must be reported. Any follow-up beyond this scope requires new explicit authorization.

The report MUST terminate with exactly one evidence-backed outcome:

- **A. SUPPORTABILITY BASIS DEMONSTRATED** — direct internal codec use remains eligible for further governance consideration. Evidence must support an explicit compatibility, security-maintenance and upgrade basis, mapping isolation, viability without copied/forked/vendored codec code, and defensible verification and maintenance responsibilities. This is not a claim that the API is public or guaranteed stable.
- **B. SUPPORTABILITY BASIS NOT DEMONSTRATED** — direct internal codec use is removed from the candidate path. Explain the evidenced supportability deficiencies or incompatibility with the required boundaries; do not generalize this to all possible compositions.
- **C. EVALUATION STOPPED** — evidence is insufficient or the bounded investigation cannot safely establish either A or B. Identify the precise missing evidence or triggered scope boundary without selecting an alternative or inventing further authority.

None of A/B/C accepts DEC-0009, admits ICU4J, approves production internal-API use, selects a final strategy, or authorizes implementation. The 0.5.0 authorization was exhausted by the completed report; the broader strict-profile acceptance requirements and separate dependency/Architecture gates remain unresolved.

## Completed Codec-Supportability Evaluation — 0.6.0 Proposed Disposition

The evaluation authorized by 0.5.0 was executed on 2026-10-03 against repository baseline `98964310b5ccc5aebfc9682e3d2f18605a6f9825`. Its [durable supportability evidence](evidence/DEC-0009/icu-punycode-supportability-evaluation-2026-10-03.md) records the bounded inventory, upstream policy and maintenance sources, artifact hashes, reproducible probes, actual results, limitations and reasoning. The exact terminal outcome is **B. SUPPORTABILITY BASIS NOT DEMONSTRATED**.

Accordingly, direct use of `com.ibm.icu.impl.Punycode` from ICU4J 78.3 is **removed from the current candidate path** and is no longer an eligible current candidate under the evaluated constraints. The 0.5.0 evaluation is complete; its authorization is exhausted and cannot be reused. This synchronization authorizes no further evidence evaluation and selects no next strategy.

The evidence supports this consequence for the following reasons:

- Raw codec functionality was demonstrated in the sampled Java 21 evaluation of ICU4J 74.2, 77.1 and 78.3; the inspected codec source, public signatures and probe outputs were identical across those releases.
- Direct invocation could remain isolated from UTS #46 processing. That positive result establishes neither strict IDNA2008 validation nor a supported external API contract.
- Sampled historical compatibility does not establish forward compatibility. `com.ibm.icu.impl` is an upstream internal interface without the external compatibility/support commitment required for this path; availability and general ICU maintenance do not supply that commitment.
- Discretionary maintenance/backports and the absence of a demonstrated external upgrade/remediation basis leave a material supportability gap. Repository-owned detection gates can identify incompatibility but cannot themselves provide a compatible remediation path when a required security upgrade breaks the internal interface, within the no-copy/no-fork constraints.
- Therefore, the required supportability basis was not demonstrated. This is not a finding that raw conversion failed or that a break was observed in the sampled releases.

Outcome B rejects only this direct ICU4J internal codec candidate under the evaluated constraints. It does not establish that ICU4J as a whole, all Punycode implementations or every external library is unsuitable, that strict IDNA2008 implementation is impossible, or that every future repository-owned composition is impossible. The prior 0.4.0 stopped-composition evidence and its narrower conclusion remain unchanged; Outcome B was not part of that earlier finding.

All unresolved strict-profile obligations remain: RFC 5890/5891/5892/5893; CONTEXTJ/CONTEXTO; domain-aware Bidi; A-label/U-label validation; no-mapping semantics; Unicode property/version strategy; unassigned-code-point handling; resource bounds; canonical comparison-key construction; and retained compatibility/upgrade requirements. Raw codec supportability and complete validation remain distinct. The evidence reports are preserved unchanged.

DEC-0009 remains a Proposed, Identity-owned Security Decision with `Authoritative: false`. This disposition neither accepts nor rejects DEC-0009 itself. No technology selection, dependency admission, backend or Login implementation, Contract/OpenAPI change, or unrelated policy decision follows. Any next strategy or evidence evaluation requires separate explicit authorization; the existing acceptance, dependency and applicable Architecture gates remain mandatory.

## Bounded Native Feasibility and Admission-Readiness Evaluation Authorization — 0.7.0 Proposed

### Authorized Evidence Question and Ownership

Version 0.7.0 authorizes exactly one isolated, bounded technical evidence evaluation of the following question:

> Can an exact, maintained native implementation, through a supported public API and defensible Java 21 integration, satisfy the complete governed strict no-mapping IDNA2008 profile—with explicit Unicode policy, resource bounds and retained-identifier upgrade behavior—while meeting repository provenance, licensing, support, reproducibility, deployment and failure-isolation requirements? Which responsibilities remain repository-owned, and would the resulting integration require an Architecture change?

Identity owns this evaluation. Security reviews conformance, hostile-input safety and supply-chain evidence; Architecture reviews integration, runtime, platform, deployment and operational implications; Engineering reviews reproducibility, supportability and maintenance responsibilities. This revision records the bounded authorization, not completed reviews or reviewer identities. It executes no evaluation and creates no evidence artifact.

The 0.3.0 stopped composition and exhausted authorization remain unchanged. The 0.5.0 evaluation remains complete with **B. SUPPORTABILITY BASIS NOT DEMONSTRATED** and exhausted authorization. Direct ICU4J 78.3 `com.ibm.icu.impl.Punycode` use remains removed from the current candidate path. This evaluation MUST NOT reopen either path through renaming, wrapping or repackaging. It does not establish that all future compositions are impossible.

### Candidate Inventory, Isolation and Execution Boundary

Before experimental execution, record the exact native release/source revision, public API, proposed JVM binding or minimal public-API bridge, Unicode data, provenance, configuration, target platform/ABI inventory, responsibility allocation, bounded corpus and resource hypotheses for Identity-owned scope review with Security, Architecture and Engineering. Candidate inventory is evidence scope, not technology selection. Historical native leads are not evidence of current maintenance, suitability or admission. If no supported candidate and bounded integration can be established, report the gap and stop rather than expand into unrelated candidate families.

Experiments MUST remain in isolated external/scratch work using synthetic inputs. A minimal experimental Java 21 bridge may invoke the identified supported native public API solely to obtain evidence; it MUST NOT implement missing IDNA/Punycode behavior, become a production adapter, or change repository build/runtime configuration. No external service, remote identity processing, copied/forked implementation, custom codec or repository-owned protocol implementation is authorized. Inspection and probes must not depend on undocumented/internal APIs as a substitute for a supported integration basis.

Do not wire experiments into Customer Login, authoritative lookup, persistence, Authentication, Principal, Session, HTTP or Contracts. Do not change `CustomerLoginEmailInput`, production/test code, dependencies, locks, verification metadata, CI, containers or deployment configuration. Do not process real Customer data. Existing test-scoped/native-related artifacts or checksums do not establish production admission. Missing behavior must be reported, not silently filled in to make the candidate pass.

### Required Evidence Boundaries

The evaluation MUST address all existing Evaluation Criteria and Required Evidence above and explicitly report the following:

1. **Complete strict conformance:** trace RFC 5890/5891/5892/5893 obligations to exact public API calls, flags, defaults and results. Cover derived properties/exceptions, CONTEXTJ, CONTEXTO, domain-aware bidi, U-label validity, A-label decode/validate/re-encode and canonical comparison, malformed/fake `xn--` labels, hyphen and encoded-length constraints, malformed Unicode and encoding boundaries. Use source-attributed positive, negative and boundary cases. Distinguish label checks from applicable cross-label/domain checks. A codec or successful conversion alone is not conformance.
2. **No-mapping and comparison semantics:** demonstrate rejection rather than normalization/repair, UTS #46 substitution, width folding, ignored-character removal or alternative-separator mapping. Include sharp-s, sigma, decomposed forms, width variants, ignored characters, join controls and bidi counterexamples. Preserve ASCII-dot separation and DEC-0005's local-part handling, dots and plus tags. Demonstrate canonical comparison-key consequences without authorizing lookup. Do not use JDK `java.net.IDN`, ASCII-only fallback or approximate equality to cover gaps.
3. **Unicode and lifecycle:** identify exact Unicode/property/normalization tables, provenance, unassigned handling and runtime dependencies. Establish determinism across evaluated Java 21/platform/locale configurations and distinguish tested from untested targets. Assess implementation/data updates, security upgrades, rollback, retained-identifier acceptance/key stability, collision detection and reconciliation obligations. No Unicode version, migration algorithm or new Product policy is selected.
4. **Native public API and supportability:** establish documented supported entry points, stability/compatibility commitments, maintenance and security-response evidence, error contracts, buffer ownership, allocation/free rules, encoding and length conventions, thread safety/reentrancy, global state and initialization/cleanup. Examine malformed input, overflow, memory exhaustion, crashes and resource consumption. Historical success or release pinning alone is not a forward support/remediation basis.
5. **Java 21 integration:** identify exact binding/bridge and any transitive components, supported Java 21 mechanism, encoding conversion and malformed UTF-16 handling, loading/linking behavior, ABI compatibility, platform/architecture availability, native-access requirements and cleanup. Test bounded failure behavior where safe and report unsupported/unexecuted cases. Do not assume a mechanism requiring a newer Java baseline is available. Distinguish library errors from process failure and assess default denial without logging protected input.
6. **Supply chain and admission readiness:** inventory native/binding/data artifacts and exact releases, source/build/binary provenance, published integrity/signature evidence, licenses and redistribution obligations, transitive/system libraries, maintenance/security history and remediation path. Identify reproducible acquisition/build/packaging and inventory/SBOM/security-review obligations, plus how applicable fixed versions, locks and reviewed verification would be maintained. Do not treat operating-system availability, package-manager installation or scratch acquisition as repository admission.
7. **Architecture and operations:** assess JNI/JNA or other binding consequences, supported OS/CPU/libc/ABI combinations, packaging/container implications, runtime loading/search paths and integrity, startup failure, concurrency, process isolation, resource bounds, patching, rollback, diagnostics and operational ownership. Record portability and deployment constraints without selecting hosting, new permissions, thresholds, retry policy or infrastructure. Identify whether a material Architecture change and ADR would be required.
8. **Repository responsibilities:** provide an explicit external-versus-repository responsibility matrix for every strict-profile obligation, canonical-key composition, Unicode maintenance, failure handling and lifecycle compatibility. Identify missing behavior, ownership, continuing cost and security burden. Any need for additional custom validation/codec work outside this scope is a gap and stop condition, not implicit permission to revive the stopped composition.

Preserve dated authoritative sources, commands, exact configurations, corpus provenance, expected and actual results, resource observations, untested cases and reproduction instructions in the later evaluation report. Distinguish upstream claims from executed evidence and residual uncertainty. Sampled probes MUST NOT be represented as exhaustive conformance, comprehensive security clearance or production readiness.

### Separate Admission, Architecture and Acceptance Gates

This authorization admits no dependency. Adding any native, JVM binding, data, build, test or runtime artifact to the repository requires separate DEC-0001-governed dependency admission, including applicable fixed versions, transitive review, license/security review, locking, reviewed verification and build compatibility evidence. Preserve Java 21, Gradle 8.14.5 and Spring Boot 3.5.16. Scratch evidence does not bypass these controls or admit system/native binaries outside them.

Architecture assessment is separately required for native integration and its runtime, platform, ABI, packaging, deployment and failure-model implications. If the eventual strategy materially changes Architecture, an Accepted ADR and governed Architecture synchronization are required before affected implementation. This authorization grants no Architecture approval and changes no Architecture artifact.

Even successful evaluation is **not technology selection, dependency admission, DEC-0009 acceptance, Architecture approval, or implementation authorization**. Those remain separate reviewed stages. Product/Security requirements, DEC-0005 semantics, Contracts and persistence authority remain unchanged. Login canonicalization and authoritative lookup remain blocked pending the required gates.

### Terminal Outcomes and Stop Conditions

The single evaluation MUST end with exactly one of these outcomes, with reproducible evidence and remaining gaps:

- **A. FEASIBILITY AND ADMISSION-READINESS BASIS DEMONSTRATED:** the exact evaluated native/public-API/Java 21 arrangement has sufficient evidence across the required boundaries to support a later strategy decision and separate admission/Architecture reviews. State limitations and residual risks; A itself selects and approves nothing.
- **B. FEASIBILITY OR ADMISSION-READINESS BASIS NOT DEMONSTRATED:** evidence identifies an unmet required obligation or an unsupported/unacceptable integration, maintenance, supply-chain or operational basis under this scope. Explain the candidate-specific reasons; do not infer universal impossibility or weaken requirements.
- **C. INCONCLUSIVE WITHIN AUTHORIZED BOUNDARY:** evidence cannot resolve the question within the bounded scope, available supported components, safe experiments or reproducibility constraints. Identify missing evidence and any separately governed next question; do not continue under this authorization.

Stop affected experimentation immediately if it requires prohibited mapping/repair or policy weakening; revival of either exhausted path; internal/unsupported API dependence; copied/forked/custom codec or missing protocol implementation; a newer Java baseline; production integration or repository dependency/configuration changes; external-service processing or real Customer data; weakening verification/security controls; unreviewed Architecture/deployment changes; unsafe resource/process behavior that cannot be contained in scratch work; or expansion beyond the reviewed candidate inventory/evidence scope. Record the trigger and terminate with B or C as supported by the evidence, rather than silently substitute components or expand the work.

Completion, a stop disposition or exhaustion of the reviewed boundary consumes this one authorization. Follow-up experiments, a materially different candidate scope or another strategy require fresh explicit authorization. No automatic renewal follows from A, B or C.

## Security and Authority Impact

Identity remains the owner of Authentication identifier interpretation and canonical comparison. Identity is the single accountable owner of this record. Acceptance requires Security review and approval, Architecture review and approval applicable to this Security Decision, and Engineering review of implementation, dependency, reproducibility, and testing evidence. Approval evidence must be durable and discoverable; no reviewers or completed approvals are asserted here.

Evaluation must assess attacker-controlled inputs, resource exhaustion, pathological conversion/validation behavior, malformed encodings, error handling, and unsafe library defaults. Record performance and resource evidence without selecting numerical rate limits, timeouts, or service-level policy.

Failures must preserve default denial, enumeration resistance, and privacy. Inputs, canonical values, protected existence, and internal dependency details must not leak through errors, logs, telemetry, or ordinary support evidence. Tests must use synthetic, non-customer data. Evaluate confusable-character risks without inventing confusable rejection or rewriting policy.

Preparation, canonicalization, lookup, and credential `MATCH` remain distinct from Authentication. No Customer or Account truth moves into Identity, and no association cardinality is established. ADR-0021 local Authentication authority and ADR-0022 owner boundaries remain unchanged.

A new ADR is required only if the eventual selected solution materially changes Architecture. This proposal creates no ADR and makes no Architecture change.

## Data, Compatibility, Migration, and Reversibility Impact

No schema, stored identifier, migration, or production data changes in this proposal. Future selection must explain how canonical-key stability will be protected for retained authoritative identifiers. Evaluate changed acceptance, changed canonical output, collisions, and ambiguous retained ownership across Unicode-data or implementation upgrades.

An upgrade or rollback must not silently merge identities, transfer or reuse identifier ownership, select an arbitrary conflicting record, restore withdrawn access, or represent uncertain lookup as success. Require compatibility comparison and governed reconciliation before applying a change that affects retained identifiers. This is an evidence obligation, not selection of a migration algorithm, retention period, or reconciliation implementation.

Reversibility depends on stable semantics and explicit compatibility evidence. Replacing an implementation after identifiers are retained may require substantially more review than changing an unused dependency. Record that cost without fabricating an existing canonical-identifier population or migration result.

## Dependency and Operational Impact

If the accepted strategy selects a new third-party artifact, separate dependency admission under DEC-0001 is required. Admission must address exact coordinates/version, dependency-management compatibility, transitive impact, licensing and security review, dependency locks, reviewed verification metadata, and applicable build/test evidence. Acceptance of this DEC would not itself admit the dependency. Adding an artifact to repository build, test, or runtime configuration requires applicable dependency admission. Isolated external/scratch evaluation does not itself admit a production dependency and remains subject to its separately authorized investigation scope.

This Proposed record adds no library and changes no build, lock, verification, runtime, or operational configuration. Evaluate maintenance and security-response responsibilities, reproducible upgrades, rollback limitations, and safe diagnosis without selecting operational thresholds, retry policy, or hosting infrastructure.

## Consequences

The proposal makes the missing technology decision explicit and gives reviewers a testable basis for comparing candidates while preserving DEC-0005. It separates preparation from canonical authority and prevents convenient but incompatible lookup implementations.

The cost is evaluation and review work before canonicalization can proceed. Candidate claims may prove incomplete, Unicode-data changes may impose continuing compatibility work, and no suitable candidate may emerge. Proposal registration does not remove the current Login dependency blocker or imply that an eventual selection will pass acceptance.

## Acceptance Gates and Separate Delivery Stages

1. **Proposal creation:** create this `0.1.0 Proposed`, non-authoritative record and register it in the Decision Index in the same change. This stage selects no implementation and claims no evaluation results.
2. **Technical evaluation:** separately collect the reproducible candidate and conformance evidence above. Record exact gaps, resource/security analysis, Unicode-version behavior, compatibility/rollback implications, and dependency impact. Evaluation is not acceptance or production dependency admission.
3. **Acceptance:** only after evidence supports a specific implementation strategy may a later revision state the selected implementation, exact evaluated release/configuration/data behavior, rationale, alternatives, residual risks, and review evidence. Obtain Identity ownership confirmation, Security and Architecture approval, and Engineering review. Synchronize status and history in this record and `DECISIONS.md`; synchronize other canonical sources only where the accepted choice actually requires it. Unresolved conformance or security gaps block acceptance rather than being silently waived.
4. **Dependency admission:** if a new third-party artifact is selected, perform the separate DEC-0001-governed admission with lock, verification, security, and compatibility evidence. Neither proposal nor acceptance substitutes for this stage.
5. **Implementation:** only after the required preceding gates, separately implement and verify strict validation/canonicalization within the governed Identity boundaries. Preserve preparation tests, architecture direction, safe failures, and the canonical-key-before-lookup requirement. No Authentication or HTTP success follows from canonicalization alone.

The 0.3.0 bounded evaluation is complete and stopped. The separate 0.5.0 codec-supportability evaluation is also complete, with Outcome B and exhausted authorization; direct ICU4J 78.3 internal Punycode use is removed from the current candidate path. Version 0.7.0 separately authorizes only the single bounded native evaluation above; it has not been executed by this revision, and the broader technical-evaluation acceptance requirements remain unsatisfied. Completion of this evidence-gathering exercise does not complete acceptance, dependency admission, or implementation. These stages must not be represented as one completed selection/admission/implementation outcome. This proposal requires no synchronization into Architecture, Product, Contracts, OpenAPI, or other governing files.

## Explicit Non-Decisions

DEC-0009 in Proposed state does not select or authorize:

- a library, implementation, version, Unicode-data version, configuration, dependency admission, production IDNA2008 implementation, or production canonicalization;
- a persistence schema, migration, authoritative identifier repository, or lookup implementation;
- Authentication orchestration, Principal establishment, Session establishment, Session lifetime, cookie attributes, or HTTP Login implementation;
- Customer/Account business state, transfer of Customer authority, or association cardinality;
- email-verification delivery, tokens, or other verification mechanics;
- recovery, MFA, external IdP, password-change behavior, or Product Decision 28;
- numerical rate limits, lockout thresholds, retry policy, timeout policy, TTLs, or SLOs;
- confusable-character rejection or rewriting policy beyond existing governing authority;
- changes to DEC-0005, Contracts, OpenAPI, Java code, build files, dependency metadata, or existing security policy; or
- technical evaluation results, acceptance, completed implementation, Authentication, or deployment readiness.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [TESTING-STANDARDS.md](../../.ai/core/TESTING-STANDARDS.md)
- [CODING-STANDARDS.md](../../.ai/core/CODING-STANDARDS.md)
- [ENGINEERING-PRINCIPLES.md](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [JAVA.md](../../.ai/backend/JAVA.md)
- [API.md](../../.ai/backend/API.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0004 — Customer Login Identifier Semantics](DEC-0004-customer-login-identifier-semantics.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0008 — Initial Customer Email Verification Policy](DEC-0008-initial-customer-email-verification-policy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [ADR-0022 — Initial Identity–Customer/Account Association Strategy](../adr/ADR-0022-identity-customer-account-association-strategy.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Initial Customer Login Contract](../contracts/identity/initial-customer-login-contract.md)
- [Initial Customer Login OpenAPI Contract](../contracts/identity/initial-customer-login.openapi.yaml)
- [CustomerLoginEmailInput preparation boundary](../../backend/src/main/java/com/enterprise/fashion/ecommerce/identity/domain/model/CustomerLoginEmailInput.java)
- [CustomerLoginEmailInput tests](../../backend/src/test/java/com/enterprise/fashion/ecommerce/identity/domain/model/CustomerLoginEmailInputTest.java)

External standards define the evaluation basis only; they are not evidence that any candidate passed:

- [RFC 5890 — IDNA Definitions and Document Framework](https://www.rfc-editor.org/rfc/rfc5890.html)
- [RFC 5891 — IDNA Protocol](https://www.rfc-editor.org/rfc/rfc5891.html)
- [RFC 5892 — Unicode Code Points and IDNA](https://www.rfc-editor.org/rfc/rfc5892.html)
- [RFC 5893 — Right-to-Left Scripts for IDNA](https://www.rfc-editor.org/rfc/rfc5893.html)
- [RFC 5894 — IDNA Background, Explanation, and Rationale](https://www.rfc-editor.org/rfc/rfc5894.html)
- [RFC 3490 — IDNA2003 comparison basis](https://www.rfc-editor.org/rfc/rfc3490.html)
- [Unicode UTS #46 — Unicode IDNA Compatibility Processing](https://www.unicode.org/reports/tr46/)

The later evaluation must identify applicable updates, errata, standard/data versions, and the dated sources used. No candidate conformance is inferred from these references.

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.7.0 | 2026-10-04 | Proposed | Authorized exactly one isolated native strict-IDNA2008 feasibility and admission-readiness evaluation with complete evidence boundaries, explicit A/B/C outcomes and stop conditions; preserved both exhausted authorizations, stopped-composition evidence and ICU internal-codec Outcome B; selected no technology, admitted no dependency, granted no Architecture approval, and authorized no production implementation or DEC acceptance. |
| 0.6.0 | 2026-10-04 | Proposed | Recorded the completed 0.5.0 evaluation and B. SUPPORTABILITY BASIS NOT DEMONSTRATED; marked its authorization exhausted and removed direct ICU4J 78.3 internal Punycode use from the current candidate path; preserved both evidence records and unresolved strict-profile obligations; authorized no further evaluation, selected no technology, admitted no dependency, and neither accepted nor rejected DEC-0009. |
| 0.5.0 | 2026-10-03 | Proposed | Authorized one isolated evidence evaluation of direct ICU4J 78.3 internal Punycode codec supportability, compatibility, security maintenance, upgrade basis and UTS #46 isolation, with explicit A/B/C exits; preserved the exhausted 0.3.0 authorization and 0.4.0 stopped evidence; selected no technology, admitted no dependency, and authorized no production implementation or acceptance. |
| 0.4.0 | 2026-10-02 | Proposed | Durably recorded the completed 0.3.0 evaluation and scope-limited stopped-path disposition, demonstrated component findings, unexecuted validation and unresolved blockers; exhausted the single authorization and retained a recommendation-only codec-supportability question; authorized no further evaluation, selection, dependency admission, or production implementation. |
| 0.3.0 | 2026-10-01 | Proposed | Authorized one bounded, isolated repository-owned strict-IDNA2008 technical evaluation for evidence gathering with ownership/review, component/codec, conformance, isolation, dependency, Architecture, reporting, and stop-condition boundaries; selected no strategy, library, or Unicode version, admitted no dependency, and authorized no production implementation or acceptance. |
| 0.2.0 | 2026-10-01 | Proposed | Clarified Proposed scope to preserve prohibitions on improvised/approximate IDNA fallbacks while permitting future separately authorized evaluation of bounded repository-owned strict validation; required explicit ownership, review, evidence, isolation, and stop conditions before a spike; preserved separate dependency and Architecture governance; selected no implementation, admitted no dependency, authorized no spike or production implementation, and retained Proposed status. |
| 0.1.0 | 2026-10-01 | Proposed | Created the strict Customer Login IDNA2008 implementation-strategy proposal with evaluation criteria, alternatives, reproducible evidence requirements, acceptance gates, authority boundaries, and separate dependency-admission and implementation stages; selected no implementation and claimed no technical evaluation or acceptance. |
