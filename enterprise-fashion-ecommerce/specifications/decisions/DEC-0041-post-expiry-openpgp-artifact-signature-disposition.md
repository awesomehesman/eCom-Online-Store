# DEC-0041 — Post-Expiry OpenPGP Artifact Signature Disposition

- **Identifier:** DEC-0041
- **Title:** Post-Expiry OpenPGP Artifact Signature Disposition
- **Type:** Security Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-10-16
- **Last Updated:** 2026-10-16
- **Owner:** Security
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context

PR [#219](https://github.com/awesomehesman/eCom-Online-Store/pull/219) consolidates a Gradle version assertion correction, three reviewed XMLUnit checksums, bounded verification-report collection and public verification keys matching existing repository trust rules. Its GitHub Actions [run 38942131465](https://github.com/awesomehesman/eCom-Online-Store/actions/runs/38942131465) passed at commit `7f02a4d45ebb6fd899b1a33d6f9733b6e4d9873e`. Strict dependency verification remains enabled. Successful CI establishes the observed build and verification results, not Security approval or merge authorization.

The subsequent read-only security investigation identified four artifact-signing timestamps after the available certified OpenPGP key expiries. All four artifact signatures verify cryptographically, but no authenticated expiry extensions were established. Publisher provenance remains incomplete. Existing trusted-key rules predate PR #219 and do not themselves establish acceptance of post-expiry signing.

This Proposed record captures the project-conversation investigation findings supplied on 2026-10-16 and the evidence needed for an accountable disposition. It does not assert independent Security approval, a completed risk classification or a Security Exception. Temporary local audit outputs are supporting investigation material, not durable repository approval records; their relevant evidence must be retained or made reproducible for review.

## Decision

**Proposed decision question:** Under the existing Approved security and decision-governance requirements, what disposition is justified for continued reliance on the four exact artifacts and signing fingerprints below, given cryptographically valid signatures dated after the available certified expiries and incomplete publisher provenance?

**No alternative is selected. No security risk is accepted or rejected.**

| Governance item | Current state |
| --- | --- |
| Risk classification | PENDING |
| Security disposition | PENDING |
| Architecture disposition | PENDING |
| Independent review requirement | PENDING RISK CLASSIFICATION |
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

**Risk classification: PENDING.** No low-risk, high-risk, compliant or compromised classification is assigned here.

The accountable assessment must consider the artifacts' build-time or runtime role, effective dependency reachability, build and release trust boundaries, potential consequences of publisher or signing-key compromise, reversibility and available objective evidence. Applicable high-risk review obligations remain conditional on that assessment; no universal independent-review requirement or exemption is inferred.

Residual uncertainties include:

- Whether authenticated publisher renewal certifications or a defensible publisher explanation exist but were not obtained.
- Whether complete provenance and key-lifecycle evidence supports continued reliance on each artifact.
- Whether inspected key material reflects current revocation state.
- How reliance on these existing trust rules affects build inputs and release confidence.
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
| Security Authority | PENDING | No acceptance or rejection is recorded. |
| Architecture Authority | PENDING | No acceptance or rejection is recorded. |
| Affected Engineering/component and Platform ownership review | PENDING | Review scope must follow the affected responsibilities and assessed risk. |
| Independent review requirement | PENDING RISK CLASSIFICATION | Do not infer either a universal requirement or a waiver. |
| Security Exception applicability | UNDETERMINED | Any mandatory waiver requires the separate §41 process. |

The project conversation identifies the repository owner as the initial Security Authority and Architecture Authority. This is attributable role context only; it supplies neither disposition. If approval in both capacities is later permitted under applicable risk and separation-of-duties requirements, both capacities and their dated evidence must be recorded explicitly. Such approval must not be described as independent review and cannot authorize a self-approved Security Exception.

Acceptance requires the applicable Security and Architecture authorities under Decisions §17, with affected-owner participation and any separately triggered review. A generic risk acceptance, ordinary PR approval or AI statement cannot replace a required Security Exception. This record remains Proposed until a separately authorized, evidence-backed lifecycle change occurs.

## Review Triggers

Review is required when new authenticated expiry-extension or revocation evidence, publisher clarification, contradictory artifact evidence, compromise indicators or relevant governance changes affect this assessment. Changes to artifact versions, fingerprints, trust scope, build/release exposure or proposed compensating controls require reassessment rather than automatic extension of any future disposition.

The eventual decision must specify its applicable review obligations and any time bounds. This proposal establishes no acceptance period or exception expiry.

## PR #219 Relationship and Registration Status

PR #219's successful CI at `7f02a4d45ebb6fd899b1a33d6f9733b6e4d9873e` is technical evidence only. **Merge authorization: WITHHELD.** This Proposed record neither approves that PR nor establishes that all merge or release gates are satisfied. It does not modify PR #219 or the other related PRs.

This initial single-file preparation intentionally leaves `DECISIONS.md` unchanged under the task's explicit scope. Index registration, metadata/history synchronization and validation of matching owner, date, title, status and canonical path remain outstanding under Decisions §§10 and 49–50. The record is not represented as registration-complete. No identifier is reserved by an index placeholder, and no governing baseline is changed by this preparation.

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
| 0.1.0 | 2026-10-16 | Proposed | Captured the four unresolved post-expiry signing cases, evidence limits and accountable-disposition requirements without selecting an alternative, granting approval or creating an exception; index synchronization remains pending. |
