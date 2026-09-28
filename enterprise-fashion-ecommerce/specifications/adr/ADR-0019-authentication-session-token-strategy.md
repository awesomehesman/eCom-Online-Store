# ADR-0019 — Authentication Session and Token Strategy

## Identifier

ADR-0019

## Title

Authentication Session and Token Strategy

## Version

1.0.0

## Status

Accepted

## Date

2026-09-28

## Last Updated

2026-09-28

## Owner

Architecture

## Authoritative

true

## Scope

authentication-session-architecture

## Context

Existing repository authority assigns Authentication credentials, trusted Principal establishment, Sessions, revocation, and Identity-owned security evidence to Identity. Authentication establishes trusted Principal context but does not replace contextual server-side Authorization by the Domain or Use Case owning the affected Resource, action, property, association, and state.

Customer-facing capability already includes sign-in, sign-out, password recovery, and Session presentation. Logout must terminate authoritative server-side Session state or otherwise revoke relevant credential authority; clearing frontend state alone is insufficient. Password reset where required, compromise handling, account disablement, and material privilege changes must invalidate affected access. Renewal, retries, concurrent work, and reordered processing must not restore withdrawn access.

Existing security governance prefers minimally scoped `HttpOnly`, `Secure`, strictest-workable `SameSite` cookies for browser Session credentials. Cookie-authenticated state-changing requests require CSRF protection, and CORS is not CSRF protection. Session secrets, Refresh Tokens, and equivalent Secrets must not enter persistent browser storage, URLs, logs, analytics, telemetry, or error responses.

Privileged identities retain the mandatory MFA requirements already established by `SECURITY-STANDARDS.md`. The concrete Identity Provider, MFA factor, provider, protocol, broader Customer MFA policy, Session or token mechanism, and implementation technology remain unresolved.

`ARCHITECTURE.md` §30.5 and former §34 item 3 required an ADR for the exact Customer and administrator Session/token strategy. This Accepted ADR resolves only the initial human/browser Session boundary. It does not invent Product behavior, API Contracts, token Claims, numerical expiry values, provider selection, infrastructure, or implementation.

## Decision Drivers

- Preserve Identity ownership of Authentication, Principal establishment, Sessions, revocation, and security evidence.
- Provide authoritative logout and revocation semantics stronger than client-state clearing.
- Prevent browser persistence of bearer Authentication Secrets.
- Preserve server-side contextual Authorization by owning Domains and Use Cases.
- Prevent renewal, retries, races, or stale privilege from restoring withdrawn access.
- Apply existing cookie, CSRF, CORS, Sensitive Data, audit, and abuse-resistance requirements.
- Support multiple independently revocable Customer Sessions without inventing a device limit.
- Preserve stronger privileged-identity controls and mandatory MFA without selecting an MFA mechanism.
- Remain compatible with a future separately governed OIDC/OAuth2 Identity Provider.
- Avoid prematurely selecting Session storage, dependencies, topology, APIs, schemas, or numerical values.
- Preserve separate DEC-0001 dependency admission and executable implementation.

## Decision

The initial first-party browser Authentication architecture SHALL use an Identity-owned authoritative server-side Session with a protected browser cookie.

The browser SHALL NOT directly own bearer Access Tokens or Refresh Tokens for the normal first-party browser Authentication flow. The browser credential represents or references authoritative server-side Session state; browser-held credential state does not become authoritative Identity, Session, or Authorization truth.

This is a BFF-style Session-cookie trust boundary. “BFF-style” describes credential custody and trust semantics only. It does not create or authorize a new deployable BFF service, process, or topology. The existing Spring Boot backend may implement the browser-facing boundary subject to later dependency admission and implementation governance.

This Accepted decision establishes Architecture direction only. It does not claim dependency admission, configuration, executable implementation, or physical Session storage.

## Browser Credential

The browser Session credential SHALL use a minimally scoped cookie with:

- `HttpOnly`;
- `Secure`; and
- the strictest workable `SameSite` policy.

The exact cookie name, `Path`, `Domain`, `SameSite` value, expiry, rotation interval, and other concrete configuration remain implementation or configuration decisions subject to existing security governance. This ADR selects no numerical value.

