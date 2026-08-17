---
title: Product Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-08-17
authoritative: false
---

# Product Domain

## 1. Purpose

This Specification defines Product-domain Requirements for Product, Product Variant, Attribute, Product Media, publication, Product-owned sellability, catalogue visibility eligibility, lifecycle, historical integrity, and downstream boundaries.

It refines the Approved Product baseline and `specifications/business/business-requirements.md` without defining Category, Pricing, Inventory, Customer, Cart, Checkout, Order, Payment, Shipping, or administration behavior.

## 2. Scope and Authority

This document is Approved and is normative only within its assigned Product-domain scope. Its `authoritative: false` metadata MUST remain false; approval does not make it a core Source of Truth.

`PRODUCT.md` remains the higher Product authority, `GLOSSARY.md` owns canonical terminology, `ARCHITECTURE.md` owns Architecture, and the Approved business Requirements remain the upstream business baseline. Contracts and implementation standards remain separate. Conflicts MUST follow the `AGENTS.md` Decision Hierarchy.

**Requirement scope code:** `PRD`

Requirement identifiers use `REQ-PRD-NNN` and remain stable under `.ai/core/DOCUMENTATION-STANDARDS.md`.

## 3. Domain Context

The Product Domain owns the descriptive catalogue definition and Product-owned lifecycle of Products and Product Variants. It enables Customer understanding and downstream association while preventing Product content or publication from becoming authority for Price, Inventory, Customer eligibility, Checkout, Payment, Order, or Shipping truth.

The Product Domain MUST remain usable by governed storefront, administration, search, Pricing, Inventory, Cart, Checkout, Order, and reporting consumers without absorbing their rules.

## 4. Canonical Terminology

This Specification uses Product, Product Variant, Attribute, Product Media, Draft, Published, Archived, Category, Price, Inventory, Stock, Stock Reservation, Available-to-Sell, Projection, Staff User, Principal, Authorization, Audit Record, Sensitive Data, and Secret with the meanings in `GLOSSARY.md`.

The canonical Product states used here are limited to Draft, Published, and Archived. This Specification does not create aliases, additional repository-wide states, a SKU format, or another identifier scheme.

## 5. Product Ownership

### REQ-PRD-001 — Product-Domain Authority

The Product Domain MUST own stable Product identity, descriptive Product content, Product Variant definition, Product-owned Attributes, Product Media association and metadata, Product-owned publication state, and Product-owned visibility and structural sellability eligibility.

### REQ-PRD-002 — Downstream Authority Separation

Product state MUST NOT become authoritative for Price, Discount, Promotion, Voucher, Money calculation, Currency calculation, tax, Stock, Stock Reservation, Available-to-Sell, final purchasability, Customer eligibility, Authorization, Payment, Order, Shipping, or Category hierarchy and classification.

## 6. Product Entity

### REQ-PRD-003 — Stable Product Identity

A Product MUST have a stable identity and represent the sellable catalogue concept that groups one or more Product Variants where applicable. Identity MUST NOT be silently replaced when descriptive content changes.

### REQ-PRD-004 — Coherent Product Definition

A Product MUST maintain coherent Product-owned descriptive information and associations sufficient to distinguish it from another Product and support governed Customer evaluation.

### REQ-PRD-005 — Product Lifecycle

A Product MUST have an explicit Product-owned lifecycle using the applicable canonical Draft, Published, and Archived states. State transitions MUST be valid, authorized, and traceable where material; no local or downstream state may silently publish or reactivate a Product.

### REQ-PRD-006 — Historical Product Meaning

Product lifecycle and descriptive changes MUST NOT rewrite confirmed Order snapshots, invalidate historical Orders, or make retained commercial references unintelligible.

## 7. Product Variant

### REQ-PRD-007 — Product Variant Membership

A Product Variant MUST represent a purchasable or selectable Product configuration where applicable and MUST belong to exactly one Product within Product-domain ownership.

### REQ-PRD-008 — Stable Product Variant Identity

A Product Variant MUST have stable identity independent of mutable descriptions, current Price, and Inventory state. This Specification does not define SKU or barcode formats.

### REQ-PRD-009 — Product Variant Uniqueness

Variant-defining Attributes MUST distinguish Product Variants within a Product, and the Product Domain MUST reject semantically equivalent duplicate Product Variant configurations under the governed Attribute model.

### REQ-PRD-010 — Product Variant Lifecycle Consistency

A Product Variant MUST remain consistent with its owning Product lifecycle and MAY be Product-owned publishable or structurally sellable only when its Product-owned validation and publication Requirements are satisfied. Price and Inventory do not determine Product Variant lifecycle state.

## 8. Attributes

### REQ-PRD-011 — Attribute Semantics

The Product Domain MUST distinguish descriptive Attributes from variant-defining Attributes so that Customer meaning and Product Variant uniqueness remain unambiguous.

### REQ-PRD-012 — Attribute Validity

An Attribute MUST use governed meaning, normalization, and allowed values where Product governance establishes them. Duplicate names, conflicting meanings, or incompatible values MUST be rejected or resolved before publication.

### REQ-PRD-013 — Attribute Evolution

