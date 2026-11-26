# DEC-0048 — Customer Login Email Canonicalization Profile

- **Identifier:** DEC-0048
- **Title:** Customer Login Email Canonicalization Profile
- **Type:** Product Decision / Security Decision / Data Decision
- **Status:** Proposed
- **Version:** 0.1.0
- **Date:** 2026-11-26
- **Owner:** Identity
- **Authoritative:** false
- **Supersedes:** Proposed successor to DEC-0005 on acceptance and synchronization
- **Superseded By:** N/A

## Context and proposed decision

DEC-0005 remains effective. Its strict no-mapping IDNA2008 policy has no qualified Java implementation in the recorded evidence. Propose UTS #46 non-transitional domain processing with the public ICU4J 79.1 IDNA API and Unicode 18.0.0 data. This changes the equality policy because mapping and normalization can admit inputs or collapse distinctions previously rejected under DEC-0005. Keep the ASCII dot-atom local part, ASCII case folding, separation of delivery representation from the comparison key, and prohibition on provider-specific rewriting.

Process the complete domain with explicit non-transitional ASCII and Unicode flags, STD3, Bidi, CONTEXTJ and CONTEXTO checks. Reject malformed Unicode, controls, empty labels, alternate dot separators and root-dot input before processing. Reject errors, invalid A-label round trips, out-of-bound labels and domains, and **trailing root dots in canonical output before constructing a comparison key**. Apply the governed email input and output bounds. Retain deterministic ASCII comparison independent of locale or database collation.

Consider retaining strict IDNA2008 if a supported conformant implementation becomes available. A bounded ordinary ASCII domain repertoire remains a fallback requiring explicit Product approval if the internationalized candidate cannot qualify.

## Qualification and corrective authorization

The [qualification evidence](evidence/DEC-0048/product-02b-registration-mechanism-qualification.md) records 6,396 non-transitional ToASCII comparisons, 68 differences and 21 passed supplemental domain checks. The original follow-up output reported an aggregate A4_2/root-dot classification. The complete row trace is unavailable for independent verification. The protocol comparison and application restrictions have separate evidence requirements. Overall disposition is **PARTIAL**. The historical full row inventory was discarded with the scratch harness.

**Pending authorization request:** Identity to authorize one checksum-pinned Java 21 scratch reconstruction, with Security and Engineering review of the scope. Use the same exact JAR and vectors, no candidate substitution, synthetic inputs only, a 256 MiB JVM heap and at most 30 minutes of execution. Reconstruct the 68 rows, retain only source row number, inherited status, expected and observed outcome, classification and reason. Exercise the output guard on those rows and verify complete governed email bounds, contextual and Bidi rejection, and bounded hostile-input behavior. Keep vector comparison and application-profile checks separate. Stop on an unexpected disagreement, provenance failure or scope expansion. This request authorizes no run by itself.

## Approval and transition conditions

Preserve one retained authoritative owner per canonical key. Obtain an authoritative inventory including inactive and retained ownership before cutover, then classify invalid records and collisions without writes. Resolve conflicts with accountable disposition and no automatic merge, reassignment or release. Neither synthetic database emptiness nor a profile-version column establishes ownership compatibility.

Identity, Product, Security, Architecture, Customer and applicable Data/Privacy dispositions remain PENDING. Engineering qualification and DEC-0001 exact dependency admission remain separate. Require artifact provenance, license and notice review, including the distributed artifact, before admission. If accepted, synchronize the canonical index, Product policy, registration and login contract pairs, and affected Identity source/design requirements. Preserve DEC-0005 acceptance history and DEC-0009 Proposed status and prior evidence until their lifecycle dispositions are separately recorded.

## References

- [DEC-0005](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0009](DEC-0009-customer-login-idna2008-implementation-strategy.md)
- [UTS #46 revision 36](https://www.unicode.org/reports/tr46/tr46-36.html)
- [ICU public IDNA API](https://unicode-org.github.io/icu-docs/apidoc/released/icu4j/com/ibm/icu/text/IDNA.html)

## Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.1.0 | 2026-11-26 | Proposed | Recorded a candidate profile, bounded qualification evidence and a pending corrective authorization. |
