---
title: ARCHITECTURE
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-08-05
applies_to:
  - Frontend
  - Backend
  - Data
  - Integrations
  - Infrastructure
  - AI coding agents
  - Human contributors
review_cycle: Monthly
source_of_truth: true
---

# ARCHITECTURE.md

> Enterprise Fashion Commerce Platform — Authoritative Technical Blueprint

This document defines the approved architectural direction, system boundaries, quality attributes, dependency rules, and technology baseline for the Enterprise Fashion Commerce Platform. It must be read together with `.ai/core/AGENTS.md`. The constitution governs contributor behaviour; this document governs the technical shape of the platform.

## 1. Purpose and Authority

The purpose of this document is to ensure that all contributors design and implement the platform as one coherent system rather than as disconnected features.

It defines:

- The architectural vision and target state.
- The initial deployment and modularity strategy.
- The approved technology baseline.
- Domain ownership and dependency direction.
- Layer responsibilities and prohibited coupling.
- Integration, data, security, observability, performance, and resilience principles.
- The path for future evolution without premature distribution.

The statements **must**, **must not**, **required**, and **prohibited** are mandatory. The statements **should** and **should not** are strong defaults that may be overridden only through a documented reason. Material deviations require an accepted Architecture Decision Record.

Where guidance conflicts, the decision hierarchy in `.ai/core/AGENTS.md` applies.

## 2. Architectural Vision

The platform will provide a production-grade digital commerce capability for a premium fashion and streetwear business. It must support a refined customer storefront and reliable operational administration across catalogue, pricing, inventory, customer accounts, cart, checkout, payments, orders, fulfilment, shipping, content, notifications, and reporting.

The architecture must enable the business to:

- Present products and collections through a fast, mobile-first storefront.
- Maintain accurate product, price, variant, media, and inventory information.
- Accept secure online payments without storing raw card data.
- Create and fulfil orders reliably despite asynchronous provider behaviour.
- Operate through role-protected administration workflows.
- Diagnose failures and reconcile critical business processes.
- Introduce additional providers, channels, and capabilities without redesigning the core domain.

The initial system will be implemented as a **domain-driven modular monolith**. The frontend and backend will be separately deployable applications, while the backend will remain one primary deployable unit with strongly enforced internal module boundaries.

The platform must avoid premature microservices. Distribution will be considered only where independently measurable operational, scalability, security, deployment, ownership, or availability requirements justify the additional complexity.

## 3. Architecture Goals

The architecture must optimise for the following outcomes:

1. **Correctness:** prices, stock, payment outcomes, orders, refunds, and fulfilment state must remain trustworthy.
2. **Maintainability:** modules must be understandable and changeable without broad unintended impact.
3. **Security and privacy:** customer, administrative, payment, and operational data must be protected by default.
4. **Mobile-first usability:** customer-facing capabilities must perform and remain accessible on mobile devices.
5. **Operational simplicity:** the initial platform must be supportable by a small engineering team.
6. **Extensibility:** payment, shipping, notification, storage, search, and analytics providers must remain replaceable.
7. **Observability:** critical flows must be diagnosable through logs, metrics, traces, audit records, and correlation identifiers.
8. **Scalability:** the design must support horizontal application scaling and managed-service growth without premature decomposition.
9. **Testability:** business rules and integrations must be isolated behind testable boundaries.
10. **AI-assisted consistency:** repository context must enable multiple coding agents to produce changes aligned with the same architecture.

## 4. Quality Attributes

Quality attributes are architectural requirements, not optional refinements.

### 4.1 Security

The system must:

- Enforce authentication and authorization at trusted backend boundaries.
- Apply least privilege to users, services, databases, cloud identities, and pipelines.
- Store secrets in approved secret-management services.
- Encrypt data in transit and rely on approved encryption at rest.
- Validate all external input and safely encode output.
- Isolate external providers behind controlled adapters.
- Avoid storage of raw payment-card data.
- Produce auditable records for sensitive administrative and financial actions.

