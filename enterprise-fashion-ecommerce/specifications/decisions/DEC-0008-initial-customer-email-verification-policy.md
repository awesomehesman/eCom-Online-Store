# DEC-0008 — Initial Customer Email Verification Policy

- **Identifier:** DEC-0008
- **Title:** Initial Customer Email Verification Policy
- **Type:** Product Decision / Security Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-09-30
- **Owner:** Product
- **Authoritative:** true
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

`PRODUCT.md` Product Decision 5 leaves Customer email-verification requirements unresolved. Accepted DEC-0004 establishes email as the single initial Customer-facing login identifier, while DEC-0005 establishes deterministic comparison, uniqueness, collision, and lifecycle semantics. Neither decision treats possession or submission of an email address as verification or Authentication proof.

Accepted DEC-0003, DEC-0006, and DEC-0007 govern the initial password credential, password hashing and verification, and Customer password policy. Accepted ADR-0019 through ADR-0022 govern the browser Session architecture, Session store, Identity-owned local Customer Authentication authority, and Identity–Customer/Account association. Registration and login Contracts remain unable to define authoritative completion, Authentication, Session, and protected-access outcomes until the Product and Security significance of email verification is explicit.

This decision establishes a strict initial email-verification gate. It remains provider-neutral and implementation-neutral and does not define a delivery mechanism, verification artifact, Contract, persistence design, recovery workflow, or implementation.

## Verified Governing Constraints

1. Email is the single initial Customer-facing login identifier and remains untrusted input until governed validation establishes the applicable evidence.
2. Email verification can prove control of the verified email address at the accepted verification event; it does not prove a person's real-world identity, Customer or Account ownership, Authentication, association, entitlement, or Authorization.
3. Identity owns login identifiers, credentials, Authentication evidence, Principal establishment, verification security evidence, and Session handoff.
4. Customer owns Customer and Account business truth and applicable Customer-to-Account relationships.
5. The Identity-to-Customer association remains Identity-owned under ADR-0022 and must be resolved unambiguously for protected Customer context.
6. Registration completion requires all applicable authoritative Identity, Customer, association, Product, and Security conditions.
7. Authentication, Principal establishment, Session establishment, and contextual Authorization remain distinct outcomes.
8. Security-sensitive failure must preserve default denial, enumeration resistance, privacy, Sensitive Data protection, auditability, and safe recovery where separately governed.

## Decision

Verification of the current authoritative Customer login email SHALL be required for the initial Customer registration and login path.

### Verification Meaning and Authority

Accepted verification evidence means only that the verification interaction established control of the current authoritative login email under the later governed verification mechanism. Verification is both Identity-owned security state and an enforcement gate under this Product policy.

Verification MUST NOT be treated as proof of real-world Identity, successful password Authentication, an authenticated Principal, Session establishment, Customer or Account ownership, the Identity-to-Customer association, contextual Authorization, entitlement, recovery authority, marketing Consent, or any unrelated Domain fact.

Message dispatch, delivery, opening, client or UI state, identifier knowledge, a verification request, or an incomplete challenge MUST NOT establish verified state. Only accepted Identity-owned verification evidence for the current authoritative login email may do so.

### Registration Completion

Initial registration MUST NOT be represented as complete until the current authoritative login email has accepted verification evidence and the separately governed authoritative Identity, credential, Customer, Account, and association outcomes required by ADR-0022 are complete.

Registration may hold a bounded pending-verification outcome while verification is not yet established. That pending outcome is not completed registration, an authenticated Principal, an active Session, Customer or Account access, association proof, or Authorization. This decision does not prescribe whether any provisional internal record exists, which owner stores it, or its physical representation.

### Authentication, Principal, and Session Gate

An unverified Customer login email MUST NOT produce successful Customer Authentication for the initial flow, establish an authenticated Customer Principal, or establish a Customer Session. Correct password evidence does not override the verification gate.

Rejected, invalid, unavailable, stale, conflicting, or uncertain verification evidence MUST preserve default denial for Authentication and Session establishment. Externally observable outcomes MUST resist Account enumeration and MUST NOT disclose whether an email, Identity, Customer, Account, credential, association, or verification record exists.

