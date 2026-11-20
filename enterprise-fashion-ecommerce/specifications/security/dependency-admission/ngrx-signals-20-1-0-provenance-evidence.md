---
title: NgRx Signal Store 20.1.0 Provenance Evidence
version: 0.1.0
status: Draft
owner: Engineering
last_updated: 2026-11-20
authoritative: false
review_cycle: Event-driven
---

# NgRx Signal Store 20.1.0 Provenance Evidence

## 1. Purpose and Scope

This record preserves reproducible supply-chain provenance evidence for the exact
`@ngrx/signals@20.1.0` npm artifact. It supports future Engineering, Security and
Platform/DevOps dependency-admission review.

This record does not grant dependency admission, accept an Architecture Decision
Record (ADR), promote an implementation standard, approve a Security Exception,
accept residual Risk or establish production readiness.

## 2. Current Evidence and Admission Status

| Field | Current state |
| --- | --- |
| Evidence status | **TECHNICALLY VERIFIED WITH UNRESOLVED LIMITATION** |
| Engineering disposition | **PENDING** |
| Security disposition | **PENDING** |
| Platform/DevOps disposition | **PENDING** |
| Dependency admission | **NOT GRANTED** |
| Architecture acceptance | **NOT INFERRED** |

The unresolved limitation is the absence of authenticated evidence explaining why
the npm `gitHead` identifies the release commit while the provenance attestation
identifies its direct parent as the source revision. No matching public `20.1.0` or
`v20.1.0` tag was observed.

## 3. Reproducible Verification Baseline

Verification was performed on 2026-11-20 from
`enterprise-fashion-ecommerce/frontend` where applicable.

| Evidence | Observed value |
| --- | --- |
| Repository branch | `feat/fe-cat-m1-catalogue-api-integration` |
| Repository commit | `af8bc9284f1958db5fdaf2cc6ba5f3d6ab210513` |
| Node.js | `v20.19.4` |
| npm | `10.8.2` |
| Registry | `https://registry.npmjs.org/` |
| `package-lock.json` SHA-256 | `5d3b5308c9c8b7e0570fa31b99328bd95b27c97dce1834180df433739b4caea7` |

The following commands define the reproducible verification procedure:

```sh
npm view '@ngrx/signals@20.1.0' \
  name version dist gitHead repository maintainers \
  --json --registry=https://registry.npmjs.org/

npm audit signatures \
  --registry=https://registry.npmjs.org/

curl --fail --location \
  --output /tmp/ngrx-signals-20.1.0.tgz \
  'https://registry.npmjs.org/@ngrx/signals/-/signals-20.1.0.tgz'

openssl dgst -sha512 /tmp/ngrx-signals-20.1.0.tgz
shasum -a 256 package-lock.json

curl --fail --location \
  'https://registry.npmjs.org/-/npm/v1/attestations/@ngrx%2fsignals@20.1.0'

curl --fail --location \
  'https://api.github.com/repos/ngrx/platform/commits/44e17cbb1afe86268863ea1842740bd308f2af65'

curl --fail --location \
  'https://api.github.com/repos/ngrx/platform/commits/c775af4a19e672cff92b172c8b7ffc895e2e49db'

curl --fail --location \
  'https://api.github.com/repos/ngrx/platform/git/matching-refs/tags/20.1.0'

curl --fail --location \
  'https://api.github.com/repos/ngrx/platform/git/matching-refs/tags/v20.1.0'
```

All commands completed successfully. Empty tag-query arrays are successful queries
with no matching refs, not command failures. No network, tooling or verification
failure was converted into a passing result.

## 4. Exact Package and Artifact Identity

| Field | Verified observation |
| --- | --- |
| Package | `@ngrx/signals` |
| Version | `20.1.0` |
| Registry | `https://registry.npmjs.org/` |
| Tarball | `https://registry.npmjs.org/@ngrx/signals/-/signals-20.1.0.tgz` |
| Lockfile integrity | `sha512-ARAHp5yA131Sw6FEtY8XtYcdGcwW5lgpZaJoDIRxc6i12VO3ZDZYp3M/FQhpIDMDXkXHR+pDqoitrqvzI69aQA==` |
| Downloaded tarball SHA-512, hexadecimal | `011007a79c80d77d52c3a144b58f17b5871d19cc16e6582965a2680c847173a8b5d953b7643658a7733f1508692033035e45c747ea43aa88adaeabf323af5a40` |

