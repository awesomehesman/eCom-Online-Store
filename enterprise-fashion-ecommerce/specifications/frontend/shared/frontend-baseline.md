---
title: Shared Frontend Baseline Specification
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-07
authoritative: false
---

# Shared Frontend Baseline Specification

## 1. Purpose

This Specification defines the implementation-neutral shared frontend obligations that every later customer-facing and Staff User-facing frontend experience must preserve.

This document uses scope code `FEB`. Its Approved Requirements are normative only within the Shared Frontend Baseline scope and are not repository-wide authority. They remain subordinate to higher-authority governing sources, preserve every Approved Domain's authority, and resolve no Open Product Decision.

## 2. Scope, Authority, and Requirements

### REQ-FEB-001 — Lifecycle, Authority, and Scope

Shared frontend behavior MUST be governed only under scope `FEB`, preserve governing-source precedence and Approved Domain authority, and MUST treat this Approved Specification as normative only within the Shared Frontend Baseline scope and not as repository-wide authority.

### REQ-FEB-002 — Shared Frontend Authority

FEB MUST own only cross-journey presentation-state semantics, interaction safeguards, accessibility obligations, Design System consumption, frontend evidence, and shared verification boundaries; later frontend Specifications MUST preserve these shared obligations without transferring source-Domain authority to FEB.

### REQ-FEB-003 — Cross-Domain Non-Authority

FEB MUST NOT own or redefine Product, Category, Customer, Identity, Inventory, Cart, Pricing, Payment, Shipping and Fulfilment, Checkout, Order, Return, Notifications, CMS, Administration, Reporting, Search and Discovery, Analytics, fraud, tax, legal, commercial, or Product-policy truth.

### REQ-FEB-004 — Frontend Representation Non-Authority

Frontend state MUST remain a representation and MUST NOT independently establish Authentication, Authorization, ownership, eligibility, business state, mutation success, or current commercial truth.

### REQ-FEB-005 — Authoritative Revalidation

Where an interaction requires current authoritative truth, the frontend MUST consume or revalidate governed backend or owning-Domain evidence before representing that truth as confirmed.

### REQ-FEB-006 — Client-State Categories

Local, shared, cached, route, query, and temporary interaction state MUST have explicit ownership and lifetime appropriate to their use and MUST NOT silently become Server State or source-Domain truth.

### REQ-FEB-007 — Cache Freshness

Cached or projected data whose freshness is material MUST expose sufficient freshness, source, or revalidation context to prevent stale evidence from being represented as current authoritative truth.

### REQ-FEB-008 — State Isolation

Shared frontend state MUST prevent data or interaction context from leaking across Principals, Sessions, Customers, Staff Users, Resources, or other governed isolation boundaries.

### REQ-FEB-009 — Navigation Context

Applicable navigation MUST preserve meaningful user context and restore state safely without treating route state, history, parameters, or identifiers as authority.

### REQ-FEB-010 — Navigation Predictability

Navigation and frontend state changes MUST be predictable and preserve meaningful focus and status context, while concrete routes, labels, hierarchy, and journey-specific redirects remain outside FEB authority.

### REQ-FEB-011 — Authentication Presentation

The frontend MAY present trusted Identity-owned Authentication and Session context but MUST NOT authenticate a Principal or treat displayed Session information, prior responses, or client-held evidence as Authentication truth.

### REQ-FEB-012 — Contextual Authorization Non-Authority

Protected reads and mutations MUST depend on current server-side contextual Authorization for the Principal, Resource, action, and owning-Domain state; frontend conditions, route guards, or presentation logic MUST NOT be final Authorization.

### REQ-FEB-013 — Labels and Controls Non-Authority

Visible or hidden controls, Role labels, Permissions, Claims, Scope, feature flags, or possession of a Resource identifier MUST NOT independently authorize an operation or prove entitlement.

### REQ-FEB-014 — Applicable Presentation States

Each frontend experience MUST represent the materially applicable initial, loading, empty, validation-failure, denied, unavailable, failed, stale, partial, uncertain, pending, recovery, and confirmed-success states without requiring semantically inapplicable states.

### REQ-FEB-015 — State Distinction

Materially different denied, failed, unavailable, stale, partial, pending, uncertain, and confirmed outcomes MUST remain distinguishable and MUST NOT collapse into generic success or misleading completeness.

### REQ-FEB-016 — Loading and Pending Integrity

Loading and pending presentation MUST communicate that an outcome is not yet confirmed, preserve applicable user context, and prevent an in-flight state from being represented as completed business truth.

### REQ-FEB-017 — Asynchronous Supersession Safety

Stale, cancelled, reordered, or superseded asynchronous results MUST NOT overwrite newer relevant user intent or presentation state.

### REQ-FEB-018 — Empty-State Integrity

An empty presentation MUST distinguish a confirmed empty result from loading, filtering, denial, failure, staleness, partial data, or unknown completeness where those distinctions are material.

### REQ-FEB-019 — Validation Presentation

Frontend validation MUST provide timely, understandable, associated feedback while remaining distinct from authoritative backend and owning-Domain validation.

### REQ-FEB-020 — Failure Disclosure

Failure presentation MUST explain the safe known outcome and applicable next action without exposing unnecessary Sensitive Data, inaccessible Resource existence, provider internals, Secrets, or security detail.

### REQ-FEB-021 — Recovery Interaction

