---
title: Category Backend Specification
version: 1.0.0
status: Approved
owner: Category
last_updated: 2026-09-19
authoritative: false
scope: BCAT
---

# Category Backend Specification

## 1. Purpose

This Approved Specification defines the implementation-facing Category-only backend obligations authorized by Accepted ADR-0008. Its Requirements are normative only within BCAT. It remains `authoritative: false`, subordinate to higher governing sources, and resolves no Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BCAT-REQ-001 — Lifecycle and Authority
BCAT MUST retain scope `BCAT`, `authoritative: false`, `1.0.0` Approved lifecycle metadata, and authority bounded to the Approved Category Domain.

### BCAT-REQ-002 — Complete Category Specialization
BCAT MUST specialize every `REQ-CAT-001` through `REQ-CAT-043` obligation without weakening, extending, or transferring Category authority.

### BCAT-REQ-003 — Category-Only Roadmap Boundary
BCAT MUST remain Category-only in `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT` and establish no later backend identity or order.

### BCAT-REQ-004 — BEB Inheritance
BCAT MUST inherit every materially applicable BEB Requirement and preserve conditional obligations when later governed Category capabilities make them applicable.

### BCAT-REQ-005 — Bounded External Evidence
BCAT MAY consume governed BIDN, BCUS, BPRD, BINV, BPRC, and BCART evidence only where Category authority requires it, without transferring Identity, Customer, Product, Inventory, Pricing, or Cart authority.

### BCAT-REQ-006 — Modular Architecture and Persistence
BCAT MUST keep Category rules within its modular, hexagonal Domain/Application boundary, own only Category data behind project-owned Ports, and select no physical schema, hierarchy representation, provider, or infrastructure mechanism.

## 3. Category Identity, Hierarchy, and Taxonomy

### BCAT-REQ-007 — Downstream Authority Separation
BCAT MUST NOT establish another Domain's truth, policy, workflow, or backend roadmap position.

### BCAT-REQ-008 — Stable Category Identity
BCAT MUST preserve stable Category identity across Category lifecycle, relationships, history, Contracts, and permitted representations without selecting an identifier mechanism.

### BCAT-REQ-009 — Semantic Category Uniqueness
BCAT MUST reject governed Category meanings that conflict or duplicate within the applicable semantic context without inventing naming or taxonomy policy.

### BCAT-REQ-010 — Valid Parent-Child Relationship
BCAT MUST validate every Category parent-child relationship against current Category authority and reject invalid, self-referential, or unsupported relationships.

### BCAT-REQ-011 — Acyclic Hierarchy
BCAT MUST prevent direct and indirect hierarchy cycles before accepting a Category relationship.

### BCAT-REQ-012 — Orphan Handling
BCAT MUST detect and safely reject or reconcile Category changes that would create an invalid orphan or unreachable Category under governed policy.

### BCAT-REQ-013 — Hierarchy Consistency
BCAT MUST preserve consistent parent, child, ancestry, descendant, and navigation evidence across accepted Category mutations without selecting storage representation.

### BCAT-REQ-014 — Hierarchy Projection Reconciliation
BCAT MUST keep non-authoritative hierarchy Projections traceable and reconcilable to current Category truth, including delayed, duplicate, reordered, stale, or partial evidence.

### BCAT-REQ-015 — Taxonomy Authority
BCAT MUST own only Approved Category taxonomy meaning and invariants while leaving initial taxonomy structure, roots, depth, naming, and classification policy unresolved.

### BCAT-REQ-016 — Coherent Classification Meaning
BCAT MUST preserve coherent Category classification meaning and reject ambiguous or conflicting classification outcomes without inventing Product Attribute standards or automatic classification.

## 4. Membership, Lifecycle, Navigation, and Content

### BCAT-REQ-017 — Valid Product Association
BCAT MUST associate only governed Product identity evidence supplied by BPRD and MUST reject unknown, invalid, or mismatched Product references safely.

### BCAT-REQ-018 — Category-Side Membership Policy
BCAT MUST enforce only Approved Category-side membership policy and leave cardinality, primary Category, automation, and classification details unresolved.

### BCAT-REQ-019 — Membership Non-Authority
Category membership MUST NOT redefine Product identity, Product lifecycle, Product Variant identity, Product Attributes, Product publication, Pricing, Inventory, or sellability.

### BCAT-REQ-020 — Governed Category Lifecycle
BCAT MUST preserve only governed Category lifecycle outcomes and their history without inventing lifecycle states, transitions, timing, or publication workflow.

