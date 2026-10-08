---
title: Current Login Email Verification Evidence Design
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-08
authoritative: false
---

# Current Login Email Verification Evidence Design

## 1. Purpose, scope and lifecycle

This Approved Specification describes how Identity may later produce and consume evidence that the **current authoritative Customer login email** bound to a stable Identity has been verified under Accepted DEC-0008. Scope code **IEVE** identifies its requirements. It specializes existing authority for later implementation review; it neither approves a verification mechanism nor authorizes production implementation.

At inspected baseline `b26265b3ec946848a33b83264489190ca8e0786c`, Identity implements preliminary Login-email input preparation and source-bound Customer password verification. The repository does not demonstrate an authoritative current-login-email source or population path, an accepted email-verification producer or consumer, email-verification persistence, complete Authentication, Principal establishment or Session issuance. Existing Session storage/revocation and credential-source machinery supply none of those missing email-verification facts.

This design begins **after** an authoritative stable Identity and its current login-email binding have been obtained. Submitted Login email to stable Identity resolution, including DEC-0005's strict canonical comparison, is a separate prerequisite blocked by unresolved DEC-0009. A future implementation must prove the actual source and accepted verification path; names, stored rows, fixtures and constructors do not establish authority.

## 2. Governing authority

| Label | Governing source and scope |
| --- | --- |
| G1 | [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md): mandatory current-email verification gate, accepted evidence meaning, change/re-verification, replay, privacy and non-decisions. |
| G2 | [DEC-0005](../../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md): authoritative login-identifier equality, retained ownership, currentness and conflict; [DEC-0009](../../decisions/DEC-0009-customer-login-idna2008-implementation-strategy.md): unresolved Proposed strict-IDNA2008 strategy, with no renewed evaluation authority. |
| G3 | [Initial Customer Login Contract](../../contracts/identity/initial-customer-login-contract.md), especially §§5–15, and its [OpenAPI](../../contracts/identity/initial-customer-login.openapi.yaml): separate identifier, credential, email-verification, Authentication, Principal and Session gates. |
| G4 | [Initial Customer Registration Contract](../../contracts/customer/initial-customer-registration-contract.md), especially §§8–16 and 20–24, and its [OpenAPI](../../contracts/customer/initial-customer-registration.openapi.yaml): separately confirmed registration outcomes and verification gate. |
| G5 | [Identity backend](identity-backend.md), especially BIDN-REQ-010–019, 032–034 and 038–050; [Identity Domain](../../domains/identity/identity-domain.md), especially REQ-IDN-004–013 and 039–044; [Customer Domain](../../domains/customer/customer-domain.md), especially REQ-CUS-001–010: owner boundaries, evidence, failure and reconciliation. |
| G6 | [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md), [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md) and [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md): distinct Authentication, Principal, Session and association authority. |
| G7 | [ICRD](customer-credential-state-design.md), [ICED](authoritative-credential-evidence-design.md), [ICSC](credential-source-confirmation-design.md) and [ICPO](credential-publication-observation-design.md): existing credential-state/source/verification distinctions; none is an email-verification source. |
| G8 | [Product](../../../.ai/core/PRODUCT.md), [Architecture](../../../.ai/core/ARCHITECTURE.md), [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), [Glossary](../../../.ai/core/GLOSSARY.md) and [AGENTS](../../../.ai/core/AGENTS.md): Product gate, trust, ownership, terminology and decision hierarchy. |
| G9 | [Testing Standards](../../../.ai/core/TESTING-STANDARDS.md), [Engineering Principles](../../../.ai/core/ENGINEERING-PRINCIPLES.md), [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), [Decision governance](../../../.ai/core/DECISIONS.md) and [API Standards](../../../.ai/backend/API.md): testability, lifecycle, escalation and Contract authority. |
| G10 | [DEC-0010](../../decisions/DEC-0010-customer-password-blocklist-strategy.md): unresolved Proposed establishment-policy strategy, not a Login-time verification check. |

