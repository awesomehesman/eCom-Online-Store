// Bounded real-browser checks using Chrome's DevTools pipe; no added dependencies.
import { spawn } from 'node:child_process';
import { createServer } from 'node:http';
import { mkdtemp, readFile, readdir, rm } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import { join, resolve, extname } from 'node:path';
import assert from 'node:assert/strict';

const root = resolve('dist/storefront/browser');
let assetFailure;
const server = createServer(async (req, res) => {
  try {
    const pathname = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
    const path = resolve(root, '.' + pathname);
    if (!path.startsWith(root + '/') && path !== root) { res.writeHead(403).end(); return; }
    // Local SPA fallback only; this does not select a hosting configuration.
    const file = extname(path) ? path : join(root, 'index.html');
    if (assetFailure && file === assetFailure.file) {
      const failure = assetFailure;
      failure.started();
      await failure.release;
      assetFailure = undefined;
      res.writeHead(503).end('test-only asset failure detail');
      return;
    }
    const data = await readFile(file);
    res.setHeader('Content-Type', ({ '.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css' })[extname(file)] ?? 'application/octet-stream');
    res.end(data);
  } catch { res.writeHead(404).end(); }
});
await new Promise(resolve => server.listen(0, '127.0.0.1', resolve));
const base = `http://127.0.0.1:${server.address().port}`;
const profile = await mkdtemp(join(tmpdir(), 'storefront-a11y-'));
const chrome = spawn(process.env.CHROME_BIN || 'google-chrome', [
  '--headless', '--disable-gpu', '--no-first-run', '--remote-debugging-pipe',
  `--user-data-dir=${profile}`, 'about:blank',
], { stdio: ['ignore', 'ignore', 'ignore', 'pipe', 'pipe'] });
let sequence = 0;
let buffer = '';
const pending = new Map();
const eventWaiters = new Map();
chrome.on('error', error => { for (const p of pending.values()) p.reject(error); });
chrome.stdio[4].on('data', chunk => {
  buffer += chunk.toString();
  let end;
  while ((end = buffer.indexOf('\0')) >= 0) {
    const message = JSON.parse(buffer.slice(0, end)); buffer = buffer.slice(end + 1);
    const waiter = eventWaiters.get(message.method);
    if (waiter && message.sessionId === session) {
      eventWaiters.delete(message.method);
      waiter();
    }
    const request = pending.get(message.id);
    if (request) {
      pending.delete(message.id);
      message.error ? request.reject(new Error(JSON.stringify(message.error))) : request.resolve(message.result);
    }
  }
});
function command(method, params = {}, sessionId) {
  return new Promise((resolve, reject) => {
    const id = ++sequence;
    const timer = setTimeout(() => { pending.delete(id); reject(new Error(`Timed out: ${method}`)); }, 15000);
    pending.set(id, { resolve: value => { clearTimeout(timer); resolve(value); }, reject: error => { clearTimeout(timer); reject(error); } });
    chrome.stdio[3].write(JSON.stringify({ id, method, params, sessionId }) + '\0');
  });
}
let session;
async function evaluate(expression) {
  const result = await command('Runtime.evaluate', { expression, returnByValue: true, awaitPromise: true }, session);
  assert.equal(result.exceptionDetails, undefined, JSON.stringify(result.exceptionDetails));
  return result.result.value;
}
async function ready(heading) {
  await evaluate(`new Promise((resolve, reject) => {
    const matches = () => document.querySelector('h1')?.textContent === ${JSON.stringify(heading)};
    if (matches()) { resolve(true); return; }
    const observer = new MutationObserver(() => { if (matches()) { observer.disconnect(); clearTimeout(timer); resolve(true); } });
    const timer = setTimeout(() => { observer.disconnect(); reject(new Error('Route did not render: ' + location.pathname + ' active=' + document.activeElement.tagName + ' heading=' + document.querySelector('h1')?.textContent)); }, 10000);
    observer.observe(document, { subtree: true, childList: true, characterData: true });
  })`);
}
function nextEvent(method) {
  return new Promise((resolve, reject) => {
    const timer = setTimeout(() => { eventWaiters.delete(method); reject(new Error(`Timed out: ${method}`)); }, 15000);
    eventWaiters.set(method, () => { clearTimeout(timer); resolve(); });
  });
}
async function checkContext(heading, title, current) {
  await ready(heading);
  assert.equal(await evaluate('document.title'), title);
  assert.equal(await evaluate('document.activeElement.id'), 'main-content');
  assert.equal(await evaluate('document.querySelectorAll("main").length'), 1);
  assert.equal(await evaluate('document.querySelector("nav a").getAttribute("aria-current")'), current);
  assert.equal(await evaluate('document.querySelector("[role=status]").textContent'), '');
  assert.equal(await evaluate('document.querySelector("[role=alert]").textContent'), '');
}
async function key(key, code, virtualKey, modifiers = 0) {
  for (const type of ['keyDown', 'keyUp']) {
    await command('Input.dispatchKeyEvent', { type, key, code, windowsVirtualKeyCode: virtualKey, modifiers }, session);
  }
}
async function waitFor(condition) {
  await evaluate(`new Promise((resolve, reject) => {
    const deadline = performance.now() + 10000;
    function check() {
      if (${condition}) resolve(true);
      else if (performance.now() > deadline) reject(new Error('Expected browser state was not observed'));
      else requestAnimationFrame(check);
    }
    check();
  })`);
}
async function checkSemantics() {
  const { nodes } = await command('Accessibility.getFullAXTree', {}, session);
  const exposed = nodes.filter(node => !node.ignored);
  assert.equal(exposed.filter(node => node.role?.value === 'main').length, 1);
  assert.equal(exposed.filter(node => node.role?.value === 'heading' && node.properties?.some(p => p.name === 'level' && p.value.value === 1)).length, 1);
  assert.ok(exposed.filter(node => node.role?.value === 'link').every(node => node.name?.value?.trim()));
  assert.equal(await evaluate(`document.documentElement.lang`), 'en');
  assert.equal(await evaluate(`(() => { const link = document.querySelector('a.skip-link'); return link.hash === '#main-content' && link.pathname === location.pathname && link.search === location.search; })()`), true);
  assert.equal(await evaluate(`document.querySelectorAll('a.skip-link').length`), 1);
  assert.equal(await evaluate(`[...document.querySelectorAll('[role="status"], [role="alert"]')].filter(el => el.textContent.trim()).length <= 1`), true);
}
async function checkFocusedLink() {
  const result = await evaluate(`(() => {
    const el = document.activeElement;
    const s = getComputedStyle(el);
    const r = el.getBoundingClientRect();
    const ring = Math.max(0, parseFloat(s.outlineWidth) + parseFloat(s.outlineOffset));
    const points = [...el.getClientRects()].map(rect => [rect.left + rect.width / 2, rect.top + rect.height / 2]);
    return {
      link: el.tagName === 'A',
      outline: s.outlineStyle !== 'none' && parseFloat(s.outlineWidth) > 0,
      contained: r.left - ring >= 0 && r.top - ring >= 0 && r.right + ring <= innerWidth && r.bottom + ring <= innerHeight,
      unobscured: points.every(([x, y]) => el.contains(document.elementFromPoint(x, y))),
      text: el.textContent.trim(),
      bounds: { left: r.left, top: r.top, right: r.right, bottom: r.bottom, ring, width: innerWidth, height: innerHeight },
    };
  })()`);
  assert.equal(result.link, true, JSON.stringify(result));
  assert.equal(result.outline, true, JSON.stringify(result));
  assert.equal(result.contained, true, `Focus outline clipped: ${JSON.stringify(result)}`);
  assert.equal(result.unobscured, true, `Focused link obscured: ${JSON.stringify(result)}`);
}
try {
  const { targetId } = await command('Target.createTarget', { url: 'about:blank' });
  ({ sessionId: session } = await command('Target.attachToTarget', { targetId, flatten: true }));
  await command('Page.enable', {}, session);
  await command('Page.navigate', { url: base }, session);
  await ready('Welcome to the storefront');
  assert.equal(await evaluate(`document.querySelectorAll('main').length`), 1);
  assert.equal(await evaluate(`document.querySelector('nav a').getAttribute('aria-current')`), 'page');

  // Start keyboard traversal at the document, without fabricating keyboard DOM events.
  await evaluate(`document.body.tabIndex = -1; document.body.focus(); document.body.removeAttribute('tabindex');`);
  await key('Tab', 'Tab', 9);
  assert.equal(await evaluate('document.activeElement.className'), 'skip-link');
  await checkFocusedLink();
  assert.equal(await evaluate(`(() => { const r = document.activeElement.getBoundingClientRect(); const s = getComputedStyle(document.activeElement); return r.top >= 0 && r.bottom <= innerHeight && s.outlineStyle !== 'none'; })()`), true);
  await key('Enter', 'Enter', 13);
  assert.equal(await evaluate('document.activeElement.id'), 'main-content');
  console.log('PASS: native Tab/Enter bypass navigation and visible focus');

  await command('Page.navigate', { url: base + '/missing' }, session);
  await ready('Page not available');
  assert.equal(await evaluate(`document.querySelector('nav a').hasAttribute('aria-current')`), false);
  assert.equal(await evaluate('document.activeElement.id'), 'main-content');
  await key('Tab', 'Tab', 9);
  assert.equal(await evaluate('document.activeElement.textContent.trim()'), 'Return to the storefront');
  await key('Tab', 'Tab', 9, 8); // Shift+Tab returns to the header entry.
  assert.equal(await evaluate('document.activeElement.textContent.trim()'), 'Storefront');
  await key('Tab', 'Tab', 9);
  await key('Enter', 'Enter', 13);
  await ready('Welcome to the storefront');
  assert.equal(await evaluate('document.activeElement.id'), 'main-content');
  assert.equal(await evaluate('document.title'), 'Storefront');
  console.log('PASS: not-found context, forward/reverse keyboard order, Enter recovery and route focus');

  // Real browser history, not a mocked Location or Router event stream.
  await evaluate('history.back()');
  await checkContext('Page not available', 'Page not available', null);
  await evaluate('history.forward()');
  await checkContext('Welcome to the storefront', 'Storefront', 'page');
  console.log('PASS: browser Back/Forward restore route, title, main focus and current location');

  for (const path of ['/', '/missing']) {
    await command('Page.navigate', { url: base + path }, session);
    const heading = path === '/' ? 'Welcome to the storefront' : 'Page not available';
    const title = path === '/' ? 'Storefront' : 'Page not available';
    const current = path === '/' ? 'page' : null;
    await checkContext(heading, title, current);
    const loaded = nextEvent('Page.loadEventFired');
    await command('Page.reload', { ignoreCache: true }, session);
    await loaded;
    await checkContext(heading, title, current);
    console.log(`PASS: direct navigation and browser refresh at ${path}`);
  }

  // Fault a real lazy JS asset, not a backend API or a fabricated commerce response.
  let failedChunk;
  for (const name of await readdir(root)) {
    if (name.endsWith('.js') && (await readFile(join(root, name), 'utf8')).includes('app-page-not-found')) failedChunk = join(root, name);
  }
  assert.ok(failedChunk, 'The not-found lazy asset must exist');
  let started;
  let release;
  const requested = new Promise(resolve => { started = resolve; });
  assetFailure = { file: failedChunk, started, release: new Promise(resolve => { release = resolve; }) };
  await command('Network.enable', {}, session);
  await command('Network.setCacheDisabled', { cacheDisabled: true }, session);
  await command('Page.navigate', { url: base + '/asset-failure-check' }, session);
  await requested;
  await waitFor(`document.querySelector('[role="status"]')?.textContent === 'Opening page…'`);
  assert.equal(await evaluate(`document.querySelector('[role="alert"]').textContent`), '');
  release();
  await ready('Page could not open');
  await checkSemantics();
  assert.equal(await evaluate('document.title'), 'Page could not open');
  assert.equal(await evaluate('document.activeElement.id'), 'main-content');
  assert.equal(await evaluate(`document.querySelector('[role="alert"]').textContent`), 'The requested page could not open.');
  assert.equal(await evaluate(`document.querySelector('[role="status"]').textContent`), '');
  assert.equal(await evaluate(`document.body.textContent.includes('test-only asset failure detail')`), false);
  await key('Tab', 'Tab', 9, 8);
  assert.equal(await evaluate('document.activeElement.textContent.trim()'), 'Storefront');
  await checkFocusedLink();
  await key('Enter', 'Enter', 13);
  await checkContext('Welcome to the storefront', 'Storefront', 'page');
  await checkSemantics();
  console.log('PASS: real lazy-asset loading/failure, exclusive status markup and keyboard recovery');

  // 320x256 CSS pixels is the reflow viewport equivalent to 1280x1024 at 400%.
  // This tests the equivalent reflow condition, not the browser zoom control itself.
  const viewports = [
    { width: 1280, height: 900, type: 'landscapePrimary', angle: 90, label: 'wide viewport' },
    { width: 320, height: 256, type: 'landscapePrimary', angle: 90, label: '400%-equivalent reflow' },
    { width: 320, height: 640, type: 'portraitPrimary', angle: 0, label: 'portrait' },
    { width: 640, height: 320, type: 'landscapePrimary', angle: 90, label: 'landscape' },
  ];
  for (const path of ['/', '/missing']) {
    for (const viewport of viewports) {
      await command('Emulation.setDeviceMetricsOverride', {
        width: viewport.width, height: viewport.height, deviceScaleFactor: 1, mobile: false,
        screenOrientation: { type: viewport.type, angle: viewport.angle },
      }, session);
      // Reload each fixture so stress styles and content do not accumulate.
      const loaded = nextEvent('Page.loadEventFired');
      await command('Page.navigate', { url: base + path }, session);
      await loaded;
      await ready(path === '/' ? 'Welcome to the storefront' : 'Page not available');
      await checkSemantics();
      assert.equal(await evaluate(`screen.orientation.type`), viewport.type.replace('Primary', '-primary'));
      // WCAG 1.4.12 spacing conditions, plus separately identified text-size stress.
      await evaluate(`(() => {
        const style = document.createElement('style');
        style.textContent = 'html { font-size: 200% !important; } * { line-height: 1.5 !important; letter-spacing: .12em !important; word-spacing: .16em !important; } p { margin-bottom: 2em !important; }';
        document.head.append(style);
        document.querySelector('h1').textContent += ' ' + 'LongContent'.repeat(30);
        document.querySelector('nav a').textContent = 'Storefront entry navigation';
      })()`);
      assert.equal(await evaluate(`document.documentElement.scrollWidth <= innerWidth`), true);
      assert.equal(await evaluate(`[...document.querySelectorAll('main, nav, h1, p, a:not(.skip-link)')].every(el => { const r = el.getBoundingClientRect(); return r.width > 0 && r.left >= 0 && r.right <= innerWidth; })`), true);
      // Test fixtures alter only text, never hrefs, application routes or business evidence.
      await evaluate(`document.body.tabIndex = -1; document.body.focus(); document.body.removeAttribute('tabindex');`);
      await key('Tab', 'Tab', 9);
      assert.equal(await evaluate('document.activeElement.className'), 'skip-link');
      await checkFocusedLink();
      await key('Tab', 'Tab', 9);
      assert.equal(await evaluate(`document.activeElement.closest('nav') !== null`), true);
      await checkFocusedLink();
      await key('Tab', 'Tab', 9, 8);
      assert.equal(await evaluate('document.activeElement.className'), 'skip-link');
      await key('Enter', 'Enter', 13);
      // Allow native fragment/popstate delivery and the following Angular render to settle.
      await evaluate('new Promise(resolve => requestAnimationFrame(() => requestAnimationFrame(resolve)))');
      await waitFor(`document.activeElement.id === 'main-content' && document.querySelector('[role="status"]').textContent === ''`);
      assert.equal(await evaluate('location.pathname'), path);
      assert.equal(await evaluate('document.activeElement.id'), 'main-content');
      if (path === '/missing') {
        await key('Tab', 'Tab', 9);
        assert.equal(await evaluate('document.activeElement.textContent.trim()'), 'Return to the storefront');
        await checkFocusedLink();
        await key('Enter', 'Enter', 13);
        await checkContext('Welcome to the storefront', 'Storefront', 'page');
      }
      console.log(`PASS: ${path} ${viewport.label} ${viewport.width}x${viewport.height}, text spacing, long content, wrapped navigation, unobscured focus and keyboard bypass/recovery`);
    }
  }
  console.log('These bounded checks do not certify WCAG conformance or replace manual assistive-technology/zoom review.');
} finally {
  chrome.kill();
  await new Promise(resolve => chrome.once('exit', resolve));
  server.close();
  await rm(profile, { recursive: true, force: true });
}
