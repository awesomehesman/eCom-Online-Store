---
title: CODING-STANDARDS
version: 1.0.2
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Coding Standards

## 1. Purpose

This document defines the repository-wide implementation-quality baseline. It exists to make code correct, consistent, maintainable, secure, testable, observable, and faithful to the approved architecture. It applies equally to human-authored and AI-assisted work and provides reviewers with explicit, enforceable expectations.

## 2. Scope

These standards apply to Angular frontend code, Spring Boot backend code, Java, TypeScript, HTML templates, CSS/SCSS, SQL, persistence code, APIs, integrations, infrastructure-related source, scripts, tests, configuration, generated code, and AI-generated code.

Lower-level Specifications and technology standards MAY strengthen these rules but MUST NOT weaken or contradict them.

## 3. Normative Language

- **MUST** and **MUST NOT** define mandatory requirements.
- **SHOULD** and **SHOULD NOT** define strong recommendations; deviations require documented rationale.
- **MAY** defines an optional practice.

## 4. Repository Authority

Implementation decisions MUST follow the Decision Hierarchy in `AGENTS.md`.

- `AGENTS.md` governs repository behavior, process, and completion.
- `GLOSSARY.md` governs canonical terminology.
- `PRODUCT.md` governs product and business semantics.
- `ARCHITECTURE.md` governs structure, ownership, dependency direction, and boundaries.
- `SECURITY-STANDARDS.md` governs mandatory security requirements and Security Exceptions.
- `TESTING-STANDARDS.md` governs verification.
- This document governs implementation quality and code-level conventions.

This document MUST NOT redefine a higher-authority rule. Conflicts MUST be resolved through the repository Decision Hierarchy and MUST NOT be silently encoded in implementation.

## 5. Core Coding Principles

Code MUST favor clarity over cleverness, explicit intent over hidden behavior, and correctness over convenience. Implementations MUST follow least surprise, separation of concerns, dependency inversion, secure defaults, testability, and observability.

Units SHOULD be small and cohesive, state SHOULD be immutable where appropriate, and invalid boundary input SHOULD fail early and safely. Contributors MUST minimize meaningful duplication without creating premature abstractions or speculative generalizations.

## 6. Repository and Module Boundaries

Modules and bounded contexts MUST preserve the ownership and dependency direction defined by `ARCHITECTURE.md`.

- Domain code MUST NOT depend on framework, persistence, provider, cloud, or transport implementations.
- Application Services MAY depend on domain code and Ports but MUST NOT depend on concrete Adapters.
- Inbound Adapters MUST translate external input into Commands, Queries, or Use Case calls.
- Outbound Adapters MUST implement approved Ports.
- Repositories and persistence mappings MUST remain internal to the owning Module.
- Cross-domain access MUST use an intentional public Contract, Use Case, or Integration Event.
- Shared libraries MUST contain stable, genuinely shared capabilities and MUST NOT become a route around domain ownership.

Code MUST NOT import another Module's internal Entity, Aggregate, Repository, persistence model, or provider implementation.

## 7. Naming Standards

Names MUST reveal intent and use exact canonical terminology from `GLOSSARY.md`. Classes and interfaces SHOULD name cohesive responsibilities; methods and functions SHOULD name actions or answers; variables SHOULD identify their meaning and scope; constants MUST identify stable values; and enums or union types MUST describe a closed set.

Commands, Queries, Domain Events, Integration Events, DTOs, requests, responses, database objects, tests, files, and directories MUST use consistent domain language. Abbreviations MAY be used only when established and unambiguous. Names such as `Manager`, `Helper`, `Utils`, `Common`, or `Misc` SHOULD NOT be used unless a narrow responsibility makes the name accurate. Canonical concepts MUST NOT receive local aliases.

## 8. General Code Structure

Files, functions, methods, and classes MUST remain cohesive and reviewable; no arbitrary line-count limit applies. Public APIs MUST be minimal and intentional. Encapsulation MUST prevent invalid state and unauthorized mutation.

Control flow SHOULD use early returns where they reduce nesting. Deep nesting, duplicated business logic, dead code, unused exports, commented-out code, and unreachable branches MUST be removed. Comments MUST explain information the code cannot express clearly. Temporary work MUST follow section 38.

