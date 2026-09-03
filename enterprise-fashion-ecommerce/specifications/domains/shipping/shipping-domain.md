---
title: Shipping and Fulfilment Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-03
authoritative: false
---

# Shipping and Fulfilment Domain

## 1. Purpose

This Shipping and Fulfilment Domain Specification defines implementation-neutral authority for governed delivery choices, Fulfilment, Shipment, Dispatch, tracking, delivery, provider evidence, reconciliation, and safe recovery.

This document uses requirement scope code `SHP`. As an Approved Domain Specification, its Requirements are normative within the Shipping and Fulfilment Domain scope, remain subordinate to higher-authority governing sources under the repository Decision Hierarchy, and do not resolve Open Product Decisions unless an Approved governing source explicitly does so.

## 2. Scope and Authority

### REQ-SHP-001 — Lifecycle, Authority, and Scope

The Shipping and Fulfilment Domain MUST own authoritative Shipping processing truth within its scope, preserve governing-source precedence, use scope code `SHP`, and MUST apply this Approved Specification normatively only within the Shipping and Fulfilment Domain scope while remaining subordinate to higher-authority governing sources.

### REQ-SHP-002 — Cross-Domain Authority Separation

Shipping and Fulfilment MUST NOT own Product, Product Variant, Category, Customer identity, Authentication, Authorization policy, Pricing, Promotion or Discount policy, Cart, Checkout orchestration, Inventory, Stock, Stock Reservation, Available-to-Sell, Order creation or history, Payment, Return or Refund policy, Notification truth, Administration policy, Tax, invoice, or Credit Note policy.

## 3. Domain Context

Shipping and Fulfilment consumes governed commercial and operational context and produces authoritative delivery-choice, Fulfilment, Shipment, Dispatch, tracking, delivery, exception, and reconciliation outcomes. Checkout, Order, Inventory, Customer, Pricing, Payment, Return, Administration, Notifications, and Reporting retain their respective authority.

## 4. Canonical Terminology

Canonical terms follow `GLOSSARY.md`. Shipping, Shipment, Fulfilment, Carrier, Delivery Method, Shipping Address, Shipping Rate, Dispatch, Tracking Number, Order, Order Item, Order Snapshot, Stock, Stock Reservation, Money, Currency, Contract, Domain Event, Integration Event, Projection, Audit Record, Sensitive Data, Authorization, Principal, Resource, and Risk retain their canonical meanings. Lowercase phrases such as delivery quotation, delivery eligibility, delivery area, service level, provider evidence, delivery exception, and reverse logistics are descriptive rather than new canonical terms.

## 5. Shipping and Fulfilment Relationship

### REQ-SHP-003 — Unified Authority with Distinct Concerns

Shipping and Fulfilment MUST be governed together as one authority for delivery and fulfilment processing while keeping delivery choice and eligibility, Shipment, Fulfilment work, Dispatch, Carrier handling, tracking, delivery, and reverse-logistics coordination distinguishable. No separate concern or representation MAY become a competing authority for Shipment lifecycle truth.

## 6. Delivery Choices and Eligibility

### REQ-SHP-004 — Governed Delivery Choices

Shipping MAY determine available Delivery Method choices and delivery eligibility only from current governed purchase, destination, Shipping Address, delivery-area, service-level, and provider-availability context where applicable. It MUST NOT invent a Carrier, provider, geography, service level, Delivery Method, delivery window, cutoff, price, fee, or free-delivery threshold.

### REQ-SHP-005 — Delivery Revalidation and Failure

Shipping MUST distinguish current, stale, invalid, unsupported, and unavailable delivery-choice context and MUST revalidate materially stale choices before governed commitment. Invalid or unavailable context MUST produce an explicit safe outcome rather than a fabricated eligible choice, and Checkout MAY consume the outcome without becoming Shipping authority.

## 7. Shipping Rate and Quotation Boundary

### REQ-SHP-006 — Shipping-Owned Quotation Evidence

Where applicable, Shipping MAY determine or obtain a Shipping Rate or delivery quotation from governed inputs and MUST preserve its identity, applicability, freshness, and provider evidence. It MUST NOT establish final commercial totals, Promotion or Discount treatment, Tax, a fee algorithm, zone or weight formula, threshold, or rounding policy.

### REQ-SHP-007 — Pricing and Checkout Charge Separation

Pricing MUST remain authoritative for commercial calculation and Checkout MUST NOT trust a client-declared or stale Shipping charge. Shipping MUST provide applicable governed rate evidence for Pricing or Checkout revalidation, and a material disagreement MUST remain explicit and be safely revalidated or reconciled rather than silently resolved by Shipping.

## 8. Money and Currency

### REQ-SHP-008 — Money and Currency Integrity

Every Shipping monetary value MUST carry explicit Currency and use precision-safe, non-floating-point Money semantics. Shipping MUST reject silent Currency mixing, MUST NOT perform implicit conversion, and MUST preserve applicable Version 1 ZAR governance while leaving final commercial calculation to Pricing. No storage type, scale, Minor Unit, rounding algorithm, or foreign-exchange policy is established here.

## 9. Shipping Address and Customer Boundary

### REQ-SHP-009 — Governed Shipping Address Input

