---
title: DESIGN-SYSTEM
version: 0.1.0
status: Draft
owner: Product Design
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Design System

## 1. Purpose

This document establishes the durable visual, interaction, accessibility, component, token, and content principles shared by customer and Staff User interfaces. It exists to create a coherent experience, improve accessibility and maintainability, accelerate implementation, reduce design drift, and give Product, Design, Engineering, QA, and AI Agents one design language.

The Design System governs reusable UI and UX decisions. It does not replace Product Requirements, page Specifications, implementation standards, or qualified design and engineering judgment.

## 2. Scope

This standard applies to the Storefront, Account area, Cart, Checkout, Order and Payment-related flows, Staff User tooling, responsive layouts, navigation, forms, feedback, data presentation, reusable components, design tokens, content patterns, and accessible default, loading, empty, error, success, and disabled states.

It governs shared patterns rather than page-by-page behavior. A feature Specification MAY add context-specific detail without weakening this baseline or changing behavior owned by a higher-authority source.

## 3. Repository Authority

Design decisions MUST follow the Decision Hierarchy in `AGENTS.md`.

| Governing source | Relationship to design |
| --- | --- |
| `AGENTS.md` | Governs repository authority, contributor behavior, and the Decision Hierarchy. |
| `GLOSSARY.md` | Governs canonical terminology. |
| `VISION.md` | Defines durable strategic experience direction without creating current Product scope. |
| `PRODUCT.md` | Governs Product behavior, scope, policies, and Requirements. |
| `ARCHITECTURE.md` | Governs Angular and frontend technical architecture. |
| `SECURITY-STANDARDS.md` | Governs mandatory security and privacy controls. |
| `TESTING-STANDARDS.md` | Governs verification and evidence. |
| `CODING-STANDARDS.md` | Governs implementation practices. |
| `ENGINEERING-PRINCIPLES.md` | Provides decision heuristics within governing constraints. |
| `DOCUMENTATION-STANDARDS.md` | Governs documentation quality and lifecycle. |
| `DECISIONS.md` | Governs durable decision recording and indexing. |
| `DESIGN-SYSTEM.md` | Governs visual and interaction consistency within those constraints. |

This document MUST NOT override Product behavior, Architecture, security controls, or another higher-authority Requirement. Accessibility requirements remain mandatory regardless of aesthetic preference.

## 4. Design Principles

- Clarity before decoration.
- Customer confidence before conversion pressure.
- Accessibility by default.
- Consistency unless context justifies difference.
- Progressive disclosure without hiding critical information.
- Strong hierarchy and explicit state.
- Responsive behavior by design.
- Error prevention followed by clear recovery.
- Predictable interaction and restrained motion.
- Data and business truth before decorative presentation.
- Brand expression without loss of usability.

## 5. Experience Principles

The experience SHOULD feel premium, modern, calm, trustworthy, responsive, visually confident, and understandable under normal operation and failure. Customer journeys SHOULD remain focused and usable on mobile, while Staff User journeys SHOULD favor operational clarity and accurate action over decorative treatment.

Fast perception MUST NOT be created by concealing pending or uncertain state. Visual refinement MUST support comprehension, not compete with it.

## 6. Brand Expression

Brand expression SHOULD use photography-led merchandising, strong typographic hierarchy, restrained interface chrome, purposeful whitespace, and editorial composition where it supports discovery. Visual confidence SHOULD come from composition, imagery, proportion, and consistency rather than unnecessary ornament.

No exact logo, font family, color palette, or asset identity is established by current Approved sources. Product Design MUST approve those assets through normal governance before they are represented as canonical, and adopted values MUST meet accessibility, performance, licensing, and implementation requirements.

## 7. Design Tokens

Design tokens SHOULD define color, typography, spacing, sizing, radius, border, shadow, elevation, motion, responsive breakpoints, and stacking order where those concerns recur.

Tokens MUST express semantic intent where practical, separate intent from raw values, avoid repeated magic values, support controlled evolution or theming, and remain consistently implementable. Exact values require Approved design evidence and MUST NOT be inferred from examples in this document.

## 8. Token Naming

Token names SHOULD communicate purpose, category, and role. Conceptual examples include `color-text-primary`, `color-surface-default`, `space-md`, and `radius-sm`.

Semantic names are preferred over names tied only to a raw hue or measurement. The implementation MAY adapt syntax to repository conventions, but equivalent concepts MUST remain traceable and aliases MUST NOT obscure ownership.

## 9. Color System

The color system SHOULD provide semantic roles for primary and secondary text, surfaces, borders, interactive states, success, warning, error, informational feedback, disabled content, selection, and focus.

