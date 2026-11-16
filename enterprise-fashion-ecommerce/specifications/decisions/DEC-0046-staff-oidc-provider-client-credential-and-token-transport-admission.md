# DEC-0046 — Staff OIDC Provider, Client, Credential and Token-Transport Admission

- **Identifier:** DEC-0046
- **Title:** Staff OIDC Provider, Client, Credential and Token-Transport Admission
- **Type:** Identity Decision / Security Decision / Operational Decision
- **Status:** Proposed
- **Version:** 0.2.0
- **Date:** 2026-11-16
- **Last Updated:** 2026-11-16
- **Owner:** Identity
- **Required Accountable Reviews:** Architecture, Security, Platform/Operations, Backend Engineering
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## 1. Purpose and Decision Context

[ADR-0023](../adr/ADR-0023-staff-authentication-authority-and-mechanism.md)
accepts enterprise-managed OpenID Connect as the upstream Staff Authentication
mechanism. It requires a backend authorization-code flow, PKCE S256, state and nonce,
Identity-owned verification, existing Staff-association resolution and authoritative
server-side Sessions. It also separates Authentication from administrative
Authorization.

ADR-0023 deliberately does not admit an Identity Provider, tenant, issuer, client,
token endpoint, client-authentication method, credential technology, secret store,
network path, provider assurance mapping or operational activation. This record
prepares those provider-specific credential and token-transport choices for one exact,
immutable provider-registration revision.

The Approved Identity backend specification and Backend baseline already require
provider Ports and Adapters, safe external-effect boundaries, explicit timeouts,
bounded retry where permitted, uncertainty preservation, Secret protection, failure
classification and proportional audit. The Approved Security Standards already govern
Identity Provider integration, Secrets, keys, certificates, outbound network security,
TLS, SSRF protection, monitoring and Security review. This record must select values
within those controls; it cannot weaken or duplicate their authority.

Existing dormant implementation can represent and bind registration identity and
revision, issuer, client identity, redirect URI, token endpoint, client-authentication
method, token trust and transport bounds. It can prepare an internal exchange request.
That structural capability is implementation evidence only. It contains no operational
provider registration, credential source or network transport and grants no admission.

This is a **proposal for accountable review**, not a provider selection or implementation
authorization. Every unknown or unconfirmed operational value below remains
**PENDING OWNER DECISION**.

## 2. Scope and Explicit Exclusions

### 2.1 In Scope

For one exact provider-registration identity and immutable revision, this record covers:

- provider, tenant/directory, Environment, issuer and client admission;
- exact redirect URI and token endpoint;
- client-authentication method;
- credential or signing-key authority, custody, storage, retrieval and lifecycle;
- outbound destination, DNS/IP, SSRF, proxy and TLS policy;
- connection, response and response-size limits;
- redirect, retry, uncertain-exchange and recovery policy;
- token-exchange transport failure classification;
- protected operational audit and monitoring ownership; and
- independent accountable acceptance and separate implementation-readiness gates.

### 2.2 Explicitly Out of Scope

This record does not:

- reselect the OIDC mechanism or amend ADR-0023;
- change Customer Authentication;
- redesign or provision Staff associations;
- select Staff Roles, Permissions or administrative assignments;
- grant Product or other owning-Domain Authorization;
- establish or activate a Staff Session;
- activate Staff login or callback Endpoints;
- admit positive Staff authentication evidence;
- approve an HTTP token-exchange implementation;
- admit a provider SDK, new dependency or cryptographic artifact;
- create credentials, Secrets, keys or operational configuration; or
- authorize deployment or operational activation.

[DEC-0012](DEC-0012-administrative-role-permission-matrix.md) remains Proposed and
non-authoritative. It is referenced only to preserve the boundary between Staff
Authentication and administrative Authorization. Acceptance of this record could not
grant a Staff Permission or authorize a Product action.

## 3. Authoritative References and Inherited Controls

