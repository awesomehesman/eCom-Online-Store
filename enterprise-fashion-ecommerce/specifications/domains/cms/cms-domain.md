---
title: CMS Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-06
authoritative: false
---

# CMS Domain

## 1. Purpose

This Specification defines implementation-neutral Requirements for CMS-owned content, versions, publication evidence, presentation eligibility, history, recovery, and reconciliation.

This document uses scope code `CMS`. Its Approved Requirements are normative only within the CMS Domain scope and are not repository-wide authority. They remain subordinate to higher-authority governing sources, preserve every Approved Domain’s authority, and resolve no Open Product Decision.

## 2. Scope and Authority

### REQ-CMS-001 — Lifecycle, Authority, and Scope

CMS MUST govern only CMS-owned truth under scope `CMS`, preserve governing-source precedence and Approved Domain authority, and MUST treat this Approved Specification as normative only within the CMS Domain scope and not as repository-wide authority.

### REQ-CMS-002 — CMS-Owned Truth

CMS MUST own CMS Content identity, CMS-specific records, editorial pages, homepage content, banners, campaigns, policy presentation, content validation outcomes, versions, publication-related outcomes, placement eligibility, provenance, history, recovery, and reconciliation.

### REQ-CMS-003 — Cross-Domain Non-Authority

CMS MUST NOT own Product, Product Variant, Product publication, Product Media metadata, Category, Pricing, Inventory, Customer, Identity, Cart, Checkout, Payment, Order, Shipping, Return, Notifications delivery, Product-policy substance, Administration policy, Reporting, Analytics, search-index, media-provider, cache, CDN, API, schema, persistence, or scheduler truth.

### REQ-CMS-004 — External Truth Separation

Publishing, previewing, rendering, caching, indexing, or displaying CMS Content MUST NOT establish, modify, or repair an authoritative fact owned by another Domain.

### REQ-CMS-005 — Stable CMS Content Identity

Each CMS Content record MUST have stable identity that remains distinct from its title, URL, placement, version, rendering, media reference, and external Domain identifiers.

### REQ-CMS-006 — Content Classification

CMS Content classification MUST distinguish supported editorial pages, homepage content, banners, campaigns, and policy presentation without inventing a closed repository-wide taxonomy.

### REQ-CMS-007 — Governed Creation

CMS Content creation MUST produce an explicit accepted or rejected outcome, preserve actor and source context, and create no published state implicitly.

### REQ-CMS-008 — Content Validation

CMS MUST validate required CMS-owned completeness, integrity, compatibility, safety, and publication-readiness inputs while treating external facts as untrusted references.

### REQ-CMS-009 — Version Identity and History

Every material CMS Content revision MUST preserve distinguishable version identity and history sufficient to explain what content existed and when.

### REQ-CMS-010 — Historical Evidence Integrity

Historical CMS versions and publication evidence MUST remain interpretable and MUST NOT be silently rewritten by later edits, corrections, withdrawal, or supersession.

### REQ-CMS-011 — Draft Isolation

Draft, incomplete, rejected, withdrawn, or otherwise unapproved CMS Content MUST NOT become publicly visible or eligible merely because it exists, renders, or is referenced.

### REQ-CMS-012 — Approval-Readiness Boundary

CMS MAY record approval-readiness evidence but MUST NOT invent an approval chain, approver count, organizational role, Permission matrix, or content-approval policy.

### REQ-CMS-013 — Publication Outcome

Publication MUST be an explicit authorized CMS outcome for an eligible version; storage, rendering, scheduling, caching, or provider acknowledgement alone MUST NOT prove publication.

### REQ-CMS-014 — Publication Eligibility

CMS MUST reject or safely hold publication when CMS-owned validation, current authorization, required source evidence, or applicable governed policy is missing, stale, conflicting, or uncertain.

### REQ-CMS-015 — Scheduled-Publication Intent

A schedule MAY express CMS-owned publication intent, but MUST NOT guarantee execution or define a publication time, delay, scheduler, timezone policy, retry count, or infrastructure mechanism.

### REQ-CMS-016 — Scheduled Execution Uncertainty

Delayed, duplicated, missed, reordered, or uncertain scheduled execution MUST remain distinguishable from successful publication and support safe investigation and recovery.

### REQ-CMS-017 — Withdrawal

Withdrawal MUST produce an explicit CMS-owned outcome and history without deleting prior publication evidence or changing the truth of external facts previously presented.

### REQ-CMS-018 — Correction and Supersession

CMS correction and supersession MUST preserve affected versions, provenance, public-state uncertainty, and accountable history without rewriting prior evidence.

### REQ-CMS-019 — Publication History

CMS MUST retain attributable history for material publication, withdrawal, correction, supersession, and failed or uncertain publication outcomes.

### REQ-CMS-020 — Product Boundary

CMS MAY reference Product and Product Variant context but MUST preserve Product-owned identity, definition, content, lifecycle, publication, and sellability authority.

### REQ-CMS-021 — Product Media Boundary

CMS MAY reference governed Product Media but MUST NOT redefine Product Media metadata, rights evidence, publication eligibility, accessibility meaning, lifecycle, or Product association.

### REQ-CMS-022 — Category Boundary

CMS placements MAY reference Category context but MUST NOT establish Category identity, hierarchy, membership, ordering, classification, or navigation authority.

### REQ-CMS-023 — Commercial Truth Boundary

CMS MUST NOT calculate or establish Price, Money, Currency, Discount, Promotion, Voucher, tax, fee, eligibility, stacking, totals, or commercial outcomes; claims MUST derive from current authoritative evidence.

### REQ-CMS-024 — Inventory Boundary

CMS MUST NOT establish Stock, Stock Reservation, Available-to-Sell, availability, or replenishment truth, and stale availability presentation MUST remain non-authoritative and correctable.

