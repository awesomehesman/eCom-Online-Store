# DEC-0004 — Customer Login Identifier Semantics

- **Identifier:** DEC-0004
- **Title:** Customer Login Identifier Semantics
- **Type:** Product Decision / Security Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-09-29
- **Owner:** Identity
- **Authoritative:** true
- **Supersedes:** Not applicable — this is the first governed Customer login-identifier decision.
- **Superseded By:** Not applicable — this Accepted Decision Record has not been superseded.

## Context

Accepted ADR-0021 establishes Identity-owned local Customer Authentication authority. Accepted DEC-0003 establishes an Identity-owned password-based local Customer credential as the single initial credential category with no initial fallback. Neither decision selects what a Customer submits to identify the applicable Identity during login.

The Approved Identity Domain and BIDN require identifiers to remain untrusted references, preserve enumeration resistance, and avoid becoming Authentication or Authorization proof. The Approved Customer Domain and BCUS require stable Customer and Account business identity independently of mutable contact, credential, Principal, or Session state. The Storefront Account and Identity Frontend Specification presents Authentication intent but cannot establish identifier, Identity, Principal, or Authentication truth.

Registration and login Contracts cannot safely define required input, uniqueness conflicts, error concealment, or association semantics until the initial Customer-facing login identifier is governed. This Accepted decision selects only that identifier category and the minimum semantic, security, lifecycle, and evolution boundaries necessary for later governance.

## Decision

An **email address** SHALL be the single initial Customer-facing login identifier category for local Customer Authentication.

The submitted email address SHALL be treated only as untrusted Authentication lookup input. Possession, knowledge, submission, lookup match, Customer-profile presence, or mailbox access SHALL NOT prove Identity, Authentication, Authorization, Customer ownership, Account ownership, mailbox control, verified-email status, or recovery authority. Identity may establish the local Principal only after the separately governed password credential is successfully verified through accepted Identity-owned Authentication evidence.

The initial implementation SHALL NOT support login by mobile or phone number, username, Customer ID, Account ID, Principal ID, internal credential identifier, or arbitrary multiple identifier categories. Those alternatives are deferred rather than permanently prohibited.

## Decision Boundary

### In Scope

- Email address as the single initial Customer-facing login identifier category.
- Email as untrusted Authentication lookup input only.
- Identity ownership of lookup interpretation and identifier security lifecycle.
- Requirements for deterministic comparison, uniqueness, conflict, change, privacy, and enumeration semantics before implementation.
- Compatibility and migration expectations for future identifier categories.

### Out of Scope

This decision does not select verified-email policy, exact canonicalization, case-comparison implementation, persistence design, Contracts, recovery, MFA, framework components, dependencies, providers, infrastructure, or numerical controls.

## Product Decision 5 Boundary

Product Decision 5, Customer email-verification requirements, remains unresolved.

Selecting email as the login identifier does not decide whether Customer email verification is required. Verified email is not made a prerequisite for registration, Authentication, Session establishment, Account access, or another Product outcome by this decision. If a later Approved registration or Authentication workflow requires verified email, the dependency MUST be resolved through applicable Product governance.

Email-as-identifier semantics and email-verification policy remain distinct: an address can be lookup input without its submission or presence proving mailbox control.

## Authority and Ownership Boundary

Identity owns:

- Authentication interpretation of the submitted login identifier;
- lookup, matching, conflict, and accepted Authentication-evidence semantics;
- identifier security lifecycle and change consequences within Authentication;
- enumeration resistance and security-safe disclosure; and
- Principal establishment.

Customer retains Customer and Account business identity, profile, contact information, and lifecycle truth under existing governance. Using an email address for Authentication MUST NOT make mutable Customer contact data authoritative Authentication proof or allow Identity to rewrite Customer or Account business truth.

The Customer-facing login input remains distinct from stable Identity, Customer, Account, Principal, credential, and Session identity. A change to the login email MUST NOT create a new Customer, Account, Principal, password credential, or Session merely because the identifier changes.

## Normalization, Case, and Uniqueness Boundary

Implementation MUST NOT begin until deterministic canonical comparison semantics are defined for the governed login-email purpose.

Before lookup or uniqueness enforcement is implemented, later governance or approved design must explicitly define:

- exact normalization behavior;
- case-comparison behavior;
- uniqueness scope;
- conflict and duplicate handling;
- identifier establishment and change lifecycle;
- treatment of reassigned or reused addresses;
- persistence enforcement; and
- compatibility and migration behavior.

This decision does not assume that every email local part is universally case-insensitive. It selects no normalization algorithm, provider-specific canonicalization, case-folding rule, maximum length, database column, index, constraint, schema, SQL, or mapping.

## Security and Privacy Analysis

### Enumeration and Failure Disclosure

Email lookup can expose whether an Account or Identity exists and can assist credential stuffing or brute-force targeting. Authentication responses, response timing, logs, support tools, and recovery behavior MUST preserve applicable enumeration resistance and safe disclosure. This decision selects no response text, timing mechanism, rate, delay, threshold, lockout, or retry policy.

### PII, Logs, and Telemetry

