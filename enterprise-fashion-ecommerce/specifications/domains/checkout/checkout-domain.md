---
title: Checkout Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-04
authoritative: false
---

# Checkout Domain

## 1. Purpose

This Checkout Domain Specification defines implementation-neutral authority for governed purchase orchestration from Cart intent through authoritative revalidation, Payment initiation, and the Order-creation handoff.

This document uses requirement scope code `CHK`. Because its status is Approved, its Requirements are normative only within the Checkout Domain scope, remain subordinate to higher-authority governing sources under the repository Decision Hierarchy, preserve Approved upstream Domain authority within each upstream scope, and do not resolve Open Product Decisions.

## 2. Scope and Authority

### REQ-CHK-001 — Lifecycle, Authority, and Scope

The Checkout Domain MUST govern only Checkout-owned purchase-orchestration truth, preserve governing-source precedence and Approved upstream Domain authority, use scope code `CHK`, and MUST treat this Approved Specification as normative only within the Checkout Domain scope and not as repository-wide authority.

### REQ-CHK-002 — Purchase-Orchestration Authority

Checkout MUST own the governed coordination, correlation, progression, submission integrity, failure, uncertainty, and recovery outcomes of the purchase-orchestration process. Coordination MUST NOT transfer ownership of consumed Domain truth to Checkout.

### REQ-CHK-003 — Cross-Domain Non-Authority

Checkout MUST NOT own or redefine Product, Product Variant, Category, catalogue or merchandising truth, Customer or Principal identity, Account, Session, Authentication, Authorization policy, Role, Permission, Claims, Scope, Address ownership, Cart intent or lifecycle, Pricing, Price, Discount, Promotion, Voucher, Tax, Currency conversion, Inventory, Stock, Available-to-Sell, Stock Reservation lifecycle, Stock Adjustment, Stock Movement, Overselling, Shipping, Fulfilment, Delivery Method, Shipping Rate, Shipment, Carrier, Dispatch, Tracking Number, delivery truth, Payment, Payment Attempt, Payment Provider evidence, Payment Transaction, Payment Authorization, Capture, Void, Settlement, Refund, Chargeback, Order identity or history, Order Snapshot authority, Return policy, Administration policy, Notification delivery truth, or analytical Projection authority.

## 3. Domain Context

Checkout is a purchase-orchestration Domain that consumes governed facts from owning Domains, coordinates their revalidation, initiates permitted downstream work, and preserves an explicit orchestration outcome. It is not a replacement source of truth for any fact it consumes.

## 4. Canonical Terminology

Canonical terms follow `GLOSSARY.md`. Checkout, Cart, Cart Item, Product, Product Variant, Category, Customer, Account, Address, Shipping Address, Principal, Session, Authentication, Authorization, Role, Permission, Claims, Scope, Price, Discount, Promotion, Voucher, Tax, Money, Currency, Inventory, Stock, Available-to-Sell, Stock Reservation, Delivery Method, Shipping Rate, Payment, Payment Attempt, Payment Authorization, Payment Transaction, Order, Order Item, Order Snapshot, Contract, Domain Event, Integration Event, Projection, Audit Record, Sensitive Data, and Risk retain their canonical meanings. Phrases such as Checkout orchestration context, Checkout submission, Checkout progression, purchase readiness, and orchestration outcome are ordinary descriptions and do not establish a canonical Checkout Session, entity, Aggregate, lifecycle, or state machine.

## 5. Checkout Context and Correlation

### REQ-CHK-004 — Governed Context Correlation

Checkout MUST correlate its orchestration context to the applicable governed Cart intent, actor context, commercial context, Inventory outcome, Shipping outcome, Payment initiation, and Order handoff where required. Unknown, inaccessible, ambiguous, mismatched, or stale associations MUST fail safely without exposing protected Resource existence.

### REQ-CHK-005 — Entry and Purchase Readiness

Checkout progression MAY begin only from permitted governed context and MUST distinguish ready, incomplete, invalid, stale, unavailable, denied, and uncertain inputs where materially different. Entry into Checkout MUST NOT prove final purchasability, Payment success, Stock Reservation, Shipping eligibility, or Order existence, and no concrete Checkout state enumeration is established.

## 6. Cart Boundary

### REQ-CHK-006 — Cart Intent Handoff

Checkout MAY consume governed Cart and Cart Item purchase intent through Contracts and MUST preserve applicable correlation, quantities, and references for validation. It MUST NOT rewrite historical Cart intent, own Cart lifecycle, infer final purchase readiness from Cart presence, or define Cart-clearing timing or post-Order Cart behavior.

### REQ-CHK-007 — Cart Revalidation and Staleness

Checkout MUST treat Cart Price, availability, delivery, Promotion, Voucher, and other downstream representations as provisional and MUST coordinate current owning-Domain revalidation before commitment. Invalid, stale, changed, unavailable, or inconsistent Cart context MUST interrupt unsupported progression with an explicit recoverable outcome where permitted.

## 7. Product and Category Boundary

### REQ-CHK-008 — Product and Product Variant Revalidation

Checkout MUST obtain current governed Product and Product Variant identity, association, publication, and structural-sellability outcomes required for the intended transaction. It MUST NOT recreate Product rules, Category hierarchy or membership, classification, merchandising, or publication truth.

### REQ-CHK-009 — Product Change Safety

A material Product or Product Variant disagreement, invalid association, or stale Product context MUST remain explicit and MUST prevent irreversible progression based on unsupported assumptions. Checkout MUST NOT invent a Product lifecycle state or independently reactivate, publish, classify, or declare a Product Variant sellable.

## 8. Customer, Visitor, Address, and Identity Boundary

### REQ-CHK-010 — Governed Customer or Visitor Context

Checkout MUST distinguish permitted authenticated Customer context from permitted unauthenticated Visitor context without deciding whether guest Checkout, Account creation, or email verification is required. Applicable contact context MUST be limited to Approved Product policy, and supplied Customer identifiers MUST NOT establish identity or access.

