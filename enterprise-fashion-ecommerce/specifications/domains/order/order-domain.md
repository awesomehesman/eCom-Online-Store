---
title: Order Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-04
authoritative: false
---

# Order Domain

## 1. Purpose

This Order Domain Specification defines implementation-neutral authority for durable commercial Order truth received through a governed Checkout handoff, including Order identity, creation, Order Items, historical snapshots, lifecycle, history, and Order-owned recovery and reconciliation outcomes.

This document uses requirement scope code `ORD`. Its Approved Requirements are normative only within the Order Domain scope and are not repository-wide authority. They remain subordinate to higher-authority governing sources under the repository Decision Hierarchy, preserve Approved upstream Domain authority within each upstream scope, and do not resolve Open Product Decisions. Historical evidence retained by Order does not make Order the live authority for the upstream facts represented by that evidence.

## 2. Scope and Authority

### REQ-ORD-001 — Lifecycle, Authority, and Scope

The Order Domain MUST govern only Order-owned durable commercial truth, preserve governing-source precedence and Approved upstream Domain authority, use scope code `ORD`, and MUST treat this Approved Specification as normative only within the Order Domain scope and not as repository-wide authority.

### REQ-ORD-002 — Order-Owned Commercial Truth

Order MUST own Order identity, Order Number, creation truth, durable commercial records, Order Items, Order Snapshots, lifecycle, status history, controlled transitions, cancellation coordination, and Order-owned failure, recovery, and reconciliation history. Retaining external evidence MUST NOT transfer its source authority to Order.

### REQ-ORD-003 — Cross-Domain Non-Authority

Order MUST NOT own or redefine live Product, Product Variant, Category, catalogue or merchandising truth, current Customer profile or Address truth, Authentication, general Authorization policy, Role, Permission, Claims, Scope, live Pricing or Pricing policy, Payment, Payment Attempt, Payment Authorization, Capture, Settlement, Void, Refund execution, Chargeback, Payment Transaction or provider evidence, Inventory, Stock, Available-to-Sell, Stock Reservation lifecycle, Stock Adjustment, Stock Movement, Overselling, Shipment, Carrier, Dispatch, Tracking Number, delivery truth, Fulfilment execution, Return eligibility or lifecycle, Notification delivery, reporting, analytics, exports, Projections, Tax, invoice, Credit Note, Promotion, fraud, gift-card, store-credit, or promotional-credit policy.

## 3. Domain Context

Order is the durable commercial-record Domain downstream of Checkout and upstream of applicable fulfilment, Return, Notification, Administration, and Reporting capabilities. It establishes whether an Order exists and preserves the historical facts applicable to that Order while consuming governed evidence from owning Domains.

## 4. Canonical Terminology

Canonical terms follow `GLOSSARY.md`. Order, Order Number, Order Item, Order Snapshot, Cancellation, Fulfilment Group, Pending Payment, Confirmed, Processing, Partially Fulfilled, Fulfilled, Cancelled, Completed, Archived, Checkout, Cart, Cart Item, Product, Product Variant, Customer, Visitor, Address, Shipping Address, Payment, Payment Attempt, Payment Authorization, Payment Transaction, Refund, Inventory, Stock, Stock Reservation, Shipment, Fulfilment, Return, Contract, Domain Event, Integration Event, Projection, Audit Record, Sensitive Data, Authorization, Principal, and Risk retain their canonical meanings.

## 5. Order Identity and Number

### REQ-ORD-004 — Stable Order Identity

Each Order MUST have stable, unique identity that remains correlated to its creation evidence and historical record. Identity MUST NOT be inferred from Checkout, Payment, Cart, Customer, Shipment, browser, UI, analytics, or Projection state, and no identifier format, UUID strategy, sequence, prefix, generation algorithm, or persistence mechanism is established.

### REQ-ORD-005 — Order Number

Each Order MUST have a unique customer-facing Order Number distinct from its internal identity, stable for the Order's governed history, and safe to use as a reference without making it an Authorization credential. No format, allocation, sequence, prefix, or generation mechanism is selected.

### REQ-ORD-006 — Identifier Exposure and Resource Isolation

Order identity and Order Number use MUST preserve server-side Authorization, Customer ownership, Resource isolation, enumeration resistance, correlation, and safe error behavior. Knowledge or guessability of either identifier MUST NOT grant access or prove Order existence to an unauthorized Principal.

## 6. Checkout-to-Order Handoff

### REQ-ORD-007 — Governed Checkout Handoff

Order creation MUST originate only from a governed Checkout outcome and MUST preserve applicable Cart, Customer or Visitor, Address, Product and Product Variant, commercial, Payment, Inventory, Stock Reservation, Shipping or delivery, correlation, and Authorization context. Checkout remains authoritative for orchestration and MUST NOT establish Order identity, existence, creation success, lifecycle, or history.

### REQ-ORD-008 — Independent Handoff Validation

Order MUST independently validate the applicable handoff's authority, identity, correlation, completeness, freshness, consistency, and permitted actor context before establishing Order truth. Invalid, incomplete, stale, mismatched, unauthorized, unavailable, duplicate, or uncertain context MUST produce a distinguishable safe outcome.

## 7. Order Creation

### REQ-ORD-009 — Order Creation Truth

Only Order MAY establish that an Order exists and creation succeeded. Receipt, validation, or acceptance of a creation request, Checkout submission or success, Payment success, browser or UI state, client callback, external expectation, or downstream effect MUST NOT independently prove Order creation.

### REQ-ORD-010 — Creation Outcome Separation

Order creation request receipt, acceptance for processing, successful creation, confirmation, rejection, duplicate outcome, failure, and uncertainty MUST remain distinguishable where materially different. Creation MUST be governed, correlated, duplicate-safe, and based on applicable authoritative evidence without prescribing a transaction sequence or orchestration mechanism.

### REQ-ORD-011 — Creation and Confirmation Separation

Order creation MUST remain distinct from the `Confirmed` lifecycle meaning. An Order MAY exist in `Pending Payment` where governed, but no universal Payment sequence is established; transition to `Confirmed` MUST require the governed validation and accepted payment arrangement applicable to that Order.

### REQ-ORD-012 — Duplicate Order Prevention

Duplicate, retried, replayed, stale, concurrent, or reordered Checkout handoffs and creation requests MUST NOT create more than one Order for the same governed purchase effect. Applicable Contracts MUST preserve Idempotency Key semantics without selecting key format, storage, lock, isolation level, table constraint, cache, queue, or retry count.

### REQ-ORD-013 — Safe Retry, Replay, and Concurrency

Before repeating creation or another potentially irreversible Order operation, Order MUST determine from governed evidence whether the prior effect is known, unknown, conflicting, or safely repeatable. Replay, concurrent activity, reordered outcomes, and stale context MUST NOT overwrite accepted truth, bypass a valid transition, or multiply Order or external effects.

