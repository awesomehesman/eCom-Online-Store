# ADR-0012 — Post-Order Backend Specification

## Identifier

ADR-0012

## Title

Post-Order Backend Specification

## Version

0.1.0

## Status

Proposed

## Date

2026-09-21

## Last Updated

2026-09-21

## Owner

Architecture

## Authoritative

false

## Context

Accepted ADR-0001 through ADR-0011 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD`. The canonical BORD Specification is now `1.0.0 Approved`, and no backend position after BORD is governed.

The completed post-BORD eligibility audit found exactly three independently eligible capabilities: CMS, Payment, and Shipping and Fulfilment. It found no unique next capability and did not select or rank them. Return still lacks governed Payment and Shipping backend Contracts; Administration, Notifications, and Reporting still lack materially required owning-source backend Contracts.

CMS was independently eligible before BORD and remains independently eligible. Payment was independently eligible before BORD and remains independently eligible. Shipping and Fulfilment became independently eligible specifically because Approved BORD now supplies the governed Order and Order Item backend handoff that was missing after BCHK. This Proposed decision selects Shipping and Fulfilment through governance dependency closure: it closes the dependency chain intentionally opened by ADR-0011's selection of Order. It does not claim that Shipping and Fulfilment has greater technical merit, business value, priority, convenience, or implementation advantage than CMS or Payment.

`ARCHITECTURE.md` §35 still states that BORD is authorized to enter Draft lifecycle but is not Approved. That lifecycle statement is stale and must be corrected during acceptance synchronization; this Proposed ADR does not modify `ARCHITECTURE.md`.

## Decision Drivers

- Canonical governance establishes no backend position after BORD.
- CMS, Payment, and Shipping and Fulfilment are independently eligible, so eligibility alone does not determine the next position.
- Approved BORD supplies governed Order identity, Order Item, historical snapshot, fulfilment-handoff, and Order/Shipping separation evidence.
- That BORD boundary closes the specific prerequisite that prevented Shipping and Fulfilment from being independently eligible after BCHK.
- Selecting Shipping and Fulfilment closes the dependency chain intentionally opened by ADR-0011 without selecting Return or another later capability.
- CMS and Payment must remain independently eligible, separate, unresolved, unranked, and unordered.
- One immediate boundary must be selected without predetermining any later roadmap position.

## Candidate Analysis

### CMS

CMS was independently eligible before BORD and remains independently eligible from governed Product, Category, Identity, Customer, Search, and other Approved authority. It could legitimately occupy the immediate post-BORD position. Deferral is not rejection or ranking; CMS remains separate, unresolved, unranked, and unordered relative to Payment and every other unselected capability.

### Payment

Payment was independently eligible before BORD and remains independently eligible from BCHK's governed initiation and correlation boundary and other Approved upstream authority. A Payment backend Contract would close one remaining Return dependency. Deferral is not rejection or ranking; Payment remains separate, unresolved, unranked, and unordered relative to CMS and every other unselected capability.

### Shipping and Fulfilment

Shipping and Fulfilment is independently eligible because Approved BORD now supplies the governed Order and Order Item handoff required by the Approved Shipping and Fulfilment Domain. Approved BCHK, BINV, BPRC, BCUS, BIDN, and other upstream specifications supply bounded delivery, Inventory, commercial, Customer, and Identity evidence without transferring their authority.

Shipping and Fulfilment is selected because it is the capability newly unblocked by completion of the immediately preceding BORD roadmap position. This closes the governance dependency chain intentionally opened by ADR-0011. It does not rank Shipping and Fulfilment above CMS or Payment by technical merit, business value, effort, priority, convenience, or preference.

## Decision

ADR-0012 proposes that the eleventh downstream Backend Specification after BEB, immediately after BORD, SHALL be:

- **Capability:** Shipping and Fulfilment
- **Specification:** Shipping and Fulfilment Backend Specification
- **Path:** `specifications/backend/shipping/shipping-backend.md`
- **Scope code:** `BSHP`
- **Decomposition:** Shipping-and-Fulfilment-only; specializes exactly the Approved Shipping and Fulfilment Domain
- **Position:** Eleventh downstream Backend Specification after BEB, immediately after BORD

`BSHP` means Backend Shipping and Fulfilment. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

If Accepted and canonically synchronized, BSHP SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved Shipping and Fulfilment Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

The resulting canonical sequence would be `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP`.

BSHP is the sole roadmap position proposed by ADR-0012. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BSHP remains unresolved. Proposed ADR-0012 does not authorize BSHP drafting. If ADR-0012 becomes Accepted and canonical `ARCHITECTURE.md` and `DECISIONS.md` synchronization is complete, BSHP may enter Draft lifecycle only; that governance action will not approve BSHP, which must complete its own Draft-to-Approved lifecycle.

## Authority and Decomposition Boundaries

- **Shipping and Fulfilment:** BSHP may specialize Shipping-owned quotation evidence, delivery choices, fulfilment coordination, Shipment identity and lifecycle, tracking, delivery evidence, exceptions, recovery, reconciliation, and other behavior already governed by the Approved Shipping and Fulfilment Domain. It must not expand that Domain's authority.
- **BEB:** BSHP must inherit and explicitly trace every materially applicable BEB Requirement without duplicating or weakening shared backend governance.
- **BIDN:** trusted Principal and Authentication evidence may be consumed where materially applicable; Identity, credentials, Sessions, tokens, and Authentication authority do not transfer.
- **BCUS:** governed Customer, Account, Address, Consent, Preference, and ownership evidence may be consumed where materially applicable; Customer and Account authority do not transfer.
- **BPRD and BCAT:** Product, Product Variant, and Category evidence may be consumed where materially applicable; catalogue authority does not transfer.
- **BINV:** Inventory outcomes may be consumed for bounded fulfilment coordination; Stock, Stock Reservation, Stock Adjustment, Stock Movement, Stock Location, availability, and overselling authority do not transfer.
- **BPRC:** governed commercial and Shipping Rate evidence may be exchanged where materially applicable; final Pricing, Promotion, Discount, Tax, fee calculation, Currency conversion, and commercial-total authority do not transfer.
- **BCART and BCHK:** purchase-intent and delivery-choice evidence may be consumed where materially applicable; Cart and Checkout identity, lifecycle, validation, orchestration, and success authority do not transfer.
- **BORD:** governed Order and Order Item context and fulfilment handoff evidence may be consumed; Order identity, creation, lifecycle, history, snapshots, cancellation, and commercial history authority do not transfer.
- **Payment:** Payment truth remains Payment-owned. Shipment, fulfilment, tracking, or delivery state must not establish Payment Authorization, Capture, Settlement, Refund, or other Payment outcome.
- **Return:** Return eligibility, authorization, lifecycle, inspection, disposition, and Refund policy remain Return-owned. BSHP does not create a reverse-logistics implementation or make Return automatically next.
- **CMS, Administration, Notifications, and Reporting:** content, administrative coordination, communication delivery, and analytical representation remain with their owning Domains and cannot establish or mutate Shipping truth.
- **Search:** Search and Discovery remains separate and its indexes, documents, rankings, and results remain non-authoritative representations.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, identifiers, Role labels, Permissions, Claims, Order state, Payment state, or derived representations must not independently establish Shipping or Fulfilment authority.

## Dependency Consequences

Approved BORD closes the governed Order and Order Item handoff that previously blocked authority-safe Shipping and Fulfilment specialization. Approved BCHK supplies bounded delivery-choice context; BINV supplies Inventory evidence; BPRC preserves commercial authority; and the remaining Approved upstream backends supply bounded identity, customer, catalogue, cart, category, and search evidence.

Selecting BSHP would close Shipping and Fulfilment's immediate roadmap ambiguity but would not make Return automatically eligible or next. Return would still depend on an unresolved Payment backend Contract in addition to any future Approved BSHP Contract. Administration, Notifications, and Reporting would remain dependent on other unresolved owning-source backend Contracts.

## Considered Independently Eligible Alternatives

### A. CMS

CMS is independently eligible and could legitimately be selected. It remains separate, unresolved, unranked, and unordered. Deferring it establishes no later CMS position.

### B. Payment

Payment is independently eligible and could legitimately be selected. It remains separate, unresolved, unranked, and unordered. Deferring it establishes no later Payment position.

### C. Shipping and Fulfilment

Shipping and Fulfilment is selected solely through governance dependency closure because Approved BORD newly closes its previously missing governed Order and Order Item backend handoff. This rationale expresses no superiority or priority over CMS or Payment.

### D. Defer the Decision

Deferral would preserve the three-candidate ambiguity and establish no position after BORD. It remains a valid governance option but would not close the dependency chain intentionally opened by ADR-0011.

## Open Product Decisions

This Proposed decision preserves the following **6 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Shipping and Fulfilment Domain §39:

| Item | Open Product Decision | Preserved BSHP boundary |
| ---: | --- | --- |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, Carrier, service level, delivery area, eligibility, or fee policy is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or commercial treatment is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, timing, or fulfilment-stop policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return eligibility, exchange, Refund, or reverse-logistics policy is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No support channel, escalation owner, route, workflow, expectation, or target is selected. |

No listed Product Decision is resolved by selecting BSHP.

## Open Architecture Decisions

This Proposed decision preserves the following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BSHP boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 6 | Shipping provider selection. | No Shipping Provider, Carrier, integration, or provider Contract is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, tracking, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No messaging adoption, service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention period, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Shipping persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BSHP. Frontend hosting, Payment provider selection, initial Search implementation, and Product-media upload and transformation remain unresolved but are not materially required for this Shipping-and-Fulfilment-only roadmap decision.

## Required Governance Reviews

Before ADR-0012 may become Accepted, governance must record completion of:

- Architecture review of the immediate post-BORD selection, Shipping-and-Fulfilment-only decomposition, title, path, scope code, and dependency-closure rationale;
- affected Shipping and Fulfilment ownership review confirming accurate Domain specialization;
- affected Order ownership review confirming bounded BORD handoff and preserved Order authority;
- affected Checkout ownership review confirming bounded BCHK delivery context and preserved Checkout authority;
- affected Inventory ownership review confirming bounded BINV evidence and preserved Inventory authority;
- affected Pricing ownership review confirming bounded BPRC evidence and preserved Pricing authority;
- affected Customer ownership review confirming bounded BCUS Address and Customer evidence and preserved Customer/Account authority;
- affected Identity ownership review confirming bounded BIDN evidence and no Authentication, Session, or token mechanism;
- affected Product and Product Catalogue ownership review confirming bounded BPRD evidence and preserved Product/Product Variant authority;
- affected Cart ownership review confirming bounded BCART evidence and preserved Cart authority;
- affected Payment ownership review confirming Payment remains external and no Payment outcome or provider is selected;
- affected Return ownership review confirming reverse logistics and Return policy remain external, Return does not automatically follow BSHP, and the unresolved Payment backend dependency remains explicit;
- affected Administration ownership review confirming protected operational actions transfer no Shipping authority and define no Role/Permission matrix;
- affected Notifications ownership review confirming communications remain non-authoritative and no notification provider or event Contract is selected;
- affected Reporting ownership review confirming analytical representations remain non-authoritative and no reporting definition is selected;
- Security review of contextual Authorization, Sensitive Data, provider evidence, duplicate effects, uncertainty, and recovery boundaries;
- Testing review of future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, traceability, cross-references, and roadmap containment;
- confirmation that `BSHP` is unique among current Specification scope codes;
- confirmation that `specifications/backend/shipping/shipping-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered, with no ordering between them;
- confirmation that Return, Administration, Notifications, and Reporting remain separate and unresolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BSHP is established; and
- synchronized canonical updates to ADR-0012, `ARCHITECTURE.md`, and `DECISIONS.md` upon acceptance.

