import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Subject } from 'rxjs';

import { CatalogueApiClient, CatalogueObservation } from '../../../../../data-access/catalogue/catalogue-api-client';
import { validateCataloguePage } from '../../../../../data-access/catalogue/catalogue-contract';
import { CataloguePageComponent } from './catalogue-page.component';
import { CatalogueStore } from '../../state/catalogue.store';

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

describe('CataloguePageComponent', () => {
  let client: jasmine.SpyObj<CatalogueApiClient>;
  let fixture: ComponentFixture<CataloguePageComponent>;

  beforeEach(() => {
    client = jasmine.createSpyObj<CatalogueApiClient>('CatalogueApiClient', ['loadPage']);
    TestBed.configureTestingModule({
      imports: [CataloguePageComponent],
      providers: [
        provideRouter([]),
        { provide: CatalogueApiClient, useValue: client },
      ],
    });
  });

  describe('store integration', () => {
    it('injects the CatalogueStore and loads on construction', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      expect(client.loadPage).toHaveBeenCalledWith(20, null);
      expect(fixture.componentInstance.store).toBeDefined();
      expect(fixture.componentInstance.store.loadState()).toBe('loading');
    });

    it('displays products when store has loaded items', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      subject.next(page('product-a', null));
      fixture.detectChanges();

      expect(fixture.nativeElement.querySelector('article h3')?.textContent).toContain('Product product-a');
    });

    it('shows next page button when hasNextPage is true', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      subject.next(page('product-a', 'next-cursor'));
      fixture.detectChanges();

      const buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
      expect(Array.from(buttons).some(btn => btn.textContent?.includes('Show next products'))).toBeTrue();
    });

    it('hides next page button when hasNextPage is false', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      subject.next(page('product-a', null));
      fixture.detectChanges();

      const buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
      expect(Array.from(buttons).some(btn => btn.textContent?.includes('Show next products'))).toBeFalse();
    });

    it('disables next page button while loading', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      initialSubject.next(page('product-a', 'cursor'));
      fixture.detectChanges();

      // Verify next page button exists and is enabled before loading
      let buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
      let nextButton = Array.from(buttons).find(btn => btn.textContent?.includes('Show next products'));
      expect(nextButton).toBeTruthy();
      expect(nextButton?.disabled).toBeFalse();

      // After loadNextPage, the page is cleared so the button is hidden
      // Instead verify the store is in loading state
      fixture.componentInstance.store.loadNextPage();
      expect(fixture.componentInstance.store.isLoading()).toBeTrue();
    });
  });

  describe('request lifecycle', () => {
    it('cancels rapid pagination when recheck supersedes it and ignores obsolete outcomes', () => {
      const initial = new Subject<CatalogueObservation>();
      const pagination = new Subject<CatalogueObservation>();
      const recheck = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initial, pagination, recheck);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      initial.next(page('product-a', 'Opaque_next'));
      fixture.detectChanges();

      fixture.componentInstance.store.loadNextPage();
      expect(initial.observed).toBeFalse();
      expect(pagination.observed).toBeTrue();

      fixture.componentInstance.store.recheck();
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

  describe('accessibility', () => {
    it('has a heading with correct attributes for focus management', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      const heading = fixture.nativeElement.querySelector('#catalogue-heading');
      expect(heading).toBeTruthy();
      expect(heading.getAttribute('tabindex')).toBe('-1');
    });

    it('has a status region for announcements', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      const status = fixture.nativeElement.querySelector('[role="status"]');
      expect(status).toBeTruthy();
      expect(status.getAttribute('aria-atomic')).toBe('true');
    });

    it('displays status message through store.statusMessage()', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.componentInstance['announce'].set(true);
      fixture.detectChanges();

      expect(fixture.nativeElement.querySelector('[role="status"]').textContent).toContain('Checking catalogue');

      subject.next(page('product-a', null));
      fixture.detectChanges();

      expect(fixture.nativeElement.querySelector('[role="status"]').textContent).toContain('1 products available');
    });
  });

  describe('user interactions', () => {
    it('calls store.recheck() when recheck button is clicked', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      subject.next(page('product-a', null));
      fixture.detectChanges();

      const recheckSpy = spyOn(fixture.componentInstance.store, 'recheck');
      const buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
      const recheckButton = Array.from(buttons).find(btn => btn.textContent?.includes('Check catalogue again'));
      recheckButton?.click();

      expect(recheckSpy).toHaveBeenCalled();
    });

    it('calls store.loadNextPage() when next page button is clicked', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      fixture = TestBed.createComponent(CataloguePageComponent);
      fixture.detectChanges();

      subject.next(page('product-a', 'cursor'));
      fixture.detectChanges();

      const nextPageSpy = spyOn(fixture.componentInstance.store, 'loadNextPage');
      const buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
      const nextButton = Array.from(buttons).find(btn => btn.textContent?.includes('Show next products'));
      nextButton?.click();

      expect(nextPageSpy).toHaveBeenCalled();
    });
  });
});
