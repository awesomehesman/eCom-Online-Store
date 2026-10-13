---
title: DEC-0012 Administrative Responsibility Evidence
version: 0.5.0
status: Draft
owner: Product
last_updated: 2026-10-13
authoritative: false
---

# DEC-0012 Administrative Responsibility Evidence

## 1. Purpose and authority boundary

This Draft prepares attributable evidence collection for [DEC-0012](../../DEC-0012-administrative-role-permission-matrix.md). It inventories repository-derived administrative task candidates, Product direction supplied by the requester, separate proposals for Product review, and the questions that Administration, Identity, Security, Operations, and affected Domain owners must answer. It records **no independently confirmed stakeholder operational fact**. It is neither a comparison of DEC-0012 alternatives nor a permission policy, and it cannot make the Proposed decision Accepted. The current evidence-sufficiency gate in §9 is **not satisfied**.

## 2. Governing sources

- [AGENTS.md §5](../../../../.ai/core/AGENTS.md) and [DECISIONS.md §§3, 8, 25 and 49–50](../../../../.ai/core/DECISIONS.md): authority hierarchy, Proposed non-authority, Product-source synchronization, and the DEC-0012 index boundary.
- [PRODUCT.md §§7.3–7.8, 12.5–12.9, 15.1–15.5 and 24 item 23](../../../../.ai/core/PRODUCT.md): illustrative Staff personas, administrative areas and journeys, and the unresolved matrix. Other open Product decisions remain open.
- [SECURITY-STANDARDS.md §§10–12 and 27](../../../../.ai/core/SECURITY-STANDARDS.md): mandatory Role/Permission, Authentication, RBAC, contextual Authorization, privilege-lifecycle, and audit controls.
- [ARCHITECTURE.md §§9 and 35](../../../../.ai/core/ARCHITECTURE.md): Identity and Administration ownership, owning-Domain separation, and the completed backend-specification roadmap.
- [Approved Administration Domain Specification, especially REQ-ADM-005–015, 023–045 and §7](../../../domains/admin/admin-domain.md), [Approved BADM, especially BADM-REQ-005–015 and 023–045](../../../backend/admin/admin-backend.md), and [Approved Administration Frontend Specification, especially REQ-FAD-020 and 030–033](../../../frontend/administration/administration-frontend.md): coordination and presentation without access-policy or owning-Domain authority.
- [Approved Identity Backend Specification, especially BIDN-REQ-004 and 024–026](../../../backend/identity/identity-backend.md): Identity-owned access evidence and assignments without business Authorization authority.
- [GLOSSARY.md §§13 and 22](../../../../.ai/core/GLOSSARY.md) and [DOCUMENTATION-STANDARDS.md §§7–9](../../../../.ai/core/DOCUMENTATION-STANDARDS.md): canonical terms and Draft lifecycle.

The repository currently has no Administration-specific executable Contract. Existing Customer Registration and Identity Login Contracts do not define this matrix. No Contract behavior is inferred from the task examples below.

## 3. Classification and provenance rules

| Class | Meaning and permitted use |
| --- | --- |
| **BR — Binding repository requirement** | An Accepted/Approved or canonical requirement constraining any later policy. Cite exact source; do not treat it as an answer to an open Product question. |
| **RC — Repository-derived candidate or example** | A possible task or persona extracted from a cited repository source. It is not confirmed current operational practice, an assigned permission, or evidence that a supporting executable capability exists. |
| **SC — Independently stakeholder-confirmed operational fact** | A dated, attributable statement from the accountable function, with source or review record, scope, and any limitations, independently confirming actual practice or an owner boundary. None is recorded in this Draft. |
| **UQ — Unresolved question** | A policy, actor, owner, sensitivity, or applicability question awaiting attributable evidence; no implied answer. |
| **DS — Disputed or superseded evidence** | Conflicting or replaced claims retained with provenance, disposition, and unresolved impact. None is recorded in this Draft. |
| **PD — User-confirmed Product direction** | A choice explicitly supplied by the requester for this 2026-10-13 Product-direction update. It records intended direction, not independently confirmed Operations practice, Security/Identity/Domain approval, an Accepted DEC-0012 policy, or executable authority. |
| **PR — Product-review proposal** | A recommendation supplied for Product and affected-owner review in this 2026-10-13 update. It is not a confirmed choice, operational fact, Role assignment, permission grant, or approved policy. |

