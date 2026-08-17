---
title: AZURE
version: 1.0.0
status: Approved
owner: Engineering
last_updated: 2026-08-17
authoritative: false
review_cycle: Quarterly
---

# Azure Standards

## 1. Purpose

This document defines the repository's lower-level Azure implementation and operational standard. It refines the Azure direction established by `.ai/core/ARCHITECTURE.md` without creating Product or Architecture authority.

It governs Azure-specific implementation boundaries, cloud identity, Secrets, networking, hosting constraints, managed data integration, observability, deployment, infrastructure governance, resilience, security, recovery, testing, and operations.

## 2. Scope

This standard applies to repository-owned Azure resource configuration, deployment integration, operational procedures, infrastructure evidence, and application integration with Architecture-approved Azure capabilities across all Environments.

It does not establish Product behavior, Domain semantics, business Requirements, provider Contracts, or an Azure resource topology. An Azure capability is mandatory only where an Approved governing source establishes it and the applicable implementation scope requires it.

## 3. Repository Authority

This standard is subordinate to the Decision Hierarchy in `.ai/core/AGENTS.md` and to Approved Canonical Documents within their scopes. Its `authoritative: false` metadata MUST remain false even if this document is later Approved.

`.ai/core/ARCHITECTURE.md` owns canonical Architecture and the Azure deployment direction. `.ai/core/SECURITY-STANDARDS.md` owns mandatory security Requirements and Security Exceptions. Approved backend standards retain ownership of Java, Spring, database, PostgreSQL, API, and eventing rules.

This standard MUST NOT override, weaken, reinterpret, or silently replace a mandatory higher-level Requirement. A material change to Approved Architecture requires an ADR and synchronized Architecture updates. Ordinary Azure configuration within an approved Architecture boundary does not automatically require an ADR.

## 4. Normative Language

The terms **MUST**, **MUST NOT**, **REQUIRED**, and **PROHIBITED** are mandatory. **SHOULD** and **SHOULD NOT** state strong defaults that require documented rationale to depart from. **MAY** identifies a permitted option, not a selected technology or Product commitment.

## 5. Azure Baseline

Microsoft Azure is the Architecture-approved deployment direction. Azure implementations MUST preserve the Modular Monolith, separately deployable frontend and backend, PostgreSQL authority, Ports and Adapters, Domain ownership, security boundaries, and operational evidence required by Approved governing documents.

Azure control-plane or managed-service behavior MUST NOT become Domain authority. Azure service success, telemetry, queue acceptance, storage persistence, or deployment completion does not by itself prove a business outcome.

## 6. Azure Technology Classification

| Classification | Capability | Governed status |
| --- | --- | --- |
| Established | Microsoft Azure | Approved deployment direction. |
| Established | Azure Database for PostgreSQL | Approved relational-data capability; exact release, SKU, tier, topology, and operational profile remain deferred. |
| Established | Azure Blob Storage | Approved object-storage direction for media; Product-media upload and transformation details remain open. |
| Established | Azure Key Vault | Approved Secret-management capability. |
| Established | Azure Front Door and approved web edge controls | Approved edge direction; detailed edge, WAF, routing, and CDN configuration remains governed implementation work. |
| Established | Application Insights, Azure Monitor, and Log Analytics | Approved Azure observability direction. |
| Established | OpenTelemetry-compatible instrumentation | Required observability compatibility baseline. |
| Established | GitHub Actions | Approved initial automation platform. |
| Conditional | Managed identities | Required where supported and applicable; fallback credentials remain governed by least privilege and Secret controls. |
| Unresolved Architecture Decision | Azure App Service or Azure Container Apps | Backend hosting selection requires an ADR and synchronized Architecture update. |
| Unresolved Architecture Decision | Bicep or Terraform | Architecture identifies this selection as an open Architecture Decision; it requires ADR governance before becoming binding, while ordinary configuration within an approved baseline does not automatically require an ADR. |
| Conditional | Redis and Azure cache implementation | Redis introduction and approved Use Cases require governance; Azure Cache for Redis is not selected here. |
| Conditional | External messaging and Azure messaging service | External messaging and service selection require approved need and ADR governance; Service Bus is not selected here. |
| Conditional | Private endpoints and advanced network isolation | Introduced where approved Requirements and Risk justify them; no topology is selected here. |
| Unselected | Other Azure services | No service is selected merely because it could satisfy a logical capability. |

