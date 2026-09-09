---
title: Cart and Purchase Specification
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-09
authoritative: false
---

# Cart and Purchase Specification

## 1. Purpose

This Specification defines implementation-neutral requirements for customer-facing Cart and purchase presentation, intent, progression, uncertainty, recovery, and confirmation boundaries.

This document uses scope code `FCP`. This Specification is Approved, and its Requirements are normative only within Cart and Purchase frontend scope and are not repository-wide authority. It remains subordinate to governing sources, Product and Business Requirements, Approved Domain Specifications, and applicable Approved FEB, FSC, FCD, FPE, and FCA Requirements, and resolves no Open Product Decision.

## 2. Scope and Authority

FCP owns only customer-facing presentation and interaction behavior for governed Cart evidence and intent, Checkout entry and progression, purchase inputs and summaries, Payment handoff and outcomes, and authoritative purchase-confirmation presentation.

FCP does not own Product, Product Variant, Price, Money, Currency, Discount, Promotion, Voucher, Tax, Stock, Available-to-Sell, availability, Stock Reservation, Customer, Identity, Session, Authorization, Cart, Checkout, Payment, Order, Shipping, Return, Refund, or other Domain truth. Presentation, submission, navigation, redirect, provider display, local or optimistic state, timeout, Notification, analytics, or stopped observation cannot transfer that authority to FCP.

Shared behavior remains owned by FEB; shell by FSC; discovery by FCD; evaluation by FPE; account and identity presentation by FCA; post-purchase by FPP; Administration by FAD; and Reporting by FRP.

## 3. Terminology and Cart / Purchase Model

Canonical terms retain their `GLOSSARY.md` meanings. Cart, Cart Item, Product Variant, Price, Money, Currency, Stock, Available-to-Sell, Stock Reservation, Checkout, Payment, Order, Customer, Visitor, Principal, Session, Authentication, Authorization, Address, Shipment, Consent, Contract, Analytics Event, Domain Event, Integration Event, and Audit Record remain distinct.

For FCP only, **purchase presentation context** describes non-authoritative frontend state used to present governed Cart, Checkout, Payment, and Order evidence. It is not a canonical entity or authoritative purchase lifecycle.

## 4. Requirements

### REQ-FCP-001 — Lifecycle, Authority, and Scope

FCP MUST govern only Cart and Purchase frontend behavior under scope `FCP`, preserve governing-source precedence, applicable frontend inheritance and Approved Domain authority, and MUST treat this Approved Specification as normative only within Cart and Purchase frontend scope and not as repository-wide authority.

### REQ-FCP-002 — Frontend Inheritance

FCP MUST consume every materially applicable Approved FEB, FSC, FCD, FPE, and FCA Requirement without copying, weakening, conflicting with, or transferring ownership of inherited behavior.

### REQ-FCP-003 — Domain and Journey Non-Authority

FCP MUST NOT own or redefine source-Domain truth or FSC, FCD, FPE, FCA, FPP, FAD, or FRP behavior; presentation, entry, summary, or handoff MUST NOT transfer that authority.

### REQ-FCP-004 — Cart Identity and Association

FCP MUST present governed Cart identity and applicable Visitor, Customer, Session, Currency, and market association without establishing or changing those relationships locally.

### REQ-FCP-005 — Cart Item Membership

FCP MUST present Cart Item identity, Cart membership, Product Variant reference, quantity, and governed state without treating display order, duplication, or client keys as Cart truth.

### REQ-FCP-006 — Product Variant Evidence

FCP MUST preserve governed Product Variant identity and association while Product retains publication, visibility, structural sellability, content, and Product Variant truth.

### REQ-FCP-007 — Cart Mutation Intent

FCP MUST submit add, update, remove, and clear actions only as Cart mutation intent and MUST NOT represent intent, pending work, or local change as accepted Cart state.

### REQ-FCP-008 — Quantity Intent and Constraints

Quantity presentation MUST preserve applicable governed constraints and validation context without defining quantity policy, creating Stock Reservation, or authorizing purchase.

### REQ-FCP-009 — Accepted Cart State

FCP MUST present a Cart mutation as accepted only from correlated Cart-owned evidence and MUST preserve rejected, failed, denied, conflicting, and uncertain outcomes distinctly.

### REQ-FCP-010 — Duplicate Cart Mutation

Repeated or duplicate Cart interaction MUST preserve the intended Cart effect and expose governed outcomes without FCP defining backend idempotency or merge rules.

### REQ-FCP-011 — Concurrent Cart Mutation

Concurrent, reordered, or conflicting Cart mutations MUST preserve accepted Cart state, identify conflict or uncertainty, and prevent stale completion from silently overwriting newer intent.

### REQ-FCP-012 — Cart Persistence and Merge Neutrality

FCP MUST NOT select guest persistence, authenticated persistence, expiry, cross-device synchronization, sign-in merge, conflict resolution, or Cart restoration policy.

### REQ-FCP-013 — Price, Money, and Currency Evidence

FCP MUST present only governed Pricing evidence with Price, Money, Currency, applicability, source, and freshness intact and MUST NOT calculate or establish authoritative commercial values.

### REQ-FCP-014 — Discount, Promotion, and Voucher Evidence

Discount, Promotion, Voucher, comparison Price, savings, eligibility, and combination presentation MUST be supported by governed Pricing outcomes and MUST NOT imply unsupported benefit, stacking, or urgency.

### REQ-FCP-015 — Tax Evidence

FCP MUST present Tax evidence only where governed, preserving Money, Currency, jurisdiction and inclusion context without selecting tax display, invoice, Credit Note, or compliance policy.

### REQ-FCP-016 — Commercial Change Safety

Material Price, Discount, Promotion, Voucher, Tax, charge, or total change MUST be distinguishable, explained from governed evidence, and accepted where required before purchase progression continues.

### REQ-FCP-017 — Inventory and Availability Evidence

FCP MUST present current governed Inventory and availability evidence with Product Variant association, applicability, freshness, and uncertainty while Inventory retains Stock and Available-to-Sell truth.

### REQ-FCP-018 — Stock Reservation Boundary

Cart presence, quantity, availability display, Checkout entry, pending state, or local hold language MUST NOT establish Stock Reservation; only governed Inventory evidence may establish its existence and state.

### REQ-FCP-019 — Availability Change Safety

Stale, unknown, insufficient, unavailable, or conflicting Inventory evidence MUST produce truthful outcomes and revalidation or permitted recovery without promising Stock or preventing overselling locally.

### REQ-FCP-020 — Cart Validation and Reconciliation Presentation

