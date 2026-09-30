# Initial Customer Login Contract

## 1. Purpose and Scope

This governed human-readable Contract defines the workflow, ownership, Authentication, Principal, Session, association, failure, uncertainty, retry, concurrency, security, observability, and non-decision semantics for initial local Customer Login.

Login is an Identity-owned externally consumed capability. It is independently governed from Customer Registration. Completion of Registration does not perform Login, establish Authentication, create a Principal or Session, or transfer Login authority to Customer. Conversely, Login does not create or complete Registration.

This Contract is paired with `initial-customer-login.openapi.yaml`, which is the authoritative executable HTTP wire Contract under `API.md` section 64.2. This Markdown Contract is authoritative only for the human-readable workflow, ownership, lifecycle, orchestration, cross-Module, uncertainty, rationale, traceability, non-decision, failure, and security semantics it defines.

This document MUST NOT be interpreted as defining or redefining a route, HTTP method, request or response field, HTTP status, cookie name, header, security-scheme representation, wire schema, or RFC 9457 mapping. The paired Markdown and OpenAPI artifacts MUST remain synchronized, and conflicting semantics constitute a Contract defect that MUST be corrected. This document does not claim that Login implementation exists.

## 2. Authority and Governing Sources

This Contract is subordinate to the repository decision hierarchy and the following governing sources:

- [PRODUCT.md](../../../.ai/core/PRODUCT.md), [ARCHITECTURE.md](../../../.ai/core/ARCHITECTURE.md), [SECURITY-STANDARDS.md](../../../.ai/core/SECURITY-STANDARDS.md), [DECISIONS.md](../../../.ai/core/DECISIONS.md), and [GLOSSARY.md](../../../.ai/core/GLOSSARY.md);
- [API.md](../../../.ai/backend/API.md), including the paired Contract artifact and authority convention;
- the Approved [Identity Domain Specification](../../domains/identity/identity-domain.md) and [Customer Domain Specification](../../domains/customer/customer-domain.md);
- the Approved [Identity Backend Specification](../../backend/identity/identity-backend.md) and [Customer Backend Specification](../../backend/customer/customer-backend.md);
- Accepted [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md), [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), [ADR-0021](../../adr/ADR-0021-customer-authentication-authority-strategy.md), and [ADR-0022](../../adr/ADR-0022-identity-customer-account-association-strategy.md); and
- Accepted [DEC-0003](../../decisions/DEC-0003-initial-local-customer-credential-mechanism.md), [DEC-0004](../../decisions/DEC-0004-customer-login-identifier-semantics.md), [DEC-0005](../../decisions/DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), [DEC-0006](../../decisions/DEC-0006-customer-password-hashing-verification-strategy.md), [DEC-0007](../../decisions/DEC-0007-initial-customer-password-policy.md), and [DEC-0008](../../decisions/DEC-0008-initial-customer-email-verification-policy.md).

If this Contract conflicts with a higher-authority source, the higher-authority source prevails and the Contract defect MUST be corrected. Canonical repository terminology applies. The terms MUST, MUST NOT, REQUIRED, SHALL, SHALL NOT, SHOULD, SHOULD NOT, and MAY have their repository-governed normative meanings.

## 3. Ownership and Authority Boundaries

Identity owns the external Login capability and all Login security authority, including:

- interpretation and lookup of the Customer login identifier;
- local credential verification and Authentication evidence;
- the Authentication decision;
- Principal establishment;
- authoritative Session establishment and lifecycle truth; and
- the authoritative Identity-to-Customer association.

Customer owns Customer and Account business truth, Customer-to-Account relationships, and contextual Authorization for Customer and Account Resources, actions, properties, associations, and state.

Login MUST NOT create, replace, delete, merge, or rewrite Customer or Account truth. Identity evidence MAY be consumed through bounded project-owned Contracts but MUST NOT become Customer truth automatically. Customer MUST NOT reinterpret a login identifier, credential result, Authentication result, Principal, or Session as Customer or Account authority.

Neither Module may read or write the other's internal persistence, Entities, Repositories, mappings, framework objects, configuration, or transaction state. Physical co-location in one deployable application or database transfers no authority.

