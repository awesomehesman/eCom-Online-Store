---
title: SPRING
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: false
review_cycle: Quarterly
---

# Spring Boot Standards

## 1. Purpose

This document refines Spring Boot implementation conventions for the repository. It is a lower-level implementation standard subordinate to `.ai/core/` and does not establish core repository authority.

`ARCHITECTURE.md` owns the backend Architecture, `CODING-STANDARDS.md` owns general coding rules, `SECURITY-STANDARDS.md` owns security, `TESTING-STANDARDS.md` owns verification, and `PRODUCT.md` owns Product behavior. This document governs Spring-specific implementation detail only and MUST NOT weaken, replace, or reinterpret those sources.

## 2. Scope

This standard covers Spring Boot application bootstrap, dependency management, dependency injection, configuration, Spring Profiles and Environments, Controllers, validation, exception handling, Spring Security integration boundaries, Application Services, Adapters, persistence integration, Database Transactions, scheduling and Background Jobs, HTTP clients, observability, health endpoints, testing, and framework-leakage controls.

Java language-level conventions belong in `.ai/backend/JAVA.md`. API, database, PostgreSQL, and event details belong in their respective lower-level companion standards once those documents are Approved.

## 3. Repository Authority

Spring implementation decisions MUST follow the Decision Hierarchy in `AGENTS.md`.

- `GLOSSARY.md` governs canonical terminology.
- `PRODUCT.md` governs Product behavior and business semantics.
- `ARCHITECTURE.md` governs backend structure, domain ownership, technology direction, and trust boundaries.
- `SECURITY-STANDARDS.md` governs mandatory security Requirements and Security Exceptions.
- `TESTING-STANDARDS.md` governs verification and Testing Exceptions.
- `CODING-STANDARDS.md` governs repository-wide implementation quality and Coding Exceptions.
- `ENGINEERING-PRINCIPLES.md` provides decision heuristics within governing constraints.
- `DOCUMENTATION-STANDARDS.md` governs documentation quality and lifecycle.
- `DECISIONS.md` governs durable Decision Records.

This Draft MAY refine Spring-specific implementation detail but MUST NOT silently override a higher-authority Requirement or Accepted Decision Record.

## 4. Exact Spring Boot Release

`ARCHITECTURE.md` establishes Spring Boot 3.x with Java 21 LTS. No current repository build file or dependency-management source establishes an exact Spring Boot minor or patch release.

Before backend implementation begins, Engineering MUST select a currently supported Spring Boot 3.x release compatible with Java 21 LTS. The exact version MUST be recorded in this document or an approved repository-owned dependency-management source. Selection and upgrades MUST be governed changes and MUST pass compatibility testing, dependency and security review, and the applicable Pipeline gates.

No contributor may infer or introduce an exact Spring Boot release from external currency, personal preference, or an ungoverned local build.

## 5. Dependency Management

The build MUST use the Spring Boot parent, BOM, or an equivalent centralized dependency-management mechanism so Spring framework modules remain mutually compatible. Spring Framework versions MUST NOT be pinned independently unless an approved compatibility need is documented and verified.

Third-party versions SHOULD be centralized where practical. Duplicate or conflicting versions are prohibited. Every dependency MUST have a justified capability, owner, compatible license and maintenance posture, and security review under `CODING-STANDARDS.md` and `SECURITY-STANDARDS.md`.

Dependency upgrades MUST run applicable compilation, Unit Tests, Integration Tests, Contract Tests, security scans, and migration checks. This standard does not select Maven or Gradle because the repository does not yet establish either build tool.

## 6. Application Bootstrap

Each deployable Spring Boot application MUST have one clear `@SpringBootApplication` bootstrap. The bootstrap class MUST contain minimal startup logic and MUST NOT contain business rules, data repair, provider behavior, or hidden Environment-specific decisions.

Initialization and configuration MUST be explicit, owned, testable, and safe to rerun where repetition is possible. A missing or invalid critical dependency or configuration value MUST fail startup clearly rather than silently placing the application in an unsafe or misleading state.

## 7. Module Boundaries

