# DEC-0042 — Product Publication Acceptance and Visibility

- **Identifier:** DEC-0042
- **Title:** Product Publication Acceptance and Visibility
- **Type:** Product Decision / Security Decision
- **Status:** Proposed
- **Version:** 0.6.0
- **Date:** 2026-10-30
- **Last Updated:** 2026-11-12
- **Owner:** Product
- **Required Accountable Security Review:** Security
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## Context and Authority

The initial Product foundation supports stable identity, canonical Variant configurations,
explicit lifecycle reconstruction, create/load persistence, internal readiness assessment
and internal ID pagination. It does not establish content/media acceptance, publication
provenance or positive customer visibility. Published lifecycle alone is insufficient.

This record consolidates the Product-owner dispositions from the M1.3B assessment and
M1.3C instruction. P1–P4 are local disposition labels, not separate DEC identifiers.
The Product business policy below is explicitly owner-approved; the combined record
remains Proposed because the accountable Security choices are unresolved. This record
is not yet authoritative for implementation and does not claim canonical specification
synchronization. Existing authoritative Security requirements remain binding regardless
of this record's status.

## Accountable Dispositions and Approval Provenance

| Scope | Accountable capacity | Recorded disposition | Boundary |
| --- | --- | --- | --- |
| P1 — Content Acceptance Checklist 1.0 | Product Business Owner | APPROVED | Business acceptance policy below |
| P2 — Media acceptance and minimum image requirement | Product Business Owner | APPROVED, subject to Security controls | Product policy does not approve the security-check authority or operational controls |
| P4 — Lifecycle and acceptance reuse | Product Business Owner | APPROVED | Only the four specified transitions and reuse boundaries below |
| Initial Product operating model and additional business policy | Product Business Owner | APPROVED in Product-owner capacity only; recorded 2026-11-06 | Business responsibility and conditional eligibility only; no Staff assignment, Security/Identity approval or operational authority |
| S1 — Media security controls | Security Owner | PENDING | Checking authority, evidence and invalidation controls require accountable confirmation |
| P3 / S2 — Contextual Staff authorization implementation | Security Owner | PENDING | Assignments, separation, authentication strength and revalidation choices remain unresolved |

Approval provenance is the repository owner's explicit session statement that the Product
Business Owner approved P1, P2 and P4, reiterated in the M1.3C instruction to record those
confirmed decisions. Recorded on 2026-10-30; a separate original approval date was not
supplied. No Security approval, independent reviewer, signature or independent human
review is claimed. A recommendation or an unselected ACCEPT / REJECT / AMEND field is
not an accountable disposition.

## Product-Owner Business Disposition — Recorded 2026-11-06

The Product owner explicitly confirmed the following operating model and additional
business policy in the instruction authorizing this record. This is attributable
Product-owner business disposition only, recorded on 2026-11-06. No personal name,
independent reviewer, signature, Security or Identity approval is inferred. Existing
P1, P2 and P4 approvals retain their original scope and provenance.

| Confirmed direction | Approved Product business disposition and limitation |
| --- | --- |
| 1A — Initial operator | The founder is the only initial operator. This identifies intended business responsibility, not authenticated Staff identity or an access assignment. |
| 2B — Publication eligibility | The founder and future explicitly designated Product publishers may be eligible to publish only after required content and media acceptance. Designation alone grants no operational permission; assignments and contextual authorization remain governed separately. |
| 3C — Acceptance/publication separation | Separation is risk-based, subject to Security-approved restrictions. This permits neither blanket same-actor publication nor an assumed independent approval chain. |
| 4A — Media usage rights | The founder/Product owner initially owns media usage rights confirmation. Required evidence must be attributable, bound to the relevant revisions and withdrawable; responsibility alone is not evidence that rights have been verified. |
| Additional policy — Higher-risk Products | Candidate categories include disputed/uncertain media rights, unresolved security findings, sensitive/restricted content, invalidated acceptance evidence and exceptional publication handling. These are business candidates for Security assessment, not a completed Security classification or a closed taxonomy. |
| Additional policy — Mandatory evidence | Missing mandatory evidence always blocks publication. Risk classification does not override mandatory controls or permit publication with unresolved required acceptance. |
| Additional policy — Withdrawal | The founder initially handles Product withdrawal as business responsibility. Future operators require explicitly governed assignments; applicable Security controls remain mandatory for every operator. This grants no withdrawal permission and does not change P4's lifecycle boundaries. |

These confirmations reduce business ambiguity; they do not establish Staff identity,
Staff association, Role/Permission assignments, privileged assurance or operational
permissions. Customer Authentication, founder status and publisher designation do
not substitute for trusted Identity evidence or Product contextual authorization.
Media-rights confirmation does not establish media-security acceptance.

S1 checking authority, trustworthy evidence/compatibility and invalidation remain
unresolved. P3/S2 still requires accountable decisions on eligible assignments,
action classification, same-actor restrictions, applicable MFA/reauthentication,
independent approval, assignment withdrawal, in-flight revalidation, audit failure
and uncertain outcomes. The approved risk-based business direction does not choose
those Security or Identity controls. Administration coordination and required review
also remain unresolved; no parallel authorization model is introduced.

At the time of this Product-owner disposition, [DEC-0012 v0.2.0](DEC-0012-administrative-role-permission-matrix.md) remained Proposed
and unchanged by that disposition. Neither its full matrix nor its bounded operational policy is
approved by this disposition. DEC-0042 remains Proposed and non-authoritative;
overall acceptance and applicable synchronization remain separate gates.
**Operational publication remains NO-GO until required controls are implemented
and verified under the applicable governance and implementation authorization.**
Default-deny boundaries remain unchanged. No scanner, Identity Provider, audit
mechanism, Role, Permission identifier, code change or deployment is selected.

## Product-Owner-Approved Business Policy

### P1 — Content Acceptance Checklist 1.0

Acceptance must verify:

- A nonblank, accurate Product name and description.
- Supportable material claims and accessible, understandable customer-facing content.
- Valid Product family, canonical Attributes, explicit applicability and Variant
  configurations under Accepted DEC-0019, preserving Product-scoped uniqueness.
- Acceptance bound to the exact Product content/configuration revision.

