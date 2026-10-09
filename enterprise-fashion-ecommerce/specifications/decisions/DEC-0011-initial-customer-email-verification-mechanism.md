# DEC-0011 — Initial Customer Email Verification Mechanism

- **Identifier:** DEC-0011
- **Title:** Initial Customer Email Verification Mechanism
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.5.0
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

Version 0.1.0 defined the decision problem and acceptance evidence boundary without evaluation authority. Version 0.2.0 authorized exactly one bounded documentation/static-evidence evaluation, completed with Outcome A and its authorization **CONSUMED / EXHAUSTED**. Version 0.3.0 synchronized that result without selecting a mechanism. A later read-only selection review reached **C — NO DEFENSIBLE MECHANISM SELECTION FROM CURRENT EVIDENCE**: both categories remained plausible, but no repository-specific discriminator was established. Version 0.4.0 authorized a separate, single-use comparative evaluation; the completed comparison reached **C — NO REPOSITORY-SPECIFIC DISCRIMINATOR DEMONSTRATED**, and its authorization is **CONSUMED / EXHAUSTED**. The 0.2.0 Outcome A remains historically valid as a reviewability result; neither the read-only selection review nor the 0.4.0 Outcome C contradicts it. DEC-0011 remains Proposed/deferred because current canonical authority establishes no defensible repository-specific discriminator. **No mechanism, candidate, proof format, cryptographic construction, provider, dependency, persistence design or implementation is selected.** Future selection requires a fresh attributable governance basis and separately reviewed decision revision with required Security and applicable Architecture review. This synchronization authorizes no further evaluation. DEC-0008 and IEVE retain their existing authority independently of this Proposed record.

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

## Alternatives for the Bounded Evaluation

These were categories for comparison under the single, now-exhausted 0.2.0 authorization below, **not selected candidates or permission to expand its scope**. That comparison considered proof security and guessing resistance, replay and repeat effects, exposure, storage, concurrency, usability, delivery constraints, operational recovery, abuse controls, implementation complexity, dependency and provider implications, provider independence, and migration or replacement cost.

| Category | Distinct questions for future evidence |
| --- | --- |
| Short user-entered one-time code | How can a human-enterable proof resist guessing and automation, remain bound to one subject/current email, and be invalidated or consumed safely under retry, reissue and concurrency? |
| High-entropy verification link or token | How are proof custody, transport, accidental URL/referrer/log exposure, replay, current-binding applicability and safe repeated opening handled? |
| Bounded hybrid or other possession-proof interaction | What additional steps and trust boundaries are introduced, what each step proves, and how is incomplete work prevented from appearing accepted? |
| Stateful versus derived/stateless representation, where compatible | Where does authoritative acceptance and invalidation state live; how are reissue, supersession, single effect, concurrency and key/secret lifecycle enforced without assuming storage-free verification? |

No category is presumed safer, more usable, cheaper or compatible merely from its name. A later selection/acceptance review must substantiate any such claim against the same governed requirements.

## Completed Bounded Evaluation and 0.3.0 Proposed Disposition

The single 0.2.0 documentation/static-evidence evaluation was executed once and is recorded in the [durable bounded-verification-mechanism evidence](evidence/DEC-0011/bounded-verification-mechanism-evaluation.md). It compared (A) a short user-entered one-time code and (B) a high-entropy verification link/token, with stateful versus derived/stateless representation as a cross-cutting dimension. It recorded **A — EVIDENCE SUFFICIENT FOR LATER SELECTION REVIEW**. This means only that at least one evaluated category has a sufficiently supported, repository-compatible security and lifecycle basis to justify a later DEC-0011 mechanism-selection and acceptance review. The evidence did not establish a materially distinct hybrid candidate.

Both primary categories have recognizable confirmation-mechanism support in attributable guidance. Short codes depend more heavily on a bounded online guessing envelope. High-entropy links/tokens reduce random guessing exposure but add bearer/URL disclosure and unintended-opening concerns. Entropy alone cannot decide between them. Exact Identity/current-email binding, source-backed currentness, one-use/reissue/concurrency effects, fail-closed uncertainty and durable accepted evidence remain common requirements. No concrete production security envelope, provider behavior or production implementation was evaluated, and no candidate-specific construction was demonstrated production-ready.

