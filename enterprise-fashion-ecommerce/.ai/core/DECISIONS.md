---
title: DECISIONS
version: 1.0.0
status: Approved
owner: Architecture
last_updated: 2026-08-12
authoritative: true
review_cycle: Quarterly
---

# Decisions

## 1. Purpose

This document defines how material repository decisions are captured, categorized, reviewed, approved, superseded, and traced. Durable decision records preserve repository memory, reduce repeated debate, make trade-offs and consequences discoverable, support onboarding and auditability, and give humans and AI Agents reliable context beyond pull requests, tickets, comments, or chat history.

This document is the repository-wide index and operating standard for decision-recording discipline. It does not replace the Decision Hierarchy, governing Product or Architecture sources, detailed ADRs, Specifications, Contracts, implementation standards, or delivery tracking.

The terms MUST, MUST NOT, SHOULD, SHOULD NOT, and MAY are normative. MUST and MUST NOT define mandatory requirements; SHOULD and SHOULD NOT define strong defaults whose departure requires documented rationale; MAY defines an allowed option.

## 2. Scope

This standard applies to material decisions concerning:

- Product direction, scope, policy, and capability adoption;
- Architecture, Module boundaries, domain ownership, and technology;
- data ownership, persistence, retention, projections, and migration;
- APIs, Contracts, schemas, compatibility, and breaking changes;
- security architecture, Identity, Authentication, and Authorization;
- testing strategy and repository-wide engineering practices;
- deployment, infrastructure, operations, reliability, and operational Risk;
- External Systems and provider selection or replacement;
- Payment, Inventory, Order, and other high-impact domain behavior;
- AI-assisted development, AI product capabilities, permissions, and authority boundaries; and
- decisions that materially affect cost, reversibility, maintainability, or multiple owners.

This standard governs the recording process. The governing source for each decision's substantive content retains its existing authority.

## 3. Repository Authority

Repository decisions MUST follow the Decision Hierarchy in `AGENTS.md`.

| Governing source | Relationship to decisions |
| --- | --- |
| `AGENTS.md` | Governs repository authority, contributor behavior, process, and the Decision Hierarchy. |
| `GLOSSARY.md` | Governs canonical terminology used in decisions and the index. |
| `VISION.md` | Governs durable long-term strategic direction without creating current Product commitments. |
| `PRODUCT.md` | Governs the current Approved Product model, scope, semantics, policies, and Requirements. |
| `ARCHITECTURE.md` | Governs the approved technical structure, ownership, boundaries, and technology baseline. |
| `SECURITY-STANDARDS.md` | Governs mandatory security requirements and Security Exceptions. |
| `TESTING-STANDARDS.md` | Governs verification requirements and Testing Exceptions. |
| `CODING-STANDARDS.md` | Governs implementation quality and Coding Exceptions. |
| `ENGINEERING-PRINCIPLES.md` | Provides durable engineering decision heuristics within governing constraints. |
| `DOCUMENTATION-STANDARDS.md` | Governs documentation quality, lifecycle, and Documentation Exceptions. |
| `DECISIONS.md` | Governs decision-recording discipline and the repository decision index. |

A decision record MUST NOT override a higher-authority Canonical Document or mandatory requirement. An Accepted decision that changes a governing source MUST update that source through its approved change process. The decision record explains the choice; it does not become a silent substitute for synchronized Product, Architecture, security, testing, coding, or documentation governance.

## 4. Decision Principles

Decision governance follows these principles:

- durable records over ephemeral discussion;
- evidence over preference;
- explicit trade-offs over hidden assumptions;
- ceremony proportional to impact, Risk, cost, and reversibility;
- one authoritative record for each decision;
- an unambiguous current status;
- visible benefits, costs, Risks, and operational consequences;
- traceable supersession rather than rewritten history; and
- records located near the affected ownership and linked from this index.

A decision record SHOULD be concise enough to review while complete enough to explain why the repository chose the approach.

## 5. What Requires a Decision Record

A durable decision record is REQUIRED when a choice materially:

- introduces, replaces, or standardizes a framework, library, platform, runtime, managed service, or major technology;
- establishes or changes an architecture pattern, deployment model, Module boundary, or domain ownership;
- establishes a cross-domain Contract or changes authoritative data ownership;
- introduces a breaking API, Integration Event, Message, database schema, or provider Contract change;
- changes security architecture, a trust boundary, privileged access, cryptography, or key-management architecture;
- selects or materially changes a Payment Provider, Identity Provider, hosting, infrastructure, search, messaging, shipping, notification, analytics, or other provider strategy;
- changes Payment architecture, Inventory consistency, migration strategy, compatibility policy, or recovery behavior;
- creates an irreversible, high-cost, high-Risk, or operationally burdensome choice;
- establishes a new repository-wide or cross-cutting standard;
- establishes an AI authority, tool-permission, data-exposure, provider, or Human Approval Gate boundary; or
- turns an exception or repeated local workaround into an intended repository pattern.

An ADR is REQUIRED for material Architecture Decisions under `AGENTS.md` and `ARCHITECTURE.md`. Other material decisions MUST use the general Decision Record mechanism defined in sections 9 and 10 rather than being labeled as ADRs.

## 6. What Does Not Require a Decision Record

A separate decision record is normally unnecessary for:

- routine refactoring that preserves approved behavior and boundaries;
- formatting or configured style changes;
- local naming choices already governed by standards and `GLOSSARY.md`;
- an obvious defect correction that restores approved behavior;
- implementation details already determined by an Approved Specification, Contract, standard, or ADR;
- small, reversible local choices with no cross-team, cross-domain, Contract, security, data, or operational impact; and
- ordinary application of an established repository pattern.

If a supposedly local choice changes ownership, authority, a public Contract, a mandatory control, or a difficult-to-reverse path, it is material and requires the applicable record.

## 7. Decision Types

The following labels organize decision records and the index:

| Type | Typical concern |
| --- | --- |
| Product Decision | Product scope, policy, market, channel, or capability adoption. |
| Architecture Decision | Structure, ownership, technology, deployment, or cross-domain design. |
| Security Decision | Trust, access, cryptography, security architecture, or control design. |
| Data Decision | Authoritative ownership, schema strategy, retention, projection, or migration. |
| Integration Decision | External System, provider, API, Contract, event, or messaging boundary. |
| Operational Decision | Deployment, reliability, observability, recovery, support, or capacity. |
| Engineering Practice Decision | Repository-wide development, testing, tooling, or delivery practice. |
| AI Governance Decision | AI provider, data, permission, authority, review, or Guardrail boundary. |

These labels are organizational categories for this index. They do not create new canonical glossary concepts or replace the authority of the governing source.

## 8. Decision Status

Each decision record MUST have exactly one current record status:

- **Proposed**: submitted for review but not authoritative or approved for implementation.
- **Accepted**: approved by the required authority and current unless superseded or changed through governance.
- **Rejected**: considered and declined; it is not authority.
- **Superseded**: replaced by a later Accepted decision identified by stable reference.

These are decision-record statuses, not YAML document lifecycle states. They MUST NOT be used to invent or alter the Draft and Approved lifecycle governed by `DOCUMENTATION-STANDARDS.md`.

## 9. Decision Identifier

A Decision is the recorded choice defined in `GLOSSARY.md`; a Decision Record is the durable artifact that captures it. An Architecture Decision is a Decision about material Architecture, and its Decision Record is an ADR.

An Architecture Decision MUST use one stable identifier in the repository ADR sequence:

```text
ADR-0001
ADR-0002
```

Every other durable Decision Record MUST use one stable identifier in the general decision sequence:

```text
DEC-0001
DEC-0002
```

`DEC` identifies a general Decision Record; it does not create a new decision type. An ADR is an Architecture Decision Record and is reserved for Architecture Decisions. Identifiers MUST be zero-padded to four digits, unique within their sequence, immutable, and never reused after rejection or supersession. The identifier MUST appear in the filename, title or metadata, index, references, and supersession links.

## 10. Decision Record Location

Architecture Decision Records belong in the verified repository ADR location established by `AGENTS.md`:

```text
specifications/adr/
```

Filenames MUST follow the repository convention:

```text
ADR-0001-concise-kebab-case-title.md
```

