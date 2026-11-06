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

  it('marks only the established entry as current and preserves the shell across navigation', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const main = root.querySelector('main');
    const nav = root.querySelector('nav');
    expect(nav?.getAttribute('aria-label')).toBe('Storefront');
    expect(nav?.querySelector('a')?.getAttribute('aria-current')).toBe('page');
    await TestBed.inject(Router).navigateByUrl('/missing');
    await fixture.whenStable();
    fixture.detectChanges();
    expect(root.querySelector('main')).toBe(main);
    expect(root.querySelector('nav')).toBe(nav);
    expect(nav?.querySelector('[aria-current]')).toBeNull();
    expect(root.querySelectorAll('h1').length).toBe(1);
    expect(root.querySelectorAll('main').length).toBe(1);
  });

  it('uses native links and no positive tab stops or keyboard interception', async () => {
    const fixture = await open('/missing');
    const root: HTMLElement = fixture.nativeElement;
    for (const link of Array.from(root.querySelectorAll('a'))) {
      expect(link.getAttribute('href')).toBeTruthy();
      expect(link.getAttribute('role')).toBeNull();
      const key = new KeyboardEvent('keydown', { key: 'Tab', bubbles: true, cancelable: true });
      link.dispatchEvent(key);
      expect(key.defaultPrevented).toBeFalse();
    }
    expect(Array.from(root.querySelectorAll('[tabindex]')).some(el => Number(el.getAttribute('tabindex')) > 0)).toBeFalse();
  });

  it('states unavailability without fabricating commerce or customer evidence', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('main')?.textContent?.trim()).toBe(
      'Welcome to the storefrontProduct browsing and purchasing are not available here yet.',
    );
    expect(root.querySelector('button, form, img, input, [itemscope]')).toBeNull();
    expect(root.textContent).not.toMatch(/R\s*\d|ZAR|in stock|sale|discount|signed in|cart \(0\)/i);
    expect(root.querySelectorAll('a').length).toBe(2);
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
  it('announces pending route loading without claiming empty or failed commerce', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const router = TestBed.inject(Router);
    let release!: () => void;
    let started!: () => void;
    const pending = new Promise<void>(resolve => { release = resolve; });
    const loading = new Promise<void>(resolve => { started = resolve; });
    router.resetConfig([
      { path: 'slow', loadComponent: async () => {
        started();
        await pending;
        return (await import('./storefront/storefront-entry')).StorefrontEntry;
      } },
      ...routes,
    ]);
    const navigation = router.navigateByUrl('/slow');
    await loading;
    fixture.detectChanges();
    const status = root.querySelector('[role="status"]');
    expect(status?.textContent).toBe('Opening page…');
    expect(status?.getAttribute('aria-atomic')).toBe('true');
    expect(root.querySelector('[role="alert"]')?.textContent).toBe('');
    expect(root.textContent).not.toMatch(/empty catalogue|no products found|unknown outcome/i);
    release();
    await navigation;
    await fixture.whenStable();
    fixture.detectChanges();
    expect(root.querySelector('[role="status"]')).toBe(status);
    expect(status?.textContent).toBe('');
    expect(root.querySelector('h1')?.textContent).toBe('Welcome to the storefront');
  });

  it('contains route-load failure and keeps home navigation operable without leaking errors', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const router = TestBed.inject(Router);
    const nav = root.querySelector('nav');
    router.resetConfig([
      { path: 'broken', loadComponent: () => Promise.reject(new Error('sensitive internal detail')) },
      ...routes,
    ]);
    await expectAsync(router.navigateByUrl('/broken')).toBeRejected();
    fixture.detectChanges();
    expect(root.querySelector('app-storefront-page-state[state="navigation-failed"] h1')?.textContent).toBe('Page could not open');
    expect(root.querySelector('[role="alert"]')?.textContent).toBe('The requested page could not open.');
    expect(root.querySelector('[role="status"]')?.textContent).toBe('');
    expect(root.textContent).not.toContain('sensitive internal detail');
    expect(root.querySelector('router-outlet')?.parentElement?.hidden).toBeTrue();
    expect(root.querySelector('nav')).toBe(nav);
    expect(document.activeElement).toBe(root.querySelector('main'));
    expect(root.querySelector('button')).toBeNull();
    root.querySelector<HTMLAnchorElement>('nav a')!.click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(router.url).toBe('/');
    expect(root.querySelector('[role="alert"]')?.textContent).toBe('');
    expect(root.querySelector('router-outlet')?.parentElement?.hidden).toBeFalse();
    expect(root.querySelector('h1')?.textContent).toBe('Welcome to the storefront');
  });

  it('clears pending presentation on cancellation without inventing an error or authorization result', async () => {
    const fixture = await open();
    const router = TestBed.inject(Router);
    router.resetConfig([
      { path: 'cancelled', canActivate: [() => false], loadComponent: routes[0]!.loadComponent },
      ...routes,
    ]);
    expect(await router.navigateByUrl('/cancelled')).toBeFalse();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[role="status"]').textContent).toBe('');
    expect(fixture.nativeElement.querySelector('[role="alert"]').textContent).toBe('');
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Welcome to the storefront');
  });

  it('does not let superseded lazy work replace the newer destination', async () => {
    const fixture = await open();
    const router = TestBed.inject(Router);
    let release!: () => void;
    let started!: () => void;
    const pending = new Promise<void>(resolve => { release = resolve; });
    const loading = new Promise<void>(resolve => { started = resolve; });
    router.resetConfig([
      { path: 'obsolete', loadComponent: async () => {
        started();
        await pending;
        return (await import('./storefront/storefront-entry')).StorefrontEntry;
      } },
      ...routes,
    ]);
    const oldNavigation = router.navigateByUrl('/obsolete');
    await loading;
    await router.navigateByUrl('/missing');
    release();
    expect(await oldNavigation).toBeFalse();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(router.url).toBe('/missing');
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Page not available');
    expect(fixture.nativeElement.querySelector('[role="status"]').textContent).toBe('');
    expect(fixture.nativeElement.querySelector('[role="alert"]').textContent).toBe('');
  });

});
