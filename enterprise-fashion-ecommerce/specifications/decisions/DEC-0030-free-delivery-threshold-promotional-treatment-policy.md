# DEC-0030 — Free-Delivery Threshold and Promotional Treatment Policy

- **Identifier:** DEC-0030
- **Title:** Free-Delivery Threshold and Promotional Treatment Policy
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

[`PRODUCT.md` §24 item 8](../../.ai/core/PRODUCT.md) leaves the free-delivery threshold and promotional treatment unresolved. Its §§16.1, 16.5–16.6 require truthful commercial and delivery information, backend recalculation, current supported delivery choices and governed Promotion rules; they select no free-delivery entitlement or commercial value. [`DECISIONS.md`](../../.ai/core/DECISIONS.md) governs the Product decision lifecycle and provider boundaries.

The [Approved Pricing Domain Specification](../domains/pricing/pricing-domain.md) REQ-PRC-002, 009–012 and 014 and [Pricing Backend Specification](../backend/pricing/pricing-backend.md) BPRC-REQ-020–024 reserve authoritative commercial calculation and policy-permitted Promotion application to Pricing. They expressly defer free-delivery thresholds, promotional delivery treatment, combination rules and formulas. [Shipping REQ-SHP-004–007](../domains/shipping/shipping-domain.md) and its [Backend Specification](../backend/shipping/shipping-backend.md) retain delivery choices, revalidation and quotation evidence without determining the final charge. [Cart REQ-CART-016–018](../domains/cart/cart-domain.md) and its [Backend Specification](../backend/cart/cart-backend.md) keep displayed values provisional. [Checkout REQ-CHK-013–014 and 019–021](../domains/checkout/checkout-domain.md) and its [Backend Specification](../backend/checkout/checkout-backend.md) require current Pricing and Shipping revalidation before commitment. [Order REQ-ORD-023–025](../domains/order/order-domain.md) and its [Backend Specification](../backend/order/order-backend.md) preserve historical commercial evidence without defining policy. The [Payment Domain](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md) retain financial transaction truth, distinct from commercial eligibility and calculation. These Approved requirements establish authority and safeguards, not a free-delivery policy.

Proposed [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md) leaves Voucher and Promotion stacking unresolved. Proposed [DEC-0029](DEC-0029-initial-shipping-policy-delivery-expectations.md) leaves the initial Shipping offering unresolved. Neither is Accepted policy. Cancellation and Return/Refund treatment remain separately Proposed in [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md). [`ARCHITECTURE.md` §34 item 6](../../.ai/core/ARCHITECTURE.md) separately leaves shipping-provider selection open.

## Decision Question and Status

Should the initial Product offer no free-delivery incentive, consider conditional free delivery under a later governed qualifying policy, or consider promotional or staged free-delivery treatment subject to separate approvals? What policy evidence should govern eligibility, exclusions, customer-facing charge transparency, Voucher and Promotion interaction, operational feasibility, fraud risk and post-purchase consequences?

**NO OPTION SELECTED.** No comparative evaluation is authorized. `PRODUCT.md` §24 item 8 remains open.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential Product benefit | Limitation, risk and evidence needed |
| --- | --- | --- | --- |
| A — No initial free-delivery incentive | Consider an initial policy without a free-delivery incentive. | Avoids introducing a shipping-related commercial incentive before supporting evidence exists. | May reduce commercial attractiveness; customer demand, competitive positioning, Shipping economics and Product objectives need evidence. This does not set a Shipping charge. |
| B — Conditional free delivery | Consider free delivery only under a separately approved qualifying policy. | Could support a governed incentive if commercially sustainable. | Qualification, exclusions, margin exposure, abuse and operational effects remain undecided; Pricing economics, carrier cost, customer-behavior, capacity and fraud evidence are needed. |
| C — Promotional or staged treatment | Consider limited or phased incentives subject to separate Product, Promotion, Shipping and launch approvals. | Could permit controlled later introduction responsive to evidence. | Promotion interaction, eligibility, communication, abuse prevention, reconciliation and rollout need decisions; campaign goals, operational feasibility, financial exposure and disclosure evidence are needed. |