The terms Identity, Customer, Account, Principal, Authentication, Session and Authorization retain their canonical Glossary meanings. The distinctions below describe this bounded design; they do not add repository-wide terms or a stored state machine. DEC-0009 and DEC-0010 remain Proposed and non-authoritative. This Specification cannot renew their exhausted evaluations.

## 3. Semantic distinctions and trust path

| Distinction | Meaning and limit |
| --- | --- |
| Stable Identity subject | The authoritative Identity to which the current login identifier belongs; an input reference alone proves neither existence nor association. |
| Authoritative current login email | The Identity-owned login-email state applicable to that subject now, under DEC-0005; a submitted or Customer profile email is not its substitute. |
| Current-email binding | The attributable relationship between that stable Identity and the exact authoritative email fact/value in its applicable lifecycle context; no key format is selected here. |
| Verification request or challenge initiation | Intent to seek control evidence for a proposed binding; it proves no control or acceptance. |
| Delivery attempt | Dispatch, provider acceptance or delivery activity; it is distinct from verification success. |
| Evidence presentation | A later-governed interaction presenting proposed proof; receipt or parsing alone is not acceptance. |
| Accepted verification outcome | Identity's confirmed conclusion that the later-governed interaction established control of the exact binding. |
| Durable verification evidence or state | An owner-controlled representation of the accepted outcome and its binding; storage alone cannot manufacture acceptance. |
| Current verification evidence | Accepted evidence still applicable to the same authoritative current-email binding at the point of reliance. |
| Superseded verification evidence | Prior evidence no longer applicable following a confirmed relevant change/effect; retained history does not restore it. |
| Consumer observation | An Identity-owned source-backed assessment of accepted outcome, binding, provenance, currentness and confirmation limits. |
| Reliance decision | The bounded decision to satisfy, or withhold, the DEC-0008 gate for a governed operation; it is not complete Authentication. |

The logical path is: **already-authoritative Identity/current-email state → later-governed verification interaction → Identity-owned accepted outcome → protected durable evidence where required → fresh source observation → bounded gate reliance**. Each arrow requires its own evidence. No number of services, reads or transactions is prescribed. An authoritative current-email population path and a verification mechanism are not established at this baseline.

## 4. Design requirements

### REQ-IEVE-001 — Stable subject and current-email authority

Identity MUST identify the intended stable Identity and its current authoritative login-email binding from Identity-owned authority before producing or relying on verification evidence. A submitted email, Customer or Account identifier, Session value, caller flag or email string alone MUST NOT create that binding. This requirement addresses evaluation after authoritative resolution, not submitted-email canonicalization or lookup. Sources: G1; G2 DEC-0005; G3 §5; G5; G6 ADR-0022.

### REQ-IEVE-002 — Current-email source and lawful population

The later Identity-owned current-email source MUST distinguish an attributable current binding from missing, superseded, conflicting, incomplete and unavailable source evidence. It MUST preserve the exact governed email value/fact and stable Identity relationship needed to test verification applicability. A row, projection, Customer profile value or fixture MUST NOT establish authoritative current-email population merely by existing. Later review MUST identify the lawful producer and controlled access path; neither exists by assertion in this Specification. Sources: G1; G2 DEC-0005; G5; G8 Architecture.

### REQ-IEVE-003 — Initiation and delivery do not verify

Challenge creation, token generation, dispatch request, provider acceptance, delivery, email opening, link opening, identifier knowledge, UI state, incomplete challenge and caller assertion MUST NOT establish accepted verification evidence. Initiation and delivery remain distinguishable from presentation and acceptance. Sources: G1 Verification Meaning and Authority; G3 §7; G4 §13; G8 Security Standards.

### REQ-IEVE-004 — Presented proof requires governed acceptance

