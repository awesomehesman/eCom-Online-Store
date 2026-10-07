---
title: Authoritative Credential Evidence and Consumption Boundary Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-07
authoritative: false
---

# Authoritative Credential Evidence and Consumption Boundary Design

## 1. Purpose, scope and lifecycle

This Specification specializes the Approved Customer Credential State and Currentness Design (ICRD), particularly §6, for review of a later authoritative credential evidence model and its inward-facing consumption boundary. Scope code **ICED** identifies this Specification's requirements. It defines semantic responsibilities and permitted conclusions, not executable interfaces or a persistence model. It is Approved within its bounded internal-design scope and grants no production implementation authority. Implementation remains subject to separately governed prerequisites and later implementation review.

The inspected canonical baseline is `7efe63f5359d0605e33ca68a64565f83cdaa367f`. Existing Identity code provides `CustomerPasswordVerificationPort`, `CredentialVerificationOutcome`, the Argon2 verification adapter, prospective-password preparation, Login-email input preparation and Session storage/revocation boundaries. Existing tests cover those limited responsibilities and module dependencies. None supplies authoritative credential ownership/currentness. A parameter named `authoritativeVerifierRepresentation` does not itself establish provenance. The Session adapter's confirmed-absence observation is specific to Session revocation and does not supply credential absence evidence.

The demonstrated future consumer is Identity's credential-verification step within the governed Login flow, after separately governed canonical identifier resolution exists. Its need is to determine which credential evidence may be relied upon for that operation before composing it with password matching. This design does not enable Login or establish a new Login lookup strategy. Registration is inspected for constraints, not made an additional implemented consumer.

## 2. Governing authority and source register

Accepted/Approved sources govern independently of this Specification. Labels below are local traceability aids, not new authority.

| Label | Source and governing scope |
| --- | --- |
| G1 | [ICRD](customer-credential-state-design.md), REQ-ICRD-001–012 and §6: credential authority, currentness, observation, uncertainty and deferred concrete design. |
| G2 | [Identity backend](identity-backend.md), BIDN-REQ-010–019, 032–034, 038–043 and 048–050; [Identity domain](../../domains/identity/identity-domain.md), REQ-IDN-004, 012–017 and 039–044: ownership, evidence, reconciliation and safe consumption. |
| G3 | [DEC-0003](../../decisions/DEC-0003-initial-local-customer-credential-mechanism.md), password credential category; [DEC-0006](../../decisions/DEC-0006-customer-password-hashing-verification-strategy.md), representation, verification, supported-profile upgrade and uncertainty boundaries. |
| G4 | [DEC-0007](../../decisions/DEC-0007-initial-customer-password-policy.md), complete establishment policy; [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md), separate current-email gate. |
| G5 | [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md), local Authentication authority; [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), separate association and business authority; [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md) and [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), separate Session authority. |
| G6 | [Login Contract](../../contracts/identity/initial-customer-login-contract.md), §§6–10 and failure/concurrency boundaries; [Login OpenAPI](../../contracts/identity/initial-customer-login.openapi.yaml); [Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), §§9, 12–14 and reconciliation; [Registration OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml): existing consumption and disclosure constraints only. |
| G7 | [AGENTS](../../../.ai/core/AGENTS.md), hierarchy and specification before implementation; [Glossary](../../../.ai/core/GLOSSARY.md), canonical terms; [Product](../../../.ai/core/PRODUCT.md), existing Customer credential and verification policy; [Architecture](../../../.ai/core/ARCHITECTURE.md), ownership and inward dependencies. |
| G8 | [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), default denial and secret protection; [Coding Standards](../../../.ai/core/CODING-STANDARDS.md), invariants and non-speculative boundaries; [Engineering Principles](../../../.ai/core/ENGINEERING-PRINCIPLES.md), truthful outcomes and preserved authority. |
| G9 | [ADR-0017](../../adr/ADR-0017-postgresql-schema-strategy.md) and [ADR-0018](../../adr/ADR-0018-persistence-technology.md); [DATABASE](../../../.ai/backend/DATABASE.md), [POSTGRES](../../../.ai/backend/POSTGRES.md), [JAVA](../../../.ai/backend/JAVA.md) and [SPRING](../../../.ai/backend/SPRING.md): persistence ownership, project-owned Ports and implementation neutrality. |
| G10 | [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), Specification lifecycle and identifiers; [Testing Standards](../../../.ai/core/TESTING-STANDARDS.md), deterministic invariant/failure verification; [DECISIONS](../../../.ai/core/DECISIONS.md), material decision escalation; [DEC-0001](../../decisions/DEC-0001-backend-build-tool-dependency-management.md), separate dependency admission. |
| G11 | [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md) and [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md): Proposed/non-authoritative, unresolved/deferred dispositions and exhausted evaluation boundaries; neither is an accepted strategy. |

