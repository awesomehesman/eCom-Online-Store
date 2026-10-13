# DEC-0035 — South African Tax, Invoice and Credit Note Policy

- **Identifier:** DEC-0035
- **Title:** South African Tax, Invoice and Credit Note Policy
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

[`PRODUCT.md` §24 item 25](../../.ai/core/PRODUCT.md) leaves “South African tax-display, invoice, and credit-note policy” unresolved. Product §16.10 requires customer-visible prices to follow an approved South African tax-display policy, Order and invoice records to preserve applicable tax values at confirmation, and tax, invoice-numbering and document requirements to be defined before production launch. Client-side tax or invoice totals are not authoritative. These are binding constraints, not a selected tax treatment, document rule or legal interpretation.

[DEC-0031](DEC-0031-initial-tax-display-invoice-requirements-policy.md) separately frames §24 item 9, “Tax-inclusive display and invoice requirements,” through three unselected approaches to initial customer-facing presentation. It expressly leaves item 25, Credit Note eligibility and issuance, corrections, Refund-related tax treatment and accounting implications unresolved. No qualified South African Finance/Tax or Legal interpretation, document policy, stakeholder approval or implementation authority is established by either Proposed record.

Approved [Pricing REQ-PRC-013](../domains/pricing/pricing-domain.md) and [BPRC-REQ-023](../backend/pricing/pricing-backend.md) require qualified Approved tax policy and governed inputs before tax-related calculation or presentation. [Checkout REQ-CHK-037](../domains/checkout/checkout-domain.md), [Order REQ-ORD-023 and 025](../domains/order/order-domain.md), [Payment REQ-PAY-044](../domains/payment/payment-domain.md), [Return REQ-RET-035](../domains/return/return-domain.md) and [Reporting REQ-RPT-031](../domains/reporting/reporting-domain.md) preserve separate commercial, historical, transaction, Return and derived-reporting authority. None selects Invoice or Credit Note policy.

## Decision Question and Status

What South African tax, Invoice, Credit Note and correction policy should govern the initial commerce offering, consistently with DEC-0031 and applicable qualified Finance/Tax and Legal requirements?

**NO ALTERNATIVE SELECTED.** This record documents the remaining Product question and its dependencies only. It does not authorize comparative evaluation, resolve `PRODUCT.md` §24 item 25 or establish a legally compliant policy.

## Alternatives Considered — Not Formally Compared

| Option | Conditional policy approach | Potential benefit | Limitation, risk and required evidence |
| --- | --- | --- | --- |
| A — Coordinated document policy | Consider a single governed policy framework for initial Invoice, Credit Note, correction and Refund-document relationships, with context-specific rules only where qualified requirements demand them. | Could give customers and Operations one coherent explanation and reconciliation model. | A common framework may obscure legally or commercially distinct cases; qualified obligations, transaction-context inventory, document relationships and affected-owner feasibility must be established. |
| B — Context-specific document policy | Consider explicitly differentiated rules for materially distinct sale, correction, reversal, Return and Refund contexts, reconciled under one Product authority. | Could make differences in evidence and customer communication explicit. | More policy and operational complexity; Finance/Tax and Legal must validate each distinction, required documents, access, historical integrity and reconciliation. No context is classified here. |
| C — Bounded initial policy scope | Consider limiting initial commerce contexts to a scope for which complete, qualified Invoice, Credit Note and correction requirements can be approved, with other contexts addressed through later governance. | Could avoid unsupported document behavior for contexts not ready for launch. | Any exclusion or deferral must itself be lawful and Product-approved; mandatory customer or tax obligations cannot be deferred. Scope, customer impact, Operations, reversibility and later migration require evidence. |

All three approaches are **unselected, unranked, non-authoritative and conditional on qualified evidence**. They describe policy scope and operational consequences, not alternative ways to evade mandatory obligations. None establishes an issuance trigger, document format, tax treatment or implementation.

## DEC-0031 Boundary and Reconciliation

DEC-0031 governs the unresolved initial tax-display and invoice-requirements question in `PRODUCT.md` §24 item 9. DEC-0035 concerns the distinct broader item 25 question, especially Credit Notes, correction and Refund-document relationships; it **does not supersede DEC-0031**. Shared tax-display, Invoice, customer-disclosure and historical-evidence terminology must be reconciled before either policy is accepted. Neither Proposed record is authoritative tax or Invoice policy. Any later acceptance affecting both requires coordinated Product governance, qualified Finance/Tax and Legal validation, and synchronization with canonical Product guidance. No second or competing Invoice authority is created.

## Ownership and Approved Safeguards

| Authority | Boundary preserved |
| --- | --- |
| Product | Owns customer-facing commercial and document policy, subject to qualified validation; no policy is selected here. |
| Finance/Tax and Legal | Provide qualified jurisdiction-specific tax, accounting and legal interpretation and review; no conclusion or approval is attributed to them. |
| Pricing | Retains authoritative governed commercial calculation only from qualified Approved policy and inputs; it cannot independently choose Tax Inclusive or Tax Exclusive treatment or document rules. |
| Checkout | Coordinates current authoritative commercial revalidation and may carry governed evidence, without defining tax or document policy. |
| Order | Retains confirmed historical commercial evidence without recalculating Tax or deriving Invoice or Credit Note policy from history. |
| Payment | Retains Payment and Refund transaction truth without defining Tax treatment or financial-document rules. |
| Return | Retains Return lifecycle and assessment without calculating tax adjustments or assigning Credit Note treatment. |
| Reporting | Consumes governed evidence as derived reporting, not source truth or legal-document authority. |
| Security and Privacy | Preserve commercial integrity, contextual Authorization, tamper resistance, protected personal data and lawful access and retention controls where applicable. |

