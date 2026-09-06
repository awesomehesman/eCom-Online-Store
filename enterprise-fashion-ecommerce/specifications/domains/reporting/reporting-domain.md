---
title: Reporting Domain
version: 0.1.0
status: Draft
owner: Product and Engineering
last_updated: 2026-09-07
authoritative: false
---

# Reporting Domain

## 1. Purpose

This Specification defines implementation-neutral Requirements for Reporting-owned definitions, read models, Projections, reports, dashboards, exports, lineage, freshness, recovery, and reconciliation.

This document uses scope code `RPT`. It is a Draft and is not yet normative. It remains subordinate to higher-authority governing sources, preserves every Approved Domain's authority, resolves no Open Product Decision, and has no repository-wide authority.

## 2. Scope, Authority, and Requirements

### REQ-RPT-001 — Lifecycle, Authority, and Scope

Reporting MUST govern only Reporting-owned truth under scope `RPT`, preserve governing-source precedence and Approved Domain authority, and MUST NOT treat this Draft as normative or repository-wide authority before approval.

### REQ-RPT-002 — Reporting-Owned Truth

Reporting MUST own only Reporting-specific report, dashboard, Projection, export, definition-version, lineage, freshness, delivery, failure, recovery, and reconciliation truth.

### REQ-RPT-003 — Cross-Domain Non-Authority

Reporting MUST NOT own, mutate, or redefine Product, Category, Customer, Identity, Inventory, Cart, Pricing, Checkout, Payment, Shipping, Order, Return, Notifications, CMS, Administration, fraud, tax, legal, commercial, Consent, Preference, or Product-policy truth.

### REQ-RPT-004 — Stable Reporting Identity

Each governed report, dashboard, Projection, export request, export outcome, and definition version MUST have stable identity appropriate to its Reporting meaning and distinct from source identifiers, UI routes, file names, jobs, and provider identifiers.

### REQ-RPT-005 — Governed Definition

Every governed Reporting definition MUST identify its purpose, owner, source facts, formula or derivation where applicable, unit, aggregation, time grain, Dimensions, filters, Currency treatment, and known exclusions before its result is represented as defined.

### REQ-RPT-006 — Metric and KPI Boundary

A Metric MUST use an explicit governed definition, while a KPI MUST remain a Product-governed designation; Reporting MUST NOT invent KPI status, success targets, thresholds, or business interpretation.

### REQ-RPT-007 — Dimension and Measure Integrity

Dimensions and Measures MUST preserve their governed meaning, units, source lineage, valid applicability, and aggregation constraints without using presentation labels as semantic authority.

### REQ-RPT-008 — Fact Boundary

A Reporting Fact MUST be a Reporting-owned analytical representation derived from traceable source evidence and MUST NOT be treated as the originating Domain fact or used to mutate it.

### REQ-RPT-009 — Funnel Definition

A Funnel MUST identify governed ordered analytical steps, qualifying evidence, population, time window semantics, exclusions, and completion meaning without defining a transactional lifecycle or mandatory customer traversal.

### REQ-RPT-010 — Attribution Boundary

Attribution MUST use an Approved rule and traceable eligible evidence and MUST NOT infer causation, Consent, Customer identity, Campaign authority, or commercial truth from correlation alone.

### REQ-RPT-011 — Definition Versioning

A material change to formula, source, unit, aggregation, time grain, Dimension, filter, Currency treatment, or exclusion MUST create or identify a distinguishable governed definition version and MUST NOT silently reinterpret prior results.

### REQ-RPT-012 — Historical Reproducibility

Reporting MUST preserve sufficient definition version, source lineage, temporal context, and calculation context to reproduce or explain governed historical results where required.

### REQ-RPT-013 — Source Lineage and Provenance

Every governed Reporting result MUST retain traceable lineage to the authoritative source types, applicable source identifiers or bounded references, extraction or observation context, definition version, and transformation provenance without exposing protected internals.

### REQ-RPT-014 — Source Authority Preservation

Reporting MUST consume source-Domain evidence according to the producing Domain's Contract and MUST NOT reinterpret fields, infer missing authority, or substitute a Projection for authoritative reads where current truth is required.

### REQ-RPT-015 — Transactional and Analytical Separation

Reporting computation, correction, rebuild, backfill, query, dashboard, and export activity MUST NOT participate in or control transactional Domain decisions or write paths.

### REQ-RPT-016 — Domain Event and Analytics Event Separation

Reporting MUST preserve the distinction between Domain Events, Integration Events, Analytics Events, and technical telemetry and MUST NOT treat an Analytics Event or technical event as proof of an authoritative Domain outcome.

### REQ-RPT-017 — Freshness and Staleness

Each governed Projection, report, dashboard, or export MUST expose or make determinable its applicable freshness, source cutoff, or staleness status and MUST NOT present stale-capable data as current authoritative truth.

### REQ-RPT-018 — Completeness and Uncertainty

Complete, partial, unavailable, pending, stale, conflicting, and unknown Reporting outcomes MUST remain distinguishable, with coverage and exclusions stated where material.

### REQ-RPT-019 — Late and Missing Evidence

Late or missing source evidence MUST preserve the affected Reporting period, source, expected coverage, and uncertainty and support governed correction or reconciliation without fabricating values.

### REQ-RPT-020 — Duplicate and Reordered Evidence

Duplicate, replayed, or reordered source evidence MUST NOT multiply Measures, regress accepted source meaning, or silently overwrite a later governed Reporting outcome.

### REQ-RPT-021 — Superseded and Conflicting Evidence

Superseded or conflicting source evidence MUST remain distinguishable, preserve provenance and definition context, and use owning-Domain evidence to determine any permitted Reporting correction.

### REQ-RPT-022 — Replay-Safe Projection Processing

Projection processing MUST be duplicate-safe, replay-safe, order-aware, and capable of identifying conflicting or uncertain contributions without prescribing a processing mechanism.

