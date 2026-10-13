# DEC-0024 — Back-order and Pre-order Support Policy

- **Identifier:** DEC-0024
- **Title:** Back-order and Pre-order Support Policy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-10-13
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Current Approved Baseline

[`PRODUCT.md` §24 item 13](../../.ai/core/PRODUCT.md) leaves back-order and pre-order support open. The Approved [Inventory Domain Specification](../domains/inventory/inventory-domain.md) REQ-INV-007–011 and [Inventory Backend Specification](../backend/inventory/inventory-backend.md) BINV-REQ-016–020 keep Stock, Available-to-Sell and Stock Reservation authoritative in Inventory. Unknown, stale, ambiguous or insufficient Stock evidence cannot establish confirmed availability or authorize an unsupported commercial commitment; unsupported negative-Stock outcomes are rejected. The Approved [Checkout Domain Specification](../domains/checkout/checkout-domain.md) REQ-CHK-016–018 and [Checkout Backend Specification](../backend/checkout/checkout-backend.md) BCHK-REQ-016–018 require current Inventory revalidation and safe, distinguishable reservation outcomes before governed progression. These requirements do not permit future-dated purchases merely because this Product question is open.

For this decision's *evaluation only*, a back-order is a possible commercial commitment concerning an item not currently available for immediate fulfilment; a pre-order is a possible commitment concerning an item intended for a future release or availability event. These are candidate commercial distinctions, not approved repository definitions, Inventory Stock states or enabled capabilities. Each could carry different promises and risks. The alternatives below are bounded initial-policy candidates; they do not assert that possible later back-order and pre-order capabilities must always be mutually exclusive.

## Decision Question and Status

Should the initial Product support neither back-orders nor pre-orders, consider back-order support, or consider pre-order support, subject to separately governed evidence, policy acceptance and domain changes?

**NO OPTION SELECTED.** No comparison is authorized by creating this record. `PRODUCT.md` §24 item 13 remains open; no purchase, reservation, delivery or release promise follows from this question.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation and evidence needed |
| --- | --- | --- | --- |
| A — No initial support | Maintain the existing current-availability and Checkout eligibility boundaries without adding back-order or pre-order support. | Avoids a new future-fulfilment promise and preserves existing denial of unsupported progression. | May forgo demand that could be served later; Product must establish whether that opportunity is material. This is not a selected policy. |
| B — Potential back-order support | Consider a later, separately approved policy for demand concerning an item unavailable for immediate fulfilment. | Could serve demand where credible replenishment and operations support a customer promise. | Replenishment confidence, Stock integrity, payment treatment, delivery expectations, cancellation and recovery need evidence. This grants no unavailable-Stock purchase right. |
| C — Potential pre-order support | Consider a later, separately approved policy for an item intended for a future release or availability event. | Could allow Product to assess customer interest ahead of a release. | Release uncertainty, publication, pricing, payment, fulfilment and customer-rights consequences need evidence. This grants no purchase before release or availability. |

All three remain unselected and unranked. Back-order and pre-order must be assessed as distinct potential commercial promises, not interchangeable Inventory states. Later Product review may refine, combine or reject candidates through separate governance; this record does not decide a future capability taxonomy.

## Domain Authority and Existing Safeguards

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns Product and Product Variant structure, publication and structural sellability under [Product Domain](../domains/product/product-domain.md) and [Product Backend](../backend/product/product-backend.md). Product policy owns the unresolved commercial choice, but publication or sellability cannot prove Inventory availability. |
| Inventory | Alone owns Stock, Available-to-Sell, Stock Reservation, availability uncertainty and authoritative reservation outcomes under REQ-INV-007–011 and BINV-REQ-016–020. Unsupported negative Stock is rejected; no speculative availability or reservation is inferred. |
| Pricing | Owns current Price and governed calculations under [Pricing Domain](../domains/pricing/pricing-domain.md) and [Pricing Backend](../backend/pricing/pricing-backend.md); it neither creates Inventory truth nor selects future commercial terms here. |
| Cart | Holds provisional customer intent under [Cart Domain](../domains/cart/cart-domain.md) and [Cart Backend](../backend/cart/cart-backend.md). Retaining or displaying an item proves neither eligibility nor Stock Reservation. |
| Checkout | Coordinates current validation and progression under REQ-CHK-016–018 and BCHK-REQ-016–018. Insufficient, stale, conflicting, unavailable or uncertain Inventory evidence interrupts unsupported progression; Checkout cannot fabricate Stock or reservation truth. |
| Order | Alone owns governed Order creation and lifecycle under [Order Domain](../domains/order/order-domain.md) and [Order Backend](../backend/order/order-backend.md). It may consume only a separately governed eligible Checkout outcome. |
| Payment | Owns authorization, capture, refund and related financial lifecycle truth under [Payment Domain](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md). No Payment timing or treatment is selected here. |
| Shipping | Owns fulfilment and Shipment lifecycle truth under [Shipping Domain](../domains/shipping/shipping-domain.md) and [Shipping Backend](../backend/shipping/shipping-backend.md). No delivery promise or release schedule is selected here. |

Wishlist, CMS, Reporting and Notifications gain no source-Domain or purchase authority through a possible customer message about either candidate. No displayed availability, release, replenishment or delivery statement can override current Inventory truth or Checkout safeguards.

