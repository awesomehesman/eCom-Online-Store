---
title: Authoritative Credential Source and Confirmation Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-07
authoritative: false
---

# Authoritative Credential Source and Confirmation Design

## 1. Purpose, scope and lifecycle

This Approved Specification defines the source/access obligations needed to realize Approved ICED evidence semantics without making constructors, stored rows or mocked results authoritative. Scope code **ICSC** identifies its requirements. ICED continues to define evidence meanings; ICRD continues to define credential-state/currentness obligations. This subordinate design grants no implementation authority and makes no Product, Security or Architecture decision.

Inspected baseline: `0690f154c9e69f135dc3f8f9b8ba3c632ba6ffa2`. Existing Identity production code provides password verification and bounded outcomes, prospective-password preparation, Login-email preparation and Session storage/revocation. Their tests and module architecture tests cover those bounded responsibilities. There is no authoritative credential source or credential producer at this baseline. Session confirmed-absence behavior is not credential-source completeness evidence. The existing build includes the governed JDBC/PostgreSQL infrastructure; dependency availability is not credential authority.

The proposed realization is an Identity-owned credential source backed by the already-governed PostgreSQL/Spring Data JDBC architecture, reached through a project-owned inward source-access responsibility. An Identity-owned producer validates the access result before a consumer can rely on it under ICED. This is a concrete ownership and access path for later implementation review, not a claim that a populated source, executable Port, physical model or consistency mechanism already exists.

## 2. Governing authority and source register

| Label | Source and applicable authority |
| --- | --- |
| G1 | [ICED](authoritative-credential-evidence-design.md), REQ-ICED-001–011 and §6: subject, production provenance, outcomes, consumption and confirmation mechanics. |
| G2 | [ICRD](customer-credential-state-design.md), REQ-ICRD-001–012: establishment prerequisites, currentness, concurrency and observation-only boundaries. |
| G3 | [Identity backend](identity-backend.md), BIDN-REQ-010–019, 032–034, 038–043 and 048–050; [Identity domain](../../domains/identity/identity-domain.md), REQ-IDN-004, 012–017 and 039–044: owned state, failure and reconciliation. |
| G4 | [DEC-0003](../../decisions/DEC-0003-initial-local-customer-credential-mechanism.md), credential category; [DEC-0006](../../decisions/DEC-0006-customer-password-hashing-verification-strategy.md), verifier representation, containment, matching and upgrade; [DEC-0007](../../decisions/DEC-0007-initial-customer-password-policy.md), complete establishment policy; [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md), separate current-email gate. |
| G5 | [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md), local Identity authority; [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), business/association separation; [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md) and [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), distinct Session authority. |
| G6 | [ADR-0017](../../adr/ADR-0017-postgresql-schema-strategy.md), persistence ownership; [ADR-0018](../../adr/ADR-0018-persistence-technology.md), Spring Data JDBC and inward Ports; [DATABASE](../../../.ai/backend/DATABASE.md), especially §§8–13; [POSTGRES](../../../.ai/backend/POSTGRES.md), physical ownership/integrity; [JAVA](../../../.ai/backend/JAVA.md) and [SPRING](../../../.ai/backend/SPRING.md), model and infrastructure separation. |
| G7 | [Login Contract](../../contracts/identity/initial-customer-login-contract.md), §§6–10 and concurrency/failure rules; [Login OpenAPI](../../contracts/identity/initial-customer-login.openapi.yaml); [Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), §§9, 12–14 and reconciliation; [Registration OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml): existing ownership and consumption constraints only. |
| G8 | [AGENTS](../../../.ai/core/AGENTS.md), hierarchy and specification-before-implementation; [Glossary](../../../.ai/core/GLOSSARY.md), canonical terms; [Product](../../../.ai/core/PRODUCT.md), credential and verification policy; [Architecture](../../../.ai/core/ARCHITECTURE.md), ownership and inward dependencies. |
| G9 | [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), default denial and containment; [Coding Standards](../../../.ai/core/CODING-STANDARDS.md), invariants and non-speculative design; [Engineering Principles](../../../.ai/core/ENGINEERING-PRINCIPLES.md), truthful uncertainty and authority. |
| G10 | [Testing Standards](../../../.ai/core/TESTING-STANDARDS.md), deterministic tests and realistic persistence verification; [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), lifecycle and traceability; [DECISIONS](../../../.ai/core/DECISIONS.md), material-decision escalation; [DEC-0001](../../decisions/DEC-0001-backend-build-tool-dependency-management.md), separate dependency admission. |
| G11 | [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md) and [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md): Proposed/non-authoritative, unresolved/deferred dispositions and exhausted evaluation boundaries, not accepted strategies. |

