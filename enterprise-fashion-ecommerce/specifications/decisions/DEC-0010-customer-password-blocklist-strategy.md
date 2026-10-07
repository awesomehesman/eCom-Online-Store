# DEC-0010 — Customer Password Blocklist Strategy

- **Identifier:** DEC-0010
- **Title:** Customer Password Blocklist Strategy
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.3.0
- **Date:** 2026-10-07
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted, authoritative DEC-0007 already requires every prospective Customer password establishment or change to compare the complete NFC-normalized password against a governed blocklist of known commonly used, expected, or compromised passwords. Matching whole-password values must be rejected; mere substring presence does not establish a match. Unavailable, failed, stale beyond governed operating conditions, or uncertain evaluation fails closed. Credential creation or replacement cannot proceed without successful required policy evaluation, and outcomes must remain distinguishable and safely retryable without unsafe disclosure.

At canonical baseline `a4e59eb451b866c270f18cedf6d3a94c96d5936c`, PR #128 supplies `PreparedProspectiveCustomerPassword`. It rejects malformed UTF-16, normalizes the complete prospective password to NFC, and enforces the post-normalization 15–64 Unicode-code-point structural bounds. It explicitly awaits remaining establishment policy checks and does not establish complete password-policy acceptance. The implementation preserves whitespace and case and does not truncate the password.

DEC-0006 governs Argon2id credential hashing and verification mechanics. Hashing or successful verifier matching is not blocklist approval. No governed blocklist source, provider, dataset, API, library, service, or update mechanism is currently selected, and no production blocklist checker exists at this baseline. Identity owns policy enforcement; Customer retains Registration coordination and Customer/Account business truth under ADR-0022 and the Initial Customer Registration Contract.

These are accepted upstream requirements and inspected repository facts, not evidence that any candidate strategy is compliant. Candidate suitability, coverage, freshness, supportability, privacy, and operational feasibility remain open evidence questions. The completed documentary investigation and its limitations are recorded in the 0.3.0 disposition below; no unrecorded candidate-specific findings are asserted.

DEC-0009 remains 0.8.0 Proposed, non-authoritative and deferred pending materially new evidence and fresh explicit authorization. This separate password decision neither reopens its exhausted evaluations nor bypasses strict IDNA2008 validation, canonical comparison-key construction, or authoritative login-email lookup.

## Decision Question

What Identity-owned blocklist strategy and governed operating conditions will reliably evaluate the complete NFC-normalized prospective Customer password against known commonly used, expected, or compromised values, while preserving DEC-0007's rejection, fail-closed, privacy, and safe-retry requirements?

## Decision — Proposed Governance and Evidence Boundary

This proposal defines the decision problem, evidence requirements, authority boundaries, and later review gates. Version 0.2.0 authorized exactly one bounded documentation/static evidence evaluation. That evaluation is complete and its authorization is exhausted. Version 0.3.0 synchronizes the recorded outcome only and grants no renewed evaluation authority. **No blocklist strategy, source, provider, dataset, service, library, dependency, or implementation mechanism is selected.** The record is Proposed and non-authoritative; accepted upstream requirements remain authoritative independently of this proposal.

A future selection must be supported by attributable evidence against the requirements below and undergo the applicable decision lifecycle and approvals. A list of candidate classes, a successful experiment, or acceptance of an evaluation scope is not strategy selection, dependency admission, provider approval, Architecture approval, or implementation authorization.

## Future Evaluation Requirements

Future separately authorized evaluation must address each applicable obligation and explain any inapplicability rather than silently omit it:

