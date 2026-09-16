# ADR-0002 — Identity and Access Backend Specification Selection

## Identifier

ADR-0002

## Title

Identity and Access Backend Specification Selection

## Status

Accepted

## Date

2026-09-16

## Owner

Architecture

## Context

ADR-0001 established the Shared Backend Baseline Specification as the first Backend Specification. That Specification now exists at `specifications/backend/shared/backend-baseline.md` as version `1.0.0 Approved` under scope `BEB`.

ADR-0001 and `ARCHITECTURE.md` §35 deliberately left downstream Backend Specification titles, filenames, paths, scope codes, and ordering unresolved. A further Architecture Decision is therefore required before the first downstream Backend Specification may be drafted under Approved governance.

The repository now contains 17 Approved Domain Specifications and nine Approved Frontend Specifications. The Approved Identity Domain establishes authoritative semantics for Identity, Authentication, credentials, Principal establishment, Sessions, revocation, access assignments, Staff access, Service Principals, security evidence, recovery, and reconciliation. Protected backend capabilities consume trusted Principal, Authentication, Session, revocation, access, and security evidence where applicable, while their owning business Domains retain contextual Authorization and business invariants.

Identity is therefore a foundational dependency for later protected backend specializations. This does not imply that every public backend operation requires Authentication.

## Decision

The first downstream Backend Specification after the Shared Backend Baseline SHALL be:

- **Title:** Identity and Access Backend Specification
- **Path:** `specifications/backend/identity/identity-backend.md`
- **Scope code:** `BIDN`
- **Position:** First downstream Backend Specification after the Shared Backend Baseline

`BIDN` means Backend Identity. It is unique among current Specification scope codes, is introduced only for the Identity and Access Backend Specification, and does not reserve or imply a scope-code convention for future Backend Specifications. Future downstream scope codes remain unresolved.

The Identity and Access Backend Specification MUST inherit every materially applicable BEB Requirement and explicitly trace that inheritance. It MUST specialize the Approved Identity Domain without transferring Identity Domain authority to the backend Specification or redefining Identity semantics.

Where governed by higher-authority sources, BIDN may provide bounded backend specialization for:

- Authentication;
- credentials;
- credential recovery;
- Principal establishment;
- Sessions;
- revocation;
- access assignments;
- Staff access;
- Service Principals;
- security evidence;
- recovery and reconciliation;
- Identity-owned Use Case orchestration;
- Identity-specific Contract obligations; and
- Identity-specific persistence, concurrency, failure, event, integration, and verification obligations.

BIDN MUST NOT define concrete behavior that is not already governed.

## Authority Boundary

BIDN remains subordinate to governing sources, Approved Business Requirements, and the Approved Identity Domain. It MUST inherit materially applicable BEB Requirements and MAY consume applicable Approved frontend Contract requirements without taking frontend authority.

BIDN is not repository-wide authority. It MUST NOT duplicate, weaken, conflict with, or transfer authority from BEB, and it MUST NOT redefine Identity Domain semantics.

Other protected backend capabilities MAY consume trusted Identity evidence where applicable. The owning business Domain retains authority over whether its current Resource, action, property, association, and Domain state permit an operation. BIDN MUST NOT centralize or replace contextual business Authorization.

## Exclusions

This decision does not decide, and does not authorize BIDN to decide:

- Customer profile behavior, Account business semantics, Address, Preference, Consent, or Wishlist;
- another Domain's Resource state, invariants, or contextual Authorization policy;
- Administration workflow policy or a concrete Role and Permission matrix;
- Identity Provider or Authentication protocol selection;
- token format, Claims schema, cookie strategy, browser storage, or Session storage implementation;
- token or Session rotation or lifetime;
- password algorithm or concrete credential policy;
- MFA, SSO, or email-verification enablement, mechanism, or policy;
- concrete routes, HTTP methods, DTO fields, database schemas, tables, SQL, ORM, or provider payloads;
- event names, event payloads, messaging broker, or cloud and infrastructure topology;
- numerical thresholds, timeout values, retry counts, or retention periods;
- fraud provider, model, score, or manual-review workflow; or
- the remaining downstream Backend Specification roadmap.

## Dependency Rationale

Identity owns Authentication and trusted Principal establishment. Protected Customer, Cart, Checkout, Payment, Order, Shipping, Return, Reporting, Administration, and other backend capabilities require authenticated or authorized context where applicable. Administration specifically requires trusted Staff User and access evidence.

Identity can be specialized without requiring any consuming Domain to surrender its business authority. A generic API, persistence, or event Specification would substantially duplicate BEB and the applicable backend standards, while concrete API, stored-truth, and event semantics require an owning Domain. Cross-domain workflow Specifications have broader dependencies and greater authority-transfer risk.

## Alternatives Considered

### A. Generic API Backend Specification First

Not selected because BEB and `API.md` already govern shared API qualities. Concrete API semantics require an owning Domain and must not be invented by a generic technical Specification.

