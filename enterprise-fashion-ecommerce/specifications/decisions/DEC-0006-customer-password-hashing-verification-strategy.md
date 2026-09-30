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

The current backend admits Spring Security cryptography transitively through the governed Spring Security starter. The admitted module exposes password-hashing integration points, but framework availability is not approval of an algorithm or configuration. Its available Argon2 adapter requires an Argon2 implementation dependency that is not currently admitted. No production Customer password verifier exists, and the repository contains no benchmark evidence from which safe initial numerical Argon2id parameters can yet be selected.

This proposal selects the security strategy and deterministic configuration-governance model required before password verifier creation and verification. It does not admit a dependency, establish numerical parameters without evidence, define Customer-facing password policy, authorize persistence, or create an API Contract or implementation.

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

The initial numerical profile is intentionally not selected by this proposal because the repository contains no controlled benchmark evidence for the governed Java 21, Spring Boot 3.5.16, deployment, concurrency, and resource context. DEC-0006 MUST NOT become Accepted until the initial numerical profile is either recorded in this Decision Record or linked from it to one version-controlled, Identity-owned canonical security-configuration source.

The initial profile and every later profile change require:

1. current security guidance for Argon2id;
2. controlled benchmark evidence on representative supported runtime resources;
3. explicit memory, latency, concurrency, denial-of-service, capacity, and availability analysis;
4. Security and Identity approval with Architecture and Engineering review;
5. compatibility, migration, rollback, and operational evidence; and
6. version-controlled configuration with deterministic tests and fail-closed startup or capability behavior when the required profile is missing, malformed, unsupported, or ambiguous.

Application Environments MAY provision configuration through an approved delivery mechanism, but they MUST NOT silently select different logical password-hashing profiles. Environment overrides, framework defaults, library upgrades, or deployment convenience MUST NOT become implicit parameter authority.

## Salt Semantics

Every verifier SHALL use a newly generated unique random salt produced inside the Identity-owned credential-establishment boundary by a cryptographically secure random-number generator.

The salt is non-secret and MAY be retained with the self-describing verifier representation. Callers, clients, Customer or Account data, API inputs, imported untrusted data, and unrelated Domains MUST NOT choose or reuse the salt for a newly established verifier.

Salt-generation failure or uncertainty SHALL fail credential establishment without retaining a verifier or representing success. This decision selects no database field, encoding API, random-provider implementation, or numerical salt length; salt length remains a mandatory field of the evidence-backed Approved profile.

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

The representation SHALL be treated as Identity-owned sensitive security data even though it is not plaintext and the salt is non-secret. It MUST NOT be exposed through ordinary Contracts, Customer or Account state, URLs, logs, metrics, traces, analytics, events, support evidence, fixtures, or client storage.

Malformed, truncated, ambiguous, non-canonical, unsupported, unexpectedly downgraded, or unrecognized representations SHALL fail safely. Parsing MUST be bounded and MUST NOT permit representation-controlled resource use outside governed limits. No database type, field, column, table, index, ORM mapping, JDBC mapping, repository, or serialization library is selected.

## Verification Semantics

The raw password SHALL enter only the Identity-owned credential-verification boundary over a separately governed protected Contract. It SHALL remain transient, purpose-limited, and excluded from persistence, logs, errors, telemetry, events, analytics, caches, Customer or Account records, and support tooling.

Verification SHALL use the algorithm, version, parameters, salt, and derived output represented by a supported stored verifier. A successful cryptographic match is Identity-owned credential evidence, not by itself a Principal, Session, Customer, Account, or contextual Authorization decision. ADR-0021 and ADR-0019 continue to govern accepted Authentication evidence, Principal establishment, and Session establishment.

A non-match, malformed or unsupported verifier, unavailable required configuration, hashing error, parsing error, resource-bound violation, or uncertain result SHALL NOT establish successful password verification, a Principal, or a Session. External behavior must preserve existing enumeration resistance and MUST NOT disclose whether failure arose from identifier absence, password mismatch, representation state, configuration, migration, or internal security handling.

Verification and comparison MUST use reviewed cryptographic facilities and MUST NOT expose password or verifier material through timing shortcuts, diagnostic output, or custom comparison code. This decision defines no route, response, HTTP status, error body, timing equalization mechanism, rate, delay, lockout, or retry count.

## Upgrade and Rehash Semantics

The current Approved profile SHALL determine whether a supported stored verifier requires upgrade. New password establishment and password change SHALL use only the current profile.

After successful verification of a supported older verifier, Identity MAY derive a replacement verifier from the already accepted raw password using the current profile. Upgrade eligibility SHALL be based on explicit algorithm, version, and parameter comparison, never on string age, record order, framework default, or an untrusted caller instruction.

Successful verification against an older representation that remains explicitly supported and not withdrawn MAY establish accepted credential evidence even if the subsequent upgrade write fails. The failed or uncertain upgrade SHALL remain observable for bounded retry or reconciliation without logging credential material, falsely claiming upgrade, multiplying effects, or weakening the accepted Authentication result. A verifier classified as unsupported, unsafe, withdrawn, malformed, or ambiguously downgraded SHALL fail closed and SHALL NOT use this compatibility allowance.

