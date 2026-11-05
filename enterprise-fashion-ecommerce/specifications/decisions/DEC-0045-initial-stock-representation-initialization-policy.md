# DEC-0045 — Initial Stock Representation and Initialization Policy

- **Identifier:** DEC-0045
- **Title:** Initial Stock Representation and Initialization Policy
- **Type:** Inventory Business / Technical Decision
- **Status:** Proposed
- **Version:** 0.8.0
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

The Inventory business owner approved IBD-01 through IBD-08 on
2026-11-05, and the Inventory technical owner approved ITD-01
through ITD-08 on the same date. The Architecture owner
completed ACCEPT WITH CONDITIONS subject to AOC-01 through
AOC-06. The Product affected owner subsequently completed
ACCEPT WITH CONDITIONS subject to POC-01 through POC-06.

The bounded Security governance assessment SAD-01 through
SAD-06 is complete with PASS WITH CONDITIONS; it is not
accountable Security-owner approval. Security-owner, Identity
and Administration affected-owner dispositions and overall
DEC-0045 acceptance remain pending.

DEC-0045 remains Proposed, non-authoritative and not approved
for implementation or operational initialization.

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
for recorded on-hand Stock quantities (ITD-01). Architecture-owner acceptance is subject to AOC-01 through AOC-06;
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

- Accountable Security-owner, Identity and Administration
  affected-owner dispositions and overall DEC-0045 acceptance
  remain pending. The completed bounded Security assessment
  does not constitute Security-owner approval.
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
confirmation and conditional Architecture-owner acceptance are recorded below. Database
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
Conditional Architecture-owner acceptance is recorded separately below; applicable
affected-owner reviews remain pending. DEC-0015 and DEC-0024
remain Proposed. No additional reviewer, signature, independent approval or executed
verification evidence is asserted.

## Architecture Assessment — 2026-11-05

**PASS WITH CONDITIONS — technical/governance assessment only.** The completed bounded
assessment records the findings below. It is not an Architecture-owner approval or
independent review. No reviewer name, signature, approval or executed verification
evidence is asserted.

| ID | Assessment | Finding |
| --- | --- | --- |
| ARD-01 | Inventory ownership of Stock | PASS — Inventory owns Stock state; Product retains Product/Variant authority. |
| ARD-02 | Product Variant association | PASS — Product-owned evidence supports association without granting Stock mutation authority. |
| ARD-03 | Initial logical Stock Location | CONDITIONAL PASS — One logical location is compatible with current architecture; identity, registration and lifecycle remain unresolved. |
| ARD-04 | Consistency and concurrency | PASS — Existing architecture requires safe concurrent mutations and preservation of accepted state; implementation mechanisms remain separately governed. |
| ARD-05 | Separate ADR requirement | No additional ADR currently required for the bounded representation policy; reassess if later design introduces a new architectural choice. |

The following conditions remain unresolved:

- Logical Stock Location identity, registration and lifecycle.
- Initialization authorization, eligible actors and audit provenance.
- Persistence, uniqueness, concurrency, retry and reconciliation mechanisms.
- Product affected-owner review was subsequently completed on
  2026-11-05 with ACCEPT WITH CONDITIONS (POC-01–POC-06);
  Security, Identity and Administration reviews remain pending.
- Any stronger Product/Inventory consistency guarantee required by future operations.

The existing IBD-01 through IBD-08 business approval and ITD-01 through ITD-08 technical
approval remain unchanged. The accountable Architecture-owner ACCEPT WITH CONDITIONS
disposition is recorded separately below.
This assessment grants no Stock implementation, initialization, API, schema, operational
permission or deployment authority. Overall DEC-0045 remains Proposed and non-authoritative.

## Architecture-Owner Disposition — 2026-11-05

**ACCEPT WITH CONDITIONS — ARD-01 through ARD-05.**