Acceptance is attributable, versioned and auditable. Covered content/configuration
changes, withdrawn supporting facts or explicit revocation invalidate reliance on the
corresponding acceptance. Valid Attributes alone cannot establish publication eligibility.
This checklist introduces no additional mandatory descriptive field catalogue, SKU format,
Colour vocabulary or change to DEC-0019.

### P2 — Media Acceptance

Require revision-bound evidence of correct Product/Variant association, permitted
storefront usage rights, security acceptance, accurate and usable representation,
meaningful accessibility information, and immutable asset and association revision
references. A linked Variant must belong to the owning Product.

Initial publication requires **at least one accepted Product-level image**. A separate
image for every Variant is not universally required. Withdrawal or invalidation of the
last required accepted image must immediately prevent positive Product visibility.
Availability, upload or processing success is not acceptance. Replacing asset content
or relevant association meaning cannot silently reuse evidence for the previous revision.

The numerical minimum is newly recorded Product-owner-approved policy, not a claim that
the existing Domain, backend or catalogue Contract previously specified that minimum.
Its implementation remains conditional on accountable Security controls and governing-source
synchronization; this step changes no Contract or Approved Specification.

### P4 — Lifecycle and Acceptance Reuse

The approved initial transitions are:

| Source | Target | Required boundary |
| --- | --- | --- |
| DRAFT | PUBLISHED | Current applicable acceptance, publication authorization, concurrency protection and attributable audit evidence |
| PUBLISHED | DRAFT | Explicit authorized withdrawal with concurrency protection and attributable audit evidence |
| PUBLISHED | ARCHIVED | Explicit authorized archival with concurrency protection, audit and historical-reference preservation |
| DRAFT | ARCHIVED | Explicit authorized archival with concurrency protection, audit and historical-reference preservation |

No initial Archived reactivation is supported. Publication-relevant edits require explicit
withdrawal to Draft before modification; edits cannot silently transition lifecycle.
Acceptance may be reused only when the exact accepted subject is unchanged, applicable
criteria remain valid and no relevant invalidation exists. Reuse does not reuse a withdrawn
publication grant or authorize republishing: a new publication action needs current authority.

Evidence invalidation must prevent unsafe visibility without silently changing lifecycle.
Unpublication and archival need not wait for content to become publishable. Stable Product
and Variant identity, accepted membership and historical commercial snapshots remain protected.

## Existing Authoritative Security and Ownership Requirements

These obligations are inherited, not new pending recommendations:

- Enforce current server-side contextual Authorization at the trusted entry point and owning
  Use Case, with default denial and exact Resource, action and applicable state checks.
- Identity supplies trusted Principal/access evidence; a Session, Role label, Claim,
  Permission value or identifier alone cannot authorize Product actions.
- Product owns its invariants and contextual business Authorization. Administration coordinates
  governed Staff workflows without acquiring Product publication authority.
- Private, Draft, invalid, unauthorized or otherwise non-publishable media cannot be public;
  applicable rights, security, quality, accuracy and accessibility requirements remain binding.
- Material publication, withdrawal and archival require attributable audit evidence. Audit
  records must preserve applicable actor/system, trusted time, Resource, action, state/outcome,
  authorization context and correlation, with access control and tamper protection.

Security Standards §§12 and 27, REQ-PRD-014–020, 043, 049–050, BPRD-REQ-007, 037, 044,
BIDN-REQ-004, 021, 024–026 and REQ-ADM-007, 014, 023 establish these boundaries.

## Recommended Security Controls — Awaiting Confirmation

### S1 — Media Security Acceptance

Recommend authoritative, revision-bound evidence identifying the security check and its
version, result, checking source and exact asset/association revision. Missing, failed,
stale, withdrawn or invalidated evidence must never grant positive visibility. Security
must confirm the accountable checking authority, acceptable supporting evidence, version
compatibility, invalidation triggers, finding/withdrawal handling and controls excluding
private references or credentials from public media.

The fail-closed obligation follows existing requirements; the specific evidence producers,
check definitions and operational implementation are not approved by this record.
No independent Media service, scanner, provider or pipeline is selected.

### P3 / S2 — Contextual Staff Authorization

Recommend separate contextual decisions for content acceptance, media acceptance,
publication, withdrawal and archival, each bound to its Product and applicable revisions/state.
No named Permission, Role hierarchy, Staff assignment or emergency override is invented.
No mandatory dual approval is introduced by this proposal; any applicable approved Security
policy remains binding, and permission for the same actor to accept and publish is not assumed.

Security confirmation remains required for:

1. Applicable governed Staff assignments for each action.
2. Whether the same actor may accept and publish.
3. Any stronger Authentication or independent approval requirement.
4. Authorization revalidation and in-flight access-withdrawal handling.
5. Security events invalidating current acceptance or publication reliance.

DEC-0012 remains Proposed; this record neither resolves its matrix nor invents assignments.

## Consolidated Disposition Package — Awaiting Accountable Review

**ALL OPTIONS AND RECOMMENDATIONS BELOW ARE NOT APPROVED.** This package makes
S1 and P3/S2 reviewable together; it records no accountable disposition. P1, P2 and
P4 remain approved only in their previously recorded Product-owner capacity and
are not reopened. DEC-0042 remains Proposed and non-authoritative. No option below
selects an evidence producer, Staff assignment, operational permission or implementation.

### A. Already Mandatory Controls

The preceding Existing Authoritative Security and Ownership Requirements section
continues to apply. [Security Standards §§7.6, 10–13 and 27](../../.ai/core/SECURITY-STANDARDS.md)
also govern separation of duties, trusted identity, RBAC, privileged assurance,
revocation and audit. MFA for privileged Staff and applicable high-impact
reauthentication are existing obligations, not optional controls created here.
BIDN-REQ-011–014, 021–026, BADM-REQ-005, 007, 014, 045 and BPRD-REQ-007,
019–023, 030, 037 preserve their respective ownership boundaries. This package
identifies unresolved application of those controls rather than reproducing or
weakening the authoritative requirements.

### B. S1 Options and Outstanding Accountable Choices

Every row is **RECOMMENDED / NOT APPROVED**. Security must resolve the checking
boundary with affected Product and rights-evidence ownership; Product retains the
final acceptance and publication decision.

