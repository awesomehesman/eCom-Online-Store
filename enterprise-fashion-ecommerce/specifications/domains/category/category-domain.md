---
title: Category Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-08-17
authoritative: false
---

# Category Domain

## 1. Purpose

This Specification defines Category-domain Requirements for Category identity, hierarchy, classification, Product association, navigation eligibility, lifecycle, historical integrity, administration, security, accessibility, testing, and traceability.

It refines the Approved Product and business baselines without defining Product, Pricing, Inventory, Customer, search, Cart, Checkout, Order, Payment, Shipping, Authorization policy, or administration workflow behavior.

## 2. Scope and Authority

This document is Approved and is normative only within its assigned Category-domain scope. Its `authoritative: false` metadata MUST remain false; approval does not make it a core Source of Truth.

`PRODUCT.md` remains the higher Product authority, `specifications/business/business-requirements.md` remains the upstream business baseline, and `specifications/domains/product/product-domain.md` governs Product-side Category association behavior. `GLOSSARY.md` owns canonical terminology, `ARCHITECTURE.md` owns Architecture, and Contracts remain separate. Conflicts MUST follow the `AGENTS.md` Decision Hierarchy.

**Requirement scope code:** `CAT`

Requirement identifiers use `REQ-CAT-NNN` and remain stable under `.ai/core/DOCUMENTATION-STANDARDS.md`.

## 3. Domain Context

The Category Domain owns coherent classification and navigation structure for the catalogue. It enables Product association and Customer discovery while preserving Product, Pricing, Inventory, Customer, search, Checkout, Payment, Order, Shipping, Authorization, and administration boundaries.

Category behavior is consumed by governed storefront, administration, Product, search, reporting, and integration contexts while their respective rules remain outside Category authority.

## 4. Canonical Terminology

This Specification uses Category, Product, Product Variant, Product Media, Projection, Staff User, Principal, Authentication, Authorization, Role, Permission, Claims, Audit Record, Sensitive Data, Secret, Contract, Domain Event, Integration Event, and Risk with the meanings in `GLOSSARY.md`.

`GLOSSARY.md` does not establish named Category lifecycle states. This Specification therefore does not reuse Product states such as Draft, Published, or Archived for Category, create Category state aliases, or define an identifier or slug scheme.

## 5. Category Ownership

### REQ-CAT-001 — Category-Domain Authority

The Category Domain MUST own stable Category identity, hierarchy, parent-child structure, taxonomy and classification meaning, Category-owned membership policy, Category lifecycle, and Category-owned navigation and visibility eligibility where Approved Product governance assigns them.

### REQ-CAT-002 — Downstream Authority Separation

Category state or association MUST NOT become authoritative for Product identity, Product descriptive content, Product Variant, Product Media, Product publication, Product visibility, Product structural sellability, Price, Discount, Promotion, Voucher, Stock, Stock Reservation, Available-to-Sell, Customer eligibility, final purchasability, Checkout, Payment, Order, Shipping, or Authorization policy.

## 6. Category Identity

### REQ-CAT-003 — Stable Category Identity

A Category MUST have stable identity independent of mutable name, label, description, placement, parent association, navigation eligibility, or presentation. Renaming or moving a Category MUST NOT silently replace its identity.

### REQ-CAT-004 — Semantic Category Uniqueness

Category creation or change MUST prevent or explicitly resolve semantically duplicate or conflicting Categories within the governed taxonomy context. This Specification does not define numeric identifiers, UUIDs, external codes, URL slugs, or another identifier format.

## 7. Category Hierarchy

### REQ-CAT-005 — Valid Parent-Child Relationship

A Category parent-child relationship MUST reference valid governed Categories and MUST preserve coherent classification meaning. Root or top-level treatment applies only where Approved Product governance establishes it.

### REQ-CAT-006 — Acyclic Hierarchy

The Category hierarchy MUST remain acyclic. A Category MUST NOT be its own parent or ancestor, directly or indirectly.

### REQ-CAT-007 — Orphan Handling

A Category whose required parent is missing, invalid, withdrawn, or otherwise unavailable for hierarchy use MUST NOT be represented as validly placed. The outcome MUST be distinguishable and recoverable through governed correction where permitted.

### REQ-CAT-008 — Hierarchy Consistency

Hierarchy changes MUST preserve valid ancestor and descendant relationships and MUST NOT leave contradictory, unreachable, or silently reassigned Category structure.

### REQ-CAT-009 — Hierarchy Projection Reconciliation

Navigation, search, cache, UI, reporting, and analytical Projections MAY lag authoritative Category hierarchy changes but MUST reconcile without redefining Category truth or restoring obsolete relationships.

No hierarchy depth, root count, hierarchy algorithm, or implementation structure is selected here.

## 8. Taxonomy and Classification

### REQ-CAT-010 — Taxonomy Authority

The Category Domain MUST remain authoritative for Category taxonomy and classification structure within its assigned scope. Other Domains and Projections MAY consume governed Category meaning but MUST NOT redefine it.

