---
title: TESTING-STANDARDS
version: 1.0.2
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Testing Standards

## 1. Purpose

This document defines the repository-wide minimum testing baseline for software, infrastructure, data changes, integrations, and operational delivery. It establishes the evidence required to demonstrate that behavior is correct, secure, resilient, and ready for release.

## 2. Scope

These standards apply to all production code, test code, configuration, database changes, infrastructure definitions, Pipelines, scripts, APIs, user interfaces, scheduled work, provider integrations, and AI-generated contributions in this repository.

They apply to developers, reviewers, QA engineers, architects, DevOps engineers, automated agents, and any other contributor. Lower-level Specifications, Contracts, and local standards MAY strengthen this baseline but MUST NOT weaken it. Accepted ADRs retain the authority assigned to them by the repository Decision Hierarchy and MUST NOT silently conflict with this baseline.

## 3. Normative Language

The terms MUST, MUST NOT, SHOULD, SHOULD NOT, and MAY are normative:

- **MUST** and **MUST NOT** define mandatory requirements.
- **SHOULD** and **SHOULD NOT** define strong recommendations; deviations require documented rationale.
- **MAY** defines an optional practice.

## 4. Repository Authority

Testing decisions MUST follow the repository decision hierarchy in `AGENTS.md`.

- `GLOSSARY.md` governs canonical terminology.
- `PRODUCT.md` governs product behavior and business semantics.
- `ARCHITECTURE.md` governs domain ownership, trust boundaries, dependency rules, and architectural constraints.
- `SECURITY-STANDARDS.md` governs mandatory security controls and Security Exceptions.
- This document governs the repository-wide minimum testing baseline.

Where a conflict exists, the repository Decision Hierarchy determines authority. An accepted ADR or other higher-authority source that changes this baseline MUST make the conflict explicit and trigger an approved update to the affected source-of-truth documents. A lower-level document or local pattern MUST NOT silently override a mandatory requirement in this document. Temporary waiver of a testing requirement MUST follow section 41; waiver of a security requirement additionally MUST use an approved Security Exception under `SECURITY-STANDARDS.md`.

## 5. Testing Principles

Tests MUST:

- verify externally meaningful behavior and invariants rather than incidental implementation details;
- be deterministic, isolated, reproducible, observable, and maintainable;
- provide confidence proportional to production Risk;
- use the lowest effective test layer while preserving realistic boundary behavior;
- exercise explicit Contracts and realistic trust boundaries;
- make failures actionable through clear assertions and diagnostic evidence; and
- remain fast enough for the Pipeline stage in which they run.

Test design SHOULD favor stable behavior over private structure. A passing suite MUST NOT be treated as proof of correctness when material Risk remains untested.

## 6. Test Strategy and Test Pyramid

Each change MUST use the smallest combination of test layers that provides adequate confidence:

| Layer | Primary purpose |
|---|---|
| Unit Test | Isolated rules, invariants, and transformations |
| Component Test | A cohesive frontend or backend component with controlled boundaries |
| Integration Test | Real collaboration with databases, infrastructure, frameworks, or adapters |
| Contract Test | Compatibility between consumers, providers, messages, Webhooks, and schemas |
| End-to-End Test | Critical user journeys across deployed system boundaries |
| Acceptance Test | Evidence that an Acceptance Criterion is satisfied |
| Performance Test | Measured behavior under representative load and duration |
| Security testing | Verification of security controls and abuse resistance |

Unit Tests SHOULD form the broad base. Integration Tests and Contract Tests MUST cover consequential boundaries. End-to-End Tests MUST be selective and MUST NOT replace lower-level tests by default.

## 7. Risk-Based Testing

Test depth, independence, and Pipeline enforcement MUST increase with Risk. The assessment MUST consider authentication, Authorization, payments, refunds, Inventory, Orders, Price, Discount, customer and PII handling, irreversible actions, public APIs, infrastructure, migrations, concurrency, and external providers.

