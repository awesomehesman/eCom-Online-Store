import { ChangeDetectionStrategy, Component, computed, input, signal } from '@angular/core';

/** Presentation projection, not the complete Product wire schema or visibility evidence.
 * Family is required on the wire; callers may omit it from this summary projection.
 * Input must originate from separately validated, eligible Product evidence.
 */
export interface ProductSummary {
  readonly name: string;
  readonly description: string;
  readonly productId?: string;
  readonly variants?: readonly { readonly variantId: string }[];
  readonly media?: readonly ProductSummaryMedia[];
  readonly family?: 'Apparel' | 'Footwear' | 'Accessories';
}

/** Media fields mirror the Product catalogue wire Contract. */
export interface ProductSummaryMedia {
  readonly mediaId: string;
  readonly url: string;
  readonly alternativeText: string;
  readonly variantId?: string;
}

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

// Each immutable media reference owns its loading state. Replacing it creates a new view,
// so an obsolete load/error event cannot mark a replacement image as failed.
@Component({
  selector: 'app-product-summary-image',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <figure>
      @if (state() !== 'failed') {
        <img [src]="media().url" [alt]="media().alternativeText"
          referrerpolicy="no-referrer" decoding="async"
          [style.visibility]="state() === 'loaded' ? 'visible' : 'hidden'"
          (load)="state.set('loaded')" (error)="state.set('failed')" />
      } @else {
        <figcaption>Product image unavailable.</figcaption>
      }
    </figure>
  `,
  styles: `
    :host { display: block; min-inline-size: 0; }
    figure { margin: 0; aspect-ratio: 1; position: relative; overflow-wrap: anywhere; }
    img { display: block; position: absolute; width: 100%; height: 100%; object-fit: contain; }
  `,
})
class ProductSummaryImage {
  readonly media = input.required<ProductSummaryMedia>();
  protected readonly state = signal<'loading' | 'loaded' | 'failed'>('loading');
}

@Component({
  selector: 'app-product-summary',
  standalone: true,
  imports: [ProductSummaryImage],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <article>
      <h3>{{ product().name }}</h3>
      <p>{{ product().description }}</p>
      @if (product().family; as family) {
        <p>Product family: {{ family }}</p>
      }
      @for (media of images(); track media) {
        <app-product-summary-image [media]="media" />
      }
    </article>
  `,
  styles: `
    :host { display: block; min-inline-size: 0; overflow-wrap: anywhere; }
  `,
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
