---
title: API
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: false
review_cycle: Quarterly
---

# API Standards

## 1. Purpose

This standard defines repository-wide implementation rules for HTTP APIs. It turns the Approved Product, Architecture, Security, Testing, Coding, documentation, and backend standards into consistent, secure, testable API behavior without creating Product semantics or changing domain ownership.

## 2. Scope

This standard covers REST resource design, HTTP semantics, JSON Contracts, RFC 9457 Problem Details, validation, Authentication, Authorization, idempotency, pagination, compatibility, Payment and Inventory safety, OpenAPI, testing, security, and observability.

It does not own Product Requirements, Architecture, Spring or Java implementation details, persistence design, Integration Event schemas, provider Contracts, or individual Endpoint Contracts.

## 3. Repository Authority

This standard does not establish a precedence model; `.ai/core/AGENTS.md` governs the Decision Hierarchy. Approved Canonical Documents remain the current repository truth within their scopes, while `.ai/core/DECISIONS.md` governs ADRs for material Architecture Decisions. An Accepted ADR that changes Architecture requires the applicable canonical Architecture source to be synchronized before the changed baseline is treated as current. Approved backend standards apply only within their subordinate assigned scopes.

This document MUST NOT weaken a mandatory Security Requirement, invent Product behavior, reassign authoritative data, or override Architecture. A reserved or placeholder file, including `.ai/backend/EVENTS.md`, contributes rules only when its metadata and substantive content make it applicable within that scope.

## 4. Normative Language

The terms MUST, MUST NOT, REQUIRED, and PROHIBITED express mandatory Requirements. SHOULD and SHOULD NOT express strong defaults that require a documented, governed reason to depart from. MAY expresses a permitted choice whose consequences remain subject to higher-authority Requirements.

## 5. API Baseline

The platform uses REST-first JSON APIs over HTTPS under `/api/v1`, documented with OpenAPI 3.1. APIs MUST use RFC 9457 Problem Details, propagate Correlation IDs, enforce server-side Authentication and Authorization except for explicitly public Endpoints, and support idempotency for duplicate-sensitive commands.

This standard does not select GraphQL, gRPC, an API gateway, or a gateway vendor. A material alternative to the Approved API Architecture requires an ADR and synchronized governing updates.

## 6. API Versioning

Externally consumed HTTP APIs MUST use the Architecture-approved `/api/v1` base path. A different versioning mechanism or new major API version requires Architecture governance and an ADR.

Compatible evolution SHOULD be additive. Breaking and potentially breaking changes MUST be classified, reviewed, documented, reflected in OpenAPI and consumer Contracts, and released through the governed compatibility process. Version identifiers MUST NOT encode build numbers or imply an unapproved roadmap.

## 7. Resource Design

API paths MUST use resource-oriented, `kebab-case` nouns and expose capabilities owned by the applicable Domain. Resource identity, hierarchy, parent-child relationships, subresources, and state-changing actions MUST be explicit in the Contract.

An API MUST NOT expose every persistence operation as CRUD by default. An action-style Endpoint MAY represent a meaningful command when ordinary resource semantics would obscure the Use Case or permit invalid lifecycle mutation.

## 8. HTTP Methods

- `GET` retrieves a representation and MUST be safe.
- `POST` creates a Resource or invokes a Contracted command and requires idempotency where duplicate effects are harmful.
- `PUT` replaces the Contracted representation and MUST preserve its idempotent HTTP semantics.
- `PATCH` applies a Contracted partial change and MUST define omitted, null, and mutable-field behavior.
- `DELETE` requests Contracted removal and MUST preserve idempotent HTTP effect semantics: repeating the same `DELETE` for the intended Resource or operation MUST preserve the same intended external effect and MUST NOT create additional harmful side effects, although the exact response status or representation MAY differ according to the Contract and current Resource state. `DELETE` does not guarantee physical deletion; Soft Delete, lifecycle closure, anonymization, archival, or another governed removal MAY apply according to Product and Domain Requirements. It MUST NOT bypass lifecycle, retention, Authorization, audit, Product, or Domain rules.

Method choice MUST reflect observable semantics rather than Controller convenience.

## 9. HTTP Status Codes

