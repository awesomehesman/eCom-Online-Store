---
title: Identity–Customer Association Evidence Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-12
authoritative: false
---

# Identity–Customer Association Evidence Design

## 1. Purpose and authority boundary

This Approved internal Specification describes conditional evidence obligations for the Identity-owned, authoritative Identity–Customer association selected by Accepted ADR-0022. Scope code **ICAE** identifies its requirements. It addresses how a **separately authorized and accepted** association fact could be attributed, represented, observed and relied upon safely. It neither accepts such a fact nor authorizes an association producer, operation or production implementation.

At the inspected repository baseline `1bb3b48d8f4658671b025a0d170d4a8a6424db2b`, the governing sources establish association ownership and required safety semantics; they do not demonstrate a lawful production association issuer, an accepted association fact or an implemented authoritative observation mechanism. An identifier, registration intent, returned success shape, stored row or fixture cannot fill that gap. Positive association reliance MUST remain withheld until a later review establishes the applicable fact and provenance under separately governed authority.

## 2. Scope and explicit exclusions

In scope are the distinction between accepted association fact and its representation, accountable provenance, stable Identity and Customer-reference binding, current observation, supersession safety, duplicate/conflict/uncertainty handling, bounded Customer consumption, separation from Registration and Authentication, and security, privacy and audit obligations.

Out of scope are initial association establishment or an issuer; association change, transfer, withdrawal, recovery or reassignment operations; global Identity–Customer or Customer–Account cardinality; Customer or Account creation and lifecycle; concrete cross-Module Contracts; physical persistence, transaction, retry or event mechanisms; provider, dependency and public API choices. This document does not convert an Approved Registration Contract into permission to complete Registration.

## 3. Governing authority and traceability

| Label | Governing source and applicable boundary |
| --- | --- |
| G1 | [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), especially **Decision**, **Association Authority and Cardinality**, **Registration Coordination**, **Cross-Module Contract Direction**, **Duplicate and Concurrent Registration**, **Partial Failure, Uncertainty, and Reconciliation**, **Security, Privacy, and Authority Boundary**, and **Explicit Non-Decisions**: Identity owns the security-side association; Customer owns Customer and Account business truth; contextual resolution must be unambiguous and fail closed. |
| G2 | [Architecture](../../../.ai/core/ARCHITECTURE.md), §30.2.1, and [AGENTS](../../../.ai/core/AGENTS.md), §§5, 27.1 and 31.4: ownership, separate transactions, authority hierarchy and implementation gates. |
| G3 | [Initial Customer Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), especially §§4, 6, 8–11, 14–20 and 23–29: separate owner outcomes, association confirmation, incomplete Registration, duplicates, uncertainty, reconciliation and safe disclosure. Its paired [OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml) alone governs executable wire behavior. |
| G4 | [Identity backend](identity-backend.md) and [Customer backend](../customer/customer-backend.md), especially BIDN-REQ-010/011 and BCUS-REQ-012–019: stable subjects, actor separation, Customer authority and contextual Authorization. |
| G5 | [Product](../../../.ai/core/PRODUCT.md), [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), [Glossary](../../../.ai/core/GLOSSARY.md), [Decision governance](../../../.ai/core/DECISIONS.md) and [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md): Product limits, default denial, privacy, canonical terminology, material-choice escalation and Specification lifecycle. |
| G6 | [API Standards](../../../.ai/backend/API.md) and [Database Standards](../../../.ai/backend/DATABASE.md): bounded owner Contracts and owner-local data authority. This design selects no executable interface or storage model. |
| G7 | Approved [CLSP](current-login-email-source-population-design.md) and [IEVE](current-login-email-verification-evidence-design.md): login-email population and verification evidence are distinct from association evidence and supply no association issuer or proof. |
| G8 | [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md), [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md) and [DEC-0011](../../decisions/DEC-0011-initial-customer-email-verification-mechanism.md) remain Proposed/deferred with exhausted evaluations. They are open prerequisites where applicable, not Accepted authority for this design. |

REQ-ICAE-001–014 specialize G1–G6. G7 preserves neighboring evidence boundaries; G8 records unresolved dependencies. Neither G7 nor G8 establishes an association fact or producer.

## 4. Association fact, evidence and currentness distinctions

