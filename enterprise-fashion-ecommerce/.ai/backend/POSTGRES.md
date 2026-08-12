---
title: POSTGRES
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: false
review_cycle: Quarterly
---

# PostgreSQL Standards

## 1. Purpose

This document refines PostgreSQL-specific implementation mechanics beneath `.ai/backend/DATABASE.md`. It applies the PostgreSQL baseline established by `.ai/core/ARCHITECTURE.md` without redefining Product behavior, Domain ownership, Database Transaction semantics, Payment truth, Inventory authority, Authorization, or repository governance.

This standard is a lower-level implementation standard. It MUST NOT weaken or silently override an Approved governing source or Accepted Decision Record.

## 2. Scope

This standard applies to PostgreSQL schemas, roles, native types, constraints, indexes, SQL, Flyway migrations, engine behavior, connections, sessions, concurrency, testing, observability, maintenance, replication, backup, and recovery mechanics used by repository-owned systems.

It does not select cloud service configuration, Product behavior, a persistence library, connection-pool implementation, exact PostgreSQL release, deployment topology, retention period, backup schedule, RPO, RTO, or performance threshold.

## 3. Repository Authority

PostgreSQL work MUST follow the Decision Hierarchy in `.ai/core/AGENTS.md`.

- `.ai/core/GLOSSARY.md` governs canonical terminology and repository naming.
- `.ai/core/PRODUCT.md` governs Product behavior and commercial semantics.
- `.ai/core/ARCHITECTURE.md` governs PostgreSQL selection, Architecture, Modules, ownership, and deployment direction.
- `.ai/core/SECURITY-STANDARDS.md` governs mandatory security Requirements and Security Exceptions.
- `.ai/core/TESTING-STANDARDS.md` governs testing Requirements and Testing Exceptions.
- `.ai/core/CODING-STANDARDS.md` governs general implementation quality and Coding Exceptions.
- `.ai/core/ENGINEERING-PRINCIPLES.md` provides decision heuristics within governing constraints.
- `.ai/core/DOCUMENTATION-STANDARDS.md` governs documentation quality and Documentation Exceptions.
- `.ai/core/DECISIONS.md` governs Decision Records and ADRs.
- `.ai/backend/DATABASE.md` governs database ownership, persistence boundaries, Database Transactions, integrity, migrations, and database-level policy.
- `.ai/backend/SPRING.md` governs Spring-specific persistence integration.
- `.ai/backend/JAVA.md` governs Java-specific implementation.

This standard governs only PostgreSQL-specific implementation mechanics within those boundaries.

## 4. Normative Language

- MUST and MUST NOT express mandatory requirements or prohibitions.
- SHOULD and SHOULD NOT express expected practices whose departure requires reviewable justification.
- MAY expresses a permitted option.

These meanings apply in prose, lists, tables, and review criteria.

## 5. PostgreSQL Baseline and Release Selection

PostgreSQL is the Architecture-approved authoritative transactional database family for the initial platform. No repository build, Docker, Testcontainers, CI/CD, infrastructure, Environment configuration, or approved lower-level source currently establishes an exact supported PostgreSQL release or image tag.

An exact release MUST be selected and recorded in an approved source before implementation, testing, or deployment locks the runtime. Selection MUST include compatibility, security support, migration behavior, driver and framework compatibility, Testcontainers evidence, extension compatibility where applicable, backup and restore evidence, operational support, and deployment-platform compatibility.

This standard does not select any exact PostgreSQL release.

## 6. DATABASE.md and POSTGRES.md Boundary

`DATABASE.md` owns platform-neutral database policy, including ownership, persistence Ports and Adapters, Database Transaction principles, integrity, migration governance, concurrency requirements, query correctness, Payment and Inventory safety, and recovery governance.

This document refines PostgreSQL-specific behavior such as native types, MVCC, isolation implementation, locks, SQLSTATE, index implementations, planner evidence, vacuum, sessions, engine timeouts, replication implications, point-in-time recovery, and PostgreSQL-focused tests. A cross-reference to `DATABASE.md` SHOULD be used instead of duplicating its general policy.

## 7. Data and Module Ownership

PostgreSQL objects MUST preserve the authoritative Domain and Module ownership established by Product and Architecture. Sharing one PostgreSQL service does not permit one Module to read or mutate another Module's internal tables.

Physical layout, roles, views, functions, triggers, indexes, and maintenance procedures MUST NOT create hidden cross-Module ownership. Approved Contracts and Ports remain the allowed interaction boundaries.

## 8. Naming

Tables, columns, constraints, and indexes MUST use the canonical `snake_case` convention established by `GLOSSARY.md`. Names MUST be descriptive, stable, and free of ambiguous abbreviations.

Unquoted lowercase identifiers SHOULD be used so PostgreSQL case folding does not create accidental quoted-name dependencies. Index names MUST use the governed `idx_` prefix and identify the table and relevant columns or expression clearly. This document does not invent a universal constraint-name or schema-name pattern.

