# ADR-0018 — Persistence Technology

## Identifier

ADR-0018

## Title

Persistence Technology

## Version

0.1.0

## Status

Proposed

## Date

2026-09-25

## Last Updated

2026-09-25

## Owner

Architecture

## Authoritative

false

## Scope

persistence-architecture

## Context

The Approved Architecture establishes a Java 21 and Spring Boot 3.x modular monolith with PostgreSQL as its authoritative transactional database. Accepted DEC-0001 fixes Java 21, Spring Boot 3.5.16, Gradle 8.14.5, centralized dependency alignment, dependency locking, and dependency verification. Accepted DEC-0002 establishes PostgreSQL 18 as the supported major-version baseline. Accepted ADR-0017 establishes one application PostgreSQL database with a dedicated schema for each persistence-owning Module or Domain boundary.

Flyway is already the mandatory and sole governed schema-migration mechanism. Domain and Application layers must depend inwardly on project-owned Ports rather than persistence frameworks, database drivers, SQL representations, or infrastructure models. Direct unauthorized cross-Module persistence access remains prohibited even though Modules share one application database.

The current Architecture and implementation standards deliberately do not select JPA, Hibernate, another ORM, a SQL mapper, or a database-access library. A repository-wide persistence direction is material because it shapes aggregate persistence, mappings, queries, concurrency, framework coupling, build dependencies, testing, and operational diagnosis. This Proposed ADR records the owner-selected direction without authorizing dependencies or claiming implementation.

## Decision Drivers

- Preserve Domain and Application independence from persistence frameworks.
- Keep project-owned Ports as the inward-facing persistence boundary.
- Support aggregate-oriented transactional persistence without introducing an ORM persistence context into Domain code.
- Permit explicit SQL only through a narrow, attributable Adapter boundary when repository abstractions are insufficient.
- Preserve governed Database Transaction ownership, concurrency, locking, constraint, idempotency, and failure semantics.
- Preserve Flyway as the sole schema authority and prohibit runtime schema generation.
- Preserve ADR-0017 schema ownership and cross-Module access restrictions.
- Support PostgreSQL-specific behavior where an Approved Requirement requires it.
- Require realistic PostgreSQL 18 verification through Testcontainers where database behavior matters.
- Keep persistence behavior observable and diagnosable without exposing Sensitive Data.
- Prevent arbitrary per-feature persistence technology proliferation.
- Keep dependency admission subordinate to DEC-0001.

## Proposed Decision

If Accepted, Spring Data JDBC SHALL be the repository-wide primary persistence technology for aggregate-oriented transactional persistence.

Spring `JdbcClient` SHALL be permitted only as a narrowly bounded complementary mechanism inside persistence Adapters where a concrete need is not adequately expressed by Spring Data JDBC repository or query abstractions. Such needs may include justified complex reads or Projections, explicit SQL, locking operations, PostgreSQL-specific operations, and query shapes that are not appropriately represented by Spring Data JDBC repositories.

`JdbcClient` is not a second repository-wide persistence architecture. Its use must be attributable to the persistence-owning Module or Domain, remain behind project-owned Ports, and satisfy the same ownership, transaction, security, testing, observability, and compatibility rules as Spring Data JDBC persistence.

This proposal does not authorize dependency or implementation work while its status remains Proposed.

## Primary and Complementary Responsibilities

### Spring Data JDBC

Spring Data JDBC is proposed to own the default aggregate-persistence mechanism within persistence Adapters, including applicable aggregate loading, insertion, update, deletion, repository integration, mapping, and governed optimistic-version behavior.

Its aggregate model must follow the owning Domain's approved Aggregate boundaries. Framework convenience must not redefine Aggregate ownership, lifecycle, invariants, associations, or transaction scope.

### JdbcClient

`JdbcClient` may be used only when the persistence Adapter documents a concrete query or operation that Spring Data JDBC does not adequately express. Every use must:

