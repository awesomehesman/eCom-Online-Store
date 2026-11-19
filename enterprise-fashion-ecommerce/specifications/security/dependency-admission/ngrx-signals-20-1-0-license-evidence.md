---
title: NgRx Signal Store 20.1.0 License Evidence
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# NgRx Signal Store 20.1.0 License Evidence

## 1. Purpose and Scope

This Draft records reproducible licensing evidence for the exact production
dependency `@ngrx/signals@20.1.0`. It inventories the repository's current
outbound-license position, the package's declared and distributed license, the
production dependency license metadata, and the existing notice-generation behavior.

This record does not select an outbound license, establish Legal advice or approval,
approve license compatibility, admit the dependency, accept ADR-0024, promote the
Angular standard, or authorize distribution or production use. Technical evidence is
distinct from accountable repository-owner, Legal/licensing, Engineering, Security,
and Platform/DevOps dispositions.

## 2. Evidence and Admission Status

| Decision or evidence area | Current status |
| --- | --- |
| Repository outbound license | **UNRESOLVED** |
| Distribution model | **UNRESOLVED** |
| Repository-owner disposition | **PENDING** |
| Legal/licensing disposition | **PENDING** |
| Engineering disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Dependency admission | **NOT GRANTED** |
| ADR-0024 | **PROPOSED; not accepted by this record** |
| Angular Standards v1.1.0 | **DRAFT; not promoted by this record** |

The package's license identity and included text are verified as technical facts.
Their compatibility with an unknown outbound license and unresolved distribution
model is **NOT VERIFIED**.

## 3. Verification Baseline and Commands

Evidence was inspected on 2026-11-20 in the original repository workspace.

| Evidence | Observed value |
| --- | --- |
| Repository branch | `feat/fe-cat-m1-catalogue-api-integration` |
| Repository commit | `681394b0efe046d10b6bac92e8c2e2ce3b02124b` |
| Frontend package | `@ngrx/signals@20.1.0` |
| Registry artifact | `https://registry.npmjs.org/@ngrx/signals/-/signals-20.1.0.tgz` |
| Lockfile integrity | `sha512-ARAHp5yA131Sw6FEtY8XtYcdGcwW5lgpZaJoDIRxc6i12VO3ZDZYp3M/FQhpIDMDXkXHR+pDqoitrqvzI69aQA==` |
| `package-lock.json` SHA-256 | `5d3b5308c9c8b7e0570fa31b99328bd95b27c97dce1834180df433739b4caea7` |
| Repository `LICENSE` size | One byte: newline (`0a`) |
| Generated notice output | `frontend/dist/storefront/3rdpartylicenses.txt`; ignored build output |

Commands used to inspect the evidence included:

```sh
wc -c enterprise-fashion-ecommerce/LICENSE
xxd enterprise-fashion-ecommerce/LICENSE
git ls-files | grep -Ei '(^|/)(LICENSE|NOTICE|THIRD-PARTY-NOTICES|SBOM)'
sed -n '1,80p' enterprise-fashion-ecommerce/frontend/package.json
sed -n '2338,2355p' enterprise-fashion-ecommerce/frontend/package-lock.json
shasum -a 256 enterprise-fashion-ecommerce/frontend/package-lock.json
npm ls --omit=dev --all --json
tar -tzf /private/tmp/ngrx-signals-20.1.0.tgz
tar -xOf /private/tmp/ngrx-signals-20.1.0.tgz package/package.json
tar -xOf /private/tmp/ngrx-signals-20.1.0.tgz package/LICENSE
find enterprise-fashion-ecommerce/frontend/dist -type f -iname '*license*' -print
git check-ignore -v \
  enterprise-fashion-ecommerce/frontend/dist/storefront/3rdpartylicenses.txt
```

The tarball path is a local verification artifact, not governed repository evidence.
The exact artifact integrity and provenance are documented separately in the
[NgRx provenance evidence](ngrx-signals-20-1-0-provenance-evidence.md).

## 4. Repository Outbound-License Status

The tracked [`LICENSE`](../../../LICENSE) file contains only one newline byte and no
license grant, conditions, copyright statement, or warranty terms. No other tracked
repository `LICENSE`, `NOTICE`, `THIRD-PARTY-NOTICES`, SPDX software bill of materials
(SBOM), or CycloneDX SBOM was found outside installed dependencies or ignored build
output.

