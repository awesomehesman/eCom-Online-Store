---
title: Customer and Account Backend Specification
version: 1.0.0
status: Approved
owner: Backend / Customer
last_updated: 2026-09-17
authoritative: false
scope: BCUS
---

# Customer and Account Backend Specification

## 1. Purpose

This Specification defines implementation-neutral backend Requirements for Customer-owned profile, Address, Preference, Consent, isolation, Customer and Account association, registration outcomes, history, use-case orchestration, Contracts, persistence, concurrency, failure, recovery, events, integration, observability, audit, and verification.

This document uses scope code `BCUS`. As an Approved Specification, its Requirements are normative only within the Customer and Account backend scope. It remains subordinate to governing sources, Approved Business Requirements, the Approved Customer Domain, materially applicable Shared Backend Baseline (BEB) Requirements, and materially applicable BIDN Contracts and trusted Identity evidence. It is not repository-wide authority and resolves no Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BCUS-REQ-001 — Lifecycle, Scope, and Authority

BCUS MUST govern only bounded backend specialization of Customer-owned behavior under scope `BCUS`, preserve governing-source and Approved Domain precedence, and MUST NOT be treated as repository-wide authority.

### BCUS-REQ-002 — Customer Domain Specialization

BCUS MUST specialize the Approved Customer Domain without redefining Customer semantics, transferring Customer authority, or creating Identity, Cart, Checkout, Administration, commerce, Notification, Reporting, Analytics, fraud, legal, or Product policy.

### BCUS-REQ-003 — Combined Customer and Account Boundary

Customer and Account business meaning governed by the Customer Domain MUST remain within the same BCUS boundary, while Customer, Account, Identity, Principal, Session, and other business actors remain distinct; BCUS MUST NOT create a separate Account authority or prescribe a concrete Account representation or lifecycle.

### BCUS-REQ-004 — BEB Inheritance

BCUS MUST inherit every materially applicable BEB Requirement without copying, weakening, contradicting, or transferring its authority, explicitly trace each inherited Requirement, and record reviewable evidence for any capability-conditional application.

### BCUS-REQ-005 — BIDN Consumption and Identity Non-Authority

BCUS MAY consume materially applicable BIDN Contracts and trusted Identity evidence, but MUST NOT own or redefine Authentication, credentials, Principal establishment, Sessions, revocation, recovery verification, Roles, Permissions, Claims, Scope, or Identity access evidence.

### BCUS-REQ-006 — Contextual Authorization Ownership

BCUS MUST enforce contextual Authorization for Customer-owned Resources and behavior, but every other Domain MUST retain authority over its own Resource, action, property, association, and current state; authenticated Identity evidence alone MUST NOT authorize Customer or downstream business action.

## 3. Backend Architecture Boundary

### BCUS-REQ-007 — Modular and Hexagonal Boundary

Customer backend behavior MUST preserve the modular-monolith and hexagonal boundaries inherited from BEB, keep Customer Domain and application logic independent of transport, persistence, provider, cloud, and messaging implementations, and interact through intentional project-owned Ports and Contracts.

### BCUS-REQ-008 — Customer Public Module Contract

The Customer backend Module MUST expose only the minimum governed Contract needed for Customer intent, context, accepted outcomes, failure, uncertainty, freshness, and correlation, and MUST NOT expose internal Entities, Repositories, persistence mappings, provider models, credentials, or framework internals.

### BCUS-REQ-009 — Customer Use Case Orchestration

Each supported Customer command or query MUST enter through an explicit Customer-owned Use Case that validates applicable input and context, invokes Customer Domain behavior, coordinates required Ports, preserves command/query meaning, and returns no outcome beyond accepted Customer evidence.

### BCUS-REQ-010 — Transaction and External-Effect Boundary

Customer Application Services MUST own focused Database Transaction boundaries, keep external calls outside unsafe transaction scope, and preserve explicit atomicity, durable intent, uncertainty, and recovery where Customer work spans transactional and external effects.

### BCUS-REQ-011 — Configuration and Operational Safety

Customer configuration, feature flags, migrations, deployments, and bounded work MUST preserve authority, privacy, security, compatibility, safe defaults, failure containment, and recoverability without selecting unresolved mechanisms, topology, values, or rollout policy.

## 4. Customer, Account, and Identity Separation

### BCUS-REQ-012 — Stable Customer Identity

Backend representations MUST preserve stable Customer identity independently of mutable profile, Address, Preference, Consent, Wishlist, credential, Principal, Session, Account access, or downstream association without prescribing identifier format or generation.

### BCUS-REQ-013 — Customer, Principal, and Account Distinction

Backend orchestration and Contracts MUST keep Customer, Account, Identity, Principal, Session, Staff User, and other actors distinct and MUST NOT infer Customer association, Account access, or business authority solely from Identity evidence or an identifier.

### BCUS-REQ-014 — Customer Resource Isolation

Customer-owned Resources MUST remain associated with the correct Customer, and every read or mutation MUST enforce current ownership and contextual Authorization while preventing cross-Customer access, mass assignment, enumeration, and identifier-based authority.

### BCUS-REQ-015 — Account Business Boundary

An Account backend capability MAY expose only Customer-Domain-governed registered-Customer behavior and MUST NOT redefine Identity, Session, Authorization, Customer lifecycle, or downstream commercial truth.

### BCUS-REQ-016 — Account Outcome Neutrality

Account creation, access limitation, recovery coordination, closure, and other lifecycle-dependent outcomes MUST be explicit only where governed and MUST NOT invent Account states, a state machine, Aggregate design, persistence model, or proof of downstream outcomes.

## 5. Registration, Credentials, Recovery, and Sessions

### BCUS-REQ-017 — Governed Customer Registration

Where registration is supported, BCUS MUST coordinate the Customer-owned creation and Customer/Account association outcome with applicable BIDN evidence while preventing accidental privilege, cross-Customer access, unsupported mandatory profile data, and inferred marketing Consent.