| Source | Current authority | Reused control | Boundary preserved |
| --- | --- | --- | --- |
| ADR-0023 | Accepted; authoritative | Enterprise-managed OIDC, authorization-code flow, PKCE S256, state/nonce, Identity verification, exact issuer/client trust and server-side Sessions | Provider admission, credentials, transport, assurance mappings, implementation and activation remain separate gates. |
| [Identity backend](../backend/identity/identity-backend.md) | Approved | Identity-owned orchestration; provider isolation; safe external-effect boundary; explicit failure, uncertainty, Secret protection, audit and recovery | It intentionally selects no provider, credential mechanism, topology or numerical limits. |
| [Backend baseline](../backend/shared/backend-baseline.md) | Approved | Provider Ports/Adapters; explicit timeout and failure handling; safe retry; uncertainty; Secret, configuration and audit safeguards | It does not select a provider, client library, retry count, timeout or infrastructure. |
| [Security Standards](../../.ai/core/SECURITY-STANDARDS.md) | Approved; authoritative | Identity Provider validation, Secrets and keys, TLS, SSRF, audit, monitoring, threat modelling and Security gates | Concrete provider, custodian, network, TLS and operational choices still require evidence and approval. |
| DEC-0012 | Proposed; non-authoritative | Boundary evidence only: Authentication is not administrative Authorization | No proposed Role, Permission, assignment or founder policy becomes authority here. |

The following are established controls, not choices reopened by DEC-0046:

- request, callback, token Claim and HTTP header values cannot choose provider trust;
- authorization codes, PKCE verifiers, credentials and tokens remain server-side and
  must not enter logs, audit records, browser persistence or ordinary configuration;
- required provider evidence or configuration that is missing, invalid, stale,
  conflicting or unavailable cannot establish Authentication success;
- token exchange is not ID-token verification, Staff association resolution, Session
  establishment or administrative Authorization;
- external calls remain outside unsafe long-running Database Transactions; and
- unknown or uncertain provider outcomes must not be converted into success.

## 4. Provider Admission Matrix

Acceptance requires one complete, attributable row set. A capability claim must be
classified as documented, configured, tested, unsupported or unknown. Documentation
alone does not prove tenant configuration or operational behavior.

| Admission field | Proposed requirement | Current value / evidence | Accountable confirmation |
| --- | --- | --- | --- |
| Provider identity | Name the legal/operational Identity Provider and service offering. | **PENDING OWNER DECISION** | Identity, Architecture and Security |
| Tenant/directory ownership | Identify the accountable organization, tenant custodian and administrative boundary. | **PENDING OWNER DECISION** | Identity and Platform/Operations |
| Environment | Identify the exact development, test, staging or production registration and prohibit cross-Environment credential reuse. | **PENDING OWNER DECISION** | Platform/Operations and Identity |
| Issuer | Record the exact case-sensitive issuer URI admitted for this revision. | **PENDING OWNER DECISION** | Identity and Security |
| Client ID | Record the exact provider-issued client identifier and owning application registration. | **PENDING OWNER DECISION** | Identity and Platform/Operations |
| Redirect URI | Record the exact backend callback URI registered with the provider. | **PENDING OWNER DECISION** | Identity, Security and Backend Engineering |
| Token endpoint | Record the exact HTTPS URI and authoritative source of that value. | **PENDING OWNER DECISION** | Identity and Security |
| Registration ID | Record the repository-owned stable registration identifier. | **PENDING OWNER DECISION** | Identity |
| Registration revision | Allocate one immutable positive revision covering every field and policy selected here. | **PENDING OWNER DECISION** | Identity and Architecture |
| Provider capability evidence | Provide attributable evidence for code flow, PKCE, assurance, freshness, disablement, subject stability, key rotation, audit, outage and support. | **PENDING OWNER DECISION** | Identity and Security |
| Stable subject semantics | Define the exact issuer/subject meaning, lifecycle and mapping to `StaffIdentityReference`; prohibit email/display-name linking. | **PENDING OWNER DECISION** | Identity and Security |
| Disablement authority | Identify who may disable the registration, required evidence, propagation expectations and incident path. | **PENDING OWNER DECISION** | Identity, Security and Platform/Operations |
| Change authority | Define revision creation, review, replacement, rollback and historical retention without latest-revision fallback. | **PENDING OWNER DECISION** | Identity and Architecture |

