# DEC-0048 — Customer Login Email Canonicalization Profile

- **Identifier:** DEC-0048
- **Title:** Customer Login Email Canonicalization Profile
- **Type:** Product Decision / Security Decision / Data Decision
- **Status:** Proposed
- **Version:** 0.2.1
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

The [qualification evidence](evidence/DEC-0048/product-02b-registration-mechanism-qualification.md) records **PASS for the demonstrated application-profile scope**. The corrective run evaluated 6,396 non-transitional ToASCII vector rows. It reconstructed 68 A4_2/root-dot differences, found zero unexplained differences in the comparison, and rejected all 68 under the application profile. The requester’s explicit one-run authorization in their stated founder capacity is consumed. Full UTS #46 conformance, dependency admission and retained-identifier compatibility remain unestablished.

Recommend the tested whole-domain profile for adoption. Use the public ICU4J 79.1 API with explicit `NONTRANSITIONAL_TO_ASCII`, `NONTRANSITIONAL_TO_UNICODE`, `USE_STD3_RULES`, `CHECK_BIDI`, `CHECK_CONTEXTJ` and `CHECK_CONTEXTO`. Preserve the existing 3–320 character HTTP input bound. Propose canonical limits of 64 ASCII octets for the local part, 63 per label, 253 for the rootless domain and 254 for the complete address. Reject a trailing root dot after conversion, including one produced by mapping, before constructing a comparison key.

## Approval and transition conditions

Preserve one retained authoritative owner per canonical key. Obtain an authoritative inventory including inactive and retained ownership before cutover, then classify invalid records and collisions without writes. Resolve conflicts with accountable disposition and no automatic merge, reassignment or release. Neither synthetic database emptiness nor a profile-version column establishes ownership compatibility.

The [DISP-01 accountable response](evidence/DEC-0048/product-02b-registration-mechanism-qualification.md#accountable-response-recorded-2026-11-27) confirms the qualified profile internally, subject to applicable concurrence and lawful synchronization. Effective adoption and supersession remain pending. Represent Identity, Product, Security, Architecture, Customer and applicable Data/Privacy authority in the synchronized adoption record. Controlled synthetic implementation requires effective profile adoption, exact dependency admission for the implementation scope, and explicit implementation authorization. Operational lookup or writes under the replacement profile require the retained-owner cutover review. Engineering qualification and DEC-0001 exact dependency admission remain separate. Require artifact provenance, license and notice review, including the distributed artifact, before admission. If accepted, synchronize the canonical index, Product policy, registration and login contract pairs, and affected Identity source/design requirements. See the [remaining decisions](evidence/DEC-0048/product-02b-registration-mechanism-qualification.md#remaining-decisions-for-controlled-registration) for the proposed separation between synthetic implementation and retained-owner cutover. Preserve DEC-0005 acceptance history and DEC-0009 Proposed status and prior evidence until their lifecycle dispositions are separately recorded.

## References

- [DEC-0005](DEC-0005-customer-login-email-comparison-uniqueness-lifecycle-semantics.md)
- [DEC-0009](DEC-0009-customer-login-idna2008-implementation-strategy.md)
- [UTS #46 revision 36](https://www.unicode.org/reports/tr46/tr46-36.html)
- [ICU public IDNA API](https://unicode-org.github.io/icu-docs/apidoc/released/icu4j/com/ibm/icu/text/IDNA.html)

## Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 0.2.1 | 2026-11-27 | Proposed | Reconciled the internal DISP-01 confirmation with the outstanding concurrence, adoption and transition gates. |
| 0.2.0 | 2026-11-27 | Proposed | Recorded scoped corrective PASS and proposed adoption conditions. |
| 0.1.0 | 2026-11-26 | Proposed | Recorded a candidate profile, bounded qualification evidence and a pending corrective authorization. |
