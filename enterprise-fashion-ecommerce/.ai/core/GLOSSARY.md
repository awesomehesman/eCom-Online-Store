---
title: GLOSSARY
version: 1.1.0
status: Approved
owner: Engineering and Product
last_updated: 2026-08-12
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

- Use PascalCase for types, classes, enums, domain events, and named domain concepts (e.g., `ProductCreated`).
- Use camelCase for variables, methods, attributes, properties, and JSON fields.
- Use UPPER_SNAKE_CASE for environment variables and compile-time constants where the language convention requires it.
- Use snake_case for database tables, columns, constraints, and indexes.
- Use kebab-case for API path segments, specification filenames, and human-readable repository filenames unless another repository rule explicitly applies.
- Preserve the official casing of established acronyms and technology names (e.g., API, SKU, DTO, JPA, Azure PostgreSQL).
- Avoid abbreviations unless they are widely recognized and defined in this glossary.

## 4. Business Terms

- **Merchant**: An entity or individual authorized to sell products on the platform.
- **Customer**: A user who purchases or intends to purchase products.
- **Visitor**: An unauthenticated user browsing the platform.
- **Registered Customer**: A customer with an authenticated account.
- **Staff User**: An internal user with access to administrative or operational functions.
- **Product Owner**: A delivery and product-management role accountable for product vision, priorities, outcomes, and backlog decisions; this term does not mean the owner of a catalogue Product.
- **Domain Owner**: The designated authority responsible for a specific business domain or bounded context.
- **Store Administrator**: A Staff User responsible for day-to-day catalogue, order, promotion, customer-service, and operational administration within an assigned store scope.
- **Platform Administrator**: A privileged Staff User responsible for platform-wide configuration, access governance, tenant or store provisioning, and operational oversight.

## 5. Commerce Terms

- **Catalogue**: The organized collection of products and related information available for sale.
- **Collection**: A curated grouping of products within the catalogue.
- **Category**: A classification within the catalogue used to group similar products.
- **Product**: A sellable item defined by a set of attributes and Product Variants.
- **Product Variant**: A purchasable or selectable variation of a Product that may differ by size, color, or other Product-defined variation dimensions; it is distinct from the Product, its SKU, Inventory, and Stock.
- **SKU (Stock Keeping Unit)**: A unique identifier assigned to a Product Variant for Inventory tracking.
- **Price**: A monetary amount expressed as Money and assigned to a Product or Product Variant under a defined Pricing Rule.
- **Cart**: A temporary container holding products a customer intends to purchase.
- **Checkout**: The process of finalizing a purchase from the cart.
- **Order**: A durable commercial record created from Checkout that captures the Customer, Order Items, prices, taxes, discounts, payment state, fulfilment state, and delivery details.
- **Shipment**: The physical dispatch of ordered products.
- **Refund**: The approved return of previously captured funds to a Customer through a Refund Transaction.
- **Storefront**: The customer-facing web experience used to discover Products, manage a Cart, complete Checkout, and manage an Account.
- **Back Office**: The Staff User-facing application used to administer catalogue, inventory, pricing, promotions, orders, returns, content, reporting, and platform operations.
- **Money**: A value object composed of an amount and ISO 4217 Currency code; monetary calculations must not use floating-point arithmetic.

## 6. Product Catalogue Terms

- **Attribute**: A characteristic or property of a Product or Product Variant (e.g., color, size).
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

- **Order Item**: A specific Product or Product Variant included in an Order.
- **Order Snapshot**: A record capturing the state of products and prices at the time of order.
- **Fulfilment**: The operational process of allocating stock, picking, packing, dispatching, and completing delivery or collection for one or more Order Items.
- **Cancellation**: The controlled termination of an Order or eligible Order Items before the applicable fulfilment or payment point of no return.
- **Return**: The post-delivery process through which a Customer sends eligible Order Items back under an approved Return Authorization.
- **Order Number**: A customer-facing, non-sensitive reference used to identify an Order; it is distinct from the internal primary key.
- **Fulfilment Group**: A grouping of Order Items that share a delivery method, fulfilment location, shipment, or collection arrangement.

## 9. Payment Terms

- **Payment**: The Payment domain's record and lifecycle for collecting, confirming, reconciling, settling, or reversing money for a commerce obligation; authoritative Payment state that depends on a provider outcome requires validated Payment Provider evidence and is distinct from any individual Payment Attempt or Payment Transaction.
- **Payment Attempt**: An initiated process to collect payment from a customer.
- **Payment Provider**: A third-party service facilitating Payment Transactions.
- **Callback**: A provider-initiated server-to-server notification used to communicate payment processing outcomes (such as success, failure, or cancellation) to the platform; distinct from browser redirects as it does not rely on user interaction.
- **Webhook**: An asynchronous event notification delivered over HTTP, enabling external or internal systems to notify the platform of events or status changes.
- **Idempotency**: The property ensuring repeated payment requests do not result in duplicate charges.
- **Settlement**: The final transfer of funds from the payment provider to the merchant.
- **Payment Transaction**: A provider-recorded financial operation such as Payment Authorization, Capture, Void, Refund, or Chargeback; distinct from a Database Transaction.
- **Payment Authorization**: A Payment Provider approval that reserves funds or confirms payment capability without necessarily transferring funds to the Merchant; distinct from access-control Authorization.
- **Capture**: The operation that finalizes a Payment Authorization for Settlement.
- **Void**: The cancellation of an uncaptured Payment Authorization.
- **Chargeback**: A payment reversal initiated through the Customer's issuing bank or card network and disputed against the Merchant.
- **Payment Redirect**: A browser navigation that sends the Customer to or from a Payment Provider; it is not authoritative proof of payment outcome.

## 10. Inventory Terms

