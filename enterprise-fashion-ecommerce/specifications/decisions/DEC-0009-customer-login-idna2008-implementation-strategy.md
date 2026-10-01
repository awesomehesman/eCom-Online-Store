# DEC-0009 — Customer Login IDNA2008 Implementation Strategy

- **Identifier:** DEC-0009
- **Title:** Customer Login IDNA2008 Implementation Strategy
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.2.0
- **Date:** 2026-10-01
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

This proposal defines the evaluation and acceptance boundary only. **No library, implementation, candidate release, configuration, or Unicode version is selected.** No candidate has passed evaluation through this record, and no technical spike, benchmark, conformance execution, approval, or dependency admission is claimed.

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

DEC-0009 MAY evaluate a precisely bounded repository-owned strict validation composition when no maintained complete implementation has been established, but only after explicit, separately reviewed spike authorization. This Proposed-scope clarification does not authorize that spike, select this direction, or establish that repository-owned validation is safe or preferred. Substantial repository-owned protocol validation creates security and continuing maintenance responsibilities; it must not be presented as an easy wrapper.

Before execution, the future authorization MUST identify:

- the exact investigation scope, Identity ownership, and Security, Architecture, and Engineering review;
- exact third-party components and data, if any, their Unicode/property-data versions, the Punycode/codec strategy, and each validation responsibility that would be repository-owned;
- a strict conformance evidence plan and source-attributed counterexample/regression corpus, including CONTEXTJ/CONTEXTO, RFC 5893 and applicable cross-label bidi behavior, and A-label decode/validate/re-encode evidence;
- malformed-input and resource-exhaustion hypotheses, and privacy-preserving, enumeration-safe failure requirements;
- implementation/data upgrade and rollback considerations, retained-identifier compatibility, and collision/reconciliation considerations;
- isolation from production lookup, persistence, Authentication, Principal, Session, and HTTP behavior; and
- objective stop conditions, including required semantic weakening, missing protocol behavior outside reviewed scope, unauthorized codec work, production integration, unapproved dependency adoption, or exhaustion of the authorized investigation boundary.

Permission to evaluate repository-owned validation does NOT authorize repository-owned Punycode implementation. Any codec strategy requires separate explicit evaluation. Repository-owned logic MUST NOT silently implement missing protocol behavior without reviewed scope. The spike must return reproducible evidence and unresolved gaps rather than create implementation authority; failed or incomplete evidence must not be represented as conformance or permission to proceed to production.

### F. Native Implementation Evaluation

A maintained native implementation exposed through a governed JVM integration remains a possible evaluation direction, subject to separately reviewed investigation scope and authorization. Exact native and JVM components, profile/data configuration, provenance, Java compatibility, packaging, failure behavior, and maintenance responsibilities require evidence. Native/JNI/JNA/runtime/deployment/platform/ABI changes require Architecture assessment; an ADR is required if the selected strategy materially changes Architecture. This clarification authorizes no native strategy or spike and makes no Architecture change.

Repository-owned composition, native implementation, a future maintained implementation, and deferral remain unselected directions. DEC-0009 remains Proposed until an actual implementation strategy satisfies its acceptance gates and receives the required authority.

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

No stage is claimed complete beyond proposal creation. These stages must not be represented as one completed selection/admission/implementation outcome. This proposal requires no synchronization into Architecture, Product, Contracts, OpenAPI, or other governing files.

## Explicit Non-Decisions

DEC-0009 in Proposed state does not select or authorize:

- a library, implementation, version, Unicode-data version, configuration, dependency admission, IDNA2008 implementation, or complete canonicalization;
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
| 0.2.0 | 2026-10-01 | Proposed | Clarified Proposed scope to preserve prohibitions on improvised/approximate IDNA fallbacks while permitting future separately authorized evaluation of bounded repository-owned strict validation; required explicit ownership, review, evidence, isolation, and stop conditions before a spike; preserved separate dependency and Architecture governance; selected no implementation, admitted no dependency, authorized no spike or production implementation, and retained Proposed status. |
| 0.1.0 | 2026-10-01 | Proposed | Created the strict Customer Login IDNA2008 implementation-strategy proposal with evaluation criteria, alternatives, reproducible evidence requirements, acceptance gates, authority boundaries, and separate dependency-admission and implementation stages; selected no implementation and claimed no technical evaluation or acceptance. |
