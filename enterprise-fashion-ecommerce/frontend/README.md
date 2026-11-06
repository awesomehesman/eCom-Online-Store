# Storefront foundation (FE-M1 / FE-M2 / FE-M3)

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
