# ADR-0005 — Inventory Backend Specification Selection

## Identifier

ADR-0005

## Title

Inventory Backend Specification Selection

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

Accepted ADR-0001 established the Shared Backend Baseline Specification under scope `BEB`. Accepted ADR-0002 established the Identity and Access Backend Specification under scope `BIDN` as the first downstream Backend Specification after BEB. Accepted ADR-0003 established the Customer and Account Backend Specification under scope `BCUS` as the second downstream Backend Specification after BEB, immediately after BIDN. Accepted ADR-0004 established the Product-only Product Catalogue Backend Specification under scope `BPRD` as the third downstream Backend Specification after BEB, immediately after BIDN and BCUS. BEB, BIDN, BCUS, and BPRD now exist as Approved Specifications.

`ARCHITECTURE.md` §35 and Accepted ADR-0004 leave every Backend Specification title, path, scope code, decomposition, and ordering position after BPRD unresolved. ADR-0004 identified Product and Product Variant identity as a common prerequisite for Pricing and Inventory but deliberately did not determine their relative order. Category also became a viable candidate once the Product-side association boundary was established. A further Architecture Decision is therefore required before another downstream Backend Specification may be drafted under Approved governance.

The Approved Inventory Domain owns Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, inventory availability state, Overselling prevention, and Inventory-specific reconciliation. Its governed Product Variant association now has an Approved backend source in BPRD. Inventory outcomes are direct inputs to Search and Discovery representation, Cart availability handling, Checkout validation and reservation coordination, Order-related Inventory effects, Shipping and Fulfilment coordination, Administration, and Reporting.

The Approved Pricing Domain is also a strong immediate candidate and now has its Product prerequisite. It has substantial downstream importance but retains a wider concentration of unresolved commercial policy concerning promotions, vouchers, tax, invoicing, fraud-related eligibility, and post-purchase adjustments. The Approved Category Domain is likewise ready for a bounded specialization, but its principal immediate downstream dependency is catalogue navigation and Search and Discovery rather than the transaction-integrity chain.

This Proposed ADR selects one immediate roadmap position. While Proposed, it is non-normative, does not modify the canonical roadmap, and does not authorize drafting the selected Backend Specification.

## Decision Drivers

- BPRD now provides the governed Product and Product Variant identity required for valid Inventory association.
- Inventory provides authoritative availability and reservation outcomes required across Search and Discovery, Cart, Checkout, Order, Shipping and Fulfilment, Administration, and Reporting.
- Current Stock and Available-to-Sell are trust-critical inputs; stale or inferred availability must not authorize commercial commitment.
- Inventory owns a coherent single-Domain boundary with explicit non-authority for Product, Category, Pricing, Customer, Cart, Checkout, Order, Payment, Shipping, and Administration truth.
- Inventory can be specified mechanism-neutrally while preserving reservation duration, backorder and preorder policy, availability messaging, recovery objectives, and numerical rules as unresolved.
- Establishing Inventory next reduces uncertainty for later Search, Cart, Checkout, Order, and fulfilment backend decisions without forcing Pricing or Category Contracts.
- The decision must remain consistent with ADR-0004, which established Product as the common upstream prerequisite without deciding the relative order of Pricing and Inventory.
- The selected boundary should specialize exactly one Approved Domain and remain no broader than the evidence requires.

## Decision

If ADR-0005 is Accepted and canonical governance is synchronized, the fourth downstream Backend Specification after the Shared Backend Baseline, immediately after BIDN, BCUS, and BPRD, SHALL be:

- **Title:** Inventory Backend Specification
- **Path:** `specifications/backend/inventory/inventory-backend.md`
- **Scope code:** `BINV`
- **Position:** Fourth downstream Backend Specification after BEB, immediately after BIDN, BCUS, and BPRD

`BINV` means Backend Inventory. It is unique among current Specification scope codes and is proposed only for the Inventory Backend Specification. It does not reserve or imply a scope-code convention for later Backend Specifications.

BINV SHALL specialize only the Approved Inventory Domain. It SHALL NOT absorb Product, Category, Pricing, Search and Discovery, Cart, Checkout, Order, Payment, Shipping and Fulfilment, Return and Refund, Administration, CMS, Notifications, Reporting and Analytics, Identity, Customer, Account, or another Domain's authority.

