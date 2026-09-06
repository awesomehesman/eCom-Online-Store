---
title: Administration Domain
version: 0.1.0
status: Draft
owner: Product and Engineering
last_updated: 2026-09-07
authoritative: false
---

# Administration Domain

## 1. Purpose

This Specification defines implementation-neutral Requirements for Administration-owned Staff-facing workflow coordination, protected invocation of owning-Domain capabilities, operational evidence, recovery, and reconciliation.

This document uses scope code `ADM`. It is a Draft and is not yet normative. It remains subordinate to higher-authority governing sources, preserves every Approved Domain's authority, resolves no Open Product Decision, and has no repository-wide authority.

## 2. Scope, Authority, and Requirements

### REQ-ADM-001 — Lifecycle, Authority, and Scope

Administration MUST govern only Administration-owned operational coordination under scope `ADM`, preserve governing-source precedence and Approved Domain authority, and MUST NOT treat this Draft as normative or repository-wide authority before approval.

### REQ-ADM-002 — Administration-Owned Truth

Administration MUST own only administrative workflow identity, request outcomes, coordination state, Staff-facing work context, action correlation, per-action operational evidence, and Administration-owned recovery and reconciliation.

### REQ-ADM-003 — Cross-Domain Non-Authority

Administration MUST NOT own or redefine Identity, Customer, Product, Category, Inventory, Pricing, Cart, Checkout, Payment, Shipping, Order, Return, Notifications, CMS, Reporting, Analytics, fraud, tax, legal, commercial, fulfilment, content, support-policy, or Product-policy truth.

### REQ-ADM-004 — Stable Workflow Identity

Each administrative workflow MUST have stable identity distinct from actor, target Resource, UI route, correlation identifier, approval, Domain operation, and external provider identifier.

### REQ-ADM-005 — Staff User and Principal Separation

Staff User, Principal, Customer, Account, Session, Role, Permission, Claims, and Scope MUST remain distinct; an authenticated Principal MUST NOT automatically become a Staff User or gain administrative authority.

### REQ-ADM-006 — Authentication Boundary

Administration MUST consume trusted Identity-owned Authentication and Session context without owning credentials, authentication attempts, Sessions, tokens, MFA, SSO, or recovery truth.

### REQ-ADM-007 — Contextual Authorization

Every protected administrative read or action MUST require a current trusted server-side Authorization decision for the Principal, Resource, action, and owning-Domain state.

### REQ-ADM-008 — UI and Label Non-Authority

UI visibility, navigation, routes, identifiers, Staff User labels, Role names, Permissions, Claims, or Scope MUST NOT alone grant administrative access or mutation authority.

### REQ-ADM-009 — Access Change Effects

Role, Permission, assignment, Session, provisioning, deprovisioning, and privilege changes MUST remain Identity-owned and take effect in Administration according to current trusted security context without stale privilege.

### REQ-ADM-010 — Owning-Domain Invocation

Every administrative business operation MUST invoke the owning Domain's governed capability and preserve its current invariants, validation, authorization, and outcome semantics.

### REQ-ADM-011 — Direct Mutation Prohibition

Administration MUST NOT directly rewrite another Domain's state or use direct database, provider, cache, queue, or projection manipulation as business authority.

### REQ-ADM-012 — Read and Investigation Boundary

Administrative investigation MAY correlate permitted evidence from multiple Domains but MUST preserve provenance, uncertainty, masking, current Authorization, and each source's authority.

### REQ-ADM-013 — Customer Support Boundary

Support workflows MUST remain purpose-bound, least-privileged, privacy-aware, attributable, and unable to redefine Customer, Order, Payment, Shipment, Return, Refund, or Notification outcomes.

### REQ-ADM-014 — Approval Coordination Boundary

Where Approved policy requires review or approval, Administration MAY coordinate requests and evidence but MUST NOT invent an approval chain, approver count, organizational role, override, threshold, or policy outcome.

### REQ-ADM-015 — High-Risk Confirmation

A governed high-Risk administrative action MUST require current Authorization and applicable confirmation, reason, evidence, and owning-Domain validation without defining which actions are high-Risk or a confirmation mechanism.

### REQ-ADM-016 — Bulk Operation Boundary

A bulk administrative request MUST validate authorization and owning-Domain eligibility per affected Resource and produce distinguishable per-item accepted, rejected, failed, uncertain, and skipped outcomes.

### REQ-ADM-017 — Bulk Partial Completion

Partial completion MUST preserve successful owning-Domain effects, identify unknown effects, and support safe continuation or reconciliation without automatic rollback or duplicate replay.

### REQ-ADM-018 — Duplicate, Retry, and Replay Safety

Duplicate, retried, or replayed administrative requests MUST NOT multiply harmful effects, bypass current Authorization, revive invalid intent, or overwrite newer Domain state.

### REQ-ADM-019 — Concurrency and Stale Context

Concurrent or stale administrative work MUST detect conflicting Resource, authorization, or Domain-state changes and MUST NOT silently overwrite an accepted newer outcome.

### REQ-ADM-020 — Failure and Uncertainty

Validation, authorization, dependency, provider, timeout, partial, and unknown-effect conditions MUST remain distinguishable from success and retain sufficient safe evidence for investigation.

### REQ-ADM-021 — Controlled Recovery and Repair

Recovery, repair, resend, retry, cancellation, or replay MUST be explicitly supported, currently authorized, evidence-based, duplicate-safe, and constrained by owning-Domain state and policy.

### REQ-ADM-022 — Reconciliation

Administration MUST support governed discrepancy visibility and coordination while each owning Domain retains reconciliation and repair authority, provenance, uncertainty, and outcome history.

### REQ-ADM-023 — Product and Category Administration

Catalogue workflows MUST invoke Product or Category capabilities and preserve Product, Product Variant, Product Media, publication, sellability, taxonomy, hierarchy, membership, and navigation authority.

### REQ-ADM-024 — CMS Administration

Content workflows MUST invoke CMS capabilities and preserve CMS Content identity, version, readiness, publication, withdrawal, placement, correction, and history without selecting content-approval or scheduling policy.

### REQ-ADM-025 — Pricing and Promotion Administration

