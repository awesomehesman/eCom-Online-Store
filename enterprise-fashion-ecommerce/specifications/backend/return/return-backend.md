---
title: Return Backend Specification
version: 0.1.0
status: Draft
owner: Backend
last_updated: 2026-09-23
authoritative: false
scope: BRET
---

# Return Backend Specification

## 1. Purpose

This Draft Specification defines implementation-facing backend obligations for the Approved Return Domain under scope `BRET`. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the Return backend scope, remain `authoritative: false`, and remain subordinate to governing sources, Approved Business Requirements, the Approved Return Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, Accepted ADR-0014, and applicable repository standards.

BRET uses the Return-only decomposition authorized by Accepted ADR-0014 immediately after BPAY as the thirteenth downstream Backend Specification after BEB. It consumes governed evidence without acquiring Order, Inventory, Shipping and Fulfilment, Payment, Pricing, Product, Category, Cart, Checkout, Identity, Customer, CMS, Administration, Notifications, Reporting, or other authority. It resolves no Open Product or Architecture Decision and establishes no post-BRET roadmap position.

## 2. Requirements

### BRET-REQ-001 — Lifecycle, Scope, and Authority

BRET MUST use scope `BRET`, remain `authoritative: false`, identify its `0.1.0 Draft` lifecycle, remain non-normative while Draft, specialize only the Approved Return Domain, preserve governing-source precedence, and claim no repository-wide authority.

### BRET-REQ-002 — Complete Return Domain Specialization

BRET MUST specialize every materially applicable `REQ-RET-001` through `REQ-RET-048` without weakening, duplicating, omitting, or transferring the Approved Return Domain's authority.

### BRET-REQ-003 — Decomposition and Roadmap Containment

BRET MUST use a Return-only decomposition, remain the final currently governed backend roadmap position, preserve CMS as independently eligible, separate, unresolved, unranked, and unordered, and MUST NOT establish or authorize any later Backend Specification identity, title, path, scope code, decomposition, or ordering.

### BRET-REQ-004 — BEB Inheritance

BRET MUST inherit and explicitly account for every `BEB-REQ-001` through `BEB-REQ-056`, apply every materially applicable obligation, preserve conditional applicability, and MUST NOT copy, weaken, contradict, or transfer BEB authority.

### BRET-REQ-005 — Modular and Hexagonal Boundary

BRET MUST preserve the modular-monolith boundary, hexagonal dependency direction, layer responsibilities, project-owned Ports, and dependency isolation governed by BEB without selecting extraction, provider, framework, or deployment mechanisms.

### BRET-REQ-006 — Public Contract and Use-Case Boundary

BRET MUST expose only intentional abstract public Contracts and explicit Return-owned Use Cases, distinguish commands from queries, validate governed context and bounds, translate failures safely, and prevent transport, provider, persistence, or framework types from owning Return policy.

### BRET-REQ-007 — Return Persistence and Historical Truth

BRET MUST own only Return persistence and preserve applicable Return, Return Request, Return Item, eligibility-outcome, Return Authorization, lifecycle, inspection, disposition, exception, recovery, and reconciliation history through project-owned persistence boundaries without selecting schemas, tables, columns, indexes, ORM mappings, identifier formats, or migration designs.

### BRET-REQ-008 — Transaction and External-Effect Boundary

BRET MUST use explicit focused local transaction ownership, keep network and cross-Domain effects outside long-running Database Transactions where a reliable alternative exists, and distinguish local commit, requested external effect, partial completion, timeout, and unknown outcome without implying distributed atomicity.

### BRET-REQ-009 — Identity, Customer, and Authorization Consumption

BRET MAY consume trusted Principal, Authentication, Customer, Visitor, Account, ownership, contact, and Address evidence where materially required, but Identity, credentials, Sessions, Customer, Account, Consent, Preference, and source authority MUST remain with BIDN and BCUS; Authentication or association alone MUST NOT authorize a Return action.

### BRET-REQ-010 — Order Evidence Boundary

BRET MUST associate a Return with an existing authoritative Order and applicable Order Items through trusted BORD evidence, use immutable Order Snapshot and history for purchased context, and MUST NOT create, mutate, recalculate, or redefine Order identity, items, lifecycle, cancellation, snapshots, quantities, or historical commercial truth.

### BRET-REQ-011 — Stable Return Identity and Creation Truth

BRET MUST establish stable unique Return identity, Return Request correlation, and Return-owned durable creation truth without selecting an identifier mechanism; identifiers, submissions, client state, support contact, Shipment movement, or Refund requests MUST NOT independently establish existence, association, authority, or creation.

### BRET-REQ-012 — Creation Outcome Integrity

BRET MUST distinguish receipt, validation, acceptance for processing, creation, rejection, duplication, failure, and uncertainty, preserve correlation and Authorization, and prevent retries, replay, or concurrent requests from fabricating or multiplying Return creation.

### BRET-REQ-013 — Return Item and Quantity Integrity

BRET MUST preserve each Return Item's governed Order Item association, reason, evidence, requested resolution, and requested, authorized, expected, received, inspected, accepted, rejected, missing, excess, and resolved quantities where applicable while preventing quantities from exceeding authoritative purchased and prior-return constraints.

### BRET-REQ-014 — Eligibility Policy Deferral

BRET MUST evaluate eligibility only under Approved policy and authoritative context and MUST NOT invent Return Window values, eligible categories or conditions, exclusions, fees, evidence thresholds, responsibility, reason policy, inspection policy, Exchange rules, Refund eligibility, Refund amount, or other Return treatment.

### BRET-REQ-015 — Eligibility Outcomes and Return Authorization

BRET MUST make eligible, ineligible, pending-review, denied, unavailable, stale, conflicting, and uncertain outcomes explicit and explainable, and MUST issue Return Authorization only from governed eligible evidence with applicable items, quantities, conditions, scope, actor, and provenance while distinguishing it from access-control Authorization.

### BRET-REQ-016 — External-Effect Non-Proof

Return Authorization, receipt, inspection, disposition, resolution, cancellation, UI state, or support evidence MUST NOT independently prove Refund approval or execution, Refund Transaction, Shipment creation or completion, Restocking, Stock availability, Exchange completion, Order mutation, Customer communication, or any other external effect.

### BRET-REQ-017 — Return-Scoped Lifecycle and History

BRET MUST preserve the Approved Return-scoped meanings for Requested, Pending Review, Authorized, Rejected, Awaiting Receipt, Received, Under Inspection, Resolved, and Cancelled; validate current state, policy, Authorization, quantity invariants, evidence, concurrency, and dependencies for accepted transitions; retain cause, time, prior state, outcome, and uncertainty; and MUST NOT invent a mandatory traversal or external truth.

