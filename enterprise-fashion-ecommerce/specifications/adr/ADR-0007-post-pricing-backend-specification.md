# ADR-0007 — Cart Backend Specification Selection

## Identifier

ADR-0007

## Title

Cart Backend Specification Selection

## Version

0.1.0

## Status

Proposed

## Date

2026-09-17

## Last Updated

2026-09-17

## Owner

Architecture

## Authoritative

false

## Context

Accepted ADR-0001 through ADR-0006 and synchronized `ARCHITECTURE.md` §35 establish the Approved backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BPRC remains unresolved.

A post-BPRC roadmap audit found no already-governed next capability. It identified Category and Cart as the smallest defensible immediate candidate set. Both have coherent Approved Domain authority, materially sufficient upstream evidence, and the ability to preserve unresolved policy and implementation choices. No Accepted source currently orders either candidate after BPRC, so a further Architecture Decision is required.

Category owns taxonomy, hierarchy, classification, navigation eligibility, ordering, Category-side Product membership policy, Category lifecycle, and Category-owned content. BPRD supplies its governed Product association prerequisite, while BINV and BPRC establish separate Inventory and Pricing authorities. Category is independently eligible and would provide authoritative evidence required by Search and catalogue navigation.

Cart owns provisional shopping intent, Cart identity and association, Cart Item membership, selected Product Variant references, intended quantity, and accepted Cart mutation outcomes. BIDN and BCUS provide governed Identity, Customer, Account, and association context; BPRD provides Product and Product Variant evidence; BINV provides Inventory evidence; and BPRC now provides authoritative Pricing evidence. Cart can be specified without requiring Checkout, Order, Payment, or Shipping and Fulfilment backend specifications first, while remaining non-authoritative for those later capabilities.

This Proposed ADR makes one conditional roadmap decision. While Proposed, it is non-normative, does not change the canonical roadmap, and does not authorize drafting the selected Backend Specification.

## Decision Drivers

- Category and Cart are both independently eligible after BPRC, so immediate ordering requires explicit governance rather than inference.
- BPRD, BINV, and BPRC satisfy Cart's Product, Inventory, and Pricing evidence prerequisites.
- BIDN and BCUS satisfy Cart's bounded Identity, Customer, Account, and association-context prerequisites.
- ADR-0006 selected Pricing because it removed a documented prerequisite for Cart and Checkout; completing BPRC therefore makes Cart the next coherent transaction boundary without presuming Checkout implementation.
- Cart is a direct prerequisite for later Checkout orchestration, while Checkout also retains unresolved dependencies on Shipping, Payment, and Order.
- Cart has a coherent single-Domain authority and can preserve guest capability, persistence, expiration, merge, clearing, lifecycle, and numerical-policy questions as unresolved.
- Category remains independently eligible, but selecting it now would principally unblock Search and catalogue navigation while leaving the already-started transaction dependency chain unchanged.
- Selecting Cart does not reduce Category authority or imply that Category follows Cart.
- The selected boundary must be no broader than existing evidence requires and must establish no later roadmap position.

## Candidate Analysis

### Category

Category satisfies prerequisite, authority-coherence, and mechanism-neutrality criteria. BPRD supplies governed Product identity and association evidence. BINV and BPRC supply independently governed Inventory and Pricing boundaries without transferring authority. Category can preserve unresolved taxonomy, hierarchy, membership, classification, lifecycle, publication, navigation, and ordering policy.

Category would unblock a material dependency for Search and Discovery because Search must preserve authoritative Category taxonomy, hierarchy, membership, classification, navigation eligibility, and ordering. Its main sequencing advantage is therefore catalogue discovery and navigation.

Category does not depend on Cart and is not less authoritative or less important. Existing governance does not place Category behind Cart. Its backend identity and later roadmap position remain unresolved by this ADR.

### Cart

Cart satisfies prerequisite, authority-coherence, and mechanism-neutrality criteria. BIDN and BCUS provide bounded trusted Identity and Customer or Account context; BPRD provides governed Product and Product Variant evidence; BINV provides governed Inventory evidence; and BPRC provides governed current Pricing evidence. Cart can retain provisional intent without becoming authoritative for Product, Inventory, Pricing, Checkout, Order, Payment, Shipping and Fulfilment, Customer, Identity, or another Domain.

Cart directly unblocks the Cart-intent handoff needed by Checkout. Its backend boundary can be specified before Checkout because the Approved Cart Domain already defines Checkout as a separate consumer that must revalidate current Product, Pricing, Inventory, Customer Authorization, and other required truth. Cart can preserve unresolved guest, persistence, expiration, merge, clearing, lifecycle, and numerical policies rather than inventing them.

### Comparative Trade-off

Both candidates have satisfied upstream prerequisites and coherent single-Domain authority. Category has fewer transaction dependencies and unlocks Search; Cart has more materially governed upstream consumption but all major upstream backend authorities now exist and it unlocks Checkout.

