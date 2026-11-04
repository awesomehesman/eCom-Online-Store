# DEC-0045 — Initial Stock Representation and Initialization Policy

- **Identifier:** DEC-0045
- **Title:** Initial Stock Representation and Initialization Policy
- **Type:** Inventory Business / Technical Decision
- **Status:** Proposed
- **Version:** 0.3.0
- **Date:** 2026-11-04
- **Last Updated:** 2026-11-05
- **Owner:** Inventory
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

The canonical Glossary §10 defines Stock as the recorded on-hand quantity of a
Product Variant at a Stock Location. Approved Inventory requirements establish
association integrity, Inventory-owned state, safe unknown/negative outcomes,
concurrency protection and authorized, traceable quantity changes. They do not select
quantity units, a concrete location model or an initialization workflow.

Accepted ADR-0005 establishes the Inventory backend specification boundary, not this
Stock policy. The Approved Product-owned internal Variant evidence Contract 1.1.0
permits Inventory to observe Product/Variant membership, lifecycle and representation
revision. Such evidence proves neither Stock existence nor quantity, location,
availability or mutation authority.

The current Inventory foundation contains an immutable Product/Variant reference and
an association-observation integration. Neither establishes a Stock aggregate or a
lawful Stock initialization source. This proposal identifies the smallest initial
representation choices for accountable review before those capabilities are implemented.
Affected owners include Inventory business and engineering, Product, Architecture,
Security and Administration; downstream commerce consumers retain their own authority.

## Decision Drivers

- Preserve Stock's Variant-and-location meaning without inventing physical topology.
- Distinguish unknown Stock from an explicit recorded zero.
- Make whole-unit business semantics independent of language and storage choices.
- Prevent duplicate initialization, lost updates and unsupported negative quantities.
- Preserve Product identity authority and Inventory quantity/history authority.
- Keep initialization policy separate from operational authorization and availability.

## Proposed Decision — Awaiting Accountable Approval

The Inventory business owner approved the business dispositions IBD-01 through IBD-08
on 2026-11-05 as recorded below. The Inventory technical owner confirmed ITD-01 through
ITD-08 on the same date. Architecture disposition and applicable affected-owner reviews
remain pending. The overall decision remains Proposed, non-authoritative and not
approved for implementation.

### 1. Stock Identity and Association

Propose at most one initial Stock record for each governed Product Variant and governed
logical Stock Location combination. This is a logical Stock uniqueness invariant and
does not select a database uniqueness constraint, key structure or concurrency mechanism.
Each record must retain the correct Product/Variant association. Product owns Product
and Variant identity and membership; Inventory owns Stock identity and state. This
selects neither identifier encoding nor a database key.

Before authoritative Stock is initialized, the Inventory Resource association must be
governed and verified through the Product-owned Contract. Invalid, absent, mismatched,
unavailable or inconsistent evidence must not be promoted into confirmed association.
Observation remains bounded and subject to the Contract's revalidation rules; no atomic
Product/Inventory snapshot or permanent membership guarantee is introduced.

### 2. Initial Logical Stock Location

Propose one governed logical Stock Location for initial operations. It does not establish
a physical warehouse, warehouse address, bin hierarchy, multiple-location transfers or
fulfilment topology. Location identity, registration and lifecycle must be explicitly
governed before operational use. No implicit location, registration mechanism or automatic
location creation is approved here.

### 3. Quantity Semantics and Technical Representation

**Business recommendation:** recorded on-hand quantities are non-negative whole units;
fractional garment quantities are unsupported initially. This is a proposed restriction,
not an inference that Approved requirements already mandate integer quantities.

**Technical direction confirmed by the Inventory technical owner:** use Java `long`
for recorded on-hand Stock quantities (ITD-01). Architecture disposition remains pending;
this confirmation does not select a database
column type or storage mechanism. Values outside the representable non-negative range
must fail safely; future arithmetic must not wrap, truncate, round or silently coerce
an unsupported value. No additional business cap is selected by this technical limit.

Stock remains recorded on-hand quantity, not reserved quantity or Available-to-Sell.
This proposal defines no reservation subtraction, safety-stock deduction or availability
formula, and a recorded zero must not be presented as a conclusion about an unknown record.

### 4. Explicit Inventory-Owned Initialization

Propose an explicit initialization operation owned by Inventory, with a governed Resource
association and explicitly supplied initial quantity. Product creation, membership,
publication, an observation request or a missing Stock record must not initialize Stock
or silently convert unknown Stock into zero.

