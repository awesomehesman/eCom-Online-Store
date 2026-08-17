---
title: ANGULAR
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# Angular Standards

## 1. Purpose

This document defines the repository's lower-level Angular implementation standard. It translates the Approved frontend Architecture into coherent Angular practices without creating Product, Domain, Design System, Security, Testing, API, or Architecture authority.

## 2. Scope

This standard applies to repository-owned Angular application structure, Components, templates, dependency injection, state, RxJS, forms, routing, HTTP integration, browser security, testing, observability, configuration, dependencies, and implementation evidence.

It does not define page behavior, Product Requirements, visual tokens, API Contracts, provider behavior, detailed accessibility policy, performance budgets, or deployment topology.

## 3. Repository Authority

This standard is subordinate to the Decision Hierarchy in `.ai/core/AGENTS.md` and to Approved Canonical Documents within their scopes. Its `authoritative: false` metadata MUST remain false even if this document is later Approved.

`.ai/core/ARCHITECTURE.md` owns the frontend Architecture baseline. `.ai/core/PRODUCT.md` owns Product behavior. `.ai/core/DESIGN-SYSTEM.md` owns shared visual and interaction rules. Approved Security, Testing, Coding, Documentation, API, eventing, and Azure standards retain their assigned authority.

A material change to Architecture-owned frontend baselines requires an ADR and synchronized Architecture updates. Ordinary Angular implementation choices inside an approved Architecture boundary do not automatically require an ADR.

## 4. Normative Language

The terms **MUST**, **MUST NOT**, **REQUIRED**, and **PROHIBITED** are mandatory. **SHOULD** and **SHOULD NOT** identify strong defaults requiring documented rationale to depart from. **MAY** identifies a permitted option, not a selected technology or Product commitment.

## 5. Angular Baseline

The Approved frontend baseline is Angular 20, TypeScript strict mode, Standalone Components, Signals-first state, RxJS for asynchronous streams, Angular Router, and Typed Reactive Forms.

The customer and administration frontend remains separately deployable from the backend. It integrates with the trusted backend through the Approved REST API and MUST NOT become an authoritative Domain boundary.

## 6. Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | Angular 20 | Approved frontend framework baseline. |
| Established | TypeScript strict mode | Approved language and type-safety baseline. |
| Established | Standalone Components | Approved ordinary Component model. |
| Established | Signals | Preferred for local and feature synchronous state where appropriate. |
| Established | RxJS | Approved for HTTP, event streams, cancellation, and asynchronous composition. |
| Established | Typed Reactive Forms | Approved nontrivial form baseline. |
| Established | Angular Router and route-level lazy loading | Approved routing baseline. |
| Established | OpenAPI 3.1 | Approved API Contract format. |
| Established | RFC 9457 Problem Details | Approved API error model. |
| Established | WCAG 2.2 AA | Approved accessibility target. |
| Established | Playwright | Approved end-to-end testing direction; exact configuration remains implementation-owned. |
| Established | Storybook | Approved Design System documentation direction; detailed adoption belongs to its companion standard. |
| Unselected | NgRx and NgRx SignalStore | No repository-wide state library is selected. |
| Unselected | Angular Material, Tailwind, and Bootstrap | No UI or styling library is selected. |
| Unresolved Architecture Decision | SSR, prerendering, or other frontend rendering strategy | Requires Architecture governance before becoming binding. |
| Unselected | PWA and service worker | No offline or installable-application baseline is selected. |
| Unresolved Architecture Decision | Frontend hosting platform | Architecture leaves the service selection open. |
| Unselected | Frontend Unit Test or Component Test runner | No runner is established by repository configuration or Approved governance. |
| Unselected | Visual-regression service | No service is selected. |
| Unselected | Analytics provider | Provider selection remains governed elsewhere. |
| Unresolved Architecture Decision | Identity Provider and Session/token strategy | Current Architecture leaves the strategy open. |

An empty reserved companion file or mention as an example does not establish a technology selection.

## 7. Application Structure