Color contrast and distinguishability MUST satisfy approved accessibility requirements. Color MUST NOT be the sole means of conveying state, meaning, availability, validation, or urgency. Exact color values remain deferred until Approved brand and accessibility evidence exists.

## 10. Typography System

The typography system SHOULD define a restrained hierarchy for display, heading, body, label, caption, utility, and numeric or data-heavy content where useful. Styles SHOULD prioritize legibility, consistent line height, responsive scaling, meaningful emphasis, and stable reading rhythm.

Type styles MUST remain few enough to communicate hierarchy predictably. No font family is mandated until Product Design approves one with licensing, language, performance, and accessibility evidence.

## 11. Spacing System

Recurring spacing SHOULD come from a coherent token scale. Related elements SHOULD appear closer than unrelated elements, and section boundaries SHOULD remain clear without excessive empty space.

One-off spacing values SHOULD be avoided when an existing token expresses the intent. Storefront layouts MAY be more spacious and editorial; Staff User layouts MAY be denser when legibility and interaction accuracy are preserved.

## 12. Layout System

Layouts SHOULD use consistent containers, grids, alignment, gutters, content widths, and hierarchy. Maximum reading widths SHOULD protect comprehension, while commerce imagery and data layouts MAY use wider compositions when their purpose requires it.

Layouts MUST adapt from constrained widths upward and MUST NOT depend on rigid desktop assumptions. Exact container and grid values remain governed by Approved design assets and implementation evidence.

## 13. Responsive Design

Designs SHOULD account for small mobile, mobile, tablet, desktop, and larger available spaces without assuming that device labels define behavior precisely. Components SHOULD adapt to content, interaction needs, and available space where practical.

Responsive changes MUST preserve task completion, reading order, focus order, and access to critical information. Exact breakpoints remain deferred until established by an Approved implementation or design decision.

## 14. Mobile-First Principles

Customer commerce flows SHOULD prioritize mobile usability through readable content, adequate touch targets, thumb-reachable primary actions where practical, simplified hierarchy, and efficient Checkout interaction.

No critical action or information MAY require desktop-only behavior. Responsive simplification MUST NOT hide Price, Payment, delivery, validation, consent, or destructive consequences.

## 15. Navigation

Primary, secondary, breadcrumb, Account, Staff User, and mobile navigation SHOULD communicate current context, available destinations, and hierarchy consistently. Navigation labels MUST use canonical Product terminology and remain understandable without relying only on icons.

Critical paths MUST remain discoverable. Mobile navigation MUST preserve keyboard and assistive-technology operation and provide predictable opening, closing, and focus behavior.

## 16. Information Architecture

Information architecture SHOULD group related content, expose meaningful hierarchy, support discovery, and place recurring actions predictably. Names and grouping MUST align with Product concepts rather than internal implementation structure.

Interfaces SHOULD reveal enough context for a person to understand location, state, next action, and consequences without loading unrelated detail.

## 17. Component Model

Reusable components are stable interaction primitives, not page fragments copied for convenience. A component SHOULD solve one clear problem, expose a predictable public API, use shared tokens, support accessibility, document applicable states, avoid page-specific assumptions, and be independently testable.

Variation SHOULD be expressed deliberately. A new component is justified when an existing pattern cannot represent the required semantics without harmful complexity.

## 18. Component Categories

The component catalogue MAY organize patterns under Foundations, Actions, Inputs, Navigation, Feedback, Overlays, Data Display, Commerce, Layout, and Staff User concerns.

These are documentation categories, not mandatory Angular API or package names. Implementation naming remains governed by Architecture and coding standards.

## 19. Buttons

Button hierarchy MAY include primary, secondary, tertiary, destructive, and icon-only treatments where semantics justify them. Each button MUST have an understandable accessible name, visible focus, adequate target size, and explicit disabled and loading behavior where applicable.

Destructive intent MUST be clear before activation. Color alone MUST NOT distinguish destructive or stateful actions, and icon-only buttons require accessible labeling.

## 20. Links

Links navigate to a location or resource; buttons initiate an action. They MUST NOT be used interchangeably when their semantics differ.

Inline, navigation, and external links SHOULD provide recognizable styling, visible focus, meaningful labels, and visited treatment where it benefits orientation. External behavior SHOULD be communicated when it would otherwise surprise the person using the interface.

## 21. Form Design

Forms MUST use persistent labels and MUST NOT rely only on hint text. Applicable fields SHOULD include concise help, required or optional indication, validation, accessible error association, disabled or read-only distinction, logical grouping, and predictable keyboard behavior.

