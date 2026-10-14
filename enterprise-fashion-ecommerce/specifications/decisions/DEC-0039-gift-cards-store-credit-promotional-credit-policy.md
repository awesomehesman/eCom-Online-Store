# DEC-0039 — Gift Cards, Store Credit and Promotional Credit Policy

- **Identifier:** DEC-0039
- **Title:** Gift Cards, Store Credit and Promotional Credit Policy
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

[`PRODUCT.md` §24 item 29](../../.ai/core/PRODUCT.md) leaves “Gift cards, store credit, and promotional credit policy” unresolved. Product §§16.6 and 16.10–16.12 already require governed Promotion terms, historical tax and commercial integrity, separation of Return and Refund, and protection against foreseeable payment, refund, promotion and administrative abuse. None approves a credit instrument or assigns ownership of a monetary balance. Product §25.1 places commercial policy with Product and the accountable business owner, with Finance, Operations and Legal participation where required.

The concepts must remain separate. An existing Discount or Voucher is a governed Pricing or Promotion input, not by that fact a gift card. A gift card could represent prepaid or otherwise funded value; store credit could arise from a governed commercial adjustment; promotional credit could be an incentive. These distinctions frame questions only. They establish no issuance, balance, liability, redemption or Refund capability. Each instrument may be considered independently; supporting one would not automatically support the others.

## Decision Question and Status

Should the Product support gift cards, store credit or promotional credit, individually or together, and what customer-facing policy would govern each if separately approved?

**NO ALTERNATIVE SELECTED.** This 0.1.0 Proposed, Product-owned record is non-authoritative. `PRODUCT.md` §24 item 29 remains open. Drafting authorizes neither comparative evaluation nor a capability, policy, Contract or implementation.

## Unresolved Policy Questions

For each instrument independently, Product and affected owners must determine whether it should exist; what would fund or authorize issuance; who could receive and redeem it; its eligibility, partial redemption and remaining-value treatment; expiry and forfeiture, if any; and whether transfer is permitted. They must also resolve cancellation, Return, Refund, reversal and failed-transaction restoration; interaction with Discounts, Vouchers and Promotions; balance and transaction-history authority; accounting, tax, reconciliation and liability; fraud and abuse controls; and administrative permissions and Audit Records. No answer, numerical value or common treatment is selected here.

## Alternatives Considered — Not Formally Compared

| Option | Conditional policy approach | Potential benefit | Limitations and evidence needed |
| --- | --- | --- | --- |
| A — No Credit Instruments | Consider no initial gift-card, store-credit or promotional-credit capability; separately approved existing commercial mechanisms remain governed on their own terms. | Could avoid new monetary exposure and operational complexity. | May leave customer or commercial needs unmet. Requires evidence of those needs, current alternatives and applicable obligations; it cannot override a binding obligation. |
| B — Selective Instrument Support | Consider one or more of the three instruments independently, with separate Product selection and affected-owner governance for each. | Could address a bounded need without assuming identical treatment for distinct instruments. | Requires evidence and approval for each chosen instrument's funding, eligibility, balance, reversals, tax, liability, controls and ownership. No instrument or owner is chosen by this option. |
| C — Broader Coordinated Credit Policy | Consider multiple instruments under a coordinated customer-facing commercial policy while preserving their distinct sources and obligations. | Could offer a coherent experience if a sufficient business and control case exists. | Adds cross-domain, financial, Security and operational complexity. It does not presume a new Domain, shared balance ledger, common redemption mechanism or provider. |

All three approaches are **conditional, unselected, unranked and non-authoritative**. They do not imply that any instrument is currently available.

## Approved Authority Boundaries