Frontend code MUST be organized primarily by feature or business capability. Storefront and administration shells, feature areas, the API client layer, approved client state, analytics Adapters, observability infrastructure, and Design System implementation MUST preserve their Architecture responsibilities.

Shared code MUST remain stable, stateless where practical, non-Domain-specific, and smaller than feature-owned code. A generic shared area MUST NOT become a dumping ground or hidden cross-feature dependency.

## 8. Standalone Components

New Components, directives, and pipes MUST be standalone unless a verified compatibility constraint requires otherwise. Ordinary application structure MUST NOT use NgModules merely from legacy habit.

Imports MUST be explicit and minimal. A legacy or third-party NgModule MAY be consumed at a compatibility boundary but MUST NOT drive a new NgModule-first Architecture.

## 9. Component Responsibilities

A Component SHOULD own one coherent rendering and interaction responsibility. Components MUST keep public inputs, outputs, state, accessibility behavior, failure states, and dependency boundaries understandable and testable.

Components MUST NOT combine unrelated orchestration, Domain policy, raw transport mapping, broad shared state, and presentation into one implementation.

## 10. Orchestration and Presentation Responsibilities

Route or feature Components MAY coordinate loading, routing, state, and Use Case interaction. Reusable presentation Components SHOULD receive explicit data and emit semantic user intent without acquiring hidden service or Domain ownership.

The repository does not require obsolete `smart`, `container`, `dumb`, or `presentational` naming. The underlying separation of orchestration from reusable presentation MUST remain clear.

## 11. Dependency Injection

Dependencies MUST be injected through Angular's supported dependency-injection system and scoped to the narrowest appropriate owner. Global providers MUST be justified by genuine application-wide lifecycle or identity.

Constructor injection and `inject()` are both supported Angular mechanisms. Constructor injection SHOULD remain a strong default for class dependencies when it makes them explicit and testable. `inject()` is appropriate in supported injection contexts, including functional guards, interceptors, providers, and factories, and MAY be used in classes where it improves clarity and remains within a valid injection context.

## 12. Services

Services SHOULD encapsulate cohesive infrastructure, orchestration, or shared state responsibilities. They MUST NOT become generic utility collections or hidden Domain authorities.

Service lifetime and provider scope MUST match state ownership. A root-scoped service MUST NOT retain Customer, Session, route, or feature state beyond its governed lifecycle.

## 13. Signals

Signals SHOULD represent synchronous component or feature state where ownership and mutation are explicit. Signal values MUST remain narrowly scoped and MUST NOT be used to create a competing copy of authoritative server state.

Derived values SHOULD use `computed()` rather than separately mutable Signals. A computed value MUST remain pure and MUST NOT perform I/O, mutation, navigation, logging, or other side effects.

## 14. Effects

`effect()` MAY synchronize state with an external imperative boundary when a real side effect is required. It MUST NOT be the default derivation mechanism or a hidden state-propagation graph.

Effects MUST have explicit ownership, avoid circular writes, and remain safe under repeated execution. An effect that allocates external resources, timers, listeners, subscriptions, or other cleanup-requiring state MUST use Angular-supported cleanup or lifecycle mechanisms. HTTP, persistence, analytics, and navigation side effects SHOULD remain in clear application or infrastructure boundaries.

## 15. Signal Ownership and Mutation

Writable Signals MUST have one owner. Consumers SHOULD receive read-only views or explicit commands where mutation is not their responsibility.

State reset on navigation, logout, identity change, feature disposal, and failed operations MUST be explicit. Unrelated Components MUST NOT mutate shared Signal state directly.

## 16. RxJS Role

RxJS SHOULD represent HTTP requests, user-event streams, provider events, cancellation, timing, and asynchronous composition. It MUST NOT be rejected merely because Signals are preferred for synchronous state.

`Subject` MUST NOT be the default state store. A Subject variant requires a justified streaming or multicast need and explicit lifecycle semantics.

## 17. Signals and RxJS Interoperability

Conversion between Signals and Observables MUST occur at a clear ownership boundary using supported Angular interoperability mechanisms. Conversion MUST NOT create duplicate subscriptions, feedback loops, lost cancellation, or competing mutable state.