### REQ-CAT-011 — Coherent Classification Meaning

Category names, relationships, and classification meaning MUST remain coherent and non-conflicting for their governed context. Taxonomy changes MUST NOT silently redefine Product identity, Product history, or historical commercial meaning.

The initial catalogue taxonomy, Category depth, fashion classification structure, merchandising taxonomy, Attribute taxonomy, and SEO taxonomy remain unresolved unless Approved Product governance establishes them.

## 9. Category Membership

### REQ-CAT-012 — Valid Product Association

A Category association MUST reference an existing Category eligible for the governed association and an existing Product governed by the Product Domain. Invalid, ambiguous, or unauthorized association MUST be rejected with an explainable outcome.

### REQ-CAT-013 — Category-Side Membership Policy

The Category Domain MUST enforce Category-side membership validity and policy where Approved Product governance establishes them, while the Product Domain retains Product identity, publication, and Product-side association authority.

### REQ-CAT-014 — Membership Non-Authority

Category membership MUST NOT independently publish Product, establish Product visibility, establish Price or Inventory truth, or authorize purchase. Category reassignment or removal MUST preserve Product authority and historical commercial integrity.

Single versus multiple membership, primary Category, automatic assignment, and rule-based classification remain unresolved.

## 10. Category Lifecycle

### REQ-CAT-015 — Governed Category Lifecycle

Category creation, modification, hierarchy use, classification use, navigation eligibility, withdrawal or retirement, and deletion eligibility MUST follow an explicit governed lifecycle owned by Category.

### REQ-CAT-016 — Unresolved Category States

Until Approved Product governance establishes exact Category lifecycle states and transitions, implementations MUST preserve distinguishable lifecycle outcomes without presenting local names as repository-wide canonical states or silently inferring Product lifecycle state.

## 11. Visibility and Navigation Eligibility

### REQ-CAT-017 — Category Navigation Eligibility

Only a Category satisfying applicable Category-owned hierarchy, lifecycle, content, and navigation Requirements MAY be represented as eligible for Customer navigation.

### REQ-CAT-018 — Navigation Non-Authority

Category navigation or visibility eligibility MUST NOT publish Product, override Product visibility, establish search ranking, establish Customer eligibility, or prove Price, Stock, Available-to-Sell, or final purchasability. UI, cache, search, and reporting Projections MUST remain non-authoritative.

No menu structure, mega-menu, navigation depth, breadcrumb design, URL format, or presentation mechanism is selected here.

## 12. Category Ordering

### REQ-CAT-019 — Governed Category Ordering

Where Approved Product governance requires sibling or navigation ordering, Category MUST provide deterministic business ordering without changing Category identity or authority. Exact ordering policy remains unresolved; no sort-index format, spacing strategy, drag-and-drop behavior, or alphabetical default is selected.

## 13. Category Content

### REQ-CAT-020 — Category-Owned Content

Category-owned Customer-facing content MUST preserve meaningful classification and MAY include governed names, labels, descriptions, or presentation references where Approved Product and Design System governance establish them. This Specification does not define mandatory fields, SEO metadata, Product content, Product Media ownership, or presentation values.

## 14. Historical and Product Association Integrity

### REQ-CAT-021 — Historical Category Meaning

Category rename, reparenting, reclassification, withdrawal, retirement, or deletion eligibility MUST NOT rewrite confirmed Order snapshots or make retained Product and commercial references unintelligible.

### REQ-CAT-022 — Product Association Change Safety

A Category change affecting Product associations MUST preserve valid Product references or require explicit governed resolution. It MUST NOT silently remove Product from commercial history, change Product publication, or invent Cart, Checkout, or Order behavior.

## 15. Category Creation

### REQ-CAT-023 — Governed Category Creation

Creating a Category MUST establish stable identity, coherent classification meaning, a valid parent context where applicable, and semantic uniqueness without accidental Customer navigation eligibility. Invalid, unauthorized, incomplete, cyclic, or duplicate creation MUST be rejected. Only where Approved Category or Product lifecycle governance explicitly permits retention of incomplete Category state MAY it remain in a governed non-navigation-eligible outcome. Explainable recovery MUST remain available where permitted, and appropriate Audit Record evidence MUST be retained where material.

No form, Endpoint, schema, or persistence design is established here.

## 16. Category Update

### REQ-CAT-024 — Governed Category Update

An authorized Category update MUST preserve stable identity, hierarchy validity, classification coherence, Product association integrity, and applicable navigation eligibility. Concurrent or stale updates MUST produce a safe, explainable outcome without silent accepted-data loss or downstream authority change.

No locking, version-field, merge, or persistence mechanism is selected.

## 17. Reparenting

### REQ-CAT-025 — Governed Category Reparenting

Reparenting MUST NOT introduce a cycle or invalid parent-child relationship. The resulting ancestor and descendant structure MUST remain coherent, affected Product associations MUST remain valid or receive explicit governed handling, and downstream Projections MUST reconcile without changing Product publication, Price, or Inventory truth.

