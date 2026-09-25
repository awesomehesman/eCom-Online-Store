# DEC-0002 — PostgreSQL Release Baseline

- **Identifier:** DEC-0002
- **Title:** PostgreSQL Release Baseline
- **Type:** Engineering Practice Decision / Technology Adoption Decision
- **Status:** Proposed
- **Date:** 2026-09-25
- **Owner:** Engineering
- **Supersedes:** Not applicable — this is the first governed PostgreSQL release-baseline decision.
- **Superseded By:** Not applicable — this Proposed Decision Record has not been superseded.

## Context

The Approved Architecture governs PostgreSQL as the authoritative transactional database. ADR-0017 (PostgreSQL Schema Strategy) establishes the schema-per-persistence-owning-Module/Domain strategy, Flyway migration ownership, runtime/migration capability separation, and related architectural constraints. ADR-0017 explicitly defers the exact supported PostgreSQL release to a future DEC-0002.

The repository-wide implementation-readiness audit identified the absent exact PostgreSQL release as a prerequisite to establishing an executable PostgreSQL baseline. `DECISIONS.md` governs `specifications/decisions/` and the `DEC-####` namespace for durable general non-Architecture decisions. DEC-0001 establishes the backend build-tool and dependency-management baseline (Java 21, Gradle 8.14.5, Spring Boot 3.5.16).

This record proposes the minimum PostgreSQL major-version baseline decision needed to enable subsequent implementation work. Acceptance would establish the governed major release; it would not claim that executable PostgreSQL dependencies, Testcontainers configuration, Flyway configuration, schemas, migrations, or production infrastructure already exist. Concrete implementation remains blocked until separately governed implementation work demonstrates that the actual BOM-managed dependency combination supports the selected PostgreSQL major.

### PostgreSQL Upstream Facts

As of 2026-09-25:

- PostgreSQL 18 is the current stable major release (first released 2025-09-25; supported until 2030-11-14).
- PostgreSQL 17 was released 2024-09-26 and is supported until 2029-11-08.
- PostgreSQL 16 was released 2023-09-14 and is supported until 2028-11-09.
- PostgreSQL 19 is currently in beta and is not eligible as a production baseline.
- PostgreSQL recommends running the current minor release within the selected supported major.

### Ecosystem Compatibility Evidence

- **pgJDBC:** The pgJDBC driver states compatibility with "PostgreSQL 8.4 and higher using the version 3.0 of the protocol." PostgreSQL 16, 17, and 18 all use the v3.0 wire protocol. Compatibility is expected; exact driver version is governed by Spring Boot BOM alignment.
- **Flyway:** Red Gate documentation explicitly lists PostgreSQL 12 through 18 as supported with Foundational and Advanced capabilities. PostgreSQL 16, 17, and 18 are all supported. Exact Flyway version is governed by Spring Boot BOM alignment.
- **Testcontainers:** The Testcontainers PostgreSQL module supports any valid PostgreSQL Docker image. Official `postgres:16`, `postgres:17`, and `postgres:18` images are available. Exact Testcontainers version is not selected by this decision.
- **Spring Boot 3.5.16:** Spring Boot uses pgJDBC via BOM; pgJDBC protocol compatibility indicates support for PostgreSQL 16, 17, and 18.

This evidence establishes expected compatibility; it does not claim guaranteed support. Actual implementation must verify that the BOM-managed dependency combination supports the selected major.

## Decision

If Accepted, the repository-wide PostgreSQL baseline MUST use:

- **PostgreSQL 18** as the supported PostgreSQL major-version baseline;
- a currently supported PostgreSQL 18 maintenance release in all environments;
- the same PostgreSQL major version across local development, CI, and deployed environments.

### Maintenance Release Policy

- Implementations MUST use a currently supported PostgreSQL 18 maintenance release.
- Supported PostgreSQL 18 maintenance releases MAY be adopted without creating a new General Decision Record solely because the maintenance release changed.
- Implementations SHOULD remain current with supported PostgreSQL 18 maintenance/security releases subject to normal dependency, compatibility, security, testing, and deployment validation.

Maintenance/security upgrades within PostgreSQL 18 still require applicable:

- compatibility validation;
- security validation;
- automated testing;
- migration validation where relevant;
- dependency/build verification; and
- deployment/operational evidence appropriate to the change.

### Major Version Policy

- A transition to PostgreSQL 19 or another PostgreSQL major version requires applicable repository governance and a new or superseding durable decision.
- Pre-release/beta/RC PostgreSQL versions MUST NOT become the production baseline.

### Testcontainers Boundary

PostgreSQL-dependent Testcontainers verification MUST exercise the governed PostgreSQL 18 major baseline once implementation is authorized.

This decision does NOT:

- select an exact Testcontainers library version;
- add a Testcontainers dependency;
- add a container image;
- pin a concrete image tag or digest; or
- claim executable PostgreSQL testing already exists.