### Account and Protected Customer Access

An unverified login email MUST NOT authorize access to an Account or other protected Customer functionality. Verification alone also MUST NOT authorize that access: successful Authentication, an applicable valid Session, authoritative association evidence, and owning-Domain contextual Authorization remain required.

Public functionality that does not require a Customer Principal remains governed independently and MUST NOT be reclassified by this decision. This decision does not resolve guest checkout or any unrelated access policy.

## Verification Lifecycle Semantics

The policy recognizes these outcomes without prescribing a persistence state machine:

- **Not established:** no accepted verification evidence exists for the current authoritative login email. Registration remains incomplete and Customer Authentication, Principal establishment, Session establishment, Account access, and protected Customer access remain denied.
- **Established:** accepted Identity-owned evidence applies to the current authoritative login email. This satisfies only the email-verification condition; every other required outcome remains independently authoritative.
- **Rejected or invalid:** submitted evidence is not accepted, has failed validation, is inapplicable, has been replayed where single-effect behavior applies, or otherwise cannot establish verification. No protected success follows.
- **Expired where applicable:** evidence outside its later governed validity boundary cannot establish or restore verified state. This decision selects no lifetime.
- **Unavailable or uncertain:** required evidence cannot be obtained, validated, reconciled, or attributed unambiguously. The verification gate fails closed.
- **Superseded by login-email change:** evidence for a previous login email does not verify a replacement login email.

Repeated processing of the same accepted verification intent MUST be safely idempotent and MUST NOT create duplicate Identity, Customer, Account, credential, association, Principal, or Session outcomes. Concurrent, stale, replayed, reordered, or conflicting evidence MUST NOT overwrite newer authoritative login-email state or restore verification for a superseded value. Ambiguous ownership or applicability fails closed and requires reconciliation without arbitrary selection.

### Retry and Re-verification

A Customer MUST remain eligible to retry or obtain re-verification through a later governed safe workflow when verification is pending, rejected, invalid, expired where applicable, unavailable, uncertain, or invalidated by login-email change. Eligibility does not guarantee delivery, acceptance, Account existence disclosure, or success.

Retry and re-verification behavior MUST preserve enumeration resistance, abuse resistance, privacy, current login-email authority, replay protection, and safe idempotency. No retry count, rate threshold, lockout duration, validity period, resend interval, escalation rule, or support override is selected.

### Login-Email Change

Accepted verification evidence is bound to the authoritative login-email value to which it applies. An accepted login-email change invalidates the applicability of verification evidence for the prior value to the replacement value. The replacement email is unverified until separately accepted verification evidence is established for it.

After such a change, new Customer Authentication, Principal establishment, Session establishment, and protected Customer access that require current-email verification MUST fail closed until re-verification succeeds. This policy does not independently select whether or how existing Sessions are invalidated, the email-change Contract, recovery behavior, or operational support workflow; those effects require compatible downstream governance while preserving ADR-0019 revocation and withdrawn-access semantics.

## Security and Privacy Boundaries

Email-account compromise can allow an attacker to satisfy address-control verification and therefore remains a material Risk. Verification does not replace password Authentication, compromise detection, Session controls, contextual Authorization, recovery security, or Customer support safeguards.

Verification links, codes, or other artifacts are security-sensitive. They must be protected from disclosure, replay, substitution, tampering, stale use, cross-Identity use, and application to an email value changed after issuance. This decision selects no artifact format, entropy, cryptographic algorithm, transport, storage, or expiry.

Externally observable request, resend, validation, pending, rejection, expiry, unavailable, uncertainty, and success behavior MUST minimize disclosure and resist enumeration, guessing, brute force, automation, and timing or response-shape oracles. Exact abuse-control mechanisms and numerical values remain separately governed.