High-risk changes MUST include negative paths, boundary cases, recovery behavior, authorization failures, and evidence at more than one effective layer where a single layer cannot establish confidence.

## 8. Unit Testing

Unit Tests MUST cover applicable:

- pure business logic, invariants, Value Objects, Entities, and Aggregates;
- Application Services and Domain Services whose collaborators can be controlled without hiding meaningful integration behavior;
- boundary values, invalid state transitions, failure paths, and error mapping; and
- deterministic time, identifiers, randomness, and calculations.

Unit Tests MUST NOT require unnecessary framework startup, network access, shared state, or real external services. Tests SHOULD avoid mocking the unit's own implementation and SHOULD assert outcomes rather than call sequences unless interaction order is itself a Contract.

## 9. Component Testing

Component Tests MUST verify a cohesive component through its public boundary.

Angular component tests SHOULD cover standalone component composition, Signals and computed state, forms and validation, inputs and outputs, user interaction, accessible rendering, loading and error states, and declared dependencies. They MUST NOT depend on private fields or framework internals.

Backend component tests SHOULD cover a Module or cohesive slice with controlled external dependencies, including configuration, serialization, validation, and boundary behavior that Unit Tests cannot establish.

## 10. Backend Testing

Backend tests MUST cover applicable Application Services, Domain Services, Commands, Queries, Validators, Mappers, Controllers, Exception Handlers, Jobs, and Schedulers.

They MUST verify successful and rejected operations, validation, Database Transaction boundaries, Authorization decisions, idempotency, mapping, error responses, retry behavior, and scheduling semantics. Framework-heavy behavior SHOULD be verified with focused Component Tests or Integration Tests rather than simulated in Unit Tests.

## 11. Integration Testing

Integration Tests MUST cover consequential collaboration with:

- PostgreSQL, Repositories, mappings, constraints, and migrations;
- provider Adapters, object storage, messaging, queues, and topics;
- Spring Boot configuration, serialization, Database Transactions, and security boundaries; and
- infrastructure behavior that a Test Double cannot faithfully represent.

Tests SHOULD use real PostgreSQL and required infrastructure through isolated containerized dependencies, including Testcontainers where practical. A Test Double MAY be used for an external provider when the real service is unavailable, costly, unsafe, or nondeterministic, but the provider Contract MUST still be verified independently.

## 12. API Testing

API tests MUST verify request validation, response Contracts, HTTP status codes, Problem Details, authentication, Authorization, object-level access, Idempotency Keys, Cursor Pagination, versioning, CORS policy, rate controls, Resource ownership, negative cases, and backward compatibility where applicable.

Tests MUST demonstrate that malformed, unauthorized, forbidden, missing, conflicting, duplicated, and stale requests fail safely without disclosing sensitive information.

## 13. Contract Testing

Contract Tests MUST verify consumer-provider compatibility for REST APIs, Message Envelopes, Integration Events, Webhooks, and external providers.

They MUST cover schema validation, required and optional fields, semantic meaning, version handling, backward compatibility, error behavior, and representative provider failures. A schema-valid message MUST NOT be assumed semantically valid. Breaking Contract changes MUST be detected before deployment.

## 14. Database Testing

Database tests MUST verify applicable migrations, constraints, unique rules, foreign keys, optimistic concurrency, Database Transaction boundaries, rollback, indexes, persistence mapping, concurrent writes, and integrity rules.

Every migration MUST be tested from the supported prior state and on an empty database when applicable. Tests MUST establish that partial failure cannot leave invalid state. Query and index behavior SHOULD be measured for material access paths without inventing arbitrary performance thresholds.

## 15. Messaging and Event Testing

Tests for Domain Events and Integration Events MUST verify Message Envelopes, serialization, delivery retries, duplicate delivery, Idempotent Consumers, dead-letter behavior, ordering assumptions, correlation and causation identifiers, and the Outbox Pattern where used.

