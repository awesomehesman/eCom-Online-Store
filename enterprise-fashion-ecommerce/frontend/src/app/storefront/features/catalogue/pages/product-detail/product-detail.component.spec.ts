import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { CatalogueApiClient, ProductObservation } from '../../../../../data-access/catalogue/catalogue-api-client';
import { validateCatalogueProduct } from '../../../../../data-access/catalogue/catalogue-contract';
import { ProductDetailComponent } from './product-detail.component';

describe('Product detail', () => {
  it('announces loading, supports variant media selection and cancels on navigation', () => {
    const route = new BehaviorSubject(convertToParamMap({ productId: 'shirt' }));
    const response = new Subject<ProductObservation>();
    const next = new Subject<ProductObservation>();
    const loadProduct = jasmine.createSpy('loadProduct').and.returnValues(response, next);
    TestBed.configureTestingModule({ providers: [provideRouter([]),
      { provide: ActivatedRoute, useValue: { paramMap: route } },
      { provide: CatalogueApiClient, useValue: { loadProduct } },
    ] });
    const fixture = TestBed.createComponent(ProductDetailComponent);
    fixture.detectChanges();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('[role=status]')?.textContent).toContain('Loading');
    const product = validateCatalogueProduct({
      productId: 'shirt', name: 'Shirt', description: 'Description', family: 'Apparel',
      attributeStandardVersion: '1.0.0', variantDefiningAttributes: ['Size'],
      variants: [{ variantId: 'medium', attributes: { size: { kind: 'APPAREL', code: 'M', label: 'Medium' } } }],
      media: [
        { mediaId: 'front', url: 'https://example.invalid/front', alternativeText: 'Front' },
        { mediaId: 'back', url: 'https://example.invalid/back', alternativeText: 'Back', variantId: 'medium' },
      ],
    })!;
    response.next({ kind: 'product', product });
    fixture.detectChanges();
    expect(root.querySelector('h1')?.textContent).toBe('Shirt');
    expect(root.querySelectorAll('app-product-summary-image').length).toBe(1);
    const select = root.querySelector('select')!;
    select.value = 'medium';
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();
    expect(root.querySelectorAll('app-product-summary-image').length).toBe(2);
    route.next(convertToParamMap({ productId: 'other' }));
    expect(response.observed).toBeFalse();
    next.next({ kind: 'not-found' });
    fixture.detectChanges();
    expect(root.querySelector('[role=status]')?.textContent).toContain('Product not found');
    expect(root.querySelectorAll('app-product-summary-image').length).toBe(0);
  });
});
