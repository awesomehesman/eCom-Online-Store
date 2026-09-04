---
title: Return Domain
version: 0.1.0
status: Draft
owner: Product and Engineering
last_updated: 2026-09-04
authoritative: false
---

# Return Domain

## 1. Purpose

This Specification defines implementation-neutral Return-domain boundaries for post-delivery Return Requests, Return Items, eligibility evaluation under Approved policy, Return Authorization, lifecycle history, inspection evidence, Return Disposition, and Return-owned recovery and reconciliation.

This document uses scope code `RET`. Because it is Draft, its Requirements are non-normative until Approved. If Approved, they become normative only within Return scope, remain subordinate to higher-authority governing sources, preserve every Approved upstream Domain's authority, resolve no Open Product Decision, and grant no repository-wide authority.

## 2. Scope and Authority

### REQ-RET-001 — Lifecycle, Authority, and Scope

Return MUST govern only Return-owned truth, preserve governing-source precedence and Approved upstream authority, use scope `RET`, and MUST NOT treat this Draft as normative or repository-wide authority before approval.

### REQ-RET-002 — Return-Owned Truth

Return MUST own Return and Return Request identity, creation truth, Return Item association, policy-dependent eligibility outcome, Return Authorization, Return lifecycle and history, inspection evidence, Return Disposition decision, and Return-owned exception, recovery, and reconciliation history.

### REQ-RET-003 — Cross-Domain Non-Authority

Return MUST NOT own Order, Order Item, Order Snapshot, Order lifecycle, Product, Product Variant, Category, Customer identity, Authentication, general Authorization policy, Pricing, Payment, Refund execution, Refund Transaction, Inventory, Stock, Restocking effects, Shipment, reverse-logistics execution, Notification delivery, reporting, analytics, Tax, invoice, Credit Note, fraud, Exchange fulfilment, or Administration policy.

## 3. Domain Context

Return is downstream of delivered Order Item history and coordinates governed outcomes with Order, Payment, Inventory, Shipping and Fulfilment, Customer, Pricing, Product, Administration, Notifications, and Reporting without absorbing their authority. Cart and Checkout establish no Return truth.

## 4. Canonical Terminology

Return, Return Request, Return Authorization, Return Window, Return Item, Return Disposition, Exchange, Restocking, Order, Order Item, Order Snapshot, Refund, Refund Request, Refund Transaction, Partial Refund, Stock, Stock Adjustment, Stock Movement, Shipment, Customer, Visitor, Money, Currency, Contract, Domain Event, Integration Event, Projection, Audit Record, Sensitive Data, Authorization, Principal, Resource, and Risk retain `GLOSSARY.md` meanings. Return-specific lifecycle labels below are scoped to this Domain only.

## 5. Return Identity

### REQ-RET-004 — Stable Return Identity

Each Return MUST have stable unique identity correlated to its Return Request, applicable Order and Return Items, lifecycle, evidence, and outcomes without selecting an identifier format or generator.

### REQ-RET-005 — Identifier Isolation

Return identifiers and related Order Numbers MUST be references rather than credentials and MUST preserve ownership, Resource isolation, enumeration resistance, safe errors, and trusted server-side Authorization.

## 6. Order and Order Item Boundary

### REQ-RET-006 — Governed Order Association

A Return MUST reference an existing authoritative Order and one or more applicable Order Items through trusted Order evidence; supplied identifiers, client history, receipts, or Projections MUST NOT establish that association.

### REQ-RET-007 — Historical Order Evidence

Return MUST use the immutable Order Snapshot and governed Order history for applicable purchased item, quantity, Customer or Visitor, Money, Currency, Tax, Discount, delivery, and fulfilment context without recalculating or rewriting history.

### REQ-RET-008 — Order Non-Mutation

Return activity MAY be referenced by Order but MUST NOT directly mutate Order identity, Order Items, the Order Snapshot, Order lifecycle, cancellation truth, or historical commercial values.

## 7. Return Creation

### REQ-RET-009 — Return Request Creation Truth

Only Return-owned durable evidence MUST establish a Return Request or Return. Submission, UI success, support contact, Shipment movement, Refund Request, or client state MUST NOT independently prove creation.

### REQ-RET-010 — Creation Outcome Separation

Receipt, validation, acceptance for processing, creation, rejection, duplication, failure, and uncertainty MUST remain distinguishable, correlated, authorized, and duplicate-safe.

## 8. Return Items and Quantities

### REQ-RET-011 — Return Item Ownership

Each Return Item MUST identify an Order Item quantity, reason, evidence where applicable, and requested resolution while remaining distinct from the Order Item, Product Variant, Stock unit, Shipment item, Refund line, and replacement item.

### REQ-RET-012 — Quantity Integrity

Requested, authorized, expected, received, inspected, accepted, rejected, missing, excess, and resolved quantities MUST remain distinguishable where applicable and MUST NOT exceed governed Order Item and prior-return constraints.

### REQ-RET-013 — Duplicate Return Prevention

Duplicate, overlapping, concurrent, stale, retried, or replayed requests MUST NOT authorize or resolve more quantity than permitted, create duplicate Returns, or multiply external effects.