Commercial workflows MUST invoke Pricing capabilities and preserve Price, Money, Currency, Discount, Promotion, Voucher, tax, eligibility, stacking, calculation, and historical authority.

### REQ-ADM-026 — Inventory Administration

Inventory workflows MUST invoke Inventory capabilities and preserve Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, concurrency, history, and reconciliation authority.

### REQ-ADM-027 — Customer Administration

Customer and privacy workflows MUST invoke Customer capabilities, preserve Customer isolation, purpose limitation, Consent, Preference, historical truth, and data-request boundaries, and expose only necessary information.

### REQ-ADM-028 — Cart and Checkout Administration

Administrative visibility or support MUST NOT create Cart intent, Checkout progression, commercial validation, Inventory commitment, Payment success, or Order creation, and any permitted action MUST use the owning capability.

### REQ-ADM-029 — Payment and Refund Administration

Payment and Refund workflows MUST invoke Payment capabilities, require validated provider evidence where applicable, protect financial data, preserve uncertainty, and MUST NOT create Payment, Refund, or Refund Transaction truth locally.

### REQ-ADM-030 — Order Administration

Order workflows MUST invoke Order capabilities and preserve Order identity, immutable snapshots, Order Item history, lifecycle, cancellation, Return, Refund, and cross-Domain separation.

### REQ-ADM-031 — Shipping and Fulfilment Administration

Fulfilment and Shipment workflows MUST invoke Shipping capabilities and preserve delivery choice, quotation, fulfilment, Shipment, Dispatch, tracking, provider-evidence, failure, and reconciliation authority.

### REQ-ADM-032 — Return Administration

Return workflows MUST invoke Return capabilities and preserve Return Request, eligibility, authorization, receipt, inspection, disposition, lifecycle, Inventory, Refund, and reverse-logistics boundaries.

### REQ-ADM-033 — Notifications Administration

Preview, resend, investigation, recovery, or reconciliation MUST invoke Notifications capabilities and preserve recipient, template, channel, attempt, provider, Delivery Status, and delivery authority.

### REQ-ADM-034 — Fraud and Manual Review Boundary

Administration MAY present governed fraud evidence and coordinate an authorized review but MUST NOT define fraud policy, vendor, model, threshold, rule, blocking outcome, or Payment proof.

### REQ-ADM-035 — Export Boundary

Administrative exports MUST require explicit contextual Authorization, use appropriate authoritative sources, preserve definitions and provenance, minimize Sensitive Data, remain non-authoritative, and produce proportional evidence where material.

### REQ-ADM-036 — Reporting and Analytics Boundary

Administration MAY display Reporting or Analytics Projections but MUST treat them as stale-capable and non-authoritative and MUST NOT use them to mutate transactional state.

### REQ-ADM-037 — Sensitive Data and Object Isolation

Administration MUST enforce object-level isolation, data minimization, masking where governed, enumeration resistance, and safe handling of Sensitive Data across views, searches, logs, errors, exports, events, and evidence.

### REQ-ADM-038 — Secrets and Provider Credentials

Secrets, credentials, tokens, raw Payment data, provider secrets, and internal security detail MUST NOT appear in administrative content, URLs, ordinary views, logs, telemetry, exports, events, or support evidence.

### REQ-ADM-039 — Administrative Search

Administrative search MUST enforce current Authorization and object isolation, identify source and staleness, return bounded results, and MUST NOT become an authoritative index or expose inaccessible Resources.

### REQ-ADM-040 — Operational Notes Boundary

Administrative notes MAY record permitted operational context but MUST NOT alter Domain history, contain unnecessary Sensitive Data, become Customer-visible without governance, or substitute for an owning-Domain outcome.

### REQ-ADM-041 — Escalation Boundary

Administration MAY coordinate escalation only where governed, retaining reason, ownership, status, evidence, and outcome without defining channels, levels, targets, staffing, or resolution policy.

### REQ-ADM-042 — Accessible Administration

Administrative journeys MUST support applicable WCAG 2.2 AA outcomes, including keyboard operation, focus, labels, errors, status, confirmation, tables, bulk outcomes, and recovery without making client accessibility state authoritative.

### REQ-ADM-043 — Bounded Operations

Administrative reads, searches, histories, exports, bulk actions, and cross-Domain views MUST support bounded delivery and MUST NOT require unbounded Resource collections or history.

### REQ-ADM-044 — Observability

Material administrative workflows MUST be observable, correlated, attributable, and diagnosable across Principal, Resource, action, owning Domain, request, outcome, and downstream effects without defining numerical targets.

### REQ-ADM-045 — Proportional Audit Records

Governed high-Risk, privileged, financial, security-sensitive, approval, repair, export, bulk, and policy-affecting actions MUST produce attributable proportional Audit Records, while routine low-Risk reads MUST NOT automatically receive high-Risk treatment.

### REQ-ADM-046 — Contract Boundary

Administration Contracts MUST expose only necessary bounded versioned semantics and preserve owning-Domain outcomes; routes, methods, DTOs, schemas, tables, classes, workflow engines, and provider payloads remain outside this Specification.

### REQ-ADM-047 — Conditional Events

Where required, administrative Domain or Integration Events MUST represent completed Administration-owned facts, preserve owning-Domain producer authority and correlation, and MUST NOT invent canonical names, payloads, topics, brokers, transports, or event taxonomy.

### REQ-ADM-048 — Configuration Boundary

Administration MAY coordinate approved configuration changes only through the owning capability, preserving validation, history, Authorization, rollback or recovery semantics where governed, and MUST NOT create a generic unowned configuration authority.

### REQ-ADM-049 — Deletion and Retention Boundary

Administrative deletion, archival, anonymization, or retention actions MUST invoke the owning Domain, preserve required historical and audit evidence, and MUST NOT define retention periods, legal conclusions, physical mechanisms, or automatic hard deletion.

### REQ-ADM-050 — Administration Recovery State

Administration-owned workflow recovery MUST preserve request identity, prior coordination effects, current authorization, owning-Domain outcomes, and explicit pending or uncertain state without inventing a complete lifecycle graph.

### REQ-ADM-051 — Policy Neutrality

Administration MUST keep unresolved approval, escalation, support, fraud, export, access, commercial, fulfilment, content, tax, privacy, and other Product-policy values explicit and MUST NOT select defaults through workflow behavior.