- remain inside the owning persistence Adapter or another explicitly approved infrastructure boundary;
- use the owning schema and preserve ADR-0017 ownership;
- implement an approved project-owned Port or an internal Adapter operation supporting that Port;
- avoid direct access to another Module's internal schema or persistence objects;
- use parameter binding and safe SQL construction;
- preserve approved transaction ownership and failure translation;
- include focused PostgreSQL verification where SQL, locking, constraints, pagination, or query behavior is material; and
- remain reviewable and observable without logging prohibited values.

`JdbcClient` must not become an unrestricted bypass around aggregate invariants, Spring Data JDBC ownership, project-owned Ports, Module Contracts, Application Services, contextual Authorization, or separately governed cross-Module access rules.

## Dependency Direction and Model Separation

Domain and Application code SHALL remain independent of Spring Data JDBC, `JdbcClient`, JDBC, SQL, database drivers, and persistence-specific lifecycle behavior.

Spring Data JDBC annotations and types, `JdbcClient`, JDBC types, SQL representations, `RowMapper` implementations, persistence records or models, generated database representations, and other persistence-framework details must remain inside appropriate Adapter or infrastructure boundaries.

Persistence representations must remain separate from governed Domain models wherever framework annotations, persistence metadata, storage-driven construction, mutable persistence lifecycle, or other persistence concerns would otherwise enter Domain code. Mapping between Domain and persistence representations must be explicit, testable, and owned by the persistence Adapter.

Project-owned Repository or query Ports remain the abstraction exposed toward Application and Domain code. Those Ports must express owned application or Domain needs and must not expose Spring Data, JDBC, SQL, persistence pagination, locking, or database error types.

## Transactions and Concurrency

Spring-managed Database Transaction integration must preserve the existing transaction-ownership rules in Architecture, `DATABASE.md`, and `SPRING.md`. This ADR does not establish a new global transaction model, propagation default, isolation level, retry policy, or cross-system transaction guarantee.

Where an Approved Requirement requires optimistic concurrency, the owning persistence Adapter must implement explicit governed version semantics. The version check must participate in the actual write condition, and a conflict must not be represented as successful persistence.

Pessimistic locking, explicit `SELECT FOR UPDATE`, or an equivalent governed mechanism may be implemented inside a persistence Adapter only when an Approved Requirement and evidenced invariant require it. Lock scope, acquisition order, duration, timeout behavior, contention, deadlock behavior, transaction ownership, and verification remain subject to existing standards; this ADR selects no universal locking rule.

Database constraints and uniqueness remain authoritative concurrency-safe enforcement where applicable. Application pre-checks do not replace constraints. Persistence Adapters must translate expected constraint, uniqueness, optimistic-conflict, locking, and other database failures into project-owned failure semantics without exposing database internals.

## Schema, Ownership, and Flyway

This proposal inherits Accepted ADR-0017 without redefinition:

- one application PostgreSQL database in the initial primary PostgreSQL service;
- one dedicated schema for each persistence-owning Module or Domain boundary;
- identifiable ownership for every governed persistent object;
- schema separation reinforces but does not replace logical ownership and Module Contracts;
- physical co-location never authorizes direct cross-Module persistence access;
- Flyway migration ownership aligns with persistence ownership;
- ordinary runtime capability cannot perform DDL or schema administration; and
- migration or schema-changing capability remains separately bounded under least privilege.

Spring Data JDBC and `JdbcClient` operations must use the owning schema and must not introduce unauthorized cross-Module database coupling. Any direct cross-Module persistence exception remains subject to the separate Accepted-ADR and synchronized-Architecture mechanism already governed by ADR-0017 and `ARCHITECTURE.md`.

Flyway remains the mandatory and sole governed mechanism for schema and governed data migrations. Spring Data JDBC, `JdbcClient`, or any related integration must not generate, mutate, create, update, or validate-and-change production schemas as a competing schema authority. This ADR does not reopen Flyway versus Liquibase and does not authorize another migration framework.

## Dependency and Build Boundary

This proposal inherits Java 21, Spring Boot 3.5.16, Gradle 8.14.5, the Spring Boot dependency-management baseline, fixed versions, dependency locking, dependency verification, reproducibility, and applicable Pipeline controls from Accepted DEC-0001.