No reviewer names, signatures, tickets, dates beyond this record's governed date, or external evidence are asserted while this ADR remains Proposed.

## Acceptance Conditions and Synchronization

ADR-0012 may become Accepted only after:

- governance approves BSHP as the immediate Backend Specification after BORD;
- required reviews confirm Shipping-and-Fulfilment-only specialization and preservation of every external authority;
- review confirms the dependency-closure rationale without ranking CMS, Payment, or Shipping and Fulfilment by technical or business merit;
- review confirms CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered relative to each other;
- review confirms Return still depends on the unresolved Payment backend boundary and is not automatically next;
- review confirms every post-BSHP roadmap position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized through BSHP and the stale statement that Approved BORD is still Draft/unapproved is corrected;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0012 as Accepted; and
- ADR-0012 lifecycle wording is synchronized to the Accepted decision.

Acceptance synchronization must modify only ADR-0012, `ARCHITECTURE.md`, and `DECISIONS.md` unless acceptance review discovers a direct contradiction requiring separately governed correction. No `PRODUCT.md` change is currently required.

## Explicit Non-Decisions

ADR-0012 does not create BSHP Requirements or `shipping-backend.md`; select a Shipping Provider, Carrier, service level, delivery area, delivery fee policy, free-delivery threshold, fulfilment strategy, warehouse strategy, Shipment identifier format, tracking provider or mechanism, Return or reverse-logistics implementation, API route, HTTP method or status, DTO or payload, database schema, table, column, index, ORM mapping, event name or schema, topic, queue, broker, messaging technology, provider, cache or Redis use, infrastructure product, hosting model, deployment topology, retry count, timeout, retention value, rate limit, performance target, SLA, SLO, recovery objective, or other numerical value.

