# DEC-0012 — Administrative Role and Permission Matrix

- **Identifier:** DEC-0012
- **Title:** Administrative Role and Permission Matrix
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.4.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-11-11
- **Owner:** Product
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

[`PRODUCT.md` §24 item 23](../../.ai/core/PRODUCT.md) leaves the administrative role and permission matrix open. Its §7 administrative personas and §12 administrative capabilities describe potential responsibilities but do not settle which Staff User may perform each operation. The [Approved Administration Domain Specification](../domains/admin/admin-domain.md) and [Approved BADM Backend Specification](../backend/admin/admin-backend.md) require current, trusted, contextual Authorization and preserve this Product decision as unresolved. Neither specification selects a matrix or grants Administration ownership of another Domain's state.

The [Approved Identity Backend Specification](../backend/identity/identity-backend.md) separates Role, Permission, Claims, Scope, Principal, and Session; Identity owns access assignments and trusted access evidence, while the owning business Domain decides whether an action is allowed for its Resource and current state. The repository has no Administration-specific executable Contract. The existing [Registration](../contracts/customer/initial-customer-registration-contract.md) and [Login](../contracts/identity/initial-customer-login-contract.md) Contracts do not establish administrative permissions. A policy decision is needed before binding the unresolved matrix to implementation; this proposal makes no such choice.

## Decision Question

Which administrative capabilities require explicit Product authorization, and how should their business permissions be represented at the policy level so that accountable Staff Users can perform permitted work under least privilege without treating a Role label, UI affordance, or Identity access evidence as the owning Domain's contextual Authorization decision?

The eventual decision must distinguish ordinary operations, sensitive actions, cross-domain coordination, approval or repair where separately governed, and the effect of assignment changes. It must identify the business owner of each permission and the evidence needed to review its scope. It must not define an operation or approval workflow absent from Approved authority.

## Decision — Proposed Boundary Only

**NO OPTION SELECTED.** The administrative role and permission matrix remains an open Product decision. Product must review attributable business evidence with Administration, Identity, Security, and affected Domain owners before a policy option can be selected. This Proposed record is not authoritative and grants no implementation authority.

## Scope and Authority Boundaries

| Concern | Existing authority and boundary |
| --- | --- |
| Administrative coordination | Administration coordinates protected operational workflows and invokes the owning Domain's governed capability; it does not acquire that Domain's business truth or invent its policy. |
| Product-owned business permissions | Product must establish the intended business responsibility and access policy for administrative capabilities, subject to each affected Domain's Approved rules. This record does not yet assign capabilities to Roles. |
| Identity-owned security authority | Identity owns Authentication, Principal and Session evidence, Roles, Permissions, and access assignments. Authentication or an assignment alone does not authorize a business action. |
| Domain-owned contextual Authorization | The owning Domain retains the server-side decision for the current Principal, Resource, action, purpose where applicable, and authoritative state. A matrix cannot override an owning-Domain denial or invariant. |
| Cross-domain operations | An administrative workflow may coordinate multiple owners only through their governed capabilities and applicable Contracts. Permission for one step does not grant permission for another. |
| Sensitive actions | High-risk, privileged, financial, customer-data, export, access-management, repair, and other sensitive actions require explicit policy review, least privilege, appropriate separation of duties, and auditability. This proposal does not classify specific actions or set a confirmation, approval, or override rule. |

## Bounded Product Authorization Proposal — Awaiting Accountable Disposition

**RECOMMENDED FOR REVIEW / NOT APPROVED.** Evaluate a bounded Product-specific
policy for the five actions below without selecting an overall matrix candidate.
This proposal supplies review scope, not Roles, Permission identifiers, Staff
assignments, an approval workflow or implementation authority. It does not approve
the full Administration matrix or resolve `PRODUCT.md` §24 item 23.

### Existing Mandatory Requirements — Referenced, Not Newly Selected

[Security Standards §§10–13 and 27](../../.ai/core/SECURITY-STANDARDS.md),
BIDN-REQ-011–014, 021–026, BADM-REQ-005, 007, 014, 045 and BPRD-REQ-007,
019–023, 030, 037 already govern trusted actor/access evidence, current contextual
Authorization, applicable assurance, withdrawal and audit. Those obligations remain
binding independently of this proposal; their applicability is not awaiting approval.
In particular, mandatory MFA for privileged Staff and applicable privileged-session
reauthentication must not be recast as optional new controls. Product-specific
classification and operational application remain unresolved below.

