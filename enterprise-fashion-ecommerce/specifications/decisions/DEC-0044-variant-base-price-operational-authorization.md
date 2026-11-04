# DEC-0044 — Variant Base-Price Operational Authorization

- **Identifier:** DEC-0044
- **Title:** Variant Base-Price Operational Authorization
- **Type:** Product Decision / Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-11-04
- **Last Updated:** 2026-11-04
- **Owner:** Product
- **Required Accountable Security Review:** Security
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## 1. Context and Decision Scope

Accepted DEC-0043 establishes Variant-level positive ZAR base pricing, single-current
selection and revision-preserving replacement/withdrawal. It requires an authorized,
committed operation but selects no Staff assignments or authorization mechanism.
DEC-0012 remains Proposed with no administrative matrix selected. Approved Pricing,
Identity, Security and Administration requirements establish ownership and default-deny
obligations; they do not identify eligible Staff assignments for these three actions.

The implementation has internal atomic initialize/replace/withdraw persistence, expected
revision checks, immutable history and terminal-head integrity. The Approved Product
internal Variant evidence Contract and conditional Pricing query can observe association,
lifecycle and independent Pricing state. Neither supplies actor authority, a trusted
mutation audit guarantee or an atomic Product/Pricing snapshot. Internal persistence
is not an operational authorization API.

This package presents bounded choices for accountable review. **NO OPTION IS SELECTED
OR APPROVED.** Recommendations below are not dispositions. This record is not
implementation authority. It changes no DEC-0012, DEC-0043, Approved Specification,
Contract, code or infrastructure. A required Decision Index entry accompanies it.

## 2. Existing Obligations — Not New Policy Choices

- Identity supplies trusted Authentication, Principal, Session and access-assignment
  evidence. Authentication alone does not establish Staff status or permission to act.
- Security governs access controls and assurance. Pricing enforces current contextual
  authorization at its owning Use Case, preserving externally governed access policy.
- Administration coordinates permitted Staff workflows and invokes Pricing; it does not
  acquire Pricing business authority or issue trusted Identity evidence.
- Product owns stable Product/Variant identity, membership, lifecycle and representation
  revision. Category, Search, Inventory and Pricing cannot supply substitute Product truth.
- Authorization must deny by default and evaluate the current Principal, exact Resource,
  action and applicable state. Role labels, identifiers, membership, UI and prior responses
  alone cannot grant authority. Enforcement applies at the trusted entry point and owning
  Use Case; no Staff endpoint is selected by this package.
- Pricing replacement/withdrawal must preserve historical revisions and concurrency safety.
  Assignment history alone does not satisfy proportional trusted mutation audit requirements.

Sources: REQ-PRC-004–008, 025–026, 031; BPRC-REQ-007, 009, 014, 017, 019, 033, 037;
BIDN-REQ-004, 021, 024–026; REQ-ADM-005–010, 025; Security Standards §§12, 13 and 27.

## 3. Separate Protected Operations and Resource Binding

| Business operation | Required distinct authorization scope | Existing Pricing consistency precondition |
| --- | --- | --- |
| Initialize | Current actor may initialize a base-price assignment for this exact Product/Variant pair in its observed Product state. | No existing assignment subject; duplicate identity must not overwrite or reset retained history. |
| Replace | Current actor may replace the assignment for this exact pair, observed Product state and expected current Pricing revision. | Expected revision must match; replacement retains prior history and advances one head atomically. |
| Withdraw | Current actor may withdraw the assignment for this exact pair, observed Product state and expected current Pricing revision. | Current assignment and expected head must match; withdrawal retains history and cannot restore an old price. |

These are business action descriptions, **not Permission identifiers or assigned Roles**.
Permission for one operation must not imply permission for either other operation.
Any future policy grouping must explicitly authorize each action rather than rely on
an assumed hierarchy. Product evidence must match both supplied references. Do not
infer the owning Product from a Variant reference or accept a caller's ownership claim.

## 4. Choice A — Eligible Staff Assignments and DEC-0012 Dependency

Governing sources: DEC-0012; BIDN-REQ-024–026; REQ-ADM-005–010; REQ-PRC-026;
BPRC-REQ-009; Security Standards §12.

