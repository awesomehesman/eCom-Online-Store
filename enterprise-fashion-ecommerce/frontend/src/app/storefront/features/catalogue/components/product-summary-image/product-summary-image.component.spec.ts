import { TestBed } from '@angular/core/testing';

import { ProductSummaryImageComponent } from './product-summary-image.component';

describe('Product summary image', () => {
  const media = {
    mediaId: 'media-a',
    url: 'https://example.invalid/product.png',
    alternativeText: 'Black shirt',
  };

  it('renders the immutable media reference with accessible loading attributes', () => {
    const fixture = TestBed.createComponent(ProductSummaryImageComponent);
    fixture.componentRef.setInput('media', media);
    fixture.detectChanges();
    const image: HTMLImageElement = fixture.nativeElement.querySelector('img');
    expect(image.src).toBe(media.url);
    expect(image.alt).toBe(media.alternativeText);
    expect(image.referrerPolicy).toBe('no-referrer');
    expect(image.getAttribute('decoding')).toBe('async');
    expect(image.style.visibility).toBe('hidden');
  });

  it('shows the image after load and an accessible fallback after failure', () => {
    const fixture = TestBed.createComponent(ProductSummaryImageComponent);
    fixture.componentRef.setInput('media', media);
    fixture.detectChanges();
    let image: HTMLImageElement = fixture.nativeElement.querySelector('img');
    image.dispatchEvent(new Event('load'));
    fixture.detectChanges();
    expect(image.style.visibility).toBe('visible');
    image.dispatchEvent(new Event('error'));
    fixture.detectChanges();
    image = fixture.nativeElement.querySelector('img');
    expect(image).toBeNull();
    expect(fixture.nativeElement.querySelector('figcaption').textContent)
      .toBe('Product image unavailable.');
  });
});