### REQ-CMS-025 — Policy Presentation Boundary

CMS MAY version and publish policy presentation only from governed policy substance and MUST NOT decide legal, tax, shipping, cancellation, Return, Refund, privacy, fraud, marketing-consent, or support policy.

### REQ-CMS-026 — Customer and Privacy Boundary

CMS MUST NOT own Customer, Account, Address, Consent, Preference, or Notification Preference truth and MUST minimize personal data in content and authoring contexts.

### REQ-CMS-027 — Identity and Authorization Boundary

Authentication MUST establish Principal context while contextual trusted server-side Authorization governs every protected CMS action; identifiers, UI state, Role labels, Permissions, Claims, or Scope alone MUST NOT grant access.

### REQ-CMS-028 — Administration Boundary

Administration MAY invoke CMS capabilities through governed workflows but MUST NOT bypass CMS invariants or own CMS publication truth, approval policy, organizational roles, escalation policy, or implementation.

### REQ-CMS-029 — Preview Isolation

Preview MUST enforce Principal and object isolation, prevent public discovery and protected-content leakage, and MUST NOT confer publication status or authority.

### REQ-CMS-030 — Media Reference Safety

CMS media references MUST preserve source ownership, rights, accessibility, integrity, availability, retention, deletion, and orphan-handling boundaries without owning storage or provider truth.

### REQ-CMS-031 — Safe Rendering

CMS Content MUST be safely validated and rendered so untrusted markup, links, scripts, media, or structured content cannot introduce injection, deception, unsafe navigation, or fabricated source meaning.

### REQ-CMS-032 — Truthful Claims

CMS Content MUST avoid unsupported Product, campaign, commercial, policy, environmental, availability, delivery, or service claims and support correction when evidence changes.

### REQ-CMS-033 — Sensitive Data and Secrets

Secrets, credentials, tokens, raw Payment data, unnecessary PII, protected fraud detail, and internal security or provider detail MUST NOT enter public content, previews, logs, events, exports, or ordinary operational evidence.

### REQ-CMS-034 — Accessible Content

CMS-authored customer-facing content and media MUST support applicable WCAG 2.2 AA outcomes, including meaningful structure, text alternatives, understandable language, keyboard use, and non-visual meaning where relevant.

### REQ-CMS-035 — Storefront Boundary

Storefront presentation MUST consume only eligible CMS outcomes and remain a non-authoritative presentation; client state or rendering success MUST NOT create CMS or external Domain truth.

### REQ-CMS-036 — Cache and CDN Boundary

Cache and CDN representations MUST remain replaceable, stale-capable copies; invalidation, purge, propagation, or delivery evidence MUST NOT establish publication or source truth.

### REQ-CMS-037 — Search Index Boundary

Search indexing MAY consume eligible CMS Content but MUST remain a non-authoritative Projection and support correction or reconciliation after publication, withdrawal, or supersession.

### REQ-CMS-038 — Notifications Boundary

Notifications MAY consume explicitly permitted CMS Content, but CMS MUST NOT own Notification request, recipient, template, channel, attempt, provider, Delivery Status, or delivery truth.

### REQ-CMS-039 — Reporting and Analytics Boundary

Reports, dashboards, exports, Analytics Events, and Projections MAY consume CMS evidence but MUST remain non-authoritative, privacy-aware, definition-bound, and unable to mutate CMS state.

### REQ-CMS-040 — Stale and Conflicting Sources

Stale, delayed, reordered, superseded, unavailable, or conflicting external facts MUST produce explicit safe CMS outcomes and MUST NOT be silently presented as current authoritative truth.

### REQ-CMS-041 — Duplicate, Replay, and Concurrency Safety

Duplicate, retried, replayed, or concurrent CMS actions MUST NOT multiply publication effects, lose accepted changes, revive withdrawn content, or silently overwrite a newer version.

### REQ-CMS-042 — Failure and Partial Completion

Validation, save, approval-readiness, schedule, publication, withdrawal, render, cache, index, provider, and dependency failures MUST remain distinguishable from success and preserve known and unknown effects.

### REQ-CMS-043 — Controlled Recovery

Recovery, republish, withdrawal, correction, or replay MUST be explicitly authorized, evidence-based, duplicate-safe, and constrained by current CMS and upstream truth.

### REQ-CMS-044 — Reconciliation

CMS MUST support reconciliation among content identity, versions, source provenance, eligibility, schedules, publication history, presentation copies, and downstream representations while preserving uncertainty and accountable repair.

### REQ-CMS-045 — Contract Boundary

CMS Contracts MUST expose only necessary, versioned, bounded semantics; transport DTOs, routes, methods, schemas, tables, classes, providers, and persistence designs remain outside this Specification.

### REQ-CMS-046 — Conditional Events

Where required, CMS Domain or Integration Events MUST represent completed CMS-owned facts, preserve producer authority and correlation, and MUST NOT invent canonical event names, payloads, topics, brokers, or transports.

### REQ-CMS-047 — Observability

Material CMS operations MUST be bounded, observable, correlated, and diagnosable across content identity, version, actor, source, schedule, publication outcome, and downstream representation without defining numerical targets or implementation mechanisms.

### REQ-CMS-048 — Proportional Audit Records

Governed high-Risk CMS actions MUST produce attributable and proportional Audit Records sufficient for authorized investigation, while routine low-Risk authoring MUST NOT automatically be treated as high-Risk or require disproportionate audit treatment.

### REQ-CMS-049 — Placement Eligibility