An admitted revision must be resolved only by its exact registration ID and revision.
Replacement, disablement or rotation must not silently substitute a different revision
for an in-flight transaction. The operational policy for disabling an in-flight
revision remains pending and must fail closed until approved.

## 5. Client-Authentication Method

The dormant registration model represents `CLIENT_SECRET_BASIC` and
`PRIVATE_KEY_JWT`. These are technical representations, not admitted choices.
Accountable owners must select exactly one supported method for each admitted
registration revision or reject both and require a separately governed change.

### 5.1 Evidence Required for Either Method

- provider documentation and tenant-specific configuration evidence;
- exact client registration and Environment binding;
- threat analysis covering disclosure, replay, rotation and outage;
- credential/key owner, custodian and permitted workload;
- provider-side revocation and replacement procedure;
- local retrieval, non-disclosure, audit and failure behavior;
- executed negative and rotation tests; and
- independent Identity, Security, Platform/Operations and Engineering review.

### 5.2 Method Dispositions

| Method | Proposed disposition | Required additional evidence |
| --- | --- | --- |
| `CLIENT_SECRET_BASIC` | **PENDING OWNER DECISION** | Secret generation/issuance, protected storage, runtime retrieval, transmission only to the exact endpoint, rotation overlap, revocation, compromise response and prohibition from logs/request diagnostics. |
| `PRIVATE_KEY_JWT` | **PENDING OWNER DECISION** | All controls below plus approved provider support and key-registration evidence. |

If `PRIVATE_KEY_JWT` is selected, acceptance must record:

- permitted assertion signing algorithm and key profile;
- exact token-endpoint audience semantics;
- assertion lifetime and clock-tolerance policy;
- unique `jti` generation, replay detection and retention behavior;
- signing-key authority, generation, custody and permitted workload access;
- provider and application rotation, overlap, retirement and rollback procedure; and
- compromise detection, immediate revocation, replacement and incident response.

No algorithm, lifetime, tolerance, key technology or replay-retention duration is
approved by this Proposed version.

## 6. Credential Governance

| Credential decision | Required disposition | Current status | Accountable owner |
| --- | --- | --- | --- |
| Issuer | Identify who creates or authoritatively issues the Secret/key and provider binding. | **PENDING OWNER DECISION** | Identity and Platform/Operations |
| Accountable owner | Name the function accountable for purpose, lifecycle and continued necessity. | **PENDING OWNER DECISION** | Identity |
| Operational custodian | Name the function operating storage, access, rotation and emergency response. | **PENDING OWNER DECISION** | Platform/Operations |
| Permitted workload identity | Identify the exact Environment-scoped Service Principal/workload allowed retrieval. | **PENDING OWNER DECISION** | Platform/Operations and Security |
| Storage | Select an approved secret/key-management class and protection profile; no product is selected here. | **PENDING OWNER DECISION** | Platform/Operations and Security |
| Retrieval authorization | Define least-privilege policy, authentication, purpose and denial/unavailability behavior. | **PENDING OWNER DECISION** | Platform/Operations and Security |
| Environment isolation | Prohibit production credential reuse in lower Environments and define separate identities. | **PENDING OWNER DECISION** | Platform/Operations |
| Cache policy | Decide whether runtime caching is allowed and, if so, its bound, protection, invalidation and memory-disclosure controls. | **PENDING OWNER DECISION** | Security and Backend Engineering |
| Rotation | Define interval/event triggers, overlap, deployment order, verification and rollback. | **PENDING OWNER DECISION** | Platform/Operations and Security |
| Revocation | Define immediate disablement, provider/local coordination and evidence. | **PENDING OWNER DECISION** | Identity, Security and Platform/Operations |
| Outage behavior | Define fail-closed retrieval and recovery without insecure fallback or stale-secret assumption. | **PENDING OWNER DECISION** | Identity and Platform/Operations |
| Protected access audit | Define access events, actor/workload attribution, alerts, retention and review. | **PENDING OWNER DECISION** | Security and Platform/Operations |

No secret-manager product, cloud topology, environment variable, file, keystore or
hardware facility is approved merely by appearing as an implementation possibility.

## 7. Network and Transport Trust