The representation SHOULD match the source semantics: synchronous owned state as a Signal; asynchronous or cancelable sequences as an Observable.

## 18. Observable Composition and Cancellation

Streams MUST define completion, teardown, cancellation, error, and retry behavior. A stale request MUST NOT overwrite a newer state decision where replacement semantics apply.

Operator choice MUST match intent: `switchMap` for replacement and cancellation, `concatMap` for ordered serialization, `exhaustMap` for ignoring re-entry, and `mergeMap` for intentional concurrency. Concurrency MUST NOT be chosen accidentally.

These mappings are semantic defaults, not universal rules; operator choice MUST account for source behavior, cancellation requirements, ordering, concurrency, and side-effect safety.

## 19. Subscription Management

Framework-managed template consumption, including the `async` pipe, SHOULD be preferred where it preserves clarity. Framework-provided lifecycle teardown mechanisms SHOULD manage imperative subscriptions.

Manual subscriptions MUST have explicit teardown and ownership. Nested subscriptions are PROHIBITED when they obscure ownership, cancellation, error handling, or composition that can be expressed clearly in a single stream. Subscription callbacks MUST NOT hide unsafe duplicate effects.

## 20. State Categories and Boundaries

The frontend MUST distinguish:

- authoritative backend and Domain state;
- server-derived frontend state;
- application or feature state;
- component-local UI state; and
- transient interaction state.

Moving state between categories requires an explicit owner and lifecycle; convenience MUST NOT confer authority.

## 21. Local and Feature State

Component-local state SHOULD remain in the Component. Feature state SHOULD remain inside the feature unless multiple routes or workflows have an approved shared owner.

Cross-route state MUST define initialization, invalidation, reset, error, logout, Customer-change, and navigation behavior. Global state is not a substitute for clear ownership.

## 22. Server and Authoritative State

Frontend state, cached API data, search results, projections, browser storage, query parameters, or optimistic state MUST NOT become authoritative for Payment, Payment Authorization, Stock, Stock Reservation, Available-to-Sell, Price, Discount, Order state, Authorization, or Customer ownership.

Server-derived state MUST expose freshness, loading, failure, and reconciliation behavior appropriate to the Use Case.

## 23. State-Management Library Status

No repository-wide NgRx, NgRx SignalStore, Redux, Akita, Elf, or other state-management library is selected. Features MUST use the established Angular primitives and explicit simple stores where sufficient.

Introducing a cross-cutting state library requires evidence, dependency review, migration impact, and applicable Decision or Architecture governance. A feature MUST NOT independently establish a repository-wide state pattern.

## 24. Typed Reactive Forms

Typed Reactive Forms are the baseline for nontrivial forms. Form controls, groups, arrays, and submitted values MUST be typed and MUST represent intentional optionality.

Simple display-only interactions MAY avoid a form abstraction. Template-driven forms MUST NOT become a competing default for governed workflows.

## 25. Form Models and Mapping

Form models MUST remain frontend interaction models, distinct from API DTOs and Domain models. Mapping MUST make normalization, omitted values, Money, dates, identifiers, and optional fields explicit.

Forms MUST NOT bind backend Entities or blindly serialize arbitrary form state into requests.

## 26. Form Validation

Client validation improves feedback but does not replace Contract or Domain validation. Validators MUST be deterministic where possible, compose cleanly, and produce accessible, actionable errors.

Server errors MUST be mapped only to fields or form-level states justified by the Contract. A field MUST NOT display an invented business rule.

## 27. Async Validation and Submission

Async validators MUST prevent stale responses from overwriting newer validation state and MUST bound request frequency and work. Cancellation MUST be used when the underlying operation supports it or replacement semantics require it. They SHOULD NOT call the server on every change without an appropriate interaction policy.

Submission MUST prevent conflicting duplicate interaction while preserving recovery. Disabling a button alone is not idempotency. Duplicate-sensitive operations MUST follow the API Contract and use an Idempotency Key where required.

## 28. Routing and Lazy Loading

Angular Router is the routing baseline. Major features MUST use route-level lazy loading where compatible with user experience, deployment, and failure handling.

