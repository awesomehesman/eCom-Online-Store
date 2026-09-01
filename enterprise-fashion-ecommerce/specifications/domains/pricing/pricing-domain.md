---
title: Pricing Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-01
authoritative: false
---

# Pricing Domain

## 1. Purpose

This Specification defines Approved Pricing-domain Requirements for authoritative commercial calculation, Money and Currency integrity, Price, Discount, Promotion and Voucher evaluation, cross-domain authority boundaries, revalidation, failure, recovery, security, accessibility, performance, testing, and traceability.

While this Specification remains Approved, its Requirements are normative only within the Pricing Domain. This Specification remains `authoritative: false`, subordinate to governing core sources under the `AGENTS.md` Decision Hierarchy, and does not resolve an Open Product Decision.

## 2. Scope and Authority

**Requirement scope code:** `PRC`

Requirement identifiers use `REQ-PRC-NNN`, remain stable under `.ai/core/DOCUMENTATION-STANDARDS.md`, and do not create a SPEC identifier.

### REQ-PRC-001 — Lifecycle, Authority, and Scope

This Specification MUST remain `authoritative: false`, use `PRC` as its Requirement scope code, remain subordinate to governing core sources, and have normative effect only within the Pricing Domain. While its status is Approved, its Requirements MUST be normative only within that scope.

## 3. Domain Context

Pricing produces current commercial calculation outcomes from valid governed inputs. It collaborates with Product, Customer, Inventory, Cart, Checkout, Order, Payment, Shipping and Fulfilment, Return and Refund, and Administration while preserving each owning Domain's authority.

## 4. Canonical Terminology

This Specification uses Product, Product Variant, Product Media, Category, Price, Money, Currency, Base Price, Sale Price, Discount, Discount Rule, Pricing Rule, Price List, Promotion, Voucher, Promotion Eligibility, Promotion Benefit, Promotion Combination, Tax, VAT, Tax Inclusive, Tax Exclusive, Tax Jurisdiction, Tax Category, Shipping Rate, Delivery Method, Customer, Principal, Account, Cart, Cart Item, Checkout, Order, Order Item, Order Snapshot, Payment, Payment Authorization, Capture, Settlement, Refund, Refund Transaction, Return, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling, Authentication, Authorization, Claims, Audit Record, Sensitive Data, Secret, Contract, Domain Event, Integration Event, Projection, and Risk with the meanings in `.ai/core/GLOSSARY.md`.

## 5. Pricing Ownership and Authority

### REQ-PRC-002 — Authoritative Commercial Calculation

Pricing MUST own the current authoritative server-side commercial calculation outcome permitted by Approved policy, including applicable Price determination, Discount, Promotion and Voucher application, calculated line values, totals, savings, and governed commercial eligibility. Tax and delivery-related values MUST be included only where their governing policy and authoritative inputs permit it. Every trusted Pricing outcome MUST be based on valid governed inputs and MUST remain distinguishable from historical Order commercial truth.

### REQ-PRC-003 — Cross-Domain Authority Separation

Pricing MUST NOT create or redefine Product or Product Variant truth, Product content, Product Media, Category, publication, visibility, structural sellability, Customer identity, Authentication, Authorization policy, Inventory truth, Cart intent, Checkout orchestration, Order creation or history, Payment truth, Shipment or fulfilment truth, Return or Refund policy, fraud policy, or Administration policy. A Pricing outcome MUST NOT establish success in any of those Domains.

## 6. Product and Product Variant Boundary

### REQ-PRC-004 — Governed Product Association

Every applicable Pricing outcome MUST identify and use a valid governed Product or Product Variant reference at the granularity required by Approved policy. Pricing MAY consume Product-owned attributes only where Approved policy permits and MUST NOT create or alter Product identity, Product Variant identity, content, Product Media, Category, publication, visibility, or structural sellability.

### REQ-PRC-005 — Product Input Safety

An unknown, removed, unpublished, stale, materially changed, or structurally unsellable Product or Product Variant MUST NOT silently produce trusted Pricing truth. Each applicable condition MUST yield a distinguishable safe outcome and governed revalidation or recovery where permitted, without treating Inventory availability as Product structural sellability.

## 7. Money and Currency

### REQ-PRC-006 — Money and Currency Integrity

Every Pricing monetary input, intermediate commercial value exposed across a boundary, comparison, and result MUST preserve Money semantics with explicit Currency and precision-safe calculation. Pricing MUST NOT use floating-point monetary arithmetic, silently mix Currency values, or perform implicit Currency conversion. Version 1 Customer-visible Price and commercial totals MUST use `ZAR`; an unknown or unsupported Currency context MUST produce a safe distinguishable outcome. This Specification defines no exchange rate, conversion provider, storage type, rounding algorithm, decimal scale, or Minor Unit rule.

## 8. Authoritative Price

### REQ-PRC-007 — Price Determination

