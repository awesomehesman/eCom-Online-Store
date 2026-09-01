---
title: Cart Domain
version: 1.0.0
status: Approved
owner: Product and Engineering
last_updated: 2026-09-01
authoritative: false
---

# Cart Domain

## 1. Purpose

This Specification defines Cart-domain Requirements for shopping intent, Cart Items, selected Product Variant references, intended quantity, association and isolation, mutation, lifecycle boundaries, validation, concurrency, downstream authority separation, recovery, testing, and traceability.

This Specification is Approved and is normative only within its assigned Cart-domain scope. It remains `authoritative: false` and subordinate to governing core sources under the `AGENTS.md` Decision Hierarchy.

## 2. Scope and Authority

**Requirement scope code:** `CART`

Requirement identifiers use `REQ-CART-NNN` and remain stable under `.ai/core/DOCUMENTATION-STANDARDS.md`. This Specification refines Approved Product and business Requirements without defining implementation Architecture or resolving Product policy.

### REQ-CART-001 — Approved Authority and Scope

This Approved Specification MUST remain `authoritative: false`, apply normatively only within the Cart Domain, remain subordinate to governing core sources, and use `CART` as its Requirement scope code.

## 3. Domain Context

Cart preserves provisional shopping intent between Product evaluation and Checkout. It collaborates with Product, Pricing, Inventory, Customer, Identity, Checkout, Order, Payment, Shipping, and Administration without becoming authoritative for their state.

## 4. Canonical Terminology

This Specification uses Cart, Cart Item, Product, Product Variant, Price, Discount, Promotion, Voucher, Stock, Stock Reservation, Available-to-Sell, Customer, Principal, Session, Account, Wishlist, Checkout, Order, Order Item, Payment, Shipment, Staff User, Authentication, Authorization, Audit Record, Sensitive Data, Secret, Contract, Domain Event, Integration Event, Projection, and Risk with the meanings in `.ai/core/GLOSSARY.md`.

## 5. Cart Ownership and Authority

### REQ-CART-002 — Cart Intent Authority

Cart MUST own authoritative supported shopping intent, including Cart existence, Cart Item membership, selected Product Variant references, intended quantities, the governed Cart association, and accepted Cart mutation outcomes. Cart-owned intent MUST remain provisional and MUST NOT imply commercial commitment.

### REQ-CART-003 — Downstream Authority Separation

Cart MUST NOT become authoritative for Product or Product Variant identity, definition, content, Product Media, publication, structural sellability, Category, Price, Discount, Promotion, Voucher, tax, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, Checkout orchestration, Order truth, Payment truth, Shipment truth, fulfilment truth, Customer identity, Authentication, Authorization policy, Analytics truth, or Administration policy.

### REQ-CART-004 — Cart Association and Isolation

Every Cart MUST be associated with a valid governed Customer, Principal, Session, guest, or other context only where Approved policy permits it. A supplied Cart identifier MUST NOT establish ownership or access, and one context MUST NOT read or mutate another Cart without governed Authorization.

## 6. Product and Product Variant Boundary

### REQ-CART-005 — Governed Product Variant Reference

A Cart Item MUST reference a valid governed Product Variant without creating or redefining Product identity, Product Variant identity, content, Product Media, publication, visibility, or structural sellability. Cart MUST NOT fabricate a Product Variant or treat retained Product information as current Product truth.

### REQ-CART-006 — Product Change Safety

An unknown or removed Product, unknown or removed Product Variant, unpublished Product, Product Variant that is not structurally sellable under Product authority, or materially changed Product state MUST produce a safe, distinguishable Cart outcome and governed recovery where permitted. Cart MUST NOT invent whether an existing Cart Item remains eligible when Product policy leaves that outcome unresolved. Stock, Available-to-Sell, insufficient Inventory, and unavailable Inventory remain governed by Inventory authority and REQ-CART-015.

## 7. Cart and Cart Item

### REQ-CART-007 — Cart Item Membership

Cart MUST preserve which supported Product Variant references and intended quantities are members of the Cart. A Cart Item MUST remain part of Cart intent and MUST NOT become an Order Item, Stock Reservation, Price snapshot, or authoritative Product copy.

### REQ-CART-008 — Cross-Cart Integrity

A Cart Item operation MUST affect only the intended permitted Cart and applicable Cart Item. Unknown, inaccessible, mismatched, ambiguous, or cross-Cart references MUST NOT expose or mutate protected Cart state.