Spring component scanning, Bean registration, and dependency injection MUST preserve the Modules, Ports, Adapters, Application Services, Domain Services, Repositories, and Use Cases established by `ARCHITECTURE.md`.

A Bean MUST NOT inject or call another Module's internal Entity, Aggregate, Repository, persistence model, configuration, or provider implementation merely because Spring makes it discoverable. Cross-Module interaction MUST use the owning Module's approved public Contract, Application Service, query, Domain Event, or Integration Event boundary.

Configuration and package scanning SHOULD be narrow enough to make ownership and prohibited dependencies reviewable. Architecture tests MUST protect material Module and layer boundaries.

## 8. Framework Leakage

Domain code SHOULD remain independent of Spring wherever practical. Entities, Value Objects, Aggregates, Domain Services, Domain Events, and core domain rules MUST NOT depend on Spring MVC, Spring Data, Spring Security, configuration, scheduling, or other Spring infrastructure APIs.

Spring annotations MUST NOT be placed on domain objects unless an Accepted ADR and synchronized Architecture update explicitly permit the dependency. Spring-specific concerns belong primarily in application, Adapter, infrastructure, bootstrap, and configuration layers.

Framework convenience MUST NOT become the reason to weaken domain invariants, ownership, testability, or dependency direction.

## 9. Dependency Injection

Spring-managed components MUST use constructor injection. Required dependencies SHOULD be immutable and explicit in the constructor contract.

Field injection, static access to application services or the application context, and Service Locator patterns are prohibited. Setter injection MAY be used only for a genuinely optional dependency whose lifecycle and absence are explicit and tested.

Beans MUST have a clear owner and cohesive responsibility. Conditional or dynamic injection MUST remain understandable and MUST NOT hide circular dependencies or unauthorized cross-Module coupling.

## 10. Component Stereotypes

Use `@Component`, `@Service`, `@Repository`, `@Controller`, `@RestController`, and `@Configuration` deliberately to communicate a Spring-managed technical role.

- `@Controller` and `@RestController` identify inbound web Adapters.
- `@Service` MAY identify a Spring-managed Application Service, Domain Service wrapper, or technical service, but the more specific architectural role MUST remain clear.
- `@Repository` identifies a Spring Repository or persistence Adapter and MUST NOT blur a domain-owned Repository Port or the Code Repository.
- `@Configuration` owns Bean wiring and MUST NOT contain business behavior.
- Generic `@Component` SHOULD be avoided when a more precise role is available.

Stereotypes do not redefine canonical Domain concepts or grant authority across Module boundaries.

## 11. Configuration

Configuration MUST be externalized from code, reviewable, and separated by concern. Typed configuration binding with startup validation SHOULD be preferred over scattered string lookups or direct Environment access.

Required values MUST be validated for presence, format, range, and safe combinations. Defaults MUST be explicit and safe across Environments. A default MUST NOT weaken Authentication, Authorization, transport security, data integrity, or provider validation.

Configuration classes MUST contain configuration behavior only. Business semantics MUST remain in the owning Product, domain, and application sources.

## 12. Profiles and Environments

Spring Profiles MAY vary Environment-specific wiring and configuration. They MUST NOT change Product behavior, domain invariants, Price, Payment, Inventory, Order, or Authorization semantics.

Profiles SHOULD remain few, explicit, and independently testable. Profile combinations MUST NOT create hidden production behavior. Spring Profiles MUST NOT be used as Feature Flags or as a substitute for governed rollout decisions.

Environment naming and isolation MUST follow repository and infrastructure governance. This document does not introduce additional Environment names.

## 13. Secrets

Spring configuration MUST comply with `SECURITY-STANDARDS.md`. Secrets MUST NOT be committed in source, properties, YAML, test fixtures, examples, logs, exception messages, or build artifacts.

Production Secrets MUST come from the Architecture-approved secret-management path, including Azure Key Vault and managed Identity where supported. Access MUST be least-privilege, Environment-scoped, auditable, and compatible with rotation and revocation.

Secret values MUST NOT be exposed through Actuator, configuration dumps, error responses, command-line arguments, or diagnostic logging.

## 14. Controllers

