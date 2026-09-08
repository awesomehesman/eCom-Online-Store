---
title: Customer and Account / Identity Specification
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-08
authoritative: false
---

# Customer and Account / Identity Specification

## 1. Purpose

This Specification defines implementation-neutral requirements for customer-facing registration, Authentication, Session, recovery, Customer, Account, profile, Address, Preference, Consent, and related protected-resource presentation.

This document uses scope code `FCA`. This Specification is Approved, and its Requirements are normative only within Customer and Account / Identity frontend scope and are not repository-wide authority. It remains subordinate to governing sources, Product and Business Requirements, Approved Domain Specifications, and applicable Approved FEB, FSC, FCD, and FPE Requirements, and resolves no Open Product Decision.

## 2. Scope and Authority

FCA owns only customer-facing presentation behavior for Visitor and authenticated Customer context, registration, sign-in, sign-out, Session status, recovery and verification, account navigation, profile, Address, Preference, Consent, conditional Wishlist, governed historical-reference summaries and entry points, and FCA-specific state, accessibility, security, degradation, telemetry, analytics boundaries, feature flags, and abstract Contract expectations.

FCA does not own Customer, Account, Identity, Principal, credential, Authentication, Session, Authorization, Role, Permission, Claims, Scope, Consent, communication-preference, Cart, Checkout, Payment, Order, Shipping, Return, Refund, or other Domain truth. Presentation, possession of identifiers, route state, cached state, control visibility, labels, or client assertions cannot transfer that authority to FCA.

Shared frontend behavior remains owned by FEB; shell and shared navigation by FSC; catalogue discovery by FCD; Product evaluation by FPE; Cart and purchase by FCP; post-purchase by FPP; Administration by FAD; and Reporting by FRP.

## 3. Terminology and Customer / Account / Identity Model

Canonical terms retain their meanings from `GLOSSARY.md`. Visitor, Customer, Account, Identity, Principal, Authentication, Session, Authorization, Role, Permission, Claims, Scope, and Consent remain distinct. FCA MUST NOT collapse them into a generic user concept where that would change governed meaning.

For FCA only, **account context** describes non-authoritative presentation state associated with permitted Customer-facing Account capabilities. It is not a canonical entity, credential, Principal, Session, Authorization decision, or Customer lifecycle state.

## 4. Requirements

### REQ-FCA-001 — Lifecycle, Authority, and Scope

FCA MUST govern only Customer and Account / Identity frontend behavior under scope `FCA`, preserve governing-source precedence, applicable frontend inheritance and Approved Domain authority, and MUST treat this Approved Specification as normative only within Customer and Account / Identity frontend scope and not as repository-wide authority.

### REQ-FCA-002 — Frontend Inheritance

FCA MUST consume every materially applicable Approved FEB, FSC, FCD, and FPE Requirement without copying, weakening, conflicting with, or transferring ownership of inherited behavior.

### REQ-FCA-003 — Domain and Journey Non-Authority

FCA MUST NOT own or redefine source-Domain truth or FSC, FCD, FPE, FCP, FPP, FAD, or FRP behavior; presentation, entry, summary, or handoff MUST NOT transfer that authority.

### REQ-FCA-004 — Canonical Actor and Security Distinctions

FCA MUST preserve the distinctions among Visitor, Customer, Account, Identity, Principal, Authentication, Session, Authorization, Role, Permission, Claims, Scope, and Consent without asserting unsupported identity, cardinality, or authority relationships.

### REQ-FCA-005 — Visitor and Authenticated Context

FCA MUST present Visitor and authenticated Customer context truthfully from governed evidence and MUST NOT infer a Customer, Account, Principal, Authentication, Session, or entitlement from local state or activity.

### REQ-FCA-006 — Registration and Account Establishment

Where Approved policy supports registration, FCA MUST present governed Customer and Identity establishment intent and outcomes while Customer retains Customer and Account creation truth and Identity retains credential and Authentication truth.

### REQ-FCA-007 — Registration Failure and Ambiguity

Duplicate, ambiguous, invalid, conflicting, denied, unavailable, failed, or uncertain registration outcomes MUST remain distinguishable and safely recoverable where permitted without disclosing protected Account existence or Sensitive Data.

### REQ-FCA-008 — Sign-In Presentation

FCA MUST present Authentication intent and accepted Identity-owned outcomes without treating submission, redirect, delivery, challenge display, or client assertion as successful Authentication or Principal establishment.

### REQ-FCA-009 — Authentication Outcomes and Abuse Boundary

Authentication failure, denial, unavailability, stale or conflicting evidence, and uncertainty MUST be safely distinguishable while presentation resists enumeration and exposes no security-control detail or FCA-defined abuse policy.

### REQ-FCA-010 — Sign-Out Presentation

FCA MUST distinguish sign-out intent, pending outcome, accepted completion, failure, and uncertainty and MUST NOT represent local clearance, navigation, timeout, or stopped observation as proof of server-side Session termination.

### REQ-FCA-011 — Session Presentation

FCA MUST present governed Session identity, validity, expiry, renewal, revocation, termination, compromise, and uncertainty only where applicable, without defining Session lifecycle, duration, storage, token, cookie, or protocol behavior.

### REQ-FCA-012 — Stale Session and Privilege Safety

Stale, reordered, superseded, revoked, or conflicting Session or access evidence MUST NOT restore access, extend privilege, overwrite newer security context, or be presented as current.

### REQ-FCA-013 — Recovery Entry and Verification

FCA MUST present governed recovery initiation and requester-verification interaction without selecting verification channel, factor, expiry, provider, support process, credential mechanism, or recovery policy.

### REQ-FCA-014 — Recovery Outcome Integrity

Recovery request, pending verification, rejection, expiry, success, failure, duplicate, timeout, and uncertainty MUST remain distinguishable, and UI or Notification evidence MUST NOT prove credential change, Session invalidation, or recovery success.

