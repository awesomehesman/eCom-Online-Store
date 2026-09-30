# DEC-0006 — Customer Password Hashing and Verification Strategy

- **Identifier:** DEC-0006
- **Title:** Customer Password Hashing and Verification Strategy
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-09-30
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted DEC-0003 establishes an Identity-owned password-based local Customer credential as the single initial credential category with no initial fallback. It requires a non-reversible verification representation and prohibits plaintext or reversibly encrypted password storage, but deliberately leaves the password-hashing facility, algorithm, configuration, representation, verification, upgrade, compatibility, and dependency unresolved.

`SECURITY-STANDARDS.md` requires passwords to use an approved adaptive password-hashing facility rather than a general-purpose fast hash or encryption. Accepted ADR-0021 assigns local Customer credential verification to Identity, while ADR-0017, ADR-0018, DEC-0001, and the Approved Identity Domain and BIDN preserve separate persistence, dependency, and implementation authority.

The current backend admits Spring Security cryptography transitively through the governed Spring Security starter. Bouncy Castle `org.bouncycastle:bcprov-jdk18on:1.86` is admitted, locked, and dependency-verified under the DEC-0001-governed build baseline. Framework and provider availability are not approval of an algorithm, profile, production configuration, or credential implementation. No production Customer password verifier exists.

This proposal selects the security strategy, the evidence-backed initial numerical profile, and the deterministic configuration-governance model required before password verifier creation and verification. It does not define Customer-facing password policy, authorize persistence, create an API Contract, or claim that production credential hashing or Authentication implementation exists.

## Decision

If Accepted, **Argon2id** SHALL be the single initial adaptive password-hashing strategy for Identity-owned local Customer password credentials.

Every newly established password verifier SHALL use the current Approved Argon2id parameter profile and a unique cryptographically random salt. Retained verifiers SHALL use a self-describing, version-identifiable Argon2id representation sufficient to identify the algorithm, Argon2 version, work parameters, salt, and derived output needed for verification and upgrade assessment.

Plaintext, reversible encryption, unsalted hashing, general-purpose fast hashing such as SHA-256 or SHA-512 alone, and silent fallback to a weaker or unapproved verifier SHALL be prohibited.

Argon2id is selected for its memory-hard password-hashing design, resistance to parallel password guessing relative to general-purpose fast hashes, standardized self-describing representation, and ability to evolve work parameters without changing the Customer credential category. Selection does not by itself approve a library, dependency, Spring Security class, or executable configuration.

## Configuration Authority Model

The governed Argon2id parameter profile SHALL explicitly include:

- Argon2 version;
- memory cost;
- iteration or time cost;
- parallelism;
- salt length;
- derived output length; and
- a stable profile identifier or other unambiguous compatibility identity.

The repository-owned initial profile is `identity-customer-argon2id-v1`:

| Profile field | Governed value |
| --- | --- |
| Algorithm | Argon2id |
| Argon2 version | 19 / v1.3 |
| Memory | 65,536 KiB |
| Iterations/passes | 3 |
| Parallelism/lanes | 4 |
| Salt length | 16 bytes / 128 bits |
| Output/tag length | 32 bytes / 256 bits |
| Representation | Self-describing Argon2id PHC representation |

`identity-customer-argon2id-v1` is a repository-defined compatibility identity; it is not an OWASP, RFC, Spring Security, or Bouncy Castle profile identifier. Its numerical tuple corresponds to RFC 9106's second recommended Argon2id option. Every new Customer password verifier SHALL use the complete current profile. A framework default, Environment-selected profile, caller-selected profile, Candidate A, or another tuple is not an automatic fallback. Changing any governed tuple component creates a different profile and requires governed compatibility or supersession authority.

The initial profile and every later profile change require:

1. current security guidance for Argon2id;
2. controlled benchmark evidence on representative supported runtime resources;
3. explicit memory, latency, concurrency, denial-of-service, capacity, and availability analysis;
4. Security and Identity approval with Architecture and Engineering review;
5. compatibility, migration, rollback, and operational evidence; and
6. version-controlled configuration with deterministic tests and fail-closed startup or capability behavior when the required profile is missing, malformed, unsupported, or ambiguous.