## 9. Eligibility and Return Authorization

### REQ-RET-014 — Eligibility Policy Deferral

Return MUST evaluate eligibility only under Approved policy and authoritative context and MUST NOT invent Return Window values, categories, conditions, exclusions, fees, evidence thresholds, shipping responsibility, Exchange rules, or Refund treatment.

### REQ-RET-015 — Explicit Eligibility Outcome

Eligible, ineligible, pending review, denied, unavailable, stale, conflicting, and uncertain outcomes MUST be explicit and explainable without exposing protected policy, fraud, or operational details.

### REQ-RET-016 — Return Authorization Integrity

A Return Authorization MUST be issued only from a governed eligible outcome with applicable Return Items, quantities, conditions, scope, actor, and evidence; it MUST remain distinct from Authentication and access-control Authorization.

### REQ-RET-017 — External-Effect Non-Proof

Return Authorization MUST NOT independently prove Refund approval or completion, Refund Transaction, Shipment creation or completion, physical receipt, inspection, Restocking, Stock availability, Exchange completion, or Customer communication.

## 10. Return Lifecycle

### REQ-RET-018 — Return-Scoped Lifecycle Meanings

Return-specific meanings are: `Requested`, when durable Return Request creation is established; `Pending Review`, when a governed eligibility or evidence decision remains outstanding; `Authorized`, when a Return Authorization has been issued; `Rejected`, when the Return Request has been denied with a governed reason; `Awaiting Receipt`, when an authorized Return awaits physical receipt where applicable; `Received`, when trusted evidence establishes applicable physical receipt without implying acceptance; `Under Inspection`, when governed inspection remains incomplete; `Resolved`, when the Return-owned disposition and required coordination state are recorded without implying completion in another Domain; and `Cancelled`, when the Return process has ended without implying Order, Shipment, Inventory, or Refund cancellation. These meanings are scoped to Return only and establish neither repository-wide vocabulary nor a mandatory universal traversal.

### REQ-RET-019 — Controlled Lifecycle History

Every accepted Return transition MUST validate current Return state, applicable policy, Authorization, quantity invariants, evidence, concurrency, and external dependencies and MUST preserve actor or cause, time, prior state, outcome, and uncertainty where material.

### REQ-RET-020 — Cross-Domain Lifecycle Integrity

A Return state MUST NOT establish Order, Payment, Refund, Inventory, Shipment, delivery, Exchange, Notification, or Projection truth; external-evidence-dependent progression MUST preserve its owning source and reconciliation status.

## 11. Customer and Staff Initiation

### REQ-RET-021 — Governed Customer Initiation

Customer or Visitor initiation MUST use trusted association to the applicable Order and Return Items, current contextual Authorization, policy-permitted capability, safe disclosure, and accessible outcomes without selecting guest, Account, or email-verification policy.

### REQ-RET-022 — Governed Staff Initiation

Staff initiation or assistance MUST invoke Return-owned behavior with least privilege, reason and proportional audit where material, Customer isolation, and explicit outcomes without defining a Role, Permission, approval, support, or escalation matrix.

## 12. Inspection and Return Disposition

### REQ-RET-023 — Receipt and Inspection Evidence

Return MAY retain trusted receipt and inspection evidence with provenance, applicable Return Item quantities, condition observations, actor, time, and uncertainty, but physical receipt MUST NOT independently establish eligibility, acceptance, disposition, Refund, or sellable Stock.

### REQ-RET-024 — Return Disposition Decision

Return MAY assign a governed Return Disposition after applicable inspection while deferring disposition rules and Inventory execution; restock, quarantine, repair, supplier return, disposal, or another outcome MUST NOT be fabricated from incomplete evidence.

### REQ-RET-025 — Partial and Exceptional Receipt

Partial, missing, excess, substituted, damaged, unexpected, rejected, mixed-condition, duplicate, and uncertain receipt or inspection outcomes MUST remain explicit, quantity-safe, historically traceable, and reconcilable.

## 13. Exchange Boundary

### REQ-RET-026 — Exchange Coordination Non-Authority

Return MAY coordinate a future governed Exchange outcome but MUST NOT select replacement Product or Product Variant, calculate price difference, reserve Stock, create an Order or Payment, execute Shipping, define Exchange eligibility, or imply completion.

## 14. Payment and Refund Boundary

### REQ-RET-027 — Refund Eligibility Input Boundary

Return MAY provide governed Return eligibility, Return Items, quantities, reasons, disposition, and historical evidence as inputs to an approved Refund decision, but MUST NOT establish payment-side Refund execution truth or calculate an amount independently.

### REQ-RET-028 — Refund and Return Independence

Return, Refund Request, Refund, Refund Transaction, provider processing, Order financial history, and Customer communication MUST remain distinct; Return receipt, authorization, resolution, or cancellation MUST NOT prove financial outcome.

### REQ-RET-029 — Refund Coordination Safety

Refund coordination MUST use explicit Money and Currency from governing sources, preserve Payment authority, distinguish request, processing, success, failure, rejection, partial and uncertainty, and prevent duplicate financial effects through safe correlation and reconciliation.