| Option | Trade-off | Recommendation |
| --- | --- | --- |
| A1 — Wait for the complete DEC-0012 matrix | Ensures one comprehensive administrative policy but couples three Pricing operations to unrelated capability decisions. | Viable if owners require whole-matrix acceptance. |
| A2 — Establish the bounded Pricing portion through DEC-0012 governance | Resolve only the three action-to-eligible-assignment mappings with attributable Product, Security, Identity, Administration and Pricing review. Broader matrix remains unresolved. Requires explicit agreement on how the accepted bounded portion is recorded and synchronized. | Recommended for bounded progress; not a bypass or implied acceptance of DEC-0012. |

For A2, owners must explicitly record the actual eligible governed assignments for
initialize, replace and withdraw, including scope and withdrawal effects. This package
invents none. A Role name, proposed label, session presence or an empty mapping cannot
substitute for approved assignments. No ad hoc maintainer allowlist, hardcoded user,
customer-to-Staff promotion or development override is proposed.

Accountable decision: Product/business owner with Security concurrence; Identity and
Administration confirm assignment provenance and administration boundaries; Pricing
confirms owning-operation scope. DEC-0012 remains Proposed until separately governed;
this record neither edits nor supersedes it. Even acceptance of this package without
concrete approved mappings would leave operational writes blocked.

## 5. Trusted Evidence, Outcomes and Responsibility

Future actor/access evidence must be Identity-produced or server-verified, attributable
to a stable Principal, bound to the relevant current Session/access context, and valid
under applicable expiry, revocation and effective access-withdrawal requirements.
It must establish the approved Staff assignment and permitted scope without trusting
request-supplied Staff IDs, Roles, Permissions or actor claims. Only necessary evidence
may cross the owning Contract boundary; no token format, provider or transport is selected.

Pricing must combine trusted actor/access evidence, the approved action mapping and
exact Product/Variant/state context. Product membership is a fact input, not a grant.
Administration may coordinate, but neither Administration nor Identity replaces the
Pricing contextual decision. Existing Security assurance remains binding.

Propose these distinct internal semantic outcomes; they are not HTTP or Java names:

| Outcome | Meaning | Write consequence |
| --- | --- | --- |
| Authorized | Trusted, current evidence and approved policy affirm this actor/action/resource/state scope. | Necessary but not sufficient: Product, Pricing revision and audit preconditions still apply. |
| Denied | A trustworthy policy evaluation establishes that the action is not permitted. | No write. Do not disclose protected Resource existence. |
| Authority Unavailable | The required Identity or authorization authority cannot provide a trustworthy current decision. | No write; no cached allow, fabricated denial or successful empty result. |
| Invalid or Inconsistent Authority Evidence | Input/evidence is malformed, mismatched, contradictory, incomplete or from an untrusted source. | No write; safe reason classification, no raw credentials or internal failure disclosure. |

The four-way distinction is recommended over a boolean or a collapsed denied/unavailable
outcome because it preserves truthful recovery and security handling without weakening
default denial. Alternative: retain distinct typed exceptions at a trusted boundary,
provided callers preserve the same four meanings. Security and Pricing must confirm the
semantic contract; Identity reviews trusted provenance. No access mechanism is approved.

### Assurance and Per-Operation Authorization Choice

Sources: REQ-PRC-026; BPRC-REQ-009; REQ-ADM-007, 009; Security Standards §§12–13, 27.
Current authorization must be re-evaluated for every operation; reusing a prior allow
as continuing authority is not an admissible alternative. Effective access withdrawal
must prevent reliance on known stale access before the write decision.

Options for accountable Security classification are: (1) per-action assurance under
existing privileged-session/MFA/reauthentication controls without an additional blanket
independent-approval rule; or (2) additional independent approval for specifically
classified high-impact actions. Recommend option 1 only where Security confirms it is
adequate; otherwise apply option 2 to the identified actions. Neither option relaxes
existing mandatory assurance. Security must record action classifications, whether the
same actor may perform the applicable actions, and required reauthentication/approval
conditions. Product/Pricing confirm business impact. No dual-approval hierarchy is
invented or silently ruled out, and no concrete assignment is granted.

## 6. Choice B — Product Lifecycle Eligibility

Actual governed Product values are **DRAFT, PUBLISHED and ARCHIVED** (REQ-PRD-005;
BPRD-REQ-020). Active, Suspended, Unpublished and Retired are not additional lifecycle
values. Lack of publication evidence is not a new state. Published alone does not
establish customer visibility, structural sellability or commercial Price applicability.

The table proposes state eligibility only. Every permitted cell remains conditional
on separate current action authorization, matching Product evidence, Pricing consistency
and audit requirements. No cell authorizes a Product lifecycle transition.

