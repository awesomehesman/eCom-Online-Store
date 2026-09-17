---
title: Pricing Backend Specification
version: 0.1.0
status: Draft
owner: Pricing
last_updated: 2026-09-17
authoritative: false
scope: BPRC
---

# Pricing Backend Specification

## 1. Purpose

This Specification defines implementation-neutral backend Requirements for the Pricing capability authorized by Accepted ADR-0006. BPRC specializes only the Approved Pricing Domain and governs backend handling of current authoritative Pricing outcomes, Money and Currency integrity, Price, Discount, Promotion and Voucher evaluation, governed commercial inputs, revalidation, persistence boundaries, consistency, failure, recovery, reconciliation, security, observability, Contracts, conditional events, and verification.

While Draft, this Specification is non-normative. If Approved, its Requirements will be normative only within the governed Pricing backend scope. It remains `authoritative: false`, subordinate to higher-authority governing sources, and does not claim repository-wide authority or resolve any Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BPRC-REQ-001 — Lifecycle, Scope, and Authority

BPRC MUST retain scope `BPRC`, `authoritative: false`, and `0.1.0 Draft` lifecycle metadata, remain non-normative while Draft, and remain subordinate to governing sources. If Approved, it MUST be normative only within the Pricing backend scope.

### BPRC-REQ-002 — Pricing Domain Specialization

BPRC MUST specialize the Approved Pricing Domain without redefining or transferring its authority and MUST account for every materially governed Pricing Requirement.

### BPRC-REQ-003 — Pricing-Only Decomposition

BPRC MUST own no truth outside the Approved Pricing Domain and MUST preserve Product, Product Variant, Category, Inventory, Search and Discovery, Customer, Identity, Cart, Checkout, Order, Payment, Shipping and Fulfilment, Return and Refund, Administration, Reporting and Analytics, CMS, Notifications, and every other Domain authority.

### BPRC-REQ-004 — BEB Inheritance

BPRC MUST inherit every materially applicable BEB Requirement, explicitly account for BEB-REQ-001 through BEB-REQ-056, and activate conditionally applicable obligations if later Approved governance introduces their capability or condition.

### BPRC-REQ-005 — BIDN Consumption Boundary

BPRC MAY consume materially applicable trusted BIDN Contracts or Identity evidence, but MUST NOT own Authentication, Principal establishment, credentials, Sessions, revocation, recovery verification, Roles, Permissions, Claims, Scope, or Identity truth.

### BPRC-REQ-006 — BCUS Consumption Boundary

BPRC MAY consume governed Customer or Account context only where Approved Pricing policy permits and MUST NOT define Customer identity, Account, profile, Address, Preference, Consent, segment, entitlement, loyalty, personalization, or eligibility policy.

### BPRC-REQ-007 — BPRD Consumption Boundary

BPRC MUST consume governed Product and Product Variant evidence needed for correct Pricing association and revalidation without redefining Product identity, lifecycle, publication, visibility, descriptive truth, Product-owned Attributes, Product Media, or structural sellability.

### BPRC-REQ-008 — BINV Consumption Boundary

BPRC MAY consume governed Inventory evidence only where Approved Pricing policy expressly permits and MUST NOT own, infer, create, reserve, or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, scarcity Pricing, or availability-based Pricing.

## 3. Backend Architecture and Persistence Boundary

### BPRC-REQ-009 — Contextual Pricing Authorization

Each protected Pricing action or Customer-specific outcome MUST be authorized at a trusted server-side boundary for the current Principal, Pricing Resource, action, association, and state, with default denial where required; Authentication evidence, identifiers, Role labels, Permissions, Claims, Scope, Customer context, Product context, Inventory context, UI state, or prior responses alone MUST NOT grant authority.

### BPRC-REQ-010 — Modular and Hexagonal Boundary

BPRC MUST preserve the modular-monolith and hexagonal dependency direction, keep Pricing rules independent of delivery, persistence, provider, cache, and messaging mechanisms, and expose dependencies only through Pricing-owned or explicitly governed Ports and Contracts.

### BPRC-REQ-011 — Pricing Public Module Contract

BPRC MUST expose only intentional abstract Module Contracts that preserve Pricing authority, Money and Currency, association, governed input context, explainability, compatibility, Authorization, stale-state safety, uncertainty, and distinct failure semantics without defining routes, methods, statuses, DTOs, payloads, or schemas.

### BPRC-REQ-012 — Pricing Use Case Orchestration

Pricing commands and queries MUST be orchestrated through explicit Pricing Use Cases that validate governed context, invoke Pricing rules, distinguish reads from effects, and return truthful outcomes without placing business policy in Adapters.

### BPRC-REQ-013 — Pricing Persistence Ownership

Authoritative Pricing state, where governance requires persistence, MUST be accessed only through Pricing-owned abstract Ports or Repositories and mappings that preserve Pricing invariants, isolation, current truth, and required historical evidence without selecting a schema, table, column, SQL, ORM mapping, index, identifier format, cache, Price List representation, or database topology.

### BPRC-REQ-014 — Transaction and Consistency Integrity

Each governed Pricing mutation MUST define a Pricing-owned consistency boundary that preserves all affected invariants and accepted effects atomically where required, keeps external calls outside unsafe transactions, and handles concurrency and uncertain outcomes without selecting locking, isolation, transaction, idempotency-key, or retry mechanisms. Pricing evaluation MUST remain read-only unless Approved governance permits a state-changing commercial operation.

## 4. Pricing Authority and Governed Inputs

### BPRC-REQ-015 — Current Authoritative Commercial Calculation

BPRC MUST own only the current authoritative server-side commercial calculation outcome permitted by Approved policy, including applicable Price, Discount, Promotion, Voucher, calculated line values, totals, savings, and governed commercial eligibility. Tax and delivery-related values MUST be included only when qualified policy and authoritative inputs permit, and current Pricing truth MUST remain distinguishable from historical Order truth.

### BPRC-REQ-016 — Cross-Domain Authority Separation

BPRC MUST NOT create, redefine, or establish success for Product, Product Variant, Category, Inventory, Customer, Identity, Cart, Checkout, Order, Payment, Shipping and Fulfilment, Return and Refund, fraud, Administration, Search and Discovery, Reporting, or another Domain's truth, lifecycle, policy, or outcome.

### BPRC-REQ-017 — Governed Product Association and Input Safety