Pricing MUST determine the applicable authoritative Price for the correct governed Product or Product Variant under current Approved Pricing Rules and inputs. The outcome MUST distinguish applicable Base Price, Sale Price, and other governed components without treating a client value, Cart presentation, analytics value, or Projection as authoritative. A missing or invalid Price MUST NOT produce a trusted total.

### REQ-PRC-008 — Price Change Safety

A stale, changed, missing, or invalid Price MUST produce an explicit explainable outcome and require governed recalculation before commercial commitment. Equivalent governed inputs and rule state MUST produce a consistent result, while a later Price change MUST NOT silently rewrite confirmed Order commercial history. This Specification defines no Price List structure, effective-date mechanism, historical persistence mechanism, or caching policy.

## 9. Discounts

### REQ-PRC-009 — Discount Calculation Boundary

An applicable Discount MUST be evaluated only under Approved Discount Rules, recorded and explained separately from Base Price, Sale Price, Promotion, Voucher, Tax, and shipping charges, and be reproducible under equivalent governed inputs. Invalid, stale, changed, or inapplicable Discount state MUST be distinguishable and safely revalidated. This Specification defines no threshold, percentage, eligibility value, precedence, or calculation formula.

## 10. Promotions

### REQ-PRC-010 — Promotion Evaluation

Pricing MUST apply a Promotion only when its governed validity, Promotion Eligibility, Promotion Benefit, exclusions, usage constraints, and other required Approved inputs establish applicability. Applied and unapplied outcomes MUST be explicit and explainable. Invalid, inactive, stale, changed, or conflicting Promotion state MUST produce a distinguishable safe outcome and revalidation where permitted without inventing policy values.

## 11. Vouchers

### REQ-PRC-011 — Voucher Evaluation

Pricing MUST accept a Voucher reference only as governed input to an eligible Promotion evaluation. An invalid, ineligible, expired where governed, stale, changed, or otherwise unusable Voucher MUST produce a distinguishable failure safe for the applicable Customer context. Consumption or usage state MAY affect evaluation only when supplied by its owning authority, and Pricing MUST NOT invent Voucher format, duration, redemption limit, ownership, transferability, usage count, or campaign structure.

### REQ-PRC-012 — Promotion Combination Deferral

Pricing MUST NOT establish Promotion or Voucher stacking, precedence, combinability, maximum benefit, eligibility values, usage limits, exclusions, calculation order, or Refund treatment until Approved policy governs each applicable value. Conflicting rules without a governed resolution MUST fail safely rather than choose a local outcome.

## 12. Tax Boundary

### REQ-PRC-013 — Governed Tax Calculation Boundary

Pricing MAY calculate or present Tax-related commercial outcomes only from qualified, Approved tax policy and governed inputs. Pricing MUST NOT decide whether prices are Tax Inclusive or Tax Exclusive, determine an Invoice or Credit Note legal format, invent a Tax Jurisdiction implementation, Tax rate, Tax Category mapping, formula, or provider, or represent unresolved tax policy as production-ready truth. Confirmed Order tax history MUST remain outside current Pricing authority.

## 13. Shipping Commercial Boundary

### REQ-PRC-014 — Shipping Input and Charge Separation

Pricing MAY include a governed Shipping Rate, delivery charge, or Promotion Benefit affecting delivery only when Approved policy establishes the handoff from Shipping authority. Pricing MUST NOT establish a Shipment, Carrier, Delivery Method, delivery area, fulfilment, operational delivery eligibility, fee policy, free-delivery threshold, or promotional delivery treatment. An unavailable or stale required shipping-commercial input MUST prevent a trusted affected total.

## 14. Customer and Identity Boundary

### REQ-PRC-015 — Governed Customer Eligibility Input

Pricing MAY consume a governed Customer, Principal, Account, segment, entitlement, or other eligibility input only where Approved policy permits. Pricing MUST NOT define identity, create an Account, own Authentication or Authorization policy, invent segmentation, loyalty tiers or eligibility classes, infer protected or Sensitive Data attributes, or trust a supplied Customer identifier as authority. A Customer-dependent outcome MUST identify the governed eligibility context used.

## 15. Inventory Boundary

### REQ-PRC-016 — Inventory Non-Authority

Pricing MUST NOT own, infer, create, reserve, or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection. Inventory availability MUST NOT silently change a Pricing Rule or commercial outcome unless Approved Product policy expressly governs that dependency; Pricing MUST NOT infer scarcity pricing or availability-based pricing.

## 16. Cart Boundary

### REQ-PRC-017 — Cart Presentation and Intent Separation

Pricing MAY provide governed outcomes for Cart presentation, but Cart intent, Cart Item membership, and intended quantity MUST remain Cart-owned. A displayed or retained Cart Price, Discount, Promotion, Voucher, Tax, total, or savings value MUST remain a non-authoritative presentation or Projection, MUST NOT be represented as locked or final, and MUST be recalculated through governed Pricing before Checkout commitment.

## 17. Checkout Boundary

### REQ-PRC-018 — Checkout Revalidation Support

