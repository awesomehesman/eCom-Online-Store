---
title: CMS Backend Specification
version: 0.1.0
status: Draft
owner: Backend
last_updated: 2026-09-23
authoritative: false
scope: BCMS
---

# CMS Backend Specification

## 1. Purpose

This Draft Specification defines implementation-facing backend obligations for the Approved CMS Domain under scope `BCMS`. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the CMS backend scope, remain `authoritative: false`, and remain subordinate to governing sources, Approved Business Requirements, the Approved CMS Domain, materially applicable Shared Backend Baseline (`BEB`) Requirements, Accepted ADR-0015, and applicable repository standards.

BCMS uses the CMS-only decomposition authorized by Accepted ADR-0015 immediately after Approved BRET as the fourteenth downstream Backend Specification after BEB. It consumes governed evidence without acquiring Product, Category, Pricing, Inventory, Identity, Customer, Consent, Preference, Cart, Checkout, Order, Shipping and Fulfilment, Payment, Refund, Return, Search and Discovery, Notifications, Reporting, Administration, or other authority. It resolves no Open Product or Architecture Decision and establishes no post-BCMS roadmap position.

## 2. Requirements

### BCMS-REQ-001 — Lifecycle, Scope, and Authority

BCMS MUST use scope `BCMS`, remain `authoritative: false`, identify its `0.1.0 Draft` lifecycle, remain non-normative while Draft, specialize only the Approved CMS Domain, preserve governing-source precedence, and claim no repository-wide authority.

### BCMS-REQ-002 — Complete CMS Domain Specialization

BCMS MUST specialize every `REQ-CMS-001` through `REQ-CMS-051` without weakening, duplicating, omitting, or transferring the Approved CMS Domain's authority.

### BCMS-REQ-003 — Decomposition and Roadmap Containment

BCMS MUST use a CMS-only decomposition, immediately follow Approved BRET, preserve Notifications as independently eligible, separate, unresolved, unranked, and unordered, and MUST NOT establish or authorize any later Backend Specification identity, title, path, scope code, decomposition, or ordering.

### BCMS-REQ-004 — BEB Inheritance

BCMS MUST inherit and explicitly account for every `BEB-REQ-001` through `BEB-REQ-056`, apply every materially applicable obligation, preserve conditional applicability, and MUST NOT weaken, contradict, or transfer BEB authority.

### BCMS-REQ-005 — Modular and Hexagonal Boundary

BCMS MUST preserve the modular-monolith boundary, hexagonal dependency direction, layer responsibilities, project-owned Ports, and dependency isolation governed by BEB without selecting extraction, framework, provider, or deployment mechanisms.

### BCMS-REQ-006 — Public Contract, Use-Case, and API Boundary

BCMS MUST expose only intentional abstract public Contracts and explicit CMS-owned Use Cases, distinguish commands from queries, validate governed context and bounds, translate failures safely, and preserve applicable versioning, DTO separation, collection, error, description, and compatibility obligations without selecting routes, methods, statuses, or schemas.

### BCMS-REQ-007 — Persistence and Historical Truth

BCMS persistence MUST contain only CMS-owned truth, preserve stable identity, versions, provenance, publication and placement evidence, history, integrity, consistency, atomicity, concurrency safety, and durable workflow state without selecting a schema, table, ORM, index, transaction primitive, or storage mechanism.

### BCMS-REQ-008 — CMS-Owned Truth and External Non-Authority

BCMS MUST own only CMS-specific content, version, publication, placement, history, recovery, and reconciliation truth; publishing, rendering, caching, indexing, or display MUST NOT establish, modify, or repair another Domain's authoritative fact.

### BCMS-REQ-009 — Stable Content Identity and Classification

Each CMS Content record MUST retain stable identity distinct from title, URL, placement, version, rendering, media, and external identifiers, while supported classifications remain distinguishable without creating a closed repository-wide taxonomy.

### BCMS-REQ-010 — Governed Creation and Validation

Content creation MUST return an explicit accepted or rejected outcome, preserve actor and source context, create no implicit publication, and validate CMS-owned completeness, integrity, compatibility, safety, and readiness while treating external references as untrusted.

### BCMS-REQ-011 — Version and Historical Evidence Integrity

Every material revision MUST have distinguishable version identity and interpretable history, and later edits, corrections, withdrawal, or supersession MUST NOT silently rewrite earlier content or publication evidence.

### BCMS-REQ-012 — Draft Isolation and Approval-Readiness Boundary

Draft, incomplete, rejected, withdrawn, or unapproved content MUST remain publicly ineligible, while approval-readiness evidence MAY be recorded without defining an approval chain, approver count, organizational role, Permission matrix, or approval policy.

### BCMS-REQ-013 — Publication Outcome and Eligibility

Publication MUST be an explicit authorized CMS outcome for an eligible version and MUST reject or safely hold work when CMS validation, current Authorization, source evidence, or governed policy is missing, stale, conflicting, or uncertain; storage, rendering, scheduling, caching, or provider acknowledgement alone MUST NOT prove publication.

### BCMS-REQ-014 — Scheduled Intent and Execution Uncertainty

Scheduling MAY record publication intent but MUST NOT guarantee publication or define time, delay, timezone, retry, or scheduler mechanisms, and delayed, duplicate, missed, reordered, or uncertain execution MUST remain distinguishable from success.

### BCMS-REQ-015 — Withdrawal, Correction, Supersession, and Publication History

Withdrawal, correction, and supersession MUST produce explicit attributable outcomes, preserve affected versions, provenance, uncertainty, and prior publication evidence, and retain reconstructable history for success, failure, and uncertainty.