### BCAT-REQ-021 — Unresolved Category States
BCAT MUST keep concrete Category state names, state machine, archival/withdrawal semantics, and approval/publication relationships unresolved until Approved policy governs them.

### BCAT-REQ-022 — Category Navigation Eligibility
BCAT MUST own governed Category-side navigation eligibility evidence while keeping storefront placement, presentation, Search ranking, and customer-specific navigation outside Category authority.

### BCAT-REQ-023 — Navigation Non-Authority
BCAT MUST NOT make navigation, Search, cache, CMS, frontend, or consumer Projections authoritative Category truth.

### BCAT-REQ-024 — Governed Category Ordering
BCAT MUST preserve only Approved Category ordering meaning and consistent outcomes without inventing ordering algorithms, weights, positions, scopes, or numerical limits.

### BCAT-REQ-025 — Category-Owned Content
BCAT MUST own only Category-authorized labels, descriptions, and metadata while preserving CMS publication/workflow authority and Product Media authority.

### BCAT-REQ-026 — Historical Category Meaning
BCAT MUST preserve interpretable historical Category identity, hierarchy, classification, membership, and meaning required by governed consumers without rewriting historical external truth.

### BCAT-REQ-027 — Product Association Change Safety
BCAT MUST make Product association changes explicit, consistent, traceable, and safe for current and historical consumers without mutating Product truth.

## 5. Category Operations and Integrity

### BCAT-REQ-028 — Governed Category Creation
BCAT MUST validate identity, semantic uniqueness, hierarchy, taxonomy, content, Authorization, and applicable association invariants before accepting Category creation.

### BCAT-REQ-029 — Governed Category Update
BCAT MUST apply authorized Category updates atomically where required and reject stale, invalid, conflicting, or ambiguous changes with explicit outcomes.

### BCAT-REQ-030 — Governed Reparenting
BCAT MUST validate reparenting against cycle, orphan, hierarchy, history, concurrency, and downstream evidence integrity without inventing reparenting policy.

### BCAT-REQ-031 — Governed Reclassification
BCAT MUST validate reclassification against current taxonomy and association meaning while preserving history and leaving classification policy unresolved.

### BCAT-REQ-032 — Governed Withdrawal
BCAT MUST represent withdrawal only where Approved policy permits and preserve history, associations, and explainable downstream correction without inventing lifecycle behavior.

### BCAT-REQ-033 — Governed Deletion
BCAT MUST prevent destructive Category deletion from silently erasing required history or breaking governed associations and MUST preserve deletion/retention policy as unresolved.

### BCAT-REQ-034 — Search Projection Integrity
BCAT MAY expose Category-owned evidence for future Search consumption through abstract governed Contracts, but MUST NOT define Search ranking, queries, filters, facets, indexing, provider, or synchronization mechanisms.

### BCAT-REQ-035 — Search Result Non-Authority
Search results and indexes MUST remain non-authoritative, stale-capable Projections that cannot create or mutate Category truth.

## 6. Cross-Domain Boundaries

### BCAT-REQ-036 — Product Authority Boundary
BCAT MUST consume BPRD evidence without redefining Product/Product Variant identity, lifecycle, publication, attributes, content, media, or catalogue authority.

### BCAT-REQ-037 — Pricing Authority Boundary
BCAT MUST NOT calculate, own, or infer Price, Discount, Promotion, Voucher, tax, or commercial truth from Category state or navigation.

### BCAT-REQ-038 — Inventory Authority Boundary
BCAT MUST NOT calculate, own, or infer Stock, Available-to-Sell, reservation, overselling, or purchase eligibility from Category state or navigation.

### BCAT-REQ-039 — Customer and Market Boundary
BCAT MAY consume governed Customer or market context only where Approved Category policy requires it and MUST NOT define Customer, Account, segmentation, personalization, Consent, or Preference policy.

### BCAT-REQ-040 — Administration Boundary
Administrative Category operations MUST invoke BCAT-owned Use Cases through contextual Authorization and MUST NOT transfer Category authority or define Roles, Permissions, mappings, or an access matrix.

### BCAT-REQ-041 — Category Authorization
Protected Category behavior MUST enforce trusted server-side contextual Authorization, least privilege, default denial, ownership/action checks, and protected-resource concealment; Identity evidence alone MUST NOT grant Category authority.

## 7. Security, Contracts, Operations, and Verification

### BCAT-REQ-042 — Category Data Safety
BCAT MUST validate untrusted input and protect Sensitive Data, Secrets, internal detail, and Category Resource isolation across persistence, Contracts, logs, errors, events, and Projections.

### BCAT-REQ-043 — Accessible Category Meaning
BCAT Contracts and outcomes MUST provide stable, distinguishable Category meaning sufficient for applicable WCAG 2.2 AA experiences without selecting frontend implementation.

