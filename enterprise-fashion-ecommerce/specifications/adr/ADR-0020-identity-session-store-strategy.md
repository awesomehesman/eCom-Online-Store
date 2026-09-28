# ADR-0020 — Identity Session Store Strategy

## Identifier

ADR-0020

## Title

Identity Session Store Strategy

## Version

0.1.0

## Status

Proposed

## Date

2026-09-28

## Last Updated

2026-09-29

## Owner

Architecture

## Authoritative

false

## Scope

identity-session-store-architecture

## Context

Accepted ADR-0019 establishes the initial first-party browser Authentication architecture as an Identity-owned authoritative server-side Session with protected browser-cookie credential custody. It requires authoritative logout, independent Session revocation, security-triggered invalidation, renewal that cannot restore withdrawn access, Session-fixation resistance, CSRF protection, and failure-closed treatment of invalid or uncertain Session authority. It deliberately leaves the physical Session store and Spring Session implementation choice unresolved.

The Approved Architecture requires horizontally scalable backend application instances and externalized state where state is operationally significant. Application-local Session state therefore cannot provide the governed production authority across process restart or multiple application instances.

Accepted DEC-0002 establishes PostgreSQL 18 as the supported database major-version baseline. Accepted ADR-0017 establishes one application PostgreSQL database with a dedicated schema for each persistence-owning Module or Domain boundary. Accepted ADR-0018 establishes Spring Data JDBC as the primary aggregate-persistence mechanism and Spring `JdbcClient` as a narrowly bounded complementary persistence-Adapter mechanism. Flyway remains the sole governed database-migration authority.

The repository already depends on PostgreSQL for governed application persistence. Redis remains optional and may be introduced only through Architecture approval for a justified use case. Introducing Redis solely to store the initial Sessions would add infrastructure, dependency, availability, security, recovery, and operational responsibilities that are not otherwise required by the current executable baseline.

This Proposed ADR selects the production physical store and Spring integration for ADR-0019 Session state. It does not admit dependencies, create a schema or migration, configure Spring Security, or claim implementation.

## Decision Drivers

- Preserve ADR-0019's Identity ownership and authoritative Session semantics.
- Provide shared production Session state across horizontally scaled application instances.
- Preserve authoritative logout, independent revocation, security-wide invalidation, renewal, and fixation resistance across restart, retry, and reordered work.
- Reuse the already governed PostgreSQL infrastructure boundary rather than introduce a second state service solely for initial Session storage.
- Use a standard Spring Session integration instead of building all security-sensitive Session infrastructure directly.
- Preserve ADR-0017 schema ownership, least privilege, and Flyway migration control.
- Prevent Session infrastructure from becoming a competing aggregate-persistence architecture under ADR-0018.
- Preserve DEC-0001 dependency admission, locking, verification, and executable validation as a separate implementation change.
- Keep Redis available for future separately governed use without introducing or prohibiting it here.
- Preserve a governed migration path to another Session store without changing Domain or Application ownership semantics.

## Decision

If Accepted, the production mechanism for ADR-0019 authoritative Identity Session state SHALL be Spring Session JDBC backed by the governed application PostgreSQL database.

Spring Session SHALL be a narrowly bounded Session infrastructure mechanism. Spring Session JDBC SHALL be the selected physical persistence integration, and PostgreSQL SHALL be the authoritative production Session store.

Session persistence SHALL belong to the Identity Module boundary and SHALL use an Identity-owned PostgreSQL schema consistent with ADR-0017. Physical co-location in the application PostgreSQL database SHALL NOT authorize another Module to read, mutate, join, or otherwise depend directly on Identity Session objects.

Spring Session JDBC is infrastructure and Session-state management. It SHALL NOT become a second repository-wide aggregate-persistence architecture, supersede ADR-0018, redefine an Aggregate, or authorize arbitrary JDBC persistence outside the approved persistence boundaries.