### BCMS-REQ-016 — Product and Product Media Boundary

BCMS MAY reference governed Product, Product Variant, and Product Media evidence but MUST NOT redefine their identity, definition, content, lifecycle, publication, sellability, metadata, rights, accessibility, or association authority.

### BCMS-REQ-017 — Category Boundary

BCMS placements MAY reference Category evidence but MUST NOT establish Category identity, hierarchy, membership, ordering, classification, taxonomy, or navigation authority.

### BCMS-REQ-018 — Commercial Truth Boundary

BCMS MUST NOT calculate or establish Money, Currency, Price, Discount, Promotion, Voucher, Tax, fee, eligibility, stacking, total, or other commercial outcome, and presented claims MUST remain traceable to current authoritative evidence.

### BCMS-REQ-019 — Inventory Boundary

BCMS MUST NOT establish Stock, Stock Reservation, Available-to-Sell, availability, or replenishment truth, and stale availability presentation MUST remain identifiable, non-authoritative, and correctable.

### BCMS-REQ-020 — Policy Presentation Boundary

BCMS MAY version and publish presentation only from governed policy substance and MUST NOT decide legal, tax, shipping, cancellation, Return, Refund, privacy, fraud, marketing-consent, or support policy.

### BCMS-REQ-021 — Privacy, Sensitive Data, and Secret Safety

BCMS MUST NOT own Customer, Account, Address, Consent, or Preference truth, MUST minimize personal data, and MUST prevent prohibited secrets, credentials, tokens, raw Payment data, unnecessary PII, fraud detail, or internal security and provider detail from content, previews, logs, events, exports, diagnostics, or operational evidence.

### BCMS-REQ-022 — Identity, Authorization, Administration, and Preview Isolation

Protected CMS actions MUST require authenticated Principal context and current server-side contextual Authorization, conceal unauthorized or cross-object content, and permit Administration invocation without bypassing CMS invariants or defining Roles, Permissions, approval, escalation, or implementation policy.

### BCMS-REQ-023 — Media Reference Safety

CMS media references MUST preserve source ownership, rights, accessibility, integrity, availability, retention, deletion, and orphan-handling boundaries and fail safely when required evidence is absent without making BCMS a storage or provider authority.

### BCMS-REQ-024 — Safe Rendering and Truthful Claims

BCMS MUST validate or safely render untrusted markup, links, scripts, media, and structured content against injection, deception, unsafe navigation, or fabricated meaning, and MUST prevent unsupported or stale Product, campaign, commercial, policy, environmental, availability, delivery, or service claims.

### BCMS-REQ-025 — Accessible Content

BCMS MUST preserve CMS-owned content semantics and evidence needed for applicable WCAG 2.2 AA outcomes, including meaningful structure, alternatives, understandable language, keyboard use, and non-visual meaning, without selecting rendering mechanisms.

### BCMS-REQ-026 — Storefront, Cache, and CDN Non-Authority

Storefronts MUST consume only eligible CMS outcomes, while rendering, cache, and CDN copies remain non-authoritative, stale-capable, replaceable representations whose purge, propagation, or delivery evidence cannot establish publication and whose divergence remains correctable.

### BCMS-REQ-027 — Search and Discovery Boundary

BCMS MAY expose eligible content through governed Contracts for Search consumption, but indexes, documents, extraction, ranking, query behavior, and results MUST remain non-authoritative and correctable after publication, withdrawal, or supersession.

### BCMS-REQ-028 — Notifications Boundary

BCMS MAY expose explicitly permitted CMS content or completed CMS facts for Notifications consumption but MUST NOT own a Notification request, recipient, template, channel, attempt, provider, Delivery Status, communication eligibility, or delivery truth.

### BCMS-REQ-029 — Reporting and Analytics Boundary

BCMS MAY expose bounded source-defined CMS evidence for reports, dashboards, exports, Analytics Events, and Projections, which MUST remain privacy-aware, stale-capable, non-authoritative, and unable to mutate CMS state.

### BCMS-REQ-030 — Stale and Conflicting Source Handling

Stale, delayed, reordered, superseded, unavailable, or conflicting external facts MUST produce explicit safe CMS outcomes and MUST NOT be silently presented as current authoritative truth.

### BCMS-REQ-031 — Concurrency, Idempotency, Replay, and Duplicate Safety

Concurrent, duplicate, retried, or replayed CMS actions MUST preserve stable effect identity and outcomes and MUST NOT multiply publication effects, lose accepted changes, revive withdrawn content, or overwrite a newer version, without selecting keys, locks, stores, TTLs, or retry counts.

### BCMS-REQ-032 — Explicit Failure and Uncertainty

Validation, save, readiness, schedule, publication, withdrawal, render, cache, index, provider, and dependency failures MUST remain distinguishable from success, classify known and unknown effects, translate safely, and preserve evidence needed for investigation and recovery.

### BCMS-REQ-033 — Controlled Recovery

Recovery, republish, withdrawal, correction, or replay MUST be explicitly authorized, evidence-based, duplicate-safe, and constrained by current CMS and upstream truth without selecting operational procedures or mechanisms.

### BCMS-REQ-034 — Reconciliation

BCMS MUST support accountable reconciliation among content identity, versions, provenance, eligibility, schedules, publication history, presentation copies, and downstream representations while preserving discrepancies and uncertainty until repair.

### BCMS-REQ-035 — Contract Boundary

BCMS Contracts MUST expose only necessary, versioned, bounded semantics, preserve compatibility and source authority, and MUST NOT make transport DTOs, routes, methods, statuses, schemas, classes, providers, or persistence designs authoritative.

