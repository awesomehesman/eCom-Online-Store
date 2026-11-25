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
    path: 'products/:productId',
    title: 'Product details',
    loadComponent: () => import('./storefront/features/catalogue/pages/product-detail/product-detail.component')
      .then(m => m.ProductDetailComponent),
  },
  {
    path: '**',
    title: 'Page not available',
    loadComponent: () => import('./pages/not-found/page-not-found.component').then(m => m.PageNotFound),
  },
];