No migration or hierarchy-update algorithm is selected.

## 18. Reclassification

### REQ-CAT-026 — Governed Category Reclassification

Reclassification MUST preserve Category identity, coherent taxonomy meaning, historical references, and valid Product associations. A material classification change MUST be explicit and MUST NOT silently change Product publication, Product content, Price, Inventory, Customer eligibility, or purchase permission.

## 19. Withdrawal and Retirement

### REQ-CAT-027 — Governed Category Withdrawal

Authorized withdrawal or retirement MUST stop new Category-owned navigation or classification use where applicable while preserving historical meaning and requiring governed handling of existing Product associations. Delayed navigation or search Projections MUST reconcile and MUST NOT reactivate the Category or alter Product truth.

Existing Cart and Checkout behavior remains owned by downstream Product policy.

## 20. Category Deletion

### REQ-CAT-028 — Governed Category Deletion

Hard deletion MUST NOT be the default for a referenced or historically material Category. Deletion MAY occur only where governing retention, historical, integrity, security, and Product Requirements permit it; withdrawal or retirement MAY be required instead. No retention period, anonymization technique, archival mechanism, or storage lifecycle is established here.

## 21. Search Boundary

### REQ-CAT-029 — Search Projection Integrity

Search behavior MUST remain a non-authoritative Projection of governed Category hierarchy, classification, and navigation eligibility. Stale or delayed search state MUST reconcile without creating or redefining Category truth.

### REQ-CAT-030 — Search Result Non-Authority

A search result MUST NOT independently publish Product, override Category or Product visibility, or prove Product Price, Stock, Available-to-Sell, Customer eligibility, or final purchasability.

No search technology, index schema, ranking algorithm, refresh interval, or consistency mechanism is selected.

## 22. Product Boundary

### REQ-CAT-031 — Product Authority Boundary

The Category Domain MUST treat Product identity, Product descriptive content, Product Variant, Product Media, Product publication, Product structural sellability, Product visibility eligibility, and Product-owned association behavior as Product-owned concerns. Category MUST NOT redefine or override them.

## 23. Pricing Boundary

### REQ-CAT-032 — Pricing Authority Boundary

Category classification MAY be an input to future governed Pricing policy only where Approved Product and Pricing governance establish it. Category MUST NOT own or calculate authoritative Price, Discount, Promotion, Voucher, tax, Money, Currency calculation, or Pricing Rule behavior, and no Category-based Promotion is established here.

## 24. Inventory Boundary

### REQ-CAT-033 — Inventory Authority Boundary

Category MUST NOT own or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, or other Inventory truth. Category state, placement, or navigation eligibility MUST NOT imply Inventory availability.

## 25. Customer and Market Boundary

### REQ-CAT-034 — Customer and Market Authority Boundary

Category MUST NOT own Account state, Preference, consent, Customer eligibility, market expansion, language expansion, or Currency expansion. Personalized Category visibility or eligibility is not established by this Specification.

## 26. Administration Boundary

### REQ-CAT-035 — Administration Boundary

Administration MAY invoke Category capabilities through governed Staff User workflows, but those workflows MUST preserve Authentication, server-side Authorization, Category invariants, hierarchy validity, Product associations, auditability, and safe handling of high-Risk changes. They MUST NOT redefine Category rules or bypass the Category Domain.

No Role or Permission matrix, approval chain, administration form, or bulk-operation implementation is defined.

## 27. Authentication and Authorization

Authentication establishes Principal identity; Authorization determines whether that Principal may perform an action on a Resource.

### REQ-CAT-036 — Category Authorization

Protected Category creation, update, reparenting, reclassification, withdrawal, deletion, and navigation-eligibility changes MUST enforce server-side Authorization for the current Principal, Resource, action, and applicable Category state. UI state, Role labels, Permissions, Claims, navigation, or search state MUST NOT substitute for the authoritative Authorization decision.

No Identity Provider, protocol, token, Session, password, or MFA policy is selected.

## 28. Security and Sensitive Data

Category data is generally catalogue and navigation data, while internal or pre-release taxonomy may require protection according to context.

### REQ-CAT-037 — Category Data Safety

Internal, pre-release, provider-related, or operational Category information MUST receive Sensitive Data protection where its context requires it. Secrets, credentials, private provider evidence, and unrelated Customer PII MUST NOT be stored in Category content, associations, imports, exports, logs, fixtures, or ordinary operational evidence.

No data-classification framework, encryption implementation, DLP technology, or retention period is established.

## 29. Accessibility

### REQ-CAT-038 — Accessible Category Meaning

Customer-facing Category labels, content, and classification MUST support the Approved WCAG 2.2 AA outcome. Category names and navigation meaning MUST remain understandable, and classification MUST NOT be conveyed solely through inaccessible presentation.

`.ai/frontend/ACCESSIBILITY.md`, `.ai/frontend/UI.md`, and the Design System own implementation and evidence mechanics. This Requirement is not a certification claim and does not impose Level AAA.