FCP MUST present Cart-owned validation, correction, recovery, and reconciliation outcomes without locally rewriting Cart, Product, Pricing, Inventory, Customer, or Checkout truth.

### REQ-FCP-021 — Checkout Entry

FCP MUST enter Checkout only through governed Cart handoff and Checkout eligibility evidence and MUST NOT infer readiness from Cart presence, client validation, navigation, or control activation.

### REQ-FCP-022 — Visitor, Customer, and Account Context

FCP MUST preserve governed Visitor or Customer context and applicable FCA evidence without making registration, Account, Authentication, or Customer status a purchase precondition unless Approved policy requires it.

### REQ-FCP-023 — Session and Authorization Context

Every protected Cart and purchase read or action MUST preserve current server-side contextual Authorization across Principal, Session, Customer or Visitor association, Resource, action, property, and Domain state.

### REQ-FCP-024 — Address Evidence and Selection Intent

FCP MUST present governed Address identity, ownership, applicability, validation, freshness, and selection intent without changing Customer Address truth or historical commercial snapshots.

### REQ-FCP-025 — Delivery Choice Presentation

FCP MUST present only governed delivery choices, applicability, service, area, timing, charge, Currency, freshness, and uncertainty without defining Shipping policy or commitment.

### REQ-FCP-026 — Shipping Charge Separation

FCP MUST preserve the distinction between Shipping-owned quotation evidence and Pricing-owned commercial totals and MUST NOT derive one as authoritative from the other.

### REQ-FCP-027 — Checkout Progression

FCP MUST present Checkout-owned eligibility, validation, progression, rejection, regression, and uncertainty without defining a universal Checkout transition graph or treating navigation as progression truth.

### REQ-FCP-028 — Coordinated Revalidation

Before governed purchase submission, FCP MUST present coordinated Checkout revalidation of material Cart, Product Variant, Pricing, Inventory, Customer or Visitor, Address, delivery, and Shipping evidence.

### REQ-FCP-029 — Revalidation Disagreement

Conflicting, stale, changed, incomplete, or unavailable revalidation evidence MUST block unsupported progression and produce truthful explanation and permitted recovery without FCP resolving source-Domain disagreement.

### REQ-FCP-030 — Pre-Purchase Order Summary

FCP MUST present a pre-purchase summary from current governed evidence and MUST NOT represent that summary, Checkout state, or an identifier as an Order, Order Snapshot, invoice, or purchase confirmation.

### REQ-FCP-031 — Purchase Submission

FCP MUST submit purchase intent only through governed Checkout behavior with correlated context and MUST NOT treat submission, local acceptance, navigation, or stopped observation as a commercial effect.

### REQ-FCP-032 — Duplicate Purchase Submission

Repeated activation, retry, replay, refresh, navigation, or restored client state MUST NOT cause FCP to claim or intentionally request duplicate commercial effect without governed outcome and recovery evidence.

### REQ-FCP-033 — Payment Initiation Boundary

FCP MUST present Payment initiation only from governed Checkout and Payment evidence and MUST NOT create Payment identity, select provider or method policy, or infer initiation from redirect or provider presentation.

### REQ-FCP-034 — Payment Outcome Presentation

FCP MUST preserve Payment-owned identity, Money, Currency, attempt, status, failure, denial, cancellation, and uncertainty evidence and MUST NOT establish Payment success or financial effect locally.

### REQ-FCP-035 — Provider-Return Non-Authority

A provider return, redirect, browser state, client callback, message, or displayed receipt MUST NOT prove Payment success, Order creation, or purchase completion without authoritative governed evidence.

### REQ-FCP-036 — Unknown Payment Effect

Timeout, interruption, unverifiable evidence, partial completion, or lost observation after purchase submission MUST remain uncertain until governed Payment and Checkout recovery or reconciliation evidence resolves the effect.

### REQ-FCP-037 — Order-Creation Boundary

FCP MUST treat Order creation as Order-owned truth and MUST NOT create or infer an Order from Cart, Checkout, Payment, provider, Notification, analytics, or frontend evidence.

### REQ-FCP-038 — Purchase Confirmation

FCP MUST present purchase confirmation only from authoritative correlated Order-creation evidence and MUST keep Payment success, Order creation, communication delivery, and presentation success distinct.

### REQ-FCP-039 — Post-Purchase Handoff

After authoritative Order creation, FCP MAY provide safe context to FPP but MUST NOT own Order history, Shipment, fulfilment, cancellation, Return, Refund, or other post-purchase behavior.

### REQ-FCP-040 — Presentation States

Each material FCP region MUST apply distinguishable initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states where applicable without defining a mandatory lifecycle traversal.

### REQ-FCP-041 — Freshness and Supersession

Stale, cancelled, reordered, conflicting, or superseded Cart, Pricing, Inventory, Checkout, Shipping, Payment, or Order outcomes MUST NOT overwrite newer relevant intent or presentation state.

### REQ-FCP-042 — Truthful Failure and Recovery

FCP MUST preserve failure provenance, known context, safe next actions, and governed recovery without fabricating success or defining backend retry, replay, reconciliation, or recovery mechanisms.

### REQ-FCP-043 — Optimistic Presentation Boundary

Optimistic presentation MAY apply only to reversible Cart intent, MUST remain visibly provisional and correctable, and MUST NOT establish Price, Inventory, Stock Reservation, Checkout, Payment, Order, or purchase-confirmation truth.

### REQ-FCP-044 — Context and Resource Isolation

FCP state MUST remain isolated across applicable Visitor, Customer, Account, Identity, Principal, Session, Cart, Checkout, Payment, Order, and Resource contexts and MUST be cleared, segregated, or revalidated when governing context changes.

### REQ-FCP-045 — Sensitive and Payment Data

FCP MUST minimize Sensitive Data and Payment data across content, client state, routes, errors, logs, telemetry, Analytics Events, fixtures, and screenshots and MUST expose no Secrets, credentials, raw payment instrument data, or unnecessary personal information.

### REQ-FCP-046 — Safe Rendering and Resource Concealment

FCP MUST safely render governed and provider-derived evidence and resist injection, deception, enumeration, timing disclosure, unsafe navigation, and exposure of inaccessible Cart, Checkout, Payment, Order, Customer, or other Resource existence.

### REQ-FCP-047 — Responsive and Content-Resilient Outcomes

Cart lines, quantities, totals, validation, delivery choices, Checkout progression, Payment outcomes, and confirmation MUST remain understandable and operable across supported viewport, reflow, zoom, orientation, localization, and content extremes without fixed breakpoints or layouts.

### REQ-FCP-048 — Input-Modality and Structural Accessibility

