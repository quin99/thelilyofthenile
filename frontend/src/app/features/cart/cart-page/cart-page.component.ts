import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { CartService } from '../../../core/services/cart.service';
import { CartItem } from '../../../core/models/cart-item.model';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './cart-page.html',
})
export class CartPageComponent implements OnInit {
  private cartService = inject(CartService);

  items = signal<CartItem[]>([]);
  loading = signal(true);

  get total(): number {
    return this.items().reduce((sum, i) => sum + i.product.price * i.quantity, 0);
  }

  ngOnInit() {
    this.cartService.getCart().subscribe(items => {
      this.items.set(items);
      this.loading.set(false);
    });
  }

  removeItem(productId: number) {
    this.cartService.removeItem(productId).subscribe(() => {
      this.items.update(items => items.filter(i => i.product.id !== productId));
    });
  }
}
