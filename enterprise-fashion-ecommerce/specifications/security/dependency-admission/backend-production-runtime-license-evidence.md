---
title: Backend Production Runtime Dependency License Evidence
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# Backend Production Runtime Dependency License Evidence

## 1. Purpose and Non-Approval Boundary

This Draft records reproducible technical license evidence for the exact backend
production runtime dependency graph locked at repository commit `3e55f53`. It
distinguishes effective Maven Project Object Model (POM) declarations from license,
notice and attribution material distributed inside the corresponding Java archive
(JAR) files.

This record provides no Legal advice, compatibility approval, outbound-license
selection, dependency admission, Security acceptance, release approval or production
authorization. Package metadata and distributed text are technical evidence only.
Accountable repository-owner, Legal/licensing, Engineering, Security and
Platform/DevOps dispositions remain separate.

| Decision or evidence area | Current status |
| --- | --- |
| License compatibility | **NOT VERIFIED** |
| Repository outbound license | **UNRESOLVED** |
| Distribution model | **UNRESOLVED** |
| Engineering disposition | **PENDING** |
| Legal/licensing disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Production release authorization | **NOT GRANTED** |
| DEC-0047 | **PROPOSED; not accepted by this record** |
| NgRx dependency admission | **NOT GRANTED** |
| ADR-0024 | **PROPOSED; not accepted by this record** |
| Angular Standards v1.1.0 | **DRAFT; not promoted by this record** |
| Frontend vulnerability triage | **OPEN** |

## 2. Evidence Baseline

Evidence was inspected on 2026-11-20 in the original repository workspace without
resolving or downloading dependencies.

| Evidence | Observed value |
| --- | --- |
| Repository branch | `feat/fe-cat-m1-catalogue-api-integration` |
| Repository commit | `3e55f53` |
| Assessment date | `2026-11-20` |
| Committed Gradle wrapper | `8.14.5`, with a committed distribution SHA-256 |
| Governed Java toolchain | Java `21` |
| Locally active JDK | OpenJDK `23.0.2`; observation only, not the governed toolchain |
| Dependency repository | Maven Central only |
| Dependency evidence | Existing Gradle module cache POM and JAR files |
| Dependency downloads | None |
| Build, lock or verification changes | None |

Gradle wrapper execution was not performed because the wrapper attempted to create a
Gradle-cache `.zip.lck` file. That write was incompatible with the read-only
assessment. The committed wrapper properties identify Gradle `8.14.5`; the backend
build and CI independently require Java 21.

The existing cache is transient local evidence, not retained governed release
evidence. No conclusion below depends on downloading missing material: unavailable
evidence would have been recorded as **NOT VERIFIED**.

## 3. Build and Lockfile Scope

The assessed configuration is the exact `productionRuntimeClasspath` recorded in the
committed [`gradle.lockfile`](../../../backend/gradle.lockfile). The build locks every
configuration and declares one Spring Boot dependency-management platform, eight
direct `implementation` JAR dependencies and two direct `runtimeOnly` JAR
dependencies.

| Graph category | Count |
| --- | ---: |
| Locked `productionRuntimeClasspath` coordinates | 73 |
| Runtime JAR components | 71 |
| POM-only Bill of Materials (BOM) components | 2 |
| Direct runtime JAR dependencies | 10 |
| Direct platform/BOM dependencies | 1 |
| Transitive coordinates, including one transitive BOM | 62 |

A BOM is dependency-management metadata and is not counted as a runtime JAR. The two
POM-only components are `org.springframework.boot:spring-boot-dependencies:3.5.16`
and `com.fasterxml.jackson:jackson-bom:2.21.4`.

## 4. Graph Derivation

The inventory was derived by selecting every non-comment lockfile entry whose
configuration list contains the exact token `productionRuntimeClasspath`. Each
selected `group:artifact:version` was then matched to the existing Gradle cache at:

```text
~/.gradle/caches/modules-2/files-2.1/<group>/<artifact>/<version>/
```

Direct classifications come from explicit declarations in
[`build.gradle.kts`](../../../backend/build.gradle.kts). Versions omitted from those
declarations were reconciled to the exact versions in the committed lockfile.
Coordinates not explicitly declared by the build are classified as transitive. The
direct Spring Boot BOM is classified separately from direct runtime JARs.

This procedure establishes graph membership and direct-versus-transitive status. It
does not reconstruct the complete dependency edge or selection-reason graph because
Gradle resolution was deliberately not executed. No coordinate was silently omitted.

## 5. Complete Runtime Inventory

In this table, “inherited” means that the effective declaration was obtained from an
exact locally cached parent POM. “L” means a conventionally named embedded license
resource was observed. “N” means a conventionally named `NOTICE`, `DEPENDENCIES` or
other attribution resource was observed. A dash records that no such conventionally
named resource was found; it is not a finding that the component is unlicensed or
non-compliant.

| Exact coordinate | Classification | Effective POM license declaration | Inherited | Embedded license | NOTICE / attribution | Nested, compound or limiting evidence |
| --- | --- | --- | --- | --- | --- | --- |
| `ch.qos.logback:logback-classic:1.5.34` | Transitive JAR | EPL-2.0; LGPL-2.1 | Yes | — | — | Alternative/compound parent declaration; no conventional embedded legal resource |
| `ch.qos.logback:logback-core:1.5.34` | Transitive JAR | EPL-2.0; LGPL-2.1 | Yes | — | — | Alternative/compound parent declaration; no conventional embedded legal resource |
| `com.fasterxml.jackson.core:jackson-annotations:2.21` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `com.fasterxml.jackson.core:jackson-core:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `com.fasterxml.jackson.core:jackson-databind:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | JAR additionally records TOML MIT and JFlex BSD-3-Clause material |
| `com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | Yes | L | N | Parent POM and distributed Apache text align |
| `com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | Yes | L | N | Parent POM and distributed Apache text align |
| `com.fasterxml.jackson.module:jackson-module-parameter-names:2.21.4` | Transitive JAR | The Apache Software License, Version 2.0 | Yes | L | N | Parent POM and distributed Apache text align |
| `com.fasterxml.jackson:jackson-bom:2.21.4` | Transitive BOM | Apache License, Version 2.0 | No | No JAR | No JAR | POM-only dependency-management component |
| `com.fasterxml:classmate:1.7.3` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Distributed text identifies Apache 2.0 |
| `com.github.stephenc.jcip:jcip-annotations:1.0-1` | Transitive JAR | Apache License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `com.nimbusds:content-type:2.2` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `com.nimbusds:lang-tag:1.7` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `com.nimbusds:nimbus-jose-jwt:9.37.4` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `com.nimbusds:oauth2-oidc-sdk:9.43.6` | Transitive JAR | Apache License, version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `com.zaxxer:HikariCP:6.3.3` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `io.micrometer:micrometer-commons:1.15.12` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | Distributed Apache and notice material observed |
| `io.micrometer:micrometer-observation:1.15.12` | Transitive JAR | The Apache Software License, Version 2.0 | No | L | N | Distributed Apache and notice material observed |
| `jakarta.annotation:jakarta.annotation-api:2.1.1` | Transitive JAR | EPL 2.0; GPL2 with Classpath Exception | No | L | N | Distributed SPDX expression is `EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0` |
| `jakarta.validation:jakarta.validation-api:3.0.2` | Transitive JAR | Apache License 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `net.minidev:accessors-smart:2.5.2` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `net.minidev:json-smart:2.5.2` | Transitive JAR | The Apache Software License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `org.apache.logging.log4j:log4j-api:2.24.3` | Transitive JAR | Apache-2.0 | No | L | N | JAR also contains `META-INF/DEPENDENCIES`; it must not be discarded |
| `org.apache.logging.log4j:log4j-to-slf4j:2.24.3` | Transitive JAR | Apache-2.0 | No | L | N | JAR also contains `META-INF/DEPENDENCIES`; it must not be discarded |
| `org.apache.tomcat.embed:tomcat-embed-core:10.1.55` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Distributed license records separately licensed schema/subcomponent material, including CDDL and EPL terms |
| `org.apache.tomcat.embed:tomcat-embed-el:10.1.55` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Distributed Apache and notice material observed |
| `org.apache.tomcat.embed:tomcat-embed-websocket:10.1.55` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Distributed Apache and notice material observed |
| `org.bouncycastle:bcprov-jdk18on:1.86` | Direct JAR | Bouncy Castle Licence | No | L | — | Distributed `META-INF/LICENSE.md` contains the exact Bouncy Castle MIT-style terms |
| `org.flywaydb:flyway-core:11.7.2` | Direct JAR | Apache License, Version 2.0 | Yes | L | — | Parent POM and distributed Apache text align |
| `org.flywaydb:flyway-database-postgresql:11.7.2` | Direct runtime JAR | Apache License, Version 2.0 | Yes | — | — | POM-only license evidence for the runtime JAR |
| `org.hibernate.validator:hibernate-validator:8.0.3.Final` | Transitive JAR | Apache License 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `org.jboss.logging:jboss-logging:3.6.3.Final` | Transitive JAR | Apache License 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `org.ow2.asm:asm:9.7.1` | Transitive JAR | BSD-3-Clause | No | — | — | POM-only license evidence for the runtime JAR |
| `org.postgresql:postgresql:42.7.11` | Direct runtime JAR | BSD-2-Clause | No | L | — | JAR includes separate BSD-3-Clause files for four bundled OnGres components |
| `org.slf4j:jul-to-slf4j:2.0.18` | Transitive JAR | MIT | Yes | L | — | Parent POM and embedded MIT text align |
| `org.slf4j:slf4j-api:2.0.18` | Transitive JAR | MIT | Yes | L | — | Parent POM and embedded MIT text align |
| `org.springframework.boot:spring-boot-autoconfigure:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-dependencies:3.5.16` | Direct BOM | Apache License, Version 2.0 | No | No JAR | No JAR | POM-only dependency-management component |
| `org.springframework.boot:spring-boot-starter-data-jdbc:3.5.16` | Direct JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-jdbc:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-json:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-logging:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-oauth2-client:3.5.16` | Direct JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-security:3.5.16` | Direct JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-tomcat:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-validation:3.5.16` | Direct JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter-web:3.5.16` | Direct JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot-starter:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.boot:spring-boot:3.5.16` | Transitive JAR | Apache License, Version 2.0 | No | L | N | POM and distributed evidence align |
| `org.springframework.data:spring-data-commons:3.5.13` | Transitive JAR | Apache License, Version 2.0 | Yes | L | N | Parent POM and distributed evidence align |
| `org.springframework.data:spring-data-jdbc:3.5.13` | Transitive JAR | Apache License, Version 2.0 | Yes | — | — | POM-only license evidence for the runtime JAR |
| `org.springframework.data:spring-data-relational:3.5.13` | Transitive JAR | Apache License, Version 2.0 | Yes | — | — | POM-only license evidence for the runtime JAR |
| `org.springframework.security:spring-security-config:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-core:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-crypto:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-oauth2-client:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-oauth2-core:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-oauth2-jose:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.security:spring-security-web:6.5.11` | Transitive JAR | Apache License, Version 2.0 | No | L | — | Distributed Apache text observed |
| `org.springframework.session:spring-session-core:3.5.7` | Transitive JAR | Apache License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `org.springframework.session:spring-session-jdbc:3.5.7` | Direct JAR | Apache License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |
| `org.springframework:spring-aop:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-beans:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-context:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-core:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Distributed text names ASM 9.9.1, CGLIB 3.3, JavaPoet 1.13.0 and Objenesis 3.5 |
| `org.springframework:spring-expression:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-jcl:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-jdbc:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-tx:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-web:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.springframework:spring-webmvc:6.2.19` | Transitive JAR | Apache License, Version 2.0 | No | L | N | Shared license text records separately licensed subcomponents and warns that not every listed component is necessarily used |
| `org.yaml:snakeyaml:2.4` | Transitive JAR | Apache License, Version 2.0 | No | — | — | POM-only license evidence for the runtime JAR |

