# ADR-0013 — Post-Shipping Backend Specification

## Identifier

ADR-0013

## Title

Post-Shipping Backend Specification

## Version

1.0.0

## Status

Accepted

## Date

2026-09-21

## Last Updated

2026-09-21

## Owner

Architecture

## Authoritative

true

## Context

Accepted ADR-0001 through ADR-0012 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP`. The canonical BSHP Specification is now `1.0.0 Approved`, and no backend position after BSHP is governed.

The completed post-BSHP eligibility audit found exactly two independently eligible capabilities: CMS and Payment. It found no unique next capability and did not rank them. Return now has governed Order, Inventory, and Shipping/Fulfilment backend boundaries but still lacks an Approved Payment backend Contract. Administration, Notifications, and Reporting still lack materially required owning-source backend Contracts. A governance decision is therefore required before another Backend Specification may enter Draft.

This Accepted decision evaluates CMS and Payment symmetrically through governance dependency closure. It selects Payment because an Approved Payment backend boundary would close Return's last identified backend prerequisite and would also supply missing Payment/Refund producer or source Contracts for Notifications, Reporting, and Administration. CMS is also authority-safe to specify now and supplies a missing source boundary for Reporting and an invocation boundary for Administration, but selecting CMS does not close as many currently identified dependency gaps. This comparison is not a judgment of business importance, technical merit, implementation effort, convenience, or priority.

## Decision Drivers

- Canonical governance establishes no backend position after BSHP.
- CMS and Payment are both independently eligible, so eligibility alone does not determine the next position.
- The selected capability must be specifiable implementation-neutrally from existing Approved authority and Contracts.
- Approved BCHK supplies governed Payment-initiation and correlation context.
- Approved BORD supplies governed Order identity, commercial history, Payment-reference, mismatch, and reconciliation context without owning Payment truth.
- Payment owns Payment Attempts, provider-evidenced outcomes, Payment Transactions, Refund execution, Chargebacks, and reconciliation under the Approved Payment Domain.
- Return's remaining missing backend prerequisite is an Approved Payment Contract for Refund execution and outcomes.
- Notifications, Reporting, and Administration each have materially applicable Payment or Refund dependencies.
- CMS remains authority-safe and independently eligible, and its later position must not be ranked or implied.
- All unresolved Product and Architecture decisions must remain unresolved.

## Considered Independently Eligible Alternatives

### A. CMS

CMS is independently eligible from existing Approved Product, Category, Identity, Customer, Pricing, Inventory, Search, and other governed boundaries. The Approved CMS Domain defines bounded CMS-owned content, version, publication, placement, history, recovery, reconciliation, Contract, and conditional-event semantics without transferring another Domain's authority.

A future CMS backend would provide a governed CMS source Contract for Reporting and a protected capability boundary for Administration. Content approval, scheduled publication, media handling, workflow, Roles and Permissions, escalation, and import/export decisions remain unresolved and need not be selected to draft an implementation-neutral CMS Specification.

CMS is not selected for the single immediate position. It remains independently eligible, separate, unresolved, unranked, and unordered relative to every capability whose position remains unresolved. Deferral is not rejection or a statement of lower value or priority.

### B. Payment

Payment is independently eligible from BCHK's governed initiation and correlation boundary, BORD's governed Order and commercial-history context, and the bounded evidence supplied by other Approved upstream backends. The Approved Payment Domain owns authoritative Payment processing truth, Payment Attempts, provider evidence, Payment Authorization, Capture, Void, Settlement, Payment Transactions, Refund execution, Chargebacks, and reconciliation without owning Checkout, Order, Pricing, Inventory, Return, fraud, tax, credit, or administrative policy.

An Approved Payment backend Contract would close Return's remaining identified backend prerequisite. It would also supply missing Payment/Refund producer or source boundaries for Notifications and Reporting and a protected Payment capability boundary for Administration. Provider, method, fraud, tax, Refund-policy, credit, and other open decisions remain unresolved and need not be selected to draft an implementation-neutral Payment Specification.

Payment is selected because it closes more of the explicitly documented missing governed backend dependencies while remaining authority-contained and implementation-neutral. This is dependency closure only, not a ranking by technical merit, business value, convenience, effort, or preference.

### C. Defer the Decision

Deferral would preserve the two-candidate ambiguity and establish no post-BSHP position. It remains a valid governance option but would leave Return's Payment dependency and the identified Payment/Refund dependencies of Administration, Notifications, and Reporting open.

## Dependency Comparison

| Consideration | CMS | Payment |
| --- | --- | --- |
| Independently eligible now | Yes; Approved CMS authority and bounded external relationships support an implementation-neutral specialization. | Yes; Approved Payment authority plus BCHK and BORD context support an implementation-neutral specialization. |
| Existing Approved backend inputs | May consume bounded identity, customer, product, category, pricing, inventory, search, and other evidence without authority transfer. | May consume bounded identity, customer, pricing, inventory, checkout, order, and shipping evidence without authority transfer. |
| Currently blocked capability directly advanced | Reporting and Administration gain a missing CMS source or invocation boundary. | Return gains its last identified missing backend prerequisite; Notifications, Reporting, and Administration gain missing Payment/Refund boundaries. |
| Open decisions | Content workflow, media, Roles/Permissions, escalation, import/export, infrastructure, messaging, cache, recovery, schema, and flags remain unresolved. | Provider/method, tax, cancellation, Return/Refund, Roles/Permissions, escalation, fraud, credit, infrastructure, messaging, cache, recovery, schema, and flags remain unresolved. |
| Authority containment | CMS Content and publication truth remain CMS-owned; commercial, catalogue, search, notification, reporting, and administrative truth remain external. | Payment processing and Refund execution truth remain Payment-owned; commercial policy, Checkout, Order, Return policy, fraud, tax, credit, and administrative truth remain external. |

## Decision

ADR-0013 establishes that the twelfth downstream Backend Specification after BEB, immediately after BSHP, SHALL be:

- **Capability:** Payment
- **Specification:** Payment Backend Specification
- **Path:** `specifications/backend/payment/payment-backend.md`
- **Scope code:** `BPAY`
- **Decomposition:** Payment-only; specializes exactly the Approved Payment Domain
- **Position:** Twelfth downstream Backend Specification after BEB, immediately after BSHP

`BPAY` means Backend Payment. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

BPAY SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved Payment Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

The resulting canonical sequence is `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY`.

Accepted ADR-0013 and completed canonical synchronization authorize BPAY to enter Draft. This decision does not approve BPAY; BPAY requires its own Draft-to-Approved lifecycle.

## Authority and Decomposition Boundaries

- **Payment:** BPAY may specialize Payment-owned Payment identity, Payment Attempts, provider-evidenced outcomes, Payment Authorization, Capture, Void, Settlement, Payment Transactions, Refund execution, Refund Transactions, Chargebacks, reconciliation, and other behavior already governed by the Approved Payment Domain. It must not expand that Domain's authority.
- **BEB:** BPAY must inherit and explicitly trace every materially applicable BEB Requirement without duplicating, weakening, or transferring shared backend governance.
- **BIDN:** trusted Principal and Authentication evidence may be consumed where materially applicable; Identity, credentials, Sessions, tokens, Claims, and Authentication authority do not transfer.
- **BCUS:** governed Customer, Account, ownership, and permitted contact evidence may be consumed where materially applicable; Customer, Account, Address, Consent, and Preference authority do not transfer.
- **BPRD, BCAT, and BSRCH:** Product, Product Variant, Category, and non-authoritative Search evidence may be consumed where materially applicable; catalogue and Search authority do not transfer.
- **BINV:** Inventory or Stock Reservation evidence may be consumed for bounded coordination; Stock, availability, reservation, adjustment, movement, and overselling authority do not transfer.
- **BPRC:** governed Money, Currency, Price, Discount, Promotion, Voucher, Tax, fee, and commercial-total evidence may be consumed; calculation, eligibility, stacking, tax, Currency-conversion, and commercial-policy authority do not transfer.
- **BCART and BCHK:** governed purchase-intent, initiation, correlation, and orchestration context may be consumed; Cart and Checkout identity, validation, lifecycle, orchestration, and success authority do not transfer.
- **BORD:** governed Order identity, commercial history, Payment references, and coordination evidence may be consumed; Order creation, lifecycle, history, snapshots, cancellation, and confirmation authority do not transfer.
- **BSHP:** governed Shipping and Fulfilment evidence may be consumed where materially applicable; Shipment, Carrier, Dispatch, tracking, delivery, and fulfilment authority do not transfer.
- **Return:** Return eligibility, authorization, lifecycle, inspection, disposition, exchange, and Refund policy remain Return-owned. BPAY may execute only a governed Payment-side Refund request and must not establish Return or Refund eligibility or amount policy.
- **CMS, Administration, Notifications, and Reporting:** content, protected administrative coordination, communication delivery, and analytical representation remain with their owning Domains. They may consume bounded Payment evidence without becoming authoritative and may not mutate Payment truth.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, Role labels, Permissions, Claims, identifiers, client state, provider redirects, or another Domain's state must not independently establish Payment authority.

## Open Product Decisions

This Accepted decision preserves the following **9 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Payment Domain §38:

| Item | Open Product Decision | Preserved BPAY boundary |
| ---: | --- | --- |
| 6 | Initial payment methods and provider. | No method, provider, provider Contract, or integration mechanism is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, calculation, invoice, or document policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, timing, Void, or reversal policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, exchange, Refund eligibility, amount, window, or method policy is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No support channel, escalation owner, route, workflow, expectation, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, accounting, or document policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, blocking, review, provider, threshold, or intervention policy is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, or commercial treatment is selected. |

No listed Product Decision is resolved by selecting BPAY.

## Open Architecture Decisions

This Accepted decision preserves the following **10 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BPAY boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment provider, provider Contract, adapter, or integration mechanism is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, workflow-state, or coordination use is selected. |
| 9 | External messaging introduction and service selection. | No messaging adoption, service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention period, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Payment persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BPAY. Frontend hosting, Shipping provider selection, initial Search implementation, and Product-media upload and transformation remain unresolved but are not materially required for this Payment-only roadmap decision.

## Explicit Non-Decisions

ADR-0013 does not create BPAY Requirements or `payment-backend.md`; select a Payment method or provider, provider Contract, Authentication protocol, Session or token mechanism, API route, HTTP method or status, DTO or payload, database schema, table, column, index, ORM mapping, event name or schema, topic, queue, broker, messaging technology, cache or Redis use, infrastructure product, hosting model, deployment topology, identifier format, retry count, timeout, TTL, retention value, rate limit, performance target, SLA, SLO, recovery objective, or other numerical value.

It does not resolve Payment, tax, invoice, Credit Note, cancellation, Return, exchange, Refund, fraud, Chargeback, gift-card, store-credit, promotional-credit, Customer-support, escalation, notification, reporting, or other Product policy. It defines no Role or Permission matrix and no concrete Authorization mechanism.

It does not establish a CMS backend identity or position, pre-authorize Return, or establish any Administration, Notifications, Reporting, or other later Backend Specification identity, title, path, scope code, decomposition, or ordering.

## Consequences

Accepted and synchronized governance establishes that:

- BPAY is authorized to enter Draft lifecycle as the immediate Backend Specification after BSHP;
- BPAY may specialize only Payment behavior already governed by the Approved Payment Domain and consume bounded upstream Contracts without transferring authority;
- BPAY must complete its own Draft-to-Approved lifecycle before becoming an Approved normative backend Specification;
- a future Approved BPAY Contract may close Return's remaining identified backend prerequisite, but ADR-0013 does not pre-authorize Return to enter Draft or establish Return's position;
- CMS remains independently eligible, separate, unresolved, unranked, and unordered relative to all unresolved capabilities;
- Administration, Notifications, Reporting, and Return remain separate and unresolved; and
- every position after BPAY remains unresolved.

The trade-off is that independently eligible CMS remains unresolved while Payment dependency closure is pursued. This is a governance consequence, not a ranking of value, priority, effort, convenience, or technical quality.

## Downstream Roadmap Containment

The sole roadmap position established by ADR-0013 is BPAY immediately after BSHP. This Accepted decision establishes no backend identity, title, path, scope code, decomposition, or ordering position after BPAY.

CMS remains independently eligible, separate, unresolved, unranked, and unordered. Return remains separate and unresolved: Payment selection alone does not authorize Return, and its Payment dependency closes only after a future BPAY Specification becomes Approved and exposes the applicable governed Contract. Administration, Notifications, and Reporting remain separate and unresolved. No ordering among these capabilities is stated or implied.

A future eligibility audit or Accepted ADR must govern any later position.

## Required Governance Reviews

ADR-0013 was Accepted after governance recorded completion of:

- Architecture review of the immediate post-BSHP selection, Payment-only decomposition, title, path, scope code, and dependency-closure rationale;
- affected Payment ownership review confirming accurate specialization of the Approved Payment Domain;
- affected Checkout ownership review confirming bounded BCHK initiation and correlation consumption and preserved Checkout authority;
- affected Order ownership review confirming bounded BORD context consumption and preserved Order authority;
- affected Shipping and Fulfilment ownership review confirming bounded BSHP evidence consumption and preserved Shipping authority;
- affected Return ownership review confirming Return policy and authority remain external and that Return is not pre-authorized;
- affected Administration ownership review confirming protected Payment capability use transfers no Payment authority and defines no Role or Permission matrix;
- affected Notifications ownership review confirming Payment/Refund producer evidence remains bounded and no event or provider mechanism is selected;
- affected Reporting ownership review confirming Payment/Refund source evidence remains bounded and analytical representations remain non-authoritative;
- affected Pricing ownership review confirming bounded commercial evidence consumption and preserved Pricing authority;
- affected Inventory ownership review confirming bounded Inventory evidence consumption and preserved Inventory authority;
- affected Identity and Customer ownership review confirming bounded identity, Customer, and Account evidence consumption and preserved authority;
- affected CMS ownership review confirming CMS remains independently eligible, separate, unresolved, unranked, and unordered;
- Security review of contextual Authorization, Sensitive Data, provider evidence, replay, duplicate financial effects, uncertainty, and recovery boundaries;
- Testing review of future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, cross-references, and roadmap containment;
- confirmation that `BPAY` is unique among current Specification scope codes;
- confirmation that `specifications/backend/payment/payment-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BPAY is established; and
- synchronized canonical updates to ADR-0013, `ARCHITECTURE.md`, and `DECISIONS.md`.