ADR-0018 does not select exact new dependency coordinates or versions. After acceptance, actual dependency admission must occur through a separate implementation change under DEC-0001. That change must use the governed Spring Boot dependency-management baseline where applicable, update lock and verification evidence, pass compatibility and security review, and introduce no competing persistence framework.

PostgreSQL 18 remains governed by Accepted DEC-0002. This ADR neither reopens the database release decision nor pins a PostgreSQL maintenance release, driver version, Testcontainers version, or container image.

## Testing and Verification

Implementation of an Accepted ADR-0018 must provide objective evidence appropriate to the affected behavior, including:

- architecture tests or equivalent enforcement that Domain and Application code do not depend on persistence-framework types;
- tests that persistence details remain behind project-owned Ports and owned Adapters;
- real PostgreSQL 18 integration tests through Testcontainers where SQL, schema qualification, mapping, constraints, transactions, locking, isolation, or PostgreSQL behavior matters;
- aggregate round-trip and invariant-preservation tests for Spring Data JDBC persistence;
- version-condition and conflict tests where optimistic concurrency is required;
- locking, contention, timeout, deadlock, and recovery tests where pessimistic or explicit locking is used;
- concurrent uniqueness and idempotency tests where constraints protect required behavior;
- query, projection, sorting, pagination, mapping, empty-result, and failure tests for bounded `JdbcClient` operations;
- denial or architecture-boundary evidence preventing unauthorized cross-Module persistence access;
- Flyway-before-persistence compatibility and migration evidence; and
- dependency, lock-state, verification-metadata, build, and security evidence required by DEC-0001.

In-memory substitutes are not sufficient evidence for PostgreSQL-specific SQL, schemas, constraints, locking, transaction, Flyway, or failure behavior.

## Observability and Operations

Persistence behavior must remain diagnosable through safe, attributable logs, metrics, traces, correlation, database evidence, and applicable Audit Records. Evidence should distinguish operation type, owning Module, outcome, duration, conflict or constraint category, and safe identifiers where permitted.

SQL and parameter logging must not expose Secrets, credentials, tokens, payment credentials, prohibited Sensitive Data, or unnecessary personal information. This ADR does not select a logging library, SQL-proxy library, monitoring provider, sampling rate, slow-query threshold, or numerical operational target.

## Security Impact

This persistence-technology proposal does not alter existing Authentication, Authorization, Sensitive Data, least-privilege, Audit Record, or security authority. Persistence Adapters remain subject to all applicable security governance, and SQL or debugging observability must not expose Sensitive Data, Secrets, credentials, tokens, or payment credentials.

Ordinary runtime persistence access must not gain DDL or schema-administration authority. Migration and schema-changing capability remains separately bounded under ADR-0017 and existing database and security governance.

## Data Impact

PostgreSQL 18 remains authoritative under Accepted DEC-0002, ADR-0017's schema-per-persistence-owner strategy remains authoritative, and Flyway remains the sole governed migration mechanism. Acceptance of ADR-0018 would not create schemas, tables, columns, indexes, mappings, migrations, or production data changes.

Direct cross-Module persistence access remains prohibited unless separately governed through the existing Accepted-ADR and synchronized-Architecture mechanism.

## Compatibility, Migration, and Reversibility Impact

Acceptance establishes the architectural persistence direction only. No existing production persistence implementation or production data is migrated merely by accepting ADR-0018. Dependencies and executable implementation must be introduced separately under DEC-0001 and applicable repository governance.

Replacing Spring Data JDBC after persistence Adapters and mappings exist would have non-trivial migration cost. Project-owned Ports and Domain and Application independence reduce, but do not eliminate, that cost. A future replacement must preserve governed boundaries, safely migrate affected Adapters, persistence representations, and data where applicable, synchronize affected canonical sources, and use a new or superseding durable Architecture Decision.

This Proposed ADR does not invent a concrete migration plan before an executable persistence implementation exists.

## Operational Impact

Acceptance alone creates no deployment or runtime operational change. Later implementation must preserve existing observability, failure translation, Database Transaction, locking, security, migration, and PostgreSQL verification requirements.

This ADR does not invent numerical operational thresholds.

## Authority Boundaries

