# DEC-0009 bounded evaluation evidence — durable retention

Recorded: 2026-10-02. Evaluation date: 2026-10-01. Owner: Identity. Status: informational evidence; no implementation authority. Baseline develop, HEAD and local origin/develop ccddce97c68d25686c3d20224019e98b47759d1c. DEC-0009 0.3.0 Proposed. Identity owns evaluation; Security, Architecture and Engineering review remains applicable. No completed human reviews or acceptance are asserted.

## Outcome and scope

C — CURRENT REPOSITORY-OWNED COMPOSITION PATH STOPPED. Component evidence is useful, but a complete conformant composition is not demonstrated and material end-to-end security/resource behavior remains unresolved. The authorized stop conditions prohibit promoting or silently completing an approximation. This does not establish that all future repository-owned strategies are impossible. It stops this evaluated path pending review of specific gaps.

Executed component probes only: Unicode table lookup, JDK/ICU NFC detection, ICU properties, raw ICU/GNU Punycode, malformed and length boundaries. No custom Punycode, production validator, authoritative comparison-key factory or application integration was implemented. Complete CONTEXTJ/CONTEXTO/bidi/round-trip validation composition was NOT EXECUTED. The reasons are unresolved supported codec strategy and no established complete evaluator; raw property/codec results are not strict accept/reject results. No production tests/build were run.

## Governed profile and consistency

DEC-0005 controls input preparation, ASCII local-part lowercase with dots/plus preserved, ASCII-dot domain segmentation, strict already-normalized U-labels, valid canonical A-labels and rejection rather than Unicode mapping/repair. Ordinary ASCII labels use only governed ASCII lowercase. A-labels require decoded U-label validity and canonical re-encoding, not just successful codec conversion. Domain labels must meet contextual, bidi, syntax and length requirements before any canonical key or authoritative lookup.

CustomerLoginEmailInput already rejects malformed UTF-16, controls, whitespace and specified alternate separators, preserves remaining label content and explicitly creates no canonical key. Its tests intentionally preserve decomposed and invalid candidate labels. No contradiction found: this is preparation, not strict validation. Identity backend ownership, Architecture, DEC-0001 and Security standards are consistent with isolated evidence gathering. Authentication, Principal and Session authority remain separate.

No runtime Unicode upgrade may silently change equality. Existing strict semantics, no fallback, fail-closed lookup and privacy boundaries remain mandatory. No new Product limit or Unicode version is selected.

## Components

| Component | Identity, provenance, compatibility and implications |
|---|---|
| Temurin JDK | Installed Java 21.0.6+7-LTS, Java source compiler/runtime. JDK Character uses Unicode 15.0; Normalizer supplies NFC detection, Character supplies scalar/category/directionality helpers. java.text.Bidi is text layout, not RFC5893 validation. No public standalone Punycode API established. java.net.IDN remains previously demonstrated ineligible (IDNA2003); not rerun. OpenJDK GPLv2 with Classpath Exception; no new Maven dependency. Exact installation/vendor patch must be recorded and reviewed; this runtime is evidence, not a current-patch selection. |
| ICU4J | com.ibm.icu:icu4j:78.3; official Maven Central binary reused from prior scratch; matching sources downloaded. Maintained Unicode project, March 2026 maintenance release; Unicode-3.0 plus bundled notices. Runs on tested Java21. Published POM has no dependency entries. UCharacter property APIs and Normalizer2 NFC detection are useful, not full IDNA. Runtime reports Unicode17.0.0.0. Mapping API remains ineligible; not rerun. |
| ICU raw codec | com.ibm.icu.impl.Punycode in exact same release; encode/decode with null case flags preserves tested code points and avoids IDNA mapping. Internal implementation package, not established supported public codec contract. Source identifies quadratic algorithm and 1000-code-unit encode/2000-character decode caps. These are library limits, not Product policy. Independent suitability/maintenance and exhaustive RFC3492 proof remain unresolved. |
| Unicode data | Official Unicode17.0.0/idna/Idna2008.txt; header date 2025-07-23. Complete versioned classifications incl exceptions, contextual categories and unassigned. Unicode terms apply. Data has no Java dependencies and performs no normalization, context, bidi or codec work. Table ingestion and integrity become owner responsibilities. No independent recomputation of every classification was executed. |
| GNU Java codec | GNU libidn1.43 tarball, release 2025-03-21; java/src/main/java/gnu/inet/encoding/Punycode.java and PunycodeException.java compiled unchanged on Java21. Pure Java two-class codec, no external dependencies for these classes. Source explicitly supports only 16-bit code points. Source license LGPLv3-or-later/GPLv2-or-later dual, which differs from the broad website Java licensing summary: actual release requires review. Supplementary-code-point tests demonstrate incompatibility. GNU native project maintenance does not prove this Java implementation is suitable. |
| Other leads | Bounded follow-up searches for JVM strict validators, RFC5892/CONTEXTJ and independent codecs established no new complete maintained JVM validator. Historical OpenJDK prototype, PRECIS and Unicode research-tool paths do not overturn previous findings. Native libidn2/idnkit integrations were not executed; separate scope required. Search non-discovery is not proof of universal absence. |

The extracted file named README is GNU contrib/java/README for the historical native glue proof of concept; it is NOT evidence that the separately located pure-Java codec has the same maintenance status.

## Responsibility matrix

Legend: 1 existing governed production boundary; 2 reusable capability; 3 repository-owned validation/orchestration; 4 unresolved evidence/choice; 5 prohibited under authorization. Multiple classes reflect different parts of the responsibility.

