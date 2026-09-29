# ADR-0021 — Customer Authentication Authority Strategy

## Identifier

ADR-0021

## Title

Customer Authentication Authority Strategy

## Version

1.0.0

## Status

Accepted

## Date

2026-09-29

## Last Updated

2026-09-29

## Owner

Architecture

## Authoritative

true

## Scope

customer-authentication-authority

## Context

The Approved Identity Domain and BIDN assign Customer Authentication, credential and Authentication evidence security, local Principal establishment, and Session lifecycle to Identity. The Approved Customer Domain and BCUS retain Customer and Account business truth, registration outcomes, profile, and Customer-owned lifecycle authority. Authentication, Identity, Principal, Customer, Account, and Session remain distinct canonical concepts.

Accepted ADR-0019 establishes the initial first-party browser boundary as an Identity-owned authoritative server-side Session with protected browser-cookie custody. Accepted ADR-0020 establishes Spring Session JDBC backed by the governed application PostgreSQL database as the authoritative physical store for that local Session state. Neither decision establishes whether primary Customer Authentication evidence is verified locally by Identity or delegated to an external Identity Provider.

The executable backend now contains the governed Spring Security and Spring Session foundation, but it contains no Customer credential mechanism, Identity Provider integration, Customer Authentication provider, login or registration Contract, Customer/Account persistence, or credential persistence. Selecting a credential technology, schema, provider integration, or externally observable Contract before deciding the Authentication authority boundary would prematurely assume one of materially different architectures.

The repository requires governed Customer registration and sign-in while deliberately leaving credential type, login identifier, email-verification policy, recovery mechanism, Customer MFA policy, provider selection, and concrete APIs unresolved. This Accepted ADR selects only the initial Customer Authentication authority model needed to order those downstream decisions.

## Decision Drivers

- Preserve Identity ownership of Customer Authentication, trusted evidence, local Principal establishment, and Session lifecycle.
- Preserve Customer ownership of Customer and Account business truth and registration outcomes.
- Preserve ADR-0019's server-side Session and protected browser-cookie boundary.
- Preserve ADR-0020's PostgreSQL-backed Spring Session JDBC authority for local application Sessions.
- Advance from the existing executable Identity foundation without inventing an external provider, protocol, redirect, callback, Claims mapping, or provider-token boundary.
- Avoid adding provider availability, data-processing, lock-in, migration, support, and incident dependencies before a provider decision exists.
- Keep credential mechanism, identifier, recovery, MFA, Product policy, API, persistence, and dependency decisions separately reviewable.
- Minimize the initial number of authoritative systems involved in establishing a Customer Principal.
- Preserve failure-closed Authentication, enumeration resistance, abuse resistance, Sensitive Data protection, evidence integrity, auditability, and explicit uncertainty.
- Keep future SSO, federation, and external Identity Provider adoption possible through superseding or extending governance.
- Preserve DEC-0001 dependency admission and executable validation as later implementation work.

## Decision

Identity-owned local Customer Authentication authority SHALL be the initial Customer Authentication authority model.

Identity SHALL own verification of the subsequently governed local Customer Authentication credential and SHALL establish the local Principal only from accepted Identity-owned Authentication evidence. This decision selects the authority location, not the credential type, identifier, verification algorithm, representation, or storage design.

An external Identity Provider SHALL NOT be the primary Customer Authentication authority for the initial implementation. Future external Identity Provider, SSO, or federation adoption remains possible only through separate Architecture governance that preserves or safely migrates the accepted Identity, Principal, Customer, Account, Session, recovery, revocation, security, privacy, and failure boundaries.

This Accepted Architecture direction does not itself authorize implementation, dependency admission, credential creation, persistence, or externally observable Authentication behavior. Those remain separately governed.

## Credential Authority Boundary

Identity SHALL own the security boundary for any selected local Customer credential, including accepted establishment, verification, replacement, compromise, invalidation, and recovery outcomes. Customer and Account records SHALL NOT store or become authoritative for Authentication credentials.

This decision does not select password, passkey, username, email address, phone number, cryptographic key, or another credential or identifier. It does not establish whether a password exists and therefore does not select password hashing technology or configuration.

