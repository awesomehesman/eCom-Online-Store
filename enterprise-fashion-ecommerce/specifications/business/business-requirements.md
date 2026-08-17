---
title: Business Requirements
version: 1.0.0
status: Approved
owner: Product
last_updated: 2026-08-17
authoritative: false
---

# Business Requirements

## 1. Purpose, Scope, and Authority

This Specification translates the Approved Product baseline into stable, testable business Requirements for the initial enterprise fashion e-commerce platform. It establishes the business outcomes, actors, Version 1 boundaries, cross-cutting obligations, Acceptance Criteria, and downstream handoffs required before detailed Domain Specifications and implementation-facing Specifications are completed.

This document is Approved and is normative only within its assigned business-Specification scope. Its `authoritative: false` metadata MUST remain false; approval does not make it a core Source of Truth.

This Specification refines `.ai/core/PRODUCT.md` without replacing it. It MUST NOT redefine canonical terminology, override `.ai/core/ARCHITECTURE.md`, weaken another governing Requirement, define a technical Contract, or own behavior assigned to a later Domain Specification. An implementation preference cannot resolve an Open Product Decision. Conflicts MUST be resolved through the Decision Hierarchy in `.ai/core/AGENTS.md`.

**Requirement scope code:** `BUS`

Requirement identifiers follow `REQ-BUS-NNN`. They are stable repository references governed by `.ai/core/DOCUMENTATION-STANDARDS.md`; retired identifiers are not reassigned and gaps are not closed by renumbering.

## 2. Governing Context

The initial Product is an online fashion-commerce platform for one South African business selling owned or directly managed clothing and related fashion Products through one primary storefront. The initial language is English and the initial Currency is South African Rand (`ZAR`). Customers use modern mobile and desktop web browsers, while authorized Staff Users operate catalogue, commercial, Inventory, Order, Payment, fulfilment, support, content, reporting, and platform-administration capabilities.

The platform must preserve trustworthy commercial state across Product discovery, Product Variant selection, Cart, Checkout, Payment, Order creation, fulfilment, Return, and Refund journeys. Staff operations must occur through governed product workflows rather than routine direct production-data manipulation or undocumented manual processes.

Possible future countries, currencies, languages, legal entities, sellers, marketplaces, physical stores, native applications, wholesale channels, subscriptions, loyalty programs, and other expansion described by Vision are not current Product commitments.

## 3. Business Problems

The business baseline addresses these approved problems:

- Customers need to discover relevant Products and understand Product Variant information, Price, availability, delivery, Return, and Payment expectations.
- Mobile, accessibility, validation, provider, and network failures can prevent Customers from completing or recovering critical journeys.
- Catalogue, pricing, content, and publication operations require controlled ownership and validation.
- Inaccurate Stock or unclear availability can cause Overselling and loss of trust.
- Checkout, Payment, Order, Shipment, Return, and Refund state can become ambiguous without authoritative evidence, correlation, and reconciliation.
- Staff Users need least-privilege operational workflows, searchable context, and Audit Records without routine developer or database intervention.
- Reporting and analytics must support decisions without replacing authoritative commercial records.
- Provider dependencies must not redefine core Product semantics or become hidden business authority.

## 4. Actors and Contexts

| Actor context | Approved business goal and boundary |
| --- | --- |
| Visitor | Discover and evaluate public Products, use a Cart, review public policies, and begin only those purchase or Account journeys permitted by Approved Product policy. |
| Registered Customer | Manage supported Account information, Addresses, Preferences, Wishlist, Cart, Orders, Shipment tracking, and supported post-purchase actions. |
| Store Administrator | Perform permitted store operations across catalogue, Categories, pricing, Promotions, Inventory, Orders, fulfilment, content, reporting, and Staff User administration. |
| Merchandiser or Content Editor | Manage permitted Product information, Categories, Collections, Product Media, merchandising, and content without receiving unrelated financial or security Permissions by default. |
| Inventory or Fulfilment Operator | Review Inventory, record permitted Stock Adjustments, process fulfilment, manage Shipments, and investigate Stock Reservation or fulfilment exceptions. |
| Customer Support Agent | Locate permitted Customer and Order context and perform only authorized support, recovery, cancellation, Return, or Refund actions. |
| Finance or Operations User | Review Payment, Refund, reconciliation, sales, tax, Settlement, and approved operational information and perform controlled financial operations. |
| Platform Administrator | Manage permitted Staff User, Role, Permission, configuration, integration, and high-Risk operational capabilities under enhanced control and audit. |
| External Provider | Supply only the capability and evidence defined by an approved provider Contract; it is not Product authority beyond that boundary. |

A Role is a collection of Permissions, not a separate actor. A Principal is the authenticated human or system actor associated with a security context. UI presentation, Role labels, Claims, or client state do not establish Authorization.

## 5. Business Goals

Version 1 aims to:

1. Provide a complete storefront for discovering and purchasing fashion Products.
2. Support governed Customer purchase journeys without resolving the open guest-checkout policy here.
3. Preserve authoritative Product Variant, Price, Inventory, Payment, and Order state.
4. Provide protected Staff User workflows for ordinary business operations.
5. Support fulfilment, Shipment, Notification, Refund, support, and reconciliation workflows established by Product policy.
6. Meet Approved accessibility, performance, security, privacy, testing, and observability Requirements.
7. Produce trustworthy operational and commercial evidence.
8. Preserve safe recovery and reconciliation paths for critical Customer and Staff User journeys.

These goals are qualitative outcomes. They do not establish conversion, revenue, availability, latency, volume, staffing, fraud, abandonment, or Return-rate targets.

## 6. Version 1 Business Scope

Version 1 includes the following Product-established capability areas, subject to unresolved policies in section 30:

- Storefront content, catalogue discovery, Categories, Collections, search, filtering, sorting, and Product detail.
- Product Variant and quantity selection, Product Media, attributes, Price, Discount, Promotion, Voucher, and availability presentation.
- Visitor and registered Customer contexts, Cart, Checkout, Account, Address, Wishlist, Order history, Preferences, and privacy choices where governed.
- Backend-authoritative pricing and Checkout revalidation.
- Inventory visibility, Stock Reservation, Stock Adjustment, Stock Movement, fulfilment, and Shipment operations.
- Payment initiation, validated provider outcome processing, explicit uncertainty, reconciliation, and applicable Refund processing.
- Order confirmation, history, status, support, cancellation, Return, and Refund workflows where Approved policy supports them.
- Staff User administration for catalogue, commercial, Inventory, fulfilment, Customer support, content, reporting, configuration, and access management within assigned Permissions.
- Accessibility, security, privacy, auditability, operational visibility, reporting, recovery, and support evidence.

Detailed eligibility, lifecycle states, calculations, provider choices, and operational policies remain owned by Approved Product Decisions and downstream Specifications.

## 7. Version 1 Non-Goals and Deferrals

PRODUCT.md explicitly excludes these capabilities from Version 1:

- Multi-seller marketplace operation.
- International multi-currency commerce.
- Multi-tenant software-as-a-service operation.
- Native iOS or Android applications.
- Complex warehouse-management or manufacturing capability.
- Wholesale or business-to-business commerce.
- Subscription commerce.
- Customer loyalty points or stored-value wallets.
- Social commerce as a separate Checkout channel.
- Real-time AI recommendations in the critical purchase path.
- Full omnichannel store Inventory and collection.
- Automated Returns logistics without an Approved provider and process.
- Multiple legal merchants within one deployment.
- Automated machine-learning dynamic pricing.
- Customer-to-customer resale or peer marketplace capability.
- Physical point-of-sale integration.
- Guaranteed same-day delivery.

An Open Product Decision is unresolved rather than an exclusion. A deferred capability is not Approved merely because it is technically possible. An unselected technology is an Architecture or implementation concern and is not a business non-goal.

