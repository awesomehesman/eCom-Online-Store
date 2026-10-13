# DEC-0016 — Returns, Exchanges, and Refund Policy

- **Identifier:** DEC-0016
- **Title:** Returns, Exchanges, and Refund Policy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-10-13
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

[`PRODUCT.md` §24 item 11](../../.ai/core/PRODUCT.md) leaves Returns, exchanges, and refund policy open. Its §16.11 requires explicit eligibility and lifecycle rules and separates Return, Refund, Refund Transaction, provider processing, Order financial state, Inventory disposition and customer communication. The [Approved Return Domain Specification](../domains/return/return-domain.md) REQ-RET-014–017 requires eligibility under Approved policy, explicit outcomes and valid Return Authorization, but selects no Return Window, exclusions, Exchange rule or Refund treatment. REQ-RET-027–029 preserves Payment authority and distinguishes Return from Refund. The [Approved Return Backend Specification](../backend/return/return-backend.md) specializes those boundaries without resolving Product policy.

The repository establishes owner responsibilities and safety requirements, not a Return or Exchange entitlement or a Refund calculation. This Proposed record frames the unresolved Product choice for later attributable evidence and review.

## Decision Question

What Product policy governs Return and Exchange eligibility, and when and how may an eligible outcome support a separately approved Refund, while preserving each owning domain's authority?

## Decision — Unselected

**NO OPTION SELECTED.** `PRODUCT.md` §24 item 11 remains open. No Return Window, eligibility category, Exchange entitlement, Refund amount or method, or current Product baseline is established here. The candidates below have not been formally evaluated or accepted.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A | A common Return eligibility policy for policy-permitted purchased items, with Exchange and Refund treatment separately conditioned on an approved Return outcome. | May simplify customer explanation and consistent initial assessment. | May fail to address materially different item or fulfilment conditions; operational and legal suitability is unproven. |
| B | A differentiated Return eligibility policy based on explicitly approved item and fulfilment circumstances, with separately governed Exchange and Refund outcomes. | May better match distinct conditions and business risks. | Increases transparency, fairness, evidence, audit and support burden; no categories or thresholds are selected here. |
| C | A staged policy in which a request can receive an explicit pending or review outcome before any eligibility, Exchange or Refund disposition is confirmed. | May preserve uncertainty where evidence or physical inspection is material. | Can delay customer resolution and require review capacity; it cannot make review mandatory for every case without Product and operational evidence. |

These alternatives concern policy treatment only. None defines a Return Window, fee, Refund amount, method, provider behavior, inspection threshold, shipping responsibility or operational commitment. A later comparison may revise or reject any candidate.

## Evaluation Criteria and Evidence Required

Evaluation must consider customer fairness and understandable policy content; applicable consumer-protection and legal obligations; operational feasibility and support capacity; fraud and abuse controls without fabricated denial; financial correctness across Refund approval, execution and reconciliation; Inventory integrity and Restocking ownership; Return and Refund lifecycle consistency; attributable evidence and Audit Records; and compatibility and reversibility for existing Orders and published policy. Different item, fulfilment and uncertainty contexts must be examined without preselecting categories or numerical rules.

Before selection, Product and the accountable business owner must provide attributable intended customer outcomes, commercial rationale and proposed scope. Return, Payment, Order, Shipping/Fulfilment, Inventory and Customer owners must supply boundary and operational evidence for cases they own. Operations must assess capacity, exception and customer-support consequences. Security must assess authorization, abuse, privacy and audit risks. Finance, Legal and Privacy must review where materially affected, including applicable consumer-protection requirements. Repository requirements, proposed policy, actual operational evidence and stakeholder review must remain distinguishable; none is claimed complete here.

## Authority and Domain Boundaries

| Concern | Existing authority preserved |
| --- | --- |
| Product | Owns the business policy for Return and Exchange eligibility and governed Refund treatment, with applicable business, Finance and Legal review. |
| Return | Owns Return Request, Return Items, eligibility assessment under Approved policy, Return Authorization, Return lifecycle and disposition. An approved Return outcome alone does not execute a Refund. |
| Payment | Owns payment-side Refund request processing, provider evidence, Refund and Refund Transaction outcomes; execution requires governed eligibility and amount input. |
| Order | Owns historical Order and Order Snapshot truth and applicable financial history; Return or Payment activity cannot silently rewrite it. |
| Shipping/Fulfilment | Owns Shipment, transport, delivery and reverse-logistics execution evidence. A handoff or shipment state is not Return acceptance. |
| Inventory | Owns Stock, Available-to-Sell, Stock Reservation and Restocking effects; Return disposition alone cannot establish sellable Stock. |
| Customer and Account | Own Customer-local profile and Account business truth, not Return, Refund or Shipping outcomes. Protected access requires current contextual Authorization; a guest association or contact detail is not proof. |

Return Authorization, physical receipt, inspection, Exchange, Refund approval, provider processing, Refund Transaction, Inventory disposition and customer communication remain distinguishable. Duplicate, conflicting, partial, unavailable and uncertain outcomes must not become fabricated success. [Payment Domain REQ-PAY-034–035](../domains/payment/payment-domain.md), [Order Domain REQ-ORD-039](../domains/order/order-domain.md), [Shipping Domain REQ-SHP-032](../domains/shipping/shipping-domain.md) and Return REQ-RET-027–029 retain their force.

## Open Questions and Dependencies