The governing sources and implementation inspected in the post-ICED audit remain unchanged at this baseline. No subordinate source overrides Accepted/Approved authority.

## 3. Source/authority distinctions

These descriptions allocate responsibilities, not glossary terms, Java types or stored statuses.

| Concern | Responsibility and limit |
| --- | --- |
| Authoritative credential state | Identity-owned accepted credential facts and applicable confirmed effects under governed operations. Authority derives from their lawful production and continued applicability, not storage alone. |
| Persistence representation | A faithful realization of those facts in Identity-owned storage. A database commit establishes durability within its guarantees, not password-policy acceptance or lawful establishment by itself. |
| Source-access responsibility | An inward project-owned boundary through which the producer requests the intended subject/context's authoritative source observation and learns its confirmation limits. It is not an arbitrary CRUD API. |
| Producer | Identity's responsibility for deciding whether the observation meets ICED's evidence basis, including binding, provenance and currentness. A persistence Adapter implements access, not independent policy authority. |
| Produced evidence | The bounded ICED result justified by that observation. It is neither a new source of business truth nor an indefinite guarantee. |

The logical access path is: already authoritative Identity/context → Identity producer → project-owned source-access boundary → Identity persistence Adapter → Identity-owned credential state in governed PostgreSQL → source observation → producer confirmation → bounded ICED consumption. These are responsibilities; separate classes, services or round trips are not mandated. No submitted email, Customer record, Session record, projection or caller flag substitutes for the source.

## 4. Normative design rules

### REQ-ICSC-001 — Source ownership

The source used for credential confirmation MUST represent Identity-owned credential truth under G2/G3/G5. Persistence and access MUST preserve that ownership; neither the database, Adapter, caller nor another Module may become an independent authority by storing or returning a value. Customer/Account truth and ADR-0022 associations remain distinct. Sources: G1 REQ-ICED-001/003; G2 REQ-ICRD-001; G3 BIDN-REQ-032; G5; G6; G8.

### REQ-ICSC-002 — Lawful source population

A persisted credential fact MUST be eligible for authoritative reliance only when attributable to an accepted Identity-owned outcome of a separately governed population path. Future establishment/change/migration paths must supply their required authority and confirmed outcome before their records can support evidence; this requirement authorizes none of those paths. Test, fixture, seed and import data MUST NOT prove production authority merely by existing or matching the storage shape. Sources: G1 REQ-ICED-003/010; G2 REQ-ICRD-003/007; G3; G4 DEC-0007; G7; G9.

There is no currently demonstrated production population path. Complete establishment remains blocked where DEC-0010's mandatory check is unresolved. A stored acceptance flag, successful hash or synthetic population is not a substitute. Later review must trace lawful population and mapping; this design requires no provenance column, event store or credential-history schema.

### REQ-ICSC-003 — Stable subject access

Source access MUST be scoped to an already authoritatively established stable Identity and the requested governed credential context. A supplied reference identifies the requested subject only; the source must establish the owned binding. Submitted Login email, Customer/Account references and caller assertions MUST NOT supply that authority. Sources: G1 REQ-ICED-001/002/005; G2 REQ-ICRD-004/009; G3 BIDN-REQ-010/011; G5 ADR-0022; G7.

No public identifier, database key representation, canonical Login key or association cardinality is selected. Future Login reaches this responsibility only after separately governed canonical resolution.

### REQ-ICSC-004 — Verifier and context binding

Any verifier material supplied for matching MUST come from the same confirmed source context and subject relied upon by the producer. Source access/mapping MUST preserve this relationship without pairing a confirmed subject with independently caller-selected material or combining incompatible observations. Verifier contents alone prove neither ownership nor applicability/currentness. Sources: G1 REQ-ICED-003/006/008; G2 REQ-ICRD-002/004; G4 DEC-0006; G7 Login §8.

