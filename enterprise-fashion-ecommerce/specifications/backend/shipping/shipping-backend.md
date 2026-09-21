---
title: Shipping and Fulfilment Backend Specification
version: 0.1.0
status: Draft
owner: Backend / Shipping and Fulfilment
last_updated: 2026-09-21
authoritative: false
scope: BSHP
---

# Shipping and Fulfilment Backend Specification

## 1. Purpose

This Draft Specification defines implementation-facing backend obligations for the Approved Shipping and Fulfilment Domain under scope `BSHP`. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the Shipping and Fulfilment backend scope, remain `authoritative: false`, and remain subordinate to governing sources, Approved Business Requirements, the Approved Shipping and Fulfilment Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, and applicable repository standards.

BSHP uses a Shipping-and-Fulfilment-only decomposition authorized by Accepted ADR-0012 immediately after BORD. It owns no Identity, Customer, Product, Category, Inventory, Pricing, Cart, Checkout, Order, Payment, Return, CMS, Administration, Notifications, Reporting, Search and Discovery, or other external truth. It resolves no Open Product or Architecture Decision and establishes no post-BSHP roadmap position.

## 2. Scope, Authority, and Requirements

### BSHP-REQ-001 — Lifecycle, Scope, and Authority

BSHP MUST retain scope `BSHP`, `authoritative: false`, Draft lifecycle metadata until governed approval, Shipping-and-Fulfilment-only authority, governing-source precedence, and no repository-wide authority.

### BSHP-REQ-002 — Complete Shipping Domain Specialization

BSHP MUST specialize every materially applicable `REQ-SHP-001` through `REQ-SHP-043` without weakening, duplicating, or transferring the Approved Shipping and Fulfilment Domain's authority.

### BSHP-REQ-003 — Decomposition and Roadmap Containment

BSHP MUST use a Shipping-and-Fulfilment-only decomposition, remain the final currently governed backend roadmap position, and MUST NOT establish any later Backend Specification identity, title, path, scope code, decomposition, or ordering.

### BSHP-REQ-004 — BEB Inheritance

BSHP MUST inherit every materially applicable `BEB-REQ-001` through `BEB-REQ-056`, explicitly account for conditional applicability, and MUST NOT copy, weaken, contradict, or transfer BEB authority.

### BSHP-REQ-005 — BIDN Consumption Boundary

BSHP MAY consume trusted Principal and Authentication evidence where materially required, but Identity, credentials, Authentication, Sessions, tokens, Claims, and access-evidence authority MUST remain with BIDN, and no session or token mechanism is selected.

### BSHP-REQ-006 — BCUS Consumption Boundary

BSHP MAY consume governed Customer, Account, ownership, and current Address evidence required for delivery, but Customer, Account, Address, Consent, and Preference truth MUST remain with BCUS.

### BSHP-REQ-007 — BORD Fulfilment Handoff

BSHP MAY consume governed BORD Order identity, Order Item context, Order Snapshot evidence, Fulfilment Group, delivery context, lifecycle evidence, and permitted fulfilment or cancellation coordination through BORD-owned Contracts, but MUST NOT create, mutate, infer, or redefine Order identity, creation, lifecycle, history, snapshots, cancellation, or commercial history.

### BSHP-REQ-008 — BCHK Delivery Boundary

BSHP MAY consume or provide governed delivery choice, Shipping Address context, Shipping Rate evidence, revalidation, and completed Checkout handoff evidence where materially required, but Checkout identity, lifecycle, validation, orchestration, completion, Payment initiation, and Order-creation authority MUST remain with BCHK.

### BSHP-REQ-009 — BINV Fulfilment Coordination Boundary

BSHP MAY consume governed Inventory outcomes required for fulfilment coordination, but Stock, Stock Reservation, Stock Adjustment, Stock Movement, Stock Location, Available-to-Sell, availability, and overselling controls MUST remain with BINV; Shipping progress MUST NOT prove an Inventory effect.

### BSHP-REQ-010 — BPRC Commercial Boundary

BSHP MAY consume or preserve governed Shipping Rate, Money, Currency, and commercial evidence where required, but Price, Promotion, Discount, Tax, fee calculation, Currency conversion, rounding, and commercial totals MUST remain with BPRC or their governing owner.

### BSHP-REQ-011 — Other Upstream Boundaries

BPRD Product and Product Variant truth, BCAT Category truth, BCART Cart truth, and BSRCH Search and Discovery truth MUST remain external. BSHP MUST NOT require BCAT, BCART, or BSRCH evidence merely because those backends precede BSHP, and Search representations MUST NOT establish fulfilment truth.

### BSHP-REQ-012 — Modular and Hexagonal Boundary

BSHP MUST preserve the modular-monolith and hexagonal boundaries, inward dependency direction, layer responsibilities, and project-owned Ports required by BEB without selecting a service extraction, framework, provider, or deployment topology.

### BSHP-REQ-013 — Public Contract and Use-Case Boundary

BSHP MUST expose only intentional abstract public Contracts and explicit Shipping-owned Use Cases, distinguish commands from queries, validate governed context, and prevent Controllers, Adapters, provider types, persistence models, or other Modules from owning Shipping policy.

### BSHP-REQ-014 — Shipping Persistence and Historical Truth

BSHP MUST own only Shipping and Fulfilment persistence, preserve Shipment lifecycle and operational history, use project-owned persistence Ports, maintain applicable integrity and historical truth, and prevent later upstream or provider changes from silently rewriting accepted Shipping evidence without selecting a schema, table, ORM mapping, index, identifier format, or migration design.

### BSHP-REQ-015 — Transaction and External-Effect Boundary

BSHP MUST use explicit focused local transaction boundaries, keep provider and other network calls outside long-running Database Transactions where a reliable alternative exists, and distinguish local commit, external effect, partial completion, timeout, and unknown outcome without implying distributed atomicity.

### BSHP-REQ-016 — Governed Delivery Choices

BSHP MAY determine or expose only delivery choices supported by current governed destination, Product or Order context, commercial evidence, and provider capability where applicable; it MUST NOT invent a Carrier, service level, delivery area, delivery-time promise, Delivery Method, fulfilment strategy, warehouse strategy, eligibility rule, fee, or free-delivery threshold.

### BSHP-REQ-017 — Delivery Revalidation and Failure

BSHP MUST distinguish current, stale, invalid, unavailable, unsupported, and uncertain delivery-choice evidence, require revalidation before material commitment, and return safe explicit outcomes without fabricating eligibility or treating client or Checkout state as Shipping authority.

### BSHP-REQ-018 — Shipping-Owned Quotation Evidence

