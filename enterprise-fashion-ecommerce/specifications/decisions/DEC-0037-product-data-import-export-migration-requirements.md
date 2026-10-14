# DEC-0037 — Product Data Import, Export and Migration Requirements

- **Identifier:** DEC-0037
- **Title:** Product Data Import, Export and Migration Requirements
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

[`PRODUCT.md` §24 item 27](../../.ai/core/PRODUCT.md) leaves “Product data import, export, and migration requirements” unresolved. Product owns authoritative Product identity, Product Variant definitions, Product-owned Attributes, content, media association, validation, lifecycle, publication and historical integrity under the [Approved Product Domain Specification](../domains/product/product-domain.md) and [Product Backend Specification](../backend/product/product-backend.md). Existing creation and update requirements remain binding whether a future operation is individual, bulk or migration-related. The open item commits no initial-release import, export or Product-data migration capability, source, format, schedule or reconciliation policy.

Administration can coordinate authorized Staff User and bulk activity but cannot own Product truth or bypass Product validation. Category retains taxonomy and membership authority. Search and Reporting are non-authoritative consumers and projections. CMS retains its own content lifecycle and cannot become Product source truth. Product data movement is a business-policy question distinct from technical database-schema migration governed by Architecture and backend standards. No production import/export pipeline or migration workflow is asserted to exist.

Accepted [ADR-0004](../adr/ADR-0004-catalogue-backend-specification.md), [ADR-0005](../adr/ADR-0005-post-product-backend-specification.md) and [ADR-0008](../adr/ADR-0008-post-cart-backend-specification.md) preserve this Product question rather than selecting data sources, formats, schedules, mechanisms or reconciliation policy. [`DECISIONS.md`](../../.ai/core/DECISIONS.md) governs its later Product decision lifecycle.

## Decision Question and Status

What Product-owned catalogue data import, export and migration capabilities, if any, are required for the initial offering, and what business, ownership, security, integrity and operational constraints must govern them?

The question does not presume that import, export and migration must be adopted together. **NO ALTERNATIVE SELECTED.** This 0.1.0 Proposed record is non-authoritative; `PRODUCT.md` §24 item 27 remains open. It grants no comparative evaluation, capability adoption or implementation authority.

## Alternatives Considered — Not Formally Compared

| Option | Conditional Product policy approach | Potential benefit | Limitations, risks and evidence needed |
| --- | --- | --- | --- |
| A — Existing-Control-Led Product Data Management | Consider relying on existing Approved Product creation, update and management boundaries without separately committing an initial-release import, export or Product-data migration capability. | Could keep initial data movement within already governed Product operations and limit new operational exposure. | Catalogue onboarding or legitimate transfer needs might remain unmet. Requires actual business, volume, staffing, source-quality, cost and launch-scope evidence; mandatory Product controls still apply. |
| B — Selective Product Data Movement | Consider a narrowly scoped import, export or migration need only where that specific capability is independently justified and governed. | Could address a demonstrated need without adopting all three capabilities. | A narrow scope may leave gaps or create inconsistent procedures. Requires capability-specific purpose, source ownership, validation, authorization, historical-integrity, privacy, partial-outcome, recovery and affected-owner evidence. |
| C — Coordinated Product Data Movement | Consider a broader coordinated Product data import/export/migration policy, subject to independently approved scope, ownership, validation, operational readiness and cross-domain governance. | Could support consistent treatment of several demonstrated movement needs. | Greater security, data-quality, migration, integration and support complexity. Requires evidence for each capability, cross-domain compatibility, operational capacity, cost, reversibility and explicit separation of Product, Category, CMS, Search and Reporting truth. |

All three approaches are **unselected, unranked and non-authoritative**. They do not approve a capability or technical design. Product remains authoritative under every approach; no option can bypass its validation, publication or historical requirements.

## Existing Requirements and Authority Boundaries

