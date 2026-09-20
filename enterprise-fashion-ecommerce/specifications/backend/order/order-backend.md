---
title: Order Backend Specification
version: 0.1.0
status: Draft
owner: Backend
last_updated: 2026-09-20
authoritative: false
scope: BORD
---

# Order Backend Specification

## 1. Purpose

This Draft defines implementation-facing backend obligations for the Approved Order Domain under scope `BORD`. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the Order backend scope, remain `authoritative: false`, and remain subordinate to governing sources, Approved Business Requirements, the Approved Order Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, and applicable repository standards.

BORD owns only durable Order commercial truth. It consumes governed evidence without acquiring Checkout, Payment, Shipping and Fulfilment, Inventory, Product, Pricing, Customer, Identity, Cart, Return, Administration, Notifications, Reporting, CMS, Category, Search, or other authority. It resolves no Open Product or Architecture Decision and establishes no post-BORD roadmap position.

## 2. Requirements

### BORD-REQ-001 — Lifecycle, Authority, and Scope
BORD MUST use scope `BORD`, remain `authoritative: false`, identify its `0.1.0 Draft` lifecycle, specialize only the Approved Order Domain, and claim no repository-wide authority.

### BORD-REQ-002 — Order-Owned Commercial Truth
BORD MUST own Order identity, Order Number, creation truth, durable commercial records, Order Items, Order Snapshots, lifecycle and status history, controlled transitions, cancellation coordination, and Order-owned failure, recovery, and reconciliation history without acquiring source authority for retained evidence.

### BORD-REQ-003 — Cross-Domain Non-Authority
BORD MUST NOT own or redefine live Product, Customer, Address, Identity, Pricing, Payment, Inventory, Shipping, Fulfilment, Return, Notification, Reporting, tax, invoice, Credit Note, Promotion, fraud, or credit policy or truth.

### BORD-REQ-004 — Stable Order Identity
BORD MUST establish stable unique Order identity correlated to creation evidence and history without inferring it from external state or selecting an identifier format, generator, sequence, or persistence mechanism.

### BORD-REQ-005 — Order Number
BORD MUST establish a unique stable customer-facing Order Number distinct from internal identity and never treat it as an Authorization credential, without selecting its format or allocation mechanism.

### BORD-REQ-006 — Identifier Exposure and Isolation
BORD MUST protect Order identity and Order Number through contextual Authorization, ownership checks, Resource isolation, enumeration resistance, correlation, and safe errors; identifier knowledge MUST NOT grant access or reveal existence.

### BORD-REQ-007 — Governed Checkout Handoff
BORD MUST accept creation only from the governed BCHK handoff and preserve applicable Cart, actor, Address, Product, commercial, Payment, Inventory, reservation, delivery, correlation, and Authorization context while BCHK retains Checkout orchestration authority.

### BORD-REQ-008 — Independent Handoff Validation
BORD MUST independently validate handoff authority, identity, correlation, completeness, freshness, consistency, and actor context and return distinguishable safe outcomes for invalid, incomplete, stale, mismatched, unauthorized, unavailable, duplicate, or uncertain input.

### BORD-REQ-009 — Order Creation Truth
Only BORD MAY establish Order existence and successful creation; request receipt, validation, BCHK success, Payment success, client state, callbacks, expectations, or downstream effects MUST NOT independently prove creation.

### BORD-REQ-010 — Creation Outcome Separation
BORD MUST distinguish creation request receipt, processing acceptance, success, confirmation, rejection, duplication, failure, and uncertainty, with governed correlation and duplicate safety and without selecting an orchestration mechanism.

### BORD-REQ-011 — Creation and Confirmation Separation
BORD MUST keep creation distinct from `Confirmed`; an Order MAY use `Pending Payment` where governed, and confirmation MUST require the applicable validated and accepted Payment arrangement without imposing a universal sequence.

### BORD-REQ-012 — Duplicate Order Prevention
Duplicate, retried, replayed, stale, concurrent, or reordered handoffs MUST NOT create multiple Orders for one governed purchase effect; idempotency semantics MUST remain explicit without selecting keys, storage, locks, constraints, caches, queues, or retry counts.

### BORD-REQ-013 — Retry, Replay, and Concurrency Safety
Before repeating an irreversible Order operation, BORD MUST classify prior effects as known, unknown, conflicting, or safely repeatable and MUST prevent replay, concurrency, reordering, or staleness from overwriting truth, bypassing transitions, or multiplying effects.

### BORD-REQ-014 — Timeout and Partial Completion
BORD MUST distinguish timeout, dependency unavailability, interruption, delay, and partial completion from creation, definitive failure, and cancellation while preserving known, unknown, and failed effect provenance.

### BORD-REQ-015 — Order Item Ownership
BORD MUST own each Order Item as historical purchased-item truth unambiguously associated with its Order and distinct from Cart Items, live Product/Variant, Inventory, reservation, Shipment, and Fulfilment records, without defining a field or schema design.

### BORD-REQ-016 — Historical Product Representation
BORD MUST retain sufficient governed Product and Product Variant identity and representation for historical interpretation, and later source changes MUST NOT rewrite Order Items or make BORD current catalogue authority.

### BORD-REQ-017 — Order Snapshot Authority
BORD MUST own the Order Snapshot as confirmation-time historical evidence and MAY retain governed Product, quantity, commercial, actor, Address, delivery, Payment, Inventory, and reservation context without becoming live authority for those sources.

### BORD-REQ-018 — Snapshot Completeness and Interpretability
BORD MUST retain sufficient governed context, associations, provenance, and meaning to explain a confirmed purchase to authorized consumers; missing or inconsistent required evidence MUST block unsupported confirmation or remain explicitly unresolved.

### BORD-REQ-019 — Snapshot Immutability
BORD MUST prevent silent mutation, replacement, or retroactive recalculation of confirmed snapshot values when later Product, Customer, Address, Pricing, Payment, Inventory, Shipping, or delivery evidence changes.

