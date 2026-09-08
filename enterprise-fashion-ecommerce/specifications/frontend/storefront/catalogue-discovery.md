---
title: Catalogue and Discovery Specification
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-08
authoritative: false
---

# Catalogue and Discovery Specification

## 1. Purpose

This Specification defines implementation-neutral requirements for the customer-facing frontend experience used to browse and discover the governed catalogue.

This document uses scope code `FCD`. Its Approved Requirements are normative only within the Catalogue and Discovery scope and are not repository-wide authority. It remains subordinate to higher-authority governing sources, inherits the Approved Shared Frontend Baseline and Storefront Shell and Content Specification, preserves every Approved Domain's authority, and resolves no Open Product Decision.

## 2. Scope and Authority

FCD owns only catalogue-and-discovery presentation truth: browsing and Category context, presentation of governed collections and Search results, filter, facet, sort and traversal interaction, discovery summaries, discovery-specific state, accessibility, degradation, telemetry, and abstract Contract expectations.

FCD does not own Product, Product Variant, Product Media, Product publication, visibility or sellability; Category taxonomy, hierarchy, membership, ordering or eligibility; Search query interpretation, normalization, indexing, matching, ranking or Search Projection truth; Pricing, Inventory, CMS, Customer, Identity, Cart, Checkout, Payment, Reporting, Analytics, commercial, tax, fraud, Consent, or Product-policy truth. Client state, result position, filter state, route state, cached evidence, or presentation cannot transfer that authority to FCD.

Shared frontend behavior remains owned by FEB. Storefront shell, homepage, shared navigation, and shell content remain owned by FSC. Detailed Product evaluation remains in FPE; account and Identity behavior in FCA; Cart, Checkout, and Payment behavior in FCP; post-purchase behavior in FPP; Administration behavior in FAD; and Reporting behavior in FRP.

## 3. Terminology and Discovery Model

Canonical terms retain their meanings from `GLOSSARY.md`. For FCD only, **discovery context** describes the non-authoritative frontend state needed to present and navigate an identified catalogue or Search result set, and **discovery summary** describes a bounded presentation of governed evidence used to decide whether to enter a Product or Product Variant evaluation experience. These are local descriptions, not repository-wide terms, Domain entities, or lifecycle states.

A presented result remains evidence from its owning sources. Its presence, absence, order, count, filter membership, or visual prominence does not independently establish Product publication, Category membership, Search ranking, Price, Stock, availability, eligibility, Authorization, or final purchasability.

## 4. Requirements

### REQ-FCD-001 — Lifecycle, Authority, and Scope

FCD MUST govern only Catalogue and Discovery frontend behavior under scope `FCD`, preserve governing-source precedence, FEB and FSC obligations, Product and Business Requirements, and Approved Domain authority, and MUST treat this Approved Specification as normative only within the Catalogue and Discovery scope and not as repository-wide authority.

### REQ-FCD-002 — FEB and FSC Inheritance

FCD MUST consume every materially applicable Approved FEB and FSC Requirement without copying, weakening, conflicting with, or transferring ownership of shared frontend or shell behavior.

### REQ-FCD-003 — Domain and Journey Non-Authority

FCD MUST NOT own or redefine source-Domain truth or FSC, FPE, FCA, FCP, FPP, FAD, or FRP behavior; a discovery presentation or destination reference MUST NOT transfer that authority.

### REQ-FCD-004 — Catalogue Discovery Entry

FCD MUST provide a meaningful and operable entry into governed catalogue discovery from an established FSC destination without prescribing a route, URL, label, layout, Component, or commercial proposition.

### REQ-FCD-005 — Category-Based Discovery

Where Category discovery is supported, FCD MUST present only Categories and Product associations permitted by current governed Category and Product evidence and MUST NOT create taxonomy, hierarchy, membership, ordering, publication, visibility, or navigation eligibility.

### REQ-FCD-006 — Category Context and Navigation

FCD MUST communicate applicable Category context and relationships and preserve useful discovery navigation without treating a breadcrumb, route, client hierarchy, or displayed order as Category authority.

### REQ-FCD-007 — Governed Catalogue Collections

Catalogue collections MUST present bounded governed result evidence with sufficient identity, source, applicability, completeness, and freshness context for truthful discovery without FCD establishing authoritative membership or merchandising policy.

### REQ-FCD-008 — Search Entry and Result Boundary

FCD MAY present Search input and results from the governed Search capability but MUST NOT interpret or normalize queries, index content, match or rank results, or establish Search Projection truth.

### REQ-FCD-009 — Query and Result Evidence

The current query intent and corresponding result evidence MUST remain associated and distinguishable so that a result from an absent, invalid, older, cancelled, or different query is not represented as the current outcome.

### REQ-FCD-010 — Result Non-Authority

A discovery result MUST remain a non-authoritative representation and MUST NOT prove current Product publication, visibility, Category membership, Price, availability, eligibility, Authorization, or final purchasability.

### REQ-FCD-011 — Filtering

Where filtering is supported, available and active filters MUST derive from governed Search, Category, or Product evidence, communicate their effect and removable state, and MUST NOT invent Product Attributes, Category structure, eligibility, or result membership.

### REQ-FCD-012 — Facets

Where facets are supported, FCD MUST preserve their governed meaning, values, applicability, counts where provided, and selected state without calculating authoritative facet membership or defining a repository-wide facet taxonomy.

### REQ-FCD-013 — Sorting

Where sorting is supported, available and active sort semantics MUST reflect governed Search evidence, remain understandable, and MUST NOT allow display order or frontend reordering to become Search ranking or source-Domain authority.

### REQ-FCD-014 — Result Traversal

Pagination, progressive loading, or another governed result-traversal pattern MUST preserve result-set identity, ordering evidence, position, applied discovery context, and recoverability without selecting a pattern, page size, cursor design, URL encoding, or mandatory infinite loading.

### REQ-FCD-015 — Traversal Integrity

Continuation, refresh, back navigation, and repeated traversal MUST NOT silently duplicate, omit, reorder, or combine results in a way that misrepresents the governed result set or its known completeness.

### REQ-FCD-016 — Result Count and Metadata

Where result counts or metadata are presented, FCD MUST preserve their governed scope, source, applicability, freshness, and completeness and MUST NOT infer an exact total from partial, stale, unavailable, or differently scoped evidence.

### REQ-FCD-017 — Empty and No-Result Outcomes

FCD MUST distinguish a confirmed empty catalogue context or zero-result query from emptiness caused by active filtering and from initial, loading, stale, partial, unavailable, failed, denied, or uncertain presentation states and provide only safe governed next actions.