## 8. Customer Journey Baseline

The business journey is:

**Discovery → Product evaluation → Product Variant selection → Cart → Checkout → Payment → Order confirmation and status → fulfilment and delivery → supported post-purchase handling**

The journey MUST preserve these business outcomes:

- Discovery exposes only publishable, Customer-visible Product information and supports recovery from empty or failed search and filtering.
- Evaluation presents governed Product, Product Variant, Price, availability, delivery, and policy information without unsupported claims.
- Cart accepts a valid Product Variant and quantity but does not guarantee Price or Stock until trusted revalidation.
- Checkout revalidates authoritative Price, Discount, availability, delivery, and applicable policy before an irreversible commitment.
- Payment pending, unknown, failed, and confirmed outcomes remain distinguishable.
- Confirmation occurs only when trusted Payment and Order outcomes support it.
- Post-purchase status remains consistent with authoritative Order, Payment, Shipment, Return, and Refund state.

Business-significant degraded paths include unavailable or changed Product Variant state, changed Price, validation rejection, Authorization denial, interrupted Checkout, duplicate submission, Payment decline, pending or unknown Payment, provider failure, dependency failure, and stale or partial state. Recovery MUST avoid representing uncertainty as success or creating duplicate commercial effects.

## 9. Staff User Journey Baseline

An authenticated Staff User accesses only operations permitted to the current Principal. Supported business journeys may include:

- Creating, validating, updating, and publishing permitted catalogue and content information.
- Managing governed Price, Discount, Promotion, and Voucher information.
- Reviewing Inventory and recording permitted Stock Adjustments with reasons and history.
- Reviewing Orders, processing fulfilment, creating Shipments, and investigating exceptions.
- Locating permitted Customer, Payment, Order, Shipment, Return, and Refund context for support or reconciliation.
- Performing authorized cancellation, Return, Refund, resend, repair, or escalation actions through controlled workflows.
- Reviewing reports, operational state, Audit Records, and reconciliation evidence.
- Managing Staff Users, Roles, Permissions, and approved configuration only where separately authorized.

Authentication does not grant a capability by itself. Server-side Authorization, least privilege, current Domain invariants, confirmation where Risk requires it, and applicable Audit Records remain mandatory. This Specification does not invent organizational departments, approval chains, staffing levels, or new Roles.

## 10. Business Capability Model

| Capability | Business responsibility |
| --- | --- |
| Catalogue and Category | Govern Product, Product Variant, Category, Collection, Product Media, attribute, content, and publication outcomes. |
| Search and discovery | Help Customers locate relevant Customer-visible Products without becoming Product, Price, or Inventory authority. |
| Customer | Support governed Visitor, Account, Address, Preference, privacy, Wishlist, and support contexts. |
| Cart and Checkout | Preserve shopping intent, validate input, recalculate trusted totals, revalidate availability, and coordinate the purchase journey. |
| Pricing | Own authoritative Price, Discount, Promotion, Voucher, Money, and Currency outcomes under Approved policy. |
| Inventory | Own Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement truth. |
| Order | Own the durable commercial record and governed Order lifecycle. |
| Payment | Own Payment Attempts and platform Payment state based on validated Payment Provider evidence. |
| Shipping and fulfilment | Own governed delivery choices, fulfilment work, Shipment state, tracking, and provider reconciliation. |
| Return and Refund | Preserve distinct physical Return, commercial eligibility, Refund, and Refund Transaction outcomes. |
| Administration | Expose permitted operational capabilities without bypassing Domain ownership or server-side Authorization. |
| Evidence and reporting | Provide Audit Records, correlation, reconciliation, operational visibility, and non-authoritative analytical Projections. |

This capability model describes business responsibilities, not Modules, services, schemas, Endpoints, or deployment units.

## 11. Requirement Model and Rules

Each Requirement below has one stable identifier and one coherent primary obligation. Requirement details state intent and constraints without selecting implementation mechanics. Acceptance Criteria in section 25 provide observable evidence for related Requirements.

The following rules apply throughout:

- Approved canonical terminology MUST retain the meaning defined in `.ai/core/GLOSSARY.md`.
- Client input, UI state, caches, search indexes, reports, and analytics MUST NOT become authoritative commercial truth.
- A failure, retry, replay, Callback, duplicate request, or concurrent action MUST NOT create an invalid or duplicate business effect.
- An unresolved policy MUST remain explicit and MUST NOT be converted into implementation behavior without applicable Product governance.
- Requirements in an owning Domain Specification MAY add precision but MUST NOT weaken this business baseline or a higher-authority source.

## 12. Market, Storefront, Catalogue, and Discovery Requirements

### REQ-BUS-001 — Initial Operating Context

The Product MUST support one primary South African online fashion storefront in English using ZAR as its initial Currency. Expansion to another market, language, Currency, legal merchant, seller model, or commerce channel MUST require applicable Product and Architecture governance.

### REQ-BUS-002 — Responsive Storefront Access

Customers MUST be able to complete supported critical storefront journeys through modern mobile and desktop web contexts. Responsive behavior MUST preserve information and functionality without creating a separate Product policy by device.

### REQ-BUS-003 — Customer-Visible Catalogue

Customers MUST be able to browse Customer-visible Products through Approved catalogue, Category, Collection, and merchandising structures. Draft, unauthorized, or otherwise non-publishable content MUST NOT be presented as available Product truth.

### REQ-BUS-004 — Product Discovery

Customers MUST be able to discover Products using the Product-established navigation, search, filtering, and sorting capabilities. Empty, unavailable, failed, stale, and partial discovery outcomes MUST be represented honestly and recoverably where applicable.

### REQ-BUS-005 — Product and Product Variant Evaluation

Before purchase commitment, Customers MUST receive the governed Product and Product Variant information needed to evaluate a purchase, including applicable Product Media, attributes, Price, availability, delivery, and policy information. Presentation MUST NOT create unsupported Product claims or conceal material uncertainty.

### REQ-BUS-006 — Product Variant Selection

Where a Product has Product Variants, a Customer MUST select a valid, sellable Product Variant and quantity before the item can enter a purchase commitment. Invalid or unavailable selection MUST be rejected with an understandable recovery path.

## 13. Customer, Account, Cart, and Checkout Requirements

### REQ-BUS-007 — Customer Context

The Product MUST distinguish unauthenticated Visitor context from authenticated Customer context and expose only capabilities permitted by current Approved policy. This Requirement does not resolve whether guest Checkout is mandatory, optional, or unavailable.

### REQ-BUS-008 — Account and Address Management

Registered Customers MUST be able to manage supported profile, Address, Preference, privacy, Wishlist, and Order-access capabilities without altering historical Order snapshots or gaining access to another Principal's Resources.

### REQ-BUS-009 — Cart Intent

The Cart MUST preserve supported shopping intent for selected Product Variants and quantities while making clear that Cart Price, Discount, delivery, and availability remain subject to trusted Checkout revalidation.

### REQ-BUS-010 — Cart Mutation Safety

Adding, changing, removing, or retrying a Cart action MUST produce a consistent Cart outcome and MUST NOT create unintended duplicate quantities or imply a final Stock Reservation unless the owning Inventory policy establishes one.

### REQ-BUS-011 — Checkout Input and Validation

Checkout MUST collect and validate only the Customer, Address, delivery, Promotion or Voucher, and Payment information required by Approved Product policy. Validation rejection MUST preserve recoverable input where safe and explain the correction needed without exposing Sensitive Data or provider internals.

### REQ-BUS-012 — Authoritative Checkout Revalidation

Before commercial commitment, Checkout MUST use trusted server-side outcomes to revalidate applicable Price, Discount, Promotion, Voucher, tax, delivery, Product Variant sellability, and Inventory availability. Material changes MUST be shown to the Customer before continuation.