### BORD-REQ-020 — Historical Change and Correction Boundary
BORD MUST append or associate governed later transition, cancellation, Return, Refund, correction, privacy, retention, anonymization, or deletion evidence without fabricating prior commercial truth or selecting policy or mechanism.

### BORD-REQ-021 — Customer or Visitor Association
BORD MUST preserve the governed Customer or Visitor association applicable at creation without deciding guest, Account, or email-verification policy, and later identity or profile changes MUST NOT establish, authorize, or rewrite Order truth.

### BORD-REQ-022 — Address Snapshot
BORD MUST preserve applicable historical Address context distinct from current BCUS Address and Shipping destination truth; later change or removal MUST NOT rewrite the Order Snapshot, and no Address schema, policy, or workflow is selected.

### BORD-REQ-023 — Historical Commercial Values
BORD MUST retain confirmed BPRC-governed Price, Money, Currency, Discount, Promotion, Voucher, Tax, Shipping charge, and total evidence without independently calculating or recalculating those values or policies.

### BORD-REQ-024 — Money and Currency Integrity
BORD MUST use explicit precision-safe Money and Currency, preserve Version 1 ZAR governance, and reject silent mixing or conversion without selecting rounding, foreign-exchange, Minor Unit, storage, scale, or precision rules.

### BORD-REQ-025 — Commercial Policy Non-Authority
BORD MUST NOT invent tax, invoice, numbering, Credit Note, accounting, stacking, restoration, threshold, credit, or legal policy and MUST preserve applicable historical evidence without converting it into policy authority.

### BORD-REQ-026 — Payment Evidence Consumption
BORD MAY consume and correlate trusted Payment-owned evidence and references while Payment retains all Payment, provider, processing, Refund, Chargeback, and reconciliation authority; Order status MUST NOT fabricate Payment truth.

### BORD-REQ-027 — Payment and Order Independence
BORD MUST preserve independent confirmation of Payment and Order truth; redirects, browser results, client reports, UI/local state, unvalidated provider information, and expectations MUST establish neither Payment evidence nor Order creation.

### BORD-REQ-028 — Payment Evolution and Order History
BORD MAY retain governed Payment references and history while all Payment evolution remains Payment-owned and distinct from Order lifecycle, fulfilment, cancellation, Return, Refund, and the immutable commercial snapshot.

### BORD-REQ-029 — Payment and Order Mismatch
BORD MUST keep Payment/Order mismatch explicit, correlated, recoverable, duplicate-safe, and reconcilable without fabricating Order or Payment results, duplicating financial effects, or assuming automatic reversal.

### BORD-REQ-030 — Inventory Coordination
BORD MAY retain BINV references and request permitted reservation consumption, release, finalization, or other Inventory effects while BINV retains all Stock, availability, reservation, adjustment, movement, and overselling authority.

### BORD-REQ-031 — Inventory Evidence and Mismatch
BORD MUST NOT infer Inventory truth from Order state and MUST keep reservation uncertainty, replay, effects without Order, cancellation coordination, and disagreement explicit, duplicate-safe, and reconcilable without selecting timing, locks, allocation, or persistence.

### BORD-REQ-032 — Governed Fulfilment Handoff
BORD MAY provide governed Order, Order Item, Fulfilment Group, Address, delivery, and permitted commercial context for future Shipping/Fulfilment work while Shipping retains Shipment, Carrier, Dispatch, tracking, delivery, provider, and fulfilment authority.

### BORD-REQ-033 — Order and Shipping State Separation
BORD lifecycle meanings MAY use governed Fulfilment evidence but MUST NOT substitute for Shipping truth; disagreement, delay, duplication, partial completion, and uncertainty MUST remain distinguishable and reconcilable.

### BORD-REQ-034 — Lifecycle Authority and History
BORD MUST own explicit, controlled, authorized, attributable, time-correlated, historically traceable Order transitions and safely reject invalid, stale, conflicting, duplicate, replayed, or unauthorized transitions without rewriting history.

### BORD-REQ-035 — Canonical Lifecycle Vocabulary
BORD MUST use only `Pending Payment`, `Confirmed`, `Processing`, `Partially Fulfilled`, `Fulfilled`, `Cancelled`, `Completed`, and `Archived` where applicable, without requiring every state or defining a complete transition graph or universal ordering.

### BORD-REQ-036 — Cross-Domain Lifecycle Integrity
BORD lifecycle state MUST NOT establish or mutate Payment, Inventory, reservation, Shipment, delivery, Return, Refund, Notification, or Projection truth; externally evidenced transitions MUST retain source, uncertainty, and reconciliation boundaries.

### BORD-REQ-037 — Cancellation Coordination
BORD MUST own cancellation coordination and `Cancelled` meaning while leaving eligibility and cutoff policy unresolved; requests MUST use current state, trusted Authorization, governed eligibility, duplicate safety, partial-completion awareness, explicit uncertainty, and history.

### BORD-REQ-038 — Cancellation Cross-Domain Effects
BORD MAY request governed external cancellation effects but MUST NOT imply confirmed Refund, Void, Stock release, Shipment stop, Return, or Notification; unknown effects MUST be reconciled without invented windows, cutoffs, actions, or permission matrices.

### BORD-REQ-039 — Return and Refund Non-Authority
BORD MAY supply stable Order history and retain Return or Refund references but MUST NOT define Return or Refund eligibility, lifecycle, amount, method, provider processing, or execution; Refund execution remains Payment-owned.

### BORD-REQ-040 — Failure Provenance
BORD MUST preserve distinct provenance for handoff, creation, Product, actor, Address, Pricing, Payment, Inventory, reservation, Shipping, cancellation, Return, Refund, dependency, Authorization, and lifecycle failures rather than fabricate generic truth.