## 9. Physical Schema Strategy

PostgreSQL schemas MAY be used as an implementation mechanism only after the physical schema ownership strategy is approved through Architecture governance. This document does not select schema-per-Module, one shared schema, database-per-Module, or database-per-service.

The selected strategy MUST preserve identifiable Module and data ownership, Flyway migration ownership, least privilege, operational support, and a credible evolution path. PostgreSQL `search_path` behavior MUST NOT silently substitute for an approved ownership model.

## 10. Identifier Strategy

PostgreSQL supports UUID, identity-backed, sequence-backed, and other governed identifier representations. No representation is universally mandatory or preferred by this document.

Identifier choice MUST follow Domain identity, Architecture, Contract, collision, exposure, migration, indexing, and operational Requirements. Database convenience MUST NOT define Domain identity. Customer-facing identifiers such as Order Number MUST remain distinct from internal Primary Keys where governing sources require that distinction.

If PostgreSQL generates surrogate identifiers, the mechanism MUST be compatible with the selected release and ownership model. Legacy `serial` shorthand is not established as the repository baseline.

## 11. PostgreSQL Type Selection

PostgreSQL types MUST preserve the governed value's range, precision, optionality, comparison, ordering, interoperability, migration, and query semantics. A specialized type MUST NOT be selected solely because PostgreSQL provides it.

Type selection MUST consider Java and Spring mapping, API and event Contracts, Flyway evolution, indexes, backup and restore, test support, and provider compatibility.

## 12. Integer and Exact Numeric Types

`smallint`, `integer`, and `bigint` MAY be used when their verified ranges fit the governed value and expected growth. A wider type SHOULD NOT be used as a substitute for understanding valid range and overflow behavior.

`numeric` or `decimal` MUST be used where exact decimal storage is required. Precision and scale MUST be explicit when the owning Requirement needs bounded representation, but this document does not invent universal values.

## 13. Money and Currency

Authoritative Money MUST use exact decimal representation. `real`, `double precision`, and other floating-point representations MUST NOT store or calculate authoritative monetary values.

Precision, scale, rounding, and permitted range MUST follow the owning Product or Domain Requirements. Currency MUST remain explicit and validated. Persistence MAY use separate columns or another approved representation that preserves the complete Money Value Object semantics; this document does not require one universal physical mapping.

## 14. Boolean, Text, and Governed Value Sets

`boolean` SHOULD represent genuine two-valued concepts. It MUST NOT compress a multi-state lifecycle into a flag that loses governed meaning.

`text` and `varchar` MAY represent textual values. A length limit MUST reflect a Contract, security, storage, or Domain Requirement rather than habit. Governed finite value sets require integrity protection appropriate to their ownership and evolution.

PostgreSQL native `enum` is not the default. It MAY be used only when the value set is sufficiently stable and its migration, compatibility, rollback or forward-fix, mapping, and operational consequences are understood and governed. This document does not invent lifecycle states.

## 15. Temporal Types

Temporal type selection MUST follow the represented semantics:

- `timestamptz` SHOULD represent absolute instants such as persisted system, audit, provider-evidence, and lifecycle event times.
- `date` SHOULD represent a calendar date without time-of-day.
- `time` MAY represent a genuine time-of-day concept without a date.
- `timestamp without time zone` MAY be used only when the Domain explicitly represents a local date and time without an offset or time zone and its ambiguity is governed.

PostgreSQL `timestamptz` represents an instant and normalizes input relative to an offset; it does not retain the originating named time zone. A required named time zone is separate governed data. Customer time-zone behavior is not selected by this document.

## 16. JSON and JSONB

`jsonb` MAY store justified semi-structured data when ownership, validation, query, evolution, indexing, compatibility, and retention Requirements support it. It MUST NOT become an escape hatch around relational modeling or store core authoritative structured Domain state as opaque JSON for convenience.

Materially queried JSONB fields require explicit query, index, migration, validation, and test evidence. `json` MAY be considered only where preserving input representation has an owned requirement. Neither type relaxes Contract validation or Sensitive Data controls.

## 17. Arrays and Binary Data

PostgreSQL arrays MAY be used when they accurately represent an owned value concept and the query and evolution model supports them. They MUST NOT bypass a normalized owned relationship without justification. Material Architecture impact follows ADR governance; ordinary supported use follows applicable implementation governance.

`bytea` MAY store bounded binary data with explicit ownership and security controls. Product and CMS media MUST remain in Architecture-approved object storage rather than PostgreSQL unless a governing Architecture change says otherwise.

## 18. Nullability, Defaults, and Generated Values

`NOT NULL` SHOULD protect required persisted values when compatible with lifecycle and migration sequencing. Null MUST represent real absence or an explicitly governed transitional state, not an unknown implementation shortcut.

Database defaults and generated values MAY provide safe infrastructure behavior, including owned timestamps or identifiers, but MUST NOT hide Product rules, grant Authorization, manufacture Payment success, or create Inventory truth. Generation ownership and clock or sequence behavior MUST be explicit and tested.

