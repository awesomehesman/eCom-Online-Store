import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    title: 'Storefront',
    loadComponent: () => import('./storefront/features/catalogue/pages/catalogue-page/catalogue-page.component')
      .then(m => m.CataloguePageComponent),
  },
  {
    path: '**',
    title: 'Page not available',
    loadComponent: () => import('./pages/not-found/page-not-found.component').then(m => m.PageNotFound),
  },
];
