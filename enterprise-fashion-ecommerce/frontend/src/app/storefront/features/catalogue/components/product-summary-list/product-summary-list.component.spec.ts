import { Component, signal } from '@angular/core';
import { Router, provideRouter } from '@angular/router';
import { App } from '../../../../../app';
import { routes } from '../../../../../app.routes';
import { TestBed } from '@angular/core/testing';
import { validateCataloguePage, ValidatedCataloguePage } from '../../../../../data-access/catalogue/catalogue-contract';
import { CatalogueApiClient, CatalogueObservation } from '../../../../../data-access/catalogue/catalogue-api-client';
import { ProductSummaryListComponent } from './product-summary-list.component';
import { from } from 'rxjs';

function fixturePage(media = false, nextCursor: string | null = null): ValidatedCataloguePage {
  return validateCataloguePage({ pageSize: 20, nextCursor, items: ['a', 'b'].map(id => ({
    productId: `product-${id}`, name: `Synthetic ${id} <em>literal</em>`, description: `Description ${id}`,
    family: 'Accessories', attributeStandardVersion: '1.0.0', variantDefiningAttributes: [],
    variants: [{ variantId: `variant-${id}`, attributes: {} }],
    media: media ? [{ mediaId: `image-${id}`, url: `https://example.invalid/${id}.svg`, alternativeText: `Synthetic ${id}`,
      ...(id === 'b' ? { variantId: 'variant-b' } : {}) }] : [],
  })) })!;
}
@Component({
  standalone: true, imports: [ProductSummaryListComponent],
  template: '<section aria-labelledby="test-catalogue"><h2 id="test-catalogue">Catalogue</h2><app-product-summary-list [page]="page()" /></section>',
})
class Harness {
  readonly page = signal(fixturePage());
}

