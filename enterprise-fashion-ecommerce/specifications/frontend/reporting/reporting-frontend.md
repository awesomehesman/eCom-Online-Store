---
title: Reporting Frontend Specification
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-10
authoritative: false
---

# Reporting Frontend Specification

## 1. Purpose

This Specification defines implementation-neutral requirements for frontend presentation and interaction with governed Reporting capabilities.

This document uses scope code `FRP`. As an Approved specification, its Requirements are normative only within Reporting frontend scope and are not repository-wide authority. It remains subordinate to governing sources, Product and Business Requirements, the Approved Reporting Domain, applicable Approved source Domains, FEB, and frontend boundary Specifications, and resolves no Open Product Decision.

## 2. Scope and Authority

FRP owns only frontend presentation and interaction for governed report discovery, report identity, definitions, versions, filters, periods, Dimensions, Measures, comparisons, summaries, cards, tables, charts, drill-down, provenance, freshness, completeness, uncertainty, refresh, and Reporting-owned exports.

Reporting owns governed definitions, Reporting Projections, processing outcomes, freshness, completeness, lineage, recovery, reconciliation evidence, and export outcomes. Source Domains retain authority over underlying transactional and operational Facts. Filter, period, comparison, Dimension, segment, drill-down, refresh, navigation, local calculation, chart, table, card, cache, timeout, analytics, or UI state cannot establish Reporting or source-Domain truth.

FRP does not own or redefine Product, Category, Inventory, Pricing, Customer, Identity, Cart, Checkout, Payment, Order, Shipping, Return, CMS, Notifications, Search, Administration, or Analytics truth. It does not absorb FSC, FCD, FPE, FCA, FCP, FPP, or FAD behavior and cannot mutate, repair, or reconcile source-Domain truth.

## 3. Terminology and Reporting Frontend Model

Canonical terms retain their `GLOSSARY.md` meanings, including Reporting Projection, Measure, Dimension, Fact, Metric, KPI, Funnel, Attribution, Money, Currency, Staff User, Principal, Session, Authentication, Authorization, Role, Permission, Claims, Scope, Customer, Resource, Sensitive Data, Secret, Contract, Audit Record, Domain Event, Integration Event, and Analytics Event.

For FRP only, **reporting presentation context** means frontend-only, non-canonical, non-authoritative state used to present permitted Reporting evidence and interaction context. It cannot establish report truth, source-Domain truth, entitlement, filter validity, Reporting completion, export completion, reconciliation outcome, or business Metric truth. No Glossary amendment is required.

## 4. Requirements

### REQ-FRP-001 — Lifecycle, Authority, and Scope

FRP MUST govern only Reporting frontend behavior under scope `FRP`, preserve governing-source, Product, Business Requirement, Reporting Domain, source-Domain, FEB, and frontend-boundary precedence, and MUST NOT claim repository-wide authority.

### REQ-FRP-002 — FEB Inheritance

FRP MUST consume every materially applicable Approved FEB Requirement without copying, weakening, conflicting with, or transferring ownership of inherited behavior; reversible provisional presentation Requirements apply only where separately governed.

### REQ-FRP-003 — Frontend Boundary Preservation

FRP MUST NOT own or redefine FSC, FCD, FPE, FCA, FCP, FPP, or FAD behavior; navigation, embedding, filter context, summary, export, or handoff MUST NOT transfer their authority.

### REQ-FRP-004 — Reporting Domain Authority

FRP MUST present Reporting-owned definitions, projections, processing, freshness, completeness, lineage, recovery, reconciliation, and export outcomes only from governed Reporting evidence and MUST NOT create or alter that truth.

### REQ-FRP-005 — Source-Domain Authority

FRP MUST preserve each source Domain's authority over underlying transactional and operational Facts and MUST NOT use a report, aggregate, visualization, export, or local calculation to rewrite or supersede source truth.

### REQ-FRP-006 — Protected Reporting Entry

Every protected Reporting entry MUST depend on trusted Authentication and current server-side contextual Authorization and MUST disclose no protected report or Resource merely because a route, identifier, filter, or cached context exists.

### REQ-FRP-007 — Contextual and Field Authorization

Every protected report, field, property, Dimension, Measure, drill-down, and export MUST preserve current server-side Authorization for the Principal, Session, purpose, Resource, action, property, and Reporting state.

### REQ-FRP-008 — UI Entitlement Non-Authority

UI visibility, chart or table presence, deep links, identifiers, filters, Role labels, Permissions, Claims, Scope, feature flags, caches, or client state MUST NOT grant entitlement or authorize Reporting access.

### REQ-FRP-009 — Least Privilege and Stale Privilege

FRP MUST expose only purpose-required authorized evidence and MUST revalidate material access when Authentication, Session, assignment, Authorization, report, or source context may have changed rather than preserve stale privilege.

### REQ-FRP-010 — Isolation and Inference Resistance

Reporting presentation, client state, caches, filters, comparisons, segments, drill-down, and exports MUST preserve applicable Principal, Session, Customer, purpose, and Resource isolation and MUST resist unauthorized aggregation or inference disclosure without inventing cohort thresholds.

### REQ-FRP-011 — Protected-Resource Concealment

FRP MUST resist enumeration and disclose no inaccessible report, Resource, field, segment, sensitive state, or internal detail through identifiers, routes, filters, validation, errors, timing, charts, tables, drill-down, or exports.

### REQ-FRP-012 — Sensitive Data and Privacy

FRP MUST minimize and purpose-bind Sensitive Data across presentation, client state, routes, filters, errors, logs, telemetry, Analytics Events, drill-down, and exports while preserving Consent, Preference, and privacy authority.

### REQ-FRP-013 — Secret and Raw Evidence Exclusion

Secrets, credentials, tokens, raw provider secrets, inappropriate raw Payment data, and internal security detail MUST NOT appear in reports, filters, routes, charts, tables, exports, errors, logs, telemetry, or analytics evidence.

### REQ-FRP-014 — Cache and Export Security

Cached Reporting evidence and export interaction MUST preserve current Authorization, isolation, minimization, provenance, and freshness and MUST NOT extend access after privilege or purpose changes.

### REQ-FRP-015 — Report Discovery and Navigation

FRP MUST provide authorized report discovery and predictable navigation while preserving report identity and context; discovery visibility, navigation, and embedded links MUST NOT establish entitlement or report truth.

### REQ-FRP-016 — Stable Report Identity

Each presented report or Reporting Projection MUST retain stable governed identity distinct from title, route, filter state, definition version, source identifier, export, or visualization.

### REQ-FRP-017 — Definition Identity and Version

