import { ApplicationRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { App } from './app';
import { routes } from './app.routes';

describe('Storefront foundation', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [App], providers: [provideRouter(routes)],
  }));

  async function open(url = '/') {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    await TestBed.inject(Router).navigateByUrl(url);
    await fixture.whenStable();
    fixture.detectChanges();
    return fixture;
  }

  it('bootstraps the standalone application shell with the actual route configuration', async () => {
    const host = document.createElement('app-root');
    document.body.append(host);
    const app = TestBed.inject(ApplicationRef);
    const component = app.bootstrap(App, host);
    try {
      await TestBed.inject(Router).navigateByUrl('/');
      await app.whenStable();
      app.tick();
      expect(host.querySelector('h1')?.textContent).toBe('Welcome to the storefront');
    } finally {
      component.destroy();
      host.remove();
    }
  });

  it('loads the storefront entry and sets the document title', async () => {
    const fixture = await open();
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Welcome to the storefront');
    expect(TestBed.inject(Title).getTitle()).toBe('Storefront');
  });

  it('provides semantic landmarks, one main target and one logical page heading', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelectorAll('header').length).toBe(1);
    expect(root.querySelectorAll('main').length).toBe(1);
    expect(root.querySelectorAll('h1').length).toBe(1);
    const skip = root.querySelector<HTMLAnchorElement>('.skip-link');
    expect(skip?.getAttribute('href')).toBe('#main-content');
    expect(skip?.textContent).toBe('Skip to main content');
    expect(root.querySelector('#main-content')?.getAttribute('tabindex')).toBe('-1');
    expect(root.querySelector('[tabindex="1"]')).toBeNull();
  });

  it('moves focus to the main landmark when a route is activated', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    expect(document.activeElement).toBe(root.querySelector('main'));
    await TestBed.inject(Router).navigateByUrl('/missing');
    await fixture.whenStable();
    expect(document.activeElement).toBe(root.querySelector('main'));
  });

  it('states unavailability without fabricating commerce or customer evidence', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('main')?.textContent?.trim()).toBe(
      'Welcome to the storefrontProduct browsing and purchasing are not available here yet.',
    );
    expect(root.querySelector('button, form, img, input, [itemscope]')).toBeNull();
    expect(root.textContent).not.toMatch(/R\s*\d|ZAR|in stock|sale|discount|signed in|cart \(0\)/i);
    expect(root.querySelectorAll('a').length).toBe(1);
  });

  it('distinguishes unsupported addresses and offers a working return destination', async () => {
    const fixture = await open('/not-an-established-destination');
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Page not available');
    expect(TestBed.inject(Title).getTitle()).toBe('Page not available');
    fixture.nativeElement.querySelector('main a').click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(TestBed.inject(Router).url).toBe('/');
  });
});