Forms SHOULD support appropriate autofill and mobile input modes, preserve entered values after recoverable errors, and avoid collecting data not required by the governing Product purpose.

## 22. Input Components

Supported input patterns MAY include text input, textarea, select, checkbox, radio, toggle, date input, quantity input, and search input where Product behavior requires them.

Each input MUST preserve native semantics or provide an equivalently accessible interaction. A custom control MUST NOT be introduced merely for visual novelty, and this document does not make every listed control mandatory.

## 23. Validation UX

Validation SHOULD occur when it can help correction without interrupting normal entry. Field-level errors SHOULD appear near the affected field, and a form-level summary SHOULD be provided when it materially improves recovery or accessibility.

Messages MUST be actionable, specific, and non-blaming. Validation MUST preserve safe user input and SHOULD NOT report errors so aggressively during typing that valid completion becomes difficult.

## 24. Loading States

Loading treatment SHOULD distinguish initial, page, inline, and action-specific work. Skeletons MAY support known content shapes; progress indicators MAY support bounded operations; descriptive status SHOULD accompany longer or consequential waits.

Loading MUST NOT imply completion, conceal failure indefinitely, or allow duplicate submission when an action is already pending.

## 25. Empty States

Empty states SHOULD explain why content is absent and offer a relevant next action when one exists. First-use emptiness, filtered results, no search results, unavailable content, and permission-limited views SHOULD remain distinguishable.

Decoration MAY support tone but MUST NOT replace useful explanation or recovery.

## 26. Error States

Error states MUST explain the problem in safe language, preserve context and input where possible, and offer recovery or retry only when safe. A correlation or support reference SHOULD be shown when it helps diagnosis without exposing sensitive implementation details.

Stack traces, Secrets, internal identifiers that increase exposure, and unverified technical claims MUST NOT appear in user-facing errors.

## 27. Success States

Success UI MUST represent confirmed state rather than submission alone. Payment, Order, Refund, Return, Account, and security-sensitive changes require evidence from the trusted owner before the interface communicates completion.

When outcome remains pending or unknown, the interface MUST state that honestly and explain the safe next step.

## 28. Payment UX — Strict Rules

Payment UI MUST preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Payment Transaction, Refund, and Refund Transaction.

A Payment Redirect and client-reported Payment success are not authoritative proof. Success UI MUST wait for trusted server-side confirmation based on validated Payment Provider evidence. Pending and unknown outcomes MUST remain explicit; retry guidance MUST avoid duplicate Payment effects; provider handoff and return MUST be understandable; and sensitive payment details MUST NOT be displayed or retained unnecessarily. Provider-specific branding requires separate approval.

## 29. Cart UX

Cart UI SHOULD make Product Variant, quantity, Price, Discount, availability, removal, and recovery clear. It MUST distinguish stale or changed information, preserve recoverable intent where practical, and support revalidation before Checkout.

Unavailable Product Variants require an understandable resolution path. Client-held Cart or availability data MUST NOT be treated as authoritative.

## 30. Checkout UX

Checkout SHOULD reveal required contact, delivery, Price, and Payment information progressively while keeping the current commitment and next action clear. It SHOULD preserve safe input after recoverable failure and explain provider handoff, interruption, and return behavior.

Submission controls MUST prevent accidental duplicates while work is pending. This standard does not prescribe an exact step count; Product Requirements and feature Specifications govern the supported flow.

## 31. Product Card

A Product card SHOULD present the primary Product image, Product name, current Price, legitimate Discount information where applicable, relevant availability, and a clear navigation or action affordance.

Cards SHOULD prioritize scanning and comparison without accumulating secondary data. The entire interaction boundary and any nested actions MUST remain unambiguous and keyboard accessible.

## 32. Product Detail

Product detail presentation SHOULD make imagery, Product identity, Product Variant selection, Price, Discount, availability, purchase action, relevant Product information, and governed delivery or Return context understandable.

The layout SHOULD support confident selection and purchase without exposing internal implementation mechanics or treating projections as authoritative state.

## 33. Product Variant Selection

Product Variant selection SHOULD make available options, current selection, unavailable states, and required choices explicit. It SHOULD preserve a valid selection through compatible UI changes and prevent ambiguous purchase intent.

Selection state MUST NOT imply authoritative Stock availability that the Inventory owner has not confirmed.

## 34. Price and Discount Presentation

The current payable Price MUST be prominent and server-authoritative. A comparison or original Price MUST be shown only when legitimate, and Discount presentation SHOULD explain the benefit without fabricated urgency or misleading framing.

The client MUST NOT calculate authoritative Price or Discount. Formatting MUST preserve Money and Currency meaning supplied by trusted Product behavior.

