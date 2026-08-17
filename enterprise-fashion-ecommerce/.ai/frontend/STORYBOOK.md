---
title: STORYBOOK
version: 1.0.0
status: Approved
owner: Product Design and Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# Storybook Implementation Standard

## 1. Purpose, Scope, and Authority

This document defines lower-level Storybook implementation requirements for documenting reusable UI Components, Approved Design System behavior, Component states and variants, responsive and accessibility-relevant states, loading, error and empty states, content extremes, interaction examples, design and engineering collaboration, Component verification, discovery, and implementation evidence.

It is subordinate to the Decision Hierarchy and Approved Canonical Documents. `DESIGN-SYSTEM.md` owns reusable visual and interaction guidance; `UI.md` owns UI behavior; `ANGULAR.md` owns Angular mechanics; `ACCESSIBILITY.md` owns accessibility implementation; `PERFORMANCE.md` owns frontend performance implementation; `TESTING-STANDARDS.md` owns verification governance; `PRODUCT.md` owns Product behavior; and `SECURITY-STANDARDS.md` owns security. Its `authoritative: false` metadata MUST remain false; approval does not make this document a core Source of Truth.

## 2. Storybook Status

Storybook is the Established Design System documentation direction. This document refines that Approved direction and MUST NOT reopen adoption or present Storybook as optional repository direction.

STORYBOOK.md does not select Storybook; it governs lower-level use within its assigned scope when its metadata and substantive content make it normative.

## 3. Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | Storybook | Approved Design System documentation direction. |
| Established | Angular 20 | Approved frontend framework baseline. |
| Established | Standalone Components | Approved ordinary Component model. |
| Established | Design System documentation direction | Architecture and Design System own the governing direction. |
| Established | WCAG 2.2 AA | Approved accessibility target, not a certification claim. |
| Established | Playwright direction | Approved End-to-End Test direction where relevant. |
| Unselected | Storybook accessibility addon and axe integration | No addon, package, or integration is selected. |
| Unselected | Storybook test runner and interaction-testing addon | No runner or interaction addon is selected. |
| Unselected | Storybook visual-regression service, Chromatic, Percy, and Applitools | No visual-regression tool or provider is selected. |
| Unselected | Storybook hosting platform, deployment mechanism, and publication model | No hosting or publication Architecture is selected. |
| Unselected | Storybook performance profiler and analytics | No profiler or analytics capability is selected. |
| Unselected | Storybook CI gate | No gate, workflow, or release threshold is selected. |
| Unselected | Exact Storybook builder, package versions, and package manager | Repository configuration does not establish them. |
| Unselected | Mandatory MDX authoring and snapshot tooling | No format mandate or snapshot tool is selected. |

Common ecosystem use, documentation mention, or file existence does not select a technology.

## 4. Storybook Ownership

Storybook documents Component implementation and Approved Design System behavior. It MUST NOT become authoritative for Product behavior, Domain state, API Contracts, Payment success, Inventory truth, Authorization, design tokens, colors, typography, Responsive Breakpoints, Product copy, or security policy.

When a Story conflicts with an Approved governing source or actual Contract, the governing source or Contract wins and the Story MUST be corrected.

## 5. Story Types and Categories

Useful categories MAY include foundational Design System primitives, reusable Components, composite Components, feedback and state Components, layout Components, data-display Components, form Components, navigation Components, and feature-specific reusable UI.

Categories SHOULD support discovery without creating a rigid repository taxonomy. Not every page, feature, or one-off composition belongs in Storybook.

## 6. Story Responsibility

A Story SHOULD represent one understandable Component state or scenario. It SHOULD be deterministic, reviewable, accessible, safe, understandable without hidden setup, and reusable where appropriate.

Stories are implementation documentation and evidence; they MUST NOT become hidden Product Specifications.

## 7. Story Coverage

Reusable Components SHOULD document applicable default, variant, interactive, disabled, loading, error, empty, partial, long-content, missing-optional-content, responsive, accessibility-relevant, permission-related visual, and destructive states.

