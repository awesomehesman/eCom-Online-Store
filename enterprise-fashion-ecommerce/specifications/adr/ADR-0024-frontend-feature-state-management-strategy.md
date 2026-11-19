# ADR-0024 — Frontend Feature-State Management Strategy

- **Identifier:** ADR-0024
- **Title:** Frontend Feature-State Management Strategy
- **Type:** Architecture Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-11-19
- **Last Updated:** 2026-11-19
- **Owner:** Architecture
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Problem Statement

The Approved frontend Architecture establishes a feature-oriented Angular application,
prefers Signals for local and feature state, uses RxJS for asynchronous streams and
cancellation, permits explicit stores for approved cross-component state, and keeps
server state authoritative for business truth. It does not select a state-management
library or establish one repository-wide feature-store mechanism.

As Storefront and Administration features grow, nontrivial state must coordinate
loading, validated server observations, derived presentation state, pagination,
request cancellation, stale-result rejection, navigation and cleanup without moving
transport validation into Components or creating competing copies of backend truth.
Allowing each feature to choose an unrelated state architecture would increase
coupling, review cost and inconsistent lifecycle behavior. Selecting a named library
and prohibiting competing long-lived patterns is a material cross-cutting Architecture
decision and therefore requires ADR governance and canonical Architecture
synchronization.

`.ai/frontend/ANGULAR.md` v1.1.0 records detailed proposed implementation guidance
but remains Draft and non-authoritative. This Proposed ADR supplies the missing
Architecture decision boundary. It does not approve that standard, admit a dependency
or change the current Approved Architecture baseline.

## Decision Drivers

- Maintain a coherent, understandable frontend state architecture.
- Make feature ownership, mutation and lifecycle explicit.
- Support pure reactive derived state without duplicated mutable values.
- Keep state orchestration independently testable from Components and transport.
- Provide clear initialization, cancellation, cleanup and navigation behavior.
- Reject stale asynchronous results after supersession or destruction.
- Preserve data-access ownership of HTTP and Contract validation.
- Preserve backend and owning-Domain authority for business decisions.
- Permit incremental, feature-by-feature migration without a repository rewrite.
- Avoid the ceremony and global coupling of traditional Redux-style architectures.

## Proposed Decision

**PROPOSED:** NgRx Signal Store (`@ngrx/signals`) SHALL be the standard mechanism
for nontrivial, feature-owned application state.

Angular Signals SHALL remain the default for component-local presentation state.
Pure derived state SHALL use computed signals rather than separately mutable copies.
Signal Store methods SHALL express feature operations and own the related frontend
state transitions. RxJS SHALL remain available where HTTP, cancellation, concurrency
or another genuinely asynchronous stream requires it.

Feature stores SHALL be provided at the owning feature or route boundary by default.
Application-global provisioning requires explicit evidence of an application-wide
lifecycle and owner; convenience or reuse alone is insufficient. Trivial local UI
state does not require a store.

This proposal preserves the existing feature, shell and data-access boundaries. It
does not make a frontend store authoritative for Product visibility, Price, Discount,
Inventory, Payment, Order, Authentication, Authorization or any other backend-owned
business outcome.

## Responsibility Boundaries

| Boundary | Responsibility | Must not become |
| --- | --- | --- |
| Component | Presentation, semantic interaction, DOM behavior, local UI state and focus/accessibility behavior. | HTTP adapter, Contract validator, backend authority or hidden feature-state owner. |
| Signal Store | Feature-owned frontend state, derived state, operations, loading lifecycle, cancellation coordination and stale-result suppression. | Domain authority, transport parser, durable system of record or global store by default. |
| Data access | Typed HTTP transport, request composition, response and Problem validation, and translation into frontend-consumable outcomes. | Feature presentation owner or source of business authority. |
| Backend | Authoritative business decisions, lifecycle truth, Authentication, Authorization and governed evidence. | A truth inferred or replaced by frontend state. |

Validated data-access outcomes may enter the owning feature store. Request parameters,
unvalidated payloads, browser state, visibility, successful rendering or a store
transition SHALL NOT establish authoritative backend facts.

## Prohibited Competing Patterns

If this proposal is Accepted and synchronized into the canonical Architecture, new
frontend application-state architecture SHALL NOT use:

- traditional NgRx Store;
- Redux-style global application-state architecture;
- NgRx actions or reducers;
- NgRx Effects; or
- RxJS `Subject` variants as a competing feature-state store.

RxJS Subjects may still be used for narrowly owned event or test coordination where
they do not become an application-state authority. Existing local Signals and simple
project-owned state that do not constitute a competing architecture may remain until
incremental migration provides value.

## Alternatives Considered

| Alternative | Benefits | Costs and decision treatment |
| --- | --- | --- |
| Angular Signals with project-owned services | No additional state dependency; maximum local control. | Repeated store conventions, lifecycle code and inconsistent composition would remain project-owned. Viable for trivial local state, not selected as the proposed standard for nontrivial feature state. |
| Traditional NgRx Store and Effects | Mature ecosystem and explicit event/reducer model. | Adds global ceremony, action/reducer/effect indirection and broader coupling than this application currently needs. Not proposed. |
| No repository-wide feature-state standard | Each feature can choose the smallest immediate mechanism. | Encourages competing patterns and inconsistent cancellation, lifecycle and testing semantics. Not proposed. |
| Alternative third-party signal-store library | May provide comparable reactive composition. | Would require separate maturity, compatibility, provenance, maintenance and migration evidence. No alternative library is selected. |

## Consequences and Trade-offs

