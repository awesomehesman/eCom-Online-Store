---
title: DATABASE
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-08-12
authoritative: false
review_cycle: Quarterly
---

# Database Standards

## 1. Purpose

This document refines database implementation conventions for the repository under the PostgreSQL, modular, security, and data-ownership baseline established by `.ai/core/ARCHITECTURE.md`. It is a lower-level implementation standard subordinate to `.ai/core/`, `.ai/backend/SPRING.md`, and `.ai/backend/JAVA.md` within their owned concerns.

This standard governs database ownership, persistence boundaries, Database Transactions, integrity, migrations, queries, database security, testing, and operational evidence. It MUST NOT redefine Product behavior, Domain ownership, Architecture, API or event Contracts, Java or Spring rules, or PostgreSQL-specific implementation details reserved for a future Approved `.ai/backend/POSTGRES.md`.

## 2. Scope

This standard applies to repository-owned relational schemas, tables, columns, constraints, indexes, queries, migrations, database access code, persistence Adapters, database configuration, test databases, seed and reference data, operational database procedures, and evidence supporting database changes.

It applies to production and non-production Environments. Provider control-plane configuration, cloud service tiers, PostgreSQL engine tuning, extensions, and version-specific SQL remain governed by Architecture, infrastructure governance, and a future Approved PostgreSQL standard where applicable.

## 3. Repository Authority

Database decisions MUST follow the Decision Hierarchy in `.ai/core/AGENTS.md`.

- `.ai/core/GLOSSARY.md` governs canonical terminology and naming.
- `.ai/core/PRODUCT.md` governs Product behavior, commercial truth, and lifecycle semantics.
- `.ai/core/ARCHITECTURE.md` governs PostgreSQL direction, Domain ownership, Modules, dependency direction, consistency boundaries, and deployment direction.
- `.ai/core/SECURITY-STANDARDS.md` governs mandatory security Requirements and Security Exceptions.
- `.ai/core/TESTING-STANDARDS.md` governs verification and Testing Exceptions.
- `.ai/core/CODING-STANDARDS.md` governs repository-wide implementation quality and Coding Exceptions.
- `.ai/core/ENGINEERING-PRINCIPLES.md` provides decision heuristics within governing constraints.
- `.ai/core/DOCUMENTATION-STANDARDS.md` governs documentation quality and Documentation Exceptions.
- `.ai/core/DECISIONS.md` governs durable Decision Records and ADRs.
- `.ai/backend/SPRING.md` governs Spring persistence integration and Database Transaction implementation boundaries.
- `.ai/backend/JAVA.md` governs Java language and type-level implementation.

This Draft MAY refine database-specific implementation consequences but MUST NOT weaken or silently override a higher-authority Requirement or Accepted Decision Record.

## 4. Normative Language

- MUST and MUST NOT express mandatory requirements or prohibitions.
- SHOULD and SHOULD NOT express expected practices whose departure requires reviewable justification.
- MAY expresses a permitted option.

These meanings apply in prose, lists, tables, and review criteria.

## 5. Database Baseline

PostgreSQL is the Architecture-approved authoritative transactional database technology for the platform. The primary transactional persistence implementation MUST remain compatible with PostgreSQL and the approved Azure deployment direction.

This document does not select an exact PostgreSQL release, Azure database SKU, service tier, extension, topology, parameter profile, or PostgreSQL-specific operational implementation. PostgreSQL-specific configuration and version-dependent mechanics belong in a future Approved `.ai/backend/POSTGRES.md`; material Architecture changes and other implementation choices remain subject to their applicable repository governance.

## 6. Database Authority and Product Truth

Persisted state is authoritative only within the ownership assigned by Product and Architecture. A table, view, cache, report, search index, or copied record MUST NOT acquire authority merely because data is stored in PostgreSQL.

Each authoritative data set MUST have one owning Domain or technical capability. Projections, replicas, exports, caches, analytics stores, and search representations MUST remain derived unless governing Architecture explicitly assigns them authority.

## 7. Domain and Module Ownership

Schemas, tables, constraints, migrations, queries, and operational procedures MUST preserve Module and Domain ownership. One Module MUST NOT read or mutate another Module's internal tables merely because they share a database service.

Cross-Module interaction MUST use the owning Module's approved Application Service, Contract, query boundary, Domain Event, or Integration Event. Use of an approved Contract or query boundary is not direct table access. A proposed direct cross-Module table-access pattern that creates or changes a material Architecture exception requires an Accepted ADR and synchronized Architecture update where Architecture permits it.

## 8. Persistence Architecture

Persistence is an outbound infrastructure concern. Domain and application code MUST depend on project-owned Ports rather than database drivers, SQL libraries, ORM types, persistence annotations, or provider control-plane models.

