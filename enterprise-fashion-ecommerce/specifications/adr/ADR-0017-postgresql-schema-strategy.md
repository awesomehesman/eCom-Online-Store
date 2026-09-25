# ADR-0017 — PostgreSQL Schema Strategy

## Identifier

ADR-0017

## Title

PostgreSQL Schema Strategy

## Version

1.0.0

## Status

Accepted

## Date

2026-09-25

## Last Updated

2026-09-25

## Owner

Architecture

## Authoritative

true

## Scope

persistence-architecture

## Context

The Approved Architecture establishes PostgreSQL as the authoritative transactional database for the initial platform, one primary PostgreSQL database service, a modular-monolith deployment, explicit Domain and Module data ownership, and version-controlled Flyway migrations. `ARCHITECTURE.md` §40.3 permits the initial PostgreSQL deployment to use one database with one or more schemas but does not select the physical schema strategy.

Before this decision was Accepted, `ARCHITECTURE.md` §34 item 13 was an explicit Open Architecture Decision: the PostgreSQL schema strategy for enforcing Domain ownership within the modular monolith. `DATABASE.md` and `POSTGRES.md` preserved the same unresolved boundary. They required identifiable ownership, prohibited unauthorized cross-Module persistence access, and required Flyway migration ownership, least privilege, compatibility, recovery, and realistic verification without selecting one shared schema, schema-per-Module, grouped schemas, or database-per-Module.

Logical Module boundaries, project-owned Ports and Adapters, Application Services, Contracts, query boundaries, Projections, and approved events already govern collaboration. A physical schema strategy must reinforce those boundaries without making database proximity a new source of authority or implying that PostgreSQL permissions replace Domain and application enforcement.

This Accepted ADR selects the minimum physical containment strategy needed to resolve former Architecture Open Decision 13. Canonical Architecture and applicable database standards are synchronized with this decision. Acceptance authorizes the architecture direction but does not claim or authorize completed persistence implementation.

## Decision Drivers

- Every governed persistent object must have an identifiable owning Module or Domain.
- Physical organization should strengthen the existing logical ownership model without replacing it.
- Accidental cross-Module persistence coupling must be made more difficult and detectable.
- Flyway migration ownership and collision prevention must remain explicit.
- Ordinary runtime access must not acquire schema-administration or unrestricted database capability.
- The modular monolith must retain the approved single-database transaction capability where an approved Use Case legitimately requires it.
- The strategy must support realistic PostgreSQL and migration testing.
- Ownership boundaries should remain legible for possible later Module extraction without pre-authorizing extraction.
- The initial solution must remain compatible with one primary PostgreSQL service and avoid ungoverned distributed-transaction semantics.

## Decision

The initial modular monolith SHALL use one application PostgreSQL database within the initial primary PostgreSQL service, with a dedicated PostgreSQL schema for each persistence-owning Module or Domain boundary.

Each governed persistent object SHALL belong to one identifiable owner and SHALL reside in that owner's governed schema unless a separately Accepted Architecture Decision explicitly authorizes another arrangement. Schema separation strengthens physical ownership visibility and enforcement, but it does not replace logical Module boundaries, Domain authority, project-owned Ports and Adapters, contextual Authorization, or approved Contracts.

This decision selects schema-per-owning-Module/Domain physical containment. It does not require every conceptual Domain term to become a schema and does not create concrete schema names. The implementation mapping must follow the repository's approved Module and Domain ownership rather than inventing new business or service boundaries.

## Physical Containment and Ownership

- The initial deployment SHALL use one application database in the primary PostgreSQL service.
- Each persistence-owning Module or Domain boundary SHALL have one dedicated owned schema unless a later Accepted ADR changes that strategy.
- Tables, views, sequences, constraints, indexes, functions, and other governed application-persistence objects must have attributable ownership consistent with the selected schema boundary; Flyway history remains governed technical evidence with explicit ownership.
- Physical co-location in one database does not create shared ownership.
- A schema is an implementation boundary supporting ownership; it is not a new Domain, Aggregate, Contract, or source of Product semantics.
- Projections and reporting structures remain derived and must retain an identifiable owner and source authority.