Attribute changes MUST preserve Product Variant identity and MUST NOT silently rewrite historical commercial snapshots. Fixed size, color, material, fabric, locale, and other taxonomies remain unresolved unless Approved Product governance establishes them.

## 9. Product Media

### REQ-PRD-014 — Product Media Association

Product Media MUST be associated with the applicable Product or Product Variant, retain enough Product-owned context to prevent ambiguous use, and support governed ordering where presentation order is required.

### REQ-PRD-015 — Product Media Accuracy and Accessibility

Product Media MUST accurately represent the associated Product or Product Variant and support applicable accessibility Requirements, including meaningful text alternatives owned through Product and Design System governance.

### REQ-PRD-016 — Product Media Publication

Private, Draft, invalid, unauthorized, or otherwise unapproved Product Media MUST NOT become Customer-visible. Published Product Media MUST satisfy applicable rights, security, quality, and accessibility Requirements.

### REQ-PRD-017 — Product Media Failure and Correction

Missing, failed, stale, or misleading Product Media MUST be represented honestly, MUST NOT imply unsupported Product truth, and MUST support correction or withdrawal without rewriting historical Order meaning.

This Specification does not select media storage, transformation, delivery, upload, naming, sizing, aspect-ratio, CDN, or pipeline technology. Azure Blob Storage remains an Architecture and Azure implementation direction outside this Domain Specification.

## 10. Publication

### REQ-PRD-018 — Publication Completeness

A Product or Product Variant MUST NOT become Published until applicable Product-owned identity, descriptive content, Attribute, Product Media, lifecycle, and validation Requirements are complete.

### REQ-PRD-019 — Authorized Publication

Publication, unpublication, and Product-owned reactivation MUST require server-side Authorization and MUST preserve appropriate actor, time, state-change, and Audit Record evidence where material.

### REQ-PRD-020 — Publication Authority

Only Product-owned state may establish Product publication truth. Search, Category navigation, UI state, caches, reports, analytics, imports, and other Projections MUST NOT independently publish a Product or Product Variant.

## 11. Sellability

### REQ-PRD-021 — Product-Owned Sellability Eligibility

The Product Domain MUST determine whether Product-owned structure, lifecycle, content, Attributes, and Product Media make a Product or Product Variant structurally eligible for sale.

### REQ-PRD-022 — Final Purchasability Boundary

Product-owned sellability eligibility MUST NOT be represented as final purchasability. Final purchase commitment may additionally require authoritative Price, Inventory, Customer or market eligibility, Checkout policy, and other governed outcomes from their owning Domains.

## 12. Catalogue Visibility

### REQ-PRD-023 — Product Visibility Eligibility

A Product or Product Variant MAY be Customer-visible only when its Product-owned publication and visibility Requirements are satisfied. UI, cache, Category, search, or reporting state MUST NOT independently override Product visibility eligibility.

### REQ-PRD-024 — Visibility Context Boundary

Product-owned visibility MAY provide inputs to discovery, but Category membership, search ranking, Customer or market eligibility, Price, availability, and final purchase permission remain governed by their owners.

## 13. Product Content Quality

### REQ-PRD-025 — Product Information Completeness

Published Product content MUST contain the minimum Approved Product-owned information required for Customer understanding and purchase confidence without inventing unresolved field sets in this Specification.

### REQ-PRD-026 — Truthful Product Content

Product descriptions, Attributes, Product Media, and claims MUST be accurate, supportable, accessible where applicable, and free of misleading Price, Discount, availability, delivery, or commercial implications.

### REQ-PRD-027 — Product Content Correction

Stale, inaccurate, unsupported, or misleading Product content MUST support authorized correction, withdrawal, or unpublication, with material changes remaining traceable.

Editorial style, branding values, fixed content fields, and exact Product-copy rules remain governed by Product and Design System sources rather than this Specification.

## 14. Content Lifecycle

### REQ-PRD-028 — Governed Content Lifecycle

Product content MUST support governed creation, editing, validation, publication, correction, withdrawal or unpublication, and archival under the Product lifecycle. Exact approval chains, scheduling workflows, and additional content states remain unresolved unless Approved governance establishes them.

### REQ-PRD-029 — No Silent Lifecycle Transition

An edit, import, Projection refresh, media change, or downstream operation MUST NOT silently change Product publication state. Invalid transitions MUST be rejected with an explainable recovery path.

## 15. Product History

### REQ-PRD-030 — Historical Reference Integrity

Later Product, Product Variant, Attribute, Product Media, publication, or retirement changes MUST NOT rewrite confirmed Order snapshots. Retained historical references MUST remain intelligible to authorized Customer, support, financial, and operational contexts where required.

### REQ-PRD-031 — Historical Lifecycle Integrity

Archiving, retirement, unpublication, or deletion eligibility MUST preserve the meaning and traceability of prior commercial activity. This Specification does not establish a retention period, anonymization technique, or archival mechanism.

## 16. Category Boundary

Category is the owner of Category hierarchy, parent-child rules, classification, navigation taxonomy, membership policy, and Category lifecycle. Product may retain governed references without acquiring Category authority.

### REQ-PRD-045 — Category Authority Boundary