Routes MUST preserve navigational semantics, deep linking, refresh behavior, title and focus updates, error recovery, and appropriate separation between Storefront and administration layouts.

## 29. Route Guards and Resolvers

Route guards MAY improve navigation and prevent known-invalid UX paths, but they are not an Authorization boundary. The backend MUST reauthenticate and authorize protected operations.

Resolvers MAY be used when navigation truly requires data before activation. They SHOULD NOT block navigation for optional content or hide loading, cancellation, and failure behavior.

## 30. Authentication Boundary

The Identity Provider and final Session/token architecture remain unresolved. This standard does not select token storage, cookies, an Authentication SDK, or a login protocol.

Frontend Authentication state is a view of trusted backend or Identity state. Logout MUST follow the approved strategy and MUST NOT be represented as complete merely because local state was cleared.

## 31. Authorization Boundary

Roles, Permissions, Claims, guards, hidden controls, disabled buttons, and local state MAY guide UX but MUST NOT substitute for server-side Authorization. Object-level and state-dependent Authorization remain trusted backend responsibilities.

The UI MUST handle denial safely and MUST NOT reveal inaccessible Resource details through cached state, routing, errors, or optimistic rendering.

## 32. HTTP Integration

Frontend HTTP integration MUST use the Approved REST, HTTPS, JSON, `/api/v1`, OpenAPI 3.1, RFC 9457, Authentication, Authorization, Correlation ID, and idempotency baselines.

This standard does not invent Endpoint Contracts, JSON naming, identifiers, page limits, timeout values, retry counts, or compatibility periods.

## 33. HttpClient and Interceptors

Angular `HttpClient` is the framework HTTP boundary. Interceptors MAY implement cross-cutting transport concerns such as approved credentials, Correlation ID propagation, trace context, safe error normalization, and compatible headers.

Interceptors MUST NOT contain hidden Domain rules, Payment interpretation, navigation policy unrelated to transport, or broad mutable application state.

Interceptors MUST NOT silently retry non-idempotent, state-changing, or financially sensitive requests unless the API Contract and applicable idempotency protections explicitly make repetition safe.

## 34. API DTO Boundaries

Transport DTOs MUST be explicit, typed, and separate from Component state and Domain concepts. API data MUST be validated or safely interpreted according to the Contract before use.

OpenAPI-generated clients MAY be adopted only through approved dependency and generation governance. This document does not select a generator.

## 35. RFC 9457 Problem Details

Frontend error handling MUST recognize RFC 9457 Problem Details without exposing unsafe `detail`, provider internals, stack traces, or inaccessible Resource existence.

Stable error codes MAY drive Contracted recovery behavior. Human-readable messages MUST remain safe, accessible, and appropriate to Product and content guidance.

## 36. Correlation and Idempotency

The frontend MUST preserve or propagate Correlation ID according to the API Contract and MAY display a safe support reference. Correlation identifiers MUST NOT contain Sensitive Data.

Idempotency Keys MUST be generated, scoped, reused, and discarded according to the Contract. A key MUST NOT be reused for a different logical request or treated as proof of success.

## 37. Interaction and Failure States

Every material asynchronous view MUST define applicable initial, loading, empty, success, degraded, stale, denied, and error states. Pending or unknown state MUST remain distinguishable from success.

Failure containment MUST use Angular-supported routing, local error-state, global error handling, and recovery mechanisms appropriate to the boundary. This standard does not invent a framework `ErrorBoundary` primitive.

## 38. Retry Behavior

Retry MUST be bounded, operation-aware, and safe. The frontend MUST NOT automatically retry a state-changing or financially sensitive operation unless the Contract and idempotency protection make repetition safe.

Retry controls MUST communicate pending and uncertain outcomes and prevent re-entry that could duplicate effects. No retry count, backoff, or timeout value is selected here.

## 39. Payment Frontend Safety

The frontend MUST NOT establish Payment success from a Payment Redirect, browser result, query parameter, local state, browser storage, frontend callback state, or unvalidated Callback or Webhook information. Validated Payment Provider evidence processed through the trusted backend remains authoritative.