APIs MUST select status codes consistently with the Contract and HTTP semantics. Applicable success responses include `200`, `201`, `202`, and `204`. Applicable client and security responses include `400`, `401`, `403`, `404`, `409`, `412`, `415`, `422`, and `429`. Applicable server and dependency responses include `500`, `502`, `503`, and `504`.

`422` is an optional Contract-level choice, not a mandatory repository mapping. An Endpoint MUST document its responses and MUST NOT invent inconsistent meanings for status codes.

## 10. RFC 9457 Problem Details

Error responses MUST use RFC 9457 Problem Details with `application/problem+json`. The standard members `type`, `title`, `status`, `detail`, and `instance` MUST be used according to RFC 9457 when applicable. Repository or Contract extensions, including stable machine-readable error codes and field validation details, MUST be governed and backward compatible.

Problem Details MUST NOT expose stack traces, exception class names, SQL or SQLSTATE details, provider internals, Secrets, credentials, tokens, or Sensitive Data. This document does not define a complete error-code taxonomy.

## 11. Error Mapping

The API boundary MUST intentionally translate domain, application, validation, Authentication, Authorization, concurrency, persistence, provider, and unexpected failures into safe external responses. Java types, PostgreSQL details, and provider payloads MUST remain internal.

Unknown or uncertain Payment outcomes MUST remain distinguishable and reconcilable; they MUST NOT be represented as success or as a fabricated terminal failure. Each failure SHOULD be translated once at the boundary that owns the external response.

## 12. Request and Response DTOs

API DTOs are explicit transport models. Persistence Entities, Aggregates, provider models, framework objects, and internal Domain models MUST NOT be serialized directly as public Contracts.

Request and response DTOs MAY differ. Mapping MUST be explicit enough to prevent mass assignment, leakage, and accidental coupling. Requests MUST accept only governed fields and be validated before invoking the applicable Use Case. This standard does not select a mapping library.

## 13. JSON Conventions

JSON property names, nullability, required fields, enum representations, numeric formats, and date/time formats MUST be explicit in OpenAPI and synchronized Contracts. The same concept MUST be represented consistently within a Contract.

No repository-wide camel-case, snake-case, or other JSON property-naming convention is selected here. A convention MUST NOT be inferred from Java or database names.

## 14. Money and Currency

Money MUST be represented as an exact decimal amount together with its ISO 4217 Currency. Floating-point representations MUST NOT be used for monetary values.

The Contract MUST make amount and Currency semantics explicit. This standard does not invent scale, precision, rounding, conversion, or display rules. Client-provided Price, Discount, tax, shipping amount, totals, or calculated savings are untrusted inputs.

## 15. Time and Date

Instants exchanged by APIs MUST be unambiguous and use a Contracted RFC 3339 or compatible ISO 8601 representation with an offset or UTC marker. Local dates and local times MUST remain distinct from instants.

The owning Product or Domain Contract defines business timezone, locale, precision, and calendar semantics. This standard does not select a Customer timezone or reinterpret database timestamps.

## 16. Identifiers

Identifiers MUST have stable Contracted representations and MUST be treated as untrusted lookup candidates. An identifier does not prove Resource ownership, Authentication, or Authorization.

API identity MUST not expose sensitive internal structure unnecessarily. This standard does not mandate UUIDs or another universal identifier strategy. A customer-facing Order Number remains distinct from an internal primary key.

## 17. Pagination

Collection Endpoints that can grow materially MUST provide bounded pagination. Offset/page and cursor/keyset approaches are permitted when the Contract defines request parameters, deterministic ordering, continuation behavior, boundaries, and response metadata.

The choice MUST follow Use Case, consistency, and performance evidence. This standard does not set page sizes, maximums, or a universal pagination model.

## 18. Filtering

Filtering MUST use an explicit allowlist of Contracted fields and operators, bounded input, validation, and Authorization-aware query behavior. Filter semantics and combinations MUST be documented.

APIs MUST NOT accept raw SQL, persistence expressions, arbitrary property paths, or an unrestricted query language.

## 19. Sorting

Sorting MUST use explicit, Contracted fields and deterministic tie-breaking where order affects pagination or client behavior. Client-visible sort keys MUST NOT expose database column names or permit arbitrary expressions.

## 20. Search