### REQ-CHK-011 — Address and Historical Context

Checkout MAY consume the governed Address or Shipping Address context required for purchase orchestration and MUST handle invalid, stale, inaccessible, or mismatched context safely. It MUST NOT own Address truth, invent Address fields, or permit later Customer Address changes to rewrite retained Order or Shipment history.

### REQ-CHK-012 — Authentication and Authorization Boundary

Protected Checkout actions MUST require trusted server-side Authorization for the current Principal, Resource, action, and Domain state where applicable. UI visibility, route, browser state, Session presence, Role labels, client Permissions, Claims, Scope, Authentication alone, or supplied identifiers MUST NOT authorize an action, and Checkout MUST NOT become Identity authority.

## 9. Pricing, Money, and Currency

### REQ-CHK-013 — Authoritative Pricing Revalidation

Before commercial commitment, Checkout MUST request and consume current authoritative Pricing outcomes for applicable Price, Discount, Promotion, Voucher, Tax, Shipping charge treatment, and totals. Checkout MUST NOT calculate or silently override those outcomes, define their policy, or trust client-supplied commercial values.

### REQ-CHK-014 — Material Commercial Change

Checkout MUST compare applicable previously presented commercial context with current authoritative Pricing outcomes and MUST make material change, disagreement, stale state, failure, or uncertainty explicit before continuation. Unsupported commercial context MUST prevent irreversible progression without Checkout choosing a threshold, stacking, rounding, or Tax rule.

### REQ-CHK-015 — Money and Currency Integrity

Checkout monetary context MUST use explicit Money and Currency with precision-safe, non-floating-point semantics, preserve applicable Version 1 ZAR governance, reject silent Currency mixing and implicit conversion, and defer authoritative calculation and rounding to Pricing. No storage type, scale, Minor Unit, foreign-exchange, or conversion rule is established.

## 10. Inventory and Stock Reservation

### REQ-CHK-016 — Current Inventory Revalidation

Checkout MUST request current trusted Inventory outcomes for applicable Product Variant availability and Available-to-Sell before governed commitment. It MUST NOT calculate, infer, fabricate, or mutate Stock, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling truth, and insufficient, unavailable, stale, conflicting, or unknown Inventory outcomes MUST interrupt unsupported progression.

### REQ-CHK-017 — Stock Reservation Coordination

Checkout MAY request and correlate an applicable Stock Reservation outcome through governed Contracts, but Inventory MUST retain authority for reservation identity, lifecycle, availability effect, conflict, expiry, release, consumption, and finalization. Checkout MUST NOT select a duration, interval, release timeout, consumption timing, lock, or persistence mechanism.

### REQ-CHK-018 — Reservation Failure and Duplicate Safety

Stale, duplicate, replayed, concurrent, unavailable, conflicting, or uncertain Stock Reservation activity MUST produce a distinguishable safe outcome. Checkout MUST verify or reconcile an unknown prior reservation effect before repetition and MUST NOT treat elapsed client time, Cart state, Payment state, or Order expectation as Inventory evidence.

## 11. Shipping and Fulfilment Boundary

### REQ-CHK-019 — Delivery Choice and Eligibility Coordination

Checkout MAY request and consume governed Delivery Method choices and Shipping eligibility outcomes using applicable purchase and Shipping Address context. It MUST NOT determine delivery eligibility, service rules, provider availability, Delivery Method authority, Shipment, Carrier, Dispatch, tracking, Fulfilment, or delivery truth.

### REQ-CHK-020 — Shipping Rate Revalidation

Checkout MAY consume applicable Shipping Rate or quotation evidence with explicit Money, Currency, applicability, and freshness, and MUST request revalidation when materially stale or required before commitment. Checkout MUST NOT calculate a Shipping Rate, select a fee or threshold, resolve Shipping/Pricing disagreement, or trust a client-declared Shipping charge; disagreement MUST remain explicit and safely revalidated or reconciled by owning Domains.

## 12. Final Revalidation

### REQ-CHK-021 — Coordinated Final Revalidation

Before an irreversible or externally visible purchase effect, Checkout MUST coordinate current authoritative revalidation of applicable Product and Product Variant validity, Cart intent, Customer or Visitor context, Shipping Address, Authorization, Pricing, Discount, Promotion, Voucher, Tax, Money, Currency, Inventory, Stock Reservation, Delivery Method, Shipping Rate, Shipping eligibility, and Payment initiation context. Checkout MUST request or consume owning-Domain outcomes rather than recompute their truth.

### REQ-CHK-022 — Revalidation Disagreement and Progression

Material disagreement, invalid or stale state, unavailable authority, denied context, or unresolved uncertainty from final revalidation MUST prevent unsupported progression and remain explicit. Where continuation is permitted, applicable material commercial or delivery changes MUST be communicated before continuation without inventing presentation wording or policy.

## 13. Submission Integrity

### REQ-CHK-023 — Governed Checkout Submission

A Checkout submission MUST correlate to current validated orchestration context and MUST distinguish request, acceptance for processing, downstream effects, and confirmed orchestration outcome. Repeated browser action, navigation, stopped observation, or client state MUST NOT prove submission success, failure, or cancellation.

### REQ-CHK-024 — Duplicate-Effect Prevention

Duplicate, retried, replayed, stale, or concurrent Checkout submissions MUST NOT create duplicate Payment, Payment Attempt, financial, Stock, Stock Reservation, Shipping, Order, or other commercial effects. Applicable Contracts MUST preserve Idempotency Key semantics without selecting a key format, database constraint, lock, isolation level, table, cache, queue, or retry count.

### REQ-CHK-025 — Safe Retry, Replay, and Concurrency

Before repeating an operation with a potentially irreversible effect, Checkout MUST determine from governed evidence whether the prior effect is known, unknown, or safely repeatable. Conflicting replay, reordered response, concurrent submission, and stale input MUST NOT silently overwrite accepted truth or multiply effects, and uncertain activity MUST be verified or reconciled before repetition.