Frontend retry or repeated action MUST NOT create duplicate financial effects. Unknown Payment outcomes MUST remain explicit and direct the user toward governed status verification or reconciliation.

Raw CVV MUST NOT be logged, persisted, placed in browser storage, telemetry, analytics, error reports, or durable application state. Any approved transient handling MUST be minimal and confined to the Payment flow.

## 40. Inventory Frontend Safety

Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement. Catalogue state, search results, cached API data, availability labels, and optimistic UI are non-authoritative.

Repeated actions MUST NOT create duplicate Stock Reservations or permit Overselling. The UI MUST represent stale or uncertain availability honestly and revalidate through the trusted backend where required.

## 41. Price and Money

The frontend MAY format and present Money but MUST NOT use floating-point arithmetic for authoritative monetary calculation. Currency MUST remain explicit.

Final Price, Discount, Promotion, tax, shipping amount, Refund, Chargeback, and Settlement calculations remain governed by authoritative backend and Domain rules. This standard does not define rounding policy.

## 42. Date and Time

API timestamps MUST be parsed and displayed without silently changing their semantic type. Instants, local dates, local times, and zoned business times MUST remain distinct.

Formatting, locale, and time-zone choice remain governed by Product and Contract context. Client clock values MUST NOT establish authoritative expiry, ordering, Payment, Inventory, or audit facts.

## 43. Sensitive Data, Secrets, and Browser Storage

Secrets MUST NOT be shipped in bundles, source maps, configuration, Logs, analytics, telemetry, URLs, or browser storage. Refresh Tokens, Session secrets, and other sensitive credentials MUST NOT be placed in browser storage unless an Approved Identity or Security design explicitly requires and governs that storage. Unnecessary PII MUST NOT be placed in browser storage.

Access Tokens MUST NOT be placed in persistent browser storage without an approved design and documented Risk. Stored data MUST have an owner, purpose, minimal scope, expiry, invalidation behavior, and protection appropriate to sensitivity.

## 44. Cookies and Session Boundaries

The unresolved Session strategy MUST NOT be silently decided by Angular implementation. Client code MUST preserve approved cookie attributes and MUST NOT attempt to read an `HttpOnly` credential.

Cookie-authenticated mutations require the approved CSRF protection. Session expiry, renewal, revocation, logout, and multi-tab behavior must follow the eventual governed Contract.

## 45. XSS and DOM Safety

Untrusted content MUST use Angular template binding and built-in sanitization. Direct DOM APIs, raw HTML injection, unsafe URL construction, dynamic code execution, and unsafe sinks MUST be avoided.

Use of `bypassSecurityTrust*` requires documented justification, narrow ownership, trusted content provenance, and focused Security review. Sanitization bypass MUST NOT become a convenience wrapper.

## 46. CSRF, CORS, and External Navigation

CORS is a server and browser transport policy, not Authentication, Authorization, or CSRF protection. Frontend code MUST NOT weaken credential or origin controls.

Redirects and external links MUST validate allowed scheme, destination, and purpose. Unsafe schemes and user-controlled open redirects are PROHIBITED. New browsing contexts MUST prevent opener abuse where applicable.

## 47. File Handling

Frontend file selection MUST provide safe type, size, progress, cancellation, error, and accessibility behavior consistent with the Contract. Client checks improve UX but do not replace trusted server validation, authorization, malware controls, or storage policy.

Downloaded content MUST preserve safe filenames, content types, authorization, and browser handling. Object URLs and other temporary resources MUST be released when no longer needed.

## 48. Accessibility Boundary

WCAG 2.2 AA is the Approved target. Angular implementation MUST support semantic HTML, keyboard operation, focus management, accessible names, error association, status announcements, zoom, reduced motion, and responsive reading order as applicable.

Detailed accessibility rules belong to Approved core sources and to the applicable scope of `.ai/frontend/ACCESSIBILITY.md` when that companion's own metadata, substantive content, and assigned scope make it normative. File existence alone does not establish authority.

## 49. Design System Boundary