## 9. Error Handling

Code MUST distinguish domain, validation, Authorization, infrastructure, conflict, and transient failures where behavior differs. APIs MUST translate errors at an exception boundary into consistent Problem Details without leaking internals.

Retryable and non-retryable errors MUST remain distinguishable. Exceptions MUST NOT be swallowed, replaced with ambiguous success, or logged and rethrown repeatedly at every layer. Causality and safe diagnostic context MUST be preserved. Customer-facing messages MUST be safe and actionable without exposing Secrets, credentials, stack traces, or sensitive data.

## 10. Logging and Observability in Code

Production code MUST use structured logging with appropriate correlation and causation identifiers. Security-sensitive and business-critical actions MUST emit the Audit Records or telemetry required by `SECURITY-STANDARDS.md` and `ARCHITECTURE.md`.

Passwords, Access Tokens, Refresh Tokens, raw CVV, Secrets, private keys, full payment credentials, and unnecessary PII MUST NOT be logged. Log levels MUST reflect operational severity. Debug logging MUST NOT create production noise or sensitive exposure. Material workflows SHOULD emit useful metrics and traces with bounded-cardinality attributes.

## 11. Configuration Standards

Environment-specific values MUST come from approved configuration sources and MUST NOT be hard-coded. Required configuration MUST be typed where supported and validated at startup. Defaults MUST be explicit, safe, and appropriate across Environments.

Configuration ownership MUST be clear. Feature Flags MUST follow section 47. Profiles MUST NOT conceal production-only behavior that lacks equivalent validation.

## 12. Secrets in Code

Secret handling is governed by `SECURITY-STANDARDS.md`. Code, tests, fixtures, configuration, logs, examples, and generated output MUST NOT contain hard-coded Secrets, committed tokens, production credentials, private keys, Payment Provider Secrets, or connection strings containing credentials.

Secret values MUST use approved Secret management and MUST be scoped, rotated, and exposed only to the runtime Principal that requires them.

## 13. TypeScript Standards

TypeScript MUST use strict typing. `unknown` MUST be preferred over `any` for untrusted values and narrowed before use. Arbitrary `any`, unchecked casts, and repeated non-null assertions are prohibited.

Public APIs SHOULD have explicit types. Data SHOULD be `readonly` where mutation is not required. Discriminated unions SHOULD model closed state variants. `null` and `undefined` handling MUST be deliberate. Union types SHOULD be preferred for simple closed string sets; enums MAY be used when runtime identity or interoperability justifies them. Generics and utility types MUST improve safety rather than obscure meaning.

## 14. Angular Standards

Angular code MUST align with the approved Angular 20 architecture:

- Components MUST be standalone and feature-oriented.
- Signals and `computed` SHOULD own synchronous local and derived state.
- `effect` MUST be reserved for necessary side effects and MUST NOT replace derived state.
- RxJS MUST be used for HTTP, event streams, cancellation, and asynchronous composition.
- An explicit store MAY be used only where an approved cross-component state need justifies it.
- Dependency injection MUST use clear, narrow dependencies.
- Inputs and outputs MUST form intentional component APIs.
- Typed Reactive Forms MUST be used for nontrivial forms.
- Routes SHOULD be lazy-loaded at feature boundaries.
- Components MUST coordinate presentation and interaction, not own authoritative business rules.
- HTTP clients and interceptors MUST remain in approved data-access or core boundaries.
- Subscriptions MUST be torn down with `takeUntilDestroyed` or an equivalent lifecycle-safe mechanism.
- Nested subscriptions MUST NOT be used when an appropriate flattening operator expresses the flow.
- Direct DOM manipulation MUST be avoided unless no safe Angular or platform abstraction satisfies the need.
- Sanitization MUST NOT be bypassed; unsafe values MUST be handled under `SECURITY-STANDARDS.md`.

State changes MUST work with the approved change-detection model and MUST NOT depend on hidden mutation.

## 15. Angular Template Standards

