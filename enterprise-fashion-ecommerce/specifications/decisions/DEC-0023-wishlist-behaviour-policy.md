# DEC-0023 — Wishlist Behaviour for Guest and Registered Customers

- **Identifier:** DEC-0023
- **Title:** Wishlist Behaviour for Guest and Registered Customers
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

[`PRODUCT.md` §24 item 16](../../.ai/core/PRODUCT.md) leaves Wishlist behaviour for guests and registered customers open. Its §4.1 describes Account and Wishlist capabilities only where applicable; it does not approve an initial Wishlist feature. The [Approved Customer Domain Specification](../domains/customer/customer-domain.md) REQ-CUS-022–023 and [Customer Backend Specification](../backend/customer/customer-backend.md) BCUS-REQ-029 make Customer ownership of Customer-associated saved interest conditional on Approved Product policy. They leave guest behaviour, limits, sharing, alerts, deduplication and persistence duration unresolved and prohibit Wishlist from establishing Product, Price, Stock or purchase truth.

The [Approved Identity Domain Specification](../domains/identity/identity-domain.md) distinguishes a trusted Principal from Customer business truth under REQ-IDN-005, 010, 020 and 028; the [Identity Backend Specification](../backend/identity/identity-backend.md) preserves that boundary. A Visitor is unauthenticated under the [Glossary](../../.ai/core/GLOSSARY.md). [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) remains Proposed: its guest Checkout preference is neither an accepted guest identity policy nor authority for guest Wishlist persistence or association.

## Decision Question and Product Policy Scope

What initial Product policy should govern saved-interest behaviour for guests and registered customers while preserving Customer-owned Wishlist associations and the authority of Identity, Product, Pricing, Inventory, Cart and Checkout?

Product owns the choice of whether and for whom Wishlist behaviour is enabled. If later enabled under Approved policy, Customer owns Customer-side saved-interest association. This record does not enable a feature, define guest state, or assume that a saved reference remains purchasable. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 16 remains open.

## Affected Requirements and Domain Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product policy | Determines later Wishlist enablement and permitted guest and registered-customer behaviour; this Proposed record makes no selection. |
| Customer | Owns Customer association and Customer-side saved interest only where Approved Product policy enables it, under REQ-CUS-022–023 and BCUS-REQ-029; it cannot infer guest identity. |
| Identity | Owns Authentication, trusted Principal and Session security under REQ-IDN-005, 010, 020 and 028. A guest device, client assertion or saved list cannot establish an authenticated Principal or Customer identity. |
| Product | Owns Product and Product Variant identity, publication and structural sellability under [REQ-PRD-001–003, 008 and 039](../domains/product/product-domain.md) and [BPRD-REQ-012, 014 and 021](../backend/product/product-backend.md); a Wishlist reference cannot publish or validate a Product. |
| Pricing and Inventory | Own current Price and Available-to-Sell respectively under [Pricing REQ-PRC-002–003](../domains/pricing/pricing-domain.md) and [Inventory REQ-INV-006–009](../domains/inventory/inventory-domain.md). Wishlist neither copies their authority nor reserves Stock. |
| Cart and Checkout | Cart owns purchase intent, while Checkout coordinates current revalidation under [REQ-CART-013–018](../domains/cart/cart-domain.md) and [REQ-CHK-007–008, 013 and 016](../domains/checkout/checkout-domain.md). Saved interest is neither Cart intent nor Checkout eligibility. |
| Search, Notifications and Administration | Search may consume governed Product evidence; Notifications owns communication outcomes; Administration coordinates only authorized Customer operations. None gains Wishlist, Product or Inventory authority from a proposed display or alert. |

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Registered-customer-only Wishlist | Consider saved interest only for appropriately identified registered customers under later Approved Product policy. | May keep association and access review within a governed Customer context. | Excludes guest saved interest and may create sign-in friction; no feature or access grant is selected. |
| B — Guest-limited saved interest | Consider bounded guest saved interest without assuming durable identity, persistence or cross-device continuity. | May let Visitors revisit an interest during a journey if later authorized. | Ownership, recovery, privacy and abuse risks remain unresolved; no guest identifier or storage mechanism is selected. |
| C — Conditional guest-to-account continuity | Consider whether guest interest could later be carried into an identified Customer context after separately governed identity, consent where applicable, association and reconciliation review. | May preserve customer intent across a later permitted transition. | Incorrect linkage, duplicates and unauthorized disclosure are material risks; no merge rule or transition is selected. |

