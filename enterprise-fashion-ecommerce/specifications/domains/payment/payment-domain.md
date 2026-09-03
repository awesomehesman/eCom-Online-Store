---
title: Payment Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-03
authoritative: false
---

# Payment Domain

## 1. Purpose

This Specification defines the Payment Domain as the owner of authoritative Payment processing truth and governed payment outcomes. It establishes the semantic boundaries for Payment, Payment Attempt, Payment Provider evidence, Payment Redirect, Payment Authorization, Capture, Void, Settlement, Payment Transaction, Refund, Refund Transaction, and Chargeback without selecting an implementation or resolving an Open Product Decision.

This document uses requirement scope code `PAY`. As an Approved Domain Specification, its Requirements are normative within the Payment Domain scope, remain subordinate to higher-authority governing sources under the repository Decision Hierarchy, and do not resolve Open Product Decisions unless an Approved governing source explicitly does so.

## 2. Scope and Authority

### REQ-PAY-001 — Lifecycle, Authority, and Scope

The Payment Domain MUST own authoritative payment-processing truth within its Domain, including applicable lifecycle state and provider-evidenced outcomes. It MUST preserve the precedence of governing sources, use scope code `PAY`, and MUST apply this Approved Specification normatively only within the Payment Domain scope while remaining subordinate to higher-authority governing sources.

### REQ-PAY-002 — Payment Non-Authority

Payment MUST NOT become authoritative for Product, Product Variant, Category, Customer identity, Authentication, Authorization policy, Pricing, Cart, Checkout orchestration, Inventory, Stock Reservation, Order creation or history, Shipping, Fulfilment, Return policy, fraud policy, or Administration policy. Payment success MUST NOT imply that an Order exists unless Order creation is separately confirmed by the owning Domain, and Checkout initiation MUST NOT establish Payment success.

## 3. Domain Context

Payment accepts only governed input from an authorized coordinating capability and returns Payment-owned evidence. Pricing owns commercial calculation; Checkout owns purchase orchestration; Inventory owns availability and reservation; Order owns the durable commercial record; Customer and Identity own their respective identity facts; policy owners decide fraud, Return, cancellation, credit, tax, and administrative policy.

External Payment Provider terminology and behavior enter through governed Contracts and cannot redefine this Domain. Customer-facing views, Administration, reports, events, and Projections consume Payment truth without becoming authoritative.

## 4. Canonical Terminology

Canonical meanings and capitalization come from `GLOSSARY.md`. In particular, Payment is distinct from Payment Attempt and Payment Transaction; Payment Authorization is provider-backed payment evidence and is distinct from access-control Authorization; Capture, Void, Settlement, Refund, Refund Transaction, and Chargeback remain distinct; Payment Redirect is not payment proof; and Payment Transaction is not a Database Transaction.

No additional repository-wide term is introduced by this Specification.

## 5. Payment and Payment Attempt

### REQ-PAY-003 — Payment Identity and Lifecycle Truth

Each Payment MUST have stable identity, remain associated with the applicable governed commerce obligation, and expose distinguishable current Payment truth. Applicable canonical lifecycle meanings MUST remain distinguishable, while no additional state-machine enumeration is established here; state MUST NOT be inferred from UI, browser, Checkout, Order, analytics, or Projection state.

### REQ-PAY-004 — Payment Attempt Identity and Context

Each Payment Attempt MUST represent a distinct attempt to obtain an authoritative Payment outcome for a governed purchase context, have stable attempt identity, correlate unambiguously to the applicable Payment and Checkout or purchase context, and carry explicit Money and Currency. Payment MUST reject an invalid, ambiguous, mismatched, inaccessible, or stale governed context rather than fabricate an attempt outcome.

### REQ-PAY-005 — Payment Attempt State and Evidence

A Payment Attempt MUST preserve distinguishable non-terminal, terminal, failed, cancelled or abandoned where applicable, and uncertain outcomes without inventing a concrete state enumeration. It MUST retain sufficient authoritative provider evidence and correlation for verification, audit, and reconciliation, protect Sensitive Data, and MUST NOT promote stale or incomplete evidence to current truth.

## 6. Payment Initiation

### REQ-PAY-006 — Governed Payment Initiation

Payment MAY accept an initiation request from Checkout or another explicitly permitted authority only when the request has a valid governed purchase context, authoritative commercial input, explicit Money and Currency, and applicable trusted Principal and Authorization context. Payment MUST NOT trust client totals, browser state, UI success, supplied Customer identifiers, supplied payment status, arbitrary Currency, stale Pricing values, or stale purchase context as authority.

### REQ-PAY-007 — Initiation Outcome Separation

Accepted initiation MUST establish only the applicable Payment or Payment Attempt processing state, not Payment success, Checkout success, Order creation, Stock Reservation, Shipping selection, or Customer identity. Rejected, duplicate, stale, and uncertain initiation outcomes MUST remain distinguishable and safe for governed recovery.

## 7. Money and Currency

### REQ-PAY-008 — Money and Currency Integrity

Every Payment-domain monetary value, comparison, request, and outcome MUST carry explicit Currency and preserve precision-safe, non-floating-point Money semantics. Payment MUST reject silent Currency mixing, MUST NOT perform implicit Currency conversion, and MUST fail safely for unsupported Currency or invalid monetary input. Version 1 Customer-visible commercial Payment context MUST use ZAR where applicable. This Specification defines no storage type, scale, Minor Unit rule, rounding algorithm, foreign-exchange provider, or conversion policy.

### REQ-PAY-009 — Pricing Input Integrity

Payment MAY consume a governed Pricing total but MUST NOT calculate Product Price, determine Discounts, evaluate Promotions or Vouchers, establish Tax policy or Shipping charges, or silently alter authoritative Pricing totals. A material disagreement between payment-side monetary evidence and governed Pricing input MUST produce an explicit safe disagreement or reconciliation outcome rather than silently selecting either value.

## 8. Payment Provider Boundary

