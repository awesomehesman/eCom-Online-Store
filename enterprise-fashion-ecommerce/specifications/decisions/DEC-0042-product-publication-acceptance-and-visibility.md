# DEC-0042 — Product Publication Acceptance and Visibility

- **Identifier:** DEC-0042
- **Title:** Product Publication Acceptance and Visibility
- **Type:** Product Decision / Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-30
- **Last Updated:** 2026-10-30
- **Owner:** Product
- **Required Accountable Security Review:** Security
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Authority

The initial Product foundation supports stable identity, canonical Variant configurations,
explicit lifecycle reconstruction, create/load persistence, internal readiness assessment
and internal ID pagination. It does not establish content/media acceptance, publication
provenance or positive customer visibility. Published lifecycle alone is insufficient.

This record consolidates the Product-owner dispositions from the M1.3B assessment and
M1.3C instruction. P1–P4 are local disposition labels, not separate DEC identifiers.
The Product business policy below is explicitly owner-approved; the combined record
remains Proposed because the accountable Security choices are unresolved. This record
is not yet authoritative for implementation and does not claim canonical specification
synchronization. Existing authoritative Security requirements remain binding regardless
of this record's status.

## Accountable Dispositions and Approval Provenance

| Scope | Accountable capacity | Recorded disposition | Boundary |
| --- | --- | --- | --- |
| P1 — Content Acceptance Checklist 1.0 | Product Business Owner | APPROVED | Business acceptance policy below |
| P2 — Media acceptance and minimum image requirement | Product Business Owner | APPROVED, subject to Security controls | Product policy does not approve the security-check authority or operational controls |
| P4 — Lifecycle and acceptance reuse | Product Business Owner | APPROVED | Only the four specified transitions and reuse boundaries below |
| S1 — Media security controls | Security Owner | PENDING | Checking authority, evidence and invalidation controls require accountable confirmation |
| P3 / S2 — Contextual Staff authorization implementation | Security Owner | PENDING | Assignments, separation, authentication strength and revalidation choices remain unresolved |

Approval provenance is the repository owner's explicit session statement that the Product
Business Owner approved P1, P2 and P4, reiterated in the M1.3C instruction to record those
confirmed decisions. Recorded on 2026-10-30; a separate original approval date was not
supplied. No Security approval, independent reviewer, signature or independent human
review is claimed. A recommendation or an unselected ACCEPT / REJECT / AMEND field is
not an accountable disposition.

## Product-Owner-Approved Business Policy

### P1 — Content Acceptance Checklist 1.0

Acceptance must verify:

- A nonblank, accurate Product name and description.
- Supportable material claims and accessible, understandable customer-facing content.
- Valid Product family, canonical Attributes, explicit applicability and Variant
  configurations under Accepted DEC-0019, preserving Product-scoped uniqueness.
- Acceptance bound to the exact Product content/configuration revision.

Acceptance is attributable, versioned and auditable. Covered content/configuration
changes, withdrawn supporting facts or explicit revocation invalidate reliance on the
corresponding acceptance. Valid Attributes alone cannot establish publication eligibility.
This checklist introduces no additional mandatory descriptive field catalogue, SKU format,
Colour vocabulary or change to DEC-0019.

### P2 — Media Acceptance

Require revision-bound evidence of correct Product/Variant association, permitted
storefront usage rights, security acceptance, accurate and usable representation,
meaningful accessibility information, and immutable asset and association revision
references. A linked Variant must belong to the owning Product.

Initial publication requires **at least one accepted Product-level image**. A separate
image for every Variant is not universally required. Withdrawal or invalidation of the
last required accepted image must immediately prevent positive Product visibility.
Availability, upload or processing success is not acceptance. Replacing asset content
or relevant association meaning cannot silently reuse evidence for the previous revision.

The numerical minimum is newly recorded Product-owner-approved policy, not a claim that
the existing Domain, backend or catalogue Contract previously specified that minimum.
Its implementation remains conditional on accountable Security controls and governing-source
synchronization; this step changes no Contract or Approved Specification.

### P4 — Lifecycle and Acceptance Reuse

The approved initial transitions are:

| Source | Target | Required boundary |
| --- | --- | --- |
| DRAFT | PUBLISHED | Current applicable acceptance, publication authorization, concurrency protection and attributable audit evidence |
| PUBLISHED | DRAFT | Explicit authorized withdrawal with concurrency protection and attributable audit evidence |
| PUBLISHED | ARCHIVED | Explicit authorized archival with concurrency protection, audit and historical-reference preservation |
| DRAFT | ARCHIVED | Explicit authorized archival with concurrency protection, audit and historical-reference preservation |

No initial Archived reactivation is supported. Publication-relevant edits require explicit
withdrawal to Draft before modification; edits cannot silently transition lifecycle.
Acceptance may be reused only when the exact accepted subject is unchanged, applicable
criteria remain valid and no relevant invalidation exists. Reuse does not reuse a withdrawn
publication grant or authorize republishing: a new publication action needs current authority.

Evidence invalidation must prevent unsafe visibility without silently changing lifecycle.
Unpublication and archival need not wait for content to become publishable. Stable Product
and Variant identity, accepted membership and historical commercial snapshots remain protected.

## Existing Authoritative Security and Ownership Requirements

These obligations are inherited, not new pending recommendations:

- Enforce current server-side contextual Authorization at the trusted entry point and owning
  Use Case, with default denial and exact Resource, action and applicable state checks.
- Identity supplies trusted Principal/access evidence; a Session, Role label, Claim,
  Permission value or identifier alone cannot authorize Product actions.
- Product owns its invariants and contextual business Authorization. Administration coordinates
  governed Staff workflows without acquiring Product publication authority.
- Private, Draft, invalid, unauthorized or otherwise non-publishable media cannot be public;
  applicable rights, security, quality, accuracy and accessibility requirements remain binding.
- Material publication, withdrawal and archival require attributable audit evidence. Audit
  records must preserve applicable actor/system, trusted time, Resource, action, state/outcome,
  authorization context and correlation, with access control and tamper protection.

Security Standards §§12 and 27, REQ-PRD-014–020, 043, 049–050, BPRD-REQ-007, 037, 044,
BIDN-REQ-004, 021, 024–026 and REQ-ADM-007, 014, 023 establish these boundaries.

## Recommended Security Controls — Awaiting Confirmation

### S1 — Media Security Acceptance

Recommend authoritative, revision-bound evidence identifying the security check and its
version, result, checking source and exact asset/association revision. Missing, failed,
stale, withdrawn or invalidated evidence must never grant positive visibility. Security
must confirm the accountable checking authority, acceptable supporting evidence, version
compatibility, invalidation triggers, finding/withdrawal handling and controls excluding
private references or credentials from public media.

The fail-closed obligation follows existing requirements; the specific evidence producers,
check definitions and operational implementation are not approved by this record.
No independent Media service, scanner, provider or pipeline is selected.

### P3 / S2 — Contextual Staff Authorization

Recommend separate contextual decisions for content acceptance, media acceptance,
publication, withdrawal and archival, each bound to its Product and applicable revisions/state.
No named Permission, Role hierarchy, Staff assignment or emergency override is invented.
No mandatory dual approval is introduced by this proposal; any applicable approved Security
policy remains binding, and permission for the same actor to accept and publish is not assumed.

Security confirmation remains required for:

1. Applicable governed Staff assignments for each action.
2. Whether the same actor may accept and publish.
3. Any stronger Authentication or independent approval requirement.
4. Authorization revalidation and in-flight access-withdrawal handling.
5. Security events invalidating current acceptance or publication reliance.

DEC-0012 remains Proposed; this record neither resolves its matrix nor invents assignments.

## Implementation Boundary and Acceptance Prerequisites

After remaining accountable review and required governing-source synchronization, implementation
must preserve revision-bound provenance and explicit invalidation rather than accept caller
authorization/completeness booleans. Product application services own transactions under
Architecture §35.2 and BPRD-REQ-011; narrow persistence operations must preserve atomicity,
concurrency, identity and historical integrity. Final positive reliance must coordinate with
withdrawal: a historical consistent snapshot alone cannot prove current visibility at response
authorization. No locking algorithm or evidence schema is selected here.