## 8. Quantity and Mutation

No maximum Cart size, maximum item quantity, minimum order quantity, decimal-quantity policy, automatic cap, SKU format, Cart identifier format, or business threshold is established here.

### REQ-CART-009 — Governed Cart Mutation

Adding a Product Variant, changing intended quantity, and removing a Cart Item MUST produce an explicit, consistent Cart outcome for the correct Cart. Invalid or unsupported quantity, including zero where it is not a governed removal instruction, MUST be rejected with safe correction guidance without inventing numerical limits.

### REQ-CART-010 — Duplicate Add Semantics

A repeated add for a Product Variant MUST follow an explicit governed Cart outcome and MUST NOT unintentionally multiply Cart Item membership or quantity. This Specification does not select whether valid repeated adds combine, replace, or remain distinct.

## 9. Cart Lifecycle Boundaries

### REQ-CART-011 — Governed Cart Lifecycle Outcomes

Cart creation, active use, mutation, validation, clearing, abandonment, expiration, merge, transition toward Checkout, and recovery MUST be represented only where Approved policy governs the applicable outcome. Cart MUST NOT invent a lifecycle state machine, expiration or persistence duration, automatic cleanup, or clearing trigger.

### REQ-CART-012 — Unresolved Persistence and Merge Policy

Guest persistence, authenticated persistence, expiration, anonymous-to-authenticated migration, Cart merge, merge precedence, conflict handling, and post-Checkout or post-purchase clearing MUST remain unresolved until Approved Product policy establishes them. No retained or merged state MUST silently overwrite accepted Cart intent.

## 10. Customer / Guest Boundary

### REQ-CART-013 — Customer and Guest Non-Authority

Cart MAY use governed Customer, Principal, Session, Account, or guest context for association but MUST NOT define Customer identity, Account, Address, Preference, Consent, Wishlist, Authentication, or Authorization policy. Guest and authenticated capabilities MUST remain limited to Approved Product policy.

## 11. Inventory Boundary

### REQ-CART-014 — Inventory Non-Authority

Cart MUST NOT own, establish, infer, or overwrite Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection. Cart presence and intended quantity MUST NOT reserve Stock, prove reserved quantity, guarantee availability, or establish purchase eligibility.

### REQ-CART-015 — Inventory Validation and Staleness

Cart MAY request governed Inventory validation through a Contract where policy permits. Displayed, cached, retained, or stale availability MUST remain non-authoritative; unknown, insufficient, changed, or unavailable Inventory state MUST be represented safely and revalidated before governed commitment.

## 12. Pricing Boundary

### REQ-CART-016 — Pricing Non-Authority

Cart MAY present governed current or projected Price, Discount, Promotion, Voucher, tax, totals, or savings, but MUST NOT calculate or establish authoritative commercial values. Displayed values MUST remain distinguishable from Cart intent and subject to trusted Checkout revalidation.

### REQ-CART-017 — Pricing Change Safety

Stale, unknown, or changed Price, Discount, Promotion, Voucher, tax, or total state MUST NOT be represented as locked or final. Applicable material changes MUST be explainable before commitment; this Specification does not define snapshots, locking, expiry, stacking, precedence, Voucher timing, rounding, or tax calculation.

## 13. Checkout Boundary

### REQ-CART-018 — Checkout Revalidation Boundary

Checkout MAY consume governed Cart intent through Contracts, but Cart MUST NOT become Checkout or prove Checkout success. Before commitment, current authoritative Product Variant sellability, Pricing, Inventory, Customer Authorization, and other required downstream truth MUST be revalidated; stale Cart or independently cached Projections MUST NOT substitute for that truth.

## 14. Order Boundary

### REQ-CART-019 — Order Non-Authority

Cart contents MAY contribute governed input to Checkout and, only through the governed Checkout outcome, MAY contribute to Order creation. Cart MUST NOT create or establish Order, Order Item commercial truth, Order status, or confirmed commercial history. Cart clearing timing after Order activity remains unresolved.

## 15. Payment Boundary

### REQ-CART-020 — Payment Non-Authority

Cart MUST NOT establish Payment, Payment Authorization, success, Capture, or Settlement. Payment state MUST NOT be inferred from Cart mutation, clearing, navigation, or client state, and Payment outcomes MUST NOT silently redefine Cart intent.

## 16. Shipping and Fulfilment Boundary