All alternatives remain unselected and unranked. None enables Wishlist for launch, makes Registration or Account creation a purchase prerequisite, or accepts DEC-0013's guest Checkout policy.

## Saved-Interest Meaning and Current Source Facts

A Wishlist association records only governed saved interest. It is distinct from Product publication, Product Variant identity, current Price, Stock, Available-to-Sell, Stock Reservation, Cart intent, Checkout eligibility and Order acceptance. Under REQ-CUS-023, a removed, unpublished, changed or unavailable Product reference must yield a governed non-authoritative outcome; old saved-interest state cannot preserve obsolete commercial truth. Applicable use points must obtain current Product, Pricing and Inventory evidence and follow Cart and Checkout revalidation where the customer proceeds toward purchase.

Duplicate, stale, mismatched, unavailable or removed references require distinguishable treatment and safe recovery under a future approved policy. This proposal does not select deduplication semantics, a retention period, a revalidation interval, a substitute Product, automatic Cart addition or a notification. A saved item cannot promise replenishment, Price, availability or purchase success.

## Guest, Customer and Identity Boundaries

A Visitor's guest state is not an authenticated Customer, Account, Identity, Principal or Session. A registered Customer context still requires current trusted Authentication and owning-domain Authorization for protected saved-interest access or change. A visible Wishlist, browser state, contact address, Product reference or prior activity proves neither identity nor entitlement. [Customer REQ-CUS-039](../domains/customer/customer-domain.md), [Identity REQ-IDN-028](../domains/identity/identity-domain.md) and [SECURITY-STANDARDS.md §12](../../.ai/core/SECURITY-STANDARDS.md) retain contextual access and default-denial boundaries.

Potential cross-device continuity is an open Product question, not an assumed property of registered or guest state. Guest-to-account continuity, if considered later, needs separate approved guest/account identity, association, conflict, duplicate, privacy and reconciliation rules. DEC-0013 is Proposed and cannot supply those rules. No automatic guest-to-Customer, guest-to-Account or guest-to-Order linkage is authorized.

## Privacy, Sharing, Alerts and Communications

Saved interest may reveal Customer preference or intent and therefore needs purpose limitation, minimization, access control, retention and deletion review under [SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md) and applicable [Customer privacy requirements](../domains/customer/customer-domain.md). Sharing, public access, alerts, price-drop or availability messages, marketing use and Notification delivery are separately governed possibilities. A Wishlist action is not Consent or a Notification Preference under REQ-CUS-021 and [Notifications REQ-NTF-013 and 021](../domains/notifications/notifications-domain.md). No recipient, channel, template, provider, trigger or guarantee is selected.

## Comparative Evaluation Criteria and Consequences

Product-led comparison must consider customer value, usability and accessibility; privacy and minimization; identity assurance and object isolation; Product, Pricing and Inventory authority; duplicate and stale-reference integrity; recovery and reconciliation; cross-device expectations; operational burden; security and abuse risk; and reversibility. It must assess a registered-customer-only path without assuming a production Login implementation, a guest-limited path without inventing durable guest identity, and a continuity path without treating later Registration as proof of ownership of prior guest state.

Registered-only behaviour may simplify association but limit guest utility. Guest-limited behaviour may serve Visitors while increasing privacy and recovery uncertainty. Conditional continuity may reduce repeated action but risks incorrect association or disclosure. These are hypotheses, not comparative findings or stakeholder approvals.

## Data, Compatibility, Migration and Operational Impact

A later Accepted policy could affect Customer association, Product-reference validation, storefront presentation, protected access, support, data-subject handling and historical correction. Existing Cart, Product, Pricing, Inventory and Checkout state must not be rewritten by a saved-interest transition. No guest persistence, client storage, synchronization, schema, identifier, merge algorithm, API, Event Contract, retention period, migration plan or operational service target is chosen here. Any future continuity for earlier saved interest would need separate compatibility and historical-integrity review.

## Evidence Required Before Selection

