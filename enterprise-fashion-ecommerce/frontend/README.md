# Storefront foundation (FE-M1 / FE-M2 / FE-M3 / FE-M4 / FE-M5)

Angular 20, strict TypeScript and standalone components. Run these commands from
`enterprise-fashion-ecommerce/frontend/`.

## Prerequisites and commands

Use Node.js `^20.19.0`, `^22.12.0` or `^24.0.0` and npm (validated with Node
20.19.4 and npm 10.8.2). Tests require a locally installed Chrome/Chromium.

```sh
npm ci --registry=https://registry.npmjs.org/ --ignore-scripts
npm start
npm test
npm run typecheck
npm run build
```

Development serves at `http://127.0.0.1:4200`. Production output is in
`dist/storefront/browser/`. `npm ci` installs the retained lockfile; the explicit
registry avoids dependence on a developer's CodeArtifact setup without changing
that setup. Installation does not require dependency lifecycle scripts.

If Chrome is not discovered automatically, set `CHROME_BIN` to its executable.
For example, on macOS:

```sh
CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' npm test
```

## Implementation scope

The standalone StorefrontShell owns semantic header/navigation/main regions and a
keyboard-visible skip link. App owns route composition; route components own page
content. The sole navigation destination is the established storefront entry, with
`aria-current="page"` only when that entry is active. The root route lazily loads a truthful storefront introduction. Unsupported
addresses receive a distinct page with a return link. Route titles and main-region
focus communicate navigation context. There are no fabricated catalogue, Price,
Stock, Category, promotional, customer or CMS representations. No backend requests
or browser storage are used.

Neutral CSS values serve local usability only; they establish no canonical brand,
design tokens, UI library or Product policy. Client rendering is a constraint of
this increment, not a repository rendering Architecture decision. SSR,
prerendering, service workers, hosting, analytics, authentication, authorization,
mutations and operational commerce are outside scope.

## Testing and dependency boundaries

Karma/Jasmine with headless Chrome is the implementation-local runner choice for
FE-M1, not a repository-wide standard. Angular 20's established Karma builder is
used rather than its experimental Vitest builder. Its default reporter/coverage
plugins are installed because the builder loads them, even without coverage mode.
Tests exercise real Angular bootstrap, lazy Router entry, route titles, semantic
landmarks, focus and absence of unsupported commerce content. They do not certify
WCAG conformance; keyboard, screen-reader, reflow and zoom review remain necessary
before customer release.

Dependencies are limited to Angular runtime/Router, RxJS, tslib, Zone.js and
Angular build/compiler/testing tooling. Exact direct versions and integrity hashes
are recorded in `package.json` and `package-lock.json`. No third-party UI or state
library is installed. Review dependency advisories with:

```sh
npm audit --registry=https://registry.npmjs.org/
npm audit --omit=dev --registry=https://registry.npmjs.org/
```

The initial audit reports moderate advisories in transitive CLI tooling (glob and
minimatch); npm's suggested automatic fix changes the Angular major version and is
not applied. These are an unresolved tooling dependency issue, not a production
bundle dependency. The production-only audit reports zero vulnerabilities. Do not
use `npm audit fix --force` to bypass the Angular 20 baseline. No runtime secret or environment configuration is required.

Governing references: `../.ai/core/ARCHITECTURE.md`,
`../.ai/core/DESIGN-SYSTEM.md`, `../.ai/frontend/ANGULAR.md`,
`../.ai/frontend/ACCESSIBILITY.md`,
`../specifications/frontend/shared/frontend-baseline.md` and
`../specifications/frontend/storefront/storefront-shell-content.md`.

## FE-M2 bounded accessibility verification — 2026-11-06

Automated verification used Chrome Headless 157 on macOS, Node 20.19.4 and the
unchanged Karma/Jasmine setup. Eight Angular tests passed. Strict compilation and
the production build passed. No dependency or lockfile changes were required.

Run the additional checks from this directory after building:

```sh
npm run build
CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' node scripts/verify-accessibility.mjs
```

The script uses Node built-ins and Chrome's DevTools pipe, starts an ephemeral
loopback-only static server with local SPA fallback, and cleans up its temporary
browser profile. Set `CHROME_BIN` on other platforms; otherwise it tries
`google-chrome`. This local test server is not a hosting configuration. Missing
Chrome, failed assertions or browser timeouts make the command fail.