The accountable Architecture owner explicitly accepted the bounded Architecture direction
on 2026-11-05 in the Architecture-owner capacity only. The user's explicit “Approved”
confirmation records this accountable approval, not merely another technical assessment.
It is distinct from the completed assessment above and does not assert an independent
review, personal name, signature, meeting or executed verification evidence.

| Condition | Binding limitation |
| --- | --- |
| AOC-01 — Logical Stock Location governance | Logical Stock Location identity, registration and lifecycle must be governed before operational initialization. |
| AOC-02 — Domain authority | Product retains Product/Variant identity, membership and lifecycle authority. Inventory retains Stock identity, quantity and history authority. Product-owned association evidence must not grant Stock mutation authority. |
| AOC-03 — Initialization authorization | Initialization actor eligibility, trusted identity/system provenance, contextual authorization and audit requirements remain subject to separate governance. |
| AOC-04 — Technical mechanisms | Persistence, uniqueness, concurrency, retry and reconciliation mechanisms must be selected within the Approved Architecture baseline and applicable Specifications and Contracts. |
| AOC-05 — Affected-owner reviews | Applicable Product, Security, Identity and Administration reviews remain required before overall decision acceptance. |
| AOC-06 — Future Architecture changes | A new ADR is required if subsequent design introduces a material Architecture Decision not already covered by the current Approved Architecture baseline. |

AOC-01 through AOC-06 are binding limitations on this disposition, not evidence that
unresolved mechanisms have been selected. No location identifiers or lifecycle mechanisms,
initialization actors or Staff permissions, database schemas/columns/uniqueness constraints,
locking or transaction strategies, retry/idempotency/reconciliation mechanisms, APIs,
events, operational Contracts, deployment or production initialization procedures are
selected. DEC-0015 and DEC-0024 remain Proposed. Product affected-owner ACCEPT WITH CONDITIONS was
subsequently completed on 2026-11-05; Security,
Identity and Administration reviews and overall
DEC-0045 acceptance remain pending; no implementation or operational authorization
is granted. Existing business and technical-owner approvals remain unchanged.

## Product Governance Assessment — 2026-11-05

**PASS WITH CONDITIONS — bounded Product governance evidence assessment only.**
This assessment establishes compatibility findings. It is not Product-owner approval
or an independent review, does not accept DEC-0045 overall and does not authorize
implementation. It does not constitute executed verification or assert additional
reviewers, signatures or evidence artifacts.

| ID | Assessment | Finding |
| --- | --- | --- |
| PRD-01 | Variant identity — PASS | Product retains stable Product/Variant identity and membership authority. Inventory must not create competing Variant identities or derive replacement identities from mutable attributes. |
| PRD-02 | Variant membership — PASS | Product-owned association evidence establishes observed, coherent membership only. It does not create Stock or grant Inventory Product mutation authority. |
| PRD-03 | Variant lifecycle — PASS for authority preservation | Product retains Draft, Published and Archived lifecycle authority. No lifecycle-specific Stock initialization eligibility is selected. |
| PRD-04 | Whole-unit quantities — CONDITIONAL PASS | Non-negative whole-unit Stock is consistent with the bounded Inventory proposal, but Product must confirm applicability across the intended initial assortment, including Clothing, Footwear and Accessories. Clothing corresponds here to the governed Apparel family; no new Product family is introduced. |
| PRD-05 | Logical location — CONDITIONAL PASS | One governed logical Stock Location must not redefine Product assortment, Category structure, publication eligibility or fulfilment geography. Location identity, registration and lifecycle remain unresolved. |
| PRD-06 | Cross-domain consistency — PASS | Product evidence supports bounded observations and revalidation. It does not establish continuous freshness, shared transactions, permanent membership, availability, sellability or Stock mutation authority. |

### Approved Evidence and Traceability

