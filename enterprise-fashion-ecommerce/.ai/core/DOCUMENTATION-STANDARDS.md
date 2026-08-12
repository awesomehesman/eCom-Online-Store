---
title: DOCUMENTATION-STANDARDS
version: 1.0.1
status: Approved
owner: Engineering
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Documentation Standards

## 1. Purpose

Documentation is a first-class engineering artifact and the repository's durable memory. It reduces ambiguity, preserves decisions and traceability, supports consistent delivery and onboarding, strengthens operational resilience, and gives humans and AI Agents reliable context.

## 2. Scope

These standards apply to core governance, Product and Architecture documentation, `GLOSSARY.md`, Specifications, ADRs, API and data Contracts, security and testing documentation, coding guidance, runbooks, incident and migration records, provider integration documentation, README files, generated documentation, and AI steering or context files.

## 3. Repository Authority

Documentation decisions MUST follow the Decision Hierarchy and change process in `AGENTS.md`.

- `GLOSSARY.md` governs canonical terminology.
- `PRODUCT.md` governs Product intent and business semantics.
- `ARCHITECTURE.md` governs approved structure and ownership.
- `SECURITY-STANDARDS.md` governs mandatory security requirements.
- `TESTING-STANDARDS.md` governs verification.
- `CODING-STANDARDS.md` governs implementation practices.
- `ENGINEERING-PRINCIPLES.md` provides durable decision heuristics within the constraints of governing Product, Architecture, security, testing, coding, and repository governance.
- `DECISIONS.md` governs durable Decision Record structure, lifecycle, and indexing.
- This document governs documentation quality and lifecycle.

This document MUST NOT redefine the content authority of another governing source.

## 4. Documentation Principles

Each concern SHOULD have one authoritative source located close to its owner. Documentation SHOULD record decisions, constraints, assumptions, and non-obvious behavior rather than obvious syntax. Accuracy outranks volume.

Behavior changes MUST update affected documentation. Authors SHOULD link rather than copy, state scope and authority explicitly, keep content reviewable and versioned, and write so both humans and AI Agents can distinguish current rules from examples and future ideas.

## 5. Canonical Source of Truth

Each concern SHOULD have one authoritative governing source. Lower-level documents and derived views MAY elaborate but MUST NOT contradict it. References SHOULD point to the authoritative source instead of duplicating rules. Obsolete copies MUST be removed or clearly identified as deprecated or superseded.

## 6. Documentation Hierarchy

The repository documentation model reflects, but does not replace, the Decision Hierarchy:

| Document type | Role |
| --- | --- |
| `AGENTS.md` | Repository constitution, process, and Decision Hierarchy |
| `GLOSSARY.md` | Canonical terminology |
| `VISION.md` | Approved durable long-term strategic direction without creating current Product scope |
| `PRODUCT.md` | Product intent, semantics, and approved business behavior |
| `ARCHITECTURE.md` | Technical structure, ownership, and boundaries |
| Security, testing, and coding standards | Cross-cutting requirements in their owned concerns |
| Engineering principles | Approved durable decision heuristics within governing constraints |
| `DECISIONS.md` | Durable Decision Record governance and repository decision index |
| `DESIGN-SYSTEM.md` | Approved reusable visual, interaction, accessibility, component, token, and content guidance |
| Specifications | Concern-specific Requirements and behavior under applicable repository governance |
| ADRs | Material architecture decisions and consequences |
| Contracts | Agreed interfaces, schemas, and compatibility commitments |
| Runbooks | Safe operational procedures |
| README files | Local orientation and usage |

An unfinished document MUST NOT be represented as Approved or authoritative.

## 7. Document Metadata

Authoritative core documents MUST use YAML front matter with `title`, `version`, `status`, `owner`, `last_updated`, `review_cycle`, and an authority marker established by current core-document practice: `authoritative` or `source_of_truth`. Other normative documents SHOULD use the same fields where lifecycle and ownership matter. Informational local documentation MAY use lighter metadata when ownership remains discoverable.

## 8. Document Status Lifecycle

- **Draft** content is under development and is not yet an Approved normative baseline.
- **Approved** content has accepted review evidence and is normative within its assigned authority.

Deprecation and supersession MUST be documented explicitly without inventing unsupported `status` values. Deprecated content is retained temporarily but discouraged and scheduled for replacement or removal; superseded content is historical and MUST point to its replacement.

Approval MUST be performed by authorized owners or reviewers under repository governance. Status changes MUST be explicit and reflected in revision history where required.

## 9. Versioning Standards

Authoritative documents SHOULD use semantic-style versions: major for authority or meaning-breaking change, minor for meaningful compatible additions, and patch for corrections or clarifications. This convention communicates impact but is not software-package SemVer. Authoritative core documents MUST maintain revision history.

## 10. Ownership

