---
title: GLOSSARY
version: 0.3.0
status: Draft
owner: Engineering and Product
last_updated: 2026-08-05
source_of_truth: true
review_cycle: Monthly
---

# GLOSSARY.md

This document serves as the canonical vocabulary for the repository. All AI agents, specifications, documentation, code, Architecture Decision Records (ADRs), and discussions must consistently use the definitions herein to ensure clarity, alignment, and interoperability across teams and systems. For related foundational context, please refer to `.ai/core/AGENTS.md`, `.ai/core/ARCHITECTURE.md`, and `.ai/core/PRODUCT.md`.

---

## 1. Purpose

Defines the standardized terminology used throughout the enterprise fashion ecommerce platform to promote consistent communication, reduce ambiguity, and facilitate shared understanding among all stakeholders.

## 2. Usage Rules

- Always use glossary terms as defined here in all documentation, code comments, ADRs, and AI agent interactions.
- Avoid introducing synonyms or ambiguous terms without prior glossary inclusion.
- When extending domain-specific terminology, ensure alignment with core definitions and document refinements explicitly.

## 3. Naming Conventions

- Use PascalCase for entities and domain concepts (e.g., ProductOwner).
- Use camelCase for attributes and properties.
- Acronyms should be fully capitalized (e.g., SKU, API).
- Avoid abbreviations unless widely recognized and defined.

## 4. Business Terms

- **Merchant**: An entity or individual authorized to sell products on the platform.
- **Customer**: A user who purchases or intends to purchase products.
- **Visitor**: An unauthenticated user browsing the platform.
- **Registered Customer**: A customer with an authenticated account.
- **Staff User**: An internal user with access to administrative or operational functions.
- **Product Owner**: A stakeholder responsible for the lifecycle and strategy of a product.
- **Domain Owner**: The designated authority responsible for a specific business domain or bounded context.

## 5. Commerce Terms

- **Catalogue**: The organized collection of products and related information available for sale.
- **Collection**: A curated grouping of products within the catalogue.
- **Category**: A classification within the catalogue used to group similar products.
- **Product**: A sellable item defined by a set of attributes and variants.
- **Variant**: A specific version of a product differing by attributes such as size or color.
- **SKU (Stock Keeping Unit)**: A unique identifier assigned to a variant for inventory tracking.
- **Price**: The monetary value assigned to a product or variant.
- **Promotion**: A marketing mechanism that offers discounts or incentives.
- **Voucher**: A redeemable code or token providing promotional benefits.
- **Cart**: A temporary container holding products a customer intends to purchase.
- **Checkout**: The process of finalizing a purchase from the cart.
- **Order**: A confirmed purchase request generated after checkout.
- **Shipment**: The physical dispatch of ordered products.
- **Refund**: The return of funds to a customer for a returned or canceled order.

## 6. Product Catalogue Terms

- **Attribute**: A characteristic or property of a product or variant (e.g., color, size).
- **Option**: A selectable attribute category presented to customers.
- **Option Value**: A specific value within an option (e.g., Red for color).
- **Product Media**: Visual or multimedia assets associated with a product.
- **Sellable Product**: A product published and available for purchase.
- **Draft Product**: A product in preparation, not yet published.
- **Published Product**: A product that is visible and available for purchase in the catalogue as determined by the platform's publication rules.

## 7. Customer Terms

- **Address**: A physical location associated with a customer for shipping or billing.
- **Wishlist**: A saved list of products a customer is interested in purchasing later.
- **Consent**: Customer permissions related to data usage and marketing.
- **Preference**: Customer-specific settings or choices influencing experience.
- **Account**: The authenticated profile representing a registered customer.

## 8. Order Terms

- **Order Item**: A specific product or variant included in an order.
- **Order Snapshot**: A record capturing the state of products and prices at the time of order.
- **Fulfilment**: The process of preparing and delivering an order.
- **Cancellation**: The termination of an order before fulfilment.
- **Return**: The process of a customer sending products back post-fulfilment.