Templates MUST remain clear, semantic, accessible, and free of authoritative business logic. Expressions MUST be inexpensive and side-effect free. Repeated rendering MUST use stable track expressions.

Native semantic HTML MUST be preferred. Forms MUST associate labels, instructions, and errors correctly. Loading, empty, success, and error states MUST be explicit where applicable. Unsafe HTML bindings are prohibited unless content is trusted and handled through an approved sanitization design. Stable test selectors MAY be added when user-facing semantics are not sufficient.

## 16. Frontend State Management

State MUST have one clear owner. Local UI state SHOULD remain in a component; feature state SHOULD remain in its feature; cross-route state requires an approved shared owner. Signals SHOULD hold synchronous UI state, while RxJS SHOULD represent asynchronous streams and cancellation.

Server state MUST NOT be duplicated as competing authoritative client state. Derived values MUST be computed rather than independently mutated. Updates SHOULD be immutable. Normalized state MAY be used where relationships and updates justify it. State lifecycle, reset, logout, and navigation behavior MUST be explicit.

## 17. RxJS Standards

Observable names MUST be clear and consistent within their owning area; a `$` suffix MAY be used where an established local convention distinguishes streams. Operator selection MUST match semantics: `switchMap` for replacement and cancellation, `concatMap` for ordered serialization, `exhaustMap` for ignoring re-entry, and `mergeMap` for intentional concurrency.

Streams MUST define teardown and error behavior. Retry MUST be bounded, safe, and appropriate to the operation. `shareReplay` MUST be used cautiously with explicit lifecycle and caching semantics. Side effects SHOULD be isolated in `tap` or a boundary, not hidden in transformations. Nested subscriptions are prohibited where composition is possible. `Subject` MUST NOT be the default state store; `BehaviorSubject` requires a justified streaming need. Signals SHOULD hold synchronous UI state.

## 18. Java Standards

Java code MUST use the approved Java 21 LTS baseline and modern language features where they improve clarity. Domain and transport values SHOULD be immutable; records MAY represent immutable data carriers that do not require Entity identity or mutable lifecycle.

`Optional` MAY represent an absent return value but MUST NOT be used indiscriminately for fields, parameters, or serialization. Null handling MUST be explicit. Collections SHOULD expose the least mutable form required. Streams SHOULD be used when clearer than loops, not as a goal. Equality and hash codes MUST reflect stable semantics. Value Objects MUST enforce validity. Constructors MUST establish required state. Static mutable state is prohibited. Concurrent code MUST document and enforce thread-safety assumptions.

## 19. Spring Boot Standards

Spring Boot code MUST use constructor injection. Controllers MUST validate and translate transport input, enforce the applicable security boundary, invoke an Application Service, and return approved DTOs; they MUST NOT contain business or persistence logic.

Application Services MUST own Use Case orchestration and Database Transaction boundaries. Domain Services MUST contain stateless domain behavior that does not naturally belong to an Entity or Value Object. Repositories MUST remain behind approved interfaces. Validation and Exception Handlers MUST be consistent. Security filters MUST fail safely.

Configuration MUST use validated configuration properties. Profiles MUST remain limited and explicit. Jobs, Schedulers, and asynchronous work MUST be idempotent where retry is possible, observable, and concurrency-safe. External clients MUST use outbound Adapters. Health endpoints MUST reveal only approved information.

## 20. Domain Model Standards

Aggregates and Aggregate Roots MUST protect invariants and valid state transitions. Entities MUST own identity-based behavior. Value Objects MUST be immutable where practical and validate their values. Domain Services MUST express domain behavior that spans concepts without becoming orchestration containers.

Domain Events MUST describe completed domain facts. Domain code MUST enforce ownership and MUST NOT expose mutation that bypasses invariants. Models SHOULD avoid anemic design when behavior belongs in the domain, but behavior MUST NOT be forced into Entities when an Application Service owns coordination, Authorization, Database Transactions, or external effects.

## 21. Application Layer Standards

Application Services MUST implement Use Cases through explicit Commands and Queries, orchestrate domain behavior, define Database Transaction boundaries, apply trusted Authorization, coordinate idempotency, and map between boundary and domain models.