FCP interactions MUST provide equivalent keyboard, pointer, touch, and assistive-technology operation with coherent structure, names, relationships, validation, errors, Price, Currency, availability, and selection meaning.

### REQ-FCP-049 — Focus and Dynamic Status

Cart changes, commercial changes, validation, Checkout progression, Payment outcomes, failure, recovery, and confirmation MUST preserve predictable focus and expose understandable status and change without relying only on color, position, imagery, motion, or visual updates.

### REQ-FCP-050 — Bounded Frontend Work

Cart, line, summary, validation, commercial, availability, reactive, and background work MUST remain bounded by governed inputs and performance evidence without FCP defining numerical limits, payload sizes, timing budgets, or client mechanisms.

### REQ-FCP-051 — Degradation and Failure Containment

Failure of optional content, analytics, communication, or another non-essential region MUST NOT silently block unrelated critical Cart, Checkout, Payment-outcome, confirmation, or recovery presentation and MUST remain truthful about completeness and uncertainty.

### REQ-FCP-052 — Telemetry and Evidence Separation

Material Cart, Checkout, Payment, Order, security, recovery, and Contract failures MUST produce safe frontend operational telemetry and governed correlation evidence while telemetry, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinct and non-authoritative.

### REQ-FCP-053 — Analytics Boundary

FCP Analytics Events MUST remain minimized, Consent-aware where applicable, and non-authoritative and MUST NOT define provider, taxonomy, event name, payload, identity model, audience, attribution, or commercial truth.

### REQ-FCP-054 — Feature-Flag Safety

Every reachable feature-flag state MUST preserve authority, evidence, accessibility, security, privacy, and inherited obligations without selecting rollout policy or implementation.

### REQ-FCP-055 — Abstract Contract Boundary

Future FCP Contracts MUST expose only necessary bounded and versioned identity, association, quantity, Visitor or Customer context, Price, Money, Currency, availability, Stock Reservation, Address, delivery, Checkout eligibility and progression, Payment, Order, freshness, outcome, failure, uncertainty, recovery, and correlation semantics and MUST define no route, endpoint, method, operation, DTO, schema, wire format, status mapping, event payload, persistence, transport, or provider.

### REQ-FCP-056 — Policy and Implementation Neutrality

FCP MUST keep unresolved commercial, purchase, delivery, payment, tax, reservation, support, fraud, analytics, release, provider, protocol, route, visual, numerical, and implementation choices outside normative behavior.

### REQ-FCP-057 — Verification Coverage

FCP MUST provide traceable verification across authority and inheritance boundaries, Cart identity and mutations, Pricing and commercial evidence, Inventory and Stock Reservation boundaries, Checkout entry and progression, Payment initiation and outcomes, Order creation and purchase confirmation, freshness and uncertainty, failure and recovery, accessibility and responsiveness, security and privacy, bounded work and degradation, telemetry and evidence separation, analytics, feature-flag safety, abstract Contracts, and policy and implementation neutrality.

## 5. Acceptance Criteria