Pricing MUST support authoritative revalidation of all applicable commercial inputs and outcomes before Checkout commercial commitment and MUST return explicit failure or uncertainty rather than false success. Pricing MUST NOT become Checkout, define its lifecycle, create an Order, initiate Payment merely because Pricing succeeds, reserve Inventory, or establish Shipping truth.

## 18. Order Snapshot Boundary

### REQ-PRC-019 — Current and Historical Commercial Truth

Pricing MAY provide governed commercial values for a later Order Snapshot, but MUST NOT create an Order, govern Order status or lifecycle, or own immutable Order history. Current Pricing truth and confirmed historical commercial truth MUST remain distinguishable, and later Product, Price, Discount, Promotion, Voucher, Tax, or other Pricing changes MUST NOT silently rewrite the Order Snapshot.

## 19. Payment Boundary

### REQ-PRC-020 — Payment Non-Authority

Pricing MAY provide a governed total as Payment input, but MUST NOT establish Payment, Payment Authorization, Payment success, Capture, Settlement, Refund completion, or any provider-dependent Payment truth. Pricing success MUST NOT imply Payment success, and Payment outcomes MUST NOT silently redefine authoritative Pricing Rules or current Pricing results.

## 20. Return, Refund, and Unresolved Credit Boundary

### REQ-PRC-021 — Unresolved Post-Purchase Commercial Policy

Pricing MUST NOT invent a Return window, Refund amount, Partial Refund rule, promotional Refund treatment, Voucher restoration, Discount reversal, gift cards, store credit, or promotional credit treatment. Any future governed post-purchase calculation MUST use applicable historical and current authoritative inputs without rewriting the original Order Snapshot. Gift cards, store credit, and promotional credit MUST remain unresolved external commercial inputs until Product governance assigns policy and authority.

## 21. Fraud and Abuse Boundary

### REQ-PRC-022 — Governed Fraud Eligibility Input

Pricing MUST NOT become a fraud engine or invent a score, threshold, provider, screening method, blocking rule, or review workflow. Where Approved policy permits commercial eligibility to consume a fraud or abuse decision, Pricing MUST use only the governed outcome, preserve Authorization, auditability and explainability, and fail safely when a required outcome is missing, stale, unavailable, or uncertain.

## 22. Determinism and Reproducibility

### REQ-PRC-023 — Explainable and Reproducible Pricing

A Pricing result MUST identify the governed input and rule context needed to explain and reproduce it under equivalent conditions, including applicable Product Variant, Customer or eligibility context, Price, Discount, Promotion, Voucher, Tax context, shipping-commercial input, and Currency. Equivalent governed inputs and rule state MUST yield the same commercial outcome; explanation MUST NOT expose protected or abuse-sensitive rule detail.

## 23. Stale and Changed Pricing

### REQ-PRC-024 — Material Commercial Change Handling

Stale or changed Price, Discount, Promotion, Voucher, Tax context, Customer eligibility, Product input, shipping-commercial input, or other material governed input, and an uncertain Pricing result, MUST each remain distinguishable from current trusted truth. Applicable material changes before commitment MUST be recalculated and explained without defining Customer-facing copy or silently preserving an obsolete outcome.

## 24. Concurrency and Duplicate Safety

### REQ-PRC-025 — Evaluation and Effect Safety

Concurrent, retried, replayed, duplicate, or stale Pricing requests MUST produce a consistent explainable outcome for the same governed context and MUST NOT create duplicate or conflicting commercial effects. An uncertain outcome MUST be verified before blind repetition where an effect may occur. Pricing evaluation MUST remain read-only unless Approved governance explicitly permits a state-changing commercial operation; this Specification defines no locking, transaction, isolation, idempotency-key, or persistence mechanism.

## 25. Authentication and Authorization

### REQ-PRC-026 — Protected Pricing Authorization

Where protected Pricing capability or Customer-specific commercial outcomes require Authentication, Authentication MUST establish the Principal and trusted server-side Authorization MUST govern access for the current Resource, action, and Domain state. UI state, route, Role label, client Claims, browser state, supplied Customer identifier, or supplied Voucher identifier MUST NOT grant authority. Authentication and Authorization ownership MUST remain external to Pricing.

## 26. Security and Sensitive Data

### REQ-PRC-027 — Pricing Data Protection

Pricing MUST minimize Customer PII and protect applicable Sensitive Data, Secrets, credentials, abuse-sensitive Voucher or Promotion information, provider details, operational internals, and implementation details in access, errors, logs, Contracts, events, and Projections. Failure output MUST avoid protected Resource enumeration and expose only information permitted for the receiving context; ordinary Price data MUST NOT automatically be classified as Sensitive Data.

## 27. Recovery and Reconciliation

### REQ-PRC-028 — Pricing Recovery and Reconciliation

Stale commercial state, conflicting governed input, uncertain Pricing outcome, unavailable dependency, changed Product or Customer eligibility input, invalid Promotion or Voucher input, and downstream disagreement MUST support distinguishable safe recovery or reconciliation where permitted. Recovery MUST be authorized where required, observable, duplicate-safe, and MUST NOT fabricate authority, invent policy, or overwrite historical Order truth.

