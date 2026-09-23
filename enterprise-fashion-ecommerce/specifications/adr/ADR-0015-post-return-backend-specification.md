# ADR-0015 — Post-Return Backend Specification

## Identifier

ADR-0015

## Title

Post-Return Backend Specification

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

## Scope

backend-roadmap

## Context

Accepted ADR-0001 through ADR-0014 and synchronized `ARCHITECTURE.md` §35 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY → BRET`. The Return Backend Specification is `1.0.0 Approved`, `authoritative: false`, and governed under scope `BRET`. Accepted ADR-0014 establishes no backend position after BRET.

The completed post-BRET eligibility audit found exactly two independently eligible capabilities: CMS and Notifications. Reporting remains blocked only by a missing Approved CMS backend source Contract. Administration remains blocked by missing Approved CMS, Notifications, and Reporting backend invocation Contracts. The audit did not select, rank, or order CMS and Notifications, so a governance decision is required before another Backend Specification may enter Draft.

This Proposed decision evaluates CMS and Notifications symmetrically through governed dependency closure. A future Approved CMS backend Contract would close Reporting's remaining identified source prerequisite and Administration's CMS invocation prerequisite. A future Approved Notifications backend Contract would close Administration's Notifications invocation prerequisite, while Reporting would remain blocked by CMS. This proposal therefore selects CMS as the stronger dependency-closing boundary. This reasoning is not a judgment of business value, technical merit, implementation difficulty, delivery speed, perceived importance, or preference.

## Decision Drivers

- Canonical governance establishes no backend position after BRET.
- CMS and Notifications are independently eligible, so eligibility alone does not determine the immediate position.
- Approved BRET closes Reporting's Return source prerequisite and Administration's Return invocation prerequisite.
- Reporting's only remaining identified backend prerequisite is an Approved CMS source Contract.
- Administration still requires Approved CMS, Notifications, and Reporting invocation Contracts.
- Future CMS approval would close Reporting's remaining prerequisite and one Administration prerequisite.
- Future Notifications approval would close one Administration prerequisite but would not close Reporting's missing CMS source.
- The unselected eligible capability must remain independently eligible, separate, unresolved, unranked, and unordered.
- Every Product and Architecture decision material to the selected capability must remain unresolved.

## Considered Independently Eligible Alternatives

### A. CMS

CMS is independently eligible from existing Approved Product, Category, Pricing, Inventory, Identity, Customer, Search and Discovery, Return, and other governed boundaries. The Approved CMS Domain defines bounded content, version, publication evidence, placement, presentation eligibility, history, recovery, reconciliation, Contract, and conditional-event semantics without transferring external authority.

A future Approved CMS backend would supply Reporting's remaining CMS source Contract and Administration's CMS invocation Contract. Because Approved BRET has already closed Reporting's Return source prerequisite, future CMS approval would close Reporting's currently known remaining backend prerequisite. Notifications would remain independently eligible and would not acquire a CMS prerequisite. Administration would continue to lack Approved Notifications and Reporting invocation Contracts.

Content approval and publication workflow, Product taxonomy, Product media, Roles and Permissions, escalation, reporting/export policy, providers, search implementation, messaging, storage, infrastructure, Contracts, events, and other open decisions can remain unresolved in a mechanism-neutral CMS Draft.

### B. Notifications

Notifications is independently eligible from existing Approved producer and preference boundaries, including Approved BRET. The Approved Notifications Domain can be specialized without transferring Identity, Customer, Consent, Preference, Return, CMS, Administration, Reporting, or other Domain authority.

A future Approved Notifications backend would close Administration's Notifications invocation prerequisite. Reporting would remain blocked by its missing Approved CMS source Contract, and Administration would continue to lack Approved CMS and Reporting invocation Contracts. CMS would remain independently eligible.

Notifications is not selected for the single immediate position. It remains independently eligible, separate, unresolved, unranked, and unordered relative to every unresolved capability. Deferral is not rejection and assigns no later Notifications position.

### C. Defer the Decision

Deferral would preserve the two-candidate ambiguity and establish no post-BRET position. It remains a valid governance option but would leave Reporting's final identified prerequisite and all three remaining Administration invocation prerequisites unresolved.

## Dependency Comparison

| Consideration | CMS | Notifications |
| --- | --- | --- |
| Independently eligible now | Yes; existing Approved boundaries support mechanism-neutral CMS specialization. | Yes; existing Approved producer, Customer, Consent, Preference, and Return boundaries support mechanism-neutral Notifications specialization. |
| Direct downstream closure after future approval | Reporting gains its remaining CMS source; Administration gains its CMS invocation boundary. | Administration gains its Notifications invocation boundary. |
| Dependencies still blocked after future approval | Administration still lacks Notifications and Reporting invocation Contracts. | Reporting still lacks CMS; Administration still lacks CMS and Reporting. |
| Authority containment | CMS remains bounded from Product, Category, Pricing, Inventory, Identity, Customer, Search and Discovery, Administration, Notifications, Reporting, and other owners. | Notifications remains bounded from producers, Identity, Customer, Consent, Preference, CMS, Administration, Reporting, and other owners. |
| Open decisions | Content, publication, media, Roles and Permissions, reporting, provider, search, messaging, infrastructure, and mechanisms remain unresolved. | Communication policy, Consent and Preference, provider, messaging, Roles and Permissions, infrastructure, and mechanisms remain unresolved. |

## Proposed Decision

ADR-0015 proposes that the fourteenth downstream Backend Specification after BEB, immediately after BRET, SHALL be:

- **Capability:** CMS
- **Specification:** CMS Backend Specification
- **Path:** `specifications/backend/cms/cms-backend.md`
- **Scope code:** `BCMS`
- **Decomposition:** CMS-only; specializes exactly the Approved CMS Domain
- **Position:** Fourteenth downstream Backend Specification after BEB, immediately after BRET

`BCMS` means Backend CMS. It is collision-free among current Specification scope codes and establishes no naming rule for later scopes.

If Accepted, BCMS SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved CMS Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

The proposed canonical sequence is `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY → BRET → BCMS`.

Proposed ADR-0015 does not authorize BCMS drafting. BCMS may enter Draft only after ADR-0015 is Accepted and canonical `ARCHITECTURE.md` and `DECISIONS.md` synchronization is complete. That authorization would not approve BCMS; BCMS would require its own Draft-to-Approved lifecycle.

## Authority and Decomposition Boundaries

- **CMS:** BCMS may specialize content identity, content versions, placement, presentation eligibility, publication and withdrawal evidence, history, recovery, reconciliation, and other behavior already governed by the Approved CMS Domain. It must not expand or transfer that Domain's authority.
- **BEB:** BCMS must inherit and explicitly trace every materially applicable BEB Requirement without duplicating, weakening, or transferring shared backend governance.
- **BIDN and BCUS:** trusted Principal, Authentication, Customer, Account, Consent, Preference, contact, and ownership evidence may be consumed where materially applicable; Identity, credentials, Sessions, Customer, Account, Consent, Preference, and source authority do not transfer.
- **BPRD and BCAT:** governed Product, Product Variant, Category, taxonomy, classification, and catalogue references may be consumed; their identity, definition, lifecycle, hierarchy, and membership authority do not transfer.
- **BINV and BPRC:** Inventory availability and Pricing evidence may be referenced for governed presentation; Stock, Reservation, availability, Money, Price, Discount, Promotion, Voucher, Tax, fee, and calculation authority do not transfer.
- **BCART, BCHK, BORD, BSHP, BPAY, and BRET:** governed commerce references or policy content may be presented only through bounded Contracts; no Cart, Checkout, Order, Shipping and Fulfilment, Payment, Refund, or Return authority transfers.
- **BSRCH:** CMS content may be exposed for governed discovery consumption; search indexes, ranking, query behavior, extraction, and Search and Discovery truth remain external.
- **Notifications:** bounded publication facts or content references may eventually be exposed through governed Contracts; communication eligibility, delivery, provider, channel, Consent, and Preference authority do not transfer.
- **Reporting:** bounded CMS evidence may eventually be exposed through governed Contracts; reporting, analytics, exports, Projections, and interpretation remain non-authoritative and do not mutate CMS truth.
- **Administration:** protected Staff workflows may invoke explicit BCMS capabilities without bypassing CMS invariants or acquiring CMS authority. No administrative API or Role/Permission matrix is selected.
- **All other Domains:** every owning-Domain truth remains external; presentation, content, copies, indexes, caches, and Projections cannot establish another Domain's truth.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, identifiers, Role labels, Permissions, Claims, client state, content state, or derived representations must not independently authorize an action.

## Dependency Consequences

If ADR-0015 is Accepted and canonical synchronization is completed, only a BCMS Draft is authorized. Selecting BCMS does not itself close downstream dependencies. Those dependencies can close only after a future BCMS Specification becomes Approved and exposes the applicable governed Contracts.

- **Reporting:** remains separate and dependency-blocked by the missing Approved CMS source Contract during the BCMS Draft lifecycle. Approved BRET has already closed its Return source prerequisite. Future BCMS approval may close Reporting's currently known remaining backend prerequisite, but does not authorize Reporting or establish its position.
- **Administration:** remains separate and dependency-blocked by missing Approved CMS, Notifications, and Reporting invocation Contracts during the BCMS Draft lifecycle. Future BCMS approval may close its CMS dependency, while Notifications and Reporting would remain missing.
- **Notifications:** remains independently eligible, separate, unresolved, unranked, and unordered. CMS is not a mandatory Notifications prerequisite.

No downstream capability is authorized by this dependency analysis.

## Open Product Decisions

This Proposed decision preserves the following **19 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved CMS Domain §7:

| Item | Open Product Decision | Preserved BCMS boundary |
| ---: | --- | --- |
| 1 | Final brand name and visual identity. | No brand name, identity, visual system, or presentation rule is selected. |
| 2 | Initial product categories and catalogue taxonomy. | No taxonomy, category structure, or catalogue policy is selected. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, fee, or shipping-policy content is made authoritative. |
| 8 | Free-delivery threshold and promotional treatment. | No threshold, qualification, or promotional treatment is selected. |
| 9 | Tax-inclusive display and invoice requirements. | No tax, display, invoice, or document policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility, cutoff, or policy is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, exchange, Refund, eligibility, window, or treatment policy is selected. |
| 14 | Voucher and promotion stacking policy. | No stacking, precedence, eligibility, or commercial calculation is selected. |
| 15 | Product-review support. | No review capability, content model, moderation, or publication policy is selected. |
| 17 | Low-stock and out-of-stock customer messaging. | No threshold, availability rule, or customer-message policy is selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, expectation, workflow, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No Consent, Preference, marketing, or communication policy is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, event taxonomy, instrumentation, or mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, export, audience, format, purpose, or schedule is selected. |
| 22 | Content approval and scheduled-publication workflow. | No approval flow, reviewer model, schedule rule, or publication mechanism is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No escalation owner, route, workflow, expectation, or target is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, accounting, or presentation policy is selected. |
| 30 | Product launch date, release scope, and post-launch support window. | No date, release scope, content launch rule, or support window is selected. |

No listed Product Decision is resolved by selecting BCMS.

## Open Architecture Decisions

This Proposed decision preserves the following **11 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BCMS boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 7 | Transactional notification provider selection. | No notification provider, channel, or delivery mechanism is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, Session, publication, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport is selected. |
| 10 | Initial search implementation details and extraction thresholds. | No Search implementation, index, extraction rule, threshold, or provider is selected. |
| 11 | Product-media upload and transformation strategy. | No upload, storage, transformation, derivative, or delivery mechanism is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or CMS persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BCMS. Frontend hosting, Payment provider selection, and Shipping provider selection remain unresolved but are not materially required for this CMS-only roadmap decision.

## Explicit Non-Decisions

ADR-0015 does not create BCMS Requirements or `cms-backend.md`; select content types, schemas, taxonomy, approval roles, publication workflow, scheduling rules, review or moderation policy, media handling, Product-media upload or transformation, Search extraction or indexing, Notification delivery, reporting or export behavior, escalation, import or migration behavior, API route, HTTP method or status, DTO, payload, database schema, table, column, index, ORM mapping, identifier format, event name or payload, topic, queue, broker, provider, cache, Redis use, storage service, infrastructure product, hosting topology, deployment mechanism, retry count, timeout, TTL, retention period, rate limit, SLA, SLO, recovery objective, or other numerical value.

It defines no Role or Permission matrix, concrete Authorization mechanism, downstream Contract design, or post-BCMS roadmap position.

## Consequences

If Accepted and canonically synchronized:

- BCMS will be authorized to enter Draft lifecycle as the immediate Backend Specification after BRET;
- BCMS may specialize only behavior already governed by the Approved CMS Domain and consume bounded Approved upstream Contracts without transferring authority;
- BCMS must complete its own Draft-to-Approved lifecycle before becoming an Approved normative backend Specification;
- Notifications will remain independently eligible, separate, unresolved, unranked, and unordered;
- Reporting and Administration will remain separate and dependency-blocked during the BCMS Draft lifecycle;
- future BCMS approval may close Reporting's currently known remaining source dependency and Administration's CMS invocation dependency without authorizing either capability; and
- every position after BCMS will remain unresolved.

The trade-off is that independently eligible Notifications remains unresolved while the broader documented dependency closure associated with CMS is pursued. This is a governance consequence, not a ranking of value, priority, effort, simplicity, commercial importance, or technical quality.

## Roadmap Containment

The sole roadmap position proposed by ADR-0015 is BCMS immediately after BRET. This Proposed decision establishes no backend identity, title, path, scope code, decomposition, or ordering position after BCMS.

Notifications remains independently eligible, separate, unresolved, unranked, and unordered. Reporting and Administration remain separate and unresolved with the dependency state recorded above. No ordering among these capabilities is stated or implied. A future eligibility audit or Accepted ADR must govern any later position.

## Required Governance Reviews

Before ADR-0015 may become Accepted, governance must record completion of:

- Architecture review of the immediate post-BRET selection, CMS-only decomposition, title, path, scope code, ordinal position, and dependency-closure rationale;
- affected CMS ownership review confirming accurate specialization of the Approved CMS Domain;
- affected Notifications ownership review confirming Notifications remains independently eligible, separate, unresolved, unranked, and unordered and acquires no CMS prerequisite;
- affected Reporting ownership review confirming its Return source prerequisite is closed, its CMS source remains missing during BCMS Draft, and selection does not authorize Reporting;
- affected Administration ownership review confirming its CMS, Notifications, and Reporting invocation dependencies and preservation of owning-Domain authority;
- affected Product and Category ownership review confirming bounded catalogue references and preserved Product and Category authority;
- affected Pricing and Inventory ownership review confirming bounded presentation evidence and preserved Pricing and Inventory authority;
- affected Search and Discovery ownership review confirming preserved Search authority and unresolved implementation and extraction decisions;
- affected Identity and Customer ownership review confirming bounded Principal, Customer, Account, Consent, Preference, and privacy evidence consumption and preserved authority;
- affected Cart, Checkout, Order, Shipping and Fulfilment, Payment, and Return ownership review where policy content or governed references intersect CMS presentation;
- Security review of contextual Authorization, object isolation, Sensitive Data, content integrity, tamper resistance, and recovery;
- Testing review of future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, cross-references, open decisions, and roadmap containment;
- confirmation that `BCMS` is unique among current Specification scope codes;
- confirmation that `specifications/backend/cms/cms-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BCMS is established; and
- synchronized canonical updates to ADR-0015, `ARCHITECTURE.md`, and `DECISIONS.md`.