| Requirement | Acceptance Criterion |
| --- | --- |
| REQ-FCP-001 | Lifecycle evidence confirms `1.0.0 Approved`, `authoritative: false`, scope `FCP`, normativity only within Cart and Purchase frontend scope, no repository-wide authority, governing precedence, inherited frontend obligations, and preserved Product, Business Requirement, and Approved Domain authority. |
| REQ-FCP-002 | Applicable inherited obligations are mapped individually and none is copied, weakened, contradicted, or ownership-transferred. |
| REQ-FCP-003 | Boundary review finds no FCP-owned source truth or detailed neighboring journey behavior and no authority transfer through presentation or handoff. |
| REQ-FCP-004 | Cart presentation retains governed identity and each applicable association, while manipulated local context changes none of them. |
| REQ-FCP-005 | Cart Items retain identity, membership, Product Variant reference, quantity, and governed state independent of visual order or client keys. |
| REQ-FCP-006 | Product Variant evidence retains governed association and FCP establishes none of the prohibited Product truths. |
| REQ-FCP-007 | Add, update, remove, and clear interactions remain intent until correlated Cart evidence confirms acceptance. |
| REQ-FCP-008 | Quantity controls expose governed constraints and validation while creating no policy, reservation, or purchase authority. |
| REQ-FCP-009 | Accepted, rejected, failed, denied, conflicting, and uncertain Cart outcomes are independently observable and only Cart evidence confirms acceptance. |
| REQ-FCP-010 | Duplicate interaction tests preserve the governed intended effect without embedding an idempotency or merge mechanism. |
| REQ-FCP-011 | Reordered concurrent outcomes preserve the newest relevant intent and accepted Cart state while exposing conflict and uncertainty. |
| REQ-FCP-012 | Configuration review finds no selected persistence, expiry, synchronization, merge, restoration, or conflict-resolution policy. |
| REQ-FCP-013 | Every commercial value retains governed Price, Money, Currency, source, applicability, and freshness, with no client calculation treated as authoritative. |
| REQ-FCP-014 | Promotional presentation is backed by governed Pricing evidence and contains no unsupported eligibility, stacking, benefit, savings, or urgency claim. |
| REQ-FCP-015 | Tax presentation retains governed amount, Currency, jurisdiction, and inclusion meaning while selecting no tax or document policy. |
| REQ-FCP-016 | Each material commercial change is distinguishable and explained, and progression requiring acceptance cannot continue without it. |
| REQ-FCP-017 | Availability evidence retains Product Variant association, applicability, freshness, and uncertainty and creates no Stock truth. |
| REQ-FCP-018 | Negative tests prove that no Cart, quantity, availability, Checkout, pending, or local-hold presentation establishes Stock Reservation. |
| REQ-FCP-019 | Unknown, stale, insufficient, unavailable, and conflicting Inventory outcomes remain truthful and recoverable without a local Stock promise. |
| REQ-FCP-020 | Validation and reconciliation presentation reflects owning-Domain outcomes and performs no local authoritative rewrite. |
| REQ-FCP-021 | Checkout entry requires governed Cart handoff and eligibility evidence; Cart presence, navigation, and local validation cannot establish readiness. |
| REQ-FCP-022 | Visitor and Customer paths preserve governed context without imposing an unapproved registration or Authentication prerequisite. |
| REQ-FCP-023 | Every protected Cart or purchase read and action is tested with current server-side contextual Authorization across Principal, Session, Customer or Visitor association, Resource, action, property, and Domain state, and manipulating frontend context, identifiers, labels, Claims, Permissions, Scope, or Session evidence grants no protected access. |
| REQ-FCP-024 | Address presentation retains identity, ownership, applicability, validation, freshness, and intent while Customer truth and historical snapshots remain unchanged. |
| REQ-FCP-025 | Delivery choices retain governed applicability, service, area, timing, charge, Currency, freshness, and uncertainty without becoming policy or commitment. |
| REQ-FCP-026 | Shipping quotation and Pricing totals remain separately sourced and neither is derived as authoritative from the other. |
| REQ-FCP-027 | Checkout states reflect governed eligibility and progression outcomes without route-based inference or a universal transition graph. |
| REQ-FCP-028 | Purchase submission is unavailable until current coordinated evidence exists for every applicable material input. |
| REQ-FCP-029 | Revalidation disagreements block unsupported progression and expose source-specific explanation and safe permitted recovery. |
| REQ-FCP-030 | The pre-purchase summary remains distinguishable from an Order, Order Snapshot, invoice, and confirmation under every state. |
| REQ-FCP-031 | Submission carries correlated governed intent and no local or navigational observation creates a commercial effect. |
| REQ-FCP-032 | Repeated activation, retry, replay, refresh, navigation, and restoration produce no FCP claim or intentional request for duplicate effect without governed evidence. |
| REQ-FCP-033 | Payment initiation presentation requires Checkout and Payment evidence and selects no identity, provider, method, or redirect inference. |
| REQ-FCP-034 | Payment identity, amount, Currency, attempt, status, and every named outcome retain Payment ownership and cannot be established locally. |
| REQ-FCP-035 | Provider-return tests demonstrate that redirects, browser state, callbacks, messages, and receipts establish neither Payment success, Order creation, nor purchase completion without authoritative governed evidence. |
| REQ-FCP-036 | Interrupted and unknown-effect scenarios remain uncertain until correlated governed recovery or reconciliation evidence resolves them. |
| REQ-FCP-037 | No Cart, Checkout, Payment, provider, Notification, analytics, or frontend evidence creates or implies an Order. |
| REQ-FCP-038 | Confirmation appears only with authoritative correlated Order creation and remains distinct from Payment, communication, and rendering outcomes. |
| REQ-FCP-039 | Post-confirmation handoff carries only permitted context and FCP performs none of the named post-purchase behaviors. |
| REQ-FCP-040 | Every applicable presentation state is independently produced without enforcing a universal order or business lifecycle. |
| REQ-FCP-041 | Reordered completion tests prove stale, cancelled, conflicting, and superseded outcomes cannot replace newer intent or state. |
| REQ-FCP-042 | Failures preserve provenance, context, safe next actions, and governed recovery without fabricated success or backend-mechanism definition. |
| REQ-FCP-043 | Optimistic Cart presentation is visibly provisional and correctable, and tests prove it establishes none of the prohibited truths. |
| REQ-FCP-044 | Context changes cannot expose or retain state belonging to another Visitor, Customer, Account, Identity, Principal, Session, Cart, Checkout, Payment, Order, or Resource. |
| REQ-FCP-045 | Inspection finds no unnecessary Sensitive Data, Payment data, Secret, credential, instrument data, or personal information in any named surface. |
| REQ-FCP-046 | Governed and provider-derived evidence renders safely, and adversarial tests prevent injection, deception, enumeration, timing disclosure, unsafe navigation, and protected-Resource disclosure. |
| REQ-FCP-047 | Representative viewport, reflow, zoom, orientation, localization, and content extremes preserve operable Cart and purchase outcomes. |
| REQ-FCP-048 | Every material interaction completes equivalent keyboard, pointer, touch, and assistive-technology paths with coherent semantic meaning. |
| REQ-FCP-049 | Dynamic changes preserve predictable focus and expose associated errors, validation, status, and meaning non-visually. |
| REQ-FCP-050 | Large governed inputs demonstrate bounded line, summary, validation, reactive, and background work without local numerical limits. |
| REQ-FCP-051 | Independent optional-region failures preserve critical Cart, Checkout, Payment-outcome, confirmation, and recovery presentation truthfully. |
| REQ-FCP-052 | Safe telemetry and correlation are produced for material failures, and evidence classification proves no type substitutes for another or creates Domain history. |
| REQ-FCP-053 | Analytics remains minimized, Consent-aware, and non-authoritative with no selected provider, taxonomy, payload, identity, audience, attribution, or commercial truth. |
| REQ-FCP-054 | Every reachable feature-flag state is exercised and observable evidence confirms preserved authority, evidence semantics, accessibility, security, privacy, and inherited obligations, with no state introducing locally selected rollout policy or implementation behavior. |
| REQ-FCP-055 | Contract review verifies, where applicable, bounded and versioned identity, association, quantity, Visitor or Customer context, Price, Money, Currency, availability, Stock Reservation, Address, delivery, Checkout eligibility and progression, Payment, Order, freshness, outcome, failure, uncertainty, recovery, and correlation semantics, and finds no route, endpoint, method, operation, DTO, schema, wire format, status mapping, event payload, persistence, transport, or provider design. |
| REQ-FCP-056 | Decision review confirms every named commercial, purchase, delivery, payment, tax, reservation, support, fraud, analytics, release, and technical choice remains unresolved or governed elsewhere. |
| REQ-FCP-057 | Verification evidence independently demonstrates each enumerated FCP area through applicable observable positive, negative, boundary, failure, recovery, accessibility, security, performance, and traceability checks, with no covered area relying solely on another Requirement's existence as proof of compliance. |

## 6. Requirement Traceability

