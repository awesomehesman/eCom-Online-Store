---
title: Product-Owned Internal Variant Evidence Contract
version: 1.1.0
status: Draft
owner: Product
last_updated: 2026-11-04
authoritative: false
---

# Product-Owned Internal Variant Evidence Contract

**Working revision 1.1.0 — proposed Inventory consumer amendment, not approved.**
Sections 1–10 retain the Approved 1.0.0 baseline for Pricing consumption; its approval
record and history remain intact. Section 12 proposes a compatible consumer extension
and grants no Inventory consumption permission until the required approvals are recorded.
The Draft metadata describes this working revision, not withdrawal or supersession of
the Approved 1.0.0 baseline. `Draft` follows Documentation Standards §8; “proposed”
describes the amendment rather than introducing an unsupported Contract status.

## 1. Identity, Lifecycle and Scope

This Contract is identified by its canonical path,
`specifications/contracts/product/internal-variant-evidence-contract.md`, title and
version. No new sequential Contract identifier is introduced. Version 1.0.0 is
Approved following the accountable owner's explicit approval on 2026-11-04 in the
Product, Pricing and Engineering/Architecture capacities recorded in §8. It is
authoritative only for this internal evidence Contract's semantics, subordinate to
higher-authority governing sources; it is not a repository-wide Source of Truth.
Existing governing requirements remain binding.

Producer: **Product**. Initial consumer: **Pricing**. Purpose: a bounded, internal,
read-only observation of Product existence, Variant existence within the requested
Product, membership, stable references, lifecycle and representation revision.
This is an application-facing module Contract under Architecture §35.5, not an HTTP,
public catalogue or Staff administration Contract. No OpenAPI wire interface is added.
It introduces no write capability, new business policy or publication authority.

## 2. Ownership and Consumer Boundary

Product owns identity, Variant membership, Product lifecycle and the meaning of the
returned representation revision. Pricing consumes those facts without redefining
them or accessing Product Domain objects, Repositories, tables, persistence records,
Adapters or framework types. Public Contract types must be intentional exported
application-facing types; Java signatures and package design are not selected here.

Pricing remains authoritative for governed commercial calculation and its assignment
history. Pricing data, assignment rows and identifiers cannot establish Product truth.
Only Pricing is an initial consumer of this internal capability; this declaration
creates no public access or permission for another consumer. It grants no Staff or
customer access and selects no authentication or authorization mechanism. Existing
security requirements and domain boundaries continue to apply.

## 3. Input Semantics

Require exactly the two explicit lookup references:

- Product reference.
- Variant reference.

Both are required opaque, stable Product-owned references. Apply the existing Product
identity validation and equivalence rules without inventing a new format, generating
replacement identity, case-folding or deriving Product identity from the Variant.
Missing, malformed or otherwise invalid references yield Invalid or Inconsistent
Evidence, with invalid-input meaning, before any positive association can be returned.
Syntax validity proves neither existence nor ownership.

A request concerns one Product/Variant pair. It does not request global Variant search,
a Product aggregate, content, media, assignment history, Inventory or Pricing data.

## 4. Typed Outcome Semantics

The outcome names below define semantic alternatives, not Java type names, HTTP
statuses or JSON fields. A response represents exactly one alternative.

| Outcome | Required meaning | Permitted reliance |
| --- | --- | --- |
| Confirmed Association | One coherent Product-owned observation confirms the requested Product exists, the requested Variant exists within it, and membership is valid. Returned Product and Variant references exactly match the requested pair. Explicit observed Product lifecycle and its representation revision are required. | Identity and membership facts in that observation only; no commercial eligibility grant. |
| Product Absent | A successful, coherent Product-owned read confirms the requested Product is absent. Variant existence elsewhere is neither asserted nor disclosed. | Confirmed absence of that Product in the observation only. |
| Variant Absent or Not Associated | The Product exists in the observation, but the requested Variant is not a member of it. This includes a Variant absent from that Product; it does not distinguish global nonexistence from membership in another Product. | The requested association is not confirmed. No other owning Product or protected representation is disclosed. |
| Product Evidence Unavailable | Product cannot complete a trustworthy observation because its required authority or dependency is unavailable, times out or cannot supply the required coherent read. | No existence, absence or membership conclusion. Pricing must withhold positive reliance. |
| Invalid or Inconsistent Evidence | Input is invalid, or the observation is malformed, contradictory, corrupt, mismatched, incomplete or otherwise cannot satisfy this Contract. Invalid input must remain distinguishable from invalid producer evidence through a safe reason classification. Unexpected internal failures belong to this failure alternative with internal-failure meaning, never confirmed absence. | No positive association or commercial reliance. |

