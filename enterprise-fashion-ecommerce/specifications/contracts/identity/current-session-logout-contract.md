---
title: Current-Session Logout Contract
version: 1.0.0
status: Approved
owner: Identity
last_updated: 2026-10-21
authoritative: false
---

# Current-Session Logout Contract

## 1. Purpose and authority

This Approved Contract governs browser-facing current-session logout within the accepted Identity Session architecture. Accountable Contract approvals authorize the bounded implementation direction; they do not create a new Security Decision or Architecture Decision or claim an endpoint exists.

The paired [OpenAPI Contract](current-session-logout.openapi.yaml) defines approved wire behavior exclusively. It is the executable HTTP Contract under API.md §64.2. This companion defines workflow, ownership, lifecycle, uncertainty, rationale and review questions; it does not compete with OpenAPI for routes, methods, headers, schemas or response mappings.

Baseline reviewed: `develop` at `69175584842250cc860807687b7a2be236f4b478`.

## 2. Governing sources

- [API.md](../../../.ai/backend/API.md) §§9–10, 25–27, 64–65 and 79–80. The requested `.ai/core/API.md` does not exist; the governing API standard is in `.ai/backend/`.
- [DECISIONS.md](../../../.ai/core/DECISIONS.md) §§5–6 and 17: proportionate decision recording and attributable approval; no material architectural change is proposed here.
- [Identity backend specification](../../backend/identity/identity-backend.md), particularly BIDN-REQ/AC-020–022 and 029–030.
- Accepted [ADR-0019](../../adr/ADR-0019-authentication-session-token-strategy.md), Logout, Concurrent Devices and Sessions, and failure/concurrency requirements.
- Accepted [ADR-0020](../../adr/ADR-0020-identity-session-store-strategy.md), Identity-owned authoritative PostgreSQL/Spring Session JDBC persistence.

Higher-authority sources prevail. Approval introduces no exception to a mandatory control.

## 3. Request-context targeting and ownership

Identity owns termination. Derive the target exclusively from the current server-side request/session context. No arbitrary target Session, Customer, Account or Principal identifier is accepted. The existing browser credential is interpreted by the selected server-side Session infrastructure; possession alone establishes no broader authority.

Use no Session-creating accessor or renewal path. Preserve unrelated Sessions, Customer/Account state and all business Authorization boundaries. This capability terminates only the current Session; it does not implement global sign-out, compromise handling, login or registration.

## 4. Authoritative confirmation and uncertainty

For an identified current Session, reuse the existing revocation command and authoritative observation boundary. A completed deletion call alone is not confirmation. Only authoritative absence after successful deletion establishes the confirmed outcome. A failed deletion remains uncertain even if a later read could observe absence. Continued presence or uncertain confirmation is not success.

An absent-current-session outcome requires reliable evidence from the trusted request/session resolution path. Neither anonymous Authentication nor a null local Session proves absence if an upstream lookup failed, suppressed an error or left unresolved authority. Preserve lookup uncertainty through the response boundary. An expired authoritative record may establish no active Session under the selected store semantics; do not invent its historical termination cause.

The wire Contract distinguishes confirmed/reliably absent outcomes from uncertainty. No database or framework exception details cross the boundary.

## 5. Anonymous CSRF acquisition and validation

The approved acquisition and logout interactions are explicitly public with respect to Principal Authentication, with accountable public-endpoint review recorded under API.md §§25–27. Anonymous Authentication is not evidence of Session absence. Public access never exempts logout from CSRF validation.

Use `CookieCsrfTokenRepository`. Acquisition generates or loads CSRF evidence without HTTP Session access and exposes the XOR-masked request token produced by `XorCsrfTokenRequestAttributeHandler`. Validation compares the correctly decoded masked submission with the cookie-backed expected token. Never return the raw CSRF cookie value in the JSON body or treat CSRF evidence as an Authentication credential. The exact cookie/header names, attributes, wire schema and status mappings are defined exclusively in the paired OpenAPI document.

Restrict acquisition to trusted same-origin callers and do not relax CORS. Acquisition does not require a pre-existing token; logout does. Missing, invalid and mismatched logout CSRF evidence is rejected before target resolution or revocation. Reject disallowed acquisition origins before issuing a token. The shared fail-closed origin policy in §5.4 and the executable Contract defines acceptance and rejection. Implementation must enforce those rules, not select a different fallback or rely on CORS/SameSite alone.

