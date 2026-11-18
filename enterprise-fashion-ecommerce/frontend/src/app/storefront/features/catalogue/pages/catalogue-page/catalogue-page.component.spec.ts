import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Subject } from 'rxjs';

import { CatalogueApiClient, CatalogueObservation } from '../../../../../data-access/catalogue/catalogue-api-client';
import { validateCataloguePage } from '../../../../../data-access/catalogue/catalogue-contract';
import { CataloguePageComponent } from './catalogue-page.component';

function page(productId: string, nextCursor: string | null): CatalogueObservation {
  return {
    kind: 'page',
    page: validateCataloguePage({
      items: [{
        productId,
        name: `Product ${productId}`,
        description: 'A validated Catalogue Product.',
        family: 'Accessories',
        attributeStandardVersion: '1.0.0',
        variantDefiningAttributes: [],
        variants: [],
        media: [],
      }],
      pageSize: 20,
      nextCursor,
    })!,
  };
}

describe('Catalogue page request lifecycle', () => {
  let client: jasmine.SpyObj<CatalogueApiClient>;
  let fixture: ComponentFixture<CataloguePageComponent>;

  beforeEach(() => {
    client = jasmine.createSpyObj<CatalogueApiClient>('CatalogueApiClient', ['loadPage']);
    TestBed.configureTestingModule({
      imports: [CataloguePageComponent],
      providers: [provideRouter([]), { provide: CatalogueApiClient, useValue: client }],
    });
  });

  it('cancels rapid pagination when recheck supersedes it and ignores obsolete outcomes', () => {
    const initial = new Subject<CatalogueObservation>();
    const pagination = new Subject<CatalogueObservation>();
    const recheck = new Subject<CatalogueObservation>();
    client.loadPage.and.returnValues(initial, pagination, recheck);
    fixture = TestBed.createComponent(CataloguePageComponent);
    fixture.detectChanges();

    initial.next(page('product-a', 'Opaque_next'));
    fixture.detectChanges();
    fixture.componentInstance.nextPage();
    expect(initial.observed).toBeFalse();
    expect(pagination.observed).toBeTrue();

    fixture.componentInstance.recheck();
    expect(pagination.observed).toBeFalse();
    expect(recheck.observed).toBeTrue();
    pagination.next({ kind: 'unavailable' });
    recheck.next(page('product-c', null));
    fixture.detectChanges();

    expect(client.loadPage.calls.allArgs()).toEqual([
      [20, null],
      [20, 'Opaque_next'],
      [20, null],
    ]);
    expect(fixture.nativeElement.querySelector('article h3')?.textContent).toContain('Product product-c');
    expect(fixture.nativeElement.textContent).not.toContain('could not be confirmed');
  });

  it('cancels its active request when destroyed', () => {
    const pending = new Subject<CatalogueObservation>();
    client.loadPage.and.returnValue(pending);
    fixture = TestBed.createComponent(CataloguePageComponent);
    fixture.detectChanges();
    expect(pending.observed).toBeTrue();
    fixture.destroy();
    expect(pending.observed).toBeFalse();
  });
});