**The 0.2.0 authorization is CONSUMED / EXHAUSTED.** It permits no continuation, second evaluation, candidate substitution, material scope expansion, prototype/spike or reuse of unused capacity. This 0.3.0 synchronization grants no new evaluation authority. Outcome A selects neither code nor link/token, hybrid, stateful nor derived/stateless representation; it does not Accept DEC-0011, authorize implementation or approve a provider, dependency, Architecture choice or Contract.

The subsequent read-only mechanism-selection/acceptance-readiness review found no repository-specific discriminator and selected nothing. Before acceptance, a later review must explicitly determine the chosen mechanism and testable security envelope; generation/unpredictability, validity/expiry, guessing/attempt/abuse, replay/single-effect, resend/reissue, stale/superseded binding, concurrency and proof-confidentiality controls; accepted producer, durable evidence, point-of-reliance currentness, failure/reconciliation and operational recovery; provider independence or separately governed requirements; Architecture, dependency and Contract implications; and deterministic acceptance evidence. Outcome A did not itself establish acceptance readiness. Required Security and applicable Architecture approval under DECISIONS.md, any material Architecture/ADR decision, DEC-0001 dependency admission, provider/integration review and external Contract governance remain separate gates.

DEC-0008's verification policy and Approved IEVE's exact-binding, accepted-evidence, currentness and reliance design remain in force. The existing current-login-email source remains binding/currentness infrastructure, not verification or accepted verification evidence; it still has no production population issuer, which this revision does not create. DEC-0009 and DEC-0010 remain Proposed/deferred with exhausted earlier evaluation authority; neither is reopened, continued or resolved. Canonical submitted-email resolution, complete prospective-password acceptance, Authentication orchestration, Principal construction, Session issuance, Identity/Customer association implementation and Registration completion remain unauthorized by this revision. Dispatch, delivery, opening and provider success do not establish verification.

## Historical Single Comparative Evidence Evaluation Authorization — 0.4.0 Proposed (Consumed / Exhausted)

The original scope and stop rules below are retained to explain the completed comparison. Their imperative wording records the historical authorization and grants no authority to execute it again.

### Question and finite scope

Identity may execute **exactly one** finite, non-production comparative documentation/static-evidence evaluation of only (A) a short user-entered one-time verification code and (B) a high-entropy verification link/token. Its question is: **Does either Candidate A or Candidate B provide a materially better fit for the repository's existing accepted email-verification requirements and security boundaries when both are evaluated against the same bounded construction assumptions and scenarios?** It must seek an attributable **repository-specific discriminator**, not prefer a category merely because it is common, recommended generically, apparently simpler or friendlier, higher-entropy in isolation, or widely used elsewhere. This authorization is new and independent; it neither inherits nor revives the exhausted 0.2.0 authority. It authorizes one comparison pass and one later evidence artifact, not a mechanism selection or an ongoing research program.

The evaluation must begin with current repository authority, the [completed 0.2.0 evidence](evidence/DEC-0011/bounded-verification-mechanism-evaluation.md), and its attributable sources. External static evidence may be consulted only where needed to compare these same two categories. It must label repository authority, external guidance, documented fact, inference, analytical assumption and unresolved question separately. Bounded hypothetical constructions may make the comparison meaningful, but they are **evidence models**, not selected production designs. Analytical variables or ranges are not repository requirements or measurements. No user research, operational metric, security measurement or Product preference may be invented.

### Common semantic baseline and comparative dimensions

Both candidates must be assessed against the same Accepted DEC-0008 and Approved IEVE semantics: stable Identity and exact authoritative current-email binding; proof of address control only; Identity-owned accepted evidence rather than dispatch, delivery, opening or provider success; one-use effect, replay resistance, reissue and email-supersession handling; point-of-reliance currentness; fail-closed invalid, stale, conflicting, unavailable and uncertain evidence; and no Authentication, Principal, Session, association, Authorization or recovery inflation.

