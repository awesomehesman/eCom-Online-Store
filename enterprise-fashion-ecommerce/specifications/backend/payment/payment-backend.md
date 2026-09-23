---
title: Payment Backend Specification
version: 1.0.0
status: Approved
owner: Backend / Payment
last_updated: 2026-09-23
authoritative: false
scope: BPAY
---

# Payment Backend Specification

## 1. Purpose

This Approved Specification defines implementation-facing backend obligations for the Approved Payment Domain under scope `BPAY`. Its Requirements are normative only within the Payment backend scope, remain `authoritative: false`, and remain subordinate to governing sources, Approved Business Requirements, the Approved Payment Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, Accepted ADR-0013, and applicable repository standards.

BPAY uses the Payment-only decomposition authorized by Accepted ADR-0013 immediately after BSHP. It consumes governed evidence without acquiring Identity, Customer, Product, Category, Search and Discovery, Inventory, Pricing, Cart, Checkout, Order, Shipping and Fulfilment, Return, CMS, Administration, Notifications, Reporting, or other authority. It resolves no Open Product or Architecture Decision and establishes no post-BPAY roadmap position.

## 2. Scope, Authority, and Requirements

### BPAY-REQ-001 — Lifecycle, Scope, and Authority

BPAY MUST use scope `BPAY`, remain `authoritative: false`, identify its `1.0.0 Approved` lifecycle, remain normative only within the Payment backend scope, specialize only the Approved Payment Domain, preserve governing-source precedence, and claim no repository-wide authority.

### BPAY-REQ-002 — Complete Payment Domain Specialization

BPAY MUST specialize every materially applicable `REQ-PAY-001` through `REQ-PAY-045` without weakening, duplicating, or transferring the Approved Payment Domain's authority.

### BPAY-REQ-003 — Decomposition and Roadmap Containment

BPAY MUST use a Payment-only decomposition, remain the final currently governed backend roadmap position, and MUST NOT establish any later Backend Specification identity, title, path, scope code, decomposition, or ordering.

### BPAY-REQ-004 — BEB Inheritance

BPAY MUST inherit and explicitly account for every `BEB-REQ-001` through `BEB-REQ-056`, apply every materially applicable obligation, preserve conditional applicability, and MUST NOT copy, weaken, contradict, or transfer BEB authority.

### BPAY-REQ-005 — Modular and Hexagonal Boundary

BPAY MUST preserve the modular-monolith boundary, hexagonal dependency direction, layer responsibilities, and project-owned Ports governed by BEB without selecting extraction, provider, framework, or deployment mechanisms.

### BPAY-REQ-006 — Public Contract and Use-Case Boundary

BPAY MUST expose only intentional abstract public Contracts and explicit Payment-owned Use Cases, distinguish commands from queries, validate governed context and bounds, translate failures safely, and prevent transport, provider, persistence, or framework types from owning Payment policy.

### BPAY-REQ-007 — Payment Persistence and Historical Truth

BPAY MUST own only Payment persistence, preserve applicable Payment, Payment Attempt, Payment Transaction, Refund Transaction, provider-evidence, lifecycle, and reconciliation history, and use project-owned persistence boundaries without selecting schemas, tables, columns, indexes, ORM mappings, identifier formats, or migration designs.

### BPAY-REQ-008 — Transaction and External-Effect Boundary

BPAY MUST use explicit focused local transaction ownership, keep provider and other network calls outside long-running Database Transactions where a reliable alternative exists, and distinguish local commit, external effect, partial completion, timeout, and unknown outcome without implying distributed atomicity.

### BPAY-REQ-009 — Identity and Authentication Consumption Boundary

BPAY MAY consume trusted Principal and Authentication evidence where materially required, but Identity, credentials, Authentication, Sessions, tokens, Claims, and access-evidence authority MUST remain with BIDN; Authentication alone MUST NOT authorize a Payment action.

### BPAY-REQ-010 — Customer and Account Consumption Boundary

BPAY MAY consume governed Customer, Account, ownership, and permitted purchase-context references, but Customer, Account, Address, Consent, Preference, and identity truth MUST remain with BCUS, and later source changes MUST NOT silently rewrite retained Payment history.

### BPAY-REQ-011 — Pricing and Commercial Input Boundary

BPAY MAY consume authoritative BPRC Money, Currency, totals, and applicable commercial evidence, but MUST NOT calculate Price, Discount, Promotion, Voucher, Tax, Shipping charge, conversion, or commercial policy; disagreements MUST remain explicit and reconcilable.

### BPAY-REQ-012 — Cart and Checkout Boundary

BPAY MAY consume governed purchase-intent, initiation, correlation, and orchestration context from BCART and BCHK and return Payment-owned evidence, but MUST NOT own Cart validation, Checkout lifecycle, final Pricing, Address or Shipping selection, Checkout success, or client-reported truth.

### BPAY-REQ-013 — Order Boundary

BPAY MAY consume governed BORD identity and commercial-history context and produce validated evidence for permitted Order coordination, but MUST NOT create an Order, define its lifecycle or history, rewrite an Order Snapshot, or infer that Payment success proves Order existence.

### BPAY-REQ-014 — Inventory Boundary

BPAY MUST NOT own, infer, reserve, release, finalize, or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection; Payment outcomes MUST NOT prove Inventory effects.

### BPAY-REQ-015 — Shipping and Fulfilment Boundary

BPAY MAY consume bounded BSHP evidence where materially applicable but MUST NOT own Shipment, Carrier, Dispatch, tracking, delivery, fulfilment, reverse-logistics, or Shipping Provider truth, and Shipping state MUST NOT establish Payment truth.

### BPAY-REQ-016 — Other Domain Non-Authority

Product, Product Variant, Category, Search, CMS, Return, Administration, Notifications, Reporting, fraud, tax, cancellation, credit, and other external truth MUST remain with its governing owner; no external representation or workflow may mutate or replace Payment truth.

### BPAY-REQ-017 — Payment Identity and Lifecycle Truth

Each Payment MUST have stable identity, remain associated with its governed commerce obligation, and expose distinguishable current Payment truth without inventing a complete state enumeration or inferring state from client, Checkout, Order, analytics, or Projection state.