### BORD-REQ-041 — Recovery and Reconciliation
BORD recovery MUST preserve correlation, authoritative evidence, history, duplicate safety, Authorization, observability, and resolved or retained uncertainty without directly rewriting another Domain's authoritative record.

### BORD-REQ-042 — Authorization and Tamper Resistance
Protected BORD Use Cases MUST enforce trusted server-side contextual Authorization, ownership, isolation, replay and applicable CSRF protection; client state, identifiers, labels, Claims, Scope, Authentication alone, or external state MUST NOT authorize behavior.

### BORD-REQ-043 — Sensitive Data Protection
BORD MUST minimize and protect Customer, Visitor, Address, contact, identifiers, Payment references, operational notes, and other Sensitive Data across persistence, transport, logs, errors, Contracts, events, Projections, audit, analytics, exports, and support surfaces.

### BORD-REQ-044 — Fraud and Manual-Review Non-Authority
BORD MAY consume a governed fraud or review outcome but MUST NOT define scoring, providers, thresholds, rules, blocking, or manual-review workflows or convert a signal into Payment, Order, cancellation, Return, or commercial truth.

### BORD-REQ-045 — Order Contract Boundary
BORD Contracts MUST preserve authority, identity, correlation, historical evidence, lifecycle, failure, uncertainty, idempotency, Authorization, security, compatibility, and explicit outcomes without defining routes, methods, statuses, DTOs, provider payloads, or schemas.

### BORD-REQ-046 — Conditional Canonical Order Events
BORD MUST create Domain or Integration Events only when separately governed; events MUST represent completed Order-owned facts and preserve authority, correlation, causation, compatibility, privacy, replay safety, and ordering uncertainty without selecting names, payloads, topics, brokers, or choreography.

### BORD-REQ-047 — Accessible Order Outcomes
BORD Contracts MUST expose truthful distinguishable semantics sufficient for applicable WCAG 2.2 AA Order creation, history, state, cancellation, failure, uncertainty, and recovery surfaces without defining design, components, wording, or certification.

### BORD-REQ-048 — Administration Boundary
Administration MAY invoke explicit protected BORD Use Cases without bypassing Order rules or acquiring authority; high-Risk actions MUST use contextual Authorization, applicable confirmation/approval, reasons/evidence, and proportional audit without defining roles or escalation policy.

### BORD-REQ-049 — Performance and Observability
BORD interactions MUST be bounded, correlated, attributable, and observable across creation, transitions, dependencies, concurrency, timeout, uncertainty, mismatch, recovery, and reconciliation without defining numerical targets.

### BORD-REQ-050 — Proportional Order Audit
BORD MUST create proportional Audit Records for applicable creation, transition, cancellation, privileged access, correction, recovery, reconciliation, configuration, and high-Risk actions without treating routine reads as high-Risk or selecting retention and escalation policy.

### BORD-REQ-051 — Non-Authoritative Representations
Notifications, reporting, analytics, exports, and Projections MUST derive from governed evidence, protect Sensitive Data, identify sources where applicable, and MUST NOT create or mutate Order, Payment, Shipping, Inventory, Customer, or other truth or select providers, taxonomies, formats, or channels.

### BORD-REQ-052 — Order Verification Coverage
BORD verification MUST cover every Order Requirement, positive and negative paths, authority boundaries, creation, history, lifecycle, concurrency, idempotency, uncertainty, recovery, security, accessibility, observability, audit, Contracts, events, compatibility, and operations without selecting tools or numerical coverage targets.

### BORD-REQ-053 — Module, Use Case, and Port Boundaries
BORD MUST preserve the modular-monolith, hexagonal dependency direction, explicit application Use Cases, project-owned Ports, small public Module Contracts, and Adapter translation; transport, persistence, providers, and framework types MUST NOT own Order policy or leak inward.

### BORD-REQ-054 — Persistence, Transactions, and Durable Workflow
BORD-owned persistence MUST use project-owned Ports, explicit focused transactions, integrity, concurrency control, historical evidence, durable correlation, and recoverability; cross-system work MUST NOT imply distributed atomicity or remote success from a local commit.

### BORD-REQ-055 — Provider, Compatibility, Migration, and Configuration
Any later governed provider or callback intersection MUST use owned Ports and Adapters, validated evidence, explicit failure/unknown outcomes, replay safety, compatibility, safe migration, bounded work, Secret safety, and reconciliation without selecting providers, schemas, flags, infrastructure, or numerical values.

### BORD-REQ-056 — BEB, Decision, and Roadmap Containment
BORD MUST inherit every materially applicable `BEB-REQ-001` through `BEB-REQ-056`, preserve all 21 Product and 11 Architecture Decisions unresolved, preserve the canonical sequence through BORD, keep CMS and Payment independently eligible and unordered, and establish no later backend identity or position.