### REQ-RPT-023 — Rebuild and Backfill

A governed rebuild or backfill MUST identify scope, source boundary, definition version, authorization, progress, outcome, and uncertainty and MUST not overwrite unrelated or newer accepted Reporting truth.

### REQ-RPT-024 — Source Correction Effects

A correction in an owning Domain MUST affect Reporting only through governed source evidence and applicable definition semantics, preserving prior Reporting provenance and any required historical interpretation.

### REQ-RPT-025 — Reporting Recovery

Failed or interrupted Reporting delivery, processing, query, export, rebuild, or backfill MUST support explicit safe recovery where governed, retaining identity, authorization, prior outcomes, and uncertainty.

### REQ-RPT-026 — Reporting Reconciliation

Reporting MUST support comparison of Projection outcomes with authoritative source evidence and preserve discrepancy, provenance, correction, and unresolved uncertainty while source Domains retain their authority.

### REQ-RPT-027 — Order Reporting Boundary

Order reporting MUST preserve Order identity, Order Number isolation, Order Item and Order Snapshot history, lifecycle meaning, cancellation separation, and Order-owned creation truth.

### REQ-RPT-028 — Payment and Refund Reporting Boundary

Payment reporting MUST preserve Payment, Payment Attempt, provider-evidence, Payment Authorization, Capture, Void, Settlement, Payment Transaction, Refund, Refund Transaction, Chargeback, failure, and uncertainty distinctions.

### REQ-RPT-029 — Revenue Definition Boundary

Gross Revenue, Net Revenue, Average Order Value, and other financial Metrics MUST use Approved definitions that explicitly state applicable Price, Discount, Promotion, Voucher, tax, shipping, cancellation, Return, Refund, Chargeback, and credit treatment; Reporting MUST NOT invent those policies.

### REQ-RPT-030 — Currency Integrity

Reporting MUST preserve source Money and Currency, prohibit aggregation across incompatible Currencies without an Approved conversion definition, and retain conversion source and temporal context where conversion is governed.

### REQ-RPT-031 — Tax and Commercial Document Boundary

Reporting MAY consume governed tax, invoice, and Credit Note evidence but MUST NOT calculate legal liability, establish document authority, or resolve tax-display, invoice, Credit Note, recognition, or compliance policy.

### REQ-RPT-032 — Inventory Reporting Boundary

Inventory reporting MUST preserve Inventory authority for Stock, Available-to-Sell, Stock Reservation, Stock Adjustment, Stock Movement, concurrency, and reconciliation and MUST NOT independently calculate current availability.

### REQ-RPT-033 — Shipping and Return Reporting Boundary

Shipping and Return reporting MUST preserve Shipment, Fulfilment, Dispatch, delivery, Return Request, eligibility, Return Authorization, receipt, inspection, disposition, reverse-logistics, cancellation, Refund, and Inventory distinctions.

### REQ-RPT-034 — Product, Category, and CMS Reporting Boundary

Reporting MUST preserve Product, Product Variant, Attribute, Product Media, publication, sellability, Category hierarchy, membership, navigation, CMS Content, Campaign, placement, and publication authority and historical meaning.

### REQ-RPT-035 — Cart and Checkout Analytics Boundary

Cart and Checkout reporting MAY measure governed journey evidence but MUST NOT treat Cart intent, funnel progression, submission, Payment initiation, or client activity as Order creation, Payment success, Inventory commitment, or Customer identity.

### REQ-RPT-036 — Customer and Identity Boundary

Reporting MUST keep Customer, Visitor, Account, Identity, Principal, Staff User, and Session distinct and MUST NOT derive identity, ownership, Authentication, or Authorization from analytical association.

### REQ-RPT-037 — Consent and Preference Boundary

Customer analytics and reporting MUST consume current governed Consent, Preference, purpose, and communication classification where required and MUST NOT infer Consent from purchase, presence, identity, or prior activity.

### REQ-RPT-038 — Sensitive Data and Isolation

Reporting MUST enforce purpose limitation, least privilege, object and population isolation, minimization, masking where governed, aggregation safety, and enumeration resistance across reports, dashboards, queries, caches, exports, logs, telemetry, events, and evidence.

### REQ-RPT-039 — Contextual Authorization

Every protected Reporting read, definition change, rebuild, backfill, export, recovery, and reconciliation action MUST require current trusted server-side Authorization for the Principal, Resource, action, purpose, and applicable state.

### REQ-RPT-040 — Dashboard Non-Authority

A dashboard MUST present source, definition version, filters, time context, freshness, completeness, and uncertainty as applicable and MUST NOT directly mutate source-Domain truth or present display state as authorization or proof.

### REQ-RPT-041 — Export Governance

A Reporting export MUST have governed purpose, definition, scope, source lineage, filters, time context, freshness, authorization, minimization, outcome, and expiry or retention handling where governed without defining a retention period or file mechanism.

### REQ-RPT-042 — Export Outcome Integrity

Export request, accepted, processing, complete, partial, failed, expired where governed, unavailable, and uncertain outcomes MUST remain distinguishable, and download visibility or file presence MUST NOT alone prove completeness or authorization.

### REQ-RPT-043 — Bounded Reporting Operations

Reporting queries, histories, aggregations, dashboards, exports, rebuilds, and backfills MUST support bounded delivery and MUST NOT require unbounded source scans or result collections.

### REQ-RPT-044 — Workload Isolation

Reporting workload MUST NOT materially compromise transactional correctness or availability and MUST permit governed degradation, deferral, or rejection without claiming a numerical capacity or service target.

### REQ-RPT-045 — Accessible Reporting

Applicable reports, dashboards, tables, filters, charts, status, errors, empty states, partial results, and export workflows MUST provide WCAG 2.2 AA evidence and equivalent understandable access to represented information.