## 35. Inventory and Availability UI

Inventory UI MUST use Inventory, Stock, Stock Reservation, and Available-to-Sell consistently. Customer-facing availability SHOULD communicate useful verified truth without promising Stock that is not authoritative.

Stock Reservation mechanics SHOULD remain hidden from Customers unless Product behavior requires their explanation. Staff User interfaces MAY expose operational detail when authorized, relevant, and understandable.

## 36. Order UI

Order UI SHOULD show the current Order state, key dates, Order Items, Price and Payment context, Shipment information where available, relevant actions, and material failure or exception context.

The interface MUST use lifecycle states established by Product or an owning Specification and MUST NOT invent, merge, or imply transitions from presentation state alone.

## 37. Shipment UI

Shipment UI SHOULD communicate current status, tracking where available, Carrier information where Product permits it, delays, and delivery expectations. Provider terminology SHOULD be translated into stable Product language where appropriate.

The interface MUST distinguish an estimate from a confirmed event and MUST NOT guarantee behavior controlled by an External System.

## 38. Return and Refund UI

Return and Refund UI MUST distinguish the physical Return process from the financial Refund and Refund Transaction. Requested, pending, rejected, failed, and confirmed outcomes MUST remain distinguishable according to governing lifecycle definitions.

Refund completion MUST NOT be shown before authoritative confirmation. Available actions and policy explanations remain governed by Product and the applicable Specification.

## 39. Account UI

Account UI MAY cover profile information, Addresses, Order history, supported Return and Refund journeys, Preferences, and security-sensitive Account actions within current Product scope.

Sensitive actions SHOULD explain consequences and require appropriate reauthentication or confirmation when governed. Historical commercial records MUST NOT be represented as changed merely because editable Account data changes.

## 40. Staff User Experience

Staff User interfaces MAY use greater information density than customer interfaces while preserving clarity, accessibility, searchability, auditability, explicit state, and safe action boundaries.

Permissions and consequences SHOULD be visible where they affect work. Staff User convenience MUST NOT bypass domain rules, Human Approval Gates, or trusted Authorization.

## 41. Tables

Tables SHOULD be used for genuinely tabular comparison or operational data. They SHOULD provide clear headers, useful sorting and filtering where supported, appropriate pagination, responsive behavior, loading and empty states, accessible row relationships, and unambiguous row actions.

At constrained widths, a table MAY scroll, prioritize columns, or transform to another accessible representation without losing meaning. Cards or lists SHOULD be used when they communicate the content more effectively.

## 42. Data Density

Customer UI SHOULD remain calm and focused. Staff User interfaces MAY be denser when the task benefits from comparison, monitoring, or repeated operation.

Density MUST NOT reduce legibility, target accuracy, focus visibility, comprehension, or separation between destructive and routine actions.

## 43. Search

Search UI SHOULD provide a clear input, current query state, loading feedback, no-result handling, filters where supported, a result count where useful, accessible keyboard behavior, and a clear reset action.

Search results and indexes are projections and MUST NOT be presented as authoritative Product, Price, or Inventory state when the owning domain has newer truth.

## 44. Filters and Sorting

Active filters SHOULD remain visible and removable, defaults SHOULD be predictable, and reset behavior SHOULD be clear. Mobile treatment MUST preserve access without hiding applied state.

Sorting and filtering controls MUST communicate their effect and use accessible names, groupings, and state. Hidden filter state that materially changes results is prohibited.

## 45. Pagination and Infinite Loading

The pattern SHOULD match the task. Explicit pagination often supports Staff User and data workflows; customer discovery MAY use progressive or infinite loading when accessible, recoverable, and compatible with navigation history.

Infinite loading MUST NOT be mandatory, trap keyboard users, erase the current position unexpectedly, or prevent access to meaningful page content.

## 46. Cards

Cards SHOULD group one coherent subject with a clear hierarchy and interaction boundary. Primary and secondary actions MUST remain distinguishable, and nested conflicting click targets SHOULD be avoided.

Interactive cards MUST provide correct semantics, focus behavior, and accessible names rather than depending on pointer behavior alone.

## 47. Lists

Lists SHOULD use semantic list structures when content forms a collection. List rows SHOULD expose predictable primary information, secondary metadata, and actions without making the entire row ambiguous.

Selection, expansion, reordering, and bulk behavior require explicit accessible state where supported.

## 48. Modals and Dialogs

Dialogs SHOULD interrupt only when immediate attention or a bounded decision is justified. They MUST have an accessible title, managed focus, keyboard support, a clear close path, and focus restoration when closed.

Destructive confirmation MUST explain the consequence. Large or multi-stage workflows SHOULD use a page or another persistent context rather than an oversized dialog.