| Responsibility | Class | Allocation |
|---|---|---|
| Input preparation/local part | 1 | Existing CustomerLoginEmailInput only; no changes. |
| Unicode scalar safety | 1/2/3 | Existing submitted-input checks; decoded strings still need scalar validation; Java code-point helpers are not scalar validity by themselves. |
| NFC requirement | 2/3 | ICU/JDK isNormalized; owner rejects false; no normalize-and-repair. |
| RFC5892 classification | 2/3/4 | Versioned table; reviewed parser/integrity/complete derivation reproduction unresolved. |
| Exceptions | 2/3 | Versioned table + protocol-update review, not ad hoc overrides. |
| UNASSIGNED | 2/3 | Explicit rejection under pinned profile. |
| CONTEXTJ | 2/3/4 | Joining type/ccc properties available; owner evaluates RFC contextual rules; complete implementation not executed. |
| CONTEXTO | 2/3/4 | Script/context data available; owner evaluates every contextual case; not executed. |
| RFC5893 | 2/3/4 | Bidi classes reusable; owner applies six rules with domain-wide applicability, including applicable ASCII labels; not executed. |
| Hyphens | 3 | Positions/ASCII and A-label distinctions. |
| Leading combining mark | 2/3 | Category properties plus owner rule. |
| Label/domain syntax | 1/3 | Preparation plus strict validation and domain composition. |
| A-label detection | 3 | Governed ASCII handling, prefix and syntax. |
| Punycode decode | 2/4/5 | ICU internal codec experimental only; GNU unsuitable; custom codec prohibited. |
| Decoded U-label validity | 3/4 | All strict rules again; not proven by decoding. |
| Canonical re-encode/compare | 2/3/4 | Codec plus owner comparison; complete chain not executed. |
| Fake/malformed xn-- | 3 | Reject empty, invalid scalar/property, ASCII-only fake labels and noncanonical round trips. |
| Label length | 3 | 63-octet encoded label limit; do not apply raw-codec output to ordinary ASCII labels. |
| Domain length | 3/4 | DNS wire-size accounting after conversion; full implementation not executed. |
| Alternative separators | 1/3 | Existing explicit exclusions plus strict disallowed-character gate; no mapping. |
| Mapping prohibition | 3 | Control pipeline; no forbidden mapper or normalization replacement. |
| Canonical key | 3/4 | Deterministic validated ASCII domain + governed local part; not produced by probes. |
| Hostile/resource bounds | 3/4 | Early governed bounds, bounded scans, safe exceptions; end-to-end safety unresolved. |
| Data pinning | 2/3 | Exact artifacts/hashes and synchronized property versions. |
| Upgrade behavior | 3/4 | Exhaustive acceptance/output/property diffs and reviewed rollout/rollback. |
| Retained identifier compatibility | 3/4 | Preserve ownership and comparison across versions; migration strategy not selected. |
| Collision/reconciliation | 1/3/4/5 | Existing DEC0005 authority; future governed workflow; production persistence/integration forbidden here. |

This is substantial protocol ownership, not a thin wrapper.

## Counterexample results

All detailed code points and component outputs are in results.txt; Probe.java defines the exact synthetic corpus. Punycode output below is payload only, without xn--. No UTS46 or IDNA2003 mapping API was called. ICU roundtrip refers to raw decode(encode(U)), not strict canonical A-label validation. S = sufficient evidence for that individual primitive only; I = insufficient for complete strict validation; NE = full strict chain NOT EXECUTED.

| Input (non-ASCII code points specified) | Expected strict disposition | Observed component evidence | Status |
|---|---|---|---|
| bücher (00FC) | Accept → xn--bcher-kva | ICU/GNU bcher-kva; NFC true; all PVALID | S/NE |
| xn--bcher-kva | Accept | ICU decodes bücher and re-encodes bcher-kva | S/NE |
| faß (00DF), fass | Accept distinctly | fa-hia vs fass- raw encoding; ordinary ASCII fass must not be framed as A-label | S/NE |
| ς (03C2), σ (03C3) | Accept distinctly | 3xa vs 4xa | S/NE |
| Ａ (FF21) | Reject | DISALLOWED; raw codec ph7c preserves width | S/NE |
| a<00AD>b | Reject | DISALLOWED soft hyphen; ab-5da retains it | S/NE |
| bu<0308>cher | Reject | Both NFC checks false; raw codec bucher-xyd preserves decomposition | S/NE |
| a<200C>b, a<200D>b | Reject | CONTEXTJ, codec succeeds; no context predicate run | I/NE |
| 0915 094D 200C/200D 0937 | Accept | Preceding virama ccc9, CONTEXTJ; codec succeeds | I/NE |
| a·b, l·l (00B7) | Reject / accept | Both CONTEXTO, codec succeeds for both | I/NE |
| xn--a | Reject | ICU decodes U+0080, re-encodes a; raw roundtrip succeeds | I/NE |
| xn-- | Reject | ICU decodes empty, roundtrip succeeds | I/NE |
| xn--! | Reject | ICU StringPrepParseException | S/NE |
| ab--cd, -a | Reject | All characters PVALID; raw encoding succeeds | I/NE |
| a<3002>b, a<FF0E>b, a<FF61>b | Reject | Separators DISALLOWED; no dot replacement by raw codec | S/NE |
| אב (05D0 05D1) | Accept | R/R; raw codec 4dbc | I/NE |
| אa (05D0 0061) | Reject | R/L; raw codec a-zhc succeeds | I/NE |
| lone UTF16 D800 | Reject | Both NFC checks true; ICU codec throws; GNU encodes ib9b | S for ICU scalar failure/NE |
| U+0378 | Reject unassigned | UNASSIGNED; codec zva succeeds | S classification/NE |
| 😀 / xn--e28h (1F600) | Reject | DISALLOWED; ICU decodes/encodes e28h exactly | I/NE |
| U+10300 | Accept as valid non-BMP letter | PVALID; ICU097c; GNUib9b21k; GNUdecode097c→0300 | GNU incompatible; ICU primitive S/NE |
| 0308 0061 | Reject initial mark | PVALID, NFC true, raw codec succeeds | I/NE |
| a×63 / a×64 | Accept / reject | NFC and codec do not enforce label limit | I/NE |
| ü×57 / ü×58 | Accept / reject encoded length | ICU payload59/60; with prefix63/64 | S lengths/I rejection/NE |
| (a.)×126+a / (a.)×127+a | Accept / reject DNS wire bound | 253/255 presentation chars, 127/128 labels; both NFC true | I/NE |
| (a.)×10000+a | Reject | 20001 chars/10001 labels; NFC true | I/NE |