The Product Domain MUST treat Category hierarchy, parent-child structure, taxonomy, classification, navigation taxonomy, membership policy, and Category lifecycle as Category-owned concerns. Product MAY retain governed Category references or associations but MUST NOT redefine or independently validate Category-owned structure. Category state or association MUST NOT independently publish Product, establish Price or Inventory truth, or authorize purchase.

## 17. Pricing Boundary

Pricing remains authoritative for Price, Discount, Promotion, Voucher, Money calculations, Currency calculation rules, tax calculations, Pricing Rules, and Price history. Product may consume governed derived Pricing information without acquiring Pricing authority.

### REQ-PRD-046 — Pricing Authority Boundary

The Product Domain MUST treat Price, Discount, Promotion, Voucher, Money calculations, Currency calculation rules, tax calculations, Pricing Rules, and Price history as Pricing-owned concerns. Product MAY consume or expose derived Pricing information through governed Contracts or Projections but MUST NOT calculate authoritative Price, embed ungoverned Price as Product truth, or imply that derived Pricing information remains current without authoritative Pricing evidence.

## 18. Inventory Boundary

Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, allocation, and applicable Stock Reservation expiry or release. Product may consume governed Inventory outputs without acquiring Inventory authority.

### REQ-PRD-047 — Inventory Authority Boundary

The Product Domain MUST treat Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, allocation, and applicable Stock Reservation expiry or release as Inventory-owned concerns. Product-owned visibility or structural sellability MUST NOT infer Inventory truth. Product, search, cache, UI, reporting, and analytical Projections MUST remain non-authoritative for Inventory.

No Stock Reservation duration, allocation algorithm, back-order policy, Safety Stock, replenishment policy, synchronization technology, or concurrency technology is selected here.

## 19. Search Boundary

### REQ-PRD-032 — Search Projection Integrity

Search MUST remain a non-authoritative Projection derived from governed Product state. Only Product state eligible for indexing under Approved policy may be supplied, and stale or delayed search state MUST be reconciled without redefining Product truth.

### REQ-PRD-033 — Search Result Non-Authority

A search result MUST NOT prove current Product publication, Price, Stock, Available-to-Sell, final purchasability, or Customer eligibility. Applicable authoritative outcomes MUST be revalidated through their owning Domains.

No search technology, index schema, refresh interval, ranking algorithm, or consistency mechanism is selected here.

## 20. Customer and Market Boundary

Customer and market owners retain their respective eligibility, Account, Preference, consent, access, and expansion concerns.

### REQ-PRD-048 — Customer and Market Authority Boundary

The Product Domain MUST NOT own Customer eligibility, Account state, Preference, consent, Permissions, market expansion, language expansion, or Currency expansion. Product-owned visibility constraints MAY exist only where Approved Product governance establishes them and MUST NOT substitute for Customer, Authorization, market, or Checkout decisions.

## 21. Administration Boundary

Administration owns Staff User operational workflows, not Product rules.

### REQ-PRD-049 — Administration Boundary

Administration MAY invoke Product capabilities through governed Staff User workflows, but such workflows MUST NOT bypass Product validation, lifecycle, publication, historical-integrity, or concurrency Requirements. Protected Product operations MUST be performed only by an authenticated and authorized Principal.

This Specification does not define a Role or Permission matrix, approval chain, administration form, or bulk-operation implementation.

## 22. Product Creation

### REQ-PRD-034 — Governed Product Creation

Creating a Product MUST establish stable identity and valid Product-owned information without accidental publication. Invalid, unauthorized, incomplete, or materially duplicate creation MUST be rejected or retained only in a valid non-published state with an understandable recovery path.

Material Product creation MUST retain appropriate Audit Record evidence where governed. This Specification does not define forms, Endpoints, schemas, or persistence.

## 23. Product Update

### REQ-PRD-035 — Governed Product Update

An authorized Product update MUST validate changed Product-owned information, preserve stable identity and historical snapshots, and MUST NOT silently change publication state.

Concurrent or stale updates MUST produce a safe, explainable outcome that prevents silent loss of accepted Product changes. No locking, version-field, merge, or persistence mechanism is prescribed.

## 24. Product Variant Creation and Update

### REQ-PRD-036 — Governed Product Variant Mutation

Creating or updating a Product Variant MUST preserve its owning Product association, stable identity, governed variant-defining Attributes, semantic uniqueness, and lifecycle consistency. Invalid or duplicate configurations MUST be rejected with an explainable outcome.

Product Variant mutation MUST NOT create or alter authoritative Price, Stock, Stock Reservation, or Available-to-Sell state.

## 25. Unpublication and Retirement

### REQ-PRD-037 — Governed Unpublication or Archival

Authorized unpublication or transition to Archived MUST stop new Customer visibility according to Product-owned state while preserving historical commercial references and appropriate Audit Record evidence.

Existing Cart or Checkout treatment remains owned by applicable downstream Product policy. A search Projection MAY lag but MUST reconcile; lag MUST NOT reactivate or independently publish Product.

## 26. Deletion

### REQ-PRD-038 — Governed Product Deletion

Hard deletion MUST NOT be the default for a Product or Product Variant. Deletion MAY occur only where governing retention, historical, legal, security, and Product Requirements permit it; referenced Product data MAY require unpublication or archival instead.