### REQ-ORD-014 — Timeout and Partial Completion

Timeout, dependency unavailability, interrupted processing, delayed evidence, or partial completion MUST remain distinguishable from confirmed creation, definitive failure, and cancellation. Known Order and external effects, unknown effects, and failure provenance MUST be preserved without assuming interruption stopped processing.

## 8. Order Items

### REQ-ORD-015 — Order Item Ownership

Order MUST own each Order Item as historical purchased-item truth associated unambiguously with its Order. An Order Item MUST remain distinct from Cart Item, live Product or Product Variant, Inventory, Stock Reservation, Shipment, and Fulfilment records, and no field set or persistence schema is established.

### REQ-ORD-016 — Historical Product Representation

Each applicable Order Item MUST preserve enough governed Product and Product Variant identity and representation to keep the purchase historically interpretable. Later rename, attribute, publication, classification, merchandising, retirement, or deletion changes MUST NOT silently rewrite the Order Item or make Order authoritative for current catalogue truth.

## 9. Order Snapshot

### REQ-ORD-017 — Order Snapshot Authority

Order MUST own the Order Snapshot as historical evidence of the governed commercial context applicable at confirmation. The snapshot MAY include supported Product and Product Variant representation, quantities, Price, Money, Currency, Discount, Promotion, Voucher, Tax, Shipping charge, totals, Customer or Visitor association, Address or Shipping Address, delivery context, and applicable Payment, Inventory, or Stock Reservation references without becoming live authority for those source Domains.

### REQ-ORD-018 — Snapshot Completeness and Interpretability

The Order Snapshot MUST preserve sufficient governed context, association, provenance, and meaning to explain the confirmed purchase to authorized Customer, support, financial, operational, audit, and reconciliation contexts where applicable. Missing or inconsistent required snapshot evidence MUST prevent unsupported confirmation or remain explicitly unresolved.

### REQ-ORD-019 — Snapshot Immutability

Confirmed Order Snapshot values MUST resist silent mutation, replacement, or retroactive recalculation. Later Product, Product Variant, Category, Customer, Address, Pricing, Promotion, Voucher, Tax, Shipping Rate, Payment, Inventory, or delivery changes MUST NOT rewrite the historical values that applied at confirmation.

### REQ-ORD-020 — Historical Change and Correction Boundary

Legitimate later lifecycle transitions, cancellation, Return, Refund, correction, privacy handling, retention, anonymization, or deletion obligations MUST preserve the original historical commercial record and append or associate governed evidence rather than fabricate prior truth. This Specification establishes no correction, retention, deletion, anonymization, or legal policy.

## 10. Customer, Visitor, and Address Boundary

### REQ-ORD-021 — Customer or Visitor Association

Order MUST preserve the governed Customer or Visitor association applicable to creation without deciding guest Checkout, mandatory Account, or email-verification policy. Current Customer, Account, Session, Authentication, profile, Preference, or Consent changes MUST NOT establish, authorize, or rewrite Order truth.

### REQ-ORD-022 — Address Snapshot

Order MUST preserve applicable historical Address or Shipping Address context required for the confirmed commercial record, distinct from current Customer Address and Shipment destination truth. Later Address change or removal MUST NOT rewrite the Order Snapshot, and no Address schema, validation policy, retention rule, or correction workflow is invented.

## 11. Pricing, Money, and Currency Boundary

### REQ-ORD-023 — Historical Commercial Values

Order MUST retain applicable confirmed Price, Money, Currency, Discount, Promotion, Voucher, Tax, Shipping charge, and total evidence supplied through governed authority. Order MUST NOT independently calculate or recalculate Pricing, eligibility, Tax, Discount, Promotion, Voucher, Shipping Rate, or totals.

### REQ-ORD-024 — Money and Currency Integrity

Order monetary history MUST use explicit Money and Currency with precision-safe, non-floating-point semantics, preserve applicable Version 1 ZAR governance, and reject silent Currency mixing or implicit conversion. No rounding, foreign-exchange, Minor Unit, storage type, scale, or precision rule is established.

### REQ-ORD-025 — Commercial Policy Non-Authority

Order MUST NOT invent Tax calculation or display, invoice, numbering, Credit Note, accounting, Promotion stacking, Voucher restoration, free-delivery threshold, gift-card, store-credit, promotional-credit, or legal policy. Applicable historical evidence MUST be preserved without converting it into policy authority.

## 12. Payment Boundary

### REQ-ORD-026 — Payment Evidence Consumption

Order MAY consume and historically correlate trusted Payment-owned evidence, including applicable Payment, Payment Attempt, Payment Authorization, Capture, Settlement, Void, Refund, Chargeback, and Payment Transaction references or outcomes. Payment retains authority for those concepts and provider evidence, and Order status MUST NOT fabricate Payment truth.

### REQ-ORD-027 — Payment and Order Independence

Payment success MUST NOT independently prove Order existence, and Order existence MUST NOT independently prove Payment success. A Payment Redirect, browser result, client callback or report, UI state, local state, unvalidated provider information, or expectation MUST NOT establish Payment evidence or Order creation.

### REQ-ORD-028 — Payment Evolution and Order History

Pending, authorized, captured, failed, cancelled, settled, refunded, disputed, unknown, or later Payment evolution MUST remain under Payment authority and distinguishable from Order lifecycle, fulfilment, cancellation, Return, and Refund concerns. Order MAY preserve governed historical references and financial-status history without silently rewriting the confirmed commercial snapshot.

### REQ-ORD-029 — Payment and Order Mismatch

Payment success with failed, delayed, duplicate, unavailable, or uncertain Order creation, and Order existence with uncertain or failed Payment evidence, MUST remain explicit, correlated, recoverable, and reconcilable. Recovery MUST preserve both authorities and MUST NOT create a false Order, false Payment result, duplicate financial effect, or unsupported automatic reversal.

## 13. Inventory and Stock Reservation Boundary

### REQ-ORD-030 — Inventory Coordination

Order MAY retain Inventory and Stock Reservation references and request governed reservation consumption, release, finalization, or another Inventory effect where permitted. Inventory retains authority for Inventory, Stock, Available-to-Sell, Stock Reservation identity and lifecycle, expiry, release, consumption, Stock Adjustment, Stock Movement, and Overselling protection.

### REQ-ORD-031 — Inventory Evidence and Mismatch

Order state MUST NOT independently establish an Inventory fact. Reservation uncertainty, duplicate or replayed effects, creation failure after an Inventory effect, cancellation-related coordination, and Inventory disagreement MUST remain explicit, evidence-based, duplicate-safe, and reconcilable without selecting timing, duration, lock, allocation, or persistence mechanism.

## 14. Shipping and Fulfilment Boundary

### REQ-ORD-032 — Governed Fulfilment Handoff