Every applicable Pricing outcome MUST use the correct governed Product or Product Variant at the granularity required by Approved policy. Unknown, removed, unpublished, stale, materially changed, or structurally unsellable Product evidence MUST NOT silently produce trusted Pricing and MUST yield a distinguishable safe outcome and governed revalidation or recovery without treating Inventory availability as structural sellability.

### BPRC-REQ-018 — Money and Currency Integrity

Every Pricing monetary input, intermediate value exposed across a boundary, comparison, and result MUST preserve Money semantics with explicit Currency and precision-safe, non-floating-point calculation. BPRC MUST reject silent Currency mixing and implicit conversion; Version 1 Customer-visible outcomes MUST use `ZAR`; unsupported Currency MUST fail safely; and no exchange rate, conversion provider, storage type, rounding algorithm, decimal scale, or Minor Unit rule is selected.

## 5. Price, Discount, Promotion, and Voucher Behavior

### BPRC-REQ-019 — Authoritative Price and Change Safety

BPRC MUST determine the applicable authoritative Price for the correct governed Product or Product Variant under current Approved Pricing Rules and inputs, distinguish applicable Base Price, Sale Price, and governed components, and reject client, Cart, analytics, cache, or Projection values as authority. Missing, invalid, stale, or changed Price MUST produce an explicit explainable outcome and governed recalculation before commercial commitment without rewriting confirmed Order history or selecting a Price List or effective-date mechanism.

### BPRC-REQ-020 — Discount Calculation Boundary

An applicable Discount MUST be evaluated only under Approved Discount Rules, recorded and explained separately from Base Price, Sale Price, Promotion, Voucher, Tax, and shipping charges, and be reproducible under equivalent governed inputs. Invalid, stale, changed, or inapplicable Discount state MUST remain distinguishable and safely revalidated without inventing a threshold, percentage, eligibility value, precedence, or formula.

### BPRC-REQ-021 — Promotion and Voucher Evaluation

BPRC MUST apply a Promotion only when all governed validity, Promotion Eligibility, Promotion Benefit, exclusions, usage constraints, and other required Approved inputs establish applicability. A Voucher MUST be accepted only as governed input to eligible Promotion evaluation; unusable Promotion or Voucher states MUST produce explicit, safe, explainable outcomes; usage state MUST come from its owning authority; and BPRC MUST NOT invent format, duration, redemption, ownership, campaign, or transferability policy.

### BPRC-REQ-022 — Promotion Combination Deferral

BPRC MUST NOT establish Promotion or Voucher stacking, precedence, combinability, maximum benefit, eligibility values, usage limits, exclusions, calculation order, or Refund treatment until Approved policy governs each applicable value. Conflicting rules without a governed resolution MUST fail safely rather than select a local result.

## 6. Commercial and Cross-Domain Boundaries

### BPRC-REQ-023 — Governed Tax Boundary

BPRC MAY calculate or present Tax-related commercial outcomes only from qualified Approved tax policy and governed inputs. It MUST NOT decide Tax Inclusive or Tax Exclusive policy, Invoice or Credit Note requirements, Tax Jurisdiction implementation, Tax rate, Tax Category mapping, formula, provider, or confirmed Order tax history.

### BPRC-REQ-024 — Shipping Commercial Boundary

BPRC MAY include a governed Shipping Rate, delivery charge, or Promotion Benefit affecting delivery only when Approved policy establishes the handoff from Shipping authority. It MUST NOT establish Shipment, Carrier, Delivery Method, delivery area, fulfilment, operational eligibility, fee policy, free-delivery threshold, or promotional delivery treatment; unavailable or stale required shipping-commercial input MUST prevent a trusted affected total.

### BPRC-REQ-025 — Governed Customer Eligibility Input

BPRC MAY consume governed Customer, Principal, Account, segment, entitlement, or other eligibility evidence only where Approved policy permits, MUST identify the governed eligibility context used, and MUST NOT define identity, Account, Authentication, Authorization policy, segmentation, loyalty, personalization, eligibility classes, or protected attributes or trust a supplied identifier as authority.

### BPRC-REQ-026 — Inventory Non-Authority

Inventory availability MUST NOT silently change a Pricing Rule or commercial outcome unless Approved Product policy expressly governs that dependency. BPRC MUST preserve BINV as authoritative for Inventory and MUST NOT infer scarcity Pricing, availability-based Pricing, Stock, reservation, Available-to-Sell, or purchase eligibility.

### BPRC-REQ-027 — Cart Presentation and Intent Boundary

BPRC MAY provide governed outcomes for Cart presentation, but Cart intent, Cart Item membership, and intended quantity remain Cart-owned. Retained commercial values MUST remain non-authoritative presentations or Projections, MUST NOT be represented as locked or final, and MUST be recalculated through governed Pricing before Checkout commitment.

### BPRC-REQ-028 — Checkout Revalidation Boundary

BPRC MUST support authoritative revalidation of all applicable commercial inputs and outcomes before Checkout commercial commitment and return explicit failure or uncertainty rather than false success. BPRC MUST NOT absorb Checkout orchestration, create an Order, initiate Payment, reserve Inventory, or establish Shipping truth.

### BPRC-REQ-029 — Current and Historical Order Truth

BPRC MAY provide governed commercial values for a later Order Snapshot but MUST NOT create an Order, govern its lifecycle, or own immutable Order history. Current Pricing truth and confirmed historical commercial truth MUST remain distinguishable, and later Pricing or source changes MUST NOT silently rewrite the Order Snapshot.

### BPRC-REQ-030 — Payment Non-Authority

BPRC MAY provide a governed total as Payment input but MUST NOT establish Payment, Payment Authorization, success, Capture, Settlement, Refund completion, or provider truth. Pricing success MUST NOT imply Payment success, and Payment outcomes MUST NOT redefine current Pricing truth.

### BPRC-REQ-031 — Post-Purchase, Credit, Fraud, and Abuse Boundaries

BPRC MUST NOT invent Return, exchange, Refund, promotional reversal, Voucher restoration, gift-card, store-credit, promotional-credit, fraud-screening, score, threshold, provider, blocking, or manual-review policy. A future governed post-purchase calculation or fraud/abuse eligibility input MUST use only owning-authority evidence, preserve the original Order Snapshot, Authorization, auditability, and explainability, and fail safely when required evidence is missing, stale, unavailable, or uncertain.

## 7. Integrity, Security, Contracts, and Operations

### BPRC-REQ-032 — Explainable, Reproducible, and Current Pricing