| Outstanding choice | Concrete bounded options and trade-off | Recommended initial direction for disposition |
| --- | --- | --- |
| Media-security checking authority | Governed human assessment, governed automated checking, or a combination; human review supports judgment but cannot substitute for required technical checks, while automation cannot establish rights or content accuracy by itself. | Security-designated checking authority, using appropriate technical evidence and accountable review for judgment-dependent checks; confirm permitted producers and check coverage without selecting a scanner, service or provider. |
| Acceptable evidence provenance | Direct trusted producer evidence or a governed attributable assessment of supporting evidence; copied claims and database recording alone provide no checking authority. | Require trustworthy origin, applicable checking authority, result and supporting-evidence provenance; resolve how authenticity/current applicability are established before reliance. No signature scheme or evidence schema selected. |
| Asset/association/revision binding | Exact immutable asset and association revision binding versus reliance on mutable URLs or labels; mutable references cannot identify the checked subject reliably. | Exact Product, asset, association and relevant representation revisions, with Variant membership where applicable; replacement or changed association meaning requires new evaluation. Preserve P2 rather than adding a new completeness threshold. |
| Check compatibility and versioning | Re-evaluate when a check changes, or permit explicitly reviewed version compatibility; blanket rechecking costs more, while implicit compatibility risks stale assurance. | Only expressly supported check definitions/versions may support reliance; unsupported, retired or uncertain compatibility withholds acceptance pending governed re-evaluation. Security must define compatibility and retirement authority. |
| Media-security invalidation | Explicit revocation, changed asset/association, superseded checks, new findings or compromised provenance may invalidate reliance; the exact triggers and authoritative sources are unresolved. | Govern explicit invalidation sources and affected-subject propagation; preserve history and fail closed for missing, failed, stale, conflicting or invalidated evidence. No event transport or invalidation mechanism selected. |
| Rights/security withdrawal | Stop relying on an affected association and withhold the Product if required completeness fails, versus allowing continued visibility until later review; continued unsupported reliance conflicts with existing requirements. | Apply authoritative withdrawal to every affected association and withhold positive visibility when the last required accepted image is lost; no silent lifecycle transition or revival. Identify rights/security sources and final-reliance coordination before implementation. |

**Recommended S1 disposition for consideration — NOT APPROVED:** accept the bounded
direction of trustworthy, version-compatible, exact-subject security evidence and
explicit invalidation, with Product-owned publication and rights/security withdrawal
preserved. Accountable review must identify permitted checking sources, acceptable
provenance, check coverage/compatibility, invalidation authority and failure handling.
Restating these as future conditions does not close an unresolved producer or trust
choice. No operational reliance follows until those choices are expressly resolved.

### C. P3/S2 Options and Bounded DEC-0012 Dependency

[DEC-0012 v0.4.0 Proposed](DEC-0012-administrative-role-permission-matrix.md), including its
founder-operated MVP bounded disposition and Product-Specific
Accountable Disposition Package A–H, supplies the five-action review scope and
eight pending Product-specific confirmations. It remains non-authoritative, with
no overall matrix option selected and all eight confirmations pending accountable
approval. P3/S2 depends on explicit disposition of that bounded authorization
package; its existence does not establish accepted policy or operational authority.
The action descriptions below are not Permission identifiers. Each option and
recommendation is **NOT APPROVED**.

| Outstanding choice | Concrete bounded options and trade-off | Recommended initial direction for disposition |
| --- | --- | --- |
| Product action assignments | Fixed, capability-granular or hybrid RBAC-compatible grouping; broad bundles simplify administration but may grant excessive authority. | Review eligibility separately for content acceptance, media acceptance, publication, withdrawal/unpublication and archival, bound to exact Product and applicable revisions/state. Resolve only the necessary Product subset; invent no Roles, identifiers or assignments. |
| Same-actor acceptance/publication | Permit with explicit safeguards, prohibit, or apply risk-tiered separation; same-actor work is simpler but concentrates acceptance and release authority. | Prefer an initial separation between acceptance and publication where practical, subject to explicit Product/Administration/Security disposition and operational feasibility. No same-actor permission or binding two-person rule is established. |
| Privileged-action classification | Classify each action separately or apply a common conservative privileged baseline; the latter simplifies safeguards but may overclassify routine work. | Treat publication, withdrawal and archival as candidates for high-impact classification and evaluate content/media acceptance by their effect on visibility. Security must explicitly classify every action and resolve any difference. |
| MFA and reauthentication applicability | Apply the existing privileged baseline plus action-specific assurance, or stronger assurance where justified; extra challenges can burden urgent safe withdrawal. | Require trustworthy evidence of all applicable mandatory controls; Security must settle action-specific privileged-session, MFA and reauthentication applicability with Identity. No mechanism, lifetime or exception selected. |
| Independent approval | Separate acceptance from publication, add distinct action approval where risk requires it, or explicitly permit execution without another approval where governing controls allow. | Evaluate acceptance/publication separation first; add separate independent approval only on explicit risk evidence and accountable disposition. Settle action combinations, evidence and any urgent-withdrawal treatment; no approver count, chain or override invented. |
| Assignment withdrawal | Govern grant/change/withdrawal through Identity-owned assignments and Administration coordination, or defer operational eligibility; a parallel Product assignment store would create competing authority. | Preserve one governed access model, attributable assignment lifecycle and effective revocation. Identify eligible provisioning/withdrawal authority through DEC-0012 review; no new actor selected. |
| In-flight authorization revalidation | Re-observe authority at the final governed mutation boundary with conflict rejection, or adopt stronger coordination if separately justified; revalidation alone does not prove cross-system atomicity. | Check current authority for every operation and prevent known withdrawal, stale/conflicting evidence or changed Product state from enabling mutation. Explicitly settle final reliance, residual race and recovery semantics; select no TTL, lock or transaction strategy. |
| Audit failure and uncertain outcomes | Prevent initiation when required durable audit cannot be guaranteed; for an uncertain commit, verify/reconcile instead of assuming failure or automatically repeating. Alternative guarantees require equivalent governed integrity. | Prefer fail-closed initiation when mandatory audit assurance is unavailable and truthful uncertain-outcome handling with governed reconciliation. Security/Product/Administration must settle attributable evidence, durability, failure and recovery obligations; no audit infrastructure, outbox or retry mechanism selected. |