### BPAY-REQ-018 — Payment Attempt Identity, Context, and Evidence

Each Payment Attempt MUST have stable identity, correlate unambiguously to the applicable Payment and governed purchase context, carry explicit Money and Currency, preserve applicable terminal, non-terminal, failed, cancelled, abandoned, and uncertain outcomes, and retain protected evidence sufficient for verification, audit, and reconciliation.

### BPAY-REQ-019 — Governed Payment Initiation

BPAY MAY accept initiation only from an explicitly permitted authority with valid purchase, commercial, Money, Currency, Principal, and contextual Authorization evidence where applicable; it MUST reject invalid, ambiguous, stale, mismatched, inaccessible, or client-asserted authority and MUST distinguish accepted processing from Payment, Checkout, Order, Inventory, Shipping, or Customer success.

### BPAY-REQ-020 — Money and Currency Integrity

Every Payment monetary boundary MUST use explicit Currency and precision-safe non-floating-point Money, reject silent Currency mixing and implicit conversion, fail safely for invalid or unsupported values, preserve applicable Version 1 ZAR governance, and select no storage, scale, Minor Unit, rounding, or exchange mechanism.

### BPAY-REQ-021 — Provider-Neutral Integration Boundary

Payment Provider integration MUST remain behind Approved project-owned Contract semantics; BPAY MUST NOT select a provider, gateway, method, rail, SDK, hosted-page vendor, field model, provider lifecycle, or provider-specific Contract, and provider terminology MUST NOT redefine Payment truth.

### BPAY-REQ-022 — Authoritative Provider Evidence

Provider-dependent Payment truth MUST require governed server-side authenticity, integrity, correlation, freshness, and Authorization validation; malformed, stale, replayed, mismatched, unauthorized, unverifiable, client-visible, redirect, polling, or UI evidence MUST NOT mutate trusted state or trigger duplicate effects.

### BPAY-REQ-023 — Payment Redirect Non-Authority

Where a Payment Redirect applies, initiation, navigation, return arrival, query state, abandonment, and incompletion MUST remain distinct from Payment success, and uncertain redirect outcomes MUST support safe verification or reconciliation without assuming every method uses redirects.

### BPAY-REQ-024 — Payment Authorization Integrity

Where applicable, Payment Authorization MUST represent validated Payment Provider evidence and remain distinct from Authentication, access-control Authorization, Capture, Settlement, Checkout completion, and Order confirmation; a request or client claim MUST NOT establish it.

### BPAY-REQ-025 — Capture Integrity

Where applicable, Capture request and confirmed provider outcome MUST remain distinct from each other and from Payment Authorization and Settlement; processing MUST be duplicate-safe, preserve unknown outcomes, and verify or reconcile before repeating an effect-capable action without selecting Capture timing.

### BPAY-REQ-026 — Void Integrity

Where applicable, Void MUST remain a Payment-owned cancellation of an uncaptured Payment Authorization, keep request and provider confirmation distinct, remain provider-neutral and duplicate-safe, and select no eligibility, timing, or cancellation policy.

### BPAY-REQ-027 — Settlement Integrity

Where Settlement information exists, it MUST derive from authoritative evidence and remain distinct from Payment Authorization, Capture, Customer-facing success, Checkout, and Order truth without selecting timing, batching, cadence, or mechanism.

### BPAY-REQ-028 — Payment Transaction Integrity

Each Payment Transaction MUST preserve stable identity, explicit Money and Currency, correct relationships, distinguishable operation and outcome, applicable authoritative provider evidence, and protected audit and reconciliation context without being confused with a Database Transaction or prescribing persistence design.

### BPAY-REQ-029 — Authoritative Payment Success

Payment success MUST require sufficient authoritative governed evidence; client reports, browser or UI state, redirect return, duplicate notification, Pricing success, Checkout completion, Order existence, timeout, or attempted Payment MUST NOT independently prove success or definitive failure.

### BPAY-REQ-030 — Explicit Payment Outcomes

Initiation rejection, invalid context, invalid Money or Currency, provider unavailability or rejection, decline, cancellation or abandonment, timeout, duplication, staleness, unverifiable evidence, uncertainty, reconciliation disagreement, and unauthorized action MUST produce distinguishable safe outcomes without inventing Customer-facing wording.

### BPAY-REQ-031 — Uncertainty Preservation

Unknown, pending, partial, delayed, conflicting, or otherwise uncertain outcomes MUST remain uncertain until authoritative evidence resolves them; client interruption, navigation, cancellation, timeout, or stopped observation MUST NOT prove provider processing stopped or negate a submitted effect.

### BPAY-REQ-032 — Duplicate Financial-Effect and Idempotency Safety

Duplicate, retried, replayed, stale, or concurrent initiation, notification, reconciliation, Capture, Void, and Refund work MUST NOT multiply Payment Authorizations, Captures, Voids, Refund Transactions, Orders, or other financial effects; applicable Contracts MUST preserve Idempotency Key semantics without selecting a mechanism.

### BPAY-REQ-033 — Retry, Replay, and Concurrency Safety

BPAY MUST correlate repeated evidence and requests, produce a consistent governed outcome where safe, reject unauthorized or conflicting replay, verify uncertain prior effects before repetition, and prevent stale or concurrent input from overwriting accepted truth or multiplying side effects.

### BPAY-REQ-034 — Payment Reconciliation

BPAY MUST support governed reconciliation for unknown, delayed, stale, conflicting, or provider-disagreeing outcomes by comparing authorized sources, preserving duplicate safety and history, and producing a resolved or explicitly still-uncertain outcome without selecting cadence, batch, job, queue, table, or workflow.

### BPAY-REQ-035 — Controlled Recovery and Intervention

Permitted recovery and intervention MUST be explicit, contextually authorized, observable, evidence-based, state-governed, duplicate-safe, and proportionally audited; it MUST NOT fabricate provider evidence, rewrite history, bypass another owner, or promote uncertainty to success.

### BPAY-REQ-036 — Refund Execution Integrity