Audit Records and security evidence must be sufficient to distinguish material verification intent, issue or dispatch where applicable, acceptance, rejection, invalidity, replay, expiry where applicable, supersession, failure, uncertainty, and reconciliation without recording verification Secrets or exposing protected Account existence. Email addresses and verification evidence remain Sensitive Data and must be purpose-limited, minimized, access-controlled, and excluded from unsafe logs, metrics, traces, events, analytics, URLs, support evidence, and error payloads.

Recovery remains separately governed. Email verification or email-account control MUST NOT silently become sufficient recovery authority or bypass the later governed recovery evidence boundary.

## Alternatives Considered

### A. Verification Required Before Registration Completion and Login — Selected

Require accepted verification evidence for the current login email before registration completion, successful Customer Authentication, Customer Principal establishment, Customer Session establishment, Account access, or protected Customer functionality.

This provides one deterministic initial gate across registration and login, verifies control of the identifier used for local Customer Authentication, and avoids an initial partially privileged unverified Account model. Its costs include delivery dependence, registration friction, support burden, fail-closed unavailability, and exclusion of Customers who cannot complete verification.

### B. No Mandatory Initial Email Verification — Not Selected

Registration and login could proceed using the governed password credential without proof of email control. This reduces delivery dependency and friction but permits Accounts to be established against mistyped, unavailable, or third-party-controlled addresses and weakens the reliability of later email-based communication. It is not selected for the initial flow.

### C. Registration Completes Before Verification, Which Gates Later Protected Access — Not Selected

Customer and Account truth could be established while protected access remains limited until verification. This can preserve an onboarding record and allow later completion, but it requires additional Customer lifecycle and access-state semantics and creates a partially registered Account state that current Product governance does not define. It is not selected initially.

### D. Authentication Allowed but Session or Account Access Gated — Not Selected

Identity could accept password Authentication for an unverified email while withholding a Session or protected access. This separates credential correctness from access but creates an authenticated-yet-unverified Principal boundary and additional externally observable outcomes. It is not selected for the initial flow.

## Consequences

### Benefits and Enabled Outcomes

- Product Decision 5 becomes explicit enough for later registration and login Contract governance.
- The initial flow has one deterministic verification gate across registration, Authentication, Session, and protected Customer access.
- Verification remains narrowly defined as control of the current login email rather than broader Identity, Account, association, or Authorization proof.
- Login-email change has explicit re-verification consequences without redefining stable Identity or Customer truth.
- Provider-neutral lifecycle and failure semantics permit later Contract and implementation choices without changing Product policy.

### Costs, Risks, and Limitations

- Registration and login availability depend abstractly on a verification-delivery and validation capability that has not yet been selected or implemented.
- Customers cannot complete registration or login while verification is unavailable, uncertain, or incomplete.
- Email-account compromise can satisfy the address-control gate and remains an account-takeover Risk.
- Enumeration-resistant behavior, replay protection, concurrency safety, support, monitoring, and reconciliation add delivery and operational burden.
- The strict gate may increase abandonment and support demand and may require later Product review based on evidence.

### Compatibility, Migration, and Reversibility

No production Customer population currently requires migration. Later implementation must preserve deterministic association between verification evidence and the authoritative login-email value and must not reinterpret stale evidence as applying to a changed address.

Changing to optional verification or a later-access gate would require superseding Product/Security governance, compatibility analysis, safe treatment of pending or previously verified Customers, Contract evolution, migration and reconciliation, and protection against unintended access expansion. Tightening a later policy must not silently withdraw access without governed Customer, Session, support, and recovery consequences. The decision is reversible through governance, but exit cost grows once Contracts, stored evidence, delivery operations, and Customer expectations exist.

## Operational and Support Impact

Operations and support must distinguish pending, accepted, rejected, invalid, expired where applicable, unavailable, uncertain, superseded, replayed, and conflicting verification outcomes without exposing protected existence or verification Secrets. Support MUST NOT manually assert verification, create Authentication evidence, or bypass Identity, Customer, association, Session, or Authorization authority unless separately governed.

Delivery dependence must be observable and recoverable at an abstract level, but this decision selects no provider, service level, alert threshold, retry policy, escalation process, or operational override.