### BCMS-REQ-036 — Conditional Events and Messaging

Where governed, Domain or Integration Events MUST represent completed CMS-owned facts, preserve producer authority, correlation, delivery and consumption safety, and MUST NOT select canonical names, payloads, topics, queues, brokers, transports, or introduce external messaging independently.

### BCMS-REQ-037 — Observability and Health

Material CMS operations MUST be bounded, observable, correlated, and diagnosable across content identity, version, actor, source, schedule, publication outcome, dependency, and representation, expose truthful health, and protect data without selecting products or numerical targets.

### BCMS-REQ-038 — Proportional Audit Records

Governed high-Risk CMS actions MUST produce attributable, tamper-resistant, proportional Audit Records distinct from diagnostic logs, while routine low-Risk authoring MUST NOT automatically be elevated or over-audited.

### BCMS-REQ-039 — Placement Eligibility

Placement eligibility MUST be an explicit CMS-owned outcome for an identified eligible version, remain bound to current publication truth, preserve provenance and history, support correction and reconciliation, and MUST NOT define layout, ranking, targeting, personalization, taxonomy, workflow, numerical, or implementation policy.

### BCMS-REQ-040 — Deletion and Retention Boundary

Deletion or retention MUST produce an explicit governed outcome, apply applicable reference and dependency checks, preserve required versions, publication and policy history, Audit Records, and prior public evidence, and fail safely or remain explicit when unresolved without selecting retention periods, legal conclusions, physical mechanisms, storage, archival, provider, or cleanup implementations.

### BCMS-REQ-041 — Complete Verification Coverage

Verification evidence MUST cover every BCMS Requirement, negative paths, authority separation, isolation, concurrency, uncertainty, accessibility, security, recovery, reconciliation, Contracts, events, and downstream non-authority without selecting tooling or numerical coverage targets.

### BCMS-REQ-042 — Transaction and External-Effect Separation

BCMS application Use Cases MUST own applicable transaction boundaries, make consistency and atomicity explicit, and keep external calls and uncertain effects outside unsafe transaction coupling without selecting a transaction or workflow mechanism.

### BCMS-REQ-043 — Provider, Callback, and External Interaction Boundary

Any governed external interaction MUST use project-owned Ports, validate authenticity and integrity where callbacks exist, distinguish provider failure and uncertainty, and support replay-safe recovery and reconciliation without selecting providers, SDKs, protocols, adapters, webhooks, or infrastructure.

### BCMS-REQ-044 — Backend Security and Data Protection

BCMS MUST apply least privilege, input and evidence validation, object isolation, safe errors, logging protection, Secret isolation, and dependency-boundary controls and MUST NOT expose protected content, Customer, commercial, security, or operational data through unauthorized Contracts or evidence.

### BCMS-REQ-045 — Configuration and Feature-Flag Safety

BCMS configuration and any future feature-controlled state MUST be validated, protected, observable, safely defaulted, compatible with reachable content states, and non-authoritative without selecting a store, flag provider, rollout mechanism, or lifecycle policy.

### BCMS-REQ-046 — Migration, Deployment Compatibility, and Bounded Work

BCMS data and Contract evolution MUST preserve CMS history, authority, rollback or forward-recovery safety, mixed-version compatibility, bounded resource use, and failure containment without selecting migration tooling, deployment topology, schema strategy, pagination values, or operational limits.

### BCMS-REQ-047 — Backend Verification Layers

BCMS verification MUST cover Domain and application behavior, Adapters and integrations, Contracts and compatibility, architecture rules, security, operations, failure, recovery, and traceability in proportion to Risk without selecting tools or numerical targets.

### BCMS-REQ-048 — Policy and Implementation Neutrality

BCMS MUST preserve all Open Product and Architecture Decisions, remain reversible where decisions are unresolved, and MUST NOT select concrete policy, API, DTO, persistence, event, provider, cache, infrastructure, topology, operational, numerical, or Role/Permission mechanisms.

### BCMS-REQ-049 — Downstream Dependency Containment

During BCMS Draft, Reporting MUST remain blocked by a missing Approved CMS source Contract and Administration MUST remain blocked by missing Approved CMS, Notifications, and Reporting invocation Contracts; Notifications MUST remain independently eligible, separate, unresolved, unranked, and unordered, and no future BCMS approval effect may be represented as already closed.

### BCMS-REQ-050 — Governance Integrity and Traceability

BCMS MUST maintain complete Requirement-to-Acceptance-Criterion traceability, CMS Domain coverage, BEB accounting, open-decision inventories, authority boundaries, pending governance reviews, Related Documents, Revision History, and lifecycle-correct final validation.

## 3. Canonical Inputs and Authority Boundaries

BCMS is governed by `AGENTS.md`, `PRODUCT.md`, `ARCHITECTURE.md`, `DECISIONS.md`, applicable core and backend standards, Accepted ADR-0015, Approved BEB, and the Approved CMS Domain. Approved upstream Backend Specifications are consumable only through materially applicable bounded Contracts or evidence. Frontend or downstream representations are consumers, not CMS authority.

