# ADR-0016 — Post-CMS Backend Specification

## Identifier

ADR-0016

## Title

Post-CMS Backend Specification

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

Accepted ADR-0001 through ADR-0015 establish the canonical backend sequence `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY → BRET → BCMS`. The CMS Backend Specification exists at `specifications/backend/cms/cms-backend.md` as `1.0.0 Approved`, `authoritative: false`, under scope `BCMS`. BCMS is the fourteenth downstream Backend Specification after BEB. No post-BCMS roadmap position is governed.

The completed post-BCMS eligibility audit found exactly two independently eligible capabilities: Notifications and Reporting. Administration remains ineligible because it lacks Approved Notifications and Reporting backend invocation Contracts. Approved BRET has closed the applicable Return producer, source, and invocation prerequisites; Approved BCMS has closed Reporting's CMS source prerequisite and Administration's CMS invocation prerequisite.

Canonical `ARCHITECTURE.md` §35 remains lifecycle-stale: it says BCMS is not Approved and that `cms-backend.md` does not exist. That text does not match the committed `1.0.0 Approved` BCMS Specification and must be corrected during any ADR-0016 acceptance synchronization. This Proposed ADR does not modify canonical files.

## Eligibility Evidence

### Notifications

Notifications is independently eligible. Approved BRET closes its previously missing Return producer prerequisite. Approved Identity, Customer and Account, Product, Category, Inventory, Pricing, Cart, Checkout, Order, Shipping and Fulfilment, Payment, Return, CMS, and other governed boundaries supply the source and recipient evidence needed for mechanism-neutral specialization of the Approved Notifications Domain. CMS remains conditional where CMS-owned content contributes and is not a mandatory prerequisite for every Notification.

### Reporting

Reporting is independently eligible. Approved BRET closes its Return source prerequisite, and Approved BCMS closes its previously missing CMS source prerequisite. Existing Approved source Domains and Backend Specifications provide the other governed evidence required for mechanism-neutral specialization of the Approved Reporting Domain.

### Administration

Administration is not eligible. Approved BRET closes its Return invocation prerequisite and Approved BCMS closes its CMS invocation prerequisite, but Approved Notifications and Reporting backend invocation Contracts remain missing. Administration cannot be drafted authority-safely without inventing those Contracts.

## Dependency-Closure Comparison

| Consideration | Notifications | Reporting |
| --- | --- | --- |
| Independently eligible now | Yes | Yes |
| Direct closure after future approval | Closes Administration's Notifications invocation prerequisite | Closes Administration's Reporting invocation prerequisite |
| Administration state afterward | Still lacks Approved Reporting invocation Contract | Still lacks Approved Notifications invocation Contract |
| Other eligible capability | Reporting remains independently eligible | Notifications remains independently eligible |
| Net governed closure | One remaining Administration prerequisite | One remaining Administration prerequisite |

The dependency-closure comparison is tied. Neither selection closes more governed prerequisites, makes Administration eligible, or establishes a governance advantage over the other. Repository governance and prior Accepted roadmap ADRs establish no deterministic tie-break rule for this tied state.

## Tie-Resolution Rationale

Because continued deferral would preserve ambiguity and no canonical tie-break convention exists, ADR-0016 proposes the narrowest reversible sequencing decision necessary to make progress: assign exactly one immediate position and leave every other position unresolved. It selects Notifications for that single position without asserting superiority, priority, preference, business value, technical merit, lower effort, delivery advantage, or an ordering rule.

This selection is the decision being proposed; it is not derived from an invented dependency distinction. Reporting's eligibility is unchanged, and the decision creates no presumption that Reporting must follow Notifications.

## Decision

ADR-0016 proposes that the fifteenth downstream Backend Specification after BEB, immediately after BCMS, SHALL be:

- **Capability:** Notifications
- **Specification:** Notifications Backend Specification
- **Path:** `specifications/backend/notifications/notifications-backend.md`
- **Scope code:** `BNTF`
- **Decomposition:** Notifications-only; specializes exactly the Approved Notifications Domain
- **Position:** Fifteenth downstream Backend Specification after BEB, immediately after BCMS