The inventory's `RC` classification applies to **every** original task row; its linked BR sources constrain interpretation but do not confirm the task's operational use. The PD and PR overlays in §4 do not overwrite those provenance rows. Candidate actors are illustrative Product personas, not organizational assignments. “Owning Domain” identifies the authority that would govern the underlying business fact or action; Administration may coordinate only. Review status `Open; no SC` means no independent operational confirmation or sign-off exists, even where PD is recorded. A later collector must record who supplied evidence, when, the exact claim, its scope, contradictions, and the accountable confirmation function before changing a classification. An unanswered permission-boundary question cannot be converted into a permission name or grant.

## 4. Administrative task inventory

The two tables form one record per stable ID. The first preserves provenance and candidate context; the second records the required questions and review state. Sensitive-effect entries identify **review concerns**, not final risk classifications. Every row is `RC`, with no stakeholder confirmation.

| ID | Candidate task or action | Candidate actor/persona | Owning Domain | Exact repository source | Class |
| --- | --- | --- | --- | --- | --- |
| ARE-001 | Prepare Product and Product Variant information | Store Administrator | Product | [PRODUCT §§7.3, 12.5, 15.1](../../../../.ai/core/PRODUCT.md); [REQ-ADM-023](../../../domains/admin/admin-domain.md) | RC |
| ARE-002 | Prepare or request CMS content publication | Merchandiser or Content Editor | CMS | [PRODUCT §§7.4, 12.5](../../../../.ai/core/PRODUCT.md); [REQ-ADM-024](../../../domains/admin/admin-domain.md) | RC |
| ARE-003 | Request Price or Promotion changes | Store Administrator | Pricing | [PRODUCT §§7.3, 12.6](../../../../.ai/core/PRODUCT.md); [REQ-ADM-025](../../../domains/admin/admin-domain.md) | RC |
| ARE-004 | Record a governed Stock adjustment or receipt | Inventory or Fulfilment Operator | Inventory | [PRODUCT §§7.5, 15.2](../../../../.ai/core/PRODUCT.md); [REQ-ADM-026](../../../domains/admin/admin-domain.md) | RC |
| ARE-005 | Coordinate a permitted Shipment or fulfilment action | Inventory or Fulfilment Operator | Shipping and Fulfilment | [PRODUCT §§7.5, 12.7, 15.3](../../../../.ai/core/PRODUCT.md); [BADM-REQ-031](../../../backend/admin/admin-backend.md) | RC |
| ARE-006 | Review Customer profile or Order evidence for support | Customer Support Agent | Customer for profile; Order for Order evidence | [PRODUCT §§7.6, 12.8, 15.4](../../../../.ai/core/PRODUCT.md); [REQ-ADM-027 and 030](../../../domains/admin/admin-domain.md) | RC |
| ARE-007 | Initiate a separately permitted cancellation, Return, or Refund request | Customer Support Agent | Order, Return, or Payment according to action | [PRODUCT §§7.6, 12.8](../../../../.ai/core/PRODUCT.md); [BADM-REQ-029, 030 and 032](../../../backend/admin/admin-backend.md) | RC |
| ARE-008 | Investigate Payment or Refund reconciliation and request governed repair or escalation | Finance or Operations User | Payment for financial truth; Administration for coordination | [PRODUCT §§7.7, 15.5](../../../../.ai/core/PRODUCT.md); [BADM-REQ-029 and 041](../../../backend/admin/admin-backend.md) | RC |
| ARE-009 | Request a bounded report or export | Store Administrator or Finance or Operations User | Reporting for report/export truth; source Domain for underlying facts | [PRODUCT §§7.3, 7.7, 12.10](../../../../.ai/core/PRODUCT.md); [BADM-REQ-035 and 036](../../../backend/admin/admin-backend.md) | RC |
| ARE-010 | Request Staff access or Role/Permission assignment change | Platform Administrator | Identity for access truth; Administration for coordination | [PRODUCT §§7.8, 12.9](../../../../.ai/core/PRODUCT.md); [REQ-ADM-009](../../../domains/admin/admin-domain.md); [BIDN-REQ-025–026](../../../backend/identity/identity-backend.md) | RC |