Every authoritative document MUST identify an accountable owner. The owner coordinates changes, performs or delegates scheduled review, keeps links and terminology current, responds to drift, and ensures deprecated or superseded material is handled safely.

## 11. Review Cycles

Review cadence MUST reflect document Risk and change rate. Reviews SHOULD also be triggered by material Product, Architecture, security, provider, or governance changes; significant incidents; and upstream source changes. Not every document requires the same cadence.

## 12. Naming and File Placement

Names and repository-relative paths MUST be predictable and stable. Core governance belongs under `.ai/core`; domain-local documentation SHOULD remain near its owner. Ambiguous names and sequences such as `final-v2-new` are prohibited; use stable names, metadata, and revision history.

## 13. Headings and Structure

Documents SHOULD have one H1 title, descriptive headings, consistent hierarchy, and sequential numbering when that document style uses it. Heading-only and empty sections are prohibited. Excessive nesting SHOULD be avoided. Tables SHOULD be used when they improve comparison or mapping.

## 14. Writing Style

Technical documentation MUST be precise, concise but complete, and explicit about actors and outcomes. Active voice SHOULD be used where practical. Standards MUST use consistent normative language and MUST NOT rely on vague instructions such as “handle appropriately.” Edge cases and failure behavior SHOULD be stated where material. Marketing language does not belong in technical standards.

## 15. Canonical Terminology

Canonical terms MUST use the exact spelling and capitalization in `GLOSSARY.md`. Authors MUST NOT introduce aliases or alternate capitalization. Ordinary prose should remain ordinary prose. A glossary addition is appropriate only for a genuinely repository-wide concept.

## 16. Acronyms and Abbreviations

Acronyms SHOULD be expanded on first use unless universally understood or glossary-defined. Canonical acronyms MUST remain consistent. Team-local abbreviations and shortened aliases for canonical concepts SHOULD NOT appear in authoritative documentation.

## 17. Cross-References

Repository documents SHOULD use stable repository-relative links. Authors MUST link the authoritative source rather than copying it. References MUST be updated when files move. External links MAY support context, but internal documentation SHOULD preserve the repository-owned interpretation and decision.

## 18. Avoiding Duplication

Downstream documents MUST NOT restate complete security, testing, coding, Architecture, or Product rules. They SHOULD summarize only necessary context and link the governing source. Deliberate duplication MUST be clearly identified as non-authoritative and kept synchronized or generated.

## 19. Requirements Documentation

A Requirement SHOULD state intent, actor, triggering condition, expected outcome, constraints, failure behavior, and traceability. Implementation detail belongs only when it is itself an approved constraint.

## 20. Acceptance Criteria

Acceptance Criteria MUST be observable, testable, unambiguous, free of hidden assumptions, and consistent with `TESTING-STANDARDS.md`. Positive and negative paths SHOULD be included where material.

## 21. Specifications

A Specification SHOULD define purpose, scope, Requirements, Acceptance Criteria, canonical domain terminology, lifecycle or state behavior, API and data impact, security impact, testing strategy, dependencies, unresolved questions, and migration or rollout where relevant. It MUST NOT redefine canonical terms or silently weaken higher-authority requirements.

## 22. Architecture Documentation

Architecture documentation SHOULD explain relevant context, boundaries, Modules, Ports, Adapters, data ownership, integrations, deployment, trust and transaction boundaries, and failure behavior. It MUST reference `ARCHITECTURE.md` and MUST NOT recreate the entire canonical architecture.

## 23. ADR Standards

An ADR is appropriate for a material architecture decision, new technology, cross-domain pattern, data-ownership change, irreversible or high-cost choice, security architecture change, or compatibility-breaking architecture decision. Trivial implementation choices do not require ADRs.

An ADR SHOULD contain title, status, context, decision, alternatives considered, consequences, security impact, migration or rollback where relevant, and references.

## 24. API Documentation

API documentation MUST cover purpose, Authentication, Authorization, request and response schemas, errors and Problem Details, representative safe examples, and, where applicable, Idempotency Key semantics, pagination, and versioning. Webhook documentation MUST explain applicable verification, replay, and failure semantics. Live Secrets and sensitive example data are prohibited.

## 25. Event and Messaging Documentation

Event documentation MUST distinguish Domain Events from Integration Events and describe Message Envelope, schema, producer, consumers, ownership, version, retry, duplicate delivery, ordering, idempotency, dead-letter behavior, correlation, and causation where applicable.

## 26. Database Documentation

Data documentation SHOULD identify authoritative owner, schema responsibility, important constraints and relationships, migrations, material indexes, retention, sensitive data, lifecycle, and backup or recovery assumptions. It SHOULD explain decisions rather than duplicate a generated schema dump.

## 27. Security Documentation