Consumers MUST be tested against replay, partial processing, poison messages, out-of-order delivery where possible, and recovery after failure. Tests MUST NOT assume exactly-once transport delivery unless the infrastructure Contract explicitly guarantees it.

## 16. Payment-Critical Testing

Payment tests MUST use the canonical distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

They MUST verify:

- initiation, pending, success, decline, cancellation, timeout, and unknown outcomes;
- Payment Provider callbacks and Webhooks using validated signatures and Payment Provider evidence;
- that a Payment Redirect, browser return, client-provided success, or client-provided Payment state is never payment proof;
- that Payment state changes occur only after validated authoritative Payment Provider evidence;
- Idempotency Keys, replay protection, duplicate notifications, retries, concurrent requests, and out-of-order evidence;
- partial and full Capture, Void, Refund, Chargeback, and Settlement cases where supported;
- traceability among Payment Attempts, Payment Transactions, Refund Transactions, Orders, Payment Provider references, and Audit Records;
- failure recovery without duplicate financial effects; and
- rejection of client-provided Price, Discount, tax, shipping amount, Payment Provider outcome, or authoritative totals.

Tests MUST confirm that raw CVV is never stored or logged and that hosted or tokenized provider flows minimize PCI scope.

## 17. Inventory Testing

Inventory tests MUST cover Stock, Stock Reservations, Available-to-Sell calculations, Stock Adjustments, Stock Movements, concurrency, overselling prevention, Stock Reservation expiry and release, competing checkout races, and Database Transaction rollback.

Tests MUST verify that Inventory owns authoritative Stock and Available-to-Sell state; that stale Product Catalogue, client, search, reporting, and other projection state is not authoritative; and that duplicate or retried operations do not create or release Stock incorrectly.

## 18. Order and Fulfilment Testing

Tests MUST cover the Checkout-to-Order transition, Order lifecycle, cancellation, partial fulfilment, Shipment, Return, and Refund behavior supported by the product.

Every valid state transition and every material invalid transition MUST be tested. Tests MUST cover partial completion, duplicate commands, dependency failure, compensation, and consistency among Order, Payment, Inventory, Shipment, Return, and Refund state.

## 19. Authentication and Authorization Testing

Tests MUST cover Identities, Principals, Claims, Scope, Roles, Permissions, Sessions, Access Tokens, Refresh Tokens, MFA, expiry, revocation, privilege changes, and object-level isolation where applicable.

They MUST verify default denial, missing and malformed credentials, expired and revoked Sessions and tokens, insufficient Scope or Permissions, privilege escalation attempts, tenant or customer isolation, and enforcement at trusted server boundaries. UI visibility MUST NOT be the only authorization test.

## 20. Security Testing

`SECURITY-STANDARDS.md` governs security-testing controls, evidence, findings, and exceptions. Where applicable, the execution model MUST include:

- SAST and SCA/dependency vulnerability scanning automatically in CI;
- secret scanning automatically in repository and CI workflows;
- IaC scanning in CI when infrastructure definitions change;
- container-image scanning before affected images are promoted;
- Authorization and security regression tests in automated application test suites;
- DAST against an appropriate deployed test or pre-production Environment on the required risk-based cadence; and
- periodic and risk-based penetration testing rather than penetration testing for every pull request.

Major changes to Authentication, Authorization, Payment flows, public APIs, sensitive data processing, trust boundaries, or other high-risk areas SHOULD trigger consideration of a targeted penetration test or security assessment.

This document does not replace the security baseline. Security findings, evidence, blocking rules, and Security Exceptions MUST follow `SECURITY-STANDARDS.md`. Tests SHOULD reference security Requirements without duplicating or redefining them.

## 21. Frontend End-to-End Testing

End-to-End Tests MUST cover a small set of critical user journeys, including applicable authentication, discovery, Cart, Checkout, payment handoff and verified outcome, Order confirmation, and Account operations.