| Requirement | FEB/FSC/FCD/FPE/FCA | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- | --- |
| REQ-FCP-001 | REQ-FEB-001–003; REQ-FSC-001–003; REQ-FCD-001–003; REQ-FPE-001–003; REQ-FCA-001–003 | PRODUCT.md §§21–22, 36 | REQ-BUS-047 | — | AGENTS.md §§5, 10.3, 14 | All FCP consumers |
| REQ-FCP-002 | REQ-FEB-002, 051; REQ-FSC-002–003, 043; REQ-FCD-003; REQ-FPE-003; REQ-FCA-003 | PRODUCT.md §§21–22, 26 | REQ-BUS-047 | — | AGENTS.md §27.1 | FCP implementation and later frontend Specifications |
| REQ-FCP-003 | REQ-FEB-003–004; REQ-FSC-003; REQ-FCD-003; REQ-FPE-003; REQ-FCA-003 | PRODUCT.md §§12–14, 21 | REQ-BUS-009, 021, 047 | REQ-PRD-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-CHK-002–003; REQ-PAY-002–003; REQ-ORD-002–003; REQ-SHP-002–003; REQ-RET-002–003; REQ-NTF-002–004 | ENGINEERING-PRINCIPLES.md §§7–10 | FSC, FCD, FPE, FCA, FPP, FAD and FRP |
| REQ-FCP-004 | REQ-FEB-003–008 | PRODUCT.md §§7, 12.3–12.4, 14 | REQ-BUS-007, 009 | REQ-CART-004, 013 | GLOSSARY.md §§4, 9, 13, 22 | Visitors, Customers and Checkout |
| REQ-FCP-005 | REQ-FEB-003–005, 008 | PRODUCT.md §§12.3–12.4 | REQ-BUS-006, 009 | REQ-CART-005, 007–008 | GLOSSARY.md §9 | Customers and Checkout |
| REQ-FCP-006 | REQ-FEB-003–005; REQ-FPE-006–011 | PRODUCT.md §§12.3–12.4, 14 | REQ-BUS-005–006 | REQ-PRD-007–010, 020–024 | UI.md §17 | Customers and Cart |
| REQ-FCP-007 | REQ-FEB-019–023 | PRODUCT.md §§12.3, 14 | REQ-BUS-009–010 | REQ-CART-009, 011 | UI.md §§13–14, 23 | Customers and Cart |
| REQ-FCP-008 | REQ-FEB-019, 023; REQ-FPE-020 | PRODUCT.md §§12.3, 14 | REQ-BUS-006, 009–010 | REQ-CART-009; REQ-INV-010–012 | UI.md §§13–14 | Customers and Cart |
| REQ-FCP-009 | REQ-FEB-014–016, 020–023 | PRODUCT.md §§5.4–5.6 | REQ-BUS-009–010, 042 | REQ-CART-009, 011, 023, 032 | UI.md §§21–23 | Customers and Operations |
| REQ-FCP-010 | REQ-FEB-022–023 | PRODUCT.md §§5.4–5.6 | REQ-BUS-010, 035–036 | REQ-CART-010, 022–023 | API.md §§32–34 | Customers, Cart and Operations |
| REQ-FCP-011 | REQ-FEB-016–017, 022–023 | PRODUCT.md §§5.4–5.6 | REQ-BUS-010, 035–036, 042 | REQ-CART-022–023, 026 | ANGULAR.md §§18, 37–38 | Customers and Cart |
| REQ-FCP-012 | REQ-FEB-040, 047–050 | PRODUCT.md §§21, 24–26; Open Product Decision 4 | REQ-BUS-009, 048 | REQ-CART-012–013 | DECISIONS.md §§25–40 | Product, Customers and delivery teams |
| REQ-FCP-013 | REQ-FEB-004–007 | PRODUCT.md §§5.4, 12.3 | REQ-BUS-014–016 | REQ-PRC-002, 006–008, 019 | UI.md §§17, 45 | Customers and Checkout |
| REQ-FCP-014 | REQ-FEB-004–005 | PRODUCT.md §§12.3, 14; Open Product Decisions 8, 14, 29 | REQ-BUS-015–016 | REQ-PRC-009–012, 019 | UI.md §17 | Customers, Pricing and Checkout |
| REQ-FCP-015 | REQ-FEB-004–005 | PRODUCT.md §§12.3, 20; Open Product Decisions 9, 25 | REQ-BUS-014–016, 054 | REQ-PRC-006, 013, 019 | UI.md §17 | Customers, Pricing and Checkout |
| REQ-FCP-016 | REQ-FEB-005, 007, 014–015, 020 | PRODUCT.md §§5.4–5.6 | REQ-BUS-012, 015–016, 042 | REQ-PRC-008, 018, 024, 034; REQ-CHK-013–015 | UI.md §§23, 45 | Customers and Checkout |
| REQ-FCP-017 | REQ-FEB-004–007 | PRODUCT.md §§5.4, 12.3; Open Product Decisions 13, 17 | REQ-BUS-017–018 | REQ-INV-002, 006–009 | UI.md §§17, 45 | Customers, Cart and Checkout |
| REQ-FCP-018 | REQ-FEB-003–005 | PRODUCT.md §§12.3, 14; Open Product Decision 12 | REQ-BUS-019 | REQ-INV-010–013; REQ-CART-014–015 | UI.md §17 | Customers, Cart and Checkout |
| REQ-FCP-019 | REQ-FEB-005, 007, 014–015, 020 | PRODUCT.md §§5.4–5.6; Open Product Decisions 13, 17 | REQ-BUS-017–020, 042 | REQ-INV-007–009, 015, 036; REQ-CART-015 | UI.md §§23, 45 | Customers and Checkout |
| REQ-FCP-020 | REQ-FEB-020–023 | PRODUCT.md §§5.6, 17.2 | REQ-BUS-010, 035–036, 045 | REQ-CART-006, 017, 022–023, 026, 032 | ARCHITECTURE.md §20 | Customers, Cart and Operations |
| REQ-FCP-021 | REQ-FEB-003–005, 009; REQ-FSC-025; REQ-FPE-025 | PRODUCT.md §§13–14 | REQ-BUS-011–013 | REQ-CART-018; REQ-CHK-005–007 | UI.md §§12, 29–30 | Customers and Checkout |
| REQ-FCP-022 | REQ-FEB-003–006, 011; REQ-FCA-005, 022 | PRODUCT.md §§7, 14; Open Product Decision 4 | REQ-BUS-007, 011, 032 | REQ-CUS-004, 012, 033; REQ-IDN-005, 036; REQ-CHK-010 | UI.md §§28–30 | Visitors, Customers and Checkout |
| REQ-FCP-023 | REQ-FEB-011–013, 030; REQ-FCA-023, 029–030 | PRODUCT.md §§5.5, 17.3 | REQ-BUS-032–033 | REQ-CART-024; REQ-CHK-012, 034; REQ-IDN-026–028 | SECURITY-STANDARDS.md §§10–13 | All protected FCP consumers |
| REQ-FCP-024 | REQ-FEB-003–005, 019; REQ-FCA-022–023 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-008, 011 | REQ-CUS-015–017; REQ-CHK-011 | UI.md §§13–14, 17 | Customers and Checkout |
| REQ-FCP-025 | REQ-FEB-003–005, 014–015 | PRODUCT.md §§12.3, 14; Open Product Decision 7 | REQ-BUS-011, 029 | REQ-SHP-004–006, 009, 015, 025–026 | UI.md §§17, 23 | Customers and Checkout |
| REQ-FCP-026 | REQ-FEB-003–005 | PRODUCT.md §§12.3, 14; Open Product Decisions 7–8 | REQ-BUS-014–016, 029 | REQ-SHP-006–008; REQ-PRC-006, 014, 019 | UI.md §17 | Customers, Pricing, Shipping and Checkout |
| REQ-FCP-027 | REQ-FEB-009–010, 014–015, 020; REQ-FSC-024 | PRODUCT.md §§13–14 | REQ-BUS-011–013, 042 | REQ-CHK-005, 021–023, 032 | UI.md §§21–23, 29–30 | Customers and Checkout |
| REQ-FCP-028 | REQ-FEB-005, 007, 020 | PRODUCT.md §§5.4–5.6, 13 | REQ-BUS-012, 014–019 | REQ-CHK-007–021 | UI.md §§23, 45 | Customers and Checkout |
| REQ-FCP-029 | REQ-FEB-014–015, 020–021 | PRODUCT.md §§5.4–5.6 | REQ-BUS-012–013, 042 | REQ-CHK-022, 026, 032–033 | UI.md §§21–23 | Customers and Operations |
| REQ-FCP-030 | REQ-FEB-003–005, 041 | PRODUCT.md §§12.3, 14 | REQ-BUS-011, 014–016, 021–022 | REQ-CHK-021–023, 030; REQ-ORD-009, 017 | UI.md §§17, 42 | Customers and Checkout |
| REQ-FCP-031 | REQ-FEB-019–023 | PRODUCT.md §§5.4–5.6, 13 | REQ-BUS-013, 021, 024–026 | REQ-CHK-023–025, 027, 030 | UI.md §§13–14, 23 | Customers and Checkout |
| REQ-FCP-032 | REQ-FEB-022–023 | PRODUCT.md §§5.4–5.6 | REQ-BUS-013, 026, 035–036 | REQ-CHK-024–025, 033; REQ-PAY-022–025; REQ-ORD-012–014 | API.md §§32–34 | Customers, Checkout, Payment and Operations |
| REQ-FCP-033 | REQ-FEB-003–005, 020 | PRODUCT.md §§13, 20; Open Product Decision 6 | REQ-BUS-024, 028 | REQ-CHK-027; REQ-PAY-003–007, 010 | UI.md §§17, 23 | Customers, Checkout and Payment |
| REQ-FCP-034 | REQ-FEB-003–005, 014–015, 020 | PRODUCT.md §§5.4–5.6, 13 | REQ-BUS-024–025, 028, 042 | REQ-PAY-004–005, 007–008, 019–021 | UI.md §§17, 21–23 | Customers and Checkout |
| REQ-FCP-035 | REQ-FEB-003–005, 020 | PRODUCT.md §§5.5–5.6, 20; Open Product Decision 6 | REQ-BUS-024–025 | REQ-PAY-011–013, 019; REQ-CHK-028 | SECURITY-STANDARDS.md §§18–20 | Customers and Payment |
| REQ-FCP-036 | REQ-FEB-020–023 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-025–026, 035–036, 042, 045 | REQ-PAY-020–025; REQ-CHK-029, 031–033 | API.md §§32–34 | Customers and Operations |
| REQ-FCP-037 | REQ-FEB-003–005 | PRODUCT.md §§13–14 | REQ-BUS-021–022, 024–025 | REQ-ORD-007–011; REQ-CHK-030–031; REQ-PAY-027 | UI.md §17 | Customers, Checkout and Order |
| REQ-FCP-038 | REQ-FEB-003–005, 014–015 | PRODUCT.md §§5.4, 13–14 | REQ-BUS-021–025, 042 | REQ-ORD-009–011; REQ-PAY-019, 027; REQ-CHK-028, 030–031 | UI.md §§17, 23 | Customers and FPP |
| REQ-FCP-039 | REQ-FEB-003–005, 009; REQ-FSC-025; REQ-FCA-028 | PRODUCT.md §§13–14 | REQ-BUS-021–030 | REQ-ORD-034–039; REQ-SHP-013–014; REQ-RET-002–003 | UI.md §§12, 29–30 | Customers and FPP |
| REQ-FCP-040 | REQ-FEB-014–015, 020; REQ-FSC-029 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-042 | REQ-CART-032; REQ-CHK-032; REQ-PAY-020–021 | UI.md §§21–23; ACCESSIBILITY.md §§24–26 | Customers |
| REQ-FCP-041 | REQ-FEB-016–017 | PRODUCT.md §§5.4–5.6 | REQ-BUS-012–013, 025, 042 | REQ-CART-022–023; REQ-CHK-025; REQ-PAY-021–023 | ANGULAR.md §§18, 37–38 | Customers |
| REQ-FCP-042 | REQ-FEB-020–023; REQ-FSC-031 | PRODUCT.md §§5.6, 17.2; Open Product Decisions 18, 24 | REQ-BUS-013, 025–026, 035–036, 042, 045 | REQ-CART-023, 026, 032; REQ-CHK-029, 032–033; REQ-PAY-021, 024–025 | UI.md §§23, 45 | Customers and Operations |
| REQ-FCP-043 | REQ-FEB-024–025 | PRODUCT.md §§5.4–5.6 | REQ-BUS-010, 042 | REQ-CART-009, 022–023 | ANGULAR.md §§37–38 | Customers |
| REQ-FCP-044 | REQ-FEB-006, 008, 030; REQ-FCA-031 | PRODUCT.md §§5.5, 16.7 | REQ-BUS-032, 039 | REQ-CUS-005; REQ-IDN-006; REQ-CART-004, 008, 024–025; REQ-CHK-004, 012, 035; REQ-PAY-029–032 | SECURITY-STANDARDS.md §§12, 35 | Customers and Security owners |
| REQ-FCP-045 | REQ-FEB-026–030; REQ-FCA-032 | PRODUCT.md §§16.7, 20 | REQ-BUS-027, 039–040 | REQ-CART-025; REQ-CHK-035; REQ-PAY-031–032 | SECURITY-STANDARDS.md §§14, 27, 35 | Customers, Privacy and Security |
| REQ-FCP-046 | REQ-FEB-027–030; REQ-FSC-013, 037; REQ-FCA-033 | PRODUCT.md §§5.1, 20 | REQ-BUS-032, 039, 052 | REQ-CART-024–025; REQ-CHK-034–035; REQ-PAY-030–032 | SECURITY-STANDARDS.md §§18, 33, 35 | Customers and Security |
| REQ-FCP-047 | REQ-FEB-031, 033, 041; REQ-FSC-032, 036 | PRODUCT.md §§5.2–5.3, 5.7 | REQ-BUS-002, 037 | REQ-CART-027; REQ-CHK-040; REQ-PAY-040 | ACCESSIBILITY.md §§35–38, 46, 55, 72–73 | Customers |
| REQ-FCP-048 | REQ-FEB-032, 034–038; REQ-FSC-033–035 | PRODUCT.md §5.7 | REQ-BUS-037 | REQ-CART-027; REQ-CHK-040; REQ-PAY-040 | ACCESSIBILITY.md §§8–11, 17, 23–29, 41–43, 53–54 | Customers |
| REQ-FCP-049 | REQ-FEB-035–038; REQ-FSC-034–035 | PRODUCT.md §§5.6–5.7 | REQ-BUS-037, 042 | REQ-CART-027, 032; REQ-CHK-040; REQ-PAY-040 | ACCESSIBILITY.md §§9, 11, 23–29 | Customers |
| REQ-FCP-050 | REQ-FEB-042–044; REQ-FSC-038 | PRODUCT.md §§8.3, 18.4–18.6 | REQ-BUS-038, 045 | REQ-CART-028; REQ-CHK-041; REQ-PAY-041 | PERFORMANCE.md §§16–17, 21, 35, 43–48, 54–57, 83 | Customers and Operations |
| REQ-FCP-051 | REQ-FEB-020, 043–044; REQ-FSC-030, 038 | PRODUCT.md §§5.4–5.6, 8.3 | REQ-BUS-038, 042, 045 | REQ-CART-026, 032; REQ-CHK-026, 032–033, 041; REQ-PAY-021, 025, 041 | PERFORMANCE.md §§17, 21, 23, 35, 45, 83 | Customers and Operations |
| REQ-FCP-052 | REQ-FEB-045–046; REQ-FSC-039 | PRODUCT.md §§18.3, 33 | REQ-BUS-034–035, 043 | REQ-CART-029; REQ-CHK-041–042; REQ-PAY-041–042; REQ-ORD-049–050 | EVENTS.md §§6–8, 43–48; ANGULAR.md §61 | Engineering, Security and Operations |
| REQ-FCP-053 | REQ-FEB-026, 029, 046–047; REQ-FSC-039 | PRODUCT.md §§18.3, 33; Open Product Decisions 19–20 | REQ-BUS-039–040, 043–044 | REQ-CUS-020–021, 038 | EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §§27–28 | Product, Analytics and Privacy |
| REQ-FCP-054 | REQ-FEB-048; REQ-FSC-040 | PRODUCT.md §§5.9, 21, 36–38; Open Product Decision 30 | REQ-BUS-048 | — | ARCHITECTURE.md §§38.4, 43.3; ANGULAR.md §§62–63 | Customers and delivery teams |
| REQ-FCP-055 | REQ-FEB-005, 007, 014–015, 020, 042, 049; REQ-FSC-041 | PRODUCT.md §§21–22, 26 | REQ-BUS-042, 046–047 | REQ-PRD-052; REQ-CUS-047; REQ-IDN-045; REQ-INV-035; REQ-CART-031; REQ-PRC-033; REQ-CHK-038; REQ-PAY-039; REQ-ORD-045; REQ-SHP-037 | ARCHITECTURE.md §§13, 36.5, 39; API.md §§9–13, 16–17, 23, 35–37, 63–72 | FCP implementation and Contract owners |
| REQ-FCP-056 | REQ-FEB-040, 047, 050; REQ-FSC-042 | PRODUCT.md §§21, 25–26; Open Product Decisions 3–4, 6–9, 12–14, 17–20, 24–26, 29–30 | REQ-BUS-046, 048 | — | DECISIONS.md §§25–40; ENGINEERING-PRINCIPLES.md §§11, 24, 36 | Product, Design, Engineering and later frontend Specifications |
| REQ-FCP-057 | REQ-FEB-001–003, 011–023, 026–051; REQ-FSC-002–003, 024, 043; REQ-FCD-044; REQ-FPE-045; REQ-FCA-050 | PRODUCT.md §§22, 26, 35 | REQ-BUS-009–029, 032–040, 042–049, 051–052, 054 | REQ-PRD-044; REQ-CUS-049; REQ-IDN-050; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-CHK-044; REQ-PAY-045; REQ-ORD-052; REQ-SHP-043; REQ-NTF-056 | TESTING-STANDARDS.md §§5, 7, 9, 11, 13, 19–24, 27–31, 33–40; ANGULAR.md §§56–60, 68 | Engineering, QA, Accessibility, Security and all FCP consumers |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following eighteen are materially relevant to FCP, retain their exact source wording and order, and remain unresolved by this Specification:

| Source Order | Open Product Decision | FCP Boundary |
| --- | --- | --- |
| 3 | Size, fit, colour, material, and other Product Variant or Attribute standards. | FCP preserves governed Product Variant evidence and selects no Attribute standard. |
| 4 | Guest checkout versus mandatory account rules. | FCP selects neither model and imposes no unapproved Account or Authentication prerequisite. |
| 6 | Initial payment methods and provider. | FCP selects no method, provider, fallback, routing, or presentation policy. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | FCP presents governed Shipping evidence and selects no provider, service, area, fee, or commitment policy. |
| 8 | Free-delivery threshold and promotional treatment. | FCP presents only governed commercial evidence and selects no threshold or promotional treatment. |
| 9 | Tax-inclusive display and invoice requirements. | FCP selects no inclusion, display, invoice, jurisdiction, or compliance policy. |
| 12 | Stock Reservation duration. | FCP selects no duration, renewal, release, expiry, or hold policy. |
| 13 | Back-order and pre-order support. | FCP selects neither capability nor availability, commitment, timing, or customer-message policy. |
| 14 | Voucher and promotion stacking policy. | FCP selects no combination, priority, eligibility, exclusion, or stacking rule. |
| 17 | Low-stock and out-of-stock customer messaging. | FCP selects no threshold, wording, urgency, promise, or availability policy. |
| 18 | Customer-support channels and service expectations. | FCP may present governed recovery entry but selects no channel, availability, target, or commitment. |
| 19 | Marketing-consent and communication-preference model. | FCP infers no Consent and selects no model, default, category, capture, or communication policy. |
| 20 | Initial analytics provider and event taxonomy. | FCP selects no provider, taxonomy, event, payload, identity, audience, attribution, or destination. |
| 24 | Production customer-service and operational escalation process. | FCP selects no support escalation, approval, override, repair, or operational workflow. |
| 25 | South African tax-display, invoice, and credit-note policy. | FCP selects no tax display, invoice, Credit Note, jurisdiction, or commercial-document rule. |
| 26 | Fraud-screening approach and manual-review workflow. | FCP selects no provider, signal, threshold, blocking rule, reviewer, or review workflow. |
| 29 | Gift cards, store credit, and promotional credit policy. | FCP selects no instrument support, balance, eligibility, combination, expiry, or accounting rule. |
| 30 | Product launch date, release scope, and post-launch support window. | FCP selects no launch date, initial capability set, rollout, release scope, or support window. |