An **accepted association fact** is an Identity-owned determination that a stable Identity is associated with an applicable opaque Customer reference, accepted under separately governed authority. This Specification defines the evidence obligations **if** such a fact is accepted; it does not decide how, when or by whom acceptance occurs. A Customer-owned existence or lifecycle fact may be needed for acceptance or later reliance, but Customer cannot establish Identity's association truth. Conversely, Identity's association does not establish Customer existence, Account relationship, current business eligibility or contextual Authorization.

**Representation** or publication makes an accepted fact available; a row, message, projection or success response is not its acceptance proof. **Observation** is a current, source-backed assessment of whether the accepted fact applies to a particular reliance context. Pending, rejected, conflicting, unavailable and uncertain outcomes remain distinguishable inside the trusted boundary when safe handling differs; none may be converted to positive confirmation by a consumer. A confirmed, separately authorized superseding or withdrawing fact must constrain later currentness, but this design grants no authority to perform that operation. Contextual unambiguity establishes no global one-to-one cardinality.

## 5. Conditional design requirements

### REQ-ICAE-001 — Identity authority

Identity MUST remain authoritative for the security-side Identity–Customer association. A consumer MUST NOT establish or reinterpret that authority from Customer-local state, supplied identifiers, email, credential evidence, a registration intent, a stored row or a returned success shape. Sources: G1 Decision; G2 §30.2.1; G3 §§4, 11.

### REQ-ICAE-002 — Prior accepted fact

Positive association evidence MUST be representable or relied upon only after a separately authorized Identity-owned association outcome is accepted. Dispatch, pending coordination, fixture data, persistence or publication MUST NOT substitute for that acceptance. Sources: G1 Registration Coordination and Security Boundary; G3 §§8, 11, 14–15.

### REQ-ICAE-003 — Accountable provenance

Evidence of an accepted association MUST be attributable to the accountable Identity-side acceptance outcome and the applicable governed intent or context. If that provenance cannot be confirmed, a producer or consumer MUST withhold positive association confirmation rather than invent an issuer or infer acceptance. Sources: G1 Registration Coordination and Partial Failure; G3 §§6, 19–20.

### REQ-ICAE-004 — Stable binding

Association evidence MUST bind the applicable stable Identity to the applicable opaque, Customer-owned Customer reference without deriving either subject from mutable login email, credential, Principal, Session or Account content. A bare Identity or Customer reference MUST NOT prove the relationship. Sources: G1 Association Authority and Cardinality; G2 §30.2.1; G4.

### REQ-ICAE-005 — Customer and Account non-authority

Identity association evidence MUST NOT assert Customer existence, Customer lifecycle state, Customer-to-Account relationship, Account state or business Authorization. Customer MUST independently validate its own current Customer and Account facts and contextual access under its authority. Sources: G1 Decision and Cross-Module Contract Direction; G3 §§4, 10, 23; G4.

### REQ-ICAE-006 — Current observation and supersession

Reliance MUST use association evidence applicable to the current Identity, Customer reference and governed context. Confirmed, separately authorized newer superseding or withdrawing evidence MUST NOT be ignored; stale prior evidence MUST NOT restore or overwrite newer confirmed authority. The trigger, requester, workflow and operation remain separately governed. Sources: G1 Association Authority and Cardinality, Closure and Lifecycle Boundaries; G2 §30.2.1; G3 §§17–20.

### REQ-ICAE-007 — Same-outcome duplicates

Repeated publication or observation of the same confirmed association outcome for the same governed intent MUST NOT multiply associations, create a new acceptance, or change its authoritative meaning. Incompatible repeated intent MUST NOT be silently merged or selected. Sources: G1 Duplicate and Concurrent Registration; G3 §§6, 17–18.

### REQ-ICAE-008 — Conflicting claims

Conflicting or ambiguous association claims MUST NOT be resolved by timing, record order, arbitrary selection, transfer or inferred global cardinality. Contextual Customer resolution MUST fail closed until owning authority resolves the conflict; protected existence MUST remain concealed from unauthorized callers. Sources: G1 Association Authority and Cardinality and Security Boundary; G2 §30.2.1; G3 §§11, 18, 23–24.

### REQ-ICAE-009 — Failure and uncertainty

Pending, rejected, unavailable and uncertain association outcomes MUST remain distinguishable inside the trusted boundary where their safe handling differs. Failure or uncertainty MUST withhold positive association reliance and completed Registration or protected Customer context; uncertainty MUST NOT be reported internally as confirmed rejection or success. Sources: G1 Partial Failure, Uncertainty, and Reconciliation; G3 §§15, 19–20, 24.

