---
title: Current Login Email Source Population Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-12
authoritative: false
---

# Current Login Email Source Population Design

## 1. Purpose and authority boundary

This Approved internal Specification describes the **conditional** authority and safety obligations for initial population of the Identity-owned authoritative current Customer login-email source. Scope code **CLSP** identifies its requirements. It specializes existing Accepted and Approved authority for later review; it is not a production population issuer, a new material Decision, or implementation authorization.

At baseline `06e7cbc82ec825663a0a526d896b85d71c9f8551`, `CurrentLoginEmailSource` has publication and observation mechanics and `AcceptedLoginEmailBinding` represents an already-authorized binding fact. The latter explicitly has no production issuer. A private constructor, stored row, source publication, test fixture, Customer profile value or submitted address cannot establish a lawful Identity-owned binding. This document does not assert that an initial establishment outcome or producer currently exists.

The design begins only **if** a separately authorized Identity-owned initial identifier-establishment outcome has been accepted for an already authoritative stable Identity under DEC-0004 and DEC-0005. It describes obligations for making that accepted fact available to the source without manufacturing its acceptance. If the source or its upstream authority cannot establish the outcome and provenance sufficiently, positive population and reliance remain withheld.

## 2. Scope and explicit exclusions

In scope are Identity ownership, conditional initial-establishment prerequisites, attributable production provenance, stable-subject/exact-email binding, governed comparison and uniqueness, publication and withholding, duplicate and conflict safety, observation of later confirmed supersession, privacy, auditability, and the Customer/Identity coordination boundary. These are semantic responsibilities and review criteria, not prescribed components or data structures.

Out of scope are a registration trigger or workflow; a concrete issuer; login-email change, reassignment, deletion or recovery operations; verification challenge, delivery or acceptance; password-policy completion; submitted-email lookup; Authentication, Principal or Session creation; Customer/Account establishment; public or internal executable Contracts; physical persistence, migration or transaction design; provider, dependency, canonicalization technology, and numerical operating policy.

## 3. Governing authority and traceability

| Label | Source and applicable boundary                                                                                                                                                                                                                                                                                                                                                                                             |
| ----- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| G1    | [DEC-0004](../../decisions/DEC-0004-customer-login-identifier-semantics.md), Identity-owned email identifier category and separation from proof; [DEC-0005](../../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), canonical equality, retained uniqueness, conditional establishment, conflict, currentness and change boundaries.                                                  |
| G2    | [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md), current-email verification as a separate required gate; [IEVE](current-login-email-verification-evidence-design.md), especially REQ-IEVE-001/002/008/009/016, source population, binding, currentness and review.                                                                                                                      |
| G3    | [Product](../../../.ai/core/PRODUCT.md), Customer login-email and verification policy; [Architecture](../../../.ai/core/ARCHITECTURE.md), §30.2.1 owner-local registration coordination; [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md), Identity/Customer/Account authority separation.                                                                                                 |
| G4    | [Identity backend](identity-backend.md) and [Customer backend](../customer/customer-backend.md), existing owner-scoped Requirements; [initial Customer Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), especially §§4–14 and 17–20, conditional coordination, independent owner outcomes and incomplete registration.                                                          |
| G5    | [AGENTS](../../../.ai/core/AGENTS.md), authority hierarchy; [Glossary](../../../.ai/core/GLOSSARY.md), canonical terms; [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), denial, privacy and review; [Decision governance](../../../.ai/core/DECISIONS.md), material-choice escalation; [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), Specification lifecycle.                            |
| G6    | [Database Standards](../../../.ai/backend/DATABASE.md), owner-local source truth and cross-Module access; [API Standards](../../../.ai/backend/API.md), Contract authority.                                                                                                                                                                                                                                                |
| G7    | [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md), [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md) and [DEC-0011](../../decisions/DEC-0011-initial-customer-email-verification-mechanism.md): Proposed, non-authoritative unresolved paths with exhausted evaluations. They supply no accepted implementation strategy or renewed evaluation authority. |
| G8    | Existing [CurrentLoginEmailSource](../../../backend/src/main/java/com/enterprise/fashion/ecommerce/identity/application/loginemail/CurrentLoginEmailSource.java) and [AcceptedLoginEmailBinding](../../../backend/src/main/java/com/enterprise/fashion/ecommerce/identity/application/loginemail/AcceptedLoginEmailBinding.java): inspected implementation evidence only, not governance authority or proof of a producer. |

All normative requirements below specialize G1–G6. G7 states open constraints and G8 records current implementation limits; neither is elevated to Accepted authority.

## 4. Source, value and lifecycle distinctions

