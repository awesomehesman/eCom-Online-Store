---
title: Search and Discovery Backend Specification
version: 1.0.0
status: Approved
owner: Backend
last_updated: 2026-09-19
authoritative: false
scope: BSRCH
---

# Search and Discovery Backend Specification

## 1. Purpose

This Approved Specification defines the implementation-facing, Search-and-Discovery-only backend obligations authorized by Accepted ADR-0009. Its Requirements are normative only within the governed BSRCH scope. It remains `authoritative: false`, subordinate to higher governing sources, and resolves no Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BSRCH-REQ-001 — Lifecycle, Authority, and Scope
BSRCH MUST retain scope `BSRCH`, `authoritative: false`, `1.0.0` Approved lifecycle metadata, Search-and-Discovery-only decomposition, normativity only within BSRCH, and authority bounded to specialization of the Approved Search and Discovery Domain.

### BSRCH-REQ-002 — Search-Owned Backend Truth
BSRCH MAY own only Search request and result identity, query interpretation, Search Index and Projection operational state, matching, Filter, Facet, Sort Order, Product Ranking, pagination, indexing, failure, recovery, and reconciliation outcomes.

### BSRCH-REQ-003 — Cross-Domain Non-Authority
BSRCH MUST NOT own, mutate, or redefine Product, Product Variant, Product Media, Category, Pricing, Inventory, Identity, Customer, Account, Consent, CMS, Cart, Checkout, Payment, Order, Shipping, Return, Notifications, Administration, Reporting, final purchasability, or other source-Domain truth.

### BSRCH-REQ-004 — Stable Search Request Identity
Each accepted backend Search request MUST have stable identity distinct from query text, Principal, Session, UI route, page position, correlation identifier, provider identifier, and source Resource identity without selecting an identifier mechanism.

### BSRCH-REQ-005 — Stable Result Identity
Each Search result context and its pages MUST remain correlated to its governing request, applicable Projection state, and result metadata without treating Product identifiers or navigation tokens as result identity.

## 3. Query and Discovery Behavior

### BSRCH-REQ-006 — Request Validation
BSRCH MUST validate supported query criteria, Filters, Facets, Sort Order, pagination context, encoding, bounds, and applicable access context and reject malformed, unsupported, unsafe, or ambiguous input without exposing protected internals.

### BSRCH-REQ-007 — Normalization Boundary
BSRCH MAY normalize a Search Query only under governed semantics, MUST make applied interpretation explainable, and MUST NOT invent synonym, spelling, language, stemming, tokenization, or relevance policy.

### BSRCH-REQ-008 — Keyword Discovery
Where keyword Search is governed, BSRCH MUST match only eligible projected fields and return a truthful empty, denied, failed, unavailable, partial, stale, or uncertain outcome rather than fabricate a match.

### BSRCH-REQ-009 — Product Eligibility Projection
Only Product-owned evidence eligible for discovery under Approved policy MAY enter a BSRCH Projection, and withdrawal, archival, correction, deletion, or eligibility change MUST remain traceable and reconcilable without BSRCH publishing Product truth.

### BSRCH-REQ-010 — Product Variant and Attribute Boundary
BSRCH MAY apply governed Product Variant or Attribute criteria only to eligible BPRD evidence and MUST NOT establish Variant membership, identity, lifecycle, or Attribute meaning.

### BSRCH-REQ-011 — Product Media Boundary
BSRCH MAY represent eligible Product Media references while preserving BPRD publication, accuracy, accessibility, correction, transformation, storage, delivery, and provider authority.

### BSRCH-REQ-012 — Category-Aware Discovery
Category criteria MUST consume governed BCAT evidence and preserve Category taxonomy, hierarchy, membership, classification, navigation eligibility, ordering, lifecycle, and historical authority.

### BSRCH-REQ-013 — Filter Semantics
Each supported Filter MUST use governed field meaning, operator semantics, applicability, and combination behavior, with explicit invalid or unsupported outcomes and no locally invented Product policy.

### BSRCH-REQ-014 — Facet Semantics
Each supported Facet MUST identify its governed source and authorized counting scope and MUST NOT imply inaccessible, unavailable, or authoritative Product, Pricing, Category, or Inventory truth.

### BSRCH-REQ-015 — Sort Semantics
Each supported Sort Order MUST have governed deterministic meaning for equivalent accepted evidence and MUST NOT invent price, popularity, recency, merchandising, or tie-breaking policy.

### BSRCH-REQ-016 — Ranking Boundary
Product Ranking MUST remain a Search-owned outcome of Approved policy and accepted evidence and MUST NOT redefine Product, Category, Pricing, Inventory, Customer, CMS, or Administration truth or select a ranking algorithm, formula, or weight.

### BSRCH-REQ-017 — Pagination Integrity
Pagination MUST preserve the accepted query, Filter, Sort Order, authorization, and Projection context and expose change or staleness explicitly without prescribing page size, result limits, or token design.

### BSRCH-REQ-018 — Result Composition and Metadata
Search results MUST expose the applicable criteria, source, freshness, completeness, exclusions, pagination, uncertainty, and non-authoritative status needed for truthful interpretation without prescribing a DTO or document shape.

## 4. Source Projections and Revalidation

### BSRCH-REQ-019 — Pricing Projection Boundary
BSRCH MAY represent governed BPRC Price, Discount, Promotion, Money, and Currency evidence for eligible discovery but MUST NOT calculate authoritative commercial truth or imply current price validity.

### BSRCH-REQ-020 — Inventory Projection Boundary
BSRCH MAY represent governed BINV availability evidence but MUST NOT calculate Stock, Available-to-Sell, Stock Reservation, commitment, scarcity policy, or final availability.

### BSRCH-REQ-021 — Purchasability Non-Authority
A BSRCH result MUST NOT prove current Product publication, Price, Inventory, Customer eligibility, Promotion eligibility, delivery eligibility, or purchasability, and downstream owners MUST revalidate current truth where required.

