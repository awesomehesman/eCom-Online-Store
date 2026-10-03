# DEC-0009 — ICU4J internal Punycode supportability evidence

Recorded and evaluated: 2026-10-03. Owner: Identity. Informational evidence only; no implementation authority. Repository baseline: `98964310b5ccc5aebfc9682e3d2f18605a6f9825`. Authorization: [DEC-0009 0.5.0 Proposed](../../DEC-0009-customer-login-idna2008-implementation-strategy.md), merged through PR #122 according to the task context. No human review or acceptance approval is asserted.

## Authorization and exact question

> Can direct use of `com.ibm.icu.impl.Punycode` from ICU4J 78.3 be supported as an independently maintained codec dependency, with an explicit compatibility, security-maintenance and upgrade basis, without copying/forking the codec or relying on UTS #46 processing?

This evaluates only raw-codec supportability. The [prior stopped evaluation](bounded-evaluation-2026-10-02.md), including its unexecuted full validation composition and unresolved resource/security findings, is unchanged. Its authorization is exhausted. This evaluation neither repeats that composition nor claims all future compositions impossible.

Applicable governance reviewed: DEC-0009's exact authorization and A/B/C criteria; DECISIONS.md's Proposed-state and index requirements; DEC-0001's fixed versions, integrity, dependency-admission and review controls; SECURITY-STANDARDS.md sections 25–26. This report does not synchronize governance; that is a separate subsequent change.

## Evidence inventory and method

Before executable probes, the finite comparison inventory was recorded in scratch: **74.2**, an older release; **77.1**, the preceding major; **78.3**, the candidate. Other versions were not runtime candidates. Historical commit inspection explains maintenance changes, not additional executable evaluation. [Maven Central metadata](https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/maven-metadata.xml) retrieved on the evaluation date reported latest/release 78.3 and lastUpdated `20260317204313`. No later release was available in that published artifact inventory; forward compatibility is not tested.

Execution environment: macOS arm64, Temurin Java `21.0.6+7-LTS`; direct `javac` and `java`, no Gradle invocation. Each process used `-Xmx64m` and an external 20-second process timeout. These are experimental bounds, not production limits or a performance benchmark. Scratch location: `/private/tmp/dec0009-supportability-2m6pa955`. The complete probe and output are retained below so scratch survival is unnecessary.

For each version, downloaded the binary JAR, sources JAR and POM from `https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/{version}/icu4j-{version}{suffix}`, where suffix is `.jar`, `-sources.jar` or `.pom`. Independently calculated SHA-1 matched each published `.sha1` sidecar, then calculated SHA-256. This is registry consistency evidence, not independent publisher-signature verification or a reproducible source-to-binary attestation. No PGP verification or comprehensive vulnerability scan is claimed.

| Artifact | SHA-256 |
| --- | --- |
| `icu4j-74.2.jar` | `95c055080e14c093ebeeba5b733e1a1be7a4af5854668c774cedf070d4240e43` |
| `icu4j-74.2-sources.jar` | `80aee5c42a88e6f9b881d419ba142d2135da24ff9e2eb03d9324a1f121f955ff` |
| `icu4j-74.2.pom` | `ad5c4681cffa8362fa63462515586e0a39492e86eeb3f2d8d8db8d527585463e` |
| `icu4j-77.1.jar` | `b3640b9f416a4411fd33c59abbeea8fd57d024c23e1819bf9673220a97499fe3` |
| `icu4j-77.1-sources.jar` | `2dd9da85182efca525aa269db573e0ed54943767d6af3afc50d0ede51e63d01a` |
| `icu4j-77.1.pom` | `16e54bf6bfa9fb221bb57e133a8373f442110f3dae3710156e92615c82476e18` |
| `icu4j-78.3.jar` | `e962c1758d9659ea1e1fbab99c58683f654d304e1126ace19aaabfe39e0edb25` |
| `icu4j-78.3-sources.jar` | `b8458be1c296aec6c52ea8f195f27e833c5d6030fa15a4b361a0e83030c3c3fe` |
| `icu4j-78.3.pom` | `3b5940df133c35ab1d511a4b34e271d152e1a51609bc5cad2ee22829fae6adca` |

