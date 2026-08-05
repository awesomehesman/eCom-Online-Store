---
title: GLOSSARY
version: 0.1.0
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
- **Published Product**: A product made live and visible in the catalogue.

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
- **Callback**: A synchronous notification from a payment provider regarding payment status.
- **Webhook**: An asynchronous event notification from external services.
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
- **Transaction**: A unit of work ensuring atomicity and consistency.
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

## 21. Forbidden Terminology

Ambiguous or imprecise terminology is prohibited in specifications to avoid misunderstandings. The following table outlines common terms to avoid and recommended alternatives:

| Avoid           | Use Instead                                   | Reason                                          |
| --------------- | --------------------------------------------- | ----------------------------------------------- |
| Product Type    | Category or Variant                           | "Product Type" is ambiguous; use specific terms |
| User            | Customer or Staff User                        | "User" is generic; specify role for clarity     |
| Item            | Product or Order Item                         | "Item" is vague; clarify domain context         |
| Status OK       | Explicit lifecycle state                      | "OK" is unclear; use defined states             |
| Database Object | Entity or Record                              | Ambiguous; specify data model concept           |
| Admin           | Store Administrator or Platform Administrator | Clarifies administrative scope                  |

## 22. Glossary Governance

- All new terminology must be introduced in this glossary before use elsewhere.
- Existing definitions are immutable without explicit versioned updates.
- Domain-specific specifications may refine definitions but must not contradict this glossary.
- AI agents and automated systems must adhere strictly to glossary terminology to ensure consistency and traceability.

---

## Revision History

| Version | Date       | Description                                 | Author                  |
| ------- | ---------- | ------------------------------------------- | ----------------------- |
| 0.1.0   | 2026-08-05 | Established canonical repository vocabulary | Engineering and Product |