The table records current governed status and MUST be updated when an Accepted ADR is synchronized into Architecture. It does not make an unresolved alternative binding.

## 7. Environment Boundaries

Each Environment MUST have appropriately separate configuration, credentials, identities, data, databases, and deployment controls. Production and non-production MUST be isolated sufficiently to prevent unintended access, mutation, disclosure, or deployment crossover.

Cost-aware shared non-production infrastructure MAY be used only where access and data isolation remain acceptable and evidenced. Production Sensitive Data MUST NOT be copied into a non-production Environment without explicit governance, minimization or masking, and approved safeguards.

This standard does not select Environment names, Azure tenants, subscriptions, management groups, regions, resource groups, or naming conventions.

## 8. Azure Identity and Access

Azure access MUST use attributable Principals, least-privilege Roles or role assignments, and Environment-scoped Permissions. Azure access MUST use managed Identity where possible and supported. Where managed Identity is unavailable for the specific capability, an approved fallback credential mechanism MUST remain least-privilege, Environment-scoped, auditable, revocable, protected as a Secret, and governed by Security Requirements.

Service-to-service Authentication MUST establish the intended Service Principal or workload Identity and constrain its audience, Resource access, and lifecycle as applicable. Credentials MUST be minimized, rotated or revoked when required, and MUST NOT be shared across unrelated workloads or Environments.

Azure RBAC governs Azure resource access; it MUST NOT substitute for application Domain Authorization, object-level Authorization, or Permission enforcement.

## 9. Human and Privileged Access

Human administrative access MUST use attributable identities, MFA where required by Security, least privilege, separation of duties, and time-bounded elevation where supported. Shared production credentials and casual standing production access are PROHIBITED.

Privileged changes MUST identify the Principal, Environment, Resource, intent, approval where required, time, and result. Emergency access MUST be controlled, auditable, reviewed after use, and reconciled into governed configuration.

## 10. Azure Key Vault

Production Secrets MUST use Azure Key Vault or another Architecture-approved Secret manager. Azure Key Vault access MUST use managed Identity where possible and supported. Where managed Identity is unavailable for the specific capability, an approved fallback credential mechanism MUST remain least-privilege, Environment-scoped, auditable, revocable, and stored and handled through approved Secret controls.

Keys and certificates MAY be managed through the approved mechanism when required. This standard does not establish a rotation interval, key algorithm, certificate authority, cryptographic design, or application-level encryption scheme.

## 11. Secret Retrieval and Failure

Applications MUST retrieve Secrets through an approved runtime mechanism and MUST validate required access during startup or before the protected capability becomes ready. Missing, expired, revoked, or inaccessible Secrets MUST fail safely and produce actionable telemetry without revealing the Secret.

Secret caching MAY be used only when its lifetime, rotation behavior, revocation behavior, memory exposure, and failure semantics are understood. A cached Secret MUST NOT silently defeat required rotation or revocation.

## 12. Secret Exposure Prohibitions

Secrets MUST NOT be committed to source control or appear in application logs, CI logs, deployment output, telemetry, exception messages, test fixtures, screenshots, documentation examples, frontend bundles, container layers, or ordinary configuration files.

A discovered Secret MUST be treated as compromised according to Security governance. Redaction does not make an otherwise unnecessary Secret safe to collect.

## 13. Networking Principles

Azure networking MUST minimize exposure, authenticate intended peers, encrypt traffic in transit, and restrict inbound, outbound, administrative, database, and provider connectivity according to approved Requirements and Risk.

Databases, Secret stores, caches, messaging capabilities, management Endpoints, and internal services SHOULD use private connectivity and explicit controls where required. Public exposure MUST be deliberate, reviewed, monitored, and limited to the intended surface.

## 14. Network Topology Deferral

This standard does not select a VNet shape, subnet count, CIDR range, firewall rule set, NAT design, Private Endpoint topology, DNS design, ingress product, egress product, load balancer, or connectivity SKU.

Provider endpoints, DNS resolution, certificate validation, proxy boundaries, database connectivity, dependency connectivity, and partial network failure MUST be considered in the applicable infrastructure Specification. Material topology changes require Architecture governance and, when material, an ADR.

## 15. TLS and Transport Security

External and sensitive internal traffic MUST use approved TLS and certificate validation. Hostname or certificate verification MUST NOT be disabled. Termination and re-encryption boundaries MUST preserve end-to-end trust appropriate to the data and threat model.

