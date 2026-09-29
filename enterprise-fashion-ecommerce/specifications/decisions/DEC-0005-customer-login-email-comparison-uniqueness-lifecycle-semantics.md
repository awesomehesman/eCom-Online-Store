# DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics

- **Identifier:** DEC-0005
- **Title:** Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics
- **Type:** Product Decision / Security Decision / Data Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-09-29
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** Not applicable — this is the first governed Customer login-email comparison, uniqueness, and lifecycle decision.
- **Superseded By:** Not applicable — this Proposed Decision Record has not been superseded.

## Context

Accepted DEC-0004 establishes email address as the single initial Customer-facing login identifier for local Customer Authentication. It requires deterministic comparison semantics before lookup or uniqueness enforcement but intentionally leaves normalization, case comparison, logical uniqueness, conflict, establishment, change, reassignment, reuse, persistence enforcement, compatibility, and migration unresolved.

Without one governed equality model, registration could accept identifiers that login cannot match, concurrent work could establish competing ownership, persistence implementations could enforce different uniqueness rules, and support or migration activity could merge or reassign Identity incorrectly. Those outcomes would conflict with the Approved Identity Domain, BIDN, Customer Domain, BCUS, DEC-0004, and repository security requirements.

This proposal selects the logical semantics needed for future lookup, duplicate detection, uniqueness enforcement, identifier establishment and change, conflict handling, migration, persistence design, and API Contract design. It does not authorize an API, persistence model, migration, dependency, or implementation.

## Decision

If Accepted, Customer login-email equality SHALL be determined only by a deterministic canonical comparison representation produced under the rules in this record. The Customer-facing representation MAY be retained separately for permitted display and communication purposes, but it SHALL NOT determine Authentication lookup equality.

For the initial Customer Authentication model, the canonical comparison representation SHALL consist of:

1. an initial ASCII-only local part validated as an unquoted dot-atom;
2. the local part converted with locale-independent ASCII lowercase mapping;
3. a domain split into non-empty labels only on ASCII full stop (`U+002E`), with each label validated and converted under the strict per-label IDNA2008 profile defined below to its canonical ASCII label representation;
4. the domain converted with locale-independent ASCII lowercase mapping; and
5. the resulting canonical local part and domain joined as one comparison value.

Provider-specific mailbox rewriting SHALL NOT participate in canonical comparison. Logical uniqueness SHALL apply to the canonical comparison representation across retained Identity-owned Customer login identifiers. Two inputs producing the same comparison representation SHALL NOT establish separate active login ownership.

## Input Boundary

Before validation and comparison:

- surrounding ASCII space (`U+0020`) and horizontal tab (`U+0009`) SHALL be removed;
- an empty or blank result SHALL be invalid;
- carriage return, line feed, control characters, interior whitespace, and unpaired or malformed Unicode input SHALL be invalid;
- ASCII full stop (`U+002E`) SHALL be the only accepted domain-label separator; `U+3002` IDEOGRAPHIC FULL STOP, `U+FF0E` FULLWIDTH FULL STOP, `U+FF61` HALFWIDTH IDEOGRAPHIC FULL STOP, and every other separator-like character SHALL be rejected rather than mapped;
- a leading domain separator, consecutive domain separators, an empty interior domain label, and a trailing separator or root-label notation SHALL be invalid;
- comments, display-name syntax, address lists, domain literals, quoted local parts, and multiple-address input SHALL be invalid for the initial login identifier; and
- normalization SHALL precede equality comparison.

The initial local part SHALL use the ASCII `atext` character repertoire defined by RFC 5322, with dots permitted only between non-empty segments. A leading, trailing, or consecutive dot is invalid. Quoted and internationalized or other non-ASCII local parts remain intentionally outside the initial Customer login-identifier scope rather than being transformed or approximated. This restriction does not claim that email standards universally prohibit those forms. Supporting them later requires explicit Product and compatibility governance, a deterministic comparison and migration model, and preserved ownership and collision safety.

These are logical input semantics. No API field, request schema, response, error text, or validation library is selected.

## Case and Domain Semantics

The initial local part and canonical ASCII domain are compared case-insensitively through locale-independent ASCII lowercase mapping. Equality MUST NOT depend on a process locale, database collation, filesystem behavior, or provider behavior.

