---
title: ENGINEERING-PRINCIPLES
version: 1.0.1
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Engineering Principles

## 1. Purpose

These principles provide durable heuristics for technical decisions when explicit Requirements and lower-level guidance do not fully determine an answer. Unlike `CODING-STANDARDS.md`, they guide trade-offs rather than prescribe implementation conventions. They remain applicable as frameworks change and apply equally to human and AI-assisted engineering.

## 2. Scope

These principles apply to architecture decisions, application design, frontend and backend work, APIs, data, security, testing, infrastructure, integrations, operations, documentation, and AI-assisted development.

## 3. Repository Authority

Engineering decisions MUST follow the Decision Hierarchy in `AGENTS.md`.

- `AGENTS.md` governs repository process and the Decision Hierarchy.
- `GLOSSARY.md` governs canonical terminology.
- `PRODUCT.md` governs product intent and business semantics.
- `ARCHITECTURE.md` governs approved technical structure and ownership.
- `SECURITY-STANDARDS.md` governs the mandatory security baseline and Security Exceptions.
- `TESTING-STANDARDS.md` governs verification.
- `CODING-STANDARDS.md` governs implementation-level practices.
- `DOCUMENTATION-STANDARDS.md` governs documentation.
- `DECISIONS.md` governs durable Decision Records.
- This document provides durable decision heuristics within those constraints.

These principles MUST NOT override a higher-authority Requirement or an Accepted Decision Record within its assigned authority.

## 4. Principle Priority and Trade-offs

Principles can compete. Within the repository Decision Hierarchy, choices SHOULD generally protect, in order: correctness, security, data integrity, customer and business correctness, architectural integrity, operability, maintainability, simplicity, evidenced performance, and delivery speed.

This is a trade-off heuristic, not a competing Decision Hierarchy. Applicable law, approved Requirements, ADRs, and governing standards always retain their assigned authority.

## 5. Correctness Before Convenience

Invalid behavior MUST NOT be accepted because it is easier to implement. Compilation, a successful happy path, or a passing narrow test is not proof of correctness. Domain invariants and failure behavior matter, with particularly strong protection for financial and Inventory correctness.

## 6. Security by Design

Security MUST be considered from discovery and design through operation. Decisions SHOULD apply least privilege, deny by default, explicit trust boundaries, safe defaults, assume breach, and defense in depth. `SECURITY-STANDARDS.md` governs the detailed controls.

## 7. Business Invariants Are First-Class

Implementation MUST preserve approved business truth. Payment outcomes require authoritative evidence; Inventory cannot oversell; Order transitions must be valid; Price and Discount must come from authoritative owners; and Authorization cannot rely on client presentation. Examples illustrate governing semantics and do not redefine them.

## 8. Canonical Ownership

Every capability and authoritative state MUST have one clear owner. Multiple Modules MUST NOT independently own the same business truth. Cross-domain collaboration MUST use approved public boundaries, Use Cases, Contracts, or Integration Events.

## 9. Explicit Boundaries Over Hidden Coupling

Modules, Ports, Adapters, Contracts, Repositories, public interfaces, shared libraries, and External System boundaries SHOULD be intentional and discoverable. Hidden cross-domain dependencies and provider leakage SHOULD be treated as architectural debt.

## 10. High Cohesion, Low Coupling

Behavior and data that change together SHOULD live together. Unrelated responsibilities SHOULD NOT be grouped for convenience. Broad shared Modules should be resisted, and dependencies should point toward stable abstractions and owned Contracts.

## 11. Simplicity Before Abstraction

Choose the simplest design that is correct, secure, and evolvable. Speculative generalization and premature frameworks SHOULD be avoided. Abstractions should follow demonstrated repetition or variation; temporary duplication MAY be safer than the wrong shared abstraction.

## 12. Explicit Over Implicit

Ownership, lifecycle, state transitions, dependencies, Database Transaction boundaries, errors, retries, idempotency, configuration, and Permissions SHOULD be explicit. Hidden magic SHOULD be avoided when it obscures business, operational, or security behavior.

## 13. Make Invalid States Difficult

Domain invariants SHOULD be protected through Value Objects, typed boundaries, validation, Aggregate encapsulation, explicit state transitions, and database constraints where appropriate. Types improve design but MUST NOT replace runtime validation at trust boundaries.

