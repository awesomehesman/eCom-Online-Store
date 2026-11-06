import { ChangeDetectionStrategy, Component } from '@angular/core';

import { StorefrontPageStateComponent } from './storefront-page-state';

@Component({
  selector: 'app-storefront-entry',
  standalone: true,
  imports: [StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <app-storefront-page-state state="commerce-unavailable" />
  `,
})
export class StorefrontEntry {}