Unavailable authority and invalid/inconsistent evidence remain distinguishable. Failure
reasons must be safe and must not expose SQL, exceptions, credentials, private content
or storage details. This document does not select a numerical reason-code vocabulary.
Persistence or transaction failures must not become Product Absent or Variant Absent.
Only completed authoritative negative reads may produce those negative outcomes.

Confirmed Association contains only the requested Product reference, requested Variant
reference, observed Product lifecycle and Product representation revision. The lifecycle
uses the existing Draft, Published and Archived meanings. All three may be observed
internally; confirmation of identity in Draft or Archived is not a visibility grant.
No separate Variant lifecycle is invented. Product-owned Variant lifecycle consistency
remains required; contradictory membership or state cannot produce confirmation.

No entity, persistence mapping, raw failure, media URL, actor claim or internal evidence
history is included. The revision must be bound to the observed owning Product and
representation, not substituted from a different Product or a separate read.

## 5. Consistency and Failure Integrity

Input validation, Product existence, Variant membership, lifecycle and representation
revision must resolve from one coherent Product-owned read. Do not combine a Product
from one observation with Variant membership or revision from another. If coherence
cannot be established, return an unavailable or invalid/inconsistent outcome according
to the cause, never a partially confirmed response.

Product owns the read boundary and reconstruction validation. Existing application-owned
read-only REPEATABLE READ infrastructure is an available implementation mechanism;
this Contract requires coherent evidence without mandating an additional transaction
technology. An invalid reconstructed aggregate must not be downgraded to absence.

The evidence exchange does not create atomic consistency across Product and Pricing
transactions, databases or observations. Product does not expose its transaction handle
or persistence context. Pricing must not hold a Product database lock or directly read
Product tables through this Contract. No distributed lock, cross-module transaction,
reservation, lease or shared snapshot is introduced.

## 6. Freshness and Revalidation

Evidence describes a bounded observation, not continuing authority. A representation
revision is an opaque comparison token: equality permits comparison under Product's
revision semantics, not proof that the Product has remained unchanged since the read.
It is not a timestamp, sequence order, expiration, freshness duration or eligibility flag.
No TTL or numerical observation-age limit is selected.

Pricing must obtain a new Product-owned observation for each future authorized operation
that relies on identity, membership or relevant Product state, before deciding to rely
on those facts. A prior query result, cache or historical revision cannot substitute
for this revalidation. Material Product change, mismatched revision, uncertainty or an
intervening invalidation known before reliance requires renewed observation and governed
re-evaluation; unavailable or inconsistent revalidation cannot grant positive reliance.

This rule does not authorize the operation itself and does not remove the interval in
which Product can change after an observation. Any future operation requiring stronger
commit-time coordination must establish that guarantee separately before relying on it.
This Contract does not claim atomic Product/Pricing commitment or uninterrupted freshness.

## 7. Explicit Exclusions

This evidence does not establish:

- Product publication acceptance or positive customer visibility; Published alone is insufficient.
- Product structural sellability or final purchasability.
- Inventory availability, Stock or reservation.
- Customer purchase eligibility.
- Staff authorization, trusted actor attribution or administrative access.
- Pricing assignment authorization or proof of an authorized committed Pricing operation.
- Tax-inclusive display amounts, Tax, Discount, Promotion or Checkout totals.

A confirmed association is therefore insufficient to promote a conditional Pricing
observation into a commercially authoritative Price. REQ-PRC-005 and BPRC-REQ-017 still
require applicable publication, structural-sellability and current-input safeguards.
This narrow evidence Contract neither resolves nor bypasses those other dependencies.

## 8. Review, Compatibility and Approval Boundary

| Capacity | Recorded disposition | Approved scope |
| --- | --- | --- |
| Product, accountable producer owner | APPROVED — 2026-11-04 | Evidence meanings, lifecycle/revision scope, outcomes and disclosure boundary. |
| Pricing, initial consumer | APPROVED — 2026-11-04 | Consumer semantics, safe consumption, failure handling and revalidation without commercial-authority inference. |
| Engineering/Architecture, technical boundary | APPROVED — 2026-11-04 | Exported-type encapsulation, coherent read boundary, compatibility and failure integrity; no new architecture choice is introduced. |

The accountable owner's explicit M2.4B promotion instruction records approval on
2026-11-04 in all three capacities above. The same accountable owner acts in these
separate capacities; no personal name, signature, independent reviewer or independent
approval is claimed. The preceding committed-state governance and technical review
returned PASS — Approval-ready with no findings; it is distinct from these explicit
owner dispositions. No separate meeting or evidence artifact is invented.

Approval establishes the internal Contract baseline. Implementation must conform to
this Contract and existing governing requirements; no implementation completion,
operational access, Staff authority or commercial Pricing eligibility is established
by approval. No new ADR or DEC is created or required by this document's existence.