FRP MUST present sufficient governed report-definition identity, meaning, version, and applicability to distinguish changed or historical definitions and MUST NOT define or silently reinterpret them.

### REQ-FRP-018 — Measure and Dimension Integrity

FRP MUST preserve governed Measure, Dimension, unit, aggregation, applicability, and relationship semantics and MUST NOT infer or locally redefine them from labels, layout, filters, or visualization type.

### REQ-FRP-019 — Reporting Fact Distinction

FRP MUST keep Reporting Facts and projections distinct from source-Domain Facts, raw events, Analytics Events, and frontend state and MUST NOT present one as another.

### REQ-FRP-020 — Metric and KPI Non-Invention

FRP MUST present a Metric or KPI only where a governed definition establishes it and MUST NOT invent formulas, targets, thresholds, success criteria, or business interpretation.

### REQ-FRP-021 — Funnel and Attribution Non-Invention

FRP MUST present Funnel or Attribution evidence only from governed definitions and MUST NOT create stages, windows, rules, weights, classifications, or causal claims.

### REQ-FRP-022 — Lineage and Provenance

Material Reporting evidence MUST retain sufficient report, definition, source, period, applicability, processing, freshness, and correlation context for provenance and truthful interpretation.

### REQ-FRP-023 — Historical Reproducibility

FRP MUST preserve governed historical definition, version, source, period, Currency, and correction context needed to interpret or reproduce historical Reporting evidence without substituting current definitions.

### REQ-FRP-024 — Projection and Local-Calculation Non-Authority

Reporting Projections, dashboards, frontend transformations, derived display values, charts, cards, tables, caches, and local calculations MUST remain non-authoritative for source-Domain truth and MUST NOT create business Metric truth.

### REQ-FRP-025 — Freshness and Staleness

FRP MUST expose or consume sufficient freshness, as-of, applicability, and revalidation context and MUST distinguish stale evidence from current confirmation.

### REQ-FRP-026 — Completeness, Partial, Late, and Missing Evidence

FRP MUST qualify completeness, partial coverage, late or missing sources, gaps, uncertainty, and known limitations and MUST NOT present incomplete evidence as complete or precise.

### REQ-FRP-027 — Source Correction Effects

FRP MUST preserve governed correction, supersession, and restatement evidence and MUST NOT silently overwrite historical interpretation or imply that frontend refresh repairs source truth.

### REQ-FRP-028 — Domain Reporting Boundaries

Order, Payment, Refund, Pricing, Inventory, Shipping, Return, Product, Category, CMS, Cart, Checkout, Customer, Identity, Notification, Search, Administration, and fraud evidence MUST retain its owning-Domain meaning and MUST NOT become FRP-owned truth or mutation authority.

### REQ-FRP-029 — Money and Currency Integrity

FRP MUST preserve governed Money, Currency, conversion context where supplied, aggregation applicability, and historical meaning and MUST NOT perform implicit conversion or invent financial totals.

### REQ-FRP-030 — Tax and Commercial-Document Boundary

FRP MAY present governed Tax, invoice, Credit Note, and commercial-document reporting evidence but MUST NOT determine legal meaning, calculation, eligibility, content, issuance, retention, or policy.

### REQ-FRP-031 — Filter and Period Intent

Filter, search, period, date-range, and refresh selections MUST remain explicit frontend intent subject to Reporting validation and MUST NOT establish filter validity, source truth, completion, or a mandatory period policy.

### REQ-FRP-032 — Comparison, Dimension, and Segment Intent

Comparison, Dimension, grouping, and segment selections MUST preserve governed definitions and applicability and MUST NOT invent cohorts, classifications, relationships, or business meaning.

### REQ-FRP-033 — Summary and Card Presentation

Summaries and cards MUST retain governed definition, value, unit, period, provenance, freshness, completeness, and uncertainty and MUST NOT use prominence or formatting as proof of importance or truth.

### REQ-FRP-034 — Table Presentation

Reporting tables MUST preserve governed columns, units, row identity, relationships, sort and filter meaning, provenance, freshness, partiality, and bounded traversal without becoming source truth.

### REQ-FRP-035 — Chart and Visualization Presentation

Charts and data visualizations MUST preserve governed values, scales, units, periods, labels, legends, relationships, provenance, completeness, and uncertainty without misleading encoding, unsupported precision, or visual-only meaning.

### REQ-FRP-036 — Drill-Down Boundary

Drill-down MUST preserve current contextual Authorization, parent context, definition, source, applicability, and freshness and MUST NOT infer detail, broaden access, or mutate source or Reporting truth.

### REQ-FRP-037 — Pagination and Bounded Collections

Pagination, sorting, filtering, and collection traversal MUST preserve stable report context, ordering semantics where governed, completeness qualification, and bounded delivery without implying that a page is the complete dataset.

### REQ-FRP-038 — Dashboard Non-Authority

Dashboard composition, placement, prominence, alerts, cards, charts, or local status MUST NOT establish priority, operational outcome, source truth, Reporting definition, or Administration workflow authority.

### REQ-FRP-039 — Asynchronous Supersession

Stale, cancelled, delayed, reordered, or superseded results from changed filters, periods, comparisons, Dimensions, drill-down, or refresh MUST NOT replace newer relevant intent or presentation state.

### REQ-FRP-040 — Duplicate, Reordered, and Conflicting Evidence

Duplicate interaction and duplicate, reordered, conflicting, or corrected Reporting evidence MUST preserve identity and ordering context and MUST NOT multiply intended effects or fabricate a reconciled result.

### REQ-FRP-041 — Presentation States

Each material FRP region MUST provide distinguishable initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states where applicable without defining mandatory lifecycle traversal.

### REQ-FRP-042 — Timeout and Unknown Effect

Timeout, lost response, partial observation, stopped observation, and unknown report or export effect MUST remain distinguishable from rejection and completion and require governed revalidation before retry or assertion.

### REQ-FRP-043 — Reporting Recovery and Reconciliation Boundary

FRP MAY present Reporting discrepancies and invoke explicitly supported Reporting recovery or reconciliation intent, but MUST NOT define processing mechanisms or repair, reconcile, or mutate source-Domain truth.

### REQ-FRP-044 — Degradation and Failure Containment

Validation, Authorization, source, processing, export, provider, and optional-dependency failures MUST remain truthful and isolated without corrupting governed evidence, bypassing safeguards, or blocking otherwise available safe recovery.

### REQ-FRP-045 — Export Intent Non-Authority

Export interaction MUST remain explicit authorized intent associated with the applicable report, definition, filters, period, purpose, and Resource and MUST NOT be presented as accepted or complete before Reporting-owned evidence confirms it.

### REQ-FRP-046 — Export Outcome Integrity