## 4. Login Actor and Trust Boundary

The initial actor is an unauthenticated caller requesting local Customer Authentication. Every caller-supplied identifier, password, reference, header, client state, and assertion is untrusted.

Request receipt, syntactic validity, possession or knowledge of an identifier, password submission, Customer or Account existence, frontend state, prior Registration completion, or a prior response MUST NOT be treated as Authentication, association, ownership, mailbox control, recovery authority, or Authorization evidence.

Only current accepted evidence from the owning server-side authority may establish a protected outcome.

## 5. Login Identifier Semantics

Email is the single initial Customer-facing login identifier under DEC-0004. Phone number, username, Customer identifier, Account identifier, Principal identifier, credential identifier, or another identifier category MUST NOT become an alternative or fallback Login identifier through this Contract.

Login-email input is untrusted lookup input. Identity MUST apply DEC-0005 input, strict canonical comparison, uniqueness, retained-ownership, collision, concurrency, failure, and lifecycle semantics without redefining its algorithm.

In particular:

- the initial local part and domain are interpreted only under DEC-0005;
- strict IDNA2008 domain handling remains authoritative;
- provider-specific dot removal, plus-tag removal, alias rewriting, or mailbox-provider canonicalization MUST NOT affect Login equality;
- lookup equality MUST NOT depend on locale, database collation, provider behavior, or implementation convenience; and
- a login-email change MUST preserve stable Identity and applicable Customer and Account relationships unless separately governed authority changes them.

Identifier submission, validity, canonicalization, lookup, or possession is not proof of Identity, mailbox control, Authentication, Customer or Account ownership, association, verification, recovery authority, or Authorization.

Invalid, ambiguous, conflicting, colliding, unavailable, stale, or uncertain identifier evidence MUST fail closed. Login MUST NOT silently select among conflicting candidates, merge records, transfer ownership, reassign an identifier, or create a new Identity, Customer, Account, credential, Principal, Session, or association.

## 6. Credential Verification

The initial local Customer credential is the single password-based credential category selected by DEC-0003. Identity alone owns credential verification and its security evidence.

Verification MUST follow DEC-0006. The submitted raw password SHALL:

- enter only the Identity-owned credential-verification boundary;
- remain transient and purpose-limited;
- be verified using the governed supported verifier profile represented by authoritative Identity state; and
- remain excluded from Customer and Account state, persistence as plaintext, URLs, responses, Logs, Metrics, Traces, events, analytics, caches, fixtures, exports, and support evidence.

Verifier material is Identity-owned sensitive security data and MUST NOT be returned, logged, emitted, copied into Customer state, or exposed through ordinary operational evidence.

A successful password/verifier match is credential evidence only. It does not independently establish Authentication, a Principal, Session, Customer or Account ownership, association, mailbox control, recovery authority, or Authorization.

Malformed, truncated, non-canonical, unsupported, withdrawn, unexpectedly downgraded, resource-unsafe, unavailable, failed, non-matching, or uncertain verifier evidence MUST NOT establish successful credential verification, Authentication, a Principal, or a Session.

DEC-0007 governs password establishment and change policy. Login MUST NOT reinterpret its establishment policy as a new Login-time credential-acceptance rule. In particular, Login MUST NOT reject an otherwise supported authoritative credential merely by applying a new 15-to-64-code-point establishment check to submitted Login input. Verification MUST preserve the same accepted password representation semantics required by the authoritative verifier without silently trimming, changing case, truncating, repairing, or otherwise changing the submitted secret.

## 7. Current-Login-Email Verification Gate

DEC-0008 requires accepted Identity-owned verification evidence for the current authoritative login email before initial Customer Authentication may succeed.

A correct credential result does not bypass this gate. Verification evidence for an earlier or superseded login email does not verify the current login email. Dispatch, delivery, opening, client or UI state, identifier knowledge, a verification request, or an incomplete challenge does not establish verified state.

