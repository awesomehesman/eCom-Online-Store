---
title: EVENTS
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: false
review_cycle: Quarterly
---

# Eventing Standards

## 1. Purpose

This standard defines lower-level implementation rules for Domain Events, Integration Events, Message Envelopes, delivery semantics, consumers, replay, compatibility, security, testing, and operations. It refines the Approved Architecture without selecting messaging infrastructure or changing Product and Domain ownership.

## 2. Scope

This standard applies to in-process Domain Event handling and to Integration Event publication and consumption when external messaging is approved. It also governs applicable Message Contracts, Outbox Pattern coordination, duplicate handling, retry, dead-letter behavior, observability, and operational evidence.

It does not define Product behavior, Domain lifecycles, API Contracts, provider Webhook Contracts, database implementation, a broker, or an event-sourcing Architecture.

## 3. Repository Authority

This standard is subordinate to the Decision Hierarchy in `.ai/core/AGENTS.md`. Approved Canonical Documents remain the current repository truth within their scopes. `.ai/core/DECISIONS.md` governs material Decisions and ADRs; an Accepted ADR that changes Architecture requires synchronized canonical Architecture updates before the changed baseline is current.

Approved backend standards apply only within their assigned scopes. This document MUST NOT weaken a Security Requirement, invent Product semantics, or reassign authoritative state.

## 4. Normative Language

MUST, MUST NOT, REQUIRED, and PROHIBITED express mandatory Requirements. SHOULD and SHOULD NOT express strong defaults requiring a documented, governed reason to depart from. MAY expresses a permitted choice subject to higher-authority Requirements.

## 5. Event Architecture Baseline

The initial Modular Monolith MAY use in-process Domain Events for appropriate internal reactions. External messaging infrastructure MAY be introduced only when justified by approved reliability, decoupling, scale, or integration needs and governed through Architecture.

Duplicate delivery MUST be expected wherever repetition is possible. Consumers MUST protect business effects, exactly-once delivery MUST NOT be assumed, and the Outbox Pattern remains conditional.

## 6. Domain Events

A Domain Event is an immutable record of a completed business fact within a Bounded Context. It MUST be named in past tense, owned by the producing Domain, and distinct from an Integration Event, Analytics Event, technical event, command, or request.

Domain Events MAY remain internal and in-process. Their existence does not automatically require durable messaging, a Message broker, an Outbox Pattern, or an external Contract.

## 7. Integration Events

An Integration Event is a versioned event published across a Module or system boundary to communicate a completed business fact through a stable external Contract. It MUST remain distinct from the originating Domain Event even when both describe related facts.

Integration Events MUST define producer, intended consumers or consumer class, semantic owner, schema, version, compatibility, delivery assumptions, and applicable security and operational behavior. They do not imply exactly-once delivery.

## 8. Domain Event to Integration Event Translation

An approved Adapter or application boundary SHOULD translate internal Domain facts into Integration Event Contracts. Internal Domain models, persistence Entities, provider payloads, and private lifecycle fields MUST NOT leak automatically into Integration Events.

Translation MUST preserve meaning without giving consumers authority over the producing Domain.

## 9. In-Process Delivery

In-process Domain Event delivery MAY support decoupled reactions within the Modular Monolith. It MUST NOT be represented as durable, cross-process, or exactly-once delivery.

Handlers MUST define failure and ordering behavior, Database Transaction interaction, and whether repetition is possible. An in-process handler MUST NOT perform an unsafe external side effect merely by assuming publication occurs once.

## 10. External Messaging Adoption Boundary

Introducing external cross-process messaging where the Approved Architecture uses in-process eventing, selecting or materially changing the messaging platform or topology, or otherwise changing the approved eventing Architecture requires ADR governance and synchronized Architecture updates. Implementation choices within an already-approved messaging Architecture follow the applicable Decision or implementation governance and do not require an ADR solely because they implement a dispatcher, schema-validation mechanism, destination, handler, consumer, or other messaging component.

