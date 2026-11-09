import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import type { ValidatedCataloguePage } from './catalogue-page';
import { ProductSummaryComponent } from './product-summary';

/** Isolated presentation beneath a caller-owned catalogue h2. Not imported by production routes. */
@Component({
  selector: 'app-product-summary-list',
  standalone: true,
  imports: [ProductSummaryComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul aria-label="Products">
      @for (product of page().items; track product.productId) {
        <li><app-product-summary [product]="product" /></li>
      }
    </ul>
    @if (page().nextCursor !== null) {
      <p>This page does not include all catalogue results.</p>
    }
  `,
  styles: `
    :host { display: block; min-inline-size: 0; overflow-wrap: anywhere; }
    ul { padding-inline-start: 1.5em; }
    li { min-inline-size: 0; }
  `,
})
export class ProductSummaryListComponent {
  readonly page = input.required<ValidatedCataloguePage>();
}