### REQ-RPT-046 — Reporting Observability

Material Reporting ingestion, processing, query, export, rebuild, backfill, recovery, and reconciliation MUST be observable, correlated, attributable, and diagnosable across source, definition, request, outcome, and downstream delivery without defining numerical targets.

### REQ-RPT-047 — Proportional Audit Records

Material definition, access, export, rebuild, backfill, correction, recovery, reconciliation, privacy-sensitive, financial, and high-Risk Reporting actions MUST produce attributable proportional Audit Records, while routine low-Risk reads MUST NOT automatically receive high-Risk treatment.

### REQ-RPT-048 — Reporting Contract Boundary

Reporting Contracts MUST expose only necessary bounded versioned definitions, query, result, freshness, lineage, uncertainty, export, and recovery semantics and MUST NOT prescribe routes, methods, DTOs, schemas, tables, warehouses, classes, engines, or provider payloads.

### REQ-RPT-049 — Conditional Reporting Events

Where required by Architecture or an Approved Contract, a Reporting Domain or Integration Event MUST represent a completed Reporting-owned fact, preserve source authority, correlation, privacy, compatibility, ordering uncertainty, and replay-safe consumption, and MUST NOT invent a canonical name, payload, topic, broker, transport, or Analytics taxonomy.

### REQ-RPT-050 — Analytics Provider Boundary

Reporting MAY supply governed consent-aware analytical evidence through an approved Contract but MUST NOT select an Analytics provider, SDK, taxonomy, destination, transport, identifier strategy, or provider-specific Product meaning.

### REQ-RPT-051 — Policy Neutrality

Reporting MUST keep unresolved formula, KPI, target, attribution, analytics, export, financial, tax, Refund, Return, cancellation, Chargeback, credit, Consent, retention, legal, and other Product-policy values explicit and MUST NOT select defaults through calculation or presentation.

### REQ-RPT-052 — Implementation Neutrality

Reporting MUST NOT prescribe providers, protocols, APIs, schemas, persistence, warehouses, databases, queues, processing engines, caches, files, visualization libraries, event names, numerical limits, targets, retry counts, retention periods, or complete lifecycle graphs.

### REQ-RPT-053 — Verification Coverage

Verification evidence MUST cover every Reporting Requirement, including authority, definitions, versions, lineage, reproducibility, freshness, uncertainty, event distinctions, replay, rebuild, backfill, recovery, reconciliation, financial and privacy boundaries, Authorization, exports, accessibility, boundedness, workload isolation, observability, audit, Contracts, events, policy, and implementation neutrality.

## 3. Canonical Terminology

Reporting uses canonical terms with their `GLOSSARY.md` meanings. Reporting definition, definition version, source cutoff, coverage, rebuild scope, backfill scope, and Reporting outcome are RPT-scoped descriptions and establish no repository-wide vocabulary or complete lifecycle graph.

## 4. Domain Context

Reporting is a read-oriented Domain that produces governed analytical representations from authoritative source evidence. It owns the meaning and operational integrity of its Reporting outputs while every source Domain retains its transactional authority.

## 5. Acceptance Criteria