| Automated evidence | Result and boundary |
| --- | --- |
| Landmarks and headings | One main region and one page h1; named storefront navigation; same shell survives route changes. Supports bounded review of 1.3.1 and 2.4.6. |
| Current location | Home link has `aria-current="page"` on the entry, absent on not-found. No unsupported destination added. |
| Keyboard and bypass | Real browser Tab/Enter activates the visible skip link and focuses main. Tab/Shift+Tab preserves link order; Enter returns from not-found. Supports bounded review of 2.1.1, 2.1.2 and 2.4.1. |
| Navigation context | Both route titles are checked; recovery puts focus on main and restores the storefront title. Supports bounded review of 2.4.2 and 2.4.3. |
| Focus appearance | Focused skip link has an outline and lies within the viewport. This is partial evidence for 2.4.7 and 2.4.11, not visual validation of every state. |
| Responsive stress | Both routes at 320 and 1280 CSS pixels retain main/h1 structure with no horizontal page overflow or out-of-width content bounds, including 200% root text size, long unbroken headings, line-height 1.5, paragraph spacing 2em, letter spacing .12em and word spacing .16em. These are test conditions, not canonical breakpoints or design tokens. |
| Truthfulness | Tests preserve unavailable browsing/purchasing text and absence of products, prices, stock, customer state and commerce controls. |

The long text is injected only into the test browser; it is not application
content. Width/text-size stress provides partial evidence for 1.4.4, 1.4.10 and
1.4.12. It does not simulate or prove actual browser zoom at 200%/400%, all text
spacing combinations, absence of every overlap, or every device/orientation.

**Manual verification performed: none.** No screen-reader or visual accessibility
sign-off is claimed. The following applicable WCAG 2.2 AA checks remain unverified
or only partially covered and require human review before customer release:

- 1.3.1, 1.3.2 and 4.1.2: screen-reader landmarks, reading order, link names,
  current-page announcement and semantics across supported browser/AT combinations.
- 1.4.3 and 1.4.11: measured text/non-text contrast and focus appearance, including
  forced-colors/high-contrast settings.
- 1.4.4, 1.4.10 and 1.4.12: actual browser zoom, reflow, full user text-spacing
  overrides and visual clipping/overlap checks.
- 1.3.4 and 2.5.8: physical orientation and target-size review on supported devices.
- 2.1.1, 2.1.2, 2.4.1–2.4.3, 2.4.6, 2.4.7 and 2.4.11: complete manual keyboard,
  focus visibility/non-obscuration, bypass and announcement review on both routes.
- 3.1.1, 3.2.1 and 3.2.3: language announcement and predictable focus/navigation
  behavior with assistive technology. The document declares English, but AT output
  has not been reviewed.

This is a scoped verification record, not an exhaustive WCAG applicability audit
or certification. Media, forms, authentication, payments and other commerce
journeys are absent; their criteria must be assessed if those capabilities arrive.

## FE-M3 presentation states — 2026-11-06

This increment implements REQ-FEB-014–018, 020–021, 036–038 and the applicable
REQ-FSC-012, 028–031 boundaries without introducing business state or a store.
`StorefrontPageStateComponent` reuses heading/description presentation for three
existing page outcomes; it accepts only their typed presentation names. Recovery
links remain owned by their route or the persistent shell.

| Applicable state | Evidence and presentation |
| --- | --- |
| Commerce unavailable | Static implementation scope, not an empty catalogue query. Existing welcome and unavailable browsing/purchasing wording is preserved. |
| Invalid destination | The wildcard route reports an unsupported address and provides the established return link. It does not establish protected-resource existence. |
| Navigation loading | Actual Angular NavigationStart/termination events drive a local signal. A mounted polite `role="status"` region announces opening a page without moving focus for loading alone. |
| Navigation failed | NavigationError produces a safe message and `role="alert"`; exception details are never rendered. Previous route content is hidden, focus returns to main, and persistent home navigation remains operable. |
| Bootstrap failed | The existing plain bootstrap-failure alert remains separate from route recovery; application bootstrap cannot be repaired by a component that did not start. |

The local navigation signal has only ready/loading/failed values; ready means no
pending navigation, not a confirmed business success. Cancellation clears loading
without inventing failure or backend denial. Angular Router owns cancellation and
supersession; no custom task scheduler, retry mechanism or state library is added.
A failed route is not converted into the wildcard page or unavailable commerce.
The home link performs normal Angular navigation (or reveals the previous home
page when navigation to the same current URL is skipped); there is no fake retry,
automatic retry or simulated backend response. If the entry's own assets cannot
load, successful recovery cannot be promised.

