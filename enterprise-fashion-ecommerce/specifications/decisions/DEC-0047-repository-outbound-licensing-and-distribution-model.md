# DEC-0047 — Repository Outbound Licensing and Distribution Model

- **Identifier:** DEC-0047
- **Title:** Repository Outbound Licensing and Distribution Model
- **Type:** Engineering Practice Decision / Operational Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-11-20
- **Last Updated:** 2026-11-20
- **Owner:** Repository Owner — identity and licensing authority pending verification
- **Review Cycle:** Event-driven
- **Required Accountable Reviews:** Repository Owner / verified rights holder, Product/business, qualified Legal/licensing, Architecture, Engineering, Security, Platform/DevOps
- **Authoritative:** false
- **Supersedes:** N/A
- **Superseded By:** N/A

## 1. Purpose and Decision Context

The repository's tracked [`LICENSE`](../../LICENSE) contains one newline byte and no
operative terms. No Accepted Decision Record selects an outbound license or defines
which source, browser, package, container, binary, customer-operated, software-resale,
or software-as-a-service distribution models are permitted. The repository therefore
lacks a reviewable basis for granting outbound rights or determining how third-party
license obligations apply to each release unit.

The Approved [Product baseline](../../.ai/core/PRODUCT.md) describes a commercial
online retail platform for a fashion and streetwear business. The Approved
[Architecture baseline](../../.ai/core/ARCHITECTURE.md) describes separately
deployable Angular and Spring Boot applications, a domain-driven Modular Monolith,
and production operation. It excludes multi-tenant software-as-a-service operation
from Version 1. These facts support review of an operator-hosted commercial model;
they do not select a copyright license or authorize software redistribution.

This record prepares one bounded decision for accountable review. It is not a
license, legal advice, rights grant, distribution authorization, dependency admission,
release approval, or Architecture acceptance. Every disposition and acceptance gate
below remains pending.

## 2. Decision Question and Current Lifecycle

**Decision question:** What outbound licensing position and software distribution
model should govern the enterprise fashion eCommerce repository and its release
units?

**RECOMMENDED CANDIDATE — NOT APPROVED:**

> Privately maintained, proprietary-source, commercially operated, single-merchant
> hosted fashion eCommerce platform. The operator controls the frontend, backend and
> infrastructure. Customers access the hosted retail service and receive browser
> assets. Public source redistribution, software resale, SDK distribution,
> multi-tenant SaaS and customer-operated deployment are excluded unless separately
> approved.

This candidate records a direction for comparison only. The repository has not
adopted proprietary licensing, no final proprietary license text is drafted here,
and no outbound rights are granted. Proposed status is non-authoritative and cannot
be treated as an enforceable license or distribution authorization.

## 3. Governing Evidence and Established Constraints

| Source | Current authority | Evidence or constraint | What remains unresolved |
| --- | --- | --- | --- |
| [Decision governance](../../.ai/core/DECISIONS.md) | Approved; authoritative | Requires one accountable owner, realistic alternatives, consequences, proportional review, durable approval and index synchronization. | The decision and owner authority are not accepted. |
| [Product baseline](../../.ai/core/PRODUCT.md) | Approved; authoritative | Establishes a customer-facing commercial retail platform, one coherent store operation, and authoritative business capabilities. | It does not select outbound source terms or software-distribution channels. |
| [Architecture baseline](../../.ai/core/ARCHITECTURE.md) | Approved; authoritative | Establishes operator-controlled frontend/backend deployments and excludes multi-tenant SaaS from Version 1. | It does not decide copyright licensing, customer-operated deployment rights, or redistribution terms. |
| [Security Standards](../../.ai/core/SECURITY-STANDARDS.md) | Approved; authoritative | Requires reviewed dependency licensing, provenance, locked inventories, production-release SBOMs where supported, and retained supply-chain evidence. | Notice, SBOM and release-failure mechanisms remain unimplemented. |
| [Documentation Standards](../../.ai/core/DOCUMENTATION-STANDARDS.md) | Approved; authoritative | Requires truthful lifecycle, ownership, traceability and explicit uncertainty. | No approval may be inferred from this Proposed record. |
| [NgRx license evidence](../security/dependency-admission/ngrx-signals-20-1-0-license-evidence.md) | Draft; non-authoritative evidence | Verifies exact package-license facts, the one-byte repository `LICENSE`, production license inventory and missing notice/SBOM controls. | Outbound license, distribution model, compatibility and all owner dispositions remain unresolved. |
| [NgRx provenance evidence](../security/dependency-admission/ngrx-signals-20-1-0-provenance-evidence.md) | Draft; non-authoritative evidence | Records artifact and provenance observations for the exact NgRx package. | Provenance does not establish licensing approval or admission. |
| [Frontend vulnerability triage](../security/vulnerability-triage/frontend-development-tooling-advisories.md) | Draft; non-authoritative evidence | Records OPEN development-tooling findings. | Security disposition and Risk treatment remain pending. |
| [ADR-0024](../adr/ADR-0024-frontend-feature-state-management-strategy.md) | Proposed; non-authoritative | Proposes a feature-state standard and requires separate dependency admission. | Architecture acceptance and dependency admission remain pending. |