### REQ-FCD-018 — Product Discovery Summary

A Product discovery summary MUST preserve governed Product identity and content and applicable Product-owned publication and visibility eligibility evidence required for truthful discovery; where Product Variant-derived evidence is presented, its applicable Product Variant association or context MUST remain preserved. FCD MUST remain non-authoritative for Product publication, Product visibility, structural sellability, Price, Inventory, Customer eligibility, Authorization, or final purchasability.

### REQ-FCD-019 — Product Variant Summary

Where Product Variant evidence appears in discovery, FCD MUST preserve the Product association, stable Product Variant identity, governed Attribute meaning, and applicable visibility evidence without selecting a Product Variant or establishing variant availability or purchase commitment.

### REQ-FCD-020 — Product Media

Discovery media MUST retain governed Product or Product Variant association, ordering where provided, accuracy, publication, rights, accessibility, and freshness boundaries and MUST degrade without fabricating media or Product meaning.

### REQ-FCD-021 — Pricing Evidence

Where Price or Discount evidence is presented, FCD MUST consume governed Pricing evidence with explicit Money and Currency context, source, applicability, and freshness and MUST NOT calculate authoritative Price, Discount, Promotion, Voucher, tax, delivery charge, or totals.

### REQ-FCD-022 — Inventory and Availability Evidence

Where availability evidence is presented, FCD MUST represent it as governed and potentially stale, preserve its Product Variant and applicability context, and MUST NOT establish Stock, Available-to-Sell, Stock Reservation, Product visibility, purchase authorization, or final purchasability.

### REQ-FCD-023 — Freshness and Current Truth

Materially stale, delayed, partial, conflicting, or unavailable Product, Category, Search, Pricing, Inventory, or CMS evidence MUST NOT be represented as complete current truth and MUST trigger truthful qualification, degradation, removal, or governed revalidation as applicable.

### REQ-FCD-024 — Asynchronous Supersession

Stale, cancelled, reordered, or superseded query, filter, facet, sort, traversal, media, Price, or availability results MUST NOT overwrite newer relevant discovery intent or presentation state.

### REQ-FCD-025 — Discovery Context Preservation

FCD MUST preserve useful query, Category, filter, facet, sort, traversal, position, and result context across supported navigation and recovery when safe, while treating route, URL, history, and client state as non-authoritative and lifecycle-bounded.

### REQ-FCD-026 — Product Evaluation Handoff

FCD MAY provide a safe entry to FPE for a governed Product or Product Variant destination while preserving discovery context, but MUST NOT absorb detailed evaluation, selection, review, delivery, policy, or purchase behavior.

### REQ-FCD-027 — Invalid or Unavailable Results and Destinations

An invalid, withdrawn, unpublished, stale, inaccessible, unavailable, or otherwise unsupported result or destination MUST produce a truthful distinguishable outcome and safe permitted recovery without exposing protected Resource existence or retaining an unsupported current claim.

### REQ-FCD-028 — Discovery Presentation States

Each material discovery region MUST apply distinguishable initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery, and confirmed states where applicable without defining a business lifecycle or mandatory universal traversal.

### REQ-FCD-029 — Governed Recovery

Where recovery is available, FCD MUST preserve known discovery context, distinguish retry from changed intent, prevent obsolete outcomes from replacing current intent, and invoke only governed revalidation or recovery without defining backend retry, replay, or reconciliation mechanics.

### REQ-FCD-030 — Responsive Discovery Outcome

Discovery information, controls, active state, results, and essential operation MUST remain available across supported viewport, orientation, reflow, zoom, localization, and content-extreme conditions without FCD defining breakpoints or layouts.

### REQ-FCD-031 — Input-Modality Equivalence

Query, Category, filter, facet, sort, traversal, result, and recovery interactions MUST remain operable through supported keyboard, pointer, touch, and assistive-technology paths without requiring a single input modality.

### REQ-FCD-032 — Focus and Dynamic Results

Query submission, active-filter changes, sorting, traversal, result updates, empty outcomes, failures, and recovery MUST preserve or move focus predictably and communicate the resulting context without focus theft or loss of meaningful position.

### REQ-FCD-033 — Accessible Results and Status

Discovery controls, collections, summaries, counts, active state, result changes, loading, empty, stale, partial, unavailable, failed, uncertain, and recovery outcomes MUST expose appropriate structure, names, relationships, state, and status without relying only on color, position, motion, or visual change.

### REQ-FCD-034 — Content and Media Resilience

Discovery MUST preserve comprehension and operation under long, short, absent, localized, delayed, invalid, or failed Product content and media without clipping essential meaning, creating unsupported substitutes, or blocking unrelated results.

### REQ-FCD-035 — Rendering, Security, and Privacy

FCD MUST render query-derived and governed content safely, minimize Sensitive Data in client state, routes, errors, logs, telemetry, and Analytics Events, expose no Secrets, preserve isolation, and MUST NOT infer Consent from discovery interaction or Customer state.

### REQ-FCD-036 — Protected-Resource and Abuse Resistance

Discovery outcomes MUST preserve contextual server-side Authorization, Resource concealment, and abuse resistance and MUST NOT reveal inaccessible Resource existence, internal indexing detail, or sensitive state through result differences, counts, filters, facets, errors, or timing presentation.

### REQ-FCD-037 — Bounded Collections and Frontend Work

Result rendering, collection handling, filtering presentation, media loading, reactive work, and background activity MUST remain bounded by governed inputs and performance evidence without defining a numerical result limit, page size, timing budget, or client mechanism.

### REQ-FCD-038 — Degradation and Failure Containment

Failure or degradation of optional media, metadata, facets, counts, Price, availability, analytics, or another non-essential region MUST NOT silently block unrelated discovery operation, and every degraded outcome MUST remain truthful about known completeness.

### REQ-FCD-039 — Operational Telemetry and Correlation

Material discovery requests, result-presentation failures, recovery outcomes, and Contract-boundary failures MUST produce safe frontend operational telemetry and preserve governed correlation evidence sufficient for diagnosis. Frontend operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records MUST remain distinguishable; none substitutes for another, creates authoritative Domain history, or defines event names, payloads, providers, transports, schemas, or persistence.

### REQ-FCD-040 — Analytics Boundary

Discovery Analytics Events MUST remain non-authoritative observations, respect applicable Consent and Sensitive Data constraints, and MUST NOT define a provider, taxonomy, canonical event name, payload, attribution rule, ranking input, or business truth.