The candidate JAR and sources hashes match the prior durable report. Upstream source was read from the sources archives solely for inspection; no ICU implementation was copied into the probe, report, repository, fork or replacement codec.

## Upstream support and maintenance findings

**Verified policy:** ICU distinguishes its application-facing API from internal implementation interfaces. Its design policy reserves internal interfaces for ICU and allows them to change without notice. Java `public` accessibility does not confer supported external-API status. `com.ibm.icu.impl.Punycode` belongs to that internal package and has no stable external lifecycle declaration in the inspected source. Historical availability is therefore not a compatibility commitment. [ICU design policy, pinned snapshot](https://github.com/unicode-org/icu/blob/13e2c82113189340003d4ed4611966ea76a064bd/docs/userguide/icu/design.md#internal-apis)

**Verified project maintenance:** the official 78.3 release was published on 2026-03-17 and describes a maintenance update. This demonstrates maintained ICU distribution, not a standalone codec product or an external contract for this class. [Official release](https://github.com/unicode-org/icu/releases/tag/release-78.3)

**Verified security handling:** ICU publishes a private sensitive-issue reporting route, describes volunteer reasonable-effort maintenance, and requests a 90-day remediation window before disclosure. It does not promise this caller a codec-specific fix deadline or compatible replacement. No report was submitted and no maintainer was contacted. [Security policy, pinned snapshot](https://github.com/unicode-org/icu/blob/13e2c82113189340003d4ed4611966ea76a064bd/SECURITY.md)

**Verified backport model:** the published maintenance procedure describes fixing the development stream first and considering older-release merges through project approval in response to demand. Backport availability is discretionary, not a guaranteed escape from an incompatible newer API. The page uses historical workflow terminology; it is evidence of the published model, not confirmation of every current operational detail. [Maintenance procedure](https://icu.unicode.org/processes/maintenance-releases)

**Concrete maintenance history:** source history includes a 2020-08-14 correction to decode-digit range checking, and a 2020-08-31 change adding encode/decode input caps, a new overlength exception and an overflow-guard adjustment. These show actual codec maintenance and behavioral changes, not an abandoned file. They also show why signatures alone cannot cover compatibility. No CVE classification is inferred from these commits. [Digit-range fix](https://github.com/unicode-org/icu/commit/e19d12997b1ab9599125c77709bfaf3c8c9ec09f), [input-limit fix](https://github.com/unicode-org/icu/commit/2f39d33498871e00f5ceb0d3c50eac635f8a0554)

**Assessment/inference:** pinning fixes today's artifact identity but postpones upgrade risk. A security fix elsewhere in the same ICU artifact could require an upgrade that relocates, removes or changes this internal class. That is a plausible risk, not an observed break in the three tested releases. A failing compatibility gate can stop deployment, but cannot supply a compatible patched codec. No external maintenance undertaking or guaranteed supported replacement was established. Retaining a vulnerable pin, copying the implementation or silently selecting another codec is not an authorized remedy.

## Compatibility and UTS #46 isolation

All three extracted `com/ibm/icu/impl/Punycode.java` files are byte-identical, SHA-256 `0e794a4a0ce1cb4ca27c92898b33786c284426b9b33f11a1bdfc9bddb13d47ff`. `javap -public` outputs were identical: public final class, public constructor, and static `encode(CharSequence, boolean[])` / `decode(CharSequence, boolean[])`, returning `StringBuilder` and declaring `StringPrepParseException`. Each release compiled and executed the probe. Classes compiled against 78.3 also linked and ran against 74.2 and 77.1, with identical output. This is sampled historical compatibility, not all-release, forward, module-path or multi-environment proof.

Source inspection identifies direct ICU dependencies on `UCharacter`, `UTF16`, `StringPrepParseException` and `ICUInputTooLongException`, plus JDK string/arithmetic operations. The invoked two-argument `UCharacter.getCodePoint` combines a checked surrogate pair; UTF16 helpers classify or construct surrogates. The direct codec methods do not call UTS46, IDNA, normalization, case folding or Unicode-property validation. The probe uses null case flags; alternate flag behavior was not evaluated. [Candidate source archive](https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/78.3/icu4j-78.3-sources.jar)

A candidate run with `-Xlog:class+load=info:file=class-load.txt` did not load `com.ibm.icu.impl.UTS46` or `com.ibm.icu.text.IDNA`. This supports the inspected direct path, not an exhaustive transitive initialization audit. No claim is made that ICU loads no other data or classes.

The observed round trips preserve sharp-s, separate sigma forms, fullwidth input, soft hyphen, decomposed input and alternate separators. Thus raw calls can technically remain isolated from mapping. Their preservation of invalid IDNA candidates, including emoji, also demonstrates why codec success is not a strict validation result. `ABC` encodes as `ABC-`; this is raw Punycode, not an IDNA label conversion with prefix or canonical comparison handling.

The source has a quadratic algorithm with encode input capped at 1000 UTF-16 units and decode input capped at 2000 characters. Probes observed success at each cap and `ICUInputTooLongException` immediately above it. These are library bounds, not protocol/Product limits, throughput evidence or complete denial-of-service protection.

## Supply chain and no-copy feasibility

The unchanged `com.ibm.icu:icu4j:78.3` binary was directly callable on Java 21 without copying, modifying, vendoring, forking or reimplementing the codec. The application probe contains calls only. Technical feasibility is positive on the ordinary classpath.

The candidate POM declares Unicode-3.0 licensing and no dependencies or parent; no additional Maven transitive dependency is declared. The 15,156,073-byte JAR includes ICU's wider functionality/data, so this is not a separately published small codec artifact. Its manifest declares automatic module name `com.ibm.icu`; its OSGi exported-package list omits `com.ibm.icu.impl`. OSGi/module-path deployment was not tested or proposed. [Candidate POM](https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/78.3/icu4j-78.3.pom), [candidate JAR](https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/78.3/icu4j-78.3.jar)

A later admission would still require DEC-0001 review of the full artifact, license/notices, provenance, vulnerabilities, exact version, locks and verification metadata, Java compatibility and upgrade burden. Availability from Maven Central and a permissive license do not resolve this candidate's internal-API supportability. No complete licensing clearance, security clearance or admission proposal is asserted.

## Reproducible probe

Save the following original evaluation harness as `CodecProbe.java` in an external scratch directory alongside the verified JARs. It implements no codec or IDNA validator. The round-trip assertions verify exact preservation; malformed/boundary outputs are observations, not a complete conformance oracle. SHA-256 of the executed harness: `b53a33e99e0db5040d663b8d5d2326a65fe6dba7f0cfc84334fb6a30c125a326`.

```java
import com.ibm.icu.impl.Punycode;
public class CodecProbe {
  static String points(String s) {
    return s.codePoints().mapToObj(c -> String.format("U+%04X", c))
      .collect(java.util.stream.Collectors.joining(" "));
  }
  static void round(String label, String s) throws Exception {
    String e = Punycode.encode(s, null).toString();
    String d = Punycode.decode(e, null).toString();
    if (!s.equals(d)) throw new AssertionError(label);
    System.out.println(label + " | " + e + " | " + points(d) + " | roundtrip=true");
  }
  static void call(String label, boolean encode, String input) {
    try {
      String out = (encode ? Punycode.encode(input, null) : Punycode.decode(input, null)).toString();
      System.out.println(label + " | OK length=" + out.length());
    } catch (Exception e) { System.out.println(label + " | " + e.getClass().getName()); }
  }
  public static void main(String[] args) throws Exception {
    System.out.println("Java=" + System.getProperty("java.runtime.version"));
    round("bucher", "b\u00fccher");
    round("sharp-s", "fa\u00df");
    round("sigma", "\u03c3");
    round("final-sigma", "\u03c2");
    round("fullwidth", "\uff41");
    round("soft-hyphen", "a\u00adb");
    round("decomposed", "e\u0301");
    round("alternate-dot", "a\u3002b");
    round("supplementary", "\ud800\udf00");
    round("emoji", "\ud83d\ude00");
    round("ascii-case", "ABC");
    call("lone-surrogate", true, "\ud800");
    call("malformed", false, "!");
    call("encode1000", true, "a".repeat(1000));
    call("encode1001", true, "a".repeat(1001));
    call("decode2000", false, "a".repeat(1999) + "-");
    call("decode2001", false, "a".repeat(2000) + "-");
  }
}
```

With `JAVA21` pointing to a Java 21 installation, the executed command pattern was:

```sh
for v in 74.2 77.1 78.3; do
  mkdir -p "classes-$v"
  "$JAVA21/bin/javac" -cp "icu4j-$v.jar" -d "classes-$v" CodecProbe.java
  "$JAVA21/bin/java" -Xmx64m -cp "classes-$v:icu4j-$v.jar" CodecProbe
  "$JAVA21/bin/javap" -public -classpath "icu4j-$v.jar" com.ibm.icu.impl.Punycode
done
for v in 74.2 77.1; do
  "$JAVA21/bin/java" -Xmx64m -cp "classes-78.3:icu4j-$v.jar" CodecProbe
done
"$JAVA21/bin/java" -Xmx64m -Xlog:class+load=info:file=class-load.txt \
  -cp classes-78.3:icu4j-78.3.jar CodecProbe
```

The evaluator invoked these commands with Python `subprocess`, enforcing a 20-second timeout per Java run. All compilations/runs completed successfully. Each of the three releases and both cross-link runs produced the following identical output:

```text
Java=21.0.6+7-LTS
bucher | bcher-kva | U+0062 U+00FC U+0063 U+0068 U+0065 U+0072 | roundtrip=true
sharp-s | fa-hia | U+0066 U+0061 U+00DF | roundtrip=true
sigma | 4xa | U+03C3 | roundtrip=true
final-sigma | 3xa | U+03C2 | roundtrip=true
fullwidth | mi7c | U+FF41 | roundtrip=true
soft-hyphen | ab-5da | U+0061 U+00AD U+0062 | roundtrip=true
decomposed | e-xbb | U+0065 U+0301 | roundtrip=true
alternate-dot | ab-r13a | U+0061 U+3002 U+0062 | roundtrip=true
supplementary | 097c | U+10300 | roundtrip=true
emoji | e28h | U+1F600 | roundtrip=true
ascii-case | ABC- | U+0041 U+0042 U+0043 | roundtrip=true
lone-surrogate | com.ibm.icu.text.StringPrepParseException
malformed | com.ibm.icu.text.StringPrepParseException
encode1000 | OK length=1001
encode1001 | com.ibm.icu.util.ICUInputTooLongException
decode2000 | OK length=1999
decode2001 | com.ibm.icu.util.ICUInputTooLongException
```

Reproduce downloads and byte checks in scratch with standard HTTPS retrieval, `hashlib.sha1(bytes).hexdigest()` compared to the corresponding `.sha1` sidecar, and `hashlib.sha256(bytes).hexdigest()` compared to the table above. Extract the inspection source using Python `zipfile.ZipFile(...).read("com/ibm/icu/impl/Punycode.java")`; compare exact bytes and hashes. Do not compile or redistribute that extracted implementation. Policies were fetched from upstream main and checked byte-for-byte against commit `13e2c82113189340003d4ed4611966ea76a064bd`; the pinned links above preserve the reviewed policy revision.

Two guessed modern maintenance-document URLs failed (one HTTP 404 and one browser access failure); the published maintenance page cited above was retrieved successfully. These retrieval failures do not conceal missing probe results or change the candidate outcome.

## Required controls for any separately governed future proposal

These controls are identified requirements, not implemented tests or approval:

- Exact immutable artifact/version pinning, reviewed byte/signature evidence, inventory of the entire distribution and dependency/security monitoring under named Identity ownership.
- Compile and runtime-link gates for class/package visibility, signatures, return types and exceptions; explicitly check actual deployment packaging. No reflective fallback to bypass missing APIs.
- Source-attributed RFC 3492 known-vector tests with expected outputs independent of round trips; supplementary scalars, malformed UTF-16, malformed payloads, overflow and boundary regressions. The small corpus here is insufficient for production.
- Behavior comparison across each proposed upgrade, including exceptional paths, internal helper changes, input caps and no-mapping isolation. Preserve exact data/implementation versions and review differences rather than refreshing expected results automatically.
- Resource/hostile-input tests with governed bounds, measured CPU/memory and safe error translation; do not leak submitted identifiers or dependency details.
- Security monitoring of ICU releases/advisories and relevant source changes, triage ownership and a documented response when a required security update breaks the codec. A version pin and test suite are detection controls, not a maintenance commitment or remediation mechanism.
- Separate retained-identifier compatibility, collision and rollback gates before any future canonicalization change. Security, Architecture and Engineering must review residual risks; required Identity ownership and human approvals remain outstanding.

## Unresolved matters and exclusions

No forward-release compatibility, exhaustive RFC 3492 correctness, fuzzing, performance guarantee, production packaging proof, independent publisher-signature verification, comprehensive vulnerability assessment, or upstream support undertaking was established. These limits do not turn sampled success into a supported public contract. No claim is made that internal API use is universally impossible or that upstream would refuse every bug report.

RFC 5890/5891/5892/5893, CONTEXTJ/CONTEXTO, domain-aware Bidi, complete A-label/U-label validation, Unicode property/version strategy, unassigned-code-point handling, no-mapping validation, resource safety of a composed pipeline and canonical comparison-key construction remain separate unresolved obligations. A working codec resolves none of them. No Login/IDNA implementation, real customer input, persistence, Authentication, Principal, Session, API Contract, UTS #46 substitution, alternative-library search, native integration, or renewed java.net.IDN/GNU evaluation occurred.

## Terminal outcome and consequences

**B. SUPPORTABILITY BASIS NOT DEMONSTRATED**

Direct ICU4J 78.3 internal Punycode use must be removed from the candidate path under this evaluation. The decisive issue is not failed conversion or an observed break in the tested releases: it is the combination of explicit internal-only/no-notice compatibility policy, discretionary maintenance/backports, and no demonstrated external upgrade/remediation basis within the no-copy/no-fork boundary. Successful historical tests and generic ICU maintenance do not establish that missing basis. Detection gates alone cannot make a required incompatible security update usable.

This is an evidence-backed negative supportability assessment, not a stopped investigation caused by inaccessible decisive evidence. The policy, artifact and executable evidence needed for that assessment were available. It does not require a guarantee that software never changes; it requires a defensible response to incompatible change, which this path has not established. A hypothetical future support undertaking is not current evidence.

This rejects only this direct internal-codec candidate path, not every strict-IDNA2008 strategy. Under DEC-0009 0.5.0 the single authorization is exhausted by this report. Subsequent governance synchronization and any further evaluation require their own authorized change; this report does not perform them. DEC-0009 remains Proposed. No dependency admission, production implementation, Contract change, DEC acceptance, or change to DEC-0009 or DECISIONS.md occurred.
