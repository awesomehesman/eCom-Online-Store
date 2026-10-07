---
title: Customer Credential State and Currentness Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-07
authoritative: false
---

# Customer Credential State and Currentness Design

## 1. Purpose, scope and lifecycle

This Specification specifies the minimum internal semantic distinctions needed before implementing authoritative local Customer credential models, persistence ports or orchestration. It applies existing governance; it is Approved within its bounded internal design scope and grants no implementation authority. Scope code **ICRD** identifies this Specification's requirements only. These new design identifiers are not identifiers attributed to upstream documents.

Repository baseline inspected: `043ea5610c3c312a4ad0110a69962b8d3eb2e3ac`. Existing implementation supplies credential verification, login-email input preparation, prospective-password preparation, Session storage and revocation boundaries. It does not supply authoritative credential establishment/currentness. Existing verification accepts a supplied verifier representation; preparation explicitly awaits remaining password-policy checks. Existing tests establish those bounded behaviors, not a completed credential lifecycle.

The design distinguishes representation, proposed establishment, confirmed establishment, current authority, supersession/withdrawal and uncertainty. These distinctions are not a schema, enum, exhaustive state machine or policy authorizing new credential operations.

## 2. Governing authority and source register

Accepted/Approved requirements govern independently of this Specification. The source labels below support each normative design rule and its acceptance criterion.

| Label | Governing source and relevant scope |
| --- | --- |
| G1 | [AGENTS](../../../.ai/core/AGENTS.md), decision hierarchy, specification before implementation and Identity/Customer ownership; [Engineering Principles](../../../.ai/core/ENGINEERING-PRINCIPLES.md), explicit lifecycle and boundaries. |
| G2 | [Glossary](../../../.ai/core/GLOSSARY.md), Identity, Principal, Session, Authentication and Authorization; [Product](../../../.ai/core/PRODUCT.md), Customer password and current-email policy. |
| G3 | [Architecture](../../../.ai/core/ARCHITECTURE.md); [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md), credential authority and accepted Authentication; [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), association and owner-local outcomes. |
| G4 | [Identity backend](identity-backend.md), BIDN-REQ-010–019, 032–034, 038–043 and 048–050; [Identity domain](../../domains/identity/identity-domain.md), REQ-IDN-004, 012–017, 039–044. |
| G5 | [DEC-0003](../../decisions/DEC-0003-initial-local-customer-credential-mechanism.md), local password category; [DEC-0006](../../decisions/DEC-0006-customer-password-hashing-verification-strategy.md), representation, verification, upgrade/rehash, compatibility, failure and uncertainty. |
| G6 | [DEC-0007](../../decisions/DEC-0007-initial-customer-password-policy.md), complete establishment/change policy; [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md), current-email verification gate. |
| G7 | [DEC-0004](../../decisions/DEC-0004-customer-login-identifier-semantics.md) and [DEC-0005](../../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), identifier meaning, canonical comparison and retained ownership. |
| G8 | [Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), §§12–14 and concurrency/reconciliation; [Registration OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml); [Login Contract](../../contracts/identity/initial-customer-login-contract.md), §§6–10; [Login OpenAPI](../../contracts/identity/initial-customer-login.openapi.yaml). |
| G9 | [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), password, secret, default-denial and withdrawn-access controls; [Coding Standards](../../../.ai/core/CODING-STANDARDS.md), domain invariants and non-speculative design. |
| G10 | [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md) and [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), Session authority and storage; [Customer backend](../customer/customer-backend.md) and [Customer domain](../../domains/customer/customer-domain.md), business authority and credential non-authority. |
| G11 | [ADR-0017](../../adr/ADR-0017-postgresql-schema-strategy.md), ownership; [ADR-0018](../../adr/ADR-0018-persistence-technology.md), persistence architecture; [DATABASE](../../../.ai/backend/DATABASE.md), [POSTGRES](../../../.ai/backend/POSTGRES.md), [JAVA](../../../.ai/backend/JAVA.md), [SPRING](../../../.ai/backend/SPRING.md), [API](../../../.ai/backend/API.md), inward dependencies and explicit boundaries. |
| G12 | [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), Draft lifecycle, identifiers and open questions; [Testing Standards](../../../.ai/core/TESTING-STANDARDS.md), invariant/failure verification; [DECISIONS](../../../.ai/core/DECISIONS.md), specification versus material decision; [DEC-0001](../../decisions/DEC-0001-backend-build-tool-dependency-management.md), separate dependency admission. |

[DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md) and [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md) are Proposed/non-authoritative, deferred decisions, not accepted implementation strategies. Their exhausted evaluation boundaries remain constraints. This Specification supplies neither missing strategy nor new evaluation authority.

## 3. Terminology and semantic distinctions

Canonical terms retain G2 definitions. Lowercase credential descriptions below are local analytical distinctions, not new glossary definitions or prescribed stored statuses.

| Distinction | Meaning within this Specification |
| --- | --- |
| Password/verifier representation | Sensitive input or encoded verifier material. Shape/profile validity establishes neither owner nor currentness. |
| Proposed establishment | Intent awaiting every governed precondition and confirmed Identity outcome. Not an established credential or required provisional persistent record. |
| Authoritatively established | Identity has confirmed the governed establishment outcome for the intended Identity. Historical establishment alone does not establish current usability. |
| Currently authoritative | Owner-attributed accepted state applicable to the operation, with required currentness and absence of contradictory, superseding or withdrawing evidence established. Not a global eligibility or Authorization grant. |
| Superseded or withdrawn | Prior authority is no longer applicable because of a confirmed, separately authorized change or withdrawal. This describes effects already supported by G4/G5/G9, not a new recovery or retirement policy. |
| Uncertain | Confirmation or applicability cannot safely be established. This is a knowledge/outcome distinction, not a replacement credential or mandatory persisted status. |

## 4. Normative design rules

### REQ-ICRD-001 — Authority separation

Credential establishment, ownership and currentness MUST remain Identity-owned. Customer and Account business truth and ADR-0022 association authority MUST remain separate; credential operations MUST NOT create, merge or reassign business actors or imply association cardinality. Sources: G2–G4, G10.

### REQ-ICRD-002 — Representation is not authoritative state

Possession, parsing or profile validation of a verifier MUST NOT establish its authoritative owner or currentness. A successful cryptographic match against a supplied verifier MUST remain credential-match evidence only and MUST NOT independently prove Customer eligibility, Authentication, Principal, Session, association or Authorization. Sources: G3–G5, G8.

### REQ-ICRD-003 — Establishment preconditions

Confirmed establishment MUST require governed Identity-owned establishment intent/context, complete DEC-0007 policy acceptance, DEC-0006-compliant verifier generation and a confirmed owner-local result. Preparation, hashing success, caller assertion or a policy-shaped object MUST NOT substitute for any missing precondition. DEC-0010's unresolved blocklist check MUST remain unsatisfied rather than simulated or bypassed. Sources: G3–G6, G8–G9.

### REQ-ICRD-004 — Currentness evidence

Before relying on current credential state, a future implementation MUST establish authoritative provenance to the intended stable Identity, applicability of the accepted credential outcome to the current operation and consistency with confirmed supersession/withdrawal and supported-profile authority. Conflicting, stale, unavailable or ambiguous evidence MUST NOT be arbitrarily selected or treated as current. Currentness MUST NOT be inferred solely from representation contents, arrival order, a caller-selected reference or successful matching. Sources: G3–G5; G8 Login §8.

This rule requires evidence semantics, not a timestamp, version field, freshness duration, identifier format or storage query. It does not establish one credential per Identity for every future category or lifecycle.

### REQ-ICRD-005 — Guarded transition and currentness outcomes