## 19. Constraint Responsibilities

PostgreSQL constraints MUST protect database-enforceable structural integrity and concurrent persistence effects where applicable. `NOT NULL`, Foreign Keys, Unique Constraints, and Check Constraints SHOULD be used when they correctly express the owned integrity boundary.

Constraints do not replace Domain behavior, application validation, Product policy, lifecycle coordination, or Authorization. They MUST NOT be described as implementing every Domain invariant merely because they reject some invalid rows.

## 20. Foreign Keys

Foreign Keys SHOULD protect relational references within a database-owned integrity boundary where compatible with Module ownership, lifecycle, retention, migration, and the approved physical schema strategy.

They MUST NOT create unapproved cross-Module persistence coupling. Delete and update actions MUST be deliberate. PostgreSQL does not automatically require an index on every referencing column; such indexes MUST be added when query, delete, update, lock, or operational evidence justifies them.

## 21. Unique and Check Constraints

Unique Constraints MUST protect true database-enforceable uniqueness where racing writes could otherwise create duplicates. Scope, case handling, null semantics, canonicalization, lifecycle, and soft-deletion interaction MUST be explicit.

Check Constraints SHOULD protect database-enforceable structural ranges and value relationships. They MUST NOT reproduce complex Product or Domain workflows in SQL. Cross-system uniqueness and rules outside the PostgreSQL ownership boundary require their owning mechanism.

## 22. Index Selection

Index selection MUST follow operators, query predicates, joins, ordering, data distribution, selectivity, write volume, storage, and maintenance evidence. B-tree commonly supports equality, range, and ordering use cases but is not a universal mandatory default.

GIN, GiST, BRIN, hash, partial, expression, covering, and other supported index capabilities MAY be used when the selected PostgreSQL release, operator behavior, query evidence, write cost, and operational support justify them. An ADR is required only when adoption materially changes Architecture.

## 23. Partial and Expression Indexes

A partial index MUST document its predicate, target queries, data assumptions, and compatibility with prepared or parameterized query behavior. An expression index MUST document the expression, immutability assumptions, collation or locale implications where applicable, and matching query form.

Both MUST account for write cost, storage, statistics, migration, maintenance, and removal evidence. They MUST NOT conceal an unclear data model.

## 24. SQL and Query Behavior

SQL MUST be parameterized for untrusted or variable values. Unsafe string concatenation is prohibited. Parameterization is the safety requirement; this document does not mandate server-side prepared-statement behavior that depends on unresolved driver or framework configuration.

Ordinary production application queries MUST NOT use `SELECT *`. Reads MUST be bounded, selected columns explicit, cardinality understood, and ordering deterministic where a Contract or workflow depends on order. Writes MUST verify expected affected-row counts and concurrency conditions.

## 25. EXPLAIN and Plan Evidence

`EXPLAIN` SHOULD be used for safe plan inspection when query behavior or an index decision requires evidence. Plans MUST be interpreted with representative schema, statistics, parameters, and data distribution.

`EXPLAIN ANALYZE` executes the statement. It MUST be used only in a safe Environment or under deliberate controlled conditions with authorization, bounded impact, and recovery awareness. Particular caution is required for `INSERT`, `UPDATE`, `DELETE`, locking queries, functions with side effects, and production workloads. `BUFFERS` MAY be included when useful and safe.

## 26. Pagination

Pagination MUST follow the governing Contract and correctness, consistency, navigation, and performance needs.

Offset pagination MAY suit bounded or simple result sets but can become costly and shift under concurrent changes. Keyset or cursor pagination MAY provide stable and efficient traversal when a deterministic unique ordering and Contract support it. Neither strategy is globally preferred by this document.

## 27. PostgreSQL MVCC

PostgreSQL multiversion concurrency control provides snapshots and row versions that allow many readers and writers to proceed concurrently. It does not automatically protect business invariants, prevent stale decisions, remove all blocking, or eliminate retries.

Long-running Database Transactions can retain old row versions, delay cleanup, increase table or index growth, and worsen transaction-ID pressure. Code and operations MUST keep Database Transactions focused, observe conflicts and dead tuples, and choose explicit constraint or concurrency controls for owned invariants.

## 28. Isolation Behavior

No repository-wide PostgreSQL isolation level is selected. Isolation MUST be chosen from the behavior supported by the selected release according to the invariant, anomaly risk, contention, and retry model, and MUST be tested against that release.

The implementation MUST NOT assume a default isolation level prevents lost updates, write skew, phantoms, duplicate creation, or stale application decisions. Serializable execution does not remove the need to handle serialization failure safely and retry the complete operation when appropriate.

## 29. Concurrency Control

PostgreSQL constraints, conditional writes, Optimistic Locking, row locks, or another governed mechanism MUST be selected according to the invariant. MVCC and isolation alone MUST NOT be presented as universal concurrency protection.