### REQ-CHK-026 — Dependency Timeout and Partial Completion

Dependency timeout, unavailability, delayed evidence, or partial downstream completion MUST remain distinguishable from confirmed success and definitive failure. Checkout MUST preserve known completed effects, unknown effects, and authoritative failure provenance without assuming that request interruption stopped external processing.

## 14. Payment Boundary

### REQ-CHK-027 — Payment Initiation Handoff

Checkout MAY provide Payment with validated purchase context, correlation, explicit Money and Currency, and applicable trusted Principal and Authorization context. Payment MUST retain authority for Payment and Payment Attempt creation and processing, and Checkout MUST NOT select a method or provider, handle raw payment-card data, or establish Payment truth.

### REQ-CHK-028 — Payment Evidence and Success Boundary

Checkout MAY consume authoritative Payment evidence for orchestration, but a Payment Redirect, browser result, client callback or report, UI state, local Checkout state, Order expectation, unvalidated provider information, or assumption MUST NOT establish Payment success or failure. Payment success MUST NOT prove Checkout success or Order existence.

### REQ-CHK-029 — Payment Failure and Uncertainty

Declined, failed, cancelled, pending, unknown, delayed, timed-out, mismatched, stale, duplicate, or otherwise uncertain Payment outcomes MUST remain distinguishable according to Payment authority. Retry after a possible Payment effect MUST be evidence-aware and duplicate-safe, and Checkout MUST NOT convert Payment uncertainty into success, definitive failure, or an Order.

## 15. Order Boundary

### REQ-CHK-030 — Governed Order-Creation Handoff

Checkout MAY establish the governed orchestration outcome through which Order creation is requested, preserving applicable correlated Cart, Customer, Address, commercial, Payment, Inventory, and delivery context. It MUST NOT establish Order identity, creation success, lifecycle, state, history, Order Snapshot authority, Order Item history, persistence, event, or status names, and defines no future Order Requirement.

### REQ-CHK-031 — Payment Success and Order Failure

Payment success followed by unsuccessful, delayed, duplicate, unavailable, or uncertain Order creation MUST remain an explicit recoverable or reconcilable mismatch. It MUST NOT be represented as false Checkout success, false Payment failure, or proof that an Order exists, and recovery MUST preserve Payment and future Order authority without inventing Order behavior.

## 16. Failure, Recovery, and Reconciliation

### REQ-CHK-032 — Failure Provenance

Checkout MUST distinguish applicable invalid Cart intent, Product failure, Pricing disagreement, Inventory unavailability, Stock Reservation failure, Shipping eligibility failure, stale Shipping Rate, Customer or Address failure, Authorization denial, dependency failure, Payment failure or uncertainty, Order-handoff failure or uncertainty, and Checkout orchestration failure. Failure handling MUST preserve the owning Domain and MUST NOT collapse materially different outcomes into fabricated generic truth.

### REQ-CHK-033 — Controlled Recovery and Reconciliation

Checkout recovery from interruption, abandonment, duplicate submission, changed evidence, delayed outcome, timeout, or partial completion MUST be explicit, authorized where required, observable, correlation-preserving, and duplicate-safe. Reconciliation MUST use governed evidence, preserve history, and produce a resolved or still-uncertain outcome without directly rewriting another Domain's authoritative records.

## 17. Security and Sensitive Data

### REQ-CHK-034 — Untrusted Client and Tamper Resistance

Checkout MUST NOT trust client-controlled Price, total, Discount, Promotion or Voucher result, Shipping Rate, Tax, Inventory availability, Stock Reservation state, Payment or Order status, Authorization decision, Claims, Role, Permission, Scope, or protected identifier. Applicable inputs and decisions MUST be validated through trusted server-side authority, with replay and CSRF protection where relevant and without prescribing a mechanism.

### REQ-CHK-035 — Sensitive Data and Resource Protection

Checkout MUST minimize and protect applicable Customer information, contact context, Shipping Address, identifiers, credentials, tokens, Payment-adjacent context, and other Sensitive Data through purpose limitation, least privilege, Resource isolation, tamper resistance, and safe handling across storage, transmission, logs, errors, Contracts, events, Projections, analytics, exports, and support surfaces. Failures MUST resist protected Resource enumeration, and ordinary non-sensitive commercial data MUST NOT automatically be classified as Sensitive Data.

## 18. Fraud Boundary

### REQ-CHK-036 — Fraud and Manual-Review Non-Authority

Checkout MAY consume a future governed fraud, restriction, or review outcome where applicable but MUST NOT invent fraud scoring, provider, threshold, engine, policy, or manual-review workflow, treat a fraud signal as Payment evidence, or silently convert an external Risk signal into authoritative commercial truth. False-positive recovery and intervention remain subject to Approved policy.

## 19. Tax, Invoice, and Credit Note Boundary

### REQ-CHK-037 — Commercial-Document Non-Authority

Checkout MAY present or carry governed commercial information required for purchase orchestration but MUST NOT define South African Tax or Tax-display policy, calculate Tax independently, define invoice or Credit Note policy, establish numbering or accounting treatment, or claim legal interpretation. Unresolved policy MUST remain external and production reliance MUST follow qualified governance where required.

## 20. Contracts

### REQ-CHK-038 — Checkout Contract Boundary

Future Checkout Contracts MUST preserve owning authority, correlation, explicit input and outcome semantics, invalid, stale, unavailable and uncertain results, compatibility, applicable Idempotency Key semantics, retry and replay safety, Authorization, Sensitive Data protection, and explicit failure behavior. No URL, route, HTTP method or status, GraphQL operation, JSON shape, DTO, class, controller, component, service, database table or schema, persistence model, or provider payload is defined.

## 21. Events

### REQ-CHK-039 — Conditional Checkout Events