## 30. Performance

### REQ-CAT-039 — Bounded Category Delivery

Category hierarchy, detail, association, and navigation structures MUST support bounded delivery through later governed Contracts and MUST NOT require an unbounded entire taxonomy payload.

This Specification establishes no payload limit, performance budget, percentile, latency target, device profile, network profile, SLA, or SLO. `.ai/frontend/PERFORMANCE.md` owns applicable implementation targets and evidence.

## 31. Audit

### REQ-CAT-040 — Material Category Audit Evidence

Material reparenting, taxonomy restructuring, withdrawal, deletion, high-Risk classification change, and other Category actions governed as auditable MUST produce appropriate Audit Records containing sufficient actor or system context, action, time, and outcome for authorized investigation.

Ordinary label editing is not automatically a high-Risk action; applicable Product, Security, and administration governance determines audit proportionality.

## 32. Events

Category creation, hierarchy change, reclassification, withdrawal, or deletion MAY require Domain Event or Integration Event behavior only when Approved Architecture, a governed Contract, or downstream reliability Requirements establish it.

This Specification does not mandate event publication, invent an event name or schema, select an Outbox Pattern, or assume exactly-once delivery. `.ai/backend/EVENTS.md` governs applicable implementation behavior.

## 33. Contract Impacts

Later governed Contracts may be required for Category hierarchy queries, Category detail, Product association, navigation Projection inputs, and administration operations.

### REQ-CAT-041 — Category Contract Boundary

Contracts exposing Category information or operations MUST preserve Category and Product authority, lifecycle, Authorization, compatibility, failure, stale-state, and Sensitive Data boundaries. This Specification MUST NOT itself define REST paths, HTTP methods, status codes, JSON fields, DTOs, schemas, event payloads, database structures, or provider formats.

## 34. Failure and Recovery

### REQ-CAT-042 — Category Failure Outcomes

Invalid hierarchy, cycle attempt, invalid parent, stale or conflicting update, duplicate Category, unauthorized operation, referenced Category deletion attempt, Projection lag, and classification failure MUST produce distinguishable, safe, explainable, and recoverable outcomes where recovery is permitted.

Failure output MUST NOT expose Sensitive Data, Secrets, inaccessible Resource existence, or implementation internals. No transport response or technical error code is defined.

## 35. Testing

### REQ-CAT-043 — Category Verification Coverage

Tests and acceptance evidence MUST cover applicable positive, negative, Authorization, hierarchy, cycle, orphan, duplicate, Product-association, navigation, Projection, concurrency, withdrawal, deletion, security, accessibility, audit, failure, and recovery behavior. Test design and evidence MUST follow `.ai/core/TESTING-STANDARDS.md` without selecting a test framework here.

## 36. Acceptance Criteria

