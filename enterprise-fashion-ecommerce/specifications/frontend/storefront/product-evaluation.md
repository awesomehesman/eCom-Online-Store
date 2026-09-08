---
title: Product Evaluation Specification
version: 0.1.0
status: Draft
owner: Product and Engineering
last_updated: 2026-09-08
authoritative: false
---

# Product Evaluation Specification

## 1. Purpose

This Specification defines implementation-neutral requirements for customer-facing evaluation of governed Product and Product Variant evidence before a purchase journey.

This document uses scope code `FPE`. While Draft, it is not yet normative. If Approved, its Requirements are normative only within Product Evaluation frontend scope and are not repository-wide authority. It remains subordinate to governing sources, Product and Business Requirements, Approved Domain Specifications, and applicable Approved FEB, FSC, and FCD Requirements, and resolves no Open Product Decision.

## 2. Scope and Authority

FPE owns only Product-evaluation presentation behavior: evaluation context, presentation of governed Product, Product Variant, Attribute, Product Media, Price, availability, delivery and policy evidence, Product Variant selection presentation, evaluation-specific state, accessibility, degradation, telemetry, and abstract Contract expectations.

FPE does not own Product, Product Variant, Product Media, publication, visibility, lifecycle or structural sellability truth; Pricing, Inventory, Category, Search, CMS, Customer, Identity, Cart, Checkout, Payment, Order, Shipping, Return, Refund, reviews, ratings, Consent, Authorization, merchandising, personalization, fraud, tax, commercial or Product-policy truth. Presentation, selection state, route state, cached evidence, or a destination reference cannot transfer that authority to FPE.

Shared frontend behavior remains owned by FEB; shell and homepage behavior by FSC; catalogue discovery by FCD; account and Identity behavior by FCA; Cart and purchase behavior by FCP; post-purchase behavior by FPP; Administration by FAD; and Reporting by FRP.

## 3. Terminology and Product Evaluation Model

Canonical terms retain their meanings from `GLOSSARY.md`. For FPE only, **evaluation context** describes non-authoritative frontend state used to understand an identified Product and applicable Product Variant evidence, while **selection presentation** describes the frontend representation of an intended Product Variant choice. These are local descriptions, not repository-wide terms, Domain entities, purchase commitments, or lifecycle states.

Presented Product evidence remains owned by its source. A Product or Product Variant being displayed, selected, prominent, priced, described as available, or associated with a purchase affordance does not independently establish publication, visibility, structural sellability, Stock, Available-to-Sell, Customer eligibility, Authorization, or final purchasability.

## 4. Requirements

### REQ-FPE-001 — Lifecycle, Authority, and Scope

FPE MUST govern only Product Evaluation frontend behavior under scope `FPE`, preserve governing-source precedence, FEB, FSC and applicable FCD obligations, Product and Business Requirements, and Approved Domain authority, and MUST NOT treat this Draft as normative or repository-wide authority before approval.

### REQ-FPE-002 — Frontend Inheritance

FPE MUST consume every materially applicable Approved FEB, FSC, and FCD Requirement without copying, weakening, conflicting with, or transferring ownership of shared frontend, shell, or discovery behavior.

### REQ-FPE-003 — Domain and Journey Non-Authority

FPE MUST NOT own or redefine source-Domain truth or FSC, FCD, FCA, FCP, FPP, FAD, or FRP behavior; evaluation presentation or a downstream affordance MUST NOT transfer that authority.

### REQ-FPE-004 — Governed Evaluation Entry

FPE MUST enter an evaluation context only for an identified governed Product destination while preserving safe incoming discovery context without prescribing a route, URL, label, layout, Component, or navigation policy.

### REQ-FPE-005 — Product Identity and Content

Product evaluation MUST preserve governed Product identity, descriptive content, provenance, applicability, and current Product context and MUST NOT allow presentation, local content, or CMS evidence to create or rewrite Product truth.

### REQ-FPE-006 — Publication and Visibility Evidence

FPE MUST present a Product or Product Variant as currently evaluable only when applicable governed Product publication and visibility eligibility evidence supports that presentation; rendering or cached evidence MUST NOT independently publish or expose it.

### REQ-FPE-007 — Product Variant Association

Presented Product Variant evidence MUST retain stable Product Variant identity, its owning Product association, and applicable governed context without merging, substituting, or fabricating Product Variant truth.

### REQ-FPE-008 — Product Attribute Evidence

FPE MUST preserve governed descriptive and variant-defining Attribute meaning, values, association, and applicability without defining Attribute standards, normalization, allowed values, taxonomy, or Product Variant uniqueness.

### REQ-FPE-009 — Product Variant Selection Presentation

Where selection is applicable, FPE MUST make available governed options, current intended selection, unavailable options, and required choices understandable without treating selection presentation as Product Variant, Inventory, Cart, or purchase authority.

### REQ-FPE-010 — Selection Completeness and Invalid Combinations

FPE MUST prevent an ambiguous or incomplete selection from being represented as ready for purchase handoff and MUST present invalid, incompatible, stale, or unavailable combinations with an understandable governed recovery path.

### REQ-FPE-011 — Selection Preservation and Supersession

A valid intended selection MAY be preserved across compatible evaluation changes, but stale, cancelled, reordered, incompatible, or superseded evidence MUST NOT overwrite newer intent or remain represented as valid.

### REQ-FPE-012 — Product Media Evidence

FPE MUST preserve Product Media identity, Product or Product Variant association, governed ordering where supplied, accuracy, publication, rights, accessibility, and freshness, and MUST NOT make rendered or cached media authoritative.

### REQ-FPE-013 — Media Evaluation Interaction

Where multiple or enhanced media views are supported, their interaction MUST preserve equivalent Product meaning, current media context, input-modality access, and safe failure without prescribing a gallery, zoom, transformation, provider, or media-delivery mechanism.

### REQ-FPE-014 — Price, Money, and Currency Evidence

FPE MUST present current governed Pricing evidence with explicit Money and Currency context, source, applicability, and freshness and MUST NOT calculate authoritative Price, tax, delivery charge, savings, or totals.

### REQ-FPE-015 — Discount and Promotion Evidence

Discount, Promotion, Voucher, comparison Price, or savings evidence MAY be presented only when governed Pricing evidence establishes it, and FPE MUST NOT invent eligibility, stacking, urgency, benefit, or promotional policy.

### REQ-FPE-016 — Pricing Change and Uncertainty

Stale, changed, partial, conflicting, unavailable, or uncertain Pricing evidence MUST remain distinguishable from current confirmed Price and support governed revalidation or truthful degradation before purchase handoff.

