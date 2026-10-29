# DEC-0019 — Product Variant and Attribute Standards

- **Identifier:** DEC-0019
- **Title:** Product Variant and Attribute Standards
- **Type:** Product Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-10-13
- **Acceptance Date:** 2026-10-29
- **Last Updated:** 2026-10-29
- **Owner:** Product
- **Authoritative:** true
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Approved Baseline

The Approved Product Domain REQ-PRD-007–013 requires stable Variant identity, correct Product membership, semantic uniqueness under governed Attributes, descriptive/variant-defining distinctions and historical integrity. BPRD-REQ-014–019 and 020–030 specialize membership, validation, lifecycle, publication, content and media without transferring other Domains' authority.

This record resolves the initial Attribute-policy direction previously left open by `PRODUCT.md` §24 item 3. The task explicitly limits edits to this file: canonical Product and Decision Index synchronization is outstanding and is not claimed complete. This Accepted decision must not be represented as an already synchronized repository-wide baseline; required governing-source updates remain subject to their approved process. No other document is changed here.

## Decision — Option C: Hybrid Attribute Standards

The Product owner explicitly approves Option C: a shared core of Product-owned Attribute concepts with governed Product-family applicability and extensions. Initial families are **Apparel**, **Footwear** and **Accessories**. These are Product Attribute contexts, not Category taxonomy or a complete launch assortment.

The initial standard is version **1.0.0**. **Size** and **Colour** are shared variant-defining concepts where applicable. **Fit** and **Material** are initially descriptive and must not independently create distinct Variants. No fit/material value catalogue or SKU format is selected.

### Family applicability and Size vocabulary

| Family | Governed initial Size values | Applicability |
| --- | --- | --- |
| Apparel | XS, S, M, L, XL, XXL, XXXL | Size defines size-dependent configurations. A Product's applicable defining concepts must be explicit and consistent across its Variants. |
| Footwear | UK 3 through UK 13 inclusive; half sizes UK 3.5 through UK 12.5 where applicable | Values retain the UK system; other sizing systems are not silently converted or equated. Half-size applicability must be confirmed for the Product. |
| Accessories | One Size; other applicable size configurations only when explicitly governed | Neither Size nor Colour is universally mandatory for Accessories. A Product must explicitly identify applicable concepts; missing data must not be mistaken for non-applicability. |

Product must establish the applicable defining concepts and supported configurations before publication. An accessory may be size-only, colour-only, both, or neither where that accurately represents its governed configuration. When neither applies, descriptive differences alone cannot create multiple semantically distinct configurations. Unsupported accessory sizes require the extension process below; arbitrary strings do not become approved values by storage or submission.

### Colour vocabulary and canonicalization

The minimum initial governed colour vocabulary is canonical code **BLACK**, customer-facing display label **Black**. This deliberately small vocabulary supplies the approved example without inventing a comprehensive colour catalogue. Other colours must be added through explicit Product approval before acceptance.

`Black`, `black`, `BLACK` and case-equivalent representations of the approved code/label resolve to the same canonical value, BLACK. Canonical codes and their approved label/alias mappings determine equality; display labels are not Variant identity. Comparison must be deterministic and independent of process locale, database collation or storefront rendering. No fuzzy matching, nearest-colour substitution, unknown-value acceptance or implicit alias creation is permitted. Unsupported values must be rejected. Whitespace, punctuation and other aliases are not implicitly approved; any additional equivalence must be explicitly governed and tested.

Apparel size tokens resolve case-insensitively to the listed uppercase tokens. Footwear values use exact UK numeric meaning: equivalent approved representations such as UK 3 and UK 3.0 refer to the same size, without rounding to a supported size. One Size is a governed meaning, not a substitute for an unknown or missing size. Transport encodings and accepted input spellings must be documented in the later Contract; they must preserve these meanings rather than invent new equivalences.

### Semantic uniqueness and stable identity

Within an owning Product, Variant uniqueness must use the canonical values of all applicable variant-defining concepts under the applicable family standard. Equivalent configurations must be rejected, including when their display labels or casing differ. Product membership scopes this comparison; equal configurations in different Products do not imply shared Variant identity.

Fit, Material, mutable descriptions and customer-facing labels must not independently distinguish Variants. Missing applicable values are invalid; an explicitly non-applicable concept is not an unknown value or a wildcard. All Variants of a Product must be evaluated under a coherent applicability definition so omission cannot evade duplicate detection.

Stable Product and Variant identity must remain independent of Attribute labels and values. Changing a label or canonical value must not derive a replacement identity, reassign membership, merge Variants or rewrite confirmed commercial history. Concurrent creation/correction must preserve semantic uniqueness and explicit conflict outcomes; this decision selects no persistence or locking mechanism.

## Validation and Publication Implications

Product-owned validation must reject unsupported families, sizes, colours, incompatible values, ambiguous applicability and semantically duplicate configurations. A Product or Variant must not become Published while required defining values, governed meanings or applicable validation remain missing, conflicting or unsupported.

