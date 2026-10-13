# DEC-0014 — Order Cancellation Eligibility and Cutoff Policy

- **Identifier:** DEC-0014
- **Title:** Order Cancellation Eligibility and Cutoff Policy
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

[`PRODUCT.md` §24 item 10](../../.ai/core/PRODUCT.md) leaves Order cancellation eligibility and cutoff policy open. Its §§16.3, 16.11 and 17 require distinct Payment outcomes, explicit cancellation and Return eligibility, owner-controlled lifecycle transitions and unambiguous outcomes. The [Approved Order Domain Specification](../domains/order/order-domain.md) REQ-ORD-037–039 gives Order cancellation coordination and canonical `Cancelled` meaning without choosing an eligibility rule or cutoff. Order REQ-ORD-042 requires trusted contextual Authorization. Its [backend specialization](../backend/order/order-backend.md) BORD-REQ-037–038 preserves those boundaries.

Payment owns Payment Authorization, Capture, Void and Refund outcomes, Shipping owns Shipment and Dispatch evidence, and Return owns Return eligibility and processing. A cancellation request alone establishes none of those effects. The Product sponsor prefers customer-requested cancellation before confirmed Dispatch, including after fulfilment begins, and a separately governed Return path after Dispatch. That preference is Product direction for review, not binding policy or evidence of affected-owner approval.

## Decision Question

At what authoritative Order, Payment and Shipping/Fulfilment context may a properly authorized customer request Order cancellation, what event ends that customer-facing option, and what separately owned effects must remain uncertain until confirmed?

## Decision — Proposed Product Direction Only

**Option C is Product-preferred, not Accepted.** The sponsor prefers a cancellation request option before confirmed Dispatch, including where fulfilment has begun, subject to eligibility and safe coordination. After confirmed Dispatch, the customer-facing Order cancellation option would be unavailable; the customer would instead be directed to the Return process, subject to its independently governed eligibility, approval and outcomes. This direction neither promises that every dispatched Order is returnable nor establishes a Refund entitlement.

`PRODUCT.md` §24 item 10 remains open. No alternative has been formally evaluated, selected as binding policy or Accepted. Affected-owner evidence and reviews remain outstanding, and this record authorizes no implementation.

## Alternatives Considered — Not Formally Compared

| Option | Proposed policy boundary | Potential benefit | Unresolved consequence |
| --- | --- | --- | --- |
| A | Customer cancellation request before confirmed Payment Capture. | Aligns the request boundary with one financial milestone. | Capture does not establish fulfilment or Dispatch state; Payment must review the effect of uncaptured, captured and uncertain outcomes. |
| B | Customer cancellation request before fulfilment begins. | Avoids accepting requests once fulfilment work is underway. | The meaning and authoritative observation of fulfilment start need Shipping/Fulfilment review; the boundary may exclude recoverable in-progress work. |
| C | Customer cancellation request before confirmed Dispatch, including after fulfilment begins where otherwise eligible. | Matches the sponsor's intended customer experience. | In-progress fulfilment, dispatch races, financial consequences and uncertain downstream effects require owner review and evidence. Preferred for later evaluation only. |

The alternatives are policy candidates, not tested feasibility findings. No cancellation window, clock cutoff, lifecycle transition or service guarantee is selected.

## Proposed Eligibility and Cutoff Semantics

Under preferred Option C, **confirmed Dispatch is the proposed cutoff event**, not a timestamp inferred from a customer screen, carrier message or untrusted request. Shipping/Fulfilment must identify the authoritative Dispatch evidence and its relationship to Order's current context before a rule can be accepted. A request received while fulfilment is in progress may be considered only if the eventual policy permits it and current authoritative evidence supports eligibility; it cannot itself stop fulfilment or prove cancellation.

A cancellation request, its acceptance for processing, confirmed Order cancellation and any Payment, Inventory, Shipment, Return or communication effects remain distinct. Order owns coordination and canonical Order history, but cannot assert a downstream effect before its owner confirms it. Concurrent cancellation and Dispatch, delayed observations, stale state, duplicates and uncertain outcomes need explicit review; this proposal chooses no ordering, locking, retry, transaction or reconciliation mechanism. No new Order status or mandatory transition is defined.

## Customer Access and Domain Boundaries