| Requirement | Acceptance Criteria |
| --- | --- |
| REQ-RPT-001 | Metadata and review evidence show `0.1.0 Draft`, `authoritative: false`, scope `RPT`, no Draft or repository-wide normativity, and preserved governing and Approved Domain authority. |
| REQ-RPT-002 | A Reporting record can establish only the listed RPT facts and cannot establish a source-Domain or Product-policy outcome. |
| REQ-RPT-003 | No report, dashboard, Projection, export, Metric, or analytical outcome can create, change, or prove any named external truth. |
| REQ-RPT-004 | Changing presentation, execution context, file name, route, or provider reference does not replace a governed Reporting identity or merge distinct identities. |
| REQ-RPT-005 | A definition is unavailable as governed until every applicable semantic element is explicit; absent values are not silently defaulted. |
| REQ-RPT-006 | A calculated Metric cites its definition and source, while no value is labelled a KPI or assessed against a target without Approved Product governance. |
| REQ-RPT-007 | Grouping or aggregating by an incompatible Dimension or Measure is rejected or explicitly unsupported rather than producing a misleading result. |
| REQ-RPT-008 | Every Fact identifies its source and analytical status, and changing it cannot change the authoritative source record. |
| REQ-RPT-009 | Funnel output is produced only from its declared steps and population and cannot imply that every Customer follows or must follow the sequence. |
| REQ-RPT-010 | Without an Approved attribution rule the result remains unassigned or unsupported; correlated activity alone produces no attributed outcome. |
| REQ-RPT-011 | Results expose the applicable definition version, and a material definition change leaves prior version meaning reproducible rather than silently replacing it. |
| REQ-RPT-012 | An authorized reviewer can identify the definition and source context behind a historical result even after current definitions or sources change. |
| REQ-RPT-013 | A result can be traced to its permitted sources and definition, while inaccessible data and infrastructure details remain undisclosed. |
| REQ-RPT-014 | Contract review shows source meanings are preserved; absent or stale evidence cannot become an authoritative current answer. |
| REQ-RPT-015 | Failure, delay, or replay in Reporting cannot block, approve, reverse, or mutate an authoritative transactional outcome. |
| REQ-RPT-016 | The same occurrence type is classified by its governed category and only source-authoritative evidence can prove Domain truth. |
| REQ-RPT-017 | Consumers can distinguish current-as-defined, stale, delayed, and unknown freshness without relying on display recency alone. |
| REQ-RPT-018 | A partial or unknown result cannot be labelled complete or used as certain; consumers receive its applicable coverage and exclusion context. |
| REQ-RPT-019 | Missing evidence yields an explicit incomplete outcome, and later evidence updates only governed affected results with traceable history. |
| REQ-RPT-020 | Repeated or out-of-order evidence produces the same valid contribution or an explicit conflict, never a duplicated or regressed result. |
| REQ-RPT-021 | A conflict stays visible until governed source evidence resolves it; Reporting never selects transactional truth independently. |
| REQ-RPT-022 | Reprocessing accepted evidence does not change the correct result, while unresolved ordering or conflict produces an explicit non-success state. |
| REQ-RPT-023 | A bounded rebuild affects only its authorized declared scope, preserves newer results, and exposes completed, failed, partial, and unknown outcomes. |
| REQ-RPT-024 | A source correction is traceable to changed Reporting results and cannot directly rewrite unrelated periods, definitions, or source history. |
| REQ-RPT-025 | Recovery resumes or retries only permitted unfinished work, preserves known success, and never converts unknown effects into success. |
| REQ-RPT-026 | A discrepancy remains source-labelled and unresolved until supported evidence produces a traceable Reporting correction or confirms no change. |
| REQ-RPT-027 | Reported Order identity and Order Number remain distinct, Order Number access preserves Resource isolation, Order Item and Order Snapshot values retain their applicable history, lifecycle meaning and cancellation separation remain preserved, and Reporting cannot create or transition an Order or replace Order-owned creation or current Order authority. |
| REQ-RPT-028 | A report never converts request, redirect, client assertion, or uncertain provider evidence into Payment or Refund success and keeps each financial concept distinct. |
| REQ-RPT-029 | A financial Metric is unavailable as governed until each applicable treatment is explicit, versioned, and traceable; no default treatment is inferred. |
| REQ-RPT-030 | Mixed-Currency input remains separated unless an Approved conversion definition exists, and converted output identifies its source Currency and conversion context. |
| REQ-RPT-031 | Without Approved source evidence and policy, tax and document results remain unavailable or explicitly qualified rather than legally authoritative. |
| REQ-RPT-032 | Reported inventory results retain applicable source time and movement context and cannot adjust Stock, reserve quantity, or establish current availability. |
| REQ-RPT-033 | Reported post-purchase outcomes retain their owning source and cannot infer delivery, Return eligibility, Refund success, restocking, or cancellation from an adjacent state. |
| REQ-RPT-034 | Catalogue and content Dimensions trace to their governed source/version and cannot publish, classify, sell, place, or correct source content. |
| REQ-RPT-035 | Funnel output preserves each source outcome and cannot promote progression or client evidence into downstream commercial success. |
| REQ-RPT-036 | Joining evidence cannot merge distinct actors or grant access, and ambiguous association remains explicit rather than assigned. |
| REQ-RPT-037 | Evidence excluded by purpose, Consent, or Preference is not used; absence or ambiguity never becomes permission. |
| REQ-RPT-038 | Unauthorized or overly granular results are denied or safely minimized without exposing protected existence, values, or cross-Customer information. |
| REQ-RPT-039 | Authentication, UI visibility, labels, identifiers, Claims, or report possession alone cannot authorize any protected Reporting action. |
| REQ-RPT-040 | Dashboard actions cannot alter transactional state except by separately invoking an authorized owning-Domain capability, and displayed results expose their applicable context. |
| REQ-RPT-041 | An export is denied when required context is absent; an accepted export is attributable and cannot silently exceed its authorized scope or become source authority. |
| REQ-RPT-042 | Each export request exposes its authoritative RPT outcome and coverage; partial, failed, unavailable, expired where governed, or uncertain output cannot be presented as complete. |
| REQ-RPT-043 | Each operation accepts or derives a finite scope and handles excess or unavailable scope explicitly without a locally invented numerical limit. |
| REQ-RPT-044 | Under contention, transactional truth remains correct and Reporting returns an explicit bounded degraded outcome rather than forcing unbounded source work. |
| REQ-RPT-045 | Keyboard and assistive-technology users can operate controls and understand chart meaning, filters, freshness, failures, partial results, and export outcomes without relying on visual presentation alone. |
| REQ-RPT-046 | Operational evidence distinguishes success, failure, partial completion, delay, denial, and uncertainty and correlates them to safe source and definition context. |
| REQ-RPT-047 | Governed material actions record actor or system context, action, Resource, time, and outcome; ordinary permitted reads are not automatically over-audited. |
| REQ-RPT-048 | Contract review finds sufficient semantic versioning and outcome context with none of the prohibited implementation designs. |
| REQ-RPT-049 | Any emitted event follows a completed RPT outcome and cannot claim a source-Domain outcome or mandate an event name or mechanism. |
| REQ-RPT-050 | Absent an Approved provider and taxonomy, Reporting remains provider-neutral and no adapter response or vendor term becomes RPT semantics. |
| REQ-RPT-051 | Every policy-dependent output either consumes an Approved definition or remains unavailable, qualified, or unresolved; no calculation or label supplies a default. |
| REQ-RPT-052 | Specification review finds no prohibited mechanism, fixed value, provider, data model, formula, or mandatory traversal embedded in RPT behavior. |
| REQ-RPT-053 | Positive and negative evidence covers every listed concern and Requirement without selecting a framework, test identifier scheme, architecture, or numerical coverage target. |

## 6. Requirement Traceability