| ID | Sensitive effect requiring review | Candidate permission-boundary question | Separation-of-duties concern | Accountable confirmation function | Review status | Outstanding evidence |
| --- | --- | --- | --- | --- | --- | --- |
| ARE-001 | Publication or sellability effects may follow later owner action. | Which preparation actions, if any, are distinct from publication? | Must preparation and publication be separated under Product policy? | Product and Product Domain owner | Open; no SC | Actual tasks, business owner, Product-owned state/action split. |
| ARE-002 | Public content may change. | Which CMS request, review, and publication actions exist under an approved workflow? | Is creator/approver separation required? | Product, CMS owner, Administration | Open; no SC | Product §24 item 22 policy, CMS confirmation, actual workflow. |
| ARE-003 | Commercial Price or Promotion effect. | Which Pricing actions require distinct capability boundaries? | Can an actor prepare and approve a commercial change? | Product, Pricing owner, Security | Open; no SC | Current tasks, financial impact, promotion-policy dependencies. |
| ARE-004 | Stock and availability can affect sale or fulfilment. | Which adjustment and receipt actions are separately authorized? | Is independent review needed for material adjustments? | Product, Inventory owner, Operations | Open; no SC | Adjustment categories, owner confirmation, sensitivity classification. |
| ARE-005 | Shipment and delivery state may change. | Which Shipping-owned actions may a Staff actor request? | Should fulfilment preparation and confirmation be separated? | Product, Shipping owner, Operations | Open; no SC | Actual tasks, owner-state checks, applicable provider boundary. |
| ARE-006 | Customer data and cross-domain investigation exposure. | What purpose- and Resource-scoped read boundaries apply to each source? | May the same actor investigate and mutate related records? | Product, Customer and Order owners, Privacy/Security | Open; no SC | Data minimization, case purpose, owner confirmation. |
| ARE-007 | Order, Return, or monetary effect; no success may be inferred from request. | Which initiation actions, if any, are eligible under each owner's policy? | Must initiation, authorization, and financial execution be distinct? | Product, Order, Return and Payment owners, Security | Open; no SC | Cancellation/return/refund policy, task split, owner outcomes. |
| ARE-008 | Financial discrepancy, repair, or escalation. | Which read, repair-request, and owner-approved correction boundaries are distinct? | Can an investigator also approve or perform correction? | Product, Finance/Operations, Payment owner, Security | Open; no SC | Actual reconciliation duties, repair authority, approval evidence. |
| ARE-009 | Sensitive or bulk data disclosure. | Which report definitions, purposes, and export boundaries are approved? | Should report request and export approval be separated? | Product, Reporting and source Domain owners, Privacy/Security | Open; no SC | Product §24 item 21 requirements, data scope, recipients and purpose. |
| ARE-010 | Privilege escalation or stale access. | Which Identity-owned access changes may be requested, approved, or executed? | Can a Staff actor grant or approve its own privilege? | Product, Identity, Security, Administration | Open; no SC | Assignment lifecycle, delegable authority, revocation and review evidence. |

### 4.1 User-confirmed Product direction (PD)

**Provenance:** direction explicitly supplied by the requester for this 2026-10-13 update. It is Product direction for later review, **not** an independently confirmed Operations, Administration, Identity, Security, Privacy/Legal, or owning-Domain fact. “Hybrid growth model” is the supplied operating-model description; it does **not** select DEC-0012's hybrid role-and-capability alternative or any permission model. “Risk-based approvals” and “independent review” express direction without defining a risk threshold, reviewer, approval chain, or workflow.

| Reference | Confirmed Product direction | Still requiring independent confirmation or governance |
| --- | --- | --- |
| PD-001 | Operating model: hybrid growth model. Approval philosophy: risk-based approvals. | Operational interpretation, risk criteria, owner applicability, and any separately governed approval policy. |
| PD-002 | Access lifecycle: periodic review with enhanced privileged-access scrutiny. | Identity and Security review of assignment, withdrawal, revocation, review responsibility, and privileged controls. No review interval is selected. |
| PD-003 | All ten ARE capability areas are intended for initial launch. | Operations and each affected Domain must confirm actual tasks, supported capability, owner, priority, and implementation prerequisites; launch intent is not production eligibility. |
| ARE-001 / PD-004 | Authorized Store Administrators, Merchandisers, and Product Management staff may prepare and edit Product information. Publication requires separate authorization, but not universally a different person. Ordinary changes may proceed without separate approval; sensitive changes require independent review. Product administration may coordinate Pricing and Inventory information without acquiring their authority. | Product Domain must validate preparation, edit, publication, and sellability boundaries; Pricing and Inventory must confirm any coordination. “Ordinary” and “sensitive” remain unclassified, and no individual is assigned access. |
| ARE-002 / PD-005 | Authorized Content Editors, Merchandisers, and Store Administrators may prepare and edit CMS content. Publication requires separate authorization, but not universally a different person. Ordinary changes may proceed without separate approval; sensitive changes require independent review. CMS may coordinate Product and Pricing content without overriding their owning-Domain authority. | CMS, Product, and Pricing must validate action and content boundaries; Product §24 item 22 remains open. “Ordinary” and “sensitive” remain unclassified, and no publication workflow or individual grant is established. |

### 4.2 Recommendations awaiting Product and affected-owner review (PR)