Applicable law, regulatory duties, contracts and security requirements retain their
higher authority. DEC-0047 cannot override them.

## 4. Current Repository License State

The current `enterprise-fashion-ecommerce/LICENSE` file is one byte containing only a
newline. It contains no license grant, restrictions, copyright statement, warranty
terms, notice, or identified licensor. It is therefore not evidence of an approved
outbound licensing position.

No inspected repository README, ADR, Decision Record or core standard supplies a
replacement outbound license or confirms the legal entity authorized to grant rights.
The frontend manifest's `"private": true` property prevents ordinary npm publication;
it does not create copyright terms, identify a rights holder, or resolve distribution
obligations.

The current `LICENSE` must not be removed, populated or replaced under this Proposed
record. Exact replacement text requires later authorization after DEC acceptance and
qualified Legal/licensing review.

## 5. Candidate Commercial Operating Model

The recommended candidate assumes:

- one commercial fashion retail operation;
- one operator controlling source, infrastructure, frontend and backend deployments;
- a hosted customer storefront and protected operational administration;
- browser assets delivered to customers as part of the hosted service;
- no public source grant or general software redistribution right;
- no customer-operated or white-label deployment in the initial model; and
- no multi-tenant SaaS, SDK, library product or software-resale offering.

The Product and Architecture evidence supports review of this operating model but
does not approve it as a licensing or distribution decision. Product/business,
Repository Owner, qualified Legal/licensing and Architecture confirmation remain
**PENDING**.

## 6. Alternatives Considered

| Alternative | Intended use | Benefits | Constraints | Distribution and notice implications | Required review |
| --- | --- | --- | --- | --- | --- |
| A. No outbound license grant / internal-only source access | Restrict source use to authorized internal participants while making no external software grant. | Minimizes deliberate source redistribution and preserves future choice. | Does not define customer browser delivery, contractor rights, hosted-service terms or release evidence; the empty `LICENSE` remains unclear. | Third-party terms still apply to distributed browser or other artifacts; internal access controls and contribution rights still require definition. | Rights holder, Product/business, Legal/licensing, Engineering, Security and Platform/DevOps. |
| B. Proprietary, operator-hosted commercial platform | Operate one hosted retail platform while retaining proprietary application source. | Aligns with the current single-merchant, operator-controlled candidate and limits unsupported redistribution channels. | Requires verified ownership, exact proprietary terms, contracts, notice delivery and operational release controls. | Browser assets are delivered externally; third-party notices and SBOM evidence remain necessary where applicable. | All accountable reviewers. This is the **RECOMMENDED CANDIDATE — NOT APPROVED**. |
| C. Source-available licensing with restricted redistribution | Permit defined source inspection or limited use without general open-source rights. | May support transparency, evaluation or controlled collaboration. | Exact restrictions, enforceability, contribution rights and ecosystem compatibility require qualified drafting. | Distribution scope and third-party notices must be explicit; source availability does not replace dependency terms. | Rights holder, Product/business, Legal/licensing, Architecture, Engineering, Security and Platform/DevOps. |
| D. Open-source licensing | Grant public rights under a selected approved open-source license. | May enable community use, contribution and broad reuse. | Requires verified rights, license selection, contribution governance, compatibility review and an intentional commercial strategy. | Source and release notices, dependency compatibility, attribution and possible reciprocal conditions depend on the selected license and distribution. | Rights holder, Product/business, qualified Legal/licensing, Architecture, Engineering, Security and Platform/DevOps. |
| E. Customer-operated or redistributed software | Deliver source, package, binary or container artifacts for customer operation, resale or white-label use. | Could support customer-controlled hosting or a separate software product. | Materially expands support, security, update, contract, deployment, incident, data and lifecycle responsibilities. | Requires exact artifact inventory, notices, SBOMs, delivery terms, update obligations and potentially a separate Architecture decision. | Separate Product/business, Legal/licensing, Architecture, Security, Engineering and Platform/DevOps approval. |