Rejected, invalid, expired where applicable, stale, replayed, conflicting, ambiguous, superseded, unavailable, failed, or uncertain verification evidence MUST fail closed and MUST NOT establish successful Customer Authentication, a Principal, or a Customer Session.

Current-login-email verification proves control of that current authoritative email only. It does not independently prove real-world Identity, credential validity, Customer or Account ownership, association, Authentication, Authorization, entitlement, recovery authority, or unrelated Domain truth.

## 8. Authentication Semantics

Identity MAY represent initial Customer Authentication as successful only when all applicable current Identity-owned evidence is accepted for the same Login attempt and context, including:

1. the submitted login email is valid under DEC-0005 and resolves without ambiguity to authoritative retained Identity-owned login-identifier state; failure to satisfy this internal Authentication condition MUST remain subject to the enumeration-resistant external disclosure requirements in Section 13 and MUST NOT become a separately observable protected-existence signal;
2. the submitted password successfully verifies against an accepted supported authoritative verifier under DEC-0006;
3. accepted Identity-owned verification evidence applies to the current authoritative login email under DEC-0008;
4. the applicable Identity and credential are currently eligible for Authentication under governing Security authority; and
5. no required evidence is invalid, stale, replayed, conflicting, ambiguous, superseded, unavailable, partial, or uncertain.

Request receipt, processing, email submission, password submission, identifier lookup, Customer or Account existence, client state, a credential match alone, or partial completion MUST NOT be represented as successful Authentication.

Authentication request receipt, processing, accepted success, denial, failure, unavailability, abuse handling, replay, conflict, and uncertainty MUST remain distinguishable inside the trusted boundary where diagnosis, audit, response, or recovery differs. Externally observable behavior MUST collapse sensitive distinctions whenever disclosure would reveal protected Identity, Customer, Account, credential, verification, association, or Session state.

Partial or uncertain Authentication MUST NOT be represented as success.

## 9. Principal Establishment

Identity alone establishes the authenticated Principal. Principal establishment may occur only after Identity accepts current Authentication evidence under this Contract and all applicable governing authority.

Invalid, stale, replayed, conflicting, ambiguous, unavailable, partial, failed, or uncertain evidence MUST NOT establish a Principal. A client assertion, identifier, credential submission, Customer or Account reference, frontend state, or prior Session MUST NOT create Principal truth.

A Principal is trusted actor context, not Customer or Account business truth. Principal existence does not independently establish the authoritative Identity-to-Customer association or authorize every Customer, Account, property, Resource, action, or Domain state.

## 10. Session Establishment and Lifecycle

Under ADR-0019, the initial first-party browser Authentication flow uses an Identity-owned authoritative server-side Session with protected browser-cookie custody. Under ADR-0020, Spring Session JDBC backed by the governed application PostgreSQL database remains the production Session-store mechanism.

After successful Authentication, Identity MAY establish a Session only when it can bind that Session to the accepted Principal and preserve every applicable security gate. Session establishment is a distinct authoritative outcome and MUST NOT be inferred from credential verification, Authentication processing, browser state, or cookie presence alone.

For the normal first-party browser Login flow:

- the browser MUST NOT directly own an Access Token or Refresh Token;
- the browser credential represents or references authoritative server-side Session state and is not itself authoritative;
- the Session identifier MUST change after successful Authentication and after privilege elevation where governing authority requires it;
- Identity owns Session identity, Principal association, validity, expiry, renewal eligibility, revocation, logout or termination, compromise, invalidation, and uncertainty; and
- Spring Session and PostgreSQL implement bounded infrastructure and storage mechanics without gaining Identity or Authorization authority.

Invalid, expired, revoked, terminated, compromised, disabled, stale-privilege, unavailable, or uncertain Session state MUST fail closed. Renewal, retry, race, restart, stale local state, or reordered processing MUST NOT restore withdrawn access or overwrite newer authoritative Session truth.

Customers MAY have multiple concurrent Sessions. Each Session remains independently identifiable and revocable. This Contract selects no maximum Session or device count.