### BRET-REQ-018 — Customer and Staff Initiation

Customer, Visitor, and Staff initiation or assistance MUST use trusted Order association, contextual Authorization, policy-permitted capability, safe disclosure, accessible outcomes, least privilege, reason and proportional audit where material, Customer isolation, explicit outcomes, and reconciliation without selecting guest, Account, verification, Role, Permission, approval, support, or escalation policy.

### BRET-REQ-019 — Receipt and Inspection Evidence

BRET MAY retain trusted receipt and inspection evidence with provenance, quantities, condition observations, actor, time, and uncertainty, but MUST NOT treat physical receipt or observation as independent proof of eligibility, acceptance, disposition, Refund, sellable Stock, or external completion.

### BRET-REQ-020 — Disposition and Exceptional Receipt

BRET MAY assign a governed Return Disposition after applicable inspection while deferring disposition policy and Inventory execution; partial, missing, excess, substituted, damaged, unexpected, rejected, mixed-condition, duplicate, and uncertain outcomes MUST remain explicit, quantity-safe, historically traceable, and reconcilable without fabricated restock, quarantine, repair, supplier-return, disposal, or other treatment.

### BRET-REQ-021 — Exchange Non-Authority

BRET MAY coordinate a future governed Exchange outcome but MUST NOT define Exchange eligibility, select a replacement Product or Product Variant, calculate a Price difference, reserve Stock, create an Order or Payment, execute Shipping, or imply Exchange completion.

### BRET-REQ-022 — Payment and Refund Boundary

BRET MAY provide governed Return eligibility, items, quantities, reasons, disposition, explicit Money and Currency, and historical evidence to BPAY and request governed Refund execution, but MUST NOT calculate Refund amount, determine payment-side approval, execute a Refund, own Refund Transaction or provider truth, or treat Return state as financial outcome; request, processing, success, failure, rejection, partial, and uncertainty MUST remain distinct, correlated, duplicate-safe, and reconcilable.

### BRET-REQ-023 — Inventory and Restocking Boundary

BRET MAY provide governed disposition and inspected-quantity evidence to BINV and consume governed outcomes, but Inventory MUST retain Stock, Stock Reservation, Stock Location, Stock condition where applicable, Stock Adjustment, Stock Movement, Restocking effects, Available-to-Sell, and resulting-state authority; physical receipt MUST NOT make Stock sellable, and failure, partial completion, duplication, disagreement, and uncertainty MUST remain explicit and reconcilable.

### BRET-REQ-024 — Shipping and Reverse-Logistics Boundary

BRET MAY provide authorized Return context to BSHP and consume governed reverse-logistics outcomes, but Shipping and Fulfilment MUST retain Carrier, Shipment, label, tracking, transport, provider, receipt-transport, exception, and reconciliation authority; forward Shipping, returned-to-sender, Customer Return transport, delivery, receipt, and Return lifecycle MUST remain distinct.

### BRET-REQ-025 — Product, Pricing, Tax, and Credit Boundary

BRET MUST use historical Order Item meaning for purchased context and MUST NOT calculate or own Product or Product Variant truth, Price, Discount, Promotion, Voucher restoration, Refund amount, Tax adjustment, shipping fee, Exchange difference, gift card, store credit, promotional credit, invoice, or Credit Note treatment; current catalogue changes MUST NOT rewrite Return or Order history.

### BRET-REQ-026 — Sensitive Data and Privacy Boundary

BRET MUST preserve Customer or Visitor association without owning identity, minimize and purpose-limit contact, Address, reason, evidence, images, notes, identifiers, and payment-adjacent data, and protect them across persistence, Contracts, events, logs, errors, exports, Projections, audit, and support without selecting retention, deletion, export, correction, or account-closure policy or mechanism.

### BRET-REQ-027 — Cancellation and Adjacent Outcome Separation

Order cancellation, Return cancellation, Shipment cancellation, returned-to-sender, Return, Exchange, Refund, and Refund Transaction MUST remain distinct; BRET MUST NOT invent cancellation eligibility or automatically convert another Domain's state or evidence into a Customer Return.

### BRET-REQ-028 — Failure and Uncertainty

BRET MUST distinguish invalid association, ineligibility, excessive or duplicate quantity, stale policy or evidence, denial, dependency failure, timeout, reordered outcome, partial completion, inspection disagreement, unknown external effect, and reconciliation failure without fabricated success or failure.

### BRET-REQ-029 — Controlled Recovery and Reconciliation

BRET recovery MUST preserve correlation, provenance, authoritative evidence, history, quantity and duplicate safety, contextual Authorization, observability, and resolved or still-uncertain outcomes and MUST NOT directly rewrite another Domain's record or blindly repeat an uncertain external effect.

### BRET-REQ-030 — Authorization and Tamper Resistance

Protected Return access and action MUST require trusted server-side contextual Authorization for Principal, Resource, action, ownership, association, and current state, preserve object isolation and safe concealment, validate untrusted evidence, and MUST NOT infer permission from UI visibility, route, Role label, Claims, supplied identifiers, uploads, or Authentication alone.

### BRET-REQ-031 — Fraud and Review Non-Authority

BRET MAY consume governed fraud or review outcomes but MUST NOT define signals, scores, thresholds, models, provider, blocking policy, manual-review workflow, or automatic commercial effect; suspicious activity MUST preserve authoritative state, privacy, explainability, recovery, and proportional audit.

### BRET-REQ-032 — Contract Compatibility and Evolution

Future BRET Contracts MUST preserve Return and external authority, identity, correlation, quantities, explicit outcomes, compatibility, Authorization, Sensitive Data protection, duplicate, retry, replay, concurrency, failure, and uncertainty; changes MUST evolve safely without defining concrete routes, methods, status codes, fields, DTOs, classes, schemas, or provider payloads here.

### BRET-REQ-033 — Conditional Event Boundary

Only where Architecture, an Approved Contract, reliability, or synchronization governance requires an event MAY governed Return fact meanings be exposed. Any such event MUST represent a completed fact and preserve source authority, correlation, compatibility, privacy, replay safety, ordering uncertainty, duplicate safety, and non-authoritative consumer semantics without selecting an event name, payload, transport, topic, queue, broker, or messaging technology.

### BRET-REQ-034 — Accessible Return Outcomes

Applicable Customer and Staff Return initiation, item selection, reason and evidence input, review, authorization, transport, inspection, status, denial, failure, recovery, and uncertainty surfaces MUST support the Approved accessibility outcomes without selecting UI design, wording, route, or transport mechanisms.