All three alternatives are **unselected, unranked and non-authoritative**. None creates an entitlement, threshold, percentage, price, qualifying amount, area, campaign, promotional period or preferred policy.

## Evaluation Criteria and Evidence Required

A separately authorized comparison must consider attributable customer demand and commercial objectives; Shipping cost, carrier capability and gross-margin exposure; Pricing consistency and eligibility clarity; customer transparency and legal disclosures; Promotion interaction and Voucher stacking; Cart provisional-value behavior and Checkout revalidation; Order historical commercial evidence; Payment, Refund, cancellation and Return implications; operational feasibility; fraud, manipulation and abuse exposure; Security and Privacy; provider neutrality; reversibility and future expansion. These are criteria only. No measurements, carrier assessments, financial conclusions, legal advice or stakeholder approvals are asserted.

Evidence must distinguish a governed Shipping Rate or quotation from a final Pricing outcome, an offered or displayed benefit from an accepted eligible benefit, and current commercial truth from the confirmed Order record. Stale, conflicting, missing or uncertain inputs cannot silently produce a trusted affected total or an inferred free-delivery entitlement.

## Domain Authority and Approved Safeguards

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns the unresolved customer-facing commercial policy; this record selects no policy value. |
| Pricing | Owns authoritative server-side commercial calculation, Price and Discount truth, and policy-permitted Promotion and Voucher application; it cannot invent a free-delivery rule or fee formula. |
| Shipping | Owns governed delivery choices, eligibility, Shipping Rate and quotation evidence, Shipment lifecycle and delivery truth; it does not independently establish final free-delivery charge or Promotion treatment. |
| Cart | Owns provisional customer intent and may present governed, non-authoritative values; its display creates no entitlement or final total. |
| Checkout | Consumes and revalidates current Pricing and Shipping outcomes before commercial commitment, communicates material changes and blocks unsupported progression. |
| Order | Owns lifecycle and confirmed historical commercial evidence without recalculating Pricing or defining free-delivery policy. |
| Payment | Owns Payment Authorization, Capture, Refund execution and other financial transaction truth; it does not determine promotional eligibility or Refund policy. |
| Finance | Reviews Shipping costs, margin, commercial exposure and reconciliation without a financial conclusion being presumed. |
| Operations | Reviews carrier capability, fulfilment feasibility and exceptions without an operational commitment here. |
| Security | Reviews client-value manipulation, fraud and abuse controls; client-supplied Shipping amounts or benefits cannot become authority. |
| Architecture and Engineering | Retain separately governed provider selection, integration architecture, feasibility and implementation authority. |
| Legal/Privacy | Review customer disclosures, rights and applicable personal-data obligations without a legal conclusion here. |

Authoritative Pricing calculation, Shipping-owned quotation provenance, precision-safe Money, provisional Cart presentation, current Checkout revalidation, immutable Order commercial history and Payment financial truth remain binding. No client-created free-delivery benefit or client-supplied Shipping amount is authoritative. [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) fraud, business-flow abuse, provider and sensitive-data controls remain applicable. Business semantics must remain provider-neutral; an unselected shipping provider cannot supply Product policy by implication.

## Dependencies and Future Gates

DEC-0017 and DEC-0029 remain Proposed, so a later policy must resolve applicable Promotion compatibility and Shipping offering dependencies rather than treat either record as binding. `PRODUCT.md` §24 item 8 remains open. DEC-0014 and DEC-0016 leave cancellation, Return and Refund treatment separately unresolved; this record chooses no fee restoration or post-purchase adjustment. Architecture's shipping-provider decision remains separate.

Product leads this decision. Pricing, Shipping, Cart, Checkout, Order, Payment, Finance, Operations, Security, Architecture, Engineering and Legal/Privacy must review matters within their respective authority. Consultation is not evidence that review occurred.