BPAY MAY own Payment-side Refund execution only from governed approved eligibility and Money input supplied by the appropriate authority; request, processing, provider evidence, and confirmed Refund outcome MUST remain distinct, and each Refund Transaction MUST relate to the applicable captured Payment without rewriting the Order Snapshot.

### BPAY-REQ-037 — Return and Refund Policy Separation

BPAY MUST NOT establish Return eligibility, approval, lifecycle, inspection, disposition, exchange, Refund eligibility or amount policy, partial-Refund policy, cancellation policy, restoration, or credit treatment; Return, cancellation, Refund, and Refund Transaction MUST remain distinct and unresolved policy external.

### BPAY-REQ-038 — Chargeback Boundary

Where future provider governance supports Chargeback information, BPAY MUST preserve authoritative protected payment-side Chargeback truth, keep it distinct from Refund and Return, and support reconciliation and audit without selecting dispute windows, evidence workflows, liability policy, provider process, or operational targets.

### BPAY-REQ-039 — Administration and Operational Actions

Administration MAY invoke explicit protected BPAY capabilities but MUST NOT gain Payment authority or bypass Payment rules; materially high-Risk actions MUST use proportional contextual Authorization, confirmation or Human Approval Gate where governed, reason evidence, and Audit Records without defining an admin API, Role/Permission matrix, or SLA.

### BPAY-REQ-040 — Payment Data and Secret Protection

BPAY MUST minimize Payment-data exposure, use only Approved hosted or tokenized handling where applicable, never store raw card data or raw CVV, protect Sensitive Data, Secrets, credentials, tokens, provider identifiers, fraud detail, and evidence across every interface and operational surface, and disclose no unnecessary internal detail.

### BPAY-REQ-041 — Fraud Policy Non-Authority

BPAY MUST NOT define fraud policy, provider, score, threshold, rules, or manual-review workflow; where governed fraud input applies, it MUST consume only authoritative bounded outcomes, preserve missing or uncertain state and permitted explainability, and MUST NOT treat fraud advice as Payment proof.

### BPAY-REQ-042 — Conditional Events and Messaging

Payment Domain or Integration Events MUST exist only for a governed need and preserve producer authority, compatibility, Sensitive Data protection, correlation, duplicate and replay safety, and consumer Projection non-authority; BPAY selects no event name, payload, schema, topic, queue, broker, transport, guarantee, or choreography and MUST NOT introduce messaging independently.

### BPAY-REQ-043 — API, Contract, and Compatibility Boundary

Future BPAY Contracts MUST preserve Payment identity, Money, Currency, provider neutrality, success, failure, uncertainty, authorization, security, duplicate safety, reconciliation, compatibility, validation, bounds, and explicit errors without defining routes, methods, statuses, DTOs, payloads, provider schemas, or other concrete Contract designs.

### BPAY-REQ-044 — Accessible and Non-Authoritative Representations

Applicable customer-facing Payment states, redirects, failures, recovery guidance, and confirmation MUST support WCAG 2.2 AA outcomes, while communications, reports, analytics, exports, and Projections MUST derive from protected authoritative evidence, remain non-authoritative, and never alter Payment truth.

### BPAY-REQ-045 — Observability, Health, and Audit

Material Payment work MUST be bounded, correlated, observable, diagnosable, health-aware, and supported by proportional Audit Records for high-Risk operations without exposing protected data or selecting numerical performance, timeout, retry, retention, SLA, SLO, or recovery targets.

### BPAY-REQ-046 — Commercial Document Policy Non-Authority

BPAY MUST preserve applicable governed Money evidence without establishing Tax treatment, tax display, invoice or Credit Note policy, numbering, legal interpretation, or document technology, and MUST NOT rewrite confirmed commercial history.

### BPAY-REQ-047 — Complete Payment Verification

Verification MUST cover all applicable Payment Domain behavior, boundaries, positive and negative paths, provider evidence, financial effects, uncertainty, concurrency, reconciliation, recovery, security, audit, Contracts, accessibility, compatibility, operational behavior, and every materially applicable BEB obligation without selecting a framework, test identifiers, or numerical coverage target.

### BPAY-REQ-048 — Open-Decision and Implementation Neutrality

BPAY MUST preserve all 9 Product and 10 Architecture Decisions listed in this Specification as unresolved and MUST NOT select any prohibited policy, provider, mechanism, numerical value, infrastructure, or implementation detail.

### BPAY-REQ-049 — Configuration, Migration, and Deployment Safety

BPAY MUST inherit safe configuration, Secret handling, reachable-state, feature-flag, migration, rollback, backward compatibility, bounded-work, and failure-containment obligations without selecting configuration products, flags, schema strategy, deployment topology, or operational numbers.

### BPAY-REQ-050 — Governance, Traceability, and Roadmap Integrity

BPAY MUST maintain one-to-one Requirement and Acceptance Criterion coverage, complete source traceability, Payment Domain coverage, BEB accounting, required governance review, and canonical sequence containment through BPAY while leaving CMS and every post-BPAY position unresolved.