An Identity-owned producer MUST accept evidence of email control only after a successful interaction under a separately governed verification mechanism, attributable to the intended stable Identity and exact authoritative current-email binding. Mere receipt, parseability or caller-provided success is insufficient. Unknown provenance, invalid or inapplicable presentation and incomplete processing MUST withhold acceptance. This selects no proof artifact, token or provider. Sources: G1 Verification Meaning and Authority, Verification Lifecycle Semantics; G5 BIDN-REQ-012/014/041; G8 Security Standards.

### REQ-IEVE-005 — Accepted outcome and durable representation

The accepted verification outcome and any durable representation MUST preserve their Identity-owned provenance and exact subject/current-email binding. A storage commit, delivery record, challenge record or constructed evidence object alone MUST NOT become an accepted outcome. A later source must be able to distinguish accepted evidence from pending, failed or uncertain work without inferring an ungoverned negative fact. Sources: G1; G5 BIDN-REQ-033/038–042; G8 Architecture.

### REQ-IEVE-006 — Producer's positive gate

Before producing evidence eligible for positive reliance, the Identity-owned producer MUST establish together: authoritative subject/current-email binding, accepted outcome provenance from the governed verification interaction, applicability to that exact binding and consistency with confirmed relevant changes. A password match, prior verification, fixture, provider delivery result or arbitrary constructor MUST NOT fill a missing element. Sources: G1; G3 §§7–8; G5 BIDN-REQ-012/042; G7 ICED/ICSC authority distinctions.

### REQ-IEVE-007 — Consumer observation and binding check

An Identity consumer MUST request source-backed verification evidence for an already-authoritatively identified subject and current-email binding. It MUST verify that the observed accepted outcome belongs to both, has accountable Identity provenance and is applicable for the governed operation. It MUST NOT accept a caller-selected verification flag, combine observations from different bindings or treat an unimplemented Port/mock as production authority. Sources: G1; G3 §§7–8; G5 BIDN-REQ-032/040; G7 ICED/ICSC consumption principles.

### REQ-IEVE-008 — Point-of-reliance currentness

Positive reliance MUST rest on a current observation consistent with relevant already-confirmed superseding, withdrawing or invalidating effects at the point of the governed decision. If intervening change makes earlier evidence insufficient, the consumer MUST re-establish applicability or withhold positive reliance. Earlier acceptance does not guarantee permanent verification. This chooses no lock, isolation level, timestamp, cache policy or transaction technique. Sources: G1 Verification Lifecycle Semantics; G2 DEC-0005 lifecycle; G3 §§7–8, 14; G5 BIDN-REQ-033/039.

### REQ-IEVE-009 — Accepted email change and supersession

When a separately authorized change confirms a replacement authoritative login-email binding for the same Identity, verification evidence for the prior binding MUST NOT verify the replacement. Verification of email A cannot establish verification of email B solely because both were associated with that Identity at different times. Old evidence may remain historical but is inapplicable to the replacement until separately accepted verification exists. This requirement authorizes no email-change operation or Session consequence. Sources: G1 Login-Email Change; G2 DEC-0005 Identifier Change; G3 §7; G4 §13.

### REQ-IEVE-010 — Replay and duplicate safety

Replayed old challenges, presented proof, accepted outcomes or durable records MUST NOT restore verification after a confirmed superseding current-email effect. Duplicate processing of the same governed verification intent MUST preserve its accepted binding and outcome without creating a second acceptance or changing Identity authority. Where sameness or currentness cannot be established, reliance MUST be withheld. No nonce, token format or retry policy is selected. Sources: G1 Verification Lifecycle Semantics and Retry; G3 §14; G4 §§17–18; G5 BIDN-REQ-034.

### REQ-IEVE-011 — Conflicting evidence

Incompatible current-email bindings, verification applicability claims or supersession observations MUST NOT produce an arbitrary verified winner. Where authoritative conflict exists, positive verification reliance MUST be withheld until Identity-owned evidence resolves it; observation alone authorizes no repair or overwrite. Sources: G1 Verification Lifecycle Semantics; G2 DEC-0005 Conflict Semantics; G5 BIDN-REQ-038–040; G8 Engineering Principles.