Where already governed by the Approved Inventory Domain, BINV MAY provide bounded backend specialization for:

- authoritative Stock, Available-to-Sell, Stock Reservation, Stock Adjustment, Stock Movement, and explicitly governed inventory availability outcomes;
- correct Product Variant or other explicitly governed inventory-bearing Resource association;
- Inventory-owned integrity, isolation, concurrency, duplicate safety, stale-state safety, Overselling prevention, recovery, and reconciliation outcomes;
- Inventory-owned Use Case orchestration;
- Inventory-specific Contract obligations;
- conditional Inventory event and integration obligations where separately governed; and
- Inventory-specific persistence interaction, observability, audit, security, failure, and verification obligations without selecting mechanisms.

BINV SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance.

BINV SHALL consume materially applicable BIDN Contracts or trusted Identity evidence only where Authentication, Principal establishment, Session or revocation evidence is required. It SHALL NOT transfer Identity authority or infer contextual Inventory Authorization from authenticated Identity evidence alone.

BINV SHALL consume materially applicable BCUS Contracts or governed Customer and Account context only where Inventory behavior already requires it. Customer, Account, profile, Address, Preference, Consent, Wishlist, or Session state SHALL NOT establish or change Inventory truth or create personalized Inventory policy without Approved governance.

BINV SHALL consume materially applicable BPRD Contracts or governed Product and Product Variant evidence for inventory-bearing Resource association, publication and structural-sellability separation, and stale-reference handling. It SHALL NOT transfer Product authority or infer Stock, Price, or final purchasability from Product state.

Contextual Authorization SHALL remain with the Domain owning the affected Resource, action, property, association, and current state. Inventory SHALL retain Inventory-specific Authorization. Identity evidence, Customer context, Account association, Product association, Role labels, client Claims, or UI state alone SHALL NOT grant Inventory authority.

This decision does not establish Category or Pricing backend titles, paths, scope codes, decompositions, or positions. It does not establish Search and Discovery, Cart, Checkout, or another later backend position. Every Backend Specification title, path, scope code, decomposition, and ordering position after BINV remains unresolved until separately governed.

Only after ADR-0005 is Accepted and canonical synchronization is complete would this decision authorize drafting BINV as an Approved-governance downstream Specification. It would not itself approve the future BINV Specification.

## Authority Boundary

BINV would remain subordinate to governing sources, Approved Business Requirements, the Approved Inventory Domain, BEB, and materially applicable BIDN, BCUS, and BPRD Contracts or evidence. It would not become repository-wide authority.

Inventory would retain authority only for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, explicitly governed availability state, Overselling protection, and Inventory-owned integrity and reconciliation outcomes.

Product would retain Product and Product Variant identity, content, Product Media, publication, visibility, and structural sellability. Category would retain Category identity, hierarchy, taxonomy, classification, navigation eligibility, and Category-side membership policy. Pricing would retain Price, Discount, Promotion, Voucher, Money, Currency, tax-calculation, and Pricing Rule authority.

Search and Discovery would retain search request, result, ranking, indexing, recovery, and reconciliation semantics while remaining non-authoritative for Inventory facts. A Search representation would not become current Available-to-Sell or prove Stock.

Cart would retain shopping intent and would not create or prove Stock Reservation. Checkout would retain purchase orchestration and would consume current Inventory outcomes without fabricating them. Order would retain authoritative Order truth and historical commercial records. Payment would retain Payment Authorization, Capture, Settlement, Refund, and provider outcome truth. Shipping and Fulfilment would retain Shipment, Carrier, tracking, delivery, and fulfilment truth.

Administration would retain Staff workflow coordination without becoming authoritative for Inventory truth. Return and Refund, CMS, Notifications, Reporting and Analytics, and every other Domain would retain their governed authority. Their Projections, requests, or operational convenience would not overwrite Inventory truth.

## Dependency Analysis

### Upstream prerequisites

- BEB is Approved and supplies the shared backend obligations that BINV must inherit where materially applicable.
- BIDN is Approved and supplies trusted Identity evidence where protected Inventory operations require it, without owning Inventory Authorization.
- BCUS is Approved and supplies governed Customer or Account context where materially applicable, without establishing Inventory state or policy.
- BPRD is Approved and supplies the Product and Product Variant identity required for correct inventory-bearing Resource association, without establishing availability.

