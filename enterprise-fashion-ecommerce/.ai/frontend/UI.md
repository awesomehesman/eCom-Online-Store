---
title: UI
version: 1.0.0
status: Approved
owner: Product Design and Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# UI Implementation Standard

## 1. Purpose

This document defines the repository's lower-level web UI implementation standard. It translates Approved Product, Design System, Architecture, Angular, Security, and Testing guidance into consistent implementation practices without creating new Product behavior, visual identity, or Architecture authority.

This standard covers UI composition, styling ownership, responsive behavior, interaction and feedback states, reusable and feature component implementation, content presentation, forms, navigation, loading, error, empty and degraded states, media presentation, Storefront and administration consistency, and Design System consumption.

It does not define Product semantics, business workflows, exact visual values, an accessibility policy, performance budgets, provider behavior, or API Contracts.

This standard is subordinate to the Decision Hierarchy in `.ai/core/AGENTS.md` and to Approved Canonical Documents within their scopes. Its `authoritative: false` metadata MUST remain false; approval does not make this document a core Source of Truth.

`PRODUCT.md` owns Product behavior; `DESIGN-SYSTEM.md` owns reusable visual, interaction, accessibility, token, component, and content guidance; `ARCHITECTURE.md` owns frontend structure; and `ANGULAR.md` owns Angular implementation. Security and Testing remain governed by their core standards. Companion standards govern only within their assigned scopes when their own metadata and substantive content make them normative.

**MUST**, **MUST NOT**, **REQUIRED**, and **PROHIBITED** are mandatory. **SHOULD** and **SHOULD NOT** are strong defaults requiring documented rationale to depart from. **MAY** identifies a permitted option, not a selected technology or Product commitment.

## 2. Design System Authority

UI implementation MUST consume Approved Design System decisions rather than reproduce or override them. Reusable patterns MUST use established Components and tokens before feature code creates local alternatives.

Exact colors, typography, spacing values, Responsive Breakpoints, icons, logos, brand assets, Component inventory, motion values, and visual assets remain deferred until an Approved design source or governing Decision establishes them.

## 3. Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | Angular 20 and Standalone Components | Approved frontend implementation baseline. |
| Established | Storybook | Approved Design System documentation direction; configuration remains companion-owned. |
| Established | Playwright | Approved End-to-End Test direction; exact configuration remains implementation-owned. |
| Established | WCAG 2.2 AA | Approved accessibility target, not a certification claim. |
| Established | CSS with component-scoped styling | Approved styling baseline; global CSS is limited to genuinely global concerns. |
| Unselected | SCSS preprocessor | Coding Standards apply if selected, but no Approved governance or repository configuration currently selects SCSS. |
| Unselected | Angular Material, Tailwind, and Bootstrap | No UI or styling library is selected. |
| Unselected | CSS-in-JS and utility CSS framework | No such styling model is selected. |
| Unselected | Component Library and icon library | No implementation library or icon set is selected. |
| Unselected | Design-token source and exact token set | Values require an Approved design source or governing Decision. |
| Unselected | Responsive Breakpoints, font family, and brand color palette | Exact visual values are not established. |
| Unselected | Animation library and visual-regression tool | No library, runner, or service is selected. |
| Unresolved Architecture Decision | Frontend rendering strategy | SSR, prerendering, and related strategy require Architecture governance. |
| Unselected | PWA and service worker | No offline or installable-application baseline is selected. |
| Unselected | Analytics provider | Analytics remains Adapter-bound and provider-neutral. |

File existence or an illustrative reference does not establish a technology selection.

## 4. Styling Architecture

Styles MUST have a clear owner. Component-scoped styles are the default; shared tokens and stable global foundations MAY be global when their scope is genuinely application-wide. Feature styling MUST NOT leak into unrelated Components.

Specificity, cascade order, inheritance, state selectors, theme boundaries, and overrides MUST remain understandable. Uncontrolled global CSS, arbitrary escalation, and reliance on DOM accidents are PROHIBITED. Utility styles, preprocessors, or naming systems MAY be adopted only when governed and consistently configured.

Print styles MAY be added only for an Approved Product Use Case.

## 5. Design Tokens

