# DEC-0001 — Backend Build Tool and Dependency Management Baseline

- **Identifier:** DEC-0001
- **Title:** Backend Build Tool and Dependency Management Baseline
- **Type:** Engineering Practice Decision / Technology Adoption Decision
- **Status:** Proposed
- **Date:** 2026-09-25
- **Owner:** Engineering
- **Supersedes:** Not applicable — this is the first governed repository-wide backend build-tool and dependency-management selection.
- **Superseded By:** Not applicable — this Proposed Decision Record has not been superseded.

## Context

The Approved Architecture governs Java 21 LTS and Spring Boot 3.x for backend implementation. `SPRING.md` requires selection of an exact, currently supported Spring Boot 3.x release compatible with Java 21 before Spring Boot implementation begins. `SPRING.md` and `JAVA.md` intentionally defer Maven-versus-Gradle selection until repository governance establishes the build-tool baseline.

The repository-wide implementation-readiness audit identified the absent exact Spring Boot release and build-tool selection as a blocker to Spring Boot implementation. `DECISIONS.md` version 1.0.19 establishes `specifications/decisions/` and the `DEC-####` namespace for durable general non-Architecture decisions. No existing Maven POM, Gradle build, Gradle Wrapper, or other build manifest establishes a competing backend implementation baseline.

This record therefore proposes the minimum repository-wide backend build-tool and dependency-management baseline needed before implementation. Because its status is Proposed, it records a selection for governance review but does not authorize implementation.

## Evidence

### Spring Boot

- Spring Boot 3.5.16 is the proposed exact Spring Boot 3.x release.
- Spring Boot 3.5.16 is compatible with the governed Java 21 baseline.
- Spring Boot 3.5 is the final minor generation within Spring Boot 3.x.
- The Approved Architecture authorizes Spring Boot 3.x and does not authorize Spring Boot 4.x.
- Spring Boot 3.5 supports the selected Gradle 8.x line.

### Gradle

- Gradle 8.14.5 is the proposed build-tool release and runs on Java 21.
- Gradle 8.14.5 is within the Gradle 8.x range supported by Spring Boot 3.5.
- The Gradle Wrapper can pin the repository build-tool release.
- Gradle provides native dependency locking and dependency verification.
- Gradle supports Java toolchains and BOM/platform-based dependency alignment.

This evidence establishes compatibility and available controls; it does not claim performance, security, or reproducibility guarantees beyond the cited upstream and repository requirements.

## Decision

If this Decision Record becomes Accepted, the repository-wide backend baseline MUST use:

- Gradle as the build tool;
- Gradle 8.14.5 as the exact build-tool release;
- a committed Gradle Wrapper pinned to Gradle 8.14.5, including distribution integrity/checksum validation;
- the Java 21 toolchain;
- Spring Boot 3.5.16 as the exact Spring Boot release, remaining within the governed Spring Boot 3.x generation;
- the Spring Boot Gradle plugin and Gradle-native consumption of the `spring-boot-dependencies` BOM/platform for centralized Spring dependency alignment;
- fixed dependency versions, with dynamic versions and changing modules prohibited unless separately governed;
- Gradle-native dependency locking for applicable resolvable dependency configurations;
- Gradle dependency verification using reviewed checksums and signatures where applicable;
- deterministic and reproducible archive configuration;
- repository CI evidence for dependency inventory and review; and
- the existing Security and CI standards for SCA, SBOM, and provenance requirements.

No unrelated dependency or library is selected by this Proposed decision. Until this record becomes Accepted and its required synchronization is complete, it does not authorize creation of the build configuration or commencement of Spring Boot implementation.

## Alternatives Considered

### Maven 3.9.x

Maven 3.9.x offers conventional Spring Boot integration, a declarative build model, straightforward effective-POM and dependency-tree auditing, strong Spring Boot parent/BOM conventions, and a mature CI ecosystem. It was not selected in this proposal because Maven 3 lacks a direct equivalent to Gradle's comprehensive native resolved dependency lock state; equivalent artifact verification and locking strength would require additional governed configuration or tooling.

### Gradle 8.14.5

Gradle 8.14.5 provides native dependency locking, native dependency verification, Wrapper pinning and integrity controls, Java toolchains, BOM/platform support, and review-visible lock-state changes. Its costs are that executable build scripts permit greater complexity, its DSL and dependency-resolution semantics require disciplined review, reproducibility controls require explicit configuration, and SBOM integration still requires an approved implementation mechanism. Gradle 9 is not selected because the governed Spring Boot 3.5 compatibility evidence supports the Gradle 8.x line.

### Continue Without a Repository-Wide Selection

Continuing without a selection would avoid an immediate tool commitment, but it would leave builds inconsistent and would not satisfy `SPRING.md`'s requirement for an exact supported Spring Boot release before backend implementation. This alternative is therefore rejected.

## Consequences

### Positive