### REQ-IEVE-012 — Uncertainty and negative-state limits

Unknown provenance, mismatched Identity or email binding, stale, missing, unavailable, incomplete or uncertain evidence MUST NOT satisfy the verification gate. Lack of confirmed positive evidence MUST NOT be reported as an authoritative durable `NOT VERIFIED` fact or confirmed absence unless the responsible source separately establishes that exact bounded claim. Failure and uncertainty remain distinguishable internally where response or reconciliation differs; this design selects no external status. Sources: G1 Verification Lifecycle Semantics; G3 §§7, 13–15; G5 BIDN-REQ-038–040; G8 Security Standards.

### REQ-IEVE-013 — Login and Registration consumption

Later initial Customer Login and Registration decisions MUST each consume accepted evidence for the current authoritative login-email binding as the separate DEC-0008 gate. Login MUST first obtain the stable Identity/current-email binding through the independently governed DEC-0005 path; this design supplies no DEC-0009 strategy or lookup. Registration remains incomplete while required verification is unconfirmed and MUST NOT bypass DEC-0010 or its other independently governed outcomes. Sources: G1; G2; G3 §§5–8; G4 §§8–16; G10.

### REQ-IEVE-014 — Authentication, Principal, Session and association separation

Current-email verification is necessary but insufficient for initial Customer Authentication. Even together with source-bound password `VERIFIED`, it MUST NOT independently establish complete Authentication, a Principal, Session, Authorization, Customer/Account ownership or Identity-to-Customer association. Complete Authentication requires all separately governed accepted evidence for the same attempt/context; Principal and Session establishment remain downstream. ADR-0022 association evidence and Customer-owned contextual Authorization remain independent. Sources: G1; G3 §§8–12; G5 BIDN-REQ-010–013; G6; G7 ICED.

### REQ-IEVE-015 — Sensitive evidence containment

Identity MUST minimize and protect login-email and verification evidence, restrict producer/source/consumer access by purpose and least privilege, and preserve provenance sufficient for accountable investigation without leaking the email, proof artifacts, verification Secrets or protected existence through ordinary results, URLs, logs, metrics, traces, events, analytics, diagnostic errors or support evidence. Public disclosure MUST remain enumeration-resistant. This selects no encryption, provider, Audit Record schema or retention period. Sources: G1 Security and Privacy Boundaries; G3 §13; G4 §§21, 26–27; G5 BIDN-REQ-041–044; G8 Security Standards.

### REQ-IEVE-016 — Review and unresolved-authority guard

A later implementation-readiness review MUST trace the real current-email source/population, accepted verification producer, consumer, supersession visibility and failure path against existing authority. Tests using fixtures or mocks MAY prove bounded mechanics but MUST NOT claim production provenance or complete Authentication. Material Product, Security, Architecture, dependency or Contract choices encountered MUST use their separate governance path; this Specification MUST NOT silently choose them or authorize implementation. Sources: G1 Explicit Non-Decisions; G2 DEC-0009; G5 BIDN-REQ-048–050; G8 AGENTS; G9; G10.

## 5. Design-level acceptance criteria

These are later review scenarios, not claims of executed tests or implemented production authority. Each criterion inherits the cited sources of its traced requirement.