### REQ-PAY-010 — Provider-Neutral Integration Boundary

Payment Provider integration MUST remain bounded by an Approved provider Contract and project-owned semantics. No provider, gateway, method, card scheme, bank or payment rail, redirect provider, SDK, hosted-page vendor, provider field, or provider-specific lifecycle is selected by this Specification; provider terminology, transient response, or availability MUST NOT redefine Payment truth.

### REQ-PAY-011 — Authoritative Provider Evidence

Provider-dependent Payment truth MUST require provider evidence validated through governed server-side trust and Contract rules. Applicable Callback or Webhook evidence MUST have provider authenticity and payload integrity established according to the Approved provider Contract before it contributes to Payment truth; provider responses, redirects, browser returns, client-visible callbacks, polling results, client reports, local state, and UI state MUST NOT be trusted merely because they were received.

### REQ-PAY-012 — Malformed or Unverifiable Evidence

Malformed, mismatched, stale, replayed, unauthorized, or unverifiable provider evidence MUST be rejected or retained only as a distinguishable non-authoritative outcome for permitted investigation. It MUST NOT mutate trusted Payment state, expose provider internals or Sensitive Data, or trigger duplicate financial effects.

## 9. Payment Redirect

### REQ-PAY-013 — Payment Redirect Non-Authority

Where a Payment Redirect applies, redirect initiation, browser navigation, return URL arrival, query parameters, abandonment, or incomplete interaction MUST remain distinct from Payment success. A Payment Redirect MUST NOT itself establish an authoritative outcome, and Payment MUST support safe verification or reconciliation of uncertain redirect outcomes without assuming every Payment method uses redirects.

## 10. Payment Authorization

### REQ-PAY-014 — Payment Authorization Integrity

Where applicable, a Payment Authorization MUST represent Payment-owned, validated Payment Provider approval that reserves funds or confirms payment capability without necessarily transferring funds. It MUST remain distinct from Authentication, access-control Authorization, Capture, Settlement, Checkout completion, and Order confirmation; a requested or client-declared authorization MUST NOT be represented as confirmed provider evidence.

## 11. Capture

### REQ-PAY-015 — Capture Integrity

Where applicable, Capture MUST remain distinct from Payment Authorization and Settlement, and a requested Capture MUST remain distinct from its confirmed provider outcome. Capture processing MUST prevent duplicate effects, MUST preserve uncertainty after an unknown result, and MUST verify or reconcile that result before a repeat capable of duplicating an effect. Immediate versus delayed Capture remains unresolved.

## 12. Void

### REQ-PAY-016 — Void Integrity

Where applicable, Void MUST remain a Payment-owned cancellation of an uncaptured Payment Authorization, with the request distinct from the confirmed provider outcome. Void processing MUST be provider-neutral and duplicate-safe; this Specification establishes no eligibility, timing, or cancellation policy.

## 13. Settlement

### REQ-PAY-017 — Settlement Integrity

Where Settlement information is available, it MUST represent authoritative provider or Payment evidence of the final transfer of funds and remain distinct from Payment Authorization, Capture, Customer-facing Payment success, Checkout completion, and Order creation. Payment MUST NOT infer Settlement from Order, UI, or Projection state or establish timing, batching, or reconciliation cadence here.

## 14. Payment Transaction

### REQ-PAY-018 — Payment Transaction Integrity

Each Payment Transaction MUST have stable identity, explicit Money and Currency, an unambiguous relationship to the applicable Payment and Payment Attempt where relevant, a distinguishable operation and outcome, authoritative provider evidence where applicable, and sufficient protected context for audit and reconciliation. Sensitive Data MUST be minimized, and a Payment Transaction MUST NOT be confused with a Database Transaction or prescribe a persistence schema.

## 15. Payment Success

### REQ-PAY-019 — Authoritative Payment Success

Payment success MUST require sufficient authoritative evidence under governed Payment rules. Client-declared success, browser or UI success, a Payment Redirect return, duplicate provider notification, Pricing success, Checkout completion, or Order existence MUST NOT independently establish Payment success; timeout MUST NOT be interpreted as success or definitive failure without authoritative evidence, and an attempted Payment MUST NOT imply an Order exists.

## 16. Failure and Uncertainty

### REQ-PAY-020 — Explicit Payment Outcomes

Applicable initiation rejection, invalid governed context, unsupported Currency, invalid monetary input, provider unavailability, provider rejection, declined Payment Authorization, cancelled or abandoned interaction, timeout, duplicate request, stale attempt, malformed or unverifiable provider evidence, uncertain provider outcome, Capture uncertainty, reconciliation disagreement, and unauthorized protected action MUST produce distinguishable safe outcomes. Customer-facing wording remains outside this Specification.

### REQ-PAY-021 — Uncertainty Preservation

Unknown, pending, partial, delayed, or otherwise uncertain Payment outcomes MUST NOT be represented as success or definitive failure until authoritative evidence establishes the permitted state. Client cancellation, navigation away, timeout, stopped observation, or an aborted request MUST NOT prove provider-side processing stopped or negate a submitted Payment effect.

## 17. Idempotency, Duplicates, Retries, Replay, and Concurrency

### REQ-PAY-022 — Duplicate Financial-Effect Prevention

Duplicate, retried, replayed, stale, or concurrently processed initiation requests, provider notifications, reconciliation actions, Captures, Voids, and Refunds MUST NOT create duplicate Payment Authorizations, Captures, Voids, Refund Transactions, Orders, or other financial effects. Applicable duplicate-sensitive Contracts MUST preserve Idempotency Key semantics without defining key format, storage, locking, isolation, transaction, queue, or retry mechanisms.

### REQ-PAY-023 — Safe Retry and Replay Handling

Payment MUST correlate repeated evidence and requests to the applicable Payment and Payment Attempt, return or derive a consistent governed outcome where safe, reject unauthorized or conflicting replay, and verify uncertain prior effects before repetition. Concurrency or stale input MUST NOT silently overwrite authoritative accepted state or multiply side effects.