Spring MVC Controllers MUST remain thin inbound Adapters. They MAY perform transport mapping, request validation, applicable Authentication and Principal context extraction, invocation of an approved Use Case or Application Service, and response mapping.

Controllers MUST NOT contain core business rules, persistence logic, provider integration logic, Database Transaction orchestration, or authoritative Price, Discount, Payment, Inventory, Order, Role, or Permission decisions.

Controller methods SHOULD make the invoked Use Case and returned Contract clear. Security annotations at a Controller boundary supplement and do not replace Authorization at the owning Use Case where business context is required.

## 15. Request and Response DTOs

API DTOs are transport models. Persistence Entities, Aggregates, provider models, and internal framework objects MUST NOT be exposed directly through an API.

Request DTOs MUST treat all input as untrusted and MUST allow only governed fields. Response DTOs MUST expose only approved Contract data. Mapping to application or domain inputs MUST be explicit enough to prevent mass assignment and provider or persistence leakage.

Compatibility and versioning MUST follow `ARCHITECTURE.md` and approved API Contracts. Detailed naming, JSON, pagination, and schema conventions remain the responsibility of `.ai/backend/API.md` when Approved.

## 16. Validation

Jakarta Bean Validation SHOULD be used for suitable transport and configuration constraints. Validation MUST cover applicable type, format, range, length, cardinality, and required-field rules at the trusted boundary.

Framework validation does not replace domain invariants. Application and domain code MUST still validate business eligibility, state transitions, ownership, concurrency, and authoritative values.

Validation failures MUST be translated consistently into the repository's RFC 9457 Problem Details model without exposing internal implementation information.

## 17. RFC 9457 Problem Details

Spring exception handling MUST support the RFC 9457 Problem Details baseline established by `ARCHITECTURE.md`.

Exception translation MUST be centralized at the appropriate web boundary and MUST:

- map domain, application, validation, security, conflict, provider, and infrastructure failures intentionally;
- preserve stable machine-readable error codes where a governing Contract establishes them;
- exclude stack traces, SQL details, Secrets, provider internals, and Sensitive Data;
- include or reference a Correlation ID for unexpected failures where useful; and
- normalize relevant framework exceptions into safe external responses.

This standard does not define the final repository API error-code taxonomy.

## 18. Exception Handling

Expected domain and application failures, validation failures, provider or infrastructure failures, and unexpected failures MUST remain distinguishable when their handling differs.

Broad `catch (Exception)` handling is prohibited except at an intentional outer boundary that records safe diagnostics and translates or terminates the failure consistently. Exceptions MUST NOT be swallowed, converted into ambiguous success, or repeatedly logged and rethrown at every layer.

Internal causes and safe diagnostic context SHOULD be preserved. External responses MUST remain stable, minimal, and free of sensitive information.

## 19. Application Services

Spring-managed Application Services orchestrate Use Cases. They MAY coordinate domain objects, Domain Services, Repositories, Ports, Database Transaction boundaries, idempotency, and Authorization or domain policies owned at the application boundary.

Application Services MUST NOT own HTTP transport behavior, persistence technology details, provider-specific models, or presentation concerns. They MUST depend on project-owned Ports rather than concrete outbound Adapters.

Each Application Service SHOULD expose cohesive operations with explicit inputs, outputs, side effects, and failure behavior.

## 20. Database Transaction Management

Spring `@Transactional` is an implementation mechanism for a Database Transaction; it is not the Architecture or a Payment Transaction.

Database Transaction boundaries SHOULD normally be defined at an Application Service or Use Case orchestration boundary. They MUST remain focused, protect required atomic invariants, and avoid remote network or provider calls while open where a reliable alternative exists.

Rollback behavior MUST be understood and tested. Contributors MUST NOT assume checked and unchecked exceptions produce identical rollback behavior. Explicit rollback rules MUST be used where the required outcome differs from the configured default.

`@Transactional` MUST NOT be placed arbitrarily on Controllers, Domain Entities, or methods whose boundary obscures ownership. Read-only transaction hints MAY be used where appropriate but MUST NOT be treated as a correctness or Authorization boundary.