Each Pricing result MUST identify the governed input and rule context needed to explain and reproduce it under equivalent conditions, including applicable Product Variant, eligibility, Price, Discount, Promotion, Voucher, Tax, shipping-commercial input, and Currency. Equivalent governed inputs and rule state MUST yield the same outcome; stale, changed, or uncertain material input MUST remain distinct from current truth and be recalculated and explained before commitment without exposing protected or abuse-sensitive detail.

### BPRC-REQ-033 — Duplicate, Replay, Concurrency, and Effect Safety

Concurrent, retried, replayed, duplicate, stale, or ambiguous Pricing requests MUST produce a consistent explainable outcome for the same governed context and MUST NOT create duplicate or conflicting commercial effects. Conflicts and uncertain effects MUST remain explicit and be verified or reconciled before repetition.

### BPRC-REQ-034 — Pricing Security and Data Protection

BPRC MUST enforce least privilege, Resource isolation, input and output protection, data minimization, safe failure disclosure, and protection of Secrets, credentials, Customer PII, Sensitive Data, abuse-sensitive Promotion or Voucher information, audit evidence, provider details, and operational internals across Contracts, logs, events, and Projections; ordinary Price data MUST NOT automatically be classified as Sensitive Data.

### BPRC-REQ-035 — Recovery and Reconciliation

Stale commercial state, conflicting governed input, uncertain outcome, unavailable dependency, changed Product or eligibility input, invalid Promotion or Voucher input, and downstream disagreement MUST support distinguishable safe recovery, reconciliation, correction, or escalation where permitted. Recovery MUST be appropriately authorized, observable, duplicate-safe, and MUST NOT fabricate authority or policy or overwrite Order history.

### BPRC-REQ-036 — Accessibility, Boundedness, and Failure Containment

Customer-facing Pricing outcomes MUST support applicable WCAG 2.2 AA behavior, including keyboard and assistive-technology use where relevant. Pricing work and dependencies MUST be bounded, observable, and failure-contained so degradation, dependency failure, or timeout cannot promote stale, partial, or uncertain Pricing into trusted truth, without inventing numerical limits, targets, retries, timeouts, SLAs, or SLOs.

### BPRC-REQ-037 — Proportional Pricing Audit Evidence

Material high-Risk administrative, commercial-rule mutation, reconciliation, security-sensitive, or policy-affecting Pricing action MUST produce proportional, separate, tamper-resistant Audit Records with applicable actor or system, action, time, Resource, reason, and outcome evidence. Ordinary Customer Price reads MUST NOT automatically require high-Risk audit treatment, and no retention or storage mechanism is selected.

### BPRC-REQ-038 — Conditional Events and Integrations

Pricing events or integrations MUST exist only where Architecture, an Approved Contract, downstream reliability, or synchronized governance establishes the need. When applicable, they MUST preserve Pricing authority, compatibility, data protection, idempotency, replay and duplicate safety, uncertainty, failure, reconciliation, observability, and non-authoritative consumer Projections without selecting names, schemas, payloads, topics, brokers, queues, providers, delivery guarantees, or choreography.

### BPRC-REQ-039 — Abstract Pricing Contracts

Every BPRC Contract MUST preserve authoritative Pricing ownership, correct Product and Product Variant association, Money and Currency, governed input context, stale-state safety, explainability, Authorization, privacy, compatibility, concurrency, duplicate safety, uncertainty, and distinguishable failure semantics without defining concrete interface, route, method, status, DTO, payload, schema, persistence, event, or provider design.

### BPRC-REQ-040 — Explicit Pricing Failure Outcomes

Unknown, invalid, removed, unpublished, or structurally unsellable Product evidence; missing, invalid, stale, or changed Price; unsupported Currency; invalid or stale Discount; invalid, inactive, conflicting, or stale Promotion; invalid, ineligible, or stale Voucher; missing or stale governed eligibility or shipping-commercial input; unauthorized protected request; dependency failure; conflict; and uncertain result MUST each produce a safe, distinguishable, explainable, and recoverable outcome where permitted. Uncertainty MUST NOT be represented as success or expose protected internals.

### BPRC-REQ-041 — Complete Pricing Verification

Verification MUST cover applicable positive, negative, boundary, Authorization, isolation, Product association, Money, Currency, Price, Discount, Promotion, Voucher, Tax and Shipping boundaries, eligibility, Cart presentation, Checkout revalidation, Order history, Payment and Inventory non-authority, stale state, duplicate, replay, concurrency, conflict, failure, recovery, reconciliation, audit, security, persistence, Contract, conditional event, observability, accessibility, compatibility, and cross-Domain behavior with traceability to every BPRC Requirement.

### BPRC-REQ-042 — Observability and Operational Diagnosis

BPRC MUST provide safe mechanism-neutral Logs, Metrics, Traces where applicable, correlation, commercial-input and outcome context, failure visibility, reconciliation visibility, and diagnostic evidence sufficient to distinguish and investigate critical Pricing workflows without exposing protected data, abuse-sensitive rules, or numerical operational targets.

### BPRC-REQ-043 — Compatibility, Configuration, and Migration Safety

BPRC Contracts, persistence, configuration, reachable feature-flag states, deployments, and workloads MUST preserve backward-compatible evolution, governed Pricing behavior, Money and Currency integrity, data ownership, safe migration or forward-fix, bounded work, and failure containment without selecting schema strategy, flag mechanism, deployment topology, retention, recovery objectives, or numerical targets.

### BPRC-REQ-044 — Policy, Implementation, and Roadmap Neutrality

BPRC MUST preserve all 8 materially applicable Open Product Decisions and all 11 materially applicable Open Architecture Decisions, select no prohibited policy or implementation mechanism, preserve `BEB → BIDN → BCUS → BPRD → BINV → BPRC`, and leave every Backend Specification title, path, scope code, decomposition, and ordering position after BPRC unresolved, including Category's later position.

