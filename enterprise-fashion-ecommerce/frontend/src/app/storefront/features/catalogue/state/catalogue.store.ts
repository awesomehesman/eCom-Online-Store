import { computed, DestroyRef, Injectable, inject, signal } from '@angular/core';
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
 * Feature-scoped Catalogue state.
 * Owns loading state, validated page data, pagination, and request lifecycle.
 * Preserves CatalogueApiClient as the transport and validation boundary.
 */
@Injectable()
export class CatalogueStore {
  private readonly state = signal(initialState);
  private readonly catalogueApi = inject(CatalogueApiClient);
  private activeSubscription: Subscription | null = null;
  private destroyed = false;

  readonly loadState = computed(() => this.state().loadState);
  readonly page = computed(() => this.state().page);
  readonly generation = computed(() => this.state().generation);
  readonly afterExclusive = computed(() => this.state().afterExclusive);
  readonly focusAfterLoad = computed(() => this.state().focusAfterLoad);
  readonly isLoading = computed(() => this.loadState() === 'loading');
  readonly hasItems = computed(() => (this.page()?.items.length ?? 0) > 0);
  readonly hasNextPage = computed(() => this.page()?.nextCursor != null);
  readonly itemCount = computed(() => this.page()?.items.length ?? 0);

  /** Status message for screen readers. */
  readonly statusMessage = computed(() => {
    switch (this.loadState()) {
      case 'idle':
      case 'loading':
        return 'Checking catalogue…';
      case 'empty':
        return 'No products are currently available to browse.';
      case 'ready':
        return `${this.page()?.items.length ?? 0} products available on this page.`;
      case 'unavailable':
        return 'Catalogue availability could not be confirmed. You can check again.';
      default:
        return 'The catalogue could not be loaded. You can try again.';
    }
  });

  constructor() {
    inject(DestroyRef).onDestroy(() => this.cleanup());
  }

  recheck(): void {
    this.load(null, null);
  }

  loadNextPage(): void {
    const page = this.page();
    const last = page?.items.at(-1)?.productId;
    if (page?.nextCursor && last) {
      this.load(page.nextCursor, last);
    }
  }

  clearFocusAfterLoad(): void {
    if (!this.destroyed) this.patch({ focusAfterLoad: false });
  }

  cleanup(): void {
    if (this.destroyed) return;
    this.destroyed = true;
    this.activeSubscription?.unsubscribe();
    this.activeSubscription = null;
    this.patch({ generation: this.generation() + 1 });
  }

  private load(cursor: string | null, afterExclusive: string | null): void {
    if (this.destroyed) return;
    const generation = this.generation() + 1;
    this.patch({ loadState: 'loading', page: null, generation, afterExclusive, focusAfterLoad: cursor !== null });
    this.activeSubscription?.unsubscribe();
    this.activeSubscription = this.catalogueApi.loadPage(20, cursor).subscribe(outcome => {
      this.applyOutcome(outcome, generation, afterExclusive);
    });
  }

  private applyOutcome(outcome: CatalogueObservation, generation: number, afterExclusive: string | null): void {
    if (this.destroyed || generation !== this.generation()) return;
    if (outcome.kind === 'page') {
      if (afterExclusive !== null && outcome.page.items.some(item => item.productId <= afterExclusive)) {
        this.patch({ loadState: 'error', page: null });
      } else if (outcome.page.items.length === 0) {
        this.patch({ loadState: 'empty', page: null });
      } else {
        this.patch({ loadState: 'ready', page: outcome.page });
      }
    } else {
      this.patch({ loadState: outcome.kind === 'unavailable' ? 'unavailable' : 'error', page: null });
    }
  }

  private patch(update: Partial<CatalogueState>): void {
    this.state.update(state => ({ ...state, ...update }));
  }
}