## 6. POM Declaration Evidence

Every one of the 73 locked coordinates had an exact locally available POM. No
effective license declaration was missing after following locally available parent-POM
inheritance. Twelve runtime coordinates obtain their effective declaration from a
parent POM rather than declaring it in their module POM: both Logback modules, three
Jackson extension modules, both Flyway modules, both SLF4J modules and the three
Spring Data modules.

POM declarations are publisher metadata. They do not prove that a JAR contains only
material governed by the primary declared license, that an external license URL is
authentic or unchanged, or that the declared terms are compatible with the
repository's unresolved outbound license. External license URLs were recorded from
the POM evidence but were not followed or independently authenticated during this
offline assessment.

Distributed files may preserve additional terms that a single POM declaration does
not express. Both sources are therefore required for a useful release inventory.

## 7. Distributed Artifact Evidence

All 71 expected runtime JARs were locally available. Inspection of conventionally
named archive resources produced these overlapping categories:

| Distributed evidence category | Count |
| --- | ---: |
| Runtime JARs | 71 |
| JARs with a conventionally named license resource | 51 |
| JARs with `NOTICE`, `DEPENDENCIES` or attribution resources | 39 |
| JARs with neither category | 20 |

The license and notice categories overlap: many JARs have both. The 20-JAR count is
the set with neither conventionally named category, not the sum of missing-license
and missing-notice counts.