## 15. Inventory and Restocking Boundary

### REQ-RET-030 — Inventory Non-Authority

Return MAY supply governed Return Disposition and inspected-quantity evidence, but Inventory MUST own Restocking effects, Stock condition truth where applicable, Stock Location, Stock Adjustment, Stock Movement, Available-to-Sell, and resulting Inventory state.

### REQ-RET-031 — Inventory Coordination Safety

Physical receipt or an accepted Return Item MUST NOT make Stock sellable. Inventory effects, failure, partial completion, duplication, disagreement, and uncertainty MUST remain explicit, duplicate-safe, and reconcilable.

## 16. Shipping and Reverse Logistics Boundary

### REQ-RET-032 — Governed Reverse-Logistics Handoff

Return MAY provide authorized Return context for reverse logistics while Shipping and Fulfilment retains Carrier, Shipment, label, tracking, transport, provider, receipt-transport, exception, and reconciliation authority.

### REQ-RET-033 — Transport and Return Separation

Forward Shipping, returned-to-sender, Customer Return transport, delivery, physical receipt, and Return lifecycle MUST remain distinct; no Shipping state or provider signal independently establishes Return eligibility, authorization, disposition, or resolution.

## 17. Product and Commercial Boundaries

### REQ-RET-034 — Product and Product Variant Separation

Return MUST use historical Order Item representation for purchased meaning and MAY consult current Product authority only where Approved policy requires it; current catalogue mutation MUST NOT rewrite Return or Order history.

### REQ-RET-035 — Pricing, Tax, and Credit Non-Authority

Return MUST NOT calculate Price, Discount, Promotion, Voucher restoration, Refund amount, Tax adjustment, shipping fee, Exchange difference, gift card, store credit, promotional credit, invoice, or Credit Note treatment. Governed calculations and documents remain with assigned authorities.

## 18. Customer, Privacy, and Data

### REQ-RET-036 — Customer and Sensitive Data Boundary

Return MUST preserve Customer or Visitor association without owning identity, minimize and purpose-limit contact, Address, reason, evidence, images, notes, identifiers, and payment-adjacent data, and protect them across storage, Contracts, events, logs, errors, exports, Projections, audit, and support without defining retention or deletion policy.

## 19. Cancellation and Returned-to-Sender

### REQ-RET-037 — Distinct Adjacent Outcomes

Order cancellation, Return cancellation, Shipment cancellation, returned-to-sender, Return, Exchange, Refund, and Refund Transaction MUST remain distinct. Return MUST NOT invent cancellation eligibility or convert returned-to-sender evidence into a Customer Return automatically.

## 20. Failure, Recovery, and Reconciliation

### REQ-RET-038 — Failure and Uncertainty

Invalid association, ineligibility, duplicate or excessive quantity, stale policy or evidence, denial, dependency failure, timeout, reordered outcome, partial completion, inspection disagreement, unknown external effect, and reconciliation failure MUST remain distinguishable without fabricated success or failure.

### REQ-RET-039 — Controlled Recovery and Reconciliation

Recovery MUST preserve correlation, provenance, authoritative evidence, history, quantity and duplicate safety, Authorization, observability, and resolved or still-uncertain outcomes and MUST NOT directly rewrite another Domain's record.

## 21. Security and Fraud

### REQ-RET-040 — Authorization and Tamper Resistance

Protected Return access and action MUST require trusted server-side Authorization for Principal, Resource, action, ownership, and current state. UI visibility, route, Role label, Claims, supplied identifier, uploaded evidence, or Authentication alone MUST NOT authorize behavior.

### REQ-RET-041 — Fraud Policy Non-Authority

Return MAY consume governed fraud or review outcomes but MUST NOT define signals, thresholds, models, provider, blocking policy, manual-review workflow, or automatic commercial effect; suspicious activity MUST preserve authoritative state, privacy, explainability, recovery, and audit.

## 22. Contracts and Events

### REQ-RET-042 — Return Contract Boundary

Future Contracts MUST preserve Return and external authority, identity, correlation, quantities, explicit outcomes, compatibility, Authorization, Sensitive Data, duplicate, retry, replay, concurrency, failure, and uncertainty without defining routes, methods, codes, fields, DTOs, classes, schemas, tables, or provider payloads.

### REQ-RET-043 — Conditional Return Events

Only where Architecture, an Approved Contract, reliability, or synchronization governance requires an event MAY canonical `ReturnRequested` or `ReturnAuthorized` meanings be used. Events MUST represent completed facts and preserve authority, correlation, compatibility, privacy, replay safety, ordering uncertainty, and non-authoritative consumer semantics without defining transport or payload.

## 23. Accessibility

### REQ-RET-044 — Accessible Return Outcomes

Applicable Customer and Staff Return initiation, item selection, reason and evidence input, review, authorization, transport, inspection, status, denial, failure, recovery, and uncertainty surfaces MUST target WCAG 2.2 AA with keyboard, focus, labels, instructions, errors, status announcements, and truthful outcomes without selecting design or wording.

