---
title: Product Catalogue Backend Specification
version: 0.1.0
status: Draft
owner: Backend / Product
last_updated: 2026-09-17
authoritative: false
scope: BPRD
---

# Product Catalogue Backend Specification

## 1. Purpose

This Specification defines implementation-neutral backend Requirements for Product-owned Product and Product Variant identity, descriptive information, Product-owned Attributes, Product Media association and metadata, lifecycle, publication, visibility eligibility, structural sellability, history, correction, failure, recovery, Contracts, persistence, concurrency, conditional integration, observability, Audit Records, and verification.

This document uses scope code `BPRD`. While Draft, it is non-normative. If Approved, its Requirements will be normative only within the Product Catalogue backend scope. It is the third downstream Backend Specification after BEB, immediately after BIDN and BCUS, uses the Product-only decomposition established by Accepted ADR-0004, and remains subordinate to governing sources, Approved Business Requirements, the Approved Product Domain, materially applicable BEB Requirements, and materially applicable BIDN and BCUS Contracts or evidence. It is not repository-wide authority and resolves no Open Product or Architecture Decision.

## 2. Scope, Authority, and Inheritance

### BPRD-REQ-001 — Lifecycle, Scope, and Authority

BPRD MUST govern only bounded backend specialization of Product-owned behavior under scope `BPRD`, preserve governing-source and Approved Domain precedence, and MUST NOT be treated as repository-wide authority.

### BPRD-REQ-002 — Product Domain Specialization

BPRD MUST specialize only the Approved Product Domain without redefining Product semantics, transferring Product authority, or creating Category, Search and Discovery, Pricing, Inventory, Customer, Cart, Checkout, Administration, Order, Payment, Shipping and Fulfilment, Return, Notification, Reporting, CMS, or other Domain policy.

### BPRD-REQ-003 — Product-Only Decomposition

The Product Catalogue backend boundary MUST remain Product-only; “Catalogue” identifies Product-owned descriptive catalogue capability and MUST NOT absorb Category taxonomy or Search and Discovery behavior.

### BPRD-REQ-004 — BEB Inheritance

BPRD MUST inherit every materially applicable BEB Requirement without copying, weakening, contradicting, or transferring its authority, explicitly trace each BEB Requirement, and retain capability-conditional obligations without authorizing the capability.

### BPRD-REQ-005 — BIDN Consumption and Identity Non-Authority

BPRD MAY consume materially applicable BIDN Contracts and trusted Identity evidence, but MUST NOT own or redefine Authentication, credentials, Principal establishment, Sessions, revocation, recovery verification, Roles, Permissions, Claims, Scope, or Identity access evidence.

### BPRD-REQ-006 — BCUS Consumption and Customer Non-Authority

BPRD MAY consume materially applicable BCUS Contracts and governed Customer or Account context only where Product behavior is already governed, but MUST NOT own or redefine Customer, Account, profile, Address, Preference, Consent, Customer association, eligibility, or Customer history.

## 3. Backend Architecture Boundary

### BPRD-REQ-007 — Contextual Authorization Ownership

BPRD MUST enforce current contextual Authorization for Product-owned Resources and actions; Authentication evidence, identifiers, Role labels, Permissions, Claims, Scope, Customer context, Account association, UI state, or prior responses alone MUST NOT authorize a Product action.

### BPRD-REQ-008 — Modular and Hexagonal Boundary

Product backend behavior MUST preserve inherited modular-monolith and hexagonal boundaries, keep Product Domain and application logic independent of transport, persistence, provider, cloud, media, Search, and messaging implementations, and interact through intentional project-owned Ports and Contracts.

### BPRD-REQ-009 — Product Public Module Contract

The Product backend Module MUST expose only the minimum governed Contract needed for Product intent, context, source, freshness, accepted outcome, failure, uncertainty, and correlation, and MUST NOT expose internal Entities, Repositories, persistence mappings, provider models, media internals, or framework types.

### BPRD-REQ-010 — Product Use Case Orchestration

Each supported Product command or query MUST enter through an explicit Product-owned Use Case that validates applicable input and context, invokes Product Domain behavior, coordinates required Ports, and returns no outcome beyond accepted Product evidence.

### BPRD-REQ-011 — Transaction, Configuration, and Operational Safety

Product Application Services MUST own focused Database Transaction boundaries, keep external calls outside unsafe transaction scope, and preserve authority, safe configuration, compatibility, bounded work, failure containment, recoverability, and explicit uncertainty without selecting unresolved mechanisms, topology, values, or rollout policy.

## 4. Product and Product Variant Behavior

### BPRD-REQ-012 — Stable Product Identity

Backend representations MUST preserve stable Product identity independently of mutable descriptive information, Product Media, Category association, Price, Inventory, Search representation, Customer context, or downstream state without prescribing identifier format or generation.

### BPRD-REQ-013 — Coherent Product Definition

Product creation and mutation MUST preserve coherent Product-owned descriptive information and associations sufficient to distinguish Products and support governed Customer evaluation, while invalid, incomplete, unauthorized, materially duplicate, or ambiguous intent fails safely.

### BPRD-REQ-014 — Product Variant Membership and Identity

A Product Variant MUST retain stable identity, belong to the correct governed Product, and preserve Product-to-Variant membership independently of Price, Stock, Cart, Checkout, Order, or Search representations.

### BPRD-REQ-015 — Product Variant Uniqueness and Concurrency

Product Variant mutation MUST preserve governed uniqueness and reject or explicitly resolve duplicate, conflicting, stale, or concurrent intent without silent accepted-data loss or authority change.

### BPRD-REQ-016 — Product Variant Lifecycle Consistency

Product Variant state MUST remain compatible with its owning Product lifecycle and MUST NOT become independently publishable, visible, or structurally sellable when the Product state prohibits that outcome.

### BPRD-REQ-017 — Product-Owned Attribute Semantics

BPRD MAY manage only Product-owned Attribute definitions and Product or Product Variant associations already governed by the Product Domain and MUST NOT invent Attribute standards, values, Category classification, Search Facets, Pricing rules, or Inventory meaning.