## Browser Storage Boundary

The first-party browser flow SHALL NOT place Session secrets, Access Tokens, Refresh Tokens, or equivalent bearer credentials in persistent browser storage such as `localStorage` or IndexedDB.

This prohibition does not apply to ordinary non-secret frontend state. Frontend state remains non-authoritative and must preserve existing Principal, Session, Customer, and Resource isolation requirements.

## CSRF and CORS

Because the Accepted browser Authentication model uses a cookie credential:

- state-changing authenticated browser requests MUST use the repository-approved CSRF protection model;
- CORS MUST remain explicitly configured;
- CORS MUST NOT be treated as CSRF protection; and
- credentialed browser requests MUST NOT use wildcard origins.

This ADR does not select a CSRF token name, Endpoint, header name, cookie name, Angular integration, or other concrete frontend or backend implementation.

## Authoritative Session State

Identity SHALL own authoritative server-side Session state sufficient to determine:

- Session identity;
- associated Principal;
- current validity;
- expiry;
- renewal eligibility;
- revocation or termination;
- compromise or security invalidation; and
- applicable security context needed to prevent withdrawn access from being restored.

The decision does not define a database schema and does not select PostgreSQL, Redis, Spring Session JDBC, Spring Session Redis, in-memory storage, or another physical Session store. Physical Session persistence and storage remain separate implementation or technology decisions where required by governance.

## Renewal

The initial browser architecture SHALL use Session renewal semantics and SHALL NOT require a browser-held Refresh Token.

Renewal MUST NOT restore a revoked, terminated, compromised, disabled, stale-privilege, or otherwise invalid Session. This ADR does not select renewal intervals, absolute expiry, inactivity expiry, rotation intervals, propagation timing, or distributed consistency guarantees.

## Logout

Ordinary user sign-out SHALL terminate the current authoritative Session server-side. Clearing frontend or browser state alone SHALL NOT constitute logout. After termination, the Session credential MUST NOT restore authenticated access.

Duplicate or retried logout must remain safe and must not fabricate successful termination when authoritative state is uncertain.

## Concurrent Devices and Sessions

Customers MAY maintain multiple concurrent Sessions. Each Session MUST be independently identifiable and revocable.

Ordinary sign-out terminates the current Session unless a separately governed action explicitly requests broader termination. This ADR does not establish a maximum Session or device count, device identity model, trusted-device policy, or Session-management user interface.

## Security-Triggered Invalidation

Identity MUST be capable of invalidating all affected Sessions when required by governed security state, including:

- password reset where existing security governance requires invalidation;
- confirmed or governed compromise handling;
- account disablement;
- material privilege changes; and
- other existing security requirements requiring withdrawal of access.

Concurrent renewal, retries, races, or reordered processing MUST NOT recreate withdrawn access. The decision selects no invalidation propagation interval or distributed consistency mechanism.

## Customer and Privileged Identity Boundary

Customer and privileged human identities SHALL use the same fundamental authoritative server-side Session architecture unless separately governed.

Privileged identities retain stronger existing controls, including:

- mandatory MFA where `SECURITY-STANDARDS.md` already requires it;
- shorter risk-appropriate Session limits;
- reauthentication for high-impact actions where already governed; and
- stronger auditing and revocation requirements.

Privileged MFA is already mandatory under `SECURITY-STANDARDS.md`. The unresolved matters are its concrete provider, factor, protocol, exact configuration, and any broader Customer MFA policy. This ADR does not select them or an exact privileged Session lifetime.

## Identity Provider Boundary

ADR-0019 does not select an external Identity Provider. The Accepted Session architecture SHALL remain compatible with a future separately governed OIDC/OAuth2 Identity Provider.

If a future provider supplies upstream Access Tokens, ID Tokens, or Refresh Tokens for browser Authentication, those credentials MUST remain behind the trusted server-side BFF-style boundary unless future governance explicitly changes the browser credential architecture.

This ADR does not select Azure AD, Microsoft Entra ID, Auth0, Keycloak, Cognito, or another provider product.

## Bearer-Token Boundary

JWT versus opaque Access Token is not selected for the initial first-party browser Authentication flow because that flow does not use browser-owned bearer Access Tokens.