The proposed customer-facing request option exists only where the eventual policy and trusted, current Order context establish eligibility and the requester passes contextual server-side Authorization. A visible control, Order Number, contact address, payment artifact or submitted customer assertion is not proof of ownership. After authoritative confirmed Dispatch, preferred Option C would withhold that cancellation option and direct the customer toward a separately governed Return path; Return may still reject the request. [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) remains Proposed, so this record does not assume an Accepted guest Checkout or guest-Order access policy.

| Owner | Existing authority preserved |
| --- | --- |
| Order | Coordinates authorized cancellation and owns canonical Order state, history, duplicate safety and explicit uncertainty. |
| Payment | Owns provider-backed Payment outcomes, Void of an uncaptured Payment Authorization where applicable, and governed Refund execution; a cancellation request guarantees neither Void nor Refund. |
| Shipping/Fulfilment | Owns Shipment, fulfilment and Dispatch evidence and any authorized stop outcome; a stop request is not a confirmed stop. |
| Return | Owns Return eligibility and process; Order cancellation, Return cancellation, Return and Refund remain distinct. |
| Checkout and Customer | Preserve permitted purchase context and Customer-local truth without establishing Order cancellation, guest entitlement or another owner's outcome. |

Applicable idempotency, audit, privacy, default-denial, stale-evidence and uncertain-outcome requirements remain binding. This proposal creates no financial reversal, stock-release, shipment-stop or customer-notification guarantee.

## Evaluation Criteria and Required Evidence

Formal evaluation must examine customer experience for eligible, ineligible and uncertain requests; operational ability to identify current Dispatch and in-progress fulfilment; preservation of Order, Payment, Shipping and Return ownership; financial correctness across Authorization, Capture, Void and possible Refund; trusted requester authorization and privacy; concurrent cancellation/Dispatch and duplicate-effect safety; Contract and support implications; and separate Return-policy dependencies. It must distinguish confirmed owner facts from Product preference, illustrative flows and implementation assumptions.

Product and Order must define and review the candidate business eligibility cases. Shipping/Fulfilment must establish authoritative Dispatch and in-progress evidence and assess whether the preferred boundary is operationally supportable. Payment must assess captured, uncaptured and uncertain financial cases without promising a Void or Refund. Return must review post-dispatch routing without treating it as Return approval. Checkout, Customer and Security must review authenticated and any future guest requester boundaries. Applicable Finance, Legal and Privacy stakeholders must review financial, consumer-facing and data consequences. No such review is represented as completed here.

## Open Questions

1. Which current Order conditions and item/fulfilment contexts permit or deny a cancellation request under each alternative, including partial or mixed states? No answer is selected here.
2. Which Shipping-owned evidence authoritatively confirms Dispatch, and how should an unavailable, delayed or conflicting observation affect a request?
3. What is the policy result when a cancellation request races with Dispatch or fulfilment work, and which facts must be confirmed before Order may record cancellation?
4. Which Payment states require separate Void, Refund or other governed financial review, and which unresolved Return/Refund policies prevent an entitlement claim?
5. How will authorized registered-customer and any separately governed guest requester prove entitlement to the exact Order without relying on an Order identifier or contact detail alone?
6. Which customer-facing disclosures, denial reasons, support paths and Contract changes would the accepted policy require? No endpoint, DTO or executable behavior is defined here.
7. What Product, Order, Checkout, Payment, Shipping/Fulfilment, Return, Customer, Security and applicable Finance, Legal and Privacy evidence and approvals are needed before acceptance?

## Consequences and Dependencies

If Option C is later Accepted, Product guidance must state the approved eligibility and Dispatch cutoff. Allowing requests during fulfilment may improve customer flexibility while increasing coordination and race-handling complexity; no operational or financial benefit is asserted without evidence. Order, Shipping, Payment and Return retain independent authority and may need governed Specification or Contract changes after Product policy is settled. `PRODUCT.md` §24 item 11, covering Returns, exchanges and Refund policy, remains separately open. DEC-0013 remains Proposed, so this decision cannot resolve guest access by implication.

## Security Impact

Protected cancellation requests require trusted server-side contextual Authorization, least privilege, default denial, safe failure and auditable material actions under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) §§7, 12 and 27. No role, guest verification method, privilege exception or security-sensitive interface is selected.

## Data Impact