A Domain Event or Integration Event MUST exist only where Architecture, an Approved Contract, reliability, or synchronization governance requires it. Any event MUST preserve owning-Domain authority, correlation, compatibility, Sensitive Data protection, duplicate and replay safety, ordering uncertainty where applicable, and non-authoritative consumer Projection semantics without defining an event name, taxonomy, schema, payload, topic, queue, broker, delivery guarantee, or choreography.

## 22. Accessibility

### REQ-CHK-040 — Accessible Checkout Outcomes

Applicable Checkout entry, contact, Address, delivery, Promotion or Voucher, review, submission, Payment handoff, validation, material-change, pending, failure, and recovery surfaces MUST target WCAG 2.2 AA and provide applicable keyboard operation, focus management, labels, instructions, error identification and summaries, accessible status changes, assistive-technology compatibility, unavailable-choice explanation, and non-misleading progress or success. No certification, Level AAA, visual design, component, or Customer-facing wording is established.

## 23. Performance and Observability

### REQ-CHK-041 — Bounded and Observable Checkout

Checkout interactions and Contracts MUST be bounded, observable, and correlated sufficiently to attribute applicable dependency latency, timeout, contention, unavailability, stale outcome, retry, Payment uncertainty, Inventory contention, Shipping revalidation, partial completion, failure, recovery, and reconciliation. No numerical latency, timeout, retry, throughput, device, network, SLA, or SLO target is established.

## 24. Audit and Operational Intervention

### REQ-CHK-042 — Proportional Checkout Audit Evidence

Applicable materially high-Risk administrative recovery, reconciliation, authorization-sensitive action, suspicious or abusive activity, Payment and Order mismatch recovery, or exceptional override where Approved policy permits one MUST produce proportional Audit Records and use trusted server-side Authorization. Ordinary Checkout reads and routine progression MUST NOT automatically become high-Risk audit events, and no Role or Permission matrix, approval chain, or escalation SLA is defined.

## 25. Notifications, Reporting, and Analytics

### REQ-CHK-043 — Non-Authoritative Representations

Notifications, reporting, analytics, exports, and Projections MUST derive from governed authoritative outcomes, protect Sensitive Data, and identify their source where applicable. They MUST NOT determine or mutate Checkout success, replace owning-Domain records, or claim Payment or Order success prematurely; their failure MUST NOT alter transactional truth, and no provider, taxonomy, report, export format, or delivery channel is selected.

## 26. Testing

### REQ-CHK-044 — Checkout Verification Coverage

Verification evidence MUST cover applicable positive and negative flows; entry and readiness; Cart, Product, Category, Customer, Visitor, Address and Identity boundaries; Pricing, Money and Currency; Inventory and Stock Reservation; Shipping; final revalidation; submission; Payment and Order handoffs; failure, timeout, uncertainty, duplicate, retry, replay, concurrency, partial completion, recovery and reconciliation; security, Authorization, Sensitive Data and fraud boundaries; Tax and commercial documents; Contracts, events, accessibility, performance, observability, audit, notifications, reporting and analytics. No test framework, test-case identifier scheme, tooling, or numerical coverage target is selected.

