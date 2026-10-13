# DEC-0018 — Initial Catalogue Taxonomy Policy

- **Identifier:** DEC-0018
- **Title:** Initial Catalogue Taxonomy Policy
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

[`PRODUCT.md` §24 item 2](../../.ai/core/PRODUCT.md) leaves the initial product categories and catalogue taxonomy open. The [Approved Category Domain Specification](../domains/category/category-domain.md) REQ-CAT-010–014 assigns authoritative taxonomy, classification structure and Category-side membership to Category, while expressly leaving initial roots, hierarchy depth, naming, classification and membership policy unresolved. REQ-CAT-017–019 distinguishes navigation eligibility and ordering from Category hierarchy; REQ-CAT-021–028 protects historical meaning and governed reclassification. The [Category Backend Specification](../backend/category/category-backend.md) preserves these boundaries without selecting an initial taxonomy.

The [Approved Product Domain Specification](../domains/product/product-domain.md) REQ-PRD-001–002 assigns Product and Product Variant identity and Product-owned Attributes to Product, separate from Category structure. Search may consume governed classification but cannot become taxonomy authority. CMS may present governed content but cannot redefine Category meaning. These are existing ownership rules, not approval of a launch assortment, Category hierarchy or storefront navigation design.

## Decision Question and Precise Boundary

What initial Category structure and classification policy should govern the fashion catalogue, including roots, hierarchy depth, naming and Product-to-Category membership, while preserving Product-owned identity and Attributes?

This is a Product-owned commercial taxonomy-policy choice. Category owns accepted Category identity, hierarchy, classification and Category-side membership under Approved policy. Product retains Product and Product Variant identity, publication and Product-owned Attributes. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 2 remains open.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Shallow taxonomy | Use a limited hierarchy with simple, governed Product-to-Category membership. | May simplify initial merchandising, naming and customer browsing. | May be too coarse for the intended assortment or discovery needs; membership and reclassification ambiguity still need explicit policy. No roots, depth or membership cardinality are selected. |
| B — Structured hierarchy | Use a more explicitly governed multilevel Category hierarchy with defined classification responsibilities. | May express distinct fashion groupings and consistent merchandising relationships. | Adds maintenance and reclassification complexity and may confuse discovery if the structure does not fit actual Product evidence. No levels or names are selected. |
| C — Flexible taxonomy | Permit controlled structural evolution and more flexible classification under explicit governance. | May adapt to assortment change without treating every presentation change as taxonomy truth. | Requires stronger naming, conflict, historical-integrity and ownership controls; flexibility cannot imply automatic classification or unrestricted multiple membership. |

These are structural policy approaches, not proposed launch Category names, a selected hierarchy or an implementation design. Each would need a separately reviewed rule for naming, association, lifecycle and reclassification. No alternative is preferred or formally evaluated.

## Affected Requirements and Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product policy | Product and accountable merchandising authority determine the initial commercial classification policy and launch-assortment fit. |
| Category | Owns Category identity, semantic uniqueness, hierarchy, classification, Category-side membership, lifecycle and historical Category meaning under REQ-CAT-003–014 and 021–028. |
| Product | Owns Product and Product Variant identity, Product-owned Attributes, publication and Product-side association meaning under REQ-PRD-001–002 and applicable Product Requirements. Category membership alone cannot publish or sell a Product. |
| Search and Discovery | Consumes current governed Product and Category evidence for discovery; its index or result is not Category or Product authority. |
| CMS | Presents governed Category-related content where permitted; CMS content or publication does not establish taxonomy or Product truth. |
| Storefront navigation | May consume Category meaning, but navigation grouping, ordering or routes must not be silently equated with Category hierarchy or become its authority. |

The [Search Domain Specification](../domains/search/search-domain.md) and [Search Backend Specification](../backend/search/search-backend.md), and the [CMS Domain Specification](../domains/cms/cms-domain.md) and [CMS Backend Specification](../backend/cms/cms-backend.md), retain their approved consumer and presentation boundaries. Product §24 item 3, covering size, fit, colour, material and other Product Variant or Attribute standards, remains a separate unresolved policy; this record defines none of those standards.

## Comparative Evaluation Criteria

Product-led evaluation must compare customer discovery and comprehensibility; merchandising usability; coherent Category naming and classification quality; ambiguous, missing or conflicting Product membership; hierarchy maintenance; Search discoverability; CMS and navigation implications; safe Category reparenting and Product reclassification; historical references; operational complexity; and reversibility as the assortment changes. It must distinguish an authoritative Category relationship from a navigation or Search Projection and avoid assuming that a broader hierarchy necessarily improves discovery.

## Security and Administration Impact

