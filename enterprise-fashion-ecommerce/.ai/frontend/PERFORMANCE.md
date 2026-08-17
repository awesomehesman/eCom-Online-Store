---
title: PERFORMANCE
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# Frontend Performance Implementation Standard

## 1. Purpose, Scope, and Authority

This document defines lower-level frontend performance implementation requirements for startup, loading, rendering, interaction responsiveness, network efficiency, JavaScript execution, Angular change detection, images and media, fonts, caching, API requests, data volume, collections, route loading, deferred content, background work, browser resources, observability, testing, regression detection, and accessibility trade-offs.

It is subordinate to the Decision Hierarchy and Approved Canonical Documents. `PRODUCT.md` owns user outcomes and explicit Product performance Requirements; `ARCHITECTURE.md` owns platform and rendering Architecture; `ANGULAR.md` owns Angular mechanics; `UI.md` owns UI behavior; `ACCESSIBILITY.md` owns accessibility implementation; `API.md` owns API Contract semantics; `AZURE.md` owns Azure implementation and operations; `TESTING-STANDARDS.md` owns verification governance; and `SECURITY-STANDARDS.md` owns security. Its `authoritative: false` metadata MUST remain false; approval does not make this document a core Source of Truth.

## 2. Performance Requirements

Performance Requirements MUST derive from Approved Canonical Documents, Approved Product Requirements, Approved Specifications, Accepted Architecture Decisions, or explicit Contracts where applicable. Evidence MUST identify the Requirement, Environment, journey, state, and measurement method it evaluates.

This standard defines how performance is measured and protected. It does not invent Product SLAs, SLOs, success thresholds, budgets, or capacity values.

## 3. Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | Angular 20 | Approved frontend framework baseline. |
| Established | Standalone Components | Approved ordinary Component model. |
| Established | Signals | Approved synchronous state primitive where appropriate. |
| Established | RxJS | Approved for HTTP, event streams, cancellation, and asynchronous composition. |
| Established | Angular Router and route-level lazy loading | Approved routing and feature-loading baseline. |
| Established | Angular `@defer` where appropriate | Approved conditional mechanism for noncritical content. |
| Established | Azure Front Door and approved edge direction | Architecture-owned edge direction; detailed policy remains governed elsewhere. |
| Established | Azure Blob Storage for media | Architecture-approved object-storage direction. |
| Established | OpenTelemetry-compatible instrumentation | Approved observability compatibility baseline. |
| Established | Application Insights, Azure Monitor, and Log Analytics | Approved Azure observability direction. |
| Established | Playwright | Approved End-to-End Test direction. |
| Established | Storybook | Approved Design System documentation direction. |
| Unselected | Lighthouse CI, WebPageTest, SpeedCurve, Calibre, Datadog RUM, New Relic Browser, Sentry Performance, and frontend performance SaaS | No repository evidence selects these tools or providers. |
| Unselected | Bundler analyzer tooling | No analyzer is selected. |
| Unselected | Image optimization service | No transformation service is selected. |
| Unselected | Additional CDN optimization vendor | No vendor beyond the Approved edge direction is selected. |
| Unselected | Service worker and PWA | No offline or installable-application baseline is selected. |
| Unselected | Exact caching implementation | Cache implementation remains Use Case-owned and governed. |
| Unselected | Unit Test and Component Test runner | No exact runner is selected. |
| Unresolved Architecture Decision | Frontend rendering strategy | SSR, prerendering, and related choices require Architecture governance. |
| Unresolved Architecture Decision | Frontend hosting platform | Architecture leaves the hosting service unresolved. |

Mention, availability, or common use of a technology does not select it. A material change to the Approved Architecture follows ADR governance.

## 4. Measurement Model

Performance evidence MAY cover startup and loading, route transitions, interaction latency, rendering work, network requests, JavaScript execution, memory, layout and reflow, image and media loading, long tasks, and resource contention.

Measurements MUST retain sufficient context to be reproducible and comparable without exposing Sensitive Data. This section establishes categories, not thresholds.

## 5. Real-User and Synthetic Measurement

