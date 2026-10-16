# DEC-0041 — Post-Expiry OpenPGP Artifact Signature Disposition

- **Identifier:** DEC-0041
- **Title:** Post-Expiry OpenPGP Artifact Signature Disposition
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.3.0
- **Date:** 2026-10-16
- **Last Updated:** 2026-10-16
- **Owner:** Security
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

PR [#219](https://github.com/awesomehesman/eCom-Online-Store/pull/219) consolidates a Gradle version assertion correction, three reviewed XMLUnit checksums, bounded verification-report collection and public verification keys matching existing repository trust rules. Its GitHub Actions [run 38942131465](https://github.com/awesomehesman/eCom-Online-Store/actions/runs/38942131465) passed at commit `7f02a4d45ebb6fd899b1a33d6f9733b6e4d9873e`. Strict dependency verification remains enabled. Successful CI establishes the observed build and verification results, not Security approval or merge authorization.

The subsequent read-only security investigation identified four artifact-signing timestamps after the available certified OpenPGP key expiries. All four artifact signatures verify cryptographically, but no authenticated expiry extensions were established. Publisher provenance remains incomplete. Existing trusted-key rules predate PR #219 and do not themselves establish acceptance of post-expiry signing.

This Proposed record captures the project-conversation investigation findings supplied on 2026-10-16 and the evidence needed for an accountable disposition. It records the accountable Security Authority's provisional CI/build classification below, without asserting independent Security approval, a final risk classification or a Security Exception. Temporary local audit outputs are supporting investigation material, not durable repository approval records; their relevant evidence must be retained or made reproducible for review.

## Decision

**Proposed decision question:** Under the existing Approved security and decision-governance requirements, what disposition is justified for continued reliance on the four exact artifacts and signing fingerprints below, given cryptographically valid signatures dated after the available certified expiries and incomplete publisher provenance?

**No alternative is selected. No security risk is accepted or rejected.**

| Governance item | Current state |
| --- | --- |
| Risk classification | HIGH — PROVISIONAL (CI/build scope only) |
| Security disposition | PENDING |
| Architecture disposition | PENDING |
| Independent review requirement | PENDING CASE-SPECIFIC DETERMINATION |
| Security Exception required | UNDETERMINED |
| Merge authorization | WITHHELD |

An ordinary Security Decision must remain within existing authority. If the eventual disposition would waive a mandatory security requirement, a separately compliant Security Exception is required; this record cannot replace or create one. This proposal authorizes no dependency, keyring, trust-rule, verification-setting, workflow or implementation change.

## Four-Case Evidence Assessment

All timestamps are UTC. The signing time is part of the signed artifact evidence; it is not an independently trusted timestamp.

| Artifact | Signing-key fingerprint | Available certified expiry | Artifact signing time | Established result |
| --- | --- | --- | --- | --- |
| `org.hamcrest:hamcrest:3.0` — `hamcrest-3.0.jar` | `E3A9F95079E84CE201F7CF60BEDE11EAF1164480` | `2020-11-20T07:59:08Z` | `2024-08-01T09:08:57Z` | Cryptographic signature valid; post-expiry lifecycle disposition unresolved. |
| `com.querydsl:querydsl-bom:5.1.0` — `querydsl-bom-5.1.0.pom` | `57312C37B064EE0FDAB0130490D5CE79E1DE6A2C` | `2021-11-19T22:34:40Z` | `2024-01-29T01:03:02Z` | Cryptographic signature valid; post-expiry lifecycle disposition unresolved. |
| `io.rsocket:rsocket-bom:1.1.5` — `rsocket-bom-1.1.5.pom` | `9B32CBC0F3F6BA4C13D611FC21871D2A9AB66A31` | `2023-06-09T04:10:13Z` | `2025-02-03T17:04:38Z` | Cryptographic signature valid; post-expiry lifecycle disposition unresolved. |
| `org.seleniumhq.selenium:selenium-bom:4.31.0` — `selenium-bom-4.31.0.pom` | `F23E6F40ED06B8E0B269523C0DE2A6EBAF6DB53F` | `2022-03-26T21:53:00Z` | `2025-04-05T00:41:52Z` | Cryptographic signature valid; post-expiry lifecycle disposition unresolved. |

The investigation freshly retrieved these artifacts and detached signatures from Maven Central and verified them against the relevant public keys. The inspected expiry self-certifications verify cryptographically. Fresh Ubuntu keyserver copies did not establish newer expiry extensions. Some keys.openpgp.org responses omitted identity certifications; an apparent absence of expiry in such a stripped response is not evidence of an authenticated extension or unlimited validity.

No revocations were found in inspected key material. Absence of observed revocation is not proof of current non-revocation, and the investigation does not establish that no newer certification or revocation exists elsewhere.

QueryDSL has the strongest publisher association: its official release script names the exact fingerprint, and its release author's published GitHub key contains the same fingerprint and expired certification. Hamcrest release evidence, RSocket release-signing automation and Selenium signer/project association provide context but did not establish complete, independent authentication of each exact signing fingerprint or a defensible expiry extension. No publisher explanation resolving these four post-expiry cases was established. No artifact compromise is established by these findings.

Cryptographic signature validity, key validity at signing time, publisher identity, repository trust configuration and Gradle acceptance are distinct findings. None substitutes for the others.

## Applicable Security Requirements

| Governing source | Applicable boundary |
| --- | --- |
| [Security Standards §§8–9](../../.ai/core/SECURITY-STANDARDS.md) | Assign accountable key, supply-chain and component ownership; assess material risk and apply review obligations triggered by classification. |
| [Security Standards §15](../../.ai/core/SECURITY-STANDARDS.md) | Document key purpose, ownership, consumers, trust boundary and lifecycle; govern renewal and revocation. The explicit TLS validity rule is not automatically an OpenPGP artifact-expiry policy. |
| [Security Standards §§24–26](../../.ai/core/SECURITY-STANDARDS.md) | Preserve protected CI, reviewed dependencies, provenance, locking, integrity and applicable signature verification. |
| [Security Standards §§40–43](../../.ai/core/SECURITY-STANDARDS.md) | Preserve review gates, attributable evidence and the separate formal Security Exception mechanism for any mandatory security waiver. |
| [Decisions §§11, 16–19, 24, 31 and 47–50](../../.ai/core/DECISIONS.md) | Maintain a bounded record, accountable owner, applicable Security and Architecture approval, accurate evidence and index synchronization. A Security Decision cannot substitute for a required exception. |
| [AGENTS §§13.4–13.6 and 27.7](../../.ai/core/AGENTS.md) | CI alone cannot establish approval; required approvals and blocking concerns must be resolved. AI agents cannot grant risk acceptance or exceptions. |
| [Architecture §42](../../.ai/core/ARCHITECTURE.md) | Preserve applicable dependency, security and artifact-provenance controls in CI/CD. |
| [DEC-0001 — Decision and Security Impact](DEC-0001-backend-build-tool-dependency-management.md) and [Spring §5](../../.ai/backend/SPRING.md) | Preserve reviewed Gradle dependency verification, fixed versions, locking and the governing security requirements. |

The reviewed governance does not establish an explicit blanket OpenPGP post-expiry acceptance rule or categorical prohibition. That absence does not waive the binding lifecycle, provenance, risk and review requirements, and does not establish compliance for these four cases.

## Risk Classification and Residual Risks

**Risk classification: HIGH — PROVISIONAL (CI/build scope only).** On 2026-10-16, the repository owner, acting as **Founder / Initial Security Authority**, explicitly authorized this overall CI/build supply-chain classification in the project conversation. This is an attributable accountable classification determination, not final Security approval, Architecture approval, residual-risk acceptance, an independent review or a finding that any artifact is compromised.

Hamcrest 3.0 is the primary risk driver: executable test code presents potentially High CI/build-integrity impact. The overall provisional classification does not assign the same exposure or individual risk classification to the three BOMs:

| Artifact | Separate exposure assessment | Classification boundary |
| --- | --- | --- |
| Hamcrest 3.0 | Transitive executable test dependency on test compile/runtime classpaths; potentially High CI/build-integrity impact. | Primary driver of overall provisional HIGH classification; no verified production-runtime inclusion is asserted. |
| QueryDSL BOM 5.1.0 | Limited current build-metadata exposure through the Spring Boot platform import; no selected executable QueryDSL component in the inspected locked graph. | Separate, narrower exposure assessment; no individual HIGH classification is inferred. |
| RSocket BOM 1.1.5 | Limited current build-metadata exposure through the Spring Boot platform import; no selected executable RSocket component in the inspected locked graph. | Separate, narrower exposure assessment; no individual HIGH classification is inferred. |
| Selenium BOM 4.31.0 | Limited current build-metadata exposure through the Spring Boot platform import; no selected executable Selenium component in the inspected locked graph. | Separate, narrower exposure assessment; no individual HIGH classification is inferred. |

The effectiveness of existing controls and the likelihood of malicious artifact acceptance remain unverified; no likelihood score is assigned. **Production-release risk remains unclassified** because deployment and promotion trust boundaries have not been verified. Limited runtime reachability does not eliminate CI/build supply-chain risk.

Under the accountable authorization, [Security Standards §9](../../.ai/core/SECURITY-STANDARDS.md) High-risk obligations apply **provisionally**: threat modelling, Security review, negative-path testing, explicit approval, stronger monitoring and recovery evidence. None is marked completed by this classification; required scope, evidence and accountable completion remain outstanding. The earlier CI and technical findings do not independently establish completion of these obligations.

A practical separation-of-duties assessment under [Security Standards §7.6](../../.ai/core/SECURITY-STANDARDS.md) is required. It must assess allocation of request, approval, execution and review, practical separation and applicable reviewer independence for this case. **Independent review: PENDING CASE-SPECIFIC DETERMINATION.** No universal independence requirement, completed independent review or exemption is inferred.

Classification may be revised following additional attributable technical evidence and accountable Security review. Any revision must preserve the distinction between CI/build scope, the separate artifact exposures and unclassified production-release risk.

Residual uncertainties include:

- Whether authenticated publisher renewal certifications or a defensible publisher explanation exist but were not obtained.
- Whether complete provenance and key-lifecycle evidence supports continued reliance on each artifact.
- Whether inspected key material reflects current revocation state.
- How reliance on these existing trust rules affects build inputs and release confidence, including unverified deployment and promotion trust boundaries.
- The effectiveness of existing controls, likelihood of malicious artifact acceptance and completion evidence for provisionally triggered §9 obligations.
- Practical duty allocation, separation and the case-specific independent-review determination under §7.6.
- Whether a proposed disposition would remain within existing requirements or require a specified mandatory requirement to be waived.

Existing trust configuration and successful cryptographic checks do not close these uncertainties. A later disposition must distinguish technical verification from independent publisher authentication and record the limits of both.

## Alternatives Considered

These are assessment alternatives only; none is selected, ranked or authorized:

1. Obtain authenticated publisher key-lifecycle and provenance evidence, then determine whether continued use satisfies existing requirements without a waiver.
2. Establish through accountable review that existing controls and evidence support a bounded disposition within Approved governance, explicitly recording the interpretation, remaining risk and review triggers without weakening mandatory requirements.
3. If a mandatory requirement would need to be waived, evaluate a separate, scoped, time-bound Security Exception under Security Standards §41. No such exception is created or presumed approvable by this record.
4. Decline continued reliance and propose remediation through a separate governed change. No replacement artifact, key, version or verification mechanism is selected here.

## Consequences

The proposal makes the unresolved evidence and decision boundaries reviewable without changing security enforcement. It adds no execution or merge authority. Continued investigation and accountable review remain necessary; CI success does not remove that work. Any eventual remediation or acceptance must preserve the distinction between the existing baseline, the decision and separately authorized implementation.

## Security Impact

No existing trusted-key rule, checksum, signature setting, keyring, dependency or CI control is changed. No Security Exception, signature bypass, broad trust grant or risk acceptance is established. Permanent, undocumented, self-approved or retroactive Security Exceptions remain prohibited by Security Standards §41.

The pending decision concerns integrity, authenticity, provenance and key-lifecycle assurance for these four exact cases. It does not authorize a repository-wide policy for accepting expired keys or future artifacts.

## Data Impact

No application data model, retention rule or processing purpose changes. Supporting key and signature evidence may include public signer identities and local paths; its retention and review must preserve applicable security evidence controls. No credentials or private-key material are required for the disposition.

## Compatibility and Migration Impact

No dependency versions, lockfiles, Gradle Wrapper files or application interfaces change. No migration is selected. Any later remediation must separately assess compatibility, security and governing baseline requirements, including DEC-0001 and Spring §5.

## Operational Impact

Strict verification and existing CI failure behavior remain binding. Public-key availability and the observed successful CI run do not establish production-release readiness. The accountable owner must identify follow-up ownership, evidence retention, review triggers and any remediation obligations when a disposition is proposed for acceptance.

## Security and Architecture Approval Evidence

| Required function or determination | State | Evidence boundary |
| --- | --- | --- |
| Accountable Security owner | Security ownership identified; disposition PENDING | Functional ownership is not approval. |
| Security Authority | Classification authorized 2026-10-16; final disposition PENDING | Founder / Initial Security Authority explicitly authorized provisional HIGH CI/build classification in the project conversation; no final security approval or risk acceptance. |
| Architecture Authority | PENDING | No acceptance or rejection is recorded. |
| Affected Engineering/component and Platform ownership review | PENDING | Review scope must follow the affected responsibilities and assessed risk. |
| Independent review requirement | PENDING CASE-SPECIFIC DETERMINATION | Operating arrangements and practical constraints are confirmed below; §7.6 compliance and the independent-review determination remain pending. |
| Security Exception applicability | UNDETERMINED | Any mandatory waiver requires the separate §41 process. |

The project conversation identifies the repository owner as the initial Security Authority and Architecture Authority. The separate 2026-10-16 Founder / Initial Security Authority authorization supplies only the provisional classification described above; neither final Security disposition nor Architecture disposition is supplied. If approval in both capacities is later permitted under applicable risk and separation-of-duties requirements, both capacities and their dated evidence must be recorded explicitly. Such approval must not be described as independent review and cannot authorize a self-approved Security Exception.

Acceptance requires the applicable Security and Architecture authorities under Decisions §17, with affected-owner participation and any separately triggered review. A generic risk acceptance, ordinary PR approval or AI statement cannot replace a required Security Exception. This record remains Proposed until a separately authorized, evidence-backed lifecycle change occurs.

## Case-Specific Separation-of-Duties Arrangement — §7.6

**Source and confirming capacity:** Explicit project-conversation confirmation by the Founder / Initial Security Authority, recorded in this 2026-10-16 amendment. **Classification:** attributable confirmation of current operating arrangements and practical constraints only. It is not independent human review, a §7.6 compliance determination, a mandatory-review waiver, a Security Exception or final approval.

The Founder confirms that they are the sole repository maintainer and hold Engineering, Platform, Security and Architecture authority. No additional qualified developer, Security reviewer or Platform engineer is currently assigned or available to independently review DEC-0041. Separating these responsibilities across multiple individuals is not currently practical. This records the confirmed constraint; it does not establish that lack of available personnel satisfies §7.6 or excuses any binding requirement.

### Actual duty allocation and authority boundaries

| Function | Current arrangement | Evidence and limits |
| --- | --- | --- |
| Request and proposal | Founder / sole maintainer directs the work and requests the decision assessment. | Functional allocation is confirmed; no final disposition is selected. |
| Engineering and Platform implementation | Founder / sole maintainer is accountable; Codex/Kiro assists with implementation and technical analysis under the maintainer's supervision. | Assistance does not provide an independent human approval function or establish an additional maintainer. |
| Security classification and final Security disposition | Founder / Initial Security Authority authorized the provisional HIGH CI/build classification; final Security disposition remains PENDING. | Classification authorization is separate from final approval and residual-risk acceptance. |
| Architecture disposition | Founder also holds Architecture authority; disposition remains PENDING. | Holding both capacities does not create independent human review or establish approval. |
| Execution and operational control | Founder / sole maintainer holds the relevant functional accountability; technical tasks may be assisted by Codex/Kiro and CI. | Complete effective GitHub permissions, bypass powers, production execution rights and evidence-administration permissions remain unverified. Functional authority must not be equated with a verified permission inventory. |
| Technical analysis and automated checks | Codex/Kiro supplies supervised analysis and implementation assistance; CI supplies automated results. | Neither Codex/Kiro nor CI possesses independent human approval authority. These results do not substitute for required accountable review. |
| Independent human review | No additional qualified reviewer is currently assigned or available. | No independent human review is established. The case-specific independent-review determination remains PENDING. |

This arrangement concentrates request, implementation accountability and potential approval functions in one individual. The confirmation does not establish that this individual has granted final approval or completed required review. Under [Security Standards §7.6](../../.ai/core/SECURITY-STANDARDS.md), the allocation and practical constraint must be assessed alongside applicable review obligations; unavailable personnel do not by themselves produce a SATISFIED conclusion.

### Available safeguards and verification limits

Existing evidence includes scoped trusted-key rules, strict dependency verification, fixed versions and dependency locking, review-visible Git changes, the documented signature/key-expiry investigation and PR #219's recorded CI results. The inspected workflow declares read-only repository-token permissions and read-only action-managed Gradle caching for pull requests. These are technical or procedural evidence sources, not independent human approvals. Public-key availability, cryptographic validity and green CI do not resolve publisher provenance or post-expiry risk acceptance.

The overall effectiveness of safeguards remains unverified. Actual branch-protection enforcement, approval-bypass capabilities, complete secret and credential availability, protection of evidence from alteration, and production deployment/promotion boundaries remain unverified. No additional compensating control is invented or approved by this assessment. Local or CI checks cannot establish independent separation merely because they execute separately from the maintainer's manual work.

### Outstanding determination and obligations

- **§7.6 compliance determination: PENDING.** Accountable review must determine what practical separation is required for this specific high-impact CI/build case, whether the current allocation can satisfy the applicable requirements, and which evidence supports that conclusion.
- **Independent-review determination: PENDING CASE-SPECIFIC DETERMINATION.** No universal distinct-reviewer requirement or exemption is inferred; any independently required review remains outstanding despite current personnel constraints.
- Engineering/component and Platform functions are held by the sole maintainer, but their substantive review evidence remains outstanding. Functional accountability does not establish completion of those reviews.
- The **HIGH — PROVISIONAL (CI/build scope only)** classification and all provisionally applicable §9 obligations remain unchanged: threat modelling, Security review, negative-path testing, explicit approval, stronger monitoring and recovery evidence. None is completed by this operating-arrangement confirmation.
- **Security disposition: PENDING. Architecture disposition: PENDING. Security Exception applicability: UNDETERMINED. PR #219 merge authorization: WITHHELD.** Production-release risk remains unclassified.
- Any proposed mandatory security waiver requires the separate §41 process. This confirmation creates no exception authority, and a self-approved Security Exception remains prohibited. Dual-role Security/Architecture authority cannot be relabeled as independent review.

The next accountable determination must identify the assessed duty combinations, evidence of practical constraints, required review functions and outstanding completion evidence. It must not convert this confirmation into residual-risk acceptance or final approval.

## Review Triggers

Review is required when new authenticated expiry-extension or revocation evidence, publisher clarification, contradictory artifact evidence, compromise indicators or relevant governance changes affect this assessment. Changes to artifact versions, fingerprints, trust scope, build/release exposure or proposed compensating controls require reassessment rather than automatic extension of any future disposition.

The §7.6 arrangement must be reassessed if qualified personnel become available, duties or effective permissions change, or new review obligations are identified. This is a review trigger, not a promise that a reviewer will become available.

The provisional HIGH CI/build classification must be revisited following additional attributable technical evidence and accountable Security review, including evidence about control effectiveness, likelihood, duty separation or deployment/promotion boundaries. Completion or findings of the provisionally triggered §9 work may require revision; classification must not silently become final or extend to production-release scope. The eventual decision must specify its applicable review obligations and any time bounds. This proposal establishes no acceptance period or exception expiry.

## PR #219 Relationship and Registration Status

PR #219's successful CI at `7f02a4d45ebb6fd899b1a33d6f9733b6e4d9873e` is technical evidence only. **Merge authorization: WITHHELD.** This Proposed record neither approves that PR nor establishes that all merge or release gates are satisfied. It does not modify PR #219 or the other related PRs.

**Registration completed:** DEC-0041 0.1.0 was registered in `DECISIONS.md` through [PR #220](https://github.com/awesomehesman/eCom-Online-Store/pull/220), merged into develop at `63cb544d154961603d00596ce9bbf9c7bacfca37`. Historically, the initial 0.1.0 single-file preparation preceded that registration; its pending-registration statements described that earlier stage only.

This 0.3.0 amendment changes only DEC-0041 under the authorized scope. `DECISIONS.md` remains unchanged; its historical 0.1.0 registration and pending-classification commentary must not be treated as evidence that the later provisional classification or this operating arrangement was already recorded there. Any required index commentary/history synchronization for this amendment remains a separate follow-up under Decisions §50. Registration does not establish Security acceptance, Architecture acceptance or merge authorization.

## References

- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md)
- [Decision governance and index](../../.ai/core/DECISIONS.md)
- [Engineering constitution](../../.ai/core/AGENTS.md)
- [Architecture](../../.ai/core/ARCHITECTURE.md)
- [Documentation Standards §§7–8](../../.ai/core/DOCUMENTATION-STANDARDS.md)
- [Accepted DEC-0001](DEC-0001-backend-build-tool-dependency-management.md)
- [Spring Backend Standard](../../.ai/backend/SPRING.md)
- [PR #219](https://github.com/awesomehesman/eCom-Online-Store/pull/219) and [successful CI run](https://github.com/awesomehesman/eCom-Online-Store/actions/runs/38942131465)
- [Hamcrest 3.0 artifact](https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest/3.0/hamcrest-3.0.jar) and [detached signature](https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest/3.0/hamcrest-3.0.jar.asc)
- [QueryDSL BOM 5.1.0 artifact](https://repo.maven.apache.org/maven2/com/querydsl/querydsl-bom/5.1.0/querydsl-bom-5.1.0.pom) and [detached signature](https://repo.maven.apache.org/maven2/com/querydsl/querydsl-bom/5.1.0/querydsl-bom-5.1.0.pom.asc)
- [RSocket BOM 1.1.5 artifact](https://repo.maven.apache.org/maven2/io/rsocket/rsocket-bom/1.1.5/rsocket-bom-1.1.5.pom) and [detached signature](https://repo.maven.apache.org/maven2/io/rsocket/rsocket-bom/1.1.5/rsocket-bom-1.1.5.pom.asc)
- [Selenium BOM 4.31.0 artifact](https://repo.maven.apache.org/maven2/org/seleniumhq/selenium/selenium-bom/4.31.0/selenium-bom-4.31.0.pom) and [detached signature](https://repo.maven.apache.org/maven2/org/seleniumhq/selenium/selenium-bom/4.31.0/selenium-bom-4.31.0.pom.asc)
- [QueryDSL 5.1.0 release script naming the fingerprint](https://github.com/querydsl/querydsl/blob/QUERYDSL_5_1_0/release.sh) and [release author's published public keys](https://github.com/johnktims.gpg)
- [Hamcrest 3.0 release](https://github.com/hamcrest/JavaHamcrest/releases/tag/v3.0)
- [RSocket 1.1.5 signing workflow](https://github.com/rsocket/rsocket-java/blob/1.1.5/.github/workflows/gradle-release.yml)
- [Selenium project structure](https://www.selenium.dev/project/structure/)

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.3.0 | 2026-10-16 | Proposed | Recorded confirmed sole-maintainer duty allocation, practical separation constraints and supervised Codex/Kiro assistance; distinguished functional authority from independent human review, preserved unverified safeguards, pending §7.6/review determinations, outstanding §9 obligations and withheld approvals. |
| 0.2.0 | 2026-10-16 | Proposed | Recorded the Founder / Initial Security Authority’s dated authorization of provisional HIGH CI/build risk, separate artifact exposures, outstanding §9 obligations and §7.6 assessment; preserved pending final dispositions, unclassified production-release risk and withheld merge authorization; corrected current registration status while retaining its history. |
| 0.1.0 | 2026-10-16 | Proposed | Captured the four unresolved post-expiry signing cases, evidence limits and accountable-disposition requirements without selecting an alternative, granting approval or creating an exception; index synchronization remains pending. |
