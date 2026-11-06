import { ChangeDetectionStrategy, Component, input } from '@angular/core';

// Presentation-only states that the current storefront can actually establish.
export type StorefrontPageState = 'commerce-unavailable' | 'invalid-destination' | 'navigation-failed';

const messages = {
  'commerce-unavailable': {
    heading: 'Welcome to the storefront',
    description: 'Product browsing and purchasing are not available here yet.',
  },
  'invalid-destination': {
    heading: 'Page not available',
    description: 'This address does not have an available page.',
  },
  'navigation-failed': {
    heading: 'Page could not open',
    description: 'Please use the Storefront link to return to the entry page.',
  },
} as const;

@Component({
  selector: 'app-storefront-page-state',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1>{{ messages[state()].heading }}</h1>
    <p>{{ messages[state()].description }}</p>
    <ng-content />
  `,
})
export class StorefrontPageStateComponent {
  readonly state = input.required<StorefrontPageState>();
  protected readonly messages = messages;
}