## 49. Drawers and Side Panels

Drawers and side panels MAY support contextual secondary workflows when preserving the underlying context is useful. They MUST provide appropriate dialog or region semantics, focus management, keyboard closure, and responsive behavior.

On constrained screens they SHOULD adapt without obscuring critical context or creating nested interaction traps.

## 50. Tooltips

Tooltips SHOULD be used sparingly for concise supplementary explanation. Critical instructions, errors, state, or consequences MUST NOT exist only in a tooltip.

Tooltip content MUST be available through keyboard and assistive technology, not only pointer hover, and SHOULD remain dismissible without blocking nearby content.

## 51. Toasts and Notifications

Toasts MAY communicate transient, non-critical feedback. Critical failure, pending work, or information requiring action MUST remain available in persistent context outside the toast.

Transient notifications SHOULD be announced appropriately without repeatedly interrupting assistive technology. They MUST NOT be the sole evidence of a consequential result.

## 52. Confirmation Patterns

Destructive or difficult-to-reverse actions SHOULD use confirmation proportional to Risk. Confirmation SHOULD identify the action, affected subject, consequence, and whether recovery is possible.

Harmless and easily reversible actions SHOULD avoid unnecessary confirmation. Repeated low-value prompts create confirmation fatigue and weaken attention to genuine Risk.

## 53. Destructive Actions

Destructive actions MUST use clear, specific language and SHOULD be visually separated from routine primary actions. Material actions require confirmation and MAY require stronger verification or a Human Approval Gate where governing policy requires it.

The Design System MUST NOT invent approval authority or imply that visual confirmation replaces trusted Authorization.

## 54. Status Indicators

Status indicators MUST communicate meaning through text and MAY add an icon or shape. Color may reinforce status but MUST NOT be the only signal.

Status labels MUST come from the owning Product lifecycle or Specification. Visual state MUST NOT convert pending, unknown, or failed outcomes into success.

## 55. Badges and Tags

Badges and tags SHOULD represent concise status or metadata rather than long content or primary instructions. Their hierarchy SHOULD remain quieter than the main subject and action.

Excessive badges SHOULD be avoided because they obscure priority and reduce scanability.

## 56. Icons

Icons SHOULD use a coherent visual style, reinforce meaning, and include accessible names when they communicate an action or information not already expressed in text.

Unfamiliar actions SHOULD retain visible labels. No icon library is mandated until an Approved implementation decision establishes one.

## 57. Imagery

Fashion imagery SHOULD use consistent presentation ratios by context, sufficient visual quality, intentional cropping, responsive loading, useful alternative text, and a safe fallback when media is unavailable.

Product imagery SHOULD support merchandising without harming accessibility, layout stability, or critical-path performance. Rights, safety, and content ownership remain governed outside this document.

## 58. Image Loading

Image delivery SHOULD use responsive sources, defer below-fold media where appropriate, reserve dimensions or aspect ratio to reduce layout shift, and use progressive loading when it improves perceived performance.

The visible fallback MUST preserve layout and comprehension. Specific Angular or browser APIs belong in implementation standards rather than this Design System.

## 59. Motion

Motion SHOULD explain change, reinforce hierarchy, or preserve spatial understanding. It SHOULD be restrained, performant, interruptible, and consistent.

Interfaces MUST respect reduced-motion preferences. Decorative motion that delays work, distracts from critical state, or harms accessibility SHOULD NOT be used.

## 60. Focus and Keyboard Interaction

Every interactive element MUST be keyboard reachable, display visible focus, follow a logical order, preserve expected keyboard semantics, and avoid unexpected focus loss.

Dynamic interfaces MUST move or restore focus deliberately when context changes. Focus styling MUST remain visible across surfaces and interaction states.

## 61. Accessibility

Interfaces MUST follow semantic HTML, keyboard, focus, labeling, description, validation, contrast, target-size, reduced-motion, screen-reader, and status-announcement requirements established by Product and applicable standards.

The current Product and Architecture baseline targets WCAG 2.2 AA; this statement defines the required target, not a claim of certification. Aesthetic preference MUST NOT weaken accessibility.

## 62. Content Design

UI copy SHOULD be concise, direct, specific, human, action-oriented, and consistent with canonical terminology. It SHOULD explain outcomes and recovery without blame or unnecessary technical language.

Labels SHOULD describe the action or information rather than rely on surrounding visual context.

## 63. Tone

Customer copy SHOULD feel clear, calm, confident, and respectful. Staff User copy SHOULD remain precise, operational, and explicit about consequences.