| Boundary | BCMS may consume or expose | Authority retained externally |
| --- | --- | --- |
| BIDN / BCUS | Principal, Authentication, Customer, Account, Consent, Preference, contact evidence | Identity, credentials, Sessions, Customer, Account, Consent, Preference |
| BPRD / BCAT | Product, Product Variant, Product Media, Category and taxonomy references | Catalogue identity, lifecycle, media metadata, hierarchy, membership, classification |
| BINV / BPRC | Availability and commercial evidence for truthful presentation | Stock, Reservation, Price, Discount, Promotion, Voucher, Tax, fee, calculation |
| BCART / BCHK / BORD / BSHP / BPAY / BRET | Governed commerce references and policy-source evidence | Every owning commerce Domain's identity, lifecycle, effects, and truth |
| BSRCH | Governed eligible CMS source content | Index, extraction, ranking, query behavior and Search truth |
| Notifications | Permitted content and completed CMS facts | Communication eligibility, recipient, channel, attempt, provider and delivery |
| Reporting | Bounded CMS source evidence | Reports, analytics, exports, Projections and interpretation |
| Administration | Protected invocation of explicit CMS capabilities | Administration workflows, organizational Roles, Permissions and escalation policy |

## 4. Requirement Traceability

| Requirement | CMS Domain source | BEB / governing source | Acceptance Criterion |
| --- | --- | --- | --- |
| BCMS-REQ-001 | REQ-CMS-001 | BEB-REQ-001–004; ADR-0015 | BCMS-AC-001 |
| BCMS-REQ-002 | REQ-CMS-001–051 | BEB-REQ-002–004, 055 | BCMS-AC-002 |
| BCMS-REQ-003 | REQ-CMS-001, 003 | ADR-0015; ARCHITECTURE.md §35 | BCMS-AC-003 |
| BCMS-REQ-004 | REQ-CMS-051 | BEB-REQ-001–056 | BCMS-AC-004 |
| BCMS-REQ-005 | REQ-CMS-003–004 | BEB-REQ-005–007 | BCMS-AC-005 |
| BCMS-REQ-006 | REQ-CMS-007–008, 045 | BEB-REQ-008–018, 041 | BCMS-AC-006 |
| BCMS-REQ-007 | REQ-CMS-005, 009–010, 019, 041, 044, 050 | BEB-REQ-021–028 | BCMS-AC-007 |
| BCMS-REQ-008 | REQ-CMS-002–004 | BEB-REQ-003–004, 021 | BCMS-AC-008 |
| BCMS-REQ-009 | REQ-CMS-005–006 | BEB-REQ-021, 023 | BCMS-AC-009 |
| BCMS-REQ-010 | REQ-CMS-007–008 | BEB-REQ-009–010, 015, 041 | BCMS-AC-010 |
| BCMS-REQ-011 | REQ-CMS-009–010 | BEB-REQ-023–024 | BCMS-AC-011 |
| BCMS-REQ-012 | REQ-CMS-011–012 | BEB-REQ-011, 019–020 | BCMS-AC-012 |
| BCMS-REQ-013 | REQ-CMS-013–014 | BEB-REQ-009–012, 025 | BCMS-AC-013 |
| BCMS-REQ-014 | REQ-CMS-015–016 | BEB-REQ-028–031, 041–042 | BCMS-AC-014 |
| BCMS-REQ-015 | REQ-CMS-017–019 | BEB-REQ-024–031, 041–042 | BCMS-AC-015 |
| BCMS-REQ-016 | REQ-CMS-020–021 | BEB-REQ-003–004, 021 | BCMS-AC-016 |
| BCMS-REQ-017 | REQ-CMS-022 | BEB-REQ-003–004, 021 | BCMS-AC-017 |
| BCMS-REQ-018 | REQ-CMS-023 | BEB-REQ-003–004, 021 | BCMS-AC-018 |
| BCMS-REQ-019 | REQ-CMS-024 | BEB-REQ-003–004, 021 | BCMS-AC-019 |
| BCMS-REQ-020 | REQ-CMS-025 | BEB-REQ-003–004, 056 | BCMS-AC-020 |
| BCMS-REQ-021 | REQ-CMS-026, 033 | BEB-REQ-043–044 | BCMS-AC-021 |
| BCMS-REQ-022 | REQ-CMS-027–029 | BEB-REQ-011, 019–020, 043 | BCMS-AC-022 |
| BCMS-REQ-023 | REQ-CMS-030 | BEB-REQ-032–034, 043 | BCMS-AC-023 |
| BCMS-REQ-024 | REQ-CMS-031–032 | BEB-REQ-015, 041, 043 | BCMS-AC-024 |
| BCMS-REQ-025 | REQ-CMS-034 | BEB-REQ-003, 051, 055 | BCMS-AC-025 |
| BCMS-REQ-026 | REQ-CMS-035–036 | BEB-REQ-032–034, 041–042, 051 | BCMS-AC-026 |
| BCMS-REQ-027 | REQ-CMS-037 | BEB-REQ-003–004, 037–040 | BCMS-AC-027 |
| BCMS-REQ-028 | REQ-CMS-038 | BEB-REQ-003–004, 037–040 | BCMS-AC-028 |
| BCMS-REQ-029 | REQ-CMS-039 | BEB-REQ-003–004, 021, 037–040, 043 | BCMS-AC-029 |
| BCMS-REQ-030 | REQ-CMS-040 | BEB-REQ-041–042 | BCMS-AC-030 |
| BCMS-REQ-031 | REQ-CMS-041 | BEB-REQ-027, 029–031 | BCMS-AC-031 |
| BCMS-REQ-032 | REQ-CMS-042 | BEB-REQ-033–034, 041–042 | BCMS-AC-032 |
| BCMS-REQ-033 | REQ-CMS-043 | BEB-REQ-031, 034, 036, 042 | BCMS-AC-033 |
| BCMS-REQ-034 | REQ-CMS-044 | BEB-REQ-034, 042 | BCMS-AC-034 |
| BCMS-REQ-035 | REQ-CMS-045 | BEB-REQ-008, 013–018, 050 | BCMS-AC-035 |
| BCMS-REQ-036 | REQ-CMS-046 | BEB-REQ-037–040 | BCMS-AC-036 |
| BCMS-REQ-037 | REQ-CMS-047 | BEB-REQ-046, 051 | BCMS-AC-037 |
| BCMS-REQ-038 | REQ-CMS-048 | BEB-REQ-045–046 | BCMS-AC-038 |
| BCMS-REQ-039 | REQ-CMS-049 | BEB-REQ-021, 024–031, 041–042 | BCMS-AC-039 |
| BCMS-REQ-040 | REQ-CMS-050 | BEB-REQ-021, 024, 042–043, 049 | BCMS-AC-040 |
| BCMS-REQ-041 | REQ-CMS-051 | BEB-REQ-052–055 | BCMS-AC-041 |
| BCMS-REQ-042 | REQ-CMS-007, 013–19, 041–044 | BEB-REQ-012, 025–026 | BCMS-AC-042 |
| BCMS-REQ-043 | REQ-CMS-030, 036, 042–044 | BEB-REQ-032–036 | BCMS-AC-043 |
| BCMS-REQ-044 | REQ-CMS-008, 026–033, 048, 050 | BEB-REQ-043–044 | BCMS-AC-044 |
| BCMS-REQ-045 | REQ-CMS-040–044 | BEB-REQ-047–048 | BCMS-AC-045 |
| BCMS-REQ-046 | REQ-CMS-009–010, 019, 044–045, 050 | BEB-REQ-049–051 | BCMS-AC-046 |
| BCMS-REQ-047 | REQ-CMS-051 | BEB-REQ-052–055 | BCMS-AC-047 |
| BCMS-REQ-048 | REQ-CMS-003, 012, 015, 025, 030, 036–038, 045–046, 049–050 | BEB-REQ-056; PRODUCT.md §24; ARCHITECTURE.md §34 | BCMS-AC-048 |
| BCMS-REQ-049 | REQ-CMS-003, 028, 038–039 | ADR-0015; ARCHITECTURE.md §35; Approved BRET | BCMS-AC-049 |
| BCMS-REQ-050 | REQ-CMS-051 | AGENTS.md; DOCUMENTATION-STANDARDS.md; ADR-0015 | BCMS-AC-050 |