Where governed, BSHP MAY determine or obtain Shipping-owned quotation evidence and MUST preserve its identity, applicability, freshness, Currency, source, and provider evidence, while defining no final commercial total, Promotion, Discount, Tax, fee formula, zone, parcel, threshold, or rounding policy.

### BSHP-REQ-019 — Pricing, Money, and Currency Integrity

BSHP MUST use explicit precision-safe Money and Currency, reject silent Currency mixing or conversion, preserve applicable Version 1 ZAR governance, and reconcile material disagreement through governing Pricing or Checkout boundaries rather than independently recalculating commercial truth.

### BSHP-REQ-020 — Shipping Address and Historical Context

BSHP MUST consume only the governed Shipping Address context required for delivery, reject inaccessible, invalid, mismatched, ambiguous, or stale context safely, and keep current BCUS Address truth distinct from retained BORD or Shipment delivery context without defining Address fields or a snapshot mechanism.

### BSHP-REQ-021 — Inventory Evidence and Fulfilment Coordination

BSHP MUST distinguish current, missing, stale, conflicting, unavailable, partial, and uncertain Inventory evidence, coordinate only through governed BINV Contracts, and MUST NOT invent a warehouse, bin, allocation, picking, packing, reservation, restocking, or Stock persistence mechanism.

### BSHP-REQ-022 — Order and Shipment Separation

Every applicable Shipment or Fulfilment action MUST correlate to governed BORD Order and Order Item context, reject unknown, inaccessible, mismatched, stale, or duplicate associations safely, and keep Order and Shipment identity, lifecycle, state, and history distinct.

### BSHP-REQ-023 — Checkout and Historical Purchase Separation

BSHP MUST keep current Checkout delivery evidence, BORD historical purchase evidence, and Shipping-owned operational evidence distinguishable; later Checkout, Customer, Product, Pricing, Inventory, or Address changes MUST NOT silently rewrite accepted Order or Shipment history.

### BSHP-REQ-024 — Payment Non-Authority

BSHP MUST NOT establish or infer Payment Authorization, Capture, Void, Settlement, Refund, Chargeback, Payment status, reconciliation, or provider behavior from Order, Shipment, Dispatch, tracking, delivery, client, or provider state. Any Payment-dependent coordination MUST remain conditional on future governed Payment evidence without inventing a Payment backend identity or Contract.

### BSHP-REQ-025 — Governed Fulfilment Processing

BSHP MUST preserve distinguishable accepted context, preparation, Dispatch, Shipment, tracking, delivery, exception, permitted cancellation or stop request, recovery, and reconciliation outcomes where applicable without defining a universal state graph, warehouse workflow, picker or packer process, packaging rule, cutoff, job, queue, or SLA.

### BSHP-REQ-026 — Stable Shipment Identity and Association

Each Shipment MUST have stable Shipping-owned identity and an unambiguous governed association with applicable Order, Order Item, Fulfilment Group, destination, and operational context; unknown, inaccessible, ambiguous, mismatched, or duplicate associations MUST fail safely without exposing protected Resource existence or selecting an identifier format.

### BSHP-REQ-027 — Authoritative Shipment Lifecycle

BSHP MUST own Shipment lifecycle truth, use applicable canonical meanings, preserve transition evidence and history, distinguish requested, observed, provider-reported, accepted, stale, invalid, and uncertain state, and reject invalid transitions without prescribing a complete graph or mechanism.

### BSHP-REQ-028 — Dispatch Integrity

BSHP MUST keep preparation, Dispatch request, accepted handoff, provider report, tracking, delivery, cancellation, and Order state distinct; Dispatch MUST NOT be asserted from local intent or an ambiguous provider result, and repeated Dispatch effects MUST be duplicate-safe without selecting cutoff or timing policy.

### BSHP-REQ-029 — Provider-Neutral Carrier Boundary

Any Carrier or Shipping Provider interaction MUST use project-owned Ports and isolated Adapters, translate provider authentication, models, errors, and status into governed Shipping semantics, and MUST NOT leak provider types inward or select a provider, Carrier, integration protocol, service, tracking mechanism, or Contract shape.

### BSHP-REQ-030 — Authoritative Provider Evidence

Provider evidence that may affect Shipping truth MUST be authentic, integral, correlated, fresh enough for its governed use, and distinguishable from client reports, redirects, representations, or unverified input; invalid, replayed, mismatched, stale, malformed, or unverifiable evidence MUST fail safely and preserve uncertainty.

### BSHP-REQ-031 — Tracking Truth

BSHP MUST correlate tracking evidence to the correct Shipment, distinguish provider-reported observations from accepted Shipping truth, preserve unavailable, stale, conflicting, delayed, duplicate, or uncertain outcomes, protect tracking data, and define no Tracking Number format, URL, polling cadence, provider, or refresh interval.

### BSHP-REQ-032 — Delivery Outcome Integrity

BSHP MUST distinguish delivery intent, attempt, provider report, accepted evidence, Delivered, Delivery Failed, exception, uncertainty, and Returned to Sender where applicable; no client report, tracking representation, notification, elapsed time, or Order state may independently prove delivery, and no proof, redelivery, or SLA policy is selected.

### BSHP-REQ-033 — Explicit Shipping Failure Outcomes

BSHP MUST distinguish validation, Authentication, Authorization, conflict, stale evidence, provider unavailability, timeout, rate limiting, partial completion, concurrency, duplicate, unsupported choice, delivery exception, dependency failure, and unexpected failure where their response, recovery, audit, or observability obligations differ.

### BSHP-REQ-034 — Uncertainty Preservation

Timeout, interruption, ambiguous response, stopped observation, delayed tracking, missing callback, and partial provider work MUST remain unknown rather than success or definitive failure until authorized evidence or reconciliation resolves the outcome; local cancellation MUST NOT assert that external processing stopped.

### BSHP-REQ-035 — Duplicate-Effect and Idempotency Safety

Repeated, duplicate, concurrent, replayed, or reordered requests, callbacks, Messages, jobs, reconciliation, or operator actions MUST NOT create duplicate Shipment, Dispatch, provider, delivery, Inventory, Order, notification, or financial effects; applicable idempotency identity and prior outcomes MUST remain explicit without selecting keys, storage, TTLs, locks, or caches.

### BSHP-REQ-036 — Safe Retry and Concurrency

Before repeating a potentially irreversible Shipping action, BSHP MUST classify prior effects as known, unknown, conflicting, or safely repeatable, revalidate current authority and upstream state, reject incompatible replay, and prevent concurrent work from overwriting truth or multiplying effects without selecting retry counts, timeouts, backoff, or concurrency mechanisms.