## Cross-Module Persistence Access

Physical access to the same application database SHALL NOT authorize one Module to read or mutate another Module's internal tables or schema objects.

Modules must collaborate through already-governed Application Services, Contracts, query boundaries, Projections, Domain Events, Integration Events, or other approved boundaries. Direct cross-Domain or cross-Module table or schema access remains prohibited unless a separate Accepted ADR and synchronized Architecture update explicitly document and authorize the coupling, ownership, transaction implications, compatibility obligations, and future extraction impact.

ADR-0017 creates no general direct-access exception. Schema qualification, a database grant, a shared connection, a Foreign Key, or technical query capability cannot independently establish permission or transfer Domain authority.

## Flyway Migration Ownership

Flyway remains the mandatory mechanism for governed PostgreSQL schema and data changes.

- Each migration must have one attributable owning Module or Domain and must target only that owner's governed schema unless separately authorized.
- Migration locations and execution boundaries must make ownership reviewable and must prevent ambiguous ownership, duplicate identities, ordering ambiguity, and migration collisions.
- Coordinated changes affecting more than one owned schema must identify every affected owner, compatibility sequence, failure boundary, and recovery or forward-fix approach.
- Applied migrations remain immutable.
- Corrections use new forward migrations according to existing database governance, except for a separately approved and auditable operational repair.
- Runtime schema mutation, automatic production schema creation, and untracked DDL remain prohibited.

This ADR does not select an exact Flyway version, concrete migration filename scheme, Flyway configuration shape, schema-history-table placement, or execution command.

## Runtime and Migration Capability Boundary

Ordinary application runtime capability SHALL be separated from migration and schema-changing capability at the architectural level.

- Ordinary runtime access must not have DDL, schema-ownership, superuser, or unrestricted schema-administration capability.
- Migration capability may receive only the bounded schema-changing privileges required for approved migrations.
- Runtime, migration, read-only, operational, and administrative grants must follow least privilege and be bounded to approved schemas, objects, and actions.
- Privileged actions must remain attributable and auditable.
- Sharing a process or database does not justify unrestricted grants.
- Provider-specific usernames, PostgreSQL role names, credentials, identity mappings, secret-delivery mechanisms, and deployment wiring remain implementation decisions subject to existing governance.

This decision does not require one connection identity per Module. Any later identity arrangement must still enforce the selected schema ownership, prohibit runtime DDL, preserve Module boundaries, and satisfy the approved deployment and security architecture.

## Database Transactions

Because the owned schemas remain inside one PostgreSQL database, PostgreSQL Database Transactions can technically span schemas. That capability preserves the modular monolith's ability to perform an approved atomic Use Case where governing Domain and Architecture requirements require changes to succeed or fail together.

Technical transaction capability does not authorize arbitrary cross-Module writes. Database Transaction ownership remains with an approved Application Service or Use Case orchestration boundary. Cross-Module coordination must use approved Module Contracts and preserve each owner's invariants and authority. No database commit establishes an External System outcome, and this decision introduces no distributed Database Transaction or exactly-once guarantee.

## Shared and Technical Schemas

A generic shared schema SHALL NOT be used as a convenience location for cross-Module business state, unowned tables, common persistence models, or bypasses around Module Contracts.

If a technical or shared schema later becomes necessary, it must have an explicit narrow purpose, identifiable owner, bounded consumers, least-privilege grants, migration ownership, compatibility obligations, and applicable governance. ADR-0017 neither creates nor names such a schema.

## Naming Principle

Existing PostgreSQL and repository naming governance remains in force. Schema names must be deterministic, unquoted lowercase `snake_case`, and traceable to the owning Module or Domain boundary.

This ADR does not define the complete schema-name inventory, introduce arbitrary abbreviations, or override the canonical names and ownership expressed by Approved Domain and Backend Specifications. Concrete names require validation against the implemented Module mapping and collision checks.

## Testing and Enforcement

Implementation of the Accepted strategy must provide objective evidence that includes:

- PostgreSQL integration validation through Testcontainers where practical;
- deterministic creation of every governed schema required by the tested Module set;
- Flyway application to each correct owned schema from its attributable migration boundary;
- detection of duplicate migration identities, ordering ambiguity, ownership violations, and migration collisions;
- validation that ordinary runtime capability cannot perform DDL or schema-administration operations;
- validation that migration capability is bounded to approved schema-changing responsibilities;
- denial tests for unauthorized cross-schema reads and writes where database grants enforce the boundary;
- architecture tests or equivalent automated enforcement preventing Modules from importing or using another Module's persistence mappings, Repositories, migration resources, or internal storage access;
- transaction tests for approved operations spanning owned schemas where such coordination is materially required;
- migration tests from supported prior states and an empty database where applicable; and
- compatibility, partial-failure, recovery or forward-fix, and immutable-history evidence.

Tests must not treat successful technical cross-schema access as authorization. In-memory database substitutes are not sufficient evidence for PostgreSQL schema, grant, Flyway, transaction, locking, or isolation behavior where those semantics matter.

## Evolution Boundary

Dedicated schemas preserve identifiable ownership and migration boundaries that can support later extraction analysis. They do not pre-authorize database-per-Module, a second database service, service extraction, microservices, distributed transactions, replication, or cross-database access.

Any later change to database-per-Module, service extraction, or another material persistence topology requires applicable Architecture governance, migration and compatibility analysis, operational evidence, and an Accepted ADR with synchronized canonical sources.

## Consequences

### Benefits

- Physical namespaces make Module and Domain data ownership more visible.
- Migration ownership and affected-schema review become clearer.
- Grants can be bounded around owned schemas and runtime versus migration capability.
- Accidental unqualified persistence coupling becomes easier to detect and prevent.
- One database preserves approved local PostgreSQL Database Transaction capability across schemas when a governed Use Case requires it.
- Owned schemas provide clearer boundaries for possible future extraction analysis.

### Costs and Trade-offs

- Schema creation, ownership, grants, and migration coordination require explicit management.
- Migration tooling must handle multiple owned schemas without collisions or ambiguous order.
- SQL, persistence tooling, support procedures, and observability must remain aware of schema qualification.
- PostgreSQL `search_path` must be controlled and cannot silently substitute for explicit ownership.
- Integration-test setup must create schemas, apply owned migrations, configure bounded capabilities, and verify denied access.
- Operations and support must understand multiple schema owners inside one application database.
- One database retains a shared operational failure and capacity boundary even though ownership is separated by schema.

These consequences arise from the repository's selected modular-monolith and ownership requirements. They do not claim that schema-per-Module is universally preferable outside this repository context.

## Alternatives Considered

### A. One Shared Application Schema

Not selected because one undifferentiated namespace would rely primarily on code, naming, review, and tests to distinguish ownership. Although permitted by the current Architecture, it provides weaker physical ownership visibility and makes accidental direct table coupling easier than dedicated schemas. The selected strategy preserves the same single-database transaction capability while strengthening namespace and grant boundaries.

### B. Governed Grouped Multi-Schema Containment

Not selected because no canonical grouping rule or ownership evidence establishes which Modules should share a physical schema group. Grouping unrelated owners would make migration ownership and least-privilege grants less direct and could recreate shared-schema coupling within each group. A future ADR may consider a specific grouping only from concrete ownership and operational evidence.

### C. Database per Module or Governed Module Group Within the Primary Service

Not selected for the initial modular monolith because separate databases would remove ordinary single-database atomicity across Module boundaries and add connection, credential, migration, testing, backup, and operational coordination beyond the currently evidenced need. Database-per-Module may be reconsidered through future Architecture governance if extraction, isolation, scale, or operational evidence justifies that change.

## Security Impact

The selected strategy supports least-privilege schema and object grants, separation of runtime and migration capability, attributable privileged changes, and reduced accidental persistence access. It does not replace application and Domain Authorization, default denial, Sensitive Data controls, Encryption in Transit or at Rest, secret management, privileged-access governance, or Audit Record requirements.

Schema separation alone is not a security boundary unless the implemented grants, identities, connection behavior, migration process, tests, and operations enforce it. No provider-specific identity or credential mechanism is selected.