Shipping MAY consume only the governed Shipping Address or Address context required for delivery and MUST NOT own Customer identity or profile, redefine Address authority, or trust an arbitrary client-supplied Customer identifier. Invalid, inaccessible, mismatched, or stale Address context MUST fail safely.

### REQ-SHP-010 — Current and Historical Address Separation

Current Customer Address truth and retained Order or Shipment delivery context MUST remain distinguishable. Later Customer or Address changes MUST NOT silently rewrite historical Shipment or delivery truth, and this Specification defines no Address fields or snapshot mechanism.

## 10. Inventory Boundary

### REQ-SHP-011 — Inventory Non-Authority

Shipping and Fulfilment MUST NOT own, infer, fabricate, or silently mutate Stock, Available-to-Sell, Stock Reservation, Stock Adjustment, Stock Movement, or overselling protection. Fulfilment MAY consume governed Inventory outcomes without transferring Inventory authority.

### REQ-SHP-012 — Fulfilment and Inventory Coordination

Where Fulfilment requires Inventory context, the handoff MUST use current governed evidence and distinguish missing, stale, unavailable, conflicting, or partial context. Shipping and Fulfilment MUST NOT invent a warehouse, bin, allocation or picking algorithm, reservation implementation, or Stock schema, and MUST NOT treat its own progress as proof of an Inventory effect.

## 11. Order Boundary

### REQ-SHP-013 — Order and Shipment Separation

Shipping and Fulfilment MAY consume governed Order and Order Item context where permitted but MUST NOT create an Order, own Order lifecycle or history, rewrite an Order Snapshot, infer an Order from Payment, or alter commercial history. Shipment and Order MUST remain distinct.

### REQ-SHP-014 — Governed Order Handoff

A Shipment or Fulfilment action MUST correlate to the applicable governed Order and Order Item context where required, reject invalid, stale, mismatched, or unavailable context safely, and MUST NOT fabricate future Order Requirements, lifecycle, Contracts, or implementation.

## 12. Checkout Boundary

### REQ-SHP-015 — Checkout Delivery Boundary

Shipping MAY provide Checkout with governed delivery choices, eligibility, applicable Shipping Rate or quotation evidence, revalidation, and failure outcomes. It MUST NOT own Checkout lifecycle, Cart validation, final Pricing, Payment initiation, Checkout success, or Order creation, and defines no future Checkout Requirement or implementation.

## 13. Payment Boundary

### REQ-SHP-016 — Payment Non-Authority

Shipping and Fulfilment MUST NOT establish or infer Payment success, Payment Authorization, Capture, Void, Settlement, Refund, or Payment reconciliation from Order, Shipment, delivery, or provider state. A Payment outcome MAY participate only in governed coordination permitted by higher authority.

## 14. Fulfilment Processing

### REQ-SHP-017 — Governed Fulfilment Processing

Fulfilment MUST preserve distinguishable accepted context, preparation, Dispatch, Shipment, tracking, delivery, exception, permitted cancellation or stop request, and recovery or reconciliation outcomes where applicable. It MUST NOT invent a concrete state enumeration, warehouse model, picker or packer workflow, label or packaging rule, Carrier interface, cutoff, SLA, job, or queue.

## 15. Shipment Identity and Lifecycle

### REQ-SHP-018 — Stable Shipment Identity and Association

Each Shipment MUST have stable identity and an unambiguous association with the applicable governed Order, Order Item, and Fulfilment context where required. Unknown, inaccessible, ambiguous, mismatched, and duplicate associations MUST fail safely without exposing protected Resource existence.

### REQ-SHP-019 — Authoritative Shipment Lifecycle

Shipment lifecycle truth MUST use applicable canonical lifecycle meanings, preserve valid transition evidence and historical truth, and distinguish requested, observed, provider-reported, accepted, stale, invalid, and uncertain state. Invalid transitions MUST be rejected; no persistence or state-machine mechanism is prescribed.

## 16. Dispatch

### REQ-SHP-020 — Dispatch Integrity

Dispatch MUST remain distinct from Fulfilment preparation, Shipment creation, Carrier acceptance where applicable, in-transit status, delivery, and Order completion. Requested Dispatch and confirmed Dispatch or provider evidence MUST remain distinguishable, duplicate-safe, and uncertain where authoritative confirmation is absent; no timing or cutoff policy is established.

## 17. Carrier and Provider Boundary

### REQ-SHP-021 — Provider-Neutral Carrier Boundary

Carrier and provider integration MUST remain bounded by Approved Contracts and project-owned Shipping semantics. This Specification selects no Carrier, provider, service, SDK, API, endpoint, payload, status code, callback URL, Webhook schema, signing algorithm, or credential format, and external terminology or availability MUST NOT redefine Shipping truth.

## 18. Provider Evidence

### REQ-SHP-022 — Authoritative Provider Evidence

Where provider evidence contributes to Shipping truth, authenticity and integrity MUST be established under the Approved Contract, evidence MUST correlate to the applicable Shipment, and client-visible state MUST NOT become authoritative merely because it arrived. Stale, duplicate, replayed, malformed, mismatched, unauthorized, or unverifiable evidence MUST fail safely without exposing Sensitive Data, Secrets, or provider internals.

## 19. Tracking

### REQ-SHP-023 — Tracking Truth