Structural URI validation proves only that configured values meet bounded syntax. It
does not establish DNS truth, endpoint ownership, routing, proxy behavior, certificate
trust, runtime destination safety or provider admission.

| Trust decision | Proposed requirement | Current disposition |
| --- | --- | --- |
| HTTPS enforcement | Permit only HTTPS token endpoints; never downgrade. | Established baseline requirement; implementation evidence still required. |
| Exact endpoint admission | Bind the exact endpoint to the exact registration revision; no request, token or response value may replace it. | **PENDING OWNER DECISION** for the actual endpoint. |
| Destination allowlisting | Allow only admitted endpoint host and port for this purpose. | **PENDING OWNER DECISION** |
| DNS resolution/rebinding | Define resolution timing, validation before connection, address-change handling and rebinding defense. | **PENDING OWNER DECISION** |
| IPv4/IPv6 | Define whether both families are permitted and apply equivalent validation to every resolved address. | **PENDING OWNER DECISION** |
| Private/special-use addresses | Deny loopback, link-local, metadata, multicast, unspecified, documentation and private ranges unless a separately justified, Environment-specific admission explicitly permits the required destination. | **PENDING OWNER DECISION** |
| Proxy selection | Select direct egress or one governed proxy path per Environment. | **PENDING OWNER DECISION** |
| Proxy bypass prevention | Prevent environment or library defaults from silently bypassing the selected route and destination policy. | **PENDING OWNER DECISION** |
| TLS trust source | Identify the controlled trust store/CA source and update authority. | **PENDING OWNER DECISION** |
| Certificate/hostname validation | Require chain, validity, usage and exact hostname validation; never disable verification. | Established baseline requirement; concrete trust profile pending. |
| Certificate failure | Fail closed, classify safely, alert as governed and expose no certificate/token detail to callers. | **PENDING OWNER DECISION** for alert/recovery behavior. |
| HTTP redirects | **Proposed initial policy: prohibit all token-endpoint HTTP redirects.** | **PENDING OWNER DECISION** |

Any future exception for a private or special-use destination, proxy interception or
redirect must be explicit, Environment-scoped, threat-modelled and approved through
the applicable Security governance; it cannot be inferred from successful connectivity.

## 8. Transport Operational Policy

| Policy field | Proposed position | Approval state |
| --- | --- | --- |
| Connection timeout | Require a positive bounded value per registration revision. | Numerical value **PENDING OWNER DECISION** |
| Response timeout | Require a positive bounded value per registration revision. | Numerical value **PENDING OWNER DECISION** |
| Maximum response size | Enforce a byte limit before unbounded buffering or parsing. | Numerical value **PENDING OWNER DECISION** |
| Accepted content types | Require the exact OAuth token-response media type(s) admitted by Identity/Security; reject absent or incompatible types. | **PENDING OWNER DECISION** |
| Redirect handling | **Proposed initial policy: no redirects.** A redirect attempt fails before any second destination is contacted. | **PENDING OWNER DECISION** |
| Retry behavior | **Proposed initial policy: no automatic retry of authorization-code exchange.** | **PENDING OWNER DECISION** |
| Code-consumption uncertainty | Timeout, interruption or ambiguous response must remain uncertain; never infer that the provider did not consume the code. | Established fail-closed requirement; exact terminal/recovery handling pending. |
| Recovery after uncertainty | Require a fresh governed Authentication attempt unless owners approve a demonstrably safe reconciliation path. Never replay the same code speculatively. | **PENDING OWNER DECISION** |
| Operational disablement during exchange | Revalidate the exact registration's enabled/current admissibility at the governed reliance point and define in-flight behavior. | **PENDING OWNER DECISION** |

The dormant implementation's validation ceilings are not approved operating values.
No numerical limit becomes policy until its owner disposition and supporting provider,
performance and security evidence are recorded.

## 9. Proposed Failure Classification

This table uses existing production outcome vocabulary. It proposes transport mapping
for owner review and creates no new outcome. No mapping may disclose credentials,
authorization codes, PKCE material, full tokens, provider bodies or sensitive internals.

