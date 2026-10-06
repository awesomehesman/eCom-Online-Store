# DEC-0010 — Customer Password Blocklist Strategy

- **Identifier:** DEC-0010
- **Title:** Customer Password Blocklist Strategy
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-07
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted, authoritative DEC-0007 already requires every prospective Customer password establishment or change to compare the complete NFC-normalized password against a governed blocklist of known commonly used, expected, or compromised passwords. Matching whole-password values must be rejected; mere substring presence does not establish a match. Unavailable, failed, stale beyond governed operating conditions, or uncertain evaluation fails closed. Credential creation or replacement cannot proceed without successful required policy evaluation, and outcomes must remain distinguishable and safely retryable without unsafe disclosure.

At canonical baseline `a4e59eb451b866c270f18cedf6d3a94c96d5936c`, PR #128 supplies `PreparedProspectiveCustomerPassword`. It rejects malformed UTF-16, normalizes the complete prospective password to NFC, and enforces the post-normalization 15–64 Unicode-code-point structural bounds. It explicitly awaits remaining establishment policy checks and does not establish complete password-policy acceptance. The implementation preserves whitespace and case and does not truncate the password.

DEC-0006 governs Argon2id credential hashing and verification mechanics. Hashing or successful verifier matching is not blocklist approval. No governed blocklist source, provider, dataset, API, library, service, or update mechanism is currently selected, and no production blocklist checker exists at this baseline. Identity owns policy enforcement; Customer retains Registration coordination and Customer/Account business truth under ADR-0022 and the Initial Customer Registration Contract.

These are accepted upstream requirements and inspected repository facts, not evidence that any candidate strategy is compliant. Candidate suitability, coverage, freshness, supportability, privacy, and operational feasibility remain open evidence questions. No candidate-specific assumptions or technical findings are asserted.

DEC-0009 remains 0.8.0 Proposed, non-authoritative and deferred pending materially new evidence and fresh explicit authorization. This separate password decision neither reopens its exhausted evaluations nor bypasses strict IDNA2008 validation, canonical comparison-key construction, or authoritative login-email lookup.

## Decision Question

What Identity-owned blocklist strategy and governed operating conditions will reliably evaluate the complete NFC-normalized prospective Customer password against known commonly used, expected, or compromised values, while preserving DEC-0007's rejection, fail-closed, privacy, and safe-retry requirements?

## Decision — Proposed Governance and Evidence Boundary

This initial proposal defines the decision problem, evidence requirements, authority boundaries, and later review gates. **No blocklist strategy, source, provider, dataset, service, library, dependency, or implementation mechanism is selected.** The record is Proposed and non-authoritative; accepted upstream requirements remain authoritative independently of this proposal.

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

No candidate has been evaluated or selected. These classes identify possible future comparisons, not equal suitability or authorization to investigate particular products:

- **Repository-controlled/local blocklist data:** assess coverage, provenance, distribution integrity, resource bounds, updates and repository maintenance responsibility; locality alone does not prove adequacy or freshness.
- **Supported third-party dataset:** assess coverage, licensing, provenance, versioning, update continuity and integrity; third-party publication alone does not establish trust.
- **Supported library/component carrying data or checking capability:** assess both checking semantics and data suitability, Java compatibility, maintenance and executable supply-chain consequences; dependency availability does not establish policy compliance.
- **Remote service/provider:** assess coverage, availability, observable behavior, secret/derived-data disclosure, trust boundaries, privacy, contractual conditions and exit costs before any selection; remote checking is not presumed permissible.
- **Another demonstrably supportable strategy:** require the same evidence and explicit justification of any different risks or boundaries, without treating this category as open-ended evaluation authority.
- **Continue deferral without selection:** preserves the current absence of an approved mechanism but leaves complete password-policy acceptance and dependent credential establishment blocked. Skipping evaluation or accepting uncertain results is prohibited by DEC-0007, not an available fallback.

## Security and Data Impact

The proposed process preserves DEC-0007 rather than revising its policy. A future mechanism must not log or persist plaintext prospective passwords, expose them in diagnostics, silently skip required evaluation, weaken whole-password matching to substring matching, treat failure/unavailability/staleness/uncertainty as acceptance, or treat structural preparation as complete acceptance. The accepted NFC and 15–64 bounds remain unchanged. Submitted and normalized secrets and sensitive lookup material must remain excluded from unsafe logs, metrics, traces, events, errors and support evidence under existing Security authority.

Any mechanism that could transmit password-derived or password-equivalent information outside the application/process boundary requires explicit trust-boundary, privacy and Security evaluation before selection. Transformation alone is not evidence that disclosure is safe. No such disclosure, retention, network access, or provider credential is authorized here.

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

## Evaluation Authorization

Version 0.1.0 authorizes no technology search, candidate-specific evaluation, download, experiment, execution, spike, evidence artifact or implementation. It defines requirements for later review only and consumes no future evaluation authority.

Before candidate investigation or experimental evaluation proceeds, obtain explicit bounded governance authorization recording purpose, scope, candidate limits, permitted activities, isolation, evidence requirements, stop conditions and completion criteria. Such authorization must preserve separate selection, acceptance, admission and Architecture gates; it must not be inferred from the candidate classes in this proposal.

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

Proposal readiness requires accurate Proposed/non-authoritative metadata, same-change index registration, traceability to accepted policy and current implementation, explicit evidence gaps, no implied selection or evaluation authority, and preservation of all separate gates.

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
| 0.1.0 | 2026-10-07 | Proposed | Established the Customer password blocklist decision question, future evidence requirements and authority boundaries after PR #128; selected no strategy, authorized no evaluation or implementation, preserved accepted policy and separate dependency/Architecture gates, and left DEC-0009 deferred. |
