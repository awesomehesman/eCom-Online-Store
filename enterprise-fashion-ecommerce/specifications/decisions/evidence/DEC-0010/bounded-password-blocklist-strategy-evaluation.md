# DEC-0010 bounded Customer password blocklist strategy evaluation

## Authorization and purpose

- Authorization: [DEC-0010](../../DEC-0010-customer-password-blocklist-strategy.md), 0.2.0 Proposed, Identity-owned Security Decision, Authoritative false.
- Authorization present at and repository baseline evaluated: `298a676b57573aad71e9a37662ddd1f79e2136ee`.
- Evaluation date: 2026-10-07 (Africa/Johannesburg).
- Evaluator: Codex AI assistant; accountable owner: Identity. No human review or approval is asserted.
- Purpose: documentary/static assessment only, not strategy selection or production admission.
- Investigation clock: 11:23:49–11:24:54 UTC (13:23:49–13:24:54 Africa/Johannesburg), approximately 65 seconds between recorded start and final source inspection. Evidence recording/validation follows; the eight-active-hour bound was not exceeded.

Question: Can a strategy within the authorized scope satisfy DEC-0007's requirement to evaluate the complete NFC-normalized prospective Customer password against known commonly used, expected, or compromised values while preserving whole-password matching, freshness, privacy, failure, fail-closed, safe-retry, supportability and operational requirements?

## Scope, method and actual counts

Authorized maxima: one pass, 12 discovery search queries, 30 distinct public documents/pages, four candidate arrangements, eight active investigation hours. Actual consumption:

| Measure | Used | Limit |
| --- | --- | --- |
| Discovery search queries | 0 | 12 |
| Distinct public sources | 6 | 30 |
| Candidate arrangements investigated | 2 | 4 |
| Investigation elapsed time | Approximately 65 seconds | 8 active hours |

Discovery used direct opening of official documentation/product/FAQ pages, followed by links to three additional official pages. No search-engine query was issued. Reopening sections and in-page text lookup did not add sources; all six opened public pages are counted, including peripheral FAQ content. No candidate was dropped to reclaim a slot. No additional candidate was investigated after the stop condition.

Candidates qualified because official documentation identified an attributable operator, relevant password-corpus capability, comparison information and two materially different consumption modes. Remote checking and local consumption of the same external corpus each consume a separate candidate slot. This provides bounded breadth across two classes, not an independent-source comparison. No library or repository-curated list was investigated; unused slots are not continuing authority after termination.

Labels used below: **R** = repository requirement/fact; **D** = external documented claim; **I** = evaluator inference; **U** = unresolved within inspected evidence. Documentary claims were not independently verified through execution. A missing statement in inspected pages is not proof that no statement exists anywhere.

## Repository authority inspected

The preceding repository review was retained and current-state differences verified: only DEC-0010 and DECISIONS.md changed between `c01aee05ac3a36297755331a97d6233d1acee499` and this baseline. Applicable unchanged authority comprises:

- DEC-0007, Accepted: complete NFC input, significant case/whitespace, whole-password matching, rejection and fail-closed checking, safe retry and no credential creation/replacement before successful policy evaluation.
- DEC-0006, Accepted: hashing/verification mechanics, not blocklist approval or authoritative credential currentness.
- DEC-0001 and SECURITY-STANDARDS.md: separate dependency admission, supply-chain, integrity, privacy and secret controls.
- ARCHITECTURE.md, Identity and Customer backend/domain specifications, ADR-0022 and Initial Customer Registration Contract/OpenAPI: Identity security ownership, Customer coordination, separate module authority and no false registration completion.
- DECISIONS.md 1.0.58 Approved and DEC-0010 0.2.0: single-use scope and separate gates.
- PreparedProspectiveCustomerPassword and its tests: malformed UTF-16 rejection, NFC and post-normalization 15–64 code-point preparation, explicitly awaiting remaining policy checks. Existing preparation tests are not blocklist conformance evidence; they were not executed here.

DEC-0009 remains unrelated, Proposed and deferred. Establishment/change rules must not be reinterpreted as Login-time acceptance rules. No upstream requirement is relaxed by this assessment.

## Source ledger

All sources were accessed on 2026-10-07. These are mutable public pages, not archived immutable releases; no snapshot or dataset was downloaded. Section names identify the inspected evidence. No candidate API endpoint or password form was called.

