# Initial Customer Registration Contract

## 1. Purpose

This governed human-readable Contract defines the workflow, ownership, completion, failure, uncertainty, and reconciliation semantics for initial Customer registration.

It is the workflow companion paired with `initial-customer-registration.openapi.yaml`, which is the authoritative executable HTTP wire Contract under `API.md` section 64.2. This document is authoritative only for the human-readable workflow, ownership, lifecycle, orchestration, cross-Module, uncertainty, rationale, traceability, and non-decision semantics it defines and MUST NOT be interpreted as defining or redefining an HTTP route, method, schema, header, status mapping, field name, or RFC 9457 wire representation.

The paired Markdown and OpenAPI artifacts MUST remain synchronized. Conflicting semantics constitute a Contract defect that MUST be corrected; this Contract does not claim that registration implementation exists.

## 2. Scope

This Contract governs the initial local Customer registration capability coordinated by Customer Application. It covers:

- creation and processing of a registration intent;
- bounded coordination between Customer and Identity;
- application of the governed login-email, credential, password, verification, Customer, Account, and association gates;
- completion, pending, conflict, failure, uncertainty, retry, idempotency, concurrency, and reconciliation semantics; and
- safe externally observable workflow classifications without redefining their executable wire representation in the paired OpenAPI Contract.

It does not govern Customer login, recovery, ordinary Customer MFA, external Identity Providers, frontend behavior, persistence design, provider selection, or infrastructure.

## 3. Authority and Governing Sources

This Contract is subordinate to the repository decision hierarchy and the following governing sources:

- [AGENTS.md](../../../.ai/core/AGENTS.md), [PRODUCT.md](../../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../../.ai/core/ARCHITECTURE.md), [SECURITY-STANDARDS.md](../../../.ai/core/SECURITY-STANDARDS.md), and [GLOSSARY.md](../../../.ai/core/GLOSSARY.md);
- [API.md](../../../.ai/backend/API.md), including the governed Contract artifact and OpenAPI authority convention;
- the Approved [Customer Domain Specification](../../domains/customer/customer-domain.md) and [Identity Domain Specification](../../domains/identity/identity-domain.md);
- the Approved [Customer Backend Specification](../../backend/customer/customer-backend.md) and [Identity Backend Specification](../../backend/identity/identity-backend.md);
- Accepted [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md), [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md), and [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md); and
- Accepted [DEC-0003](../../decisions/DEC-0003-initial-local-customer-credential-mechanism.md), [DEC-0004](../../decisions/DEC-0004-customer-login-identifier-semantics.md), [DEC-0005](../../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), [DEC-0006](../../decisions/DEC-0006-customer-password-hashing-verification-strategy.md), [DEC-0007](../../decisions/DEC-0007-initial-customer-password-policy.md), and [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md).

If this Contract conflicts with a higher-authority source, the higher-authority source prevails and the Contract defect MUST be corrected. The terms MUST, MUST NOT, REQUIRED, SHALL, SHALL NOT, SHOULD, SHOULD NOT, and MAY have their repository-governed normative meanings.

## 4. Terminology and Ownership Boundaries

Canonical repository terminology applies. In particular, Identity, Principal, Customer, Account, credential, Authentication, Authorization, Session, and login email are distinct concepts and MUST NOT be collapsed.

Placement under the Customer owner package identifies the owner of the externally consumed registration orchestration. It does not transfer authority:

- Customer owns registration coordination and authoritative Customer and Account business truth, including Customer-to-Account relationships.
- Identity owns the Customer login identifier, local credential establishment and verification, Identity-applicable password-policy and security evidence, current-login-email verification state and evidence, Authentication, Principal establishment, Sessions, and the authoritative Identity-to-Customer association.
- Each owner exposes only bounded project-owned Contracts and retains its internal model, persistence, and transaction authority.
- Neither owner may read or write the other owner's internal persistence or reinterpret the other owner's evidence as its own truth.