## 28. Accessibility

### REQ-PRC-029 — Accessible Pricing Outcomes

Customer-facing Price, Discount, Promotion, Voucher, totals, savings, material-change notices, validation failures, and recovery guidance MUST support applicable WCAG 2.2 AA outcomes, including keyboard and assistive-technology use where relevant. This Requirement MUST NOT claim certification, require AAA, or dictate a local visual design.

## 29. Performance

### REQ-PRC-030 — Bounded Pricing Delivery

Pricing delivery and applicable Contracts MUST be bounded and observable in accordance with governing performance sources. Degradation, dependency failure, or timeout MUST NOT convert stale, partial, or uncertain Pricing into trusted commercial truth. This Specification defines no numerical performance target, SLA, SLO, device profile, or network profile.

## 30. Audit

### REQ-PRC-031 — Proportional Pricing Audit Evidence

Material high-Risk administrative, commercial-rule mutation, reconciliation, security-sensitive, or policy-affecting Pricing action MUST produce proportional Audit Records where governing Risk requires them. Ordinary Customer Price reads MUST NOT automatically require high-Risk audit treatment.

## 31. Events

### REQ-PRC-032 — Conditional Pricing Events

A Pricing Domain Event or Integration Event MUST be required only where Architecture, an Approved Contract, downstream reliability, or synchronized governance establishes the need. Any governed event MUST preserve Pricing authority, compatibility, data protection, duplicate and replay safety, and non-authoritative consumer Projections. This Specification defines no event name, topic, schema, payload, broker, queue, choreography, or delivery guarantee.

## 32. Contract Impacts

### REQ-PRC-033 — Pricing Contract Boundary

Future Pricing Contracts MUST preserve authoritative Pricing ownership, correct Product and Product Variant association, Money and Currency, governed eligibility inputs, stale-state safety, explainability, security, applicable Authorization, compatibility, and distinguishable failure semantics. This Specification defines no URL, path, HTTP method, status code, JSON field or shape, DTO, class, service, table, database schema, provider payload, or event schema.

## 33. Failure Outcomes

### REQ-PRC-034 — Explicit Pricing Failure Outcomes

Applicable unknown Product, unknown Product Variant, removed Product, unpublished Product, structurally unsellable Product Variant, missing Price, invalid Price, stale Price, changed Price, unsupported Currency, invalid or stale Discount, invalid, inactive or stale Promotion, invalid, ineligible or stale Voucher, conflicting commercial rules, missing or stale governed eligibility input, unavailable dependency, unauthorized protected request, and uncertain Pricing result MUST each produce a distinguishable, safe, explainable, and recoverable outcome where permitted.

Failure information MUST NOT expose Sensitive Data, Secrets, credentials, inaccessible protected Resource existence, abuse-sensitive rule detail, security controls, provider internals, or operational internals. Uncertainty MUST NOT be represented as success.

## 34. Testing

### REQ-PRC-035 — Pricing Verification Coverage

Verification MUST cover applicable positive and negative Pricing behavior, Product and Product Variant association, Price, Money, Currency, Discount, Promotion, Voucher, Product and Pricing changes, stale state, Customer-specific governed input, Cart Projection boundaries, Checkout revalidation, Order Snapshot boundaries, Payment and Inventory non-authority, Shipping boundaries, duplicate, retry, replay and concurrency behavior, failure, recovery, security, Authorization, accessibility, performance, proportional audit, governed events, and Contracts. No test framework, separate test-case identifier scheme, or numerical coverage target is selected.