Search responses are projections and MUST NOT become authoritative for Product, Price, Inventory, Payment, Order, Identity, or Authorization state. PostgreSQL remains the initial authoritative transactional database, but this standard does not select a search engine or search implementation.

Search fields, matching, ranking, pagination, visibility, freshness, and fallback behavior require an approved Contract and applicable Product governance.

## 21. Field Selection and Expansion

Sparse field selection or related-resource expansion MAY be provided only when Contracted, bounded, Authorization-aware, and tested. It MUST prevent Sensitive Data leakage, unbounded traversal, hidden N+1 behavior, and bypass of Resource ownership.

This capability MUST NOT recreate unrestricted GraphQL-style query flexibility.

## 22. Idempotency

Duplicate-sensitive commands MUST implement Idempotency Key semantics. The API Contract defines observable behavior and MUST specify the key's scope to the applicable Principal, client, Resource, or Use Case, bind it to a stable request compatibility fingerprint, and define safe return or reproduction of the compatible prior result.

The owning Application Service or Use Case owns business idempotency. Architecture and `.ai/backend/DATABASE.md` govern atomic persistence coordination. Where correctness requires persistence-based deduplication, the deduplication state and protected database effect MUST share the required Database Transaction or consistency boundary; this does not require a database-backed implementation for every Idempotency Key.

Concurrent duplicate requests MUST converge on one protected business effect. Reuse with an incompatible request MUST produce a safe conflict. Expiry and retention are governed operational and Domain decisions; this standard does not invent durations. Idempotency is not required indiscriminately for every `POST`.

## 23. Conditional Requests

ETags and HTTP preconditions MAY be used where the Contract defines representation identity, validator strength, cache interaction, and failure semantics. `If-Match` and related headers MUST NOT be treated as Authentication or Authorization evidence.

Conditional requests are not a mandatory global concurrency mechanism.

## 24. Optimistic Concurrency

Concurrency-sensitive mutations MUST detect stale state where lost updates or invalid transitions are possible. A Contracted version or precondition MAY be used, but persistence versions and lock details MUST not leak accidentally.

Conflicts MUST be recoverable where safe and MUST NOT silently overwrite authoritative state.

## 25. Authentication

Every Endpoint MUST require trusted Authentication unless it is explicitly classified and reviewed as public. Authentication establishes a Principal; identifiers, client flags, UI state, and request fields do not.

Issuer, audience, Claims, Scope, Session, Access Token, Refresh Token, revocation, and token semantics MUST be validated as applicable to the approved protocol. This standard does not select an Identity Provider or token/session architecture, and tokens MUST NOT be logged.

## 26. Authorization

Authorization MUST be enforced server-side at applicable route, Use Case, object, property, and Domain-state boundaries. Role, Permission, Claims, and Scope MUST retain their canonical meanings and be evaluated only as applicable to the approved policy and protocol.

Collection results MUST be filtered according to the Principal's authority. UI visibility, guessed identifiers, client-provided Role or Permission, and possession of an identifier MUST NOT grant access.

## 27. Public Endpoints

Public Endpoints MUST be explicitly designated, minimal, validated, monitored, and protected against foreseeable abuse. Responses MUST exclude non-public, security-sensitive, and Customer-specific data and use safe caching rules.

This standard does not invent which Product capabilities are public.

## 28. Rate Limiting

Rate and abuse controls MUST be applied where Risk, provider constraints, or Security Requirements require them. Limits MAY be enforced at gateway, infrastructure, application, or provider boundaries according to Architecture, while preserving consistent client behavior and observability.

Applicable rejection uses `429`. This standard does not select values, algorithms, products, or vendors.

## 29. CORS

CORS MUST use explicit trusted origins, the minimum required methods and headers, and reviewable Environment-specific configuration. Wildcard origins MUST NOT be combined with credentialed requests.

Production origins are deployment configuration. CORS is not Authentication or Authorization.

## 30. CSRF

CSRF protection MUST follow the approved Authentication, Session, cookie, and token architecture. It MUST NOT be disabled merely because the application uses Spring or exposes JSON.

The selected control and any exclusions MUST be evidence-based, documented, and tested.

## 31. Content Types

API request and response media types MUST be explicit. JSON responses use `application/json`; RFC 9457 errors use `application/problem+json`. Unsupported request media types SHOULD produce `415` where applicable.

