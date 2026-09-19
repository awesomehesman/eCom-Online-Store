# ADR-0008 — Post-Cart Backend Specification Selection

## Identifier

ADR-0008

## Title

Post-Cart Backend Specification Selection

## Version

1.0.0

## Status

Accepted

## Date

2026-09-19

## Last Updated

2026-09-19

## Owner

Architecture

## Authoritative

false

## Context

Accepted ADR-0001 through ADR-0007 and synchronized `ARCHITECTURE.md` §35 establish the Approved backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART`. BCART is Approved `1.0.0`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCART remains unresolved.

A post-BCART roadmap audit found no already-governed next capability and identified exactly two independently eligible candidates: Category and Checkout. No Accepted ADR or Approved canonical source orders either candidate after BCART, so a further Architecture Decision is required.

Category is independently eligible. BPRD supplies governed Product and Product Variant evidence, while BPRC and BINV remain separate Pricing and Inventory authorities. Category can preserve unresolved taxonomy, hierarchy, lifecycle, membership, classification, navigation, content-workflow, and import policy without selecting mechanisms. A governed Category backend boundary is the identified missing authoritative source for Search taxonomy, hierarchy, membership, classification, and navigation evidence.

Checkout is also independently eligible after Approved BCART. BIDN, BCUS, BPRD, BINV, BPRC, and BCART supply its major governed upstream evidence. Approved Order, Payment, and Shipping and Fulfilment Domains define downstream authority boundaries without requiring those backend implementations first. Provider, delivery, reservation-duration, promotion, tax, fraud, guest, and downstream Contract details can remain unresolved.

This Accepted ADR resolves only the immediate Category-versus-Checkout ambiguity. Synchronized canonical Architecture and Decision governance establishes BCAT as the next Backend Specification after BCART. BCAT drafting under Approved governance is authorized only after these synchronized changes are merged.

## Decision Drivers

- Category and Checkout are both independently eligible after BCART, so immediate ordering requires explicit governance rather than inference.
- BPRD already supplies governed Product and Product Variant identity and association evidence required by Category.
- BINV and BPRC establish separate Inventory and Pricing authority without becoming prerequisites for Category-owned truth.
- Category supplies the authoritative taxonomy, hierarchy, membership, classification, and navigation evidence that Search requires.
- Search is not independently eligible while its governed Category backend source boundary is absent.
- A Category-only boundary can preserve unresolved policy and implementation choices without absorbing Search, CMS, Administration, or another Domain.
- Checkout remains independently eligible and must remain unresolved rather than being rejected or silently assigned a later position.
- The selected boundary must close only the identified Category governance gap and establish no post-BCAT roadmap position.

## Candidate Analysis

### Category

Category has no blocking backend prerequisite. Product and Product Variant authority is available through BPRD, while BINV and BPRC provide bounded evidence where Category representations intersect Inventory or Pricing. Its Approved Domain defines coherent authority over taxonomy, hierarchy, classification, Category-side Product membership policy, lifecycle, navigation eligibility, ordering, and Category-owned content.

Category-specific unresolved policy can remain explicit and mechanism-neutral. Selecting Category establishes the missing upstream backend boundary needed before Search can be independently reassessed; it does not select Search as the following backend.

### Checkout

Checkout has no uniquely blocking prerequisite after Approved BCART. Existing Approved backends provide its Identity, Customer, Product, Inventory, Pricing, and Cart evidence, and Approved downstream Domains define authority boundaries for Order, Payment, and Shipping and Fulfilment.

Checkout remains independently eligible. Deferring its immediate position is not a rejection, permanent deprioritization, or placement after Search or another capability. This ADR establishes no Checkout backend identity, title, path, scope code, decomposition, or future order.

### Comparative Trade-off

Both candidates are defensible and independently eligible. Category is selected because it closes a presently identified upstream governance gap: Search requires governed Category evidence and no Category backend boundary currently exists. BCAT makes Search independently assessable in a later roadmap decision while preserving Search as a separate authority.

This is a dependency and governance judgment, not a claim that Category is technically superior, easier, cheaper, faster, or more important than Checkout. It sets only the single immediate roadmap position.

## Decision

The seventh downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, BINV, BPRC, and BCART, SHALL be:

- **Capability:** Category
- **Title:** Category Backend Specification
- **Path:** `specifications/backend/category/category-backend.md`
- **Scope code:** `BCAT`
- **Decomposition:** Category-only; specializes exactly the Approved Category Domain
- **Position:** Seventh downstream Backend Specification after BEB, immediately after BCART

`BCAT` means Backend Category. It is collision-free among current Specification scope codes and is assigned only to the Category Backend Specification. It establishes no naming rule for later scopes.

BCAT SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL specialize only the Approved Category Domain and SHALL preserve Category authority over taxonomy, hierarchy, membership, classification, navigation, ordering, lifecycle, and other Category-owned behavior without extending beyond the Approved Domain.

BCAT MAY consume materially applicable governed Contracts or evidence from BPRD and other existing Approved upstream backends where required, without transferring their authority. It SHALL NOT absorb Product, Product Variant, Pricing, Inventory, Search and Discovery, CMS, Administration, Cart, Checkout, Order, Payment, Shipping and Fulfilment, or another Domain's authority.

Contextual Authorization SHALL remain with the Domain owning the affected Resource, action, property, association, and current state. Identity evidence, Role labels, Claims, UI state, or supplied identifiers alone SHALL NOT grant Category authority.

This decision establishes only `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT`. BCAT MUST be Approved before another downstream Backend Specification is introduced under this roadmap. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCAT remains unresolved.

ADR-0008 is Accepted and canonical Architecture and Decision governance is synchronized by this change. BCAT MUST NOT be drafted under Approved governance until these synchronized changes are merged canonically. Acceptance authorizes a BCAT Draft only; it does not approve BCAT, which must complete its own Draft-to-Approved lifecycle.

## Selected Candidate Rationale

Category is selected because the repository now has every material upstream authority required for a bounded Category specialization, while Search remains specifically underdetermined without a governed Category backend source. Establishing BCAT closes that gap by providing a future specialization point for Category-owned taxonomy, hierarchy, membership, classification, and navigation evidence.

Selecting BCAT does not predetermine the next step. Search still requires its own eligibility and governance assessment, and Checkout remains independently eligible and unresolved. No implementation priority beyond the immediate BCAT roadmap position is established.

## Search Boundary

- Search and Discovery remains a separate Backend Specification candidate.
- BCAT does not absorb Search authority or behavior.
- BCAT may later expose governed Category-owned evidence required by Search through separately governed Contracts.
- Search ranking, indexing, query behavior, filtering, facets, provider choice, projections, synchronization, and implementation remain Search-owned or unresolved as applicable.
- Selecting BCAT does not select Search as the Backend Specification after BCAT.

## Checkout Boundary

- Checkout remains independently eligible after Approved BCART.
- ADR-0008 does not reject, resolve, or permanently deprioritize Checkout.
- Checkout remains a separate Backend Specification candidate.
- ADR-0008 establishes no Checkout title, path, scope code, decomposition, Contract, or future roadmap position.
- Checkout is not silently placed immediately after BCAT, after Search, or after any other capability.

## Authority and Dependency Boundaries

- **BEB:** every materially applicable shared backend obligation must be inherited and explicitly traced rather than duplicated or weakened.
- **BIDN:** trusted Identity evidence may support protected Category behavior; Identity authority and mechanisms do not transfer.
- **BCUS:** governed Customer or market context may be consumed where Category policy permits; Customer and Account authority does not transfer.
- **BPRD:** Product and Product Variant identity and association evidence may support Category membership integrity; Product authority does not transfer.
- **BINV:** Inventory evidence may be represented where separately governed; Category does not acquire Stock, availability, reservation, or purchase-eligibility authority.
- **BPRC:** Pricing evidence may be represented where separately governed; Category does not acquire Price, Discount, Promotion, tax, or commercial-calculation authority.
- **BCART:** Cart remains a separate provisional-intent authority and transfers no authority to Category.
- **Search and Discovery:** Search remains separate and non-authoritative for Category truth.
- **CMS:** CMS content and publication authority remains separate; Category-owned content and navigation meaning remain Category-owned.
- **Administration:** administrative actions must invoke Category-owned capabilities through governed Authorization and Contracts; Administration gains no Category authority.
- **Checkout, Order, Payment, Shipping and Fulfilment, and other Domains:** each retains its Approved authority and receives no later roadmap position from this ADR.

## Consequences

Positive consequences include:

- Category receives a bounded backend specialization point;
- Search's missing Category source boundary can be governed before Search is reconsidered;
- Product, Product Variant, Pricing, Inventory, Cart, Search, CMS, Administration, and other Domain authorities remain preserved;
- Checkout remains independently eligible without being prematurely ordered; and
- post-BCAT sequencing remains explicitly governed rather than inferred.

Costs and trade-offs include:

- Checkout remains unresolved despite being independently eligible;
- another roadmap decision may still be required after BCAT; and
- Search remains separate and must undergo its own eligibility and governance assessment.

These trade-offs do not reject or diminish Checkout or Search and establish no later sequence.

## Rejected and Deferred Alternatives

### A. Checkout Next

Deferred, not rejected. Checkout is independently eligible after Approved BCART and remains a valid candidate for a later separately governed position. It is not selected for this immediate position because BCAT closes the currently identified Category-source gap that prevents Search from independent reassessment. No relative technical superiority or later Checkout position is asserted.

### B. Combine Category and Search

Rejected because Category and Search and Discovery are separate Approved Domains with distinct authority. Category owns taxonomy, hierarchy, classification, membership, navigation eligibility, ordering, lifecycle, and Category content; Search owns query, result, ranking, indexing, and discovery behavior. No governing source requires a combined backend boundary.

### C. Search Immediately After BCAT

Not decided. BCAT removes one identified Search prerequisite, but Search must be reassessed against its complete governing dependencies and receive separate roadmap governance. This ADR does not establish `BCAT → Search`.

### D. Leave the Immediate Position Unresolved

Not selected because the completed audit identified two eligible candidates and repository evidence supports the bounded BCAT selection as a way to close Search's missing upstream Category boundary. Accepted ADR-0008 and synchronized canonical governance establish that selection.

## Explicit Non-Decisions

ADR-0008 does not select or define:

- detailed BCAT Requirements or Acceptance Criteria;
- Category taxonomy structure, roots, names, hierarchy depth, lifecycle states, or state machine;
- primary versus multiple Category membership, membership cardinality, automatic classification, or reclassification policy;
- Category ordering, navigation policy, visibility presentation, or customer/market segmentation policy;
- Category import, export, migration, reconciliation, or destructive-change policy;
- content approval, scheduled publication, CMS integration, or publication mechanisms;
- Search ranking, indexing, query, filtering, facets, provider, projection, synchronization, or integration mechanisms;
- concrete API routes, operations, methods, statuses, DTOs, payloads, or schemas;
- database schemas, tables, columns, SQL, ORM mappings, indexes, constraints, identifier strategies, locks, or isolation levels;
- event names, schemas, payloads, topics, queues, brokers, delivery technology, or choreography;
- cache or Redis use, providers, hosting, infrastructure, deployment topology, IaC, configuration, or feature-flag mechanisms;
- numerical limits, thresholds, timeouts, retries, retention, performance targets, SLAs, SLOs, or recovery objectives;
- a Role or Permission matrix;
- Search or Checkout as the Backend Specification after BCAT; or
- any Backend Specification identity, title, path, scope code, decomposition, or ordering position after BCAT.

## Open Product Decisions

This Accepted decision preserves the following **5 materially applicable Open Product Decisions** from `PRODUCT.md` §24:

| Source Decision | Open Product Decision | Preserved BCAT boundary |
| ---: | --- | --- |
| 1 | Final brand name and visual identity. | Brand governance may constrain Category labels and presentation but selects no taxonomy, lifecycle, or Category behavior. |
| 2 | Initial product categories and catalogue taxonomy. | Initial structure, roots, depth, naming, hierarchy, and classification remain unresolved. |
| 22 | Content approval and scheduled-publication workflow. | No Category content workflow, approval chain, scheduler, state, timing rule, CMS mechanism, or publication behavior is selected. |
| 23 | Administrative role and permission matrix. | BCAT requires contextual Authorization for protected Category behavior without selecting or defining administrative Roles, Permissions, role mappings, or a Role/Permission matrix. |
| 27 | Product data import, export, and migration requirements. | No taxonomy format, source, migration, import/export mechanism, schedule, or reconciliation policy is selected. |

Product-to-Category membership cardinality, primary-Category behavior, Category ordering, navigation presentation, automatic classification, and detailed lifecycle behavior remain unresolved Category policy boundaries rather than additional `PRODUCT.md` §24 decisions.

No listed Product Decision is resolved by selecting the BCAT roadmap boundary.

## Open Architecture Decisions

This Accepted decision preserves the following **9 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Source Decision | Open Architecture Decision | Preserved BCAT boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC tool or structure is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected for protected Category behavior. |
| 8 | Redis introduction and its approved use cases. | No Category cache, Projection, or Redis use is selected. |
| 9 | External messaging introduction and service selection. | Category events and integrations remain conditional and no messaging service is selected. |
| 10 | Initial search implementation details and extraction thresholds. | BCAT may supply Category evidence but selects no Search engine, indexing, extraction, ranking, or synchronization approach. |
| 12 | Backup retention and production recovery objectives. | Recoverability is preserved without retention periods, backup mechanisms, or numerical objectives. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | Category ownership is preserved without selecting physical schema layout. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | Reachable-state safety may be required without selecting a flag mechanism or lifecycle. |

No listed Architecture Decision is resolved by selecting the BCAT roadmap boundary. Frontend hosting, Payment provider, Shipping provider, transactional notification provider, and Product-media upload and transformation remain unresolved but are not materially required to establish this Category-only roadmap position.

## Required Governance Reviews

This decision was Accepted after completion of:

- Architecture review and approval of the immediate post-BCART position, Category-only decomposition, title, path, and scope code;
- affected Category ownership review confirming accurate specialization of the Approved Category Domain;
- affected Product ownership review confirming bounded BPRD consumption and preservation of Product and Product Variant authority;
- affected Search and Discovery ownership review confirming Search remains separate, is not automatically selected after BCAT, and retains ranking, indexing, query, projection, and synchronization authority;
- affected CMS ownership review confirming CMS remains separate and no content approval, publication, scheduling, or integration mechanism is selected;
- affected Administration ownership review confirming administrative behavior invokes Category-owned capabilities without transferring Category authority or defining a Role/Permission matrix;
- affected Pricing ownership review confirming Category does not acquire Pricing authority;
- affected Inventory ownership review confirming Category does not acquire Inventory authority;
- affected Cart ownership review confirming Cart remains separate and BCART transfers no Cart authority;
- affected Checkout ownership review confirming Checkout remains independently eligible and unresolved with no title, path, scope, decomposition, Contract, or later position established;
- affected Identity ownership review confirming bounded Identity evidence consumption and no Authentication, Session, token, Role, or Permission mechanism is selected;
- affected Customer ownership review confirming Customer and market context does not transfer Customer or Account authority or establish segmentation policy;
- confirmation that `BCAT` is unique among current Specification scope codes;
- confirmation that `specifications/backend/category/category-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCAT remains unresolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` included with ADR-0008 acceptance.

The required Architecture, Category, Product, Search and Discovery, CMS, Administration, Pricing, Inventory, Cart, Checkout, Identity, and Customer reviews and the listed confirmations were completed for ADR acceptance. No reviewer names, signatures, ticket identifiers, dates beyond this record's governed date, or external evidence are asserted by this record.

## Acceptance Conditions

Acceptance synchronized governance through:

- governance approval selecting BCAT as the next Backend Specification after BCART;
- confirmation that BCAT remains Category-only and preserves every owning-Domain authority;
- confirmation that Search remains separate and is not selected after BCAT;
- confirmation that Checkout remains independently eligible and unresolved;
- confirmation that every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCAT remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History synchronization establishing BCAT as the seventh downstream Backend Specification after BEB, immediately after BCART;
- `DECISIONS.md` metadata, Decision Index, and Revision History synchronization indexing ADR-0008 as Accepted; and
- ADR-0008 lifecycle wording recording the Accepted decision consistently with those synchronized canonical changes.

No `PRODUCT.md` change was required because acceptance review identified no direct contradiction preventing this decision. BCAT MUST NOT be drafted under Approved governance until the Accepted ADR and canonical synchronization are merged. Acceptance authorizes drafting only; it does not approve BCAT.

## Security, Data, Compatibility, and Operational Impact

This Accepted decision creates no runtime behavior, API, schema, data model, migration, event, provider, cache, infrastructure, deployment, configuration, or operational target. A future BCAT Specification must inherit materially applicable BEB security, Authorization, data protection, integrity, compatibility, migration, boundedness, observability, audit, failure, recovery, reconciliation, and verification obligations without selecting unresolved mechanisms.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0004-catalogue-backend-specification.md`
- `specifications/adr/ADR-0005-post-product-backend-specification.md`
- `specifications/adr/ADR-0006-post-inventory-backend-specification.md`
- `specifications/adr/ADR-0007-post-pricing-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/admin/admin-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-19 | Accepted | Accepted BCAT as the next Backend Specification after BCART while preserving Checkout, Search, and every post-BCAT roadmap position as unresolved. |
| 0.1.0 | 2026-09-19 | Proposed | Proposed BCAT as the next backend specialization after BCART while preserving Checkout and every post-BCAT sequencing position as unresolved. |
