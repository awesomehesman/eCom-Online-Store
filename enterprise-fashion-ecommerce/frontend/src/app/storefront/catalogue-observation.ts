/** Presentation evidence only: never Product publication or commercial authority. */
export type CatalogueObservation = 'empty' | 'unavailable' | 'unsupported';

export async function observeCatalogue(signal: AbortSignal): Promise<CatalogueObservation> {
  try {
    const response = await fetch('/api/v1/products', {
      method: 'GET', credentials: 'omit', cache: 'no-store', redirect: 'error',
      headers: { Accept: 'application/json' }, signal,
    });
    if (response.status !== 200 || response.headers.get('Content-Type')?.split(';')[0].trim() !== 'application/json'
      || !response.headers.get('Cache-Control')?.split(',').some(value => value.trim().toLowerCase() === 'no-store')
      || !/^[A-Za-z0-9._-]{1,128}$/.test(response.headers.get('X-Correlation-ID') ?? '')) return 'unavailable';
    const body: unknown = await response.json();
    if (body === null || typeof body !== 'object' || Array.isArray(body)) return 'unavailable';
    const page = body as Record<string, unknown>;
    if (Object.keys(page).length !== 3 || !Array.isArray(page['items'])
      || page['pageSize'] !== 20 || page['items'].length > 20
      || !(page['nextCursor'] === null || (typeof page['nextCursor'] === 'string'
        && /^[A-Za-z0-9_-]{1,2048}$/.test(page['nextCursor'])))) return 'unavailable';
    // Positive Product schemas are intentionally not consumed. No item is rendered or trusted.
    if (page['items'].length > 0) return 'unsupported';
    return page['nextCursor'] === null ? 'empty' : 'unavailable';
  } catch {
    return 'unavailable';
  }
}