It does not resolve Pricing, Promotion, Discount, Tax, Payment, Refund, Return, cancellation, Inventory, fulfilment, warehouse, delivery, fraud, support, notification, reporting, or other Product policy. It defines no Role or Permission matrix and no concrete Authorization mechanism.

## Consequences

If Accepted and synchronized:

- BSHP will be authorized to enter Draft lifecycle as the immediate Backend Specification after BORD;
- a future BSHP Draft may specialize Shipping and Fulfilment behavior already governed by its Approved Domain and consume BORD's governed Order and Order Item handoff without transferring Order authority;
- BSHP will still require its own Draft-to-Approved lifecycle before becoming an Approved normative backend Specification;
- CMS and Payment will remain independently eligible, separate, unresolved, unranked, and unordered;
- Return will remain separate and unresolved and will not automatically follow BSHP because its Payment backend dependency remains unresolved;
- Administration, Notifications, and Reporting will remain separate and unresolved; and
- every position after BSHP will remain unresolved.

The trade-off is that independently eligible CMS and Payment remain unresolved. This is a governance consequence of dependency closure, not a ranking of value, priority, effort, convenience, or technical quality.

## Roadmap Containment

The sole proposed roadmap addition is BSHP immediately after BORD. The proposal establishes no backend identity, title, path, scope code, decomposition, or ordering position after BSHP.

CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered, with no ordering between them. Return, Administration, Notifications, and Reporting remain separate and unresolved. Search, Category, Cart, Checkout, Order, and all existing Approved backend authorities remain unchanged. Return is not stated or implied to follow BSHP.

## Validation Criteria

Before acceptance, review must verify that:

1. Shipping and Fulfilment is the sole selected capability;
2. the proposed Specification is exactly the Shipping and Fulfilment Backend Specification at `specifications/backend/shipping/shipping-backend.md` under unique scope `BSHP`;
3. the decomposition is Shipping-and-Fulfilment-only and preserves the Approved Shipping and Fulfilment Domain authority;
4. the proposed sequence ends at `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP`;
5. CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered, with no ordering between them;
6. Return remains separate and unresolved, retains its unresolved Payment backend dependency, and is not automatically eligible or next merely because BSHP is selected;
7. every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BSHP remains unresolved;
8. all 6 listed Product Decisions and 10 listed Architecture Decisions remain unresolved;
9. no implementation, provider, policy, Contract, numerical value, or later roadmap position is selected;
10. no statement incorrectly describes canonical BORD as Draft or unapproved;
11. ADR-0012 remains `0.1.0 Proposed` until required reviews and canonical synchronization are complete;
12. acceptance corrects the stale BORD lifecycle wording in `ARCHITECTURE.md` §35 without changing BORD authority or semantics;
13. whitespace and reference validation pass; and
14. ADR-0012 lifecycle and canonical synchronization changes remain limited to ADR-0012, `ARCHITECTURE.md`, and `DECISIONS.md` unless a separately governed correction is explicitly required, and no unrelated repository changes are introduced.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0011-post-checkout-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/backend/product/product-backend.md`
- `specifications/backend/inventory/inventory-backend.md`
- `specifications/backend/pricing/pricing-backend.md`
- `specifications/backend/cart/cart-backend.md`
- `specifications/backend/category/category-backend.md`
- `specifications/backend/search/search-backend.md`
- `specifications/backend/checkout/checkout-backend.md`
- `specifications/backend/order/order-backend.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-21 | Proposed | Proposed Shipping-and-Fulfilment-only BSHP immediately after BORD through governance dependency closure while preserving CMS and Payment as independently eligible, unresolved, unranked alternatives and leaving every post-BSHP position unresolved. |