Deletion MUST preserve historical commercial integrity. Product content MUST NOT contain Customer PII, Secrets, or unrelated Sensitive Data merely to support deletion behavior, and no retention timeline is established here.

## 27. Validation Rules

### REQ-PRD-039 — Product-Owned Validation

Product-owned validation MUST cover applicable identity, required descriptive information, coherent Product Variant definitions, Attribute validity, semantic uniqueness, Product Media association and validity, lifecycle transition validity, and publication completeness.

Product validation MUST NOT duplicate or replace Pricing, Inventory, Category, Customer, Checkout, Payment, Order, or Shipping validation.

## 28. Failure and Recovery

### REQ-PRD-040 — Product Failure Outcomes

Validation failure, stale or conflicting update, missing or invalid Product Media, incomplete Product, unauthorized operation, publication failure, invalid lifecycle transition, and Projection lag MUST produce distinguishable, explainable, and recoverable outcomes where recovery is permitted.

Failure output MUST NOT expose Sensitive Data, Secrets, inaccessible Resource existence, or implementation internals. This Specification defines no technical error code or transport response.

## 29. Authentication and Authorization

Authentication establishes Principal identity; Authorization determines whether that Principal may perform an action on a Resource.

### REQ-PRD-050 — Product Authorization

Protected Product mutation, publication, unpublication, archival, deletion, and other governed operations MUST enforce server-side Authorization for the current Principal, Resource, action, and applicable Product state. UI state, Role labels, Permissions, Claims, search state, or administrative navigation MUST NOT substitute for the authoritative Authorization decision.

No Identity Provider, protocol, token strategy, Session strategy, password policy, or MFA policy is selected here.

## 30. Security and Sensitive Data

Published Product catalogue data is intended for Customer visibility. Other Product information may require protection according to context; this Specification does not create a data-classification scheme.

### REQ-PRD-051 — Product Data Safety

Draft, unpublished, pre-release, internal, provider-related, or operational Product information MUST receive Sensitive Data protection where its context requires it. Secrets, credentials, private provider evidence, and unrelated Customer PII MUST NOT be stored in Product content, Product Media metadata, imports, exports, logs, fixtures, or ordinary Product operational evidence.

No classification levels, encryption implementation, DLP technology, or retention period is established here.

## 31. Accessibility

### REQ-PRD-041 — Accessible Product Content

Product content MUST support the Approved WCAG 2.2 AA outcome where applicable. Product Media MUST have meaningful alternatives, text must remain meaningful without inaccessible presentation, and Product-owned information MUST support equivalent Customer understanding.

`.ai/frontend/ACCESSIBILITY.md` and the Design System own accessibility implementation and evidence mechanics. This Requirement is not a certification claim and does not impose Level AAA.

## 32. Performance

### REQ-PRD-042 — Bounded Product Content Delivery

Product content and Product Variant structures MUST support bounded delivery through later governed Contracts and MUST NOT require unbounded Product, Attribute, or Product Media payloads.

This Specification establishes no payload limit, performance budget, percentile, latency target, device profile, network profile, SLA, or SLO. `.ai/frontend/PERFORMANCE.md` owns applicable implementation targets and evidence.

## 33. Audit and Traceability

### REQ-PRD-043 — Material Product Audit Evidence

Material publication, unpublication, archival, deletion, high-Risk change, and other Product changes governed as auditable MUST produce appropriate Audit Records containing sufficient actor or system context, action, time, and outcome for authorized investigation.

Ordinary field editing is not automatically a high-Risk action; applicable Product, Security, and administration governance determines audit proportionality.

### REQ-PRD-044 — Requirement Traceability

Downstream Specifications, Contracts, implementation, tests, and acceptance evidence SHOULD reference applicable `REQ-PRD-NNN` and upstream `REQ-BUS-NNN` identifiers where this materially improves traceability. A reference alone MUST NOT be represented as compliance evidence.

## 34. Events

Product creation, publication, change, unpublication, or archival MAY require Domain Event or Integration Event behavior only when Approved Architecture, a governed Contract, or downstream reliability Requirements establish it.

The canonical `ProductCreated` and `ProductPublished` concepts MAY be used only with their glossary meanings. This Specification does not mandate event publication, define a Message, select an Outbox Pattern, create an Integration Event name or schema, or assume exactly-once delivery.

## 35. Contract Impacts

Later governed Contracts may be required for Product query and detail, Product Variant information, Product Media, publication and administrative operations, search Projection inputs, and Product associations consumed by Pricing and Inventory.

### REQ-PRD-052 — Product Contract Boundary

Contracts exposing Product information or operations MUST preserve Product authority, lifecycle, Authorization, compatibility, failure, stale-state, and Sensitive Data boundaries. This Product Domain Specification MUST NOT itself define REST paths, HTTP methods, status codes, JSON fields, DTOs, schemas, event payloads, database structures, or provider formats.

## 36. Acceptance Criteria

Acceptance Criteria are grouped for maintainability and have no separate identifiers.