Approved tokens SHOULD represent recurring color, typography, spacing, radius, border, elevation, motion, sizing, stacking, and responsive concerns. Implementation MUST consume the canonical source once established and MUST NOT invent competing token names or values.

Hard-coded values SHOULD be minimized where a reusable Approved token exists. A local value requires narrow ownership and MUST NOT silently become a repository convention.

## 6. Component Responsibilities

Reusable Components own stable presentation and interaction patterns. Feature Components own feature composition; layout Components own arrangement; feedback Components own state communication; and data-display Components own understandable presentation of Contracted data.

Component APIs MUST be explicit, semantic, typed, and appropriately small. Reusable Components MUST NOT hide Product, Domain, Payment, Inventory, Authorization, or provider rules.

## 7. Component Variants

Variants MUST represent an Approved semantic or visual distinction, define supported states and combinations, and remain testable and accessible. Variant names and behavior belong to the Design System or the owning Approved design source.

Multiple boolean inputs MUST NOT create an accidental, contradictory variant system. Unsupported combinations MUST be prevented or handled explicitly.

## 8. Composition

Composition SHOULD be preferred over a monolithic configurable Component when it keeps responsibilities, content, state, and testing clearer. Content projection MAY be used where ownership and accessible structure remain explicit.

Composition MUST NOT create hidden coupling, inaccessible reading order, or a custom component framework that competes with Angular and the Design System.

## 9. Layout

Layouts MUST preserve content hierarchy, task flow, readable width, spacing consistency, overflow safety, and stable ownership. Fixed and sticky regions require evidence that they do not obscure content, controls, errors, or focus.

Scroll containers MUST be intentional. Nested scrolling, clipped focus, hidden overflow, and viewport assumptions MUST be avoided. Exact dimensions and container values remain governed by Approved design evidence.

## 10. Responsive Design

Responsive behavior MUST preserve task completion, content priority, reading and focus order, keyboard use, touch usability, error visibility, and critical actions. Reflow MUST not remove necessary context or make authoritative state ambiguous.

Exact Responsive Breakpoints remain deferred. This standard selects neither a mobile-first nor desktop-first ideology; implementation MUST follow Approved design evidence and content needs.

## 11. Storefront and Administration Boundary

Storefront and administration experiences MAY share Design System primitives but MUST preserve their different actors, Permissions, information density, workflow Risk, navigation, and operational needs.

Shared UI MUST NOT duplicate Product semantics. Administration route visibility, hidden actions, or disabled controls MUST NOT be treated as Authorization.

## 12. Navigation

Global and local navigation, breadcrumbs, tabs, pagination, back behavior, deep links, route state, active state, and post-navigation focus MUST follow Product structure and Angular Router behavior.

Navigation MUST communicate current location and destination without inventing an unapproved page hierarchy. Tabs MUST represent an appropriate contained view, not replace deep links or routes when navigability is required.

## 13. Forms

Nontrivial forms MUST use the Typed Reactive Forms baseline in `ANGULAR.md`. Labels, instructions, help, required state, grouping, validation, server errors, disabled and read-only state, submission progress, focus, and autocomplete behavior MUST remain explicit and accessible.

Client validation improves feedback but does not replace API, Product, or Domain validation. Destructive submissions and duplicate-sensitive commands MUST preserve Contracted confirmation and idempotency behavior.

## 14. Buttons and Actions

Buttons perform actions; links navigate. Action hierarchy terms such as primary or secondary MAY be used only when Design System semantics establish them.

Loading and disabled states MUST communicate meaning without relying on appearance alone. Repeated activation MUST NOT duplicate effects. Icon-only actions require an accessible name, and keyboard activation MUST preserve native semantics. Disabled state is not Authorization.

## 15. Links

Links MUST provide meaningful destination context and use native navigation semantics. External links MUST use safe schemes and destination handling and MUST prevent opener abuse where applicable.

Link appearance and visited-state values remain Design System-owned. A link MUST NOT masquerade as an action when button semantics are required.

## 16. Tables and Data Grids

Tabular data SHOULD use semantic tables when row and column relationships matter. Headers, captions or labels, sorting, filtering, pagination, row actions, bulk selection, loading, empty, error, and partial-result states MUST be understandable and accessible.