- **Inventory**: The domain capability and authoritative record of Stock, Stock Reservations, Available-to-Sell quantities, Stock Adjustments, and Stock Movements for Product Variants at Stock Locations.
- **Stock**: The recorded on-hand quantity of a Product Variant at a Stock Location.
- **Stock Reservation**: An Inventory-owned temporary reservation of Stock associated with a commerce operation such as Checkout or Order processing, subject to the lifecycle and ownership rules of the owning domain.
- **Available-to-Sell**: The quantity of Stock available for new commerce commitments after applicable active Stock Reservations, safety stock, and other committed quantities are considered.
- **Stock Adjustment**: A manual or automated change to Stock quantities.
- **Stock Movement**: A recorded change to Stock, including receipts, sales, or returns.
- **Stock Location**: A physical or logical location at which Stock is recorded, reserved, received, adjusted, or fulfilled.
- **Safety Stock**: A configured quantity withheld from Available-to-Sell to reduce overselling risk.
- **Overselling**: Accepting demand for a Product Variant beyond its valid Available-to-Sell quantity.

## 11. Shipping Terms

- **Carrier**: A logistics provider responsible for delivering shipments.
- **Tracking Number**: A unique identifier assigned by the carrier to monitor shipment status.
- **Delivery Method**: The chosen mode or service for shipping products.
- **Shipping Address**: The destination Address selected for delivery of a Shipment.
- **Shipping Rate**: The charge calculated for a Delivery Method under defined destination, parcel, value, and service rules.
- **Dispatch**: The handover of a packed Shipment to a Carrier or collection process.
- **Delivery Estimate**: A non-guaranteed date or date range calculated for dispatch or delivery.

## 12. Promotion Terms

- **Promotion**: A time-bound or condition-bound commercial incentive governed by eligibility, benefit, priority, combinability, usage, and validity rules.
- **Voucher**: A redeemable code or token that activates an eligible Promotion during Cart or Checkout evaluation.
- **Promotion Eligibility**: The conditions that a Customer, Cart, Product, Product Variant, Order Item, channel, or date range must satisfy for a Promotion to apply.
- **Promotion Benefit**: The value granted by a Promotion, such as a fixed discount, percentage discount, free shipping, or qualifying Product benefit.
- **Promotion Combination**: The rule determining whether multiple Promotions or Vouchers may apply together.

## 13. Administration Terms

- **Role**: A named collection of Permissions assigned to a Principal through the platform's Authorization model.
- **Permission**: A specific access right or capability granted to a Principal through a Role or an approved Authorization policy.
- **Audit Record**: A logged entry capturing significant system or user actions.

## 14. Technical Architecture Terms

- **Domain**: A defined business problem space. A Bounded Context is the explicit model boundary within which domain terminology and rules are consistent.
- **Aggregate**: A consistency boundary containing an Aggregate Root and related domain objects that must be changed through the Aggregate Root.
- **Aggregate Root**: The Entity that controls access to an Aggregate and enforces its invariants.
- **Bounded Context**: An explicit boundary within which a domain model, vocabulary, rules, and ownership are internally consistent.
- **Module**: A cohesive, independently testable unit within the Modular Monolith that encapsulates a Bounded Context or a well-defined technical capability.
- **Invariant**: A business rule that must remain true before and after a valid state transition.
- **Domain Service**: Stateless domain logic that does not naturally belong to a single Entity or Value Object.
- **Application Service**: A use-case coordinator that invokes domain behavior, persistence ports, and external adapters without owning core business rules.
- **Entity**: An object with a distinct identity that persists over time.
- **Value Object**: An immutable object defined by its attributes.
- **Use Case**: A business process or scenario implemented in the system.
- **Port**: An inbound or outbound interface owned by the application or domain boundary that defines an allowed interaction.
- **Adapter**: An implementation that connects a Port to a delivery mechanism, persistence technology, or external system.
- **DTO (Data Transfer Object)**: A data container used to transfer information between layers.
- **Modular Monolith**: A software architecture that structures a monolith into distinct modules.
- **ADR (Architecture Decision Record)**: A Decision Record for a material Architecture Decision; it is not the generic record type for non-Architecture Decisions.
- **Domain Model**: The representation of business concepts, rules, behaviours, and relationships within a Bounded Context.
- **Domain Event**: An immutable record of a completed business fact within a Bounded Context, named in past tense and distinct from an Analytics Event, technical event, or cross-boundary Integration Event.
- **Ubiquitous Language**: The shared business vocabulary used consistently by domain experts, Product, Engineering, documentation, code, and AI Agents.
- **Context Map**: A documented view of the relationships, dependencies, translation boundaries, and integration patterns between Bounded Contexts.
- **Shared Kernel**: A deliberately shared subset of a Domain Model jointly owned and governed by multiple Bounded Contexts.
- **Anti-Corruption Layer**: A translation boundary that protects one Domain Model from the concepts, terminology, and implementation details of another system.
- **Factory**: A domain component responsible for creating valid Aggregates, Entities, or Value Objects while preserving construction rules and invariants.
- **Specification Pattern**: A reusable, composable representation of a business rule used for validation, eligibility, selection, or filtering.
- **Domain Policy**: A business rule or decision that spans multiple Aggregates and does not naturally belong to a single Entity or Value Object.

## 15. Hexagonal Architecture Terms

- **Inbound Port**: An application-owned interface that defines how an external actor may invoke a Use Case.
- **Outbound Port**: An application-owned interface that defines a capability the application requires from persistence, messaging, infrastructure, or an External System.
- **Primary Adapter**: An Adapter that translates an external interaction, such as HTTP, messaging, CLI, or scheduled execution, into a call to an Inbound Port.
- **Secondary Adapter**: An Adapter that implements an Outbound Port using a database, Message broker, file system, third-party API, or infrastructure service.
- **Driving Adapter**: A synonym for Primary Adapter; it initiates interaction with the application through an Inbound Port.
- **Driven Adapter**: A synonym for Secondary Adapter; it is invoked by the application through an Outbound Port.

## 16. API Terms

- **Resource**: An entity exposed via the API.
- **Endpoint**: A specific URL path representing an API operation.
- **Contract**: The agreed interface specification between API consumers and providers.
- **Versioning**: The practice of managing API changes through version identifiers.
- **Request DTO**: A DTO representing validated input accepted by an API operation; its type name ends with `Request` or `RequestDto` according to the applicable API standard.
- **Response DTO**: A DTO representing output returned by an API operation; its type name ends with `Response` or `ResponseDto` according to the applicable API standard.
- **Problem Details**: The standardized error response model based on RFC 9457 fields and repository-defined extensions.
- **Idempotency Key**: A client-supplied unique key used to make a retried state-changing API request safe from duplicate processing.
- **Cursor Pagination**: Pagination that uses an opaque continuation cursor rather than a page number for stable traversal of changing datasets.