An email address is PII and must remain purpose-limited, minimized, protected, and unavailable to unrelated Domains or unauthorized contexts. Raw login input and unnecessary email values MUST NOT appear in URLs, unsafe errors, Logs, Metrics, Traces, analytics, screenshots, fixtures, exports, or ordinary support evidence. Any required audit or correlation representation must follow existing privacy and security authority.

### Phishing, Credential Stuffing, and Account Takeover

Email use can make phishing and stuffing targeting easier when addresses are known or leaked. Knowledge of the email does not weaken DEC-0003 password-verification requirements and cannot establish Authentication. Later controls must preserve compromised-credential checking, abuse detection, failure-closed behavior, and safe compromise response without this decision inventing mechanisms or numerical policy.

### Identifier Changes, Conflict, and Reassignment

Email change, conflict, reassignment, reuse, and concurrent update can create account-takeover or identity-confusion Risk. Later change governance must require sufficient authority, preserve stable Identity and Customer association, handle conflicts safely, invalidate or review affected access where required, and retain appropriate evidence. This decision does not select proof, workflow, Session consequences, notification, or recovery consequences.

### Recovery and Shared Devices

Password recovery remains separately governed. Access to, possession of, or knowledge of the login email is not sufficient recovery evidence. No recovery token, channel, provider, expiry, or workflow is selected. Shared-device presentation must not retain or disclose login email beyond governed usability, privacy, and security behavior.

## Compatibility with Existing Decisions

### ADR-0019

Email lookup does not alter the authoritative server-side Session, protected browser-cookie custody, CSRF, fixation, renewal, logout, revocation, failure-closed, or browser bearer-token boundaries.

### ADR-0020

The login identifier is not Session state or Session authority. DEC-0004 creates no Spring Session, PostgreSQL Session, Redis, schema, or Session-persistence change.

### ADR-0021

Identity-owned local Customer Authentication authority remains unchanged. Email lookup cannot establish the Principal before accepted Authentication evidence, and Customer and Account authority remain separate.

### DEC-0003

The password-based local Customer credential remains the single initial credential category with no fallback. Email is lookup input rather than a credential, password substitute, or fallback Authentication mechanism.

## Alternatives Considered

### A. Email Address — Selected

Email is familiar for Customer-facing account access, aligns with existing Customer Account and password-management capability direction, and avoids inventing a new public username or requiring phone collection. It can remain lookup input without resolving email verification or making contact data authoritative.

Its costs include PII exposure, enumeration and phishing targeting, normalization and case ambiguity, mutable-address lifecycle, duplicate and reassignment Risk, and Customer-support burden. Those consequences require explicit downstream semantics and implementation evidence.

### B. Mobile or Phone Number — Deferred

Phone numbers may be familiar in mobile-first journeys and can support future separately governed possession verification. They introduce sensitive PII, international normalization, shared-number and reassignment Risk, SIM-swap exposure, regional availability differences, and possible SMS/provider dependencies. The repository does not mandate phone collection or verification. This option remains eligible through future governance.

### C. Username — Deferred

A username can reduce routine disclosure of contact PII and remain independent of mutable email or phone data. It would introduce a new Customer-facing concept requiring creation, uniqueness, normalization, case handling, prohibited-name policy, change behavior, support, and recovery semantics. Current Product authority does not require a username. Future governance may introduce it with migration and Contract evolution.

### D. Multiple Identifiers — Deferred

Multiple identifiers could improve Customer flexibility but introduce type detection, collision, precedence, duplicate association, normalization, enumeration, recovery, support, and migration complexity. One submitted value could ambiguously match different categories. Multiple identifiers are deferred until a single governed path exists and applicable collision and evolution semantics are approved.

## Consequences

### Positive and Enabled Outcomes

- Customers initially have one Customer-facing login identifier category: email.
- Registration and login Contract design can target one initial identifier category once downstream semantics are governed.
- No new username concept or initial multi-identifier ambiguity is introduced.
- Identity remains stable independently of mutable login email.
- Future additional identifier categories remain possible through governance and migration.

### Negative and Trade-offs

- Email is PII and creates privacy and enumeration exposure requiring existing security controls.
- Email lifecycle introduces change, collision, reassignment, and reuse concerns.
- Deterministic normalization, case comparison, uniqueness, conflict, establishment and change, reassignment, and persistence semantics remain required before implementation.
- Registration and login implementation remains blocked until those downstream semantics, Contracts, association and persistence design, and password-security prerequisites are governed.
- Support and operational processes must avoid exposing identifier existence or treating email possession as Authentication or recovery evidence.
- Migration to additional identifier categories requires explicit governance, collision handling, compatibility, and preservation of stable Identity.

These consequences do not resolve Product Decision 5, make verified email mandatory, make email possession Authentication or recovery evidence, or authorize persistence, API Contracts, dependencies, or implementation.

## Product Impact

The decision defines the initial Customer-facing login-input category only. It does not establish registration eligibility, mandatory mailbox verification, mandatory contact fields beyond later Contract needs, guest-versus-account policy, Customer lifecycle, recovery policy, or MFA policy.

## Architecture Impact

