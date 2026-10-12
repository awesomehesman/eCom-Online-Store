# DEC-0012 — Administrative Role and Permission Matrix

- **Identifier:** DEC-0012
- **Title:** Administrative Role and Permission Matrix
- **Type:** Product Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-13
- **Last Updated:** 2026-10-13
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
| 0.1.0 | 2026-10-13 | Proposed | Framed the open Product decision on administrative Roles and Permissions, candidate policy models, required evidence, authority boundaries, and review questions without selecting a matrix or authorizing implementation. |