Coverage MUST reflect real governed or implemented behavior. Variants MUST NOT be invented merely to fill Storybook.

## 8. Variants

Story variants MUST derive from Approved Design System guidance or actual Component behavior. Their meaning, state, and available interaction MUST remain consistent with the implementation.

Primary or secondary treatment, sizes, tones, states, and token combinations MUST NOT be invented without governing or implementation evidence.

## 9. Content Extremes

Stories SHOULD cover relevant long labels, names, prices or descriptions, missing optional content, large counts, validation errors, translated expansion where relevant, narrow and wide containers, and wrapped text.

Examples MUST test presentation without inventing Product facts, promises, or current-market scope.

## 10. Determinism

Stories MUST avoid uncontrolled dates, random values, live production APIs, production credentials, time-sensitive external dependencies, and uncontrolled network calls where possible.

Controlled examples, clocks, identifiers, and test data SHOULD make states reproducible. Determinism MUST NOT fabricate authoritative Domain truth.

## 11. Mocking and Data

Storybook MAY use controlled synthetic data. It MUST NOT contain production Secrets, tokens, raw CVV, Customer PII, provider credentials, or confidential production payloads.

Mock data MUST be clearly presentation-oriented and MUST NOT be represented as authoritative Product, Domain, seed, reference, or Contract data.

## 12. API Boundary

Ordinary Component documentation SHOULD NOT require live backend APIs. API-shaped data SHOULD use safe controlled examples and preserve Contracted DTO shape where relevant.

Stories MUST NOT invent Endpoint or Contract semantics. No mocking library, service worker, interceptor package, or network-simulation technology is selected.

## 13. Payment Stories

Stories MAY demonstrate idle, entering-details, submitting, pending, unknown, failed, and confirmed-display states. Mock confirmation is presentation-only and MUST NOT establish Payment success logic.

A Payment Redirect, browser state, query parameter, local state, or Story control is not Payment proof. In the real system, provider-dependent Payment state requires validated Payment Provider evidence processed through the trusted backend.

Raw CVV MUST NOT appear in Story source, Args, Controls, Logs, screenshots, fixtures, telemetry, test output, or durable artifacts.

## 14. Inventory Stories

Stories MAY demonstrate available, low-availability, unavailable, Stock Reservation pending, Stock Reservation failed, stale, and unknown UI states.

Storybook is not authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, or Stock Movement. Mock quantities are presentation examples and MUST NOT establish Inventory rules or permit Overselling assumptions.

## 15. Authorization Stories

Stories MAY demonstrate permitted, denied, hidden, disabled, Staff User, and Customer UX states where safe. Permission context and simulated assumptions SHOULD be explicit.

Story visibility is not Authorization evidence. Server-side Authorization remains authoritative, and inaccessible Resource details MUST NOT be embedded unnecessarily.

## 16. Form Stories

Form Components SHOULD document applicable pristine, touched, invalid, server-error, asynchronous-validation, submitting, disabled, read-only, and success presentation states.

Examples MUST align with `ANGULAR.md`, `UI.md`, and `ACCESSIBILITY.md` and MUST NOT redefine trusted API or Domain validation.

## 17. Navigation Components

Reusable tabs, breadcrumbs, pagination, menus, links, and navigation controls SHOULD be documented where useful, including keyboard, current-state, disabled, and responsive behavior.

Stories MUST NOT invent Product information Architecture, route hierarchy, or Authorization policy.

## 18. Table and Data-Display Stories

Data-display Components SHOULD cover applicable populated, empty, loading, error, partial, sorting, filtering, row-action, long-content, and responsive-overflow states.

Headers, relationships, keyboard behavior, focus, and accessible status MUST remain valid. No data-grid library is selected.

## 19. Dialog and Overlay Stories

Dialog and overlay Components SHOULD cover initial, open, destructive, error, long-content, and responsive states where applicable.