## 14. Fail Safely

Security failures MUST fail closed, and failures MUST preserve data integrity. Unknown outcomes MUST NOT be converted into success. Retryable and non-retryable failures, partial completion, and customer-safe error behavior SHOULD remain distinguishable and observable.

## 15. Idempotency and Replay Safety

Payment, Order, Inventory, Webhook, and messaging operations MUST consider retries and duplicate delivery. Repeated requests using an Idempotency Key or equivalent identity MUST NOT create unintended duplicate effects.

## 16. Financial Correctness

A Payment Redirect and client-reported Payment success are not authoritative Payment proof. Provider-dependent authoritative Payment state requires validated Payment Provider evidence. Retries MUST NOT duplicate financial effects. Payment Authorization, Capture, Void, Refund Transaction, Chargeback, and Settlement transitions must remain explicit, reconcilable, and supported by appropriate Audit Records.

## 17. Inventory Correctness

Inventory owns authoritative Stock, Stock Reservations, Available-to-Sell, Stock Adjustments, and Stock Movements. Concurrency MUST preserve invariants, and overselling prevention outranks convenience. Client, search, reporting, and other projection state is not authoritative Inventory truth. Stock Reservation creation, expiry, release, and finalization SHOULD be explicit where applicable.

## 18. Data Integrity Before Availability Illusions

Systems MUST NOT report false success to appear available. Uncertain writes must remain uncertain until resolved. Retry and reconciliation MUST preserve truth. Caches and projections MAY be stale, but MUST NOT become authoritative where they do not own the state.

## 19. Database Transactions Are Boundaries of Consistency

Database Transactions SHOULD protect required atomic invariants and remain focused. External side effects require deliberate coordination. Distributed work MUST NOT pretend to be one Database Transaction. The Outbox Pattern SHOULD be used where the approved architecture requires reliable post-commit publication.

## 20. Distributed Systems Assume Failure

Designs SHOULD assume timeouts, latency, retries, duplicates, partial failure, unavailable providers, reordered messages, stale reads, and eventually consistent projections. The network is not inherently reliable.

## 21. Exactly-Once Is Not Assumed

Duplicate delivery MUST be expected. Idempotent Consumers, message identifiers, replay handling, deduplication, and the Outbox Pattern SHOULD be applied where required. Exactly-once delivery MAY be relied upon only when an explicit infrastructure Contract guarantees it.

## 22. APIs Are Contracts

External and internal APIs SHOULD have explicit schemas, validation, compatibility, versioning, stable error behavior, Authorization, idempotency, and understood consumer impact. API convenience MUST NOT bypass domain ownership or trust boundaries.

## 23. Backward Compatibility Is a Feature

APIs, Integration Events, Message schemas, database schemas, stored data, rolling deployments, and External System Contracts SHOULD evolve compatibly. Breaking changes require deliberate approval, versioning, migration, and consumer coordination.

## 24. Evolution Over Big-Bang Change

Prefer incremental migration, additive schemas, compatibility windows, controlled rollout, Feature Flags where appropriate, and tested rollback or recovery. Large irreversible rewrites require exceptional evidence and governance.

## 25. Measure Before Optimizing

Optimization SHOULD follow profiling, telemetry, performance baselines, query plans, and customer impact. Premature optimization should be avoided. Performance MUST NOT justify weakening correctness or security.

## 26. Optimize the Critical Path

When evidence justifies optimization, focus on measured bottlenecks in critical customer journeys, database access, repeated network calls, payload size, algorithmic complexity, and cache behavior.

## 27. Observability Is Part of the Design

Material workflows SHOULD provide useful logs, metrics, traces, correlation, causation, Audit Records, and operational signals. A feature that cannot be diagnosed in production is incomplete where failure has material impact.

## 28. Operability Is a Product Quality

Design SHOULD account for deployment, rollback, recovery, monitoring, support, incidents, configuration, migration, capacity, and failure modes. Operational burden is part of total system quality.

## 29. Automation Over Repeated Manual Process

Formatting, linting, tests, security scanning, builds, migrations, deployment, and verification SHOULD be automated when repeatable. Manual approval remains appropriate where Risk or a Human Approval Gate requires judgment.

## 30. Tests Are Evidence, Not Ceremony

