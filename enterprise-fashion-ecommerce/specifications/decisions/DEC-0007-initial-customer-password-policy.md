# DEC-0007 — Initial Customer Password Policy

- **Identifier:** DEC-0007
- **Title:** Initial Customer Password Policy
- **Type:** Product Decision / Security Decision
- **Status:** Accepted
- **Version:** 1.0.0
- **Date:** 2026-09-30
- **Owner:** Identity
- **Authoritative:** true
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted DEC-0003 establishes a password-based local Customer credential as the single initial credential category. Accepted DEC-0006 establishes the Identity-owned Argon2id hashing and verification strategy and the `identity-customer-argon2id-v1` profile. Neither decision establishes the Customer-facing policy that determines whether a prospective password is acceptable when a credential is first established or later changed.

`SECURITY-STANDARDS.md` requires passwords to be checked against policy and known-compromised values, carried only over protected channels, and stored only through an approved adaptive password-hashing facility. It prohibits plaintext storage, reversible encryption, security questions as primary protection, and arbitrary periodic rotation. The Approved Identity and Customer Specifications preserve those requirements while deliberately leaving concrete password policy unresolved.

The initial Customer path currently has no mandatory ordinary-Customer MFA factor. Current NIST SP 800-63B guidance treats a password used as a single Authentication factor as requiring at least 15 characters, requires support for passwords of at least 64 characters, rejects blocklisted commonly used or compromised values, avoids composition rules and arbitrary periodic changes, and supports password managers, paste, Unicode, and spaces. OWASP's current Authentication guidance reflects the same baseline and recognizes that a reasonable upper bound may protect against denial-of-service. Neither source requires rejection above 64 characters or establishes 64 as the recommended exact maximum.

This Accepted decision establishes the initial Customer password acceptance policy needed before final registration Contract governance. It does not admit a compromised-password provider, dependency, public Contract, persistence design, recovery mechanism, Customer MFA mechanism, or implementation.

## Verified Governing Constraints

1. Identity owns Customer credential establishment, verification, change, compromise, and invalidation outcomes.
2. Customer owns Customer and Account business truth; password acceptance does not establish Customer ownership or Authorization.
3. DEC-0006 remains authoritative for hashing, salts, verifier representation, verification, profile compatibility, and rehash behavior.
4. Prospective passwords must be checked against an approved policy and known-compromised values.
5. Passwords must be protected in transit and must never be persisted, logged, emitted, cached, exported, or exposed in plaintext.
6. Arbitrary periodic password rotation is prohibited as primary protection.
7. Product Decision 5 remains unresolved and email verification is not password evidence.
8. Password acceptance policy, password storage, Authentication verification, abuse controls, recovery, and MFA remain distinct concerns.

## Decision

The following policy governs initial Customer password establishment and every later supported Customer password establishment or change operation.

### Length and Bounded Input

A prospective Customer password SHALL contain at least **15 Unicode code points** and at most **64 Unicode code points** after the normalization defined below. The complete normalized password SHALL be evaluated and hashed; it MUST NOT be silently truncated.

The 15-code-point minimum follows the current NIST single-factor password baseline because ordinary Customer MFA is not currently mandatory. External guidance requires support for at least 64 characters but does not require rejection above 64 or prescribe 64 as the exact maximum. DEC-0007 therefore selects exactly 64 code points as the repository's initial Product/Security upper-bound choice: it satisfies the externally supported minimum capacity, supports passphrases and password managers, and supplies deterministic bounded input for resource protection. Controlled acceptance explicitly approved that repository-specific maximum rather than attributing it to NIST or OWASP. A future change to ordinary Customer MFA does not silently reduce the minimum; changing either bound requires governed compatibility and Product/Security review.

### Character and Normalization Semantics

Customer passwords MAY contain Unicode characters, including spaces and other whitespace. The complete submitted value SHALL be normalized with Unicode Normalization Form Canonical Composition (`NFC`) before code-point counting, blocklist comparison, and DEC-0006 hashing.

Whitespace is significant. Leading, trailing, repeated, or internal whitespace MUST NOT be silently trimmed, collapsed, inserted, removed, or substituted. Case is significant and MUST NOT be silently changed. Malformed text input MUST be rejected safely rather than repaired into another password.

The policy imposes no required mixture of uppercase letters, lowercase letters, digits, symbols, or other character classes. It does not prohibit a character merely because it is punctuation, whitespace, non-ASCII, or difficult for an implementation to escape. Implementations MUST preserve the same accepted normalized value across establishment and verification.