## 3. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BORD-AC-001 | BORD-REQ-001 | Metadata shows `0.1.0 Draft`, owner `Backend`, `authoritative: false`, scope `BORD`, Order-only scope, and no repository-wide claim. |
| BORD-AC-002 | BORD-REQ-002 | Each listed Order-owned fact is represented while retained evidence transfers no source authority. |
| BORD-AC-003 | BORD-REQ-003 | No listed external truth, policy, lifecycle, or representation becomes BORD-owned. |
| BORD-AC-004 | BORD-REQ-004 | Identity is stable, unique, correlated, externally non-inferable, and mechanism-neutral. |
| BORD-AC-005 | BORD-REQ-005 | Order Number is unique, stable, distinct, non-authorizing, and format-neutral. |
| BORD-AC-006 | BORD-REQ-006 | Unauthorized identifier use reveals neither access nor existence. |
| BORD-AC-007 | BORD-REQ-007 | Creation accepts only governed BCHK context and Checkout authority remains external. |
| BORD-AC-008 | BORD-REQ-008 | Every invalid handoff condition produces a distinguishable safe outcome. |
| BORD-AC-009 | BORD-REQ-009 | Only durable BORD evidence proves Order creation. |
| BORD-AC-010 | BORD-REQ-010 | All creation outcomes remain distinguishable, correlated, and duplicate-safe. |
| BORD-AC-011 | BORD-REQ-011 | Creation and confirmation remain distinct without a universal Payment sequence. |
| BORD-AC-012 | BORD-REQ-012 | Repeated handoffs create one purchase effect and select no idempotency mechanism. |
| BORD-AC-013 | BORD-REQ-013 | Repetition is evidence-classified and cannot overwrite or multiply effects. |
| BORD-AC-014 | BORD-REQ-014 | Timeout and partial completion preserve known, unknown, and failed effects. |
| BORD-AC-015 | BORD-REQ-015 | Each Order Item is owned, associated, historically distinct, and schema-neutral. |
| BORD-AC-016 | BORD-REQ-016 | Historical Product representation survives source change without current authority transfer. |
| BORD-AC-017 | BORD-REQ-017 | Snapshot evidence is Order-owned history while each live source retains authority. |
| BORD-AC-018 | BORD-REQ-018 | Required snapshot context is explainable; gaps block confirmation or remain explicit. |
| BORD-AC-019 | BORD-REQ-019 | Later source changes cannot silently mutate confirmed values. |
| BORD-AC-020 | BORD-REQ-020 | Later evidence is associated without fabricating history or selecting policy. |
| BORD-AC-021 | BORD-REQ-021 | Actor association is preserved without resolving guest/account/verification policy. |
| BORD-AC-022 | BORD-REQ-022 | Historical Address remains distinct and unchanged by current Address mutation. |
| BORD-AC-023 | BORD-REQ-023 | Confirmed values come from BPRC evidence and are not recalculated by BORD. |
| BORD-AC-024 | BORD-REQ-024 | Money/Currency handling is explicit, precision-safe, ZAR-compatible, and mechanism-neutral. |
| BORD-AC-025 | BORD-REQ-025 | No listed commercial or legal policy is invented. |
| BORD-AC-026 | BORD-REQ-026 | Payment evidence remains Payment-owned and Order state fabricates no Payment truth. |
| BORD-AC-027 | BORD-REQ-027 | Neither Payment nor Order truth proves the other and client evidence proves neither. |
| BORD-AC-028 | BORD-REQ-028 | Payment evolution remains external and cannot rewrite the confirmed snapshot. |
| BORD-AC-029 | BORD-REQ-029 | Payment/Order mismatch is explicit, duplicate-safe, and reconcilable. |
| BORD-AC-030 | BORD-REQ-030 | Inventory effects are requested through BINV while Inventory authority remains external. |
| BORD-AC-031 | BORD-REQ-031 | Inventory mismatch remains explicit and mechanism-neutral. |
| BORD-AC-032 | BORD-REQ-032 | Fulfilment receives bounded context while all Shipping authority remains external. |
| BORD-AC-033 | BORD-REQ-033 | Order lifecycle and Shipping truth remain distinguishable and reconcilable. |
| BORD-AC-034 | BORD-REQ-034 | Transitions are authorized, attributable, validated, and history-preserving. |
| BORD-AC-035 | BORD-REQ-035 | Only eight canonical meanings appear without a complete graph or mandatory traversal. |
| BORD-AC-036 | BORD-REQ-036 | Order lifecycle mutates no external truth and retains evidence uncertainty. |
| BORD-AC-037 | BORD-REQ-037 | Cancellation is authorized, policy-dependent, duplicate-safe, and historically explicit. |
| BORD-AC-038 | BORD-REQ-038 | Cancellation implies no external effect and uncertainty is reconciled. |
| BORD-AC-039 | BORD-REQ-039 | Return and Refund authority remains external and no policy is selected. |
| BORD-AC-040 | BORD-REQ-040 | Every listed failure retains distinct owning-source provenance. |
| BORD-AC-041 | BORD-REQ-041 | Recovery is authorized, correlated, history-preserving, duplicate-safe, and evidence-based. |
| BORD-AC-042 | BORD-REQ-042 | Protected Use Cases enforce contextual Authorization and tamper resistance. |
| BORD-AC-043 | BORD-REQ-043 | Sensitive Data is minimized and protected across every listed surface. |
| BORD-AC-044 | BORD-REQ-044 | No fraud mechanism or policy is selected and signals establish no truth. |
| BORD-AC-045 | BORD-REQ-045 | Contracts preserve governed semantics and define no concrete API, DTO, payload, or schema. |
| BORD-AC-046 | BORD-REQ-046 | Conditional events preserve authority and safety without selecting event or messaging design. |
| BORD-AC-047 | BORD-REQ-047 | Contract semantics support accessible truthful surfaces without presentation invention. |
| BORD-AC-048 | BORD-REQ-048 | Administration uses protected Order capabilities without authority transfer or Role matrix. |
| BORD-AC-049 | BORD-REQ-049 | Operations are bounded and observable without numerical targets. |
| BORD-AC-050 | BORD-REQ-050 | High-Risk actions have proportional audit while routine reads are not overclassified. |
| BORD-AC-051 | BORD-REQ-051 | Derived representations cannot create or mutate authoritative truth. |
| BORD-AC-052 | BORD-REQ-052 | Verification covers every listed concern without selecting tooling or targets. |
| BORD-AC-053 | BORD-REQ-053 | Architecture evidence shows inward dependencies, Use Cases, Ports, Contracts, and Adapter translation. |
| BORD-AC-054 | BORD-REQ-054 | Persistence and workflow evidence preserves invariants, history, atomicity boundaries, and recovery. |
| BORD-AC-055 | BORD-REQ-055 | Provider, compatibility, migration, and configuration safeguards remain mechanism-neutral. |
| BORD-AC-056 | BORD-REQ-056 | BEB is complete, decisions remain unresolved, and the roadmap ends at BORD. |