Runtime automatic schema initialization SHALL NOT be a migration authority. Every Session database object and subsequent governed change SHALL be introduced and evolved through version-controlled, Identity-owned Flyway migrations. Applied migration immutability and existing migration review, compatibility, rollback, testing, and ownership requirements remain in force.

Runtime database access SHALL retain ADR-0017's separation between least-privileged application access and separately bounded migration or schema-changing authority. Ordinary runtime identities SHALL NOT receive DDL or unrestricted administrative privileges.

Shared PostgreSQL-backed Session state SHALL support multiple application instances without relying on application-local authoritative state or sticky-session correctness. Application-local caches or representations, if later justified, SHALL remain non-authoritative and SHALL NOT restore access when authoritative state is unavailable, revoked, expired, terminated, or uncertain.

Authoritative logout, independent Session revocation, security-triggered invalidation of all affected Sessions, renewal, and fixation resistance SHALL preserve ADR-0019. Retry, renewal, reordered work, stale application-local state, restart, or concurrent processing SHALL NOT restore revoked, invalidated, expired, terminated, compromised, disabled, or stale-privilege access.

Application-local or in-memory Session storage SHALL NOT be the production Session authority. It MAY be used only as explicitly bounded test infrastructure where it is not represented as evidence of PostgreSQL, Flyway, shared-state, restart, revocation, concurrency, or production failure behavior.

This decision does not introduce Redis. Architecture Open Decision 8 remains unresolved for unrelated Redis use cases. Redis is not prohibited as a future Session store or for another future use, but introducing it or migrating authoritative Session state to it requires separate governance.

## Identity and Persistence Boundaries

Identity remains authoritative for Session identity, Principal association, validity, renewal eligibility, revocation, termination, compromise, invalidation, and applicable security context. Spring Session and PostgreSQL implement storage mechanics; they do not acquire Identity or Authorization authority.

Domain and Application code SHALL remain independent of Spring Session, JDBC, PostgreSQL, Flyway, database representations, and framework lifecycle types. Project-owned Ports or equivalent inward-owned boundaries SHALL be used where the approved architecture requires application orchestration to interact with Session infrastructure. Concrete Spring Session JDBC and database behavior SHALL remain in the appropriate Identity Adapter, infrastructure, or configuration boundary.

Authentication and Session possession do not authorize every Resource or action. Contextual Authorization remains with the Domain or Use Case owning the affected Resource, property, action, association, and current state.

Spring Data JDBC remains the repository-wide primary aggregate-persistence mechanism, and bounded Spring `JdbcClient` remains the complementary persistence-Adapter mechanism authorized by ADR-0018. Spring Session JDBC does not amend those roles because it provides narrowly bounded technical Session infrastructure rather than a competing business-Aggregate persistence model.

## Security Requirements Preserved

If Accepted, implementation of this decision must preserve:

- minimally scoped `Secure` and `HttpOnly` browser Session credential handling;
- the strictest workable `SameSite` policy without this ADR selecting its exact value;
- CSRF protection for cookie-authenticated state-changing requests;
- explicit CORS behavior that is not treated as CSRF protection;
- Session identifier change after successful Authentication and after privilege elevation;
- authoritative server-side logout;
- independently revocable concurrent Sessions;
- security-wide invalidation where required by password reset, compromise handling, account disablement, material privilege change, or other governed security state;
- mandatory privileged MFA without selecting its factor, protocol, provider, or configuration;
- failure closed when authoritative Session validity cannot be established;
- no restoration of withdrawn access through renewal, retry, race, restart, stale state, or reordered processing;
- no browser persistence of bearer Access Tokens, Refresh Tokens, Session secrets, or equivalent bearer credentials for the normal first-party browser flow;
- exclusion of Session secrets and credential material from URLs, logs, telemetry, analytics, errors, client bundles, and support evidence;
- applicable least privilege, Sensitive Data, Environment-isolation, Audit Record, monitoring, abuse-resistance, and incident-response requirements; and
- compatibility with a future separately governed OIDC/OAuth2 Identity Provider whose credentials remain behind ADR-0019's server-side custody boundary.