## 9. Payment Terms

- **Payment Attempt**: An initiated process to collect payment from a customer.
- **Payment Provider**: A third-party service facilitating payment transactions.
- **Callback**: A provider-initiated server-to-server notification used to communicate payment processing outcomes (such as success, failure, or cancellation) to the platform; distinct from browser redirects as it does not rely on user interaction.
- **Webhook**: An asynchronous event notification delivered over HTTP, enabling external or internal systems to notify the platform of events or status changes.
- **Idempotency**: The property ensuring repeated payment requests do not result in duplicate charges.
- **Settlement**: The final transfer of funds from the payment provider to the merchant.

## 10. Inventory Terms

- **Stock**: The quantity of a product or variant physically available.
- **Reservation**: A temporary hold on stock allocated to a customer or order.
- **Available-to-Sell**: The quantity of stock available for new orders after reservations.
- **Stock Adjustment**: A manual or automated change to stock quantities.
- **Stock Movement**: Any transaction affecting stock, including receipts, sales, or returns.

## 11. Shipping Terms

- **Carrier**: A logistics provider responsible for delivering shipments.
- **Tracking Number**: A unique identifier assigned by the carrier to monitor shipment status.
- **Delivery Method**: The chosen mode or service for shipping products.

## 12. Promotion Terms

- **Promotion**: Defined above; see Commerce Terms.
- **Voucher**: Defined above; see Commerce Terms.

## 13. Administration Terms

- **Role**: A collection of permissions assigned to a user.
- **Permission**: A specific access right or capability granted to roles or users.
- **Audit Record**: A logged entry capturing significant system or user actions.

## 14. Technical Architecture Terms

- **Domain**: A bounded context representing a specific business area.
- **Aggregate**: A cluster of domain objects treated as a single unit for data changes.
- **Entity**: An object with a distinct identity that persists over time.
- **Value Object**: An immutable object defined by its attributes.
- **Use Case**: A business process or scenario implemented in the system.
- **Port**: An interface defining interactions between parts of the system.
- **Adapter**: A component that implements a port to connect to external systems.
- **DTO (Data Transfer Object)**: A data container used to transfer information between layers.
- **Modular Monolith**: A software architecture that structures a monolith into distinct modules.
- **ADR (Architecture Decision Record)**: A document capturing architectural decisions.

## 15. API Terms

- **Resource**: An entity exposed via the API.
- **Endpoint**: A specific URL path representing an API operation.
- **Contract**: The agreed interface specification between API consumers and providers.
- **Versioning**: The practice of managing API changes through version identifiers.

## 16. Database Terms

- **Primary Key**: A unique identifier for a database record.
- **Foreign Key**: A reference linking one record to another.
- **Migration**: A change script modifying database schema or data.
- **Transaction**: A unit of work ensuring atomicity and consistency in a database operation; distinct from a payment transaction, which refers to the movement of funds between parties.
- **Projection**: A read-optimized view or representation of data.

## 17. Security Terms

- **Authentication**: The process of verifying user identity.
- **Authorization**: The process of granting access rights.
- **RBAC (Role-Based Access Control)**: An access control model based on user roles.
- **Principle of Least Privilege**: Granting only the minimum permissions necessary.
- **Secret**: Confidential information such as passwords or API keys.

## 18. DevOps Terms

- **CI (Continuous Integration)**: Automated process of integrating code changes.
- **CD (Continuous Delivery/Deployment)**: Automated process of delivering software to environments.
- **Pipeline**: A sequence of automated steps for building, testing, and deploying.
- **Environment**: A distinct deployment context such as development, staging, or production.
- **Rollback**: Reverting to a previous stable version after a failed deployment.

## 19. AI Development Terms

- **AI Agent**: An autonomous system component that interacts using AI capabilities.
- **Context**: The information and state available to an AI agent during operation.
- **Specification**: A detailed description of requirements or behavior.
- **Prompt**: Input text guiding an AI agent’s response.
- **Steering Document**: A directive guiding AI agent behavior and constraints.

