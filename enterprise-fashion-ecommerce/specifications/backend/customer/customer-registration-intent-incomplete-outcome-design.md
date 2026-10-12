---
title: Customer Registration Intent and Incomplete Outcome Design
version: 0.1.0
status: Draft
owner: Customer
last_updated: 2026-10-12
authoritative: false
---

# Customer Registration Intent and Incomplete Outcome Design

## 1. Purpose and authority boundary

This Draft internal Specification conditionally specializes Customer-side assessment of a governed initial Customer registration intent, independently accepted owner outcomes, incomplete Registration and reconciliation. Scope code **CRIO** identifies its requirements. It is subordinate to the Approved [Initial Customer Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), which governs the registration workflow; its paired [OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml) governs executable HTTP wire behavior.

Customer Application owns registration coordination and Customer owns authoritative Customer and Account business truth, including the Customer-to-Account relationship. Identity retains login-identifier, credential, current-email verification, Authentication, Principal, Session and security-side Identity-to-Customer association authority. This design neither accepts an owner outcome nor establishes a production registration-intent issuer, completed Registration or implementation eligibility.

## 2. Scope and exclusions

In scope are Customer-side recognition of a **separately governed** intent, correlation of independently confirmed owner outcomes, preservation of Customer-local accepted history, incomplete-outcome assessment, duplicate/conflict/uncertainty safety, bounded reconciliation and safe disclosure. These are conditional design and review obligations, not a new workflow or state machine.

Out of scope are a new registration trigger or intent issuer; Identity, Customer or Account establishment operations; the ordering or executable shape of the Approved workflow; any association or verification producer; externally observable behavior beyond the paired Contract/OpenAPI; implementation-specific persistence, transactions, locks, queues, retry policy, events, provider and dependency choices. No global Identity–Customer–Account cardinality or association-changing operation is selected.

## 3. Governing authority and traceability

| Label | Source and applicable boundary |
| --- | --- |
| G1 | [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), especially **Decision**, **Registration Coordination**, **Transaction and Atomicity Boundary**, **Duplicate and Concurrent Registration**, and **Partial Failure, Uncertainty, and Reconciliation**: Customer coordinates without acquiring Identity authority or assuming cross-Module atomicity. |
| G2 | Approved [Initial Customer Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), especially §§4, 6, 8–11, 14–20 and 23–29: authoritative registration workflow, intent, owner outcomes, incomplete state, retries, conflict, uncertainty, reconciliation and disclosure. The paired [OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml) is the executable wire authority; CRIO defines neither artifact's behavior. |
| G3 | Approved [Customer backend](customer-backend.md), especially BCUS-REQ-009/010, 012–019, 039–043 and 046–049: Customer use-case coordination, Customer/Account truth, duplicate safety, failure, reconciliation, privacy and audit. Approved [Identity backend](../identity/identity-backend.md), especially BIDN-REQ-004–008 and 010/011: distinct Identity authority and bounded evidence. |
| G4 | [Architecture](../../../.ai/core/ARCHITECTURE.md), §30.2.1; [AGENTS](../../../.ai/core/AGENTS.md), §§5, 27.1 and 31.4; [Product](../../../.ai/core/PRODUCT.md), §§12.3 and 24: owner-local authority, readiness gates and unresolved Product choices. |
| G5 | [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), §§7, 10–12; [Glossary](../../../.ai/core/GLOSSARY.md); [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), §§7–9 and 19–21; [Decision governance](../../../.ai/core/DECISIONS.md), §§5–6 and 49: default denial, terminology, lifecycle, testability and material-choice escalation. |
| G6 | [API Standards](../../../.ai/backend/API.md) and [Database Standards](../../../.ai/backend/DATABASE.md): Contract authority, safe identifiers, ownership and local consistency. Neither supplies an issuer or selects a mechanism here. |
| G7 | Approved [CLSP](../identity/current-login-email-source-population-design.md), [IEVE](../identity/current-login-email-verification-evidence-design.md) and [ICAE](../identity/identity-customer-association-evidence-design.md): conditional Identity evidence boundaries; none is a production issuer or permits Customer to manufacture Identity truth. |
| G8 | [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md), [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md) and [DEC-0011](../../decisions/DEC-0011-initial-customer-email-verification-mechanism.md): Proposed/deferred, non-authoritative strategies with exhausted evaluations. They remain implementation prerequisites where applicable, not new authority for CRIO. |