Persistence representations MUST remain distinct from API DTOs and Domain objects where their identity, lifecycle, normalization, mutability, or compatibility semantics differ. Mapping MUST preserve required invariants and remain reviewable.

## 9. Repository and Adapter Boundaries

A Repository Port MUST express Domain or application needs without exposing persistence technology. A persistence Adapter or Spring Repository MAY implement that Port using the selected database-access mechanism.

Repository methods SHOULD describe owned intent rather than expose arbitrary storage operations. Persistence-specific pagination, locking, query, and error types MUST be translated at the Adapter boundary.

The term Code Repository MUST be used for Git; Repository and Spring Repository retain the meanings established by `GLOSSARY.md`.

## 10. Persistence Technology Neutrality

Neither Architecture nor the current build selects JPA, Hibernate, another ORM, a SQL mapper, or a database-access library as mandatory. This document does not select one.

Any adopted mechanism MUST preserve Domain independence, Repository Ports, explicit query behavior, Database Transaction ownership, constraint enforcement, migration ownership, testability, and observability. A persistence-technology choice that changes the approved Architecture baseline requires Architecture governance and an ADR with synchronized governing updates. An implementation choice within an already-approved Architecture boundary follows the applicable Decision and governance process and does not require an ADR solely because it is a persistence choice.

## 11. Data Model Ownership

The owning Domain MUST define the meaning, lifecycle, required invariants, and compatibility obligations of persisted business state. Database design MUST support those rules without inventing competing Product semantics.

Persistence models MUST NOT expose or permit state transitions that bypass the owning Aggregate, Application Service, Authorization, or approval boundary. Database defaults and generated values MUST NOT silently create business meaning not established by a governing source.

## 12. Database Transaction Semantics

A Database Transaction is a database unit of work with defined atomicity and consistency boundaries. It is distinct from a Payment Transaction and MUST always be qualified where ambiguity is possible.

A Database Transaction MUST protect changes that must succeed or fail together to preserve an invariant. Its commit confirms database durability within the configured database guarantees; it does not confirm an external provider outcome, event delivery, cache update, or distributed workflow completion.

## 13. Database Transaction Ownership

Database Transaction boundaries MUST normally be owned by an Application Service or another Architecture-approved Use Case orchestration boundary. Controllers, Domain Entities, and arbitrary Repository helpers MUST NOT define hidden transaction scope.

Database Transactions SHOULD be short, focused, and explicit about rollback behavior. External network or provider calls MUST NOT remain inside long-running Database Transactions where a reliable alternative exists.

Spring-specific `@Transactional` behavior, propagation, and proxy constraints remain governed by `.ai/backend/SPRING.md`.

## 14. Atomicity and Invariants

Related writes MUST be atomic when partial success would violate a governing invariant. Required state, deduplication markers, version checks, and applicable Outbox Pattern records SHOULD be persisted in the same Database Transaction when the approved consistency design requires it.

Atomicity MUST NOT be stretched across External Systems by pretending that a distributed Database Transaction exists. Cross-system workflows require explicit state, idempotency, recovery, and reconciliation.

## 15. Isolation

Database Transaction isolation MUST be selected according to the invariant, read/write pattern, concurrency risk, and verified database behavior. This document does not impose an arbitrary repository-wide isolation level.

Code MUST NOT assume that the configured default prevents lost updates, write skew, phantoms, duplicate creation, or stale decisions. A stricter isolation level MAY be used for an evidenced Use Case, but its contention, retry, and operational effects MUST be understood and tested.

PostgreSQL-version-specific isolation semantics, configuration mechanics, and operational parameters belong in a future Approved `.ai/backend/POSTGRES.md`.

## 16. Concurrency Control

Concurrent operations MUST preserve Domain invariants under racing requests, retries, callbacks, jobs, and Message delivery. The implementation MUST use suitable constraints, conditional writes, Optimistic Locking, explicit locks, or another approved mechanism according to the behavior being protected.

Concurrency failures MUST remain distinguishable from validation, Authorization, provider, and infrastructure failures. A retry MUST re-evaluate state and MUST NOT reuse a stale authorization or business decision blindly.

## 17. Optimistic Locking

Optimistic Locking SHOULD protect concurrent mutable Aggregates where Architecture or measured behavior requires conflict detection. Version checks MUST be included in the actual write condition and a conflict MUST NOT be represented as successful persistence.

Callers MUST define whether a conflict is returned, retried, or reconciled. Automatic retry is permitted only when the complete operation remains safe, idempotent, and based on refreshed authoritative state.

## 18. Pessimistic and Explicit Locking

Pessimistic or explicit database locks MAY be used when an invariant cannot be protected reliably through constraints or Optimistic Locking alone. Lock scope, acquisition order, duration, timeout, contention, and deadlock behavior MUST be documented and tested.