### Recommended Product Action Scope

| Action for separate evaluation | Proposed Resource/context binding |
| --- | --- |
| Content acceptance | Exact Product and relevant content/configuration revision. |
| Media acceptance | Exact Product, media asset and association, applicable revisions and governed supporting evidence. |
| Publication | Exact Product, applicable current acceptance, revision and lifecycle state. |
| Withdrawal/unpublication | Exact Product, current publication/lifecycle context and applicable revision; do not presume publication permission grants withdrawal permission. |
| Archival | Exact Product, current lifecycle/revision and historical-reference obligations. |

Recommend separate contextual decisions for these actions; evaluation of one grants
no other action. This selects neither five concrete Permissions nor their grouping.
The bounded policy should identify trustworthy Identity-owned Principal evidence,
separately confirmed current Staff association, current access-assignment provenance
and scope, effective withdrawal, and evidence of applicable Session/authentication
assurance. Caller identifiers, labels or acceptance/authorization booleans cannot
supply that authority. No evidence issuer, producer, representation or authentication
mechanism is selected here; Customer Authentication must not be presumed to prove
Staff association or authority.

Product retains the server-side contextual decision for the exact Principal,
Product Resource, action and applicable revisions/state. Identity supplies trusted
identity/access evidence; Administration coordinates only governed workflows and
cannot publish Product truth or introduce a parallel access model. Permission or
assignment evidence cannot override Product denial, current invariants or missing
media-security evidence. Media-security checking authority and invalidation remain
within [DEC-0042 S1](DEC-0042-product-publication-acceptance-and-visibility.md), not
implicitly settled by media-acceptance eligibility.

### Outstanding Accountable Product Decisions

All questions below remain **unresolved**. Accountable capacities identify required
review concerns, not completed dispositions or new organizational assignments.

| Question requiring explicit disposition | Accountable review concerns |
| --- | --- |
| Which governed assignments are eligible for each action, with what Product/revision scope and trusted Staff/access provenance? | Product business responsibility; Administration coordination; Identity assignment evidence; Security least privilege. |
| May the same actor accept content or media and publish the Product, and under which combinations or limitations? | Product and Administration operational responsibility; Security separation of duties. |
| Which actions are privileged or high-impact, and what action-specific risk classification applies? | Security classification with Product and Administration context. |
| What MFA, privileged-session and reauthentication evidence is required for each classified action under existing mandatory controls? | Security assurance and Identity evidence; no new mechanism or lifetime selected. |
| Is independent approval required for any action or action combination, and what governed evidence establishes it? | Security with Product and Administration; no approver count, chain or same-actor permission assumed. |
| Who may grant, change or withdraw the applicable assignments, and how are provenance, traceability and effective revocation established? | Identity assignment integrity, Administration coordination, Product scope and Security review. |
| When is current Authorization revalidated, and what prevents known withdrawal, stale evidence or changed Product state from enabling an in-flight operation? | Product mutation boundary, Identity current access and Security safeguards; no lock, TTL or transaction strategy selected. |
| What trusted audit guarantee applies, what happens when required audit cannot be recorded, and how are uncertain commit outcomes investigated and reconciled without duplicate or falsely successful mutation? | Security audit integrity, Product outcome truth and Administration recovery coordination; no audit infrastructure or retry mechanism selected. |

Existing evidence requirements also apply to invalidation and access provisioning;
those authorities are not implicitly granted by the five action evaluations.
Resolving this subset requires explicit accountable dispositions and applicable
acceptance/synchronization before operational reliance. A recommendation, partial
approval note or recorded assignment inside this Proposed record is not an
Accepted authorization policy. Broader administrative capabilities remain unresolved.

[DEC-0042](DEC-0042-product-publication-acceptance-and-visibility.md) remains Proposed.
Its recorded Product-owner P1, P2 and P4 approvals are preserved; this proposal does
not reopen them, satisfy pending S1 or P3/S2, approve publication or authorize
positive customer visibility. Existing Accepted decisions and default-deny Product
administration boundaries remain unchanged.