Concurrency-sensitive operations MUST define conflict detection, failure translation, safe retry, observability, and tests. The selected mechanism MUST preserve Payment, Inventory, Order, Identity, and Authorization correctness where applicable.

## 30. Optimistic Concurrency

Version-based or condition-based writes SHOULD be used where stale updates must be rejected without pessimistic locking. The expected version or state MUST participate in the write predicate, and the affected-row count MUST be checked.

Timestamp-based optimistic locking is not universally required. A conflict MUST cause refreshed evaluation, an explicit failure, or governed reconciliation; it MUST NOT be reported as successful persistence.

## 31. Row and Advisory Locking

PostgreSQL row locking, including `SELECT FOR UPDATE` where supported and appropriate, MAY protect a focused invariant when constraints or Optimistic Locking are insufficient. It is not the universal concurrency model.

Advisory locking is not selected as a default. If used, key derivation, ownership, session-versus-Database Transaction scope, collision risk, timeout, cleanup, failover behavior, and tests MUST be explicit.

Lock scope, order, duration, contention, and timeout behavior MUST be intentional. Database locks MUST NOT be held across External System network calls.

## 32. Deadlocks, Serialization Failures, and Retry

Deadlocks and serialization failures are expected possibilities under concurrency. Relevant operations MUST classify them safely at the persistence Adapter boundary.

Retries MUST be bounded, observable, idempotent or replay-safe, and based on freshly read authoritative state. A retry MUST encompass the correct Database Transaction boundary, respect cancellation and timeout budgets, and MUST NOT blindly replay stale Authorization, Payment Provider evidence, provider calls, or Inventory decisions.

## 33. SQLSTATE and Error Translation

PostgreSQL SQLSTATE class and code MAY inform stable persistence Adapter classification, including integrity, deadlock, serialization, connection, and resource failures. Translation MUST use documented driver behavior and tests against the selected release.

The boundary is PostgreSQL driver failure to persistence Adapter classification to application- or Domain-appropriate failure. Vendor messages, constraint internals, and SQLSTATE codes MUST NOT leak into public or Domain APIs unless an approved Contract explicitly requires a safe representation.

## 34. Flyway Migration Governance

Flyway remains the Architecture-approved migration mechanism. PostgreSQL schema and governed data changes MUST use version-controlled Flyway migrations under the ownership, review, compatibility, immutability, and recovery rules in `DATABASE.md`.

Runtime schema mutation, automatic production schema creation, manual untracked DDL, and editing an applied migration are prohibited. Corrections SHOULD use a new forward migration.

## 35. Transactional and Restricted Migration Operations

PostgreSQL operations whose transactional restrictions vary by command or supported release MUST be explicitly identified. Flyway transaction configuration MUST be set deliberately per migration when an operation cannot run safely in the normal migration Database Transaction.

Such migrations MUST be reviewed, safely sequenced, observable, recoverable or forward-fixable, and tested against the exact supported release. This standard does not hard-code release-sensitive command behavior as a universal rule.

## 36. Expand-and-Contract Evolution

Additive expand-and-contract evolution SHOULD be used when deployment requires mixed application and schema versions. New structures are introduced compatibly, producers and consumers migrate, data is validated, and obsolete structures are removed later.

This approach does not guarantee zero downtime. Compatibility, lock, rewrite, replication, storage, and rollback or forward-fix behavior MUST be assessed for each change.

## 37. Backfills

Material backfills MUST be bounded, observable, restartable or resumable where interruption is possible, and safe under concurrent application activity. Stable selection, progress evidence, idempotency, validation, failure recovery, and stop conditions MUST be defined.

Batch sizes, throttling, scheduling, and maintenance windows MUST be derived from evidence. This document does not invent numerical values.

## 38. Destructive Migrations

Drops, truncation, destructive type changes, irreversible transformations, and bulk deletion require impact analysis, authorization, compatibility sequencing, validated recovery or forward-fix strategy, and backups or restoration evidence where applicable.

A literal rollback script is not required when reversal would be unsafe or impossible. The change MUST instead document a credible recovery or forward-fix path and verification criteria.

## 39. Connection Ownership

The selected framework or persistence Adapter owns acquired PostgreSQL connections and MUST close or release them according to its library semantics. Application and Domain code MUST NOT manually manage framework-owned pooled connections.

Code MUST NOT rely on accidental connection affinity. Failed operations MUST leave connections reusable only after the driver or framework has restored a safe state; otherwise the connection MUST be discarded through the owning mechanism.

## 40. Connection Pooling

This document does not select HikariCP or any other connection-pool implementation. It does not establish pool-size, reserve, lifetime, or acquisition numbers.

Future sizing MUST account for PostgreSQL capacity, provider limits, application replicas, synchronous workload, Background Jobs, administrative and migration needs, failure behavior, and measured operational evidence. Aggregate connection demand MUST remain within approved capacity.

## 41. Session State and Search Path