| Requirement references | Observable Acceptance Criteria |
| --- | --- |
| REQ-PRD-001–002 | Product-owned identity, descriptive content, Product Variant, Attribute, Product Media, lifecycle, visibility, and structural sellability state can be identified; Product state cannot change authoritative Pricing, Inventory, Category, Customer, Authorization, Payment, Order, or Shipping truth. |
| REQ-PRD-003–006 | A valid Product has stable identity and coherent descriptive meaning; valid lifecycle transitions are authorized and traceable; invalid transitions are rejected; later changes do not rewrite or invalidate historical Orders. |
| REQ-PRD-007–010 | Every Product Variant belongs to one Product, retains stable identity, uses valid distinguishing Attributes, rejects a semantically duplicate configuration, and cannot become Product-owned publishable before applicable Product validation passes. |
| REQ-PRD-011–013 | Descriptive and variant-defining Attributes remain distinguishable; conflicting or invalid governed values are rejected; Attribute edits preserve Product Variant identity and historical commercial snapshots. |
| REQ-PRD-014–017 | Product Media is associated unambiguously and accurately represents its subject; private, Draft, invalid, unauthorized, or otherwise unapproved Product Media cannot become Customer-visible; Published Product Media demonstrates applicable rights, security, quality, and accessibility evidence; failed, stale, or misleading media supports honest representation, correction, or withdrawal. |
| REQ-PRD-018–020 | Incomplete or unauthorized Product state cannot become Published; an applicable material publication, unpublication, or reactivation demonstrates the authorized Principal or system context, action, time, previous state where applicable, resulting state, and applicable Audit Record evidence; UI, cache, search, Category, reporting, import, and analytics state cannot independently publish Product. |
| REQ-PRD-021–022 | Product-owned structural sellability can be evaluated independently; it is not represented as final purchasability without applicable Pricing, Inventory, Customer, market, Checkout, and other governed outcomes. |
| REQ-PRD-023–024 | Only Product-owned eligible state is Customer-visible; Category, search, UI, cache, Price, availability, and Customer context do not independently override Product visibility. |
| REQ-PRD-025–027 | Published Product content demonstrates minimum governed completeness, accurate and supportable claims, accessible Customer meaning, and correction or withdrawal of stale or misleading information. |
| REQ-PRD-028–029 | Creation, editing, validation, publication, correction, withdrawal, and archival follow the Product lifecycle; edits, imports, Projections, and media changes cannot cause a silent publication-state transition; an invalid lifecycle transition is rejected and provides an explainable governed recovery path where recovery is permitted. |
| REQ-PRD-030–031 | Product changes, unpublication, archival, retirement, and eligible deletion preserve intelligible historical references and confirmed Order snapshots without inventing retention mechanics. |
| REQ-PRD-032–033 | Search contains only eligible indexed Product state, reconciles lag or drift, and does not prove current publication, Price, Inventory, Customer eligibility, or final purchasability. |
| REQ-PRD-034 | Valid authorized creation produces stable non-accidentally-published Product state and material evidence; invalid, incomplete, unauthorized, or materially duplicate creation is rejected or retained only in a valid unpublished state. |
| REQ-PRD-035 | An authorized update validates changes, preserves identity and history, does not silently publish, and handles stale or concurrent conflict without silent accepted-data loss. |
| REQ-PRD-036 | Product Variant mutation preserves Product membership, identity, Attribute validity, uniqueness, and lifecycle while leaving Price and Inventory unchanged. |
| REQ-PRD-037 | Authorized unpublication or archival stops new Product visibility, retains historical meaning and material evidence, and reconciles delayed search without deciding existing Cart or Checkout policy. |
| REQ-PRD-038 | Hard deletion is not the default; permitted deletion satisfies governing constraints and preserves historical commercial integrity without an invented retention period. |
| REQ-PRD-039 | Product validation covers all Product-owned concerns and does not duplicate Pricing, Inventory, Category, Customer, Checkout, Payment, Order, or Shipping validation. |
| REQ-PRD-040 | Material failure states are distinguishable and safely recoverable where permitted; unauthorized or invalid actions are rejected without disclosing Sensitive Data, Secrets, inaccessible Resource existence, or internals. |
| REQ-PRD-041 | Product content and Product Media provide applicable WCAG 2.2 AA evidence without a certification or Level AAA claim. |
| REQ-PRD-042 | Product content and Product Variant structures support bounded Contract delivery without inventing a payload limit, performance target, or measurement profile. |
| REQ-PRD-043 | Applicable material Product actions produce Audit Record evidence containing sufficient actor or system context, action, time, and outcome; ordinary low-Risk field edits are not automatically elevated, and audit proportionality remains governed. |
| REQ-PRD-044 | Downstream evidence references applicable stable Product and business Requirement identifiers without treating a reference as proof. |
| REQ-PRD-045 | Product can retain a valid governed Category association without redefining or independently validating Category structure; Category state or association cannot independently publish Product, establish Price or Inventory truth, or authorize purchase. |
| REQ-PRD-046 | Product can consume governed derived Pricing output without calculating authoritative Price, embedding ungoverned Price as Product truth, or representing stale derived Pricing information as current without authoritative Pricing evidence. |
| REQ-PRD-047 | Product-owned visibility and structural sellability do not infer Inventory truth; Product, search, cache, UI, reporting, and analytical Projections cannot establish Stock, Stock Reservation, or Available-to-Sell authority. |
| REQ-PRD-048 | Customer and market eligibility remain external to Product; Product visibility cannot substitute for Customer, Authorization, market, or Checkout eligibility decisions. |
| REQ-PRD-049 | An authenticated and authorized Principal can invoke a Product capability through a governed Staff User workflow; the workflow cannot bypass Product validation, lifecycle, publication, historical-integrity, or concurrency Requirements. |
| REQ-PRD-050 | A protected Product action is denied when authoritative server-side Authorization for the current Principal, Resource, action, or applicable Product state fails; UI, Role, Permission, Claims, search, or navigation state cannot authorize the action by itself. |
| REQ-PRD-051 | Contextually Sensitive Product information receives applicable protection, and Secrets, credentials, private provider evidence, and unrelated Customer PII are absent from prohibited Product content, metadata, imports, exports, logs, fixtures, and ordinary operational evidence. |
| REQ-PRD-052 | A future Product Contract preserves Product authority, lifecycle, Authorization, compatibility, failure, stale-state, and Sensitive Data boundaries, while this Specification contains no REST, HTTP, JSON, DTO, schema, event-payload, database, or provider-format design. |