Attribute validation is necessary but not sufficient for publication. Existing Product requirements for identity, descriptive completeness, media accuracy/rights/accessibility, lifecycle and server-side contextual Authorization remain applicable under REQ-PRD-014–023 and BPRD-REQ-019–030. A Published flag, Search index or Category placement cannot substitute for those checks. No new lifecycle transition graph, media mechanism or publication approval workflow is selected.

## Extension, Versioning and Evolution

Product owns a versioned record of approved concepts, canonical codes, display labels/aliases, family applicability and allowed configurations. An extension must identify its accountable Product approval, exact proposed meanings and equivalences, affected family/Products, duplicate/ambiguity examples, effective standard version and compatibility impact before use. Adding a colour or accessory size must not silently select a new variant-defining concept or change Fit/Material into defining Attributes.

Extensions require explicit Product approval. Engineering reviews deterministic validation and uniqueness; Category confirms taxonomy separation, Search and UX/accessibility review consumer meaning where affected, and Security/Operations review administration and correction impacts where material. No completed independent reviews are invented here.

Every change must explicitly version the standard and preserve stable Product/Variant identity, accepted membership, historical Order and other confirmed commercial snapshots, and unambiguous customer-facing meaning. Label corrections must not change canonical meaning. A change to canonical meaning, applicability or equivalence requires explicit impact and collision analysis, attributable approval and a governed compatibility/migration plan before application to accepted Variants.

Retain the meaning/version applicable to accepted data. Do not silently revalidate historical snapshots under new semantics, merge conflicting Variants, choose an arbitrary winner or reinterpret existing values. Where compatibility cannot be established, withhold the affected mutation/publication and expose a governed conflict pending resolution. No storage schema, migration algorithm or retention period is selected.

## Alternatives Considered

| Option | Disposition | Rationale/trade-off |
| --- | --- | --- |
| A — Uniform standards | Not selected | A single undifferentiated standard does not capture the approved family-specific sizing and accessory applicability. |
| B — Product-family standards | Not selected | Fully separate standards would omit the approved shared Size/Colour concepts. |
| C — Hybrid standards | Selected and approved | Shared canonical concepts with family-specific allowed values/applicability preserve customer meaning and Product-owned uniqueness; costs include explicit extension, versioning and compatibility management. |

These dispositions record the owner's decision, not a claim of measured comparative experiments or a complete launch assortment.

## Authority, Security and Data Boundaries

Product owns Product/Variant identities, membership, Attribute meanings and Product-owned eligibility. Category retains taxonomy, classification and Category-side associations. Search retains filters, facets, ranking and non-authoritative representations. Neither can establish Attribute meaning. Pricing and Inventory retain Price, commercial calculations, Stock and availability; Attribute validity is not final purchasability. Customer, Cart, Checkout and historical commercial owners retain their authority.

Attribute administration and corrections require trusted server-side contextual Authorization, input safety and proportional Audit Records under Security Standards. No Role/Permission matrix, staff assignment or new workflow is selected. Descriptive content and labels must remain accurate, accessible and safely presented. Imports, UI labels and stored rows cannot self-authorize new meanings.

## Compatibility, Migration and Operational Consequences

The accepted hybrid standard enables deterministic validation and semantic duplicate detection while requiring owned vocabulary maintenance. The minimal colour vocabulary intentionally rejects other colours until extension approval. Accessory applicability must be explicit rather than inferred from missing fields.

Later implementation must test canonical equivalence, family boundaries, unsupported values, descriptive-only differences, membership, concurrency and version compatibility. Changes to live representations or downstream projections must preserve existing accepted state and historical snapshots. This record performs no data migration, publication, implementation, deployment or operational adoption.

## Approval and Remaining Implementation Questions

The Product owner's explicit instruction in this task is the approval provenance for Option C and the policy recorded here, dated 2026-10-29. Product is the accountable decision owner. No signatures, independent reviewers, completed cross-domain reviews or approvals in other capacities are claimed.

Remaining implementation questions are the Contracted representation of canonical values and standard versions; persistence/integrity/concurrency enforcement; exact identifier representations; and consumer error/pagination/security Contracts. Additional accessory configurations and colours require the explicit extension process rather than guessed values. These questions do not reopen the approved hybrid direction, initial families, defining/descriptive distinction or uniqueness invariants.

Canonical Product and Decision Index synchronization remains outstanding because this task prohibits editing those files. Implementation remains subject to applicable governing-source synchronization, Domain, Contract, Architecture, Security and Engineering gates. No claim is made that this single-file acceptance closes every M1 implementation prerequisite.

## Explicit Non-Authorizations and Supersession

DEC-0019 supersedes no separate decision and is superseded by none. It selects no SKU format, database schema, API route/DTO, event Contract, Category taxonomy, Search filter policy, Pricing/Inventory rule, full colour/fit/material catalogue, launch assortment or production release policy. It does not resolve DEC-0018 or DEC-0012 and authorizes no source-code or database change by itself.

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
| 1.0.0 | 2026-10-29 | Accepted | Recorded Product-owner approval of Option C, initial families and sizing, minimal canonical colour vocabulary, applicability, semantic uniqueness, descriptive Attributes and versioned evolution; governing-source synchronization remains outstanding outside this single-file task. |