Customer visibility remains separately evaluated from lifecycle, content/media acceptance
and command Authorization. Missing, stale, contradictory or unavailable authority cannot
produce positive visibility or a stale fallback; uncertainty cannot become fabricated absence.
Children are independently evaluated without falsely satisfying Product completeness.
Internal ID pages are not eligible public catalogue pages.

Before overall acceptance, record accountable Security dispositions and resolve the operational
choices above. Acceptance of this record must follow DECISIONS.md §§8–17 and applicable owner
review; no additional Product approval of the already confirmed P1/P2 business direction or
P4 policy is requested. Canonical synchronization and implementation verification remain separate.

## Alternatives Considered and Consequences

- Retain only conversational dispositions: avoids a document but loses durable attributable
  policy and Security boundaries; not selected for this recording task.
- Separate Product, media and authorization records: permits separate lifecycles but adds
  coordination and duplication; one combined record preserves distinct dispositions instead.
- Published lifecycle alone, caller acceptance flags or automatic reactivation: incompatible
  with approved completeness, authorization and currentness requirements; not admissible.
- Multiple simultaneous published/editing revisions or mandatory dual approval: add complexity
  or Security policy not approved here; the confirmed Draft-editing policy and pending Security
  choices remain explicit.

The selected recording approach enables bounded implementation planning without misrepresenting
Security acceptance. Draft-only edits can interrupt visibility; minimum-image withdrawal can
withhold an otherwise structurally valid Product. Rights/security withdrawal must invalidate
reliance on every affected association rather than depend on non-authoritative Search updates.

## Data, Compatibility, Migration and Operational Impact

Future evidence must preserve subject identity/revision, acceptance criteria, source/actor,
trusted time, invalidation and audit correlation. Evidence must not silently reinterpret old
Variants or confirmed Order/commercial snapshots. This record selects no database layout,
migration, retention duration, infrastructure, public cursor or event-delivery mechanism.
No existing PUBLISHED row is grandfathered into accepted publication evidence.

Product retains identity, content, media associations and publication authority. Category,
Search, Pricing and Inventory retain their existing authority; Product visibility proves
neither final purchasability nor Customer/market eligibility. CMS approval/scheduling policy
is separate; this record does not resolve DEC-0020.

## Explicit Non-Authorizations

The overall status is **Proposed**, not Accepted. This record grants no positive visibility,
publication command, production rollout, Security acceptance, dependency admission or
Architecture adoption. It changes no Product code, migration, API Contract, Approved Product
Specification, DEC-0019, canonical PRODUCT.md or ARCHITECTURE.md. Only the required decision-index
registration accompanies this record. No independent review or implementation readiness beyond
the stated policy/remaining-gate distinction is claimed.

## References

- [DECISIONS.md §§8–17](../../.ai/core/DECISIONS.md), [PRODUCT.md §§16.8–16.9, 16.13](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §35.2](../../.ai/core/ARCHITECTURE.md).
- [Product Domain](../domains/product/product-domain.md): REQ-PRD-005–020, 023–031, 037, 043, 049–050.
- [Product Backend](../backend/product/product-backend.md): BPRD-REQ-007, 011, 019–030, 037–039, 044.
- [Initial catalogue Contract §§7–13](../contracts/product/initial-product-catalogue-contract.md) and [OpenAPI](../contracts/product/initial-product-catalogue.openapi.yaml).
- [Security Standards §§12, 27](../../.ai/core/SECURITY-STANDARDS.md), [Identity Backend](../backend/identity/identity-backend.md) BIDN-REQ-004, 021, 024–026, and [Administration Domain](../domains/admin/admin-domain.md) REQ-ADM-007, 014, 023.
- [Accepted DEC-0019](DEC-0019-product-variant-attribute-standards.md), [Proposed DEC-0012](DEC-0012-administrative-role-permission-matrix.md) and [Proposed DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md).

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-30 | Proposed | Recorded confirmed Product-owner P1/P2/P4 policy separately from inherited Security requirements and pending S1/P3 controls; preserved fail-closed visibility, lifecycle/history boundaries and separate acceptance, synchronization and implementation gates. |