The application layer MUST NOT contain UI concerns or depend on concrete infrastructure. Queries MUST NOT mutate domain state. Commands MUST make intended state change explicit. Duplicate-sensitive operations MUST define Idempotency Key behavior and result reuse.

## 22. API Implementation Standards

APIs MUST use project-owned request and response models, validate all external input, return consistent Problem Details, follow HTTP semantics, propagate correlation identifiers, enforce server-side Authorization, and follow approved versioning and Cursor Pagination.

State-changing duplicate-sensitive operations MUST implement Idempotency Key semantics. CORS MUST use approved allowlists. Entities and Aggregates MUST NOT be exposed directly. Client-provided Price, Discount, Role, Permission, Inventory state, Payment state, or Payment Provider outcome MUST NOT be authoritative.

Webhooks MUST authenticate and validate signatures where supported, validate schema and semantics, prevent replay, and process duplicates idempotently before changing state.

## 23. DTO and Mapping Standards

DTOs MUST define transport or application-boundary data and MUST remain separate from Entities, Aggregates, and persistence models. Mapping ownership MUST be explicit and close to the relevant boundary.

Mapping MUST enumerate writable and readable fields, prevent mass assignment, and exclude sensitive information. Reflection-heavy mapping SHOULD NOT replace explicit mapping where ownership, security, or compatibility requires reviewable behavior.

## 24. Persistence Standards

Repository abstractions MUST express domain or application needs without leaking persistence mechanisms. Persistence mappings MUST preserve domain semantics. Database Transactions, optimistic locking, safe queries, pagination, and indexes MUST be applied where required by behavior and measured access patterns.

Queries MUST be parameterized. Implementations MUST avoid unbounded reads and detect material N+1 behavior. Flyway owns versioned migrations. Runtime schema mutation and automatic production schema creation are prohibited. Database triggers MUST NOT contain business logic unless explicitly approved through architecture governance.

## 25. SQL Standards

SQL MUST be parameterized, readable, bounded, and explicit about selected and changed columns. Production code MUST NOT use `SELECT *`. Aliases and joins MUST reveal relationships without obscuring ownership.

Database Transactions and locking MUST be deliberate. Indexes MUST support evidenced access patterns. Migrations MUST be safe, reviewable, auditable, and compatible with supported deployments. Destructive changes and data backfills MUST define backup, validation, rollout, and rollback or recovery behavior.

## 26. Database Transaction Standards

Database Transaction boundaries MUST be owned by the Application Service or another approved orchestration boundary. Database Transactions SHOULD be short. External network calls MUST NOT remain inside long Database Transactions where a reliable alternative exists.

Rollback behavior, retry safety, idempotency, optimistic concurrency, and distributed side effects MUST be explicit. The Outbox Pattern SHOULD be used where reliable post-commit event publication is required.

## 27. Messaging and Events

Domain Events, Integration Events, and Message Envelopes MUST use approved schemas and explicit versioning. Messages MUST carry correlation and causation identifiers where applicable.

Consumers MUST be Idempotent Consumers and handle duplicate delivery, bounded retry, dead-letter behavior, and documented ordering assumptions. Exactly-once delivery MUST NOT be assumed without an explicit infrastructure Contract. The Outbox Pattern MUST be used where the approved reliability design requires it.

## 28. External Integration Standards

External Systems MUST be accessed through outbound Adapters. Calls MUST define timeouts, bounded retries with safe backoff, idempotency, Authentication, error translation, rate-limit behavior, observability, and fail-safe outcomes. Circuit Breakers SHOULD protect materially unstable dependencies where justified.

Provider Contracts MUST be validated. Provider-specific models and decisions MUST NOT leak into domain code. Retry MUST NOT duplicate financial, Inventory, Order, identity, or messaging effects.

## 29. Payment Code Standards

Payment code MUST preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, and Settlement.