### 4.2 Reliability

The platform must preserve business truth under retries, duplicate requests, delayed callbacks, transient provider failure, and partial workflow failure.

Critical operations such as payment confirmation, refund initiation, inventory reservation, and order creation must be idempotent where duplication could create financial or operational harm.

### 4.3 Availability

The storefront should remain available for product browsing when non-essential providers are degraded. Failure of notifications, analytics, or non-authoritative integrations must not invalidate confirmed commercial operations.

Dependency health must be classified so that an optional provider outage does not unnecessarily make the entire application unavailable.

### 4.4 Performance

The architecture must support:

- Core Web Vitals targets defined in the engineering constitution.
- Efficient catalogue and product retrieval.
- Backend p95 response-time targets defined per API class.
- Responsive checkout interactions excluding unavoidable third-party latency.
- Optimised media delivery through object storage and edge caching.
- Query and index design based on actual access patterns.

### 4.5 Scalability

The backend must remain stateless at the HTTP application tier except for explicitly externalised session or cache state. Application instances must be horizontally scalable.

Stateful concerns must be owned by managed data services such as PostgreSQL, Redis where justified, object storage, or approved messaging infrastructure.

### 4.6 Maintainability

Architecture must favour explicit domain ownership, dependency direction, typed contracts, cohesive modules, and small application use cases.

A contributor must be able to identify where a business rule belongs without searching the entire repository.

### 4.7 Testability

Domain logic must be executable without HTTP, database, cloud, or provider dependencies. External integrations must be substitutable in tests through ports and adapters.

### 4.8 Accessibility

The storefront and administrative interface must target WCAG 2.2 AA. Accessibility requirements influence component design, navigation, forms, errors, loading states, and content structure.

### 4.9 Observability

Critical business workflows must expose sufficient telemetry to answer:

- What happened?
- Which customer, order, payment, shipment, or request was affected?
- Where did the workflow fail?
- Was the operation retried?
- Is business data consistent?
- What action is required?

## 5. Architectural Principles

### 5.1 Domain First

The system must be organised around business capabilities rather than framework layers alone. Domain terminology must remain consistent across requirements, code, APIs, events, data, tests, and operations.

### 5.2 Modular Monolith First

The backend will begin as one deployable Spring Boot application composed of explicit domain modules. Internal module boundaries must be treated as if modules could later become independently deployable.

This means:

- No uncontrolled cross-module repository access.
- No shared mutable domain entities.
- No circular module dependencies.
- Cross-domain interaction through approved application interfaces, queries, APIs, or events.
- Independent ownership of business rules and authoritative data.

### 5.3 Hexagonal Architecture

Each backend domain module should follow ports-and-adapters principles:

- The domain defines business behaviour and invariants.
- The application layer coordinates use cases.
- Inbound adapters expose capabilities through REST, scheduled jobs, messaging, or administrative commands.
- Outbound ports describe persistence, provider, notification, storage, search, and event needs.
- Infrastructure adapters implement those ports.

Framework and provider details must point inward toward project-owned abstractions; domain logic must not depend outward on frameworks or vendors.

### 5.4 API First

Externally consumed HTTP contracts must be designed and documented using OpenAPI before or alongside implementation. Consumers must not rely on persistence models or undocumented response shapes.

### 5.5 Secure by Default

The default path must be the secure path. Public access, elevated permissions, broad cloud roles, sensitive logging, and relaxed validation require explicit justification.

### 5.6 Observable by Design

Telemetry must be designed with the workflow rather than added only after incidents occur.

### 5.7 Managed Services First

Use managed cloud services where they materially reduce operational burden and provide suitable security, availability, backup, and scaling characteristics.

### 5.8 Explicit Consistency

Every workflow that crosses domains or providers must define its consistency model. Contributors must not assume a distributed transaction exists across the database and external systems.

