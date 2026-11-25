import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { ProductSummary, ProductSummaryMedia } from '../../models/product-summary';
import { ProductSummaryImageComponent } from '../product-summary-image/product-summary-image.component';

export type { ProductSummary, ProductSummaryMedia } from '../../models/product-summary';

const reference = /^[A-Za-z0-9_-]{1,128}$/;

function safeMedia(media: ProductSummaryMedia, product: ProductSummary): boolean {
  if (!media || typeof media.mediaId !== 'string' || !reference.test(media.mediaId) || typeof media.alternativeText !== 'string'
    || !media.alternativeText.trim() || typeof media.url !== 'string'
    || !media.url.startsWith('https://') || /[\s\\\u0000-\u0020\u007f]/.test(media.url)) return false;
  if (media.variantId !== undefined && (typeof media.variantId !== 'string' || !reference.test(media.variantId)
    || !Array.isArray(product.variants) || product.variants.filter(variant => variant.variantId === media.variantId).length !== 1)) return false;
  try {
    const url = new URL(media.url);
    return url.protocol === 'https:' && !!url.hostname && !url.username && !url.password;
  } catch {
    return false;
  }
}

@Component({
  selector: 'app-product-summary',
  standalone: true,
  imports: [ProductSummaryImageComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './product-summary.component.html',
  styleUrl: './product-summary.component.scss',
})
export class ProductSummaryComponent {
  readonly product = input.required<ProductSummary>();
  protected readonly images = computed(() => {
    const product = this.product();
    if (typeof product.productId !== 'string' || !reference.test(product.productId)
      || !Array.isArray(product.media)) return [];
    return product.media.filter(media => safeMedia(media, product)
      && product.media!.filter(other => other?.mediaId === media.mediaId).length === 1);
  });
}
