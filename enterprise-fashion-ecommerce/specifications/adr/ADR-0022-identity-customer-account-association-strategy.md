# ADR-0022 — Initial Identity–Customer/Account Association Strategy

- **Identifier:** ADR-0022
- **Title:** Initial Identity–Customer/Account Association Strategy
- **Type:** Architecture Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-09-30
- **Owner:** Architecture
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted ADR-0021 establishes Identity-owned local Customer Authentication authority. Identity owns credentials, Authentication evidence, Principal establishment, and Sessions; Customer owns Customer and Account business truth. Authentication evidence, a Principal, Session, login identifier, credential, Customer identifier, or Account identifier does not itself establish the relationship that permits a Principal to act for a Customer or Account.

Accepted DEC-0003 through DEC-0006 govern the initial password credential category, Customer-facing email login identifier, deterministic comparison and uniqueness semantics, and Argon2id verification profile. Accepted ADR-0019 and ADR-0020 govern the authoritative server-side Session and its physical storage. Accepted ADR-0017 and ADR-0018 govern schema-per-persistence-owner and persistence technology. The Approved Identity and Customer Domain and Backend Specifications require stable identities, strict actor separation, project-owned Contracts, Customer isolation, and safe registration, failure, and reconciliation boundaries without selecting the missing cross-Module association architecture.

The unresolved architecture must connect accepted Identity authority to Customer and Account context without collapsing Domain ownership, treating Authentication as business Authorization, introducing direct cross-Module persistence, or assuming one transaction spans separate Module authorities. It must also define when registration may be represented as complete and how incomplete, duplicate, concurrent, failed, or uncertain work remains safe.

This Proposed ADR selects that architectural boundary. It does not establish Customer password policy, email-verification requirements, externally observable registration or login Contracts, physical persistence, Product Decision 5, Product Decision 28, recovery, MFA, or implementation.

## Decision

If Accepted, the initial architecture SHALL use an **Identity-owned authoritative association from Identity to Customer**, combined with a **Customer-owned authoritative relationship from Customer to Account**.

Identity SHALL own the security-side fact that a particular Identity is associated with a particular Customer for local Customer Authentication. Customer SHALL remain authoritative for whether that Customer exists, for the Customer's business state, and for the Customer-to-Account relationship and Account business state. Identity's association reference does not make Identity authoritative for Customer or Account internals. Customer MAY consume current Identity association evidence but MUST NOT reinterpret credential, Authentication, Principal, Session, or Identity security state.

An authenticated Principal MAY obtain Customer or Account context only when current accepted Identity evidence resolves through the authoritative Identity-owned association and the Customer Module accepts the referenced Customer and applicable Account context under current Customer-owned invariants and contextual Authorization. Neither side may infer or manufacture the other side's authoritative state.

## Identity, Principal, Customer, and Account Separation

Identity, Principal, Customer, Account, credential, Session, and login identifier remain distinct:

- Identity is the persistent security actor and owns credential and Authentication truth.
- Principal is current trusted actor context established from accepted Identity evidence.
- Customer is Customer-owned business identity.
- Account is the Customer-owned authenticated profile representing a registered Customer.
- A credential verifies Identity evidence but is not Customer or Account ownership.
- A Session carries current Authentication context but does not create Customer or Account truth.
- A login identifier locates candidate Identity state but is not Authentication or association proof.

Changing a login email, password verifier, Session, or Authentication evidence MUST preserve the same accepted Identity–Customer and Customer–Account relationships unless a separately governed authorized association change occurs. Such a change MUST NOT silently create a replacement Identity, Customer, or Account.

## Association Authority and Cardinality

For the initial local Customer Authentication model:

1. one Identity MAY be associated with zero or one Customer;
2. one Customer MAY be associated with zero or one Identity, and a registered Customer using the initial local Authentication model SHALL have exactly one accepted Identity association;
3. one registered Customer SHALL have exactly one Account, and that Account SHALL represent exactly that Customer; and
4. no Identity, Customer, or Account may participate in a competing accepted association under the initial model.

