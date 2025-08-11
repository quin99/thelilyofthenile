import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home-page/home-page.component')
        .then(c => c.HomePageComponent),
  },
  {
    path: 'flowers',
    loadComponent: () =>
      import('./features/catalog/catalog-page/catalog-page.component')
        .then(c => c.CatalogPageComponent),
  },
  {
    path: 'product/:slug',
    loadComponent: () =>
      import('./features/product/product-page/product-page.component')
        .then(c => c.ProductPageComponent),
  },
  {
    path: 'cart',
    loadComponent: () =>
      import('./features/cart/cart-page/cart-page.component')
        .then(c => c.CartPageComponent),
  },
  {
    path: 'checkout',
    loadComponent: () =>
      import('./features/checkout/checkout-page/checkout-page.component')
        .then(c => c.CheckoutPageComponent),
  },
  {
    path: 'account',
    loadComponent: () =>
      import('./features/account/account-page/account-page.component')
        .then(c => c.AccountPageComponent),
  },
  { path: '**', redirectTo: '' },
];
