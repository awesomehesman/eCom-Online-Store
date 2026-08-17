---
title: ACCESSIBILITY
version: 1.0.0
status: Approved
owner: Product Design and Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# Accessibility Implementation Standard

## 1. Purpose, Scope, and Authority

This document defines lower-level accessibility implementation requirements for the Storefront and administration UI, including forms, navigation, dialogs, tables, media, feedback, errors, loading, responsive behavior, keyboard, focus, screen-reader semantics, zoom, reflow, pointer input, motion, color, content, Authentication, account, Checkout, Payment, and Inventory interfaces.

It is subordinate to the Decision Hierarchy and Approved Canonical Documents. `PRODUCT.md` owns Product behavior; `DESIGN-SYSTEM.md` owns reusable visual and interaction guidance; `UI.md` owns UI implementation; `ANGULAR.md` owns Angular mechanics; `SECURITY-STANDARDS.md` owns security; and `TESTING-STANDARDS.md` owns test governance. Its `authoritative: false` metadata MUST remain false; approval does not make this document a core Source of Truth.

## 2. WCAG Target

WCAG 2.2 AA is the Approved target. This target guides Requirements, design, implementation, testing, and evidence; it is not a certification claim and automated checks alone do not demonstrate conformance.

This standard does not redefine Success Criteria, establish AAA as a repository target, invent exceptions, or reproduce WCAG as a competing source. Practical rules below MUST be interpreted consistently with the applicable WCAG 2.2 criteria and governing repository standards.

## 3. Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | WCAG 2.2 AA | Approved accessibility target, not certification. |
| Established | Angular 20 and Standalone Components | Approved frontend implementation baseline. |
| Established | Storybook | Approved Design System documentation direction. |
| Established | Playwright | Approved End-to-End Test direction. |
| Established | axe and Lighthouse accessibility-checking direction | AGENTS.md requires automated axe and Lighthouse checks in CI; exact integrations and versions remain implementation-owned. |
| Unselected | `axe-core` package or integration | No package or repository configuration selects this exact dependency. |
| Unselected | Accessibility Insights, Pa11y, and WAVE | No repository evidence selects these tools. |
| Unselected | Screen-reader automation, visual accessibility tooling, browser accessibility extensions, and accessibility testing SaaS | No tool, service, or provider is selected. |

Tool mention, file existence, or common industry use does not establish an additional selection.

## 4. Semantic HTML

Native HTML semantics SHOULD be preferred over custom ARIA recreation. Headings, landmarks, lists, tables, buttons, links, forms, fieldsets, legends, `details`, `summary`, and native controls MUST be used according to their meaning where suitable.

Presentation needs MUST NOT drive misuse of semantic elements. Custom controls require evidence that native behavior cannot satisfy the Use Case.

## 5. ARIA

ARIA roles, states, and properties MUST match actual behavior and supported patterns. `aria-label`, `aria-labelledby`, `aria-describedby`, `aria-live`, `aria-current`, `aria-expanded`, `aria-controls`, and `aria-hidden` MUST be used only when semantically valid.

Redundant or conflicting ARIA is PROHIBITED. Hidden content MUST not remain incorrectly exposed, and focusable content MUST not be hidden from assistive technology. ARIA MUST NOT replace valid native semantics unnecessarily.

## 6. Accessible Names and Descriptions

Every interactive control MUST have an appropriate accessible name. Visible labels are preferred; icon-only controls, form fields, dialogs, links, images, buttons, and status indicators require names that accurately communicate purpose.

Descriptions supplement rather than replace names. Names and descriptions MUST NOT be fabricated from misleading text, filenames, provider metadata, or implementation details.

## 7. Headings and Landmarks

Pages MUST provide an understandable heading hierarchy and meaningful landmarks for major regions. Headings MUST communicate structure rather than provide visual styling alone.

Repeated Components SHOULD avoid meaningless heading noise. Exact heading levels depend on page context and MUST not be prescribed without that context.

## 8. Keyboard Access