### Known-Compromised and Blocklisted Passwords

Every prospective password establishment or change SHALL compare the complete normalized password against a governed blocklist of known commonly used, expected, or compromised passwords. Substring presence alone is not a blocklist match.

A matching password SHALL be rejected and the Customer SHALL be required to choose a different password. The response SHALL provide safe, actionable reason guidance without echoing the password, exposing the blocklist, disclosing other Account or Identity state, or providing an oracle useful for unrelated Authentication attempts.

If the required blocklist decision is unavailable, failed, stale beyond its later governed operating conditions, or uncertain, password establishment or change MUST fail closed and MUST NOT create or replace a credential. The attempt outcome must remain distinguishable and safely retryable. This decision selects no blocklist source, provider, dataset, API, SDK, dependency, cache, update mechanism, availability target, retry count, or timeout.

### Reuse, History, and Rotation

The initial policy establishes no password-history list and no general prohibition on reuse of an earlier password beyond the current compromised-password and accepted-security checks. It does not authorize storage of prior raw passwords or additional verifier history.

Customers SHALL NOT be required to change passwords periodically or on an arbitrary schedule. Password change or replacement remains required when requested through an authorized supported operation or when accepted evidence of compromise, unsafe verifier state, or superseding Security authority requires it. Exact compromise-response workflow, forced-change Contract, Session invalidation behavior beyond existing authority, and operational timing remain separately governed.

### Safe Validation and Disclosure

Password policy validation SHALL occur within the Identity-owned credential-establishment boundary before a new verifier is accepted. A rejected, failed, or uncertain policy check MUST NOT establish or replace a credential or be represented as successful registration or password change.

Validation output may identify the applicable policy category, such as length or blocklist rejection, only to the extent safe for the current authorized operation. Raw passwords, substrings, normalized values, blocklist lookup material, verifier material, and Secrets MUST NOT enter URLs, logs, metrics, traces, analytics, events, support evidence, ordinary error payloads, or Customer/Account state.

Password acceptance does not prove Identity, Authentication, Principal establishment, Customer or Account ownership, mailbox control, email verification, recovery authority, contextual Authorization, or registration completion.

### Password Managers and Accessibility

The policy SHALL permit password-manager-generated passwords, autofill, and paste. It SHALL support passphrases and the accepted Unicode and whitespace semantics without imposing character-class rules that undermine assistive technology or password-manager use.

Applicable interfaces must communicate the requirements and rejection reasons clearly, preserve keyboard and assistive-technology operation, and avoid blocking paste or autofill. This decision establishes policy and compatibility outcomes only; it selects no frontend component, strength-meter library, visual design, interaction implementation, or accessibility certification.

### Registration and Password Establishment

Customer registration may accept a password outcome only after the proposed password satisfies this policy and Identity accepts credential establishment under DEC-0003 and DEC-0006. Policy acceptance is one required Identity-owned outcome; it does not by itself complete registration, create Customer or Account truth, establish the ADR-0022 association, authenticate a Principal, or establish a Session.

Product Decision 5 remains authoritative for whether email verification is required and whether verification affects registration, login, Session establishment, or Account access.

## Alternatives Considered

### A. Evidence-Informed Length, Blocklist, and Usability Policy — Selected

Use a 15-to-64 Unicode-code-point range after NFC normalization, no composition rules, whole-password blocklist checking, significant whitespace and case, no arbitrary periodic rotation, and password-manager-compatible input.

The 15-code-point minimum, support through 64 code points, blocklist behavior, and usability direction align with current NIST and OWASP guidance. Selecting exactly 64 as the rejection boundary is DEC-0007's own accepted Product/Security policy choice for deterministic bounded input and resource protection; it is not an externally prescribed exact maximum. The alternative preserves repository Security authority and deterministic behavior, with costs including Unicode consistency requirements, a mandatory blocklist decision boundary, denial of credential establishment when that boundary is unavailable, rejection above the repository-selected maximum, and future migration work if policy changes.

### B. Traditional Composition Rules and Shorter Minimum — Not Selected

A shorter minimum combined with mandatory uppercase, lowercase, digit, and symbol classes is familiar and easy to describe. It is not selected because current guidance favors length and compromised-password screening, composition rules can produce predictable transformations and usability burden, and ordinary Customer MFA is not mandatory.

### C. Arbitrary Periodic Rotation and Password History — Not Selected