### BCUS-REQ-018 — Duplicate and Ambiguous Registration

Duplicate, conflicting, replayed, or ambiguous registration intent MUST produce a safe, distinguishable, duplicate-aware outcome that protects Account existence and Sensitive Data without selecting email-verification, merge, deduplication, or recovery policy.

### BCUS-REQ-019 — Credential Non-Authority

Credentials, passwords, recovery artifacts, Authentication attempts, and Identity security material MUST remain outside Customer-owned content, Contracts, persistence authority, events, logs, exports, and support evidence except for minimum opaque governed references where required.

### BCUS-REQ-020 — Customer Recovery Boundary

Customer recovery coordination MUST preserve Customer identity, isolation, privacy, and history while delegating requester verification, recovery credentials, credential change, Session invalidation, and Identity outcomes to BIDN without selecting channels, factors, expiry, or support policy.

### BCUS-REQ-021 — Session Non-Authority

Session establishment, validity, renewal, expiry, logout, revocation, and compromise evidence MAY affect access to Customer capabilities but MUST NOT create, replace, delete, or rewrite Customer truth or independently authorize a Customer Resource or action.

## 6. Profile and Address

### BCUS-REQ-022 — Governed Profile Access and Mutation

Profile reads and changes MUST enforce Customer ownership, current contextual Authorization, governed validation, Sensitive Data protection, stable identity, explicit accepted outcomes, and safe stale or concurrent conflict handling.

### BCUS-REQ-023 — Profile Data Minimization

Customer profile Contracts and persistence MUST contain only fields supported by an Approved purpose and MUST NOT become a credential store, Authorization source, analytics profile, or repository of invented mandatory demographic data.

### BCUS-REQ-024 — Address Ownership and Mutation

Supported Address creation, access, update, and removal MUST preserve stable Customer association, ownership, validation, isolation, safe failure, and downstream reference constraints without selecting fields, identifier format, validation provider, geocoding, or delivery policy.

### BCUS-REQ-025 — Address Snapshot Non-Authority

Customer Address truth MUST remain distinct from Checkout input and retained Order, Payment, invoice, Shipment, and commercial snapshots; a later Address change or removal MUST NOT rewrite another Domain's accepted history.

## 7. Preference, Consent, and Wishlist

### BCUS-REQ-026 — Preference Ownership and Non-Authority

Supported Preference behavior MUST preserve Customer ownership, validation, privacy, explicit outcomes, and downstream compatibility, and MUST NOT authorize an action, override governing policy, or become Consent without Approved governance.

### BCUS-REQ-027 — Consent Boundary and Non-Inference

BCUS MAY record or expose Consent only for an Approved purpose and MUST preserve purpose, Customer association, evidence, current outcome, change or withdrawal, and separation from Preference, Authentication, Authorization, transactional necessity, and commercial state without inferring Consent from unrelated activity.

### BCUS-REQ-028 — Consent and Preference Mechanism Deferral

BCUS MUST NOT select Consent or Preference categories, defaults, lawful basis, wording, retention, storage, evidence representation, projection, synchronization, communication rules, or marketing policy while those matters remain unresolved.

### BCUS-REQ-029 — Conditional Wishlist Boundary

Wishlist behavior MAY be specialized only where Approved Product policy enables it; BCUS MUST preserve Customer ownership and Product references without deciding guest behavior, limits, sharing, persistence, publication, Price, availability, Stock Reservation, Cart intent, or purchase eligibility.

## 8. History, Creation, Update, and Data Requests

### BCUS-REQ-030 — Governed Historical Access

An authorized Customer MAY access only governed historical references, while Order, Payment, Refund, Shipping, Notifications, and other owners retain lifecycle and record authority and stale or unavailable references remain explicit.

### BCUS-REQ-031 — Historical Truth Preservation

Customer, Account, profile, Address, Preference, Consent, Wishlist, closure, or access changes MUST NOT rewrite confirmed commercial history, Audit Records, or another Domain's retained snapshots.

### BCUS-REQ-032 — Customer Creation and Update Integrity

Customer creation and update MUST preserve stable identity, supported Customer-owned data, correct ownership, validation, isolation, Sensitive Data, concurrency safety, and downstream authority while rejecting invalid, unauthorized, duplicate, ambiguous, or stale intent safely.

### BCUS-REQ-033 — Closure and Data-Request Boundary

Account closure and Customer data access, export, correction, deletion, anonymization, or reactivation behavior MAY exist only where governing Product, privacy, legal, security, retention, audit, and commercial-integrity requirements establish it; BCUS MUST NOT invent workflow, state, format, hard deletion, retention, or reactivation policy.

## 9. Cross-Domain Boundaries

### BCUS-REQ-034 — Product, Pricing, and Inventory Non-Authority

Customer context, profile, Preference, Consent, Wishlist, Account, or history MUST NOT create or redefine Product, publication, sellability, Category, Price, Discount, promotion, tax, Stock, Stock Reservation, Available-to-Sell, or Inventory truth.

### BCUS-REQ-035 — Cart and Checkout Boundary

BCUS MAY supply governed Customer, Account, Address, Preference, Consent, Principal-correlation, and Authorization context to Cart or Checkout, but MUST NOT create Cart intent, Checkout progression, final commercial values, Inventory commitment, delivery eligibility, Payment truth, or Order creation.

### BCUS-REQ-036 — Order, Payment, and Shipping Boundary

Customer history and context MAY reference governed Order, Payment, Refund, and Shipping evidence, but BCUS MUST NOT establish or mutate their identity, lifecycle, financial, fulfilment, delivery, provider, or historical truth.

### BCUS-REQ-037 — Administration Boundary