## 3. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BPAY-AC-001 | BPAY-REQ-001 | Metadata and scope review show `1.0.0 Approved`, `authoritative: false`, scope `BPAY`, normativity only within the Payment backend scope, Payment-only bounded authority, and governing-source precedence. |
| BPAY-AC-002 | BPAY-REQ-002 | The Payment coverage matrix accounts for `REQ-PAY-001` through `REQ-PAY-045` with no gap or authority transfer. |
| BPAY-AC-003 | BPAY-REQ-003 | The document establishes no backend identity or order after BPAY and preserves every listed unresolved capability. |
| BPAY-AC-004 | BPAY-REQ-004 | The BEB matrix accounts for `BEB-REQ-001` through `BEB-REQ-056` and applies each material or conditional obligation. |
| BPAY-AC-005 | BPAY-REQ-005 | Architecture review demonstrates modular and hexagonal direction with project-owned Ports and no selected extraction or deployment mechanism. |
| BPAY-AC-006 | BPAY-REQ-006 | Contract review shows intentional Use Cases, command/query separation, bounded validation, safe errors, and no framework or provider ownership leakage. |
| BPAY-AC-007 | BPAY-REQ-007 | Persistence evidence owns only Payment state, preserves history, and exposes no invented schema, mapping, index, or identifier design. |
| BPAY-AC-008 | BPAY-REQ-008 | Transaction tests distinguish local commits and external effects, preserve unknown outcomes, and keep provider waits outside long transactions where applicable. |
| BPAY-AC-009 | BPAY-REQ-009 | Trusted Identity evidence is consumed without transferring Identity authority, and Authentication alone cannot authorize an action. |
| BPAY-AC-010 | BPAY-REQ-010 | Customer references remain governed inputs, later source changes preserve Payment history, and BCUS authority remains external. |
| BPAY-AC-011 | BPAY-REQ-011 | Payment uses governed commercial input unchanged and exposes disagreement without calculating or selecting external commercial policy. |
| BPAY-AC-012 | BPAY-REQ-012 | BCHK can initiate and correlate Payment work while all Cart and Checkout truth remains external and client state proves no Payment outcome. |
| BPAY-AC-013 | BPAY-REQ-013 | Payment evidence coordinates permitted Order work without creating, confirming, mutating, or proving an Order. |
| BPAY-AC-014 | BPAY-REQ-014 | Payment operations neither mutate nor prove Inventory state or effects. |
| BPAY-AC-015 | BPAY-REQ-015 | Shipping evidence remains bounded input and no Payment state establishes Shipping or fulfilment truth. |
| BPAY-AC-016 | BPAY-REQ-016 | Each named external Domain retains its truth and no representation or workflow mutates Payment authority. |
| BPAY-AC-017 | BPAY-REQ-017 | Payment identity and current truth remain stable and distinguishable without inference from non-authoritative state or a complete invented state graph. |
| BPAY-AC-018 | BPAY-REQ-018 | Every attempt has stable identity, correct correlation, Money/Currency, distinguishable outcomes, protected evidence, and reconciliation support. |
| BPAY-AC-019 | BPAY-REQ-019 | Only valid governed initiation is accepted; invalid inputs fail safely and initiation creates no unrelated success. |
| BPAY-AC-020 | BPAY-REQ-020 | Monetary tests preserve explicit precision-safe Money/Currency, ZAR governance where applicable, and reject mixing or unsupported values without invented mechanics. |
| BPAY-AC-021 | BPAY-REQ-021 | Provider integration remains project-owned and provider-neutral with no selected provider, method, SDK, lifecycle, or concrete Contract. |
| BPAY-AC-022 | BPAY-REQ-022 | Only authenticated, integrity-checked, correlated, fresh, authorized provider evidence affects truth; invalid evidence causes no trusted or duplicate effect. |
| BPAY-AC-023 | BPAY-REQ-023 | Redirect and browser states never prove success, and uncertain redirect outcomes remain verifiable or reconcilable. |
| BPAY-AC-024 | BPAY-REQ-024 | Payment Authorization derives from validated provider evidence and remains distinct from every listed Authentication, authorization, payment, Checkout, and Order concept. |
| BPAY-AC-025 | BPAY-REQ-025 | Capture request, provider outcome, Authorization, and Settlement remain distinct; unknown and duplicate paths create no repeated effect. |
| BPAY-AC-026 | BPAY-REQ-026 | Void request and confirmation remain distinct, provider-neutral, duplicate-safe, and policy-neutral. |
| BPAY-AC-027 | BPAY-REQ-027 | Settlement derives only from authoritative evidence and remains distinct without an invented timing, batch, cadence, or mechanism. |
| BPAY-AC-028 | BPAY-REQ-028 | Each transaction preserves identity, Money/Currency, relationships, operation, outcome, provider evidence, and protected audit context without schema design. |
| BPAY-AC-029 | BPAY-REQ-029 | None of the listed client, redirect, duplicate, commercial, Checkout, Order, timeout, or attempt facts independently establishes success or failure. |
| BPAY-AC-030 | BPAY-REQ-030 | Every listed rejection, failure, cancellation, duplicate, stale, unauthorized, and uncertain condition produces a distinguishable safe outcome. |
| BPAY-AC-031 | BPAY-REQ-031 | Unknown, pending, partial, delayed, and conflicting outcomes remain uncertain until authoritative evidence resolves them. |
| BPAY-AC-032 | BPAY-REQ-032 | Duplicate and concurrent scenarios multiply no authorization, capture, void, refund, Order, or other financial effect and preserve applicable idempotency semantics. |
| BPAY-AC-033 | BPAY-REQ-033 | Repeated work correlates correctly, unsafe replay is rejected, unknown effects are verified first, and stale/concurrent input cannot overwrite truth. |
| BPAY-AC-034 | BPAY-REQ-034 | Reconciliation compares authorized evidence, preserves history and duplicate safety, and returns resolved or still-uncertain outcomes without an invented mechanism. |
| BPAY-AC-035 | BPAY-REQ-035 | Recovery is authorized, observable, evidence-based, duplicate-safe, and audited where material without fabricated evidence or rewritten history. |
| BPAY-AC-036 | BPAY-REQ-036 | Refund execution requires governed eligibility and Money, distinguishes every stage, creates correct payment-side truth, and leaves Order history unchanged. |
| BPAY-AC-037 | BPAY-REQ-037 | BPAY defines none of the listed Return, Refund, cancellation, restoration, or credit policies and preserves all conceptual distinctions. |
| BPAY-AC-038 | BPAY-REQ-038 | Chargeback evidence remains protected, authoritative, distinct, reconcilable, and audited without an invented policy, workflow, provider process, or target. |
| BPAY-AC-039 | BPAY-REQ-039 | Protected operational actions preserve Payment authority and proportional controls without an admin API, matrix, or SLA. |
| BPAY-AC-040 | BPAY-REQ-040 | Raw card data and CVV are absent from prohibited surfaces, protected data is minimized, and errors reveal no unnecessary internals. |
| BPAY-AC-041 | BPAY-REQ-041 | Fraud input remains governed and external; no provider, score, threshold, rule, workflow, or Payment proof is invented. |
| BPAY-AC-042 | BPAY-REQ-042 | Any governed event preserves authority, protection, compatibility, correlation, duplicate/replay safety, and non-authority without selecting event or messaging design. |
| BPAY-AC-043 | BPAY-REQ-043 | Contract evidence preserves all listed semantics and compatibility while containing no concrete route, method, status, DTO, payload, or provider schema. |
| BPAY-AC-044 | BPAY-REQ-044 | Applicable customer surfaces meet WCAG 2.2 AA outcomes and every communication or Projection remains protected, sourced, and non-authoritative. |
| BPAY-AC-045 | BPAY-REQ-045 | Operations expose bounded correlated observability and proportional Audit Records without protected leakage or numerical targets. |
| BPAY-AC-046 | BPAY-REQ-046 | Payment evidence preserves governed Money while creating no tax, invoice, Credit Note, numbering, legal, or technology policy. |
| BPAY-AC-047 | BPAY-REQ-047 | Verification evidence covers Payment behavior, boundaries, failures, security, financial integrity, compatibility, operations, and BEB obligations without invented targets. |
| BPAY-AC-048 | BPAY-REQ-048 | All 9 Product and 10 Architecture Decisions remain exact and unresolved, and no prohibited choice appears elsewhere. |
| BPAY-AC-049 | BPAY-REQ-049 | Configuration, migration, deployment, compatibility, and bounded-work evidence is safe without selecting mechanisms or numerical values. |
| BPAY-AC-050 | BPAY-REQ-050 | Counts, mappings, matrices, traceability, reviews, and roadmap containment are complete and internally consistent. |

