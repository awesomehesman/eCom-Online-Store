# ADR-0011 — Post-Checkout Backend Specification

## Identifier

ADR-0011

## Title

Post-Checkout Backend Specification

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

Accepted ADR-0001 through ADR-0010 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK`. Approved BCHK establishes no backend position after itself.

The completed post-BCHK eligibility audit found exactly three independently eligible capabilities: CMS, Payment, and Order. It found no unique next capability and did not select or rank them. Shipping and Fulfilment was not yet independently eligible because a governed Order backend boundary remained foundational; Return still lacked governed Order, Payment, and Shipping backend Contracts; and Administration, Notifications, and Reporting still lacked material owning-source backend Contracts.

Approved BCHK now supplies the governed Checkout-to-Order request, correlation, duplicate-safety, uncertainty, Payment/Order mismatch, recovery, and reconciliation boundary required to specialize Order without transferring Checkout authority. The Approved Payment and Shipping and Fulfilment Domains define their separate authority sufficiently for a mechanism-neutral Order backend boundary while their backend identities remain unresolved.

CMS remains independently eligible without depending on Order. Payment also remains independently eligible from BCHK's governed initiation and evidence boundary. This Accepted ADR selects one permissible immediate roadmap position through dependency-closure governance; it does not rank business value, technical merit, effort, priority, or customer value.

## Decision Drivers

- Canonical governance establishes no backend position after BCHK.
- CMS, Payment, and Order are independently eligible, so eligibility alone does not determine the next position.
- BCHK supplies the governed Checkout-to-Order handoff that was previously missing.
- Order owns durable Order identity, creation truth, Order Items, Order Snapshots, lifecycle, history, cancellation coordination, and bounded downstream handoffs.
- A governed Order backend boundary is the documented remaining foundational prerequisite for Shipping and Fulfilment eligibility.
- Shipping and Fulfilment is itself one of Return's missing governed boundaries, while selecting Payment alone would leave Return dependent on both Order and Shipping backend boundaries.
- Selecting one immediate boundary must not predetermine Shipping and Fulfilment or any later roadmap position.
- CMS and Payment must remain valid, independently eligible, unordered alternatives.

## Candidate Analysis

### CMS

CMS is independently eligible from governed Product, Category, Identity, Customer, Search, and other Approved authority. It could legitimately occupy the immediate post-BCHK position. Current audit evidence does not identify CMS as the missing foundational backend boundary blocking Shipping and Fulfilment, Payment, Order, or Return. Deferral is not rejection or ranking; CMS remains independently eligible, separate, unresolved, and unranked.

### Payment

Payment is independently eligible because Approved BCHK supplies its governed initiation and correlation boundary, while Approved Pricing, Customer, Identity, Inventory, and other sources preserve their authority. A Payment backend Contract would close one Return dependency, but Return would still require Order and Shipping backend Contracts. Deferral is not rejection or ranking; Payment remains independently eligible, separate, unresolved, and unranked.

### Order

Order is independently eligible because Approved BCHK supplies a governed Checkout-to-Order handoff, and Approved upstream backends supply bounded Customer, Product, Pricing, Inventory, Cart, Identity, Category, Search, and Checkout evidence. Approved Payment and Shipping and Fulfilment Domains preserve their authority without requiring this ADR to create their backend identities or Contracts.

The post-BCHK audit identifies the missing governed Order backend boundary as foundational to Shipping and Fulfilment eligibility. Selecting Order therefore closes a documented governance dependency while selecting no later capability. This is dependency-closure reasoning, not a claim that Order is superior, more valuable, easier, more urgent, or higher priority than CMS or Payment.

## Decision

ADR-0011 establishes that the tenth downstream Backend Specification after BEB, immediately after BCHK, SHALL be:

- **Capability:** Order
- **Specification:** Order Backend Specification
- **Path:** `specifications/backend/order/order-backend.md`
- **Scope code:** `BORD`
- **Decomposition:** Order-only; specializes exactly the Approved Order Domain
- **Position:** Tenth downstream Backend Specification after BEB, immediately after BCHK

`BORD` means Backend Order. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

BORD SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved Order Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

The resulting canonical sequence is `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD`.

BORD is the only roadmap position established by ADR-0011. Every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BORD remains unresolved. Accepted ADR-0011 and canonical `ARCHITECTURE.md` and `DECISIONS.md` synchronization authorize a BORD Draft only; they do not approve BORD, which must complete its own Draft-to-Approved lifecycle.

## Authority Boundary

- **BEB:** BORD must inherit and trace every materially applicable shared backend obligation.
- **BCHK:** BORD may consume the governed Checkout-to-Order request, correlation, and orchestration evidence; Checkout progression, submission, failure, recovery, and orchestration truth do not transfer.
- **Payment:** BORD may consume governed authoritative Payment evidence while Payment, Payment Attempt, provider evidence, Authorization, Capture, Void, Settlement, Refund, Chargeback, and reconciliation authority remain Payment-owned.
- **Shipping and Fulfilment:** BORD may provide bounded governed Order context for later fulfilment coordination while Shipment, Fulfilment, Dispatch, Carrier, tracking, delivery, provider evidence, and Shipping truth remain Shipping-owned.
- **BINV:** BORD may consume governed Inventory and Stock Reservation evidence; Stock, Available-to-Sell, reservation lifecycle, adjustments, movements, and Inventory truth do not transfer.
- **BPRD:** BORD may retain governed historical Product and Product Variant representation; current Product identity, lifecycle, publication, sellability, content, and media authority do not transfer.
- **BPRC:** BORD may retain confirmed governed commercial evidence; Price, Discount, Promotion, Voucher, Tax, Money, Currency, and current Pricing authority do not transfer.
- **BCUS:** BORD may consume governed Customer, Account, Address, Consent, Preference, and ownership evidence; Customer and Account authority do not transfer.
- **BIDN:** BORD may consume trusted Principal and Authentication evidence; Identity, credentials, Sessions, tokens, and Authentication authority do not transfer.
- **BCART:** BORD may consume correlated historical purchase-intent evidence where governed; Cart identity, contents, mutation, lifecycle, and current totals authority do not transfer.
- **BCAT and BSRCH:** Category and Search evidence creates no Order authority and Category or Search truth does not transfer.
- **Return:** Order may expose bounded historical evidence, while Return identity, eligibility, authorization, lifecycle, inspection, disposition, reverse logistics, and Return truth remain Return-owned.
- **Administration:** protected support and recovery may invoke Order-owned Use Cases through contextual Authorization without transferring Order authority or defining a Role/Permission matrix.
- **Notifications and Reporting:** communications and analytical representations remain non-authoritative and cannot establish or mutate Order truth.
- **CMS:** policy presentation or content evidence remains CMS-owned; CMS remains independently eligible and outside BORD.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, identifiers, UI state, Role labels, Permissions, Claims, Scope, Cart possession, Checkout state, Payment state, Shipping state, or derived representations MUST NOT independently establish Order authority.

## Dependency and Eligibility Traceability

| Capability | Current governed evidence | BORD relationship | Authority retained outside BORD |
| --- | --- | --- | --- |
| BEB | Approved | Inherited baseline | Shared backend governance |
| BIDN | Approved | Trusted actor evidence | Identity and Authentication |
| BCUS | Approved | Customer, Account, Address, Consent, Preference, and ownership evidence | Customer and Account truth |
| BPRD | Approved | Product/Product Variant identity and historical representation input | Current Product truth |
| BINV | Approved | Inventory and Stock Reservation evidence | Inventory truth |
| BPRC | Approved | Confirmed commercial evidence | Current Pricing truth |
| BCART | Approved | Correlated purchase-intent evidence | Cart truth |
| BCAT | Approved | Optional governed Category context | Category truth |
| BSRCH | Approved | Non-authoritative discovery context only | Search truth and derived state |
| BCHK | Approved | Governed Order-creation request and orchestration evidence | Checkout truth |
| Payment Domain | Approved | Authoritative Payment evidence where required | Payment truth; backend identity unresolved |
| Shipping and Fulfilment Domain | Approved | Future bounded fulfilment handoff | Shipping truth; backend identity unresolved |
| Return Domain | Approved | Future bounded historical Order evidence | Return truth; backend identity unresolved |
| CMS | Approved Domain; independently eligible backend candidate | Separate conditional content/policy source only | CMS truth and backend roadmap position unresolved |
| Administration | Approved Domain | Consumer of protected Order Use Cases | Administrative coordination |
| Notifications Domain | Approved | Conditional downstream consumer | Notification delivery truth |
| Reporting Domain | Approved | Conditional downstream consumer | Reporting definitions and Projections |

The eligibility audit supplies evidence for the three-candidate set; it is not authority for this selection. Accepted ADR-0011 and synchronized canonical governance supply the Architecture decision.

## Consequences

Accepted and synchronized governance establishes that:

- BORD is the authorized immediate Backend Specification after BCHK;
- a BORD Draft may specialize Order identity, creation truth, Order Items, Order Snapshots, lifecycle, history, cancellation coordination, recovery, reconciliation, and bounded handoffs without transferring external authority;
- the documented missing Order backend boundary blocking Shipping and Fulfilment eligibility is governed, while operational dependency closure requires BORD to complete its own Draft-to-Approved lifecycle;
- Shipping and Fulfilment remains a separate future eligibility and governance decision rather than becoming automatically next;
- CMS and Payment remain independently eligible, separate, unresolved, and unranked, with no ordering between them;
- Return, Administration, Notifications, and Reporting remain separate and unresolved; and
- every position after BORD remains unresolved.

Trade-offs are that CMS and Payment remain unresolved despite independent eligibility, and selecting BORD does not itself create any downstream Contract or runtime behavior. These are governance consequences, not rankings of value, priority, effort, difficulty, or technical quality.

## Alternatives Considered

### A. CMS

CMS is independently eligible and could legitimately have been selected. It remains separate, unresolved, unranked, and unordered relative to Payment and every other still-unselected capability. Its deferral establishes no later CMS position.

### B. Payment

Payment is independently eligible and could legitimately have been selected. Its selection would close one Return dependency, while Order and Shipping backend boundaries would remain unresolved. Payment remains separate, unresolved, unranked, and unordered relative to CMS and every other still-unselected capability. Its deferral establishes no later Payment position.

### C. Order

Order is selected because Approved BCHK enables authority-safe specialization and the audit identifies the governed Order backend boundary as the remaining foundational prerequisite for Shipping and Fulfilment eligibility. This closes one documented dependency without selecting Shipping and Fulfilment or another later capability.

### D. Defer the Decision

Deferral would preserve the three-candidate ambiguity and establish no position after BCHK. It remains a valid governance option but does not close the documented Order boundary.

## Explicit Non-Decisions and Roadmap Containment

ADR-0011 does not create BORD Requirements or `order-backend.md`; define Order APIs, routes, methods, statuses, DTOs, payloads, persistence, database schemas, tables, columns, indexes, ORM mappings, event names or schemas, topics, queues, brokers, transports, Order lifecycle implementation, state machine, workflow engine, cache or Redis use, provider integration, messaging, hosting, infrastructure, deployment, Payment mechanisms, Shipping mechanisms, retry counts, timeout values, retention periods, rate limits, performance targets, SLA/SLO values, recovery objectives, or other numerical operational values.

This Accepted decision does not resolve Order numbering, cancellation, Return, Refund, Payment, Shipping, Inventory, Pricing, tax, invoice, Credit Note, fraud, gift/store/promotional credit, Customer data, Consent, support, escalation, notification, reporting, or other Product policy.

BORD is the only roadmap position established. CMS remains independently eligible, separate, unresolved, and unranked. Payment remains independently eligible, separate, unresolved, and unranked. No ordering between CMS and Payment is established, and neither is stated to follow BORD. Shipping and Fulfilment remains separate, not selected, and unresolved; the audit's dependency evidence does not make it automatically next. Return, Administration, Notifications, and Reporting remain separate and unresolved. Every backend roadmap position after BORD remains unresolved.

## Open Product Decisions

This Accepted decision preserves the following **21 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Order Domain §28:

| Item | Open Product Decision | Preserved BORD boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Customer/Visitor association policy is selected. |
| 5 | Customer email-verification requirements. | No verification prerequisite or mechanism is selected. |
| 6 | Initial payment methods and provider. | No Payment method, provider, or provider Contract is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, eligibility, or fee policy is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold or commercial treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, invoice, or document rule is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, or timing policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No post-Order eligibility, exchange, or Refund policy is selected. |
| 12 | Stock Reservation duration. | No reservation duration, expiry, or timing value is selected. |
| 13 | Back-order and pre-order support. | No unavailable-item creation or fulfilment policy is selected. |
| 14 | Voucher and promotion stacking policy. | No stacking, precedence, or combination policy is selected. |
| 18 | Customer-support channels and service expectations. | No channel, hours, expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No Consent or Preference model is selected or inferred from an Order. |
| 20 | Initial analytics provider and event taxonomy. | No provider, taxonomy, or measurement mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, metric, export, schedule, or format is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, process, or service target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, or accounting policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, blocking, review, provider, or intervention policy is selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No Customer-data workflow or effect on retained Order history is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, or treatment is selected. |

No listed Product Decision is resolved by selecting BORD.

## Open Architecture Decisions

This Accepted decision preserves the following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BORD boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment provider or provider Contract is selected. |
| 6 | Shipping provider selection. | No Shipping provider or provider Contract is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery Contract is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No messaging adoption, service, broker, topic, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Order persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BORD. Frontend hosting, initial Search implementation, and Product-media upload/transformation remain unresolved but are not materially required for this Order-only roadmap decision.

## Required Governance Reviews

This decision was Accepted after completion of:

- Architecture review of the immediate post-BCHK selection, Order-only decomposition, title, path, scope code, and dependency-closure rationale;
- affected Order ownership review confirming accurate Domain specialization;
- affected Checkout ownership review confirming bounded BCHK handoff and preserved Checkout authority;
- affected Payment ownership review confirming bounded evidence consumption and preserved Payment authority;
- affected Shipping and Fulfilment ownership review confirming bounded future handoff, preserved Shipping authority, and no automatic later position;
- affected Inventory ownership review confirming bounded BINV evidence and preserved Inventory authority;
- affected Product and Product Catalogue ownership review confirming bounded BPRD evidence and preserved Product/Product Variant authority;
- affected Pricing ownership review confirming bounded BPRC evidence and preserved Pricing authority;
- affected Customer ownership review confirming bounded BCUS evidence and preserved Customer/Account authority;
- affected Identity ownership review confirming bounded BIDN evidence and no Authentication, Session, or token mechanism;
- affected Cart ownership review confirming bounded BCART evidence and preserved Cart authority;
- affected Return ownership review confirming Order history consumption transfers no Return authority and establishes no Return backend position;
- affected Notifications ownership review confirming communications remain non-authoritative and no provider or event Contract is selected;
- affected Reporting ownership review confirming analytical representations remain non-authoritative and no reporting definition is selected;
- affected Administration ownership review confirming protected support/recovery transfers no Order authority and defines no Role/Permission matrix;
- Security review of contextual Authorization, Sensitive Data, tamper resistance, duplicate effects, uncertainty, and recovery boundaries;
- Testing review of the future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, traceability, cross-references, and roadmap containment;
- confirmation that `BORD` is unique among current Specification scope codes;
- confirmation that `specifications/backend/order/order-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered relative to each other;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BORD is established; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md` completed with ADR-0011 acceptance.

No CMS, Category, or Search and Discovery ownership review is required for acceptance because ADR-0011 neither consumes their backend Contracts materially for Order specialization nor changes their authority or roadmap position. CMS remains explicitly preserved as an independently eligible alternative.

The required Architecture, Order, Checkout, Payment, Shipping and Fulfilment, Inventory, Product and Product Catalogue, Pricing, Customer, Identity, Cart, Return, Notifications, Reporting, Administration, Security, Testing, and Documentation reviews were completed for acceptance. No reviewer names, signatures, tickets, dates beyond this record's date, or external evidence are asserted.

## Acceptance Conditions and Synchronization

ADR-0011 was Accepted after:

- governance approved BORD as the immediate Backend Specification after BCHK;
- review confirmed BORD remains Order-only and preserves every source and downstream authority;
- review confirmed the dependency-closure rationale without ranking CMS, Payment, or Order;
- review confirmed CMS and Payment remain independently eligible, separate, unresolved, unranked, and unordered relative to each other;
- review confirmed Shipping and Fulfilment, Return, Administration, Notifications, Reporting, and every post-BORD roadmap position remain unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History were synchronized through BORD;
- `DECISIONS.md` metadata, Decision Index, and Revision History indexed ADR-0011 as Accepted; and
- ADR-0011 lifecycle wording recorded the Accepted decision consistently.

Acceptance synchronized only ADR-0011, `ARCHITECTURE.md`, and `DECISIONS.md`; acceptance review discovered no direct contradiction and required no `PRODUCT.md` change. BORD is authorized to enter Draft lifecycle but is not Approved.

## Security, Data, Compatibility, and Operational Impact

This Accepted decision creates no runtime behavior, API, DTO, schema, database structure, migration, event, provider, cache, infrastructure, deployment, configuration, or operational target. A future BORD Specification must inherit materially applicable BEB security, Authorization, privacy, compatibility, migration, boundedness, observability, audit, failure, recovery, reconciliation, idempotency, replay, and verification obligations without selecting unresolved mechanisms.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0009-post-category-backend-specification.md`
- `specifications/adr/ADR-0010-post-search-backend-specification.md`
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
- `specifications/domains/order/order-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-19 | Accepted | Accepted Order-only BORD as the immediate post-BCHK Backend Specification through governance dependency closure while preserving CMS and Payment as independently eligible, separate, unresolved, unranked alternatives and leaving every post-BORD position unresolved. |
| 0.1.0 | 2026-09-19 | Proposed | Proposed Order-only BORD as the immediate post-BCHK Backend Specification through governance dependency closure while preserving CMS and Payment as independently eligible, unresolved alternatives and leaving every post-BORD position unresolved. |