Negative, Authorization, lifecycle, stale-state, historical, accessibility, and Projection cases apply where material. No Acceptance Criteria identifier scheme is introduced.

## 37. Requirement Traceability

No applicable Product-domain Decision Record currently exists. Traceability therefore uses Approved Product sections, business Requirements, governing sources, and downstream scopes without fabricating a Decision identifier.

| Requirement | Product source | Business Requirement source | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- |
| REQ-PRD-001 | PRODUCT.md §§9.1, 12.1, 12.5, 13, 16.8 | REQ-BUS-003, 005, 050 | ARCHITECTURE.md §9 | Product, admin, frontend, backend |
| REQ-PRD-002 | PRODUCT.md §§5.4, 13, 16.1–16.4 | REQ-BUS-014, 015, 017 | ARCHITECTURE.md §§9–10 | All affected Domains |
| REQ-PRD-003 | PRODUCT.md §§12.1, 12.5, 13 | REQ-BUS-003, 005 | GLOSSARY.md | Product, Contracts |
| REQ-PRD-004 | PRODUCT.md §§8.2, 14.2, 32.4 | REQ-BUS-005 | DESIGN-SYSTEM.md | Product, frontend |
| REQ-PRD-005 | PRODUCT.md §§17, 32.2 | REQ-BUS-003, 050 | GLOSSARY.md | Product, admin, search |
| REQ-PRD-006 | PRODUCT.md §§16.4, 17.4, 37.4 | REQ-BUS-022, 048 | DATABASE.md | Product, Order, support |
| REQ-PRD-007 | PRODUCT.md §§12.2, 13, 14.2–14.3 | REQ-BUS-006 | GLOSSARY.md | Product, Cart/Checkout |
| REQ-PRD-008 | PRODUCT.md §§13, 16.2 | REQ-BUS-006, 017 | GLOSSARY.md | Product, Inventory, Pricing |
| REQ-PRD-009 | PRODUCT.md §§9.1, 24 | REQ-BUS-006 | GLOSSARY.md | Product, admin |
| REQ-PRD-010 | PRODUCT.md §§14.2–14.3, 17 | REQ-BUS-003, 006, 017 | ARCHITECTURE.md §9 | Product, Cart/Checkout |
| REQ-PRD-011 | PRODUCT.md §§9.1, 12.5, 24 | REQ-BUS-005, 006 | GLOSSARY.md | Product, frontend, admin |
| REQ-PRD-012 | PRODUCT.md §§24, 32.3–32.4 | REQ-BUS-005, 050 | GLOSSARY.md | Product, admin |
| REQ-PRD-013 | PRODUCT.md §§16.4, 24 | REQ-BUS-022, 048 | GLOSSARY.md | Product, Order, admin |
| REQ-PRD-014 | PRODUCT.md §§12.5, 14.2, 32 | REQ-BUS-005, 050 | DESIGN-SYSTEM.md | Product, frontend, admin |
| REQ-PRD-015 | PRODUCT.md §§16.8–16.9, 32.3 | REQ-BUS-005, 037, 050 | ACCESSIBILITY.md | Product, frontend |
| REQ-PRD-016 | PRODUCT.md §§16.8, 32.2–32.4 | REQ-BUS-003, 050 | SECURITY-STANDARDS.md | Product, frontend, admin |
| REQ-PRD-017 | PRODUCT.md §§5.6, 32.3, 35 | REQ-BUS-042, 046, 050 | ACCESSIBILITY.md | Product, frontend, operations |
| REQ-PRD-018 | PRODUCT.md §§16.8, 32.2–32.4, 35.3 | REQ-BUS-003, 050 | DESIGN-SYSTEM.md | Product, admin |
| REQ-PRD-019 | PRODUCT.md §§7.3–7.5, 15.1, 16.8 | REQ-BUS-031, 032, 034 | SECURITY-STANDARDS.md | Product, admin, backend |
| REQ-PRD-020 | PRODUCT.md §§13, 17.3, 32.2 | REQ-BUS-003, 004, 044 | ARCHITECTURE.md §28 | Product, search, frontend |
| REQ-PRD-021 | PRODUCT.md §§13, 14.2–14.4 | REQ-BUS-005, 006 | ARCHITECTURE.md §§9, 25.2 | Product, Cart/Checkout |
| REQ-PRD-022 | PRODUCT.md §§5.4, 13, 14.3–14.4, 16.1–16.2 | REQ-BUS-014, 015, 017 | ARCHITECTURE.md §9 | Product, Pricing, Inventory, Checkout |
| REQ-PRD-023 | PRODUCT.md §§14.1–14.2, 16.8, 32 | REQ-BUS-003, 004 | ARCHITECTURE.md §28 | Product, frontend, search |
| REQ-PRD-024 | PRODUCT.md §§12.1, 13, 14.1 | REQ-BUS-003, 004, 015, 017 | ARCHITECTURE.md §§9, 28 | Product, Category, search, frontend |
| REQ-PRD-025 | PRODUCT.md §§8.2, 16.8, 32.3–32.4 | REQ-BUS-005, 050 | DESIGN-SYSTEM.md | Product, frontend, admin |
| REQ-PRD-026 | PRODUCT.md §§5.4, 16.1, 16.8, 32.3 | REQ-BUS-005, 015, 050 | SECURITY-STANDARDS.md | Product, frontend |
| REQ-PRD-027 | PRODUCT.md §§16.8, 32.2–32.3 | REQ-BUS-050 | DOCUMENTATION-STANDARDS.md | Product, admin |
| REQ-PRD-028 | PRODUCT.md §§17, 32.2, 37.4 | REQ-BUS-050 | GLOSSARY.md | Product, admin, search |
| REQ-PRD-029 | PRODUCT.md §§17.3, 32.2 | REQ-BUS-003, 050 | TESTING-STANDARDS.md | Product, admin, backend |
| REQ-PRD-030 | PRODUCT.md §§16.4, 17.4, 34 | REQ-BUS-022, 050 | DATABASE.md | Product, Order, support |
| REQ-PRD-031 | PRODUCT.md §§17.4, 37.4 | REQ-BUS-023, 048 | DATABASE.md; SECURITY-STANDARDS.md | Product, Order, operations |
| REQ-PRD-032 | PRODUCT.md §§12.1, 14.1, 17 | REQ-BUS-004, 044 | ARCHITECTURE.md §28 | Product, search, backend |
| REQ-PRD-033 | PRODUCT.md §§5.4, 14.1–14.4, 23 | REQ-BUS-004, 015, 017 | ARCHITECTURE.md §28 | Search, Pricing, Inventory, Checkout |
| REQ-PRD-034 | PRODUCT.md §§12.5, 15.1, 16.8, 31 | REQ-BUS-031, 032, 034, 050 | SECURITY-STANDARDS.md | Product, admin, backend |
| REQ-PRD-035 | PRODUCT.md §§15.1, 17.3–17.4, 31.4 | REQ-BUS-022, 031, 036 | TESTING-STANDARDS.md | Product, admin, backend |
| REQ-PRD-036 | PRODUCT.md §§12.5, 15.1, 24 | REQ-BUS-006, 015, 017 | GLOSSARY.md | Product, admin, Pricing, Inventory |
| REQ-PRD-037 | PRODUCT.md §§16.8, 17.3–17.4, 37.4 | REQ-BUS-003, 022, 050 | ARCHITECTURE.md §28 | Product, search, Cart/Checkout, Order |
| REQ-PRD-038 | PRODUCT.md §§17.4, 37.4 | REQ-BUS-023, 039, 048 | DATABASE.md; SECURITY-STANDARDS.md | Product, Order, operations |
| REQ-PRD-039 | PRODUCT.md §§26.2–26.4, 35.3 | REQ-BUS-003, 005, 006, 050 | TESTING-STANDARDS.md | Product, admin, tests |
| REQ-PRD-040 | PRODUCT.md §§5.6, 17.2, 30, 35.2 | REQ-BUS-042 | SECURITY-STANDARDS.md; API.md | Product, admin, frontend, backend |
| REQ-PRD-041 | PRODUCT.md §§5.7, 16.9, 32.3, 35.2 | REQ-BUS-005, 037, 050 | ACCESSIBILITY.md; DESIGN-SYSTEM.md | Product, frontend |
| REQ-PRD-042 | PRODUCT.md §§8.3, 18.4, 35.2 | REQ-BUS-038 | PERFORMANCE.md | Product, frontend, backend, Contracts |
| REQ-PRD-043 | PRODUCT.md §§15.1, 17.4, 31, 35.3 | REQ-BUS-034, 050 | SECURITY-STANDARDS.md | Product, admin, operations |
| REQ-PRD-044 | PRODUCT.md §§22, 26, 35 | REQ-BUS-047, 048 | DOCUMENTATION-STANDARDS.md | All downstream Product scopes |
| REQ-PRD-045 | PRODUCT.md §§12.1, 12.5, 13 | REQ-BUS-003, 004 | ARCHITECTURE.md §9 | Product, Category, search, frontend |
| REQ-PRD-046 | PRODUCT.md §§13, 16.1 | REQ-BUS-014, 015, 016 | ARCHITECTURE.md §9 | Product, Pricing, frontend, Checkout |
| REQ-PRD-047 | PRODUCT.md §§13, 16.2 | REQ-BUS-017, 018, 019, 020 | ARCHITECTURE.md §§9, 20.3 | Product, Inventory, search, frontend |
| REQ-PRD-048 | PRODUCT.md §§7.1–7.2, 13, 16.7 | REQ-BUS-001, 007, 032 | ARCHITECTURE.md §9 | Product, Customer, Authorization, Checkout |
| REQ-PRD-049 | PRODUCT.md §§7.3–7.5, 12.5, 15.1, 31 | REQ-BUS-031, 032, 033, 036 | SECURITY-STANDARDS.md §12 | Product, administration, security, backend |
| REQ-PRD-050 | PRODUCT.md §§7.3–7.5, 15.1, 31 | REQ-BUS-032, 033 | SECURITY-STANDARDS.md §12 | Product, administration, frontend, backend |
| REQ-PRD-051 | PRODUCT.md §§16.7–16.8, 32 | REQ-BUS-039, 041, 050 | SECURITY-STANDARDS.md §35 | Product, administration, security, operations |
| REQ-PRD-052 | PRODUCT.md §§22, 26, 35 | REQ-BUS-047, 048 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Product, Contracts, frontend, backend |