Modal and non-modal semantics, focus, dismissal, background interaction, and focus restoration MUST follow `ACCESSIBILITY.md`. No dialog, modal, portal, or overlay library is selected.

## 20. Feedback Components

Stories MAY document banners, alerts, toasts, inline feedback, errors, success states, and warnings using governed semantics.

Critical information MUST NOT become toast-only in actual UI. No display duration, timer, notification library, or Product behavior is selected.

## 21. Accessibility

`ACCESSIBILITY.md` remains governing. Stories SHOULD support review of semantic structure, accessible names, keyboard behavior, focus, error states, contrast-dependent states, reduced motion, responsive behavior, and content extremes.

Storybook, an addon, or an automated scan does not prove WCAG 2.2 AA conformance.

## 22. Accessibility Addons

No Storybook accessibility addon, `@storybook/addon-a11y`, axe integration, accessibility panel, or automated scan plugin is selected.

If adopted through applicable tooling governance, automation remains supplemental evidence and MUST NOT replace Human Review or governed accessibility verification.

## 23. Keyboard and Focus

Interactive Stories SHOULD permit realistic keyboard operation and focus behavior. Story wrappers, decorators, and Controls MUST NOT introduce fake controls, focus traps, hidden focus, or interaction that conceals a real Component defect.

Storybook-specific framing MUST be distinguished from Component behavior during review.

## 24. Responsive Stories

Stories SHOULD demonstrate representative responsive behavior using Approved Design System and UI guidance. Content, controls, state, errors, dialogs, and accessibility MUST remain usable.

No Responsive Breakpoint, viewport value, device profile, or device matrix is established.

## 25. Viewport Tooling

No viewport addon, configuration, or exact preset is selected. Responsive review MAY use controlled containers or browser sizing where the review context is documented.

Viewport examples MUST NOT be represented as the complete supported-browser or device Contract.

## 26. Design Tokens

Storybook MAY display or demonstrate Approved Design Tokens. It MUST consume governed values and MUST NOT create or own them.

Colors, spacing, radius, typography, elevation, motion, Responsive Breakpoints, z-index, and icon sets MUST NOT be invented here.

## 27. Typography, Color, and Branding

Storybook consumes Approved Design System and Product values. It MUST NOT define a font family, palette, type scale, logo, brand asset, marketing identity, or visual policy.

Examples SHOULD expose relevant content and state without presenting sample values as new design authority.

## 28. Controls

Story Controls MAY support safe exploration of actual public Component inputs. They MUST NOT imply that arbitrary values are Approved variants, expose Secrets or production identifiers, permit raw CVV, or present impossible Product states without clear labeling.

Control ranges and options SHOULD derive from actual Component APIs and governed constraints.

## 29. Args

Args SHOULD map to actual public Component APIs and SHOULD keep state setup understandable.

Story-only APIs that misrepresent, bypass, or replace real Component behavior are PROHIBITED.

## 30. Decorators

Decorators MAY provide bounded layout, governed theme, router, form, or safe provider context. They SHOULD remain minimal and explicit.

Significant application or Domain logic MUST NOT be hidden inside decorators. No decorator library is selected.

## 31. Providers

Story providers MUST be minimal, controlled, and appropriate to the Component boundary. They MUST NOT boot the whole application unnecessarily, use production credentials, depend on live provider systems, or bypass actual Component boundaries.

Provider setup SHOULD make dependencies and assumptions reviewable.

## 32. Routing Context

Routing Components MAY receive controlled Angular Router context where needed. Links, parameters, denial, loading, and failure presentation SHOULD match actual Component behavior.

Stories MUST NOT invent Product route hierarchy, deep-link policy, or Authorization rules.

## 33. Theme Context

Storybook MUST NOT establish theming or dark mode where governance has not selected it.

If Approved theme definitions exist, Stories MAY consume them without redefining token or theme ownership.

## 34. Internationalization

Storybook does not select translation tooling, locale infrastructure, or future Product scope. Stories MAY demonstrate long or expanded content when useful for resilient UI review.