CMS placement eligibility MUST be an explicit CMS-owned outcome for an identified eligible CMS Content version, remain bound to its applicable publication outcome and current CMS truth, retain explainable provenance and history, and support correction and reconciliation. Stale, withdrawn, superseded, invalid, or otherwise ineligible content MUST NOT remain eligible merely because a placement, reference, or rendering exists. CMS MUST NOT define layout, ranking, targeting, personalization, placement taxonomy, scheduling policy, publication workflow, numerical limits, or an implementation mechanism.

### REQ-CMS-050 — CMS Content Deletion and Retention Boundary

CMS Content deletion or retention MUST produce an explicit governed outcome, apply applicable reference and dependency checks, and preserve versions, publication history, policy-presentation history, Audit Records, and other historical evidence where governing obligations require them. Deletion MUST NOT silently erase evidence needed to explain previous public outcomes; unresolved or unsafe deletion MUST fail safely or remain explicitly pending or uncertain as appropriate. CMS MUST NOT define retention periods, legal conclusions, physical deletion mechanisms, database or storage design, archival technology, providers, or scheduled-cleanup implementation.

### REQ-CMS-051 — Verification Coverage

Verification evidence MUST cover every Requirement, negative paths, authority separation, isolation, concurrency, uncertainty, accessibility, security, recovery, reconciliation, Contracts, events, and downstream non-authority without selecting test tooling or coverage targets.

## 3. Canonical Terminology

CMS uses canonical repository terms with their `GLOSSARY.md` meanings. CMS Content, content version, placement, preview, publication, withdrawal, correction, supersession, and approval-readiness are CMS-scoped descriptions and establish no repository-wide lifecycle or mandatory transition graph.

## 4. Domain Context

CMS governs publishable editorial presentation while preserving the authority of every source Domain. It does not turn content, rendering, or distribution infrastructure into commercial truth.

## 5. Acceptance Criteria