## 5. CMS Domain Requirement Coverage

| CMS Domain Requirement(s) | BCMS specialization |
| --- | --- |
| REQ-CMS-001–004 | BCMS-REQ-001–005, 008 — lifecycle, CMS authority, and external non-authority. |
| REQ-CMS-005–006 | BCMS-REQ-007, 009 — stable identity, persistence meaning, and bounded classification. |
| REQ-CMS-007–008 | BCMS-REQ-006, 010, 042 — creation, validation, Use Cases, and transactions. |
| REQ-CMS-009–010 | BCMS-REQ-007, 011, 046 — versions, history, and evolution safety. |
| REQ-CMS-011–012 | BCMS-REQ-012, 022, 048 — isolation, readiness, Authorization, and open workflow policy. |
| REQ-CMS-013–016 | BCMS-REQ-013–014, 031–034, 042 — publication, scheduling, uncertainty, and recovery. |
| REQ-CMS-017–019 | BCMS-REQ-007, 011, 015, 038 — withdrawal, correction, supersession, and history. |
| REQ-CMS-020–025 | BCMS-REQ-016–020 — Product, media, Category, commercial, Inventory, and policy boundaries. |
| REQ-CMS-026–029 | BCMS-REQ-021–022, 044 — privacy, Identity, Authorization, Administration, and preview isolation. |
| REQ-CMS-030–034 | BCMS-REQ-021, 023–025, 043–044 — media, rendering, claims, security, and accessibility. |
| REQ-CMS-035–039 | BCMS-REQ-026–029, 036, 049 — storefront, copies, Search, Notifications, Reporting, and downstream containment. |
| REQ-CMS-040–044 | BCMS-REQ-030–034, 042–046 — stale sources, concurrency, failures, recovery, and reconciliation. |
| REQ-CMS-045–048 | BCMS-REQ-006, 035–038, 043–047 — Contracts, events, observability, audit, compatibility, and verification. |
| REQ-CMS-049–050 | BCMS-REQ-039–040 — placement eligibility and deletion/retention boundaries. |
| REQ-CMS-051 | BCMS-REQ-002, 041, 047, 050 — complete verification and governance traceability. |

All 51 Approved CMS Domain Requirements are accounted for without transferring Domain authority.

## 6. BEB Applicability and Inheritance Matrix