**Provenance:** proposals supplied for this 2026-10-13 update, not user-confirmed choices or independently confirmed operational practice. “Material,” “high-risk,” “sensitive,” “eligible,” and “additional authorization” remain unclassified or separately governed; these statements establish no threshold, grant, approval chain, or executable policy.

| Reference | Product-review proposal | Outstanding review |
| --- | --- | --- |
| ARE-003 / PR-001 | Store Administrators, Merchandisers, and Pricing/Finance staff could request or prepare Pricing changes; material price changes and high-risk Promotions could require independent approval. Pricing retains authoritative control. | Product, Pricing, Finance/Operations, and Security: actual tasks, materiality, eligibility, review boundary. |
| ARE-004 / PR-002 | Inventory/Fulfilment staff could record receipts and request adjustments; material adjustments could require independent review. Inventory retains Stock authority. | Product, Inventory, Operations, and Security: action distinctions, materiality, accepted outcome. |
| ARE-005 / PR-003 | Fulfilment staff could prepare Shipment actions; sensitive or exceptional actions could require independent review. Shipping retains Shipment-state authority. | Product, Shipping, Operations, and Security: supported actions, exception classification, owner-state checks. |
| ARE-006 / PR-004 | Customer Support could receive purpose-limited Customer/Order information access; sensitive data would remain restricted and owning Domains would govern authoritative changes. | Product, Customer, Order, Privacy/Legal, and Security: purpose, data scope, object isolation, actual duties. |
| ARE-007 / PR-005 | Support could initiate eligible cancellation, Return, and Refund requests; material Refunds and sensitive exceptions could require independent authorization. Order, Return, and Payment retain their authority. | Product, Order, Return, Payment, and Security: eligibility, amount sensitivity, distinct owner outcomes and still-open policy. |
| ARE-008 / PR-006 | Finance/Operations could investigate reconciliation issues and request corrections; material corrections could require independent approval. Payment retains financial authority. | Product, Finance/Operations, Payment, and Security: repair eligibility, impact, approval need and evidence. |
| ARE-009 / PR-007 | Authorized Staff could view purpose-appropriate reports; sensitive or bulk exports could require additional authorization, purpose limitation, and auditability. Reporting and source Domains retain their respective authority. | Product, Reporting, source Domains, Privacy/Legal, and Security: report definitions, export purpose, recipients, sensitivity. |
| ARE-010 / PR-008 | Authorized administrators could request Staff access changes; Identity would govern assignment and revocation. Propose prohibiting self-approval for privilege increases and enhancing privileged-access review. | Product, Identity, Security, and Administration: delegable authority, assignment and revocation duties, self-approval policy, review evidence. |

### 4.3 Proposed Product/Operations task triage — pending confirmation

All ten ARE capability areas remain intended for launch planning under PD-003. The retained scopes below are **proposed candidates** for a later DEC-0012 alternative comparison, subject to independent Operations confirmation and owning-Domain validation. Deferred actions are outside this proposed comparison scope, not necessarily permanently outside Product scope. Product sponsor direction for these tasks, candidate actors, and deferred actions is recorded in §4.4; Operations must still confirm intended tasks, actors, accountable business owners, and scope. No task is yet confirmed eligible for comparison, and no new executable operation is asserted.

| ID | Proposed retained candidate task | Deferred from proposed comparison scope |
| --- | --- | --- |
| ARE-001 | Product information preparation/editing and publication requests. | Unclassified sensitive publication approvals. |
| ARE-002 | CMS content preparation/editing and publication requests. | Unresolved CMS approval-policy mechanics. |
| ARE-003 | Price/Promotion preparation and change requests. | Direct financial overrides and undefined material-change approvals. |
| ARE-004 | Inventory receipt recording and stock-adjustment requests. | Unclassified material-adjustment approvals. |
| ARE-005 | Fulfilment preparation and requests for permitted Shipment-state changes. | Exceptional Shipment overrides. |
| ARE-006 | Purpose-limited Customer/Order lookup. | Unrestricted Customer-data access or mutation. |
| ARE-007 | Eligible cancellation, Return, and Refund requests. | Refund execution and undefined exception approvals. |
| ARE-008 | Reconciliation investigation and correction requests. | Direct financial corrections and overrides. |
| ARE-009 | Authorized, purpose-appropriate report access. | Undefined sensitive or bulk exports. |
| ARE-010 | Staff access assignment, change, and withdrawal requests. | Self-grant, privilege escalation, and direct Identity-owned assignment. |

These are conditional task descriptions, not confirmation that a request, receipt, lookup, or other operation already exists or is authorized for execution. “Eligible,” “permitted,” and “authorized” remain subject to existing and later applicable owner policy. In particular, an ARE-010 request cannot itself change Identity-owned access; an ARE-007 request cannot establish an owning-Domain outcome.