## 35. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-PRC-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; scope code is `PRC`; the text preserves core-source precedence and limits normative effect to the Pricing Domain. |
| REQ-PRC-002 | Trusted server-side Pricing output owns only policy-permitted current Price, Discount, Promotion, Voucher, line-value, total, savings, eligibility, Tax, and delivery-related calculations; each output uses valid governed inputs and remains distinct from Order history. |
| REQ-PRC-003 | Pricing cannot create, redefine, or establish success for any listed external Domain truth or policy. |
| REQ-PRC-004 | Each outcome uses the correct governed Product or Product Variant and only permitted Product attributes without altering any listed Product-owned concept. |
| REQ-PRC-005 | Each applicable unknown, removed, unpublished, stale, changed, or structurally unsellable Product condition prevents silent trusted Pricing, produces a safe distinguishable outcome and permitted recovery, and remains separate from Inventory availability. |
| REQ-PRC-006 | All boundary monetary values, comparisons, and results carry Currency and use precision-safe non-floating-point Money semantics; Currency mixing and implicit conversion are rejected; Version 1 Customer-visible outcomes use ZAR; unsupported Currency fails safely; no prohibited monetary implementation or policy is selected. |
| REQ-PRC-007 | The correct Product or Product Variant receives an authoritative Price under current governed rules; Base Price, Sale Price, and applicable components are distinguishable; no client, Cart, analytics, or Projection value is authoritative; missing or invalid Price cannot produce a trusted total. |
| REQ-PRC-008 | Stale, changed, missing, and invalid Price each triggers an explicit outcome and recalculation before commitment; equivalent inputs are consistent; later change cannot rewrite Order history; no prohibited structure or mechanism is selected. |
| REQ-PRC-009 | A Discount applies only under Approved rules, remains separate from all listed commercial components, is explainable and reproducible, and invalid, stale, changed, or inapplicable state is distinguishable and revalidated without invented values. |
| REQ-PRC-010 | A Promotion applies only when every required governed input establishes eligibility; application and non-application are explicit; invalid, inactive, stale, changed, and conflicting state each fails or revalidates safely without invented policy. |
| REQ-PRC-011 | A Voucher is evaluated only as governed Promotion input; each listed unusable state yields a safe distinguishable Customer-context failure; usage state comes only from its owner; no prohibited Voucher policy is established. |
| REQ-PRC-012 | No stacking, precedence, combinability, benefit, eligibility, limit, exclusion, order, or Refund-treatment policy is invented, and unresolved conflict fails safely. |
| REQ-PRC-013 | Tax is calculated or presented only under qualified Approved policy and governed inputs; Pricing establishes none of the listed unresolved tax, Invoice, Credit Note, jurisdiction, mapping, formula, provider, or historical authorities. |
| REQ-PRC-014 | Delivery-related value enters Pricing only through a governed Shipping handoff; Pricing owns none of the listed Shipping facts or policies; a stale or unavailable required input blocks a trusted affected total. |
| REQ-PRC-015 | Customer-dependent Pricing uses only permitted governed eligibility context and identifies it; Pricing defines or infers none of the listed identity, access, segmentation, or sensitive concepts, and a supplied identifier grants no authority. |
| REQ-PRC-016 | Pricing cannot own, infer, reserve, or alter any listed Inventory concept; availability changes Pricing only under express Approved policy, and no scarcity or availability pricing is inferred. |
| REQ-PRC-017 | Cart retains intent authority; every presented commercial value remains non-authoritative and unlocked and is recalculated through Pricing before commitment. |
| REQ-PRC-018 | Checkout receives current authoritative revalidation and explicit failure or uncertainty; Pricing cannot become Checkout or establish any listed downstream outcome. |
| REQ-PRC-019 | Pricing may supply values for an Order Snapshot but cannot own Order creation, lifecycle, status, or history; current and historical truth remain distinct and later changes cannot rewrite the snapshot. |
| REQ-PRC-020 | Pricing total may be Payment input but cannot establish any listed Payment truth; Pricing and Payment success remain distinct and Payment cannot redefine Pricing. |
| REQ-PRC-021 | Pricing establishes none of the listed Return, Refund, Voucher-restoration, Discount-reversal, gift cards, store credit, or promotional credit policies; any future governed post-purchase calculation uses applicable historical and current authoritative inputs, preserves the original Order Snapshot unchanged, and keeps unresolved credit treatment external pending Product governance. |
| REQ-PRC-022 | Pricing defines no fraud mechanism or policy; permitted fraud input comes only from governance, preserves Authorization, auditability, and explainability, and missing, stale, unavailable, or uncertain required input fails safely. |
| REQ-PRC-023 | A result carries enough governed Product Variant, eligibility, Price, Discount, Promotion, Voucher, Tax, shipping, Currency, and rule context for safe explanation and reproduction; equivalent contexts match and protected details remain concealed. |
| REQ-PRC-024 | Every listed stale, changed, or uncertain input remains distinct from current truth, is recalculated and explained before commitment where material, and neither obsolete outcome nor UI copy is established. |
| REQ-PRC-025 | Concurrent, retried, replayed, duplicate, and stale requests remain consistent and create no duplicate or conflicting effect; uncertain effects are verified before repetition; evaluation remains read-only absent Approved mutation governance; no prohibited mechanism is selected. |
| REQ-PRC-026 | Where required, Authentication establishes the Principal and trusted server-side Authorization governs protected Pricing for the current Principal, Resource, action, and Domain state; none of the listed client or supplied values grants authority, and Authentication and Authorization ownership remain external to Pricing. |
| REQ-PRC-027 | Pricing minimizes PII and protects every listed protected class across all listed surfaces; failures prevent enumeration and disclose only permitted context, without classifying ordinary Price as Sensitive Data automatically. |
| REQ-PRC-028 | Every listed stale, conflict, uncertainty, dependency, changed-input, invalid-input, or disagreement case supports permitted distinguishable recovery; recovery is appropriately authorized, observable and duplicate-safe and fabricates no authority or policy and rewrites no history. |
| REQ-PRC-029 | Every listed Customer-facing Pricing outcome supports applicable WCAG 2.2 AA, keyboard, and assistive-technology outcomes without certification, AAA, or local visual-design claims. |
| REQ-PRC-030 | Pricing delivery and applicable Contracts are bounded and observable; degradation, dependency failure, and timeout cannot promote stale, partial, or uncertain Pricing into trusted commercial truth; no unsupported numerical or profile target is established. |
| REQ-PRC-031 | Applicable high-Risk Pricing actions create proportional Audit Records while ordinary Customer Price reads are not automatically high-Risk audit events. |
| REQ-PRC-032 | Pricing events exist only under a listed governing need and preserve authority, compatibility, protection, duplicate safety, replay safety, and Projection non-authority without selecting any prohibited event mechanism or detail. |
| REQ-PRC-033 | Future Contracts preserve every listed Pricing, association, Money, eligibility, stale, explanation, security, Authorization, compatibility, and failure concern without defining any prohibited Contract or implementation detail. |
| REQ-PRC-034 | Every applicable listed failure is distinguishable, safe, explainable, and recoverable where permitted; output conceals every listed protected detail and uncertainty is never success. |
| REQ-PRC-035 | Verification evidence covers every listed applicable positive, negative, boundary, state, resilience, security, quality, audit, event, and Contract concern without selecting a framework, test identifier scheme, or coverage target. |

