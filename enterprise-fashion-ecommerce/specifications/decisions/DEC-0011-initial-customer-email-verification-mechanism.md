# DEC-0011 — Initial Customer Email Verification Mechanism

- **Identifier:** DEC-0011
- **Title:** Initial Customer Email Verification Mechanism
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-09
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

Accepted DEC-0008 requires accepted verification of the current authoritative Customer login email before completed initial Registration, successful Customer Authentication, Principal or Session establishment, and protected Customer access. It defines the limited meaning of verification: control of that email under a later governed interaction, not Identity, Customer or Account ownership, Authentication, association, Authorization or recovery authority. Approved IEVE 1.0.0 specializes the subject/binding, accepted-outcome, source, consumer, currentness and failure evidence semantics without selecting a verification mechanism or authorizing production implementation.

The merged Identity-owned current-login-email binding source stores exact subject/email facts, supports attributable supersession and coherent observation, and distinguishes current, superseded, conflict and uncertainty. It is dormant: no production issuer or lawful population path exists. A published or observed binding, persisted email row, submitted address, delivery result or fixture is not proof of email control or accepted verification evidence.

DEC-0009 remains 0.8.0 Proposed and non-authoritative; its exhausted evaluations have not selected a strict IDNA2008 strategy, so canonical submitted-email lookup remains blocked. DEC-0010 remains 0.3.0 Proposed and non-authoritative; its exhausted evaluation has not selected a blocklist strategy, so complete prospective-password acceptance and credential establishment for completed Registration remain blocked. Neither blocker is resolved by this proposal. Authentication, Principal and Session issuance remain separate downstream outcomes under ADR-0019, ADR-0022 and the approved Contracts.

## Decision Question

Which initial Identity-owned mechanism, with what explicitly governed proof lifecycle and controls, may produce an accepted outcome that an actor controls the **exact authoritative current Customer login-email binding** of the intended stable Identity at the relevant point in that binding's lifecycle?

The eventual decision must make proof issuance, binding, presentation, acceptance, invalidation, replay and repeated effects, supersession, abuse control, secret protection, failure, durable evidence and point-of-reliance currentness implementable and testable. It must not infer control from a request, message dispatch, delivery, opening, caller assertion, source row or credential match.

## Decision — Proposed Boundary Only

This 0.1.0 record defines the decision problem and acceptance evidence boundary. **No mechanism, candidate, proof format, cryptographic construction, provider, dependency, persistence design or implementation is selected.** It grants no technical evaluation authorization. A later Proposed revision must explicitly authorize and bound any evaluation before it occurs; a later evidenced and properly reviewed decision revision is required for selection and acceptance. DEC-0008 and IEVE retain their own existing authority independently of this Proposed record.

Identity owns the accepted verification outcome and its security evidence. The decision must preserve the approved Registration and Login Contracts and Identity/Customer ownership; a mechanism must not acquire authority to establish a Customer, Account, association, Authentication result, Principal, Session, recovery factor or Authorization grant.

## Assets, Actors and Trust Boundaries

| Area | Scope to protect or assess; no implementation selected |
| --- | --- |
| Assets | Authoritative current login-email binding; stable Identity subject; proof or verification Secret; accepted outcome and provenance; verification applicability/currentness; operational abuse-control state where applicable. |
| Actors | Potentially unauthenticated presenter; already-established stable Identity subject; Identity-owned verification producer and point-of-reliance consumer; a later-selected delivery operator/provider; authorized operational actors with purpose-limited access. |
| Boundaries | External actor to Identity; Identity to any later delivery/provider; Identity application to persistence; accepted producer to evidence source; source to point-of-reliance consumer. Crossing a boundary does not itself establish verification. |

The producer must have an authoritative subject and exact current-email binding before accepting proof. A delivery system, storage layer, or consumer cannot independently manufacture the accepted outcome.

## Required Security and Evidence Properties

Future selection and acceptance must demonstrate all applicable properties below, explain any inapplicability, and identify the accountable owner of each control. These are evidence obligations for the future mechanism, not a mechanism chosen by this proposal.