| Area | Required evidence before selection |
| --- | --- |
| Coverage and comparison | Coverage appropriate to commonly used, expected, or compromised passwords under DEC-0007; exact whole-password match semantics; behavior for complete NFC-normalized input, Unicode, case and significant whitespace; no truncation or substring substitution. Source representation must be demonstrably compatible with the prepared value rather than silently changing accepted password semantics. |
| Secret handling and privacy | Data flow for prospective passwords and derived lookup material; whether either leaves the application or process trust boundary; disclosure, retention, access and correlation risks; purpose limitation and minimization. No external disclosure is presumed acceptable. |
| Provenance and integrity | Identifiable source and ownership, provenance of data and executable artifacts, integrity and authenticity verification where applicable, resistance to tampering or substitution, and response to source compromise. |
| Support and supply chain | Maintenance/support status, licensing and usage constraints where applicable, dependency and transitive-artifact implications, security posture, abandonment risk and an accountable maintenance owner. |
| Versioning and freshness | An attributable version/update model and evidence sufficient to define when checking remains current, when it becomes stale, and how updates, failed updates and compatibility are handled. This obligation selects no cadence, cache, scheduler or API. |
| Determinism and reproduction | Deterministic or otherwise governable decisions with attributable inputs and source state; reproducible acquisition and checking evidence where applicable; sufficient safe evidence to explain results without retaining password material. |
| Resources and availability | Bounded execution, memory, storage and network behavior as applicable; availability and failure modes; distinguishable rejection, unavailable, failed, stale and uncertain outcomes; fail-closed enforcement and safe retry without invented success. |
| Testing | Testable accepted and rejected whole-password cases, representation compatibility, failure, staleness, uncertainty, update/rollback and boundary behavior; isolated non-production evidence without real Customer secrets. |
| Recovery and lifecycle | Update and rollback integrity, recovery after compromise or unavailability, compatibility/migration effects, withdrawal and replacement responsibilities, and prevention of rollback into untrustworthy evidence. |
| Operations and Architecture | Java 21 compatibility where relevant; current modular/hexagonal Architecture compatibility; process, runtime, deployment, network and trust-boundary implications; operational ownership, support and secret-safe observability. |

Evidence must distinguish verified observations, assumptions, limitations and unresolved questions. No candidate may be represented as compliant solely because it is local, popular, maintained, remotely available, or described as a compromised-password service. Unsupported coverage or freshness claims cannot satisfy the decision question.

## Alternatives Considered — Candidate Classes Only

Two candidate arrangements were investigated under the now-exhausted 0.2.0 authorization, as recorded below; neither was selected. These historical candidate classes identify possible comparisons, not equal suitability or current permission to investigate:

- **Repository-controlled/local blocklist data:** assess coverage, provenance, distribution integrity, resource bounds, updates and repository maintenance responsibility; locality alone does not prove adequacy or freshness.
- **Supported third-party dataset:** assess coverage, licensing, provenance, versioning, update continuity and integrity; third-party publication alone does not establish trust.
- **Supported library/component carrying data or checking capability:** assess both checking semantics and data suitability, Java compatibility, maintenance and executable supply-chain consequences; dependency availability does not establish policy compliance.
- **Remote service/provider:** assess coverage, availability, observable behavior, secret/derived-data disclosure, trust boundaries, privacy, contractual conditions and exit costs before any selection; remote checking is not presumed permissible.
- **Another demonstrably supportable strategy:** require the same evidence and explicit justification of any different risks or boundaries, without treating this category as open-ended evaluation authority.
- **Continue deferral without selection:** preserves the current absence of an approved mechanism but leaves complete password-policy acceptance and dependent credential establishment blocked. Skipping evaluation or accepting uncertain results is prohibited by DEC-0007, not an available fallback.

## Security and Data Impact

The proposed process preserves DEC-0007 rather than revising its policy. A future mechanism must not log or persist plaintext prospective passwords, expose them in diagnostics, silently skip required evaluation, weaken whole-password matching to substring matching, treat failure/unavailability/staleness/uncertainty as acceptance, or treat structural preparation as complete acceptance. The accepted NFC and 15–64 bounds remain unchanged. Submitted and normalized secrets and sensitive lookup material must remain excluded from unsafe logs, metrics, traces, events, errors and support evidence under existing Security authority.

Any mechanism that could transmit password-derived or password-equivalent information outside the application/process boundary requires explicit trust-boundary, privacy and Security evaluation before selection. Transformation alone is not evidence that disclosure is safe. No such disclosure, retention, candidate-service runtime network access, or provider credential is authorized here. The historical public-documentation permission below is exhausted; no further candidate investigation is authorized.

This decision concerns prospective establishment/change only. It does not introduce blocklist or establishment-length checks into Login-time verification, reinterpret existing accepted credentials, or modify DEC-0006 verification semantics. Policy acceptance establishes no Authentication, Principal, Session, Registration completion or Authorization proof.

## Authority Boundaries

Identity retains the password-policy enforcement boundary. The dependency sequence remains:

```text
PreparedProspectiveCustomerPassword
→ remaining governed password-policy evaluation
→ complete password-policy acceptance
→ DEC-0006 credential establishment/hashing
→ authoritative credential state
→ persistence
```

