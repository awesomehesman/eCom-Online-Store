---
title: Customer Domain
version: 0.1.0
status: Draft
owner: Product and Engineering
last_updated: 2026-08-17
authoritative: false
---

# Customer Domain

## 1. Purpose

This Specification defines Customer-domain Requirements for Customer identity, Account business meaning, profile, Address, Preference, Consent, conditional Wishlist behavior, ownership isolation, registration outcomes, Customer history, privacy, security, downstream boundaries, testing, and traceability.

It refines the Approved Product and business baselines without defining Identity or Authentication implementation, Authorization policy, Product, Pricing, Inventory, Cart, Checkout, Order, Payment, Shipping, Administration, or Analytics behavior.

## 2. Scope and Authority

This document is Draft and non-normative. Once Approved, it is normative only within its assigned Customer-domain scope. Its `authoritative: false` metadata MUST remain false; approval does not make it a core Source of Truth.

`PRODUCT.md` remains the higher Product authority, and `specifications/business/business-requirements.md` remains the upstream business baseline. `GLOSSARY.md` owns canonical terminology, `ARCHITECTURE.md` owns Architecture, `SECURITY-STANDARDS.md` owns security controls, and Contracts remain separate. Conflicts MUST follow the `AGENTS.md` Decision Hierarchy.

**Requirement scope code:** `CUS`

Requirement identifiers use `REQ-CUS-NNN` and remain stable under `.ai/core/DOCUMENTATION-STANDARDS.md`.

## 3. Domain Context

The Customer Domain owns Customer identity and Customer-owned profile, Address, Preference, Consent, and other saved or derived Customer state only where Approved Product governance establishes that ownership. It supplies governed Customer context to other Domains without absorbing their authority.

Identity owns credentials and Sessions. Administration provides protected Staff User workflows over Customer capabilities. Product, Pricing, Inventory, Cart, Checkout, Order, Payment, Shipping, and Analytics remain authoritative for their respective state.

## 4. Canonical Terminology

This Specification uses Customer, Account, Address, Preference, Consent, Wishlist, Identity, Principal, Authentication, Authorization, Role, Permission, Claims, Session, Product, Product Variant, Product Media, Price, Inventory, Stock, Stock Reservation, Available-to-Sell, Order, Order Snapshot, Payment, Payment Provider, Payment Redirect, Payment Transaction, Shipment, Projection, Staff User, Audit Record, Sensitive Data, Secret, Contract, Domain Event, Integration Event, and Risk with the meanings in `GLOSSARY.md`.

A Customer is a business entity, a Principal is the authenticated actor in the current security context, and an Account is the authenticated profile representing a registered Customer. These concepts are distinct and are used according to their canonical meanings throughout this Specification. This Specification does not define Customer or Account lifecycle state names or an identifier format.

## 5. Customer Ownership

### REQ-CUS-001 — Customer-Domain Authority

The Customer Domain MUST own stable Customer identity, Customer-owned profile data, Addresses, Preferences, Consent records where governed, Customer-side lifecycle and ownership rules, and saved Customer state only where Approved Product governance establishes it.

### REQ-CUS-002 — Downstream Authority Separation

Customer or Account state MUST NOT become authoritative for Authentication implementation, Authorization policy, Product identity or publication, Price, Discount, Promotion, Voucher, Stock, Stock Reservation, Available-to-Sell, Cart, Checkout, Payment, Order, Shipment, Staff User workflow, or Analytics truth.

## 6. Customer Identity and Isolation

### REQ-CUS-003 — Stable Customer Identity

A Customer MUST have stable identity independent of mutable name, contact information, profile attributes, Address, Preference, Consent, Wishlist, credential, Principal, or Session state. Profile or access changes MUST NOT silently replace Customer identity.

No UUID, numeric identifier, external identity identifier, username, or email-as-primary-key strategy is established.

### REQ-CUS-004 — Customer, Principal, and Account Distinction

A Principal MAY act for a Customer only after applicable Authentication and server-side Authorization establish that relationship. Customer MUST NOT be equated with Principal, Account MUST NOT be equated with Session, and Authentication MUST NOT be treated as Authorization.

### REQ-CUS-005 — Customer Resource Isolation

Customer-owned Resources MUST remain associated with the correct Customer. One Customer MUST NOT access or mutate another Customer's Account, profile, Address, Preference, Consent, Wishlist, or other Customer-owned Resource without explicit governed Authorization. A supplied identifier is a lookup candidate and MUST NOT prove ownership.

## 7. Account

### REQ-CUS-006 — Account Business Boundary

An Account MUST represent the governed authenticated profile of a registered Customer and MAY expose supported profile, Address, Preference, privacy, Wishlist, and historical-reference capabilities only where Approved Product policy permits them. Account state MUST NOT redefine Identity, Session, Authorization, or downstream commercial truth.

### REQ-CUS-007 — Governed Account Lifecycle Outcomes

Account creation, access limitation, recovery, closure, and other lifecycle-dependent behavior MUST produce distinguishable Customer-facing outcomes without inventing repository-wide Account state names. Access state MUST NOT be presented as proof of downstream Order, Payment, Shipment, or Customer-data deletion outcomes.

No Account tier, membership tier, loyalty scheme, or reactivation policy is established.

## 8. Registration

### REQ-CUS-008 — Governed Customer Registration

Where registration is available, valid registration MUST create the appropriate Customer and Account business association without accidental privilege, cross-Customer access, unsupported mandatory profile data, or implied marketing Consent. Registration MUST preserve applicable privacy, security, and abuse-protection boundaries.

### REQ-CUS-009 — Duplicate and Ambiguous Registration

Duplicate, conflicting, or ambiguous registration input MUST produce a safe and explainable outcome that protects Account existence and Sensitive Data. Email-verification behavior applies only when Approved Product and Security governance establish it.

No OTP, verification channel, CAPTCHA, provider, or email-confirmation implementation is selected.

## 9. Credentials and Recovery Boundary

### REQ-CUS-010 — Credential Ownership Boundary

Credentials and passwords MUST be handled only through Approved Identity and Security design. They MUST NOT be treated as Customer profile data, exposed through Customer Contracts, stored in Customer content, or controlled by Customer-domain persistence rules.

