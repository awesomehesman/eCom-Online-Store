# ADR-0023 — Staff Authentication Authority and Mechanism

- **Identifier:** ADR-0023
- **Title:** Staff Authentication Authority and Mechanism
- **Type:** Architecture Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-11-10
- **Last Updated:** 2026-11-10
- **Owner:** Architecture
- **Authoritative:** true
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Identity now implements persistent, provider-neutral Staff associations with stable
Identity-local Staff principal references, active-only lookup, explicit revocation
and transactional lifecycle provenance. Dormant authentication-to-association
resolution also exists. Its `VerifiedStaffAuthentication` capability has a private
constructor, disabled JSON construction and no production issuer. A raw
reference, persisted association or successful lookup does not prove Authentication.

No trusted Staff authentication evidence issuer or operational Staff login exists.
The implemented Customer password-verification boundary and Accepted Customer
Authentication decisions do not authorize Staff credential reuse or establish Staff
identity. Implementing an actual Staff verifier therefore requires an explicit
choice of upstream Authentication authority, mechanism and trust boundary.

[ADR-0019 — Authentication Session and Token Strategy](ADR-0019-authentication-session-token-strategy.md)
already establishes authoritative server-side Sessions for first-party browser
Authentication, including privileged human identities. It preserves compatibility
with separately governed OIDC integration without selecting a provider.
[ADR-0020 — Identity Session Store Strategy](ADR-0020-identity-session-store-strategy.md)
selects Spring Session JDBC and application PostgreSQL for that local authority.
[ADR-0021 — Customer Authentication Authority Strategy](ADR-0021-customer-authentication-authority-strategy.md)
selects local Customer Authentication authority only. This decision preserves all
three Accepted decisions and does not extend Customer credential approval to Staff.

## Decision Drivers

- Establish a concrete Staff mechanism before implementing its trusted verifier.
- Preserve Customer/Staff separation and Identity ownership of local security truth.
- Reuse accepted local Session infrastructure and existing Staff associations.
- Support privileged assurance, current access, replay resistance and effective revocation.
- Avoid manufacturing trusted identity from labels, email, headers or bearer claims.
- Keep Authentication separate from assignments and owning-Domain Authorization.
- Preserve dependency admission and independently reviewable implementation gates.

## Decision

**ACCEPTED — 2026-11-10:** enterprise-managed OpenID Connect (OIDC) is the
upstream Staff authentication mechanism. The external enterprise Identity Provider
authenticates the Staff identity; Identity verifies the resulting evidence,
resolves the existing local Staff association and establishes local Session context
only after all applicable gates succeed. Identity retains local Principal,
association, Session and security-evidence authority.

No enterprise Identity Provider, tenant, issuer or account is selected. No
operational authority follows from this Accepted ADR. All mechanism-specific
controls below are binding requirements of the selected mechanism, unless
explicitly identified as inherited requirements.

```text
External enterprise Identity Provider
    | upstream Authentication
    v
Identity-owned OIDC verification and controlled evidence issuance
    | verified Staff authentication evidence
    v
Existing Staff association resolver
    | active Identity-local Staff principal reference
    v
Existing authoritative server-side Session architecture
    | current actor context only
    v
Separate owning-Domain contextual Authorization
```

These are distinct trust steps, not independently sufficient proofs. External
Authentication does not provision Staff; verification does not create an association;
association resolution does not issue a Session; a Session does not grant an
administrative action. The diagram describes the selected future integration, not
capabilities activated by this document.

## Selected Authentication Flow

1. Identity initiates a backend authorization-code flow against an explicitly
   approved issuer/client configuration. Use PKCE with S256, unpredictable state
   and nonce, and a narrowly scoped, expiring server-side transaction bound to the
   initiating browser. No client-supplied issuer or arbitrary return URL may choose
   the trust source or redirect destination.
2. The backend receives the response at a separately governed callback. Validate
   state, browser/transaction binding, expected response and expiry before positive
   reliance. Single-use processing must reject duplicate, concurrent, expired and
   superseded callbacks. Failed or uncertain exchange does not make replay valid.
   Exact transaction representation, lifetime and concurrency mechanism remain
   implementation/security gates; none is selected here.