## 21. Transaction Propagation

The simplest default propagation behavior SHOULD be preferred. Advanced propagation such as `REQUIRES_NEW` or `NESTED` MUST have documented consistency, rollback, locking, and failure semantics and focused tests.

Propagation MUST NOT be used to conceal poor Module boundaries, split an invariant incorrectly, or create unreviewed partial success. Self-invocation and proxy behavior MUST be considered when annotation-driven Database Transaction behavior is relied upon.

## 22. Persistence

Spring persistence integration MUST preserve Repository Ports, domain ownership, and Adapter direction. Domain and application interfaces MUST remain independent of the selected persistence technology.

Neither `ARCHITECTURE.md` nor the current repository build selects JPA, Hibernate, or another ORM as mandatory. This standard therefore does not select one. Any selection that materially changes Architecture requires the applicable Decision Record and synchronized governing updates.

Persistence mappings, Spring Repositories, query behavior, locking, and Database Transaction behavior MUST be tested with realistic infrastructure where their semantics matter. Detailed data rules remain governed by `ARCHITECTURE.md` and future Approved `DATABASE.md` and `POSTGRES.md` standards.

## 23. Flyway Integration

Flyway is the Architecture-approved migration mechanism. Schema and governed data changes MUST use version-controlled Flyway migrations through the approved build, deployment, or application integration process.

Application code MUST NOT mutate the schema at runtime or enable automatic production schema creation. A migration failure MUST fail the affected deployment or startup according to the approved delivery strategy rather than silently continuing against an incompatible schema.

Migration ownership MUST align with Module and data ownership. Detailed migration naming, repair, compatibility, rollback, and PostgreSQL conventions remain the responsibility of future Approved data standards.

## 24. HTTP Clients

External HTTP integrations MUST be implemented through outbound Ports and Adapters. Provider request, response, authentication, and error types MUST remain outside domain code.

Every client MUST define explicit connection and request timeouts, Authentication, response validation, correlation propagation, safe error mapping, observability, and rate-limit behavior where applicable. Network errors and uncertain outcomes MUST remain distinguishable from confirmed business outcomes.

The repository does not currently select `RestClient`, `WebClient`, Feign, or another HTTP client. No client library may become a repository-wide authority without applicable governance and evidence.

## 25. Retry

Spring retry mechanisms MAY be used only when the operation and failure class are safe to retry. Retries MUST be bounded, observable, use appropriate backoff, and preserve idempotency and replay safety.

Non-idempotent operations MUST NOT be retried automatically. Retry MUST NOT create duplicate financial, Inventory, Order, Identity, notification, or external communication effects. Payment and Stock-affecting retries require explicit idempotency and reconciliation behavior.

The repository does not currently select Spring Retry, Resilience4j, or another resilience library. This standard does not introduce one.

## 26. Payment Integration

Spring implementation MUST preserve the canonical distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

A Payment Redirect, browser result, client report, or unvalidated Callback or Webhook MUST NOT establish Payment success. Provider-dependent authoritative Payment state requires validated Payment Provider evidence through an authenticated or signature-validated Callback or Webhook where applicable, or a trusted server-side verification path.

Controllers and handlers MUST pass verified inputs to Payment-owned Use Cases. Payment Adapters MUST validate evidence, amounts, Currency, freshness, signatures, and provider references as applicable. Duplicate requests, Callbacks, Webhooks, and retries MUST be idempotent, replay-resistant, reconcilable, and unable to create duplicate financial or Order effects.

Provider-specific Payment rules do not belong in this standard.

## 27. Inventory Integration

Spring implementation MUST preserve Inventory ownership of authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state.

Stock Reservation creation, expiry, release, and finalization MUST preserve domain invariants under concurrency, rollback, duplicate requests, and retries. Implementations MUST prevent overselling and MUST NOT calculate competing authoritative Available-to-Sell state in Controllers, clients, search, reporting, or unrelated Modules.

Spring scheduling, caching, persistence, and retry facilities MUST NOT weaken Inventory authority or idempotency.

## 28. Spring Security Boundary