Order history must distinguish a request, processing state, confirmed Order outcome and separately confirmed owner effects. This record selects no schema, event, persistence or retention mechanism and changes no historical Order Snapshot or Payment evidence.

## Compatibility and Migration Impact

No existing Order, Payment, Shipping, Return, Checkout or Customer Contract changes now. Any future customer-facing or cross-domain Contract change requires separate affected-owner review; existing Orders and uncertain external effects cannot be silently reclassified by this proposal.

## Operational Impact

In-progress fulfilment, dispatch races, provider uncertainty, duplicate requests and support handoffs require evidence before policy acceptance. This record sets no operational procedure, staffing rule, deadline, cancellation window, numerical threshold or SLA.

## Decision and Acceptance Gates

Creation of this Proposed record is authorized. **Formal alternative evaluation is not authorized by this instruction.** A later evaluation needs separately approved scope and attributable evidence. Selection and acceptance require Product authority, affected-owner review and applicable Security, Finance, Legal and Privacy review, recorded under [DECISIONS.md](../../.ai/core/DECISIONS.md) §§8–17 and [`PRODUCT.md` §38](../../.ai/core/PRODUCT.md). An Accepted policy must be synchronized into canonical Product guidance. Production implementation remains a separate gate requiring the Accepted policy and applicable Specification, Contract, Security and Engineering approvals.

## Explicit Non-Authorizations

This Proposed record does not select or accept a cancellation alternative; close Product item 10 or item 11; resolve DEC-0013; authorize customer cancellation, Return, Void, Refund, shipment stop, stock release or notification; guarantee any downstream effect; or authorize implementation. It creates no endpoint, method, DTO, OpenAPI behavior, status transition, schema, provider, dependency, transaction, locking, retry, event, timestamp, threshold, cancellation window or SLA. It does not grant guest-order access or override Order, Payment, Shipping, Return, Customer, Checkout or Security authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [DECISIONS.md §§8–17](../../.ai/core/DECISIONS.md), and [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) — decision and documentation governance.
- [PRODUCT.md §§7.6, 16.3, 16.11, 17, 24 items 10–11 and 38](../../.ai/core/PRODUCT.md) — authorized support scope, lifecycle, open policy and approval.
- [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [SECURITY-STANDARDS.md §§7, 12 and 27](../../.ai/core/SECURITY-STANDARDS.md), and [GLOSSARY.md](../../.ai/core/GLOSSARY.md) — ownership, authorization and distinct lifecycle terminology.
- [Order Domain REQ-ORD-037–039 and 042](../domains/order/order-domain.md) and [Order Backend BORD-REQ-037–038](../backend/order/order-backend.md).
- [Payment Domain REQ-PAY-014–016 and 034–035](../domains/payment/payment-domain.md) and [Payment Backend](../backend/payment/payment-backend.md).
- [Shipping Domain REQ-SHP-019 and 031](../domains/shipping/shipping-domain.md) and [Shipping Backend](../backend/shipping/shipping-backend.md).
- [Return Domain REQ-RET-027–029 and 037](../domains/return/return-domain.md) and [Return Backend](../backend/return/return-backend.md).
- [Checkout Domain REQ-CHK-010 and 012](../domains/checkout/checkout-domain.md), [Checkout Backend](../backend/checkout/checkout-backend.md), [Customer Domain REQ-CUS-039](../domains/customer/customer-domain.md), and [Customer Backend](../backend/customer/customer-backend.md).
- [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) — Proposed guest Checkout policy, not Accepted cancellation or guest-access authority.
- [ADR-0011](../adr/ADR-0011-post-checkout-backend-specification.md), [ADR-0012](../adr/ADR-0012-post-order-backend-specification.md), [ADR-0013](../adr/ADR-0013-post-shipping-backend-specification.md), [ADR-0014](../adr/ADR-0014-post-payment-backend-specification.md), and [ADR-0015](../adr/ADR-0015-post-return-backend-specification.md) — relevant Accepted backend sequencing boundaries; none selects Product item 10.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed Order cancellation eligibility and cutoff alternatives, recorded Option C as the Product sponsor's non-binding preference for requests before confirmed Dispatch with separately governed post-dispatch Return routing, and preserved owner reviews, unresolved policy and separate acceptance and implementation gates. |
