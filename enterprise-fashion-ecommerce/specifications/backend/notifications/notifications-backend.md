---
title: Notifications Backend Specification
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-09-24
authoritative: false
scope: BNTF
---

# Notifications Backend Specification

## 1. Purpose

This Approved Specification defines implementation-facing backend obligations for the Approved Notifications Domain under scope `BNTF`. Its Requirements are normative only within the Notifications backend scope, remain `authoritative: false`, and remain subordinate to higher-authority governing sources, Approved Business Requirements, the Approved Notifications Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, Accepted ADR-0016, and applicable repository standards.

BNTF uses the Notifications-only decomposition authorized by Accepted ADR-0016 as the fifteenth downstream Backend Specification after BEB, immediately after Approved BCMS. It consumes only materially applicable governed Contracts or evidence and acquires no Identity, Customer, Account, Product, Category, Inventory, Pricing, Cart, Checkout, Order, Shipping and Fulfilment, Payment, Return, CMS, Reporting, Administration, or other Domain authority. It resolves no Open Product or Architecture Decision and establishes no post-BNTF roadmap position.

## 2. Requirements

### BNTF-REQ-001 — Lifecycle, Scope, and Authority

BNTF MUST use scope `BNTF`, remain `authoritative: false`, identify its `1.0.0 Approved` lifecycle, remain normative only within the Notifications backend scope, specialize only the Approved Notifications Domain, preserve governing-source precedence, and claim no repository-wide authority.

### BNTF-REQ-002 — Complete Notifications Domain Specialization

BNTF MUST specialize every `REQ-NTF-001` through `REQ-NTF-056` without weakening, omitting, rewriting, or transferring Notifications Domain authority.

### BNTF-REQ-003 — Decomposition and Roadmap Containment

BNTF MUST use a Notifications-only decomposition, immediately follow Approved BCMS, preserve Reporting as independently eligible, separate, unresolved, unranked, and unordered, preserve Administration as dependency-blocked by the missing Approved Reporting backend invocation Contract after BNTF approval closes only its Notifications prerequisite, and MUST NOT establish or authorize any post-BNTF Backend Specification identity, title, path, scope, decomposition, or ordering.

### BNTF-REQ-004 — BEB Inheritance

BNTF MUST inherit and explicitly trace every materially applicable `BEB-REQ-001` through `BEB-REQ-056` without weakening shared backend governance or duplicating it as Notifications policy.

### BNTF-REQ-005 — Modular and Hexagonal Boundary

BNTF MUST remain a cohesive Notifications Module in the governed modular monolith, keep Domain and application logic independent of frameworks and external systems, and enforce inward dependency direction through project-owned Ports and Adapters.

### BNTF-REQ-006 — Public Contract, Use-Case, and API Boundary

BNTF MUST expose only intentional abstract Contracts and explicit Notifications-owned Use Cases, distinguish commands from queries, validate governed context and bounds, translate failures safely, evolve compatibility deliberately, and prevent transport, provider, persistence, or framework types from owning Notifications policy.

### BNTF-REQ-007 — Notifications Persistence and Historical Truth

BNTF MUST own only Notifications persistence and preserve Notification identity, accepted request outcomes, source provenance, template-version evidence, delivery-attempt evidence, Delivery Status, retry state, uncertainty, history, recovery, and reconciliation through project-owned persistence boundaries without selecting schemas, tables, columns, indexes, ORM mappings, identifier formats, or retention periods.

### BNTF-REQ-008 — Notifications-Owned Truth and External Non-Authority

BNTF MUST own only Notification request, template, attempt, delivery, retry, recovery, and reconciliation truth governed by the Approved Notifications Domain. A request, render, queue condition, attempt, provider response, callback, display, or delivery outcome MUST NOT create, change, prove, reverse, or repair another Domain's authoritative fact.

### BNTF-REQ-009 — Stable Identity, Provenance, and Correlation

BNTF MUST preserve stable distinct Notification and delivery-attempt identities, validated source ownership and stable source reference, purpose, correlation, causation where applicable, and sufficient provenance without selecting identifier or storage mechanisms.

### BNTF-REQ-010 — Request Intake and Outcome Separation

BNTF MUST distinguish receipt, validation, acceptance, rejection, duplication, delayed processing, and uncertainty, accept only requests with sufficient current governed provenance, source, purpose, recipient, template, and policy evidence, and MUST NOT treat receipt or queue presence as sending or delivery.

### BNTF-REQ-011 — Duplicate and Ambiguous Request Safety

Repeated, replayed, concurrent, or ambiguously completed requests MUST preserve prior accepted effects, avoid unintended duplicate communication, and expose explicit duplicate, conflict, or uncertain outcomes without selecting an idempotency mechanism.

### BNTF-REQ-012 — Recipient Context and Isolation

BNTF MUST consume only minimum governed recipient identity, contact, locale, purpose, Consent, Preference, and Notification Preference evidence; preserve freshness and disclosure constraints; and isolate requests, previews, history, resends, and evidence by recipient, Customer, Principal, object, and authorized Staff context.

### BNTF-REQ-013 — Customer, Consent, and Preference Boundary

BCUS MUST remain authoritative for Customer, Account, Address, contact, Consent, Preference, and Notification Preference truth. BNTF MAY retain purpose-bound delivery context and historical evidence but MUST NOT infer marketing Consent, silently correct Customer truth, or treat delivery as proof of Customer mutation or access.

### BNTF-REQ-014 — Communication Classification and Policy Uncertainty

BNTF MUST distinguish transactional and marketing communication and preserve missing, stale, conflicting, or unresolved classification, necessity, suppression, Consent, Preference, and channel eligibility as uncertainty; it MUST NOT invent classification, permission, communication eligibility, suppression, or Product policy.

### BNTF-REQ-015 — Notification Template Identity and Authority

BNTF MAY own notification-specific templates and MUST preserve their stable identity and sufficient version or history evidence for each applicable attempt, while template content MUST NOT become authority for CMS, public policy, Product, Pricing, Customer, commerce, Identity, legal, or source facts.

### BNTF-REQ-016 — Template Eligibility and CMS Boundary

BNTF MUST use only templates permitted by applicable governance and fail safely for missing, Draft, unapproved, stale, withdrawn, incompatible, or uncertain template conditions. Where BCMS content materially contributes, BNTF MUST consume governed CMS evidence without transferring CMS publication, placement, editorial, policy-presentation, or content authority; CMS is not a universal Notifications prerequisite.

### BNTF-REQ-017 — Rendering Safety, Integrity, and Accessibility

Rendering MUST preserve validated source, recipient, channel, template-version, escaping, content-integrity, privacy, and equivalent accessible meaning, and MUST fail safely for missing, malformed, incompatible, or untrusted content without fabricating or exposing truth or Sensitive Data.

### BNTF-REQ-018 — Channel, Fallback, and Suppression Deferral

Channel availability, eligibility, priority, fallback, substitution, suppression, cancellation, and override MUST follow Approved governance and current context. BNTF MUST NOT mandate a channel, infer fallback permission, suppress required communication automatically, or bypass a governed prohibition.