### REQ-FPE-017 — Inventory and Availability Evidence

Availability presentation MUST preserve governed Product Variant, source, applicability, freshness, and uncertainty and MUST NOT establish Stock, Available-to-Sell, Stock Reservation, Product visibility, purchase authorization, or final purchasability.

### REQ-FPE-018 — Availability Messaging Boundary

Where Customer-facing availability messaging is presented, it MUST reflect governed Inventory evidence without FPE selecting low-stock, out-of-stock, back-order, pre-order, threshold, wording, suppression, urgency, or replenishment policy.

### REQ-FPE-019 — Structural Sellability and Purchasability Non-Authority

Product-owned structural sellability evidence MAY support evaluation, but FPE MUST NOT represent it, selection completeness, Price, or availability evidence as sufficient proof of final purchasability.

### REQ-FPE-020 — Quantity Presentation Boundary

Where evaluation includes quantity intent, FPE MUST present applicable governed constraints and validation context without creating Cart intent, reserving Stock, defining quantity policy, or claiming that frontend validation authorizes purchase.

### REQ-FPE-021 — Delivery and Shipping Evidence

Where delivery, fulfilment, service, area, timing, or charge evidence is presented, FPE MUST preserve its governed source, destination context, applicability, Currency, freshness, and uncertainty without owning Shipping policy, quotation, commitment, or Shipment truth.

### REQ-FPE-022 — Product and Policy Content Boundary

Product, commercial, delivery, Return, support, or policy content presented during evaluation MUST derive from governed Product, CMS, Pricing, Shipping, or policy evidence and MUST NOT create unsupported claims or policy substance.

### REQ-FPE-023 — Reviews and Ratings Boundary

FPE MUST NOT present reviews, ratings, aggregates, submission, moderation, or eligibility as governed capability unless applicable Approved governance explicitly establishes Product-review support; Product-review support and policy remain unresolved.

### REQ-FPE-024 — Discovery Context and Return

FPE MUST preserve safe useful FCD context for supported return navigation without allowing route, history, filter, result position, or cached discovery state to become Product, Search, Category, or Authorization truth.

### REQ-FPE-025 — Purchase-Journey Handoff

FPE MAY hand off an identified Product Variant, quantity intent where applicable, and governed evaluation evidence to FCP, but MUST NOT create or mutate Cart, reserve Stock, authorize Checkout, initiate Payment, or claim purchase success.

### REQ-FPE-026 — Freshness and Authoritative Revalidation

Where current truth is material, FPE MUST expose or consume sufficient source, applicability, freshness, and revalidation context and MUST NOT represent stale Product, Product Variant, Price, Inventory, delivery, CMS, or policy evidence as current confirmation.

### REQ-FPE-027 — Asynchronous Supersession Safety

Stale, cancelled, reordered, or superseded Product, Product Variant, selection, media, Price, availability, delivery, or policy outcomes MUST NOT overwrite newer relevant evaluation intent or presentation state.

### REQ-FPE-028 — Evaluation Presentation States

Each material evaluation region MUST apply distinguishable initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery, and confirmed states where applicable without defining a business lifecycle or mandatory universal traversal.

### REQ-FPE-029 — Withdrawn, Invalid, or Unavailable Evaluation

A withdrawn, unpublished, invisible, invalid, inaccessible, stale, unavailable, or unsupported Product or Product Variant evaluation MUST produce a truthful distinguishable outcome and safe permitted recovery without retaining unsupported claims or disclosing protected Resource existence.

### REQ-FPE-030 — Governed Recovery

Where recovery is available, FPE MUST preserve known evaluation context, distinguish retry from changed intent, reject obsolete outcomes, and invoke only governed revalidation or recovery without defining backend retry, replay, idempotency, or reconciliation mechanics.

### REQ-FPE-031 — Responsive Evaluation Outcome

Product information, media, Product Variant choices, Price, availability, policy evidence, status, and essential actions MUST remain understandable and operable across supported viewport, orientation, reflow, zoom, localization, and content-extreme conditions without FPE defining breakpoints or layouts.

### REQ-FPE-032 — Input-Modality Equivalence

Evaluation content, media, Product Variant choices, quantity intent, navigation, recovery, and purchase handoff MUST remain operable through supported keyboard, pointer, touch, and assistive-technology paths without requiring a single input modality.

### REQ-FPE-033 — Focus and Dynamic Evaluation

Product Variant changes, media changes, Price or availability updates, validation, failure, recovery, and handoff MUST preserve or move focus predictably and communicate the resulting evaluation context without focus theft or loss of meaningful position.

### REQ-FPE-034 — Accessible Structure, State, and Status

Product evaluation MUST expose coherent structure, headings, names, relationships, selected and unavailable state, Price and Currency meaning, availability, validation, loading, failure, uncertainty, recovery, and change status without relying only on color, position, motion, imagery, or visual change.

### REQ-FPE-035 — Content and Media Resilience

Evaluation MUST preserve essential comprehension and operation under long, short, absent, localized, delayed, invalid, stale, partial, or failed Product content and media without clipping material meaning, fabricating substitutes, or blocking unrelated evidence.

### REQ-FPE-036 — Security, Privacy, Consent, and Isolation

FPE MUST render governed and query-derived content safely, minimize Sensitive Data in client state, routes, errors, logs, telemetry, and Analytics Events, expose no Secrets, preserve Principal, Session, Customer and Resource isolation, and MUST NOT infer Consent from evaluation interaction or context.

### REQ-FPE-037 — Authorization and Resource Concealment

Protected evaluation reads and handoffs MUST preserve current server-side contextual Authorization and Resource concealment; frontend visibility, selection, identifiers, errors, timing, Role labels, Permissions, Claims, or Scope MUST NOT authorize access or disclose inaccessible Resource existence.

### REQ-FPE-038 — Bounded Frontend Work

Product content, Product Variant options, media, related evaluation evidence, reactive work, and background activity MUST remain bounded by governed inputs and performance evidence without defining numerical collection limits, media limits, timing budgets, or client mechanisms.

### REQ-FPE-039 — Degradation and Failure Containment

Failure or degradation of optional media, secondary content, policy evidence, analytics, or another non-essential region MUST NOT silently block unrelated critical Product evaluation, and degraded outcomes MUST remain truthful about known completeness and uncertainty.

### REQ-FPE-040 — Telemetry and Evidence Separation

Material evaluation failures, selection outcomes, recovery, and Contract-boundary failures MUST produce safe frontend operational telemetry and preserve governed correlation evidence. Operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records MUST remain distinct; none substitutes for another or creates authoritative Domain history.