No legal winner is selected. Acceptance must fairly consider the alternatives against
verified ownership, Product intent, distribution requirements, operational cost,
security, reversibility and third-party obligations.

## 7. Decision Criteria

An accountable selection must evaluate:

- verified copyright ownership and authority to grant rights;
- alignment with the Approved Product and Architecture baselines;
- commercial operating and customer-contract requirements;
- supported release units and distribution channels;
- third-party license compatibility and notice obligations;
- contribution, employee and contractor rights;
- security, supply-chain, privacy, regulatory and contractual impact;
- notice and SBOM completeness, retention and delivery;
- operational support, update, vulnerability and incident responsibilities;
- enforceability, clarity, reversibility and migration cost; and
- future change and separately governed expansion paths.

Successful builds, package metadata, private repository settings or current
implementation do not substitute for these criteria.

## 8. Proposed Distribution Boundaries

| Distribution context | Evidence-supported current state | Proposed treatment | Current authorization |
| --- | --- | --- | --- |
| Internal development and testing | Repository and CI workflows support development and verification. | Permit only within existing access, confidentiality and third-party obligations pending final terms. | Existing bounded activity only; no new outbound grant. |
| Hosted browser-asset delivery | The Angular application builds customer-facing browser assets. | Candidate operator-hosted model includes delivery through the hosted retail service, subject to notice and release controls. | **NOT AUTHORIZED BY THIS PROPOSED DECISION** |
| Source distribution | No approved source-distribution model was found. | Excluded from the candidate unless separately approved. | **NOT AUTHORIZED** |
| Downloadable application packages | No approved customer-download model was found. | Excluded unless separately approved. | **NOT AUTHORIZED** |
| Container or binary distribution | Production deployment is anticipated, but customer redistribution is not approved. | Operator-internal deployment may be evaluated separately; external delivery is excluded unless approved. | External distribution **NOT AUTHORIZED** |
| Customer-operated deployment | Version 1 evidence does not establish it. | Excluded from the candidate. | **NOT AUTHORIZED** |
| SDK or library redistribution | No Product or Architecture evidence establishes an SDK/library product. | Excluded from the candidate. | **NOT AUTHORIZED** |
| Multi-tenant SaaS operation | Explicitly outside Version 1 Architecture. | Excluded; requires separate Product and Architecture governance. | **NOT AUTHORIZED** |
| Software resale or white-label distribution | No approved business model was found. | Excluded from the candidate. | **NOT AUTHORIZED** |

This table records proposed boundaries, not enforceable permission. An Accepted final
decision must state exact channels and release units rather than relying on the word
“private” or “hosted.”

## 9. Rights-Holder and Licensing-Authority Verification

The actual copyright-rights holder or authorized licensing entity is **NOT VERIFIED**.
Repository ownership, administrative control, authorship and copyright ownership are
not interchangeable.

Before acceptance, attributable evidence must verify:

- repository ownership versus copyright ownership;
- the legal person or entity authorized to grant outbound rights;
- employee and contractor contribution ownership or assignments;
- third-party contribution rights and applicable inbound terms;
- pre-existing code, assets, generated material and other incorporated works;
- existing customer, supplier, employment or other contractual restrictions; and
- authority to approve the exact outbound terms and distribution channels.

No legal entity, copyright holder, contracting party or licensor is invented by this
record. If authority cannot be established, the decision must remain Proposed and no
replacement `LICENSE` may be installed.

## 10. Third-Party Dependency Licensing Implications

The committed NgRx licensing evidence records the following exact production
dependency declarations relevant to this decision:

- `@ngrx/signals@20.1.0` — MIT;
- `rxjs@7.8.2` — Apache-2.0; and
- `tslib@2.8.1` — 0BSD.

It also inventories the remaining production Angular and Zone.js dependencies as
MIT. Package metadata and distributed license text are technical evidence, not
compatibility approval.