### REQ-FCA-015 — Conditional MFA, SSO, and Verification

FCA MAY present MFA, SSO, email verification, or another verification capability only when Approved governance establishes it and MUST NOT mandate support or select provider, protocol, factor, method, threshold, timing, or policy.

### REQ-FCA-016 — Credential Non-Authority

FCA MUST treat credentials as Identity-owned protected material and MUST NOT establish credential truth, expose stored credential values, define password or token rules, or treat possession of a credential-like value as Authorization.

### REQ-FCA-017 — Recovery Artifact Safety

Recovery, verification, invitation, or reset artifacts MUST be minimized, safely presented, isolated, and treated as untrusted references until accepted by governed Identity behavior; they MUST NOT appear in unsafe content, telemetry, analytics, logs, errors, or unrelated state.

### REQ-FCA-018 — Account Navigation and Context

FCA MUST provide predictable entry, location, and return context for supported account capabilities without prescribing routes, URLs, labels, layouts, Components, or navigation policy or treating navigation state as authority.

### REQ-FCA-019 — Protected Destination Outcomes

Unavailable, invalid, expired, inaccessible, or unsupported account destinations MUST produce truthful safe outcomes and permitted recovery without revealing protected Resource or Account existence.

### REQ-FCA-020 — Profile Presentation

FCA MUST present Customer-owned profile evidence with governed identity, purpose, applicability, freshness, and ownership while preserving Identity, Authorization, and downstream Domain authority.

### REQ-FCA-021 — Profile Mutation Intent

Profile mutation presentation MUST submit only governed Customer intent, preserve field validation, ownership, privacy, and accepted state, and MUST NOT treat client validation, pending state, or optimistic state as an accepted Customer update.

### REQ-FCA-022 — Address Presentation

FCA MUST present governed Customer Address identity, ownership, applicability, validation context, and freshness without redefining address policy or rewriting Checkout, Order, Shipment, invoice, or other historical snapshots.

### REQ-FCA-023 — Address Mutation Intent

Address creation, update, selection, or removal presentation MUST preserve Customer ownership, validation, concurrency, reference constraints, safe failure, and downstream authority without claiming acceptance before governed Customer evidence.

### REQ-FCA-024 — Preference Presentation

FCA MUST present and submit only governed Customer Preference evidence or intent, preserve ownership and purpose, and MUST NOT treat Preference as Consent, Authorization, transactional necessity, or communication policy.

### REQ-FCA-025 — Consent Presentation

Where governed Consent capability exists, FCA MUST preserve purpose, category, current evidence, change or withdrawal intent, and outcome while never inferring Consent from Account existence, Authentication, Session, Preference, purchase, inactivity, or unrelated interaction.

### REQ-FCA-026 — Conditional Wishlist Boundary

FCA MAY present Customer-owned Wishlist capability only where Approved Product policy enables it and MUST NOT resolve guest behavior or allow Wishlist state to establish Product publication, visibility, Price, availability, Stock Reservation, Cart intent, Authorization, or purchasability.

### REQ-FCA-027 — Historical Reference Summaries

FCA MAY present authorized Customer historical-reference summaries for Order, Payment, Refund, Shipment, Return, or communication evidence only from governed owning-Domain sources and MUST NOT create, rewrite, or reinterpret their truth or lifecycle.

### REQ-FCA-028 — Downstream Journey Handoff

FCA MAY provide safe entry or context to FCP or FPP but MUST NOT perform Cart, Checkout, Payment, Order, Shipping, Return, Refund, fulfilment, or post-purchase behavior or claim their outcomes.

### REQ-FCA-029 — Contextual Authorization

Every protected read, mutation, summary, and handoff MUST preserve current server-side contextual Authorization for Principal, Customer ownership, Resource, action, association, property, and Domain state; frontend state MUST NOT authorize access.

### REQ-FCA-030 — Labels and Identifiers Non-Authority

Role labels, Permissions, Claims, Scope, control visibility, route state, identifiers, client assertions, or Authentication alone MUST NOT grant entitlement, authorize an operation, or disclose inaccessible Resource existence.

### REQ-FCA-031 — Context and Resource Isolation

FCA state MUST remain isolated across applicable Visitor, Identity, Principal, Session, Customer, Account, and Resource contexts and MUST be cleared, segregated, or revalidated when the governing context changes.

### REQ-FCA-032 — Sensitive Data and Secrets

FCA MUST minimize Sensitive Data across content, client state, routes, errors, logs, telemetry, Analytics Events, exports, fixtures, and screenshots and MUST expose no Secrets, credentials, recovery artifacts, raw Payment data, or unnecessary personal information.

### REQ-FCA-033 — Safe Rendering and Disclosure

FCA MUST render governed and query-derived content safely and resist injection, deception, unsafe navigation, enumeration, timing disclosure, and exposure of inaccessible Account, Customer, Identity, or Resource state.

### REQ-FCA-034 — Freshness and Authoritative Revalidation

Where current truth is material, FCA MUST expose or consume sufficient source, applicability, freshness, and revalidation context and MUST NOT present stale Customer, Identity, Session, Authorization, Consent, Preference, or downstream evidence as current confirmation.

### REQ-FCA-035 — Asynchronous Supersession

Stale, cancelled, reordered, conflicting, or superseded registration, Authentication, Session, recovery, profile, Address, Preference, Consent, Wishlist, or history outcomes MUST NOT overwrite newer relevant intent or presentation state.

### REQ-FCA-036 — Concurrent Mutation Safety

Concurrent or repeated profile, Address, Preference, Consent, recovery, or account-related intent MUST preserve accepted state, expose conflict or uncertainty, and avoid silent overwrite or duplicate effect without defining backend concurrency or idempotency mechanisms.

### REQ-FCA-037 — Optimistic Presentation Boundary

Where optimistic profile or account presentation is used, it MUST remain explicitly provisional, correctable, and distinguishable from accepted Customer or Identity truth and MUST revert or reconcile safely after rejection, conflict, failure, or uncertainty.

