import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../../shared/components/product-card/product-card';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
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
  private route = inject(ActivatedRoute);

  products = signal<Product[]>([]);
  loading = signal(true);
  activeCategory = signal<Category | null>(null);

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
    this.cartService.addItem(product.id, 1).subscribe();
  }
}