## Inherited Authority

This decision explicitly inherits without redefining:

### From ADR-0017 — PostgreSQL Schema Strategy

- One application PostgreSQL database within the initial primary PostgreSQL service.
- Dedicated schema per persistence-owning Module/Domain boundary.
- Physical schema separation does not replace logical ownership.
- Direct unauthorized cross-Module persistence access remains prohibited.
- Flyway migration ownership aligns with persistence ownership.
- Ordinary runtime capability cannot perform DDL/schema administration.
- Migration/schema-changing capability is separately bounded.
- Least privilege.
- No concrete schema inventory established by ADR-0017.
- No executable persistence implementation established by ADR-0017.

### From DEC-0001 — Backend Build Tool and Dependency Management Baseline

- Java 21.
- Gradle 8.14.5.
- Spring Boot 3.5.16.
- Spring Boot 3.x only.
- Gradle-native Spring dependency alignment.
- Dependency locking.
- Dependency verification.
- Reproducible build expectations.

### From Existing Canonical Sources

- PostgreSQL as the Architecture-approved transactional database (`ARCHITECTURE.md`).
- Flyway as the canonical migration mechanism (`ARCHITECTURE.md`, `DATABASE.md`).
- Realistic PostgreSQL verification through Testcontainers where applicable (`TESTING-STANDARDS.md`).
- Existing Security, Testing, Database, and PostgreSQL standards.

## Scope Boundaries

### In Scope

- PostgreSQL major-version baseline selection.
- Maintenance/security release policy within the selected major.
- Environment consistency requirement (local, CI, deployed).
- Testcontainers major-version alignment expectation.

### Explicitly Out of Scope

This decision MUST NOT select or invent:

- exact pgJDBC version or JDBC implementation beyond governed compatibility;
- JPA, Hibernate, ORM strategy, SQL mapper, or database-access library;
- exact Flyway version;
- connection-pool implementation;
- PostgreSQL extensions;
- concrete schema names, schema inventory, tables, columns, indexes, or migrations;
- seed data;
- database credentials, database names, or ports/endpoints;
- global transaction isolation level or locking/retry policy;
- backup schedule, retention period, RPO, or RTO;
- HA topology or replication topology;
- cloud provider, Azure SKU, or service tier;
- container registry, image tag, or image digest;
- production deployment topology;
- numeric performance, SLO, or capacity thresholds; or
- unrelated API, event, cache, messaging, search, payment, or provider decisions.

## Implementation Authority

### Acceptance Would Authorize

If Accepted, this decision would authorize:

- PostgreSQL 18 as the supported PostgreSQL major-version baseline for subsequent implementation work.
- The expectation that implementation-phase dependency additions align with PostgreSQL 18.
- The expectation that Testcontainers-based PostgreSQL verification exercises PostgreSQL 18.

### Acceptance Would NOT Automatically Authorize

Acceptance of this decision alone does NOT automatically authorize:

- Adding pgJDBC, Flyway, or Testcontainers dependencies.
- Selecting ORM, JPA, Hibernate, or persistence library strategy.
- Creating concrete Domain schemas or Flyway migrations.
- Implementing Domain persistence.
- Creating production infrastructure, provider selection, or SKU decisions.
- Deployment or operational activation.

Any executable baseline must separately demonstrate that the actual BOM-managed pgJDBC/Flyway/Testcontainers combination supports PostgreSQL 18 through applicable verification.

## Alternatives Considered

### PostgreSQL 16

- **First released:** 2023-09-14
- **End of support:** 2028-11-09
- **Maturity:** High (~3 years GA)
- **Remaining support:** ~2 years from 2026-09-25

PostgreSQL 16 offers the highest operational maturity with confirmed ecosystem support. It was not selected because its remaining support window is the shortest of the three viable candidates, providing approximately 2 years before requiring a major version upgrade for a greenfield project beginning implementation in late 2026.

### PostgreSQL 17

- **First released:** 2024-09-26
- **End of support:** 2029-11-08
- **Maturity:** High (~2 years GA)
- **Remaining support:** ~3 years from 2026-09-25

PostgreSQL 17 offers a balanced choice between operational maturity and remaining support window. It was not selected because PostgreSQL 18 provides a longer support runway while remaining a supported stable release with confirmed ecosystem compatibility evidence.

### PostgreSQL 18

- **First released:** 2025-09-25
- **End of support:** 2030-11-14
- **Maturity:** Moderate (~1 year GA)
- **Remaining support:** ~4 years from 2026-09-25

PostgreSQL 18 is the current stable release with the longest remaining support window. For a greenfield project with no production PostgreSQL migration history, adopting the current stable release provides approximately 4 years of support before requiring major version governance. Ecosystem evidence indicates Flyway and pgJDBC support. Owner selected PostgreSQL 18 for this repository.