Initialization must satisfy applicable contextual Inventory authorization, consistency
and audit requirements. The concrete initialization authority and actor eligibility
remain unresolved and require separate governance before implementation. An explicit
zero is a possible proposed initial quantity, not a default. No eligible actor, Staff
mapping, system authority, API, payload or transaction mechanism is selected.
Initialization changes Stock and is not an authorization bypass around governed Stock
Adjustment controls.

Duplicate initialization must not create duplicate Stock or overwrite accepted state.
Failed or uncertain initialization must not fabricate an accepted record; uncertainty
must remain distinguishable and be reconciled before repeating a potentially accepted
effect. The exact retry/idempotency Contract remains a later bounded design dependency.

### 5. Negative Stock, Concurrency and History

Propose prohibiting negative recorded on-hand Stock initially. This does not resolve
back-order or pre-order promises: DEC-0024 remains Proposed and non-authoritative.

Required outcomes are no silent lost updates, no duplicate accepted initialization,
no unsupported negative quantity, and distinguishable accepted, rejected, conflicting
and uncertain effects. Accepted quantity-changing operations must retain appropriate
source/reason, effect, outcome and historical/audit traceability under existing requirements.
Prior Inventory and confirmed external commercial history must not be rewritten.

This record selects no optimistic or pessimistic locking, event sourcing, transaction
isolation level, retry mechanism or retention period.

## Alternatives Considered

| Alternative | Trade-off and proposed disposition |
| --- | --- |
| Variant-only Stock | Simpler but omits the canonical location dimension; not recommended. |
| One logical location with explicit Variant association | Recommended bounded start; concrete location governance remains necessary. |
| Multiple locations, transfers and physical warehouse modeling | Greater operational breadth with ungoverned topology/workflow choices; defer. |
| Fractional quantities | Supports measured goods but requires units and precision policy; not recommended initially. |
| Whole units represented by Java `long` | Recommended technical starting point; finite range and overflow require explicit validation. |
| Whole units with arbitrary-precision representation | Avoids fixed-width range limits but adds representation complexity; technical alternative for review. |
| Automatic zero initialization | Convenient but conflates absence with accepted Stock and creates an implicit mutation path; not recommended. |
| Explicit governed initialization | Recommended; requires trusted authorization, provenance, consistency and uncertainty handling. |
| Negative on-hand Stock | Introduces additional operational policy; not recommended and does not follow from back-order support. |

These alternatives preserve the proposal rationale. The business and technical-owner
dispositions recorded below do not assert completed comparative evaluation, Architecture
approval or overall decision acceptance.

## Authority Boundaries

Inventory owns Stock and its governed state changes. Product owns Product/Variant identity,
membership, lifecycle and representation revision. Product evidence supplies association
facts only. Pricing owns commercial amounts; Order owns confirmed commercial history;
Shipping owns fulfilment outcomes. None acquires another Domain's authority here.

Identity supplies trusted identity/access evidence, Security governs applicable controls,
and Administration coordinates governed operations without creating a second access model.
This Inventory-owned record cannot independently approve Product business scope or a
material Architecture change; affected authority must be represented before acceptance.

## Consequences

If accepted, the proposal provides a bounded basis for later Stock modeling and validation.
It retains the location dimension and makes unknown-versus-zero explicit. Costs include
location registration governance, finite-range validation, immutable historical meaning
and a separately authorized initialization boundary. It does not make operational writes
executable merely by accepting representation choices.

## Risks and Mitigations

| Risk | Required mitigation / remaining gate |
| --- | --- |
| Logical location is mistaken for a warehouse | Preserve logical meaning; separately govern identity, registration and lifecycle. |
| Stock is mistaken for Available-to-Sell | Keep raw on-hand and commitment eligibility separate; define no formula here. |
| Identifier syntax is mistaken for membership | Use current Product-owned evidence and fail closed on adverse outcomes. |
| Duplicate or racing initialization overwrites Stock | Require uniqueness and consistent accepted effects; select mechanisms later. |
| Quantity overflow silently changes meaning | Reject unsupported values and unsafe arithmetic; verify boundaries before implementation. |
| Initialization bypasses authorization or history | Require existing contextual controls and audit; no operational authority is supplied here. |

## Explicit Exclusions

Stock Reservations, reservation duration/timing, Available-to-Sell formulas, back-orders,
pre-orders, Safety Stock, reorder thresholds, replenishment, warehouse topology, Stock
transfers and receiving workflows remain unresolved and outside this proposal.