No repository location is currently established for non-Architecture Decision Records. The applicable governing authority MUST approve that location before such a record is added; this document MUST NOT invent a path in place of that approval. The Decision Index MUST link each record at its approved repository path.

`DECISIONS.md` is the governance document and index. It MUST NOT absorb the full body of every Decision Record. Domain Specifications, Product sources, and other governing documents MAY link to a Decision Record but MUST NOT create a competing copy of its rationale.

## 11. Minimum Decision Record Structure

Every decision record MUST contain:

- Identifier;
- Title;
- Status;
- Date;
- Owner;
- Context;
- Decision;
- Alternatives Considered;
- Consequences;
- Security Impact where relevant;
- Data Impact where relevant;
- Compatibility and Migration Impact where relevant;
- Operational Impact where relevant;
- References; and
- Supersedes and Superseded By fields where relevant.

Sections that are not applicable SHOULD state that they are not applicable with a concise reason rather than being omitted when omission could hide an unreviewed impact.

## 12. Context Quality

The Context section MUST explain the problem, current state, relevant constraints, why a decision is needed, affected Modules, affected stakeholders or consumers, available evidence, and known uncertainty.

Context MUST distinguish verified facts, assumptions, Requirements, existing Approved decisions, and open questions. It MUST be sufficient for a future reader to understand the decision without reconstructing private discussion.

## 13. Decision Statement

The Decision section MUST state the chosen action explicitly and within a defined boundary. It SHOULD be testable or independently verifiable where applicable and MUST distinguish the current choice from future possibilities.

Vague language, unstated scope, implicit ownership, and claims that every future case is covered are prohibited. A decision MUST identify material conditions or limits when the choice is conditional.

## 14. Alternatives Considered

The record MUST document realistic alternatives that materially informed the choice, including the status quo when it is credible. For each alternative, the record SHOULD explain the relevant trade-offs and why it was not selected.

Rejected options MUST be represented fairly. Authors MUST NOT fabricate alternatives or inflate weak options merely to make the chosen decision appear inevitable.

## 15. Consequences

The Consequences section MUST document material:

- benefits and enabled outcomes;
- costs and maintenance burden;
- Risks and limitations;
- security, privacy, data, and operational effects;
- compatibility and migration implications;
- delivery or support impact; and
- reversibility or exit cost.

Consequences MUST include unfavorable effects when known. Approval is not evidence that trade-offs disappeared.

## 16. Decision Ownership

Every material decision MUST have one accountable owner. The owner MUST:

- maintain the decision record and its references;
- coordinate required reviewers and approvers;
- ensure affected Canonical Documents, Specifications, Contracts, tests, and operational guidance are updated;
- monitor review triggers and known assumptions;
- keep status and supersession links current; and
- coordinate correction when implementation or documentation drifts.

Ownership MAY transfer through an explicit update that preserves history and accountability.

## 17. Approval

Approval MUST match the decision's authority and impact:

- Product Decisions require the Product authority defined by `PRODUCT.md` and applicable Product governance.
- Architecture Decisions require Architecture authority and affected owners under `ARCHITECTURE.md`.
- Security Decisions require the security and Architecture authority applicable to the affected control and MUST follow `SECURITY-STANDARDS.md`.
- Data, Integration, and cross-domain decisions require the affected Domain Owners and the governing Architecture or Product authority.
- Operational and Engineering Practice Decisions require the owners of the affected repository, delivery, or operational concern.
- AI Governance Decisions require the capability owner and applicable Security, Architecture, Product, and data authorities.

High-Risk or cross-cutting decisions MAY require multiple approvers. Pull-request approval alone is sufficient only when the required authorities are represented and the approval evidence is durable and discoverable.

## 18. Decision Review

A decision record SHOULD be reviewed for:

- correctness and alignment with Requirements;
- authority and ownership;
- canonical terminology;
- Product and Architecture consistency;
- security and privacy impact;
- authoritative data ownership and integrity;
- operational impact and supportability;
- API, event, schema, and provider compatibility;
- migration, rollback, recovery, and reversibility; and
- testing and other evidence where relevant.

Review depth MUST be proportional to Risk, cost, blast radius, and difficulty of reversal.