### Exact Maintenance-Release Pinning as Governance Baseline

Pinning an exact maintenance release (e.g., "PostgreSQL 18.1") as the governed baseline would require a new General Decision Record for every security/maintenance upgrade. This approach was rejected because PostgreSQL's versioning model treats minor releases as backward-compatible security and bug fixes. Major-only governance aligns with PostgreSQL's support model and avoids unnecessary governance overhead for routine maintenance.

### Pre-Release PostgreSQL 19

PostgreSQL 19 is currently in beta. Pre-release versions are explicitly excluded because production baselines must not be established on pre-release versions that may contain breaking changes before GA and lack ecosystem tooling support guarantees.

## Consequences

### Positive

- Establishes a concrete PostgreSQL major-version baseline enabling subsequent implementation.
- Aligns with the current PostgreSQL stable release.
- Provides approximately 4 years of upstream support before major version migration is required.
- Major-only governance avoids unnecessary decision overhead for routine maintenance/security releases.
- Confirmed ecosystem compatibility evidence supports implementation-phase dependency verification.
- Greenfield project avoids production migration complexity from an older PostgreSQL major.

### Negative and Cost

- PostgreSQL 18 has approximately 1 year of GA maturity, less than PostgreSQL 16 or 17.
- Implementation must verify that Spring Boot 3.5.16 BOM-managed dependency versions support PostgreSQL 18.
- Future major version transition (e.g., to PostgreSQL 19) will require governance and migration.
- Contributors must understand PostgreSQL versioning and maintenance release policy.

### Risks

- BOM-managed Flyway or pgJDBC versions could theoretically lag PostgreSQL 18 support; implementation verification is required.
- Azure Database for PostgreSQL availability for version 18 should be confirmed during infrastructure decisions.

## Security Impact

PostgreSQL 18 maintenance releases include security fixes. The maintenance release policy permits timely adoption of security releases without governance overhead beyond normal validation. This decision does not replace or relax the security requirements in `SECURITY-STANDARDS.md`, `DATABASE.md`, `POSTGRES.md`, or applicable infrastructure and deployment standards.

## Data Impact

This decision selects the PostgreSQL major-version baseline. It does not define domain data, persistence schemas, retention, migration, concrete tables, or data ownership. ADR-0017 governs schema-per-Module/Domain strategy; domain specifications govern concrete persistence.

## Compatibility and Migration Impact

This is a greenfield baseline selection with no existing production PostgreSQL to migrate. Ecosystem compatibility evidence indicates pgJDBC and Flyway support PostgreSQL 18; implementation must verify the specific BOM-managed versions. Future movement to PostgreSQL 19 or another major requires applicable governance.

## Operational Impact

Local development, CI, and deployed environments will use the same PostgreSQL 18 major version. Maintenance release currency is an operational responsibility within the governance boundary. Infrastructure decisions (hosting, Azure SKU, backup, HA) are not determined by this decision.

## Explicit Boundaries and Non-Decisions

This Proposed decision does not:

- authorize PostgreSQL implementation before acceptance;
- authorize persistence libraries, ORM, or connection-pool selection;
- create schemas, migrations, or domain persistence;
- establish infrastructure, hosting, or provider decisions;
- modify ADR-0017, DEC-0001, or the Approved Architecture baseline;
- resolve unrelated Open Decisions; or
- claim that PostgreSQL 18 support is verified beyond current ecosystem evidence.

## Proposed-State Constraints

Because this decision remains Proposed:

- PostgreSQL 18 MUST NOT be described as an Accepted repository baseline.
- `DATABASE.md` and `POSTGRES.md` MUST continue to state that the exact release is unresolved.
- No executable implementation authority exists from this Proposed record.
- No canonical source other than the required Proposed Decision Index registration is synchronized to PostgreSQL 18.

## References

- [Architecture](../../.ai/core/ARCHITECTURE.md)
- [Decisions](../../.ai/core/DECISIONS.md)
- [ADR-0017 — PostgreSQL Schema Strategy](../adr/ADR-0017-postgresql-schema-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](./DEC-0001-backend-build-tool-dependency-management.md)
- [Database Standard](../../.ai/backend/DATABASE.md)
- [PostgreSQL Standard](../../.ai/backend/POSTGRES.md)
- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md)
- [Testing Standards](../../.ai/core/TESTING-STANDARDS.md)
- [Spring Backend Standard](../../.ai/backend/SPRING.md)
- [Java Backend Standard](../../.ai/backend/JAVA.md)
- [PostgreSQL versioning policy](https://www.postgresql.org/support/versioning/)
- [Flyway supported databases](https://documentation.red-gate.com/flyway/getting-started-with-flyway/system-requirements/supported-databases-and-versions)
- [pgJDBC compatibility](https://jdbc.postgresql.org/documentation/)