This ADR selects no cryptographic algorithm, identifier format, lifetime, threshold, retry count, availability target, or other numerical value.

## Failure, Concurrency, and Operational Consequences

PostgreSQL availability becomes material to authenticated Session availability. An unavailable, failed, or uncertain authoritative Session lookup MUST NOT become authenticated success. Recovery and reconciliation MUST preserve revocation and newer accepted Session truth.

Concurrent renewal, logout, invalidation, privilege change, and revocation must preserve the latest accepted authority. Database transactions, constraints, locking, and concurrency mechanisms may be selected only where justified by applicable Requirements and implementation evidence; this ADR establishes no universal isolation level, lock, retry, timeout, or conflict algorithm.

The selected shared store removes application-local Session authority as a horizontal-scaling dependency, but it does not itself select PostgreSQL hosting, high availability, replication, backup, recovery objectives, connection pooling, capacity, maintenance, or deployment topology.

Operational telemetry must distinguish Session creation, use, renewal, logout, revocation, invalidation, denial, failure, and uncertainty without exposing credentials, Session secrets, protected Identity existence, or unnecessary Sensitive Data.

## Dependency and Implementation Boundary

Acceptance of ADR-0020 would establish Architecture direction only. It would not admit a dependency, modify a build, create an implementation, create database objects, or prove executable behavior.

Subsequent implementation requires DEC-0001-governed admission and review of:

- Spring Security;
- the appropriate Spring Session JDBC dependency; and
- any additional direct or transitive dependency not already admitted by the executable backend baseline.

Dependency admission must preserve Java 21, Spring Boot 3.5.16, Gradle 8.14.5, Spring Boot dependency alignment, dependency locking, strict dependency verification, reproducible builds, compatibility review, and applicable security evidence. This ADR pins no dependency version.

After acceptance and dependency admission, separately reviewed implementation may establish:

- an Identity-owned Session Port or equivalent inward-owned boundary where required;
- Spring Session JDBC outbound or infrastructure integration;
- Identity-owned Flyway migrations;
- Spring Security configuration consistent with ADR-0019;
- PostgreSQL 18 integration tests;
- authoritative logout and revocation tests;
- concurrent and independently revocable Session tests;
- fixation and identifier-change tests;
- application restart and shared-state tests;
- failure-closed tests; and
- tests proving revoked or invalidated access cannot be restored.

None of those dependencies, migrations, configurations, tests, or implementations is created or claimed by this Proposed ADR.

## Migration, Compatibility, and Reversibility

No legacy production Session store or active production Session population currently requires migration. If Accepted and later implemented, PostgreSQL through Spring Session JDBC becomes the initial production Session authority.

A future migration to Redis or another Session store requires separate governance and must preserve Identity ownership, Domain and Application independence, authoritative validity, logout, revocation, invalidation, fixation resistance, and failure-closed behavior. Migration may preserve active Sessions only when their authoritative state and security properties can be transferred safely; otherwise affected Sessions must be deliberately invalidated and reauthentication required.

Rollback, migration, dual-running, retry, or reconciliation MUST NOT silently restore Sessions that were revoked, expired, terminated, compromised, disabled, or invalidated through privilege change. Physical Session-store replacement must not leak storage or framework types into Domain or Application code.

The use of project-owned boundaries, Identity ownership, and Flyway-managed data evolution reduces but does not eliminate exit cost. This ADR selects no migration date, rollout phase, dual-write design, migration tool, compatibility interval, or rollback mechanism.

## Consequences

### Positive Consequences