## Product-Specific Accountable Disposition Package — Prepared 2026-11-09

**PROPOSED DISPOSITION — NOT APPROVED. PENDING ACCOUNTABLE APPROVAL for all eight items.**
This package maps A–H to the eight outstanding questions above; these are local
review labels, not Roles, Permission identifiers, assignments or new decisions.
It prepares accountable choices without selecting an overall matrix, closing any
question, recording Security concurrence or granting implementation authority.
Accountable owners below denote review capacities, not personal identities or
completed approvals. Explicit confirmation must record the accepting capacity,
date, scope and any conditions; no confirmation is supplied by this package.

**ESTABLISHED REQUIREMENT** identifies obligations already binding under the
Existing Mandatory Requirements section. **CONFIRMED BUSINESS DIRECTION** refers
only to the Product-owner dispositions recorded in [DEC-0042, Product-Owner Business
Disposition and P1/P2/P4](DEC-0042-product-publication-acceptance-and-visibility.md).
Those approvals retain their original provenance and limits; this document does
not reproduce them as Security, Identity or Administration approvals. Absence of
an item-specific business choice is identified explicitly.

### A — Product-Scoped Eligible Assignments

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Least privilege, separately trusted Principal/Staff/access evidence, and Product-owned current Resource/action Authorization apply; Authentication or an assignment alone grants no Product action. See BIDN-REQ-011–014, 021–026 and BPRD-REQ-007, 037. |
| CONFIRMED BUSINESS DIRECTION | DEC-0042 records the founder as the only initial operator, conditional eligibility for the founder and future designated publishers after required acceptance, and founder responsibility for initial withdrawal. None establishes an assignment. |
| PROPOSED DISPOSITION | Evaluate eligibility separately for the five actions and exact Product/revision scope. Initially provision only the founder's capabilities expressly approved through governance; future designation alone grants nothing. Preserve mandatory RBAC and Product contextual denial without choosing concrete Roles, Permissions or assignment mechanisms. |
| Responsible accountable owners | Product: business eligibility and Product scope; Administration: operational coordination; Identity: trusted Staff association and access evidence; Security: least privilege and prohibited combinations. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Confirm eligibility for each action, Product/revision limits, required Staff/access provenance and any prohibited combinations. Confirm that founder status and publisher designation cannot establish access. |
| Consequence if unresolved | No operational action may rely on assumed eligibility; existing default denial remains. |
| Implementation dependency | Governed Staff authentication/association, current assignment evidence, contextual Product evaluation and applicable reviewed Contracts; no source, representation or provisioning mechanism is selected here. |

### B — Risk-Based Same-Actor Restrictions

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Applicable separation of duties and mandatory acceptance/security evidence cannot be bypassed. See Security Standards §§7.6, 12 and DEC-0042 Existing Authoritative Security and Ownership Requirements. |
| CONFIRMED BUSINESS DIRECTION | DEC-0042 approves risk-based separation subject to Security restrictions. The founder is the only initial operator; higher-risk categories are candidates for assessment, not a completed Security classification. Missing mandatory evidence always blocks publication. |
| PROPOSED DISPOSITION | Consider same-actor acceptance/publication only for a Security-defined ordinary-risk class after every mandatory check. Higher-risk or unclassified cases remain blocked until their applicable separation requirements are satisfied. This is neither blanket same-actor permission nor a universal two-person rule. |
| Responsible accountable owners | Security: risk classes and separation restrictions; Product: business applicability; Administration: operational feasibility and coordination. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Define the permitted ordinary-risk class, prohibited action combinations and higher-risk treatment, including how classification is established and who may resolve uncertainty. Explicitly accept or amend any same-actor permission. |
| Consequence if unresolved | No same-actor acceptance/publication permission may be inferred; operations requiring that permission remain unavailable. |
| Implementation dependency | Trusted actor attribution, revision-bound acceptance, governed risk evidence and enforcement of accepted separation rules; no risk engine or workflow is selected. |