## 27. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-CHK-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; scope is `CHK`; Approved Domain-bounded normativity, governing precedence, upstream authority, and absence of repository-wide authority are identifiable. |
| REQ-CHK-002 | Checkout owns each listed orchestration concern while no coordinated fact transfers from its owning Domain. |
| REQ-CHK-003 | Checkout owns or redefines none of the listed external Domain truths, policies, lifecycles, records, or representations. |
| REQ-CHK-004 | Required context correlates to every applicable owning-Domain input and handoff; all listed invalid associations fail safely without protected enumeration. |
| REQ-CHK-005 | Permitted context alone begins progression; each listed readiness condition is distinguishable; entry proves none of the prohibited downstream outcomes and creates no state enumeration. |
| REQ-CHK-006 | Checkout consumes correlated Cart intent without rewriting it, owning Cart lifecycle, inferring readiness, or selecting clearing and post-Order behavior. |
| REQ-CHK-007 | Provisional Cart representations are authoritatively revalidated; every listed degraded context interrupts unsupported progression with an explicit permitted recovery outcome. |
| REQ-CHK-008 | Required Product facts come from current governed Product truth while Checkout recreates none of the listed Product or Category authority. |
| REQ-CHK-009 | Material Product disagreement or staleness is explicit and blocks unsupported irreversible progression; Checkout performs none of the prohibited Product actions. |
| REQ-CHK-010 | Customer and Visitor contexts remain policy-permitted and distinguishable without deciding guest, Account, or verification policy; contact collection follows policy and supplied identifiers grant no authority. |
| REQ-CHK-011 | Required Address context is governed; every listed invalid condition fails safely; Checkout owns no Address fields or truth and later Address change rewrites no retained history. |
| REQ-CHK-012 | Protected actions use contextual server-side Authorization; none of the listed client or identity facts authorizes alone, and Identity remains external. |
| REQ-CHK-013 | Every applicable listed commercial value comes from current Pricing; Checkout neither calculates, overrides, defines policy, nor trusts client values. |
| REQ-CHK-014 | Prior and current commercial context is compared; every material change or degraded state is explicit before continuation; unsupported context blocks progression without a prohibited commercial rule. |
| REQ-CHK-015 | Money and Currency are explicit and precision-safe; Currency mixing and conversion are rejected; ZAR and Pricing boundaries hold; no representation or rule is selected. |
| REQ-CHK-016 | Current Inventory supplies availability; Checkout calculates or mutates none of the listed Inventory truth, and every degraded outcome interrupts unsupported progression. |
| REQ-CHK-017 | Checkout requests and correlates reservation outcomes while Inventory retains every listed authority and no duration, timing, locking, or persistence choice is made. |
| REQ-CHK-018 | Every listed reservation hazard produces a distinct safe result; unknown effects are verified or reconciled before repetition, and no downstream or elapsed-time state becomes Inventory evidence. |
| REQ-CHK-019 | Checkout consumes governed choices and eligibility without determining or owning any listed Shipping or Fulfilment truth. |
| REQ-CHK-020 | Applicable rate evidence preserves Money, Currency, applicability, and freshness; stale evidence is revalidated; Checkout calculates or selects no prohibited value and resolves no authority disagreement. |
| REQ-CHK-021 | Before applicable irreversible or visible effects, every listed input is revalidated through its owning authority and none is recomputed by Checkout. |
| REQ-CHK-022 | Every listed invalid or uncertain revalidation condition blocks unsupported progression; permitted continuation communicates applicable material change without invented wording or policy. |
| REQ-CHK-023 | Submission correlates to current validated context and keeps request, processing acceptance, downstream effect, and outcome distinct; client activity proves no outcome. |
| REQ-CHK-024 | Repetition creates none of the listed duplicate effects; applicable Contracts preserve Idempotency Key semantics without a prohibited mechanism. |
| REQ-CHK-025 | Potentially irreversible repetition is evidence-classified first; replay, ordering, concurrency, and staleness cannot overwrite or multiply effects; uncertainty is verified or reconciled. |
| REQ-CHK-026 | Timeout, unavailability, delay, and partial completion remain distinct from success and failure; completed, unknown, and failed effects retain provenance and interruption proves no stoppage. |
| REQ-CHK-027 | Payment receives governed correlated commercial and actor context; Payment retains creation and processing authority; Checkout selects no provider or method, handles no raw card data, and establishes no Payment truth. |
| REQ-CHK-028 | Only authoritative Payment evidence contributes to orchestration; none of the listed client or inferred signals proves Payment, and Payment success proves neither Checkout success nor Order existence. |
| REQ-CHK-029 | Every listed Payment outcome remains distinguishable under Payment authority; retry is evidence-aware and duplicate-safe; uncertainty becomes neither success, failure, nor Order truth. |
| REQ-CHK-030 | Checkout supplies the governed correlated Order request context while establishing none of the listed Order authority, design, event, status, or future Requirements. |
| REQ-CHK-031 | Every listed Payment/Order mismatch remains explicit and recoverable or reconcilable, creates no false Checkout or Payment representation, proves no Order, and preserves owning authority. |
| REQ-CHK-032 | Every listed failure retains distinct provenance and owning authority without collapsing into fabricated generic truth. |
| REQ-CHK-033 | Each listed recovery scenario uses explicit, authorized where required, observable, correlated, duplicate-safe recovery and governed evidence, preserves history, records resolution or uncertainty, and rewrites no external truth. |
| REQ-CHK-034 | Checkout trusts none of the listed client-controlled facts; authoritative server-side validation and applicable replay/CSRF protection hold without a selected mechanism. |
| REQ-CHK-035 | Applicable protected data is minimized, purpose-limited, least-privileged, isolated, tamper-resistant, and safe across every listed surface; enumeration is resisted and ordinary data is not overclassified. |
| REQ-CHK-036 | Checkout may consume only governed fraud or review outcomes, establishes none of the prohibited fraud choices or truth, treats no signal as Payment evidence, and leaves recovery to policy. |
| REQ-CHK-037 | Checkout may carry governed commercial information but establishes none of the listed Tax, document, numbering, accounting, or legal policy and preserves qualified-governance dependency. |
| REQ-CHK-038 | Future Contracts preserve every listed semantic, safety, security, compatibility, and failure obligation while selecting none of the prohibited interface or implementation details. |
| REQ-CHK-039 | Events exist only for governed need and preserve every listed authority, correlation, safety, compatibility, privacy, ordering, and Projection boundary without prohibited event design. |
| REQ-CHK-040 | Applicable Checkout surfaces provide WCAG 2.2 AA, keyboard, focus, labeling, instruction, error, status, assistive-technology, unavailable-choice, and truthful-progress evidence without certification or design invention. |
| REQ-CHK-041 | Checkout is bounded, observable, correlated, and attributable across every listed condition without establishing a numerical target or profile. |
| REQ-CHK-042 | Every applicable listed high-Risk operation is authorized and proportionally audited; ordinary activity is not automatically high-Risk and no access, approval, or escalation policy is selected. |
| REQ-CHK-043 | Every representation derives from protected governed outcomes, identifies source where applicable, establishes or mutates no transaction truth, claims no premature success, and cannot change truth through failure; no provider or format is selected. |
| REQ-CHK-044 | Verification covers every listed applicable functional, boundary, resilience, security, quality, event, and Contract concern without choosing framework, IDs, tooling, or coverage target. |