Credential material and Authentication evidence remain purpose-limited Sensitive Data or Secrets as applicable. They MUST remain excluded from logs, analytics, URLs, unsafe errors, frontend persistence, Customer profiles, and unrelated Domain Contracts.

## Authentication Evidence and Principal Establishment

Identity SHALL validate the selected local Authentication evidence and establish the local Principal only after an accepted Authentication outcome. Request submission, identifier possession, Customer or Account existence, frontend state, message delivery, redirect, challenge initiation, or partial processing SHALL NOT prove Authentication.

Authentication establishes trusted Principal context but SHALL NOT independently authorize every Resource, property, association, action, or Domain state. Contextual Authorization remains with the owning Domain or Use Case.

No provider assertion, Claims mapping, issuer, audience, Scope, or external subject mapping is created by this decision. Those concerns become applicable only if later Architecture governance introduces an external Identity Provider.

## Customer and Account Boundary

Customer retains authoritative Customer and Account business identity, creation, profile, Address, Preference, Consent, association, and applicable lifecycle truth. Identity SHALL NOT create or rewrite Customer or Account business truth merely because local Authentication succeeds, fails, or changes.

Where later governed registration coordinates Customer and Identity outcomes, each owner SHALL preserve its own result, failure, uncertainty, history, and recovery semantics. A credential, Identity, Principal, Account, Customer, and Session SHALL NOT be collapsed into one record or lifecycle.

This decision does not establish Account representation, persistence, lifecycle states, identifier format, registration transaction design, or Customer-to-Identity association mechanism.

## ADR-0019 Browser Session Boundary

Successful local Customer Authentication evidence SHALL enter the existing ADR-0019 boundary through Identity. Identity may establish the governed local Principal and authoritative server-side Session only after Authentication succeeds.

The normal first-party browser flow SHALL continue to receive only the governed protected Session cookie. The browser SHALL NOT directly own bearer Access Tokens or Refresh Tokens for that flow, and local Authentication does not convert the browser into credential, Identity, Principal, Session, or Authorization authority.

ADR-0019 remains authoritative for cookie custody, CSRF, CORS, fixation resistance, renewal, logout, independent Session revocation, concurrent Sessions, security-triggered invalidation, failure-closed behavior, and unresolved Session lifetimes and cookie configuration.

## ADR-0020 Session Store Boundary

Spring Session JDBC backed by the governed application PostgreSQL database remains authoritative for local application Session state after successful Authentication. Local credential verification does not move credential authority into Spring Session or store credentials in Session state.

ADR-0020 remains authoritative for Identity-owned Session persistence, Flyway-managed Session objects, least privilege, shared-state behavior, and the prohibition on application-local production Session authority. This decision creates no Session schema or persistence change.

## Recovery Boundary

Identity SHALL own local credential-recovery security outcomes if and when a concrete local credential and recovery mechanism are separately governed. Recovery MUST require governed requester verification, preserve Identity and Customer separation, resist enumeration, distinguish failure and uncertainty, and MUST NOT silently create or substitute an Identity, Customer, Account, Principal, credential, or Session.

This decision does not select a recovery channel, factor, token, provider, expiry, support process, notification Contract, or Account-recovery workflow. Product and support policies remain with their governing owners.

## MFA Boundary

Mandatory privileged MFA under `SECURITY-STANDARDS.md` remains in force and is not weakened by selection of local Customer Authentication authority. The broader Customer MFA policy and every MFA factor, provider, protocol, challenge, fallback, recovery, and configuration choice remain unresolved.

Local Customer Authentication MUST remain capable of later governed MFA without treating challenge initiation, delivery, frontend state, or incomplete evidence as Authentication success.

## Failure, Uncertainty, and Abuse Boundaries

Invalid, unavailable, stale, conflicting, replayed, or uncertain local Authentication evidence MUST NOT establish a Principal or Session. Dependency failure, timeout, partial completion, duplicate work, abuse controls, and uncertainty MUST remain distinguishable where handling or recovery differs and MUST fail safely without disclosing protected Customer, Account, Identity, or credential existence.