Where applicable, a Tracking Number and tracking evidence MUST correlate to the correct Shipment and distinguish provider-reported evidence from authoritative accepted Shipping truth. Stale, duplicate, conflicting, or uncertain tracking MUST remain explicit, Customer-visible tracking and Projections MUST remain non-authoritative, and protected tracking data MUST receive applicable Sensitive Data and privacy treatment. No tracking URL or polling cadence is defined.

## 20. Delivery

### REQ-SHP-024 — Delivery Outcome Integrity

Expected or requested delivery, provider-reported delivery, authoritative accepted delivery evidence, delivery exception, failed delivery, and uncertain delivery MUST remain distinguishable where applicable. Shipping MUST NOT claim delivery without sufficient governed evidence or invent proof-of-delivery, signature, one-time-password, photograph, retry, redelivery, or SLA policy.

## 21. Failure and Uncertainty

### REQ-SHP-025 — Explicit Shipping Failure Outcomes

Applicable invalid Shipping Address or context, unavailable delivery choice, stale quotation, unsupported context, provider unavailability or rejection, malformed evidence, timeout, duplicate request or evidence, stale evidence, Dispatch, tracking or delivery uncertainty, Inventory or Order-context disagreement, unauthorized action, and reconciliation disagreement MUST produce distinguishable safe outcomes without invented Customer-facing wording.

### REQ-SHP-026 — Uncertainty Preservation

Unknown, pending, delayed, partial, conflicting, or otherwise uncertain Shipping outcomes MUST NOT be represented as success or definitive failure until authoritative evidence establishes the permitted state. Navigation, timeout, stopped observation, provider silence, or request abortion MUST NOT prove an external effect stopped or did not occur.

## 22. Idempotency, Duplicates, Retries, Replay, and Concurrency

### REQ-SHP-027 — Duplicate-Effect Prevention

Duplicate, retried, replayed, stale, or concurrent requests and evidence MUST NOT create duplicate Shipments, Dispatch, provider booking or other provider effects, tracking mutations, operational effects, or reverse-logistics effects. Applicable Contracts MUST preserve Idempotency Key semantics without defining a key format, constraint, lock, isolation, queue, table, cache, or retry count.

### REQ-SHP-028 — Safe Retry and Concurrency

Repeated activity MUST correlate to the applicable Shipment and governed context, return a consistent outcome where safe, reject conflicting or unauthorized replay, and verify unknown prior effects before repetition. Concurrent or stale input MUST NOT silently overwrite accepted truth or multiply effects.

## 23. Reconciliation and Recovery

### REQ-SHP-029 — Shipping Reconciliation

Shipping MUST reconcile delayed, missing, stale, conflicting, or internally and provider-disagreeing evidence, including uncertain Dispatch or delivery. Reconciliation MUST use authorized evidence, preserve history and duplicate safety, and produce an explicit resolved or still-uncertain outcome without defining cadence, scheduler, batch, queue, table, or workflow engine.

### REQ-SHP-030 — Controlled Recovery

Permitted recovery or operational intervention MUST be explicit, server-side authorized, observable, governed by current Shipping state, duplicate-safe, and supported by a proportional Audit Record where materially high-Risk. Recovery MUST NOT fabricate evidence, rewrite history, bypass another Domain, or convert uncertainty into success.

## 24. Cancellation Boundary

### REQ-SHP-031 — Cancellation Non-Authority

Shipping MUST NOT invent cancellation eligibility or cutoff policy. An authorized cancellation or stop request, where permitted, MUST remain distinct from its confirmed Shipping or provider outcome, preserve uncertainty and duplicate safety, leave Order history unchanged, and MUST NOT imply a Refund.

## 25. Return and Reverse-Logistics Boundary

### REQ-SHP-032 — Reverse-Logistics Coordination

Shipping and Fulfilment MAY support reverse-logistics execution or coordination only from governed approved Return context and MUST distinguish it from forward Shipping. It MUST NOT determine Return or Refund eligibility, Refund amount, own Refund execution, rewrite an Order Snapshot, or define a Return lifecycle.

## 26. Administration and Operations

### REQ-SHP-033 — Governed Operational Actions

Administration MAY invoke explicit protected Shipping and Fulfilment capabilities without becoming Shipping authority or bypassing Domain rules. Materially high-Risk actions MUST receive proportional server-side Authorization, appropriate confirmation, a Human Approval Gate where governing policy requires it, reason or evidence where applicable, and an appropriate Audit Record, without defining a Role or Permission matrix or escalation SLA.

## 27. Authentication and Authorization

### REQ-SHP-034 — Protected Shipping Authorization

Protected Shipping and Fulfilment actions MUST require trusted server-side Authorization for the current Principal, Resource, action, and Domain state. UI visibility, route, browser state, Role labels, client Permissions or Claims, supplied identifiers, and Authentication alone MUST NOT authorize an action, and Shipping MUST NOT become Identity authority.

## 28. Security and Sensitive Data

### REQ-SHP-035 — Shipping Data Protection

Shipping MUST minimize and protect applicable Shipping Address, Customer delivery context, sensitive Tracking Number or tracking data, provider credentials, Secrets, tokens, provider identifiers, sensitive operational notes, and abuse-sensitive information through least privilege and purpose limitation across storage, transmission, logs, errors, Contracts, events, Projections, Audit Records, analytics, exports, and support or administration surfaces. Ordinary non-sensitive commercial data MUST NOT automatically be classified as Sensitive Data.