Responsive overflow MUST preserve headers, actions, focus, and critical values. This standard selects no grid library, page size, or bulk-operation limit.

## 17. Lists and Cards

Lists and cards MUST reflect content and interaction semantics. Cards MUST NOT replace simpler lists merely for visual variety, and a large clickable region MUST retain understandable link or action behavior.

Nested actions, headings, focus targets, and reading order MUST remain valid. Repeated content MUST handle missing, long, and exceptional values safely.

## 18. Modals and Dialogs

Dialogs require an accessible name, deliberate initial focus, contained keyboard interaction, Escape behavior where safe, background interaction control, and focus return. Destructive confirmation MUST state the action and consequence.

Nested dialogs SHOULD be avoided. Scroll behavior and constrained-view layouts MUST preserve controls and content. This standard selects no dialog library.

## 19. Drawers, Popovers, Menus, and Tooltips

Each transient surface MUST match its semantic purpose and define trigger, focus, keyboard, dismissal, positioning failure, and responsive behavior. Menus represent sets of actions; they MUST NOT be used as generic content containers.

Tooltips MUST NOT contain essential information unavailable through visible text or another accessible mechanism. No positioning library is selected.

## 20. Notifications, Toasts, and Banners

Transient feedback, persistent warnings, blocking errors, and success confirmation MUST remain distinct. Critical, actionable, denied, Payment, or uncertain information MUST NOT rely solely on a disappearing toast.

Notifications MUST be announced appropriately without stealing focus unnecessarily. No display duration, queue policy, or notification library is selected.

## 21. Loading States

Local, page, and progressive loading MUST preserve context and distinguish pending from success. Skeletons MAY represent predictable structure; progress indicators SHOULD represent measurable work; spinners MAY represent indeterminate work when useful.

Loading UI MUST avoid unnecessary layout shift, duplicate requests, inaccessible animation, and invented minimum durations.

## 22. Empty States

The UI MUST distinguish truly empty, no-results, filtered-empty, permission-limited, unavailable, and not-yet-configured states. Copy and actions MUST reflect the verified cause without exposing inaccessible Resource existence.

An empty state MUST NOT imply failure or success when the underlying state is pending, stale, unknown, or denied.

## 23. Error, Degraded, and Stale States

Error, denied, stale, pending, unknown, partial, degraded, and applicable offline or network-interruption states MUST remain distinguishable. Recovery actions MUST be safe and must preserve user input where practical.

Uncertain Payment or Inventory state MUST NOT be collapsed into success or terminal failure. Technical and provider internals MUST not leak into UI messages.

## 24. Payment UI Safety

The UI MUST NOT establish confirmed Payment success from a Payment Redirect, query string, browser return, client state, browser storage, optimistic state, or frontend callback state. Only trusted backend state based on validated Payment Provider evidence can establish provider-dependent Payment state.

Pending, unknown, and reconciliation states MUST be represented honestly. Repeated actions MUST NOT create duplicate financial effects. Raw CVV MUST NOT be redisplayed after entry, persisted, logged, stored, telemetered, included in analytics or error reports, or placed in durable state.

## 25. Inventory UI Safety

Inventory owns authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state. Availability labels such as in stock, low stock, reserved, and sold out MUST reflect trusted backend semantics and freshness.

Cached, search, Catalogue, and optimistic UI state remain non-authoritative. Repeated interaction MUST NOT duplicate Stock Reservations or permit Overselling.

## 26. Price and Discount UI

Displayed Price, Discount, Promotion, Voucher, tax, shipping amount, totals, and savings are presentations of authoritative server and Domain calculations. Currency MUST remain explicit.

Client calculations MUST NOT become authoritative. This standard does not define rounding, allocation, formatting policy, or Promotion behavior.

## 27. Order, Return, and Refund UI

UI state and actions MUST use Approved lifecycle states and server-authoritative allowed transitions. This standard MUST NOT invent Order, Return, Payment, or Refund behavior.

A Return is distinct from a Refund, and a Refund Transaction remains a Payment Transaction distinct from the Return workflow. Uncertain or partial outcomes MUST be explicit.

## 28. Authentication UI

The UI MUST handle unauthenticated, expired Session or token, denied, logout, recovery, and identity-transition states according to the Approved Identity and Security design.