### BCAT-REQ-044 — Bounded Category Delivery
Category operations MUST be bounded and observable; degradation MUST NOT promote stale or uncertain Category or external evidence to authoritative truth, and no numerical target is selected.

### BCAT-REQ-045 — Material Category Audit Evidence
Material privileged, administrative, destructive, reconciliation, and high-Risk Category actions MUST produce proportional, attributable Audit Records while routine reads are not automatically high-Risk.

### BCAT-REQ-046 — Category Contract and Conditional Event Boundary
BCAT MUST expose intentional, compatible, versionable behavioral Contracts and create events only when separately governed, selecting no route, DTO, schema, broker, topic, queue, provider, or choreography.

### BCAT-REQ-047 — Category Failure, Recovery, and Reconciliation
Unknown, invalid, unauthorized, stale, duplicate, concurrent, conflicting, partial, and uncertain Category outcomes MUST be safe, distinguishable, explainable, and recoverable where governed, without leaking protected details or fabricating external truth.

### BCAT-REQ-048 — Complete Category Verification
Verification MUST cover every applicable Category requirement, invariant, boundary, Contract, security, concurrency, failure, recovery, accessibility, compatibility, persistence, operation, and conditional event behavior without selecting tooling or numerical coverage targets.

## 8. Acceptance Criteria