### BPRD-REQ-018 — Attribute Validity and Evolution

Attribute mutation MUST preserve governed validity, Product and Product Variant coherence, historical intelligibility, and downstream compatibility; later changes MUST NOT rewrite confirmed commercial history.

### BPRD-REQ-019 — Product-Owned Validation

Every Product-owned mutation MUST validate applicable identity, association, lifecycle, completeness, authorization, concurrency, and content invariants at the authoritative backend boundary rather than trusting client or Projection state.

## 5. Lifecycle, Publication, Visibility, and Structural Sellability

### BPRD-REQ-020 — Product Lifecycle Integrity

Product lifecycle behavior MUST preserve the canonical Product-owned Draft, Published, and Archived meanings, permit only governed transitions, and reject silent publication, reactivation, unpublication, or archival.

### BPRD-REQ-021 — Publication Completeness and Authorization

Publication MUST require current Product-owned completeness and server-side contextual Authorization, retain material evidence, and MUST NOT be inferred from media upload, Category placement, Search indexing, frontend display, feature flags, or downstream state.

### BPRD-REQ-022 — Publication Authority and Non-Transfer

Only governed Product behavior MAY establish Product publication state; Category, Search, CMS, Administration, media processing, cache, projection, client, or provider evidence MUST NOT independently publish, unpublish, archive, or reactivate Product truth.

### BPRD-REQ-023 — Visibility Eligibility

BPRD MAY expose Product-owned catalogue visibility eligibility only from current governed Product evidence and MUST distinguish unpublished, archived, incomplete, unauthorized, unavailable, stale, and uncertain outcomes without establishing final Customer eligibility or purchasability.

### BPRD-REQ-024 — Structural Sellability Boundary

BPRD MAY establish only Product-owned structural sellability; final purchasability MUST remain dependent on current owning-Domain evidence including applicable Pricing, Inventory, Customer, Checkout, Shipping, and policy outcomes.

### BPRD-REQ-025 — Governed Unpublication, Archival, and Deletion

Unpublication, archival, and any governed deletion MUST preserve authorization, lifecycle validity, historical references, accepted downstream records, Audit Records, recovery evidence, and explicit effects without inventing deletion, retention, approval, or scheduling policy.

### BPRD-REQ-026 — Historical Product Integrity

Product and Product Variant lifecycle, content, Attribute, association, and Product Media changes MUST preserve intelligible historical references and MUST NOT rewrite confirmed Order snapshots, Payment evidence, Shipment history, Audit Records, or another Domain's retained truth.

## 6. Product Content and Product Media

### BPRD-REQ-027 — Product Information Completeness and Truth

Product-owned descriptive information MUST remain complete enough for its governed lifecycle, accurate, supportable, accessible where applicable, and correctable; BPRD MUST reject or withdraw unsupported, misleading, internally inconsistent, or non-publishable Product truth through governed outcomes.

### BPRD-REQ-028 — Product Content Correction

Product content correction MUST preserve stable identity, lifecycle authority, accepted history, downstream compatibility, and explicit stale or superseded representations without silently rewriting another Domain's historical record.

### BPRD-REQ-029 — Product Media Association and Metadata

BPRD MAY own Product Media association and governed metadata, ordering, accessibility meaning, lifecycle compatibility, and correction evidence. Product Media MUST retain an unambiguous association with the correct Product and, where applicable, Product Variant context, accurately represent that Product or Product Variant, and provide meaningful text alternatives where governed accessibility requires them. BPRD MUST NOT select upload, storage, transformation, delivery, provider, pipeline, concrete format, numerical media limit, approval workflow, or infrastructure mechanisms.

### BPRD-REQ-030 — Product Media Publication and Failure

Private, Draft, invalid, unauthorized, or otherwise non-publishable Product Media MUST NOT become Customer-visible. Published or Customer-visible Product Media MUST satisfy applicable governed rights, security, quality, accuracy, and accessibility checks. Product Media availability or processing success alone MUST NOT publish Product truth; invalid, inaccessible, stale, failed, misleading, or otherwise non-publishable media MUST produce a distinguishable safe Product-owned outcome and support governed correction where permitted.

## 7. Cross-Domain Boundaries

### BPRD-REQ-031 — Category Authority Boundary

Category MUST retain authority for taxonomy, hierarchy, classification, navigation eligibility, ordering, and Category-side Product membership policy; BPRD MAY retain governed Product-side associations but MUST NOT define Category semantics or backend placement.

### BPRD-REQ-032 — Search and Discovery Authority Boundary

Search and Discovery MUST retain authority for search requests, normalization, filters, Facets, ranking, indexing outcomes, reconciliation, and Search operational truth. Only Product evidence currently eligible for indexing under Approved policy MAY be supplied as eligible Product evidence to Search. Product withdrawal, archival, correction, publication or visibility eligibility change, or another governed Product eligibility change MUST provide governed Product evidence sufficient to support reconciliation of stale or delayed Search representations where the governed Search Contract requires it. Search results, indexes, caches, and Projections MUST remain non-authoritative representations of Product truth; BPRD MUST NOT define Search indexing, ranking, reconciliation mechanics, provider, extraction thresholds, backend implementation, or transfer Product or Search authority.

### BPRD-REQ-033 — Pricing and Inventory Non-Authority

BPRD MUST NOT calculate or establish Price, Discount, Promotion, Voucher, Money, Currency, tax, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, Stock Movement, scarcity, or availability truth and MUST preserve current owning-Domain revalidation.

### BPRD-REQ-034 — Customer, Cart, and Checkout Boundary

Customer, Account, Cart intent, Customer eligibility, contextual Authorization, market eligibility, Checkout progression, commercial revalidation, and purchase readiness MUST remain with their applicable owning governance or Domain. BPRD MUST NOT establish market, language, or Currency expansion policy, and Product-owned visibility, Product evidence, or BIDN/BCUS evidence MUST NOT establish or substitute for Customer eligibility, contextual Authorization, market eligibility, Checkout eligibility, or the other listed outcomes.