### REQ-ICAE-010 — Bounded Customer consumption

Customer MAY consume only bounded, current Identity-owned association evidence through a governed owner Contract. It MUST NOT inspect Identity internals, accept client-claimed relationships, or convert association evidence into Customer or Account truth without its own confirmation. This requirement defines no executable Contract shape. Sources: G1 Cross-Module Contract Direction; G2 §30.2.1; G3 §§4, 10–11, 23; G6.

### REQ-ICAE-011 — Registration completion separation

An accepted association is one required Registration outcome, not completed Registration. Coordination MUST NOT claim completion until all separately governed Identity, Customer, Account, verification and association outcomes and applicable Product/Security conditions are confirmed for the governed intent, with no required outcome uncertain. Sources: G1 Registration Coordination; G3 §§8, 14–15, 19–20; G7.

### REQ-ICAE-012 — Authentication and Session separation

Association evidence alone MUST NOT establish Authentication, a Principal, a Session, Customer or Account access, or contextual Authorization. Current Identity Authentication and Customer-owned access decisions remain separate gates; absence, conflict or uncertainty in required association evidence MUST deny protected Customer context. Sources: G1 Decision, Cross-Module Contract Direction and Security Boundary; G2 §30.2.1; G3 §§23–24; G5.

### REQ-ICAE-013 — Security, privacy and auditability

Production evidence handling MUST limit association disclosure to authorized purposes, resist enumeration and cross-Customer access, and preserve attributable, privacy-bounded audit evidence for accepted outcomes and material discrepancies. Ordinary logs, telemetry, support views and client claims MUST NOT become association authority or expose protected existence or Sensitive Data. Sources: G1 Security, Privacy, and Authority Boundary; G3 §§21, 26–28; G5 Security Standards §§7–10.

### REQ-ICAE-014 — Production-readiness gate

A later production slice MUST demonstrate the lawful acceptance issuer and provenance, authoritative source/observation path, currentness and conflict behavior, safe bounded consumer, negative and concurrent cases, and all separately governed Product, Security, Architecture and Contract prerequisites before positive reliance. This Approved Specification alone MUST NOT establish implementation eligibility. Sources: G1 Required Governance Reviews and Explicit Non-Decisions; G2 §27.1; G3 §§20, 29–30; G5.

## 6. Design-level acceptance criteria

| ID | Falsifiable design-level check |
| --- | --- |
| ICAE-AC-001 | Given Customer-local data, supplied identifiers, email, intent, a row or a success response without Identity acceptance, the design classifies association as unconfirmed and leaves Identity authoritative. |
| ICAE-AC-002 | Given only dispatch, pending coordination, publication or fixture data, the design withholds positive association evidence; it permits positive representation only after a separately authorized accepted Identity outcome. |
| ICAE-AC-003 | Given an outcome whose Identity-side acceptance or governed intent/context cannot be attributed, the design withholds positive confirmation and records provenance as an unresolved production prerequisite. |
| ICAE-AC-004 | Given a mutable email, credential, Principal, Session or Account change, the design retains the stable Identity–opaque Customer-reference binding where separately accepted; either bare reference alone is insufficient proof. |
| ICAE-AC-005 | Given confirmed association evidence but missing current Customer or Account authority, the design does not infer Customer existence, Account relationship or business access. |
| ICAE-AC-006 | Given confirmed newer superseding or withdrawing evidence, an older association observation cannot restore prior applicability; the design defines no change operation. |
| ICAE-AC-007 | Given two deliveries of the same accepted outcome and an incompatible reuse of intent, the first does not multiply associations and the second is not silently merged or selected. |
| ICAE-AC-008 | Given conflicting or ambiguous candidate associations, the design denies contextual resolution without selecting by order or assuming global one-to-one cardinality. |
| ICAE-AC-009 | Given pending, rejected, unavailable or uncertain evidence, the design distinguishes them internally as needed and grants no positive association or completed/protected context; uncertainty is neither success nor confirmed rejection. |
| ICAE-AC-010 | Given a Customer consumer, the design supplies only bounded current Identity evidence through a governed boundary and requires independent Customer/Account confirmation. |
| ICAE-AC-011 | Given only an accepted association while any other required Registration gate is absent or uncertain, completion remains unconfirmed. |
| ICAE-AC-012 | Given only association evidence, Authentication, Principal, Session, Account access and contextual Authorization remain unestablished; missing required association evidence denies protected Customer context. |
| ICAE-AC-013 | Given unauthorized caller, log or support access, the design discloses neither protected association existence nor Sensitive Data and retains attributable, bounded audit responsibility. |
| ICAE-AC-014 | A production-readiness review cannot pass on this Approved Specification alone: it must identify a lawful issuer, source and observation path, safe consumer, negative/concurrent evidence and separately approved material prerequisites. |