## 17. Database Terms

- **Primary Key**: A unique identifier for a database record.
- **Foreign Key**: A reference linking one record to another.
- **Migration**: A change script modifying database schema or data.
- **Database Transaction**: A database unit of work with defined atomicity and consistency boundaries; distinct from a Payment Transaction.
- **Projection**: A read-optimized view or representation of data.
- **Optimistic Locking**: A concurrency-control strategy that rejects an update when the persisted version has changed since it was read.
- **Soft Delete**: Marking a record as logically deleted while retaining it for audit, recovery, or referential purposes; use only where explicitly required.
- **Outbox Pattern**: Persisting a Domain Event record in the same Database Transaction as the Aggregate change so it can be published reliably after commit.
- **Unique Constraint**: A database rule preventing duplicate values for a defined column or column combination.

## 18. Security Terms

- **Authentication**: The process of verifying user identity.
- **Authorization**: The access-control decision that determines whether a Principal may perform an action on a Resource; distinct from Payment Authorization.
- **RBAC (Role-Based Access Control)**: An access control model based on user roles.
- **Principle of Least Privilege**: Granting only the minimum permissions necessary.
- **Secret**: Confidential information such as passwords or API keys.
- **Cryptographic Key**: Secret or public cryptographic material used by an approved algorithm for encryption, decryption, signing, signature verification, or message authentication.
- **Certificate**: A digitally signed credential that binds an Identity, service, domain name, or cryptographic public key to an issuer and defined validity period.
- **PII (Personally Identifiable Information)**: Information that identifies or can reasonably be linked to a natural person.
- **Sensitive Data**: Data requiring enhanced protection due to legal, contractual, security, financial, or privacy impact.
- **Encryption at Rest**: Protection of persisted data using encryption managed by the storage platform or application.
- **Encryption in Transit**: Protection of data exchanged over networks using approved transport encryption such as TLS.
- **Audit Trail**: A tamper-evident sequence of Audit Records sufficient to reconstruct material security, administrative, and commercial actions.
- **Security Exception**: An approved, time-bound, auditable waiver of a mandatory security requirement that records the affected requirement and systems, risk, compensating controls, accountable owner, approver, expiry, and remediation plan.

## 19. DevOps Terms

- **CI (Continuous Integration)**: Automated process of integrating code changes.
- **CD (Continuous Delivery/Deployment)**: Automated process of delivering software to environments.
- **Pipeline**: A sequence of automated steps for building, testing, and deploying.
- **Environment**: A distinct deployment context such as development, staging, or production.
- **Rollback**: Reverting to a previous stable version after a failed deployment.

## 20. AI Development Terms

- **AI Agent**: An autonomous system component that interacts using AI capabilities.
- **Context**: The information and state available to an AI agent during operation.
- **Prompt**: Input text guiding an AI agent’s response.
- **Steering Document**: A directive guiding AI agent behavior and constraints.
- **Context Hierarchy**: The ordered set of repository instructions that an AI Agent must resolve before acting, from global governance through domain and feature-specific specifications.
- **Guardrail**: A mandatory constraint that limits AI Agent behavior, generated changes, or tool usage.
- **Human Approval Gate**: A workflow point at which an authorized person must review or approve an AI-produced decision or change before continuation.
- **Requirement**: A verifiable capability, behaviour, quality attribute, or outcome that the system or process must satisfy.
- **Constraint**: A mandatory limitation on design, implementation, operation, technology, compliance, or delivery choices.
- **Assumption**: A condition treated as true for planning or design purposes until it is validated, rejected, or replaced.
- **Decision**: A recorded choice between alternatives together with its rationale, consequences, and ownership.
- **Decision Record**: A durable repository record of a material Decision and its context, rationale, consequences, ownership, status, and affected concerns.
- **Architecture Decision**: A material Decision about Architecture whose durable Decision Record is an ADR.
- **Acceptance Criterion**: A specific, testable condition that must be satisfied for a Requirement, feature, or task to be accepted.
- **Risk**: An uncertain event or condition that may negatively affect scope, quality, security, cost, schedule, reliability, or business outcomes.
- **RFC (Request for Comments)**: A structured proposal circulated for review before a significant technical, product, process, or governance decision is finalized.

## 21. Repository Terms

- **Standard**: A documented set of rules or guidelines.
- **Testing Exception**: An approved, time-bound, auditable waiver of a mandatory testing requirement governed by `TESTING-STANDARDS.md`; it does not waive a mandatory security requirement.
- **Coding Exception**: An approved, time-bound, auditable waiver of a mandatory coding requirement governed by `CODING-STANDARDS.md`; it does not waive a mandatory security requirement.
- **Documentation Exception**: An approved, time-bound, auditable waiver of a mandatory documentation requirement governed by `DOCUMENTATION-STANDARDS.md`; it does not waive a requirement owned by another governing source.
- **Specification**: A formal description of system requirements or behavior.
- **Source of Truth**: The authoritative location for definitive information.
- **Baseline**: A reference version or state used for comparison.
- **Code Repository**: The version-controlled collection of source code, documentation, configuration, tests, and delivery assets for this platform.
- **Canonical Document**: The approved Source of Truth for a defined area of repository governance or system behavior.
- **Normative Rule**: A mandatory requirement expressed using terms such as must, must not, required, or prohibited.
- **Guideline**: A recommended practice that may be departed from when the reason is documented and approved.
- **Proposed**: A document or change state indicating that content has been submitted for review but is not yet authoritative.
- **Approved**: A document or change state indicating that authorized reviewers have accepted the content as normative and enforceable.
- **Deprecated**: A supported but discouraged term, feature, interface, or document that is scheduled for replacement or removal.
- **Experimental**: A non-guaranteed state used for evaluation where behaviour, interfaces, or terminology may change without backward compatibility.
- **Breaking Change**: A change that requires consumers, integrations, data, code, configuration, or operating procedures to be updated to continue functioning correctly.
- **Non-Breaking Change**: A backward-compatible change that does not require existing consumers to modify their current usage.
- **Semantic Version**: A version identifier in `MAJOR.MINOR.PATCH` form where MAJOR indicates breaking changes, MINOR indicates backward-compatible additions, and PATCH indicates backward-compatible fixes or clarifications.
- **Repository Steward**: The person or group responsible for maintaining repository governance, consistency, review discipline, and the health of canonical documents.