| Source | Relevant assessment evidence |
| --- | --- |
| [PRODUCT.md](../../.ai/core/PRODUCT.md), §§16.2, 16.13 | Inventory authority and Product-owned Variant standards; Attribute families do not select launch assortment or Stock quantity units. |
| [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md), §§9, 35.5 | Domain ownership and intentional exported Module Contracts without internal persistence access. |
| [Product Domain](../domains/product/product-domain.md), REQ-PRD-001–003, 005, 007–008, 010 | Product/Variant identity, membership and lifecycle authority, with downstream authority separation. |
| [Inventory Domain](../domains/inventory/inventory-domain.md), REQ-INV-003–008 | Association integrity, Stock authority and separation of Product publication, raw Stock and Available-to-Sell. |
| [Product Backend](../backend/product/product-backend.md), BPRD-REQ-008–016 | Governed exported evidence, coherent Product-owned orchestration and stable identity/membership/lifecycle boundaries. |
| [Inventory Backend](../backend/inventory/inventory-backend.md), BINV-REQ-015–017 | Inventory-owned Stock, safe unknown/stale/negative outcomes and separate Available-to-Sell authority. |
| [Approved Product internal Variant evidence Contract 1.1.0](../contracts/product/internal-variant-evidence-contract.md), §§3–7, 12 | Bounded association, explicit failures, lifecycle/revision context, revalidation and no operational authority or cross-domain atomicity. |
| [Accepted DEC-0019](DEC-0019-product-variant-attribute-standards.md) | Stable Product/Variant identity, Product-owned applicability and versioned Attribute meaning. |

[DEC-0042](DEC-0042-product-publication-acceptance-and-visibility.md) remains Proposed
and must not be used as Accepted authority. Existing Approved evidence supports the
compatibility findings; that evidence alone is not Product-owner approval. The separate
accountable disposition below confirms whole-unit applicability subject to POC-01 through
POC-06.

### Product Conditions Addressed by the Accountable Disposition

The separate completed Product-owner disposition below addresses:

- Product identity, membership and lifecycle authority.
- Whole-unit applicability to the intended assortment.
- Logical-location neutrality toward assortment and publication.
- Bounded association evidence and consistency limitations.

Security, Identity and Administration reviews remain pending. DEC-0015, DEC-0024 and
DEC-0042 remain Proposed. No Stock implementation, initialization, API, schema, operational
permission or deployment is authorized. No location identifiers or registration mechanisms,
initialization actors or Staff mappings, database/concurrency mechanisms, retry or
reconciliation Contracts, availability/reservation policies or Product lifecycle-specific
initialization eligibility rules are selected. Existing Inventory and conditional
Architecture-owner approvals remain unchanged; overall decision acceptance is pending.

## Product-Owner Disposition — 2026-11-05

**ACCEPT WITH CONDITIONS — POC-01 through POC-06.**

The accountable Product affected owner explicitly approved POC-01 through POC-06 on
2026-11-05. The user's explicit “Approved” confirmation establishes this disposition
in the Product affected-owner capacity only. This is an accountable approval, not another
assessment. No independent review, personal name, signature, meeting or executed
verification evidence is asserted.

| Condition | Binding limitation |
| --- | --- |
| POC-01 — Product authority | Product retains exclusive authority over Product/Variant identity, membership, lifecycle and representation revision. Inventory must not establish competing Product identities, membership rules or lifecycle authority. |
| POC-02 — Whole-unit applicability | Non-negative whole-unit recorded on-hand Stock quantities are acceptable for the initial Apparel, Footwear and Accessories assortment. Fractional Stock quantities remain unsupported initially. This does not select Product assortment, Category taxonomy, SKU policy or future Product-family expansion. |
| POC-03 — Logical-location neutrality | One governed logical Stock Location must not determine Product assortment, Category classification, publication eligibility or fulfilment geography. Location identity, registration and lifecycle remain separately governed. |
| POC-04 — Product association evidence | Product-owned Variant association evidence establishes bounded membership observations only. It does not initialize Stock, grant Stock mutation authority or transfer Product ownership to Inventory. |
| POC-05 — Lifecycle eligibility | Product lifecycle states do not independently authorize or prohibit Stock initialization. Any lifecycle-specific initialization eligibility rule requires separate governance. No Draft/Published/Archived eligibility matrix is established. |
| POC-06 — Consistency limitations | Product evidence does not guarantee continuous freshness, permanent membership or atomic Product/Inventory consistency. Stronger coordination requirements must be governed separately. |