The following are illustrative **business responsibility groupings**, not selected Roles, Permission sets, grants, approval authorities, or organizational positions:

| Illustrative grouping | Candidate areas |
| --- | --- |
| Catalogue and merchandising operations | ARE-001, ARE-002, ARE-003 |
| Inventory and fulfilment operations | ARE-004, ARE-005 |
| Customer support operations | ARE-006, ARE-007 |
| Financial operations | ARE-003, ARE-007, ARE-008 |
| Reporting and oversight | ARE-009 |
| Staff access administration | ARE-010 |

Groupings may overlap. A small team may hold multiple responsibilities only subject to future separation-of-duties constraints; the overlap neither assigns access nor resolves those constraints. DEC-0012 alternatives remain unselected.

For **each proposed retained task**, the Product sponsor direction in §4.4 must be reconciled with independent Operations confirmation of intended business need, task scope, candidate actor/responsibility, and accountable business owner. The owning Domain must validate the Resource/action, current-state, and denial boundaries. Product/Security must review sensitivity where applicable; Identity/Security must review privilege, assignment, revocation, and separation-of-duties concerns; Administration must review its coordination-only scope. Finance/Operations and Privacy/Legal review applies only to affected tasks. Each eventual confirmation must record its function, date, source, scope, disposition, and any dispute. This requests accountable review without naming reviewers, asserting sign-off, or defining approval mechanics.

### 4.4 Product sponsor responsibility confirmation (PD-006)

**Function:** Product sponsor/requester. **Date:** 2026-10-13. **Source:** explicit requester approval in the project conversation following presentation of the ARE-001–ARE-010 Product responsibility baseline. **Scope:** all ten ARE areas, their candidate actors, and the §4.3 deferred comparison actions. **Disposition:** approved as Product sponsor direction. This is `PD`, not `SC`: it is not independently confirmed Operations practice or Administration, Identity, Security, Finance, Privacy/Legal, or owning-Domain approval. The original `RC` inventory and `PR` recommendations retain their historical provenance. Where a PR differs from this Product-approved baseline, the PR remains an unconfirmed historical proposal rather than current Product direction.

| ARE | Product-approved candidate business responsibility | Candidate actor or function |
| --- | --- | --- |
| ARE-001 | Product preparation, editing, and publication requests. | Store Administrator, Merchandiser, specifically authorized Product Management staff. |
| ARE-002 | CMS preparation, editing, and publication requests. | Content Editor, Merchandiser, specifically authorized Store Administrator. |
| ARE-003 | Pricing and Promotion preparation/change requests. | Authorized Pricing/Finance staff, Store Administrator; Merchandiser only if separately authorized. |
| ARE-004 | Inventory receipts and adjustment requests. | Inventory/Fulfilment staff. |
| ARE-005 | Fulfilment preparation and Shipment-action requests. | Fulfilment staff. |
| ARE-006 | Purpose-limited Customer/Order lookup. | Customer Support staff. |
| ARE-007 | Cancellation, Return, and Refund requests. | Customer Support staff. |
| ARE-008 | Reconciliation investigation and correction requests. | Finance/Operations staff. |
| ARE-009 | Authorized reporting access. | Operations, Finance, and specifically authorized staff. |
| ARE-010 | Staff access-change requests. | Specifically authorized administrative staff. |

The Product sponsor also approved the existing §4.3 deferred actions as exclusions from the **proposed comparison scope**. They are not permanently excluded from Product scope or authorized for execution. These actors and responsibilities are candidates for accountable review, not selected executable Roles, granted Permissions, organizational assignments, confirmed operational practice, or proof that an operation exists. Operations must still independently confirm intended tasks, actors, business owners, and scope; each affected owner must validate its authority and applicable policy.

### 4.5 Founder-led Product operating direction (PD-007–PD-009)

**Provenance:** Function: Founder/Product sponsor/requester. Date: 2026-10-13. Source: explicit approval in the project conversation. Disposition: approved as Product operating direction, not as an Accepted DEC-0012 policy, independent Operations practice, or Identity, Security, Administration, Finance, Privacy/Legal, or owning-Domain approval. The historical `RC`, PD-001–PD-006, and PR-001–PR-008 records remain intact; earlier PR recommendations remain historical proposals where the direction below differs.

**PD-007 — Founder-led operating model.** Initial launch business governance is founder-led, with the founder acting as the initial Product/business sponsor and a small initial operating team. Responsibilities may overlap where permitted; dedicated functions may be introduced as the organization grows. Overlap does not override current Authorization, least privilege, owning-Domain authority, or binding separation-of-duties controls. This Product direction does not independently establish the founder as an Identity, Security, Operations, or owning-Domain authority or assign anyone an executable Role.