### REQ-FPE-041 — Analytics Boundary

Product-evaluation Analytics Events MUST remain non-authoritative observations, respect applicable Consent and Sensitive Data constraints, and MUST NOT define provider, taxonomy, canonical event name, payload, attribution, personalization, ranking, or Product truth.

### REQ-FPE-042 — Feature-Flag Safety

Every reachable feature-flag or progressive-delivery state affecting evaluation MUST preserve valid evidence, truthful outcomes, context, accessibility, security, privacy, inherited obligations, and Domain authority without selecting rollout policy or implementation.

### REQ-FPE-043 — Abstract Contract Boundary

Future FPE Contracts MUST expose only necessary bounded and versioned Product, Product Variant, Attribute, media, selection, Price, availability, delivery, policy, source, applicability, freshness, outcome, and correlation semantics; FPE MUST NOT define routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, transport, or providers.

### REQ-FPE-044 — Product-Policy and Implementation Neutrality

FPE MUST keep unresolved Attribute, media, Price-display, Promotion, tax, delivery, availability-message, reviews, analytics, launch, personalization, merchandising, route, visual, provider, protocol, caching, numerical, and implementation choices outside its normative behavior.

### REQ-FPE-045 — Verification Coverage

FPE verification MUST cover applicable authority, frontend inheritance, Product evaluation, Product Variant and Attribute evidence, selection, media, Price, availability, delivery, policy, reviews boundary, context, handoff, freshness, supersession, states, recovery, accessibility, responsiveness, security, privacy, Consent, boundedness, degradation, telemetry, feature flags, Contracts, and policy neutrality.

## 5. Acceptance Criteria

| Requirement | Acceptance Criterion |
| --- | --- |
| REQ-FPE-001 | Lifecycle evidence confirms `0.1.0 Draft`, `authoritative: false`, scope `FPE`, Draft non-normativity, governing precedence, inherited frontend obligations, and preserved Product, Business Requirement and Approved Domain authority. |
| REQ-FPE-002 | Verification maps every materially applicable FEB, FSC, and FCD Requirement to FPE and finds none copied, weakened, conflicted with, or ownership-transferred. |
| REQ-FPE-003 | Boundary review finds no FPE-owned or redefined source-Domain truth, redefined FSC or FCD behavior, detailed FCA, FCP, FPP, FAD or FRP behavior, or authority transfer through presentation or handoff. |
| REQ-FPE-004 | A governed Product destination opens a meaningful evaluation context with safe incoming discovery context while tests require no fixed route, URL, label, layout, Component, or navigation policy. |
| REQ-FPE-005 | Every material Product presentation resolves to governed identity, content, provenance, applicability, and current context; local or CMS presentation cannot create or rewrite Product truth. |
| REQ-FPE-006 | Published and visible evaluation states require applicable current Product evidence, and negative tests show rendering, routes, or cached state cannot publish or expose an ineligible Product or Product Variant. |
| REQ-FPE-007 | Product Variant presentations retain stable identity and owning Product association, and substitutions, merges, or fabricated contexts are rejected or represented as invalid. |
| REQ-FPE-008 | Attributes retain governed meaning, values, association, and applicability while inspection finds no FPE-defined standards, normalization, allowed values, taxonomy, or uniqueness rules. |
| REQ-FPE-009 | Applicable choices expose understandable available, selected, unavailable, and required state, and manipulating selection presentation establishes no Product Variant, Inventory, Cart, or purchase authority. |
| REQ-FPE-010 | Incomplete, ambiguous, invalid, incompatible, stale, and unavailable combinations cannot appear handoff-ready and provide a governed understandable recovery path where permitted. |
| REQ-FPE-011 | Compatible changes preserve valid intended selection, while stale, cancelled, reordered, incompatible, or superseded evidence cannot overwrite newer intent or remain valid. |
| REQ-FPE-012 | Product Media evidence retains identity, Product or Product Variant association, governed ordering where supplied, accuracy, publication, rights, accessibility, and freshness; rendered or cached media cannot independently establish or alter authoritative Product Media evidence. |
| REQ-FPE-013 | Supported media interactions preserve meaning, current context, equivalent input access, and safe failure without tests depending on gallery, zoom, transformation, provider, or delivery design. |
| REQ-FPE-014 | Every Price presentation retains governed amount, Currency, source, applicability, and freshness, and no client calculation becomes authoritative Price, tax, delivery charge, savings, or total. |
| REQ-FPE-015 | Each Discount, Promotion, Voucher, comparison Price, or savings presentation is backed by applicable governed Pricing evidence, and unsupported eligibility, stacking, urgency, benefit, or promotional claims are absent. |
| REQ-FPE-016 | Tests distinguish current Price from stale, changed, partial, conflicting, unavailable, and uncertain evidence and require revalidation or truthful degradation before handoff. |
| REQ-FPE-017 | Availability retains governed Product Variant, source, applicability, freshness, and uncertainty while proving none of Stock, Available-to-Sell, Stock Reservation, Product visibility, purchase authorization, or final purchasability. |
| REQ-FPE-018 | Customer-facing availability messages remain evidence-backed, and review finds no FPE-selected low-stock, out-of-stock, back-order, pre-order, threshold, wording, suppression, urgency, or replenishment policy. |
| REQ-FPE-019 | Verification confirms structural sellability remains Product-owned evidence and that structural sellability, selection completeness, Price, or availability evidence cannot individually or collectively establish final purchasability in FPE. |
| REQ-FPE-020 | Quantity presentation exposes applicable governed constraints and validation context, while tests confirm that quantity intent cannot create Cart intent, reserve Stock, define quantity policy, or authorize purchase. |
| REQ-FPE-021 | Applicable delivery, fulfilment, service, area, timing, and charge presentations retain governed source, destination context, applicability, Currency where relevant, freshness, and uncertainty while establishing no Shipping policy, quotation, commitment, or Shipment truth. |
| REQ-FPE-022 | Every Product or policy claim retains a governed source and current applicability, and unsupported Product, commercial, delivery, Return, support, or policy substance is absent. |
| REQ-FPE-023 | No review, rating, aggregate, submission, moderation, or eligibility capability appears unless applicable Approved governance explicitly establishes Product-review support, and FPE selects no review-support or review-policy value. |
| REQ-FPE-024 | Supported return navigation restores useful discovery context while route, history, filter, position, and cache manipulation cannot establish Product, Search, Category, or Authorization truth. |
| REQ-FPE-025 | Handoff carries only the identified Product Variant, applicable quantity intent, and governed evidence; FPE neither creates nor mutates Cart, establishes Stock Reservation, authorizes Checkout, initiates Payment, nor claims purchase success. |
| REQ-FPE-026 | Product, Product Variant, Price, Inventory, delivery, CMS, and policy evidence exposes or consumes sufficient source, applicability, freshness, and revalidation context; stale, partial, conflicting, or unavailable evidence is qualified, degraded, removed, or governed-revalidated according to materiality and is never presented as current confirmation. |
| REQ-FPE-027 | Reordered completion tests across Product, Product Variant, selection, media, Price, availability, delivery, and policy evidence show stale, cancelled, reordered, or superseded outcomes cannot replace newer intent. |
| REQ-FPE-028 | Each material region demonstrates every applicable initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery, and confirmed state without enforcing a universal sequence. |
| REQ-FPE-029 | Withdrawn, unpublished, invisible, invalid, inaccessible, stale, unavailable, and unsupported evaluation contexts produce truthful distinguishable outcomes and safe permitted recovery where available, without continued unsupported claims or protected-Resource existence disclosure. |
| REQ-FPE-030 | Recovery preserves known context, distinguishes retry from new intent, rejects obsolete outcomes, and uses governed capabilities without specifying backend retry, replay, idempotency, or reconciliation. |
| REQ-FPE-031 | Evaluation remains understandable and operable under representative viewport, orientation, reflow, zoom, localization, and content-extreme conditions without fixed FPE breakpoints or layouts. |
| REQ-FPE-032 | All applicable content, media, choice, quantity, navigation, recovery, and handoff interactions complete equivalent keyboard, pointer, touch, and assistive-technology paths. |
| REQ-FPE-033 | Variant, media, Price, availability, validation, failure, recovery, and handoff changes preserve or move focus predictably and expose the resulting context without unexpected position loss. |
| REQ-FPE-034 | Structural and assistive-technology evidence confirms coherent headings, names, relationships, selected and unavailable state, Price and Currency meaning, availability, validation, loading, failure, uncertainty, recovery, and change status, with material meaning never conveyed only through color, position, motion, imagery, or visual change. |
| REQ-FPE-035 | Long, short, absent, localized, delayed, invalid, stale, partial, and failed content or media preserves essential comprehension and unrelated evaluation operation without clipping or invented claims. |
| REQ-FPE-036 | Security and privacy tests find no unsafe rendering, Secret exposure, inferred Consent, unnecessary Sensitive Data in client state, routes, errors, logs, telemetry, or Analytics Events, or leakage across applicable Principal, Session, Customer, and Resource boundaries. |
| REQ-FPE-037 | Unauthorized and manipulation tests preserve current server-side contextual Authorization and disclose no inaccessible Resource existence through frontend visibility, selection, identifiers, errors, timing, Role labels, Permissions, Claims, or Scope. |
| REQ-FPE-038 | Large and degraded governed inputs demonstrate that Product content, Product Variant options, media, related evaluation evidence, reactive work, and background activity remain bounded by governed inputs and applicable performance evidence without requiring a locally invented numerical limit, media limit, timing budget, or client mechanism. |
| REQ-FPE-039 | Independent failure of each optional region leaves critical evaluation available and communicates the affected evidence's known completeness and uncertainty truthfully. |
| REQ-FPE-040 | Material evaluation failures, selection outcomes, recovery, and Contract-boundary failures produce safe frontend operational telemetry with governed correlation evidence; evidence classification distinguishes operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events, and Audit Records and confirms none substitutes for another or creates authoritative Domain history. |
| REQ-FPE-041 | Analytics review confirms non-authority, applicable Consent, minimization, and neutrality regarding provider, taxonomy, canonical names, payloads, attribution, personalization, and ranking. |
| REQ-FPE-042 | Every reachable flagged evaluation state retains valid evidence, truthful outcomes, context, accessibility, security, privacy, inherited obligations, and Domain authority; FPE selects no rollout policy or feature-flag mechanism. |
| REQ-FPE-043 | Contract review confirms necessary bounded and versioned Product, Product Variant, Attribute, media, selection, Price, availability, delivery, policy, source, applicability, freshness, outcome, and correlation semantics, with no FPE-defined route, method, operation, DTO, schema, wire format, status mapping, event payload, persistence, transport, or provider. |
| REQ-FPE-044 | Decision review finds every named Product and technical choice unresolved or governed elsewhere, with no local default embedded in evaluation behavior. |
| REQ-FPE-045 | Traceable evidence maps to every FPE Requirement and observably covers all substantive areas named by its verification Requirement. |