### BRET-REQ-035 — Bounded and Observable Operations

Return creation, review, authorization, receipt, inspection, disposition, external coordination, failure, recovery, and reconciliation MUST be bounded, correlated, attributable, observable, and health-assessable without defining latency, timeout, retry, capacity, rate, SLA, SLO, device, network, provider, or recovery targets.

### BRET-REQ-036 — Proportional Audit Records

Material eligibility decisions, Return Authorization, rejection, disposition, administrative intervention, financial coordination, sensitive evidence access, correction, recovery, reconciliation, and exceptional action MUST produce protected, attributable, tamper-resistant, proportionate Audit Records while routine reads MUST NOT automatically be treated as high-Risk.

### BRET-REQ-037 — Non-Authoritative Representations

Notifications, support views, reports, analytics, exports, and Projections MUST derive from governed facts, identify source where applicable, protect data, expose delay or staleness, survive delivery failure, and MUST NOT mutate or replace Return or upstream authority.

### BRET-REQ-038 — Complete Verification

Verification evidence MUST cover applicable positive, negative, boundary, identity, association, quantity, eligibility, Authorization, lifecycle, inspection, disposition, Exchange, Refund, Inventory, Shipping, Customer, cancellation, resilience, concurrency, security, fraud, accessibility, performance, audit, Contract, event, representation, and BEB behavior without selecting tools, identifiers, mechanisms, or numerical coverage targets.

### BRET-REQ-039 — Open-Decision Preservation

BRET MUST preserve exactly the 21 materially applicable Open Product Decisions and 11 materially applicable Open Architecture Decisions recorded by Accepted ADR-0014 as unresolved and MUST NOT imply a decision through requirements, examples, tests, configuration, or operational wording.

### BRET-REQ-040 — Implementation Neutrality

BRET MUST NOT select any concrete API, HTTP behavior, DTO, payload, database design, ORM mapping, identifier format, event design, messaging mechanism, provider, cache, infrastructure product, hosting topology, numerical retry, timeout, TTL, retention, rate, SLA, SLO, recovery objective, Role/Permission matrix, or unresolved Product policy.

### BRET-REQ-041 — Configuration and Feature-State Safety

BRET configuration and any future feature-controlled state MUST be validated, protected, observable, safely defaulted, and compatible with reachable states without selecting a configuration store, flag provider, rollout mechanism, or lifecycle policy and without making configuration the authority for Return truth.

### BRET-REQ-042 — Migration and Deployment Compatibility

BRET data and Contract evolution MUST preserve Return history, authority, rollback or forward-recovery safety where governed, mixed-version compatibility, and bounded failure containment without selecting migration tooling, deployment topology, schema strategy, or sequencing mechanism.

### BRET-REQ-043 — External Adapter and Provider Neutrality

Any governed external interaction MUST use project-owned Ports, validate and preserve authoritative evidence, distinguish dependency failure and uncertainty, and support recovery and reconciliation without selecting providers, SDKs, protocols, adapters, infrastructure, or direct dependency types.

### BRET-REQ-044 — Data Integrity and Concurrency

BRET MUST enforce Return-owned invariants at authoritative boundaries, prevent lost updates and write skew where material, preserve durable history and referential meaning, and make conflicts explicit without selecting a locking strategy, database schema, transaction primitive, or persistence technology.

### BRET-REQ-045 — Idempotency, Retry, and Replay Safety

Creation and every operation capable of multiplying a Return, quantity, financial request, Inventory request, Shipping request, notification fact, or other external effect MUST preserve stable correlation, effect classification, applicable idempotency semantics, retry and replay safety, duplicate detection, and uncertainty reconciliation without selecting keys, storage, TTLs, or retry counts.

### BRET-REQ-046 — Security, Secrets, and Evidence Protection

BRET MUST apply least privilege, input and evidence validation, Sensitive Data minimization, safe errors, logging protection, Secret isolation, and dependency-boundary controls and MUST NOT expose protected Return, Customer, fraud, Payment, Shipping, Inventory, or operational data through unauthorized Contracts, diagnostics, Audit Records, or representations.

### BRET-REQ-047 — Operational Support and Health

BRET MUST provide implementation-neutral correlated diagnostic evidence sufficient to distinguish Return-owned failure from upstream or downstream dependency failure, support safe investigation and recovery, and expose truthful health without defining support channels, escalation workflows, telemetry products, dashboards, or numerical targets.

### BRET-REQ-048 — Downstream Dependency Containment

During BRET Draft, Administration MUST remain blocked by missing Approved CMS, Return, Notifications, and Reporting invocation Contracts; Notifications MUST remain blocked by an Approved Return producer Contract; Reporting MUST remain blocked by Approved Return and CMS source Contracts. Draft BRET closes none of these dependencies, authorizes no downstream Specification, and creates no downstream Contract authority.

### BRET-REQ-049 — Future Approval Effects Remain Conditional

A future Approved BRET MAY close only applicable Return-side prerequisites for Administration, Notifications, and Reporting. Such approval MUST NOT close CMS, Notifications, Reporting, or other missing dependencies, authorize another Draft, or establish a later roadmap position.

### BRET-REQ-050 — Governance Completeness

BRET MUST maintain complete Requirement-to-Acceptance-Criterion traceability, Return Domain coverage, BEB accounting, open-decision inventories, authority matrices, pending governance reviews, Related Documents, Revision History, and lifecycle-correct final validation.