Reusable visual primitives, tokens, interaction patterns, and content guidance remain governed by `.ai/core/DESIGN-SYSTEM.md`. Feature code MUST consume established Design System patterns rather than recreate competing primitives.

This standard does not select colors, typography, spacing values, breakpoints, icons, animation values, a component library, or brand assets.

## 50. Responsive and Media Implementation

Components MUST adapt to content, interaction needs, and available space while preserving reading order, focus order, task completion, and critical information. Exact breakpoints remain deferred.

Images SHOULD use responsive sources, reserve dimensions or aspect ratio, provide appropriate alternative text, defer noncritical media where useful, and render safe fallbacks. Media optimization MUST not change Product meaning or expose private content.

## 51. Performance Boundary

Angular code MUST avoid obvious unnecessary work, duplicate requests, retained subscriptions, excessive rendering, unbounded DOM growth, and eager loading of noncritical features.

Detailed budgets, Core Web Vitals targets, bundle limits, thresholds, and measurement policy remain governed by applicable Approved Requirements and by the applicable scope of `.ai/frontend/PERFORMANCE.md` when that companion's own metadata, substantive content, and assigned scope make it normative. No numerical target is selected here.

## 52. Change Detection and Rendering

Components SHOULD use efficient change detection compatible with Signals and explicit data flow. Immutable updates and narrowly owned state SHOULD make rendering causes understandable.

Repeated rendering MUST use stable identity through an appropriate `track` expression. Identity MUST represent the item rather than its position when order or membership can change.

## 53. Modern Template Control Flow

Angular 20 template control flow such as `@if`, `@for`, and `@switch` SHOULD be preferred for new templates where it improves clarity. Templates MUST remain declarative and MUST NOT contain hidden business calculations or unsafe function-heavy expressions.

Empty and alternate states SHOULD be represented explicitly. Template aliases and narrowing SHOULD reduce duplication without concealing state.

## 54. Deferrable Views

Angular deferrable views MAY defer noncritical UI when Angular 20 support, user experience, accessibility, loading behavior, and performance evidence justify them.

`@defer` MUST NOT delay critical content, security controls, primary task completion, or necessary error information. Triggers, fallback content, loading, error behavior, and server or rendering compatibility MUST be tested.

## 55. Internationalization Boundary

Angular implementation SHOULD keep text, layout, formatting, and content adaptable to future localization without claiming future Product scope. Current market, language, and Currency facts remain governed by PRODUCT.md.

This standard does not independently establish South Africa, English, ZAR, locale identifiers, translation tooling, multi-currency, or a localization delivery process.

## 56. Testing Baseline

`.ai/core/TESTING-STANDARDS.md` governs test depth, independence, evidence, and Exceptions. Angular changes MUST use the smallest effective combination of Unit Tests, Component Tests, Integration Tests, Contract Tests, End-to-End Tests, accessibility tests, and security tests appropriate to Risk.

This standard does not select a Unit Test runner, Component Test runner, mocking library, harness framework, or visual-regression service. Playwright remains the Approved end-to-end direction.

## 57. Unit and Component Testing

Tests SHOULD cover Components, Signals, computed state, forms, validation, inputs, outputs, user interaction, accessible rendering, routing interaction, loading, denial, empty, degraded, and error states.

Tests MUST assert public behavior rather than private fields or framework internals. They MUST be deterministic and MUST NOT rely on production credentials, external providers, arbitrary delay, or shared mutable state.

## 58. Integration and Contract Testing

Integration Tests SHOULD verify Router, HttpClient, interceptors, DI scope, API mapping, configuration, and browser behavior where isolated tests cannot provide confidence.

Contract Tests MUST verify frontend expectations against OpenAPI 3.1 and representative RFC 9457 responses. Mocks and fixtures MUST NOT drift from current Contracts or make invalid states appear supported.

## 59. Accessibility and End-to-End Testing

Accessibility testing MUST combine applicable automation with keyboard, focus, zoom, screen-reader, responsive, and state-announcement review proportionate to Risk. Automated checks do not prove WCAG conformance alone.

Playwright End-to-End Tests SHOULD cover selected critical journeys and cross-boundary behavior. They MUST NOT replace lower-level tests or invent a separate Product Contract.