### REQ-FCA-038 — Account Presentation States

Each material FCA region MUST apply distinguishable initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states where applicable without defining a business lifecycle or mandatory universal traversal.

### REQ-FCA-039 — Truthful Failure and Recovery

FCA MUST preserve failure provenance, known context, safe next actions, and permitted recovery without fabricating success, Account existence, authorization, provider effects, or backend retry, replay, reconciliation, or recovery mechanics.

### REQ-FCA-040 — Responsive and Content-Resilient Outcomes

Account information and actions MUST remain understandable and operable across supported viewport, orientation, reflow, zoom, localization, and content extremes without FCA defining breakpoints, layouts, or visual tokens.

### REQ-FCA-041 — Input-Modality Equivalence

Registration, Authentication, recovery, account navigation, profile, Address, Preference, Consent, Wishlist, history, and handoff interactions MUST remain operable through supported keyboard, pointer, touch, and assistive-technology paths.

### REQ-FCA-042 — Focus and Dynamic Status

Authentication, Session, validation, mutation, failure, recovery, and context changes MUST preserve or move focus predictably and expose understandable structure, state, errors, status, and change without relying only on visual presentation.

### REQ-FCA-043 — Bounded Frontend Work

Profile, Address, Preference, Consent, Wishlist, history, security-context, reactive, and background work MUST remain bounded by governed inputs and performance evidence without FCA defining numerical limits, payload sizes, timing budgets, or client mechanisms.

### REQ-FCA-044 — Failure Containment

Failure or degradation of optional content, communication, history, analytics, or another non-essential account region MUST NOT silently block unrelated critical Authentication, recovery, or account access and MUST remain truthful about completeness and uncertainty.

### REQ-FCA-045 — Telemetry and Evidence Separation

Material authentication, Session, recovery, protected-access, mutation, and Contract failures MUST produce safe frontend operational telemetry and governed correlation evidence while telemetry, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinct and non-authoritative.

### REQ-FCA-046 — Analytics Boundary

FCA Analytics Events MUST remain minimized, Consent-aware where applicable, and non-authoritative and MUST NOT define provider, taxonomy, event name, payload, identity model, audience, personalization, attribution, or Customer truth.

### REQ-FCA-047 — Feature-Flag Safety

Every reachable feature-flag state affecting FCA MUST preserve valid evidence, truthful outcomes, accessibility, security, privacy, isolation, inherited obligations, and Domain authority without selecting rollout policy or implementation.

### REQ-FCA-048 — Abstract Contract Boundary

Future FCA Contracts MUST expose only necessary bounded and versioned identity, context, ownership, source, applicability, freshness, outcome, failure, recovery, and correlation semantics and MUST NOT define routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, transport, or providers.

### REQ-FCA-049 — Product-Policy and Implementation Neutrality

FCA MUST keep unresolved account, verification, Wishlist, support, Consent, analytics, fraud, closure, export, launch, provider, protocol, route, visual, caching, numerical, and implementation choices outside normative behavior.

### REQ-FCA-050 — Verification Coverage

FCA verification MUST cover authority, inheritance, canonical distinctions, registration, Authentication, Sessions, recovery, verification, account navigation, profile, Address, Preference, Consent, conditional Wishlist, history, handoff, Authorization, isolation, Sensitive Data, freshness, supersession, concurrency, states, recovery, accessibility, boundedness, degradation, telemetry, analytics, feature flags, Contracts, and policy neutrality.

## 5. Acceptance Criteria