All essential functionality MUST be keyboard accessible. Native Tab, Shift+Tab, Enter, Space, Escape, and arrow-key behavior MUST be preserved where the relevant control or established widget pattern requires it.

Keyboard traps are PROHIBITED. Roving `tabindex` MAY be used for an established composite-widget pattern. Custom shortcuts require Product and Design approval and MUST avoid conflict with browser or assistive-technology commands.

## 9. Focus Management

Focus MUST be visible, ordered logically, and moved only when doing so communicates a meaningful context change. Navigation, dialogs, validation errors, inserted or removed content, destructive actions, and recovery flows MUST define appropriate focus behavior.

Dialogs require deliberate initial focus and focus return. Skip navigation SHOULD be provided where repeated content warrants it. Indiscriminate autofocus and focus theft are PROHIBITED.

## 10. Tabindex

Positive `tabindex` values are PROHIBITED. DOM order SHOULD normally determine focus order; `tabindex="0"` and `tabindex="-1"` MAY be used only for valid participation or programmatic focus needs.

Custom focus ordering requires clear semantics and MUST remain stable across responsive layouts.

## 11. Routing and SPA Navigation

Angular route changes MUST leave page context understandable through deliberate document title, heading, focus, and announcement behavior appropriate to the route. Redirects, denial, loading, and route failures MUST not strand keyboard or screen-reader users.

This standard does not mandate one focus target for every page. Angular Router mechanics remain governed by `ANGULAR.md`.

## 12. Forms

Forms MUST provide explicit labels, required state, instructions, field grouping, appropriate autocomplete and input-purpose semantics, validation, programmatic error association, submission feedback, and understandable disabled or read-only state.

Typed Reactive Forms remain Angular-owned. Accessibility feedback improves interaction but does not replace API, Product, or Domain validation.

## 13. Validation and Errors

Errors MUST identify the affected field or area, explain the problem clearly, support recovery, and be programmatically associated where applicable. Errors MUST not rely on color alone and MUST remain available long enough to understand and act upon.

Error messages MUST not expose provider internals, stack traces, Sensitive Data, or inaccessible Resource details.

## 14. Error Summaries

Long or complex forms SHOULD provide an error summary when it materially improves discovery and recovery. A summary SHOULD link or move focus to affected fields and MUST preserve entered data where practical.

An error summary is not mandatory for every trivial form.

## 15. Required and Optional State

Required and optional state MUST be visually and programmatically understandable. Placeholder text MUST NOT be the only label or instruction.

Indicators and wording MUST follow Approved Design System and Product content guidance.

## 16. Autocomplete and Input Purpose

Appropriate browser autocomplete and input-purpose semantics SHOULD be used where they improve accessibility and remain consistent with Security, privacy, and Product Requirements.

This standard does not invent autocomplete behavior for credentials, Payment data, or other Sensitive Data.

## 17. Button and Link Semantics

Buttons perform actions and links navigate. Native semantics, accessible names, keyboard activation, focus, and state MUST be preserved.

Disabled controls MUST communicate their state and MUST NOT be the sole Authorization mechanism. Fake-disabled patterns that remain confusingly interactive are PROHIBITED.

## 18. Dialogs

All dialogs require an accessible name, valid dialog semantics, deliberate initial focus, keyboard-operable dismissal, usable scrolling and reflow, and appropriate focus restoration when closed.

Modal dialogs MUST contain focus appropriately while active and prevent interaction with surrounding content according to the applicable accessible modal-dialog pattern.

Non-modal dialogs MUST preserve accessible interaction with surrounding content and provide a deliberate, keyboard-accessible means to move between the dialog and the rest of the page.

Nested dialogs SHOULD be avoided. No modal or dialog library is selected.

## 19. Menus, Popovers, and Disclosures

Triggers MUST expose purpose, expanded state, and relationships where applicable. Keyboard navigation, focus, dismissal, and state MUST follow the established pattern.

Menu semantics MUST NOT be applied to generic navigation or content incorrectly.

## 20. Tooltips