The domain consists of one or more non-empty labels separated in submitted input only by ASCII full stop (`U+002E`). IDNA2008 U-label and A-label terminology applies to each label, not to the domain as a whole. Each label SHALL be processed independently under a strict IDNA2008 profile, and the canonical comparison domain SHALL be the resulting sequence of validated canonical ASCII labels joined by `U+002E`.

An accepted Unicode label SHALL already satisfy the Unicode normalization and validity requirements of strict IDNA2008, including the required normalized form, and SHALL be converted deterministically to its corresponding A-label. UTS #46 transitional or non-transitional mapping, compatibility mapping, provider mapping, runtime-specific normalization, and any other preprocessing that changes logical equality are prohibited. Input requiring such additional mapping is invalid rather than silently rewritten.

A submitted `xn--` label SHALL be accepted only when it is a valid A-label under the same strict IDNA2008 profile, decodes to a valid corresponding U-label, and re-encodes under that profile to the same A-label after locale-independent ASCII lowercase canonicalization. An arbitrary ASCII label beginning with `xn--`, a malformed A-label, or a non-canonical round trip is invalid. An ordinary ASCII label that is not an A-label SHALL satisfy the applicable strict IDNA2008 ASCII-label validity rules and SHALL be canonicalized using locale-independent ASCII lowercase mapping.

If any label cannot be validated, converted, or round-tripped deterministically under this profile, canonical comparison fails and uniqueness remains unresolved. Registration, login lookup, identifier establishment, and identifier change SHALL NOT claim success from that input. Database collation, locale, provider behavior, DNS resolver behavior, filesystem behavior, and library-specific optional mapping modes SHALL NOT alter logical equality.

This decision selects no third-party IDNA or email-validation library and does not authorize environment-specific or locale-sensitive conversion.

## Provider-Specific Rewriting

Dot removal, plus-address removal, alias rewriting, mailbox-provider rules, and other provider-specific canonicalization SHALL NOT be applied to logical login-email comparison.

Such transformations are not universal email semantics, can change over time, can vary across providers or managed domains, and can collapse distinct Customer-controlled addresses. Any future provider-specific behavior requires separate governance, compatibility analysis, and migration evidence.

## Comparison and Customer-Facing Representations

The canonical comparison representation exists only to determine logical equality, lookup, collision, and uniqueness. It is not proof of mailbox control, Identity, Authentication, Authorization, Customer or Account ownership, verified-email status, or recovery authority.

A validated Customer-facing or originally submitted representation MAY be retained separately where an Approved purpose permits it. That representation remains PII, must be protected and purpose-limited, and MUST NOT override the canonical comparison result. Display preservation does not require preserving unsafe whitespace, controls, invalid syntax, or unbounded raw input.

No storage field, persistence shape, retention period, or source-of-truth record is selected.

## Logical Uniqueness

At most one retained Identity-owned Customer login-identifier association may own a canonical comparison representation at a time. Registration, identifier establishment, or identifier change MUST NOT create separate active login ownership for canonically equal input.

Logical uniqueness is an Identity-owned invariant. Later persistence MUST enforce it consistently and concurrency-safely, but this record selects no database constraint, index, collation, lock, isolation level, transaction technique, schema, or SQL.

## Duplicate, Collision, and Conflict Semantics

When registration submits an email canonically equal to an existing retained login identifier, the system SHALL NOT establish another login identifier, Identity, credential, Customer, or Account from that submission. The externally visible outcome must remain enumeration-resistant and must not disclose whether the collision arose from an existing Identity, retained history, concurrent work, or another protected condition.

An identifier change to a canonical email already associated with another retained Identity SHALL NOT transfer or duplicate ownership. Discovery of multiple authoritative records with the same canonical representation is a security and data-integrity conflict: the records SHALL NOT be automatically merged, reassigned, selected by arbitrary ordering, or treated as safe Authentication evidence.

Where authoritative uniqueness cannot be established, registration, change, lookup, and Authentication SHALL fail safely without representing success. Resolution requires governed investigation and reconciliation with sufficient historical and audit evidence.

## Establishment

Initial establishment requires a valid canonical comparison representation, confirmation that no conflicting retained Identity-owned association exists, and an authorized registration or Identity workflow governed separately.