The handoff to `CustomerPasswordVerificationPort` remains inside the bounded Identity verification path. Passwords remain transient; verifier material must not escape through ordinary results, public/cross-domain Contracts, diagnostics, logs, metrics, traces, events or support evidence. No evidence-object payload or storage field is prescribed.

### REQ-ICSC-005 — Positive confirmation gate

The producer MUST emit confirmed applicable/current evidence only when the reviewed source observation establishes all of: lawful source provenance under REQ-ICSC-002, intended subject binding, applicable credential context, consistent verifier binding where material is supplied, and currentness consistent with relevant confirmed superseding/withdrawing effects. Access must provide a consistency basis sufficient for the governed operation; missing or contradictory confirmation cannot be filled by inference from row presence or password match. Sources: G1 REQ-ICED-003/004/006; G2 REQ-ICRD-004/005; G3 BIDN-REQ-012/040/042; G7 Login §8.

This gate is conditional, not a claim that the baseline can emit a positive result. It requires no stored `CURRENT` status and proves neither password match nor the remaining email-verification, eligibility, Authentication, Principal, Session, association or Authorization gates.

### REQ-ICSC-006 — Confirmed absence deferred

Production emission of ICED confirmed absence is **DEFERRED** in this initial source design: no credential-specific completeness basis is demonstrated. Empty, missing, incomplete or unavailable access MUST NOT produce confirmed absence; where no other ICED outcome is authoritatively established, confirmation remains uncertain and reliance fails safely. Sources: G1 REQ-ICED-004/007 and §6; G2 REQ-ICRD-005/007; G3 BIDN-REQ-038–040.

Supporting absence later requires review evidence that the queried scope covers the authoritative state for the intended subject/context, access completed successfully and applicable concurrent effects cannot invalidate the claimed absence. This does not require every ICED semantic result to exist in the first implementation, redefine absence, authorize establishment or infer absence from the Session adapter.

### REQ-ICSC-007 — Source failure classification

The access/producer path MUST distinguish a justified positive result from established contradiction, unavailable/incomplete confirmation and authoritatively established non-currentness according to ICED. Conflicting observations yield no arbitrary current winner; an inability to establish whether evidence is stale remains uncertainty. Database errors, incomplete reads and ambiguous mappings MUST NOT become absence or currentness. Sources: G1 REQ-ICED-004/007; G2 REQ-ICRD-007; G3 BIDN-REQ-038–040; G7; G9.

Internal distinctions remain subject to existing Contract disclosure rules and select no HTTP outcome. They grant no permission to create, replace or overwrite state.

### REQ-ICSC-008 — Concurrent applicability

A producer MUST NOT claim currentness from an observation that fails to account for relevant already-confirmed superseding/withdrawing authority. Later implementation review must identify what source observation supports the claim, how relevant concurrent changes affect it and when applicability must be re-established before reliance. If that consistency basis cannot be demonstrated, the producer cannot emit positive confirmation. Sources: G1 REQ-ICED-009; G2 REQ-ICRD-004–007; G3 BIDN-REQ-033/034; G4 DEC-0006 upgrade/concurrency; G7.

This is an operation-scoped consistency obligation, not a permanent guarantee against future change or a selected global consistency model. It chooses no optimistic/pessimistic lock, version column, timestamp, isolation level, retry count, SQL locking clause or transaction implementation. Known contradictory effects cannot be disregarded merely because an earlier read succeeded.

### REQ-ICSC-009 — Reconciliation without mutation authority

Conflicting or uncertain source observations MUST remain unresolved until Identity-owned authoritative evidence establishes the applicable outcome. Reconciliation must preserve confirmed authority and relevant history without arbitrary selection, stale restoration or an inferred overwrite permission. Reading or comparing observations authorizes no repair operation or general recovery workflow. Sources: G1 REQ-ICED-007/009; G2 REQ-ICRD-005–007; G3 BIDN-REQ-039/040; G9.

### REQ-ICSC-010 — Source-access contract responsibility