This Contract does not select an exact Session lifetime, inactivity period, renewal or rotation interval, cookie name, cookie `Domain`, cookie `Path`, exact `SameSite` value, Session identifier format, cryptographic implementation, propagation interval, or concurrent-Session limit.

## 11. Identity-to-Customer Association

Identity owns the authoritative Identity-to-Customer association under ADR-0022. Login email, credential match, Authentication, Principal, Session, Customer identifier, Account identifier, or caller assertion alone is not association proof.

Use of authenticated Customer context requires current authoritative association evidence that identifies the applicable Identity and Customer and is sufficient and unambiguous for the operation. Customer may consume bounded current association evidence but MUST NOT create, transfer, reinterpret, or become authoritative for the security-side association.

Incomplete, conflicting, ambiguous, stale, unavailable, withdrawn, or uncertain association evidence MUST fail closed for Customer context. It MUST NOT cause arbitrary selection, silent merge, transfer, reassignment, replacement, or creation of Identity, Customer, Account, or association state.

This contextual requirement establishes no global Identity-to-Customer, Customer-to-Identity, or Customer-to-Account cardinality and no shared or delegated Account semantics.

## 12. Customer and Account Contextual Authorization

Successful Authentication, Principal establishment, and Session possession are not universal Authorization and do not create or rewrite Customer or Account truth.

Protected Customer or Account operations require current owning-Domain contextual Authorization using the applicable Principal, Resource, action, property, authoritative association evidence, Customer or Account relationship, and current Domain state. Customer retains the decision over whether its current invariants permit access.

Login success MUST NOT claim that every Customer or Account operation is authorized. Identity MUST NOT centralize or override Customer business invariants, and Customer MUST NOT treat a Session, identifier, or Authentication result alone as sufficient authority.

## 13. Enumeration Resistance and Safe Disclosure

Externally observable Login behavior MUST resist enumeration and MUST NOT reveal protected existence or sensitive reason detail. Where disclosure would create an oracle, external behavior MUST NOT distinguish whether an outcome arose from:

- absent, retained, conflicting, or colliding login-identifier ownership;
- login-email syntax or canonical lookup state beyond safely actionable input handling;
- Identity, Customer, or Account existence;
- password mismatch or credential absence;
- malformed, unsupported, downgraded, withdrawn, or migration-related verifier state;
- current-login-email verification state;
- Identity-to-Customer association state;
- Customer-to-Account relationship or contextual Authorization state;
- Session, internal persistence, dependency, abuse-control, or security handling; or
- another protected failure or uncertainty condition.

Status, response shape, wording, timing, retry behavior, frontend behavior, telemetry, analytics, and support evidence MUST NOT become protected-existence oracles.

Logs, Metrics, Traces, Audit Records, events, exports, and support evidence MUST exclude raw passwords, verifier material, Session secrets, verification secrets, unnecessary PII, protected identifiers, internal persistence details, and exploitable security-control detail while retaining sufficient privacy-safe evidence for authorized diagnosis and response.

## 14. Retry, Duplicate, Replay, and Concurrency Semantics

Retried, duplicated, replayed, concurrent, delayed, or reordered Login processing MUST verify current authoritative evidence and preserve the latest accepted Identity, credential, verification, Principal, Session, association, and revocation truth.

Such processing MUST NOT:

- fabricate credential verification or Authentication success;
- create unintended duplicate authoritative Principal or Session effects;
- restore revoked, terminated, compromised, disabled, expired, or otherwise withdrawn access;
- allow stale or replayed work to overwrite newer authority;
- bypass the current-login-email verification gate;
- multiply or reassign association effects; or
- convert partial, failed, conflicting, unavailable, or uncertain work into success.

Safe observable duplicate handling MUST remain compatible with enumeration resistance. Internally distinct replay, duplicate, concurrency, and uncertainty outcomes MAY remain distinguishable for authorized handling without exposing protected state externally.

The paired OpenAPI artifact governs whether and how an Idempotency Key is represented at the wire boundary under API.md. This Markdown Contract selects no Idempotency Key header, key format, scope, compatibility fingerprint, retention period, persistence mechanism, or HTTP treatment.