Order MAY provide governed Order, Order Item, Fulfilment Group, Address, delivery, and permitted commercial context for downstream Fulfilment or Shipment work. Shipping retains authority for Shipping execution, Shipment identity and lifecycle, Carrier, Dispatch, Tracking Number, provider evidence, delivery, and Fulfilment execution.

### REQ-ORD-033 — Order and Shipping State Separation

Order lifecycle meanings `Processing`, `Partially Fulfilled`, and `Fulfilled` MUST use governed Fulfilment evidence where applicable but MUST NOT substitute for Shipment, Dispatch, tracking, delivery, or provider truth. Shipping disagreement, delay, duplicate effect, partial completion, and uncertainty MUST remain distinguishable and reconcilable.

## 15. Order Lifecycle

### REQ-ORD-034 — Lifecycle Authority and History

Order MUST own its lifecycle and preserve explicit, controlled, authorized, attributable, time-correlated, and historically traceable transitions with applicable causation and evidence. Invalid, stale, conflicting, duplicate, replayed, or unauthorized transitions MUST be rejected safely without rewriting prior history.

### REQ-ORD-035 — Canonical Lifecycle Vocabulary

Where applicable, Order lifecycle state MUST use only the canonical meanings `Pending Payment`, `Confirmed`, `Processing`, `Partially Fulfilled`, `Fulfilled`, `Cancelled`, `Completed`, and `Archived`. This Specification does not require every Order to traverse every state and does not establish a complete transition graph or universal ordering.

### REQ-ORD-036 — Cross-Domain Lifecycle Integrity

An Order lifecycle state MUST NOT independently establish or mutate Payment, Inventory, Stock Reservation, Shipment, delivery, Return, Refund, Notification, or Projection truth. Each transition dependent on external evidence MUST preserve the owning Domain, evidence state, uncertainty, and applicable reconciliation boundary.

## 16. Cancellation Boundary

### REQ-ORD-037 — Cancellation Coordination

Order MUST own cancellation coordination and the canonical `Cancelled` Order meaning while leaving cancellation eligibility and cutoff policy unresolved. Any cancellation request MUST use current Order context, trusted server-side Authorization, policy-dependent eligibility, duplicate safety, partial-completion awareness, explicit failure or uncertainty, and historical evidence.

### REQ-ORD-038 — Cancellation Cross-Domain Effects

Cancellation MAY request governed Payment, Inventory, Shipping, Fulfilment, Return, or Notification coordination where Approved policy permits, but MUST NOT imply confirmed Refund, Void, Stock release, Shipment stop, Return, or external effect. Unknown or conflicting effects MUST be verified or reconciled without inventing a window, cutoff, automatic action, or permission matrix.

## 17. Return and Refund Boundary

### REQ-ORD-039 — Return and Refund Non-Authority

Order MAY supply stable Order and Order Item history and retain applicable Return or Refund references and governed consequences, but MUST NOT define Return eligibility, Return lifecycle, exchange policy, Refund eligibility, amount, method, provider processing, or execution. Payment retains Refund execution authority, and no future Return Requirement is established.

## 18. Failure, Recovery, and Reconciliation

### REQ-ORD-040 — Failure Provenance

Order MUST distinguish applicable invalid Checkout handoff, duplicate creation, Product-context disagreement, Customer or Address issue, Pricing disagreement, Payment failure or uncertainty, Inventory or Stock Reservation failure or uncertainty, Shipping or Fulfilment disagreement, cancellation failure, Return or Refund external uncertainty, dependency timeout, partial completion, Authorization denial, and Order-owned lifecycle failure without collapsing them into fabricated generic truth.

### REQ-ORD-041 — Controlled Recovery and Reconciliation

Recovery from interrupted creation, duplicate request, Payment success without Order, Inventory effect without Order, delayed external evidence, reordered outcome, cancellation mismatch, partial completion, or unknown effect MUST preserve correlation, authoritative evidence, history, duplicate safety, required Authorization, observability, and a resolved or still-uncertain result. Order MUST NOT directly rewrite another Domain's authoritative record.

## 19. Security and Sensitive Data

### REQ-ORD-042 — Authorization and Tamper Resistance

Protected Order access and actions MUST require trusted server-side Authorization for the current Principal, Resource, action, and Domain state, including Customer ownership and Staff operations where applicable. UI visibility, route state, Role labels, client Permissions, Claims, Scope, supplied identifiers, Order Number, Authentication alone, or client-controlled state MUST NOT authorize behavior; replay and CSRF protection MUST apply where relevant without prescribing a mechanism.

### REQ-ORD-043 — Sensitive Data Protection

Order MUST minimize and protect applicable Customer or Visitor identity context, contact information, Address and Shipping Address snapshots, payment-adjacent evidence, support context, identifiers, and other Sensitive Data through purpose limitation, least privilege, Resource isolation, and safe handling across storage, transmission, logs, errors, Contracts, events, Projections, analytics, exports, Audit Records, and support surfaces. No retention, anonymization, correction, or deletion policy is selected.

## 20. Fraud Boundary

### REQ-ORD-044 — Fraud and Manual-Review Non-Authority

Order MAY consume a governed fraud, restriction, or manual-review outcome where applicable but MUST NOT invent a provider, score, threshold, engine, blocking policy, or review workflow. A fraud signal MUST NOT become Payment, Order, or commercial truth without governed authority, and missing or uncertain required outcomes MUST fail safely.

## 21. Contracts

### REQ-ORD-045 — Order Contract Boundary

Future Order Contracts MUST preserve ownership, identity, correlation, compatibility, explicit request and outcome semantics, failure and uncertainty, applicable Idempotency Key semantics, retry and replay safety, Authorization, Sensitive Data protection, and cross-Domain authority. No URL, route, HTTP method or status, GraphQL operation, JSON shape, DTO, class, controller, Angular component or service, database schema or table, persistence model, or provider payload is defined.

## 22. Events

### REQ-ORD-046 — Conditional Canonical Order Events

Where Architecture, an Approved Contract, reliability, or synchronization governance requires an Order event, only applicable canonical `OrderCreated`, `OrderConfirmed`, `OrderCancelled`, or `OrderFulfilled` meanings MAY be used. Each event MUST represent a completed fact, preserve Order authority, correlation, causation, compatibility, Sensitive Data protection, duplicate and replay safety, ordering uncertainty, and non-authoritative consumer Projection semantics without requiring every event or defining payload, schema, topic, queue, broker, delivery guarantee, choreography, or partitioning.

## 23. Accessibility

### REQ-ORD-047 — Accessible Order Outcomes

Applicable Customer and Staff Order confirmation, history, detail, lifecycle, pending, partial-fulfilment, cancellation, failure, denial, stale-state, recovery, and uncertainty surfaces MUST target WCAG 2.2 AA and provide applicable keyboard operation, focus management, labels, instructions, error identification, accessible status changes, assistive-technology compatibility, and truthful progress and outcomes. No certification, visual design, component, or Customer-facing wording is established.