| Requirement | Acceptance Criteria |
| --- | --- |
| REQ-CMS-001 | Metadata and review evidence show `1.0.0 Approved`, `authoritative: false`, scope `CMS`, Approved Requirements normative only within the CMS Domain scope and not repository-wide, preserved governing-source precedence, and preserved Approved Domain authority. |
| REQ-CMS-002 | Each listed CMS-owned fact has a single CMS authority and distinguishable identity, validation, version, publication, placement, provenance, history, recovery, and reconciliation evidence. |
| REQ-CMS-003 | CMS records and operations cannot establish or redefine any listed external Domain, policy, projection, provider, delivery, storage, Contract-design, or implementation truth. |
| REQ-CMS-004 | A publish, preview, render, cache, index, or display test leaves every referenced external authoritative fact unchanged and cannot repair it. |
| REQ-CMS-005 | Content retains the same stable identifier when title, URL, placement, version, rendering, media references, or external references change, and none of those values substitutes for identity. |
| REQ-CMS-006 | Supported content types are distinguishable as editorial page, homepage content, banner, campaign, or policy presentation while no closed repository-wide taxonomy is asserted. |
| REQ-CMS-007 | Valid creation records actor and source context and returns an explicit accepted outcome without publication; invalid creation returns an explicit rejection. |
| REQ-CMS-008 | Valid content passes CMS-owned completeness, integrity, compatibility, safety, and readiness checks; missing or invalid inputs fail safely, and external references are never trusted without validation. |
| REQ-CMS-009 | Every material revision has a distinguishable version and reviewers can determine what content existed at each relevant time. |
| REQ-CMS-010 | Later edits, corrections, withdrawals, or supersession leave earlier versions and publication evidence unchanged and interpretable. |
| REQ-CMS-011 | Draft, incomplete, rejected, withdrawn, and unapproved content cannot be obtained through a public path or become eligible merely by existing, rendering, or being referenced. |
| REQ-CMS-012 | Approval-readiness evidence can be recorded without defining an approval chain, approver count, organizational role, Permission matrix, or approval policy. |
| REQ-CMS-013 | Only an eligible, explicitly authorized version produces a publication outcome; saving, rendering, scheduling, caching, or provider acknowledgement alone does not. |
| REQ-CMS-014 | Publication is rejected or safely held when validation, authorization, source evidence, or governed policy is missing, stale, conflicting, or uncertain. |
| REQ-CMS-015 | A schedule records intent without proving publication, and no time, delay, scheduler, timezone rule, retry count, or infrastructure mechanism is mandated. |
| REQ-CMS-016 | Delayed, duplicate, missed, reordered, and uncertain schedule execution remain distinguishable from publication success and expose a safe investigation or recovery outcome. |
| REQ-CMS-017 | Withdrawal creates an explicit recorded outcome while prior publication evidence and previously presented external facts remain unchanged. |
| REQ-CMS-018 | Correction and supersession retain the affected versions, provenance, public-state uncertainty, and accountable history rather than rewriting prior evidence. |
| REQ-CMS-019 | Material publication, withdrawal, correction, supersession, failure, and uncertainty can each be reconstructed from attributable publication history. |
| REQ-CMS-020 | CMS references Product context without changing Product or Product Variant identity, definition, content, lifecycle, publication, or sellability. |
| REQ-CMS-021 | Referenced Product Media retains Product-owned metadata, rights, publication, accessibility, lifecycle, and association authority. |
| REQ-CMS-022 | CMS placement references cannot create or alter Category identity, hierarchy, membership, ordering, classification, or navigation truth. |
| REQ-CMS-023 | Displayed commercial claims match current authoritative evidence; CMS performs no Price, Money, Currency, Discount, Promotion, Voucher, tax, fee, eligibility, stacking, total, or outcome calculation. |
| REQ-CMS-024 | Availability presentation is traceable to Inventory evidence, remains correctable when stale, and cannot establish Stock, reservation, Available-to-Sell, availability, or replenishment truth. |
| REQ-CMS-025 | Published policy presentation is tied to governed policy substance and CMS selects no legal, tax, shipping, cancellation, Return, Refund, privacy, fraud, consent, or support policy. |
| REQ-CMS-026 | CMS content and authoring retain no authority over Customer-owned records or preferences, and personal data is limited to what the governed purpose requires. |
| REQ-CMS-027 | Protected CMS actions require authenticated Principal context plus a current server-side decision for the Resource and action; identifiers, UI state, labels, Permissions, Claims, or Scope alone fail authorization. |
| REQ-CMS-028 | Administrative operations invoke CMS behavior and preserve CMS invariants and publication authority without defining approval, role, escalation, or implementation policy. |
| REQ-CMS-029 | Unauthorized or cross-object preview is denied without disclosure; successful preview remains non-public and cannot establish publication. |
| REQ-CMS-030 | Media references with missing ownership, rights, integrity, accessibility, lifecycle, deletion, retention, or orphan evidence fail safely without making CMS a storage/provider authority. |
| REQ-CMS-031 | Unsafe markup, links, scripts, media, and structured content are rejected or rendered safely without injection, deception, unsafe navigation, or altered source meaning. |
| REQ-CMS-032 | Unsupported claims are prevented; changed evidence produces a correctable outcome without CMS becoming Product, policy, availability, delivery, or commercial authority. |
| REQ-CMS-033 | Public content, previews, logs, events, exports, and operational evidence contain no prohibited secrets, credentials, tokens, raw Payment data, unnecessary PII, fraud detail, or internal security/provider detail. |
| REQ-CMS-034 | Applicable published content demonstrates meaningful structure, alternatives, understandable language, keyboard use, and non-visual meaning consistent with WCAG 2.2 AA without prescribing implementation. |
| REQ-CMS-035 | Storefronts consume only eligible CMS outcomes; client or render state remains non-authoritative and cannot create CMS or external truth. |
| REQ-CMS-036 | Cache and CDN copies are identifiable as stale-capable representations; purge, invalidation, propagation, or delivery evidence cannot establish publication and divergence is correctable. |
| REQ-CMS-037 | Search results remain non-authoritative and can be corrected or reconciled after publication, withdrawal, or supersession. |
| REQ-CMS-038 | Permitted CMS content can be consumed without CMS controlling Notification request, recipient, template, channel, attempt, provider, Delivery Status, or delivery truth. |
| REQ-CMS-039 | Reports, exports, dashboards, Analytics Events, and Projections are privacy-aware, source-defined, stale-capable, non-authoritative, and unable to mutate CMS. |
| REQ-CMS-040 | Stale, delayed, reordered, superseded, unavailable, and conflicting source facts each yield an explicit safe outcome rather than current authoritative presentation. |
| REQ-CMS-041 | Duplicate, retry, replay, and concurrent actions neither multiply publication effects nor lose accepted changes, revive withdrawn content, or overwrite newer versions. |
| REQ-CMS-042 | Each validation, save, readiness, schedule, publication, withdrawal, render, cache, index, provider, and dependency failure is distinguishable from success and preserves known and unknown effects. |
| REQ-CMS-043 | Recovery, republish, withdrawal, correction, and replay are denied without current authorization and evidence and otherwise remain duplicate-safe and constrained by current CMS and upstream truth. |
| REQ-CMS-044 | Reconciliation correlates identity, versions, provenance, eligibility, schedules, history, and representations; discrepancies and uncertainty remain visible until accountable repair. |
| REQ-CMS-045 | Contracts expose only necessary bounded versioned semantics and define no transport DTO, route, method, schema, table, class, provider, or persistence design. |
| REQ-CMS-046 | Emitted events, when required, follow a completed CMS-owned fact with producer authority and correlation; no canonical name, payload, topic, broker, or transport is prescribed. |
| REQ-CMS-047 | Material operations are bounded, correlated, diagnosable across the required contexts, and measurable without a numerical target or mandated mechanism. |
| REQ-CMS-048 | Governed high-Risk actions create attributable proportional Audit Records; routine low-Risk authoring is not automatically elevated or over-audited. |
| REQ-CMS-049 | Placement eligibility is an explicit CMS-owned outcome for an identified eligible version, remains bound to current publication truth, retains explainable provenance and history, and supports correction and reconciliation; stale, withdrawn, superseded, invalid, or otherwise ineligible content is not eligible because a placement, reference, or rendering exists, and no layout, ranking, targeting, personalization, taxonomy, scheduling, workflow, numerical, or implementation policy is defined. |
| REQ-CMS-050 | Deletion or retention produces an explicit governed outcome with applicable reference and dependency checks; required versions, publication and policy-presentation history, Audit Records, and historical evidence remain preserved; prior public evidence is not silently erased; unsafe or unresolved deletion fails safely or remains explicit; and no retention period, legal conclusion, physical mechanism, storage design, archival technology, provider, or cleanup implementation is defined. |
| REQ-CMS-051 | Verification covers every CMS Requirement, negative path, authority boundary, isolation, concurrency, uncertainty, accessibility, security, recovery, reconciliation, Contract, event, and downstream non-authority without selecting tooling or numerical coverage targets. |

## 6. Requirement Traceability