### BPRD-REQ-035 — Order, Payment, Shipping, and Return Boundary

BPRD MUST NOT create or mutate Order, Payment, Refund, Return, Shipment, Fulfilment, delivery, provider, or retained commercial truth; those Domains MAY retain governed Product snapshots without becoming live Product authority.

### BPRD-REQ-036 — Administration, CMS, Notification, and Reporting Boundary

Administration, CMS, Notifications, Reporting, Analytics, and frontend representations MAY invoke or consume governed Product capabilities but MUST NOT bypass Product rules or become authoritative for Product identity, lifecycle, publication, visibility, structural sellability, or content truth.

### BPRD-REQ-037 — Product Administration Boundary

Protected Product administration MUST use authenticated and contextually authorized Staff workflows, preserve Product validation and lifecycle, and produce proportional evidence without BPRD inventing Roles, Permissions, approval chains, bulk rules, support workflows, or escalation policy.

## 8. Persistence, Contracts, Integration, Failure, and Recovery

### BPRD-REQ-038 — Product Data Ownership and Persistence

Product-owned authoritative state MUST remain accessible only through Product-owned Ports and Repositories, preserve identity, associations, lifecycle, history, integrity, and applicable exact timestamps, and MUST NOT expose or depend inward on physical schemas, ORM types, SQL, indexes, or another Domain's storage.

### BPRD-REQ-039 — Concurrency and Duplicate Safety

Concurrent, duplicate, retried, replayed, reordered, or stale Product commands MUST preserve accepted Product truth, current Authorization, prior effects, lifecycle, uniqueness, and explicit conflict or uncertainty without selecting locking, idempotency-key, retry, or retention mechanisms.

### BPRD-REQ-040 — Abstract Product Contract Boundary

Product backend Contracts MUST describe only governed intent, identity, context, authority, source, freshness, outcome, failure, uncertainty, recovery, correlation, and compatibility and MUST NOT invent routes, HTTP operations or statuses, DTO fields, payloads, schemas, provider formats, or frontend behavior.

### BPRD-REQ-041 — Conditional Events and Integrations

Product Domain Events, Integration Events, providers, media integrations, Search contributions, or external messaging MAY exist only where Approved Architecture, a governed Contract, or downstream reliability establishes the capability; any applicable integration MUST preserve Product authority, compatibility, duplicate and replay safety, uncertainty, and non-authoritative consumers without selecting names, schemas, topics, brokers, providers, or delivery mechanisms.

### BPRD-REQ-042 — Explicit Failure, Correction, Recovery, and Reconciliation

Validation, Authorization, lifecycle, content, media, association, concurrency, persistence, dependency, integration, publication, and unexpected failures MUST remain distinguishable where response or recovery differs; correction, retry, recovery, and reconciliation MUST revalidate current authority and Product truth, preserve accepted effects and uncertainty, and MUST NOT promote external evidence or a Projection to Product truth.

## 9. Security, Operations, and Verification

### BPRD-REQ-043 — Security, Privacy, and Sensitive Data

Product backend behavior MUST enforce least privilege, contextual Authorization, concealment where applicable, input safety, tamper resistance, and minimization of Sensitive Data across persistence, transmission, Product content, Product Media metadata, imports, exports, Contracts, events, logs, metrics, traces, errors, fixtures, and support evidence; Secrets, credentials, private provider evidence, and unrelated Customer PII MUST remain excluded.

### BPRD-REQ-044 — Observability and Proportional Audit

Product operations MUST provide safe bounded correlation, Logs, Metrics, Traces, health, readiness, and alerting for material outcomes and degradation, while material Product creation, mutation, publication, unpublication, archival, deletion, and repair MUST produce proportional Audit Records distinct from ordinary telemetry, events, analytics, and Product history without selecting retention or numerical targets.

### BPRD-REQ-045 — Accessibility, Boundedness, and Compatibility

Product Contracts and outcomes MUST support governed accessible Product experiences, bounded collections and content delivery, degradation safety, migration, rollback or forward-fix evidence, mixed-version compatibility, and backward-compatible deployment without selecting UI mechanics, page sizes, payload limits, performance targets, SLAs, or SLOs.

### BPRD-REQ-046 — Complete Verification and Traceability

Verification MUST deterministically cover Product invariants, Use Case orchestration, Product-only authority, BIDN and BCUS consumption, Authorization, Product and Product Variant identity and association, Attributes, Product Media, lifecycle, publication, visibility, structural sellability, history, persistence, concurrency, Contracts, conditional integrations, failure, correction, recovery, security, accessibility, compatibility, operations, BEB inheritance, and complete traceability beyond mocked success paths.

## 10. Acceptance Criteria

