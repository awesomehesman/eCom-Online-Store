// Bounded real-browser checks using Chrome's DevTools pipe; no added dependencies.
import { spawn } from 'node:child_process';
import { createServer } from 'node:http';
import { mkdtemp, readFile, rm } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import { join, resolve, extname } from 'node:path';
import assert from 'node:assert/strict';

const root = resolve('dist/storefront/browser');
const server = createServer(async (req, res) => {
  try {
    const pathname = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
    const path = resolve(root, '.' + pathname);
    if (!path.startsWith(root + '/') && path !== root) { res.writeHead(403).end(); return; }
    // Local SPA fallback only; this does not select a hosting configuration.
    const file = extname(path) ? path : join(root, 'index.html');
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
    const timer = setTimeout(() => { observer.disconnect(); reject(new Error('Route did not render')); }, 10000);
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

  for (const path of ['/', '/missing']) {
    await command('Page.navigate', { url: base + path }, session);
    await ready(path === '/' ? 'Welcome to the storefront' : 'Page not available');
    for (const width of [320, 1280]) {
      await command('Emulation.setDeviceMetricsOverride', { width, height: 900, deviceScaleFactor: 1, mobile: false }, session);
      // WCAG 1.4.12 spacing conditions plus 200% text-size stress. No breakpoint is selected.
      await evaluate(`(() => { const style = document.createElement('style'); style.textContent = 'html { font-size: 200% !important; } * { line-height: 1.5 !important; letter-spacing: .12em !important; word-spacing: .16em !important; } p { margin-bottom: 2em !important; }'; document.head.append(style); })()`);
      await evaluate(`document.querySelector('h1').textContent += ' ' + 'LongContent'.repeat(30)`);
      assert.equal(await evaluate(`document.documentElement.scrollWidth <= innerWidth`), true);
      assert.equal(await evaluate(`document.querySelectorAll('main').length === 1 && document.querySelectorAll('h1').length === 1`), true);
      assert.equal(await evaluate(`[...document.querySelectorAll('main, nav, h1, p, a:not(.skip-link)')].every(el => { const r = el.getBoundingClientRect(); return r.width > 0 && r.left >= 0 && r.right <= innerWidth; })`), true);
      console.log(`PASS: ${path} at ${width}px, enlarged text, text spacing and long content; no horizontal overflow`);
    }
  }
  console.log('These bounded checks do not certify WCAG conformance or replace manual assistive-technology/zoom review.');
} finally {
  chrome.kill();
  await new Promise(resolve => chrome.once('exit', resolve));
  server.close();
  await rm(profile, { recursive: true, force: true });
}