This proposal selects a persistence technology direction only. It does not transfer Product, Domain, Module, security, data, or operational authority to Spring Data JDBC, `JdbcClient`, PostgreSQL, or a persistence Adapter.

The owning Domain remains authoritative for business meaning, Aggregate boundaries, invariants, lifecycle, contextual Authorization, and approved persistence needs. Database constraints supplement but do not replace Domain validation or Authorization. Persistence success does not prove an External System outcome, event delivery, Payment result, Inventory decision, or other separately owned truth.

## Consequences

### Benefits

- The repository gains one primary aggregate-persistence direction rather than per-feature technology selection.
- Spring Data JDBC provides Spring-integrated aggregate repositories without introducing JPA persistence-context semantics into the architecture.
- Bounded `JdbcClient` use preserves explicit SQL access for evidenced cases where repository abstractions are insufficient.
- Project-owned Ports and separate persistence representations protect Domain and Application independence.
- Explicit SQL, constraints, version checks, and locking remain available at the Adapter boundary.
- The selected direction remains compatible with Flyway ownership and schema-per-persistence-owner architecture.

### Costs and Trade-offs

- Separate Domain and persistence representations require explicit mapping and tests.
- Spring Data JDBC's aggregate semantics require disciplined alignment with approved Aggregate boundaries.
- Complex joins, Projections, dynamic queries, and PostgreSQL-specific operations may require additional `JdbcClient` code.
- Bounded use of two Spring data-access APIs requires clear review rules to prevent role erosion.
- SQL, row mapping, batching, and advanced query behavior may require more explicit implementation than an ORM supplies.
- Contributors must understand Spring Data JDBC save semantics, transaction behavior, schema qualification, constraints, locking, and PostgreSQL failure handling.

## Alternatives Considered

### A. Spring Data JDBC with Bounded JdbcClient

Selected as this proposal. It provides a repository-wide aggregate-persistence mechanism integrated with Spring while retaining narrowly bounded explicit SQL for justified Adapter needs. Its costs are explicit mapping, aggregate-model discipline, and additional SQL or row-mapping work for complex operations.

### B. jOOQ

jOOQ provides a strong SQL-first, type-safe DSL, extensive query composition, PostgreSQL-specific SQL support, batching, locking, and schema-derived code generation. It was not selected as the primary persistence direction because its strongest model introduces a generated-schema and code-generation lifecycle and leaves aggregate persistence and Domain mapping more explicitly application-owned. This is a trade-off, not an incompatibility claim.

### C. Spring Data JPA / Hibernate

JPA and Hibernate provide mature ORM, repository, transaction, optimistic-locking, pessimistic-locking, query, projection, and persistence-context capabilities. They were not selected because proxy, lazy-loading, dirty-checking, cascading, flush, and persistence-context semantics create additional coupling and mapping consequences that the selected Spring Data JDBC direction avoids. JPA could remain behind separate persistence models, so this is not an incompatibility claim.

### D. Spring JdbcClient or JdbcTemplate as the Sole Primary Mechanism

An explicit JDBC-only direction provides direct SQL visibility and control over PostgreSQL behavior, constraints, locking, batching, and mappings. It was not selected as the sole primary mechanism because aggregate loading, change persistence, repository implementation, version handling, and repeated mapping infrastructure would remain entirely application-owned. The proposal instead bounds `JdbcClient` to cases where explicit control is justified.

### E. MyBatis

MyBatis provides explicit SQL mapping, dynamic query support, Spring transaction integration, and optional generation without ORM persistence-context semantics. It was not selected because it would introduce a separate mapper abstraction and explicitly versioned third-party Spring Boot integration outside the current Spring Boot-managed persistence starters. This is a dependency and architectural trade-off, not an incompatibility claim.

## Competing Persistence Technologies

Acceptance of this ADR would not authorize JPA, Hibernate, jOOQ, MyBatis, or another ORM, mapper, generated SQL DSL, or repository-wide persistence mechanism alongside Spring Data JDBC.

It would also prohibit arbitrary per-feature selection of persistence technologies. A future departure, replacement, or material expansion beyond this direction requires applicable governance and, where the Approved Architecture baseline changes, an Accepted ADR with synchronized canonical sources.

