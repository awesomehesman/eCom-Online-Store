import { ChangeDetectionStrategy, Component, ElementRef, computed, inject, signal, viewChild } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subject, distinctUntilChanged, map, startWith, switchMap, tap } from 'rxjs';
import { CatalogueApiClient } from '../../../../../data-access/catalogue/catalogue-api-client';
import { ProductSummaryImageComponent } from '../../components/product-summary-image/product-summary-image.component';

@Component({
  standalone: true,
  imports: [RouterLink, ProductSummaryImageComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss',
})
export class ProductDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly api = inject(CatalogueApiClient);
  private readonly reload = new Subject<void>();
  private readonly heading = viewChild.required<ElementRef<HTMLHeadingElement>>('heading');
  protected readonly selection = signal<{ id: string; variantId: string } | null>(null);
  protected readonly state = toSignal(this.route.paramMap.pipe(
    map(params => params.get('productId') ?? ''),
    distinctUntilChanged(),
    tap(() => this.selection.set(null)),
    switchMap(id => this.reload.pipe(startWith(undefined), switchMap(() => this.api.loadProduct(id).pipe(
      startWith({ kind: 'loading' } as const),
    )))),
  ), { initialValue: { kind: 'loading' } as const });
  protected readonly product = computed(() => {
    const state = this.state();
    return state.kind === 'product' ? state.product : null;
  });
  protected readonly media = computed(() => {
    const product = this.product();
    const selected = this.selection();
    return product?.media.filter(image => image.variantId === undefined
      || (selected?.id === product.productId && image.variantId === selected.variantId)) ?? [];
  });

  protected retry(): void {
    this.heading().nativeElement.focus();
    this.reload.next();
  }

  protected select(variantId: string): void {
    const product = this.product();
    this.selection.set(product && variantId ? { id: product.productId, variantId } : null);
  }
}