Submission, syntactic validity, canonicalization, uniqueness, or lookup match does not prove mailbox control, Identity, Authentication, Authorization, Customer ownership, Account ownership, verified-email status, or recovery authority. Product Decision 5 remains unresolved.

## Identifier Change

An accepted login-email change must be authorized under later governed change semantics, preserve the same stable Identity and applicable Customer/Account association, establish the replacement canonical representation without collision, preserve required history, and leave no ambiguous concurrent ownership.

A submitted change that produces the existing canonical comparison representation does not change logical identifier ownership, although any permitted Customer-facing representation update remains authorized and governed separately.

A login-email change SHALL NOT create a replacement Identity, Customer, Account, Principal, Session, or password credential merely because the identifier changes. This proposal selects no change API, requester-verification workflow, mailbox-verification policy, notification behavior, Session consequence, recovery consequence, or support procedure.

## Reassignment and Reuse

A canonical login email SHALL NOT be reassigned to or reused by another Identity while authoritative retained Identity state still associates that canonical representation with the prior Identity. Disablement, loss of access, credential invalidation, profile change, or Account closure alone does not prove that reassignment is safe.

Final reuse after governed deletion, anonymization, or expiration of required retention cannot be decided safely until Product Decision 28 and applicable privacy, legal, security, audit, and retention governance establish what Identity and Customer evidence remains. This decision therefore neither permits permanent reuse prohibition after final deletion nor authorizes reuse after closure or deletion.

## Concurrency

Concurrent registration or identifier-change attempts for the same canonical comparison representation MUST converge on at most one accepted owning Identity association. Every competing attempt must observe an accepted duplicate, conflict, failure, or uncertainty outcome rather than create duplicate active ownership or falsely report success.

This invariant applies regardless of implementation technique. No locking strategy, isolation level, transaction boundary, index, or retry count is selected.

## Failure and Uncertainty

Invalid input, failed canonicalization, unavailable authoritative lookup, uncertain uniqueness, conflicting evidence, partial work, timeout, or inability to produce the governed comparison representation SHALL NOT establish, change, reassign, authenticate, or recover an Identity or Customer login identifier.

Failure and uncertainty must remain distinguishable internally where handling, investigation, or recovery differs, while external disclosure remains safe and enumeration-resistant. Retry, repair, or reconciliation must preserve the original intent, prior accepted ownership, and latest authoritative truth without multiplying effects.

No timeout, retry, escalation, or numerical operational policy is selected.

## Enumeration, Privacy, and Disclosure

Registration, login, identifier change, support, and administrative behavior MUST avoid exposing protected Account or Identity existence through content, timing, status variation, logging, telemetry, analytics, or support tooling. No response payload, HTTP status, timing equalization mechanism, or numerical abuse control is selected.

Login email is PII. Raw invalid input and unnecessary Customer-facing or canonical representations MUST NOT appear in URLs, unsafe errors, Logs, Metrics, Traces, events, analytics, screenshots, fixtures, exports, or ordinary support evidence. Authorized support or administrative visibility must remain purpose-limited, least-privileged, auditable, and unable to transfer identifier ownership.

## Authority and Ownership Boundary

Identity owns:

- Authentication interpretation of Customer login email;
- canonical comparison semantics for Authentication lookup;
- logical login-identifier uniqueness and conflict truth;
- identifier establishment and security lifecycle within Identity;
- authoritative Authentication lookup behavior; and
- Principal establishment only from separately accepted Authentication evidence.

Customer retains Customer and Account business identity, profile, contact information, lifecycle, and other Customer-owned truth. The same textual email appearing in Customer profile or contact data does not make that data an Identity-owned login identifier, authoritative Authentication input, or proof of association. Identity must not rewrite Customer or Account truth during identifier establishment, comparison, change, conflict handling, or reconciliation.

This proposal preserves existing authority boundaries and does not select Identity-to-Customer/Account cardinality, coordination, persistence, or mapping.

## Compatibility with Existing Decisions

### ADR-0019

Comparison and uniqueness do not alter the authoritative server-side Session, browser-cookie custody, CSRF, fixation, renewal, logout, revocation, failure-closed, or browser bearer-token boundaries.

### ADR-0020

The login-email representation is not Session state. No Spring Session, PostgreSQL Session, Redis, or Session-persistence change is introduced.

### ADR-0021