| Requirement | PRODUCT.md | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- |
| REQ-CMS-001 | PRODUCT.md §§13, 17.3 | REQ-BUS-001–003, 048, 050 | REQ-PRD-001–002; REQ-CAT-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-PAY-002–003; REQ-SHP-002–003; REQ-CHK-002–003; REQ-ORD-002–003; REQ-RET-002–003; REQ-NTF-002–003 | AGENTS.md §§3.6, 5; ARCHITECTURE.md §§9–10 | All CMS consumers |
| REQ-CMS-002 | PRODUCT.md §§12.1, 15.6, 16.8 | REQ-BUS-050 | REQ-PRD-025–028; REQ-NTF-019 | ARCHITECTURE.md §§9–10 | CMS authors, Storefront, Administration |
| REQ-CMS-003 | PRODUCT.md §§13, 17.3 | REQ-BUS-001–003, 013–016, 031–034, 044, 048–050, 052–054 | REQ-PRD-014–020, 025–028; REQ-CAT-010, 017–020, 029–031; REQ-CUS-018–021, 037–041; REQ-IDN-024–031, 037, 043–049; REQ-INV-002–009; REQ-CART-002–003; REQ-PRC-002–015; REQ-PAY-002–003, 029–035, 043–044; REQ-SHP-002–003, 041–042; REQ-CHK-002–003, 043; REQ-ORD-002–003, 051; REQ-RET-002–003, 035, 047; REQ-NTF-016–020, 045–055 | AGENTS.md §§3.6, 5; ARCHITECTURE.md §§9–10, 14 | All CMS consumers |
| REQ-CMS-004 | PRODUCT.md §§13, 17.3 | REQ-BUS-001–003, 048, 050 | REQ-PRD-001–002; REQ-CAT-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-PAY-002–003; REQ-SHP-002–003; REQ-CHK-002–003; REQ-ORD-002–003; REQ-RET-002–003; REQ-NTF-002–003 | AGENTS.md §§3.6, 5; ARCHITECTURE.md §§9–10 | All CMS consumers |
| REQ-CMS-005 | PRODUCT.md §§15.6, 17.4 | REQ-BUS-038, 050 | — | ARCHITECTURE.md §§9–10; DATABASE.md §42 | CMS, Administration, downstream consumers |
| REQ-CMS-006 | PRODUCT.md §§12.1, 15.6, 29 | REQ-BUS-050 | REQ-PRD-025–028; REQ-CAT-020 | ARCHITECTURE.md §§9–10 | CMS authors, Storefront |
| REQ-CMS-007 | PRODUCT.md §§7.3–7.4, 15.6, 16.8 | REQ-BUS-038, 050 | REQ-PRD-025–028, 034 | ARCHITECTURE.md §§9–10; SECURITY-STANDARDS.md §§18–20 | CMS authors, Administration |
| REQ-CMS-008 | PRODUCT.md §§15.6, 16.8, 34 | REQ-BUS-038, 050 | REQ-PRD-025–028, 039–041 | SECURITY-STANDARDS.md §§18–20, 27; ARCHITECTURE.md §9 | CMS authors, Administration, Security |
| REQ-CMS-009 | PRODUCT.md §§16.8, 17.4 | REQ-BUS-038, 050 | REQ-PRD-006, 028, 030–031 | ARCHITECTURE.md §§9, 23; DATABASE.md §42 | CMS, Administration, Audit |
| REQ-CMS-010 | PRODUCT.md §§16.8, 17.4 | REQ-BUS-034, 050 | REQ-PRD-006, 030–031 | ARCHITECTURE.md §23; DATABASE.md §§42, 48–52 | CMS, Audit, Support |
| REQ-CMS-011 | PRODUCT.md §§15.6, 16.8 | REQ-BUS-050 | REQ-PRD-018–020, 028 | ARCHITECTURE.md §9; SECURITY-STANDARDS.md §§10–12 | Storefront, CMS authors, Administration |
| REQ-CMS-012 | PRODUCT.md §§15.6, 24 (Decision 22) | REQ-BUS-032–034, 038, 050 | REQ-IDN-024–031, 037, 049; REQ-PRD-019 | SECURITY-STANDARDS.md §§10–12, 27; ARCHITECTURE.md §§9, 30 | CMS authors, Administration, Security |
| REQ-CMS-013 | PRODUCT.md §§15.6, 16.8, 17.3 | REQ-BUS-038, 050 | REQ-PRD-018–020, 028 | ARCHITECTURE.md §§9, 14 | CMS authors, Storefront, Administration |
| REQ-CMS-014 | PRODUCT.md §§15.6, 16.8, 17.3, 24 | REQ-BUS-038, 048, 050 | REQ-PRD-018–020, 028; REQ-IDN-028, 037 | SECURITY-STANDARDS.md §§10–12, 27; ARCHITECTURE.md §9 | CMS authors, Administration, Storefront |
| REQ-CMS-015 | PRODUCT.md §§17.2–17.4, 23–24, 35 | REQ-BUS-035–036, 045–046, 050 | — | ARCHITECTURE.md §§14, 20, 26; API.md §§48, 54–58; DATABASE.md §§42, 52 | Operations, CMS, downstream consumers |
| REQ-CMS-016 | PRODUCT.md §§17.2–17.4, 23–24, 35 | REQ-BUS-035–036, 045–046, 050 | — | ARCHITECTURE.md §§14, 20, 26; API.md §§48, 54–58; DATABASE.md §§42, 52 | Operations, CMS, downstream consumers |
| REQ-CMS-017 | PRODUCT.md §§15.6, 16.8, 17.4 | REQ-BUS-038, 050 | REQ-PRD-028, 037 | ARCHITECTURE.md §§9, 23; DATABASE.md §42 | CMS authors, Storefront, Audit |
| REQ-CMS-018 | PRODUCT.md §§16.8, 17.4 | REQ-BUS-038, 050 | REQ-PRD-006, 027–031 | ARCHITECTURE.md §23; DATABASE.md §42 | CMS authors, Storefront, Audit |
| REQ-CMS-019 | PRODUCT.md §§16.8, 17.4, 31 | REQ-BUS-034, 043, 050 | REQ-PRD-006, 030–031, 043 | ARCHITECTURE.md §23; DATABASE.md §42; SECURITY-STANDARDS.md §27 | Audit, Support, Administration |
| REQ-CMS-020 | PRODUCT.md §§12.1, 15.6, 16.8 | REQ-BUS-050 | REQ-PRD-018–020, 025–028 | AGENTS.md §§3.4–3.10, 5 | Product, CMS authors, Storefront |
| REQ-CMS-021 | PRODUCT.md §§12.1, 15.6, 16.8, 29 | REQ-BUS-050 | REQ-PRD-014–017 | AGENTS.md §§3.4–3.10, 5 | Product, CMS authors, Storefront |
| REQ-CMS-022 | PRODUCT.md §§12.1, 13, 29 | REQ-BUS-050 | REQ-CAT-010, 017–018, 020, 029–031 | AGENTS.md §§3.4–3.10, 5 | Category, Search, Storefront |
| REQ-CMS-023 | PRODUCT.md §§9.3, 12.6, 16.3, 24 | REQ-BUS-014–016, 050, 054 | REQ-PRC-002–015 | AGENTS.md §§3.4–3.10, 5 | Pricing, CMS authors, Storefront |
| REQ-CMS-024 | PRODUCT.md §§9.4, 12.7 | REQ-BUS-013, 050 | REQ-INV-002–009 | AGENTS.md §§3.4–3.10, 5 | Inventory, CMS authors, Storefront |
| REQ-CMS-025 | PRODUCT.md §§15.6, 16.8, 16.11, 24 | REQ-BUS-048, 050, 054 | REQ-PRC-013, 021; REQ-PAY-035, 044; REQ-SHP-042; REQ-RET-014, 035 | AGENTS.md §5; ARCHITECTURE.md §9 | Product, Legal, CMS authors, Storefront |
| REQ-CMS-026 | PRODUCT.md §§5.5, 16.5, 24 | REQ-BUS-031–034, 050 | REQ-CUS-018–021, 037–041; REQ-IDN-043–044 | SECURITY-STANDARDS.md §§7, 12, 27, 35 | Privacy, Security, CMS authors |
| REQ-CMS-027 | PRODUCT.md §§7.3–7.7, 12.9, 17.3, 24 | REQ-BUS-031–034, 038 | REQ-IDN-024–031, 037, 043–049 | SECURITY-STANDARDS.md §§10–12, 27, 35; ARCHITECTURE.md §30 | CMS authors, Administration, Security |
| REQ-CMS-028 | PRODUCT.md §§7.3–7.7, 12.9, 17.3, 24 | REQ-BUS-031–034, 038 | REQ-CUS-037, 039–040; REQ-IDN-024–031, 037, 043–049 | SECURITY-STANDARDS.md §§10–12, 27, 35; ARCHITECTURE.md §30 | Administration, Security, CMS authors |
| REQ-CMS-029 | PRODUCT.md §§7.3–7.7, 12.9, 17.3, 24 | REQ-BUS-031–034, 038 | REQ-CUS-037, 039–040; REQ-IDN-024–031, 037, 043–049 | SECURITY-STANDARDS.md §§10–12, 27, 35; ARCHITECTURE.md §30 | Administration, Security, CMS authors |
| REQ-CMS-030 | PRODUCT.md §§15.6, 16.8, 29 | REQ-BUS-038, 050 | REQ-PRD-014–017 | ARCHITECTURE.md §29; SECURITY-STANDARDS.md §§18–20 | Product, CMS authors, Media adapters |
| REQ-CMS-031 | PRODUCT.md §§15.6, 16.8, 29 | REQ-BUS-038, 050 | REQ-PRD-014–017, 041 | SECURITY-STANDARDS.md §§18–20, 27; ARCHITECTURE.md §29 | CMS authors, Storefront, Media adapters |
| REQ-CMS-032 | PRODUCT.md §§12.1, 15.6, 16.8 | REQ-BUS-050 | REQ-PRD-018–020, 025–028 | AGENTS.md §§3.4–3.10, 5 | Product, CMS authors, Storefront |
| REQ-CMS-033 | PRODUCT.md §§5.5, 16.5, 23 | REQ-BUS-031, 034, 050 | REQ-CUS-040–041; REQ-IDN-043–044; REQ-PAY-031–032 | SECURITY-STANDARDS.md §§7, 12, 27, 35 | Security, CMS authors, Operations |
| REQ-CMS-034 | PRODUCT.md §§6.4, 16.10, 30 | REQ-BUS-042, 050 | REQ-PRD-015, 041 | ACCESSIBILITY.md §§1–32; DESIGN-SYSTEM.md | Accessibility, CMS authors, Storefront |
| REQ-CMS-035 | PRODUCT.md §§12.1, 29 | REQ-BUS-037, 042, 050 | REQ-PRD-023–024, 042 | ARCHITECTURE.md §§12, 26, 29; PERFORMANCE.md | Storefront, CMS authors |
| REQ-CMS-036 | PRODUCT.md §§12.1, 29 | REQ-BUS-037, 042, 050 | REQ-PRD-023–024, 042 | ARCHITECTURE.md §§12, 26, 29; PERFORMANCE.md | Storefront, Cache/CDN adapters, Operations |
| REQ-CMS-037 | PRODUCT.md §§12.1, 29 | REQ-BUS-044, 050 | REQ-PRD-032–033; REQ-CAT-029–030 | ARCHITECTURE.md §§9, 26 | Search, Storefront, Operations |
| REQ-CMS-038 | PRODUCT.md §§15.6, 16.8, 20 | REQ-BUS-049–050 | REQ-NTF-016–020, 045–055 | ARCHITECTURE.md §§9, 14, 26; EVENTS.md | Notifications, CMS authors |
| REQ-CMS-039 | PRODUCT.md §§12.10, 18, 24 | REQ-BUS-044, 053 | REQ-CUS-038; REQ-NTF-055 | ARCHITECTURE.md §§9, 14, 26; SECURITY-STANDARDS.md §35 | Reporting, Analytics, Administration |
| REQ-CMS-040 | PRODUCT.md §§17.2–17.4, 35 | REQ-BUS-035, 044–045, 050 | REQ-PRC-008, 024; REQ-INV-007, 009; REQ-NTF-031, 055 | ARCHITECTURE.md §§20, 26; API.md §§54–58 | CMS, Storefront, downstream consumers |
| REQ-CMS-041 | PRODUCT.md §§17.3–17.4, 35 | REQ-BUS-035–036, 050 | REQ-PRD-029, 035; REQ-CAT-024; REQ-INV-016; REQ-CART-022; REQ-NTF-042 | DATABASE.md §§42, 52; API.md §§48, 54–58 | CMS, Administration, Operations |
| REQ-CMS-042 | PRODUCT.md §§17.2, 23, 35 | REQ-BUS-035, 045–046, 050 | REQ-PRD-040; REQ-CAT-042; REQ-CART-032; REQ-NTF-041 | ARCHITECTURE.md §§20, 26; API.md §§54–58 | CMS, Operations, provider adapters |
| REQ-CMS-043 | PRODUCT.md §§17.3–17.4, 23, 35 | REQ-BUS-036, 045, 050 | REQ-CUS-037; REQ-IDN-037, 041; REQ-NTF-043 | SECURITY-STANDARDS.md §§10–12, 27; API.md §§48, 54–58 | Administration, Operations, Support |
| REQ-CMS-044 | PRODUCT.md §§17.4, 18, 23, 35 | REQ-BUS-036, 044–045, 050 | REQ-INV-030; REQ-PRC-028; REQ-PAY-024; REQ-SHP-029; REQ-NTF-044, 055 | ARCHITECTURE.md §§20, 26; DATABASE.md §42 | Operations, Audit, downstream consumers |
| REQ-CMS-045 | PRODUCT.md §§13, 37 | REQ-BUS-037, 046, 050 | — | API.md §§37–61; ARCHITECTURE.md §§12–16 | CMS clients, integration adapters |
| REQ-CMS-046 | PRODUCT.md §§13, 17, 20 | REQ-BUS-039, 046, 050 | — | EVENTS.md; ARCHITECTURE.md §14 | Event consumers, Notifications, Search, Reporting |
| REQ-CMS-047 | PRODUCT.md §§18, 23, 35 | REQ-BUS-045 | — | ARCHITECTURE.md §§20, 31; SECURITY-STANDARDS.md §27 | Operations, Support |
| REQ-CMS-048 | PRODUCT.md §§7.3–7.7, 17.4, 31 | REQ-BUS-033–034, 043 | REQ-CUS-045; REQ-IDN-049; REQ-INV-033; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040; REQ-CHK-042; REQ-ORD-050; REQ-RET-046; REQ-NTF-054 | SECURITY-STANDARDS.md §§7.9, 27; DATABASE.md §42 | Audit, Security, Administration |
| REQ-CMS-049 | PRODUCT.md §§12.1, 15.6, 29 | REQ-BUS-037, 050 | REQ-PRD-023–024; REQ-CAT-017–018 | ARCHITECTURE.md §§9, 12, 26 | Storefront, CMS authors, Search |
| REQ-CMS-050 | PRODUCT.md §§16.8, 37 | REQ-BUS-034, 050 | REQ-PRD-006, 030–031, 038; REQ-CUS-028–029, 041 | DATABASE.md §§42, 48–52; SECURITY-STANDARDS.md §35 | CMS authors, Audit, Operations |
| REQ-CMS-051 | PRODUCT.md §§20, 31, 34 | REQ-BUS-040–043, 050 | REQ-PRD-044; REQ-CAT-043; REQ-CUS-049; REQ-IDN-050; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-CHK-044; REQ-ORD-052; REQ-RET-048; REQ-NTF-056 | TESTING-STANDARDS.md; ACCESSIBILITY.md; SECURITY-STANDARDS.md | Engineering, Product, Security, Accessibility |

