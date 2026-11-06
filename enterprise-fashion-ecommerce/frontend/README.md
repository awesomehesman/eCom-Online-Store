# Storefront foundation (FE-M1)

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

The root shell provides semantic header/main regions and a keyboard-visible skip
link. The root route lazily loads a truthful storefront introduction. Unsupported
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
bundle dependency. The production-only audit reports zero vulnerabilities. Do not use `npm audit fix --force` to bypass the Angular 20
baseline. No runtime secret or environment configuration is required.

Governing references: `../.ai/core/ARCHITECTURE.md`,
`../.ai/core/DESIGN-SYSTEM.md`, `../.ai/frontend/ANGULAR.md`,
`../.ai/frontend/ACCESSIBILITY.md`,
`../specifications/frontend/shared/frontend-baseline.md` and
`../specifications/frontend/storefront/storefront-shell-content.md`.