## 24. Administration, Operations, and Representations

### REQ-ORD-048 — Administration Boundary

Administration MUST invoke protected Order-owned behavior through governed use cases and MUST NOT directly establish, bypass, or rewrite Order identity, creation, snapshot, lifecycle, cancellation, recovery, or reconciliation truth. Staff action MUST use trusted server-side Authorization, applicable least privilege, explicit outcomes, and proportional audit evidence without defining a Role, Permission, approval, support, or escalation matrix.

### REQ-ORD-049 — Performance and Observability

Order creation, transitions, cancellation, duplicate handling, timeout, partial completion, cross-Domain disagreement, recovery, and reconciliation MUST be bounded, observable, correlated, and attributable sufficiently to distinguish failure and uncertainty. No numerical latency, throughput, timeout, retry, capacity, device, network, SLA, SLO, or provider target is established.

### REQ-ORD-050 — Proportional Order Audit

Materially high-Risk or privileged creation anomalies, lifecycle transitions, cancellation, correction, financial mismatch, privacy-sensitive intervention, recovery, reconciliation, and administrative actions MUST produce attributable, time-correlated, tamper-resistant, proportional Audit Records. Routine Order reads and ordinary valid progression MUST NOT automatically be classified as high-Risk, and no approval chain, Role, Permission, or escalation SLA is defined.

### REQ-ORD-051 — Notifications, Reporting, Analytics, and Projections

Notifications, reporting, analytics, exports, dashboards, caches, search indexes, and Projections MUST derive from governed Order facts, identify authoritative source where applicable, protect Sensitive Data, tolerate applicable delay or staleness, and MUST NOT mutate Order or establish Payment, Inventory, Shipping, Return, or Refund truth. Their failure MUST NOT alter Order transactional truth, and no provider, taxonomy, channel, report, export format, or caching mechanism is selected.

## 25. Testing

### REQ-ORD-052 — Order Verification Coverage

Verification evidence MUST cover applicable positive and negative behavior for creation; Order identity and Order Number; Order Items and Order Snapshot; historical immutability; lifecycle and canonical states; cancellation; Checkout, Product, Customer, Visitor, Address, Pricing, Money, Currency, Payment, Inventory, Stock Reservation, Shipping, Fulfilment, Return, and Refund boundaries; duplicate, retry, replay, concurrency, stale context, timeout, partial completion, uncertainty, recovery, reconciliation, Authorization, Resource isolation, Sensitive Data, fraud, Tax, commercial documents, Contracts, events, accessibility, performance, observability, audit, notifications, reporting, and analytics. No test framework, test identifier scheme, tooling, or numerical coverage target is selected.

## 26. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-ORD-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; scope is `ORD`; Order-scoped normativity, governing precedence, and preserved Approved upstream Domain authority are identifiable. |
| REQ-ORD-002 | Every listed Order-owned concern is identifiable, and retained external evidence transfers no source authority. |
| REQ-ORD-003 | Order owns or redefines none of the listed external Domain truths, policies, lifecycles, records, or representations. |
| REQ-ORD-004 | Each Order has stable unique identity correlated to creation and history; none of the listed external states proves identity and no generation mechanism is selected. |
| REQ-ORD-005 | Each Order has a unique stable customer-facing Order Number distinct from internal identity and Authorization, with no format or generation choice. |
| REQ-ORD-006 | Identifier access enforces server Authorization, ownership, isolation, enumeration resistance, correlation, and safe errors; identifier knowledge grants no access or existence proof. |
| REQ-ORD-007 | Creation originates only from a governed Checkout outcome carrying applicable listed context while Checkout establishes no Order-owned truth. |
| REQ-ORD-008 | Every material handoff property is independently validated and each invalid, stale, mismatched, denied, unavailable, duplicate, or uncertain outcome fails safely and distinguishably. |
| REQ-ORD-009 | Only Order proves existence and successful creation; none of the listed request, Checkout, Payment, client, or expected states does so independently. |
| REQ-ORD-010 | Request, processing acceptance, creation, confirmation, rejection, duplication, failure, and uncertainty remain distinguishable; creation is governed and duplicate-safe without implementation design. |
| REQ-ORD-011 | Creation and `Confirmed` remain distinct; governed `Pending Payment` is possible without imposing a universal Payment sequence, and confirmation uses applicable validation and arrangement. |
| REQ-ORD-012 | Repeated, stale, concurrent, or reordered creation cannot produce more than one Order; applicable idempotency semantics exist without a prohibited mechanism choice. |
| REQ-ORD-013 | Potentially irreversible repetition is evidence-classified first; replay, concurrency, ordering, and staleness cannot overwrite, bypass, or multiply effects. |
| REQ-ORD-014 | Timeout, unavailability, interruption, delay, and partial completion remain distinct from creation, failure, and cancellation while known, unknown, and failed effects retain provenance. |
| REQ-ORD-015 | Every Order Item is unambiguously Order-owned and distinct from Cart, catalogue, Inventory, Shipping, and Fulfilment records without an invented schema. |
| REQ-ORD-016 | Historical Product and Product Variant meaning remains interpretable after every listed catalogue change without making Order live catalogue authority. |
| REQ-ORD-017 | The Order Snapshot contains only applicable governed historical categories and transfers no live source authority. |
| REQ-ORD-018 | Required snapshot context is sufficient and explainable to applicable authorized uses; missing or inconsistent required evidence prevents confirmation or remains unresolved. |
| REQ-ORD-019 | Every listed later upstream change leaves confirmed snapshot values unchanged. |
| REQ-ORD-020 | Later lifecycle, cancellation, Return, Refund, correction, privacy, retention, anonymization, or deletion handling preserves original history and appends governed evidence without inventing policy. |
| REQ-ORD-021 | Order preserves governed Customer or Visitor association without selecting guest, Account, or verification policy; later Customer state neither authorizes nor rewrites Order truth. |
| REQ-ORD-022 | Historical Address context remains distinct and unchanged by current Address or Shipment changes, with no schema or data-lifecycle policy invented. |
| REQ-ORD-023 | Order retains each applicable listed governed commercial value and performs no independent calculation or recalculation. |
| REQ-ORD-024 | Monetary history has explicit precision-safe Money and Currency, preserves ZAR governance, rejects mixing and conversion, and selects no numerical or storage rule. |
| REQ-ORD-025 | Order establishes none of the listed commercial, document, credit, accounting, or legal policies while preserving applicable evidence. |
| REQ-ORD-026 | Order consumes only trusted Payment-owned evidence, preserves Payment authority for every listed concept, and cannot fabricate Payment truth through Order status. |
| REQ-ORD-027 | Payment and Order independently prove their own truth; none of the listed client or unvalidated signals proves either. |
| REQ-ORD-028 | Payment evolution remains distinct and Payment-owned while Order preserves governed references and financial history without snapshot mutation. |
| REQ-ORD-029 | Each Payment/Order mismatch remains explicit, correlated, recoverable, reconcilable, authority-preserving, and free of false or duplicate effects. |
| REQ-ORD-030 | Permitted Inventory coordination and references preserve Inventory authority for every listed concept and select no Inventory behavior. |
| REQ-ORD-031 | Order state proves no Inventory fact; every listed mismatch or uncertain effect is explicit, duplicate-safe, and reconcilable without timing or mechanism choices. |
| REQ-ORD-032 | Downstream handoff carries only governed applicable context while Shipping retains every listed execution and lifecycle authority. |
| REQ-ORD-033 | Order fulfilment-related meanings use governed evidence but establish no Shipment or delivery truth; disagreement, delay, duplication, partial completion, and uncertainty remain explicit. |
| REQ-ORD-034 | Every lifecycle transition is controlled, authorized, attributable, historically traceable, evidence-backed, and safely rejects every listed invalid condition. |
| REQ-ORD-035 | Only the eight canonical Order lifecycle meanings are used; neither universal traversal, complete graph, nor universal ordering is established. |
| REQ-ORD-036 | No Order lifecycle state establishes or mutates any listed external truth; evidence-dependent transitions preserve authority and uncertainty. |
| REQ-ORD-037 | Cancellation uses Order-owned coordination, canonical meaning, current context, Authorization, external policy, duplicate safety, partial awareness, failure handling, and history without resolving eligibility. |
| REQ-ORD-038 | Cancellation implies none of the listed external effects; uncertainty is verified or reconciled and no window, cutoff, automation, or permission matrix is selected. |
| REQ-ORD-039 | Order supplies stable history and may retain references while defining none of the listed Return or Refund policies, lifecycle, amount, method, processing, or execution. |
| REQ-ORD-040 | Every listed failure or uncertainty retains distinct provenance and is not collapsed into fabricated truth. |
| REQ-ORD-041 | Every listed recovery scenario preserves correlation, authority, history, duplicate safety, Authorization, observability, and resolved or uncertain outcome without rewriting external truth. |
| REQ-ORD-042 | Every protected action uses trusted contextual server Authorization, isolation and safe identifier handling; no listed client signal authorizes, and applicable replay and CSRF protection remain mechanism-neutral. |
| REQ-ORD-043 | Every listed Sensitive Data category and surface is minimized, purpose-limited, least-privilege, isolated, and safely handled without selecting a data-lifecycle policy. |
| REQ-ORD-044 | Order consumes only governed fraud or review outcomes, establishes none of the prohibited choices, and treats no ungoverned signal as Order, Payment, or commercial truth. |
| REQ-ORD-045 | Every future Contract preserves the listed semantic, authority, safety, security, privacy, and compatibility properties without any prohibited interface or implementation design. |
| REQ-ORD-046 | Only applicable canonical Order event meanings are used for governed needs; events are completed facts and preserve every listed authority and safety property without requiring events or transport design. |
| REQ-ORD-047 | Every applicable Order surface provides WCAG 2.2 AA, keyboard, focus, labeling, instruction, error, status, assistive-technology, and truthful-outcome evidence without design invention. |
| REQ-ORD-048 | Every Staff action invokes governed Order behavior with trusted contextual Authorization, least privilege, explicit outcomes, and proportional evidence without bypass, direct truth mutation, or an invented access or escalation matrix. |
| REQ-ORD-049 | Every listed operation is bounded, observable, correlated, attributable, and distinguishes failure and uncertainty without any prohibited numerical or provider target. |
| REQ-ORD-050 | Every applicable materially high-Risk or privileged action creates proportional attributable, time-correlated, tamper-resistant Audit Records; routine reads and progression are not automatically high-Risk and no access process is invented. |
| REQ-ORD-051 | Every listed downstream representation derives from governed facts, identifies source where applicable, protects data, handles delay or staleness, mutates no authoritative truth, survives delivery failure, and selects no provider or format. |
| REQ-ORD-052 | Evidence covers every listed positive, negative, boundary, lifecycle, resilience, security, quality, and representation concern without selecting framework, identifiers, tooling, or a numerical target. |