GNU column in results.txt always encodes the literal input: its encoding of an xn-- string is NOT an A-label decoding test. GNU decoding tests are separately in codec-results.txt. Raw ASCII codec output has a trailing delimiter; that is not the canonical ordinary ASCII label.

## Codec questions

1. Executable Java21 raw codec exists in maintained ICU78.3, but a supported independent production codec contract was not established. GNU1.43's Java codec is demonstrably unsuitable for full Unicode scalars.
2. ICU null-caseFlags raw calls preserved tested width, ignored characters, decomposition, sharp-s and sigma without IDNA mapping. This is component evidence only.
3. detect xn-- → decode → strict U-label validation → re-encode → governed ASCII comparison is conceptually the required boundary, including fake-label rejection and encoded bounds. It was not demonstrated as a complete pipeline.
4. Owner retains prefix/ASCII handling, scalar/NFC/property/context/bidi/syntax/length checks, roundtrip comparison, safe failures, key composition and upgrades.
5. Repository adoption of any third-party codec requires DEC0001 admission; scratch execution admits nothing.
6. Failure to establish a suitable codec blocks this composition. Do not fill the gap with custom Punycode. Internal-API suitability remains unresolved, not proof that every possible codec fails.

## Security and resources

ICU source explicitly identifies a quadratic codec algorithm and defensive length caps. Executed: encode1000 succeeds, encode1001 throws ICUInputTooLongException; decode2000 succeeds, decode2001 throws it. These caps do not enforce DNS bounds: ü×58 still encodes to an overlong A-label. NFC checks accepted ASCII strings of 1000/10000/100000 units in approximately0.027/0.191/1.843ms in one un-warmed run. These observations are not benchmarks or worst-case guarantees; -Xmx128m was a harness containment setting, not Product policy.

NFC is not UTF16/scalar safety: lone D800 returned true. GNU non-BMP truncation could alter identity comparison if adopted; no production exposure exists here. GNU decoding BCHER-KVA threw StringIndexOutOfBoundsException; ICU preserves uppercase basic characters, so governed ASCII handling cannot be omitted. Fake xn--a and emoji show why codec roundtrip alone is unsafe.

Pathological label-count splitting allocates before semantic rejection; contextual scans can become quadratic if repeated across long transparent runs. Full normalization worst cases, contextual/bidi complexity, allocation ceilings, concurrency and composed failure paths were NOT EXECUTED. End-to-end resource behavior remains materially unresolved. Error details, raw labels and existence must not enter public responses/logs; probes contain synthetic data only. Fail closed before authoritative lookup.

## Unicode and retained identifiers

Observed U+1C8A: JDK category0/unassigned, ICU lowercase-letter and Unicode17 table PVALID. U+1C89 similarly differs in assignment but table is DISALLOWED. U+19DA is DISALLOWED in the evaluated official data. Mixing JDK15 properties with ICU/table17 therefore yields inconsistent repertoire decisions. NFC stability does not make all property versions interchangeable.

Exact table/artifact versions can be pinned. Reproduction of RFC5892 requires its ordered rules, exceptions and applicable updates against a single versioned UCD; compare every scalar with authoritative derived tables. That independent exhaustive recomputation and a multi-version diff were not executed. Future updates need reviewed property/acceptance/canonical-output differences, old/new retained-identifier replay and collision analysis. Never merge/reassign/arbitrarily choose conflicting records. Version metadata is one possible approach; a governed migration/compatibility strategy is another. Neither is selected, and a version prefix alone cannot solve ownership/collision policy. Rollback must account for identifiers newly admitted under newer data. No schema or real data work occurred.

## Architecture and admission

A pure-Java/property-data composition could stay behind Identity-owned boundaries without automatically requiring an ADR. It still requires Security/Engineering review and DEC0001 admission, exact versions, license and transitive inventory, verification metadata, locks and tests. Native/JNI/JNA alternatives introduce platform/ABI/container/loading/failure responsibilities requiring Architecture assessment and an ADR for material changes. No native integration was executed or adopted. No strategy, Unicode version or dependency is selected.

## Stop conditions