### REQ-FCD-041 — Feature-Flag Safety

Every reachable feature-flag or progressive-delivery state affecting discovery MUST preserve valid evidence, truthful results, context, accessibility, security, privacy, FEB and FSC obligations, and Domain authority without selecting rollout policy or implementation.

### REQ-FCD-042 — Abstract Contract Boundary

Future FCD Contracts MUST expose only necessary bounded and versioned query, Category, result-set identity, result, filter, facet, sort, traversal, count, source, freshness, completeness, outcome, and correlation semantics; FCD MUST NOT define routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, index technology, transport, or providers.

### REQ-FCD-043 — Product-Policy and Implementation Neutrality

FCD MUST keep unresolved taxonomy, Attribute, media, availability-message, analytics, launch, merchandising, personalization, ranking, filtering, sorting, traversal, route, visual, provider, protocol, caching, numerical, and implementation choices outside its normative behavior.

### REQ-FCD-044 — Verification Coverage

FCD verification MUST cover applicable authority, FEB and FSC inheritance, catalogue and Category discovery, Search boundaries, filters, facets, sorting, traversal, counts, summaries, Product Media, Price, availability, freshness, supersession, context, FPE handoff, presentation states, recovery, accessibility, responsiveness, security, privacy, Consent, boundedness, degradation, telemetry, feature flags, Contracts, and policy neutrality.

## 5. Acceptance Criteria