There is no optional CMS/media/content loader in the current application. None is
introduced just to demonstrate degradation. Shared navigation remains outside the
route failure presentation, so a failed route cannot remove the established entry.
A failure of the entire runtime is outside this local containment boundary.

**Deferred, not represented as successful or empty:** confirmed catalogue emptiness,
filtered results, backend failures/denials, stale/partial Domain evidence, mutation
pending/unknown outcomes, commerce success and governed backend retry/reconciliation.
These require real capabilities and owning-Domain Contracts first. The absence of
commerce evidence establishes none of them. No new governance policy is selected.

Verification uses the commands above: `npm run typecheck`, `npm test`,
`npm run build`, and the existing `scripts/verify-accessibility.mjs` Chrome check.
Final results: 12 Angular tests passed, strict compilation and production build
passed, and all existing bounded Chrome accessibility checks passed.
The Angular suite now includes controlled client-side lazy loaders (not mock
commerce APIs) to verify pending, failure, cancellation, supersession, safe error
text and home recovery, alongside FE-M1/FE-M2 regressions. The browser check still
covers native keyboard navigation, route focus, recovery and responsive stress.

Live-region markup and update behavior are automated evidence only. Actual
screen-reader timing, interruption/duplicate-announcement behavior, failure-state
visual review and the previously listed manual WCAG checks remain unverified.
The existing tooling advisories remain unresolved; dependencies and lockfile are
unchanged. No operational, commercial, Identity or publication authority is added.

## FE-M4 navigation resilience — 2026-11-06

FE-M4 preserves the existing two routes and Angular Router history ownership.
Focus now runs once after a successful navigation has settled and Angular has
rendered its page context/title, including reuse of the wildcard component.
Outlet activation no longer moves focus early. Cancellation preserves user focus;
redirects do not introduce intermediate focus changes. Failures receive a truthful
page title and retain the failure presentation during recovery loading or
cancellation. Successful same-URL home recovery restores the route's normal title
and main focus. No additional live regions were added; the existing polite loading
and alert messages are not populated concurrently.

| Applicable requirement | Implementation and evidence |
| --- | --- |
| REQ-FEB-009–010; REQ-FSC-005, 011 | Angular Router and real browser history restore route context; no custom history store or replacement navigation framework. |
| REQ-FEB-014–017; REQ-FSC-012, 029–031 | Loading, failed and unsupported states remain distinct; cancelled recovery never reveals obsolete content. Late superseded results cannot override the newer page. |
| REQ-FEB-020–021 | Safe failure text and established home recovery; internal exceptions are not rendered, and no backend retry or response is invented. |
| REQ-FEB-035–038; REQ-FSC-033–035 | Settled main focus, route titles, one main landmark, page heading, current-location semantics, native keyboard links and existing status/alert regions. |

Verification commands remain:

```sh
npm run typecheck
CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' npm test
npm run build
CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' node scripts/verify-accessibility.mjs
```

Actual Chrome Headless 157 checks exercise native browser Back and Forward after
client-side not-found recovery. They assert heading, title, main focus,
`aria-current` and cleared status messages. Direct loads and real browser refresh
are checked at both `/` and `/missing`; existing Tab/Shift+Tab/Enter, skip-link,
recovery and responsive stress checks also run. The test server's SPA fallback is
local verification infrastructure only. Production deep-link serving remains a
future hosting responsibility, not a selected hosting configuration.

Angular component tests exercise initial/unsupported routes, loading, failed
navigation, normal and cancelled recovery, cancellation without focus theft,
settled wildcard reuse, one focus change across redirects, supersession and late
rejection of obsolete work. Extra routes/guards/loaders are isolated test fixtures;
they add no production destinations, authorization capability or commerce APIs.

Manual accessibility limitations from FE-M2/FE-M3 remain: browser tests do not prove
screen-reader title/focus announcements, all AT/browser combinations, visual focus
contrast/non-obscuration, actual zoom, touch targets or universal WCAG 2.2 AA
conformance. No manual accessibility sign-off is claimed. Commerce, Identity, CMS,
SSR, deployment, hosting and other deferred capabilities remain unimplemented.
No dependency, lockfile, branding or governance change is required for FE-M4.

Final FE-M4 results: **17 Angular tests passed**, strict typecheck passed,
production build passed, and all nine bounded browser verification groups passed
(including Back/Forward and both refresh scenarios). `git diff --check` passed.
No manual accessibility tests were executed.

## FE-M5 accessibility and responsive hardening — 2026-11-06

Bounded browser checks reproduced three defects and drove minimal corrections:

- The skip-link box was visible at the viewport edge while its external focus
  outline was clipped. Its existing outline is now inset inside its padded box.
- A literal fragment-only link resolved against `<base href="/">`, sending an
  unsupported address to home instead of bypassing within that page. The native
  anchor now uses the settled Router URL (including query context) and target
  fragment. A Router-to-Signal binding updates the OnPush shell; no key handler,
  additional route, custom history or business authority is introduced.
- A fragment-only Router completion could refocus main after native bypass had
  already focused it, stealing a subsequent Tab. Main focus now follows meaningful
  page changes and recovery; native fragment focus remains browser-owned.

| Applicable requirements | Bounded implementation/verification evidence |
| --- | --- |
| REQ-FEB-031, 033; REQ-FSC-032 | Both routes tested at 1280x900, 320x256, portrait 320x640 and landscape 640x320 CSS pixels, including orientation changes. No horizontal page overflow or out-of-width ordinary text/controls under spacing, enlarged text, long headings and wrapped navigation stress. |
| REQ-FEB-032, 035–036; REQ-FSC-033, 035 | Real Tab/Shift+Tab/Enter checks, native bypass and recovery, visible outlines, viewport containment and link hit-testing at the sampled text rectangles. Existing cancellation, supersession and settled-focus regressions remain. |
| REQ-FEB-034, 051; REQ-FSC-043 | Reproducible automated evidence only; no universal WCAG or manual review claim. |
| REQ-FEB-037; REQ-FSC-034, 036 | Chrome accessibility-tree checks for one exposed main, one level-one heading and nonempty exposed link names; document language, titles, target fragments and current-location semantics are also checked. |
| REQ-FEB-038; REQ-FSC-035–036 | Loading/failure live-region markup is mutually exclusive; loading does not steal focus. Hidden obsolete headings are not exposed. Actual static-asset failure and keyboard recovery are tested without inventing backend outcomes. |
| REQ-FEB-039–040 | Existing neutral styles and dependencies retained; no branding, token scale, UI framework or canonical breakpoint selected. |

The browser script holds and fails the real not-found JavaScript asset once in its
loopback test server. It observes actual route loading, releases a controlled 503,
checks safe failure markup/title/semantics and returns home using the keyboard.
This is frontend asset-failure injection, not a commerce API, operational response
or hosting decision. Long test headings and navigation labels are injected only
into browser fixtures and never enter application content. Each viewport reloads
its fixture, preventing accumulated styles/text from distorting later cases.
Keyboard assertions wait for fragment/popstate delivery and the next render before
sending the next action; they do not manually focus the expected target.

The 320x256 case is the **equivalent reflow viewport** for a 1280x1024 viewport at
400% zoom. It does not operate or verify Chrome's browser zoom control. Tests also
apply 200% root text size and WCAG 1.4.12 spacing conditions: line-height 1.5,
paragraph spacing 2em, letter spacing .12em and word spacing .16em. These are test
conditions, not Design System values. Emulated orientation is not physical-device
or sensor verification.

Final automated results (Chrome Headless 157, Node 20.19.4):

- `npm run typecheck`: PASS.
- `CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' npm test`:
  **19/19 PASS**, including new nested bypass/query and fragment-focus regressions,
  plus strengthened loading-focus, live-region exclusivity and exposed-heading assertions.
- `npm run build`: PASS.
- `CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' node scripts/verify-accessibility.mjs`:
  **14 browser verification groups PASS**, retaining history/refresh checks and
  adding real asset failure, accessibility-tree checks and eight responsive cases.
- Root `git diff --check`: PASS.

**Manual accessibility checks performed: none.** Automated geometry, hit-testing
and accessibility-tree assertions do not establish complete WCAG 2.2 AA conformance.
Manual screen-reader reading order, language/title/current-location announcements,
live-region timing/duplication (1.3.1, 2.4.2–2.4.3, 4.1.2–4.1.3), visual focus and
non-obscuration (2.4.7, 2.4.11), contrast/forced-colors (1.4.3, 1.4.11), actual browser
zoom/text-spacing visual review (1.4.4, 1.4.10, 1.4.12), physical orientation and
pointer/touch target review (1.3.4, 2.5.8), and other applicable manual checks remain
outstanding. Hit-testing samples link text rectangles; it is not pixel-level
validation of every outline or possible overlay.

No Product, Category, Search, Cart, Checkout, Identity or CMS capability is added.
Commerce evidence, backend Contracts, SSR, hosting/deployment, analytics, production
deep-link serving and canonical branding remain deferred. Existing development
advisories are not resolved by this increment. Dependencies and lockfile are unchanged.