## 5. Registration Actor and Trust Boundary

The initial actor is an unauthenticated Visitor expressing an intent to register as a Customer. All actor-supplied values and claims are untrusted input.

Possession or submission of a login email, password, identifier, verification material, Customer reference, Account reference, client state, or prior response is not proof of Identity, mailbox control, Authentication, Authorization, Customer ownership, Account ownership, association, recovery authority, or verified-email state. Trust may be established only by the owning server-side authority under the applicable governed evidence rules.

## 6. Registration Intent Semantics

A registration intent represents a request to begin or continue the governed initial Customer registration workflow. It is not a completed registration, Identity, Customer, Account, credential, association, Authentication result, Principal, Session, or Authorization grant.

An intent MUST have a stable, owner-recognizable identity sufficient to make duplicate-sensitive coordination safe. This Contract does not select its public representation, internal identifier format, retention period, or persistence design.

Repeated, concurrent, or resumed processing MUST preserve the same governed intent and confirmed owner-local outcomes when they refer to the same attempt. An intent MUST NOT be used to merge, transfer, reassign, replace, or arbitrarily select ownership.

## 7. Registration Input Semantics at the Workflow Level

The workflow requires only categories of input justified by governed registration semantics:

- the single initial Customer-facing login identifier, email;
- a prospective password for the initial local Customer credential;
- Customer information required by the separately governed Customer registration outcome; and
- evidence or references needed to continue the governed verification and coordination workflow.

This list does not define exact request fields, required-versus-optional wire properties, JSON names, formats, DTOs, or schemas.

Email is untrusted lookup and registration input. It MUST NOT be treated as proof of any protected fact. Password material MUST remain confined to the Identity-owned credential-establishment boundary and MUST NOT become Customer or Account state, workflow telemetry, or general coordination data.

## 8. Customer-Coordinated Registration Workflow

Customer Application coordinates the workflow through bounded Customer-owned and Identity-owned Contracts. The coordination MUST:

1. recognize or establish the governed registration intent without representing it as complete;
2. request only the Identity-owned outcomes required for login-identifier ownership, credential establishment, current-login-email verification, and the authoritative Identity-to-Customer association;
3. establish or confirm the required Customer-owned Customer and Account outcomes within Customer authority;
4. evaluate confirmed owner evidence rather than infer success from dispatch, request acceptance, timeout, client state, or another non-authoritative signal;
5. keep partial, failed, or uncertain work incomplete and eligible for safe retry or reconciliation where governance permits; and
6. represent completion only when every completion criterion in this Contract is satisfied.

Coordination MUST NOT collapse owner-local work into a distributed transaction or allow one Module to mutate the other's state.

## 9. Identity-Owned Responsibilities

Identity is responsible for:

- applying DEC-0004 and DEC-0005 to the proposed login email;
- preserving the single retained owner of a canonical login-email comparison value;
- applying DEC-0007 before accepting a prospective password;
- establishing the password-based local credential under DEC-0003 and the verifier semantics of DEC-0006;
- owning current-login-email verification state and accepting evidence only under DEC-0008;
- establishing and owning the authoritative Identity-to-Customer association under ADR-0022;
- preserving separation among Identity, credential, verification, association, Authentication, Principal, and Session state; and
- returning bounded evidence or outcomes without exposing internal persistence, credential material, verification secrets, or protected existence.

Identity MUST NOT create or reinterpret Customer or Account business truth.

## 10. Customer-Owned Responsibilities

Customer is responsible for:

- coordinating the externally consumed registration capability;
- establishing and retaining authoritative Customer business truth;
- establishing and retaining required Account business truth and Customer-to-Account relationships;
- applying Customer-owned lifecycle and integrity rules without importing Identity internals;
- tracking the workflow's Customer-owned pending, confirmed, failed, or uncertain coordination state; and
- representing completion only from confirmed evidence for all required owner outcomes.

