# ADR-0003 — Customer and Account Backend Specification Selection

## Identifier

ADR-0003

## Title

Customer and Account Backend Specification Selection

## Status

Proposed

## Date

2026-09-17

## Owner

Architecture

## Context

ADR-0001 established the Shared Backend Baseline Specification under scope `BEB` as the first Backend Specification. ADR-0002 then established the Identity and Access Backend Specification under scope `BIDN` as the first downstream Backend Specification after BEB. Both Specifications now exist as Approved `1.0.0` baselines.

ADR-0002 and `ARCHITECTURE.md` §35 deliberately leave every Backend Specification title, path, scope code, and ordering position after BIDN unresolved. A further Architecture Decision is therefore required before another downstream Backend Specification may be drafted under Approved governance.

The Approved Customer Domain owns Customer and Account business meaning, Customer-owned profile, Address, Preference, governed Consent, Customer isolation, Customer-side registration outcomes, Customer history, and other Customer-owned behavior. It depends on Identity for trusted Principal and Authentication context, credentials, Sessions, recovery verification, revocation, and Identity access evidence. The Approved BIDN Specification now provides that governed backend Identity boundary.

Customer supplies governed Customer and Address context consumed by Cart and Checkout. Administration invokes Customer capabilities without replacing Customer authority. These dependencies make a combined Customer and Account backend specialization the evidence-supported next backend boundary while providing no basis for fixing any later Backend Specification position.

## Decision

The second downstream Backend Specification after the Shared Backend Baseline, immediately after BIDN, is proposed to be:

- **Title:** Customer and Account Backend Specification
- **Path:** `specifications/backend/customer/customer-backend.md`
- **Scope code:** `BCUS`
- **Position:** Second downstream Backend Specification after BEB, immediately after BIDN

`BCUS` means Backend Customer. It is proposed only for the Customer and Account Backend Specification. It does not reserve or imply a scope-code convention for later Backend Specifications, whose scope codes remain unresolved.

BCUS MUST specialize the Approved Customer Domain without redefining Customer semantics or transferring Customer Domain authority. Customer and Account business meaning governed by the Customer Domain MUST remain within the same BCUS backend Specification at this stage; this decision does not create a separate Account backend Specification.

Where already governed by the Approved Customer Domain, BCUS MAY provide bounded backend specialization for:

- Customer-owned profile behavior;
- Address behavior;
- Preference behavior;
- governed Consent behavior;
- Customer and Customer-owned Resource isolation;
- Customer and Account association;
- Customer-side registration outcomes;
- Customer history and governed historical references;
- Customer-owned Use Case orchestration;
- Customer-specific Contract obligations; and
- Customer-specific persistence, concurrency, failure, recovery, event, integration, observability, audit, and verification obligations.

BCUS MUST inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It MUST consume BIDN Contracts or trusted Identity evidence only where materially applicable and MUST NOT transfer Identity authority into BCUS.

BCUS MUST NOT define concrete Customer behavior, Product policy, security policy, or implementation detail that is not already governed.

## Authority Boundary

BCUS remains subordinate to governing sources, Approved Business Requirements, the Approved Customer Domain, BEB, and materially applicable BIDN Contracts and evidence. It MUST preserve every other Approved Domain's authority and MAY consume applicable Approved frontend Contract requirements without taking frontend authority.

Customer and Account business meaning, profile, Address, Preference, Consent, Customer isolation, and other Customer-owned truth remain governed by the Approved Customer Domain. Identity-owned Authentication, credentials, Principal establishment, Sessions, revocation, recovery verification, and Identity access evidence remain governed by the Approved Identity Domain and specialized by BIDN.

BCUS is not repository-wide authority. It MUST NOT duplicate, weaken, conflict with, or transfer authority from BEB or BIDN. It MUST NOT redefine Identity evidence, infer Customer authority solely from Identity evidence, or make Customer or Account state authoritative for another Domain's truth.

Contextual business Authorization remains with the Domain that owns the Resource, action, property, association, and current Domain state. BCUS may decide contextual Authorization only for Customer-owned Resources and behavior within Customer Domain authority; it MUST NOT centralize or replace another Domain's contextual Authorization.

