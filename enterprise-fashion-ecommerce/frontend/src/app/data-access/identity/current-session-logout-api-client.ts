import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, switchMap } from 'rxjs';

export type CurrentSessionLogoutOutcome =
  | { readonly kind: 'confirmed' | 'rejected' | 'uncertain' | 'invalid-request' }
  | {
    readonly kind: 'unavailable' | 'invalid-response';
    readonly stage: 'acquisition' | 'logout';
  };

type RequestStage = 'acquisition' | 'logout';

function validOutcomeHeaders(headers: HttpHeaders): boolean {
  const correlation = headers.getAll('X-Correlation-ID');
  const cache = headers.getAll('Cache-Control');
  return correlation?.length === 1 && /^[A-Za-z0-9._~-]{1,128}$/.test(correlation[0])
    && cache?.length === 1 && cache[0] === 'no-store';
}

function contentType(headers: HttpHeaders): string | undefined {
  return headers.get('Content-Type')?.split(';')[0].trim().toLowerCase();
}

function objectValue(value: unknown): Record<string, unknown> | null {
  return value !== null && typeof value === 'object' && !Array.isArray(value)
    ? value as Record<string, unknown> : null;
}

function boundedString(value: unknown, min: number, max: number): value is string {
  return typeof value === 'string' && value.length >= min && value.length <= max;
}

function validProblem(response: HttpErrorResponse): boolean {
  let payload: unknown = response.error;
  if (typeof payload === 'string') {
    try {
      payload = JSON.parse(payload);
    } catch {
      return false;
    }
  }
  const body = objectValue(payload);
  return validOutcomeHeaders(response.headers) && contentType(response.headers) === 'application/problem+json'
    && body !== null && body['status'] === response.status
    && boundedString(body['type'], 0, 2048) && boundedString(body['title'], 1, 160)
    && (!Object.hasOwn(body, 'detail') || boundedString(body['detail'], 0, 1024))
    && (!Object.hasOwn(body, 'instance') || boundedString(body['instance'], 0, 2048))
    && Object.keys(body).every(key => ['status', 'type', 'title', 'detail', 'instance'].includes(key));
}

function failureOutcome(error: unknown, stage: RequestStage): CurrentSessionLogoutOutcome {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 0 || (error.status >= 500 && error.status !== 503)) {
      return { kind: 'unavailable', stage };
    }
    if (error.status === 400 && validProblem(error)) return { kind: 'invalid-request' };
    if (error.status === 403 && validProblem(error)) return { kind: 'rejected' };
    if (error.status === 503 && stage === 'logout' && validProblem(error)) return { kind: 'uncertain' };
  }
  return { kind: 'invalid-response', stage };
}

@Injectable({ providedIn: 'root' })
export class CurrentSessionLogoutApiClient {
  private readonly http = inject(HttpClient);

  logout(): Observable<CurrentSessionLogoutOutcome> {
    return this.http.get<unknown>('/api/v1/identity/csrf', {
      observe: 'response', headers: { Accept: 'application/json' },
    }).pipe(
      switchMap(response => {
        const body = objectValue(response.body);
        const token = body?.['token'];
        if (response.status !== 200 || !validOutcomeHeaders(response.headers)
          || contentType(response.headers) !== 'application/json' || body === null
          || Object.keys(body).length !== 1 || typeof token !== 'string' || !/^[!-~]+$/.test(token)) {
          return of({ kind: 'invalid-response', stage: 'acquisition' } as const);
        }

        return this.http.post('/api/v1/identity/logout', null, {
          observe: 'response', responseType: 'text',
          headers: { Accept: 'application/problem+json', 'X-XSRF-TOKEN': token },
        }).pipe(
          map(response => response.status === 204 && validOutcomeHeaders(response.headers)
            && (response.body === null || response.body === '')
            ? { kind: 'confirmed' } as const
            : { kind: 'invalid-response', stage: 'logout' } as const),
          catchError((error: unknown) => of(failureOutcome(error, 'logout'))),
        );
      }),
      catchError((error: unknown) => of(failureOutcome(error, 'acquisition'))),
    );
  }
}