Zero permits independently created or incomplete state to remain distinguishable without falsely representing completed registration or access. These cardinalities are Architecture constraints for the initial model, not physical key or database design. Supporting multiple Customer identities, shared Accounts, multiple Accounts per Customer, household access, delegated access, merged identities, or another cardinality requires separate Product and Architecture governance with migration and Authorization semantics.

The association itself requires stable identity independent of mutable login email, credential, Principal, Session, Customer profile, or Account content. This ADR selects no identifier format or physical representation.

## Registration Coordination

The Customer Application boundary SHALL coordinate the Customer-facing registration business outcome because Customer owns Customer and Account creation truth. It SHALL use intentional project-owned Identity and Customer Contracts rather than direct persistence access.

The governed coordination sequence is:

1. the coordinator validates that applicable Product, Customer, Identity, Security, privacy, and abuse prerequisites are governed and satisfied;
2. it requests Identity through an Identity-owned Contract to establish or reuse only an explicitly authorized Identity intent, login-identifier ownership, and credential outcome;
3. Identity returns only bounded authoritative evidence and an opaque stable Identity reference needed for coordination, without exposing credentials, persistence state, or framework types;
4. the Customer Module establishes the governed Customer and Account business outcome within Customer authority;
5. Customer supplies bounded authoritative Customer evidence and an opaque stable Customer reference to the Identity-owned association Contract;
6. Identity establishes the authoritative Identity-to-Customer association only after it can validate the current Identity intent and accepted Customer evidence; and
7. registration may be represented as complete only after Identity establishment, Customer and Account establishment, authoritative association confirmation, and all applicable Product and Security conditions are accepted and no material outcome remains uncertain.

This ordering does not decide whether email verification is a completion condition. Product Decision 5 remains authoritative for that later policy. No caller, frontend, supplied identifier, Customer payload, or Account payload may establish or select the association.

## Cross-Module Contract Direction

Customer MAY request Identity-owned credential and association capabilities through a minimum public Identity Contract. Identity MAY consume bounded Customer-owned existence and lifecycle evidence required to validate association or access context through a minimum public Customer Contract. Neither Module may depend on the other's Domain, Adapter, configuration, Entity, Repository, persistence mapping, or internal state.

For authenticated Customer context resolution:

1. Identity resolves current Principal evidence to its authoritative Customer association;
2. the resulting opaque Customer reference and association evidence cross the governed Contract boundary; and
3. Customer independently validates current Customer and Account business state, ownership, and contextual Authorization before exposing or mutating Customer-owned Resources.

Identity disablement, credential invalidation, Session revocation, or association withdrawal MAY remove Authentication or access eligibility but MUST NOT rewrite Customer or Account truth. Customer or Account closure and access limitation MAY deny Customer-owned capability but MUST NOT rewrite Identity, credential, Session history, or security evidence. Lifecycle information crossing either boundary remains evidence owned by its source, not transferred authority.

## Transaction and Atomicity Boundary

Identity credential or login-identifier establishment, Customer and Account creation, and Identity-to-Customer association establishment cross Module ownership and SHALL NOT be treated as one assumed atomic transaction. Each owning Module SHALL control its local transaction and invariants. Shared PostgreSQL deployment does not authorize a coordinator to mutate another Module's schema or make direct cross-Module table access an atomicity mechanism.

The cross-Module registration workflow SHALL therefore preserve explicit intent, idempotency, accepted local outcomes, incomplete state, uncertainty, and reconciliation. No externally observable completion may be emitted while required local outcomes or association confirmation are failed, absent, conflicting, or uncertain. The architecture selects no Saga framework, Outbox implementation, event transport, queue, lock, isolation level, retry count, or timeout.

## Duplicate and Concurrent Registration

DEC-0005 remains authoritative for canonical login-email equality and retained login-identifier ownership. Canonically identical registration attempts MUST NOT create competing login ownership, Identities, Customers, Accounts, credentials, or associations.