- Payment state MUST change only from validated authoritative Payment Provider evidence.
- A Payment Redirect or client-reported success MUST NOT be treated as proof.
- Duplicate-sensitive operations MUST use scoped Idempotency Keys, replay protection, and safe result reuse.
- Webhooks and callbacks MUST be authenticated, signature-validated where supported, deduplicated, and processed safely under retry.
- Duplicate delivery MUST NOT create duplicate financial effects.
- State transitions MUST be explicit, reconcilable, and represented by safe Audit Records.
- Raw CVV MUST NOT be stored or logged; sensitive payment data MUST follow `SECURITY-STANDARDS.md` and PCI scope-minimization requirements.

## 30. Inventory Code Standards

Inventory code MUST use the canonical concepts Inventory, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement.

The Inventory Module MUST own authoritative state. Stock and Stock Reservation changes MUST be atomic where consistency requires it, concurrency-safe, auditable, and protected against overselling. Stock Reservation creation, expiry, release, and finalization MUST be explicit. Duplicate and retried operations MUST be idempotent, and rollback MUST preserve valid Available-to-Sell. Client-provided Inventory state MUST NOT be authoritative.

## 31. Order and Fulfilment Code Standards

Checkout, Order, Shipment, Return, and Refund code MUST enforce only lifecycle transitions established by governing Specifications. The owning Application Service MUST coordinate transitions and consistency with Payment and Inventory.

Client-provided lifecycle state MUST NOT be authoritative. Operations MUST be retry-safe where replay is possible, preserve immutable commercial snapshots where required, and produce appropriate Audit Records.

## 32. Authentication and Authorization Code Standards

Identity, Principal, Authentication, Authorization, Role, Permission, Claims, Scope, Session, Access Token, Refresh Token, and MFA code MUST follow `SECURITY-STANDARDS.md`.

Authorization MUST be enforced at trusted server boundaries with default denial and object-level checks. Privilege changes MUST be authorized and audited. Access Tokens and Refresh Tokens MUST validate issuer, audience, signature, expiry, revocation, and other Claims as applicable to their protocol. Sessions MUST enforce applicable expiry and revocation behavior. Frontend visibility MUST NOT be an Authorization control. Tokens MUST NOT leak through URLs, logs, errors, or insecure storage.

## 33. Security-Sensitive Coding Rules

External input MUST be validated by trusted code. Implementations MUST prevent XSS, CSRF, SSRF, SQL injection, command injection, path traversal, unsafe deserialization, mass assignment, open redirects, and unsafe file handling as applicable.

File uploads MUST validate type, size, content, storage location, authorization, and scanning requirements. Approved cryptographic APIs and protocols MUST be used. Custom cryptography and TLS certificate-verification bypasses are prohibited.

## 34. Concurrency Standards

Shared mutable state MUST be eliminated or synchronized through an explicit design. Atomicity, optimistic locking, race handling, retry safety, idempotency, and duplicate processing MUST be considered for concurrent workflows.

Payment, Inventory, Order, and identity operations MUST preserve invariants under concurrent requests, callbacks, messages, and Jobs. Thread-safety assumptions MUST be documented and tested.

## 35. Immutability and Side Effects

Data SHOULD be immutable unless mutation represents an owned lifecycle. Mutation MUST be explicit, localized, and protected by invariants. Pure functions SHOULD be used for deterministic calculations and transformations.

Side effects MUST be isolated at approved boundaries. Hidden mutation and global mutable state are prohibited.

## 36. Dependency Standards

Every dependency MUST have a justified capability and an assessed license, maintenance, versioning, and security posture. The dependency footprint MUST remain minimal; duplicate libraries for the same purpose SHOULD NOT be introduced.

Versions MUST use repository-owned management. Transitive Risk MUST be reviewed. Vulnerable, abandoned, malicious, or unmaintained dependencies MUST follow `SECURITY-STANDARDS.md`. Generated dependency updates require the same tests and review as manual changes.

## 37. Comments and Documentation in Code

Comments SHOULD explain rationale, constraints, non-obvious business rules, security decisions, architectural workarounds, or verified External System behavior. They MUST remain accurate and MUST NOT contain Secrets.

Comments MUST NOT restate obvious code, preserve obsolete behavior, or substitute for clear naming and structure. Public Contracts and non-obvious operational behavior SHOULD have durable documentation.

## 38. TODO/FIXME Standards