| Condition | Status | Reason |
|---|---|---|
| Strict semantics would need weakening | NOT TRIGGERED | No weakening attempted or established necessary for all possible compositions. |
| Required RFC behavior cannot be demonstrated | TRIGGERED for this evaluated path | No complete strict evaluator/chain demonstrated; primitive results must not be promoted to conformance. |
| Silent approximation required | NOT TRIGGERED | Missing rules remained explicit gaps. |
| Unapproved custom codec becomes necessary | UNRESOLVED | Supported codec choice unresolved; custom codec was not written and is not assumed the only remedy. |
| Unauthorized dependency admission necessary | NOT TRIGGERED | Scratch-only work. |
| Production integration required to prove feasibility | NOT TRIGGERED | No integration needed or performed. |
| Security/resource behavior remains materially unresolved | TRIGGERED | Composed worst-case behavior and safe failure paths unproven. |
| Unicode upgrade/retained compatibility cannot be bounded | UNRESOLVED | Obligations identified, complete upgrade evidence absent. |
| Authorized scope exceeded | NOT TRIGGERED | Component probes/source review only; no custom codec or production code. |

On these findings, stop exploratory implementation; preserve evidence for review. Stop is not a claim of universal technical impossibility.

## Gaps, review action and severity

Required gaps: supported scalar-correct codec strategy; full strict validator evidence including all contexts/domain bidi/canonical roundtrips; complete authoritative corpus and data derivation checks; hostile-input bounds; multi-version comparison and retained-identifier compatibility/rollback plan; full artifact licensing/security/provenance/admission review. Remaining owner logic is exactly the class3 responsibilities above, not an unreported helper layer.

Next governance action: Identity-led Security/Architecture/Engineering review of this stopped-path report, explicitly dispositioning codec support and the missing conformance/resource evidence before any separately scoped continuation. Keep DEC0009 Proposed; no acceptance, strategy selection or dependency admission follows. No document changes were made.

Findings (evaluation risks, not deployed vulnerabilities): Critical0; High2 (GNU scalar corruption; absent complete strict/security proof blocks acceptance); Medium2 (ICU internal-codec support contract; demonstrated mixed-version property disagreement and missing upgrade proof); Low0. Other unexecuted cases are evidence gaps, not claims of passing tests.

## Sources

- Repository DEC0005, DEC0009, DEC0001; SECURITY-STANDARDS; ARCHITECTURE; Identity backend specification; CustomerLoginEmailInput and its tests, at the baseline commit.
- https://www.unicode.org/Public/17.0.0/idna/Idna2008.txt
- https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/78.3/icu4j-78.3-sources.jar
- https://repo.maven.apache.org/maven2/com/ibm/icu/icu4j/78.3/icu4j-78.3.jar
- https://ftp.gnu.org/gnu/libidn/libidn-1.43.tar.gz
- https://lists.gnu.org/archive/html/help-libidn/2025-03/msg00014.html
- https://unicode-org.github.io/icu/userguide/dev/codingguidelines
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Character.html
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/text/Normalizer.html
- https://www.rfc-editor.org/rfc/rfc5890.html
- https://www.rfc-editor.org/rfc/rfc5891.html
- https://www.rfc-editor.org/rfc/rfc5892.html
- https://www.rfc-editor.org/rfc/rfc5893.html
- https://www.rfc-editor.org/rfc/rfc3492.html
- https://www.rfc-editor.org/rfc/rfc1035.html (63-octet label, 255-octet DNS wire name; root excluded in governed presentation)
- https://www.rfc-editor.org/rfc/rfc6452.html
- https://www.rfc-editor.org/rfc/rfc8753.html

## Retention and reproduction limitations

This document retains the material report, exact raw outputs, original repository-owned experimental harnesses and original hash inventory. All `/private/tmp` paths below are historical provenance, not required evidence storage. No third-party source or binaries are retained here. Reproduction requires reacquiring the exact upstream artifacts and the recorded JDK; availability and publisher identity must be checked independently. Hashes establish byte identity, not signature verification or dependency admission. Timings and compiled-class hashes may depend on the runtime/compiler. No experiments were rerun for this documentation change.

The two Java listings below are the original evaluation harnesses, retained as inert documentation, not repository test/production code. They call third-party codecs; they do not implement Punycode. Reproducing the stopped evaluation or expanding it requires separate explicit authorization. These instructions preserve reproducibility and are not execution authority.

Reproduction procedure (not executed by this recording change):

1. Choose isolated scratch storage and obtain Temurin Java 21.0.6+7-LTS. Set `scratch`, `jdk`, and `icu` in the retained shell listing to those locations.
2. Reacquire ICU4J 78.3 binary and sources, GNU libidn 1.43 archive, and Unicode17.0.0 Idna2008.txt from the exact Sources URLs. Check SHA-256 against the inventory below.
3. Extract only `libidn-1.43/java/src/main/java/gnu/inet/encoding/Punycode.java` and `PunycodeException.java` into scratch without modification. Their original source hashes are retained below. The unrelated original `README` came from `libidn-1.43/contrib/java/README`; do not confuse native glue with the pure-Java codec.
4. Save the two original Java listings as `Probe.java` and `CodecBoundary.java`, and the Unicode file as `Idna2008-17.0.0.txt`. Save the shell listing with paths adapted to the isolated environment; its original absolute paths explain the historical run only.
5. When separately authorized, the listed javac/java commands compile only scratch files and emit the two result logs. Compare semantic results against the retained raw outputs; resource timings are observations, not thresholds.