## 15. Failure, Uncertainty, Recovery, and Reconciliation

Login fails closed whenever required authoritative evidence is invalid, stale, replayed, conflicting, ambiguous, superseded, unavailable, partial, failed, or uncertain.

Failure closed means no successful Customer Authentication, Principal, or valid Session establishment is represented and no Customer or Account access is granted from that attempt. Unknown or uncertain outcomes MUST NOT be converted into fabricated success or an unsupported authoritative failure classification.

Internal diagnostic and Audit Record truth MUST retain the distinctions needed for secure response, investigation, retry, reconciliation, and accountable repair. External disclosure remains bounded by enumeration resistance and privacy requirements.

Failure or uncertainty in one owner does not authorize destructive rollback, deletion, merger, or rewriting of independently accepted authoritative state owned elsewhere. Reconciliation, where required, MUST preserve source authority, provenance, accepted history, effective revocation, latest truth, least privilege, and default denial. Repair MUST be authorized and MUST NOT promote client state, projections, telemetry, or support assertions into Identity, Customer, Account, association, or Session truth.

This Contract selects no recovery mechanism, reconciliation job, queue, event, schedule, retry count, support workflow, or repair implementation.

## 16. Security, Privacy, Audit, and Observability

Login MUST apply default denial, least privilege, purpose limitation, data minimization, protected transport, trusted server-side validation, abuse resistance, replay resistance, fixation resistance, credential-stuffing protections, and safe output handling.

Applicable protected-cookie, CSRF, and CORS requirements from ADR-0019, ADR-0020, Security Standards, and API Standards remain in force. CORS MUST NOT be treated as CSRF protection. This Contract does not select their unresolved concrete wire attributes or configuration.

Login processing MUST propagate governed correlation context across supported boundaries. Correlation values are diagnostic context only and MUST NOT become Login intent, Identity, idempotency, Authentication, association, or Authorization evidence.

Privacy-safe Logs, Metrics, Traces, and proportional Audit Records MUST distinguish material Login attempt, accepted Authentication, denial, abuse, failure, uncertainty, Principal establishment, Session establishment, and security response outcomes where governance requires them. Evidence MUST be bounded, attributable, correlated, and sufficient for accountable investigation without exposing protected state.

Passwords, verifier material, Session secrets, verification secrets, tokens, private security evidence, unnecessary Sensitive Data, protected identifiers, internal implementation classes, stack traces, database details, provider details, and unsafe reason detail MUST NOT appear in telemetry, analytics, ordinary errors, examples, exports, or support evidence.

This Contract selects no telemetry schema, retention period, alert threshold, monitoring product, rate, delay, lockout value, SLO, SLA, capacity target, or incident-severity model.

## 17. Traceability to Governing Authority

| Contract concern | Governing source | Applied Login boundary |
| --- | --- | --- |
| Customer Login Product capability | `PRODUCT.md` | Login is independently governed from Registration and does not itself authorize Customer or Account operations. |
| Identity-owned local Authentication | `ARCHITECTURE.md`; ADR-0021 | Identity owns credential verification, Authentication, Principal establishment, and the external Login capability. |
| Server-side browser Session | ADR-0019 | Successful first-party Authentication uses authoritative server-side Session state and protected browser-cookie custody, not browser-owned bearer tokens. |
| Production Session store | ADR-0020 | Spring Session JDBC and governed PostgreSQL provide bounded Identity Session infrastructure without gaining Domain authority. |
| Identity-to-Customer association | ADR-0022 | Identity owns the association; authenticated Customer context requires current unambiguous evidence without global cardinality assumptions. |
| Initial credential category | DEC-0003 | The initial local Customer credential is password-based with no initial fallback category. |
| Login identifier | DEC-0004 | Email is the single initial Customer-facing login identifier and remains untrusted input. |
| Login-email comparison and lifecycle | DEC-0005 | Identity applies governed canonical comparison, retained ownership, collision, lifecycle, concurrency, and safe-failure semantics. |
| Password verification | DEC-0006 | Identity verifies the authoritative supported Argon2id representation; a match alone is not Authentication or Authorization. |
| Password policy boundary | DEC-0007 | Establishment/change policy is not reinterpreted as a Login-time 15-to-64-code-point rejection rule. |
| Current-login-email verification | DEC-0008 | Accepted evidence for the current authoritative login email is mandatory before initial Customer Authentication, Principal, or Session success. |
| Identity requirements | Identity Domain and Backend Specifications | Preserve Identity, credential, Authentication, Principal, Session, failure, concurrency, Contract, security, audit, and reconciliation authority. |
| Customer requirements | Customer Domain and Backend Specifications | Customer retains Customer/Account truth, isolation, association consumption, contextual Authorization, failure, and privacy authority. |
| API and wire authority | `API.md` | Markdown governs human-readable semantics; the paired OpenAPI 3.1 artifact governs authoritative executable HTTP wire behavior. |
| Security and privacy | `SECURITY-STANDARDS.md` | Preserve default denial, enumeration resistance, credential protection, Session security, least privilege, safe telemetry, and security testing. |
| Canonical terminology | `GLOSSARY.md` | Identity, Principal, Authentication, Authorization, Session, Customer, Account, and related terms remain distinct. |