## 7. Open Product Decisions

`PRODUCT.md` contains exactly 30 Open Product Decisions. The following 19 are materially relevant to CMS, remain in exact source order, and are unresolved by this Specification.

| Product Decision | CMS boundary affected |
| --- | --- |
| Final brand name and visual identity. | Controls brand-facing content and visual presentation; CMS records only approved inputs and does not choose the brand identity. |
| Initial product categories and catalogue taxonomy. | Controls catalogue and category presentation references; CMS does not define taxonomy, Product, or Category truth. |
| Shipping provider, service levels, delivery areas, and fee policy. | Controls published delivery-policy content; CMS does not select providers, areas, service levels, or fees. |
| Free-delivery threshold and promotional treatment. | Controls free-delivery and campaign claims; CMS does not determine thresholds or promotional treatment. |
| Tax-inclusive display and invoice requirements. | Controls tax and invoice presentation; CMS does not determine calculation or document policy. |
| Cancellation eligibility and cutoff policy. | Controls cancellation-policy presentation; CMS does not establish eligibility or cutoff rules. |
| Returns, exchanges, and refund policy. | Controls Return, exchange, and Refund policy presentation; CMS does not select eligibility, lifecycle, or financial rules. |
| Voucher and promotion stacking policy. | Controls Promotion and Voucher claims; CMS does not decide stacking or commercial calculation. |
| Product-review support. | Determines whether review content exists and its governance; CMS defines no review model or moderation policy. |
| Low-stock and out-of-stock customer messaging. | Controls customer-facing stock messaging; CMS does not establish availability thresholds or Inventory truth. |
| Customer-support channels and service expectations. | Controls support-information presentation; CMS does not choose channels, hours, or service expectations. |
| Marketing-consent and communication-preference model. | Controls marketing and consent-related content; CMS does not infer Consent or define Preference policy. |
| Initial analytics provider and event taxonomy. | Controls permitted CMS analytics consumption; CMS does not select a provider or event taxonomy. |
| Initial reporting and export requirements. | Controls CMS evidence exposed to reports or exports; CMS does not choose reporting scope, format, or targets. |
| Content approval and scheduled-publication workflow. | Directly governs future approval and scheduling policy; CMS records readiness and intent without selecting workflow or schedule rules. |
| Administrative role and permission matrix. | Controls protected CMS access; CMS requires contextual Authorization without defining Roles or Permissions. |
| Production customer-service and operational escalation process. | Controls content-incident recovery and escalation presentation; CMS does not select escalation levels or service targets. |
| South African tax-display, invoice, and credit-note policy. | Controls South African tax, invoice, and Credit Note presentation; CMS does not decide legal or financial policy. |
| Product launch date, release scope, and post-launch support window. | Controls launch-content readiness and withdrawal planning; CMS does not choose a launch date, release scope, or support window. |