### REQ-CART-021 — Shipping and Fulfilment Non-Authority

Cart MUST NOT establish Shipment, delivery, tracking, carrier, or fulfilment truth. Any delivery information presented with Cart intent remains governed by its owning Domain and subject to downstream validation.

## 17. Concurrency and Duplicate Safety

### REQ-CART-022 — Concurrent Mutation Integrity

Concurrent, duplicate, retried, replayed, or stale Cart mutations MUST NOT silently overwrite accepted intent or multiply Cart Item effects unintentionally. Conflicting and stale updates MUST be distinguishable and support safe refresh, retry, reconciliation, or escalation where governed.

### REQ-CART-023 — Uncertain Mutation Safety

An ambiguous or uncertain Cart mutation result MUST be verified against authoritative Cart state before blind repetition where duplicate effects matter. Recovery MUST preserve accepted intent without fabricating Product, Pricing, Inventory, Checkout, Order, or Payment truth.

## 18. Authentication and Authorization

### REQ-CART-024 — Cart Authorization

Where Authentication is required, it MUST establish the Principal, while trusted server-side Authorization MUST govern Cart access and mutation. UI state, route, Role label, client Claims, browser state, or supplied Cart identifier MUST NOT grant authority; Staff User access, if governed, MUST be separately authorized and least-privileged.

## 19. Security and Sensitive Data

### REQ-CART-025 — Cart Data Protection

Cart behavior MUST enforce Resource isolation, minimize unnecessary Customer PII, and protect Secrets, credentials, Sensitive Data, operational detail, and internal implementation detail in access, logs, errors, Contracts, events, and Projections. Failure behavior MUST avoid protected Resource enumeration and fail safely.

## 20. Recovery / Reconciliation

### REQ-CART-026 — Cart Recovery and Reconciliation

Uncertain mutation, duplicate mutation, stale Cart, conflict, invalid Product Variant reference, Product Variant that is no longer structurally sellable under Product authority, unavailable or insufficient Inventory, stale Price, stale Inventory, invalid or expired governed context, and downstream dependency failure MUST support distinguishable safe recovery or reconciliation where permitted. Repair MUST be authorized, duplicate-safe, observable, and MUST NOT silently invent external Domain truth.

## 21. Accessibility

### REQ-CART-027 — Accessible Cart Outcomes

Customer-facing Cart contents, quantity controls, removal, validation failures, presented Price or availability changes, errors, and recovery guidance MUST support applicable WCAG 2.2 AA outcomes, including keyboard and assistive-technology use where relevant, without claiming certification or requiring AAA.

## 22. Performance

### REQ-CART-028 — Bounded Cart Delivery

Cart operations and Contracts MUST support bounded and observable delivery appropriate to their use under governing performance sources. Degradation MUST NOT convert stale or uncertain Product, Price, Inventory, Cart association, or Authorization state into trusted truth; no numerical target is established here.

## 23. Audit

### REQ-CART-029 — Proportional Cart Audit Evidence

Material privileged, administrative, reconciliation, security-sensitive, or other high-Risk Cart actions MUST produce proportional Audit Records where governing Risk requires them. Ordinary Customer reads and routine low-Risk Cart mutations MUST NOT automatically require high-Risk audit treatment.

## 24. Events

### REQ-CART-030 — Conditional Cart Events

A Domain Event or Integration Event MUST be required only where Architecture, an Approved Contract, downstream reliability, or synchronized governance establishes the need. Any governed event MUST preserve Cart authority, Authorization, data protection, compatibility, and duplicate or replay safety without making a consumer Projection authoritative; no event name, schema, topic, payload, broker, queue, delivery guarantee, or choreography is defined here.

## 25. Contract Impacts

### REQ-CART-031 — Cart Contract Boundary

Future Cart Contracts MUST preserve Cart authority, correct Cart and Product Variant association, Authorization, Resource isolation, concurrency, duplicate-effect safety, stale-state handling, privacy, security, compatibility, and distinguishable failure semantics. This Specification defines no path, method, status code, JSON field, DTO, class, service, table, database schema, provider payload, or event schema.

## 26. Failure and Recovery

### REQ-CART-032 — Explicit Cart Failure Outcomes

