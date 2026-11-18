import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';

import { ValidatedCataloguePage, validateCataloguePage } from './catalogue-page';

export type CatalogueFailure = 'network' | 'server' | 'invalid-response';
export type CatalogueObservation =
  | { readonly kind: 'page'; readonly page: ValidatedCataloguePage }
  | { readonly kind: 'unavailable' }
  | { readonly kind: 'error'; readonly failure: CatalogueFailure };

const correlation = /^[A-Za-z0-9._-]{1,128}$/;

function validHeaders(headers: HttpHeaders): boolean {
  return headers.get('Content-Type')?.split(';')[0].trim().toLowerCase() === 'application/json'
    && headers.get('Cache-Control')?.split(',').some(value => value.trim().toLowerCase() === 'no-store') === true
    && correlation.test(headers.get('X-Correlation-ID') ?? '');
}

/** Anonymous, read-only client. A valid response remains presentation data, never visibility authority. */
@Injectable({ providedIn: 'root' })
export class CatalogueApiClient {
  private readonly http = inject(HttpClient);

  loadPage(pageSize = 20, cursor: string | null = null): Observable<CatalogueObservation> {
    if (!Number.isInteger(pageSize) || pageSize < 1 || pageSize > 100
      || (cursor !== null && !/^[A-Za-z0-9_-]{1,2048}$/.test(cursor))) {
      return of({ kind: 'error', failure: 'invalid-response' });
    }
    const params: Record<string, string> = { limit: String(pageSize) };
    if (cursor !== null) params['cursor'] = cursor;
    return this.http.get<unknown>('/api/v1/products', {
      observe: 'response', responseType: 'json', params,
      headers: { Accept: 'application/json', 'Cache-Control': 'no-store' },
    }).pipe(
      map((response: HttpResponse<unknown>): CatalogueObservation => {
        const page = validHeaders(response.headers) ? validateCataloguePage(response.body) : null;
        return page !== null && page.pageSize === pageSize
          ? { kind: 'page', page }
          : { kind: 'error', failure: 'invalid-response' };
      }),
      catchError((failure: unknown): Observable<CatalogueObservation> => {
        if (failure instanceof HttpErrorResponse) {
          if (failure.status === 503) return of({ kind: 'unavailable' });
          if (failure.status === 0) return of({ kind: 'error', failure: 'network' });
          if (failure.status === 200) return of({ kind: 'error', failure: 'invalid-response' });
        }
        return of({ kind: 'error', failure: 'server' });
      }),
    );
  }
}
