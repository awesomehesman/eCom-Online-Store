# DEC-0043 — Initial V1 Base-Price Assignment Policy

- **Identifier:** DEC-0043
- **Title:** Initial V1 Base-Price Assignment Policy
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-11-03
- **Last Updated:** 2026-11-03
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Approved Pricing requirements assign current authoritative Price determination to
Pricing, require governed Product or Variant association and explicit Currency, and
protect confirmed Order history. They do not select initial assignment granularity
or the applicable initial Pricing Rule. Existing immutable Money and Currency value
objects support exact ZAR arithmetic and comparison but do not establish a Price.
No Pricing API Contract currently selects these business choices.

This proposal records the bounded M2.2B policy for Product/business-owner review.
Accepted ADR-0006 selects the Pricing backend specification, not a commercial rule.
Accepted DEC-0019 governs Product/Variant identity and Attribute semantics, not
Price assignment. No approval or implementation authority is inferred from either.
Affected consumers include future Product price presentation, Cart, Checkout and
Order; none acquires Pricing authority through this proposal.

## Decision Scope and Status

Propose a single initial V1 base-price assignment rule. The policy below is subject
to accountable Product/business-owner acceptance. **No policy is Accepted by this
record.** It changes neither Approved specifications nor existing implementation.
It is not a complete selling-price, publication, tax or Checkout policy.

## Proposed Policy

1. Base prices are assigned at Variant level.
2. Each assignment references the owning Product and Variant. References must
   identify valid governed membership; identifiers alone do not prove it.
3. V1 base prices use explicit ZAR Currency.
4. Exactly one current explicit base-price assignment is permitted per Variant.
   This is a uniqueness limit, not a requirement that every Variant already has a
   price: absence remains an explicit missing-price outcome.
5. No Product-level fallback is permitted.
6. Base-price amounts must be strictly positive. This constraint applies to base
   prices; it does not restrict Money generally or establish decimal scale or rounding.
7. Assignments become applicable immediately upon an authorized, committed operation.
   A request, in-flight operation or failed transaction cannot establish applicability.
8. An assignment remains applicable until explicitly replaced or withdrawn.
9. Replacement and withdrawal preserve historical revisions. Replacement must not
   expose two current assignments or silently overwrite the previous revision.
10. Missing, invalid, conflicting, obsolete or withdrawn assignments cannot produce
    a current valid base-price result. A replaced revision is obsolete for current
    selection; retained history is not fallback authority. Product evidence safety
    and revalidation requirements remain applicable.
11. No arbitrary precedence or latest-wins selection is permitted. Conflicting
    current assignments fail safely rather than selecting a winner.
12. Tax, discounts, promotions, scheduled pricing, customer/channel variations and
    currency conversion remain outside this initial rule.
13. A valid base price alone does not establish sellability, tax-inclusive display
    amounts or Checkout totals. It cannot establish Product publication, customer
    visibility or Inventory availability.

## Alternatives Considered

| Alternative | Proposed disposition and consequence |
| --- | --- |
| Variant-level explicit assignment | Recommended: matches purchasable Variant identity and supports distinct Variant amounts; requires explicit assignment for each priced Variant. |
| Product-level price inherited by Variants | Not recommended initially: simplifies shared pricing but requires inheritance and exception semantics not established here. |
| Product default with Variant overrides | Deferred: adds fallback and precedence rules and complicates missing/conflicting outcome handling. |
| Scheduled or multiple contextual assignments | Deferred: adds temporal, customer/channel and selection policy beyond the initial scope. |
| Retain the status quo | Feasible: Money remains usable, but deterministic business base-price assignment remains blocked pending a policy choice. |

These are proposal assessments, not accepted rejections or measured evaluation results.

## Consequences

If accepted and synchronized through governance, the policy enables deterministic
Variant base-price construction and selection without a Price List or formula.
Explicit absence and conflict prevent fabricated valid amounts. Costs include more
assignments where Variants share a price, revision-history maintenance and atomic
replacement/withdrawal consistency. Strict positivity excludes zero-price selling.
Immediate applicability excludes scheduling; later expansion requires explicit policy
and compatibility review. Moving to Product defaults later must not reinterpret
existing assignments or historical commercial snapshots.

## Security Impact

An authorized operation requires existing trusted server-side contextual authorization;
this proposal selects no Staff assignment, Permission, Role, actor provenance, endpoint
or audit mechanism. Applicable security and audit requirements remain binding. Price
input, a stored row, an identifier or successful Money construction cannot establish
operation authorization, governed Product validity or current Price applicability.
No Identity, Staff or publication governance is reopened.

