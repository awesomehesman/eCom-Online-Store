import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a class="skip-link" href="#main-content">Skip to main content</a>
    <header><p>Storefront</p></header>
    <main #mainContent id="main-content" tabindex="-1">
      <router-outlet (activate)="mainContent.focus()" />
    </main>
  `,
})
export class App {}