They MUST use stable user-facing selectors, isolated data, controlled accounts, and observable assertions. They MUST NOT depend on test execution order or use arbitrary delays. Lower-level tests MUST cover combinatorial validation and edge cases.

## 22. Accessibility Testing

Accessibility tests MUST cover semantic structure, keyboard operation, focus order and restoration, accessible names and labels, forms, validation errors, status announcements, and applicable contrast requirements.

Automated checks MUST be supplemented by focused manual testing for keyboard and assistive-technology behavior on critical journeys. Passing automated tooling MUST NOT be treated as complete accessibility evidence.

## 23. Performance Testing

Performance Tests SHOULD measure applicable latency, throughput, concurrency, load, stress, soak behavior, database access, APIs, checkout, catalog browsing, search, and background Jobs.

Acceptance thresholds MUST come from an approved Requirement, SLO, capacity model, or measured baseline. Tests MUST NOT invent arbitrary latency, throughput, or resource thresholds. Results MUST record workload, data scale, Environment, duration, dependencies, and observed resource use.

## 24. Resilience and Failure Testing

Tests MUST cover applicable provider timeouts, network failure, safe retries, circuit breaking, database unavailability, duplicate messages, partial completion, degraded operation, recovery, idempotency, and compensation.

They MUST verify bounded failure, preserved invariants, diagnosable outcomes, and safe recovery after dependencies return. Retrying MUST NOT create duplicate financial, Inventory, Order, or identity effects.

## 25. Test Data

Test data MUST be synthetic, deterministic, isolated, minimal, and appropriate to the Environment. Fixtures MUST be explicit, uniquely identifiable where parallelism requires it, repeatable, and cleaned up or safely disposable.

Production customer data, PII, payment credentials, raw CVV, production secrets, and live provider credentials MUST NOT be used in tests. Seed data MUST NOT create hidden test-order dependencies.

## 26. Test Doubles

A Test Double MAY stand in for a dependency when isolation is the purpose of the test. A Stub provides controlled responses; a Mock verifies required interactions. Mocks MUST be limited to external dependencies, consistent with `AGENTS.md`. A lightweight in-memory substitute MAY be used only when its behavior is sufficiently faithful for the assertion.

Tests MUST use real implementations for boundaries whose behavior is material to confidence, including database constraints and Database Transaction semantics. Excessive mocking that merely restates implementation logic MUST be avoided.

## 27. Test Naming and Structure

Test names MUST state the behavior, relevant condition, and expected outcome in readable language. Tests SHOULD use a clear arrange-act-assert structure or an equally clear behavioral form, exercise one coherent behavior, and use nested context only when it improves comprehension.

Names MUST NOT expose private implementation details as Requirements. Shared setup MUST remain understandable and MUST NOT hide the behavior under test.

## 28. Determinism and Flakiness

Tests MUST control time, randomness, identifiers, data, locale, concurrency, and external responses when those inputs affect outcomes. Fixed sleeps and unbounded retries MUST NOT be used as synchronization.

Retries MAY be bounded for an explicitly asynchronous assertion, but MUST have a diagnostic timeout. A flaky test MUST be investigated, owned, and fixed or quarantined under section 36; it MUST NOT be silently ignored.

## 29. Coverage

Coverage metrics are evidence, not a substitute for behavioral confidence. The repository MUST NOT adopt arbitrary percentage targets without an approved, risk-based Requirement.

Critical rules, invariants, negative paths, security boundaries, and failure recovery MUST have meaningful coverage. Mutation testing MAY be used to assess assertion quality. New or changed code SHOULD not reduce relevant confidence, even when aggregate coverage appears unchanged.

## 30. Regression Testing

Every confirmed defect SHOULD first be reproduced by a failing test at the lowest effective layer. The corrective change MUST include a Regression Test unless automation is infeasible and the rationale, manual evidence, owner, and follow-up are recorded.

Regression Tests MUST preserve the product behavior or invariant that failed, not merely the implementation of the fix.

## 31. CI Quality Gates