Locks MUST NOT be held across slow computation or External System calls. A lock MUST NOT be used to conceal unclear ownership or an invalid Database Transaction boundary.

## 19. Constraints and Integrity

Database constraints MUST enforce integrity that the database can express reliably, including required values, referential integrity, uniqueness, and valid structural ranges where applicable.

Application validation does not replace database constraints for invariant protection under concurrency. Database constraints do not replace Domain validation, Authorization, lifecycle rules, or Product policy.

Constraint names and failures MUST be stable and diagnosable enough for safe Adapter translation without exposing database internals to clients.

## 20. Referential Integrity

Foreign Keys SHOULD protect durable relationships within an ownership boundary where referential integrity is required. Their update and deletion behavior MUST be deliberate and compatible with lifecycle, retention, and migration rules.

Cross-Domain Foreign Keys MUST NOT create hidden ownership or coupling contrary to Architecture. Their use requires explicit ownership and compatibility review.

## 21. Uniqueness

Unique Constraints MUST protect uniqueness requirements where concurrent operations could otherwise create duplicates. Application pre-checks alone are insufficient.

Canonicalization, case sensitivity, null behavior, scope, and lifecycle effects MUST be defined before a uniqueness rule is implemented. A uniqueness conflict MUST be translated into the correct owned failure rather than treated as an unexpected success or generic server error.

## 22. Identifiers

Business identifiers MUST follow Architecture and applicable Contracts, including UUID-based business identifiers where established. Customer-facing references such as Order Number MUST remain distinct from internal Primary Keys and MUST NOT expose sensitive or guessable database structure.

Identifier generation MUST be collision-safe, concurrency-safe, and owned. Client-provided identifiers are lookup candidates only and MUST NOT establish ownership or Authorization.

This document does not select one universal Primary Key type or generation strategy for every table.

## 23. Schema and Object Naming

Database tables, columns, constraints, and indexes MUST use the canonical `snake_case` convention established by `GLOSSARY.md`. Names MUST be descriptive, stable, and qualified enough to reveal ownership without unnecessary abbreviation.

This document does not select a physical schema-per-Module strategy, global schema name, table prefix, sequence convention, or other ungoverned namespace layout. The ownership strategy is an open Architecture Decision requiring the applicable ADR; PostgreSQL-specific namespace mechanics belong in a future Approved `.ai/backend/POSTGRES.md`.

## 24. Column and Data-Type Design

Column types MUST preserve the range, precision, optionality, and semantics of the governed value. Monetary amounts MUST use exact decimal representation and explicit Currency; authoritative Money MUST NOT use floating-point storage or calculations.

Boolean, numeric, text, temporal, JSON, binary, enum-like, and identifier representations MUST be chosen deliberately. A database-specific type or extension MUST NOT be adopted repository-wide without compatibility, migration, tooling, and operational evidence. PostgreSQL-specific type and extension choices belong in a future Approved `.ai/backend/POSTGRES.md`.

## 25. Nullability and Defaults

Nullability MUST reflect actual optionality rather than implementation convenience. Required persisted state SHOULD use `NOT NULL` when compatible with migrations and lifecycle rules.

Defaults MUST be deterministic, safe, and semantically owned. A default MUST NOT invent Product state, grant access, imply Payment success, create Inventory authority, or hide a missing required value.

## 26. Flyway Ownership

Flyway is the Architecture-approved mechanism for versioned database migrations. Every governed schema or data change MUST be represented through version-controlled Flyway migrations executed by the approved delivery or application integration process.

Runtime schema mutation, automatic production schema creation, and manual untracked schema drift are prohibited. A migration failure MUST block the affected deployment or startup according to the approved delivery strategy.

## 27. Migration Authorship and Review

Each migration MUST have a clear owner, purpose, affected Modules, compatibility assessment, deployment assumptions, data impact, security impact, and verification plan.

Migration review MUST consider locks, duration, table rewrites, index construction, constraint validation, concurrent traffic, mixed application versions, replication or backup effects where applicable, and recovery behavior.

## 28. Applied Migration Immutability

An applied migration MUST NOT be edited to change production history. Corrections SHOULD use a new forward migration.

Exceptional repair of migration metadata or an incorrectly applied migration requires an approved, auditable operational procedure with validated target state, backups where applicable, owner, authorization, and post-repair evidence. It MUST NOT conceal drift.

## 29. Migration Compatibility

Migrations MUST support the approved deployment strategy and any required mixed-version window. Additive, expand-and-contract evolution SHOULD be preferred where application and schema versions may overlap.

Destructive removal or incompatible constraint tightening MUST occur only after all supported consumers and stored data are ready. Compatibility assumptions MUST be documented and tested.