No reviewer name, signature, ticket, or external approval artifact is asserted by this Proposed ADR.

## Acceptance Conditions and Synchronization

This Proposed decision becomes Accepted only after:

- governance approves BCMS as the immediate Backend Specification after BRET;
- required reviews confirm CMS-only specialization and preservation of every external authority;
- review confirms the dependency-closure rationale without ranking CMS or Notifications by business or technical merit;
- review confirms Notifications remains independently eligible, separate, unresolved, unranked, and unordered;
- review confirms Reporting remains blocked by a missing Approved CMS source Contract during the BCMS Draft lifecycle and that Approved BRET has closed its Return source prerequisite;
- review confirms Administration remains blocked by missing Approved CMS, Notifications, and Reporting invocation Contracts during the BCMS Draft lifecycle;
- review confirms every post-BCMS roadmap position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized by recording BRET as `1.0.0 Approved` and `specifications/backend/return/return-backend.md` as existing and Approved; removing obsolete wording that BRET is not Approved or that its file need not exist; recording that Approved BRET closed Reporting's Return source prerequisite and Administration's Return invocation prerequisite; establishing CMS-only BCMS immediately after BRET with authorization only to enter Draft after ADR-0015 acceptance and canonical synchronization; preserving Notifications as independently eligible, separate, unresolved, unranked, and unordered; preserving the exact remaining Administration and Reporting dependency state; and leaving every post-BCMS roadmap position unresolved;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0015 as Accepted; and
- ADR-0015 lifecycle wording is synchronized to the Accepted decision.