## 8. Risks

| Risk | Control |
| --- | --- |
| Draft or unapproved content becomes public | Require Draft isolation, current publication eligibility, and contextual Authorization before any public outcome. |
| CMS becomes alternate Product or commercial truth | Validate referenced source authority and keep CMS presentation unable to create Product or commercial truth. |
| Stale commercial, availability, shipping, Return, tax, or policy claims | Preserve provenance and freshness, surface stale or conflicting evidence, and use correction and reconciliation. |
| Unsupported campaign or Product claims | Require supportable governed evidence and correct or withdraw claims when that evidence changes. |
| Publication race or lost concurrent update | Use stable version identity plus duplicate, replay, and concurrency safeguards that prevent silent overwrite. |
| Schedule executes after withdrawal or supersession | Revalidate the intended version, current eligibility, withdrawal, and supersession state before execution. |
| Inaccessible content or media | Require applicable WCAG 2.2 AA content and media evidence before eligible publication. |
| Unsafe markup or injection | Validate untrusted content and render it safely without scripts, injection, deception, or changed source meaning. |
| Malicious or unauthorized media | Accept only governed media references with verified source ownership, rights, integrity, and safety evidence. |
| Broken media reference | Detect unresolved references, prevent unsafe publication, and retain an explainable failure outcome. |
| Cross-Principal preview or authoring disclosure | Require contextual server-side Authorization, Principal/object isolation, and protected non-public preview behavior. |
| Excessive administrative privilege | Apply least privilege and owning-Domain Authorization without defining an administrative Role or Permission matrix. |
| Missing version or publication history | Preserve interpretable content-version and publication history through later correction, withdrawal, and supersession. |
| Cache, CDN, or storefront divergence | Keep storefront, cache, and CDN copies non-authoritative and support correction, invalidation, and reconciliation. |
| Search-index divergence | Keep the search index a correctable non-authoritative Projection reconciled to current publication truth. |
| Notifications consume stale or unauthorized content | Require current permitted CMS content while preserving Notifications-owned template and delivery authority. |
| Reporting or Analytics becomes authoritative | Keep outputs source-labelled, stale-capable, and unable to mutate CMS or upstream truth. |
| Sensitive Data or Secrets enter content | Minimize data and exclude Secrets and prohibited Sensitive Data from content, previews, logs, events, exports, and evidence. |
| Unknown partial publication effect | Record known and unknown publication effects distinctly and require investigation, recovery, or reconciliation before success. |
| Reconciliation gap | Correlate identity, versions, provenance, schedules, outcomes, and representations with accountable authorized repair. |
| Implementation choice becomes CMS policy | Keep provider and implementation mechanisms outside CMS policy and behind replaceable governed Contracts. |
| Policy presentation diverges from governed substance | Version presentation against governed policy substance and correct divergence without deciding the policy. |
| Unauthorized withdrawal or correction | Require current contextual Authorization, current eligibility, an explicit outcome, and attributable evidence. |
| Historical evidence is rewritten | Require governed deletion outcomes and preserve required versions, publication history, policy history, and Audit Records. |

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

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-06 | Draft | Initial comprehensive CMS Domain Specification. |
| 1.0.0 | 2026-09-06 | Approved | Promoted the CMS Domain Specification to its Approved normative baseline without changing substantive Domain behavior or authority boundaries. |

