# ADR-0006 — Pricing Backend Specification Selection

## Identifier

ADR-0006

## Title

Pricing Backend Specification Selection

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

Accepted ADR-0001 through ADR-0005 and synchronized `ARCHITECTURE.md` §35 establish the Approved backend sequence `BEB → BIDN → BCUS → BPRD → BINV`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BINV remains unresolved.

A post-BINV evidence audit found no already-governed next capability. It identified Category and Pricing as the smallest defensible immediate candidate set. Both have coherent Approved Domain authority and materially sufficient upstream evidence, but no Accepted source orders either ahead of the other. A further Architecture Decision is therefore required.

The Approved Category Domain owns Category identity, taxonomy, hierarchy, classification, lifecycle, navigation eligibility, and Category-side Product membership policy. BPRD satisfies its Product association prerequisite. Category is independently important to catalogue navigation and Search and Discovery.

The Approved Pricing Domain owns authoritative Price and governed commercial calculation outcomes, including applicable Discount, Promotion, Voucher, Money, Currency, and qualified tax-related Pricing outcomes. BPRD satisfies its Product and Product Variant prerequisite; BCUS can supply governed Customer or Account eligibility context; BINV preserves authoritative Inventory separation. Pricing is a direct prerequisite for fully supporting Cart and Checkout and supplies governed commercial context to Order and Payment.

This Proposed ADR makes one conditional roadmap decision. While Proposed, it is non-normative, does not change the canonical roadmap, and does not authorize drafting the selected Backend Specification.

## Decision Drivers

- Both Category and Pricing are eligible after BINV, so selection requires explicit governance rather than inferred ordering.
- BPRD supplies governed Product and Product Variant identity required by Pricing.
- BCUS supplies governed Customer and Account context where Approved Pricing policy permits its use.
- BINV supplies a governed Inventory boundary while preserving Pricing non-authority for Inventory truth.
- Pricing is required before Cart and Checkout backend boundaries can be fully supported.
- Pricing supplies governed commercial context needed by later Order snapshots and Payment amount handling.
- Selecting Pricing reduces uncertainty across the transaction chain without requiring Category or Search Contracts.
- Pricing has a coherent single-Domain boundary and can be specified while commercial Product decisions remain unresolved.
- The selected boundary must be no broader than existing evidence requires and must not establish any later position.

## Dependency Analysis

### Category

The Product/BPRD association prerequisite is satisfied. Category can be specialized independently without Pricing or Inventory authority and supplies taxonomy, classification, navigation, and membership evidence to Search and catalogue experiences. `REQ-CAT-012`–`014` preserve Product association and non-authority; `REQ-CAT-029`–`030` establish the Search projection boundary; `REQ-CAT-031`–`033` preserve Product, Pricing, and Inventory authority.

Category is eligible. Existing governance does not order it ahead of Pricing. Its strongest immediate downstream dependency is Search and catalogue navigation, and unresolved taxonomy, hierarchy, membership, lifecycle, and publication policy can be preserved without preventing a future mechanism-neutral specification.

### Pricing

BPRD satisfies governed Product and Product Variant association under `REQ-PRC-004`. BCUS can supply Customer or Account eligibility context under `REQ-PRC-015` without transferring Customer authority. BINV preserves the separation required by `REQ-PRC-016`; Inventory availability does not establish Price or scarcity policy.

Pricing supplies governed outcomes to Cart under `REQ-PRC-017`, authoritative Checkout revalidation under `REQ-PRC-018`, current values distinct from historical Order truth under `REQ-PRC-019`, and governed Payment input without Payment authority under `REQ-PRC-020`. ADR-0005 states that Pricing remains independently required before Cart and Checkout backend boundaries can be fully supported.

Pricing is eligible. Existing governance did not previously order it ahead of Category. This ADR proposes that ordering because Pricing removes a documented prerequisite from multiple transaction capabilities, while Category primarily removes a prerequisite from Search and catalogue navigation. This is a roadmap dependency judgment, not a claim that Pricing is more authoritative or intrinsically more important.

## Decision

If ADR-0006 is Accepted and canonical governance is synchronized, the fifth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, and BINV, SHALL be:

- **Capability:** Pricing
- **Title:** Pricing Backend Specification
- **Path:** `specifications/backend/pricing/pricing-backend.md`
- **Scope code:** `BPRC`
- **Decomposition:** Pricing-only; specializes exactly the Approved Pricing Domain
- **Position:** Fifth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, and BINV

`BPRC` means Backend Pricing. It is unique among current Specification scope codes and is proposed only for the Pricing Backend Specification. It does not establish a naming rule for later scopes.

BPRC SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL specialize only the Approved Pricing Domain and SHALL NOT absorb Category, Product, Inventory, Customer, Identity, Cart, Checkout, Order, Payment, Shipping, Return, Search, Administration, or another Domain's authority.

BPRC SHALL consume materially applicable BIDN Contracts or trusted Identity evidence only where governed protected Pricing behavior requires it, without transferring Identity authority. Authentication evidence alone SHALL NOT establish Pricing Authorization.

BPRC SHALL consume materially applicable BCUS Contracts or governed Customer and Account context only where Approved Pricing policy permits, without inventing eligibility, segmentation, loyalty, entitlement, or personalization policy and without transferring Customer or Account authority.

BPRC SHALL consume materially applicable BPRD Contracts or governed Product and Product Variant evidence for correct Pricing association and revalidation without transferring Product authority or treating Product state as Price.

BPRC SHALL consume materially applicable BINV Contracts or governed Inventory evidence only where Approved Pricing policy permits, without transferring Inventory authority or inferring scarcity pricing, availability-based pricing, Stock, Stock Reservation, Available-to-Sell, or purchase eligibility.

Contextual Authorization SHALL remain with the Domain owning the affected Resource, action, property, association, and current state. Identity evidence, Customer context, Product context, Inventory context, Role labels, Claims, Scope, UI state, or supplied identifiers alone SHALL NOT grant Pricing authority.

This decision establishes only `BEB → BIDN → BCUS → BPRD → BINV → BPRC`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BPRC remains unresolved. It does not establish Category, Search and Discovery, Cart, Checkout, Order, Payment, Shipping and Fulfilment, or another later backend position.

Only after ADR-0006 is Accepted and canonical synchronization is complete would this decision authorize drafting BPRC under Approved governance. It would not itself approve the future BPRC Specification.

## Authority Boundary

BPRC would remain subordinate to governing sources, Approved Business Requirements, the Approved Pricing Domain, BEB, and materially applicable BIDN, BCUS, BPRD, and BINV Contracts or evidence. It would not become repository-wide authority.

Pricing would retain only its governed authority for current authoritative commercial calculation outcomes, including applicable Price, Discount, Promotion, Voucher, Money, Currency, qualified tax-related Pricing outcomes, rule evaluation, explainability, and Pricing-owned failure and reconciliation.

Product would retain Product and Product Variant identity, content, publication, visibility, structural sellability, and Product-owned attributes. Category would retain identity, taxonomy, hierarchy, classification, navigation, lifecycle, and Category-side membership policy. Inventory would retain Stock, Available-to-Sell, Stock Reservation, Stock Adjustment, Stock Movement, and Overselling protection.

Customer and Account would retain Customer identity, Account, profile, Address, Preference, Consent, and governed context. Identity would retain Authentication, Principal establishment, credentials, Sessions, revocation, recovery verification, Roles, Permissions, Claims, Scope, and Identity evidence.

Cart would retain shopping intent. Checkout would retain purchase orchestration and commercial commitment. Order would retain Order creation, lifecycle, snapshots, and historical commercial truth. Payment would retain Payment Attempt, Authorization, Capture, Settlement, Refund execution, provider evidence, and Payment truth. Shipping would retain delivery choice, operational eligibility, Shipping Rate source, Shipment, carrier, fulfilment, tracking, and delivery truth.

Search and Discovery would retain search request, representation, filtering, faceting, ranking, indexing, recovery, and Search operational truth. Return and Refund, Administration, CMS, Notifications, Reporting and Analytics, and all other Domains would retain their governed authority.

## Inheritance and Consumption Relationships