### BSRCH-REQ-022 — CMS Boundary
BSRCH MAY project eligible CMS evidence only from governed CMS-owned publication state and MUST reconcile withdrawal, supersession, correction, or placement change without defining CMS workflow, approval, scheduling, provider, or backend roadmap identity.

### BSRCH-REQ-023 — Empty Result Outcome
A valid Search with no eligible matches MUST produce an explicit empty outcome distinct from invalid, denied, unavailable, failed, partial, stale, and uncertain outcomes.

### BSRCH-REQ-024 — Failure and Uncertainty
Validation, authorization, source, indexing, dependency, timeout, partial, stale, conflicting, and unknown-effect conditions MUST remain distinguishable from success and retain safe evidence for diagnosis and governed recovery.

### BSRCH-REQ-025 — Freshness and Completeness
BSRCH MUST expose or make determinable applicable Projection freshness, source cutoff, completeness, exclusions, delay, and unknown coverage without selecting a freshness duration or presenting stale or partial evidence as current and complete.

## 5. Consistency, Indexing, and Recovery

### BSRCH-REQ-026 — Duplicate and Replay Safety
Duplicate or replayed requests and source evidence MUST NOT duplicate projected meaning, restore obsolete evidence, bypass Authorization, or multiply harmful effects.

### BSRCH-REQ-027 — Reordered and Superseded Evidence
Late, reordered, superseded, or conflicting evidence MUST preserve provenance and MUST NOT regress later accepted Search state or silently choose source truth.

### BSRCH-REQ-028 — Concurrent Update Integrity
Concurrent indexing, rebuild, correction, and query activity MUST preserve accepted newer evidence and expose conflict, stale, partial, or uncertain outcomes when coherent results cannot be established.

### BSRCH-REQ-029 — Indexing Outcomes
Indexing requested, accepted, processing, complete, partial, failed, unavailable, superseded, and uncertain outcomes MUST remain distinguishable and correlated to eligible source evidence without selecting an indexing mechanism.

### BSRCH-REQ-030 — Rebuild and Backfill
A governed rebuild or backfill MUST identify authorized scope, source boundary, progress, outcome, and uncertainty and MUST NOT restore ineligible or superseded evidence or overwrite unrelated newer state.

### BSRCH-REQ-031 — Recovery
Recovery after failed query, indexing, rebuild, backfill, or dependency activity MUST be explicit, authorized where required, evidence-based, duplicate-safe, bounded by current source truth, and neutral about retries and recovery mechanisms.

### BSRCH-REQ-032 — Reconciliation
BSRCH MUST reconcile derived state with governed Product, Category, Pricing, Inventory, and conditional CMS evidence while preserving discrepancies, provenance, corrections, and unresolved uncertainty and without mutating sources.

### BSRCH-REQ-033 — Current-Truth Revalidation
Where a consumer needs current authoritative truth, BSRCH MUST direct or enable revalidation through the owning Domain rather than treat any result, index, document, cache, ranking, or Projection as proof.

## 6. Actors, Authorization, Security, and Privacy

### BSRCH-REQ-034 — Actor Separation
Visitor, Customer, Account, Identity, Principal, Session, and Staff User MUST remain distinct, and Search association or history MUST NOT establish identity, ownership, Authentication, Consent, or authority.

### BSRCH-REQ-035 — Consent and Preference Boundary
Personalized or measured Search behavior MUST consume governed purpose, Consent, and Preference evidence where required and MUST NOT infer them from identity, Session, query, click, purchase, or prior activity.

### BSRCH-REQ-036 — Personalization Neutrality
BSRCH MUST NOT select whether personalization applies, which evidence it uses, its ranking effect, segmentation, or synchronization behavior until Approved policy governs those choices.

### BSRCH-REQ-037 — Contextual Authorization
Every protected query, result, projection operation, rebuild, backfill, recovery, or reconciliation action MUST enforce current trusted server-side contextual Authorization for Principal, Resource, action, purpose, and state; search evidence MUST NOT become authorization authority.

### BSRCH-REQ-038 — Isolation and Sensitive Data
BSRCH MUST enforce population and object isolation, minimization, masking where governed, enumeration resistance, and safe handling across requests, derived state, results, logs, telemetry, exports, events, and recovery evidence.

### BSRCH-REQ-039 — Abuse Resistance
BSRCH MUST resist applicable enumeration, scraping, injection, query amplification, automation abuse, and resource exhaustion under governed Security and Product policy without selecting thresholds, rate mechanisms, or blocking policy.

### BSRCH-REQ-040 — Accessible Search Contracts
BSRCH Contracts and outcomes MUST provide stable, distinguishable meaning sufficient for applicable WCAG 2.2 AA Search experiences, including criteria, results, status, empty/error states, and recovery, without selecting frontend implementation.

## 7. Operations, Contracts, and Verification

### BSRCH-REQ-041 — Bounded Search Operations
Queries, result navigation, Facets, histories, indexing, rebuilds, backfills, and reconciliation MUST support bounded execution without unbounded scans or collections and without inventing a numerical limit.

### BSRCH-REQ-042 — Workload Isolation and Degradation
Search workload and failure MUST NOT compromise transactional correctness or source availability and MUST support explicit governed degradation or unavailability without selecting topology or numerical targets.

### BSRCH-REQ-043 — Observability
Material query, indexing, dependency, rebuild, backfill, recovery, and reconciliation activity MUST be observable, correlated, attributable where required, and diagnosable without exposing Sensitive Data or defining numerical targets.

### BSRCH-REQ-044 — Proportional Audit Records
Material configuration, protected indexing, rebuild, backfill, correction, recovery, reconciliation, export, security-sensitive, and high-Risk Search actions MUST produce proportional Audit Records while routine public searches MUST NOT automatically be classified as high-Risk.

### BSRCH-REQ-045 — API and Contract Boundary
BSRCH MUST expose only necessary, intentional, compatible, versionable Contracts for query, Filter, Facet, Sort Order, pagination, result, freshness, failure, uncertainty, indexing, and recovery semantics without selecting routes, methods, statuses, DTOs, schemas, engines, or provider payloads.