## 20. Repository Terms

- **Standard**: A documented set of rules or guidelines.
- **Specification**: A formal description of system requirements or behavior.
- **Source of Truth**: The authoritative location for definitive information.
- **Baseline**: A reference version or state used for comparison.
- **Repository**: The abstraction for data persistence and retrieval, specifically referring to the Spring Boot Repository interface and its implementations unless otherwise qualified; responsible for managing the persistence of domain entities.

## 21. Identity & Access Terms

- **Identity**: A unique representation of a user or system entity.
- **Principal**: An entity (user or system) authenticated within the system.
- **Session**: A temporary context representing a user's interaction period.
- **Access Token**: A credential granting access to protected resources.
- **Refresh Token**: A token used to obtain new access tokens without reauthentication.
- **MFA (Multi-Factor Authentication)**: A security process requiring multiple verification methods.
- **SSO (Single Sign-On)**: An authentication process allowing access to multiple systems with one login.
- **Claims**: Statements about a user’s identity and attributes.
- **Scope**: The permissions or access boundaries granted by a token.
- **Permission Set**: A defined collection of access rights assigned to a principal.

## 22. Pricing & Tax Terms

- **Base Price**: The original price of a product before discounts or taxes.
- **Sale Price**: The discounted price offered to customers.
- **Tax**: A mandatory financial charge imposed by authorities.
- **VAT (Value Added Tax)**: A consumption tax applied at each stage of production.
- **Tax Inclusive**: Pricing that includes tax within the displayed amount.
- **Tax Exclusive**: Pricing that excludes tax from the displayed amount.
- **Discount**: A reduction applied to the base or sale price.
- **Discount Rule**: Criteria defining when and how discounts apply.
- **Pricing Rule**: Guidelines determining product pricing strategies.
- **Invoice**: A document detailing a transaction and amount due.
- **Credit Note**: A document issued to acknowledge a reduction or refund.

## 23. Returns & Refund Terms

- **Return Request**: A customer-initiated request to return purchased items.
- **Return Authorization**: Approval granted for a return to proceed.
- **Return Window**: The allowable timeframe for returning products.
- **Refund Request**: A request to return funds for a returned or canceled order.
- **Refund Transaction**: The process of reimbursing funds to a customer.
- **Exchange**: The replacement of returned products with alternative items.
- **Restocking**: The process of returning products to inventory.
- **Refund Reason**: The justification provided for issuing a refund.

## 24. Notification Terms

- **Notification**: A message sent to inform users of events or updates.
- **Email Template**: A predefined format for email communications.
- **SMS Notification**: A text message sent to users' mobile devices.
- **Push Notification**: A message delivered to a device via an app or service.
- **Delivery Status**: The current state of a notification's transmission.
- **Retry Policy**: Rules governing attempts to resend failed notifications.
- **Dead Letter Queue**: A queue for messages that cannot be delivered or processed.

## 25. Reporting & Analytics Terms

- **KPI (Key Performance Indicator)**: A measurable value indicating performance.
- **Metric**: Quantitative data used for analysis.
- **Dimension**: A categorical attribute used to segment data.
- **Fact**: A quantitative measurement stored in a data warehouse.
- **Event**: An occurrence or action tracked in the system; business or domain events represent meaningful business changes (e.g., OrderCreated), while technical events relate to system-level actions (e.g., log entry, error).
- **Funnel**: A sequence of steps representing a user journey.
- **Conversion Rate**: The percentage of users completing a desired action.
- **Average Order Value**: The mean value of orders placed.
- **Gross Revenue**: Total revenue before deductions.
- **Net Revenue**: Revenue after deductions such as returns and discounts.

## 26. Angular Terms