The ICU source inspected was `com/ibm/icu/impl/Punycode.java` in the source JAR; the published POM was `META-INF/maven/com.ibm.icu/icu4j/pom.xml` in the binary JAR. Both can be reacquired from their hash-identified artifacts. The original binary at `/private/tmp/dec0009-icu4j-78.3.jar` was reused, not newly admitted.

## Original probe harness

```java
import com.ibm.icu.lang.*;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.impl.Punycode;
import com.ibm.icu.util.VersionInfo;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
public class Probe {
 static String[] dp=new String[0x110000];
 static String cps(String s){return s.codePoints().mapToObj(x->String.format("U+%04X",x)).reduce((a,b)->a+" "+b).orElse("EMPTY");}
 static String prop(int cp){return dp[cp]+"/"+UCharacter.getPropertyValueName(UProperty.BIDI_CLASS,UCharacter.getIntPropertyValue(cp,UProperty.BIDI_CLASS),1)+"/"+UCharacter.getPropertyValueName(UProperty.JOINING_TYPE,UCharacter.getIntPropertyValue(cp,UProperty.JOINING_TYPE),1)+"/ccc="+UCharacter.getCombiningClass(cp);}
 static void probe(String id,String s){
  String codec;
  try {String u=s.startsWith("xn--")?Punycode.decode(s.substring(4),null).toString():s; String e=Punycode.encode(u,null).toString(); codec="decoded="+cps(u)+";encoded="+e+";roundtrip="+Punycode.decode(e,null).toString().equals(u);}
  catch(Exception e){codec=e.getClass().getSimpleName();}
  String g;try{g=gnu.inet.encoding.Punycode.encode(s);}catch(Exception e){g=e.getClass().getSimpleName();}
  System.out.println(id+"\t"+cps(s)+"\tNFC_ICU="+Normalizer2.getNFCInstance().isNormalized(s)+"\tNFC_JDK="+Normalizer.isNormalized(s,Normalizer.Form.NFC)+"\t"+s.codePoints().mapToObj(Probe::prop).toList()+"\tICU:"+codec+"\tGNU:"+g);
 }
 public static void main(String[] args)throws Exception{
  Arrays.fill(dp,"UNASSIGNED");
  for(String line:Files.readAllLines(Path.of(args[0]))){String q=line.split("#",2)[0].trim();if(q.isEmpty())continue;String[] f=q.split(";");String[] r=f[0].trim().split("\\.\\.");int a=Integer.parseInt(r[0],16),b=Integer.parseInt(r[r.length-1],16);Arrays.fill(dp,a,b+1,f[1].trim());}
  System.out.println("JAVA="+System.getProperty("java.runtime.version")+" ICU="+VersionInfo.ICU_VERSION+" UNICODE="+UCharacter.getUnicodeVersion());
  String[][] cases={{"bucher","bücher"},{"alabel","xn--bcher-kva"},{"sharp","faß"},{"ss","fass"},{"final_sigma","ς"},{"sigma","σ"},{"width","Ａ"},{"ignored","a\u00adb"},{"decomposed","bu\u0308cher"},{"zwnj_bad","a\u200cb"},{"zwj_bad","a\u200db"},{"zwnj_good","क्\u200cष"},{"zwj_good","क्\u200dष"},{"middle_bad","a·b"},{"middle_good","l·l"},{"fake","xn--a"},{"empty_ace","xn--"},{"bad_ace","xn--!"},{"hyphen34","ab--cd"},{"leading_hyphen","-a"},{"dot3002","a\u3002b"},{"dotff0e","a\uff0eb"},{"dotff61","a\uff61b"},{"bidi_good","אב"},{"bidi_bad","אa"},{"surrogate","\ud800"},{"unassigned","\u0378"},{"emoji","😀"},{"emoji_ace","xn--e28h"},{"supplementary_valid","\ud800\udf00"},{"initial_mark","\u0308a"},{"ascii63","a".repeat(63)},{"ascii64","a".repeat(64)},{"unicode63","ü".repeat(57)},{"unicode64","ü".repeat(58)}};
  for(String[] c:cases)probe(c[0],c[1]);
  for(int cp:new int[]{0x19da,0x1c89,0x1c8a,0x11f02})System.out.println("VERSION_PROBE "+cps(new String(Character.toChars(cp)))+" JDKtype="+Character.getType(cp)+" ICUtype="+UCharacter.getType(cp)+" "+prop(cp));
  for(int n:new int[]{1000,10000,100000}) {String s="a".repeat(n);long t=System.nanoTime();boolean ok=Normalizer2.getNFCInstance().isNormalized(s);System.out.println("NFC_RESOURCE units="+n+" normalized="+ok+" nanos="+(System.nanoTime()-t));}
  for(String s:List.of("a.".repeat(126)+"a","a.".repeat(127)+"a","a.".repeat(10000)+"a"))System.out.println("DOMAIN_COMPONENT_ONLY chars="+s.length()+" labels="+s.split("\\.",-1).length+" NFC="+Normalizer2.getNFCInstance().isNormalized(s));
 }
}
```

## Original codec-boundary harness