## 27. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-ORD-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047–048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9, 19–21 | Order governance |
| REQ-ORD-002 | PRODUCT.md §16.4 | REQ-BUS-021–023 | REQ-CHK-030 | AGENTS.md §31.7; ARCHITECTURE.md §9 | Order |
| REQ-ORD-003 | PRODUCT.md §§16.1–16.12 | REQ-BUS-021–030, 044 | REQ-PRD-001–002; REQ-CUS-034; REQ-INV-024; REQ-PRC-019; REQ-PAY-027; REQ-SHP-013; REQ-CHK-030 | AGENTS.md §31; ARCHITECTURE.md §§9, 40 | All adjacent Domains |
| REQ-ORD-004 | PRODUCT.md §§14.6, 16.4 | REQ-BUS-021, 032 | REQ-CHK-030 | GLOSSARY.md §§5, 8; SECURITY-STANDARDS.md §12 | Customer, support |
| REQ-ORD-005 | PRODUCT.md §14.6 | REQ-BUS-021, 035 | REQ-CUS-024, 034 | GLOSSARY.md §8; ARCHITECTURE.md §18 | Customer, support |
| REQ-ORD-006 | PRODUCT.md §§12.3–12.4, 16.7 | REQ-BUS-008, 032–033, 039 | REQ-CUS-024, 034; REQ-CHK-034 | SECURITY-STANDARDS.md §§12, 35; API.md §43 | Customer, Administration |
| REQ-ORD-007 | PRODUCT.md §§14.4–14.6, 16.4 | REQ-BUS-021 | REQ-CHK-004, 030 | ARCHITECTURE.md §§9, 25.3 | Checkout, Order |
| REQ-ORD-008 | PRODUCT.md §§14.4–14.6, 16.4 | REQ-BUS-021–025, 032, 042 | REQ-CHK-021–023, 030 | API.md §§22–24, 43 | Checkout, Order |
| REQ-ORD-009 | PRODUCT.md §§14.6, 16.4 | REQ-BUS-021 | REQ-PAY-002, 019, 027; REQ-CHK-028, 030–031 | ARCHITECTURE.md §§9, 25.3 | Checkout, Payment |
| REQ-ORD-010 | PRODUCT.md §§5.4–5.6, 14.6 | REQ-BUS-021, 025, 042 | REQ-CHK-023–026, 030–031 | API.md §§22, 24, 58 | Customer, operations |
| REQ-ORD-011 | PRODUCT.md §§14.5–14.6, 16.3–16.4 | REQ-BUS-021, 024–025 | REQ-PAY-007, 019, 027; REQ-CHK-028–031 | GLOSSARY.md §41 | Payment, Order |
| REQ-ORD-012 | PRODUCT.md §§16.3–16.4, 17.2 | REQ-BUS-021, 026 | REQ-PAY-022–023; REQ-CHK-024–025 | ARCHITECTURE.md §§4.2, 20.2, 26.4 | Checkout, Payment |
| REQ-ORD-013 | PRODUCT.md §§5.5–5.6, 17.2 | REQ-BUS-026, 036, 042, 045 | REQ-PAY-022–025; REQ-CHK-024–026 | DECISIONS.md §45; API.md §58 | Operations |
| REQ-ORD-014 | PRODUCT.md §§5.5–5.6, 17.2 | REQ-BUS-025, 035–036, 042, 045 | REQ-PAY-020, 024–025; REQ-CHK-026, 031 | ARCHITECTURE.md §§20, 26 | Operations, support |
| REQ-ORD-015 | PRODUCT.md §16.4 | REQ-BUS-021–023 | REQ-PRD-006, 030; REQ-CART-002; REQ-CHK-030 | GLOSSARY.md §§5, 8; DATABASE.md §52 | Customer, Return, Shipping |
| REQ-ORD-016 | PRODUCT.md §§16.4, 17.4 | REQ-BUS-022–023 | REQ-PRD-006, 030–031; REQ-CAT-021–022 | ARCHITECTURE.md §40.5 | Customer, support |
| REQ-ORD-017 | PRODUCT.md §§16.1, 16.4, 16.7, 16.10 | REQ-BUS-021–023 | REQ-PRD-030; REQ-CUS-017, 025; REQ-PRC-019; REQ-SHP-010, 013 | GLOSSARY.md §8; ARCHITECTURE.md §§15, 40.5 | Customer, Finance |
| REQ-ORD-018 | PRODUCT.md §§14.6, 16.4 | REQ-BUS-021–023, 035 | REQ-PRD-030; REQ-CUS-025; REQ-PRC-019 | ARCHITECTURE.md §§18, 40.5 | Customer, support, audit |
| REQ-ORD-019 | PRODUCT.md §§16.1–16.5, 16.7 | REQ-BUS-022–023 | REQ-PRD-006, 030; REQ-CUS-017, 025; REQ-PRC-019; REQ-SHP-010 | ARCHITECTURE.md §40.5; DATABASE.md §52 | All historical consumers |
| REQ-ORD-020 | PRODUCT.md §§16.4, 16.7, 16.11, 20 | REQ-BUS-022–023, 030, 039, 041 | REQ-CUS-025, 028; REQ-PRC-021; REQ-PAY-034–035; REQ-SHP-031–032 | SECURITY-STANDARDS.md §35; DATABASE.md §52 | Return, Payment, privacy |
| REQ-ORD-021 | PRODUCT.md §§12.3–12.4, 14.4, 16.7, 24 | REQ-BUS-008, 021–023, 040 | REQ-CUS-015–017, 033–034; REQ-CHK-010–012 | SECURITY-STANDARDS.md §§10–12 | Customer, Identity |
| REQ-ORD-022 | PRODUCT.md §§14.4, 16.4, 16.7 | REQ-BUS-008, 021–023 | REQ-CUS-015–017, 025, 036; REQ-SHP-009–010; REQ-CHK-011 | ARCHITECTURE.md §40.5; SECURITY-STANDARDS.md §35 | Customer, Shipping |
| REQ-ORD-023 | PRODUCT.md §§14.4, 16.1, 16.4 | REQ-BUS-012–015, 021–023 | REQ-PRC-002, 006–014, 018–019; REQ-CHK-013–015 | ARCHITECTURE.md §§15, 40.5 | Pricing, Finance |
| REQ-ORD-024 | PRODUCT.md §§5.3, 9.4, 16.1 | REQ-BUS-014–015, 021–022 | REQ-PRC-006, 019; REQ-CHK-015 | GLOSSARY.md §§5, 23; DATABASE.md §42 | Pricing, Payment |
| REQ-ORD-025 | PRODUCT.md §§16.1, 16.6, 16.10–16.12, 24 | REQ-BUS-012, 014–016, 030, 041, 048, 054 | REQ-PRC-012–014, 019, 021; REQ-PAY-035, 044; REQ-SHP-042; REQ-CHK-037 | GLOSSARY.md §§23–24 | Finance, Return |
| REQ-ORD-026 | PRODUCT.md §§14.5–14.6, 16.3–16.4 | REQ-BUS-021, 024–025, 028 | REQ-PAY-002, 004, 013, 019, 027; REQ-CHK-027–029 | GLOSSARY.md §9; ARCHITECTURE.md §20.2 | Payment, Finance |
| REQ-ORD-027 | PRODUCT.md §§5.4, 14.5–14.6, 16.3–16.4 | REQ-BUS-021, 024–025 | REQ-PAY-002, 019, 027; REQ-CHK-028–031 | ARCHITECTURE.md §§20.2, 25.3 | Checkout, Payment |
| REQ-ORD-028 | PRODUCT.md §§16.3–16.4, 17.4 | REQ-BUS-023–025, 028, 035 | REQ-PAY-013–019, 027, 034–036 | GLOSSARY.md §§9, 41; ARCHITECTURE.md §40.5 | Payment, Finance, Return |
| REQ-ORD-029 | PRODUCT.md §§5.5–5.6, 14.5–14.6, 17.2 | REQ-BUS-021–026, 035–036, 042 | REQ-PAY-019, 022–029; REQ-CHK-028–033 | ARCHITECTURE.md §§20.2, 20.5, 25.3 | Payment, operations |
| REQ-ORD-030 | PRODUCT.md §§16.2, 16.4, 17 | REQ-BUS-019–023 | REQ-INV-010–015, 023–026; REQ-CHK-016–018 | ARCHITECTURE.md §§20.3, 25.3 | Inventory, Fulfilment |
| REQ-ORD-031 | PRODUCT.md §§5.5–5.6, 16.2, 17.2 | REQ-BUS-019–023, 026, 035–036, 042 | REQ-INV-013–015, 024–026, 030; REQ-CHK-017–018, 025–026 | ARCHITECTURE.md §§20.3, 20.5 | Inventory, operations |
| REQ-ORD-032 | PRODUCT.md §§14.6–14.7, 16.4–16.5 | REQ-BUS-021–023, 029 | REQ-SHP-013–018; REQ-CHK-019–020, 030 | ARCHITECTURE.md §§9, 25.4 | Shipping, Fulfilment |
| REQ-ORD-033 | PRODUCT.md §§14.7, 16.4–16.5 | REQ-BUS-023, 029, 042 | REQ-SHP-017–020, 026; REQ-INV-026 | GLOSSARY.md §§8, 11, 41 | Shipping, Customer |
| REQ-ORD-034 | PRODUCT.md §§15.3, 16.4 | REQ-BUS-023, 032–036 | REQ-CUS-034; REQ-SHP-013; REQ-CHK-030 | GLOSSARY.md §41; SECURITY-STANDARDS.md §12 | Administration, Customer |
| REQ-ORD-035 | PRODUCT.md §§16.4, 17.4 | REQ-BUS-023 | REQ-CUS-034; REQ-CHK-030 | GLOSSARY.md §41 | Customer, Shipping |
| REQ-ORD-036 | PRODUCT.md §§16.3–16.5, 16.11 | REQ-BUS-023–030 | REQ-INV-024–026; REQ-PAY-027, 034–035; REQ-SHP-013–019, 031–032 | ARCHITECTURE.md §§9, 40 | All adjacent Domains |
| REQ-ORD-037 | PRODUCT.md §§14.8, 15.3, 16.4, 24 | REQ-BUS-023, 030–036, 042, 048 | REQ-PAY-035; REQ-SHP-031 | GLOSSARY.md §§8, 41; SECURITY-STANDARDS.md §12 | Customer, Administration |
| REQ-ORD-038 | PRODUCT.md §§16.2–16.5, 16.11, 24 | REQ-BUS-023, 030, 035–036, 042 | REQ-INV-024–025; REQ-PAY-034–035; REQ-SHP-031–032 | ARCHITECTURE.md §§20.5, 25.5 | Payment, Inventory, Shipping, Return |
| REQ-ORD-039 | PRODUCT.md §§14.8, 16.11, 24 | REQ-BUS-023, 030, 048 | REQ-PRC-021; REQ-PAY-034–035; REQ-SHP-032 | GLOSSARY.md §24 | Return, Payment |
| REQ-ORD-040 | PRODUCT.md §§5.5–5.6, 17.2 | REQ-BUS-021–030, 042, 045 | REQ-CHK-032; REQ-PAY-020; REQ-INV-030; REQ-SHP-026 | ARCHITECTURE.md §§20, 26 | Operations, support |
| REQ-ORD-041 | PRODUCT.md §§5.5–5.6, 15.4–15.5, 17.2 | REQ-BUS-026, 034–036, 042–045 | REQ-PAY-024–025; REQ-INV-030; REQ-SHP-029–030; REQ-CHK-031–033 | ARCHITECTURE.md §20.5; DATABASE.md §52 | Operations, support |
| REQ-ORD-042 | PRODUCT.md §§15.3–15.4, 16.7, 20 | REQ-BUS-032–034, 039, 042 | REQ-CUS-010–012, 034, 039–040; REQ-CHK-012, 034–035 | SECURITY-STANDARDS.md §§12, 27, 35; API.md §43 | Customer, Administration |
| REQ-ORD-043 | PRODUCT.md §§16.7, 20, 34.5 | REQ-BUS-027, 039–041 | REQ-CUS-040–041; REQ-PAY-032; REQ-CHK-035 | SECURITY-STANDARDS.md §§14, 27, 35; DATABASE.md §35 | All representations |
| REQ-ORD-044 | PRODUCT.md §§16.12, 24 | REQ-BUS-048, 052 | REQ-CUS-042; REQ-PRC-022; REQ-PAY-033; REQ-CHK-036 | SECURITY-STANDARDS.md §§9, 27 | Fraud, operations |
| REQ-ORD-045 | PRODUCT.md §§21, 36 | REQ-BUS-046–048 | REQ-CHK-038; REQ-PAY-039; REQ-SHP-037 | API.md §§22–24, 43, 58; DOCUMENTATION-STANDARDS.md §26 | All consumers |
| REQ-ORD-046 | PRODUCT.md §§21, 33, 36 | REQ-BUS-043–044, 048–049 | REQ-PAY-038; REQ-SHP-036; REQ-CHK-039 | GLOSSARY.md §42; EVENTS.md §§9, 14, 37–39 | Notifications, Reporting |
| REQ-ORD-047 | PRODUCT.md §§5.7, 14.6–14.8, 30.4 | REQ-BUS-037, 042 | REQ-CUS-043; REQ-CHK-040 | ACCESSIBILITY.md §§21–32; UI.md §§26, 40, 50 | Customer, Administration |
| REQ-ORD-048 | PRODUCT.md §§15.3–15.5, 16.4, 20, 24 | REQ-BUS-031–036, 043 | REQ-CUS-037, 039, 045; REQ-INV-027–028, 033; REQ-PAY-030, 037, 042; REQ-SHP-033–034, 040; REQ-CHK-042 | AGENTS.md §31.11; SECURITY-STANDARDS.md §§12, 27 | Administration, support |
| REQ-ORD-049 | PRODUCT.md §§5.5–5.6, 17.2, 35 | REQ-BUS-038, 042–045 | REQ-INV-032; REQ-PRC-030; REQ-PAY-041; REQ-SHP-039; REQ-CHK-041 | PERFORMANCE.md; ARCHITECTURE.md §18 | Operations |
| REQ-ORD-050 | PRODUCT.md §§15, 16.4, 17.4, 20, 31 | REQ-BUS-033–036, 043 | REQ-CUS-045; REQ-INV-033; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040; REQ-CHK-042 | SECURITY-STANDARDS.md §§7.9, 27; DATABASE.md §42 | Administration, audit |
| REQ-ORD-051 | PRODUCT.md §§9.3, 14.6–14.8, 33–34 | REQ-BUS-043–044, 049, 053 | REQ-CUS-024–025; REQ-PAY-043; REQ-SHP-041; REQ-CHK-043 | ARCHITECTURE.md §§9, 20.4, 26.2 | Notifications, Reporting |
| REQ-ORD-052 | PRODUCT.md §§26.4, 35 | REQ-BUS-011–013, 021–045, 047 | REQ-PRD-039; REQ-CUS-049; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-CHK-044 | TESTING-STANDARDS.md; ACCESSIBILITY.md; PERFORMANCE.md | Verification evidence |

