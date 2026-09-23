# ADR-0014 — Post-Payment Backend Specification

## Identifier

ADR-0014

## Title

Post-Payment Backend Specification

## Version

0.1.0

## Status

Proposed

## Date

2026-09-23

## Last Updated

2026-09-23

## Owner

Architecture

## Authoritative

true

## Context

Accepted ADR-0001 through ADR-0013 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY`. The Payment Backend Specification is `1.0.0 Approved`, `authoritative: false`, and governed under scope `BPAY`. No backend position after BPAY is governed.

The completed post-BPAY eligibility audit found exactly two independently eligible capabilities: CMS and Return. Administration remains dependency-blocked by missing CMS, Return, Notifications, and Reporting backend Contracts. Notifications remains blocked by a missing Return producer Contract. Reporting remains blocked by missing Return and CMS source Contracts. The audit did not select, rank, or order CMS and Return, so a governance decision is required before another Backend Specification may enter Draft.

This Proposed decision evaluates CMS and Return symmetrically through governance dependency closure. It proposes Return because a future Approved Return backend Contract would close Notifications' remaining identified producer dependency and one of Reporting's two remaining source dependencies, while also closing a Return invocation dependency for Administration. A future Approved CMS backend Contract would close Reporting's CMS source dependency and a CMS invocation dependency for Administration but would leave Notifications blocked by Return. This is dependency analysis only, not a judgment of business value, technical merit, effort, simplicity, commercial importance, or preference.

## Decision Drivers

- Canonical governance establishes no backend position after BPAY.
- CMS and Return are independently eligible, so eligibility alone does not determine the immediate position.
- Approved BPAY closes Return's previously missing Payment-side Refund execution and Refund Transaction backend dependency.
- Approved BORD supplies governed Order and Order Item evidence for Return.
- Approved BINV supplies governed Inventory and restocking boundaries for Return.
- Approved BSHP supplies governed Shipping and reverse-logistics boundaries for Return.
- Notifications' remaining identified producer dependency is Return.
- Reporting's remaining identified source dependencies are Return and CMS.
- Administration retains multiple missing invocation Contracts and is not made eligible by either selection alone.
- The unselected eligible capability must remain independent, unresolved, unranked, and unordered.
- Every Product and Architecture decision material to the selected capability must remain unresolved.

## Considered Independently Eligible Alternatives

### A. CMS

CMS is independently eligible from existing Approved Product, Category, Pricing, Inventory, Identity, Customer, Search and other governed boundaries. The Approved CMS Domain defines bounded content, version, publication, placement, history, recovery, reconciliation, Contract, and conditional-event semantics without transferring external authority.

A future Approved CMS backend would supply Reporting's missing CMS source Contract and Administration's missing CMS invocation Contract. It would not close Notifications' missing Return producer Contract or Reporting's missing Return source Contract. Content workflow, media policy, Roles and Permissions, escalation, import/export, storage, provider, Contract, event, infrastructure, and other open decisions can remain unresolved in a mechanism-neutral CMS Draft.

CMS is not selected for the single immediate position. It remains independently eligible, separate, unresolved, unranked, and unordered relative to every unresolved capability. Deferral is not rejection and assigns no later CMS position.

### B. Return

Return is independently eligible because Approved BORD, BINV, BSHP, and BPAY now provide the governed Order, Inventory/restocking, Shipping/reverse-logistics, and Payment-side Refund execution boundaries required for authority-safe specialization. The Approved Return Domain owns Return identity, requests, Return Items, eligibility outcomes under future governed policy, Return Authorization, Return-scoped lifecycle evidence, receipt, inspection, disposition, and coordination while preserving every external authority.

A future Approved Return backend would close Notifications' remaining identified producer dependency, one of Reporting's two remaining source dependencies, and Administration's missing Return invocation Contract. Return eligibility, windows, exchange policy, Refund eligibility and amount policy, partial Refunds, cancellation, restoration, tax, Credit Note, credit treatment, provider choices, and implementation mechanisms remain unresolved and need not be selected for a mechanism-neutral Return Draft.

Return is selected because it creates the broader documented dependency closure while remaining authority-contained and implementation-neutral. This is not a ranking by business value, technical merit, effort, simplicity, commercial importance, or preference.

### C. Defer the Decision

Deferral would preserve the two-candidate ambiguity and establish no post-BPAY position. It remains a valid governance option but would leave the identified Return-dependent producer, source, and invocation boundaries unresolved.

## Dependency Comparison

| Consideration | CMS | Return |
| --- | --- | --- |
| Independently eligible now | Yes; existing Approved boundaries support mechanism-neutral CMS specialization. | Yes; BORD, BINV, BSHP, and BPAY close the governed dependencies needed for mechanism-neutral Return specialization. |
| Direct downstream closure after future approval | Reporting gains its CMS source; Administration gains its CMS invocation boundary. | Notifications gains its remaining producer; Reporting gains its Return source; Administration gains its Return invocation boundary. |
| Dependencies still blocked after future approval | Notifications still lacks Return; Reporting still lacks Return; Administration still lacks Return, Notifications, and Reporting. | Reporting still lacks CMS; Administration still lacks CMS, Notifications, and Reporting. |
| Authority containment | CMS remains bounded from Product, Category, Pricing, Inventory, Identity, Customer, Search, Administration, Notifications, and Reporting. | Return remains bounded from Order, Payment, Inventory, Shipping, Customer, Product, Pricing, Administration, Notifications, Reporting, and other owners. |
| Open decisions | Content workflow, media, roles, escalation, import/export, provider, infrastructure, and mechanisms remain unresolved. | Return, exchange, Refund, cancellation, restoration, tax, credit, provider, infrastructure, and mechanisms remain unresolved. |

## Decision

ADR-0014 proposes that the thirteenth downstream Backend Specification after BEB, immediately after BPAY, SHALL be:

- **Capability:** Return
- **Specification:** Return Backend Specification
- **Path:** `specifications/backend/return/return-backend.md`
- **Scope code:** `BRET`
- **Decomposition:** Return-only; specializes exactly the Approved Return Domain
- **Position:** Thirteenth downstream Backend Specification after BEB, immediately after BPAY

`BRET` means Backend Return. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

BRET SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved Return Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

If ADR-0014 becomes Accepted and canonical `ARCHITECTURE.md` and `DECISIONS.md` synchronization is completed, the resulting canonical sequence will be `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY → BRET`.

Proposed ADR-0014 does not yet authorize BRET drafting. BRET may enter Draft only after ADR-0014 is Accepted and canonical synchronization is complete. Acceptance would not approve BRET; BRET would require its own Draft-to-Approved lifecycle.

## Authority and Decomposition Boundaries

- **Return:** BRET may specialize Return identity, Return requests, Return Items, governed eligibility outcomes, Return Authorization, Return-scoped lifecycle and history, receipt, inspection, disposition, failure, uncertainty, recovery, reconciliation, and other behavior already governed by the Approved Return Domain. It must not expand that Domain's authority.
- **BEB:** BRET must inherit and explicitly trace every materially applicable BEB Requirement without duplicating, weakening, or transferring shared backend governance.
- **BIDN and BCUS:** trusted Principal, Authentication, Customer, Account, ownership, contact, and Address evidence may be consumed where materially applicable; Identity, credentials, Sessions, Customer, Account, Consent, Preference, and source authority do not transfer.
- **BPRD and BCAT:** governed Product, Product Variant, and Category references may be consumed; catalogue identity, definition, taxonomy, content, and lifecycle authority do not transfer.
- **BINV:** BRET may request or consume governed restocking, inspection-related Inventory coordination, and Inventory outcomes; Stock, Stock Reservation, Stock Adjustment, Stock Movement, availability, and disposition effects owned by Inventory do not transfer.
- **BPRC:** governed Money, Currency, Price, Discount, Promotion, Voucher, Tax, fee, and historical commercial evidence may be consumed; calculation, eligibility, reversal, restoration, conversion, and commercial-policy authority do not transfer.
- **BCART and BCHK:** historical purchase or orchestration references may be consumed only where governed; Cart and Checkout identity, lifecycle, validation, orchestration, and success authority do not transfer.
- **BORD:** governed Order identity, Order Item, Order Snapshot, quantity, status, cancellation, and historical evidence may be consumed; Order creation, lifecycle, history, cancellation coordination, and commercial snapshot authority do not transfer.
- **BSHP:** governed reverse-logistics, transport, Shipment, delivery, and tracking evidence may be consumed or requested; Shipping, Fulfilment, Carrier, Dispatch, tracking, and delivery authority do not transfer.
- **BPAY:** governed Payment, Capture, Refund-execution, Refund Transaction, failure, uncertainty, and reconciliation evidence may be consumed or requested; Payment processing, provider, financial-effect, Refund execution, and Refund Transaction authority do not transfer.
- **Administration:** protected Staff workflows may invoke explicit BRET capabilities without bypassing Return invariants or acquiring Return authority. No administrative API or Role/Permission matrix is selected.
- **Notifications and Reporting:** bounded Return facts may eventually be exposed through governed Contracts; communication delivery, reporting, analytics, exports, and Projections remain non-authoritative and cannot mutate Return truth.
- **CMS, Search, and other Domains:** all owning-Domain truth remains external and no presentation, index, workflow, or Projection establishes Return truth.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, identifiers, Role labels, Permissions, Claims, client state, Order state, Payment state, Shipping state, or derived representations must not independently authorize a Return action.

## Dependency Consequences

Accepted ADR-0014 and canonical synchronization would authorize only a BRET Draft. Selecting BRET does not itself close downstream dependencies. Those dependencies can close only after a future BRET Specification becomes Approved and exposes the applicable governed Contracts.

- **Administration:** remains separate and dependency-blocked by missing Approved CMS, Return, Notifications, and Reporting invocation Contracts during the BRET Draft lifecycle. After future BRET approval, CMS, Notifications, and Reporting would remain missing.
- **Notifications:** remains separate and dependency-blocked by the missing Approved Return producer Contract during the BRET Draft lifecycle. Future BRET approval may close that dependency but does not authorize Notifications or establish its position.
- **Reporting:** remains separate and dependency-blocked by missing Approved Return and CMS source Contracts during the BRET Draft lifecycle. After future BRET approval, the CMS source would remain missing.
- **CMS:** remains independently eligible, separate, unresolved, unranked, and unordered.

No downstream capability is authorized by this dependency analysis.

## Open Product Decisions

This Proposed decision preserves the following **21 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Return Domain §28:

| Item | Open Product Decision | Preserved BRET boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Customer, Visitor, or Account prerequisite is selected. |
| 5 | Customer email-verification requirements. | No verification prerequisite or mechanism is selected. |
| 6 | Initial payment methods and provider. | No method, provider, provider Contract, or Refund mechanism is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No reverse-logistics provider, service, area, eligibility, or fee is selected. |
| 8 | Free-delivery threshold and promotional treatment. | No Return-related delivery or commercial treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax-display, invoice, Refund-document, or commercial policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, timing, or Return transition policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No eligibility, window, exchange, Refund, disposition, or treatment policy is selected. |
| 12 | Stock Reservation duration. | No Reservation duration or exchange-allocation treatment is selected. |
| 13 | Back-order and pre-order support. | No eligible-item or exchange treatment for either capability is selected. |
| 14 | Voucher and promotion stacking policy. | No reversal, restoration, stacking, precedence, or Refund treatment is selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No marketing, consent, preference, or Return-communication policy is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, event taxonomy, or Return analytics mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, export, audience, purpose, format, or schedule is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, workflow, expectation, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, accounting, or document policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No screening, provider, score, threshold, rule, review, or intervention policy is selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No privacy workflow, retention treatment, correction, deletion, or closure mechanism is selected. |
| 29 | Gift cards, store credit, and promotional credit policy. | No credit instrument, balance, redemption, restoration, Refund, or treatment is selected. |

No listed Product Decision is resolved by selecting BRET.

## Open Architecture Decisions

This Proposed decision preserves the following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BRET boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 5 | Payment provider selection. | No Payment Provider, Refund integration, provider Contract, or adapter is selected. |
| 6 | Shipping provider selection. | No reverse-logistics provider, Carrier, Contract, or adapter is selected. |
| 7 | Transactional notification provider selection. | No notification provider or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, Session, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Return persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BRET. Frontend hosting, initial Search implementation, and Product-media upload and transformation remain unresolved but are not materially required for this Return-only roadmap decision.

## Explicit Non-Decisions

ADR-0014 does not create BRET Requirements or `return-backend.md`; select Return eligibility, window, approval, lifecycle graph, inspection criteria, disposition rules, exchange policy, Refund eligibility or amount policy, partial Refund policy, cancellation policy, Voucher or Promotion restoration, tax or Credit Note policy, credit treatment, provider, API route, HTTP method or status, DTO, payload, schema, table, column, index, ORM mapping, identifier format, event name or payload, topic, queue, broker, messaging technology, Redis or cache use, infrastructure product, hosting topology, retry count, timeout, TTL, retention period, rate limit, SLA, SLO, recovery objective, or other numerical value.

It defines no Role or Permission matrix, concrete Authorization mechanism, downstream Contract design, or post-BRET roadmap position.

## Consequences

If Accepted and canonically synchronized:

- BRET will be authorized to enter Draft lifecycle as the immediate Backend Specification after BPAY;
- BRET may specialize only behavior already governed by the Approved Return Domain and consume bounded Approved upstream Contracts without transferring authority;
- BRET must complete its own Draft-to-Approved lifecycle before becoming an Approved normative backend Specification;
- CMS will remain independently eligible, separate, unresolved, unranked, and unordered;
- Administration, Notifications, and Reporting will remain separate and dependency-blocked during the BRET Draft lifecycle;
- future BRET approval may close the Return dependency for Notifications and Reporting without authorizing either capability; and
- every position after BRET will remain unresolved.

The trade-off is that independently eligible CMS remains unresolved while Return dependency closure is pursued. This is a governance consequence, not a ranking of value, priority, effort, simplicity, commercial importance, or technical quality.

## Roadmap Containment

The sole roadmap position proposed by ADR-0014 is BRET immediately after BPAY. This Proposed decision establishes no backend identity, title, path, scope code, decomposition, or ordering position after BRET.

CMS remains independently eligible, separate, unresolved, unranked, and unordered. Administration, Notifications, and Reporting remain separate and unresolved with the dependency state recorded above. No ordering among these capabilities is stated or implied. A future eligibility audit or Accepted ADR must govern any later position.

## Required Governance Reviews

Before ADR-0014 may become Accepted, governance must record completion of:

- Architecture review of the immediate post-BPAY selection, Return-only decomposition, title, path, scope code, and dependency-closure rationale;
- affected Return ownership review confirming accurate specialization of the Approved Return Domain;
- affected Order ownership review confirming bounded BORD evidence consumption and preserved Order authority;
- affected Inventory ownership review confirming bounded BINV coordination and preserved Inventory authority;
- affected Shipping and Fulfilment ownership review confirming bounded BSHP reverse-logistics coordination and preserved Shipping authority;
- affected Payment ownership review confirming bounded BPAY Refund execution and evidence consumption and preserved Payment authority;
- affected Pricing ownership review confirming bounded commercial evidence consumption and preserved Pricing authority;
- affected Identity and Customer ownership review confirming bounded Principal, Customer, Account, Address, and privacy evidence consumption and preserved authority;
- affected Administration ownership review confirming protected capability invocation transfers no Return authority and defines no Role/Permission matrix;
- affected Notifications ownership review confirming BRET selection does not authorize Notifications or define an event, payload, provider, or delivery mechanism;
- affected Reporting ownership review confirming BRET selection does not authorize Reporting or define source, projection, export, or pipeline mechanisms;
- affected CMS ownership review confirming CMS remains independently eligible, separate, unresolved, unranked, and unordered;
- Security review of contextual Authorization, object isolation, Sensitive Data, tamper resistance, replay, duplicate effects, fraud non-authority, and recovery;
- Testing review of future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, cross-references, open decisions, and roadmap containment;
- confirmation that `BRET` is unique among current Specification scope codes;
- confirmation that `specifications/backend/return/return-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BRET is established; and
- synchronized canonical updates to ADR-0014, `ARCHITECTURE.md`, and `DECISIONS.md`.