## 30. Migration Rollback and Forward Fix

Every material migration MUST define recovery behavior. A forward fix SHOULD be preferred when reversing the migration would be unsafe, lossy, or incompatible with already-written data.

Rollback MUST NOT be promised when it cannot restore the prior data and application state safely. Where rollback is appropriate, its prerequisites, data consequences, application compatibility, and verification MUST be documented and tested.

This document does not require reversible down migrations for every change.

## 31. Destructive Changes

Drops, truncation, destructive type changes, irreversible transformations, and bulk deletion require explicit approval, impact analysis, validated backups or another recovery mechanism where applicable, staged rollout, observability, and post-change verification.

Destructive targets MUST be resolved explicitly. Broad patterns, unresolved variables, or assumptions about the active database MUST NOT determine destructive scope.

## 32. Data Migrations and Backfills

Data migrations and backfills MUST be deterministic, restartable or resumable where interruption is possible, bounded, observable, and safe under concurrent application activity.

Large or long-running changes SHOULD be separated from startup-critical schema migration when the approved deployment design permits it. Batches MUST have stable selection, progress tracking, validation, failure recovery, and idempotency.

Backfills MUST NOT invent Product values for unknown historical state without Product governance.

## 33. Indexes

Indexes MUST support evidenced query, constraint, ordering, or operational needs. Index design MUST consider selectivity, write cost, storage, maintenance, ordering, partial conditions, and actual access patterns.

Duplicate, unused, or ineffective indexes SHOULD be removed through a governed migration after evidence and risk review. Index creation and removal on material tables MUST consider locking and deployment impact.

No arbitrary index count or universal indexing formula is established by this document.

PostgreSQL-specific index implementations, operator behavior, construction options, and tuning belong in a future Approved `.ai/backend/POSTGRES.md`.

## 34. Query Design

Queries MUST be parameterized, readable, bounded, and explicit about selected and changed columns. Production application queries MUST NOT use `SELECT *`.

Joins, subqueries, common table expressions, aggregation, sorting, and projections MUST preserve ownership and have understood cardinality. Material N+1 behavior, repeated round trips, unbounded reads, and accidental full-table processing MUST be prevented or corrected.

Queries MUST NOT rely on unspecified row ordering.

## 35. Commands and Writes

Insert, update, and delete operations MUST make the intended affected rows and concurrency conditions explicit. Bulk operations MUST define bounds, authorization, auditing, rollback or recovery, and partial-failure behavior.

An update count inconsistent with the expected invariant MUST be handled as a conflict or failure rather than silently ignored. Database writes MUST NOT bypass the owning Use Case merely because direct SQL is convenient.

## 36. Pagination

Large or externally controlled result sets MUST be paginated or otherwise bounded. Cursor Pagination MUST be used where required by an approved API Contract or where stable traversal of changing data is necessary.

Pagination order MUST be deterministic and include a stable tie-breaker. Offset pagination MAY be used for suitable bounded internal Use Cases, but its consistency and cost MUST be understood.

This document does not redefine API pagination Contracts.

## 37. Connection and Resource Management

Database connections, statements, cursors, result streams, and related resources MUST have explicit ownership and bounded lifetimes. Application code MUST release owned resources reliably and MUST NOT close framework-managed resources it does not own.

Connection acquisition, query, lock, and Database Transaction timeouts MUST be configured through approved Environment-specific mechanisms according to operational evidence. This document does not select a connection-pool implementation or numerical limits. PostgreSQL-specific connection parameters and engine configuration belong in a future Approved `.ai/backend/POSTGRES.md`.

Connections MUST NOT be held while waiting for unrelated external work.

## 38. Sensitive Data

Database design MUST minimize collection, persistence, duplication, exposure, and retention of Sensitive Data. Sensitive columns and records MUST be identified sufficiently for access, logging, export, backup, retention, and incident controls.

Secrets, credentials, Access Tokens, Refresh Tokens, Session secrets, raw CVV, private keys, and prohibited payment data MUST NOT be stored in ordinary application tables, migration files, seed data, query logs, or committed fixtures.

Query parameters and results containing Sensitive Data MUST NOT be emitted to Logs or diagnostic artifacts without an approved, minimized, and redacted design.

## 39. Encryption Boundaries

Encryption at Rest and Encryption in Transit MUST follow `SECURITY-STANDARDS.md` and the approved platform Architecture. Database clients MUST validate protected transport and MUST NOT disable certificate or hostname verification.

Application-level or column-level encryption MAY be introduced only for an approved Requirement with defined key ownership, rotation, query implications, migration, recovery, and operational behavior. This document does not select an encryption algorithm, key service integration, or encrypted-column convention.