### REQ-ADM-052 — Implementation Neutrality

Administration MUST NOT prescribe providers, protocols, APIs, schemas, persistence, workflow engines, queues, event names, UI components, access matrices, approval chains, numerical limits, retry counts, retention periods, or transition graphs.

### REQ-ADM-053 — Verification Coverage

Verification evidence MUST cover authority, Identity separation, Authorization, owning-Domain invocation, isolation, support, approval, bulk outcomes, failure, concurrency, recovery, reconciliation, security, accessibility, observability, audit, Contracts, events, policy, and implementation neutrality.

## 3. Canonical Terminology

Administration uses canonical terms with their `GLOSSARY.md` meanings. Administrative workflow, work context, investigation, bulk operation, recovery coordination, and escalation coordination are ADM-scoped descriptions and establish no repository-wide vocabulary or complete mandatory lifecycle graph.

## 4. Domain Context

Administration is a protected operational coordination Domain. It gives authorized Staff Users governed access to owning-Domain capabilities and evidence without duplicating their rules or authority.

## 5. Acceptance Criteria

| Requirement | Acceptance Criteria |
| --- | --- |
| REQ-ADM-001 | Metadata and review evidence show `0.1.0 Draft`, `authoritative: false`, scope `ADM`, no Draft or repository-wide normativity, and preserved governing and Approved Domain authority. |
| REQ-ADM-002 | Each administrative workflow and request has one ADM identity, explicit outcome, correlated work context and evidence, and Administration-owned recovery state without claiming business-Domain truth. |
| REQ-ADM-003 | No administrative record, view, request, action, approval, repair, export, or workflow can establish or alter any listed external authority or policy. |
| REQ-ADM-004 | Workflow identity remains stable as actors, views, correlation, Domain outcomes, or provider references change, and none substitutes for identity. |
| REQ-ADM-005 | Tests distinguish every named concept and deny administrative capability when only authentication or an unrelated business identity exists. |
| REQ-ADM-006 | Administrative entry succeeds only with trusted Identity context; Administration cannot issue or redefine credentials, Sessions, tokens, MFA, SSO, or recovery outcomes. |
| REQ-ADM-007 | A protected operation is allowed only when all four authorization contexts are current; absence or mismatch produces safe denial. |
| REQ-ADM-008 | Possessing or manipulating any listed client-visible value never bypasses the trusted server-side decision, and unauthorized UI state remains non-authoritative. |
| REQ-ADM-009 | Revoked or changed access cannot continue through cached navigation, an old Session context, or stale Claims, while Administration creates no Identity outcome. |
| REQ-ADM-010 | Each supported operation reaches the owning Domain use case; invalid or unauthorized requests are rejected there, and ADM cannot bypass its invariants. |
| REQ-ADM-011 | No administrative path changes business state except through an owning-Domain capability; direct technical manipulation cannot count as an accepted business outcome. |
| REQ-ADM-012 | Authorized investigators see only necessary correlated evidence with source and uncertainty; the view cannot modify or merge authoritative Domain truth. |
| REQ-ADM-013 | A support user can perform only a governed action on authorized context; unrelated Customer data and prohibited mutations remain unavailable and every material action is attributable. |
| REQ-ADM-014 | Approval coordination records request and evidence only under an approved policy; absent policy produces no invented workflow, role, count, threshold, override, or business outcome. |
| REQ-ADM-015 | When governing policy marks an action high-Risk, missing authorization, confirmation, reason, evidence, or Domain validation prevents execution; no local risk list or mechanism is created. |
| REQ-ADM-016 | A mixed bulk request never inherits permission or eligibility across items; each item has its own outcome and failures do not falsely report whole-batch success. |
| REQ-ADM-017 | After interruption, completed, failed, and unknown item effects remain distinct; continuation avoids repeating known success and does not assume universal rollback. |
| REQ-ADM-018 | Repetition yields the prior safe outcome or a current rejection, never duplicated harm, stale authorization, revived intent, or lost newer state. |
| REQ-ADM-019 | A stale or concurrent request receives an explicit conflict or refreshed outcome and cannot overwrite newer state or rely on superseded access. |
| REQ-ADM-020 | Each listed failure class yields a distinct non-success outcome; unknown effects remain explicit and evidence is sufficient but safely minimized. |
| REQ-ADM-021 | Unsupported or unauthorized recovery is denied; permitted recovery uses current evidence and Domain rules and cannot duplicate effects or fabricate success. |
| REQ-ADM-022 | Cross-Domain discrepancies remain visible and source-labelled until the owning Domain reconciles them; ADM records coordination but cannot perform an unauthorized rewrite. |
| REQ-ADM-023 | Administrative catalogue changes produce only Product- or Category-owned outcomes and cannot bypass publication, media, hierarchy, membership, or navigation invariants. |
| REQ-ADM-024 | An administrative content action returns a CMS-owned outcome, preserves all CMS invariants, and creates no local approval chain or schedule rule. |
| REQ-ADM-025 | Administrative commercial actions use Pricing-owned validation and outcomes; ADM neither calculates values nor selects unresolved Promotion, tax, or credit policy. |
| REQ-ADM-026 | Authorized inventory actions produce Inventory-owned outcomes with reason and history; ADM cannot directly alter quantities, reservations, or movements. |
| REQ-ADM-027 | A Staff User sees or changes only authorized Customer context through Customer-owned behavior; cross-Customer access, excess PII, inferred Consent, and historical rewriting are prevented. |
| REQ-ADM-028 | Support can inspect permitted Cart or Checkout evidence without creating intent or progression; any governed action preserves Cart and Checkout authority. |
| REQ-ADM-029 | Finance operations expose only authorized minimized evidence; provider or UI claims cannot prove success, and every action returns a Payment-owned outcome. |
| REQ-ADM-030 | Administrative Order actions preserve snapshots and history and yield Order-owned outcomes; ADM cannot transition Order state or decide cancellation, Return, or Refund policy. |
| REQ-ADM-031 | Operational actions return Shipping-owned outcomes, and ADM cannot create Dispatch, delivery, tracking, or provider truth. |
| REQ-ADM-032 | Staff actions yield Return-owned outcomes, preserve distinct adjacent states, and do not decide eligibility, disposition, Refund, or exchange policy. |
| REQ-ADM-033 | Permitted operations produce Notifications-owned outcomes; ADM cannot claim delivery, alter source facts, infer channels, or expose protected recipients. |
| REQ-ADM-034 | Fraud context remains advisory and protected; no administrative review establishes Payment proof or locally selects screening or blocking policy. |
| REQ-ADM-035 | An export is denied without current authorization; permitted output is minimized, source-labelled, definition-preserving, non-authoritative, and auditable when material. |
| REQ-ADM-036 | Dashboards visibly preserve definitions, sources, staleness, and non-authority; no report or analytical value can trigger an ungoverned business mutation. |
| REQ-ADM-037 | Unauthorized or cross-object access is denied without enumeration; every listed surface exposes only necessary protected data and applies governed masking. |
| REQ-ADM-038 | Inspection of all named surfaces finds no prohibited secret or raw financial/security material, while authorized provider operations remain behind owning Contracts. |
| REQ-ADM-039 | Queries return only authorized bounded results with source/staleness context; inaccessible records are neither returned nor discoverable, and the index cannot mutate truth. |
| REQ-ADM-040 | A note is attributable, purpose-bound, non-authoritative, safely protected, and incapable of changing Domain state or public presentation without a governed capability. |
| REQ-ADM-041 | A governed escalation retains its reason, ownership, status, evidence, and outcome; without governing policy, no escalation occurs and Administration invents no channel, level, target, staffing rule, or resolution policy. |
| REQ-ADM-042 | Representative workflows expose all listed information and actions accessibly through keyboard and assistive technology, including failures and partial bulk results. |
| REQ-ADM-043 | Each listed operation supports a bounded request and response while preserving completeness semantics and without specifying a numerical limit. |
| REQ-ADM-044 | Operational evidence correlates all listed contexts and distinguishes success, failure, denial, partial completion, and uncertainty without mandated tooling or targets. |
| REQ-ADM-045 | Material governed actions record actor, Resource, action, reason where required, time, and outcome; ordinary low-Risk reads are not automatically over-audited. |
| REQ-ADM-046 | Contract review finds only necessary versioned ADM coordination semantics and faithful Domain outcomes, with none of the prohibited implementation designs. |
| REQ-ADM-047 | Any emitted event follows a completed ADM fact and retains correlation and producer authority; no event claims an owning-Domain outcome or mandates prohibited event design. |
| REQ-ADM-048 | A configuration change invokes an identified owning capability only with current contextual Authorization, preserves owning-Domain validation, history, and governed rollback or recovery semantics, fails safely when unowned, invalid, unauthorized, or ambiguous, and creates no competing Administration truth. |
| REQ-ADM-049 | Each applicable deletion, archival, anonymization, or retention action invokes the owning Domain and preserves required historical and Audit evidence; Administration defines no retention period, legal conclusion, physical deletion mechanism, or mandatory automatic hard deletion. |
| REQ-ADM-050 | Recovery can reconstruct prior ADM coordination and resume safely only with current authorization; prior Domain effects remain intact and uncertainty is never shown as success. |
| REQ-ADM-051 | For every unresolved policy-dependent path, ADM either consumes an approved outcome or exposes a deferred/unsupported result; no local default resolves policy. |
| REQ-ADM-052 | Specification review finds no prohibited mechanism, fixed value, access matrix, approval chain, or complete lifecycle traversal in any ADM Requirement or criterion. |
| REQ-ADM-053 | Evidence includes positive and negative scenarios for every listed concern and each Requirement, without selecting a framework, test identifier scheme, or numerical coverage target. |