| Option | DRAFT initialize / replace / withdraw | PUBLISHED initialize / replace / withdraw | ARCHIVED initialize / replace / withdraw |
| --- | --- | --- | --- |
| B1 — Draft preparation and live maintenance | Permit / Permit / Permit | Permit / Permit / Permit | Reject / Reject / Permit |
| B2 — Draft-only price establishment | Permit / Permit / Permit | Reject / Reject / Permit | Reject / Reject / Permit |
| B3 — Published-only price establishment | Reject / Reject / Permit | Permit / Permit / Permit | Reject / Reject / Permit |

Recommend **B1**: supports Draft preparation and live price maintenance while preventing
new/replacement prices for Archived Products; withdrawal remains possible for historical
cleanup or risk reduction. Cost: Published price changes need appropriate action-specific
controls and audit. B2 restricts live changes and may require an independently authorized
Product workflow; this package does not create one. B3 prevents Draft pre-pricing and
may leave newly Published Variants unpriced. All are unselected options, not claims that
existing Product lifecycle policy already governs Price administration.

Sources: DEC-0043; REQ-PRC-004–005, 007–008; BPRC-REQ-017, 019; REQ-PRD-005,
007–010, 052; BPRD-REQ-014–016, 020. Accountable decision: Product/business owner and
Pricing, with Product producer confirmation and Security review of state-bound authority.
A missing Product/association or unavailable/invalid evidence never qualifies through
this matrix. Confirmed Draft membership may support an approved administrative action;
it cannot support trusted customer-facing commercial applicability by itself.

## 7. Choice C — Product Revalidation and Cross-Domain Race Boundary

The Approved internal Variant evidence Contract §§5–6 already requires a new coherent
Product observation for each future operation relying on those facts. Use explicit
Product and Variant references and the observed lifecycle/revision. A representation
revision is a comparison token, not a timestamp or proof of continuing freshness;
lifecycle can change without a representation-revision change.

| Option | Trade-off | Recommendation |
| --- | --- | --- |
| C1 — Explicit bounded-observation reliance | Obtain Product evidence for each operation outside unsafe Pricing transaction scope; authorize against the exact observed pair/state and current access evidence, then enforce the expected Pricing revision atomically. Known material Product change, invalidation or uncertainty before reliance requires renewed observation/re-evaluation or a safe stop. An unobserved intervening Product change remains a stated race limitation. | Recommended only with explicit owner acceptance of this limitation for the selected operations. |
| C2 — Require stronger commit-time Product validity | Do not enable affected operations until Product/Pricing owners establish an approved stronger guarantee and Contract; assess any material Architecture implications separately. | Appropriate if the business cannot accept C1; this package selects no mechanism. |

Neither option permits a known failed membership/state check to be ignored. No TTL,
distributed lock, cross-module transaction, Product database lock, reservation or shared
snapshot is introduced. Product and Pricing retain independent coherent observations.
Authorization and Product revalidation are separate obligations; one cannot replace the
other. Do not hold external authority calls inside unsafe Pricing transactions or claim
that the Pricing head lock freezes Product or Staff access.

Sources: internal Variant evidence Contract §§5–7; REQ-PRC-004–005, 024–026;
BPRC-REQ-007, 011–014, 017, 033; BPRD-REQ-011. Product/Pricing owners must decide the
acceptable validation point and race tolerance; Security confirms access-withdrawal
reliance, Engineering/Architecture confirms technical limits. No stronger consistency
guarantee is inferred from current PostgreSQL co-location.

## 8. Mutation Audit and Failure Policy Choice

Existing REQ-PRC-031, BPRC-REQ-037 and Security Standards §27 require proportional
trusted audit for applicable material/high-risk mutations and security-relevant events.
The minimum evidence, where applicable, is a server-controlled trusted timestamp,
correlation, trusted Principal/system and source context, action, exact Product/Variant,
old/new Pricing revision or safe state summary, authorization decision context, outcome,
safe reason category and approving Principal where governed. Requested actions, denied
attempts and successful commits must not be conflated. No caller-supplied actor or
successful-mutation claim may become trusted audit merely by storage.

Audit records require access control, tamper protection, separation from the actor
being audited, monitoring of ingestion failure and governed retention. Immutable Pricing
revisions preserve commercial history but do not themselves establish trusted actor,
authorization, recording time or audit persistence. No retention duration is selected.