- **BEB:** every materially applicable shared backend obligation is inherited and explicitly traced rather than duplicated.
- **BIDN:** trusted Identity evidence may be consumed for protected behavior; Identity authority and mechanisms do not transfer.
- **BCUS:** governed Customer or Account eligibility context may be consumed only where Approved policy permits; Customer and Account authority does not transfer.
- **BPRD:** Product and Product Variant evidence supports association and revalidation; Product authority does not transfer.
- **BINV:** Inventory evidence remains independently authoritative and cannot create or alter Pricing policy or outcomes unless Approved Product policy governs the dependency.
- **Later consumers:** Cart, Checkout, Order, and Payment may consume governed Pricing outcomes through future governed Contracts without transferring Pricing authority or receiving an established roadmap position from this ADR.

## Consequences

Positive consequences include:

- a bounded Pricing-only backend proposal based on documented transaction prerequisites;
- removal of a required upstream gap for later Cart and Checkout governance;
- a governed future source for current commercial outcomes consumed by Order and Payment boundaries;
- preservation of Category as an independently eligible, unresolved backend concern;
- explicit BEB inheritance and bounded consumption of BIDN, BCUS, BPRD, and BINV; and
- continued independent governance of every later roadmap position.

Costs and trade-offs include:

- Category remains unresolved despite being eligible and important for Search and navigation;
- Cart and Checkout remain unresolved and may still require additional upstream governance;
- unresolved promotion, Voucher, tax, invoice, fraud, shipping-commercial, Return, Refund, gift-card, store-credit, and credit policy must remain explicit;
- later Contract and compatibility decisions may require coordinated migration planning; and
- a later decomposition change would require separate governance.

## Alternatives Considered

### A. Pricing Next

Selected conditionally because its prerequisites are already governed and it is a documented prerequisite for multiple later transaction capabilities. The selection preserves all unresolved commercial policy and does not establish Cart, Checkout, Order, or Payment ordering.

### B. Category Next

Not selected for the immediate position. BPRD satisfies Category's Product association prerequisite, Category has a coherent single-Domain boundary, and it is independently important to navigation and Search. Existing governance did not order Category behind Pricing before this ADR. Pricing is selected because it removes a prerequisite shared by Cart, Checkout, Order commercial context, and Payment amount context, whereas Category principally unlocks Search and catalogue navigation. Category's later identity and position remain unresolved.

### C. Combine Category and Pricing

Not selected because they are separate Approved Domains with distinct authority, lifecycle, integrity, policy, and consumer relationships. Shared Product dependency does not justify a combined backend boundary.

### D. Search and Discovery Next

Not selected because Search consumes Product, Category, Pricing, and Inventory evidence. Category and Pricing backend boundaries are not both governed, and Search implementation and extraction thresholds remain unresolved.

### E. Cart, Checkout, or Another Later Capability Next

Not selected because Cart still requires Pricing, and Checkout additionally requires Cart, Pricing, Shipping, Payment, and Order evidence. Later capabilities have equal or greater unresolved prerequisites.

### F. Leave the Roadmap Unresolved

Not selected because repository evidence supports a bounded Pricing specialization that removes a documented prerequisite from several later capabilities. The decision still requires acceptance and canonical synchronization.

## Open Product Decisions

This decision preserves all materially applicable `PRODUCT.md` §24 decisions, including:

- shipping provider, service levels, delivery areas, and fee policy;
- free-delivery threshold and promotional treatment;
- tax-inclusive display and invoice requirements;
- returns, exchanges, and refund policy;
- Voucher and Promotion stacking policy;
- South African tax-display, invoice, and Credit Note policy;
- fraud-screening approach and manual-review workflow; and
- gift cards, store credit, and promotional credit policy.

No listed decision is resolved by selecting the Pricing backend boundary.

## Open Architecture Decisions

This decision preserves all materially applicable `ARCHITECTURE.md` §34 decisions, including backend hosting, infrastructure as code, customer and administrator session/token strategy, Payment provider selection, Shipping provider selection, Redis introduction and approved use cases, external messaging introduction and service selection, backup retention and production recovery objectives, PostgreSQL schema strategy, and repository-wide feature-flag implementation and lifecycle management.

No listed decision is resolved by selecting the Pricing backend boundary.