| Requirement | Acceptance Criterion                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| REQ-FCD-001 | Lifecycle validation confirms that metadata and Revision History state `1.0.0 Approved`, `authoritative: false`, and scope `FCD`; normativity is confined to the Catalogue and Discovery scope without repository-wide authority or loss of governing-source, FEB, FSC, Product, Business Requirement, or Approved Domain precedence. |
| REQ-FCD-002 | Verification assesses every materially applicable Approved FEB and FSC Requirement against FCD and finds none copied, weakened, conflicted with, or ownership-transferred.                                                                                                                                                                                                                                                                                                                                    |
| REQ-FCD-003 | Boundary review finds no FCD-owned or redefined source-Domain truth, redefined FEB or FSC behavior, detailed FPE, FCA, FCP, FPP, FAD, or FRP behavior, or authority transfer through result or destination presentation.                                                                                                                                                                                                                                                                                      |
| REQ-FCD-004 | A Customer can enter a meaningful discovery context from the governed shell while tests require no fixed route, label, layout, Component, or commercial proposition.                                                                                                                                                                                                                                                                                                                                          |
| REQ-FCD-005 | Every presented Category and association resolves to current governed Category and Product evidence, and negative tests reject client-created taxonomy, membership, order, publication, visibility, or eligibility.                                                                                                                                                                                                                                                                                           |
| REQ-FCD-006 | Category context and relationships are understandable and restorable, while changes to breadcrumbs, routes, client state, or display order cannot alter governed Category truth.                                                                                                                                                                                                                                                                                                                              |
| REQ-FCD-007 | Representative collections expose bounded identity, source, applicability, completeness, and freshness evidence, and no presentation decision creates authoritative membership or merchandising policy.                                                                                                                                                                                                                                                                                                       |
| REQ-FCD-008 | Search presentation consumes governed Search outcomes, and inspection finds no FCD-defined query interpretation, normalization, indexing, matching, ranking, or Projection rule.                                                                                                                                                                                                                                                                                                                              |
| REQ-FCD-009 | Results remain bound to their initiating query intent, and delayed or mismatched outcomes cannot appear as the current query's response.                                                                                                                                                                                                                                                                                                                                                                      |
| REQ-FCD-010 | Verification confirms that discovery presentation cannot establish current Product publication, Product visibility, Category membership, Price, availability, eligibility, Authorization, or final purchasability.                                                                                                                                                                                                                                                                                            |
| REQ-FCD-011 | Applied filters are visible, understandable, removable, and source-backed; unsupported attributes, Categories, eligibility, or membership cannot be introduced through frontend state.                                                                                                                                                                                                                                                                                                                        |
| REQ-FCD-012 | Each facet preserves governed meaning, applicability, values, optional counts, and selection, and no local calculation is accepted as authoritative membership or taxonomy.                                                                                                                                                                                                                                                                                                                                   |
| REQ-FCD-013 | The active sort and its effect are understandable, and client reordering cannot be represented as governed Search ranking or source truth.                                                                                                                                                                                                                                                                                                                                                                    |
| REQ-FCD-014 | Every supported traversal preserves result-set identity, order evidence, position, active context, and recovery without tests depending on a fixed pagination mechanism or size.                                                                                                                                                                                                                                                                                                                              |
| REQ-FCD-015 | Continuation, refresh, return navigation, and repeated traversal are tested against duplicate, missing, reordered, and incorrectly combined presentations using the governed completeness evidence.                                                                                                                                                                                                                                                                                                           |
| REQ-FCD-016 | Counts and metadata identify their scope, source, applicability, freshness, and completeness; partial or stale evidence is never shown as an exact current total.                                                                                                                                                                                                                                                                                                                                             |
| REQ-FCD-017 | Tests independently produce initial, confirmed empty catalogue, zero-result query, active-filter-caused emptiness, loading, stale, partial, invalid, unavailable, failed, denied, uncertain, and recovery outcomes and verify safe context-appropriate next actions without treating any of them as a mandatory lifecycle sequence. |
| REQ-FCD-018 | Product summaries retain governed Product identity and content, applicable Product-owned publication and visibility eligibility evidence, and any applicable Product Variant association for variant-derived evidence; negative tests prove that FCD cannot establish Product publication, Product visibility, structural sellability, Price, Inventory, Customer eligibility, Authorization, or final purchasability. |
| REQ-FCD-019 | Presenting Product Variant evidence retains Product association, stable identity, Attribute meaning, and applicable visibility evidence and cannot establish Product Variant selection, availability, or purchase commitment. |
| REQ-FCD-020 | Representative media preserves source association, order where governed, accuracy, publication, rights, alternatives, and freshness; missing or failed media produces no fabricated substitute.                                                                                                                                                                                                                                                                                                               |
| REQ-FCD-021 | Every monetary presentation carries governed amount, Currency, source, applicability, and freshness, and no frontend calculation is accepted as authoritative commercial truth.                                                                                                                                                                                                                                                                                                                               |
| REQ-FCD-022 | Availability presentation retains governed Product Variant, source, applicability, uncertainty, and freshness while proving none of Stock, Available-to-Sell, Stock Reservation, Product visibility, purchase authorization, or final purchasability.                                                                                                                                                                                                                                                         |
| REQ-FCD-023 | Stale, delayed, partial, conflicting, and unavailable evidence is qualified, degraded, removed, or revalidated according to materiality and is never presented as complete current truth.                                                                                                                                                                                                                                                                                                                     |
| REQ-FCD-024 | Tests cover stale, cancelled, reordered, and superseded asynchronous outcomes across queries, filters, facets, sorts, traversal, media, Price, and availability and verify that none can overwrite newer relevant discovery intent or presentation state.                                                                                                                                                                                                                                                     |
| REQ-FCD-025 | Supported navigation and recovery retain useful discovery context without route, URL, history, cache, or client state becoming durable or authoritative beyond its governed lifetime.                                                                                                                                                                                                                                                                                                                         |
| REQ-FCD-026 | Entry into FPE retains safe discovery return context and governed destination identity, while FCD contains no detailed evaluation, selection, review, delivery, policy, or purchase behavior.                                                                                                                                                                                                                                                                                                                 |
| REQ-FCD-027 | Withdrawn, unpublished, invalid, stale, inaccessible, unavailable, and unsupported results or destinations produce distinguishable safe outcomes without continued claims or protected-existence disclosure.                                                                                                                                                                                                                                                                                                  |
| REQ-FCD-028 | Each material region demonstrates all applicable initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery, and confirmed states without enforcing a universal sequence.                                                                                                                                                                                                                                                                                             |
| REQ-FCD-029 | Recovery preserves known context, distinguishes retry from new intent, rejects obsolete outcomes, and uses governed capabilities without specifying backend retry, replay, or reconciliation.                                                                                                                                                                                                                                                                                                                 |
| REQ-FCD-030 | Discovery remains understandable and operable under representative viewport, orientation, reflow, zoom, localization, and content-extreme conditions without fixed FCD breakpoints or layouts.                                                                                                                                                                                                                                                                                                                |
| REQ-FCD-031 | All applicable discovery controls and results complete equivalent keyboard, pointer, touch, and assistive-technology paths without an input-specific dead end.                                                                                                                                                                                                                                                                                                                                                |
| REQ-FCD-032 | Query submission, active-filter changes, sorting, traversal, result updates, empty outcomes, failures, and recovery preserve or move focus predictably and expose the resulting context without unexpected position loss.                                                                                                                                                                                                                                                                                     |
| REQ-FCD-033 | Structural and assistive-technology evidence confirms correct names, relationships, active states, collection meaning, and restrained announcements for every material discovery outcome.                                                                                                                                                                                                                                                                                                                     |
| REQ-FCD-034 | Long, short, absent, localized, delayed, invalid, and failed content or media preserves essential comprehension and unrelated result operation without clipping or invented claims.                                                                                                                                                                                                                                                                                                                           |
| REQ-FCD-035 | Security and privacy tests find no unsafe query or content rendering, Secret exposure, inferred Consent, or unnecessary Sensitive Data across client state, routes, errors, logs, telemetry, and Analytics Events, and verify retained discovery and client state remains isolated across applicable Principal, Session, Customer, and Resource boundaries. |
| REQ-FCD-036 | Unauthorized and abuse-oriented tests preserve contextual server-side Authorization and Resource concealment and disclose no inaccessible Resource existence, internal indexing detail, or sensitive state through results, counts, filters, facets, errors, or timing presentation.                                                                                                                                                                                                                          |
| REQ-FCD-037 | Large and degraded governed inputs produce bounded rendering, collection, media, reactive, and background work without requiring a locally invented numerical limit or mechanism.                                                                                                                                                                                                                                                                                                                             |
| REQ-FCD-038 | Independent failure of each optional region leaves unrelated discovery operation available and communicates the affected result set's known completeness truthfully.                                                                                                                                                                                                                                                                                                                                          |
| REQ-FCD-039 | Evidence for material discovery requests, failures, recoveries, and Contract boundaries preserves safe frontend operational telemetry and governed correlation while explicitly distinguishing operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records; verification confirms that none substitutes for another or creates authoritative Domain history, without requiring event names, payloads, providers, transports, schemas, or persistence. |
| REQ-FCD-040 | Analytics review confirms non-authority, applicable Consent, minimization, and neutrality regarding provider, taxonomy, canonical names, payloads, attribution, and ranking.                                                                                                                                                                                                                                                                                                                                  |
| REQ-FCD-041 | Every reachable flagged discovery state retains valid evidence, truthful results, context, accessibility, security, privacy, inherited obligations, and Domain authority; review confirms FCD selects no rollout policy or feature-flag implementation mechanism.                                                                                                                                                                                                                                             |
| REQ-FCD-042 | Contract review finds necessary bounded and versioned query, Category, result-set identity, result, filter, facet, sort, traversal, count, source, freshness, completeness, outcome, and correlation semantics and no FCD-defined routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, index technology, transport, or providers.                                                                                                                          |
| REQ-FCD-043 | Decision review finds every named Product and technical choice unresolved or governed elsewhere, with no local default embedded in discovery behavior.                                                                                                                                                                                                                                                                                                                                                        |
| REQ-FCD-044 | Traceable evidence maps to every FCD Requirement and observably covers authority, FEB and FSC inheritance, catalogue and Category discovery, Search boundaries, filters, facets, sorting, traversal, counts, summaries, Product Media, Price, availability, freshness, supersession, context, FPE handoff, presentation states, recovery, accessibility, responsiveness, security, privacy, Consent, boundedness, degradation, telemetry, feature flags, Contracts, and policy neutrality.                    |

## 6. Requirement Traceability