A transaction's `CONSUMED` status alone is not proof that atomic V16 transaction
consumption occurred. Future operational exchange orchestration must receive transaction
provenance directly from the governed V16 consumption path. A caller-constructed
transaction marked `CONSUMED` is insufficient to authorize exchange and must fail as
`INVALID_TRANSACTION`; the existing outcome mapping below remains unchanged.

| Condition | Proposed existing outcome | Rationale / unresolved point |
| --- | --- | --- |
| Transaction missing, expired, replayed, superseded, corrupt or not atomically consumed | `INVALID_TRANSACTION` | Existing transaction boundary; no exchange. |
| Callback is absent, malformed, provider-error shaped or not an authorization-code callback | `REJECTED_CALLBACK` | Existing callback boundary; owner must confirm provider-error granularity. |
| Exact registration missing, disabled, ambiguous or unavailable | `VERIFICATION_UNAVAILABLE` | Existing dormant mapping; disabled-during-exchange treatment requires confirmation. |
| Credential unavailable or retrieval cannot be trusted | `VERIFICATION_UNAVAILABLE` **or** `INDETERMINATE` | **PENDING OWNER DECISION** based on operational recovery and audit semantics. |
| Credential type does not match the admitted registration method | `INDETERMINATE` | Treat as internal/configuration inconsistency; no network call. **PENDING OWNER CONFIRMATION.** |
| DNS resolution or connection failure | `PROVIDER_UNAVAILABLE` | Proposed dependency-unavailability classification; DNS policy violations may instead be `INDETERMINATE`. **PENDING OWNER DECISION.** |
| TLS negotiation, chain or hostname failure | `PROVIDER_UNAVAILABLE` **or** `INDETERMINATE` | Must fail closed; distinguish provider outage from local trust/configuration defect. **PENDING OWNER DECISION.** |
| Connection or response timeout | `PROVIDER_UNAVAILABLE` | Preserve uncertain code-consumption evidence; never automatically retry. **PENDING OWNER CONFIRMATION.** |
| HTTP redirect attempt | `INDETERMINATE` | Proposed policy violation classification; no redirect followed. **PENDING OWNER DECISION.** |
| Response exceeds admitted size | `INDETERMINATE` | Local/provider Contract violation; body not parsed. **PENDING OWNER CONFIRMATION.** |
| Non-200 OAuth rejection other than 429/5xx | `EXCHANGE_REJECTED` | Preserve provider rejection without exposing error detail. |
| HTTP 429 or 5xx | `PROVIDER_UNAVAILABLE` | Matches existing parser behavior and outage semantics. |
| HTTP 200 with malformed, ambiguous, missing or invalid token response | `INVALID_TOKEN` | Existing strict token-response parsing; no internal success. |
| Unexpected internal transport/credential failure | `INDETERMINATE` | Do not convert a defect or unknown state into provider rejection or success. |

Security and Identity must confirm every row, especially the unresolved distinction
between expected provider unavailability and local trust/configuration failure.
Backend Engineering must confirm that exception translation preserves these meanings
without exposing provider or Secret detail.

## 10. Protected Operational Audit

### 10.1 Pending Audit Decisions

| Audit concern | Required decision | Status / owner |
| --- | --- | --- |
| Event types | Define initiation, registration resolution, credential retrieval result, exchange attempt/result, policy violation, denial, uncertainty, provider degradation and configuration/credential change events. | **PENDING OWNER DECISION** — Identity and Security |
| Correlation | Define safe transaction, Session, registration ID/revision and request correlation without logging reusable security material. | **PENDING OWNER DECISION** — Identity |
| Permitted diagnostics | Allow bounded outcome, reason category, provider-safe status class, duration and non-sensitive endpoint/registration references. | **PENDING OWNER DECISION** — Security and Operations |
| Prohibited fields | Authorization codes, PKCE verifier/challenge where sensitive, client credentials, private keys, assertions, ID/Access/Refresh Tokens and sensitive response bodies are prohibited. | Established requirement; implementation evidence required. |
| Audit sink | Select a protected, tamper-resistant or tamper-evident sink separate from ordinary logs. | **PENDING OWNER DECISION** — Security and Platform/Operations |
| Retention | Select legal, security and operational retention and deletion policy. | **PENDING OWNER DECISION** — Security and Platform/Operations |
| Access control | Define least-privilege read/write/admin roles and audit access to audit evidence. | **PENDING OWNER DECISION** — Security and Platform/Operations |
| Alert ownership | Name responders for credential failure, TLS/DNS policy failure, provider degradation, repeated rejection and audit failure. | **PENDING OWNER DECISION** — Security and Operations |
| Ingestion failure | Define fail-safe behavior, alerting, buffering if permitted and whether Authentication establishment must stop. | **PENDING OWNER DECISION** — Security, Identity and Operations |
| Incident response | Define credential/key containment, provider disablement, evidence preservation, recovery authority and communications. | **PENDING OWNER DECISION** — Security and Operations |