| Owner or function | Boundary preserved |
| --- | --- |
| Pricing and Promotion | [REQ-PRC-002, 009–012 and 021](../domains/pricing/pricing-domain.md) govern current commercial calculation, Discounts, Promotions and Vouchers, and explicitly defer gift-card, store-credit and promotional-credit treatment. No credit balance is inferred from a Voucher or Discount. |
| Cart and Checkout | [REQ-CART-016–017](../domains/cart/cart-domain.md) keep Cart commercial values provisional; [REQ-CHK-013–014 and 027–029](../domains/checkout/checkout-domain.md) require current Pricing revalidation and preserve Payment-evidence boundaries. Neither creates credit eligibility or redemption. |
| Payment | [REQ-PAY-003–006, 009, 018–024 and 034–035](../domains/payment/payment-domain.md) retain authoritative Payment processing, Money and Currency, validated outcomes, reconciliation and Refund execution. They do not make Payment a credit-ledger owner or make Refund-to-credit an approved method. |
| Order | [REQ-ORD-023–029 and 039](../domains/order/order-domain.md) preserve confirmed historical commercial values, Payment and Order independence, and Return/Refund non-authority. Later credit treatment cannot silently rewrite an Order Snapshot. |
| Return | [REQ-RET-027–029 and 035](../domains/return/return-domain.md) preserve Return assessment, Refund coordination and Pricing, Tax and credit non-authority. Return cannot issue store credit by implication. |
| Customer | [Customer Domain](../domains/customer/customer-domain.md) retains Customer identity, Account and Customer-data authority, subject to Privacy and Authorization. That authority does not assign ownership of a monetary balance. |
| Administration | [REQ-ADM-007, 025 and 029](../domains/admin/admin-domain.md) permit only contextually authorized coordination with owning Domains. No credit issuance, adjustment, Staff Role or Permission follows from this proposal. |
| Reporting | [REQ-RPT-002, 028, 031 and 039](../domains/reporting/reporting-domain.md) preserve derived, source-attributed reporting and contextual access. Reporting does not create a credit source, liability or financial truth. |
| Security and Architecture | [Security Standards §§27 and 36](../../.ai/core/SECURITY-STANDARDS.md) preserve protection against replay, duplicate financial effects, untrusted commercial inputs and unverifiable Payment outcomes. [Architecture](../../.ai/core/ARCHITECTURE.md) retains separate authority over material technical structure and integration. |

The [Pricing Backend](../backend/pricing/pricing-backend.md), [Checkout Backend](../backend/checkout/checkout-backend.md), [Payment Backend](../backend/payment/payment-backend.md), [Order Backend](../backend/order/order-backend.md), [Return Backend](../backend/return/return-backend.md), [Reporting Backend](../backend/reporting/reporting-backend.md) and [Administration Backend](../backend/admin/admin-backend.md) likewise leave item 29 credit behavior unselected. No existing Domain is assigned balance or transaction-history ownership by this record.

## Related Decisions and Dependencies

[DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md) leaves Voucher and Promotion stacking Proposed; [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md) leaves Return, Exchange and Refund policy Proposed; [DEC-0028](DEC-0028-initial-payment-methods-provider-policy.md) leaves initial payment methods Proposed; [DEC-0030](DEC-0030-free-delivery-threshold-promotional-treatment-policy.md) leaves promotional delivery treatment Proposed; [DEC-0031](DEC-0031-initial-tax-display-invoice-requirements-policy.md) and [DEC-0035](DEC-0035-south-african-tax-invoice-credit-note-policy.md) leave tax and financial-document policy Proposed; and [DEC-0036](DEC-0036-fraud-screening-manual-review-policy.md) leaves fraud review Proposed. [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) also leaves Staff permissions Proposed. None authorizes a credit instrument, balance owner, credit Refund, tax treatment or administrative action. They are conditional dependencies or shared evidence needs for later selection, not prerequisites to framing item 29.

## Evaluation Criteria and Evidence Required

Before formal comparison, collect attributable evidence for customer demand and commercial purpose; the funding source and financial exposure of each instrument; expected operational cost and support burden; qualified Finance/Tax accounting, liability and reconciliation analysis; qualified Legal and consumer-rights analysis; fraud, abuse, transfer and loss risks; Payment allocation, failed or reversed transaction and Refund interactions; interaction with existing Discounts, Vouchers and Promotions; Order history and source-data lineage; balance and transaction-history ownership options; Security and Authorization controls; administrative accountability; and reporting and Audit Record needs. Evidence must distinguish the three instruments and identify uncertainty. This record supplies no measurements, scores, provider assessment, legal interpretation or owner approval.

## Security, Data, Consequences and Operational Impact

A later approved policy could address customer or commercial needs, but could also create funded-value liability, misuse, duplicate redemption, inconsistent balances, misleading presentation, uncertain Payment allocation, incorrect Refund treatment or unreconciled financial history. Any proposed balance or credit transaction would require explicit ownership, trusted evidence, idempotency, Authorization, auditability, privacy, recovery and reconciliation review. Those are risks and review subjects, not a selected lifecycle, schema, ledger or operating process.

## Reviewers and Future Governance Gates

Product leads the policy question with the accountable commercial business owner. Pricing, Cart, Checkout, Payment, Order, Return, Customer, Administration and Reporting owners review affected boundaries. Finance/Tax, Legal, Security, Operations and Architecture review their respective financial, legal, abuse, operational and technical implications. Naming reviewers does not claim review or approval.

