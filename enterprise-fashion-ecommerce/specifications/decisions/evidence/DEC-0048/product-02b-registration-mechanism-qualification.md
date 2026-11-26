# PRODUCT-02B registration mechanism qualification evidence

- **Status:** Draft
- **Owner:** Identity — accountable disposition pending
- **Date:** 2026-11-26
- **Authoritative:** false
- **Baseline:** `d0f350498f12b2da9780692f73e738e6d3bb047b`

## Authorization and boundaries

The original attributable source is the requester’s conversation message beginning “I confirm AUTH-01, AUTH-02, AUTH-03 and AUTH-04 as the project founder.” It explicitly authorized bounded technical evaluations and coordinated producer design, then limited that permission to evidence collection and governance preparation. The subsequent confirmation repeated the scope subject to the proposed conditions, amendments and exclusions. The messages establish what the requester authorized in their stated founder capacity. Independent verification of that capacity and attributable specialist concurrence are not established. The earlier technical proposals supply no authority by themselves. No mechanism acceptance, dependency or dataset admission, production implementation or release authority follows. Prior strict IDNA2008, blocklist and verification authorizations retain their historical exhaustion status. Temporary inputs and harness output were cleaned up. This record distinguishes original conversation evidence, retained tool observations and reconstructed methodology.

## AUTH-01 — ICU4J evidence

| Input | SHA-256 | Basis |
| --- | --- | --- |
| ICU4J 79.1 JAR | `28b292e60d0d00c496bae88a15b2615f89f781be13670aeac9dc90f189570e07` | Maven Central bytes matched the official Unicode GitHub release asset digest. The registry SHA-256 sidecar returned 404. |
| ICU4J 79.1 POM | `8586d64603f6d4bc62df87ef919a3c52d9c9f373d24e3abe3b727375d3d26178` | Observed digest. POM declares Unicode-3.0. Publisher signature and distribution notice completeness remain unverified. |
| Unicode 18.0.0 IdnaTestV2.txt | `0236b75c5b20dfd857b3b5cf75509887959d6ab00d6c7bda9b7dc3df518c1fde` | Observed from Unicode's published vector file and matched on the follow-up. |

The historical Java 21 macOS scratch run used `javac --release 21 -cp icu.jar Qualification.java` and `/usr/bin/time -l java -Xmx256m -cp .:icu.jar Qualification`. The targeted classification used `java -Xmx256m -cp icu.jar Triage.java` with the pinned vector file in its working directory. Runtime Unicode was 18.0.0.0. The run reported 21 supplemental domain checks passed, 92.7 ms for 20,000 warm mixed checks, 0.89 s process elapsed and 128,942,080 bytes peak RSS. These were workstation measurements.

**Reconstruction method.** Verify the input digests before execution. Read UTF-8, strip comments, split semicolon columns with empty fields preserved, strip column spaces and tabs, decode vector escapes and distinguish quoted empty strings from fallback fields. Inherit blank ToUnicode output from source, blank ToASCII non-transitional output from ToUnicode and blank ToASCII status from ToUnicode status. Compare expected successful output and acceptance using explicit non-transitional, STD3, Bidi and CONTEXTJ options. Full standard conformance would additionally compare the other applicable operations, status semantics and replacement-character behavior. The historical comparison did not claim that scope. CONTEXTO is tested separately with the proposed email profile, which also rejects alternate separators, malformed Unicode and root notation.

| Comparison | Expected | Observed | Classification |
| --- | --- | --- | --- |
| 68 of 6,396 ToASCII rows | Error `A4_2` under the published vector's DNS length expectations | Matching ASCII string ending in a root dot, with no ICU error | Aggregate classification reported by the follow-up run. Individual classifications are not independently verified from retained row evidence. Application output rejection is proposed and application-profile closure remains untested across the 68. |
| Other 6,328 rows | Applicable Boolean acceptance and successful ASCII output | Matched under the scratch comparison | No remaining disagreement in the measured dimensions. |

Exact historical row trace unavailable. The initial output retained examples 273–279, 362 and 377–380 (one-based physical lines). The original follow-up tool output (cell 2122, session 39399) reported all 68 in one A4_2/root-dot group and zero outside that group. Only its aggregate is available. No exact historical per-row list is asserted. At that revision, the exact inventory, application output guard and complete email bounds remained open. The corrective evidence below records the later run separately.