This standard does not select a broker, vendor, or cloud messaging service.

## 11. Message Envelope

Every Integration Event MUST use a governed Message Envelope. The envelope MUST provide, as applicable:

- a globally or producer-scope unique Message identifier;
- event type and explicit Contract version;
- occurrence or recording timestamp with defined semantics;
- producer identity;
- Correlation ID;
- causation identifier; and
- payload and applicable trace context.

Optional metadata MUST be Contracted, bounded, safe, and compatible. The envelope MUST NOT contain credentials or establish Authorization by itself.

## 12. Event Naming

Domain Event type names MUST use canonical `PascalCase` past-tense names. Existing canonical names in `GLOSSARY.md`, including `PaymentConfirmed` and `InventoryReserved`, MUST be used exactly.

An event name MUST describe a completed fact, not an imperative command, implementation action, table change, or vague notification. Noncanonical aliases MUST NOT replace glossary-defined event names.

## 13. Event Versioning

Integration Event Contracts MUST carry an explicit version. Version semantics, supported versions, compatibility, and consumer migration MUST be documented without embedding build numbers or release dates as substitutes for Contract governance.

This standard does not impose a version syntax beyond explicit, stable, reviewable identification.

## 14. Message Identity

Every externally delivered Message MUST have a stable identity suitable for duplicate detection, traceability, and operational investigation. Producer retries of the same logical publication SHOULD preserve the applicable Message identity when required by the delivery Contract.

Message identity does not prove Authentication, Authorization, business ownership, or provider truth.

## 15. Correlation and Causation

Message Contracts MUST define Correlation ID and causation semantics where applicable. Correlation connects a business or technical flow; causation links a Message to the prior request, command, event, or operation that caused it.

Identifiers MUST be bounded, propagated safely, and included in observability without becoming authoritative Domain data or security evidence.

## 16. Producer Responsibilities

A producer MUST publish only completed facts it owns, validate the outgoing Contract, assign required envelope metadata, preserve compatibility, prevent prohibited Sensitive Data exposure, and provide operational evidence for publication state.

The producer MUST NOT claim delivery merely because a Domain Event was created or a Database Transaction committed.

## 17. Consumer Responsibilities

A consumer MUST validate the Message Envelope and supported Contract version before processing. It MUST enforce its own Domain invariants, Authorization or trust policy where applicable, duplicate protection, safe failure handling, and observability.

Consumers MUST NOT reinterpret producer-owned semantics, trust unvalidated fields as authority, or mutate another Domain's state directly.

## 18. Idempotent Consumer

Every Integration Event consumer MUST be an Idempotent Consumer. Reprocessing the same logical Message MUST NOT duplicate the intended business effect, financial effect, Stock change, Order transition, notification, or external call.

Deduplication identity, scope, concurrency behavior, stored outcome where applicable, and recovery behavior MUST be explicit. This standard does not mandate one persistence mechanism for every consumer.

## 19. Duplicate Delivery

Duplicate delivery MUST be treated as normal delivery behavior, not an exceptional impossibility. Acknowledgement loss, producer retry, consumer restart, redelivery, replay, and operational recovery may repeat a Message.

Transport acknowledgement alone MUST NOT substitute for protected business-effect idempotency.

## 20. Delivery Semantics

The applicable Contract and infrastructure MUST document the actual delivery guarantee. Exactly-once delivery MUST NOT be assumed unless an explicit infrastructure Contract establishes that guarantee. A transport-level exactly-once or equivalent guarantee does not by itself establish exactly-once business effects; every Integration Event consumer remains an Idempotent Consumer and MUST preserve Domain invariants, concurrency safety, and the duplicate and replay protections required by this standard according to the Contract and Risk.

This standard does not claim at-most-once, at-least-once, or another guarantee for infrastructure that has not been selected.

## 21. Ordering