Where recovery is supported, the frontend MUST preserve known state, distinguish retry from a new intent, and invoke only governed recovery capabilities without defining server retry, idempotency, replay, or reconciliation mechanics.

### REQ-FEB-022 — Unknown-Effect Recovery

An uncertain or unknown-effect mutation MUST NOT be presented as success or automatically repeated as new intent; the frontend MUST support governed verification or reconciliation when exposed by the applicable Contract.

### REQ-FEB-023 — Duplicate Interaction Safety

Frontend interaction design MUST reduce accidental duplicate submission and preserve distinct outcomes without claiming to provide server-side duplicate-effect prevention.

### REQ-FEB-024 — Optimistic Presentation

Optimistic presentation MAY be used only when governing policy and Contract semantics permit it, and material optimistic state MUST remain distinguishable from authoritative confirmation.

### REQ-FEB-025 — Optimistic Correction Boundary

Failure, rejection, conflict, or uncertainty following an optimistic interaction MUST remain correctable, MUST NOT silently become success, and MUST NOT authorize another protected action.

### REQ-FEB-026 — Sensitive Data Minimization

Frontend views, client state, browser retention, errors, logs, telemetry, and Analytics Events MUST minimize Sensitive Data and expose it only for an authorized, necessary purpose.

### REQ-FEB-027 — Safe Rendering and Input

Untrusted content and input MUST be handled according to governing frontend and security standards so that presentation does not create unsafe execution, disclosure, navigation, or injection behavior.

### REQ-FEB-028 — Secret Non-Exposure

Secrets, provider credentials, privileged tokens, raw Payment data, and internal security detail MUST NOT be embedded in frontend-delivered content, routes, ordinary client state, logs, telemetry, or Analytics Events.

### REQ-FEB-029 — Consent Non-Inference

Frontend defaults, interaction, account state, purchase completion, or telemetry configuration MUST NOT create or infer Consent or Preference truth; applicable governed Customer evidence MUST be consumed.

### REQ-FEB-030 — Enumeration and Disclosure Resistance

Frontend outcomes MUST preserve governed Resource concealment and abuse resistance and MUST NOT reveal inaccessible Resource existence or sensitive account state through materially distinguishable unauthorized responses.

### REQ-FEB-031 — Responsive Outcome

Applicable frontend experiences MUST preserve essential information, meaning, actions, and state across supported viewport and orientation conditions without FEB inventing breakpoint values or page layouts.

### REQ-FEB-032 — Input-Modality Equivalence

Applicable functionality MUST remain operable through supported keyboard, pointer, touch, and assistive-technology interaction without requiring a single input modality.

### REQ-FEB-033 — Reflow, Zoom, and Content Extremes

Frontend composition MUST preserve comprehension and operation under required reflow, zoom, text-spacing, localization, and content-extreme conditions without obscuring or removing essential content or controls.

### REQ-FEB-034 — WCAG 2.2 AA Evidence

Critical Customer and Staff User frontend journeys MUST produce applicable WCAG 2.2 AA evidence without implying certification, universal conformance, or unsupported AAA compliance.

### REQ-FEB-035 — Keyboard and Focus Visibility

Interactive frontend behavior MUST provide complete keyboard operation, logical focus order, and visible, unobscured focus according to the Accessibility standard.

### REQ-FEB-036 — Focus After Change

Meaningful navigation, validation, recovery, overlay, and asynchronous state changes MUST move or preserve focus predictably so the resulting context is perceivable and operable.

### REQ-FEB-037 — Assistive-Technology Semantics

Frontend content and controls MUST expose appropriate semantic structure, accessible names, relationships, and state without replacing native semantics unnecessarily.

### REQ-FEB-038 — Accessible Status and Errors

Material loading, empty, validation, denied, unavailable, failure, partial, stale, uncertain, recovery, and success updates MUST be communicated accessibly without relying only on color, position, motion, or visual change.

### REQ-FEB-039 — Design System Consumption

Frontend experiences MUST consume governed Design System and Component Library abstractions where applicable and preserve their semantic, responsive, interaction, and accessibility behavior.

### REQ-FEB-040 — Design System Non-Authority

FEB MUST NOT define exact brand assets, colors, typography, spacing, elevation, breakpoints, motion values, Component anatomy, page layouts, or journey compositions owned by Product, Design System governance, or later frontend Specifications.

### REQ-FEB-041 — Content Resilience

Shared presentation MUST tolerate valid content length, absence, variation, language, media, and user-generated or CMS-managed content extremes without changing source meaning or authority.

### REQ-FEB-042 — Bounded Frontend Work

Rendering, collection handling, reactive work, retries, background activity, and client resource use MUST remain bounded by governed inputs and established performance evidence without FEB inventing numerical budgets.

### REQ-FEB-043 — Performance Correctness

Frontend performance optimization MUST preserve accessibility, security, privacy, Domain correctness, authoritative revalidation, and truthful failure or stale-state presentation.

### REQ-FEB-044 — Workload Isolation and Degradation

Expensive or degraded frontend activity MUST NOT silently block unrelated critical interaction, and applicable degraded presentation MUST preserve correct, safe outcomes.

### REQ-FEB-045 — Frontend Observability and Correlation

Material frontend failures and interaction outcomes MUST produce safe operational telemetry and preserve governed correlation evidence sufficient for diagnosis without making telemetry business truth.

### REQ-FEB-046 — Evidence-Type Separation