The Product governance assessment and accountable Product-owner disposition are complete
and distinct. Security, Identity and Administration affected-owner reviews and overall
DEC-0045 acceptance remain pending. Existing Inventory and Architecture-owner dispositions
are unchanged. All unresolved location, authorization, audit, persistence, uniqueness,
concurrency, retry and reconciliation dependencies remain open.

This approval neither accepts DEC-0045 overall nor authorizes Stock implementation,
operational initialization or deployment. It establishes no initialization actors or
Staff permissions, API or database schemas, persistence or concurrency mechanisms,
reservation or availability policies. DEC-0045 remains Proposed and non-authoritative;
DEC-0015, DEC-0024 and DEC-0042 remain Proposed.

## Security Governance Assessment — 2026-11-05

**PASS WITH CONDITIONS — bounded Security governance compatibility assessment only.**
The proposal preserves the Approved security boundaries and explicitly withholds operations
whose authority and controls remain unresolved. That supports conditional compatibility,
not operational readiness. The findings below are evidence analysis, not accountable
Security concurrence, Security-owner approval, independent review or executed verification.
No binding Security-owner conditions are introduced. The accountable Security-owner
disposition remains Pending.

| ID | Finding | Approved evidence and established requirement | Remaining governance dependency |
| --- | --- | --- | --- |
| SAD-01 — Stock ownership and mutation authority | PASS | PRODUCT.md §16.2; Architecture §9; REQ-INV-003–006, 027–028; BINV-REQ-008, 014–015; Product evidence Contract §§7, 12.4: Inventory owns Stock, Product retains identity/membership authority, and association evidence or identifiers grant no Stock mutation permission. DEC-0045 preserves these boundaries. | Maintain the separation in later governed operations; no mutation authority is supplied by this assessment. |
| SAD-02 — Initialization authorization | CONDITIONAL PASS | Security Standards §12; REQ-INV-017–018, 028; BINV-REQ-008, 025–026 require server-side default denial and contextual authorization for the current Principal, exact Resource, action, association and state. Initialization cannot bypass those requirements as an alternative label for changing Stock. | Eligible human/system actors, action scope, current authorization evidence, applicable assurance and operational authorization Contract remain unresolved. Accountable Security review is required; no Staff mapping, Permission or lifecycle eligibility matrix is selected. |
| SAD-03 — Trusted identity and provenance | CONDITIONAL PASS | Security Standards §§10–13; BIDN-REQ-004, 024–027; REQ-ADM-005–009 distinguish authenticated Principal, Staff association, current assignments and business authorization; Identity owns trusted human/system evidence and effective withdrawal. Applicable privileged-session/MFA/reauthentication requirements remain mandatory. | Identity provenance and current-access integration, eligible system context and action-specific assurance must be governed. No issuer, Role, Staff assignment, authentication mechanism or evidence producer is selected or claimed operational. |
| SAD-04 — Audit and traceability | CONDITIONAL PASS | Security Standards §27; PRODUCT.md §16.2; REQ-INV-018, 030, 033; BINV-REQ-026, 038 require proportional audit and historical integrity. Applicable evidence includes trusted time, actor/system context, action, Resource, correlation, authorization context, outcome and safe reason; trails require access control and tamper protection. Ordinary reads are not automatically high-Risk. | Initialization audit applicability/classification, trusted provenance, durable truthful recording, audit-unavailable behavior and uncertain commit/outcome reconciliation need explicit governed contracts. Quantity history alone does not establish trusted actor attribution or audit integrity. No audit infrastructure, retention period or blanket dual-approval rule is selected. |
| SAD-05 — Failure and concurrency safeguards | CONDITIONAL PASS | REQ-INV-007, 014, 016, 030, 036; BINV-REQ-013, 016, 022, 024, 039–040; Security Standards §12 require preservation of accepted state, safe duplicate/replay/concurrent effects, explicit uncertainty and authorized reconciliation. Uncertain effects must not become success or be blindly repeated; protected failure details must not leak. | Persistence uniqueness, concurrency, retry/idempotency, reconciliation and failure Contracts remain unselected. Known stale access cannot justify mutation. No locking, isolation, distributed transaction or retry strategy is chosen or verified. |
| SAD-06 — Security authority boundaries | PASS | PRODUCT.md §25.1; Architecture §§9, 35.2, 35.5; BIDN-REQ-004; REQ-ADM-006–011; BINV-REQ-008–010 and Product evidence Contract §§5–7, 12 preserve Security control ownership, Identity evidence ownership, Inventory contextual authorization and Administration coordination. Cross-domain evidence grants no shared transaction or security override. | Security, Identity and Administration affected-owner dispositions and overall acceptance remain separate outstanding gates. Inventory, Architecture and Product approvals cannot substitute for them. |