### BSRCH-REQ-046 — Conditional Events and Projection Inputs
Where separately governed, Search events and consumed source facts MUST preserve Domain versus Integration Event separation, source authority, correlation, privacy, compatibility, ordering uncertainty, replay safety, and truthful completed outcomes without selecting names, envelopes, schemas, topics, brokers, transports, or analytics taxonomy.

### BSRCH-REQ-047 — Analytics Boundary
Search analytics MAY observe governed Search behavior but MUST remain distinct from Domain and Integration Events and MUST NOT become Search, Product, Customer, commercial, Consent, Authorization, or conversion truth.

### BSRCH-REQ-048 — Port and Provider Neutrality
Customer-facing and administrative Use Cases MUST access Search through project-owned Ports that preserve BSRCH Contract semantics, so changing or introducing an implementation does not rewrite those Use Cases or leak a provider, engine, protocol, schema, or persistence mechanism.

### BSRCH-REQ-049 — Policy, Mechanism, and Roadmap Neutrality
BSRCH MUST NOT prescribe providers, engines, protocols, APIs, DTOs, schemas, persistence, indexes, queues, caches, ranking formulas, facets, normalization, personalization, merchandising, synchronization, refresh, retry, page, retention, operational targets, complete lifecycle graphs, or any backend position after BSRCH.

### BSRCH-REQ-050 — Complete Verification
Verification MUST cover every BSRCH Requirement, Domain specialization, BEB inheritance, authority boundary, query and result behavior, projection consistency, security, privacy, failure, recovery, compatibility, observability, audit, Contract, event, accessibility, boundedness, and neutrality obligation without selecting tooling or numerical coverage targets.

### BSRCH-REQ-051 — Complete BEB Inheritance
BSRCH MUST inherit every materially applicable BEB Requirement, retain conditional BEB obligations if later governance makes them applicable, and use the applicability matrix as explicit accounting rather than duplicating or weakening BEB text.

### BSRCH-REQ-052 — Modular, Hexagonal, and Data-Ownership Boundary
BSRCH MUST keep Search rules within its modular Domain/Application boundary, direct dependencies inward through project-owned Ports, own only Search-derived operational data, and prevent persistence or Adapter concerns from becoming Search policy or source-Domain authority.

### BSRCH-REQ-053 — Transaction and External-Effect Boundary
BSRCH MUST define explicit Search-owned consistency and transaction boundaries, MUST NOT hold unsafe local transactions across external calls, and MUST preserve durable uncertainty, idempotency, replay safety, and reconciliation where an external effect or asynchronous workflow is governed.

### BSRCH-REQ-054 — Configuration and Feature-State Safety
Configuration and any governed feature state MUST be validated, environment-safe, observable, reversible where required, and unable to bypass Authorization, privacy, source authority, compatibility, or other mandatory safeguards; no configuration or feature-flag mechanism is selected.

### BSRCH-REQ-055 — Compatibility and Migration Safety
Contract, derived-data, index, configuration, and deployment evolution MUST preserve governed backward compatibility, migration safety, rollback or recovery evidence, and mixed-version behavior without selecting a physical migration, deployment, index, or persistence strategy.