**Recommended P3/S2 disposition for consideration — NOT APPROVED:** accept separate
Product-owned contextual evaluations using trusted Principal evidence, separately
confirmed Staff association, current Identity-owned assignment provenance/scope and
applicable assurance. Authentication, a Role label or an assignment alone cannot
authorize a Product action. Administration coordinates only governed workflows;
Product decides against the current Resource, action, revision and lifecycle.
All eight choices above require explicit accountable treatment. Evidence invalidation
and access provisioning authority are not automatically granted by these five actions.

The bounded subset does not approve the full Administration matrix or resolve all
of `PRODUCT.md` §24 item 23. Required Product, Administration, Identity and Security
review must establish an authoritative bounded policy through the applicable
acceptance and synchronization process before reliance. A recommendation or partial
approval note within either Proposed record is not operational authority. S1 media
checking authority is not resolved merely by assigning media-acceptance eligibility.

### D. Implementation Prerequisites and Disposition Readiness

This package is ready for accountable consideration, not implementation. Remaining
gates are explicit S1 and P3/S2 dispositions, the bounded DEC-0012 policy dependency,
overall acceptance, applicable canonical/Contract synchronization, and separately
reviewed trusted evidence, contextual Authorization and audit boundaries. The existing
catalogue Contract governs public reads, not an operational publication command API.

Acceptance of recommendations must not be recorded as executed verification or
trusted production evidence. Missing or uncertain authority remains non-confirming.
No existing PUBLISHED row, retained claim, Customer authentication mechanism or
synthetic fixture becomes a publication producer. Positive visibility, source-code
changes, migrations, endpoints, deployment and operational rollout remain unauthorized.

## S1 Accountable Disposition Package — Prepared 2026-11-09; Updated 2026-11-12

**PROPOSED DISPOSITION — NOT APPROVED.** S1-01–S1-06 are local review labels for
the six S1 questions above, not accepted check definitions, evidence sources or
new decision identifiers. All six remain **PENDING ACCOUNTABLE APPROVAL**.
Accountable owners below are review capacities, not named reviewers or recorded
concurrence. Confirmation must identify capacity, date, exact scope and conditions;
no such confirmation is recorded by this package.

**ESTABLISHED REQUIREMENT** refers to the Existing Authoritative Security and
Ownership Requirements and Consolidated Disposition Package A above, including
Security Standards §§10–13 and 27, REQ-PRD-014–020, BPRD-REQ-019–023 and 030.
**CONFIRMED BUSINESS DIRECTION** refers only to the recorded Product-owner
operating model and P1/P2/P4, with their existing provenance and limitations.
Neither those approvals nor the recommendations below establish Security approval.

**EXISTING IMPLEMENTATION EVIDENCE** describes observed technical behavior only.
It is neither an approved Security policy nor authoritative media acceptance.
**PROPOSED POLICY CANDIDATE** is a conservative recommendation for accountable
review. No candidate is effective unless the accountable owner explicitly accepts
it and the remaining implementation and operational gates are completed.

### S1-01 — Media-Security Checking Authority

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Public media requires applicable security acceptance; upload, processing success, URL availability and Product acceptance alone cannot establish trusted checking authority. Product retains publication authority. |
| CONFIRMED BUSINESS DIRECTION | P2 requires accepted media, including at least one accepted Product-level image initially. The founder/Product owner confirms usage rights, not media-security authority. |
| EXISTING IMPLEMENTATION EVIDENCE | Product has an internal checking composition that retrieves exact private-quarantine bytes, revalidates Product ownership and SHA-256, performs actual PNG/JPEG content decoding and bounded resource inspection, and invokes ClamAV `INSTREAM`. It records `CLEAN`, `INFECTED` or `INDETERMINATE` malware observations separately from technical inspection. Both observation models and their database constraints prohibit authoritative-acceptance eligibility. No operational acceptance actor or trusted invocation boundary is established. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | Designate the Product-owned controlled checking composition, not ClamAV by itself, as the initial eligible technical producer. Mandatory automated coverage comprises original-byte integrity, permitted-format/content inspection, decoding/resource limits and a determinate clean malware scan using sufficiently fresh signatures. Rights, accuracy, quality, meaningful alternative-text judgment and correct Product/Variant association remain distinct acceptance responsibilities. Unsupported mandatory coverage denies acceptance. |
| Accountable owners | Security: permitted checking authority and coverage; Product: acceptance and publication boundary, with affected rights-evidence ownership. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Security must accept, amend or reject the Product-owned composition as an eligible technical producer; confirm whether the implemented technical inspection plus ClamAV coverage is sufficient for the initial media scope; identify any additional mandatory image-safety checks; and define the trusted operational state, invocation boundary, unsupported coverage and resulting denial. Product must confirm the boundary between technical results and its acceptance decision. |
| Consequence if unresolved | No producer or founder assertion may be presumed authoritative; required media-security acceptance and positive publication remain blocked. |
| Implementation dependency | The candidate pipeline exists, but authoritative acceptance orchestration, invocation authorization, operational monitoring, approved composition and executed operational evidence remain absent. A raw ClamAV result must never be promoted directly. |

### S1-02 — Evidence Provenance and Authenticity

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Missing, uncertain, contradictory or untrusted evidence cannot support acceptance or visibility. Caller claims, copied findings and database storage alone establish no authority. |
| CONFIRMED BUSINESS DIRECTION | P1/P2 require attributable, revision-bound acceptance; the founder's rights confirmation must be attributable and withdrawable. This does not approve a security evidence source. |
| EXISTING IMPLEMENTATION EVIDENCE | Append-only technical and malware observations record Product identity, immutable quarantine asset identity, original SHA-256, execution time, result and implementation/scanner attribution. Database foreign keys preserve Product/asset/digest binding, immutable-history triggers reject ordinary update/delete, and constraints require `authoritative_acceptance_eligible = false`. These controls do not authenticate an acceptance actor and do not prevent privileged database administration from fabricating or altering trust material. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | For the initial single-service MVP, rely only on observations produced inside the Product trust boundary by the approved checker, persisted through the Product-owned adapter and retrieved through Product-owned Ports under reviewed database access controls. Caller DTOs, headers, copied results, arbitrary database rows and retained unconfirmed evidence history remain untrusted. Require attributable invocation, protected storage access, safe correlation and a reviewed trusted-time basis. A separate evidence signature is not required while producer and consumer remain within one approved trust boundary; crossing that boundary requires a new authenticity disposition. |
| Accountable owners | Security: accepted provenance and authenticity boundary; Product: accepted evidence association and reliance; Identity where producer identity evidence is needed. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Security must accept, amend or reject the proposed in-process/database trust boundary; confirm required database access separation, trusted-time, actor attribution, correlation and audit guarantees; and decide when signed or otherwise cryptographically authenticated evidence becomes mandatory. Identity must confirm any Staff/producer identity evidence used. Missing, conflicting or unavailable provenance remains non-accepting. |
| Consequence if unresolved | An asserted result cannot become trusted media acceptance; no positive reliance is permitted. |
| Implementation dependency | No authoritative acceptance record/store, authenticated acceptance actor, approved producer identity, protected operational audit or positive publication-evidence adapter exists. Database immutability alone is insufficient provenance. |