### Evidence Sources and Limits

All references below are read-only Approved governing sources, normative within their
assigned authority. This assessment does not promote DEC-0045 or any other Proposed
policy to Accepted status.

- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md), §§10–13, 27: trusted identity, assurance, default-deny contextual authorization, revocation and audit integrity.
- [Architecture](../../.ai/core/ARCHITECTURE.md), §§9, 35.2, 35.5: ownership, application-owned transactions and intentional exported Contracts.
- [PRODUCT.md](../../.ai/core/PRODUCT.md), §§16.2, 25.1: Stock safeguards and accountable cross-domain ownership.
- [Inventory Domain](../domains/inventory/inventory-domain.md), REQ-INV-003–008, 014, 016–018, 027–030, 033, 036: association, authority, concurrency, audit and failure integrity.
- [Inventory Backend](../backend/inventory/inventory-backend.md), BINV-REQ-008–010, 013–016, 022, 024–026, 038–040: contextual authorization, consistency and safe effects.
- [Product evidence Contract 1.1.0](../contracts/product/internal-variant-evidence-contract.md), §§5–7, 12: coherent bounded observations, revalidation and explicit non-authorizations.
- [Identity Backend](../backend/identity/identity-backend.md), BIDN-REQ-004, 024–027: trusted access evidence, assignment withdrawal and service-principal boundaries.
- [Administration Domain](../domains/admin/admin-domain.md), REQ-ADM-005–011: Staff/Principal separation, current contextual authorization and owning-Domain invocation.

Operational initialization remains blocked until its required authority, provenance,
audit and safety guarantees are governed and available. This is not a finding that all
representation choices conflict with Security; it is a distinction between conditional
governance compatibility and unestablished operational readiness. No initialization actor,
Staff mapping, new Role or Permission, Identity trust issuer, API/authorization Contract,
database schema, concurrency/retry/reconciliation strategy, operational procedure or
deployment is selected. Existing implementation restrictions remain unchanged.

The Security-owner disposition, Identity and Administration affected-owner reviews and
overall DEC-0045 acceptance remain Pending. Assessment completion does not satisfy any
of those approvals and claims no production security assurance.

## Acceptance Conditions