### BNTF-REQ-019 — Provider-Neutral Delivery and Evidence

Delivery MUST remain behind project-owned Ports. Provider responses, callbacks, receipts, and other evidence MUST be validated for provenance, authenticity, integrity, correlation, compatibility, freshness, and permitted meaning before changing provider-dependent Notification state, and provider terminology MUST NOT redefine Delivery Status or source truth.

### BNTF-REQ-020 — Delivery Attempts, Status, and History

Each attempt MUST remain distinguishable from its Notification, source, recipient, template, channel, retries, concurrent attempts, and provider references. Delivery Status MUST reflect current Notifications-owned evidence while accepted changes preserve attributable attempt history, ordering context, correlation, and applicable provider evidence.

### BNTF-REQ-021 — Delivery Outcome Integrity

Materially distinct requested, pending, attempted, provider-accepted, delivered, failed, rejected, cancelled, suppressed, exhausted, and uncertain outcomes MUST remain explicit. Provider acceptance, asynchronous acknowledgement, queue state, or client display MUST NOT automatically equal end-recipient delivery or source-business success.

### BNTF-REQ-022 — Retry and Exhaustion Boundary

Retry MUST require applicable governed policy and current evidence, preserve source, purpose, recipient, template and channel context, inspect prior effects, and prevent harmful duplication. Exhausted or unsafe processing MUST retain explicit unresolved evidence; BNTF selects no retry count, timing, backoff, expiry, scheduler, exhaustion policy, or Dead Letter Queue mechanism.

### BNTF-REQ-023 — Changed Source Facts and Cancellation Separation

Delayed, reordered, duplicated, stale, conflicting, corrected, revoked, or superseded source facts MUST be evaluated against owning-Domain truth and current communication context. Notification cancellation or suppression MUST remain distinct from source cancellation or reversal, and an unknown in-flight provider effect MUST NOT be assumed cancelled.

### BNTF-REQ-024 — Order Boundary

BNTF MAY consume materially applicable BORD evidence for governed communications while Order identity, lifecycle, cancellation, snapshots, items, history, and current truth remain Order-owned; no Notification can create or transition an Order or confirm purchase.

### BNTF-REQ-025 — Payment and Refund Boundary

BNTF MAY consume materially applicable BPAY evidence while Payment, Refund, Refund Transaction, financial outcome, amount, provider, and reconciliation truth remain Payment-owned; Notification or provider evidence MUST NOT establish financial success or duplicate a financial effect.

### BNTF-REQ-026 — Shipping and Fulfilment Boundary

BNTF MAY consume materially applicable BSHP evidence while Shipment, Dispatch, Carrier, tracking, delivery, exception, reverse-logistics, provider, and reconciliation truth remain Shipping-and-Fulfilment-owned; BNTF MUST expose no unnecessary provider internals.

### BNTF-REQ-027 — Return Boundary

BNTF MAY consume materially applicable BRET evidence while Return identity, request, eligibility, authorization, receipt, inspection, disposition, lifecycle, Refund, restocking, transport, recovery, and reconciliation truth remain externally owned.

### BNTF-REQ-028 — Customer and Identity Boundary

BNTF MAY consume materially applicable BCUS and BIDN evidence for governed communications while Customer, Account, contact, Consent, Preference, Principal, Authentication, credentials, verification, recovery, Sessions, revocation, and Authorization truth remain with their owners; delivery proves none of those outcomes.

### BNTF-REQ-029 — Checkout and Cart Boundary

BNTF MAY consume materially applicable BCHK and BCART evidence only for governed communication while Checkout orchestration and Cart intent remain externally owned; communication MUST NOT prove Checkout completion, reserve Stock, create an Order, confirm Payment, or invent abandonment policy.

### BNTF-REQ-030 — Product, Category, Pricing, and Inventory Boundary

BNTF MAY represent only validated BPRD, BCAT, BPRC, and BINV facts required by an Approved communication, preserving source authority, historical context where applicable, uncertainty, and non-authoritative presentation without calculating Price, changing taxonomy, or establishing Stock or availability.

### BNTF-REQ-031 — Explicit Failure and Provider Uncertainty

Validation, rendering, dependency, timeout, partial-completion, provider-rejection, provider-outage, lost-evidence, conflict, and unknown-provider-effect conditions MUST remain distinguishable, safe, diagnosable, and recoverable where governed; timeout or stopped observation MUST NOT prove provider processing stopped.

### BNTF-REQ-032 — Replay, Concurrency, and Duplicate-Effect Safety

Repeated, replayed, concurrent, or reordered request, render, attempt, callback, retry, resend, cancellation, suppression, recovery, and reconciliation activity MUST preserve accepted history, inspect prior effects, prevent unauthorized or duplicate communication, and retain uncertainty without selecting locks, keys, stores, TTLs, or another mechanism.

### BNTF-REQ-033 — Controlled Recovery and Resend

Correction, retry, resend, release, cancellation, suppression, and other recovery actions MUST be explicit, authorized, attributable, constrained by current source and Notifications truth, duplicate-safe where effects are harmful, and materially auditable without fabricating provider evidence or bypassing policy.

### BNTF-REQ-034 — Reconciliation

BNTF MUST support accountable reconciliation among accepted requests, source facts, recipient and policy context, template versions, attempts, provider evidence, Delivery Status, retry state, and downstream representations, preserving provenance, uncertainty, history, and discrepancies until repair.

### BNTF-REQ-035 — Administration and Contextual Authorization

Administration and support MAY invoke only explicit Notifications-owned template, preview, resend, investigation, recovery, and reconciliation capabilities. Protected actions MUST enforce trusted server-side contextual Authorization for Principal, Resource, action, purpose, recipient, and current state without defining Administration workflows, Roles, Permissions, approval, override, escalation, or service policy.

### BNTF-REQ-036 — Sensitive Communication Data Protection

BNTF MUST apply least privilege, default denial where applicable, object isolation, minimization, safe errors, purpose limitation, applicable necessity or Consent, and approved privacy, disclosure, and retention governance to recipient data, content, context, source references, provider evidence, logs, events, exports, and Audit Records.

### BNTF-REQ-037 — Secrets, Operational Evidence, and Compliance Boundary

BNTF MUST protect Secrets and provider credentials and exclude credentials, tokens, recovery secrets, raw Payment data, unnecessary PII, protected fraud detail, inaccessible Resource existence, and exploitable security or provider detail from unauthorized surfaces. It MAY communicate validated governed fraud, tax, invoice, Credit Note, policy, or legal outcomes but MUST NOT define them.

### BNTF-REQ-038 — Contract Boundary

BNTF Contracts MUST preserve authority, identity, purpose, recipient isolation, provenance, correlation, template and channel context, compatibility, validation, privacy, safe errors, replay and duplicate safety, delivery uncertainty, and recovery without defining concrete routes, methods, statuses, fields, DTOs, classes, schemas, provider payloads, transport, or persistence design.

### BNTF-REQ-039 — Conditional Events and Messaging