Ordering MUST NOT be assumed unless the approved Contract and infrastructure define the exact ordering scope and guarantee. Global ordering MUST NOT be inferred from timestamps, identifiers, partitions, queues, or publication sequence.

Consumers MUST handle out-of-order delivery where possible or reject, defer, reconcile, or otherwise recover safely according to the owning workflow.

## 22. Retry Semantics

Retries MUST be bounded, observable, cancellation-aware, and limited to failures safe to repeat. The owning Contract MUST define retryable classes, backoff strategy, terminal handling, and uncertainty behavior without treating stale state as current.

This standard does not set retry counts, intervals, backoff values, jitter, or timeout values. Payment, Inventory, Order, and External System effects MUST NOT be retried blindly.

## 23. Poison Messages and Dead-Letter Handling

Messages that repeatedly fail validation or processing MUST have an explicit terminal handling path appropriate to the approved infrastructure. Dead-letter or quarantine behavior MUST preserve safe evidence, restrict access, support diagnosis and recovery, and avoid automatic infinite cycling.

This standard does not mandate a dead-letter queue or define thresholds. A poisoned Message MUST NOT be discarded silently when loss would violate a Requirement.

## 24. Replay

Replay MUST be authorized, bounded, observable, and scoped by Contract version, consumer, Message identity, time or sequence boundary where applicable, and intended recovery outcome. Replayed Messages MUST pass current safety and idempotency protections.

Replay MUST NOT silently reinterpret historical facts using incompatible current semantics or bypass retention, privacy, Authorization, or Audit Record Requirements.

## 25. Outbox Pattern

The Outbox Pattern MUST be used only when Approved Architecture or reliability design requires atomic coordination between owned database state and durable Message publication. It is not required for every Domain Event or Integration Event.

When required, the owned state change and outbox record MUST share the applicable Database Transaction. Dispatch, acknowledgement, retry, cleanup, and reconciliation remain separate operational concerns.

## 26. Database Transaction Coordination

A Database Transaction commit proves only the applicable database outcome; it does not prove external Message delivery, consumer processing, or provider success. External publication MUST occur through the approved post-commit or durable coordination mechanism.

Database Transaction ownership and persistence mechanics remain governed by `DATABASE.md` and `POSTGRES.md`. This standard does not invent distributed Database Transactions.

## 27. Event Payload Design

Payloads MUST contain the minimum stable data required for consumers to interpret the completed fact without exposing internal models. Fields require documented semantics, ownership, nullability, units, identifiers, and compatibility behavior.

Payloads MUST NOT become new authoritative copies of Product, Price, Payment, Inventory, Order, Identity, or Authorization state unless Architecture explicitly assigns that ownership.

## 28. Snapshots and References

A payload MAY include an immutable business snapshot or a reference when the Contract and consistency needs justify it. Snapshot fields MUST identify their effective meaning and MUST NOT be mistaken for continuously current authority.

References MUST NOT be used as Authorization proof. Consumers that fetch current state MUST tolerate change, absence, and access denial.

## 29. Serialization Neutrality

This standard does not select JSON, Avro, Protobuf, XML, Java serialization, or another serialization format. A selected format requires approved compatibility, security, tooling, size, and operational evidence.

Native Java objects, persistence Entities, and provider models MUST NOT become external Message Contracts by convenience.

## 30. Schema and Contract Compatibility

Integration Event schemas are Contracts and MUST remain synchronized with producers, consumers, tests, documentation, and deployed compatibility commitments. Schema validation MUST occur at appropriate producer, delivery, or consumer boundaries.

This standard does not select a schema registry or vendor. Architecture requires schema validation and compatibility controls to be selected through ADR governance when external messaging is introduced. Implementing those approved controls does not require a separate ADR solely because a schema-validation component exists; a material change to the approved Architecture does.

## 31. Additive Evolution

Compatible additive evolution SHOULD be preferred. Added fields MUST define optionality, defaults or absence behavior, semantic ownership, and consumer tolerance.