Tooltips MUST NOT contain essential information, instructions, or validation unavailable elsewhere. They MUST account for hover, focus, touch, dismissal, and magnification limitations.

Tooltip-only labels or errors are PROHIBITED.

## 21. Tabs

Tabs MUST expose correct roles, selected state, focus behavior, keyboard interaction, and tab-to-panel relationships. Automatic or manual activation MUST be intentional and testable.

Tabs MUST NOT replace routes when deep linking and navigation semantics are required.

## 22. Tables

Tables MUST expose headers, captions or labels, appropriate cell association, sorting state, row actions, and empty, loading, partial, and error states. Responsive treatment MUST preserve meaning, focus, and operability.

Complex tables MAY require an alternative presentation. No grid library is selected.

## 23. Cards and Lists

Cards and lists MUST use semantic grouping, clear headings, understandable links and actions, and valid interactive nesting. Large clickable regions MUST retain clear destination or action semantics.

Excessive nested controls and duplicate accessible names SHOULD be avoided.

## 24. Status and Live Regions

Success, error, pending, loading, background completion, and destructive outcomes MUST be announced when needed for equivalent access. Live regions MUST use appropriate politeness and atomicity and MUST not create excessive or duplicate announcements.

Not every visual state change requires an announcement.

## 25. Loading

Loading MUST be perceivable without excessive noise or focus theft. Skeletons and spinners MUST not be the only semantic indication when status needs announcement.

Pending state MUST remain distinct from completion, failure, and inactivity.

## 26. Empty, Stale, Degraded, and Unknown States

Empty, stale, degraded, denied, partial, pending, unknown, and offline or interrupted states MUST remain distinguishable and accessible.

Unknown or pending Payment MUST not be announced as success or failure. Stale Inventory information MUST not be announced as authoritative current Stock.

## 27. Payment Accessibility

Accessible Payment UI MUST represent pending, unknown, failure, recovery, and trusted confirmation honestly. Payment Redirect, browser state, query parameters, local state, storage, optimistic state, and frontend callback state are not Payment proof; validated Payment Provider evidence processed through the trusted backend remains authoritative.

Payment interaction MUST support keyboard and screen-reader completion and prevent duplicate submission. Raw CVV MUST NOT be redisplayed after entry, persisted, logged, stored, telemetered, analyzed, included in analytics or error reports, or placed in durable state.

## 28. Inventory Accessibility

Availability MUST be understandable beyond color or icon alone. Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement.

Stale, unknown, reserved, low-stock, and unavailable presentation MUST reflect trusted semantics and freshness. UI interaction MUST NOT duplicate Stock Reservations or permit Overselling.

## 29. Price and Money Accessibility

Currency and Price context MUST be understandable. Price changes SHOULD be announced when needed for the active task; Discount and savings MUST not rely on color alone; struck-through Price MUST retain semantic context.

Client calculations remain non-authoritative. No Money formatting, rounding, allocation, or Promotion rule is defined here.

## 30. Order, Return, and Refund

Accessible labels and actions MUST use Approved lifecycle semantics and server-authoritative transitions. Return and Refund remain distinct, and a Refund Transaction remains a Payment Transaction distinct from the Return workflow.

This standard does not invent lifecycle states or Product behavior.

## 31. Authentication and Account UI

Login, recovery, Session expiry, denial, logout, and identity-transition UI MUST provide accessible fields, instructions, errors, status, focus, and recovery.

No Identity Provider, protocol, credential storage model, Session strategy, or account workflow is selected.

## 32. Authorization and Denial

Denied states MUST be understandable without revealing inaccessible Resource details. UI hiding, route guards, disabled controls, Roles, Permissions, and Claims are UX mechanisms, not Authorization.

Server-side Authorization remains authoritative.

## 33. Color and Contrast

Text, non-text UI, focus indicators, status, errors, and selected states MUST meet applicable WCAG 2.2 AA contrast Requirements and Approved Design System guidance.