FRP MUST present pending, processing, completed, failed, unavailable, partial, expired, and uncertain export evidence exactly as governed, preserving identity, provenance, freshness, correlation, and safe retrieval context without inventing an export lifecycle.

### REQ-FRP-047 — Duplicate Export and Policy Boundary

Repeated, retried, replayed, or restored export interaction MUST NOT intentionally request duplicate effect or claim success, and FRP MUST NOT define export format, size, destination, retention, delivery, or approval policy.

### REQ-FRP-048 — Responsive and Content-Resilient Reporting

FRP MUST preserve meaning and operation across supported viewports, reflow, zoom, orientation, input modalities, localization, long labels, large values, and content extremes without prescribing layout or breakpoints.

### REQ-FRP-049 — Keyboard, Focus, and Dynamic Status

FRP MUST provide WCAG 2.2 AA keyboard operation, visible and logical focus, structural navigation, and perceivable loading, refresh, filter, error, uncertainty, recovery, and export status.

### REQ-FRP-050 — Accessible Tables and Visualizations

Tables, charts, legends, comparisons, filters, drill-down, and exported-report interactions MUST provide programmatic names and relationships, color-independent meaning, assistive-technology access, and non-visual equivalents for material visual information.

### REQ-FRP-051 — Bounded Reporting Frontend Work

Large datasets, tables, charts, filters, sorting, pagination, drill-down, refresh, concurrent requests, exports, reactive work, and background work MUST remain bounded by governed inputs and applicable performance evidence without FRP inventing numerical limits or mechanisms.

### REQ-FRP-052 — Telemetry, Reporting, Audit, and Event Separation

FRP MUST distinguish frontend operational telemetry, correlation evidence, Reporting evidence, Audit Records, Domain Events, Integration Events, and Analytics Events; none MUST substitute for another or create authoritative history.

### REQ-FRP-053 — Analytics Non-Authority

Analytics collection and evidence MUST remain Consent-aware where applicable and MUST NOT establish Reporting or source truth, authorize access, change an outcome, or select analytics provider or event taxonomy.

### REQ-FRP-054 — Feature-Flag Safety

Every reachable feature-flag state MUST preserve authority, Authorization, security, privacy, accessibility, freshness, evidence integrity, failure handling, recovery, and FEB obligations without selecting rollout policy or implementation.

### REQ-FRP-055 — Abstract Reporting Contract

FRP MUST consume only bounded, versioned abstract Contracts sufficient for governed report identity, definitions, filters, periods, Dimensions, Measures, provenance, freshness, completeness, outcomes, failure, uncertainty, recovery, export, and correlation semantics.

### REQ-FRP-056 — Policy and Implementation Neutrality

FRP MUST NOT define KPI formulas, report or export policy, retention, provider, taxonomy, access matrix, approval model, routes, endpoints, methods, operations, DTOs, schemas, queries, SQL, persistence, warehouse, ETL, event payloads, workflow engines, transports, charting libraries, numerical targets, or other implementation details.

### REQ-FRP-057 — Verification Coverage

FRP MUST provide traceable verification across lifecycle, authority, inheritance, security, Reporting evidence, source boundaries, presentation, concurrency, failure, exports, accessibility, performance, telemetry, feature flags, Contracts, policy neutrality, and implementation neutrality.

## 5. Acceptance Criteria