## 4. Requirement Traceability

| Requirement | Payment Domain source | BEB source | Approved backend / external boundary source | Verification |
| --- | --- | --- | --- | --- |
| BPAY-REQ-001 | REQ-PAY-001 | BEB-REQ-001–004 | ADR-0013; ARCHITECTURE.md §35 | BPAY-AC-001 |
| BPAY-REQ-002 | REQ-PAY-001–045 | BEB-REQ-002–004 | Approved Payment Domain Requirements | BPAY-AC-002 |
| BPAY-REQ-003 | REQ-PAY-001–002 | BEB-REQ-003–004, 056 | ADR-0013 | BPAY-AC-003 |
| BPAY-REQ-004 | REQ-PAY-001, 045 | BEB-REQ-001–056 | BEB §§2–6 | BPAY-AC-004 |
| BPAY-REQ-005 | REQ-PAY-001, 039 | BEB-REQ-005–007 | ARCHITECTURE.md §§5, 35 | BPAY-AC-005 |
| BPAY-REQ-006 | REQ-PAY-006–007, 039 | BEB-REQ-008–018, 041 | API.md; SPRING.md | BPAY-AC-006 |
| BPAY-REQ-007 | REQ-PAY-003–005, 018, 024, 034, 036 | BEB-REQ-021–024, 049–050 | DATABASE.md; POSTGRES.md | BPAY-AC-007 |
| BPAY-REQ-008 | REQ-PAY-011–025, 034 | BEB-REQ-012, 025–028, 032–036 | DATABASE.md; SPRING.md | BPAY-AC-008 |
| BPAY-REQ-009 | REQ-PAY-029–030 | BEB-REQ-011, 019–020, 043 | BIDN; SECURITY-STANDARDS.md | BPAY-AC-009 |
| BPAY-REQ-010 | REQ-PAY-029 | BEB-REQ-011, 019–024, 043 | BCUS | BPAY-AC-010 |
| BPAY-REQ-011 | REQ-PAY-008–009, 044 | BEB-REQ-008, 021, 023–025, 041 | BPRC | BPAY-AC-011 |
| BPAY-REQ-012 | REQ-PAY-006–007, 026 | BEB-REQ-008–011, 021 | BCART; BCHK-REQ-027–029, 038 | BPAY-AC-012 |
| BPAY-REQ-013 | REQ-PAY-027 | BEB-REQ-008, 021, 024 | BORD-REQ-026–029, 045 | BPAY-AC-013 |
| BPAY-REQ-014 | REQ-PAY-028 | BEB-REQ-003–004, 021 | BINV | BPAY-AC-014 |
| BPAY-REQ-015 | REQ-PAY-002, 043 | BEB-REQ-003–004, 021 | BSHP-REQ-024, 045 | BPAY-AC-015 |
| BPAY-REQ-016 | REQ-PAY-002, 033, 035, 037, 043–044 | BEB-REQ-003–004, 021, 056 | Owning Domain Specifications | BPAY-AC-016 |
| BPAY-REQ-017 | REQ-PAY-003 | BEB-REQ-021–024, 027–028 | GLOSSARY.md §9 | BPAY-AC-017 |
| BPAY-REQ-018 | REQ-PAY-004–005 | BEB-REQ-015, 021–024, 028 | BCHK | BPAY-AC-018 |
| BPAY-REQ-019 | REQ-PAY-006–007 | BEB-REQ-009–011, 015, 020 | BCHK-REQ-027–029 | BPAY-AC-019 |
| BPAY-REQ-020 | REQ-PAY-008 | BEB-REQ-015, 021, 023–025, 041 | BPRC; PRODUCT.md §20 | BPAY-AC-020 |
| BPAY-REQ-021 | REQ-PAY-010 | BEB-REQ-006–008, 032–034, 056 | ADR-0013 Product decision 6; Architecture decision 5 | BPAY-AC-021 |
| BPAY-REQ-022 | REQ-PAY-011–012 | BEB-REQ-015, 020, 032–036, 041, 043–044 | SECURITY-STANDARDS.md; API.md | BPAY-AC-022 |
| BPAY-REQ-023 | REQ-PAY-013 | BEB-REQ-020, 033–034, 041–043 | BCHK | BPAY-AC-023 |
| BPAY-REQ-024 | REQ-PAY-014 | BEB-REQ-021, 023–025, 032–035 | DECISIONS.md §35 | BPAY-AC-024 |
| BPAY-REQ-025 | REQ-PAY-015 | BEB-REQ-025–034, 041–042 | Payment Domain §11 | BPAY-AC-025 |
| BPAY-REQ-026 | REQ-PAY-016 | BEB-REQ-025–034, 041–042 | Product decision 10 | BPAY-AC-026 |
| BPAY-REQ-027 | REQ-PAY-017 | BEB-REQ-021, 024, 032–034, 042 | ARCHITECTURE.md §20.2 | BPAY-AC-027 |
| BPAY-REQ-028 | REQ-PAY-018 | BEB-REQ-021–025, 043–046 | DATABASE.md | BPAY-AC-028 |
| BPAY-REQ-029 | REQ-PAY-019 | BEB-REQ-015, 021, 032–036, 041–043 | BCHK; BORD | BPAY-AC-029 |
| BPAY-REQ-030 | REQ-PAY-020 | BEB-REQ-017, 033, 041–043, 051 | API.md; SECURITY-STANDARDS.md | BPAY-AC-030 |
| BPAY-REQ-031 | REQ-PAY-021 | BEB-REQ-028, 033–034, 041–042 | BCHK-REQ-029, 033 | BPAY-AC-031 |
| BPAY-REQ-032 | REQ-PAY-022 | BEB-REQ-027–031, 035–036, 039 | BCHK; BORD | BPAY-AC-032 |
| BPAY-REQ-033 | REQ-PAY-023 | BEB-REQ-027–031, 033–036, 041–042 | API.md; DATABASE.md | BPAY-AC-033 |
| BPAY-REQ-034 | REQ-PAY-024 | BEB-REQ-028, 031, 034, 039, 042 | BORD-REQ-029, 041 | BPAY-AC-034 |
| BPAY-REQ-035 | REQ-PAY-025 | BEB-REQ-011, 028–031, 042, 045–046 | Administration; SECURITY-STANDARDS.md | BPAY-AC-035 |
| BPAY-REQ-036 | REQ-PAY-034 | BEB-REQ-021–031, 032–036, 041–046 | Return Domain REQ-RET-027–029 | BPAY-AC-036 |
| BPAY-REQ-037 | REQ-PAY-035 | BEB-REQ-003–004, 021, 056 | Return Domain; decisions 10–11, 29 | BPAY-AC-037 |
| BPAY-REQ-038 | REQ-PAY-036 | BEB-REQ-021–024, 032–036, 042–046 | Payment Domain §26 | BPAY-AC-038 |
| BPAY-REQ-039 | REQ-PAY-030, 037, 042 | BEB-REQ-009, 011, 020, 045 | Administration REQ-ADM-010–011, 029 | BPAY-AC-039 |
| BPAY-REQ-040 | REQ-PAY-031–032 | BEB-REQ-014–015, 017, 020, 043–044 | SECURITY-STANDARDS.md | BPAY-AC-040 |
| BPAY-REQ-041 | REQ-PAY-033 | BEB-REQ-003–004, 011, 043, 056 | Product decision 26 | BPAY-AC-041 |
| BPAY-REQ-042 | REQ-PAY-038 | BEB-REQ-037–040, 043 | EVENTS.md; Notifications Domain | BPAY-AC-042 |
| BPAY-REQ-043 | REQ-PAY-039 | BEB-REQ-013–018, 032, 041, 049–050 | API.md; DOCUMENTATION-STANDARDS.md | BPAY-AC-043 |
| BPAY-REQ-044 | REQ-PAY-040, 043 | BEB-REQ-003, 021, 043, 051 | Notifications; Reporting | BPAY-AC-044 |
| BPAY-REQ-045 | REQ-PAY-041–042 | BEB-REQ-045–047, 051, 054 | SECURITY-STANDARDS.md; TESTING-STANDARDS.md | BPAY-AC-045 |
| BPAY-REQ-046 | REQ-PAY-044 | BEB-REQ-003–004, 021, 024, 056 | BPRC; Product decisions 9, 25 | BPAY-AC-046 |
| BPAY-REQ-047 | REQ-PAY-045 | BEB-REQ-052–055 | TESTING-STANDARDS.md | BPAY-AC-047 |
| BPAY-REQ-048 | REQ-PAY-001–045 | BEB-REQ-040, 047–048, 056 | PRODUCT.md §24; ARCHITECTURE.md §34; ADR-0013 | BPAY-AC-048 |
| BPAY-REQ-049 | REQ-PAY-024–025, 039, 041 | BEB-REQ-047–051 | DATABASE.md; POSTGRES.md | BPAY-AC-049 |
| BPAY-REQ-050 | REQ-PAY-001–045 | BEB-REQ-001–056 | ADR-0013; DOCUMENTATION-STANDARDS.md | BPAY-AC-050 |