These are distinct responsibilities, not a selected transaction or persistence design. Hash generation does not establish authoritative credential ownership/currentness or confirmed persistence. Customer coordination cannot reinterpret Identity policy evidence or store password material as Customer state.

Credential ownership/currentness modeling, credential persistence, Registration orchestration, Customer lifecycle, Authentication, Principal, Session, Authorization, email verification, email canonicalization, IDNA2008, recovery, MFA, and external IdP/social login remain outside this decision. Existing DEC-0003 through DEC-0008, ADR-0017 through ADR-0022, Domain/backend Specifications and Registration Contracts retain their authority.

## Dependency and Architecture Gates

DEC-0001 remains authoritative for executable third-party dependency admission, version control, locking, verification and build evidence. Future strategy acceptance does not itself admit any dependency or native artifact. Necessary admission must be reviewed separately before production use, preserving provenance, licensing, maintenance and Security controls.

Acceptance of DEC-0010 must not automatically approve a provider, external service, native artifact, Architecture change or deployment-topology change. Applicable provider, integration, privacy, operational and admission reviews remain separate gates.

Architecture must assess any proposed change to trust boundaries, runtime/deployment topology or process boundaries. A material Architecture change requires a separate Accepted ADR and canonical Architecture synchronization before reliance on that change. No ADR is created or accepted here, and no current Architecture boundary is changed.

## Completed Evaluation and Current Disposition — 0.3.0

The 0.2.0 bounded documentation/static evaluation was executed exactly once. Its [durable evidence artifact](evidence/DEC-0010/bounded-password-blocklist-strategy-evaluation.md), at `specifications/decisions/evidence/DEC-0010/bounded-password-blocklist-strategy-evaluation.md`, is merged in canonical baseline `770442323ec8fe3e1da680efd59326f771086d6f` and records two investigated arrangements:

- HIBP Pwned Passwords remote SHA-1 range checking;
- HIBP Pwned Passwords externally maintained corpus consumed locally.

The terminal outcome is **C — STOPPED / INCONCLUSIVE**. It does not establish technical infeasibility or a completed adverse conformance assessment. Neither arrangement is selected, accepted, universally rejected or demonstrated compliant. Material unresolved evidence includes representation/NFC compatibility, freshness/currentness, integrity, usage/licensing applicability, resource/operational bounds and repository-specific failure/recovery/conformance matters. The limitations remain bounded to the inspected evidence and investigated arrangements, not all possible strategies.

The DEC-0010 0.2.0 single-use authorization is **CONSUMED / EXHAUSTED**. Unused query, source, candidate or time capacity is not reusable authority. No retry, continuation, second evaluation, candidate substitution or material expansion is authorized.

DEC-0010 is **DEFERRED PENDING MATERIALLY NEW EVIDENCE**, while remaining Proposed, Identity-owned, a Security Decision and non-authoritative. The post-Outcome-C governance audit identified no justified materially different bounded investigation objective from current repository evidence. Materially new evidence may justify future governance consideration, but does not itself authorize research, evaluation or strategy selection. Any future evaluation requires fresh explicit governance authorization.

This synchronization selects or admits no strategy, provider, dataset, library, service or dependency. DEC-0007 remains unchanged and authoritative. DEC-0001 dependency admission remains separate where applicable, as do Architecture/ADR, provider/integration, conditional privacy/legal/licensing and operational governance. Production implementation remains unauthorized at the unresolved blocklist-policy acceptance boundary: structural preparation cannot become complete policy acceptance without successful governed checking, and credential creation/replacement cannot bypass that requirement. DEC-0006 hashing authority is not blocklist approval. DEC-0009 remains untouched and unrelated.

## Historical Bounded Evidence-Gathering Evaluation Authorization — 0.2.0 (Consumed / Exhausted)

The following scope, permissions, limits and outcomes preserve the original 0.2.0 authorization as historical context only. They are not current permissions and cannot be reused. The 0.3.0 disposition above records the subsequent outcome without attributing foreknowledge to the original authorization.

Version 0.1.0 authorized no investigation or execution. Version 0.2.0 authorized exactly **one single-use, bounded documentation/static evidence evaluation**, owned by Identity. That limited authorization did not make the Proposed strategy decision authoritative or Accepted. The 0.2.0 authorization revision itself performed no evaluation.

### Exact Evaluation Question