This standard does not select an Identity Provider, login protocol, credential storage model, or Session strategy. Clearing local state alone MUST NOT be presented as completed logout when trusted revocation is required.

## 29. Authorization UI

Visibility, route guards, disabled controls, Roles, Permissions, and Claims MAY guide UX but MUST NOT substitute for server-side Authorization. Denial MUST be handled safely even when the UI predicted access.

The UI MUST NOT reveal inaccessible Resource existence through navigation, cached state, errors, counts, or optimistic rendering.

## 30. Sensitive Data UI

Sensitive Data display MUST be necessary, minimal, authorized, and appropriate to the current Principal and task. Masking, reveal, copy, print, screenshots, autofill, autocomplete, clipboard, hidden values, and errors require deliberate Risk-based behavior.

No masking format is selected here. Hidden visual content remains present to the browser and MUST NOT be treated as a security control.

## 31. Search UI

Search is a non-authoritative projection. Input, request frequency, cancellation, loading, no-results, stale, filtering, sorting, pagination, highlighting, and failure behavior MUST remain explicit.

No debounce duration, search provider, ranking rule, highlighting library, or Product search behavior is selected.

## 32. Filters, Sorting, and Pagination

Controls MUST reflect API Contract semantics and preserve understandable active state, removal, reset, URL or route behavior, and accessibility. Filter state MAY be retained only when useful, safe, and lifecycle-bounded.

This standard does not select page sizes, filter operators, sort fields, persistence duration, or query encoding.

## 33. Media and Images

Media MUST define ownership, authorization, privacy, alternative text, aspect-ratio behavior, responsive sources, loading, placeholders, failure, and removal where applicable. Dimensions or aspect ratio SHOULD be reserved to reduce layout shift.

Object storage and media transformation remain Architecture and provider concerns. No CDN URL, transformation syntax, image service, or upload policy is selected here.

## 34. Icons

No icon library is selected. Icons MUST follow Approved Design System meaning and MUST NOT be the sole source of required meaning when visible text or an accessible name is necessary.

Custom icons require appropriate licensing, ownership, viewBox or sizing behavior, focus handling, contrast, and alternative semantics.

## 35. Typography

UI implementation MUST consume Approved Design System typography once established. It MUST NOT invent a font family, type scale, weights, line-height values, letter spacing, or type ramp as repository conventions.

Typography MUST preserve readable hierarchy, zoom, reflow, localization resilience, and content extremes.

## 36. Color

UI implementation MUST consume Approved Design System color tokens once established and MUST NOT invent a brand palette. Color alone MUST NOT convey required status, selection, validation, Risk, or action meaning.

Contrast and forced-color behavior MUST meet applicable accessibility Requirements.

## 37. Spacing and Density

Spacing and density MUST consume Approved tokens when established. Local spacing MUST not become a competing scale.

Administration density MAY differ from Storefront presentation only when task needs and Approved design evidence justify it; readability, focus, touch use, and accessibility MUST remain intact.

## 38. Elevation, Borders, and Radius

Elevation, shadow, borders, radius, and stacking MUST use Approved tokens when established. Values MUST NOT be invented as shared conventions in feature code.

Layering MUST remain finite and owned. Arbitrary z-index escalation and decorative boundaries that obscure hierarchy are PROHIBITED.

## 39. Motion and Animation

Motion MUST be purposeful, interruptible where applicable, and compatible with reduced-motion preferences. It MUST NOT obscure state changes, trigger unsafe reactions, delay critical information, or block task completion.

No animation library, duration, easing curve, or motion scale is selected.

## 40. Focus and Keyboard

Interactive UI MUST provide visible focus, logical order, native keyboard semantics, no keyboard traps, and appropriate focus restoration after navigation, dialogs, errors, and removed content. Skip navigation SHOULD be provided where repeated content warrants it.

Detailed accessibility rules remain governed by Approved core sources and by `ACCESSIBILITY.md` within its normative scope.

## 41. Touch and Pointer

Essential behavior MUST work without hover. Interactive targets, spacing, gestures, drag behavior, and alternatives MUST remain usable across supported pointer and touch input.

No numerical target size is selected unless an Approved Design System or accessibility source establishes it.