3. Exchange the authorization code server-side using the transaction's PKCE verifier
   and approved client authentication. Tokens, client credentials, PKCE material
   and equivalent Secrets remain server-side and excluded from logs and browser
   persistent storage. This selects no client credential technology or secret store.
4. Verify ID-token signature using controlled algorithms and keys bound to the
   configured issuer. Validate exact issuer, expected audience, applicable authorized
   party, expiration, applicable not-before/issued-at constraints and nonce. Reject
   malformed, unsupported, ambiguous or untrusted evidence. An unverified token's
   issuer, key URL or algorithm must not expand trust or trigger arbitrary retrieval.
5. Validate required Authentication time and assurance against approved policy.
   Token issuance time is not proof of recent Authentication. Generic MFA or method
   labels do not prove a particular phishing-resistant factor without a reviewed
   provider-specific evidence mapping. Missing or insufficient evidence is not success.
6. Bind the verified stable issuer/subject pair through an explicitly reviewed
   mapping to the existing `StaffIdentityReference`. Preserve exact case-sensitive
   identity meaning; do not link by email, display name, Customer identity, requested
   Role or caller-provided authority namespace. A reference's syntax proves no trust.
7. Only the reviewed verifier/issuer may construct verified Staff evidence. Do not
   expose a public factory that blesses identity strings, a caller's success boolean,
   or a deserialized `Resolution`. Production must not use reflection to mint evidence.
   Any necessary evidence freshness/revalidation additions need review before issuance.
8. Resolve through existing active-only Staff association lookup. Unknown, revoked,
   mismatched, unavailable or uncertain association evidence cannot establish a Staff
   Session. Do not automatically create or reactivate associations during Authentication.
9. After current evidence and applicable security requirements are satisfied, Identity
   may establish Staff context through the accepted server-side Session architecture,
   including fixation resistance and governed expiry/revocation. Do not silently
   upgrade Customer context into Staff context. Cookie/context isolation details,
   externally observable Contracts and activation remain separate gates.

An Access Token is not a substitute for an ID token's Authentication purpose.
This decision does not introduce a browser bearer-token architecture, downstream
API access, Refresh Token reliance or a silent-renewal policy.

## Security Requirements and Outstanding Controls

OIDC support alone does not demonstrate compliance. Provider documentation,
entitlements, configuration and executed integration evidence must substantiate
material capabilities before reliance.