## 6. Requirement Traceability

| Requirement | FEB/FSC/FCD | Product | Business Requirements | Approved Domains | Governing Sources | Consumers |
| --- | --- | --- | --- | --- | --- | --- |
| REQ-FPE-001 | REQ-FEB-001–003; REQ-FSC-001–003; REQ-FCD-001–003 | PRODUCT.md §§21–22, 36 | REQ-BUS-047–048 | — | AGENTS.md §§5, 10.3, 14 | All FPE consumers |
| REQ-FPE-002 | REQ-FEB-002, 051; REQ-FSC-002–003, 043; REQ-FCD-002–003, 044 | PRODUCT.md §§21–22, 26 | REQ-BUS-047 | — | AGENTS.md §27.1 | FPE implementation and later storefront Specifications |
| REQ-FPE-003 | REQ-FEB-003–004; REQ-FSC-003, 019–025; REQ-FCD-003, 010, 026 | PRODUCT.md §§12–13, 21 | REQ-BUS-005, 047 | REQ-PRD-001–002; REQ-CAT-001–002; REQ-CUS-001–002; REQ-IDN-002–003; REQ-INV-002–003; REQ-CART-002–003; REQ-PRC-002–003; REQ-PAY-002–003; REQ-SHP-002–003; REQ-CHK-002–003; REQ-ORD-002–003; REQ-RET-002–003; REQ-CMS-002–004; REQ-SRCH-002–003 | ENGINEERING-PRINCIPLES.md §§7–10 | FSC, FCD, FCA, FCP, FPP, FAD, FRP |
| REQ-FPE-004 | REQ-FEB-009–010, 031; REQ-FSC-010–012; REQ-FCD-004, 024, 026 | PRODUCT.md §§5.2–5.3, 8.2, 14.2 | REQ-BUS-002, 005 | REQ-PRD-003–004, 023–024 | UI.md §§10, 12; ACCESSIBILITY.md §11 | Storefront visitors and FCD |
| REQ-FPE-005 | REQ-FEB-003–005, 007, 041; REQ-FSC-019, 027–028; REQ-FCD-018 | PRODUCT.md §§8.2, 14.2, 16.1 | REQ-BUS-003, 005 | REQ-PRD-003–004, 025–027; REQ-CMS-020, 032 | DESIGN-SYSTEM.md §32; UI.md §§17, 42 | Storefront visitors and FCP |
| REQ-FPE-006 | REQ-FEB-003–005, 007; REQ-FSC-019; REQ-FCD-018, 029 | PRODUCT.md §§12–13, 16.1 | REQ-BUS-003, 005 | REQ-PRD-018–024, 032–033 | UI.md §§23, 31 | Storefront visitors and FCP |
| REQ-FPE-007 | REQ-FEB-003–005, 007; REQ-FSC-019; REQ-FCD-019 | PRODUCT.md §§8.2, 16.1 | REQ-BUS-005–006 | REQ-PRD-007–010 | DESIGN-SYSTEM.md §§32–33 | Storefront visitors and FCP |
| REQ-FPE-008 | REQ-FEB-003–005, 041; REQ-FSC-019; REQ-FCD-011–012, 019 | PRODUCT.md §§8.2, 16.1; Open Product Decision 3 | REQ-BUS-005–006 | REQ-PRD-011–013 | DESIGN-SYSTEM.md §§32–33 | Storefront visitors and FCP |
| REQ-FPE-009 | REQ-FEB-004, 014–015, 019, 032; REQ-FCD-019 | PRODUCT.md §§14.2, 16.1; Open Product Decision 3 | REQ-BUS-005–006 | REQ-PRD-007–013, 021–022; REQ-INV-005, 007–009 | DESIGN-SYSTEM.md §33; UI.md §§13–14 | Storefront visitors and FCP |
| REQ-FPE-010 | REQ-FEB-014–015, 019–021; REQ-FCD-019, 027–029 | PRODUCT.md §§5.4–5.6, 14.2 | REQ-BUS-005–006, 042 | REQ-PRD-009–013, 039–040; REQ-INV-007–009, 036 | UI.md §§13, 23 | Storefront visitors and FCP |
| REQ-FPE-011 | REQ-FEB-006–007, 017; REQ-FCD-024 | PRODUCT.md §§5.4–5.6 | REQ-BUS-005–006, 042 | REQ-PRD-007–013; REQ-INV-007 | ANGULAR.md §§18, 37–38 | Storefront visitors |
| REQ-FPE-012 | REQ-FEB-007, 033, 037, 041; REQ-FSC-027–028, 036; REQ-FCD-020 | PRODUCT.md §§8.2, 16.9 | REQ-BUS-005, 050 | REQ-PRD-014–017, 025–027; REQ-CMS-021, 030 | UI.md §33; ACCESSIBILITY.md §§43, 46 | Storefront visitors |
| REQ-FPE-013 | REQ-FEB-032–037, 041; REQ-FCD-020 | PRODUCT.md §§5.3, 8.2 | REQ-BUS-002, 005, 037 | REQ-PRD-014–017, 041–042 | DESIGN-SYSTEM.md §§32, 57–58; ACCESSIBILITY.md §§41, 43 | Storefront visitors |
| REQ-FPE-014 | REQ-FEB-003–005, 007; REQ-FSC-022; REQ-FCD-021 | PRODUCT.md §§5.4, 16.1 | REQ-BUS-014–016 | REQ-PRC-002–003, 006–007, 019 | UI.md §26; ACCESSIBILITY.md §29 | Storefront visitors and FCP |
| REQ-FPE-015 | REQ-FEB-003–005, 007; REQ-FSC-022; REQ-FCD-021 | PRODUCT.md §§16.1, 18.1; Open Product Decisions 8, 14 | REQ-BUS-015–016 | REQ-PRC-009–012, 019, 023 | DESIGN-SYSTEM.md §34; UI.md §26 | Storefront visitors and FCP |
| REQ-FPE-016 | REQ-FEB-005, 007, 014–015, 020–021; REQ-FCD-021, 023 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-015–016, 042 | REQ-PRC-019, 024, 034 | UI.md §§23, 26, 45 | Storefront visitors and FCP |
| REQ-FPE-017 | REQ-FEB-003–005, 007; REQ-FSC-023; REQ-FCD-022 | PRODUCT.md §§5.4, 16.2; Open Product Decision 17 | REQ-BUS-017–018 | REQ-INV-002–009, 036 | UI.md §25; ACCESSIBILITY.md §28 | Storefront visitors and FCP |
| REQ-FPE-018 | REQ-FEB-003–005, 007, 041; REQ-FCD-022 | PRODUCT.md §§16.2, 17.2; Open Product Decisions 13, 17 | REQ-BUS-017–018, 042 | REQ-INV-007–009, 020 | DESIGN-SYSTEM.md §35; UI.md §25 | Storefront visitors and FCP |
| REQ-FPE-019 | REQ-FEB-003–005; REQ-FSC-019, 022–023; REQ-FCD-010, 018, 021–022 | PRODUCT.md §§14.2–14.3, 16.1–16.2 | REQ-BUS-005–006, 015, 017 | REQ-PRD-021–024; REQ-PRC-003; REQ-INV-003, 005, 009 | UI.md §§25–26 | Storefront visitors and FCP |
| REQ-FPE-020 | REQ-FEB-019; REQ-FCD-019 | PRODUCT.md §§14.2–14.3 | REQ-BUS-006, 009–010 | REQ-CART-002, 009; REQ-INV-010–011 | UI.md §§13–14, 29 | Storefront visitors and FCP |
| REQ-FPE-021 | REQ-FEB-003–005, 007, 037; REQ-FSC-022; REQ-FCD-023 | PRODUCT.md §§16.5, 17.2; Open Product Decisions 7–9 | REQ-BUS-005, 014, 029, 042 | REQ-SHP-004–008, 025–026; REQ-PRC-014 | UI.md §§23, 26; ACCESSIBILITY.md §29 | Storefront visitors and FCP |
| REQ-FPE-022 | REQ-FEB-003–005, 041; REQ-FSC-016–018, 027–028; REQ-FCD-023 | PRODUCT.md §§16.8–16.9, 16.11, 17.2; Open Product Decisions 11, 18 | REQ-BUS-005, 050 | REQ-PRD-025–027; REQ-PRC-019, 023; REQ-SHP-004–006; REQ-CMS-020, 023, 025, 032, 040; REQ-RET-014 | UI.md §§23, 42 | Storefront visitors |
| REQ-FPE-023 | REQ-FEB-003, 041, 050 | Open Product Decision 15 | — | — | — | Storefront visitors and Product owners |
| REQ-FPE-024 | REQ-FEB-006, 009–010; REQ-FCD-024–026 | PRODUCT.md §§5.6, 14 | REQ-BUS-004–005 | REQ-PRD-023–024; REQ-CAT-017–019; REQ-SRCH-004–005, 017 | UI.md §§12, 32; ACCESSIBILITY.md §11 | Storefront visitors and FCD |
| REQ-FPE-025 | REQ-FEB-004–005, 009, 019; REQ-FSC-025; REQ-FCD-026 | PRODUCT.md §§14.2–14.3 | REQ-BUS-006, 009–011 | REQ-CART-002–003, 005, 009, 014, 018; REQ-INV-010–012; REQ-CHK-002, 023, 027–028; REQ-PAY-006, 019 | UI.md §§14, 29–30 | FCP and storefront visitors |
| REQ-FPE-026 | REQ-FEB-005, 007, 014–015, 020; REQ-FSC-018, 028–029; REQ-FCD-023 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-005, 015, 017, 029, 042 | REQ-PRD-017, 027, 033, 040; REQ-PRC-019, 034; REQ-INV-007, 036; REQ-SHP-005, 025–026; REQ-CMS-040, 042 | UI.md §§23, 45 | Storefront visitors and FCP |
| REQ-FPE-027 | REQ-FEB-016–017; REQ-FSC-031; REQ-FCD-024 | PRODUCT.md §§5.4–5.6 | REQ-BUS-005, 042 | REQ-PRD-040; REQ-PRC-024, 034; REQ-INV-007, 036 | ANGULAR.md §§18, 37–38 | Storefront visitors |
| REQ-FPE-028 | REQ-FEB-014–018, 020; REQ-FSC-029; REQ-FCD-028 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-005, 042 | REQ-PRD-040; REQ-PRC-034; REQ-INV-036 | UI.md §§21–23; ACCESSIBILITY.md §§24–26 | Storefront visitors |
| REQ-FPE-029 | REQ-FEB-014–015, 020–021, 030; REQ-FSC-012, 028–031; REQ-FCD-027, 029 | PRODUCT.md §§5.4–5.6, 17.2 | REQ-BUS-003, 005, 042 | REQ-PRD-023, 037, 040; REQ-INV-007, 036 | UI.md §23; SECURITY-STANDARDS.md §18 | Storefront visitors |
| REQ-FPE-030 | REQ-FEB-017, 020–021; REQ-FSC-031; REQ-FCD-029 | PRODUCT.md §§5.6, 17.2 | REQ-BUS-042, 045 | REQ-PRD-040; REQ-PRC-028, 034; REQ-INV-030, 036 | ANGULAR.md §§37–38 | Storefront visitors and operations |
| REQ-FPE-031 | REQ-FEB-031, 033–034, 041; REQ-FSC-032, 036; REQ-FCD-030 | PRODUCT.md §§5.2–5.3, 5.7 | REQ-BUS-002, 037 | REQ-PRD-041–042 | ACCESSIBILITY.md §§35–38, 46, 55, 72–73; UI.md §§10, 58, 62 | Storefront visitors |
| REQ-FPE-032 | REQ-FEB-032, 034–035, 037; REQ-FSC-033; REQ-FCD-031 | PRODUCT.md §5.7 | REQ-BUS-037 | REQ-PRD-041 | ACCESSIBILITY.md §§8–10, 17, 41–43, 53–54 | Storefront visitors |
| REQ-FPE-033 | REQ-FEB-009–010, 016–017, 035–038; REQ-FSC-035; REQ-FCD-032 | PRODUCT.md §§5.6–5.7 | REQ-BUS-037, 042 | REQ-PRD-041 | ACCESSIBILITY.md §§9, 11, 24–26; UI.md §40 | Storefront visitors |
| REQ-FPE-034 | REQ-FEB-034–038; REQ-FSC-034–036; REQ-FCD-033 | PRODUCT.md §5.7 | REQ-BUS-005, 037, 042 | REQ-PRD-015, 041; REQ-PRC-029; REQ-INV-031; REQ-SHP-038 | ACCESSIBILITY.md §§6–7, 23–29, 43, 46 | Storefront visitors |
| REQ-FPE-035 | REQ-FEB-033, 041, 044; REQ-FSC-028, 030, 032, 036; REQ-FCD-034 | PRODUCT.md §§5.3–5.6, 8.2 | REQ-BUS-005, 037, 042, 050 | REQ-PRD-015–017, 041–042; REQ-CMS-030, 034, 040, 042 | ACCESSIBILITY.md §§43, 46, 73; UI.md §§33, 58 | Storefront visitors |
| REQ-FPE-036 | REQ-FEB-008, 026–030; REQ-FSC-037; REQ-FCD-035–036 | PRODUCT.md §§5.1, 16.7, 20; Open Product Decision 19 | REQ-BUS-032, 039–040 | REQ-PRD-050–051; REQ-CUS-005, 020–021, 039–040; REQ-IDN-006, 026–028, 043–044 | SECURITY-STANDARDS.md §§12, 14, 18, 27, 33, 35; ANGULAR.md §§43–47 | Storefront visitors, Security and Privacy owners |
| REQ-FPE-037 | REQ-FEB-012–013, 020, 030; REQ-FSC-009–012, 037; REQ-FCD-036 | PRODUCT.md §§5.1, 20 | REQ-BUS-032, 052 | REQ-PRD-050–051; REQ-CUS-005, 039–040; REQ-IDN-006, 026–028 | SECURITY-STANDARDS.md §§10–13, 18, 33, 35 | Storefront visitors and Security owners |
| REQ-FPE-038 | REQ-FEB-042–044; REQ-FSC-038; REQ-FCD-037 | PRODUCT.md §§8.3, 18.4–18.6 | REQ-BUS-038, 045 | REQ-PRD-042; REQ-PRC-030; REQ-INV-032; REQ-SHP-039 | PERFORMANCE.md §§16–17, 21, 35, 43–48, 54–57, 83 | Storefront visitors and operations |
| REQ-FPE-039 | REQ-FEB-020, 043–044; REQ-FSC-030, 038; REQ-FCD-038 | PRODUCT.md §§5.4–5.6, 8.3 | REQ-BUS-038, 042, 045 | REQ-PRD-017, 040; REQ-PRC-034; REQ-INV-036; REQ-SHP-025–026 | PERFORMANCE.md §§17, 21, 23, 35, 45, 83 | Storefront visitors and operations |
| REQ-FPE-040 | REQ-FEB-045–046; REQ-FSC-039; REQ-FCD-039 | PRODUCT.md §§18.3, 33 | REQ-BUS-043 | — | ANGULAR.md §61; EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §§27–28, 35 | Engineering and operations |
| REQ-FPE-041 | REQ-FEB-026, 029, 046–047; REQ-FSC-039; REQ-FCD-040 | PRODUCT.md §§18.3, 33; Open Product Decisions 19–20 | REQ-BUS-039–040, 043–044 | REQ-CUS-020–021, 038 | EVENTS.md §§6–8, 43–48; SECURITY-STANDARDS.md §§27–28 | Product, Analytics and Privacy owners |
| REQ-FPE-042 | REQ-FEB-048; REQ-FSC-040; REQ-FCD-041 | PRODUCT.md §§5.9, 21, 36–38; Open Product Decision 30 | — | — | ARCHITECTURE.md §§38.4, 43.3; ANGULAR.md §§62–63 | Storefront visitors and delivery teams |
| REQ-FPE-043 | REQ-FEB-005, 007, 014–015, 020, 042, 049; REQ-FSC-041; REQ-FCD-042 | PRODUCT.md §§21–22, 26 | REQ-BUS-005, 042, 046–047 | REQ-PRD-052; REQ-PRC-033–034; REQ-INV-035–036; REQ-SHP-037; REQ-CART-031–032 | ARCHITECTURE.md §§13, 36.5, 39; API.md §§9–13, 16–17, 23, 35–37, 63–72 | FPE implementation and Contract owners |
| REQ-FPE-044 | REQ-FEB-040, 047–050; REQ-FSC-042; REQ-FCD-043 | PRODUCT.md §§21, 25–26; Open Product Decisions 3, 7–9, 11, 13–15, 17–20, 25, 30 | REQ-BUS-038, 046, 048 | — | DECISIONS.md §§25–40; ENGINEERING-PRINCIPLES.md §§11, 24, 36 | Product, Design, Engineering and later storefront Specifications |
| REQ-FPE-045 | REQ-FEB-001–003, 014, 020–021, 026–030, 034, 042–051; REQ-FSC-002–003, 043; REQ-FCD-002–003, 044 | PRODUCT.md §§22, 26, 35 | REQ-BUS-002–006, 014–018, 029, 037–040, 042, 047 | REQ-PRD-044; REQ-CAT-043; REQ-CUS-049; REQ-IDN-050; REQ-INV-037; REQ-CART-033; REQ-PRC-035; REQ-PAY-045; REQ-SHP-043; REQ-CHK-044; REQ-ORD-052; REQ-RET-048; REQ-CMS-051; REQ-SRCH-050 | TESTING-STANDARDS.md §§5, 7, 9, 11, 13, 19–24, 27–31, 33–40; ANGULAR.md §§56–60, 68 | Engineering, QA, Accessibility, Security and all FPE consumers |