## Dependencies and Evaluation Evidence

[DEC-0015](DEC-0015-stock-reservation-duration-policy.md) leaves Stock Reservation duration Proposed; [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md) leaves stacking Proposed; [DEC-0021](DEC-0021-stock-availability-messaging-policy.md) leaves availability messaging Proposed. None accepts back-order or pre-order support. Their unresolved choices affect later comparison or implementation where applicable, but do not prevent framing this decision. Applicable cancellation, return/refund, guest-checkout and other open Product policies must remain separate rather than be inferred from this record.

Before formal comparison, Product needs attributable customer scenarios, the proposed commercial promise and business justification for each candidate. Inventory and Operations must examine credible replenishment or release evidence, availability uncertainty, integrity, scarcity, reservation implications and recovery. Pricing, Checkout, Cart, Order, Payment and Shipping must examine their own current-state, coordination and failure cases, including price treatment; Payment authorization, capture and refund implications; fulfilment and delivery expectations; cancellation, return and customer-rights implications; and failure, expiry and uncertain-outcome handling. Security must assess abuse and fraud risks. Finance and Legal must assess material commercial commitments and customer rights. Customer communication needs review without inventing copy, channels or guarantees. This record supplies no measurements, stakeholder confirmations or findings.

## Required Review and Future Gates

Product owns evidence framing, comparison and any eventual policy selection. Inventory, Checkout, Pricing, Cart, Order, Payment and Shipping are affected-domain reviewers, not substitute Product policy authorities. Operations, Security, Finance and Legal review the operational, abuse, financial and customer-rights consequences within their responsibilities.

1. **Evidence gathering:** Record attributable Product goals, candidate journeys, source-Domain constraints, uncertainty and affected-owner observations. This record claims none complete.
2. **Formal comparative evaluation:** Obtain separate authorization and review the evidence against each unselected alternative, including commercial and customer consequences. Drafting this record does not authorize evaluation.
3. **Product policy selection:** Product may select, reject or revise an alternative only through its governed decision process; no selection occurs here.
4. **Governance acceptance:** Applicable Product and affected-owner reviews under [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), plus canonical Product synchronization, must occur before policy is binding.
5. **Specifications and implementation:** Any Accepted policy still needs separately authorized Domain/Backend Specification, Contract, Architecture where material, Security and Engineering changes before implementation. Existing Approved safeguards remain in force until duly changed.

## Validation Criteria

Review of this Proposed record passes only if: (1) metadata remains 0.1.0 Proposed, Product-owned and non-authoritative; (2) item 13 and all three alternatives remain open and unselected; (3) back-order and pre-order are distinct candidate promises rather than Stock states; (4) REQ-INV-007–011, BINV-REQ-016–020, REQ-CHK-016–018 and BCHK-REQ-016–018 retain their current safety meaning; (5) owner authority and Proposed dependencies remain separate; (6) evidence, comparison, selection, acceptance and implementation gates remain distinct; and (7) no unapproved purchase, Stock, Payment, delivery, Contract or technical rule is introduced.

## Decision Status and Explicit Non-Authorizations

DEC-0024 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It selects no back-order or pre-order eligibility, purchase progression, negative Stock, Stock Reservation behavior, replenishment commitment, release or delivery date, Payment timing, Refund treatment, customer message, commercial term or retention period. It does not create an API or Event Contract, schema, provider, technical mechanism, workflow, new requirement overriding an Approved Domain or Backend Specification, or implementation authority. It does not modify any Domain or Backend Specification, close `PRODUCT.md` §24 item 13, authorize comparative evaluation, accept policy or grant Wishlist, CMS, Reporting or Notifications authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16, 24 item 13, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md §10](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — governing policy, decision lifecycle, ownership, terminology and security.
- [Product Domain](../domains/product/product-domain.md) and [Backend](../backend/product/product-backend.md); [Inventory Domain REQ-INV-007–011](../domains/inventory/inventory-domain.md) and [Backend BINV-REQ-016–020](../backend/inventory/inventory-backend.md); [Pricing Domain](../domains/pricing/pricing-domain.md) and [Backend](../backend/pricing/pricing-backend.md).
- [Cart Domain](../domains/cart/cart-domain.md) and [Backend](../backend/cart/cart-backend.md); [Checkout Domain REQ-CHK-016–018](../domains/checkout/checkout-domain.md) and [Backend BCHK-REQ-016–018](../backend/checkout/checkout-backend.md); [Order Domain](../domains/order/order-domain.md) and [Backend](../backend/order/order-backend.md).
- [Payment Domain](../domains/payment/payment-domain.md) and [Backend](../backend/payment/payment-backend.md); [Shipping Domain](../domains/shipping/shipping-domain.md) and [Backend](../backend/shipping/shipping-backend.md).
- [DEC-0015](DEC-0015-stock-reservation-duration-policy.md), [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md), and [DEC-0021](DEC-0021-stock-availability-messaging-policy.md) — related Proposed decisions, not accepted policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected initial back-order and pre-order policy alternatives, preserved distinct possible commercial promises, Inventory and Checkout safeguards, source-Domain authority, unresolved Product item 13 and related Proposed decisions, and separate evidence, evaluation, acceptance and implementation gates. |
