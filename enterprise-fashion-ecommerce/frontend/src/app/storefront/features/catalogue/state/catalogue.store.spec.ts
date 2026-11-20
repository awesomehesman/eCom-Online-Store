import { TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs';

import { CatalogueApiClient, CatalogueObservation } from '../../../../data-access/catalogue/catalogue-api-client';
import { validateCataloguePage, ValidatedCataloguePage } from '../../../../data-access/catalogue/catalogue-contract';
import { CatalogueStore } from './catalogue.store';

function createValidPage(productId: string, nextCursor: string | null): ValidatedCataloguePage {
  return validateCataloguePage({
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
  })!;
}

function createEmptyPage(): ValidatedCataloguePage {
  return validateCataloguePage({
    items: [],
    pageSize: 20,
    nextCursor: null,
  })!;
}

function pageObservation(productId: string, nextCursor: string | null): CatalogueObservation {
  return { kind: 'page', page: createValidPage(productId, nextCursor) };
}

function emptyPageObservation(): CatalogueObservation {
  return { kind: 'page', page: createEmptyPage() };
}

describe('CatalogueStore', () => {
  let client: jasmine.SpyObj<CatalogueApiClient>;
  let store: InstanceType<typeof CatalogueStore>;

  beforeEach(() => {
    client = jasmine.createSpyObj<CatalogueApiClient>('CatalogueApiClient', ['loadPage']);
    TestBed.configureTestingModule({
      providers: [
        CatalogueStore,
        { provide: CatalogueApiClient, useValue: client },
      ],
    });
    store = TestBed.inject(CatalogueStore);
  });

  describe('initial state', () => {
    it('starts in idle state with no page', () => {
      expect(store.loadState()).toBe('idle');
      expect(store.page()).toBeNull();
      expect(store.isLoading()).toBeFalse();
      expect(store.hasItems()).toBeFalse();
      expect(store.hasNextPage()).toBeFalse();
      expect(store.itemCount()).toBe(0);
    });

    it('provides appropriate status message for idle state', () => {
      expect(store.statusMessage()).toBe('Checking catalogue…');
    });
  });

  describe('recheck()', () => {
    it('transitions to loading state immediately', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();

      expect(store.loadState()).toBe('loading');
      expect(store.isLoading()).toBeTrue();
      expect(store.page()).toBeNull();
      expect(client.loadPage).toHaveBeenCalledWith(20, null);
    });

    it('transitions to ready state on successful load', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next(pageObservation('product-a', 'next-cursor'));

      expect(store.loadState()).toBe('ready');
      expect(store.isLoading()).toBeFalse();
      expect(store.page()).not.toBeNull();
      expect(store.hasItems()).toBeTrue();
      expect(store.itemCount()).toBe(1);
      expect(store.hasNextPage()).toBeTrue();
      expect(store.statusMessage()).toBe('1 products available on this page.');
    });

    it('transitions to empty state when catalogue has no products', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next(emptyPageObservation());

      expect(store.loadState()).toBe('empty');
      expect(store.page()).toBeNull();
      expect(store.hasItems()).toBeFalse();
      expect(store.hasNextPage()).toBeFalse();
      expect(store.statusMessage()).toBe('No products are currently available to browse.');
    });

    it('transitions to unavailable state on 503 response', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next({ kind: 'unavailable' });

      expect(store.loadState()).toBe('unavailable');
      expect(store.page()).toBeNull();
      expect(store.statusMessage()).toBe('Catalogue availability could not be confirmed. You can check again.');
    });

    it('transitions to error state on invalid response', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next({ kind: 'error', failure: 'invalid-response' });

      expect(store.loadState()).toBe('error');
      expect(store.page()).toBeNull();
      expect(store.statusMessage()).toBe('The catalogue could not be loaded. You can try again.');
    });

    it('transitions to error state on network failure', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next({ kind: 'error', failure: 'network' });

      expect(store.loadState()).toBe('error');
    });

    it('transitions to error state on server failure', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next({ kind: 'error', failure: 'server' });

      expect(store.loadState()).toBe('error');
    });
  });

  describe('pagination', () => {
    it('loadNextPage() requests next cursor when available', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      store.recheck();
      initialSubject.next(pageObservation('product-a', 'Opaque_next'));

      store.loadNextPage();

      expect(client.loadPage).toHaveBeenCalledWith(20, 'Opaque_next');
      expect(store.loadState()).toBe('loading');
    });

    it('loadNextPage() does nothing when no next cursor', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next(pageObservation('product-a', null));

      store.loadNextPage();

      expect(client.loadPage).toHaveBeenCalledTimes(1);
    });

    it('loadNextPage() does nothing when no current page', () => {
      store.loadNextPage();
      expect(client.loadPage).not.toHaveBeenCalled();
    });

    it('validates page ordering on pagination', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      store.recheck();
      initialSubject.next(pageObservation('product-b', 'next'));

      store.loadNextPage();
      // Return a product ID that should come before the previous last item
      nextSubject.next(pageObservation('product-a', null));

      expect(store.loadState()).toBe('error');
      expect(store.page()).toBeNull();
    });

    it('accepts valid page ordering on pagination', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      store.recheck();
      initialSubject.next(pageObservation('product-a', 'next'));

      store.loadNextPage();
      nextSubject.next(pageObservation('product-b', null));

      expect(store.loadState()).toBe('ready');
      expect(store.page()?.items[0].productId).toBe('product-b');
    });

    it('sets focusAfterLoad on pagination', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      store.recheck();
      expect(store.focusAfterLoad()).toBeFalse();

      initialSubject.next(pageObservation('product-a', 'next'));
      store.loadNextPage();

      expect(store.focusAfterLoad()).toBeTrue();
    });
  });

  describe('request cancellation', () => {
    it('cancels previous request when recheck is called again', () => {
      const firstSubject = new Subject<CatalogueObservation>();
      const secondSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(firstSubject, secondSubject);

      store.recheck();
      expect(firstSubject.observed).toBeTrue();

      store.recheck();
      expect(firstSubject.observed).toBeFalse();
      expect(secondSubject.observed).toBeTrue();
    });

    it('cancels previous request when pagination starts during load', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const recheckSubject = new Subject<CatalogueObservation>();
      const paginationSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, recheckSubject, paginationSubject);

      store.recheck();
      initialSubject.next(pageObservation('product-a', 'next'));

      store.recheck();
      expect(recheckSubject.observed).toBeTrue();

      // Simulate page load then pagination
      recheckSubject.next(pageObservation('product-b', 'cursor'));
      store.loadNextPage();

      expect(recheckSubject.observed).toBeFalse();
      expect(paginationSubject.observed).toBeTrue();
    });

    it('automatically cancels and invalidates an active request when destroyed', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      expect(subject.observed).toBeTrue();
      const generationBeforeDestroy = store.generation();

      TestBed.resetTestingModule();

      expect(subject.observed).toBeFalse();
      expect(store.generation()).toBe(generationBeforeDestroy + 1);
    });

    it('rejects stale outcomes and state operations after destruction', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      const generationBeforeDestroy = store.generation();
      TestBed.resetTestingModule();
      const generationAfterDestroy = store.generation();

      subject.next(pageObservation('stale-product', null));
      store.recheck();
      store.loadNextPage();
      store.clearFocusAfterLoad();

      expect(generationAfterDestroy).toBe(generationBeforeDestroy + 1);
      expect(store.generation()).toBe(generationAfterDestroy);
      expect(store.loadState()).toBe('loading');
      expect(store.page()).toBeNull();
      expect(store.focusAfterLoad()).toBeFalse();
      expect(client.loadPage).toHaveBeenCalledTimes(1);
    });
  });

  describe('stale response prevention', () => {
    it('ignores responses from superseded requests', () => {
      const firstSubject = new Subject<CatalogueObservation>();
      const secondSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(firstSubject, secondSubject);

      store.recheck();
      store.recheck();

      // Complete the superseded first request
      firstSubject.next(pageObservation('stale-product', null));

      // State should still be loading because we're waiting for second request
      expect(store.loadState()).toBe('loading');

      // Complete the current request
      secondSubject.next(pageObservation('current-product', null));

      expect(store.loadState()).toBe('ready');
      expect(store.page()?.items[0].productId).toBe('current-product');
    });

    it('ignores unavailable responses from superseded requests', () => {
      const firstSubject = new Subject<CatalogueObservation>();
      const secondSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(firstSubject, secondSubject);

      store.recheck();
      store.recheck();

      firstSubject.next({ kind: 'unavailable' });
      expect(store.loadState()).toBe('loading');

      secondSubject.next(pageObservation('product-a', null));
      expect(store.loadState()).toBe('ready');
    });

    it('ignores error responses from superseded requests', () => {
      const firstSubject = new Subject<CatalogueObservation>();
      const secondSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(firstSubject, secondSubject);

      store.recheck();
      store.recheck();

      firstSubject.next({ kind: 'error', failure: 'network' });
      expect(store.loadState()).toBe('loading');

      secondSubject.next(pageObservation('product-a', null));
      expect(store.loadState()).toBe('ready');
    });
  });

  describe('clearFocusAfterLoad()', () => {
    it('clears the focusAfterLoad flag', () => {
      const initialSubject = new Subject<CatalogueObservation>();
      const nextSubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(initialSubject, nextSubject);

      store.recheck();
      initialSubject.next(pageObservation('product-a', 'next'));
      store.loadNextPage();

      expect(store.focusAfterLoad()).toBeTrue();

      store.clearFocusAfterLoad();

      expect(store.focusAfterLoad()).toBeFalse();
    });
  });

  describe('retry behavior', () => {
    it('allows retry after error', () => {
      const errorSubject = new Subject<CatalogueObservation>();
      const retrySubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(errorSubject, retrySubject);

      store.recheck();
      errorSubject.next({ kind: 'error', failure: 'network' });
      expect(store.loadState()).toBe('error');

      store.recheck();
      retrySubject.next(pageObservation('product-a', null));

      expect(store.loadState()).toBe('ready');
      expect(store.page()?.items[0].productId).toBe('product-a');
    });

    it('allows retry after unavailable', () => {
      const unavailableSubject = new Subject<CatalogueObservation>();
      const retrySubject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValues(unavailableSubject, retrySubject);

      store.recheck();
      unavailableSubject.next({ kind: 'unavailable' });
      expect(store.loadState()).toBe('unavailable');

      store.recheck();
      retrySubject.next(pageObservation('product-a', null));

      expect(store.loadState()).toBe('ready');
    });
  });

  describe('computed signals', () => {
    it('hasNextPage is false when page is null', () => {
      expect(store.hasNextPage()).toBeFalse();
    });

    it('hasNextPage is false when nextCursor is null', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next(pageObservation('product-a', null));

      expect(store.hasNextPage()).toBeFalse();
    });

    it('hasNextPage is true when nextCursor exists', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      store.recheck();
      subject.next(pageObservation('product-a', 'cursor'));

      expect(store.hasNextPage()).toBeTrue();
    });

    it('itemCount reflects actual item count', () => {
      const subject = new Subject<CatalogueObservation>();
      client.loadPage.and.returnValue(subject);

      expect(store.itemCount()).toBe(0);

      store.recheck();
      subject.next(pageObservation('product-a', null));

      expect(store.itemCount()).toBe(1);
    });
  });
});