High-stakes Payment, security, Refund, destructive, and failure flows SHOULD avoid jokes, exaggerated reassurance, or language that minimizes uncertainty.

## 64. Error Copy

Error messages SHOULD explain what happened, what can be done next, whether submitted data was retained, and whether retry is safe. When the exact cause is unknown, the message MUST NOT invent certainty.

Internal exceptions, stack traces, provider payloads, Secrets, and Sensitive Data MUST NOT be exposed.

## 65. Payment Copy

The interface MUST NOT state “Payment successful” until authoritative confirmation exists. Pending or unknown Payment state MUST be explicit and SHOULD explain when the Customer may safely wait, refresh, retry, or seek support.

Copy MUST NOT encourage repeated unsafe Payment attempts or describe a Payment Redirect as proof.

## 66. Empty and No-Result Copy

Copy SHOULD distinguish an empty Account or Order history, zero search results, results excluded by filters, and unavailable catalogue content. It SHOULD provide a useful next step when Product behavior supports one.

Messages MUST NOT imply that data does not exist when access, filters, synchronization, or failure may explain the result.

## 67. Localization Readiness

Design SHOULD allow for longer text, alternate languages, Currency and date formats, Address variation, and right-to-left layout if future Product strategy requires it.

This readiness MUST NOT be represented as current international or multilingual Product scope. Current commitments remain governed by `PRODUCT.md`.

## 68. Currency and Money Display

Money and Price presentation MUST show a clear Currency, use consistent formatting, and use server-authoritative values. UI display logic MUST NOT derive monetary values through floating-point calculations.

The current Approved Product context uses ZAR. This Design System does not create current multi-currency support.

## 69. Date and Time Display

Dates and times SHOULD use a consistent, unambiguous format appropriate to the Customer or Staff User context. Time-zone context SHOULD be shown when omission could materially change interpretation.

The design MUST NOT invent cross-market time-zone requirements beyond current Product scope.

## 70. Accessibility States

Each reusable component SHOULD define only the states that make semantic sense, including default, hover, focus, active, disabled, loading, error, success, selected, and expanded or collapsed behavior where applicable.

State documentation MUST include accessible names, announcements, contrast, keyboard behavior, and interaction consequences where relevant.

## 71. Interaction State Model

Interaction state MUST make it clear whether an action is available, unavailable, pending, complete, or failed. Visual, textual, and programmatic state MUST remain synchronized.

A component MUST NOT conceal ongoing work, allow conflicting duplicate actions, or report completion before its trusted outcome is known.

## 72. Optimistic UI

Optimistic UI MAY be used when rollback is safe, the expected outcome is highly reliable, and temporary false state cannot create harmful customer or operational understanding.

Optimistic success MUST NOT be used for Payment, Refund, security-sensitive Account changes, or other irreversible or high-Risk operations unless authoritative behavior makes the representation demonstrably safe.

## 73. Skeletons vs Spinners

Skeletons MAY represent known content structure during initial retrieval. Spinners or progress indicators MAY represent bounded actions where the content shape is not useful.

Neither pattern may mask indefinite failure, replace useful status text for consequential waits, or imply that data exists before its outcome is known.

## 74. Progressive Disclosure

Complex, advanced, or infrequently used options SHOULD be revealed when relevant rather than displayed by default. Disclosure controls MUST communicate their state and remain keyboard and assistive-technology accessible.

Critical Price, Payment, security, consent, validation, or destructive consequences MUST NOT be hidden behind obscure disclosure.

## 75. Consistency Across Journeys

Common patterns SHOULD behave consistently across Product, Cart, Checkout, Order, Account, and Staff User tools. Labels, feedback, action hierarchy, focus behavior, and state representation SHOULD remain stable for the same concept.

A deliberate difference requires a contextual or semantic reason rather than visual preference alone.

## 76. Accessibility of Commerce States

Availability, Price, Discount, Payment, Order, Shipment, Return, and Refund changes MUST be communicated through accessible text and programmatic state, not only visual styling.

Announcements SHOULD be timely without becoming repetitive or disruptive, and they MUST preserve the distinction between pending and confirmed outcomes.

## 77. Customer Trust Signals

Trust SHOULD come from accurate state, transparent Price, secure behavior, predictable recovery, clear policies, and useful communication. Visual trust treatment MUST correspond to real controls or verified information.

Fake scarcity, manipulative urgency, unverified reassurance, and ornamental security claims are prohibited.

## 78. Dark Patterns

The following are prohibited:

- fake scarcity or manipulative countdowns;
- hidden fees or intentionally obscured Price;
- confusing opt-outs or deceptive action hierarchy;
- forced consent unrelated to the requested action;
- misleading Payment confirmation; and
- making supported cancellation or Return actions intentionally difficult to find.