Absence of an embedded file is not proof of a licensing violation, public-domain
status or compatibility. Relevant evidence may reside in POMs, parent POMs, source
distributions, upstream release materials or unusually named archive resources. This
assessment did not treat compiled classes whose names contain “License” or “Notice”
as legal resources.

## 8. Nested and Compound License Findings

### 8.1 Logback Classic and Core

The effective parent-POM metadata records both EPL-2.0 and LGPL-2.1. Neither runtime
JAR contains a conventionally named embedded license or notice resource. This record
does not select an alternative or interpret the applicable obligations.

### 8.2 Jakarta Annotation API

The POM records EPL 2.0 and GPL 2 with the Classpath Exception. The distributed
license and notice preserve the expression
`EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0`. Both alternatives remain visible;
the inventory does not collapse them into one license.

### 8.3 Jackson TOML

The POM declares Apache 2.0. The distributed `META-INF/LICENSE` additionally records
TOML-derived material under MIT and JFlex material under a three-clause BSD-style
license. These additional terms must not be lost by a POM-only notice process.

### 8.4 Tomcat Embedded Core

The POM declares Apache 2.0. The distributed license identifies separately licensed
Tomcat subcomponents and schema material, including Common Development and
Distribution License (CDDL) and Eclipse Public License terms. Qualified review must
determine the obligations applicable to the actual released artifact.

### 8.5 Spring Framework Modules

The module POMs declare Apache 2.0. Their shared distributed license text records
separately licensed subcomponents and states that a product containing the file does
not necessarily use every listed component. The `spring-core` text specifically names
ASM 9.9.1, CGLIB 3.3, JavaPoet 1.13.0 and Objenesis 3.5. A future notice mechanism
must preserve required text without falsely representing unused components as
separate resolved Gradle dependencies.

### 8.6 PostgreSQL JDBC and OnGres

The PostgreSQL JDBC POM declares BSD-2-Clause. The runtime JAR carries its primary
license and separate BSD-3-Clause files for bundled OnGres SCRAM and string-preparation
components. Those nested components are not separate coordinates in the committed
Gradle graph but remain distributed artifact evidence.

