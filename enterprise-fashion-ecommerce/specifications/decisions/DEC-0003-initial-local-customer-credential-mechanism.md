# DEC-0003 — Initial Local Customer Credential Mechanism

- **Identifier:** DEC-0003
- **Title:** Initial Local Customer Credential Mechanism
- **Type:** Security Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-09-29
- **Owner:** Identity
- **Authoritative:** true
- **Supersedes:** Not applicable — this is the first governed initial local Customer credential-mechanism decision.
- **Superseded By:** Not applicable — this Accepted Decision Record has not been superseded.

## Context

Accepted ADR-0021 establishes Identity-owned local Customer Authentication authority for the initial implementation. Identity owns verification of a subsequently governed local Customer credential and establishes the local Principal only from accepted Identity-owned Authentication evidence. ADR-0021 deliberately does not select the credential mechanism.

Accepted ADR-0019 governs the initial first-party browser boundary as an authoritative server-side Session with protected browser-cookie custody. Accepted ADR-0020 governs Spring Session JDBC backed by the application PostgreSQL database as the authoritative physical Session store. Neither Session decision makes Session state a credential store or selects a credential mechanism.

The Approved Product and Architecture contain password-management, password-reset, and password-hashing capability language, while `SECURITY-STANDARDS.md` supplies mandatory controls conditional on passwords existing. Those statements provide repository support for considering a password-based credential but do not independently select one. Passkey/WebAuthn-style and passwordless one-time or magic-link-style credentials remain viable alternatives whose different security, compatibility, recovery, provider, and operational consequences require explicit comparison.

This Accepted decision selects only the initial credential category, initial multiplicity, fallback status, and the security and lifecycle consequences inherent to that category. It does not authorize implementation.

## Decision

The initial local Customer credential mechanism SHALL be a **password-based local credential** owned by Identity.

The initial implementation SHALL support exactly one local Customer credential category: the password-based credential. No second local credential category and no fallback credential category are selected for the initial implementation.

The password credential SHALL be accepted only through later governed verification that satisfies applicable Security authority. Successful verification produces Identity-owned Authentication evidence; it does not itself create or rewrite Customer or Account business truth, establish contextual Authorization, or bypass ADR-0019 Session establishment.

This decision selects the credential category only. It does not select a login identifier, password policy, password-hashing algorithm or parameters, representation, persistence schema, recovery workflow, API Contract, framework component, library, provider, dependency, numerical threshold, or implementation.

## Decision Boundary

### In Scope

- Password-based local credential as the single initial primary Customer credential category.
- Exactly one initial local credential category.
- No initial fallback credential category.
- Security and lifecycle consequences inherent to password credentials.
- Compatibility, migration, and reversibility expectations for adding or replacing credential mechanisms later.

### Out of Scope

All matters listed under Downstream Non-Decisions remain separately governed. In particular, this decision does not equate a password with an email address, username, phone number, Customer, Account, Identity, Principal, or Session.

## Governing Authority

This decision is subordinate to and preserves:

- `AGENTS.md`, `PRODUCT.md`, `ARCHITECTURE.md`, `SECURITY-STANDARDS.md`, `GLOSSARY.md`, and `DECISIONS.md`;
- the Approved Identity Domain and BIDN;
- the Approved Customer Domain and BCUS;
- the Approved Storefront Account and Identity Frontend Specification;
- Accepted ADR-0019, ADR-0020, and ADR-0021;
- Accepted DEC-0001 and DEC-0002; and
- applicable Spring, Java, API, Database, and PostgreSQL standards.

DEC-0003 is authoritative only for the governed credential-category direction. It provides no implementation or dependency-admission authority.

## Security Analysis

### Phishing, Stuffing, and Brute Force

Passwords are phishable and reusable outside repository control. Reuse creates credential-stuffing Risk, and online verification creates brute-force and enumeration exposure. Any later implementation MUST inherit applicable compromised-value checking, rate and abuse controls, enumeration resistance, safe errors, monitoring, and incident response without this record selecting thresholds or mechanisms.