### REQ-BUS-013 — Checkout Recovery and Duplicate Protection

Interrupted, retried, resumed, or duplicate Checkout activity MUST preserve a recoverable and explainable outcome and MUST NOT create duplicate Orders, Stock effects, or financial effects.

## 14. Money, Pricing, and Inventory Requirements

### REQ-BUS-014 — Money and Currency

Every Customer-visible and commercial monetary value MUST preserve Money semantics with an explicit Currency. Version 1 Customer-visible Price and commercial totals MUST use ZAR; this Specification does not establish Currency conversion.

### REQ-BUS-015 — Authoritative Price

Authoritative Price, Discount, Promotion, Voucher, tax, delivery charge, and total calculations MUST be produced by trusted server-side ownership. Client-provided or analytically derived monetary values MUST NOT be authoritative.

### REQ-BUS-016 — Pricing Transparency

Customers MUST receive understandable Price, Discount, Promotion, Voucher, delivery, tax, and total changes before commitment where applicable. Each applicable Promotion MUST have governed definitions for validity, eligibility, thresholds, usage limits, stackability, exclusions, calculation order, and Refund treatment before its behavior becomes binding in production. The required policy completeness does not resolve the values of those policies; unresolved rounding, tax, stacking, priority, eligibility, threshold, limit, and Refund-treatment outcomes MUST remain deferred to Product governance and the Pricing Domain Specification.

### REQ-BUS-017 — Inventory Authority

Inventory MUST remain authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement at Product Variant level. Other contexts MAY consume Inventory Projections but MUST NOT redefine Inventory truth.

### REQ-BUS-018 — Availability and Overselling Protection

Purchase commitments MUST use current trusted Available-to-Sell outcomes and concurrency-safe Inventory behavior that prevents Overselling. Stale, cached, search, reporting, or frontend availability MUST NOT authorize a commitment.

### REQ-BUS-019 — Stock Reservation Integrity

Where a purchase journey creates or changes a Stock Reservation, creation, expiry, release, failure, and finalization MUST preserve Available-to-Sell correctness and remain traceable. This Specification does not establish Stock Reservation duration, warehouse allocation, back-order policy, Safety Stock, or replenishment policy.

### REQ-BUS-020 — Stock Change Evidence

Material Stock Adjustments and Stock Movements MUST retain the authorized actor or system context, reason or source, time, quantity effect, and appropriate Audit Records. Ordinary UI or reporting correction MUST NOT rewrite Inventory history.

## 15. Order, Payment, Shipping, Return, and Refund Requirements

### REQ-BUS-021 — Order Creation

An Order MUST be created only through the governed Checkout outcome and MUST become the durable commercial record of the applicable Customer, Order Items, Money values, delivery details, Payment state, and fulfilment state. An uncertain Payment outcome MUST NOT be represented as a confirmed completed purchase.

### REQ-BUS-022 — Order Snapshot Integrity

Confirmed Order Items, Product descriptions needed for history, Addresses, Prices, Discounts, taxes, delivery values, and totals MUST retain the commercial snapshot applicable at confirmation. Later Product, Customer, or pricing changes MUST NOT silently rewrite that history.

### REQ-BUS-023 — Order Lifecycle and History

Order transitions MUST be explicit, authorized, historically traceable, and consistent with separate Payment, fulfilment, cancellation, Return, and Refund concerns. Confirmed Orders MUST NOT be ordinarily deleted; correction, cancellation, Return, and Refund activity MUST preserve the historical commercial record. Governed retention, anonymization, and legal obligations MAY affect the storage lifecycle without rewriting commercial history. Detailed states and transition rules remain owned by the Order Domain Specification.

### REQ-BUS-024 — Authoritative Payment Evidence

Provider-dependent Payment success MUST be established only from validated Payment Provider evidence processed through the trusted backend. Applicable provider Callback or Webhook evidence MUST be authenticated, signature-validated, or otherwise cryptographically validated according to the approved provider Contract, and its payload integrity MUST be established before it can determine provider-dependent Payment truth. Unvalidated Callback or Webhook data, a Payment Redirect, browser result, query parameter, client report, local state, or UI callback state MUST NOT be Payment proof.

### REQ-BUS-025 — Payment Uncertainty

Pending, unknown, failed, declined, cancelled, and confirmed Payment outcomes MUST remain distinguishable where applicable. Frontend or client cancellation, navigation away, timeout, stopped observation, or an aborted request MUST NOT prove that provider-side processing stopped or reverse or negate an already-submitted Payment-side effect. An unknown or otherwise uncertain provider outcome MUST remain unresolved until trusted verification or reconciliation establishes the permitted next state.

### REQ-BUS-026 — Duplicate Financial-Effect Prevention

Duplicate, replayed, retried, or concurrently processed Payment requests, Callbacks, Webhooks, or reconciliation actions MUST NOT create duplicate Payment Authorizations, Captures, Voids, Refund Transactions, Orders, or other financial effects.

### REQ-BUS-027 — Payment Data Protection

The Product MUST use approved hosted or tokenized Payment handling that minimizes Payment-data exposure. Raw card data MUST NOT be stored by the platform. Raw CVV MUST NOT be stored, logged, redisplayed after entry, telemetered, placed in fixtures, screenshots, analytics, error reports, or any other durable artifact. This Requirement does not claim PCI certification or define provider or storage technology.

### REQ-BUS-028 — Payment Concept Integrity

Payment, Payment Attempt, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, and Settlement MUST remain distinguishable where applicable. A Payment Transaction MUST NOT be confused with a Database Transaction.

### REQ-BUS-029 — Fulfilment and Shipment

Authorized Staff Users MUST be able to process supported fulfilment and Shipment work through controlled workflows. Customer-visible tracking and delivery state MUST remain reconcilable with trusted Shipment and provider evidence without exposing internal provider details.

### REQ-BUS-030 — Return and Refund Distinction

A Return, cancellation, Refund, and Refund Transaction MUST remain distinct. Supported eligibility, approval, Inventory disposition, provider processing, Order financial effect, and Customer communication MUST follow Approved policy and the owning Domain Specifications; this Requirement does not create a Return or Refund policy.

## 16. Administration, Authorization, and Evidence Requirements

### REQ-BUS-031 — Governed Staff Operations

Authorized Staff Users MUST be able to perform ordinary supported business operations through controlled product workflows without routine direct production-database modification or code deployment.

### REQ-BUS-032 — Authentication and Server-Side Authorization

Protected Customer and Staff User operations MUST require applicable Authentication and server-side Authorization for the current Principal, Resource, action, and Domain state. UI visibility, route state, Role labels, Permissions, or Claims MUST NOT substitute for the authoritative decision.

### REQ-BUS-033 — Least Privilege and High-Risk Actions

Staff User access MUST be least-privilege and separated according to approved responsibilities. Destructive, financial, security-sensitive, privacy-affecting, bulk, or otherwise high-Risk operations MUST preserve applicable confirmation, separation, Human Approval Gate, and audit Requirements without inventing a new approval chain here.

### REQ-BUS-034 — Audit Records

Material commercial, privileged, security-sensitive, Inventory, Payment, Order, Return, Refund, configuration, and policy actions MUST produce appropriate Audit Records that support accountability, investigation, reconciliation, and authorized review.

### REQ-BUS-035 — Operational Correlation and Reconciliation

Staff Users with applicable Permission MUST be able to correlate relevant Checkout, Payment Attempt, Payment Transaction, Order, Shipment, Notification, Inventory, Return, and Refund evidence and identify unresolved discrepancies without treating analytics as commercial truth.

### REQ-BUS-036 — Controlled Recovery

Supported repair, resend, retry, cancellation, reconciliation, and escalation actions MUST be explicit, authorized, idempotent where duplicate effects are harmful, auditable where material, and constrained by the owning Domain rules.