### BSHP-REQ-037 — Shipping Reconciliation

BSHP MUST reconcile material disagreement among local Shipping state, BORD evidence, BINV evidence, BPRC evidence, BCHK context, provider evidence, tracking, delivery, and accepted history through authorized evidence, preserving provenance, history, duplicate safety, and explicit unresolved uncertainty without selecting schedules, jobs, queues, or provider mechanisms.

### BSHP-REQ-038 — Controlled Recovery

Recovery MUST be explicit, authorized, observable, state-governed, duplicate-safe, and proportionally audited; it MUST revalidate current authority and evidence, preserve prior facts, and MUST NOT fabricate success, provider evidence, Order state, Inventory effects, Payment outcomes, Return authority, or history.

### BSHP-REQ-039 — Cancellation and Fulfilment-Stop Boundary

BSHP MAY consume governed BORD cancellation or stop-request evidence and coordinate permitted Shipping effects, but Order cancellation eligibility and history remain BORD-owned; request, provider effect, Shipment outcome, Inventory effect, and Refund MUST remain distinct, uncertain where evidence is incomplete, and duplicate-safe without selecting cutoff policy.

### BSHP-REQ-040 — Return and Reverse-Logistics Boundary

BSHP is forward Shipping and Fulfilment only. It MAY preserve an abstract future handoff for governed Return context, but MUST NOT own Return eligibility, authorization, lifecycle, inspection, disposition, Refund policy, or reverse-logistics implementation and MUST NOT imply that Return follows BSHP in the backend roadmap.

### BSHP-REQ-041 — Administration and Operational Actions

Protected operational actions MUST invoke explicit Shipping-owned Use Cases, enforce current contextual server-side Authorization, preserve Shipping invariants, require proportional confirmation, reason, evidence, Human Approval Gate, and Audit Record where governing policy requires them, and MUST NOT define Roles, Permissions, mappings, administrator identities, escalation workflow, or service targets.

### BSHP-REQ-042 — Authentication and Contextual Authorization

Every protected BSHP Use Case MUST enforce current contextual Authorization for the Principal, Resource, action, property, association, and Shipping-owned state; identifiers, Role labels, Permissions, Claims, Scope, UI state, tracking possession, or prior responses MUST NOT independently authorize access, and Identity authority remains external.

### BSHP-REQ-043 — Shipping Data Protection

BSHP MUST minimize and purpose-limit Customer, Shipping Address, tracking, provider, external-identifier, operational, audit, and other Sensitive Data; protect it through least privilege across input, persistence, logs, telemetry, errors, events, tests, and exports; prevent protected Resource enumeration; and expose only necessary evidence without selecting a security mechanism.

### BSHP-REQ-044 — Conditional Events and Messaging

Shipping Domain or Integration Events MAY exist only for a governed need and MUST preserve source authority, compatibility, protection, correlation, duplicate and replay safety, and Projection non-authority. BSHP MUST NOT select an event name, schema, payload, topic, queue, broker, notification provider, delivery channel, messaging technology, or external messaging adoption.

### BSHP-REQ-045 — Contract, API, and Compatibility Boundary

Future BSHP Contracts MUST preserve identity, authority, provenance, freshness, compatibility, validation, Authorization, error, duplicate, uncertainty, recovery, and Sensitive Data semantics without selecting routes, HTTP methods or statuses, DTO fields, payloads, OpenAPI style, event schemas, provider Contracts, or breaking-change mechanisms beyond governing standards.

### BSHP-REQ-046 — Accessibility and Non-Authoritative Representations

Applicable Shipping outcomes MUST support understandable accessible semantics, while notifications, reports, analytics, exports, dashboards, caches, and Projections remain non-authoritative representations that cannot create, mutate, or prove Shipping truth or transfer authority to Notifications or Reporting.

### BSHP-REQ-047 — Bounded Observability and Audit

BSHP work MUST remain bounded and emit safe structured observability, correlation, health, failure, uncertainty, and reconciliation evidence; materially governed actions MUST produce proportional Audit Records distinct from logs, metrics, traces, events, and history, without exposing Sensitive Data or selecting numerical targets, limits, SLAs, SLOs, or recovery objectives.

### BSHP-REQ-048 — Commercial-Document Non-Authority

BSHP MAY preserve permitted Shipping Money and delivery evidence but MUST NOT establish Tax, invoice, Credit Note, numbering, accounting, legal, fee, Promotion, Discount, Currency-conversion, or document-generation policy or technology.

### BSHP-REQ-049 — Complete Shipping Verification

Verification MUST cover applicable positive and negative flows, authority boundaries, delivery choices, Address, quotations, Money, Inventory, Order, Checkout, Payment, Fulfilment, Shipment, Dispatch, provider evidence, tracking, delivery, failures, uncertainty, duplicates, replay, concurrency, reconciliation, recovery, cancellation, Return, Authorization, Sensitive Data, Contracts, events, accessibility, observability, audit, compatibility, migration, and operational behavior without selecting tooling or numerical coverage targets.

### BSHP-REQ-050 — Decision and Implementation Neutrality

BSHP MUST preserve all 6 materially applicable Open Product Decisions and 10 materially applicable Open Architecture Decisions, keep CMS and Payment independently eligible, separate, unresolved, unranked, and unordered, keep Return, Administration, Notifications, Reporting, and every post-BSHP roadmap position unresolved, and MUST NOT select any excluded policy, provider, mechanism, schema, Contract shape, infrastructure, or numerical value.