## Explicit Non-Decisions

ADR-0018 does not select, define, or authorize:

- concrete schemas or the schema-name inventory;
- tables, columns, indexes, constraints, views, sequences, or database functions;
- a universal Primary Key strategy unless already governed elsewhere;
- concrete Aggregate mappings, persistence models, annotations, converters, callbacks, or repository interfaces;
- concrete SQL, `RowMapper` implementations, queries, joins, Projections, sorting, or pagination;
- exact transaction isolation levels, propagation settings, or rollback rules;
- universal optimistic-version fields or locking rules;
- connection-pool implementation or numerical pool settings;
- an exact PostgreSQL maintenance release, JDBC driver, Testcontainers version, container image, or image tag;
- exact new dependency coordinates or versions;
- database hosting, provider, SKU, backup retention, RPO, RTO, high availability, replication, or recovery topology;
- Product policy or unresolved Domain or Product decisions;
- session, token, MFA, SSO, Role, or Permission strategy;
- API routes, HTTP methods or statuses, DTOs, event names or payloads, or Contract changes;
- infrastructure as code, application hosting, or deployment topology;
- a cache, Redis, broker, provider, extension, ORM, SQL mapper, generated SQL DSL, or second persistence model; or
- any timeout, retry count, TTL, capacity, performance target, SLA, SLO, or other numerical value not already governed.

## Required Governance Reviews

Before this Proposed ADR may become Accepted, governance must complete:

- Architecture review of the primary and complementary technology boundary;
- affected Domain and Module ownership review of aggregate and persistence-model separation;
- Database and PostgreSQL review of constraints, locking, schema qualification, Flyway, and failure semantics;
- Spring and Java review of framework integration and dependency direction;
- Security and privacy review of SQL safety, data exposure, least privilege, and observability;
- Testing review of PostgreSQL 18/Testcontainers, architecture, concurrency, locking, and failure evidence;
- Engineering and dependency-governance review under DEC-0001;
- Operations review of diagnosis, logging, metrics, tracing, and support consequences; and
- Documentation review of authority, alternatives, consequences, exclusions, acceptance conditions, and synchronization scope.

No review is represented as complete while this ADR remains Proposed.

## Acceptance Conditions and Synchronization

This Proposed decision may become Accepted only after:

- all required governance reviews complete without an unresolved blocker;
- the primary Spring Data JDBC role and bounded `JdbcClient` role are confirmed as unambiguous and enforceable;
- Domain and Application independence, project-owned Ports, separate persistence representations, and Adapter containment are confirmed;
- Flyway remains the sole migration authority and runtime schema generation remains prohibited;
- PostgreSQL 18, ADR-0017 schema ownership, and cross-Module access restrictions are inherited without redefinition;
- concurrency, locking, constraint, transaction, failure, observability, and testing expectations are confirmed compatible with Approved governance;
- dependency admission remains deferred to a separate DEC-0001-governed implementation change;
- no competing persistence framework or arbitrary per-feature selection is authorized; and
- all Explicit Non-Decisions remain unresolved.

Acceptance of ADR-0018 requires exactly the following minimum canonical synchronization under current repository evidence:

1. `specifications/adr/ADR-0018-persistence-technology.md` must be promoted from `0.1.0 Proposed` to the repository's Accepted lifecycle and version, record completed governance review without fabricated evidence, and preserve that dependency and implementation work has not yet occurred.
2. `.ai/core/DECISIONS.md` must change the ADR-0018 Decision Index status from Proposed to Accepted and update applicable metadata and Revision History.
3. `.ai/core/ARCHITECTURE.md` must replace the directly affected Spring Data JPA outbound-Adapter example or direction, establish Spring Data JDBC as the primary aggregate-persistence mechanism, establish `JdbcClient` as a bounded Adapter-only complementary explicit-query and SQL mechanism, preserve Domain and Application independence, and prohibit persistence annotations and types from leaking inward.
4. `.ai/backend/DATABASE.md` must replace directly affected persistence-technology-neutral wording, establish the Accepted ADR-0018 direction, synchronize directly affected quality and final-validation wording, and preserve Flyway, PostgreSQL 18, ADR-0017, ownership, Database Transaction, concurrency, and unresolved implementation constraints.
5. `.ai/backend/SPRING.md` must replace directly affected persistence-mechanism-neutral wording, establish the primary Spring Data JDBC and bounded complementary `JdbcClient` roles, synchronize directly affected quality and final-validation wording, and neither add dependencies nor claim implementation.

