---
title: Internal Base-Price Authorization Contract
version: 0.1.0
status: Draft
owner: Pricing
last_updated: 2026-11-04
authoritative: false
---

# Internal Base-Price Authorization Contract

## 1. Purpose, Identity and Authority

This Pricing-owned internal Contract is identified by its canonical path,
`specifications/contracts/pricing/internal-base-price-authorization-contract.md`, title
and version. It introduces no sequential Contract identifier. Version 0.1.0 is **Draft**,
not an Approved implementation baseline. It grants no operational Pricing write authority.

Accepted DEC-0043 governs the initial Variant-level positive ZAR base-price policy.
DEC-0044 0.2.0 remains Proposed and non-authoritative: the Product/business owner approved
A2, B1, C1 and D1 on 2026-11-04 in that capacity only. Security and other accountable
reviews remain pending. DEC-0012 remains Proposed; no concrete Staff assignments are
approved by this document. The selected M2.5E technical direction informs this Draft,
not an inferred owner acceptance of its complete semantics or dependencies.

Sections below distinguish existing obligations from proposed contract specialization.
They do not amend Approved specifications, DEC-0012 or DEC-0044. There is no HTTP,
Staff endpoint, executable Java API, audit infrastructure or persistence implementation
in this increment. Existing governing requirements remain binding independently.

## 2. Ownership and Trust Boundary

| Owner | Responsibility | Non-authority |
| --- | --- | --- |
| Pricing | Enforce contextual authorization for the exact base-price operation at its owning Use Case and preserve Pricing invariants. | Cannot manufacture Staff assignments, authentication or Product truth. |
| Identity | Supply trusted current actor, Session and access-assignment evidence through an approved server-side boundary. | Access evidence alone cannot authorize a Pricing business operation. |
| Security | Govern access policy, least privilege, assurance, withdrawal and applicable audit controls. | This Draft records no Security disposition or specific assignment mapping. |
| Administration | Coordinate governed Staff assignment workflows and invoke owning capabilities; consume Identity-owned assignment truth. | Cannot issue trusted Identity evidence, grant implicit Pricing authority or create a second access model. |
| Product | Supply association, observed lifecycle and representation-revision evidence through its Approved internal Variant evidence Contract. | Membership and lifecycle do not grant Staff authority or commercial Price applicability. |

These boundaries follow BIDN-REQ-004, 024–026, REQ-ADM-005–010, 025,
REQ-PRC-026 and BPRC-REQ-007, 009. Authentication and authorization remain distinct.
Trusted entry-point enforcement does not replace owning-Use-Case enforcement.

## 3. Immutable Action-Specific Intents

Propose three separate business actions. Their names are not Staff Permission identifiers,
Role names or an authorization hierarchy. Permission for one does not imply another.

| Intent | Required immutable content | Pricing precondition, separate from authorization |
| --- | --- | --- |
| Initialize Variant base price | Exact Product reference, exact Variant reference, proposed strictly positive ZAR Money, operation correlation. | No assignment subject already exists; initialization cannot overwrite or reset retained history. |
| Replace Variant base price | Exact Product reference, exact Variant reference, proposed strictly positive ZAR Money, expected current Pricing revision, operation correlation. | Expected head revision must match; preserve prior revisions and advance atomically. |
| Withdraw Variant base price | Exact Product reference, exact Variant reference, expected current Pricing revision, operation correlation. | A current assignment must be withdrawable under existing rules; preserve history and never restore an earlier price. |

Reuse governed reference and Money semantics. Do not infer the Product from the Variant,
normalize unrelated references into equivalence, introduce Price identifiers or select
rounding, scale, conversion or a Product-level fallback. Expected revisions are opaque
comparison references, not ordered precedence. Initialize has no invented expected-revision
sentinel. This Contract selects no new revision-generation or correlation format.