| Requirement | FEB/FSC                                                                    | Product                                                                  | Business Requirements              | Approved Domains                                                                                      | Governing Sources                                                                   | Consumers                                                        |
| ----------- | -------------------------------------------------------------------------- | ------------------------------------------------------------------------ | ---------------------------------- | ----------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | ---------------------------------------------------------------- |
| REQ-FCD-001 | REQ-FEB-001–003; REQ-FSC-001–003                                           | PRODUCT.md §§21–22, 36                                                   | REQ-BUS-047–048                    | —                                                                                                     | AGENTS.md §§5, 10.3, 14                                                             | All FCD consumers                                                |
| REQ-FCD-002 | REQ-FEB-002, 051; REQ-FSC-002–003, 043                                     | PRODUCT.md §§21–22, 26                                                   | REQ-BUS-047                        | —                                                                                                     | AGENTS.md §27.1                                                                     | FCD implementation and later storefront Specifications           |
| REQ-FCD-003 | REQ-FEB-003–004; REQ-FSC-003, 010, 019–025                                 | PRODUCT.md §§12–13, 21                                                   | REQ-BUS-003–005, 047               | REQ-PRD-001–002; REQ-CAT-001–002; REQ-SRCH-002–003; REQ-PRC-002–003; REQ-INV-002–003; REQ-CMS-002–004 | ENGINEERING-PRINCIPLES.md §§7–10                                                    | FSC, FPE, FCA, FCP, FPP, FAD, FRP                                |
| REQ-FCD-004 | REQ-FEB-009–010, 031; REQ-FSC-004–005, 021                                 | PRODUCT.md §§5.2–5.3, 8.1, 29                                            | REQ-BUS-002, 004                   | REQ-SRCH-002                                                                                          | UI.md §§10, 12, 31                                                                  | Storefront visitors and FSC                                      |
| REQ-FCD-005 | REQ-FEB-003–005, 007; REQ-FSC-020                                          | PRODUCT.md §§8.1, 12.1, 29.1; Open Product Decision 2                    | REQ-BUS-003–004                    | REQ-CAT-010, 012–020, 030–031; REQ-PRD-020, 023                                                       | UI.md §§12, 17, 32                                                                  | Storefront visitors and FPE                                      |
| REQ-FCD-006 | REQ-FEB-009–010; REQ-FSC-009–011, 020                                      | PRODUCT.md §§12.1, 29.1, 29.4                                            | REQ-BUS-003–004                    | REQ-CAT-005–009, 017–019                                                                              | UI.md §§12, 32; ACCESSIBILITY.md §§7, 11, 48                                        | Storefront visitors                                              |
| REQ-FCD-007 | REQ-FEB-004–007, 042; REQ-FSC-027, 038                                     | PRODUCT.md §§8.1–8.3, 12.1                                               | REQ-BUS-003–004, 038, 042          | REQ-PRD-023–026, 042; REQ-CAT-012–014, 039; REQ-SRCH-005, 025                                         | UI.md §§17, 23, 49                                                                  | Storefront visitors and FPE                                      |
| REQ-FCD-008 | REQ-FEB-003–005, 014; REQ-FSC-021                                          | PRODUCT.md §§8.1, 12.1                                                   | REQ-BUS-004                        | REQ-SRCH-002, 006–008, 013–018                                                                        | UI.md §31; DESIGN-SYSTEM.md §43                                                     | Storefront visitors                                              |
| REQ-FCD-009 | REQ-FEB-006, 014–017; REQ-FSC-029, 031                                     | PRODUCT.md §§5.4–5.6                                                     | REQ-BUS-004, 042                   | REQ-SRCH-004, 006, 024, 027                                                                           | UI.md §§21, 23, 31; ANGULAR.md §§18, 37–38                                          | Storefront visitors                                              |
| REQ-FCD-010 | REQ-FEB-003–005, 007; REQ-FSC-019–023                                      | PRODUCT.md §§12–13, 16.1–16.2                                            | REQ-BUS-003–005, 014–018           | REQ-PRD-023–024, 032–033; REQ-CAT-030; REQ-SRCH-021, 025, 033; REQ-PRC-003, 019; REQ-INV-003, 007     | UI.md §§25–26, 31                                                                   | Storefront visitors and FPE                                      |
| REQ-FCD-011 | REQ-FEB-009, 014, 032; REQ-FSC-029                                         | PRODUCT.md §§8.1, 12.1; Open Product Decisions 2–3                       | REQ-BUS-004                        | REQ-PRD-011–013; REQ-CAT-010–014; REQ-SRCH-013                                                        | UI.md §32; DESIGN-SYSTEM.md §44                                                     | Storefront visitors                                              |
| REQ-FCD-012 | REQ-FEB-009, 014, 032; REQ-FSC-029                                         | PRODUCT.md §§8.1, 12.1; Open Product Decisions 2–3                       | REQ-BUS-004                        | REQ-PRD-011–013; REQ-CAT-010–014; REQ-SRCH-014, 018                                                   | UI.md §32; DESIGN-SYSTEM.md §44                                                     | Storefront visitors                                              |
| REQ-FCD-013 | REQ-FEB-009–010, 032; REQ-FSC-029                                          | PRODUCT.md §§8.1, 12.1                                                   | REQ-BUS-004                        | REQ-CAT-019; REQ-SRCH-015–016                                                                         | UI.md §32; DESIGN-SYSTEM.md §44                                                     | Storefront visitors                                              |
| REQ-FCD-014 | REQ-FEB-009, 014, 021, 042; REQ-FSC-029, 031, 038                          | PRODUCT.md §§5.3, 8.1                                                    | REQ-BUS-004, 038, 042              | REQ-SRCH-017–018, 025                                                                                 | UI.md §32; DESIGN-SYSTEM.md §45                                                     | Storefront visitors                                              |
| REQ-FCD-015 | REQ-FEB-007, 009, 017, 042; REQ-FSC-031                                    | PRODUCT.md §§5.4–5.6                                                     | REQ-BUS-004, 042                   | REQ-SRCH-017, 025–028                                                                                 | UI.md §§23, 32, 45                                                                  | Storefront visitors                                              |
| REQ-FCD-016 | REQ-FEB-007, 014–015; REQ-FSC-029                                          | PRODUCT.md §§5.4–5.6, 8.1                                                | REQ-BUS-004, 042                   | REQ-SRCH-018, 025                                                                                     | UI.md §§22–23, 31                                                                   | Storefront visitors                                              |
| REQ-FCD-017 | REQ-FEB-014–018, 020–021; REQ-FSC-029, 031                                 | PRODUCT.md §§5.4–5.6, 17.2                                               | REQ-BUS-004, 042                   | REQ-SRCH-023–025, 031                                                                                 | UI.md §§21–23; ACCESSIBILITY.md §§24–26                                             | Storefront visitors                                              |
| REQ-FCD-018 | REQ-FEB-003–005, 007, 041; REQ-FSC-019, 027–028                            | PRODUCT.md §§8.2, 14.2, 16.1                                             | REQ-BUS-003, 005                   | REQ-PRD-003–004, 018, 021–026, 033                                                                    | DESIGN-SYSTEM.md §§31, 46; UI.md §§17, 33                                           | Storefront visitors and FPE                                      |
| REQ-FCD-019 | REQ-FEB-003–005, 007, 041; REQ-FSC-019                                     | PRODUCT.md §§8.2, 16.1; Open Product Decision 3                          | REQ-BUS-005–006                    | REQ-PRD-007–013, 022–026; REQ-SRCH-010                                                                | DESIGN-SYSTEM.md §§31, 33, 46                                                       | Storefront visitors and FPE                                      |
| REQ-FCD-020 | REQ-FEB-007, 033, 037, 041; REQ-FSC-027–028, 036                           | PRODUCT.md §§8.2, 16.9                                                   | REQ-BUS-005, 050                   | REQ-PRD-014–017, 025–027; REQ-SRCH-011; REQ-CMS-021, 030                                              | UI.md §33; ACCESSIBILITY.md §§43, 46, 73                                            | Storefront visitors and FPE                                      |
| REQ-FCD-021 | REQ-FEB-003–005, 007, 037; REQ-FSC-022                                     | PRODUCT.md §§5.4, 16.1                                                   | REQ-BUS-014–016                    | REQ-PRC-002–003, 006–011, 019, 029, 033–034; REQ-SRCH-019                                             | UI.md §26; ACCESSIBILITY.md §29                                                     | Storefront visitors and FPE                                      |
| REQ-FCD-022 | REQ-FEB-003–005, 007, 037; REQ-FSC-023                                     | PRODUCT.md §§5.4, 16.2; Open Product Decision 17                         | REQ-BUS-017–018                    | REQ-INV-002–003, 005–009, 021, 031, 035–036; REQ-SRCH-020–021                                         | UI.md §25; ACCESSIBILITY.md §28                                                     | Storefront visitors and FPE                                      |
| REQ-FCD-023 | REQ-FEB-005, 007, 014–015, 020; REQ-FSC-018, 023, 028–029                  | PRODUCT.md §§5.4–5.6, 17.2                                               | REQ-BUS-004–005, 042, 045          | REQ-PRD-017, 027, 033, 040; REQ-CAT-009, 030, 042; REQ-SRCH-024–025, 033; REQ-CMS-036–037, 040        | UI.md §§23, 45                                                                      | Storefront visitors and operations                               |
| REQ-FCD-024 | REQ-FEB-016–017; REQ-FSC-031                                               | PRODUCT.md §§5.4–5.6                                                     | REQ-BUS-004, 042                   | REQ-SRCH-027–028                                                                                      | ANGULAR.md §§18, 37–38                                                              | Storefront visitors                                              |
| REQ-FCD-025 | REQ-FEB-006, 009–010; REQ-FSC-005, 011, 031                                | PRODUCT.md §§5.6, 14, 29–30                                              | REQ-BUS-002, 004, 042              | REQ-SRCH-004–005, 017                                                                                 | UI.md §§12, 32, 45; ACCESSIBILITY.md §11                                            | Storefront visitors and FPE                                      |
| REQ-FCD-026 | REQ-FEB-009–010; REQ-FSC-003, 010, 019                                     | PRODUCT.md §§8.2, 14.2                                                   | REQ-BUS-005–006                    | REQ-PRD-003–004, 007–008, 023–026                                                                     | DESIGN-SYSTEM.md §§31–33                                                            | FPE and storefront visitors                                      |
| REQ-FCD-027 | REQ-FEB-014–015, 020–021, 030; REQ-FSC-012, 028–031                        | PRODUCT.md §§5.4–5.6, 17.2                                               | REQ-BUS-003–004, 042               | REQ-PRD-023, 040; REQ-CAT-007, 027, 042; REQ-SRCH-024–025, 031, 037, 039                              | UI.md §23; SECURITY-STANDARDS.md §18                                                | Storefront visitors                                              |
| REQ-FCD-028 | REQ-FEB-014–018, 020; REQ-FSC-029                                          | PRODUCT.md §§5.4–5.6, 17.2                                               | REQ-BUS-004, 042                   | REQ-SRCH-023–025                                                                                      | UI.md §§21–23; ACCESSIBILITY.md §§24–26                                             | Storefront visitors                                              |
| REQ-FCD-029 | REQ-FEB-017, 020–021; REQ-FSC-031                                          | PRODUCT.md §§5.6, 17.2                                                   | REQ-BUS-042, 045                   | REQ-SRCH-024, 027, 031–033                                                                            | ANGULAR.md §§37–38                                                                  | Storefront visitors and operations                               |
| REQ-FCD-030 | REQ-FEB-031, 033–034, 041; REQ-FSC-032                                     | PRODUCT.md §§5.2–5.3, 5.7                                                | REQ-BUS-002, 037                   | REQ-PRD-041–042; REQ-CAT-038–039; REQ-SRCH-040–041                                                    | ACCESSIBILITY.md §§35–38, 55, 72–73; UI.md §§10, 58, 62                             | Storefront visitors                                              |
| REQ-FCD-031 | REQ-FEB-032, 034–035, 037; REQ-FSC-033                                     | PRODUCT.md §5.7                                                          | REQ-BUS-037                        | REQ-SRCH-040                                                                                          | ACCESSIBILITY.md §§8–10, 17, 23, 41–42, 53–54                                       | Storefront visitors                                              |
| REQ-FCD-032 | REQ-FEB-009–010, 016–017, 035–038; REQ-FSC-035                             | PRODUCT.md §§5.6–5.7                                                     | REQ-BUS-037, 042                   | REQ-SRCH-023–024, 040                                                                                 | ACCESSIBILITY.md §§9, 11, 24–26; UI.md §40                                          | Storefront visitors                                              |
| REQ-FCD-033 | REQ-FEB-034, 037–038; REQ-FSC-034–036                                      | PRODUCT.md §5.7                                                          | REQ-BUS-037, 042                   | REQ-PRD-041; REQ-CAT-038; REQ-SRCH-040                                                                | ACCESSIBILITY.md §§6–7, 23–26, 46; UI.md §§31–32                                    | Storefront visitors                                              |
| REQ-FCD-034 | REQ-FEB-033, 041, 044; REQ-FSC-028, 030, 032, 036                          | PRODUCT.md §§5.3–5.6, 8.2                                                | REQ-BUS-005, 037, 042, 050         | REQ-PRD-015–017, 041–042; REQ-CMS-030, 034, 040, 042                                                  | ACCESSIBILITY.md §§43, 46, 73; UI.md §§33, 58                                       | Storefront visitors                                              |
| REQ-FCD-035 | REQ-FEB-008, 026–030; REQ-FSC-037                                          | PRODUCT.md §§5.1, 16.7, 20                                               | REQ-BUS-032, 039–040               | REQ-PRD-051; REQ-CAT-037; REQ-SRCH-035, 037–039                                                       | SECURITY-STANDARDS.md §§12, 14, 18, 27, 33, 35; ANGULAR.md §§43–47                  | Storefront visitors, Security and Privacy owners                 |
| REQ-FCD-036 | REQ-FEB-012–013, 020, 030; REQ-FSC-009–012, 037                            | PRODUCT.md §§5.1, 20                                                     | REQ-BUS-032, 039, 052              | REQ-PRD-050–051; REQ-CAT-036–037; REQ-SRCH-037–039                                                    | SECURITY-STANDARDS.md §§10–13, 18, 33, 35                                           | Storefront visitors and Security owners                          |
| REQ-FCD-037 | REQ-FEB-042–044; REQ-FSC-038                                               | PRODUCT.md §§8.3, 18.4–18.6                                              | REQ-BUS-038, 042, 045              | REQ-PRD-042; REQ-CAT-039; REQ-SRCH-041–042                                                            | PERFORMANCE.md §§16–17, 21, 35, 43–48, 54–57, 83                                    | Storefront visitors and operations                               |
| REQ-FCD-038 | REQ-FEB-020, 043–044; REQ-FSC-030, 038                                     | PRODUCT.md §§5.4–5.6, 8.3                                                | REQ-BUS-038, 042, 045              | REQ-PRD-017, 040; REQ-SRCH-024, 041–042                                                               | PERFORMANCE.md §§17, 21, 23, 35, 45, 83                                             | Storefront visitors and operations                               |
| REQ-FCD-039 | REQ-FEB-045–046; REQ-FSC-039                                               | PRODUCT.md §§18.3, 33                                                    | REQ-BUS-043                        | REQ-SRCH-043                                                                                          | ANGULAR.md §61; SECURITY-STANDARDS.md §§27–28, 35                                   | Engineering and operations                                       |
| REQ-FCD-040 | REQ-FEB-026, 029, 046–047; REQ-FSC-039                                     | PRODUCT.md §§18.3, 33; Open Product Decisions 19–20                      | REQ-BUS-039–040, 043–044           | REQ-SRCH-035, 047                                                                                     | EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §§27–28                               | Product, Analytics and Privacy owners                            |
| REQ-FCD-041 | REQ-FEB-048; REQ-FSC-040                                                   | PRODUCT.md §§5.9, 21, 36–38; Open Product Decision 30                    | —                                   | —                                                                                                     | ARCHITECTURE.md §§38.4, 43.3; ANGULAR.md §§62–63                                    | Storefront visitors and delivery teams                           |
| REQ-FCD-042 | REQ-FEB-005, 007, 014–015, 020, 042, 049; REQ-FSC-041                      | PRODUCT.md §§21–22, 26                                                   | REQ-BUS-004, 042, 046–047          | REQ-PRD-052; REQ-CAT-041–042; REQ-SRCH-045; REQ-PRC-033–034; REQ-INV-035–036; REQ-CMS-045             | ARCHITECTURE.md §§13, 36.5, 39; API.md §§9–13, 16–17, 23, 35–37, 63–72              | FCD implementation and Contract owners                           |
| REQ-FCD-043 | REQ-FEB-040, 047–050; REQ-FSC-042                                          | PRODUCT.md §§21, 25–26, 36–38; Open Product Decisions 2–3, 17, 19–20, 30 | REQ-BUS-038, 046, 048              | REQ-SRCH-036, 048–049                                                                                 | DECISIONS.md §§25–40; ENGINEERING-PRINCIPLES.md §§11, 24, 36                        | Product, Design, Engineering and later storefront Specifications |
| REQ-FCD-044 | REQ-FEB-001–003, 014, 020–021, 026–030, 034, 042–051; REQ-FSC-002–003, 043 | PRODUCT.md §§22, 26, 35                                                  | REQ-BUS-002–005, 037–039, 042, 047 | REQ-PRD-044; REQ-CAT-043; REQ-SRCH-050; REQ-PRC-035; REQ-INV-037; REQ-CMS-051                         | TESTING-STANDARDS.md §§5, 7, 9, 11, 13, 19–24, 27–31, 33–40; ANGULAR.md §§56–60, 68 | Engineering, QA, Accessibility, Security and all FCD consumers   |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following six are materially relevant to FCD, retain their exact source wording and order, and remain unresolved by this Draft:

| Source Order | Open Product Decision                                                          | FCD Boundary                                                                                                                                                                                                                         |
| ------------ | ------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 2            | Initial product categories and catalogue taxonomy.                             | FCD consumes governed Category and catalogue evidence but selects no Category, taxonomy, hierarchy, membership, label, order, filter, or navigation policy and does not resolve this Decision.                                       |
| 3            | Size, fit, colour, material, and other Product Variant or Attribute standards. | FCD may present governed Attributes as summary, filter, or facet evidence but selects no Attribute taxonomy, vocabulary, normalization, allowed value, display priority, or Product Variant rule and does not resolve this Decision. |
| 17           | Low-stock and out-of-stock customer messaging.                                 | FCD may present governed availability evidence but selects no threshold, wording, visual treatment, suppression, ordering, or Customer-facing behavior and does not resolve this Decision.                                           |
| 19           | Marketing-consent and communication-preference model.                          | FCD preserves governed Consent and communication-preference authority but selects no model, defaults, categories, capture mechanism, or policy and does not resolve this Decision.                                                   |
| 20           | Initial analytics provider and event taxonomy.                                 | FCD permits governed non-authoritative Analytics Events but selects no provider, taxonomy, canonical event name, payload, attribution, SDK, destination, or ranking use and does not resolve this Decision.                          |
| 30           | Product launch date, release scope, and post-launch support window.            | FCD permits only governed reachable discovery states and selects no launch date, initial catalogue scope, rollout, feature set, support window, or numerical target and does not resolve this Decision.                              |