Retries MUST NOT create duplicate Identity, Customer, Account, credential, Principal, or Session effects or restore withdrawn access. This decision selects no retry policy, lockout rule, rate, threshold, timeout, idempotency-key design, fraud mechanism, or reconciliation implementation.

## Security and Privacy Consequences

Local authority keeps primary Customer Authentication evidence within the repository-controlled Identity boundary and avoids transmitting that evidence to a newly selected external provider. It also makes the repository responsible for securely implementing and operating whichever local credential mechanism is later accepted.

The selected direction therefore increases direct responsibility for credential protection, verification correctness, abuse resistance, compromise response, recovery, secure migration, monitoring, Audit Records, maintenance, and incident handling compared with delegating primary verification to an external provider.

Data collection MUST remain minimized to the later Approved credential and Authentication purpose. No Customer profile field, email address, phone number, device signal, or provider attribute is made mandatory by this decision.

## Operational Consequences

Initial Customer Authentication availability will depend on repository-operated Identity, credential-verification, database, and Session capabilities rather than on a new external Identity Provider. This removes an initial provider availability, quota, redirect, callback, support, and vendor-escalation dependency, but transfers operational responsibility for local credential security and recovery to the repository's owners.

Monitoring and Audit Records must distinguish Authentication intent, accepted success, denial, abuse, failure, uncertainty, credential change, recovery, Principal establishment, and Session establishment without exposing credentials, Secrets, protected existence, or unnecessary PII.

This decision does not select hosting, topology, capacity, availability, recovery objectives, on-call policy, support channel, numerical target, or production escalation process.

## Dependency and Implementation Boundary

This Accepted ADR admits no dependency and claims no executable Authentication implementation. DEC-0001 continues to govern admission, locking, verification, compatibility, licensing, security evidence, build validation, and maintenance of any later credential, cryptographic, security, or testing dependency.

Separately reviewed work may govern and implement a credential mechanism, Authentication Port and Adapter boundaries, secure representation, Customer/Account association, recovery behavior, MFA integration, and external Contracts. Framework, cryptographic, persistence, or provider types MUST remain outside Domain and Application APIs according to existing architecture.

The currently admitted Spring Security and Session dependencies do not themselves select a credential mechanism or authorize Authentication behavior.

## Migration, Compatibility, and Reversibility

No production Customer Authentication population or credential store currently requires migration. This Accepted ADR establishes the initial authority direction only.

A future move to an external Identity Provider, federation, or multiple authority model requires separate Architecture governance. It must address Identity and Customer association, credential retirement or migration, account linking, Authentication continuity, active Sessions, recovery, MFA, evidence provenance, privacy, provider failure, rollback, and prevention of duplicate or unauthorized identities.

Migration, rollback, coexistence, or reconciliation MUST NOT silently authenticate a Customer, merge identities without authority, preserve compromised access, restore revoked Sessions, or represent uncertain provider or local evidence as success. This decision selects no migration date, dual-running design, account-linking algorithm, import format, or transition period.

## Consequences

### Positive Consequences

- Establishes one initial Customer Authentication authority instead of competing local and provider truth.
- Aligns Principal establishment with the existing Identity-owned backend and Session boundary.
- Avoids an immediate provider, protocol, callback, Claims, token-custody, and vendor-operations dependency.
- Enables credential-mechanism, identifier, persistence, recovery, MFA, registration, and Contract governance to proceed in a defined order.
- Preserves future SSO and external-provider evolution through explicit migration governance.

### Negative Consequences and Trade-offs

- The repository assumes direct responsibility for the security and operation of the later selected local credential mechanism.
- Credential breach exposure, secure verification, abuse resistance, recovery, monitoring, maintenance, and incident response remain repository responsibilities.
- Selecting a credential type, identifier, storage representation, recovery mechanism, and applicable cryptographic technology still requires downstream governance.
- Later external-provider adoption may require Identity linking, credential migration or retirement, and coordinated Customer communication and recovery.
- Acceptance alone does not make Customer Authentication executable.

## Alternatives Considered

### A. Identity-Owned Local Customer Authentication Authority — Selected

