import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';

import { StorefrontPageStateComponent } from './storefront-page-state';
import { CatalogueObservation, observeCatalogue } from './catalogue-observation';

@Component({
  selector: 'app-storefront-entry',
  standalone: true,
  imports: [StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-page-state state="commerce-unavailable" />
    <section aria-labelledby="catalogue-heading">
      <h2 id="catalogue-heading">Catalogue</h2>
      <p role="status" aria-atomic="true">{{ announce() ? message() : '' }}</p>
      <button type="button" (click)="recheck()">Check catalogue again</button>
    </section>
  `,
})
export class StorefrontEntry {
  protected readonly state = signal<CatalogueObservation | 'loading'>('loading');
  protected readonly announce = signal(false);
  private active?: AbortController;
  private generation = 0;
  private destroyed = false;

  constructor() {
    inject(Router).events.pipe(takeUntilDestroyed()).subscribe(event => {
      if (event instanceof NavigationStart || event instanceof NavigationError) this.announce.set(false);
      if (event instanceof NavigationEnd || event instanceof NavigationCancel) this.announce.set(true);
    });
    inject(DestroyRef).onDestroy(() => {
      this.destroyed = true;
      this.active?.abort();
    });
    this.recheck();
  }

  protected message(): string {
    switch (this.state()) {
      case 'loading': return 'Checking catalogue…';
      case 'empty': return 'No products are currently available to browse.';
      case 'unsupported': return 'Product browsing is not supported here yet.';
      default: return 'Catalogue availability could not be confirmed. You can check again.';
    }
  }

  recheck(): void {
    if (this.destroyed) return;
    this.active?.abort();
    const request = ++this.generation;
    const controller = new AbortController();
    this.active = controller;
    this.state.set('loading');
    void observeCatalogue(controller.signal).then(outcome => {
      if (!this.destroyed && request === this.generation) this.state.set(outcome);
    });
  }
}