| Area | Required future determination and evidence |
| --- | --- |
| Exact attribution | Bind issuance, presentation, acceptance and later observation to the intended stable Identity and exact authoritative current-email fact/value. Reject cross-Identity and cross-email substitution; recheck applicability against current source authority. |
| Proof security | Demonstrate unpredictability and guessing resistance, integrity, confidential handling, tamper resistance and suitable protection of material in transit and at rest where applicable. Select and justify the actual proof construction, not merely a descriptive label. |
| Validity | Define bounded validity and invalidation conditions, including expiry where selected, reissue, supersession, rejected or uncertain proof, and clock boundaries if time-dependent. Old proof cannot verify a replacement binding. |
| Replay and repeat effects | Define whether accepted proof is single-use or another explicitly justified repeat-effect model; prevent replay from creating new authority. Define duplicate request, delivery, presentation, concurrent presentation and resend/reissue behavior without duplicate accepted effects or stale restoration. |
| Concurrency and currentness | Show how concurrent reissue, acceptance and binding supersession resolve without arbitrary winners or reliance on stale proof. Positive reliance must use source-backed current evidence at the governed decision point. |
| Attempts and abuse | Define and justify guessing, brute-force, automation, flooding, resend and rate controls; keep denial-of-service and enumeration risks in view without inventing numerical values here. |
| Delivery separation | Message preparation, provider acceptance, delay, retry, failure, delivery or opening cannot count as accepted control proof. Define the eventual delivery/provider boundary and its failure effects separately from acceptance. |
| Privacy and Secrets | Minimize sensitive email and proof material; prevent disclosure in URLs, ordinary responses, logs, metrics, traces, events, analytics, diagnostic errors and support evidence; protect existence and response-shape/timing signals. |
| Durable evidence | Define what accepted outcome and provenance may be persisted, who may write it, how pending or failed work remains distinguishable, and how source observation establishes exact binding and current applicability. A row alone cannot manufacture acceptance. |
| Failure and reconciliation | Failed, unavailable, partial, stale, conflicting or uncertain issuance, acceptance, storage and observation fail closed. Define accountable reconciliation and operational recovery limits without treating a retry or support action as proof. |
| Testability and authority | Supply deterministic positive, negative, replay, concurrency, supersession, privacy and failure tests. Verification alone must not establish Authentication, Principal, Session, association, Customer/Account ownership, Authorization, recovery or MFA. |

## Alternatives for a Later Authorized Evaluation

These are categories to compare, **not selected candidates or permission to evaluate them now**. Each future comparison must consider proof security and guessing resistance, replay and repeat effects, exposure, storage, concurrency, usability, delivery constraints, operational recovery, abuse controls, implementation complexity, dependency and provider implications, provider independence, and migration or replacement cost.

| Category | Distinct questions for future evidence |
| --- | --- |
| Short user-entered one-time code | How can a human-enterable proof resist guessing and automation, remain bound to one subject/current email, and be invalidated or consumed safely under retry, reissue and concurrency? |
| High-entropy verification link or token | How are proof custody, transport, accidental URL/referrer/log exposure, replay, current-binding applicability and safe repeated opening handled? |
| Bounded hybrid or other possession-proof interaction | What additional steps and trust boundaries are introduced, what each step proves, and how is incomplete work prevented from appearing accepted? |
| Stateful versus derived/stateless representation, where compatible | Where does authoritative acceptance and invalidation state live; how are reissue, supersession, single effect, concurrency and key/secret lifecycle enforced without assuming storage-free verification? |

No category is presumed safer, more usable, cheaper or compatible merely from its name. A future evaluation must substantiate those claims against the same governed requirements.

## Threat and Failure Evidence Required Before Acceptance

A future threat model and deterministic scenarios must cover proof guessing and brute force; proof theft, tampering and logging disclosure; cross-Identity and cross-email substitution; stale proof after binding supersession; duplicate delivery and presentation; concurrent presentation and reissue; old proof after reissue; provider delay, retry and failure; persistence failure, partial write and stale read; conflicting evidence; enumeration and abuse flooding; and clock/expiry boundaries if validity depends on time. It must distinguish actual tested behavior, design assertions, assumptions and unresolved risk. This record claims that none of these controls has been implemented by DEC-0011.

## Acceptance-Readiness and Separate Gates

Before DEC-0011 could become Accepted, a later revision must identify a concrete mechanism and accountable Identity owner; record a threat model and realistic alternatives comparison; define the exact subject/current-email binding, proof issuance/presentation/acceptance lifecycle, validity and invalidation, replay/repeated effects, attempts and abuse, secret handling, provider/delivery boundary, durable evidence and source observation, concurrency and supersession, fail-closed outcomes and reconciliation. It must provide privacy and enumeration analysis, deterministic negative scenarios, operational and support responsibilities, migration/replacement implications, and a test strategy aligned with DEC-0008 and IEVE. Residual risks and limitations must be explicit. Security authority review is required; Architecture authority review is required where material Architecture is introduced.

