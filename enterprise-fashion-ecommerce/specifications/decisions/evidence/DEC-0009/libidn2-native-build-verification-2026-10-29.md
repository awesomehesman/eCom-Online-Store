# DEC-0009 — Libidn2 2.3.8 native build-verification evidence

## 1. Document metadata

- Date: 2026-10-29 (Africa/Johannesburg).
- Owner: Identity.
- Related decision: [DEC-0009](../../DEC-0009-customer-login-idna2008-implementation-strategy.md), Proposed.
- Evidence status: Completed.
- Verification outcome: Qualified Technical Pass.
- Scope: Native build and dynamic-linker compatibility only.
- Authority: Informational evidence; no implementation or candidate-selection authority.

This record transcribes the completed exercise results supplied by the repository owner in the request to create this document. The external logs and preserved binary were not independently inspected or re-executed during preparation of this record. Observations below refer to that reported exercise; no additional test, timestamp, reviewer approval or artifact verification is asserted.

## 2. Purpose and authorization

The explicitly authorized activity was bounded GNU Libidn2 2.3.8 native build verification, isolated from the repository. Its purpose was to establish whether the release could be built for the Linux AMD64 environment and whether the resulting shared object's dynamic dependencies and relocations resolved in the identified runtime.

The [existing bounded authorization record](strict-idna-evaluation-authorization-proposal-2026-10-26.md) provides the related environment-verification context. This evidence does not extend that authority, reopen exhausted evaluations or assert a new approval. Environment verification is distinct from JNI implementation, functional Punycode testing and strict IDNA2008 candidate evaluation. The latter activities remain unauthorized for this exercise.

The existing cumulative five-engineering-day / 40-aggregate-person-hour ceiling remains applicable and is not reset by recording these findings. Total consumed effort has not been reconciled; no remaining allowance is claimed.

## 3. Source provenance

The reported source was the GNU Libidn2 **2.3.8** release archive.

| Check | Reported observation | Qualification |
| --- | --- | --- |
| Archive SHA-256 | `f557911bf6171621e1f72ff35f5b1825bb35b52ed45325dcdee931e5d3c0787a` | Matched the GNU release announcement. A matching published checksum alone does not establish publisher authenticity. |
| Detached GNU signature | Verification reported a good signature. | The signing key was expired at verification time. Historical signing-time validity was not independently established. |

The source was reported as authenticated release source, subject to that signing-key qualification. The earlier GPG verification identified primary key fingerprint `B1D2BD1375BECB784CF4F8C4D73CF638C53C06BE` and signing subkey fingerprint `A3CC9C870B9D310ABAD4CF2F51722B08FE4745A2`. These fingerprints are reported from the verification output; independent historical signing-time key validity remains unestablished. It does not convert the qualified signature result into unqualified provenance clearance.

## 4. Build environment

| Item | Reported environment |
| --- | --- |
| Host | macOS ARM64 with Docker Desktop |
| Target | Ubuntu 24.04, Linux AMD64 |
| Build-image reference | `ubuntu@sha256:78d4744ead1f5b19b04d0df558ab182a5300903900755e515b2ca4d10eb2044d` |
| Compiler | GCC 13.3.0 |
| Build utility | GNU Make 4.3 |
| Binary utilities | GNU Binutils 2.42 |
| Development C library package | `libc6-dev 2.39-0ubuntu8.9` |
| Execution mode | AMD64 emulation on the ARM64 host |
| Isolation | Build performed outside the repository |

The supplied build-image reference is retained exactly; its manifest/index type is not independently established here. The individually reported tool versions do not constitute a complete build-time package manifest. APT repositories were mutable, so the image reference and observed versions do not establish a fully reproducible build environment.

## 5. Build configuration

The exercise used the release source's generated `configure` script, rather than treating a development-source regeneration as part of the verified build. Reported configuration and invocation included:

```text
--with-included-libunistring
--disable-static
make -j2
```

The build completed successfully and modified no repository files. These are the supplied configuration details, not a reconstructed complete configure command or build transcript. The included-libunistring arrangement does not mean that the library contains only Punycode code or that higher-level IDNA code was removed.

## 6. Binary verification

The resulting binary was reported as an **ELF64 x86-64 shared object**. Inspection identified the following public exported symbols:

```text
idn2_punycode_encode@@IDN2_2.3.5
idn2_punycode_decode@@IDN2_2.3.5
```

`IDN2_2.3.5` is an ABI symbol version. It is not the compiled library's release version; the verified source release was Libidn2 2.3.8. Export presence establishes neither functional correctness nor successful invocation of either API.