### 5.9 Backward-Compatible Evolution

APIs, events, schemas, and deployment changes should evolve additively. Breaking changes require versioning, migration sequencing, and deprecation guidance.

### 5.10 Simplicity with an Evolution Path

Choose the simplest architecture that safely satisfies current requirements. Preserve replaceable boundaries so the platform can evolve without speculative infrastructure.

## 6. High-Level System Context

The primary actors and external systems are:

| Actor or System         | Responsibility                                                                                                   |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------- |
| Customer                | Browses, searches, saves products, checks out, pays, and tracks orders.                                          |
| Store Administrator     | Manages products, inventory, pricing, orders, content, customers, users, and reports according to permissions.   |
| Support Agent           | Investigates customer, payment, order, shipment, and communication issues.                                       |
| Angular Web Application | Provides the customer storefront and protected administration experience.                                        |
| Spring Boot Application | Owns business use cases, domain rules, security enforcement, persistence coordination, and external integration. |
| PostgreSQL              | Stores authoritative transactional and configuration data.                                                       |
| Object Storage and CDN  | Stores and distributes product media and approved static assets.                                                 |
| Payment Provider        | Hosts or processes approved payment methods and emits authoritative payment notifications.                       |
| Shipping Provider       | Provides delivery quotations, shipment creation, labels, tracking, and delivery events.                          |
| Notification Provider   | Delivers transactional email or other approved communications.                                                   |
| Analytics Platform      | Receives consent-aware customer and business events.                                                             |
| Azure Platform Services | Provide hosting, identity, secrets, networking, monitoring, backup, and managed data capabilities.               |

The browser must never communicate directly with the database. Direct browser-to-provider flows may be used only where the provider’s secure hosted pattern requires them, and authoritative confirmation must still be validated by the backend.

## 7. Target Technology Stack

The approved initial baseline is:

| Area                        | Technology Direction                                                                                                   |
| --------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| Customer and Admin Frontend | Angular 20+ with standalone components, TypeScript strict mode, Signals, RxJS, Angular Router, and Reactive Forms      |
| Backend                     | Java 21+ and Spring Boot 3.x                                                                                           |
| API                         | REST-first JSON APIs documented with OpenAPI 3.1                                                                       |
| Security                    | Spring Security with approved token/session strategy and role/permission authorization                                 |
| Persistence                 | PostgreSQL with versioned Flyway migrations                                                                            |
| Cache                       | Redis only for justified cache, session, idempotency, or coordination use cases                                        |
| Object Storage              | Azure Blob Storage                                                                                                     |
| Edge Delivery               | Azure Front Door and/or approved CDN capability                                                                        |
| Application Hosting         | Azure App Service or Azure Container Apps, selected through ADR                                                        |
| Secrets                     | Azure Key Vault and managed identities where supported                                                                 |
| Observability               | OpenTelemetry-compatible instrumentation, Application Insights, Azure Monitor, and Log Analytics                       |
| CI/CD                       | GitHub Actions with protected environments and least-privilege permissions                                             |
| Infrastructure as Code      | Bicep or Terraform, selected and standardised through ADR                                                              |
| Testing                     | JUnit, Spring Boot Test, Testcontainers, frontend unit/component tools, Playwright, accessibility and contract testing |
| Documentation               | Markdown, OpenAPI, Mermaid/PlantUML/Draw.io source, and ADRs in Git                                                    |

Technology-specific details belong in the corresponding `.ai/backend/` and `.ai/frontend/` standards. Those files may refine but must not contradict this blueprint.

## 8. Architectural Style

### 8.1 Deployment Model

The initial production topology consists of:

- One Angular web application deployment.
- One Spring Boot backend deployment that may run multiple stateless instances.
- One primary PostgreSQL database service.
- Object storage for media.
- Optional Redis introduced only for approved use cases.
- External payment, shipping, notification, and analytics providers.
- Azure-managed identity, secrets, monitoring, and networking services.