## 24. Operations and Representations

### REQ-RET-045 — Bounded and Observable Return Operations

Return creation, review, authorization, receipt, inspection, disposition, external coordination, failure, recovery, and reconciliation MUST be bounded, correlated, attributable, and observable without defining latency, timeout, retry, capacity, SLA, SLO, device, network, or provider targets.

### REQ-RET-046 — Proportional Return Audit

Material eligibility decisions, Return Authorization, rejection, disposition, administrative intervention, financial coordination, sensitive evidence access, correction, recovery, reconciliation, and exceptional action MUST produce proportional tamper-resistant Audit Records; routine reads MUST NOT automatically be high-Risk.

### REQ-RET-047 — Non-Authoritative Representations

Notifications, support views, reports, analytics, exports, and Projections MUST derive from governed facts, identify source where applicable, protect data, expose delay or staleness, survive delivery failure, and MUST NOT mutate or replace Return or upstream authority.

## 25. Testing

### REQ-RET-048 — Return Verification Coverage

Evidence MUST cover all applicable positive, negative, boundary, identity, association, quantity, eligibility, authorization, lifecycle, inspection, disposition, Exchange, Refund, Inventory, Shipping, Customer, cancellation, resilience, concurrency, security, fraud, accessibility, performance, audit, Contract, event, and representation behavior without selecting framework, tooling, identifiers, or numerical coverage.

## 26. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-RET-001 | Metadata, `RET` scope, Draft non-normativity, prospective Return-only normativity, precedence, and upstream authority are explicit. |
| REQ-RET-002 | Every listed Return-owned fact has one Return authority and durable history. |
| REQ-RET-003 | Return owns none of the listed adjacent truths or policies. |
| REQ-RET-004 | Stable unique Return identity correlates request, items, evidence, lifecycle, and outcomes without mechanism choice. |
| REQ-RET-005 | Identifiers grant no authority or existence disclosure and preserve isolation and safe errors. |
| REQ-RET-006 | Every Return uses trusted existing Order and Order Item evidence rather than client assertion. |
| REQ-RET-007 | Applicable historical context comes from immutable Order evidence and is neither recalculated nor rewritten. |
| REQ-RET-008 | Return cannot directly mutate any listed Order-owned truth. |
| REQ-RET-009 | Only Return-owned durable evidence proves creation. |
| REQ-RET-010 | All materially different creation outcomes remain explicit, correlated, authorized, and duplicate-safe. |
| REQ-RET-011 | Each Return Item has governed association, quantity, reason, evidence, and resolution while remaining distinct from adjacent records. |
| REQ-RET-012 | Every applicable quantity category is distinct and constrained by Order and prior-return truth. |
| REQ-RET-013 | Duplicate or concurrent activity cannot duplicate Returns, quantities, authorizations, resolutions, or effects. |
| REQ-RET-014 | Eligibility uses Approved policy and invents none of the prohibited values or rules. |
| REQ-RET-015 | Eligibility outcomes are explicit, explainable, and safely disclosed. |
| REQ-RET-016 | Return Authorization follows governed eligibility and remains distinct from access Authorization. |
| REQ-RET-017 | Return Authorization proves none of the listed external effects. |
| REQ-RET-018 | Only the nine Return-scoped meanings are defined, without repository-wide status or mandatory traversal. |
| REQ-RET-019 | Every transition validates applicable state, policy, authority, quantity, evidence, concurrency, and dependencies and preserves history. |
| REQ-RET-020 | Return lifecycle establishes no external Domain truth. |
| REQ-RET-021 | Customer or Visitor initiation is correctly associated, authorized, isolated, accessible, and policy-neutral. |
| REQ-RET-022 | Staff initiation is least-privileged, reasoned and audited where material, isolated, and matrix-neutral. |
| REQ-RET-023 | Receipt and inspection evidence has provenance but independently proves no eligibility, disposition, Refund, or Stock outcome. |
| REQ-RET-024 | Disposition is evidence-based and Inventory-execution-neutral with no invented rule. |
| REQ-RET-025 | Every exceptional receipt or inspection outcome remains explicit, quantity-safe, historical, and reconcilable. |
| REQ-RET-026 | Exchange coordination absorbs none of the listed Product, Pricing, Inventory, Order, Payment, or Shipping authorities. |
| REQ-RET-027 | Return supplies only governed Refund inputs and owns neither execution truth nor independent amount calculation. |
| REQ-RET-028 | Physical Return, financial Refund, Order history, and communication remain distinct. |
| REQ-RET-029 | Refund coordination preserves Money, Currency, Payment authority, explicit outcomes, duplicate safety, and reconciliation. |
| REQ-RET-030 | Inventory owns every listed Restocking and Stock effect. |
| REQ-RET-031 | Receipt never implies sellable Stock and all Inventory effects remain explicit and reconcilable. |
| REQ-RET-032 | Reverse logistics uses authorized Return context while Shipping retains execution and provider truth. |
| REQ-RET-033 | Forward, returned-to-sender, Return transport, receipt, and Return lifecycle remain distinct. |
| REQ-RET-034 | Historical purchased meaning is preserved separately from current catalogue authority. |
| REQ-RET-035 | Return performs none of the listed calculations, restorations, credit, Tax, or document decisions. |
| REQ-RET-036 | Customer authority and privacy are preserved across every listed Return data surface without lifecycle-policy invention. |
| REQ-RET-037 | Cancellation, returned-to-sender, Return, Exchange, and Refund outcomes remain distinct without invented policy. |
| REQ-RET-038 | Every listed failure or uncertainty remains distinguishable and cannot become fabricated truth. |
| REQ-RET-039 | Recovery preserves authority, provenance, history, quantities, safety, Authorization, observability, and uncertainty. |
| REQ-RET-040 | Protected actions enforce contextual server Authorization and resist identifier, UI, client, and evidence tampering. |
| REQ-RET-041 | Fraud input is governed, non-authoritative for Return policy, private, explainable, recoverable, and auditable. |
| REQ-RET-042 | Contracts preserve all listed semantics without interface, schema, persistence, or provider design. |
| REQ-RET-043 | Only conditional canonical completed-fact events are permitted, with safe semantics and no transport design. |
| REQ-RET-044 | All applicable Customer and Staff Return outcomes provide WCAG 2.2 AA evidence without design invention. |
| REQ-RET-045 | Every material operation is bounded, observable, correlated, and attributable without numerical targets. |
| REQ-RET-046 | Applicable material actions create proportional tamper-resistant audit evidence; routine reads are not automatically high-Risk. |
| REQ-RET-047 | Every downstream representation is protected, stale-aware, failure-safe, and non-authoritative. |
| REQ-RET-048 | Verification covers every listed concern without framework, tooling, identifier, or numerical choices. |