Session-local state MUST be minimized, explicitly owned, and reset safely before a pooled connection is reused. Application behavior MUST NOT depend on accidental persistence of role, time zone, locale, configuration, temporary objects, or advisory locks across borrowers.

The effective `search_path` MUST be controlled and MUST NOT include untrusted schema resolution. `SECURITY DEFINER` behavior, object qualification, and role changes require focused review. Session configuration MUST NOT bypass the approved physical schema strategy or Authorization boundary.

## 42. Timeouts and Resource Controls

Statement, lock, idle-in-transaction, connection acquisition, and applicable network timeouts MUST have explicit ownership and be configured from operational evidence. A timeout may be enforced by PostgreSQL, the driver, the framework, or another approved boundary according to the behavior being controlled.

No numerical timeout is established here. Connection usage, long-running queries, Database Transaction duration, statement work, temporary storage, maintenance work, and cancellation behavior MUST be bounded and observable according to workload and provider capacity.

## 43. PostgreSQL Extensions

No PostgreSQL extension is selected by this document. An extension requires supported-release compatibility, provenance, security review, migration and upgrade support, operational ownership, backup and restore compatibility, test evidence, and deployment-platform availability.

Adoption that materially changes Architecture, security, portability, operational dependencies, or platform capability requires Architecture governance and an ADR. Adoption within an approved boundary follows applicable implementation and dependency governance.

## 44. Planner Statistics and ANALYZE

Query-plan decisions MUST consider representative statistics and data distribution. PostgreSQL normally collects planner statistics through configured engine maintenance; arbitrary application-managed schedules are not established here.

Manual `ANALYZE`, statistics-target changes, or extended statistics MAY be used from evidence with understood maintenance, planning, and deployment effects. Stale or non-representative statistics MUST be considered before an index or query rewrite is declared effective.

## 45. VACUUM and Autovacuum

PostgreSQL autovacuum SHOULD normally remain enabled. It is engine-managed and MUST NOT be described as an application task that schedules regular autovacuum runs.

Operations MUST monitor vacuum health, dead tuples, table and index growth, transaction-ID pressure, and long-running Database Transactions. Tuning autovacuum or vacuum parameters and using manual `VACUUM` MUST be evidence-driven, compatible with the supported release and provider, and assessed for workload impact. No schedule or threshold is selected here.

## 46. Temporary Tables and Materialized Views

Temporary tables MAY be used for bounded, owned processing when connection/session behavior, cleanup, statistics, memory or disk use, and pooling interaction are understood.

Materialized views are derived data. Their owner, refresh trigger, freshness tolerance, indexes, concurrency behavior, failure recovery, and consumers MUST be explicit. They MUST NOT become authoritative Product, Payment, Inventory, Order, or Authorization truth unless Architecture explicitly assigns that ownership.

## 47. Partitioning

Partitioning MAY be adopted from measured size, lifecycle, maintenance, or query evidence. Partition key, pruning behavior, routing, indexes, constraints, uniqueness implications, retention operations, migration, and operational maintenance MUST be defined and tested.

An ADR is required when partitioning materially changes data or operational Architecture; otherwise it follows applicable implementation governance. This document does not select a partition strategy or threshold.

## 48. PostgreSQL Search

Architecture permits the initial search capability to use PostgreSQL-backed text-search and indexing features. This document does not select a tokenizer, dictionary, language configuration, ranking algorithm, extension, index type, or query shape.

Any implementation MUST remain behind the project-owned search Port, be evidence-driven, and preserve search as a Projection. Search results MUST NOT become authoritative for Product, Price, Inventory, Payment, or Authorization state.

## 49. Replication and Read Replicas

No replication or read-replica topology is selected. If later approved, routing MUST account for replication lag, read-after-write needs, stale reads, failover, recovery, connection behavior, monitoring, and consistency-sensitive operations.

A stale replica MUST NOT make authoritative Payment, Inventory, Authorization, Order-transition, or other freshness-critical decisions. Material topology adoption or change requires Architecture governance and an ADR.

## 50. Backup, Restore, and Point-in-Time Recovery

This document does not invent a backup schedule, retention period, storage tier, RPO, RTO, or recovery topology. Backups MUST be protected as Sensitive Data, access-controlled, encrypted according to approved policy, monitored, and restored at a frequency governed by operational Requirements.

Restore evidence MUST cover integrity, Flyway compatibility, Secrets and keys, application compatibility, and reconciliation with External Systems. Point-in-time recovery MAY be relied upon only when approved infrastructure explicitly configures, protects, monitors, and tests it; PostgreSQL capability alone is not evidence that it is available.

## 51. High Availability and Failover

High-availability topology, failover policy, service level, RPO, and RTO are owned by Architecture and infrastructure governance. This document selects none of them.

Once a topology exists, PostgreSQL-specific client routing, transaction outcome uncertainty, replica consistency, promotion behavior, split-brain prevention, recovery, and reconciliation MUST be documented and tested.

