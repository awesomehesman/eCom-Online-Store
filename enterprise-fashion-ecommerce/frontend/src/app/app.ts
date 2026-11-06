import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { StorefrontShell } from './storefront/storefront-shell';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, StorefrontShell],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-shell #shell>
      <router-outlet (activate)="shell.focusMain()" />
    </app-storefront-shell>
  `,
})
export class App {}