The reported direct dynamic dependency was `libc.so.6`. No direct external libunistring dependency was observed. This is a direct-linkage observation, not a complete inventory of all build inputs, bundled code, runtime platform components or licensing obligations. Higher-level IDNA code was not claimed to have been removed.

## 7. Runtime compatibility

| Item | Reported observation |
| --- | --- |
| Runtime image local ID | `sha256:8b3b108b98b06013f1fb7105745b4ab128f6bdce7759206d3fa8ef9fc58e4d90` |
| Runtime architecture | Linux AMD64 |
| Java distribution/version | Temurin Java 21.0.12.1+1 |
| Dynamic dependencies | Resolved in the inspected runtime |
| Relocation inspection | `ldd -r` reported no unresolved relocations |
| Highest observed GLIBC requirement | `GLIBC_2.14` |

The runtime identifier above is a **local image ID**, not a registry manifest digest. It must not be used as evidence of an independently resolved registry image identity.

The relocation and symbol-version findings support qualified dynamic-linker compatibility in that runtime only. They do not prove compatibility with arbitrary Linux distributions or deployment images, nor do they establish Java/JNI loading compatibility. No Java native-library loading test occurred. The presence of Java 21 in the environment is not evidence that Java loaded or called this binary.

## 8. Binary integrity

The preserved binary SHA-256 was reported as:

```text
4fb83086375aceed64ceff59bb4ca0da235cb665c4e7e5b545982422c0a37c74
```

Final checksum verification passed. Build and inspection logs were preserved outside the repository. This identifies the reported preserved output; it is not a claim that an independent rebuild produced identical bytes. External preservation locations and separate log hashes were not supplied for inclusion here.

## 9. Evidence inventory

The supplied inventory identifies the following externally preserved artifacts. Filenames alone do not establish their contents or permanent accessibility.

| Artifact | Reported purpose/status |
| --- | --- |
| `configure.log` | Configuration log |
| `build.log` | Build log |
| `elf-header.txt` | ELF header inspection |
| `dynamic-symbols.txt` | Dynamic symbol inspection |
| `dynamic-section.txt` | Direct dynamic dependency inspection |
| `library-sha256.txt` | Preserved library checksum evidence |
| `libidn2-2.3.8.so` | Preserved shared object |
| `packages.txt` | **Empty: 0 bytes. Evidence deficiency; no complete build-time package manifest exists in this artifact.** |

The individual tool/package versions in §4 do not repair the empty package inventory. The supplied observations also include signature verification and runtime `ldd -r` results; no additional named log artifact is invented for those observations. Durable external paths, their retention/access arrangements and any additional provenance remain unestablished by this record.

## 10. Qualifications and limitations

- The build-time package manifest is empty; full package/dependency versions are not captured by `packages.txt`.
- Mutable APT repositories and an incomplete package inventory prevent a claim of fully reproducible builds.
- The GNU signature reported good verification with an expired signing key; independently established historical signing-time validity is not claimed.
- Production licensing compliance remains pending. Unicode data licensing was identified but not fully assessed; included dependencies and redistribution obligations require further review.
- AMD64 execution used emulation. No native-AMD64 performance, platform equivalence or resource-safety conclusion follows.
- No JNI implementation, Java native-library loading test or Punycode functional test occurred.
- No strict IDNA2008 conformance or hostile-input evaluation occurred. Public exports and resolved relocations do not establish DEC-0005 compliance.
- No production-readiness determination or production Architecture/dependency approval is made.
- Cumulative engineering effort remains unreconciled. This record invents neither an elapsed duration for this exercise nor a remaining balance.

Relevant governing boundaries remain [DEC-0005](../../DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md), [DEC-0001](../../DEC-0001-backend-build-tool-dependency-management.md) and the existing DEC-0009 record. Successful native building does not satisfy their separate conformance, admission, security, maintenance or acceptance gates.

## 11. Governance outcome

**Native build-verification exercise: QUALIFIED PASS (Qualified Technical Pass).** The reported successful build, expected public exports, output checksum verification and resolved runtime relocations support this bounded outcome, with the evidence deficiencies and qualifications above retained.

DEC-0009 remains **Proposed**. No candidate is selected, no production implementation or JNI implementation is authorized, and candidate evaluation remains **NO-GO**. This is not an A/B/C candidate-evaluation outcome and does not recast earlier exhausted evaluations. Further execution requires a separate governance decision and confirmation of the remaining allowance under the existing cumulative effort ceiling.