## 42. Content and Microcopy

UI text MUST follow Product and Design System content guidance, communicate action and recovery clearly, and avoid provider internals, technical diagnostics, blame, or unsupported promises.

No tone, brand voice, vocabulary extension, or legal statement is established here. Canonical repository terms MUST be used where they represent governed concepts.

## 43. Confirmations

Confirmation SHOULD be used only where Risk, irreversibility, cost, or loss of work justifies interruption. Routine and safely reversible actions SHOULD NOT be over-confirmed.

Destructive or irreversible confirmation MUST identify the target, consequence, and intended action according to Approved Product and Design rules. Confirmation is not Authorization.

## 44. Optimistic UI

Optimistic UI MAY be used only where rollback, conflict handling, user communication, and authoritative reconciliation are safe. Failure MUST restore or reconcile state without concealing uncertainty.

The UI MUST NOT optimistically confirm Payment, final Stock Reservation, Authorization, or irreversible Domain effects without trusted confirmation.

## 45. Caching and Stale UI

Cached UI MUST define freshness, invalidation, identity change, logout, retry, and failure behavior. Staleness MUST be visible where it can affect decisions or trust.

Stale Payment, Inventory, Price, Authorization, or Order state MUST NOT be represented as current authoritative truth.

## 46. Feature Flags

Feature Flags MAY govern rollout but MUST define owner, purpose, default, Environment behavior, evaluation boundary, telemetry, and removal. Flagged UI MUST NOT expose inaccessible, unsafe, or incomplete behavior.

Feature Flags are not Product modeling or Authorization. No provider or naming scheme is selected.

## 47. Analytics UI Boundary

Analytics MUST use the Architecture-owned Adapter and an approved provider and purpose. Events MUST respect Consent and privacy and MUST NOT become Product or Domain authority.

Secrets, credentials, tokens, raw CVV, unnecessary PII, and inaccessible data MUST NOT enter analytics. No analytics provider or event taxonomy is selected here.

## 48. UI Observability

UI observability SHOULD capture safe errors, state and navigation failures, dependency outcomes, performance signals, and Correlation IDs where governed. Logs, Metrics, Traces, analytics, and Audit Records remain distinct.

High-cardinality identifiers MUST NOT become uncontrolled Metric dimensions, and telemetry MUST NOT expose Sensitive Data or technical details to users.

## 49. Performance Boundary

UI implementation SHOULD avoid unnecessary layout shift, unbounded DOM growth, eager noncritical content, oversized media, duplicate requests, retained resources, and excessive layout or paint work.

No numerical budget, Core Web Vitals target, bundle threshold, or device profile is selected. Detailed performance rules remain governed by applicable Approved Requirements and by `PERFORMANCE.md` within its normative scope.

## 50. Accessibility Boundary

WCAG 2.2 AA is the Approved target. UI implementation MUST support semantic structure, keyboard operation, focus, accessible names, state announcements, zoom, reflow, reduced motion, contrast, error association, and input alternatives as applicable.

This standard does not replace `DESIGN-SYSTEM.md` or `ACCESSIBILITY.md`. Companion authority depends on the companion's own metadata, substantive content, and assigned scope; file existence alone does not establish authority.

## 51. Storybook Boundary

Storybook is the Established Design System documentation direction. Reusable Components SHOULD be documentable and testable there where it improves review and evidence.

Detailed Storybook implementation, configuration, addons, hosting, and test integration remain governed by `.ai/frontend/STORYBOOK.md` within its applicable normative scope. This standard does not independently establish those implementation details.

## 52. Visual Regression

Visual regression MAY support Approved token, Component, layout, state, and viewport validation. Baselines MUST correspond to intentional Approved design and be reviewable.

No snapshot framework, visual-regression runner, service, threshold, or storage mechanism is selected.

## 53. UI Testing

`TESTING-STANDARDS.md` governs test selection, depth, independence, evidence, and Exceptions. UI tests SHOULD cover public Component behavior, responsive states, loading, error, empty, denied and degraded states, keyboard and focus, destructive actions, and applicable Payment, Inventory, Authorization, and security behavior.

No test runner, snapshot tool, or coverage percentage is selected.

## 54. End-to-End Testing