## FE-M6 content integration readiness — 2026-11-06

**Outcome: existing local presentation is reusable; concrete CMS integration is
blocked pending a governed producer/consumer Contract.** This milestone adds this
readiness record only. It adds no runtime content boundary, API client, evidence
model, injection token, provider, cache, polling, background work, retry or new UI.
Creating those now would invent semantics not supplied by a concrete Contract.

### Implemented behavior reused

- `StorefrontShell` owns native navigation, bypass, current location and main focus.
  `App` contains real Router loading/failure and recovery while preserving the shell.
- `StorefrontPageStateComponent` presents only commerce-unavailable,
  invalid-destination and navigation-failed messages. It does not accept CMS content
  or establish a general CMS state model. Existing text is application-owned scope
  information, not a CMS-managed publication or notice.
- The entry does not query a catalogue or CMS. Its unavailable message establishes
  neither an empty catalogue nor absence of published CMS content.
- The current composition has no optional content work capable of delaying entry
  or navigation. That absence is not evidence that a future content adapter has
  fault isolation; future integration must verify that boundary explicitly.
- No route, query, local configuration or client state is interpreted as CMS
  publication, placement, destination visibility or access evidence.

### Governing evidence and ownership

The reviewed CMS Domain Specification is `1.0.0 Approved`, `authoritative: false`,
normative within its own scope. FEB and FSC are likewise Approved scoped sources;
none supplies a concrete CMS transport Contract. A bounded inspection of
`../specifications/contracts/` found no CMS-to-storefront Contract. This is not a
repository-wide implementation audit or an independent governance approval.

| Source | Readiness obligation; not a newly selected Contract |
| --- | --- |
| REQ-FEB-003–007; REQ-CMS-002–004, 035 | CMS retains source ownership; presentation/configuration/rendering never creates publication, placement or external Domain truth. |
| REQ-FSC-009–012, 014–018, 026–028; REQ-CMS-011, 013–014, 017–018, 049 | Future content needs explicit eligible publication and placement evidence. Draft, withdrawn or superseded content cannot become current through an existing reference, region or destination. Destination references do not prove existence, access or visibility. |
| REQ-FEB-014–018, 020–021; REQ-FSC-029–031; REQ-CMS-036, 040, 042–044 | Loading, unavailable, invalid, stale, partial, failed, recovery and confirmed outcomes apply only when a real source can establish them. Freshness, supersession and recovery cannot be guessed or collapsed into empty/success. |
| REQ-FEB-027–030, 034–038, 041–044; REQ-FSC-035–038; REQ-CMS-030–034 | Future content/link rendering needs safe interpretation, disclosure controls, accessible meaning and bounded workload isolation. The present app has no rich-content renderer or external destination consumer. |
| REQ-FEB-049–051; REQ-FSC-041–043; REQ-CMS-045, 051 | Abstract requirements justify necessary semantic questions, not invented endpoints, DTOs, wire formats, providers or conformance claims. |
| ARCHITECTURE.md §§36.5–36.6; ANGULAR.md §§32–34, 37–38 | Use governed application/API boundaries and keep authoritative server evidence separate from local presentation. No speculative client is introduced. |

### Missing concrete integration dependencies

Before a meaningful consumer can be implemented, the producer/consumer Contract
must settle the following within existing governance. These are unresolved
semantic questions, not a proposed schema, interface, endpoint or approval policy:

1. What versioned, bounded CMS-owned evidence identifies content, its relevant
   version, publication outcome and applicability to the requested presentation?
2. What CMS-owned placement evidence ties that eligible version and publication
   to the applicable context, without the consumer inventing placement taxonomy,
   ordering, targeting, merchandising or publication workflow?
3. How are current, stale, withdrawn, superseded, missing, invalid, conflicting,
   partial, unavailable and failed evidence distinguished where applicable? What
   source/freshness and revalidation evidence prevents stale copies being treated
   as current? No TTL, cache policy or timing guarantee is selected here.
4. What content/media/link forms may be safely interpreted, and how are source
   meaning, accessibility, provenance, bounded results and correlation preserved?
   No rich-text format, sanitization library or media provider is selected.
5. What governed destination and eligibility evidence permits a navigation entry,
   including safe handling of invalid, inaccessible or withdrawn destinations
   without disclosing protected Resources? A path or reference is insufficient.
