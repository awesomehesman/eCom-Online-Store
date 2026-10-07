---
title: Credential Source Publication and Observation Protocol Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-07
authoritative: false
---

# Credential Source Publication and Observation Protocol Design

## 1. Purpose, scope and lifecycle

This Specification specializes the publication/observation relationship beneath Approved ICRD, ICED and ICSC. Scope code **ICPO** identifies its requirements. Those sources retain their state, evidence and confirmation meanings. This document defines no credential-establishment operation and grants no production implementation authority.

Inspected baseline: `c94dc02294fbe0d47880f7ccc40d95138eee9055`. The governing sources and implementation inspected for the post-ICSC audit remain unchanged. Identity currently implements password verification, prospective-password and Login-email preparation, and Session storage/revocation with bounded tests and architecture checks. The Flyway migration implements Session objects, not a credential publication source. No publication implementation, authorized establishment producer or lawfully populated credential records are demonstrated. Existing JDBC dependencies and a Session schema do not supply those capabilities.

The protocol links an already-accepted Identity credential fact to faithful publication, a coherent source observation and bounded ICED evidence. It specifies which relationships must hold, not tables, Java signatures or a globally ordered event history. Production readiness remains subject to later implementation review.

## 2. Governing authority and source register

| Label | Source and applicable authority |
| --- | --- |
| G1 | [ICSC](credential-source-confirmation-design.md), REQ-ICSC-001–012: source confirmation and conditional population; [ICED](authoritative-credential-evidence-design.md), REQ-ICED-001–011 and §6: subject, production provenance, outcomes, consumption and confirmation mechanics. |
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

G11 records deferred boundaries only; it supplies no accepted implementation strategy. This Specification uses existing Accepted/Approved authority for its normative obligations.

## 3. Publication/observation terminology and distinctions

The following descriptions are local protocol distinctions, not new glossary definitions, stored statuses or prescribed classes.

| Distinction | Meaning and limit |
| --- | --- |
| Accepted fact | A credential outcome already accepted by a separately governed Identity operation for its stable subject/context. Publication cannot manufacture that acceptance. |
| Publication | Faithful availability of that fact through the Identity-owned authoritative source, with preserved attribution and binding. It is not a new credential operation. |
| Durable representation | Stored realization of the publication. Durability alone proves neither acceptance nor current applicability. |
| Publication identity | The semantic ability to recognize the same accepted fact and its binding on repeat delivery. No new identifier format or physical identity field is required here. |
| Supersession relationship | An already-confirmed, separately authorized effect makes a prior publication no longer applicable in the affected credential context. The relationship comes from that effect, not arrival order. |
| Observation | A source-backed account of a publication's binding and applicability, including relevant confirmed effects and confirmation limits. Successful retrieval alone does not make it valid for reliance. |
| Produced evidence | The ICED result justified by a valid observation under ICSC. It is not password MATCH, Authentication or a replacement source of truth. |

## 4. Normative design rules

### REQ-ICPO-001 — Acceptance precedes publication authority

A publication path MUST require an already-accepted Identity-owned outcome from a separately governed operation before it can publish a fact as authoritative. It must preserve the outcome's attribution rather than infer acceptance from a hash, row shape, stored flag, caller assertion, fixture, seed, manual insert or import. This protocol does not determine that any such accepted outcome currently exists or authorize its creation. Sources: G1 REQ-ICSC-002/012 and REQ-ICED-003; G2 REQ-ICRD-003; G3; G4 DEC-0007; G9.

### REQ-ICPO-002 — Publication identity and binding

The publication path MUST preserve the relation between the accepted fact, its already-authoritative stable Identity subject, governed credential context and verifier representation where applicable. Re-observing or retrying the same fact must retain that relation; a different binding cannot be represented as the same accepted fact. Verifier contents or submitted email MUST NOT establish any part of its authority. Sources: G1 REQ-ICSC-003/004 and REQ-ICED-001/006; G2 REQ-ICRD-001/002/004; G4 DEC-0006; G5 ADR-0022; G7.

This requires semantic same-fact recognition, not a public identifier, Login key, globally unique publication-ID format, database key, one-credential-per-Identity constraint or Customer/Account cardinality.

### REQ-ICPO-003 — Bounded publication success

Publication success MUST mean only that the already-accepted fact has been faithfully made available through the authoritative source with its required binding and applicability relationships. A database commit alone cannot establish the upstream accepted outcome. Partial, failed or uncertain publication must not be represented as success or as proof that no effect occurred. Sources: G1 REQ-ICSC-002/005/007; G2 REQ-ICRD-005/007; G3 BIDN-REQ-038–040; G6 DATABASE §§11–14; G9.