## 8. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BPRC-AC-001 | BPRC-REQ-001 | Metadata shows `0.1.0 Draft`, owner `Pricing`, `authoritative: false`, and scope `BPRC`; text states non-normative Draft status and bounded future normativity. |
| BPRC-AC-002 | BPRC-REQ-002 | Coverage review maps REQ-PRC-001–035 completely to BPRC Requirements without authority transfer. |
| BPRC-AC-003 | BPRC-REQ-003 | Boundary review finds no non-Pricing truth assigned to BPRC. |
| BPRC-AC-004 | BPRC-REQ-004 | The BEB matrix accounts for BEB-REQ-001–056 exactly once with applicability, rationale, and BPRC trace. |
| BPRC-AC-005 | BPRC-REQ-005 | Identity evidence is consumed only through governed boundaries and cannot establish Pricing Authorization or truth. |
| BPRC-AC-006 | BPRC-REQ-006 | Customer or Account context neither transfers BCUS authority nor invents eligibility, segmentation, loyalty, or personalization policy. |
| BPRC-AC-007 | BPRC-REQ-007 | Product evidence supports Pricing association and revalidation while all Product truth remains BPRD-owned. |
| BPRC-AC-008 | BPRC-REQ-008 | Inventory evidence cannot establish Price, Pricing policy, or Pricing authority and no Inventory truth transfers. |
| BPRC-AC-009 | BPRC-REQ-009 | Protected behavior defaults safely and requires current server-side contextual Pricing Authorization. |
| BPRC-AC-010 | BPRC-REQ-010 | Dependency review preserves inward Pricing rules and Port/Adapter boundaries without selected mechanisms. |
| BPRC-AC-011 | BPRC-REQ-011 | Contract review proves intentional abstract exposure and prevents internal or concrete transport leakage. |
| BPRC-AC-012 | BPRC-REQ-012 | Use Case evidence keeps orchestration explicit, commands and queries truthful, and business rules out of Adapters. |
| BPRC-AC-013 | BPRC-REQ-013 | Persistence review preserves Pricing ownership and invariants without selecting a physical or conceptual storage design. |
| BPRC-AC-014 | BPRC-REQ-014 | Mutation and evaluation evidence preserves consistency, safe transaction boundaries, and explicit uncertainty without selecting mechanisms. |
| BPRC-AC-015 | BPRC-REQ-015 | Trusted current commercial outcomes contain only policy-permitted calculations from governed inputs and remain distinct from Order history. |
| BPRC-AC-016 | BPRC-REQ-016 | No Pricing result creates, redefines, or proves another Domain's truth, lifecycle, policy, or success. |
| BPRC-AC-017 | BPRC-REQ-017 | Correct Product or Product Variant evidence is required and every governed invalid state blocks silent trusted Pricing. |
| BPRC-AC-018 | BPRC-REQ-018 | Monetary values preserve explicit Currency and precision-safe semantics; unsafe mixing, conversion, or floating-point behavior is rejected without invented policy. |
| BPRC-AC-019 | BPRC-REQ-019 | Authoritative Price uses current governed context; missing, invalid, stale, and changed states are explicit and Order history is unchanged. |
| BPRC-AC-020 | BPRC-REQ-020 | Discount evaluation is governed, separate, reproducible, and safe without an invented formula or policy value. |
| BPRC-AC-021 | BPRC-REQ-021 | Promotions and Vouchers use only governed applicability and usage evidence, with safe explicit unusable outcomes and no invented policy. |
| BPRC-AC-022 | BPRC-REQ-022 | No unresolved stacking or combination rule is selected, and unresolved conflict fails safely. |
| BPRC-AC-023 | BPRC-REQ-023 | Tax outcomes require qualified policy and inputs; no unresolved tax, Invoice, Credit Note, jurisdiction, mapping, formula, provider, or history authority is selected. |
| BPRC-AC-024 | BPRC-REQ-024 | Shipping-commercial values require a governed handoff; operational Shipping and unresolved fee or benefit policy remain external. |
| BPRC-AC-025 | BPRC-REQ-025 | Customer-dependent outcomes identify governed eligibility context without defining Customer, Identity, segmentation, loyalty, personalization, or access policy. |
| BPRC-AC-026 | BPRC-REQ-026 | Pricing neither owns Inventory nor infers scarcity, availability Pricing, or purchase eligibility. |
| BPRC-AC-027 | BPRC-REQ-027 | Cart intent remains Cart-owned and retained commercial values remain non-authoritative until governed Pricing recalculation. |
| BPRC-AC-028 | BPRC-REQ-028 | Checkout receives current authoritative revalidation and explicit uncertainty without transferring Checkout or downstream authority. |
| BPRC-AC-029 | BPRC-REQ-029 | Current Pricing and confirmed Order Snapshot truth remain distinct and later changes cannot rewrite Order history. |
| BPRC-AC-030 | BPRC-REQ-030 | Pricing input to Payment establishes no Payment or provider truth, and Payment cannot redefine Pricing. |
| BPRC-AC-031 | BPRC-REQ-031 | Post-purchase, credit, fraud, and abuse policy remains unresolved; any governed evidence preserves ownership, history, Authorization, auditability, and safe failure. |
| BPRC-AC-032 | BPRC-REQ-032 | Equivalent governed contexts reproduce explainable outcomes; every material stale, changed, or uncertain input remains explicit and protected detail is concealed. |
| BPRC-AC-033 | BPRC-REQ-033 | Duplicate, replay, retry, concurrency, conflict, and uncertainty tests show no duplicate or conflicting commercial effect. |
| BPRC-AC-034 | BPRC-REQ-034 | Security review proves least privilege, isolation, minimization, protected-data handling, concealment, and proportional classification. |
| BPRC-AC-035 | BPRC-REQ-035 | Authorized recovery and reconciliation detect divergence, remain duplicate-safe, and neither invent policy nor rewrite history. |
| BPRC-AC-036 | BPRC-REQ-036 | Applicable WCAG 2.2 AA outcomes, bounded work, truthful degradation, and failure containment are verified without numerical targets. |
| BPRC-AC-037 | BPRC-REQ-037 | Material high-Risk Pricing actions produce separate proportional Audit Records while ordinary reads are not automatically over-audited. |
| BPRC-AC-038 | BPRC-REQ-038 | No event or integration exists without a governing need; applicable behavior is authority-safe, compatible, secure, duplicate-safe, observable, and mechanism-neutral. |
| BPRC-AC-039 | BPRC-REQ-039 | Contract evidence covers authority, association, Money, context, security, compatibility, uncertainty, and failures abstractly. |
| BPRC-AC-040 | BPRC-REQ-040 | Every listed failure is distinct, safe, explainable, recoverable where permitted, and conceals protected internals. |
| BPRC-AC-041 | BPRC-REQ-041 | Verification evidence covers every enumerated concern and maps to all BPRC Requirements. |
| BPRC-AC-042 | BPRC-REQ-042 | Operational evidence correlates and diagnoses critical Pricing outcomes without protected-data or rule leakage or invented targets. |
| BPRC-AC-043 | BPRC-REQ-043 | Compatibility, configuration, migration, boundedness, and failure containment are verified without selecting unresolved mechanisms or values. |
| BPRC-AC-044 | BPRC-REQ-044 | Decision, exclusion, and roadmap review finds 8 Product and 11 Architecture decisions unresolved, no prohibited mechanism, and nothing ordered after BPRC. |