| Acceptance Criterion | Requirement | Acceptance evidence |
| --- | --- | --- |
| BPRD-AC-001 | BPRD-REQ-001 | Metadata and review show `0.1.0 Draft`, `authoritative: false`, scope `BPRD`, Draft non-normativity, bounded authority, and no repository-wide claim. |
| BPRD-AC-002 | BPRD-REQ-002 | Boundary review finds only Product backend specialization and no redefinition or transfer of Product or another Domain's authority or policy. |
| BPRD-AC-003 | BPRD-REQ-003 | Review proves “Catalogue” is Product descriptive capability only and finds no absorbed Category or Search and Discovery behavior. |
| BPRD-AC-004 | BPRD-REQ-004 | The BEB matrix accounts for BEB-REQ-001 through BEB-REQ-056 exactly once, traces applicable obligations, and authorizes no conditional capability. |
| BPRD-AC-005 | BPRD-REQ-005 | Integration tests consume only governed BIDN evidence and find no BPRD-owned Identity, Authentication, credential, Principal, Session, revocation, or access truth. |
| BPRD-AC-006 | BPRD-REQ-006 | Integration tests consume only governed BCUS context and find no BPRD-owned Customer, Account, profile, Address, Preference, Consent, eligibility, or history truth. |
| BPRD-AC-007 | BPRD-REQ-007 | Authorization tests require current Product Resource, action, property, and state context and prove Identity, Customer, UI, and label evidence alone grants no Product action. |
| BPRD-AC-008 | BPRD-REQ-008 | Architecture tests enforce inward dependency direction and reject Product domain/application dependencies on outward implementation types. |
| BPRD-AC-009 | BPRD-REQ-009 | Contract review exposes only necessary governed semantics and no internal Entity, Repository, mapping, media, provider, or framework type. |
| BPRD-AC-010 | BPRD-REQ-010 | Use Case tests validate context, invoke Product behavior, coordinate Ports, and return only accepted Product evidence. |
| BPRD-AC-011 | BPRD-REQ-011 | Transaction, configuration, deployment, and failure tests preserve focused ownership, safeguards, bounds, recoverability, and uncertainty without an unresolved mechanism or value. |
| BPRD-AC-012 | BPRD-REQ-012 | Product identity remains stable across every listed mutable association and representation, with no identifier format or generation choice. |
| BPRD-AC-013 | BPRD-REQ-013 | Creation and mutation tests accept coherent valid Product intent and safely reject invalid, incomplete, unauthorized, duplicate, and ambiguous intent. |
| BPRD-AC-014 | BPRD-REQ-014 | Variant tests preserve stable identity and correct Product membership independently of downstream commercial and representational state. |
| BPRD-AC-015 | BPRD-REQ-015 | Racing, stale, conflicting, and duplicate Variant mutations preserve uniqueness and accepted state or return an explicit safe conflict. |
| BPRD-AC-016 | BPRD-REQ-016 | Variant lifecycle tests prevent visibility, publication, or structural sellability when the owning Product state prohibits it. |
| BPRD-AC-017 | BPRD-REQ-017 | Attribute review finds only governed Product-owned semantics and no invented standards, Category classification, Facets, Pricing, or Inventory meaning. |
| BPRD-AC-018 | BPRD-REQ-018 | Attribute evolution tests preserve validity, coherence, history, and compatibility and do not rewrite confirmed commercial history. |
| BPRD-AC-019 | BPRD-REQ-019 | Negative and boundary tests prove every Product mutation revalidates the listed invariants at the backend boundary rather than trusting client or Projection state. |
| BPRD-AC-020 | BPRD-REQ-020 | Lifecycle tests permit only governed Draft, Published, and Archived transitions and reject every silent or invalid transition. |
| BPRD-AC-021 | BPRD-REQ-021 | Publication tests require current completeness and Authorization, retain evidence, and prove none of the listed representations or mechanisms publishes Product truth. |
| BPRD-AC-022 | BPRD-REQ-022 | Authority tests allow only governed Product behavior to alter publication state and reject every listed external assertion as publication authority. |
| BPRD-AC-023 | BPRD-REQ-023 | Visibility tests derive eligibility from current Product evidence, distinguish each listed adverse state, and establish neither Customer eligibility nor purchasability. |
| BPRD-AC-024 | BPRD-REQ-024 | Structural-sellability tests prove Product establishes no final purchasability and preserves current owning-Domain revalidation. |
| BPRD-AC-025 | BPRD-REQ-025 | Unpublication, archival, and deletion tests preserve authority, history, downstream records, audit and recovery evidence and select no unresolved policy or schedule. |
| BPRD-AC-026 | BPRD-REQ-026 | Later Product change leaves confirmed Order, Payment, Shipment, audit, and other retained Domain truth unchanged and intelligible. |
| BPRD-AC-027 | BPRD-REQ-027 | Content tests accept complete, accurate, supportable, accessible Product information and safely reject, withdraw, or correct each prohibited state. |
| BPRD-AC-028 | BPRD-REQ-028 | Correction tests preserve identity, lifecycle, history, compatibility, and explicit stale representations without rewriting another Domain's history. |
| BPRD-AC-029 | BPRD-REQ-029 | Media tests prove unambiguous Product and applicable Product Variant association, accurate representation, governed meaningful text alternatives, metadata, ordering, lifecycle compatibility, and correction while review finds none of the prohibited media mechanisms, formats, limits, or workflows. |
| BPRD-AC-030 | BPRD-REQ-030 | Visibility and failure tests prevent every listed non-publishable media state from becoming Customer-visible, verify applicable rights, security, quality, accuracy, and accessibility checks for visible media, distinguish every adverse outcome, support governed correction, and prove availability or processing success alone publishes no Product truth. |
| BPRD-AC-031 | BPRD-REQ-031 | Category boundary tests preserve all Category-owned semantics and find no BPRD-defined Category backend placement. |
| BPRD-AC-032 | BPRD-REQ-032 | Search tests reject ineligible Product evidence as indexing-eligible, prove governed Product eligibility changes provide sufficient evidence for required Search reconciliation, keep every Search representation non-authoritative for Product truth, preserve Product and Search authority, and find no BPRD-selected Search mechanism or implementation. |
| BPRD-AC-033 | BPRD-REQ-033 | Review and integration tests find no Product-owned Pricing or Inventory truth and require current owning-Domain evidence where applicable. |
| BPRD-AC-034 | BPRD-REQ-034 | Cross-Domain tests prove Product visibility, Product evidence, and BIDN/BCUS evidence establish none of the listed Customer, Authorization, market, Cart, or Checkout outcomes, and review finds no market, language, or Currency expansion policy or invented Domain. |
| BPRD-AC-035 | BPRD-REQ-035 | Cross-Domain tests preserve every listed owner and allow historical Product snapshots without transferring live Product authority. |
| BPRD-AC-036 | BPRD-REQ-036 | Invocations and representations preserve Product rules and establish no Product identity, lifecycle, publication, visibility, sellability, or content truth. |
| BPRD-AC-037 | BPRD-REQ-037 | Protected administration tests require trusted Authentication and current contextual Authorization, preserve Product rules and audit evidence, and find no invented matrix or workflow policy. |
| BPRD-AC-038 | BPRD-REQ-038 | Persistence tests enforce Product-owned Ports, mappings, identity, associations, lifecycle, history, and integrity with no inward physical-storage or cross-Domain dependency. |
| BPRD-AC-039 | BPRD-REQ-039 | Concurrency and replay tests preserve accepted truth, prior effects, authority, lifecycle and uniqueness and select no locking, idempotency, retry, or retention mechanism. |
| BPRD-AC-040 | BPRD-REQ-040 | Contract review covers all required abstract semantics and finds none of the prohibited concrete API, DTO, payload, schema, provider, or frontend choices. |
| BPRD-AC-041 | BPRD-REQ-041 | No event or integration exists without governed capability evidence; applicable tests preserve authority, compatibility, replay safety and uncertainty without selecting a mechanism. |
| BPRD-AC-042 | BPRD-REQ-042 | Failure and recovery tests distinguish material outcomes, revalidate current authority and truth, preserve accepted effects and uncertainty, and promote no external representation to truth. |
| BPRD-AC-043 | BPRD-REQ-043 | Security tests enforce every listed protection and find no Secret, credential, private provider evidence, or unrelated Customer PII in prohibited surfaces. |
| BPRD-AC-044 | BPRD-REQ-044 | Operational tests provide safe bounded telemetry and proportional Product Audit Records that remain distinct from telemetry, events, analytics, and history without invented targets. |
| BPRD-AC-045 | BPRD-REQ-045 | Accessibility, bounds, degradation, migration, deployment, and compatibility tests pass without selecting UI mechanics or numerical targets. |
| BPRD-AC-046 | BPRD-REQ-046 | Deterministic Domain, application, Adapter, Contract, security, architecture, operational, inheritance, and traceability evidence covers every listed concern beyond mocked success. |