This decision does not prohibit bearer credentials for separately governed machine, API, integration, or future superseding architecture boundaries.

## Service-to-Service Authentication

Concrete service-to-service credential and token strategy remains deferred to separate governance. Existing requirements remain in force for:

- Service Principals;
- least privilege;
- Environment isolation;
- credential lifecycle and revocation;
- protected transport;
- minimal Scope; and
- caller-specific telemetry.

Browser Session cookies MUST NOT silently become the service-to-service Authentication mechanism.

## Required Security Properties

The architecture must preserve:

- Session fixation resistance;
- Session identifier change after successful Authentication and after privilege elevation as required by `SECURITY-STANDARDS.md`, without treating identifier rotation as the complete Session-fixation defense;
- secure Session creation only after successful Authentication;
- authoritative server-side validity;
- revocation and termination;
- replay-resistant renewal behavior;
- stale-privilege prevention;
- protected credential transport;
- CSRF protection for authenticated state changes;
- no persistent browser storage of bearer Secrets;
- credential redaction from URLs, logs, telemetry, analytics, and errors;
- least privilege;
- applicable security Audit Records; and
- inherited brute-force, enumeration, credential-stuffing, tampering, fixation, replay, and automation-abuse protections.

This ADR selects no cryptographic algorithm, rate limit, lockout threshold, Session lifetime, or operational numerical target.

`HttpOnly` cookie custody reduces direct JavaScript extraction of the Session credential, but it does not eliminate XSS Risk. Malicious same-origin script can still attempt authenticated actions through the victim's active browser context. `HttpOnly` cookie custody therefore does not replace XSS prevention, output encoding, Content Security Policy, or other applicable browser-security controls already governed by repository security standards. This ADR introduces no new Content Security Policy design or frontend implementation detail.

## Authorization Boundary

An authenticated Session establishes trusted Principal context. It does not authorize every Resource, property, action, association, or Domain state.

Contextual Authorization remains server-side with the owning Domain and Use Case. Role, Permission, Claims, Scope, frontend visibility, route access, identifiers, Authentication success, or Session possession alone MUST NOT replace contextual Authorization.

ADR-0019 does not resolve Product Decision 23 and does not create a Role, Permission, hierarchy, assignment, or access-matrix model.

## Failure and Concurrency Semantics

- Invalid, expired, revoked, or terminated Sessions MUST fail closed.
- Uncertain Session validity MUST NOT become authenticated success.
- Unavailable authoritative Session state MUST fail safely under existing security requirements.
- Concurrent renewal and revocation MUST preserve revocation.
- Duplicate or retried logout MUST remain safe.
- Stale privilege state MUST NOT regain access through renewal.
- Reordered or delayed work MUST NOT overwrite newer accepted Session or revocation truth.

This ADR does not define HTTP status codes, API routes, DTOs, retry counts, locking, transaction isolation, storage consistency, or concrete failure representations.

## Data, Privacy, Audit, and Observability

Session state is security-sensitive and must follow existing least-privilege, Sensitive Data, retention, access-control, Environment-isolation, and disclosure requirements.

Secrets and credential material must not appear in URLs, logs, analytics, telemetry, error responses, client bundles, or support evidence. Observability must distinguish useful Authentication, Session, renewal, logout, revocation, invalidation, denial, abuse, failure, and uncertainty outcomes without exposing credential material or protected Identity existence.

Applicable Authentication, recovery, revocation, privileged access, credential, compromise, and other security-sensitive events require governed tamper-resistant Audit Records distinct from ordinary Logs.

This ADR defines no concrete Session storage schema, telemetry schema, Audit Record schema, or retention duration.

## Operational and Scaling Consequences

Authoritative server-side Session state introduces material availability, consistency, capacity, recovery, and horizontal-scaling responsibilities. Horizontal scaling MUST preserve authoritative Session validity, independent revocation, security-triggered invalidation, and stale-privilege prevention.

This ADR does not select sticky Sessions, PostgreSQL Session storage, Redis, Spring Session JDBC, Spring Session Redis, cache topology, replication topology, failover design, or an availability target. A concrete Session-state storage mechanism must be selected and admitted before implementation if not already governed through the applicable process.