## 6. Requirement Traceability

| Requirement | PRODUCT.md | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- |
| REQ-ADM-001 | PRODUCT.md §§13, 17.3 | REQ-BUS-001–003, 048 | REQ-IDN-001; REQ-CMS-001 | AGENTS.md §§1, 3.6, 5; ARCHITECTURE.md §§9–10 | All administrative consumers |
| REQ-ADM-002 | PRODUCT.md §§7.3–7.7, 12.9 | REQ-BUS-031–036 | REQ-IDN-037 | ARCHITECTURE.md §§9–10, 30 | Administration, Operations |
| REQ-ADM-003 | PRODUCT.md §§13, 17.3, 24 | REQ-BUS-001–003, 031–036, 044, 048 | REQ-PRD-001–002, 049; REQ-CAT-001–002, 035; REQ-CUS-001–002, 037; REQ-IDN-002–003, 037; REQ-INV-002–003, 027; REQ-CART-002–003, 024; REQ-PRC-002–003, 026; REQ-PAY-002–003, 037; REQ-SHP-002–003, 033; REQ-CHK-002–003, 012, 042; REQ-ORD-002–003, 048; REQ-RET-002–003, 022; REQ-NTF-002–003, 045; REQ-CMS-002–004, 028 | AGENTS.md §§3.6, 5; ARCHITECTURE.md §§9–10 | All Domains, Administration |
| REQ-ADM-004 | PRODUCT.md §§15, 17.4 | REQ-BUS-034–036 | — | DATABASE.md §42; SECURITY-STANDARDS.md §27 | Administration, Audit |
| REQ-ADM-005 | PRODUCT.md §§7.3–7.7, 12.9 | REQ-BUS-031–033 | REQ-CUS-004, 010–012; REQ-IDN-005, 010, 024 | ARCHITECTURE.md §30; SECURITY-STANDARDS.md §§10–12 | Identity, Administration, Security |
| REQ-ADM-006 | PRODUCT.md §§7, 16.4, 24 | REQ-BUS-031–032, 051 | REQ-IDN-009–023, 034 | ARCHITECTURE.md §30; SECURITY-STANDARDS.md §§10–12 | Identity, Administration |
| REQ-ADM-007 | PRODUCT.md §§7.3–7.7, 17.3 | REQ-BUS-031–033 | REQ-IDN-027–028, 037 | SECURITY-STANDARDS.md §§10–12, 27; ARCHITECTURE.md §30 | Administration, Security, all owning Domains |
| REQ-ADM-008 | PRODUCT.md §§12.9, 29–30 | REQ-BUS-032 | REQ-IDN-006, 020, 026–028 | UI.md; ANGULAR.md; SECURITY-STANDARDS.md §§10–12 | Administration frontend, Security |
| REQ-ADM-009 | PRODUCT.md §§12.9, 16.4, 24 | REQ-BUS-032–033, 051 | REQ-IDN-025, 029–031 | ARCHITECTURE.md §30; SECURITY-STANDARDS.md §§10–12 | Identity, Administration, Security |
| REQ-ADM-010 | PRODUCT.md §§15, 17.3 | REQ-BUS-031–036 | REQ-PRD-049–050; REQ-CAT-035–036; REQ-CUS-037, 039; REQ-IDN-028, 037; REQ-INV-027–028; REQ-CART-024; REQ-PRC-026; REQ-PAY-030, 037; REQ-SHP-033–034; REQ-CHK-012, 033; REQ-ORD-042, 048; REQ-RET-022, 040; REQ-NTF-045–046; REQ-CMS-027–028 | ARCHITECTURE.md §§9–12, 25.4 | All owning Domains, Administration |
| REQ-ADM-011 | PRODUCT.md §§5.6, 17.3 | REQ-BUS-031–036 | REQ-ORD-048; REQ-SHP-033; REQ-PAY-037 | ARCHITECTURE.md §§9–12, 25.4; DATABASE.md | Administration, Operations, owning Domains |
| REQ-ADM-012 | PRODUCT.md §§7.6–7.7, 15.4, 20 | REQ-BUS-031–034, 044 | REQ-ORD-048–051; REQ-PAY-037, 042–043; REQ-SHP-033, 040–041; REQ-NTF-043–055 | SECURITY-STANDARDS.md §§12, 27, 35 | Support, Operations, Audit |
| REQ-ADM-013 | PRODUCT.md §§7.6, 15.4, 20, 24 | REQ-BUS-031–036 | REQ-CUS-037, 040–045; REQ-ORD-042, 048; REQ-PAY-030, 037; REQ-SHP-033–034; REQ-RET-022, 040; REQ-NTF-045–048 | SECURITY-STANDARDS.md §§10–12, 27, 35 | Support, Customer, Security |
| REQ-ADM-014 | PRODUCT.md §§15, 24 (Decisions 22–24) | REQ-BUS-033–034, 038, 048 | REQ-IDN-025–030; REQ-CMS-012, 028 | SECURITY-STANDARDS.md §§10–12, 27 | Administration, Product, Security |
| REQ-ADM-015 | PRODUCT.md §§16.7, 17.3, 24 | REQ-BUS-033–034, 052 | REQ-CUS-045; REQ-INV-033; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040; REQ-CHK-042; REQ-ORD-050; REQ-RET-046; REQ-NTF-054; REQ-CMS-048 | SECURITY-STANDARDS.md §§10–12, 27 | Administration, Security, Audit |
| REQ-ADM-016 | PRODUCT.md §§15, 31.4 | REQ-BUS-032–036, 053 | REQ-IDN-028; REQ-INV-027–028; REQ-PRD-049–050 | API.md §§48, 54–58; SECURITY-STANDARDS.md §§10–12 | Administration, Operations, owning Domains |
| REQ-ADM-017 | PRODUCT.md §§17.2, 23, 35 | REQ-BUS-035–036, 045 | REQ-INV-030; REQ-PAY-024–025; REQ-SHP-029–030 | ARCHITECTURE.md §§20, 26; API.md §§48, 54–58 | Operations, owning Domains |
| REQ-ADM-018 | PRODUCT.md §§17.3–17.4, 35 | REQ-BUS-035–036 | REQ-CART-022; REQ-CHK-024–025; REQ-PAY-022–023; REQ-NTF-042 | API.md §§48, 54–58; DATABASE.md §52 | Administration, Operations, owning Domains |
| REQ-ADM-019 | PRODUCT.md §§17.2–17.4, 35 | REQ-BUS-035 | REQ-PRD-035; REQ-CAT-024; REQ-INV-016; REQ-IDN-031 | DATABASE.md §§42, 52; API.md §§54–58 | Administration, owning Domains |
| REQ-ADM-020 | PRODUCT.md §§17.2, 23, 35 | REQ-BUS-035, 045–046 | REQ-PAY-020–025; REQ-SHP-025–030; REQ-NTF-041 | ARCHITECTURE.md §§20, 26; API.md §§54–58 | Administration, Operations, Support |
| REQ-ADM-021 | PRODUCT.md §§15.4, 17.3–17.4, 24 | REQ-BUS-036 | REQ-CUS-037; REQ-IDN-041; REQ-INV-030; REQ-PRC-028; REQ-PAY-025, 037; REQ-SHP-030, 033; REQ-ORD-041, 048; REQ-RET-039; REQ-NTF-043 | SECURITY-STANDARDS.md §§10–12, 27 | Administration, Operations, Support |
| REQ-ADM-022 | PRODUCT.md §§17.4, 20, 23, 35 | REQ-BUS-036, 044–045 | REQ-INV-030; REQ-PRC-028; REQ-PAY-024; REQ-SHP-029; REQ-ORD-041; REQ-RET-039; REQ-NTF-044; REQ-CMS-044 | ARCHITECTURE.md §§20, 26 | Operations, Audit, all owning Domains |
| REQ-ADM-023 | PRODUCT.md §§7.3–7.4, 12.2, 15.1 | REQ-BUS-009–012, 038, 050 | REQ-PRD-014–020, 034–039, 045, 049–051; REQ-CAT-010–015, 017–028, 035–037 | ARCHITECTURE.md §§9, 29 | Product, Category, Administration |
| REQ-ADM-024 | PRODUCT.md §§7.3–7.4, 15.6, 24 | REQ-BUS-038, 050 | REQ-CMS-005–019, 027–029, 049–050 | ARCHITECTURE.md §§9, 29–30 | CMS, Administration, Storefront |
| REQ-ADM-025 | PRODUCT.md §§7.3, 12.6, 16.3, 24 | REQ-BUS-014–016 | REQ-PRC-002–015, 019, 021–026 | ARCHITECTURE.md §9 | Pricing, Administration, Finance |
| REQ-ADM-026 | PRODUCT.md §§7.5, 12.7, 15.3 | REQ-BUS-013, 031–036 | REQ-INV-002–020, 027–030, 033 | ARCHITECTURE.md §9 | Inventory, Fulfilment, Administration |
| REQ-ADM-027 | PRODUCT.md §§7.6, 12.8, 16.5, 24 | REQ-BUS-008, 031–034 | REQ-CUS-005, 013–029, 037, 039–045 | SECURITY-STANDARDS.md §§12, 27, 35 | Customer, Support, Privacy |
| REQ-ADM-028 | PRODUCT.md §§9.2–9.3, 15.4 | REQ-BUS-017–024, 031–036 | REQ-CART-002–003, 024–026; REQ-CHK-002–003, 012, 033–035, 042 | SECURITY-STANDARDS.md §§10–12, 27 | Support, Cart, Checkout |
| REQ-ADM-029 | PRODUCT.md §§7.7, 12.6, 15.5, 24 | REQ-BUS-025–030, 031–036 | REQ-PAY-010–025, 030–039, 042–044 | SECURITY-STANDARDS.md §§12, 27, 35; ARCHITECTURE.md §25.5 | Payment, Finance, Support |
| REQ-ADM-030 | PRODUCT.md §§7.5–7.7, 15.3–15.5, 17, 24 | REQ-BUS-025–036 | REQ-ORD-002–020, 034–050 | ARCHITECTURE.md §25.4 | Order, Fulfilment, Support |
| REQ-ADM-031 | PRODUCT.md §§7.5, 12.7, 15.3, 24 | REQ-BUS-025–036 | REQ-SHP-002–030, 033–040 | ARCHITECTURE.md §25.4 | Shipping, Fulfilment, Administration |
| REQ-ADM-032 | PRODUCT.md §§7.6–7.7, 15.4–15.5, 16.11, 24 | REQ-BUS-025–036 | REQ-RET-002–043, 046 | SECURITY-STANDARDS.md §§10–12, 27 | Return, Support, Finance |
| REQ-ADM-033 | PRODUCT.md §§7.6, 15.4, 20, 24 | REQ-BUS-036, 049 | REQ-NTF-005–030, 041–055 | ARCHITECTURE.md §§14, 26; SECURITY-STANDARDS.md §35 | Notifications, Support, Administration |
| REQ-ADM-034 | PRODUCT.md §§16.7, 24 (Decision 26) | REQ-BUS-052 | REQ-PRC-022; REQ-PAY-033; REQ-CHK-036; REQ-RET-041 | SECURITY-STANDARDS.md §§12, 27, 35 | Security, Fraud operations, Payment |
| REQ-ADM-035 | PRODUCT.md §§12.10, 24 | REQ-BUS-044, 053 | REQ-CUS-029, 038, 040–045; REQ-NTF-055; REQ-CMS-039 | SECURITY-STANDARDS.md §§12, 27, 35 | Administration, Reporting, Audit |
| REQ-ADM-036 | PRODUCT.md §§12.10, 18, 24 | REQ-BUS-044, 053 | REQ-CUS-038; REQ-NTF-055; REQ-CMS-039 | ARCHITECTURE.md §§9, 14, 26 | Administration, Reporting, Analytics |
| REQ-ADM-037 | PRODUCT.md §§5.5, 7.6–7.7, 16.5 | REQ-BUS-031–034, 053 | REQ-CUS-005, 040–041; REQ-IDN-006, 043–044; REQ-PAY-031–032 | SECURITY-STANDARDS.md §§7, 12, 27, 35 | Security, Privacy, Administration |
| REQ-ADM-038 | PRODUCT.md §§5.5, 23 | REQ-BUS-034, 046, 053 | REQ-IDN-012, 043–044; REQ-PAY-031–032; REQ-NTF-047–048 | SECURITY-STANDARDS.md §§7, 12, 27 | Security, Operations, provider adapters |
| REQ-ADM-039 | PRODUCT.md §§7.6, 12.8–12.9 | REQ-BUS-031–034, 044 | REQ-CUS-005, 039–040; REQ-IDN-006, 028 | ARCHITECTURE.md §26; SECURITY-STANDARDS.md §§10–12 | Administration, Support, Search |
| REQ-ADM-040 | PRODUCT.md §§7.6, 15.4, 17.4 | REQ-BUS-033–034 | REQ-CUS-040–045; REQ-ORD-019–020, 050 | SECURITY-STANDARDS.md §§12, 27, 35 | Support, Audit, Customer |
| REQ-ADM-041 | PRODUCT.md §§7.6–7.7, 15.4, 24 (Decisions 18, 24) | REQ-BUS-036 | REQ-NTF-045; REQ-ORD-048 | SECURITY-STANDARDS.md §27 | Support, Operations, Administration |
| REQ-ADM-042 | PRODUCT.md §§6.4, 29–30 | REQ-BUS-042 | REQ-CMS-034; REQ-IDN-047 | ACCESSIBILITY.md §§1–32; DESIGN-SYSTEM.md; UI.md | Administration frontend, Accessibility |
| REQ-ADM-043 | PRODUCT.md §§23, 31 | REQ-BUS-037, 045, 053 | REQ-CUS-044; REQ-INV-032; REQ-PRC-030; REQ-PAY-041; REQ-SHP-039; REQ-ORD-049; REQ-RET-045; REQ-NTF-053 | PERFORMANCE.md; API.md §§37–61 | Administration, Operations |
| REQ-ADM-044 | PRODUCT.md §§18, 23, 35 | REQ-BUS-045 | REQ-IDN-048; REQ-PAY-041; REQ-SHP-039; REQ-CHK-041; REQ-ORD-049; REQ-RET-045; REQ-NTF-053; REQ-CMS-047 | ARCHITECTURE.md §§20, 31; SECURITY-STANDARDS.md §27 | Operations, Support |
| REQ-ADM-045 | PRODUCT.md §§7, 17.4, 31 | REQ-BUS-033–034, 043 | REQ-CUS-045; REQ-IDN-049; REQ-INV-033; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040; REQ-CHK-042; REQ-ORD-050; REQ-RET-046; REQ-NTF-054; REQ-CMS-048 | SECURITY-STANDARDS.md §§7.9, 27; DATABASE.md §42 | Audit, Security, Administration |
| REQ-ADM-046 | PRODUCT.md §§13, 37 | REQ-BUS-037, 046 | REQ-PRD-052; REQ-CAT-041; REQ-CUS-047; REQ-IDN-045; REQ-INV-035; REQ-CART-031; REQ-PRC-033; REQ-PAY-039; REQ-SHP-037; REQ-CHK-038; REQ-ORD-045; REQ-RET-042; REQ-NTF-050; REQ-CMS-045 | API.md §§37–61; ARCHITECTURE.md §§12–16 | Administration clients, owning Domains |
| REQ-ADM-047 | PRODUCT.md §§13, 17, 20, 24 | REQ-BUS-039, 046 | REQ-IDN-046; REQ-NTF-051; REQ-CMS-046 | EVENTS.md; ARCHITECTURE.md §14 | Event consumers, Audit, Operations |
| REQ-ADM-048 | PRODUCT.md §§12.9, 15, 24 | REQ-BUS-031–036, 048 | REQ-IDN-037; REQ-CMS-028 | ARCHITECTURE.md §§9–10, 30; SECURITY-STANDARDS.md §§10–12 | Administration, Operations, owning Domains |
| REQ-ADM-049 | PRODUCT.md §§16.5, 17.4, 24, 37 | REQ-BUS-034, 048, 053–054 | REQ-CUS-028–029, 041; REQ-ORD-019–020; REQ-CMS-050 | DATABASE.md §§42, 48–52; SECURITY-STANDARDS.md §35 | Privacy, Legal, Audit, owning Domains |
| REQ-ADM-050 | PRODUCT.md §§17.2–17.4, 35 | REQ-BUS-035–036, 045 | REQ-IDN-041; REQ-NTF-043–044; REQ-CMS-043–044 | ARCHITECTURE.md §§20, 26; API.md §§48, 54–58 | Administration, Operations |
| REQ-ADM-051 | PRODUCT.md §24 | REQ-BUS-048, 052–054 | REQ-PRC-012–015, 021–022; REQ-IDN-030, 034; REQ-CMS-012 | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9 | Product, Administration, all owning Domains |
| REQ-ADM-052 | PRODUCT.md §§24, 37 | REQ-BUS-037, 046, 048 | — | AGENTS.md §§3.2, 3.10, 5; DOCUMENTATION-STANDARDS.md §§7–9 | Engineering, Architecture |
| REQ-ADM-053 | PRODUCT.md §§20, 31, 34 | REQ-BUS-040–043 | REQ-PRD-044; REQ-CAT-043; REQ-CUS-049; REQ-IDN-050; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-CHK-044; REQ-ORD-052; REQ-RET-048; REQ-NTF-056; REQ-CMS-051 | TESTING-STANDARDS.md; SECURITY-STANDARDS.md; ACCESSIBILITY.md | Engineering, Product, Security, Accessibility |