## 28. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-CHK-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047–048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9, 19–21 | Checkout governance |
| REQ-CHK-002 | PRODUCT.md §§13–14, 16 | REQ-BUS-011–013 | REQ-CART-018; REQ-PRC-018; REQ-INV-023; REQ-PAY-026; REQ-SHP-015 | ARCHITECTURE.md §§9, 25.3 | Checkout |
| REQ-CHK-003 | PRODUCT.md §§13, 16 | REQ-BUS-011–030, 032, 044 | REQ-PRD-039; REQ-CUS-033–036; REQ-INV-023–026; REQ-CART-018–021; REQ-PRC-018–021; REQ-PAY-002, 026; REQ-SHP-002, 015–016 | ARCHITECTURE.md §§9–10 | Cross-domain |
| REQ-CHK-004 | PRODUCT.md §§5.4–5.6, 14.4–14.6, 17 | REQ-BUS-011–013, 021, 035 | REQ-CART-018–020; REQ-PAY-004, 026 | API.md §§22–24, 43 | Payment, Order, operations |
| REQ-CHK-005 | PRODUCT.md §§5.4–5.6, 14.4, 17.1–17.2 | REQ-BUS-011–013, 042 | REQ-CART-018; REQ-PRC-018; REQ-INV-009; REQ-SHP-005 | GLOSSARY.md §§5, 41 | Customer journey |
| REQ-CHK-006 | PRODUCT.md §§14.3–14.4, 16 | REQ-BUS-009–013 | REQ-CART-018–019 | ARCHITECTURE.md §§9, 25.3 | Cart, Checkout |
| REQ-CHK-007 | PRODUCT.md §§14.3–14.4, 16.1–16.2 | REQ-BUS-009–013, 042 | REQ-CART-016–018, 023 | API.md §§42–43 | Cart, recovery |
| REQ-CHK-008 | PRODUCT.md §§13, 14.2–14.4, 16.1–16.2 | REQ-BUS-005–006, 012 | REQ-PRD-021–022, 039, 046–048; REQ-CAT-033–034 | ARCHITECTURE.md §9 | Product, Category |
| REQ-CHK-009 | PRODUCT.md §§5.4, 14.2–14.4, 17.2 | REQ-BUS-005–006, 012, 042 | REQ-PRD-021–022, 037, 039 | API.md §43 | Product, recovery |
| REQ-CHK-010 | PRODUCT.md §§7, 12.2–12.3, 14.4, 24 | REQ-BUS-007, 011 | REQ-CUS-001–005, 033 | SECURITY-STANDARDS.md §§10–12 | Customer, Identity |
| REQ-CHK-011 | PRODUCT.md §§7, 14.4, 16.4, 16.7 | REQ-BUS-008, 011, 022 | REQ-CUS-015–017, 036; REQ-SHP-009–010 | SECURITY-STANDARDS.md §35 | Customer, Shipping, Order |
| REQ-CHK-012 | PRODUCT.md §§5.5, 7, 16.7 | REQ-BUS-032–033 | REQ-CUS-004–005, 039; REQ-CART-024; REQ-PAY-030; REQ-SHP-034 | SECURITY-STANDARDS.md §§10–12 | Identity, security |
| REQ-CHK-013 | PRODUCT.md §§14.4, 16.1, 16.6 | REQ-BUS-012, 014–016 | REQ-CART-016–018; REQ-PRC-002, 009–014, 018 | API.md §§42–43 | Pricing |
| REQ-CHK-014 | PRODUCT.md §§5.4, 14.4, 16.1, 30.4 | REQ-BUS-012, 016, 042 | REQ-CART-017–018; REQ-PRC-018 | UI.md §§26, 50 | Customer journey |
| REQ-CHK-015 | PRODUCT.md §§6, 16.1, 20 | REQ-BUS-014–015 | REQ-PRC-006; REQ-PAY-009; REQ-SHP-008 | GLOSSARY.md §§5, 35; DATABASE.md §24 | Pricing, Payment |
| REQ-CHK-016 | PRODUCT.md §§14.4, 16.2, 23 | REQ-BUS-012, 017–019 | REQ-INV-005–009, 023; REQ-CART-018 | API.md §§41, 43 | Inventory |
| REQ-CHK-017 | PRODUCT.md §§16.2, 17, 24 | REQ-BUS-019, 048 | REQ-INV-010–015, 023 | DATABASE.md §§19, 50 | Inventory, Order |
| REQ-CHK-018 | PRODUCT.md §§5.6, 16.2, 17.2, 23 | REQ-BUS-013, 019, 036, 042 | REQ-INV-013–015, 023 | DATABASE.md §§50–52 | Inventory recovery |
| REQ-CHK-019 | PRODUCT.md §§14.4, 16.5 | REQ-BUS-011–012, 029 | REQ-SHP-004–005, 015; REQ-CART-021 | GLOSSARY.md §11 | Shipping |
| REQ-CHK-020 | PRODUCT.md §§14.4, 16.1, 16.5–16.6 | REQ-BUS-012, 014–016, 029 | REQ-PRC-014, 018; REQ-SHP-006–008, 015 | API.md §§42–43 | Shipping, Pricing |
| REQ-CHK-021 | PRODUCT.md §§14.4, 16.1–16.5, 23 | REQ-BUS-011–013, 015, 018–019, 024 | REQ-PRD-021–022; REQ-CUS-033; REQ-INV-009, 023; REQ-CART-018; REQ-PRC-018; REQ-PAY-006; REQ-SHP-015 | ARCHITECTURE.md §25.3; API.md §43 | All purchase Domains |
| REQ-CHK-022 | PRODUCT.md §§5.4–5.6, 14.4, 30.4 | REQ-BUS-012, 042 | REQ-PRC-018; REQ-INV-009; REQ-SHP-005, 007 | UI.md §§26, 50; ACCESSIBILITY.md | Customer journey |
| REQ-CHK-023 | PRODUCT.md §§5.4–5.6, 14.4, 30.4 | REQ-BUS-013, 025, 042 | REQ-CART-022–023; REQ-PAY-007, 021 | API.md §§22, 24, 58 | Payment, Order |
| REQ-CHK-024 | PRODUCT.md §§5.6, 16.2–16.3, 23 | REQ-BUS-013, 019, 026, 036 | REQ-INV-014; REQ-CART-022–023; REQ-PAY-022–023; REQ-SHP-027–028 | API.md §§22, 24; EVENTS.md | All effect-owning Domains |
| REQ-CHK-025 | PRODUCT.md §§5.6, 17.2, 23 | REQ-BUS-013, 025–026, 036, 042 | REQ-INV-014–015; REQ-CART-022–023; REQ-PAY-023; REQ-SHP-028 | DATABASE.md §§51–52; API.md §24 | Recovery |
| REQ-CHK-026 | PRODUCT.md §§5.4–5.6, 17.2, 23 | REQ-BUS-013, 025, 042, 045–046 | REQ-PAY-021, 024; REQ-SHP-026 | API.md §58; EVENTS.md §§37–39 | Operations, support |
| REQ-CHK-027 | PRODUCT.md §§14.4–14.5, 16.1–16.3 | REQ-BUS-011–015, 024 | REQ-PAY-006–007, 026; REQ-PRC-018 | SECURITY-STANDARDS.md; API.md §43 | Payment |
| REQ-CHK-028 | PRODUCT.md §§5.4, 14.5–14.6, 16.3 | REQ-BUS-021, 024–025 | REQ-PAY-013, 019, 026 | ARCHITECTURE.md §20.2; API.md §58 | Payment, Order |
| REQ-CHK-029 | PRODUCT.md §§5.4–5.6, 14.5, 16.3, 17.2 | REQ-BUS-013, 024–026, 042 | REQ-PAY-019–025 | API.md §58 | Payment recovery |
| REQ-CHK-030 | PRODUCT.md §§14.4–14.6, 16.4 | REQ-BUS-021–023 | REQ-CUS-034; REQ-INV-024; REQ-CART-019; REQ-PRC-019; REQ-PAY-027 | ARCHITECTURE.md §§9, 25.3 | Order |
| REQ-CHK-031 | PRODUCT.md §§5.4–5.6, 14.5–14.6, 17.2 | REQ-BUS-013, 021–026, 035–036, 042 | REQ-PAY-019, 027–029 | ARCHITECTURE.md §20.5; EVENTS.md §39 | Order, Payment, operations |
| REQ-CHK-032 | PRODUCT.md §§5.4–5.6, 8.4, 17.2 | REQ-BUS-013, 042, 045–046 | REQ-CART-023; REQ-PRC-018; REQ-INV-036; REQ-PAY-021; REQ-SHP-025 | API.md §§52–58 | Customer, support |
| REQ-CHK-033 | PRODUCT.md §§5.5–5.6, 15.4–15.5, 31.4, 34 | REQ-BUS-033–036, 043, 045 | REQ-PAY-024–025; REQ-SHP-029–030 | SECURITY-STANDARDS.md §27; DATABASE.md §52 | Operations, support |
| REQ-CHK-034 | PRODUCT.md §§5.5, 16.7, 23 | REQ-BUS-032, 039, 042, 052 | REQ-CUS-039–040; REQ-CART-024–025; REQ-PAY-030–032 | SECURITY-STANDARDS.md §§10–14, 27 | All Checkout interfaces |
| REQ-CHK-035 | PRODUCT.md §§5.5, 16.7, 20, 34.5 | REQ-BUS-039–040, 042 | REQ-CUS-040; REQ-CART-025; REQ-PAY-032; REQ-SHP-035 | SECURITY-STANDARDS.md §§14, 27, 35; API.md §§52–53 | All Checkout interfaces |
| REQ-CHK-036 | PRODUCT.md §§5.5, 16.12, 18.6, 23–24 | REQ-BUS-048, 052 | REQ-CUS-042; REQ-PAY-033 | SECURITY-STANDARDS.md | Fraud, operations |
| REQ-CHK-037 | PRODUCT.md §§16.1, 16.10, 20, 24 | REQ-BUS-012, 014–016, 041, 054 | REQ-PRC-013–014, 019; REQ-PAY-044; REQ-SHP-042 | GLOSSARY.md §23 | Pricing, Order, Finance |
| REQ-CHK-038 | PRODUCT.md §§13, 22, 35 | REQ-BUS-011–013, 032, 039, 042, 046–047 | REQ-CART-031; REQ-PRC-033; REQ-PAY-039; REQ-SHP-037 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Contract consumers |
| REQ-CHK-039 | PRODUCT.md §§13, 17, 23, 33 | REQ-BUS-013, 039, 044–047 | REQ-CART-030; REQ-PRC-032; REQ-PAY-038; REQ-SHP-036 | ARCHITECTURE.md §14; EVENTS.md | Event consumers |
| REQ-CHK-040 | PRODUCT.md §§5.7, 14.4–14.5, 16.9, 30.4 | REQ-BUS-011, 037, 042 | REQ-CUS-043; REQ-CART-027; REQ-PRC-029; REQ-PAY-040; REQ-SHP-038 | ACCESSIBILITY.md; UI.md §§26, 40, 50 | Customer journey |
| REQ-CHK-041 | PRODUCT.md §§10, 18.4, 20, 23 | REQ-BUS-038, 043, 045–046 | REQ-CART-028; REQ-PRC-030; REQ-PAY-041; REQ-SHP-039 | PERFORMANCE.md §§46–49, 58–60, 83 | All Checkout consumers |
| REQ-CHK-042 | PRODUCT.md §§5.5, 15, 17.4, 31, 34 | REQ-BUS-033–036 | REQ-CUS-045; REQ-CART-029; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040 | SECURITY-STANDARDS.md §27; DATABASE.md §42 | Operations, security |
| REQ-CHK-043 | PRODUCT.md §§9.3, 18.3, 33–34 | REQ-BUS-043–044, 049, 053 | REQ-CUS-024–025; REQ-PAY-043; REQ-SHP-041 | EVENTS.md; SECURITY-STANDARDS.md | Notifications, reporting |
| REQ-CHK-044 | PRODUCT.md §§26.4, 35 | REQ-BUS-011–013, 021, 024–026, 032, 037–039, 042, 045–047, 052 | REQ-PRD-039; REQ-CUS-049; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043 | TESTING-STANDARDS.md; ACCESSIBILITY.md; PERFORMANCE.md | Verification evidence |