The comparison must address for **each** candidate: guessing resistance; proof-secret disclosure; mailbox compromise, forwarding and phishing; unintended presentation/opening; URL, browser history, referrer and log exposure where applicable; replay, duplicate and concurrent presentation; resend/reissue, concurrent reissue and older proof after reissue; authoritative-email supersession and stale proof; source unavailability and conflicting evidence; provider delay, retry and failure; enumeration and abuse/flood resistance; confidentiality and telemetry; accessibility and same-device versus cross-device use; deterministic testability; operational support and recovery; provider independence; dependency, Architecture and Contract implications; persistence/state implications; and reversibility. Inapplicability must be explained, not silently omitted. Both candidates must be compared under equivalent binding, currentness and failure obligations rather than unequal safeguards.

The evaluation must examine whether one-use, immediate reissue invalidation, supersession, concurrency, replay control and immediate invalidation require authoritative server-side state/evidence even when a proof is derived or signed. It must distinguish that **semantic** requirement from a physical persistence choice. It may not select tables, columns, indexes, repositories, SQL, transactions, locks or isolation levels. It must identify whether an existing repository Requirement, accepted Product policy, Security Requirement, Architecture constraint, IEVE requirement or operational constraint materially favors one category. If none does, it must say so. A deciding Product preference not yet governed is a dependency for later governance, not a preference the evaluator may invent.

### Evidence artifact, stop conditions and terminal outcomes

The later execution may create **exactly one** durable artifact at `specifications/decisions/evidence/DEC-0011/bounded-comparative-mechanism-evaluation.md`. This 0.4.0 authorization does **not** create it. The artifact must record actual sources and classification, common assumptions, symmetric comparison, threat and negative scenarios, any discriminator or its absence, state/evidence implications, gaps, encountered stop conditions, exactly one terminal outcome and authorization consumption. It must not rewrite the prior evidence.

The execution **must stop rather than expand** if credible comparison requires an ungoverned Product policy/preference, executable prototype, provider selection, dependency admission, material Architecture decision, external Contract definition, invented assumption to claim a discriminator, a third candidate, or reconciliation of a material conflict with an Accepted decision that cannot be resolved within this scope. A need for executable experimentation is a stop, not implicit prototype authority. No production implementation, migration, dependency installation, provider integration, Contract/OpenAPI change or prototype is authorized.

The execution must terminate with **exactly one** of:

- **A — CANDIDATE A DISCRIMINATOR DEMONSTRATED:** attributable evidence establishes a repository-specific basis for preferring the short user-entered one-time code.
- **B — CANDIDATE B DISCRIMINATOR DEMONSTRATED:** attributable evidence establishes a repository-specific basis for preferring the high-entropy verification link/token.
- **C — NO REPOSITORY-SPECIFIC DISCRIMINATOR DEMONSTRATED:** both remain plausible and current evidence cannot justify choosing one.
- **D — STOPPED / INCONCLUSIVE:** a stop condition is reached before a defensible comparative result.

Starting and completing the one execution, or stopping at any terminal outcome, **CONSUMES / EXHAUSTS** this 0.4.0 authorization. Unused source, candidate or time capacity is not reusable. No continuation, second evaluation, candidate substitution, prototype or implementation follows automatically. Even Outcome A or B permits only a separately reviewed mechanism-selection/acceptance-readiness governance step; none of A/B/C/D selects a mechanism, Accepts DEC-0011 or authorizes implementation. Any future evaluation after exhaustion needs fresh explicit governance authorization.

### Explicit non-authority and preserved gates

This revision selects neither code nor link/token, hybrid, stateful nor stateless physical representation, and establishes no production security envelope. It fixes no code/token length or entropy, expiry, attempt, resend or rate value, cryptographic primitive, storage form, provider, endpoint, DTO, schema, cache, queue or transaction/isolation strategy. It admits no provider or dependency, changes no Architecture or Contract, creates or Accepts no ADR, and authorizes no Authentication, Principal, Session, Identity/Customer association, Registration or Login implementation. DEC-0001 dependency admission, material Architecture/ADR, provider/integration and Contract/OpenAPI changes remain separately governed; later DEC-0011 acceptance still requires applicable Security and Architecture approval.