Administration MAY invoke Customer capabilities only through governed least-privilege Staff workflows with current contextual Authorization, purpose limitation, Customer isolation, and proportional audit; BCUS MUST NOT create Administration policy, Roles, Permissions, approval, override, support, or escalation rules.

### BCUS-REQ-038 — Analytics, Fraud, and Representation Non-Authority

Analytics, fraud signals, reports, projections, frontend state, and support representations MUST remain purpose-limited, stale-capable, non-authoritative inputs or outputs and MUST NOT redefine Customer, Consent, eligibility, Authorization, security, or commercial truth.

## 10. Contracts and Persistence

### BCUS-REQ-039 — Abstract Customer Contract Boundary

Customer backend Contracts MUST describe only governed intent, context, ownership, purpose, source, freshness, outcome, failure, uncertainty, recovery, and correlation and MUST NOT invent routes, HTTP operations or statuses, DTO fields, schemas, payloads, provider formats, or frontend behavior.

### BCUS-REQ-040 — API Security and Compatibility

Where a Customer HTTP API is later governed, it MUST inherit applicable BEB and API validation, versioning, compatibility, concealment, bounded collection, and safe error obligations without exposing credentials, protected Account existence, inaccessible Resources, provider internals, or unnecessary Sensitive Data.

### BCUS-REQ-041 — Customer Data Ownership and Persistence Ports

Customer-owned records MUST remain behind Customer-owned persistence Ports and explicit mappings, preserve database and Module ownership, and MUST NOT directly access or mutate Identity, Cart, Checkout, Administration, or another Domain's internal storage.

### BCUS-REQ-042 — Persistence Integrity and History

Customer persistence MUST protect applicable uniqueness, Customer association, ownership, reference, history, Consent evidence where governed, concurrency, and lifecycle-dependent invariants without selecting schemas, tables, columns, SQL, ORM, indexes, constraints, isolation levels, or locks.

## 11. Concurrency, Events, Failure, and Recovery

### BCUS-REQ-043 — Concurrency and Duplicate Safety

Concurrent, duplicate, retried, replayed, reordered, or stale Customer commands MUST preserve accepted Customer truth, prior effects, current Authorization, ownership, and explicit conflict or uncertainty without selecting idempotency-key format, retention, retry count, or locking mechanism.

### BCUS-REQ-044 — Conditional Customer Events

Customer Domain Events or Integration Events MAY exist only where Approved Architecture, a governed Contract, or downstream reliability requires them; facts MUST preserve Customer authority, privacy, compatibility, correlation, duplicate safety, and producer ownership without selecting names, schemas, payloads, topics, consumers, brokers, or delivery mechanisms.

### BCUS-REQ-045 — Provider and Integration Boundary

Any later Approved Customer integration MUST be isolated behind project-owned Ports and Adapters, validate external evidence, contain provider types and failures, and preserve uncertainty and recovery without selecting providers, protocols, payloads, SDKs, infrastructure, or resilience values.

### BCUS-REQ-046 — Explicit Customer Failure Outcomes

Validation, ownership, Authorization, registration, profile, Address, Preference, Consent, Wishlist, concurrency, persistence, BIDN, downstream, provider, dependency, and unexpected failures MUST remain distinguishable where response, retry, recovery, alerting, or Audit Record handling differs and MUST be disclosed safely.

### BCUS-REQ-047 — Controlled Recovery and Reconciliation

Retry, recovery, and reconciliation MUST preserve original intent, current Authorization, Customer ownership, accepted effects, source authority, provenance, latest truth, and explicit uncertainty; repair MUST be authorized and MUST NOT convert a projection, BIDN evidence, notification, frontend state, or provider assertion into Customer truth.

## 12. Security, Operations, Compatibility, and Verification

### BCUS-REQ-048 — Security, Privacy, and Sensitive Data

Customer backend behavior MUST enforce purpose limitation, minimization, least privilege, Resource isolation, concealment, tamper resistance, and safe handling of PII, Consent evidence, identifiers, and other Sensitive Data across storage, transmission, logs, metrics, traces, errors, Contracts, events, exports, fixtures, and support evidence.

### BCUS-REQ-049 — Observability and Proportional Audit

Customer operations MUST provide safe bounded correlation, Logs, Metrics, Traces, health, and alerting for material outcomes and degradation, and governed high-Risk Customer activity MUST produce Audit Records distinct from ordinary Logs and events without making every read or routine edit high-Risk or selecting retention or numerical targets.

### BCUS-REQ-050 — Accessibility, Boundedness, and Compatibility

Customer backend Contracts and outcomes MUST support governed accessible Customer experiences, bounded delivery, degradation safety, migration, rollback, mixed-version compatibility, and backward-compatible deployment without selecting UI mechanics, payload limits, performance targets, SLAs, or SLOs.

### BCUS-REQ-051 — Domain, Application, Adapter, and Contract Verification

Verification MUST deterministically cover Customer invariants, Use Case orchestration, Identity separation, isolation, Authorization, registration, profile, Address, Preference, Consent, conditional Wishlist, history, concurrency, persistence, Contracts, Adapters, events where applicable, failure, security, compatibility, recovery, and reconciliation beyond mocked success paths.

### BCUS-REQ-052 — Architecture, Operational, and Traceability Verification

Verification MUST prove BEB inheritance, materially applicable BIDN consumption, modular and hexagonal direction, Customer authority, cross-Domain non-authority, configuration safety, observability, audit, operational recovery, implementation neutrality, and complete traceability to every BCUS Requirement.

## 13. Acceptance Criteria