The governed Architecture, affected ownership, Security, Testing, and Documentation reviews represented above were completed for acceptance. No reviewer names, signatures, tickets, or external approval artifacts are asserted.

## Acceptance Conditions and Synchronization

ADR-0013 was Accepted after:

- governance approved BPAY as the immediate Backend Specification after BSHP;
- required reviews confirmed Payment-only specialization and preservation of every external authority;
- review confirmed the dependency-closure rationale without ranking Payment or CMS by business or technical merit;
- review confirmed CMS remains independently eligible, separate, unresolved, unranked, and unordered;
- review confirmed Return is not pre-authorized and depends on a future Approved BPAY Contract;
- review confirmed every post-BPAY roadmap position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History were synchronized by recording BSHP as `1.0.0 Approved`, removing the obsolete assertion that `specifications/backend/shipping/shipping-backend.md` had not been created or that BSHP remained unauthorized or unapproved, recording that Return's governed Order, Inventory/restocking, and Shipping/reverse-logistics backend prerequisites are closed and its remaining identified backend prerequisite is an Approved Payment backend Contract, and establishing BPAY as the immediate backend position after BSHP in accordance with Accepted ADR-0013;
- `DECISIONS.md` metadata, Decision Index, and Revision History indexed ADR-0013 as Accepted; and
- ADR-0013 lifecycle wording was synchronized to the Accepted decision.