Real-user monitoring, synthetic browser testing, local profiling, and CI checks provide different evidence and MUST not be treated as interchangeable. Production experience depends on actual Environments, users, content, devices, networks, dependencies, and release state.

Synthetic or local results MUST NOT be represented as proof of production performance. No real-user monitoring provider is selected.

## 6. Core Web Vitals

AGENTS.md establishes mandatory Core Web Vitals Requirements of LCP `< 2.5s`, INP `< 200ms`, and CLS `< 0.1`. This subordinate standard preserves those governed values without independently establishing or changing them. Evidence MUST use the governed metric definition and an explicit test context.

No additional Core Web Vitals value, percentile, device profile, or network profile is established here. Generic public guidance MUST NOT become a repository Requirement unless adopted through applicable governance.

## 7. Performance Budgets

Approved Requirements MAY establish budgets for JavaScript, CSS, images, route payloads, request counts, execution time, memory, or DOM size. Each budget MUST identify scope, measurement method, Environment, owner, and release consequence.

No budget value is established here.

## 8. Angular Startup

Major features MUST use route-level lazy loading where compatible with user experience, failure handling, and Approved Architecture. Noncritical feature code SHOULD NOT be imported eagerly without evidence.

Provider scope, bootstrap work, initialization, configuration loading, Authentication or Session bootstrap, and preloading MUST be deliberate, observable where material, and safe under failure. No repository-wide preload strategy is selected.

## 9. Standalone Components

Standalone Components remain the Approved ordinary Component model. Performance work MUST preserve clear ownership, dependency boundaries, and lazy-loading compatibility.

Optimization MUST NOT reintroduce NgModule-first Architecture or create oversized Components that obscure responsibilities.

## 10. Signals

Signals SHOULD support fine-grained synchronous state where ownership is explicit. Derived state SHOULD use `computed()` and avoid separately mutable copies, redundant recomputation, or expensive work without clear invalidation semantics.

Writes MUST remain controlled. Signals do not automatically make an application fast and MUST NOT change authoritative Domain ownership.

## 11. Effects

Effects MUST NOT perform expensive repeated work unnecessarily, create feedback loops, issue duplicate requests, hide background polling, or own derivation better expressed through `computed()`.

Effect optimization MUST preserve lifecycle, cleanup, cancellation, error, and ownership correctness.

## 12. RxJS

RxJS flows SHOULD avoid duplicate subscriptions, unnecessary request fan-out, lost cancellation, unbounded concurrency, uncontrolled polling, and unsafe retries. Operator selection MUST match stream lifetime, ownership, ordering, cancellation, and failure semantics.

Sharing or replay MAY be used only when its cache, lifetime, error, completion, and subscriber semantics are justified. `shareReplay` is not a repository-wide default.

## 13. Subscriptions

Duplicate subscriptions that repeat work, requests, side effects, or event handling MUST be prevented. Subscription ownership and termination MUST remain explicit.

Manual subscription rules remain governed by `ANGULAR.md` and `CODING-STANDARDS.md`.

## 14. Change Detection

Angular rendering SHOULD avoid unnecessary state churn, repeated expensive work, unstable identities, and needless application-wide updates. Rendering causes SHOULD remain understandable through narrow state ownership and explicit data flow.

Performance techniques MUST use supported Angular behavior and MUST NOT justify broken state ownership, stale UI, or bypassed correctness.

## 15. Template Expressions

Template expressions SHOULD remain simple, deterministic, and inexpensive. Repeated rendering paths SHOULD avoid unnecessary allocation, expensive function execution, hidden network activity, side effects, and unpredictable getters.

Methods and getters are not categorically prohibited; their cost, purity, call frequency, and context determine suitability.

## 16. Repeated Rendering

Repeated collections MUST use stable identity or track expressions appropriate to their Domain identity. Reordering, filtering, pagination, and incremental updates SHOULD preserve reusable rendering work where correct.

No collection-size threshold is established.

## 17. Large Collections

Large collections MAY require pagination, virtualization, incremental rendering, filtering, or server-side querying according to Requirements and evidence.

No virtual-scroll library or numerical trigger threshold is selected.