Current language, market, Currency, and internationalization behavior remain Product-governed.

## 35. Icons and Media

Storybook MAY document Approved icon and media usage with safe assets that respect rights, privacy, accessible alternatives, and Product accuracy.

No icon library, image service, transformation provider, or media-storage policy is selected.

## 36. Performance

`PERFORMANCE.md` remains governing. Storybook MAY help inspect rendering behavior, content extremes, interaction, and heavy states.

Storybook rendering MUST NOT be represented as production performance truth. No performance addon, profiler, budget, or threshold is selected.

## 37. Bundle and Build

This standard does not select a Storybook builder, bundler, package manager, exact package version, build mode, optimization, or output format.

Build choices MUST remain compatible with Angular and repository dependency governance.

## 38. Story Authoring Format

No mandatory MDX, CSF variant, documentation syntax, or alternate authoring format is selected.

Stories SHOULD remain maintainable and compatible with the eventual selected Storybook and Angular tooling.

## 39. Story Naming

Story names SHOULD clearly identify the Component and scenario, use canonical repository terminology, and avoid marketing ambiguity.

No mandatory naming taxonomy or identifier scheme is established.

## 40. Folder and Title Organization

Story organization SHOULD support discovery, related-state comparison, and maintainability.

No rigid folder hierarchy, title prefix, or alternate repository structure is established.

## 41. Documentation

Storybook documentation MAY describe purpose, usage, states, constraints, accessibility notes, content guidance, and known limitations.

It MUST link to governing documentation where material and MUST NOT duplicate or replace full authority.

## 42. Design and Engineering Handoff

Storybook MAY support design and engineering collaboration, implementation review, and shared inspection of Components.

It MUST NOT replace Product Requirements, Design System Decisions, ADRs, Specifications, Contracts, Human Approval Gates, or other approval processes.

## 43. Story Review

Review SHOULD assess correct state representation, Design System alignment, accessibility, safe data, useful scenarios, real Component APIs, Product and Domain consistency, and authority drift.

Review evidence MUST NOT claim checks or approvals that did not occur.

## 44. Story Maintenance

Stories MUST evolve with reusable Component behavior. Stale, broken, unsafe, or misleading Stories MUST be updated or removed when they no longer represent the implementation.

History belongs in repository version control and governed documentation, not knowingly incorrect active Stories.

## 45. Component Lifecycle

Deprecated Components SHOULD be identified clearly when repository or Component lifecycle governance supports deprecation.

This standard does not create a deprecation state, duration, compatibility period, or removal policy.

## 46. Story Lifecycle

Story lifecycle follows the applicable Component, documentation, and repository lifecycle. Stories SHOULD be introduced, reviewed, changed, or removed with the behavior they document.

No Storybook-specific approval status or lifecycle identifier is created.

## 47. Visual Regression

No visual-regression tool, service, workflow, or baseline policy is selected. Chromatic, Percy, Applitools, Loki, and equivalent tools remain unselected.

If adopted, baseline ownership, approval, diff review, retention, access, false-positive handling, security, and privacy MUST be governed before release reliance.

## 48. Screenshots

Screenshots MAY support review or evidence but MUST NOT contain Secrets, raw CVV, production tokens, unnecessary PII, provider credentials, or confidential production data.

Screenshots are not canonical UI Specifications, Design System authority, or sufficient behavioral verification.

## 49. Interaction Testing

Storybook interaction testing remains unselected. Play functions, a Storybook test runner, an interaction addon, and an assertion library are not mandatory or selected here.

Stories MAY remain interaction-ready without claiming test execution or selecting tooling.

## 50. Playwright

Playwright remains the Approved End-to-End Test direction. Storybook does not replace Playwright for critical system journeys, cross-boundary behavior, or trusted integration evidence.

Playwright MAY consume Storybook or isolated Component contexts only through applicable testing and tooling governance.

## 51. Unit and Component Testing