## 36. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-PRC-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047–048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md §§7–9, 19–21 | Pricing governance |
| REQ-PRC-002 | PRODUCT.md §§13, 16.1, 16.6 | REQ-BUS-012, 015–016 | REQ-PRD-046; REQ-CART-016 | AGENTS.md §31.2 | Pricing, Cart, Checkout |
| REQ-PRC-003 | PRODUCT.md §§13, 16 | REQ-BUS-015, 017, 021, 024, 029–030 | REQ-PRD-046–048; REQ-CUS-031–036; REQ-INV-003; REQ-CART-003 | AGENTS.md §31 | Cross-domain |
| REQ-PRC-004 | PRODUCT.md §§14.2–14.4, 16.1 | REQ-BUS-005–006, 012, 015 | REQ-PRD-003–010, 021, 046 | GLOSSARY.md §§5–6 | Product, Pricing |
| REQ-PRC-005 | PRODUCT.md §§5.4, 14.2–14.4, 17.2 | REQ-BUS-005–006, 012, 042 | REQ-PRD-021, 037, 040; REQ-INV-005 | API.md §§42–43 | Product, Checkout |
| REQ-PRC-006 | PRODUCT.md §§6, 16.1, 20 | REQ-BUS-001, 014 | — | GLOSSARY.md §§5, 35; DATABASE.md §24; POSTGRES.md §13; API.md §14 | All commercial consumers |
| REQ-PRC-007 | PRODUCT.md §§14.2–14.4, 16.1 | REQ-BUS-005, 012, 015 | REQ-PRD-046; REQ-CART-016 | API.md §42 | Product, Cart, Checkout |
| REQ-PRC-008 | PRODUCT.md §§5.4, 14.4, 16.1, 16.4 | REQ-BUS-012, 016, 022 | REQ-CART-017 | DATABASE.md §52; UI.md §§23, 26 | Checkout, Order |
| REQ-PRC-009 | PRODUCT.md §§16.1, 16.6 | REQ-BUS-015–016 | REQ-CART-016–017 | GLOSSARY.md §§12, 23; API.md §42 | Cart, Checkout, Order |
| REQ-PRC-010 | PRODUCT.md §§16.6, 17.1 | REQ-BUS-011–012, 015–016 | REQ-CART-016–017 | GLOSSARY.md §12; API.md §42 | Cart, Checkout |
| REQ-PRC-011 | PRODUCT.md §§14.4, 16.6 | REQ-BUS-011–012, 015–016 | REQ-CART-016–017 | GLOSSARY.md §12; SECURITY-STANDARDS.md §35 | Customer, Cart, Checkout |
| REQ-PRC-012 | PRODUCT.md §§16.6, 24 | REQ-BUS-016, 048 | — | DECISIONS.md §§25, 29 | Product governance |
| REQ-PRC-013 | PRODUCT.md §§16.10, 20, 24 | REQ-BUS-012, 015–016, 041, 054 | REQ-CART-016–017 | GLOSSARY.md §23; API.md §42 | Pricing, Order, Finance |
| REQ-PRC-014 | PRODUCT.md §§16.5–16.6, 24 | REQ-BUS-012, 015–016, 029 | REQ-CUS-036; REQ-INV-026; REQ-CART-021 | AGENTS.md §31.8 | Shipping, Checkout |
| REQ-PRC-015 | PRODUCT.md §§7, 16.6–16.7 | REQ-BUS-007, 011–012, 032, 039 | REQ-CUS-004–005, 031, 039–040 | SECURITY-STANDARDS.md §§10–12, 35 | Customer, Identity |
| REQ-PRC-016 | PRODUCT.md §§13, 16.2 | REQ-BUS-017–019 | REQ-INV-002–016; REQ-CART-014–015 | ARCHITECTURE.md §20.3 | Inventory |
| REQ-PRC-017 | PRODUCT.md §§14.3–14.4, 16.1, 30.3 | REQ-BUS-009, 012, 015–016 | REQ-CART-003, 016–018 | UI.md §26 | Cart, Checkout |
| REQ-PRC-018 | PRODUCT.md §§13, 14.4, 16.1–16.3 | REQ-BUS-011–013, 015 | REQ-CUS-033; REQ-INV-023; REQ-CART-018 | ARCHITECTURE.md §25.3; API.md §43 | Checkout |
| REQ-PRC-019 | PRODUCT.md §§16.1, 16.4, 17.4 | REQ-BUS-021–023 | REQ-CUS-034; REQ-CART-019 | ARCHITECTURE.md §40.5 | Order |
| REQ-PRC-020 | PRODUCT.md §§14.5–14.6, 16.3 | REQ-BUS-024–028 | REQ-CUS-035; REQ-INV-025; REQ-CART-020 | ARCHITECTURE.md §20.2 | Payment |
| REQ-PRC-021 | PRODUCT.md §§16.11, 24 | REQ-BUS-022, 030, 048 | — | GLOSSARY.md §§8–9, 24; API.md §45 | Order, Return, Refund, Payment |
| REQ-PRC-022 | PRODUCT.md §§16.12, 24 | REQ-BUS-052 | REQ-CUS-042 | SECURITY-STANDARDS.md §§9, 27, 35 | Security, Operations |
| REQ-PRC-023 | PRODUCT.md §§5.4, 16.1, 16.6, 17.4 | REQ-BUS-014–016, 042 | — | API.md §§14, 42 | Support, Checkout, Order |
| REQ-PRC-024 | PRODUCT.md §§5.4, 14.4, 17.2 | REQ-BUS-012, 016, 042 | REQ-CART-017–018 | UI.md §23; API.md §§42–43 | Cart, Checkout |
| REQ-PRC-025 | PRODUCT.md §§5.6, 17.2, 23 | REQ-BUS-013, 036, 042 | — | DATABASE.md §§12, 16, 51–52; API.md §§22, 24 | Pricing callers |
| REQ-PRC-026 | PRODUCT.md §§5.5, 7, 16.7 | REQ-BUS-032–033 | REQ-CUS-004–005, 039 | ARCHITECTURE.md §30.4; SECURITY-STANDARDS.md §§10–12 | Customer, Administration |
| REQ-PRC-027 | PRODUCT.md §§5.5, 16.7, 20 | REQ-BUS-039, 042 | REQ-CUS-040 | SECURITY-STANDARDS.md §§14, 27, 35; API.md §§52–53, 60–61 | All Pricing interfaces |
| REQ-PRC-028 | PRODUCT.md §§5.6, 17.2, 35.4 | REQ-BUS-035–036, 042, 045 | — | ARCHITECTURE.md §20.5; DATABASE.md §52; API.md §58 | Operations, support |
| REQ-PRC-029 | PRODUCT.md §§5.7, 16.9, 30 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md §§13, 24, 26, 29; UI.md §§23, 26, 50 | Storefront, Checkout |
| REQ-PRC-030 | PRODUCT.md §§10, 18.4, 20 | REQ-BUS-038, 043 | — | PERFORMANCE.md §§2, 21, 46–48, 57–60 | All Pricing consumers |
| REQ-PRC-031 | PRODUCT.md §§5.5, 17.4 | REQ-BUS-033–034 | — | SECURITY-STANDARDS.md §27; DATABASE.md §42 | Administration, operations |
| REQ-PRC-032 | PRODUCT.md §§13, 17, 23 | REQ-BUS-045–047 | — | ARCHITECTURE.md §14; EVENTS.md §§5–39, 43–45 | Event consumers |
| REQ-PRC-033 | PRODUCT.md §§13, 22 | REQ-BUS-014–016, 032, 047 | REQ-PRD-052; REQ-CUS-047; REQ-INV-035; REQ-CART-031 | API.md §§14, 42–43, 52–58, 70–72 | Contract consumers |
| REQ-PRC-034 | PRODUCT.md §§5.4–5.6, 17.2, 23 | REQ-BUS-005–006, 012, 014–016, 032, 042, 045 | REQ-PRD-040; REQ-CUS-048; REQ-CART-032 | API.md §§52–58; SECURITY-STANDARDS.md §35 | All Pricing workflows |
| REQ-PRC-035 | PRODUCT.md §§26.4, 35 | REQ-BUS-012, 014–016, 037–039, 042, 047 | REQ-PRD-046; REQ-CUS-031; REQ-INV-003; REQ-CART-016–018 | TESTING-STANDARDS.md §§7–24, 28, 33–34 | Verification evidence |