No color value or palette is established here. Contrast claims MUST reference the applicable criterion and evidence accurately.

## 34. Color Independence

Color MUST NOT be the sole means of conveying required meaning. Text, shape, iconography, pattern, position, or programmatic semantics SHOULD provide an appropriate additional cue.

The additional cue MUST remain understandable without inventing a new Design System convention.

## 35. Typography and Readability

Typography MUST preserve readable hierarchy, zoom, reflow, wrapping, user-adjusted text spacing, and content extremes. Text MUST not be clipped or made unavailable by fixed assumptions.

No font family, type scale, weight, line-height, or size value is selected.

## 36. Zoom and Reflow

UI MUST remain usable under the applicable WCAG 2.2 AA zoom and reflow conditions without loss of information or functionality except where an applicable criterion permits an exception.

Implementation and tests MUST reference WCAG conditions accurately rather than inventing arbitrary repository viewport or zoom values.

## 37. Text Spacing

Content and functionality MUST tolerate user-adjusted text spacing where WCAG requires it. Labels, controls, errors, dialogs, and navigation MUST not clip or overlap in ways that block use.

No Design System spacing values are created here.

## 38. Orientation

Content MUST NOT unnecessarily restrict display orientation. A restriction requires a genuine task necessity and applicable Product, Design, and accessibility evidence.

This standard does not invent orientation-dependent Product behavior.

## 39. Motion and Flashing

Implementation MUST respect reduced-motion preferences and avoid unnecessary motion, unsafe flashing, discomfort-triggering effects, and animation that hides critical state or blocks tasks.

Applicable WCAG motion and flashing criteria MUST be followed accurately. No motion values are selected.

## 40. Animation and Auto-Updating Content

Moving, blinking, scrolling, or auto-updating content MUST provide applicable pause, stop, hide, or control behavior and remain understandable to assistive technology.

No carousel, rotation interval, animation duration, or content-refresh frequency is established.

## 41. Touch and Pointer

Essential functionality MUST support appropriate pointer alternatives, usable targets, hover independence, and input methods beyond precision pointing. Gesture behavior MUST not remove an equivalent accessible action.

Applicable WCAG 2.2 target-size and pointer criteria MUST be applied accurately; this standard does not invent stronger numerical dimensions.

## 42. Dragging

Where dragging is implemented, a non-drag single-pointer alternative MUST be provided when required by WCAG, unless an applicable exception is demonstrated.

This standard does not create a drag interaction or Product requirement.

## 43. Media and Images

Informative images require meaningful alternatives; decorative images require appropriate suppression; complex images require an equivalent explanation appropriate to their purpose. Product media, icons, and images of text MUST follow applicable WCAG and Design System guidance.

Alternative text MUST not be invented from filenames or untrusted provider metadata.

## 44. Audio and Video

If Product scope includes audio or video, applicable captions, transcripts, audio description, controls, and alternatives MUST be provided according to WCAG and Approved Requirements.

This standard does not introduce multimedia Product scope.

## 45. Icons

Icons MUST provide appropriate visible text or an accessible name where meaningful, and decorative icons MUST be hidden appropriately. Contrast and semantic use MUST remain consistent.

No icon library is selected.

## 46. Content and Language

Page language and language changes MUST be identified where applicable. Instructions and errors MUST be understandable and avoid unnecessary jargon, provider internals, and technical diagnostics.

Current language and market remain Product-governed. No tone, brand voice, translation tool, or future Product scope is established.

## 47. Consistent Identification

Reusable Components and actions with the same functionality SHOULD be identified consistently across contexts, subject to Product and Design requirements.

Consistency MUST not override necessary context or accessible specificity.

## 48. Consistent Navigation

Repeated navigation mechanisms SHOULD remain consistent in order, identification, and behavior where applicable.

This standard does not invent information Architecture or page hierarchy.

## 49. Help and Support

Consistent help mechanisms MUST follow Approved Product and Design decisions where present and remain accessible from relevant contexts.

No support channel, contact method, or service-level commitment is created here.