An MIT-licensed dependency does not require the application itself to use MIT.
Conversely, a proprietary application license does not replace, remove or weaken
third-party copyright, license, notice or attribution obligations. Qualified
Legal/licensing review must determine compatibility and requirements for each
approved release unit and distribution channel. No compatibility is approved here.

## 11. Notice Generation, Retention and Delivery

The Angular production build currently generates ignored output at:

```text
frontend/dist/storefront/3rdpartylicenses.txt
```

The observed file includes NgRx, Angular, RxJS, tslib and Zone.js notices. This is
implementation evidence only. No governed control currently establishes complete
coverage, retention with release artifacts, delivery to recipients, immutable
source/build binding, or failure when the notice is absent or stale.

Before a production release under an accepted model, accountable owners must approve
and implement:

- the notice source and generation mechanism;
- exact dependency and nested-attribution completeness validation;
- required placement for every release unit and channel;
- retention with immutable release evidence;
- binding to source revision, lockfile, build and artifact digests;
- review ownership and update triggers; and
- fail-closed release behavior when required notice evidence is missing, invalid,
  incomplete or stale.

This record implements none of those controls.

## 12. Production SBOM Requirements

The Security Standards require a software bill of materials (SBOM) for production
release units where tooling supports it and require retention with release evidence.
No governed frontend or repository production-SBOM mechanism was found.

The approved plan must define:

- supported format and version, such as an approved SPDX or CycloneDX profile;
- package, version, license, relationship and artifact coverage;
- generation from the exact locked and built release candidate;
- source, lockfile, builder and release-artifact binding;
- integrity, retention, access and delivery controls;
- verification and completeness checks;
- accountable maintenance and review ownership; and
- release failure when required SBOM evidence is unavailable or invalid.

No format or implementation product is selected here.

## 13. Engineering and Platform/DevOps Release Controls

Engineering must inventory all release units and bundled third-party material, define
reproducible notice and SBOM validation, and ensure generated evidence matches the
exact dependency graph and artifact. Platform/DevOps must govern build isolation,
artifact retention, integrity, promotion, distribution, evidence storage and
fail-closed release behavior.

The release process must preserve at least:

- source revision and reviewed lockfile identity;
- immutable build and release-artifact identity;
- dependency, license, notice, vulnerability and SBOM evidence;
- the approved distribution channel and recipient class;
- accountable approval evidence; and
- visible failure or prohibition when mandatory evidence is absent.

Exact mechanisms, owners and acceptance evidence remain **PENDING**. No production
release approval is inferred.

## 14. Security Supply-Chain Requirements

Security review must assess dependency origin, publisher and artifact provenance,
license and notice evidence, vulnerabilities, release integrity, evidence retention,
unauthorized distribution Risk and incident/revocation behavior. Existing mandatory
lockfile, registry, provenance, scanning and least-privilege controls remain in force.

The [frontend vulnerability triage](../security/vulnerability-triage/frontend-development-tooling-advisories.md)
remains OPEN with Risk acceptance **NOT GRANTED**. This licensing proposal neither
closes those findings nor creates a Security Exception. Security disposition for
DEC-0047 remains **PENDING**.

## 15. Architecture Compatibility and ADR Triggers

The recommended candidate appears compatible with the current operator-controlled,
single-merchant, separately deployable frontend/backend Architecture. This is a
review recommendation, not Architecture acceptance.

An additional ADR and synchronized Architecture update are required before this
decision can authorize a material Architecture change such as:

- customer-operated or on-premises deployment;
- multi-tenant SaaS operation;
- white-label or independently operated deployments;
- a new SDK or library product boundary;
- materially different release/deployment units; or
- distribution-driven ownership, security, data or operational boundaries.

Architecture disposition remains **PENDING**. The Architecture use of “distribution”
for service decomposition must not be misrepresented as a copyright or redistribution
decision.

## 16. Required Accountable-Owner Dispositions