## 7. Requirement-to-acceptance traceability

| Requirement | Acceptance criterion | Governing sources |
| --- | --- | --- |
| REQ-ICAE-001 | ICAE-AC-001 | G1, G2, G3 |
| REQ-ICAE-002 | ICAE-AC-002 | G1, G3 |
| REQ-ICAE-003 | ICAE-AC-003 | G1, G3 |
| REQ-ICAE-004 | ICAE-AC-004 | G1, G2, G4 |
| REQ-ICAE-005 | ICAE-AC-005 | G1, G3, G4 |
| REQ-ICAE-006 | ICAE-AC-006 | G1, G2, G3 |
| REQ-ICAE-007 | ICAE-AC-007 | G1, G3 |
| REQ-ICAE-008 | ICAE-AC-008 | G1, G2, G3 |
| REQ-ICAE-009 | ICAE-AC-009 | G1, G3 |
| REQ-ICAE-010 | ICAE-AC-010 | G1, G2, G3, G6 |
| REQ-ICAE-011 | ICAE-AC-011 | G1, G3, G7 |
| REQ-ICAE-012 | ICAE-AC-012 | G1, G2, G3, G5 |
| REQ-ICAE-013 | ICAE-AC-013 | G1, G3, G5 |
| REQ-ICAE-014 | ICAE-AC-014 | G1, G2, G3, G5 |

## 8. Open prerequisites and implementation-readiness limits

- **Association acceptance and issuer:** No production association issuer, accepted fact or authoritative observation mechanism is demonstrated. Later review must establish a separately authorized acceptance outcome, accountable producer, controlled source and provenance without inferring them from this design, a reference, fixture or stored representation.
- **Cardinality and operations:** Global Identity–Customer and Customer–Account cardinality, shared or delegated relationships, and any establishment trigger, change, withdrawal, transfer or recovery workflow remain for applicable Product and Architecture governance. Contextual unambiguity does not answer those questions.
- **Customer coordination:** Customer-owned Customer/Account acceptance and contextual Authorization must be demonstrated separately; a future governed Contract may carry only bounded owner evidence. Actual retry, reconciliation and physical consistency mechanics remain unselected.
- **Registration and Login:** DEC-0009, DEC-0010 and DEC-0011 remain unresolved/deferred with exhausted evaluation authority. CLSP provides no login-email population issuer, and IEVE provides no accepted email-control producer. Neither this design nor association evidence removes those gates.
- **Production review:** A later slice must show current source-backed observation, duplicate/conflict/concurrent/failure behavior, privacy and audit controls, and any necessary Product, Security, Architecture, Contract, dependency or persistence admission. If a specific obligation cannot be traced to existing authority, it requires separate governance rather than an assumption here.

## 9. Explicit non-authorizations

This Approved Specification authorizes no association acceptance, issuer, source write, migration, production observation or protected-context reliance. It selects no global cardinality, association-changing operation, Customer/Account lifecycle policy, Registration completion, Authentication, Principal, Session, Authorization, email-verification mechanism, password-policy completion or login-email canonicalization strategy. It defines no endpoint, method, DTO, response status, event, provider, dependency, schema, table, repository, transaction, lock, queue, retry count, timeout, TTL or SLO. It changes no Product, Architecture, Security Standard, Decision, ADR, Approved Specification, Contract or OpenAPI authority and cannot reopen DEC-0009, DEC-0010 or DEC-0011 evaluations.

## 10. Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-12 | Approved | Approved the conditional Identity-owned association-evidence design following a zero-finding approval-readiness audit. Preserved unresolved issuer, accepted-fact, observation, cardinality, association-operation and implementation prerequisites without expanding authority. |
| 0.1.0 | 2026-10-12 | Draft | Defined conditional Identity-owned association-evidence attribution, stable binding, current observation, duplicate/conflict/uncertainty safety and bounded consumption under Accepted ADR-0022; preserved unresolved issuer, cardinality, operation and implementation prerequisites. |