The approved host-prefixed, host-only CSRF cookie is Secure, HttpOnly, scoped to the host root and SameSite Strict, with no Domain attribute. HTTPS delivery, correct trusted proxy/scheme handling, browser prefix enforcement, frontend/API same-origin deployment and cookie-scope compatibility must be verified. If deployment cannot meet these prerequisites, reassess the Contract; do not silently relax attributes or CORS. No persistent lifetime, token-expiry duration or new authentication cookie is selected.

## 5.1 Dedicated security integration and response completion

Use a higher-priority Spring Security chain matching only the two method/path interactions in OpenAPI. Use a request-attribute-only `SecurityContextRepository`, stateless session management, `NullRequestCache`, cookie-backed CSRF and anonymous access. Keep all other requests on the existing security baseline. Do not enable default logout handlers that access or invalidate `HttpSession`. Clear the current request's local security context explicitly during logout handling, including uncertainty, without persisting an empty context. Pre-handler CSRF rejection is not successful logout handling.

The outer Spring Session filter remains installed. Its existence is not evidence of safe behavior: implementation tests must prove the current-session wrapper is never populated along either interaction, including rejection, response flush/commit and error dispatch. Do not call servlet Session accessors, `SessionRepository.findById`, create/save operations or renewal paths. Use the same configured `HttpSessionIdResolver` as Spring Session to interpret the browser credential, and the existing authoritative Identity observation/revocation ports for database work. Authoritative SELECT-only observation is permitted; the prohibition on Session lookup concerns servlet/framework Session loading, not these necessary database observations.

Response completion must not save cached Session state after revocation. Do not forward processing into another session-loading chain or downstream component. These are approved implementation constraints, not tested guarantees. They prevent same-request persistence only; other concurrent requests remain a separate lifecycle concern.

## 5.2 Credential resolution and failure precedence

A reliable empty resolver result establishes no current credential for this request. One valid supported credential is passed to the existing `RevokeIdentitySession` command. Malformed or multiple ambiguous credentials are rejected; never silently select one or revoke all of them. Anonymous Authentication, an expired local representation or a null Session accessor cannot substitute for authoritative evidence. Resolver/infrastructure uncertainty remains uncertainty. Invalid references are rejected before persistence and are never echoed in errors.

For logout, evaluate origin first, mandatory CSRF second, request shape third, credential resolution fourth and authoritative revocation/confirmation last. For acquisition, evaluate origin, request shape, then token acquisition; no pre-existing CSRF token is required. Origin rejection never reaches token issuance or revocation. Every logout request passing origin validation remains subject to mandatory CSRF enforcement before content or credential checks. Earlier rejection performs no revocation and cannot be overwritten by later success. Session-resolution, deletion and confirmation infrastructure uncertainty remains distinct from malformed input.

### Request-validation matrix

The following applies identically to both operations after origin validation and, for logout, CSRF validation. Exact wire mappings remain owned by OpenAPI.

| Request condition | Result |
| --- | --- |
| Genuinely empty body, no Content-Type | Valid request shape; continue. |
| Genuinely empty body, Content-Type application/json | Valid request shape; ignore unused Content-Type. |
| Genuinely empty body, any other unused Content-Type | Valid request shape; ignore unused Content-Type. |
| Any non-empty JSON body, including JSON null | Forbidden content: 400 Problem Details. |
| Any non-empty body with any other Content-Type | Forbidden content: 400 Problem Details. |
| Whitespace-only non-empty body | Forbidden content: 400 Problem Details. |
| Unexpected query parameters | 400 Problem Details. |
| Client-supplied Session/Account target parameters | 400 Problem Details; never use them as a target. |
| Multiple request-shape violations | One safe 400 response; no target processing. |
| Origin rejection plus any lower-priority invalidity | 403; do not issue token or revoke. |
| Valid origin, invalid logout CSRF plus invalid shape | 403; do not revoke. |
| Valid origin/CSRF, invalid shape plus credential uncertainty | 400 before credential resolution; do not inspect authority or revoke. |

These bodyless endpoints support no request representation. API.md §31 recommends 415 only where applicable; no media-type negotiation of request content is applicable here. Therefore 415 is removed. This is not permission to parse any submitted body: all non-empty bodies are forbidden. Determine actual emptiness safely within existing resource controls rather than trusting Content-Length alone. Infrastructure failures while reading a request must not be disguised as bad content; unexpected failures retain the governing safe API error treatment.

## 5.3 Browser cleanup

Cookie expiry may accompany logout, using the actual configured Session cookie identity and scope shared with the selected infrastructure; do not invent or hard-code another authentication cookie. It may occur during uncertainty, but never establishes authoritative termination or changes the uncertain response into success. Cookie expiry must not cause Session creation, renewal or persistence. Local security-context clearing remains required independently of optional browser-cookie cleanup.