### C — Security-Classified Privileged Actions

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Privileged and high-impact actions require applicable stronger controls, least privilege and audit. See Security Standards §§10–13 and 27. |
| CONFIRMED BUSINESS DIRECTION | DEC-0042 identifies higher-risk Product candidates and approved business responsibilities, but does not classify the five actions for Security purposes. |
| PROPOSED DISPOSITION | Conservatively classify publication, withdrawal and archival as high-impact; classify content/media acceptance by their ability to establish publication prerequisites. This proposed classification is not effective until accountable disposition. |
| Responsible accountable owners | Security: action classification; Product and Administration: impact and operational context. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Classify each of the five actions explicitly, identify any distinctions or additional restrictions and confirm treatment of exceptional/urgent withdrawal without assuming an exemption. |
| Consequence if unresolved | No lower-risk classification or assurance exception may be assumed to enable an action. |
| Implementation dependency | Accepted action classification and its assurance/approval mapping before operational enforcement; no numerical thresholds or classifier mechanism selected. |

### D — Action-Specific MFA and Reauthentication

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Mandatory privileged Staff MFA, applicable privileged-session controls and high-impact reauthentication already apply. Trusted Identity evidence must support reliance; see Security Standards §§11–13 and BIDN-REQ-012–014, 020–026. |
| CONFIRMED BUSINESS DIRECTION | Founder operation and designated-publisher eligibility confer no assurance exception. No Product-owner disposition selects a factor, provider, freshness interval or action-specific assurance rule. |
| PROPOSED DISPOSITION | Require Identity-verifiable evidence of the controls applicable to each classified action. Generic MFA evidence must not be interpreted as proof of a particular phishing-resistant factor. Preserve accepted server-side Sessions and Customer/Staff separation. |
| Responsible accountable owners | Security: required assurance and action-specific freshness; Identity: trustworthy evidence and integration capability; Product and Administration: action context. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Confirm each action's MFA, privileged-session, reauthentication and freshness requirements, acceptable assurance evidence and handling of missing/unsupported evidence; identify any further mechanism decision needed. |
| Consequence if unresolved | Mandatory controls remain binding, but no unconfirmed assurance mapping may activate an operational action. |
| Implementation dependency | Separately governed Staff authentication authority, assurance provenance and current Session/access evidence; no issuer, factor, provider, protocol or lifetime selected. |

### E — Risk-Based Independent Approval

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Applicable separation and stronger authorization/approval requirements remain binding; one action's permission cannot grant another. See Security Standards §12 and the Recommended Product Action Scope above. |
| CONFIRMED BUSINESS DIRECTION | DEC-0042 confirms risk-based separation, not a universal independent approval chain or unrestricted same-actor execution. |
| PROPOSED DISPOSITION | Specify independent approval only where accepted risk policy requires it, without inventing a universal chain. Block affected actions when the required independent actor or evidence is unavailable; no single-operator override is presumed. |
| Responsible accountable owners | Security: required independence and control adequacy; Product and Administration: affected actions, business impact and operational feasibility. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Identify action/risk combinations requiring independent approval, eligibility and independence constraints, revision binding and invalidation, and any urgent-withdrawal treatment. Reconcile explicitly with B; do not infer an approver count or chain. |
| Consequence if unresolved | No exemption from applicable approval requirements may be inferred; actions needing unresolved approval treatment remain blocked. |
| Implementation dependency | Governed approval evidence and current actor/revision checks after policy acceptance; no approval API, workflow or override mechanism selected. |

### F — Identity-Owned Assignment Evidence

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Identity owns trusted access assignments and effective withdrawal; Administration coordinates within its boundary and Product retains contextual Authorization. See BIDN-REQ-024–026 and BADM-REQ-005, 007, 014. |
| CONFIRMED BUSINESS DIRECTION | The founder's initial operating/withdrawal responsibility and future publisher designation do not grant assignment-management authority. No eligible grant/change/withdrawal actor has been approved here. |
| PROPOSED DISPOSITION | Use one governed Identity-owned access-evidence model with attributable assignment lifecycle, effective revocation and Administration coordination. Do not introduce a competing Product assignment authority. |
| Responsible accountable owners | Identity: assignment integrity and revocation; Administration: coordination; Product: business scope; Security: provisioning authority and least privilege. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Identify who may grant, change and withdraw each applicable assignment, the trusted provenance and scope, and the guarantee by which withdrawal becomes effective before further reliance. |
| Consequence if unresolved | Founder status, stored labels or stale assignment evidence cannot authorize actions; operational provisioning remains unavailable. |
| Implementation dependency | Governed assignment/Staff association sources, access observation and withdrawal integration; no schema, event, issuer or assignment mechanism selected. |