| Accountable capacity | Disposition | Required determination |
| --- | --- | --- |
| Inventory business owner | Approved — 2026-11-05 (Inventory business-owner capacity only) | Whole-unit non-negative on-hand semantics, one logical location, explicit initialization and operational scope. |
| Inventory technical owner | Approved — 2026-11-05 (Inventory technical-owner capacity only) | Java `long` suitability, range/overflow safety, implementable invariants and deferred technical mechanisms. |
| Architecture owner | ACCEPT WITH CONDITIONS — completed 2026-11-05 (Architecture-owner capacity only) | Domain boundaries, Variant/location association, Product evidence consumption and whether separate ADR governance is triggered. |
| Product affected owner | ACCEPT WITH CONDITIONS — completed 2026-11-05 (Product affected-owner capacity only) | Product identity, membership and lifecycle preservation; whole-unit applicability; logical-location neutrality; bounded evidence and consistency limits. |
| Security affected owner | Pending — accountable Security-owner review required. | Stock mutation authorization boundaries; default denial and contextual Resource/action scope; trusted human/system provenance; audit integrity; replay, uncertainty and failure safeguards. |
| Identity affected owner | Pending — accountable Identity-owner review required. | Trusted human/system Principal evidence, current access and revocation, Staff association where applicable, service-origin provenance and action-specific assurance boundaries; no issuer, Role, Permission or evidence producer selected. |
| Administration affected owner | Pending — accountable Administration-owner review required. | Staff/Principal separation, governed operational coordination, current assignment and Resource/action scope, owning-Domain authorization and audit boundaries; no Staff mapping or operational permission selected. |

The bounded Architecture assessment is complete. The accountable Architecture-owner
disposition is ACCEPT WITH CONDITIONS — completed 2026-11-05 across ARD-01 through ARD-05
and subject to AOC-01 through AOC-06. The Product affected-owner disposition is complete with
ACCEPT WITH CONDITIONS (POC-01–POC-06). Security,
Identity and Administration affected-owner reviews and
overall DEC-0045 acceptance remain pending. These completed dispositions do not satisfy separate
implementation and operational readiness gates.

Inventory business and technical-owner dispositions above are recorded in their respective
capacities only. Architecture-owner approval is recorded separately with binding conditions.
Product affected-owner approval is recorded separately below the assessment and is
subject to POC-01 through POC-06. Security and Identity
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
| 0.8.0 | 2026-11-05 | Proposed | Recorded bounded Security governance evidence assessment SAD-01–SAD-06; preserved pending accountable Security-owner disposition, Identity/Administration reviews, overall acceptance and implementation/operational restrictions. |
| 0.7.0 | 2026-11-05 | Proposed | Recorded accountable Product affected-owner ACCEPT WITH CONDITIONS disposition POC-01–POC-06; preserved completed Inventory and Architecture dispositions, pending Security/Identity/Administration reviews, overall acceptance and implementation/operational restrictions. |
| 0.6.0 | 2026-11-05 | Proposed | Recorded bounded Product governance evidence assessment PRD-01–PRD-06 PASS WITH CONDITIONS; preserved pending Product-owner disposition, remaining affected-owner reviews, overall acceptance and implementation/operational restrictions. |
| 0.5.0 | 2026-11-05 | Proposed | Recorded accountable Architecture-owner ACCEPT WITH CONDITIONS disposition across ARD-01–ARD-05 and AOC-01–AOC-06; preserved pending affected-owner reviews, overall acceptance and implementation/operational restrictions. |
| 0.4.0 | 2026-11-05 | Proposed | Recorded bounded Architecture assessment PASS with conditions across ARD-01–ARD-05; preserved pending Architecture-owner disposition, affected-owner reviews and implementation/operational restrictions. |
| 0.3.0 | 2026-11-05 | Proposed | Recorded confirmed Inventory technical-owner directions ITD-01 through ITD-08; preserved IBD-01 through IBD-08 business approval, pending Architecture and affected-owner reviews, unselected mechanisms and all implementation and operational authorization gates. |
| 0.2.0 | 2026-11-05 | Proposed | Recorded explicit Inventory business-owner approval of IBD-01 through IBD-08; preserved pending Inventory technical and Architecture dispositions, affected-owner reviews and all remaining implementation and operational governance gates. |
| 0.1.0 | 2026-11-04 | Proposed | Proposed Variant/location Stock association, one governed logical location, whole-unit non-negative quantities, separately reviewed Java `long` representation and explicit Inventory initialization; retained pending accountable dispositions and all operational, policy, Contract and implementation gates. |