## Explicit Non-Decisions

ADR-0006 does not select or define:

- Price List structures, Pricing algorithms, formulas, effective-date mechanisms, rounding, scale, precision, Minor Units, Currency conversion, or exchange rates;
- Discount, Promotion, Voucher, stacking, precedence, combinability, eligibility, usage, threshold, limit, expiry, redemption, reversal, or Refund policy;
- Tax rate, Tax Category, jurisdiction logic, tax-inclusive or tax-exclusive presentation, invoice, Credit Note, or legal commercial-document policy;
- shipping fee, free-delivery, delivery-area, service-level, or promotional-delivery policy;
- fraud score, threshold, provider, blocking rule, or manual-review workflow;
- gift-card, store-credit, or promotional-credit authority or calculation treatment;
- Customer segment, entitlement, loyalty, personalization, or eligibility policy;
- scarcity pricing, availability-based pricing, Stock, reservation, or Inventory policy;
- concrete API routes, operations, methods, statuses, DTOs, payloads, or schemas;
- database schemas, tables, columns, SQL, ORM mappings, indexes, constraints, identifiers, locks, isolation levels, caches, or topology;
- event names, schemas, payloads, topics, queues, brokers, delivery technology, or choreography;
- providers, hosting, infrastructure, deployment, configuration, or feature-flag mechanisms;
- numerical limits, thresholds, timeouts, retries, retention, performance targets, SLAs, SLOs, or recovery objectives;
- a Role or Permission matrix; or
- Category, Search, Cart, Checkout, Order, Payment, Shipping, or any Backend Specification identity, title, path, scope code, decomposition, or position after BPRC.

## Required Governance Reviews

This Proposed decision becomes Accepted only after:

- Architecture review and approval of the immediate post-BINV position, Pricing-only decomposition, title, path, and scope code;
- affected Pricing ownership review confirming accurate specialization of the Approved Pricing Domain;
- affected Category ownership review confirming Category remains separate, eligible, and unordered after BPRC;
- affected Product ownership review confirming bounded BPRD consumption and preservation of Product and commercial-policy authority;
- affected Search and Discovery ownership review confirming Search remains separate and its backend position unresolved;
- affected Cart ownership review confirming Pricing consumption does not transfer Cart authority or prematurely define Cart Contracts;
- affected Checkout ownership review confirming Pricing revalidation support does not transfer Checkout authority or establish Checkout ordering;
- affected Identity ownership review confirming bounded BIDN consumption;
- affected Customer ownership review confirming bounded BCUS consumption and no invented eligibility policy;
- affected Inventory ownership review confirming bounded BINV consumption and preservation of Inventory authority;
- confirmation that `BPRC` is unique among current Specification scope codes;
- confirmation that `specifications/backend/pricing/pricing-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BPRC remains unresolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` before ADR-0006 becomes Accepted.

No review listed above is represented as completed. BPRC MUST NOT be drafted under Approved governance until ADR-0006 is Accepted and canonical synchronization is complete.

If ADR-0006 is later Accepted, governance must synchronize only ADR-0006 lifecycle wording, `ARCHITECTURE.md` §35 plus metadata, Document Status, and Revision History, and `DECISIONS.md` metadata, Decision Index, and Revision History. No `PRODUCT.md` change is required unless review identifies a direct contradiction that prevents acceptance.

## Security, Data, Compatibility, and Operational Impact

This Proposed decision creates no runtime behavior, API, schema, data model, migration, event, provider, infrastructure, deployment, configuration, cache, Pricing calculation, or operational target. A future BPRC Specification would inherit materially applicable BEB security, data protection, compatibility, migration, boundedness, observability, audit, failure, recovery, reconciliation, and verification obligations without selecting mechanisms.

## Related Documents

- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0004-catalogue-backend-specification.md`
- `specifications/adr/ADR-0005-post-product-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`

## Supersedes

—

## Superseded By

—

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Proposed | Proposed the Pricing-only Pricing Backend Specification as the fifth downstream Backend Specification after BEB, immediately after BIDN, BCUS, BPRD, and BINV, while leaving Category and every later backend roadmap position unresolved. |