Preserve stable reference meaning and outcome distinctions across versions. A change
to evidence meaning, required data, consumer reliance or failure semantics requires
explicit compatibility review and a versioned revision before adoption. Approved 1.0.0
selects no Java interface, service, transport, schema, provider or deployment mechanism.
This promotion changes no Approved Product/Pricing Specification.

## 9. Acceptance Examples for Later Verification

- Existing Draft Product and its Variant: confirm identity, observed Draft and matching
  representation revision without claiming visibility or sellability.
- Existing Published or Archived Product and member Variant: preserve the actual observed
  lifecycle and matching revision; neither lifecycle alone authorizes commercial use.
- Missing Product: Product Absent only after a successful coherent negative read.
- Existing Product with no requested member: Variant Absent or Not Associated; no
  global Variant search or alternate Product disclosure.
- Reference substitution, conflicting state, missing revision or mixed observations:
  Invalid or Inconsistent Evidence, never confirmation.
- Database authority unavailable: Product Evidence Unavailable, never confirmed absence.
- Unexpected producer failure or corruption: invalid/inconsistent internal-failure outcome,
  with safe reason classification and no exception disclosure.
- Revision changes before a future operation: obtain a new observation and re-evaluate;
  failure cannot silently reuse previously confirmed association evidence.

These are Contract verification expectations, not results of executed tests.

## 10. Traceability and Governing References

| Source | Relevant meaning |
| --- | --- |
| REQ-PRD-003, 005 | Product-owned stable identity and explicit governed lifecycle. |
| REQ-PRD-007–010 | Exactly one owning Product, stable Variant identity, governed uniqueness and lifecycle consistency. This Contract confirms membership without exporting Attribute validation internals. |
| REQ-PRD-052 | Product Contract authority, lifecycle, authorization, compatibility, failure, stale-state and Sensitive Data boundaries. |
| BPRD-REQ-008–012 | Modular boundaries, minimal public module Contract, explicit Product-owned query, transaction safety and stable Product identity. |
| BPRD-REQ-014–016 | Variant identity/membership, concurrent consistency and owning-Product lifecycle constraints. |
| REQ-PRC-004–005 | Governed Product/Variant association and safe handling of unknown, stale, changed, unpublished or structurally unsellable input. |
| BPRC-REQ-007, 017 | Pricing consumes Product-owned evidence without acquiring Product authority; adverse evidence cannot silently produce trusted Pricing. |
| Accepted DEC-0019 1.0.0 | Stable Product/Variant identity, Product-owned membership and versioned Attribute meanings; Attribute validity alone is not publication. |
| Accepted DEC-0043 1.0.0 | Variant-level assignments reference their owning Product; references alone prove neither membership nor operation authority. |
| Architecture §35.5 | Small intentional exported application-facing module Contract; no internal entity, Repository or persistence exposure. |

- [Product Domain](../../domains/product/product-domain.md).
- [Product Backend](../../backend/product/product-backend.md).
- [Pricing Domain](../../domains/pricing/pricing-domain.md).
- [Pricing Backend](../../backend/pricing/pricing-backend.md).
- [Accepted DEC-0019](../../decisions/DEC-0019-product-variant-attribute-standards.md).
- [Accepted DEC-0043](../../decisions/DEC-0043-initial-v1-base-price-assignment-policy.md).
- [Architecture §35.5](../../../.ai/core/ARCHITECTURE.md).
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md).
- [Existing public catalogue Contract](initial-product-catalogue-contract.md): separate customer-facing scope; not the internal evidence API.

## 11. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-04 | Draft | Defined Product-owned internal identity/membership, observed lifecycle and revision evidence for Pricing, explicit typed outcomes, coherent observation and bounded freshness; left approval pending and granted no commercial, Staff or implementation authority. |
| 1.0.0 | 2026-11-04 | Approved | Recorded explicit accountable-owner approval in Product producer, Pricing consumer and Engineering/Architecture technical-boundary capacities; promoted the unchanged substantive evidence semantics to an authoritative internal Contract baseline without claiming independent approval, implementation completion or operational/commercial authority. |
| 1.1.0 | 2026-11-04 | Draft | Proposed Inventory as an additional internal association-evidence consumer in §12, preserving the Approved 1.0.0 Pricing baseline and unchanged provider outcomes; recorded pending Product, Inventory and Engineering/Architecture review without granting consumption or operational authority. |

## 12. Proposed Inventory Consumer Amendment — Approval Pending

### 12.1 Scope and Ownership — Proposed Additions to §§1–2

