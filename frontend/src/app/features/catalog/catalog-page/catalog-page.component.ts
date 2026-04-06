import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../../shared/components/product-card/product-card';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { Category, Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ProductCardComponent],
  templateUrl: './catalog-page.html',
})
export class CatalogPageComponent implements OnInit {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  products = signal<Product[]>([]);
  loading = signal(true);
  activeCategory = signal<Category | null>(null);
  addedProductId = signal<number | null>(null);

  categories: { label: string; value: Category }[] = [
    { label: 'Flowers',   value: 'FLOWERS' },
    { label: 'Bracelets', value: 'BRACELETS' },
    { label: 'Trinkets',  value: 'TRINKETS' },
    { label: 'Seasonal',  value: 'SEASONAL' },
  ];

  ngOnInit() {
    const category = this.route.snapshot.data['category'] as Category | undefined;
    this.activeCategory.set(category ?? null);
    this.loadProducts(category ?? null);
  }

  loadProducts(category: Category | null) {
    this.loading.set(true);
    const obs = category
      ? this.productService.getByCategory(category)
      : this.productService.getAll();
    obs.subscribe(products => {
      this.products.set(products);
      this.loading.set(false);
    });
  }

  selectCategory(category: Category | null) {
    this.activeCategory.set(category);
    this.loadProducts(category);
  }

  onAddToCart(product: Product) {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/account'], { queryParams: { redirect: 'cart' } });
      return;
    }
    this.cartService.addItem(product.id, 1).subscribe({
      next: () => {
        this.addedProductId.set(product.id);
        setTimeout(() => this.addedProductId.set(null), 1500);
      },
    });
  }
}