## 27. Requirement Traceability

| Requirement | Product | Business | Approved Domains | Governing sources | Consumers |
| --- | --- | --- | --- | --- | --- |
| REQ-RET-001 | PRODUCT.md §§16.11, 17.1, 24 | REQ-BUS-030, 047–048 | REQ-ORD-001, 039 | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9 | Return governance |
| REQ-RET-002 | PRODUCT.md §16.11 | REQ-BUS-023, 030 | REQ-ORD-039; REQ-PAY-035 | GLOSSARY.md §§8, 24 | Return |
| REQ-RET-003 | PRODUCT.md §§16.1–16.12 | REQ-BUS-021–030, 044 | REQ-ORD-003, 039; REQ-PAY-035; REQ-SHP-032 | AGENTS.md §31 | All adjacent Domains |
| REQ-RET-004 | PRODUCT.md §§16.4, 16.11 | REQ-BUS-023, 030, 035 | REQ-ORD-004, 015 | GLOSSARY.md §§8, 24 | Customer, support |
| REQ-RET-005 | PRODUCT.md §§12.3–12.4, 20 | REQ-BUS-032–033, 039 | REQ-ORD-006; REQ-CUS-005, 039 | SECURITY-STANDARDS.md §§12, 35 | Customer, Administration |
| REQ-RET-006 | PRODUCT.md §§16.4, 16.11 | REQ-BUS-022–023, 030 | REQ-ORD-015, 017–019, 039 | ARCHITECTURE.md §§9, 40.5 | Order, Return |
| REQ-RET-007 | PRODUCT.md §§16.4, 16.10–16.11 | REQ-BUS-022–023, 030, 054 | REQ-ORD-016–025, 039 | GLOSSARY.md §§8, 23–24 | Customer, Finance |
| REQ-RET-008 | PRODUCT.md §§16.4, 16.11 | REQ-BUS-022–023, 030 | REQ-ORD-019–020, 036, 039 | ARCHITECTURE.md §40.5 | Order |
| REQ-RET-009 | PRODUCT.md §16.11 | REQ-BUS-030, 042 | REQ-ORD-009–010 | GLOSSARY.md §24; API.md §45 | Customer, Staff |
| REQ-RET-010 | PRODUCT.md §§5.5–5.6, 16.11 | REQ-BUS-030, 036, 042 | REQ-ORD-010, 013–014 | API.md §§22–24 | Operations |
| REQ-RET-011 | PRODUCT.md §§16.4, 16.11 | REQ-BUS-022–023, 030 | REQ-ORD-015; REQ-SHP-013 | GLOSSARY.md §24 | Order, Inventory |
| REQ-RET-012 | PRODUCT.md §16.11 | REQ-BUS-030, 035–036 | REQ-ORD-015, 039; REQ-INV-017–019 | DATABASE.md §§20, 42 | Operations |
| REQ-RET-013 | PRODUCT.md §§16.3, 16.11, 17.2 | REQ-BUS-026, 030, 036 | REQ-PAY-022–023; REQ-INV-014, 016 | ARCHITECTURE.md §§4.2, 26.4 | Payment, Inventory |
| REQ-RET-014 | PRODUCT.md §§16.6, 16.11, 24 | REQ-BUS-030, 041, 048 | REQ-PRC-012, 021; REQ-PAY-035 | GLOSSARY.md §24 | Product, Legal |
| REQ-RET-015 | PRODUCT.md §§5.5–5.6, 16.11 | REQ-BUS-030, 042 | REQ-PAY-020–021 | API.md §45 | Customer, Staff |
| REQ-RET-016 | PRODUCT.md §16.11 | REQ-BUS-030, 032–034 | REQ-CUS-039; REQ-ORD-042 | GLOSSARY.md §§5, 24 | Customer, Staff |
| REQ-RET-017 | PRODUCT.md §16.11 | REQ-BUS-023, 028–030 | REQ-PAY-034–035; REQ-INV-024; REQ-SHP-032 | GLOSSARY.md §§9, 24 | All consumers |
| REQ-RET-018 | PRODUCT.md §§16.11, 17.1 | REQ-BUS-030 | REQ-ORD-035–036; REQ-PAY-003 | GLOSSARY.md §41 | Customer, Staff |
| REQ-RET-019 | PRODUCT.md §§16.11, 17.1 | REQ-BUS-030, 032–036 | REQ-ORD-034; REQ-CUS-039 | SECURITY-STANDARDS.md §12 | Operations |
| REQ-RET-020 | PRODUCT.md §§16.3–16.5, 16.11 | REQ-BUS-023–030 | REQ-ORD-036, 039; REQ-PAY-027–028; REQ-SHP-013 | ARCHITECTURE.md §§9, 40 | All adjacent Domains |
| REQ-RET-021 | PRODUCT.md §§12.3–12.4, 16.7, 24 | REQ-BUS-030, 032, 037, 040 | REQ-CUS-024–025, 034, 039 | SECURITY-STANDARDS.md §§10–12 | Customer, Identity |
| REQ-RET-022 | PRODUCT.md §§15.3–15.5, 24 | REQ-BUS-031–034, 036 | REQ-CUS-037, 039, 045 | SECURITY-STANDARDS.md §§12, 27 | Administration |
| REQ-RET-023 | PRODUCT.md §16.11 | REQ-BUS-030, 034–035 | REQ-SHP-024, 032; REQ-INV-017–019 | GLOSSARY.md §24 | Inventory, Payment |
| REQ-RET-024 | PRODUCT.md §16.11 | REQ-BUS-030, 034–035 | REQ-INV-017–019, 024 | GLOSSARY.md §24 | Inventory |
| REQ-RET-025 | PRODUCT.md §§5.5–5.6, 16.11 | REQ-BUS-030, 035–036, 042 | REQ-SHP-025–030; REQ-INV-030, 036 | ARCHITECTURE.md §§20, 26 | Operations |
| REQ-RET-026 | PRODUCT.md §§16.1–16.5, 16.11, 24 | REQ-BUS-021–030, 048 | REQ-PRD-022; REQ-PRC-021; REQ-INV-024–026; REQ-SHP-032 | ARCHITECTURE.md §§9, 25 | All adjacent Domains |
| REQ-RET-027 | PRODUCT.md §§16.3–16.4, 16.11 | REQ-BUS-023, 028, 030 | REQ-PAY-034–035; REQ-ORD-026–028, 039 | GLOSSARY.md §§9, 24 | Payment, Finance |
| REQ-RET-028 | PRODUCT.md §§16.3–16.4, 16.11 | REQ-BUS-023, 028, 030, 049 | REQ-PAY-034–035, 043; REQ-ORD-028, 039 | ARCHITECTURE.md §25.5 | Customer, Finance |
| REQ-RET-029 | PRODUCT.md §§16.3, 16.11, 17.2 | REQ-BUS-025–026, 030, 035–036 | REQ-PAY-008, 020–025, 034 | ARCHITECTURE.md §§20.2, 25.5 | Payment, Operations |
| REQ-RET-030 | PRODUCT.md §§16.2, 16.11 | REQ-BUS-019–020, 030 | REQ-INV-006–008, 017–019, 024; REQ-SHP-011–012 | GLOSSARY.md §§10, 24 | Inventory |
| REQ-RET-031 | PRODUCT.md §§16.2, 16.11 | REQ-BUS-019–020, 030, 035–036 | REQ-INV-007–009, 016–019, 030 | ARCHITECTURE.md §§20.3, 20.5 | Inventory, Operations |
| REQ-RET-032 | PRODUCT.md §§16.5, 16.11 | REQ-BUS-029–030 | REQ-SHP-013–014, 017–024, 032 | ARCHITECTURE.md §§9, 25.4 | Shipping |
| REQ-RET-033 | PRODUCT.md §§16.5, 16.11 | REQ-BUS-029–030, 042 | REQ-SHP-019–024, 031–032 | GLOSSARY.md §§11, 24, 41 | Customer, Shipping |
| REQ-RET-034 | PRODUCT.md §§16.4, 16.11, 17.4 | REQ-BUS-022–023, 030 | REQ-PRD-006, 030–031; REQ-ORD-016, 019 | ARCHITECTURE.md §40.5 | Customer, Support |
| REQ-RET-035 | PRODUCT.md §§16.1, 16.6, 16.10–16.11, 24 | REQ-BUS-012–016, 030, 041, 054 | REQ-PRC-012–014, 019, 021; REQ-PAY-044; REQ-SHP-042 | GLOSSARY.md §§23–24 | Finance, Legal |
| REQ-RET-036 | PRODUCT.md §§16.7, 20, 24 | REQ-BUS-039–041 | REQ-CUS-025, 028–029, 040–041; REQ-ORD-043 | SECURITY-STANDARDS.md §§14, 27, 35 | Customer, Privacy |
| REQ-RET-037 | PRODUCT.md §§14.8, 16.4–16.5, 16.11, 24 | REQ-BUS-023, 029–030 | REQ-ORD-037–039; REQ-SHP-031–032 | GLOSSARY.md §§8, 11, 24, 41 | Order, Shipping |
| REQ-RET-038 | PRODUCT.md §§5.5–5.6, 17.2 | REQ-BUS-030, 042, 045 | REQ-ORD-040; REQ-PAY-020–021; REQ-INV-036; REQ-SHP-025–026 | ARCHITECTURE.md §§20, 26 | Operations, Support |
| REQ-RET-039 | PRODUCT.md §§15.4–15.5, 17.2 | REQ-BUS-034–036, 043–045 | REQ-ORD-041; REQ-PAY-024–025; REQ-INV-030; REQ-SHP-029–030 | DATABASE.md §52 | Operations |
| REQ-RET-040 | PRODUCT.md §§15.3–15.4, 20 | REQ-BUS-032–033, 039 | REQ-CUS-039–040; REQ-ORD-042 | SECURITY-STANDARDS.md §§12, 27 | Customer, Administration |
| REQ-RET-041 | PRODUCT.md §§16.12, 24 | REQ-BUS-048, 052 | REQ-CUS-042; REQ-PRC-022; REQ-PAY-033 | SECURITY-STANDARDS.md §§9, 27 | Fraud, Operations |
| REQ-RET-042 | PRODUCT.md §§21, 36 | REQ-BUS-046–048 | REQ-ORD-045; REQ-PAY-039; REQ-INV-035; REQ-SHP-037 | API.md §§22–24, 45 | All consumers |
| REQ-RET-043 | PRODUCT.md §§21, 33, 36 | REQ-BUS-043–044, 048–049 | REQ-PAY-038; REQ-INV-034; REQ-SHP-036 | GLOSSARY.md §42; EVENTS.md §§9, 14 | Notifications, Reporting |
| REQ-RET-044 | PRODUCT.md §§5.7, 16.9, 30.4 | REQ-BUS-037, 042 | REQ-CUS-043; REQ-ORD-047; REQ-SHP-038 | ACCESSIBILITY.md §§21–32 | Customer, Administration |
| REQ-RET-045 | PRODUCT.md §§5.5–5.6, 17.2, 35 | REQ-BUS-038, 042–045 | REQ-ORD-049; REQ-PAY-041; REQ-INV-032; REQ-SHP-039 | PERFORMANCE.md; ARCHITECTURE.md §18 | Operations |
| REQ-RET-046 | PRODUCT.md §§15, 16.11, 20, 31 | REQ-BUS-033–036 | REQ-CUS-045; REQ-ORD-050; REQ-PAY-042; REQ-INV-033; REQ-SHP-040 | SECURITY-STANDARDS.md §27 | Administration, Audit |
| REQ-RET-047 | PRODUCT.md §§9.3, 15.5, 33–34 | REQ-BUS-043–044, 049, 053 | REQ-CUS-038; REQ-ORD-051; REQ-PAY-043; REQ-SHP-041 | ARCHITECTURE.md §§9, 20.4 | Notifications, Reporting |
| REQ-RET-048 | PRODUCT.md §§26.4, 35 | REQ-BUS-030, 037–045, 047 | REQ-PRD-039; REQ-CUS-049; REQ-INV-037; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-ORD-052 | TESTING-STANDARDS.md §§18–20 | Verification evidence |