| Acceptance Criterion | Requirement | Criterion |
|---|---|---|
| AC-FRP-001 | REQ-FRP-001 | Metadata and review evidence show `1.0.0 Approved`, `authoritative: false`, scope `FRP`, FRP-scoped normativity, no repository-wide authority, and preserved governing, Product, Business Requirement, Reporting Domain, source-Domain, FEB, and frontend precedence. |
| AC-FRP-002 | REQ-FRP-002 | Applicability review maps each inherited FEB obligation to FRP evidence and finds none copied, weakened, contradicted, or ownership-transferred; provisional-presentation obligations appear only with separately governed applicability. |
| AC-FRP-003 | REQ-FRP-003 | Boundary tests find no FRP-owned or redefined FSC, FCD, FPE, FCA, FCP, FPP, or FAD behavior and no authority transfer through navigation, embedding, context, or export. |
| AC-FRP-004 | REQ-FRP-004 | Each Reporting definition, projection, process, freshness, completeness, lineage, recovery, reconciliation, or export claim is backed by governed Reporting evidence and cannot be created by frontend manipulation. |
| AC-FRP-005 | REQ-FRP-005 | Source comparisons prove that reports, aggregates, visualizations, exports, and local calculations neither rewrite nor supersede authoritative source-Domain Facts. |
| AC-FRP-006 | REQ-FRP-006 | Unauthenticated, unauthorized, deep-link, identifier, filter, and cache-manipulation attempts disclose no protected report or Resource. |
| AC-FRP-007 | REQ-FRP-007 | Tests vary report, field, Dimension, Measure, drill-down, export, Principal, Session, purpose, Resource, action, property, and Reporting state independently and deny access without current server-side authorization. |
| AC-FRP-008 | REQ-FRP-008 | Manipulating visibility, visualizations, links, identifiers, filters, Role labels, Permissions, Claims, Scope, flags, caches, or state grants neither entitlement nor report access. |
| AC-FRP-009 | REQ-FRP-009 | Evidence remains purpose-minimal, and changed Authentication, Session, assignment, Authorization, report, or source context invalidates stale privilege and triggers governed revalidation. |
| AC-FRP-010 | REQ-FRP-010 | Cross-Principal, Session, Customer, purpose, Resource, aggregation, and segment tests find no disclosure through presentation, state, caches, filters, drill-down, or exports and impose no invented threshold. |
| AC-FRP-011 | REQ-FRP-011 | Enumeration and manipulation tests reveal no inaccessible report, Resource, field, segment, sensitive state, or internal detail through any listed surface. |
| AC-FRP-012 | REQ-FRP-012 | Privacy review finds only purpose-required Sensitive Data in presentation, state, routes, filters, errors, logs, telemetry, analytics, drill-down, and exports, with Consent and Preference authority preserved. |
| AC-FRP-013 | REQ-FRP-013 | Inspection finds no Secret, credential, token, provider secret, inappropriate raw Payment data, or internal security detail in any listed Reporting surface. |
| AC-FRP-014 | REQ-FRP-014 | Cache and export tests preserve current Authorization, isolation, minimization, provenance, and freshness and remove access after privilege or purpose change. |
| AC-FRP-015 | REQ-FRP-015 | Authorized discovery and navigation retain stable report context, while visibility, navigation, embedding, and links prove neither entitlement nor report truth. |
| AC-FRP-016 | REQ-FRP-016 | Reports retain governed identity across title, route, filters, definition versions, source references, exports, and visualizations without conflation. |
| AC-FRP-017 | REQ-FRP-017 | Changed and historical definitions display distinguishable governed identity, meaning, version, and applicability and cannot be silently reinterpreted by the frontend. |
| AC-FRP-018 | REQ-FRP-018 | Measure and Dimension evidence retains governed unit, aggregation, applicability, and relationships across labels, layouts, filters, and visualization changes. |
| AC-FRP-019 | REQ-FRP-019 | Classification checks distinguish Reporting Facts, projections, source Facts, raw events, Analytics Events, and frontend state and find no substitution. |
| AC-FRP-020 | REQ-FRP-020 | Every displayed Metric or KPI traces to a governed definition, and no local formula, target, threshold, success criterion, or interpretation appears. |
| AC-FRP-021 | REQ-FRP-021 | Funnel and Attribution presentation traces to governed definitions and introduces no local stage, window, rule, weight, classification, or causal claim. |
| AC-FRP-022 | REQ-FRP-022 | Material evidence exposes sufficient report, definition, source, period, applicability, processing, freshness, and correlation context to verify provenance. |
| AC-FRP-023 | REQ-FRP-023 | Historical evidence remains interpretable under its governed definition, version, sources, period, Currency, and corrections rather than current replacements. |
| AC-FRP-024 | REQ-FRP-024 | Negative tests prove that projections, dashboards, transformations, derived display values, visualizations, caches, and local calculations establish neither source truth nor Metric truth. |
| AC-FRP-025 | REQ-FRP-025 | Current and stale outcomes are independently produced and expose applicable as-of, freshness, and revalidation evidence without stale confirmation. |
| AC-FRP-026 | REQ-FRP-026 | Complete, partial, late, missing, gapped, and uncertain inputs produce truthful qualification and never display unsupported completeness or precision. |
| AC-FRP-027 | REQ-FRP-027 | Corrections, supersession, and restatement remain visible and associated, and refresh neither silently rewrites history nor claims source repair. |
| AC-FRP-028 | REQ-FRP-028 | Domain evidence retains its source meaning, and negative tests find no FRP-owned mutation or truth for any enumerated Domain or fraud evidence. |
| AC-FRP-029 | REQ-FRP-029 | Money presentation retains Currency, supplied conversion context, aggregation applicability, and historical meaning, with no implicit conversion or invented total. |
| AC-FRP-030 | REQ-FRP-030 | Tax and commercial-document evidence remains governed and FRP selects no legal meaning, calculation, eligibility, content, issuance, retention, or policy. |
| AC-FRP-031 | REQ-FRP-031 | Invalid, changed, and accepted filter, search, period, date-range, and refresh intent remains distinguishable and establishes neither validity nor completion before governed evidence. |
| AC-FRP-032 | REQ-FRP-032 | Comparison, Dimension, grouping, and segment selections retain governed applicability and create no cohort, classification, relationship, or business meaning. |
| AC-FRP-033 | REQ-FRP-033 | Cards retain definition, value, unit, period, provenance, freshness, completeness, and uncertainty, and prominence cannot create priority or truth. |
| AC-FRP-034 | REQ-FRP-034 | Tables preserve columns, units, row identity, relationships, sort/filter meaning, provenance, freshness, partiality, and bounded traversal without representing a page as complete truth. |
| AC-FRP-035 | REQ-FRP-035 | Visualization review verifies values, scales, units, periods, labels, legends, relationships, provenance, completeness, and uncertainty and finds no misleading encoding, false precision, or visual-only meaning. |
| AC-FRP-036 | REQ-FRP-036 | Drill-down preserves current Authorization and parent, definition, source, applicability, and freshness context and cannot infer detail, broaden access, or mutate truth. |
| AC-FRP-037 | REQ-FRP-037 | Pagination, sorting, filtering, and traversal retain stable report and ordering context, qualify completeness, remain bounded, and never imply page completeness. |
| AC-FRP-038 | REQ-FRP-038 | Manipulating dashboard composition, placement, prominence, alerts, cards, charts, or local status establishes no priority, outcome, source truth, definition, or Administration workflow. |
| AC-FRP-039 | REQ-FRP-039 | Stale, cancelled, delayed, reordered, and superseded results across filters, periods, comparisons, Dimensions, drill-down, and refresh cannot overwrite newer intent or state. |
| AC-FRP-040 | REQ-FRP-040 | Duplicate interaction and duplicate, reordered, conflicting, and corrected evidence retain identity and order without multiplying intent or fabricating reconciliation. |
| AC-FRP-041 | REQ-FRP-041 | Tests independently produce applicable initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed states without mandatory traversal. |
| AC-FRP-042 | REQ-FRP-042 | Timeout, lost-response, partial-observation, stopped-observation, and unknown-effect tests remain non-confirming and require governed revalidation before retry or assertion. |
| AC-FRP-043 | REQ-FRP-043 | Discrepancy and recovery interaction invokes only supported Reporting intent, preserves source authority, and defines no processing or source-repair mechanism. |
| AC-FRP-044 | REQ-FRP-044 | Validation, Authorization, source, processing, export, provider, and optional-dependency failures remain isolated, truthful, safeguard-preserving, and recoverable where supported. |
| AC-FRP-045 | REQ-FRP-045 | Export controls submit explicit authorized context, and local, pending, navigated, timed-out, or UI-confirmed state never appears as accepted or complete. |
| AC-FRP-046 | REQ-FRP-046 | Pending, processing, completed, failed, unavailable, partial, expired, and uncertain export evidence retains governed identity, provenance, freshness, correlation, and safe retrieval context without a local lifecycle. |
| AC-FRP-047 | REQ-FRP-047 | Duplicate, retry, replay, and restoration tests avoid intentionally repeated effect and false success, while format, size, destination, retention, delivery, and approval policy remain unselected. |
| AC-FRP-048 | REQ-FRP-048 | Viewport, reflow, zoom, orientation, input, localization, long-label, large-value, and content-extreme tests preserve meaning and operation without prescribed layout. |
| AC-FRP-049 | REQ-FRP-049 | Keyboard, focus, structural, and assistive-technology evidence makes loading, refresh, filter, error, uncertainty, recovery, and export changes perceivable at WCAG 2.2 AA. |
| AC-FRP-050 | REQ-FRP-050 | Tables and visualizations expose programmatic names, relationships, non-visual equivalents, legends, labels, and color-independent meaning to assistive technology. |
| AC-FRP-051 | REQ-FRP-051 | Large governed datasets and concurrent table, visualization, filter, sort, pagination, drill-down, refresh, export, reactive, and background work remain bounded without invented numbers or mechanisms. |
| AC-FRP-052 | REQ-FRP-052 | Evidence classification distinguishes telemetry, correlation, Reporting evidence, Audit Records, Domain Events, Integration Events, and Analytics Events and proves none substitutes for another. |
| AC-FRP-053 | REQ-FRP-053 | Analytics tests preserve applicable Consent and show that analytics neither authorizes nor establishes or changes Reporting or source outcomes, providers, or taxonomy. |
| AC-FRP-054 | REQ-FRP-054 | Every reachable flag state preserves authority, Authorization, security, privacy, accessibility, freshness, evidence integrity, failure handling, recovery, and FEB obligations. |
| AC-FRP-055 | REQ-FRP-055 | Contract review confirms bounded versioned Reporting semantics for every enumerated concept without introducing concrete implementation design. |
| AC-FRP-056 | REQ-FRP-056 | Review finds no locally selected formula, policy, provider, taxonomy, matrix, approval model, interface, query, persistence, processing, visualization, numerical, or implementation design. |
| AC-FRP-057 | REQ-FRP-057 | Verification evidence independently covers every enumerated FRP area through applicable positive, negative, boundary, failure, recovery, concurrency, security, privacy, accessibility, performance, and traceability checks. |