## 3. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable criterion |
| --- | --- | --- |
| BSHP-AC-001 | BSHP-REQ-001 | Metadata shows `0.1.0 Draft`, `authoritative: false`, scope `BSHP`, non-normative Draft status, bounded Shipping-and-Fulfilment-only authority, and governing-source precedence. |
| BSHP-AC-002 | BSHP-REQ-002 | The Domain coverage matrix accounts for `REQ-SHP-001` through `REQ-SHP-043` without omission, weakening, or authority transfer. |
| BSHP-AC-003 | BSHP-REQ-003 | Review finds Shipping-and-Fulfilment-only decomposition and no backend identity, path, scope, decomposition, or position after BSHP. |
| BSHP-AC-004 | BSHP-REQ-004 | The BEB matrix accounts for `BEB-REQ-001` through `BEB-REQ-056`, including conditional applicability, without weakening BEB. |
| BSHP-AC-005 | BSHP-REQ-005 | Tests consume only trusted BIDN evidence and find no Identity, credential, Authentication, Session, token, Claim, or access-evidence authority in BSHP. |
| BSHP-AC-006 | BSHP-REQ-006 | Contract tests accept only governed BCUS evidence, reject invalid Customer or Address context safely, and find no Customer/Account authority transfer. |
| BSHP-AC-007 | BSHP-REQ-007 | Handoff tests correlate governed BORD evidence and prove BSHP cannot create or mutate Order identity, Items, lifecycle, history, snapshots, cancellation, or commercial history. |
| BSHP-AC-008 | BSHP-REQ-008 | Delivery-boundary tests preserve BCHK authority and distinguish delivery evidence from Checkout validation, orchestration, completion, Payment initiation, and Order creation. |
| BSHP-AC-009 | BSHP-REQ-009 | Fulfilment tests consume governed Inventory outcomes and prove Shipping progress cannot create or mutate any BINV-owned truth. |
| BSHP-AC-010 | BSHP-REQ-010 | Commercial tests preserve governed evidence while finding no BSHP calculation or authority over Price, Promotion, Discount, Tax, fees, conversion, rounding, or totals. |
| BSHP-AC-011 | BSHP-REQ-011 | Dependency review finds no unneeded BCAT, BCART, or BSRCH dependency and no upstream authority transfer. |
| BSHP-AC-012 | BSHP-REQ-012 | Architecture checks preserve Module, layer, Port, Adapter, and inward-dependency boundaries without an ungoverned topology. |
| BSHP-AC-013 | BSHP-REQ-013 | Use-Case and public-surface checks expose only intentional Contracts and find no policy in Controllers, Adapters, persistence types, or other Modules. |
| BSHP-AC-014 | BSHP-REQ-014 | Persistence evidence shows Shipping-only ownership, protected history, project-owned Ports, and no selected physical design or identifier format. |
| BSHP-AC-015 | BSHP-REQ-015 | Transaction tests distinguish local commit and external outcome, keep external calls out of long transactions where possible, and preserve partial or unknown effects. |
| BSHP-AC-016 | BSHP-REQ-016 | Delivery-choice tests use governed context and select none of the prohibited provider, service, geography, timing, strategy, or commercial values. |
| BSHP-AC-017 | BSHP-REQ-017 | Revalidation tests distinguish current, stale, invalid, unavailable, unsupported, and uncertain evidence without fabricated eligibility. |
| BSHP-AC-018 | BSHP-REQ-018 | Quotation evidence preserves identity, applicability, freshness, Currency, source, and provider provenance while selecting no commercial rule. |
| BSHP-AC-019 | BSHP-REQ-019 | Money tests reject floating-point use, silent mixing, and implicit conversion and preserve Pricing authority over disagreement. |
| BSHP-AC-020 | BSHP-REQ-020 | Address tests reject bad context, distinguish current from historical evidence, and define no Address fields or snapshot mechanism. |
| BSHP-AC-021 | BSHP-REQ-021 | Inventory tests distinguish all degraded evidence classes and find no Shipping-owned Stock effect or prohibited fulfilment mechanism. |
| BSHP-AC-022 | BSHP-REQ-022 | Association tests reject invalid Order context safely and keep Order and Shipment identity, lifecycle, state, and history distinct. |
| BSHP-AC-023 | BSHP-REQ-023 | Historical-evidence tests prove later source changes cannot silently rewrite accepted Order or Shipment truth. |
| BSHP-AC-024 | BSHP-REQ-024 | Review finds no Payment backend identity or Contract and no Payment outcome inferred from Shipping, Order, client, or provider state. |
| BSHP-AC-025 | BSHP-REQ-025 | Fulfilment tests preserve distinguishable governed outcomes without a universal graph, warehouse workflow, job, queue, cutoff, or SLA. |
| BSHP-AC-026 | BSHP-REQ-026 | Every Shipment has stable owned identity and governed associations; invalid associations fail without enumeration and no identifier format is selected. |
| BSHP-AC-027 | BSHP-REQ-027 | Lifecycle tests preserve canonical meanings, evidence distinctions, history, and invalid-transition rejection without prescribing a graph or mechanism. |
| BSHP-AC-028 | BSHP-REQ-028 | Dispatch tests separate request, accepted handoff, provider report, tracking, delivery, cancellation, and Order state and prevent duplicate effects. |
| BSHP-AC-029 | BSHP-REQ-029 | Adapter tests isolate provider types behind project-owned Ports and find no selected provider, protocol, service, tracking mechanism, or Contract shape. |
| BSHP-AC-030 | BSHP-REQ-030 | Provider-evidence tests validate authenticity, integrity, correlation, freshness, replay, mismatch, and uncertainty without trusting client reports. |
| BSHP-AC-031 | BSHP-REQ-031 | Tracking tests preserve correlation and evidence distinctions under unavailable, stale, conflicting, delayed, duplicate, and uncertain outcomes without a chosen mechanism. |
| BSHP-AC-032 | BSHP-REQ-032 | Delivery tests distinguish all governed outcomes and prove no client, representation, notification, time, or Order state independently establishes delivery. |
| BSHP-AC-033 | BSHP-REQ-033 | Failure tests preserve each materially distinct failure class and its safe response, recovery, audit, and observability treatment. |
| BSHP-AC-034 | BSHP-REQ-034 | Uncertainty tests prevent ambiguous or partial external outcomes from becoming success or definitive failure before authorized evidence or reconciliation. |
| BSHP-AC-035 | BSHP-REQ-035 | Duplicate and replay tests prevent every listed duplicate effect and select no key, storage, TTL, lock, or cache mechanism. |
| BSHP-AC-036 | BSHP-REQ-036 | Retry and concurrency tests revalidate authority and state, classify prior effects, reject incompatible replay, and select no numerical or locking mechanism. |
| BSHP-AC-037 | BSHP-REQ-037 | Reconciliation tests preserve provenance, history, duplicate safety, and explicit uncertainty across all materially governed evidence sources. |
| BSHP-AC-038 | BSHP-REQ-038 | Recovery tests require authorization, observability, current evidence, duplicate safety, and proportional audit and fabricate no external truth. |
| BSHP-AC-039 | BSHP-REQ-039 | Cancellation-race tests keep request and each external effect distinct, preserve BORD authority, and infer no Refund or cutoff policy. |
| BSHP-AC-040 | BSHP-REQ-040 | Review finds forward-only BSHP authority, no Return policy or reverse-logistics implementation, and no implied Return roadmap position. |
| BSHP-AC-041 | BSHP-REQ-041 | Operational-action tests enforce protected Shipping Use Cases and proportional controls without Roles, Permissions, identities, escalation workflow, or targets. |
| BSHP-AC-042 | BSHP-REQ-042 | Authorization tests vary Principal, Resource, action, property, association, and state and prove no identifier, label, Claim, Scope, UI state, or tracking possession grants access alone. |
| BSHP-AC-043 | BSHP-REQ-043 | Security tests demonstrate minimization, least privilege, concealment, and safe handling across all listed surfaces without selecting a mechanism. |
| BSHP-AC-044 | BSHP-REQ-044 | Event review preserves conditionality, authority, compatibility, protection, correlation, duplicate/replay safety, and Projection non-authority and selects no event or messaging design. |
| BSHP-AC-045 | BSHP-REQ-045 | Contract tests preserve semantic and compatibility obligations while finding no invented route, method, status, DTO, payload, schema, provider Contract, or mechanism. |
| BSHP-AC-046 | BSHP-REQ-046 | Outcome tests provide accessible semantics and prove every notification, report, export, cache, dashboard, and Projection remains non-authoritative. |
| BSHP-AC-047 | BSHP-REQ-047 | Operational checks provide bounded safe observability and distinct proportional Audit Records without Sensitive Data or numerical targets. |
| BSHP-AC-048 | BSHP-REQ-048 | Review finds no Tax, invoice, Credit Note, numbering, accounting, legal, commercial, Currency-conversion, or document-technology decision. |
| BSHP-AC-049 | BSHP-REQ-049 | Verification traceability covers every listed functional, negative, boundary, resilience, security, compatibility, and operational concern without a tooling or coverage target. |
| BSHP-AC-050 | BSHP-REQ-050 | Review confirms all 6 Product and 10 Architecture decisions and every external capability and post-BSHP position remain unresolved, with no prohibited mechanism selected. |