Ordinary application logs, traces, metrics, authorization-transaction history and
provider responses are not substitutes for required Audit Records.

## 11. Accountable-Owner Dispositions

Allowed disposition values are `PENDING`, `ACCEPT`, `AMEND` and `REJECT`. Initial
dispositions are deliberately `PENDING`; no approval is inferred from authorship,
implementation, project-owner direction, pull-request review or merge.

| Accountable capacity | Required confirmation | Disposition | Attributable evidence |
| --- | --- | --- | --- |
| Identity Owner | Exact provider/tenant/client/registration facts; subject semantics; Authentication outcomes; credential consumer; disablement and recovery behavior. | **PENDING** | None recorded |
| Architecture Owner | Compatibility with ADR-0023; immutable revision boundary; external-effect, provider and Session architecture; no trust-source substitution. | **PENDING** | None recorded |
| Security Owner | Provider trust, client-authentication method, credential/key policy, network/TLS/SSRF controls, limits, retry/uncertainty, audit and residual risk. | **PENDING** | None recorded |
| Platform/Operations Owner | Tenant and registration custody, workload identity, secret/key facility, network/proxy/TLS operations, rotation, monitoring, outage and incident support. | **PENDING** | None recorded |
| Backend Engineering Owner | Implementability, exact transport behavior, failure translation, resource bounds, redaction, deterministic tests and operational diagnostics. | **PENDING** | None recorded |

Independent Engineering and Security dispositions are both mandatory. One owner,
founder or project-level instruction must not be represented as the other's review.

### 11.1 Provider-Neutral Review-Readiness Evidence

The following records are review recommendations only. They prepare bounded questions
for accountable owners and are not formally adopted dispositions, provider admission,
implementation authorization or operational approval. The formal disposition table
above remains controlling and all five dispositions remain `PENDING`.

#### Identity Review Recommendation

| Field | Recorded recommendation evidence |
| --- | --- |
| Recommendation | **CONFIRM** |
| Scope | Provider-neutral Identity controls |
| Date | 2026-11-16 |
| Reviewer | Initial Identity Owner — formal adoption required |

The recommendation covers:

- Identity-owned provider integration and Authentication outcomes;
- exact immutable registration ID/revision binding with no latest-revision fallback;
- no request-controlled provider trust selection;
- direct V16 atomic-consumption provenance;
- separation of exchange, parsing and cryptographic validation;
- separate active-only Staff-association resolution and Session establishment;
- fail-closed handling of mandatory evidence;
- governed provider replacement through an admitted revision;
- separate implementation-readiness and operational-activation gates; and
- the boundary that Authentication does not confer Authorization.

The overall Identity disposition remains **PENDING**. This recommendation does not
nominate a provider-registration revision or confirm provider-specific facts.

#### Architecture Review Recommendation

| Field | Recorded recommendation evidence |
| --- | --- |
| Recommendation | **ACCEPT** |
| Scope | Provider-neutral Architecture compatibility only |
| Date | 2026-11-16 |
| Reviewer | Initial Architecture Owner — formal adoption required |

The recommendation covers:

- compatibility with ADR-0023;
- immutable registration-revision binding;
- direct V16 transaction-consumption provenance;
- provider Adapter isolation;
- separation of exchange, parsing, validation, association, Session and Authorization trust boundaries;
- safe external-effect boundaries;
- distinct failure and uncertainty outcomes;
- preservation of the existing server-side Session Architecture;
- governed provider replacement; and
- no implementation or operational-activation authorization.