Future Accepted Decisions must be linked rather than copied. A change to Product truth requires synchronized governing updates before this Specification may rely on it.

## 38. Open Product Decisions

The following current `PRODUCT.md` concerns remain unresolved and are not decided here:

| Concern | Product-domain boundary |
| --- | --- |
| Initial Categories and catalogue taxonomy | Category owns taxonomy; Product membership and visibility must remain compatible with the future decision. |
| Product Variant and Attribute standards | Exact size, fit, color, material, and other standards remain Product-governed. |
| Product-review support | Reviews are not added to Product ownership or Version 1 behavior by this Specification. |
| Low-stock and out-of-stock Customer messaging | Inventory owns availability; Product content must not invent availability messaging. |
| Content approval and scheduled publication | No approval chain, schedule, or workflow is selected. |
| Product data import, export, and migration | No format, mechanism, source, schedule, or migration policy is selected. |

The final brand identity may constrain Product content through Product and Design System governance but is not a Product-domain lifecycle decision here. No `DEC-####` identifier is fabricated.

## 39. Risks

| Risk | Required treatment |
| --- | --- |
| Product Variants are semantically duplicated | Govern variant-defining Attributes and reject equivalent configurations. |
| Stale or misleading content is published | Require completeness, authorization, traceability, correction, and unpublication. |
| Product Media misrepresents Product | Enforce association, accuracy, publication, accessibility, and correction Requirements. |
| Product absorbs Price or Inventory authority | Preserve explicit Domain boundaries and revalidate through owning Contracts. |
| Product lifecycle rewrites commercial history | Preserve stable identity, confirmed Order snapshots, and historical references. |
| Search drift exposes stale or ineligible Product | Treat search as a reconcilable non-authoritative Projection. |
| Administrative workflow bypasses Product rules | Enforce server-side Authorization and Product invariants. |
| Unresolved policy becomes implementation behavior | Keep Open Product Decisions explicit and require applicable governance. |