## 3. Terminology and semantic distinctions

Canonical Identity, Principal, Session, Authentication and Authorization retain G7 meanings. The following lowercase descriptions distinguish information responsibilities; they are not new glossary terms, stored statuses, class names or required fields.

| Description | Meaning and limit |
| --- | --- |
| Credential subject | The stable Identity whose governed credential context is being considered. Neither mutable Login input nor Customer/Account identity substitutes for it. |
| Requested applicability | The credential context and governed operation for which the consumer needs evidence. This is not a global active state, eligibility policy or Authorization grant. |
| Evidence producer | The Identity-owned boundary responsible for confirming source authority, subject and applicability from authoritative Identity state. Module placement or object construction alone does not fulfill that responsibility. |
| Evidence consumer | The Identity application responsibility that may rely on the producer's confirmed result only within its stated applicability and governing operation. |
| Credential evidence | A bounded observation of credential ownership/currentness, absence or inability to confirm. It is distinct from the secret/verifier material and from a password-match result; no serialization or object layout is prescribed. |

## 4. Normative design rules

### REQ-ICED-001 — Subject attribution

Evidence relied upon for credential ownership/currentness MUST refer unambiguously to the intended stable Identity and the applicable credential context. A supplied email, Customer/Account reference, verifier or password match MUST NOT establish that attribution. The subject remains stable independently of mutable credentials and Login identifiers; the design assigns no public/database identifier representation, key, credential count or association cardinality. Sources: G1 REQ-ICRD-001/002/004; G2 BIDN-REQ-010/011; G5 ADR-0022; G7.

### REQ-ICED-002 — Bounded applicability

A consumer MUST limit reliance to the credential context and governed operation for which the evidence is confirmed. Applicability MUST NOT imply universal Identity eligibility, Customer/Account truth, Authentication, Principal, Session, association or Authorization. Evidence for a different subject or context cannot be reused as confirmation; this obligation prescribes no operation-code enum, global active flag, expiry interval or lifecycle state. Sources: G1 REQ-ICRD-004/009/010; G2 BIDN-REQ-012/042; G4; G5; G6.

### REQ-ICED-003 — Production authority

Only an Identity-owned producer that establishes authoritative provenance, subject attribution and applicable currentness from Identity-owned authority may supply a result relied upon as confirmed credential evidence. A request asks for confirmation; it does not confer confirmation. Caller assertions, verifier contents, successful password matching, arbitrary constructors and test fixtures MUST NOT self-authorize evidence. Later implementation review MUST identify the actual production path that fulfills these obligations; naming a type authoritative or locating it inside Identity is insufficient. This defines a trust responsibility, not a new service, provider, cryptographic attestation or persistence technology. Sources: G1 REQ-ICRD-002/004/007; G2 BIDN-REQ-012/032/040/042; G5 ADR-0021; G6 Login §8; G8.

### REQ-ICED-004 — Evidence outcome meaning

The producer and consumer MUST preserve the following distinctions where they affect safe handling. No outcome may assert more than the authoritative observation supports. These are semantic results, not an enum or stored lifecycle state machine. Sources: G1 REQ-ICRD-004–007; G2 BIDN-REQ-038–040; G3; G6.