## 28. Open Product Decisions

`PRODUCT.md` contains exactly 30 Open Product Decisions. The following 21 are materially relevant to Return, remain in source order, and are unresolved by this Draft.

| Product Decision | Return boundary affected |
| --- | --- |
| Guest checkout versus mandatory account rules | Governs Customer or Visitor access; Return selects neither model. |
| Customer email-verification requirements | Governs any verification prerequisite; Return creates none. |
| Initial payment methods and provider | Affects Refund execution evidence; Payment remains authoritative. |
| Shipping provider, service levels, delivery areas, and fee policy | Affects reverse logistics; Return selects no provider or fee. |
| Free-delivery threshold and promotional treatment | Affects unresolved Return commercial treatment. |
| Tax-inclusive display and invoice requirements | Affects Refund and Credit Note context; Return defines no policy. |
| Cancellation eligibility and cutoff policy | Governs the boundary before Return; Return does not define it. |
| Returns, exchanges, and refund policy | Governs eligibility, windows, outcomes, and treatment. |
| Stock Reservation duration | May affect Exchange coordination; Return sets no duration. |
| Back-order and pre-order support | May affect eligible Order Item context and Exchange coordination. |
| Voucher and promotion stacking policy | Governs unresolved reversal or restoration treatment. |
| Customer-support channels and service expectations | Governs assisted Return and recovery service. |
| Marketing-consent and communication-preference model | Keeps transactional Return communication distinct from marketing. |
| Initial analytics provider and event taxonomy | Governs non-authoritative analytics integration. |
| Initial reporting and export requirements | Governs downstream representations and formats. |
| Administrative role and permission matrix | Governs protected Staff actions. |
| Production customer-service and operational escalation process | Governs exceptional recovery and escalation. |
| South African tax-display, invoice, and credit-note policy | Governs commercial-document treatment. |
| Fraud-screening approach and manual-review workflow | Governs fraud inputs and review. |
| Customer data export, correction, deletion, and account-closure workflow | Governs Return-linked privacy handling. |
| Gift cards, store credit, and promotional credit policy | Governs unresolved non-cash resolution or Refund treatment. |

