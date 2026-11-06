import { observeCatalogue } from './catalogue-observation';

describe('Anonymous catalogue observation', () => {
  let request: jasmine.Spy;
  const page = { items: [], pageSize: 20, nextCursor: null };
  const response = (body: unknown = page) => new Response(JSON.stringify(body), {
    headers: { 'Content-Type': 'application/json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test' },
  });
  const observe = () => observeCatalogue(new AbortController().signal);
  beforeEach(() => { request = spyOn(window, 'fetch'); });

  it('accepts only a confirmed empty page and omits credentials and caching', async () => {
    request.and.resolveTo(response());
    expect(await observe()).toBe('empty');
    const [url, options] = request.calls.mostRecent().args;
    expect(url).toBe('/api/v1/products');
    expect(options).toEqual(jasmine.objectContaining({ method: 'GET', credentials: 'omit', cache: 'no-store', redirect: 'error' }));
    expect(options.signal).toBeInstanceOf(AbortSignal);
  });

  for (const status of [204, 400, 500, 503]) {
    it(`withholds empty success for HTTP ${status}`, async () => {
      request.and.resolveTo(new Response(null, { status }));
      expect(await observe()).toBe('unavailable');
    });
  }
  it('handles network failure and abort without success', async () => {
    request.and.rejectWith(new TypeError('private transport detail'));
    expect(await observe()).toBe('unavailable');
    request.and.rejectWith(new DOMException('Aborted', 'AbortError'));
    expect(await observe()).toBe('unavailable');
  });
  it('rejects invalid JSON', async () => {
    const broken = response();
    spyOn(broken, 'json').and.rejectWith(new SyntaxError('invalid'));
    request.and.resolveTo(broken);
    expect(await observe()).toBe('unavailable');
  });
  for (const body of [null, [], {}, { ...page, pageSize: 0 }, { ...page, pageSize: 21 },
    { ...page, pageSize: '20' }, { ...page, nextCursor: 'next' }, { ...page, extra: true },
    { ...page, items: null }, { ...page, nextCursor: '' }]) {
    it(`rejects malformed or inconsistent pagination ${JSON.stringify(body)}`, async () => {
      request.and.resolveTo(response(body));
      expect(await observe()).toBe('unavailable');
    });
  }
  it('withholds all non-empty content instead of claiming empty or rendering items', async () => {
    request.and.resolveTo(response({ ...page, items: [{ productId: 'not-rendered' }] }));
    expect(await observe()).toBe('unsupported');
  });
  it('requires contract response headers', async () => {
    request.and.resolveTo(new Response(JSON.stringify(page)));
    expect(await observe()).toBe('unavailable');
  });
});