## 8. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BSRCH-AC-001 | BSRCH-REQ-001 | Metadata shows `1.0.0 Approved`, owner `Backend`, `authoritative: false`, scope `BSRCH`, normativity only within BSRCH, and Search-and-Discovery-only bounded authority. |
| BSRCH-AC-002 | BSRCH-REQ-002 | Stored and returned state establishes only listed Search-owned operational outcomes. |
| BSRCH-AC-003 | BSRCH-REQ-003 | No Search operation can create or change any listed external truth. |
| BSRCH-AC-004 | BSRCH-REQ-004 | Actor, route, page, correlation, provider, and source identifiers cannot replace or merge request identity. |
| BSRCH-AC-005 | BSRCH-REQ-005 | Every page correlates to one result context and its accepted Projection evidence. |
| BSRCH-AC-006 | BSRCH-REQ-006 | Valid inputs are accepted and malformed, unsupported, unsafe, unbounded, or ambiguous inputs fail safely. |
| BSRCH-AC-007 | BSRCH-REQ-007 | Applied normalization is governed and explainable; absent policy is not guessed. |
| BSRCH-AC-008 | BSRCH-REQ-008 | Keyword discovery returns eligible matches or the correct explicit non-success/empty outcome. |
| BSRCH-AC-009 | BSRCH-REQ-009 | Ineligible Product evidence is excluded or explicitly stale and reconcilable without Search publication authority. |
| BSRCH-AC-010 | BSRCH-REQ-010 | Variant and Attribute criteria preserve BPRD meanings and create no Product state. |
| BSRCH-AC-011 | BSRCH-REQ-011 | Media references trace to governed evidence and create no media publication or provider authority. |
| BSRCH-AC-012 | BSRCH-REQ-012 | Category criteria preserve BCAT taxonomy, hierarchy, membership, navigation, ordering, lifecycle, and history. |
| BSRCH-AC-013 | BSRCH-REQ-013 | Filter behavior follows governed meaning and undefined combinations fail explicitly. |
| BSRCH-AC-014 | BSRCH-REQ-014 | Facet counts remain within the authorized result population and prove no source truth. |
| BSRCH-AC-015 | BSRCH-REQ-015 | Sorting is deterministic for equivalent evidence and selects no deferred policy. |
| BSRCH-AC-016 | BSRCH-REQ-016 | Ranking is bounded to Search and defines no algorithm, formula, weight, or source fact. |
| BSRCH-AC-017 | BSRCH-REQ-017 | Pages cannot silently mix query, authorization, sort, or Projection contexts and no numerical pagination rule is invented. |
| BSRCH-AC-018 | BSRCH-REQ-018 | Consumers can determine applied criteria, source, freshness, completeness, exclusions, and uncertainty. |
| BSRCH-AC-019 | BSRCH-REQ-019 | Commercial evidence is source-labelled, stale-capable, and revalidated by Pricing when current truth is required. |
| BSRCH-AC-020 | BSRCH-REQ-020 | Availability evidence is non-authoritative and cannot reserve Stock or authorize purchase. |
| BSRCH-AC-021 | BSRCH-REQ-021 | Downstream commitment requires applicable owning-Domain revalidation. |
| BSRCH-AC-022 | BSRCH-REQ-022 | CMS-derived evidence follows governed eligibility and BSRCH defines no CMS workflow, provider, or roadmap identity. |
| BSRCH-AC-023 | BSRCH-REQ-023 | Zero matches remain distinguishable from every listed failure or uncertainty class. |
| BSRCH-AC-024 | BSRCH-REQ-024 | Material failures remain truthful, distinguishable, concealed, and diagnosable. |
| BSRCH-AC-025 | BSRCH-REQ-025 | Fresh, stale, partial, delayed, excluded, and unknown coverage are distinguishable without a numerical freshness rule. |
| BSRCH-AC-026 | BSRCH-REQ-026 | Replay tests prevent duplicate meaning, obsolete restoration, authorization bypass, and repeated harmful effects. |
| BSRCH-AC-027 | BSRCH-REQ-027 | Older or conflicting evidence cannot silently replace newer accepted evidence. |
| BSRCH-AC-028 | BSRCH-REQ-028 | Concurrent activity preserves newer evidence or exposes conflict, staleness, partiality, or uncertainty. |
| BSRCH-AC-029 | BSRCH-REQ-029 | Indexing status truthfully represents each listed outcome and its source correlation. |
| BSRCH-AC-030 | BSRCH-REQ-030 | Rebuild/backfill stays authorized and bounded, preserves newer state, and excludes ineligible evidence. |
| BSRCH-AC-031 | BSRCH-REQ-031 | Recovery is authorized, evidence-based, duplicate-safe, source-bounded, and mechanism-neutral. |
| BSRCH-AC-032 | BSRCH-REQ-032 | Reconciliation preserves discrepancy and provenance and cannot rewrite source truth. |
| BSRCH-AC-033 | BSRCH-REQ-033 | Results, indexes, documents, caches, rankings, and Projections cannot bypass current owner validation. |
| BSRCH-AC-034 | BSRCH-REQ-034 | Actor ambiguity remains explicit and Search evidence establishes no identity or authority. |
| BSRCH-AC-035 | BSRCH-REQ-035 | Personalized or measured behavior requires governed purpose, Consent, and Preference evidence. |
| BSRCH-AC-036 | BSRCH-REQ-036 | Personalization, evidence, segmentation, ranking effect, and synchronization remain unresolved. |
| BSRCH-AC-037 | BSRCH-REQ-037 | Protected behavior enforces server-side contextual Authorization and default denial without making Search an authority. |
| BSRCH-AC-038 | BSRCH-REQ-038 | Security evidence covers isolation, minimization, masking, enumeration resistance, and Sensitive Data handling. |
| BSRCH-AC-039 | BSRCH-REQ-039 | Abuse cases fail safely under governed controls without an invented threshold or mechanism. |
| BSRCH-AC-040 | BSRCH-REQ-040 | Contract outcomes support applicable accessible experiences without frontend implementation claims. |
| BSRCH-AC-041 | BSRCH-REQ-041 | Material work is bounded and no numerical result, page, query, or batch limit is selected. |
| BSRCH-AC-042 | BSRCH-REQ-042 | Search degradation cannot compromise transactional correctness or source availability. |
| BSRCH-AC-043 | BSRCH-REQ-043 | Logs, metrics, traces, and health evidence are correlated and useful without Sensitive Data or invented targets. |
| BSRCH-AC-044 | BSRCH-REQ-044 | High-Risk actions create attributable Audit Records while routine public queries are not over-audited. |
| BSRCH-AC-045 | BSRCH-REQ-045 | Contracts are intentional, versionable, abstract, and free of invented routes, DTOs, schemas, engines, and payloads. |
| BSRCH-AC-046 | BSRCH-REQ-046 | Conditional events and inputs preserve source, compatibility, ordering uncertainty, and replay safety without a selected mechanism. |
| BSRCH-AC-047 | BSRCH-REQ-047 | Analytics observations cannot establish any listed Domain, Consent, Authorization, or conversion truth. |
| BSRCH-AC-048 | BSRCH-REQ-048 | Use Cases depend on project-owned Ports and can substitute implementations without semantic rewrite. |
| BSRCH-AC-049 | BSRCH-REQ-049 | Review finds no prohibited policy, mechanism, provider, number, lifecycle graph, or later roadmap position. |
| BSRCH-AC-050 | BSRCH-REQ-050 | Positive, negative, boundary, concurrency, failure, recovery, Contract, and architecture evidence covers every requirement. |
| BSRCH-AC-051 | BSRCH-REQ-051 | The BEB matrix accounts for all 56 BEB Requirements once, with specific rationale for every conditional or currently inapplicable obligation. |
| BSRCH-AC-052 | BSRCH-REQ-052 | Architecture tests and review preserve module ownership, inward dependencies, project-owned Ports, Search-only data ownership, and Adapter separation. |
| BSRCH-AC-053 | BSRCH-REQ-053 | Transaction and failure tests preserve local atomicity, external-call separation, durable uncertainty, idempotency, replay safety, and reconciliation where applicable. |
| BSRCH-AC-054 | BSRCH-REQ-054 | Configuration and feature-state tests preserve mandatory safeguards and select no mechanism. |
| BSRCH-AC-055 | BSRCH-REQ-055 | Compatibility and migration evidence covers supported prior, mixed-version, failure, rollback, and recovery behavior without physical design. |

## 9. Requirement Traceability

Each BSRCH Requirement maps to the same-numbered BSRCH Acceptance Criterion.