1. **Attributable evidence:** Obtain Product goals, customer demand, carrier costs, Pricing economics, margin exposure, operational capacity, Promotion interactions, fraud risks and customer-disclosure evidence. None is claimed complete.
2. **Comparative Product evaluation:** Obtain separate authorization before comparing the three alternatives. This record does not conduct or authorize comparison.
3. **Product selection:** Select an intended policy only through separately governed Product authority. No alternative is selected here.
4. **Acceptance and synchronization:** Obtain affected-owner review, formal governance acceptance and canonical Product synchronization where required. Proposed text alone is not Accepted policy.
5. **Downstream governance:** Authorize any necessary Domain or Backend Specification, Contract, Architecture, Security and Engineering changes separately without transferring authority.
6. **Implementation and launch readiness:** Authorize delivery separately and verify the applicable commercial, Shipping, Checkout, Order, Payment, abuse, disclosure and operational safeguards. No gate is deemed complete.

## Validation Criteria

Review of this record passes only if: (1) it remains 0.1.0 Proposed, Product-owned and non-authoritative with `PRODUCT.md` §24 item 8 open; (2) exactly three alternatives remain unselected and unranked; (3) Pricing calculation and Shipping quotation authority remain separate; (4) Cart presentation remains provisional, Checkout revalidation remains required, Order retains historical commercial evidence and Payment retains financial truth; (5) DEC-0017 and DEC-0029 remain Proposed; (6) no threshold, Promotion, entitlement, charge, formula or post-purchase treatment is selected; (7) no evidence or stakeholder approval is invented; and (8) evaluation, selection, acceptance, downstream governance and implementation remain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0030 remains **0.1.0 Proposed, Product-owned and non-authoritative**. It supersedes nothing and is superseded by nothing. It does not close `PRODUCT.md` §24 item 8, establish a commercial policy or calculation rule, or authorize implementation. It selects no free-delivery availability, qualifying threshold or basis, Customer eligibility, Product or delivery-service exclusion, geographic restriction, Voucher stacking, promotional eligibility, campaign, Discount amount, fee adjustment, Shipping charge, post-purchase adjustment, Refund treatment, Carrier, provider, API, Event, schema or technical mechanism. It neither compares alternatives nor makes existing Proposed decisions authoritative.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.1, 16.5–16.6 and 24 item 8](../../.ai/core/PRODUCT.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md §34 item 6](../../.ai/core/ARCHITECTURE.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product, decision, provider and security boundaries.
- [Pricing Domain REQ-PRC-002, 009–012 and 014](../domains/pricing/pricing-domain.md) and [Backend BPRC-REQ-020–024](../backend/pricing/pricing-backend.md) — current commercial calculation and deferred policy.
- [Shipping Domain REQ-SHP-004–007](../domains/shipping/shipping-domain.md) and [Shipping Backend](../backend/shipping/shipping-backend.md) — choice, quotation and charge separation.
- [Cart Domain REQ-CART-016–018](../domains/cart/cart-domain.md) and [Cart Backend](../backend/cart/cart-backend.md); [Checkout Domain REQ-CHK-013–014, 019–021](../domains/checkout/checkout-domain.md) and [Checkout Backend](../backend/checkout/checkout-backend.md); [Order Domain REQ-ORD-023–025](../domains/order/order-domain.md) and [Order Backend](../backend/order/order-backend.md) — provisional, revalidated and historical commercial truth.
- [Payment Domain](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md) — separate financial truth.
- [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md), [DEC-0029](DEC-0029-initial-shipping-policy-delivery-expectations.md), [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) and [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) — related Proposed, not Accepted, policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Framed three unselected free-delivery and promotional-treatment policy alternatives, preserved Pricing and Shipping authority, Cart/Checkout/Order safeguards, related Proposed decisions and separate evidence, evaluation, acceptance and implementation gates. |