Can a strategy within the explicitly authorized evaluation scope satisfy DEC-0007's requirement to evaluate the complete NFC-normalized prospective Customer password against known commonly used, expected, or compromised password values while preserving governed whole-password matching, freshness, privacy, failure, fail-closed, safe-retry, supportability and operational requirements?

### Finite Scope and Investigation Limits

The evaluation may examine the candidate classes listed above: repository/local data, externally maintained data consumed locally, supported components and remote capabilities. Another materially supportable class may be considered only if encountered within the same limits. No class or concrete candidate is preferred or selected.

The entire evaluation is limited to **one pass, at most 12 discovery search queries, 30 distinct public source documents/pages, four candidate arrangements, and eight hours of active investigation**. These are investigation caps only, not production resource or operational policy. Follow-up queries and source reads count toward the same totals; no per-class reset is permitted. Re-reading a source does not create an additional document allowance or extend the time budget. The evaluator must record cumulative usage.

A candidate may enter the maximum four-candidate set only where available public/static evidence identifies a relevant whole-password checking/data capability, attributable source or maintainer, and enough coverage, representation or operating information to investigate DEC-0007 compatibility. Discovery results alone do not establish compliance. Count every arrangement subjected to substantive candidate assessment, including rejected or discontinued candidates; dropping one does not free a slot. Prefer evidence breadth across materially distinct classes where eligible evidence permits, without assuming equal suitability or filling slots merely to continue searching. Explain inclusion/exclusion and stop when the question is answered or a limit is reached. No continuing search to find a preferred result is authorized.

### Permitted Evidence and Activities

Only read-only inspection of authoritative public documentation, browser-readable source text, release/version information and existing repository material is permitted. The evaluator may compare provenance, maintenance/support, licensing/terms, documented coverage and limitations, integrity/authenticity mechanisms, privacy/data flows, trust boundaries, updates/freshness, determinism/reproducibility, documented resource bounds, availability/failure, safe retry/recovery, published testing/conformance evidence, operational lifecycle, Architecture/deployment and supply-chain implications. Viewing documentation is permitted; acquiring dataset archives, packages, binaries or executable source checkouts is not.

Apply every obligation in Future Evaluation Requirements and record a policy-obligation matrix. Distinguish documented claims from independently demonstrated behavior, unknowns and assumptions. Documentation/static inspection must not be reported as runtime conformance or benchmark evidence. If executable evidence is needed for a material conclusion, record that limitation and stop under the applicable terminal outcome rather than silently expand permission.

The evaluation must preserve complete-password comparison, the insufficiency of substring presence, the existing complete NFC representation and significant case/whitespace, appropriate commonly used/expected/compromised coverage, and fail-closed treatment of unavailable, failed, stale or uncertain checking. No credential creation/replacement may proceed without successful required evaluation. Safe retry, password secrecy and redacted diagnostics remain mandatory. Establishment/change policy must not become Login-time policy, and no candidate may justify weakening DEC-0007.

### Prohibited Activities

No downloads of candidate artifacts/data, installations, executable experiments, spikes, benchmarks, candidate-specific runtime tests, provider API calls, provider provisioning or service/account creation are authorized. Do not use real Customer passwords or secrets or transmit prospective passwords or derived lookup material to candidates. Do not modify production code or tests, implement a checker, create production persistence/schema, change Contracts or DEC-0007 policy, weaken Security requirements, admit dependencies, change Architecture, create deployment/infrastructure, reopen or modify DEC-0009, resolve unrelated Product decisions, or automatically accept DEC-0010. Technical promise is not implementation or Architecture authority.

The only repository output permitted by this future evaluation is the single evidence artifact specified below. Governance synchronization or another evaluation requires separately authorized work.

### Exactly Three Terminal Outcomes

- **A. SUPPORTED BASIS:** Evidence is sufficient to prepare a concrete DEC-0010 strategy proposal for governance review. This does not select or accept a strategy or grant dependency, Architecture, provider or implementation authority. Material unknowns must not be concealed to claim this outcome.
- **B. BASIS NOT DEMONSTRATED:** One or more material obligations cannot be demonstrated within the authorized evidence and scope after bounded assessment. Conclusions are limited to the candidates/classes actually evaluated and must not become universal infeasibility claims.
- **C. STOPPED / INCONCLUSIVE:** Responsible completion is prevented by authorization/evidence limits, safety, incompatible terms, prerequisite governance or exhausted resource/search scope. This is not technical infeasibility. Use C when a stop condition prevents completing the assessment; use B for a completed bounded assessment that does not establish the required basis.