## 29. Events

### REQ-SHP-036 — Conditional Shipping Events

A Domain Event or Integration Event MUST exist only where Architecture, an Approved Contract, reliability, or synchronization governance requires it. Any event MUST preserve Shipping authority, compatibility, Sensitive Data protection, duplicate and replay safety, and non-authoritative consumer Projection semantics without defining a name, topic, queue, broker, schema, payload, delivery guarantee, or choreography.

## 30. Contract Impacts

### REQ-SHP-037 — Shipping Contract Boundary

Future Contracts MUST preserve Shipment identity, Fulfilment, delivery choice and eligibility, applicable rate evidence and Money or Currency, provider neutrality and evidence, Dispatch, tracking, delivery, failure and uncertainty, idempotency, duplicate, retry and replay safety, Authorization, security, reconciliation, compatibility, and explicit failure behavior. No URL, HTTP method or status, GraphQL operation, JSON shape, field, DTO, class, controller, database schema, provider payload, Webhook schema, or event schema is defined.

## 31. Accessibility

### REQ-SHP-038 — Accessible Shipping Outcomes

Applicable Customer-facing delivery choice, Shipping Address error, eligibility, rate presentation, tracking, Shipment state, failure, and recovery surfaces MUST provide WCAG 2.2 AA outcomes, including applicable keyboard operation, focus handling, understandable status or error communication, and assistive-technology support, without claiming certification or Level AAA or prescribing visual design.

## 32. Performance and Observability

### REQ-SHP-039 — Bounded and Observable Shipping

Shipping interactions and Contracts MUST be bounded and observable. Timeout, dependency failure, provider degradation or unavailability, stale result, and uncertainty MUST remain distinguishable and MUST NOT become trusted success; no SLA, SLO, latency, timeout, retry, throughput, device, or network target is established.

## 33. Audit

### REQ-SHP-040 — Proportional Shipping Audit Evidence

Applicable materially high-Risk administrative intervention, reconciliation, correction, provider configuration change, security-sensitive action, or exceptional Shipment correction MUST produce proportional Audit Records. Ordinary Shipping reads MUST NOT automatically become high-Risk audit events.

## 34. Notifications, Reporting, and Projections

### REQ-SHP-041 — Non-Authoritative Representations

Communications, reporting, analytics, exports, and Projections MUST derive from authoritative Shipping truth, protect Sensitive Data, identify their source where applicable, and MUST NOT replace Shipment records, mutate Shipping truth, or claim Dispatch or delivery prematurely. Notification or reporting failure MUST NOT alter Shipping state.

## 35. Tax, Invoice, and Credit Note Boundary

### REQ-SHP-042 — Commercial-Document Non-Authority

Shipping MAY preserve governed Money evidence required by owning Domains but MUST NOT establish Tax or tax-display policy, invoice or Credit Note policy, numbering, legal interpretation, or document technology. Unresolved policy MUST remain external.

## 36. Testing

### REQ-SHP-043 — Shipping Verification Coverage

Verification evidence MUST cover applicable positive and negative flows; delivery choice and eligibility; Shipping Address; Shipping Rate or quotation; Money and Currency; Shipment; Fulfilment; Dispatch; provider evidence; tracking; delivery; failure, timeout and uncertainty; duplicates, retries, replay, concurrency and stale state; reconciliation and recovery; cancellation and reverse-logistics boundaries; Order, Checkout, Pricing, Inventory, Customer and Payment boundaries; Authorization; Sensitive Data; audit; events; Contracts; accessibility; and performance. No test framework, test-case identifier scheme, tooling, or numerical coverage target is selected.

