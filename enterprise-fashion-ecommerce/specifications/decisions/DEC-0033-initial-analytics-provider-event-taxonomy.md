# DEC-0033 — Initial Analytics Provider and Event Taxonomy

- **Identifier:** DEC-0033
- **Title:** Initial Analytics Provider and Event Taxonomy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-14
- **Last Updated:** 2026-10-14
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Current Approved Baseline

[`PRODUCT.md` §24 item 20](../../.ai/core/PRODUCT.md) leaves “Initial analytics provider and event taxonomy” unresolved. Product §18 requires support for specified customer-journey, commercial, operational and quality measures and makes analytics consent-aware and non-authoritative for commercial truth. Product §33 requires measurement to serve defined questions, respect privacy, minimize personal data and distinguish Analytics Events from transactional records. Its candidate event categories and event-quality fields do not approve an initial catalogue, payload, provider, identifier, collection method or optional customer-behaviour tracking policy. Essential operational telemetry is not interchangeable with optional behavioural analytics and cannot be repurposed for it without applicable governance.

No initial analytics provider or event taxonomy is selected. Product owns intended measurement outcomes and analytics capability policy; [Architecture](../../.ai/core/ARCHITECTURE.md) and [`DECISIONS.md` §§30 and 38](../../.ai/core/DECISIONS.md) separately govern material technical structure and provider selection. Product policy, event purpose and taxonomy, privacy permission, provider choice, and executable Contracts or instrumentation remain separate decisions. An Analytics Event is an observation, not a [Domain Event](../../.ai/core/GLOSSARY.md) or authoritative source fact. Derived reports, including those from [Reporting](../domains/reporting/reporting-domain.md), cannot replace Order, Payment or other owning-Domain truth.

## Decision Question and Status

What initial analytics capability, provider expectations and event-taxonomy boundaries should Product approve, subject to separate Architecture, Security, Privacy, Data and Integration governance? Which measurements, if any, require optional customer-behaviour tracking, and what approved purposes and permissions would be required before collection?

**NO OPTION SELECTED.** This 0.1.0 record makes no decision, authorizes no comparative evaluation or collection, and leaves `PRODUCT.md` §24 item 20 open.

## Alternatives Considered — Not Formally Compared

| Option | Initial Product policy candidate | Potential benefit | Limitation and evidence needed |
| --- | --- | --- | --- |
| A — No optional initial behaviour tracking | Consider no optional customer-behaviour tracking beyond separately governed essential operational telemetry. | May reduce initial privacy, integration and operational burden. | Approved Product measurement obligations still require a lawful, attributable way to assess required outcomes; operational telemetry cannot silently be repurposed. Evidence needed: measurement priorities, essential-versus-optional purpose analysis, available lawful sources, Product impact and any deferred-measurement consequences. |
| B — Bounded purpose-specific analytics | Consider a limited initial capability only where purposes, event categories, source ownership, collection permission, minimization and privacy conditions are separately approved. | Could provide useful, explainable measurement within bounded scope. | Coverage may be limited and collection is not presumed authorized. Evidence needed: business questions, category-level Consent or lawful-basis assessment, source lineage, permitted identifiers and properties, Reporting use, provider-neutral requirements and operational support. |
| C — Broader initial analytics | Consider broader measurement only under additional approved purpose, tracking, access, retention, privacy, Security and provider-governance controls. | Could support wider journey analysis and later Product learning. | Greater personal-data, integration, support, cost and maintenance exposure. Evidence needed: all relevant B evidence plus attributable need for breadth, third-party and cross-border review, stronger governance capacity, deletion and access controls, and reversibility. |

The three alternatives remain **unselected, unranked and non-authoritative**. None is asserted compliant or feasible. No provider, event name, property, schema, score, default tracking behavior or customer permission is selected.

## Authority and Data-Ownership Boundaries

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns analytics capability adoption, intended measurement outcomes and Product policy. It does not approve arbitrary collection through this record. |
| Architecture | Retains data and integration architecture, technical provider selection and material cross-domain technical authority. |
| Customer | Owns Customer, Consent and Preference truth under [REQ-CUS-018–021 and 038–041](../domains/customer/customer-domain.md); it does not automatically own every analytics datum. |
| Identity | Retains Authentication, Principal and identity-security authority. Account association or a Session does not grant behavioural-tracking permission. |
| Reporting | Owns governed reports, Projections, definitions, lineage, freshness and analytical representations under [REQ-RPT-002–009, 016–023 and 050–051](../domains/reporting/reporting-domain.md); it does not automatically own behaviour collection or source facts. |
| Search | May expose governed observations under [REQ-SRCH-047](../domains/search/search-domain.md), but an Analytics Event cannot establish Search, Product, Customer or commercial truth. |
| Cart and Checkout | Retain Cart intent and Checkout validation and progression; analytics cannot substitute for either source state. |
| Order and Payment | Retain authoritative Order history and Payment financial transaction truth; an analytics conversion or payment event proves neither. |
| CMS | Retains governed content and presentation authority without granting tracking permission. |
| Notifications | Retains governed communication delivery authority without granting analytics collection permission. |
| Security and Privacy/Legal | Retain mandatory access, data-protection, lawful-purpose, Consent, disclosure, retention, deletion, third-party and cross-border review. |
| Operations | Reviews supportability, observability, failure handling and operational burden without establishing collection policy. |

No Analytics Domain or event owner is invented. Source-Domain ownership remains with each producer; a future taxonomy must attribute event purpose, trigger, source provenance and owner before any approved use. Reporting and operational telemetry remain distinct from optional customer-behaviour collection.

## Open Policy and Technical Questions