Governed transitions and currentness observations MUST satisfy the applicable obligations below. Confirmed authoritative superseding or withdrawing evidence MUST NOT be ignored when determining whether prior credential evidence remains current/applicable. The observation rows define no replacement, recovery, withdrawal or invalidation operation; actual triggers, requester authority, workflows, recovery mechanisms and lifecycle policy remain separately governed. A transition lacking its separately governed operation authority MUST NOT execute merely because this Specification describes a state consequence. Sources: G3–G6, G8–G9.

| Transition/observation | Authority and preconditions | Confirmed result | Failure and uncertainty; preservation obligation |
| --- | --- | --- | --- |
| Proposed establishment → confirmed establishment | REQ-ICRD-003; same governed intent and Identity; duplicate/conflict checks | Only the accepted owner-local credential result is established; current use still requires REQ-ICRD-004 | Known rejection/failure establishes nothing. Uncertain completion stays uncertain until authoritative reconciliation; do not assert absence or retry as a fresh intent blindly. Existing confirmed state is not overwritten by the proposal. |
| Established state → currentness determination | REQ-ICRD-004 and current operation's required authority | Evidence establishes applicability only for the governed operation; no new credential is created | Failed or ambiguous resolution grants no success. Reading/determining applicability does not mutate confirmed state. |
| Observation of authoritative superseding evidence | Confirmed Identity-owned evidence applicable to the same credential context under existing authority; REQ-ICRD-004 | Prior credential evidence is assessed against the confirmed superseding authority; this observation creates no replacement | Stale prior evidence must not restore or overwrite newer confirmed authority. Uncertain applicability fails closed where existing Security/Identity authority requires it, without inventing a completed replacement or altering confirmed history. |
| Supported older verifier → current-profile rehash, only if later supported | DEC-0006 explicit older-profile support and successful verification; new salt/current profile | Confirmed upgrade without downgrade | Failed/uncertain upgrade preserves last confirmed verifier and cannot claim completed upgrade. DEC-0006's supported-verifier match allowance remains intact; other Authentication gates still apply. No legacy profile is admitted here. |
| Observation of authoritative withdrawing evidence | Confirmed Identity-owned evidence applicable to the credential context under existing Security/Identity authority; REQ-ICRD-004 | Prior evidence cannot be treated as current/applicable contrary to confirmed withdrawing authority; this observation performs no withdrawal or invalidation | Uncertain evidence fails closed where existing Security/Identity authority requires it; no completed withdrawal is inferred. Stale prior evidence cannot restore or overwrite newer confirmed authority, and retained history is not permission to ignore it. |
| Uncertain outcome → reconciled confirmed outcome | Identity-owned authoritative evidence of actual effects, current authority and accepted history | Confirm only the outcome supported by evidence | If unresolved, remain uncertain. No arbitrary winner, invented success, duplicated effect or restoration of withdrawn authority. |

These rows are logical obligations, not an exhaustive state machine, operation API or authorization for password reset, recovery or account reactivation.

### REQ-ICRD-006 — Concurrent and duplicate effects

Stale or concurrent work MUST NOT overwrite newer confirmed authority, downgrade an accepted verifier, restore withdrawn authority or establish contradictory current outcomes for the same governed credential intent. Duplicate processing MUST recognize confirmed effects and conflicts before creating another effect. Current operation authority MUST be re-evaluated where needed; replaying an earlier success is not permission to restore it. Sources: G4 BIDN-REQ-033–034; G5 upgrade/rehash; G8 concurrency and reconciliation.

### REQ-ICRD-007 — Failure, uncertainty and safe use

Known rejection, known failure and unconfirmed completion MUST remain distinguishable where handling differs. Uncertainty MUST NOT become confirmed establishment, currentness, successful Authentication or a fabricated assertion that no write occurred. The latest confirmed state/history MUST be preserved for reconciliation without treating retained history as permission to use unsafe or withdrawn credentials. The DEC-0006 supported older-profile upgrade-write allowance MUST remain distinct from uncertainty about credential ownership/currentness. Sources: G4, G5, G8–G9.

