import { ChangeDetectionStrategy, Component, DestroyRef, ElementRef, ViewChild, effect, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';

import { ProductSummaryListComponent } from '../../components/product-summary-list/product-summary-list.component';
import { StorefrontPageStateComponent } from '../../../../shell/storefront-page-state.component';
import { CatalogueStore } from '../../state/catalogue.store';

@Component({
  selector: 'app-catalogue-page',
  standalone: true,
  imports: [StorefrontPageStateComponent, ProductSummaryListComponent],
  providers: [CatalogueStore],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './catalogue-page.component.html',
  styleUrl: './catalogue-page.component.scss',
})
export class CataloguePageComponent {
  @ViewChild('catalogueHeading') private heading?: ElementRef<HTMLElement>;

  readonly store = inject(CatalogueStore);
  protected readonly announce = signal(false);

  constructor() {
    // Track navigation events for announcement timing
    inject(Router).events.pipe(takeUntilDestroyed()).subscribe(event => {
      if (event instanceof NavigationStart || event instanceof NavigationError) {
        this.announce.set(false);
      }
      if (event instanceof NavigationEnd || event instanceof NavigationCancel) {
        this.announce.set(true);
      }
    });

    // Clean up store subscriptions on destroy
    inject(DestroyRef).onDestroy(() => {
      this.store.cleanup();
    });

    // Handle focus after load
    effect(() => {
      if (this.store.focusAfterLoad()) {
        queueMicrotask(() => {
          this.heading?.nativeElement.focus();
          this.store.clearFocusAfterLoad();
        });
      }
    });

    // Initial load
    this.store.recheck();
  }

  protected recheck(): void {
    this.store.recheck();
  }

  protected nextPage(): void {
    this.store.loadNextPage();
  }
}