Cart is selected because the Accepted sequence deliberately established Product, Inventory, and Pricing before later transaction capabilities, and ADR-0006 specifically identified Pricing as the remaining prerequisite preventing Cart from being fully supported. Selecting Cart now continues that documented dependency chain and supplies a required Checkout input without selecting Checkout, Order, Payment, or Shipping and Fulfilment Contracts or ordering. Category remains independently eligible and unresolved rather than becoming the automatic next position.

## Decision

If ADR-0007 is Accepted and canonical governance is synchronized and merged, the sixth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, BINV, and BPRC, SHALL be:

- **Capability:** Cart
- **Title:** Cart Backend Specification
- **Path:** `specifications/backend/cart/cart-backend.md`
- **Scope code:** `BCART`
- **Decomposition:** Cart-only; specializes exactly the Approved Cart Domain
- **Position:** Sixth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, BINV, and BPRC

`BCART` means Backend Cart. It is collision-free among current Specification scope codes and is proposed only for the Cart Backend Specification. It establishes no naming rule for later scopes.

BCART SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL specialize only the Approved Cart Domain and SHALL NOT absorb Category, Product, Inventory, Pricing, Customer, Identity, Checkout, Order, Payment, Shipping and Fulfilment, Search and Discovery, Return and Refund, Administration, Reporting and Analytics, CMS, Notifications, or another Domain's authority.

BCART SHALL consume materially applicable BIDN Contracts or trusted Identity evidence only where governed Cart association or protected behavior requires it, without transferring Identity authority. Authentication evidence alone SHALL NOT establish Cart Authorization.

BCART SHALL consume materially applicable BCUS Contracts or governed Customer and Account context only where Approved Cart policy permits, without inventing guest capability, Customer eligibility, association, persistence, migration, merge, personalization, or Account policy and without transferring Customer or Account authority.

BCART SHALL consume materially applicable BPRD Contracts or governed Product and Product Variant evidence for correct Cart Item association and Product-change handling without transferring Product authority or treating retained Product information as current Product truth.

BCART SHALL consume materially applicable BINV Contracts or governed Inventory evidence for permitted validation and staleness handling without transferring Inventory authority or treating Cart presence or intended quantity as Stock, Stock Reservation, Available-to-Sell, or purchase eligibility.

BCART SHALL consume materially applicable BPRC Contracts or governed Pricing evidence for permitted presentation and change handling without transferring Pricing authority or representing retained commercial values as locked, final, or authoritative.

Contextual Authorization SHALL remain with the Domain owning the affected Resource, action, property, association, and current state. Identity evidence, Customer context, Product context, Inventory context, Pricing context, Role labels, Claims, Scope, UI state, or supplied identifiers alone SHALL NOT grant Cart authority.

