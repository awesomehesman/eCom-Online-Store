# DEC-0017 — Voucher and Promotion Stacking Policy

- **Identifier:** DEC-0017
- **Title:** Voucher and Promotion Stacking Policy
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

[`PRODUCT.md` §24 item 14](../../.ai/core/PRODUCT.md) leaves Voucher and Promotion stacking policy open. Its §16.6 calls for governed validity, eligibility, limits, stackability, exclusions, calculation order and Refund treatment. The [Approved Pricing Domain Specification](../domains/pricing/pricing-domain.md) REQ-PRC-010–012 assigns policy-permitted Promotion and Voucher evaluation to Pricing but expressly defers combination, precedence, maximum benefit, exclusions and calculation order. [Pricing Backend BPRC-REQ-021–022](../backend/pricing/pricing-backend.md) preserves the same deferral and requires safe failure for unresolved conflicts.

Cart may display non-authoritative commercial values, Checkout coordinates current validation, and Order owns the historical Order Snapshot. Payment owns financial execution and Refund Transactions; Return owns Return assessment and lifecycle. No Approved source establishes a stacking rule or allows a displayed, stale or conflicting value to become authoritative Pricing truth.

## Decision Question and Boundary

Under what Product policy may eligible Promotions and Vouchers combine, and what precedence, exclusions and calculation-order rules govern conflicts while preserving authoritative Pricing outcomes and historical Order values?

This is a Product-owned commercial-policy question, not a selection of a Pricing algorithm, Voucher format, Redemption mechanism, API, provider or persistence design. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 14 remains open; DEC-0017 is Proposed and non-authoritative.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation and unresolved trade-off |
| --- | --- | --- | --- |
| A — No stacking | A qualifying purchase may receive only one applicable Promotion or Voucher benefit. | Potentially simpler explanation and commercial predictability. | A choice among multiple eligible benefits, customer fairness, exclusion handling and treatment of existing offers still require policy and evidence. This option does not select which benefit wins. |
| B — Controlled stacking | Multiple eligible benefits may combine only under explicitly governed compatibility, precedence and exclusion rules. | Could support targeted campaigns while limiting conflicting combinations. | Rule maintenance, customer comprehension, calculation reproducibility and support burden may increase; no compatibility matrix or sequence is selected. |
| C — Broad stacking with safeguards | Multiple eligible benefits may combine subject to separately governed limits, eligibility and conflict protections. | Could offer broad promotional flexibility. | Greater exposure to unintended combined benefit, abuse and financial loss; limit, conflict and recovery evidence is essential. No cap or default compatibility is established. |

These are comparable policy alternatives, not current behavior, a ranking or a feasibility finding. None establishes Voucher ownership, redemption or restoration, a numerical benefit cap, Discount percentage, usage limit, calculation sequence or Refund rule.

## Affected Requirements and Domain Ownership

| Authority | Existing requirement or boundary preserved |
| --- | --- |
| Product | Owns the stacking policy and commercial trade-off under `PRODUCT.md` §§16.6, 24 and 25. |
| Pricing | Owns current authoritative commercial applicability and calculation under REQ-PRC-010–012 and BPRC-REQ-021–022. Conflicts without Approved rules fail safely; equivalent governed inputs and rule state must yield reproducible outcomes. |
| Cart | Owns shopping intent and may present Pricing results, but cannot lock a final Price, Discount, Promotion, Voucher or total. |
| Checkout | Coordinates final current revalidation and purchase submission without becoming Pricing, Payment or Order authority. |
| Order | Owns confirmed historical commercial values and the Order Snapshot; later commercial changes cannot silently rewrite it. |
| Payment | Owns Payment and Refund execution evidence, not the stacking or promotional Refund policy. |
| Return | Owns Return eligibility and lifecycle, not Pricing or Refund execution; post-purchase promotional treatment remains separately governed. |

Affected requirements include [Pricing REQ-PRC-009–012, 018, 020–021](../domains/pricing/pricing-domain.md), [Cart](../domains/cart/cart-domain.md) commercial-representation boundaries, [Checkout](../domains/checkout/checkout-domain.md) revalidation, [Order](../domains/order/order-domain.md) historical truth, [Payment REQ-PAY-034–035](../domains/payment/payment-domain.md), and [Return REQ-RET-027–029](../domains/return/return-domain.md). Their [Pricing](../backend/pricing/pricing-backend.md), [Cart](../backend/cart/cart-backend.md), [Checkout](../backend/checkout/checkout-backend.md), [Order](../backend/order/order-backend.md), [Payment](../backend/payment/payment-backend.md), and [Return](../backend/return/return-backend.md) backend specializations remain unchanged.

## Evaluation Criteria and Comparative Consequences

Product-led comparison must consider customer experience and transparent offer explanations; commercial predictability and margin exposure; Pricing calculation and maintenance complexity; independently governed Promotion and Voucher eligibility; conflict resolution and safe denial; precedence, exclusions and calculation order; any justified benefit limits; separately governed post-purchase Refund implications; auditability and reproducibility; operational support; and reversibility for active offers and historical Orders.

Option A may reduce combination complexity but needs a fair, understandable way to address competing eligible benefits. Option B may balance flexibility and control at the cost of explicit compatibility governance. Option C may improve offer flexibility while increasing loss, conflict and abuse risk. These are hypotheses for review, not evidenced outcomes or selected policy. None settles item 8 free-delivery promotion treatment, item 11 Return/Refund policy, item 25 tax and credit-note policy, or item 29 credit instruments.