Customer MUST NOT establish login-identifier ownership, accept or verify credentials, assert current-login-email verification, authenticate the actor, establish a Principal or Session, or create the authoritative Identity-to-Customer association.

## 11. Identity-to-Customer Association Responsibility

Identity owns the authoritative Identity-to-Customer association. Customer may consume current association evidence through a bounded Contract but MUST NOT become authoritative for the security-side association or infer it from identifier equality, credential acceptance, a Customer reference, an Account reference, or client claims.

Registration MUST NOT be complete until the association required for the governed context exists and is unambiguous. Incomplete, conflicting, ambiguous, unavailable, stale, or uncertain association evidence MUST fail closed.

This contextual requirement establishes no global Identity-to-Customer, Customer-to-Identity, or Customer-to-Account cardinality and no shared or delegated Account semantics.

## 12. Password-Policy Gate

Every prospective registration password MUST pass DEC-0007 within the Identity-owned credential-establishment boundary before a credential is accepted. The complete password MUST be handled under DEC-0007 normalization, Unicode code-point length, no-truncation, blocklist, character, whitespace, case, password-manager, autofill, paste, and failure-closed semantics.

This Contract neither restates nor modifies the policy algorithm or values. It MUST NOT allow client validation, Customer coordination, framework defaults, provider behavior, or implementation convenience to weaken the governed policy.

Password acceptance alone MUST NOT establish completed registration, Authentication, a Principal, a Session, Customer or Account ownership, the Identity-to-Customer association, or current-login-email verification.

## 13. Current-Login-Email Verification Gate

The current authoritative Customer login email MUST have accepted Identity-owned verification evidence under DEC-0008 before registration may be represented as complete.

Verification proves control of the current authoritative login email only. It does not independently prove real-world Identity, credential validity, Customer or Account ownership, association, Authentication, Authorization, entitlement, recovery authority, or unrelated Domain truth.

Dispatch, delivery, opening, UI state, knowledge or submission of the identifier, a verification request, or an incomplete challenge MUST NOT establish verified-email state. Stale, replayed, conflicting, ambiguous, superseded, unavailable, failed, or uncertain verification evidence MUST NOT satisfy the gate or overwrite newer authoritative state.

If the authoritative login email changes, evidence for the previous value does not verify the replacement. Completion or later eligibility dependent on current-email verification MUST fail closed until governed re-verification succeeds.

## 14. Registration Completion Criteria

Registration is complete only when authoritative evidence confirms all outcomes required for the same governed registration intent and current context:

1. Identity has accepted ownership of the canonical login-email comparison value without collision, conflict, ambiguity, or uncertainty.
2. Identity has accepted the local Customer credential after the governed password-policy and verifier requirements succeed.
3. Identity has accepted verification evidence for the current authoritative login email.
4. Customer has established the required authoritative Customer outcome.
5. Customer has established the required authoritative Account outcome and Customer-to-Account relationship for the initial flow.
6. Identity has established the required authoritative Identity-to-Customer association.
7. No required outcome is failed, conflicting, ambiguous, stale, superseded, unavailable, partial, or uncertain.

Completion means only that these registration outcomes are confirmed. It does not itself authenticate the actor, establish a Principal or Session, or authorize Account or protected Customer access.

## 15. Pending and Incomplete Registration Semantics

A bounded pending or incomplete registration MAY exist while required outcomes are not yet authoritatively established. Pending and incomplete states MUST remain distinguishable from completion and from terminal rejection or conflict where their safe handling differs.

Pending or incomplete registration MUST NOT grant or be represented as:

- completed registration;
- successful Authentication;
- an authenticated Customer Principal;
- a Customer Session;
- Account access;
- authoritative Identity-to-Customer association;
- protected Customer access; or
- authority to bypass any remaining gate.

The workflow MAY later resume, retry, re-verify, or reconcile under governed behavior, but eligibility to continue does not guarantee delivery, acceptance, or success.