## 9. Requirement Traceability

| Requirement | Pricing Domain evidence | BEB evidence | Additional governing evidence |
| --- | --- | --- | --- |
| BPRC-REQ-001 | REQ-PRC-001 | BEB-REQ-001–004, 056 | ADR-0006; ARCHITECTURE.md §35 |
| BPRC-REQ-002 | REQ-PRC-001–035 | BEB-REQ-003–004, 055 | ADR-0006 Decision |
| BPRC-REQ-003 | REQ-PRC-003 | BEB-REQ-003–004 | ADR-0006 Authority Boundary |
| BPRC-REQ-004 | REQ-PRC-035 | BEB-REQ-001–056 | ADR-0001; ARCHITECTURE.md §35 |
| BPRC-REQ-005 | REQ-PRC-015, 026–027 | BEB-REQ-011, 019–020, 043 | BIDN-REQ-004, 011–013, 021, 024 |
| BPRC-REQ-006 | REQ-PRC-015, 022, 026–027 | BEB-REQ-003, 019–021 | BCUS-REQ-005–006, 012–016, 034–035 |
| BPRC-REQ-007 | REQ-PRC-004–005 | BEB-REQ-003, 021, 024 | BPRD-REQ-012–016, 020–024, 033 |
| BPRC-REQ-008 | REQ-PRC-016 | BEB-REQ-003, 021, 024 | BINV-REQ-014–024, 029–032 |
| BPRC-REQ-009 | REQ-PRC-026 | BEB-REQ-011, 019–020 | SECURITY-STANDARDS.md §§10–13, 19–20 |
| BPRC-REQ-010 | REQ-PRC-003, 033 | BEB-REQ-005–007 | ARCHITECTURE.md §§8, 11, 35; SPRING.md |
| BPRC-REQ-011 | REQ-PRC-033 | BEB-REQ-008, 013–018 | API.md; ADR-0006 Explicit Non-Decisions |
| BPRC-REQ-012 | REQ-PRC-002–034 | BEB-REQ-009–010 | SPRING.md; ARCHITECTURE.md §35 |
| BPRC-REQ-013 | REQ-PRC-002, 006–012, 023–025, 028 | BEB-REQ-021–024 | DATABASE.md; POSTGRES.md |
| BPRC-REQ-014 | REQ-PRC-025 | BEB-REQ-012, 025–031 | DATABASE.md §§12–19, 51–52; SPRING.md |
| BPRC-REQ-015 | REQ-PRC-002 | BEB-REQ-003, 021, 024 | PRODUCT.md §§13, 16.1, 16.6; REQ-BUS-012, 015–016 |
| BPRC-REQ-016 | REQ-PRC-003 | BEB-REQ-003, 021 | AGENTS.md §31; ADR-0006 Authority Boundary |
| BPRC-REQ-017 | REQ-PRC-004–005 | BEB-REQ-015, 021, 041 | REQ-BUS-005–006, 012, 042; BPRD-REQ-012–016 |
| BPRC-REQ-018 | REQ-PRC-006 | BEB-REQ-014–015, 023 | JAVA.md §§22–23; POSTGRES.md §§12–13; API.md §14 |
| BPRC-REQ-019 | REQ-PRC-007–008 | BEB-REQ-021, 024, 041 | REQ-BUS-005, 012, 015–016, 022; API.md §42 |
| BPRC-REQ-020 | REQ-PRC-009 | BEB-REQ-021, 024, 041 | REQ-BUS-015–016; API.md §42 |
| BPRC-REQ-021 | REQ-PRC-010–011 | BEB-REQ-015, 021, 024, 041 | REQ-BUS-011–012, 015–016; SECURITY-STANDARDS.md §35 |
| BPRC-REQ-022 | REQ-PRC-012 | BEB-REQ-003, 041, 056 | PRODUCT.md §24; DECISIONS.md §§25, 29 |
| BPRC-REQ-023 | REQ-PRC-013 | BEB-REQ-003, 021, 024 | REQ-BUS-012, 015–016, 041, 054; PRODUCT.md §24 |
| BPRC-REQ-024 | REQ-PRC-014 | BEB-REQ-003, 021, 041–042 | REQ-BUS-012, 015–016, 029; PRODUCT.md §24 |
| BPRC-REQ-025 | REQ-PRC-015 | BEB-REQ-011, 019–021, 043 | REQ-BUS-007, 011–012, 032, 039 |
| BPRC-REQ-026 | REQ-PRC-016 | BEB-REQ-003, 021, 024 | BINV-REQ-015–018, 029–032; ARCHITECTURE.md §20.3 |
| BPRC-REQ-027 | REQ-PRC-017 | BEB-REQ-003, 008, 021 | REQ-BUS-009, 012, 015–016 |
| BPRC-REQ-028 | REQ-PRC-018 | BEB-REQ-003, 008, 021, 041 | REQ-BUS-011–013, 015; API.md §43 |
| BPRC-REQ-029 | REQ-PRC-019 | BEB-REQ-003, 021, 024 | REQ-BUS-021–023; ARCHITECTURE.md §40.5 |
| BPRC-REQ-030 | REQ-PRC-020 | BEB-REQ-003, 021, 024, 028–031 | REQ-BUS-024–028; ARCHITECTURE.md §20.2 |
| BPRC-REQ-031 | REQ-PRC-021–022 | BEB-REQ-003, 011, 021, 024, 041–045 | PRODUCT.md §24; REQ-BUS-022, 030, 048, 052 |
| BPRC-REQ-032 | REQ-PRC-023–024 | BEB-REQ-009–010, 017, 041 | REQ-BUS-012, 014–016, 042; API.md §§14, 42–43 |
| BPRC-REQ-033 | REQ-PRC-025 | BEB-REQ-025, 027, 029–031 | DATABASE.md §§12, 16, 51–52; API.md §§22, 24 |
| BPRC-REQ-034 | REQ-PRC-027 | BEB-REQ-011, 020, 043–044 | SECURITY-STANDARDS.md §§7–21, 25–28, 35–40 |
| BPRC-REQ-035 | REQ-PRC-028 | BEB-REQ-028, 034, 041–042, 046 | REQ-BUS-035–036, 042, 045 |
| BPRC-REQ-036 | REQ-PRC-029–030 | BEB-REQ-016, 041, 049–051 | TESTING-STANDARDS.md §§22–24; API.md §§28, 32, 47–48, 76 |
| BPRC-REQ-037 | REQ-PRC-031 | BEB-REQ-045–046 | REQ-BUS-033–034; SECURITY-STANDARDS.md §27; DATABASE.md §42 |
| BPRC-REQ-038 | REQ-PRC-032 | BEB-REQ-026, 029–040, 056 | EVENTS.md; ARCHITECTURE.md §§14, 34 |
| BPRC-REQ-039 | REQ-PRC-033 | BEB-REQ-008, 013–018, 056 | API.md §§12–18, 42–43, 63–72 |
| BPRC-REQ-040 | REQ-PRC-034 | BEB-REQ-017, 041–043 | API.md §§52–58; JAVA.md §28 |
| BPRC-REQ-041 | REQ-PRC-035 | BEB-REQ-052–055 | TESTING-STANDARDS.md §§5–34, 39–41 |
| BPRC-REQ-042 | REQ-PRC-028, 030–031, 034 | BEB-REQ-045–046 | AGENTS.md §3.9; ARCHITECTURE.md §18 |
| BPRC-REQ-043 | REQ-PRC-006, 025, 030, 033 | BEB-REQ-047–051 | ARCHITECTURE.md §§31, 38–39, 42–44; DATABASE.md §§26–35 |
| BPRC-REQ-044 | REQ-PRC-001, 003, 012–014, 016, 021–022, 032–035 | BEB-REQ-040, 048, 056 | ADR-0006; PRODUCT.md §24; ARCHITECTURE.md §§34–35 |