No password algorithm, length, rotation, hashing, storage, token, MFA, SSO, OIDC, OAuth, or SAML selection is made.

### REQ-CUS-011 — Governed Account Recovery Boundary

Customer-domain recovery outcomes MUST preserve Customer identity, ownership isolation, privacy, and downstream history while delegating requester verification, recovery credentials, credential change, and Session invalidation to Identity and Security governance. Recovery MUST NOT expose Account existence or grant unauthorized access.

No recovery channel, reset-token design, expiry value, or implementation workflow is established.

## 10. Session Boundary

### REQ-CUS-012 — Session Non-Authority

Session establishment, expiry, revocation, and renewal belong to Identity and Security. Those outcomes MAY affect access to Customer capabilities but MUST NOT create, replace, delete, or rewrite Customer truth. Logout MUST NOT delete Customer data or alter downstream commercial records.

No token, cookie, Session store, browser-storage, or multi-device strategy is selected.

## 11. Customer Profile

### REQ-CUS-013 — Governed Profile Access and Mutation

A Customer MAY access or update supported Customer-owned profile data only through server-side ownership and Authorization checks. Accepted changes MUST satisfy governed validation, protect Sensitive Data, preserve stable identity and historical integrity, and MUST NOT grant cross-Customer access.

### REQ-CUS-014 — Profile Data Minimization

Customer profile data MUST be limited to fields supported by an Approved Product purpose and MUST NOT silently become a credential store, Authorization source, analytics profile, or repository of unnecessary PII.

No date of birth, gender, identification number, marital status, demographic profile, or marketing field is required by this Specification.

## 12. Address

### REQ-CUS-015 — Customer Address Ownership

An Address associated with an Account MUST belong to the correct Customer and have identity sufficient for governed access and mutation without assuming an identifier format. Default or preferred Address behavior MAY exist only where Approved Product governance establishes it.

### REQ-CUS-016 — Governed Address Mutation

An authorized Customer MUST be able to create, access, update, or remove supported Addresses subject to Customer ownership, applicable validation, safe failure, and downstream reference constraints. One Customer MUST NOT access another Customer's Address.

No Address field set, geocoding service, postal provider, address-validation provider, or delivery-eligibility rule is established.

### REQ-CUS-017 — Address and Commercial Snapshot Integrity

Customer Address truth MUST remain distinct from Checkout billing or shipping input, confirmed Order snapshots, and Shipment destination snapshots. A later Customer Address change or removal MUST NOT rewrite a confirmed Order, Payment record, Shipment destination, invoice, or other retained commercial history.

No downstream snapshot schema is defined.

## 13. Preference

### REQ-CUS-018 — Preference Ownership

The Customer Domain MUST own supported Customer Preferences as mutable Customer-specific settings or choices where Approved Product governance establishes them. A Preference change MUST preserve Customer ownership, validation, privacy, and applicable downstream compatibility.

### REQ-CUS-019 — Preference Non-Authority

A Preference MUST NOT independently authorize an action, override legal, security, Product, Pricing, Inventory, Checkout, Order, Payment, or Shipping Requirements, or be treated as Consent unless Approved Product and qualified governance explicitly establish the applicable relationship.

No Preference category, default, or value set is established.

## 14. Consent

### REQ-CUS-020 — Governed Consent Boundary

The Customer Domain MAY record or expose governed Consent only for a defined Approved purpose and according to applicable Product, Privacy, Legal, and Security governance. Consent state MUST remain distinguishable from transactional necessity, Preference, Authentication, Authorization, and commercial state.

### REQ-CUS-021 — Consent Non-Inference

Consent MUST NOT be inferred from Account existence, Preference, purchase, Session, inactivity, Wishlist activity, or another unrelated Customer action. Consent collection, change, withdrawal, evidence, and downstream use MUST remain purpose-bound where governing policy establishes them.

No Consent category, lawful basis, retention period, checkbox wording, marketing policy, or jurisdictional conclusion is established.

## 15. Wishlist

### REQ-CUS-022 — Conditional Wishlist Ownership

Where Approved Product policy enables Wishlist behavior, the Customer Domain MUST own the Customer association and Customer-side saved-interest state while referencing Product through governed identifiers. Guest Wishlist behavior remains unresolved.

### REQ-CUS-023 — Wishlist Non-Authority

Wishlist state MUST NOT publish Product, override Product visibility, establish Price, reserve Stock, create a Stock Reservation, prove Available-to-Sell, authorize purchase, or alter Product, Pricing, Inventory, Cart, Checkout, or Order truth. Product removal or unavailability MUST produce a governed non-authoritative Wishlist outcome.

No Wishlist limit, sharing, public access, alerting, deduplication policy, or persistence duration is established.

## 16. Customer History

### REQ-CUS-024 — Governed Historical Access

A Customer MAY access their own governed historical references, including Order, Payment, Refund, Shipment, and communication state where Approved Product policy and server-side Authorization permit it. The owning Domains remain authoritative for those records and lifecycles.

### REQ-CUS-025 — Historical Truth Preservation

Customer, Account, profile, Address, Preference, Consent, Wishlist, closure, or access-state changes MUST NOT rewrite confirmed Order snapshots, Payment evidence, Refund history, Shipment history, or other authoritative commercial records. Retention, anonymization, and deletion remain subject to governing sources.

## 17. Customer Creation and Update

### REQ-CUS-026 — Governed Customer Creation

Customer creation MUST establish stable identity, valid supported Customer-owned data, correct ownership, and no accidental privilege. Invalid, unauthorized, materially duplicate, or ambiguous creation MUST be rejected with a safe and explainable outcome or handled only through explicitly governed recovery.

### REQ-CUS-027 — Governed Customer Update

An authorized Customer update MUST preserve identity, ownership isolation, validation, Sensitive Data protection, and downstream authority. A stale or concurrent update MUST produce a safe, explainable outcome without silently overwriting accepted data or changing another Customer or downstream Domain state.

No locking, version field, merge algorithm, or retry mechanism is selected.

## 18. Account Closure and Customer Data Requests

### REQ-CUS-028 — Account Closure Boundary

