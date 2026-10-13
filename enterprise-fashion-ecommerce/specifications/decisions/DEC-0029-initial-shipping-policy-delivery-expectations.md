# DEC-0029 — Initial Shipping Policy and Delivery Expectations

- **Identifier:** DEC-0029
- **Title:** Initial Shipping Policy and Delivery Expectations
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

[`PRODUCT.md` §24 item 7](../../.ai/core/PRODUCT.md) leaves the shipping provider, service levels, delivery areas and fee policy open. Item 8 separately leaves free-delivery thresholds and promotional treatment open. The Product journey requires a governed delivery choice and communication of material delivery changes before payment confirmation; it does not select a service, area, charge or delivery promise. [`ARCHITECTURE.md` §34 item 6](../../.ai/core/ARCHITECTURE.md) separately leaves shipping-provider selection to Architecture governance.

The [Approved Shipping and Fulfilment Domain Specification](../domains/shipping/shipping-domain.md) REQ-SHP-004–007, 019–029 and 031–032 and [Approved Shipping Backend Specification](../backend/shipping/shipping-backend.md) BSHP-REQ-016–019 and 027–039 establish Shipping authority, delivery-choice and quotation safeguards, provider evidence, distinct delivery outcomes, failure handling, duplicate safety and reconciliation. [Checkout REQ-CHK-019–021](../domains/checkout/checkout-domain.md) and [Order REQ-ORD-032–038](../domains/order/order-domain.md) preserve their respective coordination and lifecycle boundaries. These Approved Specifications do not select a carrier, geography, service level, fee or delivery policy. [`DECISIONS.md` §§29 and 38](../../.ai/core/DECISIONS.md) governs Product and provider decisions; [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) retains applicable Authorization, provider-evidence and privacy controls.

Proposed [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) leaves cancellation eligibility and cutoff unaccepted. Proposed [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) leaves Return, Exchange and Refund policy unaccepted. Neither supplies Shipping policy or implementation authority.

## Decision Questions and Status

What breadth of delivery services and geographic coverage should the initial Product offer, and what customer-facing expectations and disclosures should apply? What evidence should govern delivery availability, service-level and window communication, Shipping charges and fee transparency, delays, failed delivery, exceptions, recovery, customer communications and operational support? How should customer demand, carrier capability and reliability, operational capacity, cost, commercial viability, Checkout compatibility and future expansion constrain later policy selection?

**NO OPTION SELECTED.** No service, delivery area, timeframe, fee, guarantee, carrier or provider is chosen. `PRODUCT.md` §24 items 7 and 8 and `ARCHITECTURE.md` §34 item 6 remain unresolved.

## Alternatives Considered — Not Formally Compared

| Option | Initial Product policy candidate | Potential Product benefit | Limitation, risk and evidence needed |
| --- | --- | --- | --- |
| A — Narrow initial service and coverage | Consider a bounded initial delivery-service and geographic scope justified by launch evidence. | Could simplify truthful customer disclosures, fulfilment support and initial operational control. | Could exclude material demand or reduce accessibility; attributable demand, geographic, carrier-capability, cost and support evidence is needed. No service or area is selected. |
| B — Broader initial offering | Consider broader service and coverage only where customer, carrier, commercial and operational evidence supports it. | Could serve more customer needs and delivery situations at launch. | More variability may increase unavailable choices, cost, exception, support and disclosure complexity; evidence of reliable capability and recovery is needed. No service or area is selected. |
| C — Staged service and geographic expansion | Consider a bounded initial offering with later additions under separate Product, Architecture, Contract and readiness approvals. | Could preserve reversibility while allowing evidence-led expansion. | An initial limit may not meet demand, and each expansion requires provider, commercial, customer-communication and operational validation. No starting scope, expansion schedule or addition is selected. |

The three alternatives are **unselected, unranked and non-authoritative**. They describe policy breadth only. None names or ranks a provider, establishes geographic coverage, sets a service level or delivery guarantee, or approves a fee.

## Evaluation Criteria and Evidence Required

A separately authorized comparison must examine attributable customer demand; geographic accessibility; current delivery-service availability and carrier capability; operational capacity and delivery reliability; delays, exceptions and failed-delivery recovery; truthful customer communication and disclosures; Checkout compatibility and Order/Shipment traceability; Shipping cost, commercial viability and fee transparency; Security and Privacy; provider portability and replacement; reversibility, future expansion and launch readiness. These are criteria, not scores or findings. No demand measurement, carrier assessment, commercial quote, legal conclusion or stakeholder approval is asserted.

Evidence must distinguish a customer-facing expectation from a guaranteed outcome, a quoted or requested service from a confirmed Shipment or delivery, and a provider report from Shipping-accepted authoritative evidence. A later policy must address unsupported destinations, stale choices, changed quotations, provider unavailability, delay, failed delivery and unresolved outcomes without promising success where authoritative evidence is absent.