DEC-0008 remains authoritative and IEVE remains Approved. The current-login-email source remains binding/currentness infrastructure, **not** verification or accepted verification evidence, and still lacks a production population issuer; this authorization does not create one. DEC-0009 and DEC-0010 remain Proposed/deferred with their earlier evaluation authorizations exhausted; this revision neither reopens nor resolves them. Registration and Login implementation eligibility remains unestablished.

## Completed Comparative Evaluation and 0.5.0 Proposed Disposition

The single 0.4.0 comparative evaluation was executed once and recorded **C — NO REPOSITORY-SPECIFIC DISCRIMINATOR DEMONSTRATED** in the [durable comparative evidence](evidence/DEC-0011/bounded-comparative-mechanism-evaluation.md). Candidate A, the short user-entered one-time code, and Candidate B, the high-entropy verification link/token, remain plausible but unselected. No hybrid is selected. The result establishes no repository-specific preference, production security envelope or acceptance readiness; it is not a finding that either candidate is universally infeasible.

**The 0.4.0 authorization is CONSUMED / EXHAUSTED.** It permits no continuation, repetition, candidate substitution, another evidence run, mechanism selection or implementation. The earlier 0.2.0 authorization remains separately **CONSUMED / EXHAUSTED**. Its Outcome A established evidence sufficiency for later review, not a winner; the subsequent read-only selection review's **C — NO DEFENSIBLE MECHANISM SELECTION FROM CURRENT EVIDENCE** and the completed comparative Outcome C are consistent with that history. No fresh evaluation is authorized by this synchronization. Any future mechanism selection requires fresh attributable governance basis and required Security and applicable Architecture review. DEC-0011 remains Proposed/deferred, non-authoritative and unimplemented.

## Historical Single Bounded Documentation/Static-Evidence Evaluation Authorization — 0.2.0 Proposed (Consumed / Exhausted)

The original scope and stop rules below are retained to explain the completed evaluation. Their imperative wording records the historical authorization and grants no authority to execute it again.

### Question, ownership and finite scope

Version 0.2.0 permitted **exactly one** finite, non-production documentation/static-evidence evaluation answering: **Which evaluated initial Customer email-verification proof category, if any, demonstrates a sufficient security, lifecycle, operational and governance basis to justify a later DEC-0011 mechanism-selection and acceptance review for proving control of the exact authoritative current Customer login-email binding?** That evaluation did not reconsider DEC-0008's mandatory policy or redefine IEVE evidence semantics. It gathered evidence only; it did not select or Accept a mechanism. Its authorization is now consumed/exhausted.

The single execution must compare (A) a short user-entered one-time code and (B) a high-entropy verification link/token. A hybrid or other bounded possession-proof category may be considered only if materially distinct security or lifecycle characteristics cannot be represented by evaluating A and B separately; unused hybrid capacity creates no further authority. Stateful versus derived/stateless proof representation is a cross-cutting dimension where applicable, not automatically another mechanism category. Each viable arrangement must be assessed against existing repository trust, ownership and evidence boundaries.

### Required comparison and security envelope

For each evaluated category, record attributable evidence or an explicit gap for all applicable dimensions:

| Dimension | Required analysis |
| --- | --- |
| Binding and interaction | Exact stable Identity and authoritative current-email fact/value; issuance, presentation and acceptance; generation and unpredictability; resistance to guessing and brute force; proof confidentiality and integrity. |
| Lifecycle and effects | Bounded validity and expiry where applicable; replay; single-use or explicitly justified repeat effects; duplicate initiation, delivery and presentation; resend/reissue and the relationship between old and newly issued proof; authoritative-email supersession and stale-proof rejection. |
| Concurrent authority | Concurrent presentation and reissue, intervening binding change, durable accepted-evidence provenance, stateful versus derived/stateless implications, persistence needs, stale observation, point-of-reliance currentness, conflict, uncertainty and reconciliation. |
| Abuse, privacy and operations | Attempts and guessing controls; abuse/rate controls and flooding; enumeration and response-shape risks; sensitive URL, log and telemetry exposure; provider delay, retry and failure separated from verification acceptance; operational recovery, migration and replacement. |
| Governance and testability | Provider independence; dependency, Architecture and Contract implications; deterministic positive and negative scenarios; DEC-0008/IEVE compatibility; separation from Authentication, Principal, Session, Authorization, recovery, email change and MFA authority. |