### S1-03 — Subject and Revision Binding

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Product/Variant association and immutable subject identity must be preserved; unrelated or stale checks cannot establish current acceptance. |
| CONFIRMED BUSINESS DIRECTION | P2 already requires exact asset and association revisions and same-Product Variant membership. P1/P4 restrict acceptance reuse to unchanged subjects with still-valid criteria and no invalidation. |
| EXISTING IMPLEMENTATION EVIDENCE | Quarantine and malware observations bind a generated asset UUID, owning Product identity and SHA-256 of original bytes. The publication-evidence contract can represent Product revision, media-association identity, asset identity/revision, association revision and optional Variant identity. No authoritative mapping currently joins the quarantine asset UUID/SHA-256 to the current Product Media association, its opaque asset revision, Product representation revision and optional Variant membership. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | Bind authoritative security acceptance simultaneously to the Product identity, exact Product representation revision, quarantine asset UUID, original SHA-256, Product Media association identity, asset revision, association revision and optional Variant identity. The Variant must belong to the same Product. Asset-byte/digest change, Product or association revision change, Variant reassociation or changed association meaning requires new evaluation. URL, filename, MIME claim, label and storage location are never subject identity. |
| Accountable owners | Product: subject identity, membership, revision and reuse semantics; Security: adequacy of checking-subject binding. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Product must confirm the canonical mapping among quarantine asset UUID/SHA-256, Product Media asset revision and association revision, including Variant membership. Security must confirm the complete binding is sufficient and identify every change that invalidates security reliance at final publication and visibility evaluation. |
| Consequence if unresolved | No mutable-reference or previous-revision evidence may be substituted for current acceptance. |
| Implementation dependency | Implement the explicit Product-owned association binding and exact-match evaluation before positive acceptance. Existing publication-evidence shapes may be reused, but shape validation does not establish trust. |

### S1-04 — Check-Definition/Version Compatibility

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Stale, unsupported or uncertain evidence cannot grant visibility; current evidence must satisfy applicable requirements. |
| CONFIRMED BUSINESS DIRECTION | P4 allows reuse only while applicable criteria remain valid and no relevant invalidation exists. No Product disposition selects check definitions, versions or compatibility rules. |
| EXISTING IMPLEMENTATION EVIDENCE | Technical inspection reports `product-image-technical-validation` implementation `1.0.0`. ClamAV reports scanner identity `clamav-clamd`, check definition `clamav-instream-malware-scan` and implementation `1.1.0`, with observed engine, signature database version and signature timestamp where available. `CLEAN` becomes `INDETERMINATE` when version data is unavailable or the signature timestamp is missing, malformed, more than the configured future skew ahead, or older than the configured maximum. Spring defaults are 48 hours maximum signature age and five minutes future clock skew. `VERSION` is observed before, but is not atomically bound to, `INSTREAM`; signature-update monitoring and alerting are not implemented. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | Initially support only the explicitly reviewed technical-check definition/version and ClamAV integration version executed by the approved producer. Reject unknown, retired or uncertain definitions, implementations and engine compatibility. Adopt 48 hours maximum signature age and five minutes future clock skew as conservative initial policy candidates, conditional on reliable signature updating, UTC clock synchronization, health monitoring, pre-threshold alerting and an incident/recheck procedure. A material checker defect, unsupported engine family, compromised producer or retired definition requires re-evaluation. |
| Accountable owners | Security: check coverage, supported versions, compatibility and retirement authority; Product: acceptance-reuse and visibility consequences. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Security must accept, amend or reject the named definitions/implementation versions, ClamAV engine/container compatibility policy, 48-hour age, five-minute skew, non-atomic `VERSION`/`INSTREAM` limitation, monitoring obligations, retirement/recheck authority and scanner-CVE or definition-rollback response. Product must confirm how incompatibility affects existing acceptance and visibility. |
| Consequence if unresolved | Neither a version label nor an old success result establishes acceptable current security coverage. |
| Implementation dependency | Implement an approved source/definition/version policy and operational monitoring only after disposition. **Scan-time freshness is distinct from accepted-evidence reuse lifetime:** the former qualifies scanner state at execution; the latter governs how long an acceptance may be relied on and may require rechecking sooner, later only with explicit compatibility approval, or immediately after an invalidating event. No reuse lifetime is approved here. |

### S1-05 — Evidence Invalidation and Withdrawal

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Missing, failed, stale, conflicting, withdrawn or invalidated evidence cannot support positive visibility. Historical evidence must not silently restore withdrawn authority. |
| CONFIRMED BUSINESS DIRECTION | P1/P2 invalidate affected acceptance after relevant changes or withdrawal; P4 requires valid criteria and no invalidation for reuse and forbids silent lifecycle changes. |
| EXISTING IMPLEMENTATION EVIDENCE | Publication-evidence contracts distinguish current, invalidated and revoked evidence and explicit invalidation/withdrawal. The readiness evaluator rejects invalidated, withdrawn, unsupported, conflicting or unavailable evidence. Production composition remains fail-closed through an unavailable evidence adapter and reject-all source/version policy. No authoritative invalidation producer, propagation mechanism or current-evidence store exists. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | Invalidate every affected security acceptance on asset/digest mismatch, Product or association revision change, Variant reassociation, an `INFECTED` or `INDETERMINATE` recheck, stale or unsupported definitions, producer compromise, material checker defect, authoritative adverse finding, explicit Security revocation or conflicting evidence. Preserve history, prohibit automatic reactivation and require new approved checking before restored reliance. Unavailable invalidation state fails closed. |
| Accountable owners | Security: authoritative security findings, revocation and invalidation scope; Product: affected acceptance, associations, history and current visibility. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Security must accept, amend or reject the proposed triggers; designate authoritative adverse-finding/revocation sources; and confirm affected scope, propagation/freshness bound, conflict/outage treatment, proactive recheck obligations and restored-reliance criteria. Product must confirm affected acceptance and visibility consequences. |
| Consequence if unresolved | Continued validity cannot be assumed after relevant uncertainty or withdrawal; positive reliance remains unavailable. |
| Implementation dependency | Append-only authoritative acceptance/invalidation persistence, current-evidence projection, delivery/recheck coordination and reconciliation remain missing. No event, polling, TTL, locking or transport is selected. |

