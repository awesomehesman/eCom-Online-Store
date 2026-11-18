import { ChangeDetectionStrategy, Component, input } from '@angular/core';

// Presentation-only states that the current storefront can actually establish.
export type StorefrontPageState = 'catalogue' | 'commerce-unavailable' | 'invalid-destination' | 'navigation-failed';

const messages = {
  catalogue: {
    heading: 'Welcome to the storefront',
    description: 'Browse the catalogue. Purchasing is not available here yet.',
  },
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
  templateUrl: './storefront-page-state.component.html',
  styleUrl: './storefront-page-state.component.scss',
})
export class StorefrontPageStateComponent {
  readonly state = input.required<StorefrontPageState>();
  protected readonly messages = messages;
}