## 18. Reconciliation and Recovery

### REQ-PAY-024 — Payment Reconciliation

Payment MUST support governed reconciliation when an authoritative outcome is unknown, delayed, stale, internally conflicting, or disagrees with Payment Provider evidence. Reconciliation MUST compare authorized sources, preserve duplicate-effect safety and history, and produce a distinguishable resolved or still-uncertain outcome without inventing a cadence, batch, job, queue, table, or workflow.

### REQ-PAY-025 — Controlled Recovery and Intervention

Recovery after dependency failure and safe operational intervention, where permitted, MUST be explicit, server-side authorized, observable, duplicate-safe, and governed by Payment state. Material high-Risk intervention MUST create an appropriate Audit Record; recovery MUST NOT fabricate provider evidence, silently rewrite history, bypass another Domain, or convert uncertainty into success.

## 19. Checkout Boundary

### REQ-PAY-026 — Checkout Coordination Boundary

Payment MAY receive a governed initiation request from Checkout and return authoritative Payment evidence for coordination, but MUST NOT define Checkout lifecycle, own Cart validation, own final Pricing authority, own Shipping or Customer Address selection, or establish Checkout success by itself. Payment MUST preserve the Checkout correlation needed to distinguish stale or mismatched purchase context.

## 20. Order Boundary

### REQ-PAY-027 — Order Authority Boundary

Payment MAY produce validated evidence used as a prerequisite or trigger input for Order confirmation where Architecture permits, but MUST NOT create an Order, define Order lifecycle, create Order history, or rewrite an Order Snapshot. Payment success and Order existence MUST be independently confirmed by their owning Domains.

## 21. Inventory Boundary

### REQ-PAY-028 — Inventory Non-Authority

Payment MUST NOT own, infer, reserve, release, finalize, or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection. A Payment outcome MAY participate in a later governed Inventory coordination flow, but MUST NOT redefine Inventory authority or prove Inventory success.

## 22. Customer and Identity Boundary

### REQ-PAY-029 — Customer, Principal, and Identity Boundary

Payment MAY consume governed Principal, Customer, Account, or purchase-context references only as required, but MUST NOT define identity, own Authentication, establish Customer truth, define Roles or Permissions, or trust a supplied Customer identifier as authority. Customer and Account changes MUST NOT silently rewrite retained Payment history.

### REQ-PAY-030 — Protected Payment Authorization

Protected Payment operations MUST use trusted server-side Authorization for the current Principal, Resource, action, and Domain state. UI visibility, route or browser state, Role labels, client Permissions or Claims, supplied identifiers, provider possession, and Authentication alone MUST NOT authorize an action.

## 23. Security and Payment Data

### REQ-PAY-031 — Payment Data Minimization and Protection

Payment MUST use Approved hosted or tokenized handling that minimizes Payment-data exposure. Raw card data MUST NOT be stored by the platform, and raw CVV MUST NOT be stored, logged, redisplayed after entry, telemetered, placed in fixtures, screenshots, analytics, error reports, or any durable artifact. This Requirement selects no provider or technology and makes no PCI or other certification claim.

### REQ-PAY-032 — Sensitive Data, Secret, and Evidence Safety

Payment MUST protect applicable Sensitive Data, Secrets, credentials, provider credentials, tokens, payment identifiers, and abuse-sensitive information with least privilege and purpose limitation across storage, transmission, logs, errors, Contracts, Domain Events, Integration Events, Projections, Audit Records, analytics, exports, and support surfaces. Ordinary non-sensitive commercial data MUST NOT automatically be classified as Sensitive Data, and protected errors MUST disclose neither unnecessary data nor provider, security, or inaccessible Resource internals.

## 24. Fraud Boundary

### REQ-PAY-033 — Fraud Policy Non-Authority

Payment MUST NOT define fraud policy, provider, score, threshold, rules, or manual-review workflow. Where Approved governance requires fraud or abuse input, Payment MUST consume only a governed authoritative outcome, distinguish missing, stale, unavailable, and uncertain input, preserve explainability where permitted, Authorization, security, Audit evidence where appropriate, and authoritative commercial state; fraud signals or advice MUST NOT become Payment proof.

## 25. Refund

### REQ-PAY-034 — Refund Execution Boundary

Payment MAY own payment-side Refund execution truth only after receiving governed approved eligibility and amount input from the appropriate authority. Refund request, provider processing, and confirmed Refund outcome MUST remain distinguishable; a Refund Transaction MUST return an approved Money amount against a prior captured Payment without silently changing the historical Order Snapshot.

### REQ-PAY-035 — Return and Refund Policy Separation

Payment MUST NOT establish Return eligibility, Return window, refund eligibility, Refund amount policy, partial Refund policy, cancellation policy, Voucher restoration, Promotion or Discount reversal, gift-card treatment, store-credit treatment, or promotional-credit treatment. These unresolved policies MUST remain external until Approved, and Return, cancellation, Refund, and Refund Transaction MUST remain distinct.

## 26. Chargeback

### REQ-PAY-036 — Chargeback Boundary

Where future provider governance supports Chargeback information, Payment MUST preserve authoritative payment-side Chargeback truth, keep it distinct from Refund and Return, protect Sensitive Data and provider information, and support governed reconciliation and audit. This Specification establishes no dispute window, evidence workflow, provider process, liability policy, or operational SLA.

## 27. Administration and Operations Boundary

### REQ-PAY-037 — Governed Operational Payment Actions

Administration MAY invoke future Payment capabilities only through explicit protected workflows and MUST NOT become Payment authority or bypass Payment rules. Materially high-Risk operational actions MUST receive proportional server-side Authorization, confirmation or Human Approval Gate where governing policy requires it, reason and Audit Record evidence where applicable, without defining a Role or Permission matrix or operational SLA.

## 28. Events

### REQ-PAY-038 — Conditional Payment Events