Proxy or edge forwarding headers MUST be trusted only from configured infrastructure boundaries. Client IP, scheme, host, and protocol information MUST NOT be accepted as authoritative merely because a request contains a forwarding header.

## 16. Compute and Hosting Status

The backend hosting runtime is unselected between Azure App Service and Azure Container Apps pending ADR governance. Frontend hosting service and rendering strategy also remain open. This standard does not select App Service, Container Apps, AKS, Virtual Machines, Functions, Kubernetes, or another compute platform.

Infrastructure Specifications MUST NOT treat either Architecture alternative as selected before the Accepted ADR and synchronized Architecture update exist.

## 17. Compute Platform Requirements

Any selected backend compute platform MUST support Java 21, Spring Boot 3.x, externalized configuration, approved Secret integration, health and readiness behavior, graceful shutdown, observability, deployment traceability, and resource controls based on evidence.

Application instances MUST be safe for horizontal scaling where required. Process-local state, Background Jobs, scheduling, retries, locks, and idempotency MUST NOT assume a single instance unless the approved Architecture and operational controls guarantee that boundary.

Deployment and recovery behavior MUST define safe startup, termination, rollback or forward-fix, partial failure, and dependency unavailability without inventing numerical resource limits.

## 18. Azure Database for PostgreSQL Boundary

Azure Database for PostgreSQL is the approved managed relational-data direction. `.ai/backend/DATABASE.md` governs database policy and `.ai/backend/POSTGRES.md` governs PostgreSQL-specific implementation semantics.

Azure configuration MUST preserve authoritative PostgreSQL data, Database Transaction semantics, Module ownership, Flyway migration integrity, least-privilege access, TLS, observability, Environment isolation, backup evidence, and tested restore behavior.

This standard does not select an exact PostgreSQL release, image, Azure SKU, compute tier, storage size, IOPS, high-availability topology, replica count, connection limit, maintenance window, backup retention, RPO, or RTO.

## 19. Database Identity and Connectivity

Database identities and credentials MUST be separate by workload and Environment, least-privilege, auditable, and compatible with rotation or revocation. Administrative and migration access MUST be distinct from ordinary application access where required by Approved database standards.

Connectivity failure, failover, credential rotation, pool exhaustion, timeout, and unknown commit outcomes MUST be handled without weakening Database Transaction, Payment, Inventory, or idempotency guarantees. This standard does not select a connection pool or database Authentication mechanism.

## 20. Azure Blob Storage Boundary

Azure Blob Storage is the Architecture-approved object-storage direction for media. Product-media upload, transformation, container layout, tiers, replication, URL strategy, retention, and lifecycle implementation remain governed by Product, Architecture, Security, and future Specifications.

Stored content MUST have an owner, classification, access policy, retention and deletion basis, and auditability appropriate to Risk. Public access MUST be denied by default; public media MUST use a deliberately separated delivery boundary that cannot expose private uploads, exports, Logs, or backups.

Signed access mechanisms, when used, MUST be narrowly scoped, time-bounded, protected as Sensitive Data where applicable, and unable to grant broader access than the Use Case requires. Upload paths MUST validate type, size, content, and malware Risk where applicable.

## 21. Cache Status and Safety

Redis remains conditional and requires approved Use Cases and ADR governance. This standard does not select Azure Cache for Redis, another cache product, a cache topology, or TTL values.

A cache MUST remain non-authoritative. Cache loss, staleness, duplication, or unavailability MUST NOT corrupt authoritative state or bypass Authorization. Invalidation, bounded lifetime, Sensitive Data handling, fallback, isolation, and observability MUST be defined for each approved Use Case.

## 22. Eventing and Messaging Status

External messaging remains conditional. This standard does not select Azure Service Bus, Event Grid, Event Hubs, Storage Queues, a broker, destination convention, partition strategy, schema registry, serialization format, or delivery guarantee.

Any material external-messaging adoption requires ADR governance and synchronized Architecture updates. `.ai/backend/EVENTS.md` continues to govern Domain Events, Integration Events, Message Envelopes, compatibility, delivery, replay, and operational safety.

## 23. Messaging Correctness

Duplicate delivery MUST be expected and handled through an Idempotent Consumer where external messaging is adopted. Exactly-once delivery MUST NOT be assumed; business effects MUST be protected by idempotency, constraints, ownership, and reconciliation.