## 29. Open Product Decisions

`PRODUCT.md` currently contains exactly 30 Open Product Decisions. Eighteen are materially relevant to Checkout, and this Specification resolves none of them. All concrete values remain external until Approved by the owning governance.

| Product Decision | Checkout boundary affected |
| --- | --- |
| Guest checkout versus mandatory account rules | Determines which actor contexts may progress; this Draft selects neither guest nor mandatory Account behavior. |
| Customer email-verification requirements | Determines whether verification gates progression; no verification prerequisite is selected. |
| Initial payment methods and provider | Determines permitted Payment initiation choices and provider Contract; Checkout remains method- and provider-neutral. |
| Shipping provider, service levels, delivery areas, and fee policy | Determines future delivery choices and governed Shipping evidence; Checkout defines only coordination. |
| Free-delivery threshold and promotional treatment | Determines Pricing and Promotion treatment; Checkout does not define the threshold or calculation. |
| Tax-inclusive display and invoice requirements | Determines presentation and document obligations; Checkout preserves governed commercial outcomes only. |
| Stock Reservation duration | Determines external Inventory timing; Checkout does not select duration or expiry. |
| Back-order and pre-order support | Determines whether unavailable Product Variants may progress; Checkout consumes the governed outcome. |
| Voucher and promotion stacking policy | Determines authoritative Pricing combination behavior; Checkout does not choose stacking or precedence. |
| Low-stock and out-of-stock customer messaging | Determines future messaging; Checkout exposes governed Inventory outcomes without defining copy. |
| Customer-support channels and service expectations | Determines recovery and support presentation; no channel or service level is selected. |
| Marketing-consent and communication-preference model | Determines optional consent handling; Checkout does not infer marketing consent from purchase activity. |
| Initial analytics provider and event taxonomy | Determines future analytics integration; Checkout defines no provider or taxonomy. |
| Administrative role and permission matrix | Determines protected intervention assignments; Checkout requires contextual Authorization without defining the matrix. |
| Production customer-service and operational escalation process | Determines exceptional support and escalation workflow; Checkout defines only safe recovery boundaries. |
| South African tax-display, invoice, and credit-note policy | Determines production commercial-document policy; Checkout does not establish it. |
| Fraud-screening approach and manual-review workflow | Determines fraud restriction and review behavior; Checkout does not own fraud policy. |
| Gift cards, store credit, and promotional credit policy | Determines future Pricing and Payment treatment; Checkout defines no credit behavior. |