Where an Approved Contract provides a Domain or Integration Event, BNTF MAY consume it while preserving producer authority, compatibility, correlation, causation, ordering uncertainty, replay safety, and Sensitive Data controls. Any governed BNTF event MUST represent a completed Notifications-owned fact; BNTF MUST NOT invent event names, payloads, topics, queues, brokers, transports, or introduce external messaging independently.

### BNTF-REQ-040 — Accessible Notification Outcomes

Applicable content and Customer or Staff request, preference-context, preview, delivery-history, failure, and recovery experiences MUST provide verifiable WCAG 2.2 AA semantics, understandable status and recovery, equivalent meaning, and keyboard and assistive-technology operability without prescribing visual design or claiming certification.

### BNTF-REQ-041 — Observability and Health

Material request, validation, rendering, delivery, provider, retry, exhaustion, recovery, and reconciliation activity MUST be bounded, observable, correlated, attributable, diagnosable, and represented truthfully in health evidence while protecting data and selecting no product or numerical target.

### BNTF-REQ-042 — Proportional Audit Records

Material template-governance, privileged preview, bulk or high-Risk send, resend, suppression, recovery, policy-affecting, provider-configuration, and reconciliation actions MUST produce attributable, tamper-resistant, proportional Audit Records distinct from diagnostic logs; routine processing and reads MUST NOT automatically be treated as high-Risk.

### BNTF-REQ-043 — Reporting, Analytics, Export, and Projection Non-Authority

BNTF MAY expose explicitly permitted Notifications evidence for future governed Reporting, analytics, exports, dashboards, support views, or Projections, which MUST remain protected, source-traceable, stale-capable, non-authoritative, and unable to mutate Notification or source truth. BNTF assigns Reporting no backend identity or roadmap position.

### BNTF-REQ-044 — Transaction and External-Effect Separation

BNTF application Use Cases MUST own applicable transaction boundaries, make consistency and atomicity explicit, and keep external provider calls and uncertain effects outside unsafe transaction coupling without selecting a transaction, workflow, or orchestration mechanism.

### BNTF-REQ-045 — Configuration and Feature-Flag Safety

BNTF configuration and any future feature-controlled state MUST be validated, protected, observable, safely defaulted, compatible with reachable Notification states, and non-authoritative without selecting a store, provider, rollout system, or feature-flag lifecycle policy.

### BNTF-REQ-046 — Migration, Deployment Compatibility, and Bounded Work

BNTF data and Contract evolution MUST use the version-controlled Flyway migration governance mandated by BEB-REQ-049, preserve immutable applied migration history, prohibit runtime schema mutation and untracked drift, support mixed-version compatibility and forward recovery, and keep requests, callbacks, retries, reconciliation, queries, and batch work bounded without inventing limits or physical design.

### BNTF-REQ-047 — Complete Verification Coverage

BNTF verification MUST cover every Requirement through Domain, application, Adapter, integration, Contract, architecture, security, privacy, accessibility, operations, failure, recovery, reconciliation, provider uncertainty, and traceability evidence in proportion to Risk without selecting tooling or numerical coverage targets.

### BNTF-REQ-048 — Policy and Implementation Neutrality

BNTF MUST preserve every Open Product and Architecture Decision and MUST NOT select communication, Consent, Preference, channel, template, provider, retry, exhaustion, escalation, retention, API, DTO, persistence, event, messaging, cache, infrastructure, deployment, numerical, or Role/Permission policy or mechanism not already mandated by canonical governance.

### BNTF-REQ-049 — Provider, Callback, and External Interaction Boundary

Any materially applicable provider interaction or callback MUST use project-owned Ports and Adapters, enforce authenticity, integrity, correlation, replay and duplicate safety, compatibility, privacy, time and failure bounds, uncertainty, recovery, and reconciliation, and MUST NOT allow provider acknowledgement or vocabulary to become Notifications or source authority.

### BNTF-REQ-050 — Retention, Correction, and Deletion Boundary

BNTF MUST preserve required historical, security, audit, investigation, and reconciliation evidence while applying only governed correction, deletion, closure, and retention policy; unresolved retention or data-request policy MUST remain explicit and MUST NOT be converted into a local period, erasure rule, or storage mechanism.

### BNTF-REQ-051 — Downstream Dependency Containment

Approved BNTF closes only Administration's Notifications backend invocation prerequisite. Administration MUST remain blocked by the missing Approved Reporting backend invocation Contract; Reporting remains independently eligible, separate, unresolved, unranked, and unordered, and no post-BNTF roadmap position is established.

### BNTF-REQ-052 — Governance Integrity and Traceability

BNTF MUST maintain complete Requirement-to-Acceptance-Criterion traceability, Notifications Domain coverage, BEB accounting, exact Open Decision inventories, authority boundaries, governance-review requirements, Related Documents, Revision History, and lifecycle-correct final validation.

## 3. Canonical Inputs and Authority Boundaries

BNTF is governed by `AGENTS.md`, `PRODUCT.md`, `ARCHITECTURE.md`, `DECISIONS.md`, applicable core and backend standards, Accepted ADR-0016, Approved BEB, and the Approved Notifications Domain. Approved Backend Specifications are consumable only through materially applicable governed Contracts or evidence; their existence alone does not establish applicability.

| Boundary | BNTF may consume or expose | Authority retained externally |
| --- | --- | --- |
| BIDN | Trusted Principal, Authentication, verification, recovery, and security-communication evidence where governed | Identity, credentials, verification, Sessions, revocation, recovery, and access evidence |
| BCUS | Minimum Customer, Account, contact, Consent, Preference, Notification Preference, locale, and purpose evidence | Customer, Account, contact, Address, Consent, Preference, privacy, and current source truth |
| BPRD / BCAT / BPRC / BINV | Validated catalogue, taxonomy, Money, Price, Promotion, Stock, availability, and historical context required by an Approved communication | Product, Product Variant, Category, Pricing, commercial, Inventory, Stock, and availability truth |
| BCART / BCHK | Governed intent or orchestration facts only where communication is Approved | Cart and Checkout identity, lifecycle, intent, and orchestration truth |
| BORD | Governed Order facts and immutable historical context | Order identity, lifecycle, items, snapshots, cancellation, and history |
| BSHP | Governed Shipment, tracking, delivery, exception, and reverse-logistics evidence | Shipping, Fulfilment, Carrier, Shipment, provider, and delivery truth |
| BPAY | Governed Payment, Refund, and financial-outcome evidence | Payment, Refund, Refund Transaction, provider, amount, and financial truth |
| BRET | Governed Return evidence | Return identity, eligibility, lifecycle, inspection, disposition, recovery, and reconciliation |
| BCMS | Governed content only where materially applicable | CMS content, publication, placement, editorial, and policy-presentation truth |
| Reporting | Protected Notifications evidence through a future governed Contract | Reporting definitions, reports, analytics, exports, dashboards, and Projections |
| Administration | Future protected invocation of explicit BNTF capabilities | Administration workflows, Roles, Permissions, approval, override, escalation, and service policy |