### 8.7 Bouncy Castle

The POM uses the non-SPDX name “Bouncy Castle Licence.” The JAR contains the exact
`META-INF/LICENSE.md` text and copyright statement. Its MIT-style language is
observable technical evidence; this record does not replace the publisher's wording
with a compatibility or SPDX conclusion.

### 8.8 Log4j Dependency Attributions

Both Log4j runtime JARs contain `META-INF/LICENSE`, `META-INF/NOTICE` and
`META-INF/DEPENDENCIES`. A future notice process must inspect and preserve applicable
dependency attribution material rather than collecting only files named `LICENSE`.

## 9. POM and JAR Differences

Material observed differences include:

- Jackson TOML's POM states Apache 2.0 while its JAR records additional MIT and BSD
  material.
- Tomcat embedded core's POM states Apache 2.0 while its JAR records separately
  licensed schema and subcomponent terms.
- Spring Framework POMs state Apache 2.0 while distributed license resources enumerate
  additional third-party components and terms.
- PostgreSQL JDBC's POM states BSD-2-Clause while its JAR includes separate nested
  BSD-3-Clause OnGres files.
- Jakarta Annotation consistently preserves its alternative-license expression in
  both metadata and distributed evidence.
- Flyway, SLF4J, Spring Data, Jackson extension modules and Logback depend on parent-POM
  inheritance for their effective declarations.
- Several runtime JARs have effective POM declarations but no conventionally named
  embedded legal resources.

These differences are evidence that neither POM-only nor JAR-only inventory is
sufficient. They are not findings of incompatibility.

## 10. Evidence Gaps and Reproducibility Limitations

- The Gradle cache is mutable local state and is not retained release evidence.
- Full dependency-edge and selection-reason reconstruction was not performed.
- Wrapper execution was not performed because it attempted a cache-lock write.
- Existing strict Gradle verification was not rerun. Some repository verification
  entries use exact checksums and others use configured signature trust.
- Parent-POM evidence is necessary for twelve coordinates but is not itself part of
  the 73-coordinate production runtime inventory.
- External license URLs and upstream release statements were not independently
  fetched or authenticated.
- Source JARs were not compared with runtime JARs.
- Convention-based resource discovery may not identify unusually named legal files.
- Bytecode-level shaded or copied-component analysis was not performed.
- No generated backend notice, license report or software bill of materials (SBOM)
  exists as governed evidence.
- License compatibility remains **NOT VERIFIED** because the outbound license and
  distribution model remain unresolved and qualified Legal review is pending.

## 11. Release-Unit and Notice Evidence Requirements

A future governed notice and SBOM design must bind at least:

1. the exact source revision;
2. the committed dependency lock and verification configuration;
3. the resolved direct, transitive and BOM coordinates;
4. effective module and parent-POM license declarations;
5. distributed JAR license, notice and nested-attribution resources;
6. the build toolchain and configuration;
7. source and release-artifact digests;
8. the actual backend release unit and distribution channel; and
9. review, retention, delivery and fail-closed release results.

The mechanism must not reduce alternative or compound licenses to a single label,
discard POM-only declarations, discard nested attribution files, or imply that every
component named in shared license text is a separately resolved dependency. This
record selects no notice generator, SBOM format, plugin or release workflow.

## 12. Legal Review Questions

Qualified Legal/licensing review must provide attributable answers for at least:

1. Which alternative applies to Logback's EPL/LGPL declaration, and what obligations
   follow for each approved release unit and channel?
2. How must the Jakarta Annotation EPL-or-GPL-with-Classpath-Exception expression be
   represented and satisfied?
3. Which CDDL, EPL or other embedded Tomcat schema terms apply to the distributed
   backend artifact?
4. Which Jackson TOML MIT and BSD notices must accompany distribution?
5. How should Spring Framework's shared subcomponent text be retained without
   overstating actual component use?