## 60. Payment, Inventory, and Security Testing

Payment tests MUST cover redirect return without proof, pending and unknown outcomes, duplicate submission, Idempotency Key behavior, denial, reconciliation, and prohibited-data leakage.

Inventory tests MUST cover stale availability, concurrent or repeated interaction, Stock Reservation failure, Available-to-Sell refresh, and prevention of duplicate effects or misleading success.

Security tests MUST cover applicable XSS, unsafe HTML, external links, storage, token exposure, CSRF, CORS assumptions, Authorization denial, Sensitive Data, file handling, and dependency Risk.

## 61. Observability and Analytics

Frontend observability SHOULD capture safe errors, performance signals, navigation, dependency outcomes, and Correlation IDs where governed. Logs, Metrics, Traces, analytics events, and Audit Records remain distinct.

Telemetry MUST NOT expose credentials, full tokens, raw CVV, unnecessary PII, inaccessible data, or provider internals. High-cardinality identifiers MUST NOT become uncontrolled Metric dimensions.

Analytics MUST pass through the Architecture-owned Analytics Adapter, use an approved provider and purpose, respect Consent and privacy, and MUST NOT become Domain authority. No analytics provider is selected here.

## 62. Feature Flags and Configuration

Feature Flags MAY control governed rollout or operational fallback but MUST define owner, purpose, default, Environment behavior, evaluation location, telemetry, and removal plan. They MUST NOT replace Domain modeling or Authorization.

Frontend configuration MUST contain only public, non-secret values, be validated before use, and remain Environment-aware without changing business semantics unexpectedly. Bundled or runtime configuration is visible to the client and MUST be treated accordingly.

## 63. Environment, Build, and Dependencies

Builds MUST preserve TypeScript strictness, reproducibility, source revision, dependency integrity, and Environment-specific public configuration. Production behavior MUST NOT depend on a developer-only default or hidden local file.

Exact Angular package versions beyond the Angular 20 baseline and the package manager or build configuration remain unselected until repository dependency configuration establishes them.

Third-party dependencies and scripts MUST have justified need, ownership, license review, maintenance evidence, security review, bundle and runtime impact, and a removal path. A dependency MUST NOT acquire broad DOM, network, credential, or Sensitive Data access by convenience.

## 64. Upgrades and Deprecated APIs

Angular, TypeScript, RxJS, browser, and dependency upgrades MUST assess compatibility, migration guidance, deprecated APIs, security, generated output, tests, accessibility, performance, and deployment impact.

Deprecated or experimental APIs MUST NOT be adopted as a new baseline without evidence and applicable governance. Preview behavior MUST NOT be represented as stable merely because it exists in the framework distribution.

## 65. Decision and ADR Governance

Material changes to Angular version, rendering Architecture, hosting Architecture, cross-cutting state Architecture, security boundary, or other Architecture-owned baseline require ADR governance and synchronized Architecture updates.

Ordinary Components, routes, forms, stores, operator choices, and local implementation inside the approved baseline do not automatically require an ADR. A generic Decision Record cannot independently override Architecture.

## 66. Exception Governance

This standard creates no Angular-specific formal Exception. Applicable Security, Testing, Coding, Documentation, Design, and other governed Exceptions remain scoped to their owning standards; multiple Exceptions may be required when multiple standards are affected.

A runtime JavaScript exception, Angular error, failed request, or browser failure is not a governance Exception. A lower-level deviation MUST NOT waive a mandatory Security Requirement without an approved Security Exception.

## 67. Prohibited Practices

The following are PROHIBITED:

- Treating frontend state, UI visibility, guards, storage, cache, search, or analytics as Domain authority.
- Treating a Payment Redirect or client result as Payment proof.
- Logging or persisting raw CVV, Secrets, full tokens, Session secrets, or unnecessary Sensitive Data.
- Using floating-point arithmetic for authoritative Money calculations.
- Creating duplicate Payment, Order, or Stock effects through retry or repeated interaction.
- Introducing NgModule-first structure for ordinary new application code.
- Using `effect()` as the default derived-state mechanism.
- Unmanaged or nested subscriptions where composition is possible.
- Unsafe DOM injection or casual `bypassSecurityTrust*` use.
- Treating CORS, route guards, hidden controls, or disabled controls as Authorization.
- Putting Domain logic in interceptors, Components, templates, or generic services.
- Selecting an ungoverned state, UI, Identity, analytics, hosting, testing, or rendering technology.
- Inventing breakpoints, budgets, retry counts, timeouts, coverage targets, or Product behavior.
- Bypassing ADR governance for a material Architecture change.

## 68. Compliance and Review Evidence

Angular changes MUST identify applicable Requirements, Specifications, Contracts, source revision, dependencies, affected routes and Components, security and accessibility impact, test evidence, and reviewer as applicable.

Review MUST assess structure, ownership, state authority, Signals/RxJS semantics, forms, routing, API compatibility, security, Payment, Inventory, accessibility, performance, observability, failure, and deferred decisions proportionate to Risk.

## 69. Ownership Boundaries

ANGULAR.md owns Angular-specific implementation refinements. It does not own Product scope, Domain semantics, Design System values, accessibility policy, API Contracts, backend behavior, provider evidence, Azure topology, or business Requirements.

Companion standards govern only within their assigned scopes when their own metadata and substantive content make them normative. File or path existence alone does not establish authority, and this standard MUST NOT assume authority on behalf of another companion document.

## 70. Deferred Decisions

The following remain deliberately unresolved or unselected:

- Repository-wide state-management library, including NgRx and NgRx SignalStore.
- UI/component and styling libraries.
- Detailed Storybook implementation and configuration.
- SSR, prerendering, SSG, and rendering strategy.
- PWA and service-worker adoption.
- Frontend hosting platform.
- Unit Test and Component Test runner.
- Visual-regression tooling or service.
- Analytics provider.
- Identity Provider and Session/token storage strategy.
- Exact dependency and build versions beyond the Approved baseline.
- Detailed performance budgets, responsive breakpoints, and accessibility implementation rules governed by applicable Approved Requirements and by the relevant companion standards within their normative scopes.

Deferral is not permission for independent feature-level selection.

## 71. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`

## 72. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Angular implementation standard covering Angular 20, Standalone Components, Signals, RxJS, Typed Reactive Forms, frontend state, API integration, security, Payment, Inventory, accessibility boundaries, testing, observability, performance, governance, and implementation quality. |

## 73. Quality Requirements

This standard MUST remain subordinate, Angular-specific, modern, coherent, and testable. It MUST preserve frontend/backend separation, server authority, accessibility, security, API compatibility, Domain ownership, and explicit technology status.

Implementation guidance MUST remain compatible with Angular 20 and avoid legacy-first, speculative, experimental, or library-specific rules not established by governance.

## 74. Final Validation and Implementation Reliance Gate

Before approval, material revision, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version 0.1.0 Draft with `authoritative: false`;
2. Angular 20, strict TypeScript, Standalone Components, Signals, RxJS, Typed Reactive Forms, Router, OpenAPI 3.1, RFC 9457, WCAG 2.2 AA, Playwright, and Storybook match Approved Architecture;
3. no state, UI, Identity, analytics, hosting, unit-test, rendering, or other unselected technology is silently established;
4. frontend state remains non-authoritative and server-side Authentication, Authorization, Payment, Inventory, Price, Discount, and Order rules remain intact;
5. security, accessibility, API, eventing, Azure, Design System, and Product boundaries remain consistent with governing sources;
6. no unsupported framework API, Product behavior, numerical target, Decision type, or formal Exception type is invented;
7. headings are sequential and unique, sections are non-empty, Markdown tables are valid, and Related Documents exist without self-reference;
8. no unfinished-work marker, actual ellipsis, or fabricated path is present;
9. implementation and test evidence is practical and Risk-based; and
10. only `.ai/frontend/ANGULAR.md` changes for a scoped update.

Approval would make this a subordinate implementation standard within its scope. It would not make `authoritative: false` true or permit this document to override `.ai/core/`.