## 22. Identity & Access Terms

- **Identity**: A persistent representation of a human or system actor recognized by the platform.
- **Principal**: The authenticated human or system actor associated with the current security context.
- **Session**: A bounded authenticated interaction context for a Principal with defined creation, expiry, revocation, and renewal behavior.
- **Access Token**: A credential granting a Principal delegated access to protected Resources within its Claims and Scope.
- **Refresh Token**: A token used to obtain new Access Tokens without repeating interactive Authentication.
- **MFA (Multi-Factor Authentication)**: An Authentication process requiring multiple independent verification factors.
- **SSO (Single Sign-On)**: An Authentication process that allows a Principal to access multiple systems through one Identity Provider sign-in.
- **Claims**: Verified statements about a Principal issued by an Identity Provider and carried in or resolved from a security token.
- **Scope**: A delegated Authorization boundary granted to a token or client; it is distinct from a Role or Permission.
- **Identity Provider**: The trusted service that authenticates a Principal and issues identity tokens or Access Tokens.
- **Service Principal**: A non-human Identity used by an application, workload, automation, or integration.
- **Token Revocation**: The invalidation of an Access Token, Refresh Token, or Session before normal expiry.

## 23. Pricing & Tax Terms

- **Base Price**: The standard selling Price before Promotion benefits, manual markdowns, or campaign-specific adjustments; tax treatment is defined separately.
- **Sale Price**: A currently effective selling Price lower than the Base Price under a markdown, Campaign, or Pricing Rule.
- **Tax**: A mandatory financial charge imposed by authorities.
- **VAT**: Value Added Tax applied according to the configured tax jurisdiction, product tax classification, invoice rules, and effective rate; for the initial South African market, VAT handling must follow approved local requirements.
- **Tax Inclusive**: Pricing that includes tax within the displayed amount.
- **Tax Exclusive**: Pricing that excludes tax from the displayed amount.
- **Discount**: A monetary reduction calculated from an eligible amount and recorded separately from Base Price, Sale Price, Tax, and shipping charges.
- **Discount Rule**: Criteria defining when and how discounts apply.
- **Pricing Rule**: Guidelines determining product pricing strategies.
- **Invoice**: A legally and commercially relevant document recording the seller, Customer, supplied goods or services, amounts, Tax, payment references, and required jurisdictional details.
- **Credit Note**: A document that reduces all or part of a previously issued Invoice and records the reason, affected lines, Tax adjustment, and amount credited.
- **Tax Jurisdiction**: The country, region, or authority whose tax rules apply to a transaction.
- **Tax Category**: The classification used to determine the applicable tax treatment for a Product, Delivery Method, discount, or fee.
- **Price List**: A named set of Prices applicable to a Currency, region, channel, Customer segment, or validity period.

## 24. Returns & Refund Terms

- **Return Request**: A customer-initiated request to return purchased items.
- **Return Authorization**: Approval granted for a return to proceed.
- **Return Window**: The allowable timeframe for returning products.
- **Refund Request**: A request to return funds for a returned or canceled order.
- **Refund Transaction**: A Payment Transaction that returns an approved amount against a prior captured payment.
- **Exchange**: The replacement of returned products with alternative items.
- **Restocking**: The inspected and approved movement of returned Stock back into a sellable or non-sellable Stock Location and condition.
- **Refund Reason**: The justification provided for issuing a refund.
- **Return Item**: An Order Item quantity included in a Return Request together with condition, reason, evidence, and requested resolution.
- **Return Disposition**: The operational outcome assigned after inspection, such as restock, quarantine, repair, supplier return, or disposal.
- **Partial Refund**: A Refund for less than the total captured amount of the related Order or Payment Transaction.

## 25. Notification Terms

- **Notification**: A message sent to inform users of events or updates.
- **Email Template**: A predefined format for email communications.
- **SMS Notification**: A text message sent to users' mobile devices.
- **Push Notification**: A message delivered to a device via an app or service.
- **Delivery Status**: The current state of a notification's transmission.
- **Retry Policy**: Rules governing attempts to resend failed notifications.
- **Dead Letter Queue**: A queue for messages that cannot be delivered or processed.
- **Notification Channel**: The delivery medium used for a Notification, such as email, SMS, push, or an in-application message.
- **Notification Preference**: A Customer or Staff User choice controlling eligible non-mandatory Notification Channels or topics.
- **Transactional Notification**: A service message required to communicate an account, payment, order, shipment, return, refund, or security event; it is distinct from marketing communication.

## 26. Reporting & Analytics Terms

- **KPI (Key Performance Indicator)**: A measurable value indicating performance.
- **Metric**: A named quantitative measurement with an explicit formula, unit, aggregation method, time grain, filters, and Source of Truth.
- **Dimension**: A categorical attribute used to segment data.
- **Fact**: A quantitative measurement stored in a data warehouse.
- **Event**: An occurrence or action tracked in the system; business or domain events represent meaningful business changes (e.g., OrderCreated), while technical events relate to system-level actions (e.g., log entry, error).
- **Funnel**: A sequence of steps representing a user journey.
- **Conversion Rate**: The percentage of users completing a desired action.
- **Average Order Value**: The mean value of orders placed.
- **Gross Revenue**: The sum of recognized merchandise and applicable service revenue before discounts, returns, refunds, chargebacks, and specified deductions, according to the approved reporting formula.
- **Net Revenue**: Gross Revenue less the deductions defined by the approved reporting formula; the formula must state treatment of Tax, shipping, discounts, refunds, returns, and chargebacks.
- **Analytics Event**: A structured observation emitted for measurement of behavior or system outcomes; it must not be confused with a Domain Event used for business state propagation.
- **Measure**: A numeric field that can be aggregated in reporting, such as quantity, amount, duration, or count.
- **Attribution**: The rule used to assign a conversion or commercial outcome to a marketing source, Campaign, channel, or interaction.