## 50. Cognitive Load

UI SHOULD use clear labels, predictable state, error prevention, recoverability, Risk-appropriate confirmation, preserved input, and reduced unnecessary memory burden.

This standard does not claim support beyond applicable WCAG and Approved Requirements or create unsupported cognitive-accessibility certification.

## 51. Accessible Authentication

Authentication implementation MUST follow applicable WCAG 2.2 accessible-authentication criteria. It MUST NOT require a cognitive-function test without an allowed accessible mechanism or exception under the criterion.

This requirement constrains implementation but does not select or invent an Authentication design.

## 52. Redundant Entry

Where the WCAG 2.2 redundant-entry criterion applies, information previously entered by or provided to the user in the same process MUST be auto-populated or available for selection unless an allowed exception applies.

This standard does not invent Checkout, account, or recovery workflow behavior.

## 53. Focus Visibility and Obscuring

Keyboard focus indicators MUST remain visible and MUST satisfy the applicable WCAG 2.2 AA focus Requirements, including Focus Visible and Focus Not Obscured (Minimum), together with Approved Design System guidance.

WCAG 2.2 Focus Appearance (2.4.13) is a Level AAA Success Criterion and is not made mandatory by this AA-target standard unless separately established through Approved governance.

No additional visual value is invented here; evidence MUST demonstrate the applicable governed criterion.

## 54. Target Size

Interactive targets MUST satisfy the applicable WCAG 2.2 target-size criterion and its exceptions. Product and Design MAY establish a stronger target through applicable governance.

This standard does not establish a stronger repository-wide numerical target.

## 55. Responsive Accessibility

Responsive reflow MUST preserve reading order, focus order, content, controls, errors, status, tables, dialogs, and critical actions. Content MUST not become available only through hover or a precision pointer.

Exact Responsive Breakpoints remain Design System and UI-owned.

## 56. Custom Components

Custom widgets MUST follow an established accessible pattern, support required keyboard interaction, expose names, roles, states, and properties, manage focus, and remain testable.

Native controls SHOULD be preferred where suitable.

## 57. Shadow DOM, Portals, and Overlays

If used, Shadow DOM, portals, and overlays MUST preserve focus, accessible names, ownership, relationships, reading order, dismissal, and testability across boundaries.

No overlay, portal, or component library is selected.

## 58. Browser and Assistive Technology

Testing SHOULD use representative supported browser and assistive-technology combinations selected according to Requirements, user impact, and Risk.

No browser, device, screen-reader, or assistive-technology matrix is established here.

## 59. Security and Accessibility

Accessibility implementation MUST NOT weaken Authentication, Authorization, Session security, CSRF, CORS, safe-link handling, trusted-content boundaries, or Sensitive Data protection.

Visually hidden or collapsed content MUST not expose inaccessible Sensitive Data to assistive technology or the DOM unnecessarily.

## 60. Privacy

Announcements, labels, autocomplete, clipboard, print, screenshots, testing, and third-party tools MUST respect privacy and data minimization.

Accessibility tooling, telemetry, fixtures, and reports MUST not leak Sensitive Data, credentials, raw CVV, or unnecessary PII.

## 61. Analytics and Telemetry

Accessibility telemetry MAY measure failures or use only through Approved analytics and observability boundaries with Consent and privacy where applicable.

No Sensitive Data or unnecessary PII may be collected, and no accessibility analytics provider is selected.

## 62. Performance Boundary

Accessibility MUST NOT be removed, hidden, deferred, or degraded as a performance optimization. Performance work MUST preserve semantics, focus, announcements, and equivalent access.

`PERFORMANCE.md` governs only within its assigned scope when its own metadata and substantive content make it normative. No performance threshold is established here.

## 63. Storybook Boundary

Storybook remains the Established Design System documentation direction. Reusable Components SHOULD expose accessibility-relevant variants, states, content extremes, and interaction examples where useful.

This standard does not select Storybook addons, axe integration, hosting, or test tooling. `STORYBOOK.md` governs details within its applicable normative scope.