Propose Inventory as an additional internal consumer solely for Product existence,
Variant existence within the requested Product and Product-to-Variant association
observations, with the existing observed lifecycle and representation revision context.
Pricing's Approved 1.0.0 permissions, behavior and obligations remain unchanged.
The approved statement that only Pricing may consume the capability remains effective
until this extension is approved. No other consumer is proposed.

Product remains authoritative for Product identity, Variant identity, membership,
lifecycle and representation revisions. Inventory would consume only the Product-owned
exported application Contract under Architecture §35.5. It must not directly access
Product tables, internal Domain objects, Repositories, mappings or transaction state.

### 12.2 Inputs, Outcomes and Coherence — Proposed Application of §§3–5

Inventory would supply both explicit references using the unchanged §3 rules. The
existing provider outcomes remain unchanged: Confirmed Association, Product Absent,
Variant Absent or Not Associated, Product Evidence Unavailable, and Invalid or
Inconsistent Evidence. Inventory must preserve these distinctions. Unknown, unavailable,
invalid or inconsistent evidence cannot establish confirmed membership; failures cannot
be converted into confirmed absence or a successful empty observation.

Membership, lifecycle and representation revision must come from the same coherent
Product-owned read under §5. No new outcome, response field or Java API is proposed.
Product and Inventory observations do not establish a shared transaction, cross-domain
locking, a shared snapshot, continuous freshness or permanent membership guarantees.

### 12.3 Freshness and Reliance — Proposed Addition to §6

For each future governed Inventory operation relying on Product association or state,
Inventory must obtain a new Product-owned observation before deciding to rely on it.
Known material Product changes, mismatched revisions, uncertainty or intervening
invalidation require renewed observation and governed re-evaluation. Unavailable or
invalid revalidation must withhold positive reliance rather than reuse an earlier result.

The revision remains a comparison token, not proof of continuing freshness. No arbitrary
TTL is introduced. Product can change after observation; this extension grants neither
atomic Product/Inventory commitment nor stronger commit-time coordination. An operation
requiring a stronger guarantee must establish it separately before implementation.

### 12.4 Inventory Exclusions — Proposed Addition to §7

Product association evidence does not establish Stock existence, Stock Location, Stock
quantity, Available-to-Sell, reservation eligibility, adjustment authorization or Product
sellability. Observed lifecycle is context, not permission to initialize, adjust, reserve
or otherwise mutate Inventory. All existing §7 exclusions remain effective. No operational
Inventory permissions, quantity model, location model or availability formula is selected.

### 12.5 Required Dispositions — Proposed Addition to §8

| Capacity | Amendment disposition | Required review scope |
| --- | --- | --- |
| Product producer / Contract owner | Pending | Explicit Inventory consumer scope, unchanged Product evidence meaning and disclosure boundary. |
| Inventory consumer owner | Pending | Association-only reliance, failure distinctions, revalidation and Inventory non-authority exclusions. |
| Engineering/Architecture | Pending | Compatibility with Pricing, exported boundary, coherent reads and independent observation limitations. |

Existing §8 approvals apply to 1.0.0 only and do not approve this amendment. The amendment
remains proposed in this Draft revision until the required dispositions are recorded
and promotion follows the governed approval process. No completed review is asserted.

### 12.6 Proposed Acceptance Examples — Addition to §9

- Confirmed Product and member Variant: Inventory may rely on the matching identity and
  membership in that observation after extension approval, without inferring Stock.
- Product evidence unavailable: preserve Unavailable; do not infer membership, absence,
  zero Stock or availability, and withhold any operation requiring confirmed association.
- Draft, Published or Archived Product with a member Variant: preserve observed lifecycle
  and revision; none of these observations establishes Inventory operational eligibility.

Later verification must retain Pricing regression coverage and verify Inventory reference
matching, unchanged outcome meanings, unavailable handling and lack of operational grants.
These examples are proposed expectations, not executed tests or permission to implement.

### 12.7 Proposed Inventory Traceability — Addition to §10

| Source | Proposed extension traceability |
| --- | --- |
| REQ-INV-004–005 | Correct governed Variant association; Product publication and Inventory availability remain separate authorities. |
| BINV-REQ-007 | Inventory consumes governed Product evidence without redefining Product identity, lifecycle or sellability. |
| BINV-REQ-014 | Unknown, invalid, ambiguous or mismatched associations cannot establish valid Inventory reliance. |
| Architecture §35.5 | Intentional Product-owned exported application Contract; no internal entity or persistence access. |

- [Inventory Domain](../../domains/inventory/inventory-domain.md), §6.
- [Inventory Backend](../../backend/inventory/inventory-backend.md), §§2 and 4.

Existing Pricing traceability in §10 is unchanged. This proposal introduces no adapter,
Stock aggregate, location, quantity, reservation, adjustment, HTTP or migration capability.