## 27. Angular Terms

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

## 28. Frontend Architecture Terms

- **Page**: A routable customer-facing or Staff User-facing screen that composes Layouts, Views, Components, and application state for a complete user task or destination.
- **Layout**: A reusable structural shell that defines persistent page regions such as navigation, header, footer, sidebars, or content containers.
- **View**: A presentation-focused composition that renders data and interactions for part of a Page without owning cross-cutting application responsibilities.
- **Design System**: The governed collection of design principles, tokens, accessibility rules, interaction patterns, and reusable UI standards for the platform.
- **Component Library**: The implemented set of reusable UI Components that conforms to the Design System.
- **Theme**: A named visual configuration of design tokens such as colour, typography, spacing, elevation, and component appearance.
- **Responsive Breakpoint**: A defined viewport threshold at which Layout or Component behaviour changes to preserve usability across device sizes.
- **Client State**: State created or owned by the frontend, such as UI selections, local workflow progress, or temporary interaction state.
- **Server State**: Data owned by backend systems and cached, synchronized, invalidated, or refreshed by the frontend.

## 29. Spring Boot Terms

- **Controller**: A component handling HTTP requests and responses.
- **Service**: A Spring-managed component implementing an Application Service, Domain Service, or technical integration capability; use the more specific term whenever the distinction matters.
- **Repository**: A Spring persistence abstraction that loads and stores Aggregate Roots or persistence models while hiding database access details; the term Code Repository must be used for the Git repository.
- **Bean**: An object managed by the Spring container.
- **Configuration**: Classes defining Spring context setup.
- **Transactional**: The Spring transaction-boundary declaration used to execute database work atomically; it must not be used as a synonym for Payment Transaction.
- **Entity Manager**: The interface for interacting with persistence contexts.
- **JPA (Java Persistence API)**: A specification for object-relational mapping.
- **Spring Profile**: A configuration mechanism for environment-specific settings.

## 30. Backend Architecture Terms

- **Command**: A request to perform a state-changing Use Case.
- **Query**: A request to retrieve data without intentionally changing business state.
- **Command Handler**: An application component that validates and coordinates execution of a Command.
- **Query Handler**: An application component that executes a Query and returns a read model or Response DTO.
- **Validator**: A component that checks input, state, or rules and returns or raises explicit validation outcomes.
- **Mapper**: A component that converts data between representations such as API DTOs, domain objects, persistence models, and integration contracts.
- **Exception Handler**: A boundary component that translates Exceptions into consistent API, Message, operational, or user-facing error outcomes.
- **Domain Exception**: An Exception representing violation of a business rule or invalid domain state.
- **Business Rule**: A policy, condition, calculation, or invariant that expresses required business behaviour.
- **Background Job**: A non-interactive unit of work executed asynchronously outside a direct request-response interaction.
- **Scheduler**: A component that triggers a Background Job or process according to a defined time, interval, or calendar rule.

## 31. Azure Terms

- **Azure App Service**: A platform for hosting web applications.
- **Azure PostgreSQL**: A managed PostgreSQL database service.
- **Azure Storage**: Cloud storage services for blobs, files, queues, and tables.
- **Azure Key Vault**: A service for managing secrets and keys.
- **Azure Monitor**: A platform for collecting and analyzing telemetry.
- **Azure Application Insights**: A service for application performance monitoring.
- **Azure Front Door**: A global load balancing and application acceleration service.
- **Azure CDN**: A content delivery network for fast content distribution.

## 32. Search & Discovery Terms

- **Search Index**: A data structure optimized for fast full-text or attribute-based search and retrieval of products or content.
- **Search Query**: A structured request specifying search criteria, filters, and sorting for retrieving relevant results from the search index.
- **Facet**: An attribute or category used to group and filter search results (e.g., brand, color).
- **Filter**: A constraint applied to limit search results based on attributes or values.
- **Sort Order**: The sequence in which search results are presented, determined by specified criteria (e.g., price ascending).
- **Synonym**: Alternative terms or words mapped together to improve search recall and relevance.
- **Autocomplete**: A feature that suggests search queries or product names as the user types.

## 33. Merchandising Terms

- **Featured Product**: A product highlighted for promotional or strategic reasons, often displayed prominently on the platform.
- **Collection Rule**: A configurable condition that determines which products are included in a dynamic collection.
- **Campaign**: A coordinated set of promotional activities and merchandising rules executed within a defined time frame.
- **Badge**: A visual label or marker (e.g., "New", "Bestseller") displayed on products to signal special status or attributes.
- **Product Ranking**: The ordered position of products within a list, collection, or search results, influenced by business rules or algorithms.

## 34. Content Management Terms

- **CMS Page**: A page managed by the Content Management System, typically used for non-product content such as About or FAQ.
- **Banner**: A graphical or text-based promotional element displayed on a page or section.
- **Hero**: A prominent visual or message, usually at the top of a page, designed to capture user attention.
- **Landing Page**: A dedicated page designed for a specific marketing or campaign purpose.
- **Content Block**: A modular section of content that can be reused or placed within CMS pages.

## 35. Localization Terms

- **Locale**: A combination of language and regional settings (e.g., en-ZA) that determines formatting, translation, and content.
- **Currency**: The unit of monetary exchange configured for pricing and transactions (e.g., ZAR, USD).
- **Time Zone**: The regional time offset applied to timestamps and scheduling.
- **Translation**: The process or result of rendering content in another language.
- **Regional Catalogue**: A product catalogue tailored to a specific country, region, or locale, reflecting local assortment, pricing, and regulations.
- **ISO 4217 Currency Code**: The three-letter standard code used to identify a Currency, such as `ZAR`.
- **Minor Unit**: The smallest standard subdivision used to store or calculate a Currency amount, such as cents for ZAR.