Spring Security is an implementation mechanism, not the owner of Product Authorization semantics. It MUST establish or consume trusted Authentication and Principal context and enforce server-side Authorization at applicable request, method, Use Case, and object boundaries.

Security configuration MUST deny by default. Role, Permission, Claims, and Scope mapping MUST preserve the canonical model and remain qualified by the actual protocol. Request or method security MUST NOT replace object-level and domain-state Authorization where required.

Frontend visibility and route behavior are not Authorization controls. This standard does not select or define an Identity Provider or final Session and token strategy.

## 29. Security Configuration

Security configuration MUST:

- explicitly identify public Endpoints and avoid broad `permitAll` defaults;
- protect management, diagnostics, documentation, and administrative Endpoints;
- apply approved security headers and transport expectations;
- configure CORS deliberately;
- configure CSRF according to the actual approved Authentication and Session architecture;
- fail closed when required policy or verification material is absent; and
- avoid disabling security controls for local convenience.

Different Authentication models have different CSRF and CORS implications. Configuration MUST be evidence-based and MUST NOT assume one universal token or cookie design.

## 30. CORS

CORS MUST use explicit trusted origins, minimum required methods and headers, and reviewable Environment-specific configuration. A wildcard origin MUST NOT be combined with credentialed requests.

Preflight acceptance does not establish Authentication or Authorization. Production origins MUST come from approved deployment configuration; this document does not invent them.

## 31. Scheduling and Background Jobs

Spring scheduling MAY be used for bounded in-process Background Jobs where `ARCHITECTURE.md` permits that execution model. Every scheduled operation MUST have clear ownership, bounded work, idempotency where repetition is possible, concurrency control, safe failure handling, retry policy, observability, and a recovery path.

Scheduling MUST NOT assume exactly-once execution. In-process scheduling MUST NOT be used to claim durable distributed execution, leader election, or delivery guarantees that the deployment does not provide.

Jobs MUST revalidate stale state and enforce the same Authorization, domain invariants, and Audit Record requirements as equivalent interactive work.

## 32. Async Execution

`@Async` MAY be used only deliberately. The owning component MUST define executor and queue bounds, thread-pool configuration, context and Correlation ID propagation, exception handling, shutdown behavior, and observability.

Async execution MUST NOT assume caller Database Transaction or security context propagation unless explicitly configured and tested. It MUST NOT hide failures or substitute for durable messaging when delivery guarantees, replay, reconciliation, or cross-process behavior are required.

## 33. Events

Spring application events MAY support in-process Domain Event handling consistent with `ARCHITECTURE.md`. A Domain Event and an Integration Event MUST remain distinct.

In-process publication does not provide durable delivery, cross-process delivery, or exactly-once execution. Handlers MUST tolerate duplicate invocation where repetition is possible and MUST make ordering assumptions explicit.

External messaging requires approved Architecture and stable Contracts. The Outbox Pattern or another Accepted durable mechanism MUST be used where the approved reliability design requires reliable post-commit publication. Detailed event conventions remain the responsibility of `.ai/backend/EVENTS.md` when Approved.

## 34. Observability

Spring and Micrometer capabilities MAY provide the structured Logs, Metrics, Traces, Correlation IDs, health information, and readiness signals required by `ARCHITECTURE.md`.

Telemetry MUST identify useful technical and business outcomes without exposing Secrets, tokens, Sensitive Data, raw provider payloads, or unnecessary PII. Metric and Trace attributes MUST remain bounded and operationally useful.

This document does not select an additional external monitoring vendor. Instrumentation MUST remain compatible with the OpenTelemetry and Azure observability direction established by Architecture.

## 35. Actuator

If Spring Boot Actuator is adopted, only required endpoints and information MUST be exposed. Management Endpoints MUST be authenticated and authorized according to Risk and MUST NOT be publicly reachable by default.

Health information SHOULD distinguish liveness from readiness where doing so supports safe operation. Actuator MUST NOT expose Secrets, tokens, full configuration, internal stack traces, or Sensitive Data.

Actuator availability does not replace application-specific Audit Records, business Metrics, or operational runbooks.

## 36. Logging