## 3. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BRET-AC-001 | BRET-REQ-001 | Metadata and scope review show `0.1.0 Draft`, `authoritative: false`, scope `BRET`, non-normative Draft status, bounded authority, and no repository-wide claim. |
| BRET-AC-002 | BRET-REQ-002 | The Return coverage matrix accounts for every `REQ-RET-001` through `REQ-RET-048` without omission or authority transfer. |
| BRET-AC-003 | BRET-REQ-003 | BRET is Return-only, follows BPAY, preserves CMS status, and establishes no later identity or position. |
| BRET-AC-004 | BRET-REQ-004 | The BEB matrix accounts for `BEB-REQ-001` through `BEB-REQ-056`, with every applicable obligation inherited. |
| BRET-AC-005 | BRET-REQ-005 | Architecture evidence preserves module, hexagonal, layer, Port, and dependency boundaries without mechanism selection. |
| BRET-AC-006 | BRET-REQ-006 | Public Contracts and Use Cases are intentional, bounded, validated, failure-safe, and free of transport or framework authority. |
| BRET-AC-007 | BRET-REQ-007 | Persistence evidence contains only Return-owned truth and complete history with no concrete storage design. |
| BRET-AC-008 | BRET-REQ-008 | Transaction tests distinguish local commit and external effects, including partial and unknown outcomes, without distributed-atomicity assumptions. |
| BRET-AC-009 | BRET-REQ-009 | Identity and Customer evidence is consumed without authority transfer, and Authentication or association alone authorizes nothing. |
| BRET-AC-010 | BRET-REQ-010 | Every Return uses trusted BORD evidence and cannot mutate or recalculate Order-owned truth. |
| BRET-AC-011 | BRET-REQ-011 | Stable Return identity and durable creation evidence are Return-owned, mechanism-neutral, isolated, and not inferred from untrusted references. |
| BRET-AC-012 | BRET-REQ-012 | Each creation outcome remains distinguishable, correlated, authorized, and duplicate-safe under retry, replay, concurrency, failure, and uncertainty. |
| BRET-AC-013 | BRET-REQ-013 | Return Items and all applicable quantity classes remain distinct and within authoritative purchased and prior-return limits. |
| BRET-AC-014 | BRET-REQ-014 | Eligibility uses only Approved policy and contains none of the prohibited invented policies, values, or rules. |
| BRET-AC-015 | BRET-REQ-015 | Eligibility outcomes are explicit and Return Authorization is issued only from governed evidence and remains distinct from access Authorization. |
| BRET-AC-016 | BRET-REQ-016 | No Return-owned or client-visible state independently proves an external effect or another Domain's truth. |
| BRET-AC-017 | BRET-REQ-017 | Lifecycle evidence uses only Approved Return-scoped meanings, validates transitions, retains history, and implies neither universal traversal nor external truth. |
| BRET-AC-018 | BRET-REQ-018 | Customer, Visitor, and Staff initiation preserves association, contextual Authorization, isolation, accessibility, audit, and unresolved policy. |
| BRET-AC-019 | BRET-REQ-019 | Receipt and inspection evidence has provenance but independently proves no eligibility, disposition, Refund, Stock, or external outcome. |
| BRET-AC-020 | BRET-REQ-020 | Disposition and exceptional receipt outcomes are explicit, quantity-safe, traceable, and policy-neutral, with no fabricated Inventory treatment. |
| BRET-AC-021 | BRET-REQ-021 | Exchange coordination absorbs none of the listed Product, Pricing, Inventory, Order, Payment, or Shipping authorities. |
| BRET-AC-022 | BRET-REQ-022 | Refund coordination supplies only governed inputs and requests, preserves BPAY execution truth, distinguishes outcomes, and prevents duplicate financial effects. |
| BRET-AC-023 | BRET-REQ-023 | BINV retains every Inventory and restocking authority; Return evidence cannot independently create sellable Stock or hide uncertain effects. |
| BRET-AC-024 | BRET-REQ-024 | BSHP retains reverse-logistics authority and every Shipping, transport, receipt, and Return state remains distinct. |
| BRET-AC-025 | BRET-REQ-025 | Historical purchased meaning remains stable and BRET performs none of the prohibited catalogue, Pricing, tax, fee, credit, or document calculations. |
| BRET-AC-026 | BRET-REQ-026 | Sensitive Data is minimized, purpose-limited, protected on every surface, and governed privacy policy remains unresolved. |
| BRET-AC-027 | BRET-REQ-027 | Cancellation, returned-to-sender, Return, Exchange, Refund, and Refund Transaction outcomes remain distinct without invented policy. |
| BRET-AC-028 | BRET-REQ-028 | Failure evidence distinguishes every listed cause and unknown state without fabricated success or failure. |
| BRET-AC-029 | BRET-REQ-029 | Recovery and reconciliation preserve evidence, history, quantity, duplicate safety, Authorization, observability, and external authority. |
| BRET-AC-030 | BRET-REQ-030 | Protected operations enforce server-side contextual Authorization, object isolation, safe concealment, and tamper resistance without relying on client or identity evidence alone. |
| BRET-AC-031 | BRET-REQ-031 | Fraud and review evidence remains external and no signal, score, threshold, provider, workflow, or commercial effect is invented. |
| BRET-AC-032 | BRET-REQ-032 | Contract review preserves all governed semantics and compatibility while containing no concrete transport, DTO, persistence, or provider design. |
| BRET-AC-033 | BRET-REQ-033 | Any governed event is a completed authoritative fact with compatibility, protection, replay, ordering, duplicate, and consumer non-authority safeguards and no selected design. |
| BRET-AC-034 | BRET-REQ-034 | Applicable surfaces satisfy governed accessibility outcomes without selecting UI or transport implementation. |
| BRET-AC-035 | BRET-REQ-035 | Operations are bounded, correlated, attributable, observable, and health-assessable without numerical or technology commitments. |
| BRET-AC-036 | BRET-REQ-036 | Material actions produce protected proportional Audit Records while ordinary reads are not automatically classified high-Risk. |
| BRET-AC-037 | BRET-REQ-037 | Every representation preserves source, protection, staleness, failure tolerance, and non-authority. |
| BRET-AC-038 | BRET-REQ-038 | Verification covers the complete listed behavior and BEB surface without invented tools or numerical targets. |
| BRET-AC-039 | BRET-REQ-039 | Exactly 21 Product and 11 Architecture Decisions remain exact and unresolved throughout BRET. |
| BRET-AC-040 | BRET-REQ-040 | No prohibited policy, API, data, event, provider, cache, infrastructure, numerical, or authorization mechanism is selected. |
| BRET-AC-041 | BRET-REQ-041 | Configuration and future feature states are safe and observable without becoming Return authority or selecting a mechanism. |
| BRET-AC-042 | BRET-REQ-042 | Evolution evidence preserves history, authority, compatibility, recovery, and bounded failure without selecting migration or deployment design. |
| BRET-AC-043 | BRET-REQ-043 | External interactions use project-owned boundaries and preserve evidence, failure, uncertainty, recovery, and provider neutrality. |
| BRET-AC-044 | BRET-REQ-044 | Concurrency evidence preserves Return invariants, history, and explicit conflicts without a selected database or locking design. |
| BRET-AC-045 | BRET-REQ-045 | Effect-capable operations remain correlated, idempotent where applicable, retry/replay-safe, duplicate-safe, and reconcilable without selected values or mechanisms. |
| BRET-AC-046 | BRET-REQ-046 | Security evidence demonstrates least privilege, validation, minimization, safe errors, Secret isolation, and protected diagnostics across all listed data classes. |
| BRET-AC-047 | BRET-REQ-047 | Diagnostic evidence distinguishes ownership and dependency failures and supports safe investigation, recovery, and truthful health without provider or target choices. |
| BRET-AC-048 | BRET-REQ-048 | Draft BRET closes none of the exact Administration, Notifications, or Reporting dependencies and authorizes no downstream specification or Contract authority. |
| BRET-AC-049 | BRET-REQ-049 | Future BRET approval effects remain conditional, Return-side only, and establish no other eligibility or roadmap position. |
| BRET-AC-050 | BRET-REQ-050 | Counts, mappings, matrices, decision inventories, reviews, references, history, and final validation are complete and internally consistent. |