## 17. Cross-Cutting Business Requirements

### REQ-BUS-037 — Accessibility Outcome

Customer-facing and Staff User capabilities MUST target WCAG 2.2 AA and provide equivalent access to critical journeys. This target is a Requirement, not a certification claim, and does not silently make Level AAA criteria mandatory.

### REQ-BUS-038 — Performance Outcome

Critical Customer journeys MUST meet the Approved performance Requirements and evidence model governed by `.ai/core/AGENTS.md` and `.ai/frontend/PERFORMANCE.md`. This Specification does not create a percentile, device profile, network profile, route budget, bundle budget, backend latency target, capacity target, SLA, or SLO.

### REQ-BUS-039 — Sensitive Data and Secret Protection

Sensitive Data and Secrets MUST be collected, accessed, displayed, retained, transmitted, logged, exported, and deleted only within approved purposes, Permissions, lifecycle controls, and security boundaries. Errors, analytics, support tools, and operational evidence MUST NOT expose unnecessary Sensitive Data.

### REQ-BUS-040 — Privacy and Consent

Customer data collection and use MUST have a defined Product purpose. Transactional necessity, Preference, and marketing consent MUST remain distinguishable, and purchase MUST NOT imply marketing consent.

### REQ-BUS-041 — Qualified Compliance Validation

Applicable South African privacy, consumer, tax, Payment, commercial, and contractual constraints MUST be validated by qualified accountable stakeholders before production reliance. This Specification MUST NOT be represented as legal, tax, PCI, privacy, or regulatory certification.

### REQ-BUS-042 — Explainable Failure and Degraded State

Customer and Staff User experiences MUST distinguish success, failure, pending, unknown, stale, partial, denied, and unavailable states where materially different. Recovery guidance MUST be based on known state and MUST NOT expose Sensitive Data, inaccessible Resource existence, or provider internals.

### REQ-BUS-043 — Operational Visibility

The business MUST have authorized visibility into material Order, Payment, Refund, Inventory, fulfilment, Shipment, Notification, security, and dependency outcomes needed for ordinary operation, reconciliation, support, and recovery.

### REQ-BUS-044 — Reporting and Projection Integrity

Approved reports and analytical Projections MUST use explicit definitions and traceable sources. They MUST NOT replace authoritative Order, Payment, Refund, Price, Customer, or Inventory records or silently change Product behavior.

### REQ-BUS-045 — Business Continuity and Recovery Outcomes

Critical purchase and operational capabilities MUST have governed failure detection, recovery, reconciliation, and support outcomes proportionate to their business Risk. Exact recovery objectives, backup schedules, infrastructure mechanics, and operational thresholds remain owned by applicable Architecture and operational governance.

### REQ-BUS-046 — Provider Boundary

An External Provider MUST remain bounded to its approved Contract and evidence authority. Provider terminology, transient response, SDK behavior, or availability MUST NOT redefine Product semantics, Domain ownership, or Customer-visible success.

### REQ-BUS-047 — Requirement Traceability

Material downstream Specifications, Contracts, implementation, tests, and acceptance evidence SHOULD reference the applicable `REQ-BUS-NNN` identifier where this materially improves traceability. A reference MUST NOT be used to claim compliance without supporting evidence.

### REQ-BUS-048 — Governed Product Change

A material change to business scope, market, policy, actors, Customer eligibility, commercial semantics, or Version 1 goals and non-goals MUST follow Product Decision governance, update affected governing sources and Specifications, and preserve traceability before the changed behavior becomes current Product truth.

### REQ-BUS-049 — Transactional Notifications and Customer Communications

Applicable business-significant Customer communications, including Order confirmation or status, Payment, fulfilment or Shipment, Return, Refund, and material failure, retry, or reconciliation outcomes, MUST reliably represent authoritative state and remain traceable where operational investigation requires it. Communication content MUST NOT claim success before authoritative state exists, and a Notification delivery or provider failure MUST NOT alter authoritative commercial state. Channels, providers, retry counts, delivery targets, channel priorities, and template technology remain governed elsewhere.

### REQ-BUS-050 — Content Lifecycle and Product Media

Product-facing content MUST use governed creation, update, and publication outcomes; applicable policy content MUST be versioned, and Draft or unapproved content MUST NOT be published. Product and campaign claims MUST remain accurate and supportable, Product Media MUST meet Approved Product and Design System Requirements, and stale or misleading published content MUST be correctable. Material publication MUST remain authorized and traceable. This Requirement does not select a CMS, workflow engine, approval chain, publication schedule, image service, or asset pipeline.

### REQ-BUS-051 — Customer Registration, Credentials, and Sessions

The Product MUST support governed Customer registration and applicable Account recovery where Product policy establishes them. Credential and password handling MUST follow Security governance; Session establishment, expiry, and revocation and failed Authentication outcomes MUST protect Account access, prevent cross-Principal access, and MUST NOT allow Session state to bypass server-side Authorization. This Requirement does not select an Identity Provider, protocol, token or cookie strategy, MFA policy, password algorithm, or Session storage technology.

### REQ-BUS-052 — Fraud and Abuse Protection

Applicable registration, Authentication, Voucher, Promotion, Checkout, Payment, Refund, and administration workflows MUST support governed detection, restriction, and escalation of suspicious or abusive behavior. Such protections MUST preserve Authorization, auditability, and authoritative commercial state, MUST NOT create unauthorized commercial effects, and fraud signals or advice MUST NOT become Payment proof. False-positive recovery and manual review remain governed by applicable Product, Security, and Operations policy; this Requirement does not select a vendor, model, threshold, rule engine, review target, or blocking policy.

### REQ-BUS-053 — Operational Export Integrity

Exports containing commercial or operational records MUST require explicit Authorization, derive from appropriate authoritative sources, preserve applicable definitions and context, and MUST NOT become a competing source of truth. Sensitive Data MUST be minimized and protected, and material export generation and use MUST be auditable. Analytical reports remain non-authoritative Projections where applicable. Format, size, retention, storage, scheduling, and delivery channel remain governed elsewhere.

### REQ-BUS-054 — Tax, Invoice, and Commercial Document Readiness

Applicable tax display, invoice requirements, invoice numbering, credit-note requirements, and other commercial-document obligations MUST be defined and qualified where necessary before production reliance. Confirmed records MUST preserve applicable tax values, and client calculations MUST NOT establish authoritative tax or invoice totals. The specific policy values remain subject to Product governance and qualified legal or tax validation; this Requirement does not define a tax formula, rate, numbering format, legal interpretation, or invoice technology.

## 18. Security, Privacy, Accessibility, and Performance Boundaries

This Specification establishes business outcomes rather than implementation controls:

- `.ai/core/SECURITY-STANDARDS.md` governs security, privacy, Authentication, Authorization, Sensitive Data, Secret, Payment, Human Approval Gate, and Security Exception controls.
- `.ai/frontend/ACCESSIBILITY.md` governs implementation of the WCAG 2.2 AA target. Neither this Specification nor automated evidence claims certification.
- `.ai/frontend/PERFORMANCE.md` and the applicable core requirements govern performance evidence. Approved Core Web Vitals values are referenced from their governing source rather than duplicated here.
- `.ai/core/TESTING-STANDARDS.md` governs verification levels, evidence, release consequences, and Testing Exceptions.

Applicable POPIA, consumer, tax, Payment, privacy, and other obligations remain constraints requiring qualified validation. No statement in this Specification is a legal conclusion.

## 19. Success and Guardrail Measures

The Product must support measurement of the following without treating analytics as authoritative commercial state:

| Measure group | Measures requiring governed definitions | Target status |
| --- | --- | --- |
| Customer journey | Discovery engagement, search use and zero-result rate, Product-detail engagement, add-to-Cart rate, Checkout progression and completion, Payment outcomes, purchase conversion, repeat purchase, Wishlist use, and support contact by journey stage. | No numerical target Approved here. |
| Commercial | Gross Revenue, Net Revenue, Average Order Value, units per Order, Discount and Promotion impact, cancellation and Refund rates, Product and Category performance, sell-through, and stock-out impact. | No numerical target Approved here. |
| Operational | Order-to-fulfilment time, Shipment creation, tracking freshness, Notification delivery, Payment reconciliation exceptions, Inventory discrepancies, support resolution, and administrative errors. | No numerical target Approved here. |
| Quality | Approved performance evidence, accessibility evidence, API reliability, Checkout and Payment errors, production defects, recovery evidence, and security finding remediation. | Governed standards own applicable thresholds. |
| Guardrails | Accessibility failures, duplicate or uncertain Payment, Overselling or Stock Reservation discrepancies, complaints, support demand caused by change, performance regression, security/privacy/consent incidents, and Promotion misuse or margin impact. | Tolerances remain unresolved unless an Approved source establishes them. |

An improvement in engagement or conversion MUST NOT be represented as successful when it causes an unacceptable breach of an Approved Requirement or guardrail.

## 20. Assumptions

This Specification carries these Product-supported assumptions for validation:

- The initial merchant owns or directly controls the Products sold.
- The operation uses one primary South African market, English language, ZAR Currency, legal merchant, storefront, and catalogue.
- Product availability is managed at Product Variant level.
- Physical fulfilment uses an approved courier, shipping provider, or controlled process.
- Payment uses an approved hosted or tokenized provider model.
- Authorized staff responsibilities can be represented through Authentication, Roles, Permissions, and server-side Authorization.
- Critical provider and operational outcomes can be reconciled against trusted evidence.

These are assumptions, not independent Product approvals. If evidence invalidates an assumption or materially changes Product behavior, the change requires applicable Product and Architecture governance.

## 21. Constraints

The business Specification is constrained by:

- The Approved South African, English-language, ZAR initial Product context.
- One primary legal merchant and online storefront for Version 1.
- Physical Product fulfilment.
- Applicable privacy, consumer, tax, Payment, contractual, and commercial obligations requiring qualified validation.
- Approved accessibility, security, data-integrity, testing, documentation, and Product-governance Requirements.
- Provider Contracts and service capabilities without allowing providers to redefine Product semantics.
- Progressive delivery that cannot present partial or placeholder critical journeys as complete.

Architecture-selected technologies are not business constraints owned by this Specification.

## 22. Domain Specification Handoffs

| Existing directory | Required downstream precision |
| --- | --- |
| `specifications/domains/product/` | Product, Product Variant, attribute, Product Media, publication, sellability, and catalogue lifecycle rules. |
| `specifications/domains/category/` | Category hierarchy, membership, navigation classification, and merchandising boundaries. |
| `specifications/domains/customer/` | Customer, Account, Address, Preference, privacy, Wishlist, and support-context behavior. |
| `specifications/domains/inventory/` | Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, concurrency, expiry, release, and reconciliation rules. |
| `specifications/domains/pricing/` | Money, Currency, Price, Discount, Promotion, Voucher, tax, eligibility, calculation, stacking, rounding, snapshots, and Refund treatment. |
| `specifications/domains/order/` | Order creation, Order Item snapshots, lifecycle, cancellation, history, support, and cross-domain coordination. |
| `specifications/domains/payment/` | Payment Attempt, validated Payment Provider evidence, Payment Authorization, Capture, Void, Payment Transaction, Refund Transaction, Chargeback, Settlement, uncertainty, idempotency, and reconciliation. |
| `specifications/domains/shipping/` | Delivery choices, quotes, fulfilment, Shipment lifecycle, tracking, provider evidence, failure, and reconciliation. |
| `specifications/domains/admin/` | Staff User workflows, permitted operations, repair, approval, bulk action, support, reporting, and operational evidence. |

These handoffs do not establish final filenames or resolve open Product policy. Each downstream Specification must use its own governed scope code and Requirement identifiers without weakening this baseline.

## 23. Frontend and Backend Handoffs

Frontend Specifications own page and Component behavior, interaction and state presentation, Angular mechanics, routing, visual implementation, exact responsive implementation, accessibility implementation evidence, and frontend telemetry implementation. They must preserve the business outcomes and non-authoritative nature of client state established here.

Backend Specifications own Spring Use Case orchestration, REST Endpoints, DTO schemas, persistence, Database Transactions, event processing, provider Adapters, idempotency mechanics, reconciliation mechanics, and backend operational implementation. They must preserve authoritative Domain ownership and business invariants.

This Specification does not select or define either implementation layer.

## 24. Contract and Data Impacts

The Requirements imply later governed Contracts for catalogue queries, search, Customer and Account operations, Cart and Checkout, pricing, Inventory availability, Orders, Payment, shipping and fulfilment, Return and Refund, administration, reporting, provider interactions, and applicable events.

Those Contracts must preserve business meaning, authoritative ownership, validation, Authorization, idempotency, compatibility, failure, and Sensitive Data boundaries. This Specification does not define URL paths, HTTP methods, schemas, DTO fields, table names, Event names, Message formats, provider payloads, database structures, or compatibility versions.

Data impacts include authoritative commercial records, immutable or historical snapshots where governed, Audit Records, consent and Preference evidence, reconciliation evidence, and non-authoritative Projections. Retention, deletion, schema, migration, encryption, backup, and recovery mechanics remain owned by applicable Product, Domain, Architecture, security, database, and operational governance.

## 25. Business Acceptance Criteria

Acceptance Criteria are grouped for maintainability and do not have separate identifiers.