Applicable unknown Cart, inaccessible Cart, invalid Cart context, unknown Product Variant, Product Variant that is not structurally sellable under Product authority, invalid quantity, stale Cart, stale Product, stale Price, stale Inventory, duplicate mutation, replayed mutation, concurrent mutation, conflicting mutation, cross-Cart mutation, unauthorized access, expired governed context, downstream dependency failure, and uncertain mutation result MUST each produce a distinguishable, safe, explainable, and recoverable outcome where permitted.

Failure information MUST NOT expose Secrets, credentials, inaccessible protected Resource existence, database or infrastructure internals, security controls, or provider internals. An uncertain effect MUST NOT be represented as success or blindly repeated.

## 27. Testing

### REQ-CART-033 — Cart Verification Coverage

Verification MUST explicitly cover applicable positive and negative behavior, Cart creation, Cart Item add/change/remove, quantity validation, Product Variant association, stale Product, stale Price, stale Inventory, Inventory non-reservation, governed Customer or guest association, cross-Cart isolation, Authorization, duplicate/retry/replay behavior, concurrency, conflict, partial failure, recovery, Checkout, Order, Payment and Shipping boundaries, security, audit where applicable, accessibility, performance, governed events, and Contracts. No test framework, separate test-case identifier scheme, or numerical coverage target is selected.