- The initial production Session store reuses the governed PostgreSQL infrastructure boundary.
- Shared authoritative state supports multiple application instances and survives individual application-process restart.
- Authoritative logout, independent revocation, and security-triggered invalidation can operate against one shared Session authority.
- Spring Session supplies standard Spring Session lifecycle integration instead of requiring all Session infrastructure to be developed directly.
- Identity ownership, dedicated-schema governance, Flyway migrations, and least privilege remain aligned with ADR-0017.
- The decision avoids introducing Redis solely for initial Session storage.
- ADR-0019's protected-cookie and BFF-style credential-custody architecture remains unchanged.

### Negative Consequences and Trade-offs

- Authenticated availability becomes coupled to PostgreSQL Session-store availability and performance.
- Session traffic, cleanup, expiry, capacity, contention, backup, recovery, and operational diagnosis add load and responsibilities to PostgreSQL.
- Spring Session JDBC introduces framework-specific storage compatibility and migration obligations inside the infrastructure boundary.
- Identity-owned Flyway migrations must maintain compatibility with the selected Spring Session integration.
- Dependency admission, security configuration, migrations, and executable verification remain additional work.
- Moving to Redis or another store later requires separately governed migration or deliberate Session invalidation.

## Alternatives Considered

### A. Custom Identity-Owned PostgreSQL Session Adapter

This approach is compatible with PostgreSQL, ADR-0017 ownership, Flyway, and ADR-0019 authoritative Session semantics. It could remain behind project-owned Identity Ports and use the ADR-0018 persistence direction where applicable.

It was not selected because it would require the project to design and maintain more security-sensitive Session lifecycle, persistence, expiry, lookup, and framework-integration behavior directly. It remains technically viable but offers less standard Spring Session integration for the initial implementation.

### B. Spring Session JDBC — Selected

Spring Session JDBC provides a standard Spring integration for shared server-side Session state backed by the already governed PostgreSQL database. It supports multiple application instances without local authoritative state and permits Identity-owned Flyway control and schema ownership.

Its costs include PostgreSQL availability coupling, framework-compatible Session schema maintenance, database load, cleanup, and operational responsibility. Selection does not make Spring Session a Domain authority or a second aggregate-persistence architecture.

### C. Redis-Backed Spring Session

Redis-backed Spring Session is viable in principle and could provide shared low-latency Session state across application instances. It may become appropriate through future governance.

It was not selected because the current repository has not introduced Redis, and doing so solely for initial Session storage would add a new infrastructure service, client dependencies, security configuration, availability and partition behavior, eviction and persistence semantics, monitoring, recovery, provider or hosting concerns, and Architecture governance. This is a current-context trade-off, not a claim that PostgreSQL is universally superior or that Redis is permanently prohibited.

### D. Application-Local or In-Memory Sessions

Application-local state avoids an external Session-store dependency but cannot provide the governed production authority reliably across multiple application instances, process restart, or cross-instance revocation and invalidation. Sticky routing would not repair all restart, revocation, stale-state, or recovery semantics and is not selected.

This alternative is rejected as production authority. Explicitly bounded in-memory test infrastructure remains permissible when it is not treated as production-behavior evidence.

## Explicit Non-Decisions and Unresolved Matters

ADR-0020 does not select, define, or authorize:

- exact Session, inactivity, or privileged-Session lifetime;
- renewal, expiry, cleanup, or rotation threshold;
- cookie name, `Domain`, `Path`, or exact `SameSite` value;
- Session identifier format;
- cryptographic algorithm;
- MFA factor, protocol, provider, configuration, or broader Customer MFA policy;
- an Identity Provider or OIDC/OAuth provider;
- service-to-service credentials;
- API routes, HTTP methods, statuses, DTOs, or payload schemas;
- Product Decision 23, a Role or Permission model, or an access matrix;
- concrete table, column, index, constraint, sequence, or SQL design;
- PostgreSQL hosting, provider, SKU, extension, pool, replication, high availability, backup, RPO, RTO, or recovery policy;
- exact dependency coordinates or versions;
- unrelated Redis introduction or use;
- another business Module's behavior or authority;
- a numerical limit, threshold, timeout, retry count, capacity target, SLA, SLO, or propagation interval; or
- executable implementation, configuration, migration, deployment, or completed test evidence.