| BEB Requirement(s) | Classification | BCMS application / rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, non-authority, and specialization. BCMS-REQ-001–004, 008. |
| BEB-REQ-005–007 | Applicable | Modular, hexagonal, and layer boundaries. BCMS-REQ-005. |
| BEB-REQ-008–012 | Applicable | Public Contracts, Use Cases, command/query, Authorization, and transactions. BCMS-REQ-006, 010, 013, 022, 042. |
| BEB-REQ-013–018 | Conditionally applicable | Apply wherever an external API or collection Contract exists; no route or DTO is selected. BCMS-REQ-006, 035, 046. |
| BEB-REQ-019–020 | Applicable | Authentication context, contextual Authorization, and concealment. BCMS-REQ-012, 022, 044. |
| BEB-REQ-021–024 | Applicable | CMS data ownership, mapping isolation, integrity, and historical truth. BCMS-REQ-007–011, 015, 039–040. |
| BEB-REQ-025–028 | Applicable | Consistency, transactions, external-effect separation, concurrency, and durable workflow evidence. BCMS-REQ-007, 013–15, 031, 039, 042. |
| BEB-REQ-029–031 | Applicable where mutation or consumption can duplicate effects | Idempotency, effect identity, retry, replay, and duplicate safety. BCMS-REQ-014–015, 031, 033, 039. |
| BEB-REQ-032–034 | Conditionally applicable | Apply to any media, cache/CDN, search, or other provider Adapter without selecting one. BCMS-REQ-023, 026, 032–034, 043. |
| BEB-REQ-035–036 | Conditionally applicable | Apply only if a governed Callback or webhook Contract exists; none is mandated. BCMS-REQ-043. |
| BEB-REQ-037–039 | Conditionally applicable | Apply whenever governed Domain or Integration Events are produced or consumed. BCMS-REQ-027–029, 036. |
| BEB-REQ-040 | Applicable | External messaging cannot be introduced independently. BCMS-REQ-036, 048. |
| BEB-REQ-041–042 | Applicable | Explicit errors, recovery, and reconciliation. BCMS-REQ-006, 014–015, 024, 030, 032–034, 039–040. |
| BEB-REQ-043–044 | Applicable | Security, data, Secret, and Payment-data safety. BCMS-REQ-021–024, 044. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records, observability, and health. BCMS-REQ-037–038. |
| BEB-REQ-047–048 | Applicable | Safe configuration and conditional feature controls without selecting mechanisms. BCMS-REQ-045, 048. |
| BEB-REQ-049–051 | Applicable | Migration safety, mixed-version compatibility, bounded work, and failure containment. BCMS-REQ-040, 046. |
| BEB-REQ-052–055 | Applicable | Domain, application, Adapter, integration, architecture, operational, and traceable verification. BCMS-REQ-041, 047, 050. |
| BEB-REQ-056 | Applicable | Open-decision and implementation neutrality. BCMS-REQ-020, 048. |

All 56 BEB Requirements are accounted for. Conditional applicability preserves the inherited obligation whenever the governed interaction exists; it does not waive it.