## 16. Duplicate and Canonical Login-Email Collision Semantics

DEC-0005 exclusively governs canonical login-email comparison, uniqueness, retained ownership, collision, and lifecycle semantics. This Contract consumes those semantics and does not redefine the comparison algorithm.

Duplicate, canonically colliding, conflicting, ambiguous, or uncertain login-email ownership MUST NOT:

- create competing Identity ownership;
- create an unintended duplicate Identity, Customer, Account, credential, or association;
- silently merge, select, transfer, replace, reassign, or overwrite ownership;
- disclose whether protected Identity, Customer, Account, credential, association, or verification state exists; or
- be represented as successful or complete registration.

Authoritative retained ownership continues to control the canonical comparison value. Unrelated post-deletion reuse policy remains unresolved under Product Decision 28.

## 17. Retry and Idempotency Semantics

Registration is duplicate-sensitive. Retries MUST reuse the governed registration intent and confirmed authoritative outcomes rather than multiply effects.

For the same governed intent, repeated processing MUST be safe and MUST NOT create duplicate or competing Identities, Customers, Accounts, credentials, verification outcomes, or associations. A retry MUST re-evaluate current authority where freshness or lifecycle state matters and MUST NOT restore withdrawn, superseded, invalid, or failed state.

Incompatible reuse of intent, conflicting supplied meaning, or a collision with different authoritative ownership MUST produce a safe conflict or incomplete outcome rather than arbitrary selection. This Contract does not define an Idempotency Key header, key format, storage mechanism, retention duration, or exact response mapping; applicable wire behavior is governed by the paired OpenAPI Contract, while implementation concerns remain constrained by these semantics.

## 18. Concurrency Semantics

Concurrent, reordered, duplicated, or overlapping registration processing MUST converge on current authoritative owner state and preserve the same safety properties as sequential processing.

Concurrency MUST NOT:

- establish competing ownership of a canonical login email;
- accept multiple credentials for one initial credential intent;
- create an unintended duplicate association for the same governed association intent;
- overwrite newer verification, lifecycle, or ownership truth with stale evidence;
- convert a partial, conflicting, or uncertain outcome into success; or
- select a winner merely by timing, ordering, or implementation convenience.

Where authoritative evidence cannot establish one valid outcome, the workflow remains incomplete or conflicting and requires governed reconciliation.

## 19. Partial-Failure and Uncertain-Outcome Semantics

Every owner-local accepted outcome remains owned by its source when another step fails or becomes uncertain. A downstream or coordinating failure MUST NOT retroactively fabricate, erase, transfer, or reinterpret an accepted upstream fact.

If Identity work succeeds while required Customer or Account work fails, the Identity outcome remains Identity-owned but MUST NOT grant Customer or Account context. If Customer or Account work succeeds while credential, verification, or association work fails or is uncertain, Customer truth remains Customer-owned but registration remains incomplete and protected Customer context remains denied.

Timeouts, unavailable responses, interrupted coordination, ambiguous persistence results, or unknown dependency outcomes MUST remain uncertain until resolved from authoritative evidence. Uncertainty MUST NOT be represented as rejection when rejection is unknown, or as success when success is unconfirmed.

## 20. Reconciliation Requirements

The participating owners MUST support bounded reconciliation sufficient to compare the registration intent with current authoritative Identity, login-email ownership, credential, verification, Customer, Account, Customer-to-Account relationship, and Identity-to-Customer association evidence.

Reconciliation MUST:

- preserve ownership, provenance, accepted history, and newer authoritative truth;
- default-deny completion and protected context while required evidence is missing, conflicting, ambiguous, or uncertain;
- distinguish safe retry from conflict and accountable repair;
- prevent duplicate effects and restoration of withdrawn or superseded state;
- make material discrepancies visible to authorized operations without exposing protected existence or Sensitive Data; and
- permit repair only through authorized owner behavior.