## 37. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-SHP-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; scope is `SHP`; Shipping and Fulfilment-owned truth, governing-source precedence, and normative authority limited to the Shipping and Fulfilment Domain scope are identifiable. |
| REQ-SHP-002 | Shipping owns none of the listed external Domain truths or policies. |
| REQ-SHP-003 | All listed Shipping and Fulfilment concerns share one authority while remaining distinguishable, and no representation becomes competing Shipment lifecycle truth. |
| REQ-SHP-004 | Delivery choices use current governed context where applicable and select none of the prohibited provider, geography, service, timing, or commercial values. |
| REQ-SHP-005 | Current, stale, invalid, unsupported, and unavailable choices are distinguishable; stale choices are revalidated, invalid/unavailable context produces no fabricated eligibility, and Checkout remains non-authoritative. |
| REQ-SHP-006 | Applicable rate evidence preserves identity, applicability, freshness, and provider evidence while establishing none of the listed commercial policies or algorithms. |
| REQ-SHP-007 | Pricing retains calculation authority, Checkout trusts no stale/client charge, governed evidence supports revalidation, and disagreement is explicit rather than silently resolved. |
| REQ-SHP-008 | Every monetary value has Currency and precision-safe non-floating-point Money; mixing and implicit conversion are rejected; applicable ZAR and Pricing boundaries hold; no prohibited rule or representation is chosen. |
| REQ-SHP-009 | Only required governed Address context is consumed; Customer and Address authority remain external; supplied identifiers grant no authority; invalid, inaccessible, mismatched, and stale context fails safely. |
| REQ-SHP-010 | Current Address and historical delivery context remain distinct, later change rewrites no history, and no fields or snapshot mechanism is defined. |
| REQ-SHP-011 | Shipping owns, infers, fabricates, or mutates none of the listed Inventory concepts; governed consumption transfers no authority. |
| REQ-SHP-012 | Fulfilment uses governed Inventory evidence, distinguishes every listed degraded context, invents no prohibited model or mechanism, and its progress proves no Inventory effect. |
| REQ-SHP-013 | Shipping may consume governed Order context but creates or rewrites none of the listed Order truth; Shipment and Order remain distinct. |
| REQ-SHP-014 | Required actions correlate to valid current Order and Order Item context; all listed invalid context fails safely; no future Order design is fabricated. |
| REQ-SHP-015 | Checkout receives only listed Shipping outcomes while Shipping owns none of the listed Checkout, Cart, Pricing, Payment, success, or Order concerns and invents no Checkout design. |
| REQ-SHP-016 | Shipping establishes or infers no listed Payment truth; coordination occurs only where higher authority permits. |
| REQ-SHP-017 | Applicable Fulfilment concerns remain distinguishable without any prohibited state, operational, provider, timing, SLA, job, or queue design. |
| REQ-SHP-018 | Every Shipment has stable identity and correct governed associations; unknown, inaccessible, ambiguous, mismatched, or duplicate association fails without protected enumeration. |
| REQ-SHP-019 | Lifecycle uses canonical meaning, preserves transition evidence/history, distinguishes all evidence states, rejects invalid transition, and prescribes no mechanism. |
| REQ-SHP-020 | Dispatch remains distinct from every listed concern; request and confirmation remain distinct, duplicate-safe, and uncertain without evidence; no timing policy exists. |
| REQ-SHP-021 | Provider integration is Contract-bounded and provider-neutral, selects no prohibited detail, and external behavior cannot redefine Shipping truth. |
| REQ-SHP-022 | Applicable evidence is authentic, integral, correctly correlated, and not client-authoritative; every listed invalid evidence case fails safely without protected disclosure. |
| REQ-SHP-023 | Tracking correlates correctly, distinguishes provider report from accepted truth, preserves degraded outcomes, keeps representations non-authoritative, protects applicable data, and defines no URL or cadence. |
| REQ-SHP-024 | Every listed delivery outcome is distinguishable; unsupported delivery claims and every prohibited proof, retry, redelivery, or SLA policy are absent. |
| REQ-SHP-025 | Every applicable listed failure produces a distinguishable safe outcome without invented Customer wording. |
| REQ-SHP-026 | Every listed uncertain state remains neither success nor definitive failure; client or provider interruption proves no external outcome. |
| REQ-SHP-027 | Repetition creates none of the listed duplicate effects; Contracts preserve applicable Idempotency Key semantics without a prohibited mechanism. |
| REQ-SHP-028 | Repeated activity correlates correctly, is consistent where safe, rejects bad replay, verifies unknown effects, and cannot overwrite truth or multiply effects. |
| REQ-SHP-029 | Every listed reconciliation condition uses authorized evidence, preserves history and duplicate safety, and remains explicitly resolved or uncertain without a prohibited mechanism. |
| REQ-SHP-030 | Recovery is explicit, authorized, observable, state-governed, duplicate-safe and proportionally audited, and fabricates no evidence, history, authority, or success. |
| REQ-SHP-031 | No cancellation policy is invented; request and outcome remain distinct, uncertain and duplicate-safe; Order history is unchanged and Refund is not implied. |
| REQ-SHP-032 | Governed Return context alone permits distinct reverse coordination, which establishes no Return/Refund policy, Refund execution, snapshot mutation, or Return lifecycle. |
| REQ-SHP-033 | Administration uses explicit protected capabilities without gaining Shipping authority or bypassing Domain rules; materially high-Risk actions receive proportional server-side Authorization, appropriate confirmation, a Human Approval Gate where governing policy requires it, reason or evidence where applicable, and an appropriate Audit Record; no Role or Permission matrix or escalation SLA is defined. |
| REQ-SHP-034 | Protected actions use contextual server-side Authorization; none of the listed client/security facts authorizes alone, and Identity remains external. |
| REQ-SHP-035 | Every listed protected data category is minimized and purpose/least-privilege protected across all surfaces, while ordinary data is not automatically Sensitive Data. |
| REQ-SHP-036 | Events exist only for governed need and preserve authority, compatibility, protection, duplicate/replay safety, and Projection non-authority without prohibited design. |
| REQ-SHP-037 | Future Contracts preserve every listed semantic, safety, security, compatibility, and failure concern without defining prohibited Contract or implementation detail. |
| REQ-SHP-038 | Applicable Shipping surfaces provide WCAG 2.2 AA, keyboard, focus, understandable communication, and assistive-technology evidence without certification, AAA, or visual-design claims. |
| REQ-SHP-039 | Delivery is bounded and observable; each degraded condition remains explicit and cannot become success; no numerical target or profile is asserted. |
| REQ-SHP-040 | Each applicable listed high-Risk action produces proportional Audit Record evidence while ordinary reads are not automatically high-Risk. |
| REQ-SHP-041 | Every representation derives from protected Shipping truth, identifies source where applicable, replaces or mutates no record, claims no premature outcome, and cannot alter state through its failure. |
| REQ-SHP-042 | Shipping preserves permitted Money evidence but establishes no listed Tax, document, numbering, legal, or technology policy; unresolved policy stays external. |
| REQ-SHP-043 | Verification covers every listed applicable functional, negative, boundary, resilience, security, quality, event, and Contract concern without selecting framework, IDs, tooling, or coverage target. |