This alternative keeps primary verification of the later selected Customer credential within Identity. It aligns with existing Identity ownership, local Principal establishment, ADR-0019's local server-side Session boundary, ADR-0020's local Session authority, and the current provider-free executable foundation.

It avoids introducing provider availability, trust, privacy, token custody, redirect/callback, Claims mapping, vendor support, and exit dependencies before the repository has requirements or evidence to select a provider. Its cost is direct repository responsibility for secure credential verification, recovery, abuse resistance, maintenance, and incident response. It is selected for the initial implementation without selecting password or any other credential mechanism.

### B. External Identity Provider as Primary Customer Authentication Authority — Deferred

This alternative could reduce direct handling of primary credentials and may provide mature federation, recovery, abuse controls, or MFA capabilities. It can remain compatible with ADR-0019 when provider credentials stay server-side and trusted evidence is translated into the local Identity, Principal, and Session boundary.

It is not selected initially because no provider, protocol configuration, subject/Claims mapping, data-processing boundary, recovery allocation, availability requirement, cost model, support model, or migration evidence is governed. Selecting delegation now would make executable Customer Authentication dependent on another unresolved provider decision and introduce an external authoritative dependency before its Contract and exit strategy exist.

### C. Hybrid or Multiple Primary Authorities — Rejected for the Initial Implementation

Supporting local and external authorities concurrently could enable migration, federation, or Customer choice later. Initially it would require account linking, duplicate-Identity prevention, authority precedence, recovery ownership, credential coexistence, evidence reconciliation, provider and local failure handling, and migration semantics before either single path exists.

It is rejected for the initial implementation because it multiplies security, privacy, testing, support, and operational boundaries and could create competing Authentication truth. Rejection does not prohibit future separately governed evolution after one authority model and its migration requirements are established.

## Explicit Non-Decisions and Unresolved Matters

ADR-0021 does not select, define, or authorize:

- password, passkey, cryptographic key, one-time code, or another credential type;
- username, email address, phone number, provider subject, or another login identifier;
- password hashing library, algorithm, parameters, upgrade policy, or password policy;
- external Identity Provider, vendor, issuer, SDK, or service;
- OAuth2, OIDC, SAML, Claims, Scope, callback, redirect, or provider-token configuration;
- credential, provider-subject, Identity, Customer, Account, or association persistence schema;
- Customer or Account representation, identifier, lifecycle, table, column, index, constraint, or migration;
- Product Decision 5, Customer email-verification requirements;
- Product Decision 23, administrative role and permission matrix;
- recovery channel, mechanism, factor, token, provider, expiry, or support workflow;
- MFA factor, provider, protocol, challenge, fallback, recovery, or Customer policy;
- login, registration, recovery, verification, callback, or related route, method, status, DTO, field, or externally observable Contract;
- Session absolute lifetime, inactivity lifetime, renewal threshold, privileged lifetime, or propagation interval;
- cookie name, `Domain`, `Path`, exact `SameSite` value, expiry, or rotation interval;
- service-to-service Authentication or Service Principal credential strategy;
- hosting, infrastructure, deployment, provider topology, replication, or failover;
- fraud policy, lockout rule, retry count, rate, timeout, threshold, SLA, SLO, or other numerical value;
- dependency admission, build change, configuration, implementation, schema, migration, Endpoint, or completed test; or
- unrelated Product, Domain, Architecture, API, data, provider, or operational policy.

## Downstream Governance Enabled

With ADR-0021 Accepted and canonically synchronized, downstream governance may proceed in this order without being resolved here:

1. select the initial local Customer credential mechanism;
2. govern the login identifier and applicable uniqueness, concealment, and association semantics;
3. resolve Product Decision 5 where registration completion depends on email verification;
4. if passwords are selected, govern password hashing technology and configuration;
5. govern credential persistence, migration, least privilege, and compatibility;
6. govern Customer/Account persistence and registration coordination with Identity;
7. govern recovery mechanism and evidence;
8. govern Customer MFA policy and mechanism where applicable;
9. approve concrete login, registration, recovery, verification, and Session-establishment Contracts; and
10. admit dependencies and deliver implementation through DEC-0001 and normal specification, security, testing, and review controls.

This sequence describes eligibility and dependency order, not automatic approval or implementation authorization.