Encryption MUST NOT be treated as a substitute for least privilege, minimization, Authorization, or retention controls.

## 40. Database Access Security

Human and workload database access MUST be least-privilege, Environment-scoped, attributable, and auditable. Application credentials SHOULD be limited to the operations required by the owning workload and MUST NOT have schema-administration privileges unless explicitly required by the approved migration mechanism.

Production access MUST follow privileged-access, approval, MFA, network, and incident controls in `SECURITY-STANDARDS.md`. Shared human accounts and untracked credentials are prohibited.

Database ownership or administrator access MUST NOT be used for routine application operation.

## 41. Configuration and Secrets

Connection endpoints, database names, credentials, certificates, and operational parameters MUST be externalized and separated by Environment. Secrets MUST use the Architecture-approved secret-management path and MUST NOT be committed to source, migration configuration, examples, or test fixtures.

Missing or invalid critical database configuration MUST fail safely. Production database configuration MUST NOT silently fall back to a local, test, or weaker connection.

## 42. Audit Records

Material administrative, security, Payment, Order, Refund, and Inventory actions MUST produce the Audit Records required by governing sources. An Audit Record MUST capture attributable action, relevant safe identifiers, time, outcome, and approved context without prohibited Sensitive Data.

Database engine logs and change history are not automatically sufficient Audit Records. Audit design MUST preserve tamper resistance, access control, retention, and traceability appropriate to Risk.

Audit records MUST NOT be updated or deleted through ordinary business workflows where that would undermine the required Audit Trail.

## 43. Database Change Auditability

Schema migrations, privileged data corrections, manual production changes, restoration, and security-relevant configuration changes MUST be attributable and linked to approval and execution evidence.

Unavoidable manual changes MUST be recorded, independently verified where Risk requires it, and reconciled back into version-controlled automation. Undocumented drift is prohibited.

## 44. Timestamps

Persisted system timestamps MUST use UTC unless a governing Product Requirement explicitly requires another representation. Timestamp types and precision MUST match the semantic Contract and remain consistent across writes, queries, serialization, and tests.

Creation, update, effective, expiry, provider, and business-event times MUST remain distinct where their meanings differ. Database-generated time and application-provided time MUST have explicit ownership.

This document does not create Customer locale or Time Zone policy.

## 45. Deletion

Hard deletion, Soft Delete, anonymization, archival, and lifecycle closure are distinct operations. The owning Product, privacy, legal, security, and Domain sources MUST determine which behavior applies.

Soft Delete MUST be used only where explicitly required. It MUST define query visibility, uniqueness, referential behavior, restoration, retention, auditing, and eventual disposal. It MUST NOT become a default substitute for a governed retention or deletion policy.

Hard deletion MUST preserve required referential, Audit Record, financial, and regulatory obligations.

## 46. Retention

Every retained data category MUST have an approved purpose and owner. Retention, archival, legal hold, deletion, and anonymization behavior MUST follow Product, privacy, legal, security, and operational governance.

This document does not invent retention periods. Database schema or code MUST NOT make a temporary data copy permanent merely because deletion behavior is absent.

Retention jobs MUST be bounded, idempotent, observable, authorized, and tested against applicable relationships and Audit Record requirements.

## 47. Reference and Seed Data

Reference data required for application correctness MUST have a clear owner, stable identity, Environment behavior, update mechanism, and compatibility policy. Governed reference-data changes SHOULD use migrations or another approved versioned mechanism.

Seed data MUST NOT invent Product configuration, production identities, permissions, payment outcomes, or operational policy. Production seeding MUST be minimal, intentional, auditable, and safe to repeat where repetition is possible.

Sample credentials and production Secrets are prohibited.

## 48. Test Data

Test data MUST be deterministic, isolated, minimal, and valid for the behavior under test. Production Customer data and production Secrets MUST NOT be copied into test fixtures or lower Environments without an approved sanitized process.

Test builders and fixtures MUST not bypass constraints or Domain invariants unless the test intentionally verifies invalid or legacy state. Tests MUST clean up or isolate data according to their execution model.

## 49. Payment Integrity

Database design MUST preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

Provider-dependent authoritative Payment state MUST be persisted only after validated Payment Provider evidence. A Payment Redirect, browser result, client-reported success, or unvalidated Callback or Webhook MUST NOT establish Payment success.

Payment Provider references, evidence metadata, deduplication, lifecycle changes, Payment Transactions, Refund Transactions, Orders, and Audit Records MUST remain traceable and reconcilable where applicable. Raw CVV MUST never be stored or logged.

## 50. Inventory Integrity

The Inventory Module owns authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state.

Stock Reservation creation, expiry, release, and finalization MUST be atomic with related Stock changes where the governing invariant requires it. Constraints, locking, or Optimistic Locking MUST prevent overselling under concurrent operations.