XML or another representation MUST NOT be introduced without an approved Contract and justified consumer need.

## 32. Request Size and Resource Bounds

Requests, collections, nested objects, strings, and computationally expensive inputs MUST have governed bounds appropriate to Risk and Use Case. Oversized input MUST fail safely before causing avoidable memory, storage, or dependency pressure.

This standard does not invent numeric limits.

## 33. File Uploads

File upload Endpoints MAY exist only for an approved Product Requirement. They MUST enforce Authorization, bounded size, content-derived type validation, allowed formats, safe naming, storage isolation, streaming where appropriate, and malware or content scanning when required by Risk.

Client filenames and paths are untrusted. Uploads MUST NOT enable path traversal, executable placement, or expansion of storage authority.

## 34. File Downloads

Downloads MUST enforce Resource-level Authorization, safe content type, content disposition, caching, and filename handling. Range requests MAY be supported only when Contracted and safe.

Filesystem paths and storage-provider details MUST not be exposed.

## 35. Cache Headers

HTTP caching MUST respect visibility, Authorization, locale, query parameters, and freshness. Sensitive or Customer-specific responses require conservative controls.

Cached representations MUST NOT become authoritative for Payment, Inventory, Price, Order, or Authorization state. `Cache-Control`, validators, and invalidation behavior MUST be Contracted where material.

## 36. Correlation IDs

APIs MUST propagate the repository Correlation ID across supported boundaries. A valid bounded incoming `X-Correlation-ID` MAY be preserved; otherwise the trusted boundary MUST generate one. The identifier SHOULD be returned or referenced where useful and included in safe diagnostic context.

Client-supplied correlation values are untrusted and MUST NOT establish identity, Authorization, or idempotency.

## 37. Trace Context

Approved distributed trace context SHOULD be propagated across supported HTTP and integration boundaries. Trace identifiers are diagnostic context, not business identifiers, Authentication, or Authorization proof.

This standard does not select vendor-specific trace headers or an observability vendor.

## 38. Headers

Custom headers MUST have a documented purpose, bounded and validated values, compatibility rules, and safe logging behavior. Standard HTTP semantics SHOULD be used before creating custom headers.

Headers carrying credentials, tokens, cookies, signatures, or Sensitive Data MUST be protected and redacted.

## 39. Payment API Safety

Payment APIs MUST preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

A Payment Redirect, browser result, client-reported success, or unvalidated Callback or Webhook MUST NOT establish Payment success. Provider-dependent authoritative Payment state requires validated Payment Provider evidence. Client Price, Currency, Payment state, provider reference, or claimed provider outcome is not authoritative.

## 40. Payment Callbacks and Webhooks

Payment Callbacks and Webhooks MUST verify source authenticity and payload integrity according to the Payment Provider Contract, validate schema and semantics, enforce freshness or replay controls where applicable, and process duplicates idempotently before changing state.

Acknowledgement behavior MUST be safe under retries and uncertain outcomes. Asynchronous processing MAY be used where appropriate, but a client redirect is never a substitute and duplicate delivery MUST NOT create duplicate financial or Order effects. This standard does not invent a provider-specific signature algorithm.

## 41. Inventory API Safety

Inventory APIs MUST preserve Inventory ownership of authoritative Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement state. Client input, Product Catalogue, search, and reporting projections MUST NOT become authoritative Inventory truth.

Concurrency-sensitive mutations MUST use server-authoritative state and approved persistence controls. Duplicate requests MUST NOT double-adjust Stock, duplicate Stock Reservations, or permit Overselling.

## 42. Price, Discount, and Promotion Safety

Client-provided Price, Discount, Promotion, Voucher, totals, and calculated savings are untrusted. Server and Domain calculations remain authoritative.

A request MAY provide a governed Voucher code or other input, but eligibility and resulting monetary values MUST be validated by the owning Domain.

## 43. Cart and Checkout

Cart and Checkout APIs MUST NOT treat client Cart totals, tax, Price, Discount, Stock availability, or Payment state as authoritative. Checkout MUST revalidate applicable authoritative state and coordinate the owning Domains according to Product and Architecture.

This standard does not invent Checkout steps or Product behavior.

## 44. Order API