These prerequisites are sufficient to specify the Inventory backend boundary without requiring an Approved Category or Pricing backend Contract. Inventory does not need Category classification to own Inventory truth, and it must not use Price to infer availability or scarcity policy.

### Downstream dependencies reduced

- Search and Discovery may consume non-authoritative Inventory representations while requiring authoritative revalidation for commitment.
- Cart may present Inventory outcomes but cannot reserve Stock or treat a presentation as authoritative.
- Checkout requires current availability and governed Stock Reservation outcomes.
- Order, Payment, and Shipping and Fulfilment may coordinate Inventory effects only through later governed workflows and Contracts.
- Administration may invoke authorized Inventory capabilities without replacing Inventory authority.
- Reporting may consume Inventory evidence without becoming Inventory truth.

Selecting BINV reduces uncertainty for these later decisions by establishing the Inventory-owned side of their boundaries. It does not define their Contracts, choreography, order, or mechanisms.

### Remaining independent prerequisites

Pricing remains independently required before later Cart and Checkout backend boundaries can be considered fully supported. Category remains independently important for catalogue navigation and Search. Selecting Inventory next does not establish that Pricing or Category follows BINV, and does not imply a relative order between them.

## Alternatives Considered

### A. Category Next

Not selected for the immediate position. Category has a coherent Approved authority boundary, and BPRD now supplies the Product-side association needed for Category membership. It is a valid future candidate. Its strongest immediate downstream effect is catalogue navigation and Search and Discovery, while Inventory supplies trust-critical outcomes to a broader transaction-integrity chain. This decision does not determine Category's later title, path, scope code, decomposition, or position.

### B. Pricing Next

Not selected for the immediate position. Pricing has a coherent Approved authority boundary, BPRD supplies its Product association, and BCUS can supply governed Customer eligibility context. Pricing is a direct prerequisite for Cart, Checkout, Order commercial snapshots, and Payment amount context. Inventory is selected because its authoritative availability, reservation, concurrency, and Overselling outcomes constrain more of the operational commitment and fulfilment chain while requiring fewer unresolved commercial-policy inputs. This is not a claim that Pricing is less important and does not establish Pricing's later position.

### C. Inventory Next

Selected because its upstream BEB, Identity, Customer-context, and Product-reference prerequisites are already governed; its single-Domain authority is explicit; it can be specified without choosing unresolved policy or mechanisms; and its outcomes reduce uncertainty across Search, Cart, Checkout, Order, Shipping and Fulfilment, Administration, and Reporting.

### D. Search and Discovery Next

Not selected because Search consumes Product, Category, Pricing, and Inventory evidence. Only Product currently has an Approved domain-aligned backend specialization among those sources. Selecting Search now would risk defining projections and freshness Contracts before Category, Pricing, and Inventory backend boundaries exist, and the initial Search implementation and extraction thresholds remain an Open Architecture Decision.

### E. Cart Next

Not selected because Cart depends on Product, Pricing, Inventory, and governed Customer or guest context. BCUS and BPRD satisfy only part of that dependency set. Establishing Cart now would risk premature Pricing and Inventory Contract assumptions and could implicitly resolve guest, reservation, or commercial policy.

### F. Checkout or Another Later Commerce Capability Next

Not selected because Checkout additionally coordinates Cart, Pricing, Inventory, Shipping, Payment, and Order evidence. Order, Payment, Shipping and Fulfilment, Return and Refund, Notifications, Reporting, Administration, and other later capabilities likewise depend on upstream authoritative outcomes or unresolved providers and workflows. Selecting one now would bypass documented prerequisites or invent orchestration.

### G. Combine Category, Pricing, and Inventory or Another Multi-Domain Boundary

Not selected because Category, Pricing, and Inventory are separate Approved Domains with distinct truth, lifecycle, integrity, Authorization, and downstream responsibilities. Shared dependence on Product does not transfer authority or require a combined Backend Specification. Combining them merely to reduce the number of Specifications would create an unsupported decomposition.

### H. Leave the Roadmap Unresolved