## 36. Messaging Terms

- **Message**: A discrete unit of data or instruction sent between services or systems.
- **Queue**: A messaging destination from which each message is normally consumed by one processing path, subject to retry and dead-letter behavior; ordering is not assumed unless explicitly guaranteed.
- **Topic**: A publish-subscribe messaging destination that distributes a published Message to one or more independent subscriptions.
- **Publisher**: A component or service that sends messages to a queue or topic.
- **Subscriber**: A component or service that receives and processes messages from a queue or topic.
- **Event Bus**: An architectural mechanism for distributing events across decoupled components or services.
- **Message Handler**: The component responsible for validating, processing, acknowledging, retrying, or rejecting a Message.
- **At-Least-Once Delivery**: A delivery guarantee under which a Message may be delivered more than once, requiring idempotent handling.
- **Message Envelope**: Standard metadata surrounding a Message payload, including identifiers, type, version, timestamp, correlation, causation, and producer.

## 37. Integration Terms

- **External System**: A system, service, platform, or provider outside the platform's ownership boundary that exchanges data or invokes capabilities through an Integration.
- **Integration**: A governed technical connection that exchanges data, commands, events, files, or requests between the platform and another system or Bounded Context.
- **API Gateway**: An entry-point component that routes, secures, limits, observes, and optionally transforms API traffic before it reaches application endpoints.
- **Circuit Breaker**: A resilience mechanism that temporarily blocks calls to an unhealthy dependency after configured failure thresholds are reached.
- **Integration Retry Policy**: Rules governing repeated attempts for failed integration operations, including delay, backoff, jitter, limits, and terminal handling; distinct from a Notification Retry Policy.
- **Timeout**: The maximum duration allowed for an operation before it is treated as failed or abandoned.
- **Saga**: A coordinated sequence of local Database Transactions that implements a multi-step business process across modules or systems.
- **Compensating Transaction**: A business operation that semantically reverses or mitigates the effect of a previously completed Saga step.
- **Idempotent Consumer**: A Message consumer that safely handles repeated delivery without duplicating the intended business effect.
- **Integration Event**: A versioned event published across a module or system boundary to communicate a completed business fact using a stable external Contract.

## 38. Observability Terms

- **Log**: A record of events, errors, or informational messages generated by the system.
- **Metric**: See Reporting & Analytics Terms. In observability, a Metric is used specifically to monitor system behavior, reliability, capacity, or performance.
- **Trace**: A record of the execution path of a request or transaction across system components.
- **Span**: A single operation or segment within a trace, representing a unit of work.
- **Correlation ID**: A unique identifier used to link related logs, traces, or events across distributed systems.
- **Health Check**: An automated probe or test to determine the operational status of a component or service.
- **Structured Log**: A Log emitted as machine-queryable fields rather than only unstructured text.
- **Distributed Trace**: A Trace that follows a request or Message across process, module, and external-service boundaries.
- **Service Level Indicator (SLI)**: A measured indicator of service behavior such as availability, latency, correctness, or throughput.
- **Service Level Objective (SLO)**: A target value or range for an SLI over a defined period.

## 39. Performance Terms

- **Cache**: A temporary storage layer used to speed up data retrieval by storing frequently accessed data.
- **Cache Invalidation**: The process of removing or updating cached data when the underlying source changes.
- **CDN Cache**: A cache maintained by a Content Delivery Network to distribute static assets closer to users.
- **Lazy Loading**: A strategy of loading resources or data only when they are needed, reducing initial load time.
- **Pagination**: The division of large result sets into discrete pages to improve performance and usability.
- **Cache Key**: The stable identifier used to store and retrieve a cached representation.
- **Time to Live (TTL)**: The duration after which a cached value, token, Message, or temporary record expires.
- **Rate Limit**: A rule restricting requests or operations for a Principal, client, endpoint, or time window.

## 40. Testing Terms

- **Unit Test**: A test focusing on a single component or function.
- **Integration Test**: A test verifying interactions between components.
- **Component Test**: A test that verifies a UI component or bounded application component with its public behavior and selected real dependencies while excluding the full system.
- **Contract Test**: A test that verifies a consumer and provider conform to an agreed API, Message, or integration Contract without requiring a full End-to-End environment.
- **End-to-End Test**: A test simulating user workflows across the system.
- **Smoke Test**: A preliminary test to check basic functionality.
- **Regression Test**: A test ensuring new changes do not break existing features.
- **Test Fixture**: The setup required for running tests.
- **Mock**: A configurable test double that verifies expected interactions or behavior.
- **Stub**: A test double that returns predetermined data or behavior without verifying interactions.
- **Test Double**: A controlled replacement for a real dependency used in testing; Mocks and Stubs are specific kinds of Test Double.
- **Acceptance Test**: A test that verifies an acceptance criterion from a Specification from an externally observable perspective.
- **Performance Test**: A test that measures latency, throughput, scalability, stability, or resource usage under defined workloads.

## 41. Lifecycle Vocabulary

Only lifecycles that require repository-wide canonical state names are defined in this section. Other lifecycle and state machines belong in the owning domain Specification and MUST use glossary terminology without contradicting these definitions.

### Product Lifecycle

- **Draft**: An initial, unpublished state of a product.
- **Pending Review**: Product awaiting approval or feedback.
- **Published**: Product is visible and available for purchase according to publication rules.
- **Active**: Product is currently available for sale.
- **Inactive**: Product is not available for sale.
- **Archived**: Product is stored for historical reference, no longer active.

### Order Lifecycle

- **Pending Payment**: The Order exists but the required payment outcome has not been confirmed.
- **Confirmed**: The Order has passed required validation and has an accepted payment arrangement.
- **Processing**: Fulfilment work has started for one or more Order Items.
- **Partially Fulfilled**: Some, but not all, fulfillable Order Items have completed fulfilment.
- **Fulfilled**: All fulfillable Order Items have completed the platform's fulfilment obligation; this does not necessarily mean every Shipment is Delivered.
- **Cancelled**: All remaining eligible Order Items have been cancelled and no further fulfilment is expected.
- **Completed**: The Order has reached its terminal operational state after fulfilment, delivery or collection, and applicable post-order processing.
- **Archived**: The Order is retained for historical or regulatory reference and excluded from normal operational work queues.