| Acceptance Criterion | Requirement | Acceptance evidence |
| --- | --- | --- |
| BCUS-AC-001 | BCUS-REQ-001 | Metadata and review show `1.0.0 Approved`, `authoritative: false`, scope `BCUS`, normative authority only within the bounded Customer and Account backend scope, and no repository-wide claim. |
| BCUS-AC-002 | BCUS-REQ-002 | Boundary review finds only Customer backend specialization and no redefinition or transfer of Customer or other Domain authority or Product policy. |
| BCUS-AC-003 | BCUS-REQ-003 | Tests preserve all named actor distinctions, keep Customer and Account within BCUS, and find no separate Account authority or concrete representation/lifecycle. |
| BCUS-AC-004 | BCUS-REQ-004 | The BEB matrix covers BEB-REQ-001 through BEB-REQ-056 exactly once and traces every inherited or conditional obligation. |
| BCUS-AC-005 | BCUS-REQ-005 | Integration tests consume only governed BIDN evidence and find no BCUS-owned Authentication, credential, Principal, Session, revocation, recovery-verification, or access truth. |
| BCUS-AC-006 | BCUS-REQ-006 | Authorization tests require current Customer ownership and context and prove Authentication alone grants neither Customer nor downstream authority. |
| BCUS-AC-007 | BCUS-REQ-007 | Architecture tests enforce modular and hexagonal direction and prevent Customer domain/application dependencies on outward implementations. |
| BCUS-AC-008 | BCUS-REQ-008 | Contract review exposes only necessary governed semantics and no internal Entity, Repository, mapping, provider, credential, or framework type. |
| BCUS-AC-009 | BCUS-REQ-009 | Application tests enter every command/query through an explicit Use Case and preserve validation, orchestration, semantic distinction, and evidence-limited outcomes. |
| BCUS-AC-010 | BCUS-REQ-010 | Transaction tests prove focused ownership, safe external-call separation, and explicit atomicity, durable intent, uncertainty, and recovery. |
| BCUS-AC-011 | BCUS-REQ-011 | Configuration, flag, migration, deployment, and bounded-work tests preserve safeguards and select no unresolved mechanism, topology, value, or rollout policy. |
| BCUS-AC-012 | BCUS-REQ-012 | Mapping tests preserve stable Customer identity across all named mutable facts without selecting identifier format or generation. |
| BCUS-AC-013 | BCUS-REQ-013 | Tests vary Customer, Account, Identity, Principal, Session, and Staff User independently and infer no association or authority from evidence alone. |
| BCUS-AC-014 | BCUS-REQ-014 | Isolation tests deny cross-Customer, mass-assignment, enumeration, and identifier-only access for every Customer-owned Resource. |
| BCUS-AC-015 | BCUS-REQ-015 | Account capabilities remain limited to Customer-governed behavior and redefine no Identity, Session, Authorization, lifecycle, or commercial truth. |
| BCUS-AC-016 | BCUS-REQ-016 | Account outcomes are explicit only when governed and introduce no state names, graph, Aggregate, persistence model, or downstream proof. |
| BCUS-AC-017 | BCUS-REQ-017 | Registration produces correlated Customer/Account and BIDN outcomes without excess data, privilege, cross-Customer access, or inferred Consent. |
| BCUS-AC-018 | BCUS-REQ-018 | Duplicate, conflicting, replayed, and ambiguous registration outcomes remain safe and distinct without revealing existence or selecting policy. |
| BCUS-AC-019 | BCUS-REQ-019 | Data-flow review finds no credential or Identity security material in Customer-owned content, persistence, Contracts, observability, events, exports, or support evidence. |
| BCUS-AC-020 | BCUS-REQ-020 | Recovery tests preserve Customer truth and delegate every Identity outcome to BIDN without selecting verification or support mechanics. |
| BCUS-AC-021 | BCUS-REQ-021 | Session changes affect access only and cannot rewrite Customer truth or authorize a Customer Resource or action independently. |
| BCUS-AC-022 | BCUS-REQ-022 | Profile tests enforce ownership, Authorization, validation, data protection, identity, accepted outcomes, and safe stale/concurrent conflict. |
| BCUS-AC-023 | BCUS-REQ-023 | Profile review finds only purpose-supported fields and no credential, Authorization, analytics, or invented demographic data. |
| BCUS-AC-024 | BCUS-REQ-024 | Address tests preserve association, ownership, validation, isolation, failure, and references and select no field, identifier, provider, geocoding, or delivery design. |
| BCUS-AC-025 | BCUS-REQ-025 | Address changes leave Checkout input history and every listed commercial snapshot under its owner unchanged. |
| BCUS-AC-026 | BCUS-REQ-026 | Preference tests preserve ownership, validation, privacy, outcomes, and compatibility and grant no Authorization or Consent. |
| BCUS-AC-027 | BCUS-REQ-027 | Consent tests preserve approved purpose, association, evidence, outcome, change/withdrawal, distinctions, and non-inference. |
| BCUS-AC-028 | BCUS-REQ-028 | Design review finds no selected Consent/Preference taxonomy, default, legal rule, wording, retention, storage, evidence, projection, synchronization, communication, or marketing mechanism. |
| BCUS-AC-029 | BCUS-REQ-029 | Wishlist tests run only where policy enables behavior and create none of the prohibited guest, limit, sharing, persistence, catalogue, inventory, Cart, or purchase truth. |
| BCUS-AC-030 | BCUS-REQ-030 | Historical access is Customer-authorized, source-governed, and explicit when stale or unavailable, while source owners retain lifecycle authority. |
| BCUS-AC-031 | BCUS-REQ-031 | Every listed Customer change leaves commercial history, Audit Records, and another Domain's snapshots unchanged. |
| BCUS-AC-032 | BCUS-REQ-032 | Creation/update tests cover identity, supported data, ownership, validation, isolation, security, concurrency, authority, and all rejection cases. |
| BCUS-AC-033 | BCUS-REQ-033 | Closure/data-request review selects no workflow, state, format, deletion, retention, anonymization, or reactivation policy and preserves governing obligations. |
| BCUS-AC-034 | BCUS-REQ-034 | Boundary tests prove Customer context creates or changes none of the listed Product, Pricing, or Inventory truth. |
| BCUS-AC-035 | BCUS-REQ-035 | Cart/Checkout integration receives only governed context and creates none of the prohibited intent, progression, commercial, inventory, delivery, Payment, or Order truth. |
| BCUS-AC-036 | BCUS-REQ-036 | Customer history references source-governed evidence and cannot create or mutate Order, Payment, Refund, or Shipping truth. |
| BCUS-AC-037 | BCUS-REQ-037 | Staff tests require governed Administration invocation, least privilege, Authorization, purpose, isolation, and audit and create no policy or matrix. |
| BCUS-AC-038 | BCUS-REQ-038 | Analytics, fraud, reports, projections, frontend, and support evidence remain non-authoritative and create none of the listed Customer or business truth. |
| BCUS-AC-039 | BCUS-REQ-039 | Contract review contains only abstract governed semantics and no route, HTTP, DTO, schema, payload, provider, or frontend choice. |
| BCUS-AC-040 | BCUS-REQ-040 | Applicable API tests prove inherited validation, compatibility, concealment, bounds, and safe errors with no protected or sensitive leakage. |
| BCUS-AC-041 | BCUS-REQ-041 | Persistence tests use Customer-owned Ports and mappings and prevent direct access to every other Domain's internal storage. |
| BCUS-AC-042 | BCUS-REQ-042 | Integrity tests protect all named invariants while design review finds no selected physical schema, ORM, index, constraint, isolation, or lock. |
| BCUS-AC-043 | BCUS-REQ-043 | Concurrency tests preserve accepted truth, prior effects, Authorization, ownership, conflict, and uncertainty without selecting an idempotency or locking mechanism. |
| BCUS-AC-044 | BCUS-REQ-044 | Event review permits only governed Customer facts and finds no invented name, schema, payload, topic, consumer, broker, or delivery mechanism. |
| BCUS-AC-045 | BCUS-REQ-045 | Adapter tests isolate and validate external evidence and failure while selecting no provider, protocol, payload, SDK, infrastructure, or resilience value. |
| BCUS-AC-046 | BCUS-REQ-046 | Failure tests distinguish every applicable category and disclose no Account existence, inaccessible Resource, Sensitive Data, or internal detail. |
| BCUS-AC-047 | BCUS-REQ-047 | Recovery/reconciliation tests preserve intent, Authorization, ownership, effects, provenance, truth, and uncertainty and promote no representation or external evidence to truth. |
| BCUS-AC-048 | BCUS-REQ-048 | Security/privacy review proves purpose limitation, minimization, least privilege, isolation, concealment, tamper resistance, and safe Sensitive Data handling across every listed surface. |
| BCUS-AC-049 | BCUS-REQ-049 | Observability distinguishes material outcomes safely; Audit Records remain proportional and separate with no universal high-Risk classification, retention, or numerical target. |
| BCUS-AC-050 | BCUS-REQ-050 | Contract and deployment tests support accessibility, bounds, degradation, migration, rollback, mixed versions, and compatibility without UI or numerical choices. |
| BCUS-AC-051 | BCUS-REQ-051 | Deterministic Domain, application, Adapter, and Contract evidence covers every listed positive, negative, boundary, concurrency, failure, and recovery concern beyond mocked success. |
| BCUS-AC-052 | BCUS-REQ-052 | Architecture and operational evidence proves inheritance, BIDN consumption, direction, authority, safety, neutrality, recovery, and complete BCUS traceability. |