## Dependency and Implementation Boundary

Accepted DEC-0001 continues to govern Java 21, Spring Boot 3.5.16, Gradle 8.14.5, dependency alignment, locking, verification, reproducibility, and implementation admission.

This decision does not claim that the repository currently contains or implements:

- Spring Security;
- Spring Session;
- OAuth2 Resource Server;
- OAuth2 Client;
- JOSE or JWT support;
- Redis;
- an Identity Provider SDK; or
- Session persistence.

Acceptance of ADR-0019 establishes Architecture direction only. Actual dependency admission, configuration, executable implementation, tests, Session storage, schemas, migrations, and operational deployment remain separate DEC-0001-governed or otherwise applicable changes.

## Compatibility, Migration, and Reversibility

Accepting ADR-0019 does not migrate an existing Authentication implementation because no Session or token implementation currently exists. Acceptance establishes Architecture direction only; dependency admission, implementation, rollout, and concrete Session-state storage remain separately governed. Compatibility with a future separately governed OIDC/OAuth2 Identity Provider remains preserved through the server-side credential-custody boundary.

Future replacement of the accepted browser Authentication architecture requires a superseding governed decision and a safe migration or transition for affected authoritative Session state and browser credential state. Active Sessions must be safely terminated, migrated, or otherwise reconciled according to the superseding architecture. A transition, rollback, or replacement must not preserve or silently reactivate access that has been revoked, terminated, compromised, disabled, or invalidated through privilege change.

The architecture is reversible through governance, but its exit cost increases once active Session state and browser clients depend on the model. Replacement therefore requires coordinated migration, termination, or reconciliation rather than an ungoverned implementation swap. This ADR selects no migration date, rollout phase, TTL, schema, provider, topology, or migration tooling.

## Consequences

### Positive Consequences

- Identity has direct authoritative Session validity and revocation state.
- Logout has strong server-side termination semantics.
- The browser does not persist bearer Access Tokens or Refresh Tokens.
- Stale privilege and compromise invalidation can operate against authoritative Session state.
- Multiple Customer Sessions can be independently identified and revoked.
- The architecture remains compatible with a future external OIDC/OAuth2 Identity Provider whose credentials remain server-side.
- The initial browser architecture avoids premature JWT, opaque-token introspection, and Refresh Token family complexity.
- Contextual Authorization remains separate from Authentication and Session possession.

### Negative Consequences and Trade-offs

- Server-side Session-state availability becomes operationally significant.
- Horizontal scaling requires a governed shared or otherwise consistent Session-state approach.
- Cookie Authentication requires robust CSRF protection.
- A Session storage technology and dependency set still require selection and admission.
- Session-state failure and uncertainty must fail safely and may affect authenticated availability.
- External Identity Provider integration remains separately governed.
- Machine and service-to-service credential architecture remains unresolved.

## Alternatives Considered

### A. Server-Side Session with Protected Browser Cookie — Selected

Benefits include direct authoritative revocation, server-side logout, no persistent browser bearer token, and straightforward stale-privilege invalidation. Costs include operationally significant Session state, CSRF requirements, and the need for a governed scalable Session store. This is selected for the initial first-party browser architecture because it satisfies current requirements without introducing browser-owned bearer-token and Refresh Token family complexity.

### B. Short-Lived JWT Access Token with Refresh Token

Benefits include local access-token validation and established ecosystem support. Costs include key and Claims governance, browser credential custody, refresh-family state, replay detection, revocation propagation, logout complexity, and stale-privilege handling. It is technically viable but is not selected for the initial browser architecture. JWT may remain viable for separately governed machine or API boundaries or a future superseding decision.

### C. Opaque Access Token with Refresh Token and Introspection

Benefits include central token authority and direct revocation through authoritative token state. Costs include introspection availability and latency, token-state operations, browser bearer-token custody, refresh replay protection, and another service or authority boundary. It is technically viable but is not selected for the initial browser architecture. Opaque credentials may remain viable for separately governed machine or API boundaries.

### D. External OIDC/OAuth2 Identity Provider with Server-Side/BFF Custody