This Contract selects no reconciliation schedule, job, queue, event, support UI, retry count, or repair implementation.

## 21. Enumeration Resistance and Disclosure Requirements

All externally observable registration outcomes MUST resist enumeration of login emails, Identities, Customers, Accounts, credentials, associations, verification state, and retained ownership.

Safe disclosure MUST NOT reveal whether failure or delay arose from identifier absence or ownership, a canonical collision, credential state, Customer or Account existence, association state, verification state, blocklist handling, internal persistence, or reconciliation status beyond what the authorized workflow may safely disclose.

Response timing, shape, wording, status selection, retry behavior, telemetry, and support evidence MUST NOT become protected-existence oracles. The paired OpenAPI Contract translates the workflow categories into authoritative executable wire behavior and MUST remain synchronized with these requirements without weakening them.

## 22. Authentication, Principal, Session, and Authorization Boundaries

Registration completion MUST NOT itself imply successful Authentication or create an authenticated Principal or Session. A separately governed Authentication operation must validate current Identity-owned evidence and all applicable gates before ADR-0019 and ADR-0020 Session behavior may apply.

A registration intent, accepted password, verified email, Customer, Account, association, completed registration, identifier, or client state alone MUST NOT establish Authentication or a Principal. Authentication or Session possession alone MUST NOT establish contextual Authorization.

No Session identifier, cookie, storage, renewal, logout, revocation, or physical Session behavior is defined by this Contract.

## 23. Account-Access Boundary

Registration completion MUST NOT itself establish contextual Account Authorization. Access to an Account or protected Customer capability requires, at the time of access:

- successful governed Authentication;
- an applicable valid Session;
- authoritative, unambiguous Identity-to-Customer association evidence;
- current Customer and Account business eligibility; and
- owning-Domain contextual Authorization for the Principal, action, Resource, association, and state.

Verified email is necessary for the initial governed flow but is not sufficient for Account access. This Contract establishes no global Account cardinality, shared or delegated Account semantics, Role or Permission matrix, or Account lifecycle policy.

## 24. Failure-Closed Requirements

The workflow MUST fail closed whenever a required security, Product, ownership, lifecycle, or completion fact is invalid, rejected, unavailable, stale, superseded, conflicting, ambiguous, incomplete, or uncertain.

Failure closed means that registration is not represented as complete and no Authentication, Principal, Session, Account access, association authority, or protected Customer access is granted. It does not require destructive rollback of accepted owner-local truth and MUST NOT conceal a reconciliation need.

Client assertions, frontend state, message delivery, provider claims, logs, analytics, projections, or coordinator assumptions MUST NOT substitute for current authoritative evidence.

## 25. Cross-Module Transaction Boundaries

Identity and Customer perform owner-local work in separate owner-local transactions. Cross-Module registration MUST NOT assume, simulate, or require a distributed atomic transaction.

Customer coordination MUST use bounded project-owned Contracts. Neither Module may access the other's tables, repositories, entities, persistence mappings, or transaction internals. Transaction completion by one owner proves only that owner's outcome and does not prove completion by another owner.

Cross-Module consistency is achieved through explicit intent, duplicate safety, confirmed evidence, incomplete-state representation, retry, and reconciliation—not shared persistence or transferred authority.

## 26. Security and Privacy Requirements

The workflow MUST apply least privilege, data minimization, purpose limitation, secure transport, trusted server-side validation, and default denial. Passwords, verifier material, verification secrets, Session credentials, security tokens, and other Secrets MUST NOT cross into Customer state or appear in URLs, logs, metrics, traces, analytics, events, screenshots, fixtures, support evidence, or ordinary errors.

Login email and registration data are PII and MUST be exposed only as necessary for the authorized workflow. Internal identifiers, persistence details, constraint names, stack traces, and sensitive reason detail MUST remain concealed.