Where security depends materially on quantities, identify the **security envelope a later acceptance must fix**: entropy and proof-space size, generation requirements, validity bounds, guessing/attempt controls, and replay and reissue invalidation/effect semantics where applicable. An exact implementation value may remain a later implementation detail only if a precise, testable accepted envelope can govern it. The evaluation must not select final production values merely by analyzing them.

The threat/failure analysis must explicitly address proof guessing, brute force, theft, replay, cross-Identity and cross-email substitution, stale proof after authoritative-email supersession, duplicate delivery and presentation, concurrent presentation and reissue, old proof after reissue, provider delay/retry/failure, persistence failure, partial write, stale observation, conflicting evidence, enumeration, abuse flooding, expiry-boundary behavior where applicable, and uncertainty at the point of reliance. Unsupported claims remain gaps, not positive results.

### Permitted evidence and durable artifact

The execution may use attributable static/documentary evidence from repository governance and implementation, relevant security standards, standards bodies, authoritative protocol/security documentation, vendor-neutral guidance, official platform/runtime documentation, and peer-reviewed or otherwise reputable security literature. It must distinguish repository fact, attributable external fact, documented security property, already available measured evidence, assumption and unresolved question; assumptions cannot be described as measurements. The current-email source may be modeled with synthetic facts for this comparison. **Production population of that source is not required or authorized.** Its publication, observation and persisted rows remain neither verification nor accepted proof of email control.

The completed execution created only `specifications/decisions/evidence/DEC-0011/bounded-verification-mechanism-evaluation.md` as its durable evidence artifact. The 0.2.0 authorization revision did **not** create it; this 0.3.0 revision records its completion. The artifact identifies the authorization, question, actual categories, sources, threat model, per-category and quantitative-envelope analysis, stateful/stateless implications, provider/dependency/Architecture/Contract implications, assumptions, unresolved questions, stop-condition result, terminal outcome, rationale and authorization-consumption state. An evidence artifact is not a Decision Record or implementation authority.

### Prohibited expansion and stop conditions

No prototype or spike is authorized. The execution must not add or change production code, test code for an implementation/prototype, migrations, schemas, dependencies or versions, a provider/SDK/integration, email delivery, queue or cache, Contracts/OpenAPI, endpoints or DTOs. It must not create a current-email production issuer, Authentication orchestration, Principal or Session issuance, Identity/Customer association or Registration implementation, recovery, email change or MFA implementation. It must not evaluate or resolve DEC-0009 or DEC-0010. Conceptual evaluation admits no dependency under DEC-0001 and selects or ranks no email provider. Provider-facing needs and possible later external verification interaction Contracts may be identified without inventing an endpoint, method, DTO or response. No ADR is created by this authorization; a candidate's material Architecture implication must be identified for a separately governed ADR and canonical synchronization if later selected.

**Stop rather than expand** if static/documentary evidence cannot answer a required question; a prototype, new dependency, provider selection, material Architecture decision, Contract design or production implementation becomes necessary; material evidence conflict cannot be resolved in scope; DEC-0009/DEC-0010 resolution would be required; or the candidate/dimension boundary would be exceeded. A need for a spike requires a fresh governance revision, not an implicit exception.

### Exactly three terminal outcomes and exhaustion

The execution must terminate with **exactly one** of:

- **A — EVIDENCE SUFFICIENT FOR LATER SELECTION REVIEW:** at least one evaluated category has a sufficiently supported, repository-compatible security and lifecycle basis for a later DEC-0011 selection/acceptance review. This is neither selection nor acceptance.
- **B — REQUIRED SECURITY BASIS NOT DEMONSTRATED:** the evaluated categories do not demonstrate a sufficient basis for later selection within this authorization's available evidence. This candidate/evidence-specific conclusion is not universal infeasibility without proof of that broader claim.
- **C — STOPPED / INCONCLUSIVE:** required evidence is unavailable, contradictory or outside the static boundary, or answering responsibly requires a prototype, dependency, provider, Architecture decision or other new authority. C permits no continuation.

Authorization permits **one execution only**. Starting and completing that execution, any terminal stop, and each A/B/C outcome consumes/exhausts it. Unused candidate, source or time capacity does not survive completion. No retry, continuation, second evaluation, candidate substitution or material scope expansion is authorized; any future evaluation requires fresh explicit governance authorization.

### Acceptance remains separate

DEC-0011 remains Proposed after this synchronization unless a later controlled decision explicitly changes its status. Outcome A does not Accept it. A later review must decide whether evidence supports a concrete mechanism and testable security envelope, with applicable Security and Architecture authority; any material Architecture choice needs a separate ADR, any new artifact separate DEC-0001 admission, and any provider/integration or external Contract its own applicable governance. The accepted DEC-0008 policy and Approved IEVE semantics remain unchanged. DEC-0009 and DEC-0010 remain Proposed/deferred with exhausted prior authorizations; the completed evaluation reopened neither.

## Threat and Failure Evidence Required Before Acceptance

A future threat model and deterministic scenarios must cover proof guessing and brute force; proof theft, tampering and logging disclosure; cross-Identity and cross-email substitution; stale proof after binding supersession; duplicate delivery and presentation; concurrent presentation and reissue; old proof after reissue; provider delay, retry and failure; persistence failure, partial write and stale read; conflicting evidence; enumeration and abuse flooding; and clock/expiry boundaries if validity depends on time. It must distinguish actual tested behavior, design assertions, assumptions and unresolved risk. This record claims that none of these controls has been implemented by DEC-0011.

## Acceptance-Readiness and Separate Gates

Before DEC-0011 could become Accepted, a later revision must identify a concrete mechanism and accountable Identity owner; record a threat model and realistic alternatives comparison; define the exact subject/current-email binding, proof issuance/presentation/acceptance lifecycle, validity and invalidation, replay/repeated effects, attempts and abuse, secret handling, provider/delivery boundary, durable evidence and source observation, concurrency and supersession, fail-closed outcomes and reconciliation. It must provide privacy and enumeration analysis, deterministic negative scenarios, operational and support responsibilities, migration/replacement implications, and a test strategy aligned with DEC-0008 and IEVE. Residual risks and limitations must be explicit. Security authority review is required; Architecture authority review is required where material Architecture is introduced.

A material Architecture choice requires its own ADR and, if accepted, canonical Architecture synchronization; DEC-0011 cannot silently select a new Architecture. Any new library or dependency requires separate DEC-0001 admission. Provider selection or external integration requires its applicable provider/integration review, privacy and operational assessment, and any separately required decision. Acceptance of this Security Decision would not itself admit a dependency or provider, approve an external Contract, authorize a migration, or establish production readiness. Production implementation needs separate implementation-readiness review of the real lawful current-email population, accepted producer, source-backed consumer, applicability, failure and privacy paths.

The single 0.2.0 documentation/static-evidence evaluation above is complete and its authority exhausted. It is not a mechanism selection, acceptance, dependency/provider admission or production implementation. Unresolved evidence blocks selection rather than inviting an implicit experiment or fallback.

## Relationship to Current Flows

The current-email source is infrastructure for binding and currentness observation, not email-verification evidence. Publication and observation are not proof of control; its persisted email state has no production population issuer. DEC-0011 does not create that issuer.

For Login, submitted email → strict canonical key → stable Identity resolution remains dependent on DEC-0009. Even a future accepted verification outcome satisfies only the DEC-0008 gate; password evidence, complete Authentication, Principal and Session establishment, association and contextual Authorization remain separate.