6. What producer capability, version compatibility and safe recovery behavior can
   the consumer actually rely on? Optional-content work must remain independent
   of critical entry/navigation and obsolete asynchronous work must not overwrite
   newer intent. No retries, background calls or failure-result mapping are chosen.

CMS publication/placement ownership remains intact; Product, Category, Pricing,
Inventory, Identity and other Domains retain their own authority. CMS content
cannot provide commercial Price, availability, sellability or purchase authority.
This record selects no Open Product Decision or owner approval. The next bounded
step is to define and review the concrete producer/consumer Contract through
repository governance before implementing or testing a CMS consumer.

### Tested behavior versus documented readiness

The existing Angular and production-browser suites verify actual implemented
entry messaging, unsupported destinations, Router loading/failure/cancellation,
supersession, shell recovery, keyboard/focus behavior, semantics and responsive
stress. Their lazy-asset failure injection is not a CMS failure response or CMS
Contract test. No new tests are added for nonexistent content behavior.

**Not implemented or verified:** CMS publication/placement validation, cached CMS
freshness, withdrawn/stale/partial evidence handling, managed-content safety,
CMS request races, eligible external destinations, or optional CMS failure
containment. There are no CMS fixtures rendered as published content. Passing
existing regressions must not be described as CMS Contract conformance or live
integration readiness.

Manual screen-reader, visual accessibility, actual browser zoom and physical-device
checks remain outstanding as recorded in FE-M5. Future dynamic content will need
its own status/focus and accessibility evidence. Commerce journeys, managed banners,
campaigns, policy/support content, publication workflow, analytics, branding,
SSR and hosting remain deferred. No dependencies or runtime files are changed.

FE-M6 regression verification executed on 2026-11-06:

| Command | Actual result |
| --- | --- |
| `npm run typecheck` | PASS |
| `CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' npm test` | 19/19 PASS |
| `npm run build` | PASS |
| `CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome' node scripts/verify-accessibility.mjs` | All 14 bounded browser groups PASS |
| Repository-root `git diff --check` | PASS |

These results confirm the existing frontend regressions only, not CMS eligibility,
CMS Contract conformance, independent review or manual accessibility completion.

## Anonymous catalogue observation — 2026-11-06

The existing entry now requests `GET /api/v1/products` once on creation and on
an explicit **Check catalogue again** action. Native fetch uses `credentials:
'omit'`, `cache: 'no-store'` and rejects redirects. No response is persisted.
Only HTTP 200 JSON with the expected headers, exact page envelope, default page
size 20, empty items and null continuation confirms an empty eligible catalogue.
Failures, invalid JSON/metadata and unavailable authority never become empty
success. Non-empty envelopes are unsupported: Product bodies are neither
interpreted nor rendered, and no positive Product-schema compatibility is claimed.

Rechecks supersede/abort earlier requests; late responses and responses after
entry destruction cannot update the view. Catalogue status uses a mounted polite
live region, suppressed during route loading/failure to avoid competing shell
announcements. Rechecking preserves focus; shell landmarks, headings, bypass,
routes and history remain unchanged. Purchasing remains unavailable.

Applicable requirements: FEB-004–007, 014–021, 035–038, 049 and 051;
FCD-004/007 and source-Domain non-authority; FSC-019, 029–031, 035 and 043.
The Product catalogue companion §§4–5, 10–13 and its paired OpenAPI define the
consumed wire semantics. Contract eligibility for development does not establish
release approval.

`npm start` uses `proxy.conf.json` to reach a separately running local backend at
`http://127.0.0.1:8080`. This is development-only plumbing; it selects no production
host, CORS policy, deployment or reverse proxy. A missing/unconfigured backend
produces safe unavailability. No backend service, database or cursor secret is
started/configured by the frontend. Production needs a separately reviewed API
connection arrangement.

Verification: strict typecheck, 41 Angular tests and production build passed.
The Chrome production-build verifier passes 15 bounded groups. Its local HTTP
server supplies explicitly synthetic catalogue fixtures for loading, empty,
unavailable and unsupported responses; native keyboard recheck and focus are
verified. Request-option, malformed-response, navigation destruction and late
superseded-response checks run in the Angular suite. Fixtures prove consumer
handling, not Product publication, server correctness or live deployment.

**No real local backend smoke test was run.** Manual screen-reader announcement
review, visual accessibility review, actual browser zoom and physical-device
checks remain outstanding. Automated checks do not establish complete WCAG 2.2 AA
conformance. Product rendering/details, Search, Pricing, Inventory, Cart,
Checkout, Staff authorization and publication remain deferred. Producer/consumer
release review and operational connection verification remain required.