Duplicate or retried operations MUST NOT create, finalize, release, adjust, or move Stock more than intended. Product Catalogue, client, search, reporting, and other projection state MUST NOT become authoritative Inventory truth.

## 51. Idempotency

Duplicate-sensitive commands MUST use the applicable Idempotency Key, provider reference, Unique Constraint, deduplication record, or another approved stable identity to prevent repeated effects.

Idempotency records MUST have explicit scope, owner, request identity or safe fingerprint where applicable, result-reuse semantics, concurrency behavior, and retention governed by the owning Contract. A duplicate with incompatible input MUST NOT reuse an unrelated success.

Idempotency MUST be enforced in the same consistency boundary as the protected effect where required for correctness.

## 52. Reconciliation

Workflows with uncertain, delayed, duplicated, or externally confirmed outcomes MUST persist enough safe state to support reconciliation. Unknown or pending outcomes MUST remain distinguishable from success and failure.

Reconciliation MUST be idempotent, authorized, observable, and supported by Audit Records where required. It MUST use authoritative sources, including validated Payment Provider evidence and Inventory-owned state, rather than client claims or stale projections.

## 53. External Side Effects and Outbox

A Database Transaction commit MUST NOT be treated as proof that an external side effect completed. External calls, Message publication, cache changes, and provider actions require explicit coordination and failure handling.

The Outbox Pattern or another Accepted durable mechanism MUST be used where the approved reliability design requires reliable post-commit publication. Outbox records MUST be persisted in the same Database Transaction as the owned state change when atomic coordination is required.

Detailed Message and Integration Event Contracts remain outside this document.

## 54. Failure Handling

Database failures MUST remain distinguishable where handling differs, including connectivity, timeout, constraint, serialization, deadlock, optimistic conflict, resource exhaustion, migration, and unavailable dependency failures.

Failures MUST NOT be swallowed, converted into ambiguous success, or exposed to clients as raw SQL, schema, constraint, credential, host, or stack information. Adapter translation MUST preserve safe causes and retryability.

Retries MUST be bounded, observable, based on refreshed state where necessary, and safe for the complete Use Case. A failed commit MUST not be assumed to have had no effect when the outcome is uncertain; such cases require verification or reconciliation.

## 55. Database Testing

Database testing MUST comply with `TESTING-STANDARDS.md` and verify behavior that cannot be established reliably through Test Doubles.

Tests MUST cover applicable mappings, queries, constraints, uniqueness, Foreign Keys, indexes, Database Transactions, rollback, locking, Optimistic Locking, concurrent writes, idempotency, migrations, failure translation, and integrity rules.

Unit Tests MAY verify isolated mapping or query-construction logic, but they MUST NOT replace realistic database verification for PostgreSQL-dependent behavior.

## 56. PostgreSQL and Testcontainers

Integration Tests SHOULD use real PostgreSQL through Testcontainers where practical, consistent with Architecture and Approved SPRING.md, so production-representative relational and database behavior is exercised.

An in-memory substitute MUST NOT be used as evidence for PostgreSQL SQL, types, constraints, locking, isolation, Flyway, query plans, or Database Transaction behavior when those semantics matter.

Containerized tests MUST be deterministic, isolated, version-controlled, and compatible with CI. This document does not establish a PostgreSQL image tag, exact release, or engine configuration; compatibility with the supported deployment baseline belongs in a future Approved `.ai/backend/POSTGRES.md` once that baseline is established.

## 57. Migration Testing

Every migration MUST be tested from each supported prior state and against an empty database where applicable. Tests MUST verify Flyway ordering, checksums, schema outcome, data transformations, constraints, compatibility, failure behavior, and repeat execution behavior where relevant.

Material migrations MUST be tested with representative data volume and concurrent-access conditions when locking, duration, or resource impact could affect deployment safety. Tests MUST not invent arbitrary production-scale thresholds.

## 58. Payment and Inventory Testing

Payment database tests MUST verify validated Payment Provider evidence, duplicate Callbacks and Webhooks, Idempotency Keys, concurrent requests, uncertain outcomes, reconciliation, and prevention of duplicate financial or Order effects.

Inventory database tests MUST verify Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, competing checkout operations, expiry, release, rollback, concurrent mutation, and overselling prevention.

Both areas MUST verify Database Transaction failure paths and authoritative ownership, not only successful persistence.

## 59. Observability

Database observability MUST provide safe evidence for availability, connection acquisition, query latency, Database Transaction outcomes, lock contention, deadlocks, resource saturation, replication or backup health where applicable, migration execution, and error categories.

Telemetry MUST use bounded attributes and applicable Correlation IDs without SQL parameter values, credentials, Secrets, raw provider payloads, or unnecessary Sensitive Data.