| Acceptance Criterion | Requirement | Observable evidence |
| --- | --- | --- |
| BCAT-AC-001 | BCAT-REQ-001 | Metadata shows `1.0.0 Approved`, owner `Category`, `authoritative: false`, scope `BCAT`, and normative authority only within BCAT. |
| BCAT-AC-002 | BCAT-REQ-002 | The coverage matrix accounts for all 43 Category Requirements without weakened or transferred authority. |
| BCAT-AC-003 | BCAT-REQ-003 | The sequence ends at BCAT and every later identity/order remains unresolved. |
| BCAT-AC-004 | BCAT-REQ-004 | The BEB matrix accounts for all 56 Requirements exactly once. |
| BCAT-AC-005 | BCAT-REQ-005 | Each external dependency is bounded and transfers no authority. |
| BCAT-AC-006 | BCAT-REQ-006 | Architecture/persistence review preserves module ownership, inward dependencies, Ports, and mechanism neutrality. |
| BCAT-AC-007 | BCAT-REQ-007 | No BCAT behavior establishes another Domain's truth, policy, workflow, or roadmap position. |
| BCAT-AC-008 | BCAT-REQ-008 | Identity remains stable across lifecycle, relationships, history, Contracts, and representations. |
| BCAT-AC-009 | BCAT-REQ-009 | Conflicting or duplicate semantic meanings are rejected in their governed context without invented taxonomy. |
| BCAT-AC-010 | BCAT-REQ-010 | Invalid, self-referential, and unsupported parent-child relationships are rejected. |
| BCAT-AC-011 | BCAT-REQ-011 | Direct and indirect cycles are detected before acceptance. |
| BCAT-AC-012 | BCAT-REQ-012 | Orphaning/unreachability produces a safe rejected or reconcilable outcome under governed policy. |
| BCAT-AC-013 | BCAT-REQ-013 | Accepted mutations preserve consistent hierarchy evidence without selecting storage design. |
| BCAT-AC-014 | BCAT-REQ-014 | Projections handle delayed, duplicate, reordered, stale, and partial evidence and reconcile to Category truth. |
| BCAT-AC-015 | BCAT-REQ-015 | BCAT owns bounded taxonomy meaning while structure, roots, depth, naming, and classification policy remain unresolved. |
| BCAT-AC-016 | BCAT-REQ-016 | Classification remains coherent and introduces no Product Attribute or automation policy. |
| BCAT-AC-017 | BCAT-REQ-017 | Unknown, invalid, and mismatched Product references are rejected using governed BPRD evidence. |
| BCAT-AC-018 | BCAT-REQ-018 | Only Approved Category membership policy is enforced; cardinality/primary/automation remain unresolved. |
| BCAT-AC-019 | BCAT-REQ-019 | Membership cannot redefine any listed Product, Pricing, Inventory, or sellability truth. |
| BCAT-AC-020 | BCAT-REQ-020 | Lifecycle outcomes/history occur only where governed and no state machine or timing is invented. |
| BCAT-AC-021 | BCAT-REQ-021 | States, transitions, archival, withdrawal, approval, and publication details remain unresolved. |
| BCAT-AC-022 | BCAT-REQ-022 | Category navigation eligibility remains distinct from placement, presentation, Search ranking, and personalization. |
| BCAT-AC-023 | BCAT-REQ-023 | Consumer Projections cannot create or mutate Category truth. |
| BCAT-AC-024 | BCAT-REQ-024 | Ordering is consistent where governed and defines no algorithm, weight, scope, position, or limit. |
| BCAT-AC-025 | BCAT-REQ-025 | Category content is bounded from CMS workflow/publication and Product Media authority. |
| BCAT-AC-026 | BCAT-REQ-026 | Historical meaning remains interpretable without rewriting external history. |
| BCAT-AC-027 | BCAT-REQ-027 | Association changes are explicit, consistent, traceable, history-safe, and cannot mutate Product. |
| BCAT-AC-028 | BCAT-REQ-028 | Creation validates every listed invariant and Authorization before acceptance. |
| BCAT-AC-029 | BCAT-REQ-029 | Authorized updates handle atomicity, staleness, invalidity, conflict, and ambiguity explicitly. |
| BCAT-AC-030 | BCAT-REQ-030 | Reparenting preserves cycle, orphan, hierarchy, history, concurrency, and consumer integrity. |
| BCAT-AC-031 | BCAT-REQ-031 | Reclassification preserves current taxonomy, association meaning, and history without policy invention. |
| BCAT-AC-032 | BCAT-REQ-032 | Withdrawal occurs only where governed and preserves history and downstream correction. |
| BCAT-AC-033 | BCAT-REQ-033 | Deletion cannot erase required history or break associations; retention policy remains unresolved. |
| BCAT-AC-034 | BCAT-REQ-034 | Abstract Category evidence can support Search without defining any Search behavior or mechanism. |
| BCAT-AC-035 | BCAT-REQ-035 | Search indexes/results remain non-authoritative and cannot mutate Category. |
| BCAT-AC-036 | BCAT-REQ-036 | Product evidence is consumed without transferring any listed Product authority. |
| BCAT-AC-037 | BCAT-REQ-037 | Category state establishes no Pricing truth. |
| BCAT-AC-038 | BCAT-REQ-038 | Category state establishes no Inventory or purchase-eligibility truth. |
| BCAT-AC-039 | BCAT-REQ-039 | Customer/market evidence is bounded and creates no Customer, Account, segmentation, Consent, or Preference policy. |
| BCAT-AC-040 | BCAT-REQ-040 | Administrative operations invoke BCAT with contextual Authorization and define no Role/Permission matrix. |
| BCAT-AC-041 | BCAT-REQ-041 | Server-side tests prove least privilege/default denial and reject Identity/UI/Claims/identifiers as sole authority. |
| BCAT-AC-042 | BCAT-REQ-042 | Security evidence covers validation, isolation, minimization, concealment, Secrets, logs, errors, Contracts, and events. |
| BCAT-AC-043 | BCAT-REQ-043 | Contract outcomes support accessible Category experiences without frontend implementation or certification claims. |
| BCAT-AC-044 | BCAT-REQ-044 | Bounded/degradation tests prevent stale or uncertain evidence becoming authoritative and assert no number. |
| BCAT-AC-045 | BCAT-REQ-045 | High-Risk actions produce separate attributable audit evidence; routine reads are not over-audited. |
| BCAT-AC-046 | BCAT-REQ-046 | Contracts are abstract/versionable and events conditional, with no concrete surface or mechanism selected. |
| BCAT-AC-047 | BCAT-REQ-047 | All listed failures are safe, distinguishable, concealed, recoverable where permitted, and externally non-authoritative. |
| BCAT-AC-048 | BCAT-REQ-048 | Verification covers all Category/BCAT obligations with traceability and no invented tool or target. |

## 9. Requirement Traceability

Each BCAT Requirement maps to the same-numbered BCAT Acceptance Criterion.