## 10. Pricing Domain Coverage Matrix

Every Approved Pricing Domain Requirement is covered exactly once in this matrix. Supporting BPRC Requirements may contribute to verification without changing the single primary coverage assignment.

| Pricing Domain Requirement(s) | Primary BPRC coverage | Coverage rationale |
| --- | --- | --- |
| REQ-PRC-001 | BPRC-REQ-001–002 | Lifecycle, bounded authority, and complete specialization. |
| REQ-PRC-002 | BPRC-REQ-015 | Current authoritative commercial calculation. |
| REQ-PRC-003 | BPRC-REQ-003, 016 | Cross-Domain non-authority. |
| REQ-PRC-004, REQ-PRC-005 | BPRC-REQ-007, 017 | Governed Product association and unsafe Product input handling. |
| REQ-PRC-006 | BPRC-REQ-018 | Money and Currency integrity. |
| REQ-PRC-007, REQ-PRC-008 | BPRC-REQ-019 | Authoritative Price and change safety. |
| REQ-PRC-009 | BPRC-REQ-020 | Discount boundary. |
| REQ-PRC-010, REQ-PRC-011 | BPRC-REQ-021 | Promotion and Voucher evaluation. |
| REQ-PRC-012 | BPRC-REQ-022 | Combination-policy deferral. |
| REQ-PRC-013 | BPRC-REQ-023 | Tax boundary. |
| REQ-PRC-014 | BPRC-REQ-024 | Shipping-commercial boundary. |
| REQ-PRC-015 | BPRC-REQ-006, 025 | Governed Customer and Identity eligibility input. |
| REQ-PRC-016 | BPRC-REQ-008, 026 | Inventory non-authority. |
| REQ-PRC-017 | BPRC-REQ-027 | Cart presentation and intent separation. |
| REQ-PRC-018 | BPRC-REQ-028 | Checkout revalidation. |
| REQ-PRC-019 | BPRC-REQ-029 | Current versus historical Order truth. |
| REQ-PRC-020 | BPRC-REQ-030 | Payment non-authority. |
| REQ-PRC-021, REQ-PRC-022 | BPRC-REQ-031 | Post-purchase, credit, fraud, and abuse boundaries. |
| REQ-PRC-023, REQ-PRC-024 | BPRC-REQ-032 | Explainability, reproducibility, and material change handling. |
| REQ-PRC-025 | BPRC-REQ-014, 033 | Evaluation, duplicate, replay, concurrency, and effect safety. |
| REQ-PRC-026 | BPRC-REQ-005, 009 | Protected Pricing Authorization. |
| REQ-PRC-027 | BPRC-REQ-034 | Pricing data protection. |
| REQ-PRC-028 | BPRC-REQ-035 | Recovery and reconciliation. |
| REQ-PRC-029, REQ-PRC-030 | BPRC-REQ-036 | Accessibility and bounded delivery. |
| REQ-PRC-031 | BPRC-REQ-037 | Proportional audit evidence. |
| REQ-PRC-032 | BPRC-REQ-038 | Conditional Pricing events. |
| REQ-PRC-033 | BPRC-REQ-011, 039 | Pricing Contract boundary. |
| REQ-PRC-034 | BPRC-REQ-040 | Explicit Pricing failures. |
| REQ-PRC-035 | BPRC-REQ-041 | Complete verification coverage. |

## 11. BEB Applicability and Inheritance Matrix

Each BEB Requirement is accounted for exactly once. A conditional capability becomes Applicable when later Approved governance introduces it.