## 7. Open Product Decisions

All 30 Open Product Decisions in `PRODUCT.md` were reviewed. The following fourteen are materially relevant to FPE, retain their exact source wording and order, and remain unresolved by this Draft:

| Source Order | Open Product Decision | FPE Boundary |
| --- | --- | --- |
| 3 | Size, fit, colour, material, and other Product Variant or Attribute standards. | FPE presents only governed Attribute and Product Variant evidence and selects no standard, taxonomy, normalization, value set, display priority, or selection rule. |
| 7 | Shipping provider, service levels, delivery areas, and fee policy. | FPE may present governed delivery evidence but selects no provider, service, area, eligibility, timing, fee, or fulfilment policy. |
| 8 | Free-delivery threshold and promotional treatment. | FPE may present governed delivery or Promotion evidence but selects no threshold, qualification, wording, calculation, or promotional treatment. |
| 9 | Tax-inclusive display and invoice requirements. | FPE may present governed Price and tax evidence but selects no inclusion, display, calculation, invoice, or jurisdictional policy. |
| 11 | Returns, exchanges, and refund policy. | FPE may present governed Return, refund, or exchange policy evidence but selects no Return, exchange, refund, eligibility, timing, process, outcome, or policy. |
| 13 | Back-order and pre-order support. | FPE selects no support, eligibility, availability label, purchase treatment, timing, reservation, or fulfilment behavior. |
| 14 | Voucher and promotion stacking policy. | FPE may present governed commercial evidence but selects no stacking, priority, eligibility, application, or display rule. |
| 15 | Product-review support. | FPE presents no reviews or ratings unless later Approved governance establishes support and selects no submission, moderation, aggregation, eligibility, or display policy. |
| 17 | Low-stock and out-of-stock customer messaging. | FPE may present governed availability evidence but selects no threshold, wording, urgency, suppression, replenishment, or Customer-facing treatment. |
| 18 | Customer-support channels and service expectations. | FPE may present governed support evidence but selects no support channel, availability, service expectation, response commitment, escalation, or support policy. |
| 19 | Marketing-consent and communication-preference model. | FPE preserves Consent authority for analytics and selects no model, default, category, capture mechanism, communication behavior, or policy. |
| 20 | Initial analytics provider and event taxonomy. | FPE selects no provider, taxonomy, canonical event name, payload, attribution, personalization, SDK, or destination. |
| 25 | South African tax-display, invoice, and credit-note policy. | FPE may present governed tax-related Price or policy evidence but selects no tax-display, invoice, Credit Note, calculation, or legal policy. |
| 30 | Product launch date, release scope, and post-launch support window. | FPE selects no launch date, initial evaluation surface, rollout, release scope, support window, or numerical target. |