Pipelines MUST define explicit gates appropriate to their stage:

- pull requests MUST run fast formatting, static analysis, Unit Tests, focused Component Tests, relevant Integration Tests, and required security scans;
- merge or mainline validation MUST run the broader Integration Test and Contract Test suites;
- pre-production and release validation MUST run applicable End-to-End, Acceptance, migration, Performance, resilience, and security suites; and
- reports and diagnostic artifacts MUST be retained long enough for review and audit.

A blocking failure MUST prevent progression. It MUST NOT be bypassed without an approved, time-bound exception under section 41; a security finding additionally requires any Security Exception mandated by `SECURITY-STANDARDS.md`.

## 32. Test Environments

Local, CI, integration, staging, and production-validation Environments MUST have documented purposes and controls. Test Environments SHOULD preserve relevant production parity for runtime versions, configuration shape, database behavior, network controls, and deployment topology.

Secrets MUST be Environment-specific. Provider sandboxes MUST be used where available. Shared Environments MUST isolate accounts and data and prevent cross-test interference. Destructive, uncontrolled, or customer-impacting test execution against production MUST NOT be permitted as test or validation activity.

## 33. Acceptance Testing

Each Acceptance Test MUST connect an approved Requirement and Acceptance Criterion to an externally observable outcome. It SHOULD exercise the applicable Use Case through a stable boundary and MUST avoid asserting private implementation details.

Acceptance evidence MUST record the tested version, Environment, input conditions, outcome, and any limitation.

## 34. Test Traceability

High-risk and release-critical behavior MUST be traceable through:

`Requirement → Acceptance Criterion → Use Case → Test → CI result → release evidence`

Traceability MAY be implemented through identifiers, test metadata, Pipeline reports, or linked artifacts. It MUST be durable enough for review without relying on individual memory.

## 35. Defect Reproduction

A defect report MUST include reproducible steps, Environment and version, input data, observed behavior, expected behavior, frequency, and relevant logs or correlation identifiers. Sensitive information MUST be redacted.

The resulting fix MUST identify the Regression Test or document why automated reproduction is infeasible.

## 36. Test Failure Handling

Blocking failures MUST stop the applicable Pipeline or release. A failing test MAY be quarantined only when it is independently tracked with an owner, Risk assessment, scope, expiry, remediation plan, and preserved visibility.

Suppressions and skips MUST reference an active issue and expiry date. Permanent ignored failures, swallowed exceptions, and unowned flaky tests are prohibited.

## 37. AI-Generated Code and Tests

AI-generated code and tests MUST satisfy the same Requirements, review, testing, security, and evidence standards as human-authored work.

Reviewers MUST check AI-generated tests for mirrored implementation errors, shallow assertions, invented APIs or dependencies, unsafe fixtures, missing negative paths, and authorization or security gaps. AI assistance MUST NOT reduce required test scope or Human Approval Gates.

## 38. Manual Testing

Manual testing SHOULD be used for exploratory behavior, usability, visual quality, accessibility, novel Risk, and scenarios that are not practical to automate. It complements but MUST NOT replace automatable critical regression, security, Contract, payment, or release-gate tests.

Manual evidence MUST record scope, tester, date, version, Environment, results, and defects.

## 39. Release Verification

Before release, the responsible team MUST verify applicable green suites, migration evidence, security gates, Smoke Test coverage, rollback readiness, and production observability.

Known failures MUST be assessed and resolved or covered by an approved exception. Release evidence MUST identify the artifact, commit, Environment, test results, approvers, and limitations.

## 40. Post-Deployment Validation

Each deployment MUST run or verify appropriate health checks and Smoke Tests. Production post-deployment verification MAY include safe health checks, read-only checks, synthetic probes, or specifically designed non-destructive transactions that cannot create harmful Customer, Payment, Inventory, or Order effects.

Destructive, uncontrolled, or customer-impacting test execution against production MUST NOT be authorized merely by labeling it validation or a Smoke Test.

