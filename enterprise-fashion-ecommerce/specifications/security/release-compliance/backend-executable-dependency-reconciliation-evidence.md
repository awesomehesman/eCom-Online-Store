---
title: Backend Executable Dependency-Reconciliation Evidence
version: 0.1.0
status: Draft
owner: Engineering — accountable disposition pending
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Backend Executable Dependency-Reconciliation Evidence

## 1. Purpose and Scope

This Draft preserves bounded Engineering technical evidence from the
ARCH-LIC-M2D.6B executable Spring Boot `bootJar` verification. It records the local
artifact identity, bounded reproducibility result, executable structure, exact
packaged dependency inventory, locked-to-packaged reconciliation and packaged
resources.

This record does not grant dependency admission, licensing compatibility, Engineering
approval, Security approval, Platform/DevOps approval, Legal approval, release
readiness or production authorization. It does not select an outbound license,
distribution model, notice mechanism, software bill of materials (SBOM) tool,
deployment provider or release process.

## 2. Governance and Authority

| Governance area | Current status |
| --- | --- |
| [DEC-0047](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md) | **PROPOSED; non-authoritative** |
| Repository outbound license | **UNRESOLVED** |
| Rights-holder authority | **NOT VERIFIED** |
| Engineering disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Qualified Legal/licensing disposition | **PENDING** |
| Production release authorization | **NOT GRANTED** |

Technical verification, accountable approval and release authorization remain
separate states. Successful build or artifact reconciliation does not imply any later
state.

## 3. Verification Environment

| Evidence | Observed value |
| --- | --- |
| Repository branch | `feat/fe-cat-m1-catalogue-api-integration` |
| Source commit | `5247b1d31d5a8b506d68937909eaead31afede10` |
| Assessment date | `2026-11-20` |
| Gradle | `8.14.5` |
| Build JVM | Eclipse Adoptium `21.0.6` |
| Host JVM before override | OpenJDK `23.0.2` |
| Operating system | macOS `26.5.2`, `aarch64` |
| Dependency resolution | Offline, using existing local cache |
| Dependency verification | Strict |
| Lockfile update | None |
| Verification-metadata update | None |

The build used the repository's existing Gradle wrapper, locked graph and verification
metadata. No dependency refresh, dependency update, tool installation, application
execution or external service startup occurred.

## 4. Build Procedure

Both equivalent builds used:

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 21) \
./gradlew --no-daemon --offline \
  --dependency-verification=strict clean bootJar
