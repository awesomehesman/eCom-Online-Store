import { ApplicationRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';
import { StorefrontEntry } from './storefront/storefront-entry';
import { Title } from '@angular/platform-browser';
import { App } from './app';
import { routes } from './app.routes';
import { Subject, from } from 'rxjs';
import { CatalogueApiClient, CatalogueObservation } from './storefront/catalogue-observation';
import { validateCataloguePage } from './storefront/catalogue-page';

describe('Storefront foundation', () => {
  let catalogue: jasmine.SpyObj<CatalogueApiClient>;

  beforeEach(() => {
    catalogue = jasmine.createSpyObj<CatalogueApiClient>('CatalogueApiClient', ['loadPage']);
    catalogue.loadPage.and.callFake(() => from(Promise.resolve({ kind: 'unavailable' } as CatalogueObservation)));
    TestBed.configureTestingModule({ imports: [App], providers: [provideRouter(routes),
      { provide: CatalogueApiClient, useValue: catalogue }] });
  });

  const empty = (): CatalogueObservation => ({ kind: 'page',
    page: validateCataloguePage({ items: [], pageSize: 20, nextCursor: null })! });
  const productPage = (id: string, nextCursor: string | null): CatalogueObservation => ({ kind: 'page',
    page: validateCataloguePage({ pageSize: 20, nextCursor, items: [{
      productId: id, name: `Product ${id}`, description: 'A validated catalogue Product.',
      family: 'Accessories', attributeStandardVersion: '1.0.0', variantDefiningAttributes: [],
      variants: [], media: [],
    }] })! });
  const respond = (observation: CatalogueObservation) => from(Promise.resolve(observation));

  async function open(url = '/') {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    await TestBed.inject(Router).navigateByUrl(url);
    await fixture.whenStable();
    fixture.detectChanges();
    return fixture;
  }


  it('announces a confirmed empty observation and rechecks without moving focus', async () => {
    catalogue.loadPage.and.callFake(() => respond(empty()));
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    await new Promise(resolve => setTimeout(resolve, 0));
    await fixture.whenStable();
    fixture.detectChanges();
    expect(root.querySelector('section [role=status]')?.textContent).toContain('No products are currently available');
    const button = root.querySelector<HTMLButtonElement>('button')!;
    button.focus();
    button.click();
    fixture.detectChanges();
    expect(root.querySelector('section [role=status]')?.textContent).toBe('Checking catalogue…');
    await fixture.whenStable();
    fixture.detectChanges();
    await new Promise(resolve => setTimeout(resolve, 0));
    await fixture.whenStable();
    fixture.detectChanges();
    expect(document.activeElement).toBe(button);
    expect(catalogue.loadPage).toHaveBeenCalledTimes(2);
    expect(root.querySelectorAll('h1').length).toBe(1);
  });

  it('renders validated Product summaries and preserves cursor-bound next-page requests', async () => {
    catalogue.loadPage.and.returnValues(respond(productPage('product-a', 'Opaque_next')), respond(productPage('product-b', null)));
    const fixture = await open();
    fixture.detectChanges();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('article h3')?.textContent).toContain('Product product-a');
    expect(root.querySelector('section [role=status]')?.textContent).toContain('1 products available');
    const next = Array.from(root.querySelectorAll<HTMLButtonElement>('button'))
      .find(button => button.textContent?.includes('Show next products'))!;
    next.focus();
    next.click();
    await fixture.whenStable();
    fixture.detectChanges();
    await new Promise<void>(resolve => queueMicrotask(resolve));
    expect(catalogue.loadPage.calls.allArgs()).toEqual([[20, null], [20, 'Opaque_next']]);
    expect(root.querySelector('article h3')?.textContent).toContain('Product product-b');
    expect(document.activeElement).toBe(root.querySelector('#catalogue-heading'));
  });

  it('fails closed when a continued page regresses across its exclusive Product boundary', async () => {
    catalogue.loadPage.and.returnValues(respond(productPage('product-b', 'Opaque_next')), respond(productPage('product-a', null)));
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const next = Array.from(root.querySelectorAll<HTMLButtonElement>('button'))
      .find(button => button.textContent?.includes('Show next products'))!;
    next.click();
    await fixture.whenStable();
    fixture.detectChanges();
    await new Promise<void>(resolve => queueMicrotask(resolve));
    expect(root.querySelector('article')).toBeNull();
    expect(root.querySelector('section [role=status]')?.textContent).toContain('could not be loaded');
    expect(document.activeElement).toBe(root.querySelector('#catalogue-heading'));
  });

  it('distinguishes unavailable authority from transport or invalid-response errors', async () => {
    catalogue.loadPage.and.returnValue(respond({ kind: 'unavailable' }));
    const fixture = await open();
    expect(fixture.nativeElement.querySelector('section [role=status]').textContent).toContain('could not be confirmed');
    catalogue.loadPage.and.returnValue(respond({ kind: 'error', failure: 'network' }));
    (fixture.debugElement.query(By.directive(StorefrontEntry)).componentInstance as StorefrontEntry).recheck();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('section [role=status]').textContent).toContain('could not be loaded');
    expect(fixture.nativeElement.querySelector('article')).toBeNull();
  });

  it('aborts entry requests on departure and disregards late results', async () => {
    const pending = new Subject<CatalogueObservation>();
    catalogue.loadPage.and.returnValue(pending);
    const fixture = await open();
    await TestBed.inject(Router).navigateByUrl('/missing');
    await fixture.whenStable();
    expect(pending.observed).toBeFalse();
    pending.next({ kind: 'unavailable' });
    await fixture.whenStable();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Page not available');
    expect(fixture.nativeElement.querySelector('#catalogue-heading')).toBeNull();
  });

  it('preserves the newer recheck when an aborted request completes late', async () => {
    const obsolete = new Subject<CatalogueObservation>();
    catalogue.loadPage.and.returnValue(obsolete);
    const fixture = await open();
    catalogue.loadPage.and.returnValue(respond(empty()));
    (fixture.debugElement.query(By.directive(StorefrontEntry)).componentInstance as StorefrontEntry).recheck();
    expect(obsolete.observed).toBeFalse();
    await new Promise(resolve => setTimeout(resolve, 0));
    await fixture.whenStable();
    obsolete.next({ kind: 'unavailable' });
    await fixture.whenStable();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('section [role=status]').textContent).toContain('No products are currently available');
  });

  it('keeps catalogue announcements exclusive during overlapping cancelled navigation', async () => {
    catalogue.loadPage.and.returnValue(respond(empty()));
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const router = TestBed.inject(Router);
    const activeAnnouncements = () => Array.from(root.querySelectorAll('[role=status], [role=alert]'))
      .filter(element => element.textContent?.trim());
    expect(root.querySelector('section [role=status]')?.textContent).toContain('No products are currently available');
    const focused = root.querySelector<HTMLAnchorElement>('nav a')!;
    focused.focus();
    const releases: Array<(allowed: boolean) => void> = [];
    const started: Array<() => void> = [];
    const guards = [0, 1].map(index => new Promise<void>(resolve => { started[index] = resolve; }));
    router.resetConfig(routes.map(route => route.path === '**'
      ? { ...route, canActivate: [() => new Promise<boolean>(resolve => { releases.push(resolve); started[releases.length - 1](); })] } : route));
    const subscription = router.events.subscribe(() => {
      fixture.detectChanges();
      expect(activeAnnouncements().length).toBeLessThanOrEqual(1);
    });
    try {
      const first = router.navigateByUrl('/pending-first');
      await guards[0];
      fixture.detectChanges();
      expect(root.querySelector('section [role=status]')?.textContent).toBe('');
      expect(activeAnnouncements().length).toBe(1);
      const second = router.navigateByUrl('/pending-second');
      await guards[1];
      expect(await first).toBeFalse();
      expect(releases.length).toBe(2);
      fixture.detectChanges();
      expect(root.querySelector('section [role=status]')?.textContent).toBe('');
      releases[1](false);
      expect(await second).toBeFalse();
      releases[0](true);
      await fixture.whenStable();
      fixture.detectChanges();
      expect(router.url).toBe('/');
      expect(activeAnnouncements().length).toBe(1);
      expect(root.querySelector('section [role=status]')?.textContent).toContain('No products are currently available');
      expect(document.activeElement).toBe(focused);
      expect(catalogue.loadPage).toHaveBeenCalledTimes(1);
    } finally {
      subscription.unsubscribe();
    }
  });

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
    expect(skip?.getAttribute('href')).toBe('/#main-content');
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
    expect(root.querySelector('main')?.textContent?.trim()).toContain(
      'Welcome to the storefrontBrowse the catalogue. Purchasing is not available here yet.',
    );
    expect(root.querySelector('form, img, input, [itemscope]')).toBeNull();
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
    const focusedLink = root.querySelector<HTMLAnchorElement>('nav a')!;
    focusedLink.focus();
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
    expect(document.activeElement).toBe(focusedLink);
    expect(Array.from(root.querySelectorAll('[role="status"]')).filter(el => el.textContent?.trim()).length).toBe(1);
    expect(root.querySelectorAll('[role="alert"]').length).toBe(1);
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
    expect(Array.from(root.querySelectorAll('h1')).filter(el => !el.closest('[hidden]')).length).toBe(1);
    expect(Array.from(root.querySelectorAll('[role="status"], [role="alert"]')).filter(el => el.textContent?.trim()).length).toBe(1);
    expect(root.querySelector('nav')).toBe(nav);
    expect(document.activeElement).toBe(root.querySelector('main'));
    expect(root.querySelector('button')?.closest('[hidden]')).not.toBeNull();
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

  it('focuses settled content and title when Angular reuses the wildcard component', async () => {
    const fixture = await open('/missing');
    const root: HTMLElement = fixture.nativeElement;
    root.querySelector<HTMLAnchorElement>('nav a')!.focus();
    const observed: string[] = [];
    root.querySelector('main')!.addEventListener('focus', () => {
      observed.push(root.querySelector('h1')!.textContent + ':' + TestBed.inject(Title).getTitle());
    });
    await TestBed.inject(Router).navigateByUrl('/another-unsupported-address');
    await fixture.whenStable();
    fixture.detectChanges();
    expect(observed).toEqual(['Page not available:Page not available']);
  });

  it('focuses once after a redirect settles rather than during outlet activation', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    root.querySelector<HTMLAnchorElement>('nav a')!.focus();
    const router = TestBed.inject(Router);
    router.resetConfig([{ path: 'redirect', redirectTo: '/missing', pathMatch: 'full' }, ...routes]);
    const focus = spyOn(root.querySelector<HTMLElement>('main')!, 'focus').and.callThrough();
    await router.navigateByUrl('/redirect');
    await fixture.whenStable();
    fixture.detectChanges();
    expect(router.url).toBe('/missing');
    expect(focus).toHaveBeenCalledTimes(1);
    expect(root.querySelector('h1')?.textContent).toBe('Page not available');
    expect(TestBed.inject(Title).getTitle()).toBe('Page not available');
  });

  it('preserves keyboard focus when navigation is cancelled', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const link = root.querySelector<HTMLAnchorElement>('nav a')!;
    link.focus();
    const focus = spyOn(root.querySelector<HTMLElement>('main')!, 'focus').and.callThrough();
    const router = TestBed.inject(Router);
    router.resetConfig([{ path: 'cancel', canActivate: [() => false], loadComponent: routes[0]!.loadComponent }, ...routes]);
    await router.navigateByUrl('/cancel');
    await fixture.whenStable();
    fixture.detectChanges();
    expect(document.activeElement).toBe(link);
    expect(focus).not.toHaveBeenCalled();
  });

  it('keeps obsolete content hidden during cancelled recovery and restores the failure state', async () => {
    const fixture = await open();
    const root: HTMLElement = fixture.nativeElement;
    const router = TestBed.inject(Router);
    let decide!: (allowed: boolean) => void;
    let started!: () => void;
    const gate = new Promise<boolean>(resolve => { decide = resolve; });
    const pending = new Promise<void>(resolve => { started = resolve; });
    router.resetConfig([
      { path: 'broken', loadComponent: () => Promise.reject(new Error('private detail')) },
      { path: 'cancel-recovery', canActivate: [() => { started(); return gate; }], loadComponent: routes[0]!.loadComponent },
      ...routes,
    ]);
    await expectAsync(router.navigateByUrl('/broken')).toBeRejected();
    fixture.detectChanges();
    expect(TestBed.inject(Title).getTitle()).toBe('Page could not open');
    const next = router.navigateByUrl('/cancel-recovery');
    await pending;
    fixture.detectChanges();
    expect(root.querySelector('router-outlet')?.parentElement?.hidden).toBeTrue();
    expect(root.querySelector('h1')?.textContent).toBe('Page could not open');
    expect(root.querySelector('[role="status"]')?.textContent).toBe('Opening page…');
    expect(root.querySelector('[role="alert"]')?.textContent).toBe('');
    decide(false);
    await next;
    await fixture.whenStable();
    fixture.detectChanges();
    expect(root.querySelector('router-outlet')?.parentElement?.hidden).toBeTrue();
    expect(root.querySelector('[role="alert"]')?.textContent).toBe('The requested page could not open.');
    root.querySelector<HTMLAnchorElement>('nav a')!.click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(TestBed.inject(Title).getTitle()).toBe('Storefront');
    expect(document.activeElement).toBe(root.querySelector('main'));
  });

  it('ignores a rejected lazy load after a newer navigation has succeeded', async () => {
    const fixture = await open();
    const router = TestBed.inject(Router);
    let reject!: (reason: Error) => void;
    let started!: () => void;
    const obsolete = new Promise<never>((_, fail) => { reject = fail; });
    const loading = new Promise<void>(resolve => { started = resolve; });
    router.resetConfig([
      { path: 'late-failure', loadComponent: () => { started(); return obsolete; } },
      ...routes,
    ]);
    const oldNavigation = router.navigateByUrl('/late-failure');
    await loading;
    await router.navigateByUrl('/missing');
    reject(new Error('obsolete sensitive failure'));
    expect(await oldNavigation).toBeFalse();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(router.url).toBe('/missing');
    expect(TestBed.inject(Title).getTitle()).toBe('Page not available');
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Page not available');
    expect(fixture.nativeElement.querySelector('[role="alert"]').textContent).toBe('');
    expect(fixture.nativeElement.querySelector('router-outlet').parentElement.hidden).toBeFalse();
  });

  it('keeps native bypass navigation on the current unsupported route and preserves its query', async () => {
    const fixture = await open('/missing?context=preserved');
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('a.skip-link')?.getAttribute('href')).toBe('/missing?context=preserved#main-content');
    expect(root.querySelector('main')?.id).toBe('main-content');
    expect(root.querySelector('h1')?.textContent).toBe('Page not available');
  });

  it('does not refocus main for a fragment-only change on the settled page', async () => {
    const fixture = await open('/missing');
    const root: HTMLElement = fixture.nativeElement;
    const link = root.querySelector<HTMLAnchorElement>('nav a')!;
    link.focus();
    const focus = spyOn(root.querySelector<HTMLElement>('main')!, 'focus').and.callThrough();
    await TestBed.inject(Router).navigateByUrl('/missing#main-content');
    await fixture.whenStable();
    fixture.detectChanges();
    expect(focus).not.toHaveBeenCalled();
    expect(document.activeElement).toBe(link);
    expect(root.querySelector('a.skip-link')?.getAttribute('href')).toBe('/missing#main-content');
  });

});
