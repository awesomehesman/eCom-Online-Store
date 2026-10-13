# DEC-0021 — Stock Availability Messaging Policy

- **Identifier:** DEC-0021
- **Title:** Stock Availability Messaging Policy
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

[`PRODUCT.md` §24 item 17](../../.ai/core/PRODUCT.md) leaves low-stock and out-of-stock customer messaging open. Its §9.2 identifies low-stock visibility as a business need, but supplies no threshold, wording, presentation, or communication policy. The [Approved Inventory Domain Specification](../domains/inventory/inventory-domain.md) REQ-INV-006–009 and [Inventory Backend Specification](../backend/inventory/inventory-backend.md) BINV-REQ-015–018 make Inventory authoritative for Stock and Available-to-Sell, require current trusted Inventory outcomes for commitment, and prohibit representing unknown or stale Stock as confirmed availability. Neither Specification selects a messaging threshold.

The [Approved CMS Domain Specification](../domains/cms/cms-domain.md) REQ-CMS-024 and [CMS Backend Specification](../backend/cms/cms-backend.md) BCMS-REQ-019 permit governed availability presentation without transferring Inventory truth to CMS. [Search REQ-SRCH-020](../domains/search/search-domain.md) preserves the same non-authoritative representation boundary. [Cart REQ-CART-014–015](../domains/cart/cart-domain.md) keeps displayed or cached availability distinct from Inventory validation and purchase eligibility. [Checkout REQ-CHK-016–018 and 021–022](../domains/checkout/checkout-domain.md) requires current Inventory revalidation and safe interruption of unsupported progression. These are binding boundaries, not an approved customer-message policy.

## Decision Question and Product Policy Scope

What initial customer-facing messaging policy should govern low-stock, out-of-stock, unavailable, unknown, stale, conflicting, and uncertain availability across applicable commerce touchpoints while preserving Inventory as the authoritative source of availability?

Product owns the customer-message policy. It may later determine which supported distinctions are useful to customers, where they may be presented, and what evidence is required before a claim is made. This record does not create a new Inventory state or define the conditions under which a state is established. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 17 remains open.

## Affected Requirements and Domain Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product policy | Owns the unresolved customer-facing messaging choice, not authoritative Stock or Available-to-Sell. |
| Inventory | Owns Stock, Stock Reservations, Available-to-Sell, current availability and uncertainty under REQ-INV-006–012 and BINV-REQ-015–019. |
| Product | Owns Product and Product Variant identity and publication; it cannot establish Stock truth under [REQ-PRD-001–003](../domains/product/product-domain.md) and [BPRD-REQ-033](../backend/product/product-backend.md). |
| CMS and storefront | May present governed Inventory evidence, but CMS content, rendering, cache state or wording cannot establish, override or correct Inventory truth. |
| Search | May show source-labelled, stale-capable Inventory representations; an index or result cannot independently calculate availability or authorize purchase under REQ-SRCH-020 and [BSRCH-REQ-020](../backend/search/search-backend.md). |
| Cart | Holds purchase intent. Cart presence does not reserve Stock; displayed availability is non-authoritative under REQ-CART-014–015 and [BCART-REQ-026–027](../backend/cart/cart-backend.md). |
| Checkout and Order | Checkout consumes current Inventory outcomes before governed commitment; messaging cannot override Checkout eligibility or establish Order acceptance under REQ-CHK-016–018, 021–022 and [Order REQ-ORD-030–031](../domains/order/order-domain.md). |
| Notifications | Owns notification requests and delivery outcomes, not Inventory facts. Its [REQ-NTF-020–023](../domains/notifications/notifications-domain.md) and [BNTF-REQ-018–020](../backend/notifications/notifications-backend.md) do not select a stock-message channel or guarantee delivery. |

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Minimal state-based messaging | Present only availability distinctions supported clearly by current governed Inventory evidence at an applicable touchpoint. | Limits unsupported claims and may make the customer message easier to understand. | May leave customers without useful context when evidence changes or is incomplete; no state mapping or copy is selected. |
| B — Contextual availability messaging | Vary the permitted explanation by governed availability context and customer journey while retaining the same Inventory source and uncertainty boundaries. | May explain a changed Cart or Checkout outcome more clearly. | Raises consistency and freshness demands across touchpoints; no context rule, threshold or commitment is selected. |
| C — Proactive availability messaging | Consider additional warnings or updates when separately authorized, current evidence and communication policy support them. | May help customers respond to material availability changes. | Can amplify false scarcity, stale claims, notification burden or privacy exposure; no channel, trigger, timing or entitlement is selected. |