### Credential Theft, Secret Storage, and Replay

A password is a Customer-held secret and requires protected collection and transmission. Identity must retain only a later-governed non-reversible verification representation; plaintext or reversibly encrypted password storage is prohibited. Logs, telemetry, URLs, errors, events, frontend persistence, Customer profiles, support evidence, and unrelated Contracts must contain no password material.

The selected category does not by itself prevent phishing, endpoint compromise, credential reuse, or replay of captured input. Accepted Authentication evidence must remain bounded to Identity and must not be treated as the password itself.

### Recovery and Account Takeover

Password loss and compromise create recovery and account-takeover Risk. Recovery remains separately governed and must not weaken Authentication, disclose protected Account existence, silently create an Identity or Customer, or restore withdrawn access. No recovery channel, factor, provider, expiry, or support workflow is selected here.

### Device Loss, Shared Devices, Accessibility, and Compatibility

A password does not require a particular Customer device, platform authenticator, or hardware security key and is broadly compatible with current browser input capabilities. That compatibility does not remove accessibility obligations, shared-device exposure, password-manager considerations, shoulder-surfing Risk, or compromised-device Risk. Concrete user-interface behavior remains governed by later Contracts and frontend implementation.

### Privacy, Auditability, and Compromise Response

Credential processing must remain purpose-limited and minimize Sensitive Data. Audit Records and monitoring must distinguish intent, success, denial, abuse, compromise, change, invalidation, and uncertainty without recording password material or exposing protected existence. Confirmed or suspected compromise must support containment, invalidation, recovery, evidence preservation, Session invalidation where governed, and safe reconciliation.

### Future MFA and External Provider Compatibility

The password category must remain compatible with later governed Customer MFA. It does not select an MFA factor or policy. Future passkey, external Identity Provider, SSO, federation, or hybrid adoption remains possible only through separate governance and safe migration.

## Alternatives Considered

### A. Password-Based Local Credential — Selected

This alternative is supported by existing Product and Architecture capability language and by detailed conditional controls in `SECURITY-STANDARDS.md`. It works within ADR-0021's local Identity authority without requiring an external provider, mandatory email or phone identifier, delivery provider, platform authenticator, or new browser trust boundary.

Its disadvantages are material: passwords are phishable, may be reused, create credential-stuffing and brute-force exposure, require protected secret verification, and create recovery and support burden. Those Risks require later governed controls and implementation evidence. The alternative is selected because it is the only candidate presently supported by repository capability language that can establish one initial local path without first resolving a provider, mandatory delivery channel, platform-authenticator dependency, or Product Decision 5.

### B. Passkey / WebAuthn-Style Local Credential — Deferred

Passkeys can provide strong phishing resistance, remove password reuse and stuffing exposure, and reduce server-held reusable-secret Risk. They can also improve Authentication speed on supported devices.

They are not selected initially because repository governance does not yet establish authenticator eligibility, browser/platform support expectations, device loss and replacement behavior, shared-device behavior, attestation policy, discoverable-credential policy, recovery, Customer support, WebAuthn library admission, or migration evidence. Deferral does not reject future passkey adoption.

### C. Passwordless One-Time / Magic-Link-Style Local Credential — Deferred

One-time or magic-link credentials can avoid persistent Customer passwords and credential reuse. Their security and availability depend on a governed delivery destination, possession proof, expiry and replay controls, message delivery, provider behavior, redirect or code handling, recovery, and account-linking semantics.

They are not selected initially because the repository has not selected email or phone as a login identifier, has not resolved Product Decision 5, and has not selected an email/SMS provider, verification Contract, or delivery-evidence boundary. Deferral does not reject future passwordless adoption.

### Multiple Initial Credential Categories or a Fallback Credential

