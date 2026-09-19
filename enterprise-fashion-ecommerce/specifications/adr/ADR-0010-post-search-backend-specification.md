# ADR-0010 — Post-Search Backend Specification

## Identifier

ADR-0010

## Title

Post-Search Backend Specification

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

Accepted ADR-0001 through ADR-0009 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH`. Approved BSRCH establishes no position after itself.

The completed post-BSRCH eligibility audit found exactly two independently eligible capabilities: Checkout and CMS. It found no unique next capability and did not select or rank either candidate. Order, Payment, Shipping and Fulfilment, Return, Administration, Reporting, and Notifications were not independently eligible because concrete governed upstream backend boundaries remain missing.

Checkout can now be specialized from Approved Cart, Product, Inventory, Pricing, Identity, Customer, Category, and Search backend boundaries. Approved Payment, Order, and Shipping and Fulfilment Domain Specifications preserve their downstream authority without requiring those backend specifications to be selected first.

CMS is also independently eligible from governed Product and Category authority. It remains a legitimate separate candidate that does not require a Search or Administration backend prerequisite. This Proposed ADR selects between the two eligible candidates without claiming that either is objectively superior.

## Decision Drivers

- Canonical governance establishes no backend position after BSRCH.
- The eligibility audit identified Checkout and CMS, and only those two capabilities, as independently eligible.
- Approved BCART provides governed Cart identity, contents, lifecycle, and intent evidence.
- Approved BPRD, BINV, and BPRC provide Product, Inventory, and Pricing evidence required for Checkout revalidation.
- Approved BIDN and BCUS provide bounded Identity, Customer, Account, Address, Consent, and Preference evidence where applicable.
- Approved BCAT and BSRCH preserve Category and discovery boundaries without becoming Checkout authority.
- Approved Payment, Order, and Shipping and Fulfilment Domains define downstream authority that Checkout may hand off to without absorbing it.
- Order requires a governed Checkout-to-Order backend handoff.
- Payment requires an authorized coordinating Checkout backend handoff.
- Shipping and Fulfilment requires a governed Checkout delivery-selection and rate handoff in addition to later Order handoffs.
- One explicit Architecture decision is required to resolve the immediate ambiguity while preserving every later roadmap position.

## Candidate Analysis

### Checkout

Checkout is independently eligible. Current Approved backend boundaries supply the authoritative Cart, Product, Inventory, Pricing, Identity, Customer, Category, and Search evidence needed to specialize the Approved Checkout Domain without transferring their authority.

Checkout owns purchase-orchestration coordination, correlation, progression, submission integrity, failure, uncertainty, recovery, and bounded handoff outcomes. Payment retains Payment truth, Order retains durable Order truth, and Shipping and Fulfilment retains delivery, fulfilment, Shipment, and provider-evidence truth. Their Approved Domain Specifications are sufficient to preserve these downstream authority boundaries while their future backend identities remain unresolved.

Selecting Checkout closes a governed boundary required by several currently blocked capabilities: Checkout-to-Order creation, authorized Payment initiation, and Checkout delivery-selection or rate coordination. This is a governance rationale for one legitimate selection, not a ranking of business value or technical merit.

### CMS

CMS is independently eligible. Governed Product and Category authority is sufficient to specialize CMS-owned content identity, versioning, readiness, publication evidence, placement, history, recovery, and reconciliation without requiring Search or Administration backend specifications first.

CMS could legitimately have been selected. It remains separate, independently eligible, unresolved, and unordered relative to every still-unselected post-BCHK capability. This ADR does not characterize CMS as lower priority, less important, less valuable, harder, easier, later by necessity, or technically inferior.

### Comparative Governance Choice

Both candidates satisfy the eligibility test. Checkout is selected because doing so establishes a missing governed handoff boundary explicitly required by Order, Payment, and Shipping and Fulfilment specialization. The selection records one permissible roadmap choice; it does not prove that Checkout is the only valid candidate or rank it above CMS.

## Decision

The ninth downstream Backend Specification after BEB, immediately after BSRCH, SHALL be:

- **Capability:** Checkout
- **Specification:** Checkout Backend Specification
- **Path:** `specifications/backend/checkout/checkout-backend.md`
- **Scope code:** `BCHK`
- **Decomposition:** Checkout-only; specializes exactly the Approved Checkout Domain
- **Position:** Ninth downstream Backend Specification after BEB, immediately after BSRCH

`BCHK` means Backend Checkout. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

BCHK SHALL inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It SHALL specialize only the Approved Checkout Domain and SHALL consume governed evidence or Contracts without acquiring source- or downstream-Domain authority.

BCHK MAY consume materially applicable governed evidence from BIDN, BCUS, BPRD, BINV, BPRC, BCART, BCAT, BSRCH, and other Approved sources where Checkout authority requires it. Identity, Customer, Account, Address, Product, Product Variant, Category, Pricing, Inventory, Cart, Search, CMS, Administration, Payment, Order, Shipping and Fulfilment, Return, Notifications, and Reporting authority SHALL remain with their owners.

BCHK MAY later define bounded Checkout-owned handoff obligations to Payment, Order, and Shipping and Fulfilment, but MUST NOT establish their backend titles, paths, scope codes, decompositions, implementation mechanisms, or later roadmap positions.

This decision proposes only `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK`. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BCHK remains unresolved.

Because ADR-0010 remains Proposed, BCHK MUST NOT be drafted under Approved governance until this ADR is Accepted and synchronized canonical Architecture and Decision changes are merged. Acceptance would authorize a BCHK Draft only; it would not approve BCHK.

## Authority Boundary

- **BEB:** BCHK must inherit and trace every materially applicable shared backend obligation.
- **BIDN:** trusted Principal and Authentication evidence may support protected Checkout behavior; Identity, credentials, Sessions, tokens, and Authentication authority do not transfer.
- **BCUS:** governed Customer, Account, Address, Consent, Preference, and ownership evidence may be consumed where applicable; Customer and Account authority do not transfer.
- **BPRD:** Product and Product Variant identity, lifecycle, eligibility, content, and Product Media evidence may be revalidated; Product authority does not transfer.
- **BINV:** current Inventory and Stock Reservation outcomes may be requested or consumed; Stock, Available-to-Sell, reservation lifecycle, commitment, and Inventory authority do not transfer.
- **BPRC:** current commercial outcomes may be requested or consumed; Price, Discount, Promotion, Voucher, Tax, Money, Currency, and Pricing authority do not transfer.
- **BCART:** governed Cart intent may enter Checkout; Cart identity, contents, lifecycle, mutation, and totals authority do not transfer.
- **BCAT:** Category evidence may constrain Product context where governed; taxonomy, hierarchy, membership, navigation, ordering, and Category authority do not transfer.
- **BSRCH:** Search results are non-authoritative discovery evidence and do not prove Checkout readiness or purchasability.
- **Payment:** Payment initiation is a bounded handoff; Payment, provider evidence, authorization, Capture, Void, Settlement, Refund, Chargeback, and financial outcome authority remain Payment-owned.
- **Order:** Order creation is a bounded handoff; Order identity, creation truth, snapshots, lifecycle, status, and history remain Order-owned.
- **Shipping and Fulfilment:** delivery choices and rates are governed inputs; fulfilment, Shipment, Dispatch, tracking, carrier, delivery, and provider evidence remain Shipping-owned.
- **CMS:** policy presentation or content evidence may be consumed only where governed; CMS remains independently eligible, separate, and unresolved.
- **Administration:** protected support or recovery invokes Checkout-owned Use Cases through contextual Authorization and transfers no Checkout authority.
- **Notifications and Reporting:** derived communication and analytical evidence remain non-authoritative and cannot establish Checkout or downstream truth.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, a supplied identifier, client state, Role label, Permission, Claim, Scope, Search result, or Cart possession MUST NOT independently establish Checkout authority.

## Dependency and Eligibility Traceability

| Capability | Current governed evidence | BCHK relationship | Authority retained outside BCHK |
| --- | --- | --- | --- |
| BEB | Approved | Inherited baseline | Shared backend governance |
| BIDN | Approved | Trusted actor evidence | Identity and Authentication |
| BCUS | Approved | Customer, Account, Address, Consent, and Preference evidence | Customer and Account truth |
| BPRD | Approved | Product/Product Variant revalidation | Product truth |
| BINV | Approved | Inventory and reservation coordination evidence | Inventory truth |
| BPRC | Approved | Commercial revalidation evidence | Pricing truth |
| BCART | Approved | Cart-intent handoff | Cart truth |
| BCAT | Approved | Category context where applicable | Category truth |
| BSRCH | Approved | Non-authoritative discovery boundary | Search truth and derived state |
| Payment Domain | Approved | Future bounded initiation and outcome handoff | Payment truth; backend identity unresolved |
| Order Domain | Approved | Future bounded creation handoff | Order truth; backend identity unresolved |
| Shipping and Fulfilment Domain | Approved | Future bounded delivery-choice/rate handoff | Shipping truth; backend identity unresolved |
| CMS Domain | Approved | Separate eligible alternative and conditional content source | CMS truth; backend identity unresolved |
| Administration Domain | Approved | Consumer of protected Checkout Use Cases | Administrative coordination |
| Notifications Domain | Approved | Conditional downstream consumer | Delivery truth |
| Reporting Domain | Approved | Conditional downstream consumer | Reporting definitions and Projections |

The canonical post-BSRCH eligibility audit is evidence for the two-candidate set, not authority for this selection. ADR-0010 supplies the Architecture governance decision selecting Checkout.

## Consequences

Established consequences if this ADR is Accepted and synchronized include:

- BCHK becomes the authorized immediate Backend Specification after BSRCH;
- a future BCHK Draft can specialize Checkout using governed upstream evidence and bounded downstream Domain authority;
- the Checkout-to-Order, Checkout-to-Payment, and Checkout-to-Shipping handoff boundaries can be specified without transferring downstream authority;
- CMS remains independently eligible, separate, unresolved, and unranked;
- Payment, Order, Shipping and Fulfilment, and every other capability retain their authority; and
- every post-BCHK roadmap identity and position remains unresolved.

Trade-offs include:

- CMS remains unresolved despite being independently eligible;
- selecting BCHK does not itself govern any downstream backend Contract or implementation;
- another governance decision may be required after BCHK; and
- Checkout Product policy and Architecture mechanisms remain open.

These consequences are governance effects, not statements of candidate priority, value, cost, difficulty, urgency, or technical superiority.

## Alternatives Considered

### A. CMS

CMS was independently eligible and could legitimately have been selected. It remains unresolved, separate, and unordered relative to every still-unselected post-BCHK capability. Deferral does not rank CMS below Checkout or imply that it must follow any particular future capability.

### B. Defer the Decision

Not selected because deferral would preserve the two-candidate ambiguity and prevent establishment of the next canonical backend position. Deferral remains a valid governance option but would establish no position after BSRCH.

No other candidate was treated as independently eligible by the post-BSRCH audit.

## Explicit Non-Decisions

ADR-0010 does not create BCHK Requirements or `checkout-backend.md`; select Checkout lifecycle states or persistence; define API routes, methods, statuses, DTOs, schemas, database structures, Integration Event names or payloads, topics, brokers, transports, provider Contracts, Redis, caches, hosting, infrastructure, deployment topology, retry counts, timeouts, retention, performance targets, recovery objectives, or numerical operational values; select Payment or Shipping providers; define Payment, Order, Shipping, fraud, tax, Cart, Pricing, Inventory, Customer, Identity, CMS, Administration, Notifications, Reporting, or other external policy; or establish any post-BCHK roadmap position.

The decision does not select Order, Payment, Shipping and Fulfilment, CMS, Return, Administration, Reporting, Notifications, or another capability after BCHK. No dependency described here is an implied roadmap order.

## Open Product Decisions

This Proposed decision preserves the following **18 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Checkout Domain §29:

| Item | Open Product Decision | Preserved BCHK boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No actor model or mandatory Account rule is selected. |
| 5 | Customer email-verification requirements. | No verification prerequisite or mechanism is selected. |
| 6 | Initial payment methods and provider. | No Payment method, provider, or provider Contract is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, eligibility, or fee policy is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or Pricing/Promotion treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, calculation, invoice, or document rule is selected. |
| 12 | Stock Reservation duration. | No reservation duration, expiry, or timing value is selected. |
| 13 | Back-order and pre-order support. | No unavailable-item progression or availability policy is selected. |
| 14 | Voucher and promotion stacking policy. | No stacking, precedence, or combination policy is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No message, threshold, display, or eligibility rule is selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, service expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No Consent or Preference model is selected or inferred from purchase activity. |
| 20 | Initial analytics provider and event taxonomy. | No provider, taxonomy, or measurement mechanism is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, process, or service target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, or financial policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, blocking, review, provider, or intervention policy is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, or commercial treatment is selected. |

No listed Product Decision is resolved by selecting BCHK.

## Open Architecture Decisions

This Proposed decision preserves the following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BCHK boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment provider or provider Contract is selected. |
| 6 | Shipping provider selection. | No Shipping provider or provider Contract is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or orchestration-state use is selected. |
| 9 | External messaging introduction and service selection. | No messaging adoption, service, broker, topic, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Checkout persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No feature-flag mechanism, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BCHK. Frontend hosting, transactional notification provider selection, initial Search implementation, and Product-media strategy remain unresolved but are not materially required for this Checkout-only roadmap decision.

## Required Governance Reviews

This Proposed decision becomes Accepted only after:

- Architecture review of the immediate post-BSRCH selection, Checkout-only decomposition, title, path, and scope code;
- affected Checkout ownership review confirming accurate Domain specialization;
- affected Cart ownership review confirming bounded BCART handoff and preserved Cart authority;
- affected Product and Product Catalogue ownership review confirming bounded BPRD consumption and preserved Product/Product Variant authority;
- affected Pricing ownership review confirming bounded BPRC revalidation and preserved Pricing authority;
- affected Inventory ownership review confirming bounded BINV revalidation and reservation coordination without Inventory authority transfer;
- affected Identity ownership review confirming bounded BIDN evidence and no Authentication, Session, or token mechanism;
- affected Customer ownership review confirming bounded BCUS Customer, Account, Address, Consent, and Preference evidence;
- affected Category ownership review confirming Category authority remains separate;
- affected Search and Discovery ownership review confirming BSRCH evidence remains non-authoritative and does not prove Checkout readiness;
- affected Payment ownership review confirming bounded future initiation/outcome handoffs and preserved Payment authority;
- affected Order ownership review confirming bounded future creation handoff and preserved Order authority;
- affected Shipping and Fulfilment ownership review confirming bounded future delivery-choice/rate handoffs and preserved Shipping authority;
- affected CMS ownership review confirming CMS remains independently eligible, separate, unresolved, and unranked;
- affected Administration ownership review confirming protected support/recovery transfers no Checkout authority and defines no Role/Permission matrix;
- affected Notifications ownership review confirming downstream communication creates no Checkout or source truth;
- affected Reporting ownership review confirming downstream analytics and Projections remain non-authoritative;
- confirmation that `BCHK` is unique among current Specification scope codes;
- confirmation that `specifications/backend/checkout/checkout-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BCHK is established; and
- preparation of synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` as part of acceptance.

No review is represented as completed, and no reviewer names, signatures, tickets, dates beyond this record's date, or external evidence are asserted.

## Acceptance Conditions

ADR-0010 may become Accepted only when:

- governance approves BCHK as the immediate Backend Specification after BSRCH;
- review confirms BCHK remains Checkout-only and preserves every source and downstream authority;
- review confirms CMS remains independently eligible, separate, unresolved, and unranked;
- review confirms every post-BCHK roadmap identity and position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized through BCHK;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0010 as Accepted; and
- ADR-0010 lifecycle wording records the Accepted decision consistently.

No `PRODUCT.md` change is required unless acceptance review discovers a direct contradiction. BCHK MUST NOT be drafted under Approved governance until Accepted ADR-0010 and synchronized canonical changes are merged. Acceptance authorizes drafting only; it does not approve BCHK.

## Security, Data, Compatibility, and Operational Impact

This Proposed decision creates no runtime behavior, API, DTO, schema, database structure, migration, event, provider, cache, infrastructure, deployment, configuration, or operational target. A future BCHK Specification must inherit materially applicable BEB security, Authorization, privacy, compatibility, migration, boundedness, observability, audit, failure, recovery, reconciliation, idempotency, replay, and verification obligations without selecting unresolved mechanisms.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0007-post-pricing-backend-specification.md`
- `specifications/adr/ADR-0008-post-cart-backend-specification.md`
- `specifications/adr/ADR-0009-post-category-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/backend/search/search-backend.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-19 | Proposed | Proposed Checkout-only BCHK as the immediate post-BSRCH Backend Specification while preserving CMS as independently eligible, separate, unresolved, and unranked and leaving every post-BCHK roadmap position unresolved. |