The intent must remain unchanged throughout evaluation and attempted execution. An amount,
action, resource, expected revision or correlation substitution invalidates reliance on
that decision. A changed intent requires fresh evaluation, not an amended allow result.

## 4. Trusted Actor and Access Evidence

Actor/access context must originate from a trusted server-side Identity boundary and
be current, purpose-appropriate and attributable to the actual Principal. Applicable
Session validity, expiry, access assignment, assurance and effective withdrawal evidence
must be verified. Authentication alone cannot establish Staff access or action permission.

The caller must not supply authoritative Staff IDs, Roles, Permissions, Claims, assurance
flags or actor attribution through request fields, headers, cookies or constructed DTOs.
A transported credential is not trusted until verified by the owning security boundary.
Do not use a caller-controlled actor identifier or a public record as proof of provenance.

**Unresolved prerequisite:** the Identity-owned actor/access provenance Contract, including
its acquisition path, required evidence, effective withdrawal and safe failure semantics,
is not established by this Draft. No trusted actor DTO, token format, identity issuer,
provider or evidence factory is invented. If that trusted boundary is missing or cannot
supply valid evidence, operational authorization cannot return Authorized.

## 5. Conceptual Authorization Request and Binding

The conceptual internal request contains:

- One immutable action-specific intent from §3.
- Matching Product `ConfirmedAssociation` evidence from the Approved Product Contract.
- Operation correlation, consistently bound to the same intent; differing copies are invalid.
- Trusted actor/access context acquired through the approved Identity boundary, not supplied
  as an authoritative caller claim. Its concrete representation remains unresolved.

Pricing verifies that Product and Variant references match across the intent and Product
evidence. The observed lifecycle and Product representation revision remain part of the
evaluated context, alongside the actual proposed Money and expected Pricing revision
where applicable. Product evidence confirms membership only in its observation; it does
not supply actor access or authorize the action.

Future binding must connect any allow decision to the trusted actor/access context,
exact immutable intent, matching Product observation and applicable governed policy context.
It cannot be transferred to a different actor, resource, action, amount or operation.
This is a semantic obligation, not a selected decision-token format or reusable capability.

## 6. Four Mutually Exclusive Authorization Outcomes

| Outcome | Meaning | Mutation rule |
| --- | --- | --- |
| Authorized | Current trusted evidence and approved policy affirm the exact evaluated actor/action/resource/state context. | May proceed only to remaining write-precondition checks. Does not itself authorize a successful commit, audit claim or commercial Price applicability. |
| Denied | A trustworthy current policy evaluation establishes that this operation is not permitted. | No mutation. Preserve safe refusal semantics without unnecessary Resource-existence disclosure. |
| Authority Unavailable | Required Identity or authorization authority cannot provide a trustworthy current decision, including recognized dependency, resource, timeout or transaction availability failures. | No mutation. Do not substitute a cached allow, fabricated denial, absence or empty success. |
| Invalid/Inconsistent Authority Evidence | Required input/evidence is malformed, untrusted, incomplete, substituted, contradictory or inconsistent with the evaluated operation. Unexpected internal failures remain separately identifiable through a safe internal-failure reason. | No mutation. Do not disclose credentials, exception messages, SQL or infrastructure details. |

The selected technical direction is four explicit semantic alternatives, not HTTP codes
or approved Java names. Recognized wrapped availability failures must retain unavailable
meaning rather than be treated as malformed evidence merely because they are wrapped.
An ordinary programming or non-availability data failure is not automatically unavailable.

An Authorized result must bind the exact context in §5. It is not a reusable credential,
continuing permission, substitute for live Identity evidence, or approval to change the
intent. Missing, null or unrecognized decisions cannot grant mutation. No generic boolean,
Role label, `authenticated()` fallback or previous success substitutes for these outcomes.

## 7. Product Lifecycle Eligibility — Business Direction, Operational Policy Pending