An additive field or value is not automatically non-breaking; producers MUST assess supported consumers, strict decoders, exhaustive enums, size, and meaning.

## 32. Breaking Changes

Removing or renaming fields, changing types or meaning, narrowing valid values, changing requiredness, altering envelope or identity semantics, and changing delivery or ordering assumptions are breaking or potentially breaking changes.

Breaking changes require applicable Decision governance, explicit versioning, migration, coordinated rollout, compatibility evidence, recovery, and consumer communication.

## 33. Deprecation

Deprecated event versions MUST be documented with replacement guidance, affected producers and consumers, observability, and governed removal criteria. Removal MUST follow the approved compatibility process.

This standard does not invent a deprecation or compatibility duration.

## 34. Consumer-Driven Compatibility

Consumer expectations SHOULD inform compatibility tests and rollout decisions, particularly at high-Risk boundaries. They MUST NOT permanently prevent legitimate producer evolution or override the producing Domain's semantic ownership.

## 35. Event Time Semantics

Every timestamp field MUST state whether it represents business occurrence, recording, publication, receipt, processing, or another event. Instants MUST use unambiguous offset or UTC representations.

Message order MUST NOT be inferred solely from wall-clock timestamps. Clock skew, retries, replay, and delayed delivery MUST be considered.

## 36. Eventual Consistency

Asynchronous consumers and Projections are eventually consistent unless an approved Contract states otherwise. Specifications and user-facing behavior MUST identify where delay, stale state, pending state, or reconciliation is possible.

Eventual consistency MUST NOT be used to hide missing invariants or misrepresent uncertain Payment, Inventory, or Order state as confirmed.

## 37. Failure Handling

Publication, delivery, validation, processing, acknowledgement, and downstream-effect failures MUST remain distinguishable where recovery differs. Failures MUST be observable and lead to bounded retry, terminal handling, reconciliation, or explicit operational intervention.

Consumers MUST NOT swallow failures, acknowledge before the required protected effect, or convert unknown outcomes into false success.

## 38. Unknown Outcomes

Unknown, pending, delayed, or otherwise uncertain outcomes MUST remain distinct from confirmed success and terminal failure. A timeout or missing acknowledgement is not proof that a producer, broker, consumer, or External System did not act.

Recovery MUST use authoritative evidence and idempotent behavior before repeating an effect.

## 39. Reconciliation

Material workflows with delayed, duplicated, lost, reordered, or externally confirmed outcomes MUST provide reconciliation where required by Product, Architecture, or Risk. Reconciliation MUST compare safe local evidence with the authoritative owner and repair or escalate divergence through governed behavior.

Reconciliation itself MUST be idempotent, bounded, observable, and auditable where applicable.

## 40. Payment Events

Payment events MUST preserve Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key distinctions.

`PaymentConfirmed` MAY be emitted only after validated Payment Provider evidence confirms the required Payment Authorization or Capture. A Payment Redirect, browser result, client report, unvalidated Callback or Webhook, Database Transaction commit, or event publication is not proof of provider-side success.

Duplicates, retries, replay, and reconciliation MUST NOT create duplicate financial or Order effects. Unknown Payment outcomes MUST remain explicit.

## 41. Inventory Events

Inventory owns authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state. `InventoryReserved` MAY be emitted only when Inventory has created the applicable Stock Reservation under its invariants, reducing Available-to-Sell as defined by Inventory rules.

Duplicate or replayed Inventory events MUST NOT create duplicate Stock Reservations, double-adjust Stock, independently calculate Available-to-Sell, or permit Overselling. Consumer Projections remain non-authoritative.

## 42. Order Events

Order events MUST describe completed facts from the canonical Order lifecycle and MUST NOT act as hidden commands or permit arbitrary state transitions. Payment and Inventory dependencies, Authorization, idempotency, and Audit Record Requirements remain enforced by their owners.

This standard does not invent Order states or workflow steps.

## 43. Security and Sensitive Data

