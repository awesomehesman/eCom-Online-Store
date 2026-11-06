import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    title: 'Storefront',
    loadComponent: () => import('./storefront/storefront-entry').then(m => m.StorefrontEntry),
  },
  {
    path: '**',
    title: 'Page not available',
    loadComponent: () => import('./storefront/page-not-found').then(m => m.PageNotFound),
  },
];