For Registration, DEC-0008 requires accepted verification of the current authoritative email before completion. This proposal does not resolve DEC-0010, establish a credential or Customer/Account association, coordinate completed Registration, or change either Contract. Login-email change, recovery, password reset and Customer MFA remain outside this initial decision. Verification or mailbox control must not silently become recovery authority.

## Consequences and Reversibility

This Proposed boundary makes the missing Security choice and required evidence durable, prevents ad-hoc proof acceptance, and records the completed 0.2.0 and 0.4.0 evaluations without renewing either exhausted authorization. Implementation remains blocked and evidence and review work remain. Premature selection could create weak guessing or replay semantics, provider coupling, authority inflation, recovery bypass, or privacy and enumeration exposure; none is accepted as a trade-off here. This proposal is readily reversible because it selects no mechanism and creates no production state. Any future accepted mechanism must assess migration, replacement and retained-evidence consequences before selection.

## Explicit Non-Decisions

DEC-0011 0.5.0 selects no code versus link versus other proof; numeric or alphanumeric format; length, entropy, cryptographic construction, hashing or storage representation; validity duration; attempt, resend or rate values; provider, vendor, SDK, dependency or delivery technology; schema, table, column, index, cache, queue or event infrastructure; endpoint, DTO, status or public/internal Contract change; recovery, password reset, email-change or MFA behavior; Authentication, Principal, Session or association implementation. It admits no dependency or provider, changes no Product or Architecture baseline, resolves neither DEC-0009 nor DEC-0010, and authorizes no further evaluation, prototype or production implementation. The 0.2.0 and 0.4.0 evaluation authorizations remain separately consumed/exhausted.

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
| 0.5.0 | 2026-10-10 | Proposed | Synchronized the completed single-use 0.4.0 comparative evaluation and its durable C — NO REPOSITORY-SPECIFIC DISCRIMINATOR DEMONSTRATED; recorded the 0.4.0 authorization CONSUMED / EXHAUSTED while preserving the separately exhausted 0.2.0 Outcome A and the consistent read-only selection review; kept both candidates and any hybrid unselected and DEC-0011 Proposed/deferred, without renewed evaluation, acceptance, implementation or Product/Architecture/Security/Contract authority. |
| 0.4.0 | 2026-10-10 | Proposed | After the completed 0.2.0 evaluation's Outcome A and 0.3.0 synchronization, recorded the read-only selection review's C — NO DEFENSIBLE MECHANISM SELECTION FROM CURRENT EVIDENCE; authorized exactly one new finite, single-use, static comparative evaluation of code and link/token under common repository semantics, explicit A/B/C/D exits and stop/exhaustion rules; selected no mechanism, production envelope, provider, dependency, Architecture or Contract, did not Accept DEC-0011 and authorized no prototype or implementation, while preserving DEC-0008/IEVE and unresolved DEC-0009/DEC-0010. |
| 0.3.0 | 2026-10-10 | Proposed | Synchronized the completed single-use 0.2.0 documentation/static-evidence evaluation and its durable Outcome A — EVIDENCE SUFFICIENT FOR LATER SELECTION REVIEW; recorded the authorization CONSUMED / EXHAUSTED, with no new evaluation authority, mechanism or security-envelope selection, acceptance, provider/dependency/Architecture/Contract approval or implementation authority; preserved DEC-0008/IEVE and unresolved DEC-0009/DEC-0010. |
| 0.2.0 | 2026-10-09 | Proposed | Authorized exactly one finite documentation/static-evidence comparison of initial current-email proof categories, with bounded candidates and dimensions, explicit A/B/C outcomes, stop and exhaustion rules, and separate acceptance, dependency, provider, Architecture and Contract gates; performed no evaluation, selected no mechanism, and authorized no implementation while preserving DEC-0008/IEVE and unresolved DEC-0009/DEC-0010. |
| 0.1.0 | 2026-10-09 | Proposed | Established the Identity-owned Security decision question, alternatives, threat and acceptance-evidence boundaries for an initial current-email control-proof mechanism; selected no mechanism, provider, dependency or persistence design, authorized no evaluation or implementation, and preserved DEC-0008/IEVE authority and unresolved DEC-0009/DEC-0010 prerequisites. |