| BCAT Requirement | Category source | BEB inheritance | Additional authority |
| --- | --- | --- | --- |
| BCAT-REQ-001 | REQ-CAT-001 | BEB-REQ-001–004, 056 | ADR-0008; ARCHITECTURE.md §35 |
| BCAT-REQ-002 | REQ-CAT-001–043 | BEB-REQ-003–004, 055 | Category Domain Specification |
| BCAT-REQ-003 | REQ-CAT-002 | BEB-REQ-003–004, 056 | ADR-0008 |
| BCAT-REQ-004 | REQ-CAT-043 | BEB-REQ-001–056 | BEB; ADR-0001 |
| BCAT-REQ-005 | REQ-CAT-012, 031–034, 036 | BEB-REQ-003, 011, 019–021 | BIDN; BCUS; BPRD; BINV; BPRC; BCART |
| BCAT-REQ-006 | REQ-CAT-001, 041 | BEB-REQ-005–010, 021–025 | SPRING.md; DATABASE.md; POSTGRES.md |
| BCAT-REQ-007 | REQ-CAT-002 | BEB-REQ-003, 021 | ADR-0008 Authority Boundary |
| BCAT-REQ-008 | REQ-CAT-003 | BEB-REQ-021, 023–024 | GLOSSARY.md |
| BCAT-REQ-009 | REQ-CAT-004 | BEB-REQ-015, 021, 023, 041 | Category Domain §6 |
| BCAT-REQ-010 | REQ-CAT-005 | BEB-REQ-015, 023, 025 | DATABASE.md |
| BCAT-REQ-011 | REQ-CAT-006 | BEB-REQ-023, 025, 027 | DATABASE.md |
| BCAT-REQ-012 | REQ-CAT-007 | BEB-REQ-023, 041–042 | Category Domain §7 |
| BCAT-REQ-013 | REQ-CAT-008 | BEB-REQ-023, 025, 027 | DATABASE.md |
| BCAT-REQ-014 | REQ-CAT-009 | BEB-REQ-028–031, 037–042 | EVENTS.md |
| BCAT-REQ-015 | REQ-CAT-010 | BEB-REQ-003, 021, 056 | PRODUCT.md §24 item 2 |
| BCAT-REQ-016 | REQ-CAT-011 | BEB-REQ-003, 021, 041 | BPRD; PRODUCT.md §24 |
| BCAT-REQ-017 | REQ-CAT-012 | BEB-REQ-015, 021, 041 | BPRD |
| BCAT-REQ-018 | REQ-CAT-013 | BEB-REQ-003, 021, 056 | PRODUCT.md §24 item 2 |
| BCAT-REQ-019 | REQ-CAT-014 | BEB-REQ-003, 021, 024 | BPRD; BINV; BPRC |
| BCAT-REQ-020 | REQ-CAT-015 | BEB-REQ-021, 024–025 | PRODUCT.md §24 item 22 |
| BCAT-REQ-021 | REQ-CAT-016 | BEB-REQ-003, 056 | PRODUCT.md §24 items 2, 22 |
| BCAT-REQ-022 | REQ-CAT-017 | BEB-REQ-003, 021 | Search Domain; CMS Domain |
| BCAT-REQ-023 | REQ-CAT-018 | BEB-REQ-003, 021 | ADR-0008 Search Boundary |
| BCAT-REQ-024 | REQ-CAT-019 | BEB-REQ-015–016, 021, 051 | PRODUCT.md §24 item 2 |
| BCAT-REQ-025 | REQ-CAT-020 | BEB-REQ-003, 021, 043 | CMS Domain; PRODUCT.md §24 item 22 |
| BCAT-REQ-026 | REQ-CAT-021 | BEB-REQ-021, 024 | DATABASE.md |
| BCAT-REQ-027 | REQ-CAT-022 | BEB-REQ-025, 041–042 | BPRD |
| BCAT-REQ-028 | REQ-CAT-023 | BEB-REQ-009–012, 015, 025 | SECURITY-STANDARDS.md |
| BCAT-REQ-029 | REQ-CAT-024 | BEB-REQ-009–012, 025, 027 | DATABASE.md |
| BCAT-REQ-030 | REQ-CAT-025 | BEB-REQ-025, 027, 029–031 | DATABASE.md |
| BCAT-REQ-031 | REQ-CAT-026 | BEB-REQ-025, 027, 041 | BPRD |
| BCAT-REQ-032 | REQ-CAT-027 | BEB-REQ-021, 024–025, 041–042 | Search Domain |
| BCAT-REQ-033 | REQ-CAT-028 | BEB-REQ-021, 023–025, 043 | PRODUCT.md §24 item 27 |
| BCAT-REQ-034 | REQ-CAT-029 | BEB-REQ-008, 037–040, 056 | Search Domain; ADR-0008 |
| BCAT-REQ-035 | REQ-CAT-030 | BEB-REQ-003, 021, 041 | Search Domain |
| BCAT-REQ-036 | REQ-CAT-031 | BEB-REQ-003, 021, 024 | BPRD |
| BCAT-REQ-037 | REQ-CAT-032 | BEB-REQ-003, 021, 024 | BPRC |
| BCAT-REQ-038 | REQ-CAT-033 | BEB-REQ-003, 021, 024 | BINV |
| BCAT-REQ-039 | REQ-CAT-034 | BEB-REQ-003, 011, 019–021 | BCUS |
| BCAT-REQ-040 | REQ-CAT-035 | BEB-REQ-011, 020, 045 | Administration Domain; PRODUCT.md §24 item 23 |
| BCAT-REQ-041 | REQ-CAT-036 | BEB-REQ-011, 019–020 | BIDN; SECURITY-STANDARDS.md |
| BCAT-REQ-042 | REQ-CAT-037 | BEB-REQ-015, 020, 043–044 | SECURITY-STANDARDS.md |
| BCAT-REQ-043 | REQ-CAT-038 | BEB-REQ-016–018, 041, 049–051 | API.md |
| BCAT-REQ-044 | REQ-CAT-039 | BEB-REQ-041, 046, 051 | AGENTS.md §3.9 |
| BCAT-REQ-045 | REQ-CAT-040 | BEB-REQ-045–046 | SECURITY-STANDARDS.md §27 |
| BCAT-REQ-046 | REQ-CAT-041 | BEB-REQ-008, 013–018, 037–040, 050, 056 | API.md; EVENTS.md |
| BCAT-REQ-047 | REQ-CAT-042 | BEB-REQ-017, 028–031, 041–043 | API.md; DATABASE.md |
| BCAT-REQ-048 | REQ-CAT-043 | BEB-REQ-052–055 | TESTING-STANDARDS.md |