Success establishes no new password-policy acceptance, eligibility, password match, Authentication, Principal, Session, Authorization, current-email verification, Customer/Account ownership or association cardinality. This defines no separate distributed acceptance transaction or intermediate stored lifecycle state.

### REQ-ICPO-004 — Supersession is attributable, not inferred ordering

A later already-confirmed credential effect MUST constrain the applicability of the prior publication it affects in the same governed context. The source must preserve enough of that relationship for observation to exclude the prior publication from current reliance when the effect makes it inapplicable. Delivery order, row order, timestamps or a larger identifier MUST NOT alone establish supersession. Sources: G1 REQ-ICSC-005/008 and REQ-ICED-009; G2 REQ-ICRD-004–006; G3 BIDN-REQ-033/034; G4 DEC-0006 concurrency; G7.

Only the affected relationship is required. No total order across Identities or contexts, global sequence, event sourcing, credential-history architecture or stored ACTIVE/CURRENT flag is selected. A withdrawing effect need not supply a replacement; inability to rely on the prior fact is not confirmed absence. Describing effects authorizes no change, reset, recovery, withdrawal, invalidation, migration, rehash or eligibility transition.

### REQ-ICPO-005 — Coherent observation basis

An observation supporting ICSC positive confirmation MUST establish together the accepted-fact publication provenance, stable subject/context binding, associated verifier where supplied, and applicability relative to relevant confirmed superseding/withdrawing effects. It must not combine a verifier from one publication with another publication's confirmation, or omit an effect required to assess applicability. Missing, incomplete or contradictory confirmation withholds positive evidence. Sources: G1 REQ-ICSC-004/005/007 and REQ-ICED-003/004/006; G2 REQ-ICRD-004; G7 Login §8.

These facts need a coherent observation, not necessarily separate records or a particular number of reads. No password comparison occurs in this protocol. Secret/verifier material remains inside its bounded Identity handling path, excluded from ordinary evidence, public/cross-domain Contracts, diagnostics, telemetry, events and support evidence under G4/G9.

### REQ-ICPO-006 — Observation and reliance boundary

The producer MUST identify the source observation on which confirmation rests and the governed operation/context for which it is relied upon. Publication binding and relevant applicability relationships must remain consistent for that reliance. A previously successful observation cannot justify current reliance contrary to an already-confirmed relevant effect. Where intervening change or inability to establish consistency makes the observation insufficient, applicability must be re-established or positive confirmation withheld. Sources: G1 REQ-ICSC-008 and REQ-ICED-002/009; G2 REQ-ICRD-004–007; G3 BIDN-REQ-033/039; G7.

The future publication/read implementation must jointly demonstrate this property: an accepted superseding effect cannot be treated as irrelevant merely because its source representation lags. If visibility/confirmation cannot be established, the observation is non-confirming. This is not a permanent guarantee against future change, a new effect-confirmation workflow or a selected isolation level. Locks, versions, timestamps, retries, SQL clauses and transaction propagation remain implementation-review matters under G6; any material consistency/trust change requires escalation.

### REQ-ICPO-007 — Duplicate publication preserves authority

Retry or duplicate delivery of the same accepted fact MUST preserve its subject/context/verifier binding and accepted applicability relationships without creating another credential, additional authority or a new supersession. An earlier fact replayed after confirmed supersession cannot restore its former applicability. If sameness cannot be established safely, no fresh authority may be inferred. Sources: G1 REQ-ICSC-008/009; G2 REQ-ICRD-006; G3 BIDN-REQ-034/039; G7 concurrency/reconciliation.

No external idempotency Contract, retention period, key format or deduplication storage is selected. A retry cannot be represented as a newly accepted establishment intent merely because prior publication completion is uncertain.

### REQ-ICPO-008 — Conflicting observations do not select authority

Incompatible publications or unresolved applicability MUST yield ICED conflict/uncertainty as justified by the evidence, rather than arbitrary selection or overwrite. Failure to read or confirm must not become positive evidence. Reconciliation may establish an outcome only from authoritative evidence and grants no automatic repair, mutation or general recovery authority. Sources: G1 REQ-ICSC-007/009; G2 REQ-ICRD-005/007; G3 BIDN-REQ-038–040; G9.

### REQ-ICPO-009 — No absence-completeness inference