Identity-owned local Customer Authentication authority remains unchanged. Canonical equality and lookup cannot establish a Principal without accepted Identity-owned Authentication evidence.

### DEC-0003

The password-based local Customer credential remains the single initial credential category. Email comparison does not become credential verification or select password security configuration.

### DEC-0004

Email remains the single initial Customer-facing login identifier and untrusted lookup input. Product Decision 5 remains unresolved, and no alternative initial identifier category is introduced.

### Product Decision 28

Customer data export, correction, deletion, and Account-closure workflow remains unresolved. This proposal preserves that boundary and does not select deletion, anonymization, retention, closure, reactivation, or final post-deletion reuse policy.

## Alternatives Considered

### A. Governed Canonical Comparison with ASCII Case-Insensitive Local Part and IDNA2008 Domain — Selected in This Proposal

This approach gives registration, login, change, migration, and persistence one deterministic equality model independent of locale and database collation. Restricting the initial local part to ASCII avoids silently transforming internationalized mailbox identifiers without a governed SMTPUTF8 compatibility model. Its costs are rejection of otherwise deliverable quoted or internationalized local parts and future migration work if those forms are introduced.

### B. Exact Raw-String Equality — Not Selected

Raw equality is simple and preserves every submitted distinction. It also makes leading or trailing input artifacts, domain case, Unicode domain presentation, and equivalent A-label/U-label forms produce inconsistent lookup and duplicate behavior. It would not provide the deterministic normalized equality required by DEC-0004.

### C. Domain-Insensitive, Local-Part-Sensitive Comparison — Not Selected

Preserving local-part case follows the theoretical possibility that a mail system distinguishes case. It can also create Customer confusion, duplicate registrations, and ambiguous support outcomes because most Customer-facing usage does not treat ASCII case variants as distinct accounts. It remains a legitimate standards-conservative option but is not selected for the initial platform model.

### D. Fully Case-Insensitive Comparison — Partially Selected with Constraints

Case-insensitive comparison improves predictable Customer login and duplicate detection. Applying unrestricted locale-sensitive or Unicode case conversion would be unstable, so the proposal applies only locale-independent ASCII lowercase mapping to the initially supported ASCII local part and canonical ASCII domain.

### E. Provider-Specific Canonicalization — Not Selected

Provider rules such as dot or plus-tag removal may identify aliases for some mailboxes and reduce some accidental duplicates. They are not universal, may vary by provider or managed domain, and can collapse distinct addresses. They remain possible only through later provider-specific governance and migration evidence.

### F. Multiple Active Identities for One Canonical Email — Not Selected

Allowing multiple ownership could support shared mailboxes or unusual Customer arrangements. It would make single-value login ambiguous, increase enumeration and takeover Risk, and require an additional discriminator not selected by DEC-0004. Shared contact information does not require shared Authentication ownership.

### G. Separate Normalization and Uniqueness Decisions — Not Selected

Separate records could reduce individual decision size. Equality determines collision and uniqueness, while lifecycle and migration depend on that same result. Splitting them would allow incompatible intermediate authority and would not close DEC-0004's implementation gate coherently.

## Consequences

### Positive and Enabled Outcomes

- Registration, login, change, persistence design, and migration can use one deterministic equality model.
- Logical equality is independent of database collation, runtime locale, and mailbox-provider behavior.
- Equivalent valid domain representations converge through one canonical ASCII-compatible form.
- Duplicate active ownership is prohibited and concurrent work has one required logical outcome.
- Customer-facing presentation can remain separate from security-sensitive comparison.
- Stable Identity and Customer authority remain independent of mutable login email.

### Negative and Trade-offs

- Internationalized and quoted local parts are unsupported initially even where an email system might accept them.
- ASCII local-part case distinctions are intentionally collapsed for the Customer Authentication use case.
- IDNA2008 conversion, collision detection, safe migration, and reconciliation add delivery and operational work.
- Retaining a Customer-facing representation alongside a comparison representation increases privacy, consistency, and support obligations.
- Login and registration implementation remains blocked by password-security, persistence, association, Contract, dependency, and implementation governance.
- Reuse after final deletion remains blocked until Product Decision 28 and applicable retention authority are resolved.

### Security, Privacy, Data, and Operations