## 10. Category Domain Coverage Matrix

| Category Requirement | BCAT specialization |
| --- | --- |
| REQ-CAT-001 | BCAT-REQ-001–002, 006 |
| REQ-CAT-002 | BCAT-REQ-003, 007 |
| REQ-CAT-003 | BCAT-REQ-008 |
| REQ-CAT-004 | BCAT-REQ-009 |
| REQ-CAT-005 | BCAT-REQ-010 |
| REQ-CAT-006 | BCAT-REQ-011 |
| REQ-CAT-007 | BCAT-REQ-012 |
| REQ-CAT-008 | BCAT-REQ-013 |
| REQ-CAT-009 | BCAT-REQ-014 |
| REQ-CAT-010 | BCAT-REQ-015 |
| REQ-CAT-011 | BCAT-REQ-016 |
| REQ-CAT-012 | BCAT-REQ-017 |
| REQ-CAT-013 | BCAT-REQ-018 |
| REQ-CAT-014 | BCAT-REQ-019 |
| REQ-CAT-015 | BCAT-REQ-020 |
| REQ-CAT-016 | BCAT-REQ-021 |
| REQ-CAT-017 | BCAT-REQ-022 |
| REQ-CAT-018 | BCAT-REQ-023 |
| REQ-CAT-019 | BCAT-REQ-024 |
| REQ-CAT-020 | BCAT-REQ-025 |
| REQ-CAT-021 | BCAT-REQ-026 |
| REQ-CAT-022 | BCAT-REQ-027 |
| REQ-CAT-023 | BCAT-REQ-028 |
| REQ-CAT-024 | BCAT-REQ-029 |
| REQ-CAT-025 | BCAT-REQ-030 |
| REQ-CAT-026 | BCAT-REQ-031 |
| REQ-CAT-027 | BCAT-REQ-032 |
| REQ-CAT-028 | BCAT-REQ-033 |
| REQ-CAT-029 | BCAT-REQ-034 |
| REQ-CAT-030 | BCAT-REQ-035 |
| REQ-CAT-031 | BCAT-REQ-036 |
| REQ-CAT-032 | BCAT-REQ-037 |
| REQ-CAT-033 | BCAT-REQ-038 |
| REQ-CAT-034 | BCAT-REQ-039 |
| REQ-CAT-035 | BCAT-REQ-040 |
| REQ-CAT-036 | BCAT-REQ-041 |
| REQ-CAT-037 | BCAT-REQ-042 |
| REQ-CAT-038 | BCAT-REQ-043 |
| REQ-CAT-039 | BCAT-REQ-044 |
| REQ-CAT-040 | BCAT-REQ-045 |
| REQ-CAT-041 | BCAT-REQ-046 |
| REQ-CAT-042 | BCAT-REQ-047 |
| REQ-CAT-043 | BCAT-REQ-048 |

## 11. BEB Applicability and Accounting Matrix