The downloaded tarball SHA-512, when Base64 encoded and prefixed with `sha512-`,
exactly equals the integrity value in `package-lock.json` and the official npm
metadata. The decoded provenance subject contains the same SHA-512 digest. These
three observations bind the lockfile, downloaded registry artifact and attestation
subject to the same artifact bytes.

## 5. Registry Signature Verification

Official npm metadata contains one registry signature for the exact artifact with
key ID:

```text
SHA256:DhQ8wR5APBvFHLF/+Tc+AYvPOdTpcIDqOhxsBHRwC7U
```

`npm audit signatures --registry=https://registry.npmjs.org/` exited successfully
using npm 10.8.2 on 2026-11-20 and reported:

```text
541 packages have verified registry signatures
124 packages have verified attestations
```

Those counts cover the installed dependency graph. The command's aggregate output
does not identify `@ngrx/signals` as a separate result. Accordingly, this record does
not misrepresent 541 as a package-specific count. The target's official registry
metadata independently shows that its exact artifact carries the registry signature
and provenance-attestation references inspected here.

## 6. Provenance Attestation Verification

The official npm attestation endpoint returned both an npm publish attestation and a
SLSA provenance v1 attestation. npm's signature audit verified the installed graph's
attestations successfully.

Decoded attestation claims and matching artifact digests establish consistency with
the downloaded package, but inspection alone is not a separate package-specific
cryptographic verification. The successful npm signature audit reports aggregate
installed-graph verification; this record does not claim an independently
demonstrated package-specific verification result.

Relevant decoded SLSA claims were:

| Claim | Verified value |
| --- | --- |
| Subject | `pkg:npm/%40ngrx/signals@20.1.0` |
| Subject SHA-512 | `011007a79c80d77d52c3a144b58f17b5871d19cc16e6582965a2680c847173a8b5d953b7643658a7733f1508692033035e45c747ea43aa88adaeabf323af5a40` |
| Predicate type | `https://slsa.dev/provenance/v1` |
| Source repository | `https://github.com/ngrx/platform` |
| Source ref | `refs/heads/main` |
| Release workflow | `.github/workflows/release.yml` |
| Workflow event | `workflow_dispatch` |
| Builder | `https://github.com/actions/runner/github-hosted` |
| Attested source commit | `c775af4a19e672cff92b172c8b7ffc895e2e49db` |

The attestation subject identity and digest match the exact npm artifact. The
attestation records how the artifact was built and published; it does not establish
maintainer intent, package security, fitness, dependency admission or Architecture
approval.

## 7. Source Repository and Release Workflow

Official npm metadata identifies the package repository as
`git+https://github.com/ngrx/platform.git`. The provenance certificate and decoded
SLSA predicate identify the same `ngrx/platform` repository and
`.github/workflows/release.yml` on `refs/heads/main`.

The attestation records a public GitHub-hosted workflow-dispatch execution. This is
source and builder identity evidence for the exact artifact, not proof that every
workflow input, release decision or maintainer action was independently reviewed by
this repository.

## 8. Commit Relationship and Release-Tag Limitation

Official npm metadata reports:

```text
gitHead: 44e17cbb1afe86268863ea1842740bd308f2af65
```

The official GitHub repository reports that commit with message
`chore: release 20.1.0`. It updates the package versions, including
`modules/signals/package.json`, to 20.1.0. GitHub reports its commit-signature status
as `verified: false`, reason `unsigned`.

The same GitHub response identifies exactly one direct parent:

```text
c775af4a19e672cff92b172c8b7ffc895e2e49db
```

That parent exactly matches the source commit in the provenance attestation. The
direct-parent relationship is therefore verified through the official GitHub commit
response; it is not inferred from similar timestamps or messages.

The official GitHub matching-ref queries for `tags/20.1.0` and `tags/v20.1.0` each
returned an empty array. This establishes that those exact public refs were not
returned on 2026-11-20; it does not prove that no differently named or subsequently
created release reference exists.

The precise reason why npm `gitHead` records the release commit while the attestation
records its direct parent is **NOT VERIFIED**. The relationship is consistent with a
release-sequencing explanation, but no authenticated maintainer explanation was
obtained and none is invented here.

## 9. Evidence Interpretation and Limitations

- The npm signature command reports aggregate graph counts rather than a
  package-specific result.
- The exact package's registry metadata carries a signature and attestation
  reference whose artifact identity and digest were inspected.
- The release commit exists in the official repository but is observed as unsigned.
- The `gitHead` and attested commits differ, although their direct-parent
  relationship is verified.