| Criterion | Trace | Review expectation |
| --- | --- | --- |
| IEVE-AC-001 | REQ-IEVE-001 | Substitute a submitted email, Customer ID, Session value or caller flag for current Identity-owned binding: no positive verification authority results. |
| IEVE-AC-002 | REQ-IEVE-002 | A stored or fixture email without a lawful current-email source outcome cannot be treated as current; missing, conflicting and unavailable source states remain distinct. |
| IEVE-AC-003 | REQ-IEVE-003 | Initiation, generated artifact, dispatch, delivery, opening and UI success each fail to satisfy the verification gate on their own. |
| IEVE-AC-004 | REQ-IEVE-004 | Present invalid, mismatched, unproven or incomplete proof: no accepted outcome; successful later-governed proof must bind to the authoritative subject and exact current email. |
| IEVE-AC-005 | REQ-IEVE-005 | A durable row or delivered challenge without an accepted outcome remains non-authoritative; accepted evidence preserves provenance and binding without inventing negative state. |
| IEVE-AC-006 | REQ-IEVE-006 | Remove any one of subject, current-email binding, accepted proof provenance or applicability: the producer cannot issue evidence eligible for positive reliance. |
| IEVE-AC-007 | REQ-IEVE-007 | Substitute another Identity/email observation or caller-supplied verified flag: the consumer withholds reliance; a mock alone does not prove production confirmation. |
| IEVE-AC-008 | REQ-IEVE-008 | Interleave earlier observation and a confirmed relevant change before reliance: re-established applicability is required or positive reliance is withheld. |
| IEVE-AC-009 | REQ-IEVE-009 | Verify email A, confirm a separately authorized change to B, then query B: A's evidence does not verify B or create an email-change effect. |
| IEVE-AC-010 | REQ-IEVE-010 | Replay challenge, proof, accepted result and stored record after supersession: none restores current verification; duplicate same-intent processing creates no extra acceptance. |
| IEVE-AC-011 | REQ-IEVE-011 | Present incompatible current-email or applicability claims: no arbitrary winner, positive gate or automatic repair follows. |
| IEVE-AC-012 | REQ-IEVE-012 | Missing, stale, unavailable, incomplete and uncertain observations withhold the gate without claiming an authoritative `NOT VERIFIED` or confirmed-absence state. |
| IEVE-AC-013 | REQ-IEVE-013 | Login without governed identifier resolution and Registration without all required independent gates remain incomplete despite any email-verification-shaped object. |
| IEVE-AC-014 | REQ-IEVE-014 | Accepted current-email verification, including alongside password `VERIFIED`, grants no complete Authentication, Principal, Session, association or Authorization by itself. |
| IEVE-AC-015 | REQ-IEVE-015 | Review results, diagnostics, telemetry and support surfaces for protected email/proof material and existence disclosure; no such material escapes ordinary boundaries. |
| IEVE-AC-016 | REQ-IEVE-016 | Review the real source, producer and consumer path; fixtures and named interfaces alone cannot prove production readiness or admit an unreviewed material choice. |

## 6. Requirement traceability

| Requirement | Acceptance criterion | Governing source |
| --- | --- | --- |
| REQ-IEVE-001 | IEVE-AC-001 | G1, G2, G3, G5, G6 |
| REQ-IEVE-002 | IEVE-AC-002 | G1, G2, G5, G8 |
| REQ-IEVE-003 | IEVE-AC-003 | G1, G3, G4, G8 |
| REQ-IEVE-004 | IEVE-AC-004 | G1, G5, G8 |
| REQ-IEVE-005 | IEVE-AC-005 | G1, G5, G8 |
| REQ-IEVE-006 | IEVE-AC-006 | G1, G3, G5, G7 |
| REQ-IEVE-007 | IEVE-AC-007 | G1, G3, G5, G7 |
| REQ-IEVE-008 | IEVE-AC-008 | G1, G2, G3, G5 |
| REQ-IEVE-009 | IEVE-AC-009 | G1, G2, G3, G4 |
| REQ-IEVE-010 | IEVE-AC-010 | G1, G3, G4, G5 |
| REQ-IEVE-011 | IEVE-AC-011 | G1, G2, G5, G8 |
| REQ-IEVE-012 | IEVE-AC-012 | G1, G3, G5, G8 |
| REQ-IEVE-013 | IEVE-AC-013 | G1, G2, G3, G4, G10 |
| REQ-IEVE-014 | IEVE-AC-014 | G1, G3, G5, G6, G7 |
| REQ-IEVE-015 | IEVE-AC-015 | G1, G3, G4, G5, G8 |
| REQ-IEVE-016 | IEVE-AC-016 | G1, G2, G5, G8, G9, G10 |