Playwright remains the Approved End-to-End Test direction. Selected critical visual and interaction journeys SHOULD be verified end to end without duplicating all lower-level tests.

Tests MUST remain behavioral, deterministic, accessible, and independent of production credentials and uncontrolled External Systems.

## 55. Accessibility Testing

Accessibility verification MUST combine applicable automation with keyboard, focus, zoom, screen-reader, responsive, and state-announcement review proportionate to Risk.

Automated scans do not prove WCAG conformance. No accessibility test tool or numerical target is selected here.

## 56. Design System Testing

Reusable Components SHOULD provide evidence for supported variants, states, responsive behavior, accessibility, content extremes, interaction, loading, failure, and invalid combinations.

Test evidence MUST trace to Approved Design System behavior and MUST NOT use snapshots to invent or silently approve visual decisions.

## 57. Responsive Testing

Tests MUST cover representative Approved layouts, states, input modes, orientation or viewport changes where applicable, reflow, overflow, focus order, and critical action visibility.

No viewport matrix or Responsive Breakpoint value is established by this standard.

## 58. Content Extremes

UI implementation and tests SHOULD cover long text, missing optional data, long names, large counts, expanded or translated text where relevant, empty values, narrow space, and content failure.

Tests MUST use representative data without inventing Product content or exposing Sensitive Data.

## 59. Browser Support

UI implementation SHOULD use supported modern browser capabilities compatible with Angular 20 and the eventual Approved browser-support policy.

No browser matrix, minimum version, polyfill strategy, or device-support commitment is established here.

## 60. Print

Print behavior MAY be implemented only where an Approved Product Use Case requires it. Printed output MUST protect Sensitive Data, retain necessary meaning, and avoid exposing hidden or inaccessible content.

No repository-wide print scope or print design is selected.

## 61. Theming and Dark Mode

This standard does not establish theming or dark mode. If Product and Design governance approve them, implementation MUST be token-driven, accessible, consistent, and testable without duplicating Component behavior.

Theme choice MUST NOT change Product semantics, Authorization, or authoritative state.

## 62. Localization

Current market, language, and Currency scope remains governed by `PRODUCT.md`. UI structure SHOULD avoid unnecessary hard-coded assumptions that prevent future localization, expanded content, or bidirectional layout if later required.

No translation tooling, locale, delivery process, or future Product commitment is selected.

## 63. Administration UX

Administration UI MUST communicate Permission context, destructive consequences, bulk-operation scope, auditability cues where useful, and partial or failed results. Sensitive operations require clear target and outcome presentation.

This standard does not invent Staff User workflows, Permissions, approvals, or administration Product behavior.

## 64. Checkout UX

Checkout UI MUST preserve authoritative Price and Discount, Inventory revalidation, Payment pending and unknown states, Idempotency Key behavior, input preservation, and safe recovery according to Product and API Contracts.

It MUST NOT invent Checkout steps, Payment Provider behavior, shipping choices, or Product policy.

## 65. Customer Account UX

Account UI MUST protect Sensitive Data, preserve server-side Authorization, represent Session transitions honestly, and handle inaccessible, stale, and revoked state safely.

It MUST NOT invent Customer lifecycle, Identity Provider behavior, recovery policy, or Session strategy.

## 66. Design and Engineering Handoff

Implementation MUST trace relevant Product Requirements, Design System patterns, design evidence, Contracts, states, accessibility, and test expectations. Code MUST NOT silently decide an ambiguous material visual or interaction requirement.

Material ambiguity belongs with the owning Product, Design, Architecture, or security authority. Approved decisions and implementation must be synchronized.

## 67. Decision and ADR Governance

Material Architecture changes require ADR governance. Material Design System or Product decisions follow their applicable governance. Ordinary styling and Component implementation inside Approved boundaries does not automatically require an ADR.

This standard creates no UI-specific Decision identifier. A generic Decision Record cannot independently override Architecture or another higher-authority source.

## 68. Exception Governance

This standard creates no UI-specific formal Exception. Security, Testing, Coding, Documentation, Design, accessibility, and other Exceptions remain governed by their owning standards. Runtime UI errors are not governance Exceptions.

## 69. Prohibited Practices

