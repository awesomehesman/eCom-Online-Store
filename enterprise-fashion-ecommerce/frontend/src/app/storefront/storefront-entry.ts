import { ChangeDetectionStrategy, Component, DestroyRef, ElementRef, ViewChild, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { StorefrontPageStateComponent } from './storefront-page-state';
import { CatalogueApiClient, CatalogueObservation } from './catalogue-observation';
import { ProductSummaryListComponent } from './product-summary-list';
import type { ValidatedCataloguePage } from './catalogue-page';

@Component({
  selector: 'app-storefront-entry',
  standalone: true,
  imports: [StorefrontPageStateComponent, ProductSummaryListComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-page-state state="catalogue" />
    <section aria-labelledby="catalogue-heading">
      <h2 #catalogueHeading id="catalogue-heading" tabindex="-1">Catalogue</h2>
      <p role="status" aria-atomic="true">{{ announce() ? message() : '' }}</p>
      @if (page(); as currentPage) {
        <app-product-summary-list [page]="currentPage" />
      }
      <button type="button" (click)="recheck()">Check catalogue again</button>
      @if (page()?.nextCursor) {
        <button type="button" (click)="nextPage()" [disabled]="state() === 'loading'">Show next products</button>
      }
    </section>
  `,
})
export class StorefrontEntry {
  @ViewChild('catalogueHeading') private heading?: ElementRef<HTMLElement>;
  protected readonly state = signal<'loading' | 'empty' | 'ready' | 'unavailable' | 'error'>('loading');
  protected readonly page = signal<ValidatedCataloguePage | null>(null);
  protected readonly announce = signal(false);
  private readonly catalogue = inject(CatalogueApiClient);
  private active?: Subscription;
  private generation = 0;
  private destroyed = false;
  private focusAfterLoad = false;

  constructor() {
    inject(Router).events.pipe(takeUntilDestroyed()).subscribe(event => {
      if (event instanceof NavigationStart || event instanceof NavigationError) this.announce.set(false);
      if (event instanceof NavigationEnd || event instanceof NavigationCancel) this.announce.set(true);
    });
    inject(DestroyRef).onDestroy(() => {
      this.destroyed = true;
      this.active?.unsubscribe();
    });
    this.recheck();
  }

  protected message(): string {
    switch (this.state()) {
      case 'loading': return 'Checking catalogue…';
      case 'empty': return 'No products are currently available to browse.';
      case 'ready': return `${this.page()?.items.length ?? 0} products available on this page.`;
      case 'unavailable': return 'Catalogue availability could not be confirmed. You can check again.';
      default: return 'The catalogue could not be loaded. You can try again.';
    }
  }

  recheck(): void {
    this.load(null, null, false);
  }

  nextPage(): void {
    const page = this.page();
    const cursor = page?.nextCursor;
    const previous = page?.items.at(-1)?.productId;
    if (cursor && previous) this.load(cursor, previous, true);
  }

  private load(cursor: string | null, afterExclusive: string | null, focusAfterLoad: boolean): void {
    if (this.destroyed) return;
    this.active?.unsubscribe();
    const request = ++this.generation;
    this.focusAfterLoad = focusAfterLoad;
    this.page.set(null);
    this.state.set('loading');
    this.active = this.catalogue.loadPage(20, cursor).subscribe(outcome => {
      if (this.destroyed || request !== this.generation) return;
      this.apply(outcome, afterExclusive);
    });
  }

  private apply(outcome: CatalogueObservation, afterExclusive: string | null): void {
    if (outcome.kind === 'page') {
      if (afterExclusive !== null && outcome.page.items.some(item => item.productId <= afterExclusive)) {
        this.page.set(null);
        this.state.set('error');
      } else {
        this.page.set(outcome.page.items.length === 0 ? null : outcome.page);
        this.state.set(outcome.page.items.length === 0 ? 'empty' : 'ready');
      }
    } else {
      this.page.set(null);
      this.state.set(outcome.kind);
    }
    if (this.focusAfterLoad) queueMicrotask(() => this.heading?.nativeElement.focus());
    this.focusAfterLoad = false;
  }
}