## 7. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BCMS-AC-001 | BCMS-REQ-001 | Metadata and scope review show `0.1.0 Draft`, `authoritative: false`, scope `BCMS`, non-normative Draft status, bounded authority, and no repository-wide claim. |
| BCMS-AC-002 | BCMS-REQ-002 | The coverage matrix accounts for every `REQ-CMS-001` through `REQ-CMS-051` without omission or authority transfer. |
| BCMS-AC-003 | BCMS-REQ-003 | BCMS is CMS-only, follows BRET, preserves Notifications status, and establishes no later identity or position. |
| BCMS-AC-004 | BCMS-REQ-004 | The BEB matrix accounts for `BEB-REQ-001` through `BEB-REQ-056` with defensible applicability and BCMS traces. |
| BCMS-AC-005 | BCMS-REQ-005 | Architecture evidence preserves module, layer, Port, and inward dependency boundaries without mechanism selection. |
| BCMS-AC-006 | BCMS-REQ-006 | Contracts and Use Cases are intentional, bounded, validated, failure-safe, compatible, and free of concrete transport or framework authority. |
| BCMS-AC-007 | BCMS-REQ-007 | Persistence evidence contains only CMS truth and preserves identity, versions, integrity, history, consistency, and uncertainty without concrete storage design. |
| BCMS-AC-008 | BCMS-REQ-008 | CMS operations cannot establish, change, or repair any listed external authoritative fact. |
| BCMS-AC-009 | BCMS-REQ-009 | Content identity survives changes to labels and references, and classifications remain bounded rather than repository-wide taxonomy. |
| BCMS-AC-010 | BCMS-REQ-010 | Creation and validation yield explicit safe outcomes, preserve actor/source context, and create no publication implicitly. |
| BCMS-AC-011 | BCMS-REQ-011 | Material revisions remain distinguishable and earlier content and publication evidence remain interpretable after later changes. |
| BCMS-AC-012 | BCMS-REQ-012 | Ineligible content is unavailable publicly and readiness evidence defines no approval chain or Role/Permission policy. |
| BCMS-AC-013 | BCMS-REQ-013 | Only an eligible, authorized version produces publication; missing or uncertain evidence safely blocks it. |
| BCMS-AC-014 | BCMS-REQ-014 | Schedule intent never proves publication and delayed, duplicate, missed, reordered, and uncertain execution remain explicit. |
| BCMS-AC-015 | BCMS-REQ-015 | Withdrawal, correction, and supersession preserve explicit outcomes, affected versions, provenance, uncertainty, and reconstructable history. |
| BCMS-AC-016 | BCMS-REQ-016 | Product and media references remain governed externally and cannot be redefined by CMS. |
| BCMS-AC-017 | BCMS-REQ-017 | Category references cannot create or alter taxonomy, hierarchy, membership, ordering, classification, or navigation. |
| BCMS-AC-018 | BCMS-REQ-018 | Presented commercial claims trace to authoritative evidence and BCMS performs no commercial calculation. |
| BCMS-AC-019 | BCMS-REQ-019 | Availability presentation remains traceable, stale-capable, correctable, and unable to establish Inventory truth. |
| BCMS-AC-020 | BCMS-REQ-020 | Policy presentation traces to governed substance and selects no legal, commercial, operational, or Product policy. |
| BCMS-AC-021 | BCMS-REQ-021 | Personal and sensitive data are minimized and prohibited secrets, credentials, Payment data, and internal detail are absent from every representation. |
| BCMS-AC-022 | BCMS-REQ-022 | Protected actions require authenticated context and current contextual Authorization; unauthorized preview is concealed and Administration gains no CMS authority. |
| BCMS-AC-023 | BCMS-REQ-023 | Missing media ownership, rights, accessibility, integrity, lifecycle, deletion, retention, or orphan evidence fails safely without provider authority transfer. |
| BCMS-AC-024 | BCMS-REQ-024 | Unsafe content and unsupported claims are rejected or rendered safely and stale claims remain correctable. |
| BCMS-AC-025 | BCMS-REQ-025 | Applicable content preserves verifiable WCAG 2.2 AA semantics without prescribing a rendering implementation. |
| BCMS-AC-026 | BCMS-REQ-026 | Only eligible outcomes reach storefronts and render, cache, or CDN state cannot establish publication or external truth. |
| BCMS-AC-027 | BCMS-REQ-027 | Search consumes bounded eligible content while indexes and results remain external, non-authoritative, and reconcilable. |
| BCMS-AC-028 | BCMS-REQ-028 | Notifications may consume permitted content or facts without CMS owning communication or delivery truth. |
| BCMS-AC-029 | BCMS-REQ-029 | Reporting and analytics consume bounded source evidence without mutating CMS or becoming authoritative. |
| BCMS-AC-030 | BCMS-REQ-030 | Every stale, unavailable, reordered, superseded, or conflicting source condition yields an explicit safe outcome. |
| BCMS-AC-031 | BCMS-REQ-031 | Concurrent, duplicate, retried, and replayed actions preserve accepted state and never multiply publication effects. |
| BCMS-AC-032 | BCMS-REQ-032 | Failures and unknown outcomes are distinguishable from success and retain diagnostic and recovery evidence. |
| BCMS-AC-033 | BCMS-REQ-033 | Recovery is denied without current Authorization and evidence and otherwise remains bounded and duplicate-safe. |
| BCMS-AC-034 | BCMS-REQ-034 | Reconciliation correlates all listed CMS evidence and keeps discrepancies visible until accountable repair. |
| BCMS-AC-035 | BCMS-REQ-035 | Contract review shows necessary versioned semantics, compatibility, and source authority with no concrete interface or persistence design. |
| BCMS-AC-036 | BCMS-REQ-036 | Any event represents a completed CMS fact with producer authority and safe delivery semantics while selecting no messaging mechanism. |
| BCMS-AC-037 | BCMS-REQ-037 | Diagnostic evidence is correlated, bounded, truthful, protected, and useful without products or numerical targets. |
| BCMS-AC-038 | BCMS-REQ-038 | High-Risk actions produce attributable proportional Audit Records distinct from logs and routine authoring is not over-audited. |
| BCMS-AC-039 | BCMS-REQ-039 | Placement eligibility binds an eligible version to current publication truth, provenance, history, and correction without invented policy. |
| BCMS-AC-040 | BCMS-REQ-040 | Deletion and retention outcomes preserve required evidence, fail safely when unresolved, and select no period or mechanism. |
| BCMS-AC-041 | BCMS-REQ-041 | Verification covers every stated functional, negative, boundary, security, accessibility, resilience, Contract, and event obligation. |
| BCMS-AC-042 | BCMS-REQ-042 | Transaction review shows explicit atomic boundaries and safe external-effect separation without a selected mechanism. |
| BCMS-AC-043 | BCMS-REQ-043 | External interactions use owned Ports and preserve authenticity, uncertainty, recovery, and reconciliation without provider selection. |
| BCMS-AC-044 | BCMS-REQ-044 | Security verification demonstrates least privilege, isolation, safe errors, protected evidence, and no unauthorized disclosure. |
| BCMS-AC-045 | BCMS-REQ-045 | Configuration and possible feature-controlled states are safe, compatible, observable, and non-authoritative without a selected system. |
| BCMS-AC-046 | BCMS-REQ-046 | Evolution evidence preserves history, compatibility, recovery, bounded work, and containment without concrete migration or deployment design. |
| BCMS-AC-047 | BCMS-REQ-047 | Layered tests cover Domain through operations and retain one-to-one traceability without mandated tools or targets. |
| BCMS-AC-048 | BCMS-REQ-048 | Review finds no resolved Open Decision or selected API, schema, event, provider, cache, infrastructure, numerical, or Role/Permission mechanism. |
| BCMS-AC-049 | BCMS-REQ-049 | Draft BCMS closes no downstream dependency; Reporting, Administration, and Notifications retain exactly their governed states and no later position. |
| BCMS-AC-050 | BCMS-REQ-050 | Counts, matrices, decisions, reviews, documents, history, and final validation are complete, consistent, and lifecycle-correct. |

## 8. Open Product Decisions

The following **19 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved CMS Domain remain unresolved:

| Item | Open Product Decision | BCMS preservation |
| ---: | --- | --- |
| 1 | Final brand name and visual identity. | No identity or visual rule selected. |
| 2 | Initial product categories and catalogue taxonomy. | No taxonomy policy selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No shipping policy selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or treatment selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax or invoice policy selected. |
| 10 | Cancellation eligibility and cutoff policy. | No eligibility or cutoff selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, exchange, or Refund policy selected. |
| 14 | Voucher and promotion stacking policy. | No stacking or precedence selected. |
| 15 | Product-review support. | No review or moderation model selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No threshold or message policy selected. |
| 18 | Customer-support channels and service expectations. | No channel or target selected. |
| 19 | Marketing-consent and communication-preference model. | No Consent or Preference policy selected. |
| 20 | Initial analytics provider and event taxonomy. | No provider or taxonomy selected. |
| 21 | Initial reporting and export requirements. | No report or export policy selected. |
| 22 | Content approval and scheduled-publication workflow. | No workflow or schedule rule selected. |
| 23 | Administrative role and permission matrix. | No Role/Permission matrix selected. |
| 24 | Production customer-service and operational escalation process. | No escalation process selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No legal or document policy selected. |
| 30 | Product launch date, release scope, and post-launch support window. | No date, scope, or window selected. |