## Isolated Product summary — 2026-11-06

`ProductSummaryComponent` is a standalone OnPush presentation component with one
required signal input. Its narrow projection uses the catalogue Contract's `name`,
`description` and optional presentation of `family` (Apparel, Footwear, Accessories).
Family remains required in the complete OpenAPI Product; omitting it here only
omits summary context. This type is not a complete wire schema or runtime validator.
A future caller must supply separately validated, eligible Product evidence.

The component uses an article, an h3 intended beneath the catalogue h2, plain
Angular text interpolation and optional family text. HTML-like descriptions are
literal text. Local host styling provides block layout, minimum-size resilience
and wrapping without introducing branding, tokens or responsive breakpoints.
It adds no focus changes, live announcements, requests or persistence.

The component is deliberately not imported into production entry/routing or the
catalogue observation flow. Existing non-empty responses remain unsupported.
Live positive catalogue rendering is prohibited until separately authorized and
its validation, visibility, freshness and release-review prerequisites are met.
Prices, stock, purchasing eligibility, detail links, controls, Variant selection
and media are excluded.

Thirteen new Chrome-run unit tests use synthetic contract-shaped Product evidence
and presentation projections. They cover text/family, omitted family, long content,
literal HTML-like text, absence of interactive/media/commercial output, no fetch
or XMLHttpRequest calls, focus preservation, and real layout at 320/640 CSS pixels
with enlarged text and increased spacing. Enlarged text is not actual browser zoom
or physical-device orientation verification. Fixtures are test-only: no live Product
is created or published. The isolated component is not reachable by the existing
production browser verifier; adding a fixture route/build solely for it was avoided.

Verification: `npm run typecheck` passed; `npm test` with the configured Chrome
passed 56/56 tests; `npm run build` passed; the unchanged
`scripts/verify-accessibility.mjs` passed all 16 existing browser groups with
`CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'`.
The test compilation includes the isolated component; the application typecheck
and production build validate the existing application and do not imply live
component integration. Manual screen-reader, actual zoom and visual accessibility
review remain outstanding. No full WCAG conformance or production readiness is
claimed.

## Isolated Product media — 2026-11-07

The isolated summary now optionally accepts the wire-supported `mediaId`, `url`,
`alternativeText` and `variantId` fields. A valid parent `productId` is required
before media is rendered. Variant-associated media must identify exactly one
Variant in the supplied Product's `variants`; missing/foreign/ambiguous membership
is omitted. Product-level media has no `variantId`. Array order is retained; no
sorting, primary-image designation or Variant selection is invented. Duplicate
media identities are omitted rather than ambiguously re-associated.

Only absolute `https://` URLs with a parsed hostname and no userinfo, raw whitespace,
control characters or backslashes are rendered. Missing/blank alternative text
is omitted, not fabricated; valid supplied alternative text is preserved exactly
in `alt`. These defensive presentation checks cannot establish public accessibility,
usage rights, media-security acceptance, meaningfulness of source copy or absence
of private/sensitive URL content. Those remain upstream Contract obligations.
The narrow projection is still not a complete wire validator or publication grant.

Native images use no-referrer and asynchronous decoding. There are no new API,
fetch or XMLHttpRequest calls; browser image loading necessarily requests the
supplied media URL. The view reserves a local square frame with `object-fit:
contain`, without cropping or asserting intrinsic asset dimensions. This is local
layout containment, not a canonical Design System ratio or placeholder Product
image. Images stay hidden until loaded and are removed after error, leaving a
neutral “Product image unavailable.” caption in the reserved frame. Name,
description and other media remain intact. No automatic retry, focus change,
additional live region, link or control is introduced. Immutable replacement media
references create fresh loading state; obsolete image events do not affect them.

This extends the preceding text-only component increment, not the live catalogue
flow. Prices, Stock, purchasability, Product links, Cart and publication remain
excluded. The component is still unimported by production routes/entry; non-empty
live catalogue responses remain unsupported until separately authorized.

Verification uses contract-shaped synthetic Product/Variant/media fixtures plus
explicit malformed-input variations. Most component tests suppress image URL
assignments and dispatch events. Two additional Chrome tests intercept synthetic
HTTPS source assignments with deterministic local Blob URLs, preserving native
image decoding and load/error events without contacting external image hosts.
They verify successful decoding and visibility, failed-image removal and neutral
fallback, retained Product text and other imagery, unchanged frame height, narrow
text-spaced layout and unchanged keyboard focus. The failed image is hidden before
its error and absent afterward, avoiding a visible broken-image element.
Tests also cover association, input order, missing media, unsafe and credential-bearing
URLs, absent alternative text and replacement state. No production fixture route
or live catalogue integration was added.