Registration controls MUST preserve abuse resistance, replay resistance, injection safety, credential-stuffing protections, CSRF requirements where applicable to the paired browser Contract, and current security authority without this document selecting mechanisms or numerical thresholds.

## 27. Audit and Observability Requirements

Registration coordination MUST propagate governed correlation context and provide bounded, privacy-safe Logs, Metrics, Traces, and Audit Records where required to distinguish material accepted, pending, rejected, conflicting, failed, uncertain, retried, and reconciled outcomes.

Evidence MUST retain source, time, subject or governed intent, action, outcome category, correlation, provenance, and applicable uncertainty without exposing passwords, verification secrets, verifier material, raw security evidence, or unnecessary PII.

Audit Records remain distinct from ordinary Logs and Domain or Integration Events. Telemetry and support views are non-authoritative and MUST NOT create registration, verification, association, Authentication, or Customer truth. This Contract selects no event, metric, log schema, retention period, alert threshold, or observability product.

## 28. Traceability to Governing Authority

| Contract concern | Governing source | Applied Contract boundary |
| --- | --- | --- |
| Customer registration and Account capability | `PRODUCT.md`; Customer Domain and Backend Specifications | Customer owns external coordination and Customer/Account business truth. |
| Modular ownership and cross-Module consistency | `ARCHITECTURE.md`; ADR-0022 | Separate owner-local transactions, bounded Contracts, no cross-owner persistence access, incomplete and reconcilable partial outcomes. |
| Authentication, Principal, and Session separation | ADR-0019; ADR-0020; ADR-0021 | Registration does not authenticate, establish a Principal, or create a Session. |
| Identity-to-Customer association | ADR-0022 | Identity owns the authoritative association; required context must be unambiguous and fail closed. |
| Initial local credential | DEC-0003 | One initial password-based local Customer credential category with Identity authority. |
| Login identifier | DEC-0004 | Email is the single initial Customer-facing login identifier and remains untrusted input. |
| Email comparison, uniqueness, collision, and lifecycle | DEC-0005 | Consume canonical comparison and retained-ownership semantics without redefining the algorithm. |
| Password verifier | DEC-0006 | Identity-owned governed verifier behavior; no verifier or password material crosses the Customer boundary. |
| Password policy | DEC-0007 | Credential establishment passes the complete governed policy and fails closed. |
| Current-login-email verification | DEC-0008 | Verification is required before registration completion and proves only control of the current authoritative login email. |
| API and OpenAPI authority | `API.md` | Markdown governs human-readable workflow and ownership semantics; the paired OpenAPI 3.1 artifact governs authoritative executable HTTP wire behavior. |
| Security and privacy | `SECURITY-STANDARDS.md`; Approved backend specifications | Least privilege, safe disclosure, enumeration resistance, Sensitive Data protection, audit, and default denial. |
| Product Decision 28 | `PRODUCT.md` | Post-deletion login-email reuse and the broader governed Customer data workflow remain unresolved. |

## 29. Explicit Non-Decisions

This Contract does not decide, authorize, create, or claim completion of:

- public route, HTTP method, headers, request or response JSON schemas, field names, exact HTTP status mapping, or RFC 9457 wire schemas beyond the authoritative paired OpenAPI Contract;
- a physical database schema, table, column, index, constraint, sequence, or seed data;
- ORM, entity, aggregate-persistence mapping, repository class, or Java/Spring implementation design;
- concrete internal Java interfaces, method signatures, DTOs, or framework types;
- verification token or code format, lifetime, storage, delivery, or cryptographic mechanism;
- an email provider, delivery mechanism, queue, event technology, topic, payload, or callback;
- concrete retry counts, timing, rate limits, timeouts, lockout thresholds, SLOs, SLAs, TTLs, or retention periods;
- persistence implementation beyond already governed authority;
- a recovery mechanism or recovery evidence policy;
- an ordinary Customer MFA mechanism, factor, protocol, or provider;
- an external Identity Provider, social login, federation, or account-linking design;
- frontend, UI, accessibility implementation, or client-state design;
- infrastructure, deployment, hosting, network, secret-management, or provider topology;
- global Identity, Customer, or Account cardinality or shared/delegated Account semantics;
- Product Decision 28 or unrelated post-deletion login-email reuse policy;
- unrelated lifecycle authority belonging to another Domain; or
- implementation, deployment, or operational readiness.

