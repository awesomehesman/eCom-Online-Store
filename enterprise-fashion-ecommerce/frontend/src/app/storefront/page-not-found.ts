import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { StorefrontPageStateComponent } from './storefront-page-state';

@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [RouterLink, StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-page-state state="invalid-destination">
      <a routerLink="/">Return to the storefront</a>
    </app-storefront-page-state>
  `,
})
export class PageNotFound {}