Duplicate, concurrent, retried, or reordered coordination MUST converge on at most one accepted Identity, one accepted Customer, one accepted Account, and one accepted association for the governed registration intent. An already-associated Identity, a Customer already associated with another Identity, conflicting Customer evidence, or incompatible repeated intent SHALL produce an internal conflict or incomplete outcome and MUST NOT be silently merged, reassigned, overwritten, or represented as successful registration.

Externally visible duplicate and conflict semantics remain for later Contract governance and must preserve enumeration resistance. This ADR selects no HTTP status, error code, Idempotency Key representation, database constraint, or concurrency mechanism.

## Partial Failure, Uncertainty, and Reconciliation

If Identity succeeds and Customer creation fails, the Identity outcome remains Identity-owned but MUST NOT grant Customer or Account context. If Customer or Account creation succeeds but association establishment fails or is uncertain, Customer truth remains Customer-owned but registration MUST NOT be represented as complete and authenticated Customer context MUST remain denied.

Retries MUST reuse governed intent and confirmed outcomes rather than multiplying effects. An unavailable response, timeout, interrupted coordination, or ambiguous persistence result MUST remain uncertain until authoritative evidence resolves it. Compensation or repair MUST preserve accepted Identity and Customer history and MUST NOT delete, merge, reassign, or recreate authoritative state merely to simplify recovery.

Identity and Customer MUST support reconciliation that can compare:

- registration intent and correlation evidence;
- accepted Identity outcome;
- accepted Customer and Account outcome;
- authoritative association state;
- duplicate, conflict, failure, and uncertainty evidence; and
- current disablement, closure, and access eligibility where applicable.

Discrepancies MUST be visible, auditable where required, denied from Customer context by default, and repairable only through authorized accountable behavior. This ADR selects no reconciliation schedule, operational UI, job, event, queue, retry policy, or support workflow.

## Security, Privacy, and Authority Boundary

The association is security-sensitive and purpose-limited. Access to it SHALL follow least privilege, and protected association existence, credential state, Customer existence, and Account existence MUST remain concealed from unauthorized callers and ordinary telemetry.

Default denial applies whenever:

- current Authentication evidence is missing, invalid, stale, revoked, or uncertain;
- association evidence is absent, conflicting, withdrawn, or uncertain;
- the Customer or Account cannot be accepted under Customer-owned rules; or
- contextual Authorization does not permit the requested action.

An authenticated Principal is not automatically authorized for a Customer or Account. Login identifier presence, a credential match, Session possession, Customer identifier, or Account identifier is not association proof. Association establishment MUST originate from authorized server-side coordination and never from caller-selected identifiers or claimed relationships.

No Module gains authority to read or mutate another Module's internal persistence. No association may bypass Customer isolation, contextual Authorization, Session revocation, credential invalidation, Product policy, privacy obligations, or Audit Record requirements.

## Closure, Disablement, and Lifecycle Boundaries

Identity disablement or withdrawal of Authentication authority SHALL prevent affected Principal and Session use according to Identity and Session governance without deleting or rewriting Customer, Account, Order, Payment, or other business history.

Customer or Account closure or access limitation SHALL prevent Customer-owned access where governed, while preserving Identity, credential, Session, and security history according to their owning authority. It MUST NOT silently delete or reactivate Identity state, and Identity state MUST NOT be treated as evidence that closure, deletion, export, anonymization, or another Product outcome completed.

Product Decision 28 remains unresolved. This ADR establishes no closure state machine, deletion, anonymization, retention, reuse, reactivation, or Customer-data workflow.

## Migration, Compatibility, and Reversibility

No production Identity–Customer association implementation or population currently requires migration. Initial implementation must preserve provenance for Identity, Customer, Account, registration intent, and association outcomes so future compatibility and reconciliation are possible without conflating authority.

The selected architecture supports future Module extraction by requiring project-owned Contracts, separate owner transactions, opaque references, and no cross-Module persistence access. It does not authorize extraction, external services, distributed transactions, events, or infrastructure.

A future cardinality, association-owner, external Identity Provider, merged-account, delegated-access, or service-boundary change requires superseding Architecture governance, compatibility analysis, migration evidence, dual-state prevention, rollback or reconciliation, and preservation of current revocation and Customer isolation. Exit cost grows after persisted association state and Contracts depend on this direction.

