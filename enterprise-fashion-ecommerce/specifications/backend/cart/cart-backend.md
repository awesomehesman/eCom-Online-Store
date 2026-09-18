---
title: Cart Backend Specification
version: 0.1.0
status: Draft
owner: Cart
last_updated: 2026-09-17
authoritative: false
scope: BCART
---

# Cart Backend Specification

## 1. Purpose

This Specification defines the implementation-facing backend obligations for the Cart-only boundary authorized by Accepted ADR-0007. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the governed Cart backend scope. It remains `authoritative: false`, subordinate to higher-authority governing sources and the Approved Cart Domain, and does not claim repository-wide authority or resolve an Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BCART-REQ-001 — Lifecycle, Scope, and Authority

BCART MUST retain scope `BCART`, `authoritative: false`, Draft lifecycle metadata until separately Approved, and authority bounded to Cart backend behavior under the Approved Cart Domain and Accepted ADR-0007.

### BCART-REQ-002 — Complete Cart Domain Specialization

BCART MUST implement every applicable `REQ-CART-001` through `REQ-CART-033` obligation without weakening, redefining, or transferring Cart Domain authority.

### BCART-REQ-003 — Cart-Only Decomposition and Roadmap Boundary

BCART MUST remain Cart-only in the canonical sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART`; it MUST NOT establish Category or any Backend Specification identity, decomposition, scope, path, or order after BCART.

### BCART-REQ-004 — BEB Inheritance

BCART MUST inherit every materially applicable BEB Requirement, apply the classifications in §11, and preserve conditional obligations if later Approved Cart capabilities make them applicable.

### BCART-REQ-005 — BIDN Consumption Boundary

BCART MAY consume trusted Principal, Authentication, and Session evidence from BIDN only where governed Cart association or protected behavior requires it. Identity evidence alone MUST NOT grant contextual Cart Authorization or transfer Identity authority.

### BCART-REQ-006 — BCUS Consumption Boundary

BCART MAY consume governed Customer or Account evidence from BCUS only where Approved Cart policy permits association or protected behavior. It MUST NOT define Customer, Account, Address, Preference, Consent, Wishlist, guest eligibility, migration, or merge policy.

### BCART-REQ-007 — BPRD Consumption Boundary

BCART MAY consume governed Product and Product Variant identity, structural sellability, publication, and change evidence from BPRD as required for Cart Item integrity. Retained Product information MUST NOT become current Product truth or transfer Product authority.

### BCART-REQ-008 — BINV Consumption Boundary

BCART MAY consume governed Inventory validation and availability evidence from BINV where policy permits. Cart presence or quantity MUST NOT become Stock, Stock Reservation, Available-to-Sell, an availability guarantee, or purchase eligibility.

### BCART-REQ-009 — BPRC Consumption Boundary

BCART MAY consume governed current or projected Pricing evidence from BPRC for permitted presentation and change handling. Retained or displayed values MUST NOT become authoritative, locked, or final Pricing truth.

## 3. Backend Architecture, Contracts, and Persistence

### BCART-REQ-010 — Modular and Hexagonal Boundary

BCART MUST preserve the modular-monolith and hexagonal dependency rules, keeping Cart rules in the Cart Domain/Application boundary and infrastructure behind owned Ports and Adapters.

### BCART-REQ-011 — Abstract Cart Public Contract

BCART MUST expose only intentional, versionable Cart Contracts that preserve Cart ownership, association, isolation, Product Variant association, validation, concurrency, compatibility, and safe failure semantics. No concrete route, method, status, DTO, payload, or event schema is selected here.

### BCART-REQ-012 — Cart Use-Case Orchestration

Cart commands and queries MUST be explicit Use Cases with validated inputs, truthful outcomes, owned transaction boundaries, and no business logic delegated to controllers, persistence, clients, or external Adapters.

### BCART-REQ-013 — Cart Persistence and Data Ownership

BCART MUST own persistence only for governed Cart intent and MUST protect Cart association, membership, quantity, and accepted mutation integrity through project-owned abstractions and database integrity where applicable. It MUST NOT select schema, table, ORM, index, identifier, retention, expiration, or lifecycle design.

## 4. Cart Intent, Association, and Product Boundary

### BCART-REQ-014 — Authoritative Provisional Cart Intent

BCART MUST own supported Cart existence, Cart Item membership, selected Product Variant references, intended quantities, governed association, and accepted mutation outcomes while representing all Cart intent as provisional and non-committal.

### BCART-REQ-015 — Cross-Domain Authority Separation

BCART MUST NOT establish Product, Category, Pricing, Inventory, Search, Checkout, Order, Payment, Shipping and Fulfilment, Customer, Identity, Administration, Reporting, Notifications, CMS, or another Domain's truth or policy.

### BCART-REQ-016 — Cart Association and Isolation

Each Cart MUST be associated only with an Approved policy-permitted context; supplied identifiers MUST NOT grant access, and unknown, inaccessible, mismatched, ambiguous, or cross-context references MUST expose or mutate no protected Cart state.

### BCART-REQ-017 — Governed Product Variant Reference

Each Cart Item MUST retain a valid governed Product Variant reference without creating or redefining Product identity, content, media, publication, visibility, or structural sellability.

### BCART-REQ-018 — Product Change Safety

Unknown, removed, unpublished, structurally unsellable, or materially changed Product or Product Variant state MUST yield safe distinguishable outcomes and governed recovery where permitted, without inventing existing-item eligibility or confusing Product state with Inventory state.

## 5. Cart Item, Quantity, Mutation, and Lifecycle

### BCART-REQ-019 — Cart Item Membership

BCART MUST preserve supported Product Variant membership and intended quantity as Cart intent; a Cart Item MUST NOT become an Order Item, Stock Reservation, Price snapshot, or Product copy.

### BCART-REQ-020 — Cross-Cart Integrity

Every operation MUST affect only the intended permitted Cart and Cart Item, and cross-Cart or mismatched references MUST fail without disclosing or changing protected state.

### BCART-REQ-021 — Governed Cart Mutation

Add, quantity-change, and removal Use Cases MUST return explicit consistent outcomes for the correct Cart. Invalid or unsupported quantities, including zero where not governed as removal, MUST be rejected safely without inventing numerical limits.

### BCART-REQ-022 — Duplicate Add Semantics

Repeated adds MUST NOT unintentionally multiply membership or quantity. BCART MUST preserve combine, replace, or distinct-item behavior as unresolved until Approved policy governs it.

### BCART-REQ-023 — Governed Lifecycle Outcomes

Creation, active use, mutation, validation, clearing, abandonment, expiration, merge, transition toward Checkout, and recovery MUST exist only where Approved policy governs them; BCART MUST NOT invent a lifecycle state machine, duration, cleanup, or clearing trigger.

### BCART-REQ-024 — Persistence and Merge Policy Deferral

Guest and authenticated persistence, expiration, anonymous migration, merge, precedence, conflict handling, and post-Checkout or post-purchase clearing MUST remain unresolved, and retained or merged state MUST NOT silently overwrite accepted intent.

### BCART-REQ-025 — Customer and Guest Non-Authority

BCART MAY use governed Customer, Principal, Session, Account, or guest context only as Approved policy permits and MUST NOT define Customer identity, Account, Address, Preference, Consent, Wishlist, Authentication, Authorization policy, or guest capability.

## 6. Inventory, Pricing, and Downstream Boundaries

### BCART-REQ-026 — Inventory Non-Authority

BCART MUST NOT own, infer, modify, or prove Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, reserved quantity, availability guarantee, or purchase eligibility.

### BCART-REQ-027 — Inventory Validation and Staleness

Where policy permits, BCART MAY request Inventory validation through a governed Contract; displayed, cached, retained, unknown, insufficient, changed, unavailable, or stale Inventory evidence MUST remain non-authoritative and require revalidation before governed commitment.

### BCART-REQ-028 — Pricing Non-Authority

BCART MAY present governed Price, Discount, Promotion, Voucher, tax, total, or savings evidence but MUST NOT calculate or establish authoritative commercial values, which remain subject to trusted Checkout revalidation.

### BCART-REQ-029 — Pricing Change Safety

Unknown, changed, or stale commercial evidence MUST NOT be represented as locked or final, and material changes MUST be explainable before commitment without selecting snapshot, locking, expiry, stacking, precedence, Voucher timing, rounding, currency, tax, or repricing policy.

### BCART-REQ-030 — Checkout Revalidation Boundary

BCART MAY provide governed Cart intent for eventual Checkout consumption but MUST NOT define Checkout, prove Checkout success, or prematurely define its backend Contract. Checkout remains responsible for current authoritative revalidation before commitment.

### BCART-REQ-031 — Order Non-Authority

Cart contents MAY contribute input toward Checkout and only through governed Checkout outcomes toward Order creation; BCART MUST NOT create Order truth, Order Item commercial truth, Order status, or confirmed commercial history, and clearing timing remains unresolved.

### BCART-REQ-032 — Payment Non-Authority

BCART MUST NOT establish Payment, Authorization, Capture, Settlement, success, or other Payment truth, infer it from Cart or client state, or allow Payment outcomes silently to redefine Cart intent.

### BCART-REQ-033 — Shipping and Fulfilment Non-Authority

BCART MUST NOT establish Shipment, delivery, tracking, carrier, Shipping Rate, operational eligibility, or fulfilment truth; any presented delivery evidence remains owned and revalidated by its governing Domain.

## 7. Integrity, Security, Operations, and Verification

### BCART-REQ-034 — Concurrent Mutation Integrity

Concurrent, duplicate, retried, replayed, conflicting, or stale Cart mutations MUST NOT silently overwrite accepted intent or multiply effects, and MUST provide distinguishable safe refresh, retry, reconciliation, or escalation where governed.

### BCART-REQ-035 — Uncertain Mutation and Idempotency Safety

Where duplicate effects matter, ambiguous outcomes MUST be verified against authoritative Cart state before repetition; applicable idempotency and replay controls MUST preserve accepted intent without selecting keys, retention, retry counts, or mechanisms.

### BCART-REQ-036 — Authentication and Contextual Authorization

Where required, BIDN Authentication MUST establish the Principal while trusted server-side Cart Authorization enforces ownership, association, action, and Resource access with least privilege and default denial. UI state, Roles, Claims, browser state, or identifiers alone MUST NOT grant authority; administrative access MUST be separately authorized without defining a Role matrix.

### BCART-REQ-037 — Cart Security and Data Protection

BCART MUST isolate Resources, minimize Customer PII, validate untrusted input, and protect Sensitive Data, Secrets, credentials, and internal detail across storage, Contracts, logs, errors, events, and Projections. Failure behavior MUST prevent protected Resource enumeration and fail safely.

### BCART-REQ-038 — Recovery and Reconciliation

Uncertain or duplicate mutations, stale Cart or upstream evidence, conflicts, invalid Product Variant references, invalid or expired contexts, and dependency failures MUST support authorized, observable, duplicate-safe recovery or reconciliation where permitted without fabricating external truth.

### BCART-REQ-039 — Accessible Cart Outcomes

Backend Cart Contracts and failure semantics MUST supply stable, distinguishable information sufficient for applicable WCAG 2.2 AA Cart experiences without selecting frontend implementation or claiming certification.

### BCART-REQ-040 — Bounded Delivery and Failure Containment

Cart operations MUST be bounded and observable under governing standards; degradation MUST NOT promote stale or uncertain Product, Pricing, Inventory, association, or Authorization evidence to trusted truth, and no numerical target is selected.

### BCART-REQ-041 — Proportional Audit and Observability

Material privileged, administrative, reconciliation, security-sensitive, or other high-Risk Cart actions MUST produce proportionate Audit Records and safe operational telemetry, while ordinary reads and routine low-Risk mutations MUST NOT automatically receive high-Risk treatment.

### BCART-REQ-042 — Conditional Events and Messaging

Cart events MUST exist only when Architecture, an Approved Contract, downstream reliability, or synchronized governance requires them. Any governed event MUST preserve Cart authority, Authorization, privacy, compatibility, duplicate and replay safety, and non-authoritative consumer Projections without selecting event names, schemas, brokers, topics, queues, or delivery mechanisms.

### BCART-REQ-043 — Explicit Failure Outcomes

Applicable unknown, inaccessible, invalid, stale, duplicate, replayed, concurrent, conflicting, cross-Cart, unauthorized, expired, dependency-failed, and uncertain cases MUST yield distinguishable, safe, explainable, recoverable outcomes without exposing protected existence or internal details or representing uncertainty as success.

### BCART-REQ-044 — Complete Cart Verification

Verification MUST cover all applicable positive, negative, boundary, security, concurrency, recovery, accessibility, compatibility, operational, Contract, and conditional-event behavior required by `REQ-CART-033`, with no selected framework or numerical coverage target.

### BCART-REQ-045 — Compatibility, Configuration, and Neutrality

Cart Contracts, data changes, configuration, reachable feature states, and deployments MUST preserve compatibility, safe migration, isolation, and recovery under BEB. BCART MUST remain neutral on every excluded mechanism, unresolved policy, Open Decision, and later roadmap position.

## 8. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BCART-AC-001 | BCART-REQ-001 | Metadata shows `0.1.0 Draft`, owner `Cart`, `authoritative: false`, and scope `BCART`; Draft status is non-normative and authority is bounded. |
| BCART-AC-002 | BCART-REQ-002 | The Cart coverage matrix accounts for `REQ-CART-001` through `REQ-CART-033` without a gap or transferred authority. |
| BCART-AC-003 | BCART-REQ-003 | Only Cart is specialized; the canonical sequence ends at BCART and Category plus every later position remain unresolved. |
| BCART-AC-004 | BCART-REQ-004 | The BEB matrix accounts for `BEB-REQ-001` through `BEB-REQ-056` and traces applicable obligations. |
| BCART-AC-005 | BCART-REQ-005 | Trusted BIDN evidence is bounded to identity context and cannot itself authorize Cart access. |
| BCART-AC-006 | BCART-REQ-006 | BCUS evidence is consumed without defining Customer, Account, guest, migration, or merge policy. |
| BCART-AC-007 | BCART-REQ-007 | Product evidence supports valid association and change handling without becoming retained Cart truth. |
| BCART-AC-008 | BCART-REQ-008 | Inventory evidence remains non-authoritative and Cart intent creates no reservation or availability guarantee. |
| BCART-AC-009 | BCART-REQ-009 | Pricing evidence is presented as governed and revalidatable, never locked, final, or Cart-owned. |
| BCART-AC-010 | BCART-REQ-010 | Dependency and layer checks preserve the Cart module and inward dependency direction. |
| BCART-AC-011 | BCART-REQ-011 | Contract review shows intentional versionable abstract Cart surfaces and no concrete API or payload invention. |
| BCART-AC-012 | BCART-REQ-012 | Each Cart command/query has validated input, explicit outcome, orchestration, and an owned transaction boundary where applicable. |
| BCART-AC-013 | BCART-REQ-013 | Persistence review shows only Cart-owned intent behind Ports with integrity safeguards and no physical design or retention policy selected. |
| BCART-AC-014 | BCART-REQ-014 | Cart truth covers existence, membership, references, quantities, association, and accepted mutations while remaining provisional. |
| BCART-AC-015 | BCART-REQ-015 | Tests and contract review prove BCART cannot establish another Domain's truth or policy. |
| BCART-AC-016 | BCART-REQ-016 | Association, ownership, isolation, and concealment tests reject supplied, mismatched, and cross-context identifiers safely. |
| BCART-AC-017 | BCART-REQ-017 | Every item retains governed Product Variant identity without creating or redefining Product state. |
| BCART-AC-018 | BCART-REQ-018 | Each governed Product-change condition yields a safe distinguishable outcome without invented eligibility. |
| BCART-AC-019 | BCART-REQ-019 | Membership and quantity remain Cart intent and cannot become Order, Inventory, Pricing, or Product truth. |
| BCART-AC-020 | BCART-REQ-020 | Cross-Cart and mismatched operations neither disclose nor mutate protected state. |
| BCART-AC-021 | BCART-REQ-021 | Add, quantity change, removal, invalid quantity, and unsupported quantity produce explicit consistent outcomes without numerical invention. |
| BCART-AC-022 | BCART-REQ-022 | Repeated adds do not multiply effects unintentionally and no combine/replace/distinct policy is implied. |
| BCART-AC-023 | BCART-REQ-023 | Lifecycle behavior exists only when governed and defines no invented state machine, duration, cleanup, or trigger. |
| BCART-AC-024 | BCART-REQ-024 | Persistence, expiration, migration, merge, precedence, conflict, and clearing remain unresolved; intent is not silently overwritten. |
| BCART-AC-025 | BCART-REQ-025 | Customer or guest context is bounded to governed association and creates no Customer, Identity, Account, Wishlist, or guest policy. |
| BCART-AC-026 | BCART-REQ-026 | Cart presence and quantity cannot create, change, infer, or prove any listed Inventory truth. |
| BCART-AC-027 | BCART-REQ-027 | Inventory validation is governed, non-authoritative evidence is labelled accordingly, and commitment requires current revalidation. |
| BCART-AC-028 | BCART-REQ-028 | Presented commercial values remain BPRC-owned evidence and require Checkout revalidation. |
| BCART-AC-029 | BCART-REQ-029 | Stale/changed commercial evidence is explainable and non-final, with every listed Pricing policy left unresolved. |
| BCART-AC-030 | BCART-REQ-030 | BCART can provide Cart intent but cannot define or prove Checkout; current external truth must be revalidated. |
| BCART-AC-031 | BCART-REQ-031 | Cart contributes no Order truth and post-Order clearing timing remains unresolved. |
| BCART-AC-032 | BCART-REQ-032 | No Cart or client condition establishes Payment truth or silently redefines intent. |
| BCART-AC-033 | BCART-REQ-033 | No Cart evidence establishes Shipping or Fulfilment truth, and delivery evidence remains externally owned. |
| BCART-AC-034 | BCART-REQ-034 | Concurrency, duplicate, retry, replay, stale, and conflict tests preserve accepted intent and prevent multiplied effects. |
| BCART-AC-035 | BCART-REQ-035 | Uncertain effects are verified before repetition and applicable duplicate safety selects no mechanism or numerical value. |
| BCART-AC-036 | BCART-REQ-036 | Server-side tests enforce contextual least privilege/default denial and reject Roles, Claims, UI state, or identifiers as sole authority. |
| BCART-AC-037 | BCART-REQ-037 | Security verification covers isolation, minimization, validation, concealment, safe logging, errors, Contracts, and events. |
| BCART-AC-038 | BCART-REQ-038 | Recovery cases are distinguishable, authorized, observable, duplicate-safe, and invent no external truth. |
| BCART-AC-039 | BCART-REQ-039 | Contract outcomes support accessible Cart feedback without selecting UI implementation or claiming certification. |
| BCART-AC-040 | BCART-REQ-040 | Load, degradation, and dependency-failure evidence proves bounded work and prevents stale/uncertain evidence becoming trusted truth. |
| BCART-AC-041 | BCART-REQ-041 | High-Risk actions yield separate safe audit and operational evidence; ordinary low-Risk activity is not over-classified. |
| BCART-AC-042 | BCART-REQ-042 | Events remain conditional and any governed event preserves ownership, protection, compatibility, duplicate/replay safety, and projection non-authority. |
| BCART-AC-043 | BCART-REQ-043 | All listed failures are safe, distinguishable, explainable, recoverable where permitted, concealed appropriately, and never misstate uncertainty as success. |
| BCART-AC-044 | BCART-REQ-044 | Verification evidence covers every `REQ-CART-033` category and all BCART Requirements without a framework or target mandate. |
| BCART-AC-045 | BCART-REQ-045 | Compatibility, configuration, migration, reachable-state, and exclusion review finds no invented policy, mechanism, number, or roadmap position. |

## 9. Requirement Traceability

Each row traces its BCART Requirement to the Acceptance Criterion with the identical numeric suffix (for example, `BCART-REQ-014` → `BCART-AC-014`), completing the Cart Domain Requirement → BCART Requirement → BCART Acceptance Criterion chain without a second mapping convention.

| BCART Requirement | Cart Domain Requirement | BEB inheritance | Additional source / upstream evidence |
| --- | --- | --- | --- |
| BCART-REQ-001 | REQ-CART-001 | BEB-REQ-001–004, 056 | ADR-0007; ARCHITECTURE.md §35 |
| BCART-REQ-002 | REQ-CART-001–033 | BEB-REQ-003–004, 055 | Cart Domain Specification |
| BCART-REQ-003 | REQ-CART-003 | BEB-REQ-003–004, 056 | ADR-0007 Decision |
| BCART-REQ-004 | REQ-CART-033 | BEB-REQ-001–056 | ADR-0001; BEB |
| BCART-REQ-005 | REQ-CART-004, 013, 024 | BEB-REQ-011, 019–020, 043 | BIDN-REQ-004–005, 011–013, 021, 024 |
| BCART-REQ-006 | REQ-CART-004, 013, 024–025 | BEB-REQ-003, 011, 019–021 | BCUS-REQ-005–006, 012–016, 034–035 |
| BCART-REQ-007 | REQ-CART-005–006 | BEB-REQ-003, 021, 024, 041 | BPRD-REQ-012–016, 020–024, 033 |
| BCART-REQ-008 | REQ-CART-014–015 | BEB-REQ-003, 021, 024, 041 | BINV-REQ-014–024, 029–032 |
| BCART-REQ-009 | REQ-CART-016–017 | BEB-REQ-003, 021, 024, 041 | BPRC-REQ-015–029 |
| BCART-REQ-010 | REQ-CART-003, 031 | BEB-REQ-005–007 | ARCHITECTURE.md §§8, 11, 35; SPRING.md |
| BCART-REQ-011 | REQ-CART-031 | BEB-REQ-008, 013–018 | API.md; ADR-0007 Explicit Non-Decisions |
| BCART-REQ-012 | REQ-CART-009–010, 022–023, 032 | BEB-REQ-009–012, 025 | SPRING.md; JAVA.md |
| BCART-REQ-013 | REQ-CART-002, 004, 007–012 | BEB-REQ-021–025, 027 | DATABASE.md; POSTGRES.md |
| BCART-REQ-014 | REQ-CART-002 | BEB-REQ-003, 021, 024 | PRODUCT.md §§12.2, 14.3; REQ-BUS-009 |
| BCART-REQ-015 | REQ-CART-003 | BEB-REQ-003, 021 | ADR-0007 Authority Boundary |
| BCART-REQ-016 | REQ-CART-004 | BEB-REQ-011, 019–020, 043 | BIDN; BCUS; SECURITY-STANDARDS.md |
| BCART-REQ-017 | REQ-CART-005 | BEB-REQ-015, 021, 041 | BPRD; REQ-BUS-005–006, 009 |
| BCART-REQ-018 | REQ-CART-006 | BEB-REQ-017, 021, 041–042 | BPRD; REQ-BUS-006, 042 |
| BCART-REQ-019 | REQ-CART-007 | BEB-REQ-021, 023–024 | BPRD; BINV; GLOSSARY.md |
| BCART-REQ-020 | REQ-CART-008 | BEB-REQ-011, 020, 023 | SECURITY-STANDARDS.md |
| BCART-REQ-021 | REQ-CART-009 | BEB-REQ-009–10, 015, 025 | API.md; DATABASE.md |
| BCART-REQ-022 | REQ-CART-010 | BEB-REQ-025, 027, 029–031 | API.md; DATABASE.md |
| BCART-REQ-023 | REQ-CART-011 | BEB-REQ-003, 021, 056 | PRODUCT.md §24; ADR-0007 |
| BCART-REQ-024 | REQ-CART-012 | BEB-REQ-003, 025, 027, 056 | PRODUCT.md §24; ADR-0007 |
| BCART-REQ-025 | REQ-CART-013 | BEB-REQ-003, 011, 019–021 | BIDN; BCUS; PRODUCT.md §24 |
| BCART-REQ-026 | REQ-CART-014 | BEB-REQ-003, 021, 024 | BINV; ARCHITECTURE.md §20.3 |
| BCART-REQ-027 | REQ-CART-015 | BEB-REQ-017, 021, 041–042 | BINV; API.md §§41, 43 |
| BCART-REQ-028 | REQ-CART-016 | BEB-REQ-003, 008, 021 | BPRC; API.md §§42–43 |
| BCART-REQ-029 | REQ-CART-017 | BEB-REQ-017, 021, 041 | BPRC; PRODUCT.md §24 |
| BCART-REQ-030 | REQ-CART-018 | BEB-REQ-003, 008, 021, 041 | Checkout Domain; API.md §43 |
| BCART-REQ-031 | REQ-CART-019 | BEB-REQ-003, 021, 024 | Checkout and Order Domains |
| BCART-REQ-032 | REQ-CART-020 | BEB-REQ-003, 021, 024 | Payment Domain; ARCHITECTURE.md §20.2 |
| BCART-REQ-033 | REQ-CART-021 | BEB-REQ-003, 021, 041 | Shipping Domain |
| BCART-REQ-034 | REQ-CART-022 | BEB-REQ-025, 027, 029–031 | DATABASE.md §§15–19; API.md §24 |
| BCART-REQ-035 | REQ-CART-023 | BEB-REQ-028–031, 041–042 | DATABASE.md §§51–52; API.md §22 |
| BCART-REQ-036 | REQ-CART-024 | BEB-REQ-011, 019–020 | BIDN; SECURITY-STANDARDS.md |
| BCART-REQ-037 | REQ-CART-025 | BEB-REQ-015, 020, 043–044 | SECURITY-STANDARDS.md |
| BCART-REQ-038 | REQ-CART-026 | BEB-REQ-028, 034, 041–042, 046 | DATABASE.md §52; API.md §§54, 58 |
| BCART-REQ-039 | REQ-CART-027 | BEB-REQ-016–018, 041, 049–051 | Cart Domain §21; API.md |
| BCART-REQ-040 | REQ-CART-028 | BEB-REQ-041, 046, 051 | ARCHITECTURE.md §4.4; TESTING-STANDARDS.md |
| BCART-REQ-041 | REQ-CART-029 | BEB-REQ-045–046 | SECURITY-STANDARDS.md §27; DATABASE.md §42 |
| BCART-REQ-042 | REQ-CART-030 | BEB-REQ-026, 029–040, 056 | EVENTS.md; ARCHITECTURE.md §§14, 34 |
| BCART-REQ-043 | REQ-CART-032 | BEB-REQ-017, 041–043 | API.md §§52–58; JAVA.md §28 |
| BCART-REQ-044 | REQ-CART-033 | BEB-REQ-052–055 | TESTING-STANDARDS.md; BEB |
| BCART-REQ-045 | REQ-CART-011–012, 028, 030–033 | BEB-REQ-047–051, 056 | ADR-0007; ARCHITECTURE.md §§34–35 |

## 10. Cart Domain Coverage Matrix

| Cart Domain Requirement | BCART coverage |
| --- | --- |
| REQ-CART-001 | BCART-REQ-001–002 |
| REQ-CART-002 | BCART-REQ-014 |
| REQ-CART-003 | BCART-REQ-003, 015 |
| REQ-CART-004 | BCART-REQ-005–006, 016 |
| REQ-CART-005 | BCART-REQ-007, 017 |
| REQ-CART-006 | BCART-REQ-007, 018 |
| REQ-CART-007 | BCART-REQ-019 |
| REQ-CART-008 | BCART-REQ-020 |
| REQ-CART-009 | BCART-REQ-021 |
| REQ-CART-010 | BCART-REQ-022 |
| REQ-CART-011 | BCART-REQ-023 |
| REQ-CART-012 | BCART-REQ-024 |
| REQ-CART-013 | BCART-REQ-006, 025 |
| REQ-CART-014 | BCART-REQ-008, 026 |
| REQ-CART-015 | BCART-REQ-008, 027 |
| REQ-CART-016 | BCART-REQ-009, 028 |
| REQ-CART-017 | BCART-REQ-009, 029 |
| REQ-CART-018 | BCART-REQ-030 |
| REQ-CART-019 | BCART-REQ-031 |
| REQ-CART-020 | BCART-REQ-032 |
| REQ-CART-021 | BCART-REQ-033 |
| REQ-CART-022 | BCART-REQ-034 |
| REQ-CART-023 | BCART-REQ-035 |
| REQ-CART-024 | BCART-REQ-005, 036 |
| REQ-CART-025 | BCART-REQ-037 |
| REQ-CART-026 | BCART-REQ-038 |
| REQ-CART-027 | BCART-REQ-039 |
| REQ-CART-028 | BCART-REQ-040 |
| REQ-CART-029 | BCART-REQ-041 |
| REQ-CART-030 | BCART-REQ-042 |
| REQ-CART-031 | BCART-REQ-011 |
| REQ-CART-032 | BCART-REQ-043 |
| REQ-CART-033 | BCART-REQ-044 |

## 11. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BCART application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, non-authority, and specialization govern BCART. BCART-REQ-001–004. |
| BEB-REQ-005–007 | Applicable | Modular-monolith, hexagonal direction, and layer responsibilities apply. BCART-REQ-010, 012. |
| BEB-REQ-008 | Applicable | Cart requires an intentional public Module Contract. BCART-REQ-011. |
| BEB-REQ-009–010 | Applicable | Cart commands and queries require explicit Use Cases and truthful semantics. BCART-REQ-012, 021. |
| BEB-REQ-011 | Applicable | Cart access and mutation require contextual Authorization. BCART-REQ-016, 036. |
| BEB-REQ-012 | Applicable | Cart mutations require an owned transaction boundary. BCART-REQ-012–013, 034. |
| BEB-REQ-013–018 | Applicable | Applicable Cart Contracts inherit versioning, DTO separation, validation, bounds, errors, description, and evolution safeguards without selecting surfaces. BCART-REQ-011, 039, 043, 045. |
| BEB-REQ-019–020 | Applicable | BCART consumes Identity evidence and enforces contextual Authorization and concealment. BCART-REQ-005, 016, 036–037. |
| BEB-REQ-021–024 | Applicable | Cart owns provisional intent behind Ports with integrity and historical-truth separation. BCART-REQ-013–015, 019, 031. |
| BEB-REQ-025 | Applicable | Cart mutations preserve affected invariants atomically. BCART-REQ-012–013, 021–022, 034. |
| BEB-REQ-026 | Not currently applicable | No external provider call is authorized for BCART; inherited if a later Approved Cart integration requires one. BCART-REQ-042, 045. |
| BEB-REQ-027 | Applicable | Concurrent Cart work must preserve accepted intent. BCART-REQ-013, 034. |
| BEB-REQ-028 | Applicable | Uncertain Cart effects require recoverable state and reconciliation. BCART-REQ-035, 038. |
| BEB-REQ-029–031 | Applicable | Cart mutations require applicable idempotency, replay, and duplicate safety. BCART-REQ-022, 034–035, 038. |
| BEB-REQ-032–034 | Not currently applicable | No Cart external provider capability is authorized; inherited if later Approved governance introduces one. BCART-REQ-038, 042, 045. |
| BEB-REQ-035–036 | Not currently applicable | No Cart Callback or Webhook capability is authorized; authenticity, replay, acknowledgement, and recovery remain conditional. BCART-REQ-042. |
| BEB-REQ-037–039 | Not currently applicable | Cart events are conditional and no event Contract or delivery mechanism is authorized; inherited when separately governed. BCART-REQ-042. |
| BEB-REQ-040 | Applicable | BCART preserves in-process-first delivery and cannot adopt external messaging independently. BCART-REQ-042, 045. |
| BEB-REQ-041–042 | Applicable | Cart failures, uncertainty, recovery, and reconciliation require safe classification and handling. BCART-REQ-038, 043. |
| BEB-REQ-043–044 | Applicable | Cart data, Contracts, evidence, configuration, and operational surfaces require protection and Secret safety. BCART-REQ-037, 041, 045. |
| BEB-REQ-045–046 | Applicable | Material Cart actions require separate Audit Records and safe operational evidence. BCART-REQ-041. |
| BEB-REQ-047–048 | Applicable | Configuration and reachable flagged states must preserve Cart safeguards without selecting mechanisms. BCART-REQ-045. |
| BEB-REQ-049–051 | Applicable | Cart persistence, Contracts, deployments, and workloads require safe migration, compatibility, bounds, and failure containment. BCART-REQ-013, 040, 045. |
| BEB-REQ-052–055 | Applicable | Cart behavior, Adapters, Contracts, architecture, operations, and traceability require complete verification. BCART-REQ-044. |
| BEB-REQ-056 | Applicable | BCART remains policy- and implementation-neutral and preserves every later roadmap decision. BCART-REQ-001–004, 023–024, 042, 045. |

## 12. Upstream Backend Consumption Boundaries

| Upstream | Consumed evidence / Contract | Permitted BCART use | Authority not transferred |
| --- | --- | --- | --- |
| BIDN | Trusted Principal, Authentication, and Session evidence where required | Establish trusted identity context for governed Cart association and protected Use Cases | Identity, credentials, Authentication, Session, revocation, recovery, and contextual Cart Authorization |
| BCUS | Governed Customer or Account identity/association evidence where policy permits | Associate or validate a Cart context without defining eligibility | Customer, Account, Address, Preference, Consent, Wishlist, guest, migration, merge, and Customer isolation policy |
| BPRD | Product/Product Variant identity, publication, structural-sellability, and change evidence | Validate item association and handle Product change safely | Product identity, content, media, publication, visibility, and sellability |
| BINV | Governed availability/Inventory validation evidence where policy permits | Present non-authoritative evidence and detect stale or changed Inventory state | Stock, reservations, Available-to-Sell, movements, adjustments, overselling, and purchase eligibility |
| BPRC | Governed current/projected Pricing evidence and material-change evidence | Present commercial evidence and signal required revalidation | Price, Discount, Promotion, Voucher, tax, totals, currency, rounding, stacking, precedence, and authoritative Pricing truth |

## 13. Cross-Domain Boundaries

- Category taxonomy, hierarchy, membership, navigation, lifecycle, and its backend roadmap position remain Category-owned and unresolved.
- Search and Discovery ranking, indexing, query, projection, provider, and backend roadmap position remain Search-owned and unresolved.
- Checkout may eventually consume Cart-owned intent but owns orchestration, commitment, and final revalidation; BCART defines no Checkout backend Contract or position.
- Order owns confirmed records, Order Items, snapshots, lifecycle, and history; Payment owns Payment truth; Shipping and Fulfilment owns delivery and operational fulfilment truth.
- Administration may use Cart-owned visibility or support capabilities only through governed Authorization and Contracts; it gains no Cart authority and no Role/Permission matrix is defined.
- Reporting, Analytics, Notifications, CMS, Returns, and every other Domain retain their authority; Projections and consumers cannot become authoritative Cart state.

## 14. Open Product and Architecture Decisions

The following **5 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and ADR-0007 remain unresolved:

| Source | Open Product Decision | Preserved BCART boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No guest Cart, guest Checkout, mandatory Account, association, persistence, migration, or continuation policy is selected. |
| 13 | Back-order and pre-order support. | No unavailable-Product eligibility, negative availability, backorder, preorder, or retention policy is selected. |
| 14 | Voucher and promotion stacking policy. | No stacking, precedence, or application policy is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No threshold, wording, presentation, or eligibility rule is selected. |
| 23 | Administrative role and permission matrix. | Contextual least privilege is required without defining Roles or Permissions. |

Cart expiration, persistence duration, abandonment, anonymous-to-authenticated migration, merge and precedence, clearing timing, lifecycle detail, and numerical Cart rules remain unresolved scope boundaries rather than additional `PRODUCT.md` §24 decisions.

The following **8 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and ADR-0007 remain unresolved:

| Source | Open Architecture Decision | Preserved BCART boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC tool or structure is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected for Cart association or access. |
| 8 | Redis introduction and its approved use cases. | No Cart cache, persistence, Session, or Redis use is selected. |
| 9 | External messaging introduction and service selection. | Cart events remain conditional and no messaging service is selected. |
| 12 | Backup retention and production recovery objectives. | Recoverability is required without retention periods, mechanisms, or numerical objectives. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | Cart ownership is preserved without selecting physical schema layout. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | Reachable-state safety is required without selecting a flag mechanism or lifecycle. |

No listed decision is resolved by this Draft.

## 15. Explicit Non-Decisions

BCART does not select guest behavior; persistence, expiration, abandonment, cleanup, merge, precedence, clearing, or lifecycle policy; repeated-add semantics; Cart or quantity limits; reservation behavior; Checkout behavior; Pricing, promotion, tax, currency, or rounding policy; concrete APIs or DTOs; persistence schemas or identifiers; events or messaging; cache or Redis use; providers; infrastructure; Roles or Permissions; numerical targets; or any Backend Specification identity, decomposition, scope, path, or order after BCART.

## 16. Risks and Controls

| Risk | Required control direction |
| --- | --- |
| Stale Product, Pricing, or Inventory evidence | Preserve owning authority, mark uncertainty, and revalidate before commitment. |
| Cart mistaken for reservation or commitment | Keep intent explicitly provisional and separate from Inventory, Checkout, Order, and Payment truth. |
| Unauthorized or cross-Cart access | Enforce trusted server-side contextual Authorization, isolation, and concealment. |
| Duplicate, lost, or concurrent mutation | Preserve accepted intent and make conflicts, duplicates, and uncertainty explicit. |
| Unresolved lifecycle policy embedded locally | Keep persistence, merge, expiration, and clearing behavior unselected. |
| Sensitive or internal detail leakage | Minimize data and protect storage, logs, errors, Contracts, events, and telemetry. |
| Consumer Projection treated as truth | Require authoritative Cart state and prevent reverse ownership. |

## 17. Related Documents

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
- `specifications/adr/ADR-0007-post-pricing-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/search/search-domain.md`

## 18. Governance Review Requirements

Before approval, review MUST confirm complete Cart Domain coverage; one-to-one Requirement, Acceptance Criterion, and traceability; complete BEB accounting; bounded BIDN, BCUS, BPRD, BINV, and BPRC consumption; Cart-only authority; preservation of all 5 Product and 8 Architecture decisions; security, privacy, integrity, failure, recovery, observability, compatibility, accessibility, and verification completeness; and absence of invented policy, mechanisms, numerical values, or later-roadmap positions. This Draft does not claim that approval review is complete.

## 19. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Draft | Established the initial Cart-only backend specialization under Accepted ADR-0007 while preserving all unresolved Product and Architecture decisions and downstream backend sequencing. |

## 20. Final Validation

Before Draft approval review, reviewers MUST validate:

1. metadata is `0.1.0` Draft, owner is `Cart`, `authoritative: false`, and scope is `BCART`;
2. all 45 `BCART-REQ-NNN` identifiers are unique and contiguous;
3. all 33 Approved Cart Domain Requirements are covered without weakened or transferred authority;
4. every BCART Requirement has exactly one Acceptance Criterion and one traceability row;
5. all 56 BEB Requirements are accounted for with defensible applicability classifications;
6. BIDN, BCUS, BPRD, BINV, and BPRC consumption remains bounded and transfers no authority;
7. Cart remains provisional intent and does not become Product, Inventory, Pricing, Checkout, Order, Payment, Shipping, or other Domain truth;
8. all 5 listed Open Product Decisions and all 8 listed Open Architecture Decisions remain unresolved;
9. Category and every Backend Specification identity, decomposition, scope, path, and order after BCART remain unresolved;
10. no concrete API, DTO, schema, event, cache, provider, infrastructure, Role matrix, numerical target, or unresolved business policy is invented;
11. security, privacy, concurrency, idempotency, recovery, accessibility, operations, compatibility, and verification obligations remain complete and traceable;
12. the final change creates only `specifications/backend/cart/cart-backend.md`, passes whitespace validation, and remains unstaged, uncommitted, and unpushed.