## Data, Compatibility and Migration Impact

Preserve stable Product and Variant identity and their governed ownership. Attribute
labels or configurations do not become Price identity. Preserve immutable assignment
revision history on replacement and withdrawal. Current Pricing may supply values to
Order, but confirmed Order snapshots remain Order-owned and are never automatically
rewritten by Price changes. This record chooses no independent Price identifier,
identifier format, schema, migration, retention period or historical storage mechanism.
No existing data is migrated or grandfathered into authoritative Pricing.

## Operational Impact

Future operations must distinguish missing, invalid, conflicting, obsolete and withdrawn
assignments, dependency failure and uncertain outcomes. A failed or uncommitted mutation
must not publish a current assignment. Technical transaction/concurrency mechanisms and
observability remain implementation responsibilities under existing Architecture and
Pricing requirements; this proposal selects no infrastructure, lock or retry policy.

## Required Acceptance Authority

| Accountable capacity | Disposition | Required scope |
| --- | --- | --- |
| Product / business owner | PENDING | Explicit acceptance, rejection or amendment of this bounded commercial policy, including Variant-only assignment, positivity and immediate applicability. |

Engineering must verify implementability, consistency and compatibility under existing
review conventions. Any affected-owner review required by DECISIONS.md remains applicable.
No independent reviewer, signature, acceptance date or completed review is claimed.
The creation date records proposal preparation, not business acceptance.

## Implementation Dependencies and Verification

Before policy-dependent implementation, obtain attributable accountable acceptance and
complete applicable governing-source synchronization and review. Do not treat indexing
or Proposed status as authority. The smallest subsequent increment is a pure Pricing
base-price assignment and deterministic selection model using Money and governed
Product/Variant references, without Product persistence integration or HTTP.

Implementation must verify exact Product/Variant membership at the appropriate trusted
boundary, preserve rule/revision context and enforce one current assignment atomically
when persistence is introduced. Proposed tests cover positive ZAR construction, zero
and negative rejection, missing assignments, competing current assignments, obsolete
and withdrawn revisions, ownership mismatch, replacement/withdrawal history, and no
fallback or implicit conversion. Missing or unsafe Product evidence must not produce
trusted current Pricing. Technical identity representation can be selected consistently
with approved contracts; no speculative Price aggregate or lifecycle is required.

## Explicit Exclusions and Non-Authorizations

No tax treatment or tax-inclusive display claim, Discount, Promotion, Voucher, scheduled
pricing, customer/channel variation, currency conversion, rounding or scale rule, total
calculation, Order implementation, Product persistence integration, database migration,
public/Staff endpoint, Staff authorization policy, publication or positive visibility is
authorized. This record changes no Product code, Accepted DEC-0019, Pricing specification
or public Contract. Unresolved tax and promotion decisions remain unresolved. Acceptance
of this policy would not by itself establish operational authorization or release readiness.

## Traceability and References

| Governing requirement | Proposed specialization |
| --- | --- |
| REQ-PRC-004 | Explicit owning Product and Variant references; Variant-level granularity, no Product fallback. |
| REQ-PRC-007 | One current explicit base-price assignment under this proposed initial rule; missing/invalid/conflicting assignments cannot supply a valid result. |
| REQ-PRC-008 | Explicit changed/obsolete/withdrawn outcomes and retained revisions; no arbitrary winner or historical rewrite. |
| BPRC-REQ-019 | Deterministic authoritative selection under approved rules and current governed inputs; no client or projection authority. |
| REQ-PRC-006 / BPRC-REQ-018 | Exact Money with explicit ZAR; no implicit conversion, scale or rounding rule. |
| REQ-PRC-005, 019 / BPRC-REQ-017, 029 | Product input safety and separation of current Price from confirmed Order history. |

- [Pricing Domain](../domains/pricing/pricing-domain.md).
- [Pricing Backend](../backend/pricing/pricing-backend.md).
- [Accepted ADR-0006](../adr/ADR-0006-post-inventory-backend-specification.md).
- [Accepted DEC-0019](DEC-0019-product-variant-attribute-standards.md).
- [Decision conventions and index](../../.ai/core/DECISIONS.md),
  [Product baseline §16.1](../../.ai/core/PRODUCT.md),
  [Architecture](../../.ai/core/ARCHITECTURE.md) and
  [Glossary — Price, Base Price and Pricing Rule](../../.ai/core/GLOSSARY.md).

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-03 | Proposed | Proposed bounded Variant-level V1 ZAR base-price assignment, strict positivity, single-current selection and revision-preserving replacement/withdrawal; left accountable Product/business-owner acceptance pending and granted no implementation authority. |