Stock Adjustment authorization, administrative permissions, database schema/migrations,
API and event Contracts are not selected. No Java implementation, provider, lock strategy,
transaction mechanism, deployment, production seed data or operational rollout is authorized.

## Open Decisions

- Architecture disposition and applicable affected-owner reviews remain pending; the
  business and technical-owner dispositions below do not satisfy those separate gates.
- Governed logical location identity, registration and lifecycle before operational use.
- Authorized actor/system provenance, Resource/action scope and audit guarantees for
  initialization; no Staff mapping or trusted actor source is assumed.
- Concrete initialization failure, retry and reconciliation Contract and persistence design.
- Separate reservation, availability, replenishment and commercial-policy decisions where
  future capabilities require them; DEC-0015 and DEC-0024 remain Proposed.

## Inventory Business-Owner Disposition — 2026-11-05

The accountable Inventory business owner explicitly confirmed acceptance of IBD-01 through
IBD-08 on 2026-11-05. This records approval in the Inventory business-owner capacity only,
not overall DEC-0045 acceptance or completed technical or Architecture review. No personal
name, signature, independent approval or additional reviewer is asserted.

| ID | Approved business disposition |
| --- | --- |
| IBD-01 | One logical Stock record per governed Product Variant and logical Stock Location combination. |
| IBD-02 | One governed logical Stock Location initially; physical warehouse topology deferred. |
| IBD-03 | Non-negative whole-unit recorded on-hand quantities; fractional quantities unsupported initially. |
| IBD-04 | Explicit Inventory-owned initialization with a supplied initial quantity. |
| IBD-05 | Missing Stock remains unknown; no automatic zero initialization. |
| IBD-06 | Negative recorded on-hand Stock prohibited initially. |
| IBD-07 | Duplicate initialization must not overwrite accepted Stock state. |
| IBD-08 | Reservations, Available-to-Sell, replenishment, transfers and warehouse operations remain outside this decision. |

Business approval alone did not select Java `long`; the separate technical-owner
confirmation is recorded below, with Architecture disposition still pending. Database
schemas, keys and uniqueness mechanisms; location registration and lifecycle mechanisms;
initialization actor eligibility and Staff permissions; authorization Contracts; retry,
concurrency and reconciliation mechanisms; and reservation or availability formulas remain
unselected. Business approval establishes no operational initialization authority and does
not remove any existing exclusion or affected-owner review requirement.

## Inventory Technical-Owner Disposition — 2026-11-05

The accountable Inventory technical owner explicitly confirmed ITD-01 through ITD-08
on 2026-11-05 in the Inventory technical-owner capacity only. This is a recorded
technical direction, not independent technical verification, Architecture approval,
overall DEC-0045 acceptance or implementation authorization. The existing IBD-01 through
IBD-08 business-owner approval remains unchanged.

| ID | Approved technical direction |
| --- | --- |
| ITD-01 | Java `long` for recorded on-hand Stock quantities; database representation remains unselected. |
| ITD-02 | Reject negative quantities, arithmetic overflow, truncation and unsupported conversions. |
| ITD-03 | Enforce one Stock record per governed Variant/location combination; concrete persistence constraints remain unselected. |
| ITD-04 | Prevent concurrent initialization from creating duplicate Stock records or overwriting accepted state. |
| ITD-05 | Prevent lost updates and inconsistent quantities during concurrent Stock adjustments; mechanism deferred. |
| ITD-06 | Prevent retries and failures from silently duplicating mutations or corrupting accepted Stock state; concrete mechanisms deferred. |
| ITD-07 | Preserve the distinction between missing Stock and recorded zero. |
| ITD-08 | Require governed Product-owned Variant association evidence before initialization, without transferring Product authority to Inventory. |

These directions establish no database schema, key, constraint or concurrency mechanism,
API or authorization Contract, eligible actor, Staff permission or operational capability.
Architecture and applicable affected-owner reviews remain pending. DEC-0015 and DEC-0024
remain Proposed. No additional reviewer, signature, independent approval or executed
verification evidence is asserted.

## Acceptance Conditions

| Accountable capacity | Disposition | Required determination |
| --- | --- | --- |
| Inventory business owner | Approved — 2026-11-05 (Inventory business-owner capacity only) | Whole-unit non-negative on-hand semantics, one logical location, explicit initialization and operational scope. |
| Inventory technical owner | Approved — 2026-11-05 (Inventory technical-owner capacity only) | Java `long` suitability, range/overflow safety, implementable invariants and deferred technical mechanisms. |
| Architecture owner | Pending — accept / reject / request changes | Domain boundaries, Variant/location association, Product evidence consumption and whether separate ADR governance is triggered. |