A Payment Domain Event or Integration Event MUST exist only where Architecture, an Approved Contract, reliability needs, or synchronization governance requires it. Any such event MUST preserve Payment authority, compatibility, Sensitive Data protection, duplicate safety, replay safety, and non-authoritative consumer Projection semantics; this Specification defines no event name, topic, schema, broker, queue, payload, delivery guarantee, or choreography.

## 29. Contract Impacts

### REQ-PAY-039 — Payment Contract Boundary

Future Payment Contracts MUST preserve authoritative Payment truth, Money and Currency, Payment and Payment Attempt identity, provider-neutral semantics, success, failure and uncertainty distinctions, duplicate, retry and replay safety, server-side Authorization, security, compatibility, reconciliation, and explicit failure behavior. This Specification defines no URL, route, HTTP method or code, JSON shape, field, DTO, class, controller, database schema, provider payload, Webhook schema, or event schema.

## 30. Accessibility

### REQ-PAY-040 — Accessible Payment Outcomes

Customer-facing Payment states, failures, Payment Redirect transitions, recovery guidance, and confirmation surfaces MUST provide applicable WCAG 2.2 AA outcomes, including relevant keyboard operation, focus handling, understandable status and error communication, and assistive-technology support. This Requirement makes no certification or Level AAA claim and prescribes no local visual design.

## 31. Performance and Observability

### REQ-PAY-041 — Bounded and Observable Payment Delivery

Payment interactions and applicable Contracts MUST be bounded and observable according to governing performance standards. Timeout, degradation, dependency failure, and Payment Provider unavailability MUST remain distinguishable and MUST NOT convert partial or uncertain state into trusted success; this Specification establishes no SLA, SLO, latency, timeout, retry, throughput, device, or network target.

## 32. Audit

### REQ-PAY-042 — Proportional Payment Audit Evidence

Applicable material high-Risk Payment operations, including administrative intervention, reconciliation, Refund execution, security-sensitive change, provider configuration change where permitted, and materially risky corrective action, MUST produce proportional Audit Records sufficient for accountability and investigation. Ordinary Payment reads MUST NOT automatically be treated as high-Risk audit events.

## 33. Notifications, Reporting, and Projections

### REQ-PAY-043 — Non-Authoritative Representations

Customer communications, operational reporting, analytics, exports, and Projections MUST derive Payment state from authoritative Payment evidence, identify their source where applicable, protect Sensitive Data, and MUST NOT claim premature success, replace Payment records, or alter Payment truth. Delivery, analytics, or reporting failure MUST NOT change authoritative Payment state.

## 34. Tax, Invoice, and Credit Note Boundary

### REQ-PAY-044 — Commercial Document Policy Non-Authority

Payment MUST preserve applicable governed Money evidence needed by owning Domains without establishing Tax treatment, tax display, invoice or Credit Note policy, numbering, legal interpretation, or document technology. Unresolved tax, invoice, and Credit Note values MUST remain external, and Payment-side evidence MUST NOT rewrite confirmed commercial history.

## 35. Testing

### REQ-PAY-045 — Payment Verification Coverage

Verification evidence MUST cover applicable positive and negative flows; initiation; Money and Currency; Payment and Payment Attempt; provider evidence; Payment Redirect; Payment Authorization; Capture; Void; Settlement; Payment Transaction; success, decline, cancellation, abandonment, timeout and uncertainty; duplicates, retries, replay, concurrency and stale state; reconciliation and recovery; Refund; Chargeback boundary; Checkout, Order, Pricing, Inventory, Customer and Identity boundaries; Authorization; Sensitive Data; fraud non-authority; audit; events; Contracts; accessibility; performance; failure and recovery. No test framework, test-case identifier scheme, or numerical coverage target is selected.