A material Architecture choice requires its own ADR and, if accepted, canonical Architecture synchronization; DEC-0011 cannot silently select a new Architecture. Any new library or dependency requires separate DEC-0001 admission. Provider selection or external integration requires its applicable provider/integration review, privacy and operational assessment, and any separately required decision. Acceptance of this Security Decision would not itself admit a dependency or provider, approve an external Contract, authorize a migration, or establish production readiness. Production implementation needs separate implementation-readiness review of the real lawful current-email population, accepted producer, source-backed consumer, applicability, failure and privacy paths.

This proposal authorizes no research or evaluation. A bounded investigation, if needed, must first receive explicit scope, evidence criteria, stop conditions and authority in a later governed revision. Unresolved evidence blocks selection rather than inviting an implicit experiment or fallback.

## Relationship to Current Flows

The current-email source is infrastructure for binding and currentness observation, not email-verification evidence. Publication and observation are not proof of control; its persisted email state has no production population issuer. DEC-0011 does not create that issuer.

For Login, submitted email → strict canonical key → stable Identity resolution remains dependent on DEC-0009. Even a future accepted verification outcome satisfies only the DEC-0008 gate; password evidence, complete Authentication, Principal and Session establishment, association and contextual Authorization remain separate.

For Registration, DEC-0008 requires accepted verification of the current authoritative email before completion. This proposal does not resolve DEC-0010, establish a credential or Customer/Account association, coordinate completed Registration, or change either Contract. Login-email change, recovery, password reset and Customer MFA remain outside this initial decision. Verification or mailbox control must not silently become recovery authority.

## Consequences and Reversibility

Creating this Proposed boundary makes the missing Security choice and required evidence durable, prevents ad-hoc proof acceptance, and permits a future separately authorized comparison. It leaves implementation blocked and imposes evidence and review work. Premature selection could create weak guessing or replay semantics, provider coupling, authority inflation, recovery bypass, or privacy and enumeration exposure; none is accepted as a trade-off here. This proposal is readily reversible because it selects no mechanism and creates no production state. Any future accepted mechanism must assess migration, replacement and retained-evidence consequences before selection.

## Explicit Non-Decisions

DEC-0011 0.1.0 selects no code versus link versus other proof; numeric or alphanumeric format; length, entropy, cryptographic construction, hashing or storage representation; validity duration; attempt, resend or rate values; provider, vendor, SDK, dependency or delivery technology; schema, table, column, index, cache, queue or event infrastructure; endpoint, DTO, status or public/internal Contract change; recovery, password reset, email-change or MFA behavior; Authentication, Principal, Session or association implementation. It admits no dependency or provider, changes no Product or Architecture baseline, resolves neither DEC-0009 nor DEC-0010, and authorizes no evaluation or production implementation.

## References

- [DECISIONS.md](../../.ai/core/DECISIONS.md)
- [SECURITY-STANDARDS.md](../../.ai/core/SECURITY-STANDARDS.md)
- [DEC-0001 — Backend Build Tool and Dependency Management Baseline](DEC-0001-backend-build-tool-dependency-management.md)
- [DEC-0005 — Customer Login Email Comparison, Uniqueness, and Lifecycle Semantics](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0008 — Initial Customer Email Verification Policy](DEC-0008-initial-customer-email-verification-policy.md)
- [DEC-0009 — Customer Login IDNA2008 Implementation Strategy](DEC-0009-customer-login-idna2008-implementation-strategy.md)
- [DEC-0010 — Customer Password Blocklist Strategy](DEC-0010-customer-password-blocklist-strategy.md)
- [ADR-0019 — Authentication Session and Token Strategy](../adr/ADR-0019-authentication-session-token-strategy.md)
- [ADR-0022 — Initial Identity–Customer/Account Association Strategy](../adr/ADR-0022-identity-customer-account-association-strategy.md)
- [Current Login Email Verification Evidence Design (IEVE)](../backend/identity/current-login-email-verification-evidence-design.md)
- [Identity and Access Backend Specification](../backend/identity/identity-backend.md)
- [Identity Domain Specification](../domains/identity/identity-domain.md)
- [Initial Customer Login Contract](../contracts/identity/initial-customer-login-contract.md)
- [Initial Customer Registration Contract](../contracts/customer/initial-customer-registration-contract.md)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-10-09 | Proposed | Established the Identity-owned Security decision question, alternatives, threat and acceptance-evidence boundaries for an initial current-email control-proof mechanism; selected no mechanism, provider, dependency or persistence design, authorized no evaluation or implementation, and preserved DEC-0008/IEVE authority and unresolved DEC-0009/DEC-0010 prerequisites. |