| Semantic result | Required evidence basis | Permitted inference and limit |
| --- | --- | --- |
| Confirmed applicable/current | Authoritative subject attribution and applicability/currentness established for the requested context, consistent with confirmed superseding/withdrawing authority | The consumer may rely on that credential context for the governed operation. It proves neither password match nor any later Authentication gate and is not an indefinite currentness guarantee. |
| Confirmed absence | The authoritative producer can establish absence for the requested subject and credential context | Only that bounded absence is confirmed. It proves neither global Identity/Customer absence nor permission to establish a credential. An empty result from an unconfirmed or incomplete read is insufficient. |
| Conflict/contradiction | Evidence establishes incompatible claims whose required authoritative applicability cannot be resolved | No claim may be arbitrarily selected as current. Conflict is not absence, invalid password proof or permission to overwrite either claim. |
| Uncertain/unavailable confirmation | Required provenance, effects or applicability cannot be established, including unavailable confirmation | No currentness or absence is confirmed. Unavailability and other uncertainty remain distinguishable where response or recovery handling differs; no timeout, retry policy or external response is selected. |
| Stale/non-current | Authoritative evidence establishes that prior evidence is superseded, withdrawn or otherwise inapplicable under existing authority | Prior evidence cannot support current reliance. This does not establish absence, identify an acceptable replacement, or perform any withdrawal/replacement operation. If non-currentness itself cannot be confirmed, uncertainty remains explicit. |

### REQ-ICED-005 — Inward consumption responsibility

The future Identity Login credential-verification step MUST request only authoritative credential context for an already authoritatively resolved stable Identity and the applicable governed verification operation. Conceptually, an inward project-owned boundary supplies a subject/applicability-bound outcome under REQ-ICED-004; its responsibility is confirmation, not interpreting submitted email, selecting a Login key, establishing credentials or granting Authentication. A consumer MUST NOT supply a flag that decides the returned authority. Sources: G1 REQ-ICRD-004/009/011; G2 BIDN-REQ-032; G5 ADR-0021; G6 Login §§6–8; G7; G9.

This conceptual request/result relationship describes a future Port responsibility without choosing a Java interface, method signature, repository method, identifier type or result layout. Its producer and consumer remain inside Identity. A separately reviewed implementation must demonstrate the concrete path; an unimplemented Port or mocked success is not a completed capability.

### REQ-ICED-006 — Password verification composition

A consumer MUST keep authoritative credential context and `CustomerPasswordVerificationPort` results distinct. When later supplied to the bounded verifier, the verifier representation MUST belong to the same authoritatively confirmed subject/context being relied upon; a match against unrelated supplied material cannot satisfy that context. `MATCH` proves only the bounded password match, while confirmed credential evidence alone proves no match. Neither result independently establishes ownership/currentness beyond its own evidence, Customer eligibility, accepted Authentication, Principal, Session, association or Authorization. Combining them still leaves all other governed gates unsatisfied until independently confirmed. Sources: G1 REQ-ICRD-002/004/009; G3 DEC-0006 verification; G4 DEC-0008; G5; G6 Login §§6–10.

This composition does not reapply establishment length/blocklist rules at Login or change the verifier's existing input behavior. DEC-0006's explicitly supported older-profile upgrade-write allowance remains distinct from uncertain ownership/currentness; no older profile is admitted and no upgrade writer is selected.

### REQ-ICED-007 — Fail-safe consumption

Absent sufficient applicable confirmation, consumers MUST deny reliance on credential currentness and MUST NOT turn unavailable, uncertain, conflicting or stale evidence into confirmed absence, confirmed currentness or successful Authentication. They MUST NOT infer permission to retry as a fresh establishment or overwrite confirmed authority. Known absence likewise grants no establishment authority. Existing Contract disclosure and failure distinctions remain intact; no internal result selects a new HTTP response or reveals protected existence. Sources: G1 REQ-ICRD-003–007; G2 BIDN-REQ-038–040; G4 DEC-0007; G6; G8.

### REQ-ICED-008 — Secret containment

Raw/normalized passwords and verifier material MUST remain within their governed Identity handling boundaries and MUST NOT become ordinary public or cross-domain credential evidence. If a later implementation supplies verifier material for matching, it MUST restrict that material to the bounded Identity verification path, preserve its binding to confirmed context and exclude it from ordinary results, diagnostics, logs, metrics, traces, events and support evidence. Passwords remain transient and purpose-limited. This defines no storage field, external Contract representation or mandatory evidence-object payload. Sources: G1 REQ-ICRD-008; G2 BIDN-REQ-016/041; G3; G4; G6; G8.

### REQ-ICED-009 — Stale evidence and reconciliation