Application Environments MAY provision configuration through an approved delivery mechanism, but they MUST NOT silently select different logical password-hashing profiles. Environment overrides, framework defaults, library upgrades, or deployment convenience MUST NOT become implicit parameter authority.

## Benchmark Evidence and Profile Selection

The first authorized local benchmark execution is incomplete. Its original macOS physical-memory preflight treated immediately free physical memory as a hard gate, so it stopped after Candidate A sequential evidence and is not primary profile-selection evidence. Its preserved local output had SHA-256 `a084eec13568056197797ee71cb6a5fb3aa4d53f26a98e78102c2558996a0613`.

The second execution completed on 2026-09-30 using Eclipse Temurin 21.0.6+7-LTS, Spring Security 6.5.11, governed `bcprov-jdk18on:1.86`, macOS 26.5.2 ARM64, 12 logical processors, 24 GiB physical memory, and a benchmark JVM configured with `-Xms512m -Xmx2g`. Each Candidate and suite used 10 warm-up encode/match pairs, 30 sequential encode samples, 30 sequential match samples, controlled concurrency levels 1, 2, and 4, and 30 operations per concurrent phase. Both `A then B` and `B then A` suite orders completed. All generated PHC representations and password matches passed, every bounded memory preflight passed, and the Gradle execution completed successfully. The preserved local output had SHA-256 `29f28594978fb30c16edc17da6b8a89fbd56ac430feaa0e533ceee63b93deed8`.

These hashes identify the reviewed local artifacts; they do not make temporary output files durable repository artifacts.

Candidate A used Argon2id v=19, `m=19456`, `t=2`, `p=1`, a 16-byte salt, and a 32-byte output. Across both suite orders, sequential encode median was 27.030–27.069 ms, sequential encode p95 was 28.066–28.720 ms, sequential match median was 26.643–26.792 ms, concurrency-four encode median was 28.476–31.701 ms, and concurrency-four encode p95 was 34.206–41.495 ms. Its parameter-derived concurrency-four working memory was 76 MiB.

Candidate B used Argon2id v=19, `m=65536`, `t=3`, `p=4`, a 16-byte salt, and a 32-byte output. Across both suite orders, sequential encode median was 155.355–156.335 ms, sequential encode p95 was 158.623–163.721 ms, sequential match median was 157.973–159.189 ms, concurrency-four encode median was 173.924–175.579 ms, concurrency-four encode p95 was 188.506–196.029 ms, and concurrency-four match median was 176.377–176.445 ms. Its parameter-derived concurrency-four working memory was 256 MiB. The concurrency-four conservative required memory was 1,073,741,824 bytes, against a 2,147,483,648-byte benchmark JVM maximum heap and a 6,442,450,944-byte physical-memory 25% safety limit; the preflight passed.

Reported encode-phase throughput is not treated as pure encoding throughput because phase wall time includes post-encode PHC validation and matching.

Candidate B is selected because it corresponds to RFC 9106's second recommended Argon2id option, provides materially greater memory and time cost than Candidate A, was represented exactly by the governed Spring Security and Bouncy Castle path, produced sufficiently stable comparative evidence in reversed suite order, remained feasible at bounded concurrency four under the benchmark safety limits, and had no measured evidence of infeasibility. Candidate A remains a legitimate OWASP-recommended minimum configuration and is not classified as insecure, but it is not selected because Candidate B provides greater per-guess cost while remaining feasible in this bounded evidence. Candidate A MUST NOT become an automatic fallback.

This is local development evidence, not production-capacity evidence, and it establishes no SLO. It covers one ARM64 macOS development machine, concurrency 1, 2, and 4, and 30 measured operations per phase as bounded comparative evidence rather than capacity modelling. No hostile-load, soak, multi-process, container-limit, or production-topology test occurred. Production capacity validation remains required before deployment, and abuse and rate controls remain separately governed. Spring Security and Bouncy Castle performance on this machine does not establish attacker-side hardware cost or parallelism equivalence.

## Salt Semantics

Every new current-profile verifier SHALL use a newly generated unique random 16-byte salt produced inside the Identity-owned credential-establishment boundary by a cryptographically secure random-number generator.

The salt is non-secret and SHALL be retained as part of the self-describing verifier representation. Callers, clients, Customer or Account data, API inputs, imported untrusted data, and unrelated Domains MUST NOT choose or reuse the salt for a newly established verifier.