| Requirement references | Observable business Acceptance Criteria |
| --- | --- |
| REQ-BUS-001–002 | The initial context is South African, English, and ZAR; supported critical Customer journeys remain functionally usable in governed mobile and desktop contexts; no additional market or Currency is represented as current scope. |
| REQ-BUS-003–006 | A Customer can discover and evaluate publishable Products and select a valid Product Variant; empty, failed, unavailable, stale, and invalid-selection outcomes are distinguishable and recoverable; non-publishable content is not exposed as sellable. |
| REQ-BUS-007 | Visitor and registered Customer capabilities reflect Approved policy without resolving guest Checkout. |
| REQ-BUS-008 | A registered Customer can complete each supported profile, Address, Preference, Wishlist, and Order-access operation; access by another Principal is denied; profile or Address changes do not alter historical Order snapshots. |
| REQ-BUS-009–010 | Cart operations preserve intended Product Variant and quantity state without implying final Price or Stock; repeated actions do not produce unintended duplicate quantities or Stock effects. |
| REQ-BUS-011–013 | Invalid Checkout input is rejected clearly; safe input remains recoverable; trusted Price and availability changes are shown before continuation; interrupted or duplicate submission does not create duplicate Order, Inventory, or Payment effects. |
| REQ-BUS-014–015 | Customer-visible monetary values identify ZAR; trusted server outcomes determine final Price, Discount, Promotion, Voucher, tax, delivery, and total values. |
| REQ-BUS-016 | Each production Promotion demonstrates complete applicable validity, eligibility, threshold, usage-limit, stackability, exclusion, calculation-order, and Refund-treatment definitions; missing mandatory semantics prevent binding production behavior, while unresolved policy values remain unresolved. |
| REQ-BUS-017–019 | Inventory remains authoritative; stale Projections cannot authorize purchase; concurrent attempts cannot exceed valid Available-to-Sell; Stock Reservation creation, expiry, release, failure, and finalization remain traceable without inventing duration or allocation policy. |
| REQ-BUS-020 | A material Stock Adjustment or Stock Movement preserves the authorized actor or system context, reason or source, time, quantity effect, and applicable Audit Record evidence; UI or reporting correction does not rewrite Inventory history. |
| REQ-BUS-021–022 | A governed Checkout can produce one durable Order with historical commercial snapshots; invalid or uncertain outcomes do not appear confirmed; later Product, Customer, or pricing changes do not rewrite the snapshot. |
| REQ-BUS-023 | Ordinary deletion of a confirmed Order is rejected; permitted later transitions remain traceable and preserve the historical commercial record despite governed storage-lifecycle treatment. |
| REQ-BUS-024 | Unvalidated Callback or Webhook evidence cannot confirm Payment; applicable evidence establishes provider authenticity through signature validation or other approved cryptographic validation and establishes payload integrity before it may contribute to provider-dependent Payment truth. |
| REQ-BUS-025 | Pending and unknown outcomes remain explicit until validated evidence or reconciliation permits transition; client cancellation, navigation, timeout, stopped observation, or request abortion does not prove provider processing stopped or negate a submitted effect. |
| REQ-BUS-026 | Duplicate or replayed requests, Callback or Webhook evidence, and reconciliation activity cannot duplicate Payment Authorizations, Captures, Voids, Refund Transactions, Orders, or other financial effects. |
| REQ-BUS-027 | Approved hosted or tokenized handling minimizes Payment-data exposure; prohibited raw card data is not stored; raw CVV is absent from storage, logs, redisplay, telemetry, fixtures, screenshots, analytics, error reports, and other durable artifacts. |
| REQ-BUS-028 | Payment concepts remain distinguishable, and Payment Transaction is not confused with Database Transaction. |
| REQ-BUS-029 | An authorized Staff User can complete supported fulfilment and Shipment work; provider or Notification failure does not rewrite Shipment or Order truth; Customer communication reflects authoritative state. |
| REQ-BUS-030 | Return, Refund, Refund Transaction, cancellation, Inventory disposition, and communication remain distinguishable and follow Approved policy. |
| REQ-BUS-031 | An authorized Staff User can complete each permitted ordinary operation through a controlled workflow; unauthorized direct production modification or code deployment is not the normal workflow. |
| REQ-BUS-032 | Unauthenticated or unauthorized attempts are denied safely; UI state, Role labels, Permissions, and Claims cannot bypass server-side Authorization for the current Principal, Resource, action, and Domain state. |
| REQ-BUS-033 | A Staff User cannot exercise Permissions outside the approved least-privilege boundary; applicable separation of responsibilities is preserved; a high-Risk operation requires confirmation, a Human Approval Gate, or both where governing policy requires them; the material action produces appropriate Audit Record evidence. |
| REQ-BUS-034–035 | Material actions produce appropriate Audit Records; permitted Staff Users can correlate critical evidence without treating analytics as commercial truth. |
| REQ-BUS-036 | Only an authorized Principal can invoke a supported recovery action; the action respects applicable owning Domain state and rules, cannot create duplicate harmful effects when repeated where idempotency is required, and produces appropriate Audit Record evidence where material; invalid or disallowed actions are rejected safely. |
| REQ-BUS-037 | Critical Customer and Staff User journeys have testable WCAG 2.2 AA evidence without a certification or silent AAA claim. |
| REQ-BUS-038 | Performance evidence traces to the Approved governing Requirements without inventing an additional profile, budget, SLA, SLO, or threshold. |
| REQ-BUS-039 | Sensitive Data and Secrets do not appear in unauthorized views, errors, logs, analytics, or exports and remain governed throughout their lifecycle. |
| REQ-BUS-040 | Evidence distinguishes transactional necessity from Preference and marketing consent; completing a purchase does not create marketing consent. |
| REQ-BUS-041 | Applicable legal and policy questions retain qualified-review evidence rather than unsupported compliance claims. |
| REQ-BUS-042 | Material failure, denial, stale, partial, unavailable, pending, and unknown states are distinguishable; recovery guidance reflects known state; error and recovery output does not expose unnecessary Sensitive Data, inaccessible Resource existence, or provider internals to Customer-facing or unauthorized contexts. |
| REQ-BUS-043 | Authorized Staff Users retain the operational visibility needed to detect, investigate, reconcile, support, and recover material outcomes. |
| REQ-BUS-044 | Reports identify their sources and definitions and do not alter or replace authoritative commercial records. |
| REQ-BUS-045 | Critical failures have owned detection, recovery, reconciliation, and support outcomes without invented operational targets. |
| REQ-BUS-046 | A provider outage or transient response does not redefine Product state or Customer-visible success; an applicable degraded, verification, or reconciliation path remains available. |
| REQ-BUS-047–048 | Downstream evidence references applicable stable Requirement identifiers; material Product changes have the required Decision and synchronized governing updates before being represented as current behavior. |
| REQ-BUS-049 | Applicable Customer communications reflect authoritative Order, Payment, Shipment, Return, Refund, failure, retry, or reconciliation state; premature success is not communicated; delivery or provider failure does not alter commercial truth; material communication evidence supports investigation. |
| REQ-BUS-050 | Only authorized, publishable content is exposed; policy versions and material publication evidence are traceable; unsupported or stale claims can be corrected; Product Media satisfies applicable Approved Product and Design System Requirements. |
| REQ-BUS-051 | Supported registration and recovery outcomes protect Account access; failed Authentication is handled safely; Session expiry or revocation takes effect; cross-Principal access and Session-based Authorization bypass are denied. |
| REQ-BUS-052 | Applicable workflows can identify and govern suspicious or abusive behavior without unauthorized commercial effects; fraud advice cannot establish Payment truth; permitted false-positive recovery or review remains auditable and policy-governed. |
| REQ-BUS-053 | An authorized user can generate a permitted operational export from appropriate authoritative sources with definitions and context intact; unauthorized export is denied; Sensitive Data is minimized; the export does not become authoritative or silently replace analytical Projection status. |
| REQ-BUS-054 | Production reliance is blocked until applicable tax display, invoice, invoice-numbering, credit-note, and commercial-document obligations are defined and qualified where necessary; confirmed tax values remain preserved and client totals remain non-authoritative. |

Negative, concurrency, retry, cancellation, accessibility, responsive, Audit Record, and telemetry cases apply only where material to the referenced Requirement. No Gherkin or EARS notation is mandated.

## 26. Requirement Traceability

No applicable `DEC-####` Product Decision Record currently exists in the repository. The table therefore links Approved Product sections, governing standards, and intended downstream Specifications without fabricating Decision references.

