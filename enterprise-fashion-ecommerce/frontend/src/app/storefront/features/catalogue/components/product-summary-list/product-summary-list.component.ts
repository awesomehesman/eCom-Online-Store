import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import type { ValidatedCataloguePage } from '../../../../../data-access/catalogue/catalogue-contract';
import { ProductSummaryComponent } from '../product-summary/product-summary.component';

/** Contract-validated presentation beneath a caller-owned catalogue h2. */
@Component({
  selector: 'app-product-summary-list',
  standalone: true,
  imports: [ProductSummaryComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './product-summary-list.component.html',
  styleUrl: './product-summary-list.component.scss',
})
export class ProductSummaryListComponent {
  readonly page = input.required<ValidatedCataloguePage>();
}