### G — Current Authority and Revision Revalidation

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Current contextual Authorization, effective withdrawal, concurrency integrity and Product invariants must hold at the owning mutation boundary. See BIDN-REQ-021–025, BPRD-REQ-007 and DEC-0042 Implementation Boundary and Acceptance Prerequisites. |
| CONFIRMED BUSINESS DIRECTION | P1/P2 acceptance is revision-bound; P4 permits reuse only for unchanged, still-valid evidence and requires current authority for a new publication. No residual-race policy or concurrency mechanism was approved. |
| PROPOSED DISPOSITION | Revalidate current authority and relevant Product revision/state at final mutation reliance. Known withdrawal and stale/conflicting evidence prevent execution. Revalidation alone is not proof of atomic cross-system consistency. |
| Responsible accountable owners | Product: final contextual evaluation and revision integrity; Identity: current access and withdrawal; Security: acceptable reliance/failure boundary. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Confirm final-reliance and in-flight withdrawal semantics, residual race treatment, changed-state rejection and recovery obligations. Identify whether stronger coordination needs separate governance. |
| Consequence if unresolved | No cached authorization or historical snapshot may be treated as sufficient current mutation authority. |
| Implementation dependency | Trusted current evidence, revision checks and separately reviewed concurrency/recovery design; no TTL, locking, transaction or coordination strategy selected. |

### H — Protected Audit and Uncertain Outcomes

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Applicable material mutations need attributable, protected audit evidence and truthful outcomes; uncertainty cannot become successful authorization or silent replay. See Security Standards §§7.6 and 27, BPRD-REQ-044 and DEC-0042 Existing Authoritative Security and Ownership Requirements. |
| CONFIRMED BUSINESS DIRECTION | P1/P2 acceptance and P4 publication/withdrawal/archival preserve attribution, relevant revisions and history. No audit implementation, durability or uncertain-commit recovery policy has been approved. |
| PROPOSED DISPOSITION | Prevent initiation when required audit integrity cannot be assured. Preserve an uncertain commit as uncertain, and investigate/reconcile before repeating the action or reporting success. A client error alone must not be treated as proof that mutation did not occur. |
| Responsible accountable owners | Security: integrity, access control and audit guarantee; Product: mutation/outcome truth; Administration: governed investigation and recovery coordination. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Confirm required attribution and audit durability, audit-unavailable behavior, uncertain-commit investigation/reconciliation responsibility and evidence required before retry or success confirmation. |
| Consequence if unresolved | No operational mutation may rely on an assumed audit guarantee or blind retry/recovery policy. |
| Implementation dependency | Separately reviewed protected audit and mutation-outcome/reconciliation boundaries; no audit store, outbox, transport, retry or reconciliation implementation selected. |

## Founder-Operated MVP Bounded Disposition — Prepared 2026-11-11

**PROPOSED DISPOSITION — NOT APPROVED. PENDING ACCOUNTABLE PROJECT-OWNER
CONFIRMATION.** This package narrows A–H to the minimum Product publication policy
needed for the founder-operated MVP. It does not select Role or Permission identifiers,
create an assignment, approve a Staff authentication assurance mapping, close DEC-0042
S1, authorize implementation or approve the broader Administration matrix.

### MVP Product Actions and Scope

The following five actions require distinct Product-owned contextual decisions. They
are action descriptions for accountable review, not executable Permission names. One
action's eligibility or assignment grants no other action.