REQ-CRIO-001–015 specialize G1–G6. G7 and G8 constrain assumptions and downstream readiness; they do not authorize an owner outcome. A conflict with the Contract, ADR-0022 or higher authority requires correction or separate governance, not reinterpretation by this Draft.

## 4. Registration intent and owner-outcome distinctions

A **governed registration intent** is a request to begin or continue the Approved workflow, recognizable as the same attempt across applicable repeated or interrupted coordination. It is not completed Registration or an issuer of Identity, Customer, Account or association facts. This design requires safe recognition **if** such an intent has been separately governed; it defines no trigger, producer, format, retention or global state machine.

An **accepted Customer-local outcome** remains Customer-owned truth. Identity-owned login-email, credential, verification and association outcomes remain Identity truth. A coordinator's record or observation of an owner outcome does not accept it on that owner's behalf. A request, dispatch, timeout, client response, fixture, row or correlated reference is not confirmation. Correlation establishes which governed intent an outcome purports to concern; source-backed acceptance and current applicability must still be established.

**Registration is not complete** whenever at least one required, independently accepted current outcome or applicable Product/Security condition is not confirmed. Pending or incomplete coordination, confirmed rejection, conflict, unavailability and uncertainty remain distinct classifications wherever their governing handling differs; none permits a completed-Registration claim. **Reconciliation** compares the original intent and owner evidence to current authoritative truth; it cannot manufacture acceptance, erase accepted history or convert uncertainty into success or rejection by assumption.

## 5. Conditional design requirements

### REQ-CRIO-001 — Customer coordination boundary

Customer Application MUST assess the Customer-facing registration outcome within Customer authority and MUST NOT establish or reinterpret Identity-owned evidence. It MUST preserve the Approved Contract's workflow and each owner's local transaction boundary. Sources: G1 Decision and Registration Coordination; G2 §§4, 8–10, 25; G3 BCUS-REQ-009/010.

### REQ-CRIO-002 — Governed intent recognition

Where a registration intent is separately governed, Customer-side coordination MUST recognize its stable owner-recognizable identity and correlate observations to the same attempt without treating intent recognition as acceptance, completion or authority to create another owner fact. This design specifies no intent issuer or representation. Sources: G1 Registration Coordination; G2 §6; G3 BCUS-REQ-017/018.

### REQ-CRIO-003 — Independent owner-outcome confirmation

Customer MUST assess each required Identity-owned and Customer-owned outcome from its responsible owner's accepted, applicable evidence for the governed intent. A coordinator observation, dispatched request, supplied identifier or returned success shape MUST NOT substitute for independent owner confirmation. Sources: G1 Registration Coordination and Partial Failure; G2 §§8–11, 14, 19–20; G3 BCUS-REQ-005/017.

### REQ-CRIO-004 — Customer-local accepted history

A failure, interruption or uncertain outcome elsewhere MUST NOT erase, fabricate, transfer or overwrite an accepted Customer or Account outcome. Customer MUST preserve its accepted local truth and newer authoritative evidence while keeping overall Registration incomplete when another gate is unconfirmed. Sources: G1 Partial Failure and Reconciliation; G2 §§19–20; G3 BCUS-REQ-032/043/047.

### REQ-CRIO-005 — Identity evidence non-authority