No Module, trust, provider, persistence, Session, deployment, or authority boundary changes. Identity remains Authentication owner; Customer remains Customer and Account business owner. No new ADR is required by this decision.

## Data Impact

Email login input is purpose-limited PII. This decision selects no source-of-truth record, persistence representation, table, column, schema, index, constraint, retention period, or Customer-to-Identity association implementation. Later persistence must follow ADR-0017, ADR-0018, Database standards, least privilege, and governed uniqueness semantics.

## Compatibility, Migration, and Reversibility

No production Customer login-identifier population requires migration. A future additional or replacement identifier category requires governance of association, collision, precedence, normalization, Customer communication, recovery, coexistence, Contract compatibility, rollback, and removal. Migration must not create duplicate identities, silently change Customer or Account truth, authenticate from identifier possession, or restore withdrawn access.

## Operational Impact

Later implementation must support safe duplicate and conflict handling, identifier changes, reassignment investigation, enumeration-resistant support, privacy-safe diagnostics, monitoring, Audit Records, and incident response. This decision selects no provider, support workflow, service level, rate, timeout, lockout, recovery target, or infrastructure.

## Downstream Non-Decisions

DEC-0004 does not select, define, admit, or authorize:

- Product Decision 5 or Product Decision 23;
- exact email normalization or canonicalization algorithm;
- exact case-comparison implementation;
- provider-specific email rewriting;
- exact uniqueness implementation or persistence scope;
- identifier-change proof, workflow, conflict, Session, notification, or recovery behavior;
- credential or identifier persistence representation, schema, table, column, index, constraint, SQL, or mapping;
- Identity-to-Customer or Account persistence association;
- password hashing algorithm, configuration, parameters, library, or `PasswordEncoder`;
- password policy;
- password reset or recovery mechanism, token, channel, provider, expiry, or workflow;
- Customer MFA policy or mechanism;
- registration, login, verification, or recovery Contract;
- API route, HTTP method, status, DTO, field name, error text, or timing design;
- Spring Security `AuthenticationProvider`, `UserDetailsService`, or another framework component;
- Session TTL or cookie configuration;
- external Identity Provider, SSO, federation, protocol, SDK, or Claims mapping;
- dependency admission, implementation class, executable implementation, or completed testing;
- infrastructure, provider, deployment topology, or numerical abuse-control threshold.

## Acceptance and Validation Criteria

The Accepted record and canonical synchronization verify:

1. metadata is `1.0.0 Accepted`, Type `Product Decision / Security Decision`, owner `Identity`, and `authoritative: true`;
2. email address is the single initial Customer-facing login identifier category;
3. email input, possession, submission, match, profile presence, or mailbox access is not Identity, Authentication, Authorization, ownership, mailbox-control, verification, or recovery proof;
4. Product Decision 5 remains unresolved and verified email is not silently required;
5. Identity owns Authentication interpretation and Principal establishment while Customer retains Customer and Account business truth;
6. no other initial identifier category or fallback is selected, and future alternatives remain governable;
7. deterministic comparison, normalization, case, uniqueness, conflict, change, reassignment, and persistence semantics remain required before implementation without being invented here;
8. identifier change cannot create a new Identity, Customer, Account, Principal, credential, or Session merely because the email changes;
9. password recovery remains separately governed and mailbox access is not assumed sufficient recovery proof;
10. ADR-0019, ADR-0020, ADR-0021, and DEC-0003 remain compatible and unchanged;
11. no registration, login, recovery, verification, or other API Contract is authorized;
12. no credential, identifier, Customer, Account, or association persistence model is authorized;
13. no framework component, dependency, provider, or infrastructure is authorized;
14. no executable registration or login implementation is authorized;
15. Product Decisions 5 and 23 remain unresolved;
16. email, phone, username, and multiple-identifier alternatives remain represented fairly;
17. security, privacy, operational, compatibility, migration, and reversibility consequences remain recorded without numerical or implementation invention; and
18. canonical acceptance synchronization is limited to DEC-0004, `DECISIONS.md`, and `PRODUCT.md`, passes whitespace and diff validation, and introduces no unrelated tracked changes.

## Acceptance Synchronization

Acceptance synchronized DEC-0004, its indexed status and history in `DECISIONS.md`, and the directly affected Customer-facing Product truth in `PRODUCT.md`. No other canonical source, Specification, Contract, build file, dependency, test, or implementation file required synchronization. Acceptance establishes the governed identifier direction only and claims no dependency admission or implementation completion.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [API.md](../../.ai/backend/API.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [Storefront Account and Identity Frontend Specification](../frontend/storefront/account-identity.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](./DEC-0003-initial-local-customer-credential-mechanism.md)

## Supersedes

None.

## Superseded By

None.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-29 | Accepted | Established email address as the single initial Customer-facing login identifier while preserving Product Decision 5 and downstream normalization, uniqueness, persistence, Contract, recovery, MFA, dependency, and implementation decisions. |
| 0.1.0 | 2026-09-29 | Proposed | Proposed email address as the single initial Customer-facing login identifier while preserving Product Decision 5 and downstream security, normalization, persistence, Contract, recovery, MFA, dependency, and implementation decisions. |