### 8.2 Modular Monolith

A modular monolith is selected because it provides:

- Strong transactional support for closely related commerce workflows.
- Lower operational complexity than microservices.
- Easier local development and integration testing.
- Clear domain boundaries without premature network distribution.
- A credible future extraction path when module characteristics justify it.

The modular monolith must not become an unstructured monolith. Module boundaries must be enforced through package structure, tests, ownership, and dependency rules.

### 8.3 Future Module Extraction

A module may be considered for independent deployment only when one or more of the following are demonstrated:

- It requires materially different scaling characteristics.
- It requires independent release cadence or ownership.
- It has a distinct security or compliance boundary.
- Its failure must be isolated from the core transaction path.
- Its technology or data needs are incompatible with the primary runtime.
- Operational evidence shows that distribution provides more value than complexity.

Extraction requires an ADR, contract definition, data-ownership plan, observability plan, deployment strategy, and failure-mode analysis.

## 9. Business Domains

| Domain         | Primary Responsibility                                                                                        |
| -------------- | ------------------------------------------------------------------------------------------------------------- |
| Product        | Owns product definitions, variants, attributes, publication lifecycle, and product media metadata.            |
| Category       | Owns hierarchical classification and customer navigation groupings.                                           |
| Inventory      | Owns on-hand, reserved, available-to-sell quantities, reservations, adjustments, and movements.               |
| Pricing        | Owns prices, promotional rules, voucher eligibility, discounts, and pricing policy.                           |
| Customer       | Owns customer profiles, addresses, preferences, and consent records.                                          |
| Identity       | Owns credentials, authentication sessions, roles, permissions, and access-security events.                    |
| Cart           | Owns active purchase intent, cart items, quantity changes, and cart persistence.                              |
| Checkout       | Coordinates final validation of customer, pricing, promotion, inventory, shipping, and payment initiation.    |
| Payment        | Owns payment attempts, provider references, callbacks, payment state, refunds, and reconciliation.            |
| Order          | Owns confirmed commercial records, order snapshots, lifecycle, status history, and cancellation coordination. |
| Shipping       | Owns methods, quotations, shipments, tracking, and delivery events.                                           |
| Notifications  | Owns templates, notification requests, attempts, retry state, and delivery outcome.                           |
| CMS            | Owns editorial pages, banners, homepage content, campaigns, and policy presentation.                          |
| Administration | Provides protected operational workflows over domain-owned application services.                              |
| Reporting      | Owns read models, exports, dashboards, and analytical projections without mutating transactional state.       |

Detailed ownership rules remain governed by `.ai/core/AGENTS.md` and domain specifications under `specifications/domains/`.

## 10. Domain-Driven Modular Structure

The backend root package should be organised by business capability:

```text
com.enterprisecommerce
├── identity
├── customer
├── product
├── category
├── inventory
├── pricing
├── cart
├── checkout
├── payment
├── order
├── shipping
├── notification
├── cms
├── administration
├── reporting
└── shared
```

The `shared` area must remain small and may contain only genuine cross-cutting technical primitives or stable value types. It must not become a location for business logic that lacks clear ownership.

Each domain module should expose only its approved application-facing contract. Internal domain objects, repositories, persistence mappings, and provider adapters must remain encapsulated.

### 10.1 Dependency Direction

Allowed dependency direction is:

```text
Presentation / Inbound Adapters
            ↓
      Application Layer
            ↓
        Domain Layer
```

Infrastructure adapters implement ports defined inward by the application or domain layer:

```text
Infrastructure Adapter → Application or Domain Port
```

The domain layer must not depend on Spring MVC, JPA, Azure SDKs, HTTP clients, payment SDKs, or other infrastructure frameworks.

### 10.2 Cross-Domain Interaction

Cross-domain interaction should use one of the following:

- A public application-service interface.
- An explicit query contract.
- A documented internal API.
- A domain event inside the same process.
- An integration event when asynchronous or externally visible communication is required.