## 4. Requirement Traceability

| BORD Requirement | Order Domain source | BEB source | Additional source |
| --- | --- | --- | --- |
| BORD-REQ-001 | REQ-ORD-001 | BEB-REQ-001–004 | ADR-0011; DOCUMENTATION-STANDARDS.md |
| BORD-REQ-002 | REQ-ORD-002 | BEB-REQ-003–004, 009, 021 | Order Domain |
| BORD-REQ-003 | REQ-ORD-003 | BEB-REQ-003–004, 021 | ADR-0011; owning Domains |
| BORD-REQ-004 | REQ-ORD-004 | BEB-REQ-021, 023, 027 | DATABASE.md |
| BORD-REQ-005 | REQ-ORD-005 | BEB-REQ-021, 023 | SECURITY-STANDARDS.md |
| BORD-REQ-006 | REQ-ORD-006 | BEB-REQ-011, 017, 020 | SECURITY-STANDARDS.md; API.md |
| BORD-REQ-007 | REQ-ORD-007 | BEB-REQ-008–010, 021 | BCHK; ADR-0011 |
| BORD-REQ-008 | REQ-ORD-008 | BEB-REQ-009–010, 015, 041 | BCHK; API.md |
| BORD-REQ-009 | REQ-ORD-009 | BEB-REQ-010, 021, 025 | BCHK; Payment Domain |
| BORD-REQ-010 | REQ-ORD-010 | BEB-REQ-009–010, 028–031, 041 | API.md |
| BORD-REQ-011 | REQ-ORD-011 | BEB-REQ-010, 021, 024 | Payment Domain |
| BORD-REQ-012 | REQ-ORD-012 | BEB-REQ-027–031 | BCHK; Payment Domain |
| BORD-REQ-013 | REQ-ORD-013 | BEB-REQ-027–031, 041–042 | DATABASE.md; API.md |
| BORD-REQ-014 | REQ-ORD-014 | BEB-REQ-026, 028, 033–034, 041–042 | BCHK; Payment Domain |
| BORD-REQ-015 | REQ-ORD-015 | BEB-REQ-021–024 | BPRD; BCART |
| BORD-REQ-016 | REQ-ORD-016 | BEB-REQ-021, 024 | BPRD; BCAT |
| BORD-REQ-017 | REQ-ORD-017 | BEB-REQ-021, 023–025 | BPRD; BCUS; BPRC; BINV |
| BORD-REQ-018 | REQ-ORD-018 | BEB-REQ-023–025, 041 | Order Domain |
| BORD-REQ-019 | REQ-ORD-019 | BEB-REQ-021, 024–025 | DATABASE.md |
| BORD-REQ-020 | REQ-ORD-020 | BEB-REQ-024, 041–042, 049 | BCUS; Payment/Return Domains |
| BORD-REQ-021 | REQ-ORD-021 | BEB-REQ-019–021, 024, 056 | BIDN; BCUS; PRODUCT.md §24 items 4–5 |
| BORD-REQ-022 | REQ-ORD-022 | BEB-REQ-021, 024, 043 | BCUS; Shipping Domain |
| BORD-REQ-023 | REQ-ORD-023 | BEB-REQ-021, 023–024 | BPRC; BCHK |
| BORD-REQ-024 | REQ-ORD-024 | BEB-REQ-023–024 | BPRC; DATABASE.md |
| BORD-REQ-025 | REQ-ORD-025 | BEB-REQ-003, 021, 024, 056 | PRODUCT.md §24 items 8–9, 14, 25, 29 |
| BORD-REQ-026 | REQ-ORD-026 | BEB-REQ-008, 021, 032–036, 043–044 | Payment Domain; BCHK |
| BORD-REQ-027 | REQ-ORD-027 | BEB-REQ-010, 021, 032–036, 041 | Payment Domain; BCHK |
| BORD-REQ-028 | REQ-ORD-028 | BEB-REQ-021, 024, 041–042 | Payment Domain |
| BORD-REQ-029 | REQ-ORD-029 | BEB-REQ-028–036, 041–042 | Payment Domain; BCHK |
| BORD-REQ-030 | REQ-ORD-030 | BEB-REQ-008, 021, 025–031 | BINV; BCHK |
| BORD-REQ-031 | REQ-ORD-031 | BEB-REQ-027–031, 041–042 | BINV |
| BORD-REQ-032 | REQ-ORD-032 | BEB-REQ-008, 021, 024–031 | Shipping Domain |
| BORD-REQ-033 | REQ-ORD-033 | BEB-REQ-021, 028, 041–042 | Shipping Domain |
| BORD-REQ-034 | REQ-ORD-034 | BEB-REQ-009, 021, 024–031, 045 | Order Domain |
| BORD-REQ-035 | REQ-ORD-035 | BEB-REQ-003, 009, 021, 056 | GLOSSARY.md |
| BORD-REQ-036 | REQ-ORD-036 | BEB-REQ-003, 021, 024, 041 | Payment; Inventory; Shipping; Return Domains |
| BORD-REQ-037 | REQ-ORD-037 | BEB-REQ-009, 011, 025, 027–031, 045 | PRODUCT.md §24 item 10 |
| BORD-REQ-038 | REQ-ORD-038 | BEB-REQ-025–034, 041–042 | Payment; BINV; Shipping; Return Domains |
| BORD-REQ-039 | REQ-ORD-039 | BEB-REQ-003, 021, 024, 056 | Payment and Return Domains; PRODUCT.md §24 item 11 |
| BORD-REQ-040 | REQ-ORD-040 | BEB-REQ-017, 041–042 | BCHK; owning Domains |
| BORD-REQ-041 | REQ-ORD-041 | BEB-REQ-028–031, 041–046 | SECURITY-STANDARDS.md; DATABASE.md |
| BORD-REQ-042 | REQ-ORD-042 | BEB-REQ-011, 015, 019–020, 043 | BIDN; BCUS; SECURITY-STANDARDS.md |
| BORD-REQ-043 | REQ-ORD-043 | BEB-REQ-014, 017, 020, 043–046 | SECURITY-STANDARDS.md; API.md |
| BORD-REQ-044 | REQ-ORD-044 | BEB-REQ-003, 011, 021, 056 | PRODUCT.md §24 item 26 |
| BORD-REQ-045 | REQ-ORD-045 | BEB-REQ-008, 013–018, 050 | API.md; DOCUMENTATION-STANDARDS.md |
| BORD-REQ-046 | REQ-ORD-046 | BEB-REQ-037–040, 050 | EVENTS.md; ARCHITECTURE.md §34 item 9 |
| BORD-REQ-047 | REQ-ORD-047 | BEB-REQ-010, 014, 017–018 | Order Domain; API.md |
| BORD-REQ-048 | REQ-ORD-048 | BEB-REQ-011, 019–020, 045–046 | Administration Domain; PRODUCT.md §24 items 23–24 |
| BORD-REQ-049 | REQ-ORD-049 | BEB-REQ-033–034, 041–042, 046, 051 | ENGINEERING-PRINCIPLES.md |
| BORD-REQ-050 | REQ-ORD-050 | BEB-REQ-011, 045–046 | SECURITY-STANDARDS.md |
| BORD-REQ-051 | REQ-ORD-051 | BEB-REQ-003, 021, 037–040, 043 | Notifications and Reporting Domains |
| BORD-REQ-052 | REQ-ORD-052 | BEB-REQ-052–055 | TESTING-STANDARDS.md |
| BORD-REQ-053 | REQ-ORD-002–003, 045 | BEB-REQ-005–010, 022 | SPRING.md; JAVA.md; ADR-0011 |
| BORD-REQ-054 | REQ-ORD-009–014, 019–020, 029–041 | BEB-REQ-012, 021–031, 041–046, 049 | DATABASE.md; POSTGRES.md |
| BORD-REQ-055 | REQ-ORD-026–033, 038, 045–051 | BEB-REQ-013–020, 026, 032–051 | API.md; EVENTS.md; SECURITY-STANDARDS.md |
| BORD-REQ-056 | REQ-ORD-001–052 | BEB-REQ-001–056 | BEB; ADR-0001; ADR-0011 |