Messages, envelopes, destinations, logs, dead-letter storage, replay tools, and operational views MUST comply with `SECURITY-STANDARDS.md`. Sensitive Data and Secrets MUST be minimized, encrypted in transit and at rest through approved mechanisms, access-controlled, retained only as governed, and redacted from diagnostics.

Credentials, Access Tokens, Refresh Tokens, raw CVV, passwords, and private provider evidence MUST NOT be placed in event payloads or ordinary telemetry.

## 44. PII Minimization

Integration Events MUST contain only PII required by the approved consumer Contract. Stable non-sensitive references SHOULD be preferred where they safely satisfy the Use Case.

Purpose, lawful basis, access, retention, deletion, data-subject obligations, and cross-boundary exposure MUST be reviewed where applicable. Replay and dead-letter handling MUST preserve those controls.

## 45. Authentication and Authorization Boundaries

Producers, publishers, consumers, replay operators, and administrative tools MUST use trusted workload identity, least privilege, and Environment-scoped access where supported by approved infrastructure.

Envelope fields, producer names, Message identifiers, Correlation IDs, and payload references do not establish Authentication or Authorization. Consumers MUST enforce applicable object, action, and Domain-state Authorization outside the Message transport.

## 46. Audit Records

Security-sensitive and business-critical event-driven changes MUST create governed Audit Records where required. Message logs, broker metadata, traces, and dead-letter entries are not automatically Audit Records or a complete Audit Trail.

Audit Records MUST remain traceable to applicable Message identity, Correlation ID, Principal or workload, action, and outcome without duplicating prohibited Sensitive Data.

## 47. Observability

Event telemetry SHOULD cover publication attempts and outcomes, delivery and processing outcomes, duration, retries, duplicate detection, consumer lag or backlog where supported, dead-letter or quarantine state, replay, reconciliation, and dependency failures.

Metrics MUST use bounded dimensions such as Contracted event type and version; raw payloads, Customer identifiers, Message identifiers, and other high-cardinality or Sensitive Data MUST NOT become unbounded Metric labels.

## 48. Trace Context

Supported trace context, Correlation ID, and causation identifiers SHOULD propagate across event boundaries. Trace propagation MUST use approved formats and MUST NOT create a vendor-specific Contract in the business payload.

Tracing is diagnostic evidence, not proof of delivery, processing, provider success, or business authority.

## 49. Testing

Tests MUST cover Domain Event and Integration Event creation, Message Envelopes, schema validation, serialization when selected, delivery failure, retry, duplicate delivery, Idempotent Consumers, ordering assumptions, dead-letter behavior, replay, correlation, causation, and the Outbox Pattern where used.

Tests MUST use realistic boundaries when transport, Database Transaction, concurrency, or serialization behavior is material.

## 50. Contract Testing

Contract Tests MUST verify producer and consumer compatibility for supported Integration Event versions, required and optional fields, envelope semantics, enum evolution, unknown fields, invalid Messages, and deprecation where applicable.

Tests MUST prevent internal model or provider payload leakage and MUST remain synchronized with Contract documentation.

## 51. Replay and Idempotency Testing

Consumers MUST be tested with the same Message delivered sequentially and concurrently, after partial failure, after restart, and through governed replay where applicable. Tests MUST prove one intended protected business effect rather than only one handler invocation.

Incompatible or malformed replay MUST fail safely without corrupting deduplication state.

## 52. Concurrency and Ordering Testing

Tests MUST cover concurrent duplicates, out-of-order Messages, stale versions, competing state changes, acknowledgement loss, and redelivery when those conditions can affect correctness.

No test may rely on an ordering guarantee or exactly-once behavior absent from the approved infrastructure Contract.

## 53. Payment and Inventory Event Testing

Payment tests MUST prove that `PaymentConfirmed` requires validated Payment Provider evidence and that forged, duplicated, delayed, reordered, replayed, or uncertain Messages cannot create duplicate financial or Order effects.