## Domain and Governance Boundaries

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns customer-facing shipping-policy scope, delivery expectations and fee-disclosure policy. This record does not select their values. |
| Shipping and Fulfilment | Owns governed delivery choices, eligibility, quotation evidence, Shipment lifecycle, Dispatch, tracking, delivery, revalidation, exceptions and Shipping truth under the Approved Shipping Specifications. |
| Checkout | Consumes and revalidates governed Shipping evidence before relevant purchase effects; it cannot independently establish delivery eligibility, Shipping Rate or confirmed delivery. |
| Order | Owns Order lifecycle and commercial history, while Shipment and delivery truth remain Shipping-owned. |
| Finance and Pricing | Finance reviews charge, cost and financial implications. Pricing retains final commercial calculation; Shipping quotation evidence does not select a fee formula, total, promotion or threshold. |
| Operations | Reviews carrier capacity, fulfilment readiness, support, exceptions, recovery and reconciliation without an operational commitment being selected here. |
| Security and Privacy | Preserve contextual Authorization, protected customer and tracking data, provider-evidence trust, minimization and applicable privacy review. |
| Architecture | Owns the separate shipping-provider selection ADR and integration architecture under `ARCHITECTURE.md` §34 item 6. Product shipping policy informs but does not replace that decision. |
| Engineering | Reviews feasibility and compatibility without selecting a Contract, mechanism or integration through this record. |
| Legal | Reviews customer disclosures, delivery representations, contractual obligations and applicable consumer-protection requirements without asserting a legal conclusion here. |

## Shipping Safety and Related Open Decisions

Shipping-owned state and governed current delivery choices remain authoritative. Materially stale selections require revalidation; applicable quotation evidence retains identity, applicability, freshness, Currency and provider provenance. Requested delivery, provider-reported delivery and Shipping-accepted confirmed delivery remain distinct. A client report, tracking display, notification or unvalidated carrier callback cannot itself establish delivery. Provider evidence must meet applicable authenticity, integrity and correlation requirements before acceptance. Unknown, delayed, partial and conflicting outcomes remain explicit and subject to authorized reconciliation and recovery; duplicate or replayed activity cannot multiply Shipping effects.

Order and Shipment truth remain separate. Provider-neutral project-owned Shipping semantics must survive a separately governed provider replacement without rewriting accepted history. This record defines no callback protocol or carrier Contract. It establishes no delivery guarantee, automatic recovery, cancellation or Return entitlement. `PRODUCT.md` §24 item 8 remains open for free-delivery thresholds and promotions; Architecture §34 item 6 remains open for provider selection; DEC-0014 and DEC-0016 remain Proposed, not Accepted.

## Reviewers, Dependencies and Future Gates

Product leads this policy question. Shipping, Checkout, Order, Finance, Operations, Architecture, Engineering and Security must review the aspects they own. Legal/Privacy review applies to customer disclosures, carrier contracts or protected customer data. Consultation is not evidence that any review has occurred.

1. **Attributable evidence:** Obtain customer-demand, carrier-capability, commercial-cost, operational-capacity and customer-disclosure evidence. None is claimed complete here.
2. **Comparative Product evaluation:** Obtain separate authorization before comparing the three policy alternatives. This record conducts and authorizes no comparison.
3. **Product selection and acceptance:** Product and affected owners decide the intended policy under [`PRODUCT.md`](../../.ai/core/PRODUCT.md) and [`DECISIONS.md`](../../.ai/core/DECISIONS.md), with canonical synchronization where required. No alternative is Accepted here.
4. **Architecture provider selection:** Resolve `ARCHITECTURE.md` §34 item 6 through separately governed ADR authority. Product policy does not choose the carrier or integration architecture.
5. **Provider and control approval:** Obtain applicable carrier Contract, Finance, Security, Operations, Legal/Privacy and Engineering approvals before relying on a provider or delivery commitment. No gate is presumed passed.
6. **Implementation and launch readiness:** Authorize implementation separately and validate Shipping, Checkout, Order, customer disclosure, failure, recovery, security and compatibility safeguards. This Proposed record grants no delivery authority.

## Validation Criteria

Review of this record passes only if: (1) metadata remains 0.1.0 Proposed, Product-owned and non-authoritative, with `PRODUCT.md` §24 item 7 traceability; (2) exactly three policy-level alternatives remain unselected and unranked; (3) Architecture provider authority remains separate and Product item 8 stays open; (4) DEC-0014 and DEC-0016 remain Proposed; (5) Shipping ownership, quotation and revalidation safeguards, provider-evidence validation, distinct delivery outcomes, uncertainty and reconciliation remain intact; (6) Checkout and Order authority is preserved; (7) no delivery commitment, area, fee, threshold, provider, operational measurement or stakeholder approval is invented; (8) no Contract, API, Event, schema or mechanism is selected; and (9) evidence, evaluation, acceptance, Architecture selection and implementation remain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0029 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It selects no Shipping Provider, Carrier, delivery service, area, geography, guarantee, service-level commitment, delivery window, Shipping fee, free-delivery threshold, promotional treatment, Carrier Contract or ranking, quotation formula, API, Event, schema, callback mechanism, SDK, integration architecture or implementation. It does not compare alternatives, authorize Product evaluation, accept policy, resolve Architecture provider selection, change an Approved Specification, or make DEC-0014 or DEC-0016 authoritative.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§14.4–14.7 and 24 items 7–8](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §34 item 6](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§29 and 38](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product, decision, provider and security boundaries.
- [Shipping Domain REQ-SHP-004–007, 019–029 and 031–032](../domains/shipping/shipping-domain.md) and [Shipping Backend BSHP-REQ-016–019 and 027–039](../backend/shipping/shipping-backend.md) — Shipping ownership, evidence, outcomes and recovery.
- [Checkout Domain REQ-CHK-019–021](../domains/checkout/checkout-domain.md) and [Order Domain REQ-ORD-032–038](../domains/order/order-domain.md) — distinct coordination and lifecycle authority.
- [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) — related Proposed, not Accepted, policies.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed three unselected shipping-policy and delivery-expectations alternatives, preserving Shipping ownership, separate Architecture provider selection, unresolved commercial and commerce policies, and distinct evidence, evaluation, acceptance, provider and implementation gates. |