## 19. Decision Effective Date

An Accepted decision is effective on its stated effective date or, when no separate date is given, on its acceptance date. Acceptance authorizes the direction within the decision's authority; it does not claim that implementation, migration, rollout, or operational adoption is complete.

Implementation MUST NOT silently diverge from an Accepted decision. When the decision changes a governing source, that source MUST be synchronized through its approved process before the changed baseline is represented as current. Rollout state SHOULD remain traceable separately from decision status.

## 20. Supersession

Superseded records MUST remain in repository history and MUST retain their original context, decision, and consequences. A new decision MUST identify the record it supersedes, and the old record MUST identify the replacement.

The index MUST make the current Accepted decision unambiguous. Authors MUST NOT silently edit historical records to imply that an earlier choice or consequence never existed.

## 21. Rejected Decisions

A Rejected decision MAY be retained when its context and rejection rationale will prevent repeated debate, preserve important evidence, or explain constraints.

The record MUST clearly show Rejected status, reason, date, owner, and relevant context. A Rejected decision is historical information and MUST NOT be treated as authority or implementation approval.

## 22. Reversing a Decision

A material reversal SHOULD create a new decision record that supersedes the earlier record. The new record MUST explain changed conditions, new evidence, why reversal is justified, migration or rollback, and new consequences.

Minor factual corrections MAY update the existing record when they do not change the decision or historical meaning. Material reinterpretation MUST use supersession.

## 23. Decision Expiry and Time-Bounded Decisions

A temporary decision MUST state its expiry date or objective review trigger, owner, exit criteria, and required follow-up. The record MUST explain what happens at expiry and whether the prior baseline resumes, a migration occurs, or a new decision is required.

Temporary decisions MUST NOT silently become permanent. Renewal requires current evidence, review, and explicit approval.

## 24. Decision vs Exception

A Decision selects an approach within existing authority. An exception temporarily waives a specified mandatory Requirement through the formal process owned by the governing standard.

- A Security Exception is governed by `SECURITY-STANDARDS.md`.
- A Testing Exception is governed by `TESTING-STANDARDS.md`.
- A Coding Exception is governed by `CODING-STANDARDS.md`.
- A Documentation Exception is governed by `DOCUMENTATION-STANDARDS.md`.

A decision record MUST NOT be used to bypass, disguise, or make permanent an exception. Where a decision depends on an exception, both records MUST be linked, and the exception's scope and expiry remain controlling.

## 25. Decision vs Requirement

A Requirement defines a needed capability, behavior, quality, outcome, or constraint. A Decision selects an approach within the authority established by applicable Requirements and governing sources.

A decision MUST NOT silently change Approved Product Requirements. When Product scope, semantics, policy, or Requirements change, Product governance MUST update the applicable Product source and trace the decision.

## 26. Decision vs Specification

A Specification defines approved behavior, design detail, constraints, Acceptance Criteria, and implementation-facing expectations for its scope. A decision record explains why a material choice was made.

Specifications MAY reference applicable decisions. An Accepted decision MAY require Specification changes, but it MUST NOT leave an affected Specification contradicting the current governing source.

## 27. Decision vs Architecture

`ARCHITECTURE.md` defines the current canonical Architecture baseline. ADRs are Architecture Decision Records reserved for material Architecture Decisions and their evolution; non-Architecture decisions use the general Decision Record mechanism.

An Accepted ADR that changes the baseline MUST trigger an approved update to `ARCHITECTURE.md` and affected Architecture Specifications, diagrams, tests, and operational guidance. The ADR MUST NOT remain the only location where current Architecture is discoverable.

## 28. Decision vs Vision

`VISION.md` defines durable long-term strategic direction. A Decision selects a current action or approach within current authority.

A future possibility in Vision is not automatically an Approved Product commitment or Accepted decision. Decisions MUST remain within current Product and Architecture governance, and capability adoption requires the applicable approval and source updates.

## 29. Product Decisions

Product Decisions may address scope boundaries, channel commitments, supported markets, business policy, capability adoption, customer or Staff User outcomes, and provider strategy where Product-owned.