| BSRCH Requirement | Search Domain source | BEB inheritance | Additional authority |
| --- | --- | --- | --- |
| BSRCH-REQ-001 | REQ-SRCH-001 | BEB-REQ-001–004, 056 | ADR-0009; ARCHITECTURE.md §35 |
| BSRCH-REQ-002 | REQ-SRCH-002 | BEB-REQ-003–004, 021 | Search Domain Specification |
| BSRCH-REQ-003 | REQ-SRCH-003 | BEB-REQ-003–004, 021 | ADR-0009 Authority Boundaries |
| BSRCH-REQ-004 | REQ-SRCH-004 | BEB-REQ-015, 021, 023 | GLOSSARY.md |
| BSRCH-REQ-005 | REQ-SRCH-005 | BEB-REQ-016, 021, 024 | API.md |
| BSRCH-REQ-006 | REQ-SRCH-006 | BEB-REQ-013–018, 041 | API.md; SECURITY-STANDARDS.md |
| BSRCH-REQ-007 | REQ-SRCH-007 | BEB-REQ-003, 009–010, 056 | PRODUCT.md §24 |
| BSRCH-REQ-008 | REQ-SRCH-008 | BEB-REQ-009–010, 041 | Search Domain Specification |
| BSRCH-REQ-009 | REQ-SRCH-009 | BEB-REQ-021, 024, 031, 041–042 | BPRD |
| BSRCH-REQ-010 | REQ-SRCH-010 | BEB-REQ-003, 021, 024 | BPRD |
| BSRCH-REQ-011 | REQ-SRCH-011 | BEB-REQ-003, 021, 043 | BPRD; ARCHITECTURE.md §34 item 11 |
| BSRCH-REQ-012 | REQ-SRCH-012 | BEB-REQ-003, 021, 024 | BCAT |
| BSRCH-REQ-013 | REQ-SRCH-013 | BEB-REQ-009–010, 015–016 | BPRD; BCAT |
| BSRCH-REQ-014 | REQ-SRCH-014 | BEB-REQ-011, 016, 020–021 | BPRD; BCAT; BPRC; BINV |
| BSRCH-REQ-015 | REQ-SRCH-015 | BEB-REQ-010, 016, 056 | PRODUCT.md §24 |
| BSRCH-REQ-016 | REQ-SRCH-016 | BEB-REQ-003, 010, 021, 056 | PRODUCT.md §24; ADR-0009 |
| BSRCH-REQ-017 | REQ-SRCH-017 | BEB-REQ-016–018, 024, 050–051 | API.md |
| BSRCH-REQ-018 | REQ-SRCH-018 | BEB-REQ-008, 014, 016–018 | API.md |
| BSRCH-REQ-019 | REQ-SRCH-019 | BEB-REQ-003, 021, 024 | BPRC |
| BSRCH-REQ-020 | REQ-SRCH-020 | BEB-REQ-003, 021, 024 | BINV |
| BSRCH-REQ-021 | REQ-SRCH-021 | BEB-REQ-003, 008, 021 | BPRD; BPRC; BINV; BCART; Checkout Domain |
| BSRCH-REQ-022 | REQ-SRCH-022 | BEB-REQ-003, 021, 024, 031 | CMS Domain; PRODUCT.md §24 item 22 |
| BSRCH-REQ-023 | REQ-SRCH-023 | BEB-REQ-010, 017, 041 | API.md |
| BSRCH-REQ-024 | REQ-SRCH-024 | BEB-REQ-017, 028, 041–042 | API.md; DATABASE.md |
| BSRCH-REQ-025 | REQ-SRCH-025 | BEB-REQ-008, 016–018, 041 | BPRD; BCAT; BPRC; BINV |
| BSRCH-REQ-026 | REQ-SRCH-026 | BEB-REQ-029–031, 039 | EVENTS.md |
| BSRCH-REQ-027 | REQ-SRCH-027 | BEB-REQ-027–031, 041–042 | EVENTS.md; DATABASE.md |
| BSRCH-REQ-028 | REQ-SRCH-028 | BEB-REQ-025, 027–031, 041 | DATABASE.md |
| BSRCH-REQ-029 | REQ-SRCH-029 | BEB-REQ-028–031, 041, 046 | EVENTS.md |
| BSRCH-REQ-030 | REQ-SRCH-030 | BEB-REQ-009, 011, 025, 028–031, 051 | Administration Domain |
| BSRCH-REQ-031 | REQ-SRCH-031 | BEB-REQ-028–031, 041–042, 051 | DATABASE.md; TESTING-STANDARDS.md |
| BSRCH-REQ-032 | REQ-SRCH-032 | BEB-REQ-031, 041–042, 046 | BPRD; BCAT; BPRC; BINV; CMS Domain |
| BSRCH-REQ-033 | REQ-SRCH-033 | BEB-REQ-003, 008, 021 | BCART; Checkout Domain |
| BSRCH-REQ-034 | REQ-SRCH-034 | BEB-REQ-019–021, 043 | BIDN; BCUS |
| BSRCH-REQ-035 | REQ-SRCH-035 | BEB-REQ-011, 019–021, 043 | BCUS; PRODUCT.md §24 item 19 |
| BSRCH-REQ-036 | REQ-SRCH-036 | BEB-REQ-003, 021, 056 | PRODUCT.md §24 item 19 |
| BSRCH-REQ-037 | REQ-SRCH-037 | BEB-REQ-011, 019–020, 043 | BIDN; SECURITY-STANDARDS.md |
| BSRCH-REQ-038 | REQ-SRCH-038 | BEB-REQ-020–021, 043–046 | SECURITY-STANDARDS.md |
| BSRCH-REQ-039 | REQ-SRCH-039 | BEB-REQ-015, 020, 041, 043, 051 | SECURITY-STANDARDS.md |
| BSRCH-REQ-040 | REQ-SRCH-040 | BEB-REQ-008, 014, 017–018 | TESTING-STANDARDS.md §22 |
| BSRCH-REQ-041 | REQ-SRCH-041 | BEB-REQ-015–016, 046, 051 | API.md; TESTING-STANDARDS.md §23 |
| BSRCH-REQ-042 | REQ-SRCH-042 | BEB-REQ-026, 033–034, 041–042, 046, 051 | ARCHITECTURE.md §§27–28 |
| BSRCH-REQ-043 | REQ-SRCH-043 | BEB-REQ-045–046, 051 | SECURITY-STANDARDS.md |
| BSRCH-REQ-044 | REQ-SRCH-044 | BEB-REQ-045–046 | SECURITY-STANDARDS.md |
| BSRCH-REQ-045 | REQ-SRCH-045 | BEB-REQ-008, 013–018, 050 | API.md |
| BSRCH-REQ-046 | REQ-SRCH-046 | BEB-REQ-037–040, 050 | EVENTS.md |
| BSRCH-REQ-047 | REQ-SRCH-047 | BEB-REQ-003, 037–040, 043 | PRODUCT.md §24 item 20; EVENTS.md |
| BSRCH-REQ-048 | REQ-SRCH-048 | BEB-REQ-005–008, 022, 032, 056 | SPRING.md; ADR-0009 |
| BSRCH-REQ-049 | REQ-SRCH-049 | BEB-REQ-003–004, 040, 047–050, 056 | ADR-0009; PRODUCT.md §24; ARCHITECTURE.md §34 |
| BSRCH-REQ-050 | REQ-SRCH-050 | BEB-REQ-052–055 | TESTING-STANDARDS.md |
| BSRCH-REQ-051 | REQ-SRCH-001, 049–050 | BEB-REQ-001–056 | BEB; ADR-0001; ADR-0009 |
| BSRCH-REQ-052 | REQ-SRCH-002–003, 048–049 | BEB-REQ-005–008, 021–023 | SPRING.md; JAVA.md; DATABASE.md; POSTGRES.md |
| BSRCH-REQ-053 | REQ-SRCH-024, 026–032, 046 | BEB-REQ-012, 025–040, 042 | DATABASE.md; API.md; EVENTS.md |
| BSRCH-REQ-054 | REQ-SRCH-037–039, 042–043, 049 | BEB-REQ-043–048 | SECURITY-STANDARDS.md; ARCHITECTURE.md §34 item 14 |
| BSRCH-REQ-055 | REQ-SRCH-025–032, 045–046, 048–050 | BEB-REQ-018, 024, 042, 049–050 | API.md; DATABASE.md; TESTING-STANDARDS.md |