## 4. Requirement Traceability

| Requirement | Return Domain source | BEB source | Approved backend / governing source | Acceptance Criterion |
| --- | --- | --- | --- | --- |
| BRET-REQ-001 | REQ-RET-001 | BEB-REQ-001–004 | ADR-0014; ARCHITECTURE.md §35 | BRET-AC-001 |
| BRET-REQ-002 | REQ-RET-001–048 | BEB-REQ-002–004, 056 | Approved Return Domain | BRET-AC-002 |
| BRET-REQ-003 | REQ-RET-001, 003 | BEB-REQ-003–004, 056 | ADR-0014; ARCHITECTURE.md §35 | BRET-AC-003 |
| BRET-REQ-004 | REQ-RET-001, 048 | BEB-REQ-001–056 | Approved BEB | BRET-AC-004 |
| BRET-REQ-005 | REQ-RET-001, 042 | BEB-REQ-005–007 | ARCHITECTURE.md §§5, 35; SPRING.md | BRET-AC-005 |
| BRET-REQ-006 | REQ-RET-010, 042 | BEB-REQ-008–018, 041 | API.md; SPRING.md | BRET-AC-006 |
| BRET-REQ-007 | REQ-RET-002, 004, 009, 018–020, 023–025, 038–039, 046 | BEB-REQ-021–024, 049–050 | DATABASE.md; POSTGRES.md | BRET-AC-007 |
| BRET-REQ-008 | REQ-RET-010, 013, 017, 029, 031, 038–039 | BEB-REQ-012, 025–036 | BINV; BSHP; BPAY | BRET-AC-008 |
| BRET-REQ-009 | REQ-RET-005, 021–022, 036, 040 | BEB-REQ-011, 019–020, 043–044 | BIDN; BCUS; SECURITY-STANDARDS.md | BRET-AC-009 |
| BRET-REQ-010 | REQ-RET-006–008 | BEB-REQ-008, 021–024, 041 | BORD | BRET-AC-010 |
| BRET-REQ-011 | REQ-RET-004–005, 009 | BEB-REQ-009–011, 021–024 | BORD; BCUS | BRET-AC-011 |
| BRET-REQ-012 | REQ-RET-010, 013 | BEB-REQ-025–031, 041–042 | API.md; DATABASE.md | BRET-AC-012 |
| BRET-REQ-013 | REQ-RET-011–013 | BEB-REQ-021–031, 041–042 | BORD | BRET-AC-013 |
| BRET-REQ-014 | REQ-RET-014 | BEB-REQ-003–004, 056 | Product decisions 4, 7–8, 10–14, 29 | BRET-AC-014 |
| BRET-REQ-015 | REQ-RET-015–016 | BEB-REQ-008–012, 021–024, 041 | Return Domain §§9–10 | BRET-AC-015 |
| BRET-REQ-016 | REQ-RET-017, 020 | BEB-REQ-003–004, 021, 041–042 | BORD; BINV; BSHP; BPAY | BRET-AC-016 |
| BRET-REQ-017 | REQ-RET-018–020 | BEB-REQ-021–031, 041–046 | Return Domain §10 | BRET-AC-017 |
| BRET-REQ-018 | REQ-RET-021–022 | BEB-REQ-009–011, 019–020, 043–046 | BIDN; BCUS; Administration | BRET-AC-018 |
| BRET-REQ-019 | REQ-RET-023 | BEB-REQ-021–024, 041–046 | BINV; BSHP | BRET-AC-019 |
| BRET-REQ-020 | REQ-RET-024–025 | BEB-REQ-021–031, 041–046 | BINV | BRET-AC-020 |
| BRET-REQ-021 | REQ-RET-026 | BEB-REQ-003–004, 021, 056 | BPRD; BPRC; BINV; BORD; BPAY; BSHP | BRET-AC-021 |
| BRET-REQ-022 | REQ-RET-027–029 | BEB-REQ-021–036, 041–046 | BPAY; BPRC | BRET-AC-022 |
| BRET-REQ-023 | REQ-RET-030–031 | BEB-REQ-021–036, 041–046 | BINV | BRET-AC-023 |
| BRET-REQ-024 | REQ-RET-032–033 | BEB-REQ-021–036, 041–046 | BSHP | BRET-AC-024 |
| BRET-REQ-025 | REQ-RET-034–035 | BEB-REQ-003–004, 021–024, 056 | BORD; BPRD; BPRC | BRET-AC-025 |
| BRET-REQ-026 | REQ-RET-036 | BEB-REQ-019–024, 043–046, 049–051 | BCUS; SECURITY-STANDARDS.md | BRET-AC-026 |
| BRET-REQ-027 | REQ-RET-037 | BEB-REQ-003–004, 021, 041–042, 056 | BORD; BSHP; BPAY | BRET-AC-027 |
| BRET-REQ-028 | REQ-RET-038 | BEB-REQ-028–036, 041–046 | BORD; BINV; BSHP; BPAY | BRET-AC-028 |
| BRET-REQ-029 | REQ-RET-039 | BEB-REQ-025–036, 041–046 | BORD; BINV; BSHP; BPAY | BRET-AC-029 |
| BRET-REQ-030 | REQ-RET-005, 021–022, 040 | BEB-REQ-011, 019–020, 043–046 | BIDN; BCUS; SECURITY-STANDARDS.md | BRET-AC-030 |
| BRET-REQ-031 | REQ-RET-041 | BEB-REQ-003–004, 011, 043–046, 056 | Product decision 26 | BRET-AC-031 |
| BRET-REQ-032 | REQ-RET-042 | BEB-REQ-013–018, 032, 041, 049–050 | API.md; DOCUMENTATION-STANDARDS.md | BRET-AC-032 |
| BRET-REQ-033 | REQ-RET-043 | BEB-REQ-037–040, 043 | EVENTS.md; Notifications Domain | BRET-AC-033 |
| BRET-REQ-034 | REQ-RET-044 | BEB-REQ-003, 008, 051, 055 | Return Domain §23 | BRET-AC-034 |
| BRET-REQ-035 | REQ-RET-045 | BEB-REQ-017–018, 041–046, 049–051 | TESTING-STANDARDS.md | BRET-AC-035 |
| BRET-REQ-036 | REQ-RET-022, 046 | BEB-REQ-020, 043–046 | SECURITY-STANDARDS.md | BRET-AC-036 |
| BRET-REQ-037 | REQ-RET-047 | BEB-REQ-003–004, 021, 037–043, 051 | Notifications; Reporting | BRET-AC-037 |
| BRET-REQ-038 | REQ-RET-048 | BEB-REQ-052–055 | TESTING-STANDARDS.md | BRET-AC-038 |
| BRET-REQ-039 | REQ-RET-001–048 | BEB-REQ-040, 047–048, 056 | PRODUCT.md §24; ARCHITECTURE.md §34; ADR-0014 | BRET-AC-039 |
| BRET-REQ-040 | REQ-RET-014, 022, 026, 035–036, 041–043, 045, 048 | BEB-REQ-003–004, 040, 047–056 | ADR-0014; backend standards | BRET-AC-040 |
| BRET-REQ-041 | REQ-RET-014, 038–039, 045 | BEB-REQ-047–048 | ARCHITECTURE.md §34 items 8, 14 | BRET-AC-041 |
| BRET-REQ-042 | REQ-RET-002, 018–020, 039, 042 | BEB-REQ-049–051 | DATABASE.md; POSTGRES.md | BRET-AC-042 |
| BRET-REQ-043 | REQ-RET-029, 031–033, 038–039, 042–043 | BEB-REQ-032–040, 041–043 | BSHP; BPAY; EVENTS.md | BRET-AC-043 |
| BRET-REQ-044 | REQ-RET-010, 012–013, 019, 025, 038–039 | BEB-REQ-021–031, 041–042 | DATABASE.md; POSTGRES.md | BRET-AC-044 |
| BRET-REQ-045 | REQ-RET-010, 013, 029, 031, 038–039, 042–043 | BEB-REQ-025–031, 035–039, 041–042 | BINV; BSHP; BPAY | BRET-AC-045 |
| BRET-REQ-046 | REQ-RET-005, 022, 036, 040–043, 046–047 | BEB-REQ-019–020, 043–046 | SECURITY-STANDARDS.md | BRET-AC-046 |
| BRET-REQ-047 | REQ-RET-038–039, 045–047 | BEB-REQ-041–046, 051, 054 | ENGINEERING-PRINCIPLES.md; TESTING-STANDARDS.md | BRET-AC-047 |
| BRET-REQ-048 | REQ-RET-003, 042–043, 047 | BEB-REQ-003–004, 037–040, 056 | ADR-0014; Administration; Notifications; Reporting | BRET-AC-048 |
| BRET-REQ-049 | REQ-RET-003, 042–043, 047 | BEB-REQ-003–004, 056 | ADR-0014; ARCHITECTURE.md §35 | BRET-AC-049 |
| BRET-REQ-050 | REQ-RET-001–048 | BEB-REQ-001–056 | ADR-0014; DOCUMENTATION-STANDARDS.md | BRET-AC-050 |