## Exclusions

This proposed decision does not decide, and does not authorize BCUS to decide:

- concrete API routes, operations, HTTP methods, status mappings, or DTO fields;
- public or internal payload schemas;
- persistence schemas, tables, columns, SQL, ORM mappings, indexes, constraints, or migration mechanics;
- event names, event schemas, payloads, topics, consumers, brokers, delivery technology, or choreography;
- Customer, Identity, analytics, communication, storage, cloud, or other provider choices;
- infrastructure, hosting, network, deployment, cache, queue, or messaging topology;
- Customer identifier format or generation design;
- a concrete Account representation, Aggregate, lifecycle, state machine, or persistence model;
- Consent or Preference storage, evidence, projection, or synchronization mechanism;
- Identity Provider, Authentication protocol, token format, Claims schema, Session mechanism, cookie strategy, browser storage, rotation, lifetime, MFA, SSO, or email-verification policy;
- concrete Role or Permission matrix;
- numerical limits, thresholds, timeouts, retry counts, retention periods, performance targets, SLAs, or SLOs;
- Product decisions concerning guest Checkout, Wishlist, Consent, communication, data requests, support, fraud, or other unresolved policy; or
- any later Backend Specification title, path, scope code, decomposition, or ordering position.

## Dependency Rationale

The Approved Customer Domain requires trusted Identity context before a Principal may act for a Customer and assigns credentials, Authentication, Sessions, recovery verification, and revocation to Identity. BIDN is Approved and now provides the governed backend boundary for consuming that evidence without transferring Identity authority.

Customer is also upstream of protected commerce behavior. It supplies governed Customer, Account, Address, Preference, and applicable Consent context to Cart and Checkout while those Domains retain their own intent and orchestration authority. Administration invokes Customer capabilities through authorized Staff workflows while Customer retains Customer rules, isolation, privacy, and historical truth.

This creates a direct evidence-supported sequence from BIDN to BCUS. The sequence does not imply that Customer owns Cart, Checkout, Administration, or any other downstream behavior, and it does not determine which backend specialization follows BCUS.

## Alternatives Considered

### A. Separate Customer and Account Backend Specifications Now

Not selected because the Approved Customer Domain owns Customer and Account business meaning together, and current evidence does not establish an independent Account Domain authority or justify a second backend boundary. A later separately governed change may revisit decomposition if approved requirements establish a distinct owner.

### B. Account Backend Specification Before Customer

Not selected because Account business meaning is governed within the Customer Domain. Placing Account first or outside Customer would create authority ambiguity and risk conflating Account, Identity, Principal, and Session.

### C. Cart or Checkout Immediately After BIDN

Not selected because Cart and Checkout consume governed Customer or Visitor context, Customer Authorization, and Address context in addition to Product, Pricing, Inventory, Shipping, Payment, and other Domain facts. Customer specialization provides a more direct next dependency boundary without attempting to settle those broader orchestration dependencies.

### D. Administration Immediately After BIDN

Not selected because Administration coordinates protected capabilities across Customer and many other owning Domains. It depends on those capabilities and trusted Identity evidence but must not replace their authority, giving it a broader dependency and authority-transfer surface than Customer.

### E. Leave the Next Backend Specification Unresolved

Not selected because BIDN is Approved and repository evidence now supports a bounded Customer-aligned next step. Keeping the position unresolved would defer an evidence-supported dependency decision and prevent governed downstream drafting without preserving an identified architectural benefit.

## Consequences

Positive consequences include:

- a governed proposal for the second downstream backend specialization after BEB;
- one Customer-aligned backend boundary for Customer and Account business meaning already governed together;
- explicit consumption of BIDN evidence without Identity authority transfer;
- a Customer backend boundary available before more dependency-heavy Cart, Checkout, and Administration specializations;
- direct traceability to the Approved Customer Domain, BIDN, and BEB;
- preservation of modular-monolith and hexagonal boundaries; and
- continued independent governance of all later backend roadmap decisions.

Costs and trade-offs include:

- Customer and Account backend work would precede other domain-aligned backend specializations;
- BCUS must preserve a careful boundary between Customer business truth and Identity security truth;
- unresolved Product policy and implementation mechanisms remain deferred;
- a later approved decomposition change may require migration and compatibility planning; and
- every Backend Specification position after BCUS still requires separate governance.

## Acceptance Requirements

This Proposed decision becomes Accepted only after:

- Architecture review;
- affected Customer and Identity ownership review;
- confirmation that `BCUS` remains unique among current Specification scope codes;
- confirmation that `specifications/backend/customer/customer-backend.md` remains collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md`.

The Customer and Account Backend Specification MUST NOT be drafted as an Approved-governance downstream Specification until ADR-0003 is Accepted and the required canonical synchronization is complete.

## Acceptance Synchronization

When ADR-0003 is accepted, governance must synchronize only:

- ADR-0003: change status from Proposed to Accepted and correct lifecycle-dependent wording where required.
- `ARCHITECTURE.md` §35: establish the Customer and Account Backend Specification, path `specifications/backend/customer/customer-backend.md`, scope `BCUS`, its position as the second downstream Backend Specification after BEB and immediately after BIDN, mandatory materially applicable BEB inheritance, materially applicable BIDN consumption without Identity authority transfer, Customer Domain specialization, combined Customer and Account backend decomposition at this stage, and the continued unresolved status of all later Backend Specification titles, paths, scope codes, decompositions, and ordering. Update metadata, Document Status, and Revision History according to repository convention.
- `DECISIONS.md`: add ADR-0003 to the Decision Index as Accepted and update metadata, version, and Revision History according to repository convention.

No `PRODUCT.md` change is required because this proposed decision preserves Product authority and leaves every applicable Open Product Decision unresolved. If acceptance review identifies a direct Product contradiction, acceptance MUST stop until that contradiction is governed; this ADR does not authorize editing or overriding `PRODUCT.md`.

## Matters Deliberately Unresolved

This proposed decision leaves unresolved:

- every Backend Specification title, path, scope code, decomposition, and ordering position after BCUS;
- concrete Customer and Account APIs and DTOs;
- Customer and Account persistence schemas and physical data design;
- Customer and Account event names, payloads, producers, consumers, and delivery mechanisms;
- provider and infrastructure choices;
- Customer identifier design;
- concrete Account representation and lifecycle design;
- Consent and Preference storage and synchronization mechanisms;
- Identity Provider, Authentication protocol, token, Claims, cookie, browser, Session, MFA, SSO, and verification mechanisms;
- concrete Role and Permission matrix;
- guest Checkout, Wishlist, Consent, communication-preference, support, fraud, data-request, and other unresolved Product policy;
- compatibility versions and migration mechanics for any later concrete Contract or schema; and
- numerical operational values.

## Security Impact

This proposed decision introduces no new security policy or mechanism. BCUS would consume trusted BIDN evidence where materially applicable while preserving current server-side contextual Authorization, Customer isolation, privacy, Sensitive Data protection, resource concealment, least privilege, and audit obligations governed elsewhere. Identity mechanisms, access matrices, fraud policy, and numerical security controls remain unresolved.

## Data Impact

This proposed decision creates no schema, table, column, SQL, storage, migration, retention, identifier, Account representation, Consent-storage, or Preference-storage design. Customer-owned data remains governed by the Approved Customer Domain. Identity-owned data remains governed by the Approved Identity Domain and BIDN, and every other Domain retains its data authority.

## Compatibility and Migration Impact

This proposed decision creates no runtime, data, or Contract migration. A future BCUS Specification must inherit materially applicable BEB compatibility obligations and consume BIDN Contracts compatibly where applicable. Concrete compatibility versions and migration mechanics require later governed Contracts or designs.

## Operational Impact

This proposed decision creates no runtime operational behavior, provider commitment, infrastructure topology, support process, escalation path, capacity target, threshold, timeout, retry count, SLA, or SLO. A future BCUS Specification may specialize governed observability, recovery, reconciliation, and audit outcomes without selecting unresolved mechanisms or numerical values.

## References

- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/PRODUCT.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/adr/ADR-0002-identity-access-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/admin/admin-domain.md`

## Supersedes

—

## Superseded By

—