DEC-0044 B1 records the following Product/business-approved direction. It remains a
**proposed operational lifecycle policy** because DEC-0044 is Proposed and remaining
accountable reviews and implementation gates are unresolved.

| Observed Product lifecycle | Initialize | Replace | Withdraw |
| --- | --- | --- | --- |
| DRAFT | Eligible | Eligible | Eligible |
| PUBLISHED | Eligible | Eligible | Eligible |
| ARCHIVED | Ineligible | Ineligible | Eligible |

These are the existing governed Product lifecycle values; no Active, Suspended,
Unpublished or Retired state is introduced. Eligibility is a state precondition,
not an authorization grant. Ineligible operations cannot mutate even after an allow
decision. Withdrawal eligibility cannot bypass missing membership, current authorization,
revision consistency or audit guarantees. No Product lifecycle transition is introduced.

Neither membership, Published state, a valid Price nor this matrix establishes publication
acceptance, customer visibility, structural sellability, Inventory availability, Customer
purchase eligibility, tax-inclusive display amounts, Checkout totals or Order eligibility.

## 8. Revalidation, Withdrawal and Concurrency

Obtain a new coherent Product observation and a current authorization decision for each
operation. Do not reuse cached association or authorization as continuing authority.
Known material Product, intent, access or policy changes and relevant invalidation before
reliance require renewed observation/evaluation or a safe stop. Known expired or withdrawn
access cannot support mutation. Evidence validity and authorization are separate checks.

The Product revision is an opaque comparison token, not continuing freshness, a timestamp
or a monotonic change counter. Lifecycle can change without that revision changing.
Product and Pricing retain independent coherent observations and transaction ownership.
Do not hold external authority calls inside unsafe Pricing transactions.

C1's Product/business acceptance acknowledges that an unobserved Product change may occur
between observation and Pricing persistence. It does not guarantee a shared instant,
continuing Product validity or valid Staff authority after withdrawal. It cannot weaken
Identity/Security revocation requirements. Security disposition of the authorization
write-decision point and in-flight access withdrawal remains pending. No TTL, artificial
freshness guarantee, distributed lock, Product lock or shared Product/Pricing transaction
is selected. A stronger guarantee, if required later, needs its own approved Contract.

Use existing database-enforced Pricing expected-revision checks for replacement/withdrawal
and uniqueness for initialization. Application-only revision comparison is insufficient.
Concurrent conflict cannot silently overwrite accepted state. Preserve immutable history,
one terminal head and atomic Pricing effects; no arbitrary latest-wins resolution or
blind retry of uncertain effects. These are Pricing-local safeguards, not Product locks.

## 9. Security Assurance — Existing Controls and Pending Classification

Existing Security Standards §§10–13 require unique trusted identities, least privilege,
server-side contextual authorization and applicable Session expiry/revocation controls.
MFA must protect privileged Staff; privileged identities require phishing-resistant MFA
where supported. Privileged Sessions require shorter limits and reauthentication for
high-impact actions. These obligations are not optional alternatives selected by this Draft.

Accountable Security decisions remain unresolved for initialize, replace and withdraw:

- Which operations/contexts are privileged or high-impact.
- The applicable MFA and reauthentication requirements under those classifications.
- Whether any operation requires independent approval.
- Which same-actor action combinations are permitted.
- The decision point and effective handling of in-flight access withdrawal.

No blanket dual approval, universal same-actor permission, numerical session lifetime,
reauthentication window, emergency bypass or new Staff mapping is established. Business
approval of B1/C1 does not constitute Security approval of assurance or race controls.

## 10. Trusted Mutation Audit Prerequisites

DEC-0044 D1 records the Product/business choice for trusted mutation audit on all three
operations. This is proposed operational specialization pending remaining review, not an
implemented recording guarantee. Existing REQ-PRC-031, BPRC-REQ-037 and Security Standards
§27 remain binding. **Operational writes remain blocked until required audit recording
and durability semantics are approved and available.** No infrastructure is selected.