## 7. Later implementation-readiness boundary

Before approving a production slice, review must identify the authoritative current-email source and lawful population path; show how accepted control evidence is produced for that exact Identity/email binding; and demonstrate source-backed consumption at the decision point. It must test accepted email change, supersession and replay, conflicting/uncertain/unavailable evidence, and safe disclosure against the real affected boundaries. It must establish that submitted-email resolution does not bypass DEC-0009, that email verification does not inflate credential `VERIFIED` into complete Authentication, and that Principal/Session/association authority remains separate. Any provider, token, schema, dependency or external Contract choice must be separately reviewed under its governing authority. No synthetic fixture can satisfy the production provenance gate.

## 8. Open questions and escalation boundaries

- **Identifier prerequisite:** DEC-0009 remains unresolved, Proposed and non-authoritative. No strict IDNA2008 strategy, canonical Login key or submitted-email lookup is supplied. Its prior evaluations are exhausted; this design grants no new evaluation.
- **Current-email state:** The actual Identity-owned current-email population, change and access path is not implemented. Later review must establish it under DEC-0005; an email-change workflow is not defined here.
- **Verification mechanism:** Challenge/proof interaction, delivery provider, artifact representation, token or code format, entropy, hashing/signing, validity/expiry, resend, attempts, retry and abuse controls remain for later governed design or implementation review. Material Security decisions require explicit governance. No provider or dependency is admitted.
- **Storage and consistency:** Durable representation, schema, repository, concurrency technique and effect visibility remain unselected. Later implementation must demonstrate the semantic currentness and replay guarantees without inventing confirmed absence.
- **Downstream outcomes:** Complete Authentication orchestration, current Identity/credential eligibility evidence, Principal/Session integration, Identity-to-Customer association and Customer-owned access decisions remain separate. Recovery, MFA and existing-Session consequences of email change remain separately governed.
- **Establishment:** DEC-0010 remains unresolved/deferred. Email verification cannot substitute for complete prospective-password policy or lawfully populate credentials.

If implementation needs a material new Product, Security, Architecture or trust-boundary choice beyond Accepted/Approved authority, affected work must stop at the appropriate DEC/ADR or canonical review. Ordinary Java, Spring and persistence details need no new durable decision when they faithfully implement an approved boundary.

## 9. Explicit non-decisions and review

This Specification defines no submitted-email canonicalization, lookup, uniqueness mechanism, verification challenge or delivery operation, email-change operation, recovery or MFA factor, password policy, Account lifecycle, association cardinality, Principal or Session creation, Authorization policy, endpoint, response, DTO, cookie, provider, cryptographic library, dependency, database object, SQL, cache, event topic, physical transaction or lock. It changes no Login or Registration Contract and neither resolves DEC-0009 nor DEC-0010. Verification of the current email remains a required **separate gate**, never complete Authentication by itself.

Controlled approval reviewed Identity provenance and currentness, Security replay/default-denial/privacy, Architecture and ADR-0022 separation, Customer/Registration impacts, Engineering testability and traceability. Approval covers this bounded internal design only; it does not establish implementation eligibility or renew an exhausted evaluation.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-08 | Approved | Approved the bounded Identity current-login-email verification evidence design after direct validation and a clean repository-wide approval-readiness review; preserved DEC-0008 authority, unresolved DEC-0009/DEC-0010 prerequisites, provider/token/persistence/Contract neutrality and the separate implementation-readiness gate without authorizing production implementation or renewed evaluation. |
| 0.1.0 | 2026-10-08 | Draft | Defined the bounded Identity current-login-email verification evidence boundary, including source/subject binding, accepted producer and consumer responsibility, point-of-reliance currentness, supersession, replay, conflict, uncertainty, privacy and downstream non-authority, while preserving DEC-0008 policy, unresolved DEC-0009/DEC-0010 prerequisites and provider/token/persistence/Contract neutrality without implementation authority. |