The Outbox Pattern remains conditional on a Use Case requiring reliable publication coordinated with a Database Transaction. Retry, terminal handling, replay, ordering, retention, and reconciliation MUST be Contracted and evidence-based without invented values.

## 24. API and Edge Baseline

Azure edge implementation MUST preserve HTTPS, REST under `/api/v1`, JSON, OpenAPI 3.1, RFC 9457 Problem Details, Authentication, server-side Authorization, Correlation ID propagation, idempotency, request bounds, and Sensitive Data protections defined by `.ai/backend/API.md`.

Azure Front Door and approved web edge controls are the Architecture direction. This standard does not independently select API Management, Application Gateway, an additional WAF, CDN product, or another gateway. Edge infrastructure MUST NOT substitute for application Authentication, Domain Authorization, validation, or business invariants.

## 25. Observability Baseline

Azure observability MUST align with OpenTelemetry-compatible instrumentation, Application Insights, Azure Monitor, and Log Analytics. Implementation MUST produce useful Logs, Metrics, Traces, Correlation IDs, dependency telemetry, health signals, and deployment markers where applicable.

Telemetry SHOULD cover backend compute, Background Jobs, database dependencies, provider dependencies, Payment reconciliation, Inventory failures, and event processing when adopted. This standard does not establish retention, sampling percentages, alert thresholds, SLOs, SLAs, latency targets, or an additional observability vendor.

## 26. Telemetry Safety

Secrets, raw CVV, tokens, unnecessary PII, and other prohibited Sensitive Data MUST NOT appear in telemetry. High-cardinality identifiers MUST NOT be used as uncontrolled Metric dimensions.

Telemetry failure MUST NOT silently disable security or Audit requirements. Access to Azure telemetry MUST be least-privilege and Environment-scoped, and exported or retained telemetry MUST follow approved privacy, retention, and deletion Requirements.

## 27. Logs, Audit Records, and Audit Trail

Application Logs, Azure platform Logs, security Logs, Audit Records, and the Audit Trail are distinct. Azure activity or diagnostic Logs MUST NOT automatically be treated as canonical application Audit Records.

Material administrative, deployment, access, configuration, and security actions MUST preserve attributable evidence. Audit Records required by Product, Security, or Domain governance MUST be generated and protected at the owning trusted boundary.

## 28. Health, Readiness, and Liveness

Process liveness, application readiness, dependency health, and degraded dependency behavior MUST remain distinct. Liveness MUST indicate whether the process can continue; readiness MUST indicate whether the instance can safely receive applicable work.

An optional or degradable dependency SHOULD NOT make the whole application unready unless correctness requires it. Health Endpoints MUST be access-controlled as applicable and MUST NOT expose Secrets, Sensitive Data, provider internals, or exploitable infrastructure details.

## 29. Configuration

Configuration MUST be explicit, Environment-aware, validated, and separated from business rules. Source-controlled non-sensitive defaults, Environment variables, Azure application configuration where approved, Azure Key Vault for Secrets, and approved frontend bootstrap configuration remain the governed source categories.

Required configuration MUST fail fast when missing, invalid, contradictory, or unsafe. Defaults MUST be safe. Runtime-changeable configuration MUST define ownership, validation, propagation, rollback, auditability, and behavior during partial update.

## 30. Feature Flags

Feature Flags MAY control rollout, experiments, provider migration, or operational fallback only within governed Product and Architecture boundaries. Each Feature Flag MUST define owner, purpose, default, Environment behavior, evaluation location, expiry or removal plan, and telemetry.

This standard does not select an Azure Feature Flag service. Feature Flags MUST NOT permanently replace Domain modeling, Authorization, or an ADR required for a material Architecture change.

## 31. Deployment Principles

Deployments MUST use reproducible artifacts and trace each deployed version to source revision, build evidence, configuration, approvals, migration state, Environment, and result. Immutable build artifacts SHOULD be promoted between Environments where practical.

Deployment procedures MUST coordinate Flyway migrations, configuration compatibility, mixed-version compatibility, health and readiness verification, partial failure, controlled activation, and rollback or forward-fix. This standard does not mandate blue/green, canary, rolling, slot-based, or another deployment strategy.

## 32. CI/CD Boundary

GitHub Actions is the approved initial automation platform. Azure deployment configuration MUST be consumable by protected Pipelines without moving workflow implementation details into this standard.