| Requirement | Acceptance Criterion |
| --- | --- |
| REQ-FCA-001 | Lifecycle evidence confirms `1.0.0 Approved`, `authoritative: false`, scope `FCA`, normativity only within Customer and Account / Identity frontend scope, no repository-wide authority, governing precedence, inherited frontend obligations, and preserved Product, Business Requirement, and Approved Domain authority. |
| REQ-FCA-002 | Every materially applicable inherited obligation is mapped to FCA and none is copied, weakened, contradicted, or ownership-transferred. |
| REQ-FCA-003 | Boundary review finds no FCA-owned source-Domain truth or detailed FCP, FPP, FAD, FRP, FSC, FCD, or FPE behavior and no authority transfer through presentation or handoff. |
| REQ-FCA-004 | Tests distinguish every named actor, business, security, Session, access, and Consent concept without unsupported equivalence or cardinality. |
| REQ-FCA-005 | Visitor and authenticated contexts reflect governed evidence, and local activity or state establishes none of the prohibited Customer or security truths. |
| REQ-FCA-006 | Supported registration presents correlated Customer and Identity outcomes while creation and credential authority remain with their owning Domains. |
| REQ-FCA-007 | Each listed registration outcome is independently produced with safe recovery where allowed and without Account enumeration or Sensitive Data disclosure. |
| REQ-FCA-008 | Sign-in evidence distinguishes intent and accepted outcome, and no submission, redirect, challenge, delivery, or client assertion proves Authentication. |
| REQ-FCA-009 | Authentication negative outcomes remain distinguishable and non-enumerating without exposing controls or selecting abuse policy. |
| REQ-FCA-010 | Sign-out intent, progress, completion, failure, and uncertainty are distinct, and local clearance or navigation never proves Session termination. |
| REQ-FCA-011 | Tests independently present every applicable Session identity, validity, expiry, renewal, revocation, termination, compromise, and uncertainty condition from Identity-owned evidence and confirm FCA selects no Session lifecycle, duration, storage, token, cookie, or protocol behavior. |
| REQ-FCA-012 | Reordered or stale Session and privilege evidence cannot restore access, extend privilege, or replace newer accepted context. |
| REQ-FCA-013 | Recovery entry requires governed verification presentation and selects no channel, factor, expiry, provider, support, credential, or recovery policy. |
| REQ-FCA-014 | Every recovery outcome is independently observable, and Notification or UI evidence cannot prove credential or Session effects. |
| REQ-FCA-015 | MFA, SSO, and verification appear only when governed, with no FCA-selected support, provider, protocol, factor, threshold, timing, or policy. |
| REQ-FCA-016 | Credential tests find no FCA-owned credential truth, stored value exposure, locally defined rule, or credential-based Authorization inference. |
| REQ-FCA-017 | Recovery artifacts remain minimized, isolated, safely rendered, and absent from prohibited evidence surfaces until governed acceptance. |
| REQ-FCA-018 | Supported account navigation preserves predictable context without prescribed route or presentation design and never becomes authority. |
| REQ-FCA-019 | Invalid and protected destinations produce safe truthful outcomes and permitted recovery without Resource or Account-existence disclosure. |
| REQ-FCA-020 | Profile evidence retains governed identity, purpose, ownership, applicability, and freshness, and boundary tests confirm that Identity, Authorization, and downstream Domain authority are not transferred to FCA. |
| REQ-FCA-021 | Profile intent preserves validation, ownership, privacy, and accepted state, and provisional frontend outcomes never become accepted updates. |
| REQ-FCA-022 | Address evidence retains identity, ownership, applicability, validation, and freshness while all historical downstream snapshots remain unchanged. |
| REQ-FCA-023 | Address intent preserves ownership, validation, concurrency, references, failure, and downstream authority and claims acceptance only from governed evidence. |
| REQ-FCA-024 | Preference evidence and intent remain Customer-owned and distinct from Consent, Authorization, necessity, and communication policy. |
| REQ-FCA-025 | Consent presentation retains governed purpose, category, evidence, intent, and outcome, and none of the named unrelated contexts infers Consent. |
| REQ-FCA-026 | Wishlist appears only when governed, leaves guest policy unresolved, and establishes none of the prohibited commerce or access truths. |
| REQ-FCA-027 | Authorized historical summaries retain owning-Domain sources and cannot create, rewrite, or reinterpret commercial or communication history. |
| REQ-FCA-028 | Downstream entry carries only safe permitted context and performs or claims none of the named purchase or post-purchase behavior. |
| REQ-FCA-029 | Every protected capability requires current server-side contextual Authorization across all named dimensions; frontend state grants nothing. |
| REQ-FCA-030 | Manipulating labels, controls, routes, identifiers, Permissions, Claims, Scope, or Authentication grants no access and reveals no protected existence. |
| REQ-FCA-031 | Context changes cannot expose or retain state belonging to another Visitor, Identity, Principal, Session, Customer, Account, or Resource. |
| REQ-FCA-032 | Inspection finds no unnecessary Sensitive Data or prohibited Secret, credential, recovery, Payment, or personal evidence in any named surface. |
| REQ-FCA-033 | Adversarial rendering and disclosure tests prevent injection, deception, unsafe navigation, enumeration, timing leaks, and protected-state exposure. |
| REQ-FCA-034 | Material FCA evidence exposes or consumes source, applicability, freshness, and revalidation context and stale evidence is never current confirmation. |
| REQ-FCA-035 | Reordered completion tests show stale, cancelled, conflicting, or superseded outcomes cannot overwrite newer intent or presentation state. |
| REQ-FCA-036 | Repeated and concurrent mutation tests preserve accepted state, expose conflict or uncertainty, and create no silent overwrite or duplicate effect. |
| REQ-FCA-037 | Optimistic account presentation is visibly provisional and safely corrected after every rejection, conflict, failure, or uncertain outcome. |
| REQ-FCA-038 | Each material region demonstrates every applicable named state without enforcing a universal sequence or business lifecycle. |
| REQ-FCA-039 | Failures preserve provenance, context, safe next actions, and permitted recovery without fabricated success, existence, authority, or backend mechanics. |
| REQ-FCA-040 | Representative viewport, orientation, reflow, zoom, localization, and content extremes preserve understandable, operable account outcomes without fixed layout rules. |
| REQ-FCA-041 | Every named FCA interaction completes equivalent keyboard, pointer, touch, and assistive-technology paths. |
| REQ-FCA-042 | Dynamic changes preserve predictable focus and expose structure, validation, errors, status, and change through non-visual as well as visual means. |
| REQ-FCA-043 | Large governed inputs demonstrate bounded account, history, security-context, reactive, and background work without local numerical or mechanism choices. |
| REQ-FCA-044 | Independent optional-region failures leave critical Authentication, recovery, and account access available with truthful completeness and uncertainty. |
| REQ-FCA-045 | Material outcomes emit safe telemetry and correlation evidence, and classification proves no evidence type substitutes for another or creates Domain history. |
| REQ-FCA-046 | Analytics remains minimized, consent-aware, and non-authoritative with no selected provider, taxonomy, payload, identity, audience, attribution, or personalization. |
| REQ-FCA-047 | Every reachable flag state retains evidence, authority, isolation, accessibility, security, and privacy without selecting rollout or implementation. |
| REQ-FCA-048 | Contract review finds necessary bounded versioned semantics and no route, method, operation, DTO, schema, wire format, mapping, payload, persistence, transport, or provider. |
| REQ-FCA-049 | Decision review finds every named Product and technical choice unresolved or governed elsewhere with no local default embedded in FCA. |
| REQ-FCA-050 | Traceable evidence observably covers every FCA concern named by the verification Requirement. |

## 6. Requirement Traceability