## 28. Open Product Decisions

`PRODUCT.md` currently contains exactly 30 Open Product Decisions. Twenty-one are materially relevant to Order, and this Specification resolves none of them. All concrete values remain external until Approved by the owning governance.

| Product Decision | Order boundary affected |
| --- | --- |
| Guest checkout versus mandatory account rules | Determines permitted Customer or Visitor association; Order selects neither policy. |
| Customer email-verification requirements | Determines whether verification affects permitted creation context; Order creates no verification gate. |
| Initial payment methods and provider | Determines applicable Payment evidence and references; Order remains method- and provider-neutral. |
| Shipping provider, service levels, delivery areas, and fee policy | Determines governed delivery and fulfilment context; Order preserves history without choosing service policy. |
| Free-delivery threshold and promotional treatment | Determines Pricing and Shipping charge evidence retained in the snapshot; Order defines no threshold. |
| Tax-inclusive display and invoice requirements | Determines presentation and document obligations; Order preserves governed values without selecting policy. |
| Cancellation eligibility and cutoff policy | Determines whether cancellation is permitted; Order defines coordination and history only. |
| Returns, exchanges, and refund policy | Determines post-order eligibility and treatment; Order defines only future boundaries. |
| Stock Reservation duration | Determines Inventory timing; Order selects neither duration nor expiry behavior. |
| Back-order and pre-order support | Determines permitted creation and fulfilment context; Order consumes the governed outcome. |
| Voucher and promotion stacking policy | Determines confirmed commercial values; Order preserves but does not calculate them. |
| Customer-support channels and service expectations | Determines recovery and support presentation; Order selects no channel or service level. |
| Marketing-consent and communication-preference model | Determines eligible optional communication; Order does not infer Consent from purchase. |
| Initial analytics provider and event taxonomy | Determines future analytics integration; Order defines no provider or taxonomy. |
| Initial reporting and export requirements | Determines future non-authoritative outputs; Order defines no report or export format. |
| Administrative role and permission matrix | Determines protected operational access; Order requires contextual Authorization without defining the matrix. |
| Production customer-service and operational escalation process | Determines exceptional recovery and escalation; Order defines only safe boundaries. |
| South African tax-display, invoice, and credit-note policy | Determines production commercial-document handling; Order establishes no policy or legal conclusion. |
| Fraud-screening approach and manual-review workflow | Determines restriction and review inputs; Order does not own fraud policy. |
| Customer data export, correction, deletion, and account-closure workflow | Determines privacy handling of retained Customer linkage and history; Order invents no data-lifecycle policy. |
| Gift cards, store credit, and promotional credit policy | Determines future commercial and Payment treatment; Order defines no credit behavior. |

