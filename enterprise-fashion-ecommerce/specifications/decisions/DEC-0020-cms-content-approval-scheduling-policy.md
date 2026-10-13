# DEC-0020 — CMS Content Approval and Scheduling Policy

- **Identifier:** DEC-0020
- **Title:** CMS Content Approval and Scheduling Policy
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

[`PRODUCT.md` §24 item 22](../../.ai/core/PRODUCT.md) leaves CMS content approval and scheduled-publication workflow open. Its §32.5 already requires policy content affecting customer rights or obligations to be versioned and approved before publication, without choosing a CMS approver or approval chain. The [Approved CMS Domain Specification](../domains/cms/cms-domain.md) REQ-CMS-011–016 requires draft isolation, permits approval-readiness evidence without inventing approval policy, distinguishes explicit authorized publication from storage or scheduling, and treats scheduling as intent rather than guaranteed execution. REQ-CMS-017–019 preserves attributable withdrawal, correction and publication history. [CMS Backend BCMS-REQ-012–015](../backend/cms/cms-backend.md) specializes these same boundaries.

CMS owns editorial and policy presentation. Product, Category, Pricing and other owning Domains retain their respective facts; approval of CMS wording cannot approve or change an underlying business policy. [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) remains Proposed and establishes no administrative Role or Permission matrix. Existing requirements are binding, while the approval and scheduling policy itself remains unresolved.

## Decision Question and Policy Scope

What initial CMS content approval and scheduling policy should govern editorial and policy content, including approval-readiness evidence, publication eligibility, scheduling intent, exception handling and accountable oversight, without assuming unresolved administrative Roles or Permissions?

This Product Decision concerns business approval obligations for CMS Content and customer-facing publication intent. It does not determine named approvers, staff access, a repository Decision Record's acceptance process, publication timing guarantees or workflow technology. **NO OPTION SELECTED.** `PRODUCT.md` §24 item 22 remains open.

## Alternatives Considered — Not Formally Compared

| Option | Policy-level candidate | Potential benefit | Limitation or trade-off requiring evidence |
| --- | --- | --- | --- |
| A — Single-stage governed approval | Require one policy-governed approval stage before eligible CMS Content may be published. | May simplify editorial handling and accountability. | May provide insufficient independent scrutiny for sensitive policy content; no approver or approval-chain depth is selected. |
| B — Risk-tiered approval | Vary approval obligations by separately governed content sensitivity or customer impact. | May focus scrutiny where errors are most consequential. | Classification, consistency and exception handling add policy and operational burden; no tier, approver or threshold is selected. |
| C — Multi-stage governed approval | Require multiple policy-governed stages where accountable evidence justifies them. | May improve review of material or customer-impacting content. | Can delay publication and increase handoff complexity; no number of stages, sequence or organizational Role is selected. |

The alternatives do not change the binding public ineligibility of unapproved content. They have not been compared, preferred or Accepted. None grants an administrator an approval capability or bypasses owning-domain authorization.

## Affected Requirements and Ownership

| Authority | Approved boundary preserved |
| --- | --- |
| Product | Owns the content-approval and scheduling policy, including customer-impact and business-accountability criteria. |
| CMS | Owns CMS Content versions, approval-readiness evidence, authorized publication outcomes, scheduling intent, withdrawal, correction and publication history under REQ-CMS-011–019. |
| Owning Domains | Product, Category, Pricing and other owners retain source facts, eligibility and policy authority; CMS presentation cannot establish or amend them. |
| Administration and Identity | Administration coordinates permitted staff operations; Identity and owning Domains retain access and contextual Authorization boundaries. Proposed DEC-0012 selects no Roles, Permissions or assignments. |

Approval-readiness evidence is not final CMS approval or publication authority. Published content requires an explicit, currently authorized CMS outcome for an eligible version. Missing, stale, conflicting or uncertain validation, authorization, source evidence or policy must cause safe rejection or withholding rather than fabricated success.

## Scheduling-Policy Considerations

A future policy must distinguish immediate publication intent from scheduled publication intent and define when scheduling is eligible under current content, source and authorization evidence. Changes or withdrawal of pending intent, and delayed, missed, duplicate, reordered or uncertain execution, must remain explicit and recoverable without treating the intended time or a scheduler acknowledgement as a confirmed public outcome. The review must address timezone and effective-date meaning where material, but this record chooses no time rule, timezone policy, delay, retry count or scheduler.

Withdrawal and correction require attributable CMS-owned outcomes and preservation of previous Content versions, provenance, uncertainty and publication evidence. Neither a changed schedule nor an edited draft may silently rewrite what was previously public. Scheduling, storage, rendering, cache state and provider acknowledgement each remain distinct from successful publication.

## Comparative Evaluation Criteria and Consequences