### REQ-ICRD-008 — Secret and verifier containment

Raw/normalized passwords MUST remain transient and purpose-limited; verifier material MUST remain protected Identity security data. Raw/normalized passwords and verifier material MUST NOT enter ordinary Contracts, Customer/Account state, URLs, logs, metrics, traces, events, analytics, diagnostic errors or support evidence. Internal outcomes MUST disclose only the minimum governed evidence and MUST NOT expose secrets through object diagnostics. Sources: G4 BIDN-REQ-016/041; G5 representation/verification; G6 safe validation; G9.

### REQ-ICRD-009 — Login boundary

Future Login MUST obtain authoritative credential context only through the separately governed identifier-resolution path after DEC-0005 canonical validation succeeds. Input preparation MUST NOT stand in for strict IDNA2008, a canonical key or authoritative lookup. Credential match/currentness MUST remain separate from DEC-0008 current-email verification, Identity eligibility, accepted Authentication, Principal and Session establishment and contextual Authorization. Sources: G3, G5–G8, G10. DEC-0009 remains unresolved.

### REQ-ICRD-010 — Registration and association boundary

Registration MUST NOT represent credential establishment or completion merely from password preparation, hashing, current credential evidence or a Customer reference. Required policy, identifier, verification, Customer/Account and association outcomes MUST remain independently confirmed under the Registration Contract. A credential or an Identity reference alone MUST NOT establish Customer/Account association or cardinality. Sources: G3, G6–G8, G10. DEC-0010 remains unresolved.

### REQ-ICRD-011 — Persistence and framework neutrality

Authoritative state confirmation, history, concurrency and reconciliation MUST preserve Identity ownership and project-owned inward boundaries. This design MUST NOT select a schema, table, column, repository implementation/interface, ORM, query, transaction implementation, locking primitive, database constraint, cache or provider. Accepted persistence architecture remains unchanged; physical design waits for separately reviewed implementation detail. Sources: G3–G4, G11–G12.

### REQ-ICRD-012 — Specification and unresolved-authority guard

This Specification MUST NOT authorize implementation, invent policy results or resolve an unsupported Product, Security or Architecture choice. Material unresolved choices MUST be referred to their governing authorities rather than answered through a type, enum, method, schema or test fixture. Neither exhausted evaluation may be resumed through this design. Sources: G1, G6, G12; recorded DEC-0009/DEC-0010 dispositions.

## 5. Design-level acceptance criteria

The following 12 review scenarios assess a later implementation proposal; they do not claim executable tests or constitute Specification approval. Each criterion inherits the sources of its traced requirement.

| Criterion | Trace | Deterministic review expectation |
| --- | --- | --- |
| 1 | REQ-ICRD-001 | A credential change leaves stable Identity and Customer/Account/association authority distinct; no implicit cardinality is introduced. |
| 2 | REQ-ICRD-002 | A supplied valid verifier and successful match alone cannot establish ownership, currentness or Authentication. |
| 3 | REQ-ICRD-003 | Prepared input plus a valid hash with missing complete policy acceptance cannot establish a credential; no fabricated blocklist result is accepted. |
| 4 | REQ-ICRD-004 | Conflicting owner/currentness evidence denies reliance instead of choosing by arrival order or verifier contents. |
| 5 | REQ-ICRD-005 | Every proposed transition maps to an applicable table row and authority; unsupported recovery/replacement triggers remain disabled/unimplemented. |
| 6 | REQ-ICRD-006 | A delayed prior write and a duplicate same-intent request cannot downgrade, duplicate, contradict or restore confirmed authority. |
| 7 | REQ-ICRD-007 | Unknown persistence completion yields uncertainty, not success or presumed absence; confirmed retained state does not override withdrawal. Supported rehash-write failure is distinguished from uncertain owner/currentness. |
| 8 | REQ-ICRD-008 | Outcome and diagnostic review finds no password/verifier disclosure; Customer records and ordinary response shapes contain no such material. |
| 9 | REQ-ICRD-009 | Prepared email without canonical validation cannot reach authoritative Login lookup; credential evidence alone cannot establish Principal or Session. |
| 10 | REQ-ICRD-010 | Missing policy, current-email verification or association evidence prevents completion; existing Customer/Account truth remains owner-controlled. |
| 11 | REQ-ICRD-011 | Design review finds no invented storage object, repository method, lock, schema or framework-bound domain API. |
| 12 | REQ-ICRD-012 | An unresolved policy/technology question is explicitly deferred; no stub, test success or Draft status is used as implementation approval. |