Benefits include standards-based federation and compatibility with externally managed Authentication capabilities. Costs include provider selection, provider availability, Claims mapping, redirect and issuer governance, upstream logout/revocation integration, and provider-token custody. The selected BFF-style boundary remains compatible with this alternative, but no external provider is selected by ADR-0019.

### E. Browser-Owned Bearer-Token Architecture

Benefits include conventional bearer API interaction and reduced dependence on cookie-based browser Sessions. Costs include browser token custody, XSS theft exposure, persistent-storage restrictions, refresh handling, revocation, logout, rotation, and replay complexity. It is technically viable with appropriate controls but is not selected for the initial first-party browser flow.

## Explicitly Unresolved and Out of Scope

ADR-0019 does not select, define, or authorize:

- exact Session or inactivity lifetime;
- renewal or rotation interval;
- cookie name, exact `SameSite` value, `Domain`, or `Path`;
- Session identifier format;
- cryptographic algorithms;
- Session storage technology or database/cache schema;
- Redis;
- Spring Session implementation choice;
- exact Spring Security dependencies;
- exact MFA factor, provider, protocol, configuration, or broader Customer policy;
- Identity Provider product;
- OAuth2/OIDC provider selection;
- JWT or opaque tokens for the initial browser flow;
- token Claims schema or concrete Scope design;
- API routes, HTTP methods, DTO schemas, or HTTP status mapping;
- exact lockout, rate, timeout, propagation, availability, or other numerical thresholds;
- concrete administrative Session-management UI or workflow;
- Product Decision 23 or another Role/Permission decision;
- service-to-service credential strategy;
- deployment topology, sticky Sessions, replication, or failover;
- implementation, configuration, dependencies, schemas, migrations, or tests; or
- unrelated Product, Domain, API, infrastructure, or provider decisions.

## Governance Relationships

ADR-0019 is subordinate to and preserves:

- `.ai/core/AGENTS.md`;
- `.ai/core/GLOSSARY.md`;
- `.ai/core/PRODUCT.md`;
- `.ai/core/ARCHITECTURE.md` as synchronized with this Accepted ADR;
- `.ai/core/SECURITY-STANDARDS.md`;
- `.ai/core/TESTING-STANDARDS.md`;
- `.ai/core/CODING-STANDARDS.md`;
- `.ai/core/DECISIONS.md`;
- `.ai/backend/SPRING.md`;
- `.ai/backend/API.md` where applicable;
- the Approved Identity Domain Specification;
- the Approved Identity and Access Backend Specification; and
- Accepted DEC-0001.

This ADR supersedes none of those sources. Only the explicitly synchronized portions of current Architecture and Spring standards changed; all unaffected authority remains intact.

## Completed Governance Reviews

The acceptance-readiness governance review completed successfully and confirmed:

- Architecture approval of the initial browser Session boundary and operational consequences;
- Identity ownership approval of authoritative Session, renewal, revocation, and invalidation semantics;
- Security approval of cookie custody, CSRF, CORS, fixation, replay, logout, MFA, Sensitive Data, and Audit Record boundaries;
- affected Customer and Administration ownership review of sign-out, concurrent Session, privileged-access, and non-authority boundaries;
- Frontend review of browser credential custody and persistent-storage prohibitions;
- Engineering and dependency-governance review under DEC-0001;
- Testing review of failure, concurrency, revocation, renewal, CSRF, abuse, and boundary evidence;
- Operations review of authoritative Session-state availability and scaling consequences; and
- Documentation review of canonical synchronization and unresolved detail.

The review found no unresolved acceptance blocker. This record does not fabricate named reviewers, meetings, tickets, signatures, implementation evidence, or test execution.

## Acceptance Conditions and Synchronization

ADR-0019 is Accepted and authoritative within its governed Architecture scope. Acceptance does not authorize dependency admission, Session storage, configuration, or implementation.

Acceptance synchronized exactly:

1. `specifications/adr/ADR-0019-authentication-session-token-strategy.md`;
2. `.ai/core/DECISIONS.md`;
3. `.ai/core/ARCHITECTURE.md`; and
4. `.ai/backend/SPRING.md`.