## 10. Search Domain Coverage Matrix

| Search Domain Requirement | BSRCH specialization |
| --- | --- |
| REQ-SRCH-001 | BSRCH-REQ-001 |
| REQ-SRCH-002 | BSRCH-REQ-002 |
| REQ-SRCH-003 | BSRCH-REQ-003 |
| REQ-SRCH-004 | BSRCH-REQ-004 |
| REQ-SRCH-005 | BSRCH-REQ-005 |
| REQ-SRCH-006 | BSRCH-REQ-006 |
| REQ-SRCH-007 | BSRCH-REQ-007 |
| REQ-SRCH-008 | BSRCH-REQ-008 |
| REQ-SRCH-009 | BSRCH-REQ-009 |
| REQ-SRCH-010 | BSRCH-REQ-010 |
| REQ-SRCH-011 | BSRCH-REQ-011 |
| REQ-SRCH-012 | BSRCH-REQ-012 |
| REQ-SRCH-013 | BSRCH-REQ-013 |
| REQ-SRCH-014 | BSRCH-REQ-014 |
| REQ-SRCH-015 | BSRCH-REQ-015 |
| REQ-SRCH-016 | BSRCH-REQ-016 |
| REQ-SRCH-017 | BSRCH-REQ-017 |
| REQ-SRCH-018 | BSRCH-REQ-018 |
| REQ-SRCH-019 | BSRCH-REQ-019 |
| REQ-SRCH-020 | BSRCH-REQ-020 |
| REQ-SRCH-021 | BSRCH-REQ-021 |
| REQ-SRCH-022 | BSRCH-REQ-022 |
| REQ-SRCH-023 | BSRCH-REQ-023 |
| REQ-SRCH-024 | BSRCH-REQ-024 |
| REQ-SRCH-025 | BSRCH-REQ-025 |
| REQ-SRCH-026 | BSRCH-REQ-026 |
| REQ-SRCH-027 | BSRCH-REQ-027 |
| REQ-SRCH-028 | BSRCH-REQ-028 |
| REQ-SRCH-029 | BSRCH-REQ-029 |
| REQ-SRCH-030 | BSRCH-REQ-030 |
| REQ-SRCH-031 | BSRCH-REQ-031 |
| REQ-SRCH-032 | BSRCH-REQ-032 |
| REQ-SRCH-033 | BSRCH-REQ-033 |
| REQ-SRCH-034 | BSRCH-REQ-034 |
| REQ-SRCH-035 | BSRCH-REQ-035 |
| REQ-SRCH-036 | BSRCH-REQ-036 |
| REQ-SRCH-037 | BSRCH-REQ-037 |
| REQ-SRCH-038 | BSRCH-REQ-038 |
| REQ-SRCH-039 | BSRCH-REQ-039 |
| REQ-SRCH-040 | BSRCH-REQ-040 |
| REQ-SRCH-041 | BSRCH-REQ-041 |
| REQ-SRCH-042 | BSRCH-REQ-042 |
| REQ-SRCH-043 | BSRCH-REQ-043 |
| REQ-SRCH-044 | BSRCH-REQ-044 |
| REQ-SRCH-045 | BSRCH-REQ-045 |
| REQ-SRCH-046 | BSRCH-REQ-046 |
| REQ-SRCH-047 | BSRCH-REQ-047 |
| REQ-SRCH-048 | BSRCH-REQ-048 |
| REQ-SRCH-049 | BSRCH-REQ-049 |
| REQ-SRCH-050 | BSRCH-REQ-050 |

## 11. BEB Applicability and Accounting Matrix

