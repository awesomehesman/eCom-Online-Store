# DEC-0015 — Stock Reservation Duration Policy

- **Identifier:** DEC-0015
- **Title:** Stock Reservation Duration Policy
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

[`PRODUCT.md` §24 item 12](../../.ai/core/PRODUCT.md) leaves Stock Reservation duration open. Its §16 requires current availability for purchase and explicit Stock Reservation expiry and release. The [Approved Inventory Domain Specification](../domains/inventory/inventory-domain.md) REQ-INV-010–016 establishes Inventory-owned reservation truth, explicit lifecycle outcomes, duplicate and concurrency safety, and Overselling prevention while selecting no duration, hold timing, expiry interval, allocation model, finalization trigger, scheduler or decrement point. The [Approved Inventory Backend Specification](../backend/inventory/inventory-backend.md) BINV-REQ-019–024 preserves these constraints. [Checkout Domain REQ-CHK-017–018](../domains/checkout/checkout-domain.md) permits governed reservation coordination but assigns neither reservation authority nor duration selection to Checkout. Order and Payment remain separate owners of their respective outcomes.

This record frames the Product policy choice. Existing requirements are binding; no duration value or candidate policy is selected by this Proposed record.

## Decision Question

What Product policy should govern the duration and expiry of an Inventory-owned Stock Reservation associated with a permitted commerce operation, while preserving explicit release, consumption, uncertainty and overselling controls?

## Decision — Unselected

**NO OPTION SELECTED.** Product item 12 remains open. A duration rule, its applicable contexts, and any relationship to a permitted reservation start, release or consumption require evidence and affected-owner review before Product acceptance. This record grants no policy, Contract or implementation authority.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation and trade-off |
| --- | --- | --- | --- |
| A | A common bounded hold duration for all policy-permitted Stock Reservations. | Simpler customer communication and consistent treatment across comparable requests. | Different commerce contexts may need different treatment; a common hold could retain scarce Stock unnecessarily or interrupt legitimate Checkout. No value or start event is proposed. |
| B | Context-sensitive bounded durations under explicitly approved Product categories of commerce operation. | Can account for different customer journeys and operational needs. | Greater fairness, explanation, review and support complexity; categories and rules need Product and Inventory evidence. No category, value or trigger is selected. |
| C | A policy-defined reservation boundary tied to an authoritative commerce outcome rather than a single elapsed duration, with explicit handling for abandonment and unresolved outcomes. | May align the hold with an actual governed business outcome. | May retain Stock too long or make expiry unclear when an outcome is delayed or uncertain; requires strong coordination and recovery evidence. No outcome trigger, indefinite hold or technical mechanism is authorized. |

These are alternatives for later comparison, not feasible designs or accepted behavior. None implies that Cart presence reserves Stock or that a client clock establishes expiry. A later review may reject or refine a candidate based on evidence without silently selecting another.

## Scope and Authority Boundaries

| Concern | Governing boundary |
| --- | --- |
| Product | Owns the business duration/expiry policy, customer and fairness consequences, and any eventual approved differentiation by commerce context. |
| Inventory | Alone establishes authoritative Stock Reservation identity, state, expiry, release, consumption and Available-to-Sell effects. It preserves duplicate, concurrency, uncertainty, reconciliation and Overselling controls. |
| Checkout | May request and correlate a reservation through a governed Contract; its progression, Cart or browser state cannot create, prove, expire or release Inventory truth. |
| Order | Owns Order records and may request governed Inventory effects; an Order state alone cannot establish a reservation transition. |
| Payment | Owns Payment Authorization, Capture and provider outcomes; a Payment state alone cannot establish reservation success, expiry or release. |
| Technical design | Scheduling, clocks, persistence, locking, transaction, event and retry mechanisms require separate Architecture, Contract and Engineering governance. |

An accepted Product duration policy would still need to preserve current authoritative evidence, safe failure and explicit owner outcomes. Neither an elapsed client interval nor a missing callback proves Inventory release.

## Evaluation Criteria and Evidence Required

Formal comparison must assess: customer Checkout continuity and clear expectations; current Inventory availability and Overselling prevention; fairness when Stock is scarce; abandonment and retained-Stock effects; Payment and Order coordination without authority transfer; distinguishable expiry, release and consumption; timeout, duplicate, replay, concurrency and uncertain-outcome recovery; and support, audit and operational cost. Reversibility and migration consequences must be considered before acceptance.

Required evidence includes attributable Product goals and permitted commerce contexts; Inventory observations or credible workload evidence about scarcity, turnover, reservation occupancy and abandoned Checkout; Checkout, Order and Payment journey/failure cases; operational support and reconciliation needs; customer-facing communication implications; and Security review of abuse and denial risks. Evidence must distinguish current repository requirements from measured operation, estimates and stakeholder proposals. This record contains no measurement or stakeholder approval.

## Open Questions and Dependencies