## 4. Requirement Traceability

| Requirement | Notifications Domain source | BEB / governing source | Acceptance Criterion |
| --- | --- | --- | --- |
| BNTF-REQ-001 | REQ-NTF-001 | BEB-REQ-001–004; ADR-0016 | BNTF-AC-001 |
| BNTF-REQ-002 | REQ-NTF-001–056 | BEB-REQ-002–004, 055 | BNTF-AC-002 |
| BNTF-REQ-003 | REQ-NTF-001, 003 | ADR-0016; ARCHITECTURE.md §35 | BNTF-AC-003 |
| BNTF-REQ-004 | REQ-NTF-056 | BEB-REQ-001–056 | BNTF-AC-004 |
| BNTF-REQ-005 | REQ-NTF-002–003 | BEB-REQ-005–007 | BNTF-AC-005 |
| BNTF-REQ-006 | REQ-NTF-007–008, 050 | BEB-REQ-008–018, 041 | BNTF-AC-006 |
| BNTF-REQ-007 | REQ-NTF-002, 005–006, 017, 025–026, 041–044 | BEB-REQ-021–031, 049–051 | BNTF-AC-007 |
| BNTF-REQ-008 | REQ-NTF-002–004, 028 | BEB-REQ-003–004, 021 | BNTF-AC-008 |
| BNTF-REQ-009 | REQ-NTF-005–006, 025 | BEB-REQ-023–024, 029–031 | BNTF-AC-009 |
| BNTF-REQ-010 | REQ-NTF-007–008 | BEB-REQ-009–010, 015, 041 | BNTF-AC-010 |
| BNTF-REQ-011 | REQ-NTF-009 | BEB-REQ-027, 029–031 | BNTF-AC-011 |
| BNTF-REQ-012 | REQ-NTF-010–011 | BEB-REQ-011, 019–020, 043 | BNTF-AC-012 |
| BNTF-REQ-013 | REQ-NTF-012–014, 037 | BEB-REQ-003–004, 021, 043 | BNTF-AC-013 |
| BNTF-REQ-014 | REQ-NTF-014–015 | BEB-REQ-003–004, 056 | BNTF-AC-014 |
| BNTF-REQ-015 | REQ-NTF-016–017 | BEB-REQ-021, 023–024 | BNTF-AC-015 |
| BNTF-REQ-016 | REQ-NTF-018–019 | BEB-REQ-003–004, 021, 041 | BNTF-AC-016 |
| BNTF-REQ-017 | REQ-NTF-020, 052 | BEB-REQ-015, 041, 043 | BNTF-AC-017 |
| BNTF-REQ-018 | REQ-NTF-021–022 | BEB-REQ-003–004, 056 | BNTF-AC-018 |
| BNTF-REQ-019 | REQ-NTF-023–024 | BEB-REQ-032–036, 041–044 | BNTF-AC-019 |
| BNTF-REQ-020 | REQ-NTF-025–026 | BEB-REQ-021, 023–024, 028 | BNTF-AC-020 |
| BNTF-REQ-021 | REQ-NTF-027–028 | BEB-REQ-024, 026, 034, 041 | BNTF-AC-021 |
| BNTF-REQ-022 | REQ-NTF-029–030 | BEB-REQ-028–034, 041–042, 051 | BNTF-AC-022 |
| BNTF-REQ-023 | REQ-NTF-031–032 | BEB-REQ-024, 027–031, 041–042 | BNTF-AC-023 |
| BNTF-REQ-024 | REQ-NTF-033 | BEB-REQ-003–004, 021 | BNTF-AC-024 |
| BNTF-REQ-025 | REQ-NTF-034 | BEB-REQ-003–004, 021, 044 | BNTF-AC-025 |
| BNTF-REQ-026 | REQ-NTF-035 | BEB-REQ-003–004, 021 | BNTF-AC-026 |
| BNTF-REQ-027 | REQ-NTF-036 | BEB-REQ-003–004, 021 | BNTF-AC-027 |
| BNTF-REQ-028 | REQ-NTF-037–038 | BEB-REQ-003–004, 019–021, 043 | BNTF-AC-028 |
| BNTF-REQ-029 | REQ-NTF-039 | BEB-REQ-003–004, 021 | BNTF-AC-029 |
| BNTF-REQ-030 | REQ-NTF-040 | BEB-REQ-003–004, 021 | BNTF-AC-030 |
| BNTF-REQ-031 | REQ-NTF-041 | BEB-REQ-026, 032–034, 041–042 | BNTF-AC-031 |
| BNTF-REQ-032 | REQ-NTF-009, 042 | BEB-REQ-027, 029–031, 035–039 | BNTF-AC-032 |
| BNTF-REQ-033 | REQ-NTF-043 | BEB-REQ-029–031, 041–042, 045 | BNTF-AC-033 |
| BNTF-REQ-034 | REQ-NTF-044 | BEB-REQ-034, 036, 039, 042 | BNTF-AC-034 |
| BNTF-REQ-035 | REQ-NTF-045–046 | BEB-REQ-011, 019–020, 043, 045 | BNTF-AC-035 |
| BNTF-REQ-036 | REQ-NTF-010–015, 047 | BEB-REQ-019–020, 043 | BNTF-AC-036 |
| BNTF-REQ-037 | REQ-NTF-024, 038, 048–049 | BEB-REQ-035, 043–046 | BNTF-AC-037 |
| BNTF-REQ-038 | REQ-NTF-050 | BEB-REQ-008, 013–018 | BNTF-AC-038 |
| BNTF-REQ-039 | REQ-NTF-051 | BEB-REQ-037–040, 043 | BNTF-AC-039 |
| BNTF-REQ-040 | REQ-NTF-020, 052 | BEB-REQ-015, 052–055 | BNTF-AC-040 |
| BNTF-REQ-041 | REQ-NTF-053 | BEB-REQ-046, 051, 054 | BNTF-AC-041 |
| BNTF-REQ-042 | REQ-NTF-054 | BEB-REQ-045 | BNTF-AC-042 |
| BNTF-REQ-043 | REQ-NTF-055 | BEB-REQ-003–004, 021, 043 | BNTF-AC-043 |
| BNTF-REQ-044 | REQ-NTF-007, 041–044 | BEB-REQ-012, 025–026 | BNTF-AC-044 |
| BNTF-REQ-045 | REQ-NTF-015, 021–024, 053 | BEB-REQ-047–048 | BNTF-AC-045 |
| BNTF-REQ-046 | REQ-NTF-026, 041–044, 053 | BEB-REQ-049–051 | BNTF-AC-046 |
| BNTF-REQ-047 | REQ-NTF-056 | BEB-REQ-052–055 | BNTF-AC-047 |
| BNTF-REQ-048 | REQ-NTF-003, 013–015, 018–023, 029–030, 045, 049–051, 053–056 | BEB-REQ-056 | BNTF-AC-048 |
| BNTF-REQ-049 | REQ-NTF-023–024, 041–044 | BEB-REQ-032–036, 041–044 | BNTF-AC-049 |
| BNTF-REQ-050 | REQ-NTF-012, 017, 026, 047–048 | PRODUCT.md §24 items 19, 28; BEB-REQ-024, 043 | BNTF-AC-050 |
| BNTF-REQ-051 | REQ-NTF-045, 055 | ADR-0016; ARCHITECTURE.md §35 | BNTF-AC-051 |
| BNTF-REQ-052 | REQ-NTF-001, 056 | DOCUMENTATION-STANDARDS.md; ADR-0016 | BNTF-AC-052 |