Frontend operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records MUST remain distinguishable; the frontend MUST NOT claim to create authoritative Domain history or required Audit Records through ordinary telemetry.

### REQ-FEB-047 — Analytics Boundary

Frontend Analytics Events MUST remain non-authoritative observations, respect applicable Consent and Sensitive Data constraints, and MUST NOT define provider, taxonomy, canonical event names, payloads, attribution, or business truth.

### REQ-FEB-048 — Feature-Flag Safety

Every reachable feature-flag or progressive-delivery state MUST preserve Authentication and Authorization boundaries, privacy, accessibility, Domain authority, and safe unsupported or unavailable presentation.

### REQ-FEB-049 — Abstract Contract Boundary

Frontend behavior MUST consume governed Contracts with enough explicit outcome, freshness, correlation, and bounded-result semantics for truthful presentation while FEB MUST NOT define routes, methods, operations, DTOs, schemas, wire formats, concrete status mappings, persistence, or event payloads.

### REQ-FEB-050 — Policy and Implementation Neutrality

FEB MUST remain neutral about providers, implementation mechanisms, route design, numerical thresholds, retry counts, timeouts, Product policy, and unresolved Product Decisions.

### REQ-FEB-051 — Verification Coverage

Shared frontend verification MUST cover applicable success, negative, degraded, accessibility, security, privacy, state-authority, feature-flag, performance, and Contract-boundary outcomes with traceability to the governing Requirement.

## 3. Terminology and Shared State Model

Canonical terms retain the meanings in `GLOSSARY.md`. In particular, Client State is frontend-created state, while Server State is backend-owned data cached, synchronized, invalidated, or refreshed by the frontend. Neither cached Server State nor a frontend representation transfers authority from its owning Domain.

For FEB only, a **presentation state** describes what the frontend can truthfully communicate about an interaction at a point in time. Initial, loading, empty, validation failure, denied, unavailable, failed, stale, partial, uncertain, pending, recovery, and confirmed success are presentation meanings, not a repository-wide lifecycle or mandatory transition graph. A later experience applies only the states material to its behavior.

## 4. Frontend Context

FEB is consumed by later storefront, catalogue and discovery, Product evaluation, Identity and customer-account, cart and purchase, order and post-purchase, Administration, and Reporting frontend Specifications. Those Specifications own their journey compositions and must preserve FEB's shared constraints.

The Design System owns governed visual principles, tokens, reusable Components, and their common interaction and accessibility behavior. Approved Domains own business truth. Backend Specifications and Contracts own server orchestration and concrete integration shapes. FEB governs how the frontend represents and interacts with those authorities without replacing them.

## 5. Acceptance Criteria