- **Standalone Component**: An Angular component that does not require a module.
- **Signal**: A reactive primitive representing a value that changes over time.
- **Computed Signal**: A signal derived from other signals.
- **Effect**: A reactive side-effect triggered by signal changes.
- **Injectable**: A class decorated for dependency injection.
- **Guard**: A service controlling route access.
- **Resolver**: A service retrieving data before route activation.
- **Interceptor**: A service that intercepts HTTP requests or responses.
- **Reactive Form**: A form model driven by reactive programming.
- **Route Configuration**: The setup defining routes and their components.

## 27. Spring Boot Terms

- **Controller**: A component handling HTTP requests and responses.
- **Service**: A class containing business logic.
- **Repository**: A component managing data persistence.
- **Bean**: An object managed by the Spring container.
- **Configuration**: Classes defining Spring context setup.
- **Transactional**: An annotation managing database transaction boundaries.
- **Entity Manager**: The interface for interacting with persistence contexts.
- **JPA (Java Persistence API)**: A specification for object-relational mapping.
- **Spring Profile**: A configuration mechanism for environment-specific settings.

## 28. Azure Terms

- **Azure App Service**: A platform for hosting web applications.
- **Azure PostgreSQL**: A managed PostgreSQL database service.
- **Azure Storage**: Cloud storage services for blobs, files, queues, and tables.
- **Azure Key Vault**: A service for managing secrets and keys.
- **Azure Monitor**: A platform for collecting and analyzing telemetry.
- **Azure Application Insights**: A service for application performance monitoring.
- **Azure Front Door**: A global load balancing and application acceleration service.
- **Azure CDN**: A content delivery network for fast content distribution.

## 29. Search & Discovery Terms

- **Search Index**: A data structure optimized for fast full-text or attribute-based search and retrieval of products or content.
- **Search Query**: A structured request specifying search criteria, filters, and sorting for retrieving relevant results from the search index.
- **Facet**: An attribute or category used to group and filter search results (e.g., brand, color).
- **Filter**: A constraint applied to limit search results based on attributes or values.
- **Sort Order**: The sequence in which search results are presented, determined by specified criteria (e.g., price ascending).
- **Synonym**: Alternative terms or words mapped together to improve search recall and relevance.
- **Autocomplete**: A feature that suggests search queries or product names as the user types.

## 30. Merchandising Terms

- **Featured Product**: A product highlighted for promotional or strategic reasons, often displayed prominently on the platform.
- **Collection Rule**: A configurable condition that determines which products are included in a dynamic collection.
- **Campaign**: A coordinated set of promotional activities and merchandising rules executed within a defined time frame.
- **Badge**: A visual label or marker (e.g., "New", "Bestseller") displayed on products to signal special status or attributes.
- **Product Ranking**: The ordered position of products within a list, collection, or search results, influenced by business rules or algorithms.

## 31. Content Management Terms

- **CMS Page**: A page managed by the Content Management System, typically used for non-product content such as About or FAQ.
- **Banner**: A graphical or text-based promotional element displayed on a page or section.
- **Hero**: A prominent visual or message, usually at the top of a page, designed to capture user attention.
- **Landing Page**: A dedicated page designed for a specific marketing or campaign purpose.
- **Content Block**: A modular section of content that can be reused or placed within CMS pages.

## 32. Localization Terms

- **Locale**: A combination of language and regional settings (e.g., en-US) that determines formatting, translation, and content.
- **Currency**: The unit of monetary exchange configured for pricing and transactions (e.g., USD, EUR).
- **Time Zone**: The regional time offset applied to timestamps and scheduling.
- **Translation**: The process or result of rendering content in another language.
- **Regional Catalogue**: A product catalogue tailored to a specific country, region, or locale, reflecting local assortment, pricing, and regulations.

## 33. Messaging Terms

- **Message**: A discrete unit of data or instruction sent between services or systems.
- **Queue**: A FIFO structure used to store and deliver messages asynchronously.
- **Topic**: A named channel to which messages are published and from which subscribers receive messages.
- **Publisher**: A component or service that sends messages to a queue or topic.
- **Subscriber**: A component or service that receives and processes messages from a queue or topic.
- **Event Bus**: An architectural mechanism for distributing events across decoupled components or services.