**PD-008 — Product-approved operational policy direction.** The requester approved the following direction for the ten ARE areas. Each statement remains subject to current Security, Identity, owning-Domain, and separately governed Product policy; it is neither an executable grant nor proof that the action exists or has been implemented. Where independent review is described, PD-009's sensitive-action exception preference cannot waive that review unless separately governed and found compatible with binding controls.

| ARE | Founder-approved Product operating direction |
| --- | --- |
| ARE-001 | Authorized staff may prepare and edit products. Ordinary publication may proceed without a separate approval step when specifically authorized. Sensitive publication remains subject to applicable controls and unresolved policy review. |
| ARE-002 | Authorized staff may prepare and edit CMS content. Ordinary publication may proceed without a separate approval step when specifically authorized. Sensitive or high-impact publication remains subject to applicable controls and unresolved CMS policy. |
| ARE-003 | Authorized staff may prepare Pricing and Promotion changes. Material financial changes require independent approval, subject to the separately recorded, unresolved administrator exception preference. |
| ARE-004 | Authorized staff may record Inventory receipts and request adjustments. Material adjustments require review, subject to the separately recorded, unresolved administrator exception preference. |
| ARE-005 | Authorized staff may prepare Fulfilment activities and request permitted Shipment actions. Exceptional changes require additional authorization. |
| ARE-006 | Customer Support may access Customer/Order information only for authorized, purpose-limited support duties. |
| ARE-007 | Customer Support may initiate eligible cancellation, Return, and Refund requests; owning Domains determine eligibility and outcomes. Material Refund execution requires independent approval, subject to the separately recorded, unresolved administrator exception preference. |
| ARE-008 | Finance/Operations may investigate reconciliation discrepancies and request corrections. Material financial corrections require independent approval, subject to the separately recorded, unresolved administrator exception preference. |
| ARE-009 | Reporting access must be purpose-limited and authorized. Sensitive or bulk exports require additional authorization. |
| ARE-010 | Authorized administrators may request staff access changes. Identity retains assignment, withdrawal, and effective revocation authority. |

**PD-009 — Administrator autonomy preference.** The requester states: “Where a user is an administrator, the administrator should be able to self-approve actions or perform them without a separate approval.” For **routine authorized actions**, this supports approval-free operation only where the administrator already has current, applicable authorization and no governing policy requires a separate approval; it grants no universal administrator access. For **sensitive or privileged actions**, self-approval or no separate approval is an **OPEN POLICY EXCEPTION REQUEST**, not an accepted exception. Security, Identity, affected owning Domains, and applicable separation-of-duties authority must assess it before any policy resolution. It does not permit self-granting Permissions, unauthorized privilege escalation, bypass of Identity-owned assignment or revocation, owning-Domain Authorization or invariants, binding separation of duties, unrestricted financial changes, or Refund execution. The §4.3 deferred actions remain deferred from comparison unless separately reviewed and authorized.

## 5. Sensitive actions and separation-of-duties questions

[Security Standards §§10–12 and 27](../../../../.ai/core/SECURITY-STANDARDS.md) already bind least privilege, privileged-account controls, current Authorization, and audit. PRODUCT §7.4 also bars financial, security, and Staff-administration permissions for the content-editor role by default. These are BR controls. PD-001, PD-004, PD-005, and PD-008 express Product risk-based or independent-review direction; PR-001–PR-008 remain historical proposals. PD-009's sensitive administrator self-approval or approval-free preference is unresolved and cannot override binding separation of duties. Which ARE entries are high-Risk, material, ordinary, or sensitive; which duties must be separated; who may request, approve, execute, and review; and whether temporary or exceptional access is permitted remain UQ. No row classifies an action conclusively or creates an approval chain. Product and affected owners must identify business effects before Security can review classifications.

## 6. Role lifecycle and approval evidence gaps

[BIDN-REQ-024–026](../../../backend/identity/identity-backend.md) and [Security Standards §10](../../../../.ai/core/SECURITY-STANDARDS.md) are BR for trusted access evidence, explicit authorized assignment, change, withdrawal, effective revocation, historical traceability, and periodic review. PD-002 adds Product direction for periodic review and enhanced privileged-access scrutiny; PR-008 historically proposes a prohibition on self-approval for privilege increases. PD-009 records the competing founder preference as an open exception request, not a resolution of delegable authority or self-grant safeguards. Missing evidence includes who may request each assignment, who may approve it, whether self-grant is prohibited under the eventual policy, who verifies withdrawal, and who owns periodic business review. Approval chains, approver counts, review interval, temporary-grant duration, override policy, and workflow mechanics remain unresolved; none is established here.

