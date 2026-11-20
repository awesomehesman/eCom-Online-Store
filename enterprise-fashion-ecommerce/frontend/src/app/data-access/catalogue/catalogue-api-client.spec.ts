import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CatalogueApiClient } from './catalogue-api-client';

describe('Catalogue API client', () => {
  const page = { items: [], pageSize: 20, nextCursor: null };
  let client: CatalogueApiClient;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    client = TestBed.inject(CatalogueApiClient);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  function flush(body: unknown, status = 200, headers: Record<string, string> = {
    'Content-Type': 'application/json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test',
  }): void {
    http.expectOne(request => request.url === '/api/v1/products' && request.params.get('limit') === '20')
      .flush(body as never, { status, statusText: status === 200 ? 'OK' : 'Failure', headers });
  }

  it('requests and accepts a strictly validated empty page', () => {
    let result: unknown;
    client.loadPage().subscribe(value => result = value);
    const request = http.expectOne('/api/v1/products?limit=20');
    expect(request.request.method).toBe('GET');
    expect(request.request.withCredentials).toBeFalse();
    expect(request.request.headers.get('Accept')).toBe('application/json');
    request.flush(page, { headers: {
      'Content-Type': 'application/json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test',
    } });
    expect(result).toEqual(jasmine.objectContaining({ kind: 'page' }));
  });

  it('binds continuation requests to the original page size', () => {
    client.loadPage(20, 'Opaque_next').subscribe();
    const request = http.expectOne('/api/v1/products?limit=20&cursor=Opaque_next');
    expect(request.request.method).toBe('GET');
    request.flush(page, { headers: {
      'Content-Type': 'application/json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test',
    } });
  });

  it('maps 503 to unavailable without treating it as empty', () => {
    let result: unknown;
    client.loadPage().subscribe(value => result = value);
    flush({ type: 'about:blank', title: 'Service Unavailable', status: 503, correlationId: 'test' }, 503, {
      'Content-Type': 'application/problem+json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test',
    });
    expect(result).toEqual({ kind: 'unavailable' });
  });

  it('rejects malformed 503 responses instead of claiming evidence unavailability', () => {
    for (const body of [null, {}, { type: 'about:blank', title: 'Service Unavailable', status: 503, correlationId: 'wrong' }]) {
      let result: unknown;
      client.loadPage().subscribe(value => result = value);
      flush(body, 503, {
        'Content-Type': 'application/problem+json', 'Cache-Control': 'no-store', 'X-Correlation-ID': 'test',
      });
      expect(result).toEqual({ kind: 'error', failure: 'invalid-response' });
    }
  });

  it('requires the exact no-store response directive', () => {
    for (const cacheControl of ['no-cache, no-store', 'no-store, private', 'max-age=0']) {
      let result: unknown;
      client.loadPage().subscribe(value => result = value);
      flush(page, 200, {
        'Content-Type': 'application/json', 'Cache-Control': cacheControl, 'X-Correlation-ID': 'test',
      });
      expect(result).toEqual({ kind: 'error', failure: 'invalid-response' });
    }
  });

  it('cancels the underlying HTTP request when its subscriber leaves', () => {
    const subscription = client.loadPage().subscribe();
    const request = http.expectOne('/api/v1/products?limit=20');
    expect(request.cancelled).toBeFalse();
    subscription.unsubscribe();
    expect(request.cancelled).toBeTrue();
  });

  for (const status of [400, 404, 500]) {
    it(`maps HTTP ${status} to an error`, () => {
      let result: unknown;
      client.loadPage().subscribe(value => result = value);
      flush(null, status);
      expect(result).toEqual({ kind: 'error', failure: 'server' });
    });
  }

  it('distinguishes network failure', () => {
    let result: unknown;
    client.loadPage().subscribe(value => result = value);
    http.expectOne('/api/v1/products?limit=20').error(new ProgressEvent('network'));
    expect(result).toEqual({ kind: 'error', failure: 'network' });
  });

  it('rejects malformed and unsupported response bodies', () => {
    let malformed: unknown;
    client.loadPage().subscribe(value => malformed = value);
    flush('not-json');
    expect(malformed).toEqual({ kind: 'error', failure: 'invalid-response' });

    let unsupported: unknown;
    client.loadPage().subscribe(value => unsupported = value);
    flush({ ...page, extra: true });
    expect(unsupported).toEqual({ kind: 'error', failure: 'invalid-response' });
  });

  it('requires contract response headers and the requested page size', () => {
    let missingHeaders: unknown;
    client.loadPage().subscribe(value => missingHeaders = value);
    flush(page, 200, {});
    expect(missingHeaders).toEqual({ kind: 'error', failure: 'invalid-response' });

    let wrongSize: unknown;
    client.loadPage().subscribe(value => wrongSize = value);
    flush({ ...page, pageSize: 10 });
    expect(wrongSize).toEqual({ kind: 'error', failure: 'invalid-response' });
  });

  it('rejects invalid local pagination without making a request', async () => {
    expect(await client.loadPage(0).toPromise()).toEqual({ kind: 'error', failure: 'invalid-response' });
    expect(await client.loadPage(20, 'bad cursor').toPromise()).toEqual({ kind: 'error', failure: 'invalid-response' });
  });
});
