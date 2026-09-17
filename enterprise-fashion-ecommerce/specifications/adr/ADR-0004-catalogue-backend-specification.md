# ADR-0004 — Product Catalogue Backend Specification Selection

## Identifier

ADR-0004

## Title

Product Catalogue Backend Specification Selection

## Version

1.0.0

## Status

Accepted

## Date

2026-09-17

## Last Updated

2026-09-17

## Owner

Architecture

## Authoritative

false

## Context

Accepted ADR-0001 established the Shared Backend Baseline Specification under scope `BEB`. Accepted ADR-0002 established the Identity and Access Backend Specification under scope `BIDN` as the first downstream Backend Specification after BEB. Accepted ADR-0003 established the Customer and Account Backend Specification under scope `BCUS` as the second downstream Backend Specification after BEB, immediately after BIDN. BEB, BIDN, and BCUS now exist as Approved `1.0.0` Specifications.

ADR-0003 and `ARCHITECTURE.md` §35 deliberately leave every Backend Specification title, path, scope code, decomposition, and ordering position after BCUS unresolved. A further Architecture Decision is therefore required before another downstream Backend Specification may be drafted under Approved governance.

The Approved Product Domain owns stable Product and Product Variant identity, Product-owned Attributes, Product Media associations and metadata, publication state, catalogue visibility eligibility, and Product-owned structural sellability. It is upstream of governed Pricing and Inventory association, Search and Discovery representation, Cart intent, Checkout revalidation, Order snapshots, reporting, and Product administration.

The Approved Category Domain separately owns Category identity, taxonomy, hierarchy, classification, navigation eligibility, and Category-side Product membership policy. The Product Domain explicitly excludes Category hierarchy and classification authority, while the Category Domain preserves Product authority. The Approved Search and Discovery Domain separately owns search request and result semantics and consumes Product, Category, Pricing, and Inventory evidence without becoming authoritative for those facts.

Repository evidence therefore supports a Product-only backend specialization as the minimum defensible next boundary. Combining Category or Search and Discovery with Product would merge separately governed Domain authority without an established requirement to do so. Acceptance and canonical governance synchronization now authorize drafting the downstream Specification under Approved governance.

## Decision Drivers

- Product and Product Variant identity are foundational references for Pricing, Inventory, Search and Discovery, Cart, Checkout, Order, Administration, and Reporting.
- Product owns descriptive catalogue meaning, Product Media association, publication, visibility eligibility, and structural sellability without owning Category, Price, Stock, or final purchasability.
- Product and Category are separate Approved Domains with reciprocal authority boundaries.
- Search and Discovery is a separate Approved Domain and a non-authoritative consumer of Product, Category, Pricing, and Inventory evidence.
- The next boundary must inherit BEB and preserve applicable BIDN and BCUS authority.
- The decision must not settle unresolved Product policy, implementation mechanisms, or any later backend ordering.
- The selected boundary should be no broader than current repository evidence requires.

## Decision

The third downstream Backend Specification after the Shared Backend Baseline, immediately after BIDN and BCUS, SHALL be:

- **Title:** Product Catalogue Backend Specification
- **Path:** `specifications/backend/product/product-backend.md`
- **Scope code:** `BPRD`
- **Position:** Third downstream Backend Specification after BEB, immediately after BIDN and BCUS

`BPRD` means Backend Product. It is unique among current Specification scope codes and is introduced only for the Product Catalogue Backend Specification. It does not reserve or imply a scope-code convention for later Backend Specifications.

BPRD SHALL specialize only the Approved Product Domain. It SHALL NOT absorb Category Domain, Search and Discovery Domain, Pricing Domain, Inventory Domain, or another Domain's authority. The title uses “Product Catalogue” to identify the Product-owned descriptive catalogue capability; it does not make all catalogue-adjacent behavior part of Product authority.

Where already governed by the Approved Product Domain, BPRD MAY provide bounded backend specialization for:

- Product and Product Variant identity and association;
- Product-owned Attributes;
- Product Media association and metadata, without selecting media infrastructure;
- Product-owned lifecycle, publication, visibility eligibility, and structural sellability;
- Product-owned content, historical integrity, failure, recovery, and correction outcomes;
- Product-owned Use Case orchestration;
- Product-specific Contract obligations; and
- Product-specific persistence, concurrency, conditional event, integration, observability, audit, and verification obligations.

BPRD SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL consume materially applicable BIDN Contracts or trusted Identity evidence without transferring Identity authority. It SHALL consume materially applicable BCUS Contracts or governed Customer and Account context without transferring Customer or Account authority.

Contextual Authorization SHALL remain with the Domain owning the affected Resource, action, property, association, and current state. Authentication, Identity evidence, Customer context, or Account association alone SHALL NOT establish Product authorization or another Domain's authorization.

This decision does not establish Category or Search and Discovery backend decomposition. Every Backend Specification title, path, scope code, decomposition, and ordering position after BPRD remains unresolved until separately governed.

As an Accepted decision with synchronized canonical governance, this ADR authorizes drafting BPRD as an Approved-governance downstream Specification; it does not itself approve the future BPRD Specification.