Production confirmed absence MUST remain deferred as specified by ICSC. Empty, missing, failed or incomplete source observations cannot establish absence or permit establishment. Publication identity or a known supersession relationship does not demonstrate complete coverage of the requested subject/context. Sources: G1 REQ-ICSC-006 and REQ-ICED-004/007; G2 REQ-ICRD-007.

### REQ-ICPO-010 — Faithful persistence realization

A later realization MUST preserve accepted-fact attribution, same-fact recognition, subject/context/verifier binding and applicable supersession relationships within existing Identity-owned PostgreSQL/Spring Data JDBC boundaries. Storage must not create acceptance or lifecycle policy through defaults, constraints or access convenience. Sources: G1 REQ-ICSC-010/011; G3 BIDN-REQ-032/033; G6; G8 Architecture.

These logical obligations prescribe no table, column, SQL, index, concrete foreign key, repository signature, ORM entity or transaction/lock implementation. They require no retention of prior raw passwords or an additional verifier-history store. Physical choices can remain ordinary implementation details where they preserve the reviewed relationships and existing authority; material changes must be escalated.

### REQ-ICPO-011 — Production-path proof

Later implementation review MUST demonstrate that the actual publication entry path accepts only separately authorized Identity outcomes, mappings preserve binding, and observations account for relevant confirmed effects. It must identify how legitimate source writes are controlled and how unsupported provenance or integrity uncertainty prevents authoritative reliance; satisfying storage shape alone is not proof. Sources: G1 REQ-ICSC-012; G2 REQ-ICRD-008/012; G3 BIDN-REQ-041/048–050; G6; G9; G10.

Mocks and fixtures may verify consumer logic and persistence/concurrency mechanics only. This protocol does not claim that a reader can cryptographically distinguish an arbitrary privileged database alteration by inspecting an identical row, nor select an attestation mechanism. Production provenance requires evidence of the governed write/access path and its controls, not a self-asserted acceptance flag. Any stronger integrity/trust mechanism requiring a material decision must be separately governed.

### REQ-ICPO-012 — Population and deferred-prerequisite boundary

Defining this protocol MUST NOT be represented as implementing publication mechanics, possessing an authorized establishment producer or having lawfully populated production records. DEC-0009 canonical resolution and DEC-0010 complete password-policy acceptance remain unresolved/deferred and cannot be supplied by publication or test data. Neither exhausted evaluation may resume. Sources: G1 REQ-ICSC-002/012; G2 REQ-ICRD-003/009/012; G4 DEC-0007/0008; G7; G10; G11 dispositions.

Future Login consumption still requires separately governed canonical resolution; no IDNA technology, canonical submitted-email key or authoritative lookup is supplied here. Establishment remains blocked where its mandatory blocklist check is unresolved; no migration, import, seed or manual population is authorized as a workaround. Approval of this design does not itself establish implementation eligibility.

## 5. Design-level acceptance criteria

Each criterion inherits its requirement's source attribution. These are later review scenarios, not claims that executable tests have been written or run. Synthetic data demonstrates mechanics under stated assumptions, never production acceptance.

| Criterion | Trace | Deterministic review expectation |
| --- | --- | --- |
| 1 | REQ-ICPO-001 | Submit a hash, acceptance flag or arbitrary row without governed upstream acceptance: no accepted publication can be claimed. The legitimate entry path must reject or withhold authority for such input. |
| 2 | REQ-ICPO-002 | Substitute a subject, context or verifier for the same fact: it cannot retain the original binding or be treated as the same publication. Submitted email is not an authoritative subject. |
| 3 | REQ-ICPO-003 | Successful storage without upstream acceptance is insufficient; partial or uncertain publication yields no claimed success or presumed absence. Confirmed publication does not satisfy any downstream gate. |
| 4 | REQ-ICPO-004 | A confirmed effect targeting an earlier publication prevents its current use in the affected context; delivery reordering alone neither establishes nor reverses that relationship. No operation creating the effect is authorized. |
| 5 | REQ-ICPO-005 | Remove each required observation guarantee or mix verifier and confirmation from incompatible observations: positive evidence is withheld. Ordinary results and diagnostics contain no verifier/secret material. |
| 6 | REQ-ICPO-006 | Interleave prior observation, confirmed superseding effect and later reliance: the earlier observation cannot justify reliance contrary to the effect. Delayed effect visibility or unknown consistency requires re-establishment or withholding, not stale positive evidence. |
| 7 | REQ-ICPO-007 | Retry the same accepted fact, including after supersession: no extra credential, changed binding, new authority or restoration follows. Uncertain sameness is not a new establishment. |
| 8 | REQ-ICPO-008 | Incompatible publications yield no arrival-order winner or automatic repair; failed/incomplete reads do not yield positive confirmation. |
| 9 | REQ-ICPO-009 | Empty source access, read failure and a superseded publication yield no confirmed absence or establishment permission. |
| 10 | REQ-ICPO-010 | Review the later mapping against each logical relationship; no persistence default invents policy, history or cardinality. This Specification selects no physical mechanism. |
| 11 | REQ-ICPO-011 | Trace production acceptance through controlled publication and real source access. Fixture insertion and mocked positives cannot satisfy that proof; assess unsupported-write/integrity handling separately from schema-shape checks. |
| 12 | REQ-ICPO-012 | Mocked canonicalization or blocklist success cannot demonstrate production prerequisite satisfaction. Review distinguishes protocol definition, implemented machinery, authorized population and actual production data; neither evaluation is renewed. |