## 18. Tables

Table performance SHOULD use bounded data, Contract-aligned pagination, sorting and filtering, stable row identity, rendering of needed rows, and avoidance of excessive cell computation where applicable.

Accessibility semantics, responsive behavior, loading, empty, and error states MUST remain intact. No grid library is selected.

## 19. Routing and Lazy Loading

Route-level lazy loading is Established. Feature code splitting SHOULD align with Module boundaries and avoid transferring noncritical code before it is needed.

Preloading requires evidence. Route data, resolvers, guards, navigation cancellation, and stale navigation work MUST remain bounded and must preserve Authentication, Authorization, error, and accessibility behavior. No route budget is defined.

## 20. Angular `@defer`

`@defer` MAY load noncritical content when Product Requirements, user experience, accessibility, rendering compatibility, and measured evidence justify it. Triggers, placeholders, loading, error behavior, and recovery MUST be intentional.

Critical errors, security state, Authorization state, Payment status, essential Checkout actions, and essential accessibility content MUST NOT be deferred. No universal trigger is selected.

## 21. Data Fetching

Frontend data fetching SHOULD avoid duplicate requests, avoidable waterfalls, over-fetching, unnecessary refresh, and unbounded polling. Cancellation and stale-response handling MUST be applied where user actions or navigation supersede work.

Optimization MUST preserve API Contracts, Authorization, Domain boundaries, and failure semantics. Requests MUST NOT be combined merely for speed when doing so changes ownership or Contract meaning.

## 22. HTTP Caching

Frontend behavior MUST respect server, API, edge, and storage caching Contracts. Validators, invalidation, freshness, private responses, and failure handling MUST be honored where Contracted.

The frontend MUST NOT invent authoritative caching for Payment evidence, Authorization, or critical Inventory freshness where staleness is unsafe. No cache duration is established.

## 23. Application Caching

Frontend caches are non-authoritative Projections. Each cache requires defined ownership, invalidation or expiry, stale-state behavior, session and account boundaries, logout or reset handling, Sensitive Data controls, poisoning protections, and reconciliation where needed.

No frontend cache library or exact implementation is selected.

## 24. Browser Storage

Browser storage MUST NOT be used as a performance shortcut for Secrets, Refresh Tokens, Session secrets, prohibited Sensitive Data, Payment evidence, or authoritative Inventory state.

Any permitted storage MUST follow `ANGULAR.md`, `SECURITY-STANDARDS.md`, privacy Requirements, account boundaries, invalidation, and cleanup rules.

## 25. Search

Search results are non-authoritative Projections. Search interaction SHOULD support request cancellation, bounded request frequency, pagination, bounded result size, controlled highlighting cost, and honest stale-result handling.

No debounce duration, search engine, or client search library is established.

## 26. Form Performance

Typed Reactive Forms remain Established. Large or complex forms SHOULD avoid redundant validation, unnecessary `valueChanges` work, duplicated subscriptions, and stale asynchronous-validator results.

Optimization MUST preserve server validation, accessibility, entered data, error association, security, and correctness. Validation MUST NOT be removed merely to improve speed.

## 27. Image Performance

Azure Blob Storage remains the Approved media-storage direction. Images SHOULD use appropriate intrinsic dimensions, responsive sources, suitable formats, lazy loading where appropriate, decoding and loading hints where justified, stable aspect ratios, bounded payloads, accessible placeholders, and usable failure states.

Privacy, access control, Product accuracy, and media rights MUST remain intact. No CDN transformation or image-optimization service is selected.

## 28. Product Images

Performance optimization MUST NOT reduce Product-image accuracy, prevent essential inspection, fabricate imagery, or remove required alternatives. Product and Design System Requirements determine image purpose and presentation.

Alternative text and accessible media requirements remain governed by `ACCESSIBILITY.md`.

## 29. Fonts

Font performance MAY consider an Approved subset and loading strategy, variant count, fallback behavior, rendering stability, and failure behavior.

No font family, hosting model, preload rule, subset, or rendering policy is established here.

## 30. CSS