This decision would establish only `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCART would remain unresolved. It would not establish Category as following BCART and would not establish Search and Discovery, Checkout, Order, Payment, Shipping and Fulfilment, or another later backend position.

Only after ADR-0007 is Accepted and canonical Architecture and Decision governance is synchronized and merged would this decision authorize drafting BCART under Approved governance. It would not itself approve the future BCART Specification, which must undergo its own Draft-to-Approved lifecycle.

## Selected Candidate Rationale

Cart is selected because its material upstream backend dependencies are now governed and it removes a documented prerequisite for Checkout without requiring unresolved downstream Contracts. The decision follows the dependency rationale already recorded by ADR-0006: Pricing was selected ahead of Cart specifically because Cart required authoritative Pricing evidence. With BPRC Approved, that gap is closed.

This selection is not based on implementation convenience, directory order, frontend page order, or an assumption that transaction flow is intrinsically more important than catalogue navigation. It is a bounded sequencing judgment based on already-governed dependencies and the ability to draft a complete mechanism-neutral Cart specialization now.

## Rejected and Deferred Alternatives

### A. Category Next

Deferred, not rejected as a capability. Category is independently eligible, has coherent authority, and remains important to Search and catalogue navigation. It is not selected for the immediate position because Cart now has every major upstream backend authority established and directly supplies a missing prerequisite for Checkout. This ADR does not establish Category's backend title, path, scope code, decomposition, or later position.

### B. Combine Category and Cart

Rejected because Category and Cart are separate Approved Domains with distinct authority, lifecycle, integrity, policy, and consumer relationships. No governing evidence requires or supports a combined backend boundary.

### C. Search and Discovery Next

Deferred because Search consumes Category evidence and no Category backend boundary is governed. Search implementation details and extraction thresholds also remain an Open Architecture Decision. This ADR establishes neither Search decomposition nor ordering.

### D. Checkout Next

Deferred because Checkout requires a governed Cart-intent handoff and also coordinates unresolved Shipping, Payment, and Order concerns. Selecting Checkout before Cart would require premature cross-Domain Contract assumptions.

### E. Order, Payment, or Shipping and Fulfilment Next

Deferred because each retains material unresolved dependencies on Checkout, Order, Payment, Shipping, or their coordination. BPRC supplies commercial context but does not by itself settle their immediate order or Contracts.

### F. Leave the Roadmap Unresolved

Not selected because repository evidence supports a bounded Cart-only specialization whose major upstream authorities are governed and whose output is a documented prerequisite for later Checkout orchestration. The selection still requires ADR acceptance and canonical synchronization.

## Authority and Dependency Boundaries

- **BEB:** every materially applicable shared backend obligation must be inherited and explicitly traced rather than duplicated or weakened.
- **BIDN:** trusted Identity evidence may be consumed for governed association and protected behavior; Identity authority and mechanisms do not transfer.
- **BCUS:** governed Customer or Account context may be consumed only where Approved Cart policy permits; Customer and Account authority does not transfer.
- **BPRD:** Product and Product Variant evidence supports Cart Item association and change handling; Product authority does not transfer.
- **BINV:** Inventory evidence supports permitted validation and staleness handling; Cart does not acquire Stock, reservation, Available-to-Sell, Overselling, or purchase-eligibility authority.
- **BPRC:** Pricing evidence supports permitted presentation and material-change handling; Cart does not calculate or own authoritative commercial truth.
- **Category:** taxonomy, hierarchy, classification, navigation eligibility, ordering, lifecycle, and Category-side membership policy remain Category-owned and unresolved as a backend roadmap position.
- **Checkout:** purchase orchestration, final revalidation, commercial commitment, and Checkout lifecycle remain Checkout-owned.
- **Order:** Order creation, lifecycle, snapshots, and historical commercial truth remain Order-owned.
- **Payment:** Payment Attempt, Authorization, Capture, Settlement, Refund execution, provider evidence, and Payment truth remain Payment-owned.
- **Shipping and Fulfilment:** delivery choice, operational eligibility, Shipping Rate source, Shipment, Carrier, fulfilment, tracking, and delivery truth remain Shipping-owned.
- **Search and Discovery and other Domains:** all retain their Approved authority and receive no backend position from this ADR.

## Consequences

Positive consequences include:

- a bounded Cart-only backend proposal based on satisfied Product, Inventory, Pricing, Identity, and Customer prerequisites;
- an explicit future backend owner for provisional Cart intent without transferring upstream authority;
- removal of the Cart backend gap before later Checkout governance;
- preservation of Category as an independently eligible, unresolved backend concern;
- explicit BEB inheritance and bounded consumption of BIDN, BCUS, BPRD, BINV, and BPRC; and
- continued independent governance of every later roadmap position.

Costs and trade-offs include:

- Category remains unresolved despite being eligible and required by Search;
- Search remains blocked on Category backend governance;
- Checkout remains unresolved and still depends on Shipping, Payment, Order, and policy decisions in addition to Cart;
- guest capability, persistence, expiration, merge, clearing, lifecycle, and numerical Cart policy remain unresolved; and
- any later change to decomposition or order requires separate governance.

## Explicit Non-Decisions

ADR-0007 does not select or define:

- guest Cart or guest Checkout capability, mandatory Account policy, anonymous association, or anonymous-to-authenticated migration;
- Cart persistence, duration, expiration, abandonment, cleanup, merge, merge precedence, conflict resolution, clearing, or lifecycle state model;
- repeated-add combination behavior, quantity policy, Cart size, item limit, minimum or maximum quantity, or another numerical rule;
- Product, Product Variant, Category, Inventory, Pricing, Customer, Identity, Checkout, Order, Payment, Shipping and Fulfilment, Return and Refund, Search and Discovery, Administration, Reporting, CMS, Notifications, or another Domain's policy;
- concrete API routes, operations, methods, statuses, DTOs, payloads, or schemas;
- database schemas, tables, columns, SQL, ORM mappings, indexes, constraints, identifiers, locks, isolation levels, caches, Redis use, or topology;
- event names, schemas, payloads, topics, queues, brokers, delivery technology, or choreography;
- providers, hosting, infrastructure, deployment, configuration, or feature-flag mechanisms;
- numerical limits, thresholds, timeouts, retries, retention, performance targets, SLAs, SLOs, or recovery objectives;
- a Role or Permission matrix; or
- Category, Search and Discovery, Checkout, Order, Payment, Shipping and Fulfilment, or any Backend Specification identity, title, path, scope code, decomposition, or ordering position after BCART.

## Open Product Decisions

This decision preserves the following **5 materially applicable Open Product Decisions** from `PRODUCT.md` §24:

| Source Decision | Open Product Decision | Preserved BCART boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No guest Cart, guest Checkout, Account requirement, association, persistence, migration, or continuation policy is selected. |
| 13 | Back-order and pre-order support. | No unavailable-Product eligibility, negative availability, backorder, preorder, or retention policy is selected. |
| 14 | Voucher and promotion stacking policy. | Cart may present governed Pricing evidence but no stacking, precedence, or application policy is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No threshold, wording, presentation, or eligibility rule is selected. |
| 23 | Administrative role and permission matrix. | Contextual least privilege is required without defining Roles or Permissions. |

Cart expiration, persistence duration, abandonment, anonymous-to-authenticated migration, merge behavior and precedence, clearing timing, lifecycle details, and numerical Cart rules remain unresolved scope boundaries rather than additional `PRODUCT.md` §24 decisions.

No listed decision is resolved by selecting the Cart backend boundary.

## Open Architecture Decisions

This decision preserves the following **8 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Source Decision | Open Architecture Decision | Preserved BCART boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No IaC tool or structure is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected for Cart association or access. |
| 8 | Redis introduction and its approved use cases. | No Cart cache, persistence, Session, or Redis use is selected. |
| 9 | External messaging introduction and service selection. | Cart events remain conditional and no messaging service is selected. |
| 12 | Backup retention and production recovery objectives. | Recoverability is required without retention periods, backup mechanisms, or numerical objectives. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | Cart ownership is preserved without selecting physical schema layout. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | Reachable-state safety may be required without selecting a flag mechanism or lifecycle. |

No listed decision is resolved by selecting the Cart backend boundary. Payment provider selection, Shipping provider selection, transactional notification provider selection, initial Search implementation details and extraction thresholds, Product-media upload and transformation strategy, and frontend hosting strategy remain unresolved but are not materially required to define this Cart-only roadmap boundary.

## Required Governance Reviews

This Proposed decision becomes Accepted only after:

- Architecture review and approval of the immediate post-BPRC position, Cart-only decomposition, title, path, and scope code;
- affected Cart ownership review confirming accurate specialization of the Approved Cart Domain;
- affected Category ownership review confirming Category remains independently eligible, separate, and unordered after BCART;
- affected Checkout ownership review confirming Cart handoff does not transfer Checkout authority or prematurely define Checkout Contracts or ordering;
- affected Administration ownership review confirming that administrative visibility or support uses Cart-owned capabilities, transfers no Cart authority, and defines no Role or Permission matrix;
- affected Product ownership review confirming bounded BPRD consumption and preservation of Product and Product Variant authority;
- affected Inventory ownership review confirming bounded BINV consumption and preservation of Inventory authority;
- affected Pricing ownership review confirming bounded BPRC consumption and preservation of Pricing authority;
- affected Identity ownership review confirming bounded BIDN consumption and no invented Authentication or Session mechanism;
- affected Customer ownership review confirming bounded BCUS consumption and no invented guest, Account, association, migration, merge, or eligibility policy;
- affected Search and Discovery ownership review confirming Search remains separate and its backend position unresolved;
- affected Order, Payment, and Shipping and Fulfilment ownership review confirming their authorities and later positions remain unresolved;
- confirmation that `BCART` is unique among current Specification scope codes;
- confirmation that `specifications/backend/cart/cart-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCART remains unresolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` before ADR-0007 becomes Accepted.

No review listed above is represented as completed.

## Acceptance Conditions

ADR-0007 may become Accepted only when all Required Governance Reviews and confirmations are complete and canonical governance is synchronized and merged through:

- ADR-0007 lifecycle wording changing from Proposed to Accepted;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History establishing BCART as the sixth downstream Backend Specification after BEB, immediately after BPRC; and
- `DECISIONS.md` metadata, Decision Index, and Revision History indexing ADR-0007 as Accepted.

No `PRODUCT.md` change is required unless review identifies a direct contradiction that prevents acceptance. If such a contradiction is found, acceptance must stop rather than silently changing Product policy.

BCART MUST NOT be drafted under Approved governance until ADR-0007 is Accepted and the canonical Architecture and Decision synchronization is merged. Acceptance would authorize drafting, not approve BCART; the future Cart Backend Specification must begin as Draft and complete its own Draft-to-Approved lifecycle.

## Security, Data, Compatibility, and Operational Impact

This Proposed decision creates no runtime behavior, API, schema, data model, migration, event, provider, infrastructure, deployment, configuration, cache, Cart persistence, or operational target. A future BCART Specification would inherit materially applicable BEB security, data protection, compatibility, migration, boundedness, observability, audit, failure, recovery, reconciliation, and verification obligations without selecting mechanisms.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0006-post-inventory-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`

## Supersedes

—

## Superseded By

—

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Proposed | Proposed the Cart-only Cart Backend Specification as the sixth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, BINV, and BPRC, while leaving Category and every later backend roadmap position unresolved. |
