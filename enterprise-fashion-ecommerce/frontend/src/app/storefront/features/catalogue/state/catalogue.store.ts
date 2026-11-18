import { computed, inject } from '@angular/core';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { Subscription } from 'rxjs';

import { CatalogueApiClient, CatalogueObservation } from '../../../../data-access/catalogue/catalogue-api-client';
import type { ValidatedCataloguePage } from '../../../../data-access/catalogue/catalogue-contract';

export type CatalogueLoadState = 'idle' | 'loading' | 'ready' | 'empty' | 'unavailable' | 'error';

export interface CatalogueState {
  /** Current loading state. */
  readonly loadState: CatalogueLoadState;
  /** Validated catalogue page when loaded, null otherwise. */
  readonly page: ValidatedCataloguePage | null;
  /** Current request generation to prevent stale response application. */
  readonly generation: number;
  /** Last productId before the current page (for ordering validation). */
  readonly afterExclusive: string | null;
  /** Whether to focus heading after load completes. */
  readonly focusAfterLoad: boolean;
}

const initialState: CatalogueState = {
  loadState: 'idle',
  page: null,
  generation: 0,
  afterExclusive: null,
  focusAfterLoad: false,
};

/**
 * Feature-scoped Catalogue Signal Store.
 * Owns loading state, validated page data, pagination, and request lifecycle.
 * Preserves CatalogueApiClient as the transport and validation boundary.
 */
export const CatalogueStore = signalStore(
  withState(initialState),

  withComputed(({ loadState, page }) => ({
    /** Whether a request is currently in progress. */
    isLoading: computed(() => loadState() === 'loading'),

    /** Whether the catalogue has loaded items. */
    hasItems: computed(() => page() !== null && page()!.items.length > 0),

    /** Whether there are more pages available. */
    hasNextPage: computed(() => page()?.nextCursor !== null && page()?.nextCursor !== undefined),

    /** Number of items on the current page. */
    itemCount: computed(() => page()?.items.length ?? 0),

    /** Status message for screen readers. */
    statusMessage: computed(() => {
      switch (loadState()) {
        case 'idle':
        case 'loading':
          return 'Checking catalogue…';
        case 'empty':
          return 'No products are currently available to browse.';
        case 'ready':
          return `${page()?.items.length ?? 0} products available on this page.`;
        case 'unavailable':
          return 'Catalogue availability could not be confirmed. You can check again.';
        default:
          return 'The catalogue could not be loaded. You can try again.';
      }
    }),
  })),

  withMethods((store, catalogueApi = inject(CatalogueApiClient)) => {
    let activeSubscription: Subscription | null = null;

    function cancelActiveRequest(): void {
      if (activeSubscription) {
        activeSubscription.unsubscribe();
        activeSubscription = null;
      }
    }

    function applyOutcome(
      outcome: CatalogueObservation,
      requestGeneration: number,
      afterExclusive: string | null
    ): void {
      // Ignore stale responses from superseded requests
      if (requestGeneration !== store.generation()) {
        return;
      }

      if (outcome.kind === 'page') {
        // Validate page ordering: no item should be <= afterExclusive
        if (afterExclusive !== null && outcome.page.items.some(item => item.productId <= afterExclusive)) {
          patchState(store, {
            loadState: 'error',
            page: null,
          });
        } else {
          const isEmpty = outcome.page.items.length === 0;
          patchState(store, {
            loadState: isEmpty ? 'empty' : 'ready',
            page: isEmpty ? null : outcome.page,
          });
        }
      } else if (outcome.kind === 'unavailable') {
        patchState(store, {
          loadState: 'unavailable',
          page: null,
        });
      } else {
        patchState(store, {
          loadState: 'error',
          page: null,
        });
      }
    }

    return {
      /**
       * Load the catalogue from the beginning.
       * Cancels any active request and resets pagination state.
       */
      recheck(): void {
        cancelActiveRequest();

        const nextGeneration = store.generation() + 1;
        patchState(store, {
          loadState: 'loading',
          page: null,
          generation: nextGeneration,
          afterExclusive: null,
          focusAfterLoad: false,
        });

        activeSubscription = catalogueApi.loadPage(20, null).subscribe(outcome => {
          applyOutcome(outcome, nextGeneration, null);
        });
      },

      /**
       * Load the next page of results.
       * Only proceeds if there's a current page with a next cursor.
       */
      loadNextPage(): void {
        const currentPage = store.page();
        const cursor = currentPage?.nextCursor;
        const lastProductId = currentPage?.items.at(-1)?.productId;

        if (!cursor || !lastProductId) {
          return;
        }

        cancelActiveRequest();

        const nextGeneration = store.generation() + 1;
        patchState(store, {
          loadState: 'loading',
          page: null,
          generation: nextGeneration,
          afterExclusive: lastProductId,
          focusAfterLoad: true,
        });

        activeSubscription = catalogueApi.loadPage(20, cursor).subscribe(outcome => {
          applyOutcome(outcome, nextGeneration, lastProductId);
        });
      },

      /**
       * Mark that focus has been handled after a load.
       */
      clearFocusAfterLoad(): void {
        patchState(store, { focusAfterLoad: false });
      },

      /**
       * Clean up active subscriptions.
       * Call this when the store is being destroyed.
       */
      cleanup(): void {
        cancelActiveRequest();
      },
    };
  }),
);