Build and deployment Permissions MUST be separated where appropriate. Deployment identities MUST be least-privilege, Environment-scoped, and short-lived or federated where approved and supported. Untrusted contributions MUST NOT receive production Secrets or deployment access.

Pipelines MUST protect artifact integrity, make failed or skipped checks visible, preserve evidence, and use Human Approval Gates where governing Security or release policy requires them. This standard does not invent workflow names.

## 33. Infrastructure as Code Status

Bicep and Terraform are unresolved Architecture alternatives. This standard does not select Bicep, Terraform, ARM templates, Pulumi, CDK, Helm, Kubernetes, or another IaC implementation.

Material Azure infrastructure configuration MUST nevertheless be version-controlled, reviewable, reproducible, auditable, Environment-aware, and testable where practical. Plans and state MUST be protected when applicable, and unmanaged drift MUST be detectable.

Selecting or materially changing the infrastructure platform or pattern requires the applicable Architecture Decision and ADR. Ordinary resource properties within an approved platform and Architecture boundary do not automatically require an ADR.

## 34. Resource Naming and Tagging

Resource naming SHOULD be deterministic, unambiguous across relevant Environments, traceable to ownership and deployment, and compatible with provider constraints. Tags SHOULD support ownership, Environment identification, lifecycle, and cost attribution where applicable.

Names and tags MUST NOT contain Secrets, PII, Customer data, or other Sensitive Data. This standard does not define a concrete resource naming convention, mandatory tag set, abbreviation scheme, or generated name format; those may be established in an approved infrastructure Specification.

## 35. Cost and FinOps

Azure cost decisions MUST use measured demand, operational evidence, service capability, Risk, and approved Requirements. Reviews SHOULD consider compute, database, storage growth, telemetry volume, data transfer, scaling behavior, idle resources, provider costs, and non-production controls.

Resources SHOULD be right-sized from evidence and reviewed when demand changes. This standard does not establish a monetary budget, cost-allocation formula, alert threshold, SKU, or mandatory shutdown schedule.

## 36. Scaling and Capacity

Capacity decisions MUST consider measured demand, approved performance Requirements, concurrency, dependency limits, database constraints, Payment Provider limits, Background Job behavior, failure recovery, and cost.

Horizontal scaling MUST preserve Domain correctness, idempotency, Database Transaction boundaries, ordering assumptions, and safe job ownership. This standard does not select instance counts, autoscaling metrics, thresholds, resource limits, throughput targets, or capacity targets.

## 37. Resilience

Azure integrations MUST define timeouts, bounded retry, backoff, idempotency, dependency isolation, degraded behavior, partial-outage handling, reconciliation, and unknown-outcome behavior appropriate to the operation.

Retries MUST NOT be unbounded and MUST NOT repeat a non-idempotent effect without protection. Timeout, retry count, delay, backoff, jitter, and terminal thresholds remain Use Case- and evidence-specific; this standard selects no resilience library or numerical values.

## 38. Provider Failure and Unknown Outcomes

An Azure control-plane or data-plane acknowledgement MUST NOT be interpreted as proof of an unrelated Domain result. When a provider outcome is uncertain, the application MUST preserve the uncertainty and reconcile rather than fabricate success or failure.

Optional dependencies SHOULD degrade without blocking higher-priority flows where Product and correctness permit. Required dependencies MUST fail safely, avoid partial authoritative effects, and expose actionable telemetry.

## 39. Backup, Recovery, and Disaster Recovery

Production PostgreSQL and object storage MUST use approved backup and retention policies, backup success monitoring, and tested restore procedures. Recovery evidence MUST cover data integrity, application compatibility, infrastructure recreation, configuration, Secret or key dependencies, provider dependencies, and accountable ownership.

Backups are not sufficient until restoration is tested. This standard does not select backup frequency, retention, RPO, RTO, geo-replication, Availability Zone layout, failover topology, secondary region, or recovery service.

## 40. Regions and Availability

No Azure region, Availability Zone design, multi-region strategy, or failover region is selected by this standard. The Architecture permits a single-region Version 1 with documented recovery unless approved business Requirements justify multi-region capability.

Region and availability choices MUST consider approved legal and privacy Requirements, Sensitive Data, provider capability, latency, resilience, recovery, cost, and Architecture. South Africa as the current Product market does not by itself establish a legal Azure-region requirement.

## 41. Data Location and Privacy

Data residency and cross-border processing decisions MUST be based on approved legal, privacy, contractual, Product, and Architecture Requirements. This standard MUST NOT fabricate a POPIA interpretation or infer residency solely from Customer location or market.