Not selected because the Approved Inventory Domain and now-Approved Product backend establish a bounded, mechanism-neutral next specialization with extensive downstream value. Governance can select Inventory without resolving Pricing, Category, Search, or any later roadmap position. The selection remains conditional until this ADR is Accepted and canonical synchronization is complete.

## Consequences

Positive consequences include:

- a single-Domain Inventory backend proposal immediately after BPRD;
- explicit placement of a trust-critical availability and reservation boundary before later Cart, Checkout, Order, and fulfilment specializations;
- complete preservation of Product, Category, Pricing, Search, Cart, Checkout, and downstream authority;
- explicit BEB inheritance and bounded BIDN, BCUS, and BPRD consumption;
- reduced uncertainty for later Search, transaction, fulfilment, Administration, and Reporting Contracts; and
- continued independent governance of every later backend roadmap decision.

Costs and trade-offs include:

- Pricing and Category backend specializations remain unresolved despite being viable candidates;
- Cart and Checkout remain blocked on additional upstream backend governance, particularly Pricing;
- unresolved Inventory lifecycle and operational policy must remain explicit in BINV rather than being completed locally;
- later cross-Domain Contracts may require compatibility and migration planning; and
- an Accepted future decomposition change could require a superseding ADR.

## Explicit Exclusions

This decision does not decide, and does not authorize BINV to decide:

- Stock Reservation duration, hold timing, expiry interval, allocation model, finalization trigger, scheduling mechanism, or Inventory-decrement timing;
- backorder or preorder support, negative-Stock policy, Safety Stock, reorder thresholds, replenishment policy, supplier procurement, warehouse topology, location hierarchy, bins, transfers, or receiving workflow;
- low-stock, out-of-stock, or other availability-message wording, thresholds, or Customer policy;
- backup retention, production recovery objectives, recovery-point targets, recovery-time targets, or numerical recovery rules;
- Product, Product Variant, Inventory, Stock, Stock Reservation, warehouse, location, or another identifier format or generation mechanism;
- initial Product Categories, taxonomy, Category hierarchy, content or publication workflow, or Product import, export, and migration policy;
- promotion, Voucher, tax, invoicing, fraud, Pricing mechanism, commercial eligibility, or numerical Pricing policy;
- concrete API routes, HTTP operations, methods, statuses, DTOs, or payload schemas;
- database schemas, tables, columns, SQL, ORM mappings, indexes, constraints, transaction mechanisms, or migration mechanics;
- event names, schemas, payloads, topics, consumers, brokers, delivery technology, guarantees, or choreography;
- providers, hosting, infrastructure, deployment, cache, queue, messaging, or network topology;
- Identity Provider, Authentication protocol, token, Claims, Session, cookie, browser-storage, rotation, lifetime, MFA, SSO, or email-verification mechanism;
- a Role or Permission matrix;
- numerical limits, thresholds, timeouts, retries, retention periods, performance targets, SLAs, or SLOs;
- Search engine, Search index, Search provider, extraction threshold, ranking implementation, schema, or topology;
- Cart, Checkout, Order, Payment, Shipping, Fulfilment, Return, Refund, tax, promotion, fraud, guest Checkout, Wishlist, communication, support, or data-request policy;
- Category or Pricing backend title, path, scope code, decomposition, or ordering; or
- any Backend Specification title, path, scope code, decomposition, or ordering position after BINV.

## Open Decisions Preserved

This decision leaves all applicable Open Product Decisions unresolved, including:

- initial Product Categories and catalogue taxonomy;
- Product Variant and Attribute standards;
- Product-review support;
- Stock Reservation duration;
- backorder and preorder support;
- low-stock and out-of-stock Customer messaging;
- promotion and Voucher commercial policy, including application, precedence, and stacking;
- tax and invoicing policy;
- fraud-related commercial policy;
- content approval and scheduled-publication workflow;
- Product import, export, and migration requirements;
- administrative Role and Permission policy;
- unresolved returns, Refunds, gift-card, store-credit, communication, support, analytics, launch, and other Product policy; and
- every unresolved numerical Inventory or Pricing rule.

This decision also leaves all applicable Open Architecture Decisions unresolved, including:

- backend hosting;
- infrastructure as code;
- customer and administrator session/token strategy;
- Payment, Shipping, notification, or another applicable provider and integration choice;
- Redis introduction and its approved use cases;
- external messaging introduction and service selection;
- initial Search implementation and extraction thresholds;
- Product-media upload and transformation strategy;
- backup retention and production recovery objectives;
- PostgreSQL schema strategy; and
- repository-wide feature-flag implementation and lifecycle management.