Customer MAY consume bounded Identity evidence under governed Contracts, but MUST NOT infer Identity ownership, credential acceptance, current-email control, Authentication or Identity-to-Customer association from a Customer record, email, bare reference, fixture or client claim. Customer MUST NOT turn observed Identity evidence into Customer or Account acceptance. Sources: G1 Decision and Cross-Module Contract Direction; G2 §§4, 9–11, 23; G3 BCUS-REQ-005/013/019; G7.

### REQ-CRIO-006 — Incomplete-outcome assessment

Customer MUST withhold completed Registration whenever a required current owner outcome or applicable Product/Security condition is absent, pending, failed, conflicting, unavailable or uncertain. Customer MUST preserve the applicable pending, incomplete, rejected, conflict or uncertain classification under the Approved Contract rather than treating every disqualifying outcome as pending or incomplete. A partial set of accepted outcomes MUST NOT be described as completed Registration. Sources: G1 Registration Coordination and Transaction Boundary; G2 §§14–15, 19, 24; G4 §30.2.1.

### REQ-CRIO-007 — Internal outcome distinctions

Pending, confirmed rejection, conflict, unavailability and uncertainty MUST remain distinguishable inside the trusted boundary where safe continuation, retry, reconciliation, audit or response differs. An unknown result MUST NOT be treated as confirmed rejection or success; an authoritative rejection MUST NOT be reclassified as uncertainty merely to continue. External disclosure remains governed by the Contract and OpenAPI. Sources: G1 Partial Failure; G2 §§15, 19–21, 24; G3 BCUS-REQ-046/047.

### REQ-CRIO-008 — Duplicate-intent and observation safety

Repeated, resumed or reordered assessment of the same governed intent MUST preserve its confirmed owner outcomes without multiplying Customer, Account, Identity, credential, verification or association effects. An incompatible reuse of intent MUST NOT silently inherit an unrelated acceptance or merge ownership. Sources: G1 Duplicate and Concurrent Registration; G2 §§6, 17–18; G3 BCUS-REQ-018/043.

### REQ-CRIO-009 — Conflict and concurrency

Conflicting owner evidence or concurrent coordination MUST NOT select an arbitrary winner, overwrite newer accepted truth or infer global association cardinality. Where current authoritative evidence cannot establish one applicable result for the governed context, completion MUST remain withheld pending owning-authority resolution. Sources: G1 Association Authority and Cardinality and Duplicate and Concurrent Registration; G2 §§16–18, 20; G4 §30.2.1.

### REQ-CRIO-010 — Partial failure and interruption

An unavailable response, timeout, interrupted coordination or ambiguous persistence result MUST remain uncertain until authoritative owner evidence resolves it. Customer MUST NOT perform destructive rollback of another owner's accepted fact or represent an interrupted workflow as complete. Sources: G1 Transaction Boundary and Partial Failure; G2 §§19–20, 25; G3 BCUS-REQ-010/047.

### REQ-CRIO-011 — Bounded reconciliation

Customer-side reconciliation MUST compare the governed intent with current, attributable Identity, Customer, Account and association evidence only within each owner's authority. It MUST preserve accepted history and newer truth, distinguish safe continuation from conflict and accountable repair, and MUST NOT manufacture an acceptance or use a projection as an authoritative source. Sources: G1 Partial Failure and Reconciliation; G2 §20; G3 BCUS-REQ-047; G7.

### REQ-CRIO-012 — Completion gate

Customer MUST represent Registration as complete only when every outcome and applicable condition in the Approved Contract §14 is independently confirmed as current for the same governed intent, with no required evidence failed, conflicting, ambiguous, stale, superseded, unavailable, partial or uncertain. CRIO MUST NOT add, remove or reorder that workflow's gates. Sources: G1 Registration Coordination; G2 §§8, 14; G4 §30.2.1.

### REQ-CRIO-013 — Protected-context denial

An incomplete Registration or a confirmed association outcome alone MUST NOT establish Authentication, Principal, Session, Account access or protected Customer context. Customer MUST deny protected context when required current Identity-owned association or Customer/Account authority is absent, conflicting or uncertain. Sources: G1 Security Boundary; G2 §§15, 23–24; G3 BCUS-REQ-013/014; G7 ICAE.