## 14. Requirement Traceability

| BCUS Requirement | Customer Domain source | BEB inheritance | BIDN / additional governing source |
| --- | --- | --- | --- |
| BCUS-REQ-001 | REQ-CUS-001–002 | BEB-REQ-001–004, 056 | ADR-0003; ARCHITECTURE.md §35 |
| BCUS-REQ-002 | REQ-CUS-001–002, 030–038 | BEB-REQ-003–004 | ADR-0003 Authority Boundary |
| BCUS-REQ-003 | REQ-CUS-004, 006–007 | BEB-REQ-003–004 | ADR-0003 Decision; REQ-IDN-005 |
| BCUS-REQ-004 | REQ-CUS-001 | BEB-REQ-001–004, 056 | ADR-0003; ARCHITECTURE.md §35 |
| BCUS-REQ-005 | REQ-CUS-004, 010–012 | BEB-REQ-019 | BIDN-REQ-005, 012, 019–023 |
| BCUS-REQ-006 | REQ-CUS-005, 039 | BEB-REQ-011, 019–020 | BIDN-REQ-004, 006, 021 |
| BCUS-REQ-007 | REQ-CUS-001–002 | BEB-REQ-005–007 | ARCHITECTURE.md §§8, 11, 35 |
| BCUS-REQ-008 | REQ-CUS-047 | BEB-REQ-008 | API.md; ADR-0003 |
| BCUS-REQ-009 | REQ-CUS-026–027, 048 | BEB-REQ-009–010 | SPRING.md; ARCHITECTURE.md §35.2 |
| BCUS-REQ-010 | REQ-CUS-026–027, 048 | BEB-REQ-012, 025–026, 028 | DATABASE.md; SPRING.md |
| BCUS-REQ-011 | REQ-CUS-044, 048–049 | BEB-REQ-046–051 | ARCHITECTURE.md §§38, 42–45 |
| BCUS-REQ-012 | REQ-CUS-003 | BEB-REQ-021–024 | Customer Domain §§5–6 |
| BCUS-REQ-013 | REQ-CUS-004, 006, 012 | BEB-REQ-019, 021 | REQ-IDN-005; BIDN-REQ-011 |
| BCUS-REQ-014 | REQ-CUS-005, 039–040 | BEB-REQ-011, 015, 020, 043 | SECURITY-STANDARDS.md §§12, 35 |
| BCUS-REQ-015 | REQ-CUS-006 | BEB-REQ-003–004, 019 | REQ-IDN-007; BIDN-REQ-002 |
| BCUS-REQ-016 | REQ-CUS-007 | BEB-REQ-010, 024, 056 | PRODUCT.md §24; ADR-0003 Exclusions |
| BCUS-REQ-017 | REQ-CUS-008 | BEB-REQ-009, 025, 041 | REQ-IDN-008; BIDN-REQ-005, 017 |
| BCUS-REQ-018 | REQ-CUS-009, 048 | BEB-REQ-029–031, 041, 043 | BIDN-REQ-013–015 |
| BCUS-REQ-019 | REQ-CUS-010, 040 | BEB-REQ-043–044 | BIDN-REQ-016; SECURITY-STANDARDS.md §§11, 14 |
| BCUS-REQ-020 | REQ-CUS-011 | BEB-REQ-011, 029–031, 041–043 | BIDN-REQ-018–019 |
| BCUS-REQ-021 | REQ-CUS-012, 039 | BEB-REQ-011, 019–020 | BIDN-REQ-020–023 |
| BCUS-REQ-022 | REQ-CUS-013, 027, 039–040 | BEB-REQ-011, 015, 027, 043 | API.md; DATABASE.md |
| BCUS-REQ-023 | REQ-CUS-014, 040 | BEB-REQ-014–015, 021, 043 | SECURITY-STANDARDS.md §35 |
| BCUS-REQ-024 | REQ-CUS-015–016, 039–040 | BEB-REQ-011, 015, 021–023, 027 | API.md; DATABASE.md |
| BCUS-REQ-025 | REQ-CUS-017, 025, 036 | BEB-REQ-021, 024 | Checkout, Order, Payment, and Shipping authority |
| BCUS-REQ-026 | REQ-CUS-018–019 | BEB-REQ-009–010, 021 | PRODUCT.md §24 item 19 |
| BCUS-REQ-027 | REQ-CUS-020–021 | BEB-REQ-009–010, 021, 024 | SECURITY-STANDARDS.md §35 |
| BCUS-REQ-028 | REQ-CUS-018–021 | BEB-REQ-047, 056 | PRODUCT.md §24 item 19; ADR-0003 Exclusions |
| BCUS-REQ-029 | REQ-CUS-022–023 | BEB-REQ-003, 009, 021, 056 | PRODUCT.md §24 item 16 |
| BCUS-REQ-030 | REQ-CUS-024 | BEB-REQ-011, 016, 020–021, 024 | Order, Payment, Shipping, Notifications authority |
| BCUS-REQ-031 | REQ-CUS-025 | BEB-REQ-021, 024 | Customer Domain §16 |
| BCUS-REQ-032 | REQ-CUS-026–027 | BEB-REQ-015, 023, 025, 027, 041 | DATABASE.md; API.md |
| BCUS-REQ-033 | REQ-CUS-028–029, 041 | BEB-REQ-021, 024, 043, 045, 049, 056 | PRODUCT.md §24 item 28 |
| BCUS-REQ-034 | REQ-CUS-030–032 | BEB-REQ-003, 021 | Product, Pricing, Inventory Domain authority |
| BCUS-REQ-035 | REQ-CUS-033 | BEB-REQ-003, 008, 011, 019 | Cart and Checkout Domain Specifications |
| BCUS-REQ-036 | REQ-CUS-024–025, 034–036 | BEB-REQ-003, 021, 024 | Order, Payment, Shipping Domain authority |
| BCUS-REQ-037 | REQ-CUS-037, 039–040, 045 | BEB-REQ-011, 020, 043, 045 | Administration Domain; BIDN-REQ-024–026 |
| BCUS-REQ-038 | REQ-CUS-038, 042 | BEB-REQ-003, 021, 043 | PRODUCT.md §24 items 20, 26 |
| BCUS-REQ-039 | REQ-CUS-047 | BEB-REQ-008, 013–018, 056 | API.md; ADR-0003 Exclusions |
| BCUS-REQ-040 | REQ-CUS-005, 039–040, 044, 047–048 | BEB-REQ-013–020, 043–044 | API.md; SECURITY-STANDARDS.md §§19–20 |
| BCUS-REQ-041 | REQ-CUS-001–002, 040 | BEB-REQ-021–022 | DATABASE.md; POSTGRES.md |
| BCUS-REQ-042 | REQ-CUS-003, 005, 015, 020, 025, 040 | BEB-REQ-023–027 | DATABASE.md; POSTGRES.md |
| BCUS-REQ-043 | REQ-CUS-009, 027, 048 | BEB-REQ-027, 029–031, 041–042 | API.md; DATABASE.md |
| BCUS-REQ-044 | REQ-CUS-046 | BEB-REQ-028, 037–040 | EVENTS.md; ARCHITECTURE.md §14 |
| BCUS-REQ-045 | REQ-CUS-041–042, 048 | BEB-REQ-026, 032–036, 041–044 | ADR-0003 Exclusions |
| BCUS-REQ-046 | REQ-CUS-048 | BEB-REQ-017, 041 | API.md; SPRING.md; JAVA.md |
| BCUS-REQ-047 | REQ-CUS-025, 027, 048 | BEB-REQ-027, 029–031, 034, 042, 046 | DATABASE.md; EVENTS.md |
| BCUS-REQ-048 | REQ-CUS-040–041 | BEB-REQ-043–044 | SECURITY-STANDARDS.md §§27, 35 |
| BCUS-REQ-049 | REQ-CUS-044–045 | BEB-REQ-045–046, 051 | SECURITY-STANDARDS.md §§27–28 |
| BCUS-REQ-050 | REQ-CUS-043–044, 047 | BEB-REQ-018, 049–051 | TESTING-STANDARDS.md; API.md |
| BCUS-REQ-051 | REQ-CUS-049 | BEB-REQ-052–053, 055 | TESTING-STANDARDS.md |
| BCUS-REQ-052 | REQ-CUS-001–002, 049 | BEB-REQ-054–056 | AGENTS.md; ARCHITECTURE.md §§35, 41–42 |

