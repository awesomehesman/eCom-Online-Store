# DEC-0001 — Backend Build Tool and Dependency Management Baseline

- **Identifier:** DEC-0001
- **Title:** Backend Build Tool and Dependency Management Baseline
- **Type:** Engineering Practice Decision / Technology Adoption Decision
- **Status:** Accepted
- **Date:** 2026-09-25
- **Owner:** Engineering
- **Supersedes:** Not applicable — this is the first governed repository-wide backend build-tool and dependency-management selection.
- **Superseded By:** Not applicable — this Accepted Decision Record has not been superseded.

## Context

The Approved Architecture governs Java 21 LTS and Spring Boot 3.x for backend implementation. Before this decision was Accepted, `SPRING.md` required selection of an exact, currently supported Spring Boot 3.x release compatible with Java 21 before Spring Boot implementation could begin, and `SPRING.md` and `JAVA.md` deferred Maven-versus-Gradle selection to repository governance.

The repository-wide implementation-readiness audit identified the absent exact Spring Boot release and build-tool selection as a blocker to Spring Boot implementation. `DECISIONS.md` version 1.0.19 establishes `specifications/decisions/` and the `DEC-####` namespace for durable general non-Architecture decisions. No existing Maven POM, Gradle build, Gradle Wrapper, or other build manifest establishes a competing backend implementation baseline.

This record establishes the minimum repository-wide backend build-tool and dependency-management decision needed before implementation. Acceptance authorizes creation and validation of the separate executable implementation baseline; it does not claim that build files, the Gradle Wrapper, dependency locks, verification metadata, reproducibility configuration, or CI evidence already exist. Spring Boot backend implementation remains blocked until that executable baseline and its required validation evidence are complete.

## Evidence

### Spring Boot

- Spring Boot 3.5.16 is the selected exact Spring Boot 3.x release.
- Spring Boot 3.5.16 is compatible with the governed Java 21 baseline.
- Spring Boot 3.5 is the final minor generation within Spring Boot 3.x.
- The Approved Architecture authorizes Spring Boot 3.x and does not authorize Spring Boot 4.x.
- Spring Boot 3.5 supports the selected Gradle 8.x line.

### Gradle

- Gradle 8.14.5 is the selected build-tool release and runs on Java 21.
- Gradle 8.14.5 is within the Gradle 8.x range supported by Spring Boot 3.5.
- The Gradle Wrapper can pin the repository build-tool release.
- Gradle provides native dependency locking and dependency verification.
- Gradle supports Java toolchains and BOM/platform-based dependency alignment.

This evidence establishes compatibility and available controls; it does not claim performance, security, or reproducibility guarantees beyond the cited upstream and repository requirements.

## Decision

The repository-wide backend baseline MUST use:

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

No unrelated dependency or library is selected by this Accepted decision. Acceptance authorizes creation of the executable build baseline in a separate governed change, but Spring Boot backend implementation MUST NOT begin until that baseline and its required validation evidence are complete.

## Alternatives Considered

### Maven 3.9.x

Maven 3.9.x offers conventional Spring Boot integration, a declarative build model, straightforward effective-POM and dependency-tree auditing, strong Spring Boot parent/BOM conventions, and a mature CI ecosystem. It was not selected by this decision because Maven 3 lacks a direct equivalent to Gradle's comprehensive native resolved dependency lock state; equivalent artifact verification and locking strength would require additional governed configuration or tooling.

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

When implemented, Wrapper integrity validation, fixed versions, dependency locking, dependency verification, and CI review evidence strengthen the existing software supply-chain controls. This decision does not replace or relax the SCA, SBOM, provenance, vulnerability-management, secret-management, or other requirements in `SECURITY-STANDARDS.md` and applicable CI standards. The concrete implementation and its evidence require review before backend implementation begins.

## Data Impact

Not applicable — this decision selects build and dependency-management practices and does not define domain data, persistence schemas, retention, migration, or data ownership.

## Compatibility and Migration Impact

Acceptance evidence confirmed that Java 21, Spring Boot 3.5.16, Gradle 8.14.5, the Spring Boot Gradle plugin, and Gradle-native BOM/platform consumption are mutually compatible. Initial adoption must establish the governed baseline without an existing Maven or Gradle backend build to migrate. Later upgrades or movement beyond Spring Boot 3.x require compatibility, security, and governance review; this record does not authorize Spring Boot 4.x or Gradle 9.

## Operational Impact

Local and CI builds will use the same pinned Wrapper and Java toolchain. Lock files, dependency-verification metadata, Wrapper integrity configuration, reproducibility controls, and dependency inventory evidence become maintained operational assets. Their exact implementation is required in a separate implementation-baseline change before Spring Boot backend implementation begins.

## Explicit Boundaries and Non-Decisions

This Accepted decision does not:

- authorize Spring Boot implementation before the separate executable baseline and required validation evidence are complete;
- authorize Spring Boot 4.x;
- select a PostgreSQL release or resolve Architecture Decision 13;
- resolve session/token strategy, hosting, or infrastructure as code;
- select Payment, Shipping, Notification, Identity, Search, analytics, media, cache, messaging, ORM, mapper, HTTP-client, resilience, or other unrelated providers or libraries;
- resolve Product Decision 30 or any other Open Product or Architecture Decision;
- modify Domain or backend authority;
- authorize a post-BADM Backend Specification;
- alter the Shared Backend Baseline; or
- establish concrete API endpoints, DTOs, database schemas, events, retries, timeouts, TTLs, SLOs, or other implementation policy outside this decision.

## Review and Acceptance Evidence

Acceptance review completed successfully for:

- Engineering ownership review;
- Architecture consistency review;
- Security and software-supply-chain review;
- Testing and build-compatibility review; and
- Documentation and governance consistency review.

The completed evidence review verified:

- Java 21, Spring Boot 3.5.16, and Gradle 8.14.5 compatibility;
- centralized Spring dependency alignment through the selected plugin and BOM/platform approach;
- applicable dependency-locking and dependency-verification behavior; and
- deterministic and reproducible archive expectations.

No individual reviewer identity, ticket, signature, or external approval artifact is asserted by this record.

## Acceptance Synchronization and Implementation Sequencing

This acceptance change synchronizes DEC-0001, the `DECISIONS.md` index and history, `.ai/backend/SPRING.md`, and `.ai/backend/JAVA.md`. It establishes the governed decision but does not create or claim completion of executable build artifacts.

A separate implementation-baseline change MUST establish and validate, as applicable:

- repository Gradle build configuration;
- the committed Gradle Wrapper pinned to Gradle 8.14.5 and its distribution/JAR integrity controls;
- Java 21 toolchain enforcement;
- Spring Boot 3.5.16 plugin and BOM/platform alignment;
- dependency-locking configuration and applicable lock state;
- dependency-verification metadata;
- explicit archive and build-output reproducibility configuration; and
- applicable CI dependency, build, security, and review evidence.

Spring Boot backend implementation remains blocked until that executable baseline and its required validation evidence are complete. `ARCHITECTURE.md` does not require modification because its Java 21 and Spring Boot 3.x baseline remains unchanged. The Shared Backend Baseline does not require modification.

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