1. Which purchased items and historical fulfilment conditions, if any, should be eligible for Return or Exchange, and what evidence would justify a differentiated rule? No category or condition is selected.
2. What customer-facing policy and applicable legal obligations constrain any Return Window, exclusion, shipping responsibility or remedy? No window, threshold or commitment is selected.
3. When may a governed Return outcome support a distinct Refund eligibility and amount decision, and which authority confirms the approved amount before Payment execution?
4. What, if any, Exchange treatment is intended, and how would separate Product, Pricing, Inventory, Order, Shipping and Payment effects be governed without implying completion from Return state?
5. How should pending, denied, disputed, inspected, partial and uncertain outcomes be explained and reconciled without exposing protected details or promising an external effect?
6. How will any guest requester prove entitlement to the exact historical Order? [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) remains Proposed and selects no guest-order access mechanism.
7. Where does cancellation end and a separately eligible Return begin? [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md) remains Proposed; its preferred Dispatch cutoff is not Accepted policy.
8. Which tax, invoice, Credit Note, Voucher, Promotion, gift-card, store-credit, shipping-fee and other commercial treatments require separate Product, Finance or Legal decisions? This record resolves none; [DEC-0015](DEC-0015-stock-reservation-duration-policy.md) likewise leaves Inventory reservation duration open where an Exchange may affect Stock.
9. Which future Contracts, provider arrangements, customer disclosures and compatibility work would an Accepted policy require? No Contract, provider or mechanism is selected here.

## Consequences, Risks and Reversibility

A simpler common policy may be easier to communicate but may treat distinct situations poorly. Differentiation may improve fit while increasing audit, support and consistency burden. A staged review path can preserve uncertainty but may delay resolution. Every candidate carries financial, fraud, Inventory and customer-trust risks requiring evidence. A later policy change must preserve historical Order and Return facts, applicable policy evidence and existing customer-facing commitments; migration and reversibility require separate review. No candidate is asserted to be the current Product baseline or superior without evaluation.

## Security Impact

Customer and Staff actions require trusted, contextual Authorization, least privilege, default denial, privacy protection and proportionate audit under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). Fraud input does not itself establish Return ineligibility or a Refund denial without approved policy. No identity, abuse-screening or provider mechanism is selected.

## Data Impact

Return, Order, Payment, Shipping, Inventory and Customer retain their separately owned records and provenance. Reasons, evidence, contact data and financial information require purpose limitation and protection. This record chooses no schema, identifier, retention rule, event or analytics taxonomy.

## Compatibility and Migration Impact

No existing Order, Return, Payment, Shipping, Inventory, Customer, API or Event Contract changes now. Any accepted policy must review historical purchases, published terms, active requests, uncertain external effects and possible Contract changes without silently changing authoritative history. No migration is authorized.

## Operational Impact

Support, inspection, reverse logistics, financial reconciliation and customer communication may be affected by an eventual policy. Operations must assess feasibility and exception handling. This record sets no workflow, approval chain, service level, staffing rule, Return Window or numerical target.

## Decision and Acceptance Gates

This record is **Proposed** and non-authoritative. Formal comparison requires separately governed scope and attributable evidence; no policy alternative is selected by this creation. Product and accountable business authority must lead acceptance review, with Return, Payment, Order, Shipping/Fulfilment, Inventory, Customer, Operations and Security, plus Finance, Legal and Privacy where materially affected. Acceptance must follow [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), then be synchronized into canonical Product guidance. Implementation requires the Accepted policy and separate applicable Architecture, Contract, Security and Engineering gates.

## Explicit Non-Authorizations

This Proposed record does not close `PRODUCT.md` §24 item 11; select or accept a Return or Exchange policy; establish a Return Window, eligibility category, exclusion, Exchange entitlement, shipping responsibility, fee, Refund eligibility, amount, method or timing; guarantee a Refund, Stock, Shipment or communication effect; resolve DEC-0013, DEC-0014, DEC-0015 or another open Product decision; select a provider or dependency; create or change an API or Event Contract, status transition, schema, workflow or mechanism; or authorize implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.11, 24 item 11, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md), and [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) — governing policy, ownership, terminology and record process.
- [Return Domain REQ-RET-014–017, 021, 027–029, 032–033 and 037](../domains/return/return-domain.md) and [Return Backend BRET-REQ-014–016, 022 and 027–029](../backend/return/return-backend.md) — unresolved eligibility and separate lifecycle effects.
- [Payment Domain REQ-PAY-034–035](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md) — payment-side Refund execution and policy separation.
- [Order Domain REQ-ORD-037–039](../domains/order/order-domain.md), [Order Backend](../backend/order/order-backend.md), [Shipping Domain REQ-SHP-032](../domains/shipping/shipping-domain.md), and [Shipping Backend](../backend/shipping/shipping-backend.md) — historical, cancellation and logistics boundaries.
- [Inventory Domain](../domains/inventory/inventory-domain.md), [Inventory Backend](../backend/inventory/inventory-backend.md), [Customer Domain](../domains/customer/customer-domain.md), [Customer Backend](../backend/customer/customer-backend.md), [Checkout Domain](../domains/checkout/checkout-domain.md), and [Checkout Backend](../backend/checkout/checkout-backend.md) — separately owned Stock, Customer and purchase-context boundaries.
- [DEC-0013](DEC-0013-guest-checkout-account-requirement.md), [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md), and [DEC-0015](DEC-0015-stock-reservation-duration-policy.md) — Proposed and unresolved related Product decisions.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed unselected Return, Exchange and Refund policy alternatives and review evidence while preserving Return, Payment, Order, Shipping, Inventory and Customer authority, related Proposed decisions, open Product item 11, and separate acceptance and implementation gates. |