Where Account closure or deactivation is supported, it MUST preserve Customer identity linkage needed for governed obligations, prevent unauthorized continued access, and MUST NOT rewrite Order, Payment, Refund, Shipment, Audit Record, or other retained commercial truth. Reactivation and exact lifecycle states remain unresolved.

### REQ-CUS-029 — Customer Data Request Boundary

Customer data access, export, correction, deletion, anonymization, and closure requests MUST follow applicable Authorization, identity verification, privacy, legal, security, retention, audit, and commercial-integrity Requirements. Customer deletion MUST NOT imply automatic hard deletion of legally or operationally required records.

This Specification establishes no retention timeline, grace period, anonymization algorithm, backup treatment, legal conclusion, or deletion implementation.

## 19. Product Boundary

### REQ-CUS-030 — Product Authority Boundary

The Customer Domain MUST NOT own or redefine Product identity, Product content, Product Variant, Product Media, Category, Product publication, Product visibility, or Product structural sellability. Preference, Wishlist, profile, or Customer history MUST NOT make unavailable or unpublished Product authoritative.

## 20. Pricing Boundary

### REQ-CUS-031 — Pricing Authority Boundary

Customer context MAY be supplied as eligibility input only where Approved Product and Pricing governance establish it. Customer MUST NOT calculate or own authoritative Price, Discount, Promotion, Voucher, tax, Money rounding, Currency calculation, or Pricing Rule behavior.

No Customer pricing tier or personalized pricing policy is established.

## 21. Inventory Boundary

### REQ-CUS-032 — Inventory Authority Boundary

Customer, Account, profile, Address, Preference, Consent, Wishlist, or Session state MUST NOT establish or alter Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection. Saving Product interest MUST NOT reserve Stock.

## 22. Cart and Checkout Boundary

### REQ-CUS-033 — Cart and Checkout Authority Boundary

The Customer Domain MAY supply governed Customer identity, Address, Preference, and applicable Consent state to Cart or Checkout. It MUST NOT own Cart intent, Checkout validation, final Price, Product Variant sellability, Stock Reservation, delivery eligibility, Payment initiation or truth, or Order creation.

Guest Checkout versus mandatory Account behavior remains unresolved under Product governance.

## 23. Order Boundary

### REQ-CUS-034 — Order Authority Boundary

A Customer MAY reference or view their own Orders only where server-side Authorization permits. Order owns Order Items, commercial snapshots, lifecycle, status, cancellation coordination, and history; Customer-domain mutation MUST NOT alter Order truth.

## 24. Payment Boundary

### REQ-CUS-035 — Payment Authority Boundary

Customer state, browser state, client report, Account history, or Payment Redirect MUST NOT establish Payment success. Provider-dependent Payment state requires validated Payment Provider evidence through the Payment Domain, and Customer history MAY represent Payment state only from that governed source.

Raw card data and raw CVV MUST NOT be stored in Customer data, content, logs, telemetry, fixtures, exports, or support evidence. No Payment Provider is selected.

## 25. Shipping Boundary

### REQ-CUS-036 — Shipping Authority Boundary

A governed Customer Address MAY be input to Checkout and Shipping, but Shipping owns delivery eligibility, service, Shipment, tracking, and delivery state. A later Customer Address change MUST NOT rewrite confirmed Order or Shipment destination snapshots.

## 26. Administration Boundary

### REQ-CUS-037 — Administration Boundary

Administration MAY invoke Customer capabilities through governed Staff User workflows, but access and change MUST be least-privilege, server-side authorized, purpose-bound, auditable where material, and protected against cross-Customer leakage. Administration MUST NOT redefine Customer rules or bypass ownership isolation.

No Role or Permission matrix, approval chain, support workflow, form, or bulk-operation implementation is defined.

## 27. Analytics Boundary

### REQ-CUS-038 — Analytics Non-Authority

Analytics MAY receive purpose-limited, Consent-aware Customer observations only where Approved governance permits them. Analytics events, audiences, profiles, models, reports, and Projections MUST NOT redefine Customer identity, Preference, Consent, eligibility, Authorization, or commercial truth.

No analytics provider, event taxonomy, personalization model, or tracking implementation is selected.

## 28. Authentication and Authorization

Authentication establishes Principal identity; Authorization determines whether that Principal may perform an action on a Resource.

### REQ-CUS-039 — Customer Authorization

Protected Customer creation, profile, Address, Preference, Consent, Wishlist, history, recovery, closure, export, correction, and deletion operations MUST enforce server-side Authorization for the current Principal, Customer ownership, Resource, action, and applicable Domain state. UI state, Role labels, client-represented Permissions or Claims, route state, navigation, and search state MUST NOT authorize or substitute for that decision.

No Identity Provider, protocol, token, Session implementation, password implementation, MFA policy, Role matrix, or Permission model is selected.

## 29. Security and Sensitive Data

### REQ-CUS-040 — Customer Data Safety

Customer data MUST be minimized, purpose-bound, and exposed only to authorized contexts. Secrets, credentials, recovery artifacts, raw Payment data, and unnecessary PII MUST NOT appear in Customer content, profile fields, URLs, Logs, Metrics, Traces, analytics, telemetry, fixtures, screenshots, error reports, exports, or ordinary support evidence.

Administrative and support access MUST minimize disclosure and apply governed masking where required. No data-classification level, encryption implementation, DLP technology, or retention period is established.

## 30. Privacy and Compliance

### REQ-CUS-041 — Qualified Privacy and Compliance Boundary

Applicable POPIA, consumer, privacy, commercial, contractual, and other legal obligations affecting Customer data MUST receive qualified validation before production reliance. Collection, use, access, correction, export, retention, deletion, disclosure, and cross-border handling MUST remain governed by approved purposes and accountable owners.

This Requirement is not a compliance certification, lawful-basis determination, legal conclusion, or retention policy.

## 31. Fraud and Abuse Boundary

### REQ-CUS-042 — Customer Fraud and Abuse Outcome

Registration, Authentication, Account recovery, Customer-data access, and other applicable Customer workflows MUST support governed protection against suspicious or abusive behavior without bypassing ownership, Authorization, auditability, privacy, or authoritative commercial state. False-positive recovery MUST remain possible where policy permits it.