Identity owns login-identifier security state. The authoritative current login email is the exact accepted Identity-owned email fact applicable to a stable Identity now. Its canonical comparison representation governs equality and retained ownership under DEC-0005; the representation is not mailbox-control proof. A Customer profile/contact email, request value, comparison key, storage row, publication result or verification challenge is not independently the accepted current-email fact.

Initial establishment, source publication, current observation and email-control verification are separate events with separate evidence. A later confirmed, separately authorized replacement may make a prior binding no longer current; this Specification defines only the resulting observation/currentness obligation. It does not define the requester, trigger, approval, workflow or Session effect of a change.

## 5. Conditional design requirements

### REQ-CLSP-001 — Identity ownership

Only Identity-owned accepted identifier authority may supply a current-login-email binding to the Identity source. Customer coordination, Customer/Account state, a database Adapter, a caller or a provider MUST NOT independently create Identity login-email ownership. Sources: G1; G3; G4; G6.

### REQ-CLSP-002 — Prior authorized initial establishment

Population MUST be withheld unless a separately governed initial registration or Identity workflow has produced an accepted Identity-owned identifier-establishment outcome for the intended stable Identity. Submission, registration intent, validation success, uniqueness check or pending work alone MUST NOT be promoted to acceptance. This requirement does not authorize or define that workflow. Sources: G1 DEC-0005 §Establishment; G3; G4.

### REQ-CLSP-003 — Accountable producer provenance

A later producer MUST demonstrate its authority to carry that already-accepted Identity outcome into the source and preserve attributable provenance across the handoff. A constructed `AcceptedLoginEmailBinding`, row shape, fixture, seed, import or successful write MUST NOT substitute for evidence of lawful production. If the accepted outcome or producer authority cannot be confirmed, population remains unconfirmed. Sources: G1; G2 REQ-IEVE-002/016; G5; G8.

### REQ-CLSP-004 — Stable subject and exact email fact

An eligible fact MUST remain bound to the same already-authoritative stable Identity and exact governed current-email value throughout acceptance, publication and observation. A Customer/Account reference, submitted email, canonical key or caller-selected Identity reference alone MUST NOT establish that binding or transfer it to another Identity. Sources: G1; G2 REQ-IEVE-001/002; G3 ADR-0022.

### REQ-CLSP-005 — Governed comparison

Before an establishment outcome can support population, the email MUST satisfy DEC-0005's complete deterministic validation and canonical comparison semantics. Failure or uncertainty in any required strict IDNA2008 step MUST withhold accepted ownership and positive population; preliminary input preparation is insufficient. This Specification selects no validation library, Unicode-data release, codec or canonicalization implementation and does not resolve DEC-0009. Sources: G1 DEC-0005 §§Decision–Case and Domain Semantics; G7 DEC-0009.

### REQ-CLSP-006 — Retained uniqueness

An eligible initial outcome MUST preserve DEC-0005's at-most-one retained Identity-owned owner per canonical comparison value. The source MUST NOT treat a second accepted owner, unresolved duplicate, collision, ambiguous retained history or uncertain concurrent result as current authoritative population. No database constraint, lock, isolation level or index is selected here. Sources: G1 DEC-0005 §§Logical Uniqueness–Concurrency; G6.

### REQ-CLSP-007 — Publication follows acceptance

Publication MAY make an already-accepted, attributable Identity binding fact available to the Identity source; publication itself MUST NOT decide whether establishment was authorized or accepted. A durable write, returned publication result or later observation MUST NOT retroactively manufacture that acceptance. Source access remains owner-controlled and purpose-limited. Sources: G1; G2 REQ-IEVE-002; G4; G6; G8.

### REQ-CLSP-008 — Withholding and failure

Missing, invalid, incomplete, rejected, unavailable, stale or uncertain establishment or publication evidence MUST NOT be presented as a confirmed current binding. Failure or uncertainty MUST remain distinguishable internally where retry or reconciliation differs; a later attempt MUST recheck applicable authority and MUST NOT overwrite a newer confirmed fact. No retry count, timeout or repair mechanism is selected. Sources: G1 DEC-0005 §Failure and Uncertainty; G2 REQ-IEVE-002/008; G4.

### REQ-CLSP-009 — Same-outcome duplicates

Repeated handling of the same governed accepted initial outcome MUST preserve its subject, exact email and fact attribution without multiplying owner effects. Apparent sameness based only on email text, a caller assertion or storage shape MUST NOT cause two different outcomes to be merged or one to replace the other. Sources: G1 DEC-0005 §§Duplicate, Collision, and Conflict Semantics–Concurrency; G4.

### REQ-CLSP-010 — Collision and concurrent uncertainty

Competing or incompatible ownership claims MUST NOT produce an arbitrary current winner, silent reassignment, cross-Identity merge or successful completion claim. Where uniqueness or publication effect is unconfirmed, Identity MUST withhold positive authority pending separately governed investigation or reconciliation; Customer coordination cannot decide the owner. Sources: G1 DEC-0005 §§Duplicate, Collision, and Conflict Semantics–Failure and Uncertainty; G3; G4.