## 79. Security-Sensitive UI

Security-sensitive UI MUST minimize exposed information, rely on trusted server results, avoid revealing Account existence unnecessarily, and support MFA and reauthentication flows where governed.

Sensitive values MUST be masked or omitted according to purpose. Client presentation MUST NOT be treated as Authorization or as proof that a security action succeeded.

## 80. Authorization and Visibility

The UI MAY hide or disable unavailable actions for usability, but visibility MUST NOT be treated as Authorization enforcement. Trusted backend boundaries remain authoritative.

Staff User interfaces MUST preserve canonical Role and Permission meaning and SHOULD explain unavailable actions when doing so is safe and useful.

## 81. Privacy UX

Interfaces collecting or showing personal information MUST minimize data, support the Approved purpose, avoid unnecessary exposure, and provide safe viewing and editing behavior.

Sensitive Data SHOULD be concealed by default where full display is unnecessary. Privacy explanations SHOULD be available at the point where they affect an informed choice.

## 82. Consent and Preference UX

Where Product includes Consent or Preference flows, wording MUST be clear, choices understandable, and defaults governed by Product and applicable legal requirements. Withdrawal or editing SHOULD be discoverable where supported.

The Design System MUST NOT invent consent categories, lawful bases, or Product commitments.

## 83. Staff User Destructive Operations

High-impact Staff User actions such as Refund, Stock Adjustment, Order intervention, and Role or Permission change require interaction protection proportional to Risk and any governing Human Approval Gate.

The interface SHOULD identify the affected Resource, consequence, current state, and requested action. It MUST NOT invent approval authority or allow visual confirmation to bypass domain and security controls.

## 84. Auditability UX

Where operational actions create Audit Records, the interface SHOULD provide enough authorized context to understand the action, actor where appropriate, time, outcome, and affected Resource.

Audit presentation MUST minimize Sensitive Data, distinguish current state from historical evidence, and avoid implying that UI history replaces the canonical Audit Trail.

## 85. Notifications and Messaging

Where Product exposes communication state, UI MAY distinguish requested, pending, sent, failed, or other states established by the owning Specification. It SHOULD provide safe recovery or escalation where supported.

The Design System does not create communication channels, delivery guarantees, or lifecycle states.

## 86. Design System Documentation

Each reusable component or pattern SHOULD document its purpose, when to use it, when not to use it, anatomy, states, accessibility, behavior, content guidance, responsive behavior, and implementation references where appropriate.

Documentation MUST distinguish normative rules, examples, and current implementation. It SHOULD link governing Product and Specification sources instead of copying them.

## 87. Component Examples

Examples MUST use safe synthetic data, canonical terminology, and states supported by current Product or an owning Specification. Real PII, payment credentials, Secrets, and production identifiers are prohibited.

Examples illustrate the Design System and MUST NOT create hidden Product Requirements or claim unsupported behavior.

## 88. Figma / Design Tool Role

If Figma or another design tool is adopted, it MAY hold visual source material and component compositions. Repository documentation remains the canonical governance source, while Product and Architecture retain their assigned authority.

Inaccessible or offline design assets MUST NOT be the sole source of critical behavior, state, accessibility, or decision rationale. This document does not claim that a particular design tool currently exists.

## 89. Design-to-Code Handoff

Handoff SHOULD identify the component or pattern, applicable states, responsive behavior, content, accessibility, interaction, edge cases, assets, and links to relevant Product Requirements and Specifications.

Screenshots alone are insufficient for behavior. Material uncertainty and deferred decisions MUST be visible rather than silently resolved by implementation.

## 90. Design Review

Design review SHOULD consider Product correctness, usability, accessibility, consistency, responsive behavior, failure and recovery, content, security-sensitive behavior, and implementation feasibility.

Review depth SHOULD be proportional to Risk, reuse, customer or operational impact, and difficulty of reversal.

## 91. Engineering Review of Design

Engineering SHOULD review material design decisions that affect Architecture, performance, accessibility, data ownership, security, provider behavior, complex state, or implementation cost.

Engineering review MUST NOT redefine Product behavior through implementation convenience. Material unresolved choices require the applicable governance or Decision Record.

## 92. Accessibility Review

Material new reusable components SHOULD receive accessibility review and applicable automated and manual testing before broad reuse. Review SHOULD cover semantics, keyboard interaction, focus, labels, announcements, contrast, target size, zoom, and reduced motion.

Known accessibility failures MUST NOT be hidden by visual approval.

## 93. Visual Regression