No exact Unit Test or Component Test runner is selected. A Story is not automatically a Unit Test or Component Test and MUST NOT be counted as executed verification without evidence.

`TESTING-STANDARDS.md` remains governing.

## 52. Snapshot Testing

No snapshot-testing framework, serializer, approval policy, or baseline process is selected.

Serialized Story output is not sufficient behavioral, accessibility, visual, or Contract verification.

## 53. Accessibility Testing

Storybook MAY support accessibility review, but automation remains supplemental. No addon, package, configuration, scan, or CI integration is selected.

Human Review remains required where automation is insufficient, according to `ACCESSIBILITY.md` and test governance.

## 54. Performance Testing

Storybook rendering is not production performance evidence. Performance gates MUST NOT be created from Storybook output unless Approved Requirements and applicable tooling governance establish the method and consequence.

No performance gate, profiler, or numerical threshold is selected.

## 55. Security

Storybook source, builds, artifacts, screenshots, Controls, Logs, telemetry, and publication MUST NOT expose Secrets, credentials, production tokens, private keys, raw CVV, sensitive provider payloads, or unnecessary PII.

`SECURITY-STANDARDS.md` remains governing.

## 56. Authentication

Ordinary Component documentation MUST NOT require production Authentication. Controlled simulated state MAY support presentation review without becoming Authentication evidence.

No Identity Provider, protocol, Session strategy, token-storage strategy, or Authentication service is selected.

## 57. Authorization

Stories MAY simulate Authorization-related UX states but do not prove access. Hidden, shown, enabled, disabled, Role, Permission, or Claims-based presentation remains non-authoritative.

Server-side Authorization remains authoritative.

## 58. Networking

Ordinary Stories SHOULD NOT require uncontrolled live network dependencies. Where network-shaped state is needed, controlled examples SHOULD expose loading, failure, retry, stale, and recovery behavior safely.

No Storybook network-mocking technology, proxy, interceptor, or provider is selected.

## 59. API Contracts

Stories MAY demonstrate DTO-shaped presentation data and API-related UI states. They MUST preserve applicable field meaning without becoming a Contract definition.

`API.md`, OpenAPI, and governed Specifications remain authoritative for API Contracts.

## 60. Events

Stories MAY display UI states derived from event-driven behavior. They MUST NOT invent Domain Event or Integration Event Contracts, imply exactly-once delivery, or become an eventing authority.

`EVENTS.md` remains governing.

## 61. Azure and Hosting

`ARCHITECTURE.md` and `AZURE.md` govern hosting, infrastructure, edge, and deployment. Storybook hosting and deployment remain unselected.

This standard does not select Azure Static Web Apps, Blob hosting, App Service, an Azure Front Door origin, or third-party Storybook hosting.

## 62. CI/CD

No Storybook CI gate, build workflow, publication workflow, or deployment mechanism is selected.

If introduced, build, review, artifact, access, deployment, failure, retention, security, and ownership Requirements MUST be governed before reliance.

## 63. Publication

Storybook MUST NOT be assumed publicly accessible. Any publication model requires an explicit governed decision covering intended audience and visibility; Authentication and Authorization where required by that model; confidential-Component and internal-administration protection; Sensitive Data controls; security review; and Environment ownership.

No publication model or public URL is selected.

## 64. Internal and External Visibility

Storybook visibility is an Architecture and Security Decision. Internal administration Components and confidential workflows MUST NOT be exposed publicly without applicable approval and controls.

File existence or a successful build does not authorize publication.

## 65. Versioning

This standard does not establish Storybook-specific semantic versioning, compatibility duration, or release numbering.

Story documentation follows applicable repository, Component, Contract, and documentation governance.

## 66. Source Control

Story source belongs with applicable implementation or documentation according to the repository structure and owning Component.

No alternate Story repository, generated source authority, or external source-control location is selected.

## 67. Story Data Ownership

Example data belongs only to Story and documentation context. It MUST NOT become authoritative Product seed data, test-fixture authority, Domain reference data, or production configuration.