### REQ-CRIO-014 — Security, privacy and audit

Customer-side assessment MUST limit sensitive evidence to authorized purposes, resist enumeration and cross-Customer disclosure, and preserve attributable, privacy-bounded audit evidence for material accepted, incomplete, conflict and reconciliation outcomes. Ordinary logs, support views, client state and telemetry MUST NOT become registration or owner authority. Sources: G1 Security Boundary; G2 §§21, 26–28; G3 BCUS-REQ-048/049; G5 Security Standards §§7, 10–12.

### REQ-CRIO-015 — Production-readiness gate

A later implementation review MUST demonstrate lawful intent and owner-outcome sources, controlled consumption, currentness, negative/concurrent/failure evidence and separately governed Product, Security, Architecture and Contract prerequisites before production reliance. This Draft and later approval alone MUST NOT establish implementation eligibility. Sources: G2 §§20, 29–30; G3 BCUS-REQ-051/052; G4 AGENTS §27.1; G7–G8.

## 6. Design-level acceptance criteria

| ID | Falsifiable design-level check |
| --- | --- |
| CRIO-AC-001 | Given a coordinator view and a separate Identity outcome, the design leaves Identity acceptance with Identity, Customer/Account acceptance with Customer, and the Contract's workflow unchanged. |
| CRIO-AC-002 | Given repeated observations purporting to concern one governed attempt, the design correlates them to the same intent without treating that correlation as completion or claiming an intent issuer. |
| CRIO-AC-003 | Given a request acknowledgement, client claim or returned success shape without confirmed owner evidence, the design withholds acceptance for that owner outcome. |
| CRIO-AC-004 | Given accepted Customer truth and a failed or unknown Identity step, the Customer fact remains intact while overall Registration stays incomplete. |
| CRIO-AC-005 | Given only email, a Customer record, opaque reference or unconfirmed Identity observation, the design infers none of credential, verification, Authentication or association acceptance and does not transfer Customer authority. |
| CRIO-AC-006 | Given any required gate that is absent, pending, failed, conflicting, unavailable or uncertain, the design withholds completed Registration and preserves the applicable pending, incomplete, rejected, conflict or uncertain classification according to the Approved Contract. |
| CRIO-AC-007 | Given a confirmed rejection and a separate unknown outcome, the design keeps those internal meanings distinct; neither is presented as success and external disclosure remains Contract-governed. |
| CRIO-AC-008 | Given duplicate observations of one governed intent with an independently confirmed owner outcome, the design preserves that already accepted outcome without creating an additional effect; incompatible reuse of the intent cannot inherit or merge unrelated acceptance. |
| CRIO-AC-009 | Given concurrent or conflicting owner evidence, the design withholds completion without selecting by timing, overwriting newer truth or assuming global one-to-one cardinality. |
| CRIO-AC-010 | Given a timeout after one owner has accepted a fact, the design retains that fact under its owner and leaves the other outcome uncertain until source-backed resolution. |
| CRIO-AC-011 | Given a reconciliation discrepancy, the design compares current attributable owner evidence, preserves accepted history and requires accountable owner behavior for repair; it cannot manufacture acceptance. |
| CRIO-AC-012 | Given all but one Contract §14 outcome confirmed, the design withholds completion; with every required current outcome confirmed for the same intent and no disqualifying evidence, it may classify completion only as the Contract permits. |
| CRIO-AC-013 | Given pending Registration or association evidence alone, the design grants no Authentication, Principal, Session or protected Customer/Account context. |
| CRIO-AC-014 | Given unauthorized caller, ordinary telemetry or support view, the design exposes no protected owner existence or Sensitive Data and retains bounded attributable audit responsibility. |
| CRIO-AC-015 | A production-readiness review cannot pass on this Draft alone; it must identify lawful sources, safe consumption, negative/concurrent evidence and separate approvals for unresolved material prerequisites. |