## 37. Open Product Decisions

The current Approved `.ai/core/PRODUCT.md` §24 contains **30 Open Product Decisions**. **8 are materially relevant to Pricing**. This Specification does not resolve them.

| Exact Product Decision | Pricing boundary affected |
| --- | --- |
| Shipping provider, service levels, delivery areas, and fee policy | Pricing cannot determine a delivery charge without a governed Shipping handoff and Approved fee policy. |
| Free-delivery threshold and promotional treatment | Pricing cannot establish a threshold or whether a delivery benefit is promotional. |
| Tax-inclusive display and invoice requirements | Pricing cannot select Tax Inclusive or Tax Exclusive presentation or Invoice requirements. |
| Returns, exchanges, and refund policy | Pricing cannot determine post-purchase recalculation, reversal, or Refund treatment. |
| Voucher and promotion stacking policy | Pricing cannot select Promotion Combination, precedence, or stacking. |
| South African tax-display, invoice, and credit-note policy | Pricing cannot establish jurisdictional tax display or commercial-document policy. |
| Fraud-screening approach and manual-review workflow | Pricing cannot select fraud eligibility, blocking, or review behavior. |
| Gift cards, store credit, and promotional credit policy | Pricing cannot assign authority or calculation treatment for these unresolved commercial inputs. |