No inspected README, Architecture Decision Record (ADR), Decision Record, or core
standard selects an outbound repository license or establishes an external
distribution model. The frontend package's `"private": true` setting prevents normal
npm publication; it does not establish the repository's copyright license or resolve
distribution obligations.

Accordingly, the repository outbound license is **UNRESOLVED**. The empty file does
not grant public reuse permission and does not remove third-party notice obligations.
This record does not choose replacement terms or authorize a change to `LICENSE`.

## 5. Verified `@ngrx/signals@20.1.0` MIT License Evidence

The following sources consistently identify the exact package as MIT-licensed:

| Source | Verified observation |
| --- | --- |
| [`package.json`](../../../frontend/package.json) | Declares the exact direct dependency `@ngrx/signals` as `20.1.0`. |
| [`package-lock.json`](../../../frontend/package-lock.json) | Resolves `20.1.0`, the exact registry tarball and SHA-512 integrity, and declares `"license": "MIT"`. |
| Installed package metadata | `node_modules/@ngrx/signals/package.json` declares name `@ngrx/signals`, version `20.1.0`, repository `https://github.com/ngrx/platform.git`, and license `MIT`. |
| Exact registry tarball | Contains `package/package.json` and `package/LICENSE`. |
| Distributed and installed license text | Both contain the MIT License text and the same recorded copyright notice. |
| Angular-generated notice output | Identifies `@ngrx/signals` with license `MIT` and reproduces its distributed notice. |

No discrepancy was observed between the package metadata and distributed license
text. The distributed notice also states that the repository includes a
`debounceSync.ts` file copied from `rxjs-etc` by Nicholas Jamieson under MIT and
directs readers to that file's header. This nested notice must not be silently
discarded by any future governed notice process.

The distributed MIT terms require the copyright notice and permission notice to be
included in copies or substantial portions of the software. Whether and how that
condition applies to each future artifact or delivery channel requires the pending
distribution-model and Legal/licensing dispositions. An MIT dependency does not by
itself require this application or repository to adopt MIT.

## 6. Current Production Dependency License Inventory

The locked installed production graph reported these package license declarations:

| Package | Version | Declared license | Distributed license text observed locally |
| --- | --- | --- | --- |
| `@angular/common` | `20.3.35` | MIT | Yes |
| `@angular/compiler` | `20.3.35` | MIT | Yes |
| `@angular/core` | `20.3.35` | MIT | Yes |
| `@angular/platform-browser` | `20.3.35` | MIT | Yes |
| `@angular/router` | `20.3.35` | MIT | Yes |
| `@ngrx/signals` | `20.1.0` | MIT | Yes |
| `rxjs` | `7.8.2` | Apache-2.0 | Yes, `LICENSE.txt` |
| `tslib` | `2.8.1` | 0BSD | Yes, `LICENSE.txt` |
| `zone.js` | `0.15.1` | MIT | Yes |

No package in this inspected production graph had an undeclared license in the
lockfile inventory. No separate RxJS `NOTICE` file was found in the installed
package. That observation does not determine whether an additional notice is legally
required for a particular release or distribution model.

This inventory records package declarations and local files, not an approved
compatibility analysis. It excludes development-only dependencies, which remain
relevant to tooling and vulnerability governance but are not part of this production
license table.

## 7. Angular-Generated Third-Party License Notices

The current production Angular build generated:

```text
enterprise-fashion-ecommerce/frontend/dist/storefront/3rdpartylicenses.txt
```

The output includes entries for NgRx Signal Store, Angular runtime packages, RxJS,
tslib, and Zone.js. The NgRx entry reproduces the MIT notice and the nested
`rxjs-etc` attribution described in section 5. The Catalogue Signal Store imports
`@ngrx/signals`, and the generated notice is evidence that the production builder's
license extraction path includes the dependency.

The `dist/` directory is ignored by `frontend/.gitignore`. The generated file is not
tracked evidence, is not a repository-wide third-party notice, and has not been
verified as complete for every future release format. Its existence does not itself
establish a governed retention or delivery mechanism.

## 8. Missing Governed Notice Retention and SBOM Controls

The repository currently lacks verified controls that:

- retain the generated third-party license output with a production release unit;
- verify notice completeness and exact dependency-version correspondence;
- define which release artifacts or delivery channels must carry notices;
- fail CI or release processing when required notices are absent or stale;
- identify an accountable notice-generation and retention owner;
- generate and retain an SPDX, CycloneDX, or equivalent production SBOM; or
- bind notice and SBOM evidence to an immutable source and build revision.

The [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md) require an SBOM for
production release units where tooling supports it and require dependency licensing
to be considered. The current
[frontend CI workflow](../../../../.github/workflows/frontend-angular-baseline.yml)
performs locked installation, dependency advisory review, type-checking, tests,
production build, and accessibility verification. It does not retain or validate the
generated license output and does not generate an SBOM.

## 9. Unresolved Distribution-Model Assumptions

No inspected governed source establishes whether releases will be limited to
internal development or include one or more of the following:

- hosted browser assets delivered to customers;
- downloadable application artifacts;
- container images or customer-operated deployments;
- source-code distribution;
- commercial or managed-service distribution; or
- redistribution through another product or release unit.

The Angular application produces browser bundles, but implementation and build
output do not constitute an accountable distribution decision. The applicability,
placement, retention, and delivery of license notices can vary with the actual
release model. Those conclusions are **NOT VERIFIED** until the repository owner and
qualified Legal/licensing reviewer establish the relevant scope.

Internal development does not erase license conditions on later external
distribution. Conversely, this record does not claim that every internal build must
use an externally delivered notice mechanism.

## 10. Licensing Risks and Limitations

| Risk or limitation | Current assessment |
| --- | --- |
| Empty repository `LICENSE` | No operative outbound license terms were found; owner decision is required before relying on public or customer redistribution rights. |
| Unknown distribution model | Prevents a scoped determination of notice and redistribution obligations. |
| Ignored generated notices | License output can be omitted from retained release evidence or delivery unless governed controls are added. |
| No completeness gate | No automated evidence establishes that generated notices cover every shipped component. |
| No production SBOM mechanism | Required production release evidence is not implemented where supported. |
| Apache-2.0 dependency | RxJS redistribution obligations require qualified review in the selected distribution model. |
| Nested NgRx attribution | The distributed NgRx notice includes an `rxjs-etc` attribution that a future notice process must preserve where applicable. |
| License compatibility | **NOT VERIFIED** because the outbound license and distribution model are unresolved. |
| Legal interpretation | Package metadata and text are technical evidence only; this Draft provides no Legal advice or approval. |
| Dependency admission | Remains **NOT GRANTED** despite verified MIT metadata and text. |

No direct incompatibility is established by this inventory. Absence of an observed
conflict is not approval of compatibility.

## 11. Pending Repository-Owner Decisions

The accountable repository owner must determine, with appropriate review:

1. the intended outbound licensing position for repository source and released
   artifacts;
2. the supported internal and external distribution models;
3. which release units must contain or accompany third-party notices;
4. the approved notice presentation and retention mechanism;
5. ownership for maintaining, reviewing, and delivering notices and SBOMs;
6. whether generated Angular notice output is an acceptable input to that mechanism;
7. the release gate when required license or SBOM evidence is missing; and
8. whether separate treatment is required for source, browser, container, and
   customer-operated distributions.

All repository-owner dispositions remain **PENDING**. This Draft does not recommend
or select a particular outbound license.

## 12. Pending Legal/Licensing Dispositions

A qualified Legal/licensing reviewer must provide attributable dispositions on:

- compatibility between the eventual outbound terms and MIT, Apache-2.0, and 0BSD
  dependencies in the intended distribution models;
- the required retention and delivery of the NgRx MIT and nested attribution text;
- whether the generated `3rdpartylicenses.txt` content and placement are sufficient;
- any Apache-2.0 notice, license-copy, modification, or attribution requirements for
  RxJS in the relevant release units;
- whether separate obligations apply to hosted browser assets, downloadable
  artifacts, source distributions, or containers; and
- any required copyright, trademark, or other notices not established by the local
  package-license inventory.

The Legal/licensing disposition remains **PENDING**. Nothing in this record grants
Legal approval or accepts licensing Risk.

## 13. Engineering and Platform/DevOps Follow-Up

Engineering must, after the owner and Legal/licensing scope is known:

- implement or document the approved notice-generation mechanism;
- verify exact package/version coverage and preserve required nested notices;
- define reproducible tests for notice completeness;
- implement an approved production SBOM mechanism where supported;
- bind notice and SBOM evidence to source, lockfile, build, and release revisions;
- preserve the existing strict lockfile and package-integrity controls; and
- revalidate build, tests, accessibility, audit, notice, and SBOM behavior.

Platform/DevOps must confirm release-artifact retention, CI and registry controls,
evidence storage, release delivery, and failure behavior. Engineering,
Platform/DevOps, and Security dispositions remain **PENDING**. Implementation work
must not silently choose the owner or Legal policy.

## 14. Relationship to ADR-0024, Provenance and Vulnerability Triage

[ADR-0024](../../adr/ADR-0024-frontend-feature-state-management-strategy.md) remains
Proposed. It requires license compatibility as part of dependency admission but does
not itself admit `@ngrx/signals`, select an outbound license, or approve a
distribution model. [ANGULAR.md](../../../.ai/frontend/ANGULAR.md) v1.1.0 remains
Draft.

The [NgRx provenance evidence](ngrx-signals-20-1-0-provenance-evidence.md) records
artifact, registry-signature, attestation, source-workflow, and commit-relationship
evidence. Provenance does not establish license approval, so this record complements
rather than replaces it.

The [frontend development-tooling vulnerability triage](../vulnerability-triage/frontend-development-tooling-advisories.md)
records separate OPEN advisory findings and retains Security review as pending.
License evidence neither closes those findings nor accepts their Risk. Conversely,
the production-only audit result does not establish licensing compatibility.

Formal dependency admission requires the full governed evidence set and attributable
owner dispositions. No approval is inferred from implementation, successful CI,
package metadata, generated notices, branch history, or this Draft.

## 15. Reverification Triggers

Reverify this record when:

- `@ngrx/signals` or any production dependency version changes;
- package metadata, artifact integrity, or distributed license text changes;
- the lockfile or production dependency graph changes;
- a repository outbound license or distribution model is proposed or approved;
- notice-generation, retention, delivery, CI, release, or SBOM controls change;
- a new release unit or delivery channel is introduced;
- accountable owner or Legal/licensing evidence becomes available;
- an advisory, provenance finding, ownership change, or upstream notice affects the
  package; or
- ADR-0024 or the Angular Standards change lifecycle state.

Reverification must retain exact artifacts, commands, dates, limitations, and
dispositions. A later review must not silently reinterpret this Draft as approval.

## 16. Evidence References

- Repository [`LICENSE`](../../../LICENSE).
- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), especially §§25–26.
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), especially
  §§3–12 and 27.
- [Coding Standards](../../../.ai/core/CODING-STANDARDS.md), especially §36.
- [ADR-0024](../../adr/ADR-0024-frontend-feature-state-management-strategy.md).
- [Angular Standards](../../../.ai/frontend/ANGULAR.md).
- [Frontend manifest](../../../frontend/package.json).
- [Frontend lockfile](../../../frontend/package-lock.json).
- [Frontend README](../../../frontend/README.md).
- [Frontend CI workflow](../../../../.github/workflows/frontend-angular-baseline.yml).
- [NgRx provenance evidence](ngrx-signals-20-1-0-provenance-evidence.md).
- [Frontend development-tooling vulnerability triage](../vulnerability-triage/frontend-development-tooling-advisories.md).
- Installed `node_modules/@ngrx/signals/package.json` and
  `node_modules/@ngrx/signals/LICENSE`, inspected locally and not treated as tracked
  governance evidence.
- Ignored `frontend/dist/storefront/3rdpartylicenses.txt`, inspected as generated
  build evidence and not treated as retained release evidence.
- [Official npm package metadata](https://registry.npmjs.org/@ngrx/signals/20.1.0).
- Exact npm artifact:
  `https://registry.npmjs.org/@ngrx/signals/-/signals-20.1.0.tgz`.

External and generated evidence is an observation as of 2026-11-20. Any formal
review must revalidate it against the exact release candidate and approved
distribution scope.

## 17. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Recorded the exact NgRx MIT license evidence, production dependency license inventory, non-operative repository license, generated-notice behavior, missing governed notice/SBOM controls, unresolved distribution assumptions, and all pending owner dispositions without granting compatibility or dependency admission. |