| MVP action | Proposed founder eligibility and required Product context |
| --- | --- |
| Draft create/edit | The founder may be eligible to create a Product in Draft and edit only an exact Draft Product under its current revision and invariants. Draft eligibility grants no acceptance, publication or withdrawal authority. |
| Content acceptance | The founder may be eligible to accept the exact Product content/configuration revision only after the applicable P1 criteria are established. Acceptance becomes stale after a covered change, invalidation or withdrawal. |
| Media acceptance | The founder may be eligible to accept the exact Product, asset and Product/Variant association revisions only after current rights, security, quality, accuracy and accessibility evidence is established. Assignment evidence cannot designate the DEC-0042 S1 checking authority. |
| Publication | The founder may be eligible to publish the exact Draft Product and revision only when every current acceptance, risk, assurance, authorization, concurrency and audit prerequisite succeeds at final mutation reliance. Publication is treated as high-impact and requires fresh privileged assurance. |
| Withdrawal | The founder may be eligible to withdraw the exact Published Product under its current revision. Withdrawal is separately authorized, does not follow from publication eligibility and must not depend on continuing publication completeness. |

For every action, positive eligibility requires all of the following: an active trusted
Staff association; a current Identity-owned assignment covering the action and exact
Product scope; Product-owned contextual Authorization for the current action, Resource,
revision and state; and trustworthy evidence of the applicable Authentication and
Session assurance. Founder status, a publisher designation, a Session, an assignment,
a Role label, a Claim, a caller boolean or prior success is insufficient by itself.

### MVP Risk and Approval Boundary

For accountable consideration, permit the same founder to perform content or media
acceptance and publication only for an **ordinary-risk Product** after every mandatory
control succeeds. Ordinary risk excludes disputed or uncertain media rights, unresolved
security findings, sensitive or restricted content, invalidated acceptance evidence,
exceptional publication handling and any other Product whose applicable risk cannot be
authoritatively classified as ordinary. Missing mandatory evidence never becomes an
ordinary-risk classification.

Higher-risk and unclassified Products require revision-bound independent approval from
an eligible, separately authorized approver. When no eligible independent approver or
required approval evidence exists, the affected acceptance or publication remains
blocked. No founder override, universal same-actor permission or universal two-person
workflow is proposed. Withdrawal remains independently authorized and is not blocked
merely because content, media or publication completeness has subsequently failed;
applicable current Staff, assurance, Authorization, concurrency and audit controls still
apply.

### MVP Current Authority, Evidence and Audit Boundary

Identity-owned assignment revocation invalidates future authorization reliance. Product
must revalidate the active Staff association, current assignment and assurance, exact
Product revision/state, accepted risk/approval evidence and contextual Authorization at
the final mutation boundary. Known revocation, changed state or revision, or missing,
stale, conflicting, invalidated, unsupported or unavailable required evidence denies the
action. Historical success and cached evidence cannot restore authority.

Publication requires attributable protected audit evidence for the actor, trusted time,
Product, action, relevant revisions, authorization/assurance context and truthful outcome.
Publication must not begin when required audit integrity cannot be assured. An uncertain
mutation outcome remains uncertain and requires governed investigation and reconciliation
before retry or success can be asserted. No audit store, transaction strategy, retry or
reconciliation mechanism is selected by this policy proposal.

### Required Accountable Project-Owner Dispositions

The project owner must explicitly accept, amend or reject this bounded package while
acting in each applicable accountable capacity; a merged proposal or implementation
does not supply the disposition.

| Accountable capacity | Exact confirmation still required |
| --- | --- |
| Product | Confirm the five-action founder eligibility, exact Product/revision/state scopes, ordinary-risk business applicability and separate withdrawal authority. |
| Security | Confirm the ordinary-risk exclusions, same-actor allowance, independent-approval boundary, high-impact publication classification, fresh privileged-assurance requirement, fail-closed evidence treatment and protected-audit boundary. |
| Identity | Confirm the trusted Staff-association, current assignment, assurance-evidence and effective-revocation responsibilities, without treating Authentication as Product Authorization. |
| Administration | Confirm the bounded founder-operated workflow, independent-approver coordination for higher-risk/unclassified Products and the absence of an operational override when no eligible approver exists. |

### Remaining Operational Gates

Even after accountable policy acceptance, operational use remains gated on accepted
DEC-0042 S1 media-security dispositions; a reviewed checking producer and evidence
boundary; operational Staff Authentication under ADR-0023; trusted Staff association,
assignment, assurance and revocation evidence; Product-owned enforcement; protected
audit and uncertain-outcome handling; applicable Contract/canonical synchronization;
implementation verification; and explicit operational activation. Product administration
and customer visibility remain default-deny until those gates are completed.