Required audit meaning includes, where applicable: trusted actor/system attribution and
source, server-controlled synchronized time, exact action and Product/Variant resource,
operation correlation, authorization context, previous/new Pricing revision or safe state
summary, actual outcome, safe reason and approving Principal where separately governed.
For initialization, the prior state is explicitly absent; never invent a prior revision.
Never accept caller-provided actor attribution as trusted merely because it is stored.

Distinguish requested intent, authorization denial, precondition rejection, failed mutation,
confirmed commit and uncertain outcome. Authorization denials and security-relevant
failures require applicable recording/telemetry. Never record a successful mutation before
its committed outcome is known or claim durable audit persistence without confirmation.
Pricing revision history alone proves neither trusted actor, time, authorization nor audit.

Audit evidence requires access control, tamper resistance/evidence, protection from the
actor being audited, monitoring of ingestion failures and governed retention. No retention
duration, storage product, transport or new transaction architecture is chosen. The future
technical audit Contract must establish durable recording and truthful outcome correlation,
write blocking when guarantees are unavailable, failure reporting and recovery handling.

If commit or audit outcome is uncertain, preserve uncertainty and reconcile through
trusted Pricing revision and audit observations before any retry. Do not fabricate failure
or success, blindly replay, delete history or assume an intent record proves commitment.
If audit is unavailable during denial/failure handling, prevent mutation and surface the
recording failure through the governed operational path; do not pretend the audit succeeded.
That reporting/reconciliation path is an unresolved technical prerequisite, not a selected
infrastructure implementation.

## 11. Fail-Closed Invariants and Separate Preconditions

| Condition | Required safe behavior |
| --- | --- |
| Missing/untrusted actor evidence | Never Authorized. Invalid/untrusted evidence yields the invalid alternative; inability to obtain trustworthy evidence yields unavailable. |
| Expired or withdrawn access | No write. Trustworthy policy refusal is Denied; unusable/inconsistent evidence is Invalid; inability to establish current authority is Unavailable. No stale allow fallback. |
| Unauthorized action | Denied after trustworthy policy evaluation; no implication of permission for another action. |
| Invalid or unconfirmed Product association | No mutation or validated commercial result. Preserve Product's absence, non-membership, unavailable and invalid meanings separately. |
| Lifecycle ineligibility | Precondition rejection, no mutation even if authorization was otherwise affirmative. |
| Reference/intent/correlation mismatch | No reliance on any allow decision; invalid evidence or precondition failure according to its source. |
| Stale Pricing revision or concurrent conflict | Explicit Pricing precondition/conflict outcome and rollback, not authorization denial or a silently successful overwrite. |
| Required audit unavailable | No mutation; distinct operational/audit failure, not proof that the actor lacks permission. |
| Uncertain mutation/audit result | No confirmed-success claim or blind replay; governed reconciliation required. |

Authorization decisions and operation preconditions are different result spaces. A failed
Product read, stale Pricing revision or audit outage must not be relabeled Denied unless
there was an actual trustworthy authorization refusal. Authorized never bypasses the
other checks. The exact orchestration result types remain for later Contract/API review;
this Draft does not collapse them into the four authorization outcomes.

## 12. Future Java Boundary — Illustrative Only

A future Pricing consumer port may reside under `pricing/application/port/out/`:

```java
interface BasePriceAuthorizationPort {
    AuthorizationDecision authorize(AuthorizationRequest request);
}
```

This is illustrative, not an approved Java API. The request is the conceptual structure
in §5; action-specific immutable intent types avoid invalid optional combinations.
Concrete trusted actor/access types must come from the approved Identity provenance
boundary when available. Do not create a placeholder trusted actor record or an allow
implementation to satisfy this illustration. Product evidence must be consumed through
its exported application Contract, not Product Domain models, tables or Repositories.