Architecture Open Decision 8 remains unresolved for unrelated Redis use cases. This ADR does not prohibit future Redis adoption through separate governance.

## Governance Relationships

ADR-0020 is subordinate to and preserves:

- `.ai/core/AGENTS.md`;
- `.ai/core/GLOSSARY.md`;
- `.ai/core/PRODUCT.md`;
- `.ai/core/ARCHITECTURE.md`;
- `.ai/core/SECURITY-STANDARDS.md`;
- `.ai/core/TESTING-STANDARDS.md`;
- `.ai/core/CODING-STANDARDS.md`;
- `.ai/core/DECISIONS.md`;
- `.ai/backend/SPRING.md`;
- `.ai/backend/DATABASE.md`;
- `.ai/backend/POSTGRES.md`;
- the Approved Identity Domain Specification;
- the Approved Identity and Access Backend Specification;
- Accepted ADR-0017;
- Accepted ADR-0018;
- Accepted ADR-0019;
- Accepted DEC-0001; and
- Accepted DEC-0002.

This Proposed ADR supersedes none of those sources. Until it is Accepted and canonical synchronization is complete, it provides no authority to admit dependencies, create Session persistence, or begin the governed implementation.

## Required Governance Reviews

Before ADR-0020 may become Accepted, governance review must confirm:

- Architecture approval of the physical Session-store choice and durable operational consequences;
- Identity ownership approval of Session authority, lifecycle, revocation, invalidation, and storage boundaries;
- Security approval of credential custody, fixation resistance, failure-closed behavior, revocation, Sensitive Data, least privilege, and Audit Record boundaries;
- Database and PostgreSQL review of Identity schema ownership, Flyway-only migration authority, runtime privileges, concurrency, compatibility, and operational consequences;
- Spring review of the bounded Spring Session JDBC role and separation from Domain and Application code;
- persistence-architecture review confirming that Spring Session JDBC does not supersede or bypass ADR-0018;
- dependency-governance review confirming separate DEC-0001 admission and validation;
- Testing review of PostgreSQL, restart, shared-state, revocation, concurrency, fixation, and failure evidence;
- Operations review of PostgreSQL availability, capacity, recovery, monitoring, and horizontal-scaling consequences; and
- Documentation review of canonical synchronization, authority boundaries, and unresolved matters.

No review is represented as complete while this ADR remains Proposed. Named reviewers, meetings, tickets, signatures, implementation evidence, and test execution must not be fabricated.

## Acceptance Conditions and Synchronization

ADR-0020 may become Accepted only after the required governance reviews complete with no unresolved acceptance blocker and the selected decision is confirmed consistent with ADR-0017, ADR-0018, ADR-0019, DEC-0001, DEC-0002, and governing security and implementation standards.

Acceptance must synchronize exactly the directly affected canonical sources:

1. `specifications/adr/ADR-0020-identity-session-store-strategy.md`;
2. `.ai/core/DECISIONS.md`;
3. `.ai/core/ARCHITECTURE.md`;
4. `.ai/backend/SPRING.md`;
5. `.ai/backend/DATABASE.md`; and
6. `.ai/backend/POSTGRES.md`.

Acceptance synchronization must:

- promote ADR-0020 through the governed Proposed-to-Accepted lifecycle;
- update ADR-0020's existing indexed status from Proposed to Accepted in `DECISIONS.md`;
- establish Spring Session JDBC with PostgreSQL as the Identity Session-store Architecture;
- preserve ADR-0019's browser credential and authoritative Session semantics;
- establish Identity-owned schema and Flyway-only migration ownership without inventing concrete database objects;
- preserve ADR-0018 and distinguish Spring Session infrastructure from aggregate persistence;
- preserve runtime least privilege and prohibited cross-Module persistence access;
- record that Redis is not introduced and Open Architecture Decision 8 remains unresolved for unrelated uses;
- preserve separate DEC-0001 dependency admission and executable implementation; and
- claim no completed dependency admission, migration, configuration, implementation, deployment, or executable testing.