- No exact public `20.1.0` or `v20.1.0` tag was confirmed.
- No authenticated maintainer explanation resolves the differing commit references.
- Artifact integrity and provenance do not establish vulnerability absence,
  suitability, maintenance quality, license approval or safe application behavior.
- Technical verification does not constitute Engineering, Security or
  Platform/DevOps approval.
- No formal dependency admission exists.

## 10. Relationship to Vulnerability Triage

The separate
[frontend development-tooling vulnerability triage](../vulnerability-triage/frontend-development-tooling-advisories.md)
records two OPEN moderate advisories in the development-tooling graph. No Risk
acceptance has been granted and Security review remains pending.

Those findings are not reported against the production `@ngrx/signals` package
itself, but they remain part of the complete frontend dependency-admission context.
This record does not duplicate their assessment, close them, establish
non-applicability or approve a Security Exception.

## 11. Required Accountable-Owner Dispositions

| Owner | Required disposition | Current state |
| --- | --- | --- |
| Engineering | Confirm capability justification, compatibility, maintenance, exact graph, reproducibility and whether the provenance limitation is acceptable for admission review. | **PENDING** |
| Security | Assess publisher and registry provenance, signature and attestation evidence, commit discrepancy, vulnerabilities and supply-chain Risk. | **PENDING** |
| Platform/DevOps | Confirm approved registry use, CI verification, evidence retention and build-environment controls. | **PENDING** |

Architecture must decide ADR-0024 independently. No owner acceptance is inferred
from technical verification, CI, npm metadata, implementation, branch history or
this Draft record.

## 12. Reverification Triggers

Reverification is required when:

- the NgRx version changes;
- registry artifact bytes, integrity, signatures or attestations change;
- signature or attestation verification behavior changes;
- source repository, release workflow, source commit or release metadata changes;
- an authenticated maintainer explanation becomes available;
- new security advisories affect the dependency or relevant graph;
- the dependency graph changes; or
- release, deployment or dependency-admission gates change.

## 13. Admission and Closure Boundaries

This evidence record may be considered technically complete only while its commands
remain reproducible and its limitations remain accurate. Document completion or
approval does not itself admit the dependency.

Dependency admission requires attributable Engineering, Security and Platform/DevOps
dispositions plus the other evidence required by repository governance, including
compatibility, licensing, vulnerability, maintenance, graph, bundle and CI evidence.
Any unresolved mandatory Security waiver would require the separately governed
Security Exception process; no exception is requested or approved here.

[ADR-0024](../../adr/ADR-0024-frontend-feature-state-management-strategy.md) remains
Proposed. [ANGULAR.md](../../../.ai/frontend/ANGULAR.md) v1.1.0 remains Draft. This
record neither accepts the ADR nor promotes that standard. It does not authorize
implementation merge, operational reliance, deployment or production use.

## 14. Evidence References

- [Security Standards](../../../.ai/core/SECURITY-STANDARDS.md), especially §§24–26
  and 38–43.
- [Documentation Standards](../../../.ai/core/DOCUMENTATION-STANDARDS.md), especially
  §§7–12, 27 and 44.
- [Coding Standards](../../../.ai/core/CODING-STANDARDS.md), especially §36.
- [ADR-0024](../../adr/ADR-0024-frontend-feature-state-management-strategy.md).
- [Angular Standards](../../../.ai/frontend/ANGULAR.md).
- [Frontend manifest](../../../frontend/package.json).
- [Frontend lockfile](../../../frontend/package-lock.json).
- [Frontend README](../../../frontend/README.md).
- [Frontend CI workflow](../../../../.github/workflows/frontend-angular-baseline.yml).
- [Frontend development-tooling vulnerability triage](../vulnerability-triage/frontend-development-tooling-advisories.md).
- [Official npm package metadata](https://registry.npmjs.org/@ngrx/signals/20.1.0).
- [Official npm attestation endpoint](https://registry.npmjs.org/-/npm/v1/attestations/@ngrx%2fsignals@20.1.0).
- [npm `gitHead` release commit](https://github.com/ngrx/platform/commit/44e17cbb1afe86268863ea1842740bd308f2af65).
- [Attested source commit](https://github.com/ngrx/platform/commit/c775af4a19e672cff92b172c8b7ffc895e2e49db).

External evidence is an observation as of 2026-11-20 and must be revalidated when a
trigger in section 12 occurs.

## 15. Revision History

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-20 | Draft | Recorded reproducible registry-signature, provenance-attestation, artifact-digest, source-workflow and commit-relationship evidence for `@ngrx/signals@20.1.0`, retaining the unresolved commit/tag limitation and all dependency-admission dispositions as pending. |