## 11. Requirement Traceability

| Requirement | Product Domain | BEB | Additional governing sources |
| --- | --- | --- | --- |
| BPRD-REQ-001 | REQ-PRD-001, 044 | BEB-REQ-001–004, 056 | ADR-0004; ARCHITECTURE.md §35 |
| BPRD-REQ-002 | REQ-PRD-001–002 | BEB-REQ-003–004 | ADR-0004 Authority Boundary |
| BPRD-REQ-003 | REQ-PRD-001–002, 045 | BEB-REQ-003–004 | ADR-0004 Decision; REQ-CAT-001–002; REQ-SRCH-002–003 |
| BPRD-REQ-004 | REQ-PRD-044 | BEB-REQ-001–004, 055–056 | ADR-0004; ARCHITECTURE.md §35 |
| BPRD-REQ-005 | REQ-PRD-049–050 | BEB-REQ-011, 019–020 | BIDN-REQ-004, 011–013, 021, 024 |
| BPRD-REQ-006 | REQ-PRD-048 | BEB-REQ-003, 019, 021 | BCUS-REQ-005–006, 012–016, 034 |
| BPRD-REQ-007 | REQ-PRD-049–050 | BEB-REQ-011, 019–020 | BIDN-REQ-004, 021, 024; SECURITY-STANDARDS.md §12 |
| BPRD-REQ-008 | REQ-PRD-052 | BEB-REQ-005–007 | ARCHITECTURE.md §§8, 11, 35; SPRING.md |
| BPRD-REQ-009 | REQ-PRD-052 | BEB-REQ-008, 014 | API.md; ADR-0004 Exclusions |
| BPRD-REQ-010 | REQ-PRD-034–040 | BEB-REQ-009–010 | SPRING.md; ARCHITECTURE.md §35.2 |
| BPRD-REQ-011 | REQ-PRD-035–040, 042 | BEB-REQ-012, 025–028, 041–042, 047–051 | DATABASE.md; SPRING.md |
| BPRD-REQ-012 | REQ-PRD-003 | BEB-REQ-021–024 | GLOSSARY.md; ADR-0004 Exclusions |
| BPRD-REQ-013 | REQ-PRD-004, 025–027, 034–035, 039–040 | BEB-REQ-009, 015, 021, 041 | REQ-BUS-003, 005, 050 |
| BPRD-REQ-014 | REQ-PRD-007–008 | BEB-REQ-021, 023–024 | REQ-BUS-006, 017 |
| BPRD-REQ-015 | REQ-PRD-009, 036 | BEB-REQ-023, 027, 029–031 | DATABASE.md; TESTING-STANDARDS.md |
| BPRD-REQ-016 | REQ-PRD-010 | BEB-REQ-021, 024 | Product Domain §7 |
| BPRD-REQ-017 | REQ-PRD-011–012 | BEB-REQ-003, 021, 056 | PRODUCT.md §24; ADR-0004 Exclusions |
| BPRD-REQ-018 | REQ-PRD-013, 030–031 | BEB-REQ-018, 024, 050 | DATABASE.md; API.md |
| BPRD-REQ-019 | REQ-PRD-039–040 | BEB-REQ-009, 015, 041 | TESTING-STANDARDS.md |
| BPRD-REQ-020 | REQ-PRD-005, 028–029 | BEB-REQ-021, 024, 027 | GLOSSARY.md; DATABASE.md |
| BPRD-REQ-021 | REQ-PRD-018–019, 043, 049–050 | BEB-REQ-011, 020–021, 045 | SECURITY-STANDARDS.md §§12, 27 |
| BPRD-REQ-022 | REQ-PRD-020, 028–029 | BEB-REQ-003, 021 | ADR-0004 Authority Boundary |
| BPRD-REQ-023 | REQ-PRD-023–024 | BEB-REQ-015–018, 021, 041 | API.md; REQ-BUS-003–004 |
| BPRD-REQ-024 | REQ-PRD-021–022, 046–048 | BEB-REQ-003, 021 | Pricing, Inventory, Customer, Checkout authority |
| BPRD-REQ-025 | REQ-PRD-037–038 | BEB-REQ-011, 020–21, 024, 042–043 | PRODUCT.md §24 items 22, 27 |
| BPRD-REQ-026 | REQ-PRD-006, 030–031 | BEB-REQ-021, 024 | Order and other historical owners |
| BPRD-REQ-027 | REQ-PRD-004, 015, 018, 025–027, 041 | BEB-REQ-015, 021, 043, 051 | REQ-BUS-005, 037, 050 |
| BPRD-REQ-028 | REQ-PRD-027, 030–031, 035 | BEB-REQ-024, 041–042, 050 | DOCUMENTATION-STANDARDS.md |
| BPRD-REQ-029 | REQ-PRD-014–017 | BEB-REQ-021, 032–034, 043 | ARCHITECTURE.md §34 item 11; ADR-0004 Exclusions |
| BPRD-REQ-030 | REQ-PRD-015–017 | BEB-REQ-041–043 | REQ-BUS-050; SECURITY-STANDARDS.md |
| BPRD-REQ-031 | REQ-PRD-045 | BEB-REQ-003, 008, 021 | REQ-CAT-001–002, 010–014; ADR-0004 |
| BPRD-REQ-032 | REQ-PRD-032–033 | BEB-REQ-003, 021, 037–040 | REQ-SRCH-002–003, 009, 025, 029–033; ADR-0004 |
| BPRD-REQ-033 | REQ-PRD-046–047 | BEB-REQ-003, 021 | Pricing and Inventory Domain authority |
| BPRD-REQ-034 | REQ-PRD-022, 048 | BEB-REQ-003, 019–021 | BCUS-REQ-034–035; Cart and Checkout authority |
| BPRD-REQ-035 | REQ-PRD-002, 006, 022, 030–031 | BEB-REQ-003, 021, 024 | Order, Payment, Shipping, Return authority |
| BPRD-REQ-036 | REQ-PRD-002, 020, 032–033, 049 | BEB-REQ-003, 008, 021, 045 | Administration, CMS, Notifications, Reporting authority |
| BPRD-REQ-037 | REQ-PRD-019, 034–040, 043, 049–050 | BEB-REQ-011, 019–020, 045 | BIDN-REQ-024–026; PRODUCT.md §24 item 23 |
| BPRD-REQ-038 | REQ-PRD-003, 006, 030–031, 051 | BEB-REQ-021–025 | DATABASE.md; POSTGRES.md |
| BPRD-REQ-039 | REQ-PRD-009, 029, 035–040 | BEB-REQ-027, 029–031, 041–042 | DATABASE.md; API.md |
| BPRD-REQ-040 | REQ-PRD-052 | BEB-REQ-008, 013–018, 056 | API.md; ADR-0004 Exclusions |
| BPRD-REQ-041 | REQ-PRD-032, 043, 052 | BEB-REQ-026, 032–040, 056 | EVENTS.md; ADR-0004 Exclusions |
| BPRD-REQ-042 | REQ-PRD-017, 027, 035–040 | BEB-REQ-041–042 | API.md; DATABASE.md; EVENTS.md |
| BPRD-REQ-043 | REQ-PRD-050–051 | BEB-REQ-011, 020, 043–044 | SECURITY-STANDARDS.md §§12, 14–20, 35 |
| BPRD-REQ-044 | REQ-PRD-043 | BEB-REQ-045–046 | SECURITY-STANDARDS.md §§27–28; AGENTS.md §23 |
| BPRD-REQ-045 | REQ-PRD-015, 041–042, 052 | BEB-REQ-016, 018, 049–051 | TESTING-STANDARDS.md; API.md |
| BPRD-REQ-046 | REQ-PRD-044 | BEB-REQ-052–055 | TESTING-STANDARDS.md; ADR-0004; ARCHITECTURE.md §§35, 41–42 |