## 4. Requirement Traceability

| Requirement | Shipping Domain source | BEB source | Approved backend / external boundary source | Verification |
| --- | --- | --- | --- | --- |
| BSHP-REQ-001 | REQ-SHP-001 | BEB-REQ-001–004 | ADR-0012; ARCHITECTURE.md §35 | BSHP-AC-001 |
| BSHP-REQ-002 | REQ-SHP-001–043 | BEB-REQ-002–004 | Shipping Domain §38 | BSHP-AC-002 |
| BSHP-REQ-003 | REQ-SHP-001–002 | BEB-REQ-003–004, 056 | ADR-0012 | BSHP-AC-003 |
| BSHP-REQ-004 | REQ-SHP-001, 043 | BEB-REQ-001–056 | BEB §§2, 6 | BSHP-AC-004 |
| BSHP-REQ-005 | REQ-SHP-034 | BEB-REQ-019–020 | BIDN-REQ-012–014, 020–024, 029–030 | BSHP-AC-005 |
| BSHP-REQ-006 | REQ-SHP-009–010, 034–035 | BEB-REQ-011, 019–021, 043 | BCUS-REQ-013–014, 024–025, 036, 039–040, 048 | BSHP-AC-006 |
| BSHP-REQ-007 | REQ-SHP-013–014, 031 | BEB-REQ-008–010, 021, 024 | BORD-REQ-015–024, 032–039, 045 | BSHP-AC-007 |
| BSHP-REQ-008 | REQ-SHP-004–007, 009–010, 015 | BEB-REQ-008–010, 021 | BCHK-REQ-010–011, 019–023, 030, 038 | BSHP-AC-008 |
| BSHP-REQ-009 | REQ-SHP-011–012 | BEB-REQ-008, 021, 025 | BINV-REQ-015–027, 032–033, 041 | BSHP-AC-009 |
| BSHP-REQ-010 | REQ-SHP-006–008, 042 | BEB-REQ-008, 021, 023–025 | BPRC-REQ-018–024, 028–030, 039 | BSHP-AC-010 |
| BSHP-REQ-011 | REQ-SHP-002, 013, 043 | BEB-REQ-003–004, 021 | BPRD-REQ-001–004; BCAT, BCART, BSRCH authority | BSHP-AC-011 |
| BSHP-REQ-012 | REQ-SHP-001, 037 | BEB-REQ-005–007 | ARCHITECTURE.md §§5, 35 | BSHP-AC-012 |
| BSHP-REQ-013 | REQ-SHP-037 | BEB-REQ-008–010, 013–018 | API.md; Shipping Domain §30 | BSHP-AC-013 |
| BSHP-REQ-014 | REQ-SHP-010, 018–020, 040 | BEB-REQ-021–024, 049–050 | DATABASE.md; POSTGRES.md | BSHP-AC-014 |
| BSHP-REQ-015 | REQ-SHP-025–030 | BEB-REQ-012, 025–028, 032–034 | DATABASE.md; SPRING.md | BSHP-AC-015 |
| BSHP-REQ-016 | REQ-SHP-004 | BEB-REQ-009, 015, 056 | BCHK-REQ-019; ADR-0012 Product decisions 7–8 | BSHP-AC-016 |
| BSHP-REQ-017 | REQ-SHP-005 | BEB-REQ-015, 041–042 | BCHK-REQ-019–022 | BSHP-AC-017 |
| BSHP-REQ-018 | REQ-SHP-006 | BEB-REQ-021, 023–024, 032–034 | BPRC-REQ-024, 039 | BSHP-AC-018 |
| BSHP-REQ-019 | REQ-SHP-007–008 | BEB-REQ-023–025, 041 | BPRC-REQ-018, 024, 028–029 | BSHP-AC-019 |
| BSHP-REQ-020 | REQ-SHP-009–010 | BEB-REQ-015, 020–024, 043 | BCUS-REQ-024–025; BORD-REQ-022 | BSHP-AC-020 |
| BSHP-REQ-021 | REQ-SHP-011–012 | BEB-REQ-008, 015, 021, 041–042 | BINV-REQ-015–027, 033, 039–041 | BSHP-AC-021 |
| BSHP-REQ-022 | REQ-SHP-013–014, 018 | BEB-REQ-008, 015, 020–024 | BORD-REQ-015–019, 032–033 | BSHP-AC-022 |
| BSHP-REQ-023 | REQ-SHP-010, 013, 015 | BEB-REQ-021, 024 | BCHK-REQ-011, 019–023; BORD-REQ-017–024 | BSHP-AC-023 |
| BSHP-REQ-024 | REQ-SHP-016 | BEB-REQ-003, 021, 044, 056 | Payment Domain REQ-PAY-002–039; ADR-0012 | BSHP-AC-024 |
| BSHP-REQ-025 | REQ-SHP-003, 017 | BEB-REQ-009–010, 028, 051, 056 | Shipping Domain §§5, 14 | BSHP-AC-025 |
| BSHP-REQ-026 | REQ-SHP-018 | BEB-REQ-015, 020–024 | BORD-REQ-004, 015, 032 | BSHP-AC-026 |
| BSHP-REQ-027 | REQ-SHP-019 | BEB-REQ-009–010, 024, 027–028 | GLOSSARY.md Shipment Lifecycle | BSHP-AC-027 |
| BSHP-REQ-028 | REQ-SHP-020 | BEB-REQ-025–031, 041–042 | BORD-REQ-032–038 | BSHP-AC-028 |
| BSHP-REQ-029 | REQ-SHP-021 | BEB-REQ-006–008, 032–033 | ADR-0012 Architecture decision 6 | BSHP-AC-029 |
| BSHP-REQ-030 | REQ-SHP-022 | BEB-REQ-032–036, 041, 043 | SECURITY-STANDARDS.md; API.md | BSHP-AC-030 |
| BSHP-REQ-031 | REQ-SHP-023 | BEB-REQ-015, 032–036, 041–043 | GLOSSARY.md §11 | BSHP-AC-031 |
| BSHP-REQ-032 | REQ-SHP-024 | BEB-REQ-024, 032–034, 041–042 | GLOSSARY.md Shipment Lifecycle | BSHP-AC-032 |
| BSHP-REQ-033 | REQ-SHP-025 | BEB-REQ-017, 033, 041–043 | API.md; SECURITY-STANDARDS.md | BSHP-AC-033 |
| BSHP-REQ-034 | REQ-SHP-026 | BEB-REQ-026, 028, 033–034, 041–042 | EVENTS.md | BSHP-AC-034 |
| BSHP-REQ-035 | REQ-SHP-027 | BEB-REQ-027, 029–031, 035–039 | BORD-REQ-012–013; BINV-REQ-022–024 | BSHP-AC-035 |
| BSHP-REQ-036 | REQ-SHP-028 | BEB-REQ-027–031, 033–034, 051 | BORD-REQ-013–014, 040–041 | BSHP-AC-036 |
| BSHP-REQ-037 | REQ-SHP-029 | BEB-REQ-028, 031, 034, 039, 042 | BORD-REQ-029–033, 040–041 | BSHP-AC-037 |
| BSHP-REQ-038 | REQ-SHP-030 | BEB-REQ-011, 028–031, 042, 045–046 | BORD-REQ-041, 048–050 | BSHP-AC-038 |
| BSHP-REQ-039 | REQ-SHP-031 | BEB-REQ-025–031, 041–042 | BORD-REQ-037–039 | BSHP-AC-039 |
| BSHP-REQ-040 | REQ-SHP-032 | BEB-REQ-003–004, 008, 056 | Return Domain REQ-RET-027–033; ADR-0012 | BSHP-AC-040 |
| BSHP-REQ-041 | REQ-SHP-033 | BEB-REQ-009, 011, 020, 045 | Administration Domain REQ-ADM-010–011, 031 | BSHP-AC-041 |
| BSHP-REQ-042 | REQ-SHP-034 | BEB-REQ-011, 019–020, 043 | BIDN-REQ-012–014, 024–030 | BSHP-AC-042 |
| BSHP-REQ-043 | REQ-SHP-035 | BEB-REQ-014–015, 017, 020, 043–044 | SECURITY-STANDARDS.md; BCUS-REQ-048 | BSHP-AC-043 |
| BSHP-REQ-044 | REQ-SHP-036 | BEB-REQ-037–040, 043 | EVENTS.md; Notifications Domain REQ-NTF-033–035, 050–051 | BSHP-AC-044 |
| BSHP-REQ-045 | REQ-SHP-037 | BEB-REQ-013–018, 032, 049–050 | API.md; DOCUMENTATION-STANDARDS.md | BSHP-AC-045 |
| BSHP-REQ-046 | REQ-SHP-038, 041 | BEB-REQ-003, 021, 043, 051 | Notifications and Reporting Domains | BSHP-AC-046 |
| BSHP-REQ-047 | REQ-SHP-039–040 | BEB-REQ-045–047, 051, 054 | SECURITY-STANDARDS.md; TESTING-STANDARDS.md | BSHP-AC-047 |
| BSHP-REQ-048 | REQ-SHP-042 | BEB-REQ-003, 021, 023–024, 056 | BPRC-REQ-023–024, 029–031 | BSHP-AC-048 |
| BSHP-REQ-049 | REQ-SHP-043 | BEB-REQ-052–055 | TESTING-STANDARDS.md | BSHP-AC-049 |
| BSHP-REQ-050 | REQ-SHP-001–002, 021, 031–037, 042–043 | BEB-REQ-040, 047–048, 056 | PRODUCT.md §24; ARCHITECTURE.md §§34–35; ADR-0012 | BSHP-AC-050 |

