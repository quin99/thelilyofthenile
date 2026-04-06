import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home-page/home-page.component').then(c => c.HomePageComponent),
  },
  {
    path: 'flowers',
    loadComponent: () =>
      import('./features/catalog/catalog-page/catalog-page.component').then(c => c.CatalogPageComponent),
    data: { category: 'FLOWERS' },
  },
  {
    path: 'bracelets',
    loadComponent: () =>
      import('./features/catalog/catalog-page/catalog-page.component').then(c => c.CatalogPageComponent),
    data: { category: 'BRACELETS' },
  },
  {
    path: 'trinkets',
    loadComponent: () =>
      import('./features/catalog/catalog-page/catalog-page.component').then(c => c.CatalogPageComponent),
    data: { category: 'TRINKETS' },
  },
  {
    path: 'seasonal',
    loadComponent: () =>
      import('./features/catalog/catalog-page/catalog-page.component').then(c => c.CatalogPageComponent),
    data: { category: 'SEASONAL' },
  },
  {
    path: 'product/:id',
    loadComponent: () =>
      import('./features/product/product-page/product-page.component').then(c => c.ProductPageComponent),
  },
  {
    path: 'cart',
    loadComponent: () =>
      import('./features/cart/cart-page/cart-page.component').then(c => c.CartPageComponent),
  },
  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/checkout/checkout-page/checkout-page.component').then(c => c.CheckoutPageComponent),
  },
  {
    path: 'account',
    loadComponent: () =>
      import('./features/account/account-page/account-page.component').then(c => c.AccountPageComponent),
  },
  {
    path: '**',
    loadComponent: () =>
      import('./features/not-found/not-found.component').then(c => c.NotFoundComponent),
  },
];