Spring applications MUST use structured or consistently contextual logging and propagate Correlation IDs through supported boundaries.

Logs MUST NOT contain Secrets, raw credentials, Access Tokens, Refresh Tokens, Session secrets, raw CVV, full payment credentials, unnecessary PII, or full sensitive provider payloads. Error logging MUST preserve safe causes without duplicating the same failure at every layer.

Diagnostic Logs are not Audit Records. Material administrative, security, Payment, Order, and Inventory actions MUST produce the Audit Records required by governing sources.

## 37. Serialization

Serialization MUST use explicit API DTOs and stable Contract fields. Persistence Entities, lazy-loaded associations, provider objects, internal exceptions, and security context objects MUST NOT be serialized accidentally.

Request deserialization MUST reject or ignore fields according to the approved Contract without enabling mass assignment. Polymorphic or dynamic deserialization MUST be bounded to explicit safe types.

Date, time, numeric, naming, and compatibility conventions remain governed by approved API Contracts and `.ai/backend/API.md` when Approved.

## 38. Time Handling

Persisted and system timestamps MUST use UTC unless a governing Product Requirement explicitly requires another representation. Time that affects business behavior SHOULD use an injected, testable clock rather than scattered direct system-clock calls.

Expiration, scheduling, retries, and time-window logic MUST define precision, boundary behavior, and test coverage. This standard does not create Customer locale or Time Zone policy.

## 39. File Upload and Download Handling

When an Approved Product Specification requires file or media handling, Spring boundaries MUST validate size, content-derived type, allowed format, name, storage path, Authorization, and applicable malware or content-scanning Requirements.

Client filenames and declared MIME types MUST NOT be trusted as storage paths or sole validation. Large content SHOULD be streamed with bounded resource use. Path traversal, archive expansion, unsafe redirects, and unauthorized download enumeration MUST be prevented.

This standard does not create file-upload Product scope.

## 40. Caching

Caching MAY be used only when the owning Use Case defines safe staleness, failure, invalidation, and fallback behavior. Each Cache MUST have an owner, Cache Key, value Contract, Time to Live, invalidation strategy, staleness tolerance, and observability.

Cached state MUST NOT become authoritative for Payment, Inventory, Price, Authorization, Order financial status, Refund eligibility, or another concern whose owning Architecture does not assign authority to the Cache.

No cache technology is selected by this document. Redis MAY be used only for an Architecture-approved Use Case.

## 41. Feature Flags

When Feature Flags are used, they MUST follow Product, Architecture, security, testing, and coding governance. Every flag MUST have an owner, purpose, safe default, affected Environments, observability, fallback, test coverage, and removal plan.

Spring Profiles MUST NOT be used as Feature Flags. A Feature Flag MUST NOT bypass Authorization or another mandatory security control without an approved Security Exception.

## 42. Graceful Shutdown

Applications SHOULD support graceful shutdown where the hosting infrastructure supports it. Shutdown behavior MUST protect in-flight requests, Background Jobs, message handling, async work, and Database Transaction integrity according to their approved semantics.

Executors and resources MUST stop within configured bounds and expose incomplete work for retry or reconciliation where required. Graceful shutdown MUST NOT promise zero interruption or hide lost work.

## 43. Startup and Readiness

Required configuration and critical dependencies necessary for safe operation MUST affect startup or readiness appropriately. The application MUST NOT report ready while missing configuration or dependency state makes the advertised capability unsafe.

Optional dependency failure SHOULD degrade only the owned optional capability and MUST NOT unnecessarily make the whole application unavailable. Readiness behavior MUST be explicit, testable, observable, and aligned with deployment routing.

## 44. Testing

Spring-specific testing MUST comply with `TESTING-STANDARDS.md`. Tests SHOULD use the smallest effective Spring context and MUST NOT default every test to `@SpringBootTest`.