| Requirement | Acceptance Criterion |
| --- | --- |
| REQ-FEB-001 | Metadata shows `1.0.0 Approved`, `authoritative: false`, and scope `FEB`; review evidence confirms normativity only within the Shared Frontend Baseline scope, no repository-wide authority, governing-source precedence, and preserved Approved Domain authority. |
| REQ-FEB-002 | Every later frontend Specification traces to and preserves its applicable FEB obligations while retaining source-Domain ownership and without duplicating or transferring that authority to FEB. |
| REQ-FEB-003 | Boundary review finds no FEB-owned business state or policy for any named Domain or cross-cutting authority. |
| REQ-FEB-004 | Tests show client-held evidence alone cannot produce an authenticated, authorized, eligible, successful, owned, or commercially current outcome. |
| REQ-FEB-005 | A materially current outcome is shown as confirmed only after governed authoritative evidence is consumed or revalidated. |
| REQ-FEB-006 | State inventory identifies owner and lifetime for each applicable category and demonstrates that client categories do not supersede Server State. |
| REQ-FEB-007 | Stale-capable material data exposes applicable freshness or revalidation behavior and cannot silently appear current after its evidence is no longer sufficient. |
| REQ-FEB-008 | Isolation tests across actors, Sessions, and Resources reveal no residual shared data or action context. |
| REQ-FEB-009 | Applicable back, forward, refresh, and return navigation preserve safe context while route artifacts confer no protected access or truth. |
| REQ-FEB-010 | Navigation tests demonstrate predictable state and focus outcomes without imposing concrete route, label, hierarchy, or redirect policy. |
| REQ-FEB-011 | Authentication displays follow current trusted Identity evidence, and stale or client-held Session information cannot authenticate a Principal. |
| REQ-FEB-012 | Protected reads and mutations are denied without current server-side contextual Authorization even when client guards or controls permit presentation. |
| REQ-FEB-013 | Manipulating control visibility, Role labels, Permissions, Claims, Scope, feature flags, or Resource identifiers does not grant entitlement, authorize an operation, or provide protected Resource access. |
| REQ-FEB-014 | Each experience documents and verifies its materially applicable presentation states and marks inapplicable states without forcing artificial behavior. |
| REQ-FEB-015 | Negative and degraded-state tests demonstrate that materially different outcomes remain identifiable and none is reported as generic success. |
| REQ-FEB-016 | During loading or mutation, the interface communicates pending status, preserves relevant context, and withholds completed-business representation. |
| REQ-FEB-017 | Tests covering stale, cancelled, reordered, and superseded asynchronous work demonstrate that obsolete results cannot overwrite newer relevant user intent or presentation state. |
| REQ-FEB-018 | A confirmed zero result is distinguishable from incomplete, filtered, denied, failed, stale, or still-loading evidence where applicable. |
| REQ-FEB-019 | Field and submission tests show understandable associated feedback while backend rejection can still supersede client validation. |
| REQ-FEB-020 | Representative failures expose only safe outcome and recovery information and do not disclose protected existence, data, Secrets, or internals. |
| REQ-FEB-021 | Recovery tests retain known context, distinguish repeated from new intent, and call governed capabilities without encoding backend recovery mechanics. |
| REQ-FEB-022 | Unknown-effect tests show neither success nor automatic resubmission and expose governed verification or reconciliation when the Contract supports it. |
| REQ-FEB-023 | Rapid repeated interaction is constrained or clearly represented, while server-side duplicate-effect guarantees remain outside frontend claims. |
| REQ-FEB-024 | Permitted optimistic behavior visibly retains material pending status until authoritative evidence confirms or rejects it. |
| REQ-FEB-025 | Rejected, conflicting, failed, and uncertain optimistic outcomes can be corrected and cannot unlock a dependent protected action. |
| REQ-FEB-026 | Data-flow inspection confirms only necessary authorized Sensitive Data appears in views, storage, errors, logs, telemetry, and analytics. |
| REQ-FEB-027 | Security tests with untrusted input and content find no unsafe execution, disclosure, navigation, or injection introduced by rendering. |
| REQ-FEB-028 | Built frontend artifacts and representative routes, state, logs, telemetry, and analytics contain no prohibited Secret or privileged data. |
| REQ-FEB-029 | Defaults, commerce completion, and telemetry choices do not create Consent or Preference; displayed state follows governed Customer evidence. |
| REQ-FEB-030 | Unauthorized and nonexistent protected Resources remain concealed as governed, without exploitable presentation differences. |
| REQ-FEB-031 | Responsive evidence explicitly covers supported viewport and orientation conditions and demonstrates that essential information, meaning, actions, and state remain available without introducing FEB-specific breakpoint or layout values. |
| REQ-FEB-032 | Equivalent completion is demonstrated for each applicable supported input modality, with no pointer-only or keyboard-only dependency. |
| REQ-FEB-033 | Reflow, zoom, spacing, localization, and content-extreme tests preserve essential comprehension and operation. |
| REQ-FEB-034 | Critical journeys have traceable automated and manual WCAG 2.2 AA evidence with no unsupported certification or AAA claim. |
| REQ-FEB-035 | Keyboard tests demonstrate complete operation, logical traversal, and visible unobscured focus for applicable interactions. |
| REQ-FEB-036 | Navigation and dynamic-change tests place or retain focus at a predictable, meaningful location and expose the new context. |
| REQ-FEB-037 | Accessibility inspection confirms native or equivalent semantics, names, relationships, and current state for content and controls. |
| REQ-FEB-038 | Assistive-technology tests perceive material status and error changes, and visual inspection finds no color-, position-, or motion-only meaning. |
| REQ-FEB-039 | Component review confirms governed reusable abstractions are used where applicable without degrading their semantics or supported behavior. |
| REQ-FEB-040 | FEB contains no fixed visual token, breakpoint, anatomy, asset, layout, or journey-composition decision. |
| REQ-FEB-041 | Content-extreme tests preserve source meaning and operation for valid long, short, absent, localized, media, and managed-content cases. |
| REQ-FEB-042 | Performance evidence demonstrates bounded rendering, collection handling, reactive work, retries, background activity, and client-resource use under governed inputs while confirming that FEB introduces no numerical performance budget. |
| REQ-FEB-043 | Optimized and deferred variants pass the same correctness, accessibility, security, privacy, freshness, and degraded-state assertions. |
| REQ-FEB-044 | A degraded expensive activity does not prevent unrelated critical interaction, and the resulting safe state remains truthful. |
| REQ-FEB-045 | Material frontend failures and material interaction outcomes produce diagnosable, correlatable, privacy-safe operational evidence, and that evidence is not accepted as authoritative business truth. |
| REQ-FEB-046 | Evidence classification explicitly distinguishes frontend operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records; verification confirms that none substitutes for another and that ordinary frontend telemetry does not create authoritative Domain history or required Audit Records. |
| REQ-FEB-047 | Analytics inspection confirms non-authoritative, consent-aware, minimized observations and no FEB-selected provider, taxonomy, payload, attribution, or event name. |
| REQ-FEB-048 | Tests for every reachable flag state preserve access, privacy, accessibility, authority, and truthful unavailable behavior. |
| REQ-FEB-049 | Contract tests explicitly expose the outcome, freshness, correlation, and bounded-result semantics required for truthful presentation, while review confirms that FEB defines no routes, methods, operations, DTOs, schemas, wire formats, concrete status mappings, persistence, or event payloads. |
| REQ-FEB-050 | Review finds no selected provider, mechanism, route design, numerical threshold, retry count, timeout, Product policy, or resolution of an Open Product Decision. |
| REQ-FEB-051 | Traceable tests exercise every applicable listed outcome class and demonstrate the shared authority and Contract boundaries under success and adverse conditions. |

## 6. Requirement Traceability