Visual regression testing MAY support token, layout, state, and component stability across supported viewports. Baselines MUST be intentional, reviewable, and updated only when the visual change is approved.

Visual comparison MUST NOT replace functional, interaction, responsive, or accessibility testing.

## 94. Design Testing

Verification MAY include Component Tests, accessibility tests, responsive tests, interaction tests, visual regression, and usability validation where the Risk or uncertainty justifies it.

Testing MUST follow `TESTING-STANDARDS.md`, use meaningful states, and distinguish implementation evidence from subjective preference.

## 95. Design Versioning

Design System changes SHOULD be versioned or otherwise durably traceable. Breaking token, component, or pattern changes require impact analysis, migration consideration, and communication to affected consumers.

This document does not establish a package-version policy; implementation packaging requires its own approved convention.

## 96. Deprecation

Deprecated patterns and components SHOULD be marked clearly, identify their replacement, stop receiving new adoption, and remain only as long as a safe migration requires.

Deprecation MUST NOT invent unsupported document lifecycle metadata. Historical guidance MUST NOT remain discoverable as current design authority.

## 97. Design Exceptions

A Design Exception SHOULD record the exact rule or pattern, reason, affected scope, accessibility impact, Product impact, implementation impact, accountable owner, authorized reviewer or approver, and expiry or remediation when temporary.

A Design Exception MUST NOT waive Product, Architecture, security, accessibility, testing, coding, or other governing Requirements. Any required exception owned by another standard remains separately required.

## 98. Forbidden Design Practices

The following are prohibited:

- fake Payment success, fake scarcity, hidden fees, and misleading urgency;
- client-only Authorization or inaccessible critical interaction;
- color-only state and hint-only field labels;
- destructive actions without protection proportional to Risk;
- real PII or payment data in examples;
- arbitrary one-off visual values where governed tokens exist;
- copied components that diverge from canonical patterns;
- screenshots as the sole source of critical behavior;
- hidden errors or disabled accessibility used to match aesthetics; and
- design that invents current Product scope.

## 99. Design Compliance Matrix

| Concern | Governing Source | Design Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `AGENTS.md` | Follow repository authority and the Decision Hierarchy | Governance review |
| Terminology | `GLOSSARY.md` | Use canonical language | Terminology review |
| Vision | `VISION.md` | Express durable direction without creating scope | Strategic alignment review |
| Product | `PRODUCT.md` | Represent Approved behavior accurately | Product review and Requirement traceability |
| Architecture | `ARCHITECTURE.md` | Remain compatible with frontend architecture | Architecture and engineering review |
| Security | `SECURITY-STANDARDS.md` | Preserve trusted boundaries and safe presentation | Security review |
| Testing | `TESTING-STANDARDS.md` | Define verifiable states and behavior | Test evidence |
| Coding | `CODING-STANDARDS.md` | Remain implementable without redefining code rules | Code and design review |
| Engineering Principles | `ENGINEERING-PRINCIPLES.md` | Apply clarity, correctness, and reversibility | Decision rationale |
| Documentation | `DOCUMENTATION-STANDARDS.md` | Keep patterns current and traceable | Documentation review |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Represent only authoritative Payment outcomes | Provider evidence and Payment tests |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Represent verified availability without redefining ownership | Inventory and concurrency evidence |
| Order | `PRODUCT.md`; owning Specification | Preserve governed lifecycle and commercial context | Product and domain tests |
| Identity | `SECURITY-STANDARDS.md`; `ARCHITECTURE.md` | Preserve Identity, Session, Role, and Permission boundaries | Authorization and accessibility tests |
| Accessibility | `PRODUCT.md`; applicable standards | Make interaction perceivable, operable, understandable, and robust | Automated and manual review |
| Content | `PRODUCT.md`; `GLOSSARY.md` | Use accurate, calm, actionable language | Content review |
| Staff User | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Support safe, explicit operational work | Permission, audit, and usability evidence |
| AI | `AGENTS.md`; `SECURITY-STANDARDS.md` | Prevent model output from becoming UI authority | Human review and trusted enforcement |
| Exceptions | Applicable governing standard | Document deviation without weakening higher authority | Approval, impact, expiry, and remediation |

### Compliance Interpretation

`DESIGN-SYSTEM.md` governs UI and UX consistency and interaction principles. It MUST NOT override Product behavior, Architecture, security, testing, coding, accessibility, or other higher-authority Requirements.

## 100. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/VISION.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`

## 101. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-08-12 | Draft | Established the initial repository-wide Design System covering design principles, tokens, responsive layout, reusable components, commerce UX, Payment, Inventory, accessibility, content, Staff User patterns, design governance, testing, and exceptions. |