Order APIs MUST preserve the canonical Order lifecycle and MUST NOT accept arbitrary client-directed state transitions. Commands MUST validate current state, Authorization, concurrency, Payment and Inventory dependencies, and applicable Audit Record Requirements.

This standard does not invent Order states.

## 45. Return and Refund

Return and Refund are distinct. API Contracts MUST keep the physical Return lifecycle, financial Refund lifecycle, and Refund Transaction separate.

Eligibility, amounts, state transitions, and Authorization remain server-side and owned by the applicable Domains.

## 46. Staff User APIs

Staff User APIs require explicit server-side Authorization. Object-level access, elevated operations, bulk actions, sensitive exports, Audit Records, and Human Approval Gates MUST be enforced where governed.

Frontend route protection and hidden controls are not security boundaries.

## 47. Bulk APIs

Bulk operations MUST define scope, Authorization, bounded item count, per-item and aggregate result semantics, idempotency where relevant, partial-failure policy, Audit Record Requirements, and rate and resource protection.

This standard does not invent batch sizes.

## 48. Asynchronous Operations

`202 Accepted` MAY represent long-running work only when Contracted. The Contract MUST define operation identity and status, Authorization, polling or notification behavior, terminal and failure semantics, idempotency, and governed cleanup and retention.

This standard does not select asynchronous infrastructure.

## 49. Callbacks to Client Systems

Outbound Webhooks and Callbacks are integration Contracts, not ordinary API responses. They MUST define applicable endpoint Authentication, signing or other integrity protection, retries, Idempotency, duplicate handling, delivery observability, compatibility, and failure behavior.

Detailed event and Webhook rules may be refined by a future standard only when its metadata makes it normative.

## 50. External Provider API Adapters

External provider APIs MUST remain behind project-owned Ports and Adapters. Provider DTOs, errors, authentication mechanisms, protocols, and implementation details MUST NOT leak into public API Contracts or Domain code.

Adapters MUST define explicit timeouts, Authentication, response validation, retry safety, rate-limit behavior, safe error translation, correlation, and observability. This standard does not select an HTTP client.

## 51. Retries

Retries MUST be bounded, observable, and limited to safe failure classes. Unsafe commands MUST NOT be retried automatically without applicable Idempotency Key semantics and evidence that the prior outcome is safe to repeat.

Retry behavior depends on HTTP method, server result certainty, provider behavior, and Contract. An uncertain outcome is not proof of failure and MUST NOT be retried blindly.

## 52. Authentication Failure and Authorization Denial

`401` indicates missing or invalid Authentication where the protocol semantics apply. `403` indicates that an authenticated Principal is not authorized.

Responses MUST avoid security detail leakage and MAY conceal sensitive Resource existence where the governing Security policy requires it.

## 53. Resource Concealment

Unauthorized access MAY be represented as `404` when necessary to avoid disclosing whether a sensitive Resource exists. Behavior MUST be consistent for the protected Resource class and MUST NOT intentionally disclose ownership or existence through response detail, status behavior, or materially distinguishable processing behavior. Timing and side-channel Risks MUST be mitigated according to `.ai/core/SECURITY-STANDARDS.md`.

## 54. Conflict

`409` SHOULD represent Contracted conflicts such as concurrent state changes, uniqueness conflicts, incompatible Idempotency Key reuse, or invalid lifecycle transitions. Recovery guidance MAY be provided when safe.

Constraint names, SQL details, and internal persistence state MUST NOT be exposed.

## 55. Precondition Failure

`412` applies when Contracted HTTP conditional-request semantics fail. It MUST NOT replace a general Domain conflict unless the Contract explicitly expresses the rule as a precondition.

## 56. Unprocessable Content

`422` MAY be selected by an API Contract for semantically invalid content when that distinction is useful and remains compatible with RFC 9457. It is not mandatory and MUST NOT be introduced inconsistently across equivalent Endpoints.

## 57. Rate-Limit Response

`429` applies to governed rate or abuse limits. `Retry-After` SHOULD be returned only when its value is meaningful and known. Numeric limits remain governed configuration and are not selected here.

## 58. Server and Dependency Failures

`500`, `502`, `503`, and `504` MUST be differentiated only where accurate, safe, useful, and Contracted. Responses MUST not expose provider or implementation internals.