## 36. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-PAY-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; scope is `PAY`; review can identify Payment-owned processing truth, governing-source precedence, and the Specification's normative authority only within the Payment Domain scope. |
| REQ-PAY-002 | Payment owns none of the listed external truths; Checkout initiation does not prove Payment success; Payment success alone does not prove an Order exists. |
| REQ-PAY-003 | A Payment retains stable identity, governed obligation association, and distinguishable current truth; no UI, browser, Checkout, Order, analytics, or Projection state independently changes it, and no invented state enumeration is required. |
| REQ-PAY-004 | Every attempt has stable identity, explicit Money and Currency, and correct Payment and purchase-context correlation; each invalid, ambiguous, mismatched, inaccessible, or stale context is rejected without fabricated outcome. |
| REQ-PAY-005 | Applicable terminal, non-terminal, failed, cancelled or abandoned, and uncertain outcomes remain distinguishable; evidence supports verification, audit, and reconciliation while protected data and stale evidence remain safe. |
| REQ-PAY-006 | Initiation proceeds only with governed purchase, commercial, Money, Currency, Principal, and Authorization context where applicable; every listed client-supplied or stale input is non-authoritative. |
| REQ-PAY-007 | Initiation creates only permitted Payment processing state; all listed downstream successes remain uncreated, and rejected, duplicate, stale, and uncertain outcomes are distinguishable and recoverable where permitted. |
| REQ-PAY-008 | Every monetary boundary preserves explicit Currency and precision-safe non-floating-point Money; mixing, implicit conversion, invalid amount, and unsupported Currency fail safely; applicable Version 1 Customer-visible context is ZAR; no prohibited implementation or policy is selected. |
| REQ-PAY-009 | Payment uses the governed total unchanged, performs none of the listed Pricing decisions, and exposes a mismatch for safe reconciliation without silently selecting a value. |
| REQ-PAY-010 | Provider integration remains behind governed, project-owned semantics; none of the prohibited provider choices is selected, and provider behavior cannot redefine Payment truth. |
| REQ-PAY-011 | Provider-dependent truth changes only after applicable authenticity and integrity validation under the Approved Contract; every listed client-visible or provider channel remains non-authoritative merely by arrival. |
| REQ-PAY-012 | Each malformed, mismatched, stale, replayed, unauthorized, or unverifiable item is rejected or marked non-authoritative, causes no trusted mutation or duplicate effect, and exposes no protected detail. |
| REQ-PAY-013 | Redirect initiation, navigation, return, query state, abandonment, and incompletion cannot prove success; uncertain outcomes support verification or reconciliation without assuming redirect use. |
| REQ-PAY-014 | Payment Authorization is provider-backed and distinct from Authentication, access-control Authorization, Capture, Settlement, Checkout, and Order; a request or client claim cannot confirm it. |
| REQ-PAY-015 | Capture request and confirmation, Payment Authorization, and Settlement remain distinct; duplicate and unknown outcomes cannot be blindly repeated; capture timing stays unresolved. |
| REQ-PAY-016 | Void remains Payment-owned, request and provider confirmation are distinct, repetition creates no duplicate effect, provider neutrality is preserved, and no eligibility or timing policy is created. |
| REQ-PAY-017 | Settlement derives only from authoritative evidence and remains distinct from authorization, capture, Customer-facing success, Checkout, and Order; no timing, batch, or cadence is selected. |
| REQ-PAY-018 | Every Payment Transaction preserves identity, Money, Currency, correct relationship, operation, outcome, applicable provider evidence, protected audit/reconciliation context, and distinction from Database Transaction without a schema design. |
| REQ-PAY-019 | Only sufficient governed authoritative evidence proves success; none of the listed client, redirect, duplicate, Pricing, Checkout, Order, or timeout conditions independently proves success or failure. |
| REQ-PAY-020 | Every applicable listed failure or denial produces a distinguishable safe outcome without invented Customer wording. |
| REQ-PAY-021 | Pending, unknown, partial, and delayed outcomes remain uncertain until authoritative evidence resolves them; client-side interruption neither proves provider stoppage nor negates an effect. |
| REQ-PAY-022 | Repetition, replay, staleness, and concurrency duplicate none of the listed financial effects; applicable Contracts preserve Idempotency Key semantics without selecting a prohibited mechanism. |
| REQ-PAY-023 | Repeated activity correlates correctly and is consistent where safe; unauthorized or conflicting replay is rejected; uncertain effects are verified before repetition; concurrent or stale input neither overwrites accepted truth nor multiplies effects. |
| REQ-PAY-024 | Unknown, delayed, stale, conflicting, and provider-disagreeing outcomes compare authorized evidence and preserve history and duplicate safety until explicitly resolved or retained as uncertain; no cadence or mechanism is selected. |
| REQ-PAY-025 | Permitted recovery is explicit, authorized, observable, state-governed, duplicate-safe and audited where high-Risk; it fabricates no evidence, rewrites no history, bypasses no owner, and promotes no uncertainty. |
| REQ-PAY-026 | Checkout may initiate and consume Payment evidence while Payment owns none of the listed Checkout, Cart, Pricing, Shipping, or Address concerns and preserves context correlation. |
| REQ-PAY-027 | Payment evidence may coordinate Order confirmation but creates no Order, lifecycle, history, or snapshot mutation; Payment and Order outcomes are independently confirmed. |
| REQ-PAY-028 | Payment owns, infers, reserves, releases, finalizes, or alters none of the listed Inventory facts; coordination does not prove Inventory success or shift authority. |
| REQ-PAY-029 | Permitted identity references are governed inputs only; Payment establishes none of the listed Customer or Identity truths, trusts no supplied identifier, and later Customer changes preserve Payment history. |
| REQ-PAY-030 | Every protected action requires server-side Authorization for current context; none of the listed UI, client, identity-label, supplied, provider, or Authentication facts authorizes by itself. |
| REQ-PAY-031 | Approved hosted or tokenized handling minimizes exposure; raw card data is not stored and raw CVV is absent from every listed durable or observable surface; no provider, technology, or certification is claimed. |
| REQ-PAY-032 | Every listed Sensitive Data, Secret, credential, token, identifier, and abuse-sensitive category is least-privilege and purpose-protected across all listed surfaces; ordinary data is not automatically sensitive and errors reveal no prohibited detail. |
| REQ-PAY-033 | Payment defines none of the listed fraud choices; each governed input preserves required controls, distinguishes missing or uncertain states, and cannot become Payment proof. |
| REQ-PAY-034 | Refund execution uses governed approved eligibility and amount, distinguishes request, processing and confirmation, creates a correct Refund Transaction against a captured Payment, and preserves the Order Snapshot. |
| REQ-PAY-035 | Payment establishes none of the listed Return, cancellation, Refund, reversal, restoration, or credit policies; unresolved values stay external and all four concepts remain distinct. |
| REQ-PAY-036 | Governed Chargeback information remains authoritative, protected, auditable, reconcilable, and distinct from Refund and Return without any prohibited policy or process choice. |
| REQ-PAY-037 | Administration uses explicit protected Payment capabilities without gaining Payment authority or bypassing Payment rules; materially high-Risk actions receive proportional server-side Authorization, confirmation or a Human Approval Gate where governing policy requires it, and reason and Audit Record evidence where applicable; no Role or Permission matrix or operational SLA is invented. |
| REQ-PAY-038 | Events exist only for a governed need and preserve authority, compatibility, protection, duplicate and replay safety, and Projection non-authority without any prohibited event design. |
| REQ-PAY-039 | Future Contracts preserve every listed Payment semantic, identity, Money, state, safety, security, compatibility, reconciliation, and failure obligation without defining any prohibited Contract or implementation detail. |
| REQ-PAY-040 | Applicable Customer-facing Payment states, redirects, failures, recovery, and confirmation provide WCAG 2.2 AA, keyboard, focus, understandable communication, and assistive-technology evidence without certification, AAA, or local design claims. |
| REQ-PAY-041 | Payment delivery is bounded and observable; timeout, degradation, dependency failure, and provider unavailability remain explicit and cannot create trusted success; no prohibited numeric target or profile is asserted. |
| REQ-PAY-042 | Each applicable listed high-Risk operation produces proportional investigation-ready Audit Record evidence, while an ordinary read is not automatically high-Risk. |
| REQ-PAY-043 | Every applicable communication, report, analytic, export, or Projection derives from protected authoritative evidence, identifies source where applicable, claims no premature success, replaces no record, and cannot alter truth through its own failure. |
| REQ-PAY-044 | Payment preserves governed Money evidence but creates no tax, invoice, Credit Note, numbering, legal, or technology policy; unresolved values remain external and history remains unchanged. |
| REQ-PAY-045 | Verification evidence covers every listed applicable functional, negative, boundary, resilience, security, quality, audit, event, and Contract concern without selecting a framework, test IDs, or numerical coverage target. |

