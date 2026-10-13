# DEC-0031 — Initial Tax Display and Invoice Requirements Policy

- **Identifier:** DEC-0031
- **Title:** Initial Tax Display and Invoice Requirements Policy
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

[`PRODUCT.md` §24 item 9](../../.ai/core/PRODUCT.md) leaves “Tax-inclusive display and invoice requirements” unresolved. The initial operating context in Product §6 is South African. Product §16.10 requires customer-visible prices to follow an approved South African tax-display policy, confirmed Order and invoice records to preserve applicable tax values, and tax, invoice-numbering and document requirements to be defined before production launch. Client-side tax or invoice totals are not authoritative. This record does not interpret South African law or assert that qualified tax or legal validation has occurred.

The [Approved Pricing Domain Specification REQ-PRC-013](../domains/pricing/pricing-domain.md) and [Pricing Backend BPRC-REQ-023](../backend/pricing/pricing-backend.md) permit governed tax-related calculation only from qualified Approved policy and inputs. Pricing cannot choose Tax Inclusive or Tax Exclusive treatment or a legal Invoice or Credit Note format. [Order REQ-ORD-023–025](../domains/order/order-domain.md) and its [Backend Specification](../backend/order/order-backend.md) preserve confirmed historical commercial evidence without creating tax, invoice or Credit Note policy. [Checkout REQ-CHK-037](../domains/checkout/checkout-domain.md) and its [Backend Specification](../backend/checkout/checkout-backend.md) preserve orchestration and current commercial revalidation without independent tax calculation or document authority. [Payment REQ-PAY-044](../domains/payment/payment-domain.md), [Shipping REQ-SHP-042](../domains/shipping/shipping-domain.md) and [Return REQ-RET-035](../domains/return/return-domain.md), with their respective [Payment](../backend/payment/payment-backend.md), [Shipping](../backend/shipping/shipping-backend.md) and [Return](../backend/return/return-backend.md) backend specializations, likewise do not establish commercial-document policy.

These Approved requirements protect commercial integrity and domain boundaries. They do not select a customer-facing tax-display approach or invoice treatment. [`DECISIONS.md`](../../.ai/core/DECISIONS.md) governs the later Product decision lifecycle; [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) preserves commercial-integrity, Authorization and tamper-resistance safeguards.

## Decision Question and Status

What customer-facing tax-display and invoice-requirements approach should the initial South African offering adopt, subject to qualified Finance/Tax and Legal validation? Which contexts, customer disclosures, confirmed commercial evidence and operational capabilities must inform a later Product policy?

**NO ALTERNATIVE SELECTED.** No comparative evaluation is authorized, no policy is Accepted, and `PRODUCT.md` §24 item 9 remains open.

## Alternatives Considered — Not Formally Compared

| Option | Initial policy candidate | Potential benefit | Limitation, evidence and dependency |
| --- | --- | --- | --- |
| A — Uniform initial presentation | Consider one consistent customer-facing tax-display and invoice-requirements approach across the initial offering, subject to qualified legal and tax validation. | Could make customer understanding and support simpler. | Different transaction or document contexts may require distinctions; Finance/Tax and Legal evidence, commercial-context inventory and affected-owner review are required. Legal compliance is not presumed. |
| B — Context-sensitive presentation | Consider differentiated customer-facing tax presentation and invoice expectations only where qualified legal and tax requirements establish a necessary distinction. | Could accommodate materially different governed contexts. | More variation may increase explanation, consistency, audit and operational burden; attributable requirements, context mapping and qualified review are required. No distinction is selected. |
| C — Bounded initial presentation | Consider a narrowly scoped initial presentation and invoice approach, with additional contexts deferred to separately governed future policy. | Could limit initial complexity while preserving future extension. | Deferred contexts may be necessary for launch or customer rights; Finance/Tax, Legal, Product and operational evidence must establish any lawful initial scope. No deferral or launch exclusion is approved. |

The alternatives are **unselected, unranked and non-authoritative**. None is represented as legally compliant, and none selects Tax Inclusive or Tax Exclusive treatment, an invoice rule, a tax value or a technical mechanism.

## Item 9 and Item 25 Boundary

`PRODUCT.md` §24 item 9 concerns initial tax-display and invoice-requirements framing. Item 25 separately concerns the broader South African tax-display, invoice and Credit Note policy. **Both remain unresolved.** DEC-0031 does not establish that broader policy; define Credit Note eligibility, issuance, contents or numbering; select accounting adjustments or Refund-related tax treatment; or create a second competing invoice-policy authority. Any future overlap requires explicit Product, Finance/Tax and Legal coordination and reconciliation in canonical Product guidance before acceptance or implementation.

## Evaluation Criteria and Required Evidence

A separately authorized comparison must assess applicable South African legal obligations and qualified Finance/Tax interpretation; customer price transparency; consistency across storefront contexts; Checkout presentation; confirmed Order commercial evidence; invoice issuance expectations; accuracy, auditability, correction and historical integrity; Payment and Refund interactions; Shipping-charge presentation; Privacy and personal-data exposure; Security and tamper resistance; operational feasibility; customer-support implications; reversibility and future expansion. These are criteria, not evidence or legal conclusions. No Finance/Tax, Legal or stakeholder approval is asserted.