| Requirement | FEB/FSC/FCD/FPE | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- | --- |
| REQ-FCA-001 | REQ-FEB-001–003; REQ-FSC-001–003; REQ-FCD-001–003; REQ-FPE-001–003 | PRODUCT.md §§21–22, 36 | REQ-BUS-047 | — | AGENTS.md §§5, 10.3, 14 | All FCA consumers |
| REQ-FCA-002 | REQ-FEB-002, 051; REQ-FSC-002–003, 043; REQ-FCD-003; REQ-FPE-003 | PRODUCT.md §§21–22, 26 | REQ-BUS-047 | — | AGENTS.md §27.1 | FCA implementation and later frontend Specifications |
| REQ-FCA-003 | REQ-FEB-003–004; REQ-FSC-003; REQ-FCD-003; REQ-FPE-003 | PRODUCT.md §§12–14, 21 | REQ-BUS-007, 047 | REQ-PRD-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-CHK-002–003; REQ-PAY-002–003; REQ-ORD-002–003; REQ-SHP-002–003; REQ-RET-002–003; REQ-CMS-002–004; REQ-NTF-002–004 | ENGINEERING-PRINCIPLES.md §§7–10 | FSC, FCD, FPE, FCP, FPP, FAD, FRP |
| REQ-FCA-004 | REQ-FEB-003–004, 011–013 | PRODUCT.md §§7, 12.3, 16.7 | REQ-BUS-007, 032, 051 | REQ-CUS-003–004, 012, 020; REQ-IDN-004–005, 018, 024 | GLOSSARY.md §§4, 7, 13, 22 | All FCA consumers |
| REQ-FCA-005 | REQ-FEB-006, 011–013 | PRODUCT.md §§7.1–7.2, 14 | REQ-BUS-007, 032 | REQ-CUS-004, 012; REQ-IDN-005, 010, 018–020 | UI.md §§28–30 | Storefront visitors and Customers |
| REQ-FCA-006 | REQ-FEB-019–021; REQ-FSC-024 | PRODUCT.md §§7.1–7.2, 14; Open Product Decision 4 | REQ-BUS-007–008, 051 | REQ-CUS-008, 026; REQ-IDN-007–008 | UI.md §§28–30; SECURITY-STANDARDS.md §§10–11 | Visitors and Customers |
| REQ-FCA-007 | REQ-FEB-014–015, 019–021, 030 | PRODUCT.md §§5.5–5.6, 17.2 | REQ-BUS-042, 051–052 | REQ-CUS-009, 026, 048; REQ-IDN-006, 009, 011 | SECURITY-STANDARDS.md §11 | Visitors and Security owners |
| REQ-FCA-008 | REQ-FEB-011, 014–016, 020 | PRODUCT.md §§7, 14, 17.3 | REQ-BUS-032, 051 | REQ-IDN-009–010, 035 | UI.md §§28–30; SECURITY-STANDARDS.md §§10–11 | Visitors and Customers |
| REQ-FCA-009 | REQ-FEB-015, 020, 030 | PRODUCT.md §§5.5–5.6, 16.12 | REQ-BUS-032, 042, 052 | REQ-CUS-042, 048; REQ-IDN-009, 011, 039 | SECURITY-STANDARDS.md §§11, 33 | Visitors, Security and Operations |
| REQ-FCA-010 | REQ-FEB-014–016, 020–021 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-042, 051 | REQ-CUS-012, 048; REQ-IDN-019–020, 039 | UI.md §§28–30 | Customers |
| REQ-FCA-011 | REQ-FEB-006–007, 011, 014–015 | PRODUCT.md §§7, 17.1 | REQ-BUS-032, 051 | REQ-CUS-004, 012; REQ-IDN-018–023 | GLOSSARY.md §22; ARCHITECTURE.md §30.5 | Customers and protected storefront journeys |
| REQ-FCA-012 | REQ-FEB-005, 007, 017 | PRODUCT.md §§5.5–5.6, 17.3 | REQ-BUS-033, 042, 051 | REQ-CUS-012, 039, 048; REQ-IDN-020–021, 023, 031 | SECURITY-STANDARDS.md §§11–12 | Customers and protected storefront journeys |
| REQ-FCA-013 | REQ-FEB-019–021, 030 | PRODUCT.md §§7.2, 20; Open Product Decisions 5, 18 | REQ-BUS-032, 039, 051 | REQ-CUS-011, 039–040; REQ-IDN-015–017 | SECURITY-STANDARDS.md §§11–12 | Visitors, Customers and support entry points |
| REQ-FCA-014 | REQ-FEB-014–017, 020–021 | PRODUCT.md §§5.6, 17.2 | REQ-BUS-042, 049, 051 | REQ-CUS-011, 048; REQ-IDN-014, 016–017 | ARCHITECTURE.md §20.4 | Visitors, Customers and Notifications |
| REQ-FCA-015 | REQ-FEB-011, 019–021 | PRODUCT.md §§20, 24; Open Product Decision 5 | REQ-BUS-032, 051 | REQ-IDN-034–035 | ARCHITECTURE.md §§23, 30 | Visitors, Customers and Product owners |
| REQ-FCA-016 | REQ-FEB-026–028 | PRODUCT.md §§16.7, 20 | REQ-BUS-039, 051 | REQ-CUS-010–011, 040; REQ-IDN-012–013 | SECURITY-STANDARDS.md §§14–18 | Customers, Identity and Security |
| REQ-FCA-017 | REQ-FEB-026–030 | PRODUCT.md §§5.5, 20 | REQ-BUS-039, 051–052 | REQ-CUS-011, 040; REQ-IDN-015–017, 043–044 | SECURITY-STANDARDS.md §§11, 14, 27 | Customers, Identity and Security |
| REQ-FCA-018 | REQ-FEB-009–010; REQ-FSC-005, 009–012, 024 | PRODUCT.md §§14, 29–30 | REQ-BUS-007–008 | REQ-CUS-006–007 | UI.md §§10–12, 28–30 | Customers and FSC |
| REQ-FCA-019 | REQ-FEB-014–015, 020–021, 030; REQ-FSC-012 | PRODUCT.md §§5.5–5.6 | REQ-BUS-032, 042 | REQ-CUS-005, 039, 048; REQ-IDN-006, 026–028, 039 | SECURITY-STANDARDS.md §18; UI.md §23 | Visitors and Customers |
| REQ-FCA-020 | REQ-FEB-003–005, 007, 041 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-007–008 | REQ-CUS-001, 003, 013–014; REQ-IDN-007 | UI.md §§17, 42 | Customers |
| REQ-FCA-021 | REQ-FEB-019, 024–025 | PRODUCT.md §§5.4–5.6, 12.3 | REQ-BUS-008, 039, 042 | REQ-CUS-013, 027, 048 | UI.md §§13–14, 23 | Customers |
| REQ-FCA-022 | REQ-FEB-003–005, 007, 041 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-008 | REQ-CUS-015–017, 036 | UI.md §§17, 42 | Customers and FCP |
| REQ-FCA-023 | REQ-FEB-019–021, 024–025 | PRODUCT.md §§5.4–5.6, 12.3 | REQ-BUS-008, 039, 042 | REQ-CUS-016–017, 027, 048 | UI.md §§13–14, 23 | Customers and FCP |
| REQ-FCA-024 | REQ-FEB-003–005, 019, 029 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-007–008, 040 | REQ-CUS-018–021 | UI.md §§17, 42 | Customers, Privacy and Notifications |
| REQ-FCA-025 | REQ-FEB-019, 026, 029 | PRODUCT.md §16.7; Open Product Decision 19 | REQ-BUS-040 | REQ-CUS-018–021, 039–040 | SECURITY-STANDARDS.md §§27–28 | Customers, Privacy and Analytics |
| REQ-FCA-026 | REQ-FEB-003–005, 019; REQ-FSC-019 | PRODUCT.md §§12.3, 14; Open Product Decision 16 | REQ-BUS-007 | REQ-PRD-020, 022–023; REQ-CUS-022–023; REQ-IDN-028; REQ-INV-008, 010; REQ-CART-002; REQ-PRC-002 | UI.md §§17, 42 | Customers, Product and FCP |
| REQ-FCA-027 | REQ-FEB-003–005, 007, 014–015 | PRODUCT.md §§12.3, 14.4 | REQ-BUS-007, 044 | REQ-CUS-024–025, 034–036; REQ-ORD-047 | UI.md §§17, 23 | Customers and FPP |
| REQ-FCA-028 | REQ-FEB-003–005, 009; REQ-FSC-010, 025 | PRODUCT.md §§13–14 | REQ-BUS-007, 009, 021–030 | REQ-CUS-024, 033–036; REQ-CART-002; REQ-CHK-002; REQ-PAY-003; REQ-ORD-002; REQ-SHP-003; REQ-RET-002 | UI.md §§12, 29–30 | FCP, FPP and Customers |
| REQ-FCA-029 | REQ-FEB-012–013, 020, 030 | PRODUCT.md §§5.5, 17.3 | REQ-BUS-032–033 | REQ-CUS-005, 039; REQ-IDN-026–028 | SECURITY-STANDARDS.md §§10–13 | All protected FCA consumers |
| REQ-FCA-030 | REQ-FEB-012–013, 030 | PRODUCT.md §§5.5, 17.3 | REQ-BUS-032–033, 052 | REQ-CUS-039; REQ-IDN-024, 026 | API.md §26; SECURITY-STANDARDS.md §§10–13 | All protected FCA consumers |
| REQ-FCA-031 | REQ-FEB-006, 008, 030 | PRODUCT.md §§5.5, 16.7 | REQ-BUS-032, 039 | REQ-CUS-004–005, 039–040; REQ-IDN-005–006 | SECURITY-STANDARDS.md §§12, 35 | Customers and Security owners |
| REQ-FCA-032 | REQ-FEB-026–030 | PRODUCT.md §§16.7, 20 | REQ-BUS-039–040 | REQ-CUS-040–041; REQ-IDN-012, 043–044 | SECURITY-STANDARDS.md §§14, 27, 35 | Customers, Privacy and Security |
| REQ-FCA-033 | REQ-FEB-027–030; REQ-FSC-013, 037 | PRODUCT.md §§5.1, 20 | REQ-BUS-032, 039, 052 | REQ-CUS-005, 039–040; REQ-IDN-006, 011 | SECURITY-STANDARDS.md §§18, 33, 35 | Visitors, Customers and Security |
| REQ-FCA-034 | REQ-FEB-005, 007, 014–015, 020 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-042, 045 | REQ-CUS-027, 048; REQ-IDN-019, 021, 031, 039 | UI.md §§23, 45 | Customers and protected journeys |
| REQ-FCA-035 | REQ-FEB-016–017 | PRODUCT.md §§5.4–5.6 | REQ-BUS-042 | REQ-CUS-027, 048; REQ-IDN-009, 016, 021, 039 | ANGULAR.md §§18, 37–38 | Customers |
| REQ-FCA-036 | REQ-FEB-019, 022–025 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-035–036, 042 | REQ-CUS-009, 027, 048; REQ-IDN-021, 040 | ARCHITECTURE.md §20 | Customers and Operations |
| REQ-FCA-037 | REQ-FEB-024–025 | PRODUCT.md §§5.4–5.6 | REQ-BUS-042 | REQ-CUS-027, 048 | ANGULAR.md §§37–38 | Customers |
| REQ-FCA-038 | REQ-FEB-014–018, 020; REQ-FSC-029 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-042 | REQ-CUS-048; REQ-IDN-039 | UI.md §§21–23; ACCESSIBILITY.md §§24–26 | Visitors and Customers |
| REQ-FCA-039 | REQ-FEB-020–022; REQ-FSC-031 | PRODUCT.md §§5.6, 17.2 | REQ-BUS-036, 042, 045 | REQ-CUS-011, 048; REQ-IDN-016–017, 039–041 | UI.md §§23, 45 | Visitors, Customers and Operations |
| REQ-FCA-040 | REQ-FEB-031, 033, 041; REQ-FSC-032, 036 | PRODUCT.md §§5.2–5.3, 5.7 | REQ-BUS-002, 037 | REQ-CUS-043; REQ-IDN-047 | ACCESSIBILITY.md §§35–38, 46, 55, 72–73 | Visitors and Customers |
| REQ-FCA-041 | REQ-FEB-032, 034–035, 037; REQ-FSC-033 | PRODUCT.md §5.7 | REQ-BUS-037 | REQ-CUS-043; REQ-IDN-047 | ACCESSIBILITY.md §§8–10, 17, 41–43, 53–54 | Visitors and Customers |
| REQ-FCA-042 | REQ-FEB-035–038; REQ-FSC-034–035 | PRODUCT.md §§5.6–5.7 | REQ-BUS-037, 042 | REQ-CUS-043, 048; REQ-IDN-047 | ACCESSIBILITY.md §§9, 11, 23–29 | Visitors and Customers |
| REQ-FCA-043 | REQ-FEB-042–044; REQ-FSC-038 | PRODUCT.md §§8.3, 18.4–18.6 | REQ-BUS-038, 045 | REQ-CUS-044; REQ-IDN-048 | PERFORMANCE.md §§16–17, 21, 35, 43–48, 54–57, 83 | Customers and Operations |
| REQ-FCA-044 | REQ-FEB-020, 043–044; REQ-FSC-030, 038 | PRODUCT.md §§5.4–5.6, 8.3 | REQ-BUS-038, 042, 045 | REQ-CUS-048; REQ-IDN-039, 048 | PERFORMANCE.md §§17, 21, 23, 35, 45, 83 | Visitors, Customers and Operations |
| REQ-FCA-045 | REQ-FEB-045–046; REQ-FSC-039 | PRODUCT.md §§18.3, 33 | REQ-BUS-034–035, 043 | REQ-CUS-045; REQ-IDN-044, 049 | ANGULAR.md §61; EVENTS.md §§6–8, 43–48 | Engineering, Security and Operations |
| REQ-FCA-046 | REQ-FEB-026, 029, 046–047; REQ-FSC-039 | PRODUCT.md §§18.3, 33; Open Product Decisions 19–20 | REQ-BUS-039–040, 043–044 | REQ-CUS-020–021, 038 | EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §§27–28 | Product, Analytics and Privacy |
| REQ-FCA-047 | REQ-FEB-048; REQ-FSC-040 | PRODUCT.md §§5.9, 21, 36–38; Open Product Decision 30 | — | — | ARCHITECTURE.md §§38.4, 43.3; ANGULAR.md §§62–63 | Customers and delivery teams |
| REQ-FCA-048 | REQ-FEB-005, 007, 014–015, 020, 042, 049; REQ-FSC-041 | PRODUCT.md §§21–22, 26 | REQ-BUS-042, 046–047, 051 | REQ-CUS-047–048; REQ-IDN-045 | ARCHITECTURE.md §§13, 36.5, 39; API.md §§9–13, 16–17, 23, 35–37, 63–72 | FCA implementation and Contract owners |
| REQ-FCA-049 | REQ-FEB-040, 047–050; REQ-FSC-042 | PRODUCT.md §§21, 25–26; Open Product Decisions 4–5, 16, 18–20, 24, 26, 28, 30 | REQ-BUS-046, 048 | — | DECISIONS.md §§25–40; ENGINEERING-PRINCIPLES.md §§11, 24, 36 | Product, Design, Engineering and later frontend Specifications |
| REQ-FCA-050 | REQ-FEB-001–003, 011–023, 026–051; REQ-FSC-002–003, 024, 043; REQ-FCD-003; REQ-FPE-003 | PRODUCT.md §§22, 26, 35 | REQ-BUS-007–008, 032–040, 042–043, 045, 047, 049, 051–053 | REQ-PRD-044; REQ-CUS-049; REQ-IDN-050; REQ-CMS-051; REQ-NTF-056; REQ-CART-033; REQ-CHK-044; REQ-PAY-045; REQ-ORD-052; REQ-SHP-043; REQ-RET-048 | TESTING-STANDARDS.md §§5, 7, 9, 11, 13, 19–24, 27–31, 33–40; ANGULAR.md §§56–60, 68 | Engineering, QA, Accessibility, Security and all FCA consumers |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following ten are materially relevant to FCA, retain their exact source wording and order, and remain unresolved by this Draft:

| Source Order | Open Product Decision | FCA Boundary |
| --- | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | FCA selects neither model and does not make registration or Authentication a purchase precondition. |
| 5 | Customer email-verification requirements. | FCA selects no verification requirement, channel, timing, provider, or mechanism. |
| 16 | Wishlist behaviour for guest and registered customers. | FCA presents Wishlist only when governed and selects no guest eligibility, persistence, sharing, limit, or policy. |
| 18 | Customer-support channels and service expectations. | FCA may present governed help or recovery entry but selects no channel, availability, target, commitment, or support policy. |
| 19 | Marketing-consent and communication-preference model. | FCA preserves Consent and Preference authority and selects no category, default, capture, channel, lawful-basis, or marketing policy. |
| 20 | Initial analytics provider and event taxonomy. | FCA selects no provider, taxonomy, event name, payload, identity model, audience, attribution, or destination. |
| 24 | Production customer-service and operational escalation process. | FCA selects no escalation, approval, service target, support override, repair, or operational workflow. |
| 26 | Fraud-screening approach and manual-review workflow. | FCA selects no provider, model, signal, threshold, blocking rule, reviewer role, or manual-review workflow. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | FCA may present governed intent and outcomes but selects no format, process, retention, deletion, anonymization, closure, or reactivation policy. |
| 30 | Product launch date, release scope, and post-launch support window. | FCA selects no launch date, initial capability set, rollout, release scope, support window, or numerical target. |