## 7. Open Product Decisions

`PRODUCT.md` contains exactly 30 Open Product Decisions. The following 29 are materially relevant to Administration, remain in exact source order, and are unresolved by this Draft.

| Product Decision | Administration boundary affected |
| --- | --- |
| Initial product categories and catalogue taxonomy. | Determines which Product- and Category-owned catalogue structures Staff workflows may coordinate; ADM does not define the taxonomy. |
| Size, fit, colour, material, and other Product Variant or Attribute standards. | Determines which governed Product data Staff may maintain; ADM does not define Variant or Attribute standards. |
| Guest checkout versus mandatory account rules. | Constrains support visibility and intervention around Checkout identity context; ADM does not choose the account rule. |
| Customer email-verification requirements. | Constrains which Identity status Staff may inspect or which governed recovery capability may be offered; ADM does not define verification policy. |
| Initial payment methods and provider. | Determines which Payment-owned operational evidence and actions may be surfaced; ADM selects neither methods nor providers. |
| Shipping provider, service levels, delivery areas, and fee policy. | Determines which Shipping configuration and operational outcomes Staff may coordinate; ADM defines none of their values. |
| Free-delivery threshold and promotional treatment. | Constrains Pricing and Shipping views and support explanations; ADM does not calculate or choose the threshold or treatment. |
| Tax-inclusive display and invoice requirements. | Constrains administrative display, investigation, and export of tax and invoice evidence; ADM does not establish tax or invoice policy. |
| Cancellation eligibility and cutoff policy. | Governs whether an administrative cancellation request may be offered to Order; ADM does not decide eligibility or cutoff. |
| Returns, exchanges, and refund policy. | Governs which Return and Payment coordination paths Staff may invoke; ADM does not determine eligibility, disposition, exchange, or Refund policy. |
| Stock Reservation duration. | Affects investigation and recovery views for Inventory-owned Reservations; ADM does not select or extend the duration. |
| Back-order and pre-order support. | Determines which Product, Inventory, Cart, and Order states administrative workflows may encounter; ADM does not enable or define support. |
| Voucher and promotion stacking policy. | Constrains Pricing-owned administrative actions and explanations; ADM does not decide stacking or calculate eligibility. |
| Product-review support. | Determines whether a future owning capability exists for Staff coordination; ADM does not introduce review truth or moderation policy. |
| Wishlist behaviour for guest and registered customers. | Determines whether related support evidence or an owning capability exists; ADM does not create Wishlist behavior or authority. |
| Low-stock and out-of-stock customer messaging. | Constrains which Inventory facts and Notifications or CMS outputs Staff may coordinate; ADM does not define message policy. |
| Customer-support channels and service expectations. | Determines supported intake, escalation, and service context; ADM does not select channels, staffing, or service targets. |
| Marketing-consent and communication-preference model. | Governs which Customer consent and preference evidence Notifications administration may consume; ADM cannot infer or change Consent outside Customer capabilities. |
| Initial analytics provider and event taxonomy. | Determines downstream analytical tooling and taxonomy; ADM supplies governed evidence without choosing either. |
| Initial reporting and export requirements. | Determines approved report definitions and export purposes; ADM does not create Reporting authority or unrestricted extraction. |
| Content approval and scheduled-publication workflow. | Governs which CMS approval and scheduling capabilities administrative workflows may expose; ADM invents no workflow or schedule rule. |
| Administrative role and permission matrix. | Governs which contextual administrative operations Identity assignments may authorize; ADM defines no Role or Permission matrix. |
| Production customer-service and operational escalation process. | Governs escalation ownership, routing, and expectations; ADM records coordination without defining the process. |
| South African tax-display, invoice, and credit-note policy. | Constrains finance support, Order evidence, and exports; ADM neither calculates tax nor establishes invoice or Credit Note truth. |
| Fraud-screening approach and manual-review workflow. | Governs advisory fraud evidence and review coordination; ADM defines neither fraud policy nor Payment success. |
| Product data import, export, and migration requirements. | Governs whether and how bulk catalogue coordination may be exposed; ADM defines no format, mapping, limit, or migration mechanism. |
| Customer data export, correction, deletion, and account-closure workflow. | Governs privacy and support coordination through Customer and Identity capabilities; ADM does not own Customer data outcomes or retention policy. |
| Gift cards, store credit, and promotional credit policy. | Determines whether Pricing or Payment capabilities exist for these instruments; ADM does not introduce balances, redemption, or credit policy. |
| Product launch date, release scope, and post-launch support window. | Constrains operational readiness and support coordination; ADM does not set release scope, dates, or service windows. |