| BEB Requirement(s) | Classification | BSRCH rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, non-authority, and Search specialization. BSRCH-REQ-001–003, 051. |
| BEB-REQ-005–007 | Applicable | Modular, hexagonal, layered Search boundary through project-owned Ports. BSRCH-REQ-048, 052. |
| BEB-REQ-008 | Applicable | Intentional Search Module Contracts. BSRCH-REQ-018, 021, 025, 033, 040, 045. |
| BEB-REQ-009–010 | Applicable | Explicit Search Use Cases and truthful query/command outcomes. BSRCH-REQ-006–018, 023–033. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and owned transaction boundaries. BSRCH-REQ-030, 035, 037, 053. |
| BEB-REQ-013–018 | Applicable | Versioning, DTO separation, validation, bounds, errors, description, and evolution. BSRCH-REQ-006, 017–018, 040–041, 045. |
| BEB-REQ-019–020 | Applicable | Trusted Authentication evidence, contextual Authorization, and concealment. BSRCH-REQ-034–039. |
| BEB-REQ-021–024 | Applicable | Search-derived data ownership, Ports, integrity, and historical/source truth. BSRCH-REQ-002–005, 009–022, 025–033, 052, 055. |
| BEB-REQ-025 | Applicable | Search state changes require explicit consistency and atomicity boundaries. BSRCH-REQ-028, 030, 053. |
| BEB-REQ-026 | Applicable | Source/provider calls cannot be held inside unsafe Search-owned transactions. BSRCH-REQ-042, 053. |
| BEB-REQ-027–031 | Applicable | Concurrency, durable uncertainty, idempotency, replay, and duplicate safety. BSRCH-REQ-026–032, 053. |
| BEB-REQ-032–034 | Conditionally applicable | Any future provider remains behind a Port with governed resilience and reconciliation; no provider is selected. BSRCH-REQ-024, 031–032, 042, 048–049. |
| BEB-REQ-035–036 | Not currently applicable | No Webhook Contract is authorized; authenticity, replay, and recovery controls remain inherited if one is later governed. |
| BEB-REQ-037–039 | Conditionally applicable | Events and asynchronous Projection inputs are permitted only when separately governed. BSRCH-REQ-026–032, 046–047. |
| BEB-REQ-040 | Applicable | BSRCH cannot adopt external messaging independently. BSRCH-REQ-046, 049. |
| BEB-REQ-041–042 | Applicable | Safe errors, recovery, and reconciliation. BSRCH-REQ-023–033, 042. |
| BEB-REQ-043–044 | Applicable | Search data, Contracts, evidence, configuration, and Secrets require protection; Payment data is not Search-owned. BSRCH-REQ-034–039, 043. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records and safe observability. BSRCH-REQ-043–044. |
| BEB-REQ-047–048 | Applicable | Configuration and reachable feature states preserve safeguards without selecting configuration or flag mechanisms. BSRCH-REQ-049–050, 054. |
| BEB-REQ-049–051 | Applicable | Migration, compatibility, bounded work, and failure containment. BSRCH-REQ-017, 030–032, 041–046, 049–050, 055. |
| BEB-REQ-052–055 | Applicable | Unit, application, Adapter, integration, Contract, architecture, operational, and traceable verification. BSRCH-REQ-050. |
| BEB-REQ-056 | Applicable | Product-policy, implementation, provider, and post-BSRCH roadmap neutrality. BSRCH-REQ-001, 007, 015–017, 025, 031, 036, 041–042, 045–050. |

## 12. Dependency and Authority Matrix

| Capability | Relationship | BSRCH use | Authority retained outside BSRCH |
| --- | --- | --- | --- |
| BEB | Inherited requirement | Shared backend obligations | Shared backend governance |
| BIDN | Contextual dependency | Trusted Principal and Authentication evidence for protected behavior | Identity, Authentication, Sessions, tokens, access evidence |
| BCUS | Contextual dependency | Governed Customer, Account, Consent, and Preference evidence where policy permits | Customer, Account, Consent, Preference, segmentation |
| BPRD | Authoritative source consumed | Eligible Product, Product Variant, Attribute, content, and Product Media evidence | Product identity, lifecycle, publication, Attributes, content, media |
| BINV | Projection/read-model dependency | Stale-capable availability representation where governed | Stock, Available-to-Sell, reservations, commitment, availability truth |
| BPRC | Projection/read-model dependency | Stale-capable commercial representation where governed | Price, Discount, Promotion, Voucher, tax, commercial truth |
| BCART | Boundary/context only | Search results may support discovery before Cart intent | Cart identity, contents, lifecycle, totals, intent |
| BCAT | Authoritative source consumed | Taxonomy, hierarchy, classification, membership, navigation, ordering evidence | All Category-owned truth |
| CMS | Separate unresolved capability; conditional source | Eligible published content evidence where governed | CMS workflow, approval, scheduling, provider, truth, backend identity and order |
| Administration | Consumer/contextual dependency | Protected Search operations invoked through BSRCH Use Cases | Administrative coordination and repository-wide access policy; no Role matrix |
| Checkout | Separate unresolved capability | Current truth may be revalidated after discovery | Checkout orchestration, commitment, purchasability, backend identity and order |

## 13. API, Data, Projection, and Event Boundaries

BSRCH Contracts describe behavior and outcomes, not routes, methods, status codes, DTO shapes, provider payloads, or physical schemas. Search indexes, documents, caches, denormalized models, ranking state, and Projections are derived, non-authoritative, stale-capable, rebuildable representations. Conditional events and source inputs remain governed by source ownership, compatibility, replay, ordering uncertainty, privacy, and reconciliation requirements; no event name, schema, topic, broker, transport, index topology, persistence design, or synchronization mechanism is selected.

## 14. Open Product Decisions

The following **10 materially applicable Open Product Decisions** from `PRODUCT.md` §24 remain unresolved:

| Item | Open Product Decision | Preserved BSRCH boundary |
| ---: | --- | --- |
| 2 | Initial product categories and catalogue taxonomy. | BSRCH consumes governed BCAT evidence and defines no taxonomy. |
| 3 | Size, fit, colour, material, and other Product Variant or Attribute standards. | No filterable Attribute or Variant standard is selected. |
| 13 | Back-order and pre-order support. | No unavailable-item discovery or availability policy is inferred. |
| 15 | Product-review support. | No review capability, moderation, evidence input, or ranking effect is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No message, threshold, display, or eligibility rule is selected. |
| 19 | Marketing-consent and communication-preference model. | Governed evidence is required where applicable; no model is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, taxonomy, or measurement mechanism is selected. |
| 22 | Content approval and scheduled-publication workflow. | No CMS workflow, schedule, state, or provider is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 30 | Product launch date, release scope, and post-launch support window. | No release date, capability scope, support window, rollout, or target is selected. |

Ranking weights and formulas, query normalization, synonym, spelling, Facet, personalization, freshness, indexing, and rebuild policy remain unresolved scope boundaries rather than additional numbered Product Decisions.

## 15. Open Architecture Decisions

The following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34 remain unresolved:

| Item | Open Architecture Decision | Preserved BSRCH boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No cache or Redis use is selected. |
| 9 | External messaging introduction and service selection. | No messaging service, broker, transport, or adoption is selected. |
| 10 | Initial search implementation details and extraction thresholds. | No engine, provider, index design, extraction threshold, ranking, or synchronization mechanism is selected. |
| 11 | Product-media upload and transformation strategy. | No upload, transformation, storage, or delivery mechanism is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Search persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, rollout system, or lifecycle is selected. |

## 16. Explicit Non-Decisions and Roadmap Boundary

BSRCH selects no Search engine or provider; Redis or cache mechanism; broker or messaging provider; indexing topology; Search document or database schema; ranking algorithm, scoring formula, or weight; Facet implementation; synchronization mechanism; extraction threshold; retry count; timeout; freshness duration; cache lifetime; page/result limit; API route; DTO; Integration Event schema; physical persistence strategy; Role/Permission matrix; unresolved Product policy; or numerical operational target.

CMS and Checkout remain independently eligible, separate, and unresolved, with no ordering between them. BSRCH establishes no CMS, Administration, Checkout, Order, Payment, Shipping and Fulfilment, or other backend identity, title, path, scope, decomposition, or position after BSRCH.

## 17. Risks and Controls

| Risk | Control direction |
| --- | --- |
| Derived Search state becomes source truth | Require owning-Domain revalidation and preserve explicit non-authority. |
| Withdrawn or ineligible evidence remains discoverable | Track source eligibility, freshness, discrepancy, and reconciliation. |
| Stale Price or Inventory implies purchasability | Label staleness and require BPRC/BINV and downstream revalidation. |
| Replay or reordering restores obsolete evidence | Preserve provenance, ordering uncertainty, supersession, and duplicate safety. |
| Partial rebuild appears complete | Expose scope, progress, completeness, exclusions, and uncertainty. |
| Protected data leaks through results or Facets | Enforce contextual Authorization, population isolation, minimization, and concealment. |
| Search workload harms transactional behavior | Bound work and isolate degradation from source correctness and availability. |
| Unapproved policy becomes ranking behavior | Require governed policy and prohibit local formulas, weights, and personalization rules. |
| Provider details leak into Domain behavior | Require project-owned Ports and mechanism-neutral Contracts. |
| Routine queries create excessive audit data | Apply proportional Audit Records only to material or high-Risk behavior. |

## 18. Required Governance Reviews

The completed approval review confirmed complete Search Domain and BEB coverage; one-to-one Requirements, Acceptance Criteria, and traceability; Search-and-Discovery-only authority; bounded source consumption; preservation of all Open Decisions; mechanism neutrality; security, privacy, recovery, observability, compatibility, and testing completeness; and absence of any post-BSRCH roadmap selection. Required ownership reviews were completed for Architecture; Search and Discovery; Product and Product Catalogue; Category; Pricing; Inventory; CMS; Administration; Checkout; Identity; and Customer. No reviewer names, signatures, tickets, dates beyond the governed document date, or external evidence are asserted.

## 19. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0009-post-category-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/checkout/checkout-domain.md`

## 20. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-19 | Approved | Approved BSRCH after review confirmed complete Search Domain coverage, complete BEB accounting, Search-and-Discovery-only authority, bounded source consumption, preserved Open Product and Architecture Decisions, mechanism neutrality, and no post-BSRCH roadmap selection. |
| 0.1.0 | 2026-09-19 | Draft | Established the initial Search-and-Discovery-only Backend Specification from Approved Search Domain authority, BEB inheritance, governed source evidence, and Accepted ADR-0009. |

## 21. Final Validation

Before material revision, re-approval, or implementation reliance, reviewers MUST validate:

1. metadata is `1.0.0 Approved`, owner `Backend`, `authoritative: false`, scope `BSRCH`, and Requirements are normative only within BSRCH;
2. all 55 BSRCH Requirements are unique and contiguous;
3. every BSRCH Requirement has exactly one corresponding Acceptance Criterion and traceability row;
4. all 50 Search and Discovery Domain Requirements are accounted for;
5. all 56 BEB Requirements are accounted for exactly once by the applicability matrix ranges;
6. BSRCH remains Search-and-Discovery-only and all derived state remains non-authoritative;
7. bounded dependencies transfer no Product, Category, Pricing, Inventory, Identity, Customer, Cart, CMS, Administration, Checkout, or other authority;
8. all 10 Product and 10 Architecture decisions remain exact and unresolved;
9. no provider, engine, policy, algorithm, schema, route, DTO, event, cache, infrastructure, number, or physical mechanism is selected;
10. CMS and Checkout remain independently eligible and unresolved, with no order between them and no post-BSRCH roadmap position;
11. all required governance reviews are identified and recorded as completed without unsupported external evidence;
12. the BSRCH lifecycle change affects only `specifications/backend/search/search-backend.md`, passes whitespace validation, and introduces no unrelated repository changes.