## 52. Database Roles and Least Privilege

PostgreSQL access MUST use least-privilege roles appropriate to runtime, migration, read-only, administrative, backup, monitoring, and break-glass responsibilities where those responsibilities exist. Exact role names and provider mappings are not selected here.

The ordinary application role MUST NOT be a PostgreSQL superuser or own unrestricted administrative capability. Migration and ownership privileges SHOULD be separated from runtime access. Shared human credentials are prohibited, and privileged activity MUST be attributable and auditable.

## 53. TLS and Encryption Boundaries

PostgreSQL network traffic MUST satisfy the governed Encryption in Transit Requirements in `SECURITY-STANDARDS.md`. Certificate, trust, hostname, client-authentication, and provider connection settings MUST follow approved infrastructure and security configuration; this document does not invent a TLS mode.

Encryption at Rest remains an infrastructure and security control. Application-level field encryption or tokenization MAY be used only through an approved design with key ownership, rotation, querying, migration, backup, recovery, and incident behavior.

## 54. Row-Level Security

PostgreSQL row-level security is optional and is not a substitute for application and Domain Authorization. It MUST NOT be enabled casually as an unreviewed second authorization model.

If adopted, policies, table ownership and bypass behavior, Principal mapping, default denial, connection-pool and session-context interactions, administrative paths, migrations, observability, and deny-path tests MUST be explicit. Material security or Architecture impact follows the applicable governance and ADR process.

## 55. Sensitive Data and Prohibited Storage

PostgreSQL design MUST minimize Sensitive Data collection, duplication, exposure, and retention. Secrets, credentials, private keys, full Access Tokens, full Refresh Tokens, and production security material MUST NOT be placed in ordinary application tables, migrations, fixtures, SQL output, or Logs unless a specific approved Identity or Security Requirement governs the data class and its protection.

Raw CVV MUST never be stored or logged. Payment data storage MUST minimize PCI scope. Dumps, backups, replicas, temporary files, query diagnostics, and test data inherit the protection requirements of their source data.

## 56. Audit Records and Database Evidence

PostgreSQL engine logs, statement logs, audit extensions, triggers, and change history are not canonical Audit Records by themselves. Authoritative Audit Records MUST be created through the governed audit mechanism.

Approved database mechanisms MAY contribute evidence, tamper resistance, or change capture where their ownership, access, retention, performance, and completeness are understood. This document does not select an audit architecture or PostgreSQL audit extension.

## 57. PostgreSQL Observability

PostgreSQL observability SHOULD provide safe evidence for connection use and acquisition, query latency, Database Transaction duration and outcomes, lock waits, deadlocks, statement cancellation, errors, vacuum health, table and index growth, replication lag where applicable, backup and restore signals where exposed, and migration execution.

Telemetry MUST use bounded attributes and MUST NOT expose SQL parameters, credentials, Secrets, provider payloads, raw card data, or unnecessary Sensitive Data. No observability vendor or numerical alert threshold is selected here.

## 58. Performance and Tuning

Performance work MUST be evidence-driven and consider query plans, statistics, indexes, schema design, SQL, application access patterns, connection behavior, maintenance, storage, and provider constraints together.

Cache-hit targets, cost parameters, memory values, parallelism, maintenance settings, and engine thresholds MUST NOT be invented. A tuning change MUST record its workload, baseline, Environment, expected effect, trade-offs, verification, rollback or forward-fix behavior, and monitoring.

## 59. Payment Persistence Safety

PostgreSQL implementation MUST preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

Provider-dependent authoritative Payment state requires validated Payment Provider evidence. A Payment Redirect, browser result, client-reported success, unvalidated Callback or Webhook, or Database Transaction commit MUST NOT establish Payment Provider success.

Uniqueness, conditional writes, constraints, locks, and deduplication records MAY protect persistence effects, but duplicate callbacks and retries MUST NOT create duplicate financial or Order effects. Raw CVV storage is prohibited.

## 60. Inventory Persistence Safety

The Inventory Module owns authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state. PostgreSQL constraints, locking, conditional writes, or version checks MAY protect the applicable persistence invariants without transferring Domain ownership to PostgreSQL.

Stock Reservation creation, expiry, release, and finalization MUST preserve Available-to-Sell correctness and prevent overselling under concurrent requests, retries, rollback, and recovery. Client, search, reporting, and projection state MUST NOT become authoritative Inventory truth.

## 61. Idempotency and Deduplication

The Application Service or owning Use Case MUST own idempotent business behavior. PostgreSQL uniqueness, atomic writes, and deduplication records MAY enforce the persistence effect; duplicate mechanisms are not required at every layer when one governed consistency design is sufficient.

Where atomic protection is required, the deduplication record and protected state change MUST share the correct Database Transaction boundary. Key scope, request identity, stored result, expiry if applicable, conflict behavior, replay policy, and ownership MUST be explicit. Incompatible input MUST NOT reuse an unrelated successful result.