| BEB Requirement(s) | Classification | Pricing rationale and BPRC trace |
| --- | --- | --- |
| BEB-REQ-001, BEB-REQ-002, BEB-REQ-003, BEB-REQ-004 | Applicable | Lifecycle, inheritance, non-authority, and specialization govern BPRC. BPRC-REQ-001–004. |
| BEB-REQ-005, BEB-REQ-006, BEB-REQ-007 | Applicable | Modular-monolith, hexagonal direction, and layer responsibilities apply. BPRC-REQ-010, 012. |
| BEB-REQ-008 | Applicable | Pricing requires an intentional public Module Contract. BPRC-REQ-011, 039. |
| BEB-REQ-009, BEB-REQ-010 | Applicable | Pricing commands and queries require explicit Use Cases and truthful deterministic semantics. BPRC-REQ-012, 032. |
| BEB-REQ-011 | Applicable | Protected Pricing and Customer-specific outcomes require contextual Authorization. BPRC-REQ-009, 025, 034. |
| BEB-REQ-012 | Applicable | Any governed Pricing mutation requires an owned transaction boundary. BPRC-REQ-014. |
| BEB-REQ-013, BEB-REQ-014, BEB-REQ-015, BEB-REQ-016, BEB-REQ-017, BEB-REQ-018 | Applicable | Applicable Pricing Contracts inherit versioning, DTO separation, validation, bounds, error, description, and evolution safeguards without concrete surfaces. BPRC-REQ-011, 036, 039–040. |
| BEB-REQ-019, BEB-REQ-020 | Applicable | BPRC consumes Identity evidence and enforces Pricing Authorization and concealment without Identity ownership. BPRC-REQ-005, 009, 034. |
| BEB-REQ-021, BEB-REQ-022, BEB-REQ-023, BEB-REQ-024 | Applicable | Pricing owns its authoritative data and exact Money representation behind abstract Ports while preserving historical Order truth. BPRC-REQ-013, 018–019, 029. |
| BEB-REQ-025 | Applicable | Any Pricing state change must preserve affected commercial invariants atomically. BPRC-REQ-014, 033. |
| BEB-REQ-026 | Not currently applicable | No Pricing external provider or network workflow is authorized; inherited if later Approved tax, currency, fraud, shipping-commercial, or other integration requires one. BPRC-REQ-038, 044. |
| BEB-REQ-027 | Applicable | Concurrent Pricing work must preserve consistent governed outcomes and effects. BPRC-REQ-014, 033. |
| BEB-REQ-028 | Applicable | Any delayed or uncertain Pricing workflow requires explicit recoverable state and reconciliation evidence. BPRC-REQ-035. |
| BEB-REQ-029, BEB-REQ-030, BEB-REQ-031 | Applicable | Harmful duplicate Pricing effects require owned idempotency, replay, and duplicate safety without selected keys or retention. BPRC-REQ-014, 033, 035. |
| BEB-REQ-032, BEB-REQ-033, BEB-REQ-034 | Not currently applicable | No Pricing provider capability is authorized; inherited if later Approved provider integration exists. BPRC-REQ-035, 038, 044. |
| BEB-REQ-035, BEB-REQ-036 | Not currently applicable | No Pricing Callback or Webhook capability is authorized; authenticity, replay, acknowledgement, and recovery remain conditional. BPRC-REQ-038. |
| BEB-REQ-037, BEB-REQ-038, BEB-REQ-039 | Not currently applicable | Pricing events are conditional and no event Contract or delivery mechanism is authorized; inherited when separately governed. BPRC-REQ-038. |
| BEB-REQ-040 | Applicable | BPRC preserves in-process-first delivery and cannot adopt external messaging independently. BPRC-REQ-038, 044. |
| BEB-REQ-041, BEB-REQ-042 | Applicable | Pricing failures, uncertainty, recovery, and reconciliation require safe classification and handling. BPRC-REQ-035, 040. |
| BEB-REQ-043, BEB-REQ-044 | Applicable | Pricing data, Contracts, evidence, configuration, and operational surfaces require protection and Secret safety. BPRC-REQ-034, 042–043. |
| BEB-REQ-045, BEB-REQ-046 | Applicable | Material Pricing actions require separate Audit Records and safe operational evidence. BPRC-REQ-037, 042. |
| BEB-REQ-047, BEB-REQ-048 | Applicable | Configuration and reachable feature-flag states must preserve Pricing safeguards without selecting mechanisms. BPRC-REQ-043–044. |
| BEB-REQ-049, BEB-REQ-050, BEB-REQ-051 | Applicable | Pricing persistence, Contracts, deployments, and workloads require safe migration, compatibility, bounds, and failure containment. BPRC-REQ-013, 036, 039, 043. |
| BEB-REQ-052, BEB-REQ-053, BEB-REQ-054, BEB-REQ-055 | Applicable | Pricing behavior, Adapters, Contracts, architecture, operations, and traceability require complete verification. BPRC-REQ-041. |
| BEB-REQ-056 | Applicable | BPRC remains policy- and implementation-neutral and preserves every later roadmap decision. BPRC-REQ-001–004, 038–044. |

## 12. Upstream Consumption Boundaries

### 12.1 BIDN

BPRC consumes only materially applicable trusted Identity evidence through governed Contracts. BIDN remains authoritative for Authentication, Principal establishment, credentials, Sessions, revocation, recovery verification, Roles, Permissions, Claims, Scope, and Identity evidence. BPRC independently enforces Pricing contextual Authorization; Authentication or any supplied identity attribute alone grants no Pricing action or Customer-specific outcome. No Identity Provider, protocol, token, Session, MFA, SSO, or Role/Permission mechanism is selected.

### 12.2 BCUS

BPRC consumes Customer or Account context only where Approved Pricing policy permits. BCUS retains Customer and Account authority. Customer identity, Account association, profile, Address, Preference, Consent, segment, entitlement, loyalty, or personalization neither creates Pricing truth nor authorizes Pricing behavior. No eligibility class or Customer policy is selected.

### 12.3 BPRD

BPRC consumes governed Product and Product Variant identity and state evidence only for correct Pricing association and revalidation. BPRD retains Product authority. Product existence, publication, visibility, content, structural sellability, Product-owned Attributes, or Product Media cannot create Price; Pricing outcomes cannot publish Product or redefine Product truth.

### 12.4 BINV

BPRC consumes governed Inventory evidence only where Approved Pricing policy explicitly permits. BINV retains Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, and Overselling authority. Inventory state cannot independently create or alter Price or Pricing Rules, and BPRC cannot infer scarcity or availability-based Pricing or purchase eligibility.

## 13. Open Product and Architecture Decisions

The following **8 materially applicable Open Product Decisions** from `PRODUCT.md` §24 remain unresolved:

| Product decision | Preserved BPRC boundary |
| --- | --- |
| Shipping provider, service levels, delivery areas, and fee policy | No Shipping provider, service, area, delivery charge, or fee policy is selected. |
| Free-delivery threshold and promotional treatment | No threshold or promotional treatment is selected. |
| Tax-inclusive display and invoice requirements | No Tax Inclusive or Tax Exclusive presentation or Invoice policy is selected. |
| Returns, exchanges, and refund policy | No post-purchase recalculation, reversal, exchange, or Refund treatment is selected. |
| Voucher and promotion stacking policy | No Promotion Combination, precedence, or stacking rule is selected. |
| South African tax-display, invoice, and credit-note policy | No jurisdictional display, Invoice, or Credit Note policy is selected. |
| Fraud-screening approach and manual-review workflow | No provider, score, rule, threshold, blocking action, or review workflow is selected. |
| Gift cards, store credit, and promotional credit policy | No authority, lifecycle, eligibility, redemption, or calculation treatment is selected. |