```java
import com.ibm.icu.impl.Punycode;
public class CodecBoundary {
 public static void main(String[] args){
  for(int n:new int[]{63,64,1000,1001,2000,2001}){
   try{System.out.println("encode "+n+" output="+Punycode.encode("ü".repeat(n),null).length());}catch(Exception e){System.out.println("encode "+n+" "+e.getClass().getSimpleName());}
   try{System.out.println("decode "+n+" output="+Punycode.decode("a".repeat(n),null).length());}catch(Exception e){System.out.println("decode "+n+" "+e.getClass().getSimpleName());}
  }
  for(String s:new String[]{"097c","e28h","BCHER-KVA","abc-","a","!"}){
   try{System.out.println("GNUdecode "+s+" "+gnu.inet.encoding.Punycode.decode(s).codePoints().mapToObj(x->String.format("U+%04X",x)).toList());}catch(Exception e){System.out.println("GNUdecode "+s+" "+e.getClass().getSimpleName());}
   try{String u=Punycode.decode(s,null).toString();System.out.println("ICUdecode "+s+" "+u.codePoints().mapToObj(x->String.format("U+%04X",x)).toList()+" reencode="+Punycode.encode(u,null));}catch(Exception e){System.out.println("ICUdecode "+s+" "+e.getClass().getSimpleName());}
  }
 }
}
```

## Original reproduction commands

```sh
#!/bin/sh
set -eu
scratch=/private/tmp/dec0009-eval-ccddce97
jdk=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
icu=/private/tmp/dec0009-icu4j-78.3.jar
"$jdk/bin/javac" -d "$scratch/classes" -cp "$icu" "$scratch/Punycode.java" "$scratch/PunycodeException.java" "$scratch/Probe.java"
"$jdk/bin/javac" -d "$scratch/classes" -cp "$icu:$scratch/classes" "$scratch/CodecBoundary.java"
"$jdk/bin/java" -Xmx128m -cp "$icu:$scratch/classes" Probe "$scratch/Idna2008-17.0.0.txt" > "$scratch/results.txt"
"$jdk/bin/java" -Xmx128m -cp "$icu:$scratch/classes" CodecBoundary > "$scratch/codec-results.txt"
```

## Raw component observations