No fraud vendor, model, threshold, CAPTCHA, rule engine, manual-review workflow, or blocking policy is selected.

## 32. Accessibility

### REQ-CUS-043 — Accessible Customer Capabilities

Customer-facing Account, registration, profile, Address, Preference, Consent, Wishlist, history, and recovery capabilities MUST support the Approved WCAG 2.2 AA outcome where applicable. Labels, validation, failure, and recovery guidance MUST remain understandable without defining UI mechanics.

`.ai/frontend/ACCESSIBILITY.md`, `.ai/frontend/UI.md`, and the Design System own implementation and evidence mechanics. This Requirement is not a certification claim and does not impose Level AAA.

## 33. Performance

### REQ-CUS-044 — Bounded Customer Delivery

Customer profile, Address, Preference, Consent, Wishlist, and historical-reference Contracts MUST support bounded delivery and MUST NOT require unbounded Customer history or Customer-owned Resource payloads.

This Specification establishes no payload limit, performance budget, percentile, latency target, device profile, network profile, capacity target, SLA, or SLO. `.ai/frontend/PERFORMANCE.md` owns applicable implementation targets and evidence.

## 34. Audit

### REQ-CUS-045 — Material Customer Audit Evidence

Account recovery, sensitive profile change, material Address or Consent change, administrative access or mutation, Account closure, Customer data request, and other high-Risk Customer actions governed as auditable MUST produce appropriate Audit Records containing sufficient actor or system context, action, time, and outcome for authorized investigation.

Ordinary profile editing is not automatically high-Risk; applicable Product, Security, Privacy, and Administration governance determines proportionality.

## 35. Events

### REQ-CUS-046 — Conditional Customer Events

Customer creation, profile change, Consent change, Account closure, or another Customer outcome MAY require Domain Event or Integration Event behavior only when Approved Architecture, a governed Contract, or downstream reliability Requirements establish it.

This Specification does not mandate event publication, invent an event name or schema, select an Outbox Pattern, or assume exactly-once delivery. `.ai/backend/EVENTS.md` governs applicable implementation behavior.

## 36. Contract Impacts

Later governed Contracts may be required for Customer profile, Account, Address, Preference, Consent, conditional Wishlist, registration and recovery boundaries, Customer historical references, and Administration or support operations.

### REQ-CUS-047 — Customer Contract Boundary

Contracts exposing Customer information or operations MUST preserve Customer ownership, Identity separation, downstream authority, lifecycle, server-side Authorization, compatibility, failure, stale-state, privacy, and Sensitive Data boundaries. This Specification MUST NOT itself define REST paths, HTTP methods, status codes, JSON fields, DTOs, schemas, event payloads, database structures, or provider formats.

## 37. Failure and Recovery

### REQ-CUS-048 — Customer Failure Outcomes

Duplicate registration, ambiguous identity, invalid Customer data, invalid Address, stale or concurrent update, unauthorized or cross-Customer access, recovery failure, Session or access expiry, Customer data request failure, and unavailable downstream Product, Order, Payment, or Shipping references MUST produce distinguishable, safe, explainable, and recoverable outcomes where recovery is permitted.

Failure output MUST NOT expose Sensitive Data, Secrets, credentials, inaccessible Resource existence, Account existence where protected, provider details, security controls, or implementation internals. No transport response or technical error code is defined.

## 38. Testing

### REQ-CUS-049 — Customer Verification Coverage

Tests and acceptance evidence MUST cover applicable positive, negative, Customer-isolation, Authorization, registration, duplicate or ambiguous identity, profile, Address, Preference, Consent, conditional Wishlist, concurrency, Sensitive Data, Administration access, Session-boundary, recovery, accessibility, audit, downstream-boundary, failure, and recovery behavior.

Test design and evidence MUST follow `.ai/core/TESTING-STANDARDS.md` without selecting a test framework, test-case identifier scheme, or numerical coverage target here.

## 39. Acceptance Criteria

Acceptance Criteria have no separate identifiers.