The following are PROHIBITED:

- Inventing design tokens, Responsive Breakpoints, brand colors, fonts, icons, logos, or visual assets.
- Selecting a UI, styling, icon, animation, or visual-regression library without governance.
- Treating UI visibility, disabled state, route guards, Roles, or Claims as Authorization.
- Treating a Payment Redirect or client state as Payment proof.
- Treating stale Inventory, search, Catalogue, or cached state as authority.
- Persisting, logging, displaying after entry, or telemetering raw CVV.
- Using color alone for required meaning or creating inaccessible custom controls.
- Placing essential information only in a tooltip or disappearing notification.
- Using unbounded animation, arbitrary z-index escalation, or uncontrolled global CSS.
- Putting Product or Domain rules in reusable UI Components.
- Concealing destructive actions, partial failure, denial, pending, unknown, or stale state.
- Leaking Secrets, tokens, unnecessary PII, inaccessible data, or provider internals through analytics or UI.
- Using unsupported or unlicensed visual assets.
- Bypassing the Design System or silently creating a competing visual convention.

## 70. Ownership Boundaries

UI.md owns lower-level UI composition, styling ownership, responsive implementation, interaction-state presentation, and Design System consumption. It does not own Product behavior, Design System values, frontend Architecture, Angular mechanics, accessibility policy, performance budgets, Storybook configuration, API Contracts, security controls, or Testing policy.

`PRODUCT.md`, `DESIGN-SYSTEM.md`, `ARCHITECTURE.md`, `ANGULAR.md`, `SECURITY-STANDARDS.md`, `TESTING-STANDARDS.md`, and Approved API standards retain their scopes. `ACCESSIBILITY.md`, `PERFORMANCE.md`, and `STORYBOOK.md` govern only within assigned scopes when their own metadata and substantive content make them normative.

## 71. Deferred Decisions

The following remain deliberately unresolved or unselected:

- UI/component library and styling framework.
- CSS preprocessor, CSS-in-JS approach, and utility CSS framework.
- Icon library and exact Component inventory.
- Design-token source, names, and values.
- Font family, type scale, and exact brand palette.
- Responsive Breakpoints and detailed responsive standard.
- Motion values, animation library, theming, and dark mode.
- Visual-regression tooling, thresholds, and baseline storage.
- Browser-support matrix and print scope.
- Detailed accessibility and performance implementation rules owned by applicable normative companions.
- Detailed Storybook configuration, hosting, addons, and test integration.

Deferral is not permission for feature-level invention.

## 72. Related Documents

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
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/STORYBOOK.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`

## 73. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the UI implementation standard after final governance, Design System, component, styling, responsive, interaction-state, Payment, Inventory, Authorization, accessibility, Storybook, testing, observability, performance, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial UI implementation standard covering Design System consumption, component composition, styling, responsive behavior, interaction states, forms, navigation, Payment, Inventory, accessibility boundaries, testing, observability, performance, and governance. |

## 74. Quality Requirements

This standard MUST remain Design-System-aligned, Product-safe, accessible, responsive, testable, subordinate, technology-neutral where unresolved, and free of invented design values.

Implementation rules MUST remain practical, traceable to governing sources, and proportionate to Product, security, accessibility, and operational Risk.

## 75. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. sections are sequential and unique, content is complete, tables are valid, and Related Documents exist without self-reference;
3. no UI, styling, icon, animation, analytics, rendering, or testing technology is silently selected;
4. no color, font, token, Responsive Breakpoint, duration, budget, threshold, Product behavior, Decision type, or Exception type is invented;
5. WCAG 2.2 AA, Storybook, Playwright, Design System, Product, Angular, API, Security, and Testing boundaries match Approved governance;
6. Payment Provider evidence, Inventory authority, server-side Authorization, Sensitive Data, and raw-CVV protections remain intact;
7. companion authority remains metadata-, substantive-content-, and assigned-scope-driven;
8. no unfinished marker, actual ellipsis, fabricated path, empty section, or malformed Markdown remains; and
9. only `.ai/frontend/UI.md` changes for a scoped update.

This Approved document is a subordinate UI implementation standard within its scope. Approval does not make `authoritative: false` true or permit this document to override `.ai/core/`.