The inward source-access boundary MUST express the producer's need for the intended stable subject/context's authoritative observation, including binding and limits on confirmation. Its input requests observation, not an authoritative result chosen by the caller; its output must enable the producer to justify or withhold ICED confirmation. Failures/incompleteness must remain distinguishable where handling differs. Sources: G1 REQ-ICED-003–005; G3 BIDN-REQ-032/038; G6 DATABASE §§8–9; G8 Architecture.

The Adapter must expose neither storage/framework types nor a false positive when access fails. This resolves responsibility and guarantees, not a Java signature, result class, repository method or mandated decomposition. A later review must show the real source-access path rather than present an interface with a mocked implementation as production confirmation.

### REQ-ICSC-011 — Existing persistence realization

The proposed initial persistent source MUST preserve the already-governed Identity-owned PostgreSQL and Spring Data JDBC architecture, project-owned inward boundaries, explicit faithful mapping and owner-controlled migration/access responsibilities. The physical realization must support the binding and consistency obligations above without manufacturing policy through persistence defaults. Sources: G2 REQ-ICRD-011; G3 BIDN-REQ-032/033; G6; G8 Architecture.

This applies ADR-0017/0018 rather than selecting a new persistence strategy or dependency. It defines no table, column, concrete schema, index, foreign key, constraint, ORM entity, SQL, repository signature or lock syntax. Session infrastructure is not repurposed into a credential authority. Physical design and applicable integration validation remain for later implementation review; no new provider, cache or topology is selected.

### REQ-ICSC-012 — Production evidence and prerequisites

Later implementation review MUST demonstrate the actual producer/access path, lawful source population boundary, faithful subject/verifier mapping, supported outcome handling and concurrent applicability. Test doubles may validate consumers but MUST NOT be used as proof of production provenance/currentness. Real persistence validation must test the eventual mapping and consistency guarantees; synthetic test records demonstrate mechanics only, not completed production policy acceptance. Sources: G1 REQ-ICED-003/010/011 and acceptance criteria; G2 REQ-ICRD-003/009/012; G3 BIDN-REQ-048–050; G10; G11 dispositions.

DEC-0009 resolution and DEC-0010 policy acceptance cannot be supplied by fixtures, source constructors or stored flags. Neither exhausted evaluation may resume. Implementation must stop at an unsupported material Product/Security/Architecture choice, and this Approved Specification cannot authorize a production slice.

## 5. Design-level acceptance criteria

These are deterministic review scenarios for later implementation evidence, not claims of executed tests. Each criterion inherits its requirement's source attribution.

| Criterion | Trace | Review expectation |
| --- | --- | --- |
| 1 | REQ-ICSC-001 | Trace the source path to Identity-owned credential truth; Customer, Session, caller and database-only assertions cannot establish it. |
| 2 | REQ-ICSC-002 | A manually inserted, seeded or imported row and a valid hash do not prove lawful establishment. Review cannot approve positive production capability without a governed population path satisfying all required policy. |
| 3 | REQ-ICSC-003 | Submitted email or another subject's reference cannot substitute for authoritative resolution/binding; no public key format or cardinality is inferred. |
| 4 | REQ-ICSC-004 | Substitute another context's verifier or combine mismatched observations: the path cannot claim matching for the confirmed context. Ordinary results and diagnostics expose no secret/verifier material. |
| 5 | REQ-ICSC-005 | Independently remove each positive-gate guarantee: no positive result follows. Even with all guarantees established, a result proves neither password match nor downstream gates. |
| 6 | REQ-ICSC-006 | Empty, incomplete, failed and unavailable reads emit no confirmed absence. Review confirms the absence capability is deferred rather than supplied by a fixture or borrowed Session behavior. |
| 7 | REQ-ICSC-007 | Established contradiction yields no winner; unknown currentness remains uncertain; established stale state is not current. Database failures never become absence or externally invented responses. |
| 8 | REQ-ICSC-008 | Exercise a relevant confirmed supersession/withdrawal and a delayed earlier observation: the implementation cannot rely on the earlier observation as current contrary to that authority. Review includes controlled concurrent interleavings against the eventual real persistence path, not only mocked outcomes. |
| 9 | REQ-ICSC-009 | Unresolved observations remain unresolved; reconciliation neither restores old authority nor performs an ungoverned repair/write. |
| 10 | REQ-ICSC-010 | Trace a request through the real producer and access boundary; caller flags cannot choose authority, failures remain explicit and framework/storage types do not leak inward. |
| 11 | REQ-ICSC-011 | Physical-design review traces mappings to owned facts and existing architecture; no database default invents policy and no Session storage becomes credential truth. This Approved Specification contains no physical names or mechanics. |
| 12 | REQ-ICSC-012 | Review separates consumer-unit evidence, persistence mechanics and lawful production authority. Mocked canonicalization or blocklist acceptance satisfies neither DEC; no test success renews evaluation or authorizes implementation. |