| Requirement | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- |
| REQ-RPT-001 | PRODUCT.md §§13, 17.3 | REQ-BUS-001–003, 044, 048 | REQ-ADM-001, 036 | AGENTS.md §§1, 3.6, 5; ARCHITECTURE.md §§9–10 | All Reporting consumers |
| REQ-RPT-002 | PRODUCT.md §§12.10, 18 | REQ-BUS-044 | REQ-ADM-036 | ARCHITECTURE.md §§9, 26 | Reporting, Administration |
| REQ-RPT-003 | PRODUCT.md §§13, 18, 24 | REQ-BUS-044, 048 | REQ-PRD-001–002; REQ-CAT-001–002; REQ-CUS-001–002, 038; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-PAY-002–003, 043; REQ-SHP-002–003, 041; REQ-CHK-002–003, 043; REQ-ORD-002–003, 051; REQ-RET-002–003, 047; REQ-NTF-002–004, 055; REQ-CMS-002–004, 039; REQ-ADM-003, 036 | AGENTS.md §§3.6, 5; ARCHITECTURE.md §§9, 26 | All Domains, Reporting |
| REQ-RPT-004 | PRODUCT.md §§17.4, 18 | REQ-BUS-044 | — | DATABASE.md §42; ARCHITECTURE.md §26 | Reporting, Audit |
| REQ-RPT-005 | PRODUCT.md §§12.10, 18, 24 | REQ-BUS-044, 048 | REQ-PRC-006, 023 | GLOSSARY.md §26; DOCUMENTATION-STANDARDS.md §§7–9 | Product, Finance, Reporting |
| REQ-RPT-006 | PRODUCT.md §§18, 24 | REQ-BUS-044, 048 | — | GLOSSARY.md §26 | Product, Reporting |
| REQ-RPT-007 | PRODUCT.md §§18, 33 | REQ-BUS-044 | REQ-PRC-006 | GLOSSARY.md §26 | Reporting consumers |
| REQ-RPT-008 | PRODUCT.md §§13, 18 | REQ-BUS-044 | REQ-ORD-017, 051 | GLOSSARY.md §26; ARCHITECTURE.md §26 | Analytics, Reporting |
| REQ-RPT-009 | PRODUCT.md §§9, 18, 33 | REQ-BUS-044, 048 | REQ-CART-003; REQ-CHK-003, 043 | GLOSSARY.md §26 | Product, Analytics |
| REQ-RPT-010 | PRODUCT.md §§18, 24, 33 | REQ-BUS-040, 044, 048 | REQ-CUS-020–021, 038; REQ-CMS-039 | GLOSSARY.md §26; SECURITY-STANDARDS.md §§12, 35 | Product, Marketing, Reporting |
| REQ-RPT-011 | PRODUCT.md §§17.4, 18 | REQ-BUS-044 | REQ-PRC-019, 023 | DOCUMENTATION-STANDARDS.md §§7–9; DATABASE.md §42 | Reporting, Audit |
| REQ-RPT-012 | PRODUCT.md §§17.4, 18 | REQ-BUS-044 | REQ-ORD-017–020, 023–025 | DATABASE.md §§42, 48–52 | Finance, Audit, Reporting |
| REQ-RPT-013 | PRODUCT.md §§13, 18, 22 | REQ-BUS-043–044 | REQ-ADM-012, 035–036 | ARCHITECTURE.md §§14, 26; SECURITY-STANDARDS.md §35 | Reporting, Audit, Operations |
| REQ-RPT-014 | PRODUCT.md §§13, 17.3 | REQ-BUS-044, 046 | REQ-PRD-052; REQ-CAT-041; REQ-CUS-047; REQ-IDN-045; REQ-INV-035; REQ-CART-031; REQ-PRC-033; REQ-PAY-039; REQ-SHP-037; REQ-CHK-038; REQ-ORD-045; REQ-RET-042; REQ-NTF-050; REQ-CMS-045; REQ-ADM-010–011, 046 | ARCHITECTURE.md §§10.2, 39 | All source Domains, Reporting |
| REQ-RPT-015 | PRODUCT.md §§13, 18 | REQ-BUS-044–045 | REQ-ORD-051; REQ-NTF-055; REQ-ADM-036 | ARCHITECTURE.md §§9, 26 | Transactional Domains, Operations |
| REQ-RPT-016 | PRODUCT.md §§13, 18, 33 | REQ-BUS-039, 044, 046 | REQ-CUS-046; REQ-IDN-046; REQ-INV-034; REQ-CART-030; REQ-PRC-032; REQ-PAY-038; REQ-SHP-036; REQ-CHK-039; REQ-ORD-046; REQ-RET-043; REQ-NTF-051; REQ-CMS-046; REQ-ADM-047 | EVENTS.md; GLOSSARY.md §§14, 26, 42 | Reporting, Analytics, event consumers |
| REQ-RPT-017 | PRODUCT.md §§17.2, 18 | REQ-BUS-042, 044 | REQ-CUS-038; REQ-NTF-055; REQ-CMS-039; REQ-ADM-036 | ARCHITECTURE.md §26 | Reporting consumers |
| REQ-RPT-018 | PRODUCT.md §§17.2, 18, 23 | REQ-BUS-042, 044–045 | REQ-ADM-020 | ARCHITECTURE.md §§20, 26 | Reporting consumers, Operations |
| REQ-RPT-019 | PRODUCT.md §§17.2–17.4, 23 | REQ-BUS-042, 044–045 | REQ-PAY-021, 024; REQ-SHP-026, 029 | ARCHITECTURE.md §§20, 26 | Reporting, Operations |
| REQ-RPT-020 | PRODUCT.md §§17.3–17.4, 23 | REQ-BUS-035, 044 | REQ-PAY-022–024; REQ-NTF-031, 042 | EVENTS.md; DATABASE.md §52 | Reporting, Operations |
| REQ-RPT-021 | PRODUCT.md §§17.2–17.4 | REQ-BUS-042, 044 | REQ-CMS-040, 044; REQ-ORD-040–041 | ARCHITECTURE.md §§20, 26 | Reporting, source Domains |
| REQ-RPT-022 | PRODUCT.md §§17.3, 23 | REQ-BUS-035, 044–045 | REQ-NTF-042, 044; REQ-CMS-041, 044 | EVENTS.md; ARCHITECTURE.md §26 | Reporting, Operations |
| REQ-RPT-023 | PRODUCT.md §§17.2–17.4, 23 | REQ-BUS-035–036, 044–045 | REQ-ADM-016–022 | DATABASE.md §§48–52; SECURITY-STANDARDS.md §27 | Reporting, Operations |
| REQ-RPT-024 | PRODUCT.md §§17.4, 18 | REQ-BUS-044 | REQ-ORD-019–020; REQ-CMS-018–019 | ARCHITECTURE.md §26; DATABASE.md §42 | Reporting, Audit |
| REQ-RPT-025 | PRODUCT.md §§17.2–17.4, 23 | REQ-BUS-036, 042, 045 | REQ-ADM-017–021, 050 | ARCHITECTURE.md §§20, 26 | Reporting, Operations |
| REQ-RPT-026 | PRODUCT.md §§17.4, 18, 23 | REQ-BUS-043–045 | REQ-INV-030; REQ-PRC-028; REQ-PAY-024; REQ-SHP-029; REQ-ORD-041; REQ-RET-039; REQ-NTF-044; REQ-CMS-044; REQ-ADM-022 | ARCHITECTURE.md §§20, 26 | Reporting, Operations, Audit |
| REQ-RPT-027 | PRODUCT.md §§12.10, 15.2, 18 | REQ-BUS-025, 044 | REQ-ORD-002–020, 034–040, 051 | ARCHITECTURE.md §§9, 26 | Product, Finance, Operations |
| REQ-RPT-028 | PRODUCT.md §§12.10, 15.5, 18 | REQ-BUS-025–030, 044 | REQ-PAY-003–025, 034–036, 043–044 | ARCHITECTURE.md §§9, 26 | Finance, Operations, Product |
| REQ-RPT-029 | PRODUCT.md §§12.6, 12.10, 18, 24 | REQ-BUS-014–016, 044, 048 | REQ-PRC-006–015, 019, 021–023; REQ-PAY-034–036, 044; REQ-ORD-023–025, 037–039; REQ-RET-027–035; REQ-SHP-006–008, 042 | GLOSSARY.md §§26, 29 | Finance, Product, Reporting |
| REQ-RPT-030 | PRODUCT.md §§12.6, 18, 24 | REQ-BUS-014–016, 044 | REQ-PRC-006; REQ-PAY-008; REQ-SHP-008; REQ-CHK-015; REQ-ORD-024 | GLOSSARY.md §§5, 29 | Finance, Reporting |
| REQ-RPT-031 | PRODUCT.md §§16.10, 20, 24 | REQ-BUS-041, 044, 048 | REQ-PRC-013; REQ-PAY-044; REQ-SHP-042; REQ-CHK-037; REQ-ORD-025; REQ-RET-035 | SECURITY-STANDARDS.md §35 | Finance, Legal, Reporting |
| REQ-RPT-032 | PRODUCT.md §§12.7, 12.10, 18 | REQ-BUS-013, 044 | REQ-INV-002–020, 027–030 | ARCHITECTURE.md §§9, 25.2, 26 | Inventory, Operations, Product |
| REQ-RPT-033 | PRODUCT.md §§12.10, 15.3–15.5, 18 | REQ-BUS-025–030, 044 | REQ-SHP-002–032, 041–042; REQ-RET-002–037, 047 | ARCHITECTURE.md §§9, 26 | Operations, Finance, Product |
| REQ-RPT-034 | PRODUCT.md §§12.2, 12.10, 18 | REQ-BUS-009–012, 038, 044 | REQ-PRD-001–031, 045, 051; REQ-CAT-001–028, 031, 037; REQ-CMS-002–025, 039 | ARCHITECTURE.md §§9, 26 | Product, Merchandising, Marketing |
| REQ-RPT-035 | PRODUCT.md §§9, 18, 33 | REQ-BUS-017–024, 044 | REQ-CART-002–003, 013–021; REQ-CHK-002–033, 043 | ARCHITECTURE.md §26 | Product, Analytics |
| REQ-RPT-036 | PRODUCT.md §§12.8, 16.4–16.5, 18 | REQ-BUS-008, 031–034, 040, 044 | REQ-CUS-001–012, 038–041; REQ-IDN-002–010, 027–028, 037 | SECURITY-STANDARDS.md §§10–12, 35 | Customer, Identity, Reporting |
| REQ-RPT-037 | PRODUCT.md §§16.5, 18, 24 | REQ-BUS-040, 044, 048 | REQ-CUS-018–021, 038; REQ-NTF-012–015, 055 | SECURITY-STANDARDS.md §§12, 35 | Privacy, Marketing, Reporting |
| REQ-RPT-038 | PRODUCT.md §§5.5, 16.5, 18 | REQ-BUS-031–034, 040, 053 | REQ-CUS-005, 039–041; REQ-IDN-006, 043–044; REQ-PAY-031–032; REQ-ADM-035, 037–038 | SECURITY-STANDARDS.md §§7, 10–12, 27, 35 | Security, Privacy, Reporting |
| REQ-RPT-039 | PRODUCT.md §§12.9, 16.4, 18 | REQ-BUS-031–034 | REQ-IDN-020, 024–028, 031, 037; REQ-ADM-005–010 | SECURITY-STANDARDS.md §§10–12, 27 | Identity, Reporting, Administration |
| REQ-RPT-040 | PRODUCT.md §§12.9–12.10, 18 | REQ-BUS-032, 044 | REQ-NTF-055; REQ-CMS-039; REQ-ADM-008, 010, 036 | UI.md; SECURITY-STANDARDS.md §§10–12 | Administration, Reporting consumers |
| REQ-RPT-041 | PRODUCT.md §§12.10, 16.5, 18, 24 | REQ-BUS-040, 044, 053–054 | REQ-CUS-029, 041; REQ-NTF-055; REQ-CMS-039; REQ-ADM-035, 049 | SECURITY-STANDARDS.md §§12, 27, 35 | Reporting, Privacy, Audit |
| REQ-RPT-042 | PRODUCT.md §§17.2, 18, 23 | REQ-BUS-042, 044–045 | REQ-ADM-020, 035 | API.md §§48, 54–58 | Reporting consumers, Operations |
| REQ-RPT-043 | PRODUCT.md §§23, 31 | REQ-BUS-037, 045, 053 | REQ-CUS-044; REQ-INV-032; REQ-PRC-030; REQ-PAY-041; REQ-SHP-039; REQ-ORD-049; REQ-RET-045; REQ-NTF-053; REQ-CMS-047; REQ-ADM-043 | PERFORMANCE.md; API.md §§37–61 | Reporting, Operations |
| REQ-RPT-044 | PRODUCT.md §§5.6, 20, 23 | REQ-BUS-042, 045 | REQ-ADM-043–044 | ARCHITECTURE.md §§26, 31; PERFORMANCE.md | Operations, transactional Domains |
| REQ-RPT-045 | PRODUCT.md §§6.4, 29–30 | REQ-BUS-042 | REQ-IDN-047; REQ-CMS-034; REQ-ADM-042 | ACCESSIBILITY.md §§1–32; DESIGN-SYSTEM.md; UI.md | Reporting frontend, Accessibility |
| REQ-RPT-046 | PRODUCT.md §§18, 23, 35 | REQ-BUS-043–045 | REQ-IDN-048; REQ-PAY-041; REQ-SHP-039; REQ-CHK-041; REQ-ORD-049; REQ-RET-045; REQ-NTF-053; REQ-CMS-047; REQ-ADM-044 | ARCHITECTURE.md §§20, 31; SECURITY-STANDARDS.md §27 | Operations, Reporting |
| REQ-RPT-047 | PRODUCT.md §§17.4, 18, 31 | REQ-BUS-033–034, 043 | REQ-CUS-045; REQ-IDN-049; REQ-INV-033; REQ-PRC-031; REQ-PAY-042; REQ-SHP-040; REQ-CHK-042; REQ-ORD-050; REQ-RET-046; REQ-NTF-054; REQ-CMS-048; REQ-ADM-045 | SECURITY-STANDARDS.md §§7.9, 27; DATABASE.md §42 | Audit, Security, Reporting |
| REQ-RPT-048 | PRODUCT.md §§13, 37 | REQ-BUS-037, 044, 046 | REQ-PRD-052; REQ-CAT-041; REQ-CUS-047; REQ-IDN-045; REQ-INV-035; REQ-CART-031; REQ-PRC-033; REQ-PAY-039; REQ-SHP-037; REQ-CHK-038; REQ-ORD-045; REQ-RET-042; REQ-NTF-050; REQ-CMS-045; REQ-ADM-046 | API.md §§37–61; ARCHITECTURE.md §§12–16, 39 | Reporting clients, source Domains |
| REQ-RPT-049 | PRODUCT.md §§13, 18, 24 | REQ-BUS-039, 044, 046 | REQ-CUS-046; REQ-IDN-046; REQ-INV-034; REQ-CART-030; REQ-PRC-032; REQ-PAY-038; REQ-SHP-036; REQ-CHK-039; REQ-ORD-046; REQ-RET-043; REQ-NTF-051; REQ-CMS-046; REQ-ADM-047 | EVENTS.md; ARCHITECTURE.md §14 | Event consumers, Reporting |
| REQ-RPT-050 | PRODUCT.md §§18, 24, 33 | REQ-BUS-040, 044, 046, 048 | REQ-CUS-038; REQ-ADM-036 | ARCHITECTURE.md §§12–14; SECURITY-STANDARDS.md §35 | Analytics adapters, Product |
| REQ-RPT-051 | PRODUCT.md §24 | REQ-BUS-044, 048, 052–054 | REQ-PRC-012–015, 021–022; REQ-PAY-033–036, 044; REQ-ADM-051 | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9 | Product, Finance, Reporting |
| REQ-RPT-052 | PRODUCT.md §§24, 37 | REQ-BUS-037, 044, 046, 048 | REQ-ADM-052 | AGENTS.md §§3.2, 3.10, 5; DOCUMENTATION-STANDARDS.md §§7–9 | Engineering, Architecture |
| REQ-RPT-053 | PRODUCT.md §§20, 31, 34 | REQ-BUS-040–045 | REQ-PRD-044; REQ-CAT-043; REQ-CUS-049; REQ-IDN-050; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-CHK-044; REQ-ORD-052; REQ-RET-048; REQ-NTF-056; REQ-CMS-051; REQ-ADM-053 | TESTING-STANDARDS.md; SECURITY-STANDARDS.md; ACCESSIBILITY.md | Engineering, Product, Security, Accessibility |