Acceptance Criteria have no separate identifiers.

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-CAT-001 | Category identity, hierarchy, taxonomy, membership policy, lifecycle, and navigation eligibility are identifiable as Category-owned concerns. |
| REQ-CAT-002 | Category state cannot change authoritative Product, Pricing, Inventory, Customer, Checkout, Payment, Order, Shipping, or Authorization policy. |
| REQ-CAT-003 | Renaming, relabelling, moving, or changing Category presentation preserves the same Category identity. |
| REQ-CAT-004 | A semantically duplicate or conflicting Category is rejected or explicitly resolved without relying on an invented identifier or slug format. |
| REQ-CAT-005 | A parent-child association is accepted only when both Categories and their governed classification relationship are valid. |
| REQ-CAT-006 | Direct self-parenting and every indirect ancestor cycle are rejected. |
| REQ-CAT-007 | A Category with an invalid required parent is not represented as validly placed and receives explainable governed recovery where permitted. |
| REQ-CAT-008 | A hierarchy change leaves coherent ancestor, descendant, reachability, and assignment outcomes without silent reassignment. |
| REQ-CAT-009 | Delayed navigation, search, cache, UI, reporting, and analytical Projections reconcile to current Category hierarchy without restoring obsolete relationships. |
| REQ-CAT-010 | Category remains the authoritative source for taxonomy and classification structure; consumers cannot redefine that truth. |
| REQ-CAT-011 | Conflicting classification meaning is rejected or governed, and taxonomy changes do not rewrite Product identity or historical commercial meaning. |
| REQ-CAT-012 | An association succeeds only for valid governed Category and Product references; invalid, ambiguous, or unauthorized association is rejected explainably. |
| REQ-CAT-013 | Category-side membership policy is enforced without taking Product identity, publication, or Product-side association authority. |
| REQ-CAT-014 | Category membership cannot publish Product, establish Product visibility, establish Price or Inventory truth, or authorize purchase; reassignment preserves history. |
| REQ-CAT-015 | Category creation, modification, hierarchy/classification use, navigation eligibility, withdrawal, retirement, and deletion eligibility follow an explicit governed lifecycle. |
| REQ-CAT-016 | Lifecycle outcomes remain distinguishable without inventing canonical Category state names or inferring Product lifecycle state. |
| REQ-CAT-017 | A Category is navigation-eligible only after applicable Category hierarchy, lifecycle, content, and navigation Requirements pass. |
| REQ-CAT-018 | Category navigation does not publish Product or prove Product visibility, search rank, Customer eligibility, Price, Stock, Available-to-Sell, or purchasability. |
| REQ-CAT-019 | Where ordering is governed, repeated reads produce deterministic business order without changing Category identity or inventing a default policy. |
| REQ-CAT-020 | Category content provides meaningful governed classification without creating mandatory fields, SEO policy, Product content, Product Media ownership, or presentation values. |
| REQ-CAT-021 | Rename, move, reclassification, withdrawal, retirement, and eligible deletion preserve confirmed Order snapshots and intelligible historical references. |
| REQ-CAT-022 | A Product-association change preserves valid references or requires explicit resolution without silently changing publication or commercial history. |
| REQ-CAT-023 | Valid authorized creation produces stable identity, valid hierarchy context, semantic uniqueness, and no accidental navigation eligibility; invalid, unauthorized, incomplete, cyclic, or duplicate creation is rejected, or incomplete state remains non-navigation-eligible only where governing lifecycle policy explicitly permits that outcome; explainable recovery exists where permitted, with applicable Audit Record evidence where material. |
| REQ-CAT-024 | An authorized update preserves Category identity, hierarchy, classification, Product associations, and navigation eligibility; a stale or concurrent conflict produces a safe outcome that is explainable to the authorized actor or operational process, does not silently overwrite accepted data, and does not silently change Product, Pricing, Inventory, or other downstream authority. |
| REQ-CAT-025 | Reparenting rejects cycles and invalid parents, preserves coherent ancestor and descendant relationships, keeps affected Product associations valid where possible or gives them explicit governed handling, and reconciles downstream Projections without itself changing Product publication, Pricing, or Inventory truth. |
| REQ-CAT-026 | Reclassification preserves Category identity, coherent taxonomy and classification meaning, historical references, and Product associations where valid or gives them governed handling where applicable; a material reclassification is explicit before acceptance and does not silently change Product publication, Product content, Price, Inventory, Customer eligibility, or purchase permission. |
| REQ-CAT-027 | Withdrawal stops applicable new Category use, preserves historical meaning, preserves existing Product associations where they remain valid or subjects them to explicit governed handling, and reconciles delayed Projections without reactivation or alteration of Product truth. |
| REQ-CAT-028 | Hard deletion is not the default; permitted deletion satisfies governing constraints without an invented retention or archival mechanism. |
| REQ-CAT-029 | Search behavior is a reconcilable non-authoritative Projection and cannot create or redefine Category truth. |
| REQ-CAT-030 | Search results cannot publish Product or prove Category/Product visibility, Price, Inventory, Customer eligibility, or final purchasability. |
| REQ-CAT-031 | Category cannot redefine Product identity, content, Product Variant, Product Media, publication, structural sellability, visibility, or Product-side association behavior. |
| REQ-CAT-032 | Category may supply governed classification input but cannot own or calculate Pricing, tax, Money, Currency, Promotion, Discount, or Voucher outcomes. |
| REQ-CAT-033 | Category cannot own or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, or availability truth. |
| REQ-CAT-034 | Category cannot establish Account, Preference, consent, Customer eligibility, market, language, Currency expansion, or personalized visibility policy. |
| REQ-CAT-035 | A governed Staff User workflow preserves Authentication, Authorization, Category invariants, hierarchy, Product associations, auditability, and safe high-Risk handling. |
| REQ-CAT-036 | A protected Category action is denied when authoritative server-side Authorization for the Principal, Resource, action, or applicable Category state fails; UI state, Role labels, client-represented Permissions or Claims, navigation visibility, and search state or results cannot authorize or substitute for that decision. |
| REQ-CAT-037 | Contextually Sensitive Category information receives protection, and Secrets, credentials, provider evidence, and unrelated Customer PII are absent from prohibited artifacts. |
| REQ-CAT-038 | Category labels and navigation meaning provide WCAG 2.2 AA evidence without relying solely on inaccessible presentation or claiming certification. |
| REQ-CAT-039 | Category structures support bounded Contract delivery without inventing a payload limit, latency target, or measurement profile. |
| REQ-CAT-040 | Applicable material Category actions produce Audit Records with actor/system context, action, time, and outcome while ordinary label edits remain proportionately governed. |
| REQ-CAT-041 | A future Category Contract preserves Category authority, Product authority, applicable lifecycle semantics, server-side Authorization expectations, compatibility obligations, governed failure behavior, stale-state and reconciliation semantics, and Sensitive Data protection while this Specification defines no wire or persistence design. |
| REQ-CAT-042 | Applicable invalid hierarchy, cycle attempt, invalid parent, stale or concurrent update, duplicate Category, unauthorized operation, referenced Category deletion attempt, Projection lag, and classification failure produce distinguishable, safe, explainable, and recoverable-where-permitted outcomes without exposing Sensitive Data, Secrets, inaccessible Resource existence, or implementation internals. |
| REQ-CAT-043 | Evidence covers applicable positive, negative, Authorization, hierarchy, cycle, orphan, duplicate, Product-association, navigation, Projection, concurrency, withdrawal, deletion, security, accessibility, audit, failure, and recovery scenarios under Testing Standards. |