All three are unselected candidates. None treats a displayed message as a Stock Reservation, replenishment promise, purchase commitment, or permission to progress Checkout. The options have not been evaluated, preferred or Accepted.

## Messaging-State Considerations

A later policy must distinguish Inventory-supported availability from Product structural sellability and other eligibility. “Available” in presentation must not imply a guarantee of purchase or continued availability. “Low-stock” requires a separately approved, evidence-based policy before any scarcity claim; this record selects no threshold or calculation. “Out-of-stock” must not imply a replenishment date, back-order right, pre-order right, or permanent unavailability.

Unavailable, insufficient, unknown, stale, missing, conflicting and uncertain Inventory outcomes must retain their applicable distinctions. They cannot be collapsed into confirmed availability or fabricated Stock facts. A changed outcome during Cart or Checkout requires an understandable, correctable presentation and governed revalidation, not preservation of an obsolete customer claim. Replenishment possibility remains uncertain until supported by separate authority and evidence. No new Inventory lifecycle state is established by these messaging labels.

## Applicable Customer Touchpoints

Product detail, Product listing, Search results, Cart and Checkout may each present an applicable governed representation, subject to source attribution, freshness, accessibility and their owning-Domain boundaries. A listing or Search result may be stale-capable; Cart intent does not reserve Stock; Checkout must obtain current Inventory evidence before commitment. Cross-touchpoint consistency means preserving the same authoritative meaning, not promising identical cached values at all times.

Post-order communication may be considered only where a separately governed Order or Inventory fact and communication policy make it applicable. A displayed pre-order message must not rewrite an accepted Order or establish a new Order outcome. This record authorizes no new Notification channel, template, recipient class, event, dispatch trigger, delivery guarantee or provider.

## Comparative Evaluation Criteria and Consequences

Product-led comparison must assess customer clarity; evidence accuracy and freshness; Inventory authority; false-scarcity and false-availability risk; customer expectations; accessibility; conversion impact using attributable evidence rather than assumptions; operational burden; security and disclosure; recovery after stale or conflicting evidence; cross-touchpoint consistency; historical traceability; and reversibility. It must compare the burden of keeping representations current against the value of added context, without weakening Checkout revalidation or Overselling controls.

Minimal messaging may reduce unsupported claims but omit useful explanation. Contextual messaging may improve comprehension while increasing consistency work. Proactive messaging may increase notice but also create communication and trust risks. These are hypotheses requiring evidence, not measured results or selected policy.

## Security, Privacy and Customer Trust

Customer-visible availability claims must be proportionate to authorized evidence and must not disclose protected Stock Location, operational, Customer, Order, or staff information. Claims of scarcity must not be manufactured to influence behavior. Unknown, stale or conflicting evidence requires a safe explanation rather than false certainty. Protected administration of content or policy remains subject to contextual Authorization, default denial and proportional Audit Records under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). This record grants no Role, Permission or exception.

## Data, Compatibility, Migration and Operational Impact

A later accepted policy may affect storefront copy, CMS content, Search Projections, Cart and Checkout explanations, accessibility review and support guidance. Existing representations would need compatibility and correction review where a changed policy or Inventory outcome makes them misleading. No data model, historical rewrite, migration, cache rule, refresh interval, service level, localization rule or operational procedure is selected here. Inventory remains the source for correcting availability truth; consumers remain responsible for correcting their presentations under their approved boundaries.

## Evidence Required Before Selection

Product and accountable merchandising owners must provide attributable customer-journey scenarios, intended message purposes, candidate distinction needs, and evidence of customer comprehension or harm. Inventory must confirm which current, uncertain and stale outcomes can be safely represented without exposing internal Stock details or selecting an unsupported threshold. CMS, Search, Cart, Checkout and UX/accessibility reviewers must assess presentation feasibility and consistency. Order and Notifications must review any proposed post-order communication boundary. Operations and Security must assess correction, abuse, privacy and support effects. No such stakeholder confirmation or comparative result is claimed complete in this record.