## 6. Requirement Traceability

| Requirement | FEB/Frontend Boundaries | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
|---|---|---|---|---|---|---|
| REQ-FRP-001 | REQ-FEB-001 | PRODUCT.md §§13, 18 | REQ-BUS-044 | REQ-RPT-001–003 | AGENTS.md §§1, 3.6, 5; ARCHITECTURE.md §§9–10 | All Reporting frontend behavior |
| REQ-FRP-002 | REQ-FEB-001–023, 026–051 | — | — | REQ-RPT-001, 053 | AGENTS.md §§5, 24 | All FRP behavior |
| REQ-FRP-003 | REQ-FSC-003; REQ-FCD-003; REQ-FPE-003; REQ-FCA-003; REQ-FCP-003; REQ-FPP-003; REQ-FAD-003, 029 | PRODUCT.md §§13, 18 | — | REQ-RPT-003, 040 | ARCHITECTURE.md §§9, 26 | Storefront and Administration frontends |
| REQ-FRP-004 | REQ-FEB-002–005 | PRODUCT.md §18 | REQ-BUS-044 | REQ-RPT-002, 004–005, 011, 017–018, 025–026, 041–042 | ARCHITECTURE.md §§9, 26 | All Reporting consumers |
| REQ-FRP-005 | REQ-FEB-003–005 | PRODUCT.md §§13, 18 | REQ-BUS-044 | REQ-RPT-003, 008, 013–015 | ARCHITECTURE.md §§9, 26 | Source Domains and report consumers |
| REQ-FRP-006 | REQ-FEB-011–013, 030 | — | REQ-BUS-032 | REQ-RPT-039; REQ-IDN-010, 018–020, 026–028 | SECURITY-STANDARDS.md §§10–12, 18 | Protected Reporting entry |
| REQ-FRP-007 | REQ-FEB-012–013 | — | REQ-BUS-032–033 | REQ-RPT-038–039; REQ-IDN-027–028 | SECURITY-STANDARDS.md §§10–12; API.md §26 | Protected Reporting surfaces |
| REQ-FRP-008 | REQ-FEB-012–013, 048 | — | REQ-BUS-032 | REQ-RPT-039–040; REQ-IDN-020, 026 | SECURITY-STANDARDS.md §§10–12 | Protected Reporting surfaces |
| REQ-FRP-009 | REQ-FEB-005, 012–013 | PRODUCT.md §12.9 | REQ-BUS-032–033 | REQ-RPT-039; REQ-IDN-027–028, 031 | SECURITY-STANDARDS.md §§10–13 | Long-lived Reporting interaction |
| REQ-FRP-010 | REQ-FEB-008, 026, 029–030 | PRODUCT.md §§16.4–16.5, 18 | REQ-BUS-039–040 | REQ-RPT-036–038; REQ-CUS-005, 039–041; REQ-IDN-006 | SECURITY-STANDARDS.md §§10–12, 35 | Reports with protected aggregates or detail |
| REQ-FRP-011 | REQ-FEB-030 | — | REQ-BUS-032, 052 | REQ-RPT-038–039; REQ-IDN-006 | SECURITY-STANDARDS.md §§10–12, 18 | Protected Reporting surfaces |
| REQ-FRP-012 | REQ-FEB-026, 029 | Open Product Decision 19 | REQ-BUS-039–041 | REQ-RPT-036–038; REQ-CUS-020–021, 039–041 | SECURITY-STANDARDS.md §§12, 35 | Privacy and Reporting consumers |
| REQ-FRP-013 | REQ-FEB-028 | — | REQ-BUS-027, 039 | REQ-RPT-038; REQ-IDN-043–044; REQ-PAY-031–032 | SECURITY-STANDARDS.md §§14, 18, 35 | All Reporting evidence surfaces |
| REQ-FRP-014 | REQ-FEB-007–008, 012–013, 026 | Open Product Decision 21 | REQ-BUS-032–033, 039, 053 | REQ-RPT-039, 041; REQ-IDN-031 | SECURITY-STANDARDS.md §§10–13, 35 | Cached reports and exports |
| REQ-FRP-015 | REQ-FEB-009–010, 013 | PRODUCT.md §§18, 31 | REQ-BUS-031, 044 | REQ-RPT-004–005, 039–040 | UI.md; ACCESSIBILITY.md | Reporting navigation consumers |
| REQ-FRP-016 | REQ-FEB-004 | PRODUCT.md §18 | REQ-BUS-044 | REQ-RPT-004 | GLOSSARY.md §26 | Report consumers and exports |
| REQ-FRP-017 | REQ-FEB-004–005 | PRODUCT.md §18 | REQ-BUS-044 | REQ-RPT-005, 011–012 | DOCUMENTATION-STANDARDS.md §§7–9 | Report consumers and audit reviewers |
| REQ-FRP-018 | REQ-FEB-004 | PRODUCT.md §§18, 33 | REQ-BUS-044 | REQ-RPT-005, 007 | GLOSSARY.md §26 | Analytical report consumers |
| REQ-FRP-019 | REQ-FEB-003–004, 046–047 | PRODUCT.md §§13, 18, 33 | REQ-BUS-044, 046 | REQ-RPT-008, 015–016 | EVENTS.md; GLOSSARY.md §§14, 26, 42 | Reporting and Analytics consumers |
| REQ-FRP-020 | REQ-FEB-004, 050 | PRODUCT.md §§18, 24 | REQ-BUS-044 | REQ-RPT-005–006 | GLOSSARY.md §26 | Product and Reporting consumers |
| REQ-FRP-021 | REQ-FEB-004, 050 | PRODUCT.md §§18, 33 | REQ-BUS-044 | REQ-RPT-009–010 | GLOSSARY.md §26 | Product and Analytics consumers |
| REQ-FRP-022 | REQ-FEB-004–005 | PRODUCT.md §§13, 18 | REQ-BUS-043–044 | REQ-RPT-013–014 | ARCHITECTURE.md §§14, 26 | All Reporting consumers |
| REQ-FRP-023 | REQ-FEB-004–005 | PRODUCT.md §18 | REQ-BUS-044 | REQ-RPT-011–012, 024 | DATABASE.md §§42, 48–52 | Finance, audit, and historical report consumers |
| REQ-FRP-024 | REQ-FEB-003–007 | PRODUCT.md §§13, 18 | REQ-BUS-044 | REQ-RPT-002–003, 008, 014–015, 040 | ARCHITECTURE.md §§9, 26–27 | All Reporting consumers |
| REQ-FRP-025 | REQ-FEB-005, 007, 014–015 | PRODUCT.md §§17.2, 18 | REQ-BUS-042, 044 | REQ-RPT-017 | ARCHITECTURE.md §26 | All report regions |
| REQ-FRP-026 | REQ-FEB-014–016, 018, 020 | PRODUCT.md §§17.2, 18 | REQ-BUS-042, 044–045 | REQ-RPT-018–019 | ARCHITECTURE.md §§20, 26 | All report regions |
| REQ-FRP-027 | REQ-FEB-005, 017, 020–021 | PRODUCT.md §§17.2–17.4, 18 | REQ-BUS-042, 044 | REQ-RPT-021, 024 | ARCHITECTURE.md §§20, 26 | Historical and refreshed reports |
| REQ-FRP-028 | REQ-FEB-003–005 | PRODUCT.md §§12–13, 18 | REQ-BUS-014–030, 044 | REQ-RPT-027–038 | ARCHITECTURE.md §§9, 26 | All domain-report consumers |
| REQ-FRP-029 | REQ-FEB-004 | Open Product Decisions 6, 8–9, 11, 14, 25, 29 | REQ-BUS-014–016 | REQ-RPT-029–030; REQ-PRC-006, 019; REQ-PAY-008; REQ-ORD-023–024 | API.md §14; GLOSSARY.md §§5, 29 | Finance and commercial report consumers |
| REQ-FRP-030 | REQ-FEB-004, 050 | Open Product Decisions 9, 25 | REQ-BUS-041, 054 | REQ-RPT-031; REQ-PRC-013; REQ-PAY-044; REQ-ORD-025 | SECURITY-STANDARDS.md §35 | Finance, legal, and commercial report consumers |
| REQ-FRP-031 | REQ-FEB-005, 009, 015, 019 | PRODUCT.md §§17.2, 18 | REQ-BUS-042, 044 | REQ-RPT-005, 017–018 | API.md §§17–20 | Interactive report consumers |
| REQ-FRP-032 | REQ-FEB-004–005, 019 | PRODUCT.md §§18, 33 | REQ-BUS-044 | REQ-RPT-005, 007, 010 | GLOSSARY.md §26 | Analytical exploration consumers |
| REQ-FRP-033 | REQ-FEB-004–005, 014–015 | PRODUCT.md §18 | REQ-BUS-042, 044 | REQ-RPT-005, 007, 013, 017–018, 040 | UI.md; ACCESSIBILITY.md | Dashboard consumers |
| REQ-FRP-034 | REQ-FEB-004–005, 014–018, 037–038, 042 | PRODUCT.md §18 | REQ-BUS-037–038, 042, 044 | REQ-RPT-005, 007, 013, 017–018, 043, 045 | ACCESSIBILITY.md; PERFORMANCE.md §18 | Table consumers |
| REQ-FRP-035 | REQ-FEB-004–005, 031–041 | PRODUCT.md §§18, 33 | REQ-BUS-037–038, 042, 044 | REQ-RPT-005, 007, 013, 017–018, 045 | ACCESSIBILITY.md; DESIGN-SYSTEM.md; UI.md | Visualization consumers |
| REQ-FRP-036 | REQ-FEB-005, 009, 012–013, 030 | PRODUCT.md §18 | REQ-BUS-032–033, 044 | REQ-RPT-013, 017, 039 | SECURITY-STANDARDS.md §§10–12 | Drill-down consumers |
| REQ-FRP-037 | REQ-FEB-005, 009, 014–018, 042 | PRODUCT.md §18 | REQ-BUS-038, 042, 044 | REQ-RPT-017–018, 043 | API.md §17; PERFORMANCE.md §18 | Large report consumers |
| REQ-FRP-038 | REQ-FEB-003–005, 013 | PRODUCT.md §§18, 31 | REQ-BUS-044 | REQ-RPT-002–003, 040 | UI.md; ARCHITECTURE.md §26 | Dashboard and Administration consumers |
| REQ-FRP-039 | REQ-FEB-005, 017 | PRODUCT.md §18 | REQ-BUS-042, 044 | REQ-RPT-017, 020–021 | API.md §§17–20 | Interactive asynchronous reports |
| REQ-FRP-040 | REQ-FEB-017, 020–023 | PRODUCT.md §§17.2–17.4, 18 | REQ-BUS-035, 042, 044 | REQ-RPT-020–022, 024 | ARCHITECTURE.md §§20, 26 | Interactive and refreshed reports |
| REQ-FRP-041 | REQ-FEB-014–016, 018–021 | PRODUCT.md §§17.2, 18 | REQ-BUS-042, 044–045 | REQ-RPT-017–019, 025 | UI.md §32; ACCESSIBILITY.md §26 | All report and export regions |
| REQ-FRP-042 | REQ-FEB-015–016, 020–023 | PRODUCT.md §§17.2–17.4, 23 | REQ-BUS-042, 044–045 | REQ-RPT-018–019, 025, 042 | API.md §§48, 54–58 | Long-running reports and exports |
| REQ-FRP-043 | REQ-FEB-005, 020–023 | PRODUCT.md §§17.2–17.4, 23 | REQ-BUS-035–036, 044–045 | REQ-RPT-023, 025–026 | ARCHITECTURE.md §§20, 26 | Reporting recovery consumers |
| REQ-FRP-044 | REQ-FEB-020–021, 044 | PRODUCT.md §23 | REQ-BUS-042, 045 | REQ-RPT-018–019, 025, 044 | PERFORMANCE.md; UI.md §32 | All report and export regions |
| REQ-FRP-045 | REQ-FEB-012–013, 019, 023, 026 | Open Product Decision 21 | REQ-BUS-032–033, 053 | REQ-RPT-039, 041 | SECURITY-STANDARDS.md §§12, 35 | Export consumers |
| REQ-FRP-046 | REQ-FEB-014–016, 020–022 | Open Product Decision 21 | REQ-BUS-042, 045, 053 | REQ-RPT-041–042 | API.md §§48, 54–58 | Export consumers |
| REQ-FRP-047 | REQ-FEB-020–023, 026 | Open Product Decision 21 | REQ-BUS-039, 053 | REQ-RPT-041–042 | SECURITY-STANDARDS.md §§12, 35 | Export consumers |
| REQ-FRP-048 | REQ-FEB-031–034, 040–041 | PRODUCT.md §§29–30 | REQ-BUS-037–038 | REQ-RPT-043, 045 | ACCESSIBILITY.md; UI.md; DESIGN-SYSTEM.md | All Reporting frontend consumers |
| REQ-FRP-049 | REQ-FEB-032, 034–038 | PRODUCT.md §§29–30 | REQ-BUS-037 | REQ-RPT-045 | ACCESSIBILITY.md; UI.md | All interactive Reporting regions |
| REQ-FRP-050 | REQ-FEB-034–041 | PRODUCT.md §§29–30, 33 | REQ-BUS-037 | REQ-RPT-045 | ACCESSIBILITY.md; DESIGN-SYSTEM.md; UI.md | Table and visualization consumers |
| REQ-FRP-051 | REQ-FEB-042–044 | PRODUCT.md §§23, 31 | REQ-BUS-038, 045 | REQ-RPT-043–044 | PERFORMANCE.md §§17–18, 25, 35 | All data-intensive Reporting regions |
| REQ-FRP-052 | REQ-FEB-045–047 | Open Product Decision 20 | REQ-BUS-034–035, 043–044, 046 | REQ-RPT-013, 015–016, 046–047, 049–050 | EVENTS.md; SECURITY-STANDARDS.md §27 | Reporting, audit, operations, and analytics consumers |
| REQ-FRP-053 | REQ-FEB-029, 047 | Open Product Decisions 19–20 | REQ-BUS-040, 044, 046 | REQ-RPT-016, 037, 050; REQ-CUS-020–021, 038 | PRODUCT.md §§20, 33; SECURITY-STANDARDS.md §35 | Analytics and privacy consumers |
| REQ-FRP-054 | REQ-FEB-048 | Open Product Decision 30 | REQ-BUS-032, 037, 039, 042 | REQ-RPT-039, 045, 051 | ARCHITECTURE.md §39; TESTING-STANDARDS.md | All flagged Reporting behavior |
| REQ-FRP-055 | REQ-FEB-049 | PRODUCT.md §37 | REQ-BUS-044, 046 | REQ-RPT-048 | ARCHITECTURE.md §§12–16, 39; API.md §§37–61 | FRP implementers and Reporting integrators |
| REQ-FRP-056 | REQ-FEB-050 | PRODUCT.md §§24, 37 | REQ-BUS-044, 046 | REQ-RPT-006, 009–010, 050–052 | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9 | Product, Reporting, and engineering reviewers |
| REQ-FRP-057 | REQ-FEB-051 | PRODUCT.md §§20, 31, 34 | REQ-BUS-037–045, 047 | REQ-RPT-053 | TESTING-STANDARDS.md; SECURITY-STANDARDS.md; ACCESSIBILITY.md | FRP reviewers and verification owners |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following 15 are materially relevant to FRP, preserve exact source wording and order, and remain unresolved by this Specification.