Current repository evidence does not require acceptance synchronization of `.ai/backend/POSTGRES.md`, `.ai/backend/JAVA.md`, `.ai/core/PRODUCT.md`, Approved backend Domain Specifications, `.ai/backend/API.md`, ADR-0017, DEC-0001, or DEC-0002. This synchronization determination is limited to current repository evidence and does not authorize unrelated edits.

Acceptance would authorize a subsequent dependency-admission and implementation-baseline change. It would not itself add dependencies, schemas, migrations, mappings, repositories, SQL, configuration, or application code.

## Validation Criteria

Acceptance-readiness validation must confirm that:

1. metadata remains `0.1.0 Proposed`, `authoritative: false`, owner `Architecture`, and scope `persistence-architecture` until acceptance;
2. Spring Data JDBC is unambiguously the proposed repository-wide primary aggregate-persistence mechanism;
3. `JdbcClient` is a narrowly bounded complementary persistence-Adapter mechanism and not an alternative primary architecture;
4. every `JdbcClient` use requires a concrete justified need, owned schema, project-owned Port or supporting Adapter operation, and focused verification;
5. Domain and Application code remain independent of persistence technology;
6. project-owned Ports remain the inward-facing abstraction and persistence representations remain appropriately separated from governed Domain models;
7. Spring-managed transactions preserve existing ownership without inventing a global transaction model;
8. optimistic concurrency, explicit locking, constraints, uniqueness, idempotency, and failure translation remain compatible with Approved Requirements and standards;
9. Flyway remains the mandatory and sole migration authority and runtime schema generation remains prohibited;
10. PostgreSQL 18 and the ADR-0017 one-database, schema-per-persistence-owner strategy are inherited without redefinition;
11. direct unauthorized cross-Module persistence access remains prohibited and `JdbcClient` creates no bypass;
12. real PostgreSQL 18/Testcontainers evidence is required where PostgreSQL behavior matters;
13. dependency admission, locking, verification, compatibility, and security evidence remain governed by DEC-0001 and deferred to implementation;
14. SQL/debugging observability remains sufficient while Sensitive Data and Secrets remain protected;
15. JPA/Hibernate, jOOQ, MyBatis, and arbitrary per-feature technology selection are not silently authorized;
16. every Explicit Non-Decision remains unresolved and no concrete persistence implementation is claimed;
17. alternatives and consequences are described without unsupported incompatibility claims;
18. every affected Related Document exists and materially supports the proposal;
19. Proposed registration changes remain limited to ADR-0018 and `DECISIONS.md` and introduce no unrelated repository changes; and
20. Markdown, links, lifecycle language, terminology, and whitespace validation pass.

## Supersedes

None. ADR-0018 supersedes no prior persistence-technology ADR.

## Superseded By

None. ADR-0018 has not been superseded.

## Related Documents

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [TESTING-STANDARDS.md](../../.ai/core/TESTING-STANDARDS.md)
- [ENGINEERING-PRINCIPLES.md](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [JAVA.md](../../.ai/backend/JAVA.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](../decisions/DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0002 — PostgreSQL Release Baseline](../decisions/DEC-0002-postgresql-release-baseline.md)
- [ADR-0017 — PostgreSQL Schema Strategy](ADR-0017-postgresql-schema-strategy.md)
- [Shared Backend Baseline Specification](../backend/shared/backend-baseline.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-25 | Proposed | Proposed Spring Data JDBC as the repository-wide primary aggregate-persistence mechanism with Spring JdbcClient as a narrowly bounded complementary persistence-Adapter mechanism, preserving Domain independence, project-owned Ports, Flyway authority, PostgreSQL 18, ADR-0017 ownership, DEC-0001 dependency governance, and implementation neutrality. |