## 30. Risks

| Risk | Implementation-neutral control direction |
| --- | --- |
| Orchestration becomes competing authority | Consume owning-Domain outcomes without recalculating or rewriting them. |
| Stale Product or Product Variant fact | Revalidate current Product-owned truth before commitment. |
| Stale or incorrect Pricing | Request authoritative Pricing recalculation and expose material change. |
| Stale or unavailable Inventory | Require current Inventory outcomes and stop unsupported progression. |
| Stock Reservation uncertainty | Correlate, verify, and reconcile without selecting lifecycle timing. |
| Stale Shipping Rate or eligibility | Revalidate Shipping evidence and keep Pricing charge treatment distinct. |
| Invalid Customer or Address context | Use governed context and fail safely without rewriting authority. |
| Client tampering | Reject client-controlled commercial, Inventory, Shipping, Payment, and Authorization truth. |
| Duplicate Checkout submission | Prevent repeated downstream commercial effects. |
| Duplicate Payment or financial effect | Preserve Payment authority, correlation, and evidence-aware retry. |
| Duplicate Order | Require a governed Order handoff and verify uncertain creation before repetition. |
| Duplicate Inventory or Shipping effect | Coordinate idempotently where required and verify unknown effects. |
| Unsafe retry or replay | Classify prior effects and reject conflicting or unauthorized repetition. |
| Concurrent or reordered activity | Prevent stale responses or concurrent submissions from overwriting accepted truth. |
| Payment uncertainty | Preserve authoritative Payment evidence and avoid false success or failure. |
| Payment success with Order failure | Keep the mismatch explicit, recoverable, and reconcilable. |
| Dependency timeout or partial completion | Preserve failure provenance and known versus unknown effects. |
| Sensitive Data leakage | Minimize and protect data across interfaces, evidence, logs, and representations. |
| Protected Resource disclosure | Enforce Resource isolation and enumeration-resistant failures. |
| Unresolved Product Decision embedded locally | Keep concrete policy external and implementation reversible. |
| Inaccessible or misleading Checkout | Require applicable WCAG 2.2 AA and truthful status communication. |
| Observability or reconciliation gap | Preserve correlation, attribution, history, and explicit unresolved outcomes. |

## 31. Related Documents

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

## 32. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-04 | Draft | Initial comprehensive Checkout Domain Specification. |
| 1.0.0 | 2026-09-04 | Approved | Promoted the Checkout Domain Specification to its Approved normative baseline without changing substantive Domain behavior or authority boundaries. |

## 33. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `CHK`, Approved Requirements are normative only within the Checkout Domain scope, governing-source precedence and Approved upstream Domain authority are preserved, Open Product Decisions remain unresolved, and Checkout has no repository-wide authority;
2. Checkout is a purchase-orchestration authority and coordination transfers no owning-Domain truth;
3. Product, Category, Customer, Identity, Cart, Pricing, Inventory, Shipping, Payment, Order, Return, Administration, Notifications, and Reporting boundaries remain intact;
4. Requirement identifiers are unique, sequential, stable, gap-free, complete, testable, and implementation-neutral;
5. Cart consumption, authoritative revalidation, submission, Payment initiation, Order handoff, failure, uncertainty, duplicate, retry, replay, concurrency, recovery, and reconciliation are complete;
6. every Requirement has exactly one clause-complete Acceptance Criteria row;
7. every Requirement has exactly one semantically supported traceability row with no fabricated future Domain reference;
8. all 30 current Open Product Decisions were reviewed, all 18 materially Checkout-relevant decisions are represented in PRODUCT.md order, and none is resolved;
9. canonical terminology is correct, ordinary descriptive Checkout phrases create no Checkout Session or state model, and no Glossary amendment is required unless direct evidence proves otherwise;
10. Risks are material, non-duplicative, and paired with implementation-neutral controls;
11. every Related Document exists and is relevant, with no self-reference or nonexistent future Domain Specification;
12. Revision History contains exactly the preserved `0.1.0 Draft` row and the `1.0.0 Approved` promotion row;
13. structure, Markdown, whitespace, final newline, accessibility, performance, security, events, Contracts, audit, testing, and implementation-neutrality are valid; and
14. the final Git scope contains only lifecycle-promotion changes to `specifications/domains/checkout/checkout-domain.md`, with nothing staged or otherwise modified.
