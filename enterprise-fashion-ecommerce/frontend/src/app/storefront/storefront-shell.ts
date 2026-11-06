import { ChangeDetectionStrategy, Component, ElementRef, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-storefront-shell',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a class="skip-link" href="#main-content">Skip to main content</a>
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
  @ViewChild('mainContent', { static: true })
  private mainContent!: ElementRef<HTMLElement>;

  focusMain(): void {
    this.mainContent.nativeElement.focus();
  }
}