| BEB Requirement(s) | Classification | BCAT rationale |
| --- | --- | --- |
| BEB-REQ-001–004 | Applicable | Lifecycle, inheritance, non-authority, specialization. BCAT-REQ-001–004. |
| BEB-REQ-005–007 | Applicable | Modular monolith, hexagonal direction, layers. BCAT-REQ-006. |
| BEB-REQ-008 | Applicable | Intentional Category Module Contract. BCAT-REQ-046. |
| BEB-REQ-009–010 | Applicable | Explicit Category Use Cases and truthful command/query outcomes. BCAT-REQ-028–033. |
| BEB-REQ-011–012 | Applicable | Contextual Authorization and owned transactions. BCAT-REQ-028–041. |
| BEB-REQ-013–018 | Applicable | Versioning, DTO separation, validation, bounds, errors, description, evolution. BCAT-REQ-043, 046–047. |
| BEB-REQ-019–020 | Applicable | Trusted Authentication evidence and contextual Authorization/concealment. BCAT-REQ-005, 041–042. |
| BEB-REQ-021–024 | Applicable | Category data ownership, Ports, integrity, and historical meaning. BCAT-REQ-006, 008–033. |
| BEB-REQ-025 | Applicable | Category invariant changes require explicit atomicity. BCAT-REQ-028–033. |
| BEB-REQ-026 | Not currently applicable | No external provider call is authorized; inherited if later Approved governance requires one. |
| BEB-REQ-027–031 | Applicable | Hierarchy/membership concurrency, durable uncertainty, idempotency, replay, duplicate safety. BCAT-REQ-014, 029–031, 047. |
| BEB-REQ-032–036 | Not currently applicable | No Category external provider, Callback, or Webhook is authorized; all controls remain conditional. |
| BEB-REQ-037–039 | Not currently applicable | Category events are conditional and no event Contract/delivery mechanism is authorized. BCAT-REQ-046. |
| BEB-REQ-040 | Applicable | BCAT cannot adopt external messaging independently. BCAT-REQ-046. |
| BEB-REQ-041–042 | Applicable | Safe failure, recovery, and reconciliation. BCAT-REQ-014, 027, 047. |
| BEB-REQ-043–044 | Applicable | Category data, Contracts, evidence, configuration, and Secrets require protection. BCAT-REQ-042. |
| BEB-REQ-045–046 | Applicable | Proportional Audit Records and safe observability. BCAT-REQ-044–045. |
| BEB-REQ-047–048 | Applicable | Configuration and reachable feature states preserve safeguards without selecting mechanisms. BCAT-REQ-044, 048. |
| BEB-REQ-049–051 | Applicable | Migration, compatibility, bounded work, and failure containment. BCAT-REQ-006, 043–044, 046–048. |
| BEB-REQ-052–055 | Applicable | Unit, application, Adapter, Contract, architecture, operational, traceable verification. BCAT-REQ-048. |
| BEB-REQ-056 | Applicable | Policy and implementation neutrality, including post-BCAT roadmap. BCAT-REQ-003–004, 015–025, 046–048. |

## 12. Dependency and Authority Matrix

| Capability | Consumption | Evidence / use | Authority retained outside BCAT |
| --- | --- | --- | --- |
| BEB | Inherited | Shared backend obligations | Shared baseline policy |
| BIDN | Conditional | Trusted Principal/Authentication evidence for protected behavior | Identity, Authentication, Sessions, tokens |
| BCUS | Conditional | Governed Customer/market context where Category policy requires | Customer, Account, Consent, Preference, segmentation |
| BPRD | Yes | Product/Product Variant identity and association evidence | Product identity, lifecycle, attributes, publication, media |
| BINV | Conditional | Non-authoritative Inventory evidence where separately governed | Stock, availability, reservation, sellability |
| BPRC | Conditional | Non-authoritative Pricing evidence where separately governed | Price and all commercial calculation |
| BCART | No current consumption required | Boundary evidence only | Cart intent and lifecycle |
| Search and Discovery | No governed backend Contract exists | Future consumer of Category-owned evidence | Query, ranking, filters, facets, indexes, Search projections |
| CMS | Conditional | CMS-owned publication/content evidence where governed | CMS workflow, scheduling, publication, media management |
| Administration | Consumer | Invokes Category-owned Use Cases | Administrative coordination; no Category truth or Role matrix |
| Checkout | No current consumption required | Boundary only; remains independently eligible and unresolved | Checkout orchestration and commitment |

## 13. Open Decisions and Unresolved Category Policy

The following **5 Open Product Decisions** from `PRODUCT.md` §24 remain unresolved:

| Item | Decision | BCAT boundary |
| ---: | --- | --- |
| 1 | Final brand name and visual identity. | Labels/presentation may be constrained; taxonomy and lifecycle are not selected. |
| 2 | Initial product categories and catalogue taxonomy. | Structure, roots, depth, naming, hierarchy, and classification remain open. |
| 22 | Content approval and scheduled-publication workflow. | No workflow, scheduler, state, timing, CMS mechanism, or publication behavior is selected. |
| 23 | Administrative role and permission matrix. | Protected behavior requires contextual Authorization without Roles, Permissions, mappings, or a matrix. |
| 27 | Product data import, export, and migration requirements. | No format, source, mechanism, schedule, or reconciliation policy is selected. |