The formal Architecture disposition remains **PENDING**. This recommendation does not
approve provider-specific topology, credentials, transport or operational configuration.

## 12. Separate Implementation-Readiness and Activation Gates

| Gate | Minimum evidence required | Current disposition |
| --- | --- | --- |
| Credential-resolution implementation | Accepted method; approved custodian/storage class; workload identity; retrieval Contract; Environment isolation; rotation/revocation/outage policy; audit fields; negative tests; dependency admission if applicable. | **NO-GO** |
| Token-exchange transport implementation | Accepted exact endpoint and network/TLS/proxy policy; credential boundary; content/redirect/limit/retry/failure rules; threat model; deterministic transport tests; independent Engineering and Security acceptance. | **NO-GO** |
| Operational provider configuration | Accepted provider-registration revision; attributable tenant/client configuration evidence; credentials provisioned through the approved path; monitoring/audit/runbooks; staging verification; rollback/disablement readiness. | **NO-GO** |
| Staff Authentication activation | All earlier gates plus approved assurance/freshness mapping, key trust/rotation, positive evidence issuance, association and Session orchestration, callback/login governance, revocation, security testing, deployment approval and operational readiness. | **NO-GO** |

Acceptance of DEC-0046 is necessary but not sufficient for credential-resolution or
token-exchange transport implementation. Each implementation requires separate
readiness evidence, review and explicit implementation authorization under the gates
above. Acceptance would approve only the choices explicitly accepted within this
decision's scope; it would not prove their implementation, configure a provider, issue
a credential, make an HTTP request, create verified Staff evidence or establish a
Session. Operational Staff Authentication activation remains an independent gate after
all prerequisite implementation, assurance, deployment and operational evidence passes.

## 13. Alternatives Considered

### 13.1 Place Provider and Transport Values in ADR-0023

Not proposed. ADR-0023 intentionally establishes stable provider-neutral Architecture
and separate provider, dependency, implementation and activation gates. Environment-
specific credential and transport admission should not destabilize that Accepted ADR.

### 13.2 Treat Dormant Registration Objects as Admission

Rejected. Structural validation and exact revision binding are valuable technical
evidence but cannot prove tenant ownership, provider capability, credential custody,
runtime network trust or accountable acceptance.

### 13.3 Implement Transport First and Govern Configuration Later

Rejected. This would force unresolved endpoint, credential, network, TLS, retry,
failure and audit policy into code and could create an accidental operational authority.

### 13.4 Continue with the Unavailable Operational Verifier

This is the current safe status quo. It preserves default denial but cannot advance
operational Staff Authentication. It remains required until every applicable gate passes.

## 14. Consequences

### 14.1 Benefits

- Makes provider and transport admission reviewable without reopening ADR-0023.
- Prevents dormant technical models from being mistaken for operational trust.
- Establishes one immutable revision as the unit of provider, credential and transport review.
- Assigns accountable decisions across Identity, Security, Architecture,
  Platform/Operations and Backend Engineering.
- Keeps implementation readiness and operational activation independently visible.

### 14.2 Costs and Limitations

- Requires multi-owner evidence before a live token exchange can be built.
- Requires provider- and Environment-specific operational evidence and maintenance.
- Requires revision governance when endpoint, client, credential, trust or transport
  policy changes.
- Does not itself deliver Staff login or remove assurance, revocation, Session or
  administrative-authorization blockers.

### 14.3 Reversibility and Exit

An admitted registration must be disableable without selecting another revision by
fallback. Provider replacement requires a new admitted revision or a later decision,
credential and trust migration, in-flight transaction policy, testing and operational
evidence. Historical revision evidence must remain attributable for audit and incident
analysis according to approved retention policy.

## 15. Security Impact

The decision concerns privileged Staff Authentication, provider credentials, external
network access and token material and is therefore high Risk. A current threat model,
negative-path tests, Secret and key review, SSRF/TLS review, protected audit and
independent Security acceptance are mandatory before operational use.

This proposal creates no Security Exception and waives no Security Requirement.
Unknown mandatory evidence remains blocking.

## 16. Data Impact