```text
JAVA=21.0.6+7-LTS ICU=78.3.0.0 UNICODE=17.0.0.0
bucher	U+0062 U+00FC U+0063 U+0068 U+0065 U+0072	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0062 U+00FC U+0063 U+0068 U+0065 U+0072;encoded=bcher-kva;roundtrip=true	GNU:bcher-kva
alabel	U+0078 U+006E U+002D U+002D U+0062 U+0063 U+0068 U+0065 U+0072 U+002D U+006B U+0076 U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0062 U+00FC U+0063 U+0068 U+0065 U+0072;encoded=bcher-kva;roundtrip=true	GNU:xn--bcher-kva-
sharp	U+0066 U+0061 U+00DF	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0066 U+0061 U+00DF;encoded=fa-hia;roundtrip=true	GNU:fa-hia
ss	U+0066 U+0061 U+0073 U+0073	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0066 U+0061 U+0073 U+0073;encoded=fass-;roundtrip=true	GNU:fass-
final_sigma	U+03C2	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+03C2;encoded=3xa;roundtrip=true	GNU:3xa
sigma	U+03C3	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+03C3;encoded=4xa;roundtrip=true	GNU:4xa
width	U+FF21	NFC_ICU=true	NFC_JDK=true	[DISALLOWED/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+FF21;encoded=ph7c;roundtrip=true	GNU:ph7c
ignored	U+0061 U+00AD U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, DISALLOWED/Boundary_Neutral/Transparent/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+00AD U+0062;encoded=ab-5da;roundtrip=true	GNU:ab-5da
decomposed	U+0062 U+0075 U+0308 U+0063 U+0068 U+0065 U+0072	NFC_ICU=false	NFC_JDK=false	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Nonspacing_Mark/Transparent/ccc=230, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0062 U+0075 U+0308 U+0063 U+0068 U+0065 U+0072;encoded=bucher-xyd;roundtrip=true	GNU:bucher-xyd
zwnj_bad	U+0061 U+200C U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, CONTEXTJ/Boundary_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+200C U+0062;encoded=ab-j1t;roundtrip=true	GNU:ab-j1t
zwj_bad	U+0061 U+200D U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, CONTEXTJ/Boundary_Neutral/Join_Causing/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+200D U+0062;encoded=ab-m1t;roundtrip=true	GNU:ab-m1t
zwnj_good	U+0915 U+094D U+200C U+0937	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Nonspacing_Mark/Transparent/ccc=9, CONTEXTJ/Boundary_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0915 U+094D U+200C U+0937;encoded=11b2ezcs70k;roundtrip=true	GNU:11b2ezcs70k
zwj_good	U+0915 U+094D U+200D U+0937	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Nonspacing_Mark/Transparent/ccc=9, CONTEXTJ/Boundary_Neutral/Join_Causing/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0915 U+094D U+200D U+0937;encoded=11b2ezcw70k;roundtrip=true	GNU:11b2ezcw70k
middle_bad	U+0061 U+00B7 U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, CONTEXTO/Other_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+00B7 U+0062;encoded=ab-0ea;roundtrip=true	GNU:ab-0ea
middle_good	U+006C U+00B7 U+006C	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, CONTEXTO/Other_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+006C U+00B7 U+006C;encoded=ll-0ea;roundtrip=true	GNU:ll-0ea
fake	U+0078 U+006E U+002D U+002D U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0080;encoded=a;roundtrip=true	GNU:xn--a-
empty_ace	U+0078 U+006E U+002D U+002D	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0]	ICU:decoded=EMPTY;encoded=;roundtrip=true	GNU:xn---
bad_ace	U+0078 U+006E U+002D U+002D U+0021	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, DISALLOWED/Other_Neutral/Non_Joining/ccc=0]	ICU:StringPrepParseException	GNU:xn--!-
hyphen34	U+0061 U+0062 U+002D U+002D U+0063 U+0064	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+0062 U+002D U+002D U+0063 U+0064;encoded=ab--cd-;roundtrip=true	GNU:ab--cd-
leading_hyphen	U+002D U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+002D U+0061;encoded=-a-;roundtrip=true	GNU:-a-
dot3002	U+0061 U+3002 U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, DISALLOWED/Other_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+3002 U+0062;encoded=ab-r13a;roundtrip=true	GNU:ab-r13a
dotff0e	U+0061 U+FF0E U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, DISALLOWED/Common_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+FF0E U+0062;encoded=ab-yu3n;roundtrip=true	GNU:ab-yu3n
dotff61	U+0061 U+FF61 U+0062	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, DISALLOWED/Other_Neutral/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+FF61 U+0062;encoded=ab-213n;roundtrip=true	GNU:ab-213n
bidi_good	U+05D0 U+05D1	NFC_ICU=true	NFC_JDK=true	[PVALID/Right_To_Left/Non_Joining/ccc=0, PVALID/Right_To_Left/Non_Joining/ccc=0]	ICU:decoded=U+05D0 U+05D1;encoded=4dbc;roundtrip=true	GNU:4dbc
bidi_bad	U+05D0 U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Right_To_Left/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+05D0 U+0061;encoded=a-zhc;roundtrip=true	GNU:a-zhc
surrogate	U+D800	NFC_ICU=true	NFC_JDK=true	[DISALLOWED/Left_To_Right/Non_Joining/ccc=0]	ICU:StringPrepParseException	GNU:ib9b
unassigned	U+0378	NFC_ICU=true	NFC_JDK=true	[UNASSIGNED/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0378;encoded=zva;roundtrip=true	GNU:zva
emoji	U+1F600	NFC_ICU=true	NFC_JDK=true	[DISALLOWED/Other_Neutral/Non_Joining/ccc=0]	ICU:decoded=U+1F600;encoded=e28h;roundtrip=true	GNU:8c9bk9h
emoji_ace	U+0078 U+006E U+002D U+002D U+0065 U+0032 U+0038 U+0068	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/European_Separator/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/European_Number/Non_Joining/ccc=0, PVALID/European_Number/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+1F600;encoded=e28h;roundtrip=true	GNU:xn--e28h-
supplementary_valid	U+10300	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+10300;encoded=097c;roundtrip=true	GNU:ib9b21k
initial_mark	U+0308 U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Nonspacing_Mark/Transparent/ccc=230, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0308 U+0061;encoded=a-bcb;roundtrip=true	GNU:a-bcb
ascii63	U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061;encoded=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-;roundtrip=true	GNU:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-
ascii64	U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061 U+0061;encoded=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-;roundtrip=true	GNU:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa-
unicode63	U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC;encoded=tdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa;roundtrip=true	GNU:tdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
unicode64	U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC	NFC_ICU=true	NFC_JDK=true	[PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0, PVALID/Left_To_Right/Non_Joining/ccc=0]	ICU:decoded=U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC U+00FC;encoded=tdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa;roundtrip=true	GNU:tdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
VERSION_PROBE U+19DA JDKtype=11 ICUtype=11 DISALLOWED/Left_To_Right/Non_Joining/ccc=0
VERSION_PROBE U+1C89 JDKtype=0 ICUtype=1 DISALLOWED/Left_To_Right/Non_Joining/ccc=0
VERSION_PROBE U+1C8A JDKtype=0 ICUtype=2 PVALID/Left_To_Right/Non_Joining/ccc=0
VERSION_PROBE U+11F02 JDKtype=5 ICUtype=5 PVALID/Left_To_Right/Non_Joining/ccc=0
NFC_RESOURCE units=1000 normalized=true nanos=26834
NFC_RESOURCE units=10000 normalized=true nanos=190834
NFC_RESOURCE units=100000 normalized=true nanos=1843292
DOMAIN_COMPONENT_ONLY chars=253 labels=127 NFC=true
DOMAIN_COMPONENT_ONLY chars=255 labels=128 NFC=true
DOMAIN_COMPONENT_ONLY chars=20001 labels=10001 NFC=true
```

## Raw codec observations

```text
encode 63 output=65
decode 63 output=63
encode 64 output=66
decode 64 output=64
encode 1000 output=1002
decode 1000 output=1000
encode 1001 ICUInputTooLongException
decode 1001 output=1001
encode 2000 ICUInputTooLongException
decode 2000 output=2000
encode 2001 ICUInputTooLongException
decode 2001 ICUInputTooLongException
GNUdecode 097c [U+0300]
ICUdecode 097c [U+10300] reencode=097c
GNUdecode e28h [U+F600]
ICUdecode e28h [U+1F600] reencode=e28h
GNUdecode BCHER-KVA StringIndexOutOfBoundsException
ICUdecode BCHER-KVA [U+0042, U+00FC, U+0043, U+0048, U+0045, U+0052] reencode=BCHER-kva
GNUdecode abc- [U+0061, U+0062, U+0063]
ICUdecode abc- [U+0061, U+0062, U+0063] reencode=abc-
GNUdecode a [U+0080]
ICUdecode a [U+0080] reencode=a
GNUdecode ! PunycodeException
ICUdecode ! StringPrepParseException
```