## 5. Notifications Domain Requirement Coverage

| Notifications Requirement | BNTF specialization |
| --- | --- |
| REQ-NTF-001 | BNTF-REQ-001–003 |
| REQ-NTF-002 | BNTF-REQ-002, 007–008 |
| REQ-NTF-003 | BNTF-REQ-003, 008, 048 |
| REQ-NTF-004 | BNTF-REQ-008, 021 |
| REQ-NTF-005 | BNTF-REQ-009 |
| REQ-NTF-006 | BNTF-REQ-009 |
| REQ-NTF-007 | BNTF-REQ-010, 044 |
| REQ-NTF-008 | BNTF-REQ-010 |
| REQ-NTF-009 | BNTF-REQ-011, 032 |
| REQ-NTF-010 | BNTF-REQ-012, 036 |
| REQ-NTF-011 | BNTF-REQ-012 |
| REQ-NTF-012 | BNTF-REQ-013, 050 |
| REQ-NTF-013 | BNTF-REQ-013, 036, 048 |
| REQ-NTF-014 | BNTF-REQ-013–014 |
| REQ-NTF-015 | BNTF-REQ-014, 045, 048 |
| REQ-NTF-016 | BNTF-REQ-015 |
| REQ-NTF-017 | BNTF-REQ-015, 050 |
| REQ-NTF-018 | BNTF-REQ-016, 048 |
| REQ-NTF-019 | BNTF-REQ-016 |
| REQ-NTF-020 | BNTF-REQ-017, 040 |
| REQ-NTF-021 | BNTF-REQ-018, 045, 048 |
| REQ-NTF-022 | BNTF-REQ-018, 048 |
| REQ-NTF-023 | BNTF-REQ-019, 045, 048–049 |
| REQ-NTF-024 | BNTF-REQ-019, 037, 045, 049 |
| REQ-NTF-025 | BNTF-REQ-009, 020 |
| REQ-NTF-026 | BNTF-REQ-007, 020, 046, 050 |
| REQ-NTF-027 | BNTF-REQ-021 |
| REQ-NTF-028 | BNTF-REQ-008, 021 |
| REQ-NTF-029 | BNTF-REQ-022, 048 |
| REQ-NTF-030 | BNTF-REQ-022, 048 |
| REQ-NTF-031 | BNTF-REQ-023 |
| REQ-NTF-032 | BNTF-REQ-023 |
| REQ-NTF-033 | BNTF-REQ-024 |
| REQ-NTF-034 | BNTF-REQ-025 |
| REQ-NTF-035 | BNTF-REQ-026 |
| REQ-NTF-036 | BNTF-REQ-027 |
| REQ-NTF-037 | BNTF-REQ-013, 028 |
| REQ-NTF-038 | BNTF-REQ-028, 037 |
| REQ-NTF-039 | BNTF-REQ-029 |
| REQ-NTF-040 | BNTF-REQ-030 |
| REQ-NTF-041 | BNTF-REQ-007, 031–032, 044, 046, 049 |
| REQ-NTF-042 | BNTF-REQ-032 |
| REQ-NTF-043 | BNTF-REQ-007, 033–034, 046, 049 |
| REQ-NTF-044 | BNTF-REQ-007, 034, 046, 049 |
| REQ-NTF-045 | BNTF-REQ-035, 048, 051 |
| REQ-NTF-046 | BNTF-REQ-035 |
| REQ-NTF-047 | BNTF-REQ-036, 050 |
| REQ-NTF-048 | BNTF-REQ-037, 050 |
| REQ-NTF-049 | BNTF-REQ-037, 048 |
| REQ-NTF-050 | BNTF-REQ-006, 038, 048 |
| REQ-NTF-051 | BNTF-REQ-039, 048 |
| REQ-NTF-052 | BNTF-REQ-017, 040 |
| REQ-NTF-053 | BNTF-REQ-041, 045, 048 |
| REQ-NTF-054 | BNTF-REQ-042 |
| REQ-NTF-055 | BNTF-REQ-043, 051 |
| REQ-NTF-056 | BNTF-REQ-002, 004, 047–048, 052 |

All 56 Approved Notifications Domain Requirements are accounted for exactly once as coverage rows, with additional specialization references where materially required.

## 6. BEB Applicability and Inheritance Matrix

Every BEB Requirement is materially applicable to BNTF. Some obligations activate only when the governed interaction exists; conditional activation does not waive inheritance. No BEB Requirement is classified as non-applicable.