Rollback or migration MUST NOT create duplicate identities, competing Customer ownership, stale access, restored withdrawn access, lost Customer history, or ambiguous association truth. Unresolved migration state fails closed for Customer context.

## Alternatives Considered

### A. Identity-Owned Association Reference to Customer Authority — Selected

Identity owns the association used to resolve authenticated Identity and Principal evidence to an opaque Customer reference. Customer independently owns Customer and Account truth and authorizes Customer Resources.

This aligns association lookup with Identity-owned Authentication, preserves Customer business authority, prevents Customer credentials from moving into Customer storage, and makes association absence or withdrawal part of the security boundary. It requires cross-Module coordination and reconciliation because Customer creation and Identity association are separate authoritative outcomes.

### B. Customer-Owned Association Reference to Identity Authority — Not Selected

Customer could retain the authoritative Identity reference and resolve Customer context from Customer-owned state. This would align association with Customer creation and Account business behavior and could simplify Customer-side registration persistence.

It is not selected because Identity must resolve accepted Authentication evidence to Customer context without allowing Customer state or supplied Customer identifiers to become Authentication authority. Making Customer the sole association authority would either require Identity to depend on Customer for every resolution or create a duplicated security-side mapping. Customer MAY retain bounded correlation evidence, but it does not own the authoritative Identity-to-Customer security association.

### C. Dedicated Cross-Module Association or Coordinator Model — Deferred

A separate association owner or coordinator could centralize registration state, association lifecycle, idempotency, and reconciliation. This could become useful if multiple Identity authorities, shared Accounts, delegated access, or additional business actors require a distinct model.

It is deferred because no separate governed Domain or Module owns that truth, and introducing one now would add authority, persistence, Contracts, lifecycle, and operational complexity before the initial one-to-one Customer model exists. Registration coordination remains Customer Application behavior using Identity and Customer Contracts, not a new authoritative Domain.

### D. Shared or Collapsed Identity–Customer Persistence Model — Rejected

Collapsing Identity, Customer, Account, credential, and association state into shared tables or one aggregate could provide local transactional convenience.

It is rejected because it conflicts with established Domain ownership, ADR-0017 schema-per-persistence-owner, project-owned Ports and Adapters, stable Identity and Customer distinctions, and the prohibition on direct cross-Module persistence access. Database convenience does not authorize authority collapse or shared internal models.

## Consequences

### Benefits and Enabled Outcomes

- Establishes one authoritative security-side association without moving Customer or Account business truth into Identity.
- Enables registration and login Contract governance to proceed after remaining Product and password-policy prerequisites close.
- Preserves stable Identity, Customer, and Account across mutable email, credentials, Sessions, and profile data.
- Provides deterministic default-denial and duplicate/concurrency invariants.
- Establishes explicit transaction, failure, uncertainty, and reconciliation boundaries.
- Preserves future extraction through project-owned Contracts and separate persistence ownership.

### Costs and Complexity

- Registration spans separate authoritative transactions and cannot rely on one cross-Module database transaction.
- Incomplete and uncertain states require durable intent, correlation, reconciliation, support evidence, and careful retry behavior.
- Both Modules need bounded public Contracts and compatibility discipline.
- Operational diagnosis must distinguish Identity, Customer, Account, association, and coordination outcomes without exposing protected existence.

### Security and Privacy Impact

Association becomes Sensitive Data and an Authorization-relevant security boundary requiring least privilege, purpose limitation, concealment, audit where applicable, and fail-closed resolution. The decision reduces authority confusion but introduces risks from stale associations, duplicate links, caller-controlled identifiers, and inconsistent disablement or closure evidence.

### Data, Concurrency, and Integrity Impact

Each owner retains its Source of Truth and local transaction. Concurrency must converge on the initial cardinality without silent merge, reassignment, or overwrite. Cross-Module uncertainty remains explicit until reconciled. Physical enforcement is deferred to implementation under ADR-0017, ADR-0018, Database, and PostgreSQL governance.