## 8. Risks and Controls

| Risk | Control |
| --- | --- |
| FPE becomes an alternative source of Product truth. | Require every material Product presentation to retain governed identity, source, applicability, publication and visibility evidence. |
| An unpublished or invisible Product remains evaluable. | Require current Product eligibility evidence and test withdrawn, unpublished, invisible and stale states negatively. |
| Product Variant evidence is associated with the wrong Product. | Preserve stable Product Variant identity and owning Product association across every presentation and handoff. |
| Selection presentation implies authoritative availability or purchase readiness. | Keep intended selection distinct from Inventory and purchasability truth and require governed validation before handoff. |
| Invalid or incompatible Product Variant combinations appear valid. | Verify completeness and compatibility evidence and provide safe correction without inventing Attribute rules. |
| Stale Product content appears current. | Preserve source and freshness context and require qualification, degradation, removal, or governed revalidation. |
| Product Media is misleading, inaccessible, or blocks evaluation. | Preserve association, accuracy, rights and accessibility evidence and isolate missing or failed media from essential evaluation. |
| Price or Discount presentation becomes commercial authority. | Require governed Money, Currency, applicability and freshness and prohibit authoritative client calculation or promotion policy. |
| Availability presentation becomes Inventory authority. | Preserve Product Variant and freshness context and prohibit FPE from establishing Stock, reservation, or final purchasability. |
| Availability wording resolves low-stock or ordering policy. | Keep low-stock, out-of-stock, back-order and pre-order treatment explicitly deferred to Product governance. |
| Delivery evidence becomes a Shipping promise. | Preserve governed source, destination, applicability and uncertainty and prohibit FPE-owned quotation or fulfilment policy. |
| Unsupported policy or review claims influence purchase evaluation. | Present only current governed policy evidence and omit reviews or ratings until Approved support exists. |
| Quantity presentation creates Cart or Stock Reservation truth. | Treat quantity only as evaluation intent and require owning-Domain capabilities for Cart and Inventory effects. |
| FPE absorbs Cart, Checkout, Payment, or post-purchase behavior. | Constrain handoff to governed evaluation evidence and retain all mutations and outcomes in their owning journeys and Domains. |
| Delayed asynchronous evidence overwrites newer selection intent. | Associate outcomes with initiating context and reject stale, cancelled, reordered, incompatible, or superseded completion. |
| Evaluation context leaks across Principals or Sessions. | Keep retained state lifecycle-bounded and isolated across governed Principal, Session, Customer and Resource boundaries. |
| Protected Products become enumerable through evaluation outcomes. | Preserve contextual server-side Authorization and consistent concealment across content, errors, identifiers and timing. |
| Large media or option sets create unbounded client work. | Require bounded Contract evidence, rendering and media work without fixing local limits or mechanisms. |
| Optional evidence failure blocks critical evaluation. | Isolate optional content, media, policy and analytics failures and communicate known completeness and uncertainty. |
| Dynamic changes break focus or assistive-technology context. | Verify predictable focus and accessible status for Product Variant, media, Price, availability, validation and recovery changes. |
| Analytics bypasses Consent or captures Sensitive Data. | Consume governed Consent where required, minimize evidence, and separate analytics from telemetry and Domain truth. |
| Feature flags create invalid, unsafe, or inaccessible evaluation states. | Verify every reachable flag state against evidence, authority, security, privacy and accessibility obligations. |
| Contract evolution silently changes Product meaning or handoff semantics. | Require versioned bounded source, association, applicability, freshness, outcome and compatibility semantics with safe failure. |
| Product policy or implementation choices leak into normative behavior. | Reject Attribute, media, pricing, delivery, review, provider, route, schema, cache and numerical choices from FPE Requirements. |