| Concern | ESTABLISHED REQUIREMENT | ACCEPTED MECHANISM CONTROL | REMAINING PROVIDER / IMPLEMENTATION GATE |
| --- | --- | --- | --- |
| Trusted evidence and actor separation | BIDN-REQ-011–014 require trusted current evidence, safe validation and distinct actors; Authentication alone is not Authorization. | Backend OIDC verification and controlled Staff-only evidence issuance; no automatic association or Customer conversion. | Provider selection and Identity/Security approval must confirm permitted issuer/client trust and subject mapping within the accepted Architecture boundary. |
| MFA and assurance | Security Standards §§11–13 require privileged Staff MFA and applicable stronger controls; no new exemption is implied. | Enforce reviewed assurance/method evidence and use appropriate step-up where the provider can prove it. | Security defines applicable factors/assurance and action-specific requirements; Identity confirms verifiable provider evidence. No factor or assurance code is approved here. |
| Freshness and reauthentication | Trusted evidence must be sufficiently fresh; privileged Sessions require risk-appropriate shorter limits and high-impact reauthentication. | Validate trustworthy authentication-time evidence and explicit freshness requirements; never infer freshness from token receipt or issuance alone. | Security/Identity settle lifetimes, tolerances, reauthentication semantics and treatment of absent/unsupported evidence. No numerical default is selected. |
| Replay and correlation | BIDN-REQ-013 and 022 require replay/concurrency safety and latest accepted Identity truth. | PKCE, state/nonce correlation and single-use backend callback processing, bound to initiating context. | Identity/Security confirm transaction lifetime, supersession and uncertain-outcome behavior; review the implementation mechanism separately. |
| Provider disablement and revocation | Effective withdrawal and revocation must not be silently reversed by stale evidence. | Treat provider account disablement/upstream withdrawal, local association revocation and local Session revocation as distinct coordinated controls. | Confirm supported authoritative signals, freshness/propagation limits and existing-Session reliance policy. OIDC logout or ID-token expiry alone cannot prove local Session termination. |
| Local revocation | ADR-0019/0020 retain local authoritative Session state; association lookup is not a durable access grant. | Recheck required current evidence at governed reliance points and prevent revoked associations from establishing new Staff context; terminate affected local Sessions through governed Identity behavior. | Identity/Security confirm association-to-Session linkage, in-flight withdrawal races, local revocation scope and reconciliation. No cross-system atomicity is claimed. |
| Recovery | Recovery must preserve trusted identity, current access and effective withdrawal without an insecure fallback. | Use separately reviewed enterprise recovery and local incident/revocation coordination; no Customer-password or support override fallback. | Identity/Security confirm recovery assurance, compromise handling, subject continuity and accountable operational responsibility. |
| Audit and attribution | Security Standards §27 requires applicable attributable, protected security/audit evidence; Secrets must not enter logs. | Correlate Authentication, denial, association, Session and revocation outcomes using safe identifiers and reason categories. | Confirm required audit applicability, integrity, retention and audit-unavailable/uncertain-establishment behavior before operational use. The association journal is not a complete operational audit system. |
| Outage and uncertainty | Failure must not become success, fabricated absence or insecure fallback. | Fail closed for new Authentication when required verification/association evidence is unavailable. Never reuse stale success to bypass required assurance. | Define existing-Session behavior during provider outage and signal loss, including maximum reliance and recovery, without inventing indefinite validity or requiring unapproved topology. |
| Key rotation | Verification must use trusted compatible evidence and protect credentials/keys. | Accept only issuer-bound approved keys/algorithms; reject unknown keys safely. Bound any configured key refresh and preserve trust during rotation. | Confirm approved discovery/key sources, overlap, compromise/retirement, refresh limits and outage behavior; no library, key or cache duration is selected. |

Inherited sources are [Identity Backend](../backend/identity/identity-backend.md)
BIDN-REQ-011–014 and 020–026 and [Security Standards](../../.ai/core/SECURITY-STANDARDS.md)
§§11–13 and 27. Their mandatory controls remain effective independently of this
ADR's status. Accepted mechanism controls do not settle the later provider-specific
and implementation choices listed above.

## Provider Selection — Separate Gate

**No enterprise Identity Provider is selected.** Enterprise-managed describes the
accepted operating/trust model, not an existing enterprise directory or provider
subscription. Provider acceptance must establish:

- Enterprise tenant/directory ownership, permitted issuers and configured clients.
- Stable subject semantics and the exact issuer/subject-to-local-reference mapping.
- Enforceable MFA, supported phishing-resistant Authentication and trusted assurance claims.
- Authentication-time semantics, reauthentication/step-up support and freshness evidence.
- Account recovery, disablement and compromise handling.
- Provider logout, revocation and withdrawal signals and their operational limitations.
- Signing-key rotation, trusted verification availability and safe outage behavior.
- Security-event/audit availability, operational support and incident responsibility.
- Required provider entitlements, environments and demonstrated capability access.

No prior provider evaluation or engineering PoC constitutes production selection.
A capability matrix must distinguish documented, configured, tested, unsupported
and unknown behavior. Unknown mandatory capability blocks the affected reliance;
it is not resolved by a generic conditional acceptance statement.

## Compatibility with Accepted Architecture

ADR-0019's server-side cookie custody, CSRF obligations, fixation resistance and
privileged controls remain unchanged. Upstream tokens do not become browser-owned
application credentials. ADR-0020's Spring Session JDBC/PostgreSQL authority remains
unchanged; provider logout cannot replace local authoritative revocation.

ADR-0021 continues to govern Customer Authentication only. Staff credentials,
issuer trust, evidence and context must remain separate; this decision does not
change Customer behavior or reuse Customer password facts as Staff proof.

Existing association persistence and the dormant resolver are reused without a
second Staff identity model, automatic provisioning or schema redesign. Current
lookup is an observation, not atomic coordination with provider withdrawal or an
authorization grant. Required future integration must address that limitation.