Salt-generation failure or uncertainty SHALL fail credential establishment without retaining a verifier or representing success. This decision selects no database field, encoding API, or random-provider implementation.

## Pepper Decision

The initial strategy SHALL NOT use a pepper or another server-held secret mixed into password hashing.

An initial pepper is not selected because no required threat model, key custody, provider, rotation, availability, recovery, or multi-version verification design is governed, and introducing one would create a new secret-dependent Authentication failure boundary before repository evidence justifies it. The absence of a pepper does not weaken the requirements for strong Argon2id parameters, unique random salts, least-privileged verifier storage, encryption at rest, monitoring, and compromise response.

Future pepper adoption requires a separate Security Decision or a governed superseding update that defines purpose, custody, generation, access, version identification, rotation, compromise, availability, backup, migration, recovery, and rollback. It MUST NOT silently reinterpret existing verifier representations or select a secret manager or provider through implementation.

## Verifier Representation Semantics

The retained verifier SHALL use a canonical self-describing Argon2id encoded representation compatible with the Argon2 Password Hashing Competition string format. It SHALL identify or encode sufficiently:

- Argon2id as the algorithm;
- the Argon2 version;
- memory, iteration/time, and parallelism parameters;
- the unique salt;
- the derived output; and
- the information required to determine whether the verifier is supported and whether upgrade is required.

For `identity-customer-argon2id-v1`, the representation SHALL establish `$argon2id$`, `v=19`, `m=65536`, `t=3`, and `p=4`, and its encoded values SHALL decode to a 16-byte salt and a 32-byte output.

The representation SHALL be treated as Identity-owned sensitive security data even though it is not plaintext and the salt is non-secret. It MUST NOT be exposed through ordinary Contracts, Customer or Account state, URLs, logs, metrics, traces, analytics, events, support evidence, fixtures, or client storage.

Malformed, truncated, ambiguous, non-canonical, unsupported, withdrawn, unexpectedly downgraded, resource-unsafe, or unrecognized representations SHALL fail safely. Parsing and resource bounds MUST be applied before expensive processing where the implementation permits, and representation-controlled input MUST NOT authorize resource use outside governed limits. No database type, field, column, table, index, ORM mapping, JDBC mapping, repository, or serialization library is selected.

## Verification Semantics

The raw password SHALL enter only the Identity-owned credential-verification boundary over a separately governed protected Contract. It SHALL remain transient, purpose-limited, and excluded from persistence, logs, errors, telemetry, events, analytics, caches, Customer or Account records, and support tooling.

Verification SHALL identify an explicitly supported governed profile and use the algorithm, version, parameters, salt, and derived output represented by that supported stored verifier. A successful cryptographic match is Identity-owned credential evidence, not by itself a Principal, Session, contextual Authorization decision, or Customer or Account ownership decision. ADR-0021 and ADR-0019 continue to govern accepted Authentication evidence, Principal establishment, and Session establishment.

A non-match, malformed or unsupported verifier, unavailable required configuration, hashing error, parsing error, resource-bound violation, or uncertain result SHALL NOT establish successful password verification, a Principal, or a Session. External behavior must preserve existing enumeration resistance and MUST NOT disclose whether failure arose from identifier absence, password mismatch, representation state, configuration, migration, or internal security handling.

Verification and comparison MUST use reviewed cryptographic facilities and MUST NOT expose password or verifier material through timing shortcuts, diagnostic output, or custom comparison code. This decision defines no route, response, HTTP status, error body, timing equalization mechanism, rate, delay, lockout, or retry count.

## Upgrade and Rehash Semantics

The current Approved profile SHALL determine whether a supported stored verifier requires upgrade through comparison of the complete governed tuple, not only the algorithm name. New password establishment and password change SHALL use only the current profile and a new random 16-byte salt.

An older profile MAY be verified only while it remains explicitly governed, supported, and not withdrawn. After successful verification of such a verifier, Identity MAY derive a replacement verifier from the already accepted raw password using a new random 16-byte salt and the complete current profile. Upgrade eligibility SHALL be based on explicit algorithm, version, memory, iterations, parallelism, salt policy, output length, and profile-identity comparison, never on string age, record order, framework default, Environment configuration, or an untrusted caller instruction.