| Reviewer or authority | Required disposition | Current status |
| --- | --- | --- |
| Repository Owner / verified rights holder | Confirm identity, licensing authority, selected outbound position, permitted channels and final decision. | **PENDING** |
| Product/business | Confirm commercial operating model, customer/channel intent and exclusions. | **PENDING** |
| Qualified Legal/licensing | Confirm rights-holder authority, exact terms, compatibility, notices, delivery obligations, contracts, jurisdiction and final `LICENSE` migration. | **PENDING** |
| Architecture | Confirm compatibility with the canonical deployment model and identify any required ADR. | **PENDING** |
| Engineering | Confirm release-unit inventory, dependency/license evidence and implementability of notice/SBOM controls. | **PENDING** |
| Security | Confirm supply-chain, vulnerability, evidence, unauthorized-distribution and release-gate controls. | **PENDING** |
| Platform/DevOps | Confirm artifact, notice, SBOM, retention, delivery and fail-closed release operations. | **PENDING** |

Review or merge of this Proposed record is not a disposition. Each acceptance must be
explicit, attributable, scoped and durably recorded.

## 17. Acceptance Gates

| Gate | Required evidence | Current state |
| --- | --- | --- |
| 1. Verified rights holder | Evidence identifying the copyright-rights holder and authorized licensing authority. | **INCOMPLETE** |
| 2. Confirmed commercial operating model | Attributable Product/business and Repository Owner confirmation. | **INCOMPLETE** |
| 3. Qualified Legal disposition | Review of exact terms, rights, compatibility, notices, contracts and channels. | **INCOMPLETE** |
| 4. Architecture compatibility | Attributable confirmation and any required ADR plan. | **INCOMPLETE** |
| 5. Engineering artifact inventory | Complete release-unit, bundle and third-party-material inventory. | **INCOMPLETE** |
| 6. Security review | Supply-chain, vulnerability, evidence and release-control disposition. | **INCOMPLETE** |
| 7. Platform/DevOps notice and SBOM plan | Reviewable generation, verification, retention, delivery and failure design. | **INCOMPLETE** |
| 8. Reviewed `LICENSE` migration | Exact proposed text, provenance, path, transition and validation plan. | **INCOMPLETE** |
| 9. Explicit final decision | Selected alternative and precise boundaries recorded in a reviewed DEC revision. | **INCOMPLETE** |
| 10. Recorded accountable approvals | Durable, attributable approval evidence for every required authority. | **INCOMPLETE** |

No gate is complete merely because this proposal, evidence record, implementation or
CI result exists. Acceptance is **NO-GO** until every mandatory gate is satisfied.

## 18. Rejected Assumptions and Prohibited Inferences

The following inferences are prohibited:

- repository control proves copyright ownership or licensing authority;
- the one-byte `LICENSE` grants public, private, proprietary or open-source rights;
- a private repository establishes an outbound license;
- commercial operation implies proprietary licensing has been adopted;
- hosted operation eliminates distribution or notice obligations;
- browser assets are outside third-party licensing obligations;
- an MIT dependency requires the application to be MIT-licensed;
- proprietary application terms replace third-party licenses or notices;
- package metadata alone establishes Legal compatibility;
- generated `3rdpartylicenses.txt` is a complete governed notice mechanism;
- successful CI establishes notice, SBOM, Legal or release approval;
- DEC-0047 acceptance alone admits any dependency;
- implementation, merge, founder direction or repository-owner instruction replaces
  independent required Legal, Security, Engineering or Architecture review; or
- Proposed status authorizes source, binary, container, SDK, customer-operated,
  white-label, resale or multi-tenant distribution.

## 19. Implementation Dependencies and Migration Plan

No license, notice, SBOM, CI, release or application implementation is authorized by
this version.

If a later revision is Accepted, implementation must proceed through separately
reviewed changes in this order:

1. preserve the Accepted decision and attributable approval evidence;
2. prepare exact, qualified Legal-reviewed replacement text for `LICENSE`;
3. authorize a bounded `LICENSE` migration without rewriting decision history;
4. implement and verify governed notice generation, completeness, retention and
   delivery;
5. implement and verify the approved production SBOM mechanism;
6. bind source, lockfile, notices, SBOM and immutable release artifacts;
7. implement fail-closed release gates and operational evidence retention; and
8. validate every approved distribution channel before production use.

The current `LICENSE` remains unchanged until step 3 is explicitly authorized. A
later Accepted DEC defines direction only; it does not prove implementation or
release readiness.

## 20. Dependency, Architecture and Release Boundaries

DEC-0047 acceptance alone would not admit `@ngrx/signals@20.1.0`. NgRx dependency
admission remains **NOT GRANTED** and still requires its separate Engineering,
Security and Platform/DevOps evidence and dispositions.