## 9. Open Architecture Decisions

The following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 remain unresolved:

| Item | Open Architecture Decision | BCMS preservation |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC mechanism selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token strategy selected. |
| 7 | Transactional notification provider selection. | No notification provider selected. |
| 8 | Redis introduction and its approved use cases. | No Redis or cache use selected. |
| 9 | External messaging introduction and service selection. | No messaging service selected. |
| 10 | Initial search implementation details and extraction thresholds. | No Search mechanism or threshold selected. |
| 11 | Product-media upload and transformation strategy. | No media mechanism selected. |
| 12 | Backup retention and production recovery objectives. | No period, mechanism, or objective selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No feature-flag mechanism selected. |

## 10. Explicit Non-Decisions

BCMS selects no content schema or closed taxonomy; approval Role, workflow, or schedule rule; Product-review policy; media, Search, Notification, reporting, escalation, import, migration, storage, publication, or cache mechanism; API route, HTTP detail, DTO, payload, database schema, table, column, index, ORM mapping, identifier format, event name or payload, topic, queue, broker, provider, Redis use, infrastructure product, topology, retry count, timeout, TTL, retention period, rate limit, SLA, SLO, recovery objective, Role/Permission matrix, or unresolved Product policy.

## 11. Dependency and Roadmap Containment

Draft BCMS closes no downstream dependency. Reporting remains blocked by the missing Approved CMS source Contract; Approved BRET already closed its Return source prerequisite. Administration remains blocked by missing Approved CMS, Notifications, and Reporting invocation Contracts; Approved BRET already closed its Return invocation prerequisite. Notifications remains independently eligible, separate, unresolved, unranked, and unordered and has no CMS prerequisite. Future Approved BCMS may close the CMS-side Reporting and Administration prerequisites, but this Draft neither closes them nor authorizes or positions any downstream capability. Every post-BCMS position remains unresolved.

## 12. Risks and Controls

| Risk | Control |
| --- | --- |
| CMS content becomes external truth | Preserve source provenance, bounded references, and explicit non-authority. |
| Ineligible content becomes public | Require explicit eligibility, Authorization, publication outcomes, and isolation. |
| Unsafe or deceptive content is rendered | Validate untrusted content and preserve truthful, accessible semantics. |
| History is rewritten or lost | Preserve stable identity, versions, publication evidence, and attributable history. |
| Duplicate or concurrent work corrupts outcomes | Require stable effect semantics, concurrency safety, idempotency, and reconciliation. |
| Sensitive data leaks | Minimize data and protect every Contract, representation, log, event, and audit surface. |
| Copies become authoritative | Treat storefront, cache, CDN, Search, Reporting, and analytics representations as stale-capable. |
| Draft implies downstream authorization | Preserve dependency and roadmap containment explicitly. |

## 13. Required Governance Reviews

Before promotion, pending review MUST include Architecture and CMS ownership; affected Product, Category, Pricing, Inventory, Identity, Customer, Search and Discovery, Notifications, Reporting, Administration, and commerce ownership where Contracts intersect; Security; Accessibility; Testing; and Documentation. Review must confirm all 51 CMS Domain Requirements and 56 BEB Requirements are accounted for, one-to-one Requirement/AC/traceability, authority preservation, exact open-decision inventories, implementation neutrality, and post-BCMS containment. This Draft records no completed approval review, reviewer identity, signature, ticket, or external artifact.

## 14. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0015-post-return-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/domains/cms/cms-domain.md`

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-23 | Draft | Established the initial CMS-only BCMS backend Specification authorized by Accepted ADR-0015, specializing the Approved CMS Domain and materially applicable BEB Requirements while preserving external authority, unresolved decisions, implementation neutrality, and post-BCMS roadmap containment. |

## 16. Final Validation

Before approval-readiness review, verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, owner is `Backend`, scope is `BCMS`, and the Draft is non-normative;
2. the decomposition is CMS-only, BCMS immediately follows Approved BRET, and all authority boundaries remain intact;
3. all 50 BCMS Requirements and 50 corresponding Acceptance Criteria are unique and contiguous;
4. all 50 traceability rows map each Requirement to exactly one Acceptance Criterion and governed sources;
5. all 51 CMS Domain Requirements and all 56 BEB Requirements are explicitly accounted for;
6. all 19 Product and 11 Architecture Decisions remain unresolved;
7. CMS identity, versions, publication, withdrawal, placement, history, recovery, reconciliation, accessibility, security, failure, Contract, event, observability, audit, and verification obligations are covered;
8. no external Domain authority, concrete implementation mechanism, Role/Permission matrix, numerical target, or unresolved policy is introduced;
9. Draft BCMS closes no downstream dependency, Notifications remains independently eligible and unordered, Reporting and Administration remain unresolved, and every post-BCMS roadmap position remains unresolved;
10. required governance reviews remain pending and Revision History contains only the `0.1.0 Draft` entry;
11. Related Documents exist and terminology remains consistent with governing sources; and
12. the final change creates only `specifications/backend/cms/cms-backend.md`, passes whitespace validation, and remains unstaged, uncommitted, and unpushed.