The following remain unresolved: initial analytics scope, measurement priorities and Version 1 optional tracking; event taxonomy, category purposes and triggers; event ownership, source provenance and lineage; lawful basis or Consent by category; necessary versus optional collection; notice and customer transparency; Customer or Account association; permitted identifiers, properties and Sensitive Data exclusions; minimization, access and Authorization; retention, correction, suppression and deletion; third-party processing and cross-border effects; provider-neutral requirements, provider selection and due diligence; SDK or instrumentation; Reporting and downstream use; operational monitoring and support; relation to Proposed [DEC-0025](DEC-0025-marketing-consent-communication-preferences-policy.md); Contract and versioning semantics; and evidence for later evaluation and acceptance. These are open questions, not newly approved requirements or selected answers.

## Security and Privacy Boundary

[SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md) requires an approved purpose, owner, access policy, retention rule, deletion process and disclosure boundary for personal and Sensitive Data. Collection must be proportionate and purpose-limited, with Consent where required or another qualified lawful basis where applicable. Marketing Consent does not automatically authorize behavioural tracking; neither Account creation nor purchase supplies such permission. Technical ability to observe a customer does not justify collecting their identifiers or properties. Future provider processing requires Security and Privacy/Legal review, including notice, third-party terms and cross-border handling where applicable. Analytics observations cannot establish authoritative business state or bypass source-Domain Authorization.

## Future Evaluation Criteria and Consequences

A separately authorized evaluation should consider coverage of Approved measurement outcomes; purpose clarity; privacy and lawful processing; applicable Consent handling; minimization; source provenance, ownership and lineage; event quality and semantic consistency; access control; retention and deletion; Reporting usefulness; operational supportability; provider neutrality and lock-in; cost and maintenance; Security and integration risk; and reversibility. Criteria are not evidence, scores or a comparison.

Recording the unresolved decision makes scope, privacy and provider dependencies reviewable without selecting an implementation. Product measurement obligations remain, as do source-Domain authority and mandatory Security and Privacy controls. No Analytics Event becomes transactional proof. No provider, SDK, collection or Reporting expansion is approved by this record.

## Reviewers and Future Governance Gates

Product leads. Customer, Identity, Reporting, CMS, Search, Cart, Checkout, Order, Payment, Notifications, Security, Privacy/Legal, Architecture and Operations review their respective boundaries. Data and Integration review is required where their governed responsibilities are affected. Listing a reviewer does not assert review or approval.

1. Gather Product and affected-owner evidence and confirm required measurement outcomes.
2. Establish purpose and privacy boundaries, including necessary versus optional collection.
3. Assess event ownership, source lineage and affected-Domain authority.
4. Obtain qualified Security and Privacy/Legal review.
5. Separately authorize comparative evaluation if evidence justifies it.
6. Select a Product policy only through separate Product authority and affected-owner review.
7. Complete formal Product Decision acceptance and synchronize `PRODUCT.md` and other canonical Product guidance.
8. Govern provider and Architecture choices separately.
9. Approve taxonomy, Contracts, schemas and instrumentation through applicable separate governance.
10. Assess implementation eligibility and verify testing, privacy, operational and rollout readiness.

No gate is automatically satisfied by this Proposed record.

## Validation Criteria

Review of this record passes only if: (1) DEC-0033 exists at its canonical path as 0.1.0 Proposed, Product-owned and non-authoritative; (2) Product §24 item 20 remains unresolved; (3) exactly three alternatives remain unselected and unranked; (4) no provider or event taxonomy is selected and no customer tracking is authorized; (5) Approved Product measurement obligations remain without telemetry repurposing; (6) Customer, Identity, Reporting, Search and each commercial source Domain retain authority; (7) Security and Privacy constraints remain binding; and (8) Architecture, comparative evaluation, acceptance and implementation require separate governance.

## Decision Status and Explicit Non-Decisions

DEC-0033 remains **0.1.0 Proposed, Product-owned and non-authoritative**. It supersedes nothing and is superseded by nothing. It selects no analytics provider, SDK, platform, event taxonomy, event name, property, schema or owner; Customer identifier, Account-association rule, Consent or lawful-processing basis, retention period, tracking default, destination, third-party processor, cross-border transfer arrangement, instrumentation, Integration Contract, data architecture, new Domain boundary, implementation or rollout. It admits no provider or dependency and accepts no Architecture decision.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§18, 24 item 20 and 33](../../.ai/core/PRODUCT.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md) — Product measurement, provider, event and privacy boundaries.
- [Customer Domain REQ-CUS-018–021 and 038–041](../domains/customer/customer-domain.md), [Identity Domain](../domains/identity/identity-domain.md), [Reporting Domain REQ-RPT-002–009, 016–023 and 050–051](../domains/reporting/reporting-domain.md) and [Search Domain REQ-SRCH-047](../domains/search/search-domain.md) — Consent, identity, analytical representation and observation separation.
- [Cart Domain](../domains/cart/cart-domain.md), [Checkout Domain](../domains/checkout/checkout-domain.md), [Order Domain](../domains/order/order-domain.md), [Payment Domain](../domains/payment/payment-domain.md), [CMS Domain](../domains/cms/cms-domain.md) and [Notifications Domain](../domains/notifications/notifications-domain.md) — source-Domain and downstream boundaries.
- [DEC-0025](DEC-0025-marketing-consent-communication-preferences-policy.md) — related Proposed, not Accepted, marketing Consent and Preference policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed PRODUCT.md §24 item 20 with three unselected analytics alternatives, unresolved event taxonomy and provider selection, source-Domain authority, Security/Privacy/Legal constraints and separate evaluation, acceptance and implementation gates. |