## 5. Payment Domain Coverage Matrix

| Payment Domain Requirement(s) | BPAY specialization / classification |
| --- | --- |
| REQ-PAY-001–002 | BPAY-REQ-001–003, 016, 048, 050 — lifecycle, Payment-only authority, non-authority, and governance. |
| REQ-PAY-003–005 | BPAY-REQ-007, 017–018, 028–031 — Payment and Payment Attempt identity, state, evidence, and history. |
| REQ-PAY-006–007 | BPAY-REQ-006, 012, 019 — governed initiation and outcome separation. |
| REQ-PAY-008–009 | BPAY-REQ-011, 020, 046 — Money, Currency, Pricing input, and commercial non-authority. |
| REQ-PAY-010–012 | BPAY-REQ-008, 021–022, 040 — provider neutrality and authoritative evidence safety. |
| REQ-PAY-013–019 | BPAY-REQ-023–029 — Redirect, Authorization, Capture, Void, Settlement, Transaction, and success integrity. |
| REQ-PAY-020–025 | BPAY-REQ-030–035, 045 — failure, uncertainty, duplicate safety, retry, reconciliation, and recovery. |
| REQ-PAY-026–030 | BPAY-REQ-009–014, 019, 039 — Checkout, Order, Inventory, Customer, Identity, and Authorization boundaries. |
| REQ-PAY-031–032 | BPAY-REQ-040, 045 — Payment data, Secrets, evidence, and operational protection. |
| REQ-PAY-033 | BPAY-REQ-041, 048 — fraud non-authority and unresolved policy. |
| REQ-PAY-034–035 | BPAY-REQ-036–037, 046, 048 — Refund execution and Return/Refund policy separation. |
| REQ-PAY-036–037 | BPAY-REQ-035, 038–039, 045 — Chargeback and protected operational actions. |
| REQ-PAY-038–039 | BPAY-REQ-006, 042–043, 049 — conditional events, Contracts, compatibility, and implementation neutrality. |
| REQ-PAY-040–044 | BPAY-REQ-011, 039–046 — accessibility, observability, audit, representations, and commercial-document non-authority. |
| REQ-PAY-045 | BPAY-REQ-047, 050 — complete verification and traceability. |