Category creation, naming, reparenting, reclassification and withdrawal must retain contextual server-side Authorization, default denial, least privilege and proportional Audit Records under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). Administration coordinates only authorized Domain operations; this record assigns no Role, Permission, approver, workflow or automatic classification authority. [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) remains Proposed and does not grant taxonomy administration access.

## Data Ownership and Classification Integrity

Category remains authoritative for accepted taxonomy and Category-side membership; Product remains authoritative for Product and Variant identity and Product-owned Attributes. An invalid, ambiguous, stale or conflicting association cannot be treated as accepted Category meaning. Reclassification must preserve historical Product and Category meaning rather than silently reinterpret prior commerce records. No Category identifier, hierarchy representation, schema, index, Search mapping, event or API Contract is selected.

## Compatibility, Migration and Historical Impact

Any eventual taxonomy change must assess existing Products, Category references, Search and CMS Projections, customer-facing links, historical Order meaning and migration reversibility. A changed label or navigation path cannot silently replace stable Category identity or historical Product truth. This Proposed record changes no existing content, Contract, data, migration plan or launch assortment.

## Operational and Merchandising Consequences

A shallow structure may be easier to curate but less expressive; a structured hierarchy may improve classification discipline while increasing reparenting and maintenance work; a flexible policy may accommodate assortment evolution while requiring clearer controls and audit. These are hypotheses, not evidenced outcomes. Product and merchandising must assess actual staffing, catalogue maintenance and customer-support implications before selection. No operational workflow, approval chain, service level or rollout schedule is established.

## Evidence Required Before Selection

Product and accountable merchandising owners must provide an attributable intended launch assortment, representative Product and Product Variant examples, customer discovery goals, proposed naming principles and classification use cases. Category and Product owners must review identity, association, lifecycle, historical and Attribute boundaries. Search, CMS and UX must review discovery, presentation and navigation consequences without claiming taxonomy ownership. Operations, Security and affected commercial owners must assess administration, audit, migration and abuse risks. Evidence must distinguish current Approved Requirements from Product proposals, observed catalogue needs and stakeholder approval; no such confirmation is claimed here.

## Open Questions and Acceptance Prerequisites

1. What representative launch assortment and customer discovery evidence should discriminate among the structural alternatives? No initial Category names or roots are chosen here.
2. What hierarchy depth and naming governance, if any, are justified by that evidence? No depth or naming convention is selected.
3. Should a Product have one or multiple Category associations, and is a primary association needed? Neither cardinality nor priority is selected.
4. What policy should govern Category creation, lifecycle, reparenting, reclassification and historical references without changing Product identity or Attributes?
5. Which classification concerns belong to the separate Product Variant and Attribute standards decision under `PRODUCT.md` §24 item 3?
6. Which navigation or Search experiences need Category evidence, and which are presentation choices rather than taxonomy structure?
7. What later Specification, Contract, content and migration reviews would an Accepted policy require? None is authorized by this record.

Formal comparison requires separately governed scope and attributable evidence. Product and accountable merchandising authority lead selection and acceptance review, with Category, Product, Search, CMS, UX, Operations and Security; affected commercial and accessibility reviewers participate where material. Acceptance follows [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation remains subject to separate applicable Domain, Contract, Security, Architecture and Engineering gates.

## Explicit Non-Authorizations and Supersession

DEC-0018 supersedes nothing and is superseded by nothing. It does not select an alternative, initial Category root or name, hierarchy depth, naming convention, single- or multi-Category membership, primary Category, automatic classification, Product Attribute standard, navigation structure, assortment, Product publication rule, Role, approval workflow, API, event, schema or implementation mechanism. It does not accept a Product policy, close `PRODUCT.md` §24 item 2 or item 3, change Approved Domain authority, claim stakeholder evidence or authorize implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§24 item 2, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), and [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) — governance, ownership, terminology and Decision Record conventions.
- [Category Domain REQ-CAT-003–014, 017–019 and 021–031](../domains/category/category-domain.md) and [Category Backend](../backend/category/category-backend.md) — taxonomy, membership, lifecycle and Product boundary.
- [Product Domain REQ-PRD-001–002 and 011–013](../domains/product/product-domain.md) and [Product Backend](../backend/product/product-backend.md) — Product identity, Variant and Attribute authority.
- [Search Domain](../domains/search/search-domain.md) and [Search Backend](../backend/search/search-backend.md) — non-authoritative discovery consumption.
- [CMS Domain](../domains/cms/cms-domain.md) and [CMS Backend](../backend/cms/cms-backend.md) — governed content presentation, not Category authority.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) — Proposed administrative permission decision; no Role assignment or approval chain is inferred.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed shallow, structured and flexible initial taxonomy alternatives without selecting Category roots, names, depth, membership or launch assortment; preserved Category, Product, Search and CMS authority, Product item 3 as open, and separate evidence, acceptance and implementation gates. |