| Owner or function | Approved boundary relevant to a future policy |
| --- | --- |
| Product | [REQ-PRD-020, 029–031 and 034–040](../domains/product/product-domain.md) preserve Product publication truth, prohibit silent lifecycle transitions, protect historical references and require governed creation, update, Variant mutation, unpublication, deletion, validation and recoverable failure. [REQ-PRD-049–051](../domains/product/product-domain.md) preserve administration separation, server-side contextual Authorization and sensitive-data safety in imports and exports. |
| Product backend | [BPRD-REQ-037–043](../backend/product/product-backend.md) preserve governed Staff invocation, Product-owned persistence, duplicate and concurrency safety, abstract Contracts, conditional integration, explicit recovery and reconciliation, and Security/Privacy controls. They do not select an import/export Contract or migration mechanism. |
| Category | [Category Domain](../domains/category/category-domain.md) and [Category Backend](../backend/category/category-backend.md) retain taxonomy, hierarchy, classification and membership authority; a Product data movement policy cannot redefine them. |
| Administration | [REQ-ADM-016–018](../domains/admin/admin-domain.md) and [Administration Backend](../backend/admin/admin-backend.md) require per-resource authorization, distinguish partial and uncertain bulk outcomes, and prevent harmful replay. Administration coordinates but does not own Product data or gain new Staff Permissions. |
| Search | [Search Domain](../domains/search/search-domain.md) and [Search Backend](../backend/search/search-backend.md) retain non-authoritative indexing, freshness and reconciliation; a refreshed projection cannot publish or correct Product truth. |
| CMS | [CMS Domain](../domains/cms/cms-domain.md) and [CMS Backend](../backend/cms/cms-backend.md) retain CMS content and publication authority, separate from Product-owned catalogue data. |
| Reporting | [Reporting Domain REQ-RPT-008, 013–018, 026 and 041–042](../domains/reporting/reporting-domain.md) and [Reporting Backend](../backend/reporting/reporting-backend.md) govern derived, lineage-preserving reports and exports, not authoritative Product data export or mutation. |
| Identity and Security | Existing Authentication, server-side contextual Authorization, least privilege, default denial, sensitive-data protection and proportional Audit Records remain binding under [Security Standards §§9–12, 27 and 35](../../.ai/core/SECURITY-STANDARDS.md). No Role or Permission is granted here. |
| Architecture and Operations | [Architecture](../../.ai/core/ARCHITECTURE.md) governs material technical structure, cross-domain integration and technical migration decisions. Operations assesses readiness, failure, reconciliation, recovery and rollout within approved scope. Neither function obtains Product policy authority from this proposal. |

No new Data Migration Domain or ownership transfer is created. Product data movement must not be confused with the separately governed evolution of database schemas and deployment artifacts.

## Related Proposed Decisions and Dependencies

[DEC-0012](DEC-0012-administrative-role-permission-matrix.md) leaves Staff Role and Permission policy Proposed. [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md) leaves initial Category taxonomy Proposed; [DEC-0019](DEC-0019-product-variant-attribute-standards.md) leaves Variant and Attribute standards Proposed. [DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md) leaves CMS approval and scheduling Proposed. [DEC-0022](DEC-0022-reporting-exports-policy.md) leaves reporting and export catalogue policy Proposed; it does not answer Product-owned data export. None is authoritative implementation or migration permission. A later selected Product-data policy must respect these unresolved boundaries without preselecting their alternatives.

## Evidence Required Before Evaluation or Selection

Attributable Product and affected-owner evidence must establish actual business need; initial-release versus future scope for each proposed capability; catalogue data sources and ownership; source quality and completeness; Product identity and duplicate handling; Attribute and Variant consistency; Category taxonomy dependencies; historical Product references; lifecycle and publication safety; validation and conflict handling; partial success and uncertain outcomes; reconciliation and recovery; Staff access and authorization; unpublished or Sensitive Product data; Privacy and retention effects; operational capacity and support; External System dependencies; migration reversibility; cross-domain compatibility; cost and ongoing maintenance. These are evaluation questions, not selected design requirements or measured findings. No source inventory, provider diligence, volume, migration plan or stakeholder sign-off is claimed complete.

## Security, Data, Consequences and Operational Impact

A later evidence-backed policy might reduce catalogue onboarding effort or improve controlled movement of valid Product data. It could also introduce unauthorized disclosure, invalid or duplicate records, Category or Variant inconsistency, accidental publication, corrupted historical references, partial or uncertain bulk outcomes, difficult migration or rollback, External System dependence, and greater support cost. Exported or unpublished Product data may need stronger protection than public catalogue content. Every accepted Product change must preserve source authority, current Authorization, accurate outcome evidence, failure containment and historical meaning. These are conditional trade-offs, not measured benefits or an approved operating model.