## 29. Risks

| Risk | Implementation-neutral control direction |
| --- | --- |
| Duplicate Order creation | Require governed correlation, duplicate detection, applicable idempotency, and evidence-aware retry. |
| False creation success | Allow only Order-owned durable evidence to establish existence and successful creation. |
| Checkout and Order mismatch | Preserve the handoff, distinguish outcomes, and reconcile without fabricating either Domain's truth. |
| Payment and Order mismatch | Preserve independent authority, explicit uncertainty, duplicate safety, and reconciliation. |
| Inventory effect without Order | Correlate and reconcile through Inventory-owned evidence before repetition or compensation. |
| Shipping activity without valid Order context | Require governed Order and Order Item association and reject unsupported activity. |
| Mutable snapshot corruption | Preserve confirmed historical values and append later evidence without rewriting the original. |
| Historical Product drift | Retain intelligible purchased-item representation independent of live catalogue mutation. |
| Historical Customer or Address drift | Separate current profile and Address truth from retained Order context. |
| Pricing drift rewrites history | Separate current Pricing from the confirmed commercial snapshot. |
| Unauthorized or cross-Customer access | Enforce contextual server-side Authorization, ownership, isolation, and safe denial. |
| Order identifier enumeration | Treat identifiers as references rather than credentials and resist existence disclosure. |
| Sensitive Data leakage | Minimize and protect data across Contracts, events, logs, errors, Projections, exports, audit, and support. |
| Unsafe lifecycle transition | Validate current state, Permission, invariant, causation, and evidence while preserving history. |
| Cancellation race or partial effect | Preserve current evidence, duplicate safety, partial-completion awareness, and reconciliation. |
| Unsafe retry or replay | Classify known and unknown prior effects before repeating any harmful operation. |
| Concurrent or reordered activity | Reject stale conflict and prevent later arrival from silently overwriting accepted truth. |
| Timeout or partial completion | Keep known, unknown, completed, and failed effects explicit and recoverable. |
| Uncertain external effect | Verify through the owning authority and leave unresolved state visible until reconciled. |
| Reconciliation gap | Preserve correlation, provenance, operational visibility, authorized repair, and outcome history. |
| Downstream Projection becomes authority | Treat every representation as derived, stale-capable, and unable to mutate Order truth. |
| Inaccessible or misleading Order status | Require applicable WCAG 2.2 AA and truthful pending, partial, failure, and uncertainty communication. |
| Unresolved Product Decision embedded locally | Keep policy external, explicit, and reversible until Approved governance resolves it. |
| Order absorbs adjacent Domain authority | Preserve Contract boundaries and require owning-Domain evidence for external facts and effects. |

