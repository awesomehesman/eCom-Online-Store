# DEC-0019 — Product Variant and Attribute Standards

- **Identifier:** DEC-0019
- **Title:** Product Variant and Attribute Standards
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

[`PRODUCT.md` §24 item 3](../../.ai/core/PRODUCT.md) leaves initial size, fit, colour, material and other Product Variant and Attribute standards open. The [Approved Product Domain Specification](../domains/product/product-domain.md) REQ-PRD-007–013 requires stable Product Variant identity, uniqueness under governed variant-defining Attributes, a distinction from descriptive Attributes, valid meanings and values, and historical integrity as Attributes evolve. The [Product Backend Specification](../backend/product/product-backend.md) BPRD-REQ-014–017 preserves those obligations without choosing a launch vocabulary or SKU format.

Category owns Category taxonomy and classification, not Product Variant identity or Product-owned Attributes. Search may consume governed Product and Category evidence for discovery, but a filter, index or storefront label cannot define authoritative Attribute meaning. [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md) remains Proposed and selects no Category structure or launch assortment. Those boundaries do not prevent Product from framing its separate Attribute-policy question.

## Decision Question and Product Policy Boundary

Which initial size, fit, colour, material and other Attribute standards should govern Product Variants, including which Attributes define Variant uniqueness and how allowed meanings evolve without changing historical Product identity?

Product and its accountable merchandising authority own this policy. Product-domain requirements already govern identity, distinction, validation and history; the actual Attribute vocabulary, allowed values, normalization and variant-defining set remain unresolved. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 3 remains open.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Uniform standards | A centrally governed Attribute vocabulary applied consistently across the initial assortment. | May simplify comparison, naming and duplicate detection across Product families. | A common vocabulary may misrepresent materially different size, fit, colour or material meanings; no values or Variant keys are selected. |
| B — Product-family standards | Governed Attribute standards differentiated by Product family where justified. | May better reflect distinct merchandising and customer-selection needs. | Can fragment discovery, increase maintenance and create ambiguous cross-family comparison; no families, standards or Category linkage are selected. |
| C — Hybrid standards | A shared core Attribute vocabulary with controlled Product-family extensions. | May preserve common comparison while allowing justified variation. | Requires clear ownership, compatibility and evolution rules; the core, extensions and uniqueness treatment remain unspecified. |

These are unselected policy approaches, not definitions of a launch assortment, size scale, fit convention, colour or material vocabulary, normalization algorithm or SKU format. None assumes Category taxonomy determines Product-family membership or Product-owned Attribute truth.

## Affected Requirements and Domain Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product | Owns Product and Product Variant identity, Product-owned Attributes, Variant membership, structural sellability and historical Product meaning under REQ-PRD-001–013. |
| Category | Owns Category identity, hierarchy, classification and Category-side association under [REQ-CAT-010–014](../domains/category/category-domain.md); Category membership cannot redefine Product identity or Attributes. |
| Search and Discovery | Consumes governed Product and Category evidence; result ranking, filters and index state cannot establish a Product Variant or Attribute value. |
| Storefront presentation | May show governed Attribute meaning accessibly; filtering, navigation and labels are representations, not Attribute authority. |

Product Variant uniqueness must remain based on a separately approved variant-defining Attribute model; semantically duplicate combinations must be rejected. Descriptive Attributes must remain distinguishable from variant-defining Attributes, and changes cannot silently replace stable Variant identity or rewrite confirmed historical commercial snapshots. [Category Backend](../backend/category/category-backend.md) and the [Search Domain](../domains/search/search-domain.md) and [Search Backend](../backend/search/search-backend.md) retain their existing boundaries.

## Comparative Evaluation Criteria

Product-led comparison must assess stable Product Variant identity; the distinction between variant-defining and descriptive Attributes; size and fit consistency; colour and material meaning; allowed values and normalization; duplicate or ambiguous configurations; customer discovery and comparison; merchandising usability; Search and filtering effects; accessibility and comprehensibility; historical integrity; standard evolution and reversibility; operational maintenance; and compatibility or migration costs. A common label must not be assumed to mean the same customer-facing property across materially different Product contexts without evidence.

## Security and Administration Impact

Attribute creation, correction and Product Variant changes remain subject to trusted, contextual Authorization, default denial and proportional Audit Records under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). Administration coordinates only authorized Product operations. This record establishes no Role, Permission, approval chain or staff assignment; [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) remains Proposed. Customer-facing Attribute content must be understandable and safe without exposing protected operational details.