6. Which PostgreSQL and OnGres BSD notices must be delivered?
7. Is the exact Bouncy Castle Licence text compatible with the eventual, currently
   unresolved, outbound terms and distribution model?
8. Which Apache 2.0 `NOTICE` and `DEPENDENCIES` resources must be reproduced, and in
   what form?
9. Are POM-only declarations sufficient evidence for components without embedded
   legal files, or is authenticated upstream/source evidence required?
10. What notice placement, retention and delivery are required for hosted operation,
    executable JARs, containers, source delivery or any future customer-operated
    distribution?

This Draft does not answer those Legal questions or approve compatibility.

## 13. Engineering Disposition

**Status: PENDING.**

Engineering must confirm the graph derivation, exact coordinate coverage, parent-POM
inheritance, distributed-resource observations and suitability of this evidence as an
input to a future notice/SBOM design. Creation or review of this Draft does not grant
dependency admission or implementation authorization.

## 14. Legal/Licensing Disposition

**Status: PENDING.**

Qualified Legal/licensing review must determine compatibility and obligations only
after the accountable outbound license, distribution model and release units are
defined. No license compatibility or Risk acceptance is granted here.

## 15. Security and Platform/DevOps Dispositions

| Accountable area | Required disposition | Current status |
| --- | --- | --- |
| Security | Confirm supply-chain evidence, vulnerability, integrity, retention and fail-closed release requirements without treating this inventory as admission. | **PENDING** |
| Platform/DevOps | Confirm release-unit association, notice/SBOM generation, immutable retention, delivery, monitoring and release-blocking feasibility. | **PENDING** |

No production release, deployment, notice mechanism, SBOM mechanism or exception is
authorized by this Draft.

## 16. Reverification Triggers

Reverification is required when any of the following occurs:

- the backend lockfile or production configuration changes;
- any direct or transitive dependency version changes;
- a platform/BOM changes dependency selection;
- a new release unit or distribution channel is proposed;
- an embedded license, notice, dependency or attribution resource changes;
- an outbound-license or distribution-model decision is accepted or amended;
- a new notice-generation or SBOM mechanism is proposed or implemented;
- a dependency publisher, ownership, provenance or license declaration changes;
- a relevant vulnerability, Legal interpretation or Security requirement changes; or
- retained evidence can no longer be reproduced from the identified source revision.

## 17. Evidence References

- [Backend Gradle lockfile](../../../backend/gradle.lockfile)
- [Backend build configuration](../../../backend/build.gradle.kts)
- [Backend dependency repositories](../../../backend/settings.gradle.kts)
- [Gradle wrapper configuration](../../../backend/gradle/wrapper/gradle-wrapper.properties)
- [Gradle dependency verification metadata](../../../backend/gradle/verification-metadata.xml)
- [Backend CI workflow](../../../../.github/workflows/backend-gradle-baseline.yml)
- [DEC-0047 — Repository Outbound Licensing and Distribution Model](../../decisions/DEC-0047-repository-outbound-licensing-and-distribution-model.md)
- [NgRx Signal Store 20.1.0 License Evidence](ngrx-signals-20-1-0-license-evidence.md)
- [NgRx Signal Store 20.1.0 Provenance Evidence](ngrx-signals-20-1-0-provenance-evidence.md)
- [Frontend Development-Tooling Advisories](../vulnerability-triage/frontend-development-tooling-advisories.md)
- [ADR-0024 — Frontend Feature-State Management Strategy](../../adr/ADR-0024-frontend-feature-state-management-strategy.md)
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md)
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [Angular Standards](../../../.ai/frontend/ANGULAR.md)

## 18. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Recorded the complete 73-coordinate backend production runtime license inventory, effective POM declarations, distributed license and notice evidence, nested and compound terms, evidence limitations and pending accountable dispositions without approving compatibility, admission or release. |
