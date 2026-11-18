import { ChangeDetectionStrategy, Component, ViewChild, afterEveryRender, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NavigationCancel, NavigationEnd, NavigationError, NavigationSkipped,
  NavigationStart, Router, RouterOutlet, TitleStrategy,
} from '@angular/router';
import { Title } from '@angular/platform-browser';
import { StorefrontShell } from './storefront/shell/storefront-shell.component';
import { StorefrontPageStateComponent } from './storefront/shell/storefront-page-state.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, StorefrontShell, StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly navigation = signal<'ready' | 'loading' | 'failed'>('ready');
  protected readonly pageFailed = signal(false);
  private focusPending = false;
  private settledPageUrl?: string;
  @ViewChild(StorefrontShell) private shell?: StorefrontShell;

  constructor(router: Router, title: Title, titles: TitleStrategy) {
    // Run after the settled page and Router title are rendered, not at outlet activation.
    afterEveryRender(() => {
      if (this.focusPending) {
        this.focusPending = false;
        if (this.pageFailed()) title.setTitle('Page could not open');
        this.shell?.focusMain();
      }
    });
    router.events.pipe(takeUntilDestroyed()).subscribe(event => {
      if (event instanceof NavigationStart) {
        this.focusPending = false;
        this.navigation.set('loading');
        // Retain any failure page until recovery succeeds; never reveal stale content.
      } else if (event instanceof NavigationError) {
        this.navigation.set('failed');
        this.pageFailed.set(true);
        this.focusPending = true;
      } else if (event instanceof NavigationEnd) {
        const pageUrl = event.urlAfterRedirects.split('#')[0];
        const meaningfulChange = this.pageFailed() || pageUrl !== this.settledPageUrl;
        this.settledPageUrl = pageUrl;
        this.navigation.set('ready');
        this.pageFailed.set(false);
        // Native fragment links already manage their target focus; do not steal the next Tab.
        this.focusPending = meaningfulChange;
      } else if (event instanceof NavigationSkipped) {
        const recovering = this.pageFailed();
        this.navigation.set('ready');
        this.pageFailed.set(false);
        if (recovering) {
          titles.updateTitle(router.routerState.snapshot);
          this.focusPending = true;
        }
      } else if (event instanceof NavigationCancel) {
        this.navigation.set(this.pageFailed() ? 'failed' : 'ready');
        this.focusPending = false;
      }
    });
  }
}