Unknown Payment state MUST NOT be converted into false `200` success or fabricated confirmed failure.

## 59. API Security Headers

Security headers MUST follow `.ai/core/SECURITY-STANDARDS.md` and the approved web and infrastructure configuration. This standard does not create a conflicting header baseline or select a gateway implementation.

## 60. Sensitive Data

APIs MUST minimize exposure of Secrets, credentials, Access Tokens, Refresh Tokens, PII, Payment data, internal provider evidence, and protected Audit Record content. Raw CVV storage, logging, or echoing is prohibited.

Problem Details, headers, URLs, examples, and diagnostic responses MUST NOT reflect Sensitive Data unnecessarily.

## 61. Logging

API logging MUST use the repository-approved logging standards, parameterized messages, structured or consistently contextual fields, and safe redaction. Authorization headers, cookies, Session secrets, full Access Tokens, Refresh Tokens, raw CVV, passwords, Secrets, and full sensitive bodies MUST NOT be logged.

Correlation IDs SHOULD support troubleshooting. Logs are not automatically Audit Records.

## 62. Audit Records

Security-sensitive and business-critical changes MUST create governed Audit Records where required by the owning Product, Domain, Security, or compliance rule. API access Logs are diagnostic evidence and MUST NOT substitute for canonical Audit Records.

## 63. OpenAPI

OpenAPI 3.1 is the Architecture-established machine-readable format for externally consumed HTTP API Contracts. OpenAPI and implementation MUST remain synchronized; drift MUST block completion or release according to applicable quality gates.

This standard does not select a generator, documentation UI, code-first tool, or Contract repository layout.

## 64. OpenAPI Content

OpenAPI Contracts MUST document applicable paths, methods, parameters, request bodies, responses, schemas, security schemes, RFC 9457 errors, safe examples, deprecations, pagination, Idempotency Key behavior, and headers.

Examples MUST NOT contain Secrets, credentials, production identifiers, or real Customer data.

## 65. Contract-First and Code-First

This standard does not impose a universal contract-first or code-first workflow. In either approach, material public API changes require review before release and the OpenAPI Contract, implementation, tests, and consumer documentation MUST remain synchronized.

## 66. Schema Reuse

Stable value schemas MAY be reused where their semantics and compatibility requirements are identical. Massive shared schemas that couple unrelated Endpoints MUST be avoided.

A DTO or schema MUST NOT be reused merely because its current fields happen to match another Contract.

## 67. Null and Optional Fields

Contracts MUST distinguish missing, null, and empty values wherever their semantics differ. Required and optional behavior MUST be explicit for requests and responses.

Java `Optional` and database nullability MUST NOT leak directly into API semantics.

## 68. Enum Evolution

Client-visible finite value sets require compatibility planning because adding a value may break exhaustive consumers. Internal lifecycle enums MUST NOT be exposed casually, and enum additions MUST NOT be presumed non-breaking without consumer evidence.

## 69. Deprecation

Deprecation MUST include an explicit Contract marker, documentation, a migration path, and observability where useful. Removal requires the governed compatibility and release process.

This standard does not invent a deprecation period.

## 70. Breaking Change Classification

Breaking or potentially breaking changes include removing or renaming a field, changing its type or semantics, narrowing accepted values, changing required or optional behavior, altering status or error behavior, changing Authorization, and changing pagination semantics.

An additive change is not automatically compatible; reviewers MUST assess supported consumers and finite value handling.

## 71. Backward Compatibility

API evolution SHOULD favor additive compatible changes. The server MUST support governed client compatibility commitments, and changes MUST preserve stable semantics for the supported window.

This standard does not define a compatibility duration.

## 72. API Contract Testing

Contract tests MUST verify schemas, representative requests and responses, RFC 9457 behavior, compatibility, Authorization, negative cases, and applicable Payment and Inventory invariants. Examples MUST be executable or validated where practical.

## 73. Controller Testing

`.ai/backend/SPRING.md` owns Spring-specific Controller and slice-testing rules. API tests MUST verify externally observable Contract behavior without requiring a full application context where a focused test provides sufficient evidence.

## 74. Integration Testing

Integration Tests MUST cover real boundary behavior for serialization, validation, Authentication, Authorization, persistence or provider integration, concurrency, error translation, and Idempotency where applicable.