## 11. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `CMS`, Approved Requirements are normative only within the CMS Domain scope and not repository-wide authority, governing-source precedence is preserved, and Approved Domain authority is preserved;
2. CMS owns only CMS Content and CMS publication truth;
3. every cross-Domain and Product-policy boundary is preserved;
4. no complete mandatory lifecycle graph or implementation choice is introduced;
5. exactly 51 Requirements are unique, sequential, gap-free from `REQ-CMS-001` through `REQ-CMS-051`, clause-complete, and implementation-neutral;
6. every Requirement has exactly one individually authored, clause-complete Acceptance Criteria row, for exactly 51 rows;
7. every Requirement has exactly one semantically direct traceability row, for exactly 51 rows, and every cited identifier exists;
8. all 30 Product Decisions were reviewed, the 19 material decisions remain source-ordered, and none is resolved;
9. all Risks are material, CMS-specific, non-duplicative, and have distinct risk-specific controls without implementation prescription;
10. all Related Documents exist and are relevant;
11. Revision History contains exactly the preserved `0.1.0 Draft` row and the new `1.0.0 Approved` row;
12. no Glossary amendment is required;
13. Markdown, headings, tables, UTF-8, whitespace, final newline, and prohibited-marker checks pass; and
14. Git scope contains only the authorized lifecycle-promotion changes to `specifications/domains/cms/cms-domain.md`, with nothing staged, untracked, unrelated, or otherwise modified.