## 5. Shipping and Fulfilment Domain Coverage Matrix

| Shipping Domain Requirement(s) | BSHP specialization |
| --- | --- |
| REQ-SHP-001–003 | BSHP-REQ-001–003, 025 |
| REQ-SHP-004–005 | BSHP-REQ-008, 016–017 |
| REQ-SHP-006–008 | BSHP-REQ-010, 018–019 |
| REQ-SHP-009–010 | BSHP-REQ-006, 020, 023 |
| REQ-SHP-011–012 | BSHP-REQ-009, 021 |
| REQ-SHP-013–014 | BSHP-REQ-007, 022–023 |
| REQ-SHP-015 | BSHP-REQ-008, 023 |
| REQ-SHP-016 | BSHP-REQ-024 |
| REQ-SHP-017 | BSHP-REQ-025 |
| REQ-SHP-018–020 | BSHP-REQ-026–028 |
| REQ-SHP-021–024 | BSHP-REQ-029–032 |
| REQ-SHP-025–026 | BSHP-REQ-033–034 |
| REQ-SHP-027–028 | BSHP-REQ-035–036 |
| REQ-SHP-029–030 | BSHP-REQ-037–038 |
| REQ-SHP-031 | BSHP-REQ-039 |
| REQ-SHP-032 | BSHP-REQ-040 |
| REQ-SHP-033–035 | BSHP-REQ-041–043 |
| REQ-SHP-036–037 | BSHP-REQ-044–045 |
| REQ-SHP-038–041 | BSHP-REQ-046–047 |
| REQ-SHP-042 | BSHP-REQ-048 |
| REQ-SHP-043 | BSHP-REQ-049 |

