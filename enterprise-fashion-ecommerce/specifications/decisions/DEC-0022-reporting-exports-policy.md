# DEC-0022 — Reporting and Exports Policy

- **Identifier:** DEC-0022
- **Title:** Reporting and Exports Policy
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

[`PRODUCT.md` §12.10](../../.ai/core/PRODUCT.md) identifies sales and Order, Payment and Refund, Inventory, Customer and Promotion reporting, and operational exports as capability areas. Its [§24 item 21](../../.ai/core/PRODUCT.md) leaves the initial reporting and export requirements open. These capability areas do not select individual reports, exports, definitions, audiences or purposes.

The [Approved Reporting Domain Specification](../domains/reporting/reporting-domain.md) REQ-RPT-001–053 and [Reporting Backend Specification](../backend/reporting/reporting-backend.md) BRPT-REQ-001–053 establish Reporting ownership of analytical definitions and outputs, while preserving each source Domain's authority. They require governed definitions, lineage, freshness, uncertainty, access control and export outcomes, but explicitly leave the initial catalogue, audiences, purposes, format and schedule unresolved. This Proposed record frames that Product choice; it is not an approved catalogue or permission grant.

## Decision Question and Product Policy Scope

What initial Product policy should govern the selection, purpose, intended audience and permitted scope of reports and exports while preserving Reporting as a governed consumer of authoritative source-Domain facts?

Product owns business priorities and the eventual selection of initial reporting purposes. Reporting owns the meaning and operational integrity of governed analytical outputs under Approved definitions. Intended audience, business need and demand for a report are not proof of current access authorization. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 21 remains open.

## Affected Requirements and Domain Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product policy | Sets later business priorities, intended purposes and catalogue choices without replacing source-Domain, Security or Privacy authority. |
| Reporting | Owns Reporting-specific definitions, versions, Projections, reports, dashboards, exports, lineage, freshness and outcomes under REQ-RPT-002–026 and BRPT-REQ-002–026; it cannot mutate source-Domain truth. |
| Customer and Identity | Customer retains Customer, Consent and Preference truth under [REQ-CUS-020–021 and 039–041](../domains/customer/customer-domain.md) and [BCUS-REQ-027–028 and 048](../backend/customer/customer-backend.md). Identity retains Principal and access-security boundaries under [REQ-IDN-028](../domains/identity/identity-domain.md) and the [Identity Backend Specification](../backend/identity/identity-backend.md). Reporting cannot derive identity or authorization from an analytical association. |
| Order, Payment and Return | Order owns commercial records and lifecycle; Payment owns Payment and Refund execution evidence; Return owns Return assessment and lifecycle. REQ-RPT-027–031 and 033 preserve their distinct outcomes, including failure, uncertainty and historical meaning. See the [Order](../domains/order/order-domain.md), [Payment](../domains/payment/payment-domain.md) and [Return](../domains/return/return-domain.md) Specifications and their [Order](../backend/order/order-backend.md), [Payment](../backend/payment/payment-backend.md) and [Return](../backend/return/return-backend.md) backends. |
| Inventory, Product and CMS | Reporting consumes governed evidence without calculating current Available-to-Sell, changing Product or Product Variant truth, or establishing CMS publication state under REQ-RPT-032–034. See [Inventory](../domains/inventory/inventory-domain.md), [Product](../domains/product/product-domain.md), [CMS](../domains/cms/cms-domain.md), and their [Inventory](../backend/inventory/inventory-backend.md), [Product](../backend/product/product-backend.md) and [CMS](../backend/cms/cms-backend.md) backend Specifications. |
| Administration | May present stale-capable Reporting outputs and coordinate protected exports without becoming Reporting or source-Domain authority under [REQ-ADM-035–036](../domains/admin/admin-domain.md) and [BADM-REQ-035–036](../backend/admin/admin-backend.md). |

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Minimal operational reporting | Consider a tightly bounded initial set of reports and exports justified by immediate, attributable operational needs. | May limit early data exposure and definition-maintenance burden. | Could omit a material business view; no report, export, audience or purpose is selected. |
| B — Domain-prioritized reporting | Consider initial reports and exports by separately justified source-Domain and business priorities. | May make domain accountability and source lineage easier to review. | Cross-domain views and competing priorities need explicit reconciliation; no Domain package or priority order is selected. |
| C — Purpose-and-audience-led reporting | Consider an initial catalogue from attributable business purposes and intended audiences, subject to lawful use and current authorization. | May expose unjustified data collection or distribution early in review. | Audience requests can over-expand scope or be mistaken for permission grants; no audience or access policy is selected. |

These candidates are unselected and unranked. None authorizes a report, export, financial calculation, access grant, data reuse, or implementation. Comparative claims are hypotheses, not stakeholder confirmation or measured results.

## Report Catalogue, Export Catalogue and Intended Audiences

A report catalogue would identify governed analytical definitions and permitted presentation purposes. An export catalogue would separately identify which defined data, if any, may be extracted for an approved purpose and scope. A report's existence does not imply its export eligibility, while an operational request for a file does not establish an Approved Reporting definition. Both catalogues require distinct selection and review; neither is selected here.