## 9. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `.ai/frontend/ANGULAR.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/business/business-requirements.md`
- `specifications/frontend/shared/frontend-baseline.md`
- `specifications/frontend/storefront/storefront-shell-content.md`
- `specifications/frontend/storefront/catalogue-discovery.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/pricing/pricing-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/shipping/shipping-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/payment/payment-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`
- `specifications/domains/cms/cms-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/order/order-domain.md`
- `specifications/domains/return/return-domain.md`

## 10. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-08 | Draft | Initial comprehensive Product Evaluation Specification. |

## 11. Final Validation

Before this Draft is committed, verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, and scope is `FPE`, with no normative or repository-wide authority before approval;
2. governing-source precedence, Product and Business Requirements, FEB, FSC and applicable FCD inheritance, and every Approved Domain's authority remain preserved;
3. FPE owns only Product-evaluation presentation and remains separate from FSC, FCD, FCA, FCP, FPP, FAD and FRP;
4. Product, Product Variant, Product Media, publication, visibility, lifecycle, structural sellability, Pricing, Inventory, Category, Search, CMS, Customer, Identity, Cart, Checkout, Payment, Order, Shipping, Return, Refund, reviews, Consent and Authorization boundaries remain explicit;
5. evaluation entry, Product identity and content, Product Variant, Attributes, selection, media, Price, availability, delivery, policy, review boundary, discovery context and purchase handoff remain governed and non-authoritative in FPE;
6. initial, loading, empty, stale, partial, invalid, unavailable, failed, denied, uncertain, recovery and confirmed presentation states remain distinguishable without becoming a mandatory lifecycle graph;
7. freshness, asynchronous supersession, selection integrity, context preservation, recovery and safe handoff remain correct without defining backend or downstream mechanisms;
8. responsive, reflow, zoom, orientation, localization, content-extreme, keyboard, pointer, touch, focus, assistive-technology, structure, state and status evidence supports applicable WCAG 2.2 AA outcomes;
9. safe rendering, contextual Authorization, Resource concealment, state isolation, Sensitive Data, Secrets, privacy and Consent non-inference are preserved;
10. content, option and media work remains bounded, optional failures are isolated and degradation remains truthful without numerical targets;
11. operational telemetry, correlation evidence, Analytics Events, Domain Events, Integration Events and Audit Records remain distinct and analytics remains minimized, consent-aware, non-authoritative, provider-neutral and taxonomy-neutral;
12. every feature-flag state preserves valid evaluation evidence, truthful outcomes, security, privacy, accessibility, inherited obligations and Domain authority;
13. Contracts remain abstract, bounded and sufficient for truthful evaluation without routes, methods, operations, DTOs, schemas, wire formats, status mappings, event payloads, persistence, transport or providers;
14. Requirements, Acceptance Criteria and traceability are equal in count, unique, sequential, gap-free and one-to-one;
15. every FEB, FSC, FCD, Business Requirement, Approved Domain Requirement, Product section and governing-source citation physically exists and directly supports its FPE Requirement;
16. all fourteen Product Decisions exactly match `PRODUCT.md`, remain materially complete, source-ordered and unresolved;
17. Risks have distinct FPE-specific controls and all Related Document paths exist;
18. Revision History contains exactly one `0.1.0 Draft` row;
19. no Glossary amendment is required and FPE-local descriptions are not repository-wide terminology;
20. no Product policy, provider, protocol, route, URL, API, schema, persistence, cache mechanism, event taxonomy, payload, numerical target, timeout, retry count, breakpoint, layout, visual token, lifecycle graph or implementation mechanism is introduced;
21. Markdown headings and tables, UTF-8, trailing whitespace, exactly one final newline and prohibited-marker checks pass; and
22. Git scope contains no tracked or staged change and only `specifications/frontend/storefront/product-evaluation.md` is untracked.