They MUST state affected Requirements, actors, policies, success and guardrail considerations, and current-versus-future scope. Product Decisions MUST be governed through `PRODUCT.md` and MUST NOT turn this document or a Decision Record into a Product backlog, roadmap, or issue tracker.

## 30. Architecture Decisions

Architecture Decisions may address a new Module boundary, extraction from the Modular Monolith, persistence technology, messaging pattern, search technology, deployment model, cross-domain Contract, provider abstraction strategy, or another material technical structure.

They MUST be recorded as ADRs and use ADR identifiers. They MUST document ownership, dependency direction, data and transaction boundaries, compatibility, security, operations, cost, migration, and reversibility as applicable. They MUST align with `ARCHITECTURE.md` until an Accepted ADR and synchronized Architecture update change the baseline.

## 31. Security Decisions

Security Decisions may address an Authentication model, privileged-access model, trust boundary, cryptographic or key-management Architecture, security control design, or another material security trade-off.

They MUST align with `SECURITY-STANDARDS.md`, identify affected assets and Principals, assess Risk, document controls and evidence, and preserve default denial and least privilege. A Security Decision MUST NOT substitute for a required Security Exception.

## 32. Data Decisions

Data Decisions may address Source of Truth ownership, schema strategy, retention, deletion, projection, analytics boundaries, migration, history, backup, or recovery.

They MUST identify the authoritative owner, writers, readers, invariants, lifecycle, Sensitive Data, compatibility, integrity controls, and migration implications. Derived views, caches, search indexes, reporting models, and analytics MUST NOT silently become authoritative transactional state.

## 33. API and Contract Decisions

API and Contract Decisions MUST address versioning, schemas, semantic meaning, compatibility, affected producers and consumers, errors, security, migration, and deprecation or removal where applicable.

Breaking changes require explicit approval, a migration path, coordinated rollout, rollback or recovery, and a compatibility window when required. Internal Contracts remain governed even when they are not public.

## 34. Event and Messaging Decisions

Event and messaging decisions MUST distinguish Domain Events from Integration Events and document the asynchronous boundary, producer, consumers, Message schema and version, delivery semantics, retry, duplicate delivery, Idempotent Consumer behavior, ordering assumptions, dead-letter handling, correlation, causation, and the Outbox Pattern where used.

Exactly-once delivery MUST NOT be assumed unless an explicit infrastructure Contract guarantees it. Business effects MUST remain correct under the actual delivery guarantee.

## 35. Payment Decisions

Material Payment decisions MUST use and preserve the distinctions among Payment, Payment Attempt, Payment Provider, Payment Redirect, Payment Authorization, Capture, Void, Payment Transaction, Refund, Refund Transaction, Chargeback, Settlement, and Idempotency Key.

The record MUST address:

- validated authoritative Payment Provider evidence;
- idempotency, replay safety, and duplicate-notification handling;
- prevention of unintended duplicate financial effects;
- reconciliation and traceability;
- PCI scope and Sensitive Data impact;
- operational support, monitoring, and Audit Records; and
- rollback, recovery, or compensating action.

A Payment Redirect or client-reported Payment success MUST NOT be treated as payment proof. Authoritative Payment state requires validated Payment Provider evidence.

## 36. Inventory Decisions

Material Inventory decisions MUST use Inventory, Stock, Stock Reservation, Available-to-Sell, Stock Adjustment, and Stock Movement consistently.

The record MUST document authoritative ownership, invariants, concurrency, overselling protection, Stock Reservation semantics, data integrity, idempotency, rollback or recovery, and migration impact. Client state, projections, search, reporting, and analytics MUST NOT become authoritative Inventory truth.

## 37. Identity and Authorization Decisions

Identity and Authorization decisions MUST use Identity, Principal, Authentication, Authorization, Role, Permission, Claims, Scope, Session, Access Token, Refresh Token, MFA, and Identity Provider consistently.

The record MUST document trust boundaries, authority, privilege, default denial, token or Session impact as applicable, revocation, migration, operational recovery, and required security review. Protocol-dependent fields and semantics MUST be stated as applicable rather than assumed universal.

## 38. Provider Decisions

Provider decisions include Payment Provider, Identity Provider, shipping, email, analytics, cloud, platform, storage, search, notification, and other External System choices.