Inventory business and technical-owner dispositions above are recorded in their respective
capacities only. Architecture disposition remains pending. Product business authority
must review quantity/assortment and
location-scope effects where this specializes canonical Product policy. Security and Identity
review is required where initialization affects trusted actor/access, revocation or audit
controls; Administration reviews affected coordination. Shipping/Fulfilment and Operations
must review any later location/receiving implications before those scopes are introduced.
Additional approval follows actual impact under DECISIONS.md §17, not invented signatures
or independent reviews.

Before acceptance, required capacities must record attributable dispositions, reconcile
any higher-authority conflict, complete required index synchronization and identify affected
Specification/Contract synchronization. No personal reviewer identity, meeting, signature
or implementation evidence is asserted. Implementation and operational readiness remain
separate gates after acceptance.

## Security Impact

Existing Inventory contextual authorization, default denial, Resource isolation and
trusted audit obligations remain binding (REQ-INV-017–018, 028, 033; BINV-REQ-008,
025–026). Product association or syntactic validity grants no mutation permission.
No Role, Permission, Staff assignment or new trust issuer is created. A system-origin
initialization requires governed provenance just as human-origin work requires it.

## Data Impact

Proposed Stock meaning binds one governed Variant and logical location to explicit
on-hand quantity. Inventory owns that state and its history. No schema, database type,
identifier encoding, retention period or persistence implementation is selected. Historical
records must retain their accepted meaning independently of later model evolution.

## Compatibility and Migration Impact

No existing Stock data or implementation is migrated by this proposal. Existing Product
and Pricing meanings and the Product evidence Contract remain unchanged. Later adoption
must review historical compatibility and migration needs rather than silently recast
locations, fractional quantities, absent records or negative values. No backfill is authorized.

## Operational Impact

No initialization, adjustment or other Stock operation becomes available. Later operational
adoption requires governed locations, lawful initialization sources, current authorization,
audit and consistent effects with safe recovery. No warehouse, fulfilment, receiving,
support or production-launch commitment is made.

## Related Documents and Traceability

- [Decision governance](../../.ai/core/DECISIONS.md), §§9–11, 17–19: identity, structure, approval and effectiveness.
- [Glossary](../../.ai/core/GLOSSARY.md), §10: Stock, Stock Location, reservation and Available-to-Sell meanings.
- [Inventory Domain](../domains/inventory/inventory-domain.md), REQ-INV-004–008, 016–019, 028, 033: association, on-hand authority, safety, concurrency, authorization and history.
- [Inventory Backend](../backend/inventory/inventory-backend.md), BINV-REQ-007–009, 012–017, 024–027: evidence consumption, owned consistency, persistence and governed effects.
- [Accepted ADR-0005](../adr/ADR-0005-post-product-backend-specification.md): Inventory backend boundary, not Stock business-policy selection.
- [Approved Product evidence Contract 1.1.0](../contracts/product/internal-variant-evidence-contract.md), §§3–7 and 12: association observations, revalidation and no Inventory operational grant.
- [Accepted DEC-0019](DEC-0019-product-variant-attribute-standards.md): Product/Variant identity remains Product-owned.
- [Proposed DEC-0015](DEC-0015-stock-reservation-duration-policy.md): unresolved reservation duration and related boundaries.
- [Proposed DEC-0024](DEC-0024-back-order-pre-order-support-policy.md): unresolved back-order/pre-order commercial policy.

## Supersession

Supersedes: N/A. Superseded By: N/A. No Approved requirement, Accepted decision or existing
Contract is superseded or modified. Future supersession must preserve this proposal's history.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.3.0 | 2026-11-05 | Proposed | Recorded confirmed Inventory technical-owner directions ITD-01 through ITD-08; preserved IBD-01 through IBD-08 business approval, pending Architecture and affected-owner reviews, unselected mechanisms and all implementation and operational authorization gates. |
| 0.2.0 | 2026-11-05 | Proposed | Recorded explicit Inventory business-owner approval of IBD-01 through IBD-08; preserved pending Inventory technical and Architecture dispositions, affected-owner reviews and all remaining implementation and operational governance gates. |
| 0.1.0 | 2026-11-04 | Proposed | Proposed Variant/location Stock association, one governed logical location, whole-unit non-negative quantities, separately reviewed Java `long` representation and explicit Inventory initialization; retained pending accountable dispositions and all operational, policy, Contract and implementation gates. |