Later validation should apply G12 testing requirements at the lowest effective layer for invariants, failure/uncertainty, races and secret containment. Database behavior requires applicable realistic integration validation only when a separate persistence design is authorized. No tests are created or executed by this documentation step.

## 6. Open questions and escalation boundaries

Drafting stops short of choosing answers to these demonstrated gaps:

- **Strategy dependencies:** DEC-0009 canonicalization and DEC-0010 blocklist strategy remain deferred. Only their existing governance may authorize future work; this Specification supplies no evidence or success result.
- **Identity eligibility and lifecycle triggers:** Login §8 requires eligible Identity/credential state, but concrete enablement, disablement/reactivation, recovery authorization and Customer-facing consequences are not fully specified. New Product/Security policy requires separate durable governance with Identity/Security and affected Product authority. No default active flag or new trigger is selected.
- **Replacement/recovery workflow:** G4/G5/G9 require safe authorized effects but do not choose a recovery mechanism, requester evidence, API or operational timing. Applicable Security/Product/Contract governance must precede those operations.
- **Evidence representation and confirmation:** Identifier representations, currentness evidence, concrete model boundaries, persistence ports and reconciliation mechanics require later internal design review. Material authority changes require separate DEC/ADR governance; no particular mechanism is forced by this Specification.
- **Association cardinality and Customer lifecycle:** ADR-0022 and unresolved Product policy do not establish global cardinality or closure/reactivation semantics. Applicable Product/Architecture governance is required before relying on them.
- **Legacy compatibility:** DEC-0006 admits no older profile merely by explaining upgrade semantics. Introducing one requires its stipulated compatibility/support governance.

No new DEC or ADR is required merely to capture the above existing constraints in this Specification. Affected design/implementation must stop at any unresolved material choice; this document does not grant acceptance of those choices.

## 7. Explicit non-decisions and review

This Specification resolves neither DEC-0009 nor DEC-0010 and authorizes no evaluation, IDNA strategy, blocklist strategy, dependency or provider. It defines no database schema, concrete persistence interface, HTTP route/method/status/DTO, public API behavior, Contract amendment, recovery design, Customer eligibility policy, new Session behavior or association cardinality. It does not implement or simulate credential establishment, Login or Registration. It changes no upstream policy or authority.

Review is for Identity semantic ownership, Security preservation, Architecture boundaries and Engineering testability; Customer review applies to affected business/association boundaries. Controlled approval follows successful approval-readiness review with no Critical, High, Medium or Low findings. G12 governs the Draft-to-Approved lifecycle and any separate material decision. Approval of this internal design does not remove upstream dependencies or independently authorize a production slice.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-07 | Approved | Controlled approval after successful approval-readiness review of this bounded internal Identity design specialization; preserved existing Accepted/Approved upstream authority and unresolved/deferred DEC-0009 and DEC-0010; granted no renewed evaluation authority, selected no IDNA or blocklist strategy, selected or admitted no dependency or provider, defined no concrete persistence schema/interface and changed no public Contract. Approval does not itself authorize implementation through blocked prerequisites. |
| 0.1.0 | 2026-10-07 | Draft | Documented Identity-owned credential representation, establishment/currentness distinctions, guarded transitions, concurrency, uncertainty, containment and traceable review criteria; preserved deferred strategies and unresolved policy, Contract and persistence choices without implementation authority. |