## 28. Acceptance Criteria

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-CART-001 | Metadata states `1.0.0`, `Approved`, and `authoritative: false`; the text limits normative effect to Cart scope, preserves core-source precedence, and declares scope code `CART`. |
| REQ-CART-002 | Cart truth identifies Cart existence, Cart Item membership, Product Variant references, quantities, governed association, and accepted mutations while remaining provisional and non-committal. |
| REQ-CART-003 | Cart cannot establish or transition authority for Product, Product Variant identity or definition, Product content, Product Media, Product publication, Product structural sellability, Category, Price, Discount, Promotion, Voucher, tax, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, Overselling protection, Checkout orchestration, Order truth, Payment truth, Shipment truth, fulfilment truth, Customer identity, Authentication, Authorization policy, Analytics truth, or Administration policy. |
| REQ-CART-004 | Each Cart is associated only with a policy-permitted context; supplied identifiers cannot grant access, and another context cannot read or mutate it without Authorization. |
| REQ-CART-005 | A Cart Item retains a valid governed Product Variant reference without creating or changing Product-owned identity, content, media, publication, visibility, or sellability, and retained content cannot prove current Product truth. |
| REQ-CART-006 | An unknown Product, removed Product, unknown Product Variant, removed Product Variant, unpublished Product, Product Variant that is not structurally sellable under Product authority, and materially changed Product state each yield a safe, distinguishable outcome and governed recovery where permitted without inventing existing-item eligibility; Stock, Available-to-Sell, insufficient Inventory, and unavailable Inventory remain governed by REQ-CART-015. |
| REQ-CART-007 | Membership and quantity persist as Cart intent; a Cart Item cannot become an Order Item, Stock Reservation, Price snapshot, or Product copy. |
| REQ-CART-008 | A mutation affects only the permitted Cart and Cart Item; unknown, inaccessible, mismatched, ambiguous, and cross-Cart references expose and change no protected state. |
| REQ-CART-009 | Adding a Product Variant, changing quantity, and removing a Cart Item each produce an explicit consistent outcome for the correct Cart; invalid quantity, unsupported quantity, and zero quantity where zero is not a governed removal instruction are rejected with safe correction guidance, without establishing zero-means-remove or an invented numerical limit. |
| REQ-CART-010 | Repeating an add cannot unintentionally multiply membership or quantity, and no combine, replace, or distinct-item policy is implied. |
| REQ-CART-011 | Cart creation, active use, mutation, validation, clearing, abandonment, expiration, merge, transition toward Checkout, and recovery exist only where Approved policy governs them; no lifecycle state machine, expiration duration, persistence duration, automatic cleanup, or clearing trigger is invented. |
| REQ-CART-012 | Persistence, expiration, migration, merge, precedence, conflict, and clearing remain unresolved, and retained or merged state cannot silently overwrite accepted intent. |
| REQ-CART-013 | Cart may use governed Customer, Principal, Session, Account, or guest context for association but cannot define Customer identity, Account, Address, Preference, Consent, Wishlist, Authentication, or Authorization policy; guest and authenticated capabilities remain limited to Approved Product policy. |
| REQ-CART-014 | Cart and quantity cannot create, infer, alter, or prove any Inventory concept, reservation, availability guarantee, or purchase eligibility. |
| REQ-CART-015 | Inventory validation occurs only through a governed Contract where policy permits; displayed, cached, retained, and stale availability remain non-authoritative, while unknown, insufficient, changed, and unavailable Inventory state each produce a safe outcome and are revalidated before governed commitment. |
| REQ-CART-016 | Presented Price, Discount, Promotion, Voucher, tax, totals, and savings each remain governed presentations or Projections distinct from Cart intent and authoritative Pricing; Cart calculates or establishes none of those authoritative commercial values, and trusted Checkout revalidation remains required. |
| REQ-CART-017 | Stale, unknown, or changed Price, Discount, Promotion, Voucher, tax, and totals are not represented as final; applicable material changes are explainable before commitment, and Cart defines no snapshot, locking, expiry, stacking, precedence, Voucher timing, rounding, or tax-calculation policy. |
| REQ-CART-018 | Checkout consumes Cart through governed Contracts, Cart neither becomes Checkout nor proves Checkout success, and Checkout revalidates applicable Product Variant structural sellability, Pricing, Inventory, Customer Authorization, and other required downstream authoritative truth before commitment; stale Cart and cached Projections cannot substitute for that truth. |
| REQ-CART-019 | Cart contents contribute input to Checkout and only through the governed Checkout outcome may contribute to Order creation; Cart cannot create Order truth, Order Item commercial truth, Order status, or confirmed commercial history, and clearing timing remains unresolved. |
| REQ-CART-020 | Cart cannot establish any listed Payment outcome; Cart or client state cannot prove Payment, and Payment cannot silently redefine intent. |
| REQ-CART-021 | Cart cannot establish Shipment, delivery, tracking, carrier, or fulfilment truth; presented delivery information remains owned and revalidated downstream. |
| REQ-CART-022 | Concurrent, duplicate, retried, replayed, and stale mutations cannot silently overwrite intent or multiply effects; conflict yields a governed recovery path. |
| REQ-CART-023 | An ambiguous or uncertain result is verified before repetition, preserves accepted intent, and fabricates no external Domain truth. |
| REQ-CART-024 | Where required, Authentication establishes Principal context and trusted server-side Authorization governs Cart access and mutation; UI state, route, Role label, client Claims, browser state, and a supplied Cart identifier cannot grant authority, while Staff User access is separately authorized and least-privileged. |
| REQ-CART-025 | Cart enforces Resource isolation, minimizes unnecessary Customer PII, and protects Secrets, credentials, Sensitive Data, operational detail, and internal implementation detail across access, logs, errors, Contracts, events, and Projections without implying all Cart data is Sensitive Data; protected Resource enumeration is prevented and failures remain safe. |
| REQ-CART-026 | Uncertain mutation, duplicate mutation, stale Cart, conflict, invalid Product Variant reference, Product Variant no longer structurally sellable under Product authority, unavailable or insufficient Inventory, stale Price, stale Inventory, invalid governed context, expired governed context, and downstream dependency failure each support distinguishable safe recovery or reconciliation where permitted; repair is authorized, duplicate-safe, observable, and cannot fabricate external Domain truth. |
| REQ-CART-027 | Cart contents and all listed interactions and changes meet applicable WCAG 2.2 AA outcomes, including relevant keyboard and assistive-technology use, without certification or AAA claims. |
| REQ-CART-028 | Delivery is bounded and observable; degradation cannot promote stale or uncertain state to trusted truth, and no numerical target is asserted. |
| REQ-CART-029 | Applicable material high-Risk actions produce proportional Audit Records while ordinary reads and low-Risk mutations are not automatically treated as high-Risk. |
| REQ-CART-030 | A Cart event is required only where Architecture, an Approved Contract, a downstream reliability Requirement, or synchronized governance establishes the need; governed events preserve Cart authority, Authorization, data protection, compatibility, duplicate safety, replay safety, and non-authoritative consumer Projections, while defining no event name, schema, topic, payload, broker, queue, delivery guarantee, or choreography. |
| REQ-CART-031 | Future Contracts preserve Cart authority, correct Cart association, correct Product Variant association, Authorization, Resource isolation, concurrency safety, duplicate-effect safety, stale-state handling, privacy, security, compatibility, and distinguishable failure semantics; this Specification defines no path, HTTP method, status code, JSON field, DTO, class, service, table, database schema, provider payload, or event schema. |
| REQ-CART-032 | Unknown Cart, inaccessible Cart, invalid Cart context, unknown Product Variant, Product Variant not structurally sellable under Product authority, invalid quantity, stale Cart, stale Product, stale Price, stale Inventory, duplicate mutation, replayed mutation, concurrent mutation, conflicting mutation, cross-Cart mutation, unauthorized access, expired governed context, downstream dependency failure, and uncertain mutation result each produce a distinguishable, safe, explainable, and recoverable outcome where permitted. Failures conceal Secrets, credentials, inaccessible protected Resource existence, database internals, infrastructure internals, security controls, and provider internals; uncertain effects are neither represented as success nor blindly repeated. |
| REQ-CART-033 | Verification evidence covers applicable positive and negative behavior, Cart creation, Cart Item add, Cart Item quantity change, Cart Item removal, invalid quantity, Product Variant association, stale Product, stale Price, stale Inventory, Inventory non-reservation, governed Customer association, governed guest association where applicable, cross-Cart isolation, Authorization, duplicate mutation, retry, replay, concurrency, conflict, partial failure, recovery, Checkout, Order, Payment, and Shipping boundaries, security, audit where applicable, accessibility, performance, governed events, and Contracts; no test framework, separate test-case identifier scheme, or numerical coverage target is selected. |