Direct access to another domain’s internal repository, entity, or table is prohibited unless an accepted ADR defines a controlled exception.

### 10.3 Transaction Boundaries

Transactions must be defined around application use cases, not controllers.

A single database transaction may coordinate changes across closely related modules only when the consistency requirement is explicit and module encapsulation remains intact. External provider calls must not be held inside long-running database transactions.

## 11. Layered and Hexagonal Architecture

Each significant backend module should use the following conceptual layers.

### 11.1 Domain Layer

Owns:

- Aggregates and entities.
- Value objects.
- Domain services.
- Business invariants.
- Domain events.
- Domain-specific errors and policies.

The domain layer must be framework-independent wherever practical.

### 11.2 Application Layer

Owns:

- Named use cases.
- Command and query orchestration.
- Transaction boundaries.
- Authorization checks requiring business context.
- Coordination of repositories, domain services, and external ports.
- Mapping between inbound requests and domain/application models.
- Publication of approved events.

The application layer must not contain presentation-specific behaviour.

### 11.3 Inbound Adapters

Examples include:

- REST controllers.
- Scheduled jobs.
- Administrative commands.
- Message consumers.
- Provider callback endpoints.

Inbound adapters translate transport concerns into application use cases. They must not own business rules.

### 11.4 Outbound Ports

Ports describe what the application requires from external capabilities, such as:

- Persistence.
- Payment processing.
- Shipping quotations and booking.
- Notification delivery.
- Media storage.
- Search indexing.
- Clock and identifier generation.
- Event publication.

Ports must use project-owned types rather than leaking provider SDK models inward.

### 11.5 Outbound Adapters

Adapters implement ports through:

- Spring Data JPA.
- HTTP clients.
- Azure SDKs.
- Payment or courier SDKs.
- Email providers.
- Redis.
- Messaging services.

Provider-specific translation, validation, signatures, timeouts, retries, and errors belong in adapters.

### 11.6 Persistence Mapping

Persistence entities must not be exposed through REST APIs or used as domain models by default. Mapping may be explicit or implemented with approved mapping tools, but it must remain understandable and testable.

## 12. Frontend Architecture Direction

The Angular application will use:

- Standalone components.
- Feature-oriented folders.
- Route-level lazy loading.
- Signals for local synchronous state.
- RxJS for HTTP, event streams, and asynchronous composition.
- Typed Reactive Forms.
- Reusable design-system components.
- Separate storefront and administration layouts.
- Server-side authorization as the authoritative access boundary.

The frontend may validate user input for usability but must not become the authoritative owner of price, stock, payment, permission, or order rules.

Further rules are defined in `.ai/frontend/ANGULAR.md`, `.ai/frontend/UI.md`, `.ai/frontend/ACCESSIBILITY.md`, `.ai/frontend/PERFORMANCE.md`, and `.ai/frontend/STORYBOOK.md`.

## 13. API Architecture Direction

The platform will use REST-first APIs under a versioned base path such as `/api/v1`.

APIs must:

- Use resource-oriented naming.
- Return project-owned DTOs.
- Apply consistent pagination, filtering, and sorting.
- Use RFC 7807-style problem responses with stable error codes.
- Support idempotency for duplicate-sensitive commands.
- Propagate correlation identifiers.
- Enforce authentication and authorization at the backend.
- Be documented with OpenAPI 3.1.
- Evolve additively where possible.

GraphQL is not part of the initial platform baseline. Its introduction would require a documented need and an ADR.

## 14. Event Architecture Direction

Events may be used for decoupled reactions, audit-friendly state propagation, provider integration, notifications, reporting projections, and future module extraction.

### 14.1 Domain Events

Domain events represent completed facts inside the application boundary, such as:

- `ProductPublished`
- `StockReserved`
- `PaymentSucceeded`
- `OrderConfirmed`
- `ShipmentCreated`