Committed TODO and FIXME markers that defer work, accept Risk, or preserve a temporary workaround beyond the current change MUST reference a tracked issue. They MUST identify enough context for resolution and MUST be removed when resolved.

Security work MUST NOT be deferred through a marker when it blocks a mandatory control. Stale, ownerless, or unbounded markers MUST NOT accumulate.

## 39. Generated Code

Generated files MUST identify their generator and source of truth where practical. Generator ownership and versioning MUST be explicit. Files owned by a generator MUST NOT be edited manually; changes MUST be made in the source or generator and reproduced deterministically.

Generated output remains subject to security, compatibility, review, and testing requirements.

## 40. AI-Generated Code

AI-generated code MUST meet the same quality, terminology, architecture, security, testing, and review requirements as human-authored code. A qualified human MUST review it before acceptance.

Review MUST detect invented APIs or dependencies, mirrored implementation errors, unsafe defaults, Secret exposure, missing failure paths, and silent scope expansion. AI MUST NOT bypass Human Approval Gates or take unreviewed privileged action. Compilation alone MUST NOT be treated as evidence of correctness.

## 41. Testing Requirements for Code Changes

Verification is governed by `TESTING-STANDARDS.md`. Code changes MUST include tests appropriate to behavior and Risk. Defect fixes SHOULD include a Regression Test. Sensitive changes MUST include applicable security tests.

Contributors MUST NOT reduce required test scope, disable tests, weaken assertions, or suppress failures merely to pass a Pipeline.

## 42. Code Review Standards

Reviewers MUST assess correctness, canonical naming, architecture, security, readability, testability, performance Risk, backward compatibility, migrations, observability, and failure behavior. AI-generated code requires the same review plus the checks in section 40.

Review MUST evaluate the complete behavior and affected Contracts, not only line-level style. Repository PR process remains governed by `AGENTS.md`.

## 43. Static Analysis and Formatting

Formatting, linting, compilation, type checking, static analysis, and applicable security analysis MUST run through repository-approved automation. Actionable compiler and analyzer findings MUST be resolved or governed by an approved exception.

Contributors MUST NOT create manual style debates where configured tooling provides the decision. Tool suppressions MUST be narrow, justified, traceable, and time-bounded where Risk remains.

## 44. Performance-Aware Coding

Code SHOULD avoid premature optimization and MUST measure material paths before complex optimization. Implementations MUST consider N+1 access, repeated network calls, unnecessary allocations, oversized payloads, unbounded collections, Cursor Pagination, caching correctness, algorithmic complexity, and database access.

Performance decisions MUST use approved Requirements, SLOs, measured baselines, or capacity evidence rather than invented SLAs.

## 45. Accessibility in Code

Frontend code MUST use semantic HTML, keyboard-operable interactions, visible and managed focus, associated labels, accessible forms, and understandable errors. ARIA MUST be used only when native semantics are insufficient and MUST remain valid.

Custom controls MUST meet the interaction and accessibility behavior of their native equivalent. Visual-only or pointer-only controls are prohibited.

## 46. Backward Compatibility

APIs, Integration Events, Message schemas, database schemas, stored data, provider Contracts, and migrations MUST evolve compatibly with supported consumers and rolling deployments. Additive evolution SHOULD be preferred.

Breaking changes require explicit approval, versioning, migration, consumer coordination, deprecation, and removal plans. Code MUST tolerate the supported mixed-version window.

## 47. Feature Flags

Every Feature Flag MUST have an owner, purpose, safe default, affected Environments, test coverage, rollout plan, and removal condition. Flags MUST fail securely and MUST NOT become permanent forgotten branches.

A Feature Flag MUST NOT bypass Authorization or another mandatory security control unless explicitly governed by an approved Security Exception. Both enabled and disabled behavior MUST be tested where material.

## 48. Forbidden Coding Practices

The following are prohibited:

- committed Secrets, hard-coded production credentials, private keys, or plaintext passwords;
- disabled TLS certificate verification or custom cryptography;
- client-only Authorization or trust in client-provided Price, Discount, Payment state, Role, Permission, or provider success;
- treating a Payment Redirect as payment proof;
- SQL built through untrusted string concatenation;
- direct Entity or Aggregate exposure through APIs;
- swallowed exceptions, empty catch blocks, hidden failures, and global mutable state;
- nested RxJS subscriptions where composition is appropriate and unmanaged subscriptions;
- arbitrary TypeScript `any` or unjustified type assertions;
- disabled tests or weakened checks merely to pass CI;
- destructive production scripts without explicit governance and safeguards;
- bypassing a security control without an approved Security Exception; and
- unreviewed privileged AI-generated actions.

## 49. Coding Compliance Matrix

| Concern | Governing Source | Supporting Source | Evidence / Enforcement |
|---|---|---|---|
| Governance | `AGENTS.md` | This document | Review and Decision Hierarchy compliance |
| Terminology | `GLOSSARY.md` | This document | Naming review and linting |
| Architecture | `ARCHITECTURE.md` | ADRs and this document | Architecture tests and review |
| Security | `SECURITY-STANDARDS.md` | This document | Security analysis, tests, and Audit Records |
| Testing | `TESTING-STANDARDS.md` | This document | Pipeline results and test evidence |
| Frontend | `ARCHITECTURE.md` | Sections 13–17 and 45 | Type checking, linting, tests, and review |
| Backend | `ARCHITECTURE.md` | Sections 18–21 | Compilation, tests, and review |
| Domain | `PRODUCT.md`; `ARCHITECTURE.md` | Sections 20–21 | Domain tests and architecture review |
| API | `ARCHITECTURE.md`; `SECURITY-STANDARDS.md` | Sections 22–23 | Contract Tests and security tests |
| Database | `ARCHITECTURE.md` | Sections 24–26 | Migration and Integration Tests |
| Messaging | `ARCHITECTURE.md` | Section 27 | Contract Tests and consumer tests |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Section 29 | Payment tests and Audit Records |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Section 30 | Invariant and concurrency tests |
| Authorization | `SECURITY-STANDARDS.md` | Section 32 | Server-side Authorization tests |
| Dependencies | `SECURITY-STANDARDS.md` | Section 36 | SCA, inventory, and review |
| AI-generated code | `AGENTS.md`; `SECURITY-STANDARDS.md` | Section 40 | Human review and Pipeline evidence |
| Exceptions | Applicable governing standard | Section 50 | Approval, controls, expiry, and remediation |

### Compliance Interpretation

Evidence MUST demonstrate both implementation and enforcement. Supporting standards and lower-level Specifications MAY strengthen but MUST NOT weaken governing requirements. A Coding Exception MUST NOT override a higher-authority decision or Security Exception requirement.

## 50. Coding Exceptions

A Coding Exception MUST record the exact Requirement, reason, Risk, affected scope, compensating controls, accountable owner, authorized approver, approval date, expiry date, and remediation plan.

Coding Exceptions MUST be explicit, time-bound, auditable, and reviewed before expiry. Expiry MUST restore enforcement or block the affected change. A Coding Exception MUST NOT waive a mandatory security Requirement without an approved Security Exception under `SECURITY-STANDARDS.md`. It waives only the coding Requirements explicitly recorded within its approved scope and MUST NOT silently waive a mandatory Requirement owned by another standard. When multiple standards govern an affected change or gate, every applicable formal exception MUST be obtained.

## 51. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`
- `.ai/core/DECISIONS.md`

## 52. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.2 | 2026-08-12 | Approved | Added directly relevant engineering-principle, documentation, and Decision Record governance references for implementation work. |
| 1.0.1 | 2026-08-12 | Approved | Normalized Database Transaction terminology, aligned the Java 21 LTS baseline, and clarified Coding Exception boundaries. |
| 1.0.0 | 2026-08-11 | Approved | Promoted the repository-wide coding standards after final governance, terminology, architecture, frontend, backend, security, domain, data, integration, testing, and implementation-consistency validation. |
| 0.1.0 | 2026-08-11 | Draft | Established the initial repository-wide coding standards baseline covering implementation quality, architecture alignment, frontend, backend, security, domain code, integrations, data access, AI-generated code, review, and exception governance. |
