import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { CartService } from '../../../core/services/cart.service';
import { CartItem } from '../../../core/models/cart-item.model';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './checkout-page.html',
})
export class CheckoutPageComponent implements OnInit {
  private cartService = inject(CartService);

  items = signal<CartItem[]>([]);

  get total(): number {
    return this.items().reduce((sum, i) => sum + i.product.price * i.quantity, 0);
  }

  ngOnInit() {
    this.cartService.getCart().subscribe(items => this.items.set(items));
  }
}