### Package Acceptance Boundary

A–H and the founder-operated MVP package all remain **PENDING ACCOUNTABLE APPROVAL**.
They do not resolve DEC-0042 S1
media-security producer, provenance, check coverage/version compatibility or
invalidation authority. Founder rights responsibility does not establish those
Security authorities. DEC-0042 P1/P2/P4 and the recorded operating model retain
their original approval scope; DEC-0042 remains Proposed and unchanged.

Accountable acceptance, applicable canonical/Contract synchronization, dependency
admission where needed, implementation authorization and executed verification
are separate gates. This package authorizes no Staff authentication, assignment,
Product mutation, positive publication or deployment. Existing default-deny
administration and fail-closed customer visibility remain required. The broader
Administration matrix remains unselected.

## Alternatives Considered — Unselected Candidates

| Policy-level candidate | Potential benefit | Question or cost to evaluate |
| --- | --- | --- |
| Fixed business-role matrix | Roles may make recurring responsibilities legible to Product and Operations. | Can fixed bundles avoid excessive privilege as operations and owning-Domain rules change? |
| Capability-oriented permission granularity within RBAC | Granular Permissions grouped into mandatory Roles may support least privilege across varied duties. | Can Product and Operations understand, assign, review, and maintain the resulting permission set? |
| Hybrid role-and-capability model | Business roles may provide a usable baseline while explicit capabilities handle exceptions. | Can exceptions be bounded, reviewed, revoked, and audited without creating hidden privilege? |
| Defer selection and retain existing governed denials | Avoids adopting an unsupported matrix now. | Which administrative work remains unavailable until a policy is approved? |

These are comparison categories only. Roles remain mandatory collections of Permissions; RBAC and current contextual Authorization remain required, and no option may bypass owning-Domain Authorization. The capability-oriented option concerns permission granularity and grouping within those existing controls; a role-free permission model is not an eligible option. No candidate is preferred, selected, rejected, or authorized for implementation.

## Evaluation Criteria and Evidence Required

Selection requires reviewable evidence for least privilege, separation of duties, business accountability, contextual Authorization, Identity trust boundaries, auditability, operational usability, maintainability, and Security reviewability. In particular:

- **Product and Operations** must identify actual administrative responsibilities, intended actors, necessary tasks, unacceptable combinations, and approval or escalation needs without treating illustrative personas as the final matrix.
- **Administration** must map proposed tasks to its Approved coordination boundary and distinguish its own workflow state from each owning Domain's authoritative effects.
- **Identity** must establish which current, trusted access evidence and assignment lifecycle can support the policy without turning Authentication, Principal, Session, Role labels, Claims, or Scope into business authorization by themselves.
- **Security** must review privilege escalation, separation of duties, revocation, stale access, sensitive actions, default denial, audit evidence, and abuse paths against [Security Standards §§8 and 12](../../.ai/core/SECURITY-STANDARDS.md).
- **Affected Domain owners** must confirm each proposed Resource/action boundary, current-state condition, and owning-Domain denial. Applicable Contracts must be reviewed before any executable surface changes.

Evidence must include positive and denied cases, cross-domain and sensitive-action cases, assignment change and revocation effects, and audit/review responsibilities. No Product or Security conclusion is inferred from the existence of a candidate option.

## Open Questions

1. Which administrative responsibilities and capabilities are needed for the current Product scope, and which examples in `PRODUCT.md` §7 remain merely illustrative?
2. Which actions require distinct permissions, separation of duties, confirmation, approval, or escalation under separately governed policy?
3. Which business owner is accountable for each permission, and which owning Domain must evaluate its Resource and current state?
4. Should permissions be expressed through fixed roles, granular capabilities, a hybrid, or another evidenced policy model?
5. How should exceptional or temporary access be governed and reviewed without assuming a support override or privileged Identity bypass?
6. What evidence demonstrates that access changes, withdrawal, and stale-context handling meet the existing Security and Identity requirements?

## Consequences and Dependencies

