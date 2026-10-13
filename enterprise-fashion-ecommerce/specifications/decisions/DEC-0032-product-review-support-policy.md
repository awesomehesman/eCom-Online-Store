# DEC-0032 — Product-Review Support Policy

- **Identifier:** DEC-0032
- **Title:** Product-Review Support Policy
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

[`PRODUCT.md` §24 item 15](../../.ai/core/PRODUCT.md) explicitly leaves “Product-review support” unresolved. No initial customer-facing Product-review capability has been selected for Version 1. The [Approved Product Domain Specification §38](../domains/product/product-domain.md) says reviews are not added to Product ownership or Version 1 behavior; the [Product Backend Specification](../backend/product/product-backend.md) neither introduces reviews nor assigns their ownership or policy. Product's existing catalogue authority is not authority over customer-generated review content.

The [CMS Domain Specification](../domains/cms/cms-domain.md) defines no Product-review or moderation model; its [Backend Specification](../backend/cms/cms-backend.md) selects none. The [Search Domain Specification](../domains/search/search-domain.md) and [Search Backend Specification](../backend/search/search-backend.md) select no review evidence input, ranking or discovery effect. The [Administration Domain Specification](../domains/admin/admin-domain.md) and [Administration Backend Specification](../backend/admin/admin-backend.md) provide no independent review truth or moderation authority. Existing [Customer](../domains/customer/customer-domain.md), [Identity](../domains/identity/identity-domain.md) and [Order](../domains/order/order-domain.md) authority does not, alone or in combination, authorize a review submission. Existing [ADRs](../adr/ADR-0015-post-return-backend-specification.md) preserve the open decision rather than selecting a capability.

This record makes the Product question and missing governance visible. It does not assert that reviews, a moderation service, an owning Review Domain, staff capacity or customer demand already exist. A durable Accepted Product decision is required before any customer-facing capability commitment, with later ownership and affected-domain governance where adoption is chosen.

## Decision Question and Status

Should the initial customer-facing Product offering support Product reviews, and, if so, within what approved capability and governance boundaries?

**NO ALTERNATIVE SELECTED.** This record documents candidates and prerequisites only. It creates no Version 1 commitment, does not authorize comparative evaluation, and leaves `PRODUCT.md` §24 item 15 open.

## Alternatives Considered — Not Formally Compared

| Option | Product policy candidate | Potential benefit | Limitation and evidence required |
| --- | --- | --- | --- |
| A — No Initial Product-Review Capability | Consider excluding customer-facing Product-review submission and publication from the initial offering. | Could keep initial scope bounded while review-content ownership and moderation remain unresolved. | No initial customer-review capability; later adoption would require new Product and affected-owner approval. Evidence needed: launch priorities, customer needs, business implications, and future adoption or migration considerations. |
| B — Bounded Initial Product-Review Capability | Consider a limited customer-facing capability only after separate approval of ownership, submission eligibility, moderation, publication and removal controls. | Could provide a constrained path to customer feedback and Product discovery. | Introduces content, privacy, abuse, support and operational responsibilities. Evidence needed: accepted content owner, eligibility and any purchase-verification policy, moderation and removal authority, visibility controls, Security/Privacy/Legal review, and support capacity. |
| C — Broader Product-Review Capability | Consider broader review behavior only after additional approved publication, moderation, abuse, reporting and presentation policies. | Could support richer customer feedback and discovery experiences. | Greater governance, moderation, operational and abuse-handling complexity. Evidence needed: all relevant B evidence plus broader Product requirements, moderation capacity, reporting controls, Search/storefront impacts, privacy and operational feasibility. |

The three alternatives remain **unselected, unranked and non-authoritative**. No numerical rating, moderation threshold, review score, ranking formula or eligibility rule is established. None is presumed feasible or preferred.

## Authority Boundaries

| Authority | Existing boundary and unresolved review question |
| --- | --- |
| Product | Owns capability adoption and customer-facing policy. Catalogue facts and Product publication do not imply ownership of customer-generated review content. |
| Customer | Retains Customer context and data authority. It is not assigned review-content ownership or submission eligibility here. |
| Identity | Retains Authentication and identity authority. Authentication or an Account alone does not establish purchase verification or review eligibility. |
| Order | Retains authoritative Order and confirmed purchase evidence. Even valid purchase evidence does not independently grant permission to submit a review. |
| CMS | Retains existing editorial and presentation scope; no review moderation, review-content ownership or publication authority is assigned. |
| Search and Discovery | Retains governed discovery and Projection boundaries; no review-derived indexing, ranking, scoring or discovery authority is established. |
| Administration | Coordinates only governed capabilities under contextual Authorization; no moderation Role, Permission, review truth or owner authority is created. |
| Reporting | Retains governed reporting boundaries; no review analytics, moderation report or data-access entitlement is established. |
| Notifications | Any review-related communication requires separately approved purpose, communication policy and owning-domain coordination. No delivery behavior is selected. |
| Security and Privacy | Existing Authentication, contextual Authorization, data protection, abuse prevention and audit safeguards remain mandatory where applicable. |

No Review Domain, new Module or cross-domain transfer of content ownership is introduced. A later adoption decision must explicitly assess authoritative review-content ownership rather than infer it from Product, CMS or Customer responsibilities.

## Unresolved Prerequisites and Security/Privacy Review