## 7. Cross-domain authorization review

Administration may coordinate only through governed owning-Domain capabilities. Identity establishes trusted access evidence and assignments but not Product, Pricing, Inventory, Customer, Order, Payment, Shipping, Return, CMS, or Reporting authorization outcomes. [ADM-REQ-007–011](../../../domains/admin/admin-domain.md), [BADM-REQ-007–011](../../../backend/admin/admin-backend.md), and [SECURITY-STANDARDS §12](../../../../.ai/core/SECURITY-STANDARDS.md) require current server-side authorization, default denial, and owning-Domain invariant checks. Each affected owner must confirm the relevant Resource, action, state and denial conditions. A candidate administrative permission cannot substitute for an owner decision; a cross-domain workflow cannot inherit a grant from another step. The inventory does not establish that any task has an executable Contract or eligible production path.

## 8. Stakeholder confirmation register

The requester supplied the PD direction in §4.1 and PR recommendations in §4.2, approved the §4.4 responsibility baseline and §4.3 proposed comparison exclusions, and supplied founder-led Product direction in §4.5. Routine administrator autonomy is preferred within existing authorization; sensitive administrator self-approval remains unresolved. No independent Operations, Administration, Identity, Security, affected-Domain, or Privacy/Legal interviews, sign-offs, operational observations, or SC facts have been supplied. The entries below identify **requested confirmation functions**, not named assignees or independent approvals.

| Function | Evidence requested | State |
| --- | --- | --- |
| Product and Operations | Reconcile PD-001–PD-009 and historical PR-001–PR-008 with intended or actual tasks, business responsibility owners, candidate actors, launch relevance, and operational usability. | Founder/Product sponsor direction in §§4.4–4.5 recorded on 2026-10-13; independent Operations confirmation missing; no SC |
| Administration | Coordination steps, request/outcome boundaries, routine autonomy, and workflow evidence without owning-Domain substitution. | Missing; no SC |
| Identity | Access evidence, assignment/withdrawal lifecycle, revocation, delegable-authority constraints, and PD-009's sensitive exception request. | Missing; no SC |
| Security | Sensitivity, privilege escalation, separation of duties, default denial, audit and reviewability, including PD-009's sensitive exception request. | Missing; no SC |
| Product, CMS, Pricing, Inventory, Shipping, Customer, Order, Return, Payment, and Reporting owners as applicable | Each applicable task's Resource/action/state authority, denial cases, and source evidence. | Missing; no SC |
| Finance/Operations where affected | Reconciliation, material financial-change and Refund-execution responsibilities and independent-review boundaries. | Missing; no SC |
| Privacy/Legal where affected | Customer-data and export purposes, minimization, disclosure and applicable obligations. | Missing; no SC |

If evidence later conflicts, record both claims, dates, owners, affected IDs, and unresolved disposition as DS; do not silently choose one or mark the gate satisfied.

## 9. Evidence sufficiency gate for later comparison

A later DEC-0012 alternative comparison may be considered only after **all** of the following are documented and reviewable. PD-001–PD-009 include the Product sponsor's responsibility baseline, founder-led direction, operational policy direction, and proposed comparison exclusions, but they do not replace independent Operations, Administration, Identity, Security, affected-Domain, or applicable Finance and Privacy/Legal confirmation; the gate as a whole remains unsatisfied:

The §§4.4–4.5 Product sponsor records cover Product direction for condition 1, including the §4.3 proposed exclusions. They do not supply independent Operations confirmation or an accountable joint disposition of which tasks will actually be retained or excluded for comparison. PD-009 remains unresolved for sensitive-action classification, incompatible duties, delegable authority, Identity-owned access changes, and financial or privileged operations; an affected task must be separately resolved or excluded under the existing conditions below. Owning-Domain Resource/action and denial-boundary validation, Identity/Security review, Administration coordination-scope review, and applicable Finance and Privacy/Legal review also remain outstanding. Binding repository controls still apply. A later comparison requires separate review and authorization under DEC-0012; Product direction alone establishes no comparison eligibility. DEC-0012 alternative comparison is **NOT ELIGIBLE** now.

