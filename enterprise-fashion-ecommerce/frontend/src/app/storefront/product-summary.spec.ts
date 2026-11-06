import { TestBed } from '@angular/core/testing';
import { ProductSummary, ProductSummaryComponent } from './product-summary';

// Full synthetic wire representation. No fixture is exported to production code.
const product = {
  productId: 'synthetic-accessory',
  name: 'Synthetic accessory',
  description: 'Synthetic description for presentation verification.',
  family: 'Accessories' as const,
  attributeStandardVersion: '1.0.0',
  variantDefiningAttributes: [],
  variants: [{ variantId: 'synthetic-variant', attributes: {} }],
  media: [{ mediaId: 'synthetic-media', url: 'https://example.invalid/test.jpg', alternativeText: 'Synthetic accessory illustration' }],
};

describe('Isolated Product summary', () => {
  beforeEach(() => TestBed.configureTestingModule({ imports: [ProductSummaryComponent] }));

  function render(summary: ProductSummary = product) {
    const fixture = TestBed.createComponent(ProductSummaryComponent);
    fixture.componentRef.setInput('product', summary);
    fixture.detectChanges();
    return fixture;
  }

  it('renders the supplied Product name beneath a catalogue section heading', () => {
    const root: HTMLElement = render().nativeElement;
    expect(root.querySelector('article h3')?.textContent).toBe(product.name);
    expect(root.querySelector('h1, h2')).toBeNull();
  });

  it('renders the supplied description', () => {
    expect(render().nativeElement.querySelector('article p').textContent).toBe(product.description);
  });

  for (const family of ['Apparel', 'Footwear', 'Accessories'] as const) {
    it(`can present ${family} context`, () => {
      const root: HTMLElement = render({ ...product, family }).nativeElement;
      expect(root.textContent).toContain(`Product family: ${family}`);
    });
  }

  it('allows family to be omitted from the presentation projection', () => {
    const root: HTMLElement = render({ name: product.name, description: product.description }).nativeElement;
    expect(root.textContent).not.toContain('Product family:');
    expect(root.querySelectorAll('p').length).toBe(1);
  });

  it('preserves a long Product name without truncation', () => {
    const name = 'LongProductName'.repeat(100);
    expect(render({ ...product, name }).nativeElement.querySelector('h3').textContent).toBe(name);
  });

  it('preserves a long description without truncation', () => {
    const description = 'Long description. '.repeat(200);
    expect(render({ ...product, description }).nativeElement.querySelector('p').textContent).toBe(description);
  });

  it('renders HTML-like names and descriptions literally', () => {
    const name = '<img src="/not-requested" onerror="alert(1)">';
    const description = '<script>alert(1)</script><strong>Literal description</strong>';
    const root: HTMLElement = render({ ...product, name, description }).nativeElement;
    expect(root.querySelector('h3')?.textContent).toBe(name);
    expect(root.querySelector('p')?.textContent).toBe(description);
    expect(root.querySelector('img, script, strong')).toBeNull();
  });

  it('has no links, controls, media or unsupported commercial fields', () => {
    const root: HTMLElement = render().nativeElement;
    expect(root.querySelector('a, button, input, select, form, img, video, iframe, [tabindex]')).toBeNull();
    expect(root.textContent).not.toMatch(/price|in stock|available to buy|add to cart|checkout/i);
    expect(root.textContent).not.toContain(product.productId);
    expect(root.textContent).not.toContain(product.attributeStandardVersion);
  });

  it('makes no fetch or XMLHttpRequest requests during rendering or updates', () => {
    const fetch = spyOn(window, 'fetch');
    const xhr = spyOn(XMLHttpRequest.prototype, 'open');
    const fixture = render();
    fixture.componentRef.setInput('product', { ...product, name: 'Updated synthetic name' });
    fixture.detectChanges();
    expect(fetch).not.toHaveBeenCalled();
    expect(xhr).not.toHaveBeenCalled();
  });

  it('does not move focus when rendering or updating', () => {
    const focusTarget = document.createElement('button');
    document.body.append(focusTarget);
    focusTarget.focus();
    try {
      const fixture = render();
      expect(document.activeElement).toBe(focusTarget);
      fixture.componentRef.setInput('product', { ...product, description: 'Updated synthetic description' });
      fixture.detectChanges();
      expect(document.activeElement).toBe(focusTarget);
      expect(fixture.nativeElement.querySelector('[role=status], [role=alert], [aria-live]')).toBeNull();
    } finally {
      focusTarget.remove();
    }
  });

  it('wraps long text in narrow containers under enlarged text and spacing', () => {
    const fixture = render({ ...product, name: 'UnbrokenName'.repeat(100), description: 'UnbrokenDescription'.repeat(100) });
    const root: HTMLElement = fixture.nativeElement;
    // Actual Chrome layout checks; enlarged text is not a claim of browser zoom testing.
    for (const width of [320, 640]) {
      root.style.cssText = `width: ${width}px; font-size: 200%; line-height: 1.5; letter-spacing: .12em; word-spacing: .16em;`;
      expect(root.scrollWidth).toBeLessThanOrEqual(root.clientWidth);
      for (const element of Array.from(root.querySelectorAll('h3, p'))) {
        const bounds = element.getBoundingClientRect();
        expect(bounds.width).toBeGreaterThan(0);
        expect(bounds.right).toBeLessThanOrEqual(root.getBoundingClientRect().right + 1);
      }
    }
  });
});