## Authority Boundary

BPRD would remain subordinate to governing sources, Approved Business Requirements, the Approved Product Domain, BEB, and materially applicable BIDN and BCUS Contracts and evidence. It would not become repository-wide authority.

Product Domain authority would remain limited to Product-owned identity, descriptive content, Product Variant definition, Product-owned Attributes, Product Media association and metadata, publication, visibility eligibility, structural sellability, and Product-owned history and outcomes.

Category would retain authority for Category identity, taxonomy, hierarchy, classification, navigation eligibility, and Category-side Product membership policy. Search and Discovery would retain authority for search requests, result semantics, indexing outcomes, ranking within governed bounds, reconciliation, and search-owned operational truth while remaining non-authoritative for Product facts.

Pricing would retain Price, Discount, Promotion, Voucher, Money, Currency, tax-calculation, and Pricing Rule authority. Inventory would retain Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, and overselling-protection authority. Cart would retain shopping intent; Checkout would retain purchase orchestration; Administration would retain Staff workflow coordination; Order, Payment, Shipping and Fulfilment, Return and Refund, Notifications, Reporting and Analytics, CMS, and every other Domain would retain its governed truth.

BPRD would consume only the minimum governed cross-Domain context needed for Product-owned behavior. It would not infer final purchasability, Price, Stock, Customer eligibility, business Authorization, Payment, Order, Shipment, or another Domain outcome from Product state.

## Consequences

Positive consequences include:

- a narrowly governed proposal for the third downstream backend specialization after BEB;
- a Product-aligned backend boundary before downstream Pricing, Inventory, Search and Discovery, Cart, and Checkout specializations that consume Product references or evidence;
- preservation of separate Product, Category, and Search and Discovery authority;
- explicit BEB inheritance and bounded BIDN and BCUS consumption;
- a stable place to specify Product-owned backend obligations without selecting mechanisms; and
- continued independent governance of every later backend roadmap decision.

Costs and trade-offs include:

- Category backend specialization remains separately unresolved even though Product and Category collaborate in catalogue experiences;
- Search and Discovery backend specialization remains separately unresolved;
- downstream consumers must use governed Contracts without collapsing Product, Category, Pricing, Inventory, or Search authority;
- unresolved Product policy and technical mechanisms remain deferred; and
- a later Accepted decomposition change may require compatibility and migration planning.

## Alternatives Considered

### A. Product Alone as the Product Catalogue Backend Boundary

Selected because the Approved Product Domain already owns a coherent Product-specific catalogue capability and explicitly excludes Category hierarchy and classification. This is the minimum boundary supported by current authority and dependency evidence.

### B. Product and Category as One Catalogue Backend Boundary

Not selected because Product and Category are separate Approved Domains with distinct ownership, lifecycle, integrity, and Authorization requirements. Their collaboration does not by itself justify one backend Specification or transfer Category authority into Product. A later decision may govern Category backend placement without changing this Product-only boundary.

### C. Product, Category, and Search and Discovery as One Catalogue Backend Boundary

Not selected because Search and Discovery is a separate Approved Domain with its own request, result, ranking, indexing, recovery, reconciliation, security, and operational semantics. Search consumes Product and Category evidence but does not own their truth, and no governing source requires their backend decomposition to be combined.

### D. Category Before Product

Not selected because Product and Product Variant identity are direct foundational inputs to Pricing, Inventory, Cart, Checkout, Order snapshots, and Search, while Category is primarily taxonomy, classification, navigation, and membership authority. This does not determine Category's later backend position.

### E. Pricing or Inventory Immediately After BCUS

Not selected because each requires governed Product or Product Variant association. Establishing the Product backend boundary first provides the narrower common upstream dependency without deciding the relative order of Pricing and Inventory.

### F. Cart or Checkout Immediately After BCUS

Not selected because Cart and Checkout additionally depend on Product, Pricing, Inventory, and, for Checkout, Shipping, Payment, and Order evidence. BCUS satisfies their Customer-side dependency but not those broader backend dependencies.

### G. Leave the Next Backend Specification Unresolved

Not selected because Approved Product, Business, Architecture, and Domain evidence distinguishes the Product-owned catalogue capability as a bounded common upstream dependency. The proposed selection still requires governance approval and leaves every later position unresolved.

## Explicit Exclusions

This decision does not decide, and does not authorize BPRD to decide:

- Product taxonomy, initial Categories, Category hierarchy, classification, membership cardinality, primary-Category behavior, or navigation policy;
- Product Variant or Attribute standards such as size, fit, colour, material, or another taxonomy;
- Product-review support or review ownership;
- content approval, approval chains, scheduled publication, or publication workflow policy;
- Product import, export, migration source, format, schedule, or reconciliation policy;
- Product-media upload, transformation, storage, delivery, provider, pipeline, or infrastructure;
- Search and Discovery indexing, provider, engine, extraction threshold, ranking implementation, schema, or topology;
- concrete API routes, HTTP operations, methods, statuses, DTOs, or payload schemas;
- database schemas, tables, columns, SQL, ORM mappings, indexes, constraints, or migration mechanics;
- event names, schemas, payloads, topics, consumers, brokers, delivery technology, or choreography;
- providers, hosting, infrastructure, deployment, cache, queue, messaging, or network topology;
- identifier formats or generation mechanisms;
- numerical limits, thresholds, timeouts, retries, retention periods, performance targets, SLAs, or SLOs;
- Pricing, Inventory, Cart, Checkout, Payment, Shipping, Fulfilment, tax, Promotion, Voucher, fraud, Customer, or market policy;
- a concrete Role or Permission matrix;
- Category or Search and Discovery backend title, path, scope code, decomposition, or ordering; or
- any Backend Specification title, path, scope code, decomposition, or ordering position after BPRD.

## Open Decisions Preserved

This decision leaves applicable Open Product Decisions unresolved, including:

- final brand name and visual identity;
- initial Product Categories and catalogue taxonomy;
- size, fit, colour, material, and other Product Variant or Attribute standards;
- Product-review support;
- low-stock and out-of-stock Customer messaging;
- content approval and scheduled-publication workflow;
- Product data import, export, and migration requirements;
- initial analytics provider and event taxonomy;
- administrative Role and Permission matrix; and
- Product launch date, release scope, and post-launch support window.

It also leaves applicable Open Architecture Decisions unresolved, including backend hosting, infrastructure as code, customer and administrator session/token strategy, Redis introduction and its approved use cases, external messaging, initial Search implementation and extraction thresholds, Product-media upload and transformation, backup retention and production recovery objectives, PostgreSQL schema strategy, and repository-wide feature-flag implementation and lifecycle management.

An Open Decision is not permission for BPRD or an implementation to choose independently. No decision listed here is answered by the selected Product-only boundary.

## Acceptance and Governance Conditions

This decision was Accepted after:

- Architecture review;
- affected Product ownership review;
- affected Category ownership review confirming preservation of Category authority and the Product-only decomposition;
- affected Search and Discovery ownership review confirming preservation of Search authority and unresolved Search backend decomposition;
- affected Identity and Customer ownership review confirming bounded BIDN and BCUS consumption;
- confirmation that `BPRD` is unique among current Specification scope codes;
- confirmation that `specifications/backend/product/product-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that all backend titles, paths, scope codes, decompositions, and ordering after BPRD remain unresolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md`.

ADR-0004 is Accepted and canonical synchronization is complete, so the Product Catalogue Backend Specification may now be drafted under Approved governance.

Acceptance synchronized governance through:

- ADR-0004: changed status from `Proposed` to `Accepted` and updated lifecycle-dependent wording without changing decision semantics;
- `ARCHITECTURE.md` §35: established BPRD as the third downstream Backend Specification after BEB, immediately after BIDN and BCUS, while preserving every later roadmap position as unresolved, and synchronized metadata, Document Status, and Revision History; and
- `DECISIONS.md`: recorded ADR-0004 as Accepted in the Decision Index and synchronized metadata and Revision History.

No `PRODUCT.md` change is required because this decision preserves Product authority and leaves applicable Product Decisions unresolved. If acceptance review identifies a direct Product contradiction, acceptance must stop until the contradiction is governed; this ADR does not authorize editing or overriding `PRODUCT.md`.

## Security Impact

This decision introduces no security policy or mechanism. A future BPRD Specification would consume trusted BIDN evidence where materially applicable, preserve Product-owned contextual Authorization, consume BCUS context only where governed, protect Product administration and Sensitive Data, and leave Identity mechanisms and access matrices unresolved.

## Data Impact

This decision creates no schema, table, index, SQL, persistence mapping, identifier design, storage mechanism, migration, import/export format, Search index, media store, retention rule, or data movement. Product and Category data authority remain with their Approved Domains.

## Compatibility and Migration Impact

This decision creates no runtime, data, API, event, Search, media, or Contract migration. A future BPRD Specification would inherit materially applicable BEB compatibility obligations. Concrete versions, compatibility windows, migrations, rollback, and recovery mechanisms require later governed Contracts or designs.

## Operational Impact

This decision creates no runtime operational behavior, provider commitment, infrastructure topology, deployment design, support process, capacity target, threshold, timeout, retry count, SLA, or SLO. A future BPRD Specification may specialize governed Product observability, recovery, reconciliation, and audit outcomes without selecting unresolved mechanisms or numerical values.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0002-identity-access-backend-specification.md`
- `specifications/adr/ADR-0003-customer-account-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`

## Supersedes

—

## Superseded By

—

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Proposed | Proposed the Product-only Product Catalogue Backend Specification as the third downstream Backend Specification after BEB, immediately after BIDN and BCUS, while preserving Category, Search and Discovery, and all later backend roadmap decisions. |
| 1.0.0 | 2026-09-17 | Accepted | Accepted the Product-only Product Catalogue Backend Specification as the third downstream Backend Specification after BEB, immediately after BIDN and BCUS, while preserving Category, Search and Discovery, and all later backend roadmap decisions. |