## 15. BEB Applicability and Inheritance Matrix

All BEB Requirements are materially applicable to BCUS except where their stated capability condition is absent. Conditional rows remain inherited whenever BCUS later supports that capability; this matrix neither authorizes the capability nor selects implementation.

| BEB Requirement(s) | BCUS treatment | Primary BCUS Requirement(s) |
| --- | --- | --- |
| BEB-REQ-001, BEB-REQ-002, BEB-REQ-003, BEB-REQ-004 | Inherited: lifecycle, precedence, inheritance, and specialization boundary. | BCUS-REQ-001–006 |
| BEB-REQ-005, BEB-REQ-006, BEB-REQ-007, BEB-REQ-008 | Inherited: modular, hexagonal, layer, and public Module boundaries. | BCUS-REQ-007–009, 052 |
| BEB-REQ-009, BEB-REQ-010, BEB-REQ-011, BEB-REQ-012 | Inherited: Use Cases, command/query meaning, contextual Authorization, and transaction ownership. | BCUS-REQ-006, 009–010 |
| BEB-REQ-013, BEB-REQ-014, BEB-REQ-015, BEB-REQ-016, BEB-REQ-017, BEB-REQ-018 | Conditionally inherited for later governed Customer HTTP Contracts; no concrete API is defined. | BCUS-REQ-039–040, 050–051 |
| BEB-REQ-019, BEB-REQ-020 | Inherited for trusted Identity evidence, Customer isolation, contextual Authorization, and concealment. | BCUS-REQ-005–006, 013–021, 040 |
| BEB-REQ-021, BEB-REQ-022, BEB-REQ-023, BEB-REQ-024, BEB-REQ-025, BEB-REQ-026, BEB-REQ-027, BEB-REQ-028 | Inherited for Customer-owned data, Ports, integrity, history, consistency, external effects, concurrency, and durable workflow state. | BCUS-REQ-010, 012, 022–033, 041–043 |
| BEB-REQ-029, BEB-REQ-030, BEB-REQ-031 | Inherited where repeated Customer work could create harmful or duplicate effects. | BCUS-REQ-018, 020, 032, 043, 047 |
| BEB-REQ-032, BEB-REQ-033, BEB-REQ-034, BEB-REQ-035, BEB-REQ-036 | Conditionally inherited for any later Approved Customer provider, Callback, or Webhook integration; none is selected. | BCUS-REQ-045–047 |
| BEB-REQ-037, BEB-REQ-038, BEB-REQ-039, BEB-REQ-040 | Conditionally inherited wherever governed Customer events apply; external messaging remains unresolved. | BCUS-REQ-044–045, 051 |
| BEB-REQ-041, BEB-REQ-042 | Inherited for Customer failure, retry, recovery, and reconciliation. | BCUS-REQ-018, 032, 043, 046–047 |
| BEB-REQ-043, BEB-REQ-044, BEB-REQ-045 | Inherited for security, Secrets, Sensitive Data, payment-data non-exposure where encountered, and Audit Record separation. BCUS owns no Payment data. | BCUS-REQ-014, 019–020, 033, 037–040, 045, 048–049 |
| BEB-REQ-046, BEB-REQ-047, BEB-REQ-048 | Inherited for observability, configuration, Environment, and feature-flag safety without selecting mechanisms. | BCUS-REQ-011, 049–050 |
| BEB-REQ-049, BEB-REQ-050, BEB-REQ-051 | Inherited for migration, deployment compatibility, bounded work, and failure containment. | BCUS-REQ-011, 046, 049–050 |
| BEB-REQ-052, BEB-REQ-053, BEB-REQ-054, BEB-REQ-055 | Inherited for Domain/application, Adapter/integration, architecture/operations, and traceable verification. | BCUS-REQ-051–052 |
| BEB-REQ-056 | Inherited: policy and implementation neutrality. | BCUS-REQ-001–004, 011, 016, 028–029, 033, 039, 042, 044–045, 050, 052 |

