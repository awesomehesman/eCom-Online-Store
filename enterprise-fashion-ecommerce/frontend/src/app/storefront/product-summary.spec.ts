import { TestBed } from '@angular/core/testing';
import { ProductSummary, ProductSummaryComponent, ProductSummaryMedia } from './product-summary';

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

const textSummary = { name: product.name, description: product.description, family: product.family };

describe('Isolated Product summary', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [ProductSummaryComponent] });
    // Suppress native image downloads in synthetic unit fixtures. Events are dispatched explicitly.
    const sources = new WeakMap<HTMLImageElement, string>();
    spyOnProperty(HTMLImageElement.prototype, 'src', 'set').and.callFake(function (this: HTMLImageElement, value: string) { sources.set(this, value); });
    spyOnProperty(HTMLImageElement.prototype, 'src', 'get').and.callFake(function (this: HTMLImageElement) { return sources.get(this) ?? ''; });
  });

  function render(summary: ProductSummary = textSummary) {
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
      const root: HTMLElement = render({ ...textSummary, family }).nativeElement;
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
    expect(render({ ...textSummary, name }).nativeElement.querySelector('h3').textContent).toBe(name);
  });

  it('preserves a long description without truncation', () => {
    const description = 'Long description. '.repeat(200);
    expect(render({ ...textSummary, description }).nativeElement.querySelector('p').textContent).toBe(description);
  });

  it('renders HTML-like names and descriptions literally', () => {
    const name = '<img src="/not-requested" onerror="alert(1)">';
    const description = '<script>alert(1)</script><strong>Literal description</strong>';
    const root: HTMLElement = render({ ...textSummary, name, description }).nativeElement;
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
    fixture.componentRef.setInput('product', { ...textSummary, name: 'Updated synthetic name' });
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
      fixture.componentRef.setInput('product', { ...textSummary, description: 'Updated synthetic description' });
      fixture.detectChanges();
      expect(document.activeElement).toBe(focusTarget);
      expect(fixture.nativeElement.querySelector('[role=status], [role=alert], [aria-live]')).toBeNull();
    } finally {
      focusTarget.remove();
    }
  });

  it('wraps long text in narrow containers under enlarged text and spacing', () => {
    const fixture = render({ ...textSummary, name: 'UnbrokenName'.repeat(100), description: 'UnbrokenDescription'.repeat(100) });
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
  it('renders Product-level media with the exact supplied alternative text', () => {
    const fixture = render(product);
    const image: HTMLImageElement = fixture.nativeElement.querySelector('img');
    expect(image.src).toBe(product.media[0].url);
    expect(image.alt).toBe(product.media[0].alternativeText);
    expect(image.referrerPolicy).toBe('no-referrer');
    image.dispatchEvent(new Event('load'));
    fixture.detectChanges();
    expect(image.style.visibility).toBe('visible');
  });

  it('retains Variant association and supplied order without sorting', () => {
    const media: ProductSummaryMedia[] = [
      { ...product.media[0], mediaId: 'z-last-label', variantId: product.variants[0].variantId, alternativeText: 'Synthetic Variant view' },
      { ...product.media[0], mediaId: 'a-first-label', alternativeText: 'Synthetic Product view' },
    ];
    const root: HTMLElement = render({ ...product, media }).nativeElement;
    expect(Array.from(root.querySelectorAll('img')).map(image => image.alt)).toEqual(media.map(item => item.alternativeText));
    expect(media[0].variantId).toBe(product.variants[0].variantId);
  });

  it('omits an association to a Variant not in the supplied Product', () => {
    expect(render({ ...product, media: [{ ...product.media[0], variantId: 'other-variant' }] }).nativeElement.querySelector('img')).toBeNull();
  });

  it('requires parent Product identity before rendering media', () => {
    expect(render({ ...textSummary, media: product.media }).nativeElement.querySelector('img')).toBeNull();
  });

  it('handles absent or empty media while keeping text', () => {
    for (const summary of [textSummary, { ...product, media: [] }]) {
      const root: HTMLElement = render(summary).nativeElement;
      expect(root.querySelector('img')).toBeNull();
      expect(root.querySelector('h3')?.textContent).toBe(product.name);
      expect(root.querySelector('p')?.textContent).toBe(product.description);
    }
  });

  for (const url of ['http://example.invalid/test.jpg', 'javascript:alert(1)', 'data:image/png;base64,AAAA',
    'https://', '/relative.jpg', 'https://user:secret@example.invalid/test.jpg',
    ' https://example.invalid/test.jpg', 'https://example.invalid/white space.jpg', 'https://example.invalid/\nimage.jpg']) {
    it(`omits invalid or unsafe URL ${JSON.stringify(url)}`, () => {
      const root: HTMLElement = render({ ...product, media: [{ ...product.media[0], url }] }).nativeElement;
      expect(root.querySelector('img')).toBeNull();
      expect(root.textContent).toContain(product.name);
    });
  }

  for (const alternativeText of [undefined, '', '   ']) {
    it(`does not fabricate missing/blank alternative text ${JSON.stringify(alternativeText)}`, () => {
      // Deliberately malformed boundary variation of an otherwise contract-shaped fixture.
      const media = [{ ...product.media[0], alternativeText }] as unknown as ProductSummaryMedia[];
      expect(render({ ...product, media }).nativeElement.querySelector('img')).toBeNull();
    });
  }

  it('removes failed imagery, retains the frame and text, and leaves other media usable', () => {
    const fixture = render({ ...product, media: [product.media[0], { ...product.media[0], mediaId: 'second-image' }] });
    const root: HTMLElement = fixture.nativeElement;
    const images = root.querySelectorAll('img');
    const frame = root.querySelector('figure')!;
    const height = frame.getBoundingClientRect().height;
    expect(images[0].style.visibility).toBe('hidden');
    images[0].dispatchEvent(new Event('error'));
    images[1].dispatchEvent(new Event('load'));
    fixture.detectChanges();
    expect(root.querySelectorAll('img').length).toBe(1);
    expect(root.querySelector('figcaption')?.textContent).toBe('Product image unavailable.');
    expect(frame.getBoundingClientRect().height).toBe(height);
    expect(root.querySelector('h3')?.textContent).toBe(product.name);
    expect(root.querySelector('p')?.textContent).toBe(product.description);
    expect(root.querySelector('a, button, [tabindex]')).toBeNull();
    expect(root.querySelector('img')?.style.visibility).toBe('visible');
  });

  it('does not transfer a failed image state to a replacement media reference', () => {
    const fixture = render(product);
    const obsolete: HTMLImageElement = fixture.nativeElement.querySelector('img');
    obsolete.dispatchEvent(new Event('error'));
    fixture.detectChanges();
    fixture.componentRef.setInput('product', { ...product, media: [{ ...product.media[0], url: 'https://example.invalid/replacement.jpg' }] });
    fixture.detectChanges();
    obsolete.dispatchEvent(new Event('error'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('img').src).toBe('https://example.invalid/replacement.jpg');
    expect(fixture.nativeElement.querySelector('figcaption')).toBeNull();
  });

  it('makes no component fetch/XHR calls when media load or fail', () => {
    const fetch = spyOn(window, 'fetch');
    const xhr = spyOn(XMLHttpRequest.prototype, 'open');
    const fixture = render(product);
    fixture.nativeElement.querySelector('img').dispatchEvent(new Event('error'));
    fixture.detectChanges();
    expect(fetch).not.toHaveBeenCalled();
    expect(xhr).not.toHaveBeenCalled();
  });

  it('keeps media frames within narrow layouts and preserves focus during failure', () => {
    const target = document.createElement('button');
    document.body.append(target);
    target.focus();
    try {
      const fixture = render(product);
      const root: HTMLElement = fixture.nativeElement;
      root.style.cssText = 'width: 320px; font-size: 200%; line-height: 1.5; letter-spacing: .12em; word-spacing: .16em;';
      expect(root.scrollWidth).toBeLessThanOrEqual(root.clientWidth);
      expect(document.activeElement).toBe(target);
      root.querySelector('img')!.dispatchEvent(new Event('error'));
      fixture.detectChanges();
      expect(root.scrollWidth).toBeLessThanOrEqual(root.clientWidth);
      expect(document.activeElement).toBe(target);
      expect(root.querySelector('[role=status], [role=alert], [aria-live]')).toBeNull();
    } finally {
      target.remove();
    }
  });

});

describe('Isolated Product media native browser decoding', () => {
  const blobs: string[] = [];

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [ProductSummaryComponent] });
    const nativeSource = Object.getOwnPropertyDescriptor(HTMLImageElement.prototype, 'src')!.set!;
    const valid = URL.createObjectURL(new Blob([
      '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="10"><rect width="20" height="10" fill="gray"/></svg>',
    ], { type: 'image/svg+xml' }));
    const invalid = URL.createObjectURL(new Blob(['invalid image bytes'], { type: 'image/png' }));
    blobs.push(valid, invalid);
    // Test-only transport interception: preserve native decoding/events, avoid external hosts.
    spyOnProperty(HTMLImageElement.prototype, 'src', 'set').and.callFake(function (this: HTMLImageElement, value: string) {
      if (value === product.media[0].url) nativeSource.call(this, valid);
      else if (value === 'https://example.invalid/failure.png') nativeSource.call(this, invalid);
      else throw new Error('Unexpected image request in isolated browser fixture');
    });
  });

  afterEach(() => {
    blobs.splice(0).forEach(url => URL.revokeObjectURL(url));
  });

  function outcome(image: HTMLImageElement): Promise<string> {
    return new Promise(resolve => {
      image.addEventListener('load', () => resolve('load'), { once: true });
      image.addEventListener('error', () => resolve('error'), { once: true });
    });
  }

  it('loads and displays a real decoded image with supplied alternative text', async () => {
    const fixture = TestBed.createComponent(ProductSummaryComponent);
    fixture.componentRef.setInput('product', product);
    fixture.detectChanges();
    const image: HTMLImageElement = fixture.nativeElement.querySelector('img');
    expect(image.style.visibility).toBe('hidden');
    expect(await outcome(image)).toBe('load');
    fixture.detectChanges();
    expect(image.complete).toBeTrue();
    expect(image.naturalWidth).toBe(20);
    expect(image.naturalHeight).toBe(10);
    expect(getComputedStyle(image).visibility).toBe('visible');
    expect(image.alt).toBe(product.media[0].alternativeText);
    expect(fixture.nativeElement.querySelector('figcaption')).toBeNull();
  });

  it('contains native decoding failure without broken imagery, lost text, focus or other media', async () => {
    const target = document.createElement('button');
    document.body.append(target);
    target.focus();
    try {
      const fixture = TestBed.createComponent(ProductSummaryComponent);
      fixture.componentRef.setInput('product', { ...product, media: [
        { ...product.media[0], mediaId: 'failed-image', url: 'https://example.invalid/failure.png' },
        product.media[0],
      ] });
      fixture.detectChanges();
      const root: HTMLElement = fixture.nativeElement;
      root.style.cssText = 'display:block;width:320px;max-width:100%;font-size:200%;line-height:1.5;letter-spacing:.12em;word-spacing:.16em';
      const images = root.querySelectorAll('img');
      const frame = root.querySelector('figure')!;
      const height = frame.getBoundingClientRect().height;
      expect(images[0].style.visibility).toBe('hidden');
      expect(await Promise.all(Array.from(images, outcome))).toEqual(['error', 'load']);
      fixture.detectChanges();
      expect(images[0].naturalWidth).toBe(0);
      expect(root.contains(images[0])).toBeFalse();
      expect(root.querySelectorAll('img').length).toBe(1);
      expect(images[1].naturalWidth).toBe(20);
      expect(getComputedStyle(images[1]).visibility).toBe('visible');
      expect(root.querySelector('figcaption')?.textContent).toBe('Product image unavailable.');
      expect(frame.getBoundingClientRect().height).toBe(height);
      expect(root.querySelector('h3')?.textContent).toBe(product.name);
      expect(root.querySelector('p')?.textContent).toBe(product.description);
      expect(root.scrollWidth).toBeLessThanOrEqual(root.clientWidth);
      expect(document.activeElement).toBe(target);
      expect(root.querySelector('a,button,[tabindex],[aria-live],[role=status],[role=alert]')).toBeNull();
    } finally {
      target.remove();
    }
  });
});