## 5. Order Domain Coverage Matrix

| Order Domain Requirement | BORD specialization |
| --- | --- |
| REQ-ORD-001 | BORD-REQ-001 |
| REQ-ORD-002 | BORD-REQ-002 |
| REQ-ORD-003 | BORD-REQ-003 |
| REQ-ORD-004 | BORD-REQ-004 |
| REQ-ORD-005 | BORD-REQ-005 |
| REQ-ORD-006 | BORD-REQ-006 |
| REQ-ORD-007 | BORD-REQ-007 |
| REQ-ORD-008 | BORD-REQ-008 |
| REQ-ORD-009 | BORD-REQ-009 |
| REQ-ORD-010 | BORD-REQ-010 |
| REQ-ORD-011 | BORD-REQ-011 |
| REQ-ORD-012 | BORD-REQ-012 |
| REQ-ORD-013 | BORD-REQ-013 |
| REQ-ORD-014 | BORD-REQ-014 |
| REQ-ORD-015 | BORD-REQ-015 |
| REQ-ORD-016 | BORD-REQ-016 |
| REQ-ORD-017 | BORD-REQ-017 |
| REQ-ORD-018 | BORD-REQ-018 |
| REQ-ORD-019 | BORD-REQ-019 |
| REQ-ORD-020 | BORD-REQ-020 |
| REQ-ORD-021 | BORD-REQ-021 |
| REQ-ORD-022 | BORD-REQ-022 |
| REQ-ORD-023 | BORD-REQ-023 |
| REQ-ORD-024 | BORD-REQ-024 |
| REQ-ORD-025 | BORD-REQ-025 |
| REQ-ORD-026 | BORD-REQ-026 |
| REQ-ORD-027 | BORD-REQ-027 |
| REQ-ORD-028 | BORD-REQ-028 |
| REQ-ORD-029 | BORD-REQ-029 |
| REQ-ORD-030 | BORD-REQ-030 |
| REQ-ORD-031 | BORD-REQ-031 |
| REQ-ORD-032 | BORD-REQ-032 |
| REQ-ORD-033 | BORD-REQ-033 |
| REQ-ORD-034 | BORD-REQ-034 |
| REQ-ORD-035 | BORD-REQ-035 |
| REQ-ORD-036 | BORD-REQ-036 |
| REQ-ORD-037 | BORD-REQ-037 |
| REQ-ORD-038 | BORD-REQ-038 |
| REQ-ORD-039 | BORD-REQ-039 |
| REQ-ORD-040 | BORD-REQ-040 |
| REQ-ORD-041 | BORD-REQ-041 |
| REQ-ORD-042 | BORD-REQ-042 |
| REQ-ORD-043 | BORD-REQ-043 |
| REQ-ORD-044 | BORD-REQ-044 |
| REQ-ORD-045 | BORD-REQ-045 |
| REQ-ORD-046 | BORD-REQ-046 |
| REQ-ORD-047 | BORD-REQ-047 |
| REQ-ORD-048 | BORD-REQ-048 |
| REQ-ORD-049 | BORD-REQ-049 |
| REQ-ORD-050 | BORD-REQ-050 |
| REQ-ORD-051 | BORD-REQ-051 |
| REQ-ORD-052 | BORD-REQ-052 |