The decision reduces ambiguity and duplicate-ownership Risk but cannot eliminate phishing, enumeration, stuffing, reassignment, compromised mailbox, or support-manipulation Risk. Both representations remain PII and require minimization, least privilege, safe observability, incident handling, auditability, and controlled support access. Conflicts require explicit reconciliation rather than automated merging or reassignment.

### Compatibility, Migration, Support, and Reversibility

Existing data must be evaluated under the canonical model before enforcement. Support procedures must distinguish safe external disclosure from authorized internal investigation. Reversing or extending the model requires governed collision analysis, data migration, Contract compatibility, rollback behavior, and prevention of duplicate or reassigned Identity. Exit cost increases after persisted identifiers and public Contracts depend on the model.

These consequences do not resolve Product Decisions 5 or 28, make verified email mandatory, make email possession Authentication or recovery evidence, or authorize persistence, API Contracts, dependencies, or implementation.

## Migration and Compatibility

Before the governed comparison model is enforced against existing identifier data, migration analysis MUST derive comparison representations, identify invalid or ambiguous records, and detect collisions. No colliding records may be silently merged, deleted, reassigned, or selected as authoritative by ordering or convenience.

Each conflict requires explicit authorized disposition, preserved provenance, safe Customer and Identity handling, and sufficient Audit Records. Migration, dual-read, rollback, or repair behavior must not create duplicate ownership, restore withdrawn access, authenticate from uncertain evidence, or rewrite Customer business truth.

A rollback must preserve the last accepted ownership and conflict evidence and must not return to an equality model that makes accepted identifiers ambiguous. No migration SQL, rollout sequence, compatibility window, or remediation workflow is selected.

## Operational Impact

Later implementation must monitor canonicalization failure, collision discovery, duplicate attempts, conflict and reconciliation backlog, suspicious change or reassignment activity, and unexpected comparison drift without exposing raw PII or protected existence. Support and administrative tools require least privilege, purpose limitation, auditability, and safe disclosure.

This proposal selects no alert threshold, service level, retry count, timeout, support procedure, provider, infrastructure, or production escalation policy.

## Downstream Non-Decisions

DEC-0005 does not select, define, admit, or authorize:

- Product Decision 5 or Product Decision 28;
- verified-email policy or mailbox-control proof;
- password hashing algorithm, configuration, facility, parameters, or upgrade strategy;
- password length, composition, history, rotation, or compromised-value implementation;
- credential, identifier, Identity, Customer, Account, or association persistence schema;
- Identity-to-Customer or Account association cardinality, coordination, or mapping;
- registration, login, change, verification, recovery, or MFA Contract;
- recovery mechanism, token, channel, provider, expiry, or workflow;
- Customer or privileged MFA mechanism, factor, provider, protocol, or fallback;
- Session TTL, cookie configuration, or Session persistence change;
- external Identity Provider, email provider, SDK, protocol, or infrastructure;
- database schema, table, column, index, constraint, collation, SQL, lock, isolation level, or transaction implementation;
- ORM, JDBC, Spring Security, validation, IDNA, email-parsing, or other framework component or library;
- Flyway migration content;
- API route, HTTP method, status, DTO, field name, response body, or error text;
- dependency admission, implementation class, executable implementation, or completed testing; or
- retry count, timeout, rate limit, lockout threshold, service level, or other numerical operational policy.

## Acceptance and Validation Criteria

Before DEC-0005 may become Accepted, review must verify:

1. metadata remains `0.1.0 Proposed`, Type `Product Decision / Security Decision / Data Decision`, owner `Identity`, and `authoritative: false`;
2. comparison is deterministic and independent of database collation, process locale, and provider behavior;
3. input whitespace, empty, invalid, control, Unicode, local-part, and domain boundaries are explicit without defining an API schema;
4. local-part and domain case semantics are explicit and locale-independent;
5. IDNA2008 domain conversion and the initial non-ASCII local-part exclusion are explicit;
6. provider-specific dot, plus-tag, alias, and mailbox rewriting is excluded from canonical comparison;
7. canonical comparison and Customer-facing representations have distinct meanings, with canonical comparison governing equality;
8. logical uniqueness permits at most one retained owning Identity association for one canonical comparison representation;
9. duplicate registration, identifier-change collision, conflicting records, and uncertain uniqueness fail safely without enumeration or automatic merge or reassignment;
10. establishment does not make email submission, validity, uniqueness, or matching proof of mailbox control, Identity, Authentication, Authorization, ownership, verification, or recovery;
11. identifier change preserves stable Identity, Customer/Account truth, credentials, Principal, and Session boundaries;
12. retained Identity association prevents reassignment or reuse while final post-deletion behavior remains governed by unresolved Product Decision 28;
13. concurrent establishment or change converges on at most one accepted owning association without selecting an enforcement mechanism;
14. canonicalization, lookup, uniqueness, conflict, dependency, and partial-work uncertainty fail safely without false Authentication or registration success;
15. enumeration, timing, logs, telemetry, support, administration, privacy, PII, and audit boundaries remain intact;
16. migration detects invalid data and collisions before enforcement and cannot silently merge, delete, reassign, or choose Identity or Customer records;
17. ADR-0019, ADR-0020, ADR-0021, DEC-0003, and DEC-0004 remain compatible and unchanged;
18. Product Decisions 5 and 28 remain unresolved;
19. no persistence object, migration, API Contract, framework component, dependency, provider, infrastructure, numerical control, or implementation is authorized;
20. downstream password-security, persistence, association, Contract, dependency, recovery, MFA, and implementation work remains separately gated;
21. alternatives and consequences remain fair and complete;
22. promotion to Accepted has durable review and approval evidence representing Product authority, Security authority, Architecture authority, Identity ownership, Customer ownership, and applicable Data/Privacy authority; Identity remains the single accountable owner, one accountable owner does not remove cross-authority review, and pull-request approval counts only when those authorities are represented by durable evidence;
23. while the approval gate remains unsatisfied, DEC-0005 remains Proposed and non-authoritative; and
24. proposal registration affects only DEC-0005 and `DECISIONS.md`, registers DEC-0005 exactly once as Proposed, passes whitespace and diff validation, and introduces no unrelated tracked changes.

## Proposal Registration and Acceptance Planning

Proposal-stage registration changes only DEC-0005 and `DECISIONS.md`. If later Accepted, DEC-0005 and its existing Decision Index status and history require synchronization. Acceptance-readiness review must determine whether the selected Product, Security, and Data semantics directly require synchronization of `PRODUCT.md`, `SECURITY-STANDARDS.md`, `ARCHITECTURE.md`, backend standards, Domains, or Specifications; this proposal makes no acceptance-stage change and does not presume that final set.

Promotion from Proposed to Accepted requires durable review and approval evidence representing Product authority, Security authority, Architecture authority, Identity ownership, Customer ownership, and applicable Data/Privacy authority. Identity remains the single accountable owner; that ownership does not eliminate required cross-authority review. Pull-request approval is sufficient only when the required authorities are actually represented and the evidence is durable and discoverable under `DECISIONS.md`. Until this gate is satisfied and the record is promoted through governance, DEC-0005 remains Proposed and non-authoritative.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [SPRING.md](../../.ai/backend/SPRING.md)
- [API.md](../../.ai/backend/API.md)
- [DATABASE.md](../../.ai/backend/DATABASE.md)
- [POSTGRES.md](../../.ai/backend/POSTGRES.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [ADR-0017 — PostgreSQL Schema Strategy](../adr/ADR-0017-postgresql-schema-strategy.md)
- [ADR-0018 — Persistence Technology](../adr/ADR-0018-persistence-technology.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](./DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0002 — PostgreSQL Release Baseline](./DEC-0002-postgresql-release-baseline.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](./DEC-0003-initial-local-customer-credential-mechanism.md)
- [DEC-0004 — Customer Login Identifier Semantics](./DEC-0004-customer-login-identifier-semantics.md)
- [RFC 5322 — Internet Message Format](https://www.rfc-editor.org/rfc/rfc5322)
- [RFC 5890 — Internationalized Domain Names for Applications: Definitions and Document Framework](https://www.rfc-editor.org/rfc/rfc5890)
- [RFC 5891 — Internationalized Domain Names in Applications: Protocol](https://www.rfc-editor.org/rfc/rfc5891)

## Supersedes

None.

## Superseded By

None.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-09-29 | Proposed | Proposed deterministic Customer login-email comparison, logical uniqueness, collision, establishment, change, reuse, concurrency, failure, and migration semantics while preserving Product Decisions 5 and 28 and excluding persistence, Contracts, dependencies, and implementation. |