Acceptance synchronization must modify only ADR-0015, `ARCHITECTURE.md`, and `DECISIONS.md` unless review discovers a direct contradiction requiring a separately governed correction. `PRODUCT.md` requires no change unless such a contradiction is discovered.

Until all acceptance conditions and canonical synchronization are complete, ADR-0015 remains Proposed and BCMS is unauthorized for drafting.

## Validation Criteria

Before acceptance, reviewers must verify that:

1. metadata remains `0.1.0 Proposed`, `authoritative: true`, owner `Architecture`, and scope `backend-roadmap`;
2. exactly one immediate post-BRET specification is selected: CMS-only BCMS at `specifications/backend/cms/cms-backend.md`;
3. the proposed position is the fourteenth downstream Backend Specification after BEB, immediately after BRET;
4. the selection is supported only by governed dependency closure;
5. Notifications remains independently eligible, separate, unresolved, unranked, and unordered;
6. Reporting remains blocked during the BCMS Draft lifecycle by its missing Approved CMS source Contract, and Administration remains blocked by missing Approved CMS, Notifications, and Reporting invocation Contracts;
7. no post-BCMS Backend Specification identity, title, path, scope code, decomposition, or ordering position is established;
8. `specifications/backend/cms/cms-backend.md` is not created by this Proposed decision;
9. all 19 listed Product Decisions and all 11 listed Architecture Decisions remain unresolved;
10. owning-Domain authority and contextual Authorization remain preserved;
11. no concrete API, DTO, persistence, event, provider, cache, infrastructure, deployment, numerical, Role/Permission, or unresolved policy decision is selected;
12. acceptance requires synchronized lifecycle and canonical changes to ADR-0015, `ARCHITECTURE.md`, and `DECISIONS.md` before BCMS may enter Draft;
13. only `specifications/adr/ADR-0015-post-return-backend-specification.md` is created by this Proposed lifecycle change, whitespace validation passes, and no unrelated repository changes are introduced.

## Related Documents

- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [ADR-0001 — Backend Specification Roadmap](ADR-0001-backend-specification-roadmap.md)
- [ADR-0014 — Post-Payment Backend Specification](ADR-0014-post-payment-backend-specification.md)
- [Shared Backend Baseline Specification](../backend/shared/backend-baseline.md)
- [Return Backend Specification](../backend/return/return-backend.md)
- [CMS Domain Specification](../domains/cms/cms-domain.md)
- [Notifications Domain Specification](../domains/notifications/notifications-domain.md)
- [Reporting Domain Specification](../domains/reporting/reporting-domain.md)
- [Administration Domain Specification](../domains/admin/admin-domain.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-23 | Proposed | Proposed CMS-only BCMS as the immediate Backend Specification after Approved BRET using governed dependency closure while preserving Notifications eligibility and every later roadmap position as unresolved. |