### S1-06 — Rights/Security Withdrawal Coordination

| Review element | Disposition preparation |
| --- | --- |
| ESTABLISHED REQUIREMENT | Media must retain current rights/security eligibility; incomplete or uncertain evidence cannot be represented as accepted public content. Product owns visibility and lifecycle. |
| CONFIRMED BUSINESS DIRECTION | The founder/Product owner initially confirms attributable, revision-bound, withdrawable usage rights. P2 immediately prevents positive visibility when the last required accepted Product-level image is lost. P4 separates evidence invalidation from explicit lifecycle transitions and preserves history. |
| EXISTING IMPLEMENTATION EVIDENCE | Rights acceptance and security acceptance are separate typed inputs to publication readiness, and readiness requires both for each exact media subject. No authoritative rights-evidence producer/store, cross-evidence withdrawal coordinator, operational Staff acceptance flow or protected audit implementation exists. |
| PROPOSED POLICY CANDIDATE — NOT APPROVED | Maintain independent current rights and security acceptances. Withdrawal or invalidation of either applies to every affected association and immediately withholds visibility when required completeness fails. Revalidate both at final publication and visibility reliance. Evidence withdrawal does not silently mutate Product lifecycle; `PUBLISHED` to `DRAFT` remains a separately authorized Product action. In-flight publication fails when current rights/security evidence cannot be confirmed. Cached success, delayed review and founder responsibility cannot substitute for current evidence. |
| Accountable owners | Product: rights confirmation, affected associations, completeness and visibility; Security: security withdrawal and final-reliance safeguards; Administration/Identity for separately governed operational action/access dependencies. |
| PENDING ACCOUNTABLE APPROVAL — exact confirmation | Product must confirm authoritative rights sources, affected-association scope and immediate visibility consequences. Security must confirm security-withdrawal sources, final-reliance and in-flight behavior, uncertainty treatment and recovery. Identity/Administration must confirm the actor/access dependencies for any operational acceptance or lifecycle mutation. |
| Consequence if unresolved | No delayed review, cached success or founder responsibility may substitute for current required rights/security evidence; publication remains blocked. |
| Implementation dependency | Separate authoritative rights/security records, one fail-closed current-evidence projection, withdrawal coordination, protected audit and recovery remain missing. P3/S2 authorization is additionally required for any operational acceptance or lifecycle mutation. |

### P3/S2 Dependency and Package Limitations

[DEC-0012 v0.4.0 Proposed](DEC-0012-administrative-role-permission-matrix.md)
remains **non-authoritative**. Its eight Product-specific confirmations A–H all
remain **PENDING ACCOUNTABLE APPROVAL**: eligible assignments, risk-based same-actor
restrictions, privileged-action classification, action-specific MFA/reauthentication,
independent approval, Identity-owned assignment evidence, current authority/revision
revalidation, and protected audit/uncertain outcomes. P3/S2 remains unresolved and
cannot be closed by this S1 package, founder designation or authenticated Sessions.
Conversely, assignment eligibility cannot designate an S1 checking authority.

An implemented technical checking composition exists, but no checking producer,
check definition/version, freshness/reuse policy or evidence source is approved for
authoritative acceptance. No Staff Role/Permission or operational assignment is selected.
All S1-01–S1-06 and P3/S2 confirmations remain pending; no Security approval, overall acceptance or
publication authority is recorded. Accountable disposition, applicable canonical
and Contract synchronization, implementation authorization and executed verification
remain separate gates. Operational publication remains **NO-GO**; default-deny
administration and fail-closed visibility are preserved.

## Founder-Operated MVP Acceptance Package — Prepared 2026-11-11

**PROPOSED DISPOSITION — NOT APPROVED. PENDING ACCOUNTABLE PROJECT-OWNER
CONFIRMATION.** This package prepares the minimum Product acceptance, publication
and visibility policy for the founder-operated MVP and aligns with the bounded
[DEC-0012 v0.4.0 Proposed](DEC-0012-administrative-role-permission-matrix.md)
disposition. It preserves P1, P2 and P4 as already approved Product business policy
within their recorded scope; it does not convert them into Security approval, close
S1/P3/S2, authorize implementation or establish positive customer visibility.

### MVP Content Acceptance

Content acceptance applies to one exact Product representation revision. Positive
acceptance requires the current P1 content checklist: complete nonblank and accurate
Product name/description, supportable and accessible customer-facing content, valid
Product and Variant configuration, applicable accepted Attribute standard and
Product-scoped Variant uniqueness. Acceptance must be attributable, versioned and
bound to the accepted Product revision.

A covered content, configuration, applicability, Variant or supporting-fact change,
explicit revocation, supersession or other applicable invalidation prevents further
reliance on that acceptance. A historical acceptance, structurally valid Product or
caller-supplied completeness assertion cannot establish current content acceptance.

### MVP Media Acceptance and Controlled Ingestion

Initial publication requires at least one currently accepted Product-level image.
Every accepted media association requires current attributable usage-rights evidence,
correct same-Product Product/Variant association, meaningful alternative text, trusted
media-security checking evidence and exact immutable asset and association revision
binding. Variant-level images may supplement but cannot replace the required accepted
Product-level image. Upload success, processing success, an HTTPS reference or public
availability does not establish acceptance.

For accountable consideration, use one controlled ingestion boundary for initial MVP
media:

1. Hold candidate media in private quarantine before acceptance or public eligibility.
2. Establish an immutable asset identity and revision for the exact checked content.
3. Require Security-approved malware and image-safety checking appropriate to the
   accepted media scope; judgment-dependent rights, accuracy, quality and accessibility
   acceptance remains distinct from automated technical checking.