Tests SHOULD verify behavior at the lowest effective layer with depth proportional to Risk. Negative paths, Regression Tests, determinism, and meaningful boundaries matter. Tests that merely mirror implementation provide little confidence. `TESTING-STANDARDS.md` governs verification requirements.

## 31. Production Defects Improve the System

A production defect SHOULD lead to root-cause analysis proportionate to impact, a Regression Test where practical, improved monitoring where useful, systemic design or process correction, and updated documentation when assumptions were wrong.

## 32. Make Change Safe

Changes SHOULD be small and reviewable, preserve compatibility, include appropriate tests and migrations, use controlled rollout, support rollback or recovery, and remain observable. Feature Flags MAY reduce rollout Risk when they have ownership and removal plans.

## 33. Reversibility Matters

When uncertainty is high, prefer decisions that are cheap to reverse. Easily reversible implementation choices need less evidence than expensive architecture changes. Irreversible data, security, and financial consequences require stronger evidence and review.

## 34. Prefer Standards Over One-Off Patterns

Common problems SHOULD use established repository patterns. New patterns require clear justification and evidence of a genuine need. A standard MUST NOT be forced where contexts materially differ.

## 35. Shared Code Must Be Earned

Shared code should exist only when responsibility is genuinely shared, semantics are stable, ownership is clear, and dependency direction remains healthy. Shared dumping grounds are prohibited by design, not repaired through naming.

## 36. External Providers Stay at the Boundary

Domain and application code MUST NOT depend directly on external-provider SDKs, models, protocols, or implementation details. Payment Provider, Identity Provider, and other provider-specific integration behavior SHOULD remain at the appropriate infrastructure or Adapter boundary. Approved infrastructure and platform implementation code MAY use provider SDKs directly when consistent with `ARCHITECTURE.md` and governing security requirements.

## 37. Configuration Is Code-Like

Configuration changes affect behavior and MUST be reviewable, validated, Environment-aware, secure, and observable where material. Production behavior MUST NOT depend on undocumented manual configuration.

## 38. Schema Changes Are Production Changes

Database changes require review, compatibility planning, migration, rollback or recovery, data-integrity protection, and operational preparation. Migrations are production artifacts, not secondary files.

## 39. Secure Defaults

Defaults SHOULD deny unapproved access, avoid public exposure and insecure protocols, avoid destructive behavior and sensitive logging, and require explicit authorization for risky capabilities.

## 40. Minimize Privilege and Blast Radius

Systems SHOULD use narrow Permissions, bounded credentials, Environment isolation, scoped deployments, bounded failure domains, and minimal production access.

## 41. Human Judgment Remains Accountable

AI, automation, linters, generators, and tools MAY recommend or execute bounded work, but authorized human owners remain accountable. Compilation, passing tests, or AI confidence MUST NOT replace engineering judgment.

## 42. AI-Assisted Engineering Principles

AI is a contributor, not an authority. It MUST read governing documents, preserve canonical terminology and architecture, verify APIs and dependencies, protect Secrets and customer data, avoid silent scope expansion, validate assumptions, respect Human Approval Gates, and meet the human-authored quality bar. Generated work requires qualified review.

## 43. Documentation Is Part of Engineering

Important decisions, Contracts, invariants, operational assumptions, and non-obvious constraints SHOULD be documented at the appropriate authoritative level. Documentation SHOULD reference rather than duplicate governing content.

## 44. Decisions Must Be Traceable

Material Decisions SHOULD be traceable from a Requirement through the applicable Decision Record where appropriate, including an ADR for an Architecture Decision, and onward through implementation, tests, review, and release evidence. Trivial implementation choices do not require a Decision Record.

## 45. Technical Debt Must Be Explicit

Technical debt is a conscious trade-off, not neglected quality. Material debt SHOULD record its reason, impact, owner, follow-up, and Risk or priority context. Security debt MUST NOT bypass Security Exception governance.

## 46. Avoid Accidental Complexity

Unnecessary abstractions, premature frameworks, hidden indirection, excessive eventing, unjustified microservices, duplicated sources of truth, and unnecessary state stores SHOULD be removed or avoided. Complexity requires evidence and an owner.

## 47. Prefer Composition Over Inheritance

Composition and explicit capabilities SHOULD be preferred where practical. Inheritance MAY be appropriate when substitutability is real, stable, and understood; it is not categorically prohibited.