CSS with component-scoped styling remains the Established UI baseline. Implementations SHOULD avoid uncontrolled global CSS, unnecessarily complex selectors, duplicated styles, avoidable style recalculation, and inefficient stylesheet loading.

Critical and noncritical styling MAY be separated where governed and evidenced. SCSS, a CSS framework, and a CSS methodology remain unselected.

## 31. Layout and Reflow

Implementation SHOULD avoid unnecessary layout thrashing, repeated forced measurement-and-write cycles, unstable dimensions, and preventable layout shifts.

Optimization MUST preserve WCAG reflow, zoom, focus visibility, reading order, content, and interaction Requirements.

## 32. DOM Size

Unbounded DOM growth MUST be avoided. Pagination, incremental rendering, collapsed content, or virtualization MAY reduce rendered content where semantics, discoverability, focus, and accessibility remain correct.

No DOM-node limit is established.

## 33. Animation

Animation MUST NOT block interaction, delay critical information, obscure state, or weaken accessibility. Supported CSS and browser capabilities SHOULD be preferred where appropriate and measured.

Reduced-motion Requirements remain mandatory. No animation duration or library is selected.

## 34. Event Handlers

Implementation SHOULD avoid unnecessary global listeners, duplicate handlers, expensive synchronous handlers, and excessive scroll, pointer-move, or resize work.

Throttling or debouncing MAY be used only when interaction semantics, cleanup, responsiveness, and accessibility justify it. No duration is established.

## 35. Main Thread

Long-running synchronous work that blocks interaction SHOULD be reduced, scheduled, partitioned, or moved to an appropriate browser mechanism when evidence justifies it.

Moving work MUST preserve security, cancellation, correctness, and failure semantics. This standard does not establish Web Workers as a default.

## 36. Web Workers

Web Workers remain unselected. Adoption requires an applicable Use Case and governance proportionate to any material Architecture impact.

Workers MUST NOT bypass security, duplicate authoritative Domain logic, become a source of authority, or expose Sensitive Data.

## 37. Third-Party Scripts

Third-party scripts require explicit justification, security review, privacy and Consent review where applicable, performance assessment, loading ownership, failure isolation, and removal criteria.

No analytics, marketing, experimentation, or tag-management vendor is selected.

## 38. Analytics

Analytics MUST NOT materially degrade critical interactions or become authoritative Product or Domain evidence. Collection MUST remain provider-neutral, Consent-aware, privacy-aware, and bounded to approved purposes.

Analytics MUST NOT contain Secrets, raw CVV, or unnecessary PII. No analytics provider is selected.

## 39. Storybook

Storybook remains the Established Design System documentation direction. Performance-relevant Component states, content extremes, and examples MAY support investigation and review.

This standard does not select Storybook addons, hosting, profiling, or test tooling. `STORYBOOK.md` governs details within its applicable normative scope.

## 40. JavaScript Bundling

Bundled output SHOULD support code splitting, dead-code elimination where applicable, understandable side-effect boundaries, dependency deduplication, and tree-shaking compatibility where the selected build tooling supports it.

No bundler, analyzer, package manager, or bundle-size threshold is selected here.

## 41. Dependencies

New frontend dependencies SHOULD be evaluated for runtime cost, transfer and bundle impact, transitive dependencies, security, maintenance, accessibility, licensing, compatibility, and simpler alternatives.

Evidence MUST be proportionate to impact. No approval score or automatic package rejection threshold is established.

## 42. Source Maps

Source-map handling MUST balance diagnostics, release operations, intellectual-property exposure, and security. Public exposure MUST NOT occur where Security governance prohibits it.

No source-map generation, upload, retention, or hosting mechanism is selected.

## 43. Memory

Implementations MUST avoid leaked subscriptions, event listeners, timers, detached DOM, retained large state, stale caches, and inappropriate long-lived Customer or Session data in root-scoped services.

Memory evidence SHOULD identify lifecycle and ownership. No memory threshold is established.

## 44. Cleanup

Subscriptions, listeners, timers, observers, and temporary browser resources MUST be released when their owner ends or continued ownership is no longer valid.