All 43 Approved Shipping and Fulfilment Domain Requirements are accounted for. Open decisions constrain mechanisms and policy but do not remove Domain coverage.

## 6. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BSHP application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, Domain non-authority, specialization, and roadmap containment. BSHP-REQ-001–004, 050. |
| BEB-REQ-005–007 | Applicable | Modular-monolith, hexagonal dependency, and layer responsibilities. BSHP-REQ-012–013. |
| BEB-REQ-008–010 | Applicable | Public Contracts, Use Cases, and command/query truth. BSHP-REQ-007–013, 016–042. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and local transaction ownership. BSHP-REQ-015, 038, 041–043. |
| BEB-REQ-013–018 | Applicable where an external API or collection Contract exists | Versioning, DTO separation, validation, bounded queries, Problem Details, description, and evolution remain inherited without selecting a route or DTO. BSHP-REQ-013, 045. |
| BEB-REQ-019–020 | Applicable | BIDN supplies Authentication truth; BSHP consumes trusted evidence and enforces contextual Authorization and concealment. BSHP-REQ-005, 041–043. |
| BEB-REQ-021–024 | Applicable | Shipping data ownership, persistence boundaries, integrity, and historical truth. BSHP-REQ-007–011, 014, 018–032, 046, 048. |
| BEB-REQ-025–031 | Applicable | Atomicity, external-call separation, concurrency, durable uncertainty, idempotency, replay, and duplicate safety. BSHP-REQ-015, 028, 033–039. |
| BEB-REQ-032–034 | Applicable | Provider Ports, resilience, uncertainty, and reconciliation are material while provider and numerical choices remain unresolved. BSHP-REQ-015, 018, 029–038. |
| BEB-REQ-035–036 | Conditionally applicable | Any future governed Callback or Webhook inherits authenticity, integrity, replay, and recovery safeguards; none is selected. BSHP-REQ-030, 034–036, 044–045. |
| BEB-REQ-037–039 | Conditionally applicable | Any future governed Domain or Integration Event inherits separation, envelope, delivery, replay, and consumption safety; no event is selected. BSHP-REQ-035, 044–045. |
| BEB-REQ-040 | Applicable | External messaging cannot be introduced independently. BSHP-REQ-044, 050. |
| BEB-REQ-041–042 | Applicable | Failure classification, recovery, and reconciliation. BSHP-REQ-017, 021–024, 028, 030–039. |
| BEB-REQ-043–044 | Applicable | Backend security, data protection, Secrets, and prohibited Payment-data safety. BSHP-REQ-024, 030–031, 041–047. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records, observability, correlation, and health. BSHP-REQ-037–038, 041, 047. |
| BEB-REQ-047–048 | Applicable | Safe configuration and reachable feature states without choosing mechanisms. BSHP-REQ-029, 044–045, 050. |
| BEB-REQ-049–051 | Applicable | Migration, deployment compatibility, bounded work, and failure containment. BSHP-REQ-014, 025, 033–038, 045–047. |
| BEB-REQ-052–055 | Applicable | Domain, application, Adapter, integration, Contract, security, architecture, operational, and traceable verification. BSHP-REQ-049. |
| BEB-REQ-056 | Applicable | Policy, provider, implementation, numerical, and roadmap neutrality. BSHP-REQ-003, 011, 016–019, 024–050. |

Every `BEB-REQ-001` through `BEB-REQ-056` is accounted for; conditional classification does not authorize omission when its governed condition exists.

## 7. Dependency and Authority Matrix

| Capability | BSHP use | Authority retained outside BSHP |
| --- | --- | --- |
| BEB | Inherited shared backend obligations | Shared backend governance |
| BIDN | Trusted Principal and Authentication evidence | Identity, credentials, Authentication, Sessions, tokens, Claims |
| BCUS | Customer/Account association and current Address evidence | Customer, Account, Address, Consent, Preference |
| BPRD | Conditional current Product/Product Variant reference | Product identity, lifecycle, content, media, sellability |
| BINV | Availability, reservation, and permitted fulfilment-coordination evidence | All Inventory and Stock truth and effects |
| BPRC | Shipping Rate, Money, Currency, and commercial evidence | Pricing, calculation, Promotion, Discount, Tax, conversion, totals |
| BCART | No required normative dependency; purchase intent remains upstream of Checkout | Cart identity, contents, lifecycle, mutation, totals |
| BCAT | No required normative dependency | Category truth |
| BSRCH | No material dependency | Search truth, indexes, documents, ranking, results, Projections |
| BCHK | Delivery choice, Address context, rate revalidation, completed handoff evidence | Checkout identity, lifecycle, validation, orchestration, success |
| BORD | Order, Order Item, snapshot, Fulfilment Group, lifecycle, delivery, and cancellation coordination evidence | Order identity, creation, lifecycle, history, snapshots, cancellation, commercial history |
| Payment Domain | Conditional future governed evidence only | All Payment, provider, financial-effect, and reconciliation truth; backend identity unresolved |
| Return Domain | Abstract future governed handoff only | Return eligibility, authorization, lifecycle, inspection, disposition, Refund policy |
| Administration | Protected consumer of Shipping-owned Use Cases | Administrative coordination; Roles and Permissions unresolved |
| Notifications | Conditional non-authoritative communication need | Notification delivery truth; provider and Contracts unresolved |
| Reporting | Protected Shipping evidence consumer | Reporting definitions, analytics, exports, and Projections |
| CMS | No material normative dependency | CMS truth; independently eligible backend roadmap candidate |

## 8. Contract, Data, Transaction, Provider, and Event Boundaries

BSHP Contracts must remain abstract and project-owned. They may describe governed semantics, evidence, authority, failure, compatibility, and correlation without defining concrete API routes, HTTP operations or statuses, DTO fields, payload schemas, persistence schemas, provider models, event names, broker topology, or deployment mechanisms.

Shipping-owned persistence and local Database Transactions must remain separate from BORD, BINV, BPRC, BCHK, BCUS, BIDN, provider, Payment, Return, Notifications, and Reporting storage and effects. Cross-boundary work must preserve explicit state, uncertainty, duplicate safety, reconciliation, and truthful outcomes rather than imply distributed atomicity.

Any provider, Callback, Webhook, event, or messaging behavior remains conditional. If separately governed, it must inherit BEB security, authenticity, integrity, compatibility, replay, duplicate, observability, and recovery obligations.

## 9. Open Product Decisions

The following **6 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and Accepted ADR-0012 remain unresolved by BSHP:

| Item | Open Product Decision | Preserved BSHP effect |
| ---: | --- | --- |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, Carrier, service, geography, eligibility, or fee policy is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or Promotion treatment is selected or inferred. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, timing, or fulfilment-stop policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, exchange, Refund, or reverse-logistics policy or implementation is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No support channel, owner, route, workflow, expectation, or service target is selected. |

## 10. Open Architecture Decisions

The following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and Accepted ADR-0012 remain unresolved by BSHP:

| Item | Open Architecture Decision | Preserved BSHP effect |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 6 | Shipping provider selection. | No Shipping Provider, Carrier, integration, or provider Contract is selected. |
| 7 | Transactional notification provider selection. | No notification provider, channel, or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, tracking, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No messaging adoption, broker, topic, queue, registry, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, RTO, RPO, or numerical recovery target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Shipping persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

## 11. Explicit Non-Decisions and Roadmap Boundary

BSHP does not select or define a Shipping Provider, Carrier, service level, delivery area, delivery fee policy, free-delivery threshold, warehouse strategy, fulfilment strategy, Shipment identifier format, tracking provider or mechanism, Return or reverse-logistics implementation, API route, HTTP method or status, DTO or payload, database schema, table, column, index, ORM mapping, event name or schema, topic, queue, broker, messaging technology, Redis or cache adoption, infrastructure product, hosting or deployment topology, retry count, timeout, TTL, retention period, rate limit, SLA, SLO, recovery objective, or other numerical operational value.

It does not resolve Pricing, Promotion, Discount, Tax, cancellation, Payment, Refund, Return, exchange, Inventory, fulfilment, warehouse, delivery, fraud, support, notification, reporting, or other Product policy. It defines no Role or Permission matrix, concrete Authorization mechanism, lifecycle graph, provider behavior, or implementation Contract.

CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered relative to each other. Return, Administration, Notifications, and Reporting remain separate and unresolved. BSHP does not imply Return or any other capability follows it, and every Backend Specification identity, title, path, scope code, decomposition, and ordering after BSHP remains unresolved.

## 12. Risks and Controls

| Risk | Implementation-neutral control direction |
| --- | --- |
| Order authority leakage | Consume only governed BORD handoffs and keep Order and Shipment state distinct. |
| Inventory authority leakage | Consume BINV evidence and request only permitted effects without treating fulfilment progress as Inventory truth. |
| Pricing authority leakage | Preserve governed commercial evidence and revalidate disagreement without recalculation. |
| Invalid or stale Address | Consume governed BCUS/BORD context and fail safely on invalid or stale evidence. |
| Duplicate Shipment or provider effect | Require explicit idempotency, correlation, replay protection, and reconciliation. |
| Provider outage or ambiguous outcome | Preserve uncertainty, truthful failure classification, recovery, and reconciliation. |
| False Dispatch, tracking, or delivery state | Require sufficient authentic correlated evidence and retain source distinctions. |
| Cancellation/Fulfilment race | Revalidate BORD and Shipping state and keep requests and effects distinct. |
| Return authority leakage | Keep BSHP forward-only and require future governed Return context for any handoff. |
| Sensitive Data exposure | Minimize, conceal, and protect Address, tracking, provider, Customer, and operational evidence. |
| Unauthorized operational action | Enforce contextual Authorization and proportional audit through Shipping-owned Use Cases. |
| Non-authoritative representation becomes truth | Keep Notifications, Reporting, Search, caches, and Projections derived and non-authoritative. |
| Unresolved decision embedded as mechanism | Preserve the 6 Product and 10 Architecture decisions and explicit exclusions. |

## 13. Required Governance Reviews

Before promotion to `1.0.0 Approved`, governance review must confirm:

- complete and accurate specialization of all 43 Shipping and Fulfilment Domain Requirements;
- one-to-one BSHP Requirement, Acceptance Criterion, and traceability accounting;
- complete, defensible accounting for all 56 BEB Requirements;
- bounded BIDN, BCUS, BPRD, BINV, BPRC, BCART, BCAT, BSRCH, BCHK, and BORD consumption without authority transfer;
- Shipping-and-Fulfilment-only authority and preservation of Payment, Return, Administration, Notifications, Reporting, CMS, and all other external authority;
- preservation of all 6 Product and 10 Architecture decisions;
- security, privacy, Contextual Authorization, integrity, failure, uncertainty, recovery, reconciliation, observability, audit, accessibility, compatibility, migration, and verification completeness;
- absence of invented Product policy, provider, mechanism, API/DTO, persistence design, event Contract, infrastructure, numerical value, or later roadmap position;
- Shipping and Fulfilment ownership review, affected Order, Checkout, Inventory, Pricing, Customer, Identity, Product, Payment, Return, Administration, Notifications, and Reporting ownership review where their boundaries intersect;
- Security, Testing, Architecture, and Documentation review; and
- lifecycle, metadata, Revision History, reference, terminology, whitespace, and final-diff consistency.

This Draft does not claim that any approval review has been completed.

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
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0012-post-order-backend-specification.md`
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
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-21 | Draft | Established the initial Shipping-and-Fulfilment-only BSHP Draft immediately after BORD under Accepted ADR-0012, specializing the Approved Shipping and Fulfilment Domain while preserving upstream and unresolved external authority. |

## 16. Final Validation

Before Draft approval review, verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, owner is `Backend / Shipping and Fulfilment`, and scope is `BSHP`;
2. all 50 BSHP Requirements are unique, contiguous, implementation-neutral, and paired one-to-one with Acceptance Criteria and traceability rows;
3. all 43 Shipping and Fulfilment Domain Requirements are accounted for without authority expansion;
4. all 56 BEB Requirements are accounted for with defensible applicability classifications;
5. BORD, BCHK, BINV, BPRC, BIDN, BCUS, and other applicable upstream boundaries remain bounded and authoritative in their owned concerns;
6. Payment, Return, Administration, Notifications, Reporting, CMS, Category, Cart, Search, and all other external authority remain outside BSHP;
7. all 6 Product and 10 Architecture decisions remain unresolved;
8. no provider, policy, route, method, status, DTO, payload, persistence design, event Contract, messaging/cache/infrastructure mechanism, numerical value, or later roadmap position is selected;
9. security, privacy, Authorization, integrity, failure, uncertainty, duplicate, replay, concurrency, recovery, reconciliation, observability, audit, accessibility, compatibility, migration, and verification obligations are complete;
10. CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered, Return remains separate and unresolved, and every post-BSHP roadmap position remains unresolved;
11. related references, terminology, Markdown, and whitespace validation pass; and
12. the final change creates only `specifications/backend/shipping/shipping-backend.md`, remains unstaged, uncommitted, and unpushed.