| Requirement | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- |
| REQ-FEB-001 | PRODUCT.md §§21–26 | REQ-BUS-047–048 | — | AGENTS.md §§3, 22–24; DOCUMENTATION-STANDARDS.md §§3–7 | All downstream frontend Specifications |
| REQ-FEB-002 | PRODUCT.md §§25.1, 29–33 | REQ-BUS-047 | — | AGENTS.md §9.2; ARCHITECTURE.md §36 | All downstream frontend Specifications |
| REQ-FEB-003 | PRODUCT.md §§16, 25.1 | REQ-BUS-044, 047–048 | REQ-PRD-001–002; REQ-CAT-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-PAY-002–003; REQ-SHP-002–003; REQ-CHK-002–003; REQ-ORD-002–003; REQ-RET-002–003; REQ-NTF-002–004; REQ-CMS-002–004; REQ-ADM-002–003; REQ-RPT-002–003; REQ-SRCH-002–003 | ENGINEERING-PRINCIPLES.md §§7–10 | All downstream frontend Specifications |
| REQ-FEB-004 | PRODUCT.md §§14.2–14.6, 16 | REQ-BUS-012, 015, 017, 024, 032 | REQ-IDN-020, 026–028; REQ-ADM-007–008 | ANGULAR.md §§20–22, 29–31 | All customer and Staff User experiences |
| REQ-FEB-005 | PRODUCT.md §§14.4–14.6, 16.1–16.4 | REQ-BUS-012, 015, 017, 024–025 | REQ-CHK-021–023 | ANGULAR.md §22; ARCHITECTURE.md §§36.5–36.6, 39.3; API.md §§23–24, 39–45 | All frontend experiences requiring current authoritative truth |
| REQ-FEB-006 | — | — | — | ANGULAR.md §§20–23; GLOSSARY.md §28 | All downstream frontend Specifications |
| REQ-FEB-007 | PRODUCT.md §§14.1–14.5, 30–32 | REQ-BUS-042, 044 | REQ-RPT-017–018; REQ-SRCH-025, 033 | ANGULAR.md §22; API.md §§23, 35 | All stale-capable frontend experiences |
| REQ-FEB-008 | PRODUCT.md §§7, 16.7, 20 | REQ-BUS-032, 039 | REQ-CUS-005, 039–040; REQ-IDN-006; REQ-ADM-037 | SECURITY-STANDARDS.md §§12, 18, 35; ANGULAR.md §§43–44 | All frontend experiences using shared state |
| REQ-FEB-009 | PRODUCT.md §§14, 29–32 | — | — | ANGULAR.md §§28–29; ACCESSIBILITY.md §§9, 11 | All routable frontend experiences |
| REQ-FEB-010 | PRODUCT.md §§29–32 | REQ-BUS-002, 037 | — | UI.md §12; ACCESSIBILITY.md §§9, 11, 48 | All routable frontend experiences |
| REQ-FEB-011 | PRODUCT.md §§7, 16.7 | REQ-BUS-032, 051 | REQ-IDN-009–10, 018–20 | ANGULAR.md §30; SECURITY-STANDARDS.md §§10–13 | All frontend experiences presenting Authentication or Session context |
| REQ-FEB-012 | PRODUCT.md §§7.3–7.8, 16.7, 23 | REQ-BUS-032–033 | REQ-IDN-024–028; REQ-ADM-007 | ANGULAR.md §§29, 31; API.md §§25–26, 52–53; SECURITY-STANDARDS.md §12 | All protected frontend experiences |
| REQ-FEB-013 | PRODUCT.md §§7.3–7.8, 23 | REQ-BUS-032–033 | REQ-IDN-020, 026–028; REQ-ADM-008 | ANGULAR.md §§29, 31, 62; SECURITY-STANDARDS.md §12 | All protected frontend experiences |
| REQ-FEB-014 | PRODUCT.md §§5.4–5.6, 17.2, 30 | REQ-BUS-042 | — | ANGULAR.md §37; ACCESSIBILITY.md §§24–26 | All downstream frontend Specifications |
| REQ-FEB-015 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-025, 042, 046 | REQ-PAY-021; REQ-ADM-020; REQ-RPT-017–018 | ANGULAR.md §37; ACCESSIBILITY.md §26; API.md §§52–58 | All stateful frontend experiences |
| REQ-FEB-016 | PRODUCT.md §§14.3–14.6, 30–32 | REQ-BUS-042 | — | ANGULAR.md §§27, 37; ACCESSIBILITY.md §§24–25 | All asynchronous frontend experiences |
| REQ-FEB-017 | — | REQ-BUS-042 | — | ANGULAR.md §§18, 27, 37 | All asynchronous frontend experiences |
| REQ-FEB-018 | PRODUCT.md §§14.1, 30 | REQ-BUS-004, 042 | REQ-SRCH-023–025 | ACCESSIBILITY.md §26; UI.md §§22–23 | All frontend experiences with potentially empty data or results |
| REQ-FEB-019 | PRODUCT.md §§14.3–14.4, 30–32 | REQ-BUS-011, 042 | — | ANGULAR.md §§24–27; ACCESSIBILITY.md §§12–16 | All form-based frontend experiences |
| REQ-FEB-020 | PRODUCT.md §§5.4–5.6, 16.7, 17.2 | REQ-BUS-039, 042 | REQ-IDN-039, 043–044; REQ-ADM-020, 037–038 | SECURITY-STANDARDS.md §§18, 35; API.md §§52–60 | All downstream frontend Specifications |
| REQ-FEB-021 | PRODUCT.md §§5.6, 14.8, 28.6 | REQ-BUS-036, 042, 045 | REQ-ADM-021–022 | ANGULAR.md §§36–38; API.md §§22, 51 | All recoverable frontend experiences |
| REQ-FEB-022 | PRODUCT.md §§14.5–14.8, 17.2 | REQ-BUS-025, 035, 042, 045 | REQ-PAY-021, 023–025; REQ-ADM-020–022 | ANGULAR.md §§36–38; API.md §§22, 36–37, 48, 58 | All frontend mutation experiences with potentially unknown effects |
| REQ-FEB-023 | PRODUCT.md §§14.3–14.5 | REQ-BUS-010, 013, 026 | REQ-CART-009–010, 022–023; REQ-CHK-023–025 | ANGULAR.md §§27, 36–38; API.md §22 | All frontend experiences submitting state-changing intent |
| REQ-FEB-024 | PRODUCT.md §§14.3–14.5 | REQ-BUS-010, 012–013, 024–025 | — | ANGULAR.md §§22, 37; API.md §§22–24 | Stateful customer and Staff User experiences |
| REQ-FEB-025 | PRODUCT.md §§14.3–14.5, 17.2 | REQ-BUS-013, 025, 042 | — | ENGINEERING-PRINCIPLES.md §§14, 18, 20; ANGULAR.md §§37–38 | Stateful customer and Staff User experiences |
| REQ-FEB-026 | PRODUCT.md §§16.7, 20, 24 | REQ-BUS-039–040 | REQ-CUS-040–041; REQ-IDN-043–044; REQ-ADM-037–038 | SECURITY-STANDARDS.md §§18, 27, 35; ANGULAR.md §43 | All downstream frontend Specifications |
| REQ-FEB-027 | PRODUCT.md §§16.7, 20 | REQ-BUS-039 | — | SECURITY-STANDARDS.md §§18, 33, 35; ANGULAR.md §§45–47 | All content and input surfaces |
| REQ-FEB-028 | PRODUCT.md §§16.3, 16.7, 20 | REQ-BUS-027, 039 | REQ-PAY-031–032; REQ-ADM-038 | SECURITY-STANDARDS.md §§14, 18, 36; ANGULAR.md §§43–46 | All downstream frontend Specifications |
| REQ-FEB-029 | PRODUCT.md §§7.2, 16.7, 24 | REQ-BUS-040 | REQ-CUS-018–021; REQ-NTF-013–015 | SECURITY-STANDARDS.md §35; ACCESSIBILITY.md §§60–61 | Account, purchase, content, analytics experiences |
| REQ-FEB-030 | PRODUCT.md §§7, 16.7, 20 | REQ-BUS-032, 039, 052 | REQ-IDN-006, 011; REQ-CUS-039–042 | SECURITY-STANDARDS.md §§12, 18, 33; API.md §§52–53 | All frontend experiences accessing protected or existence-sensitive Resources |
| REQ-FEB-031 | PRODUCT.md §§5.2, 10, 30 | REQ-BUS-002, 037 | — | DESIGN-SYSTEM.md §§12–14; ANGULAR.md §50; ACCESSIBILITY.md §§36, 38, 55, 72 | All downstream frontend Specifications |
| REQ-FEB-032 | PRODUCT.md §§5.7, 19 | REQ-BUS-037 | — | ACCESSIBILITY.md §§8, 41–42, 54–55 | All interactive frontend experiences |
| REQ-FEB-033 | PRODUCT.md §§5.2, 5.7, 19 | REQ-BUS-002, 037 | — | ACCESSIBILITY.md §§35–38, 46, 55, 73 | All downstream frontend Specifications |
| REQ-FEB-034 | PRODUCT.md §§5.7, 8.6, 16.9, 19 | REQ-BUS-037 | REQ-CUS-043; REQ-IDN-047; REQ-ADM-042; REQ-RPT-045; REQ-SRCH-040 | ACCESSIBILITY.md §§2, 64–76; TESTING-STANDARDS.md §§7, 21–22, 38–39 | Critical customer and Staff User journeys |
| REQ-FEB-035 | PRODUCT.md §§5.7, 19 | REQ-BUS-037 | — | ACCESSIBILITY.md §§8–10, 53 | All interactive frontend experiences |
| REQ-FEB-036 | PRODUCT.md §§5.7, 19, 29–32 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md §§9, 11, 18, 24–26 | Routable, form, overlay, and asynchronous experiences |
| REQ-FEB-037 | PRODUCT.md §§5.7, 19 | REQ-BUS-037 | — | ACCESSIBILITY.md §§4–7, 17–23, 56 | All downstream frontend Specifications |
| REQ-FEB-038 | PRODUCT.md §§5.4–5.7, 17.2, 19 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md §§13, 24–26, 32–34 | All stateful frontend experiences |
| REQ-FEB-039 | PRODUCT.md §§8.6, 19, 30–32 | REQ-BUS-002, 037 | — | DESIGN-SYSTEM.md §§1–3, 17, 60–61, 75; ANGULAR.md §49; ACCESSIBILITY.md §§56, 63 | All downstream frontend Specifications |
| REQ-FEB-040 | PRODUCT.md §8.6; Open Product Decision 1; PRODUCT.md §§29–32 | REQ-BUS-048 | — | DESIGN-SYSTEM.md §§3, 6–18, 59, 88–89, 95–98; ANGULAR.md §§49–50, 65 | Design System and downstream frontend Specifications |
| REQ-FEB-041 | PRODUCT.md §§8.6, 14.2, 19, 30 | REQ-BUS-002, 037, 050 | REQ-CMS-008, 021, 030, 034 | UI.md §§10, 42, 58, 62; ACCESSIBILITY.md §§46, 55, 73 | Storefront shell, catalogue, Product evaluation, content experiences |
| REQ-FEB-042 | PRODUCT.md §§8.3, 18.4–18.6, 35 | REQ-BUS-038 | REQ-ADM-043; REQ-RPT-043–044; REQ-SRCH-041–042 | PERFORMANCE.md §§2, 4–25; ANGULAR.md §§51–54; API.md §32 | All downstream frontend Specifications |
| REQ-FEB-043 | PRODUCT.md §§8.3, 18.4–18.6, 19 | REQ-BUS-037–039, 042 | — | PERFORMANCE.md §§2, 24–25; ACCESSIBILITY.md §62; ANGULAR.md §§51–54 | All performance-sensitive experiences |
| REQ-FEB-044 | PRODUCT.md §§5.6, 18.4–18.6, 35 | REQ-BUS-038, 042, 045 | — | PERFORMANCE.md §§17–25; ENGINEERING-PRINCIPLES.md §§14, 20, 25–26 | All downstream frontend Specifications |
| REQ-FEB-045 | PRODUCT.md §§18.3, 34, 35.4 | REQ-BUS-035, 043 | REQ-ADM-044; REQ-RPT-046; REQ-SRCH-043 | ANGULAR.md §61; API.md §§36–37, 77; SECURITY-STANDARDS.md §§27–28 | Engineering, operations, support |
| REQ-FEB-046 | PRODUCT.md §§18.3, 33–34 | REQ-BUS-034–035, 043–044 | REQ-RPT-015–016, 047 | GLOSSARY.md §§26, 38, 42; EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §27 | Engineering, operations, Reporting, Analytics, Domain owners |
| REQ-FEB-047 | PRODUCT.md §§24, 33; Open Product Decisions 19–20 | REQ-BUS-039–040, 044, 048 | REQ-CUS-020–021, 038; REQ-RPT-016, 037, 050 | ANGULAR.md §61; ACCESSIBILITY.md §61; SECURITY-STANDARDS.md §35 | Analytics and all measured frontend experiences |
| REQ-FEB-048 | PRODUCT.md §§21, 25, 36–38; Open Product Decision 30 | REQ-BUS-032, 037, 039–040, 048 | REQ-IDN-028; REQ-ADM-048 | ANGULAR.md §§62–63, 65; SECURITY-STANDARDS.md §17 | All progressively delivered frontend experiences |
| REQ-FEB-049 | PRODUCT.md §§21–23, 26 | REQ-BUS-035, 042, 046–047 | REQ-IDN-045; REQ-ADM-046; REQ-RPT-048; REQ-SRCH-045 | AGENTS.md §§5, 24; ARCHITECTURE.md §§13, 36.5, 39; API.md §§9–13, 17, 22–24, 36–37, 63–76 | All downstream frontend Specifications and Contract owners |
| REQ-FEB-050 | PRODUCT.md §§21, 24–26, 36–38 | REQ-BUS-038, 046, 048 | REQ-ADM-051–052; REQ-RPT-050–052; REQ-SRCH-048–049 | AGENTS.md §§5, 23–24; DECISIONS.md §§25–40; ENGINEERING-PRINCIPLES.md §§11, 24 | All downstream frontend Specifications and implementation teams |
| REQ-FEB-051 | PRODUCT.md §§22, 26, 35 | REQ-BUS-037–039, 042, 047 | REQ-IDN-050; REQ-ADM-053; REQ-RPT-053; REQ-SRCH-050 | TESTING-STANDARDS.md §§5, 7, 9, 11, 13, 19–24, 27–31, 33–40; ANGULAR.md §§56–60, 68 | Engineering, QA, Accessibility, Security, all downstream frontend Specifications |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following four are materially relevant to FEB, retain their exact source wording and order, and remain unresolved by this Draft:

| Source Order | Open Product Decision | FEB Boundary |
| --- | --- | --- |
| 1 | Final brand name and visual identity. | Shared frontend behavior must remain compatible with a governed identity, but FEB selects no name, asset, theme, or visual value and does not resolve this Decision. |
| 19 | Marketing-consent and communication-preference model. | FEB preserves Consent and Preference non-inference across presentation and telemetry but selects no model or default and does not resolve this Decision. |
| 20 | Initial analytics provider and event taxonomy. | FEB separates Analytics Events from authoritative evidence but selects no provider, taxonomy, canonical name, or payload and does not resolve this Decision. |
| 30 | Product launch date, release scope, and post-launch support window. | FEB requires safe reachable delivery states but selects no date, scope, rollout, support window, or numerical target and does not resolve this Decision. |

## 8. Risks and Controls

| Risk | Control |
| --- | --- |
| Client state becomes authoritative business truth. | Require governed owning-Domain confirmation before presenting material truth as confirmed. |
| Cached data appears current after it becomes stale. | Preserve applicable freshness context and trigger governed revalidation when current evidence is required. |
| Stale, cancelled, reordered, or superseded asynchronous work overwrites newer user intent or presentation state. | Ensure obsolete asynchronous results are ignored or prevented from replacing newer relevant intent or state, preserving the latest governed interaction context. |
| Optimistic state appears authoritatively confirmed. | Keep material optimistic state distinguishable until confirmation and support correction on rejection or uncertainty. |
| Repeated interaction produces duplicate intent. | Constrain or clearly represent in-flight submissions while relying on governed server duplicate-effect protection. |
| An unknown-effect outcome is shown as success. | Preserve uncertainty and expose governed verification or reconciliation instead of automatic resubmission. |
| UI visibility is mistaken for Authorization. | Require current server-side contextual Authorization for every protected read and mutation. |
| Resource identifier possession is mistaken for access. | Apply governed concealment and verify object-level access independently of client identifiers. |
| Sensitive Data leaks through browser surfaces. | Minimize authorized data across views, retention, routes, errors, logs, telemetry, and analytics. |
| Consent is inferred from defaults or commerce activity. | Consume governed Customer Consent and Preference evidence without synthesizing it locally. |
| Asynchronous state is inaccessible. | Provide perceivable status semantics and test applicable updates with assistive technology. |
| Focus is lost after navigation or dynamic change. | Define and verify predictable focus retention or movement for each material context change. |
| Keyboard users cannot complete an interaction. | Verify complete logical keyboard operation and visible focus for every applicable control. |
| Assistive technology receives incomplete semantics. | Prefer native semantics and verify names, relationships, state, and live updates. |
| Responsive presentation removes essential information or action. | Test supported viewport, zoom, reflow, orientation, and content-extreme conditions for functional equivalence. |
| Feature code bypasses the Design System. | Require governed shared abstractions where applicable and preserve their semantics and accessibility. |
| Rendering or repeated client work is unbounded. | Bound collections, reactive work, retries, and background activity using governed evidence rather than arbitrary FEB numbers. |
| Shared state contaminates another actor or Resource context. | Partition and clear shared state at governed identity, Session, and Resource boundaries. |
| Error presentation exposes protected details. | Limit errors to safe outcome and recovery information and preserve Resource concealment. |
| Analytics Events become business truth. | Classify analytics as non-authoritative observations and prohibit their use as source-Domain confirmation. |
| Telemetry leaks Sensitive Data. | Minimize and review telemetry fields independently from user-visible data needs. |
| A feature-flag state weakens security or accessibility. | Verify every reachable state against the same Authorization, privacy, accessibility, and authority invariants. |
| Contract evolution causes silent misrepresentation. | Verify explicit outcome and compatibility semantics and fail safely when evidence cannot be interpreted truthfully. |
| A downstream frontend Specification redefines shared semantics. | Require explicit FEB traceability and conformance review for every later experience Specification. |
| Performance optimization changes correctness. | Run equivalent correctness, accessibility, security, privacy, and degraded-state tests on optimized behavior. |
| An implementation choice becomes Product policy. | Keep providers, mechanisms, numerical values, routes, taxonomy, and unresolved Decisions outside FEB Requirements. |

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
- `.ai/backend/DATABASE.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/search/search-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-07 | Draft | Initial comprehensive Shared Frontend Baseline Specification. |
| 1.0.0 | 2026-09-07 | Approved | Approved Shared Frontend Baseline Specification following comprehensive validation. |

## 11. Final Validation

Before the Approved baseline is committed, verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, and scope is `FEB`, with normativity only within the Shared Frontend Baseline scope and no repository-wide authority;
2. governing-source precedence and all Approved Domain authority remain preserved;
3. frontend and cached state remain non-authoritative representations;
4. Authentication presentation and server-side contextual Authorization remain distinct;
5. applicable shared presentation states are truthful, distinguishable, and do not form a mandatory lifecycle graph;
6. validation, failure, optimistic interaction, retry, recovery, and unknown-effect boundaries are explicit;
7. Sensitive Data, Consent, isolation, rendering, disclosure, and enumeration safeguards are preserved;
8. WCAG 2.2 AA, keyboard, focus, assistive-technology, status, responsive, input-modality, and content-resilience evidence is applicable and testable;
9. Design System authority is preserved and no visual token, breakpoint, anatomy, asset, layout, or journey composition is selected;
10. rendering and client work are bounded without invented budgets and optimization preserves correctness;
11. telemetry, correlation, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinguishable;
12. feature-flag states preserve security, privacy, accessibility, and Domain authority without selecting a provider or rollout policy;
13. Contracts provide abstract truthful-outcome semantics without FEB defining routes, methods, operations, DTOs, schemas, wire formats, providers, persistence, or event payloads;
14. Requirements, Acceptance Criteria, and traceability are equal in count, unique, sequential, and one-to-one;
15. every cited Business or Approved Domain Requirement physically exists and directly supports its FEB Requirement;
16. all four represented Product Decisions exactly match `PRODUCT.md`, remain source-ordered, and are unresolved;
17. Risks have distinct, implementation-neutral controls and all Related Document paths exist;
18. Revision History contains exactly the preserved `0.1.0 Draft` row and one `1.0.0 Approved` row;
19. no Glossary amendment is required and FEB-scoped descriptions do not become repository-wide terminology;
20. no Product policy, provider, protocol, API or schema, persistence model, event taxonomy, numerical target, retry count, timeout, route design, lifecycle graph, or implementation mechanism is introduced;
21. Markdown headings and tables, UTF-8, trailing whitespace, final newline, and prohibited-marker checks pass; and
22. Git scope contains only the authorized lifecycle-promotion changes to `specifications/frontend/shared/frontend-baseline.md`, with nothing staged, untracked, unrelated, or otherwise modified.