Acceptance synchronization modified only ADR-0013, `ARCHITECTURE.md`, and `DECISIONS.md`. Review discovered no direct contradiction, and `PRODUCT.md` required no change.

Acceptance and canonical synchronization are complete. BPAY is authorized to enter Draft governance but is not Approved and must complete its own Draft-to-Approved lifecycle.

## Validation Criteria

The completed acceptance review verified that:

1. Payment is the sole selected capability;
2. the established Specification is exactly the Payment Backend Specification at `specifications/backend/payment/payment-backend.md` under unique scope `BPAY`;
3. the decomposition is Payment-only and preserves the Approved Payment Domain authority;
4. the canonical sequence ends at `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY`;
5. CMS remains independently eligible, separate, unresolved, unranked, and unordered;
6. Return remains separate, unresolved, and unauthorized until a future Approved BPAY Contract and separate governance establish eligibility and position;
7. Administration, Notifications, and Reporting remain separate and unresolved;
8. every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BPAY remains unresolved;
9. all 9 listed Product Decisions and 10 listed Architecture Decisions remain unresolved;
10. no implementation, provider, policy, Contract design, numerical value, or later roadmap position is selected;
11. ADR-0013 is `1.0.0 Accepted` and `authoritative: true`;
12. BPAY is authorized to enter Draft only, is not Approved, and `specifications/backend/payment/payment-backend.md` was not created by acceptance;
13. whitespace and reference validation pass; and
14. ADR-0013 lifecycle and canonical synchronization changes remain limited to ADR-0013, `ARCHITECTURE.md`, and `DECISIONS.md`, and no unrelated repository changes are introduced.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0012-post-order-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/checkout/checkout-backend.md`
- `specifications/backend/order/order-backend.md`
- `specifications/backend/shipping/shipping-backend.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-21 | Accepted | Accepted Payment-only BPAY immediately after BSHP through governance dependency closure, synchronized canonical Architecture and Decision Index, preserved CMS as independently eligible and unresolved, and left every post-BPAY position unresolved. |
| 0.1.0 | 2026-09-21 | Proposed | Proposed Payment-only BPAY immediately after BSHP through governance dependency closure while preserving CMS as independently eligible, unresolved, and unranked and leaving every post-BPAY position unresolved. |
