import { ChangeDetectionStrategy, Component, ElementRef, Signal, ViewChild } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-storefront-shell',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a class="skip-link" [href]="skipDestination()">Skip to main content</a>
    <header>
      <nav aria-label="Storefront">
        <a routerLink="/" routerLinkActive="current-location"
           [routerLinkActiveOptions]="{ exact: true }" ariaCurrentWhenActive="page">Storefront</a>
      </nav>
    </header>
    <main #mainContent id="main-content" tabindex="-1">
      <ng-content />
    </main>
  `,
})
export class StorefrontShell {
  protected readonly skipDestination: Signal<string>;

  constructor(router: Router) {
    // A fragment-only href resolves against <base href="/">, not a nested page.
    // Observe settled URLs so this OnPush shell updates without custom key handlers.
    this.skipDestination = toSignal(router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map(event => event.urlAfterRedirects.split('#')[0] + '#main-content'),
    ), { initialValue: router.url.split('#')[0] + '#main-content' });
  }

  @ViewChild('mainContent', { static: true })
  private mainContent!: ElementRef<HTMLElement>;

  focusMain(): void {
    this.mainContent.nativeElement.focus();
  }
}