## 30. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/frontend/ANGULAR.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/STORYBOOK.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/checkout/checkout-domain.md`

## 31. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-04 | Draft | Initial comprehensive Order Domain Specification. |
| 1.0.0 | 2026-09-04 | Approved | Promoted the Order Domain Specification to its Approved normative baseline without changing substantive Domain behavior or authority boundaries. |

## 32. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `ORD`, Approved Requirements are normative only within the Order Domain scope and not repository-wide authority, and governing-source precedence and Approved upstream Domain authority are preserved;
2. Order authority and non-authority are complete, explicit, and transfer no adjacent Domain truth;
3. stable Order identity and Order Number remain distinct, protected, isolated, and implementation-neutral;
4. the Checkout-to-Order handoff preserves applicable governed context while Order alone establishes creation truth;
5. creation request, processing, success, confirmation, failure and uncertainty are distinct and duplicate-safe;
6. Order Items and the Order Snapshot preserve supported historical commercial evidence without becoming live upstream authority;
7. later mutable upstream change, legitimate lifecycle activity, correction, privacy handling, Return, and Refund preserve historical immutability without invented policy;
8. only `Pending Payment`, `Confirmed`, `Processing`, `Partially Fulfilled`, `Fulfilled`, `Cancelled`, `Completed`, and `Archived` are used as canonical Order states, without a complete transition graph or universal traversal;
9. Customer, Visitor, Address, Product, Product Variant, Pricing, Money, Currency, Payment, Inventory, Stock Reservation, Shipping, Fulfilment, Return, and Refund boundaries remain intact;
10. failure, uncertainty, duplicate, retry, replay, concurrency, timeout, partial completion, recovery, and reconciliation behavior preserves provenance and authority;
11. every Requirement has exactly one complete observable Acceptance Criteria row;
12. every Requirement has exactly one semantically valid traceability row supported by existing sources;
13. all 30 Open Product Decisions were reviewed, exactly 21 materially Order-relevant decisions are represented in `PRODUCT.md` order, and none is resolved;
14. canonical terminology is correct, no new canonical term is created, and no Glossary amendment is required;
15. Risks are material, Order-specific, non-duplicative, and paired with implementation-neutral controls;
16. every Related Document exists and is relevant, with no self-reference or nonexistent future Domain Specification;
17. Revision History contains exactly the preserved `0.1.0 Draft` row and the new `1.0.0 Approved` row;
18. Markdown, whitespace, final newline, security, accessibility, performance, events, Contracts, audit, testing, and implementation-neutrality are valid; and
19. the final Git scope contains only lifecycle-promotion changes to `specifications/domains/order/order-domain.md`, with nothing staged or otherwise modified.