## Product Data Ownership and Attribute Integrity

Product remains the authoritative owner of Product Variant and Product-owned Attribute facts. A Category label, Search filter, navigation path, imported value or UI state cannot establish accepted Attribute meaning. Governed values must preserve identity, semantic uniqueness and valid Product context; conflicting or unsupported values must be resolved before publication under Approved Product requirements. This record selects no Attribute field set, allowed-value list, normalization mechanism, identifier format, schema, API or Event Contract.

## Compatibility, Migration and Historical Impact

A later standard could affect existing Product and Product Variant representations, Search Projections, storefront filters, Category associations and historical Order references. Review must distinguish current corrections from historical meaning and avoid silent reassignment of stable Variant identity. No existing record, Contract, migration, redirect or publication state changes through this proposal.

## Operational and Merchandising Consequences

Uniform standards may ease governance but fail to express legitimate differences; Product-family standards may improve specificity while increasing review and support work; a hybrid may require more careful extension controls. These are hypotheses, not measured outcomes. Product and merchandising must assess real assortment and maintenance capacity before selection. No launch assortment, staffing model, service level or content workflow is approved.

## Evidence Required Before Selection

Product and accountable merchandising owners must provide attributable representative launch Product and Product Variant examples, customer choice and comparison needs, candidate Attribute meanings, known ambiguity cases and intended evolution needs. Product-domain reviewers must assess uniqueness, validity, lifecycle and historical integrity. Category reviewers must confirm taxonomy separation; Search and UX/accessibility reviewers must assess discovery and comprehension without taking Attribute authority. Operations and Security must review administration, correction, audit and abuse implications. Evidence must distinguish Approved requirements, proposed policy, actual catalogue facts and stakeholder approvals; none is claimed complete here.

## Open Questions and Acceptance Prerequisites

1. Which Attribute concepts should be shared across the launch assortment, and which, if any, need Product-family-specific meaning? No concept or family is selected.
2. Which Attributes should define Product Variant uniqueness versus describe a Product or Variant? No defining set is selected.
3. What Product-governed allowed values and normalization, if any, are necessary to reject duplicates and incompatible meanings without inventing a technical algorithm?
4. How should a later approved standard evolve while retaining stable Product Variant identity, historical Order meaning and understandable customer comparison?
5. How should [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md) taxonomy decisions inform presentation without making Category authoritative for Product-owned Attributes?
6. Which Product, Category, Search, storefront, Contract and migration changes would follow an Accepted standard? None is authorized here.

Formal comparison requires separately governed scope and attributable Product evidence. Product and accountable merchandising authority lead selection and acceptance, with Product, Category, Search, UX/accessibility, Operations and Security review; affected commercial owners participate where material. Acceptance follows [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation remains subject to separate applicable Domain, Contract, Architecture, Security and Engineering gates.

## Explicit Non-Authorizations and Supersession

DEC-0019 supersedes nothing and is superseded by nothing. It does not select Option A, B or C; establish a launch size scale, fit convention, colour or material vocabulary, allowed-value taxonomy, Product-family grouping, normalization algorithm, variant-defining Attribute set, SKU format, Category policy, Search filter, navigation structure, launch assortment, schema, API, event or Contract. It does not accept Product policy, close `PRODUCT.md` §24 item 3, resolve DEC-0018 or DEC-0012, claim stakeholder evidence or authorize implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§24 item 3, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), and [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) — governance, ownership and terminology.
- [Product Domain REQ-PRD-001–013](../domains/product/product-domain.md) and [Product Backend BPRD-REQ-014–017](../backend/product/product-backend.md) — Product Variant identity, Attribute meaning, uniqueness and evolution.
- [Category Domain REQ-CAT-010–014](../domains/category/category-domain.md) and [Category Backend](../backend/category/category-backend.md) — separate Category taxonomy and membership authority.
- [Search Domain](../domains/search/search-domain.md) and [Search Backend](../backend/search/search-backend.md) — non-authoritative discovery consumption.
- [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md) — Proposed initial Category taxonomy, not Product Attribute authority.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed unselected uniform, Product-family and hybrid Attribute-standard alternatives while preserving Product-owned Variant and Attribute authority, Category and Search boundaries, historical integrity, unresolved Product item 3, and separate evidence, acceptance and implementation gates. |