## 6. Open questions and escalation boundaries

- **Concrete enforcement:** later implementation review must demonstrate same-fact recognition, faithful binding and coherent observation/reliance against the real persistence path. Class names, internal representations and physical mapping may be ordinary implementation choices; they are not independent authority. No additional broad credential-semantics design is required merely because those details are omitted.
- **Visibility across confirmed effects:** the mechanism connecting a relevant confirmed effect to safe observation/reliance remains to be selected and validated under existing database/transaction standards. This protocol requires withholding where that relation cannot be established. If fulfilling it requires a new material consistency, trust-boundary or Architecture strategy, stop at that decision rather than infer approval here.
- **Population:** no currently authorized production establishment path is supplied. DEC-0010 remains a prerequisite where its mandatory policy check applies. The definition is conditional and does not authorize imports or recovery as alternatives.
- **Completeness:** confirmed absence remains deferred; the protocol supplies no source-completeness proof.
- **Integrity proof:** controlled write/access and mapping evidence must support production attribution. A cryptographic provenance mechanism, new privileged operating model or different trust boundary cannot be silently selected to distinguish arbitrary forged rows.

No durable material decision is made by this Specification. No concrete need for a new DEC/ADR was established merely to state these inherited obligations. Material choices encountered during realization must be escalated to the appropriate owners; their answers remain unselected.

## 7. Explicit non-decisions and review

This Specification defines no establishment, change/reset/recovery, withdrawal/invalidation, migration/import, rehash operation, eligibility transition, Customer/Account lifecycle, association cardinality or social/external IdP behavior. Representation or observation of a separately authorized effect does not authorize creating it. DEC-0006's conditional supported-verifier allowance remains unchanged; no legacy profile is admitted.

DEC-0009 and DEC-0010 remain Proposed/non-authoritative and unresolved/deferred. Neither evaluation is reopened. No IDNA or blocklist strategy, provider, dataset, library, service or policy workaround is selected. DEC-0001 dependency admission remains separate; no dependency/provider is added or admitted.

No route, HTTP method/status, DTO, header, cookie, OpenAPI schema or externally observable Login/Registration behavior changes. Existing persistence architecture remains unchanged; physical names, SQL, indexes, foreign keys, repository APIs, ORM entities, lock syntax and transaction implementation remain unselected.

Controlled approval follows clean approval-readiness review of Identity, Security, Architecture and Engineering boundaries, including affected Customer boundaries where applicable. Approval and later implementation eligibility remain separate. This subordinate Specification requires no DECISIONS index update merely for creation and amends none of ICSC, ICED or ICRD. No implementation, migration or test is introduced by this documentation task.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-07 | Approved | Approved the bounded publication/observation protocol after clean approval-readiness review; preserved accepted-fact distinction from publication/storage, subject/context/verifier binding, attributable supersession, coherent observation/reliance, duplicate/retry safety and conflict/uncertainty; kept confirmed absence deferred and production provenance tied to governed write/access controls. DEC-0009 and DEC-0010 remain unresolved/deferred; no evaluation reopened, credential lifecycle operation authorized, new persistence technology or physical persistence mechanism selected, or Contract/dependency/provider change made. Approval does not establish implementation eligibility. |
| 0.1.0 | 2026-10-07 | Draft | Defined conditional accepted-fact publication, binding, supersession relationships and coherent observation/reliance obligations; preserved duplicate safety, conflict/uncertainty, deferred absence and blocked population prerequisites under existing persistence architecture without new operation, evaluation or implementation authority. |