Record exactly one terminal outcome for the evaluation, with candidate-specific observations beneath it. An outcome is not permission to begin another investigation.

### Stop Conditions

Stop investigation immediately if continuing would exceed any candidate, search, document, active-time or scope limit; require real Customer secrets; require unapproved download, execution, dependency or provider activity; require policy weakening; encounter incompatible licensing/terms preventing responsible evaluation; require material Architecture/provider/privacy/legal/security prerequisite approval outside this authorization; require evidence unavailable within the bounded evaluation; or require a new material governance decision. Record evidence gathered so far and the applicable terminal outcome without performing the blocked activity. Safe closure and recording of the outcome do not authorize additional investigation.

### Single Durable Evidence Artifact and Traceability

The future evaluation must produce exactly one durable artifact:

`specifications/decisions/evidence/DEC-0010/bounded-password-blocklist-strategy-evaluation.md`

This path is relative to `enterprise-fashion-ecommerce/`. Do not create it as part of this authorization revision. The evaluation artifact must record DEC-0010 authorization version 0.2.0 and its commit/reference, repository baseline evaluated, evaluation date, evaluator and accountable Identity owner, exact scope and budget usage, candidates/classes actually investigated, sources and versions/access dates, permitted activities actually performed, evidence and limitations, the policy-obligation matrix, downstream gates identified, stop conditions encountered (or none), exactly one terminal outcome A/B/C, and the authorization-consumption statement. Link conclusions to sources and distinguish supplied claims from observed evidence. Do not fabricate evaluator identities, approvals or runtime results.

### Single-Use Consumption and Separate Gates

The authorization becomes **consumed/exhausted when its terminal outcome is durably recorded**. A stopped or inconclusive outcome also consumes it. Investigation stops on reaching a terminal outcome; delayed recording does not permit continuation. No automatic retry, candidate substitution after exhaustion, material expansion, second evaluation or continued investigation after an outcome is permitted. Each requires fresh explicit governance authorization.

Strategy acceptance remains separate. DEC-0001 dependency admission remains separate for proposed executable third-party dependencies. Material Architecture changes require applicable ADR governance and canonical synchronization. Provider/external-service adoption requires applicable provider/integration approval; privacy/legal/licensing review remains conditional on actual characteristics. Operational governance must establish selected freshness/update/failure/recovery/support conditions. Implementation remains separately governed and unauthorized. A supported basis automatically satisfies none of these gates.

Identity owns both decision and evaluation. Security and Architecture review are required; Engineering and Operations review evaluation and supportability implications. Product/Customer review is required only if observable Product/Registration semantics would materially change, and privacy/legal/licensing review is conditional on actual candidate characteristics. No completed human approval is asserted by this authorization.

## Required Review and Approval Responsibilities

- **Identity:** single accountable owner; policy-enforcement boundaries, evidence interpretation and separation from credential currentness, Authentication and Customer truth.
- **Security:** coverage and control adequacy, whole-password semantics, secrets, privacy, integrity, fail-closed behavior, compatibility and residual risk.
- **Architecture:** existing boundaries and candidate-specific trust, integration, runtime and deployment implications; determination of separate ADR requirements.
- **Engineering and operational owners:** Java 21 feasibility where relevant, bounded operation, reproducibility, testability, updates, recovery, maintainability and support responsibilities.
- **Product and Customer:** required involvement where observable Registration rejection, retry or availability semantics would materially change. Such changes require their applicable governance and cannot silently amend DEC-0007 or Contracts.
- **Privacy, legal and licensing authorities:** review where candidate characteristics introduce applicable disclosure, processing, usage, licensing or contractual obligations; not a claim that every strategy requires identical review.

Approval evidence must durably represent the applicable authorities under DECISIONS.md. This proposal asserts no completed approval, reviewer identity, meeting, candidate validation or implementation evidence.

## Consequences

### Benefits and Costs

A governed selection process makes the first unresolved policy dependency after PR #128 explicit and can enable a later Identity-owned checker and complete prospective-password policy acceptance. It prevents framework defaults, arbitrary lists or provider availability from becoming policy authority. It adds evidence, review, maintenance and operational responsibilities and leaves complete acceptance blocked until a suitable strategy and required gates are resolved.

### Operational and Support Impact