In-flight requests SHOULD be cancelled when the client operation supports cancellation and continued work is no longer valid. Where cancellation cannot guarantee that remote processing stops, stale responses MUST be ignored or reconciled safely and the request MUST NOT be treated as having reversed any server-side effect.

Cleanup MUST NOT prematurely discard intentionally shared or cached state whose ownership and lifetime remain active.

## 45. Polling and Background Work

Polling and background work MUST be justified, bounded, observable where material, cancelable where supported, and stopped or paused when ownership ends or navigation, tab, Session, or account state makes it invalid.

No polling interval is established.

## 46. Retries

Retries MUST be bounded, visible in behavior or telemetry where material, idempotency-aware, and safe under duplicate execution and dependency failure. They MUST NOT amplify outages or hide terminal failure.

Unsafe state changes and financial operations MUST NOT be retried without explicit API Contract and idempotency support. No retry count or backoff value is established.

## 47. Timeouts

Timeout behavior MUST align with the applicable Contract, Product experience, cancellation, recovery, and uncertain-outcome semantics.

No timeout duration is established here.

## 48. Offline and Poor Network Conditions

The UI MAY represent interruption, retry, stale state, partial state, and recovery according to Approved Product behavior. It MUST not claim offline-first capability or silently introduce offline mutation.

Network uncertainty MUST NOT be converted into confirmed Payment, Inventory, Order, or Authorization state. PWA and offline behavior remain unselected.

## 49. Payment Performance

Performance optimization MUST NOT optimistically confirm Payment, cache Payment proof, suppress pending or unknown state, retry duplicate-sensitive Payment commands unsafely, or defer critical Payment errors and status.

Validated Payment Provider evidence processed by the trusted backend remains authoritative for provider-dependent Payment state. A Payment Redirect or client state is not proof.

## 50. Inventory Performance

Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement. Frontend caches and Projections MUST remain non-authoritative and communicate staleness or uncertainty honestly.

Optimization MUST NOT duplicate Stock Reservations, suppress required freshness, bypass concurrency protection, independently calculate authoritative Available-to-Sell, or permit Overselling.

## 51. Authorization Performance

UI Authorization predictions and cached Roles, Permissions, or Claims MAY guide UX only when their Session, refresh, revocation, and invalidation semantics are respected.

Server-side Authorization remains authoritative. No Identity Provider, protocol, token-storage strategy, or authorization cache is selected.

## 52. Security and Performance

Performance optimization MUST NOT weaken TLS, Authentication, Authorization, CSP where governed, sanitization, validation, Secret handling, Sensitive Data controls, safe logging, CSRF, CORS, or trusted-content boundaries.

Security Requirements and Security Exceptions remain governed by `SECURITY-STANDARDS.md`.

## 53. Accessibility and Performance

Performance optimization MUST NOT weaken semantic HTML, accessible names, focus, keyboard access, announcements, contrast, reflow, reduced motion, readable state, error feedback, or Human Review.

`ACCESSIBILITY.md` governs accessibility implementation within its Approved scope. Accessibility content MUST NOT be removed or deferred merely to improve a performance measurement.

## 54. Content Priority

Critical task content SHOULD receive appropriate loading and interaction priority according to Product Requirements and Specifications.

This standard does not decide what is critical or create Product priority.

## 55. Loading UX

Loading indicators SHOULD reflect real work and distinguish pending, complete, failed, stale, and inactive states where applicable. Artificial delay and focus theft SHOULD be avoided.

No minimum spinner, skeleton, or loading duration is established.

## 56. Skeletons

Skeletons MAY be used when they preserve layout, meaning, accessibility, and honest state. Excessive animation, announcement noise, layout mismatch, and false progress MUST be avoided.

No skeleton library or timing rule is selected.

## 57. Error Performance

Error handling MUST remain responsive enough to support recovery and MUST not strand or trap users. Required recovery information and accessible feedback MUST remain available.

Errors MUST NOT reveal technical internals, provider details, Secrets, credentials, or Sensitive Data.

## 58. Observability

Frontend performance evidence SHOULD remain compatible with OpenTelemetry-compatible instrumentation and the Approved Application Insights, Azure Monitor, and Log Analytics direction.