[DEC-0012 — Administrative Role and Permission Matrix](../decisions/DEC-0012-administrative-role-permission-matrix.md)
remains Proposed and non-authoritative. Its Product-specific assignment/assurance
confirmations are separate from Staff Authentication mechanism acceptance.
Administration coordinates governed workflows; Identity owns security evidence;
Product and other owning Domains retain contextual Authorization. Product
administration remains default-deny and Product publication remains separately gated.

## Dependency Governance

[DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md)
remains binding. **No authentication library, OIDC client dependency, Nimbus
artifact, provider SDK or signing key is admitted by this ADR.**

Required dependencies must follow the existing fixed-version alignment, locking,
reviewed checksum/signature verification and executable compatibility process.
Strict Gradle dependency verification must remain enabled. Unresolved signing-key
admission cannot be bypassed, inferred from mechanism acceptance or solved by
changing the protected keyring without separately authorized governance. Provider
trust signing keys and artifact-release signing keys are separate trust decisions.

## Alternatives Considered

| Alternative | Benefits | Costs and boundary | Decision treatment |
| --- | --- | --- | --- |
| Enterprise-managed OIDC | Uses a governed enterprise Authentication capability while preserving Identity verification and local Sessions; can support centralized MFA and account lifecycle. | Adds provider/tenant selection, issuer trust, assurance mapping, outage and revocation coordination; support and entitlement must be proven. | Selected as the Staff authentication mechanism, subject to all remaining gates. |
| Identity-owned local Staff credentials | Keeps primary verification locally controlled and avoids an external Authentication dependency. | Requires separately approved Staff credentials, MFA, recovery, provisioning, lifecycle and operational security; Customer implementation is not Staff authority. | Not selected initially; reconsider only through later Architecture governance if enterprise capability evidence is inadequate. |
| Reuse Customer Authentication | Existing Customer verification infrastructure may appear convenient. | Cannot establish distinct Staff identity/authority merely by reusing credentials or labels; would require explicit different governance and isolation evidence. | Not an eligible shortcut under the preserved separation boundary. |
| Continue default-deny without Staff login | Preserves current security posture without new provider or operational trust. | Provides no operational Staff Authentication progress. | Required current behavior until implementation and activation gates close. |

These alternatives and trade-offs do not constitute provider findings.

## Consequences, Migration and Reversibility

Mechanism acceptance permits a concrete verifier design subject to provider,
dependency and implementation readiness. It does not establish a working login,
trusted issuer, administrative matrix or publication capability. External outages
and withdrawal coordination become explicit security/operational dependencies.

This decision migrates no credentials, associations, Sessions or data. A later provider
change must preserve stable local references, avoid email-based relinking, and
review association migration, evidence invalidation, affected Session revocation
and recovery. Automatic subject replacement or reactivation is not authorized.
No new migration, infrastructure, retention period or rollout procedure is selected.

## Accountable-Owner Dispositions

| Capacity | Required determination | Current disposition |
| --- | --- | --- |
| Architecture | External Staff Authentication authority/mechanism boundary and compatibility with ADR-0019/0020/0021. | ACCEPTED — project owner acting in the accountable Architecture capacity, 2026-11-10. Enterprise-managed OIDC is selected; provider and implementation gates remain separate. |
| Identity | Verification, stable Staff subject binding, controlled evidence issuance, Session lifecycle, recovery and revocation responsibilities. | ACCEPTED — project owner acting in the accountable Identity capacity, 2026-11-10. Identity retains the local responsibilities defined by this decision. |
| Security | Issuer trust, assurance/MFA, freshness/replay, withdrawal/revocation, failure and audit requirements. | ACCEPTED — project owner acting in the accountable Security capacity, 2026-11-10. All stated Security controls remain binding; provider-specific evidence and mappings remain gated. |
| Administration | Provisioning and association lifecycle coordination, separated from operational assignments and permissions. | ACCEPTED — project owner acting in the accountable Administration capacity, 2026-11-10. Authentication grants no assignment, Permission or administrative authority. |

The project owner explicitly supplied these dispositions while acting across the
four stated accountable capacities. No personal name, signature, independent reviewer
or third-party approval is asserted. Existing Product business approvals remain
separate and do not expand the scope of this Staff Authentication decision.