## 29. Risks

| Risk | Implementation-neutral control direction |
| --- | --- |
| Unauthorized Return access | Enforce ownership, contextual Authorization, isolation, and safe disclosure. |
| Wrong Order Item association | Use authoritative Order identity and immutable history. |
| Excess or duplicate quantity | Correlate prior activity and enforce quantity invariants. |
| Current catalogue rewrites history | Use Order Snapshot evidence for purchased meaning. |
| Eligibility policy invented locally | Require Approved policy and explicit unresolved outcomes. |
| Authorization treated as external proof | Keep Refund, Shipping, Inventory, and communication outcomes separate. |
| Refund without governed input | Supply only approved inputs and preserve Payment authority. |
| Duplicate Refund effect | Use correlation, effect classification, idempotency, and reconciliation. |
| Receipt implies sellable Stock | Require Inventory-owned disposition execution and evidence. |
| Inventory disagreement | Preserve explicit uncertainty and reconcile with Inventory. |
| Shipping state becomes Return truth | Preserve physical Return and transport separation. |
| Exchange absorbs adjacent authority | Coordinate only through owning Domains. |
| Partial Return mismatch | Track quantities separately across Return, Inventory, and Payment. |
| Unexpected or substituted item | Preserve evidence, explicit outcome, and authorized resolution. |
| Unsafe retry or replay | Verify prior effects before repetition. |
| Concurrent or reordered evidence | Reject stale conflict and preserve accepted history. |
| Timeout or partial completion | Keep known, unknown, completed, and failed effects explicit. |
| Manual override bypasses rules | Require governed use case, Authorization, reason, and audit. |
| Sensitive evidence leakage | Minimize and protect images, notes, identifiers, exports, logs, and events. |
| Fraud control mutates truth | Keep fraud advisory and policy external with explainable recovery. |
| Projection becomes authoritative | Treat every downstream representation as derived and stale-capable. |
| Commercial-policy leakage | Defer Tax, credit, fee, Voucher, Promotion, and Refund rules. |
| Inaccessible or misleading status | Require accessible truthful pending, partial, failure, and uncertainty outcomes. |
| Reconciliation gap | Preserve cross-Domain correlation, provenance, visibility, and accountable repair. |

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
- `specifications/domains/order/order-domain.md`