| BEB Requirement | Classification | BNTF application |
| --- | --- | --- |
| BEB-REQ-001 | Applicable | Lifecycle and bounded authority: BNTF-REQ-001. |
| BEB-REQ-002 | Applicable | Complete inherited accounting: BNTF-REQ-004. |
| BEB-REQ-003 | Applicable | External Domain and Product non-authority: BNTF-REQ-008, 013–030, 043. |
| BEB-REQ-004 | Applicable | Notifications-only specialization: BNTF-REQ-002–003. |
| BEB-REQ-005 | Applicable | Modular monolith boundary: BNTF-REQ-005. |
| BEB-REQ-006 | Applicable | Hexagonal dependency direction: BNTF-REQ-005. |
| BEB-REQ-007 | Applicable | Layer responsibilities: BNTF-REQ-005–006. |
| BEB-REQ-008 | Applicable | Intentional public Contract: BNTF-REQ-006, 038. |
| BEB-REQ-009 | Applicable | Use Case boundary: BNTF-REQ-006, 010. |
| BEB-REQ-010 | Applicable | Command/query semantics: BNTF-REQ-006, 010. |
| BEB-REQ-011 | Applicable | Contextual Authorization: BNTF-REQ-012, 035. |
| BEB-REQ-012 | Applicable | Application transaction ownership: BNTF-REQ-044. |
| BEB-REQ-013 | Applicable | REST/versioned API baseline applies if HTTP is exposed, without selecting routes: BNTF-REQ-006, 038. |
| BEB-REQ-014 | Applicable | Project-owned DTO boundary: BNTF-REQ-006, 038. |
| BEB-REQ-015 | Applicable | Validation and bounds: BNTF-REQ-006, 010, 017, 040. |
| BEB-REQ-016 | Applicable | Any collection query remains bounded and explicit: BNTF-REQ-006, 041. |
| BEB-REQ-017 | Applicable | Safe API error Contract: BNTF-REQ-006, 031, 038. |
| BEB-REQ-018 | Applicable | Contract description and evolution: BNTF-REQ-006, 038. |
| BEB-REQ-019 | Applicable | Authentication boundary: BNTF-REQ-012, 028, 035–036. |
| BEB-REQ-020 | Applicable | Authorization and concealment: BNTF-REQ-012, 035–037. |
| BEB-REQ-021 | Applicable | Notifications-only data ownership: BNTF-REQ-007–008, 013–030, 043. |
| BEB-REQ-022 | Applicable | Persistence Port and mapping: BNTF-REQ-007. |
| BEB-REQ-023 | Applicable | Integrity constraints: BNTF-REQ-007, 009, 015, 020. |
| BEB-REQ-024 | Applicable | Historical truth: BNTF-REQ-007, 009, 020–023, 050. |
| BEB-REQ-025 | Applicable | Consistency and atomicity: BNTF-REQ-007, 044. |
| BEB-REQ-026 | Applicable | External-call separation: BNTF-REQ-021, 031, 044. |
| BEB-REQ-027 | Applicable | Concurrency control: BNTF-REQ-011, 032. |
| BEB-REQ-028 | Applicable | Durable workflow state: BNTF-REQ-007, 020, 022. |
| BEB-REQ-029 | Applicable | Idempotency where harmful duplication is possible: BNTF-REQ-007, 009, 011, 022, 032–033. |
| BEB-REQ-030 | Applicable | Stable idempotency identity/outcome without mechanism choice: BNTF-REQ-009, 011, 032. |
| BEB-REQ-031 | Applicable | Replay and duplicate safety: BNTF-REQ-011, 022–023, 032–033. |
| BEB-REQ-032 | Applicable | Provider Port and Adapter boundary: BNTF-REQ-019, 031, 049. |
| BEB-REQ-033 | Applicable | Provider resilience without invented targets: BNTF-REQ-019, 022, 031, 049. |
| BEB-REQ-034 | Applicable | Provider uncertainty and reconciliation: BNTF-REQ-019, 021–022, 031, 034, 049. |
| BEB-REQ-035 | Applicable | Any provider callback/webhook requires authenticity and integrity: BNTF-REQ-019, 032, 037, 049. |
| BEB-REQ-036 | Applicable | Any callback/webhook requires replay-safe recovery: BNTF-REQ-019, 032, 034, 049. |
| BEB-REQ-037 | Applicable | Domain/Integration Event separation whenever events apply: BNTF-REQ-039. |
| BEB-REQ-038 | Applicable | Governed event Contract/envelope whenever events apply: BNTF-REQ-039. |
| BEB-REQ-039 | Applicable | Event delivery/consumption safety whenever events apply: BNTF-REQ-032, 034, 039. |
| BEB-REQ-040 | Applicable | External messaging adoption remains separately governed: BNTF-REQ-039, 048. |
| BEB-REQ-041 | Applicable | Error classification and translation: BNTF-REQ-006, 010, 016–017, 019, 021–023, 031, 049. |
| BEB-REQ-042 | Applicable | Recovery and reconciliation: BNTF-REQ-022–023, 031, 033–034. |
| BEB-REQ-043 | Applicable | Backend security and data protection: BNTF-REQ-012–013, 017, 019, 035–039. |
| BEB-REQ-044 | Applicable | Secret and Payment-data safety: BNTF-REQ-019, 025, 037, 049. |
| BEB-REQ-045 | Applicable | Audit/log separation: BNTF-REQ-033, 035, 037, 042. |
| BEB-REQ-046 | Applicable | Observability and health: BNTF-REQ-037, 041. |
| BEB-REQ-047 | Applicable | Configuration/environment safety: BNTF-REQ-045. |
| BEB-REQ-048 | Applicable | Feature-flag safety without provider selection: BNTF-REQ-045, 048. |
| BEB-REQ-049 | Applicable | Version-controlled Flyway migration governance: BNTF-REQ-046. |
| BEB-REQ-050 | Applicable | Backward-compatible mixed-version deployment: BNTF-REQ-046. |
| BEB-REQ-051 | Applicable | Bounded work and failure containment: BNTF-REQ-022, 041, 046. |
| BEB-REQ-052 | Applicable | Unit and application verification: BNTF-REQ-040, 047. |
| BEB-REQ-053 | Applicable | Adapter/integration verification: BNTF-REQ-019, 039, 047, 049. |
| BEB-REQ-054 | Applicable | Architecture/operational verification: BNTF-REQ-041, 047. |
| BEB-REQ-055 | Applicable | Complete traceable verification: BNTF-REQ-002, 004, 047, 052. |
| BEB-REQ-056 | Applicable | Policy and implementation neutrality: BNTF-REQ-014, 018, 022, 048. |

All 56 BEB Requirements are accounted for as materially applicable; explicitly non-applicable count is zero.