1. Every task retained for comparison has an attributable Product/Operations confirmation of current business need, actor/responsibility, and scope; rejected examples are marked as such.
2. A business responsibility owner and an owning Domain are confirmed for each retained Resource/action pair, with the owner's current-state and denial boundary validated.
3. Product and Security record a sensitivity classification or an explicit unresolved classification for each retained task; unresolved high-impact classification blocks comparison for that task.
4. Product and affected owners document incompatible duties, self-grant concerns, approval or escalation dependencies, and which questions remain separately governed; no approval chain is inferred from this record.
5. Identity and Security review the proposed task boundaries against mandatory Roles collecting Permissions, RBAC plus contextual checks, default denial, assignment/withdrawal, effective revocation, and delegable authority.
6. Administration confirms coordination-only scope; affected Domain owners confirm no proposed permission bypasses their authorization or invariants.
7. Required audit/accountability and privacy evidence are identified for sensitive and cross-domain actions without inventing retention, schema, or workflow mechanics.
8. Every confirmation has an attributable function, date, source, scope and disposition. Disputes, missing owners, and unresolved Product §24 dependencies are recorded and either resolved through governing authority or explicitly excluded from the later comparison.

Passing this gate would permit only a separately reviewed **comparison** of still-unselected DEC-0012 alternatives. It would not select a model, accept DEC-0012, resolve Product §24 item 23, or authorize implementation. Gate status: **NOT SATISFIED**.

## 10. Open questions and blockers

- For the ten capability areas intended for launch under PD-003, which specific tasks are actually performed or planned, and which Product personas are illustrative only?
- What are the exact owner-approved action and Resource boundaries, business owners, negative cases, and current-state conditions?
- Which tasks are sensitive or privileged; which duty combinations are prohibited or require separately governed review?
- What Product policy governs content approval, support escalation, cancellation, returns, refunds, exports, and other dependencies still open in PRODUCT §24?
- Who may request, approve, change, withdraw, and review access without self-grant or stale privilege?
- Can any part of PD-009's sensitive administrator self-approval preference satisfy binding Security, Identity, owning-Domain, and separation-of-duties constraints, or must the affected tasks remain excluded pending separate governance?
- Which discrepancies or disputed claims require a separate Product, Security, Domain, Contract, or Architecture decision before comparison?

These are evidence gaps, not findings that any candidate policy is acceptable. No material governance prerequisite blocks **collecting** attributable information; missing Product and affected-owner evidence blocks comparison.

## 11. Explicit non-authorizations

This evidence record selects no DEC-0012 alternative, Role/Permission matrix, permission catalog, Role assignment, organizational post, executable separation policy, high-Risk classification, approval workflow or threshold, support override, provider, dependency, endpoint, DTO, event, database schema, storage or transaction mechanism. PD-001's hybrid growth model does not select DEC-0012's hybrid permission-model candidate. PD-004 and PD-005 do not establish executable publication or approval rules; PR-001–PR-008 remain historical proposals. PD-007–PD-009 are Product direction, not independent operational or technical confirmation. PD-009 grants no accepted self-approval exception, self-grant, privilege escalation, Identity or owning-Domain bypass, unrestricted financial change, or Refund execution. This record grants no privileged Identity authority, owning-Domain authorization bypass, Contract change, implementation eligibility, DEC acceptance, Product §24 item 23 closure, or backend-roadmap extension. Existing Approved Specifications and Contracts remain unchanged.

## 12. Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.5.0 | 2026-10-13 | Draft | Recorded founder-led Product operating direction, ten approved operational policy recommendations, routine administrator autonomy, and the unresolved sensitive administrator self-approval preference; preserved existing governance and the unsatisfied evidence-sufficiency gate. |
| 0.4.0 | 2026-10-13 | Draft | Recorded attributable Product sponsor approval of the ARE-001–ARE-010 responsibility baseline, candidate actors, and §4.3 deferred comparison actions; preserved outstanding independent confirmations and the unsatisfied evidence-sufficiency gate. |
| 0.3.0 | 2026-10-13 | Draft | Proposed Product/Operations triage for all ten ARE areas, illustrative responsibility groupings, deferred comparison actions, and outstanding accountable confirmations without satisfying the evidence-sufficiency gate or selecting a policy. |
| 0.2.0 | 2026-10-13 | Draft | Recorded requester-confirmed Product direction for operating model, risk-based review, lifecycle scrutiny, launch-area intent, ARE-001 and ARE-002; separately recorded ARE-003–ARE-010 recommendations for Product and owner review without independent stakeholder confirmation, option comparison, policy selection, or implementation authority. Preserved the unsatisfied evidence-sufficiency gate. |
| 0.1.0 | 2026-10-13 | Draft | Prepared a repository-derived administrative task inventory, explicit evidence classifications, stakeholder-confirmation gaps and an unsatisfied sufficiency gate for later Product-led DEC-0012 review; recorded no operational confirmations, policy comparison or implementation authority. |