Every `REQ-PAY-001` through `REQ-PAY-045` is specialized; none is omitted or classified in a way that transfers external authority.

## 6. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BPAY application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, Domain non-authority, specialization, and roadmap containment. BPAY-REQ-001–004, 048, 050. |
| BEB-REQ-005–007 | Applicable | Modular-monolith, hexagonal dependency, and layer responsibilities. BPAY-REQ-005–006. |
| BEB-REQ-008–010 | Applicable | Public Contracts, Use Cases, and command/query semantics. BPAY-REQ-006, 009–043. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and application transaction ownership. BPAY-REQ-008–010, 019, 035, 039–040. |
| BEB-REQ-013–018 | Applicable where an external API or collection Contract exists | Versioning, DTO separation, validation, bounded queries, Problem Details, description, and evolution remain inherited without selecting routes or DTOs. BPAY-REQ-006, 043. |
| BEB-REQ-019–020 | Applicable | BIDN supplies Authentication truth; BPAY consumes trusted evidence and enforces contextual Authorization and concealment. BPAY-REQ-009–010, 019, 039–040. |
| BEB-REQ-021–024 | Applicable | Payment data ownership, persistence integrity, and historical truth. BPAY-REQ-007, 011–18, 24–29, 34–40, 44–46. |
| BEB-REQ-025–031 | Applicable | Atomicity, external-effect separation, concurrency, durable uncertainty, idempotency, replay, and duplicate safety. BPAY-REQ-008, 025–36. |
| BEB-REQ-032–034 | Applicable | Provider Ports, resilience, uncertainty, and reconciliation are central while provider choices remain unresolved. BPAY-REQ-008, 021–35. |
| BEB-REQ-035–036 | Applicable | Governed provider Callback or Webhook evidence inherits authenticity, integrity, replay, and recovery safeguards without selecting a protocol. BPAY-REQ-022–23, 29–35, 40. |
| BEB-REQ-037–039 | Conditionally applicable | Any governed Domain or Integration Event inherits separation, envelope, delivery, replay, and consumption safety; no event is selected. BPAY-REQ-032–35, 042–043. |
| BEB-REQ-040 | Applicable | External messaging cannot be introduced independently. BPAY-REQ-042, 048. |
| BEB-REQ-041–042 | Applicable | Failure classification, recovery, and reconciliation. BPAY-REQ-011, 019, 022–38, 43. |
| BEB-REQ-043–044 | Applicable | Backend security, data protection, Secrets, and prohibited Payment-data safety. BPAY-REQ-009–10, 019, 022–23, 35, 39–45. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records, observability, correlation, and health. BPAY-REQ-018, 028, 034–35, 38–39, 45. |
| BEB-REQ-047–048 | Applicable | Safe configuration and reachable feature states without selecting mechanisms. BPAY-REQ-021, 042–43, 48–49. |
| BEB-REQ-049–051 | Applicable | Migration, deployment compatibility, bounded work, and failure containment. BPAY-REQ-007–008, 030–35, 43–45, 49. |
| BEB-REQ-052–055 | Applicable | Domain, application, Adapter, integration, Contract, security, architecture, operational, and traceable verification. BPAY-REQ-047, 050. |
| BEB-REQ-056 | Applicable | Policy, provider, implementation, numerical, and roadmap neutrality. BPAY-REQ-003, 016, 020–21, 23–27, 30–50. |

Every `BEB-REQ-001` through `BEB-REQ-056` is accounted for; conditional classification does not authorize omission when its governed condition exists.

## 7. Dependency and Authority Matrix

| Boundary | BPAY may consume or expose | Authority that remains external |
| --- | --- | --- |
| BIDN / BCUS | Trusted Principal, Authentication, Customer, Account, ownership, and purchase references | Identity, credentials, Sessions, Customer, Account, Consent, Preference, and source history |
| BPRD / BCAT / BSRCH | Necessary catalogue references and non-authoritative Search context | Product, Product Variant, Category, taxonomy, Search index, ranking, and result truth |
| BINV | Governed reservation or Inventory evidence for coordination | Stock, availability, reservations, movements, adjustments, and overselling controls |
| BPRC | Governed Money, Currency, totals, and commercial evidence | Pricing calculation, Discount, Promotion, Voucher, Tax, Shipping charge, conversion, and policy |
| BCART / BCHK | Purchase intent, initiation, orchestration, and correlation context | Cart and Checkout identity, validation, lifecycle, success, and client state |
| BORD | Order identity, historical commercial context, Payment reference, and reconciliation context | Order creation, confirmation, lifecycle, history, cancellation, and snapshots |
| BSHP | Bounded Shipping evidence where material | Shipment, fulfilment, Carrier, Dispatch, tracking, delivery, and reverse logistics |
| Return | Governed approved Refund eligibility and Money input when available | Return eligibility, approval, lifecycle, inspection, disposition, exchange, and Refund policy |
| Administration | Protected invocation of explicit Payment capabilities | Administrative workflow authority, Roles, Permissions, and escalation policy |
| Notifications / Reporting | Bounded authoritative Payment and Refund facts | Communication delivery, templates, attempts, analytics, reports, exports, and Projections |
| CMS and other Domains | Only explicitly governed bounded references | All CMS and other owning-Domain truth |

## 8. Contract, Data, Transaction, Provider, Refund, and Event Boundaries

BPAY owns Payment behavior and Payment-owned transactional evidence, not transport, persistence, provider, or infrastructure mechanisms. Future concrete Contracts must preserve the Requirements above and be governed separately. Provider interaction is isolated behind project-owned Ports; external effects, local transactions, unknown outcomes, reconciliation, and recovery remain explicit. Payment-side Refund execution begins only from governed approved eligibility and Money input and never defines Return or Refund policy. Events and messaging remain conditional and mechanism-neutral.