## 12. BEB Applicability and Inheritance Matrix

Every BEB Requirement is accounted for exactly once below. “Not currently applicable” means the capability is not authorized or selected for BPRD; the obligation remains conditionally inherited if later Approved governance introduces that capability.

| BEB Requirement | Classification | BPRD rationale and trace |
| --- | --- | --- |
| BEB-REQ-001, BEB-REQ-002, BEB-REQ-003, BEB-REQ-004 | Applicable | Lifecycle, inheritance, non-authority, and specialization govern BPRD directly. BPRD-REQ-001–004. |
| BEB-REQ-005, BEB-REQ-006, BEB-REQ-007 | Applicable | Modular-monolith, hexagonal direction, and layer ownership apply. BPRD-REQ-008, 010. |
| BEB-REQ-008 | Applicable | Product needs an intentional public Module Contract. BPRD-REQ-009, 040. |
| BEB-REQ-009, BEB-REQ-010 | Applicable | Product commands and queries require explicit Use Cases and truthful semantics. BPRD-REQ-010. |
| BEB-REQ-011 | Applicable | Protected Product actions require contextual Authorization. BPRD-REQ-007, 037, 043. |
| BEB-REQ-012 | Applicable | Product mutations require owned transaction boundaries. BPRD-REQ-011, 038–039. |
| BEB-REQ-013, BEB-REQ-014, BEB-REQ-015, BEB-REQ-016, BEB-REQ-017, BEB-REQ-018 | Applicable | Applicable Product HTTP and collection Contracts inherit versioning, DTO, validation, bounds, error, description, and evolution safeguards without defining concrete surfaces or values. BPRD-REQ-009, 023, 040, 045. |
| BEB-REQ-019, BEB-REQ-020 | Applicable | BPRD consumes Identity evidence and enforces Product authorization and concealment without Identity ownership. BPRD-REQ-005, 007, 037, 043. |
| BEB-REQ-021, BEB-REQ-022, BEB-REQ-023, BEB-REQ-024 | Applicable | Product owns its data and historical integrity through abstract persistence boundaries. BPRD-REQ-012–018, 020–030, 038. |
| BEB-REQ-025 | Applicable | Product invariant-preserving writes require explicit atomicity. BPRD-REQ-011, 038–039. |
| BEB-REQ-026 | Not currently applicable | No Product external provider or network workflow is selected; inherited if later Approved media or other integration requires one. BPRD-REQ-011, 029, 041. |
| BEB-REQ-027 | Applicable | Concurrent Product mutation must preserve identity, lifecycle, uniqueness, and accepted truth. BPRD-REQ-015, 019–021, 039. |
| BEB-REQ-028 | Applicable | Governed publication, correction, media, or integration work may require explicit durable uncertain state. BPRD-REQ-011, 021, 028–030, 042. |
| BEB-REQ-029, BEB-REQ-030, BEB-REQ-031 | Applicable | Duplicate harmful Product mutations require owned duplicate and replay safety without selected keys or retention. BPRD-REQ-015, 039, 042. |
| BEB-REQ-032, BEB-REQ-033, BEB-REQ-034 | Not currently applicable | No Product media or other provider is selected; inherited if a later Approved provider capability exists. BPRD-REQ-029, 041–042. |
| BEB-REQ-035, BEB-REQ-036 | Not currently applicable | No Product Callback or Webhook capability is authorized; authenticity, replay, acknowledgement, and recovery obligations remain conditional. BPRD-REQ-041–042. |
| BEB-REQ-037, BEB-REQ-038, BEB-REQ-039 | Not currently applicable | Product events are conditional and no event Contract or delivery mechanism is authorized; inherited if later governed. BPRD-REQ-041. |
| BEB-REQ-040 | Applicable | BPRD must preserve in-process-first delivery and may not adopt external messaging independently. BPRD-REQ-041. |
| BEB-REQ-041, BEB-REQ-042 | Applicable | Product failures, correction, recovery, and reconciliation require safe classification and handling. BPRD-REQ-030, 042. |
| BEB-REQ-043, BEB-REQ-044 | Applicable | Product content, media metadata, administration, and evidence require security, data minimization, and Secret safety. BPRD-REQ-043. |
| BEB-REQ-045, BEB-REQ-046 | Applicable | Material Product actions require distinct Audit Records and safe operational evidence. BPRD-REQ-021, 025, 037, 044. |
| BEB-REQ-047, BEB-REQ-048 | Applicable | Configuration and every reachable feature-flag state must preserve Product safeguards without selecting mechanisms. BPRD-REQ-011. |
| BEB-REQ-049, BEB-REQ-050, BEB-REQ-051 | Applicable | Product persistence, Contracts, deployments, and workloads require safe migration, compatibility, bounds, and failure containment. BPRD-REQ-011, 038, 045. |
| BEB-REQ-052, BEB-REQ-053, BEB-REQ-054, BEB-REQ-055 | Applicable | Product behavior, Adapters, Contracts, architecture, operations, and traceability require complete realistic verification. BPRD-REQ-046. |
| BEB-REQ-056 | Applicable | BPRD must remain policy- and implementation-neutral and preserve all later roadmap decisions. BPRD-REQ-001–004, 017, 025, 029, 031–041, 045–046. |