## 7. Open Product Decisions

`PRODUCT.md` contains exactly 30 Open Product Decisions. The following 15 are materially relevant to Reporting, remain in exact source order, and are unresolved by this Draft.

| Product Decision | Reporting boundary affected |
| --- | --- |
| Initial payment methods and provider. | Determines source classifications and financial Dimensions; RPT selects neither method nor provider. |
| Shipping provider, service levels, delivery areas, and fee policy. | Constrains Shipping Dimensions and fee interpretation; RPT does not define provider, service, area, or fee policy. |
| Free-delivery threshold and promotional treatment. | Affects delivery and Promotion reporting definitions; RPT does not choose the threshold or treatment. |
| Tax-inclusive display and invoice requirements. | Constrains financial presentation and document reporting; RPT does not establish tax or invoice policy. |
| Cancellation eligibility and cutoff policy. | Determines which source outcomes may be classified as eligible cancellations; RPT does not decide eligibility or cutoff. |
| Returns, exchanges, and refund policy. | Controls governed post-purchase classifications and financial treatment; RPT does not define any such policy. |
| Back-order and pre-order support. | Determines whether related Product, Inventory, Cart, and Order Dimensions exist; RPT does not enable the capability. |
| Voucher and promotion stacking policy. | Affects governed Promotion and Discount interpretation; RPT does not decide stacking. |
| Marketing-consent and communication-preference model. | Constrains eligible Customer analytics and communications reporting; RPT cannot infer Consent or Preference. |
| Initial analytics provider and event taxonomy. | Determines future adapter and taxonomy inputs; RPT remains provider- and taxonomy-neutral. |
| Initial reporting and export requirements. | Determines approved report definitions, audiences, purposes, and export scope; RPT invents none. |
| South African tax-display, invoice, and credit-note policy. | Constrains tax and commercial-document measures; RPT provides no legal or tax interpretation. |
| Fraud-screening approach and manual-review workflow. | Determines whether governed fraud classifications may be reported; RPT does not define screening or review policy. |
| Gift cards, store credit, and promotional credit policy. | Determines whether and how governed credit sources enter financial definitions; RPT creates no credit semantics. |
| Product launch date, release scope, and post-launch support window. | Constrains release reporting periods and approved success measures; RPT sets no dates, scope, targets, or support window. |