They MUST document selection criteria, provider Contract, lock-in, failure model, rate or capacity constraints, operational dependence, data and privacy impact, security, cost, observability, support, migration, replacement, and exit strategy. Provider-specific models MUST remain at the appropriate Adapter or infrastructure boundary.

## 39. Build vs Buy Decisions

Build-versus-buy decisions MUST consider business differentiation, security, reliability, maintenance, cost, vendor Risk, lock-in, operational burden, team capability, migration, and time-to-value.

Commodity infrastructure SHOULD use suitable managed or established capabilities when they meet the Requirement. Custom implementation requires evidence that its value and control justify its lifecycle cost and Risk.

## 40. Technology Adoption Decisions

Before adopting a framework, library, platform, service, or tool, the decision MUST assess:

- the unmet need and existing repository capability;
- maturity, maintenance, support, security history, and provenance;
- licensing and ecosystem health;
- team skill and onboarding impact;
- integration, migration, and replacement cost;
- provider or technology lock-in;
- testing and compatibility;
- operational impact, observability, and support; and
- total lifecycle cost.

Trend, novelty, or contributor preference alone is insufficient evidence.

## 41. AI Decisions

AI decisions may concern AI-assisted development, Product AI capability, provider or model choice, tool permissions, data exposure, retrieval, Guardrails, Human Approval Gates, model-output authority, fallback, evaluation, monitoring, and auditability.

They MUST comply with `AGENTS.md` and `SECURITY-STANDARDS.md`. AI tool and production permissions MUST be least-privilege and bounded to approved data, tools, actions, and Environments. High-impact actions MUST use a Human Approval Gate unless an approved deterministic policy explicitly governs safe automation. AI MUST NOT gain authority, production access, permission to expose Sensitive Data, or permission to mutate high-impact state merely because a Decision Record exists. Authorization and domain enforcement MUST remain outside the model.

## 42. Migration Decisions

A material migration decision MUST document:

- current and target states;
- scope, owner, phases, and dependencies;
- API, event, schema, data, and deployment compatibility;
- data migration, backfill, or reconciliation;
- rollout and rollback or forward-fix strategy;
- verification and acceptance evidence;
- monitoring and operational support; and
- failure handling and stop conditions.

Migration sequencing SHOULD preserve a supported mixed-version window where required.

## 43. Breaking-Change Decisions

A breaking-change decision MUST explicitly identify affected consumers, owners, Contracts, versions, migration path, rollout order, rollback or recovery, communication, deprecation, and the compatibility window where applicable.

The record MUST explain why compatible evolution is insufficient and how unsupported versions or behavior will be retired safely.

## 44. Performance Decisions

Performance decisions SHOULD be evidence-driven. The record SHOULD document the measured bottleneck, workload, baseline, Environment, expected benefit, trade-offs, validation method, and effect on correctness, security, cost, and maintainability.

Targets MUST come from an Approved Requirement, SLO, capacity model, or measured baseline. A decision MUST NOT invent arbitrary latency, throughput, availability, or resource targets.

## 45. Reliability Decisions

Reliability decisions MUST document the failure mode, business impact, timeout, retry safety, idempotency, duplicate handling, reconciliation, recovery, observability, operational owner, and testing approach as applicable.

Unknown outcomes MUST remain visible until resolved. Reliability mechanisms MUST NOT create duplicate Payment, Inventory, Order, Identity, or external-communication effects.

## 46. Operational Decisions

Operational decisions may address deployment, rollback, runbooks, incident response, support, monitoring, alerting, capacity, backups, recovery, maintenance windows, or controlled production access.

They MUST identify the operational owner, affected Environments, permissions, failure modes, safe procedures, verification, rollback or recovery, escalation, evidence retention, and Customer or business impact where applicable.

## 47. Decision Evidence

Evidence MAY include metrics, Logs, Traces, incident history, tests, prototypes, benchmarks, cost analysis, provider documentation, user research, threat models, operational data, or migration rehearsals.

Evidence MUST be relevant, attributable, and represented accurately. Decision authors MUST distinguish measured results from forecasts and assumptions. A record MUST NOT require evidence that cannot reasonably exist before the decision, but it SHOULD define how material uncertainty will be validated after acceptance.