Successful verification against an older representation that remains explicitly supported and not withdrawn MAY establish accepted credential evidence even if the subsequent upgrade write fails. The failed or uncertain upgrade SHALL remain observable for bounded retry or reconciliation without logging credential material, falsely claiming upgrade, multiplying effects, or weakening the accepted Authentication result. A verifier classified as unsupported, unsafe, withdrawn, malformed, or ambiguously downgraded SHALL fail closed and SHALL NOT use this compatibility allowance.

Concurrent verification, upgrade, or rollback MUST converge without replacing a current or stronger accepted verifier with an older or weaker representation. Failed rehash persistence MUST NOT corrupt or downgrade the retained verifier. Persistence uncertainty MUST preserve the last confirmed verifier state and must not report an upgrade as complete. New writes MUST NOT use an older, weaker, framework-default, Environment-selected, or caller-selected profile. This decision selects no transaction, lock, isolation level, optimistic-concurrency mechanism, retry policy, repository design, or audit schema.

## Compatibility and Migration

No production Customer password verifier currently exists, so this proposal authorizes no legacy algorithm or imported verifier format.

Future support for an older or alternative representation requires explicit governed identification of:

- its algorithm and parameter bounds;
- the reason it remains safe enough for limited verification;
- the affected credential population and provenance;
- upgrade, expiry, withdrawal, and removal criteria;
- migration, reconciliation, rollback, and Customer-support behavior; and
- verification and monitoring evidence.

Algorithm or parameter strengthening SHALL preserve self-description and allow supported older verifiers to be recognized without making them the current profile. New writes MUST NOT downgrade. Rollback MUST NOT restore an unsafe profile, discard stronger accepted verifiers, or silently re-enable withdrawn support.

Removal of obsolete support requires evidence that affected credentials were upgraded, invalidated, or moved through separately governed recovery. Unrecognized or unsupported representations fail closed rather than invoking an implicit fallback.

## Compromise Boundary

Suspected exposure of verifier representations, discovery of malformed or downgraded representations, or evidence that the selected algorithm, implementation, or Approved profile is unsafe SHALL trigger applicable Security incident, evidence-preservation, containment, assessment, and credential-protection governance.

The affected state MUST NOT be treated as trustworthy merely because a stored representation parses or previously verified. New verifier creation under an unsafe or withdrawn profile SHALL stop. Continued verification, forced credential replacement, Session invalidation, Customer communication, recovery, and migration require impact-based authorized handling under existing Security authority and later operational governance.

Because the initial strategy has no pepper, pepper compromise handling is not an active runtime dependency. A future pepper decision must define compromise and rotation semantics before use.

This decision selects no incident severity, notification timeline, password-reset Contract, support procedure, Session invalidation threshold, or operational numerical target.

## Failure and Uncertainty

Hashing failure, secure-random failure, invalid parameter configuration, malformed or unsupported representation, verification failure, unavailable required security material, resource-bound violation, upgrade failure, persistence uncertainty, and dependency failure SHALL remain distinguishable internally where security handling or recovery differs.

No failure or uncertain state may be represented as successful credential establishment, successful password verification, completed upgrade, Principal establishment, or Session establishment. Recovery, retry, and reconciliation MUST preserve the latest confirmed verifier, prevent downgrade, avoid duplicate or conflicting credential effects, and exclude password material from evidence.

This decision selects no retry count, timeout, circuit breaker, queue, alert threshold, service level, recovery objective, or persistence implementation.

## Authority and Compatibility Boundaries

This proposal preserves:

- DEC-0003 authority over the password credential category;
- DEC-0004 authority over the Customer-facing email login identifier;
- DEC-0005 authority over login-email comparison, uniqueness, and lifecycle semantics;
- ADR-0021 authority over Identity-owned local Customer Authentication;
- ADR-0019 authority over Principal and Session establishment;
- ADR-0020 authority over Session persistence;
- ADR-0017 and ADR-0018 authority over persistence architecture and technology;
- DEC-0001 authority over dependency admission, locking, verification, and maintenance; and
- `SECURITY-STANDARDS.md` authority over mandatory password, cryptography, Identity, logging, incident, and exception controls.

Identity remains the single accountable owner of the credential-verification strategy. Customer and Account business truth, Product policy, contextual Authorization, Session authority, persistence Architecture, and dependency governance remain with their existing owners.