1. Which commerce operations, if any, may request a Stock Reservation? Cart presence alone is insufficient; approval and applicable Contracts remain separate.
2. What customer and business outcomes should a future duration policy optimize, and what evidence can distinguish the alternatives?
3. How should the policy account for scarce Stock, abandonment, interrupted Checkout and delayed or uncertain Payment or Order outcomes without creating an indefinite hold or fabricated release?
4. What, if any, differentiated treatment is justified across permitted contexts, and how would fairness and customer communication be assessed?
5. Which start, expiry, release and consumption semantics would an accepted policy require? No binding trigger, clock rule, grace period or renewal policy is chosen here.
6. Which existing or new Contract, Architecture, operational and migration reviews would be needed after policy selection? This record changes none.

## Consequences, Risks and Reversibility

Any eventual policy trades customer time to complete a purchase against Stock availability for other customers. A longer hold can constrain sellable availability; a shorter or poorly explained hold can interrupt Checkout. Differentiated or outcome-based rules may increase fairness-review and operational complexity. Unknown Inventory effects, duplicate processing and concurrent demand must remain safe under every alternative. Before acceptance, reviewers must examine how a later policy revision would treat existing reservations and historical outcomes without silently rewriting Inventory truth. No risk ranking or numerical result is claimed without evidence.

## Security Impact

Reservation requests and administrative adjustments remain subject to trusted, contextual Authorization, default denial, abuse protection, least privilege and audit under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). A proposed duration must not permit hoarding, client-driven expiry or unauthorized release. No abuse threshold or security mechanism is selected.

## Data Impact

Inventory retains authoritative reservation and Available-to-Sell evidence; Checkout, Order, Payment, client state and Projections cannot replace it. An accepted policy would require traceable applicable policy and lifecycle outcomes, but this record selects no field, schema, identifier, retention period or event.

## Compatibility and Migration Impact

No current API, Event Contract, reservation, Order or Payment record changes. A future policy needs review of compatibility, active reservations, historical evidence and recovery before any migration; none is authorized here.

## Operational Impact

Expiry, release, abandoned requests, reconciliation and support explanations need a governable operating model. This record sets no scheduler, retry interval, alert, support procedure, staffing assumption, SLA or SLO.

## Evidence, Review and Acceptance Gates

Product and the accountable business owner must review the customer and commercial policy. Inventory must validate authority, availability, fairness, concurrency and reconciliation consequences; Checkout, Order and Payment must review their coordination and uncertainty boundaries. Operations and Security must review abandonment, abuse, support and recovery. Finance and Legal review is required where a proposed treatment materially affects commercial commitments or customer rights. Applicable Architecture, Contract and Engineering owners review later technical changes within their own authority.

Creation of this Proposed record authorizes neither formal alternative evaluation nor policy selection. A separately governed evidence and review step must precede selection. Acceptance requires Product authority and the applicable affected-owner review under [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires an Accepted policy and separate Contract, Security, Architecture where material, and Engineering gates.

## Explicit Non-Authorizations

This record does not select a preferred alternative, duration, numerical limit, start or expiry trigger, grace period, renewal rule, reservation eligibility, Cart-based hold, allocation or decrement policy. It does not establish a Stock Reservation from Checkout, Order or Payment state; weaken currentness, idempotency, concurrency, Overselling or uncertainty controls; create or change an API or Event Contract; select a scheduler, clock, schema, persistence model, transaction, lock, queue, provider or dependency; authorize implementation; or close `PRODUCT.md` §24 item 12.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§16, 24 item 12, 25 and 38](../../.ai/core/PRODUCT.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), and [GLOSSARY.md](../../.ai/core/GLOSSARY.md) — governance, ownership and canonical Stock Reservation terminology.
- [Inventory Domain REQ-INV-008–016 and 022–025](../domains/inventory/inventory-domain.md) and [Inventory Backend BINV-REQ-017–024](../backend/inventory/inventory-backend.md) — authoritative availability and reservation safety.
- [Checkout Domain REQ-CHK-017–018](../domains/checkout/checkout-domain.md) and [Checkout Backend](../backend/checkout/checkout-backend.md) — bounded reservation coordination.
- [Order Domain REQ-ORD-007 and 030](../domains/order/order-domain.md), [Order Backend](../backend/order/order-backend.md), [Payment Domain REQ-PAY-014–016](../domains/payment/payment-domain.md), and [Payment Backend](../backend/payment/payment-backend.md) — separate Order and Payment authority.
- [ADR-0005](../adr/ADR-0005-post-product-backend-specification.md), [ADR-0011](../adr/ADR-0011-post-checkout-backend-specification.md), and [ADR-0012](../adr/ADR-0012-post-order-backend-specification.md) — Accepted backend sequencing and ownership boundaries, not duration policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed the unresolved Product-owned Stock Reservation duration question, unselected policy alternatives, evaluation evidence and owner reviews while preserving Inventory authority, Checkout/Order/Payment separation, uncertainty and Overselling controls, and separate acceptance and implementation gates. |
