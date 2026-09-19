# ADR-0009 — Post-Category Backend Specification Selection

## Identifier

ADR-0009

## Title

Post-Category Backend Specification Selection

## Version

0.1.0

## Status

Proposed

## Date

2026-09-19

## Last Updated

2026-09-19

## Owner

Architecture

## Authoritative

false

## Context

Accepted ADR-0001 through ADR-0008 and synchronized `ARCHITECTURE.md` §35 establish `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT`. BCAT is Approved. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCAT remains unresolved.

A targeted post-BCAT audit found exactly three independently eligible candidates: Search and Discovery, Checkout, and CMS. No canonical source uniquely orders them, so explicit Architecture governance is required for the immediate position.

Search was previously blocked by the absence of a governed Category backend boundary. Approved BCAT now supplies governed Category taxonomy, hierarchy, classification, membership, navigation-eligibility, ordering, and other Category-owned evidence. BPRD supplies Product and Product Variant evidence; BPRC and BINV supply governed Pricing and Inventory evidence where applicable. Search can now be specified without acquiring source authority or resolving mechanisms.

Checkout remains independently eligible from BIDN, BCUS, BPRD, BINV, BPRC, BCART, and materially applicable BCAT evidence. CMS remains independently eligible from governed Product and Category reference authority. This Proposed ADR selects among these candidates without ranking or rejecting the alternatives.

## Decision Drivers

- The immediate post-BCAT position is not uniquely established by canonical governance.
- Approved BCAT closes the missing Category source boundary that previously prevented complete Search specialization.
- BPRD, BCAT, BPRC, and BINV provide the principal governed source evidence required by Search.
- Search indexes, documents, caches, Projections, rankings, and results can remain non-authoritative representations.
- Search policy and implementation decisions can remain explicit and unresolved.
- Checkout and CMS remain independently eligible and must receive no implied future order.
- The decision must establish one position only and preserve all post-BSRCH roadmap governance.

## Candidate Analysis

### Search and Discovery

Search and Discovery is independently eligible. Approved source backends provide Product, Category, Pricing, and Inventory evidence, while BIDN and BCUS provide governed actor, Customer, Consent, and Preference evidence where applicable. CMS evidence may remain a conditional source without requiring a CMS backend first.

Search implementation, extraction thresholds, provider, indexing, ranking, synchronization, facets, personalization, analytics, Redis, and messaging can remain unresolved and mechanism-neutral.

### Checkout

Checkout remains independently eligible. Its major upstream evidence is governed, and Approved Order, Payment, and Shipping and Fulfilment Domains preserve downstream authority without requiring their backend specifications first. ADR-0009 neither rejects Checkout nor establishes its title, scope, path, decomposition, Contract, or future position.

### CMS

CMS remains independently eligible. BPRD and BCAT provide governed Product and Category reference authority, and CMS-owned content behavior can be specified without Search or Administration backend specifications first. ADR-0009 neither rejects CMS nor establishes its title, scope, path, decomposition, Contract, or future position.

### Comparative Trade-off

All three candidates are defensible. Search and Discovery is selected because BCAT has just closed its identified Category-source governance gap, making a complete bounded Search specialization possible. This is an explicit governance selection, not a claim that Search is technically superior, more valuable, easier, cheaper, higher priority, or objectively preferable to Checkout or CMS.

## Decision

The eighth downstream Backend Specification after BEB, immediately after BCAT, SHALL be:

- **Capability:** Search and Discovery
- **Title:** Search and Discovery Backend Specification
- **Path:** `specifications/backend/search/search-backend.md`
- **Scope code:** `BSRCH`
- **Decomposition:** Search-and-Discovery-only; specializes exactly the Approved Search and Discovery Domain
- **Position:** Eighth downstream Backend Specification after BEB, immediately after BCAT

`BSRCH` means Backend Search and Discovery. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

BSRCH SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL specialize only the Approved Search and Discovery Domain and SHALL consume source evidence without acquiring source-Domain authority.