Implementation MAY capture safe navigation, dependency, resource, rendering, interaction, failure, and deployment context. No retention, sampling, provider configuration, or numerical threshold is established.

## 59. Performance Telemetry

Potential performance signals include route timing, resource loading, interaction timing, request latency, long-task evidence, errors, retry patterns, and cache behavior.

Signals MUST have defined purpose, bounded dimensions, privacy controls, and interpretation. No metric threshold is established.

## 60. Correlation

Correlation ID and trace context MAY connect frontend, API, backend, dependency, and deployment evidence where governed.

Correlation MUST NOT expose Secrets, tokens, Sensitive Data, raw CVV, or unnecessary PII.

## 61. High Cardinality

Customer IDs, Order IDs, Payment IDs, unbounded URLs, free-form errors, and other high-cardinality values MUST NOT become uncontrolled metric dimensions.

Diagnostic identifiers MAY appear only in appropriate protected Logs or Traces under Security, observability, and retention governance.

## 62. Real-User Monitoring

Real-user monitoring remains unselected. Adoption requires governed decisions for purpose, provider, privacy, Consent, Sensitive Data, sampling, retention, access, deletion, and operational ownership.

RUM data is performance evidence, not authoritative Product or Domain truth.

## 63. Synthetic Testing

Playwright remains the Established End-to-End Test direction and MAY support repeatable performance-regression evidence for selected journeys.

Synthetic results MUST NOT be represented as production truth. No synthetic performance platform, Environment profile, or threshold is selected.

## 64. Local Profiling

Browser developer tools MAY support diagnosis of network, JavaScript, memory, layout, rendering, and interaction behavior.

Manual local profiling is evidence, not a formal authority source or substitute for governed production and test evidence.

## 65. Performance Testing

`TESTING-STANDARDS.md` governs verification. Applicable tests SHOULD cover startup, route loading, rendering, interactions, large-data states, images, failure and retry, caching, and accessibility-performance interactions.

Tests MUST identify their Requirement, Environment, data, method, and limitations. No arbitrary target is established.

## 66. Regression Testing

Suspected performance regressions require reproducible evidence, comparison with an appropriate baseline, impact assessment, ownership, and review.

Release MUST NOT be blocked or approved solely against an invented threshold.

## 67. CI Performance Gates

No CI performance tool or gate is selected here. If a CI performance gate is introduced, it MUST derive thresholds and release consequences from Approved Requirements and remain repeatable, explainable, and auditable.

A blocking result MUST follow Testing and applicable Exception governance; it MUST NOT be bypassed informally.

## 68. Playwright

Playwright MAY capture performance-adjacent evidence where it is suitable for the tested journey, including loading, interaction, network, and failure behavior.

Playwright is not a complete performance-measurement suite and does not prove production performance.

## 69. Lighthouse

AGENTS.md establishes Lighthouse checking direction. This standard does not select Lighthouse CI, an integration, configuration, score, category weighting, or release threshold.

Lighthouse results MUST NOT be converted automatically into repository Requirements or represented as production truth.

## 70. Axe

Axe is part of the established accessibility-checking direction, not a performance-measurement selection.

Accessibility results MUST NOT be conflated with performance evidence, and no exact axe package or integration is selected here.

## 71. Storybook Performance

Storybook MAY support Component-level investigation of rendering, states, content extremes, and interaction behavior.

Storybook rendering is not production performance truth, and no Storybook performance addon or profiler is selected.

## 72. Environment Differences

Performance evidence MUST record relevant Environment differences. Local, CI, test, staging, and production conditions may differ in data, build output, caching, network, dependencies, capacity, telemetry, and security controls.

No exact scale equivalence is required unless an Approved Requirement establishes it.

## 73. Device and Network Profiles

Device and network profiles MAY be used only when Approved Requirements or an approved test methodology establish their purpose and interpretation.

No default device, browser, processor, viewport, bandwidth, latency, or network profile is selected.

## 74. Browser Support

Performance optimization MUST remain compatible with Approved browser-support Requirements and MUST not depend silently on unsupported behavior.

