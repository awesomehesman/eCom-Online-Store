import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { PageNotFound } from './page-not-found.component';

describe('Not Found page', () => {
  it('provides an accessible heading and native storefront recovery link', async () => {
    TestBed.configureTestingModule({ imports: [PageNotFound], providers: [provideRouter([])] });
    const fixture = TestBed.createComponent(PageNotFound);
    fixture.detectChanges();
    const heading: HTMLHeadingElement = fixture.nativeElement.querySelector('h1');
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a');
    expect(heading.textContent).toBe('Page not available');
    expect(link.textContent).toBe('Return to the storefront');
    expect(link.getAttribute('href')).toBe('/');
    link.click();
    await fixture.whenStable();
    expect(TestBed.inject(Router).url).toBe('/');
  });
});