Inventory tests MUST prove that `InventoryReserved` preserves Inventory authority, Stock Reservation and Available-to-Sell correctness, and duplicate-effect prevention under concurrency and replay.

## 54. Performance and Resource Testing

Tests MUST verify governed throughput, payload, backlog, replay, recovery, and resource Requirements where applicable. Large permitted Messages, bursts, slow consumers, and failure accumulation SHOULD degrade safely.

This standard does not invent latency, throughput, payload-size, backlog, or capacity thresholds.

## 55. Security Testing

Security tests MUST cover unauthorized production and consumption, forged or malformed envelopes, schema abuse, injection, Sensitive Data leakage, replay abuse, cross-consumer isolation, dead-letter access, administrative replay, and least-privilege boundaries where applicable.

Tests MUST NOT depend only on broker configuration when application or Domain enforcement is required.

## 56. Operational Readiness

Before external messaging is released, owners MUST document topology ownership, supported Contracts, delivery semantics, retry and terminal handling, monitoring, alerting, replay, reconciliation, deployment compatibility, incident response, capacity, backup or recovery where applicable, and access procedures.

Values and infrastructure choices MUST come from approved operational and Architecture evidence, not this standard.

## 57. Decision and ADR Governance

Introducing external messaging or materially changing the approved messaging platform, topology, cross-Module communication model, delivery Architecture, or another Architecture-significant mechanism requires an ADR and synchronized canonical Architecture updates.

Ordinary event fields, handlers, consumers, destinations, schema-validation components, dispatchers, and implementation work within approved Architecture and Contracts do not automatically require an ADR. Material non-Architecture Contract or business Decisions follow `.ai/core/DECISIONS.md`; a generic Decision Record MUST NOT override Architecture.

## 58. Exception Governance

Runtime event-processing exceptions are not repository governance Exceptions. Deviations use the applicable Security Exception, Testing Exception, Coding Exception, Documentation Exception, Decision Record, or ADR according to the Requirement and owner.

This standard creates no `Event Exception`. No lower-level deviation or Decision Record may waive a mandatory Security Requirement without an approved Security Exception.

## 59. Prohibited Practices

The following are prohibited:

- treating a Domain Event and Integration Event as interchangeable;
- using an event as a hidden command;
- assuming exactly-once delivery or global ordering;
- requiring the Outbox Pattern for every event;
- publishing provider, persistence, or Domain internals as Contracts;
- treating event payloads or Projections as new authoritative state without Architecture ownership;
- emitting `PaymentConfirmed` without validated Payment Provider evidence;
- replacing canonical event names with obsolete or local aliases;
- allowing duplicate Messages to duplicate Payment, Inventory, Order, or other harmful effects;
- treating a Database Transaction commit as external delivery;
- silently dropping material poison Messages or swallowing consumer failures;
- unbounded retry, replay, payload, backlog, or dead-letter accumulation;
- placing Secrets, credentials, tokens, raw CVV, or unnecessary Sensitive Data in Messages or telemetry;
- inventing transport guarantees, versions, retention, thresholds, or Product lifecycle facts; and
- introducing event sourcing, a broker, schema registry, or serialization format without governance.

## 60. Compliance Matrix