## 6. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BORD application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, non-authority, and specialization. BORD-REQ-001–003, 056. |
| BEB-REQ-005–007 | Applicable | Modular-monolith, hexagonal, and layer boundaries. BORD-REQ-053. |
| BEB-REQ-008 | Applicable | Intentional Order Module and dependency Contracts. BORD-REQ-007–008, 026, 030, 032, 045, 053. |
| BEB-REQ-009–010 | Applicable | Explicit Use Cases and truthful command/query outcomes. BORD-REQ-007–014, 034–038, 053. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and owned transaction boundaries. BORD-REQ-037, 042, 048, 050, 054. |
| BEB-REQ-013–018 | Applicable | Versioning, DTO separation, validation, bounds, errors, description, and evolution. BORD-REQ-006, 008, 042–047, 055. |
| BEB-REQ-019–020 | Applicable | Trusted Authentication evidence, Authorization, and concealment. BORD-REQ-006, 021, 042–043, 048. |
| BEB-REQ-021–024 | Applicable | Order ownership, persistence boundaries, integrity, and historical truth. BORD-REQ-002–039, 051, 054. |
| BEB-REQ-025–026 | Applicable | Local atomicity and separation of external calls. BORD-REQ-009–014, 029–041, 054–055. |
| BEB-REQ-027–031 | Applicable | Concurrency, durable uncertainty, idempotency, replay, and duplicate safety. BORD-REQ-012–014, 029–041, 054. |
| BEB-REQ-032–034 | Conditionally applicable | Future Payment/Shipping provider intersections require Ports, resilience, uncertainty, and reconciliation; none is selected. BORD-REQ-026–033, 038, 041, 055. |
| BEB-REQ-035–036 | Conditionally applicable | Future governed callbacks require authenticity, integrity, replay safety, and recovery; no callback is selected. BORD-REQ-026–029, 045–046, 055. |
| BEB-REQ-037–039 | Conditionally applicable | Order events exist only when separately governed and inherit event separation, envelope, and delivery safeguards. BORD-REQ-046, 051, 055. |
| BEB-REQ-040 | Applicable | BORD cannot introduce external messaging independently. BORD-REQ-046, 055–056. |
| BEB-REQ-041–042 | Applicable | Safe failure classification, recovery, and reconciliation. BORD-REQ-008–014, 018, 029–041, 049, 054–055. |
| BEB-REQ-043–044 | Applicable | Order data, Contracts, evidence, configuration, and Secrets require protection. BORD-REQ-042–43, 45, 48, 50–51, 055. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records and safe observability. BORD-REQ-034, 037, 041, 048–050, 054. |
| BEB-REQ-047–048 | Applicable | Configuration and reachable feature states preserve safeguards without selecting mechanisms. BORD-REQ-055–056. |
| BEB-REQ-049–051 | Applicable | Migration, compatibility, bounded work, and failure containment. BORD-REQ-020, 045–055. |
| BEB-REQ-052–055 | Applicable | Unit, application, Adapter, integration, Contract, architecture, operational, and traceable verification. BORD-REQ-052, 056. |
| BEB-REQ-056 | Applicable | Policy, mechanism, provider, and roadmap neutrality. BORD-REQ-001, 003–005, 010–14, 20–25, 35–39, 44–47, 55–056. |

## 7. Dependency and Authority Matrix

| Capability | BORD use | Authority retained outside BORD |
| --- | --- | --- |
| BEB | Inherited shared backend obligations | Shared backend governance |
| BCHK | Governed Checkout-to-Order request and orchestration evidence | Checkout truth, progression, submission, and recovery |
| BIDN | Trusted Principal and Authentication evidence | Identity, credentials, Sessions, tokens, Authentication |
| BCUS | Customer, Visitor, Account, Address, Consent, Preference, and ownership evidence | Customer and Account truth |
| BPRD | Historical Product/Product Variant representation input | Current Product identity, lifecycle, content, media, sellability |
| BPRC | Confirmed commercial evidence | Current Price, Discount, Promotion, Voucher, Tax, Money, Currency |
| BINV | Inventory and Stock Reservation evidence/effect requests | Stock, availability, reservation lifecycle, Inventory truth |
| BCART | Correlated purchase-intent evidence | Cart identity, contents, mutation, lifecycle, totals |
| BCAT / BSRCH | Optional context only | Category and Search truth; no Order authority |
| Payment Domain | Authoritative Payment evidence | All Payment and provider truth; backend identity unresolved |
| Shipping and Fulfilment Domain | Future bounded fulfilment handoff | Shipment, Dispatch, tracking, delivery, fulfilment; backend identity unresolved |
| Return Domain | Stable historical Order evidence | Return eligibility, authorization, lifecycle, disposition, reverse logistics |
| Administration | Protected support/recovery consumer | Administrative coordination; no Role/Permission matrix |
| Notifications / Reporting | Downstream derived consumers | Delivery and analytical truth; non-authoritative representations |
| CMS | Conditional policy/content evidence only | CMS truth; independently eligible backend roadmap candidate |

Payment and Shipping/Fulfilment have no selected backend specification identity. BORD defines only semantic authority boundaries and MUST NOT invent their future titles, paths, scope codes, APIs, DTOs, event schemas, persistence, providers, implementations, or roadmap positions.

## 8. Contract, Data, Transaction, and Event Boundaries

BORD Contracts describe semantic inputs, outcomes, provenance, correlation, authority, historical evidence, failure, uncertainty, and compatibility—not concrete routes, methods, statuses, DTOs, provider payloads, event schemas, or physical persistence. BORD-owned persistence remains bounded to Order and cannot become another Domain's source of truth. Local transactions cannot prove remote success. Conditional events and callbacks remain subject to governing ownership, authentication, integrity, replay, ordering, compatibility, privacy, and reconciliation requirements.

## 9. Open Product Decisions

The following **21 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and Accepted ADR-0011 remain unresolved:

| Item | Open Product Decision | Preserved BORD boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Customer/Visitor association policy is selected. |
| 5 | Customer email-verification requirements. | No verification prerequisite or mechanism is selected. |
| 6 | Initial payment methods and provider. | No Payment method, provider, or provider Contract is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, eligibility, or fee policy is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or commercial treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, invoice, or document rule is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, or timing policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No eligibility, exchange, or Refund policy is selected. |
| 12 | Stock Reservation duration. | No duration, expiry, or timing value is selected. |
| 13 | Back-order and pre-order support. | No unavailable-item creation or fulfilment policy is selected. |
| 14 | Voucher and promotion stacking policy. | No stacking, precedence, or combination policy is selected. |
| 18 | Customer-support channels and service expectations. | No channel, hours, expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No Consent or Preference model is selected or inferred. |
| 20 | Initial analytics provider and event taxonomy. | No provider, taxonomy, or measurement mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, metric, export, schedule, or format is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, process, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, or accounting policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, blocking, review, provider, or intervention policy is selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No Customer-data workflow or historical Order effect is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, or treatment is selected. |

## 10. Open Architecture Decisions

The following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and Accepted ADR-0011 remain unresolved:

| Item | Open Architecture Decision | Preserved BORD boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment provider or provider Contract is selected. |
| 6 | Shipping provider selection. | No Shipping provider or provider Contract is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery Contract is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No messaging service, broker, topic, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, mechanism, objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Order persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, rollout system, or lifecycle is selected. |

## 11. Explicit Non-Decisions and Roadmap Boundary

BORD selects no Order identifier/number format; complete transition graph; cancellation, Return, Refund, Payment, Shipping, Inventory, Pricing, tax, invoice, Credit Note, fraud, credit, Consent, support, escalation, notification, or reporting policy; provider; route; method; status; DTO; payload; database schema, table, column, index, ORM mapping, isolation level, event name/schema/topic/queue/broker/transport, cache/Redis use, workflow engine, hosting, infrastructure, deployment topology, algorithm, threshold, retry count, timeout, TTL, retention period, rate limit, performance target, SLA/SLO, or recovery objective.

CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered. Shipping/Fulfilment, Return, Administration, Notifications, and Reporting remain separate and unresolved. No backend identity, title, path, scope, decomposition, or ordering after BORD is established.

## 12. Risks and Controls

| Risk | Control direction |
| --- | --- |
| Duplicate Order creation | Require governed correlation, idempotency, evidence-aware retry, and duplicate detection. |
| False creation success | Allow only durable Order-owned evidence to establish existence. |
| Checkout/Order or Payment/Order mismatch | Preserve independent outcomes, uncertainty, and reconciliation. |
| Inventory effect without Order | Reconcile through BINV evidence before repetition or compensation. |
| Shipping activity lacks valid Order context | Require governed Order/Item association and safe rejection. |
| Snapshot or historical evidence drifts | Preserve immutable confirmation-time evidence and append later facts. |
| Unauthorized or cross-Customer access | Enforce contextual Authorization, isolation, concealment, and safe denial. |
| Unsafe transition, retry, or replay | Validate current truth and classify prior effects before mutation. |
| Timeout or partial completion becomes false truth | Retain known, unknown, completed, and failed effects explicitly. |
| Derived representations become authority | Keep notifications, reports, analytics, exports, and Projections non-authoritative. |
| Open policy becomes implementation | Preserve all listed decisions and explicit non-decisions. |
| Post-BORD order is implied | End the canonical sequence at BORD and keep all later positions unresolved. |

## 13. Required Governance Reviews

Before approval, BORD requires review by Architecture; Order; Checkout; Payment; Shipping and Fulfilment; Inventory; Product and Product Catalogue; Pricing; Customer; Identity; Cart; Return; Notifications; Reporting; Administration; CMS; Security; Testing; and Documentation ownership where materially intersecting.

Review MUST confirm complete Order Domain and BEB coverage, one-to-one Requirement/Acceptance-Criterion/traceability accounting, Order-only authority, bounded BCHK consumption, external Payment and Shipping/Fulfilment authority, preservation of all Open Decisions, security/privacy/integrity/failure/recovery/observability/compatibility/testing completeness, mechanism neutrality, and absence of a post-BORD roadmap position. This Draft does not claim those reviews are completed.

## 14. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0011-post-checkout-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/backend/search/search-backend.md`
- `specifications/backend/checkout/checkout-backend.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/cms/cms-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-20 | Draft | Established the initial Order-only Backend Specification from Approved Order Domain authority, complete BEB inheritance, governed BCHK handoff, bounded external Payment/Shipping authority, and Accepted ADR-0011. |

## 16. Final Validation

Before approval review, reviewers MUST validate:

1. metadata is `0.1.0 Draft`, owner `Backend`, `authoritative: false`, and scope `BORD`;
2. all 56 BORD Requirements are unique and contiguous;
3. every BORD Requirement has exactly one corresponding Acceptance Criterion and traceability row;
4. all 52 Approved Order Domain Requirements are accounted for;
5. all 56 BEB Requirements are accounted for without gaps;
6. BORD remains Order-only and BCHK, Payment, Shipping/Fulfilment, Inventory, Product, Pricing, Customer, Identity, Cart, Return, Administration, Notifications, Reporting, CMS, Category, and Search authority remains external where applicable;
7. all 21 Product and 11 Architecture Decisions remain exact and unresolved;
8. duplicate/replay/concurrency safety, unknown outcomes, partial completion, recovery, and reconciliation remain explicit;
9. no provider, concrete API/DTO, persistence schema, event schema, cache/message technology, infrastructure, policy, or arbitrary numerical value is selected;
10. the sequence ends at BORD and no later position is established;
11. all required governance reviews are identified but not claimed completed;
12. Revision History contains only the Draft lifecycle entry; and
13. the final change creates only `specifications/backend/order/order-backend.md`, passes whitespace validation, and remains unstaged, uncommitted, and unpushed.