### Operational and Support Impact

Operations need evidence sufficient to find incomplete registration, association conflict, stale access, and reconciliation backlog without exposing credentials or Account existence. Repair requires accountable Authorization and must preserve history. No support workflow, escalation process, alert threshold, or SLO is selected.

### Compatibility, Migration, Extraction, and Reversibility Impact

There is no production population to migrate. Future model changes must preserve provenance, current access withdrawal, Customer isolation, and association integrity. Separate Contracts improve extraction readiness, but extraction is not approved and would add distributed consistency and operational cost. Reversing association ownership or cardinality after persistence exists requires superseding governance and controlled migration.

## Explicit Non-Decisions

ADR-0022 does not select, define, create, or authorize:

- physical database schema, table, column, primary key, foreign key, index, constraint, SQL, or concrete identifier format;
- Spring Data repository, persistence mapping, Aggregate structure, or Flyway migration contents;
- public API route, HTTP method, status, DTO, error code, or OpenAPI schema;
- concrete Integration Event, Domain Event, payload, topic, broker, queue, or delivery mechanism;
- retry count, timeout, lock, isolation level, idempotency-key format, SLO, SLA, or capacity target;
- Customer password policy or compromised-password behavior;
- Product Decision 5 or Customer email-verification workflow;
- Product Decision 28 or Customer export, correction, deletion, closure, retention, reuse, or reactivation policy;
- account-recovery mechanism, channel, factor, token, provider, expiry, or support workflow;
- ordinary Customer MFA policy, factor, provider, protocol, fallback, or recovery;
- Session storage, cookie configuration, Session lifetime, or token strategy;
- password hashing algorithm or parameters already governed by DEC-0006;
- Identity Provider, federation, SSO, provider, SDK, or protocol selection;
- dependency admission, infrastructure, hosting, deployment, production implementation, or completed executable validation;
- frontend workflow, navigation, form, state, message, or browser storage behavior; or
- shared Account, household access, delegated access, merged Identity, multiple Customer identity, or service-extraction capability.

ADR-0022 does not reopen or alter DEC-0003, DEC-0004, DEC-0005, DEC-0006, ADR-0017, ADR-0018, ADR-0019, ADR-0020, or ADR-0021.

## Required Governance Reviews

Before ADR-0022 may become Accepted, durable review evidence must represent:

- Architecture approval of association ownership, cardinality, Contract direction, transaction boundaries, compatibility, migration, and reversibility;
- Identity approval of Identity authority, Principal resolution, association evidence, credential and Session non-transfer, disablement, and reconciliation boundaries;
- Customer approval of Customer and Account authority, registration coordination, contextual Authorization, closure non-authority, and Customer isolation;
- Security approval of default denial, least privilege, association proof, Sensitive Data, enumeration resistance, failure, conflict, audit, and withdrawn-access semantics; and
- Engineering review of modular-monolith dependency direction, transaction feasibility, idempotency, concurrency, failure, reconciliation, testing, maintenance, and implementation neutrality.

The acceptance PR may provide durable review evidence only when those authorities are genuinely represented. No named reviewer, meeting, ticket, signature, external organizational approval, implementation evidence, or completed executable test is claimed by this Proposed record.

## Acceptance Conditions and Synchronization

Based on the current proposal review, acceptance is expected to synchronize:

1. `specifications/adr/ADR-0022-identity-customer-account-association-strategy.md`;
2. `.ai/core/DECISIONS.md`; and
3. `.ai/core/ARCHITECTURE.md`.

Acceptance-readiness review MUST verify the final affected-source set. Another governed source may be added only when that review identifies a direct contradiction or synchronization requirement supported by existing repository authority; it must not expand ADR-0022's substantive scope. `PRODUCT.md`, `SECURITY-STANDARDS.md`, backend standards, Domain Specifications, Backend Specifications, Contracts, build files, dependencies, and implementation files are not currently expected to change.

No Customer registration or end-to-end Customer-context implementation may treat ADR-0022 as Accepted authority until the ADR is Accepted and required canonical synchronization is complete.

## Validation Criteria

