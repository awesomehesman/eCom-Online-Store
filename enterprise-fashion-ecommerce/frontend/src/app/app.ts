import { ChangeDetectionStrategy, Component, ViewChild, afterEveryRender, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NavigationCancel, NavigationEnd, NavigationError, NavigationSkipped,
  NavigationStart, Router, RouterOutlet, TitleStrategy,
} from '@angular/router';
import { Title } from '@angular/platform-browser';
import { StorefrontShell } from './storefront/storefront-shell';
import { StorefrontPageStateComponent } from './storefront/storefront-page-state';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, StorefrontShell, StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-shell>
      <p role="status" aria-atomic="true">{{ navigation() === 'loading' ? 'Opening page…' : '' }}</p>
      <p role="alert" aria-atomic="true">{{ navigation() === 'failed' ? 'The requested page could not open.' : '' }}</p>
      @if (pageFailed()) {
        <app-storefront-page-state state="navigation-failed" />
      }
      <div [hidden]="pageFailed()">
        <router-outlet />
      </div>
    </app-storefront-shell>
  `,
})
export class App {
  protected readonly navigation = signal<'ready' | 'loading' | 'failed'>('ready');
  protected readonly pageFailed = signal(false);
  private focusPending = false;
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
        this.navigation.set('ready');
        this.pageFailed.set(false);
        this.focusPending = true;
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