No browser matrix is established here.

## 75. Resource Hints

Preload, prefetch, preconnect, and DNS-prefetch MAY be used only where measured benefit, request ownership, security, privacy, cache behavior, and waste are understood.

Assets and origins MUST NOT be blanket-preloaded. No resource-hint policy is selected.

## 76. HTTP Compression

Frontend assets and requests MUST remain compatible with server and edge compression governed by Architecture and deployment configuration.

No compression algorithm, level, threshold, or ownership mechanism is selected here.

## 77. CDN and Edge

Azure Front Door and the Approved edge direction remain Architecture-owned. Frontend behavior MUST respect governed routing, caching, security, compression, and invalidation semantics.

This standard does not establish a CDN policy, select an additional vendor, or define cache values.

## 78. Blob Delivery

Azure Blob Storage remains the Approved media-storage direction. Delivery MUST preserve access control, privacy, media integrity, failure behavior, and applicable edge or Contract semantics.

No signed-URL syntax, container strategy, Cache-Control value, transformation rule, or media pipeline is established.

## 79. Cache Control

Frontend implementation MUST respect API, edge, and storage cache-control Contracts and distinguish public, private, stale, invalidated, and non-cacheable data where governed.

No TTL, stale window, validator policy, or cache directive is invented here.

## 80. Service Worker

PWA and service-worker adoption remain unselected. No offline cache, background synchronization, installability, update, or asset-versioning behavior may be introduced silently.

A material adoption follows applicable Architecture and ADR governance.

## 81. SSR and Prerendering

SSR, prerendering, SSG, and the broader frontend rendering strategy remain an unresolved Architecture Decision.

This standard does not establish a rendering mode, hydration strategy, server runtime, cache model, or deployment topology.

## 82. Frontend Hosting

The frontend hosting platform remains unresolved. This standard does not select Azure App Service, Azure Static Web Apps, Blob static hosting, an Azure Front Door origin, or another hosting product.

Selection requires Architecture governance and an ADR when material, followed by synchronized governing updates.

## 83. Third-Party Failure

Third-party loading or failure SHOULD NOT block critical flows unless Product and Architecture explicitly justify the dependency. Loading, timeout, isolation, fallback, degraded behavior, and recovery MUST be governed by the relevant Requirements and Contracts.

This standard does not invent fallback Product behavior or dependency priority.

## 84. Performance Incidents

Performance incidents SHOULD be investigated using telemetry, reproducible scenarios, appropriate comparison, dependency evidence, Environment context, and deployment correlation.

Incident handling follows existing operational and security governance. No performance-specific severity matrix is created.

## 85. Documentation

Performance Decisions, Requirements, evidence, baselines, regressions, known limitations, measurement methods, and remediation ownership MUST follow `DOCUMENTATION-STANDARDS.md`.

Documentation MUST distinguish observation, inference, target, and verified outcome.

## 86. Decision and ADR Governance

Material Architecture changes require an ADR and synchronized Architecture updates. Examples may include rendering strategy, frontend hosting, major caching Architecture, worker model, or delivery Architecture.

Ordinary optimization within Approved boundaries does not automatically require an ADR. Material non-Architecture Decisions follow the applicable Decision Record governance. This standard creates no performance-specific Decision identifier.

## 87. Exception Governance

This standard creates no `Performance Exception`. Formal deviations use every applicable Exception process owned by the affected Requirement or standard.

Performance pressure does not justify bypassing Security, Accessibility, Testing, Coding, Documentation, Product, or Architecture Requirements. Runtime slowdowns and tool findings are not governance Exceptions.

## 88. Prohibited Practices

The following are PROHIBITED:

- Invented performance budgets, Core Web Vitals targets, bundle sizes, timeouts, retry counts, cache TTLs, device profiles, network profiles, or capacity values.
- Blocking, removing, or weakening accessibility for speed.
- Weakening security, privacy, validation, Authorization, or correctness for speed.
- Optimistically confirming Payment or treating client state as Payment proof.
- Treating stale Inventory state as authoritative truth or duplicating Stock Reservations.
- Duplicate requests, duplicate subscriptions, unbounded polling, or hidden retry storms.
- Unbounded DOM growth, giant eager imports, and avoidable expensive template work.
- Uncontrolled third-party scripts, global event listeners, timers, or background work.
- Memory and resource leaks.
- Treating local or synthetic results as production truth.
- Selecting a tool, provider, framework, service, Architecture, or numerical target without applicable governance.