## 13. BIDN Consumption Boundary

| BIDN evidence or Contract | Permitted BPRD use | Preserved boundary |
| --- | --- | --- |
| Trusted Principal and Authentication context | Establish the trusted actor context for a protected Product Use Case. | BIDN owns Principal and Authentication truth; BPRD still decides Product contextual Authorization. |
| Session and revocation evidence | Determine whether trusted Identity context remains applicable. | BIDN owns Session and revocation truth; neither proves Product authority or success. |
| Role, Permission, Claims, Scope, and access evidence | Supply security context where governed. | BIDN owns access evidence; Product retains Resource/action/state Authorization and no matrix is invented. |
| Identity failure, uncertainty, and correlation | Preserve distinguishable protected-operation outcomes. | BPRD cannot reinterpret uncertain or failed Identity evidence as Product authorization or truth. |

## 14. BCUS Consumption Boundary

| BCUS evidence or Contract | Permitted BPRD use | Preserved boundary |
| --- | --- | --- |
| Governed Customer or Account context | Apply only an Approved Product visibility or eligibility input where Product governance requires it. | BCUS owns Customer and Account truth; context does not create Product, Authorization, Price, Stock, or purchasability truth. |
| Preference or Consent evidence | Apply only a governed purpose where Approved policy exists. | BCUS owns Customer Preference and Consent; BPRD selects no taxonomy, personalization, analytics, or communication policy. |
| Customer-context failure, staleness, and correlation | Preserve explicit unavailable, stale, denied, or uncertain outcomes. | BPRD cannot fabricate Customer state, infer eligibility, or convert BCUS evidence into Product truth. |

## 15. Open Product and Architecture Decisions

The following 20 materially relevant decisions remain unresolved by BPRD.