- Plain Unit Tests SHOULD verify domain rules and isolated application behavior without Spring startup.
- Focused Spring context or component tests SHOULD verify Bean wiring, configuration, serialization, and framework integration.
- MVC or API slice tests SHOULD verify Controller, validation, security, and Problem Details behavior without unrelated infrastructure.
- Persistence Integration Tests MUST verify actual mapping, constraints, queries, locking, migrations, and Database Transaction semantics where applicable.
- Security tests MUST verify default denial, object-level Authorization, public Endpoint classification, and failure behavior.
- Architecture tests MUST verify Module and framework-leakage rules.

Full application tests are appropriate only when the complete context is the behavior under test.

## 45. Testcontainers

Testcontainers SHOULD be used where realistic infrastructure behavior is important and practical, including PostgreSQL persistence and migration tests under the approved Architecture.

Containerized tests MUST be deterministic, isolated, version-controlled, and compatible with CI. They MUST NOT introduce or imply production adoption of infrastructure that Architecture has not approved.

## 46. Test Configuration

Test-specific configuration MUST be explicit, safe, deterministic, and isolated. Tests MUST NOT depend on production credentials, live provider credentials, production Customer data, mutable shared production-like data, or hidden workstation configuration.

Configuration tests MUST cover missing, invalid, contradictory, and Environment-specific values where those failures affect safe startup or behavior.

## 47. Mocking

Mocks and other Test Doubles MAY isolate Ports and External System boundaries where isolation is the purpose of the test. Core domain behavior MUST NOT be mocked merely to make an Application Service test pass.

Tests SHOULD avoid mocking Spring internals, proxy behavior, serialization, Database Transaction semantics, persistence constraints, or security behavior when those mechanisms are material to the assertion. Realistic Integration Tests SHOULD verify those boundaries.

## 48. Framework Upgrades

Spring Boot upgrades require compatibility assessment, release-note review, Java 21 LTS compatibility, dependency and security review, migration assessment, applicable test-suite execution, and operational review where material.

Minor and patch upgrades MUST NOT silently bypass repository review or Pipeline gates. A major Spring Boot or Spring Framework upgrade changes the Architecture technology baseline and requires Architecture governance and, where material, an ADR with a synchronized Architecture update.

## 49. Deprecated APIs

New code MUST NOT introduce deprecated Spring APIs without explicit, reviewable justification and a removal plan. Existing deprecated usage SHOULD be migrated deliberately with compatibility, behavior, and test evidence.

Automated replacement MUST NOT be accepted without verifying semantic, security, configuration, and operational changes.

## 50. Prohibited Practices

The following are prohibited:

- field injection;
- business logic in Controllers;
- exposing persistence Entities directly;
- unnecessary Spring annotation leakage into domain objects;
- arbitrary `@Transactional` placement;
- provider or network calls inside long Database Transactions without explicit justification;
- broad exception swallowing or ambiguous success;
- disabling security globally for convenience;
- production Secrets in properties or source;
- uncontrolled or unsafe retry;
- hidden async execution;
- relying on exactly-once Spring event or scheduler delivery;
- runtime schema mutation; and
- allowing framework convenience to override Product, Architecture, security, data, or domain correctness.

## 51. Spring-Specific Exceptions

This document does not create a formal Spring Exception type. A deviation from a Spring-specific rule MUST document the exact rule, rationale, Risk, scope, owner, and expiry or remediation when temporary.

The applicable Coding, Architecture, Security, Testing, Documentation, Decision Record, or other governing process MUST be used. A deviation under this document MUST NOT waive a mandatory core Requirement. Every applicable formal Exception remains required, and a mandatory security Requirement may be waived only by an approved Security Exception.

## 52. Compliance Matrix