## 18. Explicit Non-Decisions

This Contract does not select, define, authorize, or claim completion of:

- Product Decision 28 or post-deletion login-email reuse policy;
- a recovery mechanism, provider, channel, factor, token, expiry, or support workflow;
- ordinary Customer MFA policy, factor, provider, protocol, challenge, fallback, or recovery;
- an external Identity Provider, social login, SSO, federation, OAuth2, OIDC, SAML, account linking, provider subject, redirect, callback, Claims mapping, or Scope design;
- a service-to-service credential or Service Principal Authentication model;
- an exact Session lifetime, inactivity period, renewal threshold, rotation interval, cleanup interval, or propagation interval;
- an exact cookie name, `Domain`, `Path`, `SameSite` value, expiry, or other unresolved cookie attribute;
- a Session identifier format, cryptographic implementation, signing mechanism, or randomness facility;
- a maximum concurrent Session or device policy, device identity model, or trusted-device policy;
- a concrete database schema, table, column, key, index, constraint, sequence, SQL, ORM mapping, JDBC mapping, Entity, Repository, or Flyway migration;
- retry counts, timeouts, rate limits, lockout thresholds, progressive delays, circuit breakers, capacity targets, SLAs, SLOs, RPOs, or RTOs;
- a fraud provider, score, model, manual-review workflow, or abuse-control implementation;
- frontend or UI behavior, route, form, component, storage, accessibility implementation, or client-state model;
- infrastructure, hosting, deployment, networking, replication, failover, cache, queue, event technology, or provider topology;
- global Identity-to-Customer, Customer-to-Identity, or Customer-to-Account cardinality or shared/delegated Account semantics;
- an administrative Role or Permission matrix, Product Decision 23, or unrelated privileged-access policy;
- Login route, HTTP method, request or response property, HTTP status, header, exact cookie wire representation, OpenAPI security scheme, RFC 9457 mapping, or example beyond the authoritative paired OpenAPI Contract;
- Idempotency Key wire behavior beyond the paired OpenAPI Contract, or an Idempotency Key format, scope, retention period, persistence mechanism, or HTTP treatment not established there;
- concrete Customer or Account contextual Authorization rules beyond preserving owning-Domain authority;
- dependency admission, implementation class, executable implementation, deployment, production readiness, or completed testing; or
- creation, modification, completion, or dependency on the separate Initial Customer Registration Contract.

## 19. Acceptance and Validation Criteria

The governed Login Contract change is valid only when review confirms all of the following:

1. The artifact is located under the Identity owner package and identifies Identity as owner of the external Login capability without making Login dependent on Registration.
2. The Contract transfers no Identity, Customer, Account, Authentication, Session, association, or Authorization authority.
3. Email remains the single initial Customer-facing Login identifier and is treated only as untrusted lookup input.
4. DEC-0005 comparison, strict IDNA2008, retained-ownership, collision, lifecycle, concurrency, and failure semantics are consumed without redefining their algorithm or applying provider-specific rewriting.
5. Identifier submission, validity, lookup, or possession cannot establish Authentication, ownership, association, verification, recovery authority, or Authorization.
6. The initial credential remains password-based under DEC-0003 and verification follows DEC-0006 exclusively within the Identity credential boundary.
7. Raw password and verifier material remain transient or protected as applicable and absent from Customer state, outputs, telemetry, events, examples, and support evidence.
8. A credential match alone cannot establish Authentication, Principal, Session, association, Customer/Account ownership, or Authorization.
9. DEC-0007 establishment/change policy is not reinterpreted as a Login-time 15-to-64-code-point rejection rule.
10. Accepted current-login-email verification evidence under DEC-0008 is required before initial Customer Authentication, Principal, or Session establishment succeeds.
11. Evidence for an old, superseded, invalid, stale, replayed, conflicting, ambiguous, unavailable, or uncertain login email cannot satisfy the verification gate.
12. Identity represents Authentication as successful only from all required current accepted Identity-owned evidence and never from request receipt, client state, existence, partial processing, or uncertainty.
13. Only Identity establishes the Principal, and Principal existence grants no automatic Customer or Account authority.
14. The normal first-party browser flow uses an authoritative Identity-owned server-side Session with protected cookie custody and issues no browser-owned Access Token or Refresh Token.
15. Spring Session JDBC backed by governed PostgreSQL remains the production Session-store mechanism without becoming Identity, Customer, or Authorization authority.
16. Session establishment binds to the accepted Principal, changes the Session identifier where governed, and cannot restore revoked or withdrawn access through retry, renewal, race, restart, or stale state.
17. Invalid, expired, revoked, terminated, compromised, disabled, stale, unavailable, or uncertain Session state fails closed, while concurrent Sessions remain independently revocable.
18. Authenticated Customer context requires current authoritative and unambiguous Identity-to-Customer association evidence without establishing global cardinality.
19. Customer retains Customer and Account contextual Authorization, and Login success does not authorize every Customer or Account operation.
20. Externally observable behavior resists enumeration and does not expose sensitive failure causes or protected Identity, Customer, Account, credential, verifier, verification, association, Session, or persistence state.
21. Retried, duplicated, replayed, concurrent, delayed, or reordered processing cannot fabricate success, create unintended duplicate authoritative effects, restore withdrawn access, or overwrite newer authority.
22. The Markdown Contract defines no Idempotency Key wire behavior; applicable wire behavior is governed by the paired executable OpenAPI Contract under API.md.
23. Invalid, failed, unavailable, partial, conflicting, ambiguous, stale, superseded, or uncertain required evidence preserves default denial without destructive rewriting of independently accepted owner truth.
24. Logs, Metrics, Traces, Audit Records, correlation, and support evidence remain privacy-safe, purpose-limited, and free of credential material, Session secrets, protected existence, and internal implementation detail.
25. Applicable protected-cookie, CSRF, CORS, least-privilege, abuse-resistance, replay, fixation, and Security requirements remain in force without selecting unresolved concrete configuration.
26. Neither Identity nor Customer accesses the other's internal persistence or framework state; cross-Module evidence remains bounded and source-owned.
27. Traceability covers the governing Product, Architecture, Security, API, Domain, Backend, ADR, DEC, and terminology sources without inventing authority.
28. Product Decision 28 and every other explicit non-decision remain unresolved.
29. The Markdown Contract invents no route, method, request or response field, HTTP status, header, cookie name, wire schema, token format, Session identifier, RFC 9457 mapping, provider, persistence design, numerical policy, or implementation beyond the authoritative paired OpenAPI Contract.
30. The paired OpenAPI 3.1 artifact exists as the authoritative executable HTTP wire Contract and MUST remain synchronized with this Contract without weakening its ownership, Authentication, security, failure, uncertainty, or non-decision semantics; conflicting semantics constitute a Contract defect.
31. Changes establishing or governing this Contract affect only explicitly authorized Contract artifacts, pass whitespace validation, and introduce no unrelated repository changes.