## 34. Observability Terms

- **Log**: A record of events, errors, or informational messages generated by the system.
- **Metric**: A numerical measurement collected over time to monitor system health or performance.
- **Trace**: A record of the execution path of a request or transaction across system components.
- **Span**: A single operation or segment within a trace, representing a unit of work.
- **Correlation ID**: A unique identifier used to link related logs, traces, or events across distributed systems.
- **Health Check**: An automated probe or test to determine the operational status of a component or service.

## 35. Performance Terms

- **Cache**: A temporary storage layer used to speed up data retrieval by storing frequently accessed data.
- **Cache Invalidation**: The process of removing or updating cached data when the underlying source changes.
- **CDN Cache**: A cache maintained by a Content Delivery Network to distribute static assets closer to users.
- **Lazy Loading**: A strategy of loading resources or data only when they are needed, reducing initial load time.
- **Pagination**: The division of large result sets into discrete pages to improve performance and usability.

## 36. Testing Terms

- **Unit Test**: A test focusing on a single component or function.
- **Integration Test**: A test verifying interactions between components.
- **Component Test**: A test of an individual UI or functional component.
- **Contract Test**: A test ensuring API compatibility between services.
- **End-to-End Test**: A test simulating user workflows across the system.
- **Smoke Test**: A preliminary test to check basic functionality.
- **Regression Test**: A test ensuring new changes do not break existing features.
- **Test Fixture**: The setup required for running tests.
- **Mock**: A simulated object mimicking real behavior.
- **Stub**: A minimal implementation returning fixed responses.

## 37. Lifecycle Vocabulary

### Product Lifecycle

- **Draft**: An initial, unpublished state of a product.
- **Pending Review**: Product awaiting approval or feedback.
- **Published**: Product is visible and available for purchase according to publication rules.
- **Active**: Product is currently available for sale.
- **Inactive**: Product is not available for sale.
- **Archived**: Product is stored for historical reference, no longer active.

### Order Lifecycle

- **Pending Payment**: Order has been created and is awaiting payment.
- **Paid**: Payment for the order has been received.
- **Fulfilled**: Order has been picked, packed, and shipped to the customer.
- **Cancelled**: Order has been terminated prior to fulfilment.
- **Refunded**: Payment for the order has been returned to the customer.
- **Archived**: Order is closed and stored for historical reference.

### Payment Lifecycle

- **Initiated**: Payment process has started.
- **Pending Confirmation**: Awaiting confirmation from payment provider.
- **Confirmed**: Payment was successful.
- **Failed**: Payment was unsuccessful.
- **Refunded**: Payment has been returned to the customer.

### Shipment Lifecycle

- **Created**: Shipment has been arranged and is pending dispatch.
- **In Transit**: Shipment is with the carrier and en route to the customer.
- **Delivered**: Shipment has been delivered to the customer.
- **Returned**: Shipment has been returned to the merchant.

### Refund Lifecycle

- **Requested**: Refund has been initiated by the customer or system.
- **Authorized**: Refund request has been approved.
- **Completed**: Refund transaction has been processed and funds returned.

## 38. Domain Event Vocabulary

- **ProductCreated**: Emitted when a new product entity is created in the system.
- **ProductPublished**: Emitted when a product transitions to the published state and becomes available for purchase.
- **InventoryReserved**: Emitted when stock is reserved for a customer or order, reducing available-to-sell quantity.
- **InventoryReleased**: Emitted when previously reserved stock is released back to inventory.
- **CartCreated**: Emitted when a new shopping cart is created for a customer or visitor.
- **CheckoutStarted**: Emitted when a customer initiates the checkout process from the cart.
- **PaymentInitiated**: Emitted when the payment process for an order is started.
- **PaymentConfirmed**: Emitted when payment is successfully processed and confirmed by the payment provider.
- **OrderCreated**: Emitted when an order is placed after successful checkout and payment initiation.
- **OrderCancelled**: Emitted when an order is cancelled before fulfilment.
- **ShipmentCreated**: Emitted when a shipment is arranged for an order or part of an order.
- **ShipmentDelivered**: Emitted when the carrier confirms delivery of a shipment to the customer.
- **RefundCompleted**: Emitted when a refund transaction is finalized and funds are returned to the customer.