## 48. Decision Traceability

Material decisions SHOULD link, where relevant, to:

- Requirement and Acceptance Criterion;
- Specification;
- related Decision Record, including an ADR where applicable;
- Contract;
- implementation and migration;
- tests and Pipeline evidence;
- release or rollout record;
- incident or operational evidence; and
- superseding or superseded decision.

Traceability MUST be durable without creating bureaucracy for trivial implementation choices.

## 49. Decision Index

This table indexes real durable decision records. No entry may be added until its record exists at the linked repository path.

The ID column uses the applicable `ADR-XXXX` or `DEC-XXXX` identifier defined in section 9 and does not imply that every indexed record is an Architecture Decision Record.

| ID | Title | Type | Status | Owner | Date | Supersedes | Record |
| --- | --- | --- | --- | --- | --- | --- | --- |

The index contains only verified decision records that exist at their linked repository paths. No entries are currently indexed.

## 50. Decision Index Maintenance

The owner of a new or changed decision record MUST update this index in the same change. Maintenance MUST ensure:

- every Proposed and Accepted durable decision is indexed;
- Rejected records retained for durable context remain clearly identified;
- Superseded records remain indexed and traceable;
- status changes are synchronized;
- supersession references are bidirectional and current;
- owners, dates, titles, and links match the record;
- stale or broken links are corrected; and
- history and evidence are never fabricated.

Periodic review SHOULD reconcile the index with actual files under `specifications/adr/` and any other decision-record location approved under section 10.

## 51. AI Use of Decision Records

AI Agents SHOULD read applicable decision records before proposing cross-cutting or difficult-to-reverse changes. They MUST distinguish Accepted decisions from Proposed, Rejected, and Superseded records and MUST follow the current governing source.

AI Agents MUST NOT revive a Rejected or Superseded approach as current authority without new evidence and applicable approval. Plans and change summaries SHOULD cite relevant identifiers when doing so improves traceability. An AI Agent MUST NOT fabricate a decision, approval, alternative, status, or evidence.

## 52. Decision Drift

Decision drift includes:

- implementation contradicting an Accepted decision;
- a Canonical Document diverging from a decision that required its update;
- a decision referencing removed components or obsolete Contracts;
- a Superseded record still being treated as current;
- the repository adopting an alternate pattern without an explicit decision; or
- the index no longer matching actual records.

Material drift MUST trigger review and correction of the implementation, record, index, or governing source according to current authority. Existing behavior alone MUST NOT be treated as approval.

## 53. Decision Review Triggers

A decision SHOULD be reviewed when:

- a material assumption or Requirement changes;
- provider capability, Contract, cost, support, or Risk changes;
- scale or measured performance invalidates a constraint;
- an incident exposes a flaw or missing consequence;
- the security posture or threat model changes;
- Product direction, Architecture, ownership, or operating context changes;
- applicable law, regulation, or contractual obligation materially changes; or
- implementation or decision drift is detected.

A review does not imply reversal. It MUST confirm, amend through permitted factual correction, or supersede the record as appropriate.

## 54. Forbidden Decision Practices

The following are prohibited:

- making a material decision only in undocumented chat;
- hiding a material choice solely in pull-request comments, tickets, or implementation;
- retroactively rewriting Accepted history instead of recording supersession;
- fabricating alternatives, evidence, approval, or decision records;
- claiming Accepted status without the required authority;
- using a Decision Record to bypass a Security Exception or another formal exception process;
- using Vision as approval for future Product scope;
- treating a Payment Redirect or client-reported Payment success as authoritative Payment evidence;
- inventing repository-wide technology standards in a local decision;
- leaving Superseded decisions appearing current;
- using a decision record as a backlog or issue tracker; and
- treating an implemented local pattern as authority merely because it exists.

## 55. Decision-Recording Exceptions

An exception to the requirement to create or update a decision record MUST document:

- the exact recording requirement being waived;
- reason and affected scope;
- accountable owner and authorized approver;
- Risk and consequences;
- approval date and expiry date;
- compensating traceability, such as a linked review or release record; and
- remediation plan and tracking reference.