The matching [Pricing](../backend/pricing/pricing-backend.md), [Checkout](../backend/checkout/checkout-backend.md), [Order](../backend/order/order-backend.md), [Payment](../backend/payment/payment-backend.md), [Return](../backend/return/return-backend.md) and [Reporting](../backend/reporting/reporting-backend.md) Backend Specifications preserve these boundaries. This record creates no Tax, Invoice or Credit Note Domain and assigns no document issuance, numbering, adjustment or accounting ownership. Any such assignment requires separate approved authority.

## Evidence Required Before Evaluation or Selection

Attributable qualified Finance/Tax and Legal evidence must establish applicable South African obligations and their interpretation for the intended commerce contexts; Invoice and Credit Note eligibility and issuance conditions; required fields and numbering; corrections, reversals and Refund relationships; and any customer disclosure or access obligations. This record states questions, not answers, and does not interpret legislation.

Affected owners must establish the authoritative sources and preservation of historical financial evidence; auditability and reconciliation needs; Customer and Staff User access and privacy boundaries; retention obligations; operational support and correction handling; affected-domain Contract implications; and alignment with DEC-0031. Evidence should distinguish confirmed requirements from assumptions, unresolved questions and implementation details. No research, validation, review or sign-off is claimed complete.

## Consequences, Risks and Operational Impact

Making item 25 explicit can prevent an Invoice or Credit Note rule from emerging accidentally through Pricing, Checkout, Order, Payment, Return or Reporting implementation. A later qualified policy could improve customer clarity, financial-document consistency and auditability. It may also impose document handling, correction, customer-service, reconciliation, rights-management and migration costs. Inconsistent records, unsupported legal claims, incorrect tax adjustments, privacy exposure and historically rewritten evidence are material risks. Reversibility depends on lawful correction of issued documents and preserved history, not merely changing a display rule. No operational process, provider, platform or correction mechanism is selected.

## Reviewers and Future Governance Gates

Product leads the question. Finance/Tax and Legal must validate applicable requirements. Pricing, Checkout, Order, Payment, Return, Reporting, Security, Privacy, Architecture, Engineering and Operations review their affected boundaries. Listing reviewers does not assert confirmation or approval.

1. **Initial Proposed drafting:** This record frames the question only.
2. **Comparative evaluation:** Obtain separate authorization and sufficient attributable evidence before comparing approaches.
3. **Qualified validation:** Obtain Finance/Tax and Legal interpretation of applicable obligations, documents and permissible scope.
4. **Product policy acceptance:** Reconcile DEC-0031 and item 25, obtain affected-owner review and formally accept any selected policy.
5. **Affected-owner and Architecture review:** Govern any material ownership, provider, integration or technical choices separately.
6. **Specification and Contract synchronization:** Update canonical Product guidance and affected approved sources through their own governance before changed behavior is binding.
7. **Implementation and rollout:** Verify compliance, historical integrity, security, operational readiness and release authority separately.

Completion of initial drafting does not authorize any downstream gate.

## Validation Criteria

This record remains valid as an initial proposal only if: (1) it traces the exact `PRODUCT.md` §24 item 25; (2) DEC-0031 and item 9 remain separately Proposed and unresolved; (3) exactly three approaches remain unselected and unranked; (4) no South African legal or tax interpretation or document rule is asserted; (5) Product, qualified Finance/Tax and Legal, and affected-domain boundaries remain intact; (6) no Tax, Invoice or Credit Note Domain or document owner is invented; and (7) evaluation, acceptance, source synchronization and implementation remain separate unauthorized gates.

## Decision Status and Explicit Non-Decisions

DEC-0035 remains **0.1.0 Proposed, Product-owned and non-authoritative**. It supersedes nothing and is superseded by nothing. No Tax rate, category, exemption, formula, Tax Inclusive or Tax Exclusive presentation, Invoice or Credit Note issuance rule, numbering scheme, document content, correction or adjustment mechanism, Refund-related tax treatment, accounting treatment, retention period, legal interpretation, provider, platform, schema, API, Event, Contract, Architecture change or implementation is selected or approved. DEC-0031, `PRODUCT.md`, Approved Specifications and Contracts are unchanged. No comparative evaluation or rollout is authorized.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16.10 and 24 items 9 and 25](../../.ai/core/PRODUCT.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product, decision, commercial and security authority.
- [DEC-0031](DEC-0031-initial-tax-display-invoice-requirements-policy.md) — related, non-superseded Proposed item 9 record and required reconciliation boundary.
- [Pricing REQ-PRC-013](../domains/pricing/pricing-domain.md) and [BPRC-REQ-023](../backend/pricing/pricing-backend.md); [Checkout REQ-CHK-037](../domains/checkout/checkout-domain.md) and [BCHK-REQ-037](../backend/checkout/checkout-backend.md) — qualified calculation, presentation and revalidation boundaries.
- [Order REQ-ORD-023 and 025](../domains/order/order-domain.md) and [BORD-REQ-023 and 025](../backend/order/order-backend.md); [Payment REQ-PAY-044](../domains/payment/payment-domain.md) and [BPAY-REQ-046](../backend/payment/payment-backend.md) — historical and transaction evidence without document-policy authority.
- [Return REQ-RET-035](../domains/return/return-domain.md) and [BRET-REQ-025](../backend/return/return-backend.md); [Reporting REQ-RPT-031](../domains/reporting/reporting-domain.md) and [BRPT-REQ-031](../backend/reporting/reporting-backend.md) — Return and derived-reporting non-authority.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initial non-authoritative framing of the South African tax, Invoice, Credit Note and correction policy question. |