## 37. Requirement Traceability

No applicable Category-domain Decision Record currently exists. Traceability therefore uses Approved Product sections, business Requirements, Product Domain Requirements, governing sources, and downstream scopes without fabricating a Decision identifier.

| Requirement | Product source | Business Requirement source | Product Domain source | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-CAT-001 | PRODUCT.md §§12.1, 12.5, 13 | REQ-BUS-003, 004 | REQ-PRD-045 | ARCHITECTURE.md §9 | Category, Product, frontend, backend |
| REQ-CAT-002 | PRODUCT.md §§5.4, 13, 16.1–16.4 | REQ-BUS-003, 015, 017 | REQ-PRD-002, 020, 023, 024 | ARCHITECTURE.md §9 | All affected Domains |
| REQ-CAT-003 | PRODUCT.md §§12.1, 12.5, 17.4 | REQ-BUS-003, 048 | REQ-PRD-045 | GLOSSARY.md | Category, Product, Contracts |
| REQ-CAT-004 | PRODUCT.md §§9.1, 24 | REQ-BUS-003, 048 | REQ-PRD-039, 045 | TESTING-STANDARDS.md | Category, administration |
| REQ-CAT-005 | PRODUCT.md §§12.1, 12.5, 24 | REQ-BUS-003, 004 | REQ-PRD-045 | ARCHITECTURE.md §9 | Category, Product, navigation |
| REQ-CAT-006 | PRODUCT.md §§12.1, 12.5, 24 | REQ-BUS-003, 004 | REQ-PRD-045 | TESTING-STANDARDS.md | Category, administration, tests |
| REQ-CAT-007 | PRODUCT.md §§5.6, 12.1, 35.2 | REQ-BUS-004, 042 | REQ-PRD-040, 045 | TESTING-STANDARDS.md | Category, navigation, administration |
| REQ-CAT-008 | PRODUCT.md §§12.1, 17.3–17.4 | REQ-BUS-004, 048 | REQ-PRD-045 | ARCHITECTURE.md §9 | Category, Product, navigation |
| REQ-CAT-009 | PRODUCT.md §§12.1, 14.1, 17.3 | REQ-BUS-004, 044 | REQ-PRD-020, 024 | ARCHITECTURE.md §28 | Category, search, frontend |
| REQ-CAT-010 | PRODUCT.md §§12.1, 13 | REQ-BUS-003, 004 | REQ-PRD-045 | ARCHITECTURE.md §9 | Category, Product, search |
| REQ-CAT-011 | PRODUCT.md §§9.1, 17.4, 24 | REQ-BUS-003, 048 | REQ-PRD-006, 030, 045 | DATABASE.md | Category, Product, history |
| REQ-CAT-012 | PRODUCT.md §§12.1, 12.5, 15.1 | REQ-BUS-003, 031 | REQ-PRD-024, 039, 045 | SECURITY-STANDARDS.md | Category, Product, administration |
| REQ-CAT-013 | PRODUCT.md §§12.1, 13, 24 | REQ-BUS-003, 048 | REQ-PRD-024, 045 | DOCUMENTATION-STANDARDS.md | Category, Product |
| REQ-CAT-014 | PRODUCT.md §§5.4, 13, 14.1–14.4 | REQ-BUS-003, 004, 015, 017 | REQ-PRD-002, 020, 023, 024, 045 | ARCHITECTURE.md §9 | Product, Pricing, Inventory, Checkout |
| REQ-CAT-015 | PRODUCT.md §§17, 37.4 | REQ-BUS-003, 048 | REQ-PRD-045 | GLOSSARY.md | Category, administration, navigation |
| REQ-CAT-016 | PRODUCT.md §§17.1–17.3, 24 | REQ-BUS-048 | REQ-PRD-045 | GLOSSARY.md | Category, implementation, tests |
| REQ-CAT-017 | PRODUCT.md §§12.1, 14.1, 17 | REQ-BUS-003, 004 | REQ-PRD-023, 024, 045 | ARCHITECTURE.md §9 | Category, navigation, frontend |
| REQ-CAT-018 | PRODUCT.md §§5.4, 13, 14.1–14.4 | REQ-BUS-003, 004, 015, 017 | REQ-PRD-020, 023, 024, 045 | ARCHITECTURE.md §§9, 28 | Category, Product, search, Checkout |
| REQ-CAT-019 | PRODUCT.md §§12.1, 14.1, 24 | REQ-BUS-003, 004 | REQ-PRD-045 | DESIGN-SYSTEM.md | Category, navigation, frontend |
| REQ-CAT-020 | PRODUCT.md §§12.1, 16.8, 32 | REQ-BUS-003, 004, 050 | REQ-PRD-024, 045 | DESIGN-SYSTEM.md | Category, frontend, administration |
| REQ-CAT-021 | PRODUCT.md §§16.4, 17.4, 37.4 | REQ-BUS-022, 023, 048 | REQ-PRD-006, 030, 031 | DATABASE.md | Category, Product, Order, support |
| REQ-CAT-022 | PRODUCT.md §§13, 17.3–17.4, 37.4 | REQ-BUS-003, 022, 048 | REQ-PRD-006, 024, 030, 031, 045 | DATABASE.md | Category, Product, Order |
| REQ-CAT-023 | PRODUCT.md §§12.5, 15.1, 31 | REQ-BUS-031, 032, 034 | REQ-PRD-039, 045, 049, 050 | SECURITY-STANDARDS.md | Category, administration, backend |
| REQ-CAT-024 | PRODUCT.md §§15.1, 17.3–17.4, 31.4 | REQ-BUS-031, 036, 048 | REQ-PRD-035, 045, 049 | TESTING-STANDARDS.md | Category, administration, backend |
| REQ-CAT-025 | PRODUCT.md §§12.1, 15.1, 17.3–17.4 | REQ-BUS-004, 031, 048 | REQ-PRD-024, 045 | TESTING-STANDARDS.md | Category, Product, search, administration |
| REQ-CAT-026 | PRODUCT.md §§12.1, 17.3–17.4, 24 | REQ-BUS-003, 004, 048 | REQ-PRD-024, 030, 045 | TESTING-STANDARDS.md | Category, Product, administration |
| REQ-CAT-027 | PRODUCT.md §§12.1, 17.3–17.4, 37.4 | REQ-BUS-003, 004, 048 | REQ-PRD-020, 024, 031, 045 | ARCHITECTURE.md §28 | Category, Product, search, navigation |
| REQ-CAT-028 | PRODUCT.md §§17.4, 37.4 | REQ-BUS-023, 039, 048 | REQ-PRD-030, 031, 045 | DATABASE.md; SECURITY-STANDARDS.md | Category, Product, operations |
| REQ-CAT-029 | PRODUCT.md §§12.1, 14.1, 17.3 | REQ-BUS-004, 044 | REQ-PRD-020, 024 | ARCHITECTURE.md §28 | Category, search, frontend |
| REQ-CAT-030 | PRODUCT.md §§5.4, 13, 14.1–14.4 | REQ-BUS-004, 015, 017 | REQ-PRD-020, 023, 024 | ARCHITECTURE.md §28 | Search behavior, Product, Pricing, Inventory |
| REQ-CAT-031 | PRODUCT.md §§12.1, 12.5, 13, 16.8 | REQ-BUS-003, 005, 050 | REQ-PRD-001, 002, 020, 023, 024, 045 | ARCHITECTURE.md §9 | Category, Product, frontend, backend |
| REQ-CAT-032 | PRODUCT.md §§13, 16.1, 16.6 | REQ-BUS-014, 015, 016 | REQ-PRD-002, 045, 046 | ARCHITECTURE.md §9 | Category, Pricing, Checkout |
| REQ-CAT-033 | PRODUCT.md §§13, 16.2 | REQ-BUS-017, 018, 019, 020 | REQ-PRD-002, 045, 047 | ARCHITECTURE.md §§9, 20.3 | Category, Inventory, Checkout |
| REQ-CAT-034 | PRODUCT.md §§7.1–7.2, 13, 16.7 | REQ-BUS-001, 007, 032 | REQ-PRD-002, 048 | ARCHITECTURE.md §9 | Category, Customer, Authorization |
| REQ-CAT-035 | PRODUCT.md §§7.3–7.5, 12.5, 15.1, 31 | REQ-BUS-031, 032, 033, 036 | REQ-PRD-049, 050 | SECURITY-STANDARDS.md §12 | Category, administration, security |
| REQ-CAT-036 | PRODUCT.md §§7.3–7.5, 15.1, 31 | REQ-BUS-032, 033 | REQ-PRD-050 | SECURITY-STANDARDS.md §12 | Category, administration, frontend, backend |
| REQ-CAT-037 | PRODUCT.md §§16.7–16.8, 32 | REQ-BUS-039, 041, 050 | REQ-PRD-051 | SECURITY-STANDARDS.md §35 | Category, administration, security, operations |
| REQ-CAT-038 | PRODUCT.md §§5.7, 14.1, 16.9, 35.2 | REQ-BUS-003, 037 | REQ-PRD-041 | ACCESSIBILITY.md; DESIGN-SYSTEM.md | Category, frontend, administration |
| REQ-CAT-039 | PRODUCT.md §§8.3, 18.4, 35.2 | REQ-BUS-038 | REQ-PRD-042 | PERFORMANCE.md | Category, frontend, backend, Contracts |
| REQ-CAT-040 | PRODUCT.md §§15.1, 17.4, 31, 35.3 | REQ-BUS-034 | REQ-PRD-043 | SECURITY-STANDARDS.md §27 | Category, administration, operations |
| REQ-CAT-041 | PRODUCT.md §§22, 26, 35 | REQ-BUS-047, 048 | REQ-PRD-044, 052 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Category, Contracts, frontend, backend |
| REQ-CAT-042 | PRODUCT.md §§5.6, 17.2, 30, 35.2 | REQ-BUS-036, 042 | REQ-PRD-040 | SECURITY-STANDARDS.md; API.md | Category, administration, frontend, backend |
| REQ-CAT-043 | PRODUCT.md §§26.2–26.4, 35.3 | REQ-BUS-047 | REQ-PRD-039, 044 | TESTING-STANDARDS.md | Category, tests, acceptance evidence |