Acceptance synchronization:

- promoted ADR-0019 through the governed lifecycle and recorded completed review without fabricated evidence;
- indexed ADR-0019 as Accepted in `DECISIONS.md`;
- replaced the unresolved Session/token strategy in Architecture;
- removed the corresponding Open Architecture Decision;
- preserved trusted server-side contextual Authorization;
- recorded that privileged MFA is already mandatory while provider, factor, and protocol remain unresolved;
- synchronized the affected Spring Security, CSRF, CORS, and Session boundaries; and
- preserved separate DEC-0001 dependency admission and executable implementation.

Current evidence does not pre-authorize acceptance changes to `PRODUCT.md`, `SECURITY-STANDARDS.md`, `GLOSSARY.md`, `API.md`, Identity specifications, frontend specifications, build files, or implementation files.

## Validation Criteria

The Accepted record and synchronized governance verify:

1. metadata is `1.0.0 Accepted`, owner `Architecture`, last updated `2026-09-28`, and `authoritative: true` within this ADR's governed Architecture scope;
2. Accepted-state authority is bounded to Architecture direction and no implementation is falsely claimed;
3. the initial browser architecture is an Identity-owned authoritative server-side Session;
4. the browser credential is a minimally scoped protected cookie;
5. the initial browser flow does not make the browser owner of Access Tokens or Refresh Tokens;
6. authoritative Session identity, Principal association, validity, expiry, renewal eligibility, revocation, termination, and invalidation remain server-side;
7. ordinary logout terminates the current Session server-side and client clearing alone is insufficient;
8. Customers may hold multiple independently identifiable and revocable Sessions without an invented limit;
9. governed security events can invalidate all affected Sessions without races or renewal restoring access;
10. mandatory privileged MFA remains preserved without selecting provider, factor, protocol, or broader Customer policy;
11. Authentication and Session possession do not replace contextual Authorization by owning Domains and Use Cases;
12. cookie Authentication requires CSRF protection, CORS remains explicit, and wildcard credentialed origins remain prohibited;
13. service-to-service credential strategy remains separately governed;
14. a future external Identity Provider remains possible but no provider is selected;
15. no physical Session store, schema, cache, replication, or topology is selected;
16. no Spring Security, Spring Session, OAuth2, JOSE/JWT, Redis, provider SDK, or other dependency is admitted;
17. no executable implementation or completed implementation test is claimed;
18. every listed implementation and policy detail remains unresolved;
19. acceptance synchronization changed exactly ADR-0019, `DECISIONS.md`, `ARCHITECTURE.md`, and `SPRING.md`;
20. Session identifiers are required to change after successful Authentication and after privilege elevation without treating identifier rotation as the complete Session-fixation defense;
21. acceptance migrates no existing Authentication implementation, establishes Architecture direction only, preserves separately governed dependency admission, implementation, rollout, and Session storage, and requires any future replacement to use superseding governance and safely migrate, terminate, or reconcile affected Session and browser credential state without restoring withdrawn access;
22. `HttpOnly` cookie custody is recognized as reducing direct JavaScript credential extraction without eliminating XSS Risk or replacing applicable browser-security controls; and
23. no unrelated Product behavior, API Contract, Identity Provider, MFA mechanism, Role/Permission matrix, service credential, or infrastructure choice is invented.

## Supersedes

None.

## Superseded By

None.

## Related Documents

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [TESTING-STANDARDS.md](../../.ai/core/TESTING-STANDARDS.md)
- [CODING-STANDARDS.md](../../.ai/core/CODING-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [API.md](../../.ai/backend/API.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-28 | Accepted | Accepted the Identity-owned authoritative server-side Session and protected browser-cookie credential-custody architecture following completed governance review and canonical Architecture and Spring synchronization, while preserving contextual Authorization, mandatory privileged MFA, unresolved implementation choices, and separate DEC-0001 dependency admission. |
| 0.1.0 | 2026-09-28 | Proposed | Proposed an Identity-owned authoritative server-side Session with a protected browser cookie for initial first-party browser Authentication while preserving contextual Authorization, privileged MFA requirements, implementation neutrality, provider deferral, and separate dependency admission. |