An Open Decision is not permission for BINV or an implementation to choose independently. No decision listed here is answered by selecting the Inventory backend boundary.

## Acceptance and Governance Conditions

This Proposed decision becomes Accepted only after:

- Architecture review and approval of the immediate post-BPRD position, single-Domain decomposition, title, path, and scope code;
- affected Inventory ownership review confirming the proposed boundary accurately specializes the Approved Inventory Domain;
- affected Product ownership review confirming bounded BPRD consumption and preservation of Product authority;
- affected Pricing ownership review confirming Pricing remains separate and its position remains unresolved;
- affected Category ownership review confirming Category remains separate and its position remains unresolved;
- affected Search and Discovery ownership review confirming Search remains separate, consumes Inventory only through governed evidence, and retains an unresolved backend position;
- affected Identity ownership review confirming bounded BIDN consumption and preservation of Identity authority;
- affected Customer ownership review confirming bounded BCUS consumption and preservation of Customer and Account authority;
- confirmation that `BINV` is unique among current Specification scope codes;
- confirmation that `specifications/backend/inventory/inventory-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that every Backend Specification title, path, scope code, decomposition, and ordering position after BINV remains unresolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` before ADR-0005 becomes Accepted.

No review listed above is represented as completed by this Proposed ADR.

BINV MUST NOT be drafted as an Approved-governance downstream Specification until ADR-0005 is Accepted and canonical synchronization is complete.

If ADR-0005 is later Accepted, governance must synchronize only:

- ADR-0005 from `Proposed` to `Accepted`, with lifecycle-dependent wording updated without changing decision semantics;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History to establish BINV as the fourth downstream Backend Specification after BEB, immediately after BIDN, BCUS, and BPRD, while preserving every later roadmap position as unresolved; and
- `DECISIONS.md` metadata, Decision Index, and Revision History to record ADR-0005 as Accepted.

No `PRODUCT.md` change is required because this decision preserves Inventory and all other Domain authority and leaves applicable Product Decisions unresolved. If acceptance review identifies a direct Product contradiction, acceptance must stop until that contradiction is governed; this ADR does not authorize editing or overriding `PRODUCT.md`.

## Security Impact

This decision introduces no security policy or mechanism. A future BINV Specification would inherit materially applicable BEB security obligations, consume trusted BIDN evidence where required, enforce Inventory-owned contextual Authorization at trusted boundaries, preserve Resource isolation, protect Sensitive Data and Secrets, and leave Identity mechanisms and access matrices unresolved.

## Data Impact

This decision creates no schema, table, index, SQL, persistence mapping, identifier design, storage mechanism, cache, migration, event store, warehouse model, Stock formula, reservation mechanism, or data movement. Inventory, Product, Customer, Pricing, Category, Search, Order, Payment, and Shipping data authority remain with their Approved Domains.

## Compatibility and Migration Impact

This decision creates no runtime, data, API, event, Inventory, Product, Pricing, Cart, Checkout, Order, Payment, or Shipping migration. A future BINV Specification would inherit materially applicable BEB compatibility obligations. Concrete Contract versions, compatibility windows, migrations, rollback, reconciliation, and recovery mechanisms require later governed Contracts or designs.

## Operational Impact

This decision creates no runtime operational behavior, provider commitment, infrastructure topology, deployment design, cache, message broker, support process, capacity target, threshold, timeout, retry count, retention period, SLA, SLO, recovery objective, reservation duration, or replenishment process. A future BINV Specification may specialize governed Inventory observability, audit, failure, recovery, and reconciliation outcomes without selecting unresolved mechanisms or numerical values.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0002-identity-access-backend-specification.md`
- `specifications/adr/ADR-0003-customer-account-backend-specification.md`
- `specifications/adr/ADR-0004-catalogue-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Supersedes

—

## Superseded By

—

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Proposed | Proposed the single-Domain Inventory Backend Specification as the fourth downstream Backend Specification after BEB, immediately after BIDN, BCUS, and BPRD, while preserving Category, Pricing, Search and Discovery, and every later backend roadmap decision. |