## Security, Privacy and Abuse Impact

Customer-specific eligibility, Voucher identifiers and usage evidence must be protected under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md), with trusted server-side contextual Authorization where required, least privilege, default denial, safe error disclosure and proportionate audit. Repeated, replayed, stale or conflicting redemption attempts must not manufacture an applicable benefit or duplicate commercial effect. This record selects no abuse threshold, fraud mechanism, Customer segment, tracking practice or privacy purpose.

## Financial, Data and Historical Impact

An eventual stacking policy may change commercial exposure, Tax context, Checkout totals and post-purchase treatment. Finance must assess material outcomes before selection. Pricing remains authoritative for current calculated values; Order retains the confirmed historical Snapshot; Payment and Return retain their distinct post-purchase roles. The record creates no Voucher balance, Redemption state, schema, event taxonomy, reporting definition or historical recalculation rule. Applicable usage state must come from its owning authority; its ownership and mechanism are not assigned here.

## Compatibility, Migration and Operational Impact

Active offers, displayed Cart values, in-progress Checkout and historical Orders may need compatibility review if a policy is later Accepted. A later rule must not silently reinterpret confirmed Order values or presume a Voucher restoration or Refund adjustment. Support and campaign operations need clear approved explanations and conflict handling; no approval workflow, migration, operational SLA, API or Event Contract is selected now.

## Evidence Required Before Selection

Product and the accountable commercial owner must provide attributable campaign goals, intended customer outcomes, eligibility and exclusion questions, and acceptable commercial risk. Pricing must provide rule-conflict and reproducibility cases; Cart, Checkout and Order must review representation, revalidation and history; Payment and Return must review post-purchase boundaries. Finance must assess financial exposure, and Security must assess abuse and protected-input risks. Operations and Support must review explainability and correction needs; Legal and Privacy review applies where material. Evidence must distinguish actual current practice, forecasts, proposed policy and stakeholder approval. None is claimed complete in this record.

## Open Questions and Acceptance Prerequisites

1. Which independently eligible Promotion and Voucher benefits, if any, may combine, and how should conflicting or overlapping rules be resolved? No combination is selected.
2. What Product-owned precedence, exclusions and calculation order, if any, are necessary for deterministic outcomes? No sequence or default winner is selected.
3. What evidence justifies any benefit limit, usage rule or commercial guardrail? No numerical limit or cap is selected.
4. How should customers understand eligibility, changed offers and safe failures without exposing abuse-sensitive details?
5. What post-purchase Promotion or Voucher treatment, if any, is required by a separately governed Return/Refund policy? DEC-0016 remains Proposed; no restoration or Refund rule is inferred.
6. Which free-delivery, Tax and credit-instrument dependencies require separate Product, Finance or Legal decisions before a particular combination is viable?
7. Which future Specification or Contract amendments would follow an Accepted policy? None is made or authorized here.

Formal comparison requires a separately governed scope and attributable evidence. Product and the accountable commercial owner lead acceptance review, with Pricing, Cart, Checkout, Order, Payment, Return, Finance, Security and Operations; Legal and Privacy review applies where material. Acceptance requires the authority and durable review evidence specified by [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires separate applicable Specification, Contract, Architecture, Security and Engineering gates.

## Explicit Non-Authorizations and Supersession

DEC-0017 supersedes nothing and is superseded by nothing. It does not select or accept Option A, B or C; close Product item 14; establish stacking, compatibility, precedence, exclusions, calculation order, benefit cap, percentage, Voucher eligibility, Redemption or restoration, promotional Refund treatment, provider or dependency; create implementation Requirements, API or Event Contracts, events, schema or algorithm; change an Approved Product or Architecture source; or authorize implementation. All existing decision statuses and owner authorities remain unchanged.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.6, 24 item 14, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), and [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) — governance, Product authority, domain ownership and terminology.
- [Pricing Domain REQ-PRC-009–012, 018 and 020–021](../domains/pricing/pricing-domain.md) and [Pricing Backend BPRC-REQ-021–022](../backend/pricing/pricing-backend.md) — Pricing ownership and combination-policy deferral.
- [Cart Domain](../domains/cart/cart-domain.md), [Checkout Domain](../domains/checkout/checkout-domain.md), [Order Domain](../domains/order/order-domain.md), [Payment Domain](../domains/payment/payment-domain.md), and [Return Domain](../domains/return/return-domain.md) — affected representation, orchestration, historical and post-purchase boundaries.
- [Cart Backend](../backend/cart/cart-backend.md), [Checkout Backend](../backend/checkout/checkout-backend.md), [Order Backend](../backend/order/order-backend.md), [Payment Backend](../backend/payment/payment-backend.md), and [Return Backend](../backend/return/return-backend.md) — corresponding bounded backend specializations.
- [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) — Proposed Return, Exchange and Refund policy; no promotional post-purchase treatment is resolved.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed unselected no-stacking, controlled-stacking and broad-stacking policy alternatives; preserved Pricing calculation authority, Cart/Checkout coordination, Order history, Payment/Return boundaries, Product item 14 as open, and separate evidence, acceptance and implementation gates. |