### B. Generic Persistence Backend Specification First

Not selected because BEB, `DATABASE.md`, and `POSTGRES.md` already govern shared persistence qualities. Concrete stored truth and transaction semantics require Domain ownership.

### C. Generic Events or Integration Specification First

Not selected because BEB and `EVENTS.md` already govern shared event and integration qualities. Concrete event facts require an owning Domain.

### D. Customer or Account Backend Specification First

Not selected because protected Account behavior consumes trusted Identity context and must preserve the separation between Customer business truth and Identity security truth.

### E. Cart or Checkout Backend Specification First

Not selected because Cart and Checkout coordinate several Domains and protected Customer context and therefore have a broader dependency surface.

### F. Payment Backend Specification First

Not selected because Payment is provider- and integration-heavy and participates in protected workflows that consume trusted Identity and security context.

### G. Define the Complete Backend Roadmap Now

Rejected because repository evidence does not justify fixing all later titles, paths, scope codes, or ordering. Those choices remain independently governable.

## Consequences

Positive consequences include:

- a governed first specialization after BEB;
- trusted Identity backend boundaries before later protected capabilities;
- direct traceability to the Approved Identity Domain and BEB;
- preserved modular-monolith and hexagonal boundaries;
- reduced risk that generic technical Specifications duplicate BEB; and
- independent governance of later roadmap decisions.

Costs and trade-offs include:

- Identity backend specification work precedes other protected backend specializations;
- token and Session Architecture remains deferred;
- provider- or mechanism-specific behavior cannot be specified without applicable governance; and
- later Backend Specification sequencing still requires separate decisions.

## Acceptance Requirements

This decision was Accepted after:

- Architecture review;
- affected Identity and Security ownership review;
- confirmation that `BIDN` remains unique;
- confirmation that `specifications/backend/identity/identity-backend.md` remains collision-free;
- confirmation that no unresolved Product or Architecture Decision is implicitly resolved; and
- synchronized canonical updates to `ARCHITECTURE.md` and `DECISIONS.md`.

The Identity and Access Backend Specification MUST NOT be drafted as an Approved-governance downstream Specification until ADR-0002 is Accepted and the required canonical synchronization is complete.

## Acceptance Synchronization

Acceptance synchronized governance through:

- `DECISIONS.md`: added ADR-0002 to the Decision Index as Accepted and updated metadata to version `1.0.4` with synchronized Revision History.
- `ARCHITECTURE.md` §35: established the Identity and Access Backend Specification, path `specifications/backend/identity/identity-backend.md`, scope `BIDN`, its position as the first downstream Backend Specification after BEB, mandatory materially applicable BEB inheritance, and the continued unresolved status of all later downstream titles, paths, scope codes, and ordering; updated metadata to version `1.3.0` with synchronized Revision History.

No change to `ARCHITECTURE.md` §30 is required unless acceptance review establishes that it is necessary for consistency.

## Matters Deliberately Unresolved

This decision leaves unresolved:

- the complete downstream Backend Specification roadmap;
- all later Backend Specification titles, paths, scope codes, and ordering;
- Customer and Account backend decomposition;
- catalogue and discovery backend decomposition;
- Cart and Checkout backend decomposition;
- Payment, Shipping, Notifications, and Reporting ordering;
- token and Session Architecture;
- Identity Provider selection;
- token and Claims formats;
- cookie, browser-storage, and Session-storage mechanisms;
- MFA, SSO, and email-verification policy;
- the Role and Permission matrix;
- fraud policy and provider selection;
- concrete APIs, DTOs, and persistence schemas;
- event names and payloads;
- external messaging adoption;
- provider choices;
- infrastructure topology; and
- numerical operational values.

## Security Impact

This decision introduces no new security policy or mechanism. It places future Identity backend specialization under the Approved Identity Domain, BEB, and governing Security requirements. Identity Provider, credential, Session, token, MFA, SSO, verification, Role, Permission, and fraud-policy choices remain unresolved.

## Data Impact

This decision creates no schema, table, SQL, storage, retention, migration, or data-model change. Identity-owned data authority remains defined by the Approved Identity Domain, and other Domains retain their own data authority.

## Compatibility and Migration Impact

This decision creates no runtime or Contract migration. The future BIDN Specification must preserve applicable BEB compatibility obligations and may define concrete compatibility requirements only where higher-authority semantics support them.

## Operational Impact

This decision creates no runtime operational behavior, provider commitment, infrastructure topology, numerical target, or support policy. Future BIDN Requirements may specialize governed observability, recovery, reconciliation, and security-evidence outcomes without selecting unresolved mechanisms.

## References

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/backend/API.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0001-backend-specification-roadmap.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/admin/admin-domain.md`
- `specifications/frontend/shared/frontend-baseline.md`
- `specifications/frontend/storefront/account-identity.md`
- `specifications/frontend/administration/administration-frontend.md`

## Supersedes

—

## Superseded By

—