## 5. Return Domain Coverage Matrix

| Return Domain Requirement(s) | BRET specialization / classification |
| --- | --- |
| REQ-RET-001–003 | BRET-REQ-001–003, 009–010, 016, 021–027, 037, 048–050 — lifecycle, Return authority, cross-Domain non-authority, and roadmap containment. |
| REQ-RET-004–005 | BRET-REQ-011, 030, 044 — stable identity, reference non-authority, isolation, and safe access. |
| REQ-RET-006–008 | BRET-REQ-010 — trusted BORD association, immutable history, and Order non-mutation. |
| REQ-RET-009–010 | BRET-REQ-011–012 — Return-owned creation truth and explicit creation outcomes. |
| REQ-RET-011–013 | BRET-REQ-013, 044–045 — item identity, quantity integrity, concurrency, retry, replay, and duplicate safety. |
| REQ-RET-014–017 | BRET-REQ-014–016, 039–040 — policy deferral, explicit eligibility, Return Authorization, and external-effect non-proof. |
| REQ-RET-018–020 | BRET-REQ-007, 017, 044 — Return-scoped lifecycle, controlled history, and cross-Domain integrity. |
| REQ-RET-021–022 | BRET-REQ-009, 018, 030, 036 — Customer and Staff initiation, contextual Authorization, isolation, and audit. |
| REQ-RET-023–025 | BRET-REQ-019–020, 023, 044 — receipt, inspection, disposition, exceptional outcomes, and Inventory non-authority. |
| REQ-RET-026 | BRET-REQ-021 — Exchange coordination and external authority preservation. |
| REQ-RET-027–029 | BRET-REQ-022, 028–029, 045 — governed Refund input, Return/Refund independence, financial duplicate safety, and reconciliation. |
| REQ-RET-030–031 | BRET-REQ-023, 028–029, 045 — Inventory/restocking boundary and coordination safety. |
| REQ-RET-032–033 | BRET-REQ-024, 028–029, 043, 045 — reverse-logistics handoff and transport/Return separation. |
| REQ-RET-034–035 | BRET-REQ-025 — historical Product meaning and commercial non-authority. |
| REQ-RET-036 | BRET-REQ-009, 026, 046 — Customer association, privacy, Sensitive Data, and protection. |
| REQ-RET-037 | BRET-REQ-027 — cancellation and adjacent-outcome separation. |
| REQ-RET-038–039 | BRET-REQ-008, 028–029, 035, 043–047 — failure, uncertainty, recovery, reconciliation, and operations. |
| REQ-RET-040–041 | BRET-REQ-030–031, 036, 046 — contextual Authorization, tamper resistance, fraud non-authority, and audit. |
| REQ-RET-042–043 | BRET-REQ-006, 032–033, 043, 045 — Contracts, compatibility, conditional events, and mechanism neutrality. |
| REQ-RET-044 | BRET-REQ-018, 034 — accessible Customer and Staff outcomes. |
| REQ-RET-045–047 | BRET-REQ-035–037, 046–047 — bounded operations, observability, Audit Records, and non-authoritative representations. |
| REQ-RET-048 | BRET-REQ-038, 050 — complete verification and traceability. |

Every `REQ-RET-001` through `REQ-RET-048` is specialized; none is omitted or classified in a way that transfers external authority.