```

Each invocation completed successfully and executed `clean`, `compileJava`,
`processResources`, `classes`, `resolveMainClassName` and `bootJar`. The procedure
used the same machine and existing dependency cache. It does not establish independent
clean-machine or cross-platform reproducibility.

## 5. Executable Artifact Identity

| Property | Observed value |
| --- | --- |
| Repository-relative path | `enterprise-fashion-ecommerce/backend/build/libs/backend.jar` |
| Filename | `backend.jar` |
| Size | `42,557,193` bytes |
| SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |

The artifact is ignored local build output used for technical evidence. It was not
published, retained as governed release evidence, signed, attested or authorized for
distribution.

## 6. Bounded Reproducibility

Two clean `bootJar` executions produced artifacts with the same size and SHA-256
value. A byte-for-byte comparison succeeded.

| Check | Result |
| --- | --- |
| First build SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |
| Second build SHA-256 | `a2425d1497f3bae338971ab809e2de38b33f1ec865bd28c2ef13d75d987e5653` |
| Byte comparison | **PASS** |
| Bounded same-machine reproducibility | **PASS** |
| Independent clean-machine reproducibility | **NOT VERIFIED** |
| Cross-platform reproducibility | **NOT VERIFIED** |

## 7. Executable Manifest

The executable `META-INF/MANIFEST.MF` records:

| Attribute | Verified value |
| --- | --- |
| `Main-Class` | `org.springframework.boot.loader.launch.JarLauncher` |
| `Start-Class` | `com.enterprise.fashion.ecommerce.EnterpriseFashionEcommerceApplication` |
| `Spring-Boot-Version` | `3.5.16` |
| `Spring-Boot-Classes` | `BOOT-INF/classes/` |
| `Spring-Boot-Lib` | `BOOT-INF/lib/` |
| `Spring-Boot-Classpath-Index` | `BOOT-INF/classpath.idx` |
| `Spring-Boot-Layers-Index` | `BOOT-INF/layers.idx` |
| `Build-Jdk-Spec` | `21` |

The manifest also identifies `Manifest-Version: 1.0` and
`Implementation-Title: backend`. `BOOT-INF/classes/`, `BOOT-INF/lib/`, the classpath
index and the layers index were present. The application was not executed.

## 8. Exact Packaged Dependency Inventory

The executable contains these exact 62 JAR filenames under `BOOT-INF/lib`:

| # | Packaged JAR filename |
| ---: | --- |
| 1 | `HikariCP-6.3.3.jar` |
| 2 | `accessors-smart-2.5.2.jar` |
| 3 | `asm-9.7.1.jar` |
| 4 | `bcprov-jdk18on-1.86.jar` |
| 5 | `classmate-1.7.3.jar` |
| 6 | `content-type-2.2.jar` |
| 7 | `flyway-core-11.7.2.jar` |
| 8 | `flyway-database-postgresql-11.7.2.jar` |
| 9 | `hibernate-validator-8.0.3.Final.jar` |
| 10 | `jackson-annotations-2.21.jar` |
| 11 | `jackson-core-2.21.4.jar` |
| 12 | `jackson-databind-2.21.4.jar` |
| 13 | `jackson-dataformat-toml-2.21.4.jar` |
| 14 | `jackson-datatype-jdk8-2.21.4.jar` |
| 15 | `jackson-datatype-jsr310-2.21.4.jar` |
| 16 | `jackson-module-parameter-names-2.21.4.jar` |
| 17 | `jakarta.annotation-api-2.1.1.jar` |
| 18 | `jakarta.validation-api-3.0.2.jar` |
| 19 | `jboss-logging-3.6.3.Final.jar` |
| 20 | `jcip-annotations-1.0-1.jar` |
| 21 | `json-smart-2.5.2.jar` |
| 22 | `jul-to-slf4j-2.0.18.jar` |
| 23 | `lang-tag-1.7.jar` |
| 24 | `log4j-api-2.24.3.jar` |
| 25 | `log4j-to-slf4j-2.24.3.jar` |
| 26 | `logback-classic-1.5.34.jar` |
| 27 | `logback-core-1.5.34.jar` |
| 28 | `micrometer-commons-1.15.12.jar` |
| 29 | `micrometer-observation-1.15.12.jar` |
| 30 | `nimbus-jose-jwt-9.37.4.jar` |
| 31 | `oauth2-oidc-sdk-9.43.6.jar` |
| 32 | `postgresql-42.7.11.jar` |
| 33 | `slf4j-api-2.0.18.jar` |
| 34 | `snakeyaml-2.4.jar` |
| 35 | `spring-aop-6.2.19.jar` |
| 36 | `spring-beans-6.2.19.jar` |
| 37 | `spring-boot-3.5.16.jar` |
| 38 | `spring-boot-autoconfigure-3.5.16.jar` |
| 39 | `spring-context-6.2.19.jar` |
| 40 | `spring-core-6.2.19.jar` |
| 41 | `spring-data-commons-3.5.13.jar` |
| 42 | `spring-data-jdbc-3.5.13.jar` |
| 43 | `spring-data-relational-3.5.13.jar` |
| 44 | `spring-expression-6.2.19.jar` |
| 45 | `spring-jcl-6.2.19.jar` |
| 46 | `spring-jdbc-6.2.19.jar` |
| 47 | `spring-security-config-6.5.11.jar` |
| 48 | `spring-security-core-6.5.11.jar` |
| 49 | `spring-security-crypto-6.5.11.jar` |
| 50 | `spring-security-oauth2-client-6.5.11.jar` |
| 51 | `spring-security-oauth2-core-6.5.11.jar` |
| 52 | `spring-security-oauth2-jose-6.5.11.jar` |
| 53 | `spring-security-web-6.5.11.jar` |
| 54 | `spring-session-core-3.5.7.jar` |
| 55 | `spring-session-jdbc-3.5.7.jar` |
| 56 | `spring-tx-6.2.19.jar` |
| 57 | `spring-web-6.2.19.jar` |
| 58 | `spring-webmvc-6.2.19.jar` |
| 59 | `tomcat-embed-core-10.1.55.jar` |
| 60 | `tomcat-embed-el-10.1.55.jar` |
| 61 | `tomcat-embed-websocket-10.1.55.jar` |
| 62 | `spring-boot-jarmode-tools-3.5.16.jar` |

Filename inspection alone was not treated as artifact identity. The reconciliation
used SHA-256 comparison with existing cached dependency artifacts where available.

## 9. Locked-to-Packaged Reconciliation

| Category | Count | Verified interpretation |
| --- | ---: | --- |
| Locked `productionRuntimeClasspath` coordinates | 73 | Complete committed application runtime graph. |
| Locked runtime JAR components | 71 | Coordinates expected to have JAR artifacts in dependency evidence. |
| BOM-only coordinates | 2 | POM-only dependency-management components; correctly not packaged as JARs. |
| Packaged JARs hash-matched to locked artifacts | 61 | Each had one exact SHA-256 match in existing cached module artifacts. |
| Locked Spring Boot starter JARs omitted from `bootJar` | 10 | Present in the resolved graph but absent from the executable library directory. |
| Additional build-packaged JAR | 1 | Added during executable packaging and absent from the application runtime lock. |
| Ambiguous hash matches | 0 | No packaged artifact produced multiple cached coordinate matches. |

The two BOM-only coordinates are:

- `org.springframework.boot:spring-boot-dependencies:3.5.16`;
- `com.fasterxml.jackson:jackson-bom:2.21.4`.

The ten locked starter coordinates not packaged in `BOOT-INF/lib` are:

1. `org.springframework.boot:spring-boot-starter-data-jdbc:3.5.16`;
2. `org.springframework.boot:spring-boot-starter-jdbc:3.5.16`;
3. `org.springframework.boot:spring-boot-starter-json:3.5.16`;
4. `org.springframework.boot:spring-boot-starter-logging:3.5.16`;
5. `org.springframework.boot:spring-boot-starter-oauth2-client:3.5.16`;
6. `org.springframework.boot:spring-boot-starter-security:3.5.16`;
7. `org.springframework.boot:spring-boot-starter-tomcat:3.5.16`;
8. `org.springframework.boot:spring-boot-starter-validation:3.5.16`;
9. `org.springframework.boot:spring-boot-starter-web:3.5.16`; and
10. `org.springframework.boot:spring-boot-starter:3.5.16`.

The locked graph describes application dependency resolution. The executable
artifact contains the concrete libraries selected for packaging, omits the ten
starter marker artifacts and adds one executable-packaging tool JAR. The artifact
inventory must therefore be reconciled with, rather than inferred solely from, the
locked graph. This difference is not classified here as compliant or noncompliant.

## 10. Build-Added JAR

| Property | Verified observation |
| --- | --- |
| Filename | `spring-boot-jarmode-tools-3.5.16.jar` |
| SHA-256 | `d05beb46a7eac0f1a06733c75828b190a1cb250076ce03be2213aca4a5c0f03f` |
| Application runtime lockfile | Not present |
| Existing dependency verification metadata | Not present |
| Packaging observation | Added during Spring Boot executable packaging |

This record does not infer artifact provenance, publisher trust, dependency admission,
license compatibility or notice sufficiency. Before complete executable third-party
coverage can be asserted, review must establish:

- exact source and publisher provenance;
- integrity-verification coverage;
- exact licensing evidence;
- notice and attribution treatment; and
- inclusion in the release dependency inventory and evidence manifest.

## 11. Packaged Resources

| Resource | Result |
| --- | --- |
| `BOOT-INF/classes/application.properties` | **INCLUDED** |
| `BOOT-INF/classes/application-local.properties` | **INCLUDED** |
| Flyway migrations V1–V17 | **INCLUDED; all 17 observed** |
| `BOOT-INF/classes/db/migration/README.md` | **INCLUDED** |
| Application classes beneath `BOOT-INF/classes/` | **INCLUDED** |

No configuration values were printed or evaluated. Inclusion of
`application-local.properties` requires explicit Engineering and Security review
before any production release. Engineering must also confirm whether the migration
README is intended release content.

## 12. Evidence Limitations

- Reproducibility was established only on the same machine with the same local
  dependency cache.
- Dependency artifacts used for hash matching were existing mutable local cache
  evidence, not independently retained release evidence.
- No independent builder or provenance attestation exists for the application
  artifact.
- No governed release artifact was retained.
- No release-evidence manifest was generated or retained.
- No backend release notice exists.
- No backend SBOM exists.
- No cross-platform or independently provisioned clean-machine build was performed.
- The application was not executed and runtime behavior was not assessed.
- No production release approval was granted.

## 13. DEC-0047 Gate Mapping

| Gate | Effect of this evidence | Current status |
| --- | --- | --- |
| Gate 5 — Engineering artifact inventory | Materially advances the gate by identifying the executable, its exact packaged libraries, locked-to-packaged differences and packaged resources. Accountable Engineering disposition and remaining completeness evidence are absent. | **INCOMPLETE** |
| Gate 7 — Platform/DevOps notice and SBOM plan | Identifies the exact executable subject and gaps that future notice, SBOM, retention and failure controls must cover. No mechanism or disposition exists. | **INCOMPLETE** |
| Gates 1–4 and 6, 8–10 | Provides no rights-holder, business, Legal, Architecture, Security, `LICENSE`, final-decision or approval disposition. | **INCOMPLETE; unchanged** |

No DEC-0047 gate becomes **MET** because this technical evidence exists.

## 14. Engineering and Security Review Questions

| ID | Review question | Required owner |
| --- | --- | --- |
| BER-01 | Is `backend.jar` the intended executable release-unit boundary, or will a later image or other wrapper become the governed release unit? | Engineering / Architecture / Platform/DevOps |
| BER-02 | Is omission of the ten locked Spring Boot starter artifacts an accepted and reproducibly verified packaging behavior? | Engineering |
| BER-03 | How must `spring-boot-jarmode-tools` be locked, verified, licensed, noticed and represented in release inventory? | Engineering / Security / Platform/DevOps / Legal |
| BER-04 | Must `application-local.properties` be excluded from production packaging, and what test will prevent unintended inclusion? | Engineering / Security |
| BER-05 | Is the migration README intended production artifact content? | Engineering |
| BER-06 | Is same-machine byte reproducibility sufficient, or is independent-builder and cross-platform evidence required? | Engineering / Security / Platform/DevOps |
| BER-07 | What artifact-bound backend notice and SBOM mechanisms are required, and how will completeness be reconciled to both the lockfile and `BOOT-INF/lib`? | Engineering / Security / Platform/DevOps / Legal |
| BER-08 | What provenance, digest, attestation and retention controls are mandatory before this artifact can be promoted? | Security / Platform/DevOps |

All answers and dispositions remain **PENDING**.

## 15. Remaining Evidence and Next Milestone

Remaining evidence includes independent-builder reproducibility if required,
artifact retention, build-added dependency provenance, complete executable licensing
and notice evidence, backend SBOM feasibility, release-manifest generation and every
accountable disposition.

The smallest next milestone is a bounded investigation of
`spring-boot-jarmode-tools-3.5.16.jar` provenance, integrity and licensing. It should
identify the exact artifact source, independently verifiable digest or signature
coverage, publisher and release provenance, distributed license/notice material and
the mechanism by which Spring Boot packaging supplied the JAR. It must not admit the
dependency, select a tool or authorize release.

## 16. References

- [Provider-Neutral Application Release-Evidence Design](provider-neutral-application-release-evidence-design.md)
- [Backend Production Runtime Dependency License Evidence](../dependency-admission/backend-production-runtime-license-evidence.md)
- [DEC-0047 — Repository Outbound Licensing and Distribution Model](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md)
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md)
- [Architecture](../../../.ai/core/ARCHITECTURE.md)
- [Backend build configuration](../../../backend/build.gradle.kts)
- [Backend dependency lock](../../../backend/gradle.lockfile)
- [Backend dependency verification metadata](../../../backend/gradle/verification-metadata.xml)
- [Backend CI workflow](../../../../.github/workflows/backend-gradle-baseline.yml)

## 17. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Recorded bounded executable `bootJar` identity, same-machine reproducibility, manifest, exact packaged dependency and resource inventories, locked-to-packaged reconciliation, build-added JAR gap, pending review questions and explicit non-approval boundaries. |