## 64. Automated Testing

Automation MAY verify semantics, ARIA, labels, keyboard behavior, contrast where supported, and common violations. Automated results MUST be reviewed and MUST NOT be represented as proof of WCAG 2.2 AA conformance.

AGENTS.md establishes axe and Lighthouse checking direction; no exact package, integration, configuration, or additional accessibility tool is selected here.

## 65. Manual Testing

Manual testing SHOULD cover keyboard-only use, focus, zoom, reflow, text spacing, reduced motion, touch and pointer use, errors, recovery, and screen-reader behavior where Risk justifies it.

No mandatory assistive-technology matrix is selected.

## 66. Playwright

Playwright remains the Approved End-to-End Test direction and MAY verify keyboard, focus, semantics, routing, and state behavior where appropriate.

Playwright alone does not prove accessibility or WCAG conformance.

## 67. Component Testing

Component Tests SHOULD verify public accessibility behavior at the smallest effective level. No Unit Test or Component Test runner is selected unless repository evidence establishes one.

Tests MUST not depend on private framework internals as accessibility evidence.

## 68. Screen-Reader Testing

Manual screen-reader verification SHOULD be used for critical or high-Risk flows where automation cannot provide sufficient evidence.

No screen reader, automation product, browser pairing, or test matrix is selected as the repository standard.

## 69. Payment Testing

Payment accessibility tests MUST cover pending, unknown, failure, trusted success, duplicate submission protection, validation, recovery, focus, announcements, and raw-CVV non-retention.

Success MUST be based on authoritative backend Payment state, not redirect or client state.

## 70. Inventory Testing

Inventory accessibility tests SHOULD cover stale state, sold-out changes, Stock Reservation failure, quantity errors, focus, recovery, and accessible availability updates.

Tests MUST preserve Inventory authority and prevention of duplicate effects or Overselling.

## 71. Authorization Testing

Tests MUST cover denied paths, UI prediction failure, object-level denial, and safe errors without exposing inaccessible Resource details.

UI visibility MUST not be the only Authorization test.

## 72. Responsive Testing

Accessibility MUST be tested across representative Approved responsive layouts, content states, input modes, orientation changes where applicable, and reflow behavior.

No breakpoint or viewport matrix is established.

## 73. Content Extremes

Tests SHOULD cover long labels and text, expanded or translated content where relevant, errors, empty values, large counts, narrow layouts, missing optional data, and content failure.

Fixtures MUST not invent Product behavior or expose Sensitive Data.

## 74. Defect Severity

Accessibility defects MUST be prioritized using user impact, affected functionality, Product Risk, security impact, prevalence, workaround availability, and existing release criteria.

This standard does not create a competing defect-severity matrix.

## 75. Release Gate

Applicable accessibility evidence is REQUIRED before release. Blocking findings MUST follow Testing, Security, Product, and release governance.

This standard creates no accessibility-specific bypass. Applicable formal Exceptions remain governed by their owning standards.

## 76. Human Review

Human review is REQUIRED for accessibility aspects that automation cannot validate reliably, proportionate to Risk and affected users.

Tool output MUST not replace informed judgment, usability evidence, or review of complex interaction.

## 77. Documentation

Accessibility decisions, Requirements, evidence, known limitations, formal Exceptions, and remediation ownership MUST be documented under `DOCUMENTATION-STANDARDS.md`.

This standard creates no competing documentation hierarchy.

## 78. Decision and ADR Governance

Material Architecture changes require ADR governance and synchronized Architecture updates. Material Product or Design System accessibility decisions follow their applicable governance.

Ordinary implementation fixes do not automatically require an ADR, and a generic Decision Record cannot independently override Architecture.

## 79. Exception Governance

This standard creates no Accessibility Exception type. Accessibility-related deviations MUST use the formal exception mechanism of the owning Requirement or standard; multiple Exceptions may be required when multiple standards are affected.

Runtime accessibility defects and tool findings are not governance Exceptions.