## Open Questions and Acceptance Prerequisites

1. Which Inventory-supported distinctions should be visible at each touchpoint, and what evidence establishes that a claim is current enough for its purpose?
2. Is a low-stock scarcity claim warranted? If so, what later Product-approved meaning and Inventory evidence would support it? No threshold is selected.
3. How should unknown, stale, unavailable, conflicting or uncertain outcomes be explained without asserting confirmed availability or inventing a new Inventory state?
4. How should changed evidence during Cart and Checkout be presented while retaining final Checkout revalidation and safe interruption?
5. Is any proactive or post-order communication justified, and what separate Notifications, consent, Order and operational authority would it require?
6. Which customer-comprehension, accessibility, operational and security evidence is sufficient to compare the alternatives?
7. Which later Specification, Contract or implementation changes would an Accepted policy require? None is authorized here.

Product leads later policy comparison and selection with Inventory, CMS, Search, Cart, Checkout, UX/accessibility, Operations and Security review; Order and Notifications participate where post-order communication is considered. Acceptance follows [`PRODUCT.md` §§24–25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires separate applicable Specification, Contract, Architecture, Security and Engineering review.

## Decision Status and Explicit Non-Authorizations

DEC-0021 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It does not select Option A, B or C; a numerical or qualitative low-stock threshold; state-to-message mapping; exact copy; localization; placement; notification channel, trigger, recipient, provider or timing guarantee; replenishment commitment; back-order or pre-order policy; reservation or allocation rule; cache, scheduler or refresh mechanism; schema, API, Event Contract or dependency. It does not alter Inventory, Product, CMS, Search, Cart, Checkout, Order or Notifications authority, accept a Product policy, close `PRODUCT.md` §24 item 17, or authorize implementation.

[DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md), [DEC-0019](DEC-0019-product-variant-attribute-standards.md) and [DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md) remain Proposed and non-authoritative. They select no taxonomy, Attribute or CMS approval policy for this record to rely on.

## References

- [AGENTS.md §5](../../.ai/core/AGENTS.md), [PRODUCT.md §§9.2, 24 item 17, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — governance, ownership, canonical terms and protection.
- [Inventory Domain REQ-INV-006–012](../domains/inventory/inventory-domain.md) and [Inventory Backend BINV-REQ-015–019](../backend/inventory/inventory-backend.md) — authoritative Stock, Available-to-Sell and uncertainty.
- [CMS Domain REQ-CMS-024](../domains/cms/cms-domain.md) and [CMS Backend BCMS-REQ-019](../backend/cms/cms-backend.md) — non-authoritative availability presentation.
- [Product Domain](../domains/product/product-domain.md) and [Product Backend](../backend/product/product-backend.md) — Product identity and Inventory non-authority.
- [Checkout Domain REQ-CHK-016–018 and 021–022](../domains/checkout/checkout-domain.md) and [Checkout Backend BCHK-REQ-016–017](../backend/checkout/checkout-backend.md) — current Inventory revalidation and coordination.
- [Search Domain REQ-SRCH-020](../domains/search/search-domain.md), [Cart Domain REQ-CART-014–015](../domains/cart/cart-domain.md), [Notifications Domain](../domains/notifications/notifications-domain.md), and their [Search](../backend/search/search-backend.md), [Cart](../backend/cart/cart-backend.md) and [Notifications](../backend/notifications/notifications-backend.md) Backend Specifications — bounded consumption and communication authority.
- [Order Domain REQ-ORD-030–031](../domains/order/order-domain.md) — separate Order and Inventory outcomes.
- [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md), [DEC-0019](DEC-0019-product-variant-attribute-standards.md) and [DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md) — related Proposed, non-authoritative decisions.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected stock-availability messaging approaches while preserving Inventory authority, uncertainty and freshness distinctions, consumer presentation boundaries, Checkout revalidation, unresolved Product item 17, and separate evidence, acceptance and implementation gates. |