## 8. Risks and Controls

| Risk | Control |
| --- | --- |
| Customer, Account, Identity, and Principal are conflated. | Preserve canonical distinctions in every presentation, Contract expectation, and verification case. |
| Frontend or Session state is treated as Authentication or Authorization proof. | Require current governed evidence and server-side contextual Authorization for protected behavior. |
| Cross-Customer data or Resource existence is disclosed. | Enforce context isolation, concealment, safe errors, and negative ownership tests. |
| Credentials, recovery artifacts, Secrets, or Sensitive Data leak. | Minimize and prohibit protected values across content, state, routes, errors, logs, telemetry, analytics, and evidence. |
| Stale Session or privilege presentation restores access. | Revalidate material security context and reject stale, revoked, reordered, or superseded evidence. |
| Sign-out or recovery is falsely represented as successful. | Distinguish intent, pending, accepted, failure, and uncertainty and require Identity-owned confirmation. |
| Consent is inferred from activity or Preference. | Require governed purpose and evidence and test every prohibited inference source. |
| Duplicate or ambiguous registration exposes Account existence. | Use safe distinguishable outcomes, enumeration resistance, and governed recovery. |
| Stale or concurrent profile or Address intent overwrites accepted state. | Preserve initiating context, expose conflict, and require governed acceptance or revalidation. |
| Account closure rewrites retained commercial history. | Keep closure presentation separate from Order, Payment, Refund, Shipment, and Audit Record truth. |
| Optimistic state appears authoritative. | Mark optimistic presentation provisional and require safe correction after every non-accepted outcome. |
| Recovery or communication failure blocks unrelated account access. | Isolate optional dependencies and preserve truthful independent critical capabilities. |
| Authentication or recovery is inaccessible. | Verify keyboard, focus, status, error, validation, and assistive-technology outcomes. |
| Account history or profile work becomes unbounded. | Require bounded governed Contracts and frontend work without local numerical limits. |
| Telemetry, analytics, events, and Audit Records are conflated. | Classify evidence explicitly and prohibit substitution or authoritative-history creation. |
| Feature flags weaken security, privacy, isolation, or accessibility. | Verify every reachable flag state against inherited and FCA-specific obligations. |
| FCA absorbs purchase, post-purchase, Administration, or Reporting behavior. | Limit FCA to safe entry, summary, and handoff while retaining downstream ownership. |
| Authentication or Contract mechanisms leak into normative behavior. | Prohibit routes, protocols, providers, tokens, schemas, persistence, transports, and mechanism choices. |
| Protected destinations become enumerable through timing or errors. | Require consistent concealment across navigation, identifiers, timing, content, and errors. |
| Cached Customer or Consent evidence appears current. | Preserve source and freshness evidence and require material revalidation or truthful degradation. |
| Dynamic security outcomes cause focus loss or misleading status. | Require predictable focus and accessible change status for Authentication, Session, and recovery outcomes. |
| Analytics correlates Customers beyond governed purpose. | Minimize observations, preserve applicable Consent, and prohibit FCA-defined identity or audience models. |