`BNTF` derives from the established backend `B` prefix and canonical Notifications Domain scope `NTF`. It is collision-free among current Specification scope codes and establishes no naming convention for later scopes.

If Accepted, BNTF SHALL inherit and explicitly trace every materially applicable BEB Requirement, specialize only the Approved Notifications Domain, and consume governed Contracts or evidence without acquiring another Domain's authority.

The proposed canonical sequence is `BEB → BIDN → BCUS → BPRD → BINV → BPRC → BCART → BCAT → BSRCH → BCHK → BORD → BSHP → BPAY → BRET → BCMS → BNTF`.

Proposed ADR-0016 does not authorize BNTF drafting. BNTF may enter Draft only after ADR-0016 is Accepted and canonical `ARCHITECTURE.md` and `DECISIONS.md` synchronization is complete. That authorization would not approve BNTF; it would require its own Draft-to-Approved lifecycle.

## Unselected Eligible Capability

Reporting remains independently eligible, separate, unresolved, unranked, and unordered beyond the single immediate BNTF position. ADR-0016 does not authorize Reporting to enter Draft, assign it a path or backend scope, or imply that it follows BNTF.

## Administration Dependency State

During a future BNTF Draft, Administration remains blocked by missing Approved Notifications and Reporting backend invocation Contracts. A future Approved BNTF may close only the Notifications invocation prerequisite. The Reporting invocation prerequisite would remain missing, so BNTF approval alone would not make Administration eligible or authorize its Draft.

## Authority and Decomposition Boundaries

- **Notifications:** BNTF may specialize Notification identity, request receipt and outcomes, notification-specific template evidence, recipient delivery context, delivery-attempt identity, retry state, Delivery Status and history, provider evidence, recovery, and reconciliation already governed by the Approved Notifications Domain. It must not expand that Domain's authority.
- **BEB:** BNTF must inherit and explicitly trace every materially applicable BEB Requirement without weakening or transferring shared backend governance.
- **BIDN and BCUS:** trusted Principal, Authentication, Customer, Account, contact, Consent, Preference, Notification Preference, and purpose evidence may be consumed where materially applicable; Identity, credentials, Sessions, Customer, Account, Consent, Preference, and current source authority do not transfer.
- **BPRD, BCAT, BINV, and BPRC:** governed catalogue, Category, availability, Money, Price, Promotion, and related facts may be represented only where an approved communication requires them; source identity, taxonomy, commercial, and Inventory authority do not transfer.
- **BCART and BCHK:** governed shopping-intent or purchase-orchestration facts may be consumed only where communication is governed; Cart and Checkout truth do not transfer.
- **BORD, BSHP, BPAY, and BRET:** governed Order, Shipping, Payment, Refund, and Return facts may be consumed for communications; their identity, lifecycle, effects, and outcome authority do not transfer.
- **BCMS:** governed CMS content may contribute where applicable without transferring CMS publication, placement, policy-presentation, or content authority; CMS is not a universal Notifications prerequisite.
- **Search and Discovery:** Notification behavior does not acquire index, ranking, query, or discovery authority.
- **Reporting:** bounded Notifications evidence may eventually be exposed through governed Contracts; reports, analytics, exports, Projections, definitions, and interpretation remain Reporting-owned and non-authoritative for Notification truth.
- **Administration:** protected Staff workflows may eventually invoke explicit BNTF capabilities without bypassing Notifications invariants or acquiring Notifications authority. No Administration workflow, Role/Permission matrix, or escalation policy is selected.
- **All other Domains:** the authoritative fact being communicated remains owned by its producing Domain, and no request, template, attempt, provider evidence, client display, or delivery outcome establishes or changes that fact.

Contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state. Authentication, identifiers, Role labels, Permissions, Claims, recipient data, template state, provider evidence, or client state must not independently authorize an action.

## Open Product Decisions

This Proposed decision preserves the following **15 materially applicable Open Product Decisions** from `PRODUCT.md` §24 and the Approved Notifications Domain §31:

| Item | Open Product Decision | Preserved BNTF boundary |
| ---: | --- | --- |
| 4 | Guest checkout versus mandatory account rules. | No Visitor, Account, recipient, or communication prerequisite is selected. |
| 5 | Customer email-verification requirements. | No verification requirement or mechanism is selected, and delivery proves no verification. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | No provider, service, area, fee, or Shipping policy is selected. |
| 10 | Cancellation eligibility and cutoff policy. | No cancellation eligibility or cutoff is selected. |
| 11 | Returns, exchanges, and refund policy. | No Return, Exchange, Refund, eligibility, or treatment policy is selected. |
| 18 | Customer-support channels and service expectations. | No support channel, hours, workflow, expectation, or target is selected. |
| 19 | Marketing-consent and communication-preference model. | No marketing permission, Consent, Preference, channel, fallback, or suppression policy is selected. |
| 20 | Initial analytics provider and event taxonomy. | No analytics provider, event taxonomy, or instrumentation mechanism is selected. |
| 21 | Initial reporting and export requirements. | No report, export, audience, purpose, format, or schedule is selected. |
| 22 | Content approval and scheduled-publication workflow. | No template or contributed-content approval chain, schedule rule, or publication mechanism is selected. |
| 23 | Administrative role and permission matrix. | Contextual Authorization is required without defining Roles, Permissions, mappings, or a matrix. |
| 24 | Production customer-service and operational escalation process. | No resend, investigation, recovery, escalation, service, or support process is selected. |
| 25 | South African tax-display, invoice, and credit-note policy. | No tax, invoice, Credit Note, legal, or document-content policy is selected. |
| 26 | Fraud-screening approach and manual-review workflow. | No fraud rule, provider, score, threshold, review, or communication policy is selected. |
| 28 | Customer data export, correction, deletion, and account-closure workflow. | No contact-data, delivery-evidence, retention, correction, deletion, or closure policy is selected. |

No listed Product Decision is resolved by selecting BNTF.

## Open Architecture Decisions

This Proposed decision preserves the following **9 materially applicable Open Architecture Decisions** from `ARCHITECTURE.md` §34:

| Item | Open Architecture Decision | Preserved BNTF boundary |
| ---: | --- | --- |
| 1 | Azure App Service versus Azure Container Apps for backend hosting. | No hosting service or topology is selected. |
| 2 | Bicep versus Terraform for infrastructure as code. | No infrastructure-as-code mechanism is selected. |
| 3 | Customer and administrator session/token strategy. | No Session or token mechanism is selected. |
| 7 | Transactional notification provider selection. | No notification provider, channel implementation, SDK, or adapter is selected. |
| 8 | Redis introduction and its approved use cases. | No Redis, cache, idempotency, retry, or workflow-state use is selected. |
| 9 | External messaging introduction and service selection. | No service, broker, topic, queue, event, or transport is selected. |
| 12 | Backup retention and production recovery objectives. | No retention, backup mechanism, recovery objective, or numerical target is selected. |
| 13 | PostgreSQL schema strategy for enforcing domain ownership within the modular monolith. | No physical schema or Notifications persistence layout is selected. |
| 14 | Repository-wide feature-flag implementation and lifecycle-management approach. | No flag mechanism, provider, rollout system, or lifecycle is selected. |

No listed Architecture Decision is resolved by selecting BNTF. Frontend hosting, Payment provider selection, Shipping provider selection, initial Search implementation, and Product-media upload and transformation remain unresolved but are not materially required for this Notifications-only roadmap decision.

## Consequences

If Accepted and canonically synchronized:

- BNTF will be authorized to enter Draft as the immediate Backend Specification after BCMS;
- BNTF may specialize only behavior governed by the Approved Notifications Domain and consume bounded Approved Contracts without transferring authority;
- BNTF must complete its own Draft-to-Approved lifecycle;
- Reporting will remain independently eligible, separate, unresolved, unranked, and unordered beyond the immediate BNTF position;
- Administration will remain blocked during BNTF Draft by missing Approved Notifications and Reporting invocation Contracts;
- future BNTF approval may close Administration's Notifications invocation prerequisite while Reporting remains missing; and
- every position after BNTF will remain unresolved.