## Original repository/environment observation

```text
branch: develop
HEAD/origin:
ccddce97c68d25686c3d20224019e98b47759d1c
ccddce97c68d25686c3d20224019e98b47759d1c
git status --short:
?? .vscode/
git diff --check:
Python: 3.14.7 (main, Aug  5 2026, 10:29:49) [Clang 21.0.0 (clang-2100.1.1.101)]
```

## Original scratch file inventory and SHA-256 values

```text
b9c469fa0fc8ae63a580a7404f1c9bbe77635e25f2892c0d82f23d49a221bba2  /private/tmp/dec0009-eval-ccddce97/CodecBoundary.java
0e794a4a0ce1cb4ca27c92898b33786c284426b9b33f11a1bdfc9bddb13d47ff  /private/tmp/dec0009-eval-ccddce97/ICU-Punycode.java.txt
e4a7526a8a37539c0defa4da25f5dbf77d0212a14d4762d455cadea608a8921c  /private/tmp/dec0009-eval-ccddce97/Idna2008-17.0.0.txt
5bd625fcef2b88b5ca5f3623d28ed37a5dd6535578faa113e8c6b346fbcbf799  /private/tmp/dec0009-eval-ccddce97/Probe.java
aaeafcbd553b7ae06ead3b9cd8c1334610f269c5f45e7e6934813d48aed45f70  /private/tmp/dec0009-eval-ccddce97/Punycode.java
230d053e81181314614198e356b605c04b6b4647c518d4c91118701c60dd7f49  /private/tmp/dec0009-eval-ccddce97/PunycodeException.java
c65acaa9282bfdf832231c364eb755e27dcb8b4e1e8b50d1947f523a76a7892a  /private/tmp/dec0009-eval-ccddce97/README
d6eed938713e3f315abc4124d0f28ff03a8d3cf1b1c7f9af3cfcca0b817f3e18  /private/tmp/dec0009-eval-ccddce97/REPORT.md
ef151303ec620dc75bc2b387eb28b02a88da8c9e8cddfe23cefd0e43def8b2bc  /private/tmp/dec0009-eval-ccddce97/classes/CodecBoundary.class
512b3c17d474b585d433996d66a6caf9df53cff3f78ff79772a60fa5ef1e91e5  /private/tmp/dec0009-eval-ccddce97/classes/Probe.class
1d8760eaf7d5f6674efce760493bb7da420a160a442d0b1dae4bed9978879c6f  /private/tmp/dec0009-eval-ccddce97/classes/gnu/inet/encoding/Punycode.class
6aa3eb5cb372e1e6a56c1c65594e9c14a78632a751e13d0f00d40b1d32300c4d  /private/tmp/dec0009-eval-ccddce97/classes/gnu/inet/encoding/PunycodeException.class
15b89eec00dac10a1c1cfb7d13c6f99e8a75134bd7b22e25ea7e3da663b8276f  /private/tmp/dec0009-eval-ccddce97/codec-results.txt
3b5940df133c35ab1d511a4b34e271d152e1a51609bc5cad2ee22829fae6adca  /private/tmp/dec0009-eval-ccddce97/icu-pom.xml
b8458be1c296aec6c52ea8f195f27e833c5d6030fa15a4b361a0e83030c3c3fe  /private/tmp/dec0009-eval-ccddce97/icu4j-78.3-sources.jar
bdc662c12d041b2539d0e638f3a6e741130cdb33a644ef3496963a443482d164  /private/tmp/dec0009-eval-ccddce97/libidn-1.43.tar.gz
24b14d6fb10c64eba7df5098952ca1b10853f684b7f33c82edf9b213da7de242  /private/tmp/dec0009-eval-ccddce97/repository-state.txt
e88a8d5589e80a3465bcf4076006aecc61aad00ccb186858e2f0440234d1398a  /private/tmp/dec0009-eval-ccddce97/reproduce.sh
50d22691f0f95e010935b6fb16282bf9223d4fcf054012b80ac777a44073e277  /private/tmp/dec0009-eval-ccddce97/results.txt
e962c1758d9659ea1e1fbab99c58683f654d304e1126ace19aaabfe39e0edb25  /private/tmp/dec0009-icu4j-78.3.jar
```

The original scratch directory additionally contained `SHA256SUMS` itself (which intentionally does not hash itself). All other original files, including REPORT.md, extracted files, downloaded artifacts, generated classes, logs and scripts, are enumerated in the manifest above. REPORT.md's hash identifies the original report, not this expanded durable artifact. No historical hash has been recomputed or changed here.

## Governance disposition and recording review

The post-evaluation governance disposition was RECORD THEN AUTHORIZE ONE NEW EVIDENCE QUESTION. This change performs recording only. The candidate question concerns direct ICU4J78.3 internal-codec supportability without copying/forking or UTS46; it is a recommendation only. No investigation is authorized by this artifact. The single 0.3.0 evaluation is complete and its authorization exhausted. DEC-0009 remains Proposed.

Required durable-record interpretation: the GNU scalar failures are demonstrated; ICU/table/NFC observations are primitive capabilities; the complete strict chain is NOT EXECUTED; codec supportability, complete security/resource proof and upgrade/retained-identifier compatibility remain UNRESOLVED. No universal impossibility was established. No dependency, production implementation, Architecture, Contract/OpenAPI, persistence, or Accepted semantic change resulted from the evaluation or this retention change.
