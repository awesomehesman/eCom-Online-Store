import { ChangeDetectionStrategy, Component, ElementRef, Signal, ViewChild } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-storefront-shell',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './storefront-shell.component.html',
  styleUrl: './storefront-shell.component.scss',
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