## 30. Acceptance and Validation Criteria

The governed Contract change is valid only when review confirms all of the following:

1. The artifact is located at `specifications/contracts/customer/initial-customer-registration-contract.md`, identifies Customer as orchestration owner, and transfers no Identity, Customer, or Account authority.
2. The Contract remains subordinate to the cited Approved and Accepted sources and uses canonical repository terminology.
3. Email remains the single initial Customer-facing login identifier and is treated only as untrusted input.
4. DEC-0005 comparison, uniqueness, retained-ownership, collision, and lifecycle semantics are consumed without redefining their algorithm.
5. Duplicate, conflicting, ambiguous, colliding, or uncertain login-email ownership cannot create competing ownership or silently merge, select, transfer, replace, or reassign state.
6. DEC-0007 password policy and DEC-0006 verifier semantics are applied within Identity authority and cannot be weakened by coordination or client behavior.
7. Password acceptance alone grants none of registration completion, Authentication, Principal, Session, Customer/Account ownership, association, or verification.
8. DEC-0008 current-login-email verification is required for completion, and the Contract neither overstates what verification proves nor accepts non-authoritative evidence.
9. Completion requires confirmed current authoritative Identity, credential, verification, Customer, Account, Customer-to-Account, and Identity-to-Customer association outcomes applicable to the same governed intent.
10. Pending or incomplete registration grants no completed-registration, Authentication, Principal, Session, Account, association, Authorization, or protected-access authority.
11. Registration completion does not itself authenticate the actor, establish a Principal or Session, or authorize Account access.
12. Identity owns the authoritative Identity-to-Customer association; Customer owns Customer and Account truth; association evidence must be unambiguous and fail closed.
13. Retry, idempotency, and concurrency semantics prevent duplicate or harmful effects without selecting a wire-level Idempotency Key design.
14. Owner-local transactions remain separate, distributed atomicity is not assumed, and neither Module reads or writes the other's internal persistence.
15. Partial and uncertain outcomes remain incomplete, distinguishable, default-denied, retryable where safe, and reconcilable from authoritative evidence.
16. Reconciliation preserves ownership, provenance, accepted history, current truth, duplicate safety, and default denial.
17. External disclosure resists enumeration and does not expose protected existence, credentials, verification secrets, internal persistence, or unsafe reason detail.
18. Security, privacy, failure-closed, audit, and observability requirements remain implementation-neutral and introduce no numerical policy.
19. Product Decision 28 and every explicit non-decision remain unresolved.
20. The document defines no route, method, field, schema, header, exact status mapping, or RFC 9457 wire representation and does not compete with or redefine the paired OpenAPI artifact.
21. Traceability covers the governing Product, Architecture, Domain, Backend, ADR, DEC, API, and Security authority used by the workflow.
22. Review identifies no invented provider behavior, persistence design, Product policy, Domain authority, cardinality, Contract wire detail, infrastructure, or implementation claim.
23. The paired OpenAPI 3.1 artifact exists as the authoritative executable HTTP wire Contract and MUST remain synchronized with this Contract without inventing or weakening its workflow and ownership semantics; conflicting semantics constitute a Contract defect.
24. Contract verification planning covers completion, pending, rejection, conflict, duplicate, retry, concurrency, partial failure, uncertainty, reconciliation, enumeration resistance, default denial, and authority boundaries.
25. Changes establishing or governing this Contract affect only explicitly authorized Contract artifacts, pass whitespace validation, and introduce no unrelated repository changes.