## 29. Requirement Traceability

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-CART-001 | PRODUCT.md §§21, 36, 38 | REQ-BUS-047, 048 | — | AGENTS.md §5; DOCUMENTATION-STANDARDS.md | Cart governance |
| REQ-CART-002 | PRODUCT.md §§12.2, 13, 14.3, 30.3 | REQ-BUS-009 | REQ-CUS-033 | GLOSSARY.md | Cart, Checkout |
| REQ-CART-003 | PRODUCT.md §§5.4, 13, 16 | REQ-BUS-009, 017, 021, 024, 029, 044 | REQ-PRD-047; REQ-CAT-033; REQ-CUS-033; REQ-INV-022–026 | AGENTS.md §31 | Cross-domain |
| REQ-CART-004 | PRODUCT.md §§7.1–7.2, 16.7, 24 | REQ-BUS-007, 032, 033 | REQ-CUS-005, 039 | SECURITY-STANDARDS.md | Customer, Identity |
| REQ-CART-005 | PRODUCT.md §§14.2–14.3, 16.8 | REQ-BUS-005, 006, 009 | REQ-PRD-007, 010, 021 | GLOSSARY.md | Product, Cart |
| REQ-CART-006 | PRODUCT.md §§5.4, 14.2–14.4, 17 | REQ-BUS-006, 042 | REQ-PRD-037 | UI.md §23 | Product, Cart, Checkout |
| REQ-CART-007 | PRODUCT.md §§14.3, 16.2, 16.4 | REQ-BUS-009, 017, 021 | REQ-INV-022, 024 | GLOSSARY.md | Cart, Order, Inventory |
| REQ-CART-008 | PRODUCT.md §§5.5, 16.7 | REQ-BUS-010, 032, 033 | REQ-CUS-005, 039 | SECURITY-STANDARDS.md | Cart security |
| REQ-CART-009 | PRODUCT.md §§14.3, 30.3 | REQ-BUS-006, 009, 010 | — | API.md §43; ACCESSIBILITY.md §13 | Cart UI, backend |
| REQ-CART-010 | PRODUCT.md §§14.3, 23 | REQ-BUS-010 | — | API.md §§22, 43 | Cart mutation |
| REQ-CART-011 | PRODUCT.md §§5.9, 17, 24 | REQ-BUS-009, 048 | — | GLOSSARY.md; DOCUMENTATION-STANDARDS.md | Cart lifecycle |
| REQ-CART-012 | PRODUCT.md §§5.6, 5.9, 24 | REQ-BUS-007, 009, 013 | — | PRODUCT.md §24 | Customer, Checkout |
| REQ-CART-013 | PRODUCT.md §§7.1–7.2, 16.7, 24 | REQ-BUS-007, 051 | REQ-CUS-001–005, 033 | SECURITY-STANDARDS.md | Customer, Identity, Cart |
| REQ-CART-014 | PRODUCT.md §§14.3, 16.2 | REQ-BUS-009, 010, 017–019 | REQ-INV-014, 022 | ARCHITECTURE.md §20.3 | Inventory, Cart |
| REQ-CART-015 | PRODUCT.md §§14.3–14.4, 16.2, 30.3 | REQ-BUS-012, 018 | REQ-INV-008, 009, 015, 022 | API.md §§41, 43 | Inventory, Checkout |
| REQ-CART-016 | PRODUCT.md §§14.3–14.4, 16.1, 30.3 | REQ-BUS-009, 012, 015, 016 | REQ-PRD-046 | API.md §§42–43; UI.md §26 | Pricing, Checkout |
| REQ-CART-017 | PRODUCT.md §§5.4, 14.4, 16.1, 16.6, 24 | REQ-BUS-012, 016 | — | API.md §42 | Pricing, Cart, Checkout |
| REQ-CART-018 | PRODUCT.md §§13, 14.4, 16.1–16.2, 30.4 | REQ-BUS-011–013 | REQ-CUS-033; REQ-INV-023 | API.md §43 | Checkout |
| REQ-CART-019 | PRODUCT.md §§14.4–14.6, 16.4 | REQ-BUS-021–023 | REQ-CUS-034; REQ-INV-024 | ARCHITECTURE.md §9 | Order |
| REQ-CART-020 | PRODUCT.md §§5.4, 14.5–14.6, 16.3 | REQ-BUS-024–026 | REQ-CUS-035; REQ-INV-025 | ARCHITECTURE.md §20.2 | Payment |
| REQ-CART-021 | PRODUCT.md §§13, 14.7, 16.5 | REQ-BUS-029 | REQ-CUS-036; REQ-INV-026 | AGENTS.md §31.8 | Shipping |
| REQ-CART-022 | PRODUCT.md §§5.6, 17, 23 | REQ-BUS-010, 013 | — | DATABASE.md §§16, 19; API.md §24 | Cart mutation |
| REQ-CART-023 | PRODUCT.md §§5.4, 5.6, 17.2 | REQ-BUS-010, 013, 036 | — | DATABASE.md §§51–52 | Recovery |
| REQ-CART-024 | PRODUCT.md §§5.5, 7, 16.7 | REQ-BUS-032, 033 | REQ-CUS-004, 039 | ARCHITECTURE.md §30.4; SECURITY-STANDARDS.md | Identity, Cart |
| REQ-CART-025 | PRODUCT.md §§5.5, 16.7, 20 | REQ-BUS-039, 040, 042 | REQ-CUS-040 | SECURITY-STANDARDS.md | All Cart interfaces |
| REQ-CART-026 | PRODUCT.md §§5.6, 17.2, 35.4 | REQ-BUS-013, 035, 036, 045 | — | DATABASE.md §52; API.md §§54, 58 | Operations, support |
| REQ-CART-027 | PRODUCT.md §§5.7, 16.9, 30.3 | REQ-BUS-037, 042 | — | ACCESSIBILITY.md §§13, 28; UI.md §50 | Cart UI |
| REQ-CART-028 | PRODUCT.md §§10, 18.4, 20 | REQ-BUS-038, 043 | — | PERFORMANCE.md §§2, 21, 46–48, 58–59; ARCHITECTURE.md §4.4 | Cart delivery |
| REQ-CART-029 | PRODUCT.md §§5.5, 17.4 | REQ-BUS-034 | — | SECURITY-STANDARDS.md §27; DATABASE.md §42 | Audit |
| REQ-CART-030 | PRODUCT.md §§13, 17, 23 | REQ-BUS-045, 047 | — | ARCHITECTURE.md §14; EVENTS.md | Event consumers |
| REQ-CART-031 | PRODUCT.md §§13, 22 | REQ-BUS-009, 010, 047 | REQ-PRD-021; REQ-CUS-033; REQ-INV-035 | API.md §§41–43 | Cart Contracts |
| REQ-CART-032 | PRODUCT.md §§5.4, 5.6, 17.2, 23 | REQ-BUS-006, 010, 013, 036, 042, 045 | — | API.md §§52–58; SECURITY-STANDARDS.md | All Cart workflows |
| REQ-CART-033 | PRODUCT.md §§26.4, 35 | REQ-BUS-009–013, 037–039, 047 | REQ-PRD-021, 037; REQ-CUS-033, 039; REQ-INV-022–023 | TESTING-STANDARDS.md; ACCESSIBILITY.md §§64–68 | Verification evidence |