Additional unresolved Category policy boundaries are membership cardinality, primary Category behavior, automatic classification, ordering, navigation presentation, lifecycle detail, withdrawal/deletion treatment, and Category content relationships. They are not additional numbered Product Decisions.

The following **9 Open Architecture Decisions** from `ARCHITECTURE.md` §34 remain unresolved:

| Item | Decision | BCAT boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting/topology selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC selected. |
| 3 | Customer and administrator session/token strategy. | No Session/token mechanism selected. |
| 8 | Redis introduction and its approved use cases. | No cache/Redis use selected. |
| 9 | External messaging introduction and service selection. | Events remain conditional; no service selected. |
| 10 | Initial search implementation details and extraction thresholds. | No Search engine, indexing, extraction, ranking, or synchronization selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, or numerical objective selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema layout selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism/lifecycle selected. |

## 14. Explicit Non-Decisions and Non-Authority

BCAT selects no Search or CMS implementation; administrative Role/Permission matrix; Product/Product Variant, Pricing, Inventory, Cart, Checkout, Order, Payment, Shipping, Return, Reporting, Notifications, Identity, or Customer authority; API/DTO shape; persistence schema or hierarchy representation; event schema or messaging; cache/Redis use; provider; infrastructure; numerical limit; unresolved Category policy; or future backend roadmap position.

## 15. Risks and Controls

| Risk | Control direction |
| --- | --- |
| Cyclic, orphaned, or inconsistent hierarchy | Validate before acceptance and preserve reconcilable authoritative hierarchy. |
| Stale or invalid Product membership | Consume BPRD evidence and preserve explicit association outcomes. |
| Projection becomes Category truth | Keep Search, CMS, navigation, caches, reporting, and UI representations non-authoritative. |
| Unauthorized administrative mutation | Enforce contextual least privilege/default denial without a Role matrix. |
| Concurrent/destructive change loses meaning | Preserve atomic invariants, history, conflict evidence, and safe recovery. |
| Unresolved taxonomy/lifecycle policy becomes code | Keep policy explicit, reversible, and unselected. |

## 16. Related Documents

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
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0008-post-cart-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/checkout/checkout-domain.md`

## 17. Governance Review Requirements

The completed approval review confirmed complete Category Domain and BEB coverage; one-to-one Requirements, Acceptance Criteria, and traceability; Category-only authority; bounded BIDN, BCUS, BPRD, BINV, BPRC, and BCART evidence; Search/CMS/Administration/Checkout separation; preservation of 5 Product and 9 Architecture decisions; security, integrity, recovery, observability, accessibility, compatibility, and verification completeness; and no invented policy, mechanism, number, or roadmap position. Required ownership reviews were completed for Architecture, Category, Product, Product Catalogue, Search and Discovery, CMS, Administration, Pricing, Inventory, Cart, Checkout, Identity, and Customer. No reviewer names, signatures, tickets, or external evidence are asserted.

## 18. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-19 | Approved | Approved BCAT after review confirmed complete Category Domain coverage, complete BEB accounting, Category-only authority, bounded cross-Domain dependencies, preserved Open Product and Architecture Decisions, and no post-BCAT roadmap selection. |
| 0.1.0 | 2026-09-19 | Draft | Established the initial Category-only Backend Specification from Approved Category authority, BEB, governed upstream backend evidence, and Accepted ADR-0008. |

## 19. Final Validation

Before material revision, re-approval, or implementation reliance, reviewers MUST validate:

1. metadata is `1.0.0` Approved, owner `Category`, `authoritative: false`, scope `BCAT`;
2. all 48 BCAT Requirements are unique and contiguous;
3. every BCAT Requirement has exactly one corresponding Acceptance Criterion and traceability row;
4. all 43 Category Domain Requirements are accounted for;
5. all 56 BEB Requirements are accounted for exactly once;
6. bounded dependencies transfer no authority;
7. all 5 Product and 9 Architecture decisions remain unresolved;
8. Search remains separate, Checkout remains unresolved, and no post-BCAT position is established;
9. no unsupported policy, mechanism, provider, schema, number, or physical design is selected;
10. the BCAT lifecycle change affects only `specifications/backend/category/category-backend.md`, passes whitespace validation, and introduces no unrelated repository changes.