## 37. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-PAY-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047–048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9, 19–21 | Payment governance |
| REQ-PAY-002 | PRODUCT.md §§13, 16.1–16.5 | REQ-BUS-015, 017, 021, 024, 029–030 | REQ-PRD-046–048; REQ-CUS-033–036; REQ-INV-003; REQ-CART-003; REQ-PRC-003 | ARCHITECTURE.md §§9, 20.2 | Cross-domain |
| REQ-PAY-003 | PRODUCT.md §§16.3, 17.1–17.2 | REQ-BUS-024–025, 028 | REQ-CUS-035; REQ-CART-020; REQ-PRC-020 | GLOSSARY.md §§9, 41 | Payment consumers |
| REQ-PAY-004 | PRODUCT.md §§14.4–14.5, 16.3 | REQ-BUS-012–014, 025 | REQ-CART-018, 020; REQ-PRC-018, 020 | GLOSSARY.md §§5, 9 | Checkout, Payment |
| REQ-PAY-005 | PRODUCT.md §§5.6, 16.3, 17.1–17.2 | REQ-BUS-025, 028, 035 | REQ-CUS-035 | SECURITY-STANDARDS.md; DATABASE.md §52 | Operations, support |
| REQ-PAY-006 | PRODUCT.md §§14.4–14.5, 16.1–16.3 | REQ-BUS-012–015, 024 | REQ-CUS-033, 035; REQ-CART-018, 020; REQ-PRC-018, 020 | API.md §§42–43 | Checkout |
| REQ-PAY-007 | PRODUCT.md §§14.5–14.6, 16.3–16.4 | REQ-BUS-013, 021, 024–025 | REQ-CART-019–020 | ARCHITECTURE.md §20.2 | Checkout, Order |
| REQ-PAY-008 | PRODUCT.md §§6, 16.1, 16.3, 20 | REQ-BUS-014, 024 | REQ-PRC-006 | GLOSSARY.md §§5, 35; DATABASE.md §24; POSTGRES.md §13 | All Payment consumers |
| REQ-PAY-009 | PRODUCT.md §§5.4, 16.1, 16.3 | REQ-BUS-012, 014–016, 024 | REQ-CART-016–018; REQ-PRC-002, 009–014, 020 | API.md §§42–43 | Pricing, Checkout |
| REQ-PAY-010 | PRODUCT.md §§9.8, 16.3, 24, 37.3 | REQ-BUS-024, 046, 048 | — | ARCHITECTURE.md §20.2; API.md; DECISIONS.md §§35, 38 | Provider adapters |
| REQ-PAY-011 | PRODUCT.md §§14.5–14.6, 16.3, 23 | REQ-BUS-024, 046 | REQ-CUS-035 | SECURITY-STANDARDS.md; API.md | Provider Contract |
| REQ-PAY-012 | PRODUCT.md §§5.5–5.6, 16.3, 23 | REQ-BUS-024–027, 039, 042 | REQ-CUS-035 | SECURITY-STANDARDS.md; API.md §§52–58 | Security, operations |
| REQ-PAY-013 | PRODUCT.md §§14.5–14.6, 16.3, 30.4 | REQ-BUS-024–025, 042 | REQ-CUS-035 | GLOSSARY.md §9; UI.md; ACCESSIBILITY.md | Checkout UI |
| REQ-PAY-014 | PRODUCT.md §§13, 16.3 | REQ-BUS-024–026, 028 | — | GLOSSARY.md §§9, 18 | Payment, Order |
| REQ-PAY-015 | PRODUCT.md §§16.3, 23 | REQ-BUS-025–026, 028 | — | GLOSSARY.md §9; API.md §24 | Payment, Order |
| REQ-PAY-016 | PRODUCT.md §§16.3, 24 | REQ-BUS-026, 028, 048 | — | GLOSSARY.md §9 | Payment, Order |
| REQ-PAY-017 | PRODUCT.md §§16.3–16.4 | REQ-BUS-024–025, 028 | — | GLOSSARY.md §9; ARCHITECTURE.md §20.2 | Finance, operations |
| REQ-PAY-018 | PRODUCT.md §§13, 16.3, 34 | REQ-BUS-026, 028, 035 | — | GLOSSARY.md §§9, 17; DATABASE.md | Operations, reporting |
| REQ-PAY-019 | PRODUCT.md §§5.4, 14.5–14.6, 16.3, 23 | REQ-BUS-021, 024–025, 046, 049 | REQ-CUS-035; REQ-CART-020; REQ-PRC-020 | SECURITY-STANDARDS.md; API.md | Checkout, Order, communications |
| REQ-PAY-020 | PRODUCT.md §§5.6, 16.3, 17.2, 30.4 | REQ-BUS-025, 042, 046 | — | API.md §§52–58; SECURITY-STANDARDS.md | All Payment workflows |
| REQ-PAY-021 | PRODUCT.md §§5.4–5.6, 16.3, 17.2 | REQ-BUS-021, 025, 042 | REQ-CART-023 | API.md §58 | Checkout, support |
| REQ-PAY-022 | PRODUCT.md §§14.5, 16.3, 23 | REQ-BUS-013, 026, 036 | REQ-INV-025; REQ-CART-022; REQ-PRC-025 | API.md §§22, 24; EVENTS.md | Payment callers |
| REQ-PAY-023 | PRODUCT.md §§5.6, 16.3, 17.2, 23 | REQ-BUS-013, 025–026, 036, 042 | REQ-CART-022–023; REQ-PRC-025 | DATABASE.md §§51–52; API.md §24 | Recovery |
| REQ-PAY-024 | PRODUCT.md §§9.3, 14.6, 16.3, 18.3, 23 | REQ-BUS-025, 035–036, 043, 045–046 | — | ARCHITECTURE.md §20.5; DATABASE.md §52 | Operations, support |
| REQ-PAY-025 | PRODUCT.md §§5.5–5.6, 15, 31.4, 34 | REQ-BUS-033–036, 043, 045 | — | SECURITY-STANDARDS.md §27; API.md §58 | Administration, operations |
| REQ-PAY-026 | PRODUCT.md §§13, 14.4–14.6, 16.1–16.3 | REQ-BUS-011–013, 021, 024–025 | REQ-CUS-033; REQ-INV-023, 025; REQ-CART-018, 020; REQ-PRC-018 | ARCHITECTURE.md §25.3; API.md §43 | Checkout |
| REQ-PAY-027 | PRODUCT.md §§14.5–14.6, 16.3–16.4, 17.4 | REQ-BUS-021–023, 025 | REQ-CUS-034–035; REQ-INV-024–025; REQ-CART-019–020; REQ-PRC-019–020 | ARCHITECTURE.md §§20.2, 40.5 | Order |
| REQ-PAY-028 | PRODUCT.md §§13, 16.2–16.3 | REQ-BUS-017–021, 026 | REQ-INV-002–016, 025; REQ-CART-014–015 | ARCHITECTURE.md §20.3 | Inventory |
| REQ-PAY-029 | PRODUCT.md §§7, 16.7, 17.4 | REQ-BUS-022, 032, 039 | REQ-CUS-004–005, 025, 033, 035 | SECURITY-STANDARDS.md §§10–12, 35 | Customer, Identity |
| REQ-PAY-030 | PRODUCT.md §§5.5, 7, 16.7 | REQ-BUS-032–033 | REQ-CUS-004–005, 039; REQ-PRC-026 | ARCHITECTURE.md §30.4; SECURITY-STANDARDS.md §§10–12 | All protected workflows |
| REQ-PAY-031 | PRODUCT.md §§16.3, 20, 23 | REQ-BUS-027, 039 | REQ-CUS-035, 040 | SECURITY-STANDARDS.md | Frontend, provider Contract |
| REQ-PAY-032 | PRODUCT.md §§5.5, 16.7, 20, 34.5 | REQ-BUS-027, 039, 042 | REQ-CUS-040; REQ-PRC-027 | SECURITY-STANDARDS.md §§14, 27, 35; API.md §§52–53, 60–61 | All Payment interfaces |
| REQ-PAY-033 | PRODUCT.md §§16.12, 24 | REQ-BUS-052 | REQ-CUS-042; REQ-PRC-022 | SECURITY-STANDARDS.md §§9, 27, 35 | Fraud, operations |
| REQ-PAY-034 | PRODUCT.md §§14.8, 16.11, 34 | REQ-BUS-023, 026, 030, 034–036 | REQ-PRC-019, 021 | GLOSSARY.md §§8–9, 24; API.md §45 | Return, Order, support |
| REQ-PAY-035 | PRODUCT.md §§16.6, 16.11, 24 | REQ-BUS-016, 030, 048 | REQ-PRC-021 | DECISIONS.md §§29, 35 | Product governance |
| REQ-PAY-036 | PRODUCT.md §§13, 16.3, 34 | REQ-BUS-028, 034–035, 039 | — | GLOSSARY.md §9; SECURITY-STANDARDS.md | Operations, finance |
| REQ-PAY-037 | PRODUCT.md §§5.5, 7.3–7.8, 15, 31, 34 | REQ-BUS-031–036, 043 | — | SECURITY-STANDARDS.md §§10–12, 27 | Administration, operations |
| REQ-PAY-038 | PRODUCT.md §§13, 17, 23 | REQ-BUS-026, 045–047 | — | ARCHITECTURE.md §14; EVENTS.md | Event consumers |
| REQ-PAY-039 | PRODUCT.md §§13, 22, 35 | REQ-BUS-024–028, 032, 039, 042, 046–047 | REQ-PRD-052; REQ-CUS-047; REQ-INV-035; REQ-CART-031; REQ-PRC-033 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Contract consumers |
| REQ-PAY-040 | PRODUCT.md §§5.7, 16.9, 30.4 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md; UI.md §50 | Checkout, account, administration |
| REQ-PAY-041 | PRODUCT.md §§10, 18.4, 20, 23 | REQ-BUS-038, 043, 045–046 | — | PERFORMANCE.md; ARCHITECTURE.md §4.4 | All Payment consumers |
| REQ-PAY-042 | PRODUCT.md §§5.5, 15, 17.4, 31, 34 | REQ-BUS-033–035 | — | SECURITY-STANDARDS.md §27; DATABASE.md §42 | Administration, operations |
| REQ-PAY-043 | PRODUCT.md §§9.3, 14.6–14.8, 18.3, 33–34 | REQ-BUS-039, 043–044, 049 | REQ-CUS-024–025 | EVENTS.md; SECURITY-STANDARDS.md | Customer, support, reporting |
| REQ-PAY-044 | PRODUCT.md §§16.10, 20, 24 | REQ-BUS-022, 048, 054 | REQ-PRC-013, 019, 021 | SECURITY-STANDARDS.md | Order, Finance |
| REQ-PAY-045 | PRODUCT.md §§26.4, 35 | REQ-BUS-013–014, 024–028, 030, 032–039, 042, 045–047, 052 | REQ-CUS-035, 039–040; REQ-INV-025; REQ-CART-020, 022–023; REQ-PRC-006, 020, 025 | TESTING-STANDARDS.md; ACCESSIBILITY.md; PERFORMANCE.md | Verification evidence |