Before ADR-0022 may become Accepted, review must verify:

1. metadata remains `0.1.0 Proposed`, Type `Architecture Decision`, owner `Architecture`, date `2026-09-30`, and `authoritative: false`;
2. Identity, Principal, Customer, Account, credential, Session, and login identifier remain distinct;
3. Identity owns the authoritative Identity-to-Customer association while Customer retains Customer and Account business truth and the Customer-to-Account relationship;
4. initial cardinality permits at most one Customer per Identity and at most one Identity per Customer, while a registered Customer has exactly one Identity association and exactly one Account representing that Customer;
5. changing login email, credential, Session, Authentication evidence, or profile data cannot silently replace Identity, Customer, Account, or association;
6. Customer Application coordinates the registration outcome through project-owned Identity and Customer Contracts without gaining Identity authority;
7. registration is complete only after accepted Identity, Customer, Account, association, and applicable Product and Security outcomes are confirmed;
8. Identity resolves Principal evidence to an opaque Customer association and Customer independently enforces current Customer/Account truth and contextual Authorization;
9. Identity, Customer, Account, and association establishment use separate owner transactions without assumed distributed atomicity or cross-Module persistence access;
10. canonically identical, duplicate, concurrent, retried, and reordered registration cannot create competing Identity, Customer, Account, credential, login ownership, or association state;
11. partial failure and uncertain outcomes cannot grant Customer context or be represented as completed registration;
12. reconciliation preserves provenance, accepted history, default denial, accountable repair, and visibility of incomplete, conflicting, or uncertain state;
13. Identity disablement affects Authentication and access authority without rewriting Customer truth, while Customer or Account closure affects Customer-owned access without rewriting Identity or credential history;
14. an authenticated Principal, login identifier, credential match, Session, Customer identifier, or Account identifier is not association proof, and association establishment is never caller-controlled;
15. migration, rollback, cardinality change, association-owner change, and future extraction preserve association integrity, Customer isolation, provenance, and withdrawn access without pre-authorizing extraction;
16. DEC-0003, DEC-0004, DEC-0005, and DEC-0006 remain unchanged and authoritative within their scopes;
17. ADR-0017, ADR-0018, ADR-0019, ADR-0020, and ADR-0021 remain unchanged and authoritative within their scopes;
18. Product Decisions 5 and 28 remain unresolved and no email-verification, deletion, closure, reuse, or reactivation policy is introduced;
19. physical persistence, public API, event, provider, dependency, infrastructure, frontend, numerical, recovery, MFA, password-policy, and implementation details remain explicit non-decisions;
20. required Architecture, Identity, Customer, Security, and Engineering review evidence is durable without fabricated authority or implementation claims; and
21. proposal registration changes only ADR-0022 and `DECISIONS.md`, registers ADR-0022 exactly once as Proposed, passes whitespace and diff validation, and introduces no unrelated tracked changes.

## Related Documents

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [JAVA.md](../../.ai/backend/JAVA.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [API.md](../../.ai/backend/API.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [ADR-0017 — PostgreSQL Schema Strategy](ADR-0017-postgresql-schema-strategy.md)
- [ADR-0018 — Persistence Technology](ADR-0018-persistence-technology.md)
- [ADR-0019 — Authentication Session and Token Strategy](ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](ADR-0021-customer-authentication-authority-strategy.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](../decisions/DEC-0003-initial-local-customer-credential-mechanism.md)
- [DEC-0004 — Customer Login Identifier Semantics](../decisions/DEC-0004-customer-login-identifier-semantics.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0006 — Customer Password Hashing and Verification Strategy](../decisions/DEC-0006-customer-password-hashing-verification-strategy.md)

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-30 | Proposed | Proposed an Identity-owned authoritative Identity-to-Customer association with Customer-owned Customer-to-Account truth, initial one-to-one registered-Customer cardinality, Customer-coordinated registration, separate owner transactions, default denial, reconciliation, and migration boundaries while preserving Product Decisions 5 and 28 and all physical persistence, Contract, policy, provider, dependency, and implementation non-decisions. |