[Release asset digest](https://github.com/unicode-org/icu/releases/expanded_assets/release-79.1) · [Release license](https://github.com/unicode-org/icu/blob/release-79.1/LICENSE) · [Pinned vectors and column definitions](https://www.unicode.org/Public/18.0.0/idna/IdnaTestV2.txt)

## AUTH-02 — Blocklist evidence

Generated only. The Python scratch harness normalized complete values to NFC, indexed sorted exact SHA-1 digests and tested membership with binary search against a memory-mapped file. It ran 16,000 warm checks on 100,001 entries (2,000,020 bytes) with 16 Python workers, p95 0.0024 ms, p99 0.0038 ms and peak RSS 64,028,672 bytes. It rejected checksum mismatch, truncation, stale and incomplete input. A rejected staged file left the active index intact and atomic replacement preserved the prior reader's view. The original AUTH-02 follow-up tool call ran an inline Python fixture and reported two generated ranges accepted and missing range, zero count, truncated line and duplicate row rejected (tool chunk cb63af). It used strictly formatted, unpadded positive-count synthetic rows in memory. No provider acquisition or real-source range completeness was tested. Reproduce with generated values, a fixed-seed mixed hit/miss workload, a stale cutoff fixture and atomic replacement in a disposable directory. No JVM performance, cold full-corpus behavior, opaque-hash NFC coverage or real-source acquisition was demonstrated.

## AUTH-03 — Email proof evidence

Python in-memory fake delivery exercised eight-digit code and 128-bit link candidates using keyed comparison, exact continuation/subject/email-fact/generation inputs, simulated expiry and resend clocks, and a lock around acceptance. Both rejected navigation, stale binding, replay and replaced challenges. One of 16 concurrent submissions succeeded per mode. The code case preserved leading zeros. The later AUTH-03 follow-up execution (tool cell 2127, session 86998) used the founder’s bounded follow-up authorization for synthetic transaction evidence. It started the existing PostgreSQL 18 image with `docker run --pull=never --network none --rm -d -e POSTGRES_HOST_AUTH_METHOD=trust postgres:18` and executed synthetic SQL via `docker exec -i <container> psql -X -qAt -v ON_ERROR_STOP=1 -U postgres`. It used code and link labels representing already-validated proofs. One statement conditionally updated a challenge and inserted acceptance from the returned row. For each candidate, explicit rollback left zero acceptance and an unconsumed challenge. Eight concurrent attempts produced one committed row and retry produced no second effect. The scratch container was removed. This later result is isolated transaction-shape evidence. The earlier in-memory report left integrated durable verification unverified, and that limitation still applies. Browser accessibility, distributed abuse, commit ambiguity recovery, provider delivery and integrated verification remain untested.

## AUTH-04 — Proposed producer and contract disposition

Customer issues a protected, scoped browser continuation before submission and binds a server-generated intent to the operation-scoped idempotency key. Loss of the continuation never permits recovery from the key alone. Identity binds credential input at first durable acceptance. Compatible retry may compare only the original eligible Argon2id verifier. Unavailable, withdrawn or superseded original state remains incomplete or uncertain. Customer stores no password or derived retry fingerprint. Keep the proposed 24-hour continuation and seven-day idempotency windows separate from reservation, proof and accepted-fact retention.

Identity checks retained ownership first. A provisional reservation binds canonical key, intent, continuation, proposed subject, exact prospective email fact, reservation revision and the applicable credential acceptance reference. Give it a hard deadline with no extension by resend. Invalidate the previous challenge on rotation, replacement, expiry or destination change. Apply destination, intent, continuation, source and global abuse limits. Retained ownership is never released by expiry. Propose a mailbox-holder recovery attempt that can replace an unpromoted reservation only after proof tied to the holder's own continuation and intent, under bounded destination and source controls. Do not inherit the earlier credential. A displaced or expired attempt's credential remains ineligible and enters governed owner-local abandonment handling with protected audit and retention. Pending review must settle reservation duration, renewal prohibition and recovery/anti-starvation budgets without creating an existence oracle.

Within one Identity transaction, recheck retained ownership, reservation revision, intent, continuation, subject, exact email fact, proof generation and applicable first credential binding. Consume proof, promote that exact owned fact and record durable verification against it. Never activate any credential from a different intent. Resolve an uncertain commit by observing the Identity outcome before retry. Customer separately accepts its own Customer and Account outcomes and completes only from independent current owner evidence.

Update registration Markdown sections 5–8, 13–21 and 25–27 and the paired OpenAPI in a later authorized change. Replace full-request fingerprint wording with a pre-acceptance/first-durable-binding rule. Specify continuation issuance and cookie custody, same-origin and CSRF behavior, authorized conflict disclosure, concealed pending, recovery and expiry. The minimum verification pair defines proof submission, controlled resend/recovery, safe outcomes, no-store and correlation. It confers no Authentication, Session or Account access. At qualification close, Customer, Identity, Security, Architecture, Product and applicable Data/Privacy dispositions were pending. See the subsequent accountable response below.

## PRODUCT-02B.2 corrective and browser qualification closure

The original **PRODUCT-02B.2 — Founder Disposition** message confirms RES-01 through RES-10 in the requester’s stated founder capacity. It authorizes one checksum-pinned corrective ICU evaluation, one fake-delivery browser qualification, and coordinated governance preparation under the stated exclusions. The two execution authorizations are **CONSUMED**. The browser syntax correction occurred before Chrome executed. This record confers no independent specialist concurrence, acceptance, dependency or operational dataset admission, real acquisition, implementation, public activation or release authority.

### Corrective ICU run

Original tool cell 2147, session 70562, completion chunk 0f2dfb. The run downloaded the exact JAR, POM and Unicode vector file into scratch and verified all three SHA-256 pins in AUTH-01 using `shasum -a 256 -c`. It compiled `Triage.java` with `javac --release 21 -cp icu.jar Triage.java`, then invoked `/usr/bin/time -l java -Xmx256m -cp .:icu.jar Triage vectors.txt` under a Python subprocess timeout of 1,800 seconds. The environment reported Temurin 21.0.6 and ICU Unicode 18.0.0.0. The actual harness used the standard comparison flags from AUTH-01, then applied CONTEXTO and the proposed application restrictions separately. Scratch files were removed.

- Evaluated 6,396 non-transitional ToASCII rows and reconstructed 68 A4_2 differences, with zero unexplained differences in the measured comparison.
- For every row listed below, the effective expected status was A4_2, ICU returned the same expected ASCII output ending in a root dot without an error, and the application profile rejected it. These are one-based physical vector line numbers from the original corrective output.
- Passed 21 supplemental checks, including mapped root-dot rejection, address and label bounds, malformed Unicode, contextual and Bidi rules, A-label round trips, locale determinism and oversized input rejection.
- JVM wall time was 0.45 s, peak RSS was 190,152,704 bytes, and 20,000 warm mixed checks took approximately 226 ms. These are workstation measurements.

```text
273, 274, 275, 276, 277, 278, 279, 362, 377, 378, 379, 380, 381,
577, 578, 666, 667, 668, 669, 670, 671, 672, 682, 683, 684, 685,
2800, 2801, 2950, 2951, 3436, 3437, 3448, 3449, 3450, 3997, 3998,
3999, 4000, 4222, 4223, 4577, 4578, 4579, 4580, 4581, 4644, 4645,
4646, 4786, 4787, 4905, 4906, 4907, 4908, 4917, 4944, 4945, 4946,
5247, 5248, 5249, 5494, 5495, 6288, 6289, 6482, 6483
```

**PASS for the demonstrated application profile.** Full UTS #46 conformance, dependency admission, publisher authentication and distributed notice review, and retained-identifier compatibility were not established. This is corrective evidence, separate from the unavailable original row trace.

### Entered-code browser run

Original cell 2150, chunk 1eb6fc, failed JavaScript parsing before Chrome started. Corrected cell 2153, session 87091, chunk 9eb98c, ran `node --check run.mjs` and `CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' node run.mjs`. It used a disposable local browser profile, fake delivery and a synthetic HTML fixture. Chrome's accessibility tree exposed the input name and status/alert roles. Native Tab and Enter, paste-equivalent text insertion, leading zeros, wrong-code focus recovery, expiry, resend feedback and spacing passed. Initial navigation had no proof effect, the fixture used no proof in the URL, and no subresource request was observed. No actual screen-reader interaction, OS clipboard paste, second-device delivery, mail scanner, distributed throttling or deployed telemetry was exercised. The scratch profile and files were removed. Prior fake-delivery and PostgreSQL evidence remains separate.

## Coordinated adoption request — DISP-01 through DISP-12

The request below identifies the affected capacities and governing sources. The subsequent accountable response is recorded after the table. Approval must match the affected authority under [DECISIONS.md §§16–19](../../../../.ai/core/DECISIONS.md#16-decision-ownership).

| ID | Accountable capacity and source | Requested disposition | Gate |
| --- | --- | --- | --- |
| DISP-01 | Identity, with Product, Security, Architecture, Customer and applicable Data/Privacy per DEC-0005 acceptance governance and DEC-0048 transition conditions | Adopt the exact qualified profile and canonical bounds, supersede DEC-0005 through synchronized acceptance, and preserve history. | Controlled canonicalization |
| DISP-02 | Engineering with Security and applicable licensing review, DEC-0001 and SECURITY-STANDARDS | Admit exact ICU4J 79.1 only after artifact, signer/trust, notices, compatibility, lock and verification review. | Controlled adapter using ICU |
| DISP-03 | Identity and Security, Engineering review, DEC-0010 BL-03 | Authorize generated-fixture Java screening and isolated credential-denial tests. | Controlled screening |
| DISP-04 | Identity, Security, Platform and applicable source-rights/Data/Privacy, DEC-0010 BL-02/04 | Approve exact source rights, acquisition, coverage, freshness, provisioning and operational admission separately. | Operational screening |
| DISP-05 | Identity and Security, applicable Architecture and Customer with accessibility review, DEC-0011 | Adopt the proposed selection-gate split, assess basic accessibility evidence, review guessing/lockout and secret exposure, then select entered codes only if criteria are met. | Controlled challenge |
| DISP-06 | Security and Identity, DEC-0011 and SECURITY-STANDARDS | Review exact proof binding, keyed verifier and key lifecycle, recovery-budget exhaustion, destination/source/intent/global limits and distributed abuse. Require independent cryptographic review where applicable. | Controlled security design. Operational validation remains separate. |
| DISP-07 | Customer, ADR-0022 and CRIO | Adopt protected continuation, local intent and key scope, Customer and Account outcomes and completion coordination. | Controlled Customer persistence |
| DISP-08 | Identity, ADR-0022, CLSP and IEVE | Adopt provisional reservation, first durable credential binding, original eligible verifier retry, challenge and atomic proof promotion, association, and immediate abandoned-credential ineligibility. | Controlled Identity producers |
| DISP-09 | Architecture with Customer and Identity, ADR-0022 | Confirm bounded owner commands and observations, owner-local transactions, uncertainty recovery and no cross-owner database access. | Controlled coordination |
| DISP-10 | Product with Customer and Security, registration contract and DEC-0008 | Confirm concealed incomplete outcomes and the retry and mailbox-holder recovery experience. | Controlled contract semantics |
| DISP-11 | Applicable Data/Privacy with Identity and Customer, registration contract audit and owner-local retention | Decide abandonment retention, proposed seven-day verifier destruction, authorized holds, and protected audit and repair retention. | Controlled abandonment policy. Retention operations require readiness evidence. |
| DISP-12 | Identity, Customer, Security and applicable operational owners, DEC-0048 transition conditions and DEC-0011 activation gate | Confirm retained-identifier cutover and collision evidence, provider admission and real delivery/recovery, deployment and public activation separately. | Operational activation |

### Accountable response recorded 2026-11-27

The attributable source is the project founder’s instruction beginning “As project founder and the appointed internal accountable owner, I confirm the consolidated DISP-01 through DISP-12 dispositions”. The founder acted in the applicable internal capacities recorded in [DECISIONS.md §16.1](../../../../.ai/core/DECISIONS.md#161-recorded-founder-led-internal-appointment), as mapped above. This is one individual's internal disposition. Mandatory independent concurrence, specialist review and operational gates remain separate.

| ID | Received disposition | Effect and remaining conditions |
| --- | --- | --- |
| DISP-01 | CONFIRM | Internal confirmation of the qualified canonicalization profile, subject to applicable concurrence and lawful synchronization. DEC-0005 remains effective pending synchronized acceptance and transition. |
| DISP-02 | CONDITIONAL | ICU4J 79.1 admission remains pending exact dependency admission, including artifact and signer trust, licensing and notices, compatibility, locking and verification. |
| DISP-03 | CONFIRM | Adopt DEC-0010 BL-03 only for generated-fixture Java screening and isolated credential-denial tests. Implementation may begin after this governance change is integrated. |
| DISP-04 | DEFER | Operational password corpus acquisition, qualification and admission remain gated. |
| DISP-05 | CONFIRM | Internal confirmation of the proposed selection-gate amendment. Existing selection authority remains in force pending amendment adoption and applicable accessibility review. Entered-code selection remains conditional on the mandatory selection criteria. |
| DISP-06 | PARTIAL CONFIRM | Preserve proof binding, secret custody and fail-closed behavior as design requirements. Independent cryptographic review and unresolved guessing, lockout, recovery-budget and distributed abuse controls remain prerequisites. |
| DISP-07 | CONFIRM | Adopt the Customer-owned producer direction internally. After integration, controlled owner-local Customer and Account relationship persistence and synthetic tests may proceed under DISP-09. Live continuation, producer invocation and completion coordination remain gated. |
| DISP-08 | CONFIRM | Confirm Identity producer boundaries subject to the outstanding mechanism and security prerequisites. Identity producer implementation remains gated. |
| DISP-09 | CONFIRM | Adopt owner-local transactions, bounded owner contracts and authoritative reconciliation as constraints. Preserve authoritative owner observations and prohibit cross-owner storage access. Cross-owner coordination requires effective producer contracts. |
| DISP-10 | CONFIRM | Confirm proposed customer-visible registration, retry and recovery semantics. Keep the existing contract and full-request fingerprint until synchronized replacement is effective. |
| DISP-11 | PARTIAL CONFIRM | Confirm immediate ineligibility of abandoned credentials as a required invariant. Retention, holds and permanent destruction remain subject to Data/Privacy review and effective owner-local policy. |
| DISP-12 | DEFER | Retained-identifier cutover, operational provider and delivery, and public activation remain gated. |

The two bounded implementation slices are BL-03 and isolated Customer/Account persistence under DISP-07 and DISP-09. Both depend on integration of the governing changes. The registration Markdown and OpenAPI, producer contract designs and effective canonicalization rules remain unchanged while their prerequisites are open. Public registration, Authentication, Session issuance and production release remain outside this disposition.

### Producer parameters and contract synchronization

Adopt a 24-hour hard continuation, seven-day idempotency binding and 30-minute hard provisional reservation with no sliding renewal. Clip challenge expiry to reservation expiry. Resend cannot extend the reservation or reset attempt budgets. Reserve one recovery send within the three-send rolling destination budget, with proposed 60-second spacing and source ceiling of ten sends per rolling hour. A reserved slot does not guarantee recovery. If any applicable budget is exhausted, send nothing, return a concealed incomplete outcome and permit only a later governed retry. Review repeated and distributed reservation, source sharing and lockout risks before treating the limits as effective policy. Recovery needs proof tied to the recovering intent and its own eligible credential before replacing an unpromoted reservation. Never release retained authoritative ownership or activate another intent's credential.

Abandoned verifier material is immediately ineligible. Propose destruction after the seven-day idempotency window subject to approved retention or holds. Preserve only authorized protected audit and repair evidence. Apply the earlier proposed atomic promotion and owner observation rules.

After adoption, synchronize `specifications/contracts/customer/initial-customer-registration-contract.md` sections 5–8, 13–21 and 25–27 and the paired `initial-customer-registration.openapi.yaml`. Define scoped continuation issuance and cookie/CSRF custody, first durable binding in place of the stable full-request fingerprint, compatible retry and protected 409, concealed 202 and confirmed completion, expiry, recovery and indeterminate outcomes. Synchronize CLSP-002–011, CRIO-002–015, IEVE-001–016 and ICAE-002–010 in their existing design records. Add the minimum verification contract pair only after adoption, covering proof submission, controlled resend/recovery, safe expiry and uncertainty, no-store and correlation, with no Authentication effect. Keep current effective contracts unchanged in this package.