| Requirement | Observable Acceptance Criteria |
| --- | --- |
| REQ-CUS-001 | Customer identity, profile, Addresses, Preferences, governed Consent, Customer-side lifecycle, and approved saved state are identifiable as Customer-owned concerns. |
| REQ-CUS-002 | Customer or Account state cannot change authoritative Identity, Authorization, Product, Pricing, Inventory, Cart, Checkout, Payment, Order, Shipping, Administration, or Analytics truth. |
| REQ-CUS-003 | Profile, Address, Preference, Consent, Wishlist, credential, Principal, and Session changes preserve the same Customer identity without requiring an invented identifier format. |
| REQ-CUS-004 | Acting for a Customer requires an authenticated and authorized Principal relationship; Customer, Principal, Account, Session, Authentication, and Authorization remain distinct. |
| REQ-CUS-005 | Access to every Customer-owned Resource verifies server-side Customer ownership and Authorization; an identifier alone cannot authorize cross-Customer access. |
| REQ-CUS-006 | An Account exposes only governed registered-Customer capabilities without redefining Identity, Session, Authorization, or commercial truth. |
| REQ-CUS-007 | Account lifecycle-dependent outcomes are distinguishable without invented Account states and cannot prove downstream history or deletion outcomes. |
| REQ-CUS-008 | Valid governed registration creates the correct Customer/Account association without excess data, privilege, cross-Customer access, or inferred marketing Consent while preserving applicable privacy, security, and abuse-protection boundaries. |
| REQ-CUS-009 | Duplicate or ambiguous registration is handled safely and explainably without exposing Account existence or Sensitive Data; verification remains policy-dependent. |
| REQ-CUS-010 | Customer profile and Contracts contain no passwords or credentials, and credential handling remains owned by Approved Identity and Security design. |
| REQ-CUS-011 | A recovery attempt preserves Customer identity, ownership isolation, privacy, and downstream history while Identity and Security own requester verification, recovery credentials, credential change, and Session invalidation; an unauthorized or unverified requester is denied safely and is not told whether a protected Account exists. |
| REQ-CUS-012 | Session establishment, expiry, revocation, renewal, and logout affect access without creating, replacing, deleting, or rewriting Customer or commercial truth. |
| REQ-CUS-013 | An authorized profile read or update verifies ownership, validates governed fields, protects Sensitive Data, preserves identity/history, and denies cross-Customer access. |
| REQ-CUS-014 | Profile data has an Approved purpose and contains no invented mandatory demographic data, credentials, Authorization source, or unapproved analytics profile. |
| REQ-CUS-015 | An Account Address belongs to the correct Customer and supports governed identity without an invented format; preferred/default behavior exists only when Approved. |
| REQ-CUS-016 | Authorized Address creation, access, update, and removal enforce ownership, validation, safe failure, and reference constraints without cross-Customer exposure. |
| REQ-CUS-017 | Updating or removing a Customer Address leaves Checkout, confirmed Order, Payment, Shipment, invoice, and retained commercial snapshots unchanged. |
| REQ-CUS-018 | Supported Preference changes preserve Customer ownership, validation, privacy, and downstream compatibility. |
| REQ-CUS-019 | A Preference cannot authorize, override governing Requirements, or become Consent without explicit Approved governance. |
| REQ-CUS-020 | Governed Consent has an Approved purpose and remains distinguishable from transactional necessity, Preference, Authentication, Authorization, and commercial state. |
| REQ-CUS-021 | Account existence, Preference, purchase, Session, inactivity, Wishlist activity, or unrelated action does not infer Consent; governed collection, change, withdrawal, evidence, and downstream use remain purpose-bound. |
| REQ-CUS-022 | When Product policy enables Wishlist, saved-interest state belongs to the correct Customer and references Product without resolving guest behavior. |
| REQ-CUS-023 | Wishlist cannot publish Product, establish Product visibility or Price, reserve Stock, prove Available-to-Sell, authorize purchase, or alter downstream truth. |
| REQ-CUS-024 | An authorized Customer can access only their governed historical references while Order, Payment, Refund, Shipment, and communication owners retain authority. |
| REQ-CUS-025 | Customer or Account changes do not rewrite confirmed Order, Payment, Refund, Shipment, or other commercial history, including during closure or data handling, while retention, anonymization, and deletion remain governed elsewhere. |
| REQ-CUS-026 | Valid Customer creation establishes stable identity, supported data, ownership, and no accidental privilege; invalid, unauthorized, duplicate, or ambiguous creation fails safely and explainably. |
| REQ-CUS-027 | An authorized update preserves identity, isolation, validation, Sensitive Data, and downstream authority; stale or concurrent conflict is safe and explainable, does not silently overwrite accepted data, and does not change another Customer or downstream Domain state. |
| REQ-CUS-028 | Supported closure prevents unauthorized continued access while preserving required Customer identity linkage, Order, Payment, Refund, Shipment, Audit Record, and other retained commercial truth, with exact states and reactivation policy unresolved. |
| REQ-CUS-029 | Applicable supported access, export, correction, deletion, anonymization, and closure requests preserve identity verification, server-side Authorization, privacy, legal, security, retention, Audit Record, and commercial-record integrity Requirements; deletion does not automatically mean hard deletion or silently rewrite commercial records, and no retention period, anonymization algorithm, legal conclusion, or deletion implementation is established. |
| REQ-CUS-030 | Customer state cannot redefine Product identity, content, Product Variant, Product Media, Category, publication, visibility, or structural sellability. |
| REQ-CUS-031 | Customer context is only governed eligibility input and cannot calculate or own Price, Discount, Promotion, Voucher, tax, Money rounding, Currency, or Pricing Rules. |
| REQ-CUS-032 | Customer, Account, profile, Address, Preference, Consent, Wishlist, and Session state cannot create or change Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, or Overselling protection. |
| REQ-CUS-033 | Customer supplies only governed context to Cart or Checkout and cannot own Cart intent, Checkout validation, final Price, sellability, Stock Reservation, delivery eligibility, Payment, or Order creation. |
| REQ-CUS-034 | Authorized Customer Order access preserves Order-owned items, snapshots, lifecycle, status, cancellation coordination, and history against Customer mutation. |
| REQ-CUS-035 | Customer/browser/client/Redirect state cannot prove Payment; governed history uses validated Payment Provider evidence, and raw card data or raw CVV is absent from Customer artifacts. |
| REQ-CUS-036 | Customer Address may be downstream input, while Shipping retains delivery and Shipment authority and later Address changes preserve confirmed destination snapshots. |
| REQ-CUS-037 | Staff User access is least-privilege, server-side authorized, purpose-bound, auditable where material, and isolated without redefining Customer rules. |
| REQ-CUS-038 | Analytics remains purpose-limited, Consent-aware, and non-authoritative for Customer identity, Preference, Consent, eligibility, Authorization, and commercial truth. |
| REQ-CUS-039 | A protected Customer action is denied when server-side Authorization or ownership evaluation fails; UI, Role, client Permission or Claims, route, navigation, and search state cannot authorize. |
| REQ-CUS-040 | Customer data is minimized, purpose-bound, and unavailable to unauthorized contexts; prohibited artifacts contain no Secrets, credentials, recovery artifacts, raw Payment data, or unnecessary PII, and administrative or support access exposes only the minimum governed information with governed masking where required. |
| REQ-CUS-041 | Applicable Customer-data collection, use, access, correction, export, retention, deletion, disclosure, and cross-border handling remain subject to an Approved purpose, accountable ownership, and qualified privacy, legal, or compliance validation where applicable; this Specification establishes no compliance certification, lawful basis, legal conclusion, or retention period. |
| REQ-CUS-042 | Applicable suspicious or abusive Customer activity receives governed handling without bypassing ownership, Authorization, audit, privacy, or commercial truth, and false-positive recovery remains possible where permitted. |
| REQ-CUS-043 | Applicable Customer capabilities provide WCAG 2.2 AA evidence with understandable labels, validation, failure, and recovery without claiming certification or AAA. |
| REQ-CUS-044 | Customer Contracts deliver bounded profile, Address, Preference, Consent, Wishlist, and historical-reference data without an invented numerical target. |
| REQ-CUS-045 | Applicable Account recovery, sensitive profile change, material Address or Consent change, administrative access or mutation, Account closure, Customer data request, and other governed high-Risk or auditable Customer actions produce Audit Records with sufficient actor or system context, action, time, and outcome; ordinary profile editing is not automatically high-Risk, and applicability remains governed by Product, Security, Privacy, and Administration sources. |
| REQ-CUS-046 | Customer events remain conditional, governed, unnamed, schema-neutral, without an Outbox mandate or exactly-once assumption. |
| REQ-CUS-047 | A future Customer Contract preserves ownership, Identity separation, downstream authority, lifecycle, Authorization, compatibility, failure, stale-state, privacy, and Sensitive Data boundaries without wire or persistence design. |
| REQ-CUS-048 | Every enumerated Customer failure is distinguishable, safe, explainable, recoverable where permitted, and does not expose Sensitive Data, Secrets, credentials, protected existence, provider/security details, or internals. |
| REQ-CUS-049 | Evidence covers applicable positive, negative, isolation, Authorization, registration, identity, profile, Address, Preference, Consent, Wishlist, concurrency, Sensitive Data, Administration, Session, recovery, accessibility, audit, downstream boundaries, failure, and recovery. |