## 38. Risks

| Risk | Required control direction |
| --- | --- |
| Stale commercial state is presented as current | Revalidate governed inputs and distinguish stale, changed, and uncertain outcomes. |
| Currency or precision is mishandled | Preserve explicit Money and Currency semantics and reject unsafe mixing or conversion. |
| Cart presentation is mistaken for locked Price | Preserve Cart intent and require Pricing revalidation before commitment. |
| Promotion or Voucher policy is invented locally | Keep unresolved values subject to Product governance and fail safely on conflict. |
| Pricing authority leaks into another Domain | Preserve Product, Inventory, Checkout, Order, Payment, Shipping, Customer, and Administration ownership. |
| Historical Order values are rewritten | Keep current Pricing truth separate from immutable Order Snapshots. |
| Customer-specific outcomes leak | Enforce server-side Authorization, minimization, and context-safe disclosure. |
| Duplicate or inconsistent evaluation creates conflicting outcomes | Preserve deterministic context, duplicate safety, and explicit uncertainty. |
| Internal or abuse-sensitive rule detail leaks | Protect errors, logs, Contracts, events, and Projections proportionately. |
| A required dependency is unavailable | Return explicit unavailable or uncertain state and provide governed recovery. |
| Tax uncertainty is represented as production-ready truth | Block reliance until qualified Approved policy exists. |
| Shipping commercial calculation becomes Shipping authority | Consume governed Shipping inputs without owning operational Shipping truth. |

## 39. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/inventory/inventory-domain.md`
- `specifications/domains/cart/cart-domain.md`

## 40. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-01 | Draft | Established initial Pricing authority, Money and Currency integrity, Price, Discount, Promotion, Voucher, tax and shipping-commercial boundaries, cross-domain separation, revalidation, failure, recovery, quality, Acceptance Criteria, and traceability. |
| 1.0.0 | 2026-09-01 | Approved | Promoted the Pricing Domain Specification to Approved status after comprehensive governance, authority, terminology, Acceptance Criteria, traceability, structure, implementation-neutrality, and whitespace validation. |

## 41. Final Validation

Before material revision, approval, or implementation reliance, reviewers MUST validate:

1. metadata states version `1.0.0`, status Approved, and `authoritative: false`, and Requirements are normative only within the Pricing Domain;
2. scope code `PRC` and stable sequential `REQ-PRC-NNN` identifiers are used without a SPEC identifier;
3. canonical terminology and Pricing authority remain aligned with `GLOSSARY.md` and governing sources;
4. Product and Product Variant, Customer, Inventory, Cart, Checkout, Order, Payment, Shipping, Return and Refund, fraud, and Administration boundaries remain explicit;
5. Money, Currency, Price, Discount, Promotion, Voucher, Tax, shipping-commercial, determinism, stale-state, concurrency, and duplicate-safety obligations remain complete and non-duplicative;
6. Open Product Decisions match current `PRODUCT.md`, state the total and relevant counts, and remain unresolved;
7. security, Authorization, Sensitive Data, failure, recovery, accessibility, performance, audit, events, Contracts, and testing remain traceable and testable;
8. Acceptance Criteria and Requirement Traceability contain exactly one materially complete row per Pricing Requirement and every citation exists;
9. Related Documents exist, remain relevant, and do not self-reference this Specification;
10. no Product policy, Architecture, technology, persistence design, Contract shape, provider, algorithm, threshold, numerical target, or implementation mechanism has been selected;
11. headings are sequential, sections are non-empty, Markdown tables are valid, and no unfinished marker or merge-conflict marker remains;
12. canonical-term review determines whether a coordinated `GLOSSARY.md` amendment is genuinely required and otherwise leaves it unchanged;
13. whitespace validation passes and the exact change scope contains only this Specification and any necessary coordinated Glossary amendment; and
14. repository state is accurately reported for the applicable review stage, including whether changes remain unstaged, uncommitted, and unpushed.