This document does not select an additional observability vendor or database monitoring product.

PostgreSQL-specific telemetry sources, engine metrics, collection configuration, and tuning signals belong in a future Approved `.ai/backend/POSTGRES.md` and applicable operational governance.

## 60. Performance

Database performance decisions MUST use measured execution evidence, representative data, approved Requirements, SLOs, or capacity evidence. Material queries SHOULD be assessed for access path, cardinality, estimated and actual work, I/O, locking, memory, and result bounds as applicable.

Optimization MUST preserve correctness, Authorization, data freshness, and Domain ownership. Denormalization, materialized views, partitioning, specialized indexes, extensions, and database-specific tuning require evidence and applicable Architecture or PostgreSQL governance.

This document does not establish arbitrary latency, throughput, data-volume, or connection thresholds.

PostgreSQL-specific query-plan interpretation, index implementation, engine parameters, and tuning procedures belong in a future Approved `.ai/backend/POSTGRES.md`.

## 61. Capacity and Growth

Owners MUST consider data growth, index growth, retention, write amplification, connection demand, background work, and operational maintenance for material data sets.

Capacity assumptions MUST be measured and reviewed as Product usage changes. Emergency capacity must not be created by disabling integrity, security, auditing, backup, or retention controls without applicable formal governance.

## 62. Backup and Recovery Boundaries

Platform/Operations owns the approved backup, restore, continuity, and disaster-recovery implementation. Database and application owners MUST define which data and consistency boundaries require recovery and MUST verify application compatibility with restored state.

Backups MUST be protected as Sensitive Data, access-controlled, encrypted according to policy, monitored, and restoration-tested at a frequency governed by operational Requirements. Restore procedures MUST include integrity validation, migration compatibility, Secret and key considerations, and reconciliation with External Systems.

This document does not invent a backup schedule, retention period, RPO, RTO, storage tier, replication topology, or recovery service.

PostgreSQL-specific backup, restore, replication, and engine-recovery mechanics belong in a future Approved `.ai/backend/POSTGRES.md` and applicable infrastructure governance.

## 63. Manual and Administrative Operations

Production data correction, bulk update, repair, export, restore, and privileged inspection require explicit scope, authorization, peer review or Human Approval Gate according to Risk, safe execution, Audit Records, validation, and recovery planning.

Scripts MUST be version-controlled where practical, parameterized safely, bounded to explicit targets, and tested in a representative non-production Environment. Ad hoc console work MUST NOT become an untracked substitute for migrations or owned operational tooling.

## 64. Prohibited Practices

The following are prohibited:

- direct cross-Module table access that bypasses approved ownership;
- unqualified use of Transaction where Database Transaction or Payment Transaction is intended;
- runtime schema mutation or automatic production schema creation;
- manual untracked production schema drift;
- editing an applied migration to rewrite production history;
- unparameterized SQL containing untrusted input;
- production application use of `SELECT *`;
- unbounded externally controlled reads or writes;
- long Database Transactions around External System calls;
- application pre-checks used instead of required Unique Constraints;
- swallowed database failures or ambiguous success;
- raw SQL, credentials, Secrets, or Sensitive Data in external errors or Logs;
- raw CVV or prohibited payment data in the database;
- client-reported Payment success treated as authoritative;
- client, search, report, or projection state treated as authoritative Inventory;
- destructive changes without approved recovery and validation;
- invented retention, backup, isolation, performance, or recovery targets; and
- treating the empty `.ai/backend/POSTGRES.md` as authority.

## 65. Deviations and Exception Governance

This document does not create a Database Exception type or independent waiver process. A deviation MUST document the exact rule, rationale, Risk, scope, owner, compensating controls, and expiry or remediation when temporary.

The applicable Coding Exception, Security Exception, Testing Exception, Documentation Exception, Decision Record, ADR, or other governing process MUST be used according to the concern being changed.

A deviation MUST NOT waive a core Requirement or an applicable SPRING.md or JAVA.md Requirement. Every applicable formal Exception remains required, and a mandatory security Requirement may be waived only through an approved Security Exception.

## 66. Compliance Matrix