BSRCH MAY consume materially applicable governed evidence from BPRD, BCAT, BPRC, BINV, BIDN, BCUS, and other Approved sources where Search authority requires it. Product, Product Variant, Category, Pricing, Inventory, Identity, Customer, Account, Consent, and Preference authority SHALL remain with their owners. Search indexes, documents, caches, Projections, rankings, and results SHALL NOT become authoritative source truth.

This decision proposes only `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BSRCH remains unresolved.

Because ADR-0009 remains Proposed, BSRCH MUST NOT be drafted under Approved governance until this ADR is Accepted and synchronized canonical Architecture and Decision changes are merged. Acceptance would authorize a BSRCH Draft only; it would not approve BSRCH.

## Selected Candidate Rationale

Approved BCAT supplies the Category-owned evidence that Search requires but previously lacked. Together with Approved Product, Pricing, and Inventory backend authorities, Search now has sufficient governed source boundaries for a complete mechanism-neutral specialization.

The selection resolves only the immediate ambiguity. Checkout and CMS remain independently eligible and unresolved, and no post-BSRCH order is inferred.

## Source Authority Boundaries

- **BEB:** BSRCH must inherit and trace every materially applicable shared backend obligation.
- **BIDN:** trusted Principal and Authentication evidence may support protected Search behavior; Identity, Sessions, tokens, and Authentication authority do not transfer.
- **BCUS:** governed Customer, Account, Consent, Preference, and contextual evidence may be consumed where Approved Search policy permits; Customer authority does not transfer.
- **BPRD:** Product/Product Variant identity, lifecycle, eligibility, Attributes, content, and Product Media evidence may be represented; Product authority does not transfer.
- **BINV:** availability evidence may be represented as non-authoritative and stale-capable; Stock, Available-to-Sell, reservations, and Inventory authority do not transfer.
- **BPRC:** commercial evidence may be represented as non-authoritative and revalidatable; Price, Discount, Promotion, Voucher, tax, and Pricing authority do not transfer.
- **BCART:** Cart intent remains separate and is not Search truth.
- **BCAT:** Category identity, taxonomy, hierarchy, classification, membership, lifecycle, navigation eligibility, ordering, content, and metadata remain Category-owned.
- **CMS:** CMS-owned content evidence may be consumed where governed; publication, scheduling, approval, provider, and CMS authority do not transfer.
- **Administration:** administrative Search may invoke Search-owned Use Cases through contextual Authorization; Search defines no repository-wide Roles, Permissions, mappings, or access matrix.
- **Checkout:** Checkout remains independently eligible, separate, and unresolved; Search evidence does not prove purchasability or Checkout readiness.

## Dependency and Authority Analysis

| Capability | Relationship to future BSRCH | Backend prerequisite | Authority remaining outside Search |
| --- | --- | --- | --- |
| BEB | Inherited shared obligations | Approved | Shared backend governance |
| BIDN | Conditional trusted actor evidence | Approved | Identity, Principal, Authentication, Sessions, tokens |
| BCUS | Conditional Customer/Consent/Preference evidence | Approved | Customer, Account, Consent, Preference |
| BPRD | Product/Product Variant source evidence | Approved | Product identity, lifecycle, Attributes, catalogue, content/media |
| BINV | Inventory representation evidence | Approved | Stock, Available-to-Sell, reservations, Inventory truth |
| BPRC | Pricing representation evidence | Approved | Price and commercial truth |
| BCART | Boundary only; no required source authority | Approved | Cart intent and lifecycle |
| BCAT | Category source evidence | Approved | Taxonomy, hierarchy, classification, membership, lifecycle, navigation, ordering |
| CMS | Conditional content source | No prior CMS backend required | CMS workflow, publication, scheduling, provider, CMS truth |
| Administration | Consumer of protected Search Use Cases | No prior Administration backend required | Administrative coordination and repository-wide access policy |
| Checkout | Separate eligible candidate and downstream revalidator | No prerequisite to BSRCH | Checkout orchestration, commitment, purchasability |

## CMS Boundary

CMS remains independently eligible and separate. BSRCH may consume CMS-owned evidence only where Approved authority permits. ADR-0009 establishes no CMS Backend Specification, workflow, scheduling, provider, Contract, scope, path, decomposition, or future position.

## Administration Boundary

Administration remains separate. Future administrative Search behavior may invoke Search-owned Use Cases through contextual Authorization, but BSRCH must not define repository-wide Roles, Permissions, role mappings, or an administrative access matrix.

## Checkout Boundary

Checkout remains independently eligible and unresolved. ADR-0009 establishes no Checkout Backend Specification, title, scope code, canonical path, Contract, decomposition, or roadmap position and does not imply that Checkout has lost eligibility.

## Post-BSRCH Roadmap Boundary

No Backend Specification after BSRCH is selected. Checkout and CMS remain independently eligible and unresolved, no ordering between them is established, neither is implied to follow BSRCH, and no other candidate receives a future position. Another governance decision is required before establishing the next position unless canonical governance later determines it uniquely.

## Alternatives Considered

### A. Checkout

Deferred, not rejected or ranked lower. Checkout remains independently eligible from governed upstream evidence, while Approved downstream Domains preserve Order, Payment, and Shipping and Fulfilment authority. Its future backend identity and order remain unresolved.

### B. CMS

Deferred, not rejected or ranked lower. CMS remains independently eligible from BPRD and BCAT reference authority and does not require Search or Administration backend specifications first. Its future backend identity and order remain unresolved.

### C. Defer the Roadmap Decision

Not selected because it would preserve the immediate ambiguity that ADR-0009 exists to resolve. This does not establish any position beyond BSRCH.

## Explicit Non-Decisions

ADR-0009 does not select or define detailed BSRCH Requirements; Elasticsearch, OpenSearch, PostgreSQL full-text search, Azure AI Search, Algolia, or another provider; Redis; a broker; indexing topology; Search document schema; ranking algorithm or scoring formula; facet implementation; synchronization mechanism; extraction threshold; retry, timeout, retention, or numerical Search limit; API route; DTO; event schema; physical persistence design; CMS workflow/provider; administrative Role/Permission matrix; Checkout or CMS backend identity; or any post-BSRCH roadmap position.

## Open Product Decisions

This Proposed decision preserves the following **10 materially applicable Open Product Decisions** from `PRODUCT.md` §24:

| Item | Open Product Decision | Preserved BSRCH boundary |
| ---: | --- | --- |
| 2 | Initial product categories and catalogue taxonomy. | Search consumes governed Category criteria but defines no taxonomy. |
| 3 | Size, fit, colour, material, and other Product Variant or Attribute standards. | No filterable Attribute standard or Variant policy is selected. |
| 13 | Back-order and pre-order support. | Search does not infer availability policy or enable unavailable-item discovery behavior. |
| 15 | Product-review support. | No review evidence, ranking input, moderation, or review capability is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No availability wording, threshold, display, or eligibility rule is selected. |
| 19 | Marketing-consent and communication-preference model. | Personalization and measurement require governed Consent/Preference evidence; no model is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, taxonomy, or measurement mechanism is selected. |
| 22 | Content approval and scheduled-publication workflow. | CMS evidence eligibility remains governed externally; Search defines no workflow or schedule. |
| 23 | Administrative role and permission matrix. | Protected Search behavior requires contextual Authorization without defining Roles or Permissions. |
| 30 | Product launch date, release scope, and post-launch support window. | No release date, Search capability scope, support window, or rollout target is selected. |

Ranking weights/formulas, query normalization policy, synonym policy, facet selection, spelling behavior, personalization rules, freshness objectives, and detailed indexing/rebuild policy remain unresolved Search policy boundaries rather than additional numbered Product Decisions.

No listed Product Decision is resolved by selecting BSRCH.

## Open Architecture Decisions

This Proposed decision preserves the following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BSRCH boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting/topology selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC selected. |
| 3 | Customer and administrator session/token strategy. | No Session/token mechanism selected for protected or personalized Search. |
| 8 | Redis introduction and its approved use cases. | No Search cache or Redis use selected. |
| 9 | External messaging introduction and service selection. | Source synchronization/events remain conditional and no messaging service is selected. |
| 10 | Initial search implementation details and extraction thresholds. | No provider, engine, indexing, extraction, ranking, or synchronization mechanism is selected. |
| 11 | Product-media upload and transformation strategy. | Search may represent governed Product Media evidence but selects no upload, transformation, storage, or delivery mechanism. |
| 12 | Backup retention and production recovery objectives. | No retention period, backup mechanism, or numerical recovery objective selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Search persistence layout selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, rollout system, or lifecycle selected. |

No listed Architecture Decision is resolved by selecting BSRCH. Frontend hosting, Payment provider, Shipping provider, and transactional notification provider remain unresolved but are not materially required for this Search-only roadmap boundary.

## Consequences

Established consequences include:

- the immediate post-BCAT ambiguity is resolved if this ADR is Accepted and synchronized;
- BSRCH becomes the authorized next Backend Specification only after acceptance synchronization is merged;
- a future BSRCH Draft can specialize Search against governed Product, Category, Pricing, Inventory, Identity, Customer, and conditional CMS boundaries;
- every source Domain retains authority; and
- post-BSRCH ordering remains unresolved.

Trade-offs include:

- Checkout remains unresolved despite being independently eligible;
- CMS remains unresolved despite being independently eligible;
- another governance decision may be required after BSRCH; and
- Search policy and mechanism decisions remain open.

These trade-offs are governance consequences, not deficiencies or candidate rankings.

## Required Governance Reviews

This Proposed decision becomes Accepted only after:

- Architecture review of the immediate post-BCAT selection, Search-only decomposition, title, path, and scope code;
- affected Search and Discovery ownership review confirming accurate Domain specialization;
- affected Product and Product Catalogue ownership review confirming bounded BPRD consumption and preserved Product/Product Variant authority;
- affected Category ownership review confirming bounded BCAT consumption and preserved Category authority;
- affected Pricing ownership review confirming bounded BPRC evidence and preserved Pricing authority;
- affected Inventory ownership review confirming bounded BINV evidence and preserved Inventory authority;
- affected CMS ownership review confirming CMS remains independently eligible, separate, and unordered after BSRCH;
- affected Administration ownership review confirming administrative Search transfers no Search authority and defines no Role/Permission matrix;
- affected Checkout ownership review confirming Checkout remains independently eligible, separate, and unordered after BSRCH;
- affected Identity ownership review confirming bounded BIDN evidence and no Authentication/Session/token mechanism;
- affected Customer ownership review confirming bounded BCUS, Consent, Preference, and contextual evidence without Customer authority transfer;
- confirmation that `BSRCH` is unique among current Specification scope codes;
- confirmation that `specifications/backend/search/search-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BSRCH is established; and
- preparation of synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` as part of acceptance.

No review is represented as completed, and no reviewer names, signatures, tickets, dates beyond this record's date, or external evidence are asserted.

## Acceptance Conditions

ADR-0009 may become Accepted only when:

- governance approves BSRCH as the immediate Backend Specification after BCAT;
- review confirms BSRCH remains Search-and-Discovery-only and preserves all source authority;
- review confirms Checkout and CMS remain independently eligible and unresolved with no ordering between them;
- review confirms every post-BSRCH roadmap identity and position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized through BSRCH;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0009 as Accepted; and
- ADR-0009 lifecycle wording records the Accepted decision consistently.

No `PRODUCT.md` change is required unless acceptance review discovers a direct contradiction. BSRCH MUST NOT be drafted under Approved governance until Accepted ADR-0009 and synchronized canonical changes are merged. Acceptance authorizes drafting only; it does not approve BSRCH.

## Security, Data, Compatibility, and Operational Impact

This Proposed decision creates no runtime behavior, API, schema, document model, index, migration, event, provider, cache, infrastructure, deployment, configuration, or operational target. A future BSRCH Specification must inherit materially applicable BEB security, Authorization, privacy, compatibility, migration, boundedness, workload isolation, observability, audit, failure, recovery, reconciliation, and verification obligations without selecting unresolved mechanisms.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0008-post-cart-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/admin/admin-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-19 | Proposed | Proposed Search and Discovery as the immediate post-BCAT Backend Specification while preserving Checkout and CMS as independently eligible and unresolved. |