## 16. BIDN Consumption Boundary

| BIDN capability/evidence | BCUS use | Preserved authority boundary |
| --- | --- | --- |
| Trusted Principal and Authentication evidence | Establish actor context for protected Customer Use Cases. | BIDN owns Authentication and Principal establishment; BCUS independently enforces Customer association and Authorization. |
| Session and revocation evidence | Determine whether Identity context remains applicable. | BIDN owns Session and revocation truth; Session evidence neither creates Customer truth nor authorizes alone. |
| Recovery-verification and credential outcomes | Coordinate Customer-side recovery context where governed. | BIDN owns verification, credentials, change, invalidation, and Identity recovery outcomes. |
| Role, Permission, Claims, Scope, and access evidence | Supply current security context where applicable. | BIDN owns Identity evidence; Customer and every other owning Domain retain contextual Authorization. |
| Identity failure, uncertainty, and correlation | Preserve distinguishable cross-boundary outcomes and recovery. | BCUS cannot reinterpret uncertain Identity evidence as Customer success or truth. |

## 17. Open Product and Architecture Decisions

The following 16 materially relevant decisions remain unresolved by BCUS.

| Source | Open decision | BCUS boundary |
| --- | --- | --- |
| PRODUCT.md §24 item 4 | Guest checkout versus mandatory account rules | BCUS does not make Account creation a Checkout prerequisite or decide guest behavior. |
| PRODUCT.md §24 item 5 | Customer email-verification requirements | BCUS selects no requirement, channel, timing, provider, or mechanism. |
| PRODUCT.md §24 item 16 | Wishlist behaviour for guest and registered customers | Wishlist remains conditional; guest behavior, limits, sharing, and persistence are unresolved. |
| PRODUCT.md §24 item 18 | Customer-support channels and service expectations | BCUS defines no support channel, target, or workflow. |
| PRODUCT.md §24 item 19 | Marketing-consent and communication-preference model | BCUS defines no taxonomy, channel rule, lawful basis, or marketing policy. |
| PRODUCT.md §24 item 20 | Initial analytics provider and event taxonomy | Analytics remains non-authoritative and provider-neutral. |
| PRODUCT.md §24 item 23 | Administrative role and permission matrix | BCUS defines no Customer-administration matrix. |
| PRODUCT.md §24 item 24 | Production customer-service and operational escalation process | BCUS defines no escalation or repair policy. |
| PRODUCT.md §24 item 26 | Fraud-screening approach and manual-review workflow | BCUS defines no vendor, model, score, threshold, blocking rule, or workflow. |
| PRODUCT.md §24 item 28 | Customer data export, correction, deletion, and account-closure workflow | BCUS defines no workflow, format, deletion, retention, anonymization, closure state, or reactivation policy. |
| ARCHITECTURE.md §34 item 3 | Customer and administrator session/token strategy | BCUS selects no Identity, token, Session, cookie, storage, rotation, or lifetime mechanism. |
| ARCHITECTURE.md §34 item 8 | Redis introduction and approved use cases | BCUS assumes no cache, Session, or storage technology. |
| ARCHITECTURE.md §34 item 9 | External messaging introduction and service selection | BCUS assumes no broker or external messaging adoption. |
| ARCHITECTURE.md §34 item 12 | Backup retention and production recovery objectives | BCUS preserves recoverability, restoration compatibility, reconciliation, and operational-recovery verification without selecting retention periods, recovery objectives, backup mechanisms, or numerical recovery targets. |
| ARCHITECTURE.md §34 item 13 | PostgreSQL schema strategy for enforcing Domain ownership | BCUS selects no schema or physical ownership mechanism. |
| ARCHITECTURE.md §34 item 14 | Repository-wide feature-flag implementation and lifecycle management | BCUS preserves flag safety without selecting tooling or lifecycle. |

