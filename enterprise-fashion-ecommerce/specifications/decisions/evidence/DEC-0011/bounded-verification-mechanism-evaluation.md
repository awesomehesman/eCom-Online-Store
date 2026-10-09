# DEC-0011 bounded initial Customer email-verification mechanism evaluation

## Purpose and authorization

- Evaluation date: 2026-10-09 (Africa/Johannesburg). Evaluator: Codex AI assistant; accountable decision owner: Identity. Human approval is not asserted.
- Authorization: [DEC-0011 0.2.0 Proposed](../../DEC-0011-initial-customer-email-verification-mechanism.md), Identity-owned Security Decision, Authoritative false; one finite documentation/static-evidence evaluation only.
- Repository baseline: `40efd1537bb769b9b4b3357a611954539ca16d86` (`origin/develop` and evaluation branch HEAD when inspected); branch `docs/dec-0011-verification-mechanism-evidence`.
- Scope: compare a short user-entered one-time code (A) with a high-entropy verification link/token (B). Consider a hybrid only if materially distinct. Analyze stateful and derived/stateless representations across candidates. This artifact is evidence, not a decision, admission or implementation authorization.

## Evaluation question

Which evaluated initial Customer email-verification proof category, if any, demonstrates a sufficient security, lifecycle, operational and governance basis to justify a **later** DEC-0011 mechanism-selection and acceptance review for proving control of the exact authoritative current Customer login-email binding?

## Repository baseline and authority

The repository sources inspected were [DEC-0011](../../DEC-0011-initial-customer-email-verification-mechanism.md), [DEC-0008](../../DEC-0008-initial-customer-email-verification-policy.md), [IEVE](../../../backend/identity/current-login-email-verification-evidence-design.md), [DEC-0005](../../DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), [DEC-0001](../../DEC-0001-backend-build-tool-dependency-management.md), [DEC-0009](../../DEC-0009-customer-login-idna2008-implementation-strategy.md), [DEC-0010](../../DEC-0010-customer-password-blocklist-strategy.md), [DECISIONS.md](../../../../.ai/core/DECISIONS.md), [SECURITY-STANDARDS.md](../../../../.ai/core/SECURITY-STANDARDS.md), [ARCHITECTURE.md](../../../../.ai/core/ARCHITECTURE.md), [API.md](../../../../.ai/backend/API.md), [ADR-0019](../../../adr/ADR-0019-authentication-session-token-strategy.md), [ADR-0022](../../../adr/ADR-0022-identity-customer-account-association-strategy.md), the Approved Identity credential-state/source/publication designs and Identity backend/domain specifications, and the existing current-login-email source implementation and tests. Code was read as implementation evidence, not treated as authority.

**R — repository fact:** DEC-0008 requires accepted current-email control evidence before the specified Registration, Customer Authentication/Principal/Session and protected-access outcomes. It does not equate dispatch, delivery or opening with acceptance and grants no recovery, association or Authentication authority. IEVE 1.0.0 requires a stable Identity subject, exact authoritative current-email binding, positive producer provenance, durable evidence where required, source-backed consumption, point-of-reliance currentness, replay and uncertainty controls. The merged current-email source supports binding observation but has no production population issuer and no accepted verification producer. Its rows, fixtures and tests are not verification. DEC-0009 and DEC-0010 remain Proposed/deferred with exhausted earlier evaluations. DEC-0011 remains Proposed; no candidate or security envelope is selected.

## Evidence method, classification and sources

This was a bounded comparison of repository authority with published security/standards documentation and logical threat analysis. No prototype, test, delivery, benchmark, entropy measurement, provider evaluation or live attack was performed. **R** denotes repository fact; **D** attributable external documentation; **I** evaluator inference from stated premises; **A** assumption for comparison; **U** unresolved question. A documented control is not a measured control in this repository. External guidance is contextual evidence, not an automatic repository requirement or decision; its illustrative numeric values are not imported.