4. Retain trustworthy producer identity, checking authority, check-definition/version,
   result, provenance, trusted time and exact subject/revision binding.
5. Permit reliance only while the producer, check definition/version, evidence origin,
   result, rights and association remain currently supported and valid.
6. Apply authoritative rights or Security withdrawal, changed asset/association meaning,
   adverse findings, compromised provenance, superseded checks and explicit revocation
   to every affected association. Loss of the last required accepted Product-level image
   immediately withholds positive visibility without silently changing lifecycle.

The repository now contains private quarantine, immutable asset identity, technical
inspection and ClamAV integration, but this proposal does not approve those mechanisms
as an authoritative checking producer, accepted check definition/version, evidence
Contract or transport. S1-01–S1-06 remain pending until Security and affected Product
ownership explicitly approve actual sources, coverage, provenance, compatibility,
invalidation and withdrawal behavior and executed evidence demonstrates the selected
capability.

### MVP Risk-Based Publication Authorization

Subject to explicit accountable approval of DEC-0012 v0.4.0, the same founder may be
eligible to perform content or media acceptance and publication only for an
**ordinary-risk Product** after every mandatory acceptance, Security, assurance,
Authorization, concurrency and audit control succeeds. Ordinary risk excludes disputed
or uncertain media rights, unresolved Security findings, sensitive or restricted
content, invalidated acceptance evidence, exceptional publication handling and any
Product whose applicable risk cannot be authoritatively classified as ordinary.

Higher-risk and unclassified Products require revision-bound independent approval from
an eligible, separately authorized approver. Without an eligible approver and current
approval evidence, acceptance/publication remains blocked. Missing mandatory evidence
never becomes ordinary risk. No founder override, blanket same-actor permission or
universal two-person workflow is established by this proposal.

### MVP Publication and Withdrawal Lifecycle

The bounded MVP uses only the existing canonical transitions `DRAFT → PUBLISHED` and
`PUBLISHED → DRAFT`; it introduces no submitted, approved or other lifecycle state.
Existing P4 archival policy remains preserved but outside this bounded initial
publication slice.

Publication requires, at final mutation reliance:

- the exact current Draft Product and expected representation revision;
- current P1 content and P2 media acceptance for that revision, including the required
  Product-level image and no applicable invalidation or withdrawal;
- current active Staff association, Identity-owned assignment, Product contextual
  Authorization and fresh privileged assurance under the accepted DEC-0012 policy;
- satisfied ordinary-risk same-actor or higher-risk independent-approval evidence;
- expected-revision concurrency protection; and
- an attributable protected audit record committed atomically with the lifecycle
  mutation.

Withdrawal is a separately authorized `PUBLISHED → DRAFT` action under the current
Product revision, Staff/assignment/assurance evidence, contextual Authorization,
concurrency protection and protected atomic audit. It must not depend on continuing
content/media publication completeness and is not granted by publication eligibility.

Missing, stale, conflicting, unsupported, invalidated or unavailable required evidence,
changed Product state/revision, failed final Authorization or an unavailable mandatory
audit guarantee denies mutation. No partial lifecycle/audit success is permitted. An
uncertain commit outcome remains uncertain and must be investigated and reconciled
before retry or success is asserted; failure cannot become a blind replay or fabricated
success.

### MVP Customer Visibility

A Product may be positively eligible for catalogue representation only when its current
lifecycle is Published, its exact current representation revision retains valid accepted
P1 content and P2 media evidence, the required Product-level image remains accepted,
and no applicable acceptance, rights, Security, asset, association or publication
invalidation/withdrawal applies. Published lifecycle alone is never sufficient.

Visibility evaluation must fail closed when current evidence, invalidation state,
withdrawal coordination or authoritative Product observation is missing, stale,
conflicting, unsupported, unavailable or uncertain. A prior response, cache, projection,
historical acceptance or internal candidate page cannot supply continuing eligibility.
This package defines no positive catalogue response implementation and does not change
the existing customer Contract, pagination, concealment, error or `no-store` behavior.

### Required Accountable Project-Owner Dispositions

The project owner must explicitly accept, amend or reject this package while acting in
each applicable accountable capacity. This Proposed revision, its merge or later code
does not supply those dispositions.

| Accountable capacity | Exact confirmation still required |
| --- | --- |
| Product | Confirm the exact-revision P1/P2 acceptance, minimum Product-level image, canonical lifecycle transitions, withdrawal independence and current visibility prerequisites. |
| Security | Confirm the controlled-ingestion direction, actual checking authority/coverage/provenance/version/invalidation treatment, ordinary-risk same-actor allowance, higher-risk independent approval, privileged assurance and protected-audit/fail-closed boundaries. |
| Identity | Confirm the trusted Staff, assignment, assurance, freshness and revocation evidence required by the aligned DEC-0012 policy without treating Authentication as publication authority. |
| Administration | Confirm the founder-operated workflow, independent-approval coordination and absence of an override when required approval/evidence is unavailable. |

### Remaining Operational Gates

DEC-0012 v0.4.0 and this DEC-0042 revision remain Proposed and non-authoritative.
Operational publication remains gated on accountable acceptance and canonical/Contract
synchronization; actual S1 checking producer and evidence approval; controlled media
ingestion implementation; operational Staff Authentication, association, assignments,
assurance and revocation; Product-owned acceptance/authorization enforcement; atomic
lifecycle, audit and uncertain-outcome handling; positive visibility implementation;
executed verification; and explicit operational activation. Default-deny Product
administration and fail-closed customer visibility remain mandatory until every
applicable gate is complete.

## Implementation Boundary and Acceptance Prerequisites

After remaining accountable review and required governing-source synchronization, implementation
must preserve revision-bound provenance and explicit invalidation rather than accept caller
authorization/completeness booleans. Product application services own transactions under
Architecture §35.2 and BPRD-REQ-011; narrow persistence operations must preserve atomicity,
concurrency, identity and historical integrity. Final positive reliance must coordinate with
withdrawal: a historical consistent snapshot alone cannot prove current visibility at response
authorization. No locking algorithm or evidence schema is selected here.