## 8. Risks and Controls

| Risk                                                                       | Control                                                                                                                                                              |
| -------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FCD becomes an alternative catalogue authority.                            | Require every material discovery representation to retain governed source evidence and prohibit frontend state from establishing Product, Category, or Search truth. |
| Draft, withdrawn, unauthorized, or invisible Products remain discoverable. | Require applicable current Product publication and visibility evidence and test ineligible source states negatively.                                                 |
| Stale results appear current.                                              | Preserve freshness and completeness evidence and require qualification, degradation, removal, or governed revalidation when current truth is material.               |
| Filters or facets invent Product or Category meaning.                      | Bind every available value and result effect to governed Search, Product, or Category evidence and reject client-created taxonomy.                                   |
| FCD presentation becomes Search ranking authority.                         | Preserve governed Search ordering evidence and prohibit local prominence or reordering from being represented as ranking truth.                                      |
| Result counts overstate completeness.                                      | Bind counts to explicit scope, freshness, and completeness evidence and qualify or omit counts when exactness is unsupported.                                        |
| Traversal duplicates, skips, reorders, or combines results incorrectly.    | Retain result-set identity and ordering context and verify continuation, refresh, return, and repeated traversal against known completeness.                         |
| Delayed results overwrite newer discovery intent.                          | Associate outcomes with initiating intent and reject stale, cancelled, reordered, or superseded completions.                                                         |
| Price presentation becomes commercial authority.                           | Require governed Money, Currency, source, applicability, and freshness and prohibit authoritative frontend calculation.                                              |
| Availability presentation becomes Inventory authority.                     | Preserve governed Product Variant and freshness context and prohibit discovery evidence from establishing Stock or purchasability.                                   |
| Missing media or content blocks discovery or creates false meaning.        | Isolate optional failures and preserve accessible essential summaries without fabricating replacement claims.                                                        |
| Dynamic result changes cause focus or context loss.                        | Verify predictable focus, retained discovery position, accessible status, and restrained announcements for result changes.                                           |
| Large result sets create unbounded client work.                            | Require bounded result delivery and rendering based on governed inputs and performance evidence without fixing a local numerical limit.                              |
| Protected Resources become enumerable through results or metadata.         | Preserve contextual server-side Authorization and concealment across results, counts, facets, errors, and timing presentation.                                       |
| Query or governed content enables unsafe rendering.                        | Apply approved input and rendering safeguards and prevent query-derived or managed content from unsafe execution or navigation.                                      |
| Discovery context leaks across Principals or Sessions.                     | Keep retained query and result state lifecycle-bounded and isolated at governed Principal, Session, Customer, and Resource boundaries.                               |
| Analytics bypasses Consent or captures Sensitive Data.                     | Consume governed Consent where required, minimize evidence, and keep analytics separate from operational telemetry and Domain truth.                                 |
| Feature flags create invalid or inaccessible discovery combinations.       | Verify every reachable flag state for valid evidence, truthful outcomes, inherited obligations, accessibility, security, and privacy.                                |
| Contract evolution silently misrepresents result semantics.                | Require versioned bounded identity, source, freshness, completeness, outcome, and traversal semantics with safe failure.                                             |
| FCD absorbs Product evaluation or backend Search behavior.                 | Test that result summaries remain discovery evidence and destination entry while detailed evaluation and Search-owned processing remain outside FCD.                 |
| Product policy or provider details become normative through presentation.  | Reject taxonomy, ranking, merchandising, personalization, provider, protocol, route, schema, cache, index, and numerical mechanisms from FCD Requirements.           |

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
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cms/cms-domain.md`

## 10. Revision History

| Version | Date       | Status | Summary                                                      |
| ------- | ---------- | ------ | ------------------------------------------------------------ |
| 0.1.0   | 2026-09-08 | Draft  | Initial comprehensive Catalogue and Discovery Specification. |
| 1.0.0 | 2026-09-08 | Approved | Approved Catalogue and Discovery Specification. |

## 11. Final Validation

Before this Approved baseline is committed, verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, and scope is `FCD`, with normativity only within the Catalogue and Discovery scope and no repository-wide authority;
2. governing-source precedence, Product and Business Requirements, FEB and FSC inheritance, and every Approved Domain's authority remain preserved;
3. FCD owns only catalogue-and-discovery presentation and remains separate from FSC, FPE, FCA, FCP, FPP, FAD, and FRP;
4. Product, Product Variant, Product Media, Category, Search, Pricing, Inventory, CMS, Customer, Identity, Cart, Checkout, Payment, Reporting, Analytics, Consent, and Product-policy boundaries remain explicit;
5. Category, Search, query, filter, facet, sort, traversal, count, Product summary, Product Variant, media, Price, and availability evidence remains governed and non-authoritative in FCD;
6. initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery, and confirmed presentation states remain distinguishable without becoming a mandatory lifecycle graph;
7. asynchronous supersession, context preservation, invalid destinations, recovery, and safe FPE handoff remain correct without defining backend mechanisms or downstream behavior;
8. responsive, reflow, zoom, orientation, localization, content-extreme, keyboard, pointer, touch, focus, assistive-technology, structure, state, and status evidence supports the applicable WCAG 2.2 AA outcome;
9. safe rendering, contextual Authorization, Resource concealment, state isolation, Sensitive Data, Secrets, privacy, abuse resistance, and Consent non-inference are preserved;
10. result and media work remains bounded, optional failures are isolated, and degradation remains truthful without numerical targets;
11. operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records remain distinct and analytics remains minimized, consent-aware, non-authoritative, provider-neutral, and taxonomy-neutral;
12. every feature-flag state preserves valid discovery evidence, truthful presentation, security, privacy, accessibility, inherited obligations, and Domain authority;
13. Contracts remain abstract, bounded, and sufficient for truthful discovery without routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, index technology, transport, or providers;
14. Requirements, Acceptance Criteria, and traceability are equal in count, unique, sequential, gap-free, and one-to-one;
15. every FEB, FSC, Business Requirement, Approved Domain Requirement, Product section, and governing-source citation physically exists and directly supports its FCD Requirement;
16. the six represented Open Product Decisions are materially complete for FCD, exactly match `PRODUCT.md`, remain source-ordered, and are unresolved;
17. Risks have distinct FCD-specific controls and all Related Document paths exist;
18. Revision History contains exactly the preserved `0.1.0 Draft` row and one `1.0.0 Approved` row;
19. no Glossary amendment is required and FCD-local descriptions are not repository-wide terminology;
20. no Product policy, provider, protocol, route, URL, API, schema, persistence, cache or index mechanism, event taxonomy, payload, ranking algorithm, relevance formula, numerical target, timeout, retry count, breakpoint, layout, visual token, lifecycle graph, or implementation mechanism is introduced;
21. Markdown headings and tables, UTF-8, trailing whitespace, exactly one final newline, and prohibited-marker checks pass; and
22. Git scope contains only the authorized lifecycle-promotion changes to `specifications/frontend/storefront/catalogue-discovery.md`, with nothing staged, untracked, unrelated, or otherwise modified.