## Dependency Boundary

The currently admitted Spring Security 6.5.11 cryptography module provides integration surfaces for password verification but does not itself authorize production credential implementation. Bouncy Castle `org.bouncycastle:bcprov-jdk18on:1.86` is admitted, locked, and dependency-verified under the DEC-0001-governed build baseline. The benchmark exercised Spring Security 6.5.11 `Argon2PasswordEncoder` through that governed implementation path.

This admitted dependency state does not mean that production password hashing, credential persistence, Authentication configuration, or credential-verification implementation exists. DEC-0001 remains authoritative over future dependency changes, locking, verification, compatibility, licensing, security, maintenance, and build evidence. Framework or provider availability MUST NOT alter the selected security semantics or profile authority.

## Alternatives Considered

### A. Argon2id — Selected in This Proposal

Argon2id is a modern adaptive, memory-hard password-hashing strategy designed to increase the cost of parallel password guessing while balancing resistance to side-channel and accelerator attacks. It provides explicit memory, iteration, parallelism, salt, version, and output controls and a self-describing representation suitable for versioned upgrades.

Its costs include memory and CPU consumption, denial-of-service and capacity considerations, mandatory benchmarking, configuration governance, and an additional implementation dependency under the current backend graph. Future changes require careful compatibility and resource analysis.

### B. bcrypt — Not Selected

bcrypt is mature, adaptive, widely deployed, and available through the already admitted Spring Security cryptography module without the additional Argon2 implementation dependency identified above. Its operational model is simpler and well understood.

It is not selected because its work model is primarily CPU-cost based, its parameter and output design is less flexible for memory-hard evolution, and its input-handling limits create additional compatibility obligations. Existing availability alone is insufficient reason to prefer it over the selected security strategy.

### C. scrypt — Not Selected

scrypt is memory-hard and offers configurable CPU and memory cost. It is a credible strategy for resisting parallel guessing and is supported by mature Java security ecosystems.

It is not selected because the repository has no evidence that its configuration, implementation dependency, operational profile, or migration characteristics are preferable to Argon2id for this platform. Selecting it would still require benchmarking, dependency review, and governed parameters.

### D. PBKDF2 — Not Selected

PBKDF2 is standardized, mature, broadly available in Java cryptographic providers, and can be appropriate where compatibility or formal compliance constraints require it.

It is not selected because it is not memory-hard and the repository has no compatibility or compliance requirement that outweighs the selected Argon2id strategy. It would still require iteration, salt, output, algorithm-variant, upgrade, and migration governance.

### E. General-Purpose Fast Hash, Reversible Encryption, Plaintext, or Unsalted Hashing — Rejected

These approaches conflict with `SECURITY-STANDARDS.md` and DEC-0003. They do not provide an acceptable adaptive password-verification boundary and are prohibited rather than retained as migration defaults.

## Consequences

### Benefits and Enabled Outcomes

- Establishes one modern adaptive, non-reversible password-verification strategy.
- Makes verifier algorithm, version, parameters, salt, output, compatibility, and upgrade state deterministic.
- Enables evidence-based parameter approval, credential persistence design, migration design, and later implementation.
- Provides a governed path for strengthening parameters and retiring obsolete representations.
- Prevents framework defaults or database representations from becoming implicit security authority.

### Costs and Maintenance Burden

- Argon2id consumes deliberate CPU and memory and requires representative benchmarking and capacity analysis.
- The admitted implementation dependency requires continued DEC-0001-governed maintenance, locking, verification, and compatibility review.
- Identity and Security must maintain parameter profiles, compatibility support, upgrade behavior, compromise response, tests, and operational evidence.
- Supporting older profiles during upgrades increases verification, migration, monitoring, and support complexity.

### Risks and Limitations

- Weak or unevidenced parameter selection can undermine the algorithm.
- Excessive parameters can create availability or denial-of-service Risk.
- Configuration drift can produce inconsistent verifier strength or failed Authentication.
- Unsafe fallback, malformed-representation handling, or upgrade races can enable downgrade or lock out Customers.
- Library or provider defects and maintenance failure remain supply-chain and operational Risks.
- Hashing cannot prevent phishing, password reuse, credential stuffing, compromised endpoints, or weak Customer password choices by itself.

### Security Impact