Options: D1 — treat all three initial operational actions as requiring mutation audit
and block writes when the required recording guarantee is unavailable; D2 — apply an
explicit owner-approved per-action/risk classification, with required evidence and safe
failure behavior for each category. Recommend D1 for the bounded initial operational
scope, without inventing a storage technology or claiming a write happened before commit.
D2 may reduce overhead but must not exempt an action already requiring audit under
higher authority. The required durable outcome-recording guarantee and handling of uncertain commit or
audit outcomes require a technical Contract before operational enablement; no generic
logger or history row may stand in for that guarantee. Security and Pricing approve
classification and failure policy; Engineering reviews achievable durability semantics.

## 9. Required Accountable Dispositions — All Pending

| Capacity | Disposition | Required decision/review |
| --- | --- | --- |
| Product / business owner | PENDING | Eligible business assignments and DEC-0012 resolution path; lifecycle option B; operation impact and bounded-observation acceptance. |
| Security | PENDING | Assignment scope, four authorization outcomes, assurance/separation, access withdrawal, race reliance and audit classification/failure controls. |
| Pricing | PENDING | Action-specific contextual enforcement, lifecycle/revalidation implications, revision conflicts, outcomes and audit requirements. |
| Identity | PENDING | Feasibility and provenance of current actor/Staff/access evidence and effective withdrawal; no central business authorization. |
| Administration | PENDING | Bounded DEC-0012 mapping and coordination responsibilities; no assignment or commercial authority inferred. |
| Engineering/Architecture | PENDING | Exported Contracts, consistency limitations, audit guarantees and implementation feasibility; separate ADR only if a material new architecture choice is needed. |

No owner disposition, independent reviewer, signature or completed review is claimed.
Approval must explicitly select/amend the options, record concrete assignment mappings
and resolve their DEC-0012 relationship. Recommendations or approval of a principle
without the necessary mappings and operational Contracts do not unblock writes.

## 10. Consequences, Compatibility and Implementation Gates

The package makes the minimum three-action boundary reviewable without opening the
entire administrative matrix. Costs include maintained assignment mappings, per-operation
observations, assurance and trusted audit. C1 accepts a documented race window; C2 delays
operations pending a stronger guarantee. Lifecycle restrictions can interrupt ordinary
price maintenance. Later policy changes must preserve stable Product/Variant identity,
accepted assignment history and confirmed Order snapshots.

Before operational implementation, record accountable acceptance, synchronize applicable
governing sources/Contracts, establish trusted actor/access and audit capability, and
verify denial/unavailability, scope substitution, state eligibility, revoked access,
concurrent revision conflict, rollback and uncertain outcomes. Existing internal storage
and conditional reads remain non-authorizing. No Java signature, Permission identifier,
Role hierarchy, Price identifier, schema, migration, HTTP route, tax, rounding, Discount,
Promotion, Checkout, Order, publication or Inventory policy is selected here.
This Proposed record authorizes no code change, rollout or write orchestration.

## 11. References

- [Accepted DEC-0043](DEC-0043-initial-v1-base-price-assignment-policy.md).
- [Proposed DEC-0012](DEC-0012-administrative-role-permission-matrix.md).
- [Pricing Domain](../domains/pricing/pricing-domain.md): REQ-PRC-004–008, 024–026, 031.
- [Pricing Backend](../backend/pricing/pricing-backend.md): BPRC-REQ-007, 009, 011–014, 017, 019, 033, 037.
- [Product Domain](../domains/product/product-domain.md): REQ-PRD-003, 005, 007–010, 052.
- [Product Backend](../backend/product/product-backend.md): BPRD-REQ-011–012, 014–016, 020.
- [Approved internal Variant evidence Contract](../contracts/product/internal-variant-evidence-contract.md).
- [Identity Backend](../backend/identity/identity-backend.md): BIDN-REQ-004, 021, 024–026.
- [Administration Domain](../domains/admin/admin-domain.md): REQ-ADM-005–010, 025.
- [Security Standards §§12–13, 27](../../.ai/core/SECURITY-STANDARDS.md).
- [Decision conventions](../../.ai/core/DECISIONS.md), [Architecture §§35.2, 35.5](../../.ai/core/ARCHITECTURE.md).

## 12. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-04 | Proposed | Presented bounded unselected Staff/action, lifecycle, Product revalidation/race, authorization-outcome and trusted audit options for Variant base-price operations; recorded all accountable dispositions pending and preserved DEC-0012, DEC-0043 and existing authority boundaries. |