| Concern | Governing Source | Spring Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `.ai/core/AGENTS.md` | Preserve authority and scope | Decision Hierarchy and diff review |
| Terminology | `.ai/core/GLOSSARY.md` | Use canonical terms | Terminology review |
| Product | `.ai/core/PRODUCT.md` | Implement without inventing behavior | Requirement and acceptance evidence |
| Architecture | `.ai/core/ARCHITECTURE.md` | Preserve Modules, Ports, Adapters, and ownership | Architecture tests and review |
| Security | `.ai/core/SECURITY-STANDARDS.md` | Apply Spring security controls without weakening baseline | Security tests, scans, and review |
| Testing | `.ai/core/TESTING-STANDARDS.md` | Use risk-based focused Spring tests | Pipeline and test evidence |
| Coding | `.ai/core/CODING-STANDARDS.md` | Apply repository implementation quality | Static analysis and code review |
| Documentation | `.ai/core/DOCUMENTATION-STANDARDS.md` | Keep configuration and framework behavior current | Documentation review |
| Decisions | `.ai/core/DECISIONS.md` | Record material technology and Architecture choices | Decision Record or ADR where applicable |
| API | `ARCHITECTURE.md`; approved API Contracts | Implement thin Controllers, DTOs, and Problem Details | Contract and API tests |
| Persistence | `ARCHITECTURE.md`; approved data standards when established | Keep persistence behind Repository Ports | Integration and architecture tests |
| Database Transactions | `ARCHITECTURE.md`; `CODING-STANDARDS.md` | Own focused Use Case boundaries | Rollback and concurrency tests |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Preserve validated provider evidence and idempotency | Payment, replay, and reconciliation tests |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Preserve Stock and Stock Reservation authority | Invariant and concurrency tests |
| Authorization | `SECURITY-STANDARDS.md`; `ARCHITECTURE.md` | Enforce trusted server-side policy | Positive and denial-path tests |
| Events | `ARCHITECTURE.md`; approved event Contracts | Preserve event categories and delivery semantics | Consumer, replay, and Contract Tests |
| Observability | `ARCHITECTURE.md`; `SECURITY-STANDARDS.md` | Emit safe Logs, Metrics, Traces, and health signals | Telemetry and redaction review |
| Exceptions | Applicable governing standard | Link deviations without creating new authority | Approval, controls, expiry, and remediation |

### Compliance Interpretation

`SPRING.md` refines Spring-specific implementation only. It MUST NOT override core authority, create Product behavior, or treat implementation evidence as permission to weaken a governing Requirement.

## 53. Related Documents

Approved governing documents:

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

The following lower-level companion files are currently empty and unapproved; they are reserved for future separation of concerns and MUST NOT be treated as authorities:

- `.ai/backend/JAVA.md`
- `.ai/backend/API.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`
- `.ai/backend/EVENTS.md`

## 54. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-12 | Approved | Promoted the Spring Boot implementation standard after final governance, architecture, framework-boundary, dependency-management, Database Transaction, persistence, API, security, Payment, Inventory, Authorization, observability, testing, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-12 | Draft | Established the initial Spring Boot implementation standard covering framework boundaries, dependency management, configuration, controllers, validation, RFC 9457 errors, Database Transactions, security integration, persistence boundaries, events, observability, testing, upgrades, and exception governance. |

## 55. Quality Requirements

This Draft MUST contain no unresolved completion markers, fabricated technology selections, or Product scope expansion. It MUST use exact canonical terminology from `GLOSSARY.md`, remain subordinate to `.ai/core/`, and distinguish current Architecture decisions from future lower-level standards.

An exact Spring Boot release, build tool, ORM, HTTP client, resilience library, Identity Provider, cache technology, and additional monitoring provider MUST NOT be stated as selected without repository evidence and applicable governance.

## 56. Final Validation

Before approval or implementation reliance, reviewers MUST verify:

1. metadata remains accurate for the document lifecycle;
2. the exact Spring Boot release is evidence-backed or explicitly deferred;
3. Java 21 LTS compatibility and the Spring Boot 3.x Architecture baseline are preserved;
4. domain code remains insulated from unnecessary Spring leakage;
5. Controller, application, domain, and infrastructure boundaries align with Architecture;
6. Database Transaction and RFC 9457 Problem Details terminology is canonical;
7. Payment, Inventory, and server-side Authorization semantics remain authoritative;
8. no ORM, build tool, HTTP client, resilience library, Identity Provider, or cloud provider was invented;
9. no exactly-once behavior is assumed;
10. Flyway remains the canonical migration mechanism;
11. Testcontainers guidance aligns with `TESTING-STANDARDS.md`;
12. no empty lower-level companion is treated as Approved;
13. no new formal Exception type was created; and
14. changes remain limited to `SPRING.md`.