## 38. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-SHP-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047–048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9, 19–21 | Shipping governance |
| REQ-SHP-002 | PRODUCT.md §§13, 16 | REQ-BUS-015, 017, 021, 024, 029–030 | REQ-PRD-045–048; REQ-CUS-031–036; REQ-INV-026; REQ-CART-021; REQ-PRC-014; REQ-PAY-002 | ARCHITECTURE.md §§9–10 | Cross-domain |
| REQ-SHP-003 | PRODUCT.md §§12.7, 14.7, 16.4–16.5 | REQ-BUS-029 | REQ-INV-026; REQ-CART-021 | GLOSSARY.md §§8, 11, 41 | Shipping, Fulfilment |
| REQ-SHP-004 | PRODUCT.md §§14.4, 16.5, 24 | REQ-BUS-012, 029, 048 | REQ-CUS-036; REQ-PRC-014 | GLOSSARY.md §11 | Checkout |
| REQ-SHP-005 | PRODUCT.md §§5.4–5.6, 16.5, 17.2 | REQ-BUS-012, 042, 046 | REQ-CART-021; REQ-PRC-014 | API.md §§43, 50–58 | Checkout |
| REQ-SHP-006 | PRODUCT.md §§16.1, 16.5–16.6, 24 | REQ-BUS-014–016, 029 | REQ-PRC-014 | GLOSSARY.md §§5, 11, 35 | Pricing, Checkout |
| REQ-SHP-007 | PRODUCT.md §§14.4, 16.1, 16.5 | REQ-BUS-012, 015–016 | REQ-CART-021; REQ-PRC-014, 018 | API.md §§42–43 | Pricing, Checkout |
| REQ-SHP-008 | PRODUCT.md §§6, 16.1, 20 | REQ-BUS-014–015 | REQ-PRC-006, 014 | GLOSSARY.md §§5, 35; DATABASE.md §24; POSTGRES.md §13 | Pricing, Checkout |
| REQ-SHP-009 | PRODUCT.md §§7, 14.4, 16.7 | REQ-BUS-008, 011, 032, 039 | REQ-CUS-015–017, 036 | SECURITY-STANDARDS.md §§10–12, 35 | Customer, Checkout |
| REQ-SHP-010 | PRODUCT.md §§16.4–16.5, 17.4 | REQ-BUS-022–023 | REQ-CUS-017, 025, 036 | ARCHITECTURE.md §40.5 | Order, Customer |
| REQ-SHP-011 | PRODUCT.md §§13, 16.2, 16.5 | REQ-BUS-017–020, 029 | REQ-INV-002–016, 026 | ARCHITECTURE.md §20.3 | Inventory, Fulfilment |
| REQ-SHP-012 | PRODUCT.md §§12.7, 15.3, 16.2, 16.5 | REQ-BUS-019–020, 029, 042 | REQ-INV-023–026, 030 | DATABASE.md §52 | Inventory, Order |
| REQ-SHP-013 | PRODUCT.md §§13, 16.4–16.5, 17.4 | REQ-BUS-021–023, 029 | REQ-CUS-034, 036; REQ-INV-024, 026; REQ-CART-019, 021; REQ-PRC-019 | ARCHITECTURE.md §40.5 | Order |
| REQ-SHP-014 | PRODUCT.md §§14.6–14.7, 16.4–16.5 | REQ-BUS-021–023, 029, 042 | REQ-INV-024, 026 | API.md §44 | Order |
| REQ-SHP-015 | PRODUCT.md §§13, 14.4, 16.5, 30.4 | REQ-BUS-011–013, 029 | REQ-CUS-033, 036; REQ-INV-023, 026; REQ-CART-018, 021; REQ-PRC-014, 018 | API.md §43 | Checkout |
| REQ-SHP-016 | PRODUCT.md §§13, 16.3–16.5 | REQ-BUS-024–029 | REQ-PAY-002, 016–019, 026–028 | ARCHITECTURE.md §20.2 | Payment |
| REQ-SHP-017 | PRODUCT.md §§9.4, 12.7, 14.7, 15.3 | REQ-BUS-029, 031 | REQ-INV-026 | GLOSSARY.md §§8, 11, 41 | Fulfilment, operations |
| REQ-SHP-018 | PRODUCT.md §§12.7, 14.7, 16.5 | REQ-BUS-029, 035 | REQ-CUS-036; REQ-INV-026 | GLOSSARY.md §§5, 8, 11 | Order, support |
| REQ-SHP-019 | PRODUCT.md §§16.5, 17.1, 17.4 | REQ-BUS-023, 029, 042 | — | GLOSSARY.md §§11, 41; DATABASE.md | Shipping consumers |
| REQ-SHP-020 | PRODUCT.md §§9.4, 14.7, 16.5 | REQ-BUS-029, 042 | REQ-INV-026 | GLOSSARY.md §§8, 11, 41 | Order, Customer |
| REQ-SHP-021 | PRODUCT.md §§9.8, 16.5, 24, 37.3 | REQ-BUS-029, 046, 048 | — | DECISIONS.md §38; API.md §50 | Provider adapters |
| REQ-SHP-022 | PRODUCT.md §§5.5–5.6, 16.5, 23 | REQ-BUS-029, 039, 042, 046 | — | SECURITY-STANDARDS.md; API.md §§49–53, 58 | Provider adapters, operations |
| REQ-SHP-023 | PRODUCT.md §§14.7, 16.5, 28.5 | REQ-BUS-029, 039, 042 | REQ-CUS-036 | GLOSSARY.md §11; SECURITY-STANDARDS.md §35 | Customer, support |
| REQ-SHP-024 | PRODUCT.md §§5.4–5.6, 14.7, 16.5 | REQ-BUS-029, 042, 046 | — | GLOSSARY.md §§11, 41; API.md §58 | Customer, Order |
| REQ-SHP-025 | PRODUCT.md §§5.6, 16.5, 17.2, 23 | REQ-BUS-029, 042, 046 | — | API.md §§52–58; SECURITY-STANDARDS.md | All Shipping workflows |
| REQ-SHP-026 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-042, 045–046 | — | API.md §58; EVENTS.md §§37–39 | Checkout, Order, support |
| REQ-SHP-027 | PRODUCT.md §§5.6, 17.2, 23 | REQ-BUS-013, 036, 042 | REQ-CART-022; REQ-PRC-025; REQ-PAY-022–023 | API.md §§22, 24; EVENTS.md | Shipping callers |
| REQ-SHP-028 | PRODUCT.md §§5.6, 17.2, 23 | REQ-BUS-013, 036, 042 | REQ-CART-022–023; REQ-PRC-025; REQ-PAY-023 | DATABASE.md §§51–52; API.md §24 | Recovery |
| REQ-SHP-029 | PRODUCT.md §§9.3–9.4, 16.5, 18.3, 23 | REQ-BUS-029, 035–036, 043, 045–046 | — | ARCHITECTURE.md §20.5; DATABASE.md §52; EVENTS.md §39 | Operations, support |
| REQ-SHP-030 | PRODUCT.md §§5.5–5.6, 15, 31.4, 34 | REQ-BUS-033–036, 043, 045 | — | SECURITY-STANDARDS.md §27; API.md §58 | Administration, operations |
| REQ-SHP-031 | PRODUCT.md §§14.8, 16.4–16.5, 24 | REQ-BUS-023, 030, 036, 048 | REQ-PAY-035 | GLOSSARY.md §8 | Order, Return, Payment |
| REQ-SHP-032 | PRODUCT.md §§14.8, 16.11, 24 | REQ-BUS-023, 030, 048 | REQ-PRC-021; REQ-PAY-034–035 | GLOSSARY.md §24 | Return, Payment |
| REQ-SHP-033 | PRODUCT.md §§5.5, 7.3–7.8, 15, 31, 34 | REQ-BUS-031–036, 043 | — | SECURITY-STANDARDS.md §§10–12, 27 | Administration, operations |
| REQ-SHP-034 | PRODUCT.md §§5.5, 7, 16.7 | REQ-BUS-032–033 | REQ-CUS-004–005, 039; REQ-PAY-030 | GLOSSARY.md §§13, 18; SECURITY-STANDARDS.md §§10–12 | Identity, Administration |
| REQ-SHP-035 | PRODUCT.md §§5.5, 16.7, 20, 34.5 | REQ-BUS-039, 042 | REQ-CUS-040; REQ-PAY-032 | SECURITY-STANDARDS.md §§14, 27, 35; API.md §§52–53, 60–61 | All Shipping interfaces |
| REQ-SHP-036 | PRODUCT.md §§13, 17, 23 | REQ-BUS-029, 045–047 | — | ARCHITECTURE.md §14; EVENTS.md | Event consumers |
| REQ-SHP-037 | PRODUCT.md §§13, 22, 35 | REQ-BUS-029, 032, 039, 042, 046–047 | REQ-PRD-052; REQ-CUS-047; REQ-INV-035; REQ-CART-031; REQ-PRC-033; REQ-PAY-039 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Contract consumers |
| REQ-SHP-038 | PRODUCT.md §§5.7, 16.9, 30.4 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md; UI.md §§40, 50, 64 | Checkout, account, administration |
| REQ-SHP-039 | PRODUCT.md §§10, 18.4, 20, 23 | REQ-BUS-038, 043, 045–046 | — | PERFORMANCE.md §§2, 21, 46–49, 58–60, 83; ARCHITECTURE.md §4.4 | All Shipping consumers |
| REQ-SHP-040 | PRODUCT.md §§5.5, 15, 17.4, 31, 34 | REQ-BUS-033–035 | — | SECURITY-STANDARDS.md §27; DATABASE.md §42 | Administration, operations |
| REQ-SHP-041 | PRODUCT.md §§9.3, 14.7, 18.3, 33–34 | REQ-BUS-029, 039, 043–044, 049 | REQ-CUS-024–025 | EVENTS.md; SECURITY-STANDARDS.md | Customer, support, reporting |
| REQ-SHP-042 | PRODUCT.md §§16.1, 16.5, 16.10, 20, 24 | REQ-BUS-014–016, 048, 054 | REQ-PRC-013–014, 019; REQ-PAY-044 | GLOSSARY.md §23 | Pricing, Order, Finance |
| REQ-SHP-043 | PRODUCT.md §§26.4, 35 | REQ-BUS-012–015, 017–023, 029–039, 042, 045–047 | REQ-CUS-036, 039–040; REQ-INV-023–026; REQ-CART-021–023; REQ-PRC-006, 014, 025; REQ-PAY-002, 022–023 | TESTING-STANDARDS.md; ACCESSIBILITY.md; PERFORMANCE.md | Verification evidence |