## 31. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-04 | Draft | Initial comprehensive Return Domain Specification. |

## 32. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, scope is `RET`, and Draft non-normativity, governing precedence, and upstream authority are preserved;
2. Return authority and non-authority are explicit and transfer no adjacent truth;
3. Order, Order Item, Order Snapshot, Product, Customer, Pricing, Payment, Refund, Inventory, Shipping, and Administration boundaries remain intact;
4. Return identity, creation, items, quantities, eligibility, authorization, lifecycle, inspection, disposition, and history are complete and implementation-neutral;
5. Return-specific lifecycle meanings create no repository-wide vocabulary, complete graph, or universal traversal;
6. Return Authorization, receipt, inspection, disposition, or resolution independently proves no Refund, Stock, Shipment, Exchange, or communication effect;
7. eligibility, Return Window, Exchange, Refund, Restocking, reverse-logistics, commercial-document, fraud, privacy, and operational policy remain unresolved where Product governance requires;
8. failure, uncertainty, duplicate, retry, replay, concurrency, timeout, partial completion, recovery, and reconciliation preserve provenance and authority;
9. security, Authorization, Sensitive Data, accessibility, observability, audit, Contracts, conditional events, representations, and testing are complete;
10. every Requirement has exactly one clause-complete Acceptance Criteria row and one semantically valid traceability row;
11. all 30 Open Product Decisions were reviewed, exactly 21 materially relevant decisions appear in source order, and none is resolved;
12. terminology is canonical and no Glossary amendment is required;
13. Risks are Return-specific and controls remain implementation-neutral;
14. every Related Document exists and is relevant;
15. Revision History contains exactly one `0.1.0 Draft` row;
16. Markdown, tables, headings, whitespace, UTF-8, final newline, and `git diff --check` pass; and
17. Git scope contains only the untracked `specifications/domains/return/return-domain.md`, with nothing staged or otherwise modified.
