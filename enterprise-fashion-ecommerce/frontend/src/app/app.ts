import { ChangeDetectionStrategy, Component, ViewChild, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NavigationCancel, NavigationEnd, NavigationError, NavigationSkipped,
  NavigationStart, Router, RouterOutlet,
} from '@angular/router';
import { StorefrontShell } from './storefront/storefront-shell';
import { StorefrontPageStateComponent } from './storefront/storefront-page-state';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, StorefrontShell, StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-shell #shell>
      <p role="status" aria-atomic="true">{{ navigation() === 'loading' ? 'Opening page…' : '' }}</p>
      <p role="alert" aria-atomic="true">{{ navigation() === 'failed' ? 'The requested page could not open.' : '' }}</p>
      @if (navigation() === 'failed') {
        <app-storefront-page-state state="navigation-failed" />
      }
      <div [hidden]="navigation() === 'failed'">
        <router-outlet (activate)="shell.focusMain()" />
      </div>
    </app-storefront-shell>
  `,
})
export class App {
  protected readonly navigation = signal<'ready' | 'loading' | 'failed'>('ready');
  @ViewChild(StorefrontShell) private shell?: StorefrontShell;

  constructor(router: Router) {
    router.events.pipe(takeUntilDestroyed()).subscribe(event => {
      if (event instanceof NavigationStart) {
        this.navigation.set('loading');
      } else if (event instanceof NavigationError) {
        // A failure to open a page says nothing about catalogue or backend outcomes.
        // Keep the persistent shell usable; never expose the underlying exception.
        this.navigation.set('failed');
        this.shell?.focusMain();
      } else if (event instanceof NavigationEnd || event instanceof NavigationSkipped) {
        const recovering = this.navigation() === 'failed';
        this.navigation.set('ready');
        if (recovering) this.shell?.focusMain();
      } else if (event instanceof NavigationCancel) {
        this.navigation.set('ready');
      }
    });
  }
}