## 9. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
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
- `specifications/domains/product/product-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-08 | Draft | Initial comprehensive Customer and Account / Identity Specification. |
| 1.0.0 | 2026-09-08 | Approved | Approved Customer and Account / Identity Specification. |

## 11. Final Validation

For this Approved baseline, verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, and scope is `FCA`, with normativity only within Customer and Account / Identity frontend scope and no repository-wide authority;
2. governing-source precedence, Product and Business Requirements, frontend inheritance, and Approved Domain authority remain preserved;
3. FCA owns only Customer and Account / Identity presentation and remains separate from FSC, FCD, FPE, FCP, FPP, FAD, and FRP;
4. Visitor, Customer, Account, Identity, Principal, Authentication, Session, Authorization, Role, Permission, Claims, Scope, and Consent remain distinct;
5. registration, sign-in, sign-out, Sessions, recovery, verification, profile, Address, Preference, Consent, Wishlist, history, and handoff remain governed and non-authoritative in FCA;
6. initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states remain distinguishable without becoming a mandatory lifecycle graph;
7. freshness, supersession, concurrency, optimistic presentation, failure provenance, and governed recovery remain correct without backend mechanism definition;
8. responsive, reflow, zoom, orientation, localization, content-extreme, keyboard, pointer, touch, focus, structure, status, validation, error, and assistive-technology evidence supports applicable WCAG 2.2 AA outcomes;
9. contextual Authorization, Resource concealment, isolation, Sensitive Data, Secrets, privacy, Consent non-inference, and safe rendering are preserved;
10. frontend work remains bounded, optional failures are isolated, and degradation remains truthful without numerical targets;
11. operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinct and non-authoritative;
12. every feature-flag state preserves evidence, authority, accessibility, security, privacy, and inherited obligations;
13. Contracts remain abstract and bounded without routes, methods, operations, DTOs, schemas, formats, mappings, payloads, persistence, transports, or providers;
14. Requirements, Acceptance Criteria, and traceability are equal in count, unique, sequential, gap-free, and one-to-one;
15. every cited Requirement and source section physically exists and directly supports its FCA Requirement;
16. all ten Product Decisions exactly match `PRODUCT.md`, remain materially complete, source-ordered, and unresolved;
17. Risks have distinct FCA-specific controls and all Related Document paths exist;
18. Revision History contains exactly the preserved `0.1.0 Draft` row and one `1.0.0 Approved` row;
19. no Glossary amendment is required and FCA-local descriptions are not repository-wide terminology;
20. no Product policy, provider, protocol, route, URL, API, schema, persistence, cache mechanism, event taxonomy, payload, numerical target, timeout, retry count, breakpoint, layout, visual token, lifecycle graph, or implementation mechanism is introduced;
21. Markdown headings and tables, UTF-8, trailing whitespace, exactly one final newline, and prohibited-marker checks pass; and
22. Git scope contains only the authorized lifecycle-promotion changes to `specifications/frontend/storefront/account-identity.md`, with nothing staged, untracked, unrelated, or otherwise modified.