## 38. Open Product Decisions

`PRODUCT.md` currently contains exactly 30 Open Product Decisions. Nine are materially relevant to Payment, and this Specification resolves none of them.

| Product Decision | Payment boundary affected |
| --- | --- |
| Initial payment methods and provider | Determines supported methods and provider Contract; this Specification remains provider-neutral. |
| Tax-inclusive display and invoice requirements | May affect the governed commercial context supplied to Payment but does not transfer Pricing, Tax, or invoice authority. |
| Cancellation eligibility and cutoff policy | Governs whether a cancellation-related payment action may be requested; Payment does not invent eligibility or timing. |
| Returns, exchanges, and refund policy | Governs eligibility and amount inputs; Payment owns only governed payment-side Refund execution truth. |
| Administrative role and permission matrix | Determines future administrative access boundaries for protected Payment operations; this Specification requires contextual server-side Authorization but does not define the Role or Permission matrix. |
| Production customer-service and operational escalation process | Determines future support and escalation workflows for uncertain, failed, reconciled, or operationally exceptional Payment outcomes; this Specification defines Payment truth and controlled intervention boundaries but does not define the support or escalation process. |
| South African tax-display, invoice, and credit-note policy | Governs commercial-document obligations external to Payment while Payment preserves applicable evidence. |
| Fraud-screening approach and manual-review workflow | Governs fraud input and intervention; Payment does not own fraud policy. |
| Gift cards, store credit, and promotional credit policy | Determines future credit treatment and authority; no credit behavior is defined here. |

