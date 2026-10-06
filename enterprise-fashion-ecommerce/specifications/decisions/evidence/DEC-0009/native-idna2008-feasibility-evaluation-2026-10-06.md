# DEC-0009 bounded native feasibility and admission-readiness evidence

- Evaluation date: 2026-10-06 (Africa/Johannesburg).
- Owner: Identity.
- Status: informational evaluation evidence; no implementation authority or completed human approval asserted.
- Authorization: [DEC-0009 0.7.0 Proposed](../../DEC-0009-customer-login-idna2008-implementation-strategy.md), authorization commit `bdc152393c47ecc8d3fefde66b07336981c46856`.
- Merged baseline and verified HEAD: `53e361aee90d2192f534b565a00e0d85ab7c57b0`.
- Verified branch: `eval/dec-0009-native-idna2008-feasibility`.
- Decision index: DECISIONS.md 1.0.55 Approved; DEC-0009 remains Proposed, Identity-owned, Security Decision, Authoritative false.

## Question, scope and outcome interpretation

> Can an exact, maintained native implementation, through a supported public API and defensible Java 21 integration, satisfy the complete governed strict no-mapping IDNA2008 profile—with explicit Unicode policy, resource bounds and retained-identifier upgrade behavior—while meeting repository provenance, licensing, support, reproducibility, deployment and failure-isolation requirements? Which responsibilities remain repository-owned, and would the resulting integration require an Architecture change?

This evaluation reached documentary/source discovery and stopped before candidate-specific experimental execution. GNU Libidn2 2.3.8 was the sole candidate inspected. Its inspected public lookup/registration arrangement does not establish the complete required validation boundary; supplementary protocol implementation is outside this authorization. No library was selected for production or admitted for experiments in the repository.

The execution request used different A/B/C captions from the controlling authorization. As explicitly directed, this report uses DEC-0009 0.7.0's exact terminal caption and semantics: **B. FEASIBILITY OR ADMISSION-READINESS BASIS NOT DEMONSTRATED**. This is a candidate-specific negative finding based on missing required behavior in the inspected public paths, not universal native infeasibility. Download failures and unexecuted integration work are additional limitations, not evidence that the candidate cannot run on Java 21.

The exhausted 0.3.0 composition authorization and its [stopped evidence](bounded-evaluation-2026-10-02.md) remain unchanged. The exhausted 0.5.0 authorization and [ICU internal-codec Outcome B](icu-punycode-supportability-evaluation-2026-10-03.md) remain unchanged. Neither path was reopened. No custom protocol logic, codec, copied implementation, fork, production adapter, real Customer data, external service, Login integration, dependency change or Contract change occurred.

## Repository authority applied

[DEC-0005](../../DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md) requires already-valid Unicode labels, canonical A-label validation and safe failure before comparison or lookup. ASCII local-part case handling, dots and plus tags remain unchanged. DEC-0009 explicitly includes CONTEXTO and domain-aware bidi beyond primitive conversion.

[DEC-0001](../../DEC-0001-backend-build-tool-dependency-management.md), JAVA.md and SPRING.md preserve Java 21, Gradle 8.14.5, Spring Boot 3.5.16, fixed dependencies, locks and reviewed verification. SECURITY-STANDARDS.md sections 23–26 require bounded runtimes, provenance, artifact review and supply-chain controls; availability is not admission. ARCHITECTURE.md preserves inward dependency direction, Identity ownership and a single Spring Boot backend deployment; material changes require an Accepted ADR and synchronized Architecture. DECISIONS.md section 47 requires measured evidence to be distinguished from assumptions. AGENTS.md, TESTING-STANDARDS.md, DOCUMENTATION-STANDARDS.md and ENGINEERING-PRINCIPLES.md require scope discipline, traceability and honest limitations. No governing document is changed by this report.

## Discovery inventory and attributable upstream evidence

Sources below were consulted on 2026-10-06. Web retrieval may return indexed/cached content; this report does not certify that 2.3.8 is the latest release or that no newer advisory exists.