## 39. Open Product Decisions

`PRODUCT.md` currently contains exactly 30 Open Product Decisions. Six are materially relevant to Shipping and Fulfilment, and this Specification resolves none of them. All concrete values remain external until Approved.

| Product Decision | Shipping and Fulfilment boundary affected |
| --- | --- |
| Shipping provider, service levels, delivery areas, and fee policy | Determines future providers and supported service, geographic, and fee values; no value is selected here. |
| Free-delivery threshold and promotional treatment | Determines external Pricing and Promotion treatment; Shipping does not define the threshold or commercial treatment. |
| Cancellation eligibility and cutoff policy | Determines whether and when cancellation-related Shipping actions may be requested; no eligibility or cutoff is defined. |
| Returns, exchanges, and refund policy | Determines Return eligibility and reverse-logistics entry; Shipping defines only its future execution boundary. |
| Administrative role and permission matrix | Determines future access assignments; this Specification requires contextual Authorization without defining the matrix. |
| Production customer-service and operational escalation process | Determines future support and escalation workflow; this Specification defines safe exception and recovery boundaries only. |

## 40. Risks

| Risk | Implementation-neutral control direction |
| --- | --- |
| Stale delivery choice | Revalidate material choice context before commitment. |
| Invalid or stale Shipping Address | Consume governed Address context and fail safely when invalid or stale. |
| Incorrect quotation or Shipping Rate | Preserve applicability and freshness and reconcile with Pricing rather than choosing silently. |
| Shipping and Pricing authority leakage | Keep rate evidence separate from final commercial calculation. |
| Duplicate Shipment or provider effect | Correlate repeated operations and prevent duplicate effects. |
| Stale or replayed provider evidence | Validate, correlate, and reject or isolate untrusted evidence. |
| Provider outage | Preserve unavailable or uncertain outcomes and support recovery. |
| Dispatch uncertainty | Verify or reconcile before repetition or success representation. |
| Tracking inconsistency | Keep disagreement explicit and reconcile against authorized evidence. |
| False delivery status | Require sufficient governed delivery evidence. |
| Inventory authority leakage | Consume Inventory outcomes without mutating or redefining Stock truth. |
| Order authority leakage | Correlate to governed Order context without creating or rewriting Order truth. |
| Sensitive Data leakage | Minimize and protect delivery, tracking, provider, and operational information. |
| Unauthorized operational intervention | Require contextual server-side Authorization and proportional audit controls. |
| Reconciliation failure | Preserve history, observability, duplicate safety, and unresolved uncertainty. |
| Return or reverse-logistics authority leakage | Require governed Return context and keep eligibility and Refund external. |
| Historical truth corruption | Preserve Shipment and Order historical context against later mutable changes. |