Password material remains inside the Identity boundary, plaintext and reversible storage remain prohibited, failures remain closed, and verifier upgrades cannot weaken accepted state. The strategy adds resource-exhaustion, dependency, configuration, migration, and compromise-response obligations requiring Security evidence before acceptance and implementation.

### Privacy and Data Impact

Verifier representations remain sensitive Identity-owned security data and must be minimized, access-controlled, encrypted at rest under existing authority, excluded from ordinary observability and Contracts, and retained only through later governed persistence and lifecycle policy. No Customer or Account field becomes credential authority.

### Operational and Support Impact

Operations must support benchmark-informed capacity, configuration integrity, dependency maintenance, upgrade visibility, malformed or downgraded verifier detection, compromise handling, and safe Customer support without exposing credential existence or material. The bounded local evidence demonstrates feasibility for the selected profile but does not establish production capacity, concurrency, throughput, or an SLO; production capacity validation remains required before deployment. No provider, support workflow, alert threshold, or service level is selected.

### Compatibility, Migration, and Reversibility Impact

No existing production verifier requires migration. Future parameter or algorithm changes require explicit compatibility identifiers, supported-verifier bounds, safe rehash, withdrawal criteria, migration and reconciliation evidence, and prevention of downgrade. The decision is reversible only through a superseding Security Decision and safe migration or invalidation of affected verifiers; exit cost increases after persisted credentials depend on Argon2id.

## Explicit Non-Decisions

DEC-0006 does not select, define, admit, or authorize:

- Customer-facing password minimum length, composition, history, reuse, or rotation policy;
- compromised-password-list Product behavior or provider;
- Product Decision 5 or Product Decision 28;
- Identity-to-Customer or Account cardinality, association, persistence, or mapping;
- registration coordination or Customer/Account creation;
- password recovery mechanism, channel, factor, token, provider, expiry, or support workflow;
- ordinary Customer MFA policy, mechanism, factor, provider, fallback, or recovery;
- login, registration, password-change, recovery, or verification API route, HTTP method, status, DTO, field, error code, or Contract;
- database schema, table, column, index, constraint, collation, SQL, ORM or JDBC mapping, repository, or Flyway migration;
- production Spring Security Bean, configuration, Authentication implementation, or credential implementation;
- additional dependency artifact or version, cryptographic provider, secret manager, infrastructure, or hosting;
- unrelated abuse, rate, lockout, retry, timeout, Session lifetime, cookie, service-level, or operational numerical policy; or
- executable production implementation, deployment, production-capacity evidence, SLO, or completed production test evidence.

## Required Acceptance Authorities and Evidence

Promotion from Proposed to Accepted requires durable evidence representing:

- Identity ownership of credential establishment, verification, upgrade, compromise, and migration semantics;
- Security approval of the Argon2id strategy, profile-governance model, salt and no-pepper decisions, verifier representation, failure, downgrade, and compromise boundaries;
- Architecture approval that existing Identity, Customer, Session, persistence, and dependency boundaries remain intact; and
- Engineering review of Java 21 and Spring Boot 3.5.16 compatibility, representative benchmark method, resource and operational implications, dependency consequences, testability, and maintenance.

Product review is required only if the decision is changed to establish Customer-facing password policy or another Product semantic; this proposal makes no such change. Identity remains the single accountable owner. Pull-request approval is sufficient only when required authorities are represented and the evidence is durable and discoverable. This Proposed record includes bounded local benchmark evidence but does not claim completed Identity, Security, Architecture, or Engineering acceptance, named reviewers, production-capacity validation, or production implementation evidence. Criterion 21 remains open until required acceptance evidence is durable.

## Acceptance and Validation Criteria

Before DEC-0006 may become Accepted, review must verify:

1. metadata remains `0.1.0 Proposed`, Type `Security Decision`, owner `Identity`, and `authoritative: false`;
2. Argon2id is the single proposed initial adaptive password-hashing strategy;
3. plaintext, reversible encryption, unsalted hashing, general-purpose fast hashing alone, and silent weaker fallback are prohibited;
4. the mandatory parameter set, authority, benchmark, security, resource, approval, versioning, test, and fail-closed configuration model is deterministic;
5. `identity-customer-argon2id-v1` records Argon2id v=19, `m=65536`, `t=3`, `p=4`, a 16-byte salt, a 32-byte output, a self-describing PHC representation, and attributable bounded benchmark evidence before acceptance;
6. every new current-profile verifier uses a unique cryptographically random non-secret 16-byte salt generated within Identity, retained with the representation, and never supplied or reused by callers;
7. no initial pepper is selected, and future adoption requires separate governed custody, rotation, compromise, migration, availability, and recovery semantics;
8. the self-describing verifier identifies the algorithm, version, complete governed profile, 16-byte salt, 32-byte output, compatibility, and upgrade state without defining persistence schema;
9. malformed, unsupported, non-canonical, unexpectedly downgraded, or resource-unsafe representations fail safely;
10. password verification failure or uncertainty cannot establish accepted Authentication evidence, a Principal, or a Session;
11. complete-profile comparison, successful supported older-profile verification, rehash eligibility with a new 16-byte salt, upgrade-write failure, concurrency, reconciliation, and stronger-state preservation are explicit without inventing a legacy profile or selecting persistence mechanics;
12. no new verifier is written under an older profile and rollback or concurrency cannot downgrade accepted state;
13. no legacy representation is authorized without explicit algorithm, bounds, provenance, migration, withdrawal, and support governance;
14. verifier, algorithm, configuration, implementation, and future-secret compromise boundaries preserve incident, containment, evidence, migration, and failure-closed requirements;
15. DEC-0001 remains authoritative; admitted `bcprov-jdk18on:1.86` and the benchmarked Spring Security 6.5.11 path do not claim production credential implementation or authorize another dependency, provider, version, or implementation class;
16. Customer-facing password policy and compromised-password-list Product behavior remain unresolved;
17. Product Decisions 5 and 28 remain unresolved;
18. Identity-to-Customer/Account association, registration coordination, recovery, and ordinary Customer MFA remain unresolved;
19. API Contracts, persistence schema, migrations, repositories, framework configuration, infrastructure, and executable implementation remain unresolved;
20. DEC-0003, DEC-0004, DEC-0005, ADR-0017, ADR-0018, ADR-0019, ADR-0020, ADR-0021, and existing Security authority remain unchanged;
21. required Identity, Security, Architecture, and Engineering acceptance evidence is durable without fabricated reviewers, approvals, benchmarks, or implementation claims; and
22. proposal registration affects only DEC-0006 and `DECISIONS.md`, registers DEC-0006 exactly once as Proposed, passes whitespace and diff validation, and introduces no unrelated tracked changes.

## Proposal Registration and Acceptance Planning

Proposal-stage registration changes only DEC-0006 and `DECISIONS.md`. Acceptance-readiness review must verify the final affected-source set after the initial parameter profile and required evidence are complete. This proposal does not presume that acceptance can occur before those gates are satisfied and does not synchronize `SECURITY-STANDARDS.md`, `ARCHITECTURE.md`, `PRODUCT.md`, Spring, Java, database, PostgreSQL, API, Domain, Backend Specification, build, or implementation sources.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [JAVA.md](../../.ai/backend/JAVA.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [API.md](../../.ai/backend/API.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [ADR-0017 — PostgreSQL Schema Strategy](../adr/ADR-0017-postgresql-schema-strategy.md)
- [ADR-0018 — Persistence Technology](../adr/ADR-0018-persistence-technology.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](./DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](./DEC-0003-initial-local-customer-credential-mechanism.md)
- [DEC-0004 — Customer Login Identifier Semantics](./DEC-0004-customer-login-identifier-semantics.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](./DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [RFC 9106 — Argon2 Memory-Hard Function for Password Hashing and Proof-of-Work Applications](https://www.rfc-editor.org/rfc/rfc9106)
- [Spring Security 6.5 — Password Storage](https://docs.spring.io/spring-security/reference/6.5/features/authentication/password-storage.html)
- [DEC-0006 Argon2id Benchmark Harness](../../backend/src/test/java/com/enterprise/fashion/ecommerce/identity/benchmark/Argon2Benchmark.java)

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-30 | Proposed | Proposed Argon2id as the initial Customer password-hashing and verification strategy with evidence-backed parameter authority, unique random salts, no initial pepper, self-describing verifiers, safe verification, upgrade, compatibility, compromise, and failure semantics while preserving separate Product policy, persistence, Contract, dependency, and implementation governance. |