No interface, service, authorization decision engine, operational command, audit mechanism,
HTTP endpoint, migration or dependency is implemented by this document. Existing internal
Pricing persistence and conditional queries do not become authorized write entry points.

## 13. Verification Requirements for Future Implementation

These are future expectations, not claims of executed tests:

- Independent initialize/replace/withdraw scope, including no implied cross-action grant.
- All four outcomes, with zero mutation after non-authorization and no disclosure of internals.
- Trusted actor provenance, ordinary customer/anonymous rejection, forged claims, expired
  context, access withdrawal, unavailable authority and recognized wrapped availability failures.
- Product/Variant, actor, action, amount, expected revision and correlation substitution.
- All B1 lifecycle cases, independent Product membership and no publication/sellability inference.
- Per-operation Product and authority revalidation; known change handling and independent
  observation timing without a shared-instant claim.
- Expected-revision conflict, concurrent operations, atomic rollback and terminal-head history.
- Audit unavailable, recording failure, truthful denial/precondition/commit outcomes and
  uncertain-commit reconciliation without duplicate mutation or false durable-record claims.
- No internal entity/credential leakage, unauthorized public or Staff API exposure, fallback
  authorization, invented Role/Permission, or cross-domain persistence access.

## 14. Governance Dependencies and References

| Prerequisite | Current boundary |
| --- | --- |
| DEC-0044 accountable Security disposition | Pending: assurance, withdrawal, outcomes, race reliance and audit controls are not Security-approved. |
| Bounded DEC-0012 Staff mappings | Pending: A2 is a Product/business-approved resolution path, not concrete eligible assignments or acceptance of DEC-0012. |
| Trusted Identity actor/access Contract | Pending: provenance, verified context and effective withdrawal must be established without a fabricated actor DTO. |
| Administration coordination | Pending: governed assignment coordination must preserve Identity and owning-Pricing authority. |
| Pricing enforcement acceptance | Pending: approve the contextual enforcement and operation/precondition failure semantics. |
| Engineering/Architecture review | Pending: verify module boundaries, feasible assurance, independent observations and recording guarantees; no architecture adoption inferred. |
| Trusted mutation audit guarantees | Pending: durable truthful recording, failure handling and uncertain-outcome reconciliation must be approved and available. |

Approval of this Draft must follow the existing accountable Contract/governance process.
No approval or independent sign-off is claimed. Even future approval cannot substitute
for missing trusted evidence or audit capability. This Draft grants no operational write
authority and does not change any decision status or Approved specification.

- [Accepted DEC-0043](../../decisions/DEC-0043-initial-v1-base-price-assignment-policy.md).
- [DEC-0044 0.2.0 Proposed](../../decisions/DEC-0044-variant-base-price-operational-authorization.md).
- [DEC-0012 Proposed](../../decisions/DEC-0012-administrative-role-permission-matrix.md).
- [Pricing Domain](../../domains/pricing/pricing-domain.md): REQ-PRC-004–008, 025–026, 031.
- [Pricing Backend](../../backend/pricing/pricing-backend.md): BPRC-REQ-007, 009, 014, 017, 019, 033, 037.
- [Identity Backend](../../backend/identity/identity-backend.md): BIDN-REQ-004, 021, 024–026.
- [Administration Domain](../../domains/admin/admin-domain.md): REQ-ADM-005–010, 025.
- [Approved Product Variant evidence Contract](../product/internal-variant-evidence-contract.md): §§3–7.
- [Security Standards §§10–13, 27](../../../.ai/core/SECURITY-STANDARDS.md).
- [Architecture §§35.2, 35.5](../../../.ai/core/ARCHITECTURE.md).
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md).

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-04 | Draft | Defined the proposed internal base-price authorization intents, trusted-context boundary, four outcomes, Product/business lifecycle direction, independent revalidation, Pricing concurrency and audit prerequisites; preserved all pending governance and blocked operational writes. |