Azure data, backups, Logs, Traces, exports, and support access MUST follow purpose limitation, minimization, access, retention, deletion, and provider-processing Requirements appropriate to their classification.

## 42. Security Boundary

`.ai/core/SECURITY-STANDARDS.md` remains governing. Azure implementation MUST enforce least privilege, minimized exposure, approved Secret handling, Sensitive Data protection, encryption boundaries, hardened deployment identities, controlled administrative access, vulnerability management, dependency security, and auditable evidence.

Azure configuration does not automatically establish application security. Infrastructure controls supplement and MUST NOT replace server-side Authentication, Authorization, input validation, Domain invariants, secure coding, or security testing.

## 43. Vulnerability and Supply-Chain Security

Applicable Azure configuration, IaC, images, dependencies, artifacts, and runtime surfaces MUST be scanned, reviewed, patched, and evidenced according to Security governance. Blocking findings MUST prevent release unless an approved, unexpired Security Exception authorizes the specific temporary waiver.

Artifacts MUST preserve source revision and integrity through promotion. Registries, base images, signing, provenance, and deployment inputs remain subject to approved supply-chain controls; this standard does not select a registry or container platform.

## 44. Payment and PCI-Sensitive Behavior

Validated Payment Provider evidence remains authoritative for provider-dependent Payment state. A Payment Redirect, browser result, client-reported success, unvalidated Callback, or unvalidated Webhook is not Payment proof. Uncertain outcomes MUST remain distinguishable and be reconciled.

Azure telemetry, storage, messaging, backups, diagnostics, and deployment output MUST NOT become accidental stores of raw CVV, prohibited card data, provider Secrets, or unnecessary payment data. Duplicate processing MUST NOT create duplicate financial effects.

Payment Attempt, Payment Authorization, Capture, Void, Payment Transaction, Refund Transaction, Chargeback, and Settlement semantics remain governed by Product and Approved implementation standards.

## 45. Inventory Integrity

Inventory remains authoritative for Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement. Azure scaling, retry, deployment, failover, caching, storage, and messaging MUST NOT enable Overselling, duplicate Stock effects, or an external projection becoming authoritative.

Concurrency, idempotency, ownership, reconciliation, and recovery MUST preserve Inventory invariants across partial failures and repeated operations.

## 46. Database Transaction and Payment Transaction

A Database Transaction is a database unit of work; a Payment Transaction is a provider-recorded financial operation. Azure configuration, telemetry, retries, or operational language MUST NOT conflate them.

An Azure operation cannot extend a local Database Transaction across a Payment Provider or other External System. Cross-boundary correctness MUST use explicit state, idempotency, reconciliation, and the Outbox Pattern where its conditional Use Case is approved.

## 47. Operational Access

Production operational access MUST be justified, least-privilege, attributable, time-bounded where supported, and auditable. Direct production database access MUST be exceptional, controlled, approved where required, and performed through governed procedures.

Operational tools MUST NOT expose broad credentials or bypass Environment boundaries. Access reviews MUST remove obsolete Principals, Roles, Permissions, tokens, keys, and Secrets.

## 48. Drift and Manual Changes

Azure infrastructure SHOULD remain reproducible from governed configuration. Unmanaged drift MUST be detectable, reviewed, and corrected.

Emergency or manual changes MUST be authorized proportionately, attributable, auditable, verified, and reconciled back into source-controlled configuration promptly. Direct production mutation by convenience is PROHIBITED.

## 49. Testing

`.ai/core/TESTING-STANDARDS.md` governs testing. Azure-relevant verification MUST cover applicable configuration, infrastructure validation, deployment behavior, Secret access, managed Identity, connectivity, database access, observability, failure handling, recovery, least privilege, and Environment isolation.

Tests MUST NOT require production Secrets or expose production Sensitive Data. Coverage, scale, latency, and failure thresholds MUST come from approved Requirements and Risk rather than invented percentages or values.

## 50. Provider-Specific Integration Testing

Local substitutes and emulators MAY support fast feedback but do not prove production Azure semantics. Real integration testing is REQUIRED where authentication, authorization, network, storage, database, telemetry, or failure behavior depends materially on Azure behavior.

Integration test Environments MUST be isolated, cost-aware, reproducible, and safe to clean up. This standard does not select an emulator, test subscription topology, or test-resource naming convention.