## 6. Open questions and escalation boundaries

- **Population remains blocked:** there is no demonstrated lawful production credential population at this baseline. DEC-0010 prevents complete establishment where its mandatory check is required. No migration/import or recovery path is authorized as an alternative. The source design is conditional; it cannot make an empty or synthetically populated database a useful positive-evidence producer.
- **Physical mapping and access implementation:** later review must propose the concrete source representation, inward types and access implementation and show how each guarantee is fulfilled. Ordinary Java choices may be made under existing standards; they need not become material decisions solely because this Approved Specification omits names/signatures.
- **Concurrent confirmation proof:** a concrete source observation/reliance boundary and its enforcement remain to be demonstrated. This Approved Specification fixes the required safety property but selects no global consistency strategy. If satisfying it requires a material trust-boundary or Architecture change, stop and obtain the applicable durable governance; do not silently weaken the claim.
- **Absence:** initial emission is deferred. A future review must supply completeness evidence before enabling it; successful empty access alone is insufficient.
- **Other gates and operations:** canonical Login resolution, current-email verification implementation, eligibility/lifecycle policy and recovery/change workflows remain separately governed. This source neither answers those questions nor authorizes their implementation.

No new durable material decision was made or found necessary merely to express this conditional source/access design under existing architecture. No concrete mechanism is represented as proven. Any later material ownership, Product, Security, trust-boundary, technology/provider or Architecture choice must be escalated rather than decided here.

## 7. Explicit non-decisions and review

This Approved Specification supplies no IDNA technology, canonical Login key or lookup from submitted email; DEC-0009 remains unresolved/deferred. It supplies no blocklist source/provider/dataset/library/service or complete password-policy acceptance; DEC-0010 remains unresolved/deferred. Both exhausted evaluation boundaries remain closed.

No route, method, status, DTO, header, cookie, OpenAPI schema or externally observable Login/Registration behavior changes. No dependency or provider is selected or admitted; DEC-0001 remains unchanged. No physical schema, SQL, index, constraint, lock, transaction or cache mechanism is chosen.

No establishment, migration, password reset/change/replacement, recovery, credential withdrawal/invalidation, Identity enablement/disablement/reactivation, Customer/Account lifecycle, association cardinality or social/external IdP behavior is authorized. Observing already-authoritative effects does not authorize producing them. Currentness is not password match, Authentication, Principal, Session or Authorization.

Controlled approval follows clean approval-readiness review of Identity, Security, Architecture and Engineering boundaries, including affected Customer boundaries where applicable. Later implementation review remains separate. Approval and implementation eligibility are separate. This subordinate Specification requires no DECISIONS index update merely for creation and amends neither ICED nor ICRD. No executable tests or production implementation are introduced by this documentation task.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-07 | Approved | Controlled approval establishes the bounded Identity-owned credential source/access and confirmation design; preserves conditional lawful source population, subject/verifier binding, positive-confirmation requirements, uncertainty and concurrent applicability; keeps confirmed absence deferred pending completeness evidence and DEC-0009 and DEC-0010 unresolved/deferred; reopens neither exhausted evaluation; selects no new persistence technology or physical persistence mechanics, changes no Contract, selects no dependency/provider and grants no lifecycle-operation authority. Approval does not itself establish production implementation eligibility. |
| 0.1.0 | 2026-10-07 | Draft | Bounded the Identity-owned credential source/access path under existing persistence architecture, conditional lawful population, subject/verifier binding and positive confirmation guarantees; deferred confirmed absence pending completeness evidence; preserved uncertainty, concurrent applicability, deferred strategies and separate implementation/material-decision gates without new operation authority. |