Teams MUST monitor technical and business signals, define rollback or remediation triggers, preserve correlation evidence, and invoke incident response when validation indicates material harm.

## 41. Testing Exceptions

A temporary exception to a mandatory testing requirement MUST be approved before bypass and MUST record:

- the exact Requirement or test gate;
- business and technical reason;
- Risk and affected scope;
- compensating controls and validation evidence;
- accountable owner and authorized approver;
- start date and expiry date; and
- remediation plan and tracking reference.

Exceptions MUST be time-bound, auditable, visible in the relevant Pipeline or release record, and reviewed before renewal. Expiry MUST restore the requirement automatically or block progression. An exception MUST NOT waive a mandatory security control; that requires an approved Security Exception under `SECURITY-STANDARDS.md`.

## 42. Forbidden Testing Practices

The following are prohibited:

- fixed sleeps as synchronization and test-order dependencies;
- production customer data, PII, payment credentials, raw CVV, or production secrets in tests;
- disabling tests or swallowing failures to make a Pipeline pass;
- assertions against private implementation details without a Contract reason;
- excessive mocking that reproduces the implementation;
- silently ignoring flaky tests;
- client-only authorization or security verification;
- treating browser redirects or client-provided payment success as payment proof;
- use of production credentials for automated testing; and
- destructive testing against production data or services without explicit approved operational governance.

## 43. Compliance Matrix

| Concern | Governing document or section | Supporting verification | Required evidence |
|---|---|---|---|
| Requirements | `PRODUCT.md`; approved Specifications | Acceptance Tests | Requirement and Acceptance Criterion traceability |
| Terminology | `GLOSSARY.md` | Review and linting | Canonical term usage |
| Architecture | `ARCHITECTURE.md` | Architecture and Integration Tests | Dependency and boundary results |
| Security | `SECURITY-STANDARDS.md` | Security testing | Scan, review, and exception records |
| Unit behavior | Sections 8 and 10 | Unit Tests | CI results |
| Integration | Section 11 | Integration Tests | Environment and dependency results |
| API | Section 12 | API tests | Contract and negative-path results |
| Contract | Section 13 | Contract Tests | Consumer-provider compatibility results |
| End-to-End | Section 21 | End-to-End Tests | Critical-journey results |
| Payments | Section 16 | Payment-focused tests | Provider-evidence and idempotency results |
| Inventory | Section 17 | Concurrency and state tests | Invariant and race-condition results |
| Authorization | Section 19 | Access-control tests | Positive and denial-path results |
| Performance | Section 23 | Performance Tests | Workload, Environment, and measurements |
| Accessibility | Section 22 | Automated and manual checks | Accessibility results |
| CI | Section 31 | Pipeline gates | Reports and retained artifacts |
| Exceptions | Section 41 | Governance review | Approval, Risk, controls, and expiry |

### Compliance Interpretation

Compliance requires both implementation and evidence. A missing, stale, unauditable, or non-reproducible result does not demonstrate compliance. Where multiple documents govern a concern, the higher-authority requirement applies and lower-level evidence MAY strengthen it but MUST NOT weaken it.

## 44. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/DECISIONS.md`

## 45. Revision History

| Version | Date | Status | Summary |
|---|---|---|---|
| 1.0.2 | 2026-08-12 | Approved | Added directly relevant coding and Decision Record governance references for implementation verification and durable testing decisions. |
| 1.0.1 | 2026-08-12 | Approved | Normalized Stock Reservation, Payment, and Database Transaction terminology and clarified authoritative Inventory test coverage. |
| 1.0.0 | 2026-08-11 | Approved | Promoted the repository-wide testing standards after final governance, terminology, architecture, security, domain, CI, release, and testing-consistency validation. |
| 0.1.0 | 2026-08-11 | Draft | Established the initial repository-wide testing standards baseline covering test strategy, domain verification, integration, security, CI gates, traceability, and release confidence. |