## 40. Requirement Traceability

No applicable Customer-domain Decision Record currently exists. Traceability therefore uses Approved Product sections, business Requirements, upstream Domain Requirements, governing sources, and downstream scopes without fabricating a Decision identifier.

| Requirement | Product source | Business Requirement source | Upstream Domain source where relevant | Additional governing source | Downstream scope |
| --- | --- | --- | --- | --- | --- |
| REQ-CUS-001 | PRODUCT.md §§12.3, 13 | REQ-BUS-007, 008 | REQ-PRD-048; REQ-CAT-034 | ARCHITECTURE.md §§9, 30.1 | Customer, Identity, frontend, backend |
| REQ-CUS-002 | PRODUCT.md §§13, 16 | REQ-BUS-007, 008, 044 | REQ-PRD-002, 048; REQ-CAT-002, 034 | ARCHITECTURE.md §9 | All affected Domains |
| REQ-CUS-003 | PRODUCT.md §§7.2, 12.3 | REQ-BUS-007, 008 | REQ-PRD-048; REQ-CAT-034 | ARCHITECTURE.md §§9, 30.1 | Customer, Identity, Contracts |
| REQ-CUS-004 | PRODUCT.md §§7.2, 12.3 | REQ-BUS-007, 032, 051 | REQ-PRD-050; REQ-CAT-036 | GLOSSARY.md; SECURITY-STANDARDS.md §10 | Customer, Identity, Authorization |
| REQ-CUS-005 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-008, 032, 039 | REQ-PRD-050; REQ-CAT-036 | SECURITY-STANDARDS.md §12 | Customer, frontend, backend, tests |
| REQ-CUS-006 | PRODUCT.md §§12.3, 30.5 | REQ-BUS-007, 008 | REQ-PRD-048 | ARCHITECTURE.md §§9, 30 | Customer, Identity, frontend |
| REQ-CUS-007 | PRODUCT.md §§16.7, 17 | REQ-BUS-008, 042, 051 | REQ-PRD-048 | GLOSSARY.md; SECURITY-STANDARDS.md | Customer, Identity, frontend |
| REQ-CUS-008 | PRODUCT.md §§7.2, 12.3, 28.3 | REQ-BUS-007, 040, 051, 052 | — | ARCHITECTURE.md §30.2; SECURITY-STANDARDS.md §11 | Customer, Identity, frontend, backend |
| REQ-CUS-009 | PRODUCT.md §§5.6, 24 | REQ-BUS-036, 042, 051, 052 | — | SECURITY-STANDARDS.md §11 | Customer, Identity, security |
| REQ-CUS-010 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-039, 051 | — | ARCHITECTURE.md §30.1; SECURITY-STANDARDS.md §§10–11 | Customer, Identity, security, Contracts |
| REQ-CUS-011 | PRODUCT.md §§5.6, 12.3 | REQ-BUS-036, 051, 052 | — | SECURITY-STANDARDS.md §11 | Customer, Identity, security, support |
| REQ-CUS-012 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-007, 032, 051 | — | ARCHITECTURE.md §30.5; SECURITY-STANDARDS.md §13 | Customer, Identity, frontend, backend |
| REQ-CUS-013 | PRODUCT.md §§12.3, 30.5 | REQ-BUS-008, 032, 039 | REQ-PRD-048 | SECURITY-STANDARDS.md §§12, 35 | Customer, frontend, backend |
| REQ-CUS-014 | PRODUCT.md §§16.7, 30.5 | REQ-BUS-008, 039, 040 | REQ-PRD-051; REQ-CAT-037 | SECURITY-STANDARDS.md §35 | Customer, privacy, Contracts |
| REQ-CUS-015 | PRODUCT.md §§12.3, 30.5 | REQ-BUS-008 | — | ARCHITECTURE.md §9 | Customer, Checkout, Shipping |
| REQ-CUS-016 | PRODUCT.md §§12.3, 30.5 | REQ-BUS-008, 032, 042 | — | SECURITY-STANDARDS.md §12 | Customer, frontend, backend |
| REQ-CUS-017 | PRODUCT.md §§14.4, 16.4, 16.7 | REQ-BUS-008, 022 | — | ARCHITECTURE.md §40.5 | Customer, Checkout, Order, Shipping |
| REQ-CUS-018 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-008, 040 | REQ-PRD-048; REQ-CAT-034 | ARCHITECTURE.md §§9, 30.1 | Customer, frontend, Contracts |
| REQ-CUS-019 | PRODUCT.md §§16.7–16.8 | REQ-BUS-008, 032, 040 | REQ-PRD-048; REQ-CAT-034 | SECURITY-STANDARDS.md §§12, 35 | Customer, Authorization, privacy |
| REQ-CUS-020 | PRODUCT.md §§12.3, 16.7 | REQ-BUS-039, 040, 041 | REQ-PRD-048; REQ-CAT-034 | SECURITY-STANDARDS.md §35 | Customer, privacy, analytics |
| REQ-CUS-021 | PRODUCT.md §§16.7, 33 | REQ-BUS-040 | — | SECURITY-STANDARDS.md §35 | Customer, privacy, analytics, marketing |
| REQ-CUS-022 | PRODUCT.md §§12.1, 12.3, 24 | REQ-BUS-008 | REQ-PRD-002, 048 | ARCHITECTURE.md §9 | Customer, Product, frontend |
| REQ-CUS-023 | PRODUCT.md §§13, 16.2 | REQ-BUS-008, 015, 017 | REQ-PRD-002, 048; REQ-CAT-002, 034 | ARCHITECTURE.md §9 | Customer, Product, Pricing, Inventory |
| REQ-CUS-024 | PRODUCT.md §§12.3–12.4, 14.8 | REQ-BUS-008, 023, 032 | — | SECURITY-STANDARDS.md §§12, 35 | Customer, Order, Payment, Shipping |
| REQ-CUS-025 | PRODUCT.md §§16.4, 16.7, 17.4 | REQ-BUS-008, 022, 023 | REQ-PRD-030, 031 | DATABASE.md; SECURITY-STANDARDS.md §35 | Customer, Order, Payment, Shipping |
| REQ-CUS-026 | PRODUCT.md §§7.2, 15.1 | REQ-BUS-007, 032, 051 | — | SECURITY-STANDARDS.md §§11–12 | Customer, Identity, backend |
| REQ-CUS-027 | PRODUCT.md §§15.4, 17.4 | REQ-BUS-008, 036, 042 | REQ-PRD-035 | TESTING-STANDARDS.md | Customer, frontend, backend |
| REQ-CUS-028 | PRODUCT.md §§16.7, 17.4, 24 | REQ-BUS-008, 023, 039, 048 | — | SECURITY-STANDARDS.md §§10, 35 | Customer, Identity, Order, privacy |
| REQ-CUS-029 | PRODUCT.md §§16.7, 24 | REQ-BUS-033, 034, 039, 040, 041, 048, 053 | — | SECURITY-STANDARDS.md §35; DATABASE.md | Customer, privacy, operations |
| REQ-CUS-030 | PRODUCT.md §§12.1, 13 | REQ-BUS-003, 005, 008 | REQ-PRD-001, 002, 048; REQ-CAT-002, 034 | ARCHITECTURE.md §9 | Customer, Product, Category |
| REQ-CUS-031 | PRODUCT.md §§13, 16.1, 16.6 | REQ-BUS-014, 015, 016 | REQ-PRD-002, 046, 048; REQ-CAT-032, 034 | ARCHITECTURE.md §9 | Customer, Pricing, Checkout |
| REQ-CUS-032 | PRODUCT.md §§13, 16.2 | REQ-BUS-017, 018, 019 | REQ-PRD-002, 047, 048; REQ-CAT-033, 034 | ARCHITECTURE.md §§9, 20.3 | Customer, Inventory, Checkout |
| REQ-CUS-033 | PRODUCT.md §§12.2–12.3, 14.3–14.4, 24 | REQ-BUS-007, 009, 011, 012, 013 | — | ARCHITECTURE.md §9 | Customer, Cart, Checkout |
| REQ-CUS-034 | PRODUCT.md §§12.3–12.4, 16.4 | REQ-BUS-008, 021, 022, 023 | — | ARCHITECTURE.md §9 | Customer, Order, support |
| REQ-CUS-035 | PRODUCT.md §§14.5, 16.3 | REQ-BUS-024, 025, 027, 028 | — | SECURITY-STANDARDS.md §36 | Customer, Payment, frontend, support |
| REQ-CUS-036 | PRODUCT.md §§12.3–12.4, 16.5 | REQ-BUS-008, 022, 029 | — | ARCHITECTURE.md §9 | Customer, Checkout, Order, Shipping |
| REQ-CUS-037 | PRODUCT.md §§12.8–12.9, 15.4, 31 | REQ-BUS-031, 032, 033, 034 | REQ-PRD-049, 050; REQ-CAT-035, 036 | SECURITY-STANDARDS.md §12 | Customer, Administration, support |
| REQ-CUS-038 | PRODUCT.md §§18.5, 33 | REQ-BUS-039, 040, 044 | REQ-PRD-048, 051; REQ-CAT-034, 037 | SECURITY-STANDARDS.md §35 | Customer, analytics, privacy |
| REQ-CUS-039 | PRODUCT.md §§7.3–7.5, 31 | REQ-BUS-032, 033 | REQ-PRD-050; REQ-CAT-036 | SECURITY-STANDARDS.md §12 | Customer, Identity, frontend, backend |
| REQ-CUS-040 | PRODUCT.md §§16.7–16.8, 32 | REQ-BUS-027, 039, 040, 053 | REQ-PRD-051; REQ-CAT-037 | SECURITY-STANDARDS.md §§14, 35–36 | Customer, security, privacy, operations |
| REQ-CUS-041 | PRODUCT.md §§16.7, 20, 24 | REQ-BUS-039, 040, 041 | — | SECURITY-STANDARDS.md §35 | Customer, privacy, legal, operations |
| REQ-CUS-042 | PRODUCT.md §§16.12, 24 | REQ-BUS-036, 042, 052 | — | SECURITY-STANDARDS.md §§11, 28, 30, 39 | Customer, Identity, security, operations |
| REQ-CUS-043 | PRODUCT.md §§5.7, 16.9, 30.5 | REQ-BUS-037 | REQ-PRD-041; REQ-CAT-038 | ACCESSIBILITY.md; DESIGN-SYSTEM.md | Customer, frontend, tests |
| REQ-CUS-044 | PRODUCT.md §§8.3, 18.4, 35.2 | REQ-BUS-038 | REQ-PRD-042; REQ-CAT-039 | PERFORMANCE.md | Customer, frontend, backend, Contracts |
| REQ-CUS-045 | PRODUCT.md §§15.4, 16.7, 31 | REQ-BUS-033, 034 | REQ-PRD-043; REQ-CAT-040 | SECURITY-STANDARDS.md §27 | Customer, Administration, security, operations |
| REQ-CUS-046 | PRODUCT.md §§22, 26, 35 | REQ-BUS-047, 049 | — | ARCHITECTURE.md §28; EVENTS.md | Customer, integrations, notifications |
| REQ-CUS-047 | PRODUCT.md §§22, 26, 35 | REQ-BUS-039, 042, 047 | REQ-PRD-052; REQ-CAT-041 | API.md; EVENTS.md; DOCUMENTATION-STANDARDS.md §§21, 24–25 | Customer, Contracts, frontend, backend |
| REQ-CUS-048 | PRODUCT.md §§5.6, 17.2, 30, 35.2 | REQ-BUS-036, 042, 051 | REQ-PRD-040; REQ-CAT-042 | SECURITY-STANDARDS.md; API.md | Customer, Identity, frontend, backend, support |
| REQ-CUS-049 | PRODUCT.md §§26.2–26.4, 35.3 | REQ-BUS-037, 039, 047 | REQ-PRD-039, 044; REQ-CAT-043 | TESTING-STANDARDS.md | Customer, tests, acceptance evidence |