## 80. Prohibited Practices

The following are PROHIBITED:

- Replacing a suitable native control with a `div` or `span` unnecessarily.
- Positive `tabindex`, keyboard traps, focus removal without replacement, and indiscriminate autofocus.
- Color-only meaning, placeholder-only labels, tooltip-only essential information, and hidden inaccessible errors.
- Inaccessible custom widgets, unsupported ARIA, conflicting ARIA, or `aria-hidden` on focusable content.
- Conveying success only visually or treating Payment Redirect or client state as Payment proof.
- Treating stale Inventory state as authoritative truth.
- CAPTCHA without a compliant alternative where applicable.
- Redisplaying after entry, persisting, logging, storing, telemetering, analyzing, or otherwise exposing raw CVV.
- Treating an automated tool or scan as accessibility certification.
- Disabling zoom, unsupported orientation lock, or animation that ignores reduced-motion needs.
- Bypassing Approved Design System patterns or weakening security for accessibility convenience.

## 81. Ownership Boundaries

ACCESSIBILITY.md owns lower-level accessibility implementation refinements. It does not own Product behavior, Design System values, general UI composition, Angular mechanics, security controls, testing governance, performance budgets, Storybook configuration, or API Contracts.

`PRODUCT.md`, `DESIGN-SYSTEM.md`, `UI.md`, `ANGULAR.md`, `SECURITY-STANDARDS.md`, `TESTING-STANDARDS.md`, `PERFORMANCE.md`, `STORYBOOK.md`, and `API.md` retain their assigned scopes according to their own metadata and substantive content.

## 82. Deferred Decisions

The following remain unresolved or unselected:

- Exact axe and Lighthouse integrations, versions, configuration, and CI implementation.
- `axe-core` dependency and any additional accessibility automation tool.
- Screen-reader automation and browser or assistive-technology test matrix.
- Storybook accessibility addon and visual accessibility regression tooling.
- Exact visual and Design System values.
- Detailed browser-support policy and monitoring provider.
- Formal accessibility certification.

Deferral does not weaken WCAG 2.2 AA or applicable release evidence.

## 83. Related Documents

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
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/STORYBOOK.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/backend/AZURE.md`

## 84. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the accessibility implementation standard after final governance, WCAG 2.2 AA, semantics, ARIA, keyboard, focus, forms, responsive behavior, Payment, Inventory, Authorization, security, Storybook, testing, human review, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial accessibility implementation standard covering WCAG 2.2 AA, semantic HTML, ARIA, keyboard, focus, forms, responsive behavior, Payment, Inventory, accessibility testing, Storybook, governance, and release evidence. |

## 85. Quality Requirements

This standard MUST remain technically accurate, implementation-focused, Product-safe, Design-System-aligned, testable, evidence-based, subordinate, and free of invented tools, design values, Product behavior, or certification claims.

Requirements MUST remain practical across the Storefront and administration UI and proportionate to user impact and Risk.

## 86. Final Validation

Before material revision, re-approval, or implementation reliance, validation MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. WCAG 2.2 AA remains the target without certification, AAA escalation, invented exceptions, or inaccurate Success Criterion claims;
3. axe and Lighthouse direction matches AGENTS.md while no exact dependency, integration, additional tool, provider, browser matrix, or assistive-technology matrix is silently selected;
4. Product, Design System, UI, Angular, Security, Testing, Performance, Storybook, API, Payment, Inventory, and Authorization boundaries remain intact;
5. no design value, Product behavior, Decision type, Exception type, performance threshold, or unsupported technology is invented;
6. headings are sequential and unique, sections are non-empty, tables are valid, Related Documents exist without self-reference, and Revision History is current;
7. no TODO, TBD, FIXME, actual ellipsis, placeholder, or fabricated path remains; and
8. only `.ai/frontend/ACCESSIBILITY.md` changes for a scoped update.

This Approved document is a subordinate accessibility implementation standard within its scope. Approval does not make `authoritative: false` true or permit this document to override `.ai/core/`.