## 89. Ownership Boundaries

PERFORMANCE.md owns lower-level frontend performance implementation and evidence refinements. It does not own Product outcomes, Architecture, Angular mechanics, UI behavior, accessibility, API Contracts, Azure operations, security controls, test governance, or Storybook configuration.

`PRODUCT.md`, `ARCHITECTURE.md`, `ANGULAR.md`, `UI.md`, `ACCESSIBILITY.md`, `API.md`, `AZURE.md`, `SECURITY-STANDARDS.md`, `TESTING-STANDARDS.md`, and `STORYBOOK.md` retain their assigned scopes according to their own metadata and substantive content.

## 90. Deferred Decisions

The following remain unresolved or unselected:

- Any Core Web Vitals targets, percentiles, or measurement profiles beyond the values already established by AGENTS.md.
- Performance budgets and numerical success thresholds.
- Device and network profiles.
- Real-user monitoring provider and frontend performance SaaS.
- Synthetic performance platform and Lighthouse CI.
- Bundler analyzer tooling.
- Image optimization service.
- Web Worker adoption.
- Service worker and PWA adoption.
- SSR, prerendering, SSG, and frontend rendering strategy.
- Frontend hosting platform.
- Frontend cache implementation and cache durations.
- Exact browser and assistive-technology matrix.
- Unit Test and Component Test runner.
- CI performance gates and thresholds.

Deferral is not permission to select these independently or to weaken an applicable Requirement.

## 91. Related Documents

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
- `.ai/frontend/ANGULAR.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/STORYBOOK.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`

## 92. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the frontend performance implementation standard after final governance, Core Web Vitals, Angular rendering, loading, network efficiency, media, caching, Payment, Inventory, Authorization, accessibility, observability, testing, regression prevention, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial frontend performance implementation standard covering Angular rendering, loading, network efficiency, media, caching, Payment, Inventory, accessibility, observability, testing, regression prevention, and governance. |

## 93. Quality Requirements

This standard MUST remain evidence-driven, Requirement-driven, measurable, accessible, secure, Domain-safe, technology-neutral where unresolved, subordinate, and free of invented thresholds.

Guidance MUST remain practical, testable, maintainable, and aligned with Product outcomes, Architecture, Contracts, Environments, and Risk.

## 94. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. no Product SLA, SLO, performance target, Core Web Vitals value beyond the existing AGENTS.md Requirements, bundle size, request or latency threshold, retry count, timeout, cache TTL, device or network profile, infrastructure capacity, or unsupported numerical value is independently invented;
3. no RUM provider, synthetic performance platform, Lighthouse CI, bundler analyzer, image service, cache implementation, Unit Test runner, Component Test runner, browser matrix, or unapproved technology is selected;
4. Angular 20, Standalone Components, Signals, RxJS, route-level lazy loading, `@defer`, Azure edge and Blob Storage direction, observability, Playwright, and Storybook match Approved governance;
5. rendering strategy and frontend hosting remain unresolved Architecture Decisions;
6. Product, Architecture, Angular, UI, Accessibility, API, Azure, Security, Testing, Storybook, Payment, Inventory, and Authorization boundaries remain intact;
7. no new Decision type or formal Exception type is introduced;
8. headings remain sequential and unique, sections remain non-empty, tables remain valid, Related Documents exist without self-reference, and Revision History remains current;
9. no TODO, TBD, FIXME, actual ellipsis, placeholder, fabricated path, or unsupported commitment remains; and
10. only `.ai/frontend/PERFORMANCE.md` changes for a scoped update.

This Approved document is a subordinate frontend performance implementation standard within its scope. Approval does not make `authoritative: false` true or permit this document to override `.ai/core/`.
