import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, CurrencyPipe, FormsModule],
  templateUrl: './product-page.html',
})
export class ProductPageComponent implements OnInit {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  product = signal<Product | null>(null);
  quantity = signal(1);
  added = signal(false);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getById(id).subscribe(p => this.product.set(p));
  }

  increment() { if (this.quantity() < (this.product()?.stock ?? 1)) this.quantity.update(n => n + 1); }
  decrement() { if (this.quantity() > 1) this.quantity.update(n => n - 1); }

  addToCart() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/account'], { queryParams: { redirect: 'cart' } });
      return;
    }
    const p = this.product();
    if (!p) return;
    this.cartService.addItem(p.id, this.quantity()).subscribe(() => {
      this.added.set(true);
      setTimeout(() => this.added.set(false), 2000);
    });
  }
}