| Requirement | Primary Product trace | Additional governing trace | Intended downstream scope |
| --- | --- | --- | --- |
| REQ-BUS-001 | PRODUCT.md sections 6, 11, 20 | VISION.md | Product, frontend, backend |
| REQ-BUS-002 | PRODUCT.md sections 5.2, 10, 30 | DESIGN-SYSTEM.md; ACCESSIBILITY.md | Frontend |
| REQ-BUS-003 | PRODUCT.md sections 12.1, 14.1, 16.8, 32 | DESIGN-SYSTEM.md | Product, Category, frontend |
| REQ-BUS-004 | PRODUCT.md sections 8.1, 12.1, 14.1, 30.1 | PERFORMANCE.md | Product, Category, frontend, backend |
| REQ-BUS-005 | PRODUCT.md sections 8.2, 14.2, 30.2, 32.4 | DESIGN-SYSTEM.md; ACCESSIBILITY.md | Product, frontend |
| REQ-BUS-006 | PRODUCT.md sections 14.2–14.3, 30.2–30.3 | GLOSSARY.md | Product, Cart/Checkout, frontend |
| REQ-BUS-007 | PRODUCT.md sections 7.1–7.2, 12.2–12.3, 24 | SECURITY-STANDARDS.md | Customer, Cart/Checkout, frontend, backend |
| REQ-BUS-008 | PRODUCT.md sections 7.2, 12.3–12.4, 16.7, 30.5 | SECURITY-STANDARDS.md | Customer, frontend, backend |
| REQ-BUS-009 | PRODUCT.md sections 12.2, 14.3, 30.3 | GLOSSARY.md | Cart/Checkout, frontend, backend |
| REQ-BUS-010 | PRODUCT.md sections 14.3, 16.2, 23 | API.md | Cart/Checkout, Inventory, frontend, backend |
| REQ-BUS-011 | PRODUCT.md sections 14.4, 30.4 | SECURITY-STANDARDS.md; ACCESSIBILITY.md | Cart/Checkout, frontend, backend |
| REQ-BUS-012 | PRODUCT.md sections 14.4, 16.1–16.2, 30.4 | API.md | Cart/Checkout, Pricing, Inventory, backend |
| REQ-BUS-013 | PRODUCT.md sections 5.6, 8.4, 14.4, 23 | API.md | Cart/Checkout, Order, Payment, Inventory |
| REQ-BUS-014 | PRODUCT.md sections 6, 16.1, 20 | GLOSSARY.md | Pricing, Order, frontend |
| REQ-BUS-015 | PRODUCT.md sections 5.4, 14.4, 16.1 | SECURITY-STANDARDS.md | Pricing, Cart/Checkout, backend |
| REQ-BUS-016 | PRODUCT.md sections 9.5, 16.1, 16.6, 23 | GLOSSARY.md | Pricing, Cart/Checkout, Order |
| REQ-BUS-017 | PRODUCT.md sections 9.2, 13, 16.2 | ARCHITECTURE.md; DATABASE.md | Inventory, backend, Contracts |
| REQ-BUS-018 | PRODUCT.md sections 9.2, 14.3–14.4, 16.2, 23 | DATABASE.md | Inventory, Cart/Checkout, backend |
| REQ-BUS-019 | PRODUCT.md sections 16.2, 17, 24 | DATABASE.md; EVENTS.md | Inventory, Cart/Checkout, Order |
| REQ-BUS-020 | PRODUCT.md sections 12.7, 15.2, 16.2 | SECURITY-STANDARDS.md; DATABASE.md | Inventory, admin, backend |
| REQ-BUS-021 | PRODUCT.md sections 14.4–14.6, 16.4, 17 | API.md; EVENTS.md | Order, Cart/Checkout, Payment |
| REQ-BUS-022 | PRODUCT.md sections 16.1, 16.4, 17.4 | DATABASE.md | Order, Pricing, Customer |
| REQ-BUS-023 | PRODUCT.md sections 16.4, 17.3–17.4, 37.4 | DATABASE.md; SECURITY-STANDARDS.md | Order, Return/Refund, backend |
| REQ-BUS-024 | PRODUCT.md sections 14.5–14.6, 16.3, 23 | SECURITY-STANDARDS.md; API.md | Payment, backend, provider Contract |
| REQ-BUS-025 | PRODUCT.md sections 5.6, 14.5–14.6, 16.3, 17.2 | API.md | Payment, Cart/Checkout, frontend, backend |
| REQ-BUS-026 | PRODUCT.md sections 14.5, 16.3, 23 | SECURITY-STANDARDS.md; API.md; EVENTS.md | Payment, Order, Cart/Checkout, backend |
| REQ-BUS-027 | PRODUCT.md sections 16.3, 20, 23 | SECURITY-STANDARDS.md | Payment, frontend, backend, provider Contract |
| REQ-BUS-028 | PRODUCT.md sections 13, 16.3 | GLOSSARY.md | Payment, database, backend |
| REQ-BUS-029 | PRODUCT.md sections 12.7, 14.7, 15.3, 16.5, 28.5 | API.md; EVENTS.md | Shipping, Order, admin, frontend |
| REQ-BUS-030 | PRODUCT.md sections 14.8, 16.11, 34 | SECURITY-STANDARDS.md; API.md | Return/Refund, Order, Payment, Inventory |
| REQ-BUS-031 | PRODUCT.md sections 5.8, 7.3–7.8, 9.7, 15, 31 | SECURITY-STANDARDS.md | Admin, frontend, backend |
| REQ-BUS-032 | PRODUCT.md sections 7, 16.7, 23 | SECURITY-STANDARDS.md; API.md | Customer, admin, frontend, backend |
| REQ-BUS-033 | PRODUCT.md sections 5.5, 7.3–7.8, 23, 31 | SECURITY-STANDARDS.md | Admin, security, frontend, backend |
| REQ-BUS-034 | PRODUCT.md sections 9.3, 12.9, 16, 17.4 | SECURITY-STANDARDS.md | All commercial and administrative domains |
| REQ-BUS-035 | PRODUCT.md sections 9.3, 9.6, 14.8, 18.3, 34 | EVENTS.md | Admin, support, reporting, backend |
| REQ-BUS-036 | PRODUCT.md sections 5.6, 15, 31.4, 34 | SECURITY-STANDARDS.md; API.md | Admin, support, all affected domains |
| REQ-BUS-037 | PRODUCT.md sections 5.7, 8.6, 16.9, 19 | ACCESSIBILITY.md | Frontend, admin |
| REQ-BUS-038 | PRODUCT.md sections 8.3, 18.4–18.6, 19, 35 | PERFORMANCE.md | Frontend, backend, infrastructure |
| REQ-BUS-039 | PRODUCT.md sections 16.7, 20, 23, 34.5 | SECURITY-STANDARDS.md | All data-processing scopes |
| REQ-BUS-040 | PRODUCT.md sections 7.2, 16.7, 24 | SECURITY-STANDARDS.md | Customer, frontend, backend |
| REQ-BUS-041 | PRODUCT.md sections 16.10, 19–20, 35.5 | SECURITY-STANDARDS.md | Product, Finance, Legal/Privacy review |
| REQ-BUS-042 | PRODUCT.md sections 5.4–5.6, 17.2, 30 | ACCESSIBILITY.md | Frontend, backend, all affected domains |
| REQ-BUS-043 | PRODUCT.md sections 9.3, 9.6, 12.10, 18.3, 35.4 | SECURITY-STANDARDS.md | Admin, operations, reporting |
| REQ-BUS-044 | PRODUCT.md sections 9.6, 12.10, 18.5, 33 | GLOSSARY.md | Reporting, analytics, admin |
| REQ-BUS-045 | PRODUCT.md sections 5.6, 18.6, 23, 28.6, 35 | ARCHITECTURE.md; SECURITY-STANDARDS.md | Operations, infrastructure, all critical domains |
| REQ-BUS-046 | PRODUCT.md sections 9.8, 13, 17, 23, 37.3 | ARCHITECTURE.md; API.md; EVENTS.md | Provider Adapters, all affected domains |
| REQ-BUS-047 | PRODUCT.md sections 22, 26, 35 | DOCUMENTATION-STANDARDS.md | All downstream Specifications and Contracts |
| REQ-BUS-048 | PRODUCT.md sections 21, 25, 36–38 | AGENTS.md; DECISIONS.md | All affected Product and implementation scopes |
| REQ-BUS-049 | PRODUCT.md sections 4.1–4.2, 9.3, 10, 14.6–14.8, 16.5, 18.3, 28.4–28.5 | EVENTS.md; SECURITY-STANDARDS.md | Order, Payment, Shipping, Return/Refund, Customer, admin |
| REQ-BUS-050 | PRODUCT.md sections 9.1, 12.1, 12.5, 15.1, 15.6, 16.8, 32, 35 | DESIGN-SYSTEM.md; SECURITY-STANDARDS.md | Product, Category, admin, frontend |
| REQ-BUS-051 | PRODUCT.md sections 7.2, 12.3, 16.7, 24, 28.1, 28.3, 30.5 | SECURITY-STANDARDS.md | Customer, frontend, backend |
| REQ-BUS-052 | PRODUCT.md sections 5.5, 16.12, 18.6, 23–24, 35 | SECURITY-STANDARDS.md | Customer, Pricing, Cart/Checkout, Payment, admin |
| REQ-BUS-053 | PRODUCT.md sections 4.2, 7.3, 7.7, 12.10, 18.3, 18.5, 24, 31 | SECURITY-STANDARDS.md | Admin, reporting, analytics, backend |
| REQ-BUS-054 | PRODUCT.md sections 16.10, 20, 24, 28.6, 35.3, 35.5 | SECURITY-STANDARDS.md | Pricing, Order, Finance, reporting |

