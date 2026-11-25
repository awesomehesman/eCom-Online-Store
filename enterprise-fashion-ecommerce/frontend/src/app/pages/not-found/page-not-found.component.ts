import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { StorefrontPageStateComponent } from '../../storefront/shell/storefront-page-state.component';

@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [RouterLink, StorefrontPageStateComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.scss',
})
export class PageNotFound {}