They are expressed in past tense and must not be used as hidden commands.

### 14.2 Integration Events

Integration events are stable, versioned messages intended for asynchronous consumers outside the originating module or deployable system.

### 14.3 Reliability

When reliable publication must be coordinated with database state, the architecture should use an outbox pattern or another accepted durable mechanism.

Consumers must be idempotent because event delivery may be at least once.

### 14.4 Initial Position

The initial modular monolith may use in-process events for appropriate internal reactions. External messaging infrastructure should be introduced only when required by reliability, decoupling, scale, or integration needs.

## 15. Data Architecture Direction

PostgreSQL is the authoritative transactional database.

The data architecture must provide:

- Explicit ownership of tables and write operations by domain.
- UUID-based business identifiers according to database standards.
- Referential integrity and appropriate database constraints.
- Versioned Flyway migrations.
- UTC timestamps and explicit currency codes.
- Optimistic locking for concurrent mutable aggregates where required.
- Immutable snapshots for confirmed order and financial history.
- Append-only movements or histories for critical inventory, payment, order, and audit changes.
- Purpose-specific read models or projections for reporting when required.

Direct database integration between external systems and domain tables is prohibited.

Search may initially use PostgreSQL capabilities. A dedicated search service may be introduced behind a search port when product scale, relevance, merchandising, or latency requirements justify it.

## 16. Integration Architecture Direction

External systems must be integrated behind project-owned ports and adapters.

Every integration must define:

- Authentication and secret handling.
- Request and response mapping.
- Timeouts.
- Retry policy.
- Idempotency behaviour.
- Rate limits.
- Error classification.
- Observability.
- Reconciliation process.
- Sandbox and production configuration.
- Failure and recovery behaviour.

Provider SDK or payload types must not leak into core domain logic.

The initial provider categories are:

- Payments.
- Shipping and courier services.
- Transactional notifications.
- Object storage.
- Analytics.
- Optional address or fraud services when approved.

## 17. Security Architecture Direction

Security architecture includes:

- Customer and administrative authentication.
- Role and permission authorization.
- Short-lived access credentials and revocable session or refresh strategy.
- Secure password hashing.
- Rate limiting and brute-force protection.
- Hosted or tokenised payment processing.
- Provider callback signature validation.
- Encryption in transit.
- Secret management through Azure Key Vault.
- Managed identities where supported.
- Secure headers, CORS, and CSRF controls appropriate to the chosen session model.
- Audit logging for sensitive operations.
- Data minimisation and privacy-aware retention.

Detailed controls belong in `.ai/core/SECURITY-STANDARDS.md` and technology-specific standards.

## 18. Observability Architecture Direction

The system must use structured, correlated telemetry.

Required capabilities include:

- Structured application logs.
- Correlation IDs across browser, API, providers, events, and jobs where possible.
- Distributed tracing with OpenTelemetry-compatible instrumentation.
- Technical metrics for latency, throughput, error rate, saturation, and dependency health.
- Business metrics for checkout, payment, order, fulfilment, refund, and notification outcomes.
- Audit records distinct from diagnostic logs.
- Health and readiness endpoints.
- Actionable dashboards and alerts.

Critical workflows must be traceable by business identifier, such as order number, payment reference, shipment reference, or inventory reservation ID, without logging prohibited sensitive data.

## 19. Azure Deployment Direction

The target Azure architecture should include:

- Azure Front Door and approved web edge controls.
- Static hosting suitable for the Angular application.
- Azure App Service or Azure Container Apps for the Spring Boot application.
- Azure Database for PostgreSQL.
- Azure Blob Storage for media.
- Azure Key Vault.
- Application Insights, Azure Monitor, and Log Analytics.
- Managed identities and least-privilege role assignments.
- Budget alerts and environment-level cost controls.

Redis, Service Bus, private endpoints, advanced network isolation, and additional managed services will be introduced when justified by approved requirements and cost.