Consumers and producers MUST NOT prefer stale, delayed, duplicated or contradictory evidence over newer confirmed applicable authority, restore superseded/withdrawn authority, or claim a reconciled result without authoritative confirmation. Required applicability must be re-established where intervening evidence makes prior reliance unsafe; retained history is not permission to use unsafe state. Observation alone authorizes no mutation. This specifies no timestamp, version column, locking primitive, transaction implementation, constraint or retry count. Sources: G1 REQ-ICRD-005–007; G2 BIDN-REQ-033/034/039/040; G3 upgrade/rehash; G6 concurrency/reconciliation; G8.

### REQ-ICED-010 — Unresolved prerequisites remain unsatisfied

Evidence objects, ports, fixtures and successful tests MUST NOT bypass separately governed prerequisites. Future Login consumption remains conditional on canonical identifier resolution; DEC-0009 remains unresolved, and neither `java.net.IDN` nor another approximation may substitute for the missing strict strategy. Complete credential establishment remains blocked where DEC-0010's unresolved check is required by DEC-0007; preparation or hashing is not complete policy acceptance. Current-email verification under DEC-0008 remains a separate gate. Neither exhausted evaluation may resume through this design. Sources: G1 REQ-ICRD-003/009/010/012; G4; G6; G11.

### REQ-ICED-011 — Review and implementation boundary

Later review MUST trace evidence production and consumption to existing authority, including negative and uncertain outcomes, without treating this Specification or test doubles as production authority. Any material Product, Security or Architecture choice encountered MUST stop at its governing review boundary. This design MUST NOT select concrete persistence interfaces, schema or infrastructure, public Contract behavior, dependencies/providers or new operation authority. Sources: G1 REQ-ICRD-011/012 and §6; G7–G10.

## 5. Design-level acceptance criteria

These scenarios assess a later implementation proposal; they are not claims that tests have been written or executed. Each inherits the sources of its requirement. A test double may exercise consumer behavior under an explicitly assumed outcome, but it cannot prove that a real producer establishes authoritative evidence. Producer evidence requires separate validation appropriate to its eventual implementation.

| Criterion | Trace | Deterministic review expectation |
| --- | --- | --- |
| 1 | REQ-ICED-001 | Substitute another Identity context, Customer reference or submitted email; none establishes the requested credential subject. A changed mutable Login identifier is not treated as a new stable Identity. |
| 2 | REQ-ICED-002 | Supply evidence for a different governed context; the consumer cannot reuse it as confirmation or infer eligibility, association or Authorization. |
| 3 | REQ-ICED-003 | Trace every production path yielding confirmed evidence. Caller flags, constructed objects, verifier contents and fixtures cannot bypass the required producer confirmation; a mocked result supplies no proof of that path. |
| 4 | REQ-ICED-004 | Review each matrix result: confirmed scoped absence differs from unavailable/empty confirmation; conflict yields no arbitrary winner; proven stale evidence differs from inability to confirm. No result implies a stored status. |
| 5 | REQ-ICED-005 | The proposed consumer requests only the resolved subject's bounded context. Prepared email cannot reach this responsibility as authoritative resolution, and no request parameter declares its own authority. |
| 6 | REQ-ICED-006 | A match without confirmed applicable context cannot establish currentness; confirmed context without a match cannot prove password verification. A match against another context is not accepted for this one; both results together still cannot bypass the current-email and other Authentication gates. |
| 7 | REQ-ICED-007 | Unavailable, uncertain, conflicting and stale outcomes grant no currentness reliance, new-establishment permission or overwrite. Confirmed absence remains distinct and also grants no establishment permission. Existing external disclosure constraints remain unchanged. |
| 8 | REQ-ICED-008 | Review ordinary result shapes, diagnostics and cross-domain flows: no raw/normalized password or verifier escapes. Any internal verifier handoff is confined to the matching path and bound to the confirmed context. |
| 9 | REQ-ICED-009 | A delayed prior observation after confirmed supersession/withdrawal cannot restore reliance; a duplicate cannot restore authority; unresolved contradictory evidence remains unresolved rather than producing a winner or mutation. |
| 10 | REQ-ICED-010 | A prepared email or mocked canonical result cannot demonstrate the missing production resolution strategy. Prepared/hashed passwords or mocked blocklist success cannot demonstrate complete policy acceptance. Tests cannot renew either evaluation or substitute for the separate email-verification gate. |
| 11 | REQ-ICED-011 | Review finds no invented schema, repository signature, HTTP behavior, provider, dependency or operation. Any unresolved material choice is recorded and implementation stops at it; tests are not presented as approval. |