The following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 remain unresolved:

| Architecture decision | Preserved BPRC boundary |
| --- | --- |
| Backend hosting | No hosting service or topology is selected. |
| Infrastructure as code | No IaC tool or structure is selected. |
| Customer and administrator session/token strategy | No Session or token mechanism is selected. |
| Payment provider selection | BPRC provides no provider selection or Payment authority. |
| Shipping provider selection | BPRC provides no provider selection or Shipping authority. |
| Redis introduction and its approved use cases | No cache or Redis use is selected. |
| External messaging introduction and service selection | Events remain conditional and no messaging service is selected. |
| Initial Search implementation details and extraction thresholds | BPRC supplies no Search implementation or extraction decision. |
| Backup retention and production recovery objectives | Recoverability is required without retention or numerical objectives. |
| PostgreSQL schema strategy | Pricing ownership is preserved without selecting schema layout. |
| Repository-wide feature-flag implementation and lifecycle management | Flag safety is inherited without selecting a mechanism. |

Price List structures, Pricing algorithms, formulas, effective-date policy, rounding, monetary precision policy, exchange-rate behavior, Customer segmentation, scarcity Pricing, and other numerical Pricing rules remain unresolved scope boundaries or Explicit Exclusions; they are not represented as additional `PRODUCT.md` §24 decisions.

## 14. Explicit Exclusions

BPRC selects no Pricing algorithm, formula, Price List structure, effective-date mechanism, rounding rule, scale, precision, Minor Unit rule, exchange-rate behavior, Discount, Promotion, Voucher, stacking, tax, shipping-fee, fraud, gift-card, store-credit, promotional-credit, Customer-segmentation, personalization, scarcity, or availability-based Pricing policy; identifier design; Role or Permission matrix; API route, method, status, DTO, or payload; database schema, table, SQL, ORM mapping, index, lock, isolation level, cache, or Redis use; event name, schema, payload, topic, queue, broker, delivery technology, or choreography; provider; infrastructure or deployment topology; numerical limit, threshold, timeout, retry count, retention, performance target, SLA, SLO, or recovery objective; or Backend Specification title, path, scope code, decomposition, or ordering after BPRC.

## 15. Risks and Controls

| Risk | Required control direction |
| --- | --- |
| Stale commercial state is presented as current | Revalidate governed inputs and keep stale, changed, and uncertain outcomes distinct. |
| Money or Currency is mishandled | Preserve explicit Currency and precision-safe Money semantics and reject mixing or conversion. |
| Cart presentation is treated as locked Price | Preserve Cart intent and require authoritative Pricing revalidation before commitment. |
| Promotion, Voucher, tax, or shipping policy is invented | Preserve Open Decisions and fail safely when policy is unresolved. |
| Pricing authority leaks across Domains | Enforce upstream consumption and downstream non-authority boundaries. |
| Historical Order values are rewritten | Keep current Pricing truth separate from confirmed Order Snapshots. |
| Customer-specific outcomes leak or overreach | Enforce contextual Authorization, minimization, isolation, and safe disclosure. |
| Duplicate or concurrent work creates conflicting effects | Preserve deterministic context, explicit conflict, duplicate safety, and reconciliation. |
| Abuse-sensitive rule detail leaks | Protect errors, logs, Contracts, events, Audit Records, and Projections proportionately. |
| Dependency failure becomes false success | Preserve unavailable and uncertain states with governed recovery. |
| Provider, cache, event, or persistence mechanism is selected implicitly | Enforce conditional applicability and explicit Architecture governance. |
| Later roadmap ordering is inferred from BPRC | Preserve every position after BPRC as unresolved. |

## 16. Related Documents

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
- `specifications/adr/ADR-0002-identity-access-backend-specification.md`
- `specifications/adr/ADR-0003-customer-account-backend-specification.md`
- `specifications/adr/ADR-0004-catalogue-backend-specification.md`
- `specifications/adr/ADR-0005-post-product-backend-specification.md`
- `specifications/adr/ADR-0006-post-inventory-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## 17. Governance Review Requirements

Before promotion, reviewers MUST confirm complete Pricing Domain coverage; one-to-one Requirement, Acceptance Criterion, and traceability; complete BEB applicability; bounded BIDN, BCUS, BPRD, and BINV consumption; Pricing-only authority; preservation of all 8 Product and 11 Architecture decisions; security, privacy, integrity, failure, recovery, observability, compatibility, and verification completeness; and absence of invented policy, implementation mechanisms, numerical values, or later-roadmap positions. Drafting this Specification does not constitute approval.

## 18. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Draft | Established the initial Pricing Backend Specification with Pricing-only authority, complete Pricing Domain coverage, complete BEB applicability, bounded BIDN/BCUS/BPRD/BINV consumption, open decisions, Acceptance Criteria, and traceability. |

## 19. Final Validation

Before Draft approval review, verify that:

1. metadata remains `0.1.0 Draft`, `authoritative: false`, owner `Pricing`, and scope `BPRC`;
2. all BPRC Requirements remain subordinate to governing sources and specialize only the Approved Pricing Domain;
3. the canonical sequence is `BEB → BIDN → BCUS → BPRD → BINV → BPRC`, with every later title, path, scope code, decomposition, and order unresolved;
4. Product, Category, Inventory, Search, Customer, Identity, Cart, Checkout, Order, Payment, Shipping, Return, Administration, Reporting, and every other Domain authority remains intact;
5. BEB-REQ-001 through BEB-REQ-056 are each accounted for exactly once;
6. BIDN, BCUS, BPRD, and BINV consumption boundaries remain explicit and non-authoritative;
7. every BPRC Requirement has exactly one corresponding Acceptance Criterion and traceability row;
8. all 35 Approved Pricing Domain Requirements are covered exactly once in the Pricing Domain Coverage Matrix;
9. all 8 Product and 11 Architecture decisions listed here remain unresolved;
10. no Pricing formula, Price List, effective-date, rounding, exchange-rate, Discount, Promotion, Voucher, tax, shipping-fee, fraud, credit, segmentation, scarcity, API, DTO, schema, persistence, event, provider, cache, infrastructure, numerical, authorization-matrix, or later-roadmap decision is invented;
11. security, observability, audit, failure, recovery, reconciliation, compatibility, boundedness, accessibility, and verification remain complete and mechanism-neutral; and
12. the BPRC lifecycle change affects only `specifications/backend/pricing/pricing-backend.md` and introduces no unrelated repository changes.