## 6. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BRET application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, Domain non-authority, specialization, and roadmap containment. BRET-REQ-001–004, 039–040, 048–050. |
| BEB-REQ-005–007 | Applicable | Modular-monolith, hexagonal dependency, and layer responsibilities. BRET-REQ-005–006. |
| BEB-REQ-008–010 | Applicable | Public Contracts, Use Cases, and command/query semantics. BRET-REQ-006, 009–38. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and application transaction ownership. BRET-REQ-008–10, 018, 030, 044–046. |
| BEB-REQ-013–018 | Applicable where an external API or collection Contract exists | Versioning, DTO separation, validation, bounded queries, Problem Details, description, and evolution remain inherited without selecting routes or DTOs. BRET-REQ-006, 032, 040, 048. |
| BEB-REQ-019–020 | Applicable | BIDN supplies Authentication truth; BRET consumes trusted evidence and enforces contextual Authorization, isolation, and concealment. BRET-REQ-009, 018, 030, 046. |
| BEB-REQ-021–024 | Applicable | Return data ownership, persistence integrity, and historical truth. BRET-REQ-007, 010–29, 32, 36–37, 42, 44. |
| BEB-REQ-025–031 | Applicable | Atomicity, external-effect separation, concurrency, durable uncertainty, idempotency, replay, and duplicate safety. BRET-REQ-008, 012–13, 17, 20, 22–24, 28–29, 44–45. |
| BEB-REQ-032–034 | Applicable | External Ports, resilience, uncertainty, and reconciliation apply while provider choices remain unresolved. BRET-REQ-008, 022–24, 28–29, 43, 45. |
| BEB-REQ-035–036 | Conditionally applicable | Any governed Callback or Webhook evidence inherits authenticity, integrity, replay, duplicate, and recovery safeguards without selecting a protocol. BRET-REQ-022–24, 28–30, 43, 45–46. |
| BEB-REQ-037–039 | Conditionally applicable | Any governed Domain or Integration Event inherits separation, envelope, delivery, replay, ordering, and consumption safety; no event is selected. BRET-REQ-033, 037, 043, 045, 048. |
| BEB-REQ-040 | Applicable | External messaging cannot be introduced independently. BRET-REQ-033, 039–040, 048. |
| BEB-REQ-041–042 | Applicable | Failure classification, recovery, and reconciliation. BRET-REQ-006, 008, 012–13, 17, 20, 22–24, 28–29, 32–33, 43–45. |
| BEB-REQ-043–044 | Applicable | Backend security, data protection, Secrets, and evidence safety. BRET-REQ-009, 018–20, 22–24, 26, 28–33, 36–37, 43, 45–47. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records, observability, correlation, and health. BRET-REQ-007, 015, 017–20, 22–24, 28–31, 35–37, 43–47. |
| BEB-REQ-047–048 | Applicable | Safe configuration and reachable feature states without selecting mechanisms. BRET-REQ-039–42, 50. |
| BEB-REQ-049–051 | Applicable | Migration, deployment compatibility, bounded work, accessibility, and failure containment. BRET-REQ-007–008, 026, 032, 034–35, 40–42, 47. |
| BEB-REQ-052–055 | Applicable | Domain, application, Adapter, integration, Contract, security, architecture, operational, and traceable verification. BRET-REQ-038, 050. |
| BEB-REQ-056 | Applicable | Policy, provider, implementation, numerical, and roadmap neutrality. BRET-REQ-003, 014, 018, 021–27, 31–35, 039–50. |

Every `BEB-REQ-001` through `BEB-REQ-056` is accounted for; conditional classification does not authorize omission when its governed condition exists.

## 7. Dependency and Authority Matrix

| Boundary | BRET may consume, request, or expose | Authority that remains external |
| --- | --- | --- |
| BIDN / BCUS | Trusted Principal, Authentication, Customer, Visitor, Account, ownership, contact, and Address evidence | Identity, credentials, Sessions, Customer, Account, Consent, Preference, and source history |
| BPRD / BCAT / BSRCH | Historical or governed catalogue references and non-authoritative Search context where policy requires | Product, Product Variant, Category, taxonomy, catalogue lifecycle, Search index, ranking, and result truth |
| BPRC | Governed Money, Currency, historical commercial evidence, and approved calculations | Pricing, Discount, Promotion, Voucher restoration, Tax, fees, conversion, credit, invoice, and Credit Note policy |
| BCART / BCHK | Historical purchase or orchestration references only where governed | Cart and Checkout identity, validation, lifecycle, success, and client state |
| BORD | Order identity, Order Items, immutable snapshots, purchased quantities, delivery and historical evidence | Order creation, lifecycle, cancellation, items, snapshots, quantities, and historical commercial truth |
| BINV | Governed Inventory coordination requests and outcomes | Stock, reservations, locations, conditions, adjustments, movements, restocking effects, and Available-to-Sell |
| BSHP | Governed reverse-logistics requests and transport outcomes | Shipment, Carrier, labels, tracking, transport, provider, delivery, and Shipping reconciliation |
| BPAY | Governed Refund-execution requests and Payment/Refund outcomes | Payment processing, provider evidence, financial effects, Refund execution, and Refund Transaction truth |
| Administration | Protected invocation of explicit Return capabilities | Administrative workflow authority, Roles, Permissions, support, and escalation policy |
| Notifications / Reporting | Bounded governed Return facts after future approval | Communication delivery, templates, attempts, analytics, reports, exports, and Projections |
| CMS and other Domains | Only explicitly governed bounded references | CMS and every other owning-Domain truth |

## 8. Return, Refund, Inventory, Shipping, Data, Contract, and Event Boundaries

BRET owns Return behavior and Return-owned transactional evidence, not transport, persistence, provider, or infrastructure mechanisms. Future concrete Contracts must preserve the Requirements above and be governed separately. Refund execution remains BPAY-owned; Inventory/restocking effects remain BINV-owned; reverse-logistics execution remains BSHP-owned; Order history remains BORD-owned. External effects, local transactions, unknown outcomes, reconciliation, and recovery remain explicit. Events and messaging remain conditional and mechanism-neutral.

## 9. Open Product Decisions

The following **21 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and Accepted ADR-0014 remain unresolved:

| Item | Open Product Decision | Preserved BRET boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Customer, Visitor, or Account prerequisite is selected. |
| 5 | Customer email-verification requirements. | No verification prerequisite or mechanism is selected. |
| 6 | Initial payment methods and provider. | No method, provider, provider Contract, or Refund mechanism is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No reverse-logistics provider, service, area, eligibility, or fee is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No Return-related delivery or commercial treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, invoice, Refund-document, or commercial policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, timing, or Return transition policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No eligibility, window, exchange, Refund, disposition, or treatment policy is selected. |
| 12 | Stock Reservation duration. | No Reservation duration or exchange-allocation treatment is selected. |
| 13 | Back-order and pre-order support. | No eligible-item or exchange treatment for either capability is selected. |
| 14 | Voucher and promotion stacking policy. | No reversal, restoration, stacking, precedence, or Refund treatment is selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No marketing, consent, preference, or Return-communication policy is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, event taxonomy, or Return analytics mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, export, audience, purpose, format, or schedule is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, workflow, expectation, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, accounting, or document policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, provider, score, threshold, rule, review, or intervention policy is selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No privacy workflow, retention treatment, correction, deletion, or closure mechanism is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, Refund, or treatment is selected. |

## 10. Open Architecture Decisions

The following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and Accepted ADR-0014 remain unresolved:

| Item | Open Architecture Decision | Preserved BRET boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment Provider, Refund integration, provider Contract, or adapter is selected. |
| 6 | Shipping provider selection. | No reverse-logistics provider, Carrier, Contract, or adapter is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Return persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

## 11. Explicit Non-Decisions and Roadmap Boundary

BRET selects no Return eligibility policy, Return Window, Exchange policy, Refund eligibility or amount policy, Partial Refund policy, cancellation rule, Voucher or Promotion restoration, Tax or Credit Note policy, store-credit policy, Return fee, restocking fee, shipping-fee Refund policy, Return reason policy, inspection policy, disposition policy, provider, API, HTTP detail, DTO, payload, schema, table, column, index, ORM mapping, event, topic, queue, broker, cache, Redis use, infrastructure product, topology, identifier format, retry count, timeout, TTL, retention period, rate limit, SLA, SLO, recovery objective, fraud mechanism, Role/Permission matrix, or other unresolved mechanism or policy.

CMS remains independently eligible, separate, unresolved, unranked, and unordered. Administration, Notifications, and Reporting remain separate and unresolved with the dependencies recorded in BRET-REQ-048–049. BRET establishes no post-BRET title, path, scope, decomposition, or order and authorizes no downstream Draft.

## 12. Risks and Controls

| Risk | Implementation-neutral control direction |
| --- | --- |
| False Return creation or authorization | Require Return-owned durable evidence, trusted association, contextual Authorization, and explicit outcomes. |
| Excess or duplicate returned quantity | Preserve quantity classes, authoritative limits, concurrency safety, stable correlation, and duplicate controls. |
| Return state treated as financial truth | Keep Return, Refund request, Refund execution, Refund Transaction, and provider outcome distinct. |
| Unauthorized or duplicate Refund request | Require governed inputs, explicit stages, correlation, applicable idempotency, and reconciliation. |
| Physical receipt creates sellable Stock | Require BINV-owned restocking outcomes before any Inventory truth changes. |
| Shipping evidence becomes Return truth | Preserve BSHP authority and require Return-owned governed progression. |
| Sensitive evidence leaks | Minimize collection and protect data across every storage, Contract, diagnostic, audit, and representation surface. |
| Recovery corrupts history | Require evidence-based, authorized, quantity-safe, duplicate-safe recovery and reconciliation. |
| Projection becomes authoritative | Preserve source lineage and prevent consumer failure or mutation from changing Return truth. |
| Policy is invented during implementation | Trace unresolved decisions and reject ungoverned values, rules, and mechanisms. |

## 13. Required Governance Reviews

Before approval, governance review MUST confirm:

- Architecture and Return ownership review of Return-only scope, ADR-0014 conformance, BEB inheritance, and roadmap containment;
- affected Order, Inventory, Shipping and Fulfilment, Payment, Pricing, Product, Identity, Customer, Administration, Notifications, Reporting, and CMS ownership review of bounded Contracts and preserved authority;
- Security review of contextual Authorization, object isolation, Sensitive Data, evidence protection, replay, duplicate external effects, fraud non-authority, recovery, and Audit Records;
- Testing review of one-to-one coverage, Return/BEB matrices, failure and uncertainty paths, external-effect safety, security, compatibility, accessibility, and operations;
- Documentation review of lifecycle, terminology, references, traceability, decision inventories, and implementation neutrality;
- all 48 Return Domain Requirements and all 56 BEB Requirements are accounted for;
- all 21 Product and 11 Architecture Decisions remain exact and unresolved;
- BRET Draft closes no Administration, Notifications, or Reporting prerequisite and authorizes no later backend Specification; and
- no provider, policy, API, persistence, event, cache, infrastructure, numerical target, or Role/Permission matrix is selected.

These reviews are pending for the `0.1.0 Draft`. No completed approval, reviewer identity, signature, ticket, or external approval artifact is asserted.

## 14. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/VISION.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0014-post-payment-backend-specification.md`
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
- `specifications/backend/order/order-backend.md`
- `specifications/backend/shipping/shipping-backend.md`
- `specifications/backend/payment/payment-backend.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/cms/cms-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-23 | Draft | Established the initial Return-only BRET backend Specification authorized by Accepted ADR-0014, specializing the Approved Return Domain and materially applicable BEB Requirements while preserving upstream authority, unresolved decisions, implementation neutrality, and post-BRET roadmap containment. |

## 16. Final Validation

Before Draft approval review, verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, owner is `Backend`, scope is `BRET`, and the Draft remains non-normative;
2. the decomposition is Return-only, BRET immediately follows BPAY, and all cross-Domain authority boundaries remain intact;
3. all 50 BRET Requirements are unique, contiguous, normative, implementation-neutral, and within scope;
4. all 50 Acceptance Criteria map one-to-one to Requirements with no orphan or duplicate identifier;
5. all 50 traceability rows map each Requirement to exactly one Acceptance Criterion and governed sources;
6. all 48 Return Domain Requirements and all 56 BEB Requirements are explicitly accounted for;
7. all 21 Product and 11 Architecture Decisions remain exact and unresolved;
8. BORD, BINV, BSHP, and BPAY evidence is consumed without transferring Order, Inventory, Shipping and Fulfilment, Payment, Refund execution, or financial authority;
9. Return eligibility, window, Exchange, Refund, cancellation, restoration, Tax, Credit Note, credit, fee, reason, inspection, and disposition policy remain unresolved where governance requires;
10. no provider, API, DTO, persistence, event, cache, infrastructure, numerical target, Role/Permission matrix, or implementation mechanism is selected;
11. required governance reviews remain pending, Revision History is lifecycle-correct, BRET Draft closes no downstream dependency, and every post-BRET roadmap position remains unresolved; and
12. the final change creates only `specifications/backend/return/return-backend.md`, passes whitespace validation, and remains unstaged, uncommitted, and unpushed.