Mocks and examples MUST remain traceable to the presentation scenario they support.

## 68. Sensitive Data

Story data MUST be synthetic and minimized. It MUST NOT contain unnecessary real names or addresses, actual Customer data, production Order or Payment identifiers, raw CVV, tokens, Secrets, private keys, or provider credentials.

Examples SHOULD make fictional status clear without using misleading placeholders as Domain truth.

## 69. Customer and Administration Context

Stories MAY represent Customer-facing and administration Components. The intended Principal, audience, and permission context SHOULD be understandable where it affects presentation.

Administration Stories MUST NOT imply that UI state is Authorization or reveal inaccessible Resource details.

## 70. Payment Context

Mock Payment states MUST be unmistakably presentation-only and MUST preserve pending, unknown, failed, and confirmed-display distinctions.

Live Payment Provider SDK Secrets, production provider data, provider payloads, and real financial information are PROHIBITED.

## 71. Inventory Context

Mock Inventory states remain non-authoritative Projections. Example quantities MUST NOT become Domain rules, Available-to-Sell calculations, or Stock Reservation behavior.

Stories SHOULD represent stale or unknown Inventory honestly where the Component supports those states.

## 72. Error and Degraded States

Stories SHOULD cover applicable validation errors, dependency failure, stale data, partial data, denied state, loading failure, and unknown state.

Error examples MUST remain safe and understandable and MUST NOT invent Product recovery flows or expose provider internals.

## 73. Empty States

Stories MAY demonstrate true empty, filtered empty, search empty, unavailable, and permission-limited states where implemented.

Examples MUST distinguish state accurately and MUST NOT leak inaccessible Resource existence.

## 74. Loading States

Stories MAY demonstrate initial loading, local loading, delayed dependency, retrying, and pending operations where implemented.

No delay, timeout, animation duration, or minimum loading period is established.

## 75. Content Safety

Story text MUST NOT leak provider internals or security details, introduce unsupported legal copy, invent Product promises, or establish policy language.

Product content and legal Requirements remain owned by their governing sources.

## 76. Accessibility Notes

Story documentation MAY record keyboard behavior, accessible-name expectations, focus behavior, error semantics, live-region behavior, and known accessibility constraints.

These notes refine implementation evidence and MUST NOT replace or weaken `ACCESSIBILITY.md`.

## 77. Performance Notes

Stories MAY record governed performance-relevant usage constraints, rendering considerations, or known high-cost states.

No numerical threshold, budget, production claim, or performance authority is created.

## 78. Known Limitations

Known limitations MUST be explicit, owned, and traceable where material. They MUST distinguish observed implementation limitations from Approved Requirements.

A Storybook note cannot silently waive a Requirement or substitute for a formal Exception.

## 79. Decision and ADR Governance

Material Architecture changes involving Storybook hosting, deployment, build Architecture, publication, or security boundaries require applicable Architecture governance, an ADR where material, and synchronized governing updates.

Ordinary Story authoring and documentation changes do not automatically require an ADR. Material non-Architecture Decisions follow applicable Decision Record governance. No Storybook-specific Decision identifier is created.

## 80. Exception Governance

This standard creates no `Storybook Exception`. Formal deviations use every applicable Exception process owned by the affected Requirement or standard.

A Story limitation, runtime error, failed build, visual difference, or tool finding is not automatically a governance Exception.

## 81. Prohibited Practices

The following are PROHIBITED:

- Treating Storybook as Product, Design System, Domain, security, or Contract authority.
- Treating Stories as API Contracts or Authorization evidence.
- Treating mock Payment success as real Payment logic or provider proof.
- Treating mock Inventory, Stock, Stock Reservation, or Available-to-Sell as authoritative truth.
- Including raw CVV, production Secrets, tokens, credentials, private keys, provider payloads, or unnecessary PII.
- Inventing Design Tokens, visual values, Product copy, Component variants, Product behavior, or Domain rules.
- Selecting addons, tools, services, providers, libraries, builders, or hosting without governance.
- Claiming WCAG conformance from Storybook or automated output.
- Treating Storybook as production performance truth.
- Depending on uncontrolled live production APIs.
- Publishing Storybook without Architecture and Security governance.
- Creating Story-only Component APIs that misrepresent production behavior.
- Retaining stale, broken, unsafe, or misleading Stories.
- Treating screenshots as canonical design or behavioral authority.