An intended audience describes a proposed business use. It is not a Role, Permission, assignment, Principal, lawful basis, or authorization decision. Every protected read and export must still meet current trusted server-side contextual Authorization for Principal, Resource, action, purpose and state under REQ-RPT-039 and BRPT-REQ-039. [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) remains Proposed and supplies no accepted administrative matrix or access grant.

## Evaluation Criteria and Consequences

Later Product-led comparison must evaluate attributable business value; source authority and lineage; definition consistency and historical reproducibility; freshness, completeness and uncertainty; correction and reconciliation; lawful purpose, Consent where applicable, minimization and retention; contextual authorization and object isolation; auditability; security and disclosure risk; accessibility; operational cost and workload isolation; and reversibility. It must compare report utility separately from export exposure and distribution burden.

Minimal scope may ease review but leave unmet operational needs. Domain-prioritized scope may improve ownership clarity but complicate cross-domain questions. Purpose-and-audience-led scope may clarify use while increasing privacy and access-review effort. No benefit, cost or priority is asserted as measured evidence. REQ-RPT-043–047 and BRPT-REQ-043–047 remain binding under every option.

## Data Ownership, Financial and Commercial Consistency

Reporting Facts are analytical representations, not originating Domain facts or permission to update them. REQ-RPT-008 and 013–026 require source lineage, definition versions, temporal context, duplicate and conflict safety, correction, recovery and reconciliation. A Projection or dashboard must not substitute for current authoritative reads where source truth is required. Order, Payment, Return, Inventory, Product and CMS retain their separate lifecycle meanings.

Any future financial report must preserve Order versus Payment versus Return and Refund outcomes under REQ-RPT-027–031. Gross Revenue, Net Revenue, Average Order Value, tax, shipping, Discount, Promotion, Voucher, cancellation, Chargeback and credit treatment require separately Approved definitions. This record selects no formula, KPI, target, Currency conversion, recognition rule, tax interpretation or financial entitlement. Proposed commercial and catalogue decisions [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md), [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md), [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md), [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md) and [DEC-0019](DEC-0019-product-variant-attribute-standards.md) cannot be treated as accepted inputs.

## Customer Data, Privacy and Security Impact

REQ-RPT-036–039 and BRPT-REQ-036–039 require Customer/Identity separation, governed Consent and Preference, purpose limitation, minimization, population isolation, masking where governed, aggregation safety, enumeration resistance and contextual Authorization. [SECURITY-STANDARDS.md §35](../../.ai/core/SECURITY-STANDARDS.md) additionally requires an approved purpose, access and retention handling for Sensitive Data; bulk exports must be authorized, auditable, bounded, encrypted and expiring. A purchase, Account, Session or analytical match does not establish Consent, identity or export eligibility.

Product interest in Customer analysis cannot override lawful purpose or Privacy/Legal review. This record selects no personal-data population, lawful basis, Consent category, retention period, deletion rule, external recipient, cross-border destination, exception or access grant. Protected Reporting and Administration operations continue to require default denial, least privilege and proportional Audit Records.

## Exports, Delivery and Distribution

REQ-RPT-041–042 and BRPT-REQ-041–042 require each governed export to preserve purpose, definition, scope, filters, time context, lineage, freshness, authorization, minimization and an explicit outcome. Requested, accepted, processing, complete, partial, failed, expired where governed, unavailable and uncertain outcomes cannot be collapsed into success; file visibility alone proves neither completeness nor authorized access. Administrative export remains bounded by REQ-ADM-035.

Any later distribution choice must distinguish making a governed result available to an authorized requester from sending it to a recipient or provider. This proposal selects no file format, destination, delivery channel, schedule, retention period, expiry duration, provider, transfer mechanism or new Contract. Reporting outputs do not become source-Domain facts when delivered or downloaded.

## Freshness, Correction and Historical Traceability

REQ-RPT-011–026 and BRPT counterparts require definition versioning, source lineage, freshness and source cutoffs, explicit partial or uncertain outcomes, safe correction, recovery and reconciliation. A historical report must remain explainable under the definition and source context used; later source correction must not silently rewrite prior meaning. A stale, incomplete or conflicting output must not be presented as current authoritative truth. This record sets no refresh interval, service target, correction deadline, processing order or reconciliation mechanism.

## Data, Compatibility, Migration and Operational Impact

An eventual catalogue may require governed source Contracts, definition-version compatibility, access review, privacy controls, operational support and migration of existing analytical representations. Any change must preserve source-Domain history and avoid treating an old export as current truth. REQ-RPT-043–049 and BRPT counterparts govern bounded workload, accessibility, observability, audit, Contract and conditional-event boundaries. No warehouse, database, schema, API, event, file, visualization tool, scheduler, processing engine, migration plan or provider is selected here.

## Evidence Required Before Selection