- Backend builds gain a deterministic, centrally governed tool and dependency baseline.
- The exact Spring Boot baseline becomes explicit.
- Java 21 compatibility can be enforced through toolchains.
- Dependency lock changes become review-visible.
- Dependency verification supports existing software supply-chain governance.
- Local and CI tool versions can converge through the committed Wrapper.

### Negative and Cost

- Contributors must understand Gradle build and dependency-resolution semantics.
- Executable build scripts require strong review discipline.
- Lock and verification metadata require ongoing maintenance.
- Upgrades require compatibility and security review.
- Migration will eventually be required when repository governance moves beyond Spring Boot 3.x.

## Security Impact

If Accepted and implemented, Wrapper integrity validation, fixed versions, dependency locking, dependency verification, and CI review evidence strengthen the existing software supply-chain controls. This proposal does not replace or relax the SCA, SBOM, provenance, vulnerability-management, secret-management, or other requirements in `SECURITY-STANDARDS.md` and applicable CI standards. The concrete implementation and its evidence require review before acceptance and use.

## Data Impact

Not applicable — this decision selects build and dependency-management practices and does not define domain data, persistence schemas, retention, migration, or data ownership.

## Compatibility and Migration Impact

Acceptance requires evidence that Java 21, Spring Boot 3.5.16, Gradle 8.14.5, the Spring Boot Gradle plugin, and Gradle-native BOM/platform consumption are mutually compatible. Initial adoption must establish the governed baseline without an existing Maven or Gradle backend build to migrate. Later upgrades or movement beyond Spring Boot 3.x require compatibility, security, and governance review; this record does not authorize Spring Boot 4.x or Gradle 9.

## Operational Impact

Local and CI builds will use the same pinned Wrapper and Java toolchain. Lock files, dependency-verification metadata, Wrapper integrity configuration, reproducibility controls, and dependency inventory evidence become maintained operational assets. Their exact implementation is deferred until this record is Accepted and synchronized.

## Explicit Boundaries and Non-Decisions

This Proposed decision does not:

- authorize Spring Boot implementation while its status remains Proposed;
- authorize Spring Boot 4.x;
- select a PostgreSQL release or resolve Architecture Decision 13;
- resolve session/token strategy, hosting, or infrastructure as code;
- select Payment, Shipping, Notification, Identity, Search, analytics, media, cache, messaging, ORM, mapper, HTTP-client, resilience, or other unrelated providers or libraries;
- resolve Product Decision 30 or any other Open Product or Architecture Decision;
- modify Domain or backend authority;
- authorize a post-BADM Backend Specification;
- alter the Shared Backend Baseline; or
- establish concrete API endpoints, DTOs, database schemas, events, retries, timeouts, TTLs, SLOs, or other implementation policy outside this decision.

## Review and Evidence Requirements

Before this record may become Accepted, it requires:

- Engineering ownership review;
- Architecture consistency review;
- Security and software-supply-chain review;
- Testing and build-compatibility review; and
- Documentation and governance consistency review.

Acceptance evidence MUST verify:

- Java 21, Spring Boot 3.5.16, and Gradle 8.14.5 compatibility;
- centralized Spring dependency alignment through the proposed plugin and BOM/platform approach;
- applicable dependency-locking and dependency-verification behavior; and
- deterministic and reproducible archive expectations.

No review is represented as completed by this Proposed record.

## Acceptance and Synchronization Requirements

If DEC-0001 is later Accepted, the controlled acceptance change MUST synchronize, as applicable:

- DEC-0001 lifecycle status and acceptance evidence;
- the `DECISIONS.md` index and status;
- `.ai/backend/SPRING.md`;
- `.ai/backend/JAVA.md`;
- repository Gradle build configuration;
- the committed Gradle Wrapper and its integrity configuration;
- dependency-locking state;
- dependency-verification metadata;
- reproducibility configuration; and
- applicable CI validation and evidence.

`ARCHITECTURE.md` does not require modification because its Java 21 and Spring Boot 3.x baseline remains unchanged. The Shared Backend Baseline does not require modification. None of these acceptance changes is performed by this Proposed record.

## References

- [Architecture](../../.ai/core/ARCHITECTURE.md)
- [Decisions](../../.ai/core/DECISIONS.md)
- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md)
- [Testing Standards](../../.ai/core/TESTING-STANDARDS.md)
- [Coding Standards](../../.ai/core/CODING-STANDARDS.md)
- [Engineering Principles](../../.ai/core/ENGINEERING-PRINCIPLES.md)
- [Spring Backend Standard](../../.ai/backend/SPRING.md)
- [Java Backend Standard](../../.ai/backend/JAVA.md)
- [Shared Backend Baseline Specification](../backend/shared/backend-baseline.md)
- [Spring Boot system requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html)
- [Gradle compatibility matrix](https://docs.gradle.org/8.14.5/userguide/compatibility.html)
- [Gradle dependency locking](https://docs.gradle.org/8.14.5/userguide/dependency_locking.html)
- [Gradle dependency verification](https://docs.gradle.org/8.14.5/userguide/dependency_verification.html)