## 62. Reconciliation

Reconciliation MUST compare local state with authoritative sources and remain idempotent, observable, authorized, and auditable where required.

Payment reconciliation MUST compare local Payment state with validated Payment Provider evidence. Inventory reconciliation MUST use authoritative Inventory and Stock records together with approved External System evidence where applicable. Client claims, stale replicas, search, and reporting Projections MUST NOT establish corrective truth.

## 63. Outbox Pattern and Message Publication

The Outbox Pattern MUST be used only where the approved Architecture or reliability design requires atomic coordination between owned Database Transaction state and durable Message publication. When required, the outbox record and owned state change MUST be persisted in the same Database Transaction.

In-process Domain Events do not automatically require an Outbox Pattern, and not every Domain Event or Integration Event requires one. Integration Event durability follows approved Architecture and Contracts. Duplicate delivery is expected, consumers MUST protect business effects appropriately, and exactly-once delivery MUST NOT be assumed.

## 64. Failure Handling

PostgreSQL failure handling MUST distinguish validation, integrity, concurrency, timeout, cancellation, connection, resource, migration, and uncertain-commit outcomes where that distinction affects recovery.

Unknown commit or failover outcomes MUST remain uncertain until authoritative state is read or reconciled. Failure handling MUST NOT convert an infrastructure error into Payment success, release Inventory incorrectly, bypass Authorization, or swallow evidence needed for recovery.

## 65. PostgreSQL Testing with Testcontainers

Integration Tests SHOULD use real PostgreSQL through Testcontainers where PostgreSQL behavior matters. The exact image tag or release MUST match the supported release once selected; this standard selects neither.

An in-memory substitute does not prove PostgreSQL types, SQL, constraints, indexes, locks, MVCC, isolation, Flyway, SQLSTATE, planner, or migration behavior. Tests MUST be isolated, reliable, reproducible, CI-compatible, and free of production Secrets and Sensitive Data.

## 66. Migration Testing

Migration tests MUST cover clean application, upgrade from supported states, Flyway validation, compatibility, constraints, data integrity, backfill behavior, and PostgreSQL-specific operations against the supported release.

Where a down migration is not part of the approved strategy, tests MUST verify recovery or forward-fix behavior rather than invent rollback. Restart or resume behavior MUST be tested for interruptible material backfills.

## 67. Concurrency and Integrity Testing

Tests MUST cover applicable write conflicts, Optimistic Locking, row locking, deadlocks, serialization failures, uniqueness races, duplicate requests, bounded retry behavior, and affected-row verification.

Payment tests MUST protect provider-evidence authority and duplicate-effect prevention. Inventory tests MUST protect Stock Reservation and Available-to-Sell correctness and overselling prevention. Concurrent tests SHOULD be reliable and reproducible without claiming deterministic thread scheduling.

## 68. Environment Compatibility

Development, CI, test, staging, and production SHOULD preserve relevant PostgreSQL compatibility: the supported release or approved compatible major, required features and extensions, migration behavior, isolation semantics, SQL behavior, and provider constraints.

Parity does not require identical scale, service tier, topology, data volume, or operational configuration. Any difference that could invalidate evidence MUST be documented and covered by suitable Environment-specific verification.

## 69. Operational Changes

PostgreSQL schema, deployment, extension, parameter, role, maintenance, backup, restore, and topology changes MUST use the applicable change, security, infrastructure, migration, and review governance.

Routine, automated, reversible operations MAY follow an approved runbook and Pipeline. Material Architecture, security, data-ownership, compatibility, or recovery changes require their governing review and evidence; not every routine operation requires an ADR.

## 70. Decision and ADR Governance

Material Architecture Decisions affecting PostgreSQL MUST be recorded through ADR governance and synchronized with `ARCHITECTURE.md` as required. Examples may include a physical schema ownership strategy, material topology, platform capability, or cross-Module persistence boundary.

Implementation decisions within an approved Architecture boundary follow the applicable Decision or dependency process and do not require an ADR solely because they involve PostgreSQL. This document does not create a competing Decision Hierarchy.

## 71. Runtime Errors and Governance Deviations

PostgreSQL, driver, SQLSTATE, and database failures are runtime errors handled through persistence Adapter and application error translation. They are not formal repository Exceptions.

This document creates no `PostgreSQL Exception` or independent waiver process. A deviation MUST use every applicable Coding Exception, Security Exception, Testing Exception, Documentation Exception, Decision Record, ADR, or other governing process according to the Requirement being changed. One formal Exception MUST NOT silently waive another source's mandatory Requirement.

## 72. Prohibited Practices

The following are prohibited:

- unsafe SQL concatenation;
- manual untracked schema mutation or runtime schema creation;
- modification of an applied Flyway migration;
- raw CVV storage or logging;
- Secrets, credentials, tokens, or unnecessary Sensitive Data in migrations, fixtures, SQL output, or Logs;
- arbitrary production `SELECT *` queries;
- unsupported or unreviewed extension adoption;
- treating MVCC or an isolation level as complete application concurrency protection;
- holding database locks across External System calls;
- treating a Database Transaction commit as validated Payment Provider success;
- using stale replicas for freshness-critical Payment, Inventory, Authorization, or Order decisions;
- assuming the Outbox Pattern is required for every Domain Event or Integration Event;
- assuming exactly-once delivery;
- treating PostgreSQL logs as complete Audit Records;
- arbitrary PostgreSQL tuning values without evidence;
- hard-coding an unapproved PostgreSQL release, image tag, service tier, topology, timeout, retention, backup, RPO, RTO, or performance target; and
- using PostgreSQL implementation convenience to bypass Module ownership, a Contract, Authorization, or a Domain invariant.

## 73. Compliance and Review Evidence

Evidence MUST be proportionate to the change and its Risk:

| Concern | Required PostgreSQL Evidence Where Applicable |
| --- | --- |
| Schema or migration | Flyway review, compatibility analysis, lock and rewrite assessment, tested application, and recovery or forward-fix evidence |
| Query or performance | Representative plan, statistics and data assumptions, measured baseline, and regression verification |
| Concurrency or integrity | Constraint, conflict, lock, deadlock, retry, and invariant tests |
| Security or Sensitive Data | Least-privilege review, threat or security review, deny paths, safe telemetry, and approved controls |
| Backup or recovery | Protected backup evidence, restoration test, integrity verification, and reconciliation procedure |
| Architecture | Accepted ADR and synchronized Architecture update where required |
| Payment | Validated Payment Provider evidence, idempotency, duplicate handling, reconciliation, and Audit Records |
| Inventory | Stock Reservation, Available-to-Sell, concurrency, rollback or recovery, and overselling tests |

Review evidence MUST be attributable and current. Vague claims of regular audit or production parity are insufficient.

## 74. Related Documents

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
- `.ai/backend/DATABASE.md`

This document does not treat empty lower-level companion files as authority.

## 75. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-12 | Approved | Promoted the PostgreSQL implementation standard after final governance, database-boundary, release-selection, type-system, integrity, query, concurrency, migration, connection, maintenance, security, Payment, Inventory, testing, observability, operational, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-12 | Draft | Established the initial PostgreSQL implementation standard covering release governance, schemas, types, constraints, indexes, SQL, MVCC, isolation, locking, Flyway migrations, connections, maintenance, security, Payment, Inventory, testing, observability, and operational boundaries. |

## 76. Quality Requirements

This standard MUST remain subordinate to Approved core and backend standards, preserve PostgreSQL as the approved database family, and avoid selecting an exact release or implementation option without repository evidence and applicable governance.

PostgreSQL mechanics MUST preserve Module ownership, Database Transaction and Payment Transaction distinctions, validated Payment Provider evidence, Inventory authority, server-side Authorization, Sensitive Data protection, safe migration, bounded concurrency recovery, and evidence-driven operations.

## 77. Final Validation

Before approval or implementation reliance, reviewers MUST verify:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. PostgreSQL remains the approved database family and no exact release or image tag was invented;
3. no cloud SKU, service tier, topology, schema strategy, universal identifier, extension, persistence library, or connection pool was selected;
4. Money uses exact decimal representation with explicit Currency and no universal precision or scale was invented;
5. temporal types follow their actual semantics and `timestamptz` is not described as retaining a named time zone;
6. native enum, JSONB, arrays, extensions, partitioning, replication, and row-level security remain conditional;
7. defaults and generated values do not hide Product or Domain logic;
8. Foreign Keys and constraints preserve ownership, and Foreign Key indexes remain evidence-driven;
9. index implementation follows operators and query evidence rather than a universal B-tree rule;
10. `EXPLAIN ANALYZE` execution risk is explicit;
11. autovacuum is engine-managed and no arbitrary vacuum or statistics schedule was introduced;
12. MVCC, isolation, locking, conflict handling, and retries remain accurate, bounded, and testable;
13. no arbitrary isolation level, timeout, resource value, retention period, backup schedule, RPO, RTO, or performance target was invented;
14. PostgreSQL-specific migration restrictions are tested against the supported release and recovery may use forward fix;
15. Payment authority requires validated Payment Provider evidence and duplicate financial effects remain prohibited;
16. Inventory remains authoritative for Stock, Stock Reservation, and Available-to-Sell;
17. the Outbox Pattern remains conditional and exactly-once delivery is not assumed;
18. formal Exception governance remains distinct from runtime database errors;
19. PostgreSQL logs are not treated as complete Audit Records;
20. tests use real PostgreSQL where PostgreSQL behavior matters without inventing an image tag;
21. Related Documents contains no self-reference and names only actual governing sources;
22. no unfinished-work marker or placeholder content exists;
23. headings and Markdown tables are valid and sequential; and
24. changes remain limited to `POSTGRES.md`.