### REQ-CLSP-011 — Later supersession observation

When separately authorized Identity evidence confirms a replacement binding, observation MUST NOT continue treating the superseded fact as current or restore it through delayed or repeated initial publication. Earlier verification evidence does not verify the replacement. This is an invariant about confirmed facts, not authorization for an email-change operation, reuse, recovery or Session transition. Sources: G1 DEC-0005 §Identifier Change; G2 DEC-0008 and REQ-IEVE-008/009; G3.

### REQ-CLSP-012 — Customer/Identity coordination

Customer MAY coordinate the governed registration intent through bounded owner Contracts, but only Identity may confirm login-identifier ownership and source population. Customer MUST distinguish a pending or uncertain Identity outcome from confirmed ownership and MUST NOT infer registration completion, Customer/Account ownership, association, Authentication, Principal or Session authority from a published email fact. Sources: G3 ADR-0022; G4 Registration Contract §§8–14.

### REQ-CLSP-013 — Security, privacy and auditability

Identity MUST restrict producer and source access to the authorized purpose, protect login email as sensitive PII, preserve accountable provenance and conflict evidence for governed investigation, and keep protected existence and unnecessary email values out of ordinary responses, URLs, logs, metrics, traces, events, analytics, diagnostics and support evidence. External outcomes MUST remain enumeration-resistant. This selects no audit schema, retention period or disclosure mechanism. Sources: G1 DEC-0004/0005 privacy; G2 DEC-0008; G5 Security Standards; G6.

### REQ-CLSP-014 — Production-readiness gate

Before approving a production population slice, a later review MUST identify the actually authorized initial establishment path and lawful Identity producer, demonstrate complete DEC-0005 comparison and uniqueness under accepted implementation authority, and verify provenance, controlled publication, current observation, duplicates, collision, failure, supersession and owner coordination against real boundaries. Fixtures and source mechanics alone MUST NOT satisfy that gate. Material Product, Security, Architecture, dependency or Contract choices encountered require their separate governance. Sources: G1; G2 REQ-IEVE-016; G4–G7.

## 6. Design-level acceptance criteria

These are deterministic **review scenarios**, not assertions that tests ran or the prerequisites exist.

| Criterion   | Scenario and expected boundary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| ----------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CLSP-AC-001 | Present Customer profile email or a source Adapter as the owner: neither can issue Identity ownership.                                                                                                                                                                                                                                                                                                                                                                                                                      |
| CLSP-AC-002 | Present only an intent, submitted email or pending uniqueness result: no accepted initial binding can populate the source.                                                                                                                                                                                                                                                                                                                                                                                                  |
| CLSP-AC-003 | Present a row, fixture or constructed fact without attributable accepted-outcome and producer evidence: no lawful population claim follows.                                                                                                                                                                                                                                                                                                                                                                                 |
| CLSP-AC-004 | Substitute another Identity, Customer reference or email value between accepted outcome and observation: positive binding is withheld.                                                                                                                                                                                                                                                                                                                                                                                      |
| CLSP-AC-005 | Use only preliminary preparation, or make a required strict comparison step fail/uncertain: no accepted owner or positive population follows.                                                                                                                                                                                                                                                                                                                                                                               |
| CLSP-AC-006 | Present two retained Identity owners for one canonical value, or unresolved concurrent ownership: neither becomes an arbitrary current winner.                                                                                                                                                                                                                                                                                                                                                                              |
| CLSP-AC-007 | Publish a shape-compatible fact without prior accepted establishment: publication and observation do not manufacture acceptance.                                                                                                                                                                                                                                                                                                                                                                                            |
| CLSP-AC-008 | Make publication unavailable or ambiguous, then retry after a newer confirmed fact: no stale positive claim or overwrite occurs.                                                                                                                                                                                                                                                                                                                                                                                            |
| CLSP-AC-009 | Repeat the same attributable accepted outcome: no multiplied ownership; a different outcome with matching email text is not silently merged.                                                                                                                                                                                                                                                                                                                                                                                |
| CLSP-AC-010 | Race competing ownership claims or return an uncertain publication result: no success or registration-completion inference follows.                                                                                                                                                                                                                                                                                                                                                                                         |
| CLSP-AC-011 | Observe an old binding after a separately authorized replacement: the old fact is not current and cannot restore prior verification.                                                                                                                                                                                                                                                                                                                                                                                        |
| CLSP-AC-012 | Give Customer only a pending result or bare published email fact without separately confirmed Identity-owned ownership evidence: Customer MUST NOT infer confirmed Identity ownership or completed Registration. Where a bounded governed Contract supplies confirmed Identity-owned ownership evidence, Customer MAY consume it for the identifier criterion only; that evidence alone MUST NOT establish completed Registration, Customer/Account ownership, association, Authentication, Principal or Session authority. |
| CLSP-AC-013 | Inspect ordinary diagnostics and externally visible failure for email/protected-existence leakage: sensitive values remain contained and disclosure safe.                                                                                                                                                                                                                                                                                                                                                                   |
| CLSP-AC-014 | Request production approval with only mechanics or fixtures, unresolved DEC-0009, or no lawful producer: readiness is denied and missing authority identified.                                                                                                                                                                                                                                                                                                                                                              |