Future Accepted Decisions must be linked rather than copied. A change to Customer truth requires synchronized governing updates before this Specification may rely on it.

## 41. Open Product Decisions

The following current `PRODUCT.md` concerns are Customer-domain relevant and remain unresolved:

| Product Decision concern | Customer-domain boundary |
| --- | --- |
| Guest checkout versus mandatory account rules | This Specification does not require or prohibit guest Checkout and does not make Account creation a Checkout precondition. |
| Customer email-verification requirements | No verification requirement, channel, timing, provider, or implementation is selected. |
| Wishlist behaviour for guest and registered customers | Customer-owned Wishlist behavior remains conditional; guest behavior, limits, sharing, and persistence are not established. |
| Customer-support channels and service expectations | No support channel, service target, or support workflow is established. |
| Marketing-consent and communication-preference model | No Consent category, Preference taxonomy, channel rule, lawful basis, or marketing policy is established. |
| Initial analytics provider and event taxonomy | No analytics provider, Customer event taxonomy, tracking implementation, or personalization model is selected. |
| Administrative role and permission matrix | No Customer-administration Role, Permission, or access matrix is established. |
| Production customer-service and operational escalation process | No escalation path, approval chain, service target, or repair workflow is established. |
| Fraud-screening approach and manual-review workflow | No fraud vendor, model, threshold, blocking rule, reviewer Role, or manual-review workflow is selected. |
| Customer data export, correction, deletion, and account-closure workflow | No export format, correction workflow, deletion behavior, closure state, retention period, anonymization method, or reactivation policy is established. |