## 7. Requirement-to-acceptance traceability

| Requirement | Acceptance criterion | Governing sources |
| --- | --- | --- |
| REQ-CRIO-001 | CRIO-AC-001 | G1, G2, G3 |
| REQ-CRIO-002 | CRIO-AC-002 | G1, G2, G3 |
| REQ-CRIO-003 | CRIO-AC-003 | G1, G2, G3 |
| REQ-CRIO-004 | CRIO-AC-004 | G1, G2, G3 |
| REQ-CRIO-005 | CRIO-AC-005 | G1, G2, G3, G7 |
| REQ-CRIO-006 | CRIO-AC-006 | G1, G2, G4 |
| REQ-CRIO-007 | CRIO-AC-007 | G1, G2, G3 |
| REQ-CRIO-008 | CRIO-AC-008 | G1, G2, G3 |
| REQ-CRIO-009 | CRIO-AC-009 | G1, G2, G4 |
| REQ-CRIO-010 | CRIO-AC-010 | G1, G2, G3 |
| REQ-CRIO-011 | CRIO-AC-011 | G1, G2, G3, G7 |
| REQ-CRIO-012 | CRIO-AC-012 | G1, G2, G4 |
| REQ-CRIO-013 | CRIO-AC-013 | G1, G2, G3, G7 |
| REQ-CRIO-014 | CRIO-AC-014 | G1, G2, G3, G5 |
| REQ-CRIO-015 | CRIO-AC-015 | G2, G3, G4, G7, G8 |

## 8. Open prerequisites and implementation-readiness limits

- **Identity strategy gates:** DEC-0009, DEC-0010 and DEC-0011 remain Proposed/deferred and non-authoritative; their evaluation authorizations are exhausted. CRIO selects no strict IDNA2008 implementation, blocklist strategy or verification mechanism and grants no new evaluation authority.
- **Missing Identity producers and evidence:** CLSP has no lawful production current-login-email issuer; IEVE has no accepted email-control verification producer; ICAE has no production association issuer, accepted association fact or authoritative observation path. Customer MUST NOT infer any of those outcomes from an intent, request, reference or stored representation.
- **Customer acceptance:** Lawful Customer and Account outcomes, including the Customer-to-Account relationship and current contextual access, must be accepted under separately governed Customer authority. CRIO does not issue, create or prove those outcomes.
- **Implementation mechanics:** Intent issuance and representation, owner-evidence access, concrete transaction and persistence boundaries, retry/reconciliation mechanism, audit representation and operational repair remain unselected. Any material Product, Security, Architecture, Contract, dependency or provider choice requires its separate gate.
- **Production review:** Complete Registration and Login remain blocked while the required decisions and lawful producers are unresolved. A future slice must demonstrate actual current owner evidence, negative/duplicate/concurrent/failure handling, safe disclosure and reviewable reconciliation against the real boundaries.

## 9. Explicit non-authorizations

This Draft authorizes no new registration workflow, trigger or intent issuer; no completed Registration, Authentication, Principal, Session or protected Customer/Account access; no Identity association, login-email or email-verification acceptance; no password-policy completion or canonicalization strategy. It selects no global cardinality, association-changing operation, Customer/Account lifecycle policy, executable Contract or OpenAPI change, endpoint, DTO, event, response status, schema, table, migration, repository, transaction, queue, lock, retry algorithm, provider, dependency or deployment. It does not admit implementation eligibility or reopen DEC-0009, DEC-0010 or DEC-0011 evaluations. Accepted/Approved owner and Contract authority remains unchanged.

## 10. Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-12 | Draft | Defined bounded, conditional Customer-side recognition of governed registration intent and independent owner outcomes, incomplete assessment and safe reconciliation under the Approved Contract; preserved unresolved Identity producers, strategy gates and separate implementation authority. |