Future Accepted Product Decisions must be linked rather than copied. If a Product Decision changes PRODUCT.md, this Specification and affected downstream artifacts must be synchronized through governance.

## 27. Dependencies

This Approved Specification depends on:

- Continued consistency with the Approved Product baseline and canonical terminology.
- Approval of downstream Domain Specifications before their detailed behavior becomes binding.
- Governed API, event, provider, and data Contracts before integration reliance.
- Security, privacy, accessibility, testing, documentation, and operational review proportional to Risk.
- Qualified validation of applicable legal, tax, consumer, privacy, Payment, and contractual constraints.
- Resolution of material Open Product Decisions before affected implementation becomes binding.
- Traceable test, review, Audit Record, reconciliation, and operational evidence where applicable.

## 28. Risks

| Risk | Required treatment |
| --- | --- |
| Unresolved Product policy is implemented as local preference | Keep the concern open and block binding behavior until applicable Product governance resolves it. |
| Cross-domain Specifications diverge | Trace downstream Requirements to this baseline and reconcile conflicts through the Decision Hierarchy. |
| Payment uncertainty is presented as success | Preserve validated Payment Provider evidence, explicit unknown state, idempotency, and reconciliation Requirements. |
| Concurrent Inventory activity permits Overselling | Require Inventory-owned concurrency rules and trusted Available-to-Sell verification. |
| Privacy, consumer, tax, or Payment wording is mistaken for legal assurance | Require qualified review and prohibit unsupported compliance claims. |
| Audit or operational evidence is incomplete | Define evidence in downstream Requirements and verify critical recovery and reconciliation paths. |
| Contracts or implementation drift from Product meaning | Maintain Requirement traceability and synchronize governing sources and Contracts. |
| Analytics becomes alternate commercial truth | Preserve authoritative Domain ownership and label analytical outputs as Projections. |

## 29. Product Decision Governance

This Specification may refine an Open Product Decision into testable options and impacts, but it cannot accept an option merely by stating it. Material Product Decisions use the `DEC-####` governance in `.ai/core/DECISIONS.md`, must update `.ai/core/PRODUCT.md` when they change current Product truth, and must link affected Requirements.

Because the repository has not established a location for non-Architecture Decision Records, this Specification does not invent one. Architecture Decisions remain ADRs and use the established ADR location and identifier convention.

## 30. Open Product Decisions

The following grouped concerns are unresolved in PRODUCT.md and remain unresolved here:

| Concern | Open Product decisions | Owning governance and downstream impact |
| --- | --- | --- |
| Brand and catalogue | Final brand identity; initial Categories and taxonomy; Product Variant and Attribute standards; Product reviews; Product data import, export, and migration. | Product and affected business owners; Product, Category, Design System, frontend, and data Specifications. |
| Customer and access | Guest Checkout versus mandatory Account; email verification; guest and registered Wishlist behavior; Customer data export, correction, deletion, and Account closure. | Product, Customer, Security, and Privacy/Legal where required; Customer, Checkout, frontend, and backend Specifications. |
| Payment and financial policy | Initial Payment methods and Payment Provider; tax-inclusive display, invoice, and credit-note policy; Return, exchange, Refund, and cancellation policy details; fraud screening and manual review; gift cards, store credit, and promotional credit. | Product with Finance, Security, Legal/Privacy, Architecture, and affected Domain Owners; Pricing, Payment, Order, Customer, API, and provider Contracts. |
| Shipping and service | Shipping Provider, service levels, delivery areas, fees, free-delivery threshold, customer-support channels, service expectations, escalation, launch support window. | Product, Operations, Finance, and affected owners; Shipping, Order, Customer, admin, and operational Specifications. |
| Inventory | Stock Reservation duration; back-order and pre-order support; low-stock and out-of-stock Customer messaging. | Product and Inventory owner; Inventory, Product, Checkout, Order, frontend, and backend Specifications. |
| Promotions and merchandising | Voucher and Promotion stacking; Product-review support; content approval and scheduled publication. | Product and accountable commercial/content owners; Pricing, Product, admin, and frontend Specifications. |
| Consent, analytics, and reporting | Marketing consent and communication Preference model; analytics provider and event taxonomy; initial reports and exports. | Product, Security, Privacy/Legal where required, and affected owners; Customer, admin, frontend, backend, and analytics Contracts. |
| Administration | Initial administrative Role and Permission matrix; production Customer-service and operational escalation process. | Product, Engineering, Security, Operations, and affected Domain Owners; admin and security Specifications. |
| Release | Product launch date, final release scope, and post-launch support window. | Product and Engineering governance; roadmap, readiness, operations, and all affected Specifications. |

No identifier in this table is a Product Decision identifier, and no option is Approved by inclusion.

## 31. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/VISION.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/API.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`

## 32. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Approved the business requirements baseline after final Product, cross-domain, traceability, Acceptance Criteria, terminology, and lifecycle validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial business requirements baseline for the enterprise fashion e-commerce platform. |

## 33. Quality Requirements

This Specification MUST remain Product-aligned, business-focused, testable, traceable, technology-neutral, explicit about uncertainty, and subordinate to the repository Decision Hierarchy. It MUST distinguish Approved Product scope from assumptions, Open Product Decisions, future possibilities, Domain detail, Contracts, and implementation mechanics.

Requirements and Acceptance Criteria MUST remain coherent enough for downstream Specifications to refine without relying on hidden business policy.

## 34. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata and Revision History accurately describe the document lifecycle and `authoritative: false` remains unchanged;
2. the declared scope code is `BUS`, current Requirement identifiers span `REQ-BUS-001` through `REQ-BUS-054`, identifiers are unique and stable, retired identifiers are not reused, and references remain valid;
3. authority and conflict handling remain aligned with the `AGENTS.md` Decision Hierarchy;
4. every Requirement remains traceable to Approved Product direction without inventing Product behavior or a Product Decision;
5. canonical terminology remains aligned with `GLOSSARY.md`;
6. business Requirements remain distinct from Domain rules, Contracts, Architecture, and implementation mechanics;
7. Payment authority remains based on validated Payment Provider evidence, Payment uncertainty remains explicit, duplicate financial effects remain prohibited, raw CVV remains prohibited, and Payment Transaction remains distinct from Database Transaction;
8. Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement, and Overselling protection remains intact;
9. Money, Currency, Price, Discount, Promotion, and Voucher semantics remain Product-governed without invented calculations or policies;
10. Authentication and server-side Authorization boundaries remain authoritative and Role, Permission, Claims, and UI state remain non-authoritative;
11. WCAG 2.2 AA remains the accessibility target without a certification or silent AAA claim;
12. security, privacy, compliance, and Sensitive Data wording remains subordinate to governing standards and qualified validation;
13. Acceptance Criteria remain observable, testable, proportionate, and traceable to Requirements without a new identifier scheme;
14. Open Product Decisions remain unresolved unless Accepted governance and synchronized source updates exist;
15. downstream Domain, frontend, backend, Contract, and data handoffs remain explicit without fabricated filenames or paths;
16. Related Documents exist, contain no self-reference, and remain relevant;
17. headings remain sequential and unique, sections remain non-empty, tables remain valid, and no unfinished marker or actual ellipsis remains; and
18. only `specifications/business/business-requirements.md` changes for a scoped update.