Product and accountable business owners must provide attributable intended decisions or operational uses for each candidate report and export, identify proposed audiences and explain why a report view versus an extract is needed. Reporting and each affected source Domain must confirm available source evidence, definition feasibility, lineage, freshness and correction implications. Finance must review financial definitions and reconciliation where applicable. Customer, Identity, Security and Privacy/Legal must review personal-data purposes, authorization, minimization, retention and disclosure. Administration and Operations must review protected use, support burden and auditability; UX/accessibility must review presentation. No stakeholder sign-off, production demand measurement or comparative result is asserted here.

## Open Questions and Acceptance Prerequisites

1. Which initial business questions justify governed reports, and which separately justify exports? No catalogue is selected.
2. What attributable purpose and proposed audience applies to each candidate, and what independent authorization and Privacy review would be required?
3. Which source-Domain facts, definition versions, exclusions and correction rules would a candidate result require without inventing source authority?
4. Which financial definitions can be considered only after applicable tax, cancellation, Return, Refund, promotion and credit policies are resolved?
5. What freshness and completeness distinctions are necessary for intended use without selecting a schedule or guarantee?
6. What lawful purpose, Consent where applicable, minimization, expiry and retention obligations govern a proposed export? No period is selected.
7. Would any external delivery or distribution be justified, and which separate Security, Privacy, Contract and provider decisions would it require?
8. Which later Specification, Contract, Architecture, Security and implementation reviews would follow an Accepted policy? None is authorized now.

Product leads policy comparison and acceptance with Reporting, affected source-Domain owners, Administration, Identity, Operations, Security and Privacy/Legal review; Finance and UX/accessibility review apply to relevant financial or presentation choices. Formal comparison requires separately attributable purpose, audience, source and risk evidence. Acceptance follows [`PRODUCT.md` §§24–25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires separate applicable Specification, Contract, Architecture, Security and Engineering gates.

## Decision Status and Explicit Non-Authorizations

DEC-0022 supersedes nothing and is superseded by nothing. It remains **0.1.0 Proposed, Product-owned and non-authoritative**. It does not select Option A, B or C; a report or export catalogue; business purpose; Metric or KPI definition, formula or target; financial calculation; intended or authorized audience; Role, Permission or assignment; Customer population or lawful basis; retention period; file format; distribution route; provider; schedule; API, Event or other Contract; schema; data store; processing mechanism or implementation. It does not close `PRODUCT.md` §24 item 21 or grant access, data reuse, policy acceptance or implementation authority.

[DEC-0012](DEC-0012-administrative-role-permission-matrix.md) and [DEC-0013–0021](../../.ai/core/DECISIONS.md) remain Proposed and non-authoritative. Their unresolved administrative, guest, commercial, catalogue, CMS and availability policies cannot be imported as settled Reporting definitions.

## References

- [AGENTS.md §5](../../.ai/core/AGENTS.md), [PRODUCT.md §§12.10, 24 item 21, 25 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md §26](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md §§12 and 35](../../.ai/core/SECURITY-STANDARDS.md) — decision governance, ownership, terminology and protection.
- [Reporting Domain REQ-RPT-001–053](../domains/reporting/reporting-domain.md) and [Reporting Backend BRPT-REQ-001–053](../backend/reporting/reporting-backend.md) — Approved Reporting boundaries, definitions, uncertainty, authorization and exports.
- [Customer Domain](../domains/customer/customer-domain.md) and [Backend](../backend/customer/customer-backend.md); [Identity Domain](../domains/identity/identity-domain.md) and [Backend](../backend/identity/identity-backend.md) — Customer, Consent, Preference, Identity and access boundaries.
- [Order Domain](../domains/order/order-domain.md) and [Backend](../backend/order/order-backend.md); [Payment Domain](../domains/payment/payment-domain.md) and [Backend](../backend/payment/payment-backend.md); [Return Domain](../domains/return/return-domain.md) and [Backend](../backend/return/return-backend.md) — separate commercial and financial truth.
- [Inventory Domain](../domains/inventory/inventory-domain.md) and [Backend](../backend/inventory/inventory-backend.md); [Product Domain](../domains/product/product-domain.md) and [Backend](../backend/product/product-backend.md); [CMS Domain](../domains/cms/cms-domain.md) and [Backend](../backend/cms/cms-backend.md) — source ownership and historical meaning.
- [Administration Domain REQ-ADM-035–036](../domains/admin/admin-domain.md) and [Backend BADM-REQ-035–036](../backend/admin/admin-backend.md) — protected export and non-authoritative Reporting display.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) and [DEC-0014](DEC-0014-order-cancellation-eligibility-cutoff-policy.md), [DEC-0016](DEC-0016-returns-exchanges-refund-policy.md), [DEC-0017](DEC-0017-voucher-promotion-stacking-policy.md), [DEC-0018](DEC-0018-initial-catalogue-taxonomy-policy.md), [DEC-0019](DEC-0019-product-variant-attribute-standards.md), [DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md), [DEC-0021](DEC-0021-stock-availability-messaging-policy.md) — Proposed, non-authoritative dependencies; no policy import.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed three unselected approaches to initial Reporting and export scope while preserving source-Domain authority, distinct catalogues, purpose-versus-access separation, financial and privacy safeguards, freshness and historical integrity, unresolved Product item 21, and separate evidence, acceptance and implementation gates. |
