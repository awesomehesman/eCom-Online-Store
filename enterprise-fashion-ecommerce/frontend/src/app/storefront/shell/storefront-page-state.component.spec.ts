import { TestBed } from '@angular/core/testing';

import { StorefrontPageStateComponent } from './storefront-page-state.component';

describe('Storefront page state', () => {
  it('renders the exact accessible page heading and description for its state', () => {
    const fixture = TestBed.createComponent(StorefrontPageStateComponent);
    fixture.componentRef.setInput('state', 'navigation-failed');
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('h1').length).toBe(1);
    expect(fixture.nativeElement.querySelector('h1').textContent).toBe('Page could not open');
    expect(fixture.nativeElement.querySelector('p').textContent)
      .toBe('Please use the Storefront link to return to the entry page.');
  });
});