PostgreSQL-specific integration MAY use Testcontainers according to the Approved database standards. Test doubles MUST NOT conceal material database, security, or provider behavior.

## 75. Security Testing

API security tests MUST cover applicable unauthenticated and unauthorized requests, object-level denial, malformed and oversized input, injection attempts, Sensitive Data leakage, replay, Callback or Webhook validation, and rate-limit behavior.

Positive authorization tests alone are insufficient.

## 76. Performance and Resource Testing

Tests MUST verify governed performance Requirements and resource bounds, including safe behavior for large permitted pages, payloads, expansions, searches, and bulk operations. This standard does not invent latency, throughput, or capacity targets.

## 77. Observability

API telemetry SHOULD provide HTTP method, templated route, response status, duration, Correlation ID or trace context, error classification, and dependency outcome without leaking Sensitive Data.

Raw URLs containing identifiers and other high-cardinality values SHOULD NOT be used as unbounded Metric dimensions.

## 78. Health and Management Endpoints

Health and management Endpoints are operational surfaces, not Product APIs. Their exposure, protection, and Actuator implementation remain governed by Architecture, Security, infrastructure, and `.ai/backend/SPRING.md`.

## 79. API Documentation

Human-readable API documentation MUST complement OpenAPI where workflows or integration guidance require explanation. It SHOULD cover Authentication, Authorization, examples, error semantics, idempotency, pagination, compatibility, deprecation, and operational recovery without duplicating Product Requirements excessively.

## 80. Decision and Exception Governance

Material Architecture changes require an ADR. Contract and business Decisions within the Approved Architecture use the applicable Decision governance. Ordinary Endpoint or field work does not require an ADR unless it makes a material Architecture Decision, and this standard does not create API-specific identifiers.

Runtime Java or API exceptions are not governance Exceptions. Deviations MUST use the applicable Security Exception, Testing Exception, Coding Exception, Documentation Exception, Decision Record, or ADR according to the Requirement and owner. A lower-level deviation MUST NOT waive a mandatory Security Requirement without an approved Security Exception.

## 81. Prohibited Practices

The following are prohibited:

- exposing persistence Entities or provider DTOs as API Contracts;
- treating client Price, Discount, Payment, Inventory, Role, Permission, Authorization, or provider outcome as authoritative;
- treating RFC 7807 as the current repository Problem Details baseline;
- returning stack traces, internal exceptions, SQL details, constraint names, or provider internals;
- arbitrary filtering, sorting, field expansion, or lifecycle-state mutation;
- unbounded requests, pages, uploads, or bulk operations;
- relying on UI behavior for Authorization;
- logging or returning raw CVV, Secrets, credentials, or tokens;
- blindly retrying unsafe or uncertain operations;
- inventing status, error, or Product semantics;
- making undocumented breaking changes; and
- treating API Logs as complete Audit Records.

## 82. Compliance Matrix