| ID | Official source | Version/date information and sections |
| --- | --- | --- |
| S1 | [HIBP API documentation](https://haveibeenpwned.com/API/v3) | Page titled API v3; Pwned Passwords overview, range search, padding, incremental searching, offline use, HTTPS and licensing. The page version is not an immutable password-corpus release. |
| S2 | [Pwned Passwords](https://haveibeenpwned.com/Passwords) | Unversioned product page: support, privacy, offline integration and negative-result limitation. |
| S3 | [HIBP FAQ](https://haveibeenpwned.com/FAQs) | Unversioned; password exposure meaning and general breach provenance discussion. Email-search statements were not treated as password-endpoint guarantees. |
| S4 | [HIBP NIST explanation](https://haveibeenpwned.com/NIST) | Unversioned provider explanation; corpus origin and compliance claims. Not an independent NIST certification or repository acceptance. |
| S5 | [Privacy Policy](https://haveibeenpwned.com/Privacy) | Marked updated May 2025; operator, Pwned Passwords, logging and processing. |
| S6 | [Terms of Use](https://haveibeenpwned.com/TermsOfUse) | Marked updated March 2026; general use obligations, changes and disclaimers. No legal conclusion asserted. |

### Concise documented facts

**D, S1:** Password checking uses hashed UTF-8 input; range requests send five hash characters and compare returned suffixes locally. Padding is documented, with zero-count dummy entries. Incremental typing queries are discouraged. Offline corpus use is documented. HTTPS is required. The password API requires no subscription key, and its specific licensing statement says no licensing/attribution requirement. No immutable corpus release was identified here.

**D, S2:** The provider offers remote and offline integration and advertises integration support through paid tiers. A negative lookup is described as absence from its index, not proof of password quality. Current product availability is not a maintenance guarantee.

**D, S3:** Password records indicate prior exposure and prevalence without identifying the password owner. General breach verification discussion does not prove integrity of every password entry.

**D, S4:** The provider attributes its corpus to breaches and stealer logs, including law-enforcement contributions, and claims suitability for the cited password-screening control. This substantiates a relevant investigation target, not independent verification of coverage or compliance with all repository obligations.

**D, S5:** The operator is Superlative Enterprises Pty Ltd. The policy describes prefix-based password checking and operational logging, including retention of web-server logs for up to 31 days. It does not justify assuming zero metadata exposure in this repository's future integration.

**D, S6:** General service terms contain use restrictions and disclaimers concerning availability and fitness. **U:** their exact application to local password-corpus consumption versus S1's specific password-API licensing statement is not resolved here. No incompatibility or legal permission is inferred.

## Candidate arrangements

### C1 — HIBP Pwned Passwords remote SHA-1 range checking

Class: remote service/provider capability. Investigated as a possible Identity-side complete-value lookup, not browser incremental checking or credential storage. Sources: S1–S6; live documented service, no exact backend deployment release established.

**Demonstrated documentarily:** an attributable comparison protocol and relevant exposed-password source (facts above). **I:** Identity could conceptually supply the prepared representation and retain full-result matching locally, but that is not evidence that upstream corpus normalization covers the same equivalence classes. Derived-information transmission creates a reviewable boundary even when plaintext is not transmitted. **U:** acceptable disclosure, normalization coverage, authoritative freshness detection and enforceable operating conditions remain unresolved. Current evidence does not support a concrete strategy proposal meeting all obligations.

### C2 — HIBP Pwned Passwords externally maintained corpus consumed locally

Class: external dataset consumed locally. Sources: S1–S6; no exact corpus snapshot/version, checksum manifest or acquisition was established. The linked downloader was not opened, evaluated, installed or run, and is not selected.

**Demonstrated documentarily:** offline use is an offered consumption mode. **I:** local checking could avoid per-attempt provider disclosure but transfers acquisition, validation, storage, freshness and recovery responsibilities to the repository. It does not cure upstream representation or coverage uncertainty. **U:** snapshot identity, authenticity/completeness, justified freshness, resource bounds, compatible usage conditions and lifecycle procedures remain unresolved. Current evidence does not support a concrete strategy proposal meeting all obligations.

## Complete DEC-0010 obligation matrix

Each row assesses both investigated arrangements. “Unresolved” means insufficient inspected evidence, not a demonstrated defect in the provider. References to documented mechanisms are to the concise facts above; additional controls below are repository requirements or inferences, not provider promises.

| Obligation | C1 remote | C2 local |
| --- | --- | --- |
| 1. Whole-password semantics | D: S1 comparison protocol is relevant. I: full-result matching must be enforced; prefix coincidence alone cannot reject. | I: full-value membership would be required; no checker is evaluated. |
| 2. NFC compatibility | U: corpus treatment of canonical equivalents not established. R: prepared representation must remain unchanged. | U: same corpus issue; local custody does not establish compatibility. |
| 3. Common/expected/compromised coverage | D: S4 supplies relevant provenance claims. U: adequacy for repository scope, including expected values. | Same source limitation; no independent dataset inspection. |
| 4. Provenance/integrity | U: completeness/authenticity evidence adequate for acceptance not established. | U: exact snapshot, integrity manifest and complete acquisition verification. |
| 5. Privacy/secrets | I: assess derived data and metadata against S5; redact locally. No disclosure approved. | I: per-check data can remain local; acquisition and diagnostics still need controls. |
| 6. Trust boundaries | I: external response influences mandatory Identity gate; review required. | I: imported data crosses supply boundary even without online checking. |
| 7. Support/maintenance | D: S2 support offering. U: accepted continuity/withdrawal arrangements. | U: repository maintainer and source-loss procedure. |
| 8. Licensing | U: reconcile S1/S6 applicability; not a legal finding. | U: confirm exact offline-use rights and constraints. |
| 9. Supply chain | U: future client/artifacts unspecified; no admission. | U: acquisition tools/data admission and provenance unspecified. |
| 10. Version/freshness | U: evidence establishing governed currentness/staleness boundary. | U: versioned snapshot, update conditions and stale cutoff. |
| 11. Determinism/reproducibility | U: no immutable response/corpus state established for replay. | I: pinned data could permit repeatability; no reproducible acquisition demonstrated. |
| 12. Resource bounds | U: repository-specific latency, response and concurrency limits. | U: capacity, indexing, validation and update resource requirements. |
| 13. Availability/failure | U: complete repository failure mapping; S6 is not an availability commitment. | U: unreadable, corrupt, partial-update and source-loss handling. |
| 14. Fail closed | R: every unsafe result must deny establishment. U: no enforcement implementation. | R: same; unavailable/stale local data cannot pass. |
| 15. Safe retry/recovery | R: no unsafe disclosure or fabricated acceptance. U: bounded recovery design. | U: safe update/retry and trustworthy rollback design. |
| 16. Testing/conformance | U: no repository-profile conformance evidence; documentation is not execution. | U: same; no dataset or checker was exercised. |
| 17. Operational lifecycle | R: Identity accountable. U: specific operator, incident and withdrawal duties. | U: acquisition, release, restore and retirement responsibilities. |
| 18. Architecture/deployment | I: external integration needs assessment, not automatic ADR acceptance. | I: data distribution/runtime footprint needs assessment; no topology selected. |

## Findings and stop rationale

Security/privacy: neither arrangement is approved to receive real secrets or derived lookup material. Hash membership is not credential hashing authority, and cannot substitute for DEC-0006. Neither preparation nor a negative provider lookup may independently establish complete policy acceptance. No policy or Login semantics changed.

Licensing/supply chain: attributable public documentation exists, but precise usage applicability and a production integrity/admission basis are not established. No incompatible terms are conclusively found. Resolving terms for a concrete deployment is a conditional downstream review, not permission inferred from the word “free.”

Operational/Architecture: freshness, trustworthy updates, failure classification, recovery, bounded resources and operational ownership require evidence and governed choices. These are not silently supplied by provider marketing or by local execution. Neither arrangement was found to mandate an Architecture change; the assessment remains outstanding.

Stop condition encountered: material evidence necessary to establish source-representation compatibility and a defensible currentness/integrity basis was not established within the inspected documentation. Completing those conclusions would require additional evidence/clarification and potentially separately authorized validation. The evaluator stopped rather than infer corpus semantics, seek new candidates, call a service or acquire data. This is a conservative evidence-limited stop, not a claim that all public information was exhausted or that experimentation alone would solve corpus-coverage questions. Limits were not exhausted; unused allowance is not a requirement to continue after a stop condition.

## Terminal outcome and consumption

**C. STOPPED / INCONCLUSIVE**

The documentary investigation did not responsibly establish all material requirements for a concrete strategy proposal. It stopped with explicit representation, freshness, integrity and usage uncertainties before candidate execution or adoption. This is not technical infeasibility, a rejection of all HIBP use, or a conclusion about uninvestigated classes. No completed adverse conformance assessment is claimed.

On durable recording of this terminal outcome, the DEC-0010 0.2.0 single-use evaluation authorization is **CONSUMED / EXHAUSTED**. Research ceased before recording. No automatic retry, second evaluation, candidate substitution, continuation or expansion is authorized; fresh explicit governance authorization is required.

## Separate downstream gates and activity confirmation

DEC-0010 remains Proposed. This artifact selects or accepts no strategy. Any future strategy proposal requires represented Identity, Security, Architecture and Engineering/Operations review. Product/Customer review applies if observable semantics materially change. DEC-0001 executable-dependency admission, applicable Architecture/ADR synchronization, provider/integration adoption, conditional privacy/legal/licensing review, operational conditions and implementation readiness remain separate. None is satisfied automatically by this evidence.

Performed: repository read-only inspection, six public documentation-page inspections, static comparison and this single evidence record. No real Customer secret was used; no candidate dataset/artifact download, installation, provider API call, provisioning, experiment, spike, benchmark or candidate test occurred. No production/test/build/Contract/persistence/Architecture/governance file was modified. DEC-0010, DECISIONS.md and DEC-0009 remain unchanged. No stage, commit, push, merge or PR creation occurred. The evidence file is intentionally untracked pending user review; creation does not stage it.