### Payment Lifecycle

- **Initiated**: A Payment Attempt has been created and processing has started.
- **Pending Confirmation**: The platform is awaiting an authoritative Payment Provider outcome.
- **Authorized**: A Payment Authorization has approved funds or payment capability, but Capture may not yet have occurred.
- **Captured**: Funds have been captured and are eligible for settlement.
- **Failed**: The Payment Attempt did not complete successfully.
- **Cancelled**: The Payment Attempt was stopped before successful capture.
- **Partially Refunded**: Part of the captured amount has been returned.
- **Refunded**: The full refundable captured amount has been returned.

### Shipment Lifecycle

- **Created**: The Shipment record exists and is awaiting preparation or carrier booking.
- **Ready for Dispatch**: The Shipment is packed, labelled, and ready for Carrier handover.
- **Dispatched**: The Shipment has been handed to the Carrier or collection process.
- **In Transit**: The Carrier is transporting the Shipment.
- **Out for Delivery**: The Shipment is on the final delivery route.
- **Delivered**: Delivery has been confirmed at the Shipping Address or approved collection point.
- **Delivery Failed**: A delivery attempt was unsuccessful and further action is required.
- **Returned to Sender**: The Shipment is being or has been returned to the Merchant or fulfilment location.

### Refund Lifecycle

- **Requested**: A Refund Request has been created.
- **Pending Review**: The Refund Request requires validation or approval.
- **Authorized**: The refundable amount and reason have been approved.
- **Processing**: The Refund Transaction has been submitted to the Payment Provider.
- **Completed**: The Payment Provider has confirmed successful refund processing.
- **Failed**: The Refund Transaction did not complete successfully and requires retry or intervention.
- **Rejected**: The Refund Request was declined with a recorded reason.

## 42. Domain Event Vocabulary

- **ProductCreated**: Emitted when a new product entity is created in the system.
- **ProductPublished**: Emitted when a product transitions to the published state and becomes available for purchase.
- **InventoryReserved**: Emitted when a Stock Reservation is created for a commerce operation, reducing Available-to-Sell quantity.
- **InventoryReleased**: Emitted when a Stock Reservation is released and its committed quantity is returned to Available-to-Sell where applicable.
- **CartCreated**: Emitted when a new shopping cart is created for a customer or visitor.
- **CheckoutStarted**: Emitted when a customer initiates the checkout process from the cart.
- **PaymentInitiated**: Emitted when a Payment Attempt is created and provider processing or payment-session initiation begins; it does not indicate a Payment Authorization or Capture.
- **PaymentConfirmed**: Emitted only after the platform validates authoritative Payment Provider evidence confirming the required Payment Authorization or Capture. A Payment Redirect, client-reported success, or unvalidated provider response MUST NOT qualify.
- **OrderCreated**: Emitted when an order is placed after successful checkout and payment initiation.
- **OrderCancelled**: Emitted when an order is cancelled before fulfilment.
- **ShipmentCreated**: Emitted when a shipment is arranged for an order or part of an order.
- **ShipmentDelivered**: Emitted when the carrier confirms delivery of a shipment to the customer.
- **RefundCompleted**: Emitted when a Refund Transaction is finalized and funds are returned to the Customer.
- **PaymentFailed**: Emitted when a Payment Attempt reaches the Failed state after validated authoritative Payment Provider evidence or a trusted terminal processing error; a Payment Redirect or client report alone MUST NOT qualify.
- **OrderConfirmed**: Emitted when an Order reaches the Confirmed state and may proceed to fulfilment.
- **OrderFulfilled**: Emitted when all fulfillable Order Items have completed the platform's fulfilment obligation.
- **ShipmentDispatched**: Emitted when a Shipment is handed to the Carrier or collection process.
- **ReturnRequested**: Emitted when a Customer or Staff User submits a valid Return Request.
- **ReturnAuthorized**: Emitted when a Return Request is approved and a Return Authorization is issued.

## 43. Repository Naming Rules

- Domain Event type names use `PascalCase` and past tense (e.g., `OrderCreated`).
- DTO type names end with `Dto`; request and response boundary types follow the more specific Request and Response rules.
- Requests end with `Request`.
- Responses end with `Response`.
- Controllers end with `Controller`.
- Services end with `Service`.
- Repositories end with `Repository`.
- Specification filenames use `kebab-case`.
- ADRs use `ADR-XXXX-title.md`.
- Database tables use `snake_case`.
- API paths use `kebab-case`.
- Domain events are immutable.
- Enums use singular `PascalCase`.
- Interfaces begin with I only when required by framework conventions; otherwise, avoid prefixes.
- Environment variables use `UPPER_SNAKE_CASE`.
- Feature flags use dot notation (e.g., `feature.checkout.express`).
- Database indexes use `idx_<table>_<columns>` (e.g., `idx_order_created_at`).

## 44. Cross-Reference Rules

The following documents and artifacts must use glossary-defined terminology consistently: `PRODUCT.md`, `ARCHITECTURE.md`, `AGENTS.md`, ADRs, domain specifications, API contracts, database models, Domain Events, tests, code comments, operational runbooks, and user-facing administration labels.

- Where a glossary term exists, use it verbatim and preserve its defined meaning.
- A local document may narrow a term for a Bounded Context but must not contradict the canonical definition.
- A term with multiple legitimate meanings must be qualified, for example `Code Repository`, Spring `Repository`, `Database Transaction`, or `Payment Transaction`.
- Forbidden or avoided terminology found during review must be replaced or explicitly justified before approval.
- Canonical core-document terminology review is a mandatory approval gate for `GLOSSARY.md` v1.0.0; downstream conformance remains an ongoing requirement under sections 47 and 48.

## 45. Preferred Terminology