| ID | Source and evidence |
| --- | --- |
| S1 | [GNU project page](https://www.gnu.org/software/libidn/) identifies Libidn2 separately from legacy Libidn and links its upstream project and documentation. Legacy Libidn platform/Java statements were not transferred to Libidn2. |
| S2 | [Maintainer's 2.3.8 release announcement, GNU info-gnu message mirrored by Mail Archive](https://www.mail-archive.com/info-gnu@gnu.org/msg03377.html): stable release dated 2025-03-08; tag `v2.3.8`, announced commit `9bc3ac79e2ae81ade245a0e308bf13c981efaa83`; Unicode 15.1.0 table update and U+19DA classification change; published archive, signatures, transparency proofs and digest. The GNU-hosted message URL could not be retrieved. This is attributable maintainer evidence through a mirror, not locally verified signed evidence. |
| S3 | [GNU public API reference](https://www.gnu.org/software/libidn/libidn2/reference/libidn2-idn2.h.html): documented UTF-8 lookup/registration functions, flags, error returns and `idn2_free`. Public API status is supported; complete strict-profile suitability is a separate question. |
| S4 | [Tagged lookup.c](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/lib/lookup.c): source inspection of default flags, label handling, validation flags and domain loop. No function was executed. |
| S5 | [Tagged register.c](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/lib/register.c): source inspection of registration, ASCII fast path and A-label round trip. No function was executed. |
| S6 | [Tagged internal idna.h](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/lib/idna.h): distinguishes existence of a CONTEXTO rule from evaluation of that rule. Internal declarations were inspected as evidence, not proposed as callable integration APIs. |
| S7 | [Tagged README](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/README.md): C library LGPLv3-or-later/GPLv2-or-later dual licensing; auxiliary tools have distinct GPLv3-or-later terms; Unicode and individual-file notices need separate review. Links issue tracking and continuous fuzzing. This supports an upstream maintenance route, not an SLA, vulnerability clearance or current support guarantee. |
| S8 | [Tagged dependency inventory](https://gitlab.com/libidn/libidn2/-/raw/v2.3.8/DEPENDENCIES.md): C toolchain/POSIX build prerequisites; libunistring may be external or an included subset; iconv and gettext implications vary by system. Mentions glibc, NetBSD and mingw; it is not a certified OS/CPU support matrix. |
| S9 | [Java 21 JNI specification](https://docs.oracle.com/en/java/javase/21/docs/specs/jni/intro.html): JNI is a supported Java/native interoperability mechanism. This establishes a realistic mechanism, not a tested Libidn2 binding. |
| S10 | [RFC 5893 sections 1.4 and 2](https://www.rfc-editor.org/rfc/rfc5893.html): bidi applicability is domain-aware, including relevant ASCII labels; the first-character rule matters for digit-leading labels. |

GNU Libidn2 2.3.8 was chosen for discovery because its upstream documents expose both a no-TR46 switch and registration validation through public C APIs. It is distinct from the previously evaluated GNU Libidn Java codec. One candidate was sufficient to reach the authorized stop condition; no general survey or substitution followed.

Discovery prerequisites were only partially satisfied: exact release identity, public functions, license and historical release/maintenance evidence were available. A supported production OS/CPU matrix, exact binding/build inventory and current security/support disposition were not established. Thus no candidate passed the full pre-experiment gate. JNI was the sole integration mechanism considered documentarily; no JNA version, preview FFM API or external service was introduced.

## Decisive source findings

S4 lines 97–113 show that explicit `IDN2_NO_TR46` is needed to avoid default non-transitional processing. Its `label` function uses `TEST_CONTEXTO_WITH_RULE`, not `TEST_CONTEXTO_RULE`; S6 defines these as different checks. The ordinary-ASCII path returns after length/copy operations. The domain loop processes labels independently without carrying bidi state across them. These are static observations, not measured counterexample results.

S5 lines 155–162 request NFC, disallowed/unassigned, hyphen, combining, CONTEXTJ, CONTEXTO-rule and bidi tests for non-ASCII registration labels. However, lines 137–148 return ordinary ASCII unchanged after a length check. Registration takes a single label and does not establish domain-wide bidi applicability. Its A-label path decodes, recursively validates and compares re-encoding; that benefit does not close the domain gap.

Inference from these call paths and S10: replacing lookup with per-label registration does not itself supply the missing domain-aware enforcement. For example, the governed disposition for `אב.123` (U+05D0 U+05D1, dot, ASCII digits) must account for the ASCII label in a bidi domain. The inspected paths do not demonstrate that enforcement. Writing supplementary bidi/ASCII validation or calling internal helpers would exceed the permitted minimal bridge. Stop rather than implement it. This is not a claim that no separately governed composition could ever address the gap.

## Protocol responsibility matrix

All entries are documentary/source findings or unresolved requirements. Local protocol results: **NOT EXECUTED**.

| Obligation | Evidence and allocation |
| --- | --- |
| RFC 5890 U-label/A-label distinctions | Public registration distinguishes the representations; Identity must select the correct validated path, not treat arbitrary conversion output as authority. |
| RFC 5891 registration versus lookup | Separate APIs have different checking behavior; lookup alone is insufficient for the stricter repository profile. |
| RFC 5892 properties/exceptions | Validation flags and release-table evidence exist; full derived-table correctness was not independently recomputed. |
| CONTEXTJ | Both inspected validation calls request contextual checking; all rule cases remain untested. |
| CONTEXTO | Registration requests rule evaluation; lookup requests rule existence. Do not claim equivalent strict validation. |
| RFC 5893/domain bidi | Required cross-label applicability, including ASCII, is not established by the inspected paths. Decisive gap. |
| U-label NFC/no repair | Registration without NFC-conversion flag is the documentary direction; complete rejection corpus not executed. Normalization as repair remains prohibited. |
| A-label validity/round trip | Registration has decode/validate/re-encode comparison. Mixed case, fake labels and malformed encodings require independent corpus evidence; none was measured. |
| ASCII syntax, label/domain lengths | Length checks do not replace the missing ASCII validity boundary. Input preparation is not complete IDNA validation. |
| No mapping/UTS #46 isolation | Explicit flag/control-flow evidence exists; defaults and locale APIs are unsafe assumptions for this profile. No mapping-isolation probe executed. |
| Malformed Unicode/unassigned | Public error model and validation flags are evidence of intended handling, not exhaustive safety proof. Java/native encoding boundary remains untested. |
| Canonical comparison key | Repository-owned composition of fully validated ASCII domain with governed local part remains necessary; no key was produced. |
| Resources and safe failure | CPU/allocation ceilings, concurrency and pathological inputs unmeasured. Uncertainty must prevent lookup. |

An illustrative future corpus would require positive/negative contexts, sharp-s/sigma distinctions, decomposition, width/ignored characters, alternative separators, malformed A-labels, supplementary/unassigned scalars and resource boundaries. No such corpus was run or reported as passing after the stop.

## Unicode and retained-identifier lifecycle

S2 reports Unicode 15.1.0 tables and a changed classification for U+19DA. This is direct evidence that version changes can affect acceptance, not proof of a stable retained-identifier policy. S8 allows variation in linked Unicode support; exact normalization/property behavior therefore requires a pinned, inventoried build, not merely the Libidn2 version string. No linked libunistring version or runtime Unicode combination was measured.

Identity retains responsibility for comparing acceptance and canonical outputs across upgrades, detecting collisions, preserving retained ownership, and reviewing rollback before newly accepted identifiers become ambiguous. DEC-0005 forbids arbitrary merge/reassignment and authentication from uncertainty. Upstream release data does not supply the repository's reconciliation workflow, and none is invented here. No Unicode version is selected.

## Java 21, supply chain and Architecture implications

JNI could call the documented UTF-8 C functions using an owned minimal bridge. This is a design inference from S9, not a completed binding. A defensible proposal would require strict Java-string-to-standard-UTF-8 conversion, malformed-surrogate/NUL handling, explicit buffer ownership and release through the documented allocator boundary, error translation, exception safety and tests. JNI modified UTF-8 must not be assumed equivalent to the required input encoding. Native faults share the JVM process; a Java exception handler is not native crash isolation. None of these behaviors was tested.

The bridge would require platform-specific compilation, authenticated native loading, an exact native/transitive inventory, reproducible packaging and startup failure behavior. Platform support, thread safety, ABI upgrade behavior, security patch response and resource limits remain incomplete evidence. No production support guarantee or absence of vulnerabilities is asserted.

DEC-0001 admission would separately require exact native/binding/data artifacts, compatible licenses and redistribution review, source-to-binary provenance, integrity verification, reviewed dependency state, scans/SBOM and build/test evidence. Native/system artifacts cannot silently escape these controls because they are outside Maven. S2 publishes signature/proof mechanisms; no signature, transparency proof or artifact digest was verified locally. Publisher attribution is not byte verification.

Assessment: adopting the considered in-process native boundary would materially introduce native loading, ABI/platform packaging and process-failure responsibilities into the backend. An Architecture decision and Accepted ADR with canonical synchronization are required before that affected production integration. Scratch discovery does not change Architecture. No external service or separate deployment is selected as a workaround.

Identity would own identifier semantics and compatibility; Engineering would own reproducible bridge/build verification; Security would review native attack surface and supply chain; operational ownership for monitoring, patching and recovery must be explicitly assigned in a later proposal. Diagnostics must exclude raw identifiers and protected existence. Rollback must preserve retained identity ownership, not just reinstall an older binary.

## Environment, commands and reproducibility limits

Observed host: Darwin arm64, macOS 26.5.2 build 25F84. Explicit Java 21 check: Temurin 21.0.6+7-LTS. This installed patch is observation, not a current-patch approval. Default Java was 23; it was not used for any candidate execution. Git 2.55.0; curl 8.7.1 (reported x86_64-apple-darwin25.0). No Gradle, compiler, native executable or integration harness was run.

Read-only repository commands included `git branch --show-current`, `git rev-parse HEAD`, `git status --short`, `git log -3 --oneline`, and targeted `cat`, `sed`, `rg` and `wc` reads of the authorization/governance/evidence. Environment inspection used `uname -sm`, `sw_vers`, `/usr/libexec/java_home -V`, the explicit Temurin `java -version`, `git --version` and `curl --version`.

Scratch directory created with `mktemp -d /tmp/dec0009-native-20261006.XXXXXX`: `/tmp/dec0009-native-20261006.JtzhR2`. Exact attempted acquisition commands:

```sh
curl -fL --max-time 45 -o /tmp/dec0009-native-20261006.JtzhR2/libidn2-2.3.8.tar.gz https://ftp.gnu.org/gnu/libidn/libidn2-2.3.8.tar.gz
curl -fsSL --connect-timeout 10 --max-time 25 -o /tmp/dec0009-native-20261006.JtzhR2/libidn2-2.3.8.tar.gz https://ftpmirror.gnu.org/libidn/libidn2-2.3.8.tar.gz
```

First command failed in the sandbox with curl exit 6 (DNS). Its permitted network retry failed with exit 28 after 45 seconds. The mirror attempt failed with exit 28 after its connection timeout. Scratch listing confirmed no downloaded files. No package install, extraction, source copy, compilation or candidate probe occurred. Consequently there are **no locally calculated candidate artifact checksums**. S2's published archive SHA-256 is `9VeRG/YXFiHh9y/zX1sYJbs1tS7UUyXc3ukx5dPAeHo=` (base64); it is an upstream reference only, not a verified local match.

Web source retrieval succeeded for S3–S10 as cited; some GNU page/message requests timed out or were inaccessible, and attempted tagged NEWS, build/CI and helper-source requests returned cache misses. A mistaken GNU message URL resolved to legacy Libidn 1.43 and was excluded as candidate release evidence. No success was inferred from failed retrievals. Tagged source inspection plus attributable release identity supports the limited negative finding; executable confirmation, immutable byte retention and full current security research remain unavailable. Reproduction should inspect the cited release paths and line/function locations; this report embeds no candidate source.

## Stop assessment, findings and next governance

Triggered: the inspected public arrangement lacks demonstrated complete required protocol behavior; filling it with custom validation or internal calls would exceed the authorized scope. Full discovery prerequisites were also not established. Evaluation stopped before experiments, with B justified by the source-level required-behavior gap rather than by network failure alone. No unsafe execution, requirement weakening, forbidden substitution, production integration or scope expansion occurred.

Evidence-quality findings, not deployed vulnerability claims:

- Critical: none established.
- High: the inspected API arrangement does not supply the complete strict validation boundary; accepting conversion as validation could violate governed identifier semantics. Production eligibility is not demonstrated.
- Medium: no verified downloaded artifact, exact linked Unicode/runtime inventory, Java/native execution, complete platform/support assessment, resource/concurrency proof or retained-identifier upgrade evidence. These remain explicit admission/readiness gaps.
- Low: task/authorization outcome-caption mismatch resolved in favor of the controlling record; some upstream retrievals failed and are recorded rather than represented as evidence.

This one evaluation is complete and its 0.7.0 authorization is consumed under its own terms. The immediate next action is Identity-led Security/Architecture/Engineering review of this report and a separately authorized governance synchronization of the outcome. DEC-0009 and DECISIONS.md are intentionally unchanged here. Any new candidate, supplementary protocol composition or follow-up experiment requires fresh explicit authorization. This report establishes neither universal impracticability nor a basis for changing Product/Security semantics.

Only this evidence file is created. No technology selection, dependency admission, DEC acceptance, Architecture approval or implementation authorization follows.

**B. FEASIBILITY OR ADMISSION-READINESS BASIS NOT DEMONSTRATED**