| Concern | Governing Source | Database Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `.ai/core/AGENTS.md` | Preserve authority and scope | Decision Hierarchy and diff review |
| Terminology | `.ai/core/GLOSSARY.md` | Use canonical database and Domain terms | Terminology review |
| Product | `.ai/core/PRODUCT.md` | Persist without inventing Product behavior | Requirement and acceptance evidence |
| Architecture | `.ai/core/ARCHITECTURE.md` | Preserve PostgreSQL direction, ownership, and consistency | Architecture tests and review |
| Security | `.ai/core/SECURITY-STANDARDS.md` | Protect access, data, transport, backups, and operations | Security tests, scans, and access review |
| Testing | `.ai/core/TESTING-STANDARDS.md` | Verify realistic PostgreSQL behavior | Integration, migration, concurrency, and recovery evidence |
| Coding | `.ai/core/CODING-STANDARDS.md` | Apply safe SQL, migration, and persistence practices | Static analysis and code review |
| Spring | `.ai/backend/SPRING.md` | Preserve persistence Adapter and Database Transaction boundaries | Spring and Integration Tests |
| Java | `.ai/backend/JAVA.md` | Preserve type, resource, Money, and error semantics | Compilation and Java review |
| Database Transactions | `.ai/core/ARCHITECTURE.md`; `.ai/backend/SPRING.md` | Protect focused atomic invariants | Rollback, locking, and concurrency tests |
| Migrations | `.ai/core/ARCHITECTURE.md`; `.ai/core/CODING-STANDARDS.md` | Use safe versioned Flyway changes | Migration and deployment evidence |
| Payment | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Preserve validated Payment Provider evidence and idempotency | Payment and reconciliation tests |
| Inventory | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Preserve Stock and Stock Reservation authority | Invariant and concurrency tests |
| Authorization | `.ai/core/SECURITY-STANDARDS.md` | Prevent storage access from bypassing policy | Positive, denial, and object-scope tests |
| Operations | `.ai/core/ARCHITECTURE.md`; approved operational governance | Support observable, recoverable database operation | Monitoring, backup, restore, and runbook evidence |
| Exceptions | Applicable governing standard | Use existing Exception governance | Approval, controls, expiry, and remediation |

### Compliance Interpretation

`DATABASE.md` refines database implementation only. Evidence MUST demonstrate implementation and enforcement. This document MUST NOT override core authority, create Product behavior, or treat database capability as permission to bypass Domain ownership or a governing Requirement.

## 67. Related Documents

Approved governing and directly relevant documents:

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`

The following lower-level companion is currently empty and unapproved. It is reserved for the exact PostgreSQL release, version-specific SQL and isolation behavior, engine types and extensions, index implementations, connection and operational parameters, observability, tuning, and engine-level backup or recovery mechanics. It MUST NOT be treated as authority:

- `.ai/backend/POSTGRES.md`

The following lower-level companions are also currently empty and unapproved and remain outside this standard's owned detail:

- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`

## 68. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-08-12 | Draft | Established the initial database implementation standard covering ownership, PostgreSQL direction, persistence boundaries, Database Transactions, integrity, Flyway migrations, queries, security, Payment, Inventory, testing, observability, and recovery governance. |

## 69. Quality Requirements

This Draft MUST preserve PostgreSQL as the Architecture-approved transactional database while remaining a platform-neutral database implementation standard. It MUST NOT invent an exact release, cloud SKU, physical schema layout, extension, ORM, connection pool, isolation default, retention period, backup schedule, RPO, RTO, or performance threshold.

Database rules MUST remain subordinate to core governance, SPRING.md, and JAVA.md; distinguish Database Transaction from Payment Transaction; preserve validated Payment Provider evidence; preserve Inventory authority; enforce security and Authorization; and defer PostgreSQL-specific implementation to a future Approved POSTGRES.md.

## 70. Final Validation

Before approval or implementation reliance, reviewers MUST verify:

1. metadata remains accurate for the document lifecycle;
2. PostgreSQL remains the Architecture-approved transactional database and no exact release or cloud SKU was invented;
3. Domain and Module ownership is preserved across schemas, tables, Repositories, and Adapters;
4. no ORM, JPA, Hibernate, database-access library, connection-pool implementation, or PostgreSQL extension was selected;
5. Database Transaction ownership, atomicity, rollback, isolation, locking, and retry behavior are explicit and evidence-based;
6. Database Transaction and Payment Transaction remain distinct;
7. constraints, identifiers, migrations, indexes, queries, pagination, and resources preserve integrity and compatibility;
8. Flyway remains the canonical migration mechanism and runtime schema mutation remains prohibited;
9. no arbitrary isolation level, retention period, backup schedule, RPO, RTO, or performance threshold was invented;
10. authoritative Payment state requires validated Payment Provider evidence;
11. Inventory remains authoritative for Stock, Stock Reservation, and Available-to-Sell;
12. Sensitive Data, Secrets, payment data, database access, encryption, auditing, and backups remain protected;
13. PostgreSQL and migration behavior is tested realistically through Testcontainers where practical;
14. observability and failure handling remain safe and actionable;
15. empty lower-level companion files are not treated as Approved;
16. no new formal Exception type or waiver mechanism was created; and
17. changes remain limited to DATABASE.md.