No numerical Risk score or tolerance is established here.

## 40. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/API.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/EVENTS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `specifications/business/business-requirements.md`

## 41. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the Product Domain Specification after final governance, Product authority, Product Variant, Attribute, Product Media, publication, sellability, lifecycle, historical integrity, domain boundaries, security, accessibility, performance, Acceptance Criteria, and traceability validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Product Domain Specification covering Product, Product Variant, Product Media, publication, sellability, lifecycle, boundaries, Acceptance Criteria, and traceability. |

## 42. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version `1.0.0` Approved with `authoritative: false`;
2. the scope code remains `PRD`, Requirement identifiers remain unique and sequential, and retired identifiers are not reused;
3. Product and Product Variant terminology and the canonical Draft, Published, and Archived states remain aligned with `GLOSSARY.md`;
4. Product owns descriptive and lifecycle truth without absorbing Category, Pricing, Inventory, Customer, Cart, Checkout, Order, Payment, Shipping, or administration authority;
5. Product-owned sellability remains distinct from final purchasability;
6. search remains a non-authoritative Projection and cannot publish Product or prove Price, Inventory, or purchase truth;
7. publication, unpublication, archival, historical integrity, deletion, Product Media, Attribute, validation, Authorization, security, accessibility, audit, and failure Requirements remain testable;
8. every material Requirement retains observable Acceptance Criteria and one-to-one traceability;
9. Open Product Decisions remain unresolved unless Accepted governance and synchronized source updates exist;
10. no Endpoint, method, status code, schema, database structure, Angular Component, Spring class, provider integration, cloud resource, implementation technology, or unsupported numerical target is introduced;
11. Related Documents exist, contain no self-reference, and remain relevant;
12. headings remain sequential and unique, sections remain non-empty, tables remain valid, and no TODO, TBD, FIXME, placeholder, or actual ellipsis remains; and
13. only `specifications/domains/product/product-domain.md` changes for a scoped update.