| Source | Open decision | BPRD boundary |
| --- | --- | --- |
| PRODUCT.md §24 item 1 | Final brand name and visual identity | BPRD does not define brand identity or presentation policy. |
| PRODUCT.md §24 item 2 | Initial product categories and catalogue taxonomy | Category remains authoritative; BPRD defines no taxonomy or Category placement. |
| PRODUCT.md §24 item 3 | Size, fit, colour, material, and other Product Variant or Attribute standards | BPRD selects no Attribute taxonomy, vocabulary, value, or format. |
| PRODUCT.md §24 item 15 | Product-review support | BPRD neither introduces reviews nor assigns their ownership or policy. |
| PRODUCT.md §24 item 17 | Low-stock and out-of-stock customer messaging | Inventory owns availability; BPRD selects no message or threshold. |
| PRODUCT.md §24 item 20 | Initial analytics provider and event taxonomy | BPRD selects neither provider nor analytics taxonomy. |
| PRODUCT.md §24 item 22 | Content approval and scheduled-publication workflow | BPRD selects no approval chain, schedule, workflow, Role, or mechanism. |
| PRODUCT.md §24 item 23 | Administrative role and permission matrix | BPRD enforces contextual Authorization but defines no Role or Permission matrix. |
| PRODUCT.md §24 item 27 | Product data import, export, and migration requirements | BPRD selects no format, source, schedule, mechanism, or migration policy. |
| PRODUCT.md §24 item 30 | Product launch date, release scope, and post-launch support window | BPRD selects no release date, scope, sequence, or support window. |
| ARCHITECTURE.md §34 item 1 | Azure App Service versus Azure Container Apps for backend hosting | BPRD selects no host, provider, or topology. |
| ARCHITECTURE.md §34 item 2 | Bicep versus Terraform for infrastructure as code | BPRD selects no infrastructure-as-code mechanism. |
| ARCHITECTURE.md §34 item 3 | Customer and administrator session/token strategy | BPRD consumes governed BIDN evidence and selects no token, Session, cookie, storage, rotation, or lifetime mechanism. |
| ARCHITECTURE.md §34 item 8 | Redis introduction and approved use cases | BPRD assumes no Redis, cache, Session, or storage technology. |
| ARCHITECTURE.md §34 item 9 | External messaging introduction and service selection | BPRD assumes no broker or external messaging adoption. |
| ARCHITECTURE.md §34 item 10 | Initial search implementation details and extraction thresholds | Search remains separate; BPRD selects no index, provider, extraction, ranking, or threshold. |
| ARCHITECTURE.md §34 item 11 | Product-media upload and transformation strategy | BPRD selects no upload, transformation, storage, delivery, provider, or pipeline. |
| ARCHITECTURE.md §34 item 12 | Backup retention and production recovery objectives | BPRD preserves recoverability and restoration compatibility without selecting backup mechanisms, retention, objectives, or numerical targets. |
| ARCHITECTURE.md §34 item 13 | PostgreSQL schema strategy for enforcing Domain ownership | BPRD selects no physical schema or ownership mechanism. |
| ARCHITECTURE.md §34 item 14 | Repository-wide feature-flag implementation and lifecycle management | BPRD preserves flag safety without selecting tooling, rollout, or lifecycle. |

Every Product taxonomy, Attribute standard, review, publication-workflow, Product data movement, media, Search, cache, provider, infrastructure, Contract, persistence, event, identifier, access-matrix, and numerical choice listed above remains unresolved. Category and Search and Discovery backend decomposition remain unresolved. Every Backend Specification title, path, scope code, decomposition, and ordering position after BPRD remains unresolved.

## 16. Explicit Exclusions

BPRD does not define concrete routes, HTTP operations or statuses, DTOs, payloads, schemas, tables, columns, SQL, ORM mappings, indexes, constraints, event names or schemas, topics, queues, brokers, providers, infrastructure, deployment topology, Redis use, media or Search mechanisms, identifier formats, numerical limits, retries, timeouts, retention, SLAs, SLOs, Product taxonomy, Attribute standards, reviews, Role or Permission matrices, publication workflow mechanics, or downstream commerce policy.

BPRD does not establish Category or Search and Discovery backend placement and does not establish any Backend Specification title, path, scope code, decomposition, or order after BPRD.

## 17. Risks and Controls

| Risk | BPRD control direction |
| --- | --- |
| “Catalogue” absorbs Category or Search authority | Enforce Product-only decomposition and explicit Category and Search boundaries. |
| Product state becomes final purchasability | Require current Pricing, Inventory, Customer, Checkout, and other owning-Domain evidence. |
| Media success publishes Product content | Keep media processing separate from Product publication authority. |
| Search or cache becomes Product truth | Treat indexes, results, caches, and Projections as stale-capable non-authoritative representations. |
| Identity or Customer context becomes Product Authorization | Require Product Resource/action/state authorization independently. |
| Concurrent change corrupts lifecycle or Variant uniqueness | Preserve accepted state through governed concurrency and explicit conflict. |
| Product correction rewrites commercial history | Preserve stable references and owning-Domain snapshots. |
| Sensitive pre-release or provider material leaks | Minimize and protect data across Contracts, storage, evidence, and observability. |
| Concrete design becomes accidental policy | Prohibit ungoverned Contract, schema, event, provider, media, Search, topology, and numerical choices. |

## 18. Related Documents

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
- `specifications/business/business-requirements.md`
- `specifications/adr/ADR-0004-catalogue-backend-specification.md`
- `specifications/backend/shared/backend-baseline.md`
- `specifications/backend/identity/identity-backend.md`
- `specifications/backend/customer/customer-backend.md`
- `specifications/domains/product/product-domain.md`
- `specifications/domains/category/category-domain.md`
- `specifications/domains/search/search-domain.md`

## 19. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-17 | Draft | Initial Product Catalogue Backend Specification established under Accepted ADR-0004 with Product-only decomposition, complete BEB applicability, bounded BIDN and BCUS consumption, and preserved Category and Search and Discovery authority. |

## 20. Final Validation

Before approval, revision, or implementation reliance, reviewers MUST verify that:

1. metadata is `0.1.0 Draft`, `authoritative: false`, scope is `BPRD`, and Draft content is non-normative;
2. BPRD remains the Product-only third downstream Backend Specification after BEB, immediately after BIDN and BCUS;
3. the Approved Product Domain retains semantic authority and Category and Search and Discovery remain separate Domain authorities;
4. all 56 BEB Requirements are classified exactly once and every applicable or capability-conditional obligation is explicitly traced;
5. materially applicable BIDN and BCUS evidence is consumed without transferring Identity, Customer, or Account authority;
6. Authentication evidence, Customer context, identifiers, labels, Claims, Scope, UI state, or prior responses alone authorize no Product action;
7. no API, DTO, schema, event, provider, infrastructure, media, Search, cache, identifier, matrix, numerical value, or unresolved Product policy is invented;
8. every BPRD Requirement has exactly one complete Acceptance Criterion and one traceability row;
9. all 20 listed Open Product and Architecture Decisions remain unresolved;
10. every Backend Specification title, path, scope code, decomposition, and order after BPRD remains unresolved;
11. all Related Documents exist and are materially relevant;
12. Markdown, tables, headings, UTF-8, whitespace, and final newline validation pass; and
13. the final change creates only `specifications/backend/product/product-backend.md`, remains unstaged, uncommitted, and unpushed.