Future adoption requires accountable ownership of source support, updates, freshness, failure handling, recovery and secret-safe diagnosis. Fail-closed checking can prevent credential establishment during uncertainty or unavailability; this is an existing DEC-0007 consequence, not a newly selected availability policy. No runtime, provider, numerical operating target or deployment behavior is established by this proposal.

### Compatibility and Migration Impact

Future evidence must address source/version changes, normalization and comparison compatibility, rollout, rollback and applicable migration. A strategy change must not silently reinterpret accepted password representation, weaken establishment policy or impose new Login-time acceptance rules. Credential migration and compromise response remain separately governed under DEC-0006 and Security authority.

### Reversibility and Residual Risks

No technology commitment is made now. Future exit costs may include data replacement, adapter changes, operational transition and provider dependence and must be evaluated before selection. Coverage gaps, stale or poisoned data, source compromise, disclosure, false confidence and denial of establishment remain risks to assess. Blocklist checking cannot guarantee password secrecy or prevent every form of credential abuse.

## Explicit Non-Decisions

The following remain unresolved: exact blocklist source, provider, dataset, dataset release/version, library/SDK, API, local versus remote mechanism, refresh/update cadence, cache strategy, TTL, retry counts, timeouts, SLOs, storage format, database schema, network topology, dependency selection/admission, provider credentials, deployment mechanism, observability product and exact user-facing copy beyond existing governed semantics.

No Product policy, public or internal Contract shape, Architecture change, persistence model, code, evaluation result or implementation authorization is established. DEC-0009 remains untouched and deferred; this proposal supplies no login-email canonicalization fallback.

## Proposal and Future Selection Readiness

Proposal readiness requires accurate Proposed/non-authoritative metadata, same-change index registration, traceability to accepted policy and current implementation, explicit evidence gaps, no implied selection or renewed evaluation authority, and preservation of all separate gates.

Future selection readiness requires attributable evidence against every applicable evaluation obligation, explicit unresolved limitations and residual risks, represented approval authorities, and a governed revision recording the actual proposed strategy and consequences. Approval of this initial boundary does not by itself meet those conditions or authorize production implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [ENGINEERING-PRINCIPLES.md](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [JAVA.md](../../.ai/backend/JAVA.md), [SPRING.md](../../.ai/backend/SPRING.md), [API.md](../../.ai/backend/API.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0006 — Customer Password Hashing and Verification Strategy](DEC-0006-customer-password-hashing-verification-strategy.md)
- [DEC-0007 — Initial Customer Password Policy](DEC-0007-initial-customer-password-policy.md)
- [DEC-0008 — Initial Customer Email Verification Policy](DEC-0008-initial-customer-email-verification-policy.md)
- [DEC-0009 — Customer Login IDNA2008 Implementation Strategy](DEC-0009-customer-login-idna2008-implementation-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [ADR-0022 — Initial Identity–Customer/Account Association Strategy](../adr/ADR-0022-identity-customer-account-association-strategy.md)
- [Identity Domain](../domains/identity/identity-domain.md) and [Identity Backend](../backend/identity/identity-backend.md)
- [Customer Domain](../domains/customer/customer-domain.md) and [Customer Backend](../backend/customer/customer-backend.md)
- [Initial Customer Registration Contract](../contracts/customer/initial-customer-registration-contract.md) and [OpenAPI](../contracts/customer/initial-customer-registration.openapi.yaml)
- [PreparedProspectiveCustomerPassword](../../backend/src/main/java/com/enterprise/fashion/ecommerce/identity/domain/model/PreparedProspectiveCustomerPassword.java)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.3.0 | 2026-10-07 | Proposed | Synchronized the completed single 0.2.0 evaluation and durable Outcome C — STOPPED / INCONCLUSIVE; recorded CONSUMED / EXHAUSTED authorization and deferral pending materially new evidence without technical-infeasibility claims, selection, acceptance, renewed evaluation authority or implementation permission; preserved separate governance gates. |
| 0.2.0 | 2026-10-07 | Proposed | Authorized exactly one bounded, single-use documentation/static evidence evaluation with finite investigation limits, A/B/C terminal outcomes, stop conditions and one future evidence artifact; preserved separate gates without executing evaluation, selecting a strategy, accepting the decision or authorizing implementation. |
| 0.1.0 | 2026-10-07 | Proposed | Established the Customer password blocklist decision question, future evidence requirements and authority boundaries after PR #128; selected no strategy, authorized no evaluation or implementation, preserved accepted policy and separate dependency/Architecture gates, and left DEC-0009 deferred. |