## 30. Open Product Decisions

The current Approved `.ai/core/PRODUCT.md` §24 contains **5 Cart-relevant open Product Decisions**. This Specification does not resolve them.

| Concern | Cart boundary pending an Approved Product Decision |
| --- | --- |
| Guest Checkout versus mandatory Account | Guest and authenticated Cart association and supported continuation toward Checkout remain policy-dependent. |
| Back-order and pre-order support | Cart cannot decide whether an unavailable Product Variant may remain eligible for later commitment. |
| Low-stock and out-of-stock Customer messaging | Cart may present governed availability outcomes but cannot define wording, thresholds, or eligibility. |
| Voucher and Promotion stacking | Cart may present governed commercial outcomes but cannot define application, precedence, or stacking. |
| Administrative Role and Permission matrix | Any Staff User Cart access remains server-side authorized without defining Roles or Permissions. |

Cart expiration, persistence duration, abandonment treatment, anonymous-to-authenticated migration, merge behavior and precedence, and clearing timing are unresolved Cart scope boundaries, not additional Product Decisions in current `PRODUCT.md`.

## 31. Risks

| Risk | Required control direction |
| --- | --- |
| Stale Product, Price, or Inventory state | Preserve owning-Domain authority and revalidate before commitment. |
| Cart mistaken for Stock Reservation | Keep intent and Inventory reservation explicitly separate. |
| Unauthorized or cross-Cart access | Enforce server-side Authorization and Resource isolation. |
| Duplicate, lost, or concurrent mutation | Preserve accepted intent and make duplicates, conflicts, and uncertainty explicit. |
| Product Variant mismatch | Validate governed association without redefining Product truth. |
| Stale Cart used for Checkout | Require current downstream revalidation. |
| Customer or guest association mismatch | Bind access to governed context without inventing identity policy. |
| Downstream state mistaken for Cart truth | Keep external Projections from overwriting intent. |
| Cart state mistaken for Order truth | Preserve provisional intent and confirmed commercial-record separation. |
| Sensitive or internal data leakage | Minimize data and protect logs, errors, Contracts, events, and Projections. |
| Unresolved lifecycle policy embedded locally | Keep persistence, merge, expiry, and clearing reversible and unresolved. |