No acceptance change to `PRODUCT.md`, `SECURITY-STANDARDS.md`, `TESTING-STANDARDS.md`, `JAVA.md`, the Identity specifications, build files, or implementation files is authorized unless acceptance review finds a direct contradiction requiring separately governed correction.

## Validation Criteria

Before acceptance, review must verify:

1. metadata is `0.1.0 Proposed`, owner `Architecture`, last updated `2026-09-29`, and `authoritative: false`;
2. Spring Session JDBC backed by the governed application PostgreSQL database is the single proposed production Session-store strategy;
3. the decision remains consistent with ADR-0017's one-database, dedicated-schema-per-persistence-owner strategy;
4. the decision remains consistent with ADR-0018 and does not create a second aggregate-persistence architecture;
5. every ADR-0019 authoritative Session, browser credential, revocation, renewal, fixation, CSRF, MFA, failure, and non-authority boundary remains preserved;
6. Identity remains the sole owner of authoritative Session state and the Session persistence boundary;
7. Session database objects remain attributable to an Identity-owned PostgreSQL schema without concrete schema or object design being invented;
8. Flyway remains the sole migration authority, runtime schema initialization is prohibited as migration authority, and applied migrations remain immutable;
9. runtime application access remains least privileged and separate from migration or schema-changing authority;
10. Redis is not introduced, is not permanently prohibited, and Open Architecture Decision 8 remains unresolved for unrelated uses;
11. application-local or in-memory Session state is prohibited as production authority and bounded test use is not represented as production evidence;
12. shared PostgreSQL-backed authority supports multiple application instances without sticky-session correctness or local authoritative state;
13. logout, independent revocation, security-wide invalidation, renewal, restart, concurrency, and reordered work cannot restore withdrawn access;
14. unavailable, failed, invalid, expired, revoked, terminated, or uncertain authoritative Session state fails closed;
15. Spring Security, Spring Session JDBC, and any additional dependency remain subject to separate DEC-0001 admission, locking, verification, and executable validation;
16. no dependency, build change, schema, migration, configuration, implementation, deployment, or completed implementation test is claimed;
17. every listed Product, security, provider, API, schema, infrastructure, and numerical non-decision remains unresolved;
18. migration and rollback preserve security semantics or deliberately invalidate affected Sessions and never restore revoked or expired access;
19. Domain and Application ownership remain independent of the physical Session store and framework types remain outside those inward layers;
20. the four alternatives are represented accurately without claiming PostgreSQL is universally superior to Redis;
21. the six-file canonical acceptance-synchronization set is complete and no synchronization is performed while the ADR remains Proposed; and
22. the Proposed change affects exactly `specifications/adr/ADR-0020-identity-session-store-strategy.md` and `.ai/core/DECISIONS.md`, registers ADR-0020 as Proposed in the canonical Decision Index, passes whitespace and diff validation, and introduces no unrelated tracked changes.

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
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [ADR-0017 — PostgreSQL Schema Strategy](ADR-0017-postgresql-schema-strategy.md)
- [ADR-0018 — Persistence Technology](ADR-0018-persistence-technology.md)
- [ADR-0019 — Authentication Session and Token Strategy](ADR-0019-authentication-session-token-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0002 — PostgreSQL Release Baseline](../decisions/DEC-0002-postgresql-release-baseline.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-28 | Proposed | Proposed Spring Session JDBC backed by the governed application PostgreSQL database as the production Identity Session store, preserving ADR-0019 security semantics, ADR-0017 ownership and Flyway authority, ADR-0018 persistence boundaries, separate DEC-0001 dependency admission, and unresolved Redis and implementation choices. |