## 9. Open Product Decisions

The following **9 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and Accepted ADR-0013 remain unresolved:

| Item | Open Product Decision | Preserved BPAY boundary |
| ---: | --- | --- |
| 6 | Initial payment methods and provider. | No method, provider, provider Contract, or integration is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, calculation, invoice, or document policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation, cutoff, timing, Void, or reversal policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, exchange, Refund eligibility, amount, window, or method policy is selected. |
| 23 | Administrative role and permission matrix. | No Role, Permission, mapping, or matrix is defined. |
| 24 | Production customer-service and operational escalation process. | No channel, escalation owner, workflow, expectation, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, accounting, or document policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No provider, score, threshold, rule, screening, or intervention policy is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, or treatment is selected. |

## 10. Open Architecture Decisions

The following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and Accepted ADR-0013 remain unresolved:

| Item | Open Architecture Decision | Preserved BPAY boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment Provider, Contract, adapter, or integration is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Payment persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

## 11. Explicit Non-Decisions and Roadmap Boundary

BPAY selects no provider, method, provider Contract, API, HTTP detail, DTO, payload, schema, table, column, index, ORM mapping, event, topic, queue, broker, cache, Redis use, infrastructure product, topology, identifier format, retry count, timeout, TTL, retention period, rate limit, performance target, SLA, SLO, recovery objective, fraud mechanism, tax rule, cancellation rule, Return/Refund policy, credit policy, Role/Permission matrix, or other unresolved mechanism or policy.

CMS remains independently eligible, separate, unresolved, unranked, and unordered. Return, Administration, Notifications, and Reporting remain separate and unresolved. BPAY does not assign any post-BPAY title, path, scope, decomposition, or order and does not pre-authorize another backend Draft.

## 12. Risks and Controls

| Risk | Implementation-neutral control direction |
| --- | --- |
| False Payment success | Require sufficient validated authoritative evidence and preserve uncertainty. |
| Duplicate financial effect | Correlate work, preserve applicable idempotency semantics, and verify uncertain effects before repetition. |
| Provider evidence spoofing or replay | Validate authenticity, integrity, correlation, freshness, Authorization, and replay state. |
| Amount or Currency mismatch | Require explicit precision-safe Money/Currency and expose disagreement safely. |
| Unknown provider outcome | Preserve uncertainty and reconcile authorized evidence without blind repetition. |
| Sensitive Data leakage | Minimize collection and exclude protected data from every non-permitted surface. |
| Authority leakage | Require owning-Domain confirmation and keep Payment evidence distinct from external truth. |
| Unauthorized Refund | Accept execution only from governed eligibility and amount input and preserve stage distinctions. |
| Recovery corrupts history | Require explicit authorized evidence-based duplicate-safe intervention and proportional Audit Records. |
| Projection becomes authoritative | Preserve source lineage and prevent consumer failure or mutation from changing Payment truth. |

## 13. Required Governance Reviews

Approval governance recorded completion of:

- Architecture and Payment ownership review of Payment-only scope, BEB inheritance, ADR-0013 conformance, and roadmap containment;
- affected Checkout, Order, Return, Pricing, Inventory, Shipping and Fulfilment, Administration, Notifications, Reporting, Identity, and Customer ownership review of their bounded Contracts and preserved authority;
- Security review of provider evidence, contextual Authorization, Sensitive Data, Secrets, replay, duplicate financial effects, uncertainty, recovery, and Audit Records;
- Testing review of one-to-one coverage, Domain/BEB matrices, provider-boundary verification, failure paths, financial integrity, security, compatibility, and operations;
- Documentation review of lifecycle, terminology, references, traceability, decision inventories, and implementation neutrality;
- confirmation that all 45 Payment Domain Requirements and 56 BEB Requirements are accounted for;
- confirmation that all 9 Product and 10 Architecture Decisions remain unresolved; and
- confirmation that no post-BPAY backend identity or position is established.

The Architecture, affected ownership, Security, Testing, and Documentation reviews represented above were completed for approval. No reviewer identity, signature, ticket, or external approval artifact is asserted.

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
- `specifications/adr/ADR-0013-post-shipping-backend-specification.md`
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
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/cms/cms-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-23 | Approved | Approved the Payment-only BPAY backend Specification following successful approval-readiness validation, preserving Payment Domain and BEB coverage, cross-domain authority boundaries, unresolved Product and Architecture decisions, implementation neutrality, and post-BPAY roadmap containment. |
| 0.1.0 | 2026-09-21 | Draft | Established the initial Payment-only BPAY backend Specification authorized by ADR-0013, specializing the Approved Payment Domain and applicable Backend Engineering Baseline while preserving cross-domain authority and unresolved Product and Architecture decisions. |

## 16. Final Validation

Final validation confirms that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, owner is `Backend / Payment`, scope is `BPAY`, and Requirements are normative only within the Payment backend scope;
2. the decomposition is Payment-only and all cross-Domain authority boundaries remain intact;
3. all 50 BPAY Requirements are unique, contiguous, normative, and within scope;
4. all 50 Acceptance Criteria map one-to-one to Requirements with no orphan or duplicate identifier;
5. all 50 traceability rows map each Requirement to its Acceptance Criterion and governed sources;
6. all 45 Payment Domain Requirements and all 56 BEB Requirements are explicitly accounted for;
7. all 9 Product and 10 Architecture Decisions remain exact and unresolved;
8. the Refund boundary preserves Payment-side execution while Return and policy authority remain external;
9. provider evidence, Sensitive Data, contextual Authorization, duplicate financial effects, uncertainty, reconciliation, recovery, observability, Audit Records, and verification obligations are complete;
10. no provider, policy, API, DTO, persistence, event, cache, infrastructure, numerical target, Role/Permission matrix, or implementation mechanism is selected;
11. required governance reviews, Related Documents, Revision History, and roadmap containment are complete and consistent; and
12. the BPAY lifecycle change affects only `specifications/backend/payment/payment-backend.md` and introduces no unrelated repository changes.