## Governance Relationships

ADR-0021 is subordinate to and preserves:

- `.ai/core/AGENTS.md`;
- `.ai/core/GLOSSARY.md`;
- `.ai/core/PRODUCT.md`;
- `.ai/core/ARCHITECTURE.md` as synchronized with this Accepted ADR;
- `.ai/core/SECURITY-STANDARDS.md`;
- `.ai/core/TESTING-STANDARDS.md`;
- `.ai/core/CODING-STANDARDS.md`;
- `.ai/core/ENGINEERING-PRINCIPLES.md`;
- `.ai/core/DECISIONS.md`;
- `.ai/backend/SPRING.md`;
- `.ai/backend/JAVA.md`;
- `.ai/backend/DATABASE.md`;
- `.ai/backend/POSTGRES.md`;
- `.ai/backend/API.md`;
- the Approved Identity Domain Specification;
- the Approved Identity and Access Backend Specification;
- the Approved Customer Domain Specification;
- the Approved Customer and Account Backend Specification;
- the Approved Storefront Account and Identity Frontend Specification;
- Accepted ADR-0017;
- Accepted ADR-0018;
- Accepted ADR-0019;
- Accepted ADR-0020;
- Accepted DEC-0001; and
- Accepted DEC-0002.

This Accepted ADR supersedes none of those sources. It establishes the initial Customer Authentication authority location but provides no authority to implement a credential mechanism, admit dependencies, create persistence, or establish externally observable Authentication behavior.

## Completed Governance Reviews

Acceptance-readiness governance review confirmed:

- Architecture approval of the Customer Authentication authority and durable trust boundary;
- Identity ownership approval of credential authority, Authentication evidence, Principal establishment, recovery, MFA readiness, and Session handoff;
- Customer ownership approval of Customer, Account, registration, and association non-authority boundaries;
- Security and Privacy approval of credential exposure, abuse resistance, enumeration resistance, failure-closed behavior, Sensitive Data, recovery, audit, monitoring, migration, and incident consequences;
- Product review confirming that Product Decisions 5 and 23 remain unresolved;
- Frontend review confirming that presentation and browser state do not become Authentication authority and no Contract is invented;
- Spring and Engineering review confirming compatibility with ADR-0019, ADR-0020, current executable foundations, and separate DEC-0001 dependency admission;
- Database and persistence review confirming that no credential, Identity, Customer, Account, or association schema is invented;
- API review confirming that no route, method, status, DTO, field, redirect, or callback Contract is established;
- Testing review of the accepted authority, failure, uncertainty, migration, and boundary-verification obligations;
- Operations review of local credential security, availability, recovery, observability, maintenance, support, and incident responsibility; and
- Documentation review of canonical synchronization and unresolved matters.

The governance review completed with no unresolved acceptance blocker. This record does not fabricate named reviewers, meetings, tickets, signatures, implementation evidence, or executable test evidence.

## Acceptance and Canonical Synchronization

ADR-0021 became Accepted after the required governance reviews completed with no unresolved acceptance blocker and confirmed the decision consistent with governing Product, Identity, Customer, Security, Architecture, API, data, and implementation authority.

Controlled acceptance synchronized the following directly affected canonical sources:

1. `specifications/adr/ADR-0021-customer-authentication-authority-strategy.md`;
2. `.ai/core/DECISIONS.md`;
3. `.ai/core/ARCHITECTURE.md`; and
4. `.ai/backend/SPRING.md`.

The final audited synchronization set contained exactly those four files. Synchronization:

- promoted ADR-0021 through the governed Proposed-to-Accepted lifecycle;
- updated ADR-0021's indexed status from Proposed to Accepted in `DECISIONS.md`;
- established Identity-owned local Customer Authentication authority in the canonical Architecture;
- synchronized Spring's Identity Provider neutrality with the accepted initial local authority while preserving future provider governance;
- preserved ADR-0019 and ADR-0020 without changing their Session and browser-custody decisions;
- preserved Customer and Account business authority;
- preserved Product Decisions 5 and 23 and every listed implementation non-decision;
- preserved separate DEC-0001 dependency admission and executable implementation; and
- claimed no credential mechanism, dependency, Contract, schema, migration, configuration, implementation, deployment, or completed executable test.