Environments must be logically separated at minimum into local, development, test/QA, and production. Production data and secrets must not be reused in lower environments except through approved, sanitised processes.

## 20. Resilience and Consistency Direction

### 20.1 External Calls

All external calls must use explicit timeouts. Retries must be bounded and restricted to safe failure classes. Non-idempotent operations must not be retried blindly.

### 20.2 Payment and Order Consistency

The platform must not treat a customer redirect as proof of payment. Authoritative payment confirmation must come from validated provider state, normally through a signed callback or server-side verification.

Duplicate callbacks must not create duplicate orders or side effects.

### 20.3 Inventory Consistency

Inventory must define explicit reservation, expiry, release, and finalisation behaviour. Available-to-sell quantity must not be calculated independently by multiple domains.

### 20.4 Notification Failure

Notification delivery failure must not invalidate a confirmed order, payment, or shipment. Failed notifications must be retryable and observable.

### 20.5 Reconciliation

Payment, order, inventory, shipment, and notification integrations must provide reconciliation mechanisms for uncertain or inconsistent state.

## 21. Architecture Decision Process

An ADR is required when a decision:

- Introduces or replaces a major technology.
- Changes module boundaries or ownership.
- Creates a new integration style.
- Introduces infrastructure with material cost or operational impact.
- Changes API, event, identity, persistence, or deployment strategy.
- Creates a long-lived exception to this document.
- Is difficult or expensive to reverse.

ADRs must document context, decision, alternatives, consequences, status, and migration impact.

This document records the architectural baseline. ADRs record individual decisions and must not silently contradict the baseline. Accepted contradictions require this document to be updated.

## 22. Architecture Governance and Fitness

Architecture must be protected through automated and manual controls.

Recommended fitness functions include:

- Architecture tests that prevent prohibited module dependencies.
- CI validation of OpenAPI and schema compatibility.
- Migration validation.
- Dependency and vulnerability scanning.
- Bundle and performance budgets.
- Test coverage of domain invariants.
- Secret scanning.
- Infrastructure policy checks.

Code review must confirm both local correctness and alignment with the target architecture.

## 23. Out of Scope for Version 1

The following are not part of the initial architecture unless later approved:

- Independent microservices for each domain.
- Multi-region active-active deployment.
- Multi-tenant SaaS operation.
- Native mobile applications.
- International multi-currency settlement.
- Marketplace and third-party sellers.
- Event sourcing as the primary persistence model.
- Full CQRS across all domains.
- Complex warehouse-management functionality.
- Real-time AI recommendations in the critical purchase path.
- Social login, passwordless login, or mandatory customer MFA.
- Dedicated search infrastructure before requirements justify it.

Exclusion from version 1 does not prohibit future evolution. It prevents speculative complexity from entering the initial platform without evidence.

## 24. Architecture Self-Review Checklist

Before proposing a material technical change, verify:

- [ ] The affected business domain and owner are identified.
- [ ] The change follows the decision hierarchy in `AGENTS.md`.
- [ ] Domain rules remain isolated from presentation and infrastructure.
- [ ] Module boundaries and dependency direction are preserved.
- [ ] External providers remain behind project-owned ports and adapters.
- [ ] Data ownership and transaction boundaries are explicit.
- [ ] API, event, and schema compatibility are considered.
- [ ] Security, privacy, accessibility, performance, and observability impacts are addressed.
- [ ] Failure, retry, idempotency, reconciliation, and rollback behaviour are defined where applicable.
- [ ] The architecture remains no more complex than required.
- [ ] A material decision is captured in an ADR.
- [ ] Documentation and diagrams are updated with the implementation.

## Revision History

| Version | Date       | Status | Summary                                                                                                                                                                                             |
| ------- | ---------- | ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 0.1.0   | 2026-08-05 | Draft  | Established the architectural vision, quality attributes, technology baseline, modular-monolith and hexagonal direction, domain boundaries, integration principles, and Azure deployment direction. |