## 8. Risks

| Risk | Control |
| --- | --- |
| Projection becomes transactional authority | Label and enforce RPT results as non-authoritative and prohibit Reporting write paths into source Domains. |
| Metric or formula drift | Require explicit owned definitions, material versioning, and reproducible historical interpretation. |
| Incompatible definition versions are combined | Carry definition identity and reject or qualify cross-version aggregation. |
| Stale results appear current | Expose applicable freshness, source cutoff, and staleness or unknown state. |
| Partial data appears complete | Retain coverage, exclusion, and partial-result semantics in every affected result. |
| Missing evidence is fabricated | Represent missing contributions explicitly and reconcile only from governed source evidence. |
| Duplicate evidence inflates Measures | Make Projection contribution duplicate-safe and traceable to source evidence. |
| Reordered evidence regresses results | Detect ordering conflicts and preserve later accepted Reporting outcomes. |
| Source provenance is lost | Retain lineage, definition version, temporal context, and transformation provenance. |
| Financial results are misstated | Require Approved treatment for Currency, tax, discounts, shipping, cancellation, Return, Refund, Chargeback, and credits. |
| Mixed Currency is aggregated silently | Keep Currencies separate unless an Approved conversion definition and context exist. |
| Analytics Event is treated as Domain truth | Preserve event categories and require source-authoritative evidence for business outcomes. |
| Cross-Customer disclosure | Enforce object and population isolation, minimization, masking, and enumeration resistance. |
| Sensitive Data leaks through reports | Protect all dashboard, query, export, log, telemetry, event, and evidence surfaces. |
| Unauthorized export is produced | Require contextual Authorization, governed purpose and scope, and attributable outcomes. |
| Consent-ineligible evidence is analyzed | Apply governed purpose, Consent, Preference, and classification boundaries before use. |
| Dashboard action mutates source state | Separate presentation from owning-Domain invocation and forbid direct source mutation. |
| Rebuild corrupts accepted projections | Bound scope and versions, preserve newer truth, and expose partial or uncertain outcomes. |
| Backfill rewrites unrelated history | Limit backfill to declared source and temporal scope with traceable effects. |
| Reporting workloads harm transactions | Require bounded work and governed degradation without sacrificing transactional correctness. |
| Unbounded queries exhaust resources | Require finite scopes and explicit rejection or degradation without fixed local thresholds. |
| Definition changes reinterpret history | Preserve prior definition versions and reproducible historical results. |
| Tax or legal reporting is mistaken for advice | Require qualified policy sources and explicit non-authority for legal or tax conclusions. |
| Provider terminology becomes Product meaning | Keep providers behind governed Contracts and preserve project-owned Reporting semantics. |
| Material actions lack evidence | Create attributable proportional Audit Records for governed high-Risk Reporting actions. |
| Routine reads are over-audited | Keep audit proportional and do not automatically classify ordinary permitted reads as high-Risk. |
| Implementation design becomes RPT policy | Exclude warehouses, schemas, engines, queues, APIs, tools, targets, and retention mechanics from Domain rules. |

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
- `specifications/domains/admin/admin-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-07 | Draft | Initial comprehensive Reporting Domain Specification. |

## 11. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, scope is `RPT`, and the Draft is neither normative nor repository-wide authority;
2. Reporting owns only Reporting-specific definitions, Projections, outputs, lineage, and operational state while every source Domain retains authority;
3. Metric, KPI, Dimension, Measure, Fact, Funnel, Attribution, event-category, transactional, and analytical meanings remain distinct and policy-neutral;
4. source lineage, definition versions, historical reproducibility, freshness, completeness, uncertainty, replay, rebuild, backfill, recovery, and reconciliation remain explicit;
5. financial, Customer, Consent, privacy, Authorization, dashboard, export, workload, accessibility, observability, and audit boundaries remain intact;
6. exactly 53 Requirements are unique, sequential, gap-free from `REQ-RPT-001` through `REQ-RPT-053`, clause-complete, and implementation-neutral;
7. exactly 53 individually authored Acceptance Criteria rows map one-to-one and introduce no new behavior;
8. exactly 53 semantically direct traceability rows map one-to-one and every cited identifier exists;
9. all 30 Product Decisions were reviewed, the 15 material decisions remain exact, source-ordered, and unresolved;
10. all 27 Risks are material, non-duplicative, and have distinct implementation-neutral controls;
11. all 38 Related Documents exist and are relevant;
12. Revision History contains exactly one `0.1.0 Draft` row;
13. no Glossary amendment is required;
14. Markdown, headings, tables, UTF-8, whitespace, final newline, prohibited markers, and structure pass; and
15. Git scope contains exactly one untracked `specifications/domains/reporting/reporting-domain.md`, with no tracked, staged, or unrelated changes.
