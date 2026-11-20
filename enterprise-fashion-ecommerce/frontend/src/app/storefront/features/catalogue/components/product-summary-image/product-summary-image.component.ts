import { ChangeDetectionStrategy, Component, input, signal } from '@angular/core';

import { ProductSummaryMedia } from '../../models/product-summary';

// Each immutable media reference owns its loading state. Replacing it creates a new view,
// so an obsolete load/error event cannot mark a replacement image as failed.
@Component({
  selector: 'app-product-summary-image',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './product-summary-image.component.html',
  styleUrl: './product-summary-image.component.scss',
})
export class ProductSummaryImageComponent {
  readonly media = input.required<ProductSummaryMedia>();
  protected readonly state = signal<'loading' | 'loaded' | 'failed'>('loading');
}