The proposal creates one recognizable pattern for complex feature state and keeps
Components smaller. Computed state, operations and lifecycle become directly
testable, while feature-level provisioning limits accidental global coupling.

The application acquires a third-party runtime dependency and an NgRx-specific API
surface. Contributors must learn its composition model, upgrades require Angular
compatibility review, and indiscriminate store creation could add abstraction without
value. The selected pattern therefore applies only to nontrivial feature-owned state;
local presentation state remains local.

Acceptance would constrain future state-library choices. Replacing the mechanism
would require Architecture governance, migration evidence and compatibility planning.

## Dependency Admission Requirements

ADR acceptance SHALL NOT itself admit `@ngrx/signals`. Before implementation reliance,
the exact dependency change must establish:

- compatibility with the repository's Angular and TypeScript baselines;
- deterministic lockfile integrity and reproducible resolution;
- package and publisher provenance appropriate to repository policy;
- license compatibility;
- current vulnerability review with its sources and review date;
- credible maintenance and support status;
- production bundle and runtime impact; and
- successful strict build, test and applicable security validation.

Version ranges, transitive artifacts and future upgrades remain governed by the
repository's dependency process. Local installation or successful compilation is not
independent dependency approval.

## Existing Implementation Evidence

The current feature branch contains a Catalogue Signal Store pilot under
`frontend/src/app/storefront/features/catalogue/state/`. It uses Signal Store state,
computed signals and methods while retaining `CatalogueApiClient` as the data-access
and validation boundary. Its tests exercise loading outcomes, pagination, request
cancellation, stale-result suppression and state transitions.

That pilot is implementation evidence only. It is not Architecture, Engineering,
Security or dependency-admission approval, and it MUST NOT be used to infer that this
Proposed decision is Accepted or that the implementation is eligible for merge or
operational reliance.

## Migration and Enforcement Strategy

If Accepted, adoption SHALL be incremental:

1. synchronize the Accepted decision into `.ai/core/ARCHITECTURE.md`;
2. approve the corresponding Angular implementation standard through its own lifecycle;
3. complete dependency admission before implementation reliance;
4. validate the Catalogue pilot as the first bounded feature implementation;
5. migrate another feature only when nontrivial state and delivery value justify it;
6. keep component-local Signals where a store would add no value; and
7. add automated import or architecture checks only where they enforce the Accepted boundaries without blocking legitimate local Signals or RxJS use.

No repository-wide rewrite, mandatory global store, bulk migration or state
persistence mechanism is selected.

## Non-Goals

This proposal does not:

- define Product, Customer, Identity, Payment, Inventory or Order policy;
- change API Contracts or backend behavior;
- make frontend state authoritative;
- select browser persistence, offline caching, SSR, PWA or hosting;
- select an Authentication or token-storage strategy;
- require Signal Store for trivial presentation state;
- authorize traditional NgRx packages or competing state libraries;
- approve an exact package version, lockfile or transitive dependency graph;
- approve the existing pilot implementation; or
- authorize deployment or operational activation.

## Accountable-Owner Dispositions

| Capacity | Required determination | Current disposition |
| --- | --- | --- |
| Architecture | Compatibility with the frontend Architecture, appropriateness of the cross-cutting selection, scope, alternatives, reversibility and canonical synchronization. | PENDING — no accountable Architecture approval is recorded. |
| Engineering | Maintainability, Angular compatibility, testability, dependency impact, incremental migration and enforceability. | PENDING — no accountable Engineering approval is recorded. |
| Security | Dependency and browser-state risks, preservation of backend authority, Sensitive Data boundaries and prohibited security reliance. | PENDING — no accountable Security approval is recorded. |

No approval is inferred from the project direction, Draft Angular standard, existing
dependency declaration, pilot code, tests, branch history or this proposal.

## Acceptance Criteria and Governance Gates

ADR-0024 may advance to Accepted only when:

1. accountable Architecture disposition explicitly accepts or amends the decision;
2. affected Engineering and Security reviews are attributable and their required findings are resolved or explicitly retained as separate gates;
3. the decision remains compatible with the Approved frontend boundaries and backend authority model;
4. alternatives, consequences, reversibility and migration impact have been reviewed;
5. dependency admission requirements and implementation approval remain distinguishable from ADR acceptance;
6. the Decision Index is synchronized without implying implementation completion;
7. a defined, reviewable plan exists to apply the decision to `.ai/core/ARCHITECTURE.md` through its approved process immediately after ADR acceptance; and
8. `.ai/frontend/ANGULAR.md` is promoted only after the Accepted ADR and canonical Architecture synchronization support that promotion.

Separate dependency admission, implementation validation, merge readiness and rollout
remain required after Architecture acceptance. Until the applicable gates close, the
Approved Architecture baseline remains unchanged and NgRx Signal Store SHALL NOT be
represented as the established repository standard.

## Related Documents

- [Architecture](../../.ai/core/ARCHITECTURE.md), §§21, 22 and 36.
- [Engineering Constitution](../../.ai/core/AGENTS.md), §§5, 9 and 37.
- [Decision Governance](../../.ai/core/DECISIONS.md), §§9, 10, 18, 19 and 49.
- [Documentation Standards](../../.ai/core/DOCUMENTATION-STANDARDS.md), §§7–9.
- [Angular Standards](../../.ai/frontend/ANGULAR.md), §§3, 21–23, 63 and 65.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-19 | Proposed | Proposed NgRx Signal Store as the standard for nontrivial feature-owned application state while preserving local Signals, RxJS asynchronous boundaries, feature/data-access separation, backend authority, incremental migration and separate dependency, implementation and approval gates. |