| Concern | Governing Source | API Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `.ai/core/AGENTS.md`; `.ai/core/DECISIONS.md` | Remain subordinate and route material Decisions correctly | Authority and Decision review |
| Terminology | `.ai/core/GLOSSARY.md` | Use canonical terms and qualified meanings | Terminology scan |
| Product | `.ai/core/PRODUCT.md` | Implement approved behavior without invention | Requirement traceability |
| Architecture | `.ai/core/ARCHITECTURE.md` | Preserve REST, boundaries, ownership, and `/api/v1` | Architecture review and ADRs |
| Security | `.ai/core/SECURITY-STANDARDS.md` | Enforce secure transport, input, output, and failure behavior | Security tests and review |
| Testing | `.ai/core/TESTING-STANDARDS.md` | Prove Contracts, denial paths, and critical invariants | Test results |
| Coding | `.ai/core/CODING-STANDARDS.md` | Keep DTOs and boundary code safe and maintainable | Code review and static checks |
| Spring | `.ai/backend/SPRING.md` | Align Controller, validation, security, and error behavior | Spring tests and review |
| Java | `.ai/backend/JAVA.md` | Preserve Java boundary and domain-safety rules | Java tests and review |
| Database | `.ai/backend/DATABASE.md` | Preserve persistence authority and Database Transaction boundaries | Integration and integrity tests |
| PostgreSQL | `.ai/backend/POSTGRES.md` | Avoid leaking PostgreSQL details into Contracts | PostgreSQL integration tests |
| RFC 9457 | `.ai/core/ARCHITECTURE.md`; `.ai/backend/SPRING.md` | Produce safe, stable Problem Details | Error Contract tests |
| OpenAPI | `.ai/core/ARCHITECTURE.md`; `.ai/core/DOCUMENTATION-STANDARDS.md` | Keep OpenAPI 3.1 and implementation synchronized | Contract validation |
| Authentication | `.ai/core/SECURITY-STANDARDS.md` | Establish a trusted Principal at protected Endpoints | Authentication tests |
| Authorization | `.ai/core/SECURITY-STANDARDS.md`; `.ai/core/ARCHITECTURE.md` | Enforce route, object, property, and state policy server-side | Positive and denial-path tests |
| Payment | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Require validated Payment Provider evidence | Provider, replay, and reconciliation tests |
| Inventory | `.ai/core/PRODUCT.md`; `.ai/core/ARCHITECTURE.md` | Preserve Stock and Stock Reservation authority | Concurrency and invariant tests |
| Idempotency | `.ai/core/ARCHITECTURE.md`; `.ai/backend/DATABASE.md` | Prevent duplicate harmful effects | Duplicate and concurrency tests |
| Compatibility | `.ai/core/ARCHITECTURE.md`; `.ai/core/DOCUMENTATION-STANDARDS.md` | Classify, document, and test API evolution | Compatibility review |
| Observability | `.ai/core/ARCHITECTURE.md`; `.ai/backend/SPRING.md` | Emit bounded, safe API telemetry | Telemetry review |
| Exceptions | Applicable core standard | Use only the governance mechanism owned by the Requirement | Approved, time-bound record where required |

## 83. Related Documents

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
- `.ai/core/DESIGN-SYSTEM.md`
- `.ai/backend/SPRING.md`
- `.ai/backend/JAVA.md`
- `.ai/backend/DATABASE.md`
- `.ai/backend/POSTGRES.md`

## 84. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-12 | Approved | Promoted the API implementation standard after final governance, REST, HTTP semantics, RFC 9457, OpenAPI, Contract, Authentication, Authorization, idempotency, Payment, Inventory, compatibility, security, testing, observability, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-12 | Draft | Established the initial API implementation standard covering REST resource design, HTTP semantics, RFC 9457 Problem Details, validation, Authentication, Authorization, idempotency, pagination, Payment, Inventory, OpenAPI, compatibility, security, testing, observability, and governance. |

## 85. Quality Requirements

This standard MUST remain subordinate to the Approved core and backend standards, use canonical terminology, and keep API Contracts synchronized with implementation and tests. It MUST NOT manufacture Product semantics, provider evidence, data ownership, lifecycle states, or Architecture.

Review evidence MUST be practical, testable, traceable, and proportionate to Risk. A complete API change includes applicable OpenAPI, compatibility, security, test, observability, and documentation updates.

## 86. Final Validation

Before this document or a governed revision is presented for approval, reviewers MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. REST, `/api/v1`, OpenAPI 3.1, and RFC 9457 match Approved Architecture;
3. RFC 7807 is not treated as the current baseline;
4. no unsupported protocol, gateway vendor, JSON naming convention, or technology is selected;
5. no numeric page, rate, payload, deprecation, or idempotency-retention value is invented;
6. Payment authority requires validated Payment Provider evidence;
7. Inventory owns Stock, Stock Reservation, and Available-to-Sell truth;
8. Authentication and server-side Authorization remain mandatory except for explicitly public Endpoints;
9. client Price, Discount, Payment state, Inventory state, Role, Permission, and provider outcome are untrusted;
10. no provider-specific Webhook signature algorithm, HTTP client, or Identity Provider is selected;
11. no arbitrary Product lifecycle or API error taxonomy is invented;
12. no API-specific governance Exception or Decision identifier is introduced;
13. ordinary Endpoint work does not automatically require an ADR;
14. OpenAPI, implementation, tests, and documentation remain synchronized;
15. there are no placeholders, empty sections, malformed tables, or broken paths; and
16. the final diff contains only intended API-standard changes.