| Preferred                                     | Avoid                                                  |
| --------------------------------------------- | ------------------------------------------------------ |
| Product                                       | Item                                                   |
| Customer                                      | User                                                   |
| Staff User                                    | Admin                                                  |
| Product Variant                               | Variant or Product Type when the commerce concept is intended |
| Stock Reservation                             | Reservation or Inventory Reservation when the Inventory concept is intended |
| Order Item                                    | Line                                                   |
| Published                                     | Live                                                   |
| Available-to-Sell                             | Available Stock                                        |
| Database Transaction or Payment Transaction   | Transaction                                            |
| Payment Authorization                         | Authorization when referring to provider approval      |
| Aggregate                                     | Object Group                                           |
| Domain Event                                  | Notification                                           |
| Code Repository                               | Repository when referring to Git                       |
| Back Office                                   | Admin Portal                                           |
| Store Administrator or Platform Administrator | Admin                                                  |
| Domain Event                                  | Technical Event when the event is business-significant |

## 46. Forbidden Terminology

Ambiguous or imprecise terminology is prohibited in specifications to avoid misunderstandings. The following table outlines common terms to avoid and recommended alternatives:

| Avoid           | Use Instead                                       | Reason                                          |
| --------------- | ------------------------------------------------- | ----------------------------------------------- |
| Product Type    | Category or Product Variant                       | "Product Type" is ambiguous; use specific terms |
| Variant         | Product Variant                                   | Use the full canonical commerce term              |
| Reservation or Inventory Reservation | Stock Reservation                         | Use the canonical Inventory concept               |
| User            | Customer or Staff User                            | "User" is generic; specify role for clarity     |
| Item            | Product or Order Item                             | "Item" is vague; clarify domain context         |
| Status OK       | Explicit lifecycle state                          | "OK" is unclear; use defined states             |
| Database Object | Entity or Record                                  | Ambiguous; specify data model concept           |
| Admin           | Store Administrator or Platform Administrator     | Clarifies administrative scope                  |
| Live            | Published or Active                               | Use the exact lifecycle meaning                 |
| Transaction     | Database Transaction or Payment Transaction       | The unqualified term is ambiguous               |
| Authorization for provider approval | Payment Authorization               | Distinguishes payment approval from access control |
| Repository      | Code Repository or Spring Repository              | The unqualified term is ambiguous               |
| Event           | Domain Event, Analytics Event, or technical event | Specify the event category                      |
| Fulfilled       | Fulfilled or Delivered, as applicable             | Fulfilment and delivery are not equivalent      |

## 47. Glossary Governance

- All new terminology must be introduced in this glossary before use elsewhere.
- Existing canonical definitions MUST NOT change except through an explicit, reviewed, versioned glossary update.
- Domain-specific specifications may refine definitions but must not contradict this glossary.
- AI agents and automated systems must adhere strictly to glossary terminology to ensure consistency and traceability.
- Every new domain specification must use glossary terminology.
- New terms require glossary approval before adoption.
- AI prompts should reference glossary terminology where ambiguity exists.
- Terminology changes require a version bump and revision history entry.
- Approval requires a terminology audit of the canonical core documents named in section 48. Specifications, ADRs, API Contracts, database models, Domain Events, code, tests, and operational documentation MUST demonstrate continuing conformance through their applicable review and delivery gates.
- Core-document audit findings MUST be resolved or recorded through the applicable approved governance mechanism before the glossary can move from Draft to Approved.
- Duplicate terms must use one canonical definition with qualified meanings or explicit cross-references.
- Lifecycle states must be defined per Aggregate and must not be reused with a conflicting meaning.

## 48. v1.0.0 Approval Gate

Approval of `GLOSSARY.md` v1.0.0 is supported by terminology and consistency validation of the canonical core documents `.ai/core/AGENTS.md`, `.ai/core/ARCHITECTURE.md`, `.ai/core/PRODUCT.md`, `.ai/core/SECURITY-STANDARDS.md`, and this glossary. That validation covered repository governance, architecture and ownership boundaries, product and Payment semantics, security terminology, lifecycle vocabulary, duplicate and conflicting definitions, and forbidden ambiguity.

This approval does not assert that every downstream artifact was audited during the v1.0.0 approval pass. Specifications, ADRs, API Contracts, database models, Domain Events, Angular and Spring Boot code, tests, and operational documentation MUST use this approved glossary and MUST be checked for conformance whenever they are created, reviewed, or changed. A downstream inconsistency MUST be corrected or handled through the applicable repository governance; it MUST NOT silently redefine canonical terminology.

The completed core-document validation established the following approved baseline:

```yaml
version: 1.0.0
status: Approved
```

---

## Revision History

| Version | Date | Status | Description |
| --- | --- | --- | --- |
| 1.1.0 | 2026-08-12 | Approved | Normalized canonical Product Variant, Stock Reservation, Database Transaction, Payment, Decision Record, and related terminology after the repository-wide core consistency audit; downstream core documents remain to be normalized separately. |
| 1.0.0 | 2026-08-11 | Approved | Promoted the canonical repository glossary after final completeness, lifecycle, architecture, product, security, ownership, and terminology consistency validation. |
| 0.5.0 | 2026-08-05 | Draft | Completed the final enterprise terminology expansion for Domain-Driven Design, Hexagonal Architecture, frontend architecture, backend architecture, integrations, documentation governance, and repository governance in preparation for the repository-wide terminology audit. |
| 0.4.0 | 2026-08-05 | Draft | Performed the glossary completeness and internal consistency audit; clarified ambiguous terms, added missing commerce, architecture, security, API, data, payment, inventory, shipping, promotion, testing, messaging, observability, and lifecycle vocabulary; strengthened repository naming, cross-reference, governance, and v1.0.0 approval rules. |
| 0.3.0 | 2026-08-05 | Draft | Substantially expanded and refined definitions; added Search & Discovery, Merchandising, Content Management, Localization, Messaging, Observability, and Performance sections; grouped lifecycle states; clarified domain events; strengthened naming and cross-reference rules for consistency. |
| 0.2.0 | 2026-08-05 | Draft | Expanded the glossary with identity, pricing, tax, returns, notifications, analytics, Angular, Spring Boot, Azure, testing, lifecycle, domain event, naming convention, and terminology governance sections. |
