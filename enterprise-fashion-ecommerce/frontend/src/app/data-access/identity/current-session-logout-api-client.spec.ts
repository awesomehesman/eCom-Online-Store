import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CurrentSessionLogoutApiClient, CurrentSessionLogoutOutcome } from './current-session-logout-api-client';

describe('CurrentSessionLogoutApiClient', () => {
  const csrf = '/api/v1/identity/csrf';
  const logout = '/api/v1/identity/logout';
  const outcomeHeaders = { 'Cache-Control': 'no-store', 'X-Correlation-ID': 'logout~test' };
  const tokenHeaders = { ...outcomeHeaders, 'Content-Type': 'application/json' };
  const problemHeaders = { ...outcomeHeaders, 'Content-Type': 'application/problem+json' };
  let client: CurrentSessionLogoutApiClient;
  let http: HttpTestingController;
  let outcomes: CurrentSessionLogoutOutcome[];

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    client = TestBed.inject(CurrentSessionLogoutApiClient);
    http = TestBed.inject(HttpTestingController);
    outcomes = [];
  });

  afterEach(() => http.verify());

  function startLogout() {
    const subscription = client.logout().subscribe(outcome => outcomes.push(outcome));
    http.expectOne({ method: 'GET', url: csrf }).flush({ token: 'masked-token' }, { headers: tokenHeaders });
    const request = http.expectOne({ method: 'POST', url: logout });
    return { request, subscription };
  }

  it('posts only the acquired masked header and confirms a bodyless 204', () => {
    const { request } = startLogout();

    expect(request.request.headers.get('X-XSRF-TOKEN')).toBe('masked-token');
    expect(request.request.body).toBeNull();
    expect(request.request.params.keys()).toEqual([]);
    request.flush(null, { status: 204, statusText: 'No Content', headers: outcomeHeaders });

    expect(outcomes).toEqual([{ kind: 'confirmed' }]);
  });

  it('stops on an invalid CSRF token, extra fields, or missing cache policy', () => {
    for (const [body, headers] of [
      [{ token: '' }, tokenHeaders],
      [{ token: 'masked\r\ntoken' }, tokenHeaders],
      [{ token: 'masked-token', session: 'unexpected' }, tokenHeaders],
      [{ token: 'masked-token' }, { 'Content-Type': 'application/json', 'X-Correlation-ID': 'test' }],
    ] as const) {
      client.logout().subscribe(outcome => outcomes.push(outcome));
      http.expectOne(csrf).flush(body, { headers });
      http.expectNone(logout);
    }
    expect(outcomes).toEqual(Array(4).fill({ kind: 'invalid-response', stage: 'acquisition' }));
  });

  it('classifies acquisition rejection and sends no POST', () => {
    client.logout().subscribe(outcome => outcomes.push(outcome));
    http.expectOne(csrf).flush({ type: 'about:blank', title: 'Forbidden', status: 403 }, {
      status: 403, statusText: 'Forbidden', headers: problemHeaders,
    });
    http.expectNone(logout);
    expect(outcomes).toEqual([{ kind: 'rejected' }]);
  });

  it('distinguishes rejected, invalid-request and uncertain Problem Details', () => {
    for (const [status, title] of [[403, 'Forbidden'], [400, 'Bad Request'], [503, 'Service Unavailable']] as const) {
      startLogout().request.flush(JSON.stringify({ type: 'about:blank', title, status }), {
        status, statusText: title, headers: problemHeaders,
      });
    }
    expect(outcomes).toEqual([{ kind: 'rejected' }, { kind: 'invalid-request' }, { kind: 'uncertain' }]);
  });

  it('rejects malformed logout responses including an invalid 503 and unexpected success', () => {
    startLogout().request.flush('{"status":403}', {
      status: 503, statusText: 'Service Unavailable', headers: problemHeaders,
    });
    startLogout().request.flush('', { status: 200, statusText: 'OK', headers: outcomeHeaders });
    startLogout().request.flush(null, { status: 204, statusText: 'No Content' });
    expect(outcomes).toEqual(Array(3).fill({ kind: 'invalid-response', stage: 'logout' }));
  });

  it('distinguishes which request was interrupted by a transport error', () => {
    client.logout().subscribe(outcome => outcomes.push(outcome));
    http.expectOne(csrf).error(new ProgressEvent('error'));
    http.expectNone(logout);
    startLogout().request.error(new ProgressEvent('error'));
    expect(outcomes).toEqual([
      { kind: 'unavailable', stage: 'acquisition' }, { kind: 'unavailable', stage: 'logout' },
    ]);
  });

  it('cancels acquisition and logout requests without emitting an outcome', () => {
    const acquisition = client.logout().subscribe(outcome => outcomes.push(outcome));
    const get = http.expectOne(csrf);
    acquisition.unsubscribe();
    expect(get.cancelled).toBeTrue();
    http.expectNone(logout);

    const { request, subscription } = startLogout();
    subscription.unsubscribe();
    expect(request.cancelled).toBeTrue();
    expect(outcomes).toEqual([]);
  });
});