| ID | External source inspected | Relevant documented statement and limit |
| --- | --- | --- |
| S1 | [NIST SP 800-63A, confirmation codes](https://pages.nist.gov/800-63-4/sp800-63a/ial-general/) | **D:** Email-address confirmation codes can be manually entered or represented in a secure HTTPS link; the guidance calls for random, time-limited, one-use confirmation. Its identity-proofing context and numeric prescriptions are not adopted as this repository's production parameters. |
| S2 | [NIST SP 800-63B, authentication](https://pages.nist.gov/800-63-4/sp800-63b.html) | **D:** Email is disallowed as an out-of-band **authentication** channel there, while email-address validation confirmation codes are expressly distinguished from authentication. This comparison concerns the latter and cannot turn either proof into a Customer authenticator. |
| S3 | [OWASP Email Validation and Verification Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Email_Validation_and_Verification_Cheat_Sheet.html) | **D:** Discusses unpredictable, limited-use verification tokens, delayed activation until verification, anti-enumeration and sensitive-data/logging precautions. Its general address-normalization advice does not supersede DEC-0005 or resolve DEC-0009. |
| S4 | [OWASP Forgot Password Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html) | **D:** Analogous token/code threat guidance covers randomness, expiry, one use, secure storage, uniform responses, rate limiting and URL referrer exposure. Password recovery is **not** authorized by this analogy. |
| S5 | [RFC 9110, HTTP Semantics §10.1.3](https://www.rfc-editor.org/rfc/rfc9110.html#section-10.1.3) | **D:** A Referer can expose URI context; fragments and userinfo must not appear in Referer. This does not establish that a query/path proof is safe from history, logs, copies or other disclosure. |

Public pages can change. No immutable snapshot or independent measurement was obtained. Source claims were limited to the cited subjects; no standards-compliance certification is asserted.

## Candidate A — user-entered one-time code

**D, S1:** Manual code entry is an established presentation class for email confirmation. **I:** Issuance would have to bind a securely generated challenge to the already-authoritative stable Identity and exact current email; the presenter would submit the code through a later governed interaction; Identity would accept only a matching, live, not-yet-consumed proof and record the bounded positive outcome. A code is not evidence merely because it was generated, sent or entered. Short human-entry format lowers the usable proof space relative to a high-entropy bearer link; online guessing resistance consequently depends on jointly bounded generation, validity, attempts and abuse controls. It need not place the proof in a URL, but form input, diagnostics, support and telemetry remain disclosure surfaces. Mailbox compromise, forwarding, phishing and cross-device entry remain relevant. Human entry may aid cross-device flows, while transcription/accessibility costs exist; no usability study was performed. Format, length, alphabet, lifetime and limits are unresolved.

## Candidate B — high-entropy verification link/token

**D, S1/S3:** A secure HTTPS link carrying a random confirmation representation is an established presentation class. **I:** The link token is bearer-like proof; acceptance still requires the same exact subject/current-email binding, live validity, one-use effect and currentness check. Larger unpredictable proof space can reduce random guessing risk, but it does not compensate for exposure through a query/path URL, browser history, server/proxy logs, copied or forwarded links, mailbox compromise, phishing or unintended opening. **D, S5:** Referer may disclose URI context, subject to the RFC's fragment exclusion; a later design must assess the actual location and browser flow rather than assume all URL forms leak identically. A link may reduce manual transcription, but device handoff, scanners/prefetch and accessibility behavior are not measured here. Whether mere navigation presents proof or an additional deliberate action is needed is a later Contract/security choice, not decided here. Token size, lifetime, URL placement and controls are unresolved.

## Hybrid assessment

**I:** A flow combining a link and a typed code would add steps, but the inspected evidence does not establish a distinct security or lifecycle property unavailable from A/B under their own binding and acceptance controls. Requiring both might change user burden and delivery exposure without proving independent factors if both arrive at the same mailbox. No third candidate is opened; a hybrid is neither selected nor ruled out universally.

## Stateful versus derived/stateless representation

| Property | Explicit server-held proof state | Derived/signed proof without per-proof state |
| --- | --- | --- |
| Validity and compromise | **I:** Server-held challenge status can be invalidated and inspected, but proof/state compromise and protected storage need review. | **I:** Integrity can be checked cryptographically if keys are protected; key compromise can affect a wider proof population. |
| One use, replay, reissue | **I:** Atomic consume/invalidate and new-versus-old relationship can be represented, subject to transaction/concurrency design. | **I:** Signature and expiry alone cannot establish one use, immediate revocation or old-proof invalidation after reissue. An authoritative consumed/reissue/binding epoch or equivalent shared state is needed. |
| Supersession/currentness | **R/I:** Either representation must consult authoritative current-email evidence at acceptance and reliance; challenge state alone is insufficient. | **R/I:** Embedded old email/subject claims cannot substitute for current source observation. |
| Failure and recovery | **I:** Store failure, partial writes and stale reads must fail closed and be reconciled without fabricating acceptance. | **I:** Key rotation, clock handling, shared replay state and inconsistent nodes add recovery requirements; avoiding a challenge row does not remove durable accepted-evidence duties. |

These are representation properties, not new mechanism categories or a representation selection. Concrete storage, signing, keys, locks and recovery designs remain unchosen.

## Security-envelope analysis

**D, S1/S3/S4:** Secure generation, finite validity, one-use semantics and resistance to guessing are documented security properties for confirmation artifacts. **I:** For an otherwise uniformly random code of proof-space size `N`, at most `q` independent online guesses give a simple upper-bound intuition of approximately `q/N` success before other effects; this is a model, not a measured rate or accepted risk threshold. The bound is meaningful only if attempts are actually bounded across retries, identities, IPs and reissues and if expiry/consumption is enforced. A high-entropy link can make `N` much larger, yet theft and URL disclosure dominate once the proof is exposed. No numeric `N`, `q`, lifetime, rate, risk tolerance or production entropy is selected. Later acceptance must make unpredictability, validity, attempts, one-use/reissue, disclosure controls and failure behavior testable for the chosen category. **U:** Whether a particular construction meets that envelope cannot be concluded without its design and validation; the category-level basis is sufficient to compare at a later review, not to approve it now.

## Threat and failure analysis

| Threat/failure | A — code | B — link/token | Required bounded disposition |
| --- | --- | --- | --- |
| Guessing/brute force | Smaller human-entry space increases dependence on online controls. | High entropy reduces random guessing if generation is sound. | Secure generation, bounded attempts, expiry, abuse controls; no values chosen. |
| Theft, mailbox compromise, forwarding, phishing | Stolen message/code can be entered by another actor. | Stolen/copied URL is a bearer path; query/path leakage and unintended opening add exposure. | A valid proof demonstrates address control only within the governed interaction; neither establishes Identity or Authentication. |
| Cross-Identity/email substitution | Entering code for another subject/email must fail. | Token for another subject/email must fail. | Bind issuance and acceptance to stable Identity and exact authoritative current email. |
| Replay, duplicate presentation/delivery | Repeated input or message delivery cannot create second acceptance. | Reopened, scanned or resent link cannot create second acceptance. | Single accepted effect, atomic consume or equivalent authoritative guard; delivery is not acceptance. |
| Concurrent presentation/reissue; old proof after reissue | Race could accept two attempts or stale code. | Race could accept two opens or stale token. | Define intent, replacement order and atomic outcome; old proof must not restore or supersede newer authority. |
| Authoritative-email supersession; stale observation | Old-email code is no longer applicable. | Old-email link is no longer applicable. | Fresh source-backed currentness at acceptance and reliance; no stale winner. |
| Provider delay/retry/failure | Late or duplicated messages cannot extend validity. | Late or duplicated links cannot extend validity. | Separate dispatch from accepted proof; define bounded resend/reissue semantics. |
| Persistence failure, partial write, conflict | Cannot infer success from generation or incomplete state. | Same, including any replay registry and accepted-evidence write. | Fail closed, distinguish uncertainty, reconcile from accountable evidence without inventing acceptance. |
| Expiry boundary, clock skew, unavailable source | Race at validity edge or missing currentness can misaccept. | Same. | Deterministic boundary rule and fresh authoritative observation; withhold positive reliance on uncertainty. |
| Enumeration, flood and telemetry exposure | Initiation/guess endpoints can reveal existence or flood mailboxes. | Initiation/open endpoints can reveal existence; full URLs are sensitive. | Non-disclosing external behavior, throttling/abuse review, secret-safe logs and support surfaces. |

The table specifies later evidence obligations, not implemented protections or a claim that every threat is fully mitigated.

## Binding, currentness, replay, reissue and concurrency

**R:** IEVE requires exact stable-subject/current-email applicability at producer acceptance and consumer reliance. **I:** Each issued proof needs a distinguishable intent bound to that pair and an authoritative lifecycle relation to later proofs. A fresh issuance cannot verify a previous email after confirmed supersession; an earlier accepted outcome cannot verify a new email. Duplicate initiation or provider retry must not silently create an additional live authority. Reissue must define whether prior proof remains live, is superseded, and how concurrent presentation is ordered. Acceptance must resolve races so that at most one bounded effect for the intended proof occurs. A prior source observation cannot be reused across an intervening confirmed change. No transaction, lock, version column or cache rule is selected.

## Abuse, enumeration, privacy and sensitive evidence

**D, S3/S4; R, SECURITY-STANDARDS/DEC-0008/IEVE:** Both categories need controls against guessing, repeated initiation, mailbox flooding and existence disclosure. Failure and success timing/messages must be reviewed for enumeration without inventing a Contract here. Proofs, full verification links, protected email and accepted evidence require purpose-limited handling in logs, metrics, traces, analytics, diagnostics and support. The link additionally exposes proof via URL-bearing surfaces; a referrer policy alone is not a complete confidentiality argument. Secret-at-rest handling is relevant to either category. This evaluation selects no encryption/hash/signing method or logging format.

## Provider and delivery separation

**R:** Identity owns accepted proof; a future provider transports a message but cannot assert verification or currentness. **I:** Provider retry, delay, duplicate delivery and failure alter operational visibility and user experience, not acceptance. Production needs a separately governed delivery boundary and failure/reconciliation policy. No provider, API, SDK, transport, queue or vendor is evaluated or selected. A delivered or opened message is not positive evidence.

## Persistence and durable-evidence implications

**R:** IEVE distinguishes a challenge, accepted outcome and durable source-backed evidence. **I:** Stateful challenges may need protected issuance, validity, consumption and reissue state. Derived proofs still need shared one-use/revocation information if those guarantees are required, plus durable accepted outcome and currentness evidence. Partial challenge or evidence writes must not create a verified flag; a successful response cannot precede accountable acceptance. No schema, table, column, index, isolation level, transaction boundary or event design is admitted.

## Architecture, dependency and Contract implications

**R/I:** Identity remains the producer and evidence authority; Customer/Account association, Authentication, Principal, Session and Authorization remain separate under Architecture, ADR-0019/0022 and the approved Contracts. The static comparison itself requires no new Architecture decision. A later provider boundary, cross-module orchestration or material state architecture must receive separate Architecture/ADR review where applicable. Neither proof category inherently requires a new third-party dependency: secure randomness, comparison and protected storage are capabilities to assess, not a library selection. Any actual new dependency needs DEC-0001 admission. A later initiation/presentation interaction may have external Contract implications, but no route, method, field, response, cookie or OpenAPI behavior is designed here.

## Operational, recovery and migration implications

**I:** Support must distinguish never issued, sent, delayed, expired, consumed, superseded, source unavailable and accepted evidence without revealing proofs or fabricating confirmed absence. Reconciliation after partial failure must preserve accepted provenance and not turn provider success into acceptance. Rotation, replacement, migration and retained-evidence effects depend on a later chosen representation and require separate review. Operational recovery here means repairing the verification process, **not** Customer account recovery, password reset, email change, MFA or Identity lifecycle authority.

## Deterministic testability

A later selection review can specify clock-controlled expiry and boundary cases; fixed or injected randomness for tests without weakening production generation; wrong-subject/email proof; old email after confirmed supersession; duplicate issue, delivery, presentation and accepted effect; concurrent presentation/reissue; old proof after reissue; provider delay/failure/retry; persistence partial write and stale read; source conflict/unavailability; secret-safe telemetry; and enumeration/abuse behavior. These are proposed future tests, not executed tests or a claim of production readiness. Fixtures and mocks cannot prove a lawful production current-email population or accepted producer.

## Compatibility with DEC-0008 and IEVE

**R/I:** Both A and B can be evaluated as proof-presentation categories consistent with DEC-0008 and IEVE only if a later governed producer binds the accepted result to the exact current email of an already-authoritative stable Identity, and a source-backed consumer checks applicability at reliance. Neither dispatch, delivery, opening nor a stored current-email row passes that gate. Email control alone is not Authentication, Principal, Session, Authorization, Customer/Account association or recovery. NIST's authentication prohibition (S2) reinforces keeping this distinction explicit; it does not prohibit the repository's separate email-address verification policy.

## Preserved unresolved decisions, assumptions and questions

- **R:** DEC-0009 strict IDNA2008/canonical submitted-email lookup and DEC-0010 complete prospective-password/blocklist acceptance remain Proposed/deferred with prior evaluation authority exhausted; neither is reopened or bypassed. The current-email source still lacks a production population issuer. No Registration or Login completion is inferred.
- **A:** For category comparison only, an already-authoritative stable Identity/current-email pair and a trustworthy Identity producer are hypothetically available. They are not established in production.
- **A:** Proof generation can use a cryptographically secure source and a future governed acceptance interaction. Neither is measured or implemented here.
- **U:** Candidate-specific proof-space, lifetime, attempt budgets, abuse thresholds, reissue semantics, token placement, link-opening behavior, key handling and residual risk tolerance need later Security decision and testing.
- **U:** Lawful source population, accepted producer, durable evidence model, delivery operations, external interaction Contract, privacy review, provider choice and any material Architecture/ADR or dependency admission remain separate prerequisites.

## Stop-condition review

The required category-level question was answerable from repository authority, cited documentation and bounded inference without a prototype, new dependency, provider selection, Architecture choice, Contract design or DEC-0009/DEC-0010 resolution. No material contradictory source was identified within this scope. This does **not** mean candidate-specific acceptance evidence is complete. Had the question required concrete implementation or quantitative production values here, the evaluation would have stopped rather than invented them. No prohibited expansion was performed.

## Comparative evaluation matrix

| Dimension | A — user-entered code | B — high-entropy link/token | Later gate |
| --- | --- | --- | --- |
| Presentation and human work | Manual entry; possible device transfer and transcription burden. | Link navigation; URL handling and unintended opening matter. | Usability/accessibility evidence and deliberate acceptance design. |
| Guessing envelope | Strongly coupled to proof space and online attempts. | High entropy reduces random guessing, not theft. | Testable generation, lifetime, attempts and abuse envelope. |
| Disclosure | Message, form, support/telemetry. | Message plus URL/history/log/referrer/copy/forward surfaces. | Purpose-limited secret handling and actual flow threat model. |
| Binding/currentness | Exact Identity/email binding and source observation required. | Same. | Lawful populated source, producer and consumer evidence. |
| Replay/reissue/concurrency | One-use/old-proof and race controls required. | Same; navigation/scanning adds presentation cases. | Atomic or equivalent authoritative effects and deterministic tests. |
| State representation | Stateful or derived possible conceptually; derived needs shared invalidation/consume authority. | Same. | Separately reviewed persistence/key/recovery design. |
| Delivery/provider | Transport cannot assert acceptance. | Same. | Provider/integration and operational governance. |
| Repository compatibility | Conditional category-level basis; no implementation eligibility. | Conditional category-level basis; URL threat is distinct. | Later DEC-0011 selection/acceptance, other gates unchanged. |

## Findings and rationale

- **Finding 1 — supported category-level basis (D/R/I):** S1 documents both manual and HTTPS-link confirmation representations; S3/S4 document their necessary security controls. DEC-0008/IEVE provide the repository binding and acceptance invariants. At least one category therefore has a defensible conceptual security and lifecycle basis for **later selection review**, conditioned on an explicit, testable envelope. This does not demonstrate a completed implementation or justify a winner.
- **Finding 2 — material difference (D/I):** Code security relies more heavily on bounded online guessing; link security has a broader URL disclosure and unintended-opening surface. A single entropy ranking would omit those differences.
- **Finding 3 — common non-negotiable controls (R/I):** Exact subject/current-email binding, source-backed currentness, one-use/reissue/concurrency ordering, fail-closed uncertainty and durable accepted evidence are required regardless of category or state representation.
- **Finding 4 — evidence limits (U):** No concrete production envelope, provider behavior, source population, Contract, Architecture implementation, cryptographic construction or operational test result exists in this evaluation. Those limits prevent selection/acceptance and implementation now, but do not prevent a later selection review under the authorized Outcome A definition.

## Terminal outcome

**A — EVIDENCE SUFFICIENT FOR LATER SELECTION REVIEW.** At least one evaluated category has a sufficiently supported, repository-compatible security and lifecycle **basis** to bring to a later DEC-0011 mechanism-selection and acceptance review. Both categories remain conditional; neither is selected or Accepted. Outcome A is not evidence that any specific production construction meets a complete quantitative envelope, and it grants no implementation, provider, dependency, Architecture or Contract authority.

## Authorization consumption and next governance boundary

Execution and this terminal outcome **CONSUME / EXHAUST** the single-use DEC-0011 0.2.0 authorization. No continuation, second evaluation, candidate substitution, material scope expansion or reuse of unused capacity is authorized. The next action is separately reviewed DEC-0011 governance synchronization and, later, a mechanism-selection/acceptance review with explicit Security evidence and separate applicable Architecture/ADR, DEC-0001 dependency, provider/integration and Contract gates. This artifact does not revise DEC-0011 or DECISIONS.md. DEC-0008 and IEVE remain in force; DEC-0009 and DEC-0010 remain unresolved/deferred; production implementation remains blocked pending its own readiness review.