The trade-off is that independently eligible Reporting remains unresolved while one tied candidate is assigned the immediate position. This is a narrow sequencing choice, not a ranking of value, priority, effort, simplicity, importance, or technical quality.

## Explicit Non-Decisions

ADR-0016 does not create BNTF Requirements or `notifications-backend.md`; select communication eligibility, transactional or marketing classification, Consent or Preference policy, channel availability or priority, fallback, suppression, cancellation, template approval, provider, retry policy, exhaustion policy, escalation, retention, API route, HTTP method or status, DTO, payload, database schema, table, column, index, ORM mapping, identifier format, event name or payload, topic, queue, broker, cache, Redis use, storage, infrastructure product, hosting topology, deployment mechanism, retry count, timeout, TTL, rate limit, SLA, SLO, recovery objective, Role/Permission matrix, or other numerical or unresolved Product policy.

It defines no concrete downstream Contract and no post-BNTF roadmap position.

## Roadmap Containment

The sole roadmap position proposed by ADR-0016 is BNTF immediately after BCMS. This Proposed decision establishes no backend identity, title, path, scope code, decomposition, or ordering position after BNTF.

Reporting remains independently eligible, separate, unresolved, unranked, and unordered beyond the immediate decision. Administration remains unresolved and dependency-blocked as recorded above. ADR-0016 does not authorize either capability or state or imply which capability follows BNTF.

## Required Governance Reviews

Before ADR-0016 may become Accepted, governance must record completion of:

- Architecture review of the tied dependency state, narrow tie resolution, Notifications-only decomposition, title, path, scope, and ordinal position;
- affected Notifications ownership review confirming accurate specialization of the Approved Notifications Domain;
- affected Reporting ownership review confirming unchanged independent eligibility and no assigned later position;
- affected Administration ownership review confirming both invocation prerequisites remain missing during BNTF Draft and one would remain after future BNTF approval;
- affected Identity and Customer ownership review confirming bounded Principal, Customer, Account, contact, Consent, Preference, and privacy evidence consumption;
- affected Product, Category, Inventory, Pricing, Cart, Checkout, Order, Shipping and Fulfilment, Payment, Return, and CMS ownership review where governed facts may be communicated;
- Security review of contextual Authorization, recipient and object isolation, Sensitive Data, Secrets, provider evidence, duplicate effects, and recovery;
- Accessibility review of equivalent communication meaning and bounded accessible outcomes;
- Testing review of future verification and traceability obligations implied by the selected boundary;
- Documentation review of lifecycle, terminology, cross-references, open decisions, tie rationale, and roadmap containment;
- confirmation that `BNTF` is unique among current Specification scope codes;
- confirmation that `specifications/backend/notifications/notifications-backend.md` is collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved;
- confirmation that no Backend Specification identity, title, path, scope code, decomposition, or order after BNTF is established; and
- synchronized canonical updates to ADR-0016, `ARCHITECTURE.md`, and `DECISIONS.md`.

No reviewer identity, signature, ticket, or external approval artifact is asserted by this Proposed ADR.

## Acceptance Conditions and Synchronization

This Proposed decision becomes Accepted only after:

- governance approves Notifications-only BNTF as the immediate Backend Specification after BCMS;
- required reviews confirm both Notifications and Reporting were independently eligible and Administration was not eligible;
- review confirms the dependency-closure comparison is tied and the selection asserts no fabricated advantage or ranking;
- review confirms Reporting remains independently eligible, separate, unresolved, unranked, and unordered beyond the immediate BNTF position;
- review confirms Administration remains blocked by missing Approved Notifications and Reporting invocation Contracts during BNTF Draft and would retain the Reporting prerequisite after future BNTF approval;
- review confirms every post-BNTF roadmap position remains unresolved;
- `ARCHITECTURE.md` §35, metadata, Document Status, and Revision History are synchronized by recording BCMS as `1.0.0 Approved` and `specifications/backend/cms/cms-backend.md` as existing and Approved; removing obsolete wording that BCMS is unapproved, missing, or Draft-only; recording that Approved BCMS closed Reporting's CMS source and Administration's CMS invocation prerequisites; establishing Notifications-only BNTF immediately after BCMS with authorization only to enter Draft after ADR-0016 acceptance and canonical synchronization; preserving Reporting as independently eligible, separate, unresolved, unranked, and unordered; preserving Administration's exact remaining Notifications and Reporting dependency state; and leaving every post-BNTF roadmap position unresolved;
- `DECISIONS.md` metadata, Decision Index, and Revision History index ADR-0016 as Accepted; and
- ADR-0016 lifecycle wording is synchronized to the Accepted decision.

Acceptance synchronization must modify only ADR-0016, `ARCHITECTURE.md`, and `DECISIONS.md` unless validation discovers a direct contradiction requiring a separately governed correction. `PRODUCT.md` requires no change unless such a contradiction is discovered.

Until all acceptance conditions and canonical synchronization are complete, ADR-0016 remains Proposed and BNTF is unauthorized for drafting.

## Validation Criteria

Before acceptance, reviewers must verify that:

1. metadata remains `0.1.0 Proposed`, `authoritative: true`, owner `Architecture`, and scope `backend-roadmap`;
2. the canonical baseline recognizes BCMS as `1.0.0 Approved` despite stale `ARCHITECTURE.md` §35 lifecycle text;
3. Notifications and Reporting were both independently eligible and Administration was not eligible;
4. exactly one immediate post-BCMS specification is selected: Notifications-only BNTF at `specifications/backend/notifications/notifications-backend.md`;
5. BNTF is the fifteenth downstream Backend Specification after BEB, immediately after BCMS;
6. the dependency-closure comparison is represented as tied, and the narrow tie resolution claims no governance advantage, ranking, or preference;
7. Reporting remains independently eligible, separate, unresolved, unranked, and unordered beyond the immediate decision;
8. Administration remains blocked by missing Approved Notifications and Reporting invocation Contracts during BNTF Draft and is not authorized;
9. no post-BNTF Backend Specification identity, title, path, scope code, decomposition, or ordering position is established;
10. `specifications/backend/notifications/notifications-backend.md` is not created by this Proposed decision;
11. all 15 listed Product Decisions and all 9 listed Architecture Decisions remain unresolved;
12. owning-Domain authority and contextual Authorization remain preserved;
13. no concrete API, DTO, persistence, event, provider, cache, infrastructure, deployment, numerical, Role/Permission, or unresolved policy decision is selected;
14. acceptance requires synchronized lifecycle and canonical changes to ADR-0016, `ARCHITECTURE.md`, and `DECISIONS.md`, including correction of stale BCMS lifecycle text, before BNTF may enter Draft;
15. only `specifications/adr/ADR-0016-post-cms-backend-specification.md` is created by this Proposed lifecycle change, whitespace validation passes, and no unrelated repository changes are introduced.

## Related Documents

- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [ADR-0001 — Backend Specification Roadmap](ADR-0001-backend-specification-roadmap.md)
- [ADR-0015 — Post-Return Backend Specification](ADR-0015-post-return-backend-specification.md)
- [Shared Backend Baseline Specification](../backend/shared/backend-baseline.md)
- [Return Backend Specification](../backend/return/return-backend.md)
- [CMS Backend Specification](../backend/cms/cms-backend.md)
- [Notifications Domain Specification](../domains/notifications/notifications-domain.md)
- [Reporting Domain Specification](../domains/reporting/reporting-domain.md)
- [Administration Domain Specification](../domains/admin/admin-domain.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-23 | Proposed | Proposed Notifications-only BNTF as the immediate Backend Specification after Approved BCMS through a narrow reversible resolution of tied dependency closure, while preserving Reporting eligibility and every post-BNTF position as unresolved. |