Periodic rotation and retained history can appear to limit reuse. They are not selected because repository Security authority prohibits arbitrary periodic rotation, current guidance discourages it without compromise evidence, and verifier history adds Sensitive Data, persistence, migration, and operational complexity not justified for the initial policy.

### D. Provider-Defined Password Policy — Rejected

Delegating policy to a framework, external provider, blocklist service, or frontend would reduce repository control. It is rejected because Identity must own deterministic policy semantics, provider availability is not policy authority, and dependencies or defaults must not silently redefine Customer behavior.

## Consequences

### Benefits and Enabled Outcomes

- Final Customer registration Contract governance can rely on deterministic password-establishment semantics.
- Passphrases, Unicode, spaces, password managers, autofill, and paste remain supported.
- Known commonly used or compromised passwords are rejected as required by Security authority.
- Composition-rule and periodic-rotation burden is avoided.
- Password acceptance remains distinct from hashing, Authentication, recovery, MFA, email verification, and Customer/Account authority.

### Costs, Risks, and Limitations

- Blocklist checking becomes a mandatory fail-closed dependency of password establishment and change, although its mechanism remains unresolved.
- Unicode normalization must be identical across establishment, verification, migration, and compatibility paths.
- The repository-selected 64-code-point maximum rejects longer passwords even though external guidance does not require rejection above 64; controlled acceptance explicitly approved this trade-off, and the bound must be communicated before submission without silent truncation.
- Absence of password history permits reuse of a prior password when it otherwise satisfies current policy.
- Policy changes after credentials exist require compatibility analysis and may require authorized password replacement rather than reinterpretation.
- Password policy cannot prevent phishing, credential stuffing, endpoint compromise, social engineering, or reuse across unrelated systems.

## Security Impact

The decision strengthens resistance to guessing by combining an evidence-backed minimum length with mandatory whole-password blocklist rejection and DEC-0006 adaptive hashing. Default denial applies to invalid, blocked, unavailable, failed, or uncertain establishment checks. It creates no Authentication success, Session, Authorization, recovery, or MFA evidence.

Raw passwords remain Secrets. Policy evaluation must be purpose-limited, least-privileged, transient, protected in transit and memory as applicable, and absent from observability and support artifacts. Abuse controls remain required by `SECURITY-STANDARDS.md` but their numerical configuration is not selected here.

## Data and Privacy Impact

No new Customer profile field, Account field, analytics attribute, event payload, or retained raw-password data is authorized. Blocklist evaluation and policy telemetry must minimize data and must not disclose or retain the submitted password. Any future external compromised-password service requires separate provider, privacy, security, dependency, and network governance.

## Compatibility and Migration Impact

No production Customer password population currently requires migration. Passwords already accepted under an earlier policy, if introduced later, MUST NOT be silently reinterpreted, normalized differently, truncated, or rejected during Authentication solely because establishment policy changed.

Future policy changes require versioned authority, compatibility analysis, safe establishment/change behavior, Customer communication where applicable, rollback or recovery, and preservation of DEC-0006 verifier semantics. A policy change may require authorized password replacement, but it MUST NOT recover raw passwords or weaken existing Authentication evidence.

## Operational Impact

Operations must distinguish length rejection, blocklist rejection, malformed input, unavailable checking, failure, and uncertainty without observing password content. Monitoring and support guidance must preserve concealment and avoid creating an Account-existence or password-quality oracle. No provider, service level, alert threshold, retry policy, timeout, cache, dataset refresh interval, or escalation process is selected.

## Product Impact

The initial Customer experience supports passphrases, password managers, autofill, paste, Unicode, and spaces without composition rules or arbitrary periodic changes. Requirements and safe rejection guidance must be understandable and accessible.

This decision does not resolve Product Decision 5, require verified email, establish registration completion by itself, decide guest checkout, select Customer recovery or MFA, or create Customer/Account business truth.

## Explicit Non-Decisions

DEC-0007 does not select, define, admit, or authorize:

- Argon2id, `identity-customer-argon2id-v1`, `m=65536`, `t=3`, `p=4`, its 16-byte salt, 32-byte output, verifier representation, hashing, verification, or rehash mechanics beyond preserving DEC-0006;
- a blocklist or breached-password provider, external API, SDK, dependency, dataset, network architecture, cache, update mechanism, retry value, timeout, SLO, or availability target;
- public or internal route, HTTP method, status, DTO, field, error schema, event name, payload, or Contract implementation;
- table, column, key, index, constraint, repository, framework class, persistence mapping, or Flyway migration;
- password recovery, recovery channel, factor, token, provider, expiry, or support workflow;
- ordinary Customer MFA policy, factor, provider, fallback, protocol, or recovery;
- Product Decision 5 or email-verification policy, workflow, registration gate, login gate, or Session gate;
- login-email comparison, uniqueness, change, reuse, or recovery beyond DEC-0004 and DEC-0005;
- lockout threshold, rate limit, retry count, timeout, SLO, capacity target, fraud policy, or escalation workflow;
- Session storage, cookie, lifetime, token, or revocation design beyond ADR-0019 and ADR-0020;
- Identity-to-Customer/Account association, cardinality, persistence, or registration coordination beyond ADR-0022;
- provider, infrastructure, hosting, deployment, frontend implementation, strength-meter library, or completed executable validation; or
- a claim that password acceptance, credential establishment, registration, Authentication, Customer creation, Account creation, association, Session establishment, or implementation currently exists.

## Completed Governance Review

The completed governance review represented:

- Identity approval of credential-policy ownership, establishment/change boundaries, safe outcomes, and separation from Authentication, recovery, MFA, and Customer truth;
- Product approval of Customer-facing length, usability, rejection, rotation, and registration consequences;
- Security approval of the single-factor baseline, blocklist behavior, default denial, Secret handling, compatibility, and non-weakening of `SECURITY-STANDARDS.md`;
- Architecture approval that DEC-0003 through DEC-0006 and ADR-0019 through ADR-0022 remain intact and no new Architecture, provider, persistence, or dependency authority is introduced;
- Customer approval of registration and Customer/Account non-authority boundaries; and
- Engineering review of Unicode/NFC determinism, bounded input, testability, migration, operations, accessibility compatibility, and implementation neutrality.

The controlled governance review completed with no unresolved acceptance blocker. This Accepted record does not fabricate or claim a named reviewer, meeting, ticket, signature, implementation evidence, provider validation, executable test, or external approval artifact.

## Acceptance Synchronization

Under the authority verified by the completed acceptance-readiness review, controlled acceptance of DEC-0007 synchronized exactly these canonical artifacts:

1. `specifications/decisions/DEC-0007-initial-customer-password-policy.md` was promoted through its governed lifecycle to Accepted and records the accepted policy without claiming implementation.
2. `.ai/core/DECISIONS.md` changes DEC-0007's indexed status from Proposed to Accepted, records the acceptance synchronization in Revision History, and preserves the decision type, owner, identifier, and canonical path.
3. `.ai/core/PRODUCT.md` synchronizes the accepted initial Customer password-policy baseline into the existing Customer Account area, includes only policy established by DEC-0007, preserves Product Decision 5 as unresolved, leaves unrelated Product decisions unchanged, and makes no implementation claim.

Based on that review, `SECURITY-STANDARDS.md` does not require synchronization because its existing mandatory password and security controls remain compatible and authoritative. `ARCHITECTURE.md` does not require synchronization because DEC-0007 changes no architectural baseline. The Identity Domain Specification, Customer Domain Specification, Identity Backend Specification, and Customer Backend Specification do not require synchronization for the currently identified acceptance scope.

These not-required classifications apply only to DEC-0007's reviewed acceptance scope and are not permanent claims about later governance. Controlled acceptance found no direct contradiction requiring the synchronization set to be broadened.

## Accepted-State Validation Criteria

The Accepted record verifies:

1. metadata is `1.0.0 Accepted`, Type `Product Decision / Security Decision`, owner `Identity`, date `2026-09-30`, and `authoritative: true`;
2. the initial policy accepts 15 through 64 Unicode code points after NFC normalization, evaluates the complete normalized password, never silently truncates it, and clearly identifies exactly 64 as a repository-selected bounded-input maximum rather than an externally prescribed rejection threshold;
3. whitespace and case remain significant and are not silently trimmed, collapsed, inserted, removed, substituted, or changed;
4. Unicode, spaces, password managers, autofill, and paste remain supported without mandatory character-class composition rules;
5. every establishment or change checks the complete normalized password against a governed commonly used or compromised-password blocklist and rejects a match with safe actionable guidance;
6. unavailable, failed, stale under later governed conditions, or uncertain required blocklist evaluation fails closed without credential creation or replacement;
7. no initial password history or general reuse prohibition is established, and no raw or additional historical password material is authorized;
8. no arbitrary periodic rotation is required, while authorized change and accepted compromise or unsafe-verifier response remain possible;
9. policy validation remains Identity-owned, transient, purpose-limited, protected, and excluded from ordinary persistence, logging, telemetry, events, analytics, support evidence, and Customer/Account state;
10. password acceptance is not Authentication, Principal establishment, Session establishment, Authorization, recovery proof, email verification, association proof, or registration completion;
11. DEC-0003, DEC-0004, DEC-0005, and DEC-0006 remain unchanged and authoritative within their scopes;
12. ADR-0019, ADR-0020, ADR-0021, and ADR-0022 remain unchanged and authoritative within their scopes;
13. Product Decision 5 remains unresolved, and no email-verification requirement or gate is introduced;
14. blocklist provider, dataset, API, SDK, dependency, network, cache, update, retry, timeout, SLO, and infrastructure choices remain unresolved;
15. Contract, persistence, recovery, ordinary Customer MFA, abuse-control values, frontend implementation, and implementation details remain explicit non-decisions;
16. current NIST and OWASP guidance supporting the 15-code-point minimum, capacity of at least 64 characters, and usability policy is attributable and reviewed for applicability, while controlled acceptance explicitly approved DEC-0007's repository-specific choice to reject above 64 without presenting that exact maximum as externally prescribed;
17. required Identity, Product, Security, Architecture, Customer, and Engineering review evidence is durable without fabricated authority or implementation claims;
18. controlled acceptance changes only DEC-0007, `DECISIONS.md`, and `PRODUCT.md`; indexes DEC-0007 exactly once as Accepted; passes whitespace and diff validation; and introduces no unrelated tracked changes; and
19. controlled acceptance synchronizes DEC-0007, `DECISIONS.md`, and `PRODUCT.md`; preserves Product Decision 5 as unresolved; explicitly approves the repository-specific exact 64-Unicode-code-point upper bound through the required authorities; and creates no implementation, provider, dependency, persistence, API Contract, recovery, MFA, or abuse-control authority.

## References

- [AGENTS.md](../../.ai/core/AGENTS.md)
- [GLOSSARY.md](../../.ai/core/GLOSSARY.md)
- [PRODUCT.md](../../.ai/core/PRODUCT.md)
- [ARCHITECTURE.md](../../.ai/core/ARCHITECTURE.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DOCUMENTATION-STANDARDS.md](../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Customer Domain Specification](../domains/customer/customer-domain.md)
- [Customer and Account Backend Specification](../backend/customer/customer-backend.md)
- [DEC-0003 — Initial Local Customer Credential Mechanism](DEC-0003-initial-local-customer-credential-mechanism.md)
- [DEC-0004 — Customer Login Identifier Semantics](DEC-0004-customer-login-identifier-semantics.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0006 — Customer Password Hashing and Verification Strategy](DEC-0006-customer-password-hashing-verification-strategy.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0020 — Identity Session Store Strategy](../adr/ADR-0020-identity-session-store-strategy.md)
- [ADR-0021 — Customer Authentication Authority Strategy](../adr/ADR-0021-customer-authentication-authority-strategy.md)
- [ADR-0022 — Initial Identity–Customer/Account Association Strategy](../adr/ADR-0022-identity-customer-account-association-strategy.md)
- [NIST SP 800-63B — Authentication and Authenticator Management](https://pages.nist.gov/800-63-4/sp800-63b.html)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)

## Supersedes

N/A.

## Superseded By

N/A.

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-09-30 | Accepted | Accepted the initial Customer password policy with a 15-code-point minimum, repository-selected exact 64-code-point maximum after NFC normalization, complete-password compromised-value checking, fail-closed establishment and change behavior, no initial history or arbitrary periodic rotation, and password-manager and accessibility compatibility while preserving Product Decision 5 and all provider, dependency, Contract, persistence, recovery, MFA, abuse-control, infrastructure, frontend, and implementation non-decisions. |
| 0.1.0 | 2026-09-30 | Proposed | Proposed an evidence-informed initial Customer password policy with a 15-code-point external-guidance minimum and a repository-selected exact 64-code-point bounded-input maximum after NFC normalization, no composition rules, mandatory whole-password compromised-value checking, no initial history or arbitrary periodic rotation, password-manager and accessibility compatibility, safe migration boundaries, and bounded acceptance synchronization across DEC-0007, `DECISIONS.md`, and `PRODUCT.md` while preserving Product Decision 5, DEC-0003 through DEC-0006, ADR-0019 through ADR-0022, and all provider, dependency, Contract, persistence, recovery, MFA, abuse-control, infrastructure, frontend, and implementation non-decisions. |