Identity Provider, credential technology, Account recovery mechanics, MFA policy, address validation, and Customer lifecycle state names remain governed or unresolved aspects of applicable Architecture, Security, Product, and future Domain governance; they are not represented here as separately recorded Product Decisions. No `DEC-####` identifier is fabricated.

## 42. Risks

| Risk | Required treatment |
| --- | --- |
| Cross-Customer access | Enforce trusted ownership and server-side Authorization for every Customer-owned Resource. |
| Sensitive Data leakage | Minimize collection and disclosure across Contracts, telemetry, analytics, exports, support, and non-production evidence. |
| Customer, Principal, and Account are conflated | Preserve canonical Identity, Principal, Customer, Account, and Session boundaries. |
| Mutable Address rewrites history | Preserve confirmed Order and Shipment snapshots independently of Customer Address. |
| Preference is treated as Consent | Require explicit governed purpose and keep Preference, Consent, and transactional necessity distinct. |
| Customer state is treated as Authorization | Deny by default and enforce server-side Resource and ownership checks. |
| Wishlist becomes Product or Inventory truth | Treat saved interest as non-authoritative and revalidate Product and Inventory state. |
| Profile or closure rewrites commercial records | Preserve Order, Payment, Refund, Shipment, and Audit Record history under owning governance. |
| Unresolved privacy policy becomes implementation default | Keep data handling subject to Approved Product and qualified Privacy/Legal governance. |
| Downstream references become stale or misleading | Preserve owning-Domain authority and provide distinguishable reconciliation or unavailable outcomes. |
| Recovery or abuse controls enable unauthorized access | Preserve verification, isolation, safe failure, auditability, and governed false-positive recovery. |

No numerical Risk score or tolerance is established here.

## 43. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `.ai/frontend/UI.md`
- `.ai/frontend/ACCESSIBILITY.md`
- `.ai/frontend/PERFORMANCE.md`
- `specifications/business/business-requirements.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`

## 44. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Customer Domain Specification covering Customer identity, Account, Address, Preference, consent, ownership, security, downstream boundaries, Acceptance Criteria, and traceability. |

## 45. Final Validation

Before material revision, approval or re-approval, or implementation reliance, validation MUST confirm that:

1. metadata remains `0.1.0` Draft with `authoritative: false` until governed promotion;
2. the scope code remains `CUS`, Requirement identifiers remain unique and sequential, and retired identifiers are not reused;
3. Customer terminology remains aligned with `GLOSSARY.md`, and no ungoverned Customer or Account lifecycle state is introduced;
4. Customer owns Customer identity and governed Customer-owned data without absorbing Identity, Authorization, Product, Pricing, Inventory, Cart, Checkout, Order, Payment, Shipping, Administration, or Analytics authority;
5. ownership isolation, profile, Address, Preference, Consent, conditional Wishlist, history, closure, privacy, Authorization, security, accessibility, audit, and failure Requirements remain testable;
6. Customer-owned data remains distinct from downstream commercial snapshots and authoritative state;
7. every material Requirement retains observable Acceptance Criteria and one-to-one traceability;
8. Customer-relevant Open Product Decisions remain unresolved unless Accepted governance and synchronized source updates exist;
9. no path, method, status code, schema, database structure, Angular Component, Spring class, event name, provider integration, Identity Provider, protocol, implementation technology, or unsupported numerical target is introduced;
10. Related Documents exist, contain no self-reference, and remain relevant;
11. headings remain sequential and unique, sections remain non-empty, tables remain valid, and no TODO, TBD, FIXME, placeholder, or actual ellipsis remains; and
12. only `specifications/domains/customer/customer-domain.md` changes for a scoped update.