## 8. Risks and Controls

| Risk | Control |
| --- | --- |
| Stale Price or Currency is presented as current. | Preserve Pricing source and freshness evidence and require governed revalidation or truthful qualification. |
| Stale Inventory or availability is presented as commitment. | Preserve Inventory provenance and uncertainty and prohibit frontend Stock promises. |
| Stock Reservation is inferred from Cart or availability presentation. | Require explicit Inventory-owned reservation evidence and negative inference tests. |
| Duplicate Cart interaction causes an unintended effect. | Preserve intent correlation and governed duplicate outcomes without defining backend mechanisms. |
| Concurrent Cart mutations silently overwrite accepted state. | Reject stale completion, expose conflicts, and revalidate accepted Cart evidence. |
| Duplicate purchase submission creates or claims duplicate commercial effect. | Gate claims and repeated intent on governed correlated outcomes and recovery evidence. |
| Unknown Payment effect is shown as success or failure. | Preserve uncertainty until Payment and Checkout evidence resolves it. |
| Purchase confirmation appears without Order creation. | Require authoritative correlated Order-creation evidence for confirmation. |
| Cart and Checkout evidence diverge. | Present coordinated revalidation and block unsupported progression. |
| Cart state leaks across Customers or Sessions. | Isolate and revalidate state across every governing context change. |
| Stale Session or Authorization context retains access. | Require current server-side contextual Authorization and reject stale security evidence. |
| Client Price calculation becomes authoritative. | Present Pricing-owned outcomes only and label no local calculation as trusted truth. |
| Shipping evidence becomes policy or commitment. | Preserve Shipping source, applicability, freshness, and uncertainty without local policy. |
| Recovery causes duplicate commercial effect. | Preserve correlation and unknown-effect state and require governed recovery evidence. |
| Optimistic state appears accepted. | Limit optimism to reversible Cart intent and keep it visibly provisional and correctable. |
| Dynamic Cart or purchase changes are inaccessible. | Verify structure, focus, status, validation, errors, and input-modality equivalence. |
| Cart or summary work becomes unbounded. | Bound frontend work by governed inputs and performance evidence without local limits. |
| Optional dependency failure blocks critical recovery. | Isolate optional regions and preserve truthful critical purchase and recovery presentation. |
| Payment or Sensitive Data leaks through telemetry or analytics. | Minimize all evidence surfaces and prohibit Secrets and raw instrument data. |
| Feature flags weaken authority, security, privacy, or accessibility. | Verify every reachable flag state against inherited and FCP-specific obligations. |
| FCP absorbs post-purchase behavior. | Limit FCP to confirmation and safe FPP handoff while retaining downstream ownership. |
| Concrete provider or Contract mechanisms become normative. | Prohibit providers, routes, methods, schemas, payloads, persistence, and transports. |
| Payment success is treated as Order creation. | Keep Payment and Order evidence distinct and require independent Order confirmation. |
| Commercial changes proceed without Customer awareness. | Expose material governed changes and require acceptance where Checkout requires it. |