| Source Decision | Open Product Decision | FRP Boundary |
|---|---|---|
| 6 | Initial payment methods and provider. | FRP may present governed classifications but leaves methods and provider unresolved. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | FRP may present governed Shipping Dimensions but leaves provider and service policy unresolved. |
| 8 | Free-delivery threshold and promotional treatment. | FRP may present governed evidence but leaves threshold and treatment unresolved. |
| 9 | Tax-inclusive display and invoice requirements. | FRP may present governed financial evidence but leaves tax and invoice policy unresolved. |
| 10 | Cancellation eligibility and cutoff policy. | FRP may present governed classifications but leaves eligibility and cutoff unresolved. |
| 11 | Returns, exchanges, and refund policy. | FRP may present governed post-purchase evidence but leaves policy unresolved. |
| 13 | Back-order and pre-order support. | FRP may report only governed capability evidence and leaves support unresolved. |
| 14 | Voucher and promotion stacking policy. | FRP may present governed commercial evidence but leaves stacking unresolved. |
| 19 | Marketing-consent and communication-preference model. | FRP preserves Consent and Preference authority and leaves the model, defaults, categories, capture, and policy unresolved. |
| 20 | Initial analytics provider and event taxonomy. | FRP keeps analytics non-authoritative and leaves provider and taxonomy unresolved. |
| 21 | Initial reporting and export requirements. | FRP presents only governed reports and exports and leaves definitions, audiences, purposes, formats, destinations, and policy unresolved. |
| 25 | South African tax-display, invoice, and credit-note policy. | FRP provides no legal interpretation and leaves tax and commercial-document policy unresolved. |
| 26 | Fraud-screening approach and manual-review workflow. | FRP may present governed classifications but leaves screening, provider, model, rules, thresholds, workflow, and outcomes unresolved. |
| 29 | Gift cards, store credit, and promotional credit policy. | FRP creates no credit semantics and leaves support, accounting, eligibility, and policy unresolved. |
| 30 | Product launch date, release scope, and post-launch support window. | FRP selects no date, release scope, target, rollout, or support window; all remain unresolved. |