## 7. Requirement-to-acceptance traceability

| Requirement  | Acceptance criterion | Governing source labels |
| ------------ | -------------------- | ----------------------- |
| REQ-CLSP-001 | CLSP-AC-001          | G1, G3, G4, G6          |
| REQ-CLSP-002 | CLSP-AC-002          | G1, G3, G4              |
| REQ-CLSP-003 | CLSP-AC-003          | G1, G2, G5, G8          |
| REQ-CLSP-004 | CLSP-AC-004          | G1, G2, G3              |
| REQ-CLSP-005 | CLSP-AC-005          | G1, G7                  |
| REQ-CLSP-006 | CLSP-AC-006          | G1, G6                  |
| REQ-CLSP-007 | CLSP-AC-007          | G1, G2, G4, G6, G8      |
| REQ-CLSP-008 | CLSP-AC-008          | G1, G2, G4              |
| REQ-CLSP-009 | CLSP-AC-009          | G1, G4                  |
| REQ-CLSP-010 | CLSP-AC-010          | G1, G3, G4              |
| REQ-CLSP-011 | CLSP-AC-011          | G1, G2, G3              |
| REQ-CLSP-012 | CLSP-AC-012          | G3, G4                  |
| REQ-CLSP-013 | CLSP-AC-013          | G1, G2, G5, G6          |
| REQ-CLSP-014 | CLSP-AC-014          | G1, G2, G4–G7           |

## 8. Open prerequisites and implementation-readiness limits

- **Initial issuer:** No production issuer of `AcceptedLoginEmailBinding` or equivalent lawfully accepted current-email fact exists. A later review must demonstrate the separately authorized Identity establishment outcome, accountable producer and controlled access path. This Specification supplies none of them.
- **Canonical comparison:** DEC-0005 is Accepted, but DEC-0009 remains Proposed/deferred; no implementation of the strict IDNA2008 profile or authoritative canonical ownership may be assumed from preliminary input preparation.
- **Registration:** DEC-0010 remains Proposed/deferred, so complete prospective-password acceptance and credential establishment cannot be inferred. The Registration Contract permits incomplete coordination but does not authorize a fabricated successful outcome.
- **Email-control proof:** DEC-0011 remains Proposed/deferred. Source population, dispatch and publication do not provide accepted DEC-0008 verification evidence.
- **Change and reuse:** Later login-email change requires its own requester, trigger and workflow authority. Product Decision 28 and applicable retention governance still govern any final post-deletion reuse question.
- **Real implementation:** A later slice must demonstrate lawful source population and implementation behavior with reviewable negative, duplicate, conflict, concurrency, privacy and reconciliation evidence. Any executable cross-Module or public Contract change, material Architecture or Security choice, new dependency, physical persistence change or Product-policy change uses its separate approval gate.

## 9. Explicit non-authorizations

This Specification authorizes no initial Registration or email-change operation, producer implementation, source write, migration, verified-email state, Login lookup, Authentication, Principal, Session, Customer/Account or association establishment, recovery, MFA, provider integration, dependency admission or evaluation. It selects no proof mechanism, canonicalization technology, physical persistence, endpoint, method, DTO, status, schema, lock, cache, queue, retry, timeout, TTL or SLO. It changes no Product, Architecture, Security Standard, Decision, ADR, Contract or OpenAPI authority. It cannot reopen the exhausted DEC-0009, DEC-0010 or DEC-0011 evaluations.

## 10. Revision history

| Version | Date       | Status | Summary                                                                                                                                                                                                                                                                                                                                     |
| ------- | ---------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1.0.0   | 2026-10-12 | Approved | Approved the conditional Identity-owned current-login-email source-population design following approval-readiness review and clarification of CLSP-AC-012; preserved the absence of a lawful production issuer, unresolved DEC-0009/0010/0011 prerequisites, and separate implementation authority. |
| 0.1.0   | 2026-10-12 | Draft  | Defined conditional Identity-owned initial current-login-email source-population obligations and review scenarios under existing authority; recorded the absent lawful production issuer and separate canonicalization, password-policy, verification, Contract and implementation gates without creating authority or selecting mechanics. |