Verification on 2026-11-07: `npm run typecheck` passed; `npm test` with
`CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'`
passed 79/79 tests; `npm run build` passed; the unchanged production
`scripts/verify-accessibility.mjs` passed all 16 browser groups; `git diff --check`
passed. Angular emits its collection-recreation warning in the immutable media
replacement test; replacement intentionally resets image loading state.
The production browser verifier covers storefront regressions, not this isolated
component. Blob interception does not establish real HTTPS delivery, provider
availability, rights or media-security acceptance. Manual screen-reader, actual
zoom and visual accessibility checks remain outstanding; no complete WCAG
conformance is claimed.

## Isolated catalogue page validation and collection — 2026-11-07

`validateCataloguePage(unknown)` is a pure, dependency-free JSON representation
boundary. It returns `null` for an unsupported/invalid page as a whole, or a
branded, detached and deeply frozen page. It preserves complete validated Product,
Variant and media fields; the existing summary consumes only its supported
presentation projection. No input is repaired, silently dropped or reordered.

Validation covers exact required/optional properties, field types, public IDs,
family, supported standard `1.0.0`, defining Size/Colour applicability, matching
Variant Attribute structures, family-compatible canonical sizes, initial BLACK
colour/Black label, unique Product/Variant/media identities and canonical Variant
configurations. Product IDs must follow the Contract's ascending ASCII/UTF-8 order;
media order is retained. Media must have nonblank alternatives, credential-free
HTTPS URLs and, when Variant-associated, membership in the same Product.
Nonblank names/descriptions/labels are required; size labels are retained, not
translated or inferred. Unknown governed extensions are rejected rather than
accepted as defaults.

Page sizes 1–100 and item-count bounds are validated. Empty pages require a null
cursor. Non-null cursors are syntax/length checked and preserved unchanged: no
cursor decoding, generation, requests, pagination controls or cross-page merging
are implemented. The isolated collection qualifies non-terminal pages without
claiming an exact total or complete catalogue. No HTTP headers or response status
are validated here: the existing anonymous observation boundary retains that
responsibility and remains unchanged.

`ProductSummaryListComponent` accepts only the branded validated page type. It
renders a semantic Products list under a caller-owned catalogue `h2`, reuses
summary `h3` headings and media failure containment, and tracks stable Product IDs.
It adds no focus movement, live regions, navigation, purchase controls or API calls.
No production entry/route imports, enablement flags or production fixtures were
introduced. Live non-empty responses remain unsupported in `StorefrontEntry`.

Contract structure cannot establish Product completeness, publication eligibility,
rights, meaningful alternative text or size-label accuracy, half-size applicability,
media-security acceptance, current visibility or freshness. These remain governed
producer obligations. Local URL checks cannot prove public availability or absence
of private storage metadata. Validation is not publication or release authority.

Synthetic tests cover valid families/pages/associations, malformed fields and
whole-page rejection, canonical uniqueness, preserved order, immutable snapshots,
collection semantics, literal text, narrow text-spaced layouts and focus. Native
Chrome image decoding uses deterministic Blob interception, not external media
hosts. A production-entry regression verifies that a valid positive response still
renders no Product content. Existing empty/unavailable, cancellation and stale
response tests remain in place. Applicable requirements include REQ-FCD-014–020,
023–024, 030–035 and 044, and REQ-FEB-031–038 and 051.

Live positive rendering, backend publication, pagination interaction, Product
links/details, Search, prices, Stock, Cart and Checkout remain deferred. Before live
integration, producer/consumer conformity, current eligibility/withdrawal evidence,
release review and real backend verification remain necessary. Manual screen-reader,
actual browser zoom and visual accessibility verification remain outstanding.

Verification for this increment: `npm run typecheck` passed; configured Chrome
`npm test` passed 168/168 tests (89 added); `npm run build` passed; the unchanged
`scripts/verify-accessibility.mjs` passed all 16 existing browser groups using
`CHROME_BIN='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'`.
`git diff --check` passed. The isolated files are compiled by the test build;
application typecheck/build verify the unchanged live application. Production
JavaScript inspection found neither the new collection selector/validator marker
nor the synthetic Product name. These checks establish no live positive backend
integration or complete WCAG conformance.