## Explicit Non-Decisions

DEC-0008 does not select, define, admit, or authorize:

- an email-delivery service or provider, verification provider, vendor, API, SDK, dependency, network design, or provider Contract;
- an API or HTTP route, method, DTO, request or response schema, status, exact error code, public or internal Contract, or frontend interaction;
- a token, code, link, artifact format, generation mechanism, entropy value, cryptographic algorithm, signing method, exact lifetime, resend interval, retry count, rate limit, lockout duration, timeout, SLO, or capacity target;
- a table, column, schema, index, constraint, repository, ORM mapping, cache, queue, topic, event technology, migration, or other persistence or messaging implementation;
- infrastructure, hosting, deployment topology, provider configuration, monitoring product, support override, or completed implementation;
- password recovery design, recovery channel, recovery factor, recovery provider, recovery token, or recovery authority;
- ordinary Customer MFA policy, mechanism, factor, provider, protocol, fallback, or recovery;
- an external Identity Provider, social login, SSO, federation, OAuth2, OIDC, SAML, callback, or provider-subject mapping;
- unrelated Authorization policy, guest-checkout policy, Customer or Account lifecycle model, marketing Consent, Product Decision 28, or any other Open Product Decision;
- implementation of DEC-0003 through DEC-0007 or ADR-0019 through ADR-0022; or
- a claim that registration, verification delivery, Authentication, Session establishment, Customer or Account creation, association, protected access, recovery, or implementation currently exists.

## Completed Governance Review

Acceptance required durable governance-review representation from:

- Product approval of mandatory verification, registration completion, login, Session, Account and protected-access gates, Customer experience, and reversibility;
- Identity approval of verification evidence ownership, current-email binding, Authentication and Principal gates, lifecycle integrity, and separation from Customer truth;
- Security approval of default denial, replay and stale-evidence resistance, enumeration resistance, Sensitive Data handling, account-takeover Risk, evidence, and non-recovery boundaries;
- Architecture approval that ADR-0019 through ADR-0022 remain intact and that no new Architecture, provider, Contract, persistence, dependency, or infrastructure authority is introduced;
- Customer approval of registration, Account, protected-function, association, and Customer-business-truth boundaries;
- Privacy approval of email and verification-evidence minimization, disclosure, purpose limitation, and retention neutrality;
- Notifications approval of bounded non-authoritative delivery interaction without provider, channel implementation, or delivery-as-verification authority; and
- Engineering review of deterministic lifecycle, idempotency, concurrency, failure, compatibility, migration, operations, testability, and implementation neutrality.

The controlled governance review completed with no unresolved acceptance blocker. This Accepted record does not fabricate or claim reviewer names, signatures, tickets, meetings, external approval artifacts, implementation evidence, provider validation, or executable tests, and acceptance does not claim implementation completion.

## Acceptance Synchronization

Controlled acceptance synchronizes exactly these canonical artifacts:

1. `specifications/decisions/DEC-0008-initial-customer-email-verification-policy.md` is promoted to `1.0.0 Accepted` and `authoritative: true` without claiming implementation.
2. `.ai/core/DECISIONS.md` changes DEC-0008's indexed status from Proposed to Accepted and records the synchronization in Revision History while preserving its identifier, type, owner, date, and canonical path.
3. `.ai/core/PRODUCT.md` resolves Product Decision 5 and synchronizes only the accepted initial Customer email-verification policy into the existing Customer Account area without resolving another Open Product Decision or claiming implementation.
4. `.ai/core/ARCHITECTURE.md` removes only the stale statement that Product Decision 5 remains unresolved while preserving Product Decision 28 and all Architecture authority.

No additional canonical source requires modification for this acceptance. Acceptance establishes policy authority only and does not claim implementation completion.

## Accepted-State Validation Criteria

The Accepted record verifies:

1. metadata is `1.0.0 Accepted`, Type `Product Decision / Security Decision`, owner `Product`, date `2026-09-30`, and `authoritative: true`;
2. Product Decision 5 is resolved by this Accepted decision and canonical Product synchronization rather than restated as an unresolved question;
3. accepted verification for the current authoritative login email is required before registration completion;
4. an unverified Customer cannot produce successful Customer Authentication, establish an authenticated Customer Principal, or establish a Customer Session in the initial flow;
5. an unverified login email cannot enable Account or protected Customer access, while verification alone is not Authorization;
6. verification proves only control of the current authoritative login email under the governed mechanism and does not prove Identity, Customer or Account ownership, association, Authentication, Session, entitlement, recovery authority, or unrelated Domain truth;
7. not-established, established, rejected or invalid, expired where applicable, unavailable or uncertain, and login-email-superseded outcomes are deterministic without defining a persistence state machine;
8. login-email change prevents prior-email verification evidence from applying to the replacement value and requires re-verification for later gated outcomes;
9. retries, duplicate processing, concurrency, replay, stale evidence, reordered work, conflict, and uncertainty preserve current authoritative state, safe idempotency, reconciliation, and default denial;
10. externally observable behavior preserves enumeration resistance, privacy, Sensitive Data protection, safe disclosure, and non-oracle outcomes;
11. email-account compromise, artifact interception or disclosure, replay, stale evidence, brute-force or guessing where applicable, logging, audit, support, and recovery implications are bounded without invented mechanisms or numerical values;
12. DEC-0003 through DEC-0007 remain unchanged and authoritative within their scopes;
13. ADR-0019 through ADR-0022 remain unchanged and authoritative within their scopes;
14. Product Decision 28 and every unrelated Open Product Decision remain unresolved;
15. no provider, API, SDK, dependency, Contract, persistence, token or code mechanism, lifetime, retry or rate value, cache, messaging technology, recovery, MFA, external IdP, infrastructure, frontend, or implementation authority is created;
16. completed Product, Identity, Security, Architecture, Customer, Privacy, Notifications, and Engineering governance review is represented without fabricated authority or implementation claims;
17. controlled acceptance synchronizes exactly DEC-0008, `DECISIONS.md`, `PRODUCT.md`, and `ARCHITECTURE.md` without silently broadening scope; and
18. DEC-0008 is `1.0.0 Accepted` and `authoritative: true`, `DECISIONS.md` indexes DEC-0008 exactly once as Accepted, `PRODUCT.md` resolves Product Decision 5, all four files are synchronized consistently, whitespace and diff validation pass, and no unrelated tracked changes are introduced.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [ADR-0022 — Initial Identity–Customer/Account Association Strategy](../adr/ADR-0022-identity-customer-account-association-strategy.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](DEC-0003-initial-local-customer-credential-mechanism.md)
- [DEC-0004 — Customer Login Identifier Semantics](DEC-0004-customer-login-identifier-semantics.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0006 — Customer Password Hashing and Verification Strategy](DEC-0006-customer-password-hashing-verification-strategy.md)
- [DEC-0007 — Initial Customer Password Policy](DEC-0007-initial-customer-password-policy.md)

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-30 | Accepted | Accepted mandatory verification of the current authoritative Customer login email before registration completion, Customer Authentication, Principal and Session establishment, Account access, or protected Customer functionality, with deterministic lifecycle, login-email-change, default-denial, enumeration-resistance, privacy, security, compatibility, migration, and reversibility boundaries; synchronized DEC-0008, `DECISIONS.md`, `PRODUCT.md`, and the narrow affected `ARCHITECTURE.md` statement without claiming implementation completion. |
| 0.1.0 | 2026-09-30 | Proposed | Proposed mandatory verification of the current authoritative Customer login email before registration completion, Customer Authentication, Principal and Session establishment, Account access, or protected Customer functionality, with deterministic lifecycle, login-email-change, default-denial, enumeration-resistance, privacy, security, compatibility, migration, and reversibility boundaries while preserving DEC-0003 through DEC-0007, ADR-0019 through ADR-0022, Product Decision 28 and unrelated Open Product Decisions, and all provider, Contract, persistence, recovery, MFA, external-IdP, infrastructure, frontend, and implementation non-decisions. |