The exception MUST be explicit, time-bound, auditable, and reviewed before expiry. It MUST NOT waive the underlying Product, Architecture, security, testing, coding, documentation, Contract, or other repository governance. If the underlying Requirement needs an exception, the applicable governing standard's exception process is separately REQUIRED.

## 56. Decision Compliance Matrix

| Concern | Governing Source | Decision Responsibility | Evidence / Review Signal |
| --- | --- | --- | --- |
| Governance | `AGENTS.md` | Follow repository authority and the Decision Hierarchy | Authority and approval review |
| Terminology | `GLOSSARY.md` | Use exact canonical language | Terminology review |
| Vision | `VISION.md` | Align current choices with durable direction without creating scope | Strategic alignment review |
| Product | `PRODUCT.md` | Trace Product scope, semantics, policy, and Requirement decisions | Product approval and source update |
| Architecture | `ARCHITECTURE.md` | Record material technical choices without replacing the baseline | Accepted ADR and Architecture update |
| Security | `SECURITY-STANDARDS.md` | Record security rationale without bypassing controls | Security review or Security Exception |
| Testing | `TESTING-STANDARDS.md` | Record material verification strategy choices | Test evidence or Testing Exception |
| Coding | `CODING-STANDARDS.md` | Record repository-wide implementation choices | Code review or Coding Exception |
| Engineering Principles | `ENGINEERING-PRINCIPLES.md` | Apply durable decision heuristics within constraints | Decision rationale |
| Documentation | `DOCUMENTATION-STANDARDS.md` | Maintain complete, current, traceable records | Documentation review or Documentation Exception |
| APIs | Approved API Contract; `ARCHITECTURE.md` | Record compatibility and consumer impact | Contract and migration review |
| Events | `ARCHITECTURE.md`; approved event Contract | Record ownership and delivery semantics | Schema and consumer review |
| Data | `ARCHITECTURE.md`; owning domain source | Preserve authoritative ownership and integrity | Data, migration, and recovery review |
| Payments | `PRODUCT.md`; `SECURITY-STANDARDS.md` | Preserve authoritative provider evidence and financial correctness | Idempotency, reconciliation, and Audit Records |
| Inventory | `PRODUCT.md`; `ARCHITECTURE.md` | Preserve Inventory ownership and invariants | Concurrency and recovery evidence |
| Identity | `SECURITY-STANDARDS.md`; `ARCHITECTURE.md` | Preserve trusted access and privilege boundaries | Authorization and security review |
| Providers | `ARCHITECTURE.md`; applicable Contract | Record selection, failure, lock-in, and exit impact | Provider and operational review |
| AI | `AGENTS.md`; `SECURITY-STANDARDS.md` | Bound data, permission, authority, and Human Approval Gates | Security evaluation and human review |
| Exceptions | Applicable governing standard | Link but do not replace formal exception governance | Approval, controls, expiry, and remediation |

### Compliance Interpretation

`DECISIONS.md` governs how durable decisions are recorded and indexed. It MUST NOT override the content authority of governing sources. Evidence and an Accepted record demonstrate a reviewed choice; they do not silently amend a Canonical Document.

## 57. Related Documents

- `.ai/core/AGENTS.md`
- `.ai/core/GLOSSARY.md`
- `.ai/core/VISION.md`
- `.ai/core/PRODUCT.md`
- `.ai/core/ARCHITECTURE.md`
- `.ai/core/SECURITY-STANDARDS.md`
- `.ai/core/TESTING-STANDARDS.md`
- `.ai/core/CODING-STANDARDS.md`
- `.ai/core/ENGINEERING-PRINCIPLES.md`
- `.ai/core/DOCUMENTATION-STANDARDS.md`

## 58. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-08-12 | Approved | Promoted the repository-wide decision governance and index after final authority, terminology, ADR, decision-record, Product, Architecture, security, data, Payment, Inventory, AI, operational, traceability, and exception-governance validation. |
| 0.1.0 | 2026-08-12 | Draft | Established the initial repository-wide decision governance and index covering material-decision criteria, ADR structure, approval, supersession, traceability, Product, Architecture, security, data, integration, Payment, Inventory, AI, and operational decisions. |