Product policy remains open on adoption and Version 1 inclusion; submission eligibility; Identity and Account requirements; whether verified purchase is required; review content and permitted fields; editing and correction; publication, visibility, removal and appeals; moderation criteria and authority; customer transparency; and storefront presentation or aggregation. Ownership remains open for review content, moderation, audit, retention and deletion. Order purchase-evidence source and validation, staff Authorization, operational accountability and support capacity remain to be established. Search indexing and ranking, Reporting access and possible Notifications behavior are further conditional choices.

Future Security, Privacy and Legal review must assess impersonation, fabricated purchase claims, spam or manipulated reviews, abusive or unlawful content, personal-data disclosure, contextual Authorization, moderation and removal permissions, reporting access, auditability, retention, correction, deletion, customer transparency, and propagation of removed or corrected content to Search and storefront representations. These are review questions, not a selected moderation workflow, fraud system or technical control.

Product policy questions must be separated from later Domain ownership, Architecture, Contract, Security-detail and implementation decisions. Missing answers cannot be converted into assumed approved behavior by a UI, data model, CMS publication flow or Search ranking input.

## Future Evaluation Criteria and Consequences

A separately authorized evaluation should consider Product value and customer need; launch scope and delivery impact; ownership and accountability; submission eligibility and purchase-evidence integrity; moderation feasibility; publication and removal governance; Security, Privacy, Legal and content risk; abuse handling; operational support capacity; Search and storefront effects; Reporting implications; cost, maintainability, reversibility and later adoption. Criteria are not evidence, a comparison, or a finding of suitability.

Recording this unresolved choice improves traceability, exposes ownership and moderation gaps, prevents accidental Version 1 commitments, preserves existing Domain boundaries and identifies later review prerequisites. Its limit is equally explicit: no capability is approved, no review owner is assigned, no moderation model is established and no implementation may proceed from this record.

## Reviewers and Future Governance Gates

Product leads. Customer, Identity, Order, CMS, Search and Discovery, Administration, Security, Privacy/Legal, Reporting, Engineering and Operations must review their respective boundaries. Notifications joins if review-related communications are proposed. Listing a reviewer does not assert review or approval.

1. Gather attributable Product and affected-owner evidence.
2. Assess and govern authoritative ownership of customer-generated review content.
3. Resolve submission, moderation, publication, removal, Security, Privacy and Legal considerations as applicable.
4. Seek separate authorization for comparative evaluation only if sufficient evidence exists.
5. Obtain Product selection and affected-owner review without assuming any candidate is preferred.
6. Complete formal Product Decision acceptance.
7. Synchronize accepted policy into canonical `PRODUCT.md` guidance.
8. Authorize any necessary Domain, Backend, Architecture, Contract or Security changes separately.
9. Assess implementation eligibility under the resulting authority.
10. Complete applicable testing, operational readiness and rollout approval.

No gate is deemed completed by this Proposed record.

## Validation Criteria

Review of this record passes only if: (1) DEC-0032 is 0.1.0 Proposed, Product-owned and non-authoritative; (2) `PRODUCT.md` §24 item 15 remains open; (3) exactly three alternatives remain unselected and unranked; (4) no Version 1 capability is committed; (5) review-content ownership, submission eligibility, moderation and publication remain unresolved; (6) Product, Customer, Identity, Order, CMS, Search, Administration and other affected authority is preserved; (7) no Review Domain is invented; and (8) no evaluation, selection, acceptance or implementation is authorized.

## Decision Status and Explicit Non-Decisions

DEC-0032 remains **0.1.0 Proposed, Product-owned and non-authoritative**. It supersedes nothing and is superseded by nothing. It selects no Product-review adoption or Version 1 inclusion; review-content ownership; submission eligibility; verified-purchase requirement; rating scale, score or aggregation; moderation rule or owner; publication, removal, visibility or retention policy; Search ranking effect; Reporting or Notification behavior; Staff Role or Permission; provider or External System; API, Contract, Event, Message, database schema, Architecture, Module, technical mechanism or implementation. It creates no Review Domain and changes no Approved Specification.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §24 item 15](../../.ai/core/PRODUCT.md), [DECISIONS.md](../../.ai/core/DECISIONS.md), [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md) and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — Product decision and cross-cutting boundaries.
- [Product Domain §38](../domains/product/product-domain.md) and [Product Backend](../backend/product/product-backend.md) — catalogue authority and explicit review non-selection.
- [Customer Domain](../domains/customer/customer-domain.md), [Identity Domain](../domains/identity/identity-domain.md) and [Order Domain](../domains/order/order-domain.md) — distinct customer, identity and purchase-evidence authority.
- [CMS Domain](../domains/cms/cms-domain.md), [Search Domain](../domains/search/search-domain.md) and [Administration Domain](../domains/admin/admin-domain.md) — no implicit review moderation, discovery or administrative authority.
- [Reporting Domain](../domains/reporting/reporting-domain.md) and [Notifications Domain](../domains/notifications/notifications-domain.md) — conditional downstream consumption only.
- [ADR-0004](../adr/ADR-0004-catalogue-backend-specification.md) and [ADR-0015](../adr/ADR-0015-post-return-backend-specification.md) — existing Accepted roadmap records that do not select review capability.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-14 | Proposed | Initially framed Product-review capability with three unselected alternatives and unresolved review-content ownership, submission eligibility, moderation, publication, privacy and affected-domain dependencies. |