No schema, migration, credential, token or operational record is created by this
document. Future implementation must minimize provider and Identity data, avoid durable
storage of authorization codes, PKCE verifiers and tokens unless separately justified,
and govern any security evidence and Audit Record retention.

## 17. Compatibility and Migration Impact

The proposal preserves ADR-0019/ADR-0020 server-side Session authority, ADR-0021
Customer Authentication separation, ADR-0023 Staff OIDC Architecture, existing Staff
associations and the dormant exact-revision boundary. It requires no compatibility or
migration change now. Later provider revisions must define mixed-version, in-flight,
disablement, rollback and historical-retention behavior before rollout.

## 18. Operational Impact

Operational adoption would introduce dependency on an external Identity Provider,
credential/key custody, controlled outbound connectivity, certificate trust, provider
availability, monitoring, audit and incident response. Exact support ownership,
Environments, service expectations, recovery procedures and escalation remain
**PENDING OWNER DECISION**.

## 19. Risks and Open Owner Questions

| Risk or open question | Required accountable answer |
| --- | --- |
| Accidental provider admission | Which exact provider, tenant, issuer, client, Environment, registration ID and revision are admitted, on what attributable evidence? |
| Subject reassignment | What guarantees stable subject meaning, and how are deletion, recreation, transfer and tenant migration handled? |
| Credential disclosure | Who issues, owns, stores, retrieves, rotates, revokes and responds to compromise of the credential/key? |
| Method mismatch | Which one client-authentication method is admitted, and how is unsupported or drifted provider configuration detected? |
| SSRF/DNS rebinding | Which hosts, ports and address classes are allowed, when is DNS revalidated, and who owns policy changes? |
| Proxy/TLS substitution | Is egress direct or proxied, which trust store/CAs apply, and how are proxy/certificate changes reviewed and detected? |
| Redirect expansion | Will Security and Identity accept the proposed categorical redirect prohibition? |
| Code replay after uncertainty | Will owners accept no automatic retry and a fresh Authentication attempt after ambiguous exchange? |
| Resource exhaustion | Which measured timeout and response-size values are safe for the admitted provider and deployment Environment? |
| Failure ambiguity | Which conditions are provider unavailability versus local policy/configuration defects, and what recovery/alert applies? |
| Audit loss | Which exchange and credential events are mandatory, and does audit-sink unavailability block positive Authentication establishment? |
| Registration disablement race | What must happen when a registration is disabled between transaction creation, consumption, exchange and token verification? |
| Key/credential rotation | What overlap, retirement, rollback, refresh and compromise behavior prevents outage or stale trust? |
| Independent acceptance | Who supplies attributable Engineering and Security dispositions, threat-model review and residual-risk acceptance? |

## 20. Proposed Decision and Current Status

**DEC-0046 v0.1.0 remains Proposed and non-authoritative.** It proposes the bounded
admission structure, proposed initial no-redirect and no-automatic-retry policies, and
the failure-classification candidates above for accountable review.

Current operational disposition:

- **No provider admission is granted.**
- **No tenant, issuer, client, endpoint or registration revision is selected.**
- **No client-authentication method is approved.**
- **No credential or key implementation is authorized.**
- **No HTTP token exchange is authorized.**
- **No Staff Authentication, verified Staff evidence or Staff Session activation is authorized.**
- **Administrative and Product Authorization remain unchanged and default-deny.**

## 21. References

- [ADR-0023 — Staff Authentication Authority and Mechanism](../adr/ADR-0023-staff-authentication-authority-and-mechanism.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [Identity Backend Specification](../backend/identity/identity-backend.md)
- [Backend Baseline](../backend/shared/backend-baseline.md)
- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0012 — Administrative Role and Permission Matrix](DEC-0012-administrative-role-permission-matrix.md)

## 22. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.2.0 | 2026-11-16 | Proposed | Recorded provider-neutral Identity and Architecture review recommendations; formal adoption and provider admission remain pending. |
| 0.1.0 | 2026-11-16 | Proposed | Created the bounded Staff OIDC provider, client, credential and token-transport admission proposal; preserved pending owner dispositions, separate implementation/activation gates and default-deny Staff Authentication. |
