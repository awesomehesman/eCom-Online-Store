import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [RouterLink],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1>Page not available</h1>
    <p>This address does not have an available page.</p>
    <a routerLink="/">Return to the storefront</a>
  `,
})
export class PageNotFound {}