## 51. Deployment and Recovery Testing

Testing MUST cover applicable configuration validation, artifact deployment, migration ordering, readiness, graceful shutdown, rollback or forward-fix, partial deployment, Secret rotation or loss, dependency outage, backup restore, and infrastructure reconstruction.

Failure-injection and recovery exercises MUST be proportionate to Risk and MUST NOT endanger production data or availability. Results MUST preserve Environment, revision, configuration, actor or automation, time, and outcome evidence.

## 52. Operational Readiness and Runbooks

Operationally significant Azure capabilities MUST have appropriate runbooks before production reliance. Runbooks SHOULD cover deployment failure, Secret-access failure, database connectivity, observability failure, capacity incidents, provider outages, recovery, reconciliation, security incidents, and rollback or forward-fix decisions as applicable.

Runbook paths MUST be real and verified before reference. This standard does not fabricate a runbook directory, escalation schedule, on-call product, or incident-management tool.

## 53. Ownership Boundaries

AZURE.md owns Azure-specific implementation and operational refinements. It does not own Product behavior, Domain semantics, canonical Architecture, generic Java or Spring rules, database policy, PostgreSQL engine semantics, REST Contract semantics, event Contract semantics, UI behavior, or business Requirements.

Where a subject is governed elsewhere, this standard states the Azure boundary and points to the owner instead of duplicating or weakening the governing rule.

## 54. Decision and ADR Governance

Material selection or change of compute platform, external messaging, network topology, cache Architecture, authoritative data technology, multi-region Architecture, or infrastructure platform or pattern requires Architecture governance and an ADR where material.

An Accepted ADR becomes canonical Architecture only after synchronized Architecture updates through repository governance. A generic Decision Record cannot independently override Architecture. Ordinary resource configuration inside an already-approved Architecture does not automatically require an ADR.

## 55. Exception Governance

This standard creates no Azure-specific formal Exception. Security Exceptions, Testing Exceptions, Coding Exceptions, Documentation Exceptions, and other governed Exceptions remain scoped to their owning standards; more than one may be required when a deviation affects multiple standards.

A runtime Azure exception, provider error, failed deployment, outage, or operational incident is not a governance Exception. Mandatory Security Requirements may be waived temporarily only through an approved, time-bound Security Exception.

## 56. Prohibited Practices

The following are PROHIBITED:

- Committing Secrets or hard-coded production credentials.
- Sharing production credentials or using unattributable administrative accounts.
- Using production Sensitive Data in non-production without explicit governance and safeguards.
- Making a Resource public by convenience or relying on naming as an access control.
- Disabling TLS certificate or hostname verification.
- Using unbounded retry or retrying non-idempotent effects without protection.
- Treating Azure service acceptance, deployment success, telemetry, cache, projection, or Logs as authoritative Domain state.
- Storing or logging raw CVV or other prohibited payment data in any Azure capability.
- Permitting manual production drift without review and reconciliation.
- Hard-coding Environment credentials or allowing credentials to cross Environment boundaries.
- Inventing RPO, RTO, retention, capacity, scaling, latency, availability, or performance values.
- Adopting an unsupported Azure service or unresolved alternative as though selected.
- Bypassing ADR governance for a material Architecture change.
- Treating Azure RBAC or edge controls as application Domain Authorization.
- Assuming exactly-once delivery or allowing repeated work to create duplicate Payment, Inventory, or Order effects.

## 57. Compliance and Review Evidence

Azure changes MUST identify their governing Requirement, Environment, Resources, source revision, configuration or artifact, reviewer, validation result, deployment result, and accountable owner as applicable. Evidence MUST be protected against unauthorized change and retained according to approved policy.

Reviews MUST verify Architecture status, least privilege, exposure, Secret handling, Sensitive Data, cost impact, failure behavior, recovery, observability, testing, drift, and operational ownership proportionate to Risk.

## 58. Compliance Matrix

