import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/** Presentation projection, not the complete Product wire schema or visibility evidence.
 * Family is required on the wire; callers may omit it from this summary projection.
 * Input must originate from separately validated, eligible Product evidence.
 */
export interface ProductSummary {
  readonly name: string;
  readonly description: string;
  readonly family?: 'Apparel' | 'Footwear' | 'Accessories';
}

@Component({
  selector: 'app-product-summary',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <article>
      <h3>{{ product().name }}</h3>
      <p>{{ product().description }}</p>
      @if (product().family; as family) {
        <p>Product family: {{ family }}</p>
      }
    </article>
  `,
  styles: `
    :host { display: block; min-inline-size: 0; overflow-wrap: anywhere; }
  `,
})
export class ProductSummaryComponent {
  readonly product = input.required<ProductSummary>();
}