## 6. Open questions and escalation boundaries

- **Concrete evidence model and Port form:** this Specification resolves the semantic producer/consumer responsibilities and permitted inferences, not Java types, constructor visibility, identifiers, result layout or signatures. Later review must demonstrate how actual production paths enforce provenance and binding, and why the chosen boundary serves the identified consumer. A constructor restriction alone is not evidence authority.
- **Authoritative confirmation mechanics:** the actual source access, completeness basis for absence, applicability across concurrent work and reconciliation mechanics need implementation-specific design and validation. No age threshold, locking rule or persistence shape is inferred. If an adequate guarantee requires a new material policy or Architecture choice, stop and obtain that governance rather than weaken the semantic obligation.
- **Canonical identifier and password-policy dependencies:** DEC-0009 and DEC-0010 remain unresolved/deferred, with no new evidence or evaluation authority. This design neither supplies a canonical key/lookup strategy nor a blocklist acceptance path.
- **Current-email and eligibility evidence:** DEC-0008 governs its gate but this design selects no verification mechanism or representation. Concrete Identity eligibility, enablement/disablement/reactivation and related Product/Security policy remain separately governed; credential currentness is no substitute.
- **Lifecycle operations and association:** recovery, password reset/replacement/change, credential withdrawal/invalidation and Customer/Account closure/reactivation triggers and workflows are not defined here. Confirmed effects may constrain observations only under existing authority. Association cardinality remains with applicable Product/Architecture governance under ADR-0022.

No new material decision is asserted by this Specification. These boundaries must not be answered through an enum, persistence default, mock or convenient API. Approval of this internal design is complete and remains distinct from later implementation-eligibility review; no production slice is declared eligible by this document.

## 7. Explicit non-decisions and review

This Specification changes no upstream authority and selects no IDNA implementation, canonical comparison key, Login lookup from submitted email, blocklist source/provider/dataset/library/service, dependency or external Identity Provider behavior. DEC-0001 admission requirements remain unchanged. Neither DEC-0009 nor DEC-0010 is resolved or reopened.

It defines no database schema, table, column, index, constraint, foreign key, ORM entity, repository implementation or concrete interface/method, SQL, lock, transaction implementation, cache, provider or infrastructure topology. ADR-0017/0018 remain unchanged. It defines no HTTP route, method, status, DTO, header, cookie or OpenAPI schema and changes no externally observable Login/Registration behavior.

It authorizes no password reset/replacement/change, recovery, credential withdrawal/invalidation, Identity enablement/disablement/reactivation or Account closure/reactivation. It creates no association cardinality and makes no credential observation equivalent to Authentication, Principal, Session or Authorization. It grants no implementation authority, even where individual semantic obligations are traceable to Accepted/Approved sources.

Controlled approval follows clean approval-readiness review of Identity ownership, Security provenance/containment, Architecture boundaries and Engineering testability, including applicable Customer business/association boundaries. Later implementation review and separately governed material decisions remain required where applicable. Required material decisions must use their existing governance. Creation of this internal Specification requires no DECISIONS index entry or amendment to ICRD; this Specification is subordinate to both existing governance and the Approved ICRD obligations.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-07 | Approved | Controlled approval after clean approval-readiness review; preserved existing Accepted/Approved upstream authority and unresolved/deferred DEC-0009 and DEC-0010; renewed neither exhausted evaluation; selected no IDNA or blocklist strategy, dependency or provider; defined no concrete persistence interface/schema; changed no public Contract; introduced no lifecycle-operation authority. Approval does not itself authorize production implementation. |
| 0.1.0 | 2026-10-07 | Draft | Defined bounded credential subject, applicability, production authority, observation outcomes and inward consumption semantics for later review; separated password matching, currentness and downstream gates; preserved deferred strategies, persistence/Contract/dependency neutrality and separately governed operations without implementation or renewed evaluation authority. |