Product must provide attributable guest and registered-customer saved-interest scenarios, intended value, required continuity, removal handling, and explicit exclusions. Customer and Identity reviewers must assess association, current authentication, protected access, guest uncertainty and reconciliation. Product, Pricing, Inventory, Cart and Checkout owners must confirm that saved interest cannot become authoritative commercial or purchase evidence. UX/accessibility must assess comprehensible stale and unavailable outcomes. Security and Privacy/Legal must assess object isolation, misuse, minimization and retention. Search, Notifications and Administration review applies if discovery, alerts or support are proposed. No such confirmation is claimed complete.

## Open Questions and Acceptance Prerequisites

1. Should Wishlist exist for registered customers, guests, both, or neither in the initial scope? No option is selected.
2. If a guest path is considered, what evidence, purpose and safety constraints would permit saved interest without inventing authenticated identity or durable persistence?
3. Would any cross-device or guest-to-account continuity be justified, and which separately approved association and reconciliation rules would it require?
4. How should duplicates and removed, unpublished, stale, unavailable or changed Product references be shown without treating them as current Price or availability?
5. What purpose, protected audience, retention and deletion rules would apply to saved interest, including any guest data?
6. Are sharing or alerts wanted at all, and what separate Product, Customer, Consent and Notifications policy would each require?
7. Which Specification, Contract, Architecture, Security and migration changes would follow an Accepted policy? None is authorized here.

Product leads later comparison and acceptance with Customer, Identity, Product, Pricing, Inventory, Cart, Checkout, UX/accessibility, Security and Privacy/Legal review; Search, Notifications and Administration participate where affected. Formal comparison requires attributable Product scenarios and affected-owner evidence. Acceptance follows [`PRODUCT.md` §§24–25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires separate applicable Specification, Contract, Architecture, Security and Engineering gates.

## Decision Status and Explicit Non-Authorizations

DEC-0023 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It does not select Option A, B or C; enable Wishlist or guest saved interest; establish a guest Identity, authenticated Customer, Account or Principal; approve guest persistence, cross-device synchronization, guest-to-account merging, deduplication, sharing, public access, alerts, notifications, limits, retention, access grants, Roles or Permissions; or create Product publication, Price, Stock, Stock Reservation, Cart, Checkout or Order authority. It selects no endpoint, DTO, schema, provider, dependency, data migration or implementation mechanism. `PRODUCT.md` §24 item 16 remains open, DEC-0013 remains Proposed, and no implementation is authorized.

## References

- [AGENTS.md §5](../../.ai/core/AGENTS.md), [PRODUCT.md §§4.1, 24 item 16, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md §§5–6, 13 and 22](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md §§12 and 35](../../.ai/core/SECURITY-STANDARDS.md) — governance, terminology, ownership and protection.
- [Customer Domain REQ-CUS-022–023 and 039](../domains/customer/customer-domain.md) and [Customer Backend BCUS-REQ-029](../backend/customer/customer-backend.md) — conditional Wishlist ownership, non-authority and access boundaries.
- [Identity Domain REQ-IDN-005, 010, 020 and 028](../domains/identity/identity-domain.md) and [Identity Backend](../backend/identity/identity-backend.md) — trusted Principal and security boundaries.
- [Product Domain](../domains/product/product-domain.md) and [Backend](../backend/product/product-backend.md); [Pricing Domain](../domains/pricing/pricing-domain.md) and [Backend](../backend/pricing/pricing-backend.md); [Inventory Domain](../domains/inventory/inventory-domain.md) and [Backend](../backend/inventory/inventory-backend.md) — independent current source facts.
- [Cart Domain](../domains/cart/cart-domain.md) and [Backend](../backend/cart/cart-backend.md); [Checkout Domain](../domains/checkout/checkout-domain.md) and [Backend](../backend/checkout/checkout-backend.md) — purchase-intent and revalidation boundaries.
- [Search Domain](../domains/search/search-domain.md) and [Backend](../backend/search/search-backend.md); [Notifications Domain](../domains/notifications/notifications-domain.md) and [Backend](../backend/notifications/notifications-backend.md); [Administration Domain](../domains/admin/admin-domain.md) and [Backend](../backend/admin/admin-backend.md) — bounded consumer, communication and support authority.
- [DEC-0013](DEC-0013-guest-checkout-account-requirement.md) — Proposed guest Checkout preference, not accepted Wishlist, guest identity or transition policy.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected guest and registered-customer saved-interest alternatives while preserving conditional Customer ownership, Identity and source-Domain authority, unresolved guest continuity and privacy choices, Product item 16, and separate evidence, acceptance and implementation gates. |