## Acceptance Conditions and Separate Readiness Gates

| Gate | Acceptance criteria | What it does not establish |
| --- | --- | --- |
| ADR mechanism acceptance | Record required accountable dispositions; resolve the architectural choice and owner boundaries; explicitly retain unresolved provider/implementation choices; complete applicable governance synchronization. | No selected issuer, admitted dependency or operational login. |
| Provider selection | Identify the actual provider, enterprise tenant, issuer/client scope and operational owner through accountable review. | Product capability claims or generic OIDC support are not evidence of mandatory assurance. |
| Provider capability approval | Demonstrate every mandatory capability, evidence mapping, entitlement, withdrawal and outage boundary; reject unsupported or unknown mandatory reliance. | No claim of tested integration from documentation alone. |
| Dependency admission | Admit each required artifact/version and verification material under DEC-0001 with strict verification and compatibility evidence. | No automatic admission from ADR/provider acceptance. |
| Implementation readiness | Review Contracts, correlation, verifier/issuer design, association/current-access boundaries, audit, recovery and local Session integration with explicit authorization for the bounded implementation. | No implied deployment or operational activation. |
| Operational activation | Execute security/integration verification, including negative, replay, concurrency, assurance, disablement, provider/local revocation, outage and recovery cases; complete approved configuration, audit and operational readiness review. | No administrative assignments, Product authorization or publication authority. |

Mechanism acceptance retains genuine later gates, but does not describe unresolved
issuer/assurance/withdrawal choices as already approved. Those choices block the
implementation or operational reliance that depends on them. No acceptance or
readiness criterion other than ADR mechanism acceptance is recorded as completed.

## Implementation Sequence

1. ADR mechanism acceptance and required canonical synchronization were completed on 2026-11-10.
2. Select the actual provider and confirm mandatory capabilities/entitlements.
3. Complete dependency admission where required, preserving strict verification.
4. Implement a trusted OIDC verifier and controlled Staff evidence issuer.
5. Execute Staff Authentication integration tests, including association denial and all material failure/security paths.
6. Separately implement and verify Staff Session establishment, termination and revocation before activation.
7. Implement administrative Authorization only after its independent assignment and owning-Domain gates.
8. Enable Product administration only after its own Contracts, authorization, audit and publication/readiness gates; Staff Authentication success alone grants nothing.

The sequence orders future work; acceptance authorizes none of these implementations.

## Explicit Exclusions

This Accepted ADR does not activate Staff login; select a provider, tenant or issuer;
grant Roles, Permissions or assignments; enable Product administration/publication;
introduce automatic Staff provisioning; reuse Customer credentials as Staff proof;
modify the Staff association model; expose a callback/controller; issue Sessions;
select a library, signing key or trust issuer; implement migrations; bypass dependency
verification; authorize deployment; or close DEC-0012 or Product publication decisions.

## Related Documents

- [ADR-0019 — Authentication Session and Token Strategy](ADR-0019-authentication-session-token-strategy.md).
- [ADR-0020 — Identity Session Store Strategy](ADR-0020-identity-session-store-strategy.md).
- [ADR-0021 — Customer Authentication Authority Strategy](ADR-0021-customer-authentication-authority-strategy.md).
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md).
- [DEC-0012 — Administrative Role and Permission Matrix](../decisions/DEC-0012-administrative-role-permission-matrix.md).
- [Identity Backend Specification](../backend/identity/identity-backend.md), BIDN-REQ-011–014 and 020–026.
- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md), §§11–13 and 27.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-11-10 | Accepted | Project owner accepted enterprise-managed OIDC as the Staff authentication mechanism while acting across accountable Architecture, Identity, Security and Administration capacities; preserved all provider, assurance-mapping, dependency, implementation, deployment and operational-activation gates and default-deny administrative boundaries. |
| 0.1.0 | 2026-11-10 | Proposed | Proposed enterprise-managed Staff OIDC with Identity-owned verification, existing Staff associations and authoritative server-side Sessions; preserved pending owner dispositions, separate provider/dependency/implementation/activation gates and default-deny administrative boundaries. |