Customer visibility remains separately evaluated from lifecycle, content/media acceptance
and command Authorization. Missing, stale, contradictory or unavailable authority cannot
produce positive visibility or a stale fallback; uncertainty cannot become fabricated absence.
Children are independently evaluated without falsely satisfying Product completeness.
Internal ID pages are not eligible public catalogue pages.

Before overall acceptance, record accountable Security dispositions and resolve the operational
choices above. Acceptance of this record must follow DECISIONS.md §§8–17 and applicable owner
review; no additional Product approval of the already confirmed P1/P2 business direction or
P4 policy is requested. Canonical synchronization and implementation verification remain separate.

## Alternatives Considered and Consequences

- Retain only conversational dispositions: avoids a document but loses durable attributable
  policy and Security boundaries; not selected for this recording task.
- Separate Product, media and authorization records: permits separate lifecycles but adds
  coordination and duplication; one combined record preserves distinct dispositions instead.
- Published lifecycle alone, caller acceptance flags or automatic reactivation: incompatible
  with approved completeness, authorization and currentness requirements; not admissible.
- Multiple simultaneous published/editing revisions or mandatory dual approval: add complexity
  or Security policy not approved here; the confirmed Draft-editing policy and pending Security
  choices remain explicit.

The selected recording approach enables bounded implementation planning without misrepresenting
Security acceptance. Draft-only edits can interrupt visibility; minimum-image withdrawal can
withhold an otherwise structurally valid Product. Rights/security withdrawal must invalidate
reliance on every affected association rather than depend on non-authoritative Search updates.

## Data, Compatibility, Migration and Operational Impact

Future evidence must preserve subject identity/revision, acceptance criteria, source/actor,
trusted time, invalidation and audit correlation. Evidence must not silently reinterpret old
Variants or confirmed Order/commercial snapshots. This record selects no database layout,
migration, retention duration, infrastructure, public cursor or event-delivery mechanism.
No existing PUBLISHED row is grandfathered into accepted publication evidence.

Product retains identity, content, media associations and publication authority. Category,
Search, Pricing and Inventory retain their existing authority; Product visibility proves
neither final purchasability nor Customer/market eligibility. CMS approval/scheduling policy
is separate; this record does not resolve DEC-0020.

## Explicit Non-Authorizations

The overall status is **Proposed**, not Accepted. This record grants no positive visibility,
publication command, production rollout, Security acceptance, dependency admission or
Architecture adoption. It changes no Product code, migration, API Contract, Approved Product
Specification, DEC-0019, canonical PRODUCT.md or ARCHITECTURE.md. Only the required decision-index
registration accompanies this record. No independent review or implementation readiness beyond
the stated policy/remaining-gate distinction is claimed.

## References

- [DECISIONS.md §§8–17](../../.ai/core/DECISIONS.md), [PRODUCT.md §§16.8–16.9, 16.13](../../.ai/core/PRODUCT.md), [ARCHITECTURE.md §35.2](../../.ai/core/ARCHITECTURE.md).
- [Product Domain](../domains/product/product-domain.md): REQ-PRD-005–020, 023–031, 037, 043, 049–050.
- [Product Backend](../backend/product/product-backend.md): BPRD-REQ-007, 011, 019–030, 037–039, 044.
- [Initial catalogue Contract §§7–13](../contracts/product/initial-product-catalogue-contract.md) and [OpenAPI](../contracts/product/initial-product-catalogue.openapi.yaml).
- [Security Standards §§12, 27](../../.ai/core/SECURITY-STANDARDS.md), [Identity Backend](../backend/identity/identity-backend.md) BIDN-REQ-004, 021, 024–026, and [Administration Domain](../domains/admin/admin-domain.md) REQ-ADM-007, 014, 023.
- [Accepted DEC-0019](DEC-0019-product-variant-attribute-standards.md), [Proposed DEC-0012](DEC-0012-administrative-role-permission-matrix.md) and [Proposed DEC-0020](DEC-0020-cms-content-approval-scheduling-policy.md).

## Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.6.0 | 2026-11-12 | Proposed | Incorporated technically grounded S1-01–S1-06 proposed dispositions using the implemented private-quarantine, technical-inspection, ClamAV, signature-freshness, immutable-observation and fail-closed readiness evidence; proposed the Product-owned checking composition, provenance boundary, exact subject/revision binding, explicit definition/version treatment, 48-hour signature-age and five-minute future-skew candidates, invalidation triggers and independent rights/security coordination for accountable review; preserved every S1 status as pending, distinguished scan-time freshness from acceptance-reuse lifetime, and retained DEC-0012, authorization, audit, publication and visibility gates. |
| 0.5.0 | 2026-11-11 | Proposed | Prepared a founder-operated MVP acceptance package aligned with DEC-0012 v0.4.0: exact-revision P1/P2 acceptance, proposed controlled media ingestion and trusted checking evidence, ordinary-risk same-actor publication, independent approval for higher-risk/unclassified Products, canonical publish/withdraw transitions, atomic audit/concurrency safeguards and fail-closed current visibility; preserved pending accountable dispositions, S1/operational gates, Proposed status and non-authoritative scope. |
| 0.4.0 | 2026-11-09 | Proposed | Prepared S1-01–S1-06 accountable disposition package and synchronized the P3/S2 dependency with DEC-0012 v0.3.0 Proposed; preserved Product approvals/provenance, pending Security and authorization confirmations, non-authoritative lifecycle and all implementation/publication restrictions. |
| 0.3.0 | 2026-11-06 | Proposed | Recorded explicit Product-owner initial operating model 1A/2B/3C/4A and additional publishing, risk-candidate, rights-evidence and withdrawal business policy; preserved P1/P2/P4, pending S1/P3 controls, Proposed/non-authoritative lifecycle and operational publication NO-GO. |
| 0.2.0 | 2026-11-06 | Proposed | Prepared consolidated S1 and P3/S2 options and recommended directions linked to DEC-0012 v0.2.0; preserved Product P1/P2/P4 approvals, pending accountable choices, Proposed/non-authoritative lifecycle and all implementation restrictions. |
| 0.1.0 | 2026-10-30 | Proposed | Recorded confirmed Product-owner P1/P2/P4 policy separately from inherited Security requirements and pending S1/P3 controls; preserved fail-closed visibility, lifecycle/history boundaries and separate acceptance, synchronization and implementation gates. |