No review is represented as completed while ADR-0014 remains Proposed. No reviewer name, signature, ticket, or external approval artifact is asserted.

## Acceptance Conditions and Synchronization

This Proposed decision becomes Accepted only after:

- governance approves BRET as the immediate Backend Specification after BPAY;
- required reviews confirm Return-only specialization and preservation of every external authority;
- review confirms the dependency-closure rationale without ranking Return or CMS by business or technical merit;
- review confirms CMS remains independently eligible, separate, unresolved, unranked, and unordered;
- review confirms Administration remains blocked by missing Approved CMS, Return, Notifications, and Reporting invocation Contracts during the BRET Draft lifecycle;
- review confirms Notifications remains blocked by a missing Approved Return producer Contract during the BRET Draft lifecycle;
- review confirms Reporting remains blocked by missing Approved Return and CMS source Contracts during the BRET Draft lifecycle;
- review confirms every post-BRET roadmap position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized by recording BPAY as `1.0.0 Approved` and `specifications/backend/payment/payment-backend.md` as existing and Approved; removing obsolete wording that BPAY is not Approved or that its file need not exist; recording that Approved BPAY closed Return's previously missing Payment/Refund backend prerequisite; establishing Return-only BRET immediately after BPAY with authorization only to enter Draft after ADR-0014 acceptance and canonical synchronization; preserving CMS as independently eligible, separate, unresolved, unranked, and unordered; preserving the exact remaining Administration, Notifications, and Reporting dependency state; and leaving every post-BRET roadmap position unresolved;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0014 as Accepted; and
- ADR-0014 lifecycle wording is synchronized to the Accepted decision.