## 82. Ownership Boundaries

STORYBOOK.md owns lower-level Story authoring, example-data, documentation, review, and maintenance refinements. It does not own Product behavior, Design System values, Architecture, Angular mechanics, UI behavior, accessibility, performance, security, testing, API Contracts, events, Azure, hosting, or deployment.

`PRODUCT.md`, `DESIGN-SYSTEM.md`, `ARCHITECTURE.md`, `ANGULAR.md`, `UI.md`, `ACCESSIBILITY.md`, `PERFORMANCE.md`, `SECURITY-STANDARDS.md`, `TESTING-STANDARDS.md`, `API.md`, `EVENTS.md`, and `AZURE.md` retain their assigned scopes.

## 83. Deferred Decisions

The following remain unresolved or unselected:

- Exact Storybook version, builder, package manager, and build mode.
- Hosting, deployment mechanism, publication model, visibility model, and CI gate.
- Storybook accessibility addon and axe integration.
- Interaction testing, play-function policy, Storybook test runner, and assertion library.
- Visual-regression service, Chromatic, Percy, Applitools, Loki, and baseline workflow.
- Storybook performance tooling and analytics.
- Mandatory MDX or another authoring format.
- Snapshot tooling and screenshot-baseline process.
- Viewport addon and exact presets.
- Network-mocking library or provider.

Deferral is not permission to select these independently or weaken an applicable Requirement.

## 84. Related Documents

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
- `.ai/frontend/PERFORMANCE.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`

## 85. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the Storybook implementation standard after final governance, Design System, Component documentation, deterministic examples, Payment, Inventory, Authorization, accessibility, responsive behavior, security, testing boundaries, publication, lifecycle, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Storybook implementation standard covering Design System documentation, reusable Component states, accessibility, responsive behavior, Payment, Inventory, security, testing boundaries, review, lifecycle, and governance. |

## 86. Quality Requirements

This standard MUST remain Design-System-aligned, Product-safe, accessible, secure, deterministic, maintainable, testable, subordinate, technology-neutral where unresolved, and free of invented design values and Product semantics.

Guidance MUST remain practical for design and engineering collaboration and traceable to actual Component behavior and governing sources.

## 87. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. Storybook remains Established without this document reopening or independently owning adoption;
3. no exact Storybook version, builder, package manager, hosting, deployment, publication, CI gate, accessibility addon, axe integration, interaction tool, test runner, visual-regression service, performance profiler, analytics, MDX mandate, snapshot tool, viewport preset, or network-mocking library is selected;
4. Product, Design System, Architecture, Angular, UI, Accessibility, Performance, Security, Testing, API, Events, Azure, Payment, Inventory, and Authorization boundaries remain intact;
5. WCAG 2.2 AA and Playwright remain accurately classified without a certification or test-completeness claim;
6. no Design Token, color, typography, Responsive Breakpoint, icon set, Product behavior, API Contract, Domain rule, security policy, hosting Architecture, or unsupported technology is invented;
7. no Storybook-specific Decision identifier, approval status, or formal Exception type is introduced;
8. headings remain sequential and unique, sections remain non-empty, tables remain valid, Related Documents exist without self-reference, and Revision History remains current;
9. no TODO, TBD, FIXME, actual ellipsis, placeholder, fabricated path, or unsupported commitment remains; and
10. only `.ai/frontend/STORYBOOK.md` changes for a scoped update.

This Approved document is a subordinate Storybook implementation standard within its scope. Approval does not make `authoritative: false` true or permit this document to override `.ai/core/`.