## 8. Risks and Controls

| Risk | Control |
|---|---|
| Reporting Projection treated as source truth | Label and test projections as stale-capable Reporting evidence and preserve source-Domain authority. |
| Stale report treated as current | Expose governed as-of and freshness context and require revalidation for current claims. |
| Unauthorized report access | Require trusted Authentication and current contextual server-side Authorization. |
| Unauthorized field or drill-down access | Authorize each field, property, Dimension, Measure, Resource, and drill-down independently. |
| Aggregation or inference disclosure | Test protected aggregations and segments for purpose-bound isolation without inventing thresholds. |
| Cross-Customer or cross-Resource leakage | Isolate presentation, state, caches, filters, drill-down, and exports by applicable security context. |
| Filter, identifier, or deep-link manipulation | Treat all frontend context as untrusted intent and revalidate it server-side. |
| Stale privilege | Revalidate material access after security-context change and remove stale evidence. |
| Partial, incomplete, late, or missing evidence presented as complete | Preserve completeness, gaps, limitations, and uncertainty explicitly. |
| Misleading chart, table, or false precision | Preserve definitions, units, scales, labels, provenance, qualification, and appropriate precision evidence. |
| Missing definition, version, provenance, or freshness | Require these interpretive fields for every material Reporting claim. |
| Superseded result overwrite | Reject stale, delayed, reordered, or superseded asynchronous outcomes. |
| Timeout mistaken for completion | Preserve unknown effect and require governed revalidation before assertion or retry. |
| Duplicate export intent | Keep export intent correlated and duplicate-safe without assuming backend mechanics. |
| Export data leakage | Apply current Authorization, isolation, minimization, and safe retrieval context. |
| False export completion | Present completion only from correlated Reporting-owned evidence. |
| Unbounded Reporting frontend work | Bound data, visualization, filter, drill-down, refresh, export, reactive, and background work by governed inputs. |
| Optional dependency degradation | Isolate failure while preserving safeguards, evidence integrity, and available safe recovery. |
| Telemetry, Reporting, Analytics, event, and Audit Record conflation | Classify evidence explicitly and verify none substitutes for another. |
| Local calculation becoming business truth | Keep frontend calculations representational and require governed definitions and evidence. |
| Inaccessible visualizations, tables, controls, or status | Verify WCAG 2.2 AA, non-visual equivalents, programmatic relationships, keyboard, focus, and status behavior. |
| FAD and FRP scope leakage | Test dedicated Reporting exploration separately from Administration operational coordination. |
| Source-Domain mutation or reconciliation leakage | Permit only Reporting intent and prohibit source mutation or repair from FRP. |
| Feature-flag safeguard bypass | Exercise every reachable flag state against authority, security, privacy, accessibility, freshness, and recovery. |
| Concrete Contract or implementation detail becomes normative | Constrain FRP to bounded abstract semantics and reject concrete interface, processing, storage, provider, and visualization design. |

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
- `.ai/backend/DATABASE.md`
- `.ai/frontend/ANGULAR.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/STORYBOOK.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/frontend/shared/frontend-baseline.md`
- `specifications/frontend/storefront/storefront-shell-content.md`
- `specifications/frontend/storefront/catalogue-discovery.md`
- `specifications/frontend/storefront/product-evaluation.md`
- `specifications/frontend/storefront/account-identity.md`
- `specifications/frontend/storefront/cart-purchase.md`
- `specifications/frontend/storefront/post-purchase.md`
- `specifications/frontend/administration/administration-frontend.md`
- `specifications/domains/reporting/reporting-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/search/search-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
|---|---|---|---|
| 0.1.0 | 2026-09-10 | Draft | Initial comprehensive Reporting Frontend Specification. |
| 1.0.0 | 2026-09-10 | Approved | Approved Reporting Frontend Specification. |

## 11. Final Validation

For this Approved specification:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `FRP`, Requirements are normative only within FRP scope, and no repository-wide authority is claimed;
2. Reporting Domain and every source Domain retain authority, and FRP cannot mutate or reconcile source truth;
3. FEB inheritance and FSC, FCD, FPE, FCA, FCP, FPP, and FAD boundaries remain preserved;
4. trusted Authentication, current contextual report- and field-level Authorization, least privilege, and stale-privilege revalidation remain verifiable;
5. filters, identifiers, links, labels, Permissions, Claims, Scope, flags, caches, charts, tables, and client state grant no entitlement;
6. aggregation and inference resistance, isolation, concealment, Sensitive Data minimization, Consent authority, and Secret exclusion are complete;
7. report identity, definitions, versions, Measures, Dimensions, Facts, Metrics, KPIs, Funnels, Attribution, lineage, provenance, and historical reproducibility remain governed;
8. Reporting Projections, dashboards, local calculations, visualizations, exports, and analytics remain non-authoritative for source truth;
9. freshness, completeness, partial, late, missing, conflicting, corrected, and uncertain evidence remains truthful and distinguishable;
10. filter, period, comparison, Dimension, segment, drill-down, refresh, and navigation intent cannot establish Reporting completion or truth;
11. summaries, cards, tables, charts, legends, labels, relationships, drill-down, pagination, and bounded collections preserve governed meaning;
12. stale, cancelled, delayed, reordered, duplicate, conflicting, and superseded outcomes cannot overwrite newer relevant intent;
13. initial, loading, empty, stale, partial, invalid, unavailable, denied, failed, uncertain, recovery, and confirmed presentation states remain distinguishable without mandatory traversal;
14. timeout and unknown effect remain non-confirming, and Reporting recovery or reconciliation intent cannot repair source truth;
15. export intent, processing, completion, failure, uncertainty, duplicate interaction, Authorization, privacy, and policy boundaries remain verifiable;
16. responsive, reflow, zoom, keyboard, focus, status, table, visualization, color-independent, and non-visual-equivalent outcomes meet WCAG 2.2 AA;
17. datasets, tables, visualizations, filters, sorting, pagination, drill-down, refresh, concurrent requests, exports, reactive work, and background work remain bounded without invented numerical limits;
18. telemetry, correlation, Reporting evidence, Audit Records, Domain Events, Integration Events, and Analytics Events remain distinct, and analytics remains non-authoritative;
19. every feature-flag state preserves safeguards, freshness, failure handling, recovery, FEB inheritance, and Domain authority;
20. Contracts remain bounded, abstract, and versioned, and no policy, route, API, DTO, schema, query, persistence, warehouse, ETL, provider, event payload, workflow engine, transport, charting library, target, threshold, or implementation detail is invented;
21. exactly 57 Requirements, 57 independently authored Acceptance Criteria, and 57 semantic traceability rows exist;
22. Requirement and Acceptance Criterion identifiers and Requirement/AC/traceability mappings are unique, sequential, gap-free, and one-to-one from `REQ-FRP-001` through `REQ-FRP-057` and `AC-FRP-001` through `AC-FRP-057`;
23. all 30 Open Product Decisions were reviewed, exactly 15 materially relevant decisions are represented with exact source wording and order, and all remain unresolved;
24. Risks and Controls are FRP-specific, distinct, materially complete, and implementation-neutral;
25. every Related Document exists and is materially relevant, no Glossary amendment is required, and Revision History contains exactly one preserved `0.1.0 Draft` row and one `1.0.0 Approved` row;
26. Markdown headings and tables, UTF-8, trailing whitespace, exactly one final newline, and prohibited-marker checks pass; and
27. Git scope contains only intended changes to `specifications/frontend/reporting/reporting-frontend.md`, nothing unrelated is staged or modified, and `git diff --check` plus applicable tracked-file validation passes.