Acceptance synchronization must modify only ADR-0014, `ARCHITECTURE.md`, and `DECISIONS.md` unless review discovers a direct contradiction requiring a separately governed correction. `PRODUCT.md` requires no change unless such a contradiction is discovered.

Until every acceptance condition and canonical synchronization step is complete, BRET remains unauthorized for Draft governance.

## Validation Criteria

Acceptance review must verify that:

1. ADR-0014 is `0.1.0 Proposed`, `authoritative: true`, dated and last updated `2026-09-23`;
2. the canonical pre-decision sequence ends at `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY`;
3. the post-BPAY audit accurately identifies CMS and Return as independently eligible and Administration, Notifications, and Reporting as dependency-blocked;
4. CMS and Return are represented fairly without subjective ranking;
5. Return is the sole selected immediate post-BPAY capability through governance dependency closure;
6. the proposed Specification is exactly the Return Backend Specification at `specifications/backend/return/return-backend.md` under unique scope `BRET` with Return-only decomposition;
7. BRET is authorized only for Draft after ADR acceptance and canonical synchronization and is not Approved;
8. CMS remains independently eligible, separate, unresolved, unranked, and unordered;
9. Administration retains missing CMS, Return, Notifications, and Reporting invocation dependencies during the BRET Draft lifecycle;
10. Notifications retains its missing Approved Return producer dependency during the BRET Draft lifecycle;
11. Reporting retains its missing Approved Return and CMS source dependencies during the BRET Draft lifecycle;
12. all 21 listed Product Decisions and 11 listed Architecture Decisions remain exact and unresolved;
13. no provider, Product policy, Contract design, implementation mechanism, infrastructure choice, or numerical value is selected;
14. acceptance synchronization remains limited to ADR-0014, `ARCHITECTURE.md`, and `DECISIONS.md` unless a separately governed correction is required;
15. every Backend Specification identity, title, path, scope code, decomposition, and ordering position after BRET remains unresolved; and
16. acceptance synchronization affects only `specifications/adr/ADR-0014-post-payment-backend-specification.md`, `.ai/core/ARCHITECTURE.md`, and `.ai/core/DECISIONS.md` unless a separately governed correction is required, and introduces no unrelated repository changes.

## Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0013-post-shipping-backend-specification.md`
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
- `specifications/backend/shipping/shipping-backend.md`
- `specifications/backend/payment/payment-backend.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/return/return-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/domains/notifications/notifications-domain.md`
- `specifications/domains/reporting/reporting-domain.md`

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-23 | Proposed | Proposed Return-only BRET immediately after BPAY through governance dependency closure while preserving CMS as independently eligible, unresolved, unranked, and unordered and leaving every post-BRET position unresolved. |