describe('Isolated validated Product collection', () => {
  let blobs: string[];
  beforeEach(() => {
    const page = fixturePage();
    TestBed.configureTestingModule({ imports: [Harness, App], providers: [provideRouter(routes), {
      provide: CatalogueApiClient,
      useValue: { loadPage: () => from(Promise.resolve({ kind: 'page', page } as CatalogueObservation)) },
    }] });
    blobs = [];
  });
  afterEach(() => blobs.forEach(url => URL.revokeObjectURL(url)));
  function render(page = fixturePage()) {
    const fixture = TestBed.createComponent(Harness);
    fixture.componentInstance.page.set(page); fixture.detectChanges(); return fixture;
  }
  it('renders a valid positive wire response in the production entry', async () => {
    const page = fixturePage();
    const fixture = TestBed.createComponent(App); fixture.detectChanges();
    await TestBed.inject(Router).navigateByUrl('/');
    await fixture.whenStable(); fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain(page.items[0].name);
    expect(fixture.nativeElement.querySelectorAll('app-product-summary-list app-product-summary').length).toBe(2);
    expect(fixture.nativeElement.querySelector('section [role=status]').textContent).toContain('2 products available');
  });
  it('renders an ordered semantic collection beneath the caller catalogue heading', () => {
    const root: HTMLElement = render().nativeElement;
    expect(root.querySelector('ul')?.getAttribute('aria-label')).toBe('Products');
    expect(root.querySelectorAll('ul > li').length).toBe(2);
    expect(Array.from(root.querySelectorAll('h2,h3'), node => node.tagName)).toEqual(['H2', 'H3', 'H3']);
    expect(Array.from(root.querySelectorAll('h3'), node => node.textContent)).toEqual(fixturePage().items.map(item => item.name));
    expect(root.querySelector('em')).toBeNull();
    expect(Array.from(root.querySelectorAll('a'), link => link.getAttribute('href')))
      .toEqual(['/products/product-a', '/products/product-b']);
    expect(root.querySelector('button,input,[tabindex],[role=status],[role=alert],[aria-live]')).toBeNull();
    expect(root.textContent).not.toMatch(/price|stock|cart|purchase/i);
  });
  it('keeps Product DOM identity stable across validated content replacement', () => {
    const fixture = render(); const first = fixture.nativeElement.querySelector('li');
    fixture.componentInstance.page.set(fixturePage()); fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('li')).toBe(first);
  });
  it('qualifies a continuation without inventing a total or traversal control', () => {
    const root: HTMLElement = render(fixturePage(false, 'opaque-next')).nativeElement;
    expect(root.textContent).toContain('This page does not include all catalogue results.');
    expect(root.textContent).not.toContain('opaque-next');
    expect(root.querySelector('button')).toBeNull();
    expect(Array.from(root.querySelectorAll('a'), link => link.getAttribute('href')))
      .toEqual(['/products/product-a', '/products/product-b']);
  });
  it('does not fabricate empty or successful commerce messaging for an empty page', () => {
    const root: HTMLElement = render(validateCataloguePage({ items: [], pageSize: 20, nextCursor: null })!).nativeElement;
    expect(root.querySelectorAll('li').length).toBe(0);
    expect(root.textContent?.trim()).toBe('Catalogue');
  });
  it('contains long text at narrow widths and preserves focus without API calls', () => {
    const fetch = spyOn(window, 'fetch'); const xhr = spyOn(XMLHttpRequest.prototype, 'open');
    const button = document.createElement('button'); document.body.append(button); button.focus();
    try {
      const source = fixturePage();
      const page = validateCataloguePage({ items: source.items.map(item => ({ ...item, name: 'Long'.repeat(200), description: 'Text '.repeat(500) })), pageSize: 20, nextCursor: null })!;
      const fixture = render(page); const root: HTMLElement = fixture.nativeElement;
      root.style.cssText = 'display:block;width:320px;font-size:200%;line-height:1.5;letter-spacing:.12em;word-spacing:.16em';
      expect(root.scrollWidth).toBeLessThanOrEqual(root.clientWidth);
      expect(document.activeElement).toBe(button);
      expect(fetch).not.toHaveBeenCalled(); expect(xhr).not.toHaveBeenCalled();
    } finally { button.remove(); }
  });
  it('integrates native image success/failure without losing other Products, imagery or focus', async () => {
    const nativeSource = Object.getOwnPropertyDescriptor(HTMLImageElement.prototype, 'src')!.set!;
    const valid = URL.createObjectURL(new Blob(['<svg xmlns="http://www.w3.org/2000/svg" width="20" height="10"><rect width="20" height="10"/></svg>'], { type: 'image/svg+xml' }));
    const invalid = URL.createObjectURL(new Blob(['invalid image bytes'], { type: 'image/png' }));
    blobs.push(valid, invalid);
    spyOnProperty(HTMLImageElement.prototype, 'src', 'set').and.callFake(function (this: HTMLImageElement, url: string) {
      if (url === 'https://example.invalid/a.svg') nativeSource.call(this, invalid);
      else if (url === 'https://example.invalid/b.svg') nativeSource.call(this, valid);
      else throw new Error('Unexpected fixture image');
    });
    const button = document.createElement('button'); document.body.append(button); button.focus();
    try {
      const fixture = render(fixturePage(true)); const root: HTMLElement = fixture.nativeElement;
      const images = root.querySelectorAll('img');
      const outcomes = Array.from(images, image => new Promise<string>(resolve => {
        image.addEventListener('load', () => resolve('load'), { once: true });
        image.addEventListener('error', () => resolve('error'), { once: true });
      }));
      expect(images[0].style.visibility).toBe('hidden');
      expect(await Promise.all(outcomes)).toEqual(['error', 'load']); fixture.detectChanges();
      expect(root.contains(images[0])).toBeFalse();
      expect(root.querySelector('figcaption')?.textContent).toBe('Product image unavailable.');
      expect(images[1].naturalWidth).toBe(20); expect(getComputedStyle(images[1]).visibility).toBe('visible');
      expect(images[1].alt).toBe('Synthetic b');
      expect(root.querySelectorAll('article').length).toBe(2);
      expect(root.textContent).toContain('Description a'); expect(root.textContent).toContain('Description b');
      expect(document.activeElement).toBe(button);
    } finally { button.remove(); }
  });
});