Customer identifier design, concrete Account representation or lifecycle, Consent and Preference mechanisms, Identity Provider and Identity mechanisms, concrete APIs, persistence schemas, event Contracts, providers, infrastructure, and numerical values also remain unresolved. Every Backend Specification title, path, scope code, decomposition, and ordering position after BCUS remains out of scope.

## 18. Risks and Controls

| Risk | BCUS control direction |
| --- | --- |
| Cross-Customer access | Enforce current Principal-to-Customer association, Resource ownership, contextual Authorization, and concealment. |
| Identity evidence becomes Customer authority | Treat BIDN evidence as security context only and apply Customer-owned association and invariants independently. |
| Credential or recovery data enters Customer storage | Keep Identity security material outside Customer content, persistence, observability, events, and exports. |
| Account conflated with Identity or Session | Preserve canonical distinctions and Customer-owned Account business meaning. |
| Mutable profile or Address rewrites history | Preserve stable Customer identity and owning-Domain commercial snapshots. |
| Preference becomes Consent or Authorization | Require Approved purpose and keep Preference, Consent, Authorization, and necessity distinct. |
| Duplicate registration or update corrupts ownership | Preserve request identity, uniqueness, concurrency safety, accepted effects, and explicit conflict. |
| Data-request implementation invents legal policy | Defer workflow, retention, deletion, anonymization, and reactivation to qualified governance. |
| Projection, analytics, frontend, or support state becomes truth | Mark representations stale-capable and non-authoritative and reconcile against owning sources. |
| Concrete design becomes accidental policy | Keep Contracts abstract and prohibit ungoverned schema, event, provider, mechanism, topology, and numerical choices. |

## 19. Related Documents

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
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/API.md`
- `.ai/backend/EVENTS.md`
- `specifications/adr/ADR-0003-customer-account-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/domains/customer/customer-domain.md`
- `specifications/domains/identity/identity-domain.md`
- `specifications/domains/cart/cart-domain.md`
- `specifications/domains/checkout/checkout-domain.md`
- `specifications/domains/admin/admin-domain.md`

## 20. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Draft | Initial Customer and Account Backend Specification established under Accepted ADR-0003 with explicit Customer Domain specialization, BEB inheritance, and bounded BIDN consumption. |
| 1.0.0 | 2026-09-17 | Approved | Approved the Customer and Account Backend Specification after approval-readiness validation, preserving Customer Domain authority, complete BEB inheritance, bounded BIDN consumption, and all unresolved Product and Architecture Decisions. |

## 21. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `1.0.0 Approved`, `authoritative: false`, scope is `BCUS`, and BCUS is normative only within its bounded Customer and Account backend scope;
2. Customer and Account remain within one BCUS boundary while the Approved Customer Domain retains semantic authority;
3. all 56 BEB Requirements have an explicit inherited or conditional disposition and trace;
4. materially applicable BIDN evidence is consumed without transferring Identity authority;
5. contextual Authorization remains with the Domain owning the affected Resource, action, property, association, and current state;
6. every Customer-owned and cross-Domain boundary named in this Specification remains intact;
7. no API, DTO, schema, event, provider, infrastructure, identifier, Account model, Consent/Preference mechanism, Identity mechanism, matrix, numerical value, or unresolved Product policy is invented;
8. every BCUS Requirement has exactly one complete Acceptance Criterion and one traceability row;
9. all 16 listed Open Product and Architecture Decisions remain unresolved;
10. every Backend Specification title, path, scope code, decomposition, and order after BCUS remains out of scope;
11. all Related Documents exist and are materially relevant;
12. Markdown, tables, headings, UTF-8, whitespace, and final newline validation pass; and
13. the BCUS lifecycle change affects only `specifications/backend/customer/customer-backend.md` and introduces no unrelated repository changes.