| Concern | Governing Source | Eventing Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `.ai/core/AGENTS.md`; `.ai/core/DECISIONS.md` | Remain subordinate and route material Decisions | Authority review and ADRs |
| Terminology | `.ai/core/GLOSSARY.md` | Use canonical event and Domain terms | Terminology scan |
| Product | `.ai/core/PRODUCT.md` | Publish only approved business facts | Requirement traceability |
| Architecture | `.ai/core/ARCHITECTURE.md` | Preserve initial in-process direction and adoption boundary | Architecture review |
| Security | `.ai/core/SECURITY-STANDARDS.md` | Protect identities, access, payloads, replay, and Sensitive Data | Security tests and review |
| Testing | `.ai/core/TESTING-STANDARDS.md` | Prove Contracts, duplicates, replay, ordering, and recovery | Test results |
| Coding | `.ai/core/CODING-STANDARDS.md` | Implement explicit schemas and Idempotent Consumers | Code review |
| Spring | `.ai/backend/SPRING.md` | Preserve in-process and framework boundaries | Spring tests |
| Java | `.ai/backend/JAVA.md` | Preserve immutable, framework-independent Domain facts | Java tests |
| Database | `.ai/backend/DATABASE.md` | Own Database Transaction and Outbox persistence coordination | Integrity and failure tests |
| PostgreSQL | `.ai/backend/POSTGRES.md` | Implement approved outbox and deduplication mechanics only | PostgreSQL Integration Tests |
| API | `.ai/backend/API.md` | Keep HTTP, Callback, and Webhook Contracts distinct | Contract review |
| Message Envelope | `.ai/core/GLOSSARY.md`; event Contract | Carry stable identity, version, time, correlation, causation, and producer | Contract tests |
| Delivery | `.ai/core/ARCHITECTURE.md` | Expect duplicates and avoid unsupported guarantees | Duplicate and failure tests |
| Outbox Pattern | `.ai/core/ARCHITECTURE.md`; `.ai/backend/DATABASE.md` | Use only when approved coordination requires it | Database Transaction and dispatch tests |
| Payment | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Protect Payment Provider evidence and financial effects | Provider, replay, and reconciliation tests |
| Inventory | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Preserve Stock Reservation and Available-to-Sell authority | Concurrency and invariant tests |
| Observability | `.ai/core/ARCHITECTURE.md` | Emit bounded publication, delivery, processing, and recovery telemetry | Telemetry review |
| Exceptions | Applicable governing source | Use only the Exception owned by the Requirement | Approved exception evidence |

## 61. Related Documents

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

## 62. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-12 | Approved | Promoted the eventing implementation standard after final governance, Domain Event, Integration Event, Message Envelope, delivery, idempotency, replay, Outbox Pattern, Payment, Inventory, compatibility, security, testing, observability, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-12 | Draft | Established the initial eventing implementation standard covering Domain Events, Integration Events, Message Envelopes, delivery semantics, idempotency, replay, Outbox Pattern, Contract evolution, Payment, Inventory, security, testing, observability, and governance. |

## 63. Quality Requirements

This standard MUST remain subordinate to Approved core and backend standards, preserve Domain ownership, use canonical terminology, and keep Integration Event Contracts synchronized with producers, consumers, tests, documentation, and operations.

It MUST NOT manufacture Product facts, provider evidence, authoritative state, transport guarantees, infrastructure choices, or operational values.

## 64. Final Validation

Before this document or a governed revision is presented for approval, reviewers MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. Domain Events and Integration Events remain distinct;
3. in-process Domain Events do not imply durable messaging;
4. external messaging adoption requires Architecture governance;
5. no broker, vendor, destination naming convention, schema registry, or serialization format is selected;
6. no retry, backoff, retention, dead-letter, partition, ordering, payload, performance, or recovery value is invented;
7. duplicate delivery is expected and consumers protect business effects;
8. exactly-once delivery is not assumed;
9. the Outbox Pattern remains conditional and shares the required Database Transaction only where approved;
10. database commit and event publication are not provider success or external delivery proof;
11. `PaymentConfirmed` requires validated Payment Provider evidence;
12. `InventoryReserved` preserves Stock Reservation and Available-to-Sell authority;
13. canonical event names are not replaced by obsolete or local aliases;
14. no event store or event-sourcing Architecture is selected;
15. security, Sensitive Data, Authentication, Authorization, Audit Record, and replay controls remain intact;
16. Integration Event Contracts, producers, consumers, tests, documentation, and operations remain synchronized;
17. there are no placeholders, empty sections, malformed tables, or broken paths; and
18. the final diff contains only intended event-standard changes.