## Data Impact

This ADR assigns physical containment to existing owning Module and Domain boundaries. It creates no table, column, index, constraint, identifier, data type, retention rule, migration, Projection, or new authoritative data set. Persisted state remains authoritative only within ownership established by Product, Architecture, and Approved Domain Specifications.

## Compatibility and Migration Impact

No existing application schema or production data migration is claimed because executable persistence has not been authorized or implemented by this Accepted ADR. Initial implementation must establish the selected schemas and migration boundaries through version-controlled Flyway changes.

Later schema evolution must remain compatible with the approved deployment strategy, use additive or expand-and-contract changes where required, preserve applied migration history, and define evidence-based recovery or forward-fix behavior.

## Operational Impact

Operations must be able to identify schema owners, migration owners, applicable runtime and migration capabilities, migration outcomes, and affected Modules. Database observability, backup, restore, incident response, and support procedures must remain schema-aware where ownership affects diagnosis or recovery.

This ADR does not select backup retention, recovery objectives, high availability, replication, hosting, service tier, provider configuration, or numerical operational targets.

## Explicit Non-Decisions

ADR-0017 does not select or authorize:

- an exact PostgreSQL release or image tag;
- a PostgreSQL driver;
- JPA, Hibernate, another ORM, a SQL mapper, or another database-access library;
- an exact Flyway version;
- a PostgreSQL extension;
- a connection-pool implementation or size;
- concrete schemas, schema-name inventory, tables, columns, indexes, constraints, sequences, SQL, or persistence mappings;
- a universal Primary Key or identifier representation or generation strategy;
- a repository-wide isolation level, locking mechanism, or retry strategy;
- backup retention, RPO, RTO, high availability, replication, or recovery topology;
- hosting service, provider, SKU, infrastructure product, or deployment topology;
- Product Decision 30 or any other unresolved Product policy;
- application features, API routes, HTTP methods or statuses, DTOs, event names or payloads, providers, caches, Redis, messaging, or brokers;
- a timeout, retry count, capacity value, performance target, SLA, SLO, or other numerical threshold;
- database-per-Module, service extraction, microservices, or distributed transactions; or
- direct cross-Module persistence access or a general exception to existing ownership governance.

## Relationship to Future DEC-0002

ADR-0017 owns the physical PostgreSQL schema and ownership Architecture. Future DEC-0002 may select the exact supported PostgreSQL release and bounded non-architectural adoption details supported by repository evidence.

DEC-0002 must inherit an Accepted ADR-0017 and cannot redefine, weaken, or bypass its schema-per-owning-Module/Domain strategy, cross-Module access rules, Flyway ownership, capability separation, transaction boundary, or evolution constraints. ADR-0017 does not itself select executable persistence dependencies, authorize database configuration or migrations, or claim that implementation exists.

## Required Governance Reviews

Acceptance governance recorded completion of:

- Architecture review of the single-application-database and schema-per-owning-Module/Domain strategy;
- affected Domain and Module ownership review of the ownership mapping principle;
- database and PostgreSQL review of schema, Flyway, transaction, naming, and evolution consequences;
- Security review of least privilege, runtime/migration capability separation, Secrets, privileged access, and cross-schema denial;
- Testing review of Testcontainers, migration, privilege, architecture-boundary, and transaction evidence;
- Operations review of schema ownership, migration execution, observability, support, and recovery implications;
- compatibility and migration review confirming no existing implementation or data migration is falsely claimed;
- documentation review of terminology, authority, alternatives, consequences, exclusions, and canonical synchronization; and
- confirmation that exact PostgreSQL release, persistence libraries, operational topology, Product Decision 30, and other excluded decisions remain unresolved.

The completed governance review found no blocker to acceptance. No reviewer identity, signature, ticket, or external approval artifact is asserted by this ADR.

## Acceptance Conditions and Synchronization

This decision was Accepted after:

- governance approved the selected strategy and boundaries through the required reviews;
- review confirmed the strategy resolves former Architecture Open Decision 13 without creating direct cross-Module access or transferring Domain authority;
- review confirmed Flyway ownership, runtime/migration capability separation, transaction consequences, testing, evolution, and explicit exclusions are complete;
- `.ai/core/ARCHITECTURE.md` metadata and Revision History were synchronized, §34 item 13 was resolved, and §40.3 established the accepted schema-per-owning-Module/Domain strategy;
- `.ai/core/DECISIONS.md` metadata, Decision Index, and Revision History indexed ADR-0017 as Accepted;
- `.ai/backend/DATABASE.md` was synchronized where its schema-strategy wording was unresolved; and
- `.ai/backend/POSTGRES.md` was synchronized where its naming, physical schema, `search_path`, Flyway, role, and validation wording depended directly on this decision.

Acceptance synchronization did not modify `PRODUCT.md`, resolve Product Decision 30, create DEC-0002, select the exact PostgreSQL release, or implement database dependencies, configuration, schemas, migrations, or application persistence. `SPRING.md` required no synchronization because its existing migration-execution wording remains compatible with this decision.

Acceptance and canonical synchronization make the selected architecture authoritative. Executable persistence still requires separately governed dependency and implementation work.

## Validation Criteria

Final validation confirms that:

1. metadata is `1.0.0 Accepted`, `authoritative: true`, owner `Architecture`, and scope `persistence-architecture`;
2. the selected strategy is exactly one application PostgreSQL database in the initial primary service with a dedicated schema for each persistence-owning Module or Domain boundary;
3. former Architecture Open Decision 13 is resolved by this Accepted ADR and synchronized Architecture governance;
4. every governed persistent object retains one identifiable owner and schema separation does not replace logical Module, Domain, Port, Adapter, Contract, or Authorization boundaries;
5. physical co-location creates no direct cross-Module access authority and no general exception is introduced;
6. Flyway remains mandatory, migration ownership is attributable, migration boundaries prevent ambiguity and collisions, applied migrations remain immutable, and corrections use governed forward migrations;
7. ordinary runtime capability cannot perform DDL or schema administration and migration capability remains separately bounded by least privilege;
8. single-database cross-schema transaction capability is distinguished from authorization, Contract ownership, External System outcomes, and distributed transactions;
9. shared or technical schemas cannot become unowned convenience boundaries;
10. schema naming is limited to deterministic lowercase `snake_case` traceable to ownership, without inventing a concrete schema inventory;
11. PostgreSQL/Testcontainers, schema creation, Flyway targeting, migration collision, privilege denial, architecture-boundary, unauthorized cross-schema access, and applicable transaction verification expectations are objective and complete;
12. future extraction remains possible but database-per-Module, service extraction, microservices, and distributed transactions are not authorized;
13. benefits, costs, and all considered alternatives are documented in repository-specific terms;
14. every Explicit Non-Decision remains unresolved and no implementation dependency, schema, migration, configuration, provider, topology, or numerical value is selected;
15. ADR-0017's boundary with future DEC-0002 is explicit and prevents DEC-0002 from redefining the schema Architecture;
16. canonical acceptance synchronization is limited to ADR-0017, `ARCHITECTURE.md`, `DECISIONS.md`, `DATABASE.md`, and `POSTGRES.md`; and
17. no executable persistence implementation is falsely claimed by the Accepted decision or synchronized sources.

## Related Documents

- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [TESTING-STANDARDS.md](../../.ai/core/TESTING-STANDARDS.md)
- [ENGINEERING-PRINCIPLES.md](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [DEC-0001 — Backend Build Tool and Dependency Management](../decisions/DEC-0001-backend-build-tool-dependency-management.md)
- [Shared Backend Baseline Specification](../backend/shared/backend-baseline.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-25 | Accepted | Accepted and canonically synchronized one application PostgreSQL database with a dedicated schema for each persistence-owning Module or Domain boundary, preserving ownership, Flyway, least-privilege, transaction, testing, and evolution constraints without claiming implementation. |
| 0.1.0 | 2026-09-25 | Proposed | Proposed one application PostgreSQL database with a dedicated schema for each persistence-owning Module or Domain boundary, preserving governed ownership, Flyway, least-privilege, transaction, testing, and evolution constraints. |