Authorization duration, Capture timing, Settlement timing, Refund window, partial-Refund policy, Chargeback policy, reconciliation interval, numeric timeouts and retries, operational SLA, and other values not Approved by these decisions remain external and unresolved.

## 39. Risks

| Risk | Implementation-neutral control direction |
| --- | --- |
| False Payment success | Require sufficient validated provider evidence and preserve uncertainty. |
| Duplicate charge or financial effect | Correlate requests and evidence, preserve Idempotency Key semantics where applicable, and verify before repeating uncertain effects. |
| Currency or amount mismatch | Require explicit precision-safe Money and Currency and expose disagreement safely. |
| Stale commercial or purchase input | Validate governed Pricing and Checkout context before initiation and reject or reconcile stale state. |
| Provider uncertainty or delayed evidence | Preserve an uncertain outcome until verification or reconciliation resolves it. |
| Blind retry after an unknown result | Verify prior provider and Payment state before another effect-capable attempt. |
| Replay or concurrent mutation | Authenticate evidence, correlate attempts, reject conflicting replay, and prevent duplicated or overwritten effects. |
| Reconciliation failure | Keep disagreement explicit, observable, recoverable, and auditable where material. |
| Sensitive Data or Secret leakage | Minimize collection and redact or exclude protected data from all non-permitted surfaces. |
| Payment authority leaks into Checkout or Order | Require owning-Domain confirmation and keep Payment evidence separate from orchestration and Order creation. |
| Refund executes without governed eligibility or amount | Accept Refund execution only from the appropriate governed authority and preserve request/outcome distinction. |
| Fraud policy is invented inside Payment | Consume only Approved fraud outcomes and keep policy, thresholds, and review external. |
| Client or provider evidence is trusted incorrectly | Validate authenticity, integrity, correlation, authorization, and freshness at the trusted boundary. |
| Historical commercial truth is corrupted | Preserve immutable Order Snapshot ownership and append traceable Payment outcomes rather than rewriting history. |

## 40. Related Documents

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

## 41. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-02 | Draft | Established the initial Payment Domain authority, processing, provider, failure, recovery, security, quality, Acceptance Criteria, and traceability baseline. |
| 1.0.0 | 2026-09-03 | Approved | Promoted the Payment Domain Specification to its Approved normative baseline without changing substantive Domain behavior or authority boundaries. |

## 42. Final Validation

Before approval, material revision, or implementation reliance, reviewers MUST verify that:

1. metadata states `1.0.0 Approved`, lifecycle wording is consistent with normative Payment Domain scope, `authoritative: false` is retained, scope code `PAY` is correct, and governing-source precedence is preserved;
2. Requirement identifiers are stable, sequential, unique, and gap-free from `REQ-PAY-001` through `REQ-PAY-045`;
3. canonical terminology preserves every listed Payment concept and distinguishes Payment Authorization from access-control Authorization;
4. Payment authority, provider neutrality, Payment Attempt, Money and Currency, Payment Redirect, Payment Authorization, Capture, Void, Settlement, Payment Transaction, Refund, and Chargeback are complete and implementation-neutral;
5. Checkout, Order, Pricing, Inventory, Customer and Identity, Shipping, Return, fraud, and Administration boundaries preserve their owning authorities;
6. success, failure, cancellation, abandonment, timeout, uncertainty, duplicate, retry, replay, concurrency, stale state, recovery, and reconciliation outcomes are distinguishable and safe;
7. security, Sensitive Data, Secret, hosted or tokenized handling, raw card and CVV prohibitions, least privilege, and server-side Authorization align with governing standards;
8. accessibility, performance, observability, proportional Audit Records, events, Contracts, notifications, reporting, and testing remain complete without invented targets or mechanisms;
9. all 30 current Open Product Decisions were reviewed, the nine materially Payment-relevant decisions are accurately represented, and none is resolved here;
10. every Requirement has one clause-complete, observable Acceptance Criteria row and one semantically supported traceability row;
11. every Related Document exists and is relevant, no self-reference exists, and the Revision History preserves the `0.1.0 Draft` row and contains exactly one `1.0.0 Approved` promotion row;
12. the document has exactly one H1, sequential numbered H2 sections, valid tables, no empty section, malformed heading, unfinished marker, merge-conflict marker, trailing whitespace, or implementation detail; and
13. the final diff changes only `specifications/domains/payment/payment-domain.md`, requires no Glossary amendment unless direct evidence changes that conclusion, and passes read-only Git and Markdown validation.