This proposal makes the Product policy gap and review inputs explicit. It does not alter the Approved Administration Domain, BADM, BIDN, Security Standards, or any Contract. An Accepted Product decision resolving `PRODUCT.md` §24 item 23 MUST be synchronized into the canonical Product guidance. Changes to affected Specifications or Contracts remain conditional on the selected policy and their applicable governance requirements; any material Architecture change requires a separate ADR. Until then, administrative implementation must preserve current default denial and contextual Authorization and must not embed an assumed matrix.

## Security Impact

Privileged access, excessive grants, stale assignments, cross-domain authority transfer, and inadequate auditability are material risks for the eventual decision. Existing server-side Authorization, least privilege, current trusted context, default denial, revocation, and audit controls remain in force. This proposal changes none of them and approves no privileged action.

## Data Impact

No data ownership, schema, storage, assignment representation, or retention choice is made. Identity-owned access evidence and owning-Domain business data remain separate.

## Compatibility and Migration Impact

No Contract, API, current Role assignment, migration, or compatibility behavior changes. Any future transition from existing access arrangements requires separate review after a policy is selected.

## Operational Impact

No staff provisioning, support override, escalation, approval chain, or operational procedure is authorized. Product and Operations must provide accountable task and review evidence before selection.

## Explicit Non-Authorizations

This Proposed decision does not select a final Role/Permission matrix or policy model; assign Roles to users; establish privileged Identity authority; bypass owning-Domain contextual Authorization; classify or authorize a sensitive action; create an approval or repair workflow; introduce endpoints, methods, DTOs, events, schemas, tables, storage or transaction mechanisms; select providers or dependencies; authorize implementation; or extend the backend-specification roadmap. It does not resolve another Open Product or Architecture Decision.

## References

- [AGENTS.md §§5 and 6](../../.ai/core/AGENTS.md) — authority hierarchy and work discipline.
- [PRODUCT.md §§7, 12 and 24](../../.ai/core/PRODUCT.md) — administrative personas, capabilities, and open Product Decision 23.
- [ARCHITECTURE.md §§9 and 35](../../.ai/core/ARCHITECTURE.md) — Identity/Administration ownership and completed backend-specification roadmap.
- [SECURITY-STANDARDS.md §§8 and 12](../../.ai/core/SECURITY-STANDARDS.md) — Authorization ownership and server-side controls.
- [GLOSSARY.md §§13, 18 and 22](../../.ai/core/GLOSSARY.md) — Administration and access terminology.
- [DECISIONS.md §§8–16](../../.ai/core/DECISIONS.md) — status, identifier, record structure, and review ownership.
- [DOCUMENTATION-STANDARDS.md §§7–9 and 23](../../.ai/core/DOCUMENTATION-STANDARDS.md) — metadata, lifecycle, versioning, and decision documentation.
- [Administration Domain Specification](../domains/admin/admin-domain.md) and [BADM Backend Specification](../backend/admin/admin-backend.md).
- [BIDN Backend Specification](../backend/identity/identity-backend.md).
- [Initial Customer Registration Contract](../contracts/customer/initial-customer-registration-contract.md) and [Initial Customer Login Contract](../contracts/identity/initial-customer-login-contract.md), neither of which defines this administrative matrix.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.4.0 | 2026-11-11 | Proposed | Prepared a bounded founder-operated MVP disposition for five contextual Product actions, ordinary-risk same-actor acceptance/publication, independent approval for higher-risk or unclassified Products, fresh privileged publication assurance, separate withdrawal, current authority/revision revalidation, fail-closed evidence and protected audit; identified exact project-owner confirmations and preserved all operational gates, Proposed status and non-authoritative scope. |
| 0.3.0 | 2026-11-09 | Proposed | Prepared the eight-item Product-specific accountable disposition package separating established requirements, confirmed business direction, proposed dispositions and pending owner confirmations; preserved unresolved questions, existing approval provenance, unselected matrix and all implementation restrictions. |
| 0.2.0 | 2026-11-06 | Proposed | Added a bounded Product authorization proposal and outstanding accountable questions for five acceptance/publication actions; preserved unselected matrix alternatives, existing Product approvals, Proposed status and all implementation restrictions. |
| 0.1.0 | 2026-10-13 | Proposed | Framed the open Product decision on administrative Roles and Permissions, candidate policy models, required evidence, authority boundaries, and review questions without selecting a matrix or authorizing implementation. |