## 9. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/ANGULAR.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/frontend/shared/frontend-baseline.md`
- `specifications/frontend/storefront/storefront-shell-content.md`
- `specifications/frontend/storefront/catalogue-discovery.md`
- `specifications/frontend/storefront/product-evaluation.md`
- `specifications/frontend/storefront/account-identity.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/notifications/notifications-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-09 | Draft | Initial comprehensive Cart and Purchase Specification. |
| 1.0.0 | 2026-09-09 | Approved | Approved Cart and Purchase Specification. |

## 11. Final Validation

For this Approved baseline, verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, and scope is `FCP`, with normativity only within Cart and Purchase frontend scope and no repository-wide authority;
2. governing precedence, inherited frontend obligations, and Product, Business Requirement, and Approved Domain authority remain preserved;
3. FCP owns only Cart and purchase presentation and remains separate from FSC, FCD, FPE, FCA, FPP, FAD, and FRP;
4. Cart, Pricing, Inventory, Checkout, Payment, Order, Shipping, Customer, and Identity truth boundaries remain preserved;
5. accepted Cart, Checkout eligibility and progression, Payment initiation and outcomes, and Order creation require governing Domain evidence;
6. unknown-effect outcomes remain uncertain and purchase confirmation requires authoritative correlated Order creation;
7. initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states remain distinguishable without a mandatory lifecycle graph;
8. freshness, supersession, concurrency, optimism, failure provenance, and governed recovery remain correct without backend mechanism definition;
9. responsive, reflow, zoom, orientation, localization, content-extreme, keyboard, pointer, touch, focus, structure, status, validation, error, and assistive-technology evidence supports applicable WCAG 2.2 AA outcomes;
10. contextual Authorization, concealment, isolation, Sensitive Data, Payment-data minimization, Secrets, privacy, Consent non-inference, and safe rendering are preserved;
11. frontend work remains bounded, optional failures are isolated, and degradation remains truthful without numerical targets;
12. operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinct and non-authoritative;
13. feature-flag safety remains independently verifiable across every reachable state;
14. abstract Contract boundaries remain independently verifiable without routes, endpoints, methods, operations, DTOs, schemas, formats, mappings, payloads, persistence, transports, or providers;
15. Product-policy and implementation neutrality remain independently verifiable;
16. verification coverage is independently traceable; exactly 57 Requirements, 57 Acceptance Criteria, and 57 traceability rows exist; and their identifiers and mappings are unique, sequential, gap-free, and one-to-one;
17. every cited Requirement and source section physically exists and directly supports its FCP Requirement;
18. all eighteen Product Decisions exactly match `PRODUCT.md`, remain materially complete, source-ordered, and unresolved;
19. Risks have distinct FCP-specific controls and all Related Document paths exist;
20. Revision History contains exactly the preserved `0.1.0 Draft` row and one `1.0.0 Approved` row;
21. no Glossary amendment is required and FCP-local descriptions are not repository-wide terminology;
22. Markdown headings and tables, UTF-8, trailing whitespace, exactly one final newline, and prohibited-marker checks pass; and
23. Git scope contains only the authorized lifecycle-promotion changes to `specifications/frontend/storefront/cart-purchase.md`, with nothing staged, untracked, unrelated, or otherwise modified.