## 7. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BNTF-AC-001 | BNTF-REQ-001 | Metadata shows `1.0.0 Approved`, `authoritative: false`, scope `BNTF`, normativity only within BNTF scope, bounded authority, and governing-source subordination. |
| BNTF-AC-002 | BNTF-REQ-002 | The Domain matrix accounts for every `REQ-NTF-001` through `REQ-NTF-056`. |
| BNTF-AC-003 | BNTF-REQ-003 | BNTF is Notifications-only after BCMS; approval closes only Administration's Notifications prerequisite, Reporting remains unresolved and unpositioned, Administration remains blocked by Reporting, and every later position remains contained. |
| BNTF-AC-004 | BNTF-REQ-004 | The BEB matrix accounts individually for all 56 BEB Requirements with BNTF traces. |
| BNTF-AC-005 | BNTF-REQ-005 | Architecture evidence preserves Module, layer, Port, Adapter, and inward dependency boundaries. |
| BNTF-AC-006 | BNTF-REQ-006 | Contracts and Use Cases are intentional, bounded, validated, compatible, failure-safe, and free of concrete transport authority. |
| BNTF-AC-007 | BNTF-REQ-007 | Persistence evidence contains only Notifications truth and preserves identity, history, integrity, uncertainty, and recovery without physical design. |
| BNTF-AC-008 | BNTF-REQ-008 | No request, render, queue, attempt, provider evidence, display, or delivery outcome changes or proves external truth. |
| BNTF-AC-009 | BNTF-REQ-009 | Notification and attempt identities remain distinct and source provenance and correlation remain validated without identifier design. |
| BNTF-AC-010 | BNTF-REQ-010 | Request states remain distinct; only sufficiently governed evidence permits acceptance, and receipt proves no send or delivery. |
| BNTF-AC-011 | BNTF-REQ-011 | Duplicate, replayed, concurrent, and ambiguous requests preserve effects and yield explicit safe outcomes without mechanism selection. |
| BNTF-AC-012 | BNTF-REQ-012 | Minimum recipient context is consumed with freshness and disclosure controls, and all protected views and actions enforce isolation. |
| BNTF-AC-013 | BNTF-REQ-013 | BCUS authority remains intact; retained evidence is purpose-bound and infers no Consent, mutation, or access. |
| BNTF-AC-014 | BNTF-REQ-014 | Transactional and marketing contexts remain distinct, uncertainty blocks invented action, and no classification or eligibility policy is selected. |
| BNTF-AC-015 | BNTF-REQ-015 | Notification templates have stable evidence and transfer no CMS, policy, commercial, Customer, legal, or source authority. |
| BNTF-AC-016 | BNTF-REQ-016 | Ineligible or uncertain templates fail safely; BCMS content retains CMS authority and is not made universally required. |
| BNTF-AC-017 | BNTF-REQ-017 | Rendering preserves validated meaning, safety, privacy, integrity, and accessibility and fails safely for invalid content. |
| BNTF-AC-018 | BNTF-REQ-018 | Channel, fallback, suppression, and cancellation require governing policy and no channel or behavior is invented. |
| BNTF-AC-019 | BNTF-REQ-019 | Provider interactions use owned Ports and validated evidence; provider terms never redefine Notification or source truth. |
| BNTF-AC-020 | BNTF-REQ-020 | Attempts remain distinguishable and Delivery Status changes preserve attributable history, ordering context, and evidence. |
| BNTF-AC-021 | BNTF-REQ-021 | Material outcomes remain distinct and no acknowledgement, queue state, display, or provider acceptance falsely proves delivery or business success. |
| BNTF-AC-022 | BNTF-REQ-022 | Retry and exhaustion preserve current evidence and duplicate safety while selecting no policy, count, timing, or mechanism. |
| BNTF-AC-023 | BNTF-REQ-023 | Changed source facts are evaluated against owner truth; Notification and source cancellation remain distinct and unknown effects remain uncertain. |
| BNTF-AC-024 | BNTF-REQ-024 | Order communication preserves BORD authority, snapshots, and history and creates or transitions no Order. |
| BNTF-AC-025 | BNTF-REQ-025 | Payment communication preserves BPAY authority and proves no financial success or duplicate financial effect. |
| BNTF-AC-026 | BNTF-REQ-026 | Shipping communication preserves BSHP authority and exposes no unnecessary provider internals. |
| BNTF-AC-027 | BNTF-REQ-027 | Return communication preserves BRET and adjacent Domain authority and performs no Return, Refund, restock, or transport outcome. |
| BNTF-AC-028 | BNTF-REQ-028 | Customer and Identity communications consume bounded evidence and prove no Customer, Identity, Session, verification, or Authorization outcome. |
| BNTF-AC-029 | BNTF-REQ-029 | Cart/Checkout communications preserve intent and orchestration authority and prove no downstream outcome or abandonment policy. |
| BNTF-AC-030 | BNTF-REQ-030 | Catalogue, taxonomy, Price, Money, Stock, and availability representations remain validated, contextual, uncertain where applicable, and non-authoritative. |
| BNTF-AC-031 | BNTF-REQ-031 | Every material failure and unknown effect remains explicit, safe, diagnosable, and recoverable; timeout proves no provider stop. |
| BNTF-AC-032 | BNTF-REQ-032 | Repeated and concurrent activity preserves history, checks prior effects, prevents unauthorized duplicates, and retains uncertainty without mechanism choice. |
| BNTF-AC-033 | BNTF-REQ-033 | Recovery is explicit, authorized, attributable, source-constrained, duplicate-safe, materially auditable, and bypasses no policy. |
| BNTF-AC-034 | BNTF-REQ-034 | Reconciliation correlates all listed evidence and keeps provenance, uncertainty, history, and discrepancies visible until repair. |
| BNTF-AC-035 | BNTF-REQ-035 | Administration uses only protected BNTF capabilities; contextual Authorization is server-side and no workflow or Role/Permission policy is invented. |
| BNTF-AC-036 | BNTF-REQ-036 | Security and privacy evidence demonstrates least privilege, denial, isolation, minimization, purpose control, and protected data across all surfaces. |
| BNTF-AC-037 | BNTF-REQ-037 | Secrets and prohibited data are absent from unauthorized evidence, and communicated compliance facts retain owning-source authority. |
| BNTF-AC-038 | BNTF-REQ-038 | Contract review shows necessary abstract semantics, compatibility, privacy, uncertainty, and safety with no concrete interface or data design. |
| BNTF-AC-039 | BNTF-REQ-039 | Events remain governed and producer-authoritative, and no event, payload, broker, queue, topic, or messaging mechanism is invented. |
| BNTF-AC-040 | BNTF-REQ-040 | Applicable content and experiences provide verifiable WCAG 2.2 AA semantics and recovery without implementation prescription or certification claim. |
| BNTF-AC-041 | BNTF-REQ-041 | Diagnostic and health evidence is bounded, correlated, attributable, truthful, protected, and free of invented products or targets. |
| BNTF-AC-042 | BNTF-REQ-042 | Material high-Risk actions produce proportional tamper-resistant Audit Records distinct from logs; routine work is not over-audited. |
| BNTF-AC-043 | BNTF-REQ-043 | Downstream representations remain protected, source-traceable, stale-capable, non-authoritative, and unable to mutate truth; Reporting remains unpositioned. |
| BNTF-AC-044 | BNTF-REQ-044 | Transaction review shows explicit consistency boundaries and safe separation of uncertain external effects without mechanism selection. |
| BNTF-AC-045 | BNTF-REQ-045 | Configuration and possible feature-controlled state are validated, safe, observable, compatible, and non-authoritative without a selected system. |
| BNTF-AC-046 | BNTF-REQ-046 | Evolution evidence confirms governed Flyway migrations, immutable history, no runtime mutation or drift, mixed-version safety, forward recovery, and bounded work. |
| BNTF-AC-047 | BNTF-REQ-047 | Layered verification covers every Requirement and material negative path with traceability and no mandated tooling or target. |
| BNTF-AC-048 | BNTF-REQ-048 | Review finds all Open Decisions unresolved and no unauthorized policy, API, schema, event, provider, cache, infrastructure, numerical, or Role/Permission choice. |
| BNTF-AC-049 | BNTF-REQ-049 | Provider and callback interactions preserve authenticity, integrity, correlation, replay safety, privacy, uncertainty, recovery, and non-authority. |
| BNTF-AC-050 | BNTF-REQ-050 | Historical evidence is preserved under governing policy; unresolved correction, deletion, closure, and retention rules remain explicit with no local mechanism or period. |
| BNTF-AC-051 | BNTF-REQ-051 | Approved BNTF closes only Administration's Notifications prerequisite; the Reporting prerequisite remains missing, Reporting remains independently eligible and unresolved, Administration remains blocked, and no later position is created. |
| BNTF-AC-052 | BNTF-REQ-052 | Counts, matrices, decisions, review requirements, documents, history, and validation are complete, consistent, and lifecycle-correct. |

## 8. Open Product Decisions

The following **15 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and Accepted ADR-0016 remain unresolved:

| Item | Open Product Decision | BNTF preservation |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Visitor, Account, recipient, or communication prerequisite selected. |
| 5 | Customer email-verification requirements. | No verification requirement or mechanism selected; delivery proves no verification. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, fee, or Shipping policy selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility or cutoff selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, Exchange, Refund, eligibility, or treatment policy selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, workflow, expectation, or target selected. |
| 19 | Marketing-consent and communication-preference model. | No marketing permission, Consent, Preference, channel, fallback, or suppression policy selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, taxonomy, or instrumentation mechanism selected. |
| 21 | Initial reporting and export requirements. | No report, export, audience, purpose, format, or schedule selected. |
| 22 | Content approval and scheduled-publication workflow. | No template or contributed-content approval chain, schedule, or publication mechanism selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No resend, investigation, recovery, escalation, service, or support process selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, or document-content policy selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No fraud rule, provider, score, threshold, review, or communication policy selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No contact-data, delivery-evidence, retention, correction, deletion, or closure policy selected. |

## 9. Open Architecture Decisions

The following **9 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 and Accepted ADR-0016 remain unresolved:

| Item | Open Architecture Decision | BNTF preservation |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting or topology selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism selected. |
| 7 | Transactional notification provider selection. | No provider, channel implementation, SDK, or Adapter selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, retry, or workflow-state use selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or persistence layout selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle selected. |

## 10. Explicit Non-Decisions

BNTF selects no communication eligibility, transactional/marketing classification, Consent or Preference policy, channel availability or priority, fallback, suppression, cancellation semantics, template approval, provider or routing, failover, retry or exhaustion policy, escalation, retention, API route, HTTP detail, DTO or payload, database schema, table, column, index, ORM mapping, identifier format, event name or payload, topic, queue, broker, cache, Redis use, storage or infrastructure product, hosting or deployment topology, timeout, TTL, rate limit, SLA, SLO, RTO, RPO, Role/Permission matrix, or other unresolved Product, Architecture, numerical, or implementation choice.

## 11. Dependency and Roadmap Containment

Approved BNTF closes only Administration's Notifications backend invocation prerequisite. Administration remains blocked by the missing Approved Reporting backend invocation Contract, so BNTF approval alone does not make Administration eligible or authorize its Draft.

Reporting remains independently eligible, separate, unresolved, unranked, and unordered beyond BNTF. BNTF does not assign Reporting a title, path, scope, decomposition, or position and does not imply that Reporting follows BNTF. Every post-BNTF roadmap position remains unresolved.

## 12. Risks and Controls

| Risk | Control |
| --- | --- |
| Notification evidence becomes source truth | Preserve producer authority and explicit communication/source separation. |
| Provider acknowledgement is treated as delivery | Preserve distinct attempt, acceptance, delivery, failure, and uncertainty outcomes. |
| Duplicate communication causes harm | Require stable effect identity, prior-effect inspection, replay safety, and reconciliation. |
| Sensitive recipient or security data leaks | Minimize data and protect content, provider, log, event, export, and audit surfaces. |
| Stale content or facts are communicated | Validate source and template evidence and retain staleness, conflict, and uncertainty. |
| Retry multiplies effects | Require governed policy, current evidence, duplicate safety, and explicit exhaustion. |
| Templates acquire CMS or policy authority | Keep template scope Notification-specific and preserve external authority. |
| BNTF approval is treated as downstream authorization | Preserve Administration dependencies, Reporting eligibility, and roadmap containment. |

## 13. Required Governance Reviews

Approval-readiness review completed with no blockers across Architecture and Notifications ownership; materially affected Identity, Customer, Product, Category, Inventory, Pricing, Cart, Checkout, Order, Shipping and Fulfilment, Payment, Return, CMS, Reporting, and Administration ownership where Contracts or evidence intersect; Security and Privacy; Accessibility; Testing; Operations; and Documentation. The review confirmed complete Domain and BEB accounting, one-to-one Requirement/Acceptance-Criterion traceability, provider and cross-Domain non-authority, exact Open Decision inventories, implementation neutrality, dependency state, and roadmap containment. No reviewer identity, signature, ticket, date beyond the governed document date, or external evidence is asserted.

## 14. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/VISION.md`
- `.ai/core/DECISIONS.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0016-post-cms-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/checkout/checkout-backend.md`
- `specifications/backend/order/order-backend.md`
- `specifications/backend/shipping/shipping-backend.md`
- `specifications/backend/payment/payment-backend.md`
- `specifications/backend/return/return-backend.md`
- `specifications/backend/cms/cms-backend.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/admin/admin-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-24 | Approved | Promoted the Notifications Backend Specification after approval-readiness validation confirmed complete Notifications Domain coverage, BEB accounting, traceability, authority boundaries, unresolved-decision preservation, implementation neutrality, and roadmap containment with no blockers. |
| 0.1.0 | 2026-09-24 | Draft | Established the initial Notifications-only BNTF backend Specification authorized by Accepted ADR-0016, specializing the Approved Notifications Domain and materially applicable BEB Requirements while preserving external authority, unresolved decisions, implementation neutrality, and post-BNTF roadmap containment. |

## 16. Final Validation

Final approval validation confirms that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, owner is `Engineering`, scope is `BNTF`, and Requirements are normative only within BNTF scope and subordinate to governing authority;
2. decomposition is Notifications-only, BNTF immediately follows Approved BCMS, and every owning-Domain authority boundary remains intact;
3. all 52 BNTF Requirements and 52 corresponding Acceptance Criteria are unique, contiguous, independently reviewable, and one-to-one;
4. all 52 Requirement Traceability rows map each BNTF Requirement to exactly one Acceptance Criterion and materially supporting sources, with no orphan or duplicate identifier;
5. all 56 Approved Notifications Domain Requirements and all 56 BEB Requirements are explicitly accounted for, with 56 materially applicable and zero explicitly non-applicable BEB Requirements;
6. exactly Product Decisions `4, 5, 7, 10, 11, 18, 19, 20, 21, 22, 23, 24, 25, 26, 28` and Architecture Decisions `1, 2, 3, 7, 8, 9, 12, 13, 14` remain unresolved;
7. Notifications identity, requests, recipient context, templates, rendering, channels, providers, attempts, Delivery Status, retry, exhaustion, stale facts, failure, recovery, reconciliation, accessibility, security, privacy, observability, audit, Contracts, conditional events, and verification are covered;
8. no external Domain authority, concrete cross-Domain Contract, event, provider, API, DTO, persistence, cache, infrastructure, deployment, Role/Permission, numerical, or unresolved policy choice is introduced;
9. approval-readiness review completed with no Critical, High, Medium, or Low blockers and without invented reviewer identities or external evidence;
10. Revision History contains exactly one preserved `0.1.0 Draft` entry and one `1.0.0 Approved` entry;
11. Related Documents exist and terminology remains consistent with governing sources;
12. repository validation confirms the BNTF change affects only `specifications/backend/notifications/notifications-backend.md`, whitespace validation passes, no unrelated repository changes exist, and the staging area remains empty; and
13. Approved BNTF closes only Administration's Notifications invocation prerequisite; Administration remains blocked by the missing Approved Reporting invocation Contract, Reporting remains independently eligible, separate, unresolved, unranked, and unordered, and no post-BNTF roadmap position is established.