## Reviewers and Future Governance Gates

Product leads the policy question. Product, Category, Administration, Search, CMS and Reporting owners; Identity, Security, Privacy/Legal, Architecture, Engineering and Operations review their affected boundaries. External-system owners or providers join only if a later proposal identifies a real dependency. Listing reviewers does not assert approval.

1. **Initial Proposed drafting:** This record frames the question and three unselected approaches only.
2. **Comparative evaluation:** Obtain separate explicit authorization and attributable business, source, risk, operational and owner evidence before comparing approaches.
3. **Security and Privacy review:** Assess access, Sensitive Data, export exposure, audit, retention and external-source risk where applicable.
4. **Product acceptance:** Select any capability and scope only with attributable evidence and affected-owner review; synchronize changed canonical Product authority.
5. **Architecture decisions:** Govern material cross-domain integration, provider, technical migration and deployment structure separately.
6. **Specification and Contract changes:** Approve any new behavior, Contract, data meaning or lifecycle effect through its owning governance.
7. **Implementation and operational rollout:** Verify data quality, partial outcomes, recovery, reconciliation, security, support and migration readiness under separate authority.

Initial drafting authorizes none of the downstream gates.

## Validation Criteria

This record remains a valid initial proposal only if: (1) it traces the exact `PRODUCT.md` §24 item 27; (2) it remains 0.1.0 Proposed, Product-owned and non-authoritative; (3) exactly three approaches remain unselected and unranked; (4) import, export and migration can be considered separately; (5) Product truth, Category taxonomy, CMS content, Search projections, Reporting exports and Administration coordination remain distinct; (6) no lifecycle, Contract, provider, Role, Permission or technical migration is selected; and (7) evaluation, acceptance, source changes and implementation remain separate gates.

## Decision Status and Explicit Non-Decisions

DEC-0037 supersedes nothing and is superseded by nothing. It selects no import, export or Product-data migration capability; initial-release scope; source; file format; field mapping; Product identifier or SKU rule; Category or Variant standard; transformation; import-validation workflow; export entitlement; Staff Role or Permission; bulk mechanism; API or Event Contract; database schema; provider; migration sequence; reconciliation implementation; rollback mechanism; schedule; automation; technical database-schema migration; deployment or rollout. It creates no Data Migration Domain, changes no Product lifecycle state, authorizes no comparative evaluation, and grants no implementation authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §24 item 27](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product question and governing authority.
- [Product Domain REQ-PRD-020, 029–031, 034–040 and 049–051](../domains/product/product-domain.md) and [Product Backend BPRD-REQ-037–043](../backend/product/product-backend.md) — Product truth, lifecycle, history, validation, Authorization and technical neutrality.
- [Category Domain](../domains/category/category-domain.md), [Category Backend](../backend/category/category-backend.md), [Administration Domain REQ-ADM-016–018](../domains/admin/admin-domain.md) and [Administration Backend](../backend/admin/admin-backend.md) — taxonomy and governed bulk coordination.
- [Search Domain](../domains/search/search-domain.md), [Search Backend](../backend/search/search-backend.md), [CMS Domain](../domains/cms/cms-domain.md), [CMS Backend](../backend/cms/cms-backend.md), [Reporting Domain](../domains/reporting/reporting-domain.md) and [Reporting Backend](../backend/reporting/reporting-backend.md) — separate content, projection and derived-export authority.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md), [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md), [DEC-0019](DEC-0019-product-variant-attribute-standards.md), [DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md) and [DEC-0022](DEC-0022-reporting-exports-policy.md) — related Proposed, non-authoritative Product decisions.
- [ADR-0004](../adr/ADR-0004-catalogue-backend-specification.md), [ADR-0005](../adr/ADR-0005-post-product-backend-specification.md) and [ADR-0008](../adr/ADR-0008-post-cart-backend-specification.md) — Accepted backend-roadmap records that leave item 27 open.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initial Proposed Product Decision Record for Product data import, export and migration requirements, preserving unselected policy alternatives, authoritative Product data, cross-domain boundaries and separate downstream governance gates. |