## 32. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
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

## 33. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-01 | Approved | Promoted the Cart Domain Specification after final governance, Cart authority, Cart Item, quantity, lifecycle boundaries, Product, Pricing, Inventory, Customer, Checkout, Order, Payment and Shipping boundaries, concurrency, security, accessibility, Acceptance Criteria, and traceability validation. |
| 0.1.0 | 2026-09-01 | Draft | Established the initial Cart Domain Specification covering Cart intent, Cart Items, quantity and mutation, lifecycle boundaries, Product, Pricing, Inventory, Customer, Checkout and downstream authority separation, concurrency, security, failure, Acceptance Criteria, and traceability. |

## 34. Final Validation

Before material revision, re-approval, or implementation reliance, reviewers MUST validate:

1. metadata accurately states version `1.0.0` Approved and `authoritative: false` is preserved;
2. scope code `CART` and stable sequential `REQ-CART-NNN` identifiers are used without a SPEC identifier;
3. canonical terminology and Cart authority remain aligned with `GLOSSARY.md` and governing sources;
4. Product, Product Variant, Pricing, Inventory, Customer or guest, Checkout, Order, Payment, Shipping, Administration, Authentication, and Authorization boundaries remain explicit;
5. Cart Item, quantity, lifecycle, mutation, concurrency, duplicate, stale-state, association, and isolation behavior remain complete and non-duplicative;
6. security, failure, recovery, accessibility, performance, audit, events, Contracts, and testing remain traceable and testable;
7. Acceptance Criteria and Requirement Traceability contain exactly one complete row per Cart Requirement;
8. Open Product Decisions match current `PRODUCT.md`, remain unresolved, and are distinguished from Cart scope boundaries;
9. Related Documents exist, remain relevant, and do not self-reference this Specification;
10. no Product policy, Architecture, technology, persistence, Contract shape, identifier format, lifecycle duration, merge policy, clearing timing, or implementation mechanism has leaked into Domain Requirements;
11. headings are sequential, sections are non-empty, tables are valid, and no unfinished marker or unsupported numerical target remains; and
12. each reviewed change contains only this Cart Specification and any coordinated governing-source amendment required by this Specification, each change is limited to its intended governance scope, whitespace validation passes for every changed file, and repository state is accurately reported for the applicable review stage.