1. **Initial Proposed drafting:** This record frames three distinct concerns and conditional alternatives only.
2. **Evidence collection:** Establish attributable commercial, financial, customer, legal, control and operational evidence for each instrument.
3. **Qualified Finance/Tax, Legal, Security, Operations and affected-owner review:** Validate obligations, authority, risks and feasible boundaries without inferring approval.
4. **Comparative evaluation:** Obtain separate governance authorization before comparing or ranking alternatives.
5. **Product selection and DEC acceptance:** Select any instrument and policy only through Product and required-owner approval, with canonical Product authority synchronized where necessary.
6. **Architecture, Specification and Contract governance:** Resolve ownership, lifecycle, provider, integration and Contract changes separately if an accepted policy requires them.
7. **Implementation authorization:** Implement or deploy only after the applicable prior gates are complete.

Completing one gate does not authorize the next.

## Validation Criteria

This record remains valid as an initial proposal only if it (1) traces exact `PRODUCT.md` §24 item 29 wording; (2) remains 0.1.0 Proposed, Product-owned and non-authoritative; (3) keeps gift cards, store credit and promotional credit independently unresolved; (4) keeps three approaches unselected and unranked; (5) preserves every existing Domain and Security boundary without inventing monetary ownership; (6) invents no financial, legal or operational policy; and (7) keeps evidence, specialist review, evaluation, acceptance, Architecture, Specification, Contract and implementation as separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0039 supersedes nothing and is superseded by nothing. It authorizes no gift-card, store-credit or promotional-credit capability; stored-value instrument or monetary balance; issuance, funding, redemption, adjustment, restoration, transfer, expiry or forfeiture workflow; balance-owning Domain or ledger; Staff Role or Permission; accounting or tax treatment; Refund-to-credit conversion; provider or integration; API, Event or Message Contract; implementation or deployment. It selects no alternative, owner, amount, threshold, term, formula, lifecycle or mechanism and claims no comparative evaluation or approval.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.6–16.12, 24 item 29 and 25](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md §§27 and 36](../../.ai/core/SECURITY-STANDARDS.md) — Product question and governing controls.
- [Pricing Domain REQ-PRC-002, 009–012 and 021](../domains/pricing/pricing-domain.md), [Cart Domain REQ-CART-016–017](../domains/cart/cart-domain.md), [Checkout Domain REQ-CHK-013–014 and 027–029](../domains/checkout/checkout-domain.md), [Payment Domain REQ-PAY-003–006, 009, 018–024 and 034–035](../domains/payment/payment-domain.md), [Order Domain REQ-ORD-023–029 and 039](../domains/order/order-domain.md) and [Return Domain REQ-RET-027–029 and 035](../domains/return/return-domain.md) — commercial and transaction boundaries.
- [Customer Domain](../domains/customer/customer-domain.md), [Administration Domain REQ-ADM-007, 025 and 029](../domains/admin/admin-domain.md) and [Reporting Domain REQ-RPT-002, 028, 031 and 039](../domains/reporting/reporting-domain.md) — Customer, operational and derived-data authority.
- [Pricing Backend](../backend/pricing/pricing-backend.md), [Cart Backend](../backend/cart/cart-backend.md), [Checkout Backend](../backend/checkout/checkout-backend.md), [Payment Backend](../backend/payment/payment-backend.md), [Order Backend](../backend/order/order-backend.md), [Return Backend](../backend/return/return-backend.md), [Customer Backend](../backend/customer/customer-backend.md), [Administration Backend](../backend/admin/admin-backend.md) and [Reporting Backend](../backend/reporting/reporting-backend.md) — approved implementation-scope boundaries without credit policy selection.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md), [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md), [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md), [DEC-0028](DEC-0028-initial-payment-methods-provider-policy.md), [DEC-0030](DEC-0030-free-delivery-threshold-promotional-treatment-policy.md), [DEC-0031](DEC-0031-initial-tax-display-invoice-requirements-policy.md), [DEC-0035](DEC-0035-south-african-tax-invoice-credit-note-policy.md) and [DEC-0036](DEC-0036-fraud-screening-manual-review-policy.md) — related Proposed, non-authoritative decisions.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed gift cards, store credit and promotional credit as separate unresolved concerns with three unselected approaches; preserved existing Domain authority and separate financial, legal, Security, evaluation, acceptance and implementation gates. |