## 8. Risks

| Risk | Control |
| --- | --- |
| Owning-Domain bypass | Require every mutation through the owning capability and reject direct technical manipulation as authority. |
| UI treated as Authorization | Require contextual server-side Authorization regardless of visibility, route, label, Claims, or identifiers. |
| Excessive Staff privilege | Apply current least privilege and Identity-owned access changes without defining a Role matrix. |
| Stale privilege survives change | Re-evaluate trusted security context and deny stale Session, assignment, Claim, or cached-navigation authority. |
| Cross-Customer disclosure | Enforce Principal/Resource isolation, minimization, masking, and enumeration-resistant failures. |
| Sensitive Data leaks through operations | Exclude unnecessary PII, Secrets, credentials, and protected detail from every administrative surface. |
| Bulk action over-applies authority | Authorize and validate each Resource independently and retain per-item outcomes. |
| Bulk partial success is hidden | Keep accepted, rejected, failed, skipped, and unknown effects distinct and reconcilable. |
| Retry duplicates commercial effects | Use owning-Domain duplicate safety and verify prior effects before recovery or replay. |
| Concurrent work loses accepted changes | Detect stale context and conflicts rather than silently overwriting newer Domain state. |
| Direct provider action becomes business truth | Treat provider evidence within the owning Domain and never as an ADM-owned outcome. |
| Approval coordination invents policy | Record only policy-governed requests and evidence; define no chain, count, role, or override. |
| Support note rewrites history | Keep notes non-authoritative, protected, attributable, and separate from Domain history. |
| Fraud advice becomes Payment proof | Keep signals advisory and require Payment-owned validated evidence for financial truth. |
| Repair bypasses current invariants | Require current Authorization, evidence, and owning-Domain validation before repair. |
| Unknown effects shown as success | Retain explicit uncertainty until owning-Domain evidence or reconciliation resolves it. |
| Reporting becomes transactional authority | Label projections as stale-capable and prevent dashboards or exports from mutating state. |
| Export leaks or loses context | Require Authorization, minimization, source definitions, provenance, and proportional audit. |
| Audit is missing for material action | Create attributable proportional records for governed high-Risk and privileged actions. |
| Routine reads are over-audited | Apply risk-proportionate audit rather than automatically classifying ordinary reads as high-Risk. |
| Operational search enumerates Resources | Bound and authorize search while hiding inaccessible Resource existence. |
| Escalation becomes an invented service policy | Require approved escalation governance and define no channel, level, target, or staffing rule. |
| Inaccessible administration blocks operations | Require WCAG 2.2 AA evidence for actions, status, tables, partial outcomes, and recovery. |
| Unbounded query harms operations | Require bounded histories, searches, exports, views, and bulk requests without fixed local limits. |
| Correlation gaps prevent diagnosis | Correlate Principal, Resource, action, Domain, request, outcome, and downstream effects safely. |
| Retention action erases required evidence | Invoke the owner and preserve governed commercial, policy, and Audit history. |
| Configuration becomes unowned truth | Require an identified owning capability and reject ambiguous or unowned configuration changes. |
| Implementation detail becomes ADM policy | Keep providers, schemas, engines, matrices, counts, and transition graphs outside the Domain baseline. |