Future Accepted Decisions must be linked rather than copied. A change to Category truth requires synchronized governing updates before this Specification may rely on it.

## 38. Open Product Decisions

The following current `PRODUCT.md` concerns are Category-domain relevant and remain unresolved:

| Product Decision concern | Category-domain boundary |
| --- | --- |
| Final brand name and visual identity | Brand governance may constrain Category labels and presentation but does not establish taxonomy or lifecycle here. |
| Initial product Categories and catalogue taxonomy | Initial structure, depth, roots, naming, and classification remain undecided. |
| Content approval and scheduled-publication workflow | Future Approved content-approval or scheduled-publication governance may affect Category content or navigation eligibility; this Specification establishes no approval workflow, scheduled-publication behavior, Category lifecycle state names, workflow technology, scheduler, approval chain, Role, timing rule, or publication mechanism. |
| Product data import, export, and migration Requirements | No Category taxonomy format, migration source, import mechanism, schedule, or reconciliation policy is selected. |

Product-to-Category membership cardinality, primary-Category behavior, Category ordering, and navigation presentation remain unresolved aspects of the applicable taxonomy and content governance; this Specification does not represent them as separately recorded Product Decisions. No `DEC-####` identifier is fabricated.

## 39. Risks

| Risk | Required treatment |
| --- | --- |
| Hierarchy contains a cycle | Reject direct and indirect cycles before accepting the relationship. |
| Category becomes orphaned or unreachable | Detect invalid parent context and require explainable governed correction. |
| Taxonomy meaning conflicts or duplicates | Govern semantic uniqueness and coherent classification. |
| Product associations become stale | Preserve or explicitly resolve associations and reconcile consumers. |
| Navigation drifts from Category truth | Treat navigation, search, cache, UI, and reporting as reconcilable Projections. |
| Category absorbs Product, Pricing, or Inventory authority | Preserve explicit Domain boundaries and authoritative revalidation. |
| Destructive taxonomy change erases history | Preserve stable identity, Product references, Order snapshots, and historical meaning. |
| Unresolved policy becomes implementation behavior | Keep Product Decisions explicit and avoid embedded defaults. |

