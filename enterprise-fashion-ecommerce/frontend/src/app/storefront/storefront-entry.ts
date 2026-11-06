import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-storefront-entry',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1>Welcome to the storefront</h1>
    <p>Product browsing and purchasing are not available here yet.</p>
  `,
})
export class StorefrontEntry {}