Multiple initial categories could improve Customer choice and migration flexibility. They would also require credential precedence, linking, recovery ownership, downgrade resistance, duplicate-Identity prevention, support, and reconciliation before one executable credential path exists. No second or fallback category is selected initially.

## Consequences

### Positive

- Establishes one clear initial local credential category within ADR-0021 authority.
- Avoids competing initial credential truth and fallback ambiguity.
- Uses repository-supported conditional password-security controls.
- Avoids requiring a new external provider, delivery channel, or platform authenticator before implementation planning.
- Preserves later passkey, MFA, and external-provider evolution.

### Negative and Trade-offs

- The repository assumes direct responsibility for password-verification security and compromise response.
- Passwords remain vulnerable to phishing, reuse, stuffing, guessing, and Customer handling failures.
- Password recovery, secure representation, compromised-value checking, abuse controls, monitoring, and support still require downstream governance and implementation.
- A later move toward passkeys or external authority may require credential migration or retirement and Customer communication.

## Product Impact

This decision establishes no registration eligibility, guest-versus-account policy, Customer lifecycle policy, mandatory email or phone use, or Customer MFA policy. Product Decision 5 remains unresolved and independent unless a later Approved workflow establishes a direct dependency.

## Architecture Impact

The selected category fits within Accepted ADR-0021 and does not change the Identity-owned Authentication authority, Customer ownership, Principal boundary, Modular Monolith, Session Architecture, Session store, provider boundary, or deployment topology. Acceptance review confirmed that existing password-specific wording in `PRODUCT.md` and `ARCHITECTURE.md` remains accurate and requires no synchronization.

## Identity Ownership Impact

Identity retains credential establishment, verification, replacement, compromise, invalidation, recovery security outcomes, accepted Authentication evidence, Principal establishment, and Session handoff. Framework or persistence representations cannot become Domain or Application authority.

## Customer and Account Ownership Impact

Customer retains Customer and Account business truth. A password, login identifier, credential record, Authentication outcome, or Principal cannot create or rewrite that truth. Customer-owned content and persistence must not contain credentials.

## Data Impact

Passwords are protected security material. This decision creates no credential representation, schema, table, column, index, constraint, migration, retention period, or Customer/Account association. Any later persistence must remain Identity-owned, follow ADR-0017 and ADR-0018, use Flyway, enforce least privilege, and avoid plaintext or reversible password storage.

## Compatibility and Migration Impact

There is no production Customer credential population to migrate. Future addition of passkeys or another credential category requires governed association, precedence, recovery, coexistence, downgrade resistance, duplicate prevention, evidence provenance, compatibility, rollback, and support behavior. Future external IdP, SSO, or federation adoption remains governed by ADR-0021 and must safely migrate or retire affected credentials without silently authenticating Customers or restoring withdrawn access.

## Operational Impact

Acceptance assigns the later implementation operational responsibility for password security, abuse detection, compromise handling, recovery, support, monitoring, auditability, maintenance, and incident response. This decision selects no service level, capacity, rate, lockout, timeout, recovery target, support workflow, provider, or infrastructure.

## Reversibility

The category decision is reversible only through later governance and safe credential migration, coexistence, or retirement. Rollback or migration must not restore compromised credentials, bypass current verification, create duplicate identities, weaken Session invalidation, or represent uncertain evidence as success. No migration date, transition window, import format, or dual-running design is selected.

## Downstream Non-Decisions

DEC-0003 does not select, define, admit, or authorize:

- login identifier semantics or mandatory email, phone, or username use;
- exact password-hashing algorithm, library, parameters, upgrade configuration, or `PasswordEncoder`;
- password length, composition, compromised-value source, rotation, attempt, rate, lockout, or other password policy;
- password reset or recovery workflow, channel, factor, token, provider, expiry, or support process;
- Product Decision 5 or Product Decision 23;
- Customer MFA policy, mechanism, factor, provider, protocol, fallback, or recovery;
- credential persistence schema or representation;
- Customer or Account persistence;
- Identity-to-Customer or Account persistence association;
- Spring Security `AuthenticationProvider` or `UserDetailsService` design;
- WebAuthn library, authenticator, provider, attestation, or discoverable-credential policy;
- email or SMS provider;
- API route, HTTP method, status, DTO, field, login Contract, registration Contract, verification Contract, or recovery Contract;
- Session TTL, cookie name, `Domain`, `Path`, exact `SameSite` value, expiry, or rotation interval;
- external Identity Provider, SSO, federation, OAuth2, OIDC, SAML, provider SDK, or Claims mapping;
- service-to-service Authentication;
- dependency admission, framework configuration, implementation class, test tool, or executable implementation;
- infrastructure, hosting, deployment, provider topology, or operational numerical threshold.

## Acceptance and Validation Criteria

The Accepted record and controlled synchronization verify:

1. metadata is `1.0.0 Accepted`, Type `Security Decision`, owner `Identity`, and `authoritative: true`;
2. password-based local credential is the single Accepted initial credential category;
3. exactly one initial local credential category is Accepted and no fallback category is selected;
4. Password selection is not interpreted as selection of a login identifier, policy, hash, representation, schema, recovery workflow, Contract, dependency, or implementation;
5. Identity, Customer, Account, Principal, and Session authority remain separate;
6. ADR-0019, ADR-0020, and ADR-0021 remain unchanged;
7. Product Decisions 5 and 23 remain unresolved;
8. password, passkey/WebAuthn-style, and passwordless one-time/magic-link alternatives are represented fairly;
9. phishing, stuffing, brute-force, enumeration, replay, theft, storage, takeover, recovery, device, shared-device, accessibility, compatibility, privacy, audit, operations, support, compromise, migration, MFA, and external-provider consequences are addressed without numerical invention;
10. no browser bearer Access Token or Refresh Token is introduced;
11. Spring Session remains outside credential authority and credential storage;
12. no API Contract or persistence object is invented;
13. dependency admission remains separate under DEC-0001;
14. no implementation or executable validation is claimed;
15. the final canonical synchronization set is limited to DEC-0003 and `DECISIONS.md` without expanding the decision boundary;
16. DEC-0003 is indexed exactly once as Accepted in `DECISIONS.md`; and
17. the acceptance change affects exactly DEC-0003 and `DECISIONS.md`, passes whitespace and diff validation, and introduces no unrelated tracked changes.

## Acceptance Synchronization

Controlled acceptance synchronizes only DEC-0003 and its indexed status and history in `DECISIONS.md`. Acceptance review confirmed that `PRODUCT.md`, `ARCHITECTURE.md`, `SECURITY-STANDARDS.md`, `GLOSSARY.md`, `SPRING.md`, `JAVA.md`, `API.md`, `DATABASE.md`, `POSTGRES.md`, Domain Specifications, Backend Specifications, and frontend Specifications require no change. Acceptance creates no implementation, dependency, API, persistence, recovery, MFA, Product-policy, or external-provider authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [Storefront Account and Identity Frontend Specification](../frontend/storefront/account-identity.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](./DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0002 — PostgreSQL Release Baseline](./DEC-0002-postgresql-release-baseline.md)

## Supersedes

None.

## Superseded By

None.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-29 | Accepted | Accepted password-based local Customer credentials as the single initial credential category with no initial fallback and Identity ownership, while preserving unresolved login identifier, password-security configuration, persistence, recovery, MFA, Product Decisions 5 and 23, Contracts, dependency admission, and implementation. |
| 0.1.0 | 2026-09-29 | Proposed | Proposed a password-based local credential as the single initial Customer credential category with no fallback category, preserving Identity and Customer authority, Session boundaries, Product Decisions 5 and 23, downstream mechanism details, future passkey and external-provider evolution, and separate dependency admission and implementation. |