## 5.4 Shared fail-closed origin policy

Apply the same policy to both operations. Trust comes exclusively from an explicitly configured canonical public HTTPS application origin, with no userinfo, path, query or fragment. Never establish trust directly from arbitrary Host, Forwarded or X-Forwarded-* headers. Any necessary proxy metadata must come from configured trusted proxies and be validated. Missing, invalid or unreliable trusted-origin configuration fails closed with generic 403 and safe operational diagnostics; this is an origin-policy rejection, not a classification of Session infrastructure failure as invalid input.

Compare scheme, host and effective port exactly after standards-based case/default-port normalization. No wildcard, suffix, same-site or substring matching is permitted. Referer path/query does not establish trust and must not be echoed. The executable Contract's x-origin-validation field owns these wire acceptance rules.

| Signal | Required decision |
| --- | --- |
| One valid matching Origin | Eligible only if every other supplied signal is valid and consistent. |
| Different-origin Origin, Origin null, malformed/multiple/ambiguous Origin | Reject; no Referer fallback. |
| Origin absent, one valid matching Referer | Eligible only if Fetch Metadata does not conflict. |
| Origin and Referer both absent | Reject. |
| Supplied Referer cross-origin, malformed, multiple/ambiguous, or conflicting with Origin | Reject even when Origin matches. |
| Valid Referer | Must be an absolute HTTP(S) URL without userinfo or fragment; its origin must match. |
| Sec-Fetch-Site cross-site or same-site | Reject; same-site is not the required same origin. |
| Sec-Fetch-Site same-origin | Corroboration only; valid matching Origin or Referer remains required. |
| Sec-Fetch-Site none | Neutral; accepted only with independently valid matching Origin or Referer. |
| Sec-Fetch-Site absent | Allowed only if the Origin/Referer rule succeeds. |
| Malformed, duplicate, ambiguous or unknown Fetch Metadata value | Reject. |
| Trusted-origin configuration cannot be established reliably | Reject with generic origin-policy 403; never use request headers as fallback configuration. |

All rejection paths precede token issuance or revocation and disclose neither trusted configuration nor raw origin values/internal validation details. CORS, SameSite cookies, valid CSRF evidence and anonymous Authentication are not substitutes for this policy. The policy intentionally rejects callers lacking the required origin evidence; no compatibility bypass is implied.

## 6. Repetition, concurrency and privacy

Repeated requests use current authoritative state, preserve unrelated Sessions and create no Session. Reliably established absence is a successful no-op. Uncertainty remains distinguishable on retries; previous success is not cached proof of current state.

Confirmation is point-in-time evidence. This Contract does not claim prevention of every concurrent stale-session resurrection or completion of the full session-lifecycle concurrency requirement.

Do not deserialize or return Session attributes. Do not expose Session identifiers, credentials, protected Identity existence or infrastructure details in response bodies, diagnostic correlation, logs or examples. Governed monitoring and security-audit obligations remain applicable; this draft establishes no new retention period or audit infrastructure. The executable Contract defines safe response caching and correlation headers using existing API conventions.

## 7. Requirement traceability and acceptance evidence

This capability contributes partially to the following existing requirements; it does not complete their full scope:

| Requirement / acceptance criterion | Required capability evidence |
| --- | --- |
| BIDN-REQ-007 / BIDN-AC-007 | Explicit application orchestration; no bypass of authoritative command outcomes. |
| BIDN-REQ-020 / BIDN-AC-020 | Authoritative current-session termination or distinguishable uncertainty. |
| BIDN-REQ-021 / BIDN-AC-021 | Neither browser cleanup nor Session possession proves termination or business Authorization. |
| BIDN-REQ-022 / BIDN-AC-022 | Safe duplicate/retried work; unrelated Sessions preserved; concurrency limitations explicit. |
| BIDN-REQ-029 / BIDN-AC-029 | Workflow guidance separated from executable HTTP wire definitions. |
| BIDN-REQ-030 / BIDN-AC-030 | Public access review, mandatory CSRF, safe RFC 9457 responses and no secret disclosure. |
| BIDN-REQ-038–039 / BIDN-AC-038–039 | Lookup/deletion/confirmation failures remain uncertain; retries never fabricate success. |
| BIDN-REQ-048–049 / BIDN-AC-048–049 | Application, HTTP/security and PostgreSQL tests prove the bounded behavior. |

The following verification criteria are required before claiming implementation assurance:

1. Real anonymous acquisition using the cookie repository, with no pre-existing HTTP Session.
2. Submission of the actual XOR-masked token from acquisition; raw-cookie substitution is rejected.
3. Missing, invalid and mismatched CSRF evidence rejects before revocation, including overlapping invalid content.
4. No HTTP Session creation, framework lookup, renewal or save on acquisition, logout, rejection or uncertainty; authoritative Identity database observations remain allowed.
5. Credential resolution uses the shared configured resolver and distinguishes no credential, one valid credential, malformed input and infrastructure failure.
6. Ambiguous credentials are rejected without selecting or revoking a target.
7. Authoritative target revocation removes the target and its attributes through the existing command.
8. Other Sessions and attributes remain unchanged.
9. Every response mapping in the paired OpenAPI document, including all logout success/rejection/uncertainty mappings, and deterministic precedence is verified.
10. Flush, response commit, error handling and dispatch never introduce hidden Session persistence or overwrite uncertainty.
11. Same-request stale state cannot resurrect the revoked Session; no claim is made about every concurrent request.
12. Responses and logs contain no Session identifiers, raw CSRF cookie tokens, Authentication credentials or infrastructure details; the masked acquisition token is intentionally returned only in its governed response.
13. Cookie attributes, same-origin restrictions, cache controls and correlation handling match the executable Contract.
14. Origin-policy tests cover matching/cross-origin/opaque origins, missing Origin with matching Referer, both absent, conflicting signals, cross-site and same-site Fetch Metadata, none and missing Fetch Metadata, malformed/ambiguous headers, and trusted-origin configuration failure; reject before token issuance or revocation.
15. Exercise every row of both origin and request-validation matrices, including failure precedence and absence of any 415 branch.

Use the full servlet/Spring Security/Spring Session chain and PostgreSQL state assertions where persistence is material. A mock CSRF postprocessor alone cannot establish acquisition or presentation correctness. These are required future tests, not completed evidence.

## 8. Review and approval boundary

The completed feasibility assessment supports these approved choices through resolved Spring Security 6.5.11 and Spring Session 3.5.7 source behavior. Cookie-backed token storage avoids HTTP Session access; the XOR handler distinguishes masked request tokens from raw cookie tokens; the outer Session filter may save a populated wrapper at response completion. The dedicated chain is technically feasible but has not been integration-tested. No runtime assurance is claimed.

On 2026-10-21, the repository owner explicitly confirmed the following accountable approvals for the complete paired Contract, public interactions, CSRF acquisition/presentation, cookie settings, same-origin enforcement, credential rejection and response precedence:

| Approving capacity | Approver | Decision | Decision date |
| --- | --- | --- | --- |
| Identity Owner / Engineering | Repository owner / sole maintainer | APPROVED | 2026-10-21 |
| Accountable Security | Repository owner / sole maintainer | APPROVED | 2026-10-21 |

The same sole maintainer performs these separately recorded capacities. This is attributable owner approval, not independent human review. Codex prepared this record from the owner's explicit confirmation; Codex does not grant either approval. Approval covers the documented Contract, not completed runtime integration tests, verified deployment compatibility, production or release readiness. Deployment prerequisites and full-chain non-persistence evidence must be established before relying on implementation. Applicable Architecture review remains required if accepted boundaries change.

This Contract selects no new store, dependency, Authentication mechanism, cryptography or email verification. No new DEC or ADR is created. The recorded approvals permit implementation within this bounded Contract. All documented verification obligations, deployment prerequisites and concurrency limitations remain in force; implementation success and release readiness require their own evidence.

## 9. Revision history

| Version | Date | Status | Summary |
| --- | --- | --- | --- |
| 1.0.0 | 2026-10-21 | Approved | Recorded the repository owner’s confirmed Identity Owner / Engineering and Accountable Security approvals in separately recorded capacities; preserved API behavior and outstanding implementation/deployment verification obligations; no independent human review claimed. |
| 0.3.0 | 2026-10-21 | Draft | Resolved origin-validation and bodyless request-classification ambiguities; specified shared fail-closed signal rules and deterministic precedence; removed unreachable 415 responses. |
| 0.2.0 | 2026-10-21 | Draft | Recorded proposed anonymous masked-token acquisition, cookie/header settings, deterministic rejection precedence, credential resolution, dedicated non-persisting security integration, deployment prerequisites and required regression evidence. |
| 0.1.0 | 2026-10-21 | Draft | Proposed bounded current-session logout wire and workflow Contracts for accountable review, preserving mandatory CSRF, authoritative confirmation, uncertainty and existing Session ownership. |