Security documentation MUST follow `SECURITY-STANDARDS.md`. Trust boundaries, sensitive data, privileged flows, threat models, Security Exceptions, security-sensitive provider integrations, and incident-relevant controls SHOULD be documented where applicable. Live Secrets MUST NOT appear.

## 28. Testing Documentation

Testing documentation MUST follow `TESTING-STANDARDS.md`. It SHOULD record, where applicable, non-obvious strategy, critical scenarios, test-data assumptions, provider sandboxes, Environment dependencies, known limitations, and required manual verification.

## 29. Operational Runbooks

A runbook SHOULD define purpose, trigger, prerequisites, Permissions, safe steps, verification, rollback or recovery, escalation, observability, and warnings. Production-impacting actions MUST be explicit, authorized, bounded, and safe.

## 30. Incident Documentation

Incident records SHOULD capture timeline, impact, detection, containment, root cause, recovery, customer or business effect, evidence, follow-up, lessons, and required documentation changes. Language MUST remain factual and blameless.

## 31. Migration Documentation

A material migration MUST document starting and target states, compatibility, rollout, rollback or recovery, data movement, verification, monitoring, ownership, and failure handling.

## 32. External Provider Documentation

External System documentation SHOULD describe, where applicable, purpose, owner, Contract, Authentication, rate limits, retries, failure semantics, sandbox or test requirements, Webhook behavior, exchanged data, and operational dependencies for Payment Provider, Identity Provider, shipping, email, analytics, and cloud integrations.

## 33. Payment Documentation

Payment documentation MUST use Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key consistently.

It MUST state that a Payment Redirect and client-reported success are not payment proof, authoritative Payment state requires validated Payment Provider evidence, and retry or replay MUST NOT create duplicate financial effects. Reconciliation, PCI-sensitive boundaries, and appropriate Audit Records for material financial transitions MUST be documented where applicable.

## 34. Inventory Documentation

Inventory documentation MUST use Inventory, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement consistently. It SHOULD describe ownership, invariants, concurrency, Stock Reservation semantics, Available-to-Sell meaning, and failure or recovery behavior.

## 35. Identity and Authorization Documentation

Identity documentation MUST use Identity, Principal, Authentication, Authorization, Role, Permission, Claims, Scope, Session, Access Token, Refresh Token, and MFA consistently. It SHOULD explain, where applicable, identity source, trust boundaries, Authorization rules, Scope and Permission behavior, token or Session lifecycle, and privileged flows without exposing sensitive configuration.

## 36. README Standards

A README SHOULD explain purpose, prerequisites, local setup, run, test, build, configuration, ownership or contact, and links to authoritative documentation. README files MUST NOT become competing Product, Architecture, security, testing, or coding authorities.

## 37. Code Comments

Code comments MUST follow `CODING-STANDARDS.md`. Comments are appropriate for non-obvious rationale, constraints, verified provider quirks, security reasoning, and business invariants. They SHOULD NOT restate obvious code.

## 38. Diagrams

Diagrams SHOULD be used when they materially clarify system context, component or Module boundaries, sequence flows, state transitions, deployment, data flow, or trust boundaries. They MUST be version-controlled where practical, and important semantics MUST remain understandable without hidden external context.

## 39. Diagram Source

Text or source-based diagrams SHOULD be preferred where maintainable. When images are used, editable source MUST be retained where possible. Stale diagrams MUST be updated or removed, and accompanying text SHOULD explain their meaning.

## 40. Examples

Examples MUST be plausible, use safe synthetic data, avoid real Secrets, PII, and payment data, and align with current Contracts. Non-executable examples MUST be labeled illustrative. Examples MUST NOT create hidden normative requirements.

## 41. Generated Documentation

Generated documentation MAY cover APIs, schemas, dependency reports, coverage, and inventories. It MUST identify its source, be reproducible, avoid manual edits when generator-owned, and supplement rather than replace human-authored rationale.

## 42. AI-Facing Documentation

AI-facing documentation SHOULD be explicit, canonical, stable in path, and clear about authority, forbidden behavior, and the difference between rules, examples, current Approved behavior, and future ideas. It MUST NOT contain Secrets or unsafe operational credentials.

## 43. Documentation for AI-Generated Changes

AI-generated changes MUST update affected documentation when they alter behavior, Contracts, Requirements, Architecture, security, or operational assumptions. AI MUST NOT silently change implementation while leaving governing documentation stale.

## 44. Traceability

Material documentation SHOULD support traceability among Requirement, Acceptance Criterion, Specification, ADR, Contract, implementation, test, release evidence, and incident evidence. Trivial implementation details SHOULD NOT create traceability bureaucracy.

## 45. Change Synchronization

When code or configuration changes documented behavior, documentation MUST change in the same change set where practical. Known stale documentation MUST NOT remain authoritative. Dependent documents SHOULD be updated or linked to the new source.

## 46. Documentation Drift

