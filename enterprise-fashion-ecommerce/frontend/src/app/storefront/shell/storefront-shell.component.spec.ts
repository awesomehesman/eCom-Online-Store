import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { StorefrontShell } from './storefront-shell.component';

describe('Storefront shell', () => {
  it('owns the storefront landmark, native skip link and focus target', async () => {
    TestBed.configureTestingModule({ imports: [StorefrontShell], providers: [provideRouter([])] });
    const fixture = TestBed.createComponent(StorefrontShell);
    fixture.detectChanges();
    await TestBed.inject(Router).navigateByUrl('/');
    await fixture.whenStable();
    fixture.detectChanges();
    const skip: HTMLAnchorElement = fixture.nativeElement.querySelector('.skip-link');
    const main: HTMLElement = fixture.nativeElement.querySelector('main');
    expect(skip.getAttribute('href')).toBe('/#main-content');
    expect(fixture.nativeElement.querySelector('nav').getAttribute('aria-label')).toBe('Storefront');
    expect(main.id).toBe('main-content');
    expect(main.tabIndex).toBe(-1);
    fixture.componentInstance.focusMain();
    expect(document.activeElement).toBe(main);
  });
});