No acceptance change to `PRODUCT.md`, `SECURITY-STANDARDS.md`, `GLOSSARY.md`, `JAVA.md`, `DATABASE.md`, `POSTGRES.md`, `API.md`, Domain Specifications, Backend Specifications, frontend Specifications, build files, or implementation files was required or authorized.

## Validation Criteria

The Accepted record and canonical synchronization verify:

1. metadata is `1.0.0 Accepted`, owner `Architecture`, last updated `2026-09-29`, and `authoritative: true`;
2. exactly one initial Customer Authentication authority is selected: Identity-owned local Customer Authentication authority;
3. external Identity Provider authority is deferred rather than silently selected or permanently prohibited;
4. Identity owns local credential verification, accepted Authentication evidence, and local Principal establishment without selecting credential details;
5. Customer retains Customer and Account business truth, and no Authentication outcome rewrites that truth;
6. Authentication, Identity, Principal, Customer, Account, and Session remain distinct;
7. ADR-0019's server-side Session, protected browser cookie, BFF-style custody, bearer-token prohibition, revocation, CSRF, and failure boundaries remain unchanged;
8. ADR-0020's Spring Session JDBC and PostgreSQL-backed local Session authority remains unchanged;
9. invalid, unavailable, conflicting, partial, replayed, or uncertain evidence cannot establish a Principal or Session;
10. recovery and MFA responsibility boundaries are stated without selecting a mechanism, provider, factor, protocol, or policy;
11. security, privacy, operational, migration, replacement, and exit consequences are recorded fairly;
12. local authority is not equated with password Authentication and no credential or login identifier is selected;
13. Product Decision 5 remains unresolved and is not replaced by Authentication evidence;
14. Product Decision 23 remains unresolved and separate from ordinary Customer Authentication;
15. no route, method, status, DTO, field, redirect, callback, or other API Contract is invented;
16. no credential, provider-subject, Identity, Customer, Account, or association schema or database object is invented;
17. no dependency is admitted and no implementation or completed implementation validation is claimed;
18. Session lifetimes, cookie details, service credentials, infrastructure, and numerical policies remain unresolved;
19. downstream governance is ordered without being represented as Approved or automatic;
20. the three alternatives are represented fairly, and hybrid authority is rejected only for the initial implementation;
21. the accepted choice remains implementable without contradicting Accepted ADR-0017, ADR-0018, ADR-0019, ADR-0020, DEC-0001, or DEC-0002;
22. acceptance synchronization is limited to ADR-0021, `DECISIONS.md`, `ARCHITECTURE.md`, and `SPRING.md`; and
23. ADR-0021 is registered exactly once as Accepted, the controlled synchronization passes whitespace and diff validation, and no unrelated tracked changes are introduced.

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
- [ENGINEERING-PRINCIPLES.md](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [JAVA.md](../../.ai/backend/JAVA.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [API.md](../../.ai/backend/API.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [Storefront Account and Identity Frontend Specification](../frontend/storefront/account-identity.md)
- [ADR-0017 — PostgreSQL Schema Strategy](ADR-0017-postgresql-schema-strategy.md)
- [ADR-0018 — Persistence Technology](ADR-0018-persistence-technology.md)
- [ADR-0019 — Authentication Session and Token Strategy](ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](ADR-0020-identity-session-store-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0002 — PostgreSQL Release Baseline](../decisions/DEC-0002-postgresql-release-baseline.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-29 | Accepted | Accepted Identity-owned local Customer Authentication authority for the initial implementation while preserving Identity and Customer boundaries, ADR-0019 and ADR-0020 Session authority, Product Decisions 5 and 23, future external-provider evolution, implementation neutrality, and separate downstream governance. |
| 0.1.0 | 2026-09-29 | Proposed | Proposed Identity-owned local Customer Authentication authority for the initial implementation while preserving Identity and Customer boundaries, ADR-0019 and ADR-0020 Session authority, Product Decisions 5 and 23, future external-provider evolution, implementation neutrality, and separate downstream governance. |