| Concern | Governing source | Azure responsibility | Evidence |
| --- | --- | --- | --- |
| Governance | `.ai/core/AGENTS.md` and `.ai/core/DECISIONS.md` | Preserve hierarchy and ADR boundaries | Decision and change review |
| Terminology | `.ai/core/GLOSSARY.md` | Use canonical repository terms | Terminology review |
| Product | `.ai/core/PRODUCT.md` | Avoid inventing Product behavior | Requirements and acceptance evidence |
| Architecture | `.ai/core/ARCHITECTURE.md` | Implement only established or approved Azure direction | ADR and Architecture review |
| Security | `.ai/core/SECURITY-STANDARDS.md` | Apply Azure identity, Secret, network, and data controls | Security tests, scans, and access review |
| Testing | `.ai/core/TESTING-STANDARDS.md` | Verify Azure integration and failure behavior | Automated and operational test evidence |
| Application | Approved backend standards | Preserve Java, Spring, data, API, and event boundaries | Tests and implementation review |
| Operations | Approved Requirements and infrastructure Specifications | Deploy, observe, recover, and reconcile safely | Deployment, restore, drill, and runbook evidence |

## 59. Related Documents

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
- `.ai/backend/EVENTS.md`

## 60. Deferred Decisions

The following remain deliberately unselected or quantitatively undefined:

- Backend and frontend hosting implementations.
- IaC technology and infrastructure repository layout.
- Redis adoption and cache service.
- External messaging adoption and service.
- Detailed edge, WAF, CDN, ingress, egress, DNS, VNet, subnet, firewall, NAT, and private-connectivity topology.
- Azure tenant, subscription, management-group, resource-group, Environment, region, Availability Zone, and failover layout.
- PostgreSQL release, Azure SKU, tier, storage, performance, availability, replication, maintenance, backup, and connection profile.
- Blob container, access, transformation, tier, replication, URL, retention, and lifecycle design.
- Identity Provider, Customer Authentication strategy, and Azure workload-identity implementation where alternatives remain.
- Resource naming and tagging convention.
- Monitoring retention, sampling, alerts, dashboards, SLOs, SLAs, and thresholds.
- Retry, timeout, capacity, cost, performance, RPO, RTO, retention, and recovery values.

These decisions MUST be resolved by their governing Requirement, Specification, Decision Record, ADR, or lower-level dependency-management mechanism as applicable. Deferral is not permission for independent feature-level selection.

## 61. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-17 | Approved | Promoted the Azure implementation and operational standard after final governance, Architecture, identity, Secret management, networking, compute, data, observability, deployment, infrastructure, resilience, Payment, Inventory, security, testing, recovery, terminology, and documentation-quality validation. |
| 0.1.0 | 2026-08-17 | Draft | Established the initial Azure implementation and operational standard covering cloud authority boundaries, identity, Secrets, networking, compute, data, observability, deployment, infrastructure governance, resilience, security, recovery, testing, and operations. |

## 62. Quality Requirements

This standard MUST remain subordinate, Azure-specific, evidence-based, and operationally testable. It MUST distinguish established, conditional, unselected, and deferred capabilities; preserve Domain and provider authority; and avoid provider-service selection beyond Approved Architecture.

Changes MUST preserve security, Environment isolation, reversibility, observability, recovery, cost awareness, and infrastructure reproducibility without inventing numerical targets or legal conclusions.

## 63. Final Validation

Before approval or material revision, validation MUST confirm that:

1. metadata accurately states version 1.0.0 Approved with `authoritative: false`;
2. headings are sequential and unique, sections are non-empty, and Markdown tables are valid;
3. no unfinished-work marker, actual ellipsis placeholder, fabricated path, or nonexistent authority is present;
4. Azure service status matches current Approved Architecture and conditional capabilities remain conditional;
5. no region, topology, SKU, tier, RPO, RTO, retention, retry, capacity, performance, legal, Product, or Domain decision is invented;
6. Azure Key Vault, managed Identity, observability, GitHub Actions, data, API, eventing, and IaC boundaries remain consistent with governing standards;
7. Payment authority, Inventory authority, Database Transaction and Payment Transaction distinctions, Audit Record distinctions, RFC 9457, OpenAPI 3.1, Outbox Pattern conditionality, and non-exactly-once semantics remain intact;
8. no new Decision type or formal Exception type is introduced;
9. implementation and operational evidence is practical and testable; and
10. only `.ai/backend/AZURE.md` changes for a scoped update.

## 64. Approval and Implementation Reliance Gate

This standard MUST NOT be relied upon for implementation or materially revised without revalidating its Azure service classifications against the current Approved Architecture, preserving every deliberate deferral, checking terminology against `GLOSSARY.md`, and confirming applicable security, testing, operational, and documentation evidence.

Approval makes this a subordinate implementation standard within its scope. It does not make `authoritative: false` true or permit this document to override `.ai/core/`.