Drift includes code contradicting documentation, removed APIs still referenced, broken examples, changed ownership or terminology, changed lifecycles, and deprecated technology described as current. Material drift MUST be corrected or escalated for formal authority and status handling through repository governance; materially stale content MUST NOT continue to be relied upon as current authority.

## 47. Deprecation and Supersession

Documents retained after deprecation MAY remain for useful historical context. A superseded document MUST point to its replacement. Obsolete normative guidance MUST NOT remain discoverable as current authority; deletion MAY be preferable when history adds no value.

## 48. Historical Documentation

Historical documentation MAY be retained but MUST be clearly labeled so humans and AI Agents do not treat it as current authority.

## 49. Documentation Review Checklist

Reviewers SHOULD verify:

- correct owner, status, version, and governing source;
- canonical terminology and no conflicting duplication;
- testable Requirements and Acceptance Criteria where relevant, and documented security implications;
- current diagrams, valid links, and safe examples;
- absence of Secrets and stale references; and
- updated revision history where required.

## 50. Forbidden Documentation Practices

The following are prohibited:

- undocumented authoritative behavior and duplicate conflicting standards;
- unresolved TODO, TBD, or `...` placeholders in Approved documents;
- live Secrets, production credentials, real CVV or payment data, and real customer PII in examples;
- undocumented manual production steps;
- screenshots as the sole source of critical technical truth;
- stale diagrams presented as current;
- copied canonical definitions that drift from `GLOSSARY.md`;
- marking a document Approved without review evidence; and
- claiming repository-wide validation that was not performed.

## 51. Documentation Exceptions

A Documentation Exception MUST identify the exact Requirement, rationale, Risk, scope, owner, approver, expiry, compensating controls, and remediation. It MUST be explicit, time-bound, and auditable. It MUST NOT waive Product, Architecture, security, testing, coding, or governance requirements owned elsewhere.

## 52. Documentation Compliance Matrix

| Concern | Governing Source | Documentation Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `AGENTS.md` | Preserve authority and process | Decision Hierarchy review |
| Terminology | `GLOSSARY.md` | Use canonical terms | Terminology review |
| Product | `PRODUCT.md` | Record intent without redefining it | Requirement traceability |
| Architecture | `ARCHITECTURE.md` | Explain approved decisions and boundaries | Architecture review |
| Security | `SECURITY-STANDARDS.md` | Record security context safely | Security review and exceptions |
| Testing | `TESTING-STANDARDS.md` | Record verification context | Test evidence and limitations |
| Coding | `CODING-STANDARDS.md` | Document implementation guidance | Code and documentation review |
| Engineering Principles | `ENGINEERING-PRINCIPLES.md` | Explain decision rationale | Decision review |
| Requirements | Owning Product or Specification source | Make outcomes verifiable | Acceptance Criteria |
| Specifications | `AGENTS.md`; applicable Product and Architecture sources | Keep scope and dependencies explicit | Specification review |
| ADRs | `DECISIONS.md`; `ARCHITECTURE.md` | Record material Architecture Decisions | Accepted ADR |
| APIs | Approved API Contract | Document behavior and compatibility | Contract review |
| Events | `ARCHITECTURE.md`; approved event Contract | Document ownership and delivery semantics | Schema and consumer review |
| Database | `ARCHITECTURE.md` | Document ownership and lifecycle | Migration and data review |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Preserve authoritative evidence semantics | Payment review and Audit Records |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Preserve Stock ownership and invariants | Domain and concurrency review |
| Identity | `SECURITY-STANDARDS.md` | Record trust and access rules | Authorization review |
| Operations | `ARCHITECTURE.md` | Provide safe executable guidance | Runbook validation |
| Incidents | `SECURITY-STANDARDS.md`; applicable operational process | Preserve evidence and learning | Incident review |
| AI documentation | `AGENTS.md`; `SECURITY-STANDARDS.md` | Expose safe authoritative context | Human review |
| Exceptions | Owning governing standard | Record scope and expiry | Approved exception record |

### Compliance Interpretation

Documentation standards govern how information is recorded and maintained. They MUST NOT override the content authority of governing documents.

## 53. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/VISION.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DECISIONS.md`
- `.ai/core/DESIGN-SYSTEM.md`

## 54. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.1 | 2026-08-12 | Approved | Corrected the stale Vision status and refreshed directly relevant Vision, Decision Record, and Design System documentation references. |
| 1.0.0 | 2026-08-11 | Approved | Promoted the repository-wide documentation standards after final governance, terminology, lifecycle, requirements, architecture, security, testing, operational, AI-context, traceability, and documentation-quality validation. |
| 0.1.0 | 2026-08-11 | Draft | Established the initial repository-wide documentation standards covering authority, structure, lifecycle, versioning, Requirements, Specifications, ADRs, technical documentation, operations, AI-facing context, traceability, drift, and exception governance. |