## 39. Repository Naming Rules

- Event names use PascalCase and past tense.
- DTOs end with `Dto`.
- Requests end with `Request`.
- Responses end with `Response`.
- Controllers end with `Controller`.
- Services end with `Service`.
- Repositories end with `Repository`.
- Specification filenames use kebab-case.
- ADRs use `ADR-XXXX-title.md`.
- Database tables use snake_case.
- API paths use lowercase and hyphens.
- Domain events are immutable.
- Enums use singular PascalCase.
- Interfaces begin with I only when required by framework conventions; otherwise, avoid prefixes.
- Environment variables use UPPER_SNAKE_CASE.
- Feature flags use dot notation (e.g., feature.checkout.express).
- Database indexes use idx*<table>*<columns> (e.g., idx_order_created_at).

## 40. Cross-Reference Rules

All core documents—including PRODUCT.md, ARCHITECTURE.md, AGENTS.md, ADRs, domain specifications, API documentation, and code comments—must use glossary-defined terminology consistently. Where a glossary term exists, it should be referenced verbatim to avoid ambiguity. Glossary alignment is mandatory for all new documentation, specifications, and code artifacts. Discrepancies must be resolved via glossary updates.

## 41. Preferred Terminology

| Preferred         | Avoid           |
| ----------------- | --------------- |
| Product           | Item            |
| Customer          | User            |
| Staff User        | Admin           |
| Variant           | Product Type    |
| Order Item        | Line            |
| Published         | Live            |
| Available-to-Sell | Available Stock |
| Payment Attempt   | Transaction     |
| Aggregate         | Object Group    |
| Domain Event      | Notification    |

## 42. Forbidden Terminology

Ambiguous or imprecise terminology is prohibited in specifications to avoid misunderstandings. The following table outlines common terms to avoid and recommended alternatives:

| Avoid           | Use Instead                                   | Reason                                          |
| --------------- | --------------------------------------------- | ----------------------------------------------- |
| Product Type    | Category or Variant                           | "Product Type" is ambiguous; use specific terms |
| User            | Customer or Staff User                        | "User" is generic; specify role for clarity     |
| Item            | Product or Order Item                         | "Item" is vague; clarify domain context         |
| Status OK       | Explicit lifecycle state                      | "OK" is unclear; use defined states             |
| Database Object | Entity or Record                              | Ambiguous; specify data model concept           |
| Admin           | Store Administrator or Platform Administrator | Clarifies administrative scope                  |

## 43. Glossary Governance

- All new terminology must be introduced in this glossary before use elsewhere.
- Existing definitions are immutable without explicit versioned updates.
- Domain-specific specifications may refine definitions but must not contradict this glossary.
- AI agents and automated systems must adhere strictly to glossary terminology to ensure consistency and traceability.
- Every new domain specification must use glossary terminology.
- New terms require glossary approval before adoption.
- AI prompts should reference glossary terminology where ambiguity exists.
- Terminology changes require a version bump and revision history entry.

---

## Revision History

| Version | Date       | Description                                                                                                                                                                                                                                                                                      | Author                  |
| ------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------- |
| 0.3.0   | 2026-08-05 | Substantially expanded and refined definitions; added Search & Discovery, Merchandising, Content Management, Localization, Messaging, Observability, and Performance sections; grouped lifecycle states; clarified domain events; strengthened naming and cross-reference rules for consistency. | Engineering and Product |
| 0.2.0   | 2026-08-05 | Expanded the glossary with identity, pricing, tax, returns, notifications, analytics, Angular, Spring Boot, Azure, testing, lifecycle, domain event, naming convention, and terminology governance sections.                                                                                     | Engineering and Product |