Product-led comparison must examine customer impact and policy-content sensitivity; evidence sufficient for publication eligibility; separation of duties; editorial usability and accessibility; operational complexity; auditability; recovery after missed or uncertain execution; historical integrity and reversibility; and dependency on unresolved administrative access policy. A single stage may reduce handoffs but concentrate risk; risk-tiering may allocate review effort while adding classification disputes; multiple stages may improve scrutiny but delay corrections. These are hypotheses, not current Product policy or measured findings.

## Security, Administration and Policy-Content Safeguards

Protected CMS actions require trusted server-side contextual Authorization, default denial, least privilege, safe disclosure and proportionate Audit Records under [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md). This record grants no Role, Permission, self-approval, emergency bypass or privilege escalation. A policy exception cannot be inferred from a staff title or scheduled job. Product §32.5's versioning and pre-publication approval safeguard for content affecting rights, delivery, cancellation, Returns, Refunds, privacy or Payment remains binding under every alternative; CMS approval of wording cannot accept the underlying Product, Legal or Security policy.

## CMS Content Approval Versus Repository Decision Acceptance

CMS content approval determines whether a particular Content version may become eligible for publication under an eventual approved CMS policy. Acceptance of a repository Product Decision Record follows [`DECISIONS.md` §§8–17](../../.ai/core/DECISIONS.md) and canonical Product governance. Neither process substitutes for the other: an Accepted Product Decision does not prove a CMS version was reviewed and published, while a CMS approval cannot make an unresolved Product Decision binding.

## Evidence Required Before Selection

Product and accountable content owners must provide attributable editorial and policy-content scenarios, customer-impact distinctions, correction urgency and desired accountability. CMS and Operations must assess versioning, readiness evidence, publication exceptions, scheduling intent, missed execution and support capacity. UX/accessibility must assess editorial and customer comprehension. Security and Administration must review separation of duties and protected action boundaries without fabricating DEC-0012's unresolved matrix. Relevant Product, Legal, Privacy and owning-Domain authorities must review sensitive policy content. No stakeholder approval, operational measurement or exception is claimed complete here.

## Open Questions and Acceptance Prerequisites

1. Which content, if any, warrants differentiated approval obligations, and what attributable evidence supports that distinction?
2. What evidence establishes approval-readiness and final CMS approval for a version without treating a draft, schedule or cached representation as authorization?
3. How should pending scheduling intent be changed, withdrawn or superseded, and how should missed, duplicate, delayed or uncertain execution be investigated?
4. Which timezone and effective-date semantics would an eventual Product policy require, if any? No timing guarantee or mechanism is selected.
5. Which actions require separation of duties, and how will they remain conditional on the later governed Role and Permission matrix in DEC-0012?
6. How will approved underlying policy and CMS presentation be kept consistent without transferring source-Domain authority to CMS?
7. Which later Contract, Architecture, Security and operational reviews would an Accepted policy require? None is authorized here.

Product leads policy selection with CMS, Operations, UX, Security and Administration review; owning policy Domains, Legal and Privacy participate where material. Formal comparison requires separately governed scope and attributable evidence. Acceptance follows [`PRODUCT.md` §§25 and 38](../../.ai/core/PRODUCT.md) and [`DECISIONS.md` §§8–17 and 29](../../.ai/core/DECISIONS.md), followed by canonical Product synchronization. Implementation requires separately applicable Specification, Contract, Architecture, Security and Engineering gates.

## Explicit Non-Authorizations and Supersession

DEC-0020 supersedes nothing and is superseded by nothing. It does not select Option A, B or C; an approver identity, Role, Permission, approval-chain depth, risk tier, policy exception, publication time, timezone rule, scheduler, retry, provider, workflow implementation, API, Event Contract or schema. It does not grant publication eligibility from approval-readiness evidence, scheduling or storage; guarantee publication; accept an underlying Product Decision; resolve DEC-0012; close `PRODUCT.md` §24 item 22; claim stakeholder review; change an Approved Specification; or authorize implementation.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md), [PRODUCT.md §§24 item 22, 25, 32.5 and 38](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §9](../../.ai/core/ARCHITECTURE.md), [DECISIONS.md §§8–17 and 29](../../.ai/core/DECISIONS.md), [GLOSSARY.md](../../.ai/core/GLOSSARY.md), [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md), and [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md) — governance, ownership, terminology and protected operations.
- [CMS Domain REQ-CMS-011–019](../domains/cms/cms-domain.md) and [CMS Backend BCMS-REQ-012–015](../backend/cms/cms-backend.md) — draft isolation, readiness, publication, scheduling and history.
- [DEC-0012](DEC-0012-administrative-role-permission-matrix.md) — Proposed administrative Role and Permission policy, not approval-chain or access authority.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-13 | Proposed | Framed unselected single-stage, risk-tiered and multi-stage CMS content-approval alternatives and scheduling-policy questions while preserving draft isolation, explicit authorized publication, scheduling uncertainty, policy-content safeguards, DEC-0012 as unresolved, and separate evidence, acceptance and implementation gates. |