Concurrent verification or upgrade MUST converge without replacing a newer or stronger accepted verifier with an older or weaker representation. Persistence uncertainty MUST preserve the last confirmed verifier state and must not report an upgrade as complete. This decision selects no transaction, lock, isolation level, optimistic-concurrency mechanism, retry policy, repository design, or audit schema.

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

The currently admitted Spring Security cryptography module provides integration surfaces for password verification but does not itself authorize a concrete `PasswordEncoder`, configuration, or algorithm use. Its available Argon2 adapter relies on an Argon2 implementation dependency that is not present in the admitted dependency graph.

Therefore, implementation of the selected Argon2id strategy requires a separate DEC-0001-governed dependency-admission change unless later verified repository evidence demonstrates that the governed implementation can be supplied entirely by already admitted artifacts. That change must establish exact artifact and version compatibility, licensing, security and maintenance evidence, locking, strict dependency verification, and successful build and test resolution.

DEC-0006 does not admit Bouncy Castle, another cryptographic provider or library, a dependency version, or a Spring Security implementation class. Framework or provider availability MUST NOT alter the selected security semantics or parameter authority.

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
- The current backend requires separate dependency admission for an implementation facility.
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

Operations must support benchmark-informed capacity, configuration integrity, dependency maintenance, upgrade visibility, malformed or downgraded verifier detection, compromise handling, and safe Customer support without exposing credential existence or material. No provider, support workflow, alert threshold, or service level is selected.

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
- Spring Security class, `PasswordEncoder`, Bean, configuration, or Authentication implementation;
- dependency artifact or version, cryptographic provider, secret manager, infrastructure, or hosting;
- unrelated abuse, rate, lockout, retry, timeout, Session lifetime, cookie, service-level, or operational numerical policy; or
- executable implementation, deployment, completed benchmark, or completed test evidence.

## Required Acceptance Authorities and Evidence

Promotion from Proposed to Accepted requires durable evidence representing:

- Identity ownership of credential establishment, verification, upgrade, compromise, and migration semantics;
- Security approval of the Argon2id strategy, profile-governance model, salt and no-pepper decisions, verifier representation, failure, downgrade, and compromise boundaries;
- Architecture approval that existing Identity, Customer, Session, persistence, and dependency boundaries remain intact; and
- Engineering review of Java 21 and Spring Boot 3.5.16 compatibility, representative benchmark method, resource and operational implications, dependency consequences, testability, and maintenance.

Product review is required only if the decision is changed to establish Customer-facing password policy or another Product semantic; this proposal makes no such change. Identity remains the single accountable owner. Pull-request approval is sufficient only when required authorities are represented and the evidence is durable and discoverable. No reviewer, approval, benchmark, or implementation evidence is claimed by this Proposed record.

## Acceptance and Validation Criteria

Before DEC-0006 may become Accepted, review must verify:

1. metadata remains `0.1.0 Proposed`, Type `Security Decision`, owner `Identity`, and `authoritative: false`;
2. Argon2id is the single proposed initial adaptive password-hashing strategy;
3. plaintext, reversible encryption, unsalted hashing, general-purpose fast hashing alone, and silent weaker fallback are prohibited;
4. the mandatory parameter set, authority, benchmark, security, resource, approval, versioning, test, and fail-closed configuration model is deterministic;
5. the initial numerical profile is recorded in this record or one directly referenced canonical source before acceptance;
6. every new verifier uses a unique cryptographically random non-secret salt generated within Identity, retained with the representation where needed, and never supplied or reused by callers;
7. no initial pepper is selected, and future adoption requires separate governed custody, rotation, compromise, migration, availability, and recovery semantics;
8. the self-describing verifier identifies the algorithm, version, parameters, salt, output, compatibility, and upgrade state without defining persistence schema;
9. malformed, unsupported, non-canonical, unexpectedly downgraded, or resource-unsafe representations fail safely;
10. password verification failure or uncertainty cannot establish accepted Authentication evidence, a Principal, or a Session;
11. successful supported legacy verification, rehash eligibility, upgrade-write failure, concurrency, reconciliation, and stronger-state preservation are explicit without selecting persistence mechanics;
12. no new verifier is written under an older profile and rollback or concurrency cannot downgrade accepted state;
13. no legacy representation is authorized without explicit algorithm, bounds, provenance, migration, withdrawal, and support governance;
14. verifier, algorithm, configuration, implementation, and future-secret compromise boundaries preserve incident, containment, evidence, migration, and failure-closed requirements;
15. DEC-0001 remains authoritative and no dependency, provider, version, or implementation class is admitted;
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
- [RFC 9106 — Argon2 Memory-Hard Function for Password Hashing and Proof-of-Work Applications](https://www.rfc-editor.org/rfc/rfc9106)

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-30 | Proposed | Proposed Argon2id as the initial Customer password-hashing and verification strategy with evidence-backed parameter authority, unique random salts, no initial pepper, self-describing verifiers, safe verification, upgrade, compatibility, compromise, and failure semantics while preserving separate Product policy, persistence, Contract, dependency, and implementation governance. |