## 41. Related Documents

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

## 42. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-03 | Draft | Established the initial Shipping and Fulfilment Domain authority, delivery, fulfilment, Shipment, provider, resilience, security, quality, Acceptance Criteria, and traceability baseline. |
| 1.0.0 | 2026-09-03 | Approved | Promoted the Shipping and Fulfilment Domain Specification to its Approved normative baseline without changing substantive Domain behavior or authority boundaries. |

## 43. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `SHP`, Approved Requirements are normative only within the Shipping and Fulfilment Domain scope, and governing-source precedence is preserved;
2. Requirement identifiers are unique, sequential, stable, and gap-free;
3. canonical terminology is correct and descriptive phrases have not become accidental canonical terms;
4. Shipping and Fulfilment authority is complete, unified, distinguishable, and implementation-neutral;
5. Shipment, Fulfilment, Dispatch, tracking, delivery, and provider evidence remain properly distinguished;
6. Product, Customer, Pricing, Inventory, Cart, Checkout, Payment, Order, Return, Administration, Notifications, Reporting, Tax, invoice, and Credit Note authority boundaries remain intact;
7. delivery choice, eligibility, rate or quotation, Money and Currency, stale context, revalidation, failure, uncertainty, duplicate, retry, replay, concurrency, reconciliation, and recovery behavior is complete;
8. security, Sensitive Data, least privilege, server-side Authorization, provider trust, and operational controls align with governance;
9. accessibility, performance, observability, Audit Records, events, Contracts, representations, and testing remain implementation-neutral and complete;
10. all 30 current Open Product Decisions were reviewed, the six materially Shipping and Fulfilment-relevant decisions are represented, and none is resolved;
11. every Requirement has exactly one clause-complete Acceptance Criteria row and one supported traceability row;
12. every Related Document exists and is relevant, no self-reference exists, and Revision History preserves the `0.1.0 Draft` row and contains exactly one `1.0.0 Approved` promotion row;
13. structure, Markdown, whitespace, final newline, and Git scope are valid; and
14. the final promotion diff changes only `specifications/domains/shipping/shipping-domain.md`, no Glossary amendment is required unless direct evidence changes that conclusion, and read-only Git and Markdown validation passes.