No numerical Risk score or tolerance is established here.

## 40. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/product/product-domain.md`

## 41. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the Category Domain Specification after final governance, Category identity, hierarchy, taxonomy, Product association, lifecycle, navigation, authority boundaries, security, accessibility, performance, Acceptance Criteria, and traceability validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Category Domain Specification covering Category identity, hierarchy, taxonomy, Product association, navigation eligibility, lifecycle, boundaries, Acceptance Criteria, and traceability. |

## 42. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version `1.0.0` Approved with `authoritative: false`;
2. the scope code remains `CAT`, Requirement identifiers remain unique and sequential, and retired identifiers are not reused;
3. Category terminology remains aligned with `GLOSSARY.md`, and no ungoverned named Category lifecycle state is introduced;
4. Category owns classification and navigation truth without absorbing Product, Pricing, Inventory, Customer, search, Cart, Checkout, Order, Payment, Shipping, Authorization policy, or administration workflow authority;
5. hierarchy, cycle, orphan, taxonomy, membership, navigation, history, deletion, Authorization, security, accessibility, audit, and failure Requirements remain testable;
6. Search behavior, navigation, cache, UI, reporting, and analytical Projections remain non-authoritative and reconcilable;
7. every material Requirement retains observable Acceptance Criteria and one-to-one traceability;
8. Category-relevant Open Product Decisions remain unresolved unless Accepted governance and synchronized source updates exist;
9. no path, method, status code, schema, database structure, Angular Component, Spring class, event name, provider integration, cloud resource, implementation technology, or unsupported numerical target is introduced;
10. Related Documents exist, contain no self-reference, and remain relevant;
11. headings remain sequential and unique, sections remain non-empty, tables remain valid, and no TODO, TBD, FIXME, placeholder, or actual ellipsis remains; and
12. only `specifications/domains/category/category-domain.md` changes for a scoped update.