## 48. Local Reasoning Matters

Code and Modules SHOULD be understandable without loading unrelated repository areas into working memory. Dependencies, side effects, state ownership, and public behavior should remain discoverable.

## 49. Encapsulation Protects Invariants

Internal state and implementation details SHOULD remain behind intentional interfaces. Mutation MUST NOT be exposed solely for testing or convenience when doing so weakens invariants.

## 50. Consistency Over Personal Preference

When several styles are valid, follow repository precedent, configured tools, and approved patterns. Contributors SHOULD avoid style churn that does not improve correctness or maintainability.

## 51. Exceptions and Principle Deviations

Principles do not bypass governance. A deviation from a SHOULD in this document MAY be accepted with documented rationale. A mandatory Requirement owned by another governing standard requires that standard's formal exception process. A generic rationale or risk acceptance MUST NOT replace an applicable Security Exception, Testing Exception, Coding Exception, or Documentation Exception, and every applicable formal exception remains required. An exception MUST NOT bypass a Human Approval Gate.

## 52. Engineering Decision Checklist

Before finalizing a material decision, ask:

- Is it correct and secure?
- Does the owning domain own the authoritative state?
- Does it preserve canonical terminology and architecture boundaries?
- Is failure explicit, safe, and retry or replay safe?
- Does it preserve Payment and Inventory correctness?
- Is it testable and observable?
- Is it the simplest correct approach?
- Are compatibility, rollout, rollback, and recovery addressed?
- Is any new abstraction supported by real evidence?

This checklist supports decision quality but MUST NOT replace formal review, approval, or exception requirements.

## 53. Principle Compliance Matrix

| Concern | Governing Source | Engineering Principle | Evidence / Decision Signal |
| --- | --- | --- | --- |
| Governance | `AGENTS.md` | Follow assigned authority | Decision Hierarchy review |
| Terminology | `GLOSSARY.md` | Preserve canonical language | Terminology review |
| Product semantics | `PRODUCT.md` | Business invariants are first-class | Requirement and domain evidence |
| Architecture | `ARCHITECTURE.md` | Canonical ownership and explicit boundaries | ADR and architecture review |
| Security | `SECURITY-STANDARDS.md` | Security by design and safe failure | Threat, control, and exception evidence |
| Testing | `TESTING-STANDARDS.md` | Tests are evidence | Risk-based test results |
| Implementation | `CODING-STANDARDS.md` | Clarity and explicit behavior | Code review and static analysis |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Financial correctness | Provider evidence, reconciliation, Audit Records |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Inventory correctness | Invariant and concurrency evidence |
| Authorization | `SECURITY-STANDARDS.md` | Deny by default | Server-side Authorization evidence |
| Data | `ARCHITECTURE.md` | Integrity before availability illusions | Constraints, Database Transactions, recovery evidence |
| Messaging | `ARCHITECTURE.md` | Duplicates and failure are expected | Idempotency and replay evidence |
| External providers | `ARCHITECTURE.md` | Providers stay at the boundary | Adapter and Contract review |
| Observability | `ARCHITECTURE.md` | Diagnosability is designed | Logs, metrics, traces, and alerts |
| Compatibility | `ARCHITECTURE.md` | Evolution over big-bang change | Version and migration plan |
| AI-assisted development | `AGENTS.md`; `SECURITY-STANDARDS.md` | Human accountability | Human review and Pipeline evidence |
| Exceptions | Applicable governing standard | Deviations remain governed | Rationale, approval, controls, and expiry |

### Compliance Interpretation

Engineering Principles guide choices only inside constraints established by governing documents. They MUST NOT be used to override explicit higher-authority Requirements.

## 54. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`

## 55. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.1 | 2026-08-12 | Approved | Normalized Database Transaction terminology and clarified Payment, Inventory, Decision Record, authority, and exception-governance principles. |
| 1.0.0 | 2026-08-11 | Approved | Promoted the repository-wide engineering principles after final governance, terminology, product, architecture, security, testing, implementation, distributed-systems, operational, and decision-quality validation. |
| 0.1.0 | 2026-08-11 | Draft | Established the initial repository-wide engineering principles covering correctness, security, domain ownership, simplicity, distributed systems, financial and Inventory integrity, operability, evolution, AI-assisted engineering, and decision quality. |