## 9. Related Documents

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
- `specifications/domains/identity/identity-domain.md`
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

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-07 | Draft | Initial comprehensive Administration Domain Specification. |

## 11. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, scope is `ADM`, and the Draft is neither normative nor repository-wide authority;
2. Administration owns only protected operational coordination and preserves every owning Domain;
3. Staff User, Principal, Authentication, Session, Role, Permission, Claims, Scope, and contextual Authorization remain distinct;
4. every mutation uses an owning-Domain capability and no direct persistence, provider, cache, queue, or projection manipulation becomes business authority;
5. support, approval, bulk, recovery, repair, escalation, export, fraud, Reporting, and Analytics boundaries remain explicit and policy-neutral;
6. exactly 53 Requirements are unique, sequential, gap-free from `REQ-ADM-001` through `REQ-ADM-053`, clause-complete, and implementation-neutral;
7. exactly 53 individually authored Acceptance Criteria rows map one-to-one and introduce no new behavior;
8. exactly 53 semantically direct traceability rows map one-to-one and every cited identifier exists;
9. all 30 Product Decisions were reviewed, the 29 material decisions remain exact, source-ordered, and unresolved;
10. all 28 Risks are material, non-duplicative, and have distinct implementation-neutral controls;
11. all 37 Related Documents exist and are relevant;
12. Revision History contains exactly one `0.1.0 Draft` row;
13. no Glossary amendment is required;
14. Markdown, headings, tables, UTF-8, whitespace, final newline, prohibited markers, and structure pass; and
15. Git scope contains exactly one untracked `specifications/domains/admin/admin-domain.md`, with no tracked, staged, or unrelated changes.