[ADR-0024](../adr/ADR-0024-frontend-feature-state-management-strategy.md) remains
Proposed. [ANGULAR.md](../../.ai/frontend/ANGULAR.md) v1.1.0 remains Draft. The
frontend vulnerability triage remains OPEN. No Architecture acceptance, Angular
standard promotion, Security Risk acceptance, implementation merge readiness,
deployment authorization or production-release approval is inferred from DEC-0047.

## 21. Reverification and Review Triggers

Re-review this decision when:

- verified rights-holder or licensing-authority evidence changes;
- contribution, employment, contractor or customer contractual evidence changes;
- the commercial operating model or supported market/channel changes;
- an outbound license alternative or exact text is proposed;
- a new distribution channel or release unit is considered;
- customer-operated, white-label, resale, SDK or multi-tenant operation is proposed;
- Product or Architecture authority changes;
- a production dependency or its license changes;
- notice, SBOM, CI, release or artifact-retention controls change;
- material legal, regulatory, contractual or security obligations change; or
- an incident identifies unauthorized distribution or missing licensing evidence.

Review must use current evidence and preserve history. A trigger permits governance
review only; it does not authorize a change.

## 22. Consequences

### 22.1 If the Recommended Candidate Is Later Accepted

Potential benefits include a bounded initial operating model, clearer source and
distribution expectations, alignment with the current single-merchant hosted
Architecture, and an explicit path for notices and SBOM evidence.

Costs and limitations include restricted redistribution, continuing Legal and release
compliance work, controlled contributor/customer access, explicit third-party notice
delivery, and separate governance for new software-delivery models. Proprietary terms
would not eliminate dependency obligations or vulnerability response.

### 22.2 Consequences of Remaining Proposed

No outbound license or distribution authorization exists. The empty `LICENSE` remains
non-operative, compatibility remains unapproved, production notice/SBOM controls
remain missing, and formal dependency admission remains blocked. This fail-closed
state avoids inventing rights but is not a complete release posture.

## 23. Security, Data, Compatibility and Operational Impact

**Security impact:** Supply-chain, artifact-integrity, vulnerability, notice, SBOM and
unauthorized-distribution controls require review and implementation. No Security
Exception or Risk acceptance is created.

**Data impact:** This proposal changes no application data ownership, schema,
retention or processing. Customer-operated or multi-tenant alternatives could create
material future data/privacy impacts and therefore require separate governance.

**Compatibility and migration impact:** No application or Contract compatibility
change occurs while Proposed. A future `LICENSE`, notice or release-process migration
must preserve third-party obligations and exact release evidence.

**Operational impact:** The candidate assumes operator-controlled deployment. Release
units, notice/SBOM retention, evidence delivery, monitoring and support responsibilities
remain incomplete and cannot be represented as production-ready.

## 24. References

- Repository [`LICENSE`](../../LICENSE).
- [Repository constitution](../../.ai/core/AGENTS.md).
- [Decision governance](../../.ai/core/DECISIONS.md).
- [Product baseline](../../.ai/core/PRODUCT.md).
- [Architecture baseline](../../.ai/core/ARCHITECTURE.md).
- [Security Standards](../../.ai/core/SECURITY-STANDARDS.md).
- [Documentation Standards](../../.ai/core/DOCUMENTATION-STANDARDS.md).
- [Coding Standards](../../.ai/core/CODING-STANDARDS.md).
- [ADR-0024](../adr/ADR-0024-frontend-feature-state-management-strategy.md).
- [Angular Standards](../../.ai/frontend/ANGULAR.md).
- [NgRx license evidence](../security/dependency-admission/ngrx-signals-20-1-0-license-evidence.md).
- [NgRx provenance evidence](../security/dependency-admission/ngrx-signals-20-1-0-provenance-evidence.md).
- [Frontend development-tooling vulnerability triage](../security/vulnerability-triage/frontend-development-tooling-advisories.md).
- [Frontend manifest](../../frontend/package.json).
- [Frontend lockfile](../../frontend/package-lock.json).
- [Frontend CI workflow](../../../.github/workflows/frontend-angular-baseline.yml).

## 25. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Proposed | Created a reviewable outbound-licensing and distribution-model proposal; recorded the privately maintained proprietary-source operator-hosted model as an unapproved candidate, evaluated alternatives, preserved unresolved rights-holder and Legal authority, and kept all distribution, dependency, Architecture, implementation and release gates pending. |