## Domain Authority and Approved Safeguards

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns customer-facing tax-presentation and invoice-expectation policy, subject to qualified validation; this record selects none. |
| Finance/Tax | Validates applicable tax obligations, accounting implications and evidence requirements; no interpretation is attributed to it here. |
| Legal | Validates applicable legal obligations, consumer disclosures and document requirements; no legal conclusion is attributed here. |
| Pricing | Owns authoritative tax-related commercial calculation only when qualified Approved policy and inputs support it; it cannot select display or document policy. |
| Order | Owns confirmed historical commercial evidence and lifecycle, without independently recalculating Tax or defining document policy. |
| Checkout | Coordinates current authoritative commercial revalidation before commitment without becoming tax or invoice authority. |
| Payment | Retains Payment and Refund transaction truth, without defining tax treatment or document policy. |
| Shipping | Retains Shipping, quotation and delivery evidence without defining tax or document policy. |
| Return | Retains Return lifecycle and assessment authority without determining tax adjustment or Credit Note treatment. |
| Security and Privacy | Preserve commercial integrity, contextual Authorization, tamper resistance, protected personal data and applicable disclosure safeguards. |
| Architecture and Engineering | Review affected integration, Contract and feasibility boundaries without selecting a mechanism or implementation through this record. |
| Operations | Reviews support, correction and document-handling implications without an operational workflow being selected. |

Current Pricing truth, Checkout revalidation, confirmed Order tax and commercial history, Payment financial truth, Shipping delivery evidence and Return assessment must remain distinct. An unverified client calculation, displayed estimate, Payment outcome or document representation cannot independently establish the tax or invoice policy.

## Reviewers, Dependencies and Future Gates

Product leads the policy question. Finance/Tax, Legal, Pricing, Checkout, Order, Payment, Shipping, Return, Security, Privacy, Architecture, Engineering and Operations must review matters within their respective authority. Listing reviewers is not evidence of review or approval.

1. **Qualified evidence:** Obtain Finance/Tax and Legal evidence for applicable obligations, customer presentation and document expectations. No evidence gate is claimed complete.
2. **Approved-source review:** Reconcile the relevant Approved Product, Pricing, Checkout, Order, Payment, Shipping and Return requirements without expanding their authority.
3. **Comparative Product evaluation:** Obtain separate authorization before comparing the three alternatives. This record conducts and authorizes none.
4. **Product selection:** Select any policy only through separate Product authority with qualified Finance/Tax and Legal validation.
5. **Acceptance:** Obtain affected-owner review and formal decision acceptance. Proposed text is not Accepted policy.
6. **Canonical synchronization:** Update Product guidance as required and explicitly reconcile item 9 with separately open item 25 before allowing overlapping policy to bind.
7. **Downstream governance:** Authorize any necessary Specification, Contract, Architecture, Security or Engineering changes and implementation separately.
8. **Launch readiness:** Verify applicable compliance, commercial integrity, document handling and operational readiness before production reliance.

No gate is deemed complete by this Proposed record.

## Validation Criteria

Review of this record passes only if: (1) metadata remains 0.1.0 Proposed, Product-owned and non-authoritative; (2) exactly three alternatives remain unselected and unranked; (3) Product §24 items 9 and 25 both remain open; (4) no tax law is interpreted and no Tax Inclusive, Tax Exclusive, rate, formula or exemption policy is selected; (5) no Invoice or Credit Note policy is established; (6) Pricing calculation, Order historical evidence, Checkout revalidation and Payment, Shipping and Return boundaries remain unchanged; (7) qualified Finance/Tax and Legal review remains required without fabricated approval; and (8) comparative evaluation, acceptance and implementation remain separately unauthorized.

## Decision Status and Explicit Non-Decisions

DEC-0031 remains **0.1.0 Proposed, Product-owned and non-authoritative**. It supersedes nothing and is superseded by nothing. No alternative is selected, no tax-display or invoice policy is Accepted, no implementation is authorized, and `PRODUCT.md` §24 items 9 and 25 remain unresolved. It selects no Tax Inclusive or Tax Exclusive treatment, tax rate, formula, classification, exemption or registration interpretation; Invoice format, numbering, issuance trigger, delivery channel or retention period; Credit Note policy, contents or numbering; Refund-related tax adjustment; accounting treatment; provider, API, Contract, Event, schema or technical mechanism. It creates no tax rule or financial-document requirement.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§6, 16.10 and 24 items 9 and 25](../../.ai/core/PRODUCT.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product, decision, commercial and security boundaries.
- [Pricing Domain REQ-PRC-013](../domains/pricing/pricing-domain.md) and [Pricing Backend BPRC-REQ-023](../backend/pricing/pricing-backend.md) — qualified tax policy and calculation boundary.
- [Order Domain REQ-ORD-023–025](../domains/order/order-domain.md) and [Order Backend](../backend/order/order-backend.md); [Checkout Domain REQ-CHK-037](../domains/checkout/checkout-domain.md) and [Checkout Backend](../backend/checkout/checkout-backend.md) — confirmed history and orchestration boundaries.
- [Payment Domain REQ-PAY-044](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md); [Shipping Domain REQ-SHP-042](../domains/shipping/shipping-domain.md) and [Shipping Backend](../backend/shipping/shipping-backend.md); [Return Domain REQ-RET-035](../domains/return/return-domain.md) and [Return Backend](../backend/return/return-backend.md) — separate financial, delivery and Return authority.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Framed three unselected initial tax-display and invoice-requirements alternatives, preserved qualified Finance/Tax and Legal review, authoritative Pricing and Order boundaries, and separate PRODUCT.md items 9 and 25. |
